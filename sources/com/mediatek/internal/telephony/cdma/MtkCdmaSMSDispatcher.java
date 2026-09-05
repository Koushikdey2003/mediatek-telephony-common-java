package com.mediatek.internal.telephony.cdma;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.cdma.SmsMessage;
import com.mediatek.internal.telephony.util.MtkSMSDispatcherUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkCdmaSMSDispatcher extends CdmaSMSDispatcher {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final int EVENT_COPY_TEXT_MESSAGE_DONE = 106;
    private static final int RESULT_ERROR_RUIM_PLUG_OUT = 107;
    private static final int RESULT_ERROR_SUCCESS = 0;
    private static final String TAG = "MtkCdmaSMSDispatcher";
    private static final boolean VDBG = false;
    private static final int WAKE_LOCK_TIMEOUT = 500;
    private boolean mCopied;
    private ThreadLocal<Integer> mEncodingType;
    protected Object mLock;
    private ThreadLocal<Integer> mOriginalPort;
    protected boolean mSuccess;

    public MtkCdmaSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        super(phone, smsDispatchersController);
        this.mLock = new Object();
        this.mCopied = false;
        this.mSuccess = true;
        this.mOriginalPort = new ThreadLocal<Integer>() { // from class: com.mediatek.internal.telephony.cdma.MtkCdmaSMSDispatcher.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return -1;
            }
        };
        this.mEncodingType = new ThreadLocal<Integer>() { // from class: com.mediatek.internal.telephony.cdma.MtkCdmaSMSDispatcher.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return 0;
            }
        };
        Rlog.d(TAG, "MtkCdmaSMSDispatcher created");
    }

    public void sendSms(SMSDispatcher.SmsTracker tracker) {
        int ss = this.mPhone.getServiceState().getState();
        if (!isIms() && ss != 0) {
            if (isSimAbsent()) {
                tracker.onFailed(this.mContext, 1, 0);
                return;
            } else {
                tracker.onFailed(this.mContext, getNotInServiceError(ss), 0);
                return;
            }
        }
        super.sendSms(tracker);
    }

    public void sendData(String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        this.mOriginalPort.set(Integer.valueOf(originalPort));
        sendData(callingPackage, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent, false);
        this.mOriginalPort.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendData(String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (this.mOriginalPort.get().intValue() == -1) {
            return super.onSendData(destAddr, scAddr, destPort, data, sentIntent, deliveryIntent);
        }
        return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, destPort, this.mOriginalPort.get().intValue(), data, deliveryIntent != null);
    }

    public int copyTextMessageToIccCard(String str, String str2, List<String> list, int i, long j) {
        boolean z;
        SmsMessage.SubmitPdu submitPduCreateEfPdu;
        this.mSuccess = true;
        int size = list.size();
        Rlog.d(TAG, "copyTextMessageToIccCard status = " + i + ", msgCount = " + size);
        if (size < 1) {
            return 1;
        }
        if (i == 1 || i == 3) {
            Rlog.d(TAG, "copyTextMessageToIccCard to encode deliver pdu");
            z = true;
        } else if (i == 5 || i == 7) {
            Rlog.d(TAG, "copyTextMessageToIccCard to encode submit pdu");
            z = false;
        } else {
            Rlog.d(TAG, "copyTextMessageToIccCard invalid status, default is deliver pdu");
            return 1;
        }
        if (!z && !MtkSMSDispatcherUtil.checkPhoneNumber(str2)) {
            Rlog.d(TAG, "copyTextMessageToIccCard invalid dest address");
            return 8;
        }
        for (int i2 = 0; i2 < size; i2++) {
            if (!this.mSuccess || (submitPduCreateEfPdu = MtkSmsMessage.createEfPdu(str2, list.get(i2), j)) == null) {
                return 1;
            }
            this.mCi.writeSmsToRuim(i, submitPduCreateEfPdu.encodedMessage, obtainMessage(EVENT_COPY_TEXT_MESSAGE_DONE));
            synchronized (this.mLock) {
                this.mCopied = false;
                while (!this.mCopied) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        return 1;
                    }
                }
            }
        }
        return 1 ^ (this.mSuccess ? 1 : 0);
    }

    public void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, false, 0L);
        this.mEncodingType.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendText(String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 1 || encodingType == 2 || encodingType == 3) {
            return MtkSmsMessage.getSubmitPdu(scAddr, destAddr, text, deliveryIntent != null, (SmsHeader) null, encodingType, validityPeriod, priority, true);
        }
        return super.onSendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
    }

    public void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendMultipartTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, 0L);
        this.mEncodingType.remove();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5, types: [int] */
    /* JADX WARN: Type inference failed for: r8v9 */
    public void sendMultipartText(String str, String str2, ArrayList<String> arrayList, ArrayList<PendingIntent> arrayList2, ArrayList<PendingIntent> arrayList3, Uri uri, String str3, boolean z, int i, boolean z2, int i2, long j) {
        PendingIntent pendingIntent;
        PendingIntent pendingIntent2;
        ArrayList<PendingIntent> arrayList4 = arrayList2;
        String multipartMessageText = getMultipartMessageText(arrayList);
        int nextConcatenatedRef = getNextConcatenatedRef() & 255;
        int size = arrayList.size();
        if (size < 1) {
            triggerSentIntentForFailure(arrayList4);
            return;
        }
        GsmAlphabet.TextEncodingDetails[] textEncodingDetailsArr = new GsmAlphabet.TextEncodingDetails[size];
        int i3 = size;
        int i4 = nextConcatenatedRef;
        int iOnSendMultipartText = onSendMultipartText(str, str2, arrayList, arrayList2, arrayList3, uri, str3, z, i, z2, i2, textEncodingDetailsArr);
        SMSDispatcher.SmsTracker[] smsTrackerArr = new SMSDispatcher.SmsTracker[i3];
        AtomicInteger atomicInteger = new AtomicInteger(i3);
        boolean z3 = false;
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        int i5 = 0;
        while (i5 < i3) {
            SmsHeader.ConcatRef concatRef = new SmsHeader.ConcatRef();
            concatRef.refNumber = i4;
            concatRef.seqNumber = i5 + 1;
            concatRef.msgCount = i3;
            concatRef.isEightBits = true;
            SmsHeader smsHeaderMakeSmsHeader = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeSmsHeader();
            smsHeaderMakeSmsHeader.concatRef = concatRef;
            if (iOnSendMultipartText == 1) {
                smsHeaderMakeSmsHeader.languageTable = textEncodingDetailsArr[i5].languageTable;
                smsHeaderMakeSmsHeader.languageShiftTable = textEncodingDetailsArr[i5].languageShiftTable;
            }
            if (arrayList4 != null && arrayList2.size() > i5) {
                pendingIntent = arrayList4.get(i5);
            } else {
                pendingIntent = null;
            }
            if (arrayList3 != null && arrayList3.size() > i5) {
                pendingIntent2 = arrayList3.get(i5);
            } else {
                pendingIntent2 = null;
            }
            int i6 = i4;
            int i7 = i5;
            boolean z4 = z3;
            SMSDispatcher.SmsTracker[] smsTrackerArr2 = smsTrackerArr;
            int i8 = iOnSendMultipartText;
            int i9 = i3;
            smsTrackerArr2[i7] = getNewSubmitPduTracker(str3, str, str2, arrayList.get(i5), smsHeaderMakeSmsHeader, iOnSendMultipartText, pendingIntent, pendingIntent2, i5 == i3 + (-1) ? true : z3, atomicInteger, atomicBoolean, uri, multipartMessageText, i, z2, i2, j, nextMessageRef());
            if (smsTrackerArr2[i7] != null) {
                i5 = i7 + 1;
                smsTrackerArr = smsTrackerArr2;
                arrayList4 = arrayList2;
                i4 = i6;
                z3 = z4;
                iOnSendMultipartText = i8;
                i3 = i9;
            } else {
                triggerSentIntentForFailure(arrayList2);
                return;
            }
        }
        boolean z5 = z3;
        SMSDispatcher.SmsTracker[] smsTrackerArr3 = smsTrackerArr;
        String carrierAppPackageName = getCarrierAppPackageName();
        if (carrierAppPackageName != null) {
            Rlog.d(TAG, "Found carrier package.");
            SMSDispatcher.MultipartSmsSender multipartSmsSender = new SMSDispatcher.MultipartSmsSender(this, arrayList, smsTrackerArr3);
            multipartSmsSender.sendSmsByCarrierApp(carrierAppPackageName, new SMSDispatcher.SmsSenderCallback(this, multipartSmsSender));
        } else {
            Rlog.v(TAG, "No carrier package.");
            int length = smsTrackerArr3.length;
            for (?? r8 = z5; r8 < length; r8++) {
                sendSubmitPdu(smsTrackerArr3[r8]);
            }
        }
    }

    protected int onSendMultipartText(String destAddr, String scAddr, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod, GsmAlphabet.TextEncodingDetails[] encodingForParts) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 1 || encodingType == 2 || encodingType == 3) {
            int msgCount = parts.size();
            for (int i = 0; i < msgCount; i++) {
                GsmAlphabet.TextEncodingDetails details = MtkSmsMessage.calculateLength(parts.get(i), false, encodingType);
                details.codeUnitSize = encodingType;
                encodingForParts[i] = details;
            }
            return encodingType;
        }
        return super.onSendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, encodingForParts);
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_COPY_TEXT_MESSAGE_DONE /* 106 */:
                AsyncResult ar = (AsyncResult) msg.obj;
                synchronized (this.mLock) {
                    this.mSuccess = ar.exception == null;
                    this.mCopied = true;
                    this.mLock.notifyAll();
                    break;
                }
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    protected void handleSendComplete(AsyncResult ar) {
        SMSDispatcher.SmsTracker tracker = (SMSDispatcher.SmsTracker) ar.userObj;
        if (ar.exception != null && ar.result != null) {
            int errorCode = ((SmsResponse) ar.result).mErrorCode;
            if (errorCode == RESULT_ERROR_RUIM_PLUG_OUT) {
                Rlog.d(TAG, "RUIM card is plug out");
                tracker.onFailed(this.mContext, 1, errorCode);
                return;
            }
            int ss = this.mPhone.getServiceState().getState();
            if (!isIms() && ss != 0 && isSimAbsent()) {
                tracker.onFailed(this.mContext, 1, errorCode);
                return;
            }
        }
        super.handleSendComplete(ar);
    }

    private boolean isSimAbsent() {
        IccCardConstants.State state;
        IccCard card = PhoneFactory.getPhone(this.mPhone.getPhoneId()).getIccCard();
        if (card == null) {
            state = IccCardConstants.State.UNKNOWN;
        } else {
            state = card.getState();
        }
        boolean ret = state == IccCardConstants.State.ABSENT || state == IccCardConstants.State.NOT_READY;
        Rlog.d(TAG, "isSimAbsent state = " + state + " ret=" + ret);
        return ret;
    }
}
