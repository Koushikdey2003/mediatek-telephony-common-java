package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Process;
import android.provider.CallLog;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.MtkIncomingCallChecker;
import java.util.Date;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class MtkGsmCdmaCallTrackerHelper implements MtkIncomingCallChecker.OnCheckCompleteListener {
    protected static final int EVENT_CALL_STATE_CHANGE = 2;
    protected static final int EVENT_CALL_WAITING_INFO_CDMA = 15;
    protected static final int EVENT_CONFERENCE_RESULT = 11;
    protected static final int EVENT_DIAL_CALL_RESULT = 1002;
    protected static final int EVENT_ECT_RESULT = 13;
    protected static final int EVENT_EXIT_ECM_RESPONSE_CDMA = 14;
    protected static final int EVENT_GET_LAST_CALL_FAIL_CAUSE = 5;
    protected static final int EVENT_HANG_UP_RESULT = 1003;
    protected static final int EVENT_INCOMING_CALL_INDICATION = 1000;
    protected static final int EVENT_MTK_BASE = 1000;
    protected static final int EVENT_OPERATION_COMPLETE = 4;
    protected static final int EVENT_POLL_CALLS_RESULT = 1;
    protected static final int EVENT_RADIO_AVAILABLE = 9;
    protected static final int EVENT_RADIO_NOT_AVAILABLE = 10;
    protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 1001;
    protected static final int EVENT_REPOLL_AFTER_DELAY = 3;
    protected static final int EVENT_SEPARATE_RESULT = 12;
    protected static final int EVENT_SWITCH_RESULT = 8;
    protected static final int EVENT_THREE_WAY_DIAL_BLANK_FLASH = 20;
    protected static final int EVENT_THREE_WAY_DIAL_L2_RESULT_CDMA = 16;
    static final String LOG_TAG = "GsmCallTkrHlpr";
    private static final int MT_CALL_GWSD = 10;
    private static final int MT_CALL_MISSED = 2;
    private static final int MT_CALL_REJECTED = 1;
    private Context mContext;
    private MtkIncomingCallChecker mIncomingCallChecker = null;
    private boolean mIsGwsdCall = false;
    private MtkGsmCdmaCallTracker mMtkTracker;

    public MtkGsmCdmaCallTrackerHelper(Context context, MtkGsmCdmaCallTracker tracker) {
        this.mContext = context;
        this.mMtkTracker = tracker;
    }

    void logD(String msg) {
        Rlog.d(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    void logI(String msg) {
        Rlog.i(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    void logW(String msg) {
        Rlog.w(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    void logE(String msg) {
        Rlog.e(LOG_TAG, msg + " (slot " + this.mMtkTracker.mPhone.getPhoneId() + ")");
    }

    public void LogerMessage(int msgType) {
        switch (msgType) {
            case 1:
                logD("handle EVENT_POLL_CALLS_RESULT");
                break;
            case 2:
                logD("handle EVENT_CALL_STATE_CHANGE");
                break;
            case 3:
                logD("handle EVENT_REPOLL_AFTER_DELAY");
                break;
            case 4:
                logD("handle EVENT_OPERATION_COMPLETE");
                break;
            case 5:
                logD("handle EVENT_GET_LAST_CALL_FAIL_CAUSE");
                break;
            case 8:
                logD("handle EVENT_SWITCH_RESULT");
                break;
            case 9:
                logD("handle EVENT_RADIO_AVAILABLE");
                break;
            case 10:
                logD("handle EVENT_RADIO_NOT_AVAILABLE");
                break;
            case 11:
                logD("handle EVENT_CONFERENCE_RESULT");
                break;
            case 12:
                logD("handle EVENT_SEPARATE_RESULT");
                break;
            case 13:
                logD("handle EVENT_ECT_RESULT");
                break;
            case 1000:
                logD("handle EVENT_INCOMING_CALL_INDICATION");
                break;
            case 1001:
                logD("handle EVENT_RADIO_OFF_OR_NOT_AVAILABLE");
                break;
            case 1002:
                logD("handle EVENT_DIAL_CALL_RESULT");
                break;
            case 1003:
                logD("handle EVENT_HANG_UP_RESULT");
                break;
            default:
                logD("handle XXXXX");
                break;
        }
    }

    public void CallIndicationProcess(AsyncResult ar) {
        CallIndicationProcess(ar, false, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0212  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void CallIndicationProcess(android.os.AsyncResult r26, boolean r27, boolean r28) {
        /*
            Method dump skipped, instruction units count: 543
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.MtkGsmCdmaCallTrackerHelper.CallIndicationProcess(android.os.AsyncResult, boolean, boolean):void");
    }

    public void CallIndicationEnd() {
        int pid = Process.myPid();
        if (Process.getThreadPriority(pid) != 0) {
            Process.setThreadPriority(pid, 0);
            logD("Current priority = " + Process.getThreadPriority(pid));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkIncomingCallChecker.OnCheckCompleteListener
    public void onCheckComplete(boolean result, Object obj) {
        CallIndicationProcess(obj != null ? (AsyncResult) obj : null, true, result);
    }

    private void addCallLog(Context context, String iccId, String number, int type, long missedReason) {
        String number2;
        int presentationMode;
        PhoneAccountHandle phoneAccountHandle = null;
        TelecomManager telecomManager = TelecomManager.from(context);
        Iterator<PhoneAccountHandle> phoneAccounts = telecomManager.getCallCapablePhoneAccounts().listIterator();
        while (true) {
            if (!phoneAccounts.hasNext()) {
                break;
            }
            PhoneAccountHandle handle = phoneAccounts.next();
            String id = handle.getId();
            if (id != null && id.equals(iccId)) {
                phoneAccountHandle = handle;
                break;
            }
        }
        if (number != null) {
            number2 = number;
        } else {
            number2 = "";
        }
        if (number2 == null || number2.equals("")) {
            presentationMode = 2;
        } else {
            presentationMode = 1;
        }
        CallLog.Calls.addCall(null, context, number2, presentationMode, type, 0, phoneAccountHandle, new Date().getTime(), 0, new Long(0L), missedReason, 0);
    }

    public void handleCallAdditionalInfo(AsyncResult ar) {
        String[] callAdditionalInfo = (String[]) ar.result;
        int type = Integer.parseInt(callAdditionalInfo[0]);
        if (type != 2 && type != 1 && type != 10) {
            logD("handleCallAdditionalInfo not handle event");
        }
        String number = callAdditionalInfo[1];
        int callMode = Integer.parseInt(callAdditionalInfo[2]);
        logD("handleCallAdditionalInfo type:" + type + " mode:" + callMode);
        switch (type) {
            case 1:
                addCallLog(this.mContext, this.mMtkTracker.mPhone.getIccSerialNumber(), number, 5, 0L);
                break;
            case 2:
                addCallLog(this.mContext, this.mMtkTracker.mPhone.getIccSerialNumber(), number, 3, 65536L);
                break;
            case 10:
                this.mIsGwsdCall = true;
                break;
        }
    }

    public boolean isGwsdCall() {
        return this.mIsGwsdCall;
    }

    public void setGwsdCall(boolean isGwsd) {
        this.mIsGwsdCall = isGwsd;
    }
}
