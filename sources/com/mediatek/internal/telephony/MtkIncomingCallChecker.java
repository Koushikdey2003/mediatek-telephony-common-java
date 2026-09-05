package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telecom.CallerInfo;
import android.telecom.CallerInfoAsyncQuery;
import android.telephony.CarrierConfigManager;
import com.android.internal.telephony.BlockChecker;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class MtkIncomingCallChecker {
    private static final String PROP_LOG_TAG = "MTCallChecker";
    private static final int TOKEN_MT_CHECKER = 256;
    AsyncBlockCheckTask blockChecker = null;
    OnCheckCompleteListener mCallback = null;
    String mName;
    Object obj;

    public interface OnCheckCompleteListener {
        void onCheckComplete(boolean z, Object obj);
    }

    public MtkIncomingCallChecker(String name, Object obj) {
        this.obj = null;
        this.mName = name;
        this.obj = obj;
    }

    public boolean startIncomingCallNumberCheck(Context context, int subId, final String number, OnCheckCompleteListener callback) {
        if (context == null) {
            proprietaryLogE("cannot do checkIncomingCallNumber (context=null, subId=" + subId + ", number=" + number + "), call will enter");
            return false;
        }
        if (callback == null) {
            proprietaryLogE("checkIncomingCallNumber callback null, call will enter");
            return false;
        }
        this.mCallback = callback;
        if (this.blockChecker != null) {
            proprietaryLog("block checker not null (" + this.blockChecker.toString() + "). will create a new one.");
        }
        this.blockChecker = new AsyncBlockCheckTask(context);
        if (!isMtkEnhancedCallBlockingEnabled(context, subId)) {
            return false;
        }
        CallerInfoAsyncQuery.startQuery(256, context, number, new CallerInfoAsyncQuery.OnQueryCompleteListener() { // from class: com.mediatek.internal.telephony.MtkIncomingCallChecker.1
            public void onQueryComplete(int token, Object cookie, CallerInfo info) {
                int i;
                boolean contactExists = info == null ? false : info.contactExists;
                AsyncBlockCheckTask asyncBlockCheckTask = MtkIncomingCallChecker.this.blockChecker;
                String[] strArr = new String[3];
                String str = number;
                strArr[0] = str;
                if (str == null || str.isEmpty()) {
                    i = 2;
                } else {
                    i = 1;
                }
                strArr[1] = String.valueOf(i);
                strArr[2] = String.valueOf(contactExists);
                asyncBlockCheckTask.execute(strArr);
            }
        }, "ContactQuery", subId);
        return true;
    }

    void onBlockCheckComplete(Boolean isBlocked) {
        proprietaryLog("query result, isBlocked=" + isBlocked);
        OnCheckCompleteListener onCheckCompleteListener = this.mCallback;
        if (onCheckCompleteListener != null) {
            onCheckCompleteListener.onCheckComplete(isBlocked.booleanValue(), this.obj);
        }
    }

    static boolean isMtkEnhancedCallBlockingEnabled(Context context, int subId) {
        if (context == null) {
            proprietaryLog("isMtkEnhancedCallBlockingEnabled fail, return false (context null)");
            return false;
        }
        CarrierConfigManager configManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        PersistableBundle carrierConfig = configManager.getConfigForSubId(subId);
        if (carrierConfig == null) {
            carrierConfig = CarrierConfigManager.getDefaultConfig();
        }
        return carrierConfig.getBoolean("mtk_support_enhanced_call_blocking_bool");
    }

    static void proprietaryLog(String s) {
        Rlog.d(PROP_LOG_TAG, s);
    }

    static void proprietaryLogE(String s) {
        Rlog.e(PROP_LOG_TAG, s);
    }

    static void proprietaryLogI(String s) {
        Rlog.i(PROP_LOG_TAG, s);
    }

    class AsyncBlockCheckTask extends AsyncTask<String, Void, Boolean> {
        private Context mContext;

        public AsyncBlockCheckTask(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(String... params) {
            String number = "";
            Bundle extras = new Bundle();
            if (params.length > 0) {
                number = params[0];
            }
            if (params.length > 1) {
                extras.putInt("extra_call_presentation", Integer.valueOf(params[1]).intValue());
                MtkIncomingCallChecker.proprietaryLogI("doInBackground isBlocked(presentation:" + Integer.valueOf(params[1]));
            }
            if (params.length > 2) {
                extras.putBoolean("extra_contact_exist", Boolean.valueOf(params[2]).booleanValue());
            }
            return Boolean.valueOf(BlockChecker.isBlocked(this.mContext, number, extras));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean isBlocked) {
            MtkIncomingCallChecker.this.onBlockCheckComplete(Boolean.valueOf(isBlocked.booleanValue()));
        }
    }
}
