package com.mediatek.internal.telephony.uicc;

/* JADX INFO: loaded from: classes.dex */
public class IccServiceInfo {

    public enum IccServiceStatus {
        NOT_EXIST_IN_SIM,
        NOT_EXIST_IN_USIM,
        ACTIVATED,
        INACTIVATED,
        UNKNOWN
    }

    public enum IccService {
        CHV1_DISABLE_FUNCTION,
        SPN,
        PNN,
        OPL,
        MWIS,
        CFIS,
        SPDI,
        EPLMN,
        SMSP,
        FDN,
        PLMNsel,
        OPLMNwACT,
        UNSUPPORTED_SERVICE;

        public int getIndex() {
            switch (AnonymousClass1.$SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[ordinal()]) {
                case 1:
                    return 0;
                case 2:
                    return 1;
                case 3:
                    return 2;
                case 4:
                    return 3;
                case 5:
                    return 4;
                case 6:
                    return 5;
                case 7:
                    return 6;
                case 8:
                    return 7;
                case 9:
                    return 8;
                case 10:
                    return 9;
                case 11:
                    return 10;
                case 12:
                    return 11;
                case 13:
                    return 10;
                default:
                    return -1;
            }
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.uicc.IccServiceInfo$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService;

        static {
            int[] iArr = new int[IccService.values().length];
            $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService = iArr;
            try {
                iArr[IccService.CHV1_DISABLE_FUNCTION.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.SPN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.PNN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.OPL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.MWIS.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.CFIS.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.SPDI.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.EPLMN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.SMSP.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.FDN.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.PLMNsel.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.OPLMNwACT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$uicc$IccServiceInfo$IccService[IccService.UNSUPPORTED_SERVICE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
        }
    }
}
