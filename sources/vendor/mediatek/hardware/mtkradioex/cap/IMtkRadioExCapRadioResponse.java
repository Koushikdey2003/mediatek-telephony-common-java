package vendor.mediatek.hardware.mtkradioex.cap;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExCapRadioResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$cap$IMtkRadioExCapRadioResponse".replace('$', '.');
    public static final String HASH = "e953434b808a081b9235e9fb5603d96e538e67e6";
    public static final int VERSION = 1;

    void abortCertificateResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableCapabilityResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void routeAuthMessageResponse(RadioResponseInfo radioResponseInfo, byte[] bArr, int i) throws RemoteException;

    void routeCertificateResponse(RadioResponseInfo radioResponseInfo, byte[] bArr, int i) throws RemoteException;

    public static class Default implements IMtkRadioExCapRadioResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
        public void abortCertificateResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
        public void enableCapabilityResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
        public void routeAuthMessageResponse(RadioResponseInfo info, byte[] devId, int capMask) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
        public void routeCertificateResponse(RadioResponseInfo info, byte[] rnd, int custId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExCapRadioResponse {
        static final int TRANSACTION_abortCertificateResponse = 1;
        static final int TRANSACTION_enableCapabilityResponse = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_routeAuthMessageResponse = 3;
        static final int TRANSACTION_routeCertificateResponse = 4;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExCapRadioResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExCapRadioResponse)) {
                return (IMtkRadioExCapRadioResponse) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code >= 1 && code <= TRANSACTION_getInterfaceVersion) {
                data.enforceInterface(descriptor);
            }
            switch (code) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    reply.writeNoException();
                    reply.writeString(getInterfaceHash());
                    return true;
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    reply.writeNoException();
                    reply.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            RadioResponseInfo _arg0 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            abortCertificateResponse(_arg0);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            enableCapabilityResponse(_arg02);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            byte[] _arg1 = data.createByteArray();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            routeAuthMessageResponse(_arg03, _arg1, _arg2);
                            return true;
                        case 4:
                            RadioResponseInfo _arg04 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            byte[] _arg12 = data.createByteArray();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            routeCertificateResponse(_arg04, _arg12, _arg22);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExCapRadioResponse {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
            public void abortCertificateResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method abortCertificateResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
            public void enableCapabilityResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableCapabilityResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
            public void routeAuthMessageResponse(RadioResponseInfo info, byte[] devId, int capMask) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeByteArray(devId);
                    _data.writeInt(capMask);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method routeAuthMessageResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
            public void routeCertificateResponse(RadioResponseInfo info, byte[] rnd, int custId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeByteArray(rnd);
                    _data.writeInt(custId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method routeCertificateResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
