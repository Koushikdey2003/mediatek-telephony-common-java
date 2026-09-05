package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemClock;
import android.os.WorkSource;
import com.android.internal.telephony.CommandException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: SmsRIL.java */
/* JADX INFO: loaded from: classes.dex */
class RILRequest {
    private static final int MAX_POOL_SIZE = 4;
    String mClientId;
    RILRequest mNext;
    int mRequest;
    Message mResult;
    int mSerial;
    long mStartTimeMs;
    int mWakeLockType;
    WorkSource mWorkSource;
    private static Random sRandom = new Random();
    private static AtomicInteger sNextSerial = new AtomicInteger(0);
    private static Object sPoolSync = new Object();
    private static RILRequest sPool = null;
    private static int sPoolSize = 0;

    RILRequest() {
    }

    static RILRequest obtain(int request, Message result) {
        RILRequest rr = null;
        synchronized (sPoolSync) {
            RILRequest rILRequest = sPool;
            if (rILRequest != null) {
                rr = rILRequest;
                sPool = rr.mNext;
                rr.mNext = null;
                sPoolSize--;
            }
        }
        if (rr == null) {
            rr = new RILRequest();
        }
        rr.mSerial = sNextSerial.getAndIncrement();
        rr.mRequest = request;
        rr.mResult = result;
        rr.mWakeLockType = -1;
        rr.mWorkSource = null;
        rr.mStartTimeMs = SystemClock.elapsedRealtime();
        if (result != null && result.getTarget() == null) {
            throw new NullPointerException("Message target must not be null");
        }
        return rr;
    }

    static RILRequest obtain(int request, Message result, WorkSource workSource) {
        RILRequest rr = obtain(request, result);
        if (workSource != null) {
            rr.mWorkSource = workSource;
            rr.mClientId = String.valueOf(workSource.get(0)) + ":" + workSource.getName(0);
        }
        return rr;
    }

    void release() {
        synchronized (sPoolSync) {
            int i = sPoolSize;
            if (i < 4) {
                this.mNext = sPool;
                sPool = this;
                sPoolSize = i + 1;
                this.mResult = null;
            }
        }
    }

    static void resetSerial() {
        sNextSerial.set(sRandom.nextInt());
    }

    String serialString() {
        StringBuilder sb = new StringBuilder(8);
        long adjustedSerial = (((long) this.mSerial) - (-2147483648L)) % 10000;
        String sn = Long.toString(adjustedSerial);
        sb.append('[');
        int s = sn.length();
        for (int i = 0; i < 4 - s; i++) {
            sb.append('0');
        }
        sb.append(sn);
        sb.append(']');
        return sb.toString();
    }

    void onError(int error, Object ret) {
        CommandException ex = CommandException.fromRilErrno(error);
        Message message = this.mResult;
        if (message != null) {
            AsyncResult.forMessage(message, ret, ex);
            this.mResult.sendToTarget();
        }
    }
}
