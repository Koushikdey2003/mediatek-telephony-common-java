package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.SignalStrengthUpdateRequest;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SignalStrengthController;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class MtkSignalStrengthController extends SignalStrengthController {
    private static final String LOG_TAG = "MtkSSCtr";
    private static ReqData sLastReqDataForSignalStrengthUpdateByBT = new ReqData(-1, -1);

    private static class ReqData {
        private int phoneId;
        private int subId;

        public ReqData(int pId, int sId) {
            this.phoneId = pId;
            this.subId = sId;
        }
    }

    public MtkSignalStrengthController(Phone phone) {
        super(phone);
    }

    protected boolean onSignalStrengthResult(AsyncResult ar) {
        if (this.mSignalStrength != null) {
            logd("old:{level:" + this.mSignalStrength.getLevel() + ", raw:" + this.mSignalStrength.toString() + "}");
        }
        boolean ssChanged = super.onSignalStrengthResult(ar);
        if (this.mSignalStrength != null) {
            logd("new:{level:" + this.mSignalStrength.getLevel() + ", raw:" + this.mSignalStrength.toString() + "}, ");
        }
        return ssChanged;
    }

    private void logd(String s) {
        Rlog.d(LOG_TAG, "[" + this.mPhone.getPhoneId() + "] " + s);
    }

    public void setSignalStrengthUpdateRequest(int subId, int callingUid, SignalStrengthUpdateRequest request, Message onCompleted) {
        super.setSignalStrengthUpdateRequest(subId, callingUid, request, onCompleted);
        if (callingUid == 1002) {
            sLastReqDataForSignalStrengthUpdateByBT.phoneId = this.mPhone.getPhoneId();
            sLastReqDataForSignalStrengthUpdateByBT.subId = subId;
        }
    }

    public void clearSignalStrengthUpdateRequest(int subId, int callingUid, SignalStrengthUpdateRequest request, Message onCompleted) {
        super.clearSignalStrengthUpdateRequest(subId, callingUid, request, onCompleted);
        if (callingUid == 1002) {
            if (sLastReqDataForSignalStrengthUpdateByBT.phoneId != -1 && sLastReqDataForSignalStrengthUpdateByBT.phoneId != this.mPhone.getPhoneId()) {
                logd("sLastReqDataForSignalStrengthUpdateByBT, phoneId = " + sLastReqDataForSignalStrengthUpdateByBT.phoneId + ", subId = " + sLastReqDataForSignalStrengthUpdateByBT.subId + ", clear it");
                Phone phone = PhoneFactory.getPhone(sLastReqDataForSignalStrengthUpdateByBT.phoneId);
                if (phone != null) {
                    phone.getSignalStrengthController().clearSignalStrengthUpdateRequest(sLastReqDataForSignalStrengthUpdateByBT.subId, callingUid, request, (Message) null);
                }
            }
            sLastReqDataForSignalStrengthUpdateByBT.phoneId = -1;
            sLastReqDataForSignalStrengthUpdateByBT.subId = -1;
        }
    }

    public boolean shouldHonorSystemThresholds() {
        boolean disableThresholds = this.mCarrierConfig.getBoolean("mtk_disable_signal_strength_thresholds_bool", false);
        if (disableThresholds) {
            logd("shouldHonorSystemThresholds: return false due to carrier config");
            return false;
        }
        return super.shouldHonorSystemThresholds();
    }
}
