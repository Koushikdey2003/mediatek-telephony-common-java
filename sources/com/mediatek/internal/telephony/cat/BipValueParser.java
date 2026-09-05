package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.ResultException;
import java.net.UnknownHostException;

/* JADX INFO: loaded from: classes.dex */
abstract class BipValueParser {
    BipValueParser() {
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static BearerDesc retrieveBearerDesc(ComprehensionTlv ctlv) throws ResultException {
        int bearerType;
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        int length = ctlv.getLength();
        int valueIndex2 = valueIndex + 1;
        try {
            bearerType = rawValue[valueIndex] & 255;
            MtkCatLog.d("CAT", "retrieveBearerDesc: bearerType:" + bearerType + ", length: " + length);
        } catch (IndexOutOfBoundsException e) {
        }
        try {
            if (2 == bearerType) {
                GPRSBearerDesc gprsbearerDesc = new GPRSBearerDesc();
                int valueIndex3 = valueIndex2 + 1;
                gprsbearerDesc.precedence = rawValue[valueIndex2] & 255;
                int valueIndex4 = valueIndex3 + 1;
                gprsbearerDesc.delay = rawValue[valueIndex3] & 255;
                int valueIndex5 = valueIndex4 + 1;
                gprsbearerDesc.reliability = rawValue[valueIndex4] & 255;
                int valueIndex6 = valueIndex5 + 1;
                gprsbearerDesc.peak = rawValue[valueIndex5] & 255;
                int valueIndex7 = valueIndex6 + 1;
                gprsbearerDesc.mean = rawValue[valueIndex6] & 255;
                int i = valueIndex7 + 1;
                gprsbearerDesc.pdpType = rawValue[valueIndex7] & 255;
                return gprsbearerDesc;
            }
            if (9 == bearerType) {
                UTranBearerDesc uTranbearerDesc = new UTranBearerDesc();
                int valueIndex8 = valueIndex2 + 1;
                uTranbearerDesc.trafficClass = rawValue[valueIndex2] & 255;
                int valueIndex9 = valueIndex8 + 1;
                uTranbearerDesc.maxBitRateUL_High = rawValue[valueIndex8] & 255;
                int valueIndex10 = valueIndex9 + 1;
                uTranbearerDesc.maxBitRateUL_Low = rawValue[valueIndex9] & 255;
                int valueIndex11 = valueIndex10 + 1;
                uTranbearerDesc.maxBitRateDL_High = rawValue[valueIndex10] & 255;
                int valueIndex12 = valueIndex11 + 1;
                uTranbearerDesc.maxBitRateDL_Low = rawValue[valueIndex11] & 255;
                int valueIndex13 = valueIndex12 + 1;
                uTranbearerDesc.guarBitRateUL_High = rawValue[valueIndex12] & 255;
                int valueIndex14 = valueIndex13 + 1;
                uTranbearerDesc.guarBitRateUL_Low = rawValue[valueIndex13] & 255;
                int valueIndex15 = valueIndex14 + 1;
                uTranbearerDesc.guarBitRateDL_High = rawValue[valueIndex14] & 255;
                int valueIndex16 = valueIndex15 + 1;
                uTranbearerDesc.guarBitRateDL_Low = rawValue[valueIndex15] & 255;
                int valueIndex17 = valueIndex16 + 1;
                uTranbearerDesc.deliveryOrder = rawValue[valueIndex16] & 255;
                int valueIndex18 = valueIndex17 + 1;
                uTranbearerDesc.maxSduSize = rawValue[valueIndex17] & 255;
                int valueIndex19 = valueIndex18 + 1;
                uTranbearerDesc.sduErrorRatio = rawValue[valueIndex18] & 255;
                int valueIndex20 = valueIndex19 + 1;
                uTranbearerDesc.residualBitErrorRadio = rawValue[valueIndex19] & 255;
                int valueIndex21 = valueIndex20 + 1;
                uTranbearerDesc.deliveryOfErroneousSdus = rawValue[valueIndex20] & 255;
                int valueIndex22 = valueIndex21 + 1;
                uTranbearerDesc.transferDelay = rawValue[valueIndex21] & 255;
                int valueIndex23 = valueIndex22 + 1;
                uTranbearerDesc.trafficHandlingPriority = rawValue[valueIndex22] & 255;
                int i2 = valueIndex23 + 1;
                uTranbearerDesc.pdpType = rawValue[valueIndex23] & 255;
                return uTranbearerDesc;
            }
            if (11 == bearerType) {
                EUTranBearerDesc euTranbearerDesc = new EUTranBearerDesc();
                int valueIndex24 = valueIndex2 + 1;
                euTranbearerDesc.QCI = rawValue[valueIndex2] & 255;
                int valueIndex25 = valueIndex24 + 1;
                euTranbearerDesc.maxBitRateU = rawValue[valueIndex24] & 255;
                int valueIndex26 = valueIndex25 + 1;
                euTranbearerDesc.maxBitRateD = rawValue[valueIndex25] & 255;
                int valueIndex27 = valueIndex26 + 1;
                euTranbearerDesc.guarBitRateU = rawValue[valueIndex26] & 255;
                int valueIndex28 = valueIndex27 + 1;
                euTranbearerDesc.guarBitRateD = rawValue[valueIndex27] & 255;
                int valueIndex29 = valueIndex28 + 1;
                euTranbearerDesc.maxBitRateUEx = rawValue[valueIndex28] & 255;
                int valueIndex30 = valueIndex29 + 1;
                euTranbearerDesc.maxBitRateDEx = rawValue[valueIndex29] & 255;
                int valueIndex31 = valueIndex30 + 1;
                euTranbearerDesc.guarBitRateUEx = rawValue[valueIndex30] & 255;
                int valueIndex32 = valueIndex31 + 1;
                euTranbearerDesc.guarBitRateDEx = rawValue[valueIndex31] & 255;
                int i3 = valueIndex32 + 1;
                euTranbearerDesc.pdnType = rawValue[valueIndex32] & 255;
                return euTranbearerDesc;
            }
            if (3 == bearerType) {
                DefaultBearerDesc defaultbearerDesc = new DefaultBearerDesc();
                return defaultbearerDesc;
            }
            if (1 == bearerType) {
                MtkCatLog.d("CAT", "retrieveBearerDesc: unsupport CSD");
                throw new ResultException(ResultCode.BEYOND_TERMINAL_CAPABILITY);
            }
            MtkCatLog.d("CAT", "retrieveBearerDesc: un-understood bearer type");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        } catch (IndexOutOfBoundsException e2) {
            MtkCatLog.d("CAT", "retrieveBearerDesc: out of bounds");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static int retrieveBufferSize(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        try {
            int size = ((rawValue[valueIndex] & 255) << 8) + (rawValue[valueIndex + 1] & 255);
            return size;
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("CAT", "retrieveBufferSize: out of bounds");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static String retrieveNetworkAccessName(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        String networkAccessName = null;
        try {
            int totalLen = ctlv.getLength();
            new String(rawValue, valueIndex, totalLen);
            String stkNetworkIdentifier = null;
            String stkOperatorIdentifier = null;
            if (totalLen > 0) {
                int valueIndex2 = valueIndex + 1;
                try {
                    int len = rawValue[valueIndex];
                    if (totalLen > len) {
                        stkNetworkIdentifier = new String(rawValue, valueIndex2, len);
                        valueIndex2 += len;
                    }
                    MtkCatLog.d("CAT", "totalLen:" + totalLen + ";" + valueIndex2 + ";" + len);
                    while (totalLen > len + 1) {
                        totalLen -= len + 1;
                        int valueIndex3 = valueIndex2 + 1;
                        try {
                            len = rawValue[valueIndex2];
                            MtkCatLog.d("CAT", "next len: " + len);
                            if (totalLen > len) {
                                String tmp_string = new String(rawValue, valueIndex3, len);
                                if (stkOperatorIdentifier == null) {
                                    stkOperatorIdentifier = tmp_string;
                                } else {
                                    stkOperatorIdentifier = stkOperatorIdentifier + "." + tmp_string;
                                }
                            }
                            valueIndex2 = valueIndex3 + len;
                            MtkCatLog.d("CAT", "totalLen:" + totalLen + ";" + valueIndex2 + ";" + len);
                        } catch (IndexOutOfBoundsException e) {
                            MtkCatLog.d("CAT", "retrieveNetworkAccessName: out of bounds");
                            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
                        }
                    }
                    if (stkNetworkIdentifier != null && stkOperatorIdentifier != null) {
                        networkAccessName = stkNetworkIdentifier + "." + stkOperatorIdentifier;
                    } else if (stkNetworkIdentifier != null) {
                        networkAccessName = stkNetworkIdentifier;
                    }
                    MtkCatLog.d("CAT", "nw:" + stkNetworkIdentifier + ";" + stkOperatorIdentifier);
                } catch (IndexOutOfBoundsException e2) {
                }
            }
            return networkAccessName;
        } catch (IndexOutOfBoundsException e3) {
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static TransportProtocol retrieveTransportProtocol(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        int valueIndex2 = valueIndex + 1;
        try {
            int protocolType = rawValue[valueIndex];
            int portNumber = ((rawValue[valueIndex2] & 255) << 8) + (rawValue[valueIndex2 + 1] & 255);
            return new TransportProtocol(protocolType, portNumber);
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("CAT", "retrieveTransportProtocol: out of bounds");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    static OtherAddress retrieveOtherAddress(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        int valueIndex2 = valueIndex + 1;
        try {
            int addressType = rawValue[valueIndex];
            if (33 == addressType) {
                OtherAddress otherAddress = new OtherAddress(addressType, rawValue, valueIndex2);
                return otherAddress;
            }
            if (87 != addressType) {
                return null;
            }
            OtherAddress otherAddress2 = new OtherAddress(addressType, rawValue, valueIndex2);
            return otherAddress2;
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("CAT", "retrieveOtherAddress: out of bounds");
            return null;
        } catch (UnknownHostException e2) {
            MtkCatLog.d("CAT", "retrieveOtherAddress: unknown host");
            return null;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static int retrieveChannelDataLength(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        MtkCatLog.d("CAT", "valueIndex:" + valueIndex);
        try {
            int length = rawValue[valueIndex] & 255;
            return length;
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("CAT", "retrieveTransportProtocol: out of bounds");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    static byte[] retrieveChannelData(ComprehensionTlv ctlv) throws ResultException {
        byte[] rawValue = ctlv.getRawValue();
        int valueIndex = ctlv.getValueIndex();
        try {
            byte[] channelData = new byte[ctlv.getLength()];
            System.arraycopy(rawValue, valueIndex, channelData, 0, channelData.length);
            return channelData;
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("CAT", "retrieveChannelData: out of bounds");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
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
}
