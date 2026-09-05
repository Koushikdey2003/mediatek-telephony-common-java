package com.mediatek.internal.telephony;

import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.DeviceStateMonitor;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class MtkDeviceStateMonitor extends DeviceStateMonitor {
    private static final String LOG_TAG = "MtkDeviceStateMonitor";
    private static final String PROPERTY_FD_ON_CHARGE = "persist.vendor.fd.on.charge";
    private static final String PROPERTY_FD_SCREEN_OFF_ONLY = "persist.vendor.fd.screen.off.only";

    public MtkDeviceStateMonitor(Phone phone) {
        super(phone);
        logd("Initialize MtkDeviceStateMonitor");
        this.mIsLowDataExpected = isLowDataExpected();
    }

    protected boolean isLowDataExpected() {
        logd("isLowDataExpected mIsScreenOn = " + this.mIsScreenOn + " mIsCharging = " + this.mIsCharging + " mIsTetheringOn = " + this.mIsTetheringOn);
        return !(isFdEnabledOnlyWhenScreenOff() && this.mIsScreenOn) && (!this.mIsCharging || isFdEnabledWhenCharging()) && !this.mIsTetheringOn;
    }

    private static boolean isFdEnabledWhenCharging() {
        return SystemProperties.getInt(PROPERTY_FD_ON_CHARGE, 1) == 1;
    }

    private static boolean isFdEnabledOnlyWhenScreenOff() {
        return SystemProperties.getInt(PROPERTY_FD_SCREEN_OFF_ONLY, 0) == 1;
    }

    private void logd(String s) {
        Rlog.d(LOG_TAG, "[phoneId" + this.mPhone.getPhoneId() + "]" + s);
    }
}
