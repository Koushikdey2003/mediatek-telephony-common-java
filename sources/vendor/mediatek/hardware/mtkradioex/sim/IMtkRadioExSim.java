package vendor.mediatek.hardware.mtkradioex.sim;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.rsu.IRsuRadioIndication;
import vendor.mediatek.hardware.mtkradioex.rsu.IRsuRadioResponse;
import vendor.mediatek.hardware.mtkradioex.rsu.RsuRequestInfo;
import vendor.mediatek.hardware.mtkradioex.se.ISERadioIndication;
import vendor.mediatek.hardware.mtkradioex.se.ISERadioResponse;
import vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication;
import vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExSim extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$sim$IMtkRadioExSim".replace('$', '.');
    public static final String HASH = "a07d8b715b307b4e57aa776d209f5655cced7f96";
    public static final int VERSION = 1;

    void activateUiccCard(int i, int i2) throws RemoteException;

    void deactivateUiccCard(int i, int i2) throws RemoteException;

    void deleteUPBEntry(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void doGeneralSimAuthentication(int i, SimAuthStructure simAuthStructure, int i2) throws RemoteException;

    void editUPBEntry(int i, String[] strArr, int i2) throws RemoteException;

    void getATR(int i, int i2) throws RemoteException;

    void getCurrentUiccCardProvisioningStatus(int i, int i2) throws RemoteException;

    void getIccid(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getPhoneBookMemStorage(int i, int i2) throws RemoteException;

    void getPhoneBookStringsLength(int i, int i2) throws RemoteException;

    void handleStkCallSetupRequestFromSimWithResCode(int i, int i2, int i3) throws RemoteException;

    void queryNetworkLock(int i, int i2, int i3) throws RemoteException;

    void queryPhbStorageInfo(int i, int i2, int i3) throws RemoteException;

    void queryUPBAvailable(int i, int i2, int i3, int i4) throws RemoteException;

    void queryUPBCapability(int i, int i2) throws RemoteException;

    void readPhbEntry(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void readPhoneBookEntryExt(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBAasList(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBAnrEntry(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBEmailEntry(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBGasList(int i, int i2, int i3, int i4) throws RemoteException;

    void readUPBGrpEntry(int i, int i2, int i3) throws RemoteException;

    void readUPBSneEntry(int i, int i2, int i3, int i4) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void sendRsuRequest(int i, RsuRequestInfo rsuRequestInfo, int i2) throws RemoteException;

    void sendVsimNotification(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void sendVsimOperation(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6) throws RemoteException;

    void setNetworkLock(int i, int i2, int i3, String str, String str2, String str3, String str4, int i4) throws RemoteException;

    void setPhoneBookMemStorage(int i, String str, String str2, int i2) throws RemoteException;

    void setPhonebookReady(int i, int i2, int i3) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExSimResponse iMtkRadioExSimResponse, IMtkRadioExSimIndication iMtkRadioExSimIndication) throws RemoteException;

    void setResponseFunctionsRsu(IRsuRadioResponse iRsuRadioResponse, IRsuRadioIndication iRsuRadioIndication) throws RemoteException;

    void setResponseFunctionsSE(ISERadioResponse iSERadioResponse, ISERadioIndication iSERadioIndication) throws RemoteException;

    void setSimPower(int i, int i2, int i3) throws RemoteException;

    void supplyDepersonalization(int i, String str, int i2, int i3) throws RemoteException;

    void supplyDeviceNetworkDepersonalization(int i, String str, int i2) throws RemoteException;

    void writePhbEntry(int i, PhbEntryStructure phbEntryStructure, int i2) throws RemoteException;

    void writePhoneBookEntryExt(int i, PhbEntryExt phbEntryExt, int i2) throws RemoteException;

    void writeUPBGrpEntry(int i, int i2, int[] iArr, int i3) throws RemoteException;

    public static class Default implements IMtkRadioExSim {
        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getIccid(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void activateUiccCard(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void deactivateUiccCard(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void deleteUPBEntry(int serial, int entryType, int adnIndex, int entryIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void doGeneralSimAuthentication(int serial, SimAuthStructure simAuth, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void editUPBEntry(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getATR(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getCurrentUiccCardProvisioningStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getPhoneBookMemStorage(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void getPhoneBookStringsLength(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void handleStkCallSetupRequestFromSimWithResCode(int serial, int resultCode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryNetworkLock(int serial, int category, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryPhbStorageInfo(int serial, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryUPBAvailable(int serial, int eftype, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readPhbEntry(int serial, int type, int bIndex, int eIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readPhoneBookEntryExt(int serial, int index1, int index2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBAasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBAnrEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBEmailEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBGasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBGrpEntry(int serial, int adnIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void readUPBSneEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void sendRsuRequest(int serial, RsuRequestInfo rri, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void sendVsimNotification(int serial, int transactionId, int eventId, int simType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void sendVsimOperation(int serial, int transactionId, int eventId, int result, int dataLength, byte[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setNetworkLock(int serial, int category, int lockop, String password, String data_imsi, String gid1, String gid2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setPhoneBookMemStorage(int serial, String storage, String password, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setPhonebookReady(int serial, int ready, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setSimPower(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void writePhbEntry(int serial, PhbEntryStructure phbEntry, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void writePhoneBookEntryExt(int serial, PhbEntryExt phbEntryExt, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void writeUPBGrpEntry(int serial, int adnIndex, int[] grpIds, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void queryUPBCapability(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void supplyDepersonalization(int serial, String netPin, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void supplyDeviceNetworkDepersonalization(int serial, String pwd, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setResponseFunctionsMtk(IMtkRadioExSimResponse radioResponse, IMtkRadioExSimIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setResponseFunctionsRsu(IRsuRadioResponse radioResponse, IRsuRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public void setResponseFunctionsSE(ISERadioResponse radioResponse, ISERadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExSim {
        static final int TRANSACTION_activateUiccCard = 2;
        static final int TRANSACTION_deactivateUiccCard = 3;
        static final int TRANSACTION_deleteUPBEntry = 4;
        static final int TRANSACTION_doGeneralSimAuthentication = 5;
        static final int TRANSACTION_editUPBEntry = 6;
        static final int TRANSACTION_getATR = 7;
        static final int TRANSACTION_getCurrentUiccCardProvisioningStatus = 8;
        static final int TRANSACTION_getIccid = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPhoneBookMemStorage = 9;
        static final int TRANSACTION_getPhoneBookStringsLength = 10;
        static final int TRANSACTION_handleStkCallSetupRequestFromSimWithResCode = 11;
        static final int TRANSACTION_queryNetworkLock = 12;
        static final int TRANSACTION_queryPhbStorageInfo = 13;
        static final int TRANSACTION_queryUPBAvailable = 14;
        static final int TRANSACTION_queryUPBCapability = 33;
        static final int TRANSACTION_readPhbEntry = 15;
        static final int TRANSACTION_readPhoneBookEntryExt = 16;
        static final int TRANSACTION_readUPBAasList = 17;
        static final int TRANSACTION_readUPBAnrEntry = 18;
        static final int TRANSACTION_readUPBEmailEntry = 19;
        static final int TRANSACTION_readUPBGasList = 20;
        static final int TRANSACTION_readUPBGrpEntry = 21;
        static final int TRANSACTION_readUPBSneEntry = 22;
        static final int TRANSACTION_responseAcknowledgementMtk = 36;
        static final int TRANSACTION_sendRsuRequest = 23;
        static final int TRANSACTION_sendVsimNotification = 24;
        static final int TRANSACTION_sendVsimOperation = 25;
        static final int TRANSACTION_setNetworkLock = 26;
        static final int TRANSACTION_setPhoneBookMemStorage = 27;
        static final int TRANSACTION_setPhonebookReady = 28;
        static final int TRANSACTION_setResponseFunctionsMtk = 37;
        static final int TRANSACTION_setResponseFunctionsRsu = 38;
        static final int TRANSACTION_setResponseFunctionsSE = 39;
        static final int TRANSACTION_setSimPower = 29;
        static final int TRANSACTION_supplyDepersonalization = 34;
        static final int TRANSACTION_supplyDeviceNetworkDepersonalization = 35;
        static final int TRANSACTION_writePhbEntry = 30;
        static final int TRANSACTION_writePhoneBookEntryExt = 31;
        static final int TRANSACTION_writeUPBGrpEntry = 32;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExSim asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExSim)) {
                return (IMtkRadioExSim) iin;
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
                            getIccid(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            activateUiccCard(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            deactivateUiccCard(_arg03, _arg13);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            deleteUPBEntry(_arg04, _arg14, _arg2, _arg3, _arg4);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            SimAuthStructure _arg15 = (SimAuthStructure) data.readTypedObject(SimAuthStructure.CREATOR);
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            doGeneralSimAuthentication(_arg05, _arg15, _arg22);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            String[] _arg16 = data.createStringArray();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            editUPBEntry(_arg06, _arg16, _arg23);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            getATR(_arg07, _arg17);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            getCurrentUiccCardProvisioningStatus(_arg08, _arg18);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            getPhoneBookMemStorage(_arg09, _arg19);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            int _arg110 = data.readInt();
                            data.enforceNoDataAvail();
                            getPhoneBookStringsLength(_arg010, _arg110);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int _arg111 = data.readInt();
                            int _arg24 = data.readInt();
                            data.enforceNoDataAvail();
                            handleStkCallSetupRequestFromSimWithResCode(_arg011, _arg111, _arg24);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg112 = data.readInt();
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            queryNetworkLock(_arg012, _arg112, _arg25);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            int _arg113 = data.readInt();
                            int _arg26 = data.readInt();
                            data.enforceNoDataAvail();
                            queryPhbStorageInfo(_arg013, _arg113, _arg26);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int _arg114 = data.readInt();
                            int _arg27 = data.readInt();
                            int _arg32 = data.readInt();
                            data.enforceNoDataAvail();
                            queryUPBAvailable(_arg014, _arg114, _arg27, _arg32);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            int _arg115 = data.readInt();
                            int _arg28 = data.readInt();
                            int _arg33 = data.readInt();
                            int _arg42 = data.readInt();
                            data.enforceNoDataAvail();
                            readPhbEntry(_arg015, _arg115, _arg28, _arg33, _arg42);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            int _arg116 = data.readInt();
                            int _arg29 = data.readInt();
                            int _arg34 = data.readInt();
                            data.enforceNoDataAvail();
                            readPhoneBookEntryExt(_arg016, _arg116, _arg29, _arg34);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            int _arg117 = data.readInt();
                            int _arg210 = data.readInt();
                            int _arg35 = data.readInt();
                            data.enforceNoDataAvail();
                            readUPBAasList(_arg017, _arg117, _arg210, _arg35);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            int _arg118 = data.readInt();
                            int _arg211 = data.readInt();
                            int _arg36 = data.readInt();
                            data.enforceNoDataAvail();
                            readUPBAnrEntry(_arg018, _arg118, _arg211, _arg36);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            int _arg119 = data.readInt();
                            int _arg212 = data.readInt();
                            int _arg37 = data.readInt();
                            data.enforceNoDataAvail();
                            readUPBEmailEntry(_arg019, _arg119, _arg212, _arg37);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            int _arg120 = data.readInt();
                            int _arg213 = data.readInt();
                            int _arg38 = data.readInt();
                            data.enforceNoDataAvail();
                            readUPBGasList(_arg020, _arg120, _arg213, _arg38);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            int _arg121 = data.readInt();
                            int _arg214 = data.readInt();
                            data.enforceNoDataAvail();
                            readUPBGrpEntry(_arg021, _arg121, _arg214);
                            return true;
                        case TRANSACTION_readUPBSneEntry /* 22 */:
                            int _arg022 = data.readInt();
                            int _arg122 = data.readInt();
                            int _arg215 = data.readInt();
                            int _arg39 = data.readInt();
                            data.enforceNoDataAvail();
                            readUPBSneEntry(_arg022, _arg122, _arg215, _arg39);
                            return true;
                        case TRANSACTION_sendRsuRequest /* 23 */:
                            int _arg023 = data.readInt();
                            RsuRequestInfo _arg123 = (RsuRequestInfo) data.readTypedObject(RsuRequestInfo.CREATOR);
                            int _arg216 = data.readInt();
                            data.enforceNoDataAvail();
                            sendRsuRequest(_arg023, _arg123, _arg216);
                            return true;
                        case TRANSACTION_sendVsimNotification /* 24 */:
                            int _arg024 = data.readInt();
                            int _arg124 = data.readInt();
                            int _arg217 = data.readInt();
                            int _arg310 = data.readInt();
                            int _arg43 = data.readInt();
                            data.enforceNoDataAvail();
                            sendVsimNotification(_arg024, _arg124, _arg217, _arg310, _arg43);
                            return true;
                        case TRANSACTION_sendVsimOperation /* 25 */:
                            int _arg025 = data.readInt();
                            int _arg125 = data.readInt();
                            int _arg218 = data.readInt();
                            int _arg311 = data.readInt();
                            int _arg44 = data.readInt();
                            byte[] _arg5 = data.createByteArray();
                            int _arg6 = data.readInt();
                            data.enforceNoDataAvail();
                            sendVsimOperation(_arg025, _arg125, _arg218, _arg311, _arg44, _arg5, _arg6);
                            return true;
                        case TRANSACTION_setNetworkLock /* 26 */:
                            int _arg026 = data.readInt();
                            int _arg126 = data.readInt();
                            int _arg219 = data.readInt();
                            String _arg312 = data.readString();
                            String _arg45 = data.readString();
                            String _arg52 = data.readString();
                            String _arg62 = data.readString();
                            int _arg7 = data.readInt();
                            data.enforceNoDataAvail();
                            setNetworkLock(_arg026, _arg126, _arg219, _arg312, _arg45, _arg52, _arg62, _arg7);
                            return true;
                        case TRANSACTION_setPhoneBookMemStorage /* 27 */:
                            int _arg027 = data.readInt();
                            String _arg127 = data.readString();
                            String _arg220 = data.readString();
                            int _arg313 = data.readInt();
                            data.enforceNoDataAvail();
                            setPhoneBookMemStorage(_arg027, _arg127, _arg220, _arg313);
                            return true;
                        case TRANSACTION_setPhonebookReady /* 28 */:
                            int _arg028 = data.readInt();
                            int _arg128 = data.readInt();
                            int _arg221 = data.readInt();
                            data.enforceNoDataAvail();
                            setPhonebookReady(_arg028, _arg128, _arg221);
                            return true;
                        case TRANSACTION_setSimPower /* 29 */:
                            int _arg029 = data.readInt();
                            int _arg129 = data.readInt();
                            int _arg222 = data.readInt();
                            data.enforceNoDataAvail();
                            setSimPower(_arg029, _arg129, _arg222);
                            return true;
                        case 30:
                            int _arg030 = data.readInt();
                            PhbEntryStructure _arg130 = (PhbEntryStructure) data.readTypedObject(PhbEntryStructure.CREATOR);
                            int _arg223 = data.readInt();
                            data.enforceNoDataAvail();
                            writePhbEntry(_arg030, _arg130, _arg223);
                            return true;
                        case 31:
                            int _arg031 = data.readInt();
                            PhbEntryExt _arg131 = (PhbEntryExt) data.readTypedObject(PhbEntryExt.CREATOR);
                            int _arg224 = data.readInt();
                            data.enforceNoDataAvail();
                            writePhoneBookEntryExt(_arg031, _arg131, _arg224);
                            return true;
                        case 32:
                            int _arg032 = data.readInt();
                            int _arg132 = data.readInt();
                            int[] _arg225 = data.createIntArray();
                            int _arg314 = data.readInt();
                            data.enforceNoDataAvail();
                            writeUPBGrpEntry(_arg032, _arg132, _arg225, _arg314);
                            return true;
                        case 33:
                            int _arg033 = data.readInt();
                            int _arg133 = data.readInt();
                            data.enforceNoDataAvail();
                            queryUPBCapability(_arg033, _arg133);
                            return true;
                        case TRANSACTION_supplyDepersonalization /* 34 */:
                            int _arg034 = data.readInt();
                            String _arg134 = data.readString();
                            int _arg226 = data.readInt();
                            int _arg315 = data.readInt();
                            data.enforceNoDataAvail();
                            supplyDepersonalization(_arg034, _arg134, _arg226, _arg315);
                            return true;
                        case TRANSACTION_supplyDeviceNetworkDepersonalization /* 35 */:
                            int _arg035 = data.readInt();
                            String _arg135 = data.readString();
                            int _arg227 = data.readInt();
                            data.enforceNoDataAvail();
                            supplyDeviceNetworkDepersonalization(_arg035, _arg135, _arg227);
                            return true;
                        case TRANSACTION_responseAcknowledgementMtk /* 36 */:
                            responseAcknowledgementMtk();
                            return true;
                        case TRANSACTION_setResponseFunctionsMtk /* 37 */:
                            IMtkRadioExSimResponse _arg036 = IMtkRadioExSimResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExSimIndication _arg136 = IMtkRadioExSimIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtk(_arg036, _arg136);
                            return true;
                        case TRANSACTION_setResponseFunctionsRsu /* 38 */:
                            IRsuRadioResponse _arg037 = IRsuRadioResponse.Stub.asInterface(data.readStrongBinder());
                            IRsuRadioIndication _arg137 = IRsuRadioIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsRsu(_arg037, _arg137);
                            return true;
                        case TRANSACTION_setResponseFunctionsSE /* 39 */:
                            ISERadioResponse _arg038 = ISERadioResponse.Stub.asInterface(data.readStrongBinder());
                            ISERadioIndication _arg138 = ISERadioIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsSE(_arg038, _arg138);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExSim {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getIccid(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getIccid is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void activateUiccCard(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method activateUiccCard is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void deactivateUiccCard(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method deactivateUiccCard is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void deleteUPBEntry(int serial, int entryType, int adnIndex, int entryIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(entryType);
                    _data.writeInt(adnIndex);
                    _data.writeInt(entryIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method deleteUPBEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void doGeneralSimAuthentication(int serial, SimAuthStructure simAuth, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(simAuth, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method doGeneralSimAuthentication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void editUPBEntry(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method editUPBEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getATR(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getATR is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getCurrentUiccCardProvisioningStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCurrentUiccCardProvisioningStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getPhoneBookMemStorage(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getPhoneBookMemStorage is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void getPhoneBookStringsLength(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getPhoneBookStringsLength is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void handleStkCallSetupRequestFromSimWithResCode(int serial, int resultCode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(resultCode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method handleStkCallSetupRequestFromSimWithResCode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryNetworkLock(int serial, int category, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(category);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryNetworkLock is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryPhbStorageInfo(int serial, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryPhbStorageInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryUPBAvailable(int serial, int eftype, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(eftype);
                    _data.writeInt(fileIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryUPBAvailable is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readPhbEntry(int serial, int type, int bIndex, int eIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(type);
                    _data.writeInt(bIndex);
                    _data.writeInt(eIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readPhbEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readPhoneBookEntryExt(int serial, int index1, int index2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(index1);
                    _data.writeInt(index2);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readPhoneBookEntryExt is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBAasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readUPBAasList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBAnrEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(fileIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readUPBAnrEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBEmailEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(fileIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readUPBEmailEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBGasList(int serial, int startIndex, int endIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(startIndex);
                    _data.writeInt(endIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readUPBGasList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBGrpEntry(int serial, int adnIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readUPBGrpEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void readUPBSneEntry(int serial, int adnIndex, int fileIndex, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeInt(fileIndex);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_readUPBSneEntry, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method readUPBSneEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void sendRsuRequest(int serial, RsuRequestInfo rri, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(rri, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendRsuRequest, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRsuRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void sendVsimNotification(int serial, int transactionId, int eventId, int simType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(transactionId);
                    _data.writeInt(eventId);
                    _data.writeInt(simType);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendVsimNotification, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendVsimNotification is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void sendVsimOperation(int serial, int transactionId, int eventId, int result, int dataLength, byte[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(transactionId);
                    _data.writeInt(eventId);
                    _data.writeInt(result);
                    _data.writeInt(dataLength);
                    _data.writeByteArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_sendVsimOperation, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendVsimOperation is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setNetworkLock(int serial, int category, int lockop, String password, String data_imsi, String gid1, String gid2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(category);
                    _data.writeInt(lockop);
                    _data.writeString(password);
                    _data.writeString(data_imsi);
                    _data.writeString(gid1);
                    _data.writeString(gid2);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNetworkLock, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNetworkLock is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setPhoneBookMemStorage(int serial, String storage, String password, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(storage);
                    _data.writeString(password);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPhoneBookMemStorage, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setPhoneBookMemStorage is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setPhonebookReady(int serial, int ready, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(ready);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPhonebookReady, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setPhonebookReady is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setSimPower(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSimPower, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSimPower is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void writePhbEntry(int serial, PhbEntryStructure phbEntry, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(phbEntry, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method writePhbEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void writePhoneBookEntryExt(int serial, PhbEntryExt phbEntryExt, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(phbEntryExt, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method writePhoneBookEntryExt is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void writeUPBGrpEntry(int serial, int adnIndex, int[] grpIds, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(adnIndex);
                    _data.writeIntArray(grpIds);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method writeUPBGrpEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void queryUPBCapability(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryUPBCapability is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void supplyDepersonalization(int serial, String netPin, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(netPin);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_supplyDepersonalization, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method supplyDepersonalization is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void supplyDeviceNetworkDepersonalization(int serial, String pwd, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(pwd);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_supplyDeviceNetworkDepersonalization, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method supplyDeviceNetworkDepersonalization is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setResponseFunctionsMtk(IMtkRadioExSimResponse radioResponse, IMtkRadioExSimIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setResponseFunctionsRsu(IRsuRadioResponse radioResponse, IRsuRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsRsu, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsRsu is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
            public void setResponseFunctionsSE(ISERadioResponse radioResponse, ISERadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsSE, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsSE is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
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

            @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim
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
