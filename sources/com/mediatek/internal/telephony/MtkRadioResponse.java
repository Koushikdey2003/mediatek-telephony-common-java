package com.mediatek.internal.telephony;

import android.hardware.radio.V1_0.OperatorInfo;
import android.hardware.radio.V1_0.RadioResponseInfo;
import android.telephony.MtkRadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.NetworkScanResult;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.RadioResponse;
import com.android.internal.telephony.ServiceStateTracker;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioResponse extends RadioResponse {
    private static final String TAG = "MtkRadioResp";
    private MtkRIL mMtkRil;

    public MtkRadioResponse(RIL ril) {
        super(ril);
        this.mMtkRil = (MtkRIL) ril;
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

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void getAvailableNetworksResponse(RadioResponseInfo responseInfo, ArrayList<OperatorInfo> networkInfos) {
        int mLac = -1;
        Phone phone = PhoneFactory.getPhone(this.mMtkRil.mInstanceId.intValue());
        if (phone != null) {
            ServiceStateTracker sst = phone.getServiceStateTracker();
            ServiceState serviceState = sst.mSS;
            mLac = ((MtkServiceStateTracker) sst).getLac();
        }
        for (int i = 0; i < networkInfos.size(); i++) {
            String mccmnc = networkInfos.get(i).operatorNumeric;
            OperatorInfo operatorInfo = networkInfos.get(i);
            MtkRIL mtkRIL = this.mMtkRil;
            operatorInfo.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, mLac);
            OperatorInfo operatorInfo2 = networkInfos.get(i);
            MtkRIL mtkRIL2 = this.mMtkRil;
            operatorInfo2.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, mLac);
        }
        super.getAvailableNetworksResponse(responseInfo, networkInfos);
    }

    public void getPreferredNetworkTypeBitmapResponse(RadioResponseInfo responseInfo, int halRadioAccessFamilyBitmap) {
        int networkType = MtkRadioAccessFamily.getNetworkTypeFromRaf(RILUtils.convertHalNetworkTypeBitMask(halRadioAccessFamilyBitmap));
        super.getPreferredNetworkTypeResponse(responseInfo, networkType);
    }

    public void startNetworkScanResponse_1_5(RadioResponseInfo responseInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(responseInfo);
        if (rr == null) {
            return;
        }
        NetworkScanResult nsr = null;
        if (responseInfo.error == 0) {
            nsr = new NetworkScanResult(1, 0, (List) null);
            sendMessageResponse(rr.mResult, nsr);
        }
        this.mMtkRil.processResponseDone(rr, responseInfo, nsr);
    }
}
