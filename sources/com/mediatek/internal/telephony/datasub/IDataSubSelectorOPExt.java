package com.mediatek.internal.telephony.datasub;

import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public interface IDataSubSelectorOPExt {
    void handleAirPlaneModeOff(Intent intent);

    void handleBootCompleteAction();

    void handleConnectivityAction();

    void handleDefaultDataChanged(Intent intent);

    void handleSimStateChanged(Intent intent);

    void handleSubinfoRecordUpdated(Intent intent);

    void init(DataSubSelector dataSubSelector, ISimSwitchForDSSExt iSimSwitchForDSSExt);

    void subSelector(Intent intent);
}
