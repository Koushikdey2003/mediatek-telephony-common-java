package com.mediatek.internal.telephony;

import android.hardware.radio.data.OsAppId;
import android.net.LinkProperties;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.telephony.data.DataProfile;
import android.telephony.data.NetworkSliceInfo;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.RILUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExData;
import vendor.mediatek.hardware.mtkradioex.data.IpDescriptors;
import vendor.mediatek.hardware.mtkradioex.data.TrafficDescriptor;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExDataProxy extends MtkRadioExServiceProxy {
    private static final String TAG = "MtkRadioExDataProxy";
    private volatile IMtkRadioExData mDataProxyMtk = null;

    public HalVersion setAidl(HalVersion halVersion, IMtkRadioExData data) {
        this.mHalVersion = halVersion;
        this.mDataProxyMtk = data;
        this.mIsAidl = true;
        try {
            int version = data.getInterfaceVersion();
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

    public IMtkRadioExData getAidl() {
        return this.mDataProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mDataProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mDataProxyMtk == null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.responseAcknowledgementMtk();
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        }
    }

    public void resetMdDataRetryCount(int serial, String apnName) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.resetMdDataRetryCount(serial, apnName, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).resetMdDataRetryCount(serial, apnName);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).resetMdDataRetryCount(serial, apnName);
        }
    }

    public void dataConnectionAttach(int serial, int type) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.dataConnectionAttach(serial, type, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).dataConnectionAttach(serial, type);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).dataConnectionAttach(serial, type);
        }
    }

    public void dataConnectionDetach(int serial, int type) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.dataConnectionDetach(serial, type, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).dataConnectionDetach(serial, type);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).dataConnectionDetach(serial, type);
        }
    }

    public void resetAllConnections(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.resetAllConnections(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).resetAllConnections(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).resetAllConnections(serial);
        }
    }

    public void enableDsdaIndication(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.enableDsdaIndication(serial, enable, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).enableDsdaIndication(serial, enable);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).enableDsdaIndication(serial, enable);
        }
    }

    public void getDsdaStatus(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxyMtk.getDsdaStatus(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getDsdaStatus(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getDsdaStatus(serial);
        }
    }

    private static TrafficDescriptor convertToHalTrafficDescriptor(TrafficDescriptor td) {
        if (td == null) {
            return null;
        }
        TrafficDescriptor trafficDescriptor = new TrafficDescriptor();
        trafficDescriptor.dnn = td.getDnn();
        String osAppId = td.getOsAppId();
        if (osAppId == null) {
            trafficDescriptor.osAppId = null;
        } else {
            trafficDescriptor.osAppId = new OsAppId();
            try {
                trafficDescriptor.osAppId.osAppId = osAppId.getBytes("ISO-8859-1");
                Rlog.d(TAG, "[DBG]convertToHalTrafficDescriptor OsAppId getBytes: " + osAppId.getBytes("ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
                Rlog.w(TAG, "convertToHalTrafficDescriptor() UnsupportedEncodingException");
                trafficDescriptor.osAppId = null;
            }
        }
        if (td.getIpDescriptors() == null) {
            trafficDescriptor.ipDescriptors = null;
        } else {
            trafficDescriptor.ipDescriptors = new IpDescriptors();
            trafficDescriptor.ipDescriptors.ipv4 = td.getIpDescriptors().getIpv4();
            trafficDescriptor.ipDescriptors.maskV4 = td.getIpDescriptors().getMaskV4();
            trafficDescriptor.ipDescriptors.ipv6 = td.getIpDescriptors().getIpv6();
            trafficDescriptor.ipDescriptors.prefixLength = td.getIpDescriptors().getPrefixLength();
            trafficDescriptor.ipDescriptors.port = td.getIpDescriptors().getDestPort();
            trafficDescriptor.ipDescriptors.portStartRange = td.getIpDescriptors().getDestPortStartRange();
            trafficDescriptor.ipDescriptors.portEndRange = td.getIpDescriptors().getDestPortEndRange();
            trafficDescriptor.ipDescriptors.protocolId = td.getIpDescriptors().getProtocol();
        }
        trafficDescriptor.domainDescriptors = td.getDomainDescriptors();
        trafficDescriptor.connectionCapabilities = td.getConnectionCapabilities();
        return trafficDescriptor;
    }

    public void setupDataCallSlice(int serial, int accessNetwork, DataProfile dataProfileInfo, boolean isRoaming, boolean roamingAllowed, int reason, LinkProperties linkProperties, int pduSessionId, NetworkSliceInfo sliceInfo, boolean matchAllRuleAllowed, TrafficDescriptor trafficDescriptor, int responseMode) throws RemoteException {
        String[] dnsesArr;
        if (isEmpty()) {
            return;
        }
        ArrayList<String> dnses = new ArrayList<>();
        if (linkProperties != null) {
            dnsesArr = new String[linkProperties.getDnsServers().size()];
            for (int i = 0; i < linkProperties.getDnsServers().size(); i++) {
                dnses.add(linkProperties.getDnsServers().get(i).getHostAddress());
                dnsesArr[i] = linkProperties.getDnsServers().get(i).getHostAddress();
            }
        } else {
            dnsesArr = new String[0];
        }
        if (isAidl() && this.mDataProxyMtk.getInterfaceVersion() > 1) {
            DataProfile dp = new DataProfile.Builder().setType(dataProfileInfo.getType()).setPreferred(dataProfileInfo.isPreferred()).setApnSetting(dataProfileInfo.getApnSetting()).build();
            this.mDataProxyMtk.setupDataCallSlice(serial, accessNetwork, RILUtils.convertToHalDataProfile(dp), roamingAllowed, reason, RILUtils.convertToHalLinkProperties(linkProperties), dnsesArr, pduSessionId, RILUtils.convertToHalSliceInfoAidl(sliceInfo), matchAllRuleAllowed, convertToHalTrafficDescriptor(trafficDescriptor), null, responseMode, 0);
            return;
        }
        Rlog.w(TAG, "setupDataCallSlice not supported!");
        throw new UnsupportedOperationException();
    }
}
