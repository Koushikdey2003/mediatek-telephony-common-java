package vendor.mediatek.hardware.mtkradioex.smartratswitch;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface ISmartRatSwitchRadioResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$smartratswitch$ISmartRatSwitchRadioResponse".replace('$', '.');
    public static final String HASH = "b9bfcf6a1d07eb6465af9664f5d5c5f3404c4e3c";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getSmartRatSwitchResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void setSmartSceneSwitchResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void smartRatSwitchResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements ISmartRatSwitchRadioResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
        public void getSmartRatSwitchResponse(RadioResponseInfo info, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
        public void setSmartSceneSwitchResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
        public void smartRatSwitchResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISmartRatSwitchRadioResponse {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getSmartRatSwitchResponse = 1;
        static final int TRANSACTION_setSmartSceneSwitchResponse = 2;
        static final int TRANSACTION_smartRatSwitchResponse = 3;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static ISmartRatSwitchRadioResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISmartRatSwitchRadioResponse)) {
                return (ISmartRatSwitchRadioResponse) iin;
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
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            getSmartRatSwitchResponse(_arg0, _arg1);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setSmartSceneSwitchResponse(_arg02);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            smartRatSwitchResponse(_arg03);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements ISmartRatSwitchRadioResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
            public void getSmartRatSwitchResponse(RadioResponseInfo info, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getSmartRatSwitchResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
            public void setSmartSceneSwitchResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSmartSceneSwitchResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
            public void smartRatSwitchResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method smartRatSwitchResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse
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
