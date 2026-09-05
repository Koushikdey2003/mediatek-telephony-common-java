package com.mediatek.internal.telephony;

import android.R;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.cdma.CdmaMmiCode;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.MtkOperatorUtils;
import com.mediatek.internal.telephony.cdma.MtkCdmaMmiCode;
import com.mediatek.internal.telephony.cdma.MtkCdmaSubscriptionSourceManager;
import com.mediatek.internal.telephony.data.MtkDataHelper;
import com.mediatek.internal.telephony.data.MtkDataNetworkController;
import com.mediatek.internal.telephony.data.MtkDataProfileManager;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.gsm.MtkGsmMmiCode;
import com.mediatek.internal.telephony.gsm.MtkSuppCrssNotification;
import com.mediatek.internal.telephony.gsm.MtkSuppServiceNotification;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import com.mediatek.internal.telephony.imsphone.MtkLocalPhoneNumberUtils;
import com.mediatek.internal.telephony.phb.CsimPhbUtil;
import com.mediatek.internal.telephony.ratconfiguration.RatConfiguration;
import com.mediatek.internal.telephony.selfactivation.ISelfActivation;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import com.mediatek.internal.telephony.worldphone.IWorldPhone;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmCdmaPhone extends GsmCdmaPhone {
    public static final String ACT_TYPE_GSM = "0";
    public static final String ACT_TYPE_LTE = "7";
    public static final String ACT_TYPE_NR = "11";
    public static final String ACT_TYPE_UTRAN = "2";
    private static final String CFU_TIME_SLOT = "persist.vendor.radio.cfu.timeslot.";
    private static final boolean DBG = true;
    protected static final int EVENT_CIPHER_INDICATION = 1000;
    protected static final int EVENT_CRSS_IND = 1003;
    protected static final int EVENT_GET_APC_INFO = 1001;
    public static final int EVENT_GET_CALL_BARRING_COMPLETE = 2006;
    public static final int EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE = 109;
    public static final int EVENT_GET_CALL_WAITING_DONE = 301;
    public static final int EVENT_GET_CLIR_COMPLETE = 2004;
    protected static final int EVENT_MTK_BASE = 1000;
    protected static final int EVENT_OEM_RAW_URC = 1005;
    public static final int EVENT_SET_CALL_BARRING_COMPLETE = 2005;
    public static final int EVENT_SET_CALL_FORWARD_TIME_SLOT_DONE = 110;
    public static final int EVENT_SET_CALL_WAITING_DONE = 302;
    protected static final int EVENT_SET_SS_PROPERTY = 1004;
    protected static final int EVENT_SSN_EX = 1002;
    protected static final int EVENT_TURN_OFF_WIFI = 1006;
    public static final int EVENT_UNSOL_RADIO_CAPABILITY_CHANGED = 111;
    public static final String GSM_INDICATOR = "2G";
    public static final String LOG_TAG = "MtkGsmCdmaPhone";
    public static final String LTE_INDICATOR = "4G";
    public static final String NR_INDICATOR = "5G";
    public static final int NT_MODE_LTE_GSM = 101;
    public static final int NT_MODE_LTE_TDD_ONLY = 102;
    private static final int OPERATION_TIME_OUT_MILLIS = 3000;
    private static final String PROPERTY_DISABLE_AUTO_RETURN_RPLMN = "persist.vendor.radio.disable_auto_return_rplmn";
    private static final int PROPERTY_MODE_BOOL = 1;
    private static final int PROPERTY_MODE_INT = 0;
    private static final int PROPERTY_MODE_STRING = 2;
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    private static final String SS_SERVICE_CLASS_PROP = "vendor.gsm.radio.ss.sc";
    public static final String UTRAN_INDICATOR = "3G";
    private boolean enableFakeSS;
    private BroadcastReceiver mBroadcastReceiver;
    private AsyncResult mCachedCrssn;
    private AsyncResult mCachedSsn;
    RegistrantList mCallRelatedSuppSvcRegistrants;
    private CountDownLatch mCallbackLatch;
    private PersistableBundle mCarrierConfig;
    protected final RegistrantList mCipherIndicationRegistrants;
    private boolean mEccRadioOnStatus;
    private ExecutorService mExecutorService;
    private boolean mIsEccSelectedPhone;
    private final Object mLock;
    public MtkRIL mMtkCi;
    public MtkServiceStateTracker mMtkSST;
    private int mNewVoiceTech;
    protected final RegistrantList mOemIndRegistrants;
    private ISelfActivation mSelfActInstance;
    private boolean mSsOverCdma;
    private boolean mWifiIsEnabledBeforeE911;

    private static class Cfu {
        final Message mOnComplete;
        final int mServiceClass;
        final String mSetCfNumber;

        Cfu(String cfNumber, Message onComplete, int serviceClass) {
            this.mSetCfNumber = cfNumber;
            this.mOnComplete = onComplete;
            this.mServiceClass = serviceClass;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmCdmaPhone(Context context, CommandsInterface ci, PhoneNotifier notifier, boolean unitTestMode, int phoneId, int precisePhoneType, TelephonyComponentFactory telephonyComponentFactory) {
        super(context, ci, notifier, unitTestMode, phoneId, precisePhoneType, telephonyComponentFactory);
        this.mNewVoiceTech = -1;
        this.mLock = new Object();
        this.mCipherIndicationRegistrants = new RegistrantList();
        this.mCallRelatedSuppSvcRegistrants = new RegistrantList();
        this.mCachedSsn = null;
        this.mCachedCrssn = null;
        this.mEccRadioOnStatus = false;
        this.mIsEccSelectedPhone = false;
        this.mSelfActInstance = null;
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.mWifiIsEnabledBeforeE911 = false;
        this.enableFakeSS = false;
        this.mOemIndRegistrants = new RegistrantList();
        this.mCarrierConfig = null;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkGsmCdmaPhone.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                intent.getAction();
                if (intent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    MtkGsmCdmaPhone mtkGsmCdmaPhone = MtkGsmCdmaPhone.this;
                    mtkGsmCdmaPhone.sendMessage(mtkGsmCdmaPhone.obtainMessage(43));
                }
            }
        };
        this.mSsOverCdma = false;
        Rlog.d(LOG_TAG, "constructor: sub = " + phoneId);
        this.mMtkCi = (MtkRIL) ci;
        this.mMtkSST = (MtkServiceStateTracker) this.mSST;
        this.mMtkCi.registerForCipherIndication(this, 1000, null);
        OpTelephonyCustomizationFactoryBase telephonyCustomizationFactory = OpTelephonyCustomizationUtils.getOpFactory(context);
        ISelfActivation iSelfActivationMakeSelfActivationInstance = telephonyCustomizationFactory.makeSelfActivationInstance(phoneId);
        this.mSelfActInstance = iSelfActivationMakeSelfActivationInstance;
        iSelfActivationMakeSelfActivationInstance.setContext(context).setCommandsInterface(ci).buildParams();
        this.mCi.getVoiceRadioTechnology(obtainMessage(40));
        this.mIsCarrierNrSupported = DBG;
    }

    public MtkGsmCdmaPhone(Context context, CommandsInterface ci, PhoneNotifier notifier, int phoneId, int precisePhoneType, TelephonyComponentFactory telephonyComponentFactory) {
        this(context, ci, notifier, false, phoneId, precisePhoneType, telephonyComponentFactory);
    }

    public ISelfActivation getSelfActivationInstance() {
        return this.mSelfActInstance;
    }

    public boolean isDataSuspended() {
        MtkDataHelper dataHelper = MtkDataHelper.getInstance();
        if (dataHelper != null) {
            return dataHelper.isDataAllowedForConcurrent(getPhoneId()) ^ DBG;
        }
        return super.isDataSuspended();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void initOnce(CommandsInterface ci) {
        super.initOnce(ci);
        this.mCi.registerForRadioCapabilityChanged(this, EVENT_UNSOL_RADIO_CAPABILITY_CHANGED, (Object) null);
        if (this.mMtkCi == null) {
            this.mMtkCi = (MtkRIL) ci;
        }
        this.mMtkCi.setOnSuppServiceNotificationEx(this, 1002, null);
        this.mMtkCi.setOnCallRelatedSuppSvc(this, 1003, null);
        this.mMtkCi.setOnUnsolOemHookRaw(this, 1005, null);
    }

    protected void onUpdateIccAvailability() {
        super.onUpdateIccAvailability();
        UiccCardApplication newUiccApplication = getUiccCardApplication();
        UiccCardApplication app = (UiccCardApplication) this.mUiccApplication.get();
        IccRecords newIccRecord = newUiccApplication != null ? newUiccApplication.getIccRecords() : null;
        if (app == newUiccApplication && this.mIccRecords.get() != newIccRecord) {
            if (app != null) {
                logd("Removing stale icc objects.");
                if (this.mIccRecords.get() != null) {
                    unregisterForIccRecordEvents();
                    this.mIccPhoneBookIntManager.updateIccRecords((IccRecords) null);
                }
                this.mIccRecords.set(null);
                this.mUiccApplication.set(null);
            }
            if (newUiccApplication != null) {
                logd("New Uicc application found. type = " + newUiccApplication.getType());
                this.mUiccApplication.set(newUiccApplication);
                this.mIccRecords.set(newUiccApplication.getIccRecords());
                registerForIccRecordEvents();
                this.mIccPhoneBookIntManager.updateIccRecords((IccRecords) this.mIccRecords.get());
            }
        }
        Rlog.d(LOG_TAG, "isPhoneTypeCdmaLte:" + isPhoneTypeCdmaLte() + ", phoneId: " + getPhoneId() + " isCdmaWithoutLteCard: " + isCdmaWithoutLteCard() + " mNewVoiceTech: " + this.mNewVoiceTech);
        if (this.mNewVoiceTech != -1) {
            if ((isPhoneTypeCdmaLte() && isCdmaWithoutLteCard()) || (isPhoneTypeCdma() && !isCdmaWithoutLteCard())) {
                updatePhoneObject(this.mNewVoiceTech);
            }
        }
    }

    protected boolean correctPhoneTypeForCdma(boolean matchCdma, int newVoiceRadioTech) {
        boolean phoneTypeChanged = false;
        if (matchCdma && getPhoneType() == 2) {
            UiccProfile uiccProfile = getUiccProfile();
            if (uiccProfile != null && uiccProfileUpdateIfNeeded()) {
                uiccProfile.setVoiceRadioTech(newVoiceRadioTech);
            }
            phoneTypeChanged = DBG;
        }
        if ((!isPhoneTypeCdmaLte() || !isCdmaWithoutLteCard()) && (!isPhoneTypeCdma() || isCdmaWithoutLteCard())) {
            phoneTypeChanged = false;
        }
        Rlog.d(LOG_TAG, "correctPhoneTypeForCdma: change:" + phoneTypeChanged + " newVoiceRadioTech=" + newVoiceRadioTech + " mActivePhone=" + getPhoneName());
        return phoneTypeChanged;
    }

    protected void switchVoiceRadioTech(int newVoiceRadioTech) {
        logd("Switching Voice Phone : " + getPhoneName() + " >>> " + (ServiceState.isGsm(newVoiceRadioTech) ? "GSM" : "CDMA"));
        if (ServiceState.isCdma(newVoiceRadioTech) && isCdmaWithoutLteCard()) {
            switchPhoneType(2);
        } else {
            super.switchVoiceRadioTech(newVoiceRadioTech);
        }
    }

    private boolean isCdmaWithoutLteCard() {
        int iccFamily = MtkTelephonyManagerEx.getDefault().getIccAppFamily(getPhoneId());
        if (iccFamily == 2) {
            return DBG;
        }
        return false;
    }

    public void triggerModeSwitchByEcc(int mode, Message response) {
        this.mMtkCi.triggerModeSwitchByEcc(mode, response);
    }

    public String getLocatedPlmn() {
        return this.mMtkSST.getLocatedPlmn();
    }

    public void selectNetworkManually(OperatorInfo network, boolean persistSelection, Message response) {
        Phone.NetworkSelectMessage nsm = new Phone.NetworkSelectMessage();
        nsm.message = response;
        nsm.operatorNumeric = network.getOperatorNumeric();
        nsm.operatorAlphaLong = network.getOperatorAlphaLong();
        nsm.operatorAlphaShort = network.getOperatorAlphaShort();
        Message msg = obtainMessage(16, nsm);
        isPhoneTypeGsm();
        this.mCi.setNetworkSelectionModeManual(network.getOperatorNumeric(), network.getRan(), msg);
        if (persistSelection) {
            updateSavedNetworkOperator(nsm);
        } else {
            clearSavedNetworkSelection();
        }
        updateManualNetworkSelection(nsm);
    }

    public void setNetworkSelectionModeSemiAutomatic(OperatorInfo network, Message response) {
        Phone.NetworkSelectMessage nsm = new Phone.NetworkSelectMessage();
        nsm.message = response;
        nsm.operatorNumeric = "";
        nsm.operatorAlphaLong = "";
        nsm.operatorAlphaShort = "";
        Message msg = obtainMessage(17, nsm);
        Rlog.d(LOG_TAG, "MTK GSMPhone setNetworkSelectionModeSemiAutomatic:" + network);
        String actype = "0";
        if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(UTRAN_INDICATOR)) {
            actype = ACT_TYPE_UTRAN;
        } else if (network.getOperatorAlphaLong() != null && network.getOperatorAlphaLong().endsWith(LTE_INDICATOR)) {
            actype = ACT_TYPE_LTE;
        }
        this.mMtkCi.setNetworkSelectionModeManualWithAct(network.getOperatorNumeric(), actype, 1, msg);
    }

    public void getAvailableNetworks(Message response) {
        if (isPhoneTypeGsm() || isPhoneTypeCdmaLte()) {
            this.mCi.getAvailableNetworks(response);
        } else {
            Rlog.d(LOG_TAG, "getAvailableNetworks: not possible in CDMA");
        }
    }

    public synchronized void cancelAvailableNetworks(Message response) {
        Rlog.d(LOG_TAG, "cancelAvailableNetworks");
        this.mMtkCi.cancelAvailableNetworks(response);
    }

    public void getFemtoCellList(Message response) {
        Rlog.d(LOG_TAG, "getFemtoCellList()");
        this.mMtkCi.getFemtoCellList(response);
    }

    public void abortFemtoCellList(Message response) {
        Rlog.d(LOG_TAG, "abortFemtoCellList()");
        this.mMtkCi.abortFemtoCellList(response);
    }

    public void selectFemtoCell(FemtoCellInfo femtocell, Message response) {
        Rlog.d(LOG_TAG, "selectFemtoCell(): " + femtocell);
        this.mMtkCi.selectFemtoCell(femtocell, response);
    }

    public void queryFemtoCellSystemSelectionMode(Message response) {
        Rlog.d(LOG_TAG, "queryFemtoCellSystemSelectionMode()");
        this.mMtkCi.queryFemtoCellSystemSelectionMode(response);
    }

    public void setFemtoCellSystemSelectionMode(int mode, Message response) {
        Rlog.d(LOG_TAG, "setFemtoCellSystemSelectionMode(), mode=" + mode);
        this.mMtkCi.setFemtoCellSystemSelectionMode(mode, response);
    }

    public String getMvnoPattern(String type) {
        String pattern = "";
        synchronized (this.mLock) {
            if (isPhoneTypeGsm() && this.mIccRecords.get() != null) {
                if (type.equals("spn")) {
                    pattern = ((MtkSIMRecords) this.mIccRecords.get()).getSpNameInEfSpn();
                } else if (type.equals("imsi")) {
                    pattern = ((MtkSIMRecords) this.mIccRecords.get()).isOperatorMvnoForImsi();
                } else if (type.equals("pnn")) {
                    pattern = ((MtkSIMRecords) this.mIccRecords.get()).isOperatorMvnoForEfPnn();
                } else if (type.equals("gid")) {
                    pattern = ((IccRecords) this.mIccRecords.get()).getGid1();
                } else {
                    Rlog.d(LOG_TAG, "getMvnoPattern: Wrong type = " + type);
                }
            }
        }
        return pattern;
    }

    public String getMvnoMatchType() {
        String type = "";
        synchronized (this.mLock) {
            if (isPhoneTypeGsm()) {
                if (this.mIccRecords.get() != null) {
                    type = ((MtkSIMRecords) this.mIccRecords.get()).getMvnoMatchType();
                }
                Rlog.d(LOG_TAG, "getMvnoMatchType: Type = " + type);
            }
        }
        return type;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangupAll() throws CallStateException {
        ((MtkGsmCdmaCallTracker) this.mCT).hangupAll();
    }

    public Call getCSRingingCall() {
        return this.mCT.mRingingCall;
    }

    boolean isInCSCall() {
        Call.State foregroundCallState = getForegroundCall().getState();
        Call.State backgroundCallState = getBackgroundCall().getState();
        Call.State ringingCallState = getCSRingingCall().getState();
        if (foregroundCallState.isAlive() || backgroundCallState.isAlive() || ringingCallState.isAlive()) {
            return DBG;
        }
        return false;
    }

    public void registerForCipherIndication(Handler h, int what, Object obj) {
        this.mCipherIndicationRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForCipherIndication(Handler h) {
        this.mCipherIndicationRegistrants.remove(h);
    }

    public void handleMessage(Message msg) {
        MtkSuppServHelper ssHelper;
        String errorMsg;
        MtkSuppServHelper ssHelper2;
        String errorMsg2;
        MtkSuppServHelper ssHelper3;
        String errorMsg3;
        MtkSuppServHelper ssHelper4;
        String errorMsg4;
        MtkSuppServHelper ssHelper5;
        String errorMsg5;
        MtkGsmCdmaConnection cn;
        MtkSuppServHelper ssHelper6;
        String errorMsg6;
        MtkSuppServHelper ssHelper7;
        String errorMsg7;
        MtkSuppServHelper ssHelper8;
        String errorMsg8;
        switch (msg.what) {
            case 2:
                logd("Event EVENT_SSN Received");
                if (isPhoneTypeGsm()) {
                    SuppServiceNotification not = (SuppServiceNotification) ((AsyncResult) msg.obj).result;
                    if (not.notificationType == 1 && not.code == 0) {
                        logd("skip AOSP event for MT forwarded call notification");
                        return;
                    }
                    AsyncResult ar = new AsyncResult((Object) null, not, (Throwable) null);
                    if (this.mSsnRegistrants.size() == 0) {
                        this.mCachedSsn = ar;
                    }
                    this.mSsnRegistrants.notifyRegistrants(ar);
                    return;
                }
                return;
            case 8:
            case 45:
                String msgStr = "(unknown msg)";
                if (msg.what == 8) {
                    msgStr = "EVENT_RADIO_OFF_OR_NOT_AVAILABLE";
                } else if (msg.what == 45) {
                    msgStr = "EVENT_MODEM_RESET";
                }
                Rlog.d(LOG_TAG, msgStr + "received.");
                setEccRadioOnStatus(false, false);
                super.handleMessage(msg);
                return;
            case 12:
                AsyncResult ar2 = (AsyncResult) msg.obj;
                IccRecords r = (IccRecords) this.mIccRecords.get();
                Cfu cfu = (Cfu) ar2.userObj;
                if (ar2.exception == null && r != null) {
                    if ((cfu.mServiceClass & 1) != 0) {
                        exception = msg.arg1 == 1;
                        setVoiceCallForwardingFlag(1, exception, cfu.mSetCfNumber);
                    }
                } else if (ar2.exception != null) {
                    CommandException cmdException = ar2.exception;
                    CommandException.Error err = cmdException.getCommandError();
                    if (isUtErrorHasMsg(err) && cmdException.getMessage() != null && cmdException.getMessage().isEmpty() && (ssHelper = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg = ssHelper.getXCAPErrorMessageFromSysProp(err)) != null && !errorMsg.isEmpty()) {
                        Rlog.d(LOG_TAG, "Create OEM error = " + err + ", msg =" + errorMsg);
                        ar2.exception = new CommandException(err, errorMsg);
                    }
                }
                if (cfu.mOnComplete != null) {
                    AsyncResult.forMessage(cfu.mOnComplete, ar2.result, ar2.exception);
                    cfu.mOnComplete.sendToTarget();
                    return;
                }
                return;
            case 13:
                Rlog.d(LOG_TAG, "mPhoneId= " + this.mPhoneId + "subId=" + getSubId());
                AsyncResult ar3 = (AsyncResult) msg.obj;
                if (ar3.exception == null) {
                    handleCfuQueryResult((CallForwardInfo[]) ar3.result);
                } else {
                    CommandException cmdException2 = ar3.exception;
                    CommandException.Error err2 = cmdException2.getCommandError();
                    if (isUtErrorHasMsg(err2) && cmdException2.getMessage() != null && cmdException2.getMessage().isEmpty() && (ssHelper2 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg2 = ssHelper2.getXCAPErrorMessageFromSysProp(err2)) != null && !errorMsg2.isEmpty()) {
                        Rlog.d(LOG_TAG, "Create OEM error = " + err2 + ", msg =" + errorMsg2);
                        ar3.exception = new CommandException(err2, errorMsg2);
                    }
                }
                Message onComplete = (Message) ar3.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, ar3.result, ar3.exception);
                    onComplete.sendToTarget();
                    return;
                }
                return;
            case 16:
                super.handleMessage(msg);
                if (isPhoneTypeGsm()) {
                    AsyncResult ar4 = (AsyncResult) msg.obj;
                    boolean disable_auto_return_rplmn = SystemProperties.getInt(PROPERTY_DISABLE_AUTO_RETURN_RPLMN, 1) > 0;
                    boolean restoreSelection = this.mContext.getResources().getBoolean(R.bool.config_subscription_database_async_update) ^ DBG;
                    if (ar4 != null && ar4.exception != null) {
                        exception = true;
                    }
                    Rlog.d(LOG_TAG, "EVENT_SET_NETWORK_MANUAL_COMPLETE, restoreSelection=" + restoreSelection + ", disable_auto_return_rplmn=" + disable_auto_return_rplmn + ", exception=" + exception);
                    if (!restoreSelection && exception && !disable_auto_return_rplmn) {
                        clearSavedNetworkSelection();
                        this.mCi.setNetworkSelectionModeAutomatic((Message) null);
                        return;
                    }
                    return;
                }
                return;
            case 18:
                Rlog.d(LOG_TAG, "EVENT_SET_CLIR_COMPLETE");
                AsyncResult ar5 = (AsyncResult) msg.obj;
                if (ar5.exception == null) {
                    saveClirSetting(msg.arg1);
                }
                if (ar5.exception != null && (ar5.exception instanceof CommandException)) {
                    CommandException cmdException3 = ar5.exception;
                    CommandException.Error err3 = cmdException3.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_SET_CLIR_COMPLETE: cmdException error:" + err3);
                    if (cmdException3 != null) {
                        if ((isOp(MtkOperatorUtils.OPID.OP01) || isOp(MtkOperatorUtils.OPID.OP02)) && isUtError(cmdException3.getCommandError())) {
                            Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                            ar5.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                        } else if (isUtErrorHasMsg(err3)) {
                            if (cmdException3.getMessage() != null && cmdException3.getMessage().isEmpty() && (ssHelper3 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg3 = ssHelper3.getXCAPErrorMessageFromSysProp(err3)) != null && !errorMsg3.isEmpty()) {
                                Rlog.d(LOG_TAG, "Create OEM error = " + err3 + ", msg =" + errorMsg3);
                                ar5.exception = new CommandException(err3, errorMsg3);
                            }
                        } else {
                            Rlog.d(LOG_TAG, "return Original Error");
                        }
                    }
                }
                Message onComplete2 = (Message) ar5.userObj;
                if (onComplete2 != null) {
                    AsyncResult.forMessage(onComplete2, ar5.result, ar5.exception);
                    onComplete2.sendToTarget();
                    return;
                }
                return;
            case 25:
                if (!isPhoneTypeGsm() && (this.mImsPhone == null || this.mImsPhone.getServiceState().getState() != 0 || !"OP12".equals(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, "")))) {
                    boolean inEcm = isInEcm();
                    super.handleMessage(msg);
                    if (!inEcm) {
                        notifyEmergencyCallRegistrants(DBG);
                        return;
                    }
                    return;
                }
                super.handleMessage(msg);
                return;
            case 26:
                super.handleMessage(msg);
                if (isOP12()) {
                    sendMessageDelayed(obtainMessage(1006), 3000L);
                    return;
                }
                return;
            case IWorldPhone.EVENT_REG_SUSPENDED_1 /* 30 */:
                AsyncResult ar6 = (AsyncResult) msg.obj;
                Rlog.d(LOG_TAG, "EVENT_ICC_CHANGED, phone id = " + getPhoneId() + ", result = " + ((Integer) ar6.result));
                if (ar6.userObj != null || (ar6.result != null && getPhoneId() == ((Integer) ar6.result).intValue())) {
                    super.handleMessage(msg);
                    return;
                }
                return;
            case 109:
                Rlog.d(LOG_TAG, "mPhoneId = " + this.mPhoneId + ", subId = " + getSubId());
                AsyncResult ar7 = (AsyncResult) msg.obj;
                Rlog.d(LOG_TAG, "[EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE]ar.exception = " + ar7.exception);
                if (ar7.exception == null) {
                    handleCfuInTimeSlotQueryResult((MtkCallForwardInfo[]) ar7.result);
                }
                Rlog.d(LOG_TAG, "[EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE]msg.arg1 = " + msg.arg1);
                if (ar7.exception != null && (ar7.exception instanceof CommandException)) {
                    CommandException cmdException4 = ar7.exception;
                    Rlog.d(LOG_TAG, "[EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE] cmdException error:" + cmdException4.getCommandError());
                    if (msg.arg1 == 1 && cmdException4 != null && cmdException4.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED && this.mSST != null && this.mSST.mSS != null && this.mSST.mSS.getState() == 0) {
                        getCallForwardingOption(0, obtainMessage(13));
                    }
                    if (cmdException4 != null && cmdException4.getCommandError() == CommandException.Error.OEM_ERROR_2) {
                        Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                        ar7.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                    }
                }
                Message onComplete3 = (Message) ar7.userObj;
                if (onComplete3 != null) {
                    AsyncResult.forMessage(onComplete3, ar7.result, ar7.exception);
                    onComplete3.sendToTarget();
                    return;
                }
                return;
            case 110:
                AsyncResult ar8 = (AsyncResult) msg.obj;
                IccRecords records = (IccRecords) this.mIccRecords.get();
                CfuEx cfuEx = (CfuEx) ar8.userObj;
                if (ar8.exception == null && records != null) {
                    exception = msg.arg1 == 1;
                    records.setVoiceCallForwardingFlag(1, exception, cfuEx.mSetCfNumber);
                    saveTimeSlot(cfuEx.mSetTimeSlot);
                }
                if (cfuEx.mOnComplete != null) {
                    AsyncResult.forMessage(cfuEx.mOnComplete, ar8.result, ar8.exception);
                    cfuEx.mOnComplete.sendToTarget();
                    return;
                }
                return;
            case EVENT_UNSOL_RADIO_CAPABILITY_CHANGED /* 111 */:
                AsyncResult ar9 = (AsyncResult) msg.obj;
                RadioCapability rc_unsol = (RadioCapability) ar9.result;
                if (ar9.exception != null) {
                    Rlog.d(LOG_TAG, "RIL_UNSOL_RADIO_CAPABILITY fail, don't change capability");
                } else {
                    radioCapabilityUpdated(rc_unsol, false);
                }
                Rlog.d(LOG_TAG, "EVENT_UNSOL_RADIO_CAPABILITY_CHANGED: rc: " + rc_unsol);
                return;
            case EVENT_GET_CALL_WAITING_DONE /* 301 */:
                AsyncResult ar10 = (AsyncResult) msg.obj;
                Rlog.d(LOG_TAG, "[EVENT_GET_CALL_WAITING_]ar.exception = " + ar10.exception);
                Message onComplete4 = (Message) ar10.userObj;
                if (ar10.exception == null) {
                    int[] cwArray = (int[]) ar10.result;
                    try {
                        Rlog.d(LOG_TAG, "EVENT_GET_CALL_WAITING_DONE cwArray[0]:cwArray[1] = " + cwArray[0] + ":" + cwArray[1]);
                        if (cwArray[0] != 1 || (cwArray[1] & 1) == 1) {
                        }
                        if (onComplete4 != null) {
                            AsyncResult.forMessage(onComplete4, ar10.result, (Throwable) null);
                            onComplete4.sendToTarget();
                            return;
                        }
                        return;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Rlog.e(LOG_TAG, "EVENT_GET_CALL_WAITING_DONE: improper result: err =" + e.getMessage());
                        if (onComplete4 != null) {
                            AsyncResult.forMessage(onComplete4, ar10.result, (Throwable) null);
                            onComplete4.sendToTarget();
                            return;
                        }
                        return;
                    }
                }
                CommandException cmdException5 = ar10.exception;
                CommandException.Error err4 = cmdException5.getCommandError();
                if (isUtErrorHasMsg(err4) && cmdException5.getMessage() != null && cmdException5.getMessage().isEmpty() && (ssHelper4 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg4 = ssHelper4.getXCAPErrorMessageFromSysProp(err4)) != null && !errorMsg4.isEmpty()) {
                    Rlog.d(LOG_TAG, "Create OEM error = " + err4 + ", msg =" + errorMsg4);
                    ar10.exception = new CommandException(err4, errorMsg4);
                }
                if (onComplete4 != null) {
                    AsyncResult.forMessage(onComplete4, ar10.result, ar10.exception);
                    onComplete4.sendToTarget();
                    return;
                }
                return;
            case EVENT_SET_CALL_WAITING_DONE /* 302 */:
                AsyncResult ar11 = (AsyncResult) msg.obj;
                Message onComplete5 = (Message) ar11.userObj;
                Rlog.d(LOG_TAG, "EVENT_SET_CALL_WAITING_DONE: ar.exception=" + ar11.exception);
                if (ar11.exception != null) {
                    CommandException cmdException6 = ar11.exception;
                    CommandException.Error err5 = cmdException6.getCommandError();
                    if (isUtErrorHasMsg(err5) && cmdException6.getMessage() != null && cmdException6.getMessage().isEmpty() && (ssHelper5 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg5 = ssHelper5.getXCAPErrorMessageFromSysProp(err5)) != null && !errorMsg5.isEmpty()) {
                        Rlog.d(LOG_TAG, "Create OEM error = " + err5 + ", msg =" + errorMsg5);
                        ar11.exception = new CommandException(err5, errorMsg5);
                    }
                    if (onComplete5 != null) {
                        AsyncResult.forMessage(onComplete5, ar11.result, ar11.exception);
                        onComplete5.sendToTarget();
                        return;
                    }
                    return;
                }
                if (onComplete5 != null) {
                    AsyncResult.forMessage(onComplete5, (Object) null, (Throwable) null);
                    onComplete5.sendToTarget();
                    return;
                }
                return;
            case 1001:
                Rlog.d(LOG_TAG, "handle EVENT_GET_APC_INFO");
                AsyncResult ar12 = (AsyncResult) msg.obj;
                PseudoCellInfoResult result = (PseudoCellInfoResult) ar12.userObj;
                if (result == null) {
                    Rlog.e(LOG_TAG, "EVENT_GET_APC_INFO: result return null");
                    return;
                }
                synchronized (result.lockObj) {
                    if (ar12.exception != null) {
                        Rlog.d(LOG_TAG, "EVENT_GET_APC_INFO: error ret null, e=" + ar12.exception);
                        result.infos = null;
                    } else {
                        int[] msgs = (int[]) ar12.result;
                        result.infos = new PseudoCellInfo(msgs);
                    }
                    result.lockObj.notify();
                    break;
                }
                return;
            case 1002:
                logd("Event EVENT_SSN_EX Received");
                if (isPhoneTypeGsm()) {
                    MtkSuppServiceNotification not2 = (MtkSuppServiceNotification) ((AsyncResult) msg.obj).result;
                    if (not2.notificationType == 1) {
                        if (not2.code == 0 || not2.code >= 11) {
                            AsyncResult ar13 = new AsyncResult((Object) null, not2, (Throwable) null);
                            if (this.mSsnRegistrants.size() == 0) {
                                this.mCachedSsn = ar13;
                            }
                            this.mSsnRegistrants.notifyRegistrants(ar13);
                            return;
                        }
                        logd("Unexpected SSN_EX code:" + not2.code);
                        return;
                    }
                    return;
                }
                return;
            case 1003:
                AsyncResult ar14 = (AsyncResult) msg.obj;
                MtkSuppCrssNotification noti = (MtkSuppCrssNotification) ar14.result;
                if (noti.code == 3) {
                    Rlog.d(LOG_TAG, "[COLP]noti.number = " + pii(noti.number));
                    if (getForegroundCall().getState() != Call.State.IDLE && (cn = (MtkGsmCdmaConnection) getForegroundCall().getConnections().get(0)) != null && cn.getAddress() != null && !cn.getAddress().equals(noti.number)) {
                        cn.setRedirectingAddress(noti.number);
                        Rlog.d(LOG_TAG, "[COLP]Redirecting address = " + pii(cn.getRedirectingAddress()));
                    }
                }
                if (this.mCallRelatedSuppSvcRegistrants.size() == 0) {
                    this.mCachedCrssn = ar14;
                }
                this.mCallRelatedSuppSvcRegistrants.notifyRegistrants(ar14);
                return;
            case 1004:
                CountDownLatch countDownLatch = this.mCallbackLatch;
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
                Rlog.d(LOG_TAG, "EVENT_SET_SS_PROPERTY done");
                return;
            case 1005:
                AsyncResult ar15 = (AsyncResult) msg.obj;
                if (ar15 != null) {
                    byte[] response = (byte[]) ar15.result;
                    RegistrantList registrantList = this.mOemIndRegistrants;
                    if (registrantList != null && response != null) {
                        registrantList.notifyRegistrants(new AsyncResult((Object) null, new String(response), (Throwable) null));
                        return;
                    }
                    return;
                }
                return;
            case 1006:
                if (!this.mWifiIsEnabledBeforeE911) {
                    Rlog.d(LOG_TAG, "Turn off wifi radio when exit ECBM for Vzw E911 CCP");
                    WifiManager wifiMngr = (WifiManager) this.mContext.getSystemService("wifi");
                    wifiMngr.setScanAlwaysAvailable(false);
                    return;
                }
                return;
            case EVENT_GET_CLIR_COMPLETE /* 2004 */:
                Rlog.d(LOG_TAG, "EVENT_GET_CLIR_COMPLETE");
                AsyncResult ar16 = (AsyncResult) msg.obj;
                if (ar16.exception != null && (ar16.exception instanceof CommandException)) {
                    CommandException cmdException7 = ar16.exception;
                    CommandException.Error err6 = cmdException7.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_GET_CLIR_COMPLETE: cmdException error:" + err6);
                    if (cmdException7 != null) {
                        if (isOp(MtkOperatorUtils.OPID.OP01) || isOp(MtkOperatorUtils.OPID.OP02)) {
                            if (isUtError(cmdException7.getCommandError())) {
                                Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                                ar16.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                            } else {
                                Rlog.d(LOG_TAG, "return Original Error");
                            }
                        } else if (isUtErrorHasMsg(err6) && cmdException7.getMessage() != null && cmdException7.getMessage().isEmpty() && (ssHelper6 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg6 = ssHelper6.getXCAPErrorMessageFromSysProp(err6)) != null && !errorMsg6.isEmpty()) {
                            Rlog.d(LOG_TAG, "Create OEM error = " + err6 + ", msg =" + errorMsg6);
                            ar16.exception = new CommandException(err6, errorMsg6);
                        }
                    }
                }
                Message onComplete6 = (Message) ar16.userObj;
                if (onComplete6 != null) {
                    AsyncResult.forMessage(onComplete6, ar16.result, ar16.exception);
                    onComplete6.sendToTarget();
                    return;
                }
                return;
            case EVENT_SET_CALL_BARRING_COMPLETE /* 2005 */:
                Rlog.d(LOG_TAG, "EVENT_SET_CALL_BARRING_COMPLETE");
                AsyncResult ar17 = (AsyncResult) msg.obj;
                if (ar17.exception != null && (ar17.exception instanceof CommandException)) {
                    CommandException cmdException8 = ar17.exception;
                    CommandException.Error err7 = cmdException8.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_SET_CALL_BARRING_COMPLETE: cmdException error:" + err7);
                    if (cmdException8 != null) {
                        if (isOp(MtkOperatorUtils.OPID.OP01)) {
                            if (isUtError(cmdException8.getCommandError())) {
                                Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                                ar17.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                            } else {
                                Rlog.d(LOG_TAG, "return Original Error");
                            }
                        } else if (isUtErrorHasMsg(err7) && cmdException8.getMessage() != null && cmdException8.getMessage().isEmpty() && (ssHelper7 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg7 = ssHelper7.getXCAPErrorMessageFromSysProp(err7)) != null && !errorMsg7.isEmpty()) {
                            Rlog.d(LOG_TAG, "Create OEM error = " + err7 + ", msg =" + errorMsg7);
                            ar17.exception = new CommandException(err7, errorMsg7);
                        }
                    }
                }
                Message onComplete7 = (Message) ar17.userObj;
                if (onComplete7 != null) {
                    AsyncResult.forMessage(onComplete7, ar17.result, ar17.exception);
                    onComplete7.sendToTarget();
                    return;
                }
                return;
            case EVENT_GET_CALL_BARRING_COMPLETE /* 2006 */:
                Rlog.d(LOG_TAG, "EVENT_GET_CALL_BARRING_COMPLETE");
                AsyncResult ar18 = (AsyncResult) msg.obj;
                if (ar18.exception != null && (ar18.exception instanceof CommandException)) {
                    CommandException cmdException9 = ar18.exception;
                    CommandException.Error err8 = cmdException9.getCommandError();
                    Rlog.d(LOG_TAG, "EVENT_GET_CALL_BARRING_COMPLETE: cmdException error:" + err8);
                    if (cmdException9 != null) {
                        if (isOp(MtkOperatorUtils.OPID.OP01) || isOp(MtkOperatorUtils.OPID.OP09)) {
                            if (isUtError(cmdException9.getCommandError())) {
                                Rlog.d(LOG_TAG, "return REQUEST_NOT_SUPPORTED");
                                ar18.exception = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
                            } else {
                                Rlog.d(LOG_TAG, "return Original Error");
                            }
                        } else if (isUtErrorHasMsg(err8) && cmdException9.getMessage() != null && cmdException9.getMessage().isEmpty() && (ssHelper8 = MtkSuppServManager.getSuppServHelper(getPhoneId())) != null && (errorMsg8 = ssHelper8.getXCAPErrorMessageFromSysProp(err8)) != null && !errorMsg8.isEmpty()) {
                            Rlog.d(LOG_TAG, "Create OEM error = " + err8 + ", msg =" + errorMsg8);
                            ar18.exception = new CommandException(err8, errorMsg8);
                        }
                    }
                }
                Message onComplete8 = (Message) ar18.userObj;
                if (onComplete8 != null) {
                    AsyncResult.forMessage(onComplete8, ar18.result, ar18.exception);
                    onComplete8.sendToTarget();
                    return;
                }
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    public void setApcMode(int apcMode, boolean reportOn, int reportInterval) {
        if (isPhoneTypeGsm()) {
            this.mMtkCi.setApcMode(apcMode, reportOn, reportInterval, null);
        } else {
            Rlog.d(LOG_TAG, "setApcMode: not possible in CDMA");
        }
    }

    private class PseudoCellInfoResult {
        PseudoCellInfo infos;
        Object lockObj;

        private PseudoCellInfoResult() {
            this.infos = null;
            this.lockObj = new Object();
        }
    }

    public PseudoCellInfo getApcInfo() {
        if (isPhoneTypeGsm()) {
            PseudoCellInfoResult result = new PseudoCellInfoResult();
            synchronized (result.lockObj) {
                result.infos = null;
                this.mMtkCi.getApcInfo(obtainMessage(1001, result));
                try {
                    result.lockObj.wait(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (result.lockObj) {
                if (result.infos != null) {
                    Rlog.d(LOG_TAG, "getApcInfo return: list.size = " + result.infos.toString());
                    return result.infos;
                }
                Rlog.d(LOG_TAG, "getApcInfo return null");
            }
        } else {
            Rlog.d(LOG_TAG, "getApcInfo: not possible in CDMA");
        }
        return null;
    }

    public void registerForCrssSuppServiceNotification(Handler h, int what, Object obj) {
        this.mCallRelatedSuppSvcRegistrants.addUnique(h, what, obj);
        AsyncResult asyncResult = this.mCachedCrssn;
        if (asyncResult != null) {
            this.mCallRelatedSuppSvcRegistrants.notifyRegistrants(asyncResult);
            this.mCachedCrssn = null;
        }
    }

    public void unregisterForCrssSuppServiceNotification(Handler h) {
        this.mCallRelatedSuppSvcRegistrants.remove(h);
        this.mCachedCrssn = null;
    }

    public void registerForSuppServiceNotification(Handler h, int what, Object obj) {
        this.mSsnRegistrants.addUnique(h, what, obj);
        if (this.mCachedSsn != null) {
            this.mSsnRegistrants.notifyRegistrants(this.mCachedSsn);
            this.mCachedSsn = null;
        }
    }

    public void unregisterForSuppServiceNotification(Handler h) {
        this.mSsnRegistrants.remove(h);
        this.mCachedSsn = null;
    }

    private boolean handleUdubIncallSupplementaryService(String dialString) {
        if (dialString.length() > 1) {
            return false;
        }
        if (getRingingCall().getState() != Call.State.IDLE || getBackgroundCall().getState() != Call.State.IDLE) {
            Rlog.d(LOG_TAG, "MmiCode 0: hangupWaitingOrBackground");
            this.mCT.hangupWaitingOrBackground();
        }
        return DBG;
    }

    public void queryPhbStorageInfo(int type, Message response) {
        IccFileHandler fh = getIccFileHandler();
        if (!CsimPhbUtil.hasModemPhbEnhanceCapability(fh)) {
            CsimPhbUtil.getPhbRecordInfo(response);
        } else {
            this.mMtkCi.queryPhbStorageInfo(type, response);
        }
    }

    public void registerForNetworkInfo(Handler h, int what, Object obj) {
        this.mMtkCi.registerForNetworkInfo(h, what, obj);
    }

    public void unregisterForNetworkInfo(Handler h) {
        this.mMtkCi.unregisterForNetworkInfo(h);
    }

    public void setRxTestConfig(int AntType, Message result) {
        Rlog.d(LOG_TAG, "set Rx Test Config");
        this.mMtkCi.setRxTestConfig(AntType, result);
    }

    public void getRxTestResult(Message result) {
        Rlog.d(LOG_TAG, "get Rx Test Result");
        this.mMtkCi.getRxTestResult(result);
    }

    public void getPolCapability(Message onComplete) {
        this.mMtkCi.getPOLCapability(onComplete);
    }

    public void getPol(Message onComplete) {
        this.mMtkCi.getCurrentPOLList(onComplete);
    }

    public void setPolEntry(NetworkInfoWithAcT networkWithAct, Message onComplete) {
        this.mMtkCi.setPOLEntry(networkWithAct.getPriority(), networkWithAct.getOperatorNumeric(), networkWithAct.getAccessTechnology(), onComplete);
    }

    public List<? extends MmiCode> getPendingMmiCodes() {
        Rlog.d(LOG_TAG, "getPendingMmiCodes");
        dumpPendingMmi();
        ImsPhone imsPhone = this.mImsPhone;
        ArrayList<MmiCode> imsphonePendingMMIs = new ArrayList<>();
        if (imsPhone != null && imsPhone.getServiceState().getState() == 0) {
            List<ImsPhoneMmiCode> imsMMIs = imsPhone.getPendingMmiCodes();
            for (ImsPhoneMmiCode mmi : imsMMIs) {
                imsphonePendingMMIs.add(mmi);
            }
        }
        ArrayList<MmiCode> allPendingMMIs = new ArrayList<>(this.mPendingMMIs);
        allPendingMMIs.addAll(imsphonePendingMMIs);
        Rlog.d(LOG_TAG, "allPendingMMIs.size() = " + allPendingMMIs.size());
        int s = allPendingMMIs.size();
        for (int i = 0; i < s; i++) {
            Rlog.d(LOG_TAG, "dump allPendingMMIs: " + allPendingMMIs.get(i));
        }
        return allPendingMMIs;
    }

    public boolean handlePinMmi(String dialString) {
        MtkGsmMmiCode mtkGsmMmiCodeNewFromDialString;
        if (isPhoneTypeGsm()) {
            mtkGsmMmiCodeNewFromDialString = MtkGsmMmiCode.newFromDialString(dialString, this, (UiccCardApplication) this.mUiccApplication.get(), null);
        } else {
            mtkGsmMmiCodeNewFromDialString = CdmaMmiCode.newFromDialString(dialString, this, (UiccCardApplication) this.mUiccApplication.get());
        }
        if (mtkGsmMmiCodeNewFromDialString != null && mtkGsmMmiCodeNewFromDialString.isPinPukCommand()) {
            this.mPendingMMIs.add(mtkGsmMmiCodeNewFromDialString);
            Rlog.d(LOG_TAG, "handlePinMmi: " + dialString + ", mmi=" + mtkGsmMmiCodeNewFromDialString);
            dumpPendingMmi();
            this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, mtkGsmMmiCodeNewFromDialString, (Throwable) null));
            try {
                mtkGsmMmiCodeNewFromDialString.processCode();
                return DBG;
            } catch (CallStateException e) {
                return DBG;
            }
        }
        loge("Mmi is null or unrecognized!");
        return false;
    }

    protected boolean isImsUtEnabledOverCdma() {
        if (isGsmSsPrefer()) {
            return DBG;
        }
        if (isPhoneTypeCdmaLte() && this.mImsPhone != null && this.mImsPhone.isUtEnabled()) {
            return DBG;
        }
        return false;
    }

    public void getCallForwardingOption(int commandInterfaceCFReason, Message onComplete) {
        getCallForwardingOptionForServiceClass(commandInterfaceCFReason, 1, onComplete);
    }

    public void getCallForwardingOptionForServiceClass(int commandInterfaceCFReason, int serviceClass, Message onComplete) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, getCallForwardingOptionForServiceClass");
            ssQueueHelper.getCallForwardingOptionForServiceClass(commandInterfaceCFReason, serviceClass, onComplete, getPhoneId());
        } else {
            getCallForwardingOptionInternal(commandInterfaceCFReason, serviceClass, onComplete);
        }
    }

    public void getCallForwardingOption(int commandInterfaceCFReason, int serviceClass, Message onComplete) {
        getCallForwardingOptionForServiceClass(commandInterfaceCFReason, serviceClass, onComplete);
    }

    public boolean useSsOverImsExt(Message onComplete) {
        return useSsOverImsExt(onComplete, false);
    }

    public boolean useSsOverImsExt(Message onComplete, boolean isQueryCfu) {
        boolean ret = false;
        boolean isUtEnabled = isUtEnabled();
        boolean isGsmPhone = isPhoneTypeGsm();
        boolean isTryImsForCdma = isImsUtEnabledOverCdma();
        boolean isCsRetry = isCsRetry(onComplete);
        boolean isCdma4g = false;
        boolean isCdmaCard = isCardSupportCdma(getPhoneId());
        if ((isUtEnabled || isGsmPhone || isTryImsForCdma) && !isCsRetry) {
            MtkIccCardConstants.CardType mCdmaCardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(getPhoneId());
            ret = DBG;
            if (RatConfiguration.isC2kSupported() && isCdmaCard && mCdmaCardType != null && !(isCdma4g = mCdmaCardType.is4GCard())) {
                ret = false;
            }
        }
        if (RatConfiguration.isC2kSupported() && isCdmaCard) {
            if (isQueryCfu) {
                this.mSsOverCdma = isCsRetry;
                Rlog.d(LOG_TAG, "useSsOverImsExt, qeuryCfu, mSsOverCdma = " + this.mSsOverCdma);
            } else if (this.mSsOverCdma) {
                ret = false;
            }
        } else {
            this.mSsOverCdma = false;
        }
        Rlog.d(LOG_TAG, "useSsOverImsExt, ret=" + ret + " (isUtEnabled:" + isUtEnabled() + ", isGsmPhone:" + isGsmPhone + ", isTryImsForCdma:" + isTryImsForCdma + ", isCsRetry:" + isCsRetry + ", isCdmaCard:" + isCdmaCard + ", isCdma4g:" + isCdma4g + ", mSsOverCdma:" + this.mSsOverCdma + ")");
        return ret;
    }

    private boolean isCardSupportCdma(int slotId) {
        String[] type = MtkTelephonyManagerEx.getDefault().getSupportCardType(slotId);
        if (type == null) {
            return false;
        }
        for (int i = 0; i < type.length; i++) {
            if ("RUIM".equals(type[i]) || "CSIM".equals(type[i])) {
                return DBG;
            }
        }
        return false;
    }

    public boolean isImsRegisteredOnly() {
        if (this.mImsPhone != null && this.mImsPhone.getServiceState().getState() == 0) {
            return DBG;
        }
        Rlog.d(LOG_TAG, "isImsRegisteredOnly, false");
        return false;
    }

    public void getCallForwardingOptionInternal(int commandInterfaceCFReason, int serviceClass, Message onComplete) {
        Message resp;
        if (useSsOverImsExt(onComplete, commandInterfaceCFReason == 0 ? DBG : false)) {
            if (isCsRetry(onComplete)) {
                getCallForwardingOptionCdmaCs(onComplete, "UT error.");
                return;
            }
            MtkImsPhone mtkImsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "getCallForwardingOptionForServiceClass enter, CFReason:" + commandInterfaceCFReason + ", serviceClass:" + serviceClass);
            if (mtkImsPhone != null && (mtkImsPhone.getServiceState().getState() == 0 || mtkImsPhone.isUtEnabled() || isImsUtEnabledOverCdma())) {
                mtkImsPhone.getCallForwardingOptionForServiceClass(commandInterfaceCFReason, serviceClass, onComplete);
                return;
            }
            if (isValidCommandInterfaceCFReason(commandInterfaceCFReason)) {
                logd("requesting call forwarding query.");
                if (commandInterfaceCFReason == 0) {
                    resp = obtainMessage(13, onComplete);
                } else {
                    resp = onComplete;
                }
                this.mCi.queryCallForwardStatus(commandInterfaceCFReason, serviceClass, (String) null, resp);
                return;
            }
            return;
        }
        getCallForwardingOptionCdmaCs(onComplete, "CDMA CS directly.");
    }

    private void getCallForwardingOptionCdmaCs(Message onComplete, String additional) {
        if (!this.mSsOverCdmaSupported) {
            AsyncResult.forMessage(onComplete, (Object) null, new CommandException(CommandException.Error.INVALID_STATE, "Call Forwarding over CDMA unavailable"));
        } else {
            loge("getCallForwardingOption: not possible in CDMA, just return empty result, case:" + additional);
            AsyncResult.forMessage(onComplete, makeEmptyCallForward(), (Throwable) null);
        }
        onComplete.sendToTarget();
    }

    public void setCallForwardingOption(int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, int timerSeconds, Message onComplete) {
        setCallForwardingOptionForServiceClass(commandInterfaceCFAction, commandInterfaceCFReason, dialingNumber, timerSeconds, 1, onComplete);
    }

    public void setCallForwardingOptionForServiceClass(int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, int timerSeconds, int serviceClass, Message onComplete) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, setCallForwardingOptionForServiceClass");
            ssQueueHelper.setCallForwardingOptionForServiceClass(commandInterfaceCFAction, commandInterfaceCFReason, dialingNumber, timerSeconds, serviceClass, onComplete, getPhoneId());
        } else {
            setCallForwardingOptionInternal(commandInterfaceCFAction, commandInterfaceCFReason, dialingNumber, timerSeconds, serviceClass, onComplete);
        }
    }

    public void setCallForwardingOption(int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, int serviceClass, int timerSeconds, Message onComplete) {
        setCallForwardingOptionForServiceClass(commandInterfaceCFAction, commandInterfaceCFReason, dialingNumber, timerSeconds, serviceClass, onComplete);
    }

    public void setCallForwardingOptionInternal(int i, int i2, String str, int i3, int i4, Message message) {
        Message messageObtainMessage;
        if (useSsOverImsExt(message)) {
            if (isCsRetry(message)) {
                setCallForwardOptionCdmaCs(i, i2, str, message, "UE error.");
                return;
            }
            MtkImsPhone mtkImsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "setCallForwardingOptionForServiceClass enter, CFAction:" + i + ", CFReason:" + i2 + ", dialingNumber:" + pii(str) + ", timerSeconds:" + i3 + ", serviceClass:" + i4);
            if (mtkImsPhone != null && (mtkImsPhone.getServiceState().getState() == 0 || mtkImsPhone.isUtEnabled() || isImsUtEnabledOverCdma())) {
                mtkImsPhone.setCallForwardingOption(i, i2, str, i4, i3, message);
                return;
            }
            if (isValidCommandInterfaceCFAction(i) && isValidCommandInterfaceCFReason(i2)) {
                if (i2 == 0) {
                    messageObtainMessage = obtainMessage(12, isCfEnable(i) ? 1 : 0, 0, new Cfu(str, message, i4));
                } else {
                    messageObtainMessage = message;
                }
                this.mCi.setCallForward(i, i2, i4, str, i3, messageObtainMessage);
                return;
            }
            return;
        }
        setCallForwardOptionCdmaCs(i, i2, str, message, "CDMA CS directly.");
    }

    private void setCallForwardOptionCdmaCs(int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, Message onComplete, String additional) {
        if (RatConfiguration.isC2kSupported() && this.mSsOverCdmaSupported) {
            String formatNumber = GsmCdmaConnection.formatDialString(dialingNumber);
            String cfNumber = MtkCdmaMmiCode.getCallForwardingPrefixAndNumberWithMccMnc(commandInterfaceCFAction, commandInterfaceCFReason, formatNumber, getOperatorNumeric());
            Rlog.i(LOG_TAG, "setCallForwardOptionCdmaCs: dial for set call forwarding, case:" + additional);
            PhoneAccountHandle phoneAccountHandle = subscriptionIdToPhoneAccountHandle(getSubId());
            Bundle extras = new Bundle();
            extras.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
            TelecomManager telecomManager = TelecomManager.from(this.mContext);
            telecomManager.placeCall(Uri.fromParts("tel", cfNumber, null), extras);
            AsyncResult.forMessage(onComplete, 255, (Throwable) null);
        } else {
            loge("setCallForwardingOption: SS over CDMA not supported, can not complete");
            AsyncResult.forMessage(onComplete, 255, (Throwable) null);
        }
        onComplete.sendToTarget();
    }

    private static class CfuEx {
        final Message mOnComplete;
        final String mSetCfNumber;
        final long[] mSetTimeSlot;

        CfuEx(String cfNumber, long[] cfTimeSlot, Message onComplete) {
            this.mSetCfNumber = cfNumber;
            this.mSetTimeSlot = cfTimeSlot;
            this.mOnComplete = onComplete;
        }
    }

    public void saveTimeSlot(long[] timeSlot) {
        String timeSlotKey = CFU_TIME_SLOT + this.mPhoneId;
        String timeSlotString = "";
        if (timeSlot != null && timeSlot.length == 2) {
            timeSlotString = Long.toString(timeSlot[0]) + "," + Long.toString(timeSlot[1]);
        }
        SystemProperties.set(timeSlotKey, timeSlotString);
        Rlog.d(LOG_TAG, "timeSlotString = " + timeSlotString);
    }

    public long[] getTimeSlot() {
        String timeSlotKey = CFU_TIME_SLOT + this.mPhoneId;
        String timeSlotString = SystemProperties.get(timeSlotKey, "");
        long[] timeSlot = null;
        if (timeSlotString != null && !timeSlotString.equals("")) {
            String[] timeArray = timeSlotString.split(",");
            if (timeArray.length == 2) {
                timeSlot = new long[2];
                for (int i = 0; i < 2; i++) {
                    timeSlot[i] = Long.parseLong(timeArray[i]);
                    Calendar calenar = Calendar.getInstance(TimeZone.getDefault());
                    calenar.setTimeInMillis(timeSlot[i]);
                    int hour = calenar.get(11);
                    int min = calenar.get(12);
                    Calendar calenar2 = Calendar.getInstance(TimeZone.getDefault());
                    calenar2.set(11, hour);
                    calenar2.set(12, min);
                    timeSlot[i] = calenar2.getTimeInMillis();
                }
            }
        }
        Rlog.d(LOG_TAG, "timeSlot = " + Arrays.toString(timeSlot));
        return timeSlot;
    }

    public void getCallForwardInTimeSlot(int commandInterfaceCFReason, Message onComplete) {
        if (isPhoneTypeGsm()) {
            ImsPhone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "getCallForwardInTimeSlot enter, CFReason:" + commandInterfaceCFReason);
            if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled())) {
                ((MtkImsPhone) imsPhone).getCallForwardInTimeSlot(commandInterfaceCFReason, onComplete);
                return;
            } else {
                if (commandInterfaceCFReason == 0) {
                    Rlog.d(LOG_TAG, "requesting call forwarding in time slot query.");
                    Message resp = obtainMessage(109, onComplete);
                    this.mMtkCi.queryCallForwardInTimeSlotStatus(commandInterfaceCFReason, 0, resp);
                    return;
                }
                return;
            }
        }
        loge("method getCallForwardInTimeSlot is NOT supported in CDMA!");
        sendErrorResponse(onComplete, CommandException.Error.GENERIC_FAILURE);
    }

    public void setCallForwardInTimeSlot(int i, int i2, String str, int i3, long[] jArr, Message message) {
        if (!isPhoneTypeGsm()) {
            loge("method setCallForwardInTimeSlot is NOT supported in CDMA!");
            sendErrorResponse(message, CommandException.Error.GENERIC_FAILURE);
            return;
        }
        ImsPhone imsPhone = this.mImsPhone;
        Rlog.d(LOG_TAG, "setCallForwardInTimeSlot enter, CFReason:" + i2 + ", CFAction:" + i + ", dialingNumber:" + str + ", timerSeconds:" + i3);
        if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled())) {
            ((MtkImsPhone) imsPhone).setCallForwardInTimeSlot(i, i2, str, i3, jArr, message);
        } else if (isValidCommandInterfaceCFAction(i) && i2 == 0) {
            this.mMtkCi.setCallForwardInTimeSlot(i, i2, 1, str, i3, jArr, obtainMessage(110, isCfEnable(i) ? 1 : 0, 0, new CfuEx(str, jArr, message)));
        }
    }

    public void disableAllCallForwardingOptions() {
        String cfNumber = MtkCdmaMmiCode.getDisableAllCallForwardingOptionsPrefix(getOperatorNumeric());
        Rlog.i(LOG_TAG, "disableAllCallForwardingOptions: prefixWithNumber= " + cfNumber);
        PhoneAccountHandle phoneAccountHandle = subscriptionIdToPhoneAccountHandle(getSubId());
        Bundle extras = new Bundle();
        extras.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
        TelecomManager telecomManager = TelecomManager.from(this.mContext);
        telecomManager.placeCall(Uri.parse("tel:" + cfNumber), extras);
    }

    private void handleCfuInTimeSlotQueryResult(MtkCallForwardInfo[] infos) {
        IccRecords r = (IccRecords) this.mIccRecords.get();
        if (r != null) {
            if (infos == null || infos.length == 0) {
                setVoiceCallForwardingFlag(1, false, null);
                return;
            }
            int s = infos.length;
            for (int i = 0; i < s; i++) {
                if ((infos[i].serviceClass & 1) != 0) {
                    setVoiceCallForwardingFlag(1, infos[i].status == 1, infos[i].number);
                    saveTimeSlot(infos[i].timeSlot);
                    return;
                }
            }
        }
    }

    public void getOutgoingCallerIdDisplay(Message onComplete) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, getOutgoingCallerIdDisplay");
            ssQueueHelper.getOutgoingCallerIdDisplay(onComplete, getPhoneId());
        } else {
            getOutgoingCallerIdDisplayInternal(onComplete);
        }
    }

    public void getOutgoingCallerIdDisplayInternal(Message onComplete) {
        if (isPhoneTypeGsm() || isGsmSsPrefer()) {
            Phone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "getOutgoingCallerIdDisplay enter");
            Message resp = obtainMessage(EVENT_GET_CLIR_COMPLETE, onComplete);
            if (imsPhone != null && imsPhone.getServiceState().getState() == 0) {
                imsPhone.getOutgoingCallerIdDisplay(resp);
                return;
            } else {
                this.mCi.getCLIR(resp);
                return;
            }
        }
        loge("getOutgoingCallerIdDisplay: not possible in CDMA");
        sendErrorResponse(onComplete, CommandException.Error.GENERIC_FAILURE);
    }

    public void setOutgoingCallerIdDisplay(int commandInterfaceCLIRMode, Message onComplete) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, setOutgoingCallerIdDisplay");
            ssQueueHelper.setOutgoingCallerIdDisplay(commandInterfaceCLIRMode, onComplete, getPhoneId());
        } else {
            setOutgoingCallerIdDisplayInternal(commandInterfaceCLIRMode, onComplete);
        }
    }

    public void setOutgoingCallerIdDisplayInternal(int commandInterfaceCLIRMode, Message onComplete) {
        if (isPhoneTypeGsm() || isGsmSsPrefer()) {
            Phone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "setOutgoingCallerIdDisplay enter, CLIRmode:" + commandInterfaceCLIRMode);
            Message resp = obtainMessage(18, commandInterfaceCLIRMode, 0, onComplete);
            if (imsPhone != null && imsPhone.getServiceState().getState() == 0) {
                imsPhone.setOutgoingCallerIdDisplay(commandInterfaceCLIRMode, resp);
                return;
            } else {
                this.mCi.setCLIR(commandInterfaceCLIRMode, resp);
                return;
            }
        }
        loge("setOutgoingCallerIdDisplay: not possible in CDMA");
        sendErrorResponse(onComplete, CommandException.Error.GENERIC_FAILURE);
    }

    public void getCallWaiting(Message onComplete) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, getCallWaiting");
            ssQueueHelper.getCallWaiting(onComplete, getPhoneId());
        } else {
            getCallWaitingInternal(onComplete);
        }
    }

    public void getCallWaitingInternal(Message onComplete) {
        if (useSsOverImsExt(onComplete)) {
            if (isOp(MtkOperatorUtils.OPID.OP09) && !isImsRegisteredOnly() && isCardSupportCdma(getPhoneId())) {
                getCallWaitingCdmaCs(onComplete, "IMS unregistered.");
                return;
            }
            if (isCsRetry(onComplete)) {
                getCallWaitingCdmaCs(onComplete, "UT error.");
                return;
            }
            Phone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "getCallWaiting enter");
            if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled() || isImsUtEnabledOverCdma())) {
                imsPhone.getCallWaiting(onComplete);
                return;
            } else {
                this.mCi.queryCallWaiting(0, onComplete);
                return;
            }
        }
        getCallWaitingCdmaCs(onComplete, "CDMA CS directly");
    }

    private void getCallWaitingCdmaCs(Message onComplete, String additional) {
        Rlog.d(LOG_TAG, "getCallWaitingCdmaCs, case:" + additional);
        if (!this.mSsOverCdmaSupported || !RatConfiguration.isC2kSupported()) {
            AsyncResult.forMessage(onComplete, (Object) null, new CommandException(CommandException.Error.INVALID_STATE, "Call Waiting over CDMA unavailable"));
        } else {
            int[] arr = {255, 0};
            AsyncResult.forMessage(onComplete, arr, (Throwable) null);
        }
        onComplete.sendToTarget();
    }

    public void setCallWaiting(boolean enable, Message onComplete) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper, setCallWaiting");
            ssQueueHelper.setCallWaiting(enable, onComplete, getPhoneId());
        } else {
            setCallWaitingInternal(enable, onComplete);
        }
    }

    public void setCallWaitingInternal(boolean enable, Message onComplete) {
        if (this.mCallWaitingController.setCallWaiting(enable, 1, onComplete)) {
            Rlog.d(LOG_TAG, "setCallWaitingInternal - mCallWaitingController");
            return;
        }
        if (useSsOverImsExt(onComplete)) {
            if (RatConfiguration.isC2kSupported()) {
                if (isOp(MtkOperatorUtils.OPID.OP09) && !isImsRegisteredOnly() && isCardSupportCdma(getPhoneId())) {
                    setCallWaitingCdmaCs(enable, onComplete, "IMS unregistered.");
                    return;
                } else if (isCsRetry(onComplete)) {
                    setCallWaitingCdmaCs(enable, onComplete, "UE error.");
                    return;
                }
            }
            Phone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "setCallWaiting enter, enable:" + enable);
            if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled() || isImsUtEnabledOverCdma())) {
                imsPhone.setCallWaiting(enable, onComplete);
                return;
            }
            int serviceClass = 1;
            CarrierConfigManager configManager = (CarrierConfigManager) getContext().getSystemService("carrier_config");
            PersistableBundle b = configManager.getConfigForSubId(getSubId());
            if (b != null) {
                serviceClass = b.getInt("call_waiting_service_class_int", 1);
            }
            Rlog.d(LOG_TAG, "setCallWaiting serviceClass = " + serviceClass);
            this.mCi.setCallWaiting(enable, serviceClass, onComplete);
            return;
        }
        setCallWaitingCdmaCs(enable, onComplete, "CDMA CS directly.");
    }

    private void setCallWaitingCdmaCs(boolean enable, Message onComplete, String additional) {
        if (this.mSsOverCdmaSupported) {
            String cwPrefix = CdmaMmiCode.getCallWaitingPrefix(enable);
            Rlog.i(LOG_TAG, "setCallWaiting in CDMA : dial for set call waiting prefix= " + cwPrefix + ", case:" + additional);
            PhoneAccountHandle phoneAccountHandle = subscriptionIdToPhoneAccountHandle(getSubId());
            Bundle extras = new Bundle();
            extras.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);
            TelecomManager telecomManager = TelecomManager.from(this.mContext);
            telecomManager.placeCall(Uri.fromParts("tel", cwPrefix, null), extras);
            AsyncResult.forMessage(onComplete, 255, (Throwable) null);
        } else {
            loge("setCallWaiting: SS over CDMA not supported, can not complete");
            AsyncResult.forMessage(onComplete, 255, (Throwable) null);
        }
        onComplete.sendToTarget();
    }

    public void getCallBarring(String facility, String password, Message onComplete, int serviceClass) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, getCallBarringInternal");
            ssQueueHelper.getCallBarring(facility, password, serviceClass, onComplete, getPhoneId());
        } else {
            getCallBarringInternal(facility, password, onComplete, serviceClass);
        }
    }

    public void getCallBarring(String facility, String password, Message onComplete) {
        getCallBarring(facility, password, onComplete, 1);
    }

    public void getCallBarringInternal(String facility, String password, Message onComplete, int serviceClass) {
        if (isPhoneTypeGsm() || isGsmSsPrefer()) {
            ImsPhone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "getCallBarringInternal enter, facility:" + facility + ", serviceClass:" + serviceClass + ", password:" + password);
            Message resp = obtainMessage(EVENT_GET_CALL_BARRING_COMPLETE, onComplete);
            if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled())) {
                imsPhone.getCallBarring(facility, password, resp, serviceClass);
                return;
            } else {
                this.mCi.queryFacilityLock(facility, password, serviceClass, resp);
                return;
            }
        }
        loge("method getFacilityLock is NOT supported in CDMA!");
        sendErrorResponse(onComplete, CommandException.Error.GENERIC_FAILURE);
    }

    public void setCallBarring(String facility, boolean lockState, String password, Message onComplete, int serviceClass) {
        MtkSuppServQueueHelper ssQueueHelper = MtkSuppServManager.getSuppServQueueHelper();
        if (ssQueueHelper != null) {
            Rlog.d(LOG_TAG, "ssQueueHelper enter, setCallBarring");
            ssQueueHelper.setCallBarring(facility, lockState, password, serviceClass, onComplete, getPhoneId());
        } else {
            setCallBarringInternal(facility, lockState, password, onComplete, serviceClass);
        }
    }

    public void setCallBarring(String facility, boolean lockState, String password, Message onComplete) {
        setCallBarring(facility, lockState, password, onComplete, 1);
    }

    public void setCallBarringInternal(String facility, boolean lockState, String password, Message onComplete, int serviceClass) {
        if (isPhoneTypeGsm() || isGsmSsPrefer()) {
            ImsPhone imsPhone = this.mImsPhone;
            Rlog.d(LOG_TAG, "setCallBarring enter, facility:" + facility + ", serviceClass:" + serviceClass + ", password:" + password + ", lockState:" + lockState);
            Message resp = obtainMessage(EVENT_SET_CALL_BARRING_COMPLETE, onComplete);
            if (imsPhone != null && (imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled())) {
                imsPhone.setCallBarring(facility, lockState, password, resp, serviceClass);
                return;
            } else {
                this.mCi.setFacilityLock(facility, lockState, password, serviceClass, resp);
                return;
            }
        }
        loge("method setFacilityLock is NOT supported in CDMA!");
        sendErrorResponse(onComplete, CommandException.Error.GENERIC_FAILURE);
    }

    public void dumpPendingMmi() {
        int size = this.mPendingMMIs.size();
        if (size == 0) {
            Rlog.d(LOG_TAG, "dumpPendingMmi: none");
            return;
        }
        for (int i = 0; i < size; i++) {
            Rlog.d(LOG_TAG, "dumpPendingMmi: " + this.mPendingMMIs.get(i));
        }
    }

    public void setServiceClass(int serviceClass) {
        Rlog.d(LOG_TAG, "setServiceClass: " + serviceClass);
        SystemProperties.set(SS_SERVICE_CLASS_PROP, String.valueOf(serviceClass));
    }

    private boolean isUsimCard() {
        if (isPhoneTypeGsm() && !isOp(MtkOperatorUtils.OPID.OP09)) {
            boolean r = false;
            String iccCardType = PhoneFactory.getPhone(getPhoneId()).getIccCard().getIccCardType();
            if (iccCardType != null && iccCardType.equals("USIM")) {
                r = DBG;
            }
            Rlog.d(LOG_TAG, "isUsimCard: " + r + ", " + iccCardType);
            return r;
        }
        String[] values = null;
        int subId = MtkSubscriptionManager.getSubIdUsingPhoneId(getPhoneId());
        int slotId = SubscriptionManager.getSlotIndex(subId);
        if (slotId >= 0) {
            String[] strArr = PROPERTY_RIL_FULL_UICC_TYPE;
            if (slotId < strArr.length) {
                String prop = SystemProperties.get(strArr[slotId], "");
                if (!prop.equals("") && prop.length() > 0) {
                    values = prop.split(",");
                }
                Rlog.d(LOG_TAG, "isUsimCard PhoneId = " + getPhoneId() + " cardType = " + Arrays.toString(values));
                if (values == null) {
                    return false;
                }
                for (String s : values) {
                    if (s.equals("USIM")) {
                        return DBG;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean isOp(MtkOperatorUtils.OPID id) {
        return MtkOperatorUtils.isOperator(getOperatorNumeric(), id);
    }

    private boolean isIccCardMncMccAvailable(int phoneId) {
        UiccController uiccCtl = UiccController.getInstance();
        IccRecords iccRecords = uiccCtl.getIccRecords(phoneId, 1);
        if (iccRecords != null) {
            String mccMnc = iccRecords.getOperatorNumeric();
            Rlog.d(LOG_TAG, "isIccCardMncMccAvailable(): mccMnc is " + mccMnc);
            if (mccMnc != null) {
                return DBG;
            }
            return false;
        }
        Rlog.d(LOG_TAG, "isIccCardMncMccAvailable(): false");
        return false;
    }

    public boolean isNotSupportUtToCS() {
        boolean r = false;
        if (((SystemProperties.getInt("persist.vendor.mtk_ct_volte_support", 0) != 0 && isOp(MtkOperatorUtils.OPID.OP09) && isUsimCard()) || isOp(MtkOperatorUtils.OPID.OP117)) && !getServiceState().getRoaming()) {
            r = DBG;
        }
        Rlog.d(LOG_TAG, "isNotSupportUtToCS: " + r);
        return r;
    }

    private boolean isUtError(CommandException.Error error) {
        if (error == CommandException.Error.OEM_ERROR_25 || error == CommandException.Error.OEM_ERROR_3 || error == CommandException.Error.OEM_ERROR_4 || error == CommandException.Error.OEM_ERROR_6 || error == CommandException.Error.OEM_ERROR_24 || error == CommandException.Error.OEM_ERROR_23 || error == CommandException.Error.OEM_ERROR_22) {
            return DBG;
        }
        return false;
    }

    private boolean isUtErrorHasMsg(CommandException.Error error) {
        if (error == CommandException.Error.OEM_ERROR_25 || error == CommandException.Error.OEM_ERROR_6 || error == CommandException.Error.OEM_ERROR_24 || error == CommandException.Error.OEM_ERROR_23 || error == CommandException.Error.OEM_ERROR_22) {
            return DBG;
        }
        return false;
    }

    public boolean isSupportCFUTimeSlot() {
        boolean r = false;
        if (isOp(MtkOperatorUtils.OPID.OP01)) {
            r = DBG;
        }
        Rlog.d(LOG_TAG, "isSupportCFUTimeSlot: " + r);
        return r;
    }

    public void updateVoiceMail() {
        if (isPhoneTypeGsm()) {
            int countVoiceMessages = 0;
            IccRecords r = (IccRecords) this.mIccRecords.get();
            if (r != null) {
                countVoiceMessages = r.getVoiceMessageCount();
            }
            if (r == null || countVoiceMessages == -2) {
                countVoiceMessages = getStoredVoiceMessageCount();
            }
            Rlog.d(LOG_TAG, "updateVoiceMail countVoiceMessages = " + countVoiceMessages + " subId " + getSubId());
            setVoiceMessageCount(countVoiceMessages);
            return;
        }
        setVoiceMessageCount(getStoredVoiceMessageCount());
    }

    void sendErrorResponse(Message onComplete, CommandException.Error error) {
        Rlog.d(LOG_TAG, "sendErrorResponse" + error);
        if (onComplete != null) {
            AsyncResult.forMessage(onComplete, (Object) null, new CommandException(error));
            onComplete.sendToTarget();
        }
    }

    public boolean isIMSRegistered() {
        MtkSuppServHelper ssHelper = MtkSuppServManager.getSuppServHelper(getPhoneId());
        if (ssHelper != null) {
            return ssHelper.getIMSRegistered();
        }
        return false;
    }

    public int getCdmaSubscriptionActStatus() {
        if (this.mCdmaSSM != null) {
            return ((MtkCdmaSubscriptionSourceManager) this.mCdmaSSM).getActStatus();
        }
        return 0;
    }

    public void setRoamingEnable(int[] config, Message response) {
        Rlog.d(LOG_TAG, "set roaming enable");
        if (config == null || config.length != 6) {
            Rlog.d(LOG_TAG, "set roaming enable, invalid paramter");
            sendErrorResponse(response, CommandException.Error.INVALID_ARGUMENTS);
        } else {
            config[0] = this.mPhoneId;
            this.mMtkCi.setRoamingEnable(config, response);
        }
    }

    public void getRoamingEnable(Message response) {
        Rlog.d(LOG_TAG, "get roaming enable");
        this.mMtkCi.getRoamingEnable(this.mPhoneId, response);
    }

    public boolean isGsmSsPrefer() {
        if ((SystemProperties.getInt("persist.vendor.mtk_ct_volte_support", 0) == 0 || !isOp(MtkOperatorUtils.OPID.OP09)) && !isOp(MtkOperatorUtils.OPID.OP117)) {
            return false;
        }
        return DBG;
    }

    public String getOperatorNumeric() {
        if (!isPhoneTypeGsm()) {
            this.mCdmaSubscriptionSource = this.mCdmaSSM.getCdmaSubscriptionSource();
        }
        return super.getOperatorNumeric();
    }

    public Message getCFCallbackMessage() {
        return obtainMessage(13);
    }

    public Message getCFTimeSlotCallbackMessage() {
        return obtainMessage(109);
    }

    public boolean isImsUseEnabled() {
        ImsManager imsManager = ImsManager.getInstance(this.mContext, this.mPhoneId);
        boolean isEnhanced4gLteModeSettingEnabledByUser = imsManager.isEnhanced4gLteModeSettingEnabledByUser();
        boolean isWfcEnabledByUser = DBG;
        boolean isNonTtyOrTtyOnVolteEnabled = DBG;
        if (!isEnhanced4gLteModeSettingEnabledByUser) {
            isWfcEnabledByUser = imsManager.isWfcEnabledByUser();
            isNonTtyOrTtyOnVolteEnabled = imsManager.isNonTtyOrTtyOnVolteEnabled();
        }
        boolean imsUseEnabled = (isEnhanced4gLteModeSettingEnabledByUser || (isWfcEnabledByUser && isNonTtyOrTtyOnVolteEnabled)) ? DBG : false;
        Rlog.d(LOG_TAG, "isImsUseEnabled() VolteEnableByUser: " + isEnhanced4gLteModeSettingEnabledByUser + ", WfcEnableByUser: " + isWfcEnabledByUser + ", isNonTtyOrTtyOnVolteEnabled: " + isNonTtyOrTtyOnVolteEnabled);
        return imsUseEnabled;
    }

    public void cleanCallForwardingIndicatorFromSharedPref() {
        setCallForwardingIndicatorInSharedPref(false);
    }

    public boolean shouldProcessSelfActivation() {
        int selfActivateState = getSelfActivationInstance().getSelfActivateState();
        Rlog.d(LOG_TAG, "shouldProcessSelfActivation() state: " + selfActivateState);
        if (selfActivateState == 2) {
            return DBG;
        }
        return false;
    }

    public boolean useImsForPCOChanged() {
        int pcoState = getSelfActivationInstance().getPCO520State();
        Rlog.d(LOG_TAG, "pcoState() state: " + pcoState);
        if (pcoState == 1) {
            return DBG;
        }
        return false;
    }

    protected boolean needResetPhbIntMgr() {
        return false;
    }

    public String getFullIccSerialNumber() {
        String iccId = super.getFullIccSerialNumber();
        if (iccId != null) {
            return iccId;
        }
        if (!isPhoneTypeGsm() && this.mUiccController != null) {
            IccRecords r = this.mUiccController.getIccRecords(this.mPhoneId, 1);
            iccId = r != null ? r.getFullIccId() : null;
            if (iccId != null) {
                return iccId;
            }
        }
        long identity = Binder.clearCallingIdentity();
        try {
            int subId = getSubId();
            SubscriptionInfo subInfo = SubscriptionManager.from(getContext()).getActiveSubscriptionInfo(subId);
            if (subInfo != null) {
                iccId = subInfo.getIccId();
            }
            return iccId;
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    protected void initRatSpecific(int precisePhoneType) {
        super.initRatSpecific(precisePhoneType);
        if (isPhoneTypeGsm()) {
            this.mIsPhoneInEcmState = getInEcmMode();
            if (this.mIsPhoneInEcmState) {
                this.mCi.exitEmergencyCallbackMode((Message) null);
            }
        }
    }

    public String getDeviceSvn() {
        if (isPhoneTypeGsm() || isPhoneTypeCdmaLte() || isPhoneTypeCdma()) {
            return this.mImeiSv;
        }
        loge("getDeviceSvn(): return 0");
        return "0";
    }

    public void invokeOemRilRequestRaw(byte[] data, Message response) {
        this.mMtkCi.invokeOemRilRequestRaw(data, response);
    }

    protected void phoneObjectUpdater(int newVoiceRadioTech) {
        this.mNewVoiceTech = newVoiceRadioTech;
        super.phoneObjectUpdater(newVoiceRadioTech);
    }

    public void setImsRegistrationState(boolean registered) {
        super.setImsRegistrationState(registered);
        if (this.mImsPhone != null && this.mImsPhone.isImsRegistered()) {
            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
            Rlog.d(LOG_TAG, "setImsRegistrationState: " + registered);
            notificationManager.cancel("wifi_calling", 1);
        }
    }

    public void setDisable2G(boolean mode, Message result) {
        Rlog.d(LOG_TAG, "setDisable2G " + mode);
        this.mMtkCi.setDisable2G(mode, result);
    }

    public void getDisable2G(Message result) {
        Rlog.d(LOG_TAG, "getDisable2G");
        this.mMtkCi.getDisable2G(result);
    }

    public void setVoiceCallForwardingFlag(int line, boolean enable, String number) {
        super.setVoiceCallForwardingFlag(line, enable, number);
        if (getPhoneType() == 2 && isGsmSsPrefer() && this.mUiccController != null) {
            IccRecords r = this.mUiccController.getIccRecords(this.mPhoneId, 1);
            if (r != null) {
                r.setVoiceCallForwardingFlag(line, enable, number);
            }
            notifyCallForwardingIndicator();
        }
    }

    public boolean getCallForwardingIndicator() {
        if (getPhoneType() == 2 && isGsmSsPrefer()) {
            IccRecords r = null;
            if (this.mUiccController != null) {
                r = this.mUiccController.getIccRecords(this.mPhoneId, 1);
            }
            int callForwardingIndicator = -1;
            if (r != null) {
                callForwardingIndicator = r.getVoiceCallForwardingFlag();
                Rlog.v(LOG_TAG, "getCallForwardingIndicator: from icc record = " + callForwardingIndicator);
            }
            if (callForwardingIndicator == -1) {
                callForwardingIndicator = getCallForwardingIndicatorFromSharedPref();
            }
            Rlog.v(LOG_TAG, "getCallForwardingIndicator: iccForwardingFlag=" + (r != null ? Integer.valueOf(r.getVoiceCallForwardingFlag()) : "null") + ", sharedPrefFlag=" + getCallForwardingIndicatorFromSharedPref());
            if (callForwardingIndicator == 1) {
                return DBG;
            }
            return false;
        }
        return super.getCallForwardingIndicator();
    }

    public void invokeOemRilRequestStrings(String[] strings, Message response) {
        this.mMtkCi.invokeOemRilRequestStrings(strings, response);
    }

    public void setSuppServProperty(String name, String value) {
        Rlog.d(LOG_TAG, "setSuppServProperty, name = " + name + ", value = " + value);
        this.mCallbackLatch = new CountDownLatch(1);
        this.mMtkCi.setSuppServProperty(name, value, obtainMessage(1004));
        if (!isCallbackDone()) {
            Rlog.e(LOG_TAG, "waitForCallback: callback is not done!");
        }
    }

    private boolean isCallbackDone() throws InterruptedException {
        boolean isDone;
        try {
            isDone = this.mCallbackLatch.await(3000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            isDone = false;
        }
        Rlog.d(LOG_TAG, "waitForCallback: isDone=" + isDone);
        return isDone;
    }

    public PhoneConstants.State getState() {
        PhoneConstants.State state = super.getState();
        if (state == PhoneConstants.State.IDLE && ((MtkGsmCdmaCallTracker) this.mCT).getHandoverConnectionSize() > 0) {
            return PhoneConstants.State.OFFHOOK;
        }
        return state;
    }

    public String getLine1PhoneNumber() {
        String msisdnNumber;
        if (isPhoneTypeGsm()) {
            String optr = SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR);
            if (optr != null && "OP20".equals(optr)) {
                MtkIccCardConstants.CardType mCdmaCardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(getPhoneId());
                boolean isCdma4g = false;
                if (mCdmaCardType != null) {
                    isCdma4g = mCdmaCardType.is4GCard();
                }
                if (isCdma4g) {
                    RuimRecords rr = UiccController.getInstance().getIccRecords(getPhoneId(), 2);
                    IccRecords r = (IccRecords) this.mIccRecords.get();
                    StringBuilder sbAppend = new StringBuilder().append("getLine1PhoneNumber, number = ");
                    if (rr != null && rr.getMdn() != null && !rr.getMdn().isEmpty()) {
                        msisdnNumber = rr.getMdn();
                    } else if (r == null) {
                        msisdnNumber = null;
                    } else {
                        msisdnNumber = r.getMsisdnNumber();
                    }
                    logd(sbAppend.append(msisdnNumber).append(", slot = ").append(getPhoneId()).toString());
                    if (rr != null && rr.getMdn() != null && !rr.getMdn().isEmpty()) {
                        return rr.getMdn();
                    }
                    if (r != null) {
                        return r.getMsisdnNumber();
                    }
                    return null;
                }
                IccRecords r2 = (IccRecords) this.mIccRecords.get();
                if (r2 != null) {
                    return r2.getMsisdnNumber();
                }
                return null;
            }
            IccRecords r3 = (IccRecords) this.mIccRecords.get();
            if (r3 != null) {
                return r3.getMsisdnNumber();
            }
            return null;
        }
        return this.mSST.getMdnNumber();
    }

    public void notifyServiceStateChanged(ServiceState ss) {
        MtkServiceStateTracker mtkSST = (MtkServiceStateTracker) this.mSST;
        if (!this.enableFakeSS && mtkSST.once_pollState_done) {
            super.notifyServiceStateChanged(ss);
        } else {
            logd("notifyServiceStateChanged, skip");
        }
    }

    public void notifyMtkFakeServiceStateChanged(ServiceState ss) {
        if (ss == null) {
            this.enableFakeSS = false;
        } else {
            this.enableFakeSS = DBG;
            this.mNotifier.notifyMtkServiceState(this, ss);
        }
    }

    public void notifyMtkServiceStateChanged(ServiceState ss) {
        this.mNotifier.notifyMtkServiceState(this, ss);
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) getDataNetworkController();
        if (mtkDnc != null) {
            mtkDnc.notifyMtkServiceStateChanged(ss);
        }
    }

    public void notifyMtkSignalStrength(SignalStrength ss) {
        this.mNotifier.notifyMtkSignalStrength(this, ss);
    }

    public String getSubscriberId() {
        String subscriberId = null;
        DataNetworkController dnc = getDataNetworkController();
        if (dnc != null) {
            subscriberId = ((MtkDataProfileManager) dnc.getDataProfileManager()).getImsi();
        }
        if (TextUtils.isEmpty(subscriberId)) {
            String subscriberId2 = super.getSubscriberId();
            return subscriberId2;
        }
        return subscriberId;
    }

    public void iwlanSetRegisterCellularQualityReport(int qualityRegister, int type, int[] values, Message result) {
        this.mMtkCi.iwlanSetRegisterCellularQualityReport(qualityRegister, type, values, result);
    }

    public void getSuggestedPlmnList(int rat, int num, int timer, Message onCompleted) {
        this.mMtkCi.getSuggestedPlmnList(rat, num, timer, onCompleted);
    }

    public void switchNrMap(boolean sw) {
        MtkServiceStateTracker mtkServiceStateTracker = this.mMtkSST;
        if (mtkServiceStateTracker == null || mtkServiceStateTracker.nm == null) {
            loge("switchNrMap mMtkSST == null && mMtkSST.nm == null");
        } else {
            this.mMtkSST.nm.swtichNrMap(sw);
        }
    }

    public void manuallySetNrMap(int i) {
        MtkServiceStateTracker mtkServiceStateTracker = this.mMtkSST;
        if (mtkServiceStateTracker == null || mtkServiceStateTracker.nm == null) {
            loge("manuallySetNrMap mMtkSST == null && mMtkSST.nm == null");
        } else {
            this.mMtkSST.nm.manuallySetNrMap(i);
        }
    }

    public boolean isNrMapEnabled() {
        MtkServiceStateTracker mtkServiceStateTracker = this.mMtkSST;
        if (mtkServiceStateTracker != null && mtkServiceStateTracker.nm != null) {
            return this.mMtkSST.nm.isNrMapEnabled();
        }
        loge("isNrMapEnabled mMtkSST == null && mMtkSST.nm == null");
        return false;
    }

    public String getNrMapStatus() {
        MtkServiceStateTracker mtkServiceStateTracker = this.mMtkSST;
        if (mtkServiceStateTracker != null && mtkServiceStateTracker.nm != null) {
            return this.mMtkSST.nm.getNrMapStatus();
        }
        loge("getNrMapStatus mMtkSST == null && mMtkSST.nm == null");
        return "n/a";
    }

    public boolean getEccRadioOnStatus() {
        return this.mEccRadioOnStatus;
    }

    public boolean isEccSelectedPhone() {
        return this.mIsEccSelectedPhone;
    }

    public void setEccRadioOnStatus(boolean isEccRadioOn, boolean isSelectedPhoneForEmergencyCall) {
        if (this.mEccRadioOnStatus != isEccRadioOn) {
            logd("ecc radio on status changed (" + (isEccRadioOn ? "t" : "f") + "," + (isSelectedPhoneForEmergencyCall ? "t" : "f") + ")");
            this.mEccRadioOnStatus = isEccRadioOn;
            if (isEccRadioOn) {
                this.mIsEccSelectedPhone = isSelectedPhoneForEmergencyCall;
            } else {
                this.mIsEccSelectedPhone = false;
            }
        }
    }

    public void resetAllPhoneEccRadioOnStatus() {
        for (MtkGsmCdmaPhone mtkGsmCdmaPhone : PhoneFactory.getPhones()) {
            if (mtkGsmCdmaPhone != null && (mtkGsmCdmaPhone instanceof MtkGsmCdmaPhone)) {
                mtkGsmCdmaPhone.setEccRadioOnStatus(false, false);
            }
        }
    }

    protected void reapplyUiccAppsEnablementIfNeeded(int retries) {
        super.reapplyUiccAppsEnablementIfNeeded(retries);
        UiccSlot slot = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        if (slot == null || slot.getCardState() != IccCardStatus.CardState.CARDSTATE_PRESENT || this.mUiccApplicationsEnabled == null) {
            if (slot == null) {
                loge("reapplyUiccAppsEnablementIfNeeded(), slot is null ,mUiccApplicationsEnabled = " + this.mUiccApplicationsEnabled);
                return;
            } else {
                loge("reapplyUiccAppsEnablementIfNeeded(), CardState=" + slot.getCardState() + " ,mUiccApplicationsEnabled = " + this.mUiccApplicationsEnabled);
                return;
            }
        }
        UiccCard uiccCard = slot.getUiccCard();
        if (uiccCard == null) {
            loge("reapplyUiccAppsEnablementIfNeeded(), uiccCard is null");
            return;
        }
        UiccPort uiccPort = uiccCard.getUiccPortForPhone(getPhoneId());
        if (uiccPort == null) {
            loge("reapplyUiccAppsEnablementIfNeeded(), uiccPort is null");
            return;
        }
        String iccId = uiccPort.getIccId();
        if (iccId == null) {
            loge("reapplyUiccAppsEnablementIfNeeded(), iccid is null");
            return;
        }
        SubscriptionInfo info = MtkSubscriptionManagerService.getInstance().getSubInfoForIccId(IccUtils.stripTrailingFs(iccId));
        boolean expectedValue = info == null ? DBG : info.areUiccApplicationsEnabled();
        logd("reapplyUiccAppsEnablementIfNeeded(), expectedValue =" + expectedValue + " ,mUiccApplicationsEnabled =" + this.mUiccApplicationsEnabled + " ,iccId=" + SubscriptionInfo.getPrintableId(iccId));
    }

    public void registerForOemIndication(Handler h, int what, Object obj) {
        this.mOemIndRegistrants.addUnique(h, what, obj);
    }

    public void unregisterForOemIndication(Handler h) {
        this.mOemIndRegistrants.remove(h);
    }

    protected void notifyImsPhoneHandoverStateChanged(Connection cn) {
        if (this.mImsPhone != null) {
            this.mImsPhone.notifyHandoverStateChanged(cn);
        }
    }

    public void getCallSubAddress(Message response) {
        this.mMtkCi.getCallSubAddress(response);
    }

    public void setCallSubAddress(boolean enable, Message response) {
        this.mMtkCi.setCallSubAddress(enable, response);
    }

    private static boolean isOP12() {
        return "OP12".equals(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, ""));
    }

    public void getEcholocateMetrics(int index, Message response) {
        Rlog.d(LOG_TAG, "getEcholocateMetrics()");
        this.mMtkCi.getEcholocateMetrics(index, response);
    }

    public void getIWlanRegistrationState(Message response) {
        this.mMtkCi.getIWlanRegistrationState(response);
    }

    public boolean isAllowCallWhenOos() {
        CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        boolean isAllowCallWhenOos = configManager.getConfigForSubId(getSubId()).getBoolean("mtk_allow_call_when_oos_bool");
        Rlog.d(LOG_TAG, "isAllowCallWhenOos-" + isAllowCallWhenOos);
        return isAllowCallWhenOos;
    }

    protected boolean onCheckIfForceHandleInCallMmiByCS() {
        return isInCSCall();
    }

    protected boolean onHandleInCallMmi0(String dialString) {
        return handleUdubIncallSupplementaryService(dialString);
    }

    protected void onCheckEmergencyCall(boolean isEmergency) {
        MtkLocalPhoneNumberUtils.setIsEmergencyNumber(isEmergency);
        if (isEmergency && isOP12()) {
            WifiManager wifiMngr = (WifiManager) this.mContext.getSystemService("wifi");
            boolean zIsScanAlwaysAvailable = wifiMngr.isScanAlwaysAvailable();
            this.mWifiIsEnabledBeforeE911 = zIsScanAlwaysAvailable;
            if (!zIsScanAlwaysAvailable) {
                Rlog.d(LOG_TAG, "Turn on wifi radio for Vzw E911 CCP test");
                wifiMngr.setScanAlwaysAvailable(DBG);
            }
        }
    }

    public void setCarrierConfigBundle(PersistableBundle carrierConfig) {
        this.mCarrierConfig = carrierConfig;
    }

    protected PersistableBundle onGetCarrierConfig(CarrierConfigManager configManager) {
        PersistableBundle persistableBundle = this.mCarrierConfig;
        return persistableBundle == null ? super.onGetCarrierConfig(configManager) : persistableBundle;
    }

    protected boolean onSetUseImsForUt(boolean useImsForUt) {
        if (isInCSCall()) {
            return false;
        }
        return useImsForUt;
    }

    protected boolean onSetUseImsForCall(boolean useImsForCall) {
        if (isInCSCall()) {
            return false;
        }
        if (!useImsForCall) {
            CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
            boolean useImsFirstForCall = configManager.getConfigForSubId(getSubId()).getBoolean("mtk_use_ims_first_for_call_bool");
            logd("useImsFirstForCall = " + useImsFirstForCall);
            if (useImsFirstForCall && isImsUseEnabled()) {
                return DBG;
            }
            if (shouldProcessSelfActivation() || useImsForPCOChanged()) {
                logd("always use ImsPhone for self activation");
                return DBG;
            }
        }
        return useImsForCall;
    }

    protected boolean onCheckIfIgnoreServiceState() {
        if (SystemProperties.getInt("vendor.gsm.gcf.testmode", 0) == 2 || isAllowCallWhenOos()) {
            return DBG;
        }
        return false;
    }

    protected boolean onCheckIfIgnoreWifiOnlyModeCheck() {
        int setting = SubscriptionManager.getIntegerSubscriptionProperty(getSubId(), "wfc_ims_mode", 2, this.mContext);
        if (setting == 0) {
            return false;
        }
        return DBG;
    }

    protected GsmMmiCode onCreateGsmMmiCode(String networkPortion, UiccCardApplication app, ResultReceiver wrappedCallback) {
        return MtkGsmMmiCode.newFromDialString(networkPortion, this, (UiccCardApplication) this.mUiccApplication.get(), wrappedCallback);
    }

    protected void onPendingMmiAdded(String dialString, GsmMmiCode mmi) {
        Rlog.d(LOG_TAG, "dialInternal: " + MtkSuppServHelper.encryptString(dialString) + ", mmi=" + mmi);
        dumpPendingMmi();
    }

    protected boolean ssOverGsmPipeIfNeeded(Message onComplete) {
        if (isCsRetry(onComplete)) {
            return false;
        }
        return DBG;
    }

    protected boolean uiccProfileUpdateIfNeeded() {
        UiccCard uiccCard;
        UiccPort uiccPort;
        String iccId;
        Rlog.d(LOG_TAG, "uiccProfileUpdateIfNeeded(), enter");
        UiccSlot slot = this.mUiccController.getUiccSlotForPhone(this.mPhoneId);
        boolean expectedValue = DBG;
        if (slot == null || slot.getCardState() != IccCardStatus.CardState.CARDSTATE_PRESENT || this.mUiccApplicationsEnabled == null || (uiccCard = slot.getUiccCard()) == null || (uiccPort = uiccCard.getUiccPortForPhone(getPhoneId())) == null || (iccId = uiccPort.getIccId()) == null) {
            return DBG;
        }
        SubscriptionInfo info = MtkSubscriptionManagerService.getInstance().getSubInfoForIccId(IccUtils.stripTrailingFs(iccId));
        if (info != null) {
            expectedValue = info.areUiccApplicationsEnabled();
        }
        Rlog.d(LOG_TAG, "uiccProfileUpdateIfNeeded(), expectedValue is: " + expectedValue);
        return expectedValue;
    }

    public boolean isOP12UssdCode(String dialString) {
        boolean ret = false;
        if ("OP12".equals(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, ""))) {
            switch (dialString) {
                case "#225":
                case "#3282":
                case "#646":
                case "#768":
                case "#7623":
                case "#874":
                case "#832":
                case "*611":
                case "*73":
                case "*86":
                    ret = DBG;
                    break;
                default:
                    if (dialString.length() == 13) {
                        String first3Chars = dialString.substring(0, 3);
                        switch (first3Chars) {
                            case "*67":
                            case "*71":
                            case "*72":
                            case "*82":
                                String last10Chars = dialString.substring(3);
                                ret = last10Chars.chars().allMatch(new IntPredicate() { // from class: com.mediatek.internal.telephony.MtkGsmCdmaPhone$$ExternalSyntheticLambda0
                                    @Override // java.util.function.IntPredicate
                                    public final boolean test(int i) {
                                        return Character.isDigit(i);
                                    }
                                });
                                break;
                        }
                    }
                    break;
            }
            Rlog.d(LOG_TAG, "isOP12UssdCode = " + ret);
        }
        return ret;
    }
}
