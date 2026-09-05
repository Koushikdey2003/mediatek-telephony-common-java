package vendor.mediatek.hardware.mtkradioex.data;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExDataResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$data$IMtkRadioExDataResponse".replace('$', '.');
    public static final String HASH = "943612175caf84e3d94934844810892a1112b91e";
    public static final int VERSION = 2;

    void dataConnectionAttachResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void dataConnectionDetachResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableDsdaIndicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getDsdaStatusResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void resetAllConnectionsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void resetMdDataRetryCountResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setupDataCallResponseSlice(RadioResponseInfo radioResponseInfo, SetupDataCallResultSlice setupDataCallResultSlice) throws RemoteException;

    public static class Default implements IMtkRadioExDataResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void dataConnectionAttachResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void dataConnectionDetachResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void enableDsdaIndicationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void getDsdaStatusResponse(RadioResponseInfo info, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void resetAllConnectionsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void resetMdDataRetryCountResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public void setupDataCallResponseSlice(RadioResponseInfo info, SetupDataCallResultSlice dcResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExDataResponse {
        static final int TRANSACTION_dataConnectionAttachResponse = 1;
        static final int TRANSACTION_dataConnectionDetachResponse = 2;
        static final int TRANSACTION_enableDsdaIndicationResponse = 3;
        static final int TRANSACTION_getDsdaStatusResponse = 4;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_resetAllConnectionsResponse = 5;
        static final int TRANSACTION_resetMdDataRetryCountResponse = 6;
        static final int TRANSACTION_setupDataCallResponseSlice = 7;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExDataResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExDataResponse)) {
                return (IMtkRadioExDataResponse) iin;
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
                            dataConnectionAttachResponse(_arg0);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            dataConnectionDetachResponse(_arg02);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            enableDsdaIndicationResponse(_arg03);
                            return true;
                        case 4:
                            RadioResponseInfo _arg04 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            getDsdaStatusResponse(_arg04, _arg1);
                            return true;
                        case 5:
                            RadioResponseInfo _arg05 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            resetAllConnectionsResponse(_arg05);
                            return true;
                        case 6:
                            RadioResponseInfo _arg06 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            resetMdDataRetryCountResponse(_arg06);
                            return true;
                        case 7:
                            RadioResponseInfo _arg07 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            SetupDataCallResultSlice _arg12 = (SetupDataCallResultSlice) data.readTypedObject(SetupDataCallResultSlice.CREATOR);
                            data.enforceNoDataAvail();
                            setupDataCallResponseSlice(_arg07, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExDataResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void dataConnectionAttachResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dataConnectionAttachResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void dataConnectionDetachResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dataConnectionDetachResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void enableDsdaIndicationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableDsdaIndicationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void getDsdaStatusResponse(RadioResponseInfo info, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getDsdaStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void resetAllConnectionsResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method resetAllConnectionsResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void resetMdDataRetryCountResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method resetMdDataRetryCountResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
            public void setupDataCallResponseSlice(RadioResponseInfo info, SetupDataCallResultSlice dcResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(dcResponse, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setupDataCallResponseSlice is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataResponse
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
