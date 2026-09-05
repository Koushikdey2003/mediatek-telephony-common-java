package com.mediatek.internal.telephony;

import android.app.ActivityThread;
import android.app.PendingIntent;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.HexDump;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsNumberUtils;
import com.android.internal.telephony.SmsRawData;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import com.mediatek.internal.telephony.cdma.MtkSmsMessage;
import com.mediatek.internal.telephony.uicc.MtkSIMFileHandler;
import java.util.ArrayList;
import java.util.List;
import mediatek.telephony.MtkSimSmsInsertStatus;
import mediatek.telephony.MtkSmsParameters;

/* JADX INFO: loaded from: classes.dex */
public class MtkIccSmsInterfaceManager extends IccSmsInterfaceManager {
    private static final int CB_ACTIVATION_OFF = 0;
    private static final int CB_ACTIVATION_ON = 1;
    private static final int CB_ACTIVATION_UNKNOWN = -1;
    static final boolean DBG = true;
    private static final int EVENT_GET_BROADCAST_ACTIVATION_DONE = 106;
    private static final int EVENT_GET_BROADCAST_CONFIG_CHANNEL_DONE = 108;
    private static final int EVENT_GET_BROADCAST_CONFIG_LANGUAGE_DONE = 110;
    private static final int EVENT_GET_SMSC_ADDRESS_BUNDLE_DONE = 113;
    private static final int EVENT_GET_SMSC_ADDRESS_DONE = 112;
    private static final int EVENT_GET_SMS_PARAMS = 103;
    private static final int EVENT_GET_SMS_SIM_MEM_STATUS_DONE = 101;
    private static final int EVENT_INSERT_TEXT_MESSAGE_TO_ICC_DONE = 102;
    private static final int EVENT_LOAD_ONE_RECORD_DONE = 105;
    private static final int EVENT_MTK_LOAD_DONE = 115;
    private static final int EVENT_MTK_UPDATE_DONE = 116;
    private static final int EVENT_REMOVE_BROADCAST_MSG_DONE = 107;
    private static final int EVENT_SET_BROADCAST_CONFIG_LANGUAGE_DONE = 109;
    private static final int EVENT_SET_ETWS_CONFIG_DONE = 111;
    private static final int EVENT_SET_SMSC_ADDRESS_DONE = 114;
    private static final int EVENT_SET_SMS_PARAMS = 104;
    private static final int EVENT_SIM_SMS_DELETE_DONE = 100;
    private static final String INDEXT_SPLITOR = ",";
    static final String LOG_TAG = "MtkIccSmsInterfaceManager";
    private static int sConcatenatedRef = 456;
    private int mCurrentCellBroadcastActivation;
    protected Handler mMtkHandler;

