package com.mediatek.internal.telephony;

import android.telephony.TelephonyDisplayInfo;
import com.android.internal.telephony.DisplayInfoController;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public final class MtkDisplayInfoController extends DisplayInfoController {
    private static final String TAG = "MtkDisplayInfoController";

    public MtkDisplayInfoController(Phone phone) {
        super(phone);
        if (this.mNetworkTypeController != null) {
            this.mNetworkTypeController.quitNow();
        }
        this.mNetworkTypeController = new MtkNetworkTypeController(phone, this);
        this.mNetworkTypeController.sendMessage(0);
    }

    public void updateTelephonyDisplayInfo() {
        TelephonyDisplayInfo newDisplayInfo = new TelephonyDisplayInfo(this.mNetworkTypeController.getDataNetworkType(), this.mNetworkTypeController.getOverrideNetworkType(), this.mServiceState.getRoaming());
        if (!newDisplayInfo.equals(this.mTelephonyDisplayInfo)) {
            MtkServiceStateTracker sst = (MtkServiceStateTracker) this.mPhone.getServiceStateTracker();
            logl("TelephonyDisplayInfo changed from " + this.mTelephonyDisplayInfo + " to " + newDisplayInfo + ", turboSS alive:" + sst.isTurboSSAlive());
            validateDisplayInfo(newDisplayInfo);
            this.mTelephonyDisplayInfo = newDisplayInfo;
            this.mTelephonyDisplayInfoChangedRegistrants.notifyRegistrants();
            if (!sst.isTurboSSAlive()) {
                this.mPhone.notifyDisplayInfoChanged(this.mTelephonyDisplayInfo);
            }
        }
    }
}
