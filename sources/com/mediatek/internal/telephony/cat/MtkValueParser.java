package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.Item;
import com.android.internal.telephony.cat.ItemsIconId;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.uicc.IccUtils;

/* JADX INFO: loaded from: classes.dex */
abstract class MtkValueParser {
    MtkValueParser() {
    }

    static Item retrieveItem(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        int length = ctlv.getLength();
        if (length == 0) {
            return null;
        }
        int textLen = length - 1;
        try {
            int id = rawValue[valueIndex] & 255;
            String text = IccUtils.adnStringFieldToString(rawValue, valueIndex + 1, removeInvalidCharInItemTextString(rawValue, valueIndex, textLen));
            Item item = new Item(id, text);
            return item;
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("ValueParser", "retrieveItem fail");
            return null;
        }
    }

    static int removeInvalidCharInItemTextString(byte[] rawValue, int valueIndex, int textLen) {
        Boolean isucs2 = false;
        int len = textLen;
        if ((textLen >= 1 && rawValue[valueIndex + 1] == -128) || ((textLen >= 3 && rawValue[valueIndex + 1] == -127) || (textLen >= 4 && rawValue[valueIndex + 1] == -126))) {
            isucs2 = true;
        }
        if (!isucs2.booleanValue() && textLen > 0) {
            for (int i = textLen; i > 0 && rawValue[valueIndex + i] == -16; i--) {
                len--;
            }
        }
        return len;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static String retrieveAlphaId(ComprehensionTlv ctlv, boolean noAlphaUsrCnf) throws ResultException {
        if (ctlv != null) {
            byte[] rawValue = ctlv.getRawValue();
            int valueIndex = ctlv.getValueIndex();
            int length = ctlv.getLength();
            if (length != 0) {
                try {
                    return IccUtils.adnStringFieldToString(rawValue, valueIndex, length);
                } catch (IndexOutOfBoundsException e) {
                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
                }
            }
            MtkCatLog.d("ValueParser", "Alpha Id length=" + length);
            return "";
        }
        if (noAlphaUsrCnf) {
            return null;
        }
        return "Default Message";
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static byte[] retrieveNextActionIndicator(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        int length = ctlv.getLength();
        byte[] nai = new byte[length];
        int index = 0;
        while (index < length) {
            int index2 = index + 1;
            int valueIndex2 = valueIndex + 1;
            try {
                nai[index] = rawValue[valueIndex];
                index = index2;
                valueIndex = valueIndex2;
            } catch (IndexOutOfBoundsException e) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
        }
        return nai;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static int retrieveTarget(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        try {
            int target = rawValue[valueIndex] & 255;
            return target;
        } catch (IndexOutOfBoundsException e) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static ItemsIconId retrieveItemsIconId(ComprehensionTlv ctlv) throws ResultException {
        MtkCatLog.d("ValueParser", "retrieveItemsIconId:");
        ItemsIconId id = new ItemsIconId();
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        boolean z = true;
        int numOfItems = ctlv.getLength() - 1;
        try {
            id.recordNumbers = new int[numOfItems];
            int valueIndex2 = valueIndex + 1;
            try {
                if ((rawValue[valueIndex] & 255) != 0) {
                    z = false;
                }
                id.selfExplanatory = z;
                int index = 0;
                while (index < numOfItems) {
                    int index2 = index + 1;
                    int valueIndex3 = valueIndex2 + 1;
                    try {
                        id.recordNumbers[index] = rawValue[valueIndex2];
                        index = index2;
                        valueIndex2 = valueIndex3;
                    } catch (IndexOutOfBoundsException e) {
                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
                    } catch (NegativeArraySizeException e2) {
                        MtkCatLog.d("ValueParser", "retrieveItemsIconId: numOfItems = -1");
                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
                    }
                }
                return id;
            } catch (IndexOutOfBoundsException e3) {
            } catch (NegativeArraySizeException e4) {
            }
        } catch (IndexOutOfBoundsException e5) {
        } catch (NegativeArraySizeException e6) {
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static MtkDeviceIdentities retrieveDeviceIdentities(ComprehensionTlv ctlv) throws ResultException {
        MtkDeviceIdentities devIds = new MtkDeviceIdentities();
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        try {
            devIds.sourceId = rawValue[valueIndex] & 255;
            devIds.destinationId = rawValue[valueIndex + 1] & 255;
            return devIds;
        } catch (IndexOutOfBoundsException e) {
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
    }
}
