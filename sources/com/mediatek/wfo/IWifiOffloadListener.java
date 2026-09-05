package com.mediatek.wfo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IWifiOffloadListener extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.wfo.IWifiOffloadListener";

    void onAllowWifiOff() throws RemoteException;

    void onHandover(int i, int i2, int i3) throws RemoteException;

    void onRequestImsSwitch(int i, boolean z) throws RemoteException;

    void onRoveOut(int i, boolean z, int i2) throws RemoteException;

    void onWfcStateChanged(int i, int i2) throws RemoteException;

    void onWifiPdnOOSStateChanged(int i, int i2) throws RemoteException;

    public static class Default implements IWifiOffloadListener {
        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onHandover(int simIdx, int stage, int ratType) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onRoveOut(int simIdx, boolean roveOut, int rssi) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onRequestImsSwitch(int simIdx, boolean isImsOn) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onWifiPdnOOSStateChanged(int simIdx, int oosState) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onAllowWifiOff() throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadListener
        public void onWfcStateChanged(int simIdx, int state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiOffloadListener {
        static final int TRANSACTION_onAllowWifiOff = 5;
        static final int TRANSACTION_onHandover = 1;
        static final int TRANSACTION_onRequestImsSwitch = 3;
        static final int TRANSACTION_onRoveOut = 2;
        static final int TRANSACTION_onWfcStateChanged = 6;
        static final int TRANSACTION_onWifiPdnOOSStateChanged = 4;

        public Stub() {
            attachInterface(this, IWifiOffloadListener.DESCRIPTOR);
        }

        public static IWifiOffloadListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IWifiOffloadListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiOffloadListener)) {
                return (IWifiOffloadListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IWifiOffloadListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IWifiOffloadListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            onHandover(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            onRoveOut(_arg02, _arg12, _arg22);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            boolean _arg13 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onRequestImsSwitch(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            onWifiPdnOOSStateChanged(_arg04, _arg14);
                            return true;
                        case 5:
                            onAllowWifiOff();
                            return true;
                        case 6:
                            int _arg05 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            onWfcStateChanged(_arg05, _arg15);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IWifiOffloadListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IWifiOffloadListener.DESCRIPTOR;
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onHandover(int simIdx, int stage, int ratType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(stage);
                    _data.writeInt(ratType);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onRoveOut(int simIdx, boolean roveOut, int rssi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeBoolean(roveOut);
                    _data.writeInt(rssi);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onRequestImsSwitch(int simIdx, boolean isImsOn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeBoolean(isImsOn);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onWifiPdnOOSStateChanged(int simIdx, int oosState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(oosState);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onAllowWifiOff() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadListener
            public void onWfcStateChanged(int simIdx, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IWifiOffloadListener.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(state);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
