package vendor.mediatek.hardware.mtkradioex.voice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication;
import vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExVoice extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$voice$IMtkRadioExVoice".replace('$', '.');
    public static final String HASH = "6fb707faf11116647ab6b8daa3ee47c2662abaa2";
    public static final int VERSION = 1;

    void getCallSubAddress(int i, int i2) throws RemoteException;

    void getColp(int i, int i2) throws RemoteException;

    void getColr(int i, int i2) throws RemoteException;

    void getEccNum(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void hangupAll(int i, int i2) throws RemoteException;

    void hangupWithReason(int i, int i2, int i3, int i4) throws RemoteException;

    void queryCallForwardInTimeSlotStatus(int i, CallForwardInfoEx callForwardInfoEx, int i2) throws RemoteException;

    void resetSuppServ(int i, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void sendCnap(int i, String str, int i2) throws RemoteException;

    void setBarringPasswordCheckedByNW(int i, String str, String str2, String str3, String str4, int i2) throws RemoteException;

    void setCallForwardInTimeSlot(int i, CallForwardInfoEx callForwardInfoEx, int i2) throws RemoteException;

    void setCallIndication(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void setCallSubAddress(int i, boolean z, int i2) throws RemoteException;

    void setCallValidTimer(int i, int i2, int i3) throws RemoteException;

    void setClip(int i, int i2, int i3) throws RemoteException;

    void setColp(int i, int i2, int i3) throws RemoteException;

    void setColr(int i, int i2, int i3) throws RemoteException;

    void setEccMode(int i, String str, int i2, int i3, int i4, int i5) throws RemoteException;

    void setEccNum(int i, String str, String str2, int i2) throws RemoteException;

    void setGwsdMode(int i, String[] strArr, int i2) throws RemoteException;

    void setIgnoreSameNumberInterval(int i, int i2, int i3) throws RemoteException;

    void setKeepAliveByIpData(int i, String str, int i2) throws RemoteException;

    void setKeepAliveByPDCPCtrlPDU(int i, String str, int i2) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExVoiceResponse iMtkRadioExVoiceResponse, IMtkRadioExVoiceIndication iMtkRadioExVoiceIndication) throws RemoteException;

    void setResponseFunctionsMtkIms(IMtkRadioExVoiceResponse iMtkRadioExVoiceResponse, IMtkRadioExVoiceIndication iMtkRadioExVoiceIndication) throws RemoteException;

    void setSuppServProperty(int i, String str, String str2, int i2) throws RemoteException;

    public static class Default implements IMtkRadioExVoice {
        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void hangupAll(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void hangupWithReason(int serial, int callId, int reason, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void getCallSubAddress(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void getColp(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void getColr(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void getEccNum(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void queryCallForwardInTimeSlotStatus(int serial, CallForwardInfoEx callInfoEx, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void resetSuppServ(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void sendCnap(int serial, String cnapssMessage, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setBarringPasswordCheckedByNW(int serial, String facility, String oldPassword, String newPassword, String cfmPassword, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setCallForwardInTimeSlot(int serial, CallForwardInfoEx callInfoEx, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setCallIndication(int serial, int mode, int callId, int seqNumber, int cause, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setCallSubAddress(int serial, boolean enable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setCallValidTimer(int serial, int timer, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setClip(int serial, int clipEnable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setColp(int serial, int colpEnable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setColr(int serial, int colrEnable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setEccMode(int serial, String number, int enable, int airplaneMode, int imsReg, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setEccNum(int serial, String ecc_list_with_card, String ecc_list_no_card, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setGwsdMode(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setIgnoreSameNumberInterval(int serial, int interval, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setKeepAliveByIpData(int serial, String config, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setKeepAliveByPDCPCtrlPDU(int serial, String config, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setSuppServProperty(int serial, String name, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setResponseFunctionsMtk(IMtkRadioExVoiceResponse radioResponse, IMtkRadioExVoiceIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public void setResponseFunctionsMtkIms(IMtkRadioExVoiceResponse radioResponse, IMtkRadioExVoiceIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExVoice {
        static final int TRANSACTION_getCallSubAddress = 3;
        static final int TRANSACTION_getColp = 4;
        static final int TRANSACTION_getColr = 5;
        static final int TRANSACTION_getEccNum = 6;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_hangupAll = 1;
        static final int TRANSACTION_hangupWithReason = 2;
        static final int TRANSACTION_queryCallForwardInTimeSlotStatus = 7;
        static final int TRANSACTION_resetSuppServ = 8;
        static final int TRANSACTION_responseAcknowledgementMtk = 25;
        static final int TRANSACTION_sendCnap = 9;
        static final int TRANSACTION_setBarringPasswordCheckedByNW = 10;
        static final int TRANSACTION_setCallForwardInTimeSlot = 11;
        static final int TRANSACTION_setCallIndication = 12;
        static final int TRANSACTION_setCallSubAddress = 13;
        static final int TRANSACTION_setCallValidTimer = 14;
        static final int TRANSACTION_setClip = 15;
        static final int TRANSACTION_setColp = 16;
        static final int TRANSACTION_setColr = 17;
        static final int TRANSACTION_setEccMode = 18;
        static final int TRANSACTION_setEccNum = 19;
        static final int TRANSACTION_setGwsdMode = 20;
        static final int TRANSACTION_setIgnoreSameNumberInterval = 21;
        static final int TRANSACTION_setKeepAliveByIpData = 22;
        static final int TRANSACTION_setKeepAliveByPDCPCtrlPDU = 23;
        static final int TRANSACTION_setResponseFunctionsMtk = 26;
        static final int TRANSACTION_setResponseFunctionsMtkIms = 27;
        static final int TRANSACTION_setSuppServProperty = 24;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExVoice asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExVoice)) {
                return (IMtkRadioExVoice) iin;
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
                            hangupAll(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            hangupWithReason(_arg02, _arg12, _arg2, _arg3);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            getCallSubAddress(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            getColp(_arg04, _arg14);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            getColr(_arg05, _arg15);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            getEccNum(_arg06, _arg16);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            CallForwardInfoEx _arg17 = (CallForwardInfoEx) data.readTypedObject(CallForwardInfoEx.CREATOR);
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            queryCallForwardInTimeSlotStatus(_arg07, _arg17, _arg22);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            resetSuppServ(_arg08, _arg18);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            String _arg19 = data.readString();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            sendCnap(_arg09, _arg19, _arg23);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            String _arg110 = data.readString();
                            String _arg24 = data.readString();
                            String _arg32 = data.readString();
                            String _arg4 = data.readString();
                            int _arg5 = data.readInt();
                            data.enforceNoDataAvail();
                            setBarringPasswordCheckedByNW(_arg010, _arg110, _arg24, _arg32, _arg4, _arg5);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            CallForwardInfoEx _arg111 = (CallForwardInfoEx) data.readTypedObject(CallForwardInfoEx.CREATOR);
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            setCallForwardInTimeSlot(_arg011, _arg111, _arg25);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg112 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg33 = data.readInt();
                            int _arg42 = data.readInt();
                            int _arg52 = data.readInt();
                            data.enforceNoDataAvail();
                            setCallIndication(_arg012, _arg112, _arg26, _arg33, _arg42, _arg52);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            boolean _arg113 = data.readBoolean();
                            int _arg27 = data.readInt();
                            data.enforceNoDataAvail();
                            setCallSubAddress(_arg013, _arg113, _arg27);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int _arg114 = data.readInt();
                            int _arg28 = data.readInt();
                            data.enforceNoDataAvail();
                            setCallValidTimer(_arg014, _arg114, _arg28);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            int _arg115 = data.readInt();
                            int _arg29 = data.readInt();
                            data.enforceNoDataAvail();
                            setClip(_arg015, _arg115, _arg29);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            int _arg116 = data.readInt();
                            int _arg210 = data.readInt();
                            data.enforceNoDataAvail();
                            setColp(_arg016, _arg116, _arg210);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            int _arg117 = data.readInt();
                            int _arg211 = data.readInt();
                            data.enforceNoDataAvail();
                            setColr(_arg017, _arg117, _arg211);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            String _arg118 = data.readString();
                            int _arg212 = data.readInt();
                            int _arg34 = data.readInt();
                            int _arg43 = data.readInt();
                            int _arg53 = data.readInt();
                            data.enforceNoDataAvail();
                            setEccMode(_arg018, _arg118, _arg212, _arg34, _arg43, _arg53);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            String _arg119 = data.readString();
                            String _arg213 = data.readString();
                            int _arg35 = data.readInt();
                            data.enforceNoDataAvail();
                            setEccNum(_arg019, _arg119, _arg213, _arg35);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            String[] _arg120 = data.createStringArray();
                            int _arg214 = data.readInt();
                            data.enforceNoDataAvail();
                            setGwsdMode(_arg020, _arg120, _arg214);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            int _arg121 = data.readInt();
                            int _arg215 = data.readInt();
                            data.enforceNoDataAvail();
                            setIgnoreSameNumberInterval(_arg021, _arg121, _arg215);
                            return true;
                        case TRANSACTION_setKeepAliveByIpData /* 22 */:
                            int _arg022 = data.readInt();
                            String _arg122 = data.readString();
                            int _arg216 = data.readInt();
                            data.enforceNoDataAvail();
                            setKeepAliveByIpData(_arg022, _arg122, _arg216);
                            return true;
                        case TRANSACTION_setKeepAliveByPDCPCtrlPDU /* 23 */:
                            int _arg023 = data.readInt();
                            String _arg123 = data.readString();
                            int _arg217 = data.readInt();
                            data.enforceNoDataAvail();
                            setKeepAliveByPDCPCtrlPDU(_arg023, _arg123, _arg217);
                            return true;
                        case TRANSACTION_setSuppServProperty /* 24 */:
                            int _arg024 = data.readInt();
                            String _arg124 = data.readString();
                            String _arg218 = data.readString();
                            int _arg36 = data.readInt();
                            data.enforceNoDataAvail();
                            setSuppServProperty(_arg024, _arg124, _arg218, _arg36);
                            return true;
                        case TRANSACTION_responseAcknowledgementMtk /* 25 */:
                            responseAcknowledgementMtk();
                            return true;
                        case TRANSACTION_setResponseFunctionsMtk /* 26 */:
                            IMtkRadioExVoiceResponse _arg025 = IMtkRadioExVoiceResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExVoiceIndication _arg125 = IMtkRadioExVoiceIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtk(_arg025, _arg125);
                            return true;
                        case TRANSACTION_setResponseFunctionsMtkIms /* 27 */:
                            IMtkRadioExVoiceResponse _arg026 = IMtkRadioExVoiceResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExVoiceIndication _arg126 = IMtkRadioExVoiceIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtkIms(_arg026, _arg126);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExVoice {
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void hangupAll(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method hangupAll is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void hangupWithReason(int serial, int callId, int reason, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(reason);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method hangupWithReason is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void getCallSubAddress(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCallSubAddress is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void getColp(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getColp is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void getColr(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getColr is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void getEccNum(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getEccNum is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void queryCallForwardInTimeSlotStatus(int serial, CallForwardInfoEx callInfoEx, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(callInfoEx, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryCallForwardInTimeSlotStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void resetSuppServ(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method resetSuppServ is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void sendCnap(int serial, String cnapssMessage, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(cnapssMessage);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendCnap is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setBarringPasswordCheckedByNW(int serial, String facility, String oldPassword, String newPassword, String cfmPassword, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(facility);
                    _data.writeString(oldPassword);
                    _data.writeString(newPassword);
                    _data.writeString(cfmPassword);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBarringPasswordCheckedByNW is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setCallForwardInTimeSlot(int serial, CallForwardInfoEx callInfoEx, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(callInfoEx, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCallForwardInTimeSlot is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setCallIndication(int serial, int mode, int callId, int seqNumber, int cause, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(callId);
                    _data.writeInt(seqNumber);
                    _data.writeInt(cause);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCallIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setCallSubAddress(int serial, boolean enable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCallSubAddress is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setCallValidTimer(int serial, int timer, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(timer);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCallValidTimer is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setClip(int serial, int clipEnable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clipEnable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setClip is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setColp(int serial, int colpEnable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(colpEnable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setColp is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setColr(int serial, int colrEnable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(colrEnable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setColr is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setEccMode(int serial, String number, int enable, int airplaneMode, int imsReg, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(number);
                    _data.writeInt(enable);
                    _data.writeInt(airplaneMode);
                    _data.writeInt(imsReg);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setEccMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setEccNum(int serial, String ecc_list_with_card, String ecc_list_no_card, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ecc_list_with_card);
                    _data.writeString(ecc_list_no_card);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setEccNum is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setGwsdMode(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setGwsdMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setIgnoreSameNumberInterval(int serial, int interval, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(interval);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setIgnoreSameNumberInterval is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setKeepAliveByIpData(int serial, String config, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(config);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setKeepAliveByIpData, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setKeepAliveByIpData is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setKeepAliveByPDCPCtrlPDU(int serial, String config, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(config);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setKeepAliveByPDCPCtrlPDU, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setKeepAliveByPDCPCtrlPDU is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setSuppServProperty(int serial, String name, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(name);
                    _data.writeString(value);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSuppServProperty, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSuppServProperty is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_responseAcknowledgementMtk, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method responseAcknowledgementMtk is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setResponseFunctionsMtk(IMtkRadioExVoiceResponse radioResponse, IMtkRadioExVoiceIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMtk, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsMtk is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
            public void setResponseFunctionsMtkIms(IMtkRadioExVoiceResponse radioResponse, IMtkRadioExVoiceIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMtkIms, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsMtkIms is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
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

            @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice
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
