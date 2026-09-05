package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import vendor.mediatek.hardware.mtkradioex.voice.CallForwardInfoEx;
import vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExVoiceResponse extends IMtkRadioExVoiceResponse.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExVoiceResponse(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void hangupAllResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void hangupWithReasonResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void getCallSubAddressResponse(RadioResponseInfo info, int enable) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(6, this.mMtkRil, info, new int[]{enable});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void getColpResponse(RadioResponseInfo info, int n, int m) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(6, this.mMtkRil, info, new int[]{n, m});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void getColrResponse(RadioResponseInfo info, int n) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(6, this.mMtkRil, info, new int[]{n});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void getEccNumResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    private void responseCallForwardInfoEx(RadioResponseInfo responseInfo, CallForwardInfoEx[] callForwardInfoExs) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(6, responseInfo);
        if (rr != null) {
            MtkCallForwardInfo[] ret = new MtkCallForwardInfo[callForwardInfoExs.length];
            for (int i = 0; i < callForwardInfoExs.length; i++) {
                long[] timeSlot = new long[2];
                ret[i] = new MtkCallForwardInfo();
                ret[i].status = callForwardInfoExs[i].status;
                ret[i].reason = callForwardInfoExs[i].reason;
                ret[i].serviceClass = callForwardInfoExs[i].serviceClass;
                ret[i].toa = callForwardInfoExs[i].toa;
                ret[i].number = callForwardInfoExs[i].number;
                ret[i].timeSeconds = callForwardInfoExs[i].timeSeconds;
                String[] timeSlotStr = {callForwardInfoExs[i].timeSlotBegin, callForwardInfoExs[i].timeSlotEnd};
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

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo info, CallForwardInfoEx[] callForwardInfoExs) {
        responseCallForwardInfoEx(info, callForwardInfoExs);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void resetSuppServResponse(RadioResponseInfo info) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void sendCnapResponse(RadioResponseInfo info, int n, int m) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(6, this.mMtkRil, info, new int[]{n});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setCallForwardInTimeSlotResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setCallIndicationResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setCallSubAddressResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setClipResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setColpResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setColrResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setEccModeResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setEccNumResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setGwsdModeResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setCallValidTimerResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setIgnoreSameNumberIntervalResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setKeepAliveByIpDataResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public void setSuppServPropertyResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(6, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public String getInterfaceHash() {
        return "6fb707faf11116647ab6b8daa3ee47c2662abaa2";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse
    public int getInterfaceVersion() {
        return 1;
    }
}
