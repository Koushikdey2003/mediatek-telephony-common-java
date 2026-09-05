package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.emergency.EmergencyNumber;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.GsmCdmaCall;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmCdmaCallTracker extends GsmCdmaCallTracker {
    protected static final int EVENT_CALL_ADDITIONAL_INFO = 1006;
    protected static final int EVENT_CDMA_CALL_ACCEPTED = 1004;
    protected static final int EVENT_DIAL_CALL_RESULT = 1002;
    protected static final int EVENT_ECONF_SRVCC_INDICATION = 1005;
    protected static final int EVENT_INCOMING_CALL_INDICATION = 1000;
    protected static final int EVENT_MTK_BASE = 1000;
    protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 1001;
    private static final int IGNORE_DUPLICATE_WAITING_CALL_INTERVAL_MILLISECONDS = 2000;
    private static final int MIN_CONNECTIONS_IN_CDMA_CONFERENCE = 2;
    private static final int MT_CALL_RQ = 4;
    private static final String PROP_LOG_TAG = "GsmCdmaCallTkr";
    private int[] mEconfSrvccConnectionIds;
    private boolean mHasPendingCheckAndEnableData;
    boolean mHasPendingSwapRequest;
    private boolean mHasPendingUpdatePhoneType;
    public MtkGsmCdmaCallTrackerHelper mHelper;
    protected Connection mImsConfHostConnection;
    private ArrayList<Connection> mImsConfParticipants;
    public MtkRIL mMtkCi;
    protected boolean mNeedWaitImsEConfSrvcc;
    private int mPhoneType;
    WaitForHoldToHangup mWaitForHoldToHangupRequest;
    WaitForHoldToRedial mWaitForHoldToRedialRequest;
    boolean mWaitPollAfterHangupPendingMO;

    class WaitForHoldToRedial {
        private EmergencyNumber mEmergencyNumberInfo;
        private boolean mWaitToRedial = false;
        private String mDialString = null;
        private int mClirMode = 0;
        private UUSInfo mUUSInfo = null;
        private boolean mIsEmergencyCall = false;
        private boolean mHasKnownUserIntentEmergency = false;

        WaitForHoldToRedial() {
            resetToRedial();
        }

        boolean isWaitToRedial() {
            return this.mWaitToRedial;
        }

        void setToRedial() {
            this.mWaitToRedial = true;
        }

        public void setToRedial(String dialSting, boolean isEmergencyCall, EmergencyNumber emergencyNumberInfo, boolean hasKnownUserIntentEmergency, int clir, UUSInfo uusinfo) {
            this.mWaitToRedial = true;
            this.mDialString = dialSting;
            this.mIsEmergencyCall = isEmergencyCall;
            this.mEmergencyNumberInfo = emergencyNumberInfo;
            this.mHasKnownUserIntentEmergency = hasKnownUserIntentEmergency;
            this.mClirMode = clir;
            this.mUUSInfo = uusinfo;
        }

        public void resetToRedial() {
            Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "Reset mWaitForHoldToRedialRequest variables");
            this.mWaitToRedial = false;
            this.mDialString = null;
            this.mClirMode = 0;
            this.mUUSInfo = null;
            this.mIsEmergencyCall = false;
            this.mHasKnownUserIntentEmergency = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean resumeDialAfterHold() {
            Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "resumeDialAfterHold begin");
            if (this.mWaitToRedial) {
                if (this.mDialString == null) {
                    Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "resumeDialAfterHold cancel (dial string empty yet)");
                } else {
                    MtkGsmCdmaCallTracker.this.mCi.dial(this.mDialString, this.mIsEmergencyCall, this.mEmergencyNumberInfo, this.mHasKnownUserIntentEmergency, this.mClirMode, this.mUUSInfo, MtkGsmCdmaCallTracker.this.obtainCompleteMessage(1002));
                }
                resetToRedial();
                Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "resumeDialAfterHold end");
                return true;
            }
            return false;
        }
    }

    class WaitForHoldToHangup {
        private boolean mWaitToHangup = false;
        private boolean mHoldDone = false;
        private GsmCdmaCall mCall = null;

        WaitForHoldToHangup() {
            resetToHangup();
        }

        boolean isWaitToHangup() {
            return this.mWaitToHangup;
        }

        boolean isHoldDone() {
            return this.mHoldDone;
        }

        void setHoldDone() {
            this.mHoldDone = true;
        }

        void setToHangup() {
            this.mWaitToHangup = true;
        }

        public void setToHangup(GsmCdmaCall call) {
            this.mWaitToHangup = true;
            this.mCall = call;
        }

        public void resetToHangup() {
            Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "Reset mWaitForHoldToHangupRequest variables");
            this.mWaitToHangup = false;
            this.mHoldDone = false;
            this.mCall = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean resumeHangupAfterHold() {
            Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "resumeHangupAfterHold begin");
            if (this.mWaitToHangup && this.mCall != null) {
                Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "resumeHangupAfterHold to hangup call");
                this.mWaitToHangup = false;
                this.mHoldDone = false;
                try {
                    MtkGsmCdmaCallTracker.this.hangup(this.mCall);
                } catch (CallStateException ex) {
                    ex.printStackTrace();
                    Rlog.e(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "unexpected error on hangup (" + ex.getMessage() + ")");
                }
                Rlog.d(MtkGsmCdmaCallTracker.PROP_LOG_TAG, "resumeHangupAfterHold end");
                this.mCall = null;
                return true;
            }
            resetToHangup();
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmCdmaCallTracker(GsmCdmaPhone phone) {
        super(phone);
        this.mMtkCi = null;
        this.mHasPendingSwapRequest = false;
        this.mWaitForHoldToRedialRequest = new WaitForHoldToRedial();
        this.mWaitForHoldToHangupRequest = new WaitForHoldToHangup();
        this.mWaitPollAfterHangupPendingMO = false;
        this.mPhoneType = 0;
        this.mHasPendingUpdatePhoneType = false;
        this.mHasPendingCheckAndEnableData = false;
        this.mNeedWaitImsEConfSrvcc = false;
        this.mImsConfHostConnection = null;
        this.mImsConfParticipants = new ArrayList<>();
        this.mEconfSrvccConnectionIds = null;
        this.mRingingCall = new GsmCdmaCall(this);
        this.mForegroundCall = new GsmCdmaCall(this);
        this.mBackgroundCall = new GsmCdmaCall(this);
        MtkRIL mtkRIL = this.mCi;
        this.mMtkCi = mtkRIL;
        mtkRIL.setOnIncomingCallIndication(this, 1000, null);
        this.mMtkCi.registerForCallAdditionalInfo(this, 1006, null);
        this.mMtkCi.registerForOffOrNotAvailable(this, 1001, null);
        this.mHelper = new MtkGsmCdmaCallTrackerHelper(phone.getContext(), this);
        this.mMtkCi.registerForEconfSrvcc(this, 1005, null);
    }

    public void updatePhoneType() {
        if (this.mState != PhoneConstants.State.IDLE) {
            this.mHasPendingUpdatePhoneType = true;
            Rlog.d("GsmCdmaCallTracker", "[updatePhoneType]mHasPendingUpdatePhoneType = true");
            if (this.mPhoneType == 2 && this.mPhone.isPhoneTypeGsm()) {
                this.mHasPendingCheckAndEnableData = true;
            }
            if (this.mLastRelevantPoll == null) {
                pollCallsWhenSafe();
                return;
            }
            return;
        }
        super.updatePhoneType();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updatePhoneType(boolean duringInit) {
        if (this.mPhoneType == 2 && !this.mPhone.isPhoneTypeGsm()) {
            return;
        }
        super.updatePhoneType(duringInit);
        if (this.mPhone.isPhoneTypeGsm()) {
            if (this.mMtkCi == null) {
                this.mMtkCi = this.mCi;
            }
            this.mMtkCi.unregisterForLineControlInfo(this);
            this.mPhoneType = 1;
            return;
        }
        if (this.mMtkCi == null) {
            this.mMtkCi = this.mCi;
        }
        this.mMtkCi.unregisterForLineControlInfo(this);
        this.mMtkCi.registerForLineControlInfo(this, 1004, null);
        this.mPhoneType = 2;
    }

    protected void reset() {
        this.mHelper.setGwsdCall(false);
        super.reset();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void handleMessage(Message msg) {
        Connection connection;
        this.mHelper.LogerMessage(msg.what);
        switch (msg.what) {
            case 1:
                Rlog.d("GsmCdmaCallTracker", "Event EVENT_POLL_CALLS_RESULT Received");
                if (msg == this.mLastRelevantPoll) {
                    this.mNeedsPoll = false;
                    this.mLastRelevantPoll = null;
                    boolean bNoCallExists = noAnyCallFromModemExist((AsyncResult) msg.obj);
                    if (!bNoCallExists && this.mHasPendingUpdatePhoneType) {
                        this.mHasPendingUpdatePhoneType = false;
                        updatePhoneType(false);
                        Rlog.d("GsmCdmaCallTracker", "[EVENT_POLL_CALLS_RESULT]!bNoCallExists");
                    }
                    handlePollCalls((AsyncResult) msg.obj);
                    if (bNoCallExists && this.mHasPendingUpdatePhoneType) {
                        this.mHasPendingUpdatePhoneType = false;
                        updatePhoneType(false);
                        Rlog.d("GsmCdmaCallTracker", "[EVENT_POLL_CALLS_RESULT]bNoCallExists");
                    }
                    if (this.mHasPendingCheckAndEnableData) {
                        if (bNoCallExists) {
                            checkAndEnableDataCallAfterEmergencyCallDropped();
                        }
                        this.mHasPendingCheckAndEnableData = false;
                    }
                    if (this.mWaitForHoldToHangupRequest.isHoldDone()) {
                        Rlog.d(PROP_LOG_TAG, "Switch ends, and poll call done, then resume hangup");
                        this.mWaitForHoldToHangupRequest.resumeHangupAfterHold();
                        return;
                    }
                    return;
                }
                return;
            case 8:
                if (isPhoneTypeGsm()) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.exception != null) {
                        if (this.mWaitForHoldToRedialRequest.isWaitToRedial()) {
                            if (this.mPendingMO != null) {
                                this.mPendingMO.mCause = 3;
                                this.mPendingMO.onDisconnect(3);
                                this.mPendingMO = null;
                                this.mHangupPendingMO = false;
                                updateWaitPollAfterAbortPendingMO(false);
                                updatePhoneState();
                            }
                            resumeBackgroundAfterDialFailed();
                            this.mPhone.notifyPreciseCallStateChanged();
                            this.mWaitForHoldToRedialRequest.resetToRedial();
                        }
                        if (this.mRingingCall.getState() == Call.State.INCOMING || (this.mRingingCall.getState() == Call.State.WAITING && this.mForegroundCall.getConnections().size() == 1 && ((Connection) this.mForegroundCall.getConnections().get(0)).getDisconnectCause() == 3)) {
                            Rlog.i(PROP_LOG_TAG, "Answer the call again, because fg call is disconnected in Modem");
                            setMute(false);
                            this.mPhone.getVoiceCallSessionStats().onRilAcceptCall(this.mRingingCall.getConnections());
                            this.mCi.acceptCall(obtainCompleteMessage());
                        } else {
                            if (msg.what == 8 && (connection = this.mForegroundCall.getLatestConnection()) != null) {
                                connection.onConnectionEvent("android.telecom.event.CALL_SWITCH_FAILED", (Bundle) null);
                            }
                            this.mPhone.notifySuppServiceFailed(getFailedService(msg.what));
                        }
                    } else {
                        synchronized (this.mWaitForHoldToRedialRequest) {
                            if (this.mWaitForHoldToRedialRequest.isWaitToRedial()) {
                                boolean isResumeDial = this.mWaitForHoldToRedialRequest.resumeDialAfterHold();
                                Rlog.d(PROP_LOG_TAG, "Switch success, then resume dial. " + (isResumeDial ? "(t)" : "(f)"));
                            }
                            break;
                        }
                    }
                    if (this.mWaitForHoldToHangupRequest.isWaitToHangup()) {
                        if (ar.exception == null && this.mWaitForHoldToHangupRequest.mCall != null) {
                            Rlog.d(PROP_LOG_TAG, "Switch ends, found waiting hangup. switch fg/bg call.");
                            if (this.mWaitForHoldToHangupRequest.mCall == this.mForegroundCall) {
                                this.mWaitForHoldToHangupRequest.setToHangup(this.mBackgroundCall);
                            } else if (this.mWaitForHoldToHangupRequest.mCall == this.mBackgroundCall) {
                                this.mWaitForHoldToHangupRequest.setToHangup(this.mForegroundCall);
                            }
                        }
                        Rlog.d(PROP_LOG_TAG, "Switch ends, wait for poll call done to hangup");
                        this.mWaitForHoldToHangupRequest.setHoldDone();
                    }
                    this.mHasPendingSwapRequest = false;
                    operationComplete();
                    return;
                }
                return;
            case 14:
                Rlog.d(PROP_LOG_TAG, "Receives EVENT_EXIT_ECM_RESPONSE_CDMA");
                if (this.mPendingCallInEcm) {
                    String dialString = (String) ((AsyncResult) msg.obj).userObj;
                    if (this.mPendingMO == null) {
                        this.mPendingMO = new GsmCdmaConnection(this.mPhone, dialString, this, this.mForegroundCall, new PhoneInternalInterface.DialArgs.Builder().setIsEmergency(false).build());
                    }
                    if (!isPhoneTypeGsm()) {
                        this.mCi.dial(this.mPendingMO.getAddress(), this.mPendingMO.isEmergencyCall(), this.mPendingMO.getEmergencyNumberInfo(), this.mPendingMO.hasKnownUserIntentEmergency(), this.mPendingCallClirMode, obtainCompleteMessage());
                    } else {
                        Rlog.e("GsmCdmaCallTracker", "originally unexpected event " + msg.what + " not handled by phone type " + this.mPhone.getPhoneType());
                        this.mCi.dial(this.mPendingMO.getAddress(), this.mPendingMO.isEmergencyCall(), this.mPendingMO.getEmergencyNumberInfo(), this.mPendingMO.hasKnownUserIntentEmergency(), this.mPendingCallClirMode, (UUSInfo) null, obtainCompleteMessage());
                    }
                    this.mPendingCallInEcm = false;
                }
                this.mPhone.unsetOnEcbModeExitResponse(this);
                return;
            case 16:
                if (!isPhoneTypeGsm()) {
                    if (((AsyncResult) msg.obj).exception == null && this.mPendingMO != null) {
                        this.mPendingMO.onConnectedInOrOut();
                        this.mPendingMO = null;
                        return;
                    }
                    return;
                }
                Rlog.e("GsmCdmaCallTracker", "unexpected event " + msg.what + " not handled by phone type " + this.mPhone.getPhoneType());
                return;
            case 1000:
                this.mHelper.CallIndicationProcess((AsyncResult) msg.obj);
                return;
            case 1001:
                Rlog.d(PROP_LOG_TAG, "Receives EVENT_RADIO_OFF_OR_NOT_AVAILABLE");
                if (!this.mPendingCallInEcm) {
                    handlePollCalls(new AsyncResult((Object) null, (Object) null, new CommandException(CommandException.Error.RADIO_NOT_AVAILABLE)));
                    this.mLastRelevantPoll = null;
                    return;
                }
                return;
            case 1002:
                if (((AsyncResult) msg.obj).exception != null) {
                    Rlog.d(PROP_LOG_TAG, "dial call failed!!");
                }
                operationComplete();
                return;
            case 1004:
                Rlog.d(PROP_LOG_TAG, "Receives EVENT_CDMA_CALL_ACCEPTED");
                if (((AsyncResult) msg.obj).exception == null) {
                    handleCallAccepted();
                    return;
                }
                return;
            case 1005:
                log("Receives EVENT_ECONF_SRVCC_INDICATION");
                if (!hasParsingCEPCapability()) {
                    this.mEconfSrvccConnectionIds = (int[]) ((AsyncResult) msg.obj).result;
                    this.mNeedWaitImsEConfSrvcc = false;
                    pollCallsWhenSafe();
                    return;
                }
                return;
            case 1006:
                this.mHelper.handleCallAdditionalInfo((AsyncResult) msg.obj);
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    public void hangupAll() throws CallStateException {
        Rlog.d(PROP_LOG_TAG, "hangupAll");
        this.mMtkCi.hangupAll(obtainCompleteMessage());
        if (!this.mRingingCall.isIdle()) {
            this.mRingingCall.onHangupLocal();
        }
        if (!this.mForegroundCall.isIdle()) {
            this.mForegroundCall.onHangupLocal();
        }
        if (!this.mBackgroundCall.isIdle()) {
            this.mBackgroundCall.onHangupLocal();
        }
    }

    void updateWaitPollAfterAbortPendingMO(boolean newValue) {
        updateWaitPollAfterAbortPendingMO(newValue, false);
    }

    void updateWaitPollAfterAbortPendingMO(boolean newValue, boolean forceUpdate) {
        if (this.mWaitPollAfterHangupPendingMO != newValue || forceUpdate) {
            Rlog.d("GsmCdmaCallTracker", "set mWaitPollAfterHangupPendingMO " + (this.mWaitPollAfterHangupPendingMO ? "t" : "f") + " -> " + (newValue ? "t" : "f") + (forceUpdate ? " (forceUpdate)" : ""));
            this.mWaitPollAfterHangupPendingMO = newValue;
        }
    }

    private boolean noAnyCallFromModemExist(AsyncResult ar) {
        List polledCalls;
        if (ar.exception == null) {
            polledCalls = (List) ar.result;
        } else {
            polledCalls = new ArrayList();
        }
        return polledCalls.size() == 0;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void checkForDialIssues(boolean isEmergencyCall) throws CallStateException {
        super.checkForDialIssues(isEmergencyCall);
        if (this.mPendingMO != null || this.mHangupPendingMO || this.mWaitPollAfterHangupPendingMO) {
            Rlog.d("GsmCdmaCallTracker", "Check pendingMO operations before dial (" + (this.mPendingMO != null ? "t," : "f,") + (this.mHangupPendingMO ? "t," : "f,") + (this.mWaitPollAfterHangupPendingMO ? "t" : "f") + ")");
            updateWaitPollAfterAbortPendingMO(false);
            throw new CallStateException(3, "A call is already dialing..");
        }
    }

    private void resumeBackgroundAfterDialFailed() {
        List<Connection> connCopy = (List) this.mBackgroundCall.mConnections.clone();
        int s = connCopy.size();
        for (int i = 0; i < s; i++) {
            MtkGsmCdmaConnection conn = (MtkGsmCdmaConnection) connCopy.get(i);
            conn.resumeHoldAfterDialFailed();
        }
    }

    protected void handleCallWaitingInfo(CdmaCallWaitingNotification cw) {
        if (!shouldNotifyWaitingCall(cw)) {
            return;
        }
        if (this.mForegroundCall.mConnections.size() > 2) {
            Iterator it = this.mForegroundCall.mConnections.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Connection c = (Connection) it.next();
                if (cw.number != null && cw.number.equals(c.getAddress())) {
                    c.onDisconnect(2);
                    break;
                }
            }
        }
        super.handleCallWaitingInfo(cw);
    }

    private void handleCallAccepted() {
        List connections = this.mForegroundCall.getConnections();
        int count = connections.size();
        Rlog.d(PROP_LOG_TAG, "handleCallAccepted, fgcall count=" + count);
        if (count == 1) {
            GsmCdmaConnection c = (GsmCdmaConnection) connections.get(0);
            if ((c instanceof MtkGsmCdmaConnection) && (this.mPhone instanceof MtkGsmCdmaPhone)) {
                ((MtkGsmCdmaConnection) c).onCdmaCallAccepted();
            }
        }
    }

    private boolean shouldNotifyWaitingCall(CdmaCallWaitingNotification cw) {
        String address = cw.number;
        Rlog.d(PROP_LOG_TAG, "shouldNotifyWaitingCall");
        if (address != null && address.length() > 0) {
            GsmCdmaConnection lastRingConn = this.mRingingCall.getLatestConnection();
            if (lastRingConn != null) {
                if (address.equals(lastRingConn.getAddress())) {
                    Rlog.d(PROP_LOG_TAG, "handleCallWaitingInfo, skip duplicate waiting call!");
                    return false;
                }
                Rlog.d(PROP_LOG_TAG, "handleCallWaitingInfo, remove older waiting call!");
                lastRingConn.onDisconnect(1);
                clearDisconnected();
            }
            GsmCdmaConnection lastActiveConn = this.mForegroundCall.getLatestConnection();
            if (lastActiveConn != null && address.equals(lastActiveConn.getAddress()) && System.currentTimeMillis() - lastActiveConn.getConnectTime() < 2000) {
                Rlog.d(PROP_LOG_TAG, "handleCallWaitingInfo, skip duplicate waiting call!");
                return false;
            }
        }
        return true;
    }

    protected Connection getHoConnection(DriverCall dc) {
        if (dc == null) {
            return null;
        }
        int[] iArr = this.mEconfSrvccConnectionIds;
        if (iArr != null && dc != null) {
            int numOfParticipants = iArr[0];
            int index = 1;
            while (true) {
                if (index > numOfParticipants) {
                    break;
                }
                if (dc.index != this.mEconfSrvccConnectionIds[index]) {
                    index++;
                } else {
                    Rlog.d(PROP_LOG_TAG, "SRVCC: getHoConnection for call-id:" + dc.index + " in a conference is found!");
                    if (this.mImsConfHostConnection == null) {
                        Rlog.d(PROP_LOG_TAG, "SRVCC: but mImsConfHostConnection is null, try to find by callState");
                    } else {
                        Rlog.v(PROP_LOG_TAG, "SRVCC: ret= " + this.mImsConfHostConnection);
                        return this.mImsConfHostConnection;
                    }
                }
            }
        }
        return super.getHoConnection(dc);
    }

    private synchronized boolean restoreConferenceParticipantAddress() {
        int[] iArr = this.mEconfSrvccConnectionIds;
        if (iArr == null) {
            Rlog.d(PROP_LOG_TAG, "SRVCC: restoreConferenceParticipantAddress():ignore because mEconfSrvccConnectionIds is empty");
            return false;
        }
        boolean finishRestore = false;
        int numOfParticipants = iArr[0];
        for (int index = 1; index <= numOfParticipants; index++) {
            int participantCallId = this.mEconfSrvccConnectionIds[index];
            GsmCdmaConnection participantConnection = this.mConnections[participantCallId - 1];
            if (participantConnection != null) {
                Rlog.d(PROP_LOG_TAG, "SRVCC: found conference connections!");
                if (participantConnection.mOrigConnection instanceof MtkImsPhoneConnection) {
                    MtkImsPhoneConnection hostConnection = participantConnection.mOrigConnection;
                    if (hostConnection == null) {
                        Rlog.v(PROP_LOG_TAG, "SRVCC: no host, ignore connection: " + participantConnection);
                    } else {
                        String address = hostConnection.getConferenceParticipantAddress(index - 1);
                        if (participantConnection instanceof MtkGsmCdmaConnection) {
                            ((MtkGsmCdmaConnection) participantConnection).updateConferenceParticipantAddress(address);
                        }
                        finishRestore = true;
                        Rlog.v(PROP_LOG_TAG, "SRVCC: restore Connection=" + participantConnection + " with address:" + address);
                    }
                } else {
                    Rlog.v(PROP_LOG_TAG, "SRVCC: host is abnormal, ignore connection: " + participantConnection);
                }
            }
        }
        return finishRestore;
    }

    boolean hasParsingCEPCapability() {
        TelephonyManager tm = (TelephonyManager) this.mPhone.getContext().getSystemService("phone");
        String mccmnc = tm.getSimOperatorNumericForPhone(this.mPhone.getPhoneId());
        log("hasParsingCEPCapability - mccmnc = " + mccmnc);
        if ("25099".equals(mccmnc) || "732103".equals(mccmnc) || "732111".equals(mccmnc) || "24001".equals(mccmnc)) {
            return false;
        }
        return true;
    }

    public int getHandoverConnectionSize() {
        return this.mHandoverConnections.size();
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(GsmCdmaPhone phone, String dialString, GsmCdmaCallTracker ct, GsmCdmaCall parent, PhoneInternalInterface.DialArgs dialArgs) {
        updateWaitPollAfterAbortPendingMO(false);
        return new MtkGsmCdmaConnection(phone, dialString, ct, parent, dialArgs);
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(Context context, CdmaCallWaitingNotification cw, GsmCdmaCallTracker ct, GsmCdmaCall parent) {
        return new MtkGsmCdmaConnection(context, cw, ct, parent);
    }

    protected GsmCdmaConnection onCreateGsmCdmaConnection(GsmCdmaPhone phone, DriverCall dc, GsmCdmaCallTracker ct, int index) {
        return new MtkGsmCdmaConnection(phone, dc, ct, index);
    }

    protected void onGsmDialBeforeHoldActiveCall() {
        this.mWaitForHoldToRedialRequest.setToRedial();
    }

    protected void onGsmDialInvalidNumber() {
        this.mWaitForHoldToRedialRequest.resetToRedial();
    }

    protected void onGsmDial(String address, boolean isEmergencyCall, EmergencyNumber emergencyNumberInfo, boolean hasKnownUserIntentEmergency, int clirMode, UUSInfo uusInfo) {
        synchronized (this.mWaitForHoldToRedialRequest) {
            if (!this.mWaitForHoldToRedialRequest.isWaitToRedial()) {
                this.mCi.dial(address, isEmergencyCall, emergencyNumberInfo, hasKnownUserIntentEmergency, clirMode, uusInfo, obtainCompleteMessage());
            } else {
                this.mWaitForHoldToRedialRequest.setToRedial(address, isEmergencyCall, emergencyNumberInfo, hasKnownUserIntentEmergency, clirMode, uusInfo);
            }
        }
    }

    protected void onSrvccStarted() {
        if (!hasParsingCEPCapability()) {
            for (Connection conn : this.mHandoverConnections) {
                if (conn.isMultiparty() && (conn instanceof MtkImsPhoneConnection) && conn.isConferenceHost()) {
                    log("srvcc: mNeedWaitImsEConfSrvcc set True");
                    this.mNeedWaitImsEConfSrvcc = true;
                    this.mImsConfHostConnection = conn;
                }
            }
        }
    }

    protected void onCallStateOffhook() {
        if (this.mState == PhoneConstants.State.IDLE) {
            ArrayList<String> info = new ArrayList<>();
            info.add(RadioCapabilitySwitchUtil.IMSI_READY);
            this.mMtkCi.getMtkRadioExIndication().callAdditionalInfoInd(0, 4, info);
        }
    }

    protected void onCallStateIdle(Phone imsPhone) {
        if (this.mState == PhoneConstants.State.IDLE && imsPhone != null) {
            imsPhone.callEndCleanupHandOverCallIfAny();
        }
    }

    protected void onSwitchWaitingOrHoldingAndActive() {
        if (!this.mHasPendingSwapRequest) {
            this.mWaitForHoldToHangupRequest.setToHangup();
            this.mCi.switchWaitingOrHoldingAndActive(obtainCompleteMessage(8));
            this.mHasPendingSwapRequest = true;
        }
    }

    protected void onHangupPendingMo() {
        updateWaitPollAfterAbortPendingMO(false);
    }

    protected boolean onCheckIfSendDisconnected() {
        return false;
    }

    protected boolean onCheckIfDisablePollCallAfterReset() {
        Phone imsPhone = this.mPhone.getImsPhone();
        if (imsPhone != null) {
            if (imsPhone != null && imsPhone.getHandoverConnection() == null) {
                return false;
            }
            Rlog.d("GsmCdmaCallTracker", "not trigger pollCall since imsCall exists");
            return true;
        }
        return false;
    }

    protected void onHangupRingCall(GsmCdmaCall call) throws CallStateException {
        GsmCdmaConnection conn = (GsmCdmaConnection) call.getConnections().get(0);
        if ((conn instanceof MtkGsmCdmaConnection) && ((MtkGsmCdmaConnection) conn).getRejectWithCause() != -1) {
            this.mMtkCi.hangupConnectionWithCause(conn.getGsmCdmaIndex(), ((MtkGsmCdmaConnection) conn).getRejectWithCause(), obtainCompleteMessage());
        } else {
            super.onHangupRingCall(call);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    protected void onHangupGsmForegroundWithRingCall(GsmCdmaCall call) throws CallStateException {
        onHangupForegroundActiveCall(call);
    }

    protected void onHangupForegroundActiveCall(GsmCdmaCall call) throws CallStateException {
        log("(foregnd) hangup active");
        if (isPhoneTypeGsm()) {
            GsmCdmaConnection cn = (GsmCdmaConnection) call.getConnections().get(0);
            String address = cn.getAddress();
            boolean isEmergencyCall = TelephonyManager.getDefault().isEmergencyNumber(address);
            if (isEmergencyCall) {
                Rlog.d(PROP_LOG_TAG, "(foregnd) hangup active ECC call by connection index");
                hangup((GsmCdmaConnection) call.getConnections().get(0));
                return;
            } else if (!this.mWaitForHoldToHangupRequest.isWaitToHangup()) {
                hangupForegroundResumeBackground();
                return;
            } else {
                this.mWaitForHoldToHangupRequest.setToHangup(call);
                return;
            }
        }
        super.onHangupForegroundActiveCall(call);
    }

    protected void onHangupBackground(GsmCdmaCall call) {
        if (!this.mWaitForHoldToHangupRequest.isWaitToHangup()) {
            super.onHangupBackground(call);
        } else {
            this.mWaitForHoldToHangupRequest.setToHangup(call);
        }
    }

    protected void onHandlePollCallsStart() {
        if (this.mWaitPollAfterHangupPendingMO) {
            updateWaitPollAfterAbortPendingMO(false);
        }
    }

    protected boolean onGetCallListDone(List polledCalls) {
        if (polledCalls.size() == 0) {
            this.mHelper.setGwsdCall(false);
        }
        if (!this.mNeedWaitImsEConfSrvcc || hasParsingCEPCapability()) {
            return false;
        }
        Rlog.d(PROP_LOG_TAG, "SRVCC: +ECONFSRVCC is still not arrival, skip this poll call.");
        return true;
    }

    protected boolean onCheckIfIgnoreInternalClearDisconnected() {
        return this.mHasPendingSwapRequest;
    }

    protected void onBeforeDisconnectPendingHandOverConnection() {
        MtkImsPhoneConnection mtkImsPhoneConnection = this.mImsConfHostConnection;
        if (mtkImsPhoneConnection != null) {
            MtkImsPhoneConnection hostConn = mtkImsPhoneConnection;
            if (this.mImsConfParticipants.size() >= 2) {
                restoreConferenceParticipantAddress();
                Rlog.d(PROP_LOG_TAG, "SRVCC: notify new participant connections");
                hostConn.notifyConferenceConnectionsConfigured(this.mImsConfParticipants);
            } else if (this.mImsConfParticipants.size() == 1) {
                GsmCdmaConnection participant = this.mImsConfParticipants.get(0);
                String address = hostConn.getConferenceParticipantAddress(0);
                Rlog.d(PROP_LOG_TAG, "SRVCC: restore participant connection with address: " + Rlog.pii(PROP_LOG_TAG, address));
                if (participant instanceof MtkGsmCdmaConnection) {
                    ((MtkGsmCdmaConnection) participant).updateConferenceParticipantAddress(address);
                }
                Rlog.d(PROP_LOG_TAG, "SRVCC: only one connection, consider it as a normal call SRVCC");
                this.mPhone.notifyHandoverStateChanged(participant);
            } else {
                Rlog.e(PROP_LOG_TAG, "SRVCC: abnormal case, no participant connections.");
            }
            this.mImsConfParticipants.clear();
            this.mImsConfHostConnection = null;
            this.mEconfSrvccConnectionIds = null;
        }
    }

    protected boolean onHandleDroppedConnectionDuringPoll(Throwable e, int i) {
        if (isCommandExceptionRadioNotAvailable(e)) {
            this.mDroppedDuringPoll.remove(i);
            return true;
        }
        return super.onHandleDroppedConnectionDuringPoll(e, i);
    }

    protected boolean onSetHasAnyCallDisconnected(Throwable e, GsmCdmaConnection conn, boolean hasAnyCallDisconnected) {
        if (isCommandExceptionRadioNotAvailable(e)) {
            return hasAnyCallDisconnected | conn.onDisconnect(14);
        }
        return super.onSetHasAnyCallDisconnected(e, conn, hasAnyCallDisconnected);
    }

    protected boolean onSetWasDisconnected(Throwable e, boolean wasDisconnected) {
        if (isCommandExceptionRadioNotAvailable(e)) {
            return true;
        }
        return super.onSetWasDisconnected(e, wasDisconnected);
    }

    protected void onBeforeNotifyNewRing(List polledCalls) {
        if (polledCalls.size() == 0 && this.mConnections.length == 0) {
            log("check whether fgCall or ringCall have mConnections");
            if (!isPhoneTypeGsm()) {
                int count = this.mForegroundCall.mConnections.size();
                for (int n = 0; n < count; n++) {
                    log("adding fgCall cn " + n + " to droppedDuringPoll");
                    GsmCdmaConnection cn = (GsmCdmaConnection) this.mForegroundCall.mConnections.get(n);
                    this.mDroppedDuringPoll.add(cn);
                }
                int count2 = this.mRingingCall.mConnections.size();
                for (int n2 = 0; n2 < count2; n2++) {
                    log("adding rgCall cn " + n2 + " to droppedDuringPoll");
                    GsmCdmaConnection cn2 = (GsmCdmaConnection) this.mRingingCall.mConnections.get(n2);
                    this.mDroppedDuringPoll.add(cn2);
                }
            }
        }
    }

    protected void onPendingMoDroppedDuringPoll() {
        updateWaitPollAfterAbortPendingMO(false);
        if (this.mPendingCallInEcm) {
            this.mPendingCallInEcm = false;
        }
        if (this.mPhone.isEcmCanceledForEmergency()) {
            this.mPhone.handleTimerInEmergencyCallbackMode(0);
        }
    }

    protected void onCdmaMoMtCallConflict(DriverCall dc, GsmCdmaCallTracker ct, int index) {
        this.mConnections[index] = onCreateGsmCdmaConnection(this.mPhone, dc, ct, index);
    }

    protected void onCreateGsmCdmaConnectionForMismatch(DriverCall dc, GsmCdmaCallTracker ct, int index) {
        if (this.mPendingMO != null && this.mPendingMO.compareTo(dc)) {
            Rlog.d(PROP_LOG_TAG, "ringing disc not updated yet & replaced by pendingMo");
            this.mConnections[index] = this.mPendingMO;
            this.mPendingMO.mIndex = index;
            this.mPendingMO.update(dc);
            this.mPendingMO = null;
            return;
        }
        this.mConnections[index] = new MtkGsmCdmaConnection(this.mPhone, dc, this, index);
    }

    protected void onGsmConnectionDropped(GsmCdmaConnection conn, int index) {
        if (((conn.getCall() == this.mForegroundCall && this.mForegroundCall.mConnections.size() == 1 && this.mBackgroundCall.isIdle()) || (conn.getCall() == this.mBackgroundCall && this.mBackgroundCall.mConnections.size() == 1 && this.mForegroundCall.isIdle())) && this.mRingingCall.getState() == Call.State.WAITING) {
            this.mRingingCall.mState = Call.State.INCOMING;
        }
        if (this.mPhone.isEcmCanceledForEmergency()) {
            this.mPhone.handleTimerInEmergencyCallbackMode(0);
        }
        this.mConnections[index] = null;
        this.mHelper.CallIndicationEnd();
    }

    protected void onAfterNotifyHandoverStateChanged(int index) {
        this.mConnections[index].onConnectionEvent("android.telecom.event.CALL_REMOTELY_UNHELD", (Bundle) null);
    }

    protected void onBeforeNotifyHandoverStateChanged() {
        if (this.mIsInEmergencyCall && !this.mPhone.isEcmCanceledForEmergency() && this.mPhone.isInEcm()) {
            Rlog.i("GsmCdmaCallTracker", "Ecm timer has been canceled in IMS, so set setEcmCanceledForEmergency(true) directly");
            this.mPhone.setEcmCanceledForEmergency(true);
        }
    }

    protected boolean onCheckHoConnection(Connection conn, int index) {
        if ((conn instanceof MtkImsPhoneConnection) && ((MtkImsPhoneConnection) conn).isMultipartyBeforeHandover() && ((MtkImsPhoneConnection) conn).isConfHostBeforeHandover() && !hasParsingCEPCapability()) {
            Rlog.i("GsmCdmaCallTracker", "SRVCC: goes to conference case.");
            this.mConnections[index].mOrigConnection = conn;
            this.mImsConfParticipants.add(this.mConnections[index]);
            return true;
        }
        return false;
    }

    protected void onDoHangupPendingMo() {
        if (isPhoneTypeGsm() && this.mPhone.isEcmCanceledForEmergency()) {
            this.mPhone.handleTimerInEmergencyCallbackMode(0);
        }
        updateWaitPollAfterAbortPendingMO(true);
    }

    protected void onDoHandupPendingMoException() {
        updateWaitPollAfterAbortPendingMO(false);
    }
}
