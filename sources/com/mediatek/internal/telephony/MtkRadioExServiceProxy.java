package com.mediatek.internal.telephony;

import android.internal.hidl.base.V1_0.IBase;
import android.os.RemoteException;
import com.android.internal.telephony.HalVersion;

/* JADX INFO: loaded from: classes.dex */
public abstract class MtkRadioExServiceProxy {
    boolean mIsAidl;
    HalVersion mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
    volatile IBase mRadioProxyMtk = null;

    abstract void clear();

    abstract boolean isEmpty();

    abstract void responseAcknowledgementMtk() throws RemoteException;

    public boolean isAidl() {
        return this.mIsAidl;
    }

    public void setHidl(HalVersion halVersion, IBase radio) {
        this.mHalVersion = halVersion;
        this.mRadioProxyMtk = radio;
        this.mIsAidl = false;
    }

    public IBase getHidl() {
        return this.mRadioProxyMtk;
    }
}
