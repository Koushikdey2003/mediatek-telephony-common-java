package vendor.mediatek.hardware.mtkradioex.mwi;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMwiRadioResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$mwi$IMwiRadioResponse".replace('$', '.');
    public static final String HASH = "0857e51e04bcb3ae03f5ee374ee6d3ca41478e7b";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getWfcConfigResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void notifyEPDGScreenStateResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setEmergencyAddressIdResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLocationInfoResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNattKeepAliveStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWfcConfigResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWifiAssociatedResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWifiEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWifiIpAddressResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWifiPingResultResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWifiSignalLevelResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMwiRadioResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void getWfcConfigResponse(RadioResponseInfo responseInfo, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void notifyEPDGScreenStateResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setEmergencyAddressIdResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setLocationInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setNattKeepAliveStatusResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setWfcConfigResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setWifiAssociatedResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setWifiEnabledResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setWifiIpAddressResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setWifiPingResultResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public void setWifiSignalLevelResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMwiRadioResponse {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getWfcConfigResponse = 1;
        static final int TRANSACTION_notifyEPDGScreenStateResponse = 2;
        static final int TRANSACTION_setEmergencyAddressIdResponse = 3;
        static final int TRANSACTION_setLocationInfoResponse = 4;
        static final int TRANSACTION_setNattKeepAliveStatusResponse = 5;
        static final int TRANSACTION_setWfcConfigResponse = 6;
        static final int TRANSACTION_setWifiAssociatedResponse = 7;
        static final int TRANSACTION_setWifiEnabledResponse = 8;
        static final int TRANSACTION_setWifiIpAddressResponse = 9;
        static final int TRANSACTION_setWifiPingResultResponse = 10;
        static final int TRANSACTION_setWifiSignalLevelResponse = 11;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMwiRadioResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMwiRadioResponse)) {
                return (IMwiRadioResponse) iin;
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
                            getWfcConfigResponse(_arg0, _arg1);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            notifyEPDGScreenStateResponse(_arg02);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setEmergencyAddressIdResponse(_arg03);
                            return true;
                        case 4:
                            RadioResponseInfo _arg04 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setLocationInfoResponse(_arg04);
                            return true;
                        case 5:
                            RadioResponseInfo _arg05 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setNattKeepAliveStatusResponse(_arg05);
                            return true;
                        case 6:
                            RadioResponseInfo _arg06 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWfcConfigResponse(_arg06);
                            return true;
                        case 7:
                            RadioResponseInfo _arg07 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWifiAssociatedResponse(_arg07);
                            return true;
                        case 8:
                            RadioResponseInfo _arg08 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWifiEnabledResponse(_arg08);
                            return true;
                        case 9:
                            RadioResponseInfo _arg09 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWifiIpAddressResponse(_arg09);
                            return true;
                        case 10:
                            RadioResponseInfo _arg010 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWifiPingResultResponse(_arg010);
                            return true;
                        case 11:
                            RadioResponseInfo _arg011 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWifiSignalLevelResponse(_arg011);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMwiRadioResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void getWfcConfigResponse(RadioResponseInfo responseInfo, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getWfcConfigResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void notifyEPDGScreenStateResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method notifyEPDGScreenStateResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setEmergencyAddressIdResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setEmergencyAddressIdResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setLocationInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLocationInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setNattKeepAliveStatusResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNattKeepAliveStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setWfcConfigResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWfcConfigResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setWifiAssociatedResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiAssociatedResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setWifiEnabledResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiEnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setWifiIpAddressResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiIpAddressResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setWifiPingResultResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiPingResultResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
            public void setWifiSignalLevelResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiSignalLevelResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse
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
