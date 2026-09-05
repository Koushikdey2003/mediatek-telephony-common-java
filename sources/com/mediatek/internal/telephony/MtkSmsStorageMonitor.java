package com.mediatek.internal.telephony;

import android.content.Intent;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsStorageMonitor;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsStorageMonitor extends SmsStorageMonitor {
    private static final int EVENT_ME_FULL = 100;
    private static final String TAG = "MtkSmsStorageMonitor";

    /* JADX WARN: Multi-variable type inference failed */
    public MtkSmsStorageMonitor(Phone phone) {
        super(phone);
        if (this.mCi != null && (this.mCi instanceof MtkRIL)) {
            MtkRIL ci = this.mCi;
            ci.setOnMeSmsFull(this, 100, null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        Rlog.d(TAG, "disposed...");
        if (this.mCi != null && (this.mCi instanceof MtkRIL)) {
            MtkRIL ci = this.mCi;
            ci.unSetOnMeSmsFull(this);
        }
        super.dispose();
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 100:
                handleMeFull();
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private void handleMeFull() {
        Intent intent = new Intent("android.provider.Telephony.SMS_REJECTED");
        intent.putExtra("result", 3);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        this.mWakeLock.acquire(5000L);
        this.mContext.sendBroadcast(intent, "android.permission.RECEIVE_SMS");
    }
}
