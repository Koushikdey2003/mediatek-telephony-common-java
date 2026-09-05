package com.mediatek.internal.telephony.rsu;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.uicc.IMtkRsuSml;

/* JADX INFO: loaded from: classes.dex */
public class RsuSml extends IMtkRsuSml {
    private static final int FAIL_MD = -3;
    private static final int RSU_MESSAGE_GET_LOCK_STATUS = 100;
    private static final String TAG = "RsuSml";
    private MtkRIL mCi;
    private Context mContext;
    private Handler mUrcHandler;
    private HandlerThread mWorker;

    public RsuSml(Context context, CommandsInterface[] ci) {
        this.mWorker = null;
        this.mUrcHandler = null;
        logi("initialize RsuSml: " + ci);
        this.mContext = context;
        this.mCi = (MtkRIL) ci[0];
        HandlerThread handlerThread = new HandlerThread("RsuWorker");
        this.mWorker = handlerThread;
        handlerThread.start();
        this.mUrcHandler = new Handler(this.mWorker.getLooper()) { // from class: com.mediatek.internal.telephony.rsu.RsuSml.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                RsuSml.this.logd("handleMessage what = " + msg.what);
                switch (msg.what) {
                    case 100:
                        RsuResponseData rri = (RsuResponseData) ar.result;
                        Message cb = (Message) ar.userObj;
                        if (ar.exception != null) {
                            AsyncResult.forMessage(cb).exception = ar.exception;
                        } else {
                            int status = -3;
                            if (rri != null) {
                                status = rri.status;
                                RsuSml.this.logi("RSU_MESSAGE_GET_LOCK_STATUS: " + status);
                            }
                            AsyncResult.forMessage(cb, Integer.valueOf(status), (Throwable) null);
                        }
                        cb.sendToTarget();
                        break;
                    default:
                        RsuSml.this.logi("Unknown urc or urc that need not handle");
                        break;
                }
            }
        };
    }

    @Override // com.mediatek.internal.telephony.uicc.IMtkRsuSml
    public int remoteSimlockGetSimlockStatusEx(Object cb) {
        logd("remoteSimlockGetSimlockStatusEx mCi = " + this.mCi);
        if (this.mCi != null) {
            RsuRequestData rri = new RsuRequestData();
            rri.opId = 200;
            rri.requestId = 4;
            rri.data = "";
            rri.reserveString1 = "";
            this.mCi.sendRsuRequest(rri, this.mUrcHandler.obtainMessage(100, 0, 0, cb));
            return 0;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String s) {
        Rlog.d(TAG, "[RSU-SIMLOCK] " + s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String s) {
        Rlog.i(TAG, "[RSU-SIMLOCK] " + s);
    }
}
