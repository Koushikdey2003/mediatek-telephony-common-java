package com.mediatek.internal.telephony.datasub;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.data.MtkDataHelper;

/* JADX INFO: loaded from: classes.dex */
public class SmartDataSwitchAssistant extends Handler {
    private static final boolean DBG = true;
    private static final int EVENT_ID_INTVL = 10;
    private static final int EVENT_SERVICE_STATE_CHANGED = 20;
    private static final int EVENT_SRVCC_STATE_CHANGED = 10;
    private static final String LOG_TAG = "SmartDataSwitch";
    private static final String TEMP_DATA_MODE = "temp_data_mode";
    public static final int TEMP_DATA_MODE_DEFAULT = 0;
    public static final int TEMP_DATA_MODE_EXCEPT_CSFB = 2;
    public static final int TEMP_DATA_MODE_ONLY_VOLTE = 1;
    private static SmartDataSwitchAssistant sSmartDataSwitchAssistant = null;
    private Context mContext;
    private int mPhoneNum;
    private Phone[] mPhones;
    private final RegistrantList mReEvalueRegistrants;
    private ContentResolver mResolver;
    private boolean isSrvccDuringCall = false;
    private int mVoiceNetworkType = 0;
    private int mInCallPhoneId = -1;

    public void updatePhones(Phone[] phones) {
        logd("updatePhones: prev=" + this.mPhoneNum + ", current=" + phones.length);
        if (this.mPhoneNum > phones.length) {
            return;
        }
        int i = this.mPhoneNum;
        this.mPhones = phones;
        this.mPhoneNum = phones.length;
        if (isSmartDataSwitchSupport()) {
            registerEvents();
        }
    }

    public static SmartDataSwitchAssistant makeSmartDataSwitchAssistant(Context context, Phone[] phones) {
        if (context == null || phones == null) {
            throw new RuntimeException("param is null");
        }
        SmartDataSwitchAssistant smartDataSwitchAssistant = sSmartDataSwitchAssistant;
        if (smartDataSwitchAssistant == null) {
            sSmartDataSwitchAssistant = new SmartDataSwitchAssistant(context, phones);
        } else {
            smartDataSwitchAssistant.updatePhones(phones);
        }
        logd("makeSDSA: X sSDSA =" + sSmartDataSwitchAssistant);
        return sSmartDataSwitchAssistant;
    }

    public static SmartDataSwitchAssistant getInstance() {
        SmartDataSwitchAssistant smartDataSwitchAssistant = sSmartDataSwitchAssistant;
        if (smartDataSwitchAssistant == null) {
            throw new RuntimeException("Should not be called before sSmartDataSwitchAssistant");
        }
        return smartDataSwitchAssistant;
    }

    private SmartDataSwitchAssistant(Context context, Phone[] phones) {
        this.mContext = null;
        logd("SmartDataSwitchAssistant is created");
        this.mPhones = phones;
        this.mPhoneNum = phones.length;
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        if (isSmartDataSwitchSupport()) {
            registerEvents();
        }
        this.mReEvalueRegistrants = new RegistrantList();
        logd("init done");
    }

    public void dispose() {
        logd("SmartDataSwitchAssistant.dispose");
        if (isSmartDataSwitchSupport()) {
            unregisterEvents();
        }
    }

    private void registerEvents() {
        logd("registerEvents");
    }

    private void unregisterEvents() {
        logd("unregisterEvents");
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        int phoneId = msg.what % 10;
        int eventId = msg.what - phoneId;
        switch (eventId) {
            case 10:
                logd("SRVCC, phoneId=" + phoneId);
                onSrvccStateChanged();
                this.mReEvalueRegistrants.notifyRegistrants();
                break;
            case 20:
                if (onServiceStateChanged(phoneId)) {
                    logd("EVENT_SERVICE_STATE_CHANGED: notify");
                    this.mReEvalueRegistrants.notifyRegistrants();
                }
                break;
            default:
                logd("Unhandled message with number: " + msg.what);
                break;
        }
    }

