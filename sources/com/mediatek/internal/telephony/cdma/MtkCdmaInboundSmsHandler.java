package com.mediatek.internal.telephony.cdma;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.UserHandle;
import android.telephony.Rlog;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.cdma.SmsMessage;

/* JADX INFO: loaded from: classes.dex */
public class MtkCdmaInboundSmsHandler extends CdmaInboundSmsHandler {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final boolean VDBG = false;
    private String mTag;

    public MtkCdmaInboundSmsHandler(Context context, SmsStorageMonitor storageMonitor, Phone phone, CdmaSMSDispatcher smsDispatcher, Looper looper) {
        super(context, storageMonitor, phone, smsDispatcher, looper);
        this.mTag = "MtkCdmaInboundSmsHandler";
        this.mTag = "MtkCdmaInboundSmsHandler-" + phone.getPhoneId();
    }

    public void dispatchIntent(Intent intent, String permission, String appOp, Bundle opts, InboundSmsHandler.SmsBroadcastReceiver resultReceiver, UserHandle user, int subId) {
        intent.putExtra("rTime", System.currentTimeMillis());
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

    protected int dispatchMessageRadioSpecific(SmsMessageBase smsb, int smsSource, int token) {
        SmsMessage sms = (SmsMessage) smsb;
        int ret = super.dispatchMessageRadioSpecific(MtkSmsMessage.newMtkSmsMessage(sms), smsSource, token);
        return ret;
    }

    protected String[] onModifyQueryWhereArgs(String[] whereArgs) {
        String subId = Integer.toString(this.mPhone.getSubId());
        return new String[]{whereArgs[0], whereArgs[1], whereArgs[2], subId};
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
}
