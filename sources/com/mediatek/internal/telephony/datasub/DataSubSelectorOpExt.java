package com.mediatek.internal.telephony.datasub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DataSubSelectorOpExt implements IDataSubSelectorOPExt {
    private static final int DSS_RET_CANNOT_GET_SIM_INFO = -2;
    private static final int DSS_RET_INVALID_PHONE_INDEX = -1;
    private static final int DSS_RET_SIM_NOT_ALL_LOADED = -3;
    private static boolean DBG = true;
    private static String LOG_TAG = "DSSExt";
    private static Context mContext = null;
    private static DataSubSelector mDataSubSelector = null;
    private static ISimSwitchForDSSExt mSimSwitchForDSS = null;
    private static CapabilitySwitch mCapabilitySwitch = null;

    public DataSubSelectorOpExt(Context context) {
        mContext = context;
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void init(DataSubSelector dataSubSelector, ISimSwitchForDSSExt simSwitchForDSS) {
        mDataSubSelector = dataSubSelector;
        mCapabilitySwitch = CapabilitySwitch.getInstance(mContext, dataSubSelector);
        mSimSwitchForDSS = simSwitchForDSS;
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void handleSimStateChanged(Intent intent) {
        int simStatus = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
        log("subsidylock: handleSimStateChanged: " + simStatus);
        if (simStatus == 10) {
            log("handleSimStateChanged: INTENT_VALUE_ICC_IMSI");
            handleNeedWaitImsi(intent);
            handleNeedWaitUnlock(intent);
        }
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void handleSubinfoRecordUpdated(Intent intent) {
        int detectedType = intent.getIntExtra("simDetectStatus", 4);
        log("handleSubinfoRecordUpdated: detectedType = " + detectedType);
        subSelector(intent);
    }

    private void handleNeedWaitImsi(Intent intent) {
    }

    private void handleNeedWaitUnlock(Intent intent) {
    }

    private int getHighCapabilityPhoneIdBySimType() {
        int i;
        int i2;
        int phoneId = -1;
        int phoneCount = mDataSubSelector.getPhoneNum();
        int[] simOpInfo = new int[phoneCount];
        int[] simType = new int[phoneCount];
        int insertedState = 0;
        int tSimCount = 0;
        int wSimCount = 0;
        int cSimCount = 0;
        int op09VolteOffPhoneId = -1;
        if (RadioCapabilitySwitchUtil.isPS2SupportLTE()) {
            int i3 = 2;
            if (phoneCount == 2) {
                SubscriptionManager subManager = (SubscriptionManager) mContext.getSystemService("telephony_subscription_service");
                List<SubscriptionInfo> subList = subManager.getAvailableSubscriptionInfoList();
                int insertedSimCount = (subList == null || subList.isEmpty()) ? 0 : subList.size();
                if (DataSubSelectorUtil.isAnySimNotReady(mContext, insertedSimCount)) {
                    log("error: sim not ready, wait for next sub ready");
                    return -3;
                }
                if (insertedSimCount > 0) {
                    for (SubscriptionInfo sub : subList) {
                        int slotId = sub.getSimSlotIndex();
                        insertedState |= 1 << slotId;
                    }
                }
                if (insertedSimCount == 0) {
                    log("no sim card, don't switch");
                    return -1;
                }
                if (!RadioCapabilitySwitchUtil.getSimInfo(simOpInfo, simType, insertedState)) {
                    log("cannot get sim operator info, don't switch");
                    return -2;
                }
                int i4 = 0;
                while (i4 < phoneCount) {
                    if (i3 == simOpInfo[i4]) {
                        tSimCount++;
                    } else if (RadioCapabilitySwitchUtil.isCdmaCard(i4, simOpInfo[i4], mContext)) {
                        cSimCount++;
                        simOpInfo[i4] = 4;
                        op09VolteOffPhoneId = i4;
                    } else if (simOpInfo[i4] != 0) {
                        wSimCount++;
                        if (simOpInfo[i4] != 4) {
                            simOpInfo[i4] = 3;
                        }
                    }
                    i4++;
                    i3 = 2;
                }
                log("getHighCapabilityPhoneIdBySimType : Inserted SIM count: " + insertedSimCount + ", insertedStatus: " + insertedState + ", tSimCount: " + tSimCount + ", wSimCount: " + wSimCount + ", cSimCount: " + cSimCount + Arrays.toString(simOpInfo));
                if (RadioCapabilitySwitchUtil.isSupportSimSwitchEnhancement(1) && RadioCapabilitySwitchUtil.isTPlusWSupport()) {
                    if (simOpInfo[0] == 2) {
                        i2 = 3;
                        if (simOpInfo[1] == 3) {
                            phoneId = 0;
                        }
                    } else {
                        i2 = 3;
                    }
                    if (simOpInfo[0] == i2 && simOpInfo[1] == 2) {
                        phoneId = 1;
                    }
                }
                if (RadioCapabilitySwitchUtil.isSupportSimSwitchEnhancement(2)) {
                    if (simOpInfo[0] == 2 && RadioCapabilitySwitchUtil.isCdmaCard(1, simOpInfo[1], mContext)) {
                        phoneId = 1;
                    } else if (RadioCapabilitySwitchUtil.isCdmaCard(0, simOpInfo[0], mContext) && simOpInfo[1] == 2) {
                        phoneId = 0;
                    }
                }
                if (RadioCapabilitySwitchUtil.isSupportSimSwitchEnhancement(3)) {
                    if (RadioCapabilitySwitchUtil.isCdmaCard(0, simOpInfo[0], mContext)) {
                        i = 3;
                        if (simOpInfo[1] == 3) {
                            phoneId = 0;
                        }
                    } else {
                        i = 3;
                    }
                    if (simOpInfo[0] == i && RadioCapabilitySwitchUtil.isCdmaCard(1, simOpInfo[1], mContext)) {
                        phoneId = 1;
                    }
                }
                if (simOpInfo[0] == 4 && simOpInfo[1] == 4 && wSimCount == 1 && cSimCount == 1 && op09VolteOffPhoneId != -1) {
                    phoneId = op09VolteOffPhoneId;
                }
            }
        }
        log("getHighCapabilityPhoneIdBySimType : " + phoneId);
        return phoneId;
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void subSelector(Intent intent) {
        int phoneId = getHighCapabilityPhoneIdBySimType();
        if (phoneId == -3) {
            return;
        }
        if (phoneId == -1) {
            int defDataSubId = SubscriptionManagerService.getInstance().getDefaultDataSubId();
            phoneId = SubscriptionManager.getPhoneId(defDataSubId);
        }
        log("Default data phoneid = " + phoneId);
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void handleBootCompleteAction() {
        log("handleBootCompleteAction");
        int simState1 = TelephonyManager.from(mContext).getSimState(0);
        int simState2 = TelephonyManager.from(mContext).getSimState(1);
        log("subsidylock: simState1 :" + simState1 + ", simState2 : " + simState2);
        if (simState1 == 1 && simState2 == 1) {
            log("subsidylock: both SIM ABSENT, Set capability and data to phoneId 0");
            mCapabilitySwitch.setCapability(0);
        }
    }

    private boolean hasConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService("connectivity");
        NetworkInfo info = cm.getActiveNetworkInfo();
        log("DataSubselector, networkinfo: " + info);
        if (info != null && info.isConnected()) {
            NetworkInfo.DetailedState state = info.getDetailedState();
            log("DataSubselector, DetailedState : " + state);
            if (state == NetworkInfo.DetailedState.CONNECTED) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void handleConnectivityAction() {
        log("handleConnectivityAction");
        if (hasConnectivity()) {
            log("SET CONNECTIVITY_STATUS TO 1");
            SystemProperties.set("persist.vendor.subsidylock.connectivity_status", String.valueOf(1));
        } else {
            log("SET CONNECTIVITY_STATUS TO 0");
            SystemProperties.set("persist.vendor.subsidylock.connectivity_status", String.valueOf(0));
        }
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void handleAirPlaneModeOff(Intent intent) {
        subSelector(intent);
    }

    @Override // com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt
    public void handleDefaultDataChanged(Intent intent) {
    }

    private void log(String txt) {
        if (DBG) {
            Rlog.d(LOG_TAG, txt);
        }
    }
}
