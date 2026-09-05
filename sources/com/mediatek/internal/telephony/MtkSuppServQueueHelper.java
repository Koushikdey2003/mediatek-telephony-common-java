package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import java.util.Vector;

/* JADX INFO: loaded from: classes.dex */
public class MtkSuppServQueueHelper {
    private static final boolean DBG = true;
    private static final int EVENT_SS_RESPONSE = 2;
    private static final int EVENT_SS_SEND = 1;
    public static final String LOG_TAG = "SuppServQueueHelper";
    private static MtkSuppServQueueHelper instance = null;
    private static Object pausedSync = new Object();
    private Context mContext;
    private SuppServQueueHelperHandler mHandler;
    private Phone[] mPhones;

    private MtkSuppServQueueHelper(Context context, Phone[] phones) {
        this.mContext = context;
        this.mPhones = phones;
    }

    public static MtkSuppServQueueHelper makeSuppServQueueHelper(Context context, Phone[] phones) {
        if (context == null || phones == null) {
            return null;
        }
        MtkSuppServQueueHelper mtkSuppServQueueHelper = instance;
        if (mtkSuppServQueueHelper == null) {
            Rlog.d(LOG_TAG, "Create MtkSuppServQueueHelper singleton instance, phones.length = " + phones.length);
            instance = new MtkSuppServQueueHelper(context, phones);
        } else {
            mtkSuppServQueueHelper.mContext = context;
            mtkSuppServQueueHelper.mPhones = phones;
        }
        return instance;
    }

    public void init(Looper looper) {
        Rlog.d(LOG_TAG, "Initialize SuppServQueueHelper!");
        this.mHandler = new SuppServQueueHelperHandler(looper);
    }

    public void dispose() {
        Rlog.d(LOG_TAG, "dispose.");
    }

    class SuppServQueueHelperHandler extends Handler implements Runnable {
        private static final int QUERY_OVER_GSM = 0;
        private static final int QUERY_OVER_GSM_OVER_UT = 1;
        private static final int QUERY_OVER_IMS = 2;
        private boolean paused;
        private Vector<Message> requestBuffer;

        public SuppServQueueHelperHandler(Looper looper) {
            super(looper);
            this.requestBuffer = new Vector<>();
            this.paused = false;
        }

