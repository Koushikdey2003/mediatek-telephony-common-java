package com.mediatek.internal.telephony.gsm;

import android.app.PendingIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import com.android.internal.telephony.AsyncEmergencyContactNotifier;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsRawData;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsMessage;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.mediatek.internal.telephony.MtkSmsDispatchersController;
import com.mediatek.internal.telephony.MtkSmsHeader;
import com.mediatek.internal.telephony.MtkSmsLocationHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmSMSDispatcher extends GsmSMSDispatcher {
    protected static final int EVENT_COPY_TEXT_MESSAGE_DONE = 106;
    private static final int EVENT_SEND_SMS_AFTER_SET_LOC = 30;
    private static final String TAG = "MtkGsmSMSDispatcher";
    private static final int TIME_SEND_SMS_AFTER_SET_LOC = 5000;
    private ThreadLocal<Integer> mEncodingType;
    protected Object mLock;
    private boolean mStorageAvailable;
    private boolean mSuccess;
    protected int messageCountNeedCopy;
    private static final boolean ENG = "eng".equals(Build.TYPE);
    protected static String PDU_SIZE = "pdu_size";
    protected static String MSG_REF_NUM = "msg_ref_num";

    /* JADX WARN: Multi-variable type inference failed */
    public MtkGsmSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, GsmInboundSmsHandler gsmInboundSmsHandler) {
        super(phone, smsDispatchersController, gsmInboundSmsHandler);
        this.mStorageAvailable = true;
        this.mSuccess = true;
        this.messageCountNeedCopy = 0;
        this.mLock = new Object();
        this.mEncodingType = new ThreadLocal<Integer>() { // from class: com.mediatek.internal.telephony.gsm.MtkGsmSMSDispatcher.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return 0;
            }
        };
        this.mUiccController.unregisterForIccChanged(this);
        Integer phoneId = new Integer(this.mPhone.getPhoneId());
        this.mUiccController.registerForIccChanged(this, 15, phoneId);
        Rlog.d(TAG, "MtkGsmSMSDispatcher created");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        super.dispose();
        this.mCi.unSetOnSmsStatus(this);
        this.mUiccController.unregisterForIccChanged(this);
    }

    public String getFormat() {
        return "3gpp";
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 15:
                Integer phoneId = getUiccControllerPhoneId(msg);
                if (phoneId.intValue() != this.mPhone.getPhoneId()) {
                    Rlog.d(TAG, "Wrong phone id event coming, PhoneId: " + phoneId);
                    return;
                } else {
                    Rlog.d(TAG, "EVENT_ICC_CHANGED, PhoneId: " + phoneId + " match exactly.");
                    onUpdateIccAvailability();
                    return;
                }
            case 30:
                Rlog.e(TAG, "EVENT_SEND_SMS_AFTER_SET_LOC");
                SMSDispatcher.SmsTracker[] trackers = (SMSDispatcher.SmsTracker[]) msg.obj;
                for (SMSDispatcher.SmsTracker tracker : trackers) {
                    Rlog.e(TAG, "send sms after seconds");
                    sendSms(tracker);
                }
                return;
            case EVENT_COPY_TEXT_MESSAGE_DONE /* 106 */:
                AsyncResult ar = (AsyncResult) msg.obj;
                synchronized (this.mLock) {
                    boolean z = ar.exception == null;
                    this.mSuccess = z;
                    if (z) {
                        Rlog.d(TAG, "[copyText success to copy one");
                        this.messageCountNeedCopy--;
                    } else {
                        Rlog.d(TAG, "[copyText fail to copy one");
                        this.messageCountNeedCopy = 0;
                    }
                    this.mLock.notifyAll();
                    break;
                }
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    protected SMSDispatcher.SmsTracker getNewSubmitPduTracker(String callingPackage, String destinationAddress, String scAddress, String message, SmsHeader smsHeader, int encoding, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean lastPart, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, String fullMessageText, int priority, boolean expectMore, int validityPeriod, long messageId, int messageRef) {
        if (ENG) {
            Rlog.d(TAG, "getNewSubmitPduTracker w/ validity");
        }
        SmsMessage.SubmitPdu pdu = MtkSmsMessage.getSubmitPdu(scAddress, destinationAddress, message, deliveryIntent != null, SmsHeader.toByteArray(smsHeader), encoding, smsHeader.languageTable, smsHeader.languageShiftTable, validityPeriod);
        if (pdu != null) {
            HashMap map = getSmsTrackerMap(destinationAddress, scAddress, message, pdu);
            return getSmsTracker(callingPackage, map, sentIntent, deliveryIntent, getFormat(), unsentPartCount, anyPartFailed, messageUri, smsHeader, !lastPart || expectMore, fullMessageText, true, true, priority, validityPeriod, false, messageId, messageRef, false);
        }
        Rlog.e(TAG, "GsmSMSDispatcher.getNewSubmitPduTracker(): getSubmitPdu() returned null");
        return null;
    }

    private Integer getUiccControllerPhoneId(Message msg) {
        Integer phoneId = new Integer(-1);
        AsyncResult ar = (AsyncResult) msg.obj;
        if (ar != null && (ar.result instanceof Integer)) {
            return (Integer) ar.result;
        }
        return phoneId;
    }

    public void sendData(String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "MtkGsmSmsDispatcher.sendData: enter");
        SmsMessage.SubmitPdu pdu = MtkSmsMessage.getSubmitPdu(scAddr, destAddr, destPort, originalPort, data, deliveryIntent != null);
        if (pdu != null) {
            HashMap map = getSmsTrackerMap(destAddr, scAddr, destPort, data, pdu);
            int messageRef = nextMessageRef();
            SMSDispatcher.SmsTracker tracker = getSmsTracker(callingPackage, map, sentIntent, deliveryIntent, getFormat(), null, false, null, false, true, false, 0L, messageRef);
            if (!sendSmsByCarrierApp(true, tracker)) {
                sendSubmitPdu(tracker);
                return;
            }
            return;
        }
        Rlog.e(TAG, "GsmSMSDispatcher.sendData(): getSubmitPdu() returned null");
    }

    public void sendMultipartData(String str, String str2, String str3, int i, ArrayList<SmsRawData> arrayList, ArrayList<PendingIntent> arrayList2, ArrayList<PendingIntent> arrayList3) {
        int i2;
        String str4;
        PendingIntent pendingIntent;
        PendingIntent pendingIntent2;
        ArrayList<SmsRawData> arrayList4 = arrayList;
        ArrayList<PendingIntent> arrayList5 = arrayList2;
        ArrayList<PendingIntent> arrayList6 = arrayList3;
        String str5 = TAG;
        if (arrayList4 == null) {
            Rlog.e(TAG, "Cannot send multipart data when data is null!");
            return;
        }
        int nextConcatenatedRef = getNextConcatenatedRef() & 255;
        int size = arrayList.size();
        SMSDispatcher.SmsTracker[] smsTrackerArr = new SMSDispatcher.SmsTracker[size];
        int i3 = 0;
        while (true) {
            i2 = 0;
            boolean z = false;
            if (i3 >= size) {
                break;
            }
            byte[] submitPduHeader = MtkSmsHeader.getSubmitPduHeader(i, nextConcatenatedRef, i3 + 1, size);
            if (arrayList5 != null && arrayList2.size() > i3) {
                pendingIntent = arrayList5.get(i3);
            } else {
                pendingIntent = null;
            }
            if (arrayList6 != null && arrayList3.size() > i3) {
                pendingIntent2 = arrayList6.get(i3);
            } else {
                pendingIntent2 = null;
            }
            byte[] bytes = arrayList4.get(i3).getBytes();
            if (pendingIntent2 != null) {
                z = true;
            }
            int i4 = i3;
            SMSDispatcher.SmsTracker[] smsTrackerArr2 = smsTrackerArr;
            smsTrackerArr2[i4] = getSmsTracker(str, getSmsTrackerMap(str2, str3, i, arrayList4.get(i3).getBytes(), MtkSmsMessage.getSubmitPdu(str3, str2, bytes, submitPduHeader, z)), pendingIntent, pendingIntent2, getFormat(), null, false, null, false, true, false, 0L, nextMessageRef());
            i3 = i4 + 1;
            arrayList4 = arrayList;
            arrayList5 = arrayList2;
            arrayList6 = arrayList3;
            smsTrackerArr = smsTrackerArr2;
            size = size;
            nextConcatenatedRef = nextConcatenatedRef;
            str5 = str5;
        }
        SMSDispatcher.SmsTracker[] smsTrackerArr3 = smsTrackerArr;
        String str6 = str5;
        if (smsTrackerArr3.length == 0 || smsTrackerArr3[0] == null) {
            String str7 = str6;
            Rlog.e(str7, "Cannot send multipart data. trackers length = " + smsTrackerArr3.length);
            return;
        }
        int length = smsTrackerArr3.length;
        while (i2 < length) {
            SMSDispatcher.SmsTracker smsTracker = smsTrackerArr3[i2];
            if (smsTracker != null) {
                if (sendSmsByCarrierApp(true, smsTracker)) {
                    str4 = str6;
                } else {
                    sendSubmitPdu(smsTracker);
                    str4 = str6;
                }
            } else {
                str4 = str6;
                Rlog.e(str4, "Null tracker.");
            }
            i2++;
            str6 = str4;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x0139  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0155  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01c1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int copyTextMessageToIccCard(java.lang.String r26, java.lang.String r27, java.util.List<java.lang.String> r28, int r29, long r30) {
        /*
            Method dump skipped, instruction units count: 602
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.gsm.MtkGsmSMSDispatcher.copyTextMessageToIccCard(java.lang.String, java.lang.String, java.util.List, int, long):int");
    }

    private boolean isValidSmsAddress(String address) {
        String encodedAddress = PhoneNumberUtils.extractNetworkPortion(address);
        return encodedAddress == null || encodedAddress.length() == address.length();
    }

    public void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendTextWithEncodingType encoding = " + encodingType);
        this.mEncodingType.set(Integer.valueOf(encodingType));
        sendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, false, 0L);
        this.mEncodingType.remove();
    }

    protected SmsMessageBase.SubmitPduBase onSendText(String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        int encodingType = this.mEncodingType.get().intValue();
        if (encodingType == 0) {
            return super.onSendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        }
        GsmAlphabet.TextEncodingDetails details = SMSDispatcherUtil.calculateLength(false, text, false);
        return SmsMessage.getSubmitPdu(scAddr, destAddr, text, deliveryIntent != null, (byte[]) null, encodingType, details.languageTable, details.languageShiftTable, validityPeriod);
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
        if (encodingType == 0) {
            int encodingType2 = super.onSendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, encodingForParts);
            Rlog.d(TAG, "onSendMultipartText encoding = " + encodingType2);
            return encodingType2;
        }
        int msgCount = parts.size();
        for (int i = 0; i < msgCount; i++) {
            GsmAlphabet.TextEncodingDetails details = SMSDispatcherUtil.calculateLength(false, parts.get(i), false);
            if (encodingType != details.codeUnitSize && (encodingType == 0 || encodingType == 1)) {
                Rlog.d(TAG, "[enc conflict between details[" + details.codeUnitSize + "] and encoding " + encodingType);
                details.codeUnitSize = encodingType;
            }
            encodingForParts[i] = details;
        }
        return encodingType;
    }

    public void handleIccFull() {
        this.mGsmInboundSmsHandler.mStorageMonitor.handleIccFull();
    }

    public void handleQueryCbActivation(AsyncResult ar) {
        Boolean result = null;
        if (ar.exception == null) {
            ArrayList<SmsBroadcastConfigInfo> list = (ArrayList) ar.result;
            if (list.size() == 0) {
                result = new Boolean(false);
            } else {
                SmsBroadcastConfigInfo cbConfig = list.get(0);
                Rlog.d(TAG, "cbConfig: " + cbConfig.toString());
                if (cbConfig.getFromCodeScheme() == -1 && cbConfig.getToCodeScheme() == -1 && cbConfig.getFromServiceId() == -1 && cbConfig.getToServiceId() == -1 && !cbConfig.isSelected()) {
                    result = new Boolean(false);
                } else {
                    result = new Boolean(true);
                }
            }
        }
        Rlog.d(TAG, "queryCbActivation: " + result);
        AsyncResult.forMessage((Message) ar.userObj, result, ar.exception);
        ((Message) ar.userObj).sendToTarget();
    }

    public void setSmsMemoryStatus(boolean status) {
        if (status != this.mStorageAvailable) {
            this.mStorageAvailable = status;
            this.mCi.reportSmsMemoryStatus(status, (Message) null);
        }
    }

    public boolean isSmsReady() {
        MtkSmsDispatchersController pSmsDispatcherctrl = (MtkSmsDispatchersController) this.mSmsDispatchersController;
        return pSmsDispatcherctrl.isSmsReady();
    }

    protected SMSDispatcher.SmsTracker getSmsTracker(String callingPackage, HashMap<String, Object> data, PendingIntent sentIntent, PendingIntent deliveryIntent, String format, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, SmsHeader smsHeader, boolean isExpectMore, String fullMessageText, boolean isText, boolean persistMessage, boolean isForVvm, long messageId, int messageRef) {
        SMSDispatcher.SmsTracker tracker = super.getSmsTracker(callingPackage, data, sentIntent, deliveryIntent, format, unsentPartCount, anyPartFailed, messageUri, smsHeader, isExpectMore, fullMessageText, isText, persistMessage, -1, -1, isForVvm, messageId, messageRef, false);
        return tracker;
    }

    public void sendRawPdu(SMSDispatcher.SmsTracker[] trackers) throws PackageManager.NameNotFoundException {
        int error = 0;
        PackageInfo appInfo = null;
        if (this.mSmsSendDisabled) {
            Rlog.e(TAG, "Device does not support sending sms.");
            error = 4;
        } else {
            int length = trackers.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                SMSDispatcher.SmsTracker tracker = trackers[i];
                if (tracker.getData().get("pdu") != null) {
                    i++;
                } else {
                    Rlog.e(TAG, "Empty PDU");
                    error = 3;
                    break;
                }
            }
            if (error == 0) {
                UserHandle userHandle = UserHandle.of(trackers[0].mUserId);
                PackageManager pm = this.mContext.createContextAsUser(userHandle, 0).getPackageManager();
                try {
                    appInfo = pm.getPackageInfo(trackers[0].getAppPackageName(), 64);
                } catch (PackageManager.NameNotFoundException e) {
                    Rlog.e(TAG, "Can't get calling app package info: refusing to send SMS " + SmsController.formatCrossStackMessageId(getMultiTrackermessageId(trackers)));
                    error = 1;
                }
            }
        }
        if (error != 0) {
            handleSmsTrackersFailure(trackers, error, -1);
            return;
        }
        if (checkDestination(trackers)) {
            if (!this.mSmsDispatchersController.getUsageMonitor().check(appInfo.packageName, trackers.length)) {
                sendMessage(obtainMessage(4, trackers));
                return;
            }
            boolean requestLocation = false;
            if (MtkSmsLocationHandler.getInstance() != null) {
                boolean supportT911Location = MtkSmsLocationHandler.getInstance().isSupportT911Location(this.mPhone.getPhoneId());
                if (supportT911Location) {
                    boolean isEmergencyNumber = this.mTelephonyManager.isEmergencyNumber(trackers[0].mDestAddress);
                    boolean isTestEmergencyNumber = MtkSmsLocationHandler.getInstance().isTestEmergencyNumber(this.mPhone.getPhoneId(), trackers[0].mDestAddress);
                    if (isEmergencyNumber || isTestEmergencyNumber) {
                        MtkSmsLocationHandler.getInstance().requestLocation(this.mPhone.getPhoneId());
                        requestLocation = true;
                    }
                }
            }
            if (requestLocation) {
                sendMessageDelayed(obtainMessage(30, trackers), 5000L);
            } else {
                for (SMSDispatcher.SmsTracker tracker2 : trackers) {
                    sendSms(tracker2);
                }
            }
        }
        if (this.mTelephonyManager.isEmergencyNumber(trackers[0].mDestAddress)) {
            new AsyncEmergencyContactNotifier(this.mContext).execute(new Void[0]);
        }
    }
}
