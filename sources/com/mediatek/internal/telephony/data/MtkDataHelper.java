package com.mediatek.internal.telephony.data;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.LinkProperties;
import android.net.NetworkStats;
import android.net.netstats.provider.NetworkStatsProvider;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.TelephonyDevController;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.uicc.IccRecords;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.MtkGsmCdmaCallTracker;
import com.mediatek.internal.telephony.MtkHardwareConfig;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.SmartDataSwitchAssistant;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataHelper {
    public static final int CALL_STATE_IN_CALL = 1;
    public static final int CALL_STATE_NO_CALL = 0;
    private static final boolean DBG = true;
    private static final int DEFAULT_VALUE_DSDA = 420;
    private static final int DELAY_DSDA_EVENT_TIME_MS = 1000;
    private static final int DRDSDA_STATUS_NOT_SUPPORT = 2;
    private static final int DSDA_STATUS_DIVISOR = 100;
    private static final int EVENT_CALL_ADDITIONAL_INFO = 70;
    private static final int EVENT_DSDA_STATE_CHANGED = 50;
    private static final int EVENT_HANDOVER_START = 90;
    private static final int EVENT_ID_INTVL = 10;
    private static final int EVENT_INTERNAL_DSDA_STATE_CHANGED = 100;
    private static final int EVENT_MOBILE_DATA_USAGE = 80;
    private static final int EVENT_NO_CS_CALL_AFTER_SRVCC = 40;
    private static final int EVENT_RADIO_UNAVAILABLE = 10;
    private static final int EVENT_VOICE_CALL_ENDED = 30;
    private static final int EVENT_VOICE_CALL_STARTED = 20;
    private static final int EVENT_VOICE_CALL_STATE_CHANGED = 60;
    private static final String LOG_TAG = "DataHelper";
    private static final int MAX_COUNT_PHONE_INCALL = 2;
    private static final int MT_CALL_MISSED = 2;
    private static final int MT_CALL_REJECTED = 1;
    private static final int MT_CALL_RQ = 4;
    private static final int STATE_DSDA_NOT_POSSIBLE = 4;
    private static final int STATE_DSDA_ONGOING = 2;
    private static final int STATE_DSDA_POSSIBLE = 3;
    private Context mContext;
    private Handler mDataUsageHandler;
    private MobileDataUsageProvider[] mDataUsageProvider;
    private boolean[] mGwsdDualSimStatusArray;
    private HandlerThread mHandlerThread;
    private volatile NetworkStats[] mMobileDataUsage;
    private int mPhoneNum;
    private Phone[] mPhones;
    private static final boolean VDBG = SystemProperties.get("ro.build.type").equals("eng");
    private static MtkDataHelper sMtkDataHelper = null;
    private static final String[] PROPERTY_RIL_TEST_SIM = {"vendor.gsm.sim.ril.testsim", "vendor.gsm.sim.ril.testsim.2", "vendor.gsm.sim.ril.testsim.3", "vendor.gsm.sim.ril.testsim.4"};
    private static TelephonyDevController mTelDevController = TelephonyDevController.getInstance();
    private ArrayList<Integer> mCallingPhoneIdList = new ArrayList<>();
    private int mDsdaMode = DEFAULT_VALUE_DSDA;
    private int mCurrentDsdaMode = DEFAULT_VALUE_DSDA;
    private Call.SrvccState mSrvccState = Call.SrvccState.NONE;
    private Handler mRspHandler = new Handler() { // from class: com.mediatek.internal.telephony.data.MtkDataHelper.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            SmartDataSwitchAssistant mSmartDataSwitchAssistant;
            int phoneId = msg.what % 10;
            int eventId = msg.what - phoneId;
            switch (eventId) {
                case 10:
                    MtkDataHelper.logd("EVENT_PHONE" + phoneId + "_RADIO_UNAVAILABLE");
                    MtkDataHelper.this.mDsdaMode = MtkDataHelper.DEFAULT_VALUE_DSDA;
                    MtkDataHelper.this.mGwsdDualSimStatusArray[phoneId] = false;
                    break;
                case 20:
                    MtkDataHelper.this.registerForImsRadioTechChange();
                    MtkDataHelper.this.mCallingPhoneIdList.remove(new Integer(phoneId));
                    MtkDataHelper.this.mCallingPhoneIdList.add(Integer.valueOf(phoneId));
                    MtkDataHelper.logd("Voice Call Started, phoneId=" + phoneId + ",mSrvccState=" + MtkDataHelper.this.mSrvccState + ", callingPhoneSize=" + MtkDataHelper.this.mCallingPhoneIdList.size());
                    MtkDataHelper.this.onVoiceCallEvent(phoneId, false);
                    break;
                case 30:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar != null && ar.result != null) {
                        MtkDataHelper.this.mSrvccState = (Call.SrvccState) ar.result;
                    } else {
                        MtkDataHelper.this.mSrvccState = Call.SrvccState.NONE;
                    }
                    MtkDataHelper.logd("mSrvccState = " + MtkDataHelper.this.mSrvccState);
                    if (!MtkDataHelper.this.isInSRVCC()) {
                        MtkDataHelper.this.mCallingPhoneIdList.remove(new Integer(phoneId));
                        MtkDataHelper.logd("Voice Call Ended, phoneId = " + phoneId + ", callingPhoneSize=" + MtkDataHelper.this.mCallingPhoneIdList.size());
                        MtkDataHelper.this.onVoiceCallEvent(phoneId, MtkDataHelper.DBG);
                    }
                    break;
                case 40:
                    MtkDataHelper.logd("Got 'no CS calls after SRVCC' notification, tunnel it to VOICE_CALL_END");
                    MtkDataHelper.this.mSrvccState = Call.SrvccState.NONE;
                    MtkPhoneSwitcher phoneSwitcher = MtkPhoneSwitcher.getInstance();
                    if (phoneSwitcher != null) {
                        phoneSwitcher.sendEmptyMessage(109);
                    }
                    MtkDataHelper.this.mCallingPhoneIdList.remove(new Integer(phoneId));
                    MtkDataHelper.logd("Voice Call Ended(no cs), phoneId = " + phoneId + ", callingPhoneSize=" + MtkDataHelper.this.mCallingPhoneIdList.size());
                    MtkDataHelper.this.onVoiceCallEvent(phoneId, MtkDataHelper.DBG);
                    break;
                case 50:
                    AsyncResult ar2 = (AsyncResult) msg.obj;
                    if (ar2 != null && ar2.result != null) {
                        MtkDataHelper.this.mCurrentDsdaMode = ((Integer) ar2.result).intValue();
                    }
                    if (!hasMessages(100)) {
                        sendMessageDelayed(obtainMessage(100), 1000L);
                    }
                    break;
                case 60:
                    if (MtkDataHelper.this.getCallState() == 1) {
                        MtkDataHelper.this.onVoiceCallEvent(phoneId, false);
                    }
                    break;
                case 70:
                    String[] callAdditionalInfo = (String[]) ((AsyncResult) msg.obj).result;
                    int type = Integer.parseInt(callAdditionalInfo[0]);
                    if (type == 4) {
                        int mtCallRq = Integer.parseInt(callAdditionalInfo[1]);
                        MtkDataHelper.this.mGwsdDualSimStatusArray[phoneId] = mtCallRq == 1;
                        MtkDataHelper.logd("MT_CALL_RQ, phoneId= " + phoneId + ", mtCallRq = " + mtCallRq + ", mGwsdDualSimStatus = " + MtkDataHelper.this.mGwsdDualSimStatusArray[phoneId]);
                    }
                    break;
                case 80:
                    AsyncResult ar3 = (AsyncResult) msg.obj;
                    if (ar3.exception == null && ar3.result != null) {
                        MtkDataHelper.this.updateMobileDataUsage(ar3, phoneId);
                        break;
                    }
                    break;
                case MtkDataHelper.EVENT_HANDOVER_START /* 90 */:
                case 100:
                    int dsdaMode = MtkDataHelper.this.getInternalDsdaMode();
                    MtkDataHelper mtkDataHelper = MtkDataHelper.this;
                    mtkDataHelper.mDsdaMode = mtkDataHelper.mCurrentDsdaMode;
                    boolean dsdaChanged = dsdaMode != MtkDataHelper.this.getInternalDsdaMode();
                    MtkDataHelper.logd("mDsdaMode=" + MtkDataHelper.this.mDsdaMode + " changed=" + dsdaChanged);
                    if (eventId == MtkDataHelper.EVENT_HANDOVER_START) {
                        MtkDataHelper.logd("handover start event receive: " + phoneId);
                    }
                    if (dsdaChanged && (mSmartDataSwitchAssistant = SmartDataSwitchAssistant.getInstance()) != null) {
                        mSmartDataSwitchAssistant.onDsdaStateChanged();
                    }
                    if (MtkDataHelper.this.getCallState() == 1) {
                        if (eventId == MtkDataHelper.EVENT_HANDOVER_START || (dsdaChanged && eventId == 100)) {
                            for (int i = 0; i < MtkDataHelper.this.mPhoneNum; i++) {
                                MtkDataNetworkController dnc = (MtkDataNetworkController) MtkDataHelper.this.mPhones[i].getDataNetworkController();
                                if (dnc != null) {
                                    dnc.onDsdaStateChanged();
                                }
                            }
                        }
                    }
                    break;
                default:
                    MtkDataHelper.logd("Unhandled message with number: " + msg.what);
                    break;
            }
        }
    };

    public class MobileDataUsageProvider extends NetworkStatsProvider {
        private int mPhoneId;
        private int mToken = 0;
        private NetworkStats mIfaceSnapshot = new NetworkStats(0, 0);

        public MobileDataUsageProvider(int phoneId) {
            this.mPhoneId = 0;
            this.mPhoneId = phoneId;
        }

        public void onRequestStatsUpdate(final int token) {
            if (MtkDataHelper.this.mDataUsageHandler != null) {
                MtkDataHelper.this.mDataUsageHandler.post(new Runnable() { // from class: com.mediatek.internal.telephony.data.MtkDataHelper$MobileDataUsageProvider$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onRequestStatsUpdate$0(token);
                    }
                });
            }
            this.mToken = token;
        }

        public void onSetLimit(String iface, long quotaBytes) {
        }

        public void onSetAlert(long quotaBytes) {
        }

        /* JADX INFO: renamed from: pushMobileDataUsageStats, reason: merged with bridge method [inline-methods] */
        public void lambda$onRequestStatsUpdate$0(int token) {
            if (MtkDataHelper.this.mMobileDataUsage[this.mPhoneId] == null) {
                MtkDataHelper.loge("mMobileDataUsage is null");
                return;
            }
            NetworkStats ifaceDiff = MtkDataHelper.this.mMobileDataUsage[this.mPhoneId].subtract(this.mIfaceSnapshot);
            try {
                notifyStatsUpdated(token, ifaceDiff, null);
                this.mIfaceSnapshot = this.mIfaceSnapshot.add(ifaceDiff);
            } catch (RuntimeException e) {
                MtkDataHelper.loge("Cannot report network stats " + e);
            }
        }
    }

    protected MtkDataHelper(Context context, Phone[] phones) {
        this.mHandlerThread = null;
        this.mDataUsageHandler = null;
        this.mContext = context;
        this.mPhones = phones;
        int length = phones.length;
        this.mPhoneNum = length;
        this.mGwsdDualSimStatusArray = new boolean[length];
        this.mMobileDataUsage = new NetworkStats[length];
        this.mDataUsageProvider = new MobileDataUsageProvider[this.mPhoneNum];
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mGwsdDualSimStatusArray[i] = false;
            this.mMobileDataUsage[i] = new NetworkStats(SystemClock.elapsedRealtime(), 1);
        }
        HandlerThread handlerThread = new HandlerThread("DataUsage");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mDataUsageHandler = new Handler(this.mHandlerThread.getLooper());
        registerEvents();
    }

    public void dispose() {
        logd("MtkDataHelper.dispose");
        unregisterEvents();
    }

    public void updatePhones(Phone[] phones) {
        logd("updatePhones: prev=" + this.mPhoneNum + ", current=" + phones.length);
        if (this.mPhoneNum > phones.length) {
            return;
        }
        int prevPhoneNum = this.mPhoneNum;
        this.mPhones = phones;
        int length = phones.length;
        this.mPhoneNum = length;
        this.mGwsdDualSimStatusArray = new boolean[length];
        this.mMobileDataUsage = (NetworkStats[]) Arrays.copyOf(this.mMobileDataUsage, this.mPhones.length);
        this.mDataUsageProvider = (MobileDataUsageProvider[]) Arrays.copyOf(this.mDataUsageProvider, this.mPhones.length);
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mGwsdDualSimStatusArray[i] = false;
        }
        for (int i2 = prevPhoneNum; i2 < this.mPhoneNum; i2++) {
            this.mPhones[i2].mCi.registerForNotAvailable(this.mRspHandler, i2 + 10, (Object) null);
            this.mPhones[i2].getCallTracker().registerForVoiceCallStarted(this.mRspHandler, i2 + 20, (Object) null);
            this.mPhones[i2].getCallTracker().registerForVoiceCallEnded(this.mRspHandler, i2 + 30, (Object) null);
            this.mPhones[i2].mCi.registerForCallAdditionalInfo(this.mRspHandler, i2 + 70, null);
            registerImsEvents(i2);
            this.mMobileDataUsage[i2] = new NetworkStats(SystemClock.elapsedRealtime(), 1);
            NetworkStatsManager statsManager = (NetworkStatsManager) this.mPhones[i2].getContext().getSystemService("netstats");
            if (statsManager != null) {
                this.mDataUsageProvider[i2] = new MobileDataUsageProvider(i2);
                statsManager.registerNetworkStatsProvider("MobileDataUsage" + i2, this.mDataUsageProvider[i2]);
            }
            this.mPhones[i2].mCi.registerForMobileDataUsage(this.mRspHandler, i2 + 80, null);
        }
    }

    public static MtkDataHelper makeMtkDataHelper(Context context, Phone[] phones) {
        if (context == null || phones == null) {
            throw new RuntimeException("param is null");
        }
        MtkDataHelper mtkDataHelper = sMtkDataHelper;
        if (mtkDataHelper == null) {
            logd("makeMtkDataHelper: phones.length=" + phones.length);
            sMtkDataHelper = new MtkDataHelper(context, phones);
        } else {
            mtkDataHelper.updatePhones(phones);
        }
        logd("makesMtkDataHelper: X sMtkDataHelper =" + sMtkDataHelper);
        return sMtkDataHelper;
    }

    public static MtkDataHelper getInstance() {
        MtkDataHelper mtkDataHelper = sMtkDataHelper;
        if (mtkDataHelper == null) {
            throw new RuntimeException("Should not be called before makesMtkDataHelper");
        }
        return mtkDataHelper;
    }

    private void registerEvents() {
        logd("registerEvents");
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].mCi.registerForNotAvailable(this.mRspHandler, i + 10, (Object) null);
            this.mPhones[i].getCallTracker().registerForVoiceCallStarted(this.mRspHandler, i + 20, (Object) null);
            this.mPhones[i].getCallTracker().registerForVoiceCallEnded(this.mRspHandler, i + 30, (Object) null);
            registerImsEvents(i);
            this.mPhones[i].mCi.registerForCallAdditionalInfo(this.mRspHandler, i + 70, null);
            this.mPhones[i].mCi.registerForDsdaStateChanged(this.mRspHandler, i + 50, null);
            NetworkStatsManager statsManager = (NetworkStatsManager) this.mPhones[i].getContext().getSystemService("netstats");
            if (statsManager != null) {
                this.mDataUsageProvider[i] = new MobileDataUsageProvider(i);
                statsManager.registerNetworkStatsProvider("MobileDataUsage" + i, this.mDataUsageProvider[i]);
            }
            this.mPhones[i].mCi.registerForMobileDataUsage(this.mRspHandler, i + 80, null);
        }
        logd("registered phone change event.");
    }

    private void registerImsEvents(int phoneId) {
        logd("registerImsEvents, phoneId = " + phoneId);
        Phone imsPhone = this.mPhones[phoneId].getImsPhone();
        if (imsPhone != null) {
            MtkImsPhoneCallTracker imsCt = imsPhone.getCallTracker();
            imsCt.registerForVoiceCallStarted(this.mRspHandler, phoneId + 20, null);
            imsCt.registerForVoiceCallEnded(this.mRspHandler, phoneId + 30, null);
            imsCt.registerForCallsDisconnectedDuringSrvcc(this.mRspHandler, phoneId + 40, null);
            return;
        }
        logd("Not register IMS phone calling state yet.");
    }

    private void unregisterEvents() {
        logd("unregisterEvents");
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].mCi.unregisterForNotAvailable(this.mRspHandler);
            this.mPhones[i].getCallTracker().unregisterForVoiceCallStarted(this.mRspHandler);
            this.mPhones[i].getCallTracker().unregisterForVoiceCallEnded(this.mRspHandler);
            unregisterImsEvents(i);
            if (i == 0) {
                this.mPhones[i].mCi.unregisterForDsdaStateChanged(this.mRspHandler);
            }
            NetworkStatsManager statsManager = (NetworkStatsManager) this.mPhones[i].getContext().getSystemService("netstats");
            if (statsManager != null) {
                statsManager.unregisterNetworkStatsProvider(this.mDataUsageProvider[i]);
            }
            this.mPhones[i].mCi.unregisterForMobileDataUsage(this.mRspHandler);
        }
    }

    private void unregisterImsEvents(int phoneId) {
        logd("unregisterImsEvents, phoneId = " + phoneId);
        Phone imsPhone = this.mPhones[phoneId].getImsPhone();
        if (imsPhone != null) {
            MtkImsPhoneCallTracker imsCt = imsPhone.getCallTracker();
            imsCt.unregisterForVoiceCallStarted(this.mRspHandler);
            imsCt.unregisterForVoiceCallEnded(this.mRspHandler);
            imsCt.unregisterForCallsDisconnectedDuringSrvcc(this.mRspHandler);
            return;
        }
        logd("Not unregister IMS phone calling state yet.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerForImsRadioTechChange() {
        logd("registerForImsRadioTechChange");
        for (int i = 0; i < this.mPhoneNum; i++) {
            PhoneSwitcher.getInstance().registerForImsRadioTechChange(this.mContext, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVoiceCallEvent(int phoneId, boolean ended) {
        for (int i = 0; i < this.mPhoneNum; i++) {
            MtkDataNetworkController dnc = (MtkDataNetworkController) this.mPhones[i].getDataNetworkController();
            if (dnc != null) {
                if (ended && phoneId != i) {
                    dnc.notifyVoiceCallEnd();
                }
                List<DataNetwork> dataNetworkList = dnc.getDataNetworkList();
                for (DataNetwork dn : dataNetworkList) {
                    ((MtkDataNetwork) dn).notifyVoiceCallEvent();
                }
            }
        }
    }

    public void notifyPreciseCallStateChanged() {
        this.mRspHandler.removeMessages(60);
        Message msg = this.mRspHandler.obtainMessage(60);
        msg.sendToTarget();
    }

    public int getNonRadioCallingPhone(ArrayList<Integer> callingPhoneList) {
        int nonRadioCallId = -1;
        if (callingPhoneList == null) {
            logd("getNonRadioCallingPhone: no calling phone!");
            return -1;
        }
        if (this.mCallingPhoneIdList.size() == 0 && callingPhoneList.size() == 2 && getInternalDsdaMode() == 0) {
            int nonRadioCallId2 = callingPhoneList.get(0).intValue();
            logd("getNonRadioCallingPhone: weird case, calling phone size is unexpected");
            return nonRadioCallId2;
        }
        if (this.mCallingPhoneIdList.size() > 0 && callingPhoneList.size() == 2 && getInternalDsdaMode() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Global CallingPhoneIdList: ");
            Iterator<Integer> it = this.mCallingPhoneIdList.iterator();
            while (it.hasNext()) {
                int cid = it.next().intValue();
                sb.append(cid + " ");
            }
            int ringingCallOnlyCount = 0;
            sb.append("Local CallingPhoneIdList:");
            Iterator<Integer> it2 = callingPhoneList.iterator();
            while (it2.hasNext()) {
                int callId = it2.next().intValue();
                sb.append(" " + callId + ": ");
                Phone phone = this.mPhones[callId];
                if (phone != null) {
                    boolean isRinging = phone.getRingingCall().getState().isRinging();
                    Phone imsPhone = this.mPhones[callId].getImsPhone();
                    if (!isRinging && imsPhone != null) {
                        isRinging = imsPhone.getRingingCall().getState().isRinging();
                    }
                    sb.append("ringing=" + isRinging + " ");
                    Call.State state = this.mPhones[callId].getForegroundCall().getState();
                    Call.State state2 = Call.State.IDLE;
                    boolean z = DBG;
                    boolean isRfCall = (state == state2 && this.mPhones[callId].getBackgroundCall().getState() == Call.State.IDLE) ? false : true;
                    if (!isRfCall && imsPhone != null) {
                        if (imsPhone.getForegroundCall().getState() == Call.State.IDLE && imsPhone.getBackgroundCall().getState() == Call.State.IDLE) {
                            z = false;
                        }
                        isRfCall = z;
                    }
                    sb.append("rfcall=" + isRfCall);
                    if (isRinging && !isRfCall) {
                        ringingCallOnlyCount++;
                        nonRadioCallId = callId;
                    }
                }
            }
            if (ringingCallOnlyCount == 2) {
                nonRadioCallId = this.mCallingPhoneIdList.get(0).intValue();
            }
            logd("getNonRadioCallingPhone nonRadioCallId=" + nonRadioCallId + " : " + ((Object) sb));
        }
        return nonRadioCallId;
    }

    public boolean isDataSupportConcurrent(int phoneId) {
        boolean isConcurrent;
        ArrayList<Integer> cpl = new ArrayList<>();
        for (int i = 0; i < this.mPhoneNum; i++) {
            if (this.mPhones[i].getState() != PhoneConstants.State.IDLE) {
                cpl.add(Integer.valueOf(i));
            }
        }
        int callingPhoneSize = cpl.size();
        boolean z = DBG;
        if (callingPhoneSize == 0) {
            logd("isDataSupportConcurrent: no calling phone!");
            return DBG;
        }
        int nonRadioCallingPhone = getNonRadioCallingPhone(cpl);
        if (nonRadioCallingPhone != -1) {
            cpl.remove(new Integer(nonRadioCallingPhone));
            callingPhoneSize = cpl.size();
            if (callingPhoneSize == 0) {
                return DBG;
            }
        }
        if (callingPhoneSize != 1 || phoneId != cpl.get(0).intValue()) {
            if (getInternalDsdaMode() == 1) {
                return DBG;
            }
            return isDsdalikeAvailable(cpl.get(0).intValue());
        }
        boolean inSrvcc = false;
        MtkGsmCdmaCallTracker ct = this.mPhones[phoneId].getCallTracker();
        Phone imsPhone = this.mPhones[phoneId].getImsPhone();
        boolean inPsEcc = imsPhone == null ? false : imsPhone.isInEmergencyCall();
        PhoneConstants.State csCallState = PhoneConstants.State.IDLE;
        if (ct != null) {
            if (ct.getHandoverConnectionSize() == 0) {
                z = false;
            }
            inSrvcc = z;
            csCallState = ct.getState();
        }
        if (inPsEcc || inSrvcc || csCallState != PhoneConstants.State.IDLE) {
            isConcurrent = this.mPhones[phoneId].getServiceStateTracker().isConcurrentVoiceAndDataAllowed();
            boolean mShouldAutoAttach = shouldAutoAttachForCall(phoneId);
            if (mShouldAutoAttach) {
                isConcurrent = DBG;
            }
        } else {
            isConcurrent = DBG;
        }
        logd("isDataSupportConcurrent: (voice/data on the same phone) isConcurrent = " + isConcurrent + ", phoneId = " + phoneId + ", callingPhoneId = " + cpl.get(0) + ", inPsEcc = " + inPsEcc + ", inSrvcc = " + inSrvcc + ", csCallState = " + csCallState);
        return isConcurrent;
    }

    public int getCallState() {
        return this.mCallingPhoneIdList.size() > 0 ? 1 : 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0034, code lost:
    
        logd("isWifiCallingEnabled phoneId: " + r2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isWifiCallingEnabled() {
        /*
            r5 = this;
            r0 = 0
            java.lang.String r1 = "phoneEx"
            android.os.IBinder r1 = android.os.ServiceManager.getService(r1)
            com.mediatek.internal.telephony.IMtkTelephonyEx r1 = com.mediatek.internal.telephony.IMtkTelephonyEx.Stub.asInterface(r1)
            if (r1 == 0) goto L53
            r2 = 0
        Lf:
            int r3 = r5.mPhoneNum     // Catch: android.os.RemoteException -> L4f
            if (r2 >= r3) goto L4e
            boolean r3 = android.telephony.SubscriptionManager.isValidPhoneId(r2)     // Catch: android.os.RemoteException -> L4f
            if (r3 == 0) goto L4b
            java.util.ArrayList<java.lang.Integer> r3 = r5.mCallingPhoneIdList     // Catch: android.os.RemoteException -> L4f
            java.lang.Integer r4 = java.lang.Integer.valueOf(r2)     // Catch: android.os.RemoteException -> L4f
            boolean r3 = r3.contains(r4)     // Catch: android.os.RemoteException -> L4f
            if (r3 == 0) goto L4b
            com.android.internal.telephony.Phone[] r3 = r5.mPhones     // Catch: android.os.RemoteException -> L4f
            r3 = r3[r2]     // Catch: android.os.RemoteException -> L4f
            int r3 = r3.getSubId()     // Catch: android.os.RemoteException -> L4f
            boolean r3 = r1.isWifiCallingEnabled(r3)     // Catch: android.os.RemoteException -> L4f
            r0 = r3
            if (r0 == 0) goto L4b
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: android.os.RemoteException -> L4f
            r3.<init>()     // Catch: android.os.RemoteException -> L4f
            java.lang.String r4 = "isWifiCallingEnabled phoneId: "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: android.os.RemoteException -> L4f
            java.lang.StringBuilder r3 = r3.append(r2)     // Catch: android.os.RemoteException -> L4f
            java.lang.String r3 = r3.toString()     // Catch: android.os.RemoteException -> L4f
            logd(r3)     // Catch: android.os.RemoteException -> L4f
            goto L4e
        L4b:
            int r2 = r2 + 1
            goto Lf
        L4e:
            goto L53
        L4f:
            r2 = move-exception
            r2.printStackTrace()
        L53:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.data.MtkDataHelper.isWifiCallingEnabled():boolean");
    }

    public boolean isWifiCallingEnabled(int phoneId) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            loge("invalid phone id: " + phoneId);
            return false;
        }
        IMtkTelephonyEx telephonyEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
        if (telephonyEx == null) {
            loge("get phoneEx service failed");
            return false;
        }
        boolean isWifiCallingEnabled = false;
        try {
            isWifiCallingEnabled = telephonyEx.isWifiCallingEnabled(this.mPhones[phoneId].getSubId());
            logd("isWifiCallingEnabled phoneId: " + phoneId + ", enabled=" + isWifiCallingEnabled);
            return isWifiCallingEnabled;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return isWifiCallingEnabled;
        }
    }

    public static boolean isImsOrEmergencyApn(String[] apnTypes) {
        if (apnTypes == null) {
            loge("isImsOrEmergencyApn: apnTypes is null");
            return false;
        }
        if (apnTypes.length == 0) {
            return false;
        }
        for (String type : apnTypes) {
            if (!"ims".equals(type) && !"emergency".equals(type)) {
                return false;
            }
        }
        return DBG;
    }

    public boolean isDataAllowedForConcurrent(int phoneId) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            logd("isDataAllowedForConcurrent: invalid calling phone id");
            return false;
        }
        if (!isDataSupportConcurrent(phoneId)) {
            if (!isWifiCallingEnabled() || this.mPhones[phoneId].isInEmergencyCall()) {
                return false;
            }
            return DBG;
        }
        return DBG;
    }

    public static boolean hasVsimApn(String[] apnTypes) {
        if (apnTypes == null) {
            loge("hasVsimApn: apnTypes is null");
            return false;
        }
        if (apnTypes.length == 0) {
            return false;
        }
        for (String type : apnTypes) {
            if (TextUtils.equals("vsim", type)) {
                return DBG;
            }
        }
        return false;
    }

    public boolean isTestIccCard(int phoneId) {
        String testCard = SystemProperties.get(PROPERTY_RIL_TEST_SIM[phoneId], "");
        if (VDBG) {
            logd("isTestIccCard: phoneId id = " + phoneId + ", iccType = " + testCard);
        }
        if (testCard == null || !testCard.equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return false;
        }
        return DBG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInSRVCC() {
        if (this.mSrvccState == Call.SrvccState.COMPLETED) {
            return DBG;
        }
        return false;
    }

    public static boolean isCdma4GDualModeCard(int phoneId) {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
            logd("isCdma4GDualModeCard invalid phoneId = " + phoneId);
            return false;
        }
        MtkIccCardConstants.CardType cardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(phoneId);
        if (cardType == MtkIccCardConstants.CardType.CT_4G_UICC_CARD || cardType == MtkIccCardConstants.CardType.NOT_CT_UICC_CARD) {
            return DBG;
        }
        return false;
    }

    public static boolean isCdma3GDualModeCard(int phoneId) {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
            logd("isCdma3GDualModeCard invalid phoneId = " + phoneId);
            return false;
        }
        MtkIccCardConstants.CardType cardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(phoneId);
        if (cardType == MtkIccCardConstants.CardType.UIM_SIM_CARD || cardType == MtkIccCardConstants.CardType.CT_UIM_SIM_CARD) {
            return DBG;
        }
        return false;
    }

    public static boolean isCdma3GCard(int phoneId) {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
            logd("isCdma3GCard invalid phoneId = " + phoneId);
            return false;
        }
        MtkIccCardConstants.CardType cardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(phoneId);
        if (cardType == MtkIccCardConstants.CardType.UIM_CARD || cardType == MtkIccCardConstants.CardType.CT_3G_UIM_CARD) {
            return DBG;
        }
        return false;
    }

    private boolean shouldAutoAttachForCall(int phoneId) {
        ServiceState serviceState = this.mPhones[phoneId].getServiceState();
        PhoneSwitcher phoneSwitcher = PhoneSwitcher.getInstance();
        boolean mAutoAttach = (serviceState == null || phoneId == phoneSwitcher.getPreferredDataPhoneId() || serviceState.getVoiceNetworkType() != 3) ? false : DBG;
        if (serviceState != null) {
            logd("shouldAutoAttachForCall=" + mAutoAttach + ", phoneId:" + phoneId + ", getVoiceNetworkType=" + serviceState.getVoiceNetworkType());
        }
        return mAutoAttach;
    }

    public int getDsdaMode() {
        return calculateDsdaMode(this.mCurrentDsdaMode);
    }

    public int getInternalDsdaMode() {
        return calculateDsdaMode(this.mDsdaMode);
    }

    private int calculateDsdaMode(int mode) {
        if (mode <= 1) {
            return mode;
        }
        int dsdaState = mode;
        if (mode > 100) {
            dsdaState = mode / 100;
        }
        if (dsdaState == 2 || dsdaState == 3) {
            return 1;
        }
        return 0;
    }

    public static int getPreferredPhoneId() {
        if (PhoneSwitcher.getInstance() == null) {
            return -1;
        }
        int preferredDataPhoneId = PhoneSwitcher.getInstance().getPreferredDataPhoneId();
        return preferredDataPhoneId;
    }

    public static boolean isPreferredDataPhone(Phone phone) {
        int preferredDataPhoneId = PhoneSwitcher.getInstance() != null ? PhoneSwitcher.getInstance().getPreferredDataPhoneId() : -1;
        int curPhoneId = phone.getPhoneId();
        if (preferredDataPhoneId != curPhoneId) {
            logd("Current phone is not preferred phone: curPhoneId = " + curPhoneId + ", preferredDataPhoneId = " + preferredDataPhoneId);
            return false;
        }
        return DBG;
    }

    public static boolean isDefaultDataPhone(Phone phone) {
        int phoneId = phone.getPhoneId();
        int defaultDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        if (phoneId == defaultDataPhoneId) {
            return DBG;
        }
        return false;
    }

    public boolean isDsdalikeAvailable(int phoneId) {
        boolean isDsdalikeAvailable;
        MtkTelephonyManagerEx tmEx = MtkTelephonyManagerEx.getDefault();
        if (tmEx != null && (isDsdalikeAvailable = tmEx.isDataAvailableForGwsdDualSim(this.mGwsdDualSimStatusArray[phoneId]))) {
            logd("isDsdalikeAvailable: " + isDsdalikeAvailable + ", phoneId: " + phoneId + ", mGwsdDualSimStatusArray[" + phoneId + "]:" + this.mGwsdDualSimStatusArray[phoneId]);
            return DBG;
        }
        logd("isDsdalikeAvailable return false.");
        return false;
    }

    public static boolean hasOperatorIaCapability() {
        TelephonyDevController telephonyDevController = mTelDevController;
        if (telephonyDevController == null || telephonyDevController.getModem(0) == null || !((MtkHardwareConfig) mTelDevController.getModem(0)).hasOperatorIaCapability()) {
            return false;
        }
        return DBG;
    }

    public static boolean hasRaCapability() {
        TelephonyDevController telephonyDevController = mTelDevController;
        if (telephonyDevController == null || telephonyDevController.getModem(0) == null || !((MtkHardwareConfig) mTelDevController.getModem(0)).hasRaCapability()) {
            return false;
        }
        return DBG;
    }

    public boolean getIwlanRegState(Phone phone) {
        NetworkRegistrationInfo nri;
        if (phone.getServiceStateTracker() == null || (nri = phone.getServiceStateTracker().getServiceState().getNetworkRegistrationInfo(2, 2)) == null || !nri.isInService()) {
            return false;
        }
        return DBG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMobileDataUsage(AsyncResult asyncResult, int i) {
        if (!SubscriptionManager.isValidPhoneId(i)) {
            loge("updateMobileDataUsage invalid phoneId=null" + i);
            return;
        }
        int[] iArr = (int[]) asyncResult.result;
        int i2 = iArr.length > 0 ? iArr[0] : 0;
        int i3 = iArr.length > 1 ? iArr[1] : 0;
        int i4 = iArr.length > 2 ? iArr[2] : 0;
        int i5 = iArr.length > 3 ? iArr[3] : 0;
        LinkProperties linkProperties = null;
        Iterator<DataNetwork> it = ((MtkDataNetworkController) this.mPhones[i].getDataNetworkController()).getDataNetworkList().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            DataNetwork next = it.next();
            if (next.isInternetSupported()) {
                linkProperties = next.getLinkProperties();
                break;
            }
        }
        String interfaceName = linkProperties == null ? "" : linkProperties.getInterfaceName();
        if (interfaceName == null) {
            loge("updateMobileDataUsage ifacename = null");
            return;
        }
        NetworkStats networkStatsAddEntry = new NetworkStats(SystemClock.elapsedRealtime(), 1).add(this.mMobileDataUsage[i]).addEntry(new NetworkStats.Entry(interfaceName, -10, 0, 0, 1, this.mPhones[i].getServiceState().getDataRoaming() ? 1 : 0, 1, i4, i5, i2, i3, 0L));
        logd("updateMobileDataUsage ifacename:" + interfaceName + ", txBytes:" + i2 + ", txPkts:" + i3 + ", rxBytes:" + i4 + ", rxPkts:" + i5);
        this.mMobileDataUsage[i] = networkStatsAddEntry;
    }

    public static boolean iccidMatches(String mvnoData, String iccId) {
        String[] mvnoIccidList = mvnoData.split(",");
        for (String mvnoIccid : mvnoIccidList) {
            if (iccId.startsWith(mvnoIccid)) {
                logd("mvno icc id match found");
                return DBG;
            }
        }
        return false;
    }

    public static boolean imsiMatches(String imsiDB, String imsiSIM) {
        int len = imsiDB.length();
        if (len <= 0 || len > imsiSIM.length()) {
            return false;
        }
        for (int idx = 0; idx < len; idx++) {
            char c = imsiDB.charAt(idx);
            if (c != 'x' && c != 'X' && c != imsiSIM.charAt(idx)) {
                return false;
            }
        }
        return DBG;
    }

    public static boolean mvnoMatches(IccRecords r, int mvnoType, String mvnoMatchData) {
        String iccId;
        if (mvnoType == 0) {
            String spn = r.getServiceProviderNameWithBrandOverride();
            if (spn != null && spn.equalsIgnoreCase(mvnoMatchData)) {
                return DBG;
            }
        } else if (mvnoType == 1) {
            String imsiSIM = r.getIMSI();
            if (imsiSIM != null && imsiMatches(mvnoMatchData, imsiSIM)) {
                return DBG;
            }
        } else if (mvnoType == 2) {
            String gid1 = r.getGid1();
            int mvno_match_data_length = mvnoMatchData.length();
            if (gid1 != null && gid1.length() >= mvno_match_data_length && gid1.substring(0, mvno_match_data_length).equalsIgnoreCase(mvnoMatchData)) {
                return DBG;
            }
        } else if (mvnoType == 3 && (iccId = r.getIccId()) != null && iccidMatches(mvnoMatchData, iccId)) {
            return DBG;
        }
        return false;
    }

    public static boolean isSimMeLockAllowed(int phoneId) {
        int cap;
        int policy = MtkTelephonyManagerEx.getDefault().getSimLockPolicy();
        if (policy == -1 || policy == 0 || (cap = MtkTelephonyManagerEx.getDefault().getShouldServiceCapability(phoneId)) == 0 || cap == 2) {
            return DBG;
        }
        return false;
    }

    public void onHandoverStart(int phoneId) {
        this.mRspHandler.sendEmptyMessage(phoneId + EVENT_HANDOVER_START);
    }

    public boolean isAutoSwitchEnabledByUser(Phone phone) {
        if (phone == null || !phone.getDataSettingsManager().isMobileDataPolicyEnabled(3)) {
            return false;
        }
        return DBG;
    }

    protected static void logv(String s) {
        Rlog.v(LOG_TAG, s);
    }

    protected static void logd(String s) {
        Rlog.d(LOG_TAG, s);
    }

    protected static void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }

    protected static void logi(String s) {
        Rlog.i(LOG_TAG, s);
    }
}
