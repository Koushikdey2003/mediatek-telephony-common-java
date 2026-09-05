package com.mediatek.internal.telephony.emergency;

import android.content.Context;
import android.os.SystemProperties;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.emergency.RadioOnHelper;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.IRadioPower;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.RadioManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioOnHelper extends RadioOnHelper {
    private static final String TAG = "MtkRadioOnHelper";
    Callback mCallback;
    private RadioPowerInterface mRadioPowerIf;

    public interface Callback {
        void onEnterMtkEmergencyMode(Phone phone);
    }

    class RadioPowerInterface implements IRadioPower {
        RadioPowerInterface() {
        }

        @Override // com.mediatek.internal.telephony.IRadioPower
        public void notifyRadioPowerChange(boolean power, int phoneId) {
            Rlog.d(MtkRadioOnHelper.TAG, "notifyRadioPowerChange, power:" + power + " phoneId:" + phoneId);
            if (power) {
                int mainPhoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
                Phone mainPhone = PhoneFactory.getPhone(mainPhoneId);
                if (MtkRadioOnHelper.this.mCallback != null) {
                    MtkRadioOnHelper.this.mCallback.onEnterMtkEmergencyMode(mainPhone);
                }
                RadioManager.unregisterForRadioPowerChange(MtkRadioOnHelper.this.mRadioPowerIf);
                MtkRadioOnHelper.this.mRadioPowerIf = null;
            }
        }
    }

    public MtkRadioOnHelper(Context context, Callback cb) {
        super(context);
        this.mCallback = null;
        this.mRadioPowerIf = null;
        this.mCallback = cb;
    }

    public void triggerRadioOnAndListen(RadioOnStateListener.Callback callback, boolean forEmergencyCall, Phone phoneForEmergencyCall, boolean isTestEmergencyNumber, int emergencyTimeoutIntervalMillis) {
        if (phoneForEmergencyCall.getHalVersion().less(RIL.RADIO_HAL_VERSION_1_5) || !SystemProperties.get("vendor.ril.call.emci_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            RadioPowerInterface radioPowerInterface = new RadioPowerInterface();
            this.mRadioPowerIf = radioPowerInterface;
            RadioManager.registerForRadioPowerChange("RadioOnHelper", radioPowerInterface);
        }
        super.triggerRadioOnAndListen(callback, forEmergencyCall, phoneForEmergencyCall, isTestEmergencyNumber, emergencyTimeoutIntervalMillis);
    }

    public void onComplete(RadioOnStateListener listener, boolean isRadioReady) {
        super.onComplete(listener, isRadioReady);
        RadioPowerInterface radioPowerInterface = this.mRadioPowerIf;
        if (radioPowerInterface != null) {
            RadioManager.unregisterForRadioPowerChange(radioPowerInterface);
        }
    }
}
