package com.mediatek.internal.telephony;

import com.android.internal.telephony.CallManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkCallManager extends CallManager {
    protected boolean onSetIncomingRejected(boolean incomingRejected) {
        return true;
    }
}
