package vendor.mediatek.hardware.mtkradioex.data;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExDataIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$data$IMtkRadioExDataIndication".replace('$', '.');
    public static final String HASH = "943612175caf84e3d94934844810892a1112b91e";
    public static final int VERSION = 2;

    void dedicatedBearerActivationInd(int i, DedicateDataCall dedicateDataCall) throws RemoteException;

    void dedicatedBearerDeactivationInd(int i, int i2) throws RemoteException;

    void dedicatedBearerModificationInd(int i, DedicateDataCall dedicateDataCall) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void mobileDataUsageInd(int i, int[] iArr) throws RemoteException;

    void networkRejectCauseInd(int i, int[] iArr) throws RemoteException;

    void onDsdaChangedInd(int i, int i2) throws RemoteException;

    void onMdDataRetryCountReset(int i) throws RemoteException;

    void onNwLimitInd(int i, int[] iArr) throws RemoteException;

    void onPlmnDataInd(int i, PlmnMvnoInfo plmnMvnoInfo) throws RemoteException;

    void pcoDataAfterAttached(int i, PcoDataAttachedInfo pcoDataAttachedInfo) throws RemoteException;

    void postUrspRule(int i, int i2, String str, UrspRule[] urspRuleArr) throws RemoteException;

    void qualifiedNetworkTypesChangedInd(int i, int[] iArr) throws RemoteException;

    public static class Default implements IMtkRadioExDataIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void dedicatedBearerActivationInd(int type, DedicateDataCall ddcData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void dedicatedBearerDeactivationInd(int type, int cid) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void dedicatedBearerModificationInd(int type, DedicateDataCall ddcData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void mobileDataUsageInd(int type, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void onDsdaChangedInd(int type, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void onMdDataRetryCountReset(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void onPlmnDataInd(int type, PlmnMvnoInfo plmnMvnoInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void pcoDataAfterAttached(int type, PcoDataAttachedInfo pcoData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void networkRejectCauseInd(int type, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void onNwLimitInd(int type, int[] state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void qualifiedNetworkTypesChangedInd(int type, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public void postUrspRule(int slotIndex, int type, String originalUrsp, UrspRule[] urspRules) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExDataIndication {
        static final int TRANSACTION_dedicatedBearerActivationInd = 1;
        static final int TRANSACTION_dedicatedBearerDeactivationInd = 2;
        static final int TRANSACTION_dedicatedBearerModificationInd = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_mobileDataUsageInd = 4;
        static final int TRANSACTION_networkRejectCauseInd = 9;
        static final int TRANSACTION_onDsdaChangedInd = 5;
        static final int TRANSACTION_onMdDataRetryCountReset = 6;
        static final int TRANSACTION_onNwLimitInd = 10;
        static final int TRANSACTION_onPlmnDataInd = 7;
        static final int TRANSACTION_pcoDataAfterAttached = 8;
        static final int TRANSACTION_postUrspRule = 12;
        static final int TRANSACTION_qualifiedNetworkTypesChangedInd = 11;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExDataIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExDataIndication)) {
                return (IMtkRadioExDataIndication) iin;
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
                            DedicateDataCall _arg1 = (DedicateDataCall) data.readTypedObject(DedicateDataCall.CREATOR);
                            data.enforceNoDataAvail();
                            dedicatedBearerActivationInd(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            dedicatedBearerDeactivationInd(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            DedicateDataCall _arg13 = (DedicateDataCall) data.readTypedObject(DedicateDataCall.CREATOR);
                            data.enforceNoDataAvail();
                            dedicatedBearerModificationInd(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int[] _arg14 = data.createIntArray();
                            data.enforceNoDataAvail();
                            mobileDataUsageInd(_arg04, _arg14);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            onDsdaChangedInd(_arg05, _arg15);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            onMdDataRetryCountReset(_arg06);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            PlmnMvnoInfo _arg16 = (PlmnMvnoInfo) data.readTypedObject(PlmnMvnoInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onPlmnDataInd(_arg07, _arg16);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            PcoDataAttachedInfo _arg17 = (PcoDataAttachedInfo) data.readTypedObject(PcoDataAttachedInfo.CREATOR);
                            data.enforceNoDataAvail();
                            pcoDataAfterAttached(_arg08, _arg17);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            int[] _arg18 = data.createIntArray();
                            data.enforceNoDataAvail();
                            networkRejectCauseInd(_arg09, _arg18);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            int[] _arg19 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onNwLimitInd(_arg010, _arg19);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int[] _arg110 = data.createIntArray();
                            data.enforceNoDataAvail();
                            qualifiedNetworkTypesChangedInd(_arg011, _arg110);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg111 = data.readInt();
                            String _arg2 = data.readString();
                            UrspRule[] _arg3 = (UrspRule[]) data.createTypedArray(UrspRule.CREATOR);
                            data.enforceNoDataAvail();
                            postUrspRule(_arg012, _arg111, _arg2, _arg3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExDataIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void dedicatedBearerActivationInd(int type, DedicateDataCall ddcData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(ddcData, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dedicatedBearerActivationInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void dedicatedBearerDeactivationInd(int type, int cid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(cid);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dedicatedBearerDeactivationInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void dedicatedBearerModificationInd(int type, DedicateDataCall ddcData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(ddcData, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dedicatedBearerModificationInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void mobileDataUsageInd(int type, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method mobileDataUsageInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void onDsdaChangedInd(int type, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onDsdaChangedInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void onMdDataRetryCountReset(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onMdDataRetryCountReset is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void onPlmnDataInd(int type, PlmnMvnoInfo plmnMvnoInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(plmnMvnoInfo, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onPlmnDataInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void pcoDataAfterAttached(int type, PcoDataAttachedInfo pcoData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(pcoData, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method pcoDataAfterAttached is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void networkRejectCauseInd(int type, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method networkRejectCauseInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void onNwLimitInd(int type, int[] state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(state);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onNwLimitInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void qualifiedNetworkTypesChangedInd(int type, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method qualifiedNetworkTypesChangedInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
            public void postUrspRule(int slotIndex, int type, String originalUrsp, UrspRule[] urspRules) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slotIndex);
                    _data.writeInt(type);
                    _data.writeString(originalUrsp);
                    _data.writeTypedArray(urspRules, 0);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method postUrspRule is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
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
