package vendor.mediatek.hardware.mtkradioex.em;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IEmRadioResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$em$IEmRadioResponse".replace('$', '.');
    public static final String HASH = "d31aa9b58576753a4f505200278461c070d291a5";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void sendRequestRawResponse(RadioResponseInfo radioResponseInfo, byte[] bArr) throws RemoteException;

    void sendRequestStringsResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    public static class Default implements IEmRadioResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
        public void sendRequestRawResponse(RadioResponseInfo info, byte[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
        public void sendRequestStringsResponse(RadioResponseInfo info, String[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEmRadioResponse {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_sendRequestRawResponse = 1;
        static final int TRANSACTION_sendRequestStringsResponse = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IEmRadioResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IEmRadioResponse)) {
                return (IEmRadioResponse) iin;
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
                            byte[] _arg1 = data.createByteArray();
                            data.enforceNoDataAvail();
                            sendRequestRawResponse(_arg0, _arg1);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg12 = data.createStringArray();
                            data.enforceNoDataAvail();
                            sendRequestStringsResponse(_arg02, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IEmRadioResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
            public void sendRequestRawResponse(RadioResponseInfo info, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRequestRawResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
            public void sendRequestStringsResponse(RadioResponseInfo info, String[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(data);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRequestStringsResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse
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
