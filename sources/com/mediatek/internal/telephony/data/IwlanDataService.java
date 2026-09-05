package com.mediatek.internal.telephony.data;

import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.DataService;
import android.telephony.data.DataServiceCallback;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.TrafficDescriptor;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class IwlanDataService extends DataService {
    private static final int APN_UNTHROTTLED = 7;
    private static final int CANCEL_HANDOVER = 6;
    private static final int DATA_CALL_LIST_CHANGED = 4;
    private static final boolean DBG = true;
    private static final int DEACTIVATE_DATA_ALL_COMPLETE = 2;
    private static final int REQUEST_DATA_CALL_LIST_COMPLETE = 3;
    private static final int SETUP_DATA_CALL_COMPLETE = 1;
    private static final int START_HANDOVER = 5;
    private static final String TAG = IwlanDataService.class.getSimpleName();

    private class IwlanDataServiceProvider extends DataService.DataServiceProvider {
        private final Map<Message, DataServiceCallback> mCallbackMap;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Looper mLooper;
        private final Phone mPhone;

        private IwlanDataServiceProvider(int slotId) {
            super(IwlanDataService.this, slotId);
            this.mCallbackMap = new HashMap();
            Phone phone = PhoneFactory.getPhone(getSlotIndex());
            this.mPhone = phone;
            HandlerThread handlerThread = new HandlerThread(IwlanDataService.class.getSimpleName());
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            this.mLooper = looper;
            Handler handler = new Handler(looper) { // from class: com.mediatek.internal.telephony.data.IwlanDataService.IwlanDataServiceProvider.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    DataServiceCallback callback = (DataServiceCallback) IwlanDataServiceProvider.this.mCallbackMap.remove(message);
                    AsyncResult ar = (AsyncResult) message.obj;
                    switch (message.what) {
                        case 1:
                            DataCallResponse response = (DataCallResponse) ar.result;
                            callback.onSetupDataCallComplete(ar.exception == null ? 0 : 4, response);
                            break;
                        case 2:
                            callback.onDeactivateDataCallComplete(ar.exception == null ? 0 : 4);
                            break;
                        case 3:
                            callback.onRequestDataCallListComplete(ar.exception == null ? 0 : 4, ar.exception != null ? null : (List) ar.result);
                            break;
                        case 4:
                            IwlanDataServiceProvider.this.notifyDataCallListChanged((List) ar.result);
                            break;
                        case 5:
                            callback.onHandoverStarted(IwlanDataServiceProvider.this.toResultCode(ar.exception));
                            break;
                        case 6:
                            callback.onHandoverCancelled(IwlanDataServiceProvider.this.toResultCode(ar.exception));
                            break;
                        case 7:
                            IwlanDataServiceProvider.this.notifyApnUnthrottled((String) ar.result);
                            break;
                        default:
                            IwlanDataService.this.loge("Unexpected event: " + message.what);
                            break;
                    }
                }
            };
            this.mHandler = handler;
            IwlanDataService.this.log("Register for data call list changed.");
            phone.mCi.registerForDataCallListChanged(handler, 4, (Object) null);
            IwlanDataService.this.log("Register for apn unthrottled.");
            phone.mCi.registerForApnUnthrottled(handler, 7, (Object) null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int toResultCode(Throwable t) {
            if (t == null) {
                return 0;
            }
            if (t instanceof CommandException) {
                CommandException ce = (CommandException) t;
                return ce.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED ? 1 : 4;
            }
            IwlanDataService.this.loge("Throwable is of type " + t.getClass().getSimpleName() + " but should be CommandException");
            return 4;
        }

        public void setupDataCall(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, int pduSessionId, NetworkSliceInfo sliceInfo, TrafficDescriptor trafficDescriptor, boolean matchAllRuleAllowed, DataServiceCallback callback) {
            IwlanDataService.this.log("setupDataCall " + getSlotIndex());
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 1);
                this.mCallbackMap.put(message, callback);
            }
            this.mPhone.mCi.setupDataCall(accessNetworkType, dataProfile, isRoaming, allowRoaming, reason, linkProperties, pduSessionId, sliceInfo, trafficDescriptor, matchAllRuleAllowed, message);
        }

        public void deactivateDataCall(int cid, int reason, DataServiceCallback callback) {
            IwlanDataService.this.log("deactivateDataCall " + getSlotIndex());
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 2);
                this.mCallbackMap.put(message, callback);
            }
            this.mPhone.mCi.deactivateDataCall(cid, reason, message);
        }

        public void requestDataCallList(DataServiceCallback callback) {
            IwlanDataService.this.log("requestDataCallList " + getSlotIndex());
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 3);
                this.mCallbackMap.put(message, callback);
            }
            this.mPhone.mCi.getDataCallList(message);
        }

        public void startHandover(int cid, DataServiceCallback callback) {
            IwlanDataService.this.log("startHandover " + getSlotIndex());
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 5);
                this.mCallbackMap.put(message, callback);
            }
            this.mPhone.mCi.startHandover(message, cid);
        }

        public void cancelHandover(int cid, DataServiceCallback callback) {
            Message message = null;
            if (callback != null) {
                message = Message.obtain(this.mHandler, 6);
                this.mCallbackMap.put(message, callback);
            }
            this.mPhone.mCi.cancelHandover(message, cid);
        }

        public void close() {
            this.mPhone.mCi.unregisterForDataCallListChanged(this.mHandler);
            this.mHandlerThread.quit();
        }
    }

    public DataService.DataServiceProvider onCreateDataServiceProvider(int slotIndex) {
        log("IWLAN data service created for slot " + slotIndex);
        if (!SubscriptionManager.isValidSlotIndex(slotIndex)) {
            loge("Tried to IWLAN data service with invalid slotId " + slotIndex);
            return null;
        }
        return new IwlanDataServiceProvider(slotIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String s) {
        Rlog.e(TAG, s);
    }
}
