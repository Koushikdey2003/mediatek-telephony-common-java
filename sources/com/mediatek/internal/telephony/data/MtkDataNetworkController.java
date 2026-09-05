package com.mediatek.internal.telephony.data;

import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.data.ApnSetting;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataRetryManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.TelephonyNetworkRequest;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.MtkServiceStateTracker;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataNetworkController extends DataNetworkController {
    private static final int BASE = 100;
    private static final boolean DBG = true;
    private static final int EVALUATE_TYPE_DATA_NETWORK = 1;
    private static final int EVALUATE_TYPE_NETWORK_REQUEST = 0;
    private static final int EVENT_MD_DATA_RETRY_COUNT_RESET = 101;
    private static final int EVENT_ROAMING_TYPE_CHANGED = 102;
    private static final int EVENT_VOICE_CALL_ENDED = 18;
    private static final String LOG_TAG = "MtkDNC";
    private static final boolean VDBG = Build.IS_ENG;
    private static final boolean mEnableDataEnhance = MtkDataHelper.hasOperatorIaCapability();
    private boolean mIsNotifyDataAttached;
    private int mRilRat;
    private ServiceState mTurboSS;

    public MtkDataNetworkController(Phone phone, Looper looper) {
        super(phone, looper);
        this.mIsNotifyDataAttached = false;
        this.mTurboSS = null;
        this.mRilRat = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onRegisterAllEvents() {
        super.onRegisterAllEvents();
        ((MtkServiceStateTracker) this.mPhone.getServiceStateTracker()).registerForDataRoamingTypeChange(this, 102, null);
        this.mPhone.mCi.registerForMdDataRetryCountReset(this, 101, null);
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 17:
                if (getTurboSS() != null && this.mRilRat != getDataNetworkTypeWithoutTurboSS(1)) {
                    log("clear TurboSS.");
                    this.mTurboSS = null;
                }
                super.handleMessage(msg);
                break;
            case 101:
                log("EVENT_MD_DATA_RETRY_COUNT_RESET");
                this.mDataRetryManager.onReset(3);
                sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.NEW_REQUEST));
                break;
            case 102:
                log("EVENT_ROAMING_TYPE_CHANGED");
                onRoamingTypeChanged();
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    protected void onRemoveNetworkRequest(final TelephonyNetworkRequest request) {
        TelephonyNetworkRequest networkRequest;
        if (this.mPhone.getHalVersion().less(RIL.RADIO_HAL_VERSION_2_0) && (networkRequest = (TelephonyNetworkRequest) this.mAllNetworkRequestList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetworkController$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((TelephonyNetworkRequest) obj).equals(request);
            }
        }).findFirst().orElse(null)) != null) {
            final int capability = networkRequest.getApnTypeNetworkCapability();
            TelephonyNetworkRequest sameCapabilityRequest = (TelephonyNetworkRequest) this.mAllNetworkRequestList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetworkController$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return MtkDataNetworkController.lambda$onRemoveNetworkRequest$1(request, capability, (TelephonyNetworkRequest) obj);
                }
            }).findFirst().orElse(null);
            if (sameCapabilityRequest == null && (networkRequest.getAttachedNetwork() == null || (networkRequest.getAttachedNetwork() != null && !networkRequest.getAttachedNetwork().isConnected()))) {
                syncCapabilityReleaseRequest(capability);
            }
        }
        super.onRemoveNetworkRequest(request);
    }

    static /* synthetic */ boolean lambda$onRemoveNetworkRequest$1(TelephonyNetworkRequest request, int capability, TelephonyNetworkRequest r) {
        if (r.equals(request) || !r.hasCapability(capability)) {
            return false;
        }
        return DBG;
    }

    public DataNetwork getDataNetworkByInterface(final String interfaceName) {
        return (DataNetwork) this.mDataNetworkList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetworkController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MtkDataNetworkController.lambda$getDataNetworkByInterface$2((DataNetwork) obj);
            }
        }).filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetworkController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return interfaceName.equals(((DataNetwork) obj).getLinkProperties().getInterfaceName());
            }
        }).findFirst().orElse(null);
    }

    static /* synthetic */ boolean lambda$getDataNetworkByInterface$2(DataNetwork dataNetwork) {
        if (dataNetwork.isDisconnecting() || dataNetwork.isDisconnected()) {
            return false;
        }
        return DBG;
    }

    public void syncCapabilityReleaseRequest(int capability) {
        int profileId = getProfileID(capability);
        log("syncCapabilityReleaseRequest profileId=" + profileId);
        ((DataServiceManager) this.mDataServiceManagers.get(1)).deactivateDataCall(profileId * (-1), 1, (Message) null);
    }

    public boolean isAnyDataNetworkConnected() {
        if (this.mDataNetworkList.isEmpty()) {
            return false;
        }
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isConnected()) {
                return DBG;
            }
        }
        return false;
    }

    public boolean areAllDataDisconnectedExceptIms() {
        if (this.mDataNetworkList.isEmpty()) {
            return DBG;
        }
        boolean anyIms = this.mDataNetworkList.stream().anyMatch(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataNetworkController$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MtkDataNetworkController.lambda$areAllDataDisconnectedExceptIms$4((DataNetwork) obj);
            }
        });
        log(" has ims " + anyIms + " mDataNetworkList.size " + this.mDataNetworkList.size());
        if (anyIms && this.mDataNetworkList.size() == 1) {
            return DBG;
        }
        return false;
    }

    static /* synthetic */ boolean lambda$areAllDataDisconnectedExceptIms$4(DataNetwork network) {
        if (network.getNetworkCapabilities().hasCapability(4) && network.isConnected()) {
            return DBG;
        }
        return false;
    }

    public List<DataNetwork> getDataNetworkList() {
        return this.mDataNetworkList;
    }

    private void evaluateForCustomization(DataEvaluation evaluation, List<Integer> apnTypes, int transport, int evaluateType) {
        boolean simLockAllow = MtkDataHelper.isSimMeLockAllowed(this.mPhone.getPhoneId());
        if (!simLockAllow) {
            log("evaluateForCustomization, sim is in locked status!");
            evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.NOT_ALLOWED_BY_POLICY);
        }
        if (((MtkDataProfileManager) this.mDataProfileManager).isFdnConstraint()) {
            log("evaluateForCustomization, fdn enabled!");
            evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.MTK_FDN_ENABLED);
        }
        if (mEnableDataEnhance) {
            boolean apnLoaded = ((MtkDataProfileManager) this.mDataProfileManager).isApnLoaded();
            if (!apnLoaded) {
                log("evaluateForCustomization, apn not loaded yet!");
            } else {
                if (evaluation.contains(DataEvaluation.DataDisallowedReason.SIM_NOT_READY)) {
                    log("evaluateForCustomization, remove sim not ready");
                    evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.SIM_NOT_READY);
                }
                if (evaluation.contains(DataEvaluation.DataDisallowedReason.DATA_CONFIG_NOT_READY)) {
                    log("evaluateForCustomization, remove config not ready");
                    evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_CONFIG_NOT_READY);
                }
            }
        }
        if (evaluation.contains(DataEvaluation.DataDisallowedReason.DATA_DISABLED)) {
            if (((MtkDataConfigManager) this.mDataConfigManager).getInternatioalRoamingConfig() && this.mDataSettingsManager.isDataRoamingEnabled() && isInternationalRoaming()) {
                log("evaluateForCustomization, remove for international roaming");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
            }
            MtkIccCardConstants.VsimType type = MtkUiccController.getVsimCardType(this.mPhone.getPhoneId());
            if (apnTypes != null && apnTypes.contains(4096) && type.isUserDataAllowed()) {
                log("evaluateForCustomization, remove for vsim");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.DATA_DISABLED);
            }
        }
        if (evaluateType == 0) {
            if (!MtkDataHelper.getInstance().isDataAllowedForConcurrent(this.mPhone.getPhoneId()) && ((transport != 2 || !MtkDataHelper.getInstance().isWifiCallingEnabled(this.mPhone.getPhoneId())) && (apnTypes == null || !apnTypes.contains(64)))) {
                if (!evaluation.contains(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED)) {
                    evaluation.addDataDisallowedReason(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED);
                }
            } else if (evaluation.contains(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED)) {
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.CONCURRENT_VOICE_DATA_NOT_ALLOWED);
            }
        }
        if (evaluation.contains(DataEvaluation.DataDisallowedReason.DEFAULT_DATA_UNSELECTED) && apnTypes != null && apnTypes.contains(4096)) {
            log("evaluateForCustomization, remove for vsim default data unselected");
            evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.DEFAULT_DATA_UNSELECTED);
        }
        if (evaluation.contains(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED)) {
            if (apnTypes != null && apnTypes.contains(64)) {
                log("evaluateForCustomization, remove for ims apn");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
                return;
            }
            if (isDomesticRoaming() && ((MtkDataConfigManager) this.mDataConfigManager).getDomesticRoamingConfig()) {
                log("evaluateForCustomization, remove for domestic roaming config");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
                return;
            }
            if (apnTypes != null && MtkDataHelper.getInstance().getIwlanRegState(this.mPhone)) {
                Set<Integer> skipRoaming = ((MtkDataConfigManager) this.mDataConfigManager).getSkipRoamingOnIwlanApnTypes();
                Iterator<Integer> it = skipRoaming.iterator();
                while (it.hasNext()) {
                    int apn = it.next().intValue();
                    if (apnTypes.contains(Integer.valueOf(apn))) {
                        log("evaluateForCustomization, remove for iwlan apns config");
                        evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
                        return;
                    }
                }
            }
            MtkIccCardConstants.VsimType type2 = MtkUiccController.getVsimCardType(this.mPhone.getPhoneId());
            if (type2 == MtkIccCardConstants.VsimType.REMOTE_SIM) {
                log("evaluateForCustomization, remove for rsim");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
            } else if ((apnTypes == null || apnTypes.contains(4096)) && type2 == MtkIccCardConstants.VsimType.SOFT_AKA_SIM) {
                log("evaluateForCustomization, remove for aka and soft sim");
                evaluation.removeDataDisallowedReason(DataEvaluation.DataDisallowedReason.ROAMING_DISABLED);
            }
        }
    }

    protected void mtkEvaluateDataNetwork(DataEvaluation evaluation, DataNetwork dataNetwork, DataEvaluation.DataEvaluationReason reason, int transport) {
        DataNetworkController.NetworkRequestList<TelephonyNetworkRequest> nrl = dataNetwork.getAttachedNetworkRequestList();
        List<Integer> apnTypes = new ArrayList<>();
        for (TelephonyNetworkRequest networkRequest : nrl) {
            List<Integer> apns = (List) Arrays.stream(networkRequest.getCapabilities()).boxed().map(new MtkDataNetworkController$$ExternalSyntheticLambda2()).collect(Collectors.toList());
            apnTypes.addAll(apns);
        }
        evaluateForCustomization(evaluation, apnTypes, transport, 1);
    }

    protected void mtkEvaluateNetworkRequest(DataEvaluation evaluation, TelephonyNetworkRequest networkRequest, DataEvaluation.DataEvaluationReason reason, int transport) {
        List<Integer> apnTypes = (List) Arrays.stream(networkRequest.getCapabilities()).boxed().map(new MtkDataNetworkController$$ExternalSyntheticLambda2()).collect(Collectors.toList());
        evaluateForCustomization(evaluation, apnTypes, transport, 0);
        if (!evaluation.containsDisallowedReasons()) {
            evaluation.addDataAllowedReason(DataEvaluation.DataAllowedReason.NORMAL);
        }
    }

    private boolean isDomesticRoaming() {
        if (this.mPhone.getServiceState().getDataRoamingType() == 2) {
            return DBG;
        }
        return false;
    }

    private boolean isInternationalRoaming() {
        if (this.mPhone.getServiceState().getDataRoamingType() == 3) {
            return DBG;
        }
        return false;
    }

    private void onRoamingTypeChanged() {
        if (((MtkDataConfigManager) this.mDataConfigManager).getDomesticRoamingConfig() || ((MtkDataConfigManager) this.mDataConfigManager).getInternatioalRoamingConfig()) {
            sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
            sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
        }
    }

    public void onDsdaStateChanged() {
        log("onDsdaStateChanged");
        for (DataNetwork dataNetwork : this.mDataNetworkList) {
            if (dataNetwork.isConnected()) {
                ((MtkDataNetwork) dataNetwork).notifyDsdaStateChanged();
            }
        }
        if (MtkDataHelper.getInstance().isDataAllowedForConcurrent(this.mPhone.getPhoneId())) {
            sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_SERVICE_STATE_CHANGED));
        }
    }

    public void notifyMtkServiceStateChanged(ServiceState ss) {
        if (!this.mIsNotifyDataAttached && ss.getDataRegState() == 0 && !ss.getDataRoaming()) {
            log("notifyMtkServiceStateChanged, save TurboSS.");
            this.mIsNotifyDataAttached = DBG;
            this.mTurboSS = ss;
            this.mRilRat = getDataNetworkType(1);
        }
    }

    public int getDataNetworkTypeWithoutTurboSS(int transport) {
        return super.getDataNetworkType(transport);
    }

    protected int getDataNetworkType(int transport) {
        NetworkRegistrationInfo nrsTurbo;
        if (transport == 1 && getTurboSS() != null && (nrsTurbo = this.mTurboSS.getNetworkRegistrationInfo(2, transport)) != null) {
            int networkType = nrsTurbo.getAccessNetworkTechnology();
            log("getTurboSS network type: " + networkType);
            return networkType;
        }
        return super.getDataNetworkType(transport);
    }

    protected int getDataRegistrationState(ServiceState ss, int transport) {
        NetworkRegistrationInfo nrsTurbo;
        if (transport == 1 && getTurboSS() != null && (nrsTurbo = this.mTurboSS.getNetworkRegistrationInfo(2, transport)) != null) {
            int regState = nrsTurbo.getRegistrationState();
            log("getTurboSS reg state: " + regState);
            return regState;
        }
        return super.getDataRegistrationState(ss, transport);
    }

    public ServiceState getTurboSS() {
        if (mEnableDataEnhance) {
            return this.mTurboSS;
        }
        return null;
    }

    public void fdnStateChanged() {
        sendMessage(obtainMessage(16, DataEvaluation.DataEvaluationReason.DATA_PROFILES_CHANGED));
        sendMessage(obtainMessage(5, DataEvaluation.DataEvaluationReason.DATA_PROFILES_CHANGED));
    }

    protected boolean shouldIgnoreHandover(DataNetwork dataNetwork) {
        if (dataNetwork.getDataProfile().canSatisfy(12)) {
            return DBG;
        }
        return false;
    }

    protected void resetMdDataRetryCount(ApnSetting apnSetting) {
        if (apnSetting != null) {
            this.mPhone.mCi.resetMdDataRetryCount(apnSetting.getApnName(), null);
        }
    }

    protected long mtkGetInternetRetrySetupAfterDisconnectMillis(int cause) {
        for (DataRetryManager.DataSetupRetryRule retryRule : this.mDataConfigManager.getDataSetupRetryRules()) {
            if (!retryRule.getFailCauses().isEmpty() && !retryRule.getRetryIntervalsMillis().isEmpty() && !retryRule.getNetworkCapabilities().isEmpty() && retryRule.canBeMatched(12, cause)) {
                return ((Long) retryRule.getRetryIntervalsMillis().get(0)).longValue();
            }
        }
        return super.mtkGetInternetRetrySetupAfterDisconnectMillis(cause);
    }

    public void notifyVoiceCallEnd() {
        sendEmptyMessage(18);
    }

    private void logi(String s) {
        Rlog.i(LOG_TAG, this.mLogTag + ": " + s);
    }

    private void log(String s) {
        Rlog.d(LOG_TAG, this.mLogTag + ": " + s);
    }
}