        @Override // java.lang.Runnable
        public void run() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "handleMessage(), msg.what = " + msg.what + " , paused = " + this.paused);
            switch (msg.what) {
                case 1:
                    synchronized (MtkSuppServQueueHelper.pausedSync) {
                        if (this.paused) {
                            Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "A SS request ongoing, add it into the queue");
                            Message msgCopy = new Message();
                            msgCopy.copyFrom(msg);
                            this.requestBuffer.add(msgCopy);
                        } else {
                            processRequest(msg.obj, msg.arg1);
                            this.paused = MtkSuppServQueueHelper.DBG;
                        }
                        break;
                    }
                    return;
                case 2:
                    synchronized (MtkSuppServQueueHelper.pausedSync) {
                        processResponse(msg.obj);
                        this.paused = false;
                        if (this.requestBuffer.size() > 0) {
                            Message request = this.requestBuffer.elementAt(0);
                            this.requestBuffer.removeElementAt(0);
                            sendMessage(request);
                        }
                        break;
                    }
                    return;
                default:
                    Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "handleMessage(), msg.what must be SEND or RESPONSE");
                    return;
            }
        }

        private void processRequest(Object obj, int phoneId) {
            boolean enable;
            MtkSuppSrvRequest ss = (MtkSuppSrvRequest) obj;
            ss.mParcel.setDataPosition(0);
            Message respCallback = MtkSuppServQueueHelper.this.mHandler.obtainMessage(2, ss);
            Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "processRequest(), ss.mRequestCode = " + ss.mRequestCode + ", ss.mResultCallback = " + ss.mResultCallback + ", phoneId = " + phoneId);
            switch (ss.mRequestCode) {
                case 3:
                    int clirMode = ss.mParcel.readInt();
                    MtkSuppServQueueHelper.this.mPhones[phoneId].setOutgoingCallerIdDisplayInternal(clirMode, respCallback);
                    break;
                case 4:
                    MtkSuppServQueueHelper.this.mPhones[phoneId].getOutgoingCallerIdDisplayInternal(respCallback);
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 15:
                case 16:
                case 17:
                default:
                    Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "processRequest(), no match mRequestCode");
                    break;
                case 9:
                    String facility = ss.mParcel.readString();
                    boolean lockState = ss.mParcel.readInt() == 1;
                    String password = ss.mParcel.readString();
                    int serviceClass = ss.mParcel.readInt();
                    MtkSuppServQueueHelper.this.mPhones[phoneId].setCallBarringInternal(facility, lockState, password, respCallback, serviceClass);
                    break;
                case 10:
                    String facility2 = ss.mParcel.readString();
                    String password2 = ss.mParcel.readString();
                    int serviceClass2 = ss.mParcel.readInt();
                    MtkSuppServQueueHelper.this.mPhones[phoneId].getCallBarringInternal(facility2, password2, respCallback, serviceClass2);
                    break;
                case 11:
                    int action = ss.mParcel.readInt();
                    int cfReason = ss.mParcel.readInt();
                    String number = ss.mParcel.readString();
                    int timerSeconds = ss.mParcel.readInt();
                    int serviceClass3 = ss.mParcel.readInt();
                    MtkSuppServQueueHelper.this.mPhones[phoneId].setCallForwardingOptionInternal(action, cfReason, number, timerSeconds, serviceClass3, respCallback);
                    break;
                case 12:
                    int cfReason2 = ss.mParcel.readInt();
                    int serviceClass4 = ss.mParcel.readInt();
                    MtkSuppServQueueHelper.this.mPhones[phoneId].getCallForwardingOptionInternal(cfReason2, serviceClass4, respCallback);
                    break;
                case 13:
                    enable = ss.mParcel.readInt() == 1;
                    MtkSuppServQueueHelper.this.mPhones[phoneId].setCallWaitingInternal(enable, respCallback);
                    break;
                case 14:
                    MtkSuppServQueueHelper.this.mPhones[phoneId].getCallWaitingInternal(respCallback);
                    break;
                case 18:
                    int reason = ss.mParcel.readInt();
                    int withTimeSlot = ss.mParcel.readInt();
                    enable = withTimeSlot == 1;
                    queryCallForwardingOption(phoneId, reason, enable, respCallback);
                    break;
            }
        }

        private void processResponse(Object obj) {
            AsyncResult ar = (AsyncResult) obj;
            MtkSuppSrvRequest ss = (MtkSuppSrvRequest) ar.userObj;
            Message resp = ss.mResultCallback;
            Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "processResponse, resp = " + resp + " , ar.result = " + ar.result + " , ar.exception = " + ar.exception);
            if (resp != null) {
                AsyncResult.forMessage(resp, ar.result, ar.exception);
                resp.sendToTarget();
            }
            ss.setResultCallback(null);
            ss.mParcel.recycle();
        }

        public void queryCallForwardingOption(int phoneId, int reason, boolean withTimeSlot, Message respCallback) {
            Rlog.d(MtkSuppServQueueHelper.LOG_TAG, "queryCallForwardingOption, reason: " + reason + ", withTimeSlot: " + withTimeSlot);
            switch (reason) {
                case 0:
                    if (isVoiceInService((MtkGsmCdmaPhone) MtkSuppServQueueHelper.this.mPhones[phoneId])) {
                        MtkSuppServQueueHelper.this.mPhones[phoneId].mCi.queryCallForwardStatus(0, 1, (String) null, respCallback);
                    } else {
                        AsyncResult.forMessage(respCallback, (Object) null, new CommandException(CommandException.Error.GENERIC_FAILURE));
                        respCallback.sendToTarget();
                    }
                    break;
                case 1:
                    if (withTimeSlot) {
                        if (isMDSupportIMSSuppServ()) {
                            MtkSuppServQueueHelper.this.mPhones[phoneId].mMtkCi.queryCallForwardInTimeSlotStatus(0, 1, respCallback);
                        }
                    } else if (isMDSupportIMSSuppServ()) {
                        MtkSuppServQueueHelper.this.mPhones[phoneId].mCi.queryCallForwardStatus(0, 1, (String) null, respCallback);
                    }
                    break;
                case 2:
                    MtkImsPhone imsPhone = MtkSuppServQueueHelper.this.mPhones[phoneId].getImsPhone();
                    if (withTimeSlot) {
                        imsPhone.getCallForwardInTimeSlot(0, respCallback);
                    } else {
                        imsPhone.getCallForwardingOption(0, respCallback);
                    }
                    break;
            }
        }

        private boolean isVoiceInService(MtkGsmCdmaPhone phone) {
            if (phone.mSST != null && phone.mSST.mSS != null && phone.mSST.mSS.getState() == 0) {
                return MtkSuppServQueueHelper.DBG;
            }
            return false;
        }

        private boolean isMDSupportIMSSuppServ() {
            if (!SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
                return false;
            }
            return MtkSuppServQueueHelper.DBG;
        }
    }

    private void addRequest(MtkSuppSrvRequest ss, int phoneId) {
        SuppServQueueHelperHandler suppServQueueHelperHandler = this.mHandler;
        if (suppServQueueHelperHandler != null) {
            Message msg = suppServQueueHelperHandler.obtainMessage(1, phoneId, 0, ss);
            msg.sendToTarget();
        }
    }

    public void getCallForwardingOptionForServiceClass(int cfReason, int serviceClass, Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(12, response);
        ss.mParcel.writeInt(cfReason);
        ss.mParcel.writeInt(serviceClass);
        addRequest(ss, phoneId);
    }

    public void setCallForwardingOptionForServiceClass(int action, int cfReason, String number, int timeSeconds, int serviceClass, Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(11, response);
        ss.mParcel.writeInt(action);
        ss.mParcel.writeInt(cfReason);
        ss.mParcel.writeString(number);
        ss.mParcel.writeInt(timeSeconds);
        ss.mParcel.writeInt(serviceClass);
        addRequest(ss, phoneId);
    }

    public void getCallWaiting(Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(14, response);
        addRequest(ss, phoneId);
    }

    public void setCallWaiting(boolean z, Message message, int i) {
        MtkSuppSrvRequest mtkSuppSrvRequestObtain = MtkSuppSrvRequest.obtain(13, message);
        mtkSuppSrvRequestObtain.mParcel.writeInt(z ? 1 : 0);
        addRequest(mtkSuppSrvRequestObtain, i);
    }

    public void getCallBarring(String facility, String password, int serviceClass, Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(10, response);
        ss.mParcel.writeString(facility);
        ss.mParcel.writeString(password);
        ss.mParcel.writeInt(serviceClass);
        addRequest(ss, phoneId);
    }

    public void setCallBarring(String str, boolean z, String str2, int i, Message message, int i2) {
        MtkSuppSrvRequest mtkSuppSrvRequestObtain = MtkSuppSrvRequest.obtain(9, message);
        mtkSuppSrvRequestObtain.mParcel.writeString(str);
        mtkSuppSrvRequestObtain.mParcel.writeInt(z ? 1 : 0);
        mtkSuppSrvRequestObtain.mParcel.writeString(str2);
        mtkSuppSrvRequestObtain.mParcel.writeInt(i);
        addRequest(mtkSuppSrvRequestObtain, i2);
    }

    public void getOutgoingCallerIdDisplay(Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(4, response);
        addRequest(ss, phoneId);
    }

    public void setOutgoingCallerIdDisplay(int clirMode, Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(3, response);
        ss.mParcel.writeInt(clirMode);
        addRequest(ss, phoneId);
    }

    public void getCallForwardingOption(int reason, int withTimeSlot, Message response, int phoneId) {
        MtkSuppSrvRequest ss = MtkSuppSrvRequest.obtain(18, response);
        ss.mParcel.writeInt(reason);
        ss.mParcel.writeInt(withTimeSlot);
        addRequest(ss, phoneId);
    }
}
