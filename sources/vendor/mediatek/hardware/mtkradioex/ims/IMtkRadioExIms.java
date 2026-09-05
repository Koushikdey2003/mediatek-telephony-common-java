package vendor.mediatek.hardware.mtkradioex.ims;

import android.hardware.radio.messaging.CdmaSmsAck;
import android.hardware.radio.messaging.ImsSmsMessage;
import android.hardware.radio.voice.Dial;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication;
import vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse;
import vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioIndication;
import vendor.mediatek.hardware.mtkradioex.mwi.IMwiRadioResponse;
import vendor.mediatek.hardware.mtkradioex.voice.ConferenceDial;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExIms extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$ims$IMtkRadioExIms".replace('$', '.');
    public static final String HASH = "e52459812a302acdde2f0131532472cd88eaad52";
    public static final int VERSION = 1;

    void acknowledgeLastIncomingCdmaSmsEx(int i, CdmaSmsAck cdmaSmsAck, int i2) throws RemoteException;

    void acknowledgeLastIncomingGsmSmsEx(int i, boolean z, int i2, int i3) throws RemoteException;

    void cancelUssi(int i, int i2) throws RemoteException;

    void conferenceDial(int i, ConferenceDial conferenceDial, int i2) throws RemoteException;

    void controlCall(int i, int i2, int i3, int i4) throws RemoteException;

    void controlImsConferenceCallMember(int i, int i2, int i3, String str, int i4, int i5) throws RemoteException;

    void dialWithSipUri(int i, String str, int i2) throws RemoteException;

    void eccRedialApprove(int i, int i2, int i3, int i4) throws RemoteException;

    void forceReleaseCall(int i, int i2, int i3) throws RemoteException;

    void getBarringCalls(int i, int i2, int i3) throws RemoteException;

    void getImsCfgFeatureValue(int i, int i2, int i3, int i4) throws RemoteException;

    void getImsCfgProvisionValue(int i, int i2, int i3) throws RemoteException;

    void getImsCfgResourceCapValue(int i, int i2, int i3) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getVoiceDomainPreference(int i, int i2) throws RemoteException;

    void getWfcConfig(int i, int i2, int i3) throws RemoteException;

    void getXcapStatus(int i, int i2) throws RemoteException;

    void imsBearerStateConfirm(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void imsEctCommand(int i, String str, int i2, int i3) throws RemoteException;

    void notifyEPDGScreenState(int i, int i2, int i3) throws RemoteException;

    void pullCall(int i, String str, boolean z, int i2) throws RemoteException;

    void querySsacStatus(int i, int i2) throws RemoteException;

    void queryVopsStatus(int i, int i2) throws RemoteException;

    void responseAcknowledgementMtk() throws RemoteException;

    void rttModifyRequestResponse(int i, int i2, int i3, int i4) throws RemoteException;

    void sendImsSmsEx(int i, ImsSmsMessage imsSmsMessage, int i2) throws RemoteException;

    void sendRttModifyRequest(int i, int i2, int i3, int i4) throws RemoteException;

    void sendRttText(int i, int i2, int i3, String str, int i4) throws RemoteException;

    void sendUssi(int i, String str, int i2) throws RemoteException;

    void setBarringCalls(int i, ImsBarringCall[] imsBarringCallArr, int i2) throws RemoteException;

    void setCallAdditionalInfo(int i, String[] strArr, int i2) throws RemoteException;

    void setEmergencyAddressId(int i, String str, int i2) throws RemoteException;

    void setImsBearerNotification(int i, int i2, int i3) throws RemoteException;

    void setImsCallMode(int i, int i2, int i3) throws RemoteException;

    void setImsCfgFeatureValue(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void setImsCfgProvisionValue(int i, int i2, String str, int i3) throws RemoteException;

    void setImsRegistrationReport(int i, int i2) throws RemoteException;

    void setImscfg(int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i2) throws RemoteException;

    void setLocationInfo(int i, String[] strArr, int i2) throws RemoteException;

    void setModemImsCfg(int i, String str, String str2, int i2, int i3) throws RemoteException;

    void setNattKeepAliveStatus(int i, String str, boolean z, String str2, int i2, String str3, int i3, int i4) throws RemoteException;

    void setResponseFunctionsMtk(IMtkRadioExImsResponse iMtkRadioExImsResponse, IMtkRadioExImsIndication iMtkRadioExImsIndication) throws RemoteException;

    void setResponseFunctionsMwi(IMwiRadioResponse iMwiRadioResponse, IMwiRadioIndication iMwiRadioIndication) throws RemoteException;

    void setRttMode(int i, int i2, int i3) throws RemoteException;

    void setSipHeader(int i, String[] strArr, int i2) throws RemoteException;

    void setSipHeaderReport(int i, String[] strArr, int i2) throws RemoteException;

    void setVoiceDomainPreference(int i, int i2, int i3) throws RemoteException;

    void setWfcConfig(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void setWfcProfile(int i, int i2, int i3) throws RemoteException;

    void setWifiAssociated(int i, String[] strArr, int i2) throws RemoteException;

    void setWifiEnabled(int i, String str, int i2, int i3, int i4) throws RemoteException;

    void setWifiIpAddress(int i, String[] strArr, int i2) throws RemoteException;

    void setWifiPingResult(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setWifiSignalLevel(int i, int i2, int i3, int i4) throws RemoteException;

    void setupXcapUserAgentString(int i, String str, int i2) throws RemoteException;

    void toggleRttAudioIndication(int i, int i2, int i3, int i4) throws RemoteException;

    void videoCallAccept(int i, int i2, int i3, int i4) throws RemoteException;

    void videoRingtoneEventRequest(int i, String[] strArr, int i2) throws RemoteException;

    void vtDial(int i, Dial dial, int i2) throws RemoteException;

    void vtDialWithSipUri(int i, String str, int i2) throws RemoteException;

    public static class Default implements IMtkRadioExIms {
        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void imsBearerStateConfirm(int serial, int aid, int action, int status, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsBearerNotification(int serial, int enable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void acknowledgeLastIncomingCdmaSmsEx(int serial, CdmaSmsAck smsAck, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void acknowledgeLastIncomingGsmSmsEx(int serial, boolean success, int cause, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendImsSmsEx(int serial, ImsSmsMessage message, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void rttModifyRequestResponse(int serial, int callId, int result, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendRttModifyRequest(int serial, int callId, int newMode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendRttText(int serial, int callId, int lenOfString, String text, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setRttMode(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void toggleRttAudioIndication(int serial, int callId, int audio, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void queryVopsStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsRegistrationReport(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void cancelUssi(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getXcapStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void sendUssi(int serial, String ussiString, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setupXcapUserAgentString(int serial, String userAgent, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getBarringCalls(int serial, int serviceClass, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setBarringCalls(int serial, ImsBarringCall[] calls, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void videoCallAccept(int serial, int videoMode, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void videoRingtoneEventRequest(int serial, String[] event, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void vtDial(int serial, Dial dialInfo, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void vtDialWithSipUri(int serial, String address, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void conferenceDial(int serial, ConferenceDial dailInfo, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void controlCall(int serial, int controlType, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void controlImsConferenceCallMember(int serial, int controlType, int confCallId, String address, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void dialWithSipUri(int serial, String address, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void eccRedialApprove(int serial, int approve, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void forceReleaseCall(int serial, int callId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void imsEctCommand(int serial, String number, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void pullCall(int serial, String target, boolean isVideoCall, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setCallAdditionalInfo(int serial, String[] info, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsCallMode(int serial, int mode, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setSipHeader(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setSipHeaderReport(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getImsCfgFeatureValue(int serial, int featureId, int network, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getImsCfgProvisionValue(int serial, int configId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getImsCfgResourceCapValue(int serial, int featureId, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getVoiceDomainPreference(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsCfgFeatureValue(int serial, int featureId, int network, int value, int isLast, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImsCfgProvisionValue(int serial, int configId, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setImscfg(int serial, boolean volteEnable, boolean vilteEnable, boolean vowifiEnable, boolean viwifiEnable, boolean smsEnable, boolean eimsEnable, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setVoiceDomainPreference(int serial, int vdp, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWfcProfile(int serial, int wfcPreference, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setModemImsCfg(int serial, String keys, String values, int type, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void querySsacStatus(int serial, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void getWfcConfig(int serial, int setting, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void notifyEPDGScreenState(int serial, int state, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setEmergencyAddressId(int serial, String aid, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setLocationInfo(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setNattKeepAliveStatus(int serial, String ifName, boolean enable, String srcIp, int srcPort, String dstIp, int dstPort, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWfcConfig(int serial, int setting, String ifName, String value, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiAssociated(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiEnabled(int serial, String ifName, int isWifiEnabled, int isFlightModeOn, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiIpAddress(int serial, String[] data, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiPingResult(int serial, int rat, int latency, int pktloss, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setWifiSignalLevel(int serial, int rssi, int snr, int clientId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void responseAcknowledgementMtk() throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setResponseFunctionsMtk(IMtkRadioExImsResponse radioResponse, IMtkRadioExImsIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public void setResponseFunctionsMwi(IMwiRadioResponse radioResponse, IMwiRadioIndication radioIndication) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExIms {
        static final int TRANSACTION_acknowledgeLastIncomingCdmaSmsEx = 3;
        static final int TRANSACTION_acknowledgeLastIncomingGsmSmsEx = 4;
        static final int TRANSACTION_cancelUssi = 13;
        static final int TRANSACTION_conferenceDial = 23;
        static final int TRANSACTION_controlCall = 24;
        static final int TRANSACTION_controlImsConferenceCallMember = 25;
        static final int TRANSACTION_dialWithSipUri = 26;
        static final int TRANSACTION_eccRedialApprove = 27;
        static final int TRANSACTION_forceReleaseCall = 28;
        static final int TRANSACTION_getBarringCalls = 17;
        static final int TRANSACTION_getImsCfgFeatureValue = 35;
        static final int TRANSACTION_getImsCfgProvisionValue = 36;
        static final int TRANSACTION_getImsCfgResourceCapValue = 37;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getVoiceDomainPreference = 38;
        static final int TRANSACTION_getWfcConfig = 46;
        static final int TRANSACTION_getXcapStatus = 14;
        static final int TRANSACTION_imsBearerStateConfirm = 1;
        static final int TRANSACTION_imsEctCommand = 29;
        static final int TRANSACTION_notifyEPDGScreenState = 47;
        static final int TRANSACTION_pullCall = 30;
        static final int TRANSACTION_querySsacStatus = 45;
        static final int TRANSACTION_queryVopsStatus = 11;
        static final int TRANSACTION_responseAcknowledgementMtk = 57;
        static final int TRANSACTION_rttModifyRequestResponse = 6;
        static final int TRANSACTION_sendImsSmsEx = 5;
        static final int TRANSACTION_sendRttModifyRequest = 7;
        static final int TRANSACTION_sendRttText = 8;
        static final int TRANSACTION_sendUssi = 15;
        static final int TRANSACTION_setBarringCalls = 18;
        static final int TRANSACTION_setCallAdditionalInfo = 31;
        static final int TRANSACTION_setEmergencyAddressId = 48;
        static final int TRANSACTION_setImsBearerNotification = 2;
        static final int TRANSACTION_setImsCallMode = 32;
        static final int TRANSACTION_setImsCfgFeatureValue = 39;
        static final int TRANSACTION_setImsCfgProvisionValue = 40;
        static final int TRANSACTION_setImsRegistrationReport = 12;
        static final int TRANSACTION_setImscfg = 41;
        static final int TRANSACTION_setLocationInfo = 49;
        static final int TRANSACTION_setModemImsCfg = 44;
        static final int TRANSACTION_setNattKeepAliveStatus = 50;
        static final int TRANSACTION_setResponseFunctionsMtk = 58;
        static final int TRANSACTION_setResponseFunctionsMwi = 59;
        static final int TRANSACTION_setRttMode = 9;
        static final int TRANSACTION_setSipHeader = 33;
        static final int TRANSACTION_setSipHeaderReport = 34;
        static final int TRANSACTION_setVoiceDomainPreference = 42;
        static final int TRANSACTION_setWfcConfig = 51;
        static final int TRANSACTION_setWfcProfile = 43;
        static final int TRANSACTION_setWifiAssociated = 52;
        static final int TRANSACTION_setWifiEnabled = 53;
        static final int TRANSACTION_setWifiIpAddress = 54;
        static final int TRANSACTION_setWifiPingResult = 55;
        static final int TRANSACTION_setWifiSignalLevel = 56;
        static final int TRANSACTION_setupXcapUserAgentString = 16;
        static final int TRANSACTION_toggleRttAudioIndication = 10;
        static final int TRANSACTION_videoCallAccept = 19;
        static final int TRANSACTION_videoRingtoneEventRequest = 20;
        static final int TRANSACTION_vtDial = 21;
        static final int TRANSACTION_vtDialWithSipUri = 22;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExIms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExIms)) {
                return (IMtkRadioExIms) iin;
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
                            int _arg3 = data.readInt();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            imsBearerStateConfirm(_arg0, _arg1, _arg2, _arg3, _arg4);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            setImsBearerNotification(_arg02, _arg12, _arg22);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            CdmaSmsAck _arg13 = (CdmaSmsAck) data.readTypedObject(CdmaSmsAck.CREATOR);
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            acknowledgeLastIncomingCdmaSmsEx(_arg03, _arg13, _arg23);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            boolean _arg14 = data.readBoolean();
                            int _arg24 = data.readInt();
                            int _arg32 = data.readInt();
                            data.enforceNoDataAvail();
                            acknowledgeLastIncomingGsmSmsEx(_arg04, _arg14, _arg24, _arg32);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            ImsSmsMessage _arg15 = (ImsSmsMessage) data.readTypedObject(ImsSmsMessage.CREATOR);
                            int _arg25 = data.readInt();
                            data.enforceNoDataAvail();
                            sendImsSmsEx(_arg05, _arg15, _arg25);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg16 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg33 = data.readInt();
                            data.enforceNoDataAvail();
                            rttModifyRequestResponse(_arg06, _arg16, _arg26, _arg33);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg17 = data.readInt();
                            int _arg27 = data.readInt();
                            int _arg34 = data.readInt();
                            data.enforceNoDataAvail();
                            sendRttModifyRequest(_arg07, _arg17, _arg27, _arg34);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg18 = data.readInt();
                            int _arg28 = data.readInt();
                            String _arg35 = data.readString();
                            int _arg42 = data.readInt();
                            data.enforceNoDataAvail();
                            sendRttText(_arg08, _arg18, _arg28, _arg35, _arg42);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            int _arg19 = data.readInt();
                            int _arg29 = data.readInt();
                            data.enforceNoDataAvail();
                            setRttMode(_arg09, _arg19, _arg29);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            int _arg110 = data.readInt();
                            int _arg210 = data.readInt();
                            int _arg36 = data.readInt();
                            data.enforceNoDataAvail();
                            toggleRttAudioIndication(_arg010, _arg110, _arg210, _arg36);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int _arg111 = data.readInt();
                            data.enforceNoDataAvail();
                            queryVopsStatus(_arg011, _arg111);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg112 = data.readInt();
                            data.enforceNoDataAvail();
                            setImsRegistrationReport(_arg012, _arg112);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            int _arg113 = data.readInt();
                            data.enforceNoDataAvail();
                            cancelUssi(_arg013, _arg113);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            getXcapStatus(_arg014, _arg114);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            String _arg115 = data.readString();
                            int _arg211 = data.readInt();
                            data.enforceNoDataAvail();
                            sendUssi(_arg015, _arg115, _arg211);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            String _arg116 = data.readString();
                            int _arg212 = data.readInt();
                            data.enforceNoDataAvail();
                            setupXcapUserAgentString(_arg016, _arg116, _arg212);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            int _arg117 = data.readInt();
                            int _arg213 = data.readInt();
                            data.enforceNoDataAvail();
                            getBarringCalls(_arg017, _arg117, _arg213);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            ImsBarringCall[] _arg118 = (ImsBarringCall[]) data.createTypedArray(ImsBarringCall.CREATOR);
                            int _arg214 = data.readInt();
                            data.enforceNoDataAvail();
                            setBarringCalls(_arg018, _arg118, _arg214);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            int _arg119 = data.readInt();
                            int _arg215 = data.readInt();
                            int _arg37 = data.readInt();
                            data.enforceNoDataAvail();
                            videoCallAccept(_arg019, _arg119, _arg215, _arg37);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            String[] _arg120 = data.createStringArray();
                            int _arg216 = data.readInt();
                            data.enforceNoDataAvail();
                            videoRingtoneEventRequest(_arg020, _arg120, _arg216);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            Dial _arg121 = (Dial) data.readTypedObject(Dial.CREATOR);
                            int _arg217 = data.readInt();
                            data.enforceNoDataAvail();
                            vtDial(_arg021, _arg121, _arg217);
                            return true;
                        case TRANSACTION_vtDialWithSipUri /* 22 */:
                            int _arg022 = data.readInt();
                            String _arg122 = data.readString();
                            int _arg218 = data.readInt();
                            data.enforceNoDataAvail();
                            vtDialWithSipUri(_arg022, _arg122, _arg218);
                            return true;
                        case TRANSACTION_conferenceDial /* 23 */:
                            int _arg023 = data.readInt();
                            ConferenceDial _arg123 = (ConferenceDial) data.readTypedObject(ConferenceDial.CREATOR);
                            int _arg219 = data.readInt();
                            data.enforceNoDataAvail();
                            conferenceDial(_arg023, _arg123, _arg219);
                            return true;
                        case TRANSACTION_controlCall /* 24 */:
                            int _arg024 = data.readInt();
                            int _arg124 = data.readInt();
                            int _arg220 = data.readInt();
                            int _arg38 = data.readInt();
                            data.enforceNoDataAvail();
                            controlCall(_arg024, _arg124, _arg220, _arg38);
                            return true;
                        case TRANSACTION_controlImsConferenceCallMember /* 25 */:
                            int _arg025 = data.readInt();
                            int _arg125 = data.readInt();
                            int _arg221 = data.readInt();
                            String _arg39 = data.readString();
                            int _arg43 = data.readInt();
                            int _arg5 = data.readInt();
                            data.enforceNoDataAvail();
                            controlImsConferenceCallMember(_arg025, _arg125, _arg221, _arg39, _arg43, _arg5);
                            return true;
                        case TRANSACTION_dialWithSipUri /* 26 */:
                            int _arg026 = data.readInt();
                            String _arg126 = data.readString();
                            int _arg222 = data.readInt();
                            data.enforceNoDataAvail();
                            dialWithSipUri(_arg026, _arg126, _arg222);
                            return true;
                        case TRANSACTION_eccRedialApprove /* 27 */:
                            int _arg027 = data.readInt();
                            int _arg127 = data.readInt();
                            int _arg223 = data.readInt();
                            int _arg310 = data.readInt();
                            data.enforceNoDataAvail();
                            eccRedialApprove(_arg027, _arg127, _arg223, _arg310);
                            return true;
                        case TRANSACTION_forceReleaseCall /* 28 */:
                            int _arg028 = data.readInt();
                            int _arg128 = data.readInt();
                            int _arg224 = data.readInt();
                            data.enforceNoDataAvail();
                            forceReleaseCall(_arg028, _arg128, _arg224);
                            return true;
                        case TRANSACTION_imsEctCommand /* 29 */:
                            int _arg029 = data.readInt();
                            String _arg129 = data.readString();
                            int _arg225 = data.readInt();
                            int _arg311 = data.readInt();
                            data.enforceNoDataAvail();
                            imsEctCommand(_arg029, _arg129, _arg225, _arg311);
                            return true;
                        case 30:
                            int _arg030 = data.readInt();
                            String _arg130 = data.readString();
                            boolean _arg226 = data.readBoolean();
                            int _arg312 = data.readInt();
                            data.enforceNoDataAvail();
                            pullCall(_arg030, _arg130, _arg226, _arg312);
                            return true;
                        case 31:
                            int _arg031 = data.readInt();
                            String[] _arg131 = data.createStringArray();
                            int _arg227 = data.readInt();
                            data.enforceNoDataAvail();
                            setCallAdditionalInfo(_arg031, _arg131, _arg227);
                            return true;
                        case 32:
                            int _arg032 = data.readInt();
                            int _arg132 = data.readInt();
                            int _arg228 = data.readInt();
                            data.enforceNoDataAvail();
                            setImsCallMode(_arg032, _arg132, _arg228);
                            return true;
                        case 33:
                            int _arg033 = data.readInt();
                            String[] _arg133 = data.createStringArray();
                            int _arg229 = data.readInt();
                            data.enforceNoDataAvail();
                            setSipHeader(_arg033, _arg133, _arg229);
                            return true;
                        case TRANSACTION_setSipHeaderReport /* 34 */:
                            int _arg034 = data.readInt();
                            String[] _arg134 = data.createStringArray();
                            int _arg230 = data.readInt();
                            data.enforceNoDataAvail();
                            setSipHeaderReport(_arg034, _arg134, _arg230);
                            return true;
                        case TRANSACTION_getImsCfgFeatureValue /* 35 */:
                            int _arg035 = data.readInt();
                            int _arg135 = data.readInt();
                            int _arg231 = data.readInt();
                            int _arg313 = data.readInt();
                            data.enforceNoDataAvail();
                            getImsCfgFeatureValue(_arg035, _arg135, _arg231, _arg313);
                            return true;
                        case TRANSACTION_getImsCfgProvisionValue /* 36 */:
                            int _arg036 = data.readInt();
                            int _arg136 = data.readInt();
                            int _arg232 = data.readInt();
                            data.enforceNoDataAvail();
                            getImsCfgProvisionValue(_arg036, _arg136, _arg232);
                            return true;
                        case TRANSACTION_getImsCfgResourceCapValue /* 37 */:
                            int _arg037 = data.readInt();
                            int _arg137 = data.readInt();
                            int _arg233 = data.readInt();
                            data.enforceNoDataAvail();
                            getImsCfgResourceCapValue(_arg037, _arg137, _arg233);
                            return true;
                        case TRANSACTION_getVoiceDomainPreference /* 38 */:
                            int _arg038 = data.readInt();
                            int _arg138 = data.readInt();
                            data.enforceNoDataAvail();
                            getVoiceDomainPreference(_arg038, _arg138);
                            return true;
                        case TRANSACTION_setImsCfgFeatureValue /* 39 */:
                            int _arg039 = data.readInt();
                            int _arg139 = data.readInt();
                            int _arg234 = data.readInt();
                            int _arg314 = data.readInt();
                            int _arg44 = data.readInt();
                            int _arg52 = data.readInt();
                            data.enforceNoDataAvail();
                            setImsCfgFeatureValue(_arg039, _arg139, _arg234, _arg314, _arg44, _arg52);
                            return true;
                        case 40:
                            int _arg040 = data.readInt();
                            int _arg140 = data.readInt();
                            String _arg235 = data.readString();
                            int _arg315 = data.readInt();
                            data.enforceNoDataAvail();
                            setImsCfgProvisionValue(_arg040, _arg140, _arg235, _arg315);
                            return true;
                        case TRANSACTION_setImscfg /* 41 */:
                            int _arg041 = data.readInt();
                            boolean _arg141 = data.readBoolean();
                            boolean _arg236 = data.readBoolean();
                            boolean _arg316 = data.readBoolean();
                            boolean _arg45 = data.readBoolean();
                            boolean _arg53 = data.readBoolean();
                            boolean _arg6 = data.readBoolean();
                            int _arg7 = data.readInt();
                            data.enforceNoDataAvail();
                            setImscfg(_arg041, _arg141, _arg236, _arg316, _arg45, _arg53, _arg6, _arg7);
                            return true;
                        case TRANSACTION_setVoiceDomainPreference /* 42 */:
                            int _arg042 = data.readInt();
                            int _arg142 = data.readInt();
                            int _arg237 = data.readInt();
                            data.enforceNoDataAvail();
                            setVoiceDomainPreference(_arg042, _arg142, _arg237);
                            return true;
                        case TRANSACTION_setWfcProfile /* 43 */:
                            int _arg043 = data.readInt();
                            int _arg143 = data.readInt();
                            int _arg238 = data.readInt();
                            data.enforceNoDataAvail();
                            setWfcProfile(_arg043, _arg143, _arg238);
                            return true;
                        case TRANSACTION_setModemImsCfg /* 44 */:
                            int _arg044 = data.readInt();
                            String _arg144 = data.readString();
                            String _arg239 = data.readString();
                            int _arg317 = data.readInt();
                            int _arg46 = data.readInt();
                            data.enforceNoDataAvail();
                            setModemImsCfg(_arg044, _arg144, _arg239, _arg317, _arg46);
                            return true;
                        case TRANSACTION_querySsacStatus /* 45 */:
                            int _arg045 = data.readInt();
                            int _arg145 = data.readInt();
                            data.enforceNoDataAvail();
                            querySsacStatus(_arg045, _arg145);
                            return true;
                        case 46:
                            int _arg046 = data.readInt();
                            int _arg146 = data.readInt();
                            int _arg240 = data.readInt();
                            data.enforceNoDataAvail();
                            getWfcConfig(_arg046, _arg146, _arg240);
                            return true;
                        case 47:
                            int _arg047 = data.readInt();
                            int _arg147 = data.readInt();
                            int _arg241 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyEPDGScreenState(_arg047, _arg147, _arg241);
                            return true;
                        case TRANSACTION_setEmergencyAddressId /* 48 */:
                            int _arg048 = data.readInt();
                            String _arg148 = data.readString();
                            int _arg242 = data.readInt();
                            data.enforceNoDataAvail();
                            setEmergencyAddressId(_arg048, _arg148, _arg242);
                            return true;
                        case TRANSACTION_setLocationInfo /* 49 */:
                            int _arg049 = data.readInt();
                            String[] _arg149 = data.createStringArray();
                            int _arg243 = data.readInt();
                            data.enforceNoDataAvail();
                            setLocationInfo(_arg049, _arg149, _arg243);
                            return true;
                        case 50:
                            int _arg050 = data.readInt();
                            String _arg150 = data.readString();
                            boolean _arg244 = data.readBoolean();
                            String _arg318 = data.readString();
                            int _arg47 = data.readInt();
                            String _arg54 = data.readString();
                            int _arg62 = data.readInt();
                            int _arg72 = data.readInt();
                            data.enforceNoDataAvail();
                            setNattKeepAliveStatus(_arg050, _arg150, _arg244, _arg318, _arg47, _arg54, _arg62, _arg72);
                            return true;
                        case TRANSACTION_setWfcConfig /* 51 */:
                            int _arg051 = data.readInt();
                            int _arg151 = data.readInt();
                            String _arg245 = data.readString();
                            String _arg319 = data.readString();
                            int _arg48 = data.readInt();
                            data.enforceNoDataAvail();
                            setWfcConfig(_arg051, _arg151, _arg245, _arg319, _arg48);
                            return true;
                        case TRANSACTION_setWifiAssociated /* 52 */:
                            int _arg052 = data.readInt();
                            String[] _arg152 = data.createStringArray();
                            int _arg246 = data.readInt();
                            data.enforceNoDataAvail();
                            setWifiAssociated(_arg052, _arg152, _arg246);
                            return true;
                        case TRANSACTION_setWifiEnabled /* 53 */:
                            int _arg053 = data.readInt();
                            String _arg153 = data.readString();
                            int _arg247 = data.readInt();
                            int _arg320 = data.readInt();
                            int _arg49 = data.readInt();
                            data.enforceNoDataAvail();
                            setWifiEnabled(_arg053, _arg153, _arg247, _arg320, _arg49);
                            return true;
                        case TRANSACTION_setWifiIpAddress /* 54 */:
                            int _arg054 = data.readInt();
                            String[] _arg154 = data.createStringArray();
                            int _arg248 = data.readInt();
                            data.enforceNoDataAvail();
                            setWifiIpAddress(_arg054, _arg154, _arg248);
                            return true;
                        case TRANSACTION_setWifiPingResult /* 55 */:
                            int _arg055 = data.readInt();
                            int _arg155 = data.readInt();
                            int _arg249 = data.readInt();
                            int _arg321 = data.readInt();
                            int _arg410 = data.readInt();
                            data.enforceNoDataAvail();
                            setWifiPingResult(_arg055, _arg155, _arg249, _arg321, _arg410);
                            return true;
                        case TRANSACTION_setWifiSignalLevel /* 56 */:
                            int _arg056 = data.readInt();
                            int _arg156 = data.readInt();
                            int _arg250 = data.readInt();
                            int _arg322 = data.readInt();
                            data.enforceNoDataAvail();
                            setWifiSignalLevel(_arg056, _arg156, _arg250, _arg322);
                            return true;
                        case TRANSACTION_responseAcknowledgementMtk /* 57 */:
                            responseAcknowledgementMtk();
                            return true;
                        case TRANSACTION_setResponseFunctionsMtk /* 58 */:
                            IMtkRadioExImsResponse _arg057 = IMtkRadioExImsResponse.Stub.asInterface(data.readStrongBinder());
                            IMtkRadioExImsIndication _arg157 = IMtkRadioExImsIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMtk(_arg057, _arg157);
                            return true;
                        case TRANSACTION_setResponseFunctionsMwi /* 59 */:
                            IMwiRadioResponse _arg058 = IMwiRadioResponse.Stub.asInterface(data.readStrongBinder());
                            IMwiRadioIndication _arg158 = IMwiRadioIndication.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            setResponseFunctionsMwi(_arg058, _arg158);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExIms {
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void imsBearerStateConfirm(int serial, int aid, int action, int status, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(aid);
                    _data.writeInt(action);
                    _data.writeInt(status);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsBearerStateConfirm is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsBearerNotification(int serial, int enable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(enable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsBearerNotification is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void acknowledgeLastIncomingCdmaSmsEx(int serial, CdmaSmsAck smsAck, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(smsAck, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method acknowledgeLastIncomingCdmaSmsEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void acknowledgeLastIncomingGsmSmsEx(int serial, boolean success, int cause, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(success);
                    _data.writeInt(cause);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method acknowledgeLastIncomingGsmSmsEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendImsSmsEx(int serial, ImsSmsMessage message, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(message, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendImsSmsEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void rttModifyRequestResponse(int serial, int callId, int result, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(result);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method rttModifyRequestResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendRttModifyRequest(int serial, int callId, int newMode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(newMode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRttModifyRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendRttText(int serial, int callId, int lenOfString, String text, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(lenOfString);
                    _data.writeString(text);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRttText is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setRttMode(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setRttMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void toggleRttAudioIndication(int serial, int callId, int audio, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(audio);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method toggleRttAudioIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void queryVopsStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryVopsStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsRegistrationReport(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsRegistrationReport is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void cancelUssi(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cancelUssi is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getXcapStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getXcapStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void sendUssi(int serial, String ussiString, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ussiString);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendUssi is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setupXcapUserAgentString(int serial, String userAgent, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(userAgent);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setupXcapUserAgentString is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getBarringCalls(int serial, int serviceClass, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(serviceClass);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getBarringCalls is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setBarringCalls(int serial, ImsBarringCall[] calls, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedArray(calls, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBarringCalls is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void videoCallAccept(int serial, int videoMode, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(videoMode);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method videoCallAccept is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void videoRingtoneEventRequest(int serial, String[] event, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(event);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method videoRingtoneEventRequest is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void vtDial(int serial, Dial dialInfo, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(dialInfo, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method vtDial is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void vtDialWithSipUri(int serial, String address, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(address);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_vtDialWithSipUri, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method vtDialWithSipUri is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void conferenceDial(int serial, ConferenceDial dailInfo, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeTypedObject(dailInfo, 0);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_conferenceDial, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method conferenceDial is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void controlCall(int serial, int controlType, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(controlType);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_controlCall, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method controlCall is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void controlImsConferenceCallMember(int serial, int controlType, int confCallId, String address, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(controlType);
                    _data.writeInt(confCallId);
                    _data.writeString(address);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_controlImsConferenceCallMember, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method controlImsConferenceCallMember is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void dialWithSipUri(int serial, String address, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(address);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_dialWithSipUri, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dialWithSipUri is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void eccRedialApprove(int serial, int approve, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(approve);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_eccRedialApprove, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method eccRedialApprove is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void forceReleaseCall(int serial, int callId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(callId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_forceReleaseCall, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method forceReleaseCall is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void imsEctCommand(int serial, String number, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(number);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsEctCommand, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsEctCommand is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void pullCall(int serial, String target, boolean isVideoCall, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(target);
                    _data.writeBoolean(isVideoCall);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method pullCall is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setCallAdditionalInfo(int serial, String[] info, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(info);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCallAdditionalInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsCallMode(int serial, int mode, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(mode);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsCallMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setSipHeader(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSipHeader is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setSipHeaderReport(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setSipHeaderReport, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSipHeaderReport is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getImsCfgFeatureValue(int serial, int featureId, int network, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(featureId);
                    _data.writeInt(network);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgFeatureValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getImsCfgFeatureValue is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getImsCfgProvisionValue(int serial, int configId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(configId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgProvisionValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getImsCfgProvisionValue is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getImsCfgResourceCapValue(int serial, int featureId, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(featureId);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgResourceCapValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getImsCfgResourceCapValue is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getVoiceDomainPreference(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getVoiceDomainPreference, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getVoiceDomainPreference is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsCfgFeatureValue(int serial, int featureId, int network, int value, int isLast, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(featureId);
                    _data.writeInt(network);
                    _data.writeInt(value);
                    _data.writeInt(isLast);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImsCfgFeatureValue, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsCfgFeatureValue is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImsCfgProvisionValue(int serial, int configId, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(configId);
                    _data.writeString(value);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsCfgProvisionValue is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setImscfg(int serial, boolean volteEnable, boolean vilteEnable, boolean vowifiEnable, boolean viwifiEnable, boolean smsEnable, boolean eimsEnable, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeBoolean(volteEnable);
                    _data.writeBoolean(vilteEnable);
                    _data.writeBoolean(vowifiEnable);
                    _data.writeBoolean(viwifiEnable);
                    _data.writeBoolean(smsEnable);
                    _data.writeBoolean(eimsEnable);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImscfg, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImscfg is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setVoiceDomainPreference(int serial, int vdp, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(vdp);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setVoiceDomainPreference, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setVoiceDomainPreference is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWfcProfile(int serial, int wfcPreference, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(wfcPreference);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWfcProfile, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWfcProfile is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setModemImsCfg(int serial, String keys, String values, int type, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(keys);
                    _data.writeString(values);
                    _data.writeInt(type);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setModemImsCfg, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setModemImsCfg is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void querySsacStatus(int serial, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_querySsacStatus, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method querySsacStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void getWfcConfig(int serial, int setting, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(setting);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getWfcConfig is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void notifyEPDGScreenState(int serial, int state, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(state);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method notifyEPDGScreenState is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setEmergencyAddressId(int serial, String aid, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(aid);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setEmergencyAddressId, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setEmergencyAddressId is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setLocationInfo(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setLocationInfo, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setLocationInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setNattKeepAliveStatus(int serial, String ifName, boolean enable, String srcIp, int srcPort, String dstIp, int dstPort, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeBoolean(enable);
                    _data.writeString(srcIp);
                    _data.writeInt(srcPort);
                    _data.writeString(dstIp);
                    _data.writeInt(dstPort);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(50, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setNattKeepAliveStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWfcConfig(int serial, int setting, String ifName, String value, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(setting);
                    _data.writeString(ifName);
                    _data.writeString(value);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWfcConfig, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWfcConfig is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiAssociated(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiAssociated, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiAssociated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiEnabled(int serial, String ifName, int isWifiEnabled, int isFlightModeOn, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeString(ifName);
                    _data.writeInt(isWifiEnabled);
                    _data.writeInt(isFlightModeOn);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiEnabled, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiEnabled is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiIpAddress(int serial, String[] data, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeStringArray(data);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiIpAddress, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiIpAddress is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiPingResult(int serial, int rat, int latency, int pktloss, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rat);
                    _data.writeInt(latency);
                    _data.writeInt(pktloss);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiPingResult, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiPingResult is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setWifiSignalLevel(int serial, int rssi, int snr, int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(serial);
                    _data.writeInt(rssi);
                    _data.writeInt(snr);
                    _data.writeInt(clientId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWifiSignalLevel, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWifiSignalLevel is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setResponseFunctionsMtk(IMtkRadioExImsResponse radioResponse, IMtkRadioExImsIndication radioIndication) throws RemoteException {
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
            public void setResponseFunctionsMwi(IMwiRadioResponse radioResponse, IMwiRadioIndication radioIndication) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(radioResponse);
                    _data.writeStrongInterface(radioIndication);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setResponseFunctionsMwi, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setResponseFunctionsMwi is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms
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
