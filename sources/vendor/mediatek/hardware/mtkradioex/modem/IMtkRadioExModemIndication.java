package vendor.mediatek.hardware.mtkradioex.modem;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExModemIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$modem$IMtkRadioExModemIndication".replace('$', '.');
    public static final String HASH = "87512b9a1978fdb596a8d9176854d3761382dc82";
    public static final int VERSION = 2;

    void dsbpStateChanged(int i, int i2) throws RemoteException;

    void eMBMSAtInfoIndication(int i, String str) throws RemoteException;

    void eMBMSSessionStatusIndication(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void oemHookRaw(int i, byte[] bArr) throws RemoteException;

    void onCellularQualityChangedInd(int i, int[] iArr) throws RemoteException;

    void onTxPowerIndication(int i, int[] iArr) throws RemoteException;

    void onTxPowerStatusIndication(int i, int[] iArr) throws RemoteException;

    void worldModeChangedIndication(int i, int[] iArr) throws RemoteException;

    public static class Default implements IMtkRadioExModemIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void dsbpStateChanged(int indicationType, int dsbpState) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void eMBMSAtInfoIndication(int type, String info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void eMBMSSessionStatusIndication(int type, int status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void oemHookRaw(int type, byte[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void onTxPowerIndication(int type, int[] indPower) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void onTxPowerStatusIndication(int type, int[] indPower) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void worldModeChangedIndication(int type, int[] modes) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public void onCellularQualityChangedInd(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExModemIndication {
        static final int TRANSACTION_dsbpStateChanged = 1;
        static final int TRANSACTION_eMBMSAtInfoIndication = 2;
        static final int TRANSACTION_eMBMSSessionStatusIndication = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_oemHookRaw = 4;
        static final int TRANSACTION_onCellularQualityChangedInd = 8;
        static final int TRANSACTION_onTxPowerIndication = 5;
        static final int TRANSACTION_onTxPowerStatusIndication = 6;
        static final int TRANSACTION_worldModeChangedIndication = 7;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExModemIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExModemIndication)) {
                return (IMtkRadioExModemIndication) iin;
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
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            dsbpStateChanged(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            eMBMSAtInfoIndication(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            eMBMSSessionStatusIndication(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            byte[] _arg14 = data.createByteArray();
                            data.enforceNoDataAvail();
                            oemHookRaw(_arg04, _arg14);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int[] _arg15 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onTxPowerIndication(_arg05, _arg15);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int[] _arg16 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onTxPowerStatusIndication(_arg06, _arg16);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int[] _arg17 = data.createIntArray();
                            data.enforceNoDataAvail();
                            worldModeChangedIndication(_arg07, _arg17);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int[] _arg18 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onCellularQualityChangedInd(_arg08, _arg18);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExModemIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void dsbpStateChanged(int indicationType, int dsbpState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(indicationType);
                    _data.writeInt(dsbpState);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dsbpStateChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void eMBMSAtInfoIndication(int type, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method eMBMSAtInfoIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void eMBMSSessionStatusIndication(int type, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method eMBMSSessionStatusIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void oemHookRaw(int type, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method oemHookRaw is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void onTxPowerIndication(int type, int[] indPower) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indPower);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onTxPowerIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void onTxPowerStatusIndication(int type, int[] indPower) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indPower);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onTxPowerStatusIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void worldModeChangedIndication(int type, int[] modes) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(modes);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method worldModeChangedIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
            public void onCellularQualityChangedInd(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onCellularQualityChangedInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
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
