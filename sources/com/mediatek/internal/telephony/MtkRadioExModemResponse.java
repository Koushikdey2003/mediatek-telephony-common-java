package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import com.android.internal.telephony.RadioResponse;
import vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExModemResponse extends IMtkRadioExModemResponse.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExModemResponse(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void runGbaAuthenticationResponse(RadioResponseInfo info, String[] resList) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendEmbmsAtCommandResponse(RadioResponseInfo responseInfo, String data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseString(3, this.mMtkRil, responseInfo, data);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendRequestRawResponse(RadioResponseInfo responseInfo, byte[] data) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(3, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, data);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, data);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendRequestStringsResponse(RadioResponseInfo responseInfo, String[] data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStrings(3, this.mMtkRil, responseInfo, data);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendSarIndicatorResponse(RadioResponseInfo info) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(3, info);
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

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void setModemPowerResponse(RadioResponseInfo info) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(3, info);
        if (rr != null) {
            if (info.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, null);
            }
            this.mMtkRil.processResponseDone(rr, info, null);
            return;
        }
        this.mMtkRil.riljLog("setModemPowerResponse, rr is null");
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void modifyModemTypeResponse(RadioResponseInfo info, int applyType) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(3, this.mMtkRil, info, new int[]{applyType});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void restartRILDResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void setTrmResponse(RadioResponseInfo info) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(3, info);
        if (rr != null) {
            if (info.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, null);
            }
            this.mMtkRil.processResponseDone(rr, info, null);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void setTxPowerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void setTxPowerStatusResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(3, responseInfo);
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

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void setVendorSettingResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void triggerModeSwitchByEccResponse(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(3, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                RadioResponse.sendMessageResponse(rr.mResult, (Object) null);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, null);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void registerCellQltyReportResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendWifiEnabledResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendWifiAssociatedResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void sendWifiIpAddressResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(3, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public void getEngineeringModeInfoResponse(RadioResponseInfo responseInfo, String[] data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStrings(3, this.mMtkRil, responseInfo, data);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public String getInterfaceHash() {
        return "87512b9a1978fdb596a8d9176854d3761382dc82";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
    public int getInterfaceVersion() {
        return 2;
    }
}
