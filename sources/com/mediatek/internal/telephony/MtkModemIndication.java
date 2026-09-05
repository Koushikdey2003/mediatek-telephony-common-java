package com.mediatek.internal.telephony;

import android.content.Intent;
import android.os.UserHandle;
import com.android.internal.telephony.ModemIndication;
import com.android.internal.telephony.RILUtils;

/* JADX INFO: loaded from: classes.dex */
public class MtkModemIndication extends ModemIndication {
    private MtkRIL mMtkRil;

    public MtkModemIndication(MtkRIL ril) {
        super(ril);
        this.mMtkRil = ril;
    }

    public void radioStateChanged(int indicationType, int radioState) {
        int oldState = this.mMtkRil.getRadioState();
        super.radioStateChanged(indicationType, radioState);
        int newState = this.mMtkRil.getRadioState();
        if (newState != oldState) {
            Intent intent = new Intent("com.mediatek.intent.action.RADIO_STATE_CHANGED");
            int transferedState = RILUtils.convertHalRadioState(radioState);
            intent.putExtra("radioState", transferedState);
            intent.putExtra("subId", MtkSubscriptionManager.getSubIdUsingPhoneId(this.mMtkRil.mInstanceId.intValue()));
            this.mMtkRil.mMtkContext.sendBroadcastAsUser(intent, UserHandle.ALL);
            this.mMtkRil.riljLog("Broadcast for RadioStateChanged: state=" + transferedState);
        }
    }

    public void rilConnected(int indicationType) {
        this.mMtkRil.processIndication(3, indicationType);
        this.mMtkRil.unsljLog(1034);
        if (!this.mMtkRil.getModemService().equals("default")) {
            this.mMtkRil.setRadioPower(false, null);
            this.mMtkRil.riljLog("rilConnected with service " + this.mMtkRil.getModemService());
        }
        MtkRIL mtkRIL = this.mMtkRil;
        mtkRIL.setCdmaSubscriptionSource(mtkRIL.mCdmaSubscription, null);
        this.mMtkRil.notifyRegistrantsRilConnectionChanged(15);
    }
}
