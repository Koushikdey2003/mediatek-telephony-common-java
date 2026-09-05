package vendor.mediatek.hardware.mtkradioex.V2_4;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.internal.hidl.base.V1_0.DebugInfo;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.cat.BipUtils;
import com.mediatek.internal.telephony.cat.MtkCatService;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import com.mediatek.internal.telephony.worldphone.IWorldPhone;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import vendor.mediatek.hardware.mtkradioex.V2_0.CallForwardInfoEx;
import vendor.mediatek.hardware.mtkradioex.V2_0.OperatorInfoWithAct;
import vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryExt;
import vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryStructure;
import vendor.mediatek.hardware.mtkradioex.V2_0.PhbMemStorageResponse;
import vendor.mediatek.hardware.mtkradioex.V2_0.RsuResponseInfo;
import vendor.mediatek.hardware.mtkradioex.V2_0.SignalStrengthWithWcdmaEcio;
import vendor.mediatek.hardware.mtkradioex.V2_0.SmsMemStatus;
import vendor.mediatek.hardware.mtkradioex.V2_0.SmsParams;
import vendor.mediatek.hardware.mtkradioex.V2_0.VsimEvent;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExResponse extends vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse {
    public static final String kInterfaceName = "vendor.mediatek.hardware.mtkradioex@2.4::IMtkRadioExResponse";

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    IHwBinder asBinder();

    void clearLteAvailableFileResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    void disableAllCALinksResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void get4x4MimoEnabledResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getBandModeResponse(RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) throws RemoteException;

    void getBandPriorityListResponse(RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) throws RemoteException;

    void getCALinkCapabilityListResponse(RadioResponseInfo radioResponseInfo, ArrayList<String> arrayList) throws RemoteException;

    void getCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo, boolean z) throws RemoteException;

    void getCaBandModeResponse(RadioResponseInfo radioResponseInfo, ArrayList<Integer> arrayList) throws RemoteException;

    void getCampedFemtoCellInfoResponse(RadioResponseInfo radioResponseInfo, ArrayList<String> arrayList) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    ArrayList<byte[]> getHashChain() throws RemoteException;

    void getLte1xRttCellListResponse(RadioResponseInfo radioResponseInfo, ArrayList<Lte1xRttCellInfo> arrayList) throws RemoteException;

    void getLteBsrTimerResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteDataResponse(RadioResponseInfo radioResponseInfo, LteData lteData) throws RemoteException;

    void getLteRRCStateResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteScanDurationResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getQamEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) throws RemoteException;

    void getTOEInfoResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3) throws RemoteException;

    void getTm9EnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    String interfaceDescriptor() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    void notifySyspropsChanged() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    void ping() throws RemoteException;

    void set4x4MimoEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBandPriorityListResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    void setHALInstrumentation() throws RemoteException;

    void setLteBandEnableStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteBsrTimerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteScanDurationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setQamEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setTm9EnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IMtkRadioExResponse asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IMtkRadioExResponse iMtkRadioExResponseQueryLocalInterface = binder.queryLocalInterface(kInterfaceName);
        if (iMtkRadioExResponseQueryLocalInterface != null && (iMtkRadioExResponseQueryLocalInterface instanceof IMtkRadioExResponse)) {
            return iMtkRadioExResponseQueryLocalInterface;
        }
        IMtkRadioExResponse proxy = new Proxy(binder);
        try {
            for (String descriptor : proxy.interfaceChain()) {
                if (descriptor.equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    static IMtkRadioExResponse castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static IMtkRadioExResponse getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static IMtkRadioExResponse getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    @Deprecated
    static IMtkRadioExResponse getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    @Deprecated
    static IMtkRadioExResponse getService() throws RemoteException {
        return getService("default");
    }

    public static final class Proxy implements IMtkRadioExResponse {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.mediatek.hardware.mtkradioex@2.4::IMtkRadioExResponse]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendEmbmsAtCommandResponse(RadioResponseInfo responseInfo, String data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeString(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setRoamingEnableResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getRoamingEnableResponse(RadioResponseInfo responseInfo, ArrayList<Integer> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void queryPhbStorageInfoResponse(RadioResponseInfo info, ArrayList<Integer> storageInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(storageInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void writePhbEntryResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readPhbEntryResponse(RadioResponseInfo info, ArrayList<PhbEntryStructure> phbEntries) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            PhbEntryStructure.writeVectorToParcel(_hidl_request, phbEntries);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void queryUPBCapabilityResponse(RadioResponseInfo info, ArrayList<Integer> upbCapability) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(upbCapability);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void editUPBEntryResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void deleteUPBEntryResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readUPBGasListResponse(RadioResponseInfo info, ArrayList<String> gasList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(gasList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readUPBGrpEntryResponse(RadioResponseInfo info, ArrayList<Integer> grpEntries) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(grpEntries);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void writeUPBGrpEntryResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getPhoneBookStringsLengthResponse(RadioResponseInfo info, ArrayList<Integer> stringLengthInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(stringLengthInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getPhoneBookMemStorageResponse(RadioResponseInfo info, PhbMemStorageResponse phbMemStorage) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            phbMemStorage.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setPhoneBookMemStorageResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readPhoneBookEntryExtResponse(RadioResponseInfo info, ArrayList<PhbEntryExt> phbEntryExts) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            PhbEntryExt.writeVectorToParcel(_hidl_request, phbEntryExts);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(16, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void writePhoneBookEntryExtResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(17, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void queryUPBAvailableResponse(RadioResponseInfo info, ArrayList<Integer> upbAvailable) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(upbAvailable);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(18, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readUPBEmailEntryResponse(RadioResponseInfo info, String email) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(email);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(19, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readUPBSneEntryResponse(RadioResponseInfo info, String sne) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(sne);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(20, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readUPBAnrEntryResponse(RadioResponseInfo info, ArrayList<PhbEntryStructure> anrs) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            PhbEntryStructure.writeVectorToParcel(_hidl_request, anrs);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(21, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void readUPBAasListResponse(RadioResponseInfo info, ArrayList<String> aasList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(aasList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(22, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setPhonebookReadyResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(23, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setClipResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(24, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getColpResponse(RadioResponseInfo info, int n, int m) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(n);
            _hidl_request.writeInt32(m);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(25, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getColrResponse(RadioResponseInfo info, int n) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(n);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(26, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendCnapResponse(RadioResponseInfo info, int n, int m) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(n);
            _hidl_request.writeInt32(m);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(27, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setColpResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(28, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setColrResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(29, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void queryCallForwardInTimeSlotStatusResponse(RadioResponseInfo info, ArrayList<CallForwardInfoEx> callForwardInfoExs) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            CallForwardInfoEx.writeVectorToParcel(_hidl_request, callForwardInfoExs);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(30, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setCallForwardInTimeSlotResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(31, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void runGbaAuthenticationResponse(RadioResponseInfo info, ArrayList<String> resList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(resList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(32, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void hangupAllResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(33, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setCallIndicationResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(34, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setEccModeResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(35, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void eccPreferredRatResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(36, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setVoicePreferStatusResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(37, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setEccNumResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(38, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getEccNumResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(39, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setCallSubAddressResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(40, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getCallSubAddressResponse(RadioResponseInfo info, int enable) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(enable);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(41, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setModemPowerResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(42, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void triggerModeSwitchByEccResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(43, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getATRResponse(RadioResponseInfo info, String response) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(response);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(44, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getIccidResponse(RadioResponseInfo info, String response) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(response);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(45, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setSimPowerResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(46, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void activateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(simPowerOnOffResponse);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(47, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void deactivateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(simPowerOnOffResponse);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(48, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo info, int simPowerOnOffStatus) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(simPowerOnOffStatus);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(49, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void queryNetworkLockResponse(RadioResponseInfo info, int catagory, int state, int retry_cnt, int autolock_cnt, int num_set, int total_set, int key_state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(catagory);
            _hidl_request.writeInt32(state);
            _hidl_request.writeInt32(retry_cnt);
            _hidl_request.writeInt32(autolock_cnt);
            _hidl_request.writeInt32(num_set);
            _hidl_request.writeInt32(total_set);
            _hidl_request.writeInt32(key_state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(50, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setNetworkLockResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(51, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void supplyDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(remainingRetries);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(52, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void vsimNotificationResponse(RadioResponseInfo info, VsimEvent event) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            event.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(53, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void vsimOperationResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(54, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getSmsParametersResponse(RadioResponseInfo info, SmsParams param) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            param.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(55, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setSmsParametersResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(56, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getSmsMemStatusResponse(RadioResponseInfo info, SmsMemStatus status) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            status.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(57, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setEtwsResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(58, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void removeCbMsgResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(59, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setGsmBroadcastLangsResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(60, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getGsmBroadcastLangsResponse(RadioResponseInfo info, String langs) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(langs);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(61, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getGsmBroadcastActivationRsp(RadioResponseInfo info, int active) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(active);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(62, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendRequestRawResponse(RadioResponseInfo info, ArrayList<Byte> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt8Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(63, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendRequestStringsResponse(RadioResponseInfo info, ArrayList<String> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(64, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setResumeRegistrationResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(65, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void modifyModemTypeResponse(RadioResponseInfo info, int applyType) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(applyType);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(66, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getSmsRuimMemoryStatusResponse(RadioResponseInfo info, SmsMemStatus memStatus) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            memStatus.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(67, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(68, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getAvailableNetworksWithActResponse(RadioResponseInfo info, ArrayList<OperatorInfoWithAct> networkInfosWithAct) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            OperatorInfoWithAct.writeVectorToParcel(_hidl_request, networkInfosWithAct);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(69, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo info, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            signalStrength.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(70, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void cancelAvailableNetworksResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(71, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getFemtocellListResponse(RadioResponseInfo responseInfo, ArrayList<String> femtoList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(femtoList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(72, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void abortFemtocellListResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(73, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void selectFemtocellResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(74, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(75, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(76, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(77, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void cfgA2offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(78, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void cfgB1offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(79, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void enableSCGfailureResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(80, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void disableNRResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(81, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setTxPowerResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(82, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(83, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setSearchRatResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(84, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(85, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setRxTestConfigResponse(RadioResponseInfo responseInfo, ArrayList<Integer> respAntConf) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(respAntConf);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(86, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getRxTestResultResponse(RadioResponseInfo responseInfo, ArrayList<Integer> respAntInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(respAntInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(87, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, ArrayList<Integer> polCapability) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(polCapability);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(88, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, ArrayList<String> polList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(polList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(89, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setPOLEntryResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(90, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setFdModeResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(91, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setTrmResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(92, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(93, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void restartRILDResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(94, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void syncDataSettingsToMdResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(95, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void resetMdDataRetryCountResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(96, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setRemoveRestrictEutranModeResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(97, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setApcModeResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(98, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getApcInfoResponse(RadioResponseInfo info, ArrayList<Integer> cellInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(cellInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(99, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void dataConnectionAttachResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(100, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void dataConnectionDetachResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(101, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void resetAllConnectionsResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(MtkGsmCdmaPhone.NT_MODE_LTE_TDD_ONLY, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setLteReleaseVersionResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(MtkSIMRecords.EVENT_OPL5G, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getLteReleaseVersionResponse(RadioResponseInfo info, int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(104, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setTxPowerStatusResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(105, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setSuppServPropertyResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(106, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(remainingRetries);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(107, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void hangupWithReasonResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(108, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setVendorSettingResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(109, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getPlmnNameFromSE13TableResponse(RadioResponseInfo info, String name) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(name);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(110, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(MtkGsmCdmaPhone.EVENT_UNSOL_RADIO_CAPABILITY_CHANGED, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setGwsdModeResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(112, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setCallValidTimerResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(113, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setIgnoreSameNumberIntervalResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(114, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setKeepAliveByPDCPCtrlPDUResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(115, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setKeepAliveByIpDataResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(116, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void enableDsdaIndicationResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(117, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getDsdaStatusResponse(RadioResponseInfo info, int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(118, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void registerCellQltyReportResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(119, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, ArrayList<String> plmnList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(plmnList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(120, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(121, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(deactivate);
            _hidl_request.writeInt32(allowSCGAdd);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(122, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setMaxUlSpeedResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(123, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendSarIndicatorResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(124, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendRsuRequestResponse(RadioResponseInfo info, RsuResponseInfo rri) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            rri.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(125, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendWifiEnabledResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(126, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendWifiAssociatedResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(127, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void sendWifiIpAddressResponse(RadioResponseInfo responseInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
            responseInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(128, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getTOEInfoResponse(RadioResponseInfo info, String longName, String shortName, String numeric) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeString(longName);
            _hidl_request.writeString(shortName);
            _hidl_request.writeString(numeric);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(129, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void disableAllCALinksResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(130, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getCALinkEnableStatusResponse(RadioResponseInfo info, boolean status) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeBool(status);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(131, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setCALinkEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(132, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getCALinkCapabilityListResponse(RadioResponseInfo info, ArrayList<String> linkCapabilityList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(linkCapabilityList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(133, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getLteDataResponse(RadioResponseInfo info, LteData data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            data.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(134, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getLteRRCStateResponse(RadioResponseInfo info, int state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(135, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setLteBandEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(136, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getBandPriorityListResponse(RadioResponseInfo info, ArrayList<Integer> bandPriList) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(bandPriList);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(137, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setBandPriorityListResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(138, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void set4x4MimoEnabledResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(139, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void get4x4MimoEnabledResponse(RadioResponseInfo info, int enabled_bitmask) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(enabled_bitmask);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(140, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setLteBsrTimerResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(141, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getLteBsrTimerResponse(RadioResponseInfo info, int timer) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(timer);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(142, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getLte1xRttCellListResponse(RadioResponseInfo info, ArrayList<Lte1xRttCellInfo> list) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            Lte1xRttCellInfo.writeVectorToParcel(_hidl_request, list);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(143, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void clearLteAvailableFileResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(144, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getBandModeResponse(RadioResponseInfo info, ArrayList<Integer> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(145, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getCaBandModeResponse(RadioResponseInfo info, ArrayList<Integer> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(146, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getCampedFemtoCellInfoResponse(RadioResponseInfo info, ArrayList<String> femto) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeStringVector(femto);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(147, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setQamEnabledResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(148, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getQamEnabledResponse(RadioResponseInfo info, boolean ulOrDl, boolean enabled) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeBool(ulOrDl);
            _hidl_request.writeBool(enabled);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(149, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setTm9EnabledResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(150, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getTm9EnabledResponse(RadioResponseInfo info, boolean fddOrTdd, boolean enabled) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeBool(fddOrTdd);
            _hidl_request.writeBool(enabled);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(151, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void setLteScanDurationResponse(RadioResponseInfo info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(152, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse
        public void getLteScanDurationResponse(RadioResponseInfo info, int duration) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExResponse.kInterfaceName);
            info.writeToParcel(_hidl_request);
            _hidl_request.writeInt32(duration);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(153, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<String> _hidl_out_descriptors = _hidl_reply.readStringVector();
                return _hidl_out_descriptors;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void debug(NativeHandle fd, ArrayList<String> options) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public String interfaceDescriptor() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                String _hidl_out_descriptor = _hidl_reply.readString();
                return _hidl_out_descriptor;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<byte[]> _hidl_out_hashchain = new ArrayList<>();
                HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    long _hidl_array_offset_1 = _hidl_index_0 * 32;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                return _hidl_out_hashchain;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void setHALInstrumentation() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void ping() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                DebugInfo _hidl_out_info = new DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends HwBinder implements IMtkRadioExResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IMtkRadioExResponse.kInterfaceName, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public void debug(NativeHandle fd, ArrayList<String> options) {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final String interfaceDescriptor() {
            return IMtkRadioExResponse.kInterfaceName;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{-34, -109, -90, -9, 32, 3, 108, -64, -29, -119, 96, 63, 39, -33, 108, -46, 111, -25, 109, 65, -111, 69, -56, 75, 38, -69, -30, 55, 5, 57, -3, 82}, new byte[]{-77, -96, 39, 17, -80, -52, 16, -96, 45, 43, 27, -102, 25, 87, -88, -85, 97, 16, 84, -108, 54, 107, -29, -53, 85, 124, -20, 31, -8, -99, -125, -112}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final void setHALInstrumentation() {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final void ping() {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExResponse, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public IHwInterface queryLocalInterface(String descriptor) {
            if (IMtkRadioExResponse.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String serviceName) throws RemoteException {
            registerService(serviceName);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int _hidl_code, HwParcel _hidl_request, HwParcel _hidl_reply, int _hidl_flags) throws RemoteException {
            switch (_hidl_code) {
                case 1:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo = new RadioResponseInfo();
                    responseInfo.readFromParcel(_hidl_request);
                    String data = _hidl_request.readString();
                    sendEmbmsAtCommandResponse(responseInfo, data);
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo2 = new RadioResponseInfo();
                    responseInfo2.readFromParcel(_hidl_request);
                    setRoamingEnableResponse(responseInfo2);
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo3 = new RadioResponseInfo();
                    responseInfo3.readFromParcel(_hidl_request);
                    ArrayList<Integer> data2 = _hidl_request.readInt32Vector();
                    getRoamingEnableResponse(responseInfo3, data2);
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info = new RadioResponseInfo();
                    info.readFromParcel(_hidl_request);
                    ArrayList<Integer> storageInfo = _hidl_request.readInt32Vector();
                    queryPhbStorageInfoResponse(info, storageInfo);
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info2 = new RadioResponseInfo();
                    info2.readFromParcel(_hidl_request);
                    writePhbEntryResponse(info2);
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info3 = new RadioResponseInfo();
                    info3.readFromParcel(_hidl_request);
                    ArrayList<PhbEntryStructure> phbEntries = PhbEntryStructure.readVectorFromParcel(_hidl_request);
                    readPhbEntryResponse(info3, phbEntries);
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info4 = new RadioResponseInfo();
                    info4.readFromParcel(_hidl_request);
                    ArrayList<Integer> upbCapability = _hidl_request.readInt32Vector();
                    queryUPBCapabilityResponse(info4, upbCapability);
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info5 = new RadioResponseInfo();
                    info5.readFromParcel(_hidl_request);
                    editUPBEntryResponse(info5);
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info6 = new RadioResponseInfo();
                    info6.readFromParcel(_hidl_request);
                    deleteUPBEntryResponse(info6);
                    return;
                case 10:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info7 = new RadioResponseInfo();
                    info7.readFromParcel(_hidl_request);
                    ArrayList<String> gasList = _hidl_request.readStringVector();
                    readUPBGasListResponse(info7, gasList);
                    return;
                case 11:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info8 = new RadioResponseInfo();
                    info8.readFromParcel(_hidl_request);
                    ArrayList<Integer> grpEntries = _hidl_request.readInt32Vector();
                    readUPBGrpEntryResponse(info8, grpEntries);
                    return;
                case 12:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info9 = new RadioResponseInfo();
                    info9.readFromParcel(_hidl_request);
                    writeUPBGrpEntryResponse(info9);
                    return;
                case 13:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info10 = new RadioResponseInfo();
                    info10.readFromParcel(_hidl_request);
                    ArrayList<Integer> stringLengthInfo = _hidl_request.readInt32Vector();
                    getPhoneBookStringsLengthResponse(info10, stringLengthInfo);
                    return;
                case 14:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info11 = new RadioResponseInfo();
                    info11.readFromParcel(_hidl_request);
                    PhbMemStorageResponse phbMemStorage = new PhbMemStorageResponse();
                    phbMemStorage.readFromParcel(_hidl_request);
                    getPhoneBookMemStorageResponse(info11, phbMemStorage);
                    return;
                case 15:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info12 = new RadioResponseInfo();
                    info12.readFromParcel(_hidl_request);
                    setPhoneBookMemStorageResponse(info12);
                    return;
                case 16:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info13 = new RadioResponseInfo();
                    info13.readFromParcel(_hidl_request);
                    ArrayList<PhbEntryExt> phbEntryExts = PhbEntryExt.readVectorFromParcel(_hidl_request);
                    readPhoneBookEntryExtResponse(info13, phbEntryExts);
                    return;
                case 17:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info14 = new RadioResponseInfo();
                    info14.readFromParcel(_hidl_request);
                    writePhoneBookEntryExtResponse(info14);
                    return;
                case 18:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info15 = new RadioResponseInfo();
                    info15.readFromParcel(_hidl_request);
                    ArrayList<Integer> upbAvailable = _hidl_request.readInt32Vector();
                    queryUPBAvailableResponse(info15, upbAvailable);
                    return;
                case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info16 = new RadioResponseInfo();
                    info16.readFromParcel(_hidl_request);
                    String email = _hidl_request.readString();
                    readUPBEmailEntryResponse(info16, email);
                    return;
                case 20:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info17 = new RadioResponseInfo();
                    info17.readFromParcel(_hidl_request);
                    String sne = _hidl_request.readString();
                    readUPBSneEntryResponse(info17, sne);
                    return;
                case WorldMode.MD_WORLD_MODE_LFCTG /* 21 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info18 = new RadioResponseInfo();
                    info18.readFromParcel(_hidl_request);
                    ArrayList<PhbEntryStructure> anrs = PhbEntryStructure.readVectorFromParcel(_hidl_request);
                    readUPBAnrEntryResponse(info18, anrs);
                    return;
                case 22:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info19 = new RadioResponseInfo();
                    info19.readFromParcel(_hidl_request);
                    ArrayList<String> aasList = _hidl_request.readStringVector();
                    readUPBAasListResponse(info19, aasList);
                    return;
                case 23:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info20 = new RadioResponseInfo();
                    info20.readFromParcel(_hidl_request);
                    setPhonebookReadyResponse(info20);
                    return;
                case 24:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info21 = new RadioResponseInfo();
                    info21.readFromParcel(_hidl_request);
                    setClipResponse(info21);
                    return;
                case 25:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info22 = new RadioResponseInfo();
                    info22.readFromParcel(_hidl_request);
                    int n = _hidl_request.readInt32();
                    int m = _hidl_request.readInt32();
                    getColpResponse(info22, n, m);
                    return;
                case 26:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info23 = new RadioResponseInfo();
                    info23.readFromParcel(_hidl_request);
                    int n2 = _hidl_request.readInt32();
                    getColrResponse(info23, n2);
                    return;
                case 27:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info24 = new RadioResponseInfo();
                    info24.readFromParcel(_hidl_request);
                    int n3 = _hidl_request.readInt32();
                    int m2 = _hidl_request.readInt32();
                    sendCnapResponse(info24, n3, m2);
                    return;
                case 28:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info25 = new RadioResponseInfo();
                    info25.readFromParcel(_hidl_request);
                    setColpResponse(info25);
                    return;
                case 29:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info26 = new RadioResponseInfo();
                    info26.readFromParcel(_hidl_request);
                    setColrResponse(info26);
                    return;
                case IWorldPhone.EVENT_REG_SUSPENDED_1 /* 30 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info27 = new RadioResponseInfo();
                    info27.readFromParcel(_hidl_request);
                    ArrayList<CallForwardInfoEx> callForwardInfoExs = CallForwardInfoEx.readVectorFromParcel(_hidl_request);
                    queryCallForwardInTimeSlotStatusResponse(info27, callForwardInfoExs);
                    return;
                case IWorldPhone.EVENT_REG_SUSPENDED_2 /* 31 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info28 = new RadioResponseInfo();
                    info28.readFromParcel(_hidl_request);
                    setCallForwardInTimeSlotResponse(info28);
                    return;
                case 32:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info29 = new RadioResponseInfo();
                    info29.readFromParcel(_hidl_request);
                    ArrayList<String> resList = _hidl_request.readStringVector();
                    runGbaAuthenticationResponse(info29, resList);
                    return;
                case 33:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info30 = new RadioResponseInfo();
                    info30.readFromParcel(_hidl_request);
                    hangupAllResponse(info30);
                    return;
                case 34:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info31 = new RadioResponseInfo();
                    info31.readFromParcel(_hidl_request);
                    setCallIndicationResponse(info31);
                    return;
                case 35:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info32 = new RadioResponseInfo();
                    info32.readFromParcel(_hidl_request);
                    setEccModeResponse(info32);
                    return;
                case 36:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info33 = new RadioResponseInfo();
                    info33.readFromParcel(_hidl_request);
                    eccPreferredRatResponse(info33);
                    return;
                case 37:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info34 = new RadioResponseInfo();
                    info34.readFromParcel(_hidl_request);
                    setVoicePreferStatusResponse(info34);
                    return;
                case 38:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info35 = new RadioResponseInfo();
                    info35.readFromParcel(_hidl_request);
                    setEccNumResponse(info35);
                    return;
                case 39:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info36 = new RadioResponseInfo();
                    info36.readFromParcel(_hidl_request);
                    getEccNumResponse(info36);
                    return;
                case 40:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info37 = new RadioResponseInfo();
                    info37.readFromParcel(_hidl_request);
                    setCallSubAddressResponse(info37);
                    return;
                case 41:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info38 = new RadioResponseInfo();
                    info38.readFromParcel(_hidl_request);
                    int enable = _hidl_request.readInt32();
                    getCallSubAddressResponse(info38, enable);
                    return;
                case 42:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo4 = new RadioResponseInfo();
                    responseInfo4.readFromParcel(_hidl_request);
                    setModemPowerResponse(responseInfo4);
                    return;
                case 43:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info39 = new RadioResponseInfo();
                    info39.readFromParcel(_hidl_request);
                    triggerModeSwitchByEccResponse(info39);
                    return;
                case 44:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info40 = new RadioResponseInfo();
                    info40.readFromParcel(_hidl_request);
                    String response = _hidl_request.readString();
                    getATRResponse(info40, response);
                    return;
                case 45:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info41 = new RadioResponseInfo();
                    info41.readFromParcel(_hidl_request);
                    String response2 = _hidl_request.readString();
                    getIccidResponse(info41, response2);
                    return;
                case MtkCatService.MSG_ID_CACHED_DISPLAY_TEXT_TIMEOUT /* 46 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info42 = new RadioResponseInfo();
                    info42.readFromParcel(_hidl_request);
                    setSimPowerResponse(info42);
                    return;
                case MtkCatService.MSG_ID_CONN_RETRY_TIMEOUT /* 47 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info43 = new RadioResponseInfo();
                    info43.readFromParcel(_hidl_request);
                    int simPowerOnOffResponse = _hidl_request.readInt32();
                    activateUiccCardRsp(info43, simPowerOnOffResponse);
                    return;
                case 48:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info44 = new RadioResponseInfo();
                    info44.readFromParcel(_hidl_request);
                    int simPowerOnOffResponse2 = _hidl_request.readInt32();
                    deactivateUiccCardRsp(info44, simPowerOnOffResponse2);
                    return;
                case 49:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info45 = new RadioResponseInfo();
                    info45.readFromParcel(_hidl_request);
                    int simPowerOnOffStatus = _hidl_request.readInt32();
                    getCurrentUiccCardProvisioningStatusRsp(info45, simPowerOnOffStatus);
                    return;
                case 50:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info46 = new RadioResponseInfo();
                    info46.readFromParcel(_hidl_request);
                    int catagory = _hidl_request.readInt32();
                    int state = _hidl_request.readInt32();
                    int retry_cnt = _hidl_request.readInt32();
                    int autolock_cnt = _hidl_request.readInt32();
                    int num_set = _hidl_request.readInt32();
                    int total_set = _hidl_request.readInt32();
                    int key_state = _hidl_request.readInt32();
                    queryNetworkLockResponse(info46, catagory, state, retry_cnt, autolock_cnt, num_set, total_set, key_state);
                    return;
                case 51:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info47 = new RadioResponseInfo();
                    info47.readFromParcel(_hidl_request);
                    setNetworkLockResponse(info47);
                    return;
                case 52:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info48 = new RadioResponseInfo();
                    info48.readFromParcel(_hidl_request);
                    int remainingRetries = _hidl_request.readInt32();
                    supplyDepersonalizationResponse(info48, remainingRetries);
                    return;
                case 53:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info49 = new RadioResponseInfo();
                    info49.readFromParcel(_hidl_request);
                    VsimEvent event = new VsimEvent();
                    event.readFromParcel(_hidl_request);
                    vsimNotificationResponse(info49, event);
                    return;
                case 54:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info50 = new RadioResponseInfo();
                    info50.readFromParcel(_hidl_request);
                    vsimOperationResponse(info50);
                    return;
                case 55:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info51 = new RadioResponseInfo();
                    info51.readFromParcel(_hidl_request);
                    SmsParams param = new SmsParams();
                    param.readFromParcel(_hidl_request);
                    getSmsParametersResponse(info51, param);
                    return;
                case 56:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info52 = new RadioResponseInfo();
                    info52.readFromParcel(_hidl_request);
                    setSmsParametersResponse(info52);
                    return;
                case 57:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info53 = new RadioResponseInfo();
                    info53.readFromParcel(_hidl_request);
                    SmsMemStatus status = new SmsMemStatus();
                    status.readFromParcel(_hidl_request);
                    getSmsMemStatusResponse(info53, status);
                    return;
                case 58:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info54 = new RadioResponseInfo();
                    info54.readFromParcel(_hidl_request);
                    setEtwsResponse(info54);
                    return;
                case 59:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info55 = new RadioResponseInfo();
                    info55.readFromParcel(_hidl_request);
                    removeCbMsgResponse(info55);
                    return;
                case 60:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info56 = new RadioResponseInfo();
                    info56.readFromParcel(_hidl_request);
                    setGsmBroadcastLangsResponse(info56);
                    return;
                case IWorldPhone.EVENT_INVALID_SIM_NOTIFY_2 /* 61 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info57 = new RadioResponseInfo();
                    info57.readFromParcel(_hidl_request);
                    String langs = _hidl_request.readString();
                    getGsmBroadcastLangsResponse(info57, langs);
                    return;
                case IWorldPhone.EVENT_INVALID_SIM_NOTIFY_3 /* 62 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info58 = new RadioResponseInfo();
                    info58.readFromParcel(_hidl_request);
                    int active = _hidl_request.readInt32();
                    getGsmBroadcastActivationRsp(info58, active);
                    return;
                case IWorldPhone.EVENT_INVALID_SIM_NOTIFY_4 /* 63 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info59 = new RadioResponseInfo();
                    info59.readFromParcel(_hidl_request);
                    ArrayList<Byte> data3 = _hidl_request.readInt8Vector();
                    sendRequestRawResponse(info59, data3);
                    return;
                case 64:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info60 = new RadioResponseInfo();
                    info60.readFromParcel(_hidl_request);
                    ArrayList<String> data4 = _hidl_request.readStringVector();
                    sendRequestStringsResponse(info60, data4);
                    return;
                case 65:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info61 = new RadioResponseInfo();
                    info61.readFromParcel(_hidl_request);
                    setResumeRegistrationResponse(info61);
                    return;
                case 66:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info62 = new RadioResponseInfo();
                    info62.readFromParcel(_hidl_request);
                    int applyType = _hidl_request.readInt32();
                    modifyModemTypeResponse(info62, applyType);
                    return;
                case 67:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info63 = new RadioResponseInfo();
                    info63.readFromParcel(_hidl_request);
                    SmsMemStatus memStatus = new SmsMemStatus();
                    memStatus.readFromParcel(_hidl_request);
                    getSmsRuimMemoryStatusResponse(info63, memStatus);
                    return;
                case 68:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info64 = new RadioResponseInfo();
                    info64.readFromParcel(_hidl_request);
                    setNetworkSelectionModeManualWithActResponse(info64);
                    return;
                case 69:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info65 = new RadioResponseInfo();
                    info65.readFromParcel(_hidl_request);
                    ArrayList<OperatorInfoWithAct> networkInfosWithAct = OperatorInfoWithAct.readVectorFromParcel(_hidl_request);
                    getAvailableNetworksWithActResponse(info65, networkInfosWithAct);
                    return;
                case IWorldPhone.EVENT_RESUME_CAMPING_1 /* 70 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info66 = new RadioResponseInfo();
                    info66.readFromParcel(_hidl_request);
                    SignalStrengthWithWcdmaEcio signalStrength = new SignalStrengthWithWcdmaEcio();
                    signalStrength.readFromParcel(_hidl_request);
                    getSignalStrengthWithWcdmaEcioResponse(info66, signalStrength);
                    return;
                case IWorldPhone.EVENT_RESUME_CAMPING_2 /* 71 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info67 = new RadioResponseInfo();
                    info67.readFromParcel(_hidl_request);
                    cancelAvailableNetworksResponse(info67);
                    return;
                case IWorldPhone.EVENT_RESUME_CAMPING_3 /* 72 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo5 = new RadioResponseInfo();
                    responseInfo5.readFromParcel(_hidl_request);
                    ArrayList<String> femtoList = _hidl_request.readStringVector();
                    getFemtocellListResponse(responseInfo5, femtoList);
                    return;
                case IWorldPhone.EVENT_RESUME_CAMPING_4 /* 73 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo6 = new RadioResponseInfo();
                    responseInfo6.readFromParcel(_hidl_request);
                    abortFemtocellListResponse(responseInfo6);
                    return;
                case 74:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo7 = new RadioResponseInfo();
                    responseInfo7.readFromParcel(_hidl_request);
                    selectFemtocellResponse(responseInfo7);
                    return;
                case 75:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo8 = new RadioResponseInfo();
                    responseInfo8.readFromParcel(_hidl_request);
                    int mode = _hidl_request.readInt32();
                    queryFemtoCellSystemSelectionModeResponse(responseInfo8, mode);
                    return;
                case 76:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo9 = new RadioResponseInfo();
                    responseInfo9.readFromParcel(_hidl_request);
                    setFemtoCellSystemSelectionModeResponse(responseInfo9);
                    return;
                case 77:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo10 = new RadioResponseInfo();
                    responseInfo10.readFromParcel(_hidl_request);
                    setServiceStateToModemResponse(responseInfo10);
                    return;
                case 78:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo11 = new RadioResponseInfo();
                    responseInfo11.readFromParcel(_hidl_request);
                    cfgA2offsetResponse(responseInfo11);
                    return;
                case 79:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo12 = new RadioResponseInfo();
                    responseInfo12.readFromParcel(_hidl_request);
                    cfgB1offsetResponse(responseInfo12);
                    return;
                case IWorldPhone.EVENT_SERVICE_STATE_CHANGED_1 /* 80 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo13 = new RadioResponseInfo();
                    responseInfo13.readFromParcel(_hidl_request);
                    enableSCGfailureResponse(responseInfo13);
                    return;
                case IWorldPhone.EVENT_SERVICE_STATE_CHANGED_2 /* 81 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo14 = new RadioResponseInfo();
                    responseInfo14.readFromParcel(_hidl_request);
                    disableNRResponse(responseInfo14);
                    return;
                case IWorldPhone.EVENT_SERVICE_STATE_CHANGED_3 /* 82 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo15 = new RadioResponseInfo();
                    responseInfo15.readFromParcel(_hidl_request);
                    setTxPowerResponse(responseInfo15);
                    return;
                case IWorldPhone.EVENT_SERVICE_STATE_CHANGED_4 /* 83 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo16 = new RadioResponseInfo();
                    responseInfo16.readFromParcel(_hidl_request);
                    setSearchStoredFreqInfoResponse(responseInfo16);
                    return;
                case 84:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo17 = new RadioResponseInfo();
                    responseInfo17.readFromParcel(_hidl_request);
                    setSearchRatResponse(responseInfo17);
                    return;
                case 85:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo18 = new RadioResponseInfo();
                    responseInfo18.readFromParcel(_hidl_request);
                    setBgsrchDeltaSleepTimerResponse(responseInfo18);
                    return;
                case 86:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo19 = new RadioResponseInfo();
                    responseInfo19.readFromParcel(_hidl_request);
                    ArrayList<Integer> respAntConf = _hidl_request.readInt32Vector();
                    setRxTestConfigResponse(responseInfo19, respAntConf);
                    return;
                case BipUtils.ADDRESS_TYPE_IPV6 /* 87 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo20 = new RadioResponseInfo();
                    responseInfo20.readFromParcel(_hidl_request);
                    ArrayList<Integer> respAntInfo = _hidl_request.readInt32Vector();
                    getRxTestResultResponse(responseInfo20, respAntInfo);
                    return;
                case 88:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo21 = new RadioResponseInfo();
                    responseInfo21.readFromParcel(_hidl_request);
                    ArrayList<Integer> polCapability = _hidl_request.readInt32Vector();
                    getPOLCapabilityResponse(responseInfo21, polCapability);
                    return;
                case 89:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo22 = new RadioResponseInfo();
                    responseInfo22.readFromParcel(_hidl_request);
                    ArrayList<String> polList = _hidl_request.readStringVector();
                    getCurrentPOLListResponse(responseInfo22, polList);
                    return;
                case 90:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo23 = new RadioResponseInfo();
                    responseInfo23.readFromParcel(_hidl_request);
                    setPOLEntryResponse(responseInfo23);
                    return;
                case 91:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info68 = new RadioResponseInfo();
                    info68.readFromParcel(_hidl_request);
                    setFdModeResponse(info68);
                    return;
                case 92:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info69 = new RadioResponseInfo();
                    info69.readFromParcel(_hidl_request);
                    setTrmResponse(info69);
                    return;
                case 93:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info70 = new RadioResponseInfo();
                    info70.readFromParcel(_hidl_request);
                    handleStkCallSetupRequestFromSimWithResCodeResponse(info70);
                    return;
                case 94:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info71 = new RadioResponseInfo();
                    info71.readFromParcel(_hidl_request);
                    restartRILDResponse(info71);
                    return;
                case 95:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info72 = new RadioResponseInfo();
                    info72.readFromParcel(_hidl_request);
                    syncDataSettingsToMdResponse(info72);
                    return;
                case 96:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info73 = new RadioResponseInfo();
                    info73.readFromParcel(_hidl_request);
                    resetMdDataRetryCountResponse(info73);
                    return;
                case 97:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo24 = new RadioResponseInfo();
                    responseInfo24.readFromParcel(_hidl_request);
                    setRemoveRestrictEutranModeResponse(responseInfo24);
                    return;
                case 98:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info74 = new RadioResponseInfo();
                    info74.readFromParcel(_hidl_request);
                    setApcModeResponse(info74);
                    return;
                case 99:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info75 = new RadioResponseInfo();
                    info75.readFromParcel(_hidl_request);
                    ArrayList<Integer> cellInfo = _hidl_request.readInt32Vector();
                    getApcInfoResponse(info75, cellInfo);
                    return;
                case 100:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info76 = new RadioResponseInfo();
                    info76.readFromParcel(_hidl_request);
                    dataConnectionAttachResponse(info76);
                    return;
                case 101:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info77 = new RadioResponseInfo();
                    info77.readFromParcel(_hidl_request);
                    dataConnectionDetachResponse(info77);
                    return;
                case MtkGsmCdmaPhone.NT_MODE_LTE_TDD_ONLY /* 102 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info78 = new RadioResponseInfo();
                    info78.readFromParcel(_hidl_request);
                    resetAllConnectionsResponse(info78);
                    return;
                case MtkSIMRecords.EVENT_OPL5G /* 103 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info79 = new RadioResponseInfo();
                    info79.readFromParcel(_hidl_request);
                    setLteReleaseVersionResponse(info79);
                    return;
                case 104:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info80 = new RadioResponseInfo();
                    info80.readFromParcel(_hidl_request);
                    int mode2 = _hidl_request.readInt32();
                    getLteReleaseVersionResponse(info80, mode2);
                    return;
                case 105:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info81 = new RadioResponseInfo();
                    info81.readFromParcel(_hidl_request);
                    setTxPowerStatusResponse(info81);
                    return;
                case 106:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info82 = new RadioResponseInfo();
                    info82.readFromParcel(_hidl_request);
                    setSuppServPropertyResponse(info82);
                    return;
                case 107:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info83 = new RadioResponseInfo();
                    info83.readFromParcel(_hidl_request);
                    int remainingRetries2 = _hidl_request.readInt32();
                    supplyDeviceNetworkDepersonalizationResponse(info83, remainingRetries2);
                    return;
                case 108:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info84 = new RadioResponseInfo();
                    info84.readFromParcel(_hidl_request);
                    hangupWithReasonResponse(info84);
                    return;
                case 109:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info85 = new RadioResponseInfo();
                    info85.readFromParcel(_hidl_request);
                    setVendorSettingResponse(info85);
                    return;
                case 110:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info86 = new RadioResponseInfo();
                    info86.readFromParcel(_hidl_request);
                    String name = _hidl_request.readString();
                    getPlmnNameFromSE13TableResponse(info86, name);
                    return;
                case MtkGsmCdmaPhone.EVENT_UNSOL_RADIO_CAPABILITY_CHANGED /* 111 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info87 = new RadioResponseInfo();
                    info87.readFromParcel(_hidl_request);
                    enableCAPlusBandWidthFilterResponse(info87);
                    return;
                case 112:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info88 = new RadioResponseInfo();
                    info88.readFromParcel(_hidl_request);
                    setGwsdModeResponse(info88);
                    return;
                case 113:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info89 = new RadioResponseInfo();
                    info89.readFromParcel(_hidl_request);
                    setCallValidTimerResponse(info89);
                    return;
                case 114:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info90 = new RadioResponseInfo();
                    info90.readFromParcel(_hidl_request);
                    setIgnoreSameNumberIntervalResponse(info90);
                    return;
                case 115:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info91 = new RadioResponseInfo();
                    info91.readFromParcel(_hidl_request);
                    setKeepAliveByPDCPCtrlPDUResponse(info91);
                    return;
                case 116:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info92 = new RadioResponseInfo();
                    info92.readFromParcel(_hidl_request);
                    setKeepAliveByIpDataResponse(info92);
                    return;
                case 117:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info93 = new RadioResponseInfo();
                    info93.readFromParcel(_hidl_request);
                    enableDsdaIndicationResponse(info93);
                    return;
                case 118:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info94 = new RadioResponseInfo();
                    info94.readFromParcel(_hidl_request);
                    int mode3 = _hidl_request.readInt32();
                    getDsdaStatusResponse(info94, mode3);
                    return;
                case 119:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info95 = new RadioResponseInfo();
                    info95.readFromParcel(_hidl_request);
                    registerCellQltyReportResponse(info95);
                    return;
                case 120:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo25 = new RadioResponseInfo();
                    responseInfo25.readFromParcel(_hidl_request);
                    ArrayList<String> plmnList = _hidl_request.readStringVector();
                    getSuggestedPlmnListResponse(responseInfo25, plmnList);
                    return;
                case 121:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo26 = new RadioResponseInfo();
                    responseInfo26.readFromParcel(_hidl_request);
                    deactivateNrScgCommunicationResponse(responseInfo26);
                    return;
                case 122:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo27 = new RadioResponseInfo();
                    responseInfo27.readFromParcel(_hidl_request);
                    int deactivate = _hidl_request.readInt32();
                    int allowSCGAdd = _hidl_request.readInt32();
                    getDeactivateNrScgCommunicationResponse(responseInfo27, deactivate, allowSCGAdd);
                    return;
                case 123:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo28 = new RadioResponseInfo();
                    responseInfo28.readFromParcel(_hidl_request);
                    setMaxUlSpeedResponse(responseInfo28);
                    return;
                case 124:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info96 = new RadioResponseInfo();
                    info96.readFromParcel(_hidl_request);
                    sendSarIndicatorResponse(info96);
                    return;
                case 125:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info97 = new RadioResponseInfo();
                    info97.readFromParcel(_hidl_request);
                    RsuResponseInfo rri = new RsuResponseInfo();
                    rri.readFromParcel(_hidl_request);
                    sendRsuRequestResponse(info97, rri);
                    return;
                case 126:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo29 = new RadioResponseInfo();
                    responseInfo29.readFromParcel(_hidl_request);
                    sendWifiEnabledResponse(responseInfo29);
                    return;
                case 127:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo30 = new RadioResponseInfo();
                    responseInfo30.readFromParcel(_hidl_request);
                    sendWifiAssociatedResponse(responseInfo30);
                    return;
                case 128:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo responseInfo31 = new RadioResponseInfo();
                    responseInfo31.readFromParcel(_hidl_request);
                    sendWifiIpAddressResponse(responseInfo31);
                    return;
                case 129:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info98 = new RadioResponseInfo();
                    info98.readFromParcel(_hidl_request);
                    String longName = _hidl_request.readString();
                    String shortName = _hidl_request.readString();
                    String numeric = _hidl_request.readString();
                    getTOEInfoResponse(info98, longName, shortName, numeric);
                    return;
                case 130:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info99 = new RadioResponseInfo();
                    info99.readFromParcel(_hidl_request);
                    disableAllCALinksResponse(info99);
                    return;
                case 131:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info100 = new RadioResponseInfo();
                    info100.readFromParcel(_hidl_request);
                    getCALinkEnableStatusResponse(info100, _hidl_request.readBool());
                    return;
                case 132:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info101 = new RadioResponseInfo();
                    info101.readFromParcel(_hidl_request);
                    setCALinkEnableStatusResponse(info101);
                    return;
                case 133:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info102 = new RadioResponseInfo();
                    info102.readFromParcel(_hidl_request);
                    ArrayList<String> linkCapabilityList = _hidl_request.readStringVector();
                    getCALinkCapabilityListResponse(info102, linkCapabilityList);
                    return;
                case 134:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info103 = new RadioResponseInfo();
                    info103.readFromParcel(_hidl_request);
                    LteData data5 = new LteData();
                    data5.readFromParcel(_hidl_request);
                    getLteDataResponse(info103, data5);
                    return;
                case 135:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info104 = new RadioResponseInfo();
                    info104.readFromParcel(_hidl_request);
                    int state2 = _hidl_request.readInt32();
                    getLteRRCStateResponse(info104, state2);
                    return;
                case 136:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info105 = new RadioResponseInfo();
                    info105.readFromParcel(_hidl_request);
                    setLteBandEnableStatusResponse(info105);
                    return;
                case 137:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info106 = new RadioResponseInfo();
                    info106.readFromParcel(_hidl_request);
                    ArrayList<Integer> bandPriList = _hidl_request.readInt32Vector();
                    getBandPriorityListResponse(info106, bandPriList);
                    return;
                case 138:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info107 = new RadioResponseInfo();
                    info107.readFromParcel(_hidl_request);
                    setBandPriorityListResponse(info107);
                    return;
                case 139:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info108 = new RadioResponseInfo();
                    info108.readFromParcel(_hidl_request);
                    set4x4MimoEnabledResponse(info108);
                    return;
                case 140:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info109 = new RadioResponseInfo();
                    info109.readFromParcel(_hidl_request);
                    int enabled_bitmask = _hidl_request.readInt32();
                    get4x4MimoEnabledResponse(info109, enabled_bitmask);
                    return;
                case 141:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info110 = new RadioResponseInfo();
                    info110.readFromParcel(_hidl_request);
                    setLteBsrTimerResponse(info110);
                    return;
                case 142:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info111 = new RadioResponseInfo();
                    info111.readFromParcel(_hidl_request);
                    int timer = _hidl_request.readInt32();
                    getLteBsrTimerResponse(info111, timer);
                    return;
                case 143:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info112 = new RadioResponseInfo();
                    info112.readFromParcel(_hidl_request);
                    ArrayList<Lte1xRttCellInfo> list = Lte1xRttCellInfo.readVectorFromParcel(_hidl_request);
                    getLte1xRttCellListResponse(info112, list);
                    return;
                case 144:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info113 = new RadioResponseInfo();
                    info113.readFromParcel(_hidl_request);
                    clearLteAvailableFileResponse(info113);
                    return;
                case 145:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info114 = new RadioResponseInfo();
                    info114.readFromParcel(_hidl_request);
                    ArrayList<Integer> data6 = _hidl_request.readInt32Vector();
                    getBandModeResponse(info114, data6);
                    return;
                case 146:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info115 = new RadioResponseInfo();
                    info115.readFromParcel(_hidl_request);
                    ArrayList<Integer> data7 = _hidl_request.readInt32Vector();
                    getCaBandModeResponse(info115, data7);
                    return;
                case 147:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info116 = new RadioResponseInfo();
                    info116.readFromParcel(_hidl_request);
                    ArrayList<String> femto = _hidl_request.readStringVector();
                    getCampedFemtoCellInfoResponse(info116, femto);
                    return;
                case 148:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info117 = new RadioResponseInfo();
                    info117.readFromParcel(_hidl_request);
                    setQamEnabledResponse(info117);
                    return;
                case 149:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info118 = new RadioResponseInfo();
                    info118.readFromParcel(_hidl_request);
                    boolean ulOrDl = _hidl_request.readBool();
                    boolean enabled = _hidl_request.readBool();
                    getQamEnabledResponse(info118, ulOrDl, enabled);
                    return;
                case 150:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info119 = new RadioResponseInfo();
                    info119.readFromParcel(_hidl_request);
                    setTm9EnabledResponse(info119);
                    return;
                case 151:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info120 = new RadioResponseInfo();
                    info120.readFromParcel(_hidl_request);
                    boolean fddOrTdd = _hidl_request.readBool();
                    boolean enabled2 = _hidl_request.readBool();
                    getTm9EnabledResponse(info120, fddOrTdd, enabled2);
                    return;
                case 152:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info121 = new RadioResponseInfo();
                    info121.readFromParcel(_hidl_request);
                    setLteScanDurationResponse(info121);
                    return;
                case 153:
                    _hidl_request.enforceInterface(IMtkRadioExResponse.kInterfaceName);
                    RadioResponseInfo info122 = new RadioResponseInfo();
                    info122.readFromParcel(_hidl_request);
                    int duration = _hidl_request.readInt32();
                    getLteScanDurationResponse(info122, duration);
                    return;
                case 256067662:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    ArrayList<String> _hidl_out_descriptors = interfaceChain();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeStringVector(_hidl_out_descriptors);
                    _hidl_reply.send();
                    return;
                case 256131655:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    NativeHandle fd = _hidl_request.readNativeHandle();
                    ArrayList<String> options = _hidl_request.readStringVector();
                    debug(fd, options);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 256136003:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    String _hidl_out_descriptor = interfaceDescriptor();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeString(_hidl_out_descriptor);
                    _hidl_reply.send();
                    return;
                case 256398152:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                    _hidl_reply.writeStatus(0);
                    HwBlob _hidl_blob = new HwBlob(16);
                    int _hidl_vec_size = _hidl_out_hashchain.size();
                    _hidl_blob.putInt32(8L, _hidl_vec_size);
                    _hidl_blob.putBool(12L, false);
                    HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
                    for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                        long _hidl_array_offset_1 = _hidl_index_0 * 32;
                        byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                        if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                            throw new IllegalArgumentException("Array element is not of the expected length");
                        }
                        childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                    }
                    _hidl_blob.putBlob(0L, childBlob);
                    _hidl_reply.writeBuffer(_hidl_blob);
                    _hidl_reply.send();
                    return;
                case 256462420:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    setHALInstrumentation();
                    return;
                case 256660548:
                default:
                    return;
                case 256921159:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    ping();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 257049926:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    DebugInfo _hidl_out_info = getDebugInfo();
                    _hidl_reply.writeStatus(0);
                    _hidl_out_info.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 257120595:
                    _hidl_request.enforceInterface("android.hidl.base@1.0::IBase");
                    notifySyspropsChanged();
                    return;
            }
        }
    }
}
