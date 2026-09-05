package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.ResultException;

/* JADX INFO: loaded from: classes.dex */
public class MtkResultException extends ResultException {
    MtkResultException(ResultCode result, int additionalInfo) {
        super(result);
        this.mResult = result;
        this.mExplanation = "";
        if (additionalInfo < 0) {
            throw new AssertionError("Additional info must be greater than zero!");
        }
        this.mAdditionalInfo = additionalInfo;
    }

    MtkResultException(ResultCode result, int additionalInfo, String explanation) {
        this(result, additionalInfo);
        this.mExplanation = explanation;
    }
}
