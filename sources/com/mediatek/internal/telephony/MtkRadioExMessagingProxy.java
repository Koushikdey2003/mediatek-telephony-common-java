package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.telephony.Rlog;
import com.android.internal.telephony.HalVersion;
import mediatek.telephony.MtkSmsParameters;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessaging;
import vendor.mediatek.hardware.mtkradioex.messaging.SmsParams;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExMessagingProxy extends MtkRadioExServiceProxy {
    private static final int CLIENT_RILJ = 0;
    private static final String TAG = "MtkRadioExMessagingProxy";
    private volatile IMtkRadioExMessaging mMessagingProxyMtk = null;

    public HalVersion setAidl(HalVersion halVersion, IMtkRadioExMessaging messaging) {
        this.mHalVersion = halVersion;
        this.mMessagingProxyMtk = messaging;
        this.mIsAidl = true;
        try {
            int version = messaging.getInterfaceVersion();
            HalVersion newHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_4_0;
            Rlog.d(TAG, "AIDL version=" + version + ", halVersion=" + newHalVersion);
            if (this.mHalVersion.less(newHalVersion)) {
                this.mHalVersion = newHalVersion;
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setAidl: " + e);
        }
        Rlog.d(TAG, "AIDL initialized mHalVersion=" + this.mHalVersion);
        return this.mHalVersion;
    }

    public IMtkRadioExMessaging getAidl() {
        return this.mMessagingProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mMessagingProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mMessagingProxyMtk == null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.responseAcknowledgementMtk();
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        }
    }

    public void getSmsParameters(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.getSmsParameters(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getSmsParameters(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getSmsParameters(serial);
        }
    }

    public void setSmsParameters(int serial, MtkSmsParameters params) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            SmsParams smsp = new SmsParams();
            smsp.dcs = params.dcs;
            smsp.format = params.format;
            smsp.pid = params.pid;
            smsp.vp = params.vp;
            this.mMessagingProxyMtk.setSmsParameters(serial, smsp, 0);
            return;
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            vendor.mediatek.hardware.mtkradioex.V3_0.SmsParams smsp2 = new vendor.mediatek.hardware.mtkradioex.V3_0.SmsParams();
            smsp2.dcs = params.dcs;
            smsp2.format = params.format;
            smsp2.pid = params.pid;
            smsp2.vp = params.vp;
            ((IMtkRadioEx) this.mRadioProxyMtk).setSmsParameters(serial, smsp2);
            return;
        }
        vendor.mediatek.hardware.mtkradioex.V2_0.SmsParams smspV2 = new vendor.mediatek.hardware.mtkradioex.V2_0.SmsParams();
        smspV2.dcs = params.dcs;
        smspV2.format = params.format;
        smspV2.pid = params.pid;
        smspV2.vp = params.vp;
        ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setSmsParameters(serial, smspV2);
    }

    public void setEtws(int serial, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.setEtws(serial, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setEtws(serial, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setEtws(serial, mode);
        }
    }

    public void removeCbMsg(int serial, int channelId, int serialId) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.removeCbMsg(serial, channelId, serialId, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).removeCbMsg(serial, channelId, serialId);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).removeCbMsg(serial, channelId, serialId);
        }
    }

    public void getSmsMemStatus(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.getSmsMemStatus(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getSmsMemStatus(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getSmsMemStatus(serial);
        }
    }

    public void getSmsRuimMemoryStatus(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.getSmsRuimMemoryStatus(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getSmsRuimMemoryStatus(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getSmsRuimMemoryStatus(serial);
        }
    }

    public void setGsmBroadcastLangs(int serial, String lang) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.setGsmBroadcastLangs(serial, lang, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setGsmBroadcastLangs(serial, lang);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setGsmBroadcastLangs(serial, lang);
        }
    }

    public void getGsmBroadcastLangs(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.getGsmBroadcastLangs(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getGsmBroadcastLangs(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getGsmBroadcastLangs(serial);
        }
    }

    public void getGsmBroadcastActivation(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mMessagingProxyMtk.getGsmBroadcastActivation(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getGsmBroadcastActivation(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getGsmBroadcastActivation(serial);
        }
    }
}
