package vendor.mediatek.hardware.mtkradioex.messaging;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExMessagingIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$messaging$IMtkRadioExMessagingIndication".replace('$', '.');
    public static final String HASH = "93a2d5af4d82e246ed35f8afe17083da08ba1147";
    public static final int VERSION = 1;

    void enterSCBMInd(int i) throws RemoteException;

    void esnMeidChangeInd(int i, String str) throws RemoteException;

    void exitSCBMInd(int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void meSmsStorageFullInd(int i) throws RemoteException;

    void smsInfoExtInd(int i, String str) throws RemoteException;

    void smsReadyInd(int i) throws RemoteException;

    public static class Default implements IMtkRadioExMessagingIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public void esnMeidChangeInd(int type, String esnMeid) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public void meSmsStorageFullInd(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public void smsInfoExtInd(int type, String info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public void smsReadyInd(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public void exitSCBMInd(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public void enterSCBMInd(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExMessagingIndication {
        static final int TRANSACTION_enterSCBMInd = 6;
        static final int TRANSACTION_esnMeidChangeInd = 1;
        static final int TRANSACTION_exitSCBMInd = 5;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_meSmsStorageFullInd = 2;
        static final int TRANSACTION_smsInfoExtInd = 3;
        static final int TRANSACTION_smsReadyInd = 4;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExMessagingIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExMessagingIndication)) {
                return (IMtkRadioExMessagingIndication) iin;
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
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            esnMeidChangeInd(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            meSmsStorageFullInd(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            smsInfoExtInd(_arg03, _arg12);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            smsReadyInd(_arg04);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            exitSCBMInd(_arg05);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            enterSCBMInd(_arg06);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExMessagingIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
            public void esnMeidChangeInd(int type, String esnMeid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(esnMeid);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method esnMeidChangeInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
            public void meSmsStorageFullInd(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method meSmsStorageFullInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
            public void smsInfoExtInd(int type, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method smsInfoExtInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
            public void smsReadyInd(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method smsReadyInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
            public void exitSCBMInd(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method exitSCBMInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
            public void enterSCBMInd(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enterSCBMInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
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
