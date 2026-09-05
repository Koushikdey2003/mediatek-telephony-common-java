package com.mediatek.internal.telephony.data;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.datasub.SmartDataSwitchAssistant;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MtkPhoneSwitcher extends PhoneSwitcher {
    private static final String EVALUATION_REASON_RADIO_ON = "EVENT_RADIO_ON";
    private static final int EVENT_CALL_EVALUATE = 1001;
    private static final String LOG_TAG = "MtkPhoneSwitcher";
    private static final boolean VDBG = true;
    private boolean mIsInCall;
    private boolean mTempDataSwitching;

    public static MtkPhoneSwitcher mtkMake(int maxDataAttachModemCount, Context context, Looper looper) {
        if (sPhoneSwitcher == null) {
            sPhoneSwitcher = new MtkPhoneSwitcher(maxDataAttachModemCount, context, looper);
        }
        return (MtkPhoneSwitcher) sPhoneSwitcher;
    }

    public MtkPhoneSwitcher(int maxActivePhones, Context context, Looper looper) {
        super(maxActivePhones, context, looper);
        this.mIsInCall = false;
        this.mTempDataSwitching = false;
    }

    public static MtkPhoneSwitcher getInstance() {
        return (MtkPhoneSwitcher) sPhoneSwitcher;
    }

    protected boolean onEvaluate(boolean requestsChanged, String reason) {
        StringBuilder sb = new StringBuilder(reason);
        boolean diffDetected = false;
        boolean isPrimaryDataSubIdChanged = false;
        int validSubCount = 0;
        int primaryDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
        if (primaryDataSubId != this.mPrimaryDataSubId) {
            sb.append(" mPrimaryDataSubId ").append(this.mPrimaryDataSubId).append("->").append(primaryDataSubId);
            this.mPrimaryDataSubId = primaryDataSubId;
            this.mLastSwitchPreferredDataReason = 1;
            this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
            sendEmptyMessage(MtkGsmCdmaPhone.EVENT_UNSOL_RADIO_CAPABILITY_CHANGED);
            isPrimaryDataSubIdChanged = VDBG;
        }
        boolean hasAnyActiveSubscription = false;
        for (int i = 0; i < this.mActiveModemCount; i++) {
            int sub = SubscriptionManager.getSubscriptionId(i);
            if (SubscriptionManager.isValidSubscriptionId(sub)) {
                hasAnyActiveSubscription = VDBG;
                validSubCount++;
            }
            if (sub != this.mPhoneSubscriptions[i]) {
                sb.append(" phone[").append(i).append("] ").append(this.mPhoneSubscriptions[i]);
                sb.append("->").append(sub);
                if (this.mAutoSelectedDataSubId == this.mPhoneSubscriptions[i]) {
                    this.mAutoSelectedDataSubId = Integer.MAX_VALUE;
                }
                this.mPhoneSubscriptions[i] = sub;
                if (SubscriptionManager.isValidSubscriptionId(sub)) {
                    registerForImsRadioTechChange(this.mContext, i);
                }
                diffDetected = VDBG;
            }
        }
        if (!hasAnyActiveSubscription) {
            transitionToEmergencyPhone();
        } else {
            log("Found an active subscription");
        }
        if (isPrimaryDataSubIdChanged) {
            updateDataEnabledState(validSubCount == 1);
        }
        int oldPreferredDataPhoneId = this.mPreferredDataPhoneId;
        int oldPreferredDataSubId = this.mPreferredDataSubId.get();
        if (hasAnyActiveSubscription) {
            updatePreferredDataPhoneId();
        }
        if (oldPreferredDataPhoneId != this.mPreferredDataPhoneId) {
            sb.append(" preferred data phoneId ").append(oldPreferredDataPhoneId).append("->").append(this.mPreferredDataPhoneId);
            diffDetected = VDBG;
        }
        if (oldPreferredDataSubId != this.mPreferredDataSubId.get()) {
            logl("SIM refresh, notify dds change");
            notifyPreferredDataSubIdChanged();
        }
        if (diffDetected || EVALUATION_REASON_RADIO_ON.equals(reason)) {
            logl("evaluating due to " + ((Object) sb));
            for (int phoneId = 0; phoneId < this.mActiveModemCount; phoneId++) {
                this.mPhoneStates[phoneId].active = VDBG;
            }
            int phoneId2 = this.mPreferredDataPhoneId;
            sendRilCommands(phoneId2);
        }
        return diffDetected;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected boolean updatesIfPhoneInVoiceCallChanged() {
        int oldPhoneIdInVoiceCall = this.mPhoneIdInVoiceCall;
        this.mPhoneIdInVoiceCall = -1;
        ArrayList<Integer> phoneIdInVoiceCallList = new ArrayList<>();
        for (Phone phone : PhoneFactory.getPhones()) {
            if (isPhoneInVoiceCall(phone) || isPhoneInVoiceCall(phone.getImsPhone())) {
                phoneIdInVoiceCallList.add(Integer.valueOf(phone.getPhoneId()));
            }
        }
        int nonRadioCallId = MtkDataHelper.getInstance().getNonRadioCallingPhone(phoneIdInVoiceCallList);
        if (nonRadioCallId != -1) {
            phoneIdInVoiceCallList.remove(new Integer(nonRadioCallId));
        }
        if (phoneIdInVoiceCallList.size() > 0) {
            this.mPhoneIdInVoiceCall = phoneIdInVoiceCallList.get(0).intValue();
        }
        SmartDataSwitchAssistant smartDataSwitchAssistant = SmartDataSwitchAssistant.getInstance();
        if (this.mPhoneIdInVoiceCall != -1) {
            if (!this.mIsInCall) {
                smartDataSwitchAssistant.registerReEvaluateEvent(this, 1001, null, this.mPhoneIdInVoiceCall);
                this.mIsInCall = VDBG;
            } else {
                smartDataSwitchAssistant.setInCallPhoneId(this.mPhoneIdInVoiceCall);
            }
        } else {
            this.mIsInCall = false;
            this.mTempDataSwitching = false;
            smartDataSwitchAssistant.unregisterReEvaluateEvent(this);
        }
        if (this.mPhoneIdInVoiceCall == oldPhoneIdInVoiceCall) {
            return false;
        }
        logl("isPhoneInVoiceCallChanged from phoneId " + oldPhoneIdInVoiceCall + " to phoneId " + this.mPhoneIdInVoiceCall);
        return VDBG;
    }

    protected boolean shouldSwitchDataDueToInCall() {
        SmartDataSwitchAssistant smartDataSwitchAssistant = SmartDataSwitchAssistant.getInstance();
        Phone voicePhone = findPhoneById(this.mPhoneIdInVoiceCall);
        Phone defaultDataPhone = getPhoneBySubId(this.mPrimaryDataSubId);
        boolean ret = defaultDataPhone != null && defaultDataPhone.isUserDataEnabled() && voicePhone != null && voicePhone.isDataAllowed() && smartDataSwitchAssistant.checkIsSwitchAvailable(this.mPhoneIdInVoiceCall, defaultDataPhone.getPhoneId(), this.mPreferredDataPhoneId);
        if (ret) {
            this.mTempDataSwitching = VDBG;
        }
        return ret;
    }

    protected void onDataEnabledChanged() {
        updateDataEnabledState(false);
        super.onDataEnabledChanged();
    }

    protected boolean isUserDataSettingEnabledOnNonDDS(Phone phone) {
        if (phone == null || !phone.isUserDataEnabled()) {
            return false;
        }
        return VDBG;
    }

    protected boolean isAutoSwitchEnabledByUser(Phone phone) {
        return MtkDataHelper.getInstance().isAutoSwitchEnabledByUser(phone);
    }

    protected boolean ignoreCheckingAutoSwitchBack() {
        if (this.mPhoneIdInVoiceCall != -1 && this.mPreferredDataPhoneId == this.mPhoneIdInVoiceCall) {
            cancelPendingAutoDataSwitch();
            return VDBG;
        }
        return false;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 108:
                log(EVALUATION_REASON_RADIO_ON);
                readDeviceResourceConfig();
                super.handleMessage(msg);
                break;
            case 109:
            case 120:
                log("message " + msg.what + " received");
                MtkDataHelper.getInstance().notifyPreciseCallStateChanged();
                super.handleMessage(msg);
                break;
            case 1001:
                log("EVENT_CALL_EVALUATE");
                onEvaluate(false, "CALL_EVALUATE");
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private void updateDataEnabledState(boolean isUpdatePrimary) {
        Phone phone;
        Phone primaryDataPhone = getPhoneBySubId(this.mPrimaryDataSubId);
        int primaryPhoneId = primaryDataPhone != null ? primaryDataPhone.getPhoneId() : -1;
        for (int phoneId = 0; phoneId < this.mActiveModemCount; phoneId++) {
            int sub = SubscriptionManager.getSubscriptionId(phoneId);
            if (SubscriptionManager.isValidSubscriptionId(sub) && ((isUpdatePrimary || phoneId != primaryPhoneId) && (phone = findPhoneById(phoneId)) != null && phone.getDataSettingsManager() != null)) {
                phone.getDataSettingsManager().updateDataEnabledAndNotify(4);
            }
        }
    }

    private boolean needHandleDialing(int phoneId) {
        Phone phone = findPhoneById(phoneId);
        if (phone != null && phone.getDataNetworkController() != null) {
            DataConfigManager dcm = phone.getDataNetworkController().getDataConfigManager();
            return ((MtkDataConfigManager) dcm).getHandleDialingDuringCall();
        }
        return VDBG;
    }

    protected boolean isPhoneInVoiceCall(Phone phone) {
        if (phone == null) {
            return false;
        }
        boolean isVolteCall = SmartDataSwitchAssistant.getInstance().isVoLteCalling(phone.getPhoneId());
        boolean isDsdalikeAvailable = MtkDataHelper.getInstance().isDsdalikeAvailable(phone.getPhoneId());
        boolean isHandleDialing = needHandleDialing(phone.getPhoneId());
        log("ForegroundCall:" + phone.getForegroundCall().getState() + ", RingingCall:" + phone.getRingingCall().getState() + ", BackgroundCall: " + phone.getBackgroundCall().getState() + ", getPhoneId(): " + phone.getPhoneId() + ", isVolteCall: " + isVolteCall + ", isDsdalikeAvailable: " + isDsdalikeAvailable + ", isHandleDialing: " + isHandleDialing);
        if (isDsdalikeAvailable && phone.getRingingCall().getState().isRinging() && isVolteCall) {
            log("dsdalikable and volte ring call exist.");
            if (phone.getForegroundCall().getState() != Call.State.ACTIVE && phone.getForegroundCall().getState() != Call.State.ALERTING && ((!isHandleDialing || phone.getForegroundCall().getState() != Call.State.DIALING) && phone.getForegroundCall().getState() != Call.State.HOLDING && phone.getBackgroundCall().getState() != Call.State.HOLDING)) {
                return false;
            }
            return VDBG;
        }
        if (phone.getForegroundCall().getState() != Call.State.ACTIVE && phone.getForegroundCall().getState() != Call.State.ALERTING && ((!isHandleDialing || phone.getForegroundCall().getState() != Call.State.DIALING) && phone.getForegroundCall().getState() != Call.State.HOLDING && phone.getBackgroundCall().getState() != Call.State.HOLDING && !phone.getRingingCall().getState().isRinging())) {
            return false;
        }
        return VDBG;
    }

    public boolean getTempDataSwitchState() {
        int subId = this.mSubscriptionManagerService.getDefaultDataSubId();
        if (!this.mTempDataSwitching || this.mSubscriptionManagerService.getPhoneId(subId) == this.mPhoneIdInVoiceCall) {
            return false;
        }
        return VDBG;
    }
}
