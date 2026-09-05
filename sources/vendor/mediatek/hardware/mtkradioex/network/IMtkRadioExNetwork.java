package vendor.mediatek.hardware.mtkradioex.network;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkIndication;
import vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse;
import vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioIndication;
import vendor.mediatek.hardware.mtkradioex.smartratswitch.ISmartRatSwitchRadioResponse;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExNetwork extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$network$IMtkRadioExNetwork".replace('$', '.');
    public static final String HASH = "583aefd4764eb70d99325928242c9ced29a9c0ee";
    public static final int VERSION = 1;

    void abortFemtocellList(int i, int i2) throws RemoteException;

    void cancelAvailableNetworks(int i, int i2) throws RemoteException;

    void cfgA2offset(int i, int i2, int i3, int i4) throws RemoteException;

    void cfgB1offset(int i, int i2, int i3, int i4) throws RemoteException;

    void clearLteAvailableFile(int i, int i2) throws RemoteException;

    void deactivateNrScgCommunication(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void enableCAPlusBandWidthFilter(int i, boolean z, int i2) throws RemoteException;

    void enableSCGfailure(int i, boolean z, int i2, int i3, int i4, int i5) throws RemoteException;

    void get4x4MimoEnabled(int i, int i2) throws RemoteException;

    void getAllBandMode(int i, int i2) throws RemoteException;

    void getApcInfo(int i, int i2) throws RemoteException;

    void getAvailableNetworksWithAct(int i, int i2) throws RemoteException;

    void getBandMode(int i, int i2) throws RemoteException;

    void getBandPriorityList(int i, int i2) throws RemoteException;

    void getCALinkCapabilityList(int i, int i2, int i3) throws RemoteException;

    void getCALinkEnableStatus(int i, String str, int i2, int i3) throws RemoteException;

    void getCaBandMode(int i, int i2, int i3) throws RemoteException;

    void getCampedFemtoCellInfo(int i, int i2) throws RemoteException;

    void getCurrentPOLList(int i, int i2) throws RemoteException;

    void getDeactivateNrScgCommunication(int i, int i2) throws RemoteException;

    void getDisable2G(int i, int i2) throws RemoteException;

    void getFemtocellList(int i, int i2) throws RemoteException;

    void getIWlanRegistrationState(int i, int i2) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getLte1xRttCellList(int i, boolean z, int i2) throws RemoteException;

    void getLteBsrTimer(int i, int i2) throws RemoteException;

    void getLteData(int i, int i2) throws RemoteException;

    void getLteRRCState(int i, int i2) throws RemoteException;

    void getLteReleaseVersion(int i, int i2) throws RemoteException;

    void getLteScanDuration(int i, int i2) throws RemoteException;

    void getPOLCapability(int i, int i2) throws RemoteException;

    void getPlmnNameFromSE13Table(int i, int i2, int i3, int i4) throws RemoteException;

    void getQamEnabled(int i, boolean z, int i2) throws RemoteException;

    void getRoamingEnable(int i, int i2, int i3) throws RemoteException;

    void getSignalStrengthWithWcdmaEcio(int i, int i2) throws RemoteException;

    void getSmartRatSwitch(int i, int i2, int i3) throws RemoteException;

    void getSuggestedPlmnList(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void getTOEInfo(int i, int i2) throws RemoteException;

    void getTm9Enabled(int i, boolean z, int i2) throws RemoteException;

    void queryFemtoCellSystemSelectionMode(int i, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void selectFemtocell(int i, String str, String str2, String str3, int i2) throws RemoteException;

    void set4x4MimoEnabled(int i, int i2, int i3) throws RemoteException;

    void setApcMode(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setBandPriorityList(int i, int[] iArr, int i2) throws RemoteException;

    void setBgsrchDeltaSleepTimer(int i, int i2, int i3) throws RemoteException;

    void setCALinkEnableStatus(int i, boolean z, String str, int i2, int i3) throws RemoteException;

    void setCarrierAggregationMode(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setDisable2G(int i, boolean z, int i2) throws RemoteException;

    void setFemtoCellSystemSelectionMode(int i, int i2, int i3) throws RemoteException;

    void setLteBandEnableStatus(int i, int i2, boolean z, int i3) throws RemoteException;

    void setLteBsrTimer(int i, int i2, int i3) throws RemoteException;

    void setLteReleaseVersion(int i, int i2, int i3) throws RemoteException;

    void setLteScanDuration(int i, int i2, int i3) throws RemoteException;

    void setNROption(int i, int i2, int i3) throws RemoteException;

    void setNetworkSelectionModeManualWithAct(int i, String str, String str2, String str3, int i2) throws RemoteException;

    void setNrBandMode(int i, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i2) throws RemoteException;

    void setPOLEntry(int i, int i2, String str, int i3, int i4) throws RemoteException;

    void setQamEnabled(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExNetworkResponse iMtkRadioExNetworkResponse, IMtkRadioExNetworkIndication iMtkRadioExNetworkIndication) throws RemoteException;

    void setResponseFunctionsSmartRatSwitch(ISmartRatSwitchRadioResponse iSmartRatSwitchRadioResponse, ISmartRatSwitchRadioIndication iSmartRatSwitchRadioIndication) throws RemoteException;

    void setRoamingEnable(int i, int[] iArr, int i2) throws RemoteException;

    void setSearchRat(int i, int[] iArr, int i2) throws RemoteException;

    void setSearchStoredFreqInfo(int i, int i2, int i3, int i4, int[] iArr, int i5) throws RemoteException;

    void setServiceStateToModem(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) throws RemoteException;

    void setTm9Enabled(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void smartRatSwitch(int i, int i2, int i3, int i4) throws RemoteException;

    public static class Default implements IMtkRadioExNetwork {
        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void abortFemtocellList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void cancelAvailableNetworks(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void cfgA2offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void cfgB1offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void clearLteAvailableFile(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void deactivateNrScgCommunication(int serial, boolean deactivate, boolean allowSCGAdd, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setCarrierAggregationMode(int serial, int mode, int option, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void enableCAPlusBandWidthFilter(int serial, boolean enable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void enableSCGfailure(int serial, boolean enable, int T1, int P1, int T2, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void get4x4MimoEnabled(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getAllBandMode(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getApcInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getAvailableNetworksWithAct(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getBandMode(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getBandPriorityList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCALinkCapabilityList(int serial, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCALinkEnableStatus(int serial, String bandsCombo, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCaBandMode(int serial, int primaryBandId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCampedFemtoCellInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getCurrentPOLList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getDeactivateNrScgCommunication(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getDisable2G(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getFemtocellList(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getIWlanRegistrationState(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLte1xRttCellList(int serial, boolean available, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteBsrTimer(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteData(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteRRCState(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteReleaseVersion(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getLteScanDuration(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getPOLCapability(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getPlmnNameFromSE13Table(int serial, int mcc, int mnc, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getQamEnabled(int serial, boolean ulOrDl, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getRoamingEnable(int serial, int phoneId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getSignalStrengthWithWcdmaEcio(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getSmartRatSwitch(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getSuggestedPlmnList(int serial, int rat, int num, int timer, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getTOEInfo(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void getTm9Enabled(int serial, boolean fddOrTdd, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void queryFemtoCellSystemSelectionMode(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void selectFemtocell(int serial, String operatorNumeric, String act, String csgId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void set4x4MimoEnabled(int serial, int enabled_bitmask, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setApcMode(int serial, int mode, int reportMode, int interval, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setBandPriorityList(int serial, int[] bandPriList, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setBgsrchDeltaSleepTimer(int serial, int sleepDuration, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setCALinkEnableStatus(int serial, boolean status, String bandsCombo, int linkType, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setDisable2G(int serial, boolean mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setFemtoCellSystemSelectionMode(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteBandEnableStatus(int serial, int bandId, boolean status, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteBsrTimer(int serial, int timer, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteReleaseVersion(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setLteScanDuration(int serial, int duration, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setNROption(int serial, int option, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setNetworkSelectionModeManualWithAct(int serial, String operatorNumeric, String act, String mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setNrBandMode(int serial, int[] saEnable, int[] saDisable, int[] nsaEnable, int[] nsaDisable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setPOLEntry(int serial, int index, String numeric, int nAct, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setQamEnabled(int serial, boolean ulOrDl, boolean enabled, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setSearchRat(int serial, int[] rat, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setSearchStoredFreqInfo(int serial, int operation, int plmn_id, int rat, int[] freq, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setServiceStateToModem(int serial, int voiceRegState, int dataRegState, int voiceRoamingType, int dataRoamingType, int rilVoiceRegState, int rilDataRegState, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setTm9Enabled(int serial, boolean fddOrTdd, boolean enabled, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void smartRatSwitch(int serial, int mode, int rat, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setRoamingEnable(int serial, int[] config, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setResponseFunctionsMtk(IMtkRadioExNetworkResponse radioResponse, IMtkRadioExNetworkIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public void setResponseFunctionsSmartRatSwitch(ISmartRatSwitchRadioResponse radioResponse, ISmartRatSwitchRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExNetwork {
        static final int TRANSACTION_abortFemtocellList = 1;
        static final int TRANSACTION_cancelAvailableNetworks = 2;
        static final int TRANSACTION_cfgA2offset = 3;
        static final int TRANSACTION_cfgB1offset = 4;
        static final int TRANSACTION_clearLteAvailableFile = 5;
        static final int TRANSACTION_deactivateNrScgCommunication = 6;
        static final int TRANSACTION_enableCAPlusBandWidthFilter = 8;
        static final int TRANSACTION_enableSCGfailure = 9;
        static final int TRANSACTION_get4x4MimoEnabled = 10;
        static final int TRANSACTION_getAllBandMode = 11;
        static final int TRANSACTION_getApcInfo = 12;
        static final int TRANSACTION_getAvailableNetworksWithAct = 13;
        static final int TRANSACTION_getBandMode = 14;
        static final int TRANSACTION_getBandPriorityList = 15;
        static final int TRANSACTION_getCALinkCapabilityList = 16;
        static final int TRANSACTION_getCALinkEnableStatus = 17;
        static final int TRANSACTION_getCaBandMode = 18;
        static final int TRANSACTION_getCampedFemtoCellInfo = 19;
        static final int TRANSACTION_getCurrentPOLList = 20;
        static final int TRANSACTION_getDeactivateNrScgCommunication = 21;
        static final int TRANSACTION_getDisable2G = 22;
        static final int TRANSACTION_getFemtocellList = 23;
        static final int TRANSACTION_getIWlanRegistrationState = 24;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLte1xRttCellList = 25;
        static final int TRANSACTION_getLteBsrTimer = 26;
        static final int TRANSACTION_getLteData = 27;
        static final int TRANSACTION_getLteRRCState = 28;
        static final int TRANSACTION_getLteReleaseVersion = 29;
        static final int TRANSACTION_getLteScanDuration = 30;
        static final int TRANSACTION_getPOLCapability = 31;
        static final int TRANSACTION_getPlmnNameFromSE13Table = 32;
        static final int TRANSACTION_getQamEnabled = 33;
        static final int TRANSACTION_getRoamingEnable = 34;
        static final int TRANSACTION_getSignalStrengthWithWcdmaEcio = 35;
        static final int TRANSACTION_getSmartRatSwitch = 36;
        static final int TRANSACTION_getSuggestedPlmnList = 37;
        static final int TRANSACTION_getTOEInfo = 38;
        static final int TRANSACTION_getTm9Enabled = 39;
        static final int TRANSACTION_queryFemtoCellSystemSelectionMode = 40;
        static final int TRANSACTION_responseAcknowledgementMtk = 64;
        static final int TRANSACTION_selectFemtocell = 41;
        static final int TRANSACTION_set4x4MimoEnabled = 42;
        static final int TRANSACTION_setApcMode = 43;
        static final int TRANSACTION_setBandPriorityList = 44;
        static final int TRANSACTION_setBgsrchDeltaSleepTimer = 45;
        static final int TRANSACTION_setCALinkEnableStatus = 46;
        static final int TRANSACTION_setCarrierAggregationMode = 7;
        static final int TRANSACTION_setDisable2G = 47;
        static final int TRANSACTION_setFemtoCellSystemSelectionMode = 48;
        static final int TRANSACTION_setLteBandEnableStatus = 49;
        static final int TRANSACTION_setLteBsrTimer = 50;
        static final int TRANSACTION_setLteReleaseVersion = 51;
        static final int TRANSACTION_setLteScanDuration = 52;
        static final int TRANSACTION_setNROption = 53;
        static final int TRANSACTION_setNetworkSelectionModeManualWithAct = 54;
        static final int TRANSACTION_setNrBandMode = 55;
        static final int TRANSACTION_setPOLEntry = 56;
        static final int TRANSACTION_setQamEnabled = 57;
        static final int TRANSACTION_setResponseFunctionsMtk = 65;
        static final int TRANSACTION_setResponseFunctionsSmartRatSwitch = 66;
        static final int TRANSACTION_setRoamingEnable = 63;
        static final int TRANSACTION_setSearchRat = 58;
        static final int TRANSACTION_setSearchStoredFreqInfo = 59;
        static final int TRANSACTION_setServiceStateToModem = 60;
        static final int TRANSACTION_setTm9Enabled = 61;
        static final int TRANSACTION_smartRatSwitch = 62;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExNetwork asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExNetwork)) {
                return (IMtkRadioExNetwork) iin;
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
                            abortFemtocellList(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            cancelAvailableNetworks(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            cfgA2offset(_arg03, _arg13, _arg2, _arg3);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            int _arg22 = data.readInt();
                            int _arg32 = data.readInt();
                            data.enforceNoDataAvail();
                            cfgB1offset(_arg04, _arg14, _arg22, _arg32);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            clearLteAvailableFile(_arg05, _arg15);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            boolean _arg16 = data.readBoolean();
                            boolean _arg23 = data.readBoolean();
                            int _arg33 = data.readInt();
                            data.enforceNoDataAvail();
                            deactivateNrScgCommunication(_arg06, _arg16, _arg23, _arg33);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg17 = data.readInt();
                            int _arg24 = data.readInt();
                            int _arg34 = data.readInt();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            setCarrierAggregationMode(_arg07, _arg17, _arg24, _arg34, _arg4);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            boolean _arg18 = data.readBoolean();
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            enableCAPlusBandWidthFilter(_arg08, _arg18, _arg25);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            boolean _arg19 = data.readBoolean();
                            int _arg26 = data.readInt();
                            int _arg35 = data.readInt();
                            int _arg42 = data.readInt();
                            int _arg5 = data.readInt();
                            data.enforceNoDataAvail();
                            enableSCGfailure(_arg09, _arg19, _arg26, _arg35, _arg42, _arg5);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            int _arg110 = data.readInt();
                            data.enforceNoDataAvail();
                            get4x4MimoEnabled(_arg010, _arg110);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int _arg111 = data.readInt();
                            data.enforceNoDataAvail();
                            getAllBandMode(_arg011, _arg111);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg112 = data.readInt();
                            data.enforceNoDataAvail();
                            getApcInfo(_arg012, _arg112);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            getAvailableNetworksWithAct(_arg013, _arg113);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            getBandMode(_arg014, _arg114);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            int _arg115 = data.readInt();
                            data.enforceNoDataAvail();
                            getBandPriorityList(_arg015, _arg115);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            int _arg116 = data.readInt();
                            int _arg27 = data.readInt();
                            data.enforceNoDataAvail();
                            getCALinkCapabilityList(_arg016, _arg116, _arg27);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            String _arg117 = data.readString();
                            int _arg28 = data.readInt();
                            int _arg36 = data.readInt();
                            data.enforceNoDataAvail();
                            getCALinkEnableStatus(_arg017, _arg117, _arg28, _arg36);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            int _arg118 = data.readInt();
                            int _arg29 = data.readInt();
                            data.enforceNoDataAvail();
                            getCaBandMode(_arg018, _arg118, _arg29);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            int _arg119 = data.readInt();
                            data.enforceNoDataAvail();
                            getCampedFemtoCellInfo(_arg019, _arg119);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            getCurrentPOLList(_arg020, _arg120);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            int _arg121 = data.readInt();
                            data.enforceNoDataAvail();
                            getDeactivateNrScgCommunication(_arg021, _arg121);
                            return true;
                        case TRANSACTION_getDisable2G /* 22 */:
                            int _arg022 = data.readInt();
                            int _arg122 = data.readInt();
                            data.enforceNoDataAvail();
                            getDisable2G(_arg022, _arg122);
                            return true;
                        case TRANSACTION_getFemtocellList /* 23 */:
                            int _arg023 = data.readInt();
                            int _arg123 = data.readInt();
                            data.enforceNoDataAvail();
                            getFemtocellList(_arg023, _arg123);
                            return true;
                        case TRANSACTION_getIWlanRegistrationState /* 24 */:
                            int _arg024 = data.readInt();
                            int _arg124 = data.readInt();
                            data.enforceNoDataAvail();
                            getIWlanRegistrationState(_arg024, _arg124);
                            return true;
                        case TRANSACTION_getLte1xRttCellList /* 25 */:
                            int _arg025 = data.readInt();
                            boolean _arg125 = data.readBoolean();
                            int _arg210 = data.readInt();
                            data.enforceNoDataAvail();
                            getLte1xRttCellList(_arg025, _arg125, _arg210);
                            return true;
                        case TRANSACTION_getLteBsrTimer /* 26 */:
                            int _arg026 = data.readInt();
                            int _arg126 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteBsrTimer(_arg026, _arg126);
                            return true;
                        case TRANSACTION_getLteData /* 27 */:
                            int _arg027 = data.readInt();
                            int _arg127 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteData(_arg027, _arg127);
                            return true;
                        case TRANSACTION_getLteRRCState /* 28 */:
                            int _arg028 = data.readInt();
                            int _arg128 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteRRCState(_arg028, _arg128);
                            return true;
                        case TRANSACTION_getLteReleaseVersion /* 29 */:
                            int _arg029 = data.readInt();
                            int _arg129 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteReleaseVersion(_arg029, _arg129);
                            return true;
                        case 30:
                            int _arg030 = data.readInt();
                            int _arg130 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteScanDuration(_arg030, _arg130);
                            return true;
                        case 31:
                            int _arg031 = data.readInt();
                            int _arg131 = data.readInt();
                            data.enforceNoDataAvail();
                            getPOLCapability(_arg031, _arg131);
                            return true;
                        case 32:
                            int _arg032 = data.readInt();
                            int _arg132 = data.readInt();
                            int _arg211 = data.readInt();
                            int _arg37 = data.readInt();
                            data.enforceNoDataAvail();
                            getPlmnNameFromSE13Table(_arg032, _arg132, _arg211, _arg37);
                            return true;
                        case 33:
                            int _arg033 = data.readInt();
                            boolean _arg133 = data.readBoolean();
                            int _arg212 = data.readInt();
                            data.enforceNoDataAvail();
                            getQamEnabled(_arg033, _arg133, _arg212);
                            return true;
                        case TRANSACTION_getRoamingEnable /* 34 */:
                            int _arg034 = data.readInt();
                            int _arg134 = data.readInt();
                            int _arg213 = data.readInt();
                            data.enforceNoDataAvail();
                            getRoamingEnable(_arg034, _arg134, _arg213);
                            return true;
                        case TRANSACTION_getSignalStrengthWithWcdmaEcio /* 35 */:
                            int _arg035 = data.readInt();
                            int _arg135 = data.readInt();
                            data.enforceNoDataAvail();
                            getSignalStrengthWithWcdmaEcio(_arg035, _arg135);
                            return true;
                        case TRANSACTION_getSmartRatSwitch /* 36 */:
                            int _arg036 = data.readInt();
                            int _arg136 = data.readInt();
                            int _arg214 = data.readInt();
                            data.enforceNoDataAvail();
                            getSmartRatSwitch(_arg036, _arg136, _arg214);
                            return true;
                        case TRANSACTION_getSuggestedPlmnList /* 37 */:
                            int _arg037 = data.readInt();
                            int _arg137 = data.readInt();
                            int _arg215 = data.readInt();
                            int _arg38 = data.readInt();
                            int _arg43 = data.readInt();
                            data.enforceNoDataAvail();
                            getSuggestedPlmnList(_arg037, _arg137, _arg215, _arg38, _arg43);
                            return true;
                        case TRANSACTION_getTOEInfo /* 38 */:
                            int _arg038 = data.readInt();
                            int _arg138 = data.readInt();
                            data.enforceNoDataAvail();
                            getTOEInfo(_arg038, _arg138);
                            return true;
                        case TRANSACTION_getTm9Enabled /* 39 */:
                            int _arg039 = data.readInt();
                            boolean _arg139 = data.readBoolean();
                            int _arg216 = data.readInt();
                            data.enforceNoDataAvail();
                            getTm9Enabled(_arg039, _arg139, _arg216);
                            return true;
                        case 40:
                            int _arg040 = data.readInt();
                            int _arg140 = data.readInt();
                            data.enforceNoDataAvail();
                            queryFemtoCellSystemSelectionMode(_arg040, _arg140);
                            return true;
                        case TRANSACTION_selectFemtocell /* 41 */:
                            int _arg041 = data.readInt();
                            String _arg141 = data.readString();
                            String _arg217 = data.readString();
                            String _arg39 = data.readString();
                            int _arg44 = data.readInt();
                            data.enforceNoDataAvail();
                            selectFemtocell(_arg041, _arg141, _arg217, _arg39, _arg44);
                            return true;
                        case TRANSACTION_set4x4MimoEnabled /* 42 */:
                            int _arg042 = data.readInt();
                            int _arg142 = data.readInt();
                            int _arg218 = data.readInt();
                            data.enforceNoDataAvail();
                            set4x4MimoEnabled(_arg042, _arg142, _arg218);
                            return true;
                        case TRANSACTION_setApcMode /* 43 */:
                            int _arg043 = data.readInt();
                            int _arg143 = data.readInt();
                            int _arg219 = data.readInt();
                            int _arg310 = data.readInt();
                            int _arg45 = data.readInt();
                            data.enforceNoDataAvail();
                            setApcMode(_arg043, _arg143, _arg219, _arg310, _arg45);
                            return true;
                        case TRANSACTION_setBandPriorityList /* 44 */:
                            int _arg044 = data.readInt();
                            int[] _arg144 = data.createIntArray();
                            int _arg220 = data.readInt();
                            data.enforceNoDataAvail();
                            setBandPriorityList(_arg044, _arg144, _arg220);
                            return true;
                        case TRANSACTION_setBgsrchDeltaSleepTimer /* 45 */:
                            int _arg045 = data.readInt();
                            int _arg145 = data.readInt();
                            int _arg221 = data.readInt();
                            data.enforceNoDataAvail();
                            setBgsrchDeltaSleepTimer(_arg045, _arg145, _arg221);
                            return true;
                        case 46:
                            int _arg046 = data.readInt();
                            boolean _arg146 = data.readBoolean();
                            String _arg222 = data.readString();
                            int _arg311 = data.readInt();
                            int _arg46 = data.readInt();
                            data.enforceNoDataAvail();
                            setCALinkEnableStatus(_arg046, _arg146, _arg222, _arg311, _arg46);
                            return true;
                        case 47:
                            int _arg047 = data.readInt();
                            boolean _arg147 = data.readBoolean();
                            int _arg223 = data.readInt();
                            data.enforceNoDataAvail();
                            setDisable2G(_arg047, _arg147, _arg223);
                            return true;
                        case TRANSACTION_setFemtoCellSystemSelectionMode /* 48 */:
                            int _arg048 = data.readInt();
                            int _arg148 = data.readInt();
                            int _arg224 = data.readInt();
                            data.enforceNoDataAvail();
                            setFemtoCellSystemSelectionMode(_arg048, _arg148, _arg224);
                            return true;
                        case TRANSACTION_setLteBandEnableStatus /* 49 */:
                            int _arg049 = data.readInt();
                            int _arg149 = data.readInt();
                            boolean _arg225 = data.readBoolean();
                            int _arg312 = data.readInt();
                            data.enforceNoDataAvail();
                            setLteBandEnableStatus(_arg049, _arg149, _arg225, _arg312);
                            return true;
                        case 50:
                            int _arg050 = data.readInt();
                            int _arg150 = data.readInt();
                            int _arg226 = data.readInt();
                            data.enforceNoDataAvail();
                            setLteBsrTimer(_arg050, _arg150, _arg226);
                            return true;
                        case TRANSACTION_setLteReleaseVersion /* 51 */:
                            int _arg051 = data.readInt();
                            int _arg151 = data.readInt();
                            int _arg227 = data.readInt();
                            data.enforceNoDataAvail();
                            setLteReleaseVersion(_arg051, _arg151, _arg227);
                            return true;
                        case TRANSACTION_setLteScanDuration /* 52 */:
                            int _arg052 = data.readInt();
                            int _arg152 = data.readInt();
                            int _arg228 = data.readInt();
                            data.enforceNoDataAvail();
                            setLteScanDuration(_arg052, _arg152, _arg228);
                            return true;
                        case TRANSACTION_setNROption /* 53 */:
                            int _arg053 = data.readInt();
                            int _arg153 = data.readInt();
                            int _arg229 = data.readInt();
                            data.enforceNoDataAvail();
                            setNROption(_arg053, _arg153, _arg229);
                            return true;
                        case TRANSACTION_setNetworkSelectionModeManualWithAct /* 54 */:
                            int _arg054 = data.readInt();
                            String _arg154 = data.readString();
                            String _arg230 = data.readString();
                            String _arg313 = data.readString();
                            int _arg47 = data.readInt();
                            data.enforceNoDataAvail();
                            setNetworkSelectionModeManualWithAct(_arg054, _arg154, _arg230, _arg313, _arg47);
                            return true;
                        case TRANSACTION_setNrBandMode /* 55 */:
                            int _arg055 = data.readInt();
                            int[] _arg155 = data.createIntArray();
                            int[] _arg231 = data.createIntArray();
                            int[] _arg314 = data.createIntArray();
                            int[] _arg48 = data.createIntArray();
                            int _arg52 = data.readInt();
                            data.enforceNoDataAvail();
                            setNrBandMode(_arg055, _arg155, _arg231, _arg314, _arg48, _arg52);
                            return true;
                        case TRANSACTION_setPOLEntry /* 56 */:
                            int _arg056 = data.readInt();
                            int _arg156 = data.readInt();
                            String _arg232 = data.readString();
                            int _arg315 = data.readInt();
                            int _arg49 = data.readInt();
                            data.enforceNoDataAvail();
                            setPOLEntry(_arg056, _arg156, _arg232, _arg315, _arg49);
                            return true;
                        case TRANSACTION_setQamEnabled /* 57 */:
                            int _arg057 = data.readInt();
                            boolean _arg157 = data.readBoolean();
                            boolean _arg233 = data.readBoolean();
                            int _arg316 = data.readInt();
                            data.enforceNoDataAvail();
                            setQamEnabled(_arg057, _arg157, _arg233, _arg316);
                            return true;
                        case TRANSACTION_setSearchRat /* 58 */:
                            int _arg058 = data.readInt();
                            int[] _arg158 = data.createIntArray();
                            int _arg234 = data.readInt();
                            data.enforceNoDataAvail();
                            setSearchRat(_arg058, _arg158, _arg234);
                            return true;
                        case TRANSACTION_setSearchStoredFreqInfo /* 59 */:
                            int _arg059 = data.readInt();
                            int _arg159 = data.readInt();
                            int _arg235 = data.readInt();
                            int _arg317 = data.readInt();
                            int[] _arg410 = data.createIntArray();
                            int _arg53 = data.readInt();
                            data.enforceNoDataAvail();
                            setSearchStoredFreqInfo(_arg059, _arg159, _arg235, _arg317, _arg410, _arg53);
                            return true;
                        case 60:
                            int _arg060 = data.readInt();
                            int _arg160 = data.readInt();
                            int _arg236 = data.readInt();
                            int _arg318 = data.readInt();
                            int _arg411 = data.readInt();
                            int _arg54 = data.readInt();
                            int _arg6 = data.readInt();
                            int _arg7 = data.readInt();
                            data.enforceNoDataAvail();
                            setServiceStateToModem(_arg060, _arg160, _arg236, _arg318, _arg411, _arg54, _arg6, _arg7);
                            return true;
                        case 61:
                            int _arg061 = data.readInt();
                            boolean _arg161 = data.readBoolean();
                            boolean _arg237 = data.readBoolean();
                            int _arg319 = data.readInt();
                            data.enforceNoDataAvail();
                            setTm9Enabled(_arg061, _arg161, _arg237, _arg319);
                            return true;
                        case 62:
                            int _arg062 = data.readInt();
                            int _arg162 = data.readInt();
                            int _arg238 = data.readInt();
                            int _arg320 = data.readInt();
                            data.enforceNoDataAvail();
                            smartRatSwitch(_arg062, _arg162, _arg238, _arg320);
                            return true;
                        case 63:
                            int _arg063 = data.readInt();
                            int[] _arg163 = data.createIntArray();
                            int _arg239 = data.readInt();
                            data.enforceNoDataAvail();
                            setRoamingEnable(_arg063, _arg163, _arg239);
                            return true;
                        case 64:
                            responseAcknowledgementMtk();
                            return true;
                        case TRANSACTION_setResponseFunctionsMtk /* 65 */:
                            IMtkRadioExNetworkResponse _arg064 = IMtkRadioExNetworkResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExNetworkIndication _arg164 = IMtkRadioExNetworkIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtk(_arg064, _arg164);
                            return true;
                        case TRANSACTION_setResponseFunctionsSmartRatSwitch /* 66 */:
                            ISmartRatSwitchRadioResponse _arg065 = ISmartRatSwitchRadioResponse.Stub.asInterface(data.readStrongBinder());
                            ISmartRatSwitchRadioIndication _arg165 = ISmartRatSwitchRadioIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsSmartRatSwitch(_arg065, _arg165);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExNetwork {
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void abortFemtocellList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method abortFemtocellList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void cancelAvailableNetworks(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cancelAvailableNetworks is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void cfgA2offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(offset);
                    _data.writeInt(threshBound);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cfgA2offset is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void cfgB1offset(int serial, int offset, int threshBound, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(offset);
                    _data.writeInt(threshBound);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cfgB1offset is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void clearLteAvailableFile(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method clearLteAvailableFile is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void deactivateNrScgCommunication(int serial, boolean deactivate, boolean allowSCGAdd, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(deactivate);
                    _data.writeBoolean(allowSCGAdd);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method deactivateNrScgCommunication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setCarrierAggregationMode(int serial, int mode, int option, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(option);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCarrierAggregationMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void enableCAPlusBandWidthFilter(int serial, boolean enable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableCAPlusBandWidthFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void enableSCGfailure(int serial, boolean enable, int T1, int P1, int T2, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(enable);
                    _data.writeInt(T1);
                    _data.writeInt(P1);
                    _data.writeInt(T2);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableSCGfailure is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void get4x4MimoEnabled(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method get4x4MimoEnabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getAllBandMode(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getAllBandMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getApcInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getApcInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getAvailableNetworksWithAct(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getAvailableNetworksWithAct is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getBandMode(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getBandMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getBandPriorityList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getBandPriorityList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCALinkCapabilityList(int serial, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCALinkCapabilityList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCALinkEnableStatus(int serial, String bandsCombo, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(bandsCombo);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCALinkEnableStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCaBandMode(int serial, int primaryBandId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(primaryBandId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCaBandMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCampedFemtoCellInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCampedFemtoCellInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getCurrentPOLList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCurrentPOLList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getDeactivateNrScgCommunication(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getDeactivateNrScgCommunication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getDisable2G(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getDisable2G, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getDisable2G is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getFemtocellList(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getFemtocellList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getFemtocellList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getIWlanRegistrationState(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getIWlanRegistrationState, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getIWlanRegistrationState is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLte1xRttCellList(int serial, boolean available, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(available);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLte1xRttCellList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLte1xRttCellList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteBsrTimer(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteBsrTimer, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteBsrTimer is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteData(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteData, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteData is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteRRCState(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteRRCState, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteRRCState is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteReleaseVersion(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteReleaseVersion, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteReleaseVersion is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getLteScanDuration(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteScanDuration is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getPOLCapability(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getPOLCapability is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getPlmnNameFromSE13Table(int serial, int mcc, int mnc, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mcc);
                    _data.writeInt(mnc);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getPlmnNameFromSE13Table is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getQamEnabled(int serial, boolean ulOrDl, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(ulOrDl);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getQamEnabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getRoamingEnable(int serial, int phoneId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(phoneId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getRoamingEnable, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getRoamingEnable is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getSignalStrengthWithWcdmaEcio(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSignalStrengthWithWcdmaEcio, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getSignalStrengthWithWcdmaEcio is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getSmartRatSwitch(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSmartRatSwitch, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getSmartRatSwitch is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getSuggestedPlmnList(int serial, int rat, int num, int timer, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rat);
                    _data.writeInt(num);
                    _data.writeInt(timer);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSuggestedPlmnList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getSuggestedPlmnList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getTOEInfo(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getTOEInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getTOEInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void getTm9Enabled(int serial, boolean fddOrTdd, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(fddOrTdd);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getTm9Enabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getTm9Enabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void queryFemtoCellSystemSelectionMode(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryFemtoCellSystemSelectionMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void selectFemtocell(int serial, String operatorNumeric, String act, String csgId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(operatorNumeric);
                    _data.writeString(act);
                    _data.writeString(csgId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_selectFemtocell, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method selectFemtocell is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void set4x4MimoEnabled(int serial, int enabled_bitmask, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(enabled_bitmask);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_set4x4MimoEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method set4x4MimoEnabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setApcMode(int serial, int mode, int reportMode, int interval, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(reportMode);
                    _data.writeInt(interval);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setApcMode, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setApcMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setBandPriorityList(int serial, int[] bandPriList, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(bandPriList);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBandPriorityList, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBandPriorityList is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setBgsrchDeltaSleepTimer(int serial, int sleepDuration, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(sleepDuration);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBgsrchDeltaSleepTimer, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBgsrchDeltaSleepTimer is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setCALinkEnableStatus(int serial, boolean status, String bandsCombo, int linkType, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(status);
                    _data.writeString(bandsCombo);
                    _data.writeInt(linkType);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCALinkEnableStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setDisable2G(int serial, boolean mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setDisable2G is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setFemtoCellSystemSelectionMode(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setFemtoCellSystemSelectionMode, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setFemtoCellSystemSelectionMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteBandEnableStatus(int serial, int bandId, boolean status, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(bandId);
                    _data.writeBoolean(status);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteBandEnableStatus, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteBandEnableStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteBsrTimer(int serial, int timer, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(timer);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(50, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteBsrTimer is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteReleaseVersion(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteReleaseVersion, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteReleaseVersion is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setLteScanDuration(int serial, int duration, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(duration);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteScanDuration, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteScanDuration is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setNROption(int serial, int option, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(option);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNROption, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNROption is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setNetworkSelectionModeManualWithAct(int serial, String operatorNumeric, String act, String mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(operatorNumeric);
                    _data.writeString(act);
                    _data.writeString(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNetworkSelectionModeManualWithAct, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNetworkSelectionModeManualWithAct is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setNrBandMode(int serial, int[] saEnable, int[] saDisable, int[] nsaEnable, int[] nsaDisable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(saEnable);
                    _data.writeIntArray(saDisable);
                    _data.writeIntArray(nsaEnable);
                    _data.writeIntArray(nsaDisable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNrBandMode, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNrBandMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setPOLEntry(int serial, int index, String numeric, int nAct, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(index);
                    _data.writeString(numeric);
                    _data.writeInt(nAct);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPOLEntry, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setPOLEntry is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setQamEnabled(int serial, boolean ulOrDl, boolean enabled, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(ulOrDl);
                    _data.writeBoolean(enabled);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setQamEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setQamEnabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setSearchRat(int serial, int[] rat, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(rat);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchRat, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSearchRat is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setSearchStoredFreqInfo(int serial, int operation, int plmn_id, int rat, int[] freq, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(operation);
                    _data.writeInt(plmn_id);
                    _data.writeInt(rat);
                    _data.writeIntArray(freq);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchStoredFreqInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSearchStoredFreqInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setServiceStateToModem(int serial, int voiceRegState, int dataRegState, int voiceRoamingType, int dataRoamingType, int rilVoiceRegState, int rilDataRegState, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(voiceRegState);
                    _data.writeInt(dataRegState);
                    _data.writeInt(voiceRoamingType);
                    _data.writeInt(dataRoamingType);
                    _data.writeInt(rilVoiceRegState);
                    _data.writeInt(rilDataRegState);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(60, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setServiceStateToModem is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setTm9Enabled(int serial, boolean fddOrTdd, boolean enabled, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(fddOrTdd);
                    _data.writeBoolean(enabled);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(61, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTm9Enabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void smartRatSwitch(int serial, int mode, int rat, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(rat);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(62, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method smartRatSwitch is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setRoamingEnable(int serial, int[] config, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeIntArray(config);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(63, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setRoamingEnable is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void responseAcknowledgementMtk() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(64, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method responseAcknowledgementMtk is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setResponseFunctionsMtk(IMtkRadioExNetworkResponse radioResponse, IMtkRadioExNetworkIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
            public void setResponseFunctionsSmartRatSwitch(ISmartRatSwitchRadioResponse radioResponse, ISmartRatSwitchRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsSmartRatSwitch, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsSmartRatSwitch is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetwork
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
