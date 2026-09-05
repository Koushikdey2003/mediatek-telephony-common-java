package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import mediatek.telephony.MtkSmsParameters;
import vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse;
import vendor.mediatek.hardware.mtkradioex.messaging.SmsMemStatus;
import vendor.mediatek.hardware.mtkradioex.messaging.SmsParams;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExMessagingResponse extends IMtkRadioExMessagingResponse.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExMessagingResponse(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void getGsmBroadcastActivationRsp(RadioResponseInfo info, int active) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(2, this.mMtkRil, info, new int[]{active});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void getGsmBroadcastLangsResponse(RadioResponseInfo info, String langs) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseString(2, this.mMtkRil, info, langs);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void getSmsMemStatusResponse(RadioResponseInfo info, SmsMemStatus param) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(2, info);
        if (rr != null) {
            Object ret = null;
            if (info.error == 0) {
                Object status = new MtkIccSmsStorageStatus(param.used, param.total);
                this.mMtkRil.riljLog("responseSmsMemStatus: from AIDL: " + status);
                ret = status;
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, info, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void getSmsParametersResponse(RadioResponseInfo info, SmsParams param) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(2, info);
        if (rr != null) {
            Object ret = null;
            if (info.error == 0) {
                Object smsp = new MtkSmsParameters(param.format, param.vp, param.pid, param.dcs);
                this.mMtkRil.riljLog("responseSmsParams: from HIDL: " + smsp);
                ret = smsp;
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, info, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void getSmsRuimMemoryStatusResponse(RadioResponseInfo info, SmsMemStatus memStatus) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(2, info);
        if (rr != null) {
            MtkIccSmsStorageStatus ret = null;
            if (info.error == 0) {
                ret = new MtkIccSmsStorageStatus(memStatus.used, memStatus.total);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, info, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void removeCbMsgResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(2, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void setEtwsResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(2, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void setGsmBroadcastLangsResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(2, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void setSmsParametersResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(2, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public void exitSCBMResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(2, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public String getInterfaceHash() {
        return "93a2d5af4d82e246ed35f8afe17083da08ba1147";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingResponse
    public int getInterfaceVersion() {
        return 1;
    }
}
