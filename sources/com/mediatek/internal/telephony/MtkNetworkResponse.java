package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.network.OperatorInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.NetworkResponse;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ServiceStateTracker;

/* JADX INFO: loaded from: classes.dex */
public class MtkNetworkResponse extends NetworkResponse {
    private MtkRIL mMtkRil;

    public MtkNetworkResponse(MtkRIL ril) {
        super(ril);
        this.mMtkRil = ril;
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void getAvailableNetworksResponse(RadioResponseInfo responseInfo, OperatorInfo[] networkInfos) {
        int mLac = -1;
        Phone phone = PhoneFactory.getPhone(this.mMtkRil.mInstanceId.intValue());
        if (phone != null) {
            ServiceStateTracker sst = phone.getServiceStateTracker();
            ServiceState serviceState = sst.mSS;
            mLac = ((MtkServiceStateTracker) sst).getLac();
        }
        for (OperatorInfo info : networkInfos) {
            String mccmnc = info.operatorNumeric;
            MtkRIL mtkRIL = this.mMtkRil;
            info.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, mLac);
            MtkRIL mtkRIL2 = this.mMtkRil;
            info.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, mLac);
        }
        super.getAvailableNetworksResponse(responseInfo, networkInfos);
    }
}
