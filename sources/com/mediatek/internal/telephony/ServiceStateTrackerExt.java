package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.Rlog;
import android.telephony.ServiceState;

/* JADX INFO: loaded from: classes.dex */
public class ServiceStateTrackerExt implements IServiceStateTrackerExt {
    static final String TAG = "SSTExt";
    protected Context mContext;

    public ServiceStateTrackerExt() {
    }

    public ServiceStateTrackerExt(Context context) {
        this.mContext = context;
    }

    @Override // com.mediatek.internal.telephony.IServiceStateTrackerExt
    public String onUpdateSpnDisplay(String plmn, ServiceState ss, int phoneId) {
        return plmn;
    }

    public void log(String text) {
        Rlog.d(TAG, text);
    }

    @Override // com.mediatek.internal.telephony.IServiceStateTrackerExt
    public boolean allowSpnDisplayed() {
        return true;
    }

    @Override // com.mediatek.internal.telephony.IServiceStateTrackerExt
    public boolean modifyRsrpThresholdsForRsrpBoost(PersistableBundle config, int cellIdentityType, int rsrpBoost) {
        return false;
    }
}
