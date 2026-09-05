package com.mediatek.internal.telephony;

/* JADX INFO: loaded from: classes.dex */
public class MtkSuppServContants {
    public static final String SYS_PROP_BOOL_CONFIG = "persist.vendor.ss.cfg.boolconfig";
    public static final String SYS_PROP_BOOL_VALUE = "persist.vendor.ss.cfg.boolvalue";

    public enum CUSTOMIZATION_ITEM {
        GSM_UT_SUPPORT,
        TBCLIR,
        IMS_NW_CW,
        NOT_SUPPORT_XCAP,
        NOT_SUPPORT_OCB,
        ENABLE_XCAP_HTTP_RESPONSE_409,
        TRANSFER_XCAP_404,
        NOT_SUPPORT_WFC_UT,
        NOT_SUPPORT_CALL_IDENTITY,
        RE_REGISTER_FOR_CF,
        SUPPORT_SAVE_CF_NUMBER,
        QUERY_CFU_AGAIN_AFTER_SET,
        NEED_CHECK_DATA_ENABLE,
        NEED_CHECK_DATA_ROAMING,
        NEED_CHECK_IMS_WHEN_ROAMING
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.MtkSuppServContants$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM;

        static {
            int[] iArr = new int[CUSTOMIZATION_ITEM.values().length];
            $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM = iArr;
            try {
                iArr[CUSTOMIZATION_ITEM.GSM_UT_SUPPORT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NOT_SUPPORT_XCAP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.TBCLIR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.IMS_NW_CW.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.ENABLE_XCAP_HTTP_RESPONSE_409.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.TRANSFER_XCAP_404.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NOT_SUPPORT_CALL_IDENTITY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.RE_REGISTER_FOR_CF.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.SUPPORT_SAVE_CF_NUMBER.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.QUERY_CFU_AGAIN_AFTER_SET.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NOT_SUPPORT_OCB.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NOT_SUPPORT_WFC_UT.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NEED_CHECK_DATA_ENABLE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NEED_CHECK_DATA_ROAMING.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[CUSTOMIZATION_ITEM.NEED_CHECK_IMS_WHEN_ROAMING.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    public static String toString(CUSTOMIZATION_ITEM item) {
        switch (AnonymousClass1.$SwitchMap$com$mediatek$internal$telephony$MtkSuppServContants$CUSTOMIZATION_ITEM[item.ordinal()]) {
            case 1:
                return "GSM_UT_SUPPORT";
            case 2:
                return "NOT_SUPPORT_XCAP";
            case 3:
                return "TBCLIR";
            case 4:
                return "IMS_NW_CW";
            case 5:
                return "ENABLE_XCAP_HTTP_RESPONSE_409";
            case 6:
                return "TRANSFER_XCAP_404";
            case 7:
                return "NOT_SUPPORT_CALL_IDENTITY";
            case 8:
                return "RE_REGISTER_FOR_CF";
            case 9:
                return "SUPPORT_SAVE_CF_NUMBER";
            case 10:
                return "QUERY_CFU_AGAIN_AFTER_SET";
            case 11:
                return "NOT_SUPPORT_OCB";
            case 12:
                return "NOT_SUPPORT_WFC_UT";
            case 13:
                return "NEED_CHECK_DATA_ENABLE";
            case 14:
                return "NEED_CHECK_DATA_ROAMING";
            case 15:
                return "NEED_CHECK_IMS_WHEN_ROAMING";
            default:
                return "UNKNOWN_ITEM";
        }
    }
}
