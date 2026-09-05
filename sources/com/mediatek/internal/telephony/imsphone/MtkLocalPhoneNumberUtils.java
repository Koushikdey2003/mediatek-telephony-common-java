package com.mediatek.internal.telephony.imsphone;

/* JADX INFO: loaded from: classes.dex */
public class MtkLocalPhoneNumberUtils {
    private static final String LOG_TAG = "MtkLocalPhoneNumberUtils";
    private static boolean sIsEmergencyNumber = false;

    public static void setIsEmergencyNumber(boolean isEmergencyNumber) {
        sIsEmergencyNumber = isEmergencyNumber;
    }

    public static boolean getIsEmergencyNumber() {
        return sIsEmergencyNumber;
    }
}
