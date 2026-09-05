package com.mediatek.internal.telephony;

import android.content.Context;
import android.hardware.radio.config.IRadioConfig;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.RadioConfig;
import com.android.telephony.Rlog;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioConfig extends RadioConfig {
    static final int EVENT_RETRY_GET_SERVICE = 1001;
    static final int GET_SERVICE_DELAY_MILLIS = 1000;
    private static final String TAG = "MtkRadioConfig";

    public MtkRadioConfig(Context context, HalVersion radioHalVersion) {
        super(context, radioHalVersion);
    }

    public void handleMessage(Message message) {
        if (message.what == 1001) {
            Rlog.d(TAG, "handleMessage: EVENT_RETRY_GET_SERVICE");
            getRadioConfigProxy(null);
        } else {
            super.handleMessage(message);
        }
    }

    protected void updateRadioConfigProxy() {
        removeMessages(1001);
        if (!isRildReady()) {
            sendMessageDelayed(obtainMessage(1001), 1000L);
        } else {
            super.updateRadioConfigProxy();
        }
    }

    private boolean isRildReady() {
        String aidlServiceName = IRadioConfig.DESCRIPTOR + "/default";
        if (ServiceManager.isDeclared(aidlServiceName)) {
            if (ServiceManager.checkService(aidlServiceName) == null) {
                Rlog.i(TAG, "AIDL Service not ready: " + aidlServiceName);
                return false;
            }
            return true;
        }
        try {
            if (android.hardware.radio.config.V1_0.IRadioConfig.getService(false) == null) {
                Rlog.i(TAG, "HIDL Service not ready.");
                return false;
            }
            return true;
        } catch (RemoteException | NoSuchElementException e) {
            Rlog.e(TAG, "HIDL Service not ready, exception: " + e);
            return false;
        }
    }

    protected boolean isGetHidlServiceSync() {
        return false;
    }
}
