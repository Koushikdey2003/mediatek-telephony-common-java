package vendor.mediatek.hardware.mtkradioex.V2_0;

import android.internal.hidl.base.V1_0.DebugInfo;
import android.internal.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import com.mediatek.internal.telephony.cat.BipUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public interface ISmartRatSwitchRadioIndication extends IBase {
    public static final String kInterfaceName = "vendor.mediatek.hardware.mtkradioex@2.0::ISmartRatSwitchRadioIndication";

    IHwBinder asBinder();

    void codecActiveInd(int i, int i2, boolean z) throws RemoteException;

    void codecEmptyInd(int i, int i2, boolean z) throws RemoteException;

    void codecFpsInd(int i, int i2, int i3) throws RemoteException;

    void codecResolutionInd(int i, int i2, int i3, int i4) throws RemoteException;

    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    DebugInfo getDebugInfo() throws RemoteException;

    ArrayList<byte[]> getHashChain() throws RemoteException;

    ArrayList<String> interfaceChain() throws RemoteException;

    String interfaceDescriptor() throws RemoteException;

    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    void notifySyspropsChanged() throws RemoteException;

    void ping() throws RemoteException;

    void setHALInstrumentation() throws RemoteException;

    void smartRatSwitchInd(int i, int i2) throws RemoteException;

    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static ISmartRatSwitchRadioIndication asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        ISmartRatSwitchRadioIndication iSmartRatSwitchRadioIndicationQueryLocalInterface = binder.queryLocalInterface(kInterfaceName);
        if (iSmartRatSwitchRadioIndicationQueryLocalInterface != null && (iSmartRatSwitchRadioIndicationQueryLocalInterface instanceof ISmartRatSwitchRadioIndication)) {
            return iSmartRatSwitchRadioIndicationQueryLocalInterface;
        }
        ISmartRatSwitchRadioIndication proxy = new Proxy(binder);
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

    static ISmartRatSwitchRadioIndication castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static ISmartRatSwitchRadioIndication getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static ISmartRatSwitchRadioIndication getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    @Deprecated
    static ISmartRatSwitchRadioIndication getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    @Deprecated
    static ISmartRatSwitchRadioIndication getService() throws RemoteException {
        return getService("default");
    }

    public static final class Proxy implements ISmartRatSwitchRadioIndication {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.mediatek.hardware.mtkradioex@2.0::ISmartRatSwitchRadioIndication]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public void smartRatSwitchInd(int type, int info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ISmartRatSwitchRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public void codecActiveInd(int type, int instanceId, boolean use) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ISmartRatSwitchRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(instanceId);
            _hidl_request.writeBool(use);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public void codecFpsInd(int type, int instanceId, int fps) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ISmartRatSwitchRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(instanceId);
            _hidl_request.writeInt32(fps);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public void codecResolutionInd(int type, int instanceId, int w, int h) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ISmartRatSwitchRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(instanceId);
            _hidl_request.writeInt32(w);
            _hidl_request.writeInt32(h);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public void codecEmptyInd(int type, int instanceId, boolean empty) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ISmartRatSwitchRadioIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(instanceId);
            _hidl_request.writeBool(empty);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends HwBinder implements ISmartRatSwitchRadioIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(ISmartRatSwitchRadioIndication.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public void debug(NativeHandle fd, ArrayList<String> options) {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final String interfaceDescriptor() {
            return ISmartRatSwitchRadioIndication.kInterfaceName;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{BipUtils.TCP_STATUS_ESTABLISHED, 109, -50, 30, 114, 15, 22, 103, -93, 68, 96, -65, -29, -110, -95, -56, 13, 66, 13, 2, 73, -39, 85, 13, 89, -114, 106, -84, 5, -78, -89, -103}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final void setHALInstrumentation() {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final void ping() {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.ISmartRatSwitchRadioIndication
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public IHwInterface queryLocalInterface(String descriptor) {
            if (ISmartRatSwitchRadioIndication.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(ISmartRatSwitchRadioIndication.kInterfaceName);
                    int type = _hidl_request.readInt32();
                    int info = _hidl_request.readInt32();
                    smartRatSwitchInd(type, info);
                    return;
                case 2:
                    _hidl_request.enforceInterface(ISmartRatSwitchRadioIndication.kInterfaceName);
                    int type2 = _hidl_request.readInt32();
                    int instanceId = _hidl_request.readInt32();
                    boolean use = _hidl_request.readBool();
                    codecActiveInd(type2, instanceId, use);
                    return;
                case 3:
                    _hidl_request.enforceInterface(ISmartRatSwitchRadioIndication.kInterfaceName);
                    int type3 = _hidl_request.readInt32();
                    int instanceId2 = _hidl_request.readInt32();
                    int fps = _hidl_request.readInt32();
                    codecFpsInd(type3, instanceId2, fps);
                    return;
                case 4:
                    _hidl_request.enforceInterface(ISmartRatSwitchRadioIndication.kInterfaceName);
                    int type4 = _hidl_request.readInt32();
                    int instanceId3 = _hidl_request.readInt32();
                    int w = _hidl_request.readInt32();
                    int h = _hidl_request.readInt32();
                    codecResolutionInd(type4, instanceId3, w, h);
                    return;
                case 5:
                    _hidl_request.enforceInterface(ISmartRatSwitchRadioIndication.kInterfaceName);
                    int type5 = _hidl_request.readInt32();
                    int instanceId4 = _hidl_request.readInt32();
                    boolean empty = _hidl_request.readBool();
                    codecEmptyInd(type5, instanceId4, empty);
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
