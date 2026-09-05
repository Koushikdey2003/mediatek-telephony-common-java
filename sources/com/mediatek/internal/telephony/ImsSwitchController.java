package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CommandsInterface;
import com.mediatek.ims.internal.IMtkImsService;
import com.mediatek.ims.internal.MtkImsManager;

/* JADX INFO: loaded from: classes.dex */
public class ImsSwitchController extends Handler {
    private static final int BIND_IMS_SERVICE_DELAY_IN_MILLIS = 2000;
    static final String LOG_TAG = "ImsSwitchController";
    private static IMtkImsService mMtkImsService = null;
    private CommandsInterface[] mCi;
    private Context mContext;
    private int mPhoneCount;
    private RadioPowerInterface mRadioPowerIf;
    private ImsServiceDeathRecipient mDeathRecipient = new ImsServiceDeathRecipient();
    private Runnable mBindImsServiceRunnable = new Runnable() { // from class: com.mediatek.internal.telephony.ImsSwitchController.1
        @Override // java.lang.Runnable
        public void run() {
            Rlog.w(ImsSwitchController.LOG_TAG, "try to bind ImsService again");
            if (!ImsSwitchController.this.checkAndBindImsService(0)) {
                ImsSwitchController.this.postDelayed(this, 2000L);
                return;
            }
            ImsSwitchController.log("manually updateImsServiceConfig");
            if (!MtkImsManager.isSupportMims()) {
                int phoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
                ImsManager imsManager = ImsManager.getInstance(ImsSwitchController.this.mContext, phoneId);
                if (imsManager != null) {
                    imsManager.updateImsServiceConfig();
                    return;
                }
                return;
            }
            for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
                ImsManager imsManager2 = ImsManager.getInstance(ImsSwitchController.this.mContext, i);
                if (imsManager2 != null) {
                    imsManager2.updateImsServiceConfig();
                }
            }
        }
    };

    ImsSwitchController(Context context, int phoneCount, CommandsInterface[] ci) {
        log("Initialize ImsSwitchController enter");
        this.mContext = context;
        this.mCi = ci;
        this.mPhoneCount = phoneCount;
        if (SystemProperties.get("persist.vendor.ims_support").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            RadioPowerInterface radioPowerInterface = new RadioPowerInterface();
            this.mRadioPowerIf = radioPowerInterface;
            RadioManager.registerForRadioPowerChange(LOG_TAG, radioPowerInterface);
            if (mMtkImsService == null) {
                checkAndBindImsService(0);
            }
        }
    }

    class RadioPowerInterface implements IRadioPower {
        RadioPowerInterface() {
        }

        @Override // com.mediatek.internal.telephony.IRadioPower
        public void notifyRadioPowerChange(boolean power, int phoneId) {
            ImsSwitchController.log("notifyRadioPowerChange, power:" + power + " phoneId:" + phoneId);
            if (!MtkImsManager.isSupportMims() && RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() != phoneId) {
                ImsSwitchController.log("radio power change ignore due to phone id isn't LTE phone");
                return;
            }
            if (SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
                ImsSwitchController.log("[" + phoneId + "] Modem auto registration so that we don't triggerImsService updateRadioState");
                return;
            }
            if (ImsSwitchController.mMtkImsService == null) {
                ImsSwitchController.this.checkAndBindImsService(phoneId);
            }
            if (ImsSwitchController.mMtkImsService != null) {
                int radioState = power ? 1 : 0;
                try {
                    ImsSwitchController.mMtkImsService.updateRadioState(radioState, phoneId);
                } catch (RemoteException e) {
                    Rlog.e(ImsSwitchController.LOG_TAG, "RemoteException can't notify power state change");
                }
            } else {
                Rlog.w(ImsSwitchController.LOG_TAG, "notifyRadioPowerChange: ImsService not ready !!!");
            }
            ImsSwitchController.log("radio power change processed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkAndBindImsService(int phoneId) {
        IBinder b = ServiceManager.getService("mtkIms");
        if (b != null) {
            try {
                b.linkToDeath(this.mDeathRecipient, 0);
                mMtkImsService = IMtkImsService.Stub.asInterface(b);
                log("checkAndBindImsService: mMtkImsService = " + mMtkImsService);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    private class ImsServiceDeathRecipient implements IBinder.DeathRecipient {
        private ImsServiceDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Rlog.w(ImsSwitchController.LOG_TAG, "ImsService died detected");
            ImsSwitchController.mMtkImsService = null;
            ImsSwitchController imsSwitchController = ImsSwitchController.this;
            imsSwitchController.postDelayed(imsSwitchController.mBindImsServiceRunnable, 2000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String s) {
        Rlog.d(LOG_TAG, s);
    }
}
