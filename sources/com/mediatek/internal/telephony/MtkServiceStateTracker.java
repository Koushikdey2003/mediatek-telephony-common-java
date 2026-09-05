package com.mediatek.internal.telephony;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.radio.V1_5.RegStateResult;
import android.hardware.radio.network.AccessTechnologySpecificInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.AccessNetworkUtils;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.LteVopsSupportInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.HbpcdUtils;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.NetworkRegistrationManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdnr.CarrierDisplayNameData;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.data.MtkDataNetworkController;
import com.mediatek.internal.telephony.uicc.MtkSpnOverride;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimConstants;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/* JADX INFO: loaded from: classes.dex */
public class MtkServiceStateTracker extends ServiceStateTracker {
    private static final int CN_OP_ID_CBN = 4;
    private static final int CN_OP_ID_CMCC = 1;
    private static final int CN_OP_ID_CT = 3;
    private static final int CN_OP_ID_CU = 2;
    private static final int CN_OP_ID_UNKNOWN = 0;
    private static final boolean DBG = true;
    protected static final int EVENT_FEMTO_CELL_INFO = 102;
    protected static final int EVENT_GET_SIGNAL_STRENGTH = 124;
    protected static final int EVENT_IWLAN_PREFERRED_CHANGED = 125;
    protected static final int EVENT_MODULATION_INFO = 105;
    protected static final int EVENT_NETWORK_EVENT = 104;
    protected static final int EVENT_PS_NETWORK_STATE_CHANGED = 103;
    protected static final int EVENT_RECHECK_NRM_STATUS = 123;
    protected static final int EVENT_RESTART_TURBO = 122;
    protected static final int EVENT_RIL_READY = 120;
    protected static final int EVENT_SIM_OPL_LOADED = 119;
    protected static final int EVENT_UPDATE_PLMN = 121;
    private static final String LOG_TAG = "MTKSST";
    protected static final String PROP_IWLAN_STATE = "persist.vendor.radio.wfc_state";
    private static final boolean VDBG = true;
    private static final long mCheckNrmPeriod = 1000;
    private boolean hasPendingPollState;
    private SparseArray<int[]> mCnShareNwConfig;
    private String mCsgId;
    private RegistrantList mDataRoamingTypeChangedRegistrants;
    private boolean mEnableERI;
    private String mFemtoAct;
    private String mFemtoPlmn;
    private int mFemtocellDomain;
    private String mHhbName;
    private int mIsFemtocell;
    private boolean mIsTurboSSAlive;
    private String mLocatedPlmn;
    private BroadcastReceiver mMtkIntentReceiver;
    private IServiceStateTrackerExt mServiceStateTrackerExt;
    private OpTelephonyCustomizationFactoryBase mTelephonyCustomizationFactory;
    private Handler mtkHandler;
    private HandlerThread mtkHandlerThread;
    protected NrMap nm;
    protected boolean once_pollState_done;
    private ServiceState turboSS;
    private SignalStrength turboSig;

    private class MtkPlmn {
        String plmn;
        boolean showPlmn;
        boolean showSpn;
        String spn;

        public MtkPlmn(boolean showPlmn, String plmn, boolean showSpn, String spn) {
            this.showPlmn = showPlmn;
            this.plmn = plmn == null ? null : new String(plmn);
            this.showSpn = showSpn;
            this.spn = spn != null ? new String(spn) : null;
        }