    protected MtkIccSmsInterfaceManager(Phone phone) {
        super(phone);
        this.mMtkHandler = new Handler() { // from class: com.mediatek.internal.telephony.MtkIccSmsInterfaceManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                IccSmsInterfaceManager.Request request = (IccSmsInterfaceManager.Request) ar.userObj;
                int i = msg.what;
                boolean z = MtkIccSmsInterfaceManager.DBG;
                switch (i) {
                    case 101:
                        MtkIccSmsStorageStatus simMemStatus = null;
                        if (ar.exception == null) {
                            simMemStatus = new MtkIccSmsStorageStatus();
                            MtkIccSmsStorageStatus tmpStatus = (MtkIccSmsStorageStatus) ar.result;
                            simMemStatus.mUsed = tmpStatus.mUsed;
                            simMemStatus.mTotal = tmpStatus.mTotal;
                        } else {
                            MtkIccSmsInterfaceManager.this.log("Cannot Get Sms SIM Memory Status from SIM");
                        }
                        notifyPending(request, simMemStatus);
                        break;
                    case 102:
                        MtkSimSmsInsertStatus smsInsertRet = new MtkSimSmsInsertStatus(1, "");
                        if (ar.exception == null) {
                            try {
                                int index = ((int[]) ar.result)[0];
                                smsInsertRet.indexInIcc += index + MtkIccSmsInterfaceManager.INDEXT_SPLITOR;
                                smsInsertRet.insertStatus = 0;
                                MtkIccSmsInterfaceManager.this.log("insertText save one pdu in index " + index);
                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            MtkIccSmsInterfaceManager.this.log("insertText fail to insert sms into ICC");
                            smsInsertRet.indexInIcc += "-1,";
                        }
                        notifyPending(request, smsInsertRet);
                        break;
                    case 103:
                        Object smsParams = null;
                        if (ar.exception == null) {
                            try {
                                smsParams = (MtkSmsParameters) ar.result;
                            } catch (ClassCastException e2) {
                                MtkIccSmsInterfaceManager.this.log("[EFsmsp fail to get sms params ClassCastException");
                                e2.printStackTrace();
                            } catch (Exception ex2) {
                                MtkIccSmsInterfaceManager.this.log("[EFsmsp fail to get sms params Exception");
                                ex2.printStackTrace();
                            }
                        } else {
                            MtkIccSmsInterfaceManager.this.log("[EFsmsp fail to get sms params");
                        }
                        notifyPending(request, smsParams);
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_SET_SMS_PARAMS /* 104 */:
                    case MtkIccSmsInterfaceManager.EVENT_REMOVE_BROADCAST_MSG_DONE /* 107 */:
                    case 109:
                    case 111:
                    case MtkIccSmsInterfaceManager.EVENT_SET_SMSC_ADDRESS_DONE /* 114 */:
                        if (ar.exception != null) {
                            z = false;
                        }
                        notifyPending(request, Boolean.valueOf(z));
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_LOAD_ONE_RECORD_DONE /* 105 */:
                        Object smsRawData = null;
                        if (ar.exception == null) {
                            try {
                                byte[] rawData = (byte[]) ar.result;
                                if (rawData[0] == 0) {
                                    MtkIccSmsInterfaceManager.this.log("sms raw data status is FREE");
                                } else {
                                    smsRawData = new SmsRawData(rawData);
                                }
                            } catch (ClassCastException e3) {
                                MtkIccSmsInterfaceManager.this.log("fail to get sms raw data ClassCastException");
                                e3.printStackTrace();
                            }
                        } else {
                            MtkIccSmsInterfaceManager.this.log("fail to get sms raw data rild");
                        }
                        notifyPending(request, smsRawData);
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_GET_BROADCAST_ACTIVATION_DONE /* 106 */:
                        boolean result = false;
                        if (ar.exception == null) {
                            int[] activation = (int[]) ar.result;
                            if (activation[0] != 1) {
                                z = false;
                            }
                            result = z;
                        }
                        notifyPending(request, Boolean.valueOf(result));
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_GET_BROADCAST_CONFIG_CHANNEL_DONE /* 108 */:
                        String smsCbChannelConfig = "";
                        if (ar.exception == null) {
                            ArrayList<SmsBroadcastConfigInfo> mList = (ArrayList) ar.result;
                            for (int i2 = 0; i2 < mList.size(); i2++) {
                                SmsBroadcastConfigInfo cbConfig = mList.get(i2);
                                if (cbConfig.getFromServiceId() == cbConfig.getToServiceId()) {
                                    smsCbChannelConfig = smsCbChannelConfig + cbConfig.getFromServiceId();
                                } else {
                                    smsCbChannelConfig = smsCbChannelConfig + cbConfig.getFromServiceId() + "-" + cbConfig.getToServiceId();
                                }
                                if (i2 + 1 != mList.size()) {
                                    smsCbChannelConfig = smsCbChannelConfig + MtkIccSmsInterfaceManager.INDEXT_SPLITOR;
                                }
                            }
                            MtkIccSmsInterfaceManager.this.log("Channel configuration " + smsCbChannelConfig);
                        } else {
                            MtkIccSmsInterfaceManager.this.log("Cannot Get CB configs");
                        }
                        notifyPending(request, smsCbChannelConfig);
                        break;
                    case 110:
                        String smsCbLanguageConfig = "";
                        if (ar.exception == null) {
                            String smsCbLanguageConfig2 = (String) ar.result;
                            smsCbLanguageConfig = smsCbLanguageConfig2 != null ? smsCbLanguageConfig2 : "";
                            MtkIccSmsInterfaceManager.this.log("Language configuration " + smsCbLanguageConfig);
                        } else {
                            MtkIccSmsInterfaceManager.this.log("Cannot Get CB configs");
                        }
                        notifyPending(request, smsCbLanguageConfig);
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_GET_SMSC_ADDRESS_DONE /* 112 */:
                        Object smscAddress = "";
                        if (ar.exception == null) {
                            smscAddress = (String) ar.result;
                        } else {
                            MtkIccSmsInterfaceManager.this.log("Cannot Get SMSC address");
                        }
                        notifyPending(request, smscAddress);
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_GET_SMSC_ADDRESS_BUNDLE_DONE /* 113 */:
                        Bundle smscAddressBundle = new Bundle();
                        if (ar.exception == null) {
                            smscAddressBundle.putByte("errorCode", (byte) 0);
                            smscAddressBundle.putCharSequence("scAddress", (String) ar.result);
                        } else {
                            MtkIccSmsInterfaceManager.this.log("Cannot Get SMSC address");
                            byte error = 1;
                            if (ar.exception instanceof CommandException) {
                                CommandException ce = ar.exception;
                                if (ce.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                                    error = 2;
                                }
                            }
                            MtkIccSmsInterfaceManager.this.log("Fail to get sc address, error = " + ((int) error));
                            smscAddressBundle.putByte("errorCode", error);
                            smscAddressBundle.putCharSequence("scAddress", "");
                        }
                        notifyPending(request, smscAddressBundle);
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_MTK_LOAD_DONE /* 115 */:
                        Object smsList = null;
                        if (ar.exception == null) {
                            smsList = MtkIccSmsInterfaceManager.this.buildValidRawData((ArrayList) ar.result);
                            MtkIccSmsInterfaceManager.this.markMessagesAsRead((ArrayList) ar.result);
                        } else if (Rlog.isLoggable("SMS", 3)) {
                            MtkIccSmsInterfaceManager.this.log("Cannot load Sms records");
                        }
                        notifyPending(request, smsList);
                        break;
                    case MtkIccSmsInterfaceManager.EVENT_MTK_UPDATE_DONE /* 116 */:
                        MtkSimSmsInsertStatus smsRet = new MtkSimSmsInsertStatus(1, "");
                        if (ar.exception == null) {
                            try {
                                int index2 = ((int[]) ar.result)[0];
                                smsRet.indexInIcc += index2 + MtkIccSmsInterfaceManager.INDEXT_SPLITOR;
                                smsRet.insertStatus = 0;
                                MtkIccSmsInterfaceManager.this.log("[insertRaw save one pdu in index " + index2);
                            } catch (ClassCastException e4) {
                                e4.printStackTrace();
                            } catch (Exception ex3) {
                                ex3.printStackTrace();
                            }
                        } else {
                            MtkIccSmsInterfaceManager.this.log("[insertRaw fail to insert raw into ICC");
                            smsRet.indexInIcc += "-1,";
                        }
                        notifyPending(request, smsRet);
                        if (ar.exception != null) {
                            CommandException e5 = ar.exception;
                            MtkIccSmsInterfaceManager.this.log("Cannot update SMS " + e5.getCommandError());
                            if (e5.getCommandError() == CommandException.Error.SIM_FULL) {
                                ((MtkSmsDispatchersController) MtkIccSmsInterfaceManager.this.mDispatchersController).handleIccFull();
                            }
                        }
                        break;
                }
            }

            private void notifyPending(IccSmsInterfaceManager.Request request, Object result) {
                if (request != null) {
                    synchronized (request) {
                        request.mResult = result;
                        request.mStatus.set(MtkIccSmsInterfaceManager.DBG);
                        request.notifyAll();
                    }
                }
            }
        };
        this.mCurrentCellBroadcastActivation = -1;
    }

