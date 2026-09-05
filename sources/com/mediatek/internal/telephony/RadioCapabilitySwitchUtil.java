package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.MtkRadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.ratconfiguration.RatConfiguration;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class RadioCapabilitySwitchUtil {
    public static final String CN_MCC = "460";
    public static final int ENHANCEMENT_T_PLUS_C = 2;
    public static final int ENHANCEMENT_T_PLUS_T = 0;
    public static final int ENHANCEMENT_T_PLUS_W = 1;
    public static final int ENHANCEMENT_W_PLUS_C = 3;
    public static final int ENHANCEMENT_W_PLUS_NA = 5;
    public static final int ENHANCEMENT_W_PLUS_W = 4;
    public static final int ICCID_ERROR = 3;
    public static final String IMSI_NOT_READY = "0";
    public static final int IMSI_NOT_READY_OR_SIM_LOCKED = 2;
    public static final String IMSI_READY = "1";
    private static final String LOG_TAG = "RadioCapabilitySwitchUtil";
    public static final int NOT_SHOW_DIALOG = 1;
    private static final String NO_SIM_VALUE = "N/A";
    public static final int OP01_6M_PRIORITY_OP01_SIM = 1;
    public static final int OP01_6M_PRIORITY_OP01_USIM = 0;
    public static final int OP01_6M_PRIORITY_OTHER = 2;
    private static final String PROPERTY_CAPABILITY_SWITCH = "persist.vendor.radio.simswitch";
    public static final int SHOW_DIALOG = 0;
    public static final int SIM_OP_INFO_OP01 = 2;
    public static final int SIM_OP_INFO_OP02 = 3;
    public static final int SIM_OP_INFO_OP09 = 4;
    public static final int SIM_OP_INFO_OP18 = 4;
    public static final int SIM_OP_INFO_OVERSEA = 1;
    public static final int SIM_OP_INFO_UNKNOWN = 0;
    public static final int SIM_SWITCHING = 4;
    public static final int SIM_SWITCH_MODE_DUAL_TALK = 3;
    public static final int SIM_SWITCH_MODE_DUAL_TALK_SWAP = 4;
    public static final int SIM_SWITCH_MODE_SINGLE_TALK_MDSYS = 1;
    public static final int SIM_SWITCH_MODE_SINGLE_TALK_MDSYS_LITE = 2;
    public static final int SIM_TYPE_OTHER = 2;
    public static final int SIM_TYPE_SIM = 0;
    public static final int SIM_TYPE_USIM = 1;
    public static final int SUBSIDY_LOCK_SUPPORT = 10;
    private static final String[] PLMN_TABLE_OP01 = {"46000", "46002", "46007", "46008", "45412", "45413", "00101", "00211", "00321", "00431", "00541", "00651", "00761", "00871", "00902", "01012", "01122", "01232", "46004", "46602", "50270"};
    private static final String[] PLMN_TABLE_OP02 = {"46001", "46006", "46009", "45407"};
    private static final String[] PLMN_TABLE_OP09 = {"46005", "45502", "46003", "46011"};
    private static final String[] PLMN_TABLE_OP09_3G = {"20404"};
    private static final String[] PLMN_TABLE_OP18 = {"405840", "405854", "405855", "405856", "405857", "405858", "405859", "405860", "405861", "405862", "405863", "405864", "405865", "405866", "405867", "405868", "405869", "405870", "405871", "405872", "405873", "405874"};
    private static final String[] PROPERTY_SIM_IMSI_STATUS = {"vendor.ril.imsi.status.sim1", "vendor.ril.imsi.status.sim2", "vendor.ril.imsi.status.sim3", "vendor.ril.imsi.status.sim4"};
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    private static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};

    public static boolean getSimInfo(int[] simOpInfo, int[] simType, int insertedStatus) {
        String propStr;
        String propStr2;
        int i = insertedStatus;
        String[] strMnc = new String[simOpInfo.length];
        String[] strSimType = new String[simOpInfo.length];
        int i2 = 0;
        while (i2 < simOpInfo.length) {
            if (i2 == 0) {
                propStr = "vendor.gsm.ril.uicctype";
            } else {
                propStr = "vendor.gsm.ril.uicctype." + (i2 + 1);
            }
            strSimType[i2] = SystemProperties.get(propStr, "");
            int i3 = 0;
            if (strSimType[i2].equals("SIM")) {
                simType[i2] = 0;
            } else if (strSimType[i2].equals("USIM")) {
                simType[i2] = 1;
            } else {
                simType[i2] = 2;
            }
            int subId = MtkSubscriptionManager.getSubIdUsingPhoneId(i2);
            if (subId < 0) {
                logd("subId is invalid");
            } else {
                strMnc[i2] = TelephonyManager.getDefault().getSubscriberId(subId);
                if (strMnc[i2] == null) {
                    logd("strMnc[" + i2 + "] is null, get mnc by ril.uim.subscriberid");
                    String propStr3 = "vendor.ril.uim.subscriberid." + (i2 + 1);
                    strMnc[i2] = SystemProperties.get(propStr3, "");
                }
                if (strMnc[i2].equals("")) {
                    logd("strMnc[" + i2 + "] is null, get mnc by vendor.gsm.ril.uicc.mccmnc");
                    if (i2 == 0) {
                        propStr2 = "vendor.gsm.ril.uicc.mccmnc";
                    } else {
                        propStr2 = "vendor.gsm.ril.uicc.mccmnc." + i2;
                    }
                    strMnc[i2] = SystemProperties.get(propStr2, "");
                }
            }
            if (strMnc[i2] == null) {
                logd("strMnc[" + i2 + "] is null");
                strMnc[i2] = "";
            }
            if (strMnc[i2].length() >= 6) {
                strMnc[i2] = strMnc[i2].substring(0, 6);
            } else if (strMnc[i2].length() >= 5) {
                strMnc[i2] = strMnc[i2].substring(0, 5);
            }
            logd("SimType[" + i2 + "]= " + strSimType[i2] + "insertedStatus:" + i);
            if (i >= 0 && ((1 << i2) & i) > 0) {
                if (strMnc[i2].equals("") || strMnc[i2].equals("error")) {
                    logd("SIM is inserted but no imsi");
                    return false;
                }
                if (!strMnc[i2].equals("sim_lock")) {
                    if (strMnc[i2].equals("N/A") || strMnc[i2].equals("sim_absent")) {
                        logd("strMnc have invalid value, return false");
                        return false;
                    }
                    if (!strMnc[i2].matches("[0-9]+")) {
                        logd("strMnc have non-numeric value, return false");
                        return false;
                    }
                } else {
                    logd("SIM is lock, wait pin unlock");
                    return false;
                }
            }
            String[] strArr = PLMN_TABLE_OP01;
            int length = strArr.length;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                String mccmnc = strArr[i3];
                if (strMnc[i2].startsWith(mccmnc)) {
                    simOpInfo[i2] = 2;
                    break;
                }
                i3++;
            }
            if (simOpInfo[i2] == 0) {
                String[] strArr2 = PLMN_TABLE_OP02;
                int length2 = strArr2.length;
                int i4 = 0;
                while (true) {
                    if (i4 >= length2) {
                        break;
                    }
                    String mccmnc2 = strArr2[i4];
                    if (strMnc[i2].startsWith(mccmnc2)) {
                        simOpInfo[i2] = 3;
                        break;
                    }
                    i4++;
                }
            }
            if (simOpInfo[i2] == 0) {
                String[] strArr3 = PLMN_TABLE_OP09;
                int length3 = strArr3.length;
                int i5 = 0;
                while (true) {
                    if (i5 >= length3) {
                        break;
                    }
                    String mccmnc3 = strArr3[i5];
                    if (strMnc[i2].startsWith(mccmnc3)) {
                        simOpInfo[i2] = 4;
                        break;
                    }
                    i5++;
                }
            }
            if (simOpInfo[i2] == 0) {
                String[] strArr4 = PLMN_TABLE_OP09_3G;
                int length4 = strArr4.length;
                int i6 = 0;
                while (true) {
                    if (i6 >= length4) {
                        break;
                    }
                    String mccmnc4 = strArr4[i6];
                    if (strMnc[i2].startsWith(mccmnc4)) {
                        String uimDualMode = SystemProperties.get(PROPERTY_RIL_CT3G[i2]);
                        if (IMSI_READY.equals(uimDualMode)) {
                            simOpInfo[i2] = 4;
                            break;
                        }
                    }
                    i6++;
                }
            }
            if ((SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, "").equals(DataSubConstants.OPERATOR_OP18) || isSubsidyLockForOmSupported()) && simOpInfo[i2] == 0) {
                String[] strArr5 = PLMN_TABLE_OP18;
                int length5 = strArr5.length;
                int i7 = 0;
                while (true) {
                    if (i7 >= length5) {
                        break;
                    }
                    String mccmnc5 = strArr5[i7];
                    if (strMnc[i2].startsWith(mccmnc5)) {
                        simOpInfo[i2] = 4;
                        break;
                    }
                    i7++;
                }
            }
            if (simOpInfo[i2] == 0 && !strMnc[i2].equals("") && !strMnc[i2].equals("N/A")) {
                simOpInfo[i2] = 1;
            }
            logd("strMnc[" + i2 + "]= " + strMnc[i2] + ", simOpInfo[" + i2 + "]=" + simOpInfo[i2]);
            i2++;
            i = insertedStatus;
        }
        return true;
    }

    public static boolean isVolteEnabled(int phoneId, Context context) {
        ImsManager imsManager = ImsManager.getInstance(context, phoneId);
        boolean imsUseEnabled = imsManager.isVolteEnabledByPlatform() && imsManager.isEnhanced4gLteModeSettingEnabledByUser();
        if (imsUseEnabled) {
            int[] subId = SubscriptionManager.getSubId(phoneId);
            if (subId != null) {
                int nwMode = Settings.Global.getInt(context.getContentResolver(), "preferred_network_mode" + subId[0], MtkRILConstants.PREFERRED_NETWORK_MODE);
                int rafFromNwMode = MtkRadioAccessFamily.getRafFromNetworkType(nwMode);
                if ((rafFromNwMode & 266240) == 0) {
                    imsUseEnabled = false;
                }
                logd("isVolteEnabled, imsUseEnabled = " + imsUseEnabled + ", nwMode = " + nwMode + ", rafFromNwMode = " + rafFromNwMode + ", rafLteGroup = 266240");
            } else {
                logd("isVolteEnabled, subId[] is null");
            }
        }
        logd("isVolteEnabled = " + imsUseEnabled);
        return imsUseEnabled;
    }

    public static boolean isHVolteEnabled() {
        if (SystemProperties.get("persist.vendor.mtk_ct_volte_support").equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN) || SystemProperties.get("persist.vendor.mtk_ct_volte_support").equals("3")) {
            return true;
        }
        return false;
    }

    public static boolean isCdmaCard(int phoneId, int opInfo, Context context) {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
            logd("isCdmaCard invalid phoneId:" + phoneId);
            return false;
        }
        String cardType = SystemProperties.get(PROPERTY_RIL_FULL_UICC_TYPE[phoneId]);
        boolean isCdmaSim = cardType.indexOf("CSIM") >= 0 || cardType.indexOf("RUIM") >= 0;
        if (!isCdmaSim && "SIM".equals(cardType)) {
            String uimDualMode = SystemProperties.get(PROPERTY_RIL_CT3G[phoneId]);
            if (IMSI_READY.equals(uimDualMode)) {
                isCdmaSim = true;
            }
        }
        if (opInfo == 4) {
            isCdmaSim = true;
        }
        if (isCdmaSim && isVolteEnabled(phoneId, context) && !isHVolteEnabled()) {
            logd("isCdmaCard, volte is enabled, SRLTE is unused for CT card");
            return false;
        }
        return isCdmaSim;
    }

    public static boolean isSupportSimSwitchEnhancement(int simType) {
        switch (simType) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return true;
            case 5:
                return true;
            default:
                return false;
        }
    }

    /* JADX WARN: Type inference failed for: r7v25 */
    /* JADX WARN: Type inference failed for: r7v26, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v28 */
    /* JADX WARN: Type inference failed for: r7v56 */
    public static boolean isSkipCapabilitySwitch(int i, int i2, Context context) {
        ?? r7;
        int[] iArr = new int[i2];
        int[] iArr2 = new int[i2];
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        String[] strArr = new String[i2];
        if (isPS2SupportLTE()) {
            if (i2 > 2) {
                return i < 2 && getMainCapabilityPhoneId() < 2 && !RatConfiguration.isC2kSupported() && !RatConfiguration.isTdscdmaSupported();
            }
            int i6 = 0;
            while (i6 < i2) {
                MtkUiccController mtkUiccController = (MtkUiccController) UiccController.getInstance();
                if (mtkUiccController != null) {
                    strArr[i6] = mtkUiccController.getIccid(i6);
                }
                if (strArr[i6] != null) {
                    int i7 = i5;
                    if (!"".equals(strArr[i6])) {
                        if (!"N/A".equals(strArr[i6])) {
                            if (isSimOn(i6) && !isRadioOffBySimManagement(i6)) {
                                i4++;
                                i3 |= 1 << i6;
                            } else {
                                logd("isSkipCapabilitySwitch, slot" + i6 + " is power off.");
                            }
                        }
                        i6++;
                        i5 = i7;
                    }
                }
                logd("iccid is not ready, do capability switch");
                return false;
            }
            int i8 = i5;
            if (i4 == 0) {
                logd("no sim card, skip capability switch");
                return true;
            }
            if (!getSimInfo(iArr, iArr2, i3)) {
                logd("cannot get sim operator info, do capability switch");
                return false;
            }
            int i9 = -1;
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            int i13 = i8;
            for (int i14 = 0; i14 < i2; i14++) {
                if (((1 << i14) & i3) > 0) {
                    if (2 == iArr[i14]) {
                        i13++;
                    } else if (isCdmaCard(i14, iArr[i14], context)) {
                        i11++;
                        i9 = i14;
                    } else if (iArr[i14] != 0) {
                        i12++;
                    }
                    if (iArr[i14] == 4) {
                        i10 = 1;
                    }
                }
            }
            logd("isSkipCapabilitySwitch : Inserted SIM count: " + i4 + ", insertedStatus: " + i3 + ", tSimCount: " + i13 + ", wSimCount: " + i12 + ", cSimCount: " + i11);
            if (isSupportSimSwitchEnhancement(0) && i4 == 2 && i13 == 2) {
                return true;
            }
            if (isSupportSimSwitchEnhancement(1) && i4 == 2 && i13 == 1 && i12 == 1 && isTPlusWSupport() && iArr[i] != 2 && i10 == 0) {
                return true;
            }
            if (isSupportSimSwitchEnhancement(2) && i4 == 2 && i13 == 1 && i11 == 1 && !isCdmaCard(i, iArr[i], context)) {
                return true;
            }
            if (isSupportSimSwitchEnhancement(3) && i4 == 2 && i12 == 1 && i11 == 1 && !isCdmaCard(i, iArr[i], context)) {
                return true;
            }
            if (isSupportSimSwitchEnhancement(4) && i4 == 2 && i12 == 2) {
                return true;
            }
            if (isSupportSimSwitchEnhancement(5)) {
                r7 = 1;
                r7 = 1;
                if (i4 == 1 && i12 == 1) {
                    return true;
                }
            } else {
                r7 = 1;
            }
            if (4 == iArr[0] && 4 == iArr[r7] && i11 == r7 && i12 == r7 && i9 != i) {
                return r7;
            }
            return false;
        }
        return false;
    }

    public static int getHighestPriorityPhone(int capPhoneId, int[] priority) {
        int targetPhone = 0;
        int phoneNum = priority.length;
        int highestPriorityCount = 0;
        int highestPriorityBitMap = 0;
        for (int i = 0; i < phoneNum; i++) {
            if (priority[i] < priority[targetPhone]) {
                targetPhone = i;
                highestPriorityCount = 1;
                highestPriorityBitMap = 1 << i;
            } else if (priority[i] == priority[targetPhone]) {
                highestPriorityCount++;
                highestPriorityBitMap |= 1 << i;
            }
        }
        if (highestPriorityCount == 1) {
            return targetPhone;
        }
        if (capPhoneId == -1 || ((1 << capPhoneId) & highestPriorityBitMap) == 0) {
            return -1;
        }
        return capPhoneId;
    }

    public static int getMainCapabilityPhoneId() {
        int phoneId = SystemProperties.getInt("persist.vendor.radio.simswitch", 1) - 1;
        return phoneId;
    }

    public static int getDeviceNrType() {
        String uuid = PhoneFactory.getDefaultPhone().getModemUuId();
        int nrType = 0;
        if (uuid.contains("nr1")) {
            nrType = 1;
        } else if (uuid.contains("nr2")) {
            nrType = 2;
        } else if (uuid.contains("nr3")) {
            nrType = 3;
        }
        logd("getDeviceNrType:" + uuid + ", nrType:" + nrType);
        return nrType;
    }

    private static void logd(String s) {
        Rlog.d(LOG_TAG, "[RadioCapSwitchUtil] " + s);
    }

    public static boolean isPS2SupportLTE() {
        if (SystemProperties.get("persist.vendor.radio.mtk_ps2_rat").indexOf(76) != -1) {
            return true;
        }
        return false;
    }

    public static boolean isTPlusWSupport() {
        if (SystemProperties.get("vendor.ril.simswitch.tpluswsupport").equals(IMSI_READY)) {
            return true;
        }
        return false;
    }

    public static String getHashCode(String iccid) {
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-256");
            alga.update(iccid.getBytes());
            byte[] hashCode = alga.digest();
            String strIccid = new String(hashCode);
            return strIccid;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RadioCapabilitySwitchUtil SHA-256 must exist");
        }
    }

    public static boolean isSimOn(int slotId) {
        if (!MtkTelephonyManagerEx.getDefault().isSimOnOffEnabled()) {
            return true;
        }
        int state = MtkTelephonyManagerEx.getDefault().getSimOnOffState(slotId);
        return state != 10;
    }

    public static boolean isRadioOffBySimManagement(int phoneId) {
        int subId = MtkSubscriptionManager.getSubIdUsingPhoneId(phoneId);
        try {
            IMtkTelephonyEx iTelEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
            if (iTelEx == null) {
                logd("iTelEx is null!");
                return false;
            }
            boolean result = iTelEx.isRadioOffBySimManagement(subId);
            return result;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isCapabilitySwitching() {
        try {
            IMtkTelephonyEx iTelEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
            if (iTelEx == null) {
                logd("iTelEx is null!");
                return false;
            }
            boolean result = iTelEx.isCapabilitySwitching();
            return result;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isSubsidyLockFeatureOn() {
        boolean supportSubsidYLock = SystemProperties.get("ro.vendor.mtk_subsidy_lock_support", "0").equals(IMSI_READY);
        int lockPolicy = MtkTelephonyManagerEx.getDefault().getSimLockPolicy();
        if (10 == lockPolicy) {
            return true;
        }
        return supportSubsidYLock;
    }

    public static boolean isSubsidyLockForOmSupported() {
        boolean isSubsidyLockSupported = isSubsidyLockFeatureOn();
        boolean subsidylockStatus = !SystemProperties.get("persist.vendor.subsidylock", "0").equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN);
        return isSubsidyLockSupported && subsidylockStatus;
    }
}
