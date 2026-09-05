package com.mediatek.internal.telephony.uicc;

import android.app.AlertDialog;
import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordLoader;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UsimServiceTable;
import com.mediatek.internal.telephony.MtkIccUtils;
import com.mediatek.internal.telephony.MtkSubscriptionManager;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.phb.MtkAdnRecordCache;
import com.mediatek.internal.telephony.uicc.IccServiceInfo;
import com.mediatek.internal.telephony.worldphone.IWorldPhone;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class MtkSIMRecords extends SIMRecords {
    private static final int EVENT_CFU_IND = 1021;
    private static final int EVENT_DELAYED_SEND_PHB_CHANGE = 1026;
    private static final int EVENT_GET_ALL_OPL5G_DONE = 1030;
    private static final int EVENT_GET_CPHSONS_DONE = 1009;
    private static final int EVENT_GET_SHORT_CPHSONS_DONE = 1010;
    private static final int EVENT_GET_SMSP_DONE = 1018;
    private static final int EVENT_IMSI_REFRESH_QUERY = 1022;
    private static final int EVENT_IMSI_REFRESH_QUERY_DONE = 1023;
    public static final int EVENT_MSISDN = 100;
    public static final int EVENT_OPL5G = 103;
    private static final int EVENT_PHB_READY = 1027;
    private static final int EVENT_QUERY_MENU_TITLE_DONE = 1005;
    private static final int EVENT_RADIO_AVAILABLE = 1001;
    private static final int EVENT_RADIO_STATE_CHANGED = 1012;
    private static final int EVENT_RSU_SIM_LOCK_CHANGED = 1029;
    private static final int GSM_PHB_NOT_READY = 0;
    private static final int GSM_PHB_READY = 1;
    protected static final String LOG_TAG_EX = "MtkSIMRecords";
    private static final int MTK_SIM_RECORD_EVENT_BASE = 1000;
    private static final String SIMRECORD_PROPERTY_RIL_PHB_READY = "vendor.gsm.sim.ril.phbready";
    String cphsOnsl;
    String cphsOnss;
    private boolean isDispose;
    private boolean isValidMBI;
    private ArrayList<IccRecords.OperatorPlmnInfo> m5gsOperatorList;
    private byte[] mEfSST;
    private boolean mIsPhbEfResetDone;
    protected String mOldMccMnc;
    private String mOldOperatorDefaultName;
    private boolean mPendingPhbNotify;
    private boolean mPhbReady;
    private PhbBroadCastReceiver mPhbReceiver;
    private boolean mPhbWaitSub;
    private Phone mPhone;
    private String mSimImsi;
    private BroadcastReceiver mSimReceiver;
    protected int mSlotId;
    private String mSpNameInEfSpn;
    private MtkSpnOverride mSpnOverride;
    private int mSubId;
    private UiccCard mUiccCard;
    private UiccController mUiccController;
    protected static final boolean ENGDEBUG = TextUtils.equals(Build.TYPE, "eng");
    protected static final boolean USERDEBUG = TextUtils.equals(Build.TYPE, DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    static final String[] SIMRECORD_PROPERTY_RIL_PUK1 = {"vendor.gsm.sim.retry.puk1", "vendor.gsm.sim.retry.puk1.2", "vendor.gsm.sim.retry.puk1.3", "vendor.gsm.sim.retry.puk1.4"};
    private static final int[] simServiceNumber = {1, 17, 51, 52, 54, 55, 56, 0, 12, 3, 7, 0, 0};
    private static final int[] usimServiceNumber = {0, 19, 45, 46, 48, 49, 51, 71, 12, 2, 0, 42, 0};

    /* JADX WARN: Multi-variable type inference failed */
    public MtkSIMRecords(MtkUiccCardApplication mtkUiccCardApplication, Context context, CommandsInterface commandsInterface) {
        super(mtkUiccCardApplication, context, commandsInterface);
        this.mSubId = -1;
        this.mPhbReady = false;
        this.mPhbWaitSub = false;
        this.mIsPhbEfResetDone = false;
        this.mPendingPhbNotify = false;
        this.isValidMBI = false;
        this.mSimImsi = null;
        this.mEfSST = null;
        this.m5gsOperatorList = null;
        this.mSpNameInEfSpn = null;
        this.isDispose = false;
        this.mSpnOverride = null;
        this.mOldMccMnc = "";
        this.mOldOperatorDefaultName = null;
        mtkLog("MtkSIMRecords constructor");
        this.mSlotId = mtkUiccCardApplication.getPhoneId();
        UiccController uiccController = UiccController.getInstance();
        this.mUiccController = uiccController;
        this.mUiccCard = uiccController.getUiccCard(this.mSlotId);
        mtkLog("mUiccCard Instance = " + this.mUiccCard);
        this.mPhone = PhoneFactory.getPhone(mtkUiccCardApplication.getPhoneId());
        this.mSpnOverride = MtkSpnOverride.getInstance();
        this.cphsOnsl = null;
        this.cphsOnss = null;
        this.mCi.registerForCallForwardingInfo(this, EVENT_CFU_IND, null);
        this.mCi.registerForRadioStateChanged(this, 1012, (Object) null);
        this.mCi.registerForAvailable(this, 1001, (Object) null);
        this.mCi.registerForImsiRefreshDone(this, EVENT_IMSI_REFRESH_QUERY, null);
        this.mSimReceiver = new SIMBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this.mSimReceiver, intentFilter);
        this.mAdnCache = new MtkAdnRecordCache(this.mFh, commandsInterface, mtkUiccCardApplication);
        this.mCi.registerForPhbReady(this, EVENT_PHB_READY, null);
        this.mPhbReceiver = new PhbBroadCastReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        intentFilter2.addAction("android.intent.action.RADIO_TECHNOLOGY");
        intentFilter2.addAction("android.intent.action.BOOT_COMPLETED");
        this.mContext.registerReceiver(this.mPhbReceiver, intentFilter2);
        mtkLog("SIMRecords updateIccRecords");
        Phone phone = this.mPhone;
        if (phone != null && phone.getIccPhoneBookInterfaceManager() != null) {
            this.mPhone.getIccPhoneBookInterfaceManager().updateIccRecords(this);
        }
        if (isPhbReady()) {
            mtkLog("Phonebook is ready.");
            this.mPhbReady = true;
            broadcastPhbStateChangedIntent(true, false);
        }
        this.mCi.registerForRsuSimLockChanged(this, EVENT_RSU_SIM_LOCK_CHANGED, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        mtkLog("Disposing MtkSIMRecords this=" + this);
        this.isDispose = true;
        this.mCi.unregisterForCallForwardingInfo(this);
        this.mCi.unregisterForRadioStateChanged(this);
        this.mContext.unregisterReceiver(this.mSimReceiver);
        this.mIccId = null;
        this.mImsi = null;
        this.mCi.unregisterForPhbReady(this);
        this.mContext.unregisterReceiver(this.mPhbReceiver);
        this.mPhbWaitSub = false;
        if (this.mPhbReady || this.mPendingPhbNotify) {
            mtkLog("MtkSIMRecords Disposing  set PHB unready mPendingPhbNotify=" + this.mPendingPhbNotify + ", mPhbReady=" + this.mPhbReady);
            this.mPhbReady = false;
            this.mPendingPhbNotify = false;
            broadcastPhbStateChangedIntent(false, false);
        }
        this.mCallForwardingStatus = 0;
        new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.uicc.MtkSIMRecords.1
            @Override // java.lang.Runnable
            public void run() {
                MtkSIMRecords.this.mPhone.notifyCallForwardingIndicator();
            }
        }).start();
        this.mCi.unregisterForRsuSimLockChanged(this);
        super.dispose();
    }

    public boolean checkEfCfis() {
        boolean isValid = this.mEfCfis != null && this.mEfCfis.length == 16;
        mtkLog("mEfCfis is null? = " + (this.mEfCfis == null));
        return isValid;
    }

    public String getVoiceMailNumber() {
        mtkLog("getVoiceMailNumber " + MtkIccUtilsEx.getPrintableString(this.mVoiceMailNum, 8));
        return super.getVoiceMailNumber();
    }

    public void setVoiceMailNumber(String alphaTag, String voiceNumber, Message onComplete) {
        mtkLog("setVoiceMailNumber, mIsVoiceMailFixed:" + this.mIsVoiceMailFixed + ", mMailboxIndex:" + this.mMailboxIndex + ", isCphsMailboxEnabled:" + isCphsMailboxEnabled() + ", alphaTag:" + alphaTag + ", voiceNumber:" + MtkIccUtilsEx.getPrintableString(voiceNumber, 8));
        super.setVoiceMailNumber(alphaTag, voiceNumber, onComplete);
    }

    public String getSIMCPHSOns() {
        String str = this.cphsOnsl;
        if (str != null) {
            return str;
        }
        return this.cphsOnss;
    }

    public void handleMessage(Message msg) {
        boolean isRecordLoadResponse = false;
        if (this.mDestroyed.get() && shallIgnoreMessage(msg)) {
            return;
        }
        try {
            try {
                boolean z = true;
                switch (msg.what) {
                    case 5:
                        isRecordLoadResponse = true;
                        AsyncResult ar = (AsyncResult) msg.obj;
                        byte[] data = (byte[]) ar.result;
                        boolean isValidMbdn = false;
                        if (ar.exception == null) {
                            mtkLog("EF_MBI: " + IccUtils.bytesToHexString(data));
                            this.mMailboxIndex = data[0] & 255;
                            if (this.mMailboxIndex != 0 && this.mMailboxIndex != 255) {
                                mtkLog("Got valid mailbox number for MBDN");
                                isValidMbdn = true;
                                this.isValidMBI = true;
                            }
                        }
                        this.mRecordsToLoad++;
                        if (isValidMbdn) {
                            mtkLog("EVENT_GET_MBI_DONE, to load EF_MBDN");
                            new AdnRecordLoader(this.mFh).loadFromEF(28615, 28616, this.mMailboxIndex, obtainMessage(6));
                        } else if (!isCphsMailboxEnabled()) {
                            mtkLog("EVENT_GET_MBI_DONE, do nothing");
                            this.mRecordsToLoad--;
                        } else {
                            mtkLog("EVENT_GET_MBI_DONE, to load EF_MAILBOX_CPHS");
                            new AdnRecordLoader(this.mFh).loadFromEF(28439, 28490, 1, obtainMessage(11));
                        }
                        break;
                    case 10:
                        isRecordLoadResponse = true;
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        if (ar2.exception == null) {
                            AdnRecord adn = (AdnRecord) ar2.result;
                            this.mMsisdn = adn.getNumber();
                            this.mMsisdnTag = adn.getAlphaTag();
                            this.mRecordsEventsRegistrants.notifyResult(100);
                            mtkLog("MSISDN: " + MtkIccUtilsEx.getPrintableString(this.mMsisdn, 8));
                        } else {
                            mtkLoge("Invalid or missing EF[MSISDN]");
                        }
                        break;
                    case 12:
                        isRecordLoadResponse = true;
                        AsyncResult ar3 = (AsyncResult) msg.obj;
                        this.mSpnState = SIMRecords.GetSpnFsmState.IDLE;
                        if (ar3 != null && ar3.exception == null) {
                            byte[] data2 = (byte[]) ar3.result;
                            int displayCondition = data2[0] & 255;
                            this.mCarrierNameDisplayCondition = 0;
                            if ((displayCondition & 1) == 1) {
                                this.mCarrierNameDisplayCondition |= 1;
                            }
                            if ((displayCondition & 2) == 0) {
                                this.mCarrierNameDisplayCondition |= 2;
                            }
                            setServiceProviderName(IccUtils.adnStringFieldToString(data2, 1, data2.length - 1));
                            String serviceProviderName = getServiceProviderName();
                            this.mSpNameInEfSpn = serviceProviderName;
                            if (serviceProviderName != null && serviceProviderName.equals("")) {
                                mtkLog("set mSpNameInEfSpn to null as parsing result is empty");
                                this.mSpNameInEfSpn = null;
                            }
                            mtkLog("Load EF_SPN: " + getServiceProviderName() + " carrierNameDisplayCondition: " + this.mCarrierNameDisplayCondition);
                            this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), getServiceProviderName());
                        } else {
                            mtkLoge("Read EF_SPN fail!");
                            this.mCarrierNameDisplayCondition = 0;
                        }
                        break;
                    case 17:
                        isRecordLoadResponse = true;
                        AsyncResult ar4 = (AsyncResult) msg.obj;
                        byte[] data3 = (byte[]) ar4.result;
                        if (ar4.exception == null) {
                            this.mUsimServiceTable = new UsimServiceTable(data3);
                            mtkLog("SST: " + this.mUsimServiceTable);
                            this.mEfSST = data3;
                        }
                        break;
                    case 26:
                        isRecordLoadResponse = true;
                        AsyncResult ar5 = (AsyncResult) msg.obj;
                        if (ar5.exception == null) {
                            this.mCphsInfo = (byte[]) ar5.result;
                            mtkLog("iCPHS: " + IccUtils.bytesToHexString(this.mCphsInfo));
                            if (!this.isValidMBI && isCphsMailboxEnabled()) {
                                this.mRecordsToLoad++;
                                new AdnRecordLoader(this.mFh).loadFromEF(28439, 28490, 1, obtainMessage(11));
                            }
                        }
                        break;
                    case IWorldPhone.EVENT_REG_SUSPENDED_1 /* 30 */:
                        isRecordLoadResponse = false;
                        AsyncResult ar6 = (AsyncResult) msg.obj;
                        if (ar6.exception == null) {
                            this.mMsisdn = this.mNewMsisdn;
                            this.mMsisdnTag = this.mNewMsisdnTag;
                            this.mRecordsEventsRegistrants.notifyResult(100);
                            mtkLog("Success to update EF[MSISDN]");
                        }
                        if (ar6.userObj != null) {
                            AsyncResult.forMessage((Message) ar6.userObj).exception = ar6.exception;
                            ((Message) ar6.userObj).sendToTarget();
                        }
                        break;
                    case 257:
                        super.handleMessage(msg);
                        break;
                    case 258:
                        super.handleMessage(msg);
                        break;
                    case 1001:
                        this.mMsisdn = "";
                        this.mRecordsEventsRegistrants.notifyResult(100);
                        break;
                    case 1009:
                        mtkLog("handleMessage (EVENT_GET_CPHSONS_DONE)");
                        isRecordLoadResponse = false;
                        AsyncResult ar7 = (AsyncResult) msg.obj;
                        if (ar7 != null && ar7.exception == null) {
                            byte[] data4 = (byte[]) ar7.result;
                            this.cphsOnsl = IccUtils.adnStringFieldToString(data4, 0, data4.length);
                            mtkLog("Load EF_SPN_CPHS: " + this.cphsOnsl);
                        }
                        break;
                    case 1010:
                        mtkLog("handleMessage (EVENT_GET_SHORT_CPHSONS_DONE)");
                        isRecordLoadResponse = false;
                        AsyncResult ar8 = (AsyncResult) msg.obj;
                        if (ar8 != null && ar8.exception == null) {
                            byte[] data5 = (byte[]) ar8.result;
                            this.cphsOnss = IccUtils.adnStringFieldToString(data5, 0, data5.length);
                            mtkLog("Load EF_SPN_SHORT_CPHS: " + this.cphsOnss);
                        }
                        break;
                    case EVENT_CFU_IND /* 1021 */:
                        AsyncResult ar9 = (AsyncResult) msg.obj;
                        if (ar9 != null && ar9.exception == null && ar9.result != null) {
                            int[] cfuResult = (int[]) ar9.result;
                            mtkLog("handle EVENT_CFU_IND: " + cfuResult[0]);
                        }
                        break;
                    case EVENT_IMSI_REFRESH_QUERY /* 1022 */:
                        if (USERDEBUG) {
                            mtkLog("handleMessage (EVENT_IMSI_REFRESH_QUERY)");
                        } else {
                            mtkLog("handleMessage (EVENT_IMSI_REFRESH_QUERY) mImsi= " + getIMSI());
                        }
                        this.mCi.getIMSIForApp(this.mParentApp.getAid(), obtainMessage(1023));
                        break;
                    case 1023:
                        mtkLog("handleMessage (EVENT_IMSI_REFRESH_QUERY_DONE)");
                        AsyncResult ar10 = (AsyncResult) msg.obj;
                        if (ar10.exception == null) {
                            this.mImsi = IccUtils.stripTrailingFs((String) ar10.result);
                            if (!Objects.equals(this.mImsi, (String) ar10.result)) {
                                mtkLoge("Invalid IMSI padding digits received.");
                            }
                            if (TextUtils.isEmpty(this.mImsi)) {
                                this.mImsi = null;
                            }
                            if (this.mImsi != null && !this.mImsi.matches("[0-9]+")) {
                                mtkLoge("Invalid non-numeric IMSI digits received.");
                                this.mImsi = null;
                            }
                            if (this.mImsi != null && (this.mImsi.length() < 6 || this.mImsi.length() > 15)) {
                                mtkLoge("invalid IMSI " + this.mImsi);
                                this.mImsi = null;
                            }
                            mtkLog("IMSI: mMncLength=" + this.mMncLength);
                            if (this.mImsi != null && this.mImsi.length() >= 6) {
                                mtkLog("IMSI: " + this.mImsi.substring(0, 6));
                            }
                            updateOperatorPlmn();
                            if (!this.mImsi.equals(this.mSimImsi)) {
                                this.mSimImsi = this.mImsi;
                                this.mImsiReadyRegistrants.notifyRegistrants();
                                mtkLog("SimRecords: mImsiReadyRegistrants.notifyRegistrants");
                            }
                            if (this.mRecordsToLoad == 0 && this.mRecordsRequested) {
                                onAllRecordsLoaded();
                            }
                        } else {
                            mtkLoge("Exception querying IMSI, Exception:" + ar10.exception);
                        }
                        break;
                    case EVENT_DELAYED_SEND_PHB_CHANGE /* 1026 */:
                        this.mPhbReady = isPhbReady();
                        mtkLog("[EVENT_DELAYED_SEND_PHB_CHANGE] isReady : " + this.mPhbReady);
                        broadcastPhbStateChangedIntent(this.mPhbReady, false);
                        break;
                    case EVENT_PHB_READY /* 1027 */:
                        AsyncResult ar11 = (AsyncResult) msg.obj;
                        if (ar11 != null && ar11.exception == null && ar11.result != null) {
                            int[] phbReadyState = (int[]) ar11.result;
                            int curSimState = TelephonyManager.getSimStateForSlotIndex(this.mSlotId);
                            if (curSimState != 4 && curSimState != 2) {
                                z = false;
                            }
                            boolean isSimLocked = z;
                            mtkLog("phbReadyState=" + phbReadyState[0] + ",curSimState = " + curSimState + ", isSimLocked = " + isSimLocked);
                            updatePHBStatus(phbReadyState[0], isSimLocked);
                        }
                        break;
                    case EVENT_RSU_SIM_LOCK_CHANGED /* 1029 */:
                        mtkLog("[RSU-SIMLOCK] handleMessage (EVENT_RSU_SIM_LOCK_CHANGED)");
                        AsyncResult ar12 = (AsyncResult) msg.obj;
                        if (ar12 != null && ar12.exception == null && ar12.result != null) {
                            int[] simMelockEvent = (int[]) ar12.result;
                            mtkLog("[RSU-SIMLOCK] sim melock event = " + simMelockEvent[0]);
                            RebootClickListener listener = new RebootClickListener();
                            if (simMelockEvent[0] == 0) {
                                AlertDialog alertDialog = new AlertDialog.Builder(this.mContext).setTitle("Unlock Phone").setMessage("Please restart the phone now since unlock setting has changed.").setPositiveButton("OK", listener).create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.getWindow().setType(2003);
                                alertDialog.show();
                            }
                        }
                        break;
                    case EVENT_GET_ALL_OPL5G_DONE /* 1030 */:
                        isRecordLoadResponse = false;
                        AsyncResult ar13 = (AsyncResult) msg.obj;
                        if (ar13.exception == null) {
                            parseEFopl5g((ArrayList) ar13.result);
                            this.mRecordsEventsRegistrants.notifyResult(Integer.valueOf(EVENT_OPL5G));
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
                if (!isRecordLoadResponse) {
                    return;
                }
            } catch (RuntimeException exc) {
                mtkLogw("Exception parsing SIM record", exc);
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

    protected void handleFileUpdate(int efid) {
        switch (efid) {
            case 20272:
            case 28474:
            case 28489:
                break;
            case 28435:
                this.mRecordsToLoad++;
                mtkLog("SIM Refresh called for EF_CFF_CPHS");
                this.mFh.loadEFTransparent(28435, obtainMessage(24));
                return;
            case 28437:
                this.mRecordsToLoad++;
                mtkLog("[CSP] SIM Refresh for EF_CSP_CPHS");
                this.mFh.loadEFTransparent(28437, obtainMessage(33));
                return;
            case 28439:
                this.mRecordsToLoad++;
                new AdnRecordLoader(this.mFh).loadFromEF(28439, 28490, 1, obtainMessage(11));
                return;
            case 28475:
                mtkLog("SIM Refresh called for EF_FDN");
                this.mParentApp.queryFdn();
                break;
            case 28480:
                this.mRecordsToLoad++;
                mtkLog("SIM Refresh called for EF_MSISDN");
                new AdnRecordLoader(this.mFh).loadFromEF(28480, getExtFromEf(28480), 1, obtainMessage(10));
                return;
            case 28615:
                this.mRecordsToLoad++;
                new AdnRecordLoader(this.mFh).loadFromEF(28615, 28616, this.mMailboxIndex, obtainMessage(6));
                return;
            case 28619:
                this.mRecordsToLoad++;
                mtkLog("SIM Refresh called for EF_CFIS");
                this.mFh.loadEFLinearFixed(28619, 1, obtainMessage(32));
                return;
            default:
                mtkLog("handleFileUpdate default");
                if (((MtkAdnRecordCache) this.mAdnCache).isUsimPhbEfAndNeedReset(efid) && !this.mIsPhbEfResetDone) {
                    this.mIsPhbEfResetDone = true;
                    this.mAdnCache.reset();
                    setPhbReady(false);
                }
                this.mLoaded.set(false);
                fetchSimRecords();
                return;
        }
        if (!this.mIsPhbEfResetDone) {
            this.mIsPhbEfResetDone = true;
            this.mAdnCache.reset();
            mtkLog("handleFileUpdate ADN like");
            setPhbReady(false);
        }
    }

    protected void handleRefresh(IccRefreshResponse refreshResponse) {
        if (refreshResponse == null) {
            mtkLog("handleSimRefresh received without input");
        }
        if (refreshResponse.aid != null && !TextUtils.isEmpty(refreshResponse.aid) && !refreshResponse.aid.equals(this.mParentApp.getAid()) && refreshResponse.refreshResult != 4) {
            mtkLog("handleRefresh, refreshResponse.aid = " + refreshResponse.aid + ", mParentApp.getAid() = " + this.mParentApp.getAid());
            return;
        }
        switch (refreshResponse.refreshResult) {
            case 0:
                mtkLog("handleRefresh with SIM_REFRESH_FILE_UPDATED");
                handleFileUpdate(refreshResponse.efId);
                this.mIsPhbEfResetDone = false;
                break;
            case 1:
                mtkLog("handleRefresh with SIM_REFRESH_INIT");
                handleFileUpdate(-1);
                break;
            case 2:
                mtkLog("handleRefresh with SIM_REFRESH_RESET");
                if (!SystemProperties.get("ro.vendor.sim_refresh_reset_by_modem").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
                    mtkLog("sim_refresh_reset_by_modem false");
                    if (this.mCi != null) {
                        this.mCi.restartRILD(null);
                    }
                } else {
                    mtkLog("Sim reset by modem!");
                }
                setPhbReady(false);
                handleFileUpdate(-1);
                break;
            case 3:
            default:
                mtkLog("handleSimRefresh callback to parent");
                super.handleRefresh(refreshResponse);
                break;
            case 4:
                mtkLog("handleRefresh with REFRESH_INIT_FULL_FILE_UPDATED");
                setPhbReady(false);
                handleFileUpdate(-1);
                break;
            case 5:
                mtkLog("handleRefresh with REFRESH_INIT_FILE_UPDATED, EFID = " + refreshResponse.efId);
                handleFileUpdate(refreshResponse.efId);
                this.mIsPhbEfResetDone = false;
                if (this.mParentApp.getState() == IccCardApplicationStatus.AppState.APPSTATE_READY) {
                    sendMessage(obtainMessage(257));
                }
                break;
            case 6:
                mtkLog("handleSimRefresh with REFRESH_SESSION_RESET");
                handleFileUpdate(-1);
                break;
        }
    }

    private String findBestLanguage(byte[] languages) {
        String[] locales = this.mContext.getAssets().getLocales();
        if (languages == null || locales == null) {
            return null;
        }
        for (int i = 0; i + 1 < languages.length; i += 2) {
            try {
                String lang = new String(languages, i, 2, "ISO-8859-1");
                mtkLog("languages from sim = " + lang);
                for (int j = 0; j < locales.length; j++) {
                    if (locales[j] != null && locales[j].length() >= 2 && locales[j].substring(0, 2).equalsIgnoreCase(lang)) {
                        return lang;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                mtkLog("Failed to parse USIM language records" + e);
            }
            if (0 != 0) {
                break;
            }
        }
        return null;
    }

    protected void onAllRecordsLoaded() {
        if (this.mParentApp.getState() == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO) {
            this.mRecordsRequested = false;
            return;
        }
        super.onAllRecordsLoaded();
        if (this.mParentApp.getState() == IccCardApplicationStatus.AppState.APPSTATE_PIN || this.mParentApp.getState() == IccCardApplicationStatus.AppState.APPSTATE_PUK) {
            this.mRecordsRequested = false;
            return;
        }
        setSpnFromConfig(getOperatorNumeric());
        String operator = getOperatorNumeric();
        mtkLog("onAllRecordsLoaded operator = " + operator + ", imsi = " + MtkIccUtilsEx.getPrintableString(getIMSI(), 10));
        if (operator != null) {
            if (operator.equals("46002") || operator.equals("46007")) {
                operator = "46000";
            }
            String newName = MtkSpnOverride.getInstance().lookupOperatorName(MtkSubscriptionManager.getSubIdUsingPhoneId(this.mParentApp.getPhoneId()), operator, true, this.mContext);
            setSystemProperty("vendor.gsm.sim.operator.default-name", newName);
        }
        fetchCPHSOns();
        fetchOpl5g();
    }

    protected boolean checkCdma3gCard() {
        boolean result = MtkIccUtilsEx.checkCdma3gCard(this.mSlotId) <= 0;
        mtkLog("checkCdma3gCard result: " + result);
        return result;
    }

    private void setSystemProperty(String key, String val) {
        if ("vendor.gsm.sim.operator.default-name".equals(key)) {
            String str = this.mOldOperatorDefaultName;
            if ((str == null && val == null) || (str != null && str.equals(val))) {
                mtkLog("set PROPERTY_ICC_OPERATOR_DEFAULT_NAME same value. val:" + val);
                return;
            }
            this.mOldOperatorDefaultName = val;
        }
        setTelephonyProperty(this.mParentApp.getPhoneId(), key, val);
    }

    private void setTelephonyProperty(int phoneId, String property, String value) {
        String propVal = "";
        String[] p = null;
        String prop = SystemProperties.get(property);
        if (value == null) {
            value = "";
        }
        value.replace(',', ' ');
        if (prop != null) {
            p = prop.split(",");
        }
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            mtkLog("setTelephonyProperty: invalid phoneId=" + phoneId + " property=" + property + " value: " + value + " prop=" + prop);
            return;
        }
        for (int i = 0; i < phoneId; i++) {
            String str = "";
            if (p != null && i < p.length) {
                str = p[i];
            }
            propVal = propVal + str + ",";
        }
        String propVal2 = propVal + value;
        if (p != null) {
            for (int i2 = phoneId + 1; i2 < p.length; i2++) {
                propVal2 = propVal2 + "," + p[i2];
            }
        }
        int propValLen = propVal2.length();
        try {
            propValLen = propVal2.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
            mtkLog("setTelephonyProperty: utf-8 not supported");
        }
        if (propValLen > 91) {
            mtkLog("setTelephonyProperty: property too long phoneId=" + phoneId + " property=" + property + " value: " + value + " propVal=" + propVal2);
        } else {
            SystemProperties.set(property, propVal2);
        }
    }

    protected void setSpnFromConfig(String carrier) {
        if (TextUtils.isEmpty(getServiceProviderName()) && this.mSpnOverride.containsCarrier(carrier)) {
            this.mTelephonyManager.setSimOperatorNameForPhone(this.mParentApp.getPhoneId(), this.mSpnOverride.getSpn(carrier));
        }
    }

    protected void setVoiceMailByCountry(String spn) {
        super.setVoiceMailByCountry(spn);
        if (this.mVmConfig.containsCarrier(spn)) {
            mtkLog("setVoiceMailByCountry");
        }
    }

    public String getSpNameInEfSpn() {
        mtkLog("getSpNameInEfSpn(): " + this.mSpNameInEfSpn);
        return this.mSpNameInEfSpn;
    }

    public String isOperatorMvnoForImsi() {
        MtkSpnOverride spnOverride = MtkSpnOverride.getInstance();
        String imsiPattern = spnOverride.isOperatorMvnoForImsi(getOperatorNumeric(), getIMSI());
        String mccmnc = getOperatorNumeric();
        mtkLog("isOperatorMvnoForImsi(), imsiPattern: " + imsiPattern + ", mccmnc: " + mccmnc);
        if (imsiPattern == null || mccmnc == null) {
            return null;
        }
        String result = imsiPattern.substring(mccmnc.length(), imsiPattern.length());
        mtkLog("isOperatorMvnoForImsi(): " + result);
        return result;
    }

    public String getFirstFullNameInEfPnn() {
        if (this.mPnns == null || this.mPnns.length == 0) {
            mtkLog("getFirstFullNameInEfPnn(): empty");
            return null;
        }
        IccRecords.PlmnNetworkName opName = this.mPnns[0];
        mtkLog("getFirstFullNameInEfPnn(): first fullname: " + opName.fullName);
        if (opName.fullName != null) {
            return new String(opName.fullName);
        }
        return null;
    }

    public String isOperatorMvnoForEfPnn() {
        String MCCMNC = getOperatorNumeric();
        String PNN = getFirstFullNameInEfPnn();
        mtkLog("isOperatorMvnoForEfPnn(): mccmnc = " + MCCMNC + ", pnn = " + PNN);
        if (MtkSpnOverride.getInstance().getSpnByEfPnn(MCCMNC, PNN) != null) {
            return PNN;
        }
        return null;
    }

    public String getMvnoMatchType() {
        String IMSI = getIMSI();
        String SPN = getSpNameInEfSpn();
        String PNN = getFirstFullNameInEfPnn();
        String GID1 = getGid1();
        String MCCMNC = getOperatorNumeric();
        if (USERDEBUG) {
            mtkLog("getMvnoMatchType(): imsi = ***, mccmnc = " + MCCMNC + ", spn = " + SPN);
        } else {
            mtkLog("getMvnoMatchType(): imsi = " + IMSI + ", mccmnc = " + MCCMNC + ", spn = " + SPN);
        }
        if (MtkSpnOverride.getInstance().getSpnByEfSpn(MCCMNC, SPN) != null) {
            return "spn";
        }
        if (MtkSpnOverride.getInstance().getSpnByImsi(MCCMNC, IMSI) != null) {
            return "imsi";
        }
        if (MtkSpnOverride.getInstance().getSpnByEfPnn(MCCMNC, PNN) != null) {
            return "pnn";
        }
        if (MtkSpnOverride.getInstance().getSpnByEfGid1(MCCMNC, GID1) != null) {
            return "gid";
        }
        return "";
    }

    private class SIMBroadCastReceiver extends BroadcastReceiver {
        private SIMBroadCastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                String reasonExtra = intent.getStringExtra(DataSubConstants.EXTRA_MOBILE_DATA_ENABLE_REASON);
                int id = intent.getIntExtra("phone", 0);
                String simState = intent.getStringExtra("ss");
                MtkSIMRecords.this.mtkLog("SIM_STATE_CHANGED: phone id = " + id + ",reason = " + reasonExtra + ", simState = " + simState);
                if ("PUK".equals(reasonExtra) && id == MtkSIMRecords.this.mSlotId) {
                    String strPuk1Count = SystemProperties.get(MtkSIMRecords.SIMRECORD_PROPERTY_RIL_PUK1[MtkSIMRecords.this.mSlotId], "0");
                    MtkSIMRecords.this.mtkLog("SIM_STATE_CHANGED: strPuk1Count = " + strPuk1Count);
                    MtkSIMRecords.this.mMsisdn = "";
                    MtkSIMRecords.this.mRecordsEventsRegistrants.notifyResult(100);
                }
                if (id == MtkSIMRecords.this.mSlotId) {
                    String strPhbReady = TelephonyManager.getTelephonyProperty(MtkSIMRecords.this.mSlotId, MtkSIMRecords.SIMRECORD_PROPERTY_RIL_PHB_READY, "false");
                    MtkSIMRecords.this.mtkLog("sim state: " + simState + ", mPhbReady: " + MtkSIMRecords.this.mPhbReady + ",strPhbReady: " + strPhbReady);
                    if ("READY".equals(simState)) {
                        if (!MtkSIMRecords.this.mPhbReady && strPhbReady.equals("true")) {
                            MtkSIMRecords.this.mPhbReady = true;
                            MtkSIMRecords mtkSIMRecords = MtkSIMRecords.this;
                            mtkSIMRecords.broadcastPhbStateChangedIntent(mtkSIMRecords.mPhbReady, false);
                        } else if (true == MtkSIMRecords.this.mPhbWaitSub && strPhbReady.equals("true")) {
                            MtkSIMRecords.this.mtkLog("mPhbWaitSub is " + MtkSIMRecords.this.mPhbWaitSub + ", broadcast if need");
                            MtkSIMRecords.this.mPhbWaitSub = false;
                            MtkSIMRecords mtkSIMRecords2 = MtkSIMRecords.this;
                            mtkSIMRecords2.broadcastPhbStateChangedIntent(mtkSIMRecords2.mPhbReady, false);
                        }
                    }
                }
            }
        }
    }

    public IccServiceInfo.IccServiceStatus getSIMServiceStatus(IccServiceInfo.IccService enService) {
        int nbit;
        int nbit2;
        int nServiceNum = enService.getIndex();
        IccServiceInfo.IccServiceStatus simServiceStatus = IccServiceInfo.IccServiceStatus.UNKNOWN;
        mtkLog("getSIMServiceStatus enService is " + enService + " Service Index is " + nServiceNum);
        if (nServiceNum >= 0 && nServiceNum < IccServiceInfo.IccService.UNSUPPORTED_SERVICE.getIndex() && this.mEfSST != null) {
            if (this.mParentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM) {
                int nUSTIndex = usimServiceNumber[nServiceNum];
                if (nUSTIndex <= 0) {
                    simServiceStatus = IccServiceInfo.IccServiceStatus.NOT_EXIST_IN_USIM;
                } else {
                    int nbyte = nUSTIndex / 8;
                    int nbit3 = nUSTIndex % 8;
                    if (nbit3 == 0) {
                        nbit2 = 7;
                        nbyte--;
                    } else {
                        nbit2 = nbit3 - 1;
                    }
                    mtkLog("getSIMServiceStatus USIM nbyte: " + nbyte + " nbit: " + nbit2);
                    byte[] bArr = this.mEfSST;
                    simServiceStatus = (bArr.length <= nbyte || (bArr[nbyte] & (1 << nbit2)) <= 0) ? IccServiceInfo.IccServiceStatus.INACTIVATED : IccServiceInfo.IccServiceStatus.ACTIVATED;
                }
            } else {
                int nSSTIndex = simServiceNumber[nServiceNum];
                if (nSSTIndex <= 0) {
                    simServiceStatus = IccServiceInfo.IccServiceStatus.NOT_EXIST_IN_SIM;
                } else {
                    int nbyte2 = nSSTIndex / 4;
                    int nbit4 = nSSTIndex % 4;
                    if (nbit4 == 0) {
                        nbit = 3;
                        nbyte2--;
                    } else {
                        nbit = nbit4 - 1;
                    }
                    int nMask = 2 << (nbit * 2);
                    mtkLog("getSIMServiceStatus SIM nbyte: " + nbyte2 + " nbit: " + nbit + " nMask: " + nMask);
                    byte[] bArr2 = this.mEfSST;
                    simServiceStatus = (bArr2.length <= nbyte2 || (bArr2[nbyte2] & nMask) != nMask) ? IccServiceInfo.IccServiceStatus.INACTIVATED : IccServiceInfo.IccServiceStatus.ACTIVATED;
                }
            }
        }
        mtkLog("getSIMServiceStatus simServiceStatus: " + simServiceStatus);
        return simServiceStatus;
    }

    private void parseEFopl5g(ArrayList messages) {
        int count = messages.size();
        mtkLog("parseEFopl5g(): opl has " + count + " records");
        this.m5gsOperatorList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            byte[] data = (byte[]) messages.get(i);
            String sPlmn = MtkIccUtils.parsePlmnToStringForEfOpl(data, 0, 3);
            byte[] minLac = {data[3], data[4], data[5]};
            int minLAC = Integer.parseInt(IccUtils.bytesToHexString(minLac), 16);
            byte[] maxLac = {data[6], data[7], data[8]};
            int maxLAC = Integer.parseInt(IccUtils.bytesToHexString(maxLac), 16);
            byte[] pnnRecordIndex = {data[9]};
            int pnnIndex = Integer.parseInt(IccUtils.bytesToHexString(pnnRecordIndex), 16);
            IccRecords.OperatorPlmnInfo oplRec = new IccRecords.OperatorPlmnInfo(sPlmn, minLAC, maxLAC, pnnIndex);
            mtkLog("parseEFopl5g(): record=" + i + " content=" + IccUtils.bytesToHexString(data) + " sPlmn=" + oplRec.plmnNumericPattern + " nMinLAC=" + oplRec.lacTacStart + " nMaxLAC=" + oplRec.lacTacEnd + " nPnnIndex=" + oplRec.pnnRecordId);
            this.m5gsOperatorList.add(oplRec);
        }
    }

    private void fetchCPHSOns() {
        mtkLog("fetchCPHSOns()");
        this.cphsOnsl = null;
        this.cphsOnss = null;
        this.mFh.loadEFTransparent(28436, obtainMessage(1009));
        this.mFh.loadEFTransparent(28440, obtainMessage(1010));
    }

    private boolean isMatchingPlmnForEfOpl(String simPlmn, String bcchPlmn) {
        if (simPlmn == null || simPlmn.equals("") || bcchPlmn == null || bcchPlmn.equals("")) {
            return false;
        }
        mtkLog("isMatchingPlmnForEfOpl(): simPlmn = " + simPlmn + ", bcchPlmn = " + bcchPlmn);
        int simPlmnLen = simPlmn.length();
        int bcchPlmnLen = bcchPlmn.length();
        if (simPlmnLen < 5 || bcchPlmnLen < 5) {
            return false;
        }
        for (int i = 0; i < 5; i++) {
            if (simPlmn.charAt(i) != 'd' && simPlmn.charAt(i) != 'D' && simPlmn.charAt(i) != bcchPlmn.charAt(i)) {
                return false;
            }
        }
        if (simPlmnLen == 6 && bcchPlmnLen == 6) {
            if (simPlmn.charAt(5) != 'd' && simPlmn.charAt(5) != 'D' && simPlmn.charAt(5) != bcchPlmn.charAt(5)) {
                return false;
            }
            return true;
        }
        if (bcchPlmnLen == 6 && bcchPlmn.charAt(5) != '0' && bcchPlmn.charAt(5) != 'd' && bcchPlmn.charAt(5) != 'D') {
            return false;
        }
        if (simPlmnLen == 6 && simPlmn.charAt(5) != '0' && simPlmn.charAt(5) != 'd' && simPlmn.charAt(5) != 'D') {
            return false;
        }
        return true;
    }

    private boolean isPlmnEqualsSimNumeric(String plmn) {
        String mccmnc = getOperatorNumeric();
        if (plmn == null) {
            return false;
        }
        if (mccmnc == null || mccmnc.equals("")) {
            mtkLog("isPlmnEqualsSimNumeric: getOperatorNumeric error: " + mccmnc);
            return false;
        }
        if (plmn.equals(mccmnc)) {
            return true;
        }
        return plmn.length() == 5 && mccmnc.length() == 6 && plmn.equals(mccmnc.substring(0, 5));
    }

    public String get5GEonsIfExist(String plmn, int nLac, boolean bLongNameRequired) {
        StringBuilder lac_sb = new StringBuilder(Integer.toHexString(nLac));
        if (lac_sb.length() == 1 || lac_sb.length() == 2) {
            lac_sb.setCharAt(0, '*');
        } else {
            for (int i = 0; i < lac_sb.length() / 2; i++) {
                lac_sb.setCharAt(i, '*');
            }
        }
        mtkLog("EONS get5GEonsIfExist: plmn is " + plmn + " nLac is " + lac_sb.toString() + " bLongNameRequired: " + bLongNameRequired);
        if (plmn == null || this.mPnns == null || this.mPnns.length == 0) {
            return null;
        }
        int nPnnIndex = -1;
        boolean isHPLMN = isPlmnEqualsSimNumeric(plmn);
        if (this.m5gsOperatorList == null) {
            if (isHPLMN) {
                mtkLog("get5GEonsIfExist: Plmn is HPLMN, return PNN's first record");
                nPnnIndex = 1;
            } else {
                mtkLog("get5GEonsIfExist: Plmn is not HPLMN and no m5gsOperatorList, return null");
                return null;
            }
        } else {
            for (int i2 = 0; i2 < this.m5gsOperatorList.size(); i2++) {
                IccRecords.OperatorPlmnInfo oplRec = this.m5gsOperatorList.get(i2);
                if (isMatchingPlmnForEfOpl(oplRec.plmnNumericPattern, plmn) && ((oplRec.lacTacStart == 0 && oplRec.lacTacEnd == 16777214) || (oplRec.lacTacStart <= nLac && oplRec.lacTacEnd >= nLac))) {
                    mtkLog("get5GEonsIfExist: find it in EF_5GOPL");
                    if (oplRec.pnnRecordId == 0) {
                        mtkLog("get5GEonsIfExist: oplRec.nPnnIndex is 0, from other sources");
                        return null;
                    }
                    nPnnIndex = oplRec.pnnRecordId;
                }
            }
        }
        if (nPnnIndex == -1 && isHPLMN && this.m5gsOperatorList.size() == 1) {
            mtkLog("get5GEonsIfExist: not find it in EF_5GOPL, but Plmn is HPLMN, return PNN's first record");
            nPnnIndex = 1;
        } else if (nPnnIndex > 1 && nPnnIndex > this.mPnns.length && isHPLMN) {
            mtkLog("get5GEonsIfExist: find it in EF_5GOPL, but index in EF_5GOPL > EF_PNN list length & Plmn is HPLMN, return PNN's first record");
            nPnnIndex = 1;
        } else if (nPnnIndex > 1 && nPnnIndex > this.mPnns.length && !isHPLMN) {
            mtkLog("get5GEonsIfExist: find it in EF_5GOPL, but index in EF_5GOPL > EF_PNN list length & Plmn is not HPLMN, return PNN's first record");
            nPnnIndex = -1;
        }
        String sEons = null;
        if (nPnnIndex >= 1) {
            IccRecords.PlmnNetworkName opName = this.mPnns[nPnnIndex - 1];
            if (bLongNameRequired) {
                if (opName.fullName != null) {
                    sEons = new String(opName.fullName);
                } else if (opName.shortName != null) {
                    sEons = new String(opName.shortName);
                }
            } else if (!bLongNameRequired) {
                if (opName.shortName != null) {
                    sEons = new String(opName.shortName);
                } else if (opName.fullName != null) {
                    sEons = new String(opName.fullName);
                }
            }
        }
        mtkLog("get5GEonsIfExist: sEons is " + sEons);
        return sEons;
    }

    public String getEonsIfExist(String plmn, int nLac, boolean bLongNameRequired) {
        StringBuilder lac_sb = new StringBuilder(Integer.toHexString(nLac));
        if (lac_sb.length() == 1 || lac_sb.length() == 2) {
            lac_sb.setCharAt(0, '*');
        } else {
            for (int i = 0; i < lac_sb.length() / 2; i++) {
                lac_sb.setCharAt(i, '*');
            }
        }
        mtkLog("EONS getEonsIfExist: plmn is " + plmn + " nLac is " + lac_sb.toString() + " bLongNameRequired: " + bLongNameRequired);
        if (plmn == null || this.mPnns == null || this.mPnns.length == 0) {
            return null;
        }
        int nPnnIndex = -1;
        boolean isHPLMN = isPlmnEqualsSimNumeric(plmn);
        if (this.mOpl == null) {
            if (isHPLMN) {
                mtkLog("getEonsIfExist: Plmn is HPLMN, return PNN's first record");
                nPnnIndex = 1;
            } else {
                mtkLog("getEonsIfExist: Plmn is not HPLMN and no mOpl, return null");
                return null;
            }
        } else {
            for (int i2 = 0; i2 < this.mOpl.length; i2++) {
                IccRecords.OperatorPlmnInfo oplRec = this.mOpl[i2];
                if (isMatchingPlmnForEfOpl(oplRec.plmnNumericPattern, plmn) && ((oplRec.lacTacStart == 0 && oplRec.lacTacEnd == 65534) || (oplRec.lacTacStart <= nLac && oplRec.lacTacEnd >= nLac))) {
                    mtkLog("getEonsIfExist: find it in EF_OPL");
                    if (oplRec.pnnRecordId == 0) {
                        mtkLog("getEonsIfExist: oplRec.nPnnIndex is 0, from other sources");
                        return null;
                    }
                    nPnnIndex = oplRec.pnnRecordId;
                }
            }
        }
        if (nPnnIndex == -1 && isHPLMN && this.mOpl.length == 1) {
            mtkLog("getEonsIfExist: not find it in EF_OPL, but Plmn is HPLMN, return PNN's first record");
            nPnnIndex = 1;
        } else if (nPnnIndex > 1 && nPnnIndex > this.mPnns.length && isHPLMN) {
            mtkLog("getEonsIfExist: find it in EF_OPL, but index in EF_OPL > EF_PNN list length & Plmn is HPLMN, return PNN's first record");
            nPnnIndex = 1;
        } else if (nPnnIndex > 1 && nPnnIndex > this.mPnns.length && !isHPLMN) {
            mtkLog("getEonsIfExist: find it in EF_OPL, but index in EF_OPL > EF_PNN list length & Plmn is not HPLMN, return PNN's first record");
            nPnnIndex = -1;
        }
        String sEons = null;
        if (nPnnIndex >= 1) {
            IccRecords.PlmnNetworkName opName = this.mPnns[nPnnIndex - 1];
            if (bLongNameRequired) {
                if (opName.fullName != null) {
                    sEons = new String(opName.fullName);
                } else if (opName.shortName != null) {
                    sEons = new String(opName.shortName);
                }
            } else if (!bLongNameRequired) {
                if (opName.shortName != null) {
                    sEons = new String(opName.shortName);
                } else if (opName.fullName != null) {
                    sEons = new String(opName.fullName);
                }
            }
        }
        mtkLog("getEonsIfExist: sEons is " + sEons);
        return sEons;
    }

    public int getMncLength() {
        mtkLog("mncLength = " + this.mMncLength);
        return this.mMncLength;
    }

    private class RebootClickListener implements DialogInterface.OnClickListener {
        private RebootClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            MtkSIMRecords.this.mtkLog("[RSU-SIMLOCK] Unlock Phone onClick");
            PowerManager pm = (PowerManager) MtkSIMRecords.this.mContext.getSystemService("power");
            pm.reboot("Unlock state changed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastPhbStateChangedIntent(boolean isReady, boolean isForceSendIntent) {
        Phone phone = this.mPhone;
        if (phone != null && phone.getPhoneType() != 1 && (!this.isDispose || isReady)) {
            this.mPendingPhbNotify = true;
            mtkLog("broadcastPhbStateChangedIntent, No active Phone, will notfiy when dispose");
            return;
        }
        mtkLog("broadcastPhbStateChangedIntent, mPhbReady " + this.mPhbReady + ", " + this.mSubId);
        if (isReady) {
            this.mSubId = MtkSubscriptionManager.getSubIdUsingPhoneId(this.mSlotId);
            int curSimState = TelephonyManager.getSimStateForSlotIndex(this.mSlotId);
            if (this.mSubId <= 0 || curSimState == 0) {
                mtkLog("broadcastPhbStateChangedIntent, mSubId " + this.mSubId + ", sim state " + curSimState);
                this.mPhbWaitSub = true;
                return;
            }
        } else {
            if (isForceSendIntent && this.mPhbReady) {
                this.mSubId = MtkSubscriptionManager.getSubIdUsingPhoneId(this.mSlotId);
            }
            if (this.mSubId <= 0) {
                mtkLog("broadcastPhbStateChangedIntent, isReady == false and mSubId <= 0");
                return;
            }
        }
        UserManager userManager = (UserManager) this.mContext.getSystemService(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
        boolean isUnlock = userManager.isUserUnlocked();
        if (!SystemProperties.get("sys.boot_completed").equals(RadioCapabilitySwitchUtil.IMSI_READY) || !isUnlock) {
            mtkLog("broadcastPhbStateChangedIntent, boot not completed, isUnlock:" + isUnlock);
            this.mPendingPhbNotify = true;
            return;
        }
        Intent intent = new Intent("mediatek.intent.action.PHB_STATE_CHANGED");
        BroadcastOptions options = BroadcastOptions.makeBasic();
        options.setDeliveryGroupPolicy(0);
        intent.putExtra("ready", isReady);
        intent.putExtra("subscription", this.mSubId);
        mtkLog("Broadcasting intent ACTION_PHB_STATE_CHANGED " + isReady + " sub id " + this.mSubId + " phoneId " + this.mParentApp.getPhoneId());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, null, options.toBundle());
        Intent bootIntent = new Intent("mediatek.intent.action.PHB_STATE_CHANGED");
        bootIntent.putExtra("ready", isReady);
        bootIntent.putExtra("subscription", this.mSubId);
        bootIntent.setPackage("com.mediatek.simprocessor");
        mtkLog("Broadcasting ACTION_PHB_STATE_CHANGED to package: simprocessor");
        this.mContext.sendBroadcastAsUser(bootIntent, UserHandle.ALL);
        if (!isReady) {
            this.mSubId = -1;
        }
    }

    public boolean isPhbReady() {
        String strCurSimState = "";
        mtkLog("phbReady(): cached mPhbReady = " + (this.mPhbReady ? "true" : "false"));
        if (this.mParentApp == null || this.mPhone == null) {
            return false;
        }
        String strPhbReady = TelephonyManager.getTelephonyProperty(this.mSlotId, SIMRECORD_PROPERTY_RIL_PHB_READY, "false");
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            mtkLog("phbReady(): strPhbReady = " + strPhbReady);
            return strPhbReady.equals("true");
        }
        String strAllSimState = SystemProperties.get("gsm.sim.state");
        if (strAllSimState != null && strAllSimState.length() > 0) {
            String[] values = strAllSimState.split(",");
            int i = this.mSlotId;
            if (i >= 0 && i < values.length && values[i] != null) {
                strCurSimState = values[i];
            }
        }
        boolean isSimLocked = strCurSimState.equals("NETWORK_LOCKED") || strCurSimState.equals("PIN_REQUIRED");
        mtkLog("phbReady(): strPhbReady = " + strPhbReady + ", strAllSimState = " + strAllSimState);
        return strPhbReady.equals("true") && !isSimLocked;
    }

    public void setPhbReady(boolean isReady) {
        mtkLog("setPhbReady(): isReady = " + (isReady ? "true" : "false"));
        if (this.mPhbReady != isReady && this.mCi != null) {
            this.mPhbReady = isReady;
            if (isReady) {
                this.mCi.setPhonebookReady(1, null);
            } else if (!isReady) {
                this.mCi.setPhonebookReady(0, null);
            }
            broadcastPhbStateChangedIntent(this.mPhbReady, false);
        }
    }

    private class PhbBroadCastReceiver extends BroadcastReceiver {
        private PhbBroadCastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if (MtkSIMRecords.this.mPhbWaitSub && action.equals("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED")) {
                MtkSIMRecords.this.mtkLog("SubBroadCastReceiver receive ACTION_SUBINFO_RECORD_UPDATED");
                MtkSIMRecords.this.mPhbWaitSub = false;
                MtkSIMRecords mtkSIMRecords = MtkSIMRecords.this;
                mtkSIMRecords.broadcastPhbStateChangedIntent(mtkSIMRecords.mPhbReady, false);
                return;
            }
            if (action.equals("android.intent.action.RADIO_TECHNOLOGY")) {
                int phoneid = intent.getIntExtra("phone", -1);
                MtkSIMRecords.this.mtkLog("[ACTION_RADIO_TECHNOLOGY_CHANGED] phoneid : " + phoneid);
                if (MtkSIMRecords.this.mParentApp != null && MtkSIMRecords.this.mParentApp.getPhoneId() == phoneid) {
                    String activePhoneName = intent.getStringExtra("phoneName");
                    int subid = intent.getIntExtra("subscription", -1);
                    boolean isUpbDone = ((MtkAdnRecordCache) MtkSIMRecords.this.mAdnCache).getUpbDone() == 1;
                    MtkSIMRecords.this.mtkLog("[ACTION_RADIO_TECHNOLOGY_CHANGED] activePhoneName : " + activePhoneName + " | subid : " + subid + ", isUpbDone: " + isUpbDone);
                    if (!isUpbDone && !"CDMA".equals(activePhoneName)) {
                        MtkSIMRecords.this.broadcastPhbStateChangedIntent(false, true);
                        MtkSIMRecords mtkSIMRecords2 = MtkSIMRecords.this;
                        mtkSIMRecords2.sendMessageDelayed(mtkSIMRecords2.obtainMessage(MtkSIMRecords.EVENT_DELAYED_SEND_PHB_CHANGE), MtkRuimRecords.PHB_DELAY_SEND_TIME);
                        MtkSIMRecords.this.mAdnCache.reset();
                        return;
                    }
                    return;
                }
                return;
            }
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                MtkSIMRecords.this.mtkLog("[onReceive] ACTION_BOOT_COMPLETED mPendingPhbNotify : " + MtkSIMRecords.this.mPendingPhbNotify);
                if (MtkSIMRecords.this.mPendingPhbNotify) {
                    MtkSIMRecords mtkSIMRecords3 = MtkSIMRecords.this;
                    mtkSIMRecords3.broadcastPhbStateChangedIntent(mtkSIMRecords3.isPhbReady(), false);
                    MtkSIMRecords.this.mPendingPhbNotify = false;
                }
            }
        }
    }

    private void updatePHBStatus(int status, boolean isSimLocked) {
        boolean simLockedState;
        mtkLog("[PHBStatus] status : " + status + " | isSimLocked : " + isSimLocked + " | mPhbReady : " + this.mPhbReady);
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            simLockedState = false;
        } else {
            simLockedState = isSimLocked;
        }
        if (status == 1) {
            if (!simLockedState) {
                if (!this.mPhbReady) {
                    this.mPhbReady = true;
                    broadcastPhbStateChangedIntent(true, false);
                    return;
                }
                return;
            }
            mtkLog("phb ready but sim is not ready.");
            return;
        }
        if (status == 0 && this.mPhbReady) {
            this.mAdnCache.reset();
            this.mPhbReady = false;
            broadcastPhbStateChangedIntent(false, false);
        }
    }

    protected boolean shallIgnoreMessage(Message msg) {
        if (msg.what == 90 || msg.what == 41) {
            mtkLoge("Received message " + msg + "[" + msg.what + "]  while being destroyed. Keep going!");
            return false;
        }
        mtkLoge("Received message " + msg + "[" + msg.what + "]  while being destroyed. Ignoring.");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mtkLog(String s) {
        Rlog.d(LOG_TAG_EX, "[SIMRecords] " + s + " (slot " + this.mSlotId + ")");
    }

    private void mtkLoge(String s) {
        Rlog.e(LOG_TAG_EX, "[SIMRecords] " + s + " (slot " + this.mSlotId + ")");
    }

    private void mtkLogw(String s, Throwable tr) {
        Rlog.w(LOG_TAG_EX, "[SIMRecords] " + s + " (slot " + this.mSlotId + ")", tr);
    }

    private void mtkLogv(String s) {
        Rlog.v(LOG_TAG_EX, "[SIMRecords] " + s + " (slot " + this.mSlotId + ")");
    }

    protected void log(String s) {
        Rlog.d("SIMRecords", "[SIMRecords] " + s + " (slot " + this.mSlotId + ")");
    }

    protected void loge(String s) {
        Rlog.e("SIMRecords", "[SIMRecords] " + s + " (slot " + this.mSlotId + ")");
    }

    protected void logw(String s, Throwable tr) {
        Rlog.w("SIMRecords", "[SIMRecords] " + s + " (slot " + this.mSlotId + ")", tr);
    }

    protected void logv(String s) {
        Rlog.v("SIMRecords", "[SIMRecords] " + s + " (slot " + this.mSlotId + ")");
    }

    protected void onLocked() {
        this.mRecordsRequested = false;
        this.mLoaded.set(false);
        if (this.mLockedRecordsReqReason != 0) {
            this.mRecordsToLoad++;
            onRecordLoaded();
        } else {
            super.onLocked();
        }
    }

    protected void updateOperatorPlmn() {
        String imsi = getIMSI();
        if (imsi != null) {
            if ((this.mMncLength == -1 || this.mMncLength == 0 || this.mMncLength == 2) && imsi.length() >= 6) {
                String mccmncCode = imsi.substring(0, 6);
                String[] strArr = MCCMNC_CODES_HAVING_3DIGITS_MNC;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String mccmnc = strArr[i];
                    if (!mccmnc.equals(mccmncCode)) {
                        i++;
                    } else {
                        this.mMncLength = 3;
                        mtkLog("IMSI: setting1 mMncLength=" + this.mMncLength);
                        break;
                    }
                }
            }
            if (this.mMncLength == -1 || this.mMncLength == 0) {
                try {
                    int mcc = Integer.parseInt(imsi.substring(0, 3));
                    this.mMncLength = MccTable.smallestDigitsMccForMnc(mcc);
                    mtkLog("setting2 mMncLength=" + this.mMncLength);
                } catch (NumberFormatException e) {
                    mtkLoge("Corrupt IMSI! setting3 mMncLength=" + this.mMncLength);
                }
            }
            if (this.mMncLength != 0 && this.mMncLength != -1 && imsi.length() >= this.mMncLength + 3) {
                mtkLog("update mccmnc=" + imsi.substring(0, this.mMncLength + 3));
                String numeric = imsi.substring(0, this.mMncLength + 3);
                if (!TextUtils.isEmpty(numeric) && !this.mOldMccMnc.equals(numeric)) {
                    this.mOldMccMnc = numeric;
                    MccTable.updateMccMncConfiguration(this.mContext, this.mOldMccMnc);
                } else {
                    mtkLog("Do not update configuration if mcc mnc no change.");
                }
            }
        }
    }

    private void fetchOpl5g() {
        mtkLog("fetchOpl5g...");
        this.mFh.loadEFLinearFixedAll(MtkIccConstants.EF_OPL5G, "3F007FFF5FC0", obtainMessage(EVENT_GET_ALL_OPL5G_DONE));
    }
}