        public String toString() {
            return "MtkPlmn showPlmn=" + this.showPlmn + " plmn=" + this.plmn + " showSpn=" + this.showSpn + " spn=" + this.spn;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTurboPLMN() {
        updatePLMN(false, null, false, null);
    }

    private void updatePLMN(boolean showPlmn, String plmn, boolean showSpn, String spn) {
        MtkPlmn mtkplmn = new MtkPlmn(showPlmn, plmn, showSpn, spn);
        this.mtkHandler.sendMessage(obtainMessage(EVENT_UPDATE_PLMN, mtkplmn));
    }

    private class MtkHandler extends Handler {
        private boolean pending;
        private boolean polling;
        private boolean stop;

        public MtkHandler(Looper looper) {
            super(looper);
            this.stop = false;
            this.polling = false;
            this.pending = false;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int regState;
            int reasonForDenial;
            int reasonForDenial2;
            boolean isEndcAvailable;
            boolean isNrAvailable;
            boolean isDcNrRestricted;
            switch (msg.what) {
                case 2:
                case MtkServiceStateTracker.EVENT_RESTART_TURBO /* 122 */:
                    break;
                case 4:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    Object result = ar.result;
                    if (ar.exception != null || result == null || (!(result instanceof RegStateResult) && !(result instanceof android.hardware.radio.V1_6.RegStateResult) && !(result instanceof android.hardware.radio.network.RegStateResult))) {
                        MtkServiceStateTracker.this.log("Turbo stop due to wrong object");
                        this.polling = false;
                        return;
                    }
                    int networkType = 0;
                    int regState2 = 1;
                    int reasonForDenial3 = 0;
                    if (result instanceof RegStateResult) {
                        RegStateResult voiceRegState = (RegStateResult) result;
                        networkType = ServiceState.rilRadioTechnologyToNetworkType(voiceRegState.rat);
                        int regState3 = voiceRegState.regState;
                        int reasonForDenial4 = voiceRegState.reasonForDenial;
                        regState = regState3;
                        reasonForDenial = reasonForDenial4;
                    } else {
                        if (result instanceof android.hardware.radio.V1_6.RegStateResult) {
                            android.hardware.radio.V1_6.RegStateResult voiceRegState2 = (android.hardware.radio.V1_6.RegStateResult) result;
                            networkType = ServiceState.rilRadioTechnologyToNetworkType(voiceRegState2.rat);
                            regState2 = voiceRegState2.regState;
                            reasonForDenial3 = voiceRegState2.reasonForDenial;
                        } else if (result instanceof android.hardware.radio.network.RegStateResult) {
                            android.hardware.radio.network.RegStateResult voiceRegState3 = (android.hardware.radio.network.RegStateResult) result;
                            networkType = ServiceState.rilRadioTechnologyToNetworkType(voiceRegState3.rat);
                            int regState4 = voiceRegState3.regState >= 10 ? voiceRegState3.regState - 10 : voiceRegState3.regState;
                            int reasonForDenial5 = voiceRegState3.reasonForDenial;
                            regState = regState4;
                            reasonForDenial = reasonForDenial5;
                        }
                        regState = regState2;
                        reasonForDenial = reasonForDenial3;
                    }
                    if (networkType == 19) {
                        networkType = 13;
                    }
                    NetworkRegistrationInfo networkRegState = new NetworkRegistrationInfo(1, 1, regState, networkType, reasonForDenial, false, null, null, "", false, 0, 0, 0);
                    int registrationState = networkRegState.getNetworkRegistrationState();
                    MtkServiceStateTracker.this.turboSS.setVoiceRegState(MtkServiceStateTracker.this.regCodeToServiceState(registrationState));
                    MtkServiceStateTracker.this.turboSS.addNetworkRegistrationInfo(networkRegState);
                    if ((MtkServiceStateTracker.this.turboSS.getVoiceRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getVoiceRoaming()) || (MtkServiceStateTracker.this.turboSS.getDataRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getDataRoaming())) {
                        if (SubscriptionManager.isValidSubscriptionId(MtkServiceStateTracker.this.mPhone.getSubId())) {
                            MtkServiceStateTracker.this.updateTurboPLMN();
                        }
                        MtkServiceStateTracker.this.log("MtkHandler: stop MTK turbo.");
                        this.stop = true;
                        MtkServiceStateTracker.this.mCi.unregisterForNetworkStateChanged(MtkServiceStateTracker.this.mtkHandler);
                        if (MtkServiceStateTracker.this.turboSS.getDataRegState() == 0) {
                            this.pending = false;
                        }
                    }
                    this.polling = false;
                    if (this.pending) {
                        this.pending = false;
                        MtkServiceStateTracker.this.mtkHandler.sendEmptyMessage(MtkServiceStateTracker.EVENT_RESTART_TURBO);
                        return;
                    }
                    return;
                case 5:
                    AsyncResult ar2 = (AsyncResult) msg.obj;
                    Object result2 = ar2.result;
                    if (ar2.exception != null || result2 == null || (!(result2 instanceof RegStateResult) && !(result2 instanceof android.hardware.radio.V1_6.RegStateResult) && !(result2 instanceof android.hardware.radio.network.RegStateResult))) {
                        MtkServiceStateTracker.this.log("Turbo stop due to wrong object");
                        return;
                    }
                    int networkType2 = 0;
                    int regState5 = 1;
                    int reasonForDenial6 = 0;
                    boolean isEndcAvailable2 = false;
                    boolean isNrAvailable2 = false;
                    boolean isDcNrRestricted2 = false;
                    if (result2 instanceof RegStateResult) {
                        RegStateResult dataRegState = (RegStateResult) result2;
                        networkType2 = ServiceState.rilRadioTechnologyToNetworkType(dataRegState.rat);
                        regState5 = dataRegState.regState;
                        int reasonForDenial7 = dataRegState.reasonForDenial;
                        if (dataRegState.accessTechnologySpecificInfo.getDiscriminator() == 2) {
                            RegStateResult.AccessTechnologySpecificInfo.EutranRegistrationInfo eutranInfo = dataRegState.accessTechnologySpecificInfo.eutranInfo();
                            isDcNrRestricted2 = eutranInfo.nrIndicators.isDcNrRestricted;
                            isNrAvailable2 = eutranInfo.nrIndicators.isNrAvailable;
                            isEndcAvailable2 = eutranInfo.nrIndicators.isEndcAvailable;
                        }
                        reasonForDenial2 = reasonForDenial7;
                        isEndcAvailable = isEndcAvailable2;
                        isNrAvailable = isNrAvailable2;
                        isDcNrRestricted = isDcNrRestricted2;
                    } else {
                        if (result2 instanceof android.hardware.radio.V1_6.RegStateResult) {
                            android.hardware.radio.V1_6.RegStateResult dataRegState2 = (android.hardware.radio.V1_6.RegStateResult) result2;
                            networkType2 = ServiceState.rilRadioTechnologyToNetworkType(dataRegState2.rat);
                            regState5 = dataRegState2.regState;
                            reasonForDenial6 = dataRegState2.reasonForDenial;
                            if (dataRegState2.accessTechnologySpecificInfo.getDiscriminator() == 2) {
                                RegStateResult.AccessTechnologySpecificInfo.EutranRegistrationInfo eutranInfo2 = dataRegState2.accessTechnologySpecificInfo.eutranInfo();
                                isDcNrRestricted2 = eutranInfo2.nrIndicators.isDcNrRestricted;
                                isNrAvailable2 = eutranInfo2.nrIndicators.isNrAvailable;
                                isEndcAvailable2 = eutranInfo2.nrIndicators.isEndcAvailable;
                            }
                        } else if (result2 instanceof android.hardware.radio.network.RegStateResult) {
                            android.hardware.radio.network.RegStateResult dataRegState3 = (android.hardware.radio.network.RegStateResult) result2;
                            networkType2 = ServiceState.rilRadioTechnologyToNetworkType(dataRegState3.rat);
                            regState5 = dataRegState3.regState >= 10 ? dataRegState3.regState - 10 : dataRegState3.regState;
                            int reasonForDenial8 = dataRegState3.reasonForDenial;
                            AccessTechnologySpecificInfo info = dataRegState3.accessTechnologySpecificInfo;
                            if (info.getTag() != 2) {
                                reasonForDenial2 = reasonForDenial8;
                                isEndcAvailable = false;
                                isNrAvailable = false;
                                isDcNrRestricted = false;
                            } else {
                                boolean isDcNrRestricted3 = info.getEutranInfo().nrIndicators.isDcNrRestricted;
                                boolean isNrAvailable3 = info.getEutranInfo().nrIndicators.isNrAvailable;
                                boolean isEndcAvailable3 = info.getEutranInfo().nrIndicators.isEndcAvailable;
                                reasonForDenial2 = reasonForDenial8;
                                isEndcAvailable = isEndcAvailable3;
                                isNrAvailable = isNrAvailable3;
                                isDcNrRestricted = isDcNrRestricted3;
                            }
                        }
                        reasonForDenial2 = reasonForDenial6;
                        isEndcAvailable = isEndcAvailable2;
                        isNrAvailable = isNrAvailable2;
                        isDcNrRestricted = isDcNrRestricted2;
                    }
                    if (networkType2 == 19) {
                        networkType2 = 13;
                    }
                    NetworkRegistrationInfo networkRegState2 = new NetworkRegistrationInfo(2, 1, regState5, networkType2, reasonForDenial2, false, null, null, "", 16, isDcNrRestricted, isNrAvailable, isEndcAvailable, new LteVopsSupportInfo(1, 1));
                    int registrationState2 = networkRegState2.getNetworkRegistrationState();
                    int serviceState = MtkServiceStateTracker.this.regCodeToServiceState(registrationState2);
                    MtkServiceStateTracker.this.turboSS.setDataRegState(serviceState);
                    MtkServiceStateTracker.this.turboSS.addNetworkRegistrationInfo(networkRegState2);
                    return;
                case 7:
                    AsyncResult ar3 = (AsyncResult) msg.obj;
                    if (ar3.result instanceof String[]) {
                        String[] opNames = (String[]) ar3.result;
                        if (opNames != null && opNames.length >= 3) {
                            MtkServiceStateTracker.this.turboSS.setOperatorName(opNames[0], opNames[1], opNames[2]);
                            return;
                        }
                        return;
                    }
                    MtkServiceStateTracker.this.log("Turbo EVENT OPERATOR wrong object");
                    return;
                case MtkServiceStateTracker.EVENT_RIL_READY /* 120 */:
                    MtkServiceStateTracker.this.mCi.registerForNetworkStateChanged(MtkServiceStateTracker.this.mtkHandler, 2, (Object) null);
                    break;
                case MtkServiceStateTracker.EVENT_UPDATE_PLMN /* 121 */:
                    MtkPlmn mtkplmn = (MtkPlmn) msg.obj;
                    int subId = MtkServiceStateTracker.this.mPhone.getSubId();
                    boolean connected = ((NetworkRegistrationManager) MtkServiceStateTracker.this.mRegStateManagers.get(1)).isServiceConnected();
                    if (connected) {
                        if (mtkplmn == null) {
                            MtkServiceStateTracker.this.log("EVENT_UPDATE_PLMN should not be happened here");
                            return;
                        }
                        if (MtkServiceStateTracker.this.isTurboSSAlive() && !mtkplmn.showPlmn && !mtkplmn.showSpn) {
                            MtkServiceStateTracker.this.log("updateTurboPLMN comes after connected, skip");
                            return;
                        }
                        MtkServiceStateTracker.this.log("MtkHandler: EVENT_UPDATE_PLMN " + mtkplmn);
                        if (SubscriptionManager.isValidSubscriptionId(subId)) {
                            MtkServiceStateTracker.this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(MtkServiceStateTracker.this.getCarrierName(mtkplmn.showPlmn, mtkplmn.plmn, mtkplmn.showSpn, mtkplmn.spn)));
                            return;
                        }
                        return;
                    }
                    if (SubscriptionManager.isValidSubscriptionId(subId)) {
                        if ((MtkServiceStateTracker.this.turboSS.getVoiceRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getVoiceRoaming()) || (MtkServiceStateTracker.this.turboSS.getDataRegState() == 0 && !MtkServiceStateTracker.this.turboSS.getDataRoaming())) {
                            int display_type = MtkServiceStateTracker.this.turboSS.getDataNetworkType();
                            int displayNetworkType = 0;
                            if ((display_type == 13 || display_type == 19) && MtkServiceStateTracker.this.turboSS.getNrState() != 0 && MtkServiceStateTracker.this.turboSS.getNrState() != 1) {
                                displayNetworkType = 3;
                            }
                            TelephonyDisplayInfo displayInfo = new TelephonyDisplayInfo(display_type, displayNetworkType);
                            MtkServiceStateTracker.this.log("updateTurboPLMN turboSS=" + MtkServiceStateTracker.this.turboSS + " displayInfo=" + displayInfo);
                            SubscriptionManagerService subscriptionManagerService = MtkServiceStateTracker.this.mSubscriptionManagerService;
                            MtkServiceStateTracker mtkServiceStateTracker = MtkServiceStateTracker.this;
                            subscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(mtkServiceStateTracker.getCarrierName(true, mtkServiceStateTracker.turboSS.getOperatorAlpha(), false, "")));
                            ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).notifyMtkServiceStateChanged(MtkServiceStateTracker.this.turboSS);
                            ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).notifyDisplayInfoChanged(displayInfo);
                            return;
                        }
                        if (mtkplmn.plmn == null && mtkplmn.spn == null) {
                            MtkServiceStateTracker.this.logv("it's updateTurboPLMN when turboSS is OOS, skip");
                            return;
                        } else {
                            MtkServiceStateTracker.this.log("update aosp PLMN mtkplmn=" + mtkplmn);
                            MtkServiceStateTracker.this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(MtkServiceStateTracker.this.getCarrierName(mtkplmn.showPlmn, mtkplmn.plmn, mtkplmn.showSpn, mtkplmn.spn)));
                            return;
                        }
                    }
                    return;
                case MtkServiceStateTracker.EVENT_RECHECK_NRM_STATUS /* 123 */:
                    boolean connected2 = ((NetworkRegistrationManager) MtkServiceStateTracker.this.mRegStateManagers.get(1)).isServiceConnected();
                    MtkServiceStateTracker.this.log("EVENT_RECHECK_NRM_STATUS connected=" + connected2);
                    if (connected2) {
                        ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).getServiceStateTracker().sendEmptyMessage(MtkServiceStateTracker.EVENT_RECHECK_NRM_STATUS);
                        return;
                    } else {
                        MtkServiceStateTracker.this.mtkHandler.sendEmptyMessageDelayed(MtkServiceStateTracker.EVENT_RECHECK_NRM_STATUS, MtkServiceStateTracker.mCheckNrmPeriod);
                        return;
                    }
                case MtkServiceStateTracker.EVENT_GET_SIGNAL_STRENGTH /* 124 */:
                    AsyncResult ar4 = (AsyncResult) msg.obj;
                    if (ar4.exception == null && ar4.result != null) {
                        MtkServiceStateTracker.this.turboSig = (SignalStrength) ar4.result;
                        MtkServiceStateTracker.this.turboSig.updateLevel(MtkServiceStateTracker.this.mCarrierConfig, MtkServiceStateTracker.this.mSS);
                        MtkServiceStateTracker.this.log("notifySignalStrength level:" + MtkServiceStateTracker.this.turboSig.getLevel() + " raw:" + MtkServiceStateTracker.this.turboSig);
                        ((MtkGsmCdmaPhone) MtkServiceStateTracker.this.mPhone).notifyMtkSignalStrength(MtkServiceStateTracker.this.turboSig);
                        return;
                    }
                    MtkServiceStateTracker.this.log("onSignalStrengthResult() Exception from RIL : " + ar4.exception);
                    return;
                default:
                    MtkServiceStateTracker.this.loge("Should not be here msg.what=" + msg.what);
                    return;
            }
            boolean connected3 = ((NetworkRegistrationManager) MtkServiceStateTracker.this.mRegStateManagers.get(1)).isServiceConnected();
            if (connected3 || this.stop) {
                MtkServiceStateTracker.this.log("MtkHandler: stop MTK turbo");
                if (!this.stop) {
                    this.stop = true;
                }
                MtkServiceStateTracker.this.mCi.unregisterForNetworkStateChanged(MtkServiceStateTracker.this.mtkHandler);
                return;
            }
            if (msg.what == MtkServiceStateTracker.EVENT_RIL_READY) {
                MtkServiceStateTracker.this.log("MtkHandler: EVENT_RIL_READY");
            } else if (msg.what == MtkServiceStateTracker.EVENT_RESTART_TURBO) {
                MtkServiceStateTracker.this.log("MtkHandler: EVENT_RESTART_TURBO");
            } else {
                MtkServiceStateTracker.this.log("MtkHandler: EVENT_NETWORK_STATE_CHANGED");
                if (this.polling) {
                    this.pending = true;
                    return;
                }
            }
            if (MtkServiceStateTracker.this.mCi.getRadioState() == 1) {
                this.polling = true;
                MtkServiceStateTracker.this.mCi.getOperator(MtkServiceStateTracker.this.mtkHandler.obtainMessage(7, MtkServiceStateTracker.this.mPollingContext));
                MtkServiceStateTracker.this.mCi.getDataRegistrationState(MtkServiceStateTracker.this.mtkHandler.obtainMessage(5, MtkServiceStateTracker.this.mPollingContext));
                MtkServiceStateTracker.this.mCi.getVoiceRegistrationState(MtkServiceStateTracker.this.mtkHandler.obtainMessage(4, MtkServiceStateTracker.this.mPollingContext));
                MtkServiceStateTracker.this.mCi.getSignalStrength(MtkServiceStateTracker.this.mtkHandler.obtainMessage(MtkServiceStateTracker.EVENT_GET_SIGNAL_STRENGTH));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkServiceStateTracker(GsmCdmaPhone phone, CommandsInterface ci) {
        super(phone, ci);
        this.mEnableERI = false;
        this.mTelephonyCustomizationFactory = null;
        this.mServiceStateTrackerExt = null;
        this.mDataRoamingTypeChangedRegistrants = new RegistrantList();
        this.mLocatedPlmn = null;
        this.mHhbName = null;
        this.mCsgId = null;
        this.mFemtocellDomain = 0;
        this.mIsFemtocell = 0;
        this.mFemtoPlmn = null;
        this.mFemtoAct = null;
        this.hasPendingPollState = false;
        this.turboSS = new ServiceState();
        this.mIsTurboSSAlive = true;
        this.turboSig = null;
        this.once_pollState_done = false;
        this.mCnShareNwConfig = new SparseArray<>(4);
        this.mMtkIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkServiceStateTracker.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("com.mediatek.common.carrierexpress.operator_config_changed")) {
                    try {
                        MtkServiceStateTracker mtkServiceStateTracker = MtkServiceStateTracker.this;
                        mtkServiceStateTracker.mTelephonyCustomizationFactory = OpTelephonyCustomizationUtils.getOpFactory(mtkServiceStateTracker.mPhone.getContext());
                        MtkServiceStateTracker mtkServiceStateTracker2 = MtkServiceStateTracker.this;
                        mtkServiceStateTracker2.mServiceStateTrackerExt = mtkServiceStateTracker2.mTelephonyCustomizationFactory.makeServiceStateTrackerExt(MtkServiceStateTracker.this.mPhone.getContext());
                        MtkServiceStateTracker.this.log("mServiceStateTrackerExt reload success");
                    } catch (Exception e) {
                        MtkServiceStateTracker.this.log("mServiceStateTrackerExt init fail");
                        e.printStackTrace();
                    }
                }
            }
        };
        HandlerThread handlerThread = new HandlerThread("MtkHandlerThread");
        this.mtkHandlerThread = handlerThread;
        handlerThread.start();
        this.mtkHandler = new MtkHandler(this.mtkHandlerThread.getLooper());
        Context context = this.mPhone.getContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mediatek.common.carrierexpress.operator_config_changed");
        context.registerReceiver(this.mMtkIntentReceiver, filter);
        try {
            OpTelephonyCustomizationFactoryBase opFactory = OpTelephonyCustomizationUtils.getOpFactory(this.mPhone.getContext());
            this.mTelephonyCustomizationFactory = opFactory;
            this.mServiceStateTrackerExt = opFactory.makeServiceStateTrackerExt(this.mPhone.getContext());
        } catch (Exception e) {
            log("mServiceStateTrackerExt init fail");
            e.printStackTrace();
        }
        this.mCi.registerForPsNetworkStateChanged(this, 103, null);
        this.mCi.registerForRilConnected(this.mtkHandler, EVENT_RIL_READY, (Object) null);
        this.nm = new NrMap(phone, ci);
    }

    public boolean isTurboSSAlive() {
        return this.mIsTurboSSAlive;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updatePhoneType() {
        NetworkRegistrationInfo nrs;
        if (this.mSS != null && this.mSS.getVoiceRoaming()) {
            this.mVoiceRoamingOffRegistrants.notifyRegistrants();
        }
        if (this.mSS != null && this.mSS.getDataRoaming()) {
            this.mDataRoamingOffRegistrants.notifyRegistrants();
        }
        if (this.mSS != null && this.mSS.getState() == 0) {
            this.mNetworkDetachedRegistrants.notifyRegistrants();
        }
        boolean skipDetachNotify = RadioCapabilitySwitchUtil.IMSI_READY.equals(TelephonyManager.getTelephonyProperty(this.mPhone.getPhoneId(), "vendor.ril.mtk_hvolte_indicator", "0"));
        if (!skipDetachNotify) {
            for (int transport : this.mAccessNetworksManager.getAvailableTransports()) {
                if (this.mSS != null && (nrs = this.mSS.getNetworkRegistrationInfo(2, transport)) != null && nrs.isInService() && this.mDetachedRegistrants.get(transport) != null) {
                    ((RegistrantList) this.mDetachedRegistrants.get(transport)).notifyRegistrants();
                }
            }
        } else {
            log("hvolte register, skip notify detach registrants");
        }
        this.once_pollState_done = false;
        if (this.mSS == null || this.mNewSS == null || !skipDetachNotify) {
            this.mSS = new ServiceState();
            this.mSS.setOutOfService(false);
            this.mNewSS = new ServiceState();
        } else {
            log("updatePhoneType, not reset mSS");
        }
        this.mLastCellInfoReqTime = 0L;
        this.mLastCellInfoList = null;
        this.mStartedGprsRegCheck = false;
        this.mReportedGprsNoReg = false;
        this.mMdn = null;
        this.mMin = null;
        this.mPrlVersion = null;
        this.mIsMinInfoReady = false;
        this.mLastNitzData = null;
        this.mNitzState.handleNetworkUnavailable();
        this.mCellIdentity = null;
        this.mPhone.getSignalStrengthController().setSignalStrengthDefaultValues();
        this.mLastKnownCellIdentity = null;
        cancelPollState();
        if (this.mPhone.isPhoneTypeGsm()) {
            if (this.mCdmaSSM != null) {
                this.mCdmaSSM.dispose(this);
            }
            this.mCi.unregisterForCdmaPrlChanged(this);
            this.mCi.unregisterForCdmaOtaProvision(this);
            this.mPhone.unregisterForSimRecordsLoaded(this);
            this.mCi.registerForNetworkEvent(this, EVENT_NETWORK_EVENT, null);
            this.mCi.registerForModulation(this, EVENT_MODULATION_INFO, null);
            if (SystemProperties.get("ro.vendor.mtk_femto_cell_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
                this.mCi.registerForFemtoCellInfo(this, 102, null);
            }
        } else {
            this.mCi.unregisterForAvailable(this);
            this.mCi.unSetOnRestrictedStateChanged(this);
            this.mPsRestrictDisabledRegistrants.notifyRegistrants();
            this.mCi.unregisterForCdmaPrlChanged(this);
            this.mCi.unregisterForCdmaOtaProvision(this);
            this.mPhone.unregisterForSimRecordsLoaded(this);
            this.mCi.unregisterForNetworkEvent(this);
            this.mCi.unregisterForModulation(this);
            if (this.mPhone.isPhoneTypeCdmaLte()) {
                this.mPhone.registerForSimRecordsLoaded(this, 16, (Object) null);
            }
            this.mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(this.mPhone.getContext(), this.mCi, this, 39, (Object) null);
            this.mIsSubscriptionFromRuim = this.mCdmaSSM.getCdmaSubscriptionSource() == 0;
            this.mCi.registerForCdmaPrlChanged(this, 40, (Object) null);
            this.mCi.registerForCdmaOtaProvision(this, 37, (Object) null);
            this.mHbpcdUtils = new HbpcdUtils(this.mPhone.getContext());
            updateOtaspState();
        }
        onUpdateIccAvailability();
        setDataNetworkTypeForPhone(0);
        this.mPhone.getSignalStrengthController().getSignalStrengthFromCi();
        sendMessage(obtainMessage(50));
        logPhoneTypeChange();
        notifyVoiceRegStateRilRadioTechnologyChanged();
        if (!skipDetachNotify) {
            for (int i : this.mAccessNetworksManager.getAvailableTransports()) {
                notifyDataRegStateRilRadioTechnologyChanged(i);
            }
        }
    }

    public void registerForDataRoamingTypeChange(Handler h, int what, Object obj) {
        Registrant r = new Registrant(h, what, obj);
        this.mDataRoamingTypeChangedRegistrants.add(r);
    }

    public void unregisterForDataRoamingTypeChange(Handler h) {
        this.mDataRoamingTypeChangedRegistrants.remove(h);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        super.dispose();
        this.mPhone.getContext().unregisterReceiver(this.mMtkIntentReceiver);
        this.mCi.unregisterForPsNetworkStateChanged(this);
        this.mtkHandlerThread.quit();
        if (this.mPhone.isPhoneTypeGsm()) {
            this.mCi.unregisterForIccRefresh(this);
            this.mCi.unSetInvalidSimInfo(this);
            this.mCi.unregisterForNetworkEvent(this);
            this.mCi.unregisterForModulation(this);
        }
    }

    public void handleMessage(Message msg) {
        logv("received event " + msg.what);
        switch (msg.what) {
            case 1:
            case 50:
                log("handle EVENT_RADIO_STATE_CHANGED");
                if (!this.mPhone.isPhoneTypeGsm() && this.mCi.getRadioState() == 1) {
                    handleCdmaSubscriptionSource(this.mCdmaSSM.getCdmaSubscriptionSource());
                }
                RadioManager.getInstance().setRadioPower(this.mDesiredPowerState, this.mPhone.getPhoneId());
                pollStateInternal(true);
                break;
            case 16:
                if (this.mPhone.isPhoneTypeGsm() && refreshSpn(this.mSS, false)) {
                    pollState();
                }
                super.handleMessage(msg);
                break;
            case 26:
                if (this.mPhone.isPhoneTypeCdmaLte()) {
                    log("Receive EVENT_RUIM_READY");
                    pollStateInternal(false);
                } else {
                    log("Receive EVENT_RUIM_READY and Send Request getCDMASubscription.");
                    getSubscriptionInfoAndStartPollingThreads();
                }
                this.mCi.getNetworkSelectionMode(obtainMessage(14));
                break;
            case 43:
            case 44:
                if (msg.obj != null) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.exception != null || ar.result == null) {
                        this.mLastCellInfoList = null;
                    }
                }
                super.handleMessage(msg);
                break;
            case 45:
                log("EVENT_CHANGE_IMS_STATE: no action");
                break;
            case 49:
                log("EVENT_ALL_DATA_DISCONNECTED");
                if (this.mPendingRadioPowerOffAfterDataOff) {
                    boolean areAllDataDisconnectedOnAllPhones = true;
                    for (Phone phone : PhoneFactory.getPhones()) {
                        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
                        if (mtkDnc != null) {
                            if (mtkDnc.areAllDataDisconnectedExceptIms()) {
                                mtkDnc.unregisterDataNetworkControllerCallback(this.mDataDisconnectedCallback);
                            } else {
                                log("Still waiting for all data disconnected on phone: " + phone.getSubId());
                                areAllDataDisconnectedOnAllPhones = false;
                            }
                        }
                        break;
                    }
                    if (areAllDataDisconnectedOnAllPhones) {
                        this.mPendingRadioPowerOffAfterDataOff = false;
                        removeMessages(38);
                        log("Data disconnected for all phones, turn radio off now.");
                        hangupAndPowerOff();
                    }
                    break;
                }
                break;
            case 53:
                log("EVENT_IMS_SERVICE_STATE_CHANGED");
                if (this.mSS.getState() != 0 && this.mSS.getDataRegState() == 0) {
                    this.mPhone.notifyServiceStateChanged(this.mPhone.getServiceState());
                    break;
                }
                break;
            case 55:
                AsyncResult ar2 = (AsyncResult) msg.obj;
                if (isTurboSSAlive()) {
                    log("Skip PHYSICAL_CHANNEL_CONFIG Because Turbo SS Alive");
                    if (ar2.exception == null) {
                        List<PhysicalChannelConfig> list = (List) ar2.result;
                        this.mLastPhysicalChannelConfigList = list;
                        this.mPhone.notifyPhysicalChannelConfig(list);
                    }
                    break;
                } else {
                    super.handleMessage(msg);
                    if (this.mLastPhysicalChannelConfigList != null && this.mLastPhysicalChannelConfigList.size() == 1) {
                        boolean needRatChet = getCarrierConfig().getBoolean("mtk_key_ratchet_nr_bandwidth_bool", true);
                        if (!needRatChet && this.mSS.getDataNetworkType() == 20) {
                            int[] iArr = new int[0];
                            int[] bandwidths = this.mLastPhysicalChannelConfigList.stream().map(new Function() { // from class: com.mediatek.internal.telephony.MtkServiceStateTracker$$ExternalSyntheticLambda0
                                @Override // java.util.function.Function
                                public final Object apply(Object obj) {
                                    return Integer.valueOf(((PhysicalChannelConfig) obj).getCellBandwidthDownlinkKhz());
                                }
                            }).mapToInt(new ToIntFunction() { // from class: com.mediatek.internal.telephony.MtkServiceStateTracker$$ExternalSyntheticLambda1
                                @Override // java.util.function.ToIntFunction
                                public final int applyAsInt(Object obj) {
                                    return ((Integer) obj).intValue();
                                }
                            }).toArray();
                            log("re-setCellBandwidths: bandwidths = " + Arrays.stream(bandwidths).sum() + ", ssBandWidths = " + Arrays.stream(bandwidths).sum());
                            if (!Arrays.equals(bandwidths, this.mSS.getCellBandwidths())) {
                                this.mSS.setCellBandwidths(bandwidths);
                            }
                            break;
                        }
                    }
                }
                break;
            case 102:
                onFemtoCellInfoResult((AsyncResult) msg.obj);
                break;
            case 103:
                onPsNetworkStateChangeResult((AsyncResult) msg.obj);
                break;
            case EVENT_NETWORK_EVENT /* 104 */:
                if (this.mPhone.isPhoneTypeGsm()) {
                    onNetworkEventReceived((AsyncResult) msg.obj);
                }
                break;
            case EVENT_MODULATION_INFO /* 105 */:
                if (this.mPhone.isPhoneTypeGsm()) {
                    onModulationInfoReceived((AsyncResult) msg.obj);
                }
                break;
            case EVENT_SIM_OPL_LOADED /* 119 */:
                AsyncResult ar3 = (AsyncResult) msg.obj;
                if (ar3 != null && ar3.result != null) {
                    Integer id = (Integer) ar3.result;
                    if (id.intValue() == 103) {
                        if (this.mPhone.isPhoneTypeGsm()) {
                            log("EVENT_SIM_OPL_LOADED: EVENT_OPL5G");
                            if (refreshSpn(this.mSS, false)) {
                                pollState();
                            }
                        } else {
                            loge("EVENT_SIM_OPL_LOADED should not be here");
                        }
                    }
                } else {
                    loge("EVENT_SIM_OPL_LOADED obj is null");
                }
                break;
            case EVENT_RECHECK_NRM_STATUS /* 123 */:
                log("EVENT_RECHECK_NRM_STATUS trigger pollState");
                pollStateInternal(true);
                break;
            case EVENT_IWLAN_PREFERRED_CHANGED /* 125 */:
                boolean isIwlanPreferred = this.mAccessNetworksManager.isAnyApnOnIwlan();
                log("EVENT_IWLAN_PREFERRED_CHANGED: isIwlanPreferred = " + isIwlanPreferred);
                if (this.mSS != null && isIwlanPreferred != this.mSS.isIwlanPreferred() && this.mPollingContext[0] == 0) {
                    pollStateInternal(false);
                    break;
                }
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    protected void handlePollStateResultMessage(int what, AsyncResult ar) {
        switch (what) {
            case 5:
                super.handlePollStateResultMessage(what, ar);
                NetworkRegistrationInfo networkRegState = (NetworkRegistrationInfo) ar.result;
                if (this.mServiceStateTrackerExt.modifyRsrpThresholdsForRsrpBoost(this.mCarrierConfig, networkRegState.getCellIdentity().getType(), this.mNewSS.getArfcnRsrpBoost())) {
                    log("need update signal strength's level after the rsrpBoost changed: " + this.mNewSS.getArfcnRsrpBoost());
                    SignalStrength ss = this.mPhone.getSignalStrengthController().getSignalStrength();
                    log("old:{level:" + ss.getLevel() + ", raw:" + ss.toString() + "}");
                    ss.updateLevel(this.mCarrierConfig, this.mNewSS);
                    log("new:{level: " + ss.getLevel() + "}");
                    this.mPhone.getSignalStrengthController().notifySignalStrength();
                }
                break;
            default:
                super.handlePollStateResultMessage(what, ar);
                break;
        }
    }

    protected void handlePollStateResult(int what, AsyncResult ar) {
        boolean isRoamingBetweenOperators;
        if (ar.userObj != this.mPollingContext) {
            return;
        }
        if (ar.exception != null) {
            CommandException.Error err = null;
            if (ar.exception instanceof IllegalStateException) {
                log("handlePollStateResult exception " + ar.exception);
            }
            if (ar.exception instanceof CommandException) {
                err = ar.exception.getCommandError();
            }
            if (this.mCi.getRadioState() != 1) {
                log("handlePollStateResult: Invalid response due to radio off or unavailable. Set ServiceState to out of service.");
                pollStateInternal(false);
                return;
            }
            if (err == CommandException.Error.RADIO_NOT_AVAILABLE) {
                loge("handlePollStateResult: RIL returned RADIO_NOT_AVAILABLE when radio is on.");
                cancelPollState();
                if (this.hasPendingPollState) {
                    this.hasPendingPollState = false;
                    pollState();
                    loge("handlePollStateResult trigger pending pollState()");
                    return;
                } else {
                    if (this.mCi.getRadioState() != 1) {
                        if (this.mCi.getRadioState() == 2) {
                            this.mNewSS.setOutOfService(false);
                        } else {
                            this.mNewSS.setOutOfService(true);
                        }
                        this.mPhone.getSignalStrengthController().setSignalStrengthDefaultValues();
                        pollStateDone();
                        loge("Mlog: pollStateDone to notify RADIO_NOT_AVAILABLE");
                        return;
                    }
                    return;
                }
            }
            if (err != CommandException.Error.OP_NOT_ALLOWED_BEFORE_REG_NW) {
                loge("handlePollStateResult: RIL returned an error where it must succeed: " + ar.exception);
            }
        } else {
            try {
                handlePollStateResultMessage(what, ar);
            } catch (RuntimeException ex) {
                loge("Exception while polling service state. Probably malformed RIL response." + ex);
            }
        }
        int[] iArr = this.mPollingContext;
        iArr[0] = iArr[0] - 1;
        if (this.mPollingContext[0] == 0) {
            this.mNewSS.setEmergencyOnly(this.mEmergencyOnly);
            combinePsRegistrationStates(this.mNewSS);
            updateOperatorNameForServiceState(this.mNewSS);
            if (this.mPhone.isPhoneTypeGsm()) {
                refreshSpn(this.mNewSS, true);
                boolean in_service = this.mNewSS.getVoiceRegState() == 0 || this.mNewSS.getDataRegState() == 0;
                NetworkRegistrationInfo wlanPsNri = this.mNewSS.getNetworkRegistrationInfo(2, 2);
                boolean radioOffwithIwlan = this.mCi.getRadioState() == 0 && (this.mNewSS.getRilDataRadioTechnology() == 18 || (wlanPsNri != null && wlanPsNri.isInService()));
                String oper = this.mNewSS.getOperatorNumeric();
                if (((!in_service && !TextUtils.isEmpty(oper)) || (in_service && TextUtils.isEmpty(oper))) && !radioOffwithIwlan && this.hasPendingPollState) {
                    loge("Temporary service state, need restart PollState");
                    this.hasPendingPollState = false;
                    cancelPollState();
                    pollState();
                    return;
                }
                updateRoamingState();
            } else {
                boolean namMatch = false;
                if (!isSidsAllZeros() && isHomeSid(this.mNewSS.getCdmaSystemId())) {
                    namMatch = true;
                }
                if (this.mIsSubscriptionFromRuim && (isRoamingBetweenOperators = isRoamingBetweenOperators(this.mNewSS.getVoiceRoaming(), this.mNewSS)) != this.mNewSS.getVoiceRoaming()) {
                    log("isRoamingBetweenOperators=" + isRoamingBetweenOperators + ". Override CDMA voice roaming to " + isRoamingBetweenOperators);
                    this.mNewSS.setVoiceRoaming(isRoamingBetweenOperators);
                }
                int dataRat = getRilDataRadioTechnologyForWwan(this.mNewSS);
                if (ServiceState.isCdma(dataRat)) {
                    boolean isVoiceInService = this.mNewSS.getState() == 0;
                    if (isVoiceInService) {
                        boolean isVoiceRoaming = this.mNewSS.getVoiceRoaming();
                        if (this.mNewSS.getDataRoaming() != isVoiceRoaming) {
                            log("Data roaming != Voice roaming. Override data roaming to " + isVoiceRoaming);
                            this.mNewSS.setDataRoaming(isVoiceRoaming);
                        }
                    } else {
                        boolean isRoamIndForHomeSystem = isRoamIndForHomeSystem(this.mRoamingIndicator);
                        boolean dataRoamingState = this.mNewSS.getDataRoaming();
                        if (this.mNewSS.getDataRoaming() == isRoamIndForHomeSystem) {
                            log("isRoamIndForHomeSystem=" + isRoamIndForHomeSystem + ", override data roaming to " + (!isRoamIndForHomeSystem));
                            this.mNewSS.setDataRoaming(!isRoamIndForHomeSystem);
                        }
                        int[] homeRoamIndicators = this.mCarrierConfig.getIntArray("cdma_enhanced_roaming_indicator_for_home_network_int_array");
                        if (!dataRoamingState && !isRoamIndForHomeSystem && homeRoamIndicators != null && homeRoamIndicators.length == 1 && homeRoamIndicators[0] == 1) {
                            log("isRoamIndForHomeSystem=" + isRoamIndForHomeSystem + ", override data roaming to false");
                            this.mNewSS.setDataRoaming(false);
                        }
                    }
                }
                this.mNewSS.setCdmaDefaultRoamingIndicator(this.mDefaultRoamingIndicator);
                this.mNewSS.setCdmaRoamingIndicator(this.mRoamingIndicator);
                boolean isPrlLoaded = true;
                if (TextUtils.isEmpty(this.mPrlVersion)) {
                    isPrlLoaded = false;
                }
                if (!isPrlLoaded || this.mNewSS.getRilVoiceRadioTechnology() == 0) {
                    logv("Turn off roaming indicator if !isPrlLoaded or voice RAT is unknown");
                    this.mNewSS.setCdmaRoamingIndicator(1);
                } else if (!isSidsAllZeros()) {
                    if (!namMatch && !this.mIsInPrl) {
                        this.mNewSS.setCdmaRoamingIndicator(this.mDefaultRoamingIndicator);
                    } else if (namMatch && !this.mIsInPrl) {
                        if (ServiceState.isPsOnlyTech(this.mNewSS.getRilVoiceRadioTechnology())) {
                            log("Turn off roaming indicator as voice is LTE or NR");
                            this.mNewSS.setCdmaRoamingIndicator(1);
                        } else {
                            this.mNewSS.setCdmaRoamingIndicator(2);
                        }
                    } else if ((namMatch || !this.mIsInPrl) && this.mRoamingIndicator <= 2) {
                        this.mNewSS.setCdmaRoamingIndicator(1);
                    } else {
                        this.mNewSS.setCdmaRoamingIndicator(this.mRoamingIndicator);
                    }
                }
                if (isRoamIndForHomeSystem(this.mNewSS.getCdmaRoamingIndicator())) {
                    log("roaming indicator: " + this.mNewSS.getCdmaRoamingIndicator() + " is home, set to ROAMING_INDICATOR_OFF");
                    this.mNewSS.setCdmaEriIconIndex(1);
                    this.mNewSS.setCdmaEriIconMode(0);
                } else if (this.mEriManager != null) {
                    int roamingIndicator = this.mNewSS.getCdmaRoamingIndicator();
                    this.mNewSS.setCdmaEriIconIndex(this.mEriManager.getCdmaEriIconIndex(roamingIndicator, this.mDefaultRoamingIndicator));
                    this.mNewSS.setCdmaEriIconMode(this.mEriManager.getCdmaEriIconMode(roamingIndicator, this.mDefaultRoamingIndicator));
                }
                log("Set CDMA Roaming Indicator to: " + this.mNewSS.getCdmaRoamingIndicator() + ". voiceRoaming = " + this.mNewSS.getVoiceRoaming() + ". dataRoaming = " + this.mNewSS.getDataRoaming() + ", isPrlLoaded = " + isPrlLoaded + ". namMatch = " + namMatch + " , mIsInPrl = " + this.mIsInPrl + ", mRoamingIndicator = " + this.mRoamingIndicator + ", mDefaultRoamingIndicator= " + this.mDefaultRoamingIndicator + ", set mEmergencyOnly=" + this.mEmergencyOnly + ", EriIconIndex = " + this.mNewSS.getCdmaEriIconIndex());
            }
            pollStateDone();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:218:0x04b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void updateSpnDisplayLegacy() {
        /*
            Method dump skipped, instruction units count: 1276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.MtkServiceStateTracker.updateSpnDisplayLegacy():void");
    }

    protected void log(String s) {
        if (this.mPhone.isPhoneTypeGsm()) {
            Rlog.d(LOG_TAG, "[GsmSST" + this.mPhone.getPhoneId() + "] " + s);
        } else if (this.mPhone.isPhoneTypeCdma()) {
            Rlog.d(LOG_TAG, "[CdmaSST" + this.mPhone.getPhoneId() + "] " + s);
        } else {
            Rlog.d(LOG_TAG, "[CdmaLteSST" + this.mPhone.getPhoneId() + "] " + s);
        }
    }

    protected void loge(String s) {
        if (this.mPhone.isPhoneTypeGsm()) {
            Rlog.e(LOG_TAG, "[GsmSST" + this.mPhone.getPhoneId() + "] " + s);
        } else if (this.mPhone.isPhoneTypeCdma()) {
            Rlog.e(LOG_TAG, "[CdmaSST" + this.mPhone.getPhoneId() + "] " + s);
        } else {
            Rlog.e(LOG_TAG, "[CdmaLteSST" + this.mPhone.getPhoneId() + "] " + s);
        }
    }

    protected void logv(String s) {
        if (this.mPhone.isPhoneTypeGsm()) {
            Rlog.v(LOG_TAG, "[GsmSST" + this.mPhone.getPhoneId() + "] " + s);
        } else if (this.mPhone.isPhoneTypeCdma()) {
            Rlog.v(LOG_TAG, "[CdmaSST" + this.mPhone.getPhoneId() + "] " + s);
        } else {
            Rlog.v(LOG_TAG, "[CdmaLteSST" + this.mPhone.getPhoneId() + "] " + s);
        }
    }

    private void onPsNetworkStateChangeResult(AsyncResult ar) {
        if (ar.exception != null || ar.result == null) {
            loge("onPsNetworkStateChangeResult exception");
            return;
        }
        int[] info = (int[]) ar.result;
        if (info.length < 6) {
            String operator_plmn = String.valueOf(info[1]);
            if (operator_plmn != null && operator_plmn.length() >= 5) {
                updateLocatedPlmn(operator_plmn);
                return;
            } else {
                updateLocatedPlmn(null);
                return;
            }
        }
        if (info.length != 7) {
            loge("onPsNetworkStateChangeResult wrong size");
            return;
        }
        if (info[0] == 4) {
            ((MtkGsmCdmaPhone) this.mPhone).notifyMtkFakeServiceStateChanged(null);
            return;
        }
        if (info[0] != 1) {
            ((MtkGsmCdmaPhone) this.mPhone).notifyMtkFakeServiceStateChanged(null);
            this.mPhone.notifyServiceStateChanged(this.mPhone.getServiceState());
            return;
        }
        boolean isCs = false;
        ServiceState fakeSS = new ServiceState();
        int networkType = ServiceState.rilRadioTechnologyToNetworkType(info[1]);
        if (info[1] == 3 || info[1] == 16) {
            isCs = true;
        }
        if (networkType == 19) {
            networkType = 13;
        }
        NetworkRegistrationInfo networkVoiceRegState = new NetworkRegistrationInfo(1, 1, isCs ? info[0] : 0, isCs ? networkType : 0, 0, false, null, null, "", false, 0, 0, 0);
        int voiceRegistrationState = networkVoiceRegState.getNetworkRegistrationState();
        fakeSS.setVoiceRegState(regCodeToServiceState(voiceRegistrationState));
        fakeSS.addNetworkRegistrationInfo(networkVoiceRegState);
        NetworkRegistrationInfo networkDataRegState = new NetworkRegistrationInfo(2, 1, !isCs ? info[0] : 0, !isCs ? networkType : 0, 0, false, null, null, "", 0, info[4] == 1, info[5] == 2, info[6] == 1, new LteVopsSupportInfo(1, 1));
        int dataRegistrationState = networkDataRegState.getNetworkRegistrationState();
        int serviceState = regCodeToServiceState(dataRegistrationState);
        fakeSS.setDataRegState(serviceState);
        fakeSS.addNetworkRegistrationInfo(networkDataRegState);
        log("broadcast fakeSS:" + fakeSS);
        ((MtkGsmCdmaPhone) this.mPhone).notifyMtkFakeServiceStateChanged(fakeSS);
        int display_type = fakeSS.getDataNetworkType();
        if (display_type == 0) {
            display_type = fakeSS.getVoiceNetworkType();
        }
        TelephonyDisplayInfo displayInfo = new TelephonyDisplayInfo(display_type, 0);
        log("broadcast fakeSS: " + displayInfo);
        ((MtkGsmCdmaPhone) this.mPhone).notifyDisplayInfoChanged(displayInfo);
    }

    protected void pollStateInternal(boolean modemTriggered) {
        boolean connected = ((NetworkRegistrationManager) this.mRegStateManagers.get(1)).isServiceConnected();
        int support_ap_iwlan = this.mRegStateManagers.get(2) != null ? 1 : 0;
        log("pollState: modemTriggered=" + modemTriggered + ", mPollingContext=" + (this.mPollingContext != null ? this.mPollingContext[0] : -1) + ", RadioState=" + this.mCi.getRadioState() + ", connected=" + connected + ", support_ap_iwlan=" + support_ap_iwlan);
        if (this.mPollingContext != null && this.mCi.getRadioState() == 1 && ((this.mPhone.isPhoneTypeGsm() && this.mPollingContext[0] == support_ap_iwlan + 4) || (!this.mPhone.isPhoneTypeGsm() && this.mPollingContext[0] == support_ap_iwlan + 3))) {
            this.hasPendingPollState = true;
            return;
        }
        this.mPollingContext = new int[1];
        log("pollState: modemTriggered=" + modemTriggered + ", radioState=" + this.mCi.getRadioState());
        switch (this.mCi.getRadioState()) {
            case 0:
                NetworkRegistrationInfo nri = this.mNewSS.getNetworkRegistrationInfo(2, 2);
                this.mNewSS.setOutOfService(true);
                if (nri != null) {
                    this.mNewSS.addNetworkRegistrationInfo(nri);
                }
                this.mPhone.getSignalStrengthController().setSignalStrengthDefaultValues();
                this.mLastNitzData = null;
                this.mNitzState.handleNetworkUnavailable();
                if (this.mDeviceShuttingDown || (!modemTriggered && 18 != this.mSS.getRilDataRadioTechnology())) {
                    pollStateDone();
                    return;
                }
                break;
            case 2:
                NetworkRegistrationInfo nri2 = this.mNewSS.getNetworkRegistrationInfo(2, 2);
                this.mNewSS.setOutOfService(false);
                if (nri2 != null) {
                    this.mNewSS.addNetworkRegistrationInfo(nri2);
                }
                this.mPhone.getSignalStrengthController().setSignalStrengthDefaultValues();
                this.mLastNitzData = null;
                this.mNitzState.handleNetworkUnavailable();
                pollStateDone();
                return;
        }
        this.mtkHandler.removeMessages(EVENT_RECHECK_NRM_STATUS);
        if (!connected) {
            log("Skip pollState due to disconnection of service");
            this.mtkHandler.sendEmptyMessageDelayed(EVENT_RECHECK_NRM_STATUS, mCheckNrmPeriod);
            return;
        }
        int[] iArr = this.mPollingContext;
        iArr[0] = iArr[0] + 1;
        this.mCi.getOperator(obtainMessage(7, this.mPollingContext));
        int[] iArr2 = this.mPollingContext;
        iArr2[0] = iArr2[0] + 1;
        ((NetworkRegistrationManager) this.mRegStateManagers.get(1)).requestNetworkRegistrationInfo(2, obtainMessage(5, this.mPollingContext));
        int[] iArr3 = this.mPollingContext;
        iArr3[0] = iArr3[0] + 1;
        ((NetworkRegistrationManager) this.mRegStateManagers.get(1)).requestNetworkRegistrationInfo(1, obtainMessage(4, this.mPollingContext));
        if (this.mRegStateManagers.get(2) != null) {
            int[] iArr4 = this.mPollingContext;
            iArr4[0] = iArr4[0] + 1;
            ((NetworkRegistrationManager) this.mRegStateManagers.get(2)).requestNetworkRegistrationInfo(2, obtainMessage(6, this.mPollingContext));
        }
        if (this.mPhone.isPhoneTypeGsm()) {
            int[] iArr5 = this.mPollingContext;
            iArr5[0] = iArr5[0] + 1;
            this.mCi.getNetworkSelectionMode(obtainMessage(14, this.mPollingContext));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0217  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0449  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x045c  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0585  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x05c7  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0643  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0666  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x069d  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x06b6  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x06c9  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x06f8  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0704  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0717  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0738  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x073d  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x08db  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x08e7  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x08ea  */
    /* JADX WARN: Removed duplicated region for block: B:352:0x08f9  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x090f  */
    /* JADX WARN: Removed duplicated region for block: B:380:0x098f  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x099a  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x099f  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x09a6  */
    /* JADX WARN: Removed duplicated region for block: B:392:0x09b1  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x09b6  */
    /* JADX WARN: Removed duplicated region for block: B:396:0x09bd  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x09c4  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x09cb  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x09d2  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x09ee A[LOOP:4: B:404:0x09e8->B:406:0x09ee, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0a17  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0a22  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0a29  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x0a36  */
    /* JADX WARN: Removed duplicated region for block: B:430:0x0a7e  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x0a85  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01fa  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0203  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void pollStateDone() {
        /*
            Method dump skipped, instruction units count: 2708
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.MtkServiceStateTracker.pollStateDone():void");
    }

    private final boolean isConcurrentVoiceAndDataAllowedForIwlan() {
        if (this.mSS.getDataRegState() == 0 && this.mSS.getRilDataRadioTechnology() == 18 && getImsServiceState() == 0) {
            return true;
        }
        return false;
    }

    public boolean isConcurrentVoiceAndDataAllowed() {
        if (this.mSS.getCssIndicator() == 1) {
            return true;
        }
        if (this.mPhone.isPhoneTypeGsm()) {
            if (isConcurrentVoiceAndDataAllowedForVolte() || isConcurrentVoiceAndDataAllowedForIwlan()) {
                return true;
            }
            return super.isConcurrentVoiceAndDataAllowed();
        }
        if (this.mPhone.isPhoneTypeCdma()) {
            return false;
        }
        return (SystemProperties.getInt("ro.vendor.mtk_c2k_lte_mode", 0) == 1 && this.mSS.getRilDataRadioTechnology() == 14) || isConcurrentVoiceAndDataAllowedForVolte() || this.mSS.getCssIndicator() == 1;
    }

    public String getLocatedPlmn() {
        return this.mLocatedPlmn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastLocatedPlmnChanged(String old_Plmn, String plmn) {
        log("updateLocatedPlmn(),previous plmn= " + old_Plmn + " ,update to: " + plmn);
        Intent intent = new Intent("com.mediatek.intent.action.LOCATED_PLMN_CHANGED");
        if (TelephonyManager.getDefault().getPhoneCount() == 1) {
            intent.addFlags(536870912);
        }
        intent.putExtra("android.telephony.extra.PLMN", plmn);
        if (plmn != null) {
            try {
                int mcc = Integer.parseInt(plmn.substring(0, 3));
                intent.putExtra("iso", MccTable.countryCodeForMcc(mcc));
            } catch (NumberFormatException ex) {
                loge("updateLocatedPlmn: countryCodeForMcc error" + ex);
                intent.putExtra("iso", "");
            } catch (StringIndexOutOfBoundsException ex2) {
                loge("updateLocatedPlmn: countryCodeForMcc error" + ex2);
                intent.putExtra("iso", "");
            }
        } else {
            intent.putExtra("iso", "");
        }
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    private void updateLocatedPlmn(final String plmn) {
        Thread subT = null;
        String str = this.mLocatedPlmn;
        if ((str == null && plmn != null) || ((str != null && plmn == null) || (str != null && plmn != null && !str.equals(plmn)))) {
            final String old_plmn = this.mLocatedPlmn;
            subT = new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.MtkServiceStateTracker.2
                @Override // java.lang.Runnable
                public void run() {
                    MtkServiceStateTracker.this.broadcastLocatedPlmnChanged(old_plmn, plmn);
                }
            });
        }
        this.mLocatedPlmn = plmn;
        if (subT != null) {
            subT.start();
        }
    }

    private void updateSsOperatorName(ServiceState ss, String strOperatorLong, String strOperatorShort, String operatorNumeric) {
        ss.setOperatorName(strOperatorLong, strOperatorShort, ss.getOperatorNumeric());
        NetworkRegistrationInfo nri_ps = ss.getNetworkRegistrationInfo(2, 1);
        if (nri_ps != null && nri_ps.getCellIdentity() != null) {
            CellIdentity ps_cell = nri_ps.getCellIdentity();
            ps_cell.setOperatorAlphaLong(strOperatorLong);
            ps_cell.setOperatorAlphaShort(strOperatorShort);
            NetworkRegistrationInfo new_nri_ps = new NetworkRegistrationInfo(nri_ps);
            ss.addNetworkRegistrationInfo(new_nri_ps);
        }
        NetworkRegistrationInfo nri_cs = ss.getNetworkRegistrationInfo(1, 1);
        if (nri_cs != null && nri_cs.getCellIdentity() != null) {
            CellIdentity cs_cell = nri_cs.getCellIdentity();
            cs_cell.setOperatorAlphaLong(strOperatorLong);
            cs_cell.setOperatorAlphaShort(strOperatorShort);
            NetworkRegistrationInfo new_nri_cs = new NetworkRegistrationInfo(nri_cs);
            ss.addNetworkRegistrationInfo(new_nri_cs);
        }
    }

    protected boolean refreshSpn(ServiceState ss, boolean fromPollState) {
        String strOperatorLong;
        String strOperatorShort;
        boolean needPollState = false;
        String brandOverride = this.mUiccController.getUiccPort(getPhoneId()) != null ? this.mUiccController.getUiccPort(getPhoneId()).getOperatorBrandOverride() : null;
        if (brandOverride != null) {
            log("refreshSpn: use brandOverride" + brandOverride);
            strOperatorLong = brandOverride;
            strOperatorShort = brandOverride;
        } else {
            int lac = getLacFromServiceState(ss);
            strOperatorLong = this.mCi.lookupOperatorName(this.mPhone.getSubId(), ss.getOperatorNumeric(), true, lac, ss);
            strOperatorShort = this.mCi.lookupOperatorName(this.mPhone.getSubId(), ss.getOperatorNumeric(), false, lac, ss);
        }
        if (!TextUtils.equals(strOperatorLong, ss.getOperatorAlphaLong()) || !TextUtils.equals(strOperatorShort, ss.getOperatorAlphaShort())) {
            needPollState = true;
            if (fromPollState) {
                updateSsOperatorName(ss, strOperatorLong, strOperatorShort, ss.getOperatorNumeric());
            }
        }
        log("refreshSpn: " + strOperatorLong + ", " + strOperatorShort + ", fromPollState=" + fromPollState + ", needPollState=" + needPollState);
        return needPollState;
    }

    protected void setPowerStateToDesired(boolean forEmergencyCall, boolean isSelectedPhoneForEmergencyCall, boolean forceApply) {
        String tmpLog = "setPowerStateToDesired: mDeviceShuttingDown=" + this.mDeviceShuttingDown + ", mDesiredPowerState=" + this.mDesiredPowerState + ", getRadioState=" + this.mCi.getRadioState() + ", mRadioPowerOffReasons=" + this.mRadioPowerOffReasons + ", IMS reg state=" + this.mImsRegistrationOnOff + ", pending radio off=" + hasMessages(62);
        log(tmpLog);
        this.mRadioPowerLog.log(tmpLog);
        if (this.mDesiredPowerState && this.mDeviceShuttingDown) {
            log("setPowerStateToDesired powering on of radio failed because the device is powering off");
            return;
        }
        if (this.mDesiredPowerState && this.mRadioPowerOffReasons.isEmpty() && (forceApply || this.mCi.getRadioState() == 0)) {
            RadioManager.getInstance();
            RadioManager.sendRequestBeforeSetRadioPower(true, this.mPhone.getPhoneId());
            if (this.mPhone instanceof MtkGsmCdmaPhone) {
                boolean currEccRadioOnStatus = ((MtkGsmCdmaPhone) this.mPhone).getEccRadioOnStatus();
                boolean currEccSelectedPhone = ((MtkGsmCdmaPhone) this.mPhone).isEccSelectedPhone();
                if (!currEccRadioOnStatus && forEmergencyCall) {
                    ((MtkGsmCdmaPhone) this.mPhone).setEccRadioOnStatus(forEmergencyCall, isSelectedPhoneForEmergencyCall);
                } else if (currEccRadioOnStatus && !forEmergencyCall && forceApply) {
                    log("reset all phone radio ecc adjusts to false");
                    for (MtkGsmCdmaPhone mtkGsmCdmaPhone : PhoneFactory.getPhones()) {
                        if (mtkGsmCdmaPhone != null && (mtkGsmCdmaPhone instanceof MtkGsmCdmaPhone)) {
                            mtkGsmCdmaPhone.setEccRadioOnStatus(false, false);
                        }
                    }
                } else if (currEccRadioOnStatus != forEmergencyCall) {
                    log("adjust forEcc, preEcc (" + (currEccRadioOnStatus ? "t" : "f") + "," + (currEccRadioOnStatus ? "t" : "f") + ")");
                    forEmergencyCall = currEccRadioOnStatus;
                    isSelectedPhoneForEmergencyCall = currEccSelectedPhone;
                }
            }
            this.mCi.setRadioPower(true, forEmergencyCall, isSelectedPhoneForEmergencyCall, (Message) null);
        } else if ((!this.mDesiredPowerState || !this.mRadioPowerOffReasons.isEmpty()) && this.mCi.getRadioState() == 1) {
            log("setPowerStateToDesired: powerOffRadioSafely()");
            powerOffRadioSafely();
        } else if (this.mDeviceShuttingDown && this.mCi.getRadioState() != 2) {
            this.mCi.requestShutdown((Message) null);
        }
        cancelDelayRadioOffWaitingForImsDeregTimeout();
    }

    protected void hangupAndPowerOff() {
        if (!this.mPhone.isPhoneTypeGsm() || this.mPhone.isInCall()) {
            this.mPhone.mCT.mRingingCall.hangupIfAlive();
            this.mPhone.mCT.mBackgroundCall.hangupIfAlive();
            this.mPhone.mCT.mForegroundCall.hangupIfAlive();
        }
        mtkHangupAllImsCall();
        RadioManager.getInstance();
        RadioManager.sendRequestBeforeSetRadioPower(false, this.mPhone.getPhoneId());
        this.mCi.setRadioPower(false, obtainMessage(54));
    }

    public String getFemtoCsgId() {
        return this.mCsgId;
    }

    public String getFemtoPlmn() {
        return this.mFemtoPlmn;
    }

    public String getFemtoAct() {
        return this.mFemtoAct;
    }

    private void onFemtoCellInfoResult(AsyncResult ar) {
        int isCsgCell = 0;
        if (ar.exception != null || ar.result == null) {
            loge("onFemtoCellInfo exception");
            return;
        }
        String[] info = (String[]) ar.result;
        if (info.length > 0) {
            if (info[0] != null && info[0].length() > 0) {
                this.mFemtocellDomain = Integer.parseInt(info[0]);
                log("onFemtoCellInfo: mFemtocellDomain set to " + this.mFemtocellDomain);
            }
            if (info[3] != null && info[3].length() > 0) {
                this.mFemtoPlmn = info[3];
                log("onFemtoCellInfo: mFemtoPlmn set to " + this.mFemtoPlmn);
            }
            if (info[4] != null && info[4].length() > 0) {
                this.mFemtoAct = info[4];
                log("onFemtoCellInfo: mFemtoAct set to " + this.mFemtoAct);
            }
            if (info[5] != null && info[5].length() > 0) {
                isCsgCell = Integer.parseInt(info[5]);
            }
            this.mIsFemtocell = isCsgCell;
            log("onFemtoCellInfo: domain= " + this.mFemtocellDomain + ",isCsgCell= " + isCsgCell);
            if (isCsgCell == 1) {
                if (info[6] != null && info[6].length() > 0) {
                    this.mCsgId = info[6];
                    log("onFemtoCellInfo: mCsgId set to " + this.mCsgId);
                }
                if (info[8] != null && info[8].length() > 0) {
                    this.mHhbName = new String(IccUtils.hexStringToBytes(info[8]));
                    log("onFemtoCellInfo: mHhbName set from " + info[8] + " to " + this.mHhbName);
                } else {
                    this.mHhbName = null;
                    log("onFemtoCellInfo: mHhbName is not available ,set to null");
                }
            } else {
                this.mCsgId = null;
                this.mHhbName = null;
                log("onFemtoCellInfo: csgId and hnbName are cleared");
            }
            Intent intent = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
            if (TelephonyManager.getDefault().getPhoneCount() == 1) {
                intent.addFlags(536870912);
            }
            intent.putExtra("android.telephony.extra.SHOW_SPN", this.mCurShowSpn);
            intent.putExtra("android.telephony.extra.SPN", this.mCurSpn);
            intent.putExtra("android.telephony.extra.SHOW_PLMN", this.mCurShowPlmn);
            intent.putExtra("android.telephony.extra.PLMN", this.mCurPlmn);
            intent.putExtra("hnbName", this.mHhbName);
            intent.putExtra("csgId", this.mCsgId);
            intent.putExtra("domain", this.mFemtocellDomain);
            intent.putExtra("femtocell", this.mIsFemtocell);
            this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
            this.mPhone.getPhoneId();
            String plmn = this.mCurPlmn;
            String str = this.mHhbName;
            if (str == null && this.mCsgId != null) {
                plmn = (plmn + " - ") + this.mCsgId;
            } else if (str != null) {
                plmn = (plmn + " - ") + this.mHhbName;
            }
            int subId = this.mPhone.getSubId();
            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                this.mSubscriptionManagerService.setCarrierName(subId, TextUtils.emptyIfNull(getCarrierName(this.mCurShowPlmn, plmn, this.mCurShowSpn, this.mCurSpn)));
            }
        }
    }

    private void onNetworkEventReceived(AsyncResult ar) {
        if (ar.exception != null || ar.result == null) {
            loge("onNetworkEventReceived exception");
            return;
        }
        int nwEventType = ((int[]) ar.result)[1];
        log("[onNetworkEventReceived] event_type:" + nwEventType);
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_NETWORK_EVENT");
        intent.addFlags(536870912);
        intent.putExtra("eventType", nwEventType + 1);
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    private void onModulationInfoReceived(AsyncResult ar) {
        if (ar.exception != null || ar.result == null) {
            loge("onModulationInfoReceived exception");
            return;
        }
        int[] info = (int[]) ar.result;
        int modulation = info[0];
        log("[onModulationInfoReceived] modulation:" + modulation);
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_NOTIFY_MODULATION_INFO");
        intent.addFlags(536870912);
        intent.putExtra("modulation_info", modulation);
        this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    protected final boolean IsInternationalRoamingException(String operatorNumeric) {
        CarrierConfigManager configManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (configManager == null) {
            Rlog.e(LOG_TAG, "Carrier config service is not available");
            return false;
        }
        PersistableBundle b = configManager.getConfigForSubId(this.mPhone.getSubId());
        if (b == null) {
            Rlog.e(LOG_TAG, "Can't get the config. subId = " + this.mPhone.getSubId());
            return false;
        }
        String[] operatorRoamingException = b.getStringArray("mtk_carrier_international_roaming_exception_list_strings");
        if (operatorRoamingException == null) {
            Rlog.e(LOG_TAG, "mtk_carrier_international_roaming_exception_list_strings is not available. subId = " + this.mPhone.getSubId());
            return false;
        }
        HashSet<String> internationalRoamingSet = new HashSet<>(Arrays.asList(operatorRoamingException));
        Rlog.d(LOG_TAG, "For subId = " + this.mPhone.getSubId() + ", international roaming exceptions are " + Arrays.toString(internationalRoamingSet.toArray()) + ", operatorNumeric = " + operatorNumeric);
        if (internationalRoamingSet.contains(operatorNumeric)) {
            Rlog.d(LOG_TAG, operatorNumeric + " in list.");
            return true;
        }
        Rlog.d(LOG_TAG, operatorNumeric + " is not in list.");
        return false;
    }

    protected void setRoamingType(ServiceState currentServiceState) {
        boolean isVoiceInService = currentServiceState.getState() == 0;
        if (isVoiceInService) {
            if (currentServiceState.getVoiceRoaming()) {
                if (this.mPhone.isPhoneTypeGsm()) {
                    if (inSameCountry(currentServiceState.getOperatorNumeric())) {
                        currentServiceState.setVoiceRoamingType(2);
                    } else {
                        currentServiceState.setVoiceRoamingType(3);
                    }
                    boolean isInternationalRoaming = IsInternationalRoamingException(currentServiceState.getVoiceOperatorNumeric());
                    if (isInternationalRoaming) {
                        log(currentServiceState.getVoiceOperatorNumeric() + " is in operator defined international roaming list");
                        currentServiceState.setVoiceRoamingType(3);
                    }
                } else {
                    int[] intRoamingIndicators = this.mPhone.getContext().getResources().getIntArray(R.array.config_builtInDisplayIsRoundArray);
                    if (intRoamingIndicators != null && intRoamingIndicators.length > 0) {
                        currentServiceState.setVoiceRoamingType(2);
                        int curRoamingIndicator = currentServiceState.getCdmaRoamingIndicator();
                        int i = 0;
                        while (true) {
                            if (i >= intRoamingIndicators.length) {
                                break;
                            }
                            if (curRoamingIndicator != intRoamingIndicators[i]) {
                                i++;
                            } else {
                                currentServiceState.setVoiceRoamingType(3);
                                break;
                            }
                        }
                    } else if (inSameCountry(currentServiceState.getOperatorNumeric())) {
                        currentServiceState.setVoiceRoamingType(2);
                    } else {
                        currentServiceState.setVoiceRoamingType(3);
                    }
                }
            } else {
                currentServiceState.setVoiceRoamingType(0);
            }
        }
        boolean isDataInService = currentServiceState.getDataRegistrationState() == 0;
        int dataRegType = getRilDataRadioTechnologyForWwan(currentServiceState);
        if (isDataInService) {
            if (!currentServiceState.getDataRoaming()) {
                currentServiceState.setDataRoamingType(0);
                return;
            }
            if (this.mPhone.isPhoneTypeGsm()) {
                if (ServiceState.isGsm(dataRegType)) {
                    if (isVoiceInService) {
                        currentServiceState.setDataRoamingType(currentServiceState.getVoiceRoamingType());
                        return;
                    }
                    if (inSameCountry(currentServiceState.getVoiceOperatorNumeric())) {
                        currentServiceState.setDataRoamingType(2);
                    } else {
                        currentServiceState.setDataRoamingType(3);
                    }
                    boolean isInternationalRoaming2 = IsInternationalRoamingException(currentServiceState.getVoiceOperatorNumeric());
                    if (isInternationalRoaming2) {
                        log(currentServiceState.getVoiceOperatorNumeric() + " is in operator defined international roaming list");
                        currentServiceState.setDataRoamingType(3);
                        return;
                    }
                    return;
                }
                currentServiceState.setDataRoamingType(1);
                return;
            }
            if (ServiceState.isCdma(dataRegType)) {
                if (isVoiceInService) {
                    currentServiceState.setDataRoamingType(currentServiceState.getVoiceRoamingType());
                    return;
                } else {
                    currentServiceState.setDataRoamingType(1);
                    return;
                }
            }
            if (inSameCountry(currentServiceState.getOperatorNumeric())) {
                currentServiceState.setDataRoamingType(2);
            } else {
                currentServiceState.setDataRoamingType(3);
            }
        }
    }

    private final boolean isConcurrentVoiceAndDataAllowedForVolte() {
        if (this.mSS.getDataRegState() == 0 && ServiceState.isPsOnlyTech(this.mSS.getRilDataRadioTechnology()) && getImsServiceState() == 0) {
            return true;
        }
        return false;
    }

    private final int getImsServiceState() {
        Phone imsPhone = this.mPhone.getImsPhone();
        if (imsPhone != null) {
            return imsPhone.getServiceState().getState();
        }
        return 1;
    }

    protected boolean isOperatorConsideredNonRoaming(ServiceState s) {
        boolean result = super.isOperatorConsideredNonRoaming(s);
        if (result) {
            log("isOperatorConsideredNonRoaming true");
        }
        return result;
    }

    protected boolean isOperatorConsideredRoaming(ServiceState s) {
        boolean result;
        String[] opeartors = MtkTelephonyManagerEx.getDefault().getSimOperatorNumericForPhoneEx(this.mPhone.getPhoneId());
        String simNumeric = opeartors != null ? opeartors[0] : "";
        log("isOperatorConsideredRoaming simNumeric  = " + simNumeric);
        String operatorNumeric = s.getOperatorNumeric();
        if (!TextUtils.isEmpty(simNumeric) && ((simNumeric.substring(0, 3).equals("404") || simNumeric.substring(0, 3).equals("405")) && !TextUtils.isEmpty(operatorNumeric) && (operatorNumeric.substring(0, 3).equals("404") || operatorNumeric.substring(0, 3).equals("405")))) {
            result = true;
        } else {
            result = super.isOperatorConsideredRoaming(s);
        }
        if (result) {
            log("isOperatorConsideredRoaming true");
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onUpdateIccAvailability() {
        UiccCardApplication newUiccApplication;
        if (this.mUiccController != null && this.mUiccApplication != (newUiccApplication = getUiccCardApplication())) {
            if (this.mIccRecords instanceof SIMRecords) {
                this.mCdnr.updateEfFromUsim((SIMRecords) null);
            } else if (this.mIccRecords instanceof RuimRecords) {
                this.mCdnr.updateEfFromRuim((RuimRecords) null);
            }
            if (this.mUiccApplication != null) {
                log("Removing stale icc objects.");
                this.mUiccApplication.unregisterForReady(this);
                if (this.mIccRecords != null) {
                    this.mIccRecords.unregisterForRecordsLoaded(this);
                    if (this.mPhone.isPhoneTypeGsm()) {
                        this.mIccRecords.unregisterForRecordsEvents(this);
                    }
                }
                this.mIccRecords = null;
                this.mUiccApplication = null;
            }
            if (newUiccApplication != null) {
                logv("New card found");
                this.mUiccApplication = newUiccApplication;
                this.mIccRecords = this.mUiccApplication.getIccRecords();
                if (this.mPhone.isPhoneTypeGsm()) {
                    this.mUiccApplication.registerForReady(this, 17, (Object) null);
                    if (this.mIccRecords != null) {
                        this.mIccRecords.registerForRecordsLoaded(this, 16, (Object) null);
                        this.mIccRecords.registerForRecordsEvents(this, EVENT_SIM_OPL_LOADED, (Object) null);
                        return;
                    }
                    return;
                }
                if (this.mIsSubscriptionFromRuim) {
                    this.mUiccApplication.registerForReady(this, 26, (Object) null);
                    if (this.mIccRecords != null) {
                        this.mIccRecords.registerForRecordsLoaded(this, 27, (Object) null);
                    }
                }
            }
        }
    }

    protected String getOperatorNameFromEri() {
        String eriText = null;
        if (this.mPhone.isPhoneTypeCdma()) {
            if (this.mCi.getRadioState() != 1 || this.mIsSubscriptionFromRuim) {
                return null;
            }
            if (this.mSS.getState() == 0) {
                return this.mPhone.getCdmaEriText();
            }
            return this.mPhone.getContext().getText(R.string.permlab_modifyAudioSettings).toString();
        }
        if (!this.mPhone.isPhoneTypeCdmaLte()) {
            return null;
        }
        String simMccMnc = TelephonyManager.from(this.mPhone.getContext()).getSimOperatorNumericForPhone(getPhoneId());
        boolean z = false;
        if (simMccMnc != null && (simMccMnc.equals("310120") || simMccMnc.equals("310009") || simMccMnc.equals("311490") || simMccMnc.equals("311870"))) {
            this.mEnableERI = true;
        } else {
            this.mEnableERI = false;
        }
        boolean hasBrandOverride = (this.mUiccController.getUiccPort(getPhoneId()) == null || this.mUiccController.getUiccPort(getPhoneId()).getOperatorBrandOverride() == null) ? false : true;
        if (!hasBrandOverride && this.mCi.getRadioState() == 1 && this.mEriManager != null && this.mEriManager.isEriFileLoaded() && ((!ServiceState.isPsOnlyTech(this.mSS.getRilVoiceRadioTechnology()) || this.mPhone.getContext().getResources().getBoolean(R.bool.auto_data_switch_ping_test_before_switch)) && !this.mIsSubscriptionFromRuim && this.mEnableERI)) {
            eriText = this.mSS.getOperatorAlpha();
            if (this.mSS.getState() == 0) {
                if (TextUtils.isEmpty(eriText)) {
                    eriText = this.mPhone.getCdmaEriText();
                } else if (!TextUtils.isEmpty(this.mPhone.getCdmaEriText()) && this.mSS.getCdmaRoamingIndicator() != 1 && this.mSS.getCdmaRoamingIndicator() != 160) {
                    log("Append ERI text to PLMN String");
                    eriText = this.mSS.getOperatorAlphaLong() + "- " + this.mPhone.getCdmaEriText();
                }
            } else if (this.mSS.getState() == 3) {
                eriText = getServiceProviderName();
                if (TextUtils.isEmpty(eriText)) {
                    eriText = SystemProperties.get("ro.cdma.home.operator.alpha");
                }
            } else if (this.mSS.getDataRegistrationState() != 0) {
                eriText = this.mPhone.getContext().getText(R.string.permlab_modifyAudioSettings).toString();
            }
        }
        if (this.mUiccApplication != null && this.mUiccApplication.getState() == IccCardApplicationStatus.AppState.APPSTATE_READY && this.mIccRecords != null && getCombinedRegState(this.mSS) == 0 && !ServiceState.isPsOnlyTech(this.mSS.getRilVoiceRadioTechnology())) {
            boolean showSpn = this.mIccRecords.getCsimSpnDisplayCondition();
            if (showSpn) {
                try {
                    if (this.mServiceStateTrackerExt.allowSpnDisplayed()) {
                        z = true;
                    }
                    showSpn = z;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            } else {
                showSpn = z;
            }
            int iconIndex = this.mSS.getCdmaEriIconIndex();
            if (showSpn && iconIndex == 1 && isInHomeSidNid(this.mSS.getCdmaSystemId(), this.mSS.getCdmaNetworkId()) && this.mIccRecords != null) {
                return getServiceProviderName();
            }
            return eriText;
        }
        return eriText;
    }

    protected boolean mtkAreAllDataDisconnected(Phone phone) {
        boolean ret = true;
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) phone.getDataNetworkController();
        if (mtkDnc != null) {
            ret = mtkDnc.areAllDataDisconnectedExceptIms();
        }
        log("mtkAreAllDataDisconnected: " + ret);
        return ret;
    }

    public void setRadioPowerForReason(boolean power, boolean forEmergencyCall, boolean isSelectedPhoneForEmergencyCall, boolean forceApply, int reason) {
        int radioState = this.mCi.getRadioState();
        log("setRadioPowerForReason power:" + power + ", mDesiredPowerState:" + this.mDesiredPowerState + ", forEmergencyCall:" + forEmergencyCall + ", isSelectedPhoneForEmergencyCall:" + isSelectedPhoneForEmergencyCall + ", forceApply:" + forceApply + ", radioState:" + radioState + ", reason:" + reason);
        if (power) {
            if (forEmergencyCall) {
                this.mRadioPowerOffReasons.clear();
            } else {
                this.mRadioPowerOffReasons.remove(Integer.valueOf(reason));
            }
        } else {
            this.mRadioPowerOffReasons.add(Integer.valueOf(reason));
        }
        if (((power && radioState == 1) || (!power && radioState == 0 && !this.mDeviceShuttingDown)) && !forceApply) {
            this.mDesiredPowerState = power;
            log("setRadioPowerForReason mDesiredPowerState is already " + power + " Do nothing.");
        } else {
            if (power && !this.mRadioPowerOffReasons.isEmpty()) {
                log("setRadioPowerForReason power: " + power + " forEmergencyCall= " + forEmergencyCall + " isSelectedPhoneForEmergencyCall: " + isSelectedPhoneForEmergencyCall + " forceApply " + forceApply + "reason:" + reason + " will not power on the radio as it is powered off for the following reasons: " + this.mRadioPowerOffReasons + ".");
                return;
            }
            this.mDesiredPowerState = power;
            setPowerStateToDesired(forEmergencyCall, isSelectedPhoneForEmergencyCall, forceApply);
            if (forEmergencyCall) {
                RadioManager.getInstance().forceRefreshSimState(power, this.mPhone.getPhoneId());
            }
        }
    }

    public boolean hasPendingRadioPowerOff() {
        return this.mPendingRadioPowerOffAfterDataOff;
    }

    protected static final String lookupOperatorName(Context context, int subId, String numeric, boolean desireLongName) {
        String operName = null;
        boolean isChinaTelecomMvno = false;
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(subId));
        if (phone != null) {
            operName = MtkSpnOverride.getInstance().getSpnByPattern(subId, numeric);
            isChinaTelecomMvno = isChinaTelecomMvno(context, subId, numeric, operName);
        } else {
            Rlog.e(LOG_TAG, "lookupOperatorName getPhone null subid=" + subId);
        }
        if (operName == null) {
            operName = lookupCnSharingOperatorName(context, numeric, desireLongName);
        }
        if (operName == null || isChinaTelecomMvno) {
            operName = MtkSpnOverride.getInstance().getSpnByNumeric(numeric, desireLongName, context);
        }
        return operName == null ? numeric : operName;
    }

    private static final boolean isChinaTelecomMvno(Context context, int subId, String numeric, String mvnoOperName) {
        String simCarrierName = TelephonyManager.from(context).getSimOperatorName(subId);
        if ("".equals(mvnoOperName)) {
            return true;
        }
        if ((!"20404".equals(numeric) && !"45403".equals(numeric)) || !"".equals(simCarrierName)) {
            return false;
        }
        return true;
    }

    protected void mtkHangupAllImsCall() {
        Phone imsPhone = this.mPhone.getImsPhone();
        if (imsPhone != null) {
            if (!imsPhone.isWifiCallingEnabled() || this.mDeviceShuttingDown) {
                boolean sendNetworkCoverage = this.mCarrierConfig.getBoolean("mtk_send_network_coverage_lost");
                log("hangupAndPowerOff: sendNetworkCoverage = " + sendNetworkCoverage);
                if (sendNetworkCoverage) {
                    imsPhone.getForegroundCall().hangupIfAlive(ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_DONE_IND);
                    imsPhone.getBackgroundCall().hangupIfAlive(ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_DONE_IND);
                    imsPhone.getRingingCall().hangupIfAlive(ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_DONE_IND);
                    log("hangupAndPowerOff: RJIO: hangup VoLTE call.");
                    return;
                }
                imsPhone.getForegroundCall().hangupIfAlive();
                imsPhone.getBackgroundCall().hangupIfAlive();
                imsPhone.getRingingCall().hangupIfAlive();
                log("hangupAndPowerOff: hangup VoLTE call.");
            }
        }
    }

    public int getLacFromServiceState(ServiceState ss) {
        List<CellIdentity> prioritizedCids = getPrioritizedCellIdentities(ss);
        CellIdentity id = prioritizedCids.isEmpty() ? null : prioritizedCids.get(0);
        if (id == null) {
            return -1;
        }
        int lac = -1;
        switch (id.getType()) {
            case 1:
                lac = ((CellIdentityGsm) id).getLac();
                break;
            case 3:
                lac = ((CellIdentityLte) id).getTac();
                break;
            case 4:
                lac = ((CellIdentityWcdma) id).getLac();
                break;
            case 5:
                lac = ((CellIdentityTdscdma) id).getLac();
                break;
            case 6:
                lac = ((CellIdentityNr) id).getTac();
                break;
        }
        if (lac == Integer.MAX_VALUE) {
            return -1;
        }
        return lac;
    }

    public int getLac() {
        return getLacFromServiceState(this.mSS);
    }

    protected void notifySpnDisplayUpdate(CarrierDisplayNameData data) {
        int subId = this.mPhone.getSubId();
        if (this.mSubId != subId || data.shouldShowPlmn() != this.mCurShowPlmn || data.shouldShowSpn() != this.mCurShowSpn || !TextUtils.equals(data.getSpn(), this.mCurSpn) || !TextUtils.equals(data.getDataSpn(), this.mCurDataSpn) || !TextUtils.equals(data.getPlmn(), this.mCurPlmn)) {
            String log = String.format("updateSpnDisplay: changed sending intent, rule=%d, showPlmn='%b', plmn='%s', showSpn='%b', spn='%s', dataSpn='%s', subId='%d'", Integer.valueOf(getCarrierNameDisplayBitmask(this.mSS)), Boolean.valueOf(data.shouldShowPlmn()), data.getPlmn(), Boolean.valueOf(data.shouldShowSpn()), data.getSpn(), data.getDataSpn(), Integer.valueOf(subId));
            log("updateSpnDisplay: " + log);
            Intent intent = new Intent("android.telephony.action.SERVICE_PROVIDERS_UPDATED");
            intent.putExtra("android.telephony.extra.SHOW_SPN", data.shouldShowSpn());
            intent.putExtra("android.telephony.extra.SPN", data.getSpn());
            intent.putExtra("android.telephony.extra.DATA_SPN", data.getDataSpn());
            intent.putExtra("android.telephony.extra.SHOW_PLMN", data.shouldShowPlmn());
            intent.putExtra("android.telephony.extra.PLMN", data.getPlmn());
            intent.putExtra("hnbName", this.mHhbName);
            intent.putExtra("csgId", this.mCsgId);
            intent.putExtra("domain", this.mFemtocellDomain);
            intent.putExtra("femtocell", this.mIsFemtocell);
            if (TelephonyManager.getDefault().getPhoneCount() == 1) {
                intent.addFlags(536870912);
            }
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
            this.mPhone.getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
            updatePLMN(data.shouldShowPlmn(), data.getPlmn(), data.shouldShowSpn(), data.getSpn());
        }
        this.mCurShowSpn = data.shouldShowSpn();
        this.mCurShowPlmn = data.shouldShowPlmn();
        this.mCurSpn = data.getSpn();
        this.mCurDataSpn = data.getDataSpn();
        this.mCurPlmn = data.getPlmn();
    }

    protected boolean updateNrFrequencyRangeFromPhysicalChannelConfigs(List<PhysicalChannelConfig> physicalChannelConfigs, ServiceState ss) {
        if ((physicalChannelConfigs == null || physicalChannelConfigs.isEmpty()) && ss.getDataNetworkType() == 20) {
            int newFrequencyRange = 0;
            NetworkRegistrationInfo nri = ss.getNetworkRegistrationInfo(2, 1);
            if (nri != null && nri.getCellIdentity() != null) {
                CellIdentity cellIdentity = nri.getCellIdentity();
                if (cellIdentity.getType() == 6) {
                    int[] bands = ((CellIdentityNr) cellIdentity).getBands();
                    for (int band : bands) {
                        newFrequencyRange = Math.max(newFrequencyRange, AccessNetworkUtils.getFrequencyRangeGroupFromNrBand(band));
                        log("NR IDLE state, use ps band[" + band + "] update FrequencyRange to " + newFrequencyRange);
                    }
                }
            }
            boolean hasChanged = newFrequencyRange != ss.getNrFrequencyRange();
            if (hasChanged) {
                log(String.format("NR frequency range changed from %s to %s.", ServiceState.frequencyRangeToString(ss.getNrFrequencyRange()), ServiceState.frequencyRangeToString(newFrequencyRange)));
            }
            ss.setNrFrequencyRange(newFrequencyRange);
            return hasChanged;
        }
        return super.updateNrFrequencyRangeFromPhysicalChannelConfigs(physicalChannelConfigs, ss);
    }

    private static String getCnOpName(Context context, int id, boolean desireLongName) {
        switch (id) {
            case 1:
                if (desireLongName) {
                    return context.getText(134545437).toString();
                }
                return context.getText(134545444).toString();
            case 2:
                if (desireLongName) {
                    return context.getText(134545438).toString();
                }
                return context.getText(134545445).toString();
            case 3:
                if (desireLongName) {
                    return context.getText(134545507).toString();
                }
                return context.getText(134545508).toString();
            case 4:
                if (desireLongName) {
                    return context.getText(134545451).toString();
                }
                return context.getText(134545452).toString();
            default:
                return "";
        }
    }

    protected static final String lookupCnSharingOperatorName(Context context, String plmn, boolean desireLongName) {
        if (TextUtils.isEmpty(plmn) || !plmn.startsWith(RadioCapabilitySwitchUtil.CN_MCC)) {
            return null;
        }
        if (plmn.equals("46050")) {
            return getCnOpName(context, 1, desireLongName) + "-" + getCnOpName(context, 3, desireLongName);
        }
        if (plmn.equals("46051")) {
            return getCnOpName(context, 4, desireLongName) + "-" + getCnOpName(context, 3, desireLongName);
        }
        if (plmn.equals("46021")) {
            return getCnOpName(context, 3, desireLongName) + "-" + getCnOpName(context, 1, desireLongName);
        }
        if (plmn.equals("46022")) {
            return getCnOpName(context, 2, desireLongName) + "-" + getCnOpName(context, 1, desireLongName);
        }
        if (plmn.equals("46031")) {
            return getCnOpName(context, 1, desireLongName) + "-" + getCnOpName(context, 2, desireLongName);
        }
        if (plmn.equals("46032")) {
            return getCnOpName(context, 4, desireLongName) + "-" + getCnOpName(context, 2, desireLongName);
        }
        if (plmn.equals("46060")) {
            return getCnOpName(context, 3, desireLongName) + "-" + getCnOpName(context, 4, desireLongName);
        }
        if (plmn.equals("46061")) {
            return getCnOpName(context, 2, desireLongName) + "-" + getCnOpName(context, 4, desireLongName);
        }
        return null;
    }

    private static final boolean isCnSharedPlmn(String numeric) {
        if (TextUtils.isEmpty(numeric) || !numeric.startsWith(RadioCapabilitySwitchUtil.CN_MCC)) {
            return false;
        }
        String[] cnSharePlmns = {"46021", "46022", "46031", "46032", "46050", "46051", "46060", "46061"};
        for (String item : cnSharePlmns) {
            if (item.equals(numeric)) {
                return true;
            }
        }
        return false;
    }
}
