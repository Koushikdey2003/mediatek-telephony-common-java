package com.mediatek.internal.telephony.datasub;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import com.android.internal.telephony.ProxyController;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;

/* JADX INFO: loaded from: classes.dex */
public class CapabilitySwitch {
    private static final String LOG_TAG = "CapaSwitch";
    private static boolean DBG = true;
    private static CapabilitySwitch mInstance = null;
    private static Context mContext = null;
    private static DataSubSelector mDataSubSelector = null;
    private static final int capability_switch_policy = SystemProperties.getInt(DataSubConstants.PROPERTY_CAPABILITY_SWITCH_POLICY, 1);

    public static CapabilitySwitch getInstance(Context context, DataSubSelector dataSubSelector) {
        if (mInstance == null) {
            mInstance = new CapabilitySwitch(context, dataSubSelector);
        }
        return mInstance;
    }

    public CapabilitySwitch(Context context, DataSubSelector dataSubSelector) {
        mContext = context;
        mDataSubSelector = dataSubSelector;
    }

    public boolean setCapability(int phoneId) {
        int phoneNum = mDataSubSelector.getPhoneNum();
        int[] phoneRat = new int[phoneNum];
        int curr3GSim = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
        log("setCapability: " + phoneId + ", current 3G Sim = " + curr3GSim);
        ProxyController proxyController = ProxyController.getInstance();
        RadioAccessFamily[] rat = new RadioAccessFamily[phoneNum];
        for (int i = 0; i < phoneNum; i++) {
            if (phoneId == i) {
                phoneRat[i] = proxyController.getMaxRafSupported();
            } else {
                phoneRat[i] = proxyController.getMinRafSupported();
            }
            rat[i] = new RadioAccessFamily(i, phoneRat[i]);
        }
        if (proxyController.setRadioCapability(rat)) {
            return true;
        }
        log("Set phone rat fail!!! MaxPhoneRat=" + phoneRat[phoneId]);
        return false;
    }

    public int getCapabilitySwitchPolicy() {
        return capability_switch_policy;
    }

    private static void log(String txt) {
        if (DBG) {
            Rlog.d(LOG_TAG, txt);
        }
    }
}
