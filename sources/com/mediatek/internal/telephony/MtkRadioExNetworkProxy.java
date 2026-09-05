package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.telephony.Rlog;
import com.android.internal.telephony.HalVersion;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExNetworkProxy extends MtkRadioExServiceProxy {
    private static final String TAG = "MtkRadioExNetworkProxy";
    private volatile IMtkRadioExNetwork mNetworkProxyMtk = null;

    public HalVersion setAidl(HalVersion halVersion, IMtkRadioExNetwork network) {
        this.mHalVersion = halVersion;
        this.mNetworkProxyMtk = network;
        this.mIsAidl = true;
        try {
            int version = network.getInterfaceVersion();
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

    public IMtkRadioExNetwork getAidl() {
        return this.mNetworkProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mNetworkProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mNetworkProxyMtk == null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.responseAcknowledgementMtk();
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        }
    }

    public void abortFemtocellList(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.abortFemtocellList(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).abortFemtocellList(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).abortFemtocellList(serial);
        }
    }

    public void cancelAvailableNetworks(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.cancelAvailableNetworks(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).cancelAvailableNetworks(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).cancelAvailableNetworks(serial);
        }
    }

    public void cfgA2offset(int serial, int offset, int threshBound) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.cfgA2offset(serial, offset, threshBound, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).cfgA2offset(serial, offset, threshBound);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).cfgA2offset(serial, offset, threshBound);
        }
    }

    public void cfgB1offset(int serial, int offset, int threshBound) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.cfgB1offset(serial, offset, threshBound, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).cfgB1offset(serial, offset, threshBound);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).cfgB1offset(serial, offset, threshBound);
        }
    }

    public void clearLteAvailableFile(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.clearLteAvailableFile(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).clearLteAvailableFile(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).clearLteAvailableFile(serial);
        }
    }

    public void deactivateNrScgCommunication(int serial, boolean deactivate, boolean allowSCGAdd) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.deactivateNrScgCommunication(serial, deactivate, allowSCGAdd, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).deactivateNrScgCommunication(serial, deactivate, allowSCGAdd);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).deactivateNrScgCommunication(serial, deactivate, allowSCGAdd);
        }
    }

    public void setCarrierAggregationMode(int serial, int mode, int option, int linkType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setCarrierAggregationMode(serial, mode, option, linkType, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).disableAllCALinks(serial, linkType);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).disableAllCALinks(serial, linkType);
        }
    }

    public void enableCAPlusBandWidthFilter(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.enableCAPlusBandWidthFilter(serial, enable, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).enableCAPlusBandWidthFilter(serial, enable);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).enableCAPlusBandWidthFilter(serial, enable);
        }
    }

    public void enableSCGfailure(int serial, boolean enable, int T1, int P1, int T2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.enableSCGfailure(serial, enable, T1, P1, T2, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).enableSCGfailure(serial, enable, T1, P1, T2);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).enableSCGfailure(serial, enable, T1, P1, T2);
        }
    }

    public void get4x4MimoEnabled(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.get4x4MimoEnabled(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).get4x4MimoEnabled(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).get4x4MimoEnabled(serial);
        }
    }

    public void getAllBandMode(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getAllBandMode(serial, 0);
        } else {
            if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).getAllBandMode(serial);
                return;
            }
            throw new RuntimeException("getAllBandMode isn't supported before 3.0.");
        }
    }

    public void getApcInfo(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getApcInfo(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getApcInfo(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getApcInfo(serial);
        }
    }

    public void getAvailableNetworksWithAct(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getAvailableNetworksWithAct(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getAvailableNetworksWithAct(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getAvailableNetworksWithAct(serial);
        }
    }

    public void getBandMode(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getBandMode(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getBandMode(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getBandMode(serial);
        }
    }

    public void getBandPriorityList(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getBandPriorityList(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getBandPriorityList(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getBandPriorityList(serial);
        }
    }

    public void getCALinkCapabilityList(int serial, int linkType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getCALinkCapabilityList(serial, linkType, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getCALinkCapabilityList(serial, linkType);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getCALinkCapabilityList(serial, linkType);
        }
    }

    public void getCALinkEnableStatus(int serial, String bandsCombo, int linkType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getCALinkEnableStatus(serial, bandsCombo, linkType, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getCALinkEnableStatus(serial, bandsCombo, linkType);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getCALinkEnableStatus(serial, bandsCombo, linkType);
        }
    }

    public void getCaBandMode(int serial, int primaryBandId) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getCaBandMode(serial, primaryBandId, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getCaBandMode(serial, primaryBandId);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getCaBandMode(serial, primaryBandId);
        }
    }

    public void getCampedFemtoCellInfo(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getCampedFemtoCellInfo(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getCampedFemtoCellInfo(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getCampedFemtoCellInfo(serial);
        }
    }

    public void getCurrentPOLList(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getCurrentPOLList(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getCurrentPOLList(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getCurrentPOLList(serial);
        }
    }

    public void getDeactivateNrScgCommunication(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getDeactivateNrScgCommunication(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getDeactivateNrScgCommunication(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getDeactivateNrScgCommunication(serial);
        }
    }

    public void getDisable2G(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getDisable2G(serial, 0);
        } else {
            if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).getDisable2G(serial);
                return;
            }
            throw new RuntimeException("getDisable2G isn't supported before 3.0.");
        }
    }

    public void getFemtocellList(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getFemtocellList(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getFemtocellList(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getFemtocellList(serial);
        }
    }

    public void getIWlanRegistrationState(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getIWlanRegistrationState(serial, 0);
        } else {
            if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).getIWlanRegistrationState(serial);
                return;
            }
            throw new RuntimeException("getIWlanRegistrationState isn't supported before 3.0.");
        }
    }

    public void getLte1xRttCellList(int serial, boolean available) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getLte1xRttCellList(serial, available, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getLte1xRttCellList(serial, available);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getLte1xRttCellList(serial, available);
        }
    }

    public void getLteBsrTimer(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getLteBsrTimer(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getLteBsrTimer(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getLteBsrTimer(serial);
        }
    }

    public void getLteData(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getLteData(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getLteData(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getLteData(serial);
        }
    }

    public void getLteRRCState(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getLteRRCState(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getLteRRCState(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getLteRRCState(serial);
        }
    }

    public void getLteReleaseVersion(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getLteReleaseVersion(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getLteReleaseVersion(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getLteReleaseVersion(serial);
        }
    }

    public void getLteScanDuration(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getLteScanDuration(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getLteScanDuration(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getLteScanDuration(serial);
        }
    }

    public void getPOLCapability(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getPOLCapability(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getPOLCapability(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getPOLCapability(serial);
        }
    }

    public void getPlmnNameFromSE13Table(int serial, int mcc, int mnc) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getPlmnNameFromSE13Table(serial, mcc, mnc, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getPlmnNameFromSE13Table(serial, mcc, mnc);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getPlmnNameFromSE13Table(serial, mcc, mnc);
        }
    }

    public void getQamEnabled(int serial, boolean ulOrDl) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getQamEnabled(serial, ulOrDl, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getQamEnabled(serial, ulOrDl);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getQamEnabled(serial, ulOrDl);
        }
    }

    public void getRoamingEnable(int serial, int phoneId) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getRoamingEnable(serial, phoneId, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getRoamingEnable(serial, phoneId);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getRoamingEnable(serial, phoneId);
        }
    }

    public void getSignalStrengthWithWcdmaEcio(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getSignalStrengthWithWcdmaEcio(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getSignalStrengthWithWcdmaEcio(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getSignalStrengthWithWcdmaEcio(serial);
        }
    }

    public void getSuggestedPlmnList(int serial, int rat, int num, int timer) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getSuggestedPlmnList(serial, rat, num, timer, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getSuggestedPlmnList(serial, rat, num, timer);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getSuggestedPlmnList(serial, rat, num, timer);
        }
    }

    public void getTOEInfo(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getTOEInfo(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getTOEInfo(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getTOEInfo(serial);
        }
    }

    public void getTm9Enabled(int serial, boolean fddOrTdd) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.getTm9Enabled(serial, fddOrTdd, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getTm9Enabled(serial, fddOrTdd);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).getTm9Enabled(serial, fddOrTdd);
        }
    }

    public void queryFemtoCellSystemSelectionMode(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.queryFemtoCellSystemSelectionMode(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).queryFemtoCellSystemSelectionMode(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).queryFemtoCellSystemSelectionMode(serial);
        }
    }

    public void selectFemtocell(int serial, String operatorNumeric, String act, String csgId) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.selectFemtocell(serial, operatorNumeric, act, csgId, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).selectFemtocell(serial, operatorNumeric, act, csgId);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).selectFemtocell(serial, operatorNumeric, act, csgId);
        }
    }

    public void set4x4MimoEnabled(int serial, int enabled_bitmask) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.set4x4MimoEnabled(serial, enabled_bitmask, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).set4x4MimoEnabled(serial, enabled_bitmask);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).set4x4MimoEnabled(serial, enabled_bitmask);
        }
    }

    public void setApcMode(int serial, int mode, int reportMode, int interval) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setApcMode(serial, mode, reportMode, interval, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setApcMode(serial, mode, reportMode, interval);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setApcMode(serial, mode, reportMode, interval);
        }
    }

    public void setBandPriorityList(int serial, int[] bandPriList) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setBandPriorityList(serial, bandPriList, 0);
            return;
        }
        ArrayList<Integer> intList = new ArrayList<>(bandPriList.length);
        for (int i : bandPriList) {
            intList.add(Integer.valueOf(i));
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setBandPriorityList(serial, intList);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setBandPriorityList(serial, intList);
        }
    }

    public void setBgsrchDeltaSleepTimer(int serial, int sleepDuration) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setBgsrchDeltaSleepTimer(serial, sleepDuration, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setBgsrchDeltaSleepTimer(serial, sleepDuration);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setBgsrchDeltaSleepTimer(serial, sleepDuration);
        }
    }

    public void setCALinkEnableStatus(int serial, boolean status, String bandsCombo, int linkType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setCALinkEnableStatus(serial, status, bandsCombo, linkType, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setCALinkEnableStatus(serial, status, bandsCombo, linkType);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setCALinkEnableStatus(serial, status, bandsCombo, linkType);
        }
    }

    public void setDisable2G(int serial, boolean mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setDisable2G(serial, mode, 0);
        } else {
            if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).setDisable2G(serial, mode);
                return;
            }
            throw new RuntimeException("setDisable2G isn't supported before 3.0.");
        }
    }

    public void setFemtoCellSystemSelectionMode(int serial, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setFemtoCellSystemSelectionMode(serial, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setFemtoCellSystemSelectionMode(serial, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setFemtoCellSystemSelectionMode(serial, mode);
        }
    }

    public void setLteBandEnableStatus(int serial, int bandId, boolean status) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setLteBandEnableStatus(serial, bandId, status, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setLteBandEnableStatus(serial, bandId, status);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setLteBandEnableStatus(serial, bandId, status);
        }
    }

    public void setLteBsrTimer(int serial, int timer) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setLteBsrTimer(serial, timer, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setLteBsrTimer(serial, timer);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setLteBsrTimer(serial, timer);
        }
    }

    public void setLteReleaseVersion(int serial, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setLteReleaseVersion(serial, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setLteReleaseVersion(serial, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setLteReleaseVersion(serial, mode);
        }
    }

    public void setLteScanDuration(int serial, int duration) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setLteScanDuration(serial, duration, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setLteScanDuration(serial, duration);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setLteScanDuration(serial, duration);
        }
    }

    public void setNROption(int serial, int option) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setNROption(serial, option, 0);
        } else {
            if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).setNROption(serial, option);
                return;
            }
            throw new RuntimeException("setNROption isn't supported before 3.0.");
        }
    }

    public void setNetworkSelectionModeManualWithAct(int serial, String operatorNumeric, String act, String mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setNetworkSelectionModeManualWithAct(serial, operatorNumeric, act, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setNetworkSelectionModeManualWithAct(serial, operatorNumeric, act, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setNetworkSelectionModeManualWithAct(serial, operatorNumeric, act, mode);
        }
    }

    public void setNrBandMode(int serial, ArrayList<Integer> saEnable, ArrayList<Integer> saDisable, ArrayList<Integer> nsaEnable, ArrayList<Integer> nsaDisable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (!isAidl()) {
            if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).setNrBandMode(serial, saEnable, saDisable, nsaEnable, nsaDisable);
                return;
            }
            return;
        }
        int[] saEnableList = new int[saEnable.size()];
        for (int i = 0; i < saEnable.size(); i++) {
            saEnableList[i] = saEnable.get(i).intValue();
        }
        int i2 = saDisable.size();
        int[] saDisableList = new int[i2];
        for (int i3 = 0; i3 < saDisable.size(); i3++) {
            saDisableList[i3] = saDisable.get(i3).intValue();
        }
        int i4 = nsaEnable.size();
        int[] nsaEnableList = new int[i4];
        for (int i5 = 0; i5 < nsaEnable.size(); i5++) {
            nsaEnableList[i5] = nsaEnable.get(i5).intValue();
        }
        int i6 = nsaDisable.size();
        int[] nsaDisableList = new int[i6];
        for (int i7 = 0; i7 < nsaDisable.size(); i7++) {
            nsaDisableList[i7] = nsaDisable.get(i7).intValue();
        }
        this.mNetworkProxyMtk.setNrBandMode(serial, saEnableList, saDisableList, nsaEnableList, nsaDisableList, 0);
    }

    public void setPOLEntry(int serial, int index, String numeric, int nAct) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setPOLEntry(serial, index, numeric, nAct, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setPOLEntry(serial, index, numeric, nAct);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setPOLEntry(serial, index, numeric, nAct);
        }
    }

    public void setQamEnabled(int serial, boolean ulOrDl, boolean enabled) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setQamEnabled(serial, ulOrDl, enabled, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setQamEnabled(serial, ulOrDl, enabled);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setQamEnabled(serial, ulOrDl, enabled);
        }
    }

    public void setSearchRat(int serial, int[] rat) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setSearchRat(serial, rat, 0);
            return;
        }
        ArrayList<Integer> intList = new ArrayList<>(rat.length);
        for (int i : rat) {
            intList.add(Integer.valueOf(i));
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setSearchRat(serial, intList);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setSearchRat(serial, intList);
        }
    }

    public void setSearchStoredFreqInfo(int serial, int operation, int plmn_id, int rat, int[] freq) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setSearchStoredFreqInfo(serial, operation, plmn_id, rat, freq, 0);
            return;
        }
        ArrayList<Integer> intList = new ArrayList<>(freq.length);
        for (int i : freq) {
            intList.add(Integer.valueOf(i));
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setSearchStoredFreqInfo(serial, operation, plmn_id, rat, intList);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setSearchStoredFreqInfo(serial, operation, plmn_id, rat, intList);
        }
    }

    public void setServiceStateToModem(int serial, int voiceRegState, int dataRegState, int voiceRoamingType, int dataRoamingType, int rilVoiceRegState, int rilDataRegState) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setServiceStateToModem(serial, voiceRegState, dataRegState, voiceRoamingType, dataRoamingType, rilVoiceRegState, rilDataRegState, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setServiceStateToModem(serial, voiceRegState, dataRegState, voiceRoamingType, dataRoamingType, rilVoiceRegState, rilDataRegState);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setServiceStateToModem(serial, voiceRegState, dataRegState, voiceRoamingType, dataRoamingType, rilVoiceRegState, rilDataRegState);
        }
    }

    public void setTm9Enabled(int serial, boolean fddOrTdd, boolean enabled) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setTm9Enabled(serial, fddOrTdd, enabled, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setTm9Enabled(serial, fddOrTdd, enabled);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioEx) this.mRadioProxyMtk).setTm9Enabled(serial, fddOrTdd, enabled);
        }
    }

    public void setRoamingEnable(int serial, int[] config) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxyMtk.setRoamingEnable(serial, config, 0);
            return;
        }
        ArrayList<Integer> intList = new ArrayList<>(config.length);
        for (int i : config) {
            intList.add(Integer.valueOf(i));
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setRoamingEnable(serial, intList);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setRoamingEnable(serial, intList);
        }
    }

    public void disableNR(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            setNROption(serial, enable ? 7 : 1);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).disableNR(serial, enable);
        }
    }
}
