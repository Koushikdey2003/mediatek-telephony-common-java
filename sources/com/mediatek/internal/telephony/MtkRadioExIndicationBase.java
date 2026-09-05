package com.mediatek.internal.telephony;

import android.hardware.radio.V1_0.SuppSvcNotification;
import com.android.internal.telephony.RIL;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V3_0.CfuStatusNotification;
import vendor.mediatek.hardware.mtkradioex.V3_0.CipherNotification;
import vendor.mediatek.hardware.mtkradioex.V3_0.CrssNotification;
import vendor.mediatek.hardware.mtkradioex.V3_0.DedicateDataCall;
import vendor.mediatek.hardware.mtkradioex.V3_0.EtwsNotification;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication;
import vendor.mediatek.hardware.mtkradioex.V3_0.IncomingCallNotification;
import vendor.mediatek.hardware.mtkradioex.V3_0.PcoDataAttachedInfo;
import vendor.mediatek.hardware.mtkradioex.V3_0.PlmnMvnoInfo;
import vendor.mediatek.hardware.mtkradioex.V3_0.SignalStrengthWithWcdmaEcio;
import vendor.mediatek.hardware.mtkradioex.V3_0.VsimOperationEvent;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExIndicationBase extends IMtkRadioExIndication.Stub {
    MtkRadioExIndicationBase(RIL ril) {
    }

    public void currentSignalStrengthWithWcdmaEcioInd(int indicationType, SignalStrengthWithWcdmaEcio signalStrength) {
    }

    public void cfuStatusNotify(int indicationType, CfuStatusNotification cfuStatus) {
    }

    public void incomingCallIndication(int indicationType, IncomingCallNotification inCallNotify) {
    }

    public void callAdditionalInfoInd(int indicationType, int ciType, ArrayList<String> info) {
    }

    public void cipherIndication(int indicationType, CipherNotification cipherNotify) {
    }

    public void suppSvcNotifyEx(int indicationType, SuppSvcNotification suppSvc) {
    }

    public void crssIndication(int indicationType, CrssNotification crssNotification) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void cdmaCallAccepted(int indicationType) {
    }

    public void eccNumIndication(int indicationType, String eccListWithCard, String eccListNoCard) {
    }

    public void responseCsNetworkStateChangeInd(int indicationType, ArrayList<String> state) {
    }

    public void responsePsNetworkStateChangeInd(int indicationType, ArrayList<Integer> state) {
    }

    public void responseNetworkEventInd(int indicationType, ArrayList<Integer> event) {
    }

    public void responseModulationInfoInd(int indicationType, ArrayList<Integer> data) {
    }

    public void responseInvalidSimInd(int indicationType, ArrayList<String> state) {
    }

    public void responseFemtocellInfo(int indicationType, ArrayList<String> info) {
    }

    public void responseLteNetworkInfo(int indicationType, int info) {
    }

    public void onMccMncChanged(int indicationType, String mccmnc) {
    }

    public void onVirtualSimOn(int indicationType, int simInserted) {
    }

    public void onVirtualSimOff(int indicationType, int simInserted) {
    }

    public void onVirtualSimStatusChanged(int indicationType, int simInserted) {
    }

    public void onImeiLock(int indicationType) {
    }

    public void onImsiRefreshDone(int indicationType) {
    }

    public void onCardDetectedInd(int indicationType) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void newEtwsInd(int indicationType, EtwsNotification etws) {
    }

    public void meSmsStorageFullInd(int indicationType) {
    }

    public void smsReadyInd(int indicationType) {
    }

    public void onPseudoCellInfoInd(int indicationType, ArrayList<Integer> info) {
    }

    public void eMBMSSessionStatusIndication(int indicationType, int status) {
    }

    public void eMBMSAtInfoIndication(int indicationType, String info) {
    }

    public void plmnChangedIndication(int indicationType, ArrayList<String> plmns) {
    }

    public void registrationSuspendedIndication(int indicationType, ArrayList<Integer> sessionIds) {
    }

    public void gmssRatChangedIndication(int indicationType, ArrayList<Integer> gmsss) {
    }

    public void worldModeChangedIndication(int indicationType, ArrayList<Integer> modes) {
    }

    public void resetAttachApnInd(int indicationType) {
    }

    public void mdChangedApnInd(int indicationType, int apnClassType) {
    }

    public void esnMeidChangeInd(int indicationType, String esnMeid) {
    }

    public void phbReadyNotification(int indicationType, int isPhbReady) {
    }

    public void bipProactiveCommand(int indicationType, String cmd) {
    }

    public void triggerOtaSP(int indicationType) {
    }

    public void onStkMenuReset(int indicationType) {
    }

    public void onMdDataRetryCountReset(int indicationType) {
    }

    public void onRemoveRestrictEutran(int indicationType) {
    }

    public void onSimHotSwapInd(int indicationType, int event, String info) {
    }

    public void onSimPowerChangedInd(int indicationType, ArrayList<Integer> info) {
    }

    public void onRsuSimLockEvent(int indicationType, int eventId) {
    }

    public void smlSlotLockInfoChangedInd(int indicationType, ArrayList<Integer> info) {
    }

    public void networkInfoInd(int indicationType, ArrayList<String> networkinfo) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void onSimMeLockEvent(int indicationType) {
    }

    public void pcoDataAfterAttached(int indicationType, PcoDataAttachedInfo pco) {
    }

    public void confSRVCC(int indicationType, ArrayList<Integer> callIds) {
    }

    public void onVsimEventIndication(int indicationType, VsimOperationEvent event) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void dedicatedBearerActivationInd(int indicationType, DedicateDataCall ddcResult) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void dedicatedBearerModificationInd(int indicationType, DedicateDataCall ddcResult) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void dedicatedBearerDeactivationInd(int indicationType, int ddcResult) {
    }

    public void oemHookRaw(int indicationType, ArrayList<Byte> data) {
    }

    public void onTxPowerIndication(int indicationType, ArrayList<Integer> txPower) {
    }

    public void onTxPowerStatusIndication(int indicationType, ArrayList<Integer> indPower) {
    }

    public void networkRejectCauseInd(int indicationType, ArrayList<Integer> data) {
    }

    public void dsbpStateChanged(int indicationType, int dsbpState) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void networkBandInfoInd(int indicationType, ArrayList<Integer> info) {
    }

    public void smsInfoExtInd(int indicationType, String info) {
    }

    public void onDsdaChangedInd(int indicationType, int mode) {
    }

    public void qualifiedNetworkTypesChangedInd(int indicationType, ArrayList<Integer> data) {
    }

    public void onCellularQualityChangedInd(int indicationType, ArrayList<Integer> indStgs) {
    }

    public void mobileDataUsageInd(int indicationType, ArrayList<Integer> data) {
    }

    public void onNwLimitInd(int indicationType, ArrayList<Integer> state) {
    }

    public void iccidChanged(int indicationType, String iccid) {
    }

    public void onPlmnDataInd(int indicationType, PlmnMvnoInfo plmnMvnoInfo) {
    }

    public void onRsuEvent(int type, int eventId, String eventString) {
    }

    public void toeInfoInd(int indicationType, String longName, String shortName, String numeric) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void sib16TimeInfoInd(int type, String data, long arg2) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void onNwCfgInfoInd(int type, boolean mimo, boolean qam_256, boolean qam_ul64) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExIndication
    public void onNwRrcStateInd(int type, int rat, int state) {
    }

    public void iwlanRegistrationStateInd(int type, int state) {
    }

    public void nrCaBandChangeInd(int type, ArrayList<Integer> bands) {
    }

    public void on5GUWInfoInd(int type, ArrayList<Integer> info) {
    }

    public void nrSysInfoInd(int indicationType, ArrayList<Integer> nrSysInfos) {
    }
}
