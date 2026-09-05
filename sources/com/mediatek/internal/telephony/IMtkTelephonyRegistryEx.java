package com.mediatek.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkTelephonyRegistryEx extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.internal.telephony.IMtkTelephonyRegistryEx";

    public static class Default implements IMtkTelephonyRegistryEx {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkTelephonyRegistryEx {
        public Stub() {
            attachInterface(this, IMtkTelephonyRegistryEx.DESCRIPTOR);
        }

        public static IMtkTelephonyRegistryEx asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMtkTelephonyRegistryEx.DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkTelephonyRegistryEx)) {
                return (IMtkTelephonyRegistryEx) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1598968902:
                    reply.writeString(IMtkTelephonyRegistryEx.DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IMtkTelephonyRegistryEx {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMtkTelephonyRegistryEx.DESCRIPTOR;
            }
        }
    }
}
