package com.mediatek.internal.telephony;

import android.os.PersistableBundle;
import android.telephony.ServiceState;

/* JADX INFO: loaded from: classes.dex */
public interface IServiceStateTrackerExt {
    boolean allowSpnDisplayed();

    boolean modifyRsrpThresholdsForRsrpBoost(PersistableBundle persistableBundle, int i, int i2);

    String onUpdateSpnDisplay(String str, ServiceState serviceState, int i);
}
