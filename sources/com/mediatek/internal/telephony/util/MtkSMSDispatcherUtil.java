package com.mediatek.internal.telephony.util;

import android.os.Build;
import android.telephony.Rlog;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.cdma.SmsMessage;
import com.mediatek.internal.telephony.gsm.MtkSmsMessage;

/* JADX INFO: loaded from: classes.dex */
public final class MtkSMSDispatcherUtil {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    static final String TAG = "MtkSMSDispatcherUtil";

    private MtkSMSDispatcherUtil() {
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader) {
        if (isCdma) {
            return getSubmitPduCdma(scAddr, destAddr, message, statusReportRequested, smsHeader);
        }
        return getSubmitPduGsm(scAddr, destAddr, message, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader, int priority, int validityPeriod) {
        if (isCdma) {
            return getSubmitPduCdma(scAddr, destAddr, message, statusReportRequested, smsHeader, priority);
        }
        return getSubmitPduGsm(scAddr, destAddr, message, statusReportRequested, validityPeriod);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String scAddr, String destAddr, String message, boolean statusReportRequested) {
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, message, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String scAddr, String destAddr, String message, boolean statusReportRequested, int validityPeriod) {
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, message, statusReportRequested, validityPeriod);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduCdma(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader) {
        return SmsMessage.getSubmitPdu(scAddr, destAddr, message, statusReportRequested, smsHeader);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduCdma(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader, int priority) {
        return SmsMessage.getSubmitPdu(scAddr, destAddr, message, statusReportRequested, smsHeader, priority);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddr, String destAddr, int destPort, byte[] message, boolean statusReportRequested) {
        if (isCdma) {
            return getSubmitPduCdma(scAddr, destAddr, destPort, message, statusReportRequested);
        }
        return getSubmitPduGsm(scAddr, destAddr, destPort, message, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduCdma(String scAddr, String destAddr, int destPort, byte[] message, boolean statusReportRequested) {
        return SmsMessage.getSubmitPdu(scAddr, destAddr, destPort, message, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String scAddr, String destAddr, int destPort, byte[] message, boolean statusReportRequested) {
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, destPort, message, statusReportRequested);
    }

    public static GsmAlphabet.TextEncodingDetails calculateLength(boolean isCdma, CharSequence messageBody, boolean use7bitOnly) {
        if (isCdma) {
            return calculateLengthCdma(messageBody, use7bitOnly);
        }
        return calculateLengthGsm(messageBody, use7bitOnly);
    }

    public static GsmAlphabet.TextEncodingDetails calculateLengthGsm(CharSequence messageBody, boolean use7bitOnly) {
        return MtkSmsMessage.calculateLength(messageBody, use7bitOnly);
    }

    public static GsmAlphabet.TextEncodingDetails calculateLengthCdma(CharSequence messageBody, boolean use7bitOnly) {
        return SmsMessage.calculateLength(messageBody, use7bitOnly, false);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddr, String destAddr, int destinationPort, int originalPort, byte[] data, boolean statusReportRequested) {
        if (isCdma) {
            return null;
        }
        return getSubmitPduGsm(scAddr, destAddr, destinationPort, originalPort, data, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String scAddr, String destAddr, int destinationPort, int originalPort, byte[] data, boolean statusReportRequested) {
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, destinationPort, originalPort, data, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddress, String destinationAddress, byte[] data, byte[] smsHeaderData, boolean statusReportRequested) {
        if (!isCdma) {
            return MtkSmsMessage.getSubmitPdu(scAddress, destinationAddress, data, smsHeaderData, statusReportRequested);
        }
        return null;
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String scAddress, String destinationAddress, byte[] data, byte[] smsHeaderData, boolean statusReportRequested) {
        return MtkSmsMessage.getSubmitPdu(scAddress, destinationAddress, data, smsHeaderData, statusReportRequested);
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable, int validityPeriod) {
        if (!isCdma) {
            return getSubmitPduGsm(scAddress, destinationAddress, message, statusReportRequested, header, encoding, languageTable, languageShiftTable, validityPeriod);
        }
        return null;
    }

    public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable, int validityPeriod) {
        return MtkSmsMessage.getSubmitPdu(scAddress, destinationAddress, message, statusReportRequested, header, encoding, languageTable, languageShiftTable, validityPeriod);
    }

    private static boolean checkPhoneNumber(char c) {
        return (c >= '0' && c <= '9') || c == '*' || c == '+' || c == '#' || c == 'N' || c == ' ' || c == '-';
    }

    public static boolean checkPhoneNumber(String address) {
        if (address == null) {
            return true;
        }
        Rlog.d(TAG, "checkPhoneNumber");
        int n = address.length();
        for (int i = 0; i < n; i++) {
            if (!checkPhoneNumber(address.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
