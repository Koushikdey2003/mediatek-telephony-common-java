package com.mediatek.internal.telephony;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.os.AsyncResult;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.uicc.IccUtils;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.phb.PBEntry;
import com.mediatek.internal.telephony.phb.PBMemStorage;
import com.mediatek.internal.telephony.phb.PhbEntry;
import com.mediatek.internal.telephony.rsu.RsuResponseData;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import mediatek.telephony.MtkSmsParameters;
import vendor.mediatek.hardware.mtkradioex.V2_0.CallForwardInfoEx;
import vendor.mediatek.hardware.mtkradioex.V2_0.OperatorInfoWithAct;
import vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryExt;
import vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryStructure;
import vendor.mediatek.hardware.mtkradioex.V2_0.PhbMemStorageResponse;
import vendor.mediatek.hardware.mtkradioex.V2_0.RsuResponseInfo;
import vendor.mediatek.hardware.mtkradioex.V2_0.SignalStrengthWithWcdmaEcio;
import vendor.mediatek.hardware.mtkradioex.V2_0.SmsMemStatus;
import vendor.mediatek.hardware.mtkradioex.V2_0.SmsParams;
import vendor.mediatek.hardware.mtkradioex.V2_0.VsimEvent;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExResponseV2 extends MtkRadioExResponseBaseV2 {
    private static final String TAG = "MtkRadioRespEx";
    private static final boolean isUserLoad = SystemProperties.get("ro.build.type").equals(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    MtkMessageBoost mMtkMessageBoost;
    MtkRIL mMtkRil;

    public MtkRadioExResponseV2(RIL ril) {
        super(ril);
        MtkRIL mtkRIL = (MtkRIL) ril;
        this.mMtkRil = mtkRIL;
        this.mMtkMessageBoost = MtkMessageBoost.init(mtkRIL);
    }

    static void sendMessageResponse(Message msg, Object ret) {
        if (msg != null) {
            AsyncResult.forMessage(msg, ret, (Throwable) null);
            msg.sendToTarget();
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void supplyDepersonalizationResponse(RadioResponseInfo responseInfo, int retriesRemaining) {
        this.mMtkRil.getMtkRadioResponse().supplyNetworkDepersonalizationResponse(responseInfo, retriesRemaining);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setClipResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getColpResponse(RadioResponseInfo responseInfo, int n, int m) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{n, m});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getColrResponse(RadioResponseInfo responseInfo, int status) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{status});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendCnapResponse(RadioResponseInfo responseInfo, int n, int m) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{n, m});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setColpResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setColrResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo responseInfo, ArrayList<CallForwardInfoEx> callForwardInfoExs) {
        responseCallForwardInfoEx(responseInfo, callForwardInfoExs);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setCallForwardInTimeSlotResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void runGbaAuthenticationResponse(RadioResponseInfo responseInfo, ArrayList<String> resList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(this.mMtkRil.getMtkRadioResponse().mRil, responseInfo, resList);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void sendOemRilRequestRawResponse(RadioResponseInfo responseInfo, ArrayList<Byte> var2) {
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setTrmResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, null);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, null);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getATRResponse(RadioResponseInfo info, String response) {
        this.mMtkRil.getMtkRadioResponse().responseString(info, response);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getIccidResponse(RadioResponseInfo info, String response) {
        this.mMtkRil.getMtkRadioResponse().responseString(info, response);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setSimPowerResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(info);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void activateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) {
        this.mMtkRil.getMtkRadioResponse().responseInts(info, new int[]{simPowerOnOffResponse});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void deactivateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) {
        this.mMtkRil.getMtkRadioResponse().responseInts(info, new int[]{simPowerOnOffResponse});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo info, int simPowerOnOffStatus) {
        this.mMtkRil.getMtkRadioResponse().responseInts(info, new int[]{simPowerOnOffStatus});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().setNetworkSelectionModeManualResponse(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getAvailableNetworksWithActResponse(RadioResponseInfo responseInfo, ArrayList<OperatorInfoWithAct> networkInfos) {
        responseOperatorInfosWithAct(responseInfo, networkInfos);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo responseInfo, SignalStrengthWithWcdmaEcio signalStrength) {
        responseGetSignalStrengthWithWcdmaEcio(responseInfo, signalStrength);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void cancelAvailableNetworksResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void cfgA2offsetResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void cfgB1offsetResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void enableSCGfailureResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{deactivate, allowSCGAdd});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void disableNRResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setTxPowerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setSearchRatResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    private static String convertOpertatorInfoToString(int status) {
        if (status == 0) {
            return "unknown";
        }
        if (status == 1) {
            return "available";
        }
        if (status == 2) {
            return "current";
        }
        if (status == 3) {
            return "forbidden";
        }
        return "";
    }

    private void responseOperatorInfosWithAct(RadioResponseInfo responseInfo, ArrayList<OperatorInfoWithAct> networkInfos) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.getMtkRadioResponse().mRil.processResponse(responseInfo);
        if (rr != null) {
            ArrayList<OperatorInfo> ret = null;
            if (responseInfo.error == 0) {
                ret = new ArrayList<>();
                for (int i = 0; i < networkInfos.size(); i++) {
                    int nLac = -1;
                    if (networkInfos.get(i).lac.length() > 0) {
                        nLac = Integer.parseInt(networkInfos.get(i).lac, 16);
                    }
                    android.hardware.radio.V1_0.OperatorInfo operatorInfo = networkInfos.get(i).base;
                    MtkRIL mtkRIL = this.mMtkRil;
                    operatorInfo.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), networkInfos.get(i).base.operatorNumeric, true, nLac);
                    android.hardware.radio.V1_0.OperatorInfo operatorInfo2 = networkInfos.get(i).base;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    operatorInfo2.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), networkInfos.get(i).base.operatorNumeric, false, nLac);
                    networkInfos.get(i).base.alphaLong = networkInfos.get(i).base.alphaLong.concat(" " + networkInfos.get(i).act);
                    networkInfos.get(i).base.alphaShort = networkInfos.get(i).base.alphaShort.concat(" " + networkInfos.get(i).act);
                    if (!this.mMtkRil.hidePLMN(networkInfos.get(i).base.operatorNumeric)) {
                        ret.add(new OperatorInfo(networkInfos.get(i).base.alphaLong, networkInfos.get(i).base.alphaShort, networkInfos.get(i).base.operatorNumeric, convertOpertatorInfoToString(networkInfos.get(i).base.status)));
                    } else {
                        this.mMtkRil.riljLog("remove this one " + networkInfos.get(i).base.operatorNumeric);
                    }
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.getMtkRadioResponse().mRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    private void responseGetSignalStrengthWithWcdmaEcio(RadioResponseInfo responseInfo, SignalStrengthWithWcdmaEcio signalStrength) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.getMtkRadioResponse().mRil.processResponse(responseInfo);
        if (rr != null) {
            SignalStrength ret = new SignalStrength();
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setModemPowerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getSmsParametersResponse(RadioResponseInfo responseInfo, SmsParams params) {
        responseSmsParams(responseInfo, params);
    }

    private void responseSmsParams(RadioResponseInfo responseInfo, SmsParams params) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            Object ret = null;
            if (responseInfo.error == 0) {
                Object smsp = new MtkSmsParameters(params.format, params.vp, params.pid, params.dcs);
                this.mMtkRil.riljLog("responseSmsParams: from HIDL: " + smsp);
                ret = smsp;
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setSmsParametersResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setEtwsResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void removeCbMsgResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getSmsMemStatusResponse(RadioResponseInfo responseInfo, SmsMemStatus params) {
        responseSmsMemStatus(responseInfo, params);
    }

    private void responseSmsMemStatus(RadioResponseInfo responseInfo, SmsMemStatus params) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            Object ret = null;
            if (responseInfo.error == 0) {
                Object status = new MtkIccSmsStorageStatus(params.used, params.total);
                this.mMtkRil.riljLog("responseSmsMemStatus: from HIDL: " + status);
                ret = status;
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setGsmBroadcastLangsResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getGsmBroadcastLangsResponse(RadioResponseInfo responseInfo, String langs) {
        this.mMtkRil.getMtkRadioResponse().responseString(responseInfo, langs);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getGsmBroadcastActivationRsp(RadioResponseInfo responseInfo, int activation) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{activation});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendEmbmsAtCommandResponse(RadioResponseInfo responseInfo, String result) {
        this.mMtkRil.getMtkRadioResponse().responseString(responseInfo, result);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void hangupAllResponse(RadioResponseInfo responseInfo) throws RemoteException {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setCallIndicationResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setVoicePreferStatusResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setEccNumResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getEccNumResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setEccModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void eccPreferredRatResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setCallSubAddressResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getCallSubAddressResponse(RadioResponseInfo responseInfo, int enabled) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{enabled});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setApcModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getApcInfoResponse(RadioResponseInfo responseInfo, ArrayList<Integer> cellInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            int[] response = new int[cellInfo.size()];
            if (responseInfo.error == 0) {
                for (int i = 0; i < cellInfo.size(); i++) {
                    response[i] = cellInfo.get(i).intValue();
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, response);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, response);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void triggerModeSwitchByEccResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, null);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, null);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getSmsRuimMemoryStatusResponse(RadioResponseInfo responseInfo, SmsMemStatus memStatus) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            MtkIccSmsStorageStatus ret = null;
            if (responseInfo.error == 0) {
                ret = new MtkIccSmsStorageStatus(memStatus.used, memStatus.total);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setFdModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setResumeRegistrationResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void modifyModemTypeResponse(RadioResponseInfo responseInfo, int applyType) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{applyType});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void queryPhbStorageInfoResponse(RadioResponseInfo responseInfo, ArrayList<Integer> storageInfo) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, storageInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void writePhbEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readPhbEntryResponse(RadioResponseInfo responseInfo, ArrayList<PhbEntryStructure> phbEntry) {
        responsePhbEntries(responseInfo, phbEntry);
    }

    private void responsePhbEntries(RadioResponseInfo responseInfo, ArrayList<PhbEntryStructure> phbEntry) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            PhbEntry[] ret = null;
            if (responseInfo.error == 0) {
                ret = new PhbEntry[phbEntry.size()];
                for (int i = 0; i < phbEntry.size(); i++) {
                    ret[i] = new PhbEntry();
                    ret[i].type = phbEntry.get(i).type;
                    ret[i].index = phbEntry.get(i).index;
                    ret[i].number = phbEntry.get(i).number;
                    ret[i].ton = phbEntry.get(i).ton;
                    ret[i].alphaId = phbEntry.get(i).alphaId;
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void queryUPBCapabilityResponse(RadioResponseInfo responseInfo, ArrayList<Integer> upbCapability) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, upbCapability);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void editUPBEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void deleteUPBEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readUPBGasListResponse(RadioResponseInfo responseInfo, ArrayList<String> gasList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(this.mMtkRil.getMtkRadioResponse().mRil, responseInfo, gasList);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readUPBGrpEntryResponse(RadioResponseInfo responseInfo, ArrayList<Integer> grpEntries) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, grpEntries);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void writeUPBGrpEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getPhoneBookStringsLengthResponse(RadioResponseInfo responseInfo, ArrayList<Integer> stringLength) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, stringLength);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getPhoneBookMemStorageResponse(RadioResponseInfo responseInfo, PhbMemStorageResponse phbMemStorage) {
        responseGetPhbMemStorage(responseInfo, phbMemStorage);
    }

    private void responseGetPhbMemStorage(RadioResponseInfo responseInfo, PhbMemStorageResponse phbMemStorage) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            PBMemStorage ret = new PBMemStorage();
            if (responseInfo.error == 0) {
                ret.setStorage(phbMemStorage.storage);
                ret.setUsed(phbMemStorage.used);
                ret.setTotal(phbMemStorage.total);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setPhoneBookMemStorageResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readPhoneBookEntryExtResponse(RadioResponseInfo responseInfo, ArrayList<PhbEntryExt> phbEntryExts) {
        responseReadPhbEntryExt(responseInfo, phbEntryExts);
    }

    private void responseCallForwardInfoEx(RadioResponseInfo responseInfo, ArrayList<CallForwardInfoEx> callForwardInfoExs) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            MtkCallForwardInfo[] ret = new MtkCallForwardInfo[callForwardInfoExs.size()];
            for (int i = 0; i < callForwardInfoExs.size(); i++) {
                long[] timeSlot = new long[2];
                ret[i] = new MtkCallForwardInfo();
                ret[i].status = callForwardInfoExs.get(i).status;
                ret[i].reason = callForwardInfoExs.get(i).reason;
                ret[i].serviceClass = callForwardInfoExs.get(i).serviceClass;
                ret[i].toa = callForwardInfoExs.get(i).toa;
                ret[i].number = callForwardInfoExs.get(i).number;
                ret[i].timeSeconds = callForwardInfoExs.get(i).timeSeconds;
                String[] timeSlotStr = {callForwardInfoExs.get(i).timeSlotBegin, callForwardInfoExs.get(i).timeSlotEnd};
                if (timeSlotStr[0] == null || timeSlotStr[1] == null) {
                    ret[i].timeSlot = null;
                } else {
                    for (int j = 0; j < 2; j++) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                        try {
                            Date date = dateFormat.parse(timeSlotStr[j]);
                            timeSlot[j] = date.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            timeSlot = null;
                        }
                    }
                    ret[i].timeSlot = timeSlot;
                }
            }
            int i2 = responseInfo.error;
            if (i2 == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    private void responseReadPhbEntryExt(RadioResponseInfo responseInfo, ArrayList<PhbEntryExt> phbEntryExts) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            PBEntry[] ret = null;
            if (responseInfo.error == 0) {
                ret = new PBEntry[phbEntryExts.size()];
                for (int i = 0; i < phbEntryExts.size(); i++) {
                    ret[i] = new PBEntry();
                    ret[i].setIndex1(phbEntryExts.get(i).type);
                    ret[i].setNumber(phbEntryExts.get(i).number);
                    ret[i].setType(phbEntryExts.get(i).type);
                    ret[i].setText(phbEntryExts.get(i).text);
                    ret[i].setHidden(phbEntryExts.get(i).hidden);
                    ret[i].setGroup(phbEntryExts.get(i).group);
                    ret[i].setAdnumber(phbEntryExts.get(i).adnumber);
                    ret[i].setAdtype(phbEntryExts.get(i).adtype);
                    ret[i].setSecondtext(phbEntryExts.get(i).secondtext);
                    ret[i].setEmail(phbEntryExts.get(i).email);
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void writePhoneBookEntryExtResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void queryUPBAvailableResponse(RadioResponseInfo responseInfo, ArrayList<Integer> upbAvailable) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, upbAvailable);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readUPBEmailEntryResponse(RadioResponseInfo responseInfo, String email) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, email);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, "xxx@email.com");
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readUPBSneEntryResponse(RadioResponseInfo responseInfo, String sne) {
        this.mMtkRil.getMtkRadioResponse().responseString(responseInfo, sne);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readUPBAnrEntryResponse(RadioResponseInfo responseInfo, ArrayList<PhbEntryStructure> anrs) {
        responsePhbEntries(responseInfo, anrs);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void readUPBAasListResponse(RadioResponseInfo responseInfo, ArrayList<String> aasList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(this.mMtkRil.getMtkRadioResponse().mRil, responseInfo, aasList);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setPhonebookReadyResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void restartRILDResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getFemtocellListResponse(RadioResponseInfo responseInfo, ArrayList<String> femtoList) {
        responseFemtoCellInfos(responseInfo, femtoList);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void abortFemtocellListResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void selectFemtocellResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{mode});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void queryNetworkLockResponse(RadioResponseInfo info, int catagory, int state, int retry_cnt, int autolock_cnt, int num_set, int total_set, int key_state) {
        this.mMtkRil.getMtkRadioResponse().responseInts(info, new int[]{catagory, state, retry_cnt, autolock_cnt, num_set, total_set, key_state});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setNetworkLockResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(info);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo responseInfo, int retriesRemaining) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{retriesRemaining});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void responseFemtoCellInfos(RadioResponseInfo responseInfo, ArrayList<String> info) {
        Phone phone;
        boolean con;
        String actStr;
        int rat;
        boolean con2;
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.getMtkRadioResponse().mRil.processResponse(responseInfo);
        ArrayList<FemtoCellInfo> femtoInfos = null;
        String fPlmn = null;
        String fAct = null;
        String fCsgId = null;
        Phone phone2 = PhoneFactory.getPhone(this.mMtkRil.mInstanceId.intValue());
        if (phone2 != null) {
            ServiceStateTracker sst = phone2.getServiceStateTracker();
            ServiceState serviceState = sst.mSS;
            fPlmn = ((MtkServiceStateTracker) sst).getFemtoPlmn();
            fAct = ((MtkServiceStateTracker) sst).getFemtoAct();
            fCsgId = ((MtkServiceStateTracker) sst).getFemtoCsgId();
        }
        if (rr != null && responseInfo.error == 0) {
            String[] strings = new String[info.size()];
            for (int i = 0; i < info.size(); i++) {
                strings[i] = info.get(i);
            }
            int i2 = strings.length;
            if (i2 % 7 != 0) {
                throw new RuntimeException("responseFemtoCellInfos: invalid response. Got " + strings.length + " strings, expected multible of 7");
            }
            ArrayList<FemtoCellInfo> femtoInfos2 = new ArrayList<>(strings.length / 7);
            int i3 = 0;
            while (i3 < strings.length) {
                if (strings[i3 + 1] != null) {
                    phone = phone2;
                    if (strings[i3 + 1].startsWith("uCs2")) {
                        this.mMtkRil.riljLog("responseFemtoCellInfos handling UCS2 format name");
                        try {
                            strings[i3 + 0] = new String(IccUtils.hexStringToBytes(strings[i3 + 1].substring(4)), "UTF-16");
                        } catch (UnsupportedEncodingException e) {
                            this.mMtkRil.riljLog("responseFemtoCellInfos UnsupportedEncodingException");
                        }
                    }
                } else {
                    phone = phone2;
                }
                if (strings[i3 + 1] == null) {
                    con = false;
                } else if (!strings[i3 + 1].equals("") && !strings[i3 + 1].equals(strings[i3 + 0])) {
                    con = false;
                } else {
                    this.mMtkRil.riljLog("lookup RIL responseFemtoCellInfos() for plmn id= " + strings[i3 + 0]);
                    MtkRIL mtkRIL = this.mMtkRil;
                    con = false;
                    strings[i3 + 1] = mtkRIL.lookupOperatorName(getSubId(mtkRIL.mInstanceId.intValue()), strings[i3 + 0], true, -1);
                }
                int sig = Integer.valueOf(strings[i3 + 6]).intValue();
                if (strings[i3 + 2] != null && strings[i3 + 2].equals(MtkGsmCdmaPhone.ACT_TYPE_LTE)) {
                    actStr = MtkGsmCdmaPhone.LTE_INDICATOR;
                    rat = 14;
                } else if (strings[i3 + 2] != null && strings[i3 + 2].equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN)) {
                    actStr = MtkGsmCdmaPhone.UTRAN_INDICATOR;
                    rat = 3;
                } else {
                    actStr = MtkGsmCdmaPhone.GSM_INDICATOR;
                    rat = 1;
                }
                strings[i3 + 1] = strings[i3 + 1].concat(" " + actStr);
                String hnbName = new String(IccUtils.hexStringToBytes(strings[i3 + 5]));
                if (strings[i3 + 0] != null && strings[i3 + 0].equals(fPlmn) && strings[i3 + 2] != null && strings[i3 + 2].equals(fAct) && strings[i3 + 3] != null && strings[i3 + 3].equals(fCsgId)) {
                    con2 = true;
                } else {
                    con2 = con;
                }
                MtkRIL mtkRIL2 = this.mMtkRil;
                String fPlmn2 = fPlmn;
                StringBuilder sbAppend = new StringBuilder().append("FemtoCellInfo(").append(strings[i3 + 3]).append(",");
                String fAct2 = fAct;
                String fAct3 = strings[i3 + 4];
                mtkRIL2.riljLog(sbAppend.append(fAct3).append(",").append(strings[i3 + 5]).append(",").append(strings[i3 + 0]).append(",").append(strings[i3 + 1]).append(",").append(rat).append(") hnbName=").append(hnbName).append(",sig=").append(sig).append(",con=").append(con2).toString());
                femtoInfos2.add(new FemtoCellInfo(Integer.parseInt(strings[i3 + 3]), Integer.parseInt(strings[i3 + 4]), hnbName, strings[i3 + 0], strings[i3 + 1], rat, con2, sig));
                i3 += 7;
                phone2 = phone;
                fPlmn = fPlmn2;
                fAct = fAct2;
            }
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, femtoInfos2);
            femtoInfos = femtoInfos2;
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, femtoInfos);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setRxTestConfigResponse(RadioResponseInfo responseInfo, ArrayList<Integer> respAntConf) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, respAntConf);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getRxTestResultResponse(RadioResponseInfo responseInfo, ArrayList<Integer> respAntInfo) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, respAntInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, ArrayList<Integer> polCapability) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, polCapability);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, ArrayList<String> polList) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        ArrayList<NetworkInfoWithAcT> NetworkInfos = null;
        if (rr != null) {
            if (responseInfo.error == 0) {
                if (polList.size() % 4 != 0) {
                    this.mMtkRil.riljLog("RIL_REQUEST_GET_POL_LIST: invalid response. Got " + polList.size() + " strings, expected multible of 4");
                } else {
                    NetworkInfos = new ArrayList<>(polList.size() / 4);
                    for (int i = 0; i < polList.size(); i += 4) {
                        String strOperName = null;
                        String strOperNumeric = null;
                        int nIndex = polList.get(i) != null ? Integer.parseInt(polList.get(i)) : 0;
                        if (polList.get(i + 1) != null) {
                            int format = Integer.parseInt(polList.get(i + 1));
                            switch (format) {
                                case 0:
                                case 1:
                                    strOperName = polList.get(i + 2);
                                    break;
                                case 2:
                                    if (polList.get(i + 2) != null) {
                                        strOperNumeric = polList.get(i + 2);
                                        MtkRIL mtkRIL = this.mMtkRil;
                                        strOperName = mtkRIL.lookupOperatorName(getSubId(mtkRIL.mInstanceId.intValue()), strOperNumeric, true, -1);
                                    }
                                    break;
                            }
                        }
                        int format2 = i + 3;
                        int nAct = polList.get(format2) != null ? Integer.parseInt(polList.get(i + 3)) : 0;
                        if (strOperNumeric != null && !strOperNumeric.equals("?????")) {
                            NetworkInfos.add(new NetworkInfoWithAcT(strOperName, strOperNumeric, nAct, nIndex));
                        }
                    }
                    this.mMtkRil.getMtkRadioResponse();
                    MtkRadioResponse.sendMessageResponse(rr.mResult, NetworkInfos);
                }
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, NetworkInfos);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setPOLEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void syncDataSettingsToMdResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void resetMdDataRetryCountResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setRemoveRestrictEutranModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setRoamingEnableResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getRoamingEnableResponse(RadioResponseInfo responseInfo, ArrayList<Integer> data) {
        this.mMtkRil.getMtkRadioResponse().responseIntArrayList(responseInfo, data);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setLteReleaseVersionResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getLteReleaseVersionResponse(RadioResponseInfo responseInfo, int mode) {
        this.mMtkRil.getMtkRadioResponse().responseInts(responseInfo, new int[]{mode});
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void vsimNotificationResponse(RadioResponseInfo info, VsimEvent event) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(info);
        if (rr != null) {
            Object ret = null;
            if (info.error == 0) {
                ret = Integer.valueOf(event.transactionId);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, info, ret);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void vsimOperationResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(info);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setWifiEnabledResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setWifiAssociatedResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setWifiSignalLevelResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setWifiIpAddressResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setLocationInfoResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setEmergencyAddressIdResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setNattKeepAliveStatusResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void setWifiPingResultResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2
    public void notifyEPDGScreenStateResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendRequestRawResponse(RadioResponseInfo responseInfo, ArrayList<Byte> data) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            byte[] ret = null;
            if (responseInfo.error == 0) {
                ret = RILUtils.arrayListToPrimitiveArray(data);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    public void setTxPowerStatusResponse(RadioResponseInfo responseInfo, ArrayList<Byte> data) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, null);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, null);
            return;
        }
        this.mMtkRil.riljLog("setTxPowerStatusResponse, rr is null");
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendRequestStringsResponse(RadioResponseInfo responseInfo, ArrayList<String> data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(this.mMtkRil, responseInfo, data);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void dataConnectionAttachResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void dataConnectionDetachResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void resetAllConnectionsResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setSuppServPropertyResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setVendorSettingResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setGwsdModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setCallValidTimerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setIgnoreSameNumberIntervalResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void setKeepAliveByIpDataResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void hangupWithReasonResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void registerCellQltyReportResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, null);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, ArrayList<String> data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(this.mMtkRil, responseInfo, data);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendSarIndicatorResponse(RadioResponseInfo info) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(info);
        if (rr != null) {
            if (info.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, null);
            }
            this.mMtkRil.processResponseDone(rr, info, null);
            return;
        }
        this.mMtkRil.riljLog("sendSarIndicatorResponse, rr is null");
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendRsuRequestResponse(RadioResponseInfo responseInfo, RsuResponseInfo rsuInfo) {
        this.mMtkRil.mtkRiljLog("V2 sendRsuRequestResponse");
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr != null) {
            RsuResponseData rrs = new RsuResponseData();
            rrs.opId = rsuInfo.opId;
            rrs.requestId = rsuInfo.requestId;
            rrs.errCode = rsuInfo.errCode;
            rrs.time = rsuInfo.time;
            rrs.version = rsuInfo.version;
            rrs.status = rsuInfo.status;
            rrs.reserveInt1 = rsuInfo.reserveInt1;
            rrs.reserveInt2 = rsuInfo.reserveInt2;
            rrs.data = rsuInfo.data == null ? "" : rsuInfo.data;
            rrs.reserveString1 = rsuInfo.reserveString1 != null ? rsuInfo.reserveString1 : "";
            if (responseInfo.error == 0) {
                AsyncResult.forMessage(rr.mResult, rrs, (Throwable) null);
                rr.mResult.sendToTarget();
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, rsuInfo);
            return;
        }
        this.mMtkRil.riljLog("sendRsuRequestResponse, rr is null");
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendWifiEnabledResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendWifiAssociatedResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExResponseBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    public void sendWifiIpAddressResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse().responseVoid(responseInfo);
    }
}
