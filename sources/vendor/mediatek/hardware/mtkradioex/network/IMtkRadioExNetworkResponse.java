package vendor.mediatek.hardware.mtkradioex.network;

import android.hardware.radio.RadioResponseInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExNetworkResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$network$IMtkRadioExNetworkResponse".replace('$', '.');
    public static final String HASH = "583aefd4764eb70d99325928242c9ced29a9c0ee";
    public static final int VERSION = 1;

    void abortFemtocellListResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cancelAvailableNetworksResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cfgA2offsetResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cfgB1offsetResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void clearLteAvailableFileResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void deactivateNrScgCommunicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableCAPlusBandWidthFilterResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void enableSCGfailureResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void get4x4MimoEnabledResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getAllBandModeResponse(RadioResponseInfo radioResponseInfo, BandModeInfo bandModeInfo) throws RemoteException;

    void getApcInfoResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getAvailableNetworksWithActResponse(RadioResponseInfo radioResponseInfo, OperatorInfoWithAct[] operatorInfoWithActArr) throws RemoteException;

    void getBandModeResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getBandPriorityListResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getCALinkCapabilityListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo, boolean z) throws RemoteException;

    void getCaBandModeResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getCampedFemtoCellInfoResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getCurrentPOLListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getDeactivateNrScgCommunicationResponse(RadioResponseInfo radioResponseInfo, int i, int i2) throws RemoteException;

    void getDisable2GResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getFemtocellListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getIWlanRegistrationStateResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getLte1xRttCellListResponse(RadioResponseInfo radioResponseInfo, Lte1xRttCellInfo[] lte1xRttCellInfoArr) throws RemoteException;

    void getLteBsrTimerResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteDataResponse(RadioResponseInfo radioResponseInfo, LteData lteData) throws RemoteException;

    void getLteRRCStateResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteReleaseVersionResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getLteScanDurationResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getPOLCapabilityResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getPlmnNameFromSE13TableResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void getQamEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) throws RemoteException;

    void getRoamingEnableResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo radioResponseInfo, SignalStrengthWithWcdmaEcio signalStrengthWithWcdmaEcio) throws RemoteException;

    void getSuggestedPlmnListResponse(RadioResponseInfo radioResponseInfo, String[] strArr) throws RemoteException;

    void getTOEInfoResponse(RadioResponseInfo radioResponseInfo, String str, String str2, String str3) throws RemoteException;

    void getTm9EnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) throws RemoteException;

    void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void selectFemtocellResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void set4x4MimoEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setApcModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBandPriorityListResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCarrierAggregationModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setDisable2GResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteBandEnableStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteBsrTimerResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteReleaseVersionResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setLteScanDurationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNROptionResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setNrBandModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setPOLEntryResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setQamEnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setRoamingEnableResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSearchRatResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSearchStoredFreqInfoResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setServiceStateToModemResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setTm9EnabledResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExNetworkResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void abortFemtocellListResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void cancelAvailableNetworksResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void cfgA2offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void cfgB1offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void clearLteAvailableFileResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setCarrierAggregationModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void enableSCGfailureResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void get4x4MimoEnabledResponse(RadioResponseInfo info, int enabled_bitmask) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getAllBandModeResponse(RadioResponseInfo info, BandModeInfo data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getApcInfoResponse(RadioResponseInfo info, int[] cellInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getAvailableNetworksWithActResponse(RadioResponseInfo info, OperatorInfoWithAct[] networkInfosWithAct) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getBandPriorityListResponse(RadioResponseInfo info, int[] bandPriList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCALinkCapabilityListResponse(RadioResponseInfo info, String[] linkCapabilityList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCALinkEnableStatusResponse(RadioResponseInfo info, boolean status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCaBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCampedFemtoCellInfoResponse(RadioResponseInfo info, String[] femto) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, String[] polList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getDisable2GResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getFemtocellListResponse(RadioResponseInfo responseInfo, String[] femtoList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getIWlanRegistrationStateResponse(RadioResponseInfo responseInfo, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLte1xRttCellListResponse(RadioResponseInfo info, Lte1xRttCellInfo[] list) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteBsrTimerResponse(RadioResponseInfo info, int timer) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteDataResponse(RadioResponseInfo info, LteData data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteRRCStateResponse(RadioResponseInfo info, int state) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteReleaseVersionResponse(RadioResponseInfo info, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getLteScanDurationResponse(RadioResponseInfo info, int duration) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, int[] polCapability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getPlmnNameFromSE13TableResponse(RadioResponseInfo info, String name) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getQamEnabledResponse(RadioResponseInfo info, boolean ulOrDl, boolean enabled) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getRoamingEnableResponse(RadioResponseInfo responseInfo, int[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo info, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, String[] plmnList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getTOEInfoResponse(RadioResponseInfo info, String longName, String shortName, String numeric) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void getTm9EnabledResponse(RadioResponseInfo info, boolean fddOrTdd, boolean enabled) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void selectFemtocellResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void set4x4MimoEnabledResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setApcModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setBandPriorityListResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setCALinkEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setDisable2GResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteBandEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteBsrTimerResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteReleaseVersionResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setLteScanDurationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setNROptionResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setNrBandModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setPOLEntryResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setQamEnabledResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setSearchRatResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setTm9EnabledResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public void setRoamingEnableResponse(RadioResponseInfo responseInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExNetworkResponse {
        static final int TRANSACTION_abortFemtocellListResponse = 1;
        static final int TRANSACTION_cancelAvailableNetworksResponse = 2;
        static final int TRANSACTION_cfgA2offsetResponse = 3;
        static final int TRANSACTION_cfgB1offsetResponse = 4;
        static final int TRANSACTION_clearLteAvailableFileResponse = 5;
        static final int TRANSACTION_deactivateNrScgCommunicationResponse = 6;
        static final int TRANSACTION_enableCAPlusBandWidthFilterResponse = 8;
        static final int TRANSACTION_enableSCGfailureResponse = 9;
        static final int TRANSACTION_get4x4MimoEnabledResponse = 10;
        static final int TRANSACTION_getAllBandModeResponse = 11;
        static final int TRANSACTION_getApcInfoResponse = 12;
        static final int TRANSACTION_getAvailableNetworksWithActResponse = 13;
        static final int TRANSACTION_getBandModeResponse = 14;
        static final int TRANSACTION_getBandPriorityListResponse = 15;
        static final int TRANSACTION_getCALinkCapabilityListResponse = 16;
        static final int TRANSACTION_getCALinkEnableStatusResponse = 17;
        static final int TRANSACTION_getCaBandModeResponse = 18;
        static final int TRANSACTION_getCampedFemtoCellInfoResponse = 19;
        static final int TRANSACTION_getCurrentPOLListResponse = 20;
        static final int TRANSACTION_getDeactivateNrScgCommunicationResponse = 21;
        static final int TRANSACTION_getDisable2GResponse = 22;
        static final int TRANSACTION_getFemtocellListResponse = 23;
        static final int TRANSACTION_getIWlanRegistrationStateResponse = 24;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLte1xRttCellListResponse = 25;
        static final int TRANSACTION_getLteBsrTimerResponse = 26;
        static final int TRANSACTION_getLteDataResponse = 27;
        static final int TRANSACTION_getLteRRCStateResponse = 28;
        static final int TRANSACTION_getLteReleaseVersionResponse = 29;
        static final int TRANSACTION_getLteScanDurationResponse = 30;
        static final int TRANSACTION_getPOLCapabilityResponse = 31;
        static final int TRANSACTION_getPlmnNameFromSE13TableResponse = 32;
        static final int TRANSACTION_getQamEnabledResponse = 33;
        static final int TRANSACTION_getRoamingEnableResponse = 34;
        static final int TRANSACTION_getSignalStrengthWithWcdmaEcioResponse = 35;
        static final int TRANSACTION_getSuggestedPlmnListResponse = 36;
        static final int TRANSACTION_getTOEInfoResponse = 37;
        static final int TRANSACTION_getTm9EnabledResponse = 38;
        static final int TRANSACTION_queryFemtoCellSystemSelectionModeResponse = 39;
        static final int TRANSACTION_selectFemtocellResponse = 40;
        static final int TRANSACTION_set4x4MimoEnabledResponse = 41;
        static final int TRANSACTION_setApcModeResponse = 42;
        static final int TRANSACTION_setBandPriorityListResponse = 43;
        static final int TRANSACTION_setBgsrchDeltaSleepTimerResponse = 44;
        static final int TRANSACTION_setCALinkEnableStatusResponse = 45;
        static final int TRANSACTION_setCarrierAggregationModeResponse = 7;
        static final int TRANSACTION_setDisable2GResponse = 46;
        static final int TRANSACTION_setFemtoCellSystemSelectionModeResponse = 47;
        static final int TRANSACTION_setLteBandEnableStatusResponse = 48;
        static final int TRANSACTION_setLteBsrTimerResponse = 49;
        static final int TRANSACTION_setLteReleaseVersionResponse = 50;
        static final int TRANSACTION_setLteScanDurationResponse = 51;
        static final int TRANSACTION_setNROptionResponse = 52;
        static final int TRANSACTION_setNetworkSelectionModeManualWithActResponse = 53;
        static final int TRANSACTION_setNrBandModeResponse = 54;
        static final int TRANSACTION_setPOLEntryResponse = 55;
        static final int TRANSACTION_setQamEnabledResponse = 56;
        static final int TRANSACTION_setRoamingEnableResponse = 61;
        static final int TRANSACTION_setSearchRatResponse = 57;
        static final int TRANSACTION_setSearchStoredFreqInfoResponse = 58;
        static final int TRANSACTION_setServiceStateToModemResponse = 59;
        static final int TRANSACTION_setTm9EnabledResponse = 60;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExNetworkResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExNetworkResponse)) {
                return (IMtkRadioExNetworkResponse) iin;
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
                            RadioResponseInfo _arg0 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            abortFemtocellListResponse(_arg0);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            cancelAvailableNetworksResponse(_arg02);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            cfgA2offsetResponse(_arg03);
                            return true;
                        case 4:
                            RadioResponseInfo _arg04 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            cfgB1offsetResponse(_arg04);
                            return true;
                        case 5:
                            RadioResponseInfo _arg05 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            clearLteAvailableFileResponse(_arg05);
                            return true;
                        case 6:
                            RadioResponseInfo _arg06 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            deactivateNrScgCommunicationResponse(_arg06);
                            return true;
                        case 7:
                            RadioResponseInfo _arg07 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setCarrierAggregationModeResponse(_arg07);
                            return true;
                        case 8:
                            RadioResponseInfo _arg08 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            enableCAPlusBandWidthFilterResponse(_arg08);
                            return true;
                        case 9:
                            RadioResponseInfo _arg09 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            enableSCGfailureResponse(_arg09);
                            return true;
                        case 10:
                            RadioResponseInfo _arg010 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            get4x4MimoEnabledResponse(_arg010, _arg1);
                            return true;
                        case 11:
                            RadioResponseInfo _arg011 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            BandModeInfo _arg12 = (BandModeInfo) data.readTypedObject(BandModeInfo.CREATOR);
                            data.enforceNoDataAvail();
                            getAllBandModeResponse(_arg011, _arg12);
                            return true;
                        case 12:
                            RadioResponseInfo _arg012 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg13 = data.createIntArray();
                            data.enforceNoDataAvail();
                            getApcInfoResponse(_arg012, _arg13);
                            return true;
                        case 13:
                            RadioResponseInfo _arg013 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            OperatorInfoWithAct[] _arg14 = (OperatorInfoWithAct[]) data.createTypedArray(OperatorInfoWithAct.CREATOR);
                            data.enforceNoDataAvail();
                            getAvailableNetworksWithActResponse(_arg013, _arg14);
                            return true;
                        case 14:
                            RadioResponseInfo _arg014 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg15 = data.createIntArray();
                            data.enforceNoDataAvail();
                            getBandModeResponse(_arg014, _arg15);
                            return true;
                        case 15:
                            RadioResponseInfo _arg015 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg16 = data.createIntArray();
                            data.enforceNoDataAvail();
                            getBandPriorityListResponse(_arg015, _arg16);
                            return true;
                        case 16:
                            RadioResponseInfo _arg016 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg17 = data.createStringArray();
                            data.enforceNoDataAvail();
                            getCALinkCapabilityListResponse(_arg016, _arg17);
                            return true;
                        case 17:
                            RadioResponseInfo _arg017 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            boolean _arg18 = data.readBoolean();
                            data.enforceNoDataAvail();
                            getCALinkEnableStatusResponse(_arg017, _arg18);
                            return true;
                        case 18:
                            RadioResponseInfo _arg018 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg19 = data.createIntArray();
                            data.enforceNoDataAvail();
                            getCaBandModeResponse(_arg018, _arg19);
                            return true;
                        case 19:
                            RadioResponseInfo _arg019 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg110 = data.createStringArray();
                            data.enforceNoDataAvail();
                            getCampedFemtoCellInfoResponse(_arg019, _arg110);
                            return true;
                        case 20:
                            RadioResponseInfo _arg020 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg111 = data.createStringArray();
                            data.enforceNoDataAvail();
                            getCurrentPOLListResponse(_arg020, _arg111);
                            return true;
                        case 21:
                            RadioResponseInfo _arg021 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg112 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            getDeactivateNrScgCommunicationResponse(_arg021, _arg112, _arg2);
                            return true;
                        case TRANSACTION_getDisable2GResponse /* 22 */:
                            RadioResponseInfo _arg022 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            getDisable2GResponse(_arg022, _arg113);
                            return true;
                        case TRANSACTION_getFemtocellListResponse /* 23 */:
                            RadioResponseInfo _arg023 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg114 = data.createStringArray();
                            data.enforceNoDataAvail();
                            getFemtocellListResponse(_arg023, _arg114);
                            return true;
                        case TRANSACTION_getIWlanRegistrationStateResponse /* 24 */:
                            RadioResponseInfo _arg024 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg115 = data.readInt();
                            data.enforceNoDataAvail();
                            getIWlanRegistrationStateResponse(_arg024, _arg115);
                            return true;
                        case TRANSACTION_getLte1xRttCellListResponse /* 25 */:
                            RadioResponseInfo _arg025 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            Lte1xRttCellInfo[] _arg116 = (Lte1xRttCellInfo[]) data.createTypedArray(Lte1xRttCellInfo.CREATOR);
                            data.enforceNoDataAvail();
                            getLte1xRttCellListResponse(_arg025, _arg116);
                            return true;
                        case TRANSACTION_getLteBsrTimerResponse /* 26 */:
                            RadioResponseInfo _arg026 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg117 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteBsrTimerResponse(_arg026, _arg117);
                            return true;
                        case TRANSACTION_getLteDataResponse /* 27 */:
                            RadioResponseInfo _arg027 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            LteData _arg118 = (LteData) data.readTypedObject(LteData.CREATOR);
                            data.enforceNoDataAvail();
                            getLteDataResponse(_arg027, _arg118);
                            return true;
                        case TRANSACTION_getLteRRCStateResponse /* 28 */:
                            RadioResponseInfo _arg028 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg119 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteRRCStateResponse(_arg028, _arg119);
                            return true;
                        case TRANSACTION_getLteReleaseVersionResponse /* 29 */:
                            RadioResponseInfo _arg029 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg120 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteReleaseVersionResponse(_arg029, _arg120);
                            return true;
                        case 30:
                            RadioResponseInfo _arg030 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg121 = data.readInt();
                            data.enforceNoDataAvail();
                            getLteScanDurationResponse(_arg030, _arg121);
                            return true;
                        case 31:
                            RadioResponseInfo _arg031 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg122 = data.createIntArray();
                            data.enforceNoDataAvail();
                            getPOLCapabilityResponse(_arg031, _arg122);
                            return true;
                        case 32:
                            RadioResponseInfo _arg032 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String _arg123 = data.readString();
                            data.enforceNoDataAvail();
                            getPlmnNameFromSE13TableResponse(_arg032, _arg123);
                            return true;
                        case 33:
                            RadioResponseInfo _arg033 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            boolean _arg124 = data.readBoolean();
                            boolean _arg22 = data.readBoolean();
                            data.enforceNoDataAvail();
                            getQamEnabledResponse(_arg033, _arg124, _arg22);
                            return true;
                        case TRANSACTION_getRoamingEnableResponse /* 34 */:
                            RadioResponseInfo _arg034 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg125 = data.createIntArray();
                            data.enforceNoDataAvail();
                            getRoamingEnableResponse(_arg034, _arg125);
                            return true;
                        case TRANSACTION_getSignalStrengthWithWcdmaEcioResponse /* 35 */:
                            RadioResponseInfo _arg035 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            SignalStrengthWithWcdmaEcio _arg126 = (SignalStrengthWithWcdmaEcio) data.readTypedObject(SignalStrengthWithWcdmaEcio.CREATOR);
                            data.enforceNoDataAvail();
                            getSignalStrengthWithWcdmaEcioResponse(_arg035, _arg126);
                            return true;
                        case TRANSACTION_getSuggestedPlmnListResponse /* 36 */:
                            RadioResponseInfo _arg036 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String[] _arg127 = data.createStringArray();
                            data.enforceNoDataAvail();
                            getSuggestedPlmnListResponse(_arg036, _arg127);
                            return true;
                        case TRANSACTION_getTOEInfoResponse /* 37 */:
                            RadioResponseInfo _arg037 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String _arg128 = data.readString();
                            String _arg23 = data.readString();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            getTOEInfoResponse(_arg037, _arg128, _arg23, _arg3);
                            return true;
                        case TRANSACTION_getTm9EnabledResponse /* 38 */:
                            RadioResponseInfo _arg038 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            boolean _arg129 = data.readBoolean();
                            boolean _arg24 = data.readBoolean();
                            data.enforceNoDataAvail();
                            getTm9EnabledResponse(_arg038, _arg129, _arg24);
                            return true;
                        case TRANSACTION_queryFemtoCellSystemSelectionModeResponse /* 39 */:
                            RadioResponseInfo _arg039 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg130 = data.readInt();
                            data.enforceNoDataAvail();
                            queryFemtoCellSystemSelectionModeResponse(_arg039, _arg130);
                            return true;
                        case 40:
                            RadioResponseInfo _arg040 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            selectFemtocellResponse(_arg040);
                            return true;
                        case TRANSACTION_set4x4MimoEnabledResponse /* 41 */:
                            RadioResponseInfo _arg041 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            set4x4MimoEnabledResponse(_arg041);
                            return true;
                        case TRANSACTION_setApcModeResponse /* 42 */:
                            RadioResponseInfo _arg042 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setApcModeResponse(_arg042);
                            return true;
                        case TRANSACTION_setBandPriorityListResponse /* 43 */:
                            RadioResponseInfo _arg043 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setBandPriorityListResponse(_arg043);
                            return true;
                        case TRANSACTION_setBgsrchDeltaSleepTimerResponse /* 44 */:
                            RadioResponseInfo _arg044 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setBgsrchDeltaSleepTimerResponse(_arg044);
                            return true;
                        case TRANSACTION_setCALinkEnableStatusResponse /* 45 */:
                            RadioResponseInfo _arg045 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setCALinkEnableStatusResponse(_arg045);
                            return true;
                        case 46:
                            RadioResponseInfo _arg046 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setDisable2GResponse(_arg046);
                            return true;
                        case 47:
                            RadioResponseInfo _arg047 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setFemtoCellSystemSelectionModeResponse(_arg047);
                            return true;
                        case TRANSACTION_setLteBandEnableStatusResponse /* 48 */:
                            RadioResponseInfo _arg048 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setLteBandEnableStatusResponse(_arg048);
                            return true;
                        case TRANSACTION_setLteBsrTimerResponse /* 49 */:
                            RadioResponseInfo _arg049 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setLteBsrTimerResponse(_arg049);
                            return true;
                        case 50:
                            RadioResponseInfo _arg050 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setLteReleaseVersionResponse(_arg050);
                            return true;
                        case TRANSACTION_setLteScanDurationResponse /* 51 */:
                            RadioResponseInfo _arg051 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setLteScanDurationResponse(_arg051);
                            return true;
                        case TRANSACTION_setNROptionResponse /* 52 */:
                            RadioResponseInfo _arg052 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setNROptionResponse(_arg052);
                            return true;
                        case TRANSACTION_setNetworkSelectionModeManualWithActResponse /* 53 */:
                            RadioResponseInfo _arg053 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setNetworkSelectionModeManualWithActResponse(_arg053);
                            return true;
                        case TRANSACTION_setNrBandModeResponse /* 54 */:
                            RadioResponseInfo _arg054 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setNrBandModeResponse(_arg054);
                            return true;
                        case TRANSACTION_setPOLEntryResponse /* 55 */:
                            RadioResponseInfo _arg055 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setPOLEntryResponse(_arg055);
                            return true;
                        case TRANSACTION_setQamEnabledResponse /* 56 */:
                            RadioResponseInfo _arg056 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setQamEnabledResponse(_arg056);
                            return true;
                        case TRANSACTION_setSearchRatResponse /* 57 */:
                            RadioResponseInfo _arg057 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setSearchRatResponse(_arg057);
                            return true;
                        case TRANSACTION_setSearchStoredFreqInfoResponse /* 58 */:
                            RadioResponseInfo _arg058 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setSearchStoredFreqInfoResponse(_arg058);
                            return true;
                        case TRANSACTION_setServiceStateToModemResponse /* 59 */:
                            RadioResponseInfo _arg059 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setServiceStateToModemResponse(_arg059);
                            return true;
                        case 60:
                            RadioResponseInfo _arg060 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setTm9EnabledResponse(_arg060);
                            return true;
                        case 61:
                            RadioResponseInfo _arg061 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setRoamingEnableResponse(_arg061);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExNetworkResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void abortFemtocellListResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method abortFemtocellListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void cancelAvailableNetworksResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cancelAvailableNetworksResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void cfgA2offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cfgA2offsetResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void cfgB1offsetResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cfgB1offsetResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void clearLteAvailableFileResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method clearLteAvailableFileResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method deactivateNrScgCommunicationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setCarrierAggregationModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCarrierAggregationModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableCAPlusBandWidthFilterResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void enableSCGfailureResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method enableSCGfailureResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void get4x4MimoEnabledResponse(RadioResponseInfo info, int enabled_bitmask) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(enabled_bitmask);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method get4x4MimoEnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getAllBandModeResponse(RadioResponseInfo info, BandModeInfo data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(data, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getAllBandModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getApcInfoResponse(RadioResponseInfo info, int[] cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(cellInfo);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getApcInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getAvailableNetworksWithActResponse(RadioResponseInfo info, OperatorInfoWithAct[] networkInfosWithAct) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(networkInfosWithAct, 0);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getAvailableNetworksWithActResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getBandModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getBandPriorityListResponse(RadioResponseInfo info, int[] bandPriList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(bandPriList);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getBandPriorityListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCALinkCapabilityListResponse(RadioResponseInfo info, String[] linkCapabilityList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(linkCapabilityList);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCALinkCapabilityListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCALinkEnableStatusResponse(RadioResponseInfo info, boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeBoolean(status);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCALinkEnableStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCaBandModeResponse(RadioResponseInfo info, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCaBandModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCampedFemtoCellInfoResponse(RadioResponseInfo info, String[] femto) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeStringArray(femto);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCampedFemtoCellInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, String[] polList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(polList);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getCurrentPOLListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(deactivate);
                    _data.writeInt(allowSCGAdd);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getDeactivateNrScgCommunicationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getDisable2GResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getDisable2GResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getDisable2GResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getFemtocellListResponse(RadioResponseInfo responseInfo, String[] femtoList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(femtoList);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getFemtocellListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getFemtocellListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getIWlanRegistrationStateResponse(RadioResponseInfo responseInfo, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getIWlanRegistrationStateResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getIWlanRegistrationStateResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLte1xRttCellListResponse(RadioResponseInfo info, Lte1xRttCellInfo[] list) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(list, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLte1xRttCellListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLte1xRttCellListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteBsrTimerResponse(RadioResponseInfo info, int timer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(timer);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteBsrTimerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteBsrTimerResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteDataResponse(RadioResponseInfo info, LteData data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(data, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteDataResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteDataResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteRRCStateResponse(RadioResponseInfo info, int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteRRCStateResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteRRCStateResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteReleaseVersionResponse(RadioResponseInfo info, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getLteReleaseVersionResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteReleaseVersionResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getLteScanDurationResponse(RadioResponseInfo info, int duration) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(duration);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getLteScanDurationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, int[] polCapability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeIntArray(polCapability);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getPOLCapabilityResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getPlmnNameFromSE13TableResponse(RadioResponseInfo info, String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getPlmnNameFromSE13TableResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getQamEnabledResponse(RadioResponseInfo info, boolean ulOrDl, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeBoolean(ulOrDl);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getQamEnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getRoamingEnableResponse(RadioResponseInfo responseInfo, int[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeIntArray(data);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getRoamingEnableResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getRoamingEnableResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo info, SignalStrengthWithWcdmaEcio signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(signalStrength, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSignalStrengthWithWcdmaEcioResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getSignalStrengthWithWcdmaEcioResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, String[] plmnList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeStringArray(plmnList);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getSuggestedPlmnListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getSuggestedPlmnListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getTOEInfoResponse(RadioResponseInfo info, String longName, String shortName, String numeric) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(longName);
                    _data.writeString(shortName);
                    _data.writeString(numeric);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getTOEInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getTOEInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void getTm9EnabledResponse(RadioResponseInfo info, boolean fddOrTdd, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeBoolean(fddOrTdd);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getTm9EnabledResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getTm9EnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_queryFemtoCellSystemSelectionModeResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryFemtoCellSystemSelectionModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void selectFemtocellResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method selectFemtocellResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void set4x4MimoEnabledResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_set4x4MimoEnabledResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method set4x4MimoEnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setApcModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setApcModeResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setApcModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setBandPriorityListResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBandPriorityListResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBandPriorityListResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setBgsrchDeltaSleepTimerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBgsrchDeltaSleepTimerResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setCALinkEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setCALinkEnableStatusResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCALinkEnableStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setDisable2GResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setDisable2GResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setFemtoCellSystemSelectionModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteBandEnableStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteBandEnableStatusResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteBandEnableStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteBsrTimerResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteBsrTimerResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteBsrTimerResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteReleaseVersionResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(50, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteReleaseVersionResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setLteScanDurationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLteScanDurationResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLteScanDurationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setNROptionResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNROptionResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNROptionResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNetworkSelectionModeManualWithActResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNetworkSelectionModeManualWithActResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setNrBandModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setNrBandModeResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNrBandModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setPOLEntryResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setPOLEntryResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setPOLEntryResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setQamEnabledResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setQamEnabledResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setQamEnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setSearchRatResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchRatResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSearchRatResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSearchStoredFreqInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSearchStoredFreqInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setServiceStateToModemResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setServiceStateToModemResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setTm9EnabledResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(60, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setTm9EnabledResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
            public void setRoamingEnableResponse(RadioResponseInfo responseInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(responseInfo, 0);
                    boolean _status = this.mRemote.transact(61, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setRoamingEnableResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
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
