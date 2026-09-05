package vendor.mediatek.hardware.mtkradioex.voice;

import android.hardware.radio.network.SuppSvcNotification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExVoiceIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$voice$IMtkRadioExVoiceIndication".replace('$', '.');
    public static final String HASH = "6fb707faf11116647ab6b8daa3ee47c2662abaa2";
    public static final int VERSION = 1;

    void callAdditionalInfoInd(int i, int i2, String[] strArr) throws RemoteException;

    void cdmaCallAccepted(int i) throws RemoteException;

    void cfuStatusNotify(int i, CfuStatusNotification cfuStatusNotification) throws RemoteException;

    void cipherIndication(int i, CipherNotification cipherNotification) throws RemoteException;

    void confSRVCC(int i, int[] iArr) throws RemoteException;

    void crssIndication(int i, CrssNotification crssNotification) throws RemoteException;

    void eccNumIndication(int i, String str, String str2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void incomingCallIndication(int i, IncomingCallNotification incomingCallNotification) throws RemoteException;

    void suppSvcNotifyEx(int i, SuppSvcNotification suppSvcNotification) throws RemoteException;

    public static class Default implements IMtkRadioExVoiceIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void callAdditionalInfoInd(int type, int ciType, String[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void cdmaCallAccepted(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void cfuStatusNotify(int type, CfuStatusNotification cfuStatus) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void cipherIndication(int type, CipherNotification cipherNotify) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void confSRVCC(int type, int[] callIds) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void crssIndication(int type, CrssNotification crssNotify) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void eccNumIndication(int type, String ecc_list_with_card, String ecc_list_no_card) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void incomingCallIndication(int type, IncomingCallNotification inCallNotify) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public void suppSvcNotifyEx(int type, SuppSvcNotification suppSvc) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExVoiceIndication {
        static final int TRANSACTION_callAdditionalInfoInd = 1;
        static final int TRANSACTION_cdmaCallAccepted = 2;
        static final int TRANSACTION_cfuStatusNotify = 3;
        static final int TRANSACTION_cipherIndication = 4;
        static final int TRANSACTION_confSRVCC = 5;
        static final int TRANSACTION_crssIndication = 6;
        static final int TRANSACTION_eccNumIndication = 7;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_incomingCallIndication = 8;
        static final int TRANSACTION_suppSvcNotifyEx = 9;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExVoiceIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExVoiceIndication)) {
                return (IMtkRadioExVoiceIndication) iin;
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
                            String[] _arg2 = data.createStringArray();
                            data.enforceNoDataAvail();
                            callAdditionalInfoInd(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            cdmaCallAccepted(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            CfuStatusNotification _arg12 = (CfuStatusNotification) data.readTypedObject(CfuStatusNotification.CREATOR);
                            data.enforceNoDataAvail();
                            cfuStatusNotify(_arg03, _arg12);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            CipherNotification _arg13 = (CipherNotification) data.readTypedObject(CipherNotification.CREATOR);
                            data.enforceNoDataAvail();
                            cipherIndication(_arg04, _arg13);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int[] _arg14 = data.createIntArray();
                            data.enforceNoDataAvail();
                            confSRVCC(_arg05, _arg14);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            CrssNotification _arg15 = (CrssNotification) data.readTypedObject(CrssNotification.CREATOR);
                            data.enforceNoDataAvail();
                            crssIndication(_arg06, _arg15);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            String _arg16 = data.readString();
                            String _arg22 = data.readString();
                            data.enforceNoDataAvail();
                            eccNumIndication(_arg07, _arg16, _arg22);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            IncomingCallNotification _arg17 = (IncomingCallNotification) data.readTypedObject(IncomingCallNotification.CREATOR);
                            data.enforceNoDataAvail();
                            incomingCallIndication(_arg08, _arg17);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            SuppSvcNotification _arg18 = (SuppSvcNotification) data.readTypedObject(SuppSvcNotification.CREATOR);
                            data.enforceNoDataAvail();
                            suppSvcNotifyEx(_arg09, _arg18);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExVoiceIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void callAdditionalInfoInd(int type, int ciType, String[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(ciType);
                    _data.writeStringArray(info);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method callAdditionalInfoInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void cdmaCallAccepted(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cdmaCallAccepted is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void cfuStatusNotify(int type, CfuStatusNotification cfuStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(cfuStatus, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cfuStatusNotify is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void cipherIndication(int type, CipherNotification cipherNotify) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(cipherNotify, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cipherIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void confSRVCC(int type, int[] callIds) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(callIds);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method confSRVCC is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void crssIndication(int type, CrssNotification crssNotify) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(crssNotify, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method crssIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void eccNumIndication(int type, String ecc_list_with_card, String ecc_list_no_card) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(ecc_list_with_card);
                    _data.writeString(ecc_list_no_card);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method eccNumIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void incomingCallIndication(int type, IncomingCallNotification inCallNotify) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(inCallNotify, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method incomingCallIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
            public void suppSvcNotifyEx(int type, SuppSvcNotification suppSvc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(suppSvc, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method suppSvcNotifyEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
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
