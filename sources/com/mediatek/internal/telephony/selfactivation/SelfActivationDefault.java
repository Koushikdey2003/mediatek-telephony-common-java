package com.mediatek.internal.telephony.selfactivation;

import android.content.Context;
import android.os.Bundle;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

/* JADX INFO: loaded from: classes.dex */
public class SelfActivationDefault implements ISelfActivation {
    private static final String TAG = "SelfActivationDefault";
    protected int mPhoneId;
    protected Context mContext = null;
    protected CommandsInterface mCi = null;

    public SelfActivationDefault(int phoneId) {
        this.mPhoneId = -1;
        Rlog.d(TAG, "init");
        this.mPhoneId = phoneId;
    }

    @Override // com.mediatek.internal.telephony.selfactivation.ISelfActivation
    public int selfActivationAction(int action, Bundle param) {
        return -1;
    }

    @Override // com.mediatek.internal.telephony.selfactivation.ISelfActivation
    public int getSelfActivateState() {
        return 0;
    }

    @Override // com.mediatek.internal.telephony.selfactivation.ISelfActivation
    public int getPCO520State() {
        return 0;
    }

    @Override // com.mediatek.internal.telephony.selfactivation.ISelfActivation
    public ISelfActivation setContext(Context context) {
        this.mContext = context;
        return this;
    }

    @Override // com.mediatek.internal.telephony.selfactivation.ISelfActivation
    public ISelfActivation setCommandsInterface(CommandsInterface ci) {
        this.mCi = ci;
        return this;
    }

    @Override // com.mediatek.internal.telephony.selfactivation.ISelfActivation
    public ISelfActivation buildParams() {
        return this;
    }
}
