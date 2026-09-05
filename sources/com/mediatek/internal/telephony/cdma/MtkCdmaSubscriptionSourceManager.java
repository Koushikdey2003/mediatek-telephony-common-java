package com.mediatek.internal.telephony.cdma;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;

/* JADX INFO: loaded from: classes.dex */
public class MtkCdmaSubscriptionSourceManager extends CdmaSubscriptionSourceManager {
    static final String LOG_TAG = "MtkCdmaSSM";
    private int mActStatus;

    public MtkCdmaSubscriptionSourceManager(Context context, CommandsInterface ci) {
        super(context, ci);
        this.mActStatus = 0;
    }

    public int getActStatus() {
        log("getActStatus " + this.mActStatus);
        return this.mActStatus;
    }

    protected void setActStatus(int status) {
        this.mActStatus = status;
    }

    private void log(String s) {
        Rlog.d(LOG_TAG, s);
    }

    private void logw(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
