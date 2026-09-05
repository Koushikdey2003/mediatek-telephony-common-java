package vendor.mediatek.hardware.mtkradioex.ims;

import android.hardware.radio.messaging.CdmaSmsMessage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.mediatek.hardware.mtkradioex.voice.Dialog;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkRadioExImsIndication extends IInterface {
    public static final String DESCRIPTOR = "vendor$mediatek$hardware$mtkradioex$ims$IMtkRadioExImsIndication".replace('$', '.');
    public static final String HASH = "e52459812a302acdde2f0131532472cd88eaad52";
    public static final int VERSION = 1;

    void audioIndication(int i, int i2, int i3) throws RemoteException;

    void callInfoIndication(int i, String[] strArr) throws RemoteException;

    void callRatIndication(int i, int i2, int i3) throws RemoteException;

    void callmodChangeIndicator(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void cdmaNewSmsEx(int i, CdmaSmsMessage cdmaSmsMessage) throws RemoteException;

    void econfResultIndication(int i, String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    void ectIndication(int i, int i2, int i3, int i4) throws RemoteException;

    void eregrtInfoInd(int i, int[] iArr) throws RemoteException;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void imsBearerInit(int i) throws RemoteException;

    void imsBearerStateNotify(int i, int i2, int i3, String str) throws RemoteException;

    void imsCfgConfigChanged(int i, int i2, String str, String str2) throws RemoteException;

    void imsCfgConfigLoaded(int i) throws RemoteException;

    void imsCfgDynamicImsSwitchComplete(int i) throws RemoteException;

    void imsCfgFeatureChanged(int i, int i2, int i3, int i4) throws RemoteException;

    void imsConferenceInfoIndication(int i, ImsConfParticipant[] imsConfParticipantArr) throws RemoteException;

    void imsDataInfoNotify(int i, String str, String str2, String str3) throws RemoteException;

    void imsDialogIndication(int i, Dialog[] dialogArr) throws RemoteException;

    void imsEventPackageIndication(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void imsRedialEmergencyIndication(int i, String str) throws RemoteException;

    void imsRegFlagInd(int i, int i2) throws RemoteException;

    void imsRegInfoInd(int i, int[] iArr) throws RemoteException;

    void imsRegStatusReport(int i, ImsRegStatusInfo imsRegStatusInfo) throws RemoteException;

    void imsRegistrationInfo(int i, int i2, int i3) throws RemoteException;

    void imsRtpInfo(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    void imsSupportEcc(int i, int i2) throws RemoteException;

    void lteMessageWaitingIndication(int i, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void newSmsEx(int i, byte[] bArr) throws RemoteException;

    void newSmsStatusReportEx(int i, byte[] bArr) throws RemoteException;

    void noEmergencyCallbackMode(int i) throws RemoteException;

    void onMDInternetUsageInd(int i, int[] iArr) throws RemoteException;

    void onSsacStatus(int i, int[] iArr) throws RemoteException;

    void onUssi(int i, int i2, String str) throws RemoteException;

    void onVolteSubscription(int i, int i2) throws RemoteException;

    void onXui(int i, String str, String str2, String str3) throws RemoteException;

    void rttCapabilityIndication(int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void rttModifyRequestReceive(int i, int i2, int i3) throws RemoteException;

    void rttModifyResponse(int i, int i2, int i3) throws RemoteException;

    void rttTextReceive(int i, int i2, int i3, String str) throws RemoteException;

    void sendVopsIndication(int i, int i2) throws RemoteException;

    void sipCallProgressIndicator(int i, String str, String str2, String str3, String str4, String str5, String str6) throws RemoteException;

    void sipHeaderReport(int i, String[] strArr) throws RemoteException;

    void sipRegInfoInd(int i, int i2, int i3, String[] strArr) throws RemoteException;

    void speechCodecInfoIndication(int i, int i2) throws RemoteException;

    void videoCapabilityIndicator(int i, String str, String str2, String str3) throws RemoteException;

    void videoRingtoneEventInd(int i, String[] strArr) throws RemoteException;

    void volteSetting(int i, boolean z) throws RemoteException;

    public static class Default implements IMtkRadioExImsIndication {
        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsBearerStateNotify(int type, int aid, int action, String capability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsBearerInit(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsDataInfoNotify(int type, String capability, String event, String extra) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void cdmaNewSmsEx(int type, CdmaSmsMessage msg) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void newSmsStatusReportEx(int type, byte[] pdu) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void newSmsEx(int type, byte[] pdu) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttModifyResponse(int type, int callId, int result) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttTextReceive(int type, int callId, int lenOfString, String text) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttCapabilityIndication(int type, int callId, int localCap, int remoteCap, int localStatus, int remoteStatus) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void rttModifyRequestReceive(int type, int callId, int rttType) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void audioIndication(int type, int callId, int audio) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sendVopsIndication(int type, int vops) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void volteSetting(int type, boolean isEnable) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegistrationInfo(int type, int registerState, int capability) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsSupportEcc(int type, int supportLteEcc) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sipRegInfoInd(int type, int account_id, int response_code, String[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegStatusReport(int type, ImsRegStatusInfo report) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegInfoInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void eregrtInfoInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRegFlagInd(int type, int flag) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRtpInfo(int type, String pdnId, String networkId, String timer, String sendPktLost, String recvPktLost, String jitter, String delay) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onXui(int type, String accountId, String broadcastFlag, String xuiInfo) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onUssi(int type, int modeType, String msg) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onVolteSubscription(int type, int status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void videoCapabilityIndicator(int type, String callId, String localVideoCap, String remoteVideoCap) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void videoRingtoneEventInd(int type, String[] event) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onMDInternetUsageInd(int type, int[] info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void ectIndication(int type, int call_id, int ectResult, int cause) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void callInfoIndication(int type, String[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void callmodChangeIndicator(int type, String callId, String callMode, String videoState, String audioDirection, String pau) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void econfResultIndication(int type, String confCallId, String op, String num, String result, String cause, String joinedCallId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sipCallProgressIndicator(int type, String callId, String dir, String sipMsgType, String method, String responseCode, String reasonText) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsConferenceInfoIndication(int type, ImsConfParticipant[] participants) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void lteMessageWaitingIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsDialogIndication(int type, Dialog[] dialogList) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void noEmergencyCallbackMode(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsRedialEmergencyIndication(int type, String callId) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void speechCodecInfoIndication(int type, int info) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsEventPackageIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void sipHeaderReport(int type, String[] data) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void callRatIndication(int type, int domain, int rat) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgDynamicImsSwitchComplete(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgConfigChanged(int type, int phoneId, String configId, String value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgFeatureChanged(int type, int phoneId, int featureId, int value) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void imsCfgConfigLoaded(int type) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public void onSsacStatus(int type, int[] status) throws RemoteException {
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkRadioExImsIndication {
        static final int TRANSACTION_audioIndication = 11;
        static final int TRANSACTION_callInfoIndication = 29;
        static final int TRANSACTION_callRatIndication = 41;
        static final int TRANSACTION_callmodChangeIndicator = 30;
        static final int TRANSACTION_cdmaNewSmsEx = 4;
        static final int TRANSACTION_econfResultIndication = 31;
        static final int TRANSACTION_ectIndication = 28;
        static final int TRANSACTION_eregrtInfoInd = 19;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_imsBearerInit = 2;
        static final int TRANSACTION_imsBearerStateNotify = 1;
        static final int TRANSACTION_imsCfgConfigChanged = 43;
        static final int TRANSACTION_imsCfgConfigLoaded = 45;
        static final int TRANSACTION_imsCfgDynamicImsSwitchComplete = 42;
        static final int TRANSACTION_imsCfgFeatureChanged = 44;
        static final int TRANSACTION_imsConferenceInfoIndication = 33;
        static final int TRANSACTION_imsDataInfoNotify = 3;
        static final int TRANSACTION_imsDialogIndication = 35;
        static final int TRANSACTION_imsEventPackageIndication = 39;
        static final int TRANSACTION_imsRedialEmergencyIndication = 37;
        static final int TRANSACTION_imsRegFlagInd = 20;
        static final int TRANSACTION_imsRegInfoInd = 18;
        static final int TRANSACTION_imsRegStatusReport = 17;
        static final int TRANSACTION_imsRegistrationInfo = 14;
        static final int TRANSACTION_imsRtpInfo = 21;
        static final int TRANSACTION_imsSupportEcc = 15;
        static final int TRANSACTION_lteMessageWaitingIndication = 34;
        static final int TRANSACTION_newSmsEx = 6;
        static final int TRANSACTION_newSmsStatusReportEx = 5;
        static final int TRANSACTION_noEmergencyCallbackMode = 36;
        static final int TRANSACTION_onMDInternetUsageInd = 27;
        static final int TRANSACTION_onSsacStatus = 46;
        static final int TRANSACTION_onUssi = 23;
        static final int TRANSACTION_onVolteSubscription = 24;
        static final int TRANSACTION_onXui = 22;
        static final int TRANSACTION_rttCapabilityIndication = 9;
        static final int TRANSACTION_rttModifyRequestReceive = 10;
        static final int TRANSACTION_rttModifyResponse = 7;
        static final int TRANSACTION_rttTextReceive = 8;
        static final int TRANSACTION_sendVopsIndication = 12;
        static final int TRANSACTION_sipCallProgressIndicator = 32;
        static final int TRANSACTION_sipHeaderReport = 40;
        static final int TRANSACTION_sipRegInfoInd = 16;
        static final int TRANSACTION_speechCodecInfoIndication = 38;
        static final int TRANSACTION_videoCapabilityIndicator = 25;
        static final int TRANSACTION_videoRingtoneEventInd = 26;
        static final int TRANSACTION_volteSetting = 13;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IMtkRadioExImsIndication asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkRadioExImsIndication)) {
                return (IMtkRadioExImsIndication) iin;
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
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            imsBearerStateNotify(_arg0, _arg1, _arg2, _arg3);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            imsBearerInit(_arg02);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            String _arg12 = data.readString();
                            String _arg22 = data.readString();
                            String _arg32 = data.readString();
                            data.enforceNoDataAvail();
                            imsDataInfoNotify(_arg03, _arg12, _arg22, _arg32);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            CdmaSmsMessage _arg13 = (CdmaSmsMessage) data.readTypedObject(CdmaSmsMessage.CREATOR);
                            data.enforceNoDataAvail();
                            cdmaNewSmsEx(_arg04, _arg13);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            byte[] _arg14 = data.createByteArray();
                            data.enforceNoDataAvail();
                            newSmsStatusReportEx(_arg05, _arg14);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            byte[] _arg15 = data.createByteArray();
                            data.enforceNoDataAvail();
                            newSmsEx(_arg06, _arg15);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg16 = data.readInt();
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            rttModifyResponse(_arg07, _arg16, _arg23);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg17 = data.readInt();
                            int _arg24 = data.readInt();
                            String _arg33 = data.readString();
                            data.enforceNoDataAvail();
                            rttTextReceive(_arg08, _arg17, _arg24, _arg33);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            int _arg18 = data.readInt();
                            int _arg25 = data.readInt();
                            int _arg34 = data.readInt();
                            int _arg4 = data.readInt();
                            int _arg5 = data.readInt();
                            data.enforceNoDataAvail();
                            rttCapabilityIndication(_arg09, _arg18, _arg25, _arg34, _arg4, _arg5);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            int _arg19 = data.readInt();
                            int _arg26 = data.readInt();
                            data.enforceNoDataAvail();
                            rttModifyRequestReceive(_arg010, _arg19, _arg26);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int _arg110 = data.readInt();
                            int _arg27 = data.readInt();
                            data.enforceNoDataAvail();
                            audioIndication(_arg011, _arg110, _arg27);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg111 = data.readInt();
                            data.enforceNoDataAvail();
                            sendVopsIndication(_arg012, _arg111);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            boolean _arg112 = data.readBoolean();
                            data.enforceNoDataAvail();
                            volteSetting(_arg013, _arg112);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int _arg113 = data.readInt();
                            int _arg28 = data.readInt();
                            data.enforceNoDataAvail();
                            imsRegistrationInfo(_arg014, _arg113, _arg28);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            int _arg114 = data.readInt();
                            data.enforceNoDataAvail();
                            imsSupportEcc(_arg015, _arg114);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            int _arg115 = data.readInt();
                            int _arg29 = data.readInt();
                            String[] _arg35 = data.createStringArray();
                            data.enforceNoDataAvail();
                            sipRegInfoInd(_arg016, _arg115, _arg29, _arg35);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            ImsRegStatusInfo _arg116 = (ImsRegStatusInfo) data.readTypedObject(ImsRegStatusInfo.CREATOR);
                            data.enforceNoDataAvail();
                            imsRegStatusReport(_arg017, _arg116);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            int[] _arg117 = data.createIntArray();
                            data.enforceNoDataAvail();
                            imsRegInfoInd(_arg018, _arg117);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            int[] _arg118 = data.createIntArray();
                            data.enforceNoDataAvail();
                            eregrtInfoInd(_arg019, _arg118);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            int _arg119 = data.readInt();
                            data.enforceNoDataAvail();
                            imsRegFlagInd(_arg020, _arg119);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            String _arg120 = data.readString();
                            String _arg210 = data.readString();
                            String _arg36 = data.readString();
                            String _arg42 = data.readString();
                            String _arg52 = data.readString();
                            String _arg6 = data.readString();
                            String _arg7 = data.readString();
                            data.enforceNoDataAvail();
                            imsRtpInfo(_arg021, _arg120, _arg210, _arg36, _arg42, _arg52, _arg6, _arg7);
                            return true;
                        case TRANSACTION_onXui /* 22 */:
                            int _arg022 = data.readInt();
                            String _arg121 = data.readString();
                            String _arg211 = data.readString();
                            String _arg37 = data.readString();
                            data.enforceNoDataAvail();
                            onXui(_arg022, _arg121, _arg211, _arg37);
                            return true;
                        case TRANSACTION_onUssi /* 23 */:
                            int _arg023 = data.readInt();
                            int _arg122 = data.readInt();
                            String _arg212 = data.readString();
                            data.enforceNoDataAvail();
                            onUssi(_arg023, _arg122, _arg212);
                            return true;
                        case TRANSACTION_onVolteSubscription /* 24 */:
                            int _arg024 = data.readInt();
                            int _arg123 = data.readInt();
                            data.enforceNoDataAvail();
                            onVolteSubscription(_arg024, _arg123);
                            return true;
                        case TRANSACTION_videoCapabilityIndicator /* 25 */:
                            int _arg025 = data.readInt();
                            String _arg124 = data.readString();
                            String _arg213 = data.readString();
                            String _arg38 = data.readString();
                            data.enforceNoDataAvail();
                            videoCapabilityIndicator(_arg025, _arg124, _arg213, _arg38);
                            return true;
                        case TRANSACTION_videoRingtoneEventInd /* 26 */:
                            int _arg026 = data.readInt();
                            String[] _arg125 = data.createStringArray();
                            data.enforceNoDataAvail();
                            videoRingtoneEventInd(_arg026, _arg125);
                            return true;
                        case TRANSACTION_onMDInternetUsageInd /* 27 */:
                            int _arg027 = data.readInt();
                            int[] _arg126 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onMDInternetUsageInd(_arg027, _arg126);
                            return true;
                        case TRANSACTION_ectIndication /* 28 */:
                            int _arg028 = data.readInt();
                            int _arg127 = data.readInt();
                            int _arg214 = data.readInt();
                            int _arg39 = data.readInt();
                            data.enforceNoDataAvail();
                            ectIndication(_arg028, _arg127, _arg214, _arg39);
                            return true;
                        case TRANSACTION_callInfoIndication /* 29 */:
                            int _arg029 = data.readInt();
                            String[] _arg128 = data.createStringArray();
                            data.enforceNoDataAvail();
                            callInfoIndication(_arg029, _arg128);
                            return true;
                        case 30:
                            int _arg030 = data.readInt();
                            String _arg129 = data.readString();
                            String _arg215 = data.readString();
                            String _arg310 = data.readString();
                            String _arg43 = data.readString();
                            String _arg53 = data.readString();
                            data.enforceNoDataAvail();
                            callmodChangeIndicator(_arg030, _arg129, _arg215, _arg310, _arg43, _arg53);
                            return true;
                        case 31:
                            int _arg031 = data.readInt();
                            String _arg130 = data.readString();
                            String _arg216 = data.readString();
                            String _arg311 = data.readString();
                            String _arg44 = data.readString();
                            String _arg54 = data.readString();
                            String _arg62 = data.readString();
                            data.enforceNoDataAvail();
                            econfResultIndication(_arg031, _arg130, _arg216, _arg311, _arg44, _arg54, _arg62);
                            return true;
                        case 32:
                            int _arg032 = data.readInt();
                            String _arg131 = data.readString();
                            String _arg217 = data.readString();
                            String _arg312 = data.readString();
                            String _arg45 = data.readString();
                            String _arg55 = data.readString();
                            String _arg63 = data.readString();
                            data.enforceNoDataAvail();
                            sipCallProgressIndicator(_arg032, _arg131, _arg217, _arg312, _arg45, _arg55, _arg63);
                            return true;
                        case 33:
                            int _arg033 = data.readInt();
                            ImsConfParticipant[] _arg132 = (ImsConfParticipant[]) data.createTypedArray(ImsConfParticipant.CREATOR);
                            data.enforceNoDataAvail();
                            imsConferenceInfoIndication(_arg033, _arg132);
                            return true;
                        case TRANSACTION_lteMessageWaitingIndication /* 34 */:
                            int _arg034 = data.readInt();
                            String _arg133 = data.readString();
                            String _arg218 = data.readString();
                            String _arg313 = data.readString();
                            String _arg46 = data.readString();
                            String _arg56 = data.readString();
                            data.enforceNoDataAvail();
                            lteMessageWaitingIndication(_arg034, _arg133, _arg218, _arg313, _arg46, _arg56);
                            return true;
                        case TRANSACTION_imsDialogIndication /* 35 */:
                            int _arg035 = data.readInt();
                            Dialog[] _arg134 = (Dialog[]) data.createTypedArray(Dialog.CREATOR);
                            data.enforceNoDataAvail();
                            imsDialogIndication(_arg035, _arg134);
                            return true;
                        case TRANSACTION_noEmergencyCallbackMode /* 36 */:
                            int _arg036 = data.readInt();
                            data.enforceNoDataAvail();
                            noEmergencyCallbackMode(_arg036);
                            return true;
                        case TRANSACTION_imsRedialEmergencyIndication /* 37 */:
                            int _arg037 = data.readInt();
                            String _arg135 = data.readString();
                            data.enforceNoDataAvail();
                            imsRedialEmergencyIndication(_arg037, _arg135);
                            return true;
                        case TRANSACTION_speechCodecInfoIndication /* 38 */:
                            int _arg038 = data.readInt();
                            int _arg136 = data.readInt();
                            data.enforceNoDataAvail();
                            speechCodecInfoIndication(_arg038, _arg136);
                            return true;
                        case TRANSACTION_imsEventPackageIndication /* 39 */:
                            int _arg039 = data.readInt();
                            String _arg137 = data.readString();
                            String _arg219 = data.readString();
                            String _arg314 = data.readString();
                            String _arg47 = data.readString();
                            String _arg57 = data.readString();
                            data.enforceNoDataAvail();
                            imsEventPackageIndication(_arg039, _arg137, _arg219, _arg314, _arg47, _arg57);
                            return true;
                        case 40:
                            int _arg040 = data.readInt();
                            String[] _arg138 = data.createStringArray();
                            data.enforceNoDataAvail();
                            sipHeaderReport(_arg040, _arg138);
                            return true;
                        case TRANSACTION_callRatIndication /* 41 */:
                            int _arg041 = data.readInt();
                            int _arg139 = data.readInt();
                            int _arg220 = data.readInt();
                            data.enforceNoDataAvail();
                            callRatIndication(_arg041, _arg139, _arg220);
                            return true;
                        case TRANSACTION_imsCfgDynamicImsSwitchComplete /* 42 */:
                            int _arg042 = data.readInt();
                            data.enforceNoDataAvail();
                            imsCfgDynamicImsSwitchComplete(_arg042);
                            return true;
                        case TRANSACTION_imsCfgConfigChanged /* 43 */:
                            int _arg043 = data.readInt();
                            int _arg140 = data.readInt();
                            String _arg221 = data.readString();
                            String _arg315 = data.readString();
                            data.enforceNoDataAvail();
                            imsCfgConfigChanged(_arg043, _arg140, _arg221, _arg315);
                            return true;
                        case TRANSACTION_imsCfgFeatureChanged /* 44 */:
                            int _arg044 = data.readInt();
                            int _arg141 = data.readInt();
                            int _arg222 = data.readInt();
                            int _arg316 = data.readInt();
                            data.enforceNoDataAvail();
                            imsCfgFeatureChanged(_arg044, _arg141, _arg222, _arg316);
                            return true;
                        case TRANSACTION_imsCfgConfigLoaded /* 45 */:
                            int _arg045 = data.readInt();
                            data.enforceNoDataAvail();
                            imsCfgConfigLoaded(_arg045);
                            return true;
                        case 46:
                            int _arg046 = data.readInt();
                            int[] _arg142 = data.createIntArray();
                            data.enforceNoDataAvail();
                            onSsacStatus(_arg046, _arg142);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkRadioExImsIndication {
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsBearerStateNotify(int type, int aid, int action, String capability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(aid);
                    _data.writeInt(action);
                    _data.writeString(capability);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsBearerStateNotify is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsBearerInit(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsBearerInit is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsDataInfoNotify(int type, String capability, String event, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(capability);
                    _data.writeString(event);
                    _data.writeString(extra);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsDataInfoNotify is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void cdmaNewSmsEx(int type, CdmaSmsMessage msg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(msg, 0);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method cdmaNewSmsEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void newSmsStatusReportEx(int type, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(pdu);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method newSmsStatusReportEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void newSmsEx(int type, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(pdu);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method newSmsEx is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttModifyResponse(int type, int callId, int result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method rttModifyResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttTextReceive(int type, int callId, int lenOfString, String text) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(lenOfString);
                    _data.writeString(text);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method rttTextReceive is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttCapabilityIndication(int type, int callId, int localCap, int remoteCap, int localStatus, int remoteStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(localCap);
                    _data.writeInt(remoteCap);
                    _data.writeInt(localStatus);
                    _data.writeInt(remoteStatus);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method rttCapabilityIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void rttModifyRequestReceive(int type, int callId, int rttType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(rttType);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method rttModifyRequestReceive is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void audioIndication(int type, int callId, int audio) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(callId);
                    _data.writeInt(audio);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method audioIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sendVopsIndication(int type, int vops) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(vops);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sendVopsIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void volteSetting(int type, boolean isEnable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeBoolean(isEnable);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method volteSetting is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegistrationInfo(int type, int registerState, int capability) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(registerState);
                    _data.writeInt(capability);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsRegistrationInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsSupportEcc(int type, int supportLteEcc) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(supportLteEcc);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsSupportEcc is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sipRegInfoInd(int type, int account_id, int response_code, String[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(account_id);
                    _data.writeInt(response_code);
                    _data.writeStringArray(info);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sipRegInfoInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegStatusReport(int type, ImsRegStatusInfo report) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(report, 0);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsRegStatusReport is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegInfoInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsRegInfoInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void eregrtInfoInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method eregrtInfoInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRegFlagInd(int type, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(flag);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsRegFlagInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRtpInfo(int type, String pdnId, String networkId, String timer, String sendPktLost, String recvPktLost, String jitter, String delay) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(pdnId);
                    _data.writeString(networkId);
                    _data.writeString(timer);
                    _data.writeString(sendPktLost);
                    _data.writeString(recvPktLost);
                    _data.writeString(jitter);
                    _data.writeString(delay);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsRtpInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onXui(int type, String accountId, String broadcastFlag, String xuiInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(accountId);
                    _data.writeString(broadcastFlag);
                    _data.writeString(xuiInfo);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onXui, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onXui is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onUssi(int type, int modeType, String msg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(modeType);
                    _data.writeString(msg);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onUssi, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onUssi is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onVolteSubscription(int type, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onVolteSubscription, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onVolteSubscription is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void videoCapabilityIndicator(int type, String callId, String localVideoCap, String remoteVideoCap) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(localVideoCap);
                    _data.writeString(remoteVideoCap);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoCapabilityIndicator, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method videoCapabilityIndicator is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void videoRingtoneEventInd(int type, String[] event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(event);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_videoRingtoneEventInd, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method videoRingtoneEventInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onMDInternetUsageInd(int type, int[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(info);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_onMDInternetUsageInd, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onMDInternetUsageInd is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void ectIndication(int type, int call_id, int ectResult, int cause) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(call_id);
                    _data.writeInt(ectResult);
                    _data.writeInt(cause);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_ectIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method ectIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void callInfoIndication(int type, String[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(data);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_callInfoIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method callInfoIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void callmodChangeIndicator(int type, String callId, String callMode, String videoState, String audioDirection, String pau) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(callMode);
                    _data.writeString(videoState);
                    _data.writeString(audioDirection);
                    _data.writeString(pau);
                    boolean _status = this.mRemote.transact(30, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method callmodChangeIndicator is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void econfResultIndication(int type, String confCallId, String op, String num, String result, String cause, String joinedCallId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(confCallId);
                    _data.writeString(op);
                    _data.writeString(num);
                    _data.writeString(result);
                    _data.writeString(cause);
                    _data.writeString(joinedCallId);
                    boolean _status = this.mRemote.transact(31, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method econfResultIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sipCallProgressIndicator(int type, String callId, String dir, String sipMsgType, String method, String responseCode, String reasonText) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(dir);
                    _data.writeString(sipMsgType);
                    _data.writeString(method);
                    _data.writeString(responseCode);
                    _data.writeString(reasonText);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sipCallProgressIndicator is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsConferenceInfoIndication(int type, ImsConfParticipant[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedArray(participants, 0);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsConferenceInfoIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void lteMessageWaitingIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(ptype);
                    _data.writeString(urcIdx);
                    _data.writeString(totalUrcCount);
                    _data.writeString(rawData);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_lteMessageWaitingIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method lteMessageWaitingIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsDialogIndication(int type, Dialog[] dialogList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedArray(dialogList, 0);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsDialogIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsDialogIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void noEmergencyCallbackMode(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_noEmergencyCallbackMode, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method noEmergencyCallbackMode is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsRedialEmergencyIndication(int type, String callId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsRedialEmergencyIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsRedialEmergencyIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void speechCodecInfoIndication(int type, int info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(info);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_speechCodecInfoIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method speechCodecInfoIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsEventPackageIndication(int type, String callId, String ptype, String urcIdx, String totalUrcCount, String rawData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(callId);
                    _data.writeString(ptype);
                    _data.writeString(urcIdx);
                    _data.writeString(totalUrcCount);
                    _data.writeString(rawData);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsEventPackageIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsEventPackageIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void sipHeaderReport(int type, String[] data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStringArray(data);
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method sipHeaderReport is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void callRatIndication(int type, int domain, int rat) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(domain);
                    _data.writeInt(rat);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_callRatIndication, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method callRatIndication is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgDynamicImsSwitchComplete(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgDynamicImsSwitchComplete, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsCfgDynamicImsSwitchComplete is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgConfigChanged(int type, int phoneId, String configId, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(phoneId);
                    _data.writeString(configId);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgConfigChanged, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsCfgConfigChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgFeatureChanged(int type, int phoneId, int featureId, int value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(phoneId);
                    _data.writeInt(featureId);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgFeatureChanged, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsCfgFeatureChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void imsCfgConfigLoaded(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(Stub.TRANSACTION_imsCfgConfigLoaded, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method imsCfgConfigLoaded is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
            public void onSsacStatus(int type, int[] status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(status);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onSsacStatus is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
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

            @Override // vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExImsIndication
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
