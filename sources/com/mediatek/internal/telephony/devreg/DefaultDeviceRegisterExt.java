package com.mediatek.internal.telephony.devreg;

import android.content.Context;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class DefaultDeviceRegisterExt implements IDeviceRegisterExt {
    private static final String TAG = "DefaultDeviceRegisterExt";
    protected Context mContext;
    protected DeviceRegisterController mDeviceRegisterController;

    public DefaultDeviceRegisterExt(Context context, DeviceRegisterController controller) {
        this.mContext = context;
        this.mDeviceRegisterController = controller;
    }

    @Override // com.mediatek.internal.telephony.devreg.IDeviceRegisterExt
    public void handleAutoRegMessage(int subId, String format, byte[] pdu) {
        Log.i(TAG, "handleAutoRegMessage sub " + subId + ", format " + format);
    }
}