    public void regSrvccEvent() {
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].registerForHandoverStateChanged(this, i + 10, (Object) null);
        }
    }

    public void unregSrvccEvent() {
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].unregisterForHandoverStateChanged(this);
        }
    }

    public void regServiceStateChangedEvent() {
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].registerForServiceStateChanged(this, i + 20, (Object) null);
        }
    }

    public void unregServiceStateChangedEvent() {
        for (int i = 0; i < this.mPhoneNum; i++) {
            this.mPhones[i].unregisterForServiceStateChanged(this);
        }
    }

    public boolean isVoLteCalling(int phoneId) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            loge("isVoLteCalling: invalid phoneId");
            return false;
        }
        Phone phone = this.mPhones[phoneId];
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null) {
                return imsPhone.isInCall();
            }
            loge("isVoLteCalling: mPhones[" + phoneId + "] imsphone is null!");
            return false;
        }
        loge("isVoLteCalling: mPhones[" + phoneId + "] is null!");
        return false;
    }

    public int getVoiceNetworkType(int phoneId) {
        if (phoneId == -1) {
            loge("updateCallType() invalid Phone Id!");
            return 0;
        }
        return this.mPhones[phoneId].getServiceStateTracker().mSS.getRilVoiceRadioTechnology();
    }

    public void onSrvccStateChanged() {
        logd("onSrvccStateChanged()");
        this.isSrvccDuringCall = DBG;
    }

    public boolean onServiceStateChanged(int phoneId) {
        if (phoneId != getInCallPhoneId()) {
            return false;
        }
        int voiceNwType = getVoiceNetworkType(phoneId);
        if (isNetworkTypeChanged(voiceNwType)) {
            return DBG;
        }
        return false;
    }

    private boolean isSmartDataSwitchSupport() {
        return SystemProperties.get("persist.vendor.radio.smart.data.switch").equals(RadioCapabilitySwitchUtil.IMSI_READY);
    }

    public void registerReEvaluateEvent(Handler h, int what, Object obj, int phoneId) {
        if (!isSmartDataSwitchSupport()) {
            logd("registerReEvaluateEvent: not have TempDataSwitchCapability");
            return;
        }
        Registrant r = new Registrant(h, what, obj);
        logd("registerReEvaluateEvent()");
        setInCallPhoneId(phoneId);
        this.mReEvalueRegistrants.add(r);
        this.isSrvccDuringCall = false;
        regServiceStateChangedEvent();
        regSrvccEvent();
    }

    private boolean isNetworkTypeChanged(int newVoiceNwType) {
        logd("isNetworkTypeChanged: mVoiceNetworkType=" + this.mVoiceNetworkType + " newVoiceNwType=" + newVoiceNwType);
        if (this.mVoiceNetworkType != newVoiceNwType) {
            this.mVoiceNetworkType = newVoiceNwType;
            return DBG;
        }
        return false;
    }

    public void unregisterReEvaluateEvent(Handler h) {
        if (!isSmartDataSwitchSupport()) {
            logd("unregisterReEvaluateEvent: not have TempDataSwitchCapability");
            return;
        }
        logd("unregisterReEvaluateEvent()");
        setInCallPhoneId(-1);
        this.mReEvalueRegistrants.remove(h);
        this.isSrvccDuringCall = false;
        unregServiceStateChangedEvent();
        unregSrvccEvent();
    }

    public boolean checkIsSwitchAvailable(int voiceCallphoneId, int primaryPhoneId, int preferPhoneId) {
        Phone phone;
        if (!isSmartDataSwitchSupport()) {
            logd("checkIsSwitchAvailable: not have TempDataSwitchCapability");
            return false;
        }
        if (!getAospTemporaryDataSettings(voiceCallphoneId)) {
            logd("checkIsSwitchAvailable() settings is off, not passed");
            return false;
        }
        if (MtkDataHelper.getInstance().getInternalDsdaMode() == 1 && (phone = findPhoneById(primaryPhoneId)) != null && phone.isDataAllowed()) {
            if (SubscriptionManager.isValidPhoneId(primaryPhoneId) && SubscriptionManager.isValidPhoneId(preferPhoneId) && primaryPhoneId != preferPhoneId) {
                return DBG;
            }
            return false;
        }
        int nwType = getVoiceNetworkType(voiceCallphoneId);
        boolean isVoLteCalling = isVoLteCalling(voiceCallphoneId);
        int mode = getTemporaryDataMode();
        logd("checkIsSwitchAvailable() nwType=" + nwType + ", isCdma=" + ServiceState.isCdma(nwType) + ", isVoLteCalling=" + isVoLteCalling + ", isSrvccDuringCall=" + this.isSrvccDuringCall);
        if (mode == 0) {
            if (ServiceState.isCdma(nwType) || nwType == 16) {
                logd("checkIsSwitchAvailable(): not passed, mode TEMP_DATA_MODE_DEFAULT");
                return false;
            }
        } else if (mode == 1) {
            if (ServiceState.isCdma(nwType) || nwType == 16 || !isVoLteCalling) {
                logd("checkIsSwitchAvailable(): not passed, mode TEMP_DATA_MODE_ONLY_VOLTE");
                return false;
            }
        } else if (mode == 2) {
            if (ServiceState.isCdma(nwType) || nwType == 16 || (!isVoLteCalling && !this.isSrvccDuringCall)) {
                logd("checkIsSwitchAvailable(): not passed, mode TEMP_DATA_MODE_EXCEPT_CSFB");
                return false;
            }
        } else {
            logd("checkIsSwitchAvailable(): not passed, unknown mode:" + mode);
        }
        logd("checkIsSwitchAvailable(): passed");
        return DBG;
    }

    public void onDsdaStateChanged() {
        logd("onDsdaStateChanged: notify");
        this.mReEvalueRegistrants.notifyRegistrants();
    }

    public boolean getAospTemporaryDataSettings(int phoneId) {
        Phone phone = findPhoneById(phoneId);
        return MtkDataHelper.getInstance().isAutoSwitchEnabledByUser(phone);
    }

    private Phone findPhoneById(int phoneId) {
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            loge("findPhoneById: invalid phoneId");
            return null;
        }
        return PhoneFactory.getPhone(phoneId);
    }

    public int getTemporaryDataMode() {
        int mode = Settings.Global.getInt(this.mResolver, TEMP_DATA_MODE, 0);
        return mode;
    }

    public void setInCallPhoneId(int phoneId) {
        this.mInCallPhoneId = phoneId;
    }

    private int getInCallPhoneId() {
        return this.mInCallPhoneId;
    }

    protected static void logv(String s) {
        Rlog.v(LOG_TAG, s);
    }

    protected static void logd(String s) {
        Rlog.d(LOG_TAG, s);
    }

    protected static void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }

    protected static void logi(String s) {
        Rlog.i(LOG_TAG, s);
    }
}
