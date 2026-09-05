package com.mediatek.internal.telephony;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.UserHandle;
import android.telephony.Rlog;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsManager;
import com.android.internal.telephony.AsyncEmergencyContactNotifier;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.ImsSmsDispatcher;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SmsController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsRawData;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import com.mediatek.internal.telephony.util.MtkSMSDispatcherUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsSmsDispatcher extends ImsSmsDispatcher {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final int EVENT_SEND_SMS_AFTER_SET_LOC = 30;
    private static final String TAG = "MtkImsSmsDispacher";
    private static final int TIME_SEND_SMS_AFTER_SET_LOC = 5000;
    private ThreadLocal<Integer> mEncodingType;
    private BroadcastReceiver mImsSmsIntentReceiver;

    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String scAddr, String destAddr, int destinationPort, int originalPort, byte[] data, boolean statusReportRequested) {
        return MtkSMSDispatcherUtil.getSubmitPdu(isCdmaMo(), scAddr, destAddr, destinationPort, originalPort, data, statusReportRequested);
    }

    protected SmsMessageBase.SubmitPduBase getSubmitPdu(String scAddr, String destAddr, byte[] data, byte[] smsHeader, boolean statusReportRequested) {
        return MtkSMSDispatcherUtil.getSubmitPdu(isCdmaMo(), scAddr, destAddr, data, smsHeader, statusReportRequested);
    }

    protected SmsMessageBase.SubmitPduBase getSubmitPdu(boolean isCdma, String scAddress, String destinationAddress, String message, boolean statusReportRequested, byte[] header, int encoding, int languageTable, int languageShiftTable, int validityPeriod) {
        return MtkSMSDispatcherUtil.getSubmitPdu(isCdmaMo(), scAddress, destinationAddress, message, statusReportRequested, header, encoding, languageTable, languageShiftTable, validityPeriod);
    }

    public MtkImsSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        super(phone, smsDispatchersController, new ImsSmsDispatcher.FeatureConnectorFactory() { // from class: com.mediatek.internal.telephony.MtkImsSmsDispatcher$$ExternalSyntheticLambda0
            public final FeatureConnector create(Context context, int i, String str, FeatureConnector.Listener listener, Executor executor) {
                return ImsManager.getConnector(context, i, str, listener, executor);
            }
        });
        this.mEncodingType = new ThreadLocal<Integer>() { // from class: com.mediatek.internal.telephony.MtkImsSmsDispatcher.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Integer initialValue() {
                return 0;
            }
        };
        this.mImsSmsIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkImsSmsDispatcher.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Rlog.d(MtkImsSmsDispatcher.TAG, "mImsSmsIntentReceiver: action " + intent.getAction());
                if (intent.getAction().equals("com.android.ims.IMS_SERVICE_DOWN")) {
                    for (Map.Entry<Integer, SMSDispatcher.SmsTracker> entry : MtkImsSmsDispatcher.this.mTrackers.entrySet()) {
                        SMSDispatcher.SmsTracker tracker = entry.getValue();
                        tracker.onFailed(MtkImsSmsDispatcher.this.mContext, 4, -1);
                    }
                    MtkImsSmsDispatcher.this.mTrackers.clear();
                }
            }
        };
        Rlog.d(TAG, "Created!");
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.ims.IMS_SERVICE_DOWN");
        this.mContext.registerReceiver(this.mImsSmsIntentReceiver, filter);
    }

    protected void sendData(String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (!isCdmaMo()) {
            sendDataGsm(callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
        } else {
            super.sendData(callingPackage, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent, false);
        }
    }

    public void sendMultipartData(String callingPackage, String destAddr, String scAddr, int destPort, ArrayList<SmsRawData> data, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        if (!isCdmaMo()) {
            sendMultipartDataGsm(callingPackage, destAddr, scAddr, destPort, data, sentIntents, deliveryIntents);
        } else {
            Rlog.d(TAG, "Don't support sendMultipartData for CDMA");
        }
    }

    public void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (!isCdmaMo()) {
            sendTextWithEncodingTypeGsm(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        } else {
            sendText(destAddr, scAddr, text, sentIntent, deliveryIntent, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, false, 0L);
        }
    }

    public void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (!isCdmaMo()) {
            sendMultipartTextWithEncodingTypeGsm(destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod);
        } else {
            sendMultipartText(destAddr, scAddr, parts, sentIntents, deliveryIntents, messageUri, callingPkg, persistMessage, priority, expectMore, validityPeriod, 0L);
        }
    }

    protected SMSDispatcher.SmsTracker getNewSubmitPduTracker(String callingPackage, String destinationAddress, String scAddress, String message, SmsHeader smsHeader, int encoding, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean lastPart, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, String fullMessageText, int priority, boolean expectMore, int validityPeriod, long messageId, int messageRef) {
        if (ENG) {
            Rlog.d(TAG, "getNewSubmitPduTracker w/ validity");
        }
        if (isCdmaMo()) {
            return super.getNewSubmitPduTracker(callingPackage, destinationAddress, scAddress, message, smsHeader, encoding, sentIntent, deliveryIntent, lastPart, unsentPartCount, anyPartFailed, messageUri, fullMessageText, priority, expectMore, validityPeriod, messageId, messageRef);
        }
        return getNewSubmitPduTrackerGsm(callingPackage, destinationAddress, scAddress, message, smsHeader, encoding, sentIntent, deliveryIntent, lastPart, unsentPartCount, anyPartFailed, messageUri, fullMessageText, priority, expectMore, validityPeriod, messageRef);
    }

    protected void sendDataGsm(String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        Rlog.d(TAG, "sendData: enter");
        SmsMessageBase.SubmitPduBase pdu = getSubmitPdu(scAddr, destAddr, destPort, originalPort, data, deliveryIntent != null);
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
        Rlog.e(TAG, "sendData(): getSubmitPdu() returned null");
    }

    public void sendMultipartDataGsm(String callingPackage, String destAddr, String scAddr, int destPort, ArrayList<SmsRawData> data, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        PendingIntent sentIntent;
        PendingIntent deliveryIntent;
        ArrayList<SmsRawData> arrayList = data;
        ArrayList<PendingIntent> arrayList2 = sentIntents;
        ArrayList<PendingIntent> arrayList3 = deliveryIntents;
        String str = TAG;
        if (arrayList == null) {
            Rlog.e(TAG, "Cannot send multipart data when data is null!");
            return;
        }
        int refNumber = getNextConcatenatedRef() & 255;
        int msgCount = data.size();
        SMSDispatcher.SmsTracker[] trackers = new SMSDispatcher.SmsTracker[msgCount];
        int i = 0;
        while (true) {
            if (i < msgCount) {
                byte[] smsHeader = MtkSmsHeader.getSubmitPduHeader(destPort, refNumber, i + 1, msgCount);
                if (arrayList2 != null && sentIntents.size() > i) {
                    PendingIntent sentIntent2 = arrayList2.get(i);
                    sentIntent = sentIntent2;
                } else {
                    sentIntent = null;
                }
                if (arrayList3 != null && deliveryIntents.size() > i) {
                    PendingIntent deliveryIntent2 = arrayList3.get(i);
                    deliveryIntent = deliveryIntent2;
                } else {
                    deliveryIntent = null;
                }
                SmsMessageBase.SubmitPduBase pdus = getSubmitPdu(scAddr, destAddr, arrayList.get(i).getBytes(), smsHeader, deliveryIntent != null);
                if (pdus != null) {
                    HashMap map = getSmsTrackerMap(destAddr, scAddr, destPort, arrayList.get(i).getBytes(), pdus);
                    int messageRef = nextMessageRef();
                    int i2 = i;
                    SMSDispatcher.SmsTracker[] trackers2 = trackers;
                    trackers2[i2] = getSmsTracker(callingPackage, map, sentIntent, deliveryIntent, getFormat(), null, false, null, false, true, false, 0L, messageRef);
                    i = i2 + 1;
                    arrayList = data;
                    arrayList2 = sentIntents;
                    arrayList3 = deliveryIntents;
                    trackers = trackers2;
                    msgCount = msgCount;
                    refNumber = refNumber;
                    str = str;
                } else {
                    Rlog.e(str, "sendMultipartDataGsm(): getSubmitPdu() returned null");
                    return;
                }
            } else {
                SMSDispatcher.SmsTracker[] trackers3 = trackers;
                String str2 = str;
                if (trackers3.length == 0 || trackers3[0] == null) {
                    Rlog.e(str2, "Cannot send multipart data. trackers length = " + trackers3.length);
                    return;
                }
                for (SMSDispatcher.SmsTracker tracker : trackers3) {
                    if (tracker != null) {
                        if (!sendSmsByCarrierApp(true, tracker)) {
                            sendSubmitPdu(tracker);
                        }
                    } else {
                        Rlog.e(str2, "Null tracker.");
                    }
                }
                return;
            }
        }
    }

    public void sendTextWithEncodingTypeGsm(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        Rlog.d(TAG, "sendTextWithEncodingTypeGsm encoding = " + encodingType);
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
        return getSubmitPdu(false, scAddr, destAddr, text, deliveryIntent != null, null, encodingType, details.languageTable, details.languageShiftTable, validityPeriod);
    }

    public void sendMultipartTextWithEncodingTypeGsm(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
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

    protected SMSDispatcher.SmsTracker getSmsTracker(String callingPackage, HashMap<String, Object> data, PendingIntent sentIntent, PendingIntent deliveryIntent, String format, Uri messageUri, boolean expectMore, String fullMessageText, boolean isText, boolean persistMessage, boolean isForVvm, long messageId, int messageRef) {
        return getSmsTracker(callingPackage, data, sentIntent, deliveryIntent, format, null, null, messageUri, null, expectMore, fullMessageText, isText, persistMessage, -1, -1, isForVvm, messageId, messageRef, false);
    }

    private SMSDispatcher.SmsTracker getNewSubmitPduTrackerGsm(String callingPackage, String destinationAddress, String scAddress, String message, SmsHeader smsHeader, int encoding, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean lastPart, AtomicInteger unsentPartCount, AtomicBoolean anyPartFailed, Uri messageUri, String fullMessageText, int priority, boolean expectMore, int validityPeriod, int messageRef) {
        boolean z = true;
        SmsMessageBase.SubmitPduBase pdu = getSubmitPdu(false, scAddress, destinationAddress, message, deliveryIntent != null, SmsHeader.toByteArray(smsHeader), encoding, smsHeader.languageTable, smsHeader.languageShiftTable, validityPeriod);
        if (pdu != null) {
            HashMap map = getSmsTrackerMap(destinationAddress, scAddress, message, pdu);
            String format = getFormat();
            if (lastPart && !expectMore) {
                z = false;
            }
            return getSmsTracker(callingPackage, map, sentIntent, deliveryIntent, format, unsentPartCount, anyPartFailed, messageUri, smsHeader, z, fullMessageText, true, true, priority, validityPeriod, false, 0L, messageRef, false);
        }
        Rlog.e(TAG, "getNewSubmitPduTrackerGsm: getSubmitPdu() returned null");
        return null;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 30:
                Rlog.e(TAG, "EVENT_SEND_SMS_AFTER_SET_LOC");
                SMSDispatcher.SmsTracker[] trackers = (SMSDispatcher.SmsTracker[]) msg.obj;
                for (SMSDispatcher.SmsTracker tracker : trackers) {
                    Rlog.e(TAG, "send sms after seconds");
                    sendSms(tracker);
                }
                break;
            default:
                super.handleMessage(msg);
                break;
        }
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
