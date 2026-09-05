package vendor.mediatek.hardware.mtkradioex.modem;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.atci.IAtciIndication;
import vendor.mediatek.hardware.mtkradioex.atci.IAtciResponse;
import vendor.mediatek.hardware.mtkradioex.cap.IMtkRadioExCapRadioResponse;
import vendor.mediatek.hardware.mtkradioex.em.IEmRadioIndication;
import vendor.mediatek.hardware.mtkradioex.em.IEmRadioResponse;
import vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication;
import vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExModem extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$modem$IMtkRadioExModem".replace('$', '.');
    public static final String HASH = "87512b9a1978fdb596a8d9176854d3761382dc82";
    public static final int VERSION = 2;

    void abortCertificate(int i, int i2, int i3) throws RemoteException;

    void enableCapability(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void getEngineeringModeInfo(int i, int i2, int i3) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void modifyModemType(int i, int i2, int i3, int i4) throws RemoteException;

    void registerCellQltyReport(int i, String str, String str2, String str3, String str4, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void restartRILD(int i, int i2) throws RemoteException;

    void routeAuthMessage(int i, int i2, byte[] bArr, int i3) throws RemoteException;

    void routeCertificate(int i, int i2, byte[] bArr, byte[] bArr2, int i3) throws RemoteException;

    void runGbaAuthentication(int i, String str, String str2, boolean z, int i2, int i3) throws RemoteException;

    void sendAtciRequest(int i, byte[] bArr, int i2) throws RemoteException;

    void sendEmbmsAtCommand(int i, String str, int i2) throws RemoteException;

    void sendRequestRaw(int i, byte[] bArr, int i2) throws RemoteException;

    void sendRequestStrings(int i, String[] strArr, int i2) throws RemoteException;

    void sendSarIndicator(int i, int i2, String str, int i3) throws RemoteException;

    void sendWifiAssociated(int i, String str, int i2, String str2, String str3, int i3, String str4, int i4) throws RemoteException;

    void sendWifiEnabled(int i, String str, int i2, int i3) throws RemoteException;

    void sendWifiIpAddress(int i, String str, String str2, String str3, int i2, int i3, String str4, String str5, int i4, String str6, int i5) throws RemoteException;

    void setMaxUlSpeed(int i, int i2, int i3) throws RemoteException;

    void setModemPower(int i, boolean z, int i2) throws RemoteException;

    void setResponseFunctionsCap(IMtkRadioExCapRadioResponse iMtkRadioExCapRadioResponse) throws RemoteException;

    void setResponseFunctionsEm(IEmRadioResponse iEmRadioResponse, IEmRadioIndication iEmRadioIndication) throws RemoteException;

    void setResponseFunctionsForAtci(IAtciResponse iAtciResponse, IAtciIndication iAtciIndication) throws RemoteException;

    void setResponseFunctionsGba(IMtkRadioExModemResponse iMtkRadioExModemResponse) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExModemResponse iMtkRadioExModemResponse, IMtkRadioExModemIndication iMtkRadioExModemIndication) throws RemoteException;

    void setResponseFunctionsMtkIms(IMtkRadioExModemResponse iMtkRadioExModemResponse, IMtkRadioExModemIndication iMtkRadioExModemIndication) throws RemoteException;

    void setTrm(int i, int i2, int i3) throws RemoteException;

    void setTxPower(int i, int i2, int i3) throws RemoteException;

    void setTxPowerStatus(int i, int i2, int i3) throws RemoteException;

    void setVendorSetting(int i, int i2, String str, int i3) throws RemoteException;

    void triggerModeSwitchByEcc(int i, int i2, int i3) throws RemoteException;

    public static class Default implements IMtkRadioExModem {
        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void abortCertificate(int serial, int uid, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void enableCapability(int serial, String id, int uid, int toActive, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void getEngineeringModeInfo(int serial, int index, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void modifyModemType(int serial, int applyType, int modemType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void restartRILD(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void routeAuthMessage(int serial, int uid, byte[] msg, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void routeCertificate(int serial, int uid, byte[] cert, byte[] msg, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendAtciRequest(int serial, byte[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendEmbmsAtCommand(int serial, String data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendRequestRaw(int serial, byte[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendRequestStrings(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendSarIndicator(int serial, int sar_cmd_type, String sar_parameter, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setMaxUlSpeed(int serial, int ulSpeed, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setModemPower(int serial, boolean isOn, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setTrm(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setTxPower(int serial, int limitpower, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setTxPowerStatus(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setVendorSetting(int serial, int setting, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void triggerModeSwitchByEcc(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void runGbaAuthentication(int serial, String nafFqdn, String nafSecureProtocolId, boolean forceRun, int netId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendWifiAssociated(int serial, String ifName, int associated, String ssid, String apMac, int mtuSize, String ueMac, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendWifiEnabled(int serial, String ifName, int isWifiEnabled, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void sendWifiIpAddress(int serial, String ifName, String ipv4Addr, String ipv6Addr, int ipv4PrefixLen, int ipv6PrefixLen, String ipv4Gateway, String ipv6Gateway, int dnsCount, String dnsServers, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void registerCellQltyReport(int serial, String registerQuality, String type, String thresholdValues, String triggerTime, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsMtk(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsMtkIms(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsCap(IMtkRadioExCapRadioResponse capRadioResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsForAtci(IAtciResponse atciResponseParam, IAtciIndication atciIndicationParam) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsEm(IEmRadioResponse radioResponse, IEmRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public void setResponseFunctionsGba(IMtkRadioExModemResponse radioResponse) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExModem {
        static final int TRANSACTION_abortCertificate = 1;
        static final int TRANSACTION_enableCapability = 2;
        static final int TRANSACTION_getEngineeringModeInfo = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_modifyModemType = 4;
        static final int TRANSACTION_registerCellQltyReport = 24;
        static final int TRANSACTION_responseAcknowledgementMtk = 25;
        static final int TRANSACTION_restartRILD = 5;
        static final int TRANSACTION_routeAuthMessage = 6;
        static final int TRANSACTION_routeCertificate = 7;
        static final int TRANSACTION_runGbaAuthentication = 20;
        static final int TRANSACTION_sendAtciRequest = 8;
        static final int TRANSACTION_sendEmbmsAtCommand = 9;
        static final int TRANSACTION_sendRequestRaw = 10;
        static final int TRANSACTION_sendRequestStrings = 11;
        static final int TRANSACTION_sendSarIndicator = 12;
        static final int TRANSACTION_sendWifiAssociated = 21;
        static final int TRANSACTION_sendWifiEnabled = 22;
        static final int TRANSACTION_sendWifiIpAddress = 23;
        static final int TRANSACTION_setMaxUlSpeed = 13;
        static final int TRANSACTION_setModemPower = 14;
        static final int TRANSACTION_setResponseFunctionsCap = 28;
        static final int TRANSACTION_setResponseFunctionsEm = 30;
        static final int TRANSACTION_setResponseFunctionsForAtci = 29;
        static final int TRANSACTION_setResponseFunctionsGba = 31;
        static final int TRANSACTION_setResponseFunctionsMtk = 26;
        static final int TRANSACTION_setResponseFunctionsMtkIms = 27;
        static final int TRANSACTION_setTrm = 15;
        static final int TRANSACTION_setTxPower = 16;
        static final int TRANSACTION_setTxPowerStatus = 17;
        static final int TRANSACTION_setVendorSetting = 18;
        static final int TRANSACTION_triggerModeSwitchByEcc = 19;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExModem asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExModem)) {
                return (IMtkRadioExModem) iin;
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
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            abortCertificate(_arg0, _arg1, _arg2);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            String _arg12 = data.readString();
                            int _arg22 = data.readInt();
                            int _arg3 = data.readInt();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            enableCapability(_arg02, _arg12, _arg22, _arg3, _arg4);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            getEngineeringModeInfo(_arg03, _arg13, _arg23);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            int _arg24 = data.readInt();
                            int _arg32 = data.readInt();
                            data.enforceNoDataAvail();
                            modifyModemType(_arg04, _arg14, _arg24, _arg32);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            restartRILD(_arg05, _arg15);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg16 = data.readInt();
                            byte[] _arg25 = data.createByteArray();
                            int _arg33 = data.readInt();
                            data.enforceNoDataAvail();
                            routeAuthMessage(_arg06, _arg16, _arg25, _arg33);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg17 = data.readInt();
                            byte[] _arg26 = data.createByteArray();
                            byte[] _arg34 = data.createByteArray();
                            int _arg42 = data.readInt();
                            data.enforceNoDataAvail();
                            routeCertificate(_arg07, _arg17, _arg26, _arg34, _arg42);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            byte[] _arg18 = data.createByteArray();
                            int _arg27 = data.readInt();
                            data.enforceNoDataAvail();
                            sendAtciRequest(_arg08, _arg18, _arg27);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            String _arg19 = data.readString();
                            int _arg28 = data.readInt();
                            data.enforceNoDataAvail();
                            sendEmbmsAtCommand(_arg09, _arg19, _arg28);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            byte[] _arg110 = data.createByteArray();
                            int _arg29 = data.readInt();
                            data.enforceNoDataAvail();
                            sendRequestRaw(_arg010, _arg110, _arg29);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            String[] _arg111 = data.createStringArray();
                            int _arg210 = data.readInt();
                            data.enforceNoDataAvail();
                            sendRequestStrings(_arg011, _arg111, _arg210);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg112 = data.readInt();
                            String _arg211 = data.readString();
                            int _arg35 = data.readInt();
                            data.enforceNoDataAvail();
                            sendSarIndicator(_arg012, _arg112, _arg211, _arg35);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            int _arg113 = data.readInt();
                            int _arg212 = data.readInt();
                            data.enforceNoDataAvail();
                            setMaxUlSpeed(_arg013, _arg113, _arg212);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            boolean _arg114 = data.readBoolean();
                            int _arg213 = data.readInt();
                            data.enforceNoDataAvail();
                            setModemPower(_arg014, _arg114, _arg213);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            int _arg115 = data.readInt();
                            int _arg214 = data.readInt();
                            data.enforceNoDataAvail();
                            setTrm(_arg015, _arg115, _arg214);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            int _arg116 = data.readInt();
                            int _arg215 = data.readInt();
                            data.enforceNoDataAvail();
                            setTxPower(_arg016, _arg116, _arg215);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            int _arg117 = data.readInt();
                            int _arg216 = data.readInt();
                            data.enforceNoDataAvail();
                            setTxPowerStatus(_arg017, _arg117, _arg216);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            int _arg118 = data.readInt();
                            String _arg217 = data.readString();
                            int _arg36 = data.readInt();
                            data.enforceNoDataAvail();
                            setVendorSetting(_arg018, _arg118, _arg217, _arg36);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            int _arg119 = data.readInt();
                            int _arg218 = data.readInt();
                            data.enforceNoDataAvail();
                            triggerModeSwitchByEcc(_arg019, _arg119, _arg218);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            String _arg120 = data.readString();
                            String _arg219 = data.readString();
                            boolean _arg37 = data.readBoolean();
                            int _arg43 = data.readInt();
                            int _arg5 = data.readInt();
                            data.enforceNoDataAvail();
                            runGbaAuthentication(_arg020, _arg120, _arg219, _arg37, _arg43, _arg5);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            String _arg121 = data.readString();
                            int _arg220 = data.readInt();
                            String _arg38 = data.readString();
                            String _arg44 = data.readString();
                            int _arg52 = data.readInt();
                            String _arg6 = data.readString();
                            int _arg7 = data.readInt();
                            data.enforceNoDataAvail();
                            sendWifiAssociated(_arg021, _arg121, _arg220, _arg38, _arg44, _arg52, _arg6, _arg7);
                            return true;
                        case TRANSACTION_sendWifiEnabled /* 22 */:
                            int _arg022 = data.readInt();
                            String _arg122 = data.readString();
                            int _arg221 = data.readInt();
                            int _arg39 = data.readInt();
                            data.enforceNoDataAvail();
                            sendWifiEnabled(_arg022, _arg122, _arg221, _arg39);
                            return true;
                        case TRANSACTION_sendWifiIpAddress /* 23 */:
                            int _arg023 = data.readInt();
                            String _arg123 = data.readString();
                            String _arg222 = data.readString();
                            String _arg310 = data.readString();
                            int _arg45 = data.readInt();
                            int _arg53 = data.readInt();
                            String _arg62 = data.readString();
                            String _arg72 = data.readString();
                            int _arg8 = data.readInt();
                            String _arg9 = data.readString();
                            int _arg10 = data.readInt();
                            data.enforceNoDataAvail();
                            sendWifiIpAddress(_arg023, _arg123, _arg222, _arg310, _arg45, _arg53, _arg62, _arg72, _arg8, _arg9, _arg10);
                            return true;
                        case TRANSACTION_registerCellQltyReport /* 24 */:
                            int _arg024 = data.readInt();
                            String _arg124 = data.readString();
                            String _arg223 = data.readString();
                            String _arg311 = data.readString();
                            String _arg46 = data.readString();
                            int _arg54 = data.readInt();
                            data.enforceNoDataAvail();
                            registerCellQltyReport(_arg024, _arg124, _arg223, _arg311, _arg46, _arg54);
                            return true;
                        case TRANSACTION_responseAcknowledgementMtk /* 25 */:
                            responseAcknowledgementMtk();
                            return true;
                        case TRANSACTION_setResponseFunctionsMtk /* 26 */:
                            IMtkRadioExModemResponse _arg025 = IMtkRadioExModemResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExModemIndication _arg125 = IMtkRadioExModemIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtk(_arg025, _arg125);
                            return true;
                        case TRANSACTION_setResponseFunctionsMtkIms /* 27 */:
                            IMtkRadioExModemResponse _arg026 = IMtkRadioExModemResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExModemIndication _arg126 = IMtkRadioExModemIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtkIms(_arg026, _arg126);
                            return true;
                        case TRANSACTION_setResponseFunctionsCap /* 28 */:
                            IMtkRadioExCapRadioResponse _arg027 = IMtkRadioExCapRadioResponse.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsCap(_arg027);
                            return true;
                        case TRANSACTION_setResponseFunctionsForAtci /* 29 */:
                            IAtciResponse _arg028 = IAtciResponse.Stub.asInterface(data.readStrongBinder());
                            IAtciIndication _arg127 = IAtciIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsForAtci(_arg028, _arg127);
                            return true;
                        case 30:
                            IEmRadioResponse _arg029 = IEmRadioResponse.Stub.asInterface(data.readStrongBinder());
                            IEmRadioIndication _arg128 = IEmRadioIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsEm(_arg029, _arg128);
                            return true;
                        case 31:
                            IMtkRadioExModemResponse _arg030 = IMtkRadioExModemResponse.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsGba(_arg030);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExModem {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void abortCertificate(int serial, int uid, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(uid);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method abortCertificate is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void enableCapability(int serial, String id, int uid, int toActive, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(id);
                    _data.writeInt(uid);
                    _data.writeInt(toActive);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableCapability is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void getEngineeringModeInfo(int serial, int index, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(index);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getEngineeringModeInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void modifyModemType(int serial, int applyType, int modemType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(applyType);
                    _data.writeInt(modemType);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method modifyModemType is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void restartRILD(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method restartRILD is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void routeAuthMessage(int serial, int uid, byte[] msg, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(uid);
                    _data.writeByteArray(msg);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method routeAuthMessage is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void routeCertificate(int serial, int uid, byte[] cert, byte[] msg, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(uid);
                    _data.writeByteArray(cert);
                    _data.writeByteArray(msg);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method routeCertificate is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendAtciRequest(int serial, byte[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeByteArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendAtciRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendEmbmsAtCommand(int serial, String data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendEmbmsAtCommand is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendRequestRaw(int serial, byte[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeByteArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRequestRaw is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendRequestStrings(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRequestStrings is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendSarIndicator(int serial, int sar_cmd_type, String sar_parameter, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(sar_cmd_type);
                    _data.writeString(sar_parameter);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendSarIndicator is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setMaxUlSpeed(int serial, int ulSpeed, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(ulSpeed);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setMaxUlSpeed is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setModemPower(int serial, boolean isOn, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(isOn);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setModemPower is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setTrm(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTrm is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setTxPower(int serial, int limitpower, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(limitpower);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTxPower is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setTxPowerStatus(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTxPowerStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setVendorSetting(int serial, int setting, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(setting);
                    _data.writeString(value);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setVendorSetting is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void triggerModeSwitchByEcc(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method triggerModeSwitchByEcc is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void runGbaAuthentication(int serial, String nafFqdn, String nafSecureProtocolId, boolean forceRun, int netId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(nafFqdn);
                    _data.writeString(nafSecureProtocolId);
                    _data.writeBoolean(forceRun);
                    _data.writeInt(netId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method runGbaAuthentication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendWifiAssociated(int serial, String ifName, int associated, String ssid, String apMac, int mtuSize, String ueMac, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeInt(associated);
                    _data.writeString(ssid);
                    _data.writeString(apMac);
                    _data.writeInt(mtuSize);
                    _data.writeString(ueMac);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendWifiAssociated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendWifiEnabled(int serial, String ifName, int isWifiEnabled, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeInt(isWifiEnabled);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendWifiEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendWifiEnabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void sendWifiIpAddress(int serial, String ifName, String ipv4Addr, String ipv6Addr, int ipv4PrefixLen, int ipv6PrefixLen, String ipv4Gateway, String ipv6Gateway, int dnsCount, String dnsServers, int clientId) throws Throwable {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    try {
                        _data.writeString(ifName);
                        try {
                            _data.writeString(ipv4Addr);
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeString(ipv6Addr);
                    try {
                        _data.writeInt(ipv4PrefixLen);
                        try {
                            _data.writeInt(ipv6PrefixLen);
                            try {
                                _data.writeString(ipv4Gateway);
                            } catch (Throwable th4) {
                                th = th4;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(ipv6Gateway);
                        try {
                            _data.writeInt(dnsCount);
                            try {
                                _data.writeString(dnsServers);
                                try {
                                    _data.writeInt(clientId);
                                } catch (Throwable th7) {
                                    th = th7;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th11) {
                    th = th11;
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendWifiIpAddress, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendWifiIpAddress is unimplemented.");
                    }
                    _data.recycle();
                } catch (Throwable th12) {
                    th = th12;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void registerCellQltyReport(int serial, String registerQuality, String type, String thresholdValues, String triggerTime, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(registerQuality);
                    _data.writeString(type);
                    _data.writeString(thresholdValues);
                    _data.writeString(triggerTime);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_registerCellQltyReport, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method registerCellQltyReport is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsMtk(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsMtkIms(IMtkRadioExModemResponse radioResponse, IMtkRadioExModemIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsCap(IMtkRadioExCapRadioResponse capRadioResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(capRadioResponse);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsCap, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsCap is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsForAtci(IAtciResponse atciResponseParam, IAtciIndication atciIndicationParam) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(atciResponseParam);
                    _data.writeStrongInterface(atciIndicationParam);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsForAtci, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsForAtci is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsEm(IEmRadioResponse radioResponse, IEmRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsEm is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
            public void setResponseFunctionsGba(IMtkRadioExModemResponse radioResponse) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsGba is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
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

            @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModem
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
