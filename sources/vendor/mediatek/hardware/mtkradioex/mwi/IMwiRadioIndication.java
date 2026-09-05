package vendor.mediatek.hardware.mtkradioex.mwi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMwiRadioIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$mwi$IMwiRadioIndication".replace('$', '.');
    public static final String HASH = "0857e51e04bcb3ae03f5ee374ee6d3ca41478e7b";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void onLocationRequest(int i, String[] strArr) throws RemoteException;

    void onNattKeepAliveChanged(int i, String[] strArr) throws RemoteException;

    void onPdnHandover(int i, int[] iArr) throws RemoteException;

    void onWfcPdnError(int i, int[] iArr) throws RemoteException;

    void onWfcPdnStateChanged(int i, int[] iArr) throws RemoteException;

    void onWifiLock(int i, String[] strArr) throws RemoteException;

    void onWifiMonitoringThreshouldChanged(int i, int[] iArr) throws RemoteException;

    void onWifiPdnActivate(int i, int[] iArr) throws RemoteException;

    void onWifiPdnOOS(int i, String[] strArr) throws RemoteException;

    void onWifiPingRequest(int i, int[] iArr) throws RemoteException;

    void onWifiRoveout(int i, String[] strArr) throws RemoteException;

    public static class Default implements IMwiRadioIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onLocationRequest(int type, String[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onNattKeepAliveChanged(int type, String[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onPdnHandover(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWfcPdnError(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWfcPdnStateChanged(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWifiLock(int type, String[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWifiMonitoringThreshouldChanged(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWifiPdnActivate(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWifiPdnOOS(int type, String[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWifiPingRequest(int type, int[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public void onWifiRoveout(int type, String[] indStgs) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMwiRadioIndication {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onLocationRequest = 1;
        static final int TRANSACTION_onNattKeepAliveChanged = 2;
        static final int TRANSACTION_onPdnHandover = 3;
        static final int TRANSACTION_onWfcPdnError = 4;
        static final int TRANSACTION_onWfcPdnStateChanged = 5;
        static final int TRANSACTION_onWifiLock = 6;
        static final int TRANSACTION_onWifiMonitoringThreshouldChanged = 7;
        static final int TRANSACTION_onWifiPdnActivate = 8;
        static final int TRANSACTION_onWifiPdnOOS = 9;
        static final int TRANSACTION_onWifiPingRequest = 10;
        static final int TRANSACTION_onWifiRoveout = 11;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMwiRadioIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMwiRadioIndication)) {
                return (IMwiRadioIndication) iin;
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
                            String[] _arg1 = data.createStringArray();
                            data.enforceNoDataAvail();
                            onLocationRequest(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            String[] _arg12 = data.createStringArray();
                            data.enforceNoDataAvail();
                            onNattKeepAliveChanged(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int[] _arg13 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onPdnHandover(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int[] _arg14 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onWfcPdnError(_arg04, _arg14);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int[] _arg15 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onWfcPdnStateChanged(_arg05, _arg15);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            String[] _arg16 = data.createStringArray();
                            data.enforceNoDataAvail();
                            onWifiLock(_arg06, _arg16);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int[] _arg17 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onWifiMonitoringThreshouldChanged(_arg07, _arg17);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int[] _arg18 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onWifiPdnActivate(_arg08, _arg18);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            String[] _arg19 = data.createStringArray();
                            data.enforceNoDataAvail();
                            onWifiPdnOOS(_arg09, _arg19);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            int[] _arg110 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onWifiPingRequest(_arg010, _arg110);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            String[] _arg111 = data.createStringArray();
                            data.enforceNoDataAvail();
                            onWifiRoveout(_arg011, _arg111);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMwiRadioIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onLocationRequest(int type, String[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(indStgs);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onLocationRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onNattKeepAliveChanged(int type, String[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(indStgs);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onNattKeepAliveChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onPdnHandover(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onPdnHandover is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWfcPdnError(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWfcPdnError is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWfcPdnStateChanged(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWfcPdnStateChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWifiLock(int type, String[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(indStgs);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWifiLock is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWifiMonitoringThreshouldChanged(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWifiMonitoringThreshouldChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWifiPdnActivate(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWifiPdnActivate is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWifiPdnOOS(int type, String[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(indStgs);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWifiPdnOOS is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWifiPingRequest(int type, int[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indStgs);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWifiPingRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
            public void onWifiRoveout(int type, String[] indStgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(indStgs);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onWifiRoveout is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication
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