    protected void sendTextInternal(String callingPackage, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, boolean isForVvm, long messageId, boolean skipShortCodeCheck) {
        log("sendTextMessage");
        if (!isValidParameters(destAddr, text, sentIntent)) {
            return;
        }
        ActivityThread.currentApplication().getApplicationContext();
        exitECBMIfNeeded(callingPackage);
        super.sendTextInternal(callingPackage, destAddr, scAddr, text, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp, priority, expectMore, validityPeriod, isForVvm, messageId, skipShortCodeCheck);
    }

    protected void sendDataInternal(String callingPackage, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean isForVvm) {
        log("sendDataMessage");
        if (!isValidParameters(destAddr, "send_data", sentIntent)) {
            return;
        }
        ActivityThread.currentApplication().getApplicationContext();
        super.sendDataInternal(callingPackage, destAddr, scAddr, destPort, data, sentIntent, deliveryIntent, isForVvm);
    }

    public List<SmsRawData> getAllMessagesFromIccEf(String callingPackage) {
        log("getAllMessagesFromEF " + callingPackage);
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECEIVE_SMS", "Reading messages from Icc");
        if (this.mAppOps.noteOp(21, Binder.getCallingUid(), callingPackage) != 0) {
            return new ArrayList();
        }
        IccSmsInterfaceManager.Request getRequest = new IccSmsInterfaceManager.Request();
        synchronized (getRequest) {
            IccFileHandler fh = this.mPhone.getIccFileHandler();
            if (fh == null) {
                Rlog.e(LOG_TAG, "Cannot load Sms records. No icc card?");
                return null;
            }
            Message response = this.mMtkHandler.obtainMessage(EVENT_MTK_LOAD_DONE, getRequest);
            fh.loadEFLinearFixedAll(28476, response);
            waitForResult(getRequest);
            return (List) getRequest.mResult;
        }
    }

    protected ArrayList<SmsRawData> buildValidRawData(ArrayList<byte[]> messages) {
        int count = messages.size();
        ArrayList<SmsRawData> ret = new ArrayList<>(count);
        int validSmsCount = 0;
        for (int i = 0; i < count; i++) {
            byte[] ba = messages.get(i);
            if (ba[0] == 0) {
                ret.add(null);
            } else {
                validSmsCount++;
                ret.add(new SmsRawData(messages.get(i)));
            }
        }
        log("validSmsCount = " + validSmsCount);
        return ret;
    }

    protected byte[] makeSmsRecordData(int status, byte[] pdu) {
        byte[] data;
        if (1 == this.mPhone.getPhoneType()) {
            data = new byte[176];
        } else {
            data = new byte[255];
        }
        data[0] = (byte) (status & 7);
        log("ISIM-makeSmsRecordData: pdu size = " + pdu.length);
        if (pdu.length == 176) {
            log("ISIM-makeSmsRecordData: sim pdu");
            try {
                System.arraycopy(pdu, 1, data, 1, pdu.length - 1);
            } catch (ArrayIndexOutOfBoundsException e) {
                log("ISIM-makeSmsRecordData: out of bounds, sim pdu");
            }
        } else {
            log("ISIM-makeSmsRecordData: normal pdu");
            try {
                System.arraycopy(pdu, 0, data, 1, pdu.length);
            } catch (ArrayIndexOutOfBoundsException e2) {
                log("ISIM-makeSmsRecordData: out of bounds, normal pdu");
            }
        }
        for (int j = pdu.length + 1; j < data.length; j++) {
            data[j] = -1;
        }
        return data;
    }

    protected void log(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Rlog.e(LOG_TAG, msg);
    }

