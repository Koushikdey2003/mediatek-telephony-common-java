package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.UiccSlot;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccSlot extends UiccSlot {
    static final String LOG_TAG = "MtkUiccSlot";

    public MtkUiccSlot(Context c, boolean isActive) {
        super(c, isActive);
        Rlog.d(LOG_TAG, "Creating");
    }

    public boolean isEuicc() {
        return super.isEuicc() && !TextUtils.isEmpty(getEid());
    }
}
