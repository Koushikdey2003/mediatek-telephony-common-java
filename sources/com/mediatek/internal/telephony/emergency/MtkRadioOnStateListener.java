package com.mediatek.internal.telephony.emergency;

import android.os.SystemProperties;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.DataSubConstants;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioOnStateListener extends RadioOnStateListener {
    private static final String TAG = "MtkRadioOnStateListener";

    protected void onHitMaxRetries(int serviceState) {
        if (!SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, "OM").equals("OP12") && !SystemProperties.get("vendor.ril.call.emci_support").equals(RadioCapabilitySwitchUtil.IMSI_READY) && serviceState == 1) {
            Rlog.d(TAG, "onRetryTimeout: Radio is on and OOS. Cleaning up.");
            onComplete(true);
        }
    }
}
