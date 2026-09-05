package vendor.mediatek.hardware.mtkradioex.modem;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExModemResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$modem$IMtkRadioExModemResponse".replace('$', '.');
    public static final String HASH = "87512b9a1978fdb596a8d9176854d3761382dc82";
    public static final int VERSION = 2;

    void getEngineeringModeInfoResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void modifyModemTypeResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void registerCellQltyReportResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void restartRILDResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void runGbaAuthenticationResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void sendEmbmsAtCommandResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void sendRequestRawResponse(RadioResponseInfo radioResponseInfo, byte[] bArr) throws RemoteException;

    void sendRequestStringsResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void sendSarIndicatorResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendWifiAssociatedResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendWifiEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendWifiIpAddressResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setModemPowerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setTrmResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setTxPowerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setTxPowerStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setVendorSettingResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void triggerModeSwitchByEccResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExModemResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void getEngineeringModeInfoResponse(RadioResponseInfo responseInfo, String[] result) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void modifyModemTypeResponse(RadioResponseInfo info, int applyType) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void restartRILDResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void runGbaAuthenticationResponse(RadioResponseInfo info, String[] resList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendEmbmsAtCommandResponse(RadioResponseInfo responseInfo, String data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendRequestRawResponse(RadioResponseInfo info, byte[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendRequestStringsResponse(RadioResponseInfo info, String[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendSarIndicatorResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void setModemPowerResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void setTrmResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void setTxPowerResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void setTxPowerStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void setVendorSettingResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void triggerModeSwitchByEccResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendWifiAssociatedResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendWifiEnabledResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void sendWifiIpAddressResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public void registerCellQltyReportResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExModemResponse {
        static final int TRANSACTION_getEngineeringModeInfoResponse = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_modifyModemTypeResponse = 2;
        static final int TRANSACTION_registerCellQltyReportResponse = 18;
        static final int TRANSACTION_restartRILDResponse = 3;
        static final int TRANSACTION_runGbaAuthenticationResponse = 4;
        static final int TRANSACTION_sendEmbmsAtCommandResponse = 5;
        static final int TRANSACTION_sendRequestRawResponse = 6;
        static final int TRANSACTION_sendRequestStringsResponse = 7;
        static final int TRANSACTION_sendSarIndicatorResponse = 8;
        static final int TRANSACTION_sendWifiAssociatedResponse = 15;
        static final int TRANSACTION_sendWifiEnabledResponse = 16;
        static final int TRANSACTION_sendWifiIpAddressResponse = 17;
        static final int TRANSACTION_setModemPowerResponse = 9;
        static final int TRANSACTION_setTrmResponse = 10;
        static final int TRANSACTION_setTxPowerResponse = 11;
        static final int TRANSACTION_setTxPowerStatusResponse = 12;
        static final int TRANSACTION_setVendorSettingResponse = 13;
        static final int TRANSACTION_triggerModeSwitchByEccResponse = 14;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExModemResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExModemResponse)) {
                return (IMtkRadioExModemResponse) iin;
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
                            String[] _arg1 = data.createStringArray();
                            data.enforceNoDataAvail();
                            getEngineeringModeInfoResponse(_arg0, _arg1);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            modifyModemTypeResponse(_arg02, _arg12);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            restartRILDResponse(_arg03);
                            return true;
                        case 4:
                            RadioResponseInfo _arg04 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg13 = data.createStringArray();
                            data.enforceNoDataAvail();
                            runGbaAuthenticationResponse(_arg04, _arg13);
                            return true;
                        case 5:
                            RadioResponseInfo _arg05 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            sendEmbmsAtCommandResponse(_arg05, _arg14);
                            return true;
                        case 6:
                            RadioResponseInfo _arg06 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            byte[] _arg15 = data.createByteArray();
                            data.enforceNoDataAvail();
                            sendRequestRawResponse(_arg06, _arg15);
                            return true;
                        case 7:
                            RadioResponseInfo _arg07 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg16 = data.createStringArray();
                            data.enforceNoDataAvail();
                            sendRequestStringsResponse(_arg07, _arg16);
                            return true;
                        case 8:
                            RadioResponseInfo _arg08 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendSarIndicatorResponse(_arg08);
                            return true;
                        case 9:
                            RadioResponseInfo _arg09 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setModemPowerResponse(_arg09);
                            return true;
                        case 10:
                            RadioResponseInfo _arg010 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setTrmResponse(_arg010);
                            return true;
                        case 11:
                            RadioResponseInfo _arg011 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setTxPowerResponse(_arg011);
                            return true;
                        case 12:
                            RadioResponseInfo _arg012 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setTxPowerStatusResponse(_arg012);
                            return true;
                        case 13:
                            RadioResponseInfo _arg013 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setVendorSettingResponse(_arg013);
                            return true;
                        case 14:
                            RadioResponseInfo _arg014 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            triggerModeSwitchByEccResponse(_arg014);
                            return true;
                        case 15:
                            RadioResponseInfo _arg015 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendWifiAssociatedResponse(_arg015);
                            return true;
                        case 16:
                            RadioResponseInfo _arg016 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendWifiEnabledResponse(_arg016);
                            return true;
                        case 17:
                            RadioResponseInfo _arg017 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendWifiIpAddressResponse(_arg017);
                            return true;
                        case 18:
                            RadioResponseInfo _arg018 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            registerCellQltyReportResponse(_arg018);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExModemResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void getEngineeringModeInfoResponse(RadioResponseInfo responseInfo, String[] result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(result);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getEngineeringModeInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void modifyModemTypeResponse(RadioResponseInfo info, int applyType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(applyType);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method modifyModemTypeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void restartRILDResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method restartRILDResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void runGbaAuthenticationResponse(RadioResponseInfo info, String[] resList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(resList);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method runGbaAuthenticationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendEmbmsAtCommandResponse(RadioResponseInfo responseInfo, String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendEmbmsAtCommandResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendRequestRawResponse(RadioResponseInfo info, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRequestRawResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendRequestStringsResponse(RadioResponseInfo info, String[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(data);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRequestStringsResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendSarIndicatorResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendSarIndicatorResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void setModemPowerResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setModemPowerResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void setTrmResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTrmResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void setTxPowerResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTxPowerResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void setTxPowerStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTxPowerStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void setVendorSettingResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setVendorSettingResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void triggerModeSwitchByEccResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method triggerModeSwitchByEccResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendWifiAssociatedResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendWifiAssociatedResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendWifiEnabledResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendWifiEnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void sendWifiIpAddressResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendWifiIpAddressResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
            public void registerCellQltyReportResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method registerCellQltyReportResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse
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
