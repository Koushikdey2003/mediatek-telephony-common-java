package com.mediatek.internal.telephony.imsphone;

import android.net.Uri;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ims.ImsCallProfile;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.ims.internal.ConferenceParticipant;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkImsPhoneConnection extends ImsPhoneConnection {
    private static final String EXTRA_IMS_GWSD = "ims_gwsd";
    private static final String LOG_TAG = "MtkImsPhoneConnection";
    public static final int STATE_AUDIO_RINGTONE = 1;
    public static final int STATE_VIDEO_RINGTONE = 2;
    public static final int SUPPORTS_AUDIO_RINGTONE = 4096;
    public static final int SUPPORTS_VIDEO_RINGTONE = 8192;
    private int mCallIdBeforeDisconnected;
    private List<ConferenceParticipant> mConferenceParticipants;
    private boolean mIsIncomingCallDuringRttEmcGuard;
    private boolean mIsIncomingCallGwsd;
    private boolean mIsRttVideoSwitchSupported;
    public boolean mWasMultiparty;
    public boolean mWasPreMultipartyHost;

    public MtkImsPhoneConnection(Phone phone, ImsCall imsCall, ImsPhoneCallTracker ct, ImsPhoneCall parent, boolean isUnknown) {
        super(phone, imsCall, ct, parent, isUnknown);
        this.mConferenceParticipants = null;
        this.mWasMultiparty = false;
        this.mWasPreMultipartyHost = false;
        this.mCallIdBeforeDisconnected = -1;
        this.mIsRttVideoSwitchSupported = true;
        this.mIsIncomingCallGwsd = false;
        this.mIsIncomingCallDuringRttEmcGuard = false;
        if (imsCall != null && imsCall.getCallProfile() != null) {
            this.mIsIncomingCallGwsd = imsCall.getCallProfile().getCallExtraInt(EXTRA_IMS_GWSD) == 1;
        }
        fetchIsRttVideoSwitchSupported(phone);
    }

    public MtkImsPhoneConnection(Phone phone, String dialString, ImsPhoneCallTracker ct, ImsPhoneCall parent, boolean isEmergency, boolean isWpsCall, ImsPhone.ImsDialArgs dialArgs) {
        super(phone, dialString, ct, parent, isEmergency, isWpsCall, dialArgs);
        this.mConferenceParticipants = null;
        this.mWasMultiparty = false;
        this.mWasPreMultipartyHost = false;
        this.mCallIdBeforeDisconnected = -1;
        this.mIsRttVideoSwitchSupported = true;
        this.mIsIncomingCallGwsd = false;
        this.mIsIncomingCallDuringRttEmcGuard = false;
        if (PhoneNumberUtils.isUriNumber(dialString)) {
            this.mAddress = dialString;
            this.mPostDialString = "";
        }
        fetchIsRttVideoSwitchSupported(phone);
    }

    public MtkImsPhoneConnection(Phone phone, String[] participantsToDial, ImsPhoneCallTracker ct, ImsPhoneCall parent, boolean isEmergency) {
        super(phone, participantsToDial, ct, parent, isEmergency);
        this.mConferenceParticipants = null;
        this.mWasMultiparty = false;
        this.mWasPreMultipartyHost = false;
        this.mCallIdBeforeDisconnected = -1;
        this.mIsRttVideoSwitchSupported = true;
        this.mIsIncomingCallGwsd = false;
        this.mIsIncomingCallDuringRttEmcGuard = false;
        fetchIsRttVideoSwitchSupported(phone);
    }

    public synchronized boolean isIncomingCallGwsd() {
        Rlog.d(LOG_TAG, "isIncomingCallGwsd: " + this.mIsIncomingCallGwsd);
        return this.mIsIncomingCallGwsd;
    }

    public void hangup() throws CallStateException {
        if (this.mOwner != null && (this.mOwner instanceof MtkImsPhoneCallTracker)) {
            ((MtkImsPhoneCallTracker) this.mOwner).logDebugMessagesWithOpFormat("CC", "Hangup", this, "MtkImsphoneConnection.hangup");
        }
        super.hangup();
    }

    public boolean onDisconnect() {
        if (!this.mDisconnected) {
            this.mCallIdBeforeDisconnected = getCallId();
        }
        return super.onDisconnect();
    }

    public void onDisconnectConferenceParticipant(Uri endpoint) {
        if (this.mOwner != null && (this.mOwner instanceof MtkImsPhoneCallTracker)) {
            ((MtkImsPhoneCallTracker) this.mOwner).logDebugMessagesWithOpFormat("CC", "RemoveMember", this, " remove: " + MtkImsPhoneCallTracker.sensitiveEncode("" + endpoint));
        }
        super.onDisconnectConferenceParticipant(endpoint);
    }

    public boolean updateAddressDisplay(ImsCall imsCall) {
        boolean changed = super.updateAddressDisplay(imsCall);
        if (changed) {
            setConnectionAddressDisplay();
        }
        return changed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" state:" + getState());
        sb.append(" mParent:");
        sb.append(getParentCallName());
        return sb.toString();
    }

    int getCallId() {
        ImsCall call = getImsCall();
        if (call == null || call.getCallSession() == null) {
            return -1;
        }
        String callId = call.getCallSession().getCallId();
        if (callId == null) {
            Rlog.d(LOG_TAG, "Abnormal! Call Id = null");
            return -1;
        }
        return Integer.parseInt(callId);
    }

    int getCallIdBeforeDisconnected() {
        return this.mCallIdBeforeDisconnected;
    }

    public String getConferenceParticipantAddress(int index) {
        List<ConferenceParticipant> list = this.mConferenceParticipants;
        if (list == null) {
            Rlog.d(LOG_TAG, "getConferenceParticipantAddress(): no XML information");
            return "";
        }
        if (index < 0 || index >= list.size()) {
            Rlog.d(LOG_TAG, "getConferenceParticipantAddress(): invalid index: " + index);
            return "";
        }
        ConferenceParticipant participant = this.mConferenceParticipants.get(index);
        if (participant == null) {
            Rlog.d(LOG_TAG, "getConferenceParticipantAddress(): empty participant info");
            return "";
        }
        Uri userEntity = participant.getHandle();
        Rlog.d(LOG_TAG, "getConferenceParticipantAddress(): ret=" + MtkImsPhoneCallTracker.sensitiveEncode("" + userEntity));
        return userEntity.toString();
    }

    private void removeHostAddress() {
        ArrayList<Uri> hostAddresses = new ArrayList<>();
        ImsPhone imsPhone = this.mOwner.mPhone;
        if (imsPhone.getCurrentSubscriberUris() != null) {
            hostAddresses.addAll(new ArrayList<>(Arrays.asList(imsPhone.getCurrentSubscriberUris())));
            Uri[] conferenceHostAddress = new Uri[hostAddresses.size()];
            Uri[] conferenceHostAddress2 = (Uri[]) hostAddresses.toArray(conferenceHostAddress);
            ConferenceParticipant hostParticipant = null;
            Iterator<ConferenceParticipant> it = this.mConferenceParticipants.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ConferenceParticipant participant = it.next();
                if (isParticipantHost(conferenceHostAddress2, participant.getHandle())) {
                    hostParticipant = participant;
                    break;
                }
            }
            if (hostParticipant != null) {
                this.mConferenceParticipants.remove(hostParticipant);
            }
            Rlog.d(LOG_TAG, "remove host participant: " + hostParticipant);
            return;
        }
        Rlog.d(LOG_TAG, "remove host participant failed ");
    }

    private boolean isParticipantHost(Uri[] hostHandles, Uri handle) {
        if (hostHandles == null || hostHandles.length == 0 || handle == null) {
            Rlog.d(LOG_TAG, "isParticipantHost(N) : host or participant uri null");
            return false;
        }
        String number = extractPhoneNumber(handle);
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        for (Uri hostHandle : hostHandles) {
            if (hostHandle != null) {
                String hostNumber = extractPhoneNumber(hostHandle);
                boolean isHost = PhoneNumberUtils.compare(hostNumber, number);
                if (isHost) {
                    return true;
                }
            }
        }
        return false;
    }

    private String extractPhoneNumber(Uri handle) {
        String number = handle.getSchemeSpecificPart();
        String[] numberParts = number.split("[@;:]");
        if (numberParts.length == 0) {
            Rlog.d(LOG_TAG, "extractPhoneNumber(N) : no number in handle");
            return "";
        }
        return numberParts[0];
    }

    String getParentCallName() {
        if (this.mOwner == null) {
            return "Unknown";
        }
        if (this.mParent == this.mOwner.mForegroundCall) {
            return "Foreground Call";
        }
        if (this.mParent == this.mOwner.mBackgroundCall) {
            return "Background Call";
        }
        if (this.mParent == this.mOwner.mRingingCall) {
            return "Ringing Call";
        }
        if (this.mParent == this.mOwner.mHandoverCall) {
            return "Handover Call";
        }
        return "Abnormal";
    }

    public boolean isConfHostBeforeHandover() {
        return this.mWasPreMultipartyHost;
    }

    public boolean isMultipartyBeforeHandover() {
        return this.mWasMultiparty;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0017  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean isIncomingCallMultiparty() {
        /*
            r1 = this;
            monitor-enter(r1)
            com.android.ims.ImsCall r0 = r1.mImsCall     // Catch: java.lang.Throwable -> L1a
            if (r0 == 0) goto L17
            com.android.ims.ImsCall r0 = r1.mImsCall     // Catch: java.lang.Throwable -> L1a
            boolean r0 = r0 instanceof com.mediatek.ims.MtkImsCall     // Catch: java.lang.Throwable -> L1a
            if (r0 == 0) goto L17
            com.android.ims.ImsCall r0 = r1.mImsCall     // Catch: java.lang.Throwable -> L1a
            com.mediatek.ims.MtkImsCall r0 = (com.mediatek.ims.MtkImsCall) r0     // Catch: java.lang.Throwable -> L1a
            boolean r0 = r0.isIncomingCallMultiparty()     // Catch: java.lang.Throwable -> L1a
            if (r0 == 0) goto L17
            r0 = 1
            goto L18
        L17:
            r0 = 0
        L18:
            monitor-exit(r1)
            return r0
        L1a:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.imsphone.MtkImsPhoneConnection.isIncomingCallMultiparty():boolean");
    }

    public void updateConferenceParticipants(List<ConferenceParticipant> conferenceParticipants) {
        this.mConferenceParticipants = conferenceParticipants;
        removeHostAddress();
        super.updateConferenceParticipants(conferenceParticipants);
    }

    public static abstract class MtkListenerBase extends Connection.ListenerBase {
        public void onConferenceParticipantsInvited(boolean isSuccess) {
        }

        public void onConferenceConnectionsConfigured(ArrayList<Connection> radioConnections) {
        }

        public void onAddressDisplayChanged() {
        }

        public void onTextCapabilityChanged(int localCapability, int remoteCapability, int localTextStatus, int realRemoteTextCapability) {
        }

        public void onRedialEcc(boolean isNeedUserConfirm) {
        }
    }

    void notifyConferenceParticipantsInvited(boolean isSuccess) {
        for (MtkListenerBase mtkListenerBase : this.mListeners) {
            if (mtkListenerBase instanceof MtkListenerBase) {
                mtkListenerBase.onConferenceParticipantsInvited(isSuccess);
            }
        }
    }

    public void notifyConferenceConnectionsConfigured(ArrayList<Connection> radioConnections) {
        for (MtkListenerBase mtkListenerBase : this.mListeners) {
            if (mtkListenerBase instanceof MtkListenerBase) {
                mtkListenerBase.onConferenceConnectionsConfigured(radioConnections);
            }
        }
    }

    public void notifyRedialEcc(boolean isNeedUserConfirm) {
        for (MtkListenerBase mtkListenerBase : this.mListeners) {
            if (mtkListenerBase instanceof MtkListenerBase) {
                mtkListenerBase.onRedialEcc(isNeedUserConfirm);
            }
        }
    }

    private void setConnectionAddressDisplay() {
        for (MtkListenerBase mtkListenerBase : this.mListeners) {
            if (mtkListenerBase instanceof MtkListenerBase) {
                mtkListenerBase.onAddressDisplayChanged();
            }
        }
    }

    protected int applyVideoRingtoneCapabilities(ImsCallProfile remoteProfile, int capabilities) {
        int remoteCapabilities;
        int ringtoneState = remoteProfile.mMediaProfile.mVideoDirection;
        if (ringtoneState >= 0) {
            if ((ringtoneState & 2) == 2) {
                Rlog.d(LOG_TAG, "Add video ringtone capability");
                remoteCapabilities = addCapability(capabilities, 8192);
            } else {
                Rlog.d(LOG_TAG, "Remove video ringtone capability");
                remoteCapabilities = removeCapability(capabilities, 8192);
            }
            if ((ringtoneState & 1) == 1) {
                Rlog.d(LOG_TAG, "Add audio ringtone capability");
                return addCapability(remoteCapabilities, 4096);
            }
            Rlog.d(LOG_TAG, "Remove audio ringtone capability");
            return removeCapability(remoteCapabilities, 4096);
        }
        int remoteCapabilities2 = removeCapability(capabilities, 8192);
        return removeCapability(remoteCapabilities2, 4096);
    }

    protected boolean skipSwitchingCallToForeground() {
        if (this.mParent != this.mOwner.mHandoverCall) {
            Rlog.d(LOG_TAG, "update() - Switch Connection to foreground call:" + this);
            return false;
        }
        return true;
    }

    protected void switchCallToBackgroundIfNecessary() {
        if (this.mParent == this.mOwner.mForegroundCall && !this.mOwner.ignoreSwitchCallToBackground()) {
            Rlog.d(LOG_TAG, "update() - Switch Connection to background call:" + this);
            this.mParent.detach(this);
            this.mParent = this.mOwner.mBackgroundCall;
            this.mParent.attach(this);
        }
    }

    protected int callNumberPresentation(ImsCallProfile callProfile) {
        int nump = ImsCallProfile.OIRToPresentation(callProfile.getCallExtraInt("oir"));
        if (!this.mIsIncoming) {
            return 1;
        }
        return nump;
    }

    protected boolean needUpdateAddress(String address) {
        if (!equalsBaseDialString(this.mAddress, address)) {
            Rlog.d(LOG_TAG, "update address = " + MtkImsPhoneCallTracker.sensitiveEncode(address) + " isMpty = " + isMultiparty());
            if (!TextUtils.isEmpty(address)) {
                return true;
            }
            return false;
        }
        return false;
    }

    protected boolean allowedUpdateMOAddress() {
        return true;
    }

    public void setIncomingCallDuringRttEmcGuard(boolean isDuringRttGuard) {
        this.mIsIncomingCallDuringRttEmcGuard = isDuringRttGuard;
        Rlog.d(LOG_TAG, "setIncomingCallDuringRttEmcGuard: " + this.mIsIncomingCallDuringRttEmcGuard);
    }

    public boolean isIncomingCallDuringRttEmcGuard() {
        Rlog.d(LOG_TAG, "isIncomingCallDuringRttEmcGuard: " + this.mIsIncomingCallDuringRttEmcGuard);
        return this.mIsIncomingCallDuringRttEmcGuard;
    }

    public boolean updateMediaCapabilities(ImsCall imsCall) {
        boolean changed = super.updateMediaCapabilities(imsCall);
        if (changed) {
            Rlog.d(LOG_TAG, "updateMediaCapabilities capabilities = " + getConnectionCapabilities());
        }
        return changed;
    }

    protected int applyLocalCallCapabilities(ImsCallProfile localProfile, int capabilities) {
        int capabilities2 = super.applyLocalCallCapabilities(localProfile, capabilities);
        boolean isRttActive = isRttEnabledForCall();
        Rlog.d(LOG_TAG, "applyLocalCallCapabilities: isRttEnabledForCall=" + isRttActive + " mIsRttVideoSwitchSupported=" + this.mIsRttVideoSwitchSupported);
        if (isRttActive && !this.mIsRttVideoSwitchSupported) {
            return removeCapability(capabilities2, 4);
        }
        return capabilities2;
    }

    private void fetchIsRttVideoSwitchSupported(Phone phone) {
        CarrierConfigManager configMgr = (CarrierConfigManager) phone.getContext().getSystemService("carrier_config");
        PersistableBundle b = configMgr.getConfigForSubId(phone.getSubId());
        if (b != null) {
            this.mIsRttVideoSwitchSupported = b.getBoolean("rtt_supported_for_vt_bool");
        }
    }

    private boolean isTestSim() {
        boolean isTestSim = SystemProperties.get("vendor.gsm.sim.ril.testsim").equals(RadioCapabilitySwitchUtil.IMSI_READY) || SystemProperties.get("vendor.gsm.sim.ril.testsim.2").equals(RadioCapabilitySwitchUtil.IMSI_READY) || SystemProperties.get("vendor.gsm.sim.ril.testsim.3").equals(RadioCapabilitySwitchUtil.IMSI_READY) || SystemProperties.get("vendor.gsm.sim.ril.testsim.4").equals(RadioCapabilitySwitchUtil.IMSI_READY);
        Rlog.d(LOG_TAG, "isTestSim: " + isTestSim);
        return isTestSim;
    }
}
