package com.mediatek.internal.telephony.imsphone;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.CallQuality;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsInfo;
import android.telephony.ims.MediaQualityStatus;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.ims.MtkImsCallForwardInfo;
import com.mediatek.internal.telephony.MtkCallForwardInfo;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.ratconfiguration.RatConfiguration;
import com.mediatek.internal.telephony.worldphone.IWorldPhone;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsPhone extends ImsPhone {
    private static final String CFU_TIME_SLOT = "persist.vendor.radio.cfu.timeslot.";
    public static final int EVENT_GET_CALL_FORWARD_TIME_SLOT_DONE = 109;
    public static final int EVENT_SET_CALL_FORWARD_TIME_SLOT_DONE = 110;
    private static final String LOG_TAG = "MtkImsPhone";
    private boolean mIsCarrierConfigLoaded;
    private boolean mIsWfcModeHomeForDomRoaming;
    protected BroadcastReceiver mReceiver;

    public /* bridge */ /* synthetic */ void activateCellBroadcastSms(int i, Message message) {
        super.activateCellBroadcastSms(i, message);
    }

    public /* bridge */ /* synthetic */ boolean disableDataConnectivity() {
        return super.disableDataConnectivity();
    }

    public /* bridge */ /* synthetic */ void disableLocationUpdates() {
        super.disableLocationUpdates();
    }

    public /* bridge */ /* synthetic */ boolean enableDataConnectivity() {
        return super.enableDataConnectivity();
    }

    public /* bridge */ /* synthetic */ void enableLocationUpdates() {
        super.enableLocationUpdates();
    }

    public /* bridge */ /* synthetic */ void getAvailableNetworks(Message message) {
        super.getAvailableNetworks(message);
    }

    public /* bridge */ /* synthetic */ void getCellBroadcastSmsConfig(Message message) {
        super.getCellBroadcastSmsConfig(message);
    }

    public /* bridge */ /* synthetic */ int getDataActivityState() {
        return super.getDataActivityState();
    }

    public /* bridge */ /* synthetic */ boolean getDataRoamingEnabled() {
        return super.getDataRoamingEnabled();
    }

    public /* bridge */ /* synthetic */ String getDeviceId() {
        return super.getDeviceId();
    }

    public /* bridge */ /* synthetic */ String getDeviceSvn() {
        return super.getDeviceSvn();
    }

    public /* bridge */ /* synthetic */ String getEsn() {
        return super.getEsn();
    }

    public /* bridge */ /* synthetic */ String getGroupIdLevel1() {
        return super.getGroupIdLevel1();
    }

    public /* bridge */ /* synthetic */ String getGroupIdLevel2() {
        return super.getGroupIdLevel2();
    }

    public /* bridge */ /* synthetic */ IccCard getIccCard() {
        return super.getIccCard();
    }

    public /* bridge */ /* synthetic */ IccFileHandler getIccFileHandler() {
        return super.getIccFileHandler();
    }

    public /* bridge */ /* synthetic */ IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager() {
        return super.getIccPhoneBookInterfaceManager();
    }

    public /* bridge */ /* synthetic */ boolean getIccRecordsLoaded() {
        return super.getIccRecordsLoaded();
    }

    public /* bridge */ /* synthetic */ String getIccSerialNumber() {
        return super.getIccSerialNumber();
    }

    public /* bridge */ /* synthetic */ String getImei() {
        return super.getImei();
    }

    public /* bridge */ /* synthetic */ int getImeiType() {
        return super.getImeiType();
    }

    public /* bridge */ /* synthetic */ String getLine1AlphaTag() {
        return super.getLine1AlphaTag();
    }

    public /* bridge */ /* synthetic */ String getMeid() {
        return super.getMeid();
    }

    public /* bridge */ /* synthetic */ boolean getMessageWaitingIndicator() {
        return super.getMessageWaitingIndicator();
    }

    public /* bridge */ /* synthetic */ int getPhoneType() {
        return super.getPhoneType();
    }

    public /* bridge */ /* synthetic */ SignalStrength getSignalStrength() {
        return super.getSignalStrength();
    }

    public /* bridge */ /* synthetic */ String getSubscriberId() {
        return super.getSubscriberId();
    }

    public /* bridge */ /* synthetic */ int getTerminalBasedCallWaitingState(boolean z) {
        return super.getTerminalBasedCallWaitingState(z);
    }

    public /* bridge */ /* synthetic */ String getVoiceMailAlphaTag() {
        return super.getVoiceMailAlphaTag();
    }

    public /* bridge */ /* synthetic */ String getVoiceMailNumber() {
        return super.getVoiceMailNumber();
    }

    public /* bridge */ /* synthetic */ boolean handlePinMmi(String str) {
        return super.handlePinMmi(str);
    }

    public /* bridge */ /* synthetic */ boolean isDataAllowed() {
        return super.isDataAllowed();
    }

    public /* bridge */ /* synthetic */ boolean isUserDataEnabled() {
        return super.isUserDataEnabled();
    }

    public /* bridge */ /* synthetic */ void migrateFrom(Phone phone) {
        super.migrateFrom(phone);
    }

    public /* bridge */ /* synthetic */ boolean needsOtaServiceProvisioning() {
        return super.needsOtaServiceProvisioning();
    }

    public /* bridge */ /* synthetic */ void notifyCallForwardingIndicator() {
        super.notifyCallForwardingIndicator();
    }

    public /* bridge */ /* synthetic */ void notifyDisconnect(Connection connection) {
        super.notifyDisconnect(connection);
    }

    public /* bridge */ /* synthetic */ void notifyImsReason(ImsReasonInfo imsReasonInfo) {
        super.notifyImsReason(imsReasonInfo);
    }

    public /* bridge */ /* synthetic */ void notifyPhoneStateChanged() {
        super.notifyPhoneStateChanged();
    }

    public /* bridge */ /* synthetic */ void notifyPreciseCallStateChanged() {
        super.notifyPreciseCallStateChanged();
    }

    public /* bridge */ /* synthetic */ void notifyPreciseCallStateToNotifier() {
        super.notifyPreciseCallStateToNotifier();
    }

    public /* bridge */ /* synthetic */ void notifySuppServiceFailed(PhoneInternalInterface.SuppService suppService) {
        super.notifySuppServiceFailed(suppService);
    }

    public /* bridge */ /* synthetic */ void onCallQualityChanged(CallQuality callQuality, int i) {
        super.onCallQualityChanged(callQuality, i);
    }

    public /* bridge */ /* synthetic */ void onMediaQualityStatusChanged(MediaQualityStatus mediaQualityStatus) {
        super.onMediaQualityStatusChanged(mediaQualityStatus);
    }

    public /* bridge */ /* synthetic */ void onTtyModeReceived(int i) {
        super.onTtyModeReceived(i);
    }

    public /* bridge */ /* synthetic */ void registerForOnHoldTone(Handler handler, int i, Object obj) {
        super.registerForOnHoldTone(handler, i, obj);
    }

    public /* bridge */ /* synthetic */ void registerForRingbackTone(Handler handler, int i, Object obj) {
        super.registerForRingbackTone(handler, i, obj);
    }

    public /* bridge */ /* synthetic */ void registerForTtyModeReceived(Handler handler, int i, Object obj) {
        super.registerForTtyModeReceived(handler, i, obj);
    }

    public /* bridge */ /* synthetic */ void selectNetworkManually(OperatorInfo operatorInfo, boolean z, Message message) {
        super.selectNetworkManually(operatorInfo, z, message);
    }

    public /* bridge */ /* synthetic */ void setCellBroadcastSmsConfig(int[] iArr, Message message) {
        super.setCellBroadcastSmsConfig(iArr, message);
    }

    public /* bridge */ /* synthetic */ void setDataRoamingEnabled(boolean z) {
        super.setDataRoamingEnabled(z);
    }

    public /* bridge */ /* synthetic */ boolean setLine1Number(String str, String str2, Message message) {
        return super.setLine1Number(str, str2, message);
    }

    public /* bridge */ /* synthetic */ void setNetworkSelectionModeAutomatic(Message message) {
        super.setNetworkSelectionModeAutomatic(message);
    }

    public /* bridge */ /* synthetic */ void setRadioPower(boolean z) {
        super.setRadioPower(z);
    }

    public /* bridge */ /* synthetic */ void setTerminalBasedCallWaitingSupported(boolean z) {
        super.setTerminalBasedCallWaitingSupported(z);
    }

    public /* bridge */ /* synthetic */ void setVoiceMailNumber(String str, String str2, Message message) {
        super.setVoiceMailNumber(str, str2, message);
    }

    public /* bridge */ /* synthetic */ void startNetworkScan(NetworkScanRequest networkScanRequest, Message message) {
        super.startNetworkScan(networkScanRequest, message);
    }

    public /* bridge */ /* synthetic */ void startOnHoldTone(Connection connection) {
        super.startOnHoldTone(connection);
    }

    public /* bridge */ /* synthetic */ void startRingbackTone() {
        super.startRingbackTone();
    }

    public /* bridge */ /* synthetic */ void stopNetworkScan(Message message) {
        super.stopNetworkScan(message);
    }

    public /* bridge */ /* synthetic */ void stopOnHoldTone(Connection connection) {
        super.stopOnHoldTone(connection);
    }

    public /* bridge */ /* synthetic */ void stopRingbackTone() {
        super.stopRingbackTone();
    }

    public /* bridge */ /* synthetic */ void unregisterForOnHoldTone(Handler handler) {
        super.unregisterForOnHoldTone(handler);
    }

    public /* bridge */ /* synthetic */ void unregisterForRingbackTone(Handler handler) {
        super.unregisterForRingbackTone(handler);
    }

    public /* bridge */ /* synthetic */ void unregisterForTtyModeReceived(Handler handler) {
        super.unregisterForTtyModeReceived(handler);
    }

    public /* bridge */ /* synthetic */ void updateServiceLocation() {
        super.updateServiceLocation();
    }

    public MtkImsPhone(Context context, PhoneNotifier notifier, Phone defaultPhone) {
        this(context, notifier, defaultPhone, new ImsPhone.ImsManagerFactory() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhone$$ExternalSyntheticLambda0
            public final ImsManager create(Context context2, int i) {
                return ImsManager.getInstance(context2, i);
            }
        }, false);
    }

    public MtkImsPhone(Context context, PhoneNotifier notifier, Phone defaultPhone, ImsPhone.ImsManagerFactory imsManagerFactory, boolean unitTestMode) {
        super(context, notifier, defaultPhone, imsManagerFactory, unitTestMode);
        this.mIsCarrierConfigLoaded = false;
        this.mReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.imsphone.MtkImsPhone.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    MtkImsPhone.this.handleCarrierConfigChanged(intent);
                }
            }
        };
        logd("Start to create MtkImsPhone.");
        setPhoneName(LOG_TAG);
        cacheCarrierConfiguration();
        registerForListenCarrierConfigChanged();
    }

    public void dispose() {
        super.dispose();
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    protected Connection dialInternal(String dialString, PhoneInternalInterface.DialArgs dialArgs, ResultReceiver wrappedCallback) throws CallStateException {
        ImsPhone.ImsDialArgs.Builder imsDialArgsBuilder;
        boolean isUriNumber = PhoneNumberUtils.isUriNumber(dialString);
        String newDialString = dialString;
        if (!isUriNumber) {
            newDialString = PhoneNumberUtils.stripSeparators(dialString);
        }
        if (handleInCallMmiCommands(newDialString)) {
            return null;
        }
        if (!(dialArgs instanceof ImsPhone.ImsDialArgs)) {
            imsDialArgsBuilder = ImsPhone.ImsDialArgs.Builder.from(dialArgs);
        } else {
            imsDialArgsBuilder = ImsPhone.ImsDialArgs.Builder.from((ImsPhone.ImsDialArgs) dialArgs);
        }
        imsDialArgsBuilder.setClirMode(this.mCT.getClirMode());
        if (this.mDefaultPhone.getPhoneType() == 2) {
            return this.mCT.dial(dialString, imsDialArgsBuilder.build());
        }
        String networkPortion = dialString;
        if (!isUriNumber) {
            networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        }
        MtkImsPhoneMmiCode mmi = null;
        if (!isUriNumber) {
            mmi = MtkImsPhoneMmiCode.newFromDialString(networkPortion, this, wrappedCallback);
        } else {
            logd("dialInternal: url dial string, it must not be MMI");
        }
        boolean isEcc = MtkLocalPhoneNumberUtils.getIsEmergencyNumber();
        logd("dialInternal: dialing w/ mmi [" + mmi + "] isEcc: " + isEcc);
        if (mmi == null || isEcc) {
            return this.mCT.dial(dialString, imsDialArgsBuilder.build());
        }
        if (mmi.isTemporaryModeCLIR()) {
            imsDialArgsBuilder.setClirMode(mmi.getCLIRMode());
            return this.mCT.dial(mmi.getDialingNumber(), imsDialArgsBuilder.build());
        }
        if (!mmi.isSupportedOverImsPhone()) {
            logi("dialInternal: USSD not supported by IMS; fallback to CS.");
            throw new CallStateException("cs_fallback");
        }
        this.mPendingMMIs.add(mmi);
        this.mMmiRegistrants.notifyRegistrants(new AsyncResult((Object) null, mmi, (Throwable) null));
        try {
            logd("MMI processCode");
            mmi.processCode();
        } catch (CallStateException cse) {
            if ("cs_fallback".equals(cse.getMessage())) {
                logi("dialInternal: fallback to GSM required.");
                this.mPendingMMIs.remove(mmi);
                throw cse;
            }
        }
        return null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangupAll() throws CallStateException {
        logd("hangupAll");
        ((MtkImsPhoneCallTracker) this.mCT).hangupAll();
    }

    public void dumpPendingMmi() {
        int size = this.mPendingMMIs.size();
        if (size == 0) {
            logd("dumpPendingMmi: none");
            return;
        }
        for (int i = 0; i < size; i++) {
            logd("dumpPendingMmi: " + this.mPendingMMIs.get(i));
        }
    }

    protected boolean isValidCommandInterfaceCFReason(int commandInterfaceCFReason) {
        switch (commandInterfaceCFReason) {
            case 6:
                return true;
            default:
                return super.isValidCommandInterfaceCFReason(commandInterfaceCFReason);
        }
    }

    protected int getConditionFromCFReason(int reason) {
        switch (reason) {
            case 6:
                return 6;
            default:
                return super.getConditionFromCFReason(reason);
        }
    }

    protected int getCFReasonFromCondition(int condition) {
        switch (condition) {
            case 6:
                return 6;
            default:
                return super.getCFReasonFromCondition(condition);
        }
    }

    @Deprecated
    public void setCallForwardingOption(int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, int serviceClass, int timerSeconds, Message onComplete) {
        logd("setCallForwardingOption action=" + commandInterfaceCFAction + ", reason=" + commandInterfaceCFReason + " serviceClass=" + serviceClass);
        if (isValidCommandInterfaceCFAction(commandInterfaceCFAction) && isValidCommandInterfaceCFReason(commandInterfaceCFReason)) {
            super.setCallForwardingOption(commandInterfaceCFAction, commandInterfaceCFReason, dialingNumber, serviceClass, timerSeconds, onComplete);
        } else if (onComplete != null) {
            sendErrorResponse(onComplete);
        }
    }

    protected int getCBTypeFromFacility(String facility) {
        if (MtkRIL.CB_FACILITY_BA_ACR.equals(facility)) {
            return 6;
        }
        return super.getCBTypeFromFacility(facility);
    }

    private static class CfEx {
        final boolean mIsCfu;
        final Message mOnComplete;
        final String mSetCfNumber;
        final long[] mSetTimeSlot;

        CfEx(String cfNumber, long[] cfTimeSlot, boolean isCfu, Message onComplete) {
            this.mSetCfNumber = cfNumber;
            this.mSetTimeSlot = cfTimeSlot;
            this.mIsCfu = isCfu;
            this.mOnComplete = onComplete;
        }
    }

    public void saveTimeSlot(long[] timeSlot) {
        String timeSlotKey = CFU_TIME_SLOT + this.mPhoneId;
        String timeSlotString = "";
        if (timeSlot != null && timeSlot.length == 2) {
            timeSlotString = Long.toString(timeSlot[0]) + "," + Long.toString(timeSlot[1]);
        }
        SystemProperties.set(timeSlotKey, timeSlotString);
        logd("timeSlotString = " + timeSlotString);
    }

    public long[] getTimeSlot() {
        String timeSlotKey = CFU_TIME_SLOT + this.mPhoneId;
        String timeSlotString = SystemProperties.get(timeSlotKey, "");
        long[] timeSlot = null;
        if (timeSlotString != null && !timeSlotString.equals("")) {
            String[] timeArray = timeSlotString.split(",");
            if (timeArray.length == 2) {
                timeSlot = new long[2];
                for (int i = 0; i < 2; i++) {
                    timeSlot[i] = Long.parseLong(timeArray[i]);
                    Calendar calenar = Calendar.getInstance(TimeZone.getDefault());
                    calenar.setTimeInMillis(timeSlot[i]);
                    int hour = calenar.get(11);
                    int min = calenar.get(12);
                    Calendar calenar2 = Calendar.getInstance(TimeZone.getDefault());
                    calenar2.set(11, hour);
                    calenar2.set(12, min);
                    timeSlot[i] = calenar2.getTimeInMillis();
                }
            }
        }
        logd("timeSlot = " + Arrays.toString(timeSlot));
        return timeSlot;
    }

    public void getCallForwardInTimeSlot(int commandInterfaceCFReason, Message onComplete) {
        logd("getCallForwardInTimeSlot reason = " + commandInterfaceCFReason);
        if (commandInterfaceCFReason == 0) {
            logd("requesting call forwarding in a time slot query.");
            Message resp = obtainMessage(109, onComplete);
            try {
                this.mCT.getUtInterface().queryCallForwardInTimeSlot(getConditionFromCFReason(commandInterfaceCFReason), resp);
                return;
            } catch (ImsException e) {
                sendErrorResponse(onComplete, e);
                return;
            }
        }
        if (onComplete != null) {
            sendErrorResponse(onComplete);
        }
    }

    public void setCallForwardInTimeSlot(int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, int timerSeconds, long[] timeSlot, Message onComplete) {
        logd("setCallForwardInTimeSlot action = " + commandInterfaceCFAction + ", reason = " + commandInterfaceCFReason);
        if (isValidCommandInterfaceCFAction(commandInterfaceCFAction) && commandInterfaceCFReason == 0) {
            CfEx cfEx = new CfEx(dialingNumber, timeSlot, true, onComplete);
            Message resp = obtainMessage(110, commandInterfaceCFAction, 0, cfEx);
            try {
                this.mCT.getUtInterface().updateCallForwardInTimeSlot(getActionFromCFAction(commandInterfaceCFAction), getConditionFromCFReason(commandInterfaceCFReason), dialingNumber, timerSeconds, timeSlot, resp);
                return;
            } catch (ImsException e) {
                sendErrorResponse(onComplete, e);
                return;
            }
        }
        if (onComplete != null) {
            sendErrorResponse(onComplete);
        }
    }

    public void getCallForwardingOptionForServiceClass(int commandInterfaceCFReason, int serviceClass, Message onComplete) {
        logd("getCallForwardingOptionForServiceClass reason=" + commandInterfaceCFReason + ", service class= " + serviceClass);
        getCallForwardingOption(commandInterfaceCFReason, serviceClass, onComplete);
    }

    private MtkCallForwardInfo[] handleCfInTimeSlotQueryResult(MtkImsCallForwardInfo[] infos) {
        MtkCallForwardInfo[] cfInfos = null;
        if (infos != null && infos.length != 0) {
            cfInfos = new MtkCallForwardInfo[infos.length];
        }
        IccRecords r = this.mDefaultPhone.getIccRecords();
        if (infos == null || infos.length == 0) {
            if (r != null) {
                setVoiceCallForwardingFlag(r, 1, false, null);
            }
        } else {
            int s = infos.length;
            for (int i = 0; i < s; i++) {
                if (infos[i].mCondition == 0 && (infos[i].mServiceClass & 1) != 0 && r != null) {
                    setVoiceCallForwardingFlag(r, 1, infos[i].mStatus == 1, infos[i].mNumber);
                    saveTimeSlot(infos[i].mTimeSlot);
                }
                cfInfos[i] = getMtkCallForwardInfo(infos[i]);
            }
        }
        return cfInfos;
    }

    private MtkCallForwardInfo getMtkCallForwardInfo(MtkImsCallForwardInfo info) {
        MtkCallForwardInfo cfInfo = new MtkCallForwardInfo();
        cfInfo.status = info.mStatus;
        cfInfo.reason = getCFReasonFromCondition(info.mCondition);
        cfInfo.serviceClass = info.mServiceClass;
        cfInfo.toa = info.mToA;
        cfInfo.number = info.mNumber;
        cfInfo.timeSeconds = info.mTimeSeconds;
        cfInfo.timeSlot = info.mTimeSlot;
        return cfInfo;
    }

    public void sendUssdResponse(String ussdMessge) {
        dumpPendingMmi();
        super.sendUssdResponse(ussdMessge);
    }

    protected CommandException getCommandException(int code, String errorString) {
        logd("getCommandException code= " + code + ", errorString= " + errorString);
        CommandException.Error error = CommandException.Error.GENERIC_FAILURE;
        switch (code) {
            case 241:
                error = CommandException.Error.FDN_CHECK_FAILURE;
                break;
            case 801:
                error = CommandException.Error.REQUEST_NOT_SUPPORTED;
                break;
            case 802:
                error = CommandException.Error.RADIO_NOT_AVAILABLE;
                break;
            case 821:
                error = CommandException.Error.PASSWORD_INCORRECT;
                break;
            case 822:
                error = CommandException.Error.SS_MODIFIED_TO_DIAL;
                break;
            case 823:
                error = CommandException.Error.SS_MODIFIED_TO_USSD;
                break;
            case 824:
                error = CommandException.Error.SS_MODIFIED_TO_SS;
                break;
            case 825:
                error = CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO;
                break;
            case 61446:
                error = CommandException.Error.OPERATION_NOT_ALLOWED;
                break;
            case 61447:
                error = CommandException.Error.OEM_ERROR_3;
                break;
            case 61449:
                error = CommandException.Error.OEM_ERROR_25;
                break;
            case 61450:
                error = CommandException.Error.OEM_ERROR_7;
                break;
            case 61456:
                error = CommandException.Error.OEM_ERROR_6;
                break;
            case 61457:
                error = CommandException.Error.OEM_ERROR_24;
                break;
            case 61458:
                error = CommandException.Error.OEM_ERROR_23;
                break;
            case 61459:
                error = CommandException.Error.OEM_ERROR_22;
                break;
        }
        return new CommandException(error, errorString);
    }

    protected CallForwardInfo getCallForwardInfo(ImsCallForwardInfo info) {
        CallForwardInfo cfInfo = new CallForwardInfo();
        cfInfo.status = info.mStatus;
        cfInfo.reason = getCFReasonFromCondition(info.mCondition);
        cfInfo.serviceClass = info.mServiceClass;
        cfInfo.toa = info.mToA;
        cfInfo.number = info.mNumber;
        cfInfo.timeSeconds = info.mTimeSeconds;
        return cfInfo;
    }

    public CallForwardInfo[] handleCfQueryResult(ImsCallForwardInfo[] infos, int sc) {
        CallForwardInfo[] cfInfos = null;
        logd("handleCfQueryResult, sc:" + sc);
        if (infos != null && infos.length != 0) {
            cfInfos = new CallForwardInfo[infos.length];
        }
        if (infos == null || infos.length == 0) {
            setVoiceCallForwardingFlag(getIccRecords(), 1, false, null);
        } else {
            int voiceIndex = -1;
            int videoIndex = -1;
            boolean hasVideo = false;
            if (sc == 0 || (sc & 16) != 0 || (sc & 512) != 0) {
                hasVideo = true;
            }
            int s = infos.length;
            for (int i = 0; i < s; i++) {
                if (infos[i].mCondition == 0 && (infos[i].mServiceClass & 1) != 0) {
                    setVoiceCallForwardingFlag(getIccRecords(), 1, infos[i].mStatus == 1, infos[i].mNumber);
                    voiceIndex = i;
                }
                cfInfos[i] = getCallForwardInfo(infos[i]);
                if (hasVideo) {
                    if ((cfInfos[i].serviceClass & 16) != 0) {
                        cfInfos[i].serviceClass |= 512;
                        videoIndex = i;
                        logd("handleCfQueryResult, add SERVICE_CLASS_VIDEO, index=" + i);
                    } else if ((cfInfos[i].serviceClass & 512) != 0) {
                        videoIndex = i;
                        logd("handleCfQueryResult, SERVICE_CLASS_VIDEO, index=" + i);
                    }
                }
            }
            if (sc != 0) {
                if (sc == 1) {
                    if (voiceIndex >= 0) {
                        CallForwardInfo[] scCfInfosIndex = {cfInfos[voiceIndex]};
                        return scCfInfosIndex;
                    }
                } else if (sc == 528 && videoIndex >= 0) {
                    CallForwardInfo[] scCfInfosIndex2 = {cfInfos[videoIndex]};
                    return scCfInfosIndex2;
                }
            }
        }
        return cfInfos;
    }

    protected int[] handleCbQueryResult(ImsSsInfo[] infos) {
        int[] cbInfos = {infos[0].mStatus};
        return cbInfos;
    }

    private boolean isSsCdmaCsPreferred(int errerCode) {
        boolean result = false;
        boolean c2kRat = RatConfiguration.isC2kSupported();
        boolean c2kCardType = isCdmaSubscriptionAppPresent();
        if (c2kRat && c2kCardType) {
            result = errerCode == 61446 || errerCode == 61447;
        }
        logd("isSsCdmaCsPreferred, c2kRat = " + c2kRat + ", c2kCardType = " + c2kCardType + ", err = " + errerCode + ", ret = " + result);
        return result;
    }

    public boolean isCdmaSubscriptionAppPresent() {
        UiccCardApplication cdmaApplication = UiccController.getInstance().getUiccCardApplication(this.mPhoneId, 2);
        return cdmaApplication != null && (cdmaApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_CSIM || cdmaApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM);
    }

    private boolean isSupportCdma(int slotId) {
        String[] type = MtkTelephonyManagerEx.getDefault().getSupportCardType(slotId);
        if (type == null) {
            return false;
        }
        for (int i = 0; i < type.length; i++) {
            if ("RUIM".equals(type[i]) || "CSIM".equals(type[i])) {
                return true;
            }
        }
        return false;
    }

    public void handleMessage(Message msg) {
        ImsException imsException;
        Message resp;
        ImsException imsException2;
        Message resp2;
        AsyncResult ar = (AsyncResult) msg.obj;
        ImsPhone.SS ss = null;
        if (ar != null && (ar.userObj instanceof ImsPhone.SS)) {
            ss = (ImsPhone.SS) ar.userObj;
        }
        logd("Mtk handleMessage what=" + msg.what);
        if ((msg.what == 12 || msg.what == 13 || msg.what == 71 || msg.what == 72) && ar != null && (ar.exception instanceof ImsException)) {
            ImsException imsException3 = ar.exception;
            logd("SS IMS exception");
            if (ss != null && isSsCdmaCsPreferred(imsException3.getCode())) {
                sendResponseOrRetryOnCsfbSs(ss, msg.what, new ImsException("Preferred CDMA dialing method", 146), null);
                return;
            }
        }
        switch (msg.what) {
            case 12:
                IccRecords r = this.mDefaultPhone.getIccRecords();
                int cfAction = msg.arg1;
                int i = msg.arg2;
                boolean zIsCfEnable = isCfEnable(cfAction);
                boolean isCfu = false;
                int serviceClass = -1;
                if (ss != null) {
                    isCfu = ss.mCfReason == 0;
                    serviceClass = ss.mServiceClass;
                }
                if (isCfu && ar.exception == null && r != null && (serviceClass & 1) != 0) {
                    setVoiceCallForwardingFlag(r, 1, zIsCfEnable, ss.mDialingNumber);
                }
                sendResponse(ss.mOnComplete, null, ar.exception);
                break;
            case 13:
                CallForwardInfo[] cfInfos = null;
                if (ar.exception == null) {
                    cfInfos = handleCfQueryResult((ImsCallForwardInfo[]) ar.result, ss.mServiceClass);
                }
                sendResponse(ss.mOnComplete, cfInfos, ar.exception);
                break;
            case 69:
                sendResponse(ss.mOnComplete, null, ar.exception);
                break;
            case IWorldPhone.EVENT_RESUME_CAMPING_1 /* 70 */:
                int[] ssInfos = null;
                if (ar.exception == null) {
                    ssInfos = handleCbQueryResult((ImsSsInfo[]) ar.result);
                }
                sendResponse(ss.mOnComplete, ssInfos, ar.exception);
                break;
            case 109:
                MtkCallForwardInfo[] mtkCfInfos = null;
                if (ar.exception == null) {
                    mtkCfInfos = handleCfInTimeSlotQueryResult((MtkImsCallForwardInfo[]) ar.result);
                }
                if (ar.exception != null && (ar.exception instanceof ImsException) && (imsException = ar.exception) != null && imsException.getCode() == 61446 && (resp = (Message) ar.userObj) != null) {
                    AsyncResult.forMessage(resp, mtkCfInfos, new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED));
                    resp.sendToTarget();
                } else {
                    sendResponse((Message) ar.userObj, mtkCfInfos, ar.exception);
                }
                break;
            case 110:
                IccRecords records = this.mDefaultPhone.getIccRecords();
                CfEx cfEx = (CfEx) ar.userObj;
                if (cfEx.mIsCfu && ar.exception == null && records != null) {
                    int cfAction2 = msg.arg1;
                    setVoiceCallForwardingFlag(records, 1, isCfEnable(cfAction2), cfEx.mSetCfNumber);
                    saveTimeSlot(cfEx.mSetTimeSlot);
                }
                if (ar.exception != null && (ar.exception instanceof ImsException) && (imsException2 = ar.exception) != null && imsException2.getCode() == 61446 && (resp2 = cfEx.mOnComplete) != null) {
                    AsyncResult.forMessage(resp2, (Object) null, new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED));
                    resp2.sendToTarget();
                } else {
                    sendResponse(cfEx.mOnComplete, null, ar.exception);
                }
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updateRoamingState(ServiceState ss) {
        if (ss == null) {
            loge("updateRoamingState: null ServiceState!");
            return;
        }
        boolean newRoamingState = ss.getRoaming();
        if (this.mLastKnownRoamingState == newRoamingState) {
            return;
        }
        boolean isInService = ss.getVoiceRegState() == 0 || ss.getDataRegState() == 0;
        if (!isInService) {
            logi("updateRoamingState: we are OUT_OF_SERVICE, ignoring roaming change.");
            return;
        }
        if (this.mCT.getState() == PhoneConstants.State.IDLE) {
            logd("updateRoamingState now: " + newRoamingState);
            this.mLastKnownRoamingState = newRoamingState;
            CarrierConfigManager configManager = (CarrierConfigManager) getContext().getSystemService("carrier_config");
            if (configManager != null && CarrierConfigManager.isConfigForIdentifiedCarrier(configManager.getConfigForSubId(getSubId())) && this.mIsCarrierConfigLoaded) {
                ImsManager imsManager = ImsManager.getInstance(this.mContext, this.mPhoneId);
                if (this.mIsWfcModeHomeForDomRoaming) {
                    int voiceRoamingType = ss.getVoiceRoamingType();
                    int dataRoamingType = ss.getDataRoamingType();
                    if (this.mLastKnownRoamingState && (voiceRoamingType == 2 || dataRoamingType == 2)) {
                        logd("Convert new roaming to HOME if it's domestic roaming,  voiceRoamingType: " + voiceRoamingType + " dataRoamingType: " + dataRoamingType);
                        imsManager.setWfcMode(imsManager.getWfcMode(false), false);
                        return;
                    }
                }
                imsManager.setWfcMode(imsManager.getWfcMode(newRoamingState), newRoamingState);
                return;
            }
            return;
        }
        logd("updateRoamingState postponed: " + newRoamingState);
        this.mCT.registerForVoiceCallEnded(this, 77, (Object) null);
    }

    public void setServiceState(int state) {
        super.setServiceState(state);
        updateIsEmergencyOnly();
        if (isImsRegistered()) {
            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
            Rlog.d(LOG_TAG, "isImsRegistered: " + isImsRegistered());
            notificationManager.cancel("wifi_calling", 1);
        }
    }

    private boolean isSupportImsEcc() {
        return ((MtkImsPhoneCallTracker) this.mCT).isSupportImsEcc();
    }

    public void updateIsEmergencyOnly() {
        ServiceState ss = getServiceState();
        logd("updateIsEmergencyOnly() sst: " + ss.getState() + " supportImsEcc: " + isSupportImsEcc());
        if (ss.getState() == 1 && isSupportImsEcc()) {
            this.mSS.setEmergencyOnly(true);
        } else {
            this.mSS.setEmergencyOnly(false);
        }
    }

    private void registerForListenCarrierConfigChanged() {
        if (this.mContext == null) {
            logd("registerForListenCarrierConfigChanged failed");
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentfilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCarrierConfigChanged(Intent intent) {
        int subId;
        int phoneId = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1);
        if (phoneId == this.mPhoneId && !intent.getBooleanExtra("android.telephony.extra.REBROADCAST_ON_UNLOCK", false) && (subId = intent.getIntExtra("subscription", -1)) == getSubId()) {
            logd("Receive carrierConfig changed phoneId: " + this.mPhoneId + " subId: " + subId);
            cacheCarrierConfiguration();
            if (subId != -1) {
                this.mIsCarrierConfigLoaded = true;
            } else {
                this.mIsCarrierConfigLoaded = false;
            }
        }
    }

    private void cacheCarrierConfiguration() {
        int subId = getSubId();
        CarrierConfigManager configMgr = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        if (configMgr == null) {
            logd("cacheCarrierConfiguration failed: config mgr access failed");
            return;
        }
        PersistableBundle carrierConfig = configMgr.getConfigForSubId(subId);
        if (carrierConfig == null) {
            logd("cacheCarrierConfiguration failed: carrier config access failed");
        } else {
            this.mIsWfcModeHomeForDomRoaming = carrierConfig.getBoolean("mtk_carrier_wfc_mode_domestic_roaming_to_home");
            logd("cacheCarrierConfiguration,  WfcModeHomeForDomRoaming: " + this.mIsWfcModeHomeForDomRoaming);
        }
    }

    public void setVoiceCallForwardingFlag(IccRecords r, int line, boolean enable, String number) {
        IccRecords record;
        super.setVoiceCallForwardingFlag(r, line, enable, number);
        if (this.mDefaultPhone.getPhoneType() == 2 && (this.mDefaultPhone instanceof MtkGsmCdmaPhone) && this.mDefaultPhone.isGsmSsPrefer()) {
            UiccController uiccCtl = UiccController.getInstance();
            if (uiccCtl != null && (record = uiccCtl.getIccRecords(this.mPhoneId, 1)) != null) {
                record.setVoiceCallForwardingFlag(line, enable, number);
            }
            this.mDefaultPhone.notifyCallForwardingIndicator();
        }
    }

    protected boolean needNotifySrvccState() {
        return true;
    }
}
