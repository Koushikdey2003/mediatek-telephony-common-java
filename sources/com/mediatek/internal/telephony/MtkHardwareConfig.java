package com.mediatek.internal.telephony;

import android.os.SystemProperties;
import com.android.internal.telephony.HardwareConfig;

/* JADX INFO: loaded from: classes.dex */
public final class MtkHardwareConfig extends HardwareConfig {
    public MtkHardwareConfig(int type) {
        super(type);
    }

    public MtkHardwareConfig(String res) {
        super(res);
    }

    public boolean hasRaCapability() {
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            return true;
        }
        return false;
    }

    public boolean hasModemDeactPdnCapabilityForMultiPS() {
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            return true;
        }
        return false;
    }

    public boolean hasOperatorIaCapability() {
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            return true;
        }
        return false;
    }

    public boolean hasMdAutoSetupImsCapability() {
        if (SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return true;
        }
        return false;
    }

    public boolean isCdma3gDualActivationSupported() {
        if (SystemProperties.get("vendor.ril.cdma.3g.dualact").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return true;
        }
        return false;
    }
}
