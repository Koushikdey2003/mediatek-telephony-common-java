package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.GsmCdmaCall;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaConnection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.telephony.Rlog;
import com.mediatek.telephony.MtkTelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class MtkGsmCdmaConnection extends GsmCdmaConnection {
    private static final int MO_CALL_VIBRATE_TIME = 200;
    private static final String PROP_LOG_TAG = "GsmCdmaConnection";
    private boolean mIsIncomingCallGwsd;
    private boolean mIsRealConnected;
    private boolean mReceivedAccepted;
    String mRedirectingAddress;
    int mRejectCauseToRIL;

    public synchronized boolean isIncomingCallGwsd() {
        log("isIncomingCallGwsd: " + this.mIsIncomingCallGwsd);
        return this.mIsIncomingCallGwsd;
    }

    public MtkGsmCdmaConnection(GsmCdmaPhone phone, String dialString, GsmCdmaCallTracker ct, GsmCdmaCall parent, PhoneInternalInterface.DialArgs dialArgs) {
        super(phone, dialString, ct, parent, dialArgs);
        this.mRejectCauseToRIL = -1;
        this.mIsIncomingCallGwsd = false;
        this.mIsRealConnected = false;
        this.mReceivedAccepted = false;
    }

    public MtkGsmCdmaConnection(GsmCdmaPhone phone, DriverCall dc, GsmCdmaCallTracker ct, int index) {
        super(phone, dc, ct, index);
        this.mRejectCauseToRIL = -1;
        this.mIsIncomingCallGwsd = false;
        if (((MtkGsmCdmaCallTracker) ct).mHelper.isGwsdCall()) {
            this.mIsIncomingCallGwsd = true;
            ((MtkGsmCdmaCallTracker) ct).mHelper.setGwsdCall(false);
        }
    }

    public MtkGsmCdmaConnection(Context context, CdmaCallWaitingNotification cw, GsmCdmaCallTracker ct, GsmCdmaCall parent) {
        super(context, cw, ct, parent);
        this.mRejectCauseToRIL = -1;
        this.mIsIncomingCallGwsd = false;
    }

    public boolean compareTo(DriverCall c) {
        return super.compareTo(c) || isIncomingCallGwsd();
    }

    public String getRedirectingAddress() {
        return this.mRedirectingAddress;
    }

    public void setRedirectingAddress(String address) {
        this.mRedirectingAddress = address;
    }

    public boolean isRealConnected() {
        return this.mIsRealConnected;
    }

    boolean onCdmaCallAccepted() {
        log("onCdmaCallAccepted, mIsRealConnected=" + this.mIsRealConnected + ", state=" + getState());
        if (getState() != Call.State.ACTIVE) {
            this.mReceivedAccepted = true;
            return false;
        }
        if (!this.mIsRealConnected) {
            this.mIsRealConnected = true;
            processNextPostDialChar();
            vibrateForAccepted();
        }
        return true;
    }

    private boolean isInChina() {
        int phoneId = this.mParent.getPhone().getPhoneId();
        TelephonyManager tm = TelephonyManager.from(this.mParent.getPhone().getContext());
        String numeric = tm.getNetworkOperatorForPhone(phoneId);
        String countryIso = "";
        if (TextUtils.isEmpty(numeric)) {
            numeric = MtkTelephonyManagerEx.getDefault().getLocatedPlmn(phoneId);
        }
        log("isInChina, numeric=" + Rlog.pii(PROP_LOG_TAG, numeric));
        if (TextUtils.isEmpty(numeric)) {
            countryIso = tm.getNetworkCountryIso(phoneId);
            log("isInChina, countryIso=" + Rlog.pii(PROP_LOG_TAG, countryIso));
        }
        return (numeric != null && numeric.indexOf(RadioCapabilitySwitchUtil.CN_MCC) == 0) || "cn".equals(countryIso);
    }

    private void vibrateForAccepted() {
        String prop = SystemProperties.get("persist.vendor.radio.telecom.vibrate", RadioCapabilitySwitchUtil.IMSI_READY);
        if ("0".equals(prop)) {
            log("vibrateForAccepted, disabled by Engineer Mode");
        } else {
            Vibrator vibrator = (Vibrator) this.mParent.getPhone().getContext().getSystemService("vibrator");
            vibrator.vibrate(200L);
        }
    }

    void resumeHoldAfterDialFailed() {
        if (this.mParent != null) {
            this.mParent.detach(this);
        }
        this.mParent = this.mOwner.mForegroundCall;
        this.mParent.attachFake(this, Call.State.ACTIVE);
    }

    void updateConferenceParticipantAddress(String address) {
        this.mAddress = address;
    }

    public boolean isMultiparty() {
        if (this.mOwner != null && (this.mOwner instanceof MtkGsmCdmaCallTracker) && !((MtkGsmCdmaCallTracker) this.mOwner).hasParsingCEPCapability() && this.mParent != null) {
            return this.mParent.isMultiparty();
        }
        return super.isMultiparty();
    }

    public void setRejectWithCause(int telephonyDisconnectCode) {
        if (this.mParent != null && this.mOwner != null) {
            GsmCdmaPhone phone = this.mOwner.getPhone();
            if (MtkIncomingCallChecker.isMtkEnhancedCallBlockingEnabled(phone.getContext(), phone.getSubId())) {
                log("setRejectWithCause set (" + this.mRejectCauseToRIL + " to " + telephonyDisconnectCode + ")");
                this.mRejectCauseToRIL = telephonyDisconnectCode;
                return;
            }
            return;
        }
        log("setRejectWithCause fail. mParent(" + this.mParent + "), mOwner(" + this.mOwner + ")");
    }

    public int getRejectWithCause() {
        return this.mRejectCauseToRIL;
    }

    public void clearRejectWithCause() {
        if (this.mRejectCauseToRIL != -1) {
            log("clearRejectWithCause (" + this.mRejectCauseToRIL + " to -1)");
            this.mRejectCauseToRIL = -1;
        }
    }

    public void onHangupLocal() {
        clearRejectWithCause();
        super.onHangupLocal();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.CallStateException */
    public void consultativeTransfer(Connection other) throws CallStateException {
        GsmCdmaPhone phone = this.mOwner.getPhone();
        if (phone != null) {
            if (phone.canTransfer()) {
                phone.explicitCallTransfer();
                return;
            }
            throw new CallStateException("cs can transfer check fail");
        }
        throw new CallStateException("cs ect fail (phone null)");
    }

    protected void onConnectedOut() {
        if (isPhoneTypeGsm()) {
            processNextPostDialChar();
            return;
        }
        int count = this.mParent.mConnections.size();
        log("mParent.mConnections.size()=" + count);
        for (Connection c : this.mParent.mConnections) {
            if (!c.isAlive()) {
                count--;
            }
        }
        if (!isInChina() && !this.mIsRealConnected && count == 1) {
            this.mIsRealConnected = true;
            processNextPostDialChar();
            vibrateForAccepted();
        }
        if (count > 1) {
            this.mIsRealConnected = true;
            processNextPostDialChar();
        }
    }

    protected void onUpdateDone() {
        if (!isPhoneTypeGsm()) {
            log("state=" + getState() + ", mReceivedAccepted=" + this.mReceivedAccepted);
            if (getState() == Call.State.ACTIVE && this.mReceivedAccepted) {
                onCdmaCallAccepted();
                this.mReceivedAccepted = false;
            }
        }
    }
}
