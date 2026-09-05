package com.mediatek.internal.telephony;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsRawData;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.SmsUsageMonitor;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsDispatchersController extends SmsDispatchersController {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    protected static final int EVENT_SMS_READY = 0;
    private static final int FORMAT_CS_CDMA = 3;
    private static final int FORMAT_CS_GSM = 2;
    private static final int FORMAT_IMS = 1;
    private static final int FORMAT_NOT_MATCH = 0;
    public static final String SELECT_BY_REFERENCE = "address=? AND reference_number=? AND count=? AND deleted=0 AND sub_id=?";
    private static final String TAG = "MtkSmsDispatchersController";
    private static final int WAKE_LOCK_TIMEOUT = 500;
    private boolean mSmsReady;
    private PowerManager.WakeLock mWakeLock;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkSmsDispatchersController(Phone phone, SmsStorageMonitor storageMonitor, SmsUsageMonitor usageMonitor) {
        super(phone, storageMonitor, usageMonitor);
        this.mSmsReady = false;
        createWakelock();
        MtkRIL ci = this.mCi;
        ci.registerForSmsReady(this, 0, null);
        Rlog.d(TAG, "MtkSmsDispatchersController created");
        if (MtkSmsLocationHandler.getInstance() == null) {
            MtkSmsLocationHandler.init(this.mContext, getLooper());
        }
    }

    protected void sendData(String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (this.mImsSmsDispatcher.isAvailable()) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendData(callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendData(callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
        } else {
            this.mGsmDispatcher.sendData(callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
        }
    }

    protected void sendMultipartData(String callingPackage, String destAddr, String scAddr, int destPort, ArrayList<SmsRawData> data, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        if (this.mImsSmsDispatcher.isAvailable()) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendMultipartData(callingPackage, destAddr, scAddr, destPort, data, sentIntents, deliveryIntents);
        } else if (!isCdmaMo()) {
            this.mGsmDispatcher.sendMultipartData(callingPackage, destAddr, scAddr, destPort, data, sentIntents, deliveryIntents);
        }
    }

    public int copyTextMessageToIccCard(String scAddress, String address, List<String> text, int status, long timestamp) {
        if (this.mPhone.getPhoneType() == 2) {
            return this.mCdmaDispatcher.copyTextMessageToIccCard(scAddress, address, text, status, timestamp);
        }
        return this.mGsmDispatcher.copyTextMessageToIccCard(scAddress, address, text, status, timestamp);
    }

    protected void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (this.mImsSmsDispatcher.isAvailable()) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendTextWithEncodingType(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendTextWithEncodingType(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        } else {
            this.mGsmDispatcher.sendTextWithEncodingType(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        }
    }

    protected void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (this.mImsSmsDispatcher.isAvailable()) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendMultipartTextWithEncodingType(destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendMultipartTextWithEncodingType(destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        } else {
            this.mGsmDispatcher.sendMultipartTextWithEncodingType(destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        }
    }

    protected void handleIccFull() {
        if (!isCdmaMo()) {
            this.mGsmDispatcher.handleIccFull();
        }
    }

    protected void setSmsMemoryStatus(boolean status) {
        if (!isCdmaMo()) {
            this.mGsmDispatcher.setSmsMemoryStatus(status);
        }
    }

    public boolean isSmsReady() {
        return this.mSmsReady;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Rlog.d(TAG, "SMS is ready, Phone: " + this.mPhone.getPhoneId());
                this.mSmsReady = true;
                notifySmsReady(true);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private void createWakelock() {
        PowerManager pm = (PowerManager) this.mPhone.getContext().getSystemService("power");
        PowerManager.WakeLock wakeLockNewWakeLock = pm.newWakeLock(1, "SmsCommonEventHelp");
        this.mWakeLock = wakeLockNewWakeLock;
        wakeLockNewWakeLock.setReferenceCounted(true);
    }

    private void notifySmsReady(boolean isReady) {
        Intent intent = new Intent("android.provider.Telephony.SMS_STATE_CHANGED");
        intent.putExtra("ready", isReady);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        intent.addFlags(16777216);
        intent.setComponent(null);
        this.mWakeLock.acquire(500L);
        this.mPhone.getContext().sendBroadcast(intent);
    }
}
