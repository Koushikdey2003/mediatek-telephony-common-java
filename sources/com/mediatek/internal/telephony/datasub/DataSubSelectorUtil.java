package com.mediatek.internal.telephony.datasub;

import android.content.Context;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.uicc.MtkUiccController;

/* JADX INFO: loaded from: classes.dex */
public class DataSubSelectorUtil {
    private static boolean DBG = true;
    private static final String LOG_TAG = "DSSelectorUtil";
    private static final int NO_SIM_STRING_LENGTH = 3;

    public static boolean isAnySimNotReady(Context context, int subCount) {
        int simCount = 0;
        int phoneCount = ((TelephonyManager) context.getSystemService("phone")).getActiveModemCount();
        for (int i = 0; i < phoneCount; i++) {
            String iccid = "";
            MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
            if (ctrl != null) {
                iccid = ctrl.getIccid(i);
            }
            if (iccid.equals("")) {
                log("Not ready sim exist:" + i + ", max:" + phoneCount);
                return true;
            }
            if (iccid.length() > 3) {
                simCount++;
            }
        }
        log("simCount:" + simCount);
        return subCount != simCount;
    }

    public static boolean isActiveSub(Context context, int phoneId) {
        int subId = -1;
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds != null && subIds.length > 0) {
            subId = subIds[0];
        }
        return SubscriptionManager.from(context).isActiveSubId(subId);
    }

    private static void log(String txt) {
        if (DBG) {
            Rlog.d(LOG_TAG, txt);
        }
    }

    private static void loge(String txt) {
        if (DBG) {
            Rlog.e(LOG_TAG, txt);
        }
    }
}
