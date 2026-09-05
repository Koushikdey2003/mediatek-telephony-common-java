package com.mediatek.internal.telephony.data;

import android.os.Looper;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.data.TelephonyNetworkFactory;
import com.android.internal.telephony.data.TelephonyNetworkRequest;

/* JADX INFO: loaded from: classes.dex */
public class MtkTelephonyNetworkFactory extends TelephonyNetworkFactory {
    public MtkTelephonyNetworkFactory(Looper looper, Phone phone) {
        super(looper, phone);
    }

    protected boolean mtkIgnoreCapabilityCheck(TelephonyNetworkRequest networkRequest, int action) {
        if (networkRequest.hasCapability(4) || networkRequest.hasCapability(10)) {
            log("ignoreCapabilityCheck() ignore IMS/EIMS PDN");
            return true;
        }
        if (networkRequest.hasCapability(30)) {
            log("ignoreCapabilityCheck() ignore VSIM PDN");
            return true;
        }
        return false;
    }
}
