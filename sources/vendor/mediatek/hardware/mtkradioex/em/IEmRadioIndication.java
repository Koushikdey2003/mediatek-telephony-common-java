package vendor.mediatek.hardware.mtkradioex.em;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IEmRadioIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$em$IEmRadioIndication".replace('$', '.');
    public static final String HASH = "d31aa9b58576753a4f505200278461c070d291a5";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void networkInfoInd(int i, String[] strArr) throws RemoteException;

    void oemHookRaw(int i, byte[] bArr) throws RemoteException;

    void onTxPowerIndication(int i, int[] iArr) throws RemoteException;

    void radioStateChanged(int i, int i2) throws RemoteException;

    public static class Default implements IEmRadioIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
        public void networkInfoInd(int type, String[] networkinfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
        public void oemHookRaw(int type, byte[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
        public void onTxPowerIndication(int type, int[] indPower) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
        public void radioStateChanged(int type, int radioState) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEmRadioIndication {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_networkInfoInd = 1;
        static final int TRANSACTION_oemHookRaw = 2;
        static final int TRANSACTION_onTxPowerIndication = 3;
        static final int TRANSACTION_radioStateChanged = 4;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IEmRadioIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IEmRadioIndication)) {
                return (IEmRadioIndication) iin;
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
                            networkInfoInd(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            byte[] _arg12 = data.createByteArray();
                            data.enforceNoDataAvail();
                            oemHookRaw(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int[] _arg13 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onTxPowerIndication(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            radioStateChanged(_arg04, _arg14);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IEmRadioIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
            public void networkInfoInd(int type, String[] networkinfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(networkinfo);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method networkInfoInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
            public void oemHookRaw(int type, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method oemHookRaw is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
            public void onTxPowerIndication(int type, int[] indPower) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(indPower);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onTxPowerIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
            public void radioStateChanged(int type, int radioState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(radioState);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method radioStateChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication
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
