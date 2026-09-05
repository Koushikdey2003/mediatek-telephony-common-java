package com.mediatek.internal.telephony;

import android.app.ActivityThread;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.ServiceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SmsRawData;
import com.mediatek.internal.telephony.IMtkSms;
import java.util.List;
import mediatek.telephony.MtkSimSmsInsertStatus;
import mediatek.telephony.MtkSmsParameters;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccSmsController extends IMtkSms.Stub {
    static final String LOG_TAG = "Mtk_RIL_UiccSmsController";

    /* JADX WARN: Multi-variable type inference failed */
    protected MtkUiccSmsController() {
        if (ServiceManager.getService("imtksms") == null) {
            ServiceManager.addService("imtksms", this);
        }
    }

    private Phone getPhone(int subId) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(subId));
        if (phone == null) {
            return PhoneFactory.getDefaultPhone();
        }
        return phone;
    }

    private void sendErrorInPendingIntent(PendingIntent intent, int errorCode) {
        if (intent != null) {
            try {
                intent.send(errorCode);
            } catch (PendingIntent.CanceledException e) {
            }
        }
    }

    private void sendErrorInPendingIntents(List<PendingIntent> intents, int errorCode) {
        for (PendingIntent intent : intents) {
            sendErrorInPendingIntent(intent, errorCode);
        }
    }

    private MtkIccSmsInterfaceManager getIccSmsInterfaceManager(int subId) {
        return (MtkIccSmsInterfaceManager) getPhone(subId).getIccSmsInterfaceManager();
    }

    public List<SmsRawData> getAllMessagesFromIccEfByModeForSubscriber(int subId, String callingPackage, int mode) {
        if (!isSmsReadyForSubscriber(subId)) {
            Rlog.e(LOG_TAG, "getAllMessagesFromIccEf SMS not ready");
            return null;
        }
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getAllMessagesFromIccEfByMode(callingPackage, mode);
        }
        Rlog.e(LOG_TAG, "getAllMessagesFromIccEfByModeForSubscriber iccSmsIntMgr is null for Subscription: " + subId);
        return null;
    }

    public int copyTextMessageToIccCardForSubscriber(int subId, String callingPackage, String scAddress, String address, List<String> text, int status, long timestamp) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            int result = iccSmsIntMgr.copyTextMessageToIccCard(callingPackage, scAddress, address, text, status, timestamp);
            return result;
        }
        Rlog.e(LOG_TAG, "sendStoredMultipartText iccSmsIntMgr is null for subscription: " + subId);
        return 1;
    }

    public void sendDataWithOriginalPortForSubscriber(int subId, String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendDataInternal(subId, callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent, true);
    }

    public void sendData(int subId, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendDataInternal(subId, ActivityThread.currentPackageName(), destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent, false);
    }

    private void sendDataInternal(int subId, String callingPackage, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean checkPermission) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.sendDataWithOriginalPort(callingPackage, destAddr, scAddr, destPort, originalPort, data, sentIntent, deliveryIntent, checkPermission);
        } else {
            Rlog.e(LOG_TAG, "sendDataInternal iccSmsIntMgr is null forsubscription: " + subId);
        }
    }

    public boolean isSmsReadyForSubscriber(int subId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.isSmsReady();
        }
        Rlog.e(LOG_TAG, "isSmsReady iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }

    public void setSmsMemoryStatusForSubscriber(int subId, boolean status) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.setSmsMemoryStatus(status);
        } else {
            Rlog.e(LOG_TAG, "setSmsMemoryStatus iccSmsIntMgr is null forsubscription: " + subId);
        }
    }

    public MtkIccSmsStorageStatus getSmsSimMemoryStatusForSubscriber(int subId, String callingPackage) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getSmsSimMemoryStatus(callingPackage);
        }
        Rlog.e(LOG_TAG, "setSmsMemoryStatus iccSmsIntMgr is null forsubscription: " + subId);
        return null;
    }

    public void sendTextWithEncodingTypeForSubscriber(int subId, String callingPackage, String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.sendTextWithEncodingType(callingPackage, destAddr, scAddr, text, encodingType, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp);
        } else {
            Rlog.e(LOG_TAG, "sendTextWithEncodingTypeForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
            sendErrorInPendingIntent(sentIntent, 1);
        }
    }

    public void sendMultipartTextWithEncodingTypeForSubscriber(int subId, String callingPackage, String destAddr, String scAddr, List<String> parts, int encodingType, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.sendMultipartTextWithEncodingType(callingPackage, destAddr, scAddr, parts, encodingType, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp);
        } else {
            Rlog.e(LOG_TAG, "sendMultipartTextWithEncodingTypeForSubscriber iccSmsIntMgr is null for subscription: " + subId);
            sendErrorInPendingIntents(sentIntents, 1);
        }
    }

    public MtkSimSmsInsertStatus insertTextMessageToIccCardForSubscriber(int subId, String callingPackage, String scAddress, String address, List<String> text, int status, long timestamp) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.insertTextMessageToIccCard(callingPackage, scAddress, address, text, status, timestamp);
            return null;
        }
        Rlog.e(LOG_TAG, "sendMultipartTextWithEncodingTypeForSubscriber iccSmsIntMgr is null for subscription: " + subId);
        return null;
    }

    public MtkSimSmsInsertStatus insertRawMessageToIccCardForSubscriber(int subId, String callingPackage, int status, byte[] pdu, byte[] smsc) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            MtkSimSmsInsertStatus ret = iccSmsIntMgr.insertRawMessageToIccCard(callingPackage, status, pdu, smsc);
            return ret;
        }
        Rlog.e(LOG_TAG, "insertRawMessageToIccCardForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return null;
    }

    public void sendTextWithExtraParamsForSubscriber(int subId, String callingPackage, String destAddr, String scAddr, String text, Bundle extraParams, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.sendTextWithExtraParams(callingPackage, destAddr, scAddr, text, extraParams, sentIntent, deliveryIntent, persistMessageForNonDefaultSmsApp);
        } else {
            Rlog.e(LOG_TAG, "sendTextWithExtraParamsForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
            sendErrorInPendingIntent(sentIntent, 1);
        }
    }

    public void sendMultipartTextWithExtraParamsForSubscriber(int subId, String callingPackage, String destAddr, String scAddr, List<String> parts, Bundle extraParams, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            iccSmsIntMgr.sendMultipartTextWithExtraParams(callingPackage, destAddr, scAddr, parts, extraParams, sentIntents, deliveryIntents, persistMessageForNonDefaultSmsApp);
        } else {
            Rlog.e(LOG_TAG, "sendTextWithExtraParamsForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
            sendErrorInPendingIntents(sentIntents, 1);
        }
    }

    public MtkSmsParameters getSmsParametersForSubscriber(int subId, String callingPackage) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getSmsParameters(callingPackage);
        }
        Rlog.e(LOG_TAG, "getSmsParametersForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return null;
    }

    public boolean setSmsParametersForSubscriber(int subId, String callingPackage, MtkSmsParameters params) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.setSmsParameters(callingPackage, params);
        }
        Rlog.e(LOG_TAG, "setSmsParametersForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }

    public SmsRawData getMessageFromIccEfForSubscriber(int subId, String callingPackage, int index) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getMessageFromIccEf(callingPackage, index);
        }
        Rlog.e(LOG_TAG, "getMessageFromIccEfForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return null;
    }

    public boolean queryCellBroadcastSmsActivationForSubscriber(int subId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.queryCellBroadcastSmsActivation();
        }
        Rlog.e(LOG_TAG, "setCellBroadcastSmsConfigForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }

    public boolean activateCellBroadcastSmsForSubscriber(int subId, boolean activate) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.activateCellBroadcastSms(activate);
        }
        Rlog.e(LOG_TAG, "activateCellBroadcastSmsForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }

    public boolean removeCellBroadcastMsgForSubscriber(int subId, int channelId, int serialId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.removeCellBroadcastMsg(channelId, serialId);
        }
        Rlog.e(LOG_TAG, "removeCellBroadcastMsg iccSmsIntMgr is null for subscription: " + subId);
        return false;
    }

    public boolean setEtwsConfigForSubscriber(int subId, int mode) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.setEtwsConfig(mode);
        }
        Rlog.e(LOG_TAG, "setEtwsConfigForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }

    public String getCellBroadcastRangesForSubscriber(int subId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getCellBroadcastRanges();
        }
        Rlog.e(LOG_TAG, "getCellBroadcastRangesForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return "";
    }

    public boolean setCellBroadcastLangsForSubscriber(int subId, String lang) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.setCellBroadcastLangs(lang);
        }
        Rlog.e(LOG_TAG, "setCellBroadcastLangsForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }

    public String getCellBroadcastLangsForSubscriber(int subId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getCellBroadcastLangs();
        }
        Rlog.e(LOG_TAG, "getCellBroadcastLangsForSubscriber iccSmsIntMgr is null forsubscription: " + subId);
        return "";
    }

    public String getScAddressForSubscriber(int subId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getScAddress();
        }
        Rlog.e(LOG_TAG, "getScAddress iccSmsIntMgr is null forsubscription: " + subId);
        return null;
    }

    public Bundle getScAddressWithErrorCodeForSubscriber(int subId) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.getScAddressWithErrorCode();
        }
        Rlog.e(LOG_TAG, "getScAddressWithErrorCode iccSmsIntMgr is null forsubscription: " + subId);
        return null;
    }

    public boolean setScAddressForSubscriber(int subId, String address) {
        MtkIccSmsInterfaceManager iccSmsIntMgr = getIccSmsInterfaceManager(subId);
        if (iccSmsIntMgr != null) {
            return iccSmsIntMgr.setScAddress(address);
        }
        Rlog.e(LOG_TAG, "setScAddress iccSmsIntMgr is null forsubscription: " + subId);
        return false;
    }
}
