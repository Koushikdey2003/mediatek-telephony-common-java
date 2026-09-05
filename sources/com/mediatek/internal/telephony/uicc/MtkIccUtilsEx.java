package com.mediatek.internal.telephony.uicc;

import android.os.SystemProperties;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccUtils;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.nio.charset.Charset;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MtkIccUtilsEx extends IccUtils {
    public static final int CDMA_CARD_TYPE_NOT_3GCARD = 0;
    public static final int CDMA_CARD_TYPE_RUIM_SIM = 2;
    public static final int CDMA_CARD_TYPE_UIM_ONLY = 1;
    static final String MTK_LOG_TAG = "MtkIccUtilsEx";
    protected static final int TAG_FULL_NETWORK_NAME = 67;
    private static final int TAG_ISIM_VALUE = 128;
    protected static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    protected static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};

    public static String parseSpnToString(int family, byte[] data) {
        if (data == null) {
            return null;
        }
        if (1 == family) {
            return IccUtils.adnStringFieldToString(data, 1, data.length - 1);
        }
        if (2 == family) {
            int encoding = data[1];
            byte b = data[2];
            byte[] spnData = new byte[32];
            int len = data.length - 3 < 32 ? data.length - 3 : 32;
            System.arraycopy(data, 3, spnData, 0, len);
            int numBytes = 0;
            while (numBytes < spnData.length && (spnData[numBytes] & 255) != 255) {
                numBytes++;
            }
            if (numBytes == 0) {
                return "";
            }
            try {
                switch (encoding) {
                    case 0:
                    case 8:
                        return new String(spnData, 0, numBytes, "ISO-8859-1");
                    case 1:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        Rlog.d(MTK_LOG_TAG, "spn decode error: " + encoding);
                        break;
                    case 2:
                        String spn = new String(spnData, 0, numBytes, "US-ASCII");
                        if (!TextUtils.isPrintableAsciiOnly(spn)) {
                            return GsmAlphabet.gsm7BitPackedToString(spnData, 0, (numBytes * 8) / 7);
                        }
                        return spn;
                    case 3:
                    case 9:
                        return GsmAlphabet.gsm7BitPackedToString(spnData, 0, (numBytes * 8) / 7);
                    case 4:
                        return new String(spnData, 0, numBytes, "utf-16");
                }
            } catch (Exception e) {
                Rlog.d(MTK_LOG_TAG, "spn decode error: " + e);
            }
        }
        return null;
    }

    public static String parsePnnToString(byte[] data) {
        if (data == null) {
            return null;
        }
        SimTlv tlv = new SimTlv(data, 0, data.length);
        while (tlv.isValidObject()) {
            if (tlv.getTag() != TAG_FULL_NETWORK_NAME) {
                tlv.nextObject();
            } else {
                return networkNameToString(tlv.getData(), 0, tlv.getData().length);
            }
        }
        return null;
    }

    public static String parseImpiToString(byte[] data) {
        if (data == null) {
            return null;
        }
        SimTlv tlv = new SimTlv(data, 0, data.length);
        do {
            if (tlv.isValidObject() && tlv.getTag() == 128) {
                return new String(tlv.getData(), Charset.forName("UTF-8"));
            }
        } while (tlv.nextObject());
        Rlog.d(MTK_LOG_TAG, "[ISIM] can't find TLV. record = " + IccUtils.bytesToHexString(data));
        return null;
    }

    public static int checkCdma3gCard(int slotId) {
        String[] values = null;
        int cdma3gCardType = -1;
        if (slotId >= 0) {
            String[] strArr = PROPERTY_RIL_FULL_UICC_TYPE;
            if (slotId < strArr.length) {
                String prop = SystemProperties.get(strArr[slotId]);
                if (prop != null && prop.length() > 0) {
                    values = prop.split(",");
                }
                if (values != null) {
                    if (Arrays.asList(values).contains("RUIM") && Arrays.asList(values).contains("SIM")) {
                        cdma3gCardType = 2;
                    } else if ((!Arrays.asList(values).contains("USIM") && !Arrays.asList(values).contains("SIM")) || (Arrays.asList(values).contains("SIM") && RadioCapabilitySwitchUtil.IMSI_READY.equals(SystemProperties.get(PROPERTY_RIL_CT3G[slotId])))) {
                        cdma3gCardType = 1;
                    } else {
                        cdma3gCardType = 0;
                    }
                }
                Rlog.d(MTK_LOG_TAG, "checkCdma3gCard slotId " + slotId + ", prop value = " + prop + ", size = " + (values != null ? values.length : 0) + ", cdma3gCardType = " + cdma3gCardType);
                return cdma3gCardType;
            }
        }
        Rlog.d(MTK_LOG_TAG, "checkCdma3gCard: invalid slotId " + slotId);
        return -1;
    }

    public static String getPrintableString(String str, int length) {
        if (str == null) {
            return null;
        }
        if (str.length() > length) {
            String strToPrint = str.substring(0, length) + Rlog.pii(false, str.substring(length));
            return strToPrint;
        }
        return str;
    }

    public static IccCardApplicationStatus.PersoSubState PersoSubstateFromRILInt(int substate) {
        switch (substate) {
            case 100:
                IccCardApplicationStatus.PersoSubState newSubState = IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM_C;
                return newSubState;
            case 101:
                IccCardApplicationStatus.PersoSubState newSubState2 = IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM_C_PUK;
                return newSubState2;
            default:
                IccCardApplicationStatus.PersoSubState newSubState3 = IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_UNKNOWN;
                return newSubState3;
        }
    }
}
