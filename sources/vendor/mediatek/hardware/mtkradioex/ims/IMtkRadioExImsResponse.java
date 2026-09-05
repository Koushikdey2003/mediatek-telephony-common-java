package vendor.mediatek.hardware.mtkradioex.ims;

import android.hardware.radio.RadioResponseInfo;
import android.hardware.radio.messaging.SendSmsResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExImsResponse extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$ims$IMtkRadioExImsResponse".replace('$', '.');
    public static final String HASH = "e52459812a302acdde2f0131532472cd88eaad52";
    public static final int VERSION = 1;

    void acknowledgeLastIncomingCdmaSmsExResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void acknowledgeLastIncomingGsmSmsExResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void cancelUssiResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void conferenceDialResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void controlCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void controlImsConferenceCallMemberResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void dialWithSipUriResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void eccRedialApproveResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void forceReleaseCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void getBarringCallsResponse(RadioResponseInfo radioResponseInfo, ImsBarringCall[] imsBarringCallArr) throws RemoteException;

    void getImsCfgFeatureValueResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getImsCfgProvisionValueResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void getImsCfgResourceCapValueResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void getVoiceDomainPreferenceResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void getXcapStatusResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void imsBearerStateConfirmResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void imsEctCommandResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void pullCallResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void querySsacStatusResponse(RadioResponseInfo radioResponseInfo, int[] iArr) throws RemoteException;

    void queryVopsStatusResponse(RadioResponseInfo radioResponseInfo, int i) throws RemoteException;

    void rttModifyRequestResponseResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendImsSmsExResponse(RadioResponseInfo radioResponseInfo, SendSmsResult sendSmsResult) throws RemoteException;

    void sendRttModifyRequestResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendRttTextResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void sendUssiResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setBarringCallsResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setCallAdditionalInfoResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsBearerNotificationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsCallModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsCfgFeatureValueResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsCfgProvisionValueResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImsRegistrationReportResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setImscfgResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setModemImsCfgResponse(RadioResponseInfo radioResponseInfo, String str) throws RemoteException;

    void setRttModeResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSipHeaderReportResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setSipHeaderResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setVoiceDomainPreferenceResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setWfcProfileResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void setupXcapUserAgentStringResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void toggleRttAudioIndicationResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void videoCallAcceptResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void videoRingtoneEventResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void vtDialResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    void vtDialWithSipUriResponse(RadioResponseInfo radioResponseInfo) throws RemoteException;

    public static class Default implements IMtkRadioExImsResponse {
        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void imsBearerStateConfirmResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsBearerNotificationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void acknowledgeLastIncomingGsmSmsExResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void acknowledgeLastIncomingCdmaSmsExResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendImsSmsExResponse(RadioResponseInfo info, SendSmsResult sms) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setRttModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendRttModifyRequestResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendRttTextResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void rttModifyRequestResponseResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void toggleRttAudioIndicationResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsRegistrationReportResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void queryVopsStatusResponse(RadioResponseInfo info, int vops) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void sendUssiResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void cancelUssiResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getXcapStatusResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setupXcapUserAgentStringResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getBarringCallsResponse(RadioResponseInfo info, ImsBarringCall[] calls) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setBarringCallsResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void vtDialResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void vtDialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void videoCallAcceptResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void videoRingtoneEventResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void dialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void imsEctCommandResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void pullCallResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void eccRedialApproveResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void conferenceDialResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void forceReleaseCallResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void controlCallResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void controlImsConferenceCallMemberResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setSipHeaderResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setSipHeaderReportResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsCallModeResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setCallAdditionalInfoResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImscfgResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setModemImsCfgResponse(RadioResponseInfo info, String results) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsCfgFeatureValueResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getImsCfgFeatureValueResponse(RadioResponseInfo info, int value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setImsCfgProvisionValueResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getImsCfgProvisionValueResponse(RadioResponseInfo info, String value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getImsCfgResourceCapValueResponse(RadioResponseInfo info, int value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setWfcProfileResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void setVoiceDomainPreferenceResponse(RadioResponseInfo info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void getVoiceDomainPreferenceResponse(RadioResponseInfo info, int vdp) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public void querySsacStatusResponse(RadioResponseInfo info, int[] status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExImsResponse {
        static final int TRANSACTION_acknowledgeLastIncomingCdmaSmsExResponse = 4;
        static final int TRANSACTION_acknowledgeLastIncomingGsmSmsExResponse = 3;
        static final int TRANSACTION_cancelUssiResponse = 14;
        static final int TRANSACTION_conferenceDialResponse = 27;
        static final int TRANSACTION_controlCallResponse = 29;
        static final int TRANSACTION_controlImsConferenceCallMemberResponse = 30;
        static final int TRANSACTION_dialWithSipUriResponse = 23;
        static final int TRANSACTION_eccRedialApproveResponse = 26;
        static final int TRANSACTION_forceReleaseCallResponse = 28;
        static final int TRANSACTION_getBarringCallsResponse = 17;
        static final int TRANSACTION_getImsCfgFeatureValueResponse = 38;
        static final int TRANSACTION_getImsCfgProvisionValueResponse = 40;
        static final int TRANSACTION_getImsCfgResourceCapValueResponse = 41;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getVoiceDomainPreferenceResponse = 44;
        static final int TRANSACTION_getXcapStatusResponse = 15;
        static final int TRANSACTION_imsBearerStateConfirmResponse = 1;
        static final int TRANSACTION_imsEctCommandResponse = 24;
        static final int TRANSACTION_pullCallResponse = 25;
        static final int TRANSACTION_querySsacStatusResponse = 45;
        static final int TRANSACTION_queryVopsStatusResponse = 12;
        static final int TRANSACTION_rttModifyRequestResponseResponse = 9;
        static final int TRANSACTION_sendImsSmsExResponse = 5;
        static final int TRANSACTION_sendRttModifyRequestResponse = 7;
        static final int TRANSACTION_sendRttTextResponse = 8;
        static final int TRANSACTION_sendUssiResponse = 13;
        static final int TRANSACTION_setBarringCallsResponse = 18;
        static final int TRANSACTION_setCallAdditionalInfoResponse = 34;
        static final int TRANSACTION_setImsBearerNotificationResponse = 2;
        static final int TRANSACTION_setImsCallModeResponse = 33;
        static final int TRANSACTION_setImsCfgFeatureValueResponse = 37;
        static final int TRANSACTION_setImsCfgProvisionValueResponse = 39;
        static final int TRANSACTION_setImsRegistrationReportResponse = 11;
        static final int TRANSACTION_setImscfgResponse = 35;
        static final int TRANSACTION_setModemImsCfgResponse = 36;
        static final int TRANSACTION_setRttModeResponse = 6;
        static final int TRANSACTION_setSipHeaderReportResponse = 32;
        static final int TRANSACTION_setSipHeaderResponse = 31;
        static final int TRANSACTION_setVoiceDomainPreferenceResponse = 43;
        static final int TRANSACTION_setWfcProfileResponse = 42;
        static final int TRANSACTION_setupXcapUserAgentStringResponse = 16;
        static final int TRANSACTION_toggleRttAudioIndicationResponse = 10;
        static final int TRANSACTION_videoCallAcceptResponse = 21;
        static final int TRANSACTION_videoRingtoneEventResponse = 22;
        static final int TRANSACTION_vtDialResponse = 19;
        static final int TRANSACTION_vtDialWithSipUriResponse = 20;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExImsResponse asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExImsResponse)) {
                return (IMtkRadioExImsResponse) iin;
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
                            imsBearerStateConfirmResponse(_arg0);
                            return true;
                        case 2:
                            RadioResponseInfo _arg02 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setImsBearerNotificationResponse(_arg02);
                            return true;
                        case 3:
                            RadioResponseInfo _arg03 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            acknowledgeLastIncomingGsmSmsExResponse(_arg03);
                            return true;
                        case 4:
                            RadioResponseInfo _arg04 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            acknowledgeLastIncomingCdmaSmsExResponse(_arg04);
                            return true;
                        case 5:
                            RadioResponseInfo _arg05 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            SendSmsResult _arg1 = (SendSmsResult) data.readTypedObject(SendSmsResult.CREATOR);
                            data.enforceNoDataAvail();
                            sendImsSmsExResponse(_arg05, _arg1);
                            return true;
                        case 6:
                            RadioResponseInfo _arg06 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setRttModeResponse(_arg06);
                            return true;
                        case 7:
                            RadioResponseInfo _arg07 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendRttModifyRequestResponse(_arg07);
                            return true;
                        case 8:
                            RadioResponseInfo _arg08 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendRttTextResponse(_arg08);
                            return true;
                        case 9:
                            RadioResponseInfo _arg09 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            rttModifyRequestResponseResponse(_arg09);
                            return true;
                        case 10:
                            RadioResponseInfo _arg010 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            toggleRttAudioIndicationResponse(_arg010);
                            return true;
                        case 11:
                            RadioResponseInfo _arg011 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setImsRegistrationReportResponse(_arg011);
                            return true;
                        case 12:
                            RadioResponseInfo _arg012 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            queryVopsStatusResponse(_arg012, _arg12);
                            return true;
                        case 13:
                            RadioResponseInfo _arg013 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            sendUssiResponse(_arg013);
                            return true;
                        case 14:
                            RadioResponseInfo _arg014 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            cancelUssiResponse(_arg014);
                            return true;
                        case 15:
                            RadioResponseInfo _arg015 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            getXcapStatusResponse(_arg015);
                            return true;
                        case 16:
                            RadioResponseInfo _arg016 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setupXcapUserAgentStringResponse(_arg016);
                            return true;
                        case 17:
                            RadioResponseInfo _arg017 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            ImsBarringCall[] _arg13 = (ImsBarringCall[]) data.createTypedArray(ImsBarringCall.CREATOR);
                            data.enforceNoDataAvail();
                            getBarringCallsResponse(_arg017, _arg13);
                            return true;
                        case 18:
                            RadioResponseInfo _arg018 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setBarringCallsResponse(_arg018);
                            return true;
                        case 19:
                            RadioResponseInfo _arg019 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            vtDialResponse(_arg019);
                            return true;
                        case 20:
                            RadioResponseInfo _arg020 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            vtDialWithSipUriResponse(_arg020);
                            return true;
                        case 21:
                            RadioResponseInfo _arg021 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            videoCallAcceptResponse(_arg021);
                            return true;
                        case TRANSACTION_videoRingtoneEventResponse /* 22 */:
                            RadioResponseInfo _arg022 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            videoRingtoneEventResponse(_arg022);
                            return true;
                        case TRANSACTION_dialWithSipUriResponse /* 23 */:
                            RadioResponseInfo _arg023 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            dialWithSipUriResponse(_arg023);
                            return true;
                        case TRANSACTION_imsEctCommandResponse /* 24 */:
                            RadioResponseInfo _arg024 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            imsEctCommandResponse(_arg024);
                            return true;
                        case TRANSACTION_pullCallResponse /* 25 */:
                            RadioResponseInfo _arg025 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            pullCallResponse(_arg025);
                            return true;
                        case TRANSACTION_eccRedialApproveResponse /* 26 */:
                            RadioResponseInfo _arg026 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            eccRedialApproveResponse(_arg026);
                            return true;
                        case TRANSACTION_conferenceDialResponse /* 27 */:
                            RadioResponseInfo _arg027 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            conferenceDialResponse(_arg027);
                            return true;
                        case TRANSACTION_forceReleaseCallResponse /* 28 */:
                            RadioResponseInfo _arg028 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            forceReleaseCallResponse(_arg028);
                            return true;
                        case TRANSACTION_controlCallResponse /* 29 */:
                            RadioResponseInfo _arg029 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            controlCallResponse(_arg029);
                            return true;
                        case 30:
                            RadioResponseInfo _arg030 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            controlImsConferenceCallMemberResponse(_arg030);
                            return true;
                        case 31:
                            RadioResponseInfo _arg031 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setSipHeaderResponse(_arg031);
                            return true;
                        case 32:
                            RadioResponseInfo _arg032 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setSipHeaderReportResponse(_arg032);
                            return true;
                        case 33:
                            RadioResponseInfo _arg033 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setImsCallModeResponse(_arg033);
                            return true;
                        case TRANSACTION_setCallAdditionalInfoResponse /* 34 */:
                            RadioResponseInfo _arg034 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setCallAdditionalInfoResponse(_arg034);
                            return true;
                        case TRANSACTION_setImscfgResponse /* 35 */:
                            RadioResponseInfo _arg035 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setImscfgResponse(_arg035);
                            return true;
                        case TRANSACTION_setModemImsCfgResponse /* 36 */:
                            RadioResponseInfo _arg036 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String _arg14 = data.readString();
                            data.enforceNoDataAvail();
                            setModemImsCfgResponse(_arg036, _arg14);
                            return true;
                        case TRANSACTION_setImsCfgFeatureValueResponse /* 37 */:
                            RadioResponseInfo _arg037 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setImsCfgFeatureValueResponse(_arg037);
                            return true;
                        case TRANSACTION_getImsCfgFeatureValueResponse /* 38 */:
                            RadioResponseInfo _arg038 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            getImsCfgFeatureValueResponse(_arg038, _arg15);
                            return true;
                        case TRANSACTION_setImsCfgProvisionValueResponse /* 39 */:
                            RadioResponseInfo _arg039 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setImsCfgProvisionValueResponse(_arg039);
                            return true;
                        case 40:
                            RadioResponseInfo _arg040 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            getImsCfgProvisionValueResponse(_arg040, _arg16);
                            return true;
                        case TRANSACTION_getImsCfgResourceCapValueResponse /* 41 */:
                            RadioResponseInfo _arg041 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            getImsCfgResourceCapValueResponse(_arg041, _arg17);
                            return true;
                        case TRANSACTION_setWfcProfileResponse /* 42 */:
                            RadioResponseInfo _arg042 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setWfcProfileResponse(_arg042);
                            return true;
                        case TRANSACTION_setVoiceDomainPreferenceResponse /* 43 */:
                            RadioResponseInfo _arg043 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            data.enforceNoDataAvail();
                            setVoiceDomainPreferenceResponse(_arg043);
                            return true;
                        case TRANSACTION_getVoiceDomainPreferenceResponse /* 44 */:
                            RadioResponseInfo _arg044 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            getVoiceDomainPreferenceResponse(_arg044, _arg18);
                            return true;
                        case TRANSACTION_querySsacStatusResponse /* 45 */:
                            RadioResponseInfo _arg045 = (RadioResponseInfo) data.readTypedObject(RadioResponseInfo.CREATOR);
                            int[] _arg19 = data.createIntArray();
                            data.enforceNoDataAvail();
                            querySsacStatusResponse(_arg045, _arg19);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExImsResponse {
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void imsBearerStateConfirmResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsBearerStateConfirmResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsBearerNotificationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsBearerNotificationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void acknowledgeLastIncomingGsmSmsExResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method acknowledgeLastIncomingGsmSmsExResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void acknowledgeLastIncomingCdmaSmsExResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method acknowledgeLastIncomingCdmaSmsExResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendImsSmsExResponse(RadioResponseInfo info, SendSmsResult sms) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedObject(sms, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendImsSmsExResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setRttModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setRttModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendRttModifyRequestResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRttModifyRequestResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendRttTextResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendRttTextResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void rttModifyRequestResponseResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method rttModifyRequestResponseResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void toggleRttAudioIndicationResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method toggleRttAudioIndicationResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsRegistrationReportResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsRegistrationReportResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void queryVopsStatusResponse(RadioResponseInfo info, int vops) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(vops);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method queryVopsStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void sendUssiResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendUssiResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void cancelUssiResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cancelUssiResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getXcapStatusResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getXcapStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setupXcapUserAgentStringResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setupXcapUserAgentStringResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getBarringCallsResponse(RadioResponseInfo info, ImsBarringCall[] calls) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeTypedArray(calls, 0);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getBarringCallsResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setBarringCallsResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setBarringCallsResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void vtDialResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method vtDialResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void vtDialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method vtDialWithSipUriResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void videoCallAcceptResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method videoCallAcceptResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void videoRingtoneEventResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoRingtoneEventResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method videoRingtoneEventResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void dialWithSipUriResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_dialWithSipUriResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method dialWithSipUriResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void imsEctCommandResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsEctCommandResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsEctCommandResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void pullCallResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_pullCallResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method pullCallResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void eccRedialApproveResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_eccRedialApproveResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method eccRedialApproveResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void conferenceDialResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_conferenceDialResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method conferenceDialResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void forceReleaseCallResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_forceReleaseCallResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method forceReleaseCallResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void controlCallResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_controlCallResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method controlCallResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void controlImsConferenceCallMemberResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method controlImsConferenceCallMemberResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setSipHeaderResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSipHeaderResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setSipHeaderReportResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setSipHeaderReportResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsCallModeResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsCallModeResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setCallAdditionalInfoResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setCallAdditionalInfoResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setCallAdditionalInfoResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImscfgResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImscfgResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImscfgResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setModemImsCfgResponse(RadioResponseInfo info, String results) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(results);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setModemImsCfgResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setModemImsCfgResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsCfgFeatureValueResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImsCfgFeatureValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsCfgFeatureValueResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getImsCfgFeatureValueResponse(RadioResponseInfo info, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgFeatureValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getImsCfgFeatureValueResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setImsCfgProvisionValueResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setImsCfgProvisionValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setImsCfgProvisionValueResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getImsCfgProvisionValueResponse(RadioResponseInfo info, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getImsCfgProvisionValueResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getImsCfgResourceCapValueResponse(RadioResponseInfo info, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getImsCfgResourceCapValueResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getImsCfgResourceCapValueResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setWfcProfileResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setWfcProfileResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setWfcProfileResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void setVoiceDomainPreferenceResponse(RadioResponseInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_setVoiceDomainPreferenceResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setVoiceDomainPreferenceResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void getVoiceDomainPreferenceResponse(RadioResponseInfo info, int vdp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeInt(vdp);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_getVoiceDomainPreferenceResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method getVoiceDomainPreferenceResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
            public void querySsacStatusResponse(RadioResponseInfo info, int[] status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeIntArray(status);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_querySsacStatusResponse, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method querySsacStatusResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsResponse
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
