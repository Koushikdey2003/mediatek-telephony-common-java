package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import com.android.internal.telephony.VoiceResponse;

/* JADX INFO: loaded from: classes.dex */
public class MtkVoiceResponse extends VoiceResponse {
    private MtkRIL mMtkRil;

    public MtkVoiceResponse(MtkRIL ril) {
        super(ril);
        this.mMtkRil = ril;
    }

    public void switchWaitingOrHoldingAndActiveResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.riljLog("clear mIsSendChldRequest");
        synchronized (this.mMtkRil) {
            this.mMtkRil.mDtmfReqQueue.resetSendChldRequest();
        }
        super.switchWaitingOrHoldingAndActiveResponse(responseInfo);
    }

    public void conferenceResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.riljLog("clear mIsSendChldRequest");
        synchronized (this.mMtkRil) {
            this.mMtkRil.mDtmfReqQueue.resetSendChldRequest();
        }
        super.conferenceResponse(responseInfo);
    }

    public void startDtmfResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.handleDtmfQueueNext(responseInfo.serial);
        super.startDtmfResponse(responseInfo);
    }

    public void stopDtmfResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.handleDtmfQueueNext(responseInfo.serial);
        super.stopDtmfResponse(responseInfo);
    }

    public void separateConnectionResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.riljLog("clear mIsSendChldRequest");
        synchronized (this.mMtkRil) {
            this.mMtkRil.mDtmfReqQueue.resetSendChldRequest();
        }
        super.separateConnectionResponse(responseInfo);
    }

    public void explicitCallTransferResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.riljLog("clear mIsSendChldRequest");
        synchronized (this.mMtkRil) {
            this.mMtkRil.mDtmfReqQueue.resetSendChldRequest();
        }
        super.explicitCallTransferResponse(responseInfo);
    }
}