    public void sendDataWithOriginalPort(String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean checkPermission) {
        Rlog.d(LOG_TAG, "Enter IccSmsInterfaceManager.sendDataWithOriginalPort");
        if (checkPermission) {
            this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
            if (Rlog.isLoggable("SMS", 2)) {
                log("sendData: data='" + HexDump.toHexString(data) + "' sentIntent=" + sentIntent + " deliveryIntent=" + deliveryIntent);
            }
            if (this.mAppOps.noteOp(20, Binder.getCallingUid(), callingPackage) != 0) {
                return;
            }
        }
        ((MtkSmsDispatchersController) this.mDispatchersController).sendData(callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent);
    }

    public void sendMultipartText(String callingPackage, String callingAttributionTag, String destAddr, String scAddr, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp, long messageId) {
        this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
        log("sendMultipartTextMessage");
        if (!isValidParameters(destAddr, parts, sentIntents)) {
            return;
        }
        ActivityThread.currentApplication().getApplicationContext();
        exitECBMIfNeeded(callingPackage);
        super.sendMultipartText(callingPackage, callingAttributionTag, destAddr, scAddr, parts, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp, messageId);
    }

    public void sendMultipartData(String callingPackage, String destAddr, String scAddr, int destPort, List<SmsRawData> data, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) {
        this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
        if (Rlog.isLoggable("SMS", 2)) {
            for (SmsRawData rData : data) {
                log("sendMultipartData:data='" + HexDump.toHexString(rData.getBytes()));
            }
        }
        if (this.mAppOps.noteOp(20, Binder.getCallingUid(), callingPackage) != 0) {
            return;
        }
        ((MtkSmsDispatchersController) this.mDispatchersController).sendMultipartData(callingPackage, destAddr, scAddr, destPort, (ArrayList) data, (ArrayList) sentIntents, (ArrayList) deliveryIntents);
    }

    public void setSmsMemoryStatus(boolean status) {
        log("setSmsMemoryStatus: set storage status -> " + status);
        this.mContext.enforceCallingPermission("android.permission.MODIFY_PHONE_STATE", "Set Sms Memory Status");
        ((MtkSmsDispatchersController) this.mDispatchersController).setSmsMemoryStatus(status);
    }

    public boolean isSmsReady() {
        this.mContext.enforceCallingPermission("android.permission.READ_PHONE_STATE", "Is Sms Ready");
        boolean isReady = ((MtkSmsDispatchersController) this.mDispatchersController).isSmsReady();
        log("isSmsReady: " + isReady);
        return isReady;
    }

    public void sendTextWithEncodingType(String callingPackage, String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) {
        sendTextWithOptions(callingPackage, destAddr, scAddr, text, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp, -1, false, -1, encodingType);
    }

    public void sendMultipartTextWithEncodingType(String callingPackage, String destAddr, String scAddr, List<String> parts, int encodingType, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) {
        sendMultipartTextWithOptions(callingPackage, destAddr, scAddr, parts, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp, -1, false, -1, encodingType);
    }

    public void sendTextWithExtraParams(String callingPackage, String destAddr, String scAddr, String text, Bundle extraParams, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) {
        this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
        if (this.mAppOps.noteOp(20, Binder.getCallingUid(), callingPackage) != 0) {
            return;
        }
        int validityPeriod = extraParams.getInt("validity_period", -1);
        sendTextInternal(callingPackage, destAddr, scAddr, text, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp, -1, false, validityPeriod, false, 0L, false);
    }

    public void sendMultipartTextWithExtraParams(String callingPackage, String destAddr, String scAddr, List<String> parts, Bundle extraParams, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) {
        String singlePart;
        PendingIntent singleSentIntent;
        this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
        if (this.mAppOps.noteOp(20, Binder.getCallingUid(), callingPackage) == 0) {
            String destAddr2 = filterDestAddress(destAddr);
            int validityPeriod = extraParams.getInt("validity_period", -1);
            if (parts.size() > 1 && parts.size() < 10 && !SmsMessage.hasEmsSupport()) {
                for (int i = 0; i < parts.size(); i++) {
                    String singlePart2 = parts.get(i);
                    if (SmsMessage.shouldAppendPageNumberAsPrefix()) {
                        singlePart = String.valueOf(i + 1) + '/' + parts.size() + ' ' + singlePart2;
                    } else {
                        singlePart = singlePart2.concat(' ' + String.valueOf(i + 1) + '/' + parts.size());
                    }
                    if (sentIntents != null && sentIntents.size() > i) {
                        PendingIntent singleSentIntent2 = sentIntents.get(i);
                        singleSentIntent = singleSentIntent2;
                    } else {
                        singleSentIntent = null;
                    }
                    PendingIntent singleDeliveryIntent = null;
                    if (deliveryIntents != null && deliveryIntents.size() > i) {
                        singleDeliveryIntent = deliveryIntents.get(i);
                    }
                    sendTextWithOptions(callingPackage, (String) null, destAddr2, scAddr, singlePart, singleSentIntent, singleDeliveryIntent, persistMessageForNonDefaultSmsApp, -1, false, validityPeriod);
                }
                return;
            }
            sendMultipartTextWithOptions(callingPackage, null, destAddr2, scAddr, parts, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp, -1, false, validityPeriod, 0L);
        }
    }

