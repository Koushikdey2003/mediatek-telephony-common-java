package com.mediatek.wfo;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.mediatek.wfo.IWifiOffloadListener;

/* JADX INFO: loaded from: classes.dex */
public interface IWifiOffloadService extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.wfo.IWifiOffloadService";

    void factoryReset() throws RemoteException;

    DisconnectCause getDisconnectCause(int i) throws RemoteException;

    String[] getMccMncAllowList(int i) throws RemoteException;

    int getRatType(int i) throws RemoteException;

    boolean isWifiConnected() throws RemoteException;

    void registerForHandoverEvent(IWifiOffloadListener iWifiOffloadListener) throws RemoteException;

    void setEpdgFqdn(int i, String str, boolean z) throws RemoteException;

    boolean setMccMncAllowList(String[] strArr) throws RemoteException;

    boolean setWifiOff() throws RemoteException;

    void unregisterForHandoverEvent(IWifiOffloadListener iWifiOffloadListener) throws RemoteException;

    void updateCallState(int i, int i2, int i3, int i4) throws RemoteException;

    void updateRadioState(int i, int i2) throws RemoteException;

    public static class Default implements IWifiOffloadService {
        @Override // com.mediatek.wfo.IWifiOffloadService
        public void registerForHandoverEvent(IWifiOffloadListener listener) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public void unregisterForHandoverEvent(IWifiOffloadListener listener) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public int getRatType(int simIdx) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public DisconnectCause getDisconnectCause(int simIdx) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public void setEpdgFqdn(int simIdx, String fqdn, boolean wfcEnabled) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public void updateCallState(int simIdx, int callId, int callType, int callState) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public boolean isWifiConnected() throws RemoteException {
            return false;
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public void updateRadioState(int simIdx, int radioState) throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public boolean setMccMncAllowList(String[] allowList) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public String[] getMccMncAllowList(int mode) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public void factoryReset() throws RemoteException {
        }

        @Override // com.mediatek.wfo.IWifiOffloadService
        public boolean setWifiOff() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWifiOffloadService {
        static final int TRANSACTION_factoryReset = 11;
        static final int TRANSACTION_getDisconnectCause = 4;
        static final int TRANSACTION_getMccMncAllowList = 10;
        static final int TRANSACTION_getRatType = 3;
        static final int TRANSACTION_isWifiConnected = 7;
        static final int TRANSACTION_registerForHandoverEvent = 1;
        static final int TRANSACTION_setEpdgFqdn = 5;
        static final int TRANSACTION_setMccMncAllowList = 9;
        static final int TRANSACTION_setWifiOff = 12;
        static final int TRANSACTION_unregisterForHandoverEvent = 2;
        static final int TRANSACTION_updateCallState = 6;
        static final int TRANSACTION_updateRadioState = 8;

        public Stub() {
            attachInterface(this, IWifiOffloadService.DESCRIPTOR);
        }

        public static IWifiOffloadService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IWifiOffloadService.DESCRIPTOR);
            if (iin != null && (iin instanceof IWifiOffloadService)) {
                return (IWifiOffloadService) iin;
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
                data.enforceInterface(IWifiOffloadService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IWifiOffloadService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IWifiOffloadListener _arg0 = IWifiOffloadListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerForHandoverEvent(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            IWifiOffloadListener _arg02 = IWifiOffloadListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterForHandoverEvent(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = getRatType(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            DisconnectCause _result2 = getDisconnectCause(_arg04);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            String _arg1 = data.readString();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setEpdgFqdn(_arg05, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg22 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            updateCallState(_arg06, _arg12, _arg22, _arg3);
                            reply.writeNoException();
                            return true;
                        case 7:
                            boolean _result3 = isWifiConnected();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 8:
                            int _arg07 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            updateRadioState(_arg07, _arg13);
                            reply.writeNoException();
                            return true;
                        case 9:
                            String[] _arg08 = data.createStringArray();
                            data.enforceNoDataAvail();
                            boolean _result4 = setMccMncAllowList(_arg08);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 10:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            String[] _result5 = getMccMncAllowList(_arg09);
                            reply.writeNoException();
                            reply.writeStringArray(_result5);
                            return true;
                        case 11:
                            factoryReset();
                            reply.writeNoException();
                            return true;
                        case 12:
                            boolean _result6 = setWifiOff();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IWifiOffloadService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IWifiOffloadService.DESCRIPTOR;
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public void registerForHandoverEvent(IWifiOffloadListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public void unregisterForHandoverEvent(IWifiOffloadListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public int getRatType(int simIdx) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public DisconnectCause getDisconnectCause(int simIdx) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    DisconnectCause _result = (DisconnectCause) _reply.readTypedObject(DisconnectCause.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public void setEpdgFqdn(int simIdx, String fqdn, boolean wfcEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeString(fqdn);
                    _data.writeBoolean(wfcEnabled);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public void updateCallState(int simIdx, int callId, int callType, int callState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(callId);
                    _data.writeInt(callType);
                    _data.writeInt(callState);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public boolean isWifiConnected() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public void updateRadioState(int simIdx, int radioState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeInt(simIdx);
                    _data.writeInt(radioState);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public boolean setMccMncAllowList(String[] allowList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeStringArray(allowList);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public String[] getMccMncAllowList(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public void factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.wfo.IWifiOffloadService
            public boolean setWifiOff() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IWifiOffloadService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
