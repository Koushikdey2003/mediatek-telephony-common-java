package com.mediatek.internal.telephony.uicc;

import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.phb.CsimPhbUtil;
import com.mediatek.internal.telephony.phb.MtkAdnRecordCache;
import com.mediatek.internal.telephony.uicc.IccServiceInfo;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MtkRuimRecords extends RuimRecords implements MtkIccConstants {
    public static final int C2K_PHB_NOT_READY = 2;
    public static final int C2K_PHB_READY = 3;
    private static final int CSIM_FDN_SERVICE_MASK_ACTIVE = 1;
    private static final int CSIM_FDN_SERVICE_MASK_EXIST = 2;
    private static final int EVENT_DELAYED_SEND_PHB_CHANGE = 503;
    private static final int EVENT_GET_EST_DONE = 501;
    private static final int EVENT_PHB_READY = 504;
    private static final int EVENT_RADIO_STATE_CHANGED = 502;
    public static final int GSM_PHB_NOT_READY = 0;
    public static final int GSM_PHB_READY = 1;
    static final String LOG_TAG = "RuimRecords";
    private static final int MCC_LEN = 3;
    static final String MTK_LOG_TAG = "MtkRuimRecords";
    public static final int PHB_DELAY_SEND_TIME = 500;
    static final String PROPERTY_RIL_C2K_PHB_READY = "vendor.cdma.sim.ril.phbready";
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    static final String PROPERTY_RIL_GSM_PHB_READY = "vendor.gsm.sim.ril.phbready";
    private static final int RUIM_FDN_SERVICE_MASK_EXIST_ACTIVE = 48;
    private static final int RUIM_FDN_SERVICE_MASK_EXIST_INACTIVE = 16;
    private boolean mDispose;
    private byte[] mEnableService;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mPendingPhbNotify;
    private boolean mPhbReady;
    private boolean mPhbWaitSub;
    private Phone mPhone;
    private int mPhoneId;
    private String mRuimImsi;
    private byte[] mSimService;
    private int mSubId;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkRuimRecords(MtkUiccCardApplication app, Context c, CommandsInterface ci) {
        super(app, c, ci);
        this.mRuimImsi = null;
        this.mPhoneId = -1;
        this.mPendingPhbNotify = false;
        this.mSubId = -1;
        this.mPhbReady = false;
        this.mPhbWaitSub = false;
        this.mDispose = false;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.uicc.MtkRuimRecords.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String strPhbReady;
                String action = intent.getAction();
                if (!action.equals("android.intent.action.RADIO_TECHNOLOGY")) {
                    if (action.equals("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED") && MtkRuimRecords.this.mParentApp != null) {
                        MtkRuimRecords.this.mtkLogd("[onReceive] onReceive ACTION_SUBINFO_RECORD_UPDATED mPhbWaitSub: " + MtkRuimRecords.this.mPhbWaitSub);
                        if (MtkRuimRecords.this.mPhbWaitSub) {
                            MtkRuimRecords.this.mPhbWaitSub = false;
                            MtkRuimRecords mtkRuimRecords = MtkRuimRecords.this;
                            mtkRuimRecords.broadcastPhbStateChangedIntent(mtkRuimRecords.mPhbReady, false);
                            return;
                        }
                        return;
                    }
                    if (action.equals("android.intent.action.SIM_STATE_CHANGED") && MtkRuimRecords.this.mParentApp != null) {
                        int id = intent.getIntExtra("phone", 0);
                        String simState = intent.getStringExtra("ss");
                        if (id == MtkRuimRecords.this.mPhoneId) {
                            if (CsimPhbUtil.isUsingGsmPhbReady(MtkRuimRecords.this.mFh)) {
                                strPhbReady = TelephonyManager.getTelephonyProperty(MtkRuimRecords.this.mPhoneId, MtkRuimRecords.PROPERTY_RIL_GSM_PHB_READY, "false");
                            } else {
                                strPhbReady = TelephonyManager.getTelephonyProperty(MtkRuimRecords.this.mPhoneId, MtkRuimRecords.PROPERTY_RIL_C2K_PHB_READY, "false");
                            }
                            MtkRuimRecords.this.mtkLogd("sim state: " + simState + ", mPhbReady: " + MtkRuimRecords.this.mPhbReady + ",strPhbReady: " + strPhbReady.equals("true"));
                            if ("READY".equals(simState)) {
                                if (!MtkRuimRecords.this.mPhbReady && strPhbReady.equals("true")) {
                                    MtkRuimRecords.this.mPhbReady = true;
                                    MtkRuimRecords mtkRuimRecords2 = MtkRuimRecords.this;
                                    mtkRuimRecords2.broadcastPhbStateChangedIntent(mtkRuimRecords2.mPhbReady, false);
                                    return;
                                } else {
                                    if (true == MtkRuimRecords.this.mPhbWaitSub && strPhbReady.equals("true")) {
                                        MtkRuimRecords.this.mtkLogd("mPhbWaitSub is " + MtkRuimRecords.this.mPhbWaitSub + ", broadcast if need");
                                        MtkRuimRecords.this.mPhbWaitSub = false;
                                        MtkRuimRecords mtkRuimRecords3 = MtkRuimRecords.this;
                                        mtkRuimRecords3.broadcastPhbStateChangedIntent(mtkRuimRecords3.mPhbReady, false);
                                        return;
                                    }
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                        MtkRuimRecords.this.mtkLogd("[onReceive] ACTION_BOOT_COMPLETED mPendingPhbNotify : " + MtkRuimRecords.this.mPendingPhbNotify);
                        if (MtkRuimRecords.this.mPendingPhbNotify) {
                            MtkRuimRecords mtkRuimRecords4 = MtkRuimRecords.this;
                            mtkRuimRecords4.broadcastPhbStateChangedIntent(mtkRuimRecords4.isPhbReady(), false);
                            MtkRuimRecords.this.mPendingPhbNotify = false;
                            return;
                        }
                        return;
                    }
                    return;
                }
                int phoneId = intent.getIntExtra("phone", -1);
                MtkRuimRecords.this.mtkLogd("[onReceive] ACTION_RADIO_TECHNOLOGY_CHANGED phoneId : " + phoneId);
                if (MtkRuimRecords.this.mParentApp != null && MtkRuimRecords.this.mParentApp.getPhoneId() == phoneId) {
                    String activePhoneName = intent.getStringExtra("phoneName");
                    int subId = intent.getIntExtra("subscription", -1);
                    boolean isUpbDone = ((MtkAdnRecordCache) MtkRuimRecords.this.mAdnCache).getUpbDone() == 1;
                    MtkRuimRecords.this.mtkLogd("[onReceive] ACTION_RADIO_TECHNOLOGY_CHANGED activePhoneName: " + activePhoneName + ",subId:" + subId + ",phoneId:" + phoneId + ",isUpbDOne:" + isUpbDone);
                    if (!isUpbDone && "CDMA".equals(activePhoneName)) {
                        MtkRuimRecords.this.broadcastPhbStateChangedIntent(false, true);
                        MtkRuimRecords mtkRuimRecords5 = MtkRuimRecords.this;
                        mtkRuimRecords5.sendMessageDelayed(mtkRuimRecords5.obtainMessage(MtkRuimRecords.EVENT_DELAYED_SEND_PHB_CHANGE), 500L);
                        MtkRuimRecords.this.mAdnCache.reset();
                    }
                }
            }
        };
        this.mIntentReceiver = broadcastReceiver;
        this.mPhoneId = app.getPhoneId();
        this.mPhone = PhoneFactory.getPhone(app.getPhoneId());
        mtkLogd("MtkRuimRecords X ctor this=" + this);
        this.mAdnCache = new MtkAdnRecordCache(this.mFh, ci, app);
        this.mCi.registerForPhbReady(this, EVENT_PHB_READY, null);
        this.mCi.registerForRadioStateChanged(this, EVENT_RADIO_STATE_CHANGED, (Object) null);
        this.mAdnCache.reset();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        filter.addAction("android.intent.action.RADIO_TECHNOLOGY");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        this.mContext.registerReceiver(broadcastReceiver, filter);
        mtkLogd("updateIccRecords in IccPhoneBookeInterfaceManager");
        Phone phone = this.mPhone;
        if (phone != null && phone.getIccPhoneBookInterfaceManager() != null) {
            this.mPhone.getIccPhoneBookInterfaceManager().updateIccRecords(this);
        }
        if (isPhbReady()) {
            this.mPhbReady = true;
            broadcastPhbStateChangedIntent(true, false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        mtkLogd("Disposing MtkRuimRecords " + this);
        this.mDispose = true;
        if (!isCdma4GDualModeCard()) {
            mtkLogd("dispose, reset operator numeric, name and country iso");
            this.mTelephonyManager.setSimOperatorNumericForPhone(this.mParentApp.getPhoneId(), "");
            this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), "");
            this.mTelephonyManager.setSimCountryIsoForPhone(this.mParentApp.getPhoneId(), "");
        }
        if (this.mPhbReady || this.mPendingPhbNotify) {
            mtkLogd("MtkRuimRecords Disposing set PHB unready mPendingPhbNotify=" + this.mPendingPhbNotify + "mPhbReady=" + this.mPhbReady);
            this.mPhbReady = false;
            this.mPendingPhbNotify = false;
            broadcastPhbStateChangedIntent(false, false);
        }
        this.mParentApp.unregisterForReady(this);
        this.mPhbWaitSub = false;
        this.mCi.unregisterForRadioStateChanged(this);
        this.mCi.unregisterForPhbReady(this);
        this.mContext.unregisterReceiver(this.mIntentReceiver);
        super.dispose();
    }

    public void handleMessage(Message msg) {
        try {
            if (this.mDestroyed.get()) {
                mtkLoge("Received message " + msg + "[" + msg.what + "] while being destroyed. Ignoring.");
                return;
            }
            try {
                switch (msg.what) {
                    case 10:
                        AsyncResult ar = (AsyncResult) msg.obj;
                        String[] localTemp = (String[]) ar.result;
                        if (ar.exception == null) {
                            this.mMyMobileNumber = localTemp[0];
                            this.mMin2Min1 = localTemp[3];
                            this.mPrlVersion = localTemp[4];
                            mtkLogd("MDN: " + MtkIccUtilsEx.getPrintableString(this.mMyMobileNumber, 8) + " MIN: " + MtkIccUtilsEx.getPrintableString(this.mMin2Min1, 8));
                        }
                        break;
                    case 17:
                        mtkLogd("Event EVENT_GET_SST_DONE Received");
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        if (ar2.exception == null) {
                            this.mSimService = (byte[]) ar2.result;
                            mtkLogd("mSimService[0]: " + ((int) this.mSimService[0]) + ", data.length: " + this.mSimService.length);
                            updateIccFdnStatus();
                        } else {
                            mtkLogi("EVENT_GET_SST_DONE failed");
                        }
                        break;
                    case EVENT_GET_EST_DONE /* 501 */:
                        mtkLogd("Event EVENT_GET_EST_DONE Received");
                        AsyncResult ar3 = (AsyncResult) msg.obj;
                        if (ar3.exception == null) {
                            this.mEnableService = (byte[]) ar3.result;
                            mtkLogd("mEnableService[0]: " + ((int) this.mEnableService[0]) + ", mEnableService.length: " + this.mEnableService.length);
                            updateIccFdnStatus();
                        } else {
                            mtkLogi("EVENT_GET_EST_DONE failed");
                        }
                        break;
                    case EVENT_DELAYED_SEND_PHB_CHANGE /* 503 */:
                        this.mPhbReady = isPhbReady();
                        mtkLogd("[EVENT_DELAYED_SEND_PHB_CHANGE] isReady : " + this.mPhbReady);
                        broadcastPhbStateChangedIntent(this.mPhbReady, false);
                        break;
                    case EVENT_PHB_READY /* 504 */:
                        AsyncResult ar4 = (AsyncResult) msg.obj;
                        mtkLogd("[DBG]EVENT_PHB_READY ar:" + ar4);
                        if (ar4 != null && ar4.exception == null && ar4.result != null) {
                            int[] phbReadyState = (int[]) ar4.result;
                            this.mParentApp.getPhoneId();
                            int curSimState = TelephonyManager.getSimStateForSlotIndex(this.mPhoneId);
                            boolean isSimLocked = curSimState == 4 || curSimState == 2;
                            updatePhbStatus(phbReadyState[0], isSimLocked);
                            updateIccFdnStatus();
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
                if (0 == 0) {
                    return;
                }
            } catch (RuntimeException exc) {
                Rlog.w(LOG_TAG, "Exception parsing RUIM record", exc);
                if (0 == 0) {
                    return;
                }
            }
            onRecordLoaded();
        } catch (Throwable th) {
            if (0 != 0) {
                onRecordLoaded();
            }
            throw th;
        }
    }

    protected void onAllRecordsLoaded() {
        super.onAllRecordsLoaded();
        mtkLogd("onAllRecordsLoaded, mParentApp.getType() = " + this.mParentApp.getType());
        if (this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM) {
            this.mFh.loadEFTransparent(28466, obtainMessage(17));
        } else if (this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_CSIM) {
            this.mFh.loadEFTransparent(28466, obtainMessage(17));
            this.mFh.loadEFTransparent(MtkIccConstants.EF_EST, obtainMessage(EVENT_GET_EST_DONE));
        }
    }

    public int getCarrierNameDisplayCondition() {
        String spn = getServiceProviderName();
        String uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(this.mPhoneId);
        mtkLogd("getCarrierNameDisplayCondition uiccProfile is " + ((Object) (uiccProfileForPhone != null ? uiccProfileForPhone : "null")));
        if (uiccProfileForPhone != null && uiccProfileForPhone.getOperatorBrandOverride() != null) {
            mtkLogd("getCarrierNameDisplayCondition, getOperatorBrandOverride is not null");
            return 1;
        }
        if (!this.mCsimSpnDisplayCondition) {
            mtkLogd("getCarrierNameDisplayCondition, no EF_SPN");
            return 1;
        }
        if (!TextUtils.isEmpty(spn) && !spn.equals("")) {
            mtkLogd("getCarrierNameDisplayCondition, show spn");
            return 2;
        }
        mtkLogd("getCarrierNameDisplayCondition, show plmn");
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mtkLogd(String s) {
        Rlog.d(MTK_LOG_TAG, "[MtkRuimRecords] " + s + " (phoneId " + this.mPhoneId + ")");
    }

    private void mtkLoge(String s) {
        Rlog.e(MTK_LOG_TAG, "[MtkRuimRecords] " + s + " (phoneId " + this.mPhoneId + ")");
    }

    private void mtkLogi(String s) {
        Rlog.i(MTK_LOG_TAG, "[MtkRuimRecords] " + s + " (phoneId " + this.mPhoneId + ")");
    }

    protected void log(String s) {
        Rlog.d(LOG_TAG, "[RuimRecords] " + s + " (phoneId " + this.mPhoneId + ")");
    }

    protected void loge(String s) {
        Rlog.e(LOG_TAG, "[RuimRecords] " + s + " (phoneId " + this.mPhoneId + ")");
    }

    public IccServiceInfo.IccServiceStatus getSIMServiceStatus(IccServiceInfo.IccService enService) {
        IccServiceInfo.IccServiceStatus simServiceStatus = IccServiceInfo.IccServiceStatus.UNKNOWN;
        if (this.mParentApp == null) {
            mtkLogd("getSIMServiceStatus enService: " + enService + ", mParentApp = null.");
            return simServiceStatus;
        }
        mtkLogd("getSIMServiceStatus enService: " + enService + ", mParentApp.getType(): " + this.mParentApp.getType());
        if (enService == IccServiceInfo.IccService.FDN && this.mSimService != null && this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM) {
            mtkLogd("getSIMServiceStatus mSimService[0]: " + ((int) this.mSimService[0]));
            byte b = this.mSimService[0];
            if ((b & 48) == RUIM_FDN_SERVICE_MASK_EXIST_ACTIVE) {
                return IccServiceInfo.IccServiceStatus.ACTIVATED;
            }
            if ((b & 16) == 16) {
                return IccServiceInfo.IccServiceStatus.INACTIVATED;
            }
            return IccServiceInfo.IccServiceStatus.NOT_EXIST_IN_SIM;
        }
        if (enService == IccServiceInfo.IccService.FDN && this.mSimService != null && this.mEnableService != null && this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_CSIM) {
            mtkLogd("getSIMServiceStatus mSimService[0]: " + ((int) this.mSimService[0]) + ", mEnableService[0]: " + ((int) this.mEnableService[0]));
            byte b2 = this.mSimService[0];
            if ((b2 & 2) == 2 && (this.mEnableService[0] & 1) == 1) {
                return IccServiceInfo.IccServiceStatus.ACTIVATED;
            }
            if ((b2 & 2) == 2) {
                return IccServiceInfo.IccServiceStatus.INACTIVATED;
            }
            return IccServiceInfo.IccServiceStatus.NOT_EXIST_IN_USIM;
        }
        return simServiceStatus;
    }

    public boolean isCdma4GDualModeCard() {
        String[] values = null;
        int i = this.mPhoneId;
        if (i >= 0) {
            String[] strArr = PROPERTY_RIL_FULL_UICC_TYPE;
            if (i < strArr.length) {
                String prop = SystemProperties.get(strArr[i]);
                if (prop != null && prop.length() > 0) {
                    values = prop.split(",");
                }
                mtkLogd("isCdma4GDualModeCard PhoneId " + this.mPhoneId + ", prop value= " + prop + ", size= " + (values != null ? values.length : 0));
                return values != null && Arrays.asList(values).contains("USIM") && Arrays.asList(values).contains("CSIM");
            }
        }
        mtkLogd("isCdma4GDualModeCard: invalid PhoneId " + this.mPhoneId);
        return false;
    }

    protected void updateIccFdnStatus() {
        mtkLogd("updateIccFdnStatus mParentAPP=" + this.mParentApp + "  getSIMServiceStatus(Phone.IccService.FDN)=" + getSIMServiceStatus(IccServiceInfo.IccService.FDN) + "  IccServiceStatus.ACTIVATE=" + IccServiceInfo.IccServiceStatus.ACTIVATED);
        if (this.mParentApp != null && getSIMServiceStatus(IccServiceInfo.IccService.FDN) == IccServiceInfo.IccServiceStatus.ACTIVATED) {
            this.mParentApp.queryFdn();
        }
    }

    public boolean isPhbReady() {
        String strPhbReady;
        String strCurSimState = "";
        mtkLogd("[phbReady] Start mPhbReady: " + (this.mPhbReady ? "true" : "false"));
        if (this.mParentApp == null) {
            return false;
        }
        if (CsimPhbUtil.isUsingGsmPhbReady(this.mFh)) {
            strPhbReady = TelephonyManager.getTelephonyProperty(this.mPhoneId, PROPERTY_RIL_GSM_PHB_READY, "false");
        } else {
            strPhbReady = TelephonyManager.getTelephonyProperty(this.mPhoneId, PROPERTY_RIL_C2K_PHB_READY, "false");
        }
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            return strPhbReady.equals("true");
        }
        String strAllSimState = SystemProperties.get("gsm.sim.state");
        if (strAllSimState != null && strAllSimState.length() > 0) {
            String[] values = strAllSimState.split(",");
            int i = this.mPhoneId;
            if (i >= 0 && i < values.length && values[i] != null) {
                strCurSimState = values[i];
            }
        }
        boolean isSimLocked = strCurSimState.equals("NETWORK_LOCKED") || strCurSimState.equals("PIN_REQUIRED");
        mtkLogd("[phbReady] End strPhbReady: " + strPhbReady + ", strAllSimState: " + strAllSimState);
        return strPhbReady.equals("true") && !isSimLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastPhbStateChangedIntent(boolean isReady, boolean isForceSendIntent) {
        int[] subIds;
        Phone phone = this.mPhone;
        if (phone != null && phone.getPhoneType() != 2 && (!this.mDispose || isReady)) {
            this.mPendingPhbNotify = true;
            mtkLogd("broadcastPhbStateChangedIntent, No active Phone will notfiy when dispose");
            return;
        }
        mtkLogd("broadcastPhbStateChangedIntent, mPhbReady " + this.mPhbReady + ", " + this.mSubId);
        if (isReady) {
            int[] subIds2 = SubscriptionManager.getSubId(this.mPhoneId);
            if (subIds2 != null && subIds2.length > 0) {
                this.mSubId = subIds2[0];
            }
            if (this.mSubId <= 0) {
                mtkLogd("broadcastPhbStateChangedIntent, mSubId <= 0");
                this.mPhbWaitSub = true;
                return;
            }
        } else {
            if (isForceSendIntent && this.mPhbReady && (subIds = SubscriptionManager.getSubId(this.mPhoneId)) != null && subIds.length > 0) {
                this.mSubId = subIds[0];
            }
            if (this.mSubId <= 0) {
                mtkLogd("broadcastPhbStateChangedIntent, isReady == false and mSubId <= 0");
                return;
            }
        }
        UserManager userManager = (UserManager) this.mContext.getSystemService(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
        boolean isUnlock = userManager.isUserUnlocked();
        if (!SystemProperties.get("sys.boot_completed").equals(RadioCapabilitySwitchUtil.IMSI_READY) || !isUnlock) {
            mtkLogd("broadcastPhbStateChangedIntent, boot not completed, isUnlock:" + isUnlock);
            this.mPendingPhbNotify = true;
            return;
        }
        Intent intent = new Intent("mediatek.intent.action.PHB_STATE_CHANGED");
        BroadcastOptions options = BroadcastOptions.makeBasic();
        options.setDeliveryGroupPolicy(0);
        intent.putExtra("ready", isReady);
        intent.putExtra("subscription", this.mSubId);
        mtkLogd("Broadcasting intent ACTION_PHB_STATE_CHANGED " + isReady + " sub id " + this.mSubId + " phoneId " + this.mParentApp.getPhoneId());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, null, options.toBundle());
        Intent bootIntent = new Intent("mediatek.intent.action.PHB_STATE_CHANGED");
        bootIntent.putExtra("ready", isReady);
        bootIntent.putExtra("subscription", this.mSubId);
        bootIntent.setPackage("com.mediatek.simprocessor");
        mtkLogd("Broadcasting intent ACTION_PHB_STATE_CHANGED to package: simprocessor");
        this.mContext.sendBroadcastAsUser(bootIntent, UserHandle.ALL);
        if (!isReady) {
            this.mSubId = -1;
        }
    }

    private void updatePhbStatus(int status, boolean isSimLocked) {
        boolean simLockedState;
        boolean isReady;
        mtkLogd("[PhbStatus] status: " + status + ", isSimLocked: " + isSimLocked + ", mPhbReady: " + this.mPhbReady);
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            simLockedState = false;
        } else {
            simLockedState = isSimLocked;
        }
        if (CsimPhbUtil.isUsingGsmPhbReady(this.mFh)) {
            if (status == 1) {
                isReady = true;
            } else if (status == 0) {
                isReady = false;
            } else {
                mtkLogd("[PhbStatus] not GSM PHB status");
                return;
            }
        } else if (status == 3) {
            isReady = true;
        } else if (status == 2) {
            isReady = false;
        } else {
            mtkLogd("[PhbStatus] not C2K PHB status");
            return;
        }
        if (isReady) {
            if (!simLockedState) {
                boolean z = this.mPhbReady;
                if (!z) {
                    this.mPhbReady = true;
                    broadcastPhbStateChangedIntent(true, false);
                    return;
                } else {
                    broadcastPhbStateChangedIntent(z, false);
                    return;
                }
            }
            mtkLogd("[PhbStatus] phb ready but sim is not ready.");
            this.mPhbReady = false;
            broadcastPhbStateChangedIntent(false, false);
            return;
        }
        boolean z2 = this.mPhbReady;
        if (z2) {
            this.mAdnCache.reset();
            this.mPhbReady = false;
            broadcastPhbStateChangedIntent(false, false);
            return;
        }
        broadcastPhbStateChangedIntent(z2, false);
    }

    protected void onGetImsiDone(String imsi) {
        if (this.mImsi != null && !this.mImsi.equals("") && this.mImsi.length() >= 3) {
            SystemProperties.set("vendor.cdma.icc.operator.mcc", this.mImsi.substring(0, 3));
        }
        if (this.mImsi != null && !this.mImsi.equals(this.mRuimImsi)) {
            this.mRuimImsi = this.mImsi;
            this.mImsiReadyRegistrants.notifyRegistrants();
            mtkLogd("MtkRuimRecords: mImsiReadyRegistrants.notifyRegistrants");
        }
    }

    protected void handleRefresh(IccRefreshResponse refreshResponse) {
        if (refreshResponse == null) {
            mtkLogd("handleRefresh received without input");
        }
        if (refreshResponse.aid != null && !TextUtils.isEmpty(refreshResponse.aid) && !refreshResponse.aid.equals(this.mParentApp.getAid()) && refreshResponse.refreshResult != 4) {
            return;
        }
        switch (refreshResponse.refreshResult) {
            case 1:
                mtkLogd("handleRefresh with SIM_REFRESH_INIT");
                handleFileUpdate(-1);
                break;
            case 2:
                mtkLogd("handleRefresh with SIM_REFRESH_RESET");
                break;
            case 3:
            default:
                mtkLogd("handleRefresh,callback to super");
                super.handleRefresh(refreshResponse);
                break;
            case 4:
                mtkLogd("handleRefresh with REFRESH_INIT_FULL_FILE_UPDATED");
                handleFileUpdate(-1);
                break;
        }
    }
}
