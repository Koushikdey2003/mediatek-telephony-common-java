package com.mediatek.internal.telephony;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.os.RemoteException;
import com.android.internal.telephony.RIL;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V3_0.BandModeInfo;
import vendor.mediatek.hardware.mtkradioex.V3_0.CallForwardInfoEx;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse;
import vendor.mediatek.hardware.mtkradioex.V3_0.Lte1xRttCellInfo;
import vendor.mediatek.hardware.mtkradioex.V3_0.LteData;
import vendor.mediatek.hardware.mtkradioex.V3_0.OperatorInfoWithAct;
import vendor.mediatek.hardware.mtkradioex.V3_0.PhbEntryExt;
import vendor.mediatek.hardware.mtkradioex.V3_0.PhbEntryStructure;
import vendor.mediatek.hardware.mtkradioex.V3_0.PhbMemStorageResponse;
import vendor.mediatek.hardware.mtkradioex.V3_0.RsuResponseInfo;
import vendor.mediatek.hardware.mtkradioex.V3_0.SignalStrengthWithWcdmaEcio;
import vendor.mediatek.hardware.mtkradioex.V3_0.SmsMemStatus;
import vendor.mediatek.hardware.mtkradioex.V3_0.SmsParams;
import vendor.mediatek.hardware.mtkradioex.V3_0.VsimEvent;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExResponseBase extends IMtkRadioExResponse.Stub {
    public MtkRadioExResponseBase(RIL ril) {
    }

    public void acknowledgeRequest(int serial) {
    }

    public void setClipResponse(RadioResponseInfo responseInfo) {
    }

    public void getColpResponse(RadioResponseInfo responseInfo, int n, int m) {
    }

    public void getColrResponse(RadioResponseInfo responseInfo, int status) {
    }

    public void sendCnapResponse(RadioResponseInfo responseInfo, int n, int m) {
    }

    public void setColpResponse(RadioResponseInfo responseInfo) {
    }

    public void setColrResponse(RadioResponseInfo responseInfo) {
    }

    public void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo responseInfo, ArrayList<CallForwardInfoEx> callForwardInfoExs) {
    }

    public void setCallForwardInTimeSlotResponse(RadioResponseInfo responseInfo) {
    }

    public void runGbaAuthenticationResponse(RadioResponseInfo responseInfo, ArrayList<String> resList) {
    }

    public void sendOemRilRequestRawResponse(RadioResponseInfo responseInfo, ArrayList<Byte> var2) {
    }

    public void setTrmResponse(RadioResponseInfo responseInfo) {
    }

    public void getATRResponse(RadioResponseInfo info, String response) {
    }

    public void getIccidResponse(RadioResponseInfo info, String response) {
    }

    public void setSimPowerResponse(RadioResponseInfo info) {
    }

    public void activateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) {
    }

    public void deactivateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) {
    }

    public void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo info, int simPowerOnOffStatus) {
    }

    public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo responseInfo) {
    }

    public void getAvailableNetworksWithActResponse(RadioResponseInfo responseInfo, ArrayList<OperatorInfoWithAct> networkInfos) {
    }

    public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo responseInfo, SignalStrengthWithWcdmaEcio signalStrength) {
    }

    public void cancelAvailableNetworksResponse(RadioResponseInfo responseInfo) {
    }

    public void cfgA2offsetResponse(RadioResponseInfo responseInfo) {
    }

    public void cfgB1offsetResponse(RadioResponseInfo responseInfo) {
    }

    public void enableSCGfailureResponse(RadioResponseInfo responseInfo) {
    }

    public void setNROptionResponse(RadioResponseInfo responseInfo) {
    }

    public void setTxPowerResponse(RadioResponseInfo responseInfo) {
    }

    public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) {
    }

    public void setSearchRatResponse(RadioResponseInfo responseInfo) {
    }

    public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) {
    }

    public void setModemPowerResponse(RadioResponseInfo responseInfo) {
    }

    public void getSmsParametersResponse(RadioResponseInfo responseInfo, SmsParams params) {
    }

    public void setSmsParametersResponse(RadioResponseInfo responseInfo) {
    }

    public void setEtwsResponse(RadioResponseInfo responseInfo) {
    }

    public void removeCbMsgResponse(RadioResponseInfo responseInfo) {
    }

    public void getSmsMemStatusResponse(RadioResponseInfo responseInfo, SmsMemStatus params) {
    }

    public void setGsmBroadcastLangsResponse(RadioResponseInfo responseInfo) {
    }

    public void getGsmBroadcastLangsResponse(RadioResponseInfo responseInfo, String langs) {
    }

    public void getGsmBroadcastActivationRsp(RadioResponseInfo responseInfo, int activation) {
    }

    public void sendEmbmsAtCommandResponse(RadioResponseInfo responseInfo, String result) {
    }

    public void hangupAllResponse(RadioResponseInfo responseInfo) throws RemoteException {
    }

    public void setCallIndicationResponse(RadioResponseInfo responseInfo) {
    }

    public void setVoicePreferStatusResponse(RadioResponseInfo responseInfo) {
    }

    public void setEccNumResponse(RadioResponseInfo responseInfo) {
    }

    public void getEccNumResponse(RadioResponseInfo responseInfo) {
    }

    public void setEccModeResponse(RadioResponseInfo responseInfo) {
    }

    public void eccPreferredRatResponse(RadioResponseInfo responseInfo) {
    }

    public void setCallSubAddressResponse(RadioResponseInfo responseInfo) {
    }

    public void getCallSubAddressResponse(RadioResponseInfo info, int enable) {
    }

    public void setApcModeResponse(RadioResponseInfo responseInfo) {
    }

    public void getApcInfoResponse(RadioResponseInfo responseInfo, ArrayList<Integer> cellInfo) {
    }

    public void triggerModeSwitchByEccResponse(RadioResponseInfo responseInfo) {
    }

    public void getSmsRuimMemoryStatusResponse(RadioResponseInfo responseInfo, SmsMemStatus memStatus) {
    }

    public void setFdModeResponse(RadioResponseInfo responseInfo) {
    }

    public void setResumeRegistrationResponse(RadioResponseInfo responseInfo) {
    }

    public void modifyModemTypeResponse(RadioResponseInfo responseInfo, int applyType) {
    }

    public void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo responseInfo) {
    }

    public void queryPhbStorageInfoResponse(RadioResponseInfo responseInfo, ArrayList<Integer> storageInfo) {
    }

    public void writePhbEntryResponse(RadioResponseInfo responseInfo) {
    }

    public void readPhbEntryResponse(RadioResponseInfo responseInfo, ArrayList<PhbEntryStructure> phbEntry) {
    }

    public void queryUPBCapabilityResponse(RadioResponseInfo responseInfo, ArrayList<Integer> upbCapability) {
    }

    public void editUPBEntryResponse(RadioResponseInfo responseInfo) {
    }

    public void deleteUPBEntryResponse(RadioResponseInfo responseInfo) {
    }

    public void readUPBGasListResponse(RadioResponseInfo responseInfo, ArrayList<String> gasList) {
    }

    public void readUPBGrpEntryResponse(RadioResponseInfo responseInfo, ArrayList<Integer> grpEntries) {
    }

    public void writeUPBGrpEntryResponse(RadioResponseInfo responseInfo) {
    }

    public void getPhoneBookStringsLengthResponse(RadioResponseInfo responseInfo, ArrayList<Integer> stringLength) {
    }

    public void getPhoneBookMemStorageResponse(RadioResponseInfo responseInfo, PhbMemStorageResponse phbMemStorage) {
    }

    public void setPhoneBookMemStorageResponse(RadioResponseInfo responseInfo) {
    }

    public void readPhoneBookEntryExtResponse(RadioResponseInfo responseInfo, ArrayList<PhbEntryExt> phbEntryExts) {
    }

    public void writePhoneBookEntryExtResponse(RadioResponseInfo responseInfo) {
    }

    public void queryUPBAvailableResponse(RadioResponseInfo responseInfo, ArrayList<Integer> upbAvailable) {
    }

    public void readUPBEmailEntryResponse(RadioResponseInfo responseInfo, String email) {
    }

    public void readUPBSneEntryResponse(RadioResponseInfo responseInfo, String sne) {
    }

    public void readUPBAnrEntryResponse(RadioResponseInfo responseInfo, ArrayList<PhbEntryStructure> anrs) {
    }

    public void readUPBAasListResponse(RadioResponseInfo responseInfo, ArrayList<String> aasList) {
    }

    public void setPhonebookReadyResponse(RadioResponseInfo responseInfo) {
    }

    public void restartRILDResponse(RadioResponseInfo responseInfo) {
    }

    public void getFemtocellListResponse(RadioResponseInfo responseInfo, ArrayList<String> femtoList) {
    }

    public void abortFemtocellListResponse(RadioResponseInfo responseInfo) {
    }

    public void selectFemtocellResponse(RadioResponseInfo responseInfo) {
    }

    public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) {
    }

    public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) {
    }

    public void syncDataSettingsToMdResponse(RadioResponseInfo responseInfo) {
    }

    public void resetMdDataRetryCountResponse(RadioResponseInfo responseInfo) {
    }

    public void setRemoveRestrictEutranModeResponse(RadioResponseInfo responseInfo) {
    }

    public void queryNetworkLockResponse(RadioResponseInfo info, int catagory, int state, int retry_cnt, int autolock_cnt, int num_set, int total_set, int key_state) {
    }

    public void setNetworkLockResponse(RadioResponseInfo info) {
    }

    public void supplyDepersonalizationResponse(RadioResponseInfo responseInfo, int retriesRemaining) {
    }

    public void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo responseInfo, int remainingAttempts) {
    }

    public void setRxTestConfigResponse(RadioResponseInfo responseInfo, ArrayList<Integer> respAntConf) {
    }

    public void getRxTestResultResponse(RadioResponseInfo responseInfo, ArrayList<Integer> respAntInfo) {
    }

    public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, ArrayList<Integer> polCapability) {
    }

    public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, ArrayList<String> polList) {
    }

    public void setPOLEntryResponse(RadioResponseInfo responseInfo) {
    }

    public void setRoamingEnableResponse(RadioResponseInfo responseInfo) {
    }

    public void getRoamingEnableResponse(RadioResponseInfo responseInfo, ArrayList<Integer> data) {
    }

    public void setLteReleaseVersionResponse(RadioResponseInfo responseInfo) {
    }

    public void getLteReleaseVersionResponse(RadioResponseInfo responseInfo, int mode) {
    }

    public void vsimNotificationResponse(RadioResponseInfo info, VsimEvent event) {
    }

    public void vsimOperationResponse(RadioResponseInfo info) {
    }

    public void setWifiEnabledResponse(RadioResponseInfo responseInfo) {
    }

    public void setWifiAssociatedResponse(RadioResponseInfo responseInfo) {
    }

    public void setWifiSignalLevelResponse(RadioResponseInfo responseInfo) {
    }

    public void setWifiIpAddressResponse(RadioResponseInfo responseInfo) {
    }

    public void setLocationInfoResponse(RadioResponseInfo responseInfo) {
    }

    public void setEmergencyAddressIdResponse(RadioResponseInfo responseInfo) {
    }

    public void setNattKeepAliveStatusResponse(RadioResponseInfo responseInfo) {
    }

    public void setWifiPingResultResponse(RadioResponseInfo responseInfo) {
    }

    public void notifyEPDGScreenStateResponse(RadioResponseInfo responseInfo) {
    }

    public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) {
    }

    public void sendRequestRawResponse(RadioResponseInfo responseInfo, ArrayList<Byte> data) {
    }

    public void sendRequestStringsResponse(RadioResponseInfo responseInfo, ArrayList<String> data) {
    }

    public void dataConnectionAttachResponse(RadioResponseInfo responseInfo) {
    }

    public void dataConnectionDetachResponse(RadioResponseInfo responseInfo) {
    }

    public void resetAllConnectionsResponse(RadioResponseInfo responseInfo) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse
    public void setTxPowerStatusResponse(RadioResponseInfo responseInfo) {
    }

    public void setSuppServPropertyResponse(RadioResponseInfo responseInfo) {
    }

    public void hangupWithReasonResponse(RadioResponseInfo responseInfo) {
    }

    public void setVendorSettingResponse(RadioResponseInfo responseInfo) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse
    public void getPlmnNameFromSE13TableResponse(RadioResponseInfo info, String name) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse
    public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) {
    }

    public void setGwsdModeResponse(RadioResponseInfo responseInfo) {
    }

    public void setCallValidTimerResponse(RadioResponseInfo responseInfo) {
    }

    public void setIgnoreSameNumberIntervalResponse(RadioResponseInfo responseInfo) {
    }

    public void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo responseInfo) {
    }

    public void setKeepAliveByIpDataResponse(RadioResponseInfo responseInfo) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse
    public void enableDsdaIndicationResponse(RadioResponseInfo responseInfo) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse
    public void getDsdaStatusResponse(RadioResponseInfo responseInfo, int mode) {
    }

    public void registerCellQltyReportResponse(RadioResponseInfo responseInfo) {
    }

    public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, ArrayList<String> data) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioExResponse
    public void setMaxUlSpeedResponse(RadioResponseInfo responseInfo) {
    }

    public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) {
    }

    public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) {
    }

    public void sendSarIndicatorResponse(RadioResponseInfo info) {
    }

    public void sendRsuRequestResponse(RadioResponseInfo responseInfo, RsuResponseInfo data) {
    }

    public void sendWifiEnabledResponse(RadioResponseInfo responseInfo) {
    }

    public void sendWifiAssociatedResponse(RadioResponseInfo responseInfo) {
    }

    public void sendWifiIpAddressResponse(RadioResponseInfo responseInfo) {
    }

    public void getTOEInfoResponse(RadioResponseInfo responseInfo, String longName, String shortName, String numeric) {
    }

    public void disableAllCALinksResponse(RadioResponseInfo responseInfo) {
    }

    public void getCALinkEnableStatusResponse(RadioResponseInfo responseInfo, boolean status) {
    }

    public void setCALinkEnableStatusResponse(RadioResponseInfo responseInfo) {
    }

    public void getCALinkCapabilityListResponse(RadioResponseInfo responseInfo, ArrayList<String> linkCapabilityList) {
    }

    public void getLteDataResponse(RadioResponseInfo responseInfo, LteData data) {
    }

    public void setQamEnabledResponse(RadioResponseInfo info) {
    }

    public void getQamEnabledResponse(RadioResponseInfo info, boolean ulOrDl, boolean enabled) {
    }

    public void setTm9EnabledResponse(RadioResponseInfo info) {
    }

    public void getTm9EnabledResponse(RadioResponseInfo info, boolean fddOrTdd, boolean enabled) {
    }

    public void setLteScanDurationResponse(RadioResponseInfo info) {
    }

    public void getLteScanDurationResponse(RadioResponseInfo info, int duration) {
    }

    public void getLteRRCStateResponse(RadioResponseInfo responseInfo, int state) {
    }

    public void setLteBandEnableStatusResponse(RadioResponseInfo responseInfo) {
    }

    public void getBandPriorityListResponse(RadioResponseInfo responseInfo, ArrayList<Integer> bandPriList) {
    }

    public void setBandPriorityListResponse(RadioResponseInfo responseInfo) {
    }

    public void set4x4MimoEnabledResponse(RadioResponseInfo responseInfo) {
    }

    public void get4x4MimoEnabledResponse(RadioResponseInfo responseInfo, int enabled_bitmask) {
    }

    public void getLteBsrTimerResponse(RadioResponseInfo responseInfo, int timer) {
    }

    public void setLteBsrTimerResponse(RadioResponseInfo responseInfo) {
    }

    public void getLte1xRttCellListResponse(RadioResponseInfo info, ArrayList<Lte1xRttCellInfo> list) {
    }

    public void clearLteAvailableFileResponse(RadioResponseInfo responseInfo) {
    }

    public void getBandModeResponse(RadioResponseInfo responseInfo, ArrayList<Integer> data) {
    }

    public void getCaBandModeResponse(RadioResponseInfo responseInfo, ArrayList<Integer> data) {
    }

    public void getCampedFemtoCellInfoResponse(RadioResponseInfo responseInfo, ArrayList<String> data) {
    }

    public void setDisable2GResponse(RadioResponseInfo responseInfo) {
    }

    public void getDisable2GResponse(RadioResponseInfo responseInfo, int mode) {
    }

    public void getEngineeringModeInfoResponse(RadioResponseInfo info, ArrayList<String> data) {
    }

    public void getIWlanRegistrationStateResponse(RadioResponseInfo responseInfo, int state) {
    }

    public void getAllBandModeResponse(RadioResponseInfo responseInfo, BandModeInfo data) {
    }

    public void setNrBandModeResponse(RadioResponseInfo info) {
    }
}