    public SmsRawData getMessageFromIccEf(String callingPackage, int index) {
        log("getMessageFromIccEf");
        this.mPhone.getContext().enforceCallingPermission("android.permission.RECEIVE_SMS", "Reading messages from SIM");
        if (this.mAppOps.noteOp(21, Binder.getCallingUid(), callingPackage) != 0) {
            return null;
        }
        IccSmsInterfaceManager.Request getRequest = new IccSmsInterfaceManager.Request();
        synchronized (getRequest) {
            IccFileHandler fh = this.mPhone.getIccFileHandler();
            if (fh != null) {
                Message response = this.mMtkHandler.obtainMessage(EVENT_LOAD_ONE_RECORD_DONE, getRequest);
                fh.loadEFLinearFixed(28476, index, response);
                waitForResult(getRequest);
            }
        }
        return (SmsRawData) getRequest.mResult;
    }

    public List<SmsRawData> getAllMessagesFromIccEfByMode(String callingPackage, int mode) {
        log("getAllMessagesFromIccEfByMode, mode=" + mode);
        if (mode < 1 || mode > 2) {
            log("getAllMessagesFromIccEfByMode wrong mode=" + mode);
            return null;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECEIVE_SMS", "Reading messages from Icc");
        if (this.mAppOps.noteOp(21, Binder.getCallingUid(), callingPackage) != 0) {
            return new ArrayList();
        }
        IccSmsInterfaceManager.Request getRequest = new IccSmsInterfaceManager.Request();
        synchronized (getRequest) {
            MtkSIMFileHandler iccFileHandler = this.mPhone.getIccFileHandler();
            if (iccFileHandler == null) {
                Rlog.e(LOG_TAG, "Cannot load Sms records. No icc card?");
                return null;
            }
            Message response = this.mMtkHandler.obtainMessage(EVENT_MTK_LOAD_DONE, getRequest);
            if (1 == this.mPhone.getPhoneType()) {
                Rlog.e(LOG_TAG, "getAllMessagesFromIccEfByMode. In the case of GSM phone");
                MtkSIMFileHandler sfh = iccFileHandler;
                sfh.loadEFLinearFixedAll(28476, mode, response);
                waitForResult(getRequest);
            }
            return (List) getRequest.mResult;
        }
    }

    public MtkSmsParameters getSmsParameters(String callingPackage) {
        log("getSmsParameters");
        enforceReceiveAndSend("Get SMS parametner on SIM");
        if (this.mAppOps.noteOp(21, Binder.getCallingUid(), callingPackage) != 0) {
            return null;
        }
        IccSmsInterfaceManager.Request getParamRequest = new IccSmsInterfaceManager.Request();
        synchronized (getParamRequest) {
            Message response = this.mMtkHandler.obtainMessage(103, getParamRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.getSmsParameters(response);
            waitForResult(getParamRequest);
        }
        return (MtkSmsParameters) getParamRequest.mResult;
    }

    public boolean setSmsParameters(String callingPackage, MtkSmsParameters params) {
        log("setSmsParameters");
        enforceReceiveAndSend("Set SMS parametner on SIM");
        if (this.mAppOps.noteOp(22, Binder.getCallingUid(), callingPackage) != 0) {
            return false;
        }
        IccSmsInterfaceManager.Request setParamRequest = new IccSmsInterfaceManager.Request();
        synchronized (setParamRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_SET_SMS_PARAMS, setParamRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.setSmsParameters(params, response);
            waitForResult(setParamRequest);
        }
        return ((Boolean) setParamRequest.mResult).booleanValue();
    }

    public int copyTextMessageToIccCard(String callingPkg, String scAddress, String address, List<String> text, int status, long timestamp) {
        log("copyTextMessageToIccCard, message count: " + text.size() + " status: " + status);
        enforceReceiveAndSend("Copying message to USIM/SIM");
        if (this.mAppOps.noteOp(22, Binder.getCallingUid(), callingPkg) != 0) {
            return 1;
        }
        MtkIccSmsStorageStatus memStatus = getSmsSimMemoryStatus(callingPkg);
        if (memStatus == null) {
            log("Fail to get SIM memory status");
            return 1;
        }
        if (memStatus.getUnused() < text.size()) {
            log("SIM memory is not enough");
            return 7;
        }
        return ((MtkSmsDispatchersController) this.mDispatchersController).copyTextMessageToIccCard(scAddress, address, text, status, timestamp);
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x0182  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:112:? -> B:83:0x01fb). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public mediatek.telephony.MtkSimSmsInsertStatus insertTextMessageToIccCard(java.lang.String r34, java.lang.String r35, java.lang.String r36, java.util.List<java.lang.String> r37, int r38, long r39) {
        /*
            Method dump skipped, instruction units count: 646
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.MtkIccSmsInterfaceManager.insertTextMessageToIccCard(java.lang.String, java.lang.String, java.lang.String, java.util.List, int, long):mediatek.telephony.MtkSimSmsInsertStatus");
    }

    public MtkSimSmsInsertStatus insertRawMessageToIccCard(String callingPackage, int status, byte[] pdu, byte[] smsc) {
        MtkSimSmsInsertStatus smsInsertRet = new MtkSimSmsInsertStatus(0, "");
        log("insertRawMessageToIccCard");
        enforceReceiveAndSend("insertRaw insert message into SIM");
        if (this.mAppOps.noteOp(22, Binder.getCallingUid(), callingPackage) != 0) {
            smsInsertRet.insertStatus = 1;
            return smsInsertRet;
        }
        IccSmsInterfaceManager.Request insertRawRequest = new IccSmsInterfaceManager.Request();
        synchronized (insertRawRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_MTK_UPDATE_DONE, insertRawRequest);
            if (2 != this.mPhone.getPhoneType()) {
                this.mPhone.mCi.writeSmsToSim(status, IccUtils.bytesToHexString(smsc), IccUtils.bytesToHexString(pdu), response);
            } else {
                this.mPhone.mCi.writeSmsToRuim(status, pdu, response);
            }
            waitForResult(insertRawRequest);
        }
        MtkSimSmsInsertStatus smsInsertRet2 = (MtkSimSmsInsertStatus) insertRawRequest.mResult;
        if (smsInsertRet2.insertStatus == 0) {
            log("insertRaw message inserted");
        } else {
            log("insertRaw pdu insert fail");
        }
        return smsInsertRet2;
    }

    public MtkIccSmsStorageStatus getSmsSimMemoryStatus(String callingPackage) {
        log("getSmsSimMemoryStatus");
        enforceReceiveAndSend("Get SMS SIM Card Memory Status from RUIM");
        if (this.mAppOps.noteOp(21, Binder.getCallingUid(), callingPackage) != 0) {
            return null;
        }
        IccSmsInterfaceManager.Request getRequest = new IccSmsInterfaceManager.Request();
        synchronized (getRequest) {
            Message response = this.mMtkHandler.obtainMessage(101, getRequest);
            MtkRIL ci = this.mPhone.mCi;
            if (this.mPhone.getPhoneType() == 2) {
                ci.getSmsRuimMemoryStatus(response);
            } else {
                ci.getSmsSimMemoryStatus(response);
            }
            waitForResult(getRequest);
        }
        return (MtkIccSmsStorageStatus) getRequest.mResult;
    }

    private static int getNextConcatRef() {
        int i = sConcatenatedRef;
        sConcatenatedRef = i + 1;
        return i;
    }

    private static boolean checkPhoneNumberCharacter(char c) {
        if ((c >= '0' && c <= '9') || c == '*' || c == '+' || c == '#' || c == 'N' || c == ' ' || c == '-') {
            return DBG;
        }
        return false;
    }

    private static boolean checkPhoneNumberInternal(String number) {
        if (number == null) {
            return DBG;
        }
        int n = number.length();
        for (int i = 0; i < n; i++) {
            if (!checkPhoneNumberCharacter(number.charAt(i))) {
                return false;
            }
        }
        return DBG;
    }

    protected MtkSimSmsInsertStatus writeTextMessageToRuim(String address, List<String> text, int status, long timestamp) {
        MtkSimSmsInsertStatus insertRet = new MtkSimSmsInsertStatus(0, "");
        for (int i = 0; i < text.size(); i++) {
            if (insertRet.insertStatus == 1) {
                log("[copyText Exception happened when copy message");
                return insertRet;
            }
            SmsMessage.SubmitPdu pdu = MtkSmsMessage.createEfPdu(address, text.get(i), timestamp);
            if (pdu != null) {
                IccSmsInterfaceManager.Request writeRequest = new IccSmsInterfaceManager.Request();
                synchronized (writeRequest) {
                    Message response = this.mMtkHandler.obtainMessage(102, writeRequest);
                    this.mPhone.mCi.writeSmsToRuim(status, pdu.encodedMessage, response);
                    waitForResult(writeRequest);
                }
                insertRet = (MtkSimSmsInsertStatus) writeRequest.mResult;
            } else {
                log("writeTextMessageToRuim: pdu == null");
                insertRet.insertStatus = 1;
                return insertRet;
            }
        }
        log("writeTextMessageToRuim: done");
        insertRet.insertStatus = 0;
        return insertRet;
    }

    private String filterDestAddress(String destAddr) {
        String result = SmsNumberUtils.filterDestAddr(this.mContext, this.mPhone.getSubId(), destAddr);
        return result != null ? result : destAddr;
    }

    private static boolean isValidParameters(String destinationAddress, String text, PendingIntent sentIntent) {
        List<PendingIntent> sentIntents = new ArrayList<>();
        List<String> parts = new ArrayList<>();
        sentIntents.add(sentIntent);
        parts.add(text);
        return isValidParameters(destinationAddress, parts, sentIntents);
    }

    private static boolean isValidParameters(String destinationAddress, List<String> parts, List<PendingIntent> sentIntents) {
        if (parts == null || parts.size() == 0) {
            return DBG;
        }
        if (!isValidSmsDestinationAddress(destinationAddress)) {
            for (int i = 0; i < sentIntents.size(); i++) {
                PendingIntent sentIntent = sentIntents.get(i);
                if (sentIntent != null) {
                    try {
                        sentIntent.send(1);
                    } catch (PendingIntent.CanceledException e) {
                    }
                }
            }
            Rlog.d("IccSmsInterfaceManagerEx", "Invalid destinationAddress: " + destinationAddress);
            return false;
        }
        if (TextUtils.isEmpty(destinationAddress)) {
            Rlog.e("IccSmsInterfaceManagerEx", "Invalid destinationAddress");
            return false;
        }
        if (parts != null && parts.size() >= 1) {
            return DBG;
        }
        Rlog.e("IccSmsInterfaceManagerEx", "Invalid message body");
        return false;
    }

    private static boolean isValidSmsDestinationAddress(String da) {
        String encodeAddress = PhoneNumberUtils.extractNetworkPortion(da);
        return encodeAddress == null ? DBG : true ^ encodeAddress.isEmpty();
    }

    public boolean activateCellBroadcastSms(boolean activate) {
        log("activateCellBroadcastSms activate : " + activate);
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Activate CellBroadcast");
        return setCellBroadcastActivation(activate);
    }

    public boolean queryCellBroadcastSmsActivation() {
        log("queryCellBroadcastSmsActivation");
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Query CellBroadcast Activation");
        IccSmsInterfaceManager.Request queryRequest = new IccSmsInterfaceManager.Request();
        synchronized (queryRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_GET_BROADCAST_ACTIVATION_DONE, queryRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.getGsmBroadcastActivation(response);
            waitForResult(queryRequest);
        }
        return ((Boolean) queryRequest.mResult).booleanValue();
    }

    public String getCellBroadcastRanges() {
        log("getCellBroadcastChannels");
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Get CellBroadcast Ranges");
        IccSmsInterfaceManager.Request getCBRRequest = new IccSmsInterfaceManager.Request();
        synchronized (getCBRRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_GET_BROADCAST_CONFIG_CHANNEL_DONE, getCBRRequest);
            this.mPhone.mCi.getGsmBroadcastConfig(response);
            waitForResult(getCBRRequest);
        }
        return (String) getCBRRequest.mResult;
    }

    public boolean setCellBroadcastLangs(String lang) {
        log("setCellBroadcastLangs");
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Set CellBroadcast Langs");
        IccSmsInterfaceManager.Request setCBLRequest = new IccSmsInterfaceManager.Request();
        synchronized (setCBLRequest) {
            Message response = this.mMtkHandler.obtainMessage(109, setCBLRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.setGsmBroadcastLangs(lang, response);
            waitForResult(setCBLRequest);
        }
        return ((Boolean) setCBLRequest.mResult).booleanValue();
    }

    public String getCellBroadcastLangs() {
        log("getCellBroadcastLangs");
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Get CellBroadcast Langs");
        IccSmsInterfaceManager.Request getCBLRequest = new IccSmsInterfaceManager.Request();
        synchronized (getCBLRequest) {
            Message response = this.mMtkHandler.obtainMessage(110, getCBLRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.getGsmBroadcastLangs(response);
            waitForResult(getCBLRequest);
        }
        return (String) getCBLRequest.mResult;
    }

    public boolean removeCellBroadcastMsg(int channelId, int serialId) {
        log("removeCellBroadcastMsg(" + channelId + " , " + serialId + ")");
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Remove CellBroadcast Msg");
        IccSmsInterfaceManager.Request removeRequest = new IccSmsInterfaceManager.Request();
        synchronized (removeRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_REMOVE_BROADCAST_MSG_DONE, removeRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.removeCellBroadcastMsg(channelId, serialId, response);
            waitForResult(removeRequest);
        }
        return ((Boolean) removeRequest.mResult).booleanValue();
    }

    public boolean setEtwsConfig(int mode) {
        log("Calling setEtwsConfig(" + mode + ')');
        this.mContext.enforceCallingPermission("android.permission.RECEIVE_EMERGENCY_BROADCAST", "Set Etws Config");
        IccSmsInterfaceManager.Request etwsRequest = new IccSmsInterfaceManager.Request();
        synchronized (etwsRequest) {
            Message response = this.mMtkHandler.obtainMessage(111, etwsRequest);
            MtkRIL ci = this.mPhone.mCi;
            ci.setEtws(mode, response);
            waitForResult(etwsRequest);
        }
        return ((Boolean) etwsRequest.mResult).booleanValue();
    }

    public String getScAddress() {
        log("getScAddress");
        this.mContext.enforceCallingPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "get Sc Address");
        IccSmsInterfaceManager.Request getScRequest = new IccSmsInterfaceManager.Request();
        synchronized (getScRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_GET_SMSC_ADDRESS_DONE, getScRequest);
            this.mPhone.getSmscAddress(response);
            waitForResult(getScRequest);
        }
        log("getScAddress: exit");
        return (String) getScRequest.mResult;
    }

    public Bundle getScAddressWithErrorCode() {
        log("getScAddressWithErrorCode");
        this.mContext.enforceCallingPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "get Sc Address with error code");
        IccSmsInterfaceManager.Request getScERequest = new IccSmsInterfaceManager.Request();
        synchronized (getScERequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_GET_SMSC_ADDRESS_BUNDLE_DONE, getScERequest);
            this.mPhone.getSmscAddress(response);
            waitForResult(getScERequest);
        }
        log("getScAddressWithErrorCode error code done");
        return (Bundle) getScERequest.mResult;
    }

    public boolean setScAddress(String address) {
        log("setScAddressUsingSubId");
        this.mContext.enforceCallingPermission("android.permission.MODIFY_PHONE_STATE", "set Sc Address");
        IccSmsInterfaceManager.Request setScRequest = new IccSmsInterfaceManager.Request();
        synchronized (setScRequest) {
            Message response = this.mMtkHandler.obtainMessage(EVENT_SET_SMSC_ADDRESS_DONE, setScRequest);
            this.mPhone.setSmscAddress(address, response);
            waitForResult(setScRequest);
        }
        log("setScAddressUsingSubId result " + ((Boolean) setScRequest.mResult).booleanValue());
        return ((Boolean) setScRequest.mResult).booleanValue();
    }

    protected boolean setCellBroadcastActivation(boolean z) {
        boolean cellBroadcastActivation;
        log("Calling proprietary setCellBroadcastActivation(" + z + ')');
        if (this.mCurrentCellBroadcastActivation != z) {
            cellBroadcastActivation = super.setCellBroadcastActivation(z);
        } else {
            cellBroadcastActivation = DBG;
        }
        if (cellBroadcastActivation && this.mCurrentCellBroadcastActivation != z) {
            this.mCurrentCellBroadcastActivation = z ? 1 : 0;
            log("mCurrentCellBroadcastActivation change to " + this.mCurrentCellBroadcastActivation);
        }
        return cellBroadcastActivation;
    }

    public void sendTextWithOptions(String callingPackage, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, int encodingType) {
        this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
        if (this.mAppOps.noteOp(20, Binder.getCallingUid(), callingPackage) != 0) {
            return;
        }
        ((MtkSmsDispatchersController) this.mDispatchersController).sendTextWithEncodingType(destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, null, callingPackage, persistMessageForNonDefaultSmsApp, priority, expectMore, validityPeriod);
    }

    public void sendMultipartTextWithOptions(String callingPackage, String destAddr, String scAddr, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod, int encodingType) {
        String singlePart;
        PendingIntent singleSentIntent;
        this.mPhone.getContext().enforceCallingPermission("android.permission.SEND_SMS", "Sending SMS message");
        if (this.mAppOps.noteOp(20, Binder.getCallingUid(), callingPackage) != 0) {
            return;
        }
        exitECBMIfNeeded(callingPackage);
        String destAddr2 = filterDestAddress(destAddr);
        if (parts.size() > 1 && parts.size() < 10 && !android.telephony.SmsMessage.hasEmsSupport()) {
            for (int i = 0; i < parts.size(); i++) {
                String singlePart2 = parts.get(i);
                if (android.telephony.SmsMessage.shouldAppendPageNumberAsPrefix()) {
                    singlePart = String.valueOf(i + 1) + '/' + parts.size() + ' ' + singlePart2;
                } else {
                    singlePart = singlePart2.concat(' ' + String.valueOf(i + 1) + '/' + parts.size());
                }
                if (sentIntents != null && sentIntents.size() > i) {
                    PendingIntent singleSentIntent2 = sentIntents.get(i);
                    singleSentIntent = singleSentIntent2;
                } else {
                    singleSentIntent = null;
                }
                PendingIntent singleDeliveryIntent = null;
                if (deliveryIntents != null && deliveryIntents.size() > i) {
                    singleDeliveryIntent = deliveryIntents.get(i);
                }
                ((MtkSmsDispatchersController) this.mDispatchersController).sendTextWithEncodingType(destAddr2, scAddr, singlePart, encodingType, singleSentIntent, singleDeliveryIntent, null, callingPackage, persistMessageForNonDefaultSmsApp, priority, expectMore, validityPeriod);
            }
            return;
        }
        ((MtkSmsDispatchersController) this.mDispatchersController).sendMultipartTextWithEncodingType(destAddr2, scAddr, (ArrayList) parts, encodingType, (ArrayList) sentIntents, (ArrayList) deliveryIntents, null, callingPackage, persistMessageForNonDefaultSmsApp, priority, expectMore, validityPeriod);
    }

    private boolean isVVM(String callingPackage) {
        TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
        List<String> packages = new ArrayList<>();
        if (telecomManager != null) {
            packages.add(telecomManager.getDefaultDialerPackage());
            packages.add(telecomManager.getSystemDialerPackage());
        }
        PersistableBundle carrierConfig = null;
        long token = Binder.clearCallingIdentity();
        try {
            CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
            if (configManager != null) {
                carrierConfig = configManager.getConfigForSubId(this.mPhone.getSubId());
            }
            if (carrierConfig != null) {
                packages.add(carrierConfig.getString("carrier_vvm_package_name_string"));
                String[] vvmPackages = carrierConfig.getStringArray("carrier_vvm_package_name_string_array");
                if (vvmPackages != null && vvmPackages.length > 0) {
                    for (String packageName : vvmPackages) {
                        packages.add(packageName);
                    }
                }
            }
            log("vvm apks: " + packages);
            for (String packageName2 : packages) {
                if (callingPackage.equals(packageName2)) {
                    return DBG;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    private void exitECBMIfNeeded(String callingPackage) {
        if (this.mPhone.isInEcm() && !isVVM(callingPackage)) {
            log(callingPackage + " normal text message, exitECBM");
            this.mPhone.mCi.invokeOemRilRequestStrings(new String[]{"AT+VMEMEXIT", "+VMEMEXIT"}, (Message) null);
        }
    }
}
