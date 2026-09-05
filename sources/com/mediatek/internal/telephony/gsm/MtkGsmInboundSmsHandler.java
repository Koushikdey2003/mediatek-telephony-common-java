package com.mediatek.internal.telephony.gsm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.Rlog;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.SmsMessage;
import com.mediatek.internal.telephony.MtkProxyController;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.MtkWapPushOverSms;
import com.mediatek.internal.telephony.util.MtkSmsCommonUtil;
import mediatek.telephony.MtkSmsMessage;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmInboundSmsHandler extends GsmInboundSmsHandler {
    private static final int EVENT_SMS_EINFO_EXTENSIONS = 8201;
    private static final int TN_VALIDATION_FAILED = 2;
    private static final int TN_VALIDATION_NOTHING = 0;
    private static final int TN_VALIDATION_PASSED = 1;
    private int mPAssertedIdentity;
    private String mTag;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmInboundSmsHandler(Context context, SmsStorageMonitor smsStorageMonitor, Phone phone, Looper looper) {
        super(context, smsStorageMonitor, phone, looper);
        this.mTag = "MtkGsmInboundSmsHandler";
        this.mPAssertedIdentity = 0;
        this.mDefaultState = new MtkDefaultState();
        this.mStartupState = new MtkStartupState();
        this.mIdleState = new MtkIdleState();
        this.mDeliveringState = new MtkDeliveringState();
        this.mWaitingState = new MtkWaitingState();
        addState(this.mDefaultState);
        addState(this.mStartupState, this.mDefaultState);
        addState(this.mIdleState, this.mDefaultState);
        addState(this.mDeliveringState, this.mDefaultState);
        addState(this.mWaitingState, this.mDeliveringState);
        setInitialState(this.mStartupState);
        this.mTag = "MtkGsmInboundSmsHandler-" + phone.getPhoneId();
        log("created InboundSmsHandler from MtkGsmInboundSmsHandler");
        phone.mCi.registerForSmsInfoExt(getHandler(), EVENT_SMS_EINFO_EXTENSIONS, null);
    }

    public static MtkGsmInboundSmsHandler makeInboundSmsHandler(Context context, SmsStorageMonitor storageMonitor, Phone phone, Looper looper) {
        MtkGsmInboundSmsHandler handler = new MtkGsmInboundSmsHandler(context, storageMonitor, phone, looper);
        handler.start();
        return handler;
    }

    private class MtkDefaultState extends InboundSmsHandler.DefaultState {
        private MtkDefaultState() {
            super(MtkGsmInboundSmsHandler.this);
        }

        public boolean processMessage(Message msg) {
            int i = msg.what;
            return super.processMessage(msg);
        }
    }

    private class MtkStartupState extends InboundSmsHandler.StartupState {
        private MtkStartupState() {
            super(MtkGsmInboundSmsHandler.this);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case MtkGsmInboundSmsHandler.EVENT_SMS_EINFO_EXTENSIONS /* 8201 */:
                    MtkGsmInboundSmsHandler.this.log("MtkStartupState.processMessage: EVENT_SMS_EINFO_EXTENSIONS");
                    MtkGsmInboundSmsHandler.this.deferMessage(msg);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }
    }

    private class MtkIdleState extends InboundSmsHandler.IdleState {
        private MtkIdleState() {
            super(MtkGsmInboundSmsHandler.this);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case MtkGsmInboundSmsHandler.EVENT_SMS_EINFO_EXTENSIONS /* 8201 */:
                    MtkGsmInboundSmsHandler.this.log("MtkIdleState.processMessage: EVENT_SMS_EINFO_EXTENSIONS");
                    MtkGsmInboundSmsHandler.this.handleSmsEInfoExtensions((AsyncResult) msg.obj);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }
    }

    private class MtkDeliveringState extends InboundSmsHandler.DeliveringState {
        private MtkDeliveringState() {
            super(MtkGsmInboundSmsHandler.this);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case MtkGsmInboundSmsHandler.EVENT_SMS_EINFO_EXTENSIONS /* 8201 */:
                    MtkGsmInboundSmsHandler.this.log("MtkDeliveringState.processMessage: EVENT_SMS_EINFO_EXTENSIONS");
                    MtkGsmInboundSmsHandler.this.deferMessage(msg);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }
    }

    private class MtkWaitingState extends InboundSmsHandler.WaitingState {
        private MtkWaitingState() {
            super(MtkGsmInboundSmsHandler.this);
        }

        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case MtkGsmInboundSmsHandler.EVENT_SMS_EINFO_EXTENSIONS /* 8201 */:
                    MtkGsmInboundSmsHandler.this.log("MtkWaitingState.processMessage: EVENT_SMS_EINFO_EXTENSIONS");
                    MtkGsmInboundSmsHandler.this.deferMessage(msg);
                    return true;
                default:
                    return super.processMessage(msg);
            }
        }
    }

    protected int dispatchMessageRadioSpecific(SmsMessageBase smsb, int smsSource, int token) {
        SmsMessage sms = (SmsMessage) smsb;
        if (sms.getDisplayOriginatingAddress() != null && sms.getDisplayOriginatingAddress().equals("10659401")) {
            log("handleAutoRegMessage.");
            handleAutoRegMessage(sms.getPdu());
            return 1;
        }
        return super.dispatchMessageRadioSpecific(smsb, smsSource, token);
    }

    private void handleAutoRegMessage(byte[] pdu) {
        ((MtkProxyController) ProxyController.getInstance()).getDeviceRegisterController().handleAutoRegMessage(this.mPhone.getSubId(), "3gpp", pdu);
    }

    protected void onQuitting() {
        super.onQuitting();
        MtkRIL ci = this.mPhone.mCi;
        ci.unregisterForSmsInfoExt(getHandler());
    }

    public void dispatchIntent(Intent intent, String permission, String appOp, Bundle opts, InboundSmsHandler.SmsBroadcastReceiver resultReceiver, UserHandle user, int subId) {
        intent.putExtra("rTime", System.currentTimeMillis());
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_DELIVER".equals(action) && this.mPAssertedIdentity != 0) {
            log("dispatchIntent with caller_verification=" + this.mPAssertedIdentity);
            intent.putExtra("caller_verification", this.mPAssertedIdentity);
            this.mPAssertedIdentity = 0;
        }
        super.dispatchIntent(intent, permission, appOp, opts, resultReceiver, user, subId);
    }

    protected void deleteFromRawTable(String deleteWhere, String[] deleteWhereArgs, int deleteType) {
        if (deleteType == 1) {
            Uri uri = sRawUriPermanentDelete;
        } else {
            Uri uri2 = sRawUri;
        }
        if (deleteWhere == null && deleteWhereArgs == null) {
            loge("No rows need be deleted from raw table!");
        } else {
            super.deleteFromRawTable(deleteWhere, deleteWhereArgs, deleteType);
        }
    }

    protected String[] onModifyQueryWhereArgs(String[] whereArgs) {
        String subId = Integer.toString(this.mPhone.getSubId());
        return new String[]{whereArgs[0], whereArgs[1], whereArgs[2], subId};
    }

    protected android.telephony.SmsMessage onCreateSmsMessage(byte[] pdu, String format) {
        return MtkSmsMessage.createFromPdu(pdu, "3gpp");
    }

    protected int onDispatchWapPdu(byte[][] smsPdus, byte[] pdu, InboundSmsHandler.SmsBroadcastReceiver receiver, String address, int subId, long messageId) {
        String sca;
        String sca2;
        if (MtkSmsCommonUtil.isWapPushSupport()) {
            log("dispatch wap push pdu with addr & sc addr");
            MtkSmsMessage sms = MtkSmsMessage.createFromPdu(smsPdus[0], "3gpp");
            if (sms != null && (sca2 = sms.getServiceCenterAddress()) != null) {
                sca = sca2;
            } else {
                sca = "";
            }
            return ((MtkWapPushOverSms) this.mWapPush).dispatchWapPdu(pdu, receiver, this, address, sca, subId, messageId);
        }
        return super.onDispatchWapPdu(smsPdus, pdu, receiver, address, subId, messageId);
    }

    protected boolean onCheckIfOverrideStates() {
        return true;
    }

    protected void log(String s) {
        Rlog.d(this.mTag, s);
    }

    protected void loge(String s) {
        Rlog.e(this.mTag, s);
    }

    protected void loge(String s, Throwable e) {
        Rlog.e(this.mTag, s, e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSmsEInfoExtensions(AsyncResult ar) {
        if (ar.exception != null) {
            loge("Exception processing handleSmsEInfoExtensions: " + ar.exception);
            return;
        }
        try {
            String info = (String) ar.result;
            if (info != null && !info.isEmpty()) {
                if (info.contains("verstat=TN-Validation-Passed")) {
                    this.mPAssertedIdentity = 1;
                } else if (info.contains("verstat=TN-Validation-Failed")) {
                    this.mPAssertedIdentity = 2;
                }
            }
        } catch (RuntimeException ex) {
            loge("Exception dispatching message", ex);
        }
    }
}
