package vendor.mediatek.hardware.mtkradioex.V2_4;

import android.hardware.radio.V1_0.SuppSvcNotification;
import android.internal.hidl.base.V1_0.DebugInfo;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import com.mediatek.internal.telephony.cat.MtkCatService;
import com.mediatek.internal.telephony.worldphone.IWorldPhone;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import vendor.mediatek.hardware.mtkradioex.V2_0.CfuStatusNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.CipherNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.CrssNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.DedicateDataCall;
import vendor.mediatek.hardware.mtkradioex.V2_0.EtwsNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.IncomingCallNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.PcoDataAttachedInfo;
import vendor.mediatek.hardware.mtkradioex.V2_0.PlmnMvnoInfo;
import vendor.mediatek.hardware.mtkradioex.V2_0.SignalStrengthWithWcdmaEcio;
import vendor.mediatek.hardware.mtkradioex.V2_0.VsimOperationEvent;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExIndication extends vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication {
    public static final String kInterfaceName = "vendor.mediatek.hardware.mtkradioex@2.4::IMtkRadioExIndication";

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    IHwBinder asBinder();

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    String interfaceDescriptor() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    void notifySyspropsChanged() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    void ping() throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    void setHALInstrumentation() throws RemoteException;

    void sib16TimeInfoInd(int i, String str, long j) throws RemoteException;

    void toeInfoInd(int i, String str, String str2, String str3) throws RemoteException;

    @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static IMtkRadioExIndication asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IMtkRadioExIndication iMtkRadioExIndicationQueryLocalInterface = binder.queryLocalInterface(kInterfaceName);
        if (iMtkRadioExIndicationQueryLocalInterface != null && (iMtkRadioExIndicationQueryLocalInterface instanceof IMtkRadioExIndication)) {
            return iMtkRadioExIndicationQueryLocalInterface;
        }
        IMtkRadioExIndication proxy = new Proxy(binder);
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

    static IMtkRadioExIndication castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static IMtkRadioExIndication getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static IMtkRadioExIndication getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    @Deprecated
    static IMtkRadioExIndication getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    @Deprecated
    static IMtkRadioExIndication getService() throws RemoteException {
        return getService("default");
    }

    public static final class Proxy implements IMtkRadioExIndication {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of vendor.mediatek.hardware.mtkradioex@2.4::IMtkRadioExIndication]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void eMBMSAtInfoIndication(int type, String info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void eMBMSSessionStatusIndication(int type, int status) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(status);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void phbReadyNotification(int type, int isPhbReady) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(isPhbReady);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void cfuStatusNotify(int type, CfuStatusNotification cfuStatus) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            cfuStatus.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void incomingCallIndication(int type, IncomingCallNotification inCallNotify) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            inCallNotify.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void callAdditionalInfoInd(int type, int ciType, ArrayList<String> info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(ciType);
            _hidl_request.writeStringVector(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void cipherIndication(int type, CipherNotification cipherNotify) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            cipherNotify.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void suppSvcNotifyEx(int type, SuppSvcNotification suppSvc) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            suppSvc.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void crssIndication(int type, CrssNotification crssNotify) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            crssNotify.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void cdmaCallAccepted(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void eccNumIndication(int type, String ecc_list_with_card, String ecc_list_no_card) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(ecc_list_with_card);
            _hidl_request.writeString(ecc_list_no_card);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onVirtualSimStatusChanged(int type, int simInserted) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(simInserted);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onImeiLock(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onImsiRefreshDone(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onSimHotSwapInd(int type, int event, String info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(event);
            _hidl_request.writeString(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onSimMeLockEvent(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(16, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onRsuSimLockEvent(int type, int eventId) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(eventId);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(17, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onCardDetectedInd(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(18, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onVsimEventIndication(int type, VsimOperationEvent event) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            event.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(19, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void newEtwsInd(int type, EtwsNotification etws) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            etws.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(20, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void meSmsStorageFullInd(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(21, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void smsReadyInd(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(22, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void oemHookRaw(int type, ArrayList<Byte> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt8Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(23, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void plmnChangedIndication(int type, ArrayList<String> plmns) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeStringVector(plmns);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(24, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void registrationSuspendedIndication(int type, ArrayList<Integer> sessionIds) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(sessionIds);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(25, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void gmssRatChangedIndication(int type, ArrayList<Integer> gmsss) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(gmsss);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(26, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void worldModeChangedIndication(int type, ArrayList<Integer> modes) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(modes);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(27, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void esnMeidChangeInd(int type, String esnMeid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(esnMeid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(28, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responseCsNetworkStateChangeInd(int type, ArrayList<String> state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeStringVector(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(29, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responsePsNetworkStateChangeInd(int type, ArrayList<Integer> state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(30, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responseInvalidSimInd(int type, ArrayList<String> state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeStringVector(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(31, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responseNetworkEventInd(int type, ArrayList<Integer> event) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(event);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(32, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responseModulationInfoInd(int type, ArrayList<Integer> modulation) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(modulation);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(33, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void dataAllowedNotification(int type, int allowed) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(allowed);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(34, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responseFemtocellInfo(int type, ArrayList<String> info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeStringVector(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(35, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void networkInfoInd(int type, ArrayList<String> networkinfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeStringVector(networkinfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(36, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void currentSignalStrengthWithWcdmaEcioInd(int type, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            signalStrength.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(37, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void responseLteNetworkInfo(int type, int lteBand) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(lteBand);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(38, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void dedicatedBearerActivationInd(int type, DedicateDataCall ddcData) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            ddcData.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(39, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void dedicatedBearerModificationInd(int type, DedicateDataCall ddcData) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            ddcData.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(40, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void dedicatedBearerDeactivationInd(int type, int cid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(cid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(41, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void bipProactiveCommand(int type, String cmd) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(cmd);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(42, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void triggerOtaSP(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(43, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onStkMenuReset(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(44, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void resetAttachApnInd(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(45, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void mdChangedApnInd(int type, int apnClassType) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(apnClassType);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(46, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void pcoDataAfterAttached(int type, PcoDataAttachedInfo pcoData) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            pcoData.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(47, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void confSRVCC(int type, ArrayList<Integer> callIds) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(callIds);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(48, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onMdDataRetryCountReset(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(49, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onRemoveRestrictEutran(int type) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(50, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onTxPowerIndication(int type, ArrayList<Integer> indPower) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(indPower);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(51, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onPseudoCellInfoInd(int type, ArrayList<Integer> cellInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(cellInfo);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(52, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onMccMncChanged(int type, String mccmnc) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(mccmnc);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(53, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onTxPowerStatusIndication(int type, ArrayList<Integer> indPower) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(indPower);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(54, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void networkRejectCauseInd(int type, ArrayList<Integer> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(55, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void dsbpStateChanged(int indicationType, int dsbpState) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(indicationType);
            _hidl_request.writeInt32(dsbpState);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(56, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void smlSlotLockInfoChangedInd(int type, ArrayList<Integer> info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(57, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onSimPowerChangedInd(int type, ArrayList<Integer> info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(58, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void networkBandInfoInd(int type, ArrayList<Integer> state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(59, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void smsInfoExtInd(int type, String info) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(info);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(60, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onDsdaChangedInd(int type, int mode) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(mode);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(61, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void qualifiedNetworkTypesChangedInd(int type, ArrayList<Integer> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(62, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onCellularQualityChangedInd(int type, ArrayList<Integer> indStgs) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(indStgs);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(63, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void mobileDataUsageInd(int type, ArrayList<Integer> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(64, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onNwLimitInd(int type, ArrayList<Integer> state) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(state);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(65, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void iccidChanged(int type, String iccid) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(iccid);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(66, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onPlmnDataInd(int type, PlmnMvnoInfo plmnMvnoInfo) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            plmnMvnoInfo.writeToParcel(_hidl_request);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(67, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void onRsuEvent(int type, int eventId, String eventString) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32(eventId);
            _hidl_request.writeString(eventString);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(68, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication
        public void toeInfoInd(int type, String longName, String shortName, String numeric) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(longName);
            _hidl_request.writeString(shortName);
            _hidl_request.writeString(numeric);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(69, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication
        public void sib16TimeInfoInd(int type, String sib16Time, long receivedTime) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IMtkRadioExIndication.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(sib16Time);
            _hidl_request.writeInt64(receivedTime);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(70, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
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

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends HwBinder implements IMtkRadioExIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(IMtkRadioExIndication.kInterfaceName, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName, "android.hidl.base@1.0::IBase"));
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public void debug(NativeHandle fd, ArrayList<String> options) {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final String interfaceDescriptor() {
            return IMtkRadioExIndication.kInterfaceName;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{74, 16, -49, 91, -64, 32, -67, 113, 122, -3, 51, 0, -64, 72, -98, 22, 113, -4, 85, -52, 87, -121, 89, 82, 85, 125, -2, 116, 1, 115, 23, -27}, new byte[]{18, 91, 85, -48, 5, 34, -1, 15, -16, 125, 70, -85, -117, -12, -7, 21, -116, -116, 92, -75, -74, 44, -38, 120, 39, 41, 102, -107, 50, 100, 12, 14}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final void setHALInstrumentation() {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final void ping() {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // vendor.mediatek.hardware.mtkradioex.V2_4.IMtkRadioExIndication, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public IHwInterface queryLocalInterface(String descriptor) {
            if (IMtkRadioExIndication.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type = _hidl_request.readInt32();
                    String info = _hidl_request.readString();
                    eMBMSAtInfoIndication(type, info);
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type2 = _hidl_request.readInt32();
                    int status = _hidl_request.readInt32();
                    eMBMSSessionStatusIndication(type2, status);
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type3 = _hidl_request.readInt32();
                    int isPhbReady = _hidl_request.readInt32();
                    phbReadyNotification(type3, isPhbReady);
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type4 = _hidl_request.readInt32();
                    CfuStatusNotification cfuStatus = new CfuStatusNotification();
                    cfuStatus.readFromParcel(_hidl_request);
                    cfuStatusNotify(type4, cfuStatus);
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type5 = _hidl_request.readInt32();
                    IncomingCallNotification inCallNotify = new IncomingCallNotification();
                    inCallNotify.readFromParcel(_hidl_request);
                    incomingCallIndication(type5, inCallNotify);
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type6 = _hidl_request.readInt32();
                    int ciType = _hidl_request.readInt32();
                    ArrayList<String> info2 = _hidl_request.readStringVector();
                    callAdditionalInfoInd(type6, ciType, info2);
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type7 = _hidl_request.readInt32();
                    CipherNotification cipherNotify = new CipherNotification();
                    cipherNotify.readFromParcel(_hidl_request);
                    cipherIndication(type7, cipherNotify);
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type8 = _hidl_request.readInt32();
                    SuppSvcNotification suppSvc = new SuppSvcNotification();
                    suppSvc.readFromParcel(_hidl_request);
                    suppSvcNotifyEx(type8, suppSvc);
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type9 = _hidl_request.readInt32();
                    CrssNotification crssNotify = new CrssNotification();
                    crssNotify.readFromParcel(_hidl_request);
                    crssIndication(type9, crssNotify);
                    return;
                case 10:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type10 = _hidl_request.readInt32();
                    cdmaCallAccepted(type10);
                    return;
                case 11:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type11 = _hidl_request.readInt32();
                    String ecc_list_with_card = _hidl_request.readString();
                    String ecc_list_no_card = _hidl_request.readString();
                    eccNumIndication(type11, ecc_list_with_card, ecc_list_no_card);
                    return;
                case 12:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type12 = _hidl_request.readInt32();
                    int simInserted = _hidl_request.readInt32();
                    onVirtualSimStatusChanged(type12, simInserted);
                    return;
                case 13:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type13 = _hidl_request.readInt32();
                    onImeiLock(type13);
                    return;
                case 14:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type14 = _hidl_request.readInt32();
                    onImsiRefreshDone(type14);
                    return;
                case 15:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type15 = _hidl_request.readInt32();
                    int event = _hidl_request.readInt32();
                    String info3 = _hidl_request.readString();
                    onSimHotSwapInd(type15, event, info3);
                    return;
                case 16:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type16 = _hidl_request.readInt32();
                    onSimMeLockEvent(type16);
                    return;
                case 17:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type17 = _hidl_request.readInt32();
                    int eventId = _hidl_request.readInt32();
                    onRsuSimLockEvent(type17, eventId);
                    return;
                case 18:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type18 = _hidl_request.readInt32();
                    onCardDetectedInd(type18);
                    return;
                case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type19 = _hidl_request.readInt32();
                    VsimOperationEvent event2 = new VsimOperationEvent();
                    event2.readFromParcel(_hidl_request);
                    onVsimEventIndication(type19, event2);
                    return;
                case 20:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type20 = _hidl_request.readInt32();
                    EtwsNotification etws = new EtwsNotification();
                    etws.readFromParcel(_hidl_request);
                    newEtwsInd(type20, etws);
                    return;
                case WorldMode.MD_WORLD_MODE_LFCTG /* 21 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type21 = _hidl_request.readInt32();
                    meSmsStorageFullInd(type21);
                    return;
                case 22:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type22 = _hidl_request.readInt32();
                    smsReadyInd(type22);
                    return;
                case 23:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type23 = _hidl_request.readInt32();
                    ArrayList<Byte> data = _hidl_request.readInt8Vector();
                    oemHookRaw(type23, data);
                    return;
                case 24:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type24 = _hidl_request.readInt32();
                    ArrayList<String> plmns = _hidl_request.readStringVector();
                    plmnChangedIndication(type24, plmns);
                    return;
                case 25:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type25 = _hidl_request.readInt32();
                    ArrayList<Integer> sessionIds = _hidl_request.readInt32Vector();
                    registrationSuspendedIndication(type25, sessionIds);
                    return;
                case 26:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type26 = _hidl_request.readInt32();
                    ArrayList<Integer> gmsss = _hidl_request.readInt32Vector();
                    gmssRatChangedIndication(type26, gmsss);
                    return;
                case 27:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type27 = _hidl_request.readInt32();
                    ArrayList<Integer> modes = _hidl_request.readInt32Vector();
                    worldModeChangedIndication(type27, modes);
                    return;
                case 28:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type28 = _hidl_request.readInt32();
                    String esnMeid = _hidl_request.readString();
                    esnMeidChangeInd(type28, esnMeid);
                    return;
                case 29:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type29 = _hidl_request.readInt32();
                    ArrayList<String> state = _hidl_request.readStringVector();
                    responseCsNetworkStateChangeInd(type29, state);
                    return;
                case IWorldPhone.EVENT_REG_SUSPENDED_1 /* 30 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type30 = _hidl_request.readInt32();
                    ArrayList<Integer> state2 = _hidl_request.readInt32Vector();
                    responsePsNetworkStateChangeInd(type30, state2);
                    return;
                case IWorldPhone.EVENT_REG_SUSPENDED_2 /* 31 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type31 = _hidl_request.readInt32();
                    ArrayList<String> state3 = _hidl_request.readStringVector();
                    responseInvalidSimInd(type31, state3);
                    return;
                case 32:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type32 = _hidl_request.readInt32();
                    ArrayList<Integer> event3 = _hidl_request.readInt32Vector();
                    responseNetworkEventInd(type32, event3);
                    return;
                case 33:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type33 = _hidl_request.readInt32();
                    ArrayList<Integer> modulation = _hidl_request.readInt32Vector();
                    responseModulationInfoInd(type33, modulation);
                    return;
                case 34:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type34 = _hidl_request.readInt32();
                    int allowed = _hidl_request.readInt32();
                    dataAllowedNotification(type34, allowed);
                    return;
                case 35:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type35 = _hidl_request.readInt32();
                    ArrayList<String> info4 = _hidl_request.readStringVector();
                    responseFemtocellInfo(type35, info4);
                    return;
                case 36:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type36 = _hidl_request.readInt32();
                    ArrayList<String> networkinfo = _hidl_request.readStringVector();
                    networkInfoInd(type36, networkinfo);
                    return;
                case 37:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type37 = _hidl_request.readInt32();
                    SignalStrengthWithWcdmaEcio signalStrength = new SignalStrengthWithWcdmaEcio();
                    signalStrength.readFromParcel(_hidl_request);
                    currentSignalStrengthWithWcdmaEcioInd(type37, signalStrength);
                    return;
                case 38:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type38 = _hidl_request.readInt32();
                    int lteBand = _hidl_request.readInt32();
                    responseLteNetworkInfo(type38, lteBand);
                    return;
                case 39:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type39 = _hidl_request.readInt32();
                    DedicateDataCall ddcData = new DedicateDataCall();
                    ddcData.readFromParcel(_hidl_request);
                    dedicatedBearerActivationInd(type39, ddcData);
                    return;
                case 40:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type40 = _hidl_request.readInt32();
                    DedicateDataCall ddcData2 = new DedicateDataCall();
                    ddcData2.readFromParcel(_hidl_request);
                    dedicatedBearerModificationInd(type40, ddcData2);
                    return;
                case 41:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type41 = _hidl_request.readInt32();
                    int cid = _hidl_request.readInt32();
                    dedicatedBearerDeactivationInd(type41, cid);
                    return;
                case 42:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type42 = _hidl_request.readInt32();
                    String cmd = _hidl_request.readString();
                    bipProactiveCommand(type42, cmd);
                    return;
                case 43:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type43 = _hidl_request.readInt32();
                    triggerOtaSP(type43);
                    return;
                case 44:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type44 = _hidl_request.readInt32();
                    onStkMenuReset(type44);
                    return;
                case 45:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type45 = _hidl_request.readInt32();
                    resetAttachApnInd(type45);
                    return;
                case MtkCatService.MSG_ID_CACHED_DISPLAY_TEXT_TIMEOUT /* 46 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type46 = _hidl_request.readInt32();
                    int apnClassType = _hidl_request.readInt32();
                    mdChangedApnInd(type46, apnClassType);
                    return;
                case MtkCatService.MSG_ID_CONN_RETRY_TIMEOUT /* 47 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type47 = _hidl_request.readInt32();
                    PcoDataAttachedInfo pcoData = new PcoDataAttachedInfo();
                    pcoData.readFromParcel(_hidl_request);
                    pcoDataAfterAttached(type47, pcoData);
                    return;
                case 48:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type48 = _hidl_request.readInt32();
                    ArrayList<Integer> callIds = _hidl_request.readInt32Vector();
                    confSRVCC(type48, callIds);
                    return;
                case 49:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type49 = _hidl_request.readInt32();
                    onMdDataRetryCountReset(type49);
                    return;
                case 50:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type50 = _hidl_request.readInt32();
                    onRemoveRestrictEutran(type50);
                    return;
                case 51:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type51 = _hidl_request.readInt32();
                    ArrayList<Integer> indPower = _hidl_request.readInt32Vector();
                    onTxPowerIndication(type51, indPower);
                    return;
                case 52:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type52 = _hidl_request.readInt32();
                    ArrayList<Integer> cellInfo = _hidl_request.readInt32Vector();
                    onPseudoCellInfoInd(type52, cellInfo);
                    return;
                case 53:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type53 = _hidl_request.readInt32();
                    String mccmnc = _hidl_request.readString();
                    onMccMncChanged(type53, mccmnc);
                    return;
                case 54:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type54 = _hidl_request.readInt32();
                    ArrayList<Integer> indPower2 = _hidl_request.readInt32Vector();
                    onTxPowerStatusIndication(type54, indPower2);
                    return;
                case 55:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type55 = _hidl_request.readInt32();
                    ArrayList<Integer> data2 = _hidl_request.readInt32Vector();
                    networkRejectCauseInd(type55, data2);
                    return;
                case 56:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int indicationType = _hidl_request.readInt32();
                    int dsbpState = _hidl_request.readInt32();
                    dsbpStateChanged(indicationType, dsbpState);
                    return;
                case 57:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type56 = _hidl_request.readInt32();
                    ArrayList<Integer> info5 = _hidl_request.readInt32Vector();
                    smlSlotLockInfoChangedInd(type56, info5);
                    return;
                case 58:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type57 = _hidl_request.readInt32();
                    ArrayList<Integer> info6 = _hidl_request.readInt32Vector();
                    onSimPowerChangedInd(type57, info6);
                    return;
                case 59:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type58 = _hidl_request.readInt32();
                    ArrayList<Integer> state4 = _hidl_request.readInt32Vector();
                    networkBandInfoInd(type58, state4);
                    return;
                case 60:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type59 = _hidl_request.readInt32();
                    String info7 = _hidl_request.readString();
                    smsInfoExtInd(type59, info7);
                    return;
                case IWorldPhone.EVENT_INVALID_SIM_NOTIFY_2 /* 61 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type60 = _hidl_request.readInt32();
                    int mode = _hidl_request.readInt32();
                    onDsdaChangedInd(type60, mode);
                    return;
                case IWorldPhone.EVENT_INVALID_SIM_NOTIFY_3 /* 62 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type61 = _hidl_request.readInt32();
                    ArrayList<Integer> data3 = _hidl_request.readInt32Vector();
                    qualifiedNetworkTypesChangedInd(type61, data3);
                    return;
                case IWorldPhone.EVENT_INVALID_SIM_NOTIFY_4 /* 63 */:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type62 = _hidl_request.readInt32();
                    ArrayList<Integer> indStgs = _hidl_request.readInt32Vector();
                    onCellularQualityChangedInd(type62, indStgs);
                    return;
                case 64:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type63 = _hidl_request.readInt32();
                    ArrayList<Integer> data4 = _hidl_request.readInt32Vector();
                    mobileDataUsageInd(type63, data4);
                    return;
                case 65:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type64 = _hidl_request.readInt32();
                    ArrayList<Integer> state5 = _hidl_request.readInt32Vector();
                    onNwLimitInd(type64, state5);
                    return;
                case 66:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type65 = _hidl_request.readInt32();
                    String iccid = _hidl_request.readString();
                    iccidChanged(type65, iccid);
                    return;
                case 67:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type66 = _hidl_request.readInt32();
                    PlmnMvnoInfo plmnMvnoInfo = new PlmnMvnoInfo();
                    plmnMvnoInfo.readFromParcel(_hidl_request);
                    onPlmnDataInd(type66, plmnMvnoInfo);
                    return;
                case 68:
                    _hidl_request.enforceInterface(vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication.kInterfaceName);
                    int type67 = _hidl_request.readInt32();
                    int eventId2 = _hidl_request.readInt32();
                    String eventString = _hidl_request.readString();
                    onRsuEvent(type67, eventId2, eventString);
                    return;
                case 69:
                    _hidl_request.enforceInterface(IMtkRadioExIndication.kInterfaceName);
                    int type68 = _hidl_request.readInt32();
                    String longName = _hidl_request.readString();
                    String shortName = _hidl_request.readString();
                    String numeric = _hidl_request.readString();
                    toeInfoInd(type68, longName, shortName, numeric);
                    return;
                case IWorldPhone.EVENT_RESUME_CAMPING_1 /* 70 */:
                    _hidl_request.enforceInterface(IMtkRadioExIndication.kInterfaceName);
                    int type69 = _hidl_request.readInt32();
                    String sib16Time = _hidl_request.readString();
                    long receivedTime = _hidl_request.readInt64();
                    sib16TimeInfoInd(type69, sib16Time, receivedTime);
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
