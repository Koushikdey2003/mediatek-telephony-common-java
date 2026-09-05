package com.mediatek.internal.telephony.datasub;

/* JADX INFO: loaded from: classes.dex */
public interface ISimSwitchForDSSExt {
    boolean checkCapSwitch(int i);

    void init(DataSubSelector dataSubSelector);

    int isNeedSimSwitch();
}
