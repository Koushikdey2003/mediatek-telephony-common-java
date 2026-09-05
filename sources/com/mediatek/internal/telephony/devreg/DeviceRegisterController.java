package com.mediatek.internal.telephony.devreg;

import android.app.PendingIntent;
import android.content.Context;
import com.mediatek.internal.telephony.MtkUiccSmsController;
import com.mediatek.internal.telephony.OpTelephonyCustomizationFactoryBase;
import com.mediatek.internal.telephony.OpTelephonyCustomizationUtils;

/* JADX INFO: loaded from: classes.dex */
public class DeviceRegisterController {
    private static IDeviceRegisterExt sDeviceRegisterExt = null;
    private MtkUiccSmsController mSmsController;

    public DeviceRegisterController(Context context, MtkUiccSmsController controller) {
        this.mSmsController = null;
        this.mSmsController = controller;
        try {
            OpTelephonyCustomizationFactoryBase factoryBase = OpTelephonyCustomizationUtils.getOpFactory(context);
            sDeviceRegisterExt = factoryBase.makeDeviceRegisterExt(context, this);
        } catch (Exception e) {
            e.printStackTrace();
            sDeviceRegisterExt = new DefaultDeviceRegisterExt(context, this);
        }
    }

    public void sendDataSms(int subId, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        this.mSmsController.sendData(subId, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
    }

    private static IDeviceRegisterExt getDeviceRegisterExt() {
        return sDeviceRegisterExt;
    }

    public void handleAutoRegMessage(int subId, String format, byte[] pdu) {
        IDeviceRegisterExt iDeviceRegisterExt = sDeviceRegisterExt;
        if (iDeviceRegisterExt != null) {
            iDeviceRegisterExt.handleAutoRegMessage(subId, format, pdu);
        }
    }
}
