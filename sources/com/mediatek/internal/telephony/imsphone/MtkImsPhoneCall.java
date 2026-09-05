package com.mediatek.internal.telephony.imsphone;

import android.telephony.Rlog;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsPhoneCall extends ImsPhoneCall {
    private static final String LOG_TAG = "MtkImsPhoneCall";

    public MtkImsPhoneCall(ImsPhoneCallTracker owner, String context) {
        super(owner, context);
    }

    void resetRingbackTone() {
        this.mIsRingbackTonePlaying = false;
    }

    protected ImsPhoneCall makeTempImsPhoneCall() {
        return new MtkImsPhoneCall(this.mOwner, "UK");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangupIfAlive(int reason) {
        if (this.mState.isAlive()) {
            try {
                Rlog.d(LOG_TAG, "hangupIfAlive with reason: " + reason);
                hangupWithCause(reason);
            } catch (CallStateException ex) {
                Rlog.w(LOG_TAG, " hangupIfActive: caught " + ex);
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void hangupWithCause(int reason) throws CallStateException {
        Rlog.d(LOG_TAG, "hangup with reason: " + reason);
        ((MtkImsPhoneCallTracker) this.mOwner).hangup(this, reason);
    }
}
