package com.mediatek.internal.telephony;

import android.content.Context;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import com.android.internal.telephony.DefaultPhoneNotifier;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class MtkPhoneNotifier extends DefaultPhoneNotifier {
    private static final String LOG_TAG = "MtkPhoneNotifr";
    private final int mFakeSub;

    public MtkPhoneNotifier(Context context) {
        super(context);
        this.mFakeSub = 2147483646;
        Rlog.d(LOG_TAG, "constructor");
        MtkTelephonyRegistryEx.init();
    }

    private String getServiceStateBrief(ServiceState ss) {
        return ss == null ? "n/a" : "voice=" + ss.getVoiceRegState() + ", data=" + ss.getDataRegState() + ", voice_type=" + ss.getVoiceNetworkType() + ", data_type=" + ss.getDataNetworkType();
    }

    public void notifyServiceState(Phone sender) {
        super.notifyServiceState(sender);
        if (sender.getSubId() <= -1) {
            notifyMtkServiceState(sender, sender.getServiceState());
        }
    }

    public void notifySignalStrength(Phone sender) {
        super.notifySignalStrength(sender);
        if (sender.getSubId() <= -1) {
            notifyMtkSignalStrength(sender, sender.getSignalStrength());
        }
    }

    public void notifyMtkServiceState(Phone sender, ServiceState mss) {
        ServiceState ss = mss;
        int phoneId = sender.getPhoneId();
        int subId = sender.getSubId() > -1 ? sender.getSubId() : 2147483646;
        if (ss == null) {
            ss = new ServiceState();
            ss.setStateOutOfService();
        }
        Rlog.d(LOG_TAG, "MtkPhoneNotifier notifyMtkServiceState phoneId:" + phoneId + " subId: " + subId + " ServiceState: " + getServiceStateBrief(ss));
        if (this.mTelephonyRegistryMgr != null) {
            this.mTelephonyRegistryMgr.notifyServiceStateChanged(phoneId, subId, ss);
        }
    }

    public void notifyMtkSignalStrength(Phone sender, SignalStrength ss) {
        int phoneId = sender.getPhoneId();
        int subId = sender.getSubId() > -1 ? sender.getSubId() : 2147483646;
        Rlog.d(LOG_TAG, "MtkPhoneNotifier notifyMtkSignalStrength phoneId:" + phoneId + " subId: " + subId + " signal: " + ss);
        if (this.mTelephonyRegistryMgr != null) {
            this.mTelephonyRegistryMgr.notifySignalStrengthChanged(phoneId, subId, ss);
        }
    }
}
