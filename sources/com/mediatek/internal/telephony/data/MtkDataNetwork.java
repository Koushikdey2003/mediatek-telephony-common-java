package com.mediatek.internal.telephony.data;

import android.R;
import android.net.INetworkManagementEventObserver;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.system.OsConstants;
import android.telephony.AccessNetworkConstants;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.util.SparseArray;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataRetryManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.TelephonyNetworkRequest;
import com.android.server.net.BaseNetworkObserver;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.function.Predicate;
import vendor.mediatek.hardware.netdagent.INetdagents;
import vendor.mediatek.hardware.netdagent.V1_0.INetdagent;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataNetwork extends DataNetwork {
    private static final int BASE = 100;
    private static final String CLASS_NAME_CONNECTEDSTATE = "ConnectedState";
    private static final String CLASS_NAME_CONNECTINGSTATE = "ConnectingState";
    private static final String CLASS_NAME_DEFAULTSTATE = "DefaultState";
    private static final boolean DBG = true;
    private static final int EVENT_FALLBACK_RETRY_CONNECTION = 102;
    private static final int EVENT_IPV4_ADDRESS_REMOVED = 103;
    private static final int EVENT_IPV6_ADDRESS_REMOVED = 104;
    private static final int EVENT_IPV6_ADDRESS_UPDATED = 105;
    private static final int EVENT_VOICE_CALL = 101;
    private static final String LOG_TAG = "MtkDN";
    private static final int RA_GET_IPV6_VALID_FAIL = -1000;
    private static final int RA_INITIAL_FAIL = -1;
    private static final int RA_REFRESH_FAIL = -2;
    private static final int TEARDOWN_REASON_BASE = 2000;
    private static final int TEARDOWN_REASON_DURING_DATA_CALL = 2006;
    private static final int TEARDOWN_REASON_RA_INITIAL_FAIL = 2002;
    private static final int TEARDOWN_REASON_RA_REFRESH_FAIL = 2004;
    private static final boolean VDBG = Build.IS_ENG;
    private INetworkManagementEventObserver mAlertObserver;
    private AddressInfo mGlobalV6AddrInfo;
    private String mInterfaceName;
    private INetworkManagementService mNetworkManager;
    private long mValid;

    public MtkDataNetwork(Phone phone, Looper looper, SparseArray<DataServiceManager> dataServiceManagers, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, int transport, DataEvaluation.DataAllowedReason dataAllowedReason, DataNetwork.DataNetworkCallback callback) {
        super(phone, looper, dataServiceManagers, dataProfile, networkRequestList, transport, dataAllowedReason, callback);
        this.mInterfaceName = null;
        this.mGlobalV6AddrInfo = null;
        this.mValid = -1L;
        this.mAlertObserver = new BaseNetworkObserver() { // from class: com.mediatek.internal.telephony.data.MtkDataNetwork.2
            public void addressRemoved(String iface, LinkAddress address) {
                int event = MtkDataNetwork.this.getEventByAddress(false, address);
                MtkDataNetwork.this.sendMessageForRa(event, iface, address);
            }

            public void addressUpdated(String iface, LinkAddress address) {
                int event = MtkDataNetwork.this.getEventByAddress(MtkDataNetwork.DBG, address);
                MtkDataNetwork.this.sendMessageForRa(event, iface, address);
            }
        };
    }

    protected void mtkReplaceStates() {
        this.mDefaultState = new MtkDefaultState();
        this.mConnectingState = new MtkConnectingState();
        this.mConnectedState = new MtkConnectedState();
    }

    protected class MtkDefaultState extends DataNetwork.DefaultState {
        protected MtkDefaultState() {
            super(MtkDataNetwork.this);
        }

        public void enter() {
            super.enter();
            MtkDataNetwork.this.registerForLegacyModem();
        }

        public void exit() {
            super.exit();
            MtkDataNetwork.this.unregisterForLegacyModem();
        }

        public String getName() {
            return MtkDataNetwork.CLASS_NAME_DEFAULTSTATE;
        }
    }

    protected class MtkConnectingState extends DataNetwork.ConnectingState {
        protected MtkConnectingState() {
            super(MtkDataNetwork.this);
        }

        public void enter() {
            super.enter();
        }

        public void exit() {
            super.exit();
        }

        public boolean processMessage(Message msg) {
            MtkDataNetwork.this.logDbg("event=" + MtkDataNetwork.eventToStringMtk(msg.what));
            switch (msg.what) {
                case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
                    int resultCode = msg.arg1;
                    MtkDataNetwork.this.onDeactivateResponse(resultCode);
                    return MtkDataNetwork.DBG;
                case 103:
                case MtkDataNetwork.EVENT_IPV6_ADDRESS_REMOVED /* 104 */:
                case MtkDataNetwork.EVENT_IPV6_ADDRESS_UPDATED /* 105 */:
                    MtkDataNetwork.this.deferMessage(msg);
                    return MtkDataNetwork.DBG;
                default:
                    return super.processMessage(msg);
            }
        }

        public String getName() {
            return MtkDataNetwork.CLASS_NAME_CONNECTINGSTATE;
        }
    }

    protected class MtkConnectedState extends DataNetwork.ConnectedState {
        protected MtkConnectedState() {
            super(MtkDataNetwork.this);
        }

        public void enter() {
            super.enter();
            if (((MtkDataConfigManager) MtkDataNetwork.this.mDataConfigManager).getVolteIotFirewallConfig()) {
                MtkDataNetwork.this.enableVolteIotFirewall(MtkDataNetwork.DBG);
            }
        }

        public void exit() {
            super.exit();
            if (((MtkDataConfigManager) MtkDataNetwork.this.mDataConfigManager).getVolteIotFirewallConfig()) {
                MtkDataNetwork.this.enableVolteIotFirewall(false);
            }
        }

        public boolean processMessage(Message msg) {
            MtkDataNetwork.this.logDbg("event=" + MtkDataNetwork.eventToStringMtk(msg.what));
            switch (msg.what) {
                case 6:
                    int resultCode = msg.arg1;
                    DataCallResponse dataCallResponse = msg.getData().getParcelable("data_call_response");
                    MtkDataNetwork.this.onSetupResponse(resultCode, dataCallResponse);
                    return MtkDataNetwork.DBG;
                case 101:
                    MtkDataNetwork.this.updateSuspendState();
                    return MtkDataNetwork.DBG;
                case 102:
                    MtkDataNetwork.this.setupData();
                    return MtkDataNetwork.DBG;
                case 103:
                    return MtkDataNetwork.DBG;
                case MtkDataNetwork.EVENT_IPV6_ADDRESS_REMOVED /* 104 */:
                    AddressInfo addrV6Info = (AddressInfo) msg.obj;
                    MtkDataNetwork.this.logDbg("addrV6Info=" + addrV6Info);
                    if (addrV6Info != null) {
                        if (MtkDataNetwork.this.mInterfaceName != null && MtkDataNetwork.this.mInterfaceName.equals(addrV6Info.mIntfName)) {
                            String strAddress = addrV6Info.mLinkAddr.getAddress().getHostAddress();
                            MtkDataNetwork.this.logDbg("strAddress=" + strAddress);
                            if (strAddress.equalsIgnoreCase("FE80::5A:5A:5A:23")) {
                                MtkDataNetwork.this.mValid = -1L;
                            } else if (strAddress.equalsIgnoreCase("FE80::5A:5A:5A:22")) {
                                MtkDataNetwork.this.mValid = -2L;
                            } else {
                                MtkDataNetwork.this.mValid = -1000L;
                            }
                            if (MtkDataNetwork.this.mValid == -1 || MtkDataNetwork.this.mValid == -2) {
                                MtkDataNetwork.this.onAddressRemoved();
                            }
                        }
                        if (MtkDataNetwork.this.mGlobalV6AddrInfo != null && MtkDataNetwork.this.mGlobalV6AddrInfo.mIntfName.equals(addrV6Info.mIntfName)) {
                            MtkDataNetwork.this.mGlobalV6AddrInfo = null;
                            return MtkDataNetwork.DBG;
                        }
                        return MtkDataNetwork.DBG;
                    }
                    return MtkDataNetwork.DBG;
                case MtkDataNetwork.EVENT_IPV6_ADDRESS_UPDATED /* 105 */:
                    AddressInfo addrV6Info2 = (AddressInfo) msg.obj;
                    MtkDataNetwork.this.logDbg("addrV6Info=" + addrV6Info2);
                    if (MtkDataNetwork.this.mInterfaceName != null && MtkDataNetwork.this.mInterfaceName.equals(addrV6Info2.mIntfName)) {
                        int scope = addrV6Info2.mLinkAddr.getScope();
                        int flag = addrV6Info2.mLinkAddr.getFlags();
                        MtkDataNetwork.this.logDbg("EVENT_IPV6_ADDRESS_UPDATED scope=" + scope + " flag=" + flag);
                        if (OsConstants.RT_SCOPE_UNIVERSE == scope && (flag & 1) != OsConstants.IFA_F_TEMPORARY) {
                            MtkDataNetwork.this.mGlobalV6AddrInfo = addrV6Info2;
                            MtkDataNetwork.this.mNetworkAgent.sendLinkProperties(MtkDataNetwork.this.replaceGlobalIpv6Address());
                            MtkDataNetwork.this.logDbg("EVENT_IPV6_ADDRESS_UPDATED notify ipv6 address update");
                            return MtkDataNetwork.DBG;
                        }
                        MtkDataNetwork.this.logDbg("EVENT_IPV6_ADDRESS_UPDATED not ipv6 address update");
                        return MtkDataNetwork.DBG;
                    }
                    return MtkDataNetwork.DBG;
                default:
                    return super.processMessage(msg);
            }
        }

        public String getName() {
            return MtkDataNetwork.CLASS_NAME_CONNECTEDSTATE;
        }
    }

    protected void onDetachNetworkRequest(TelephonyNetworkRequest networkRequest, boolean shouldRetry) {
        super.onDetachNetworkRequest(networkRequest, shouldRetry);
        if (this.mPhone.getHalVersion().less(RIL.RADIO_HAL_VERSION_2_0) && !this.mAttachedNetworkRequestList.isEmpty()) {
            final int capability = networkRequest.getApnTypeNetworkCapability();
            TelephonyNetworkRequest sameCapabilityRequest = (TelephonyNetworkRequest) this.mAttachedNetworkRequestList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetwork$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((TelephonyNetworkRequest) obj).hasCapability(capability);
                }
            }).findFirst().orElse(null);
            if (sameCapabilityRequest == null) {
                ((MtkDataNetworkController) this.mDataNetworkController).syncCapabilityReleaseRequest(capability);
            }
        }
    }

    protected void onTearDown(int reason) {
        logl("onTearDown: reason=" + tearDownReasonToString(reason));
        if (reason == 1 && isConnected() && (this.mNetworkCapabilities.hasCapability(4) || this.mNetworkCapabilities.hasCapability(12))) {
            this.mDataNetworkCallback.onTrackNetworkUnwanted(this);
        }
        int customizedReason = replaceTearDownReason(reason);
        ((DataServiceManager) this.mDataServiceManagers.get(this.mTransport)).deactivateDataCall(this.mCid.get(this.mTransport), customizedReason, obtainMessage(19));
        this.mDataCallSessionStats.setDeactivateDataCallReason(reason);
        this.mInvokedDataDeactivation = DBG;
    }

    public boolean startHandover(int targetTransport, DataRetryManager.DataHandoverRetryEntry retryEntry) {
        boolean ret = super.startHandover(targetTransport, retryEntry);
        MtkDataHelper.getInstance().onHandoverStart(this.mPhone.getPhoneId());
        return ret;
    }

    protected int mtkGetCustomizedMtu(ApnSetting apn, int type) {
        int apnMtuV6;
        int apnMtuV4;
        int testSimMtu;
        String strEmMtu = SystemProperties.get("persist.vendor.radio.mobile.mtu", "");
        if (strEmMtu != null && strEmMtu.length() > 0) {
            int emMtu = 0;
            try {
                emMtu = Integer.parseInt(strEmMtu.substring(0, strEmMtu.length() < 4 ? strEmMtu.length() : 4));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                logErr("emMtu is invalid");
            }
            if (emMtu > 0) {
                if (type == 0) {
                    logDbg("MTU set by EM, emMtu: " + emMtu);
                }
                return emMtu;
            }
        }
        int phoneId = this.mPhone.getPhoneId();
        if (SubscriptionManager.isValidPhoneId(phoneId) && MtkDataHelper.getInstance().isTestIccCard(phoneId) && (testSimMtu = ((MtkDataConfigManager) this.mDataConfigManager).getMtuForTestSim()) > 0) {
            if (type == 0) {
                logDbg("MTU set by test SIM MTU configuration: " + testSimMtu);
            }
            return testSimMtu;
        }
        if (apn != null && type == 0 && (apnMtuV4 = apn.getMtuV4()) > 0) {
            logDbg("MTU-V4 set by apn configuration: " + apnMtuV4);
            return apnMtuV4;
        }
        if (apn != null && type == 1 && (apnMtuV6 = apn.getMtuV6()) > 0) {
            logDbg("MTU-V6 set by apn configuration: " + apnMtuV6);
            return apnMtuV6;
        }
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
        MtkDataProfileManager mtkDpm = (MtkDataProfileManager) mtkDnc.getDataProfileManager();
        int mtuTurbo = mtkDpm.getMtu();
        if (mtuTurbo > 0) {
            if (type == 0) {
                logDbg("MTU set by 'self-created' config resource to: " + mtuTurbo);
            }
            return mtuTurbo;
        }
        int mtuResource = this.mPhone.getContext().getResources().getInteger(R.integer.config_letterboxActivityCornersRadius);
        if (mtuResource > 0) {
            if (type == 0) {
                logDbg("MTU set by config resource to: " + mtuResource);
            }
            return mtuResource;
        }
        return this.mDataConfigManager.getDefaultMtu();
    }

    public void notifyVoiceCallEvent() {
        sendMessage(101);
    }

    public void notifyDsdaStateChanged() {
        int callState = MtkDataHelper.getInstance().getCallState();
        if (callState == 1) {
            sendMessage(101);
        }
    }

    private boolean hasImsOrEmergencyCapability() {
        if (this.mNetworkCapabilities == null) {
            return false;
        }
        if (this.mNetworkCapabilities.hasCapability(10) || this.mNetworkCapabilities.hasCapability(4)) {
            return DBG;
        }
        return false;
    }

    protected void updateSuspendState() {
        NetworkRegistrationInfo nri;
        boolean isWifiCalling;
        if (isConnecting() || isDisconnected()) {
            return;
        }
        boolean newSuspendedState = false;
        ServiceState turboSS = ((MtkDataNetworkController) this.mDataNetworkController).getTurboSS();
        boolean z = DBG;
        if (turboSS != null && this.mTransport == 1) {
            nri = ((MtkDataNetworkController) this.mDataNetworkController).getTurboSS().getNetworkRegistrationInfo(2, this.mTransport);
        } else {
            nri = this.mPhone.getServiceStateTracker().getServiceState().getNetworkRegistrationInfo(2, this.mTransport);
        }
        if (nri == null) {
            logErr("Can't get network registration info for " + AccessNetworkConstants.transportTypeToString(getTransport()));
            return;
        }
        if (nri.getRegistrationState() != 1 && nri.getRegistrationState() != 5 && !hasImsOrEmergencyCapability() && !this.mNetworkCapabilities.hasCapability(30)) {
            newSuspendedState = DBG;
        }
        if (!newSuspendedState) {
            boolean isConcurrent = DBG;
            int callState = MtkDataHelper.getInstance().getCallState();
            if (callState == 1) {
                isConcurrent = MtkDataHelper.getInstance().isDataAllowedForConcurrent(this.mPhone.getPhoneId());
            }
            boolean isImsOrEmergency = hasImsOrEmergencyCapability();
            if (callState != 1) {
                isWifiCalling = false;
            } else {
                isWifiCalling = MtkDataHelper.getInstance().isWifiCallingEnabled();
            }
            logDbg("isSuspended: callState = " + callState + ", isConcurrent = " + isConcurrent + ", isImsOrEmergency = " + isImsOrEmergency + ", isWifiCalling = " + isWifiCalling);
            if (callState != 1 || isConcurrent || isImsOrEmergency || isWifiCalling) {
                z = false;
            }
            newSuspendedState = z;
        }
        boolean isConcurrent2 = this.mSuspended;
        if (isConcurrent2 != newSuspendedState) {
            this.mSuspended = newSuspendedState;
            logl("Network becomes " + (this.mSuspended ? "suspended" : "unsuspended"));
            updateNetworkCapabilities();
            if (!this.mSuspended) {
                this.mNetworkAgent.markConnected();
            }
            notifyPreciseDataConnectionState();
            this.mDataNetworkCallback.invokeFromExecutor(new Runnable() { // from class: com.mediatek.internal.telephony.data.MtkDataNetwork$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateSuspendState$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSuspendState$1() {
        this.mDataNetworkCallback.onSuspendedStateChanged(this, this.mSuspended);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public INetdagents getNetdagentAidlInstance() {
        INetdagents netdagentInstance = INetdagents.Stub.asInterface(ServiceManager.getService(INetdagents.DESCRIPTOR + "/default"));
        if (netdagentInstance == null) {
            logDbg("aidl: getNetdagentInstance, get" + INetdagents.DESCRIPTOR + "aidl service failed");
        }
        return netdagentInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableVolteIotFirewall(final boolean enable) {
        String simType;
        final String ifc = getLinkProperties().getInterfaceName();
        if (ifc == null || ifc.length() == 0) {
            logDbg("enableVolteIotFirewall, interface name is null");
            return;
        }
        boolean isImsApn = false;
        if (this.mDataProfile.getApnSetting() != null) {
            Iterator it = this.mDataProfile.getApnSetting().getApnTypes().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int apnType = ((Integer) it.next()).intValue();
                if (apnType == 64) {
                    isImsApn = DBG;
                    break;
                }
            }
        }
        if (!isImsApn) {
            logDbg("enableVolteIotFirewall, not ims apn");
            return;
        }
        logDbg("enableVolteIotFirewall, ifc:" + ifc + ", " + enable);
        String simType2 = SystemProperties.get("vendor.gsm.sim.ril.testsim");
        if ((simType2 == null || !simType2.equals(RadioCapabilitySwitchUtil.IMSI_READY)) && ((simType = SystemProperties.get("vendor.gsm.sim.ril.testsim.2")) == null || !simType.equals(RadioCapabilitySwitchUtil.IMSI_READY))) {
            logDbg("enableVolteIotFirewall, not TEST SIM");
        } else {
            Thread thread = new Thread("enableVolteIotFirewall") { // from class: com.mediatek.internal.telephony.data.MtkDataNetwork.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String cmd;
                    try {
                        if (enable) {
                            cmd = String.format("netdagent firewall set_volte_nsiot_firewall %s", ifc);
                        } else {
                            cmd = String.format("netdagent firewall clear_volte_nsiot_firewall %s", ifc);
                        }
                        MtkDataNetwork.this.logDbg("cmd:" + cmd);
                        INetdagents netdagentAidl = MtkDataNetwork.this.getNetdagentAidlInstance();
                        if (netdagentAidl == null) {
                            MtkDataNetwork.this.logDbg("aidl: netdagentAidl is null");
                            INetdagent agent = INetdagent.getService();
                            if (agent == null) {
                                MtkDataNetwork.this.logDbg("agnent is null");
                                return;
                            } else {
                                agent.dispatchNetdagentCmd(cmd);
                                return;
                            }
                        }
                        MtkDataNetwork.this.logDbg("aidl: netdagentAidl setIotFirewall");
                        netdagentAidl.dispatchNetdagentCmd(cmd);
                    } catch (Exception e) {
                        MtkDataNetwork.this.logDbg("enableVolteIotFirewall:" + e);
                    }
                }
            };
            thread.start();
        }
    }

    protected boolean mtkSkipDataStallAlarm(String apn) {
        MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
        MtkDataConfigManager mtkDcm = (MtkDataConfigManager) mtkDnc.getDataConfigManager();
        return mtkDcm.shouldSkipDataStall(apn);
    }

    protected int getDataNetworkType(int transport) {
        NetworkRegistrationInfo nrsTurbo;
        if (((MtkDataNetworkController) this.mDataNetworkController).getTurboSS() != null && transport == 1 && (nrsTurbo = ((MtkDataNetworkController) this.mDataNetworkController).getTurboSS().getNetworkRegistrationInfo(2, transport)) != null) {
            int networkType = nrsTurbo.getAccessNetworkTechnology();
            logDbg("getTurboSS network type: " + networkType);
            return networkType;
        }
        return super.getDataNetworkType(transport);
    }

    protected void mtkUpdateNetworkCapabilities(NetworkCapabilities.Builder builder) {
        NetworkCapabilities nc = builder.build();
        if (nc == null || !nc.hasCapability(12)) {
            return;
        }
        boolean hasVsimReq = false;
        Iterator it = this.mAttachedNetworkRequestList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TelephonyNetworkRequest networkRequest = (TelephonyNetworkRequest) it.next();
            if (networkRequest.hasCapability(30)) {
                hasVsimReq = DBG;
                break;
            }
        }
        if (hasVsimReq && !MtkDataHelper.isPreferredDataPhone(this.mPhone) && !MtkDataHelper.isDefaultDataPhone(this.mPhone)) {
            logDbg("remove INTERNET capability for VSIM");
            builder.removeCapability(12);
        }
    }

    private int replaceTearDownReason(int reason) {
        boolean isDataDuringCall = MtkPhoneSwitcher.getInstance().getTempDataSwitchState();
        logDbg("replaceTearDownReason reason=" + reason + " data during call=" + isDataDuringCall);
        if (reason == 30 && isDataDuringCall && this.mDataNetworkController.getDataSettingsManager().isDataEnabled() && this.mNetworkCapabilities.hasCapability(12) && MtkDataHelper.isDefaultDataPhone(this.mPhone)) {
            return 2006;
        }
        if (reason == 5) {
            long j = this.mValid;
            if (j == -1) {
                this.mValid = -1L;
                return 2002;
            }
            if (j == -2) {
                this.mValid = -1L;
                return 2004;
            }
        }
        return reason == 3 ? 2 : 1;
    }

    private class AddressInfo {
        String mIntfName;
        LinkAddress mLinkAddr;

        public AddressInfo(String intfName, LinkAddress linkAddr) {
            this.mIntfName = intfName;
            this.mLinkAddr = linkAddr;
        }

        public String toString() {
            return "interfaceName=" + this.mIntfName + "/" + this.mLinkAddr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getEventByAddress(boolean updated, LinkAddress la) {
        InetAddress addr = la.getAddress();
        if (!updated) {
            if (addr instanceof Inet6Address) {
                return EVENT_IPV6_ADDRESS_REMOVED;
            }
            if (addr instanceof Inet4Address) {
                return 103;
            }
            logErr("remove unknown address type, la=" + la);
            return -1;
        }
        if (addr instanceof Inet6Address) {
            return EVENT_IPV6_ADDRESS_UPDATED;
        }
        logErr("unknown address type, la=" + la);
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessageForRa(int event, String iface, LinkAddress address) {
        if (event < 0) {
            logErr("sendMessageForRa skip");
            return;
        }
        AddressInfo addrInfo = new AddressInfo(iface, address);
        logDbg("sendMessageForRa event=" + event + " addressInfo=" + addrInfo);
        sendMessage(obtainMessage(event, addrInfo));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerForLegacyModem() {
        if (MtkDataHelper.hasRaCapability()) {
            return;
        }
        logDbg("registerForLegacyModem");
        INetworkManagementService iNetworkManagementServiceAsInterface = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
        this.mNetworkManager = iNetworkManagementServiceAsInterface;
        try {
            iNetworkManagementServiceAsInterface.registerObserver(this.mAlertObserver);
            logDbg("registerNetworkAlertObserver");
        } catch (RemoteException e) {
            logErr("registerNetworkAlertObserver error");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterForLegacyModem() {
        if (MtkDataHelper.hasRaCapability()) {
            return;
        }
        logDbg("unregisterForLegacyModem");
        this.mInterfaceName = null;
        this.mGlobalV6AddrInfo = null;
        INetworkManagementService iNetworkManagementService = this.mNetworkManager;
        if (iNetworkManagementService != null) {
            try {
                iNetworkManagementService.unregisterObserver(this.mAlertObserver);
                logDbg("unregisterNetworkAlertObserver");
            } catch (RemoteException e) {
                logErr("unregisterNetworkAlertObserver error");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LinkProperties replaceGlobalIpv6Address() {
        LinkProperties lp = new LinkProperties(getLinkProperties());
        if (this.mGlobalV6AddrInfo == null) {
            return lp;
        }
        Iterator<LinkAddress> it = lp.getLinkAddresses().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            LinkAddress la = it.next();
            if (la.getAddress() instanceof Inet6Address) {
                lp.removeLinkAddress(la);
                break;
            }
        }
        lp.addLinkAddress(this.mGlobalV6AddrInfo.mLinkAddr);
        return lp;
    }

    private boolean isIpv4Connected() {
        LinkProperties lp = new LinkProperties(getLinkProperties());
        for (InetAddress addr : lp.getAddresses()) {
            if (addr instanceof Inet4Address) {
                Inet4Address i4addr = (Inet4Address) addr;
                if (!i4addr.isAnyLocalAddress() && !i4addr.isLinkLocalAddress() && !i4addr.isLoopbackAddress() && !i4addr.isMulticastAddress()) {
                    return DBG;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAddressRemoved() {
        if ((1 == this.mDataProfile.getProtocolType() || 2 == this.mDataProfile.getProtocolType()) && !isIpv4Connected()) {
            logDbg("onAddressRemoved: IPv6 RA failed and IPv4 is not connected");
            if (isConnected()) {
                onTearDown(5);
            }
        }
    }

    protected boolean mtkHandleSetupResponse(int failCause, DataCallResponse response) {
        if (MtkDataHelper.hasRaCapability()) {
            return false;
        }
        logDbg("mtkHandleSetupResponse: failCause=" + failCause);
        if (failCause == RA_GET_IPV6_VALID_FAIL) {
            this.mFailCause = 0;
            if (isConnected()) {
                long retryDelayMillis = response != null ? response.getRetryDurationMillis() : -1L;
                if (retryDelayMillis > 0) {
                    logDbg("mtkHandleSetupResponse: retryDelayMillis=" + retryDelayMillis);
                    sendMessageDelayed(102, retryDelayMillis);
                    return DBG;
                }
                return DBG;
            }
            deferMessage(obtainMessage(102));
        }
        if (this.mFailCause == 0) {
            this.mInterfaceName = response != null ? response.getInterfaceName() : null;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String eventToStringMtk(int event) {
        if (event < 100) {
            return eventToString(event);
        }
        switch (event) {
            case 101:
                return "EVENT_VOICE_CALL";
            case 102:
                return "EVENT_FALLBACK_RETRY_CONNECTION";
            case 103:
                return "EVENT_IPV4_ADDRESS_REMOVED";
            case EVENT_IPV6_ADDRESS_REMOVED /* 104 */:
                return "EVENT_IPV6_ADDRESS_REMOVED";
            case EVENT_IPV6_ADDRESS_UPDATED /* 105 */:
                return "EVENT_IPV6_ADDRESS_UPDATED";
            default:
                return "Unknown(" + event + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDbg(String s) {
        Rlog.d(LOG_TAG, this.mLogTag + ": " + (getCurrentState() != null ? getCurrentState().getName() + ": " : "") + s);
    }

    private void logErr(String s) {
        Rlog.e(LOG_TAG, this.mLogTag + ": " + (getCurrentState() != null ? getCurrentState().getName() + ": " : "") + s);
    }
}
