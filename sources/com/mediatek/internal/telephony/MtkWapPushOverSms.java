package com.mediatek.internal.telephony;

import android.app.BroadcastOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.IWapPushManager;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.SmsApplication;
import com.android.internal.telephony.WapPushOverSms;
import com.android.internal.telephony.util.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class MtkWapPushOverSms extends WapPushOverSms {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final String TAG = "Mtk_WAP_PUSH";

    public MtkWapPushOverSms(Context context) {
        super(context);
    }

    public int dispatchWapPdu(byte[] pdu, InboundSmsHandler.SmsBroadcastReceiver receiver, InboundSmsHandler handler, String address, String scAddr, int subId, long messageId) {
        Bundle options;
        boolean z = ENG;
        if (z) {
            Rlog.i(TAG, "dispathchWapPdu!");
        }
        WapPushOverSms.DecodedResult result = decodeWapPdu(pdu, handler);
        if (result.statusCode != -1) {
            return result.statusCode;
        }
        if (result.wapAppId != null) {
            boolean processFurther = true;
            try {
                IWapPushManager wapPushMan = this.mWapPushManager;
                if (wapPushMan == null) {
                    if (z) {
                        Rlog.d(TAG, "wap push manager not found!");
                    }
                } else {
                    if (z) {
                        Rlog.w(TAG, "addPowerSaveTempWhitelistAppForMms - start");
                    }
                    synchronized (this) {
                        this.mPowerWhitelistManager.whitelistAppTemporarilyForEvent(this.mWapPushManagerPackage, 2, 315, "mms-mgr");
                    }
                    if (z) {
                        Rlog.d(TAG, "addPowerSaveTempWhitelistAppForMms - end");
                    }
                    Intent intent = new Intent();
                    intent.putExtra("transactionId", result.transactionId);
                    intent.putExtra("pduType", result.pduType);
                    intent.putExtra("header", result.header);
                    intent.putExtra("data", result.intentData);
                    intent.putExtra("contentTypeParameters", result.contentTypeParameters);
                    SubscriptionManager.putPhoneIdAndSubIdExtra(intent, result.phoneId);
                    if (!TextUtils.isEmpty(address)) {
                        intent.putExtra("address", address);
                    }
                    if (!TextUtils.isEmpty(scAddr)) {
                        Rlog.d(TAG, "put sc addr info into intent 1");
                        intent.putExtra("service_center", scAddr);
                    }
                    int procRet = wapPushMan.processMessage(result.wapAppId, result.contentType, intent);
                    if (z) {
                        Rlog.v(TAG, "procRet:" + procRet);
                    }
                    if ((procRet & 1) > 0 && (32768 & procRet) == 0) {
                        processFurther = false;
                    }
                }
                if (!processFurther) {
                    return 1;
                }
            } catch (RemoteException e) {
                if (ENG) {
                    Rlog.w(TAG, "remote func failed...");
                }
            }
        }
        boolean z2 = ENG;
        if (z2) {
            Rlog.v(TAG, "fall back to existing handler");
        }
        if (result.mimeType == null) {
            if (z2) {
                Rlog.w(TAG, "Header Content-Type error.");
            }
            return 2;
        }
        Intent intent2 = new Intent("android.provider.Telephony.WAP_PUSH_DELIVER");
        intent2.setType(result.mimeType);
        intent2.putExtra("transactionId", result.transactionId);
        intent2.putExtra("pduType", result.pduType);
        intent2.putExtra("header", result.header);
        intent2.putExtra("data", result.intentData);
        intent2.putExtra("contentTypeParameters", result.contentTypeParameters);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent2, result.phoneId);
        if (!TextUtils.isEmpty(address)) {
            intent2.putExtra("address", address);
        }
        if (!TextUtils.isEmpty(scAddr)) {
            Rlog.d(TAG, "put sc addr info into intent 2");
            intent2.putExtra("service_center", scAddr);
        }
        if (messageId != 0) {
            intent2.putExtra("messageId", messageId);
        }
        UserHandle userHandle = TelephonyUtils.getSubscriptionUserHandle(this.mContext, subId);
        ComponentName componentName = SmsApplication.getDefaultMmsApplicationAsUser(this.mContext, true, userHandle);
        if (componentName == null) {
            options = null;
        } else {
            intent2.setComponent(componentName);
            if (z2) {
                Rlog.v(TAG, "Delivering MMS to: " + componentName.getPackageName() + " " + componentName.getClassName());
            }
            long duration = this.mPowerWhitelistManager.whitelistAppTemporarilyForEvent(componentName.getPackageName(), 2, 315, "mms-app");
            BroadcastOptions bopts = BroadcastOptions.makeBasic();
            bopts.setTemporaryAppAllowlist(duration, 0, 315, "");
            Bundle options2 = bopts.toBundle();
            options = options2;
        }
        handler.dispatchIntent(intent2, getPermissionForType(result.mimeType), getAppOpsStringPermissionForIntent(result.mimeType), options, receiver, UserHandle.SYSTEM, subId);
        return -1;
    }
}
