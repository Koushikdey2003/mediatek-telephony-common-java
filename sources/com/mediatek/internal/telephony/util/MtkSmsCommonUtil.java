package com.mediatek.internal.telephony.util;

import android.os.Build;
import android.os.SystemProperties;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;

/* JADX INFO: loaded from: classes.dex */
public final class MtkSmsCommonUtil {
    public static final String IS_EMERGENCY_CB_PRIMARY = "isPrimary";
    public static final String SELECT_BY_REFERENCE = "address=? AND reference_number=? AND count=? AND deleted=0 AND sub_id=?";
    public static final String SQL_3GPP2_SMS = " AND (destination_port & 262144=262144)";
    public static final String SQL_3GPP_SMS = " AND (destination_port & 131072=131072)";
    private static final String TAG = "MtkSmsCommonUtil";
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final boolean IS_PRIVACY_PROTECTION_LOCK_SUPPORT = SystemProperties.get("ro.vendor.mtk_privacy_protection_lock").equals(RadioCapabilitySwitchUtil.IMSI_READY);
    private static final boolean IS_WAPPUSH_SUPPORT = SystemProperties.get("ro.vendor.mtk_wappush_support").equals(RadioCapabilitySwitchUtil.IMSI_READY);

    private MtkSmsCommonUtil() {
    }

    public static boolean isWapPushSupport() {
        return IS_WAPPUSH_SUPPORT;
    }
}
