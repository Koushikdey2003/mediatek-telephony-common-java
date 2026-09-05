package com.mediatek.internal.telephony;

import android.content.Context;
import com.mediatek.internal.telephony.datasub.DataSubSelectorOpExt;
import com.mediatek.internal.telephony.datasub.IDataSubSelectorOPExt;
import com.mediatek.internal.telephony.datasub.ISimSwitchForDSSExt;
import com.mediatek.internal.telephony.datasub.SimSwitchForDSSExt;
import com.mediatek.internal.telephony.devreg.DefaultDeviceRegisterExt;
import com.mediatek.internal.telephony.devreg.DeviceRegisterController;
import com.mediatek.internal.telephony.devreg.IDeviceRegisterExt;
import com.mediatek.internal.telephony.selfactivation.ISelfActivation;
import com.mediatek.internal.telephony.selfactivation.SelfActivationDefault;

/* JADX INFO: loaded from: classes.dex */
public class OpTelephonyCustomizationFactoryBase {
    public IServiceStateTrackerExt makeServiceStateTrackerExt(Context context) {
        return new ServiceStateTrackerExt(context);
    }

    public IDeviceRegisterExt makeDeviceRegisterExt(Context context, DeviceRegisterController controller) {
        return new DefaultDeviceRegisterExt(context, controller);
    }

    public ISelfActivation makeSelfActivationInstance(int phoneId) {
        ISelfActivation instance = new SelfActivationDefault(phoneId);
        return instance;
    }

    public IDataSubSelectorOPExt makeDataSubSelectorOPExt(Context context) {
        return new DataSubSelectorOpExt(context);
    }

    public ISimSwitchForDSSExt makeSimSwitchForDSSOPExt(Context context) {
        return new SimSwitchForDSSExt(context);
    }
}
