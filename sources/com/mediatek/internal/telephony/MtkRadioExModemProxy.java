package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.telephony.Rlog;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.RILUtils;
import java.util.ArrayList;
import java.util.Arrays;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExModemProxy extends MtkRadioExServiceProxy {
    private static final String TAG = "MtkRadioExModemProxy";
    private volatile IMtkRadioExModem mModemProxyMtk = null;

    public HalVersion setAidl(HalVersion halVersion, IMtkRadioExModem modem) {
        this.mHalVersion = halVersion;
        this.mModemProxyMtk = modem;
        this.mIsAidl = true;
        try {
            int version = modem.getInterfaceVersion();
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

    public IMtkRadioExModem getAidl() {
        return this.mModemProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mModemProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mModemProxyMtk == null;
    }

    public void setModemPower(int serial, boolean isOn) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.setModemPower(serial, isOn, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setModemPower(serial, isOn);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setModemPower(serial, isOn);
        }
    }

    public void modifyModemType(int serial, int applyType, int modemType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.modifyModemType(serial, applyType, modemType, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).modifyModemType(serial, applyType, modemType);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).modifyModemType(serial, applyType, modemType);
        }
    }

    public void restartRILD(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.restartRILD(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).restartRILD(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).restartRILD(serial);
        }
    }

    public void setTrm(int serial, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.setTrm(serial, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setTrm(serial, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setTrm(serial, mode);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.responseAcknowledgementMtk();
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        }
    }

    public void sendRequestRaw(int serial, byte[] data) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendRequestRaw(serial, data, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendRequestRaw(serial, RILUtils.primitiveArrayToArrayList(data));
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendRequestRaw(serial, RILUtils.primitiveArrayToArrayList(data));
        }
    }

    public void sendRequestStrings(int serial, String[] strings) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendRequestStrings(serial, strings, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendRequestStrings(serial, new ArrayList<>(Arrays.asList(strings)));
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendRequestStrings(serial, new ArrayList<>(Arrays.asList(strings)));
        }
    }

    public void sendSarIndicator(int serial, int sarCmdType, String sarParameter) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendSarIndicator(serial, sarCmdType, sarParameter, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendSarIndicator(serial, sarCmdType, sarParameter);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendSarIndicator(serial, sarCmdType, sarParameter);
        }
    }

    public void setTxPower(int serial, int power) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.setTxPower(serial, power, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setTxPower(serial, power);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setTxPower(serial, power);
        }
    }

    public void setTxPowerStatus(int serial, int enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.setTxPowerStatus(serial, enable, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setTxPowerStatus(serial, enable);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setTxPowerStatus(serial, enable);
        }
    }

    public void setVendorSetting(int serial, int setting, String value) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.setVendorSetting(serial, setting, value, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setVendorSetting(serial, setting, value);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setVendorSetting(serial, setting, value);
        }
    }

    public void sendWifiEnabled(int serial, String ifName, int isWifiEnabled) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendWifiEnabled(serial, ifName, isWifiEnabled, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendWifiEnabled(serial, ifName, isWifiEnabled);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendWifiEnabled(serial, ifName, isWifiEnabled);
        }
    }

    public void sendWifiAssociated(int i, String str, boolean z, String str2, String str3, int i2, String str4) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendWifiAssociated(i, str, z ? 1 : 0, str2, str3, i2, str4, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendWifiAssociated(i, str, z ? 1 : 0, str2, str3, i2);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendWifiAssociated(i, str, z ? 1 : 0, str2, str3, i2);
        }
    }

    public void sendWifiIpAddress(int serial, String ifName, String ipv4Addr, String ipv6Addr, int ipv4PrefixLen, int ipv6PrefixLen, String ipv4Gateway, String ipv6Gateway, int dnsCount, String dnsAddresses) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendWifiIpAddress(serial, ifName, ipv4Addr, ipv6Addr, ipv4PrefixLen, ipv6PrefixLen, ipv4Gateway, ipv6Gateway, dnsCount, dnsAddresses, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendWifiIpAddress(serial, ifName, ipv4Addr, ipv6Addr, ipv4PrefixLen, ipv6PrefixLen, ipv4Gateway, ipv6Gateway, dnsCount, dnsAddresses);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendWifiIpAddress(serial, ifName, ipv4Addr, ipv6Addr, ipv4PrefixLen, ipv6PrefixLen, ipv4Gateway, ipv6Gateway, dnsCount, dnsAddresses);
        }
    }

    public void registerCellQltyReport(int serial, String registerQuality, String type, String thresholdValues, String triggerTime) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.registerCellQltyReport(serial, registerQuality, type, thresholdValues, triggerTime, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).registerCellQltyReport(serial, registerQuality, type, thresholdValues, triggerTime);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).registerCellQltyReport(serial, registerQuality, type, thresholdValues, triggerTime);
        }
    }

    public void getEngineeringModeInfo(int serial, int index) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.getEngineeringModeInfo(serial, index, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getEngineeringModeInfo(serial, index);
        }
    }

    public void sendEmbmsAtCommand(int serial, String data) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.sendEmbmsAtCommand(serial, data, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendEmbmsAtCommand(serial, data);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendEmbmsAtCommand(serial, data);
        }
    }

    public void triggerModeSwitchByEcc(int serial, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mModemProxyMtk.triggerModeSwitchByEcc(serial, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).triggerModeSwitchByEcc(serial, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).triggerModeSwitchByEcc(serial, mode);
        }
    }
}
