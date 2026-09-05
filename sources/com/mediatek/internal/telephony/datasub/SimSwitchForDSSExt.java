package com.mediatek.internal.telephony.datasub;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class SimSwitchForDSSExt implements ISimSwitchForDSSExt {
    private static final String LOG_TAG = "SimSwitchDSSExt";
    public static final boolean USER_BUILD = TextUtils.equals(Build.TYPE, DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    public static boolean DBG = true;
    private static DataSubSelector mDataSubSelector = null;
    private static CapabilitySwitch mCapabilitySwitch = null;
    protected static Context mContext = null;

    public SimSwitchForDSSExt(Context context) {
        mContext = context;
    }

    @Override // com.mediatek.internal.telephony.datasub.ISimSwitchForDSSExt
    public void init(DataSubSelector dataSubSelector) {
        mDataSubSelector = dataSubSelector;
        mCapabilitySwitch = CapabilitySwitch.getInstance(mContext, dataSubSelector);
    }

    @Override // com.mediatek.internal.telephony.datasub.ISimSwitchForDSSExt
    public boolean checkCapSwitch(int policy) {
        if (RadioCapabilitySwitchUtil.isSubsidyLockForOmSupported()) {
            return checkOp18SubsidyCapSwitch();
        }
        return false;
    }

    @Override // com.mediatek.internal.telephony.datasub.ISimSwitchForDSSExt
    public int isNeedSimSwitch() {
        return 2;
    }

    private int getMajorSim() {
        String currMajorSim = SystemProperties.get("persist.vendor.radio.simswitch", "");
        if (currMajorSim != null && !currMajorSim.equals("")) {
            log("[getMajorSim]: " + (Integer.parseInt(currMajorSim) - 1));
            return Integer.parseInt(currMajorSim) - 1;
        }
        log("[getMajorSim]: fail to get major SIM");
        return -1;
    }

    private boolean checkOp18SubsidyCapSwitch() {
        int[] simOpInfo = new int[mDataSubSelector.getPhoneNum()];
        int[] simType = new int[mDataSubSelector.getPhoneNum()];
        int targetSim = -1;
        int insertedSimCount = 0;
        int insertedStatus = 0;
        boolean[] op18Usim = new boolean[mDataSubSelector.getPhoneNum()];
        int defDataSubId = SubscriptionManagerService.getInstance().getDefaultDataSubId();
        int defDataPhoneId = -1;
        String[] currIccId = new String[mDataSubSelector.getPhoneNum()];
        log("checkOp18CapSwitch start");
        if (defDataSubId != -1) {
            defDataPhoneId = SubscriptionManager.getPhoneId(defDataSubId);
        }
        for (int i = 0; i < mDataSubSelector.getPhoneNum(); i++) {
            MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
            if (ctrl != null) {
                currIccId[i] = ctrl.getIccid(i);
            }
            if (currIccId[i] == null || "".equals(currIccId[i])) {
                log("error: iccid not found, wait for next sub ready");
                return false;
            }
            if (!DataSubConstants.NO_SIM_VALUE.equals(currIccId[i])) {
                if (RadioCapabilitySwitchUtil.isSimOn(i)) {
                    insertedSimCount++;
                    insertedStatus |= 1 << i;
                } else {
                    log("checkOp18SubsidyCapSwitch, slot" + i + " is power off.");
                }
            }
        }
        log("checkOp18SubsidyCapSwitch : Inserted SIM count: " + insertedSimCount + ", insertedStatus: " + insertedStatus);
        if (!RadioCapabilitySwitchUtil.getSimInfo(simOpInfo, simType, insertedStatus)) {
            if (RadioCapabilitySwitchUtil.isSubsidyLockForOmSupported()) {
                int protocolSim = getMajorSim();
                log("SIM is in locked state, protocolSim: " + protocolSim);
                if (protocolSim >= 0 && protocolSim <= 1) {
                    int newProtocolSim = 1 - protocolSim;
                    Context context = mContext;
                    if (context == null) {
                        log("mContext is null");
                        return false;
                    }
                    int sim1_state = TelephonyManager.from(context).getSimState(protocolSim);
                    int sim2_state = TelephonyManager.from(mContext).getSimState(newProtocolSim);
                    log("sim1_state: " + sim1_state + ", sim2_state = " + sim2_state);
                    if (sim1_state == 4 || sim2_state == 4) {
                        log("Both SIMs are in locked state, newProtocolSim: " + newProtocolSim);
                        mCapabilitySwitch.setCapability(newProtocolSim);
                        mDataSubSelector.setDefaultData(newProtocolSim);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        int capabilitySimId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
        log("op18: capabilitySimId:" + capabilitySimId);
        for (int i2 = 0; i2 < mDataSubSelector.getPhoneNum(); i2++) {
            if (simOpInfo[i2] == 4) {
                op18Usim[i2] = true;
            }
        }
        log("op18Usim: " + Arrays.toString(op18Usim));
        for (int i3 = 0; i3 < mDataSubSelector.getPhoneNum(); i3++) {
            if (defDataPhoneId == i3) {
                if (op18Usim[i3]) {
                    targetSim = i3;
                } else {
                    for (int j = 0; j < mDataSubSelector.getPhoneNum(); j++) {
                        if (op18Usim[j]) {
                            targetSim = j;
                        }
                    }
                }
                log("op18: i = " + i3 + "targetSim : " + targetSim);
                if (op18Usim[i3]) {
                    log("op18-C1: cur is old op18 USIM, no change");
                    if (capabilitySimId != i3) {
                        log("op18-C1a: old op18 SIM change slot, change!");
                        mCapabilitySwitch.setCapability(i3);
                    }
                    mDataSubSelector.setDefaultData(i3);
                    return true;
                }
                if (targetSim != -1) {
                    log("op18-C2: cur is not op18 SIM but find op18 SIM, change!");
                    mCapabilitySwitch.setCapability(targetSim);
                    mDataSubSelector.setDefaultData(targetSim);
                    return true;
                }
                mDataSubSelector.setDefaultData(capabilitySimId);
                log("op18-C6: no higher priority SIM, no cahnge");
                return true;
            }
        }
        if (op18Usim[capabilitySimId]) {
            targetSim = capabilitySimId;
        } else {
            for (int i4 = 0; i4 < mDataSubSelector.getPhoneNum(); i4++) {
                if (op18Usim[i4]) {
                    targetSim = i4;
                }
            }
        }
        log("op18: target SIM :" + targetSim);
        if (op18Usim[capabilitySimId]) {
            log("op18-C7: cur is new op18 USIM, no change");
            mDataSubSelector.setDefaultData(capabilitySimId);
            return true;
        }
        if (targetSim != -1) {
            log("op18-C8: find op18 USIM, change!");
            mCapabilitySwitch.setCapability(targetSim);
            mDataSubSelector.setDefaultData(targetSim);
            return true;
        }
        mDataSubSelector.setDefaultData(capabilitySimId);
        log("op18-C12: no higher priority SIM, no cahnge");
        return true;
    }

    private static void log(String txt) {
        if (DBG) {
            Rlog.d(LOG_TAG, txt);
        }
    }
}
