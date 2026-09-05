package vendor.mediatek.hardware.mtkradioex.sim;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExSimIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$sim$IMtkRadioExSimIndication".replace('$', '.');
    public static final String HASH = "a07d8b715b307b4e57aa776d209f5655cced7f96";
    public static final int VERSION = 1;

    void bipProactiveCommand(int i, String str) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void iccidChanged(int i, String str) throws RemoteException;

    void onCardDetectedInd(int i) throws RemoteException;

    void onImeiLock(int i) throws RemoteException;

    void onImsiRefreshDone(int i) throws RemoteException;

    void onRsuEvent(int i, int i2, String str) throws RemoteException;

    void onRsuSimLockEvent(int i, int i2) throws RemoteException;

    void onSimHotSwapInd(int i, int i2, String str) throws RemoteException;

    void onSimPowerChangedInd(int i, int[] iArr) throws RemoteException;

    void onStkMenuReset(int i) throws RemoteException;

    void onVirtualSimStatusChanged(int i, int i2) throws RemoteException;

    void onVsimEventIndication(int i, VsimOperationEvent vsimOperationEvent) throws RemoteException;

    void phbReadyNotification(int i, int i2) throws RemoteException;

    void smlSlotLockInfoChangedInd(int i, int[] iArr) throws RemoteException;

    public static class Default implements IMtkRadioExSimIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void bipProactiveCommand(int type, String cmd) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void iccidChanged(int type, String iccid) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onCardDetectedInd(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onImeiLock(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onImsiRefreshDone(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onRsuEvent(int type, int eventId, String eventString) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onRsuSimLockEvent(int type, int eventId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onSimHotSwapInd(int type, int event, String info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onSimPowerChangedInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onStkMenuReset(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onVirtualSimStatusChanged(int type, int simInserted) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void onVsimEventIndication(int type, VsimOperationEvent event) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void phbReadyNotification(int type, int isPhbReady) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public void smlSlotLockInfoChangedInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExSimIndication {
        static final int TRANSACTION_bipProactiveCommand = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_iccidChanged = 2;
        static final int TRANSACTION_onCardDetectedInd = 3;
        static final int TRANSACTION_onImeiLock = 4;
        static final int TRANSACTION_onImsiRefreshDone = 5;
        static final int TRANSACTION_onRsuEvent = 6;
        static final int TRANSACTION_onRsuSimLockEvent = 7;
        static final int TRANSACTION_onSimHotSwapInd = 8;
        static final int TRANSACTION_onSimPowerChangedInd = 9;
        static final int TRANSACTION_onStkMenuReset = 10;
        static final int TRANSACTION_onVirtualSimStatusChanged = 11;
        static final int TRANSACTION_onVsimEventIndication = 12;
        static final int TRANSACTION_phbReadyNotification = 13;
        static final int TRANSACTION_smlSlotLockInfoChangedInd = 14;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExSimIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExSimIndication)) {
                return (IMtkRadioExSimIndication) iin;
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
                            bipProactiveCommand(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            iccidChanged(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            onCardDetectedInd(_arg03);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            onImeiLock(_arg04);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            onImsiRefreshDone(_arg05);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            onRsuEvent(_arg06, _arg13, _arg2);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            onRsuSimLockEvent(_arg07, _arg14);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg15 = data.readInt();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            onSimHotSwapInd(_arg08, _arg15, _arg22);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            int[] _arg16 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onSimPowerChangedInd(_arg09, _arg16);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            onStkMenuReset(_arg010);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            onVirtualSimStatusChanged(_arg011, _arg17);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            VsimOperationEvent _arg18 = (VsimOperationEvent) data.readTypedObject(VsimOperationEvent.CREATOR);
                            data.enforceNoDataAvail();
                            onVsimEventIndication(_arg012, _arg18);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            phbReadyNotification(_arg013, _arg19);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int[] _arg110 = data.createIntArray();
                            data.enforceNoDataAvail();
                            smlSlotLockInfoChangedInd(_arg014, _arg110);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExSimIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void bipProactiveCommand(int type, String cmd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(cmd);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method bipProactiveCommand is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void iccidChanged(int type, String iccid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(iccid);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method iccidChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onCardDetectedInd(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onCardDetectedInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onImeiLock(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onImeiLock is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onImsiRefreshDone(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onImsiRefreshDone is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onRsuEvent(int type, int eventId, String eventString) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(eventId);
                    _data.writeString(eventString);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onRsuEvent is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onRsuSimLockEvent(int type, int eventId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(eventId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onRsuSimLockEvent is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onSimHotSwapInd(int type, int event, String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(event);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onSimHotSwapInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onSimPowerChangedInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onSimPowerChangedInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onStkMenuReset(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onStkMenuReset is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onVirtualSimStatusChanged(int type, int simInserted) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(simInserted);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onVirtualSimStatusChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void onVsimEventIndication(int type, VsimOperationEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(event, 0);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onVsimEventIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void phbReadyNotification(int type, int isPhbReady) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(isPhbReady);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method phbReadyNotification is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
            public void smlSlotLockInfoChangedInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method smlSlotLockInfoChangedInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
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
