package com.mediatek.internal.telephony.uicc;

import android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public abstract class IMtkRsuSml {
    protected static final int SIMLOCK_ERROR = 1;
    protected static final int SIMLOCK_SUCCESS = 0;
    private static final String TAG = "IMtkRsuSml";

    public int remoteSimlockGetSimlockStatusEx(Object cb) {
        loge("default remoteSimlockGetSimlockStatusEx, user should override this API");
        return 1;
    }

    private void loge(String s) {
        Rlog.e(TAG, "[RSU-SIMLOCK] " + s);
    }
}
