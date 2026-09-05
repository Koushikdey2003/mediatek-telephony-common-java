package com.mediatek.internal.telephony.uicc;

import com.android.internal.telephony.uicc.IccServiceTable;

/* JADX INFO: loaded from: classes.dex */
public final class IsimServiceTable extends IccServiceTable {

    public enum IsimService {
        PCSCF_ADDRESS,
        GBA,
        HTTP_DIGEST,
        GBA_LOCAL_KEY_ESTABLISHMENT,
        PCSCF_DISCOVERY,
        SMS,
        SMSR,
        SM_OVER_IP,
        COMMUNICATION_CONTROL_BY_ISIM,
        UICC_ACCESS_IMS
    }

    public IsimServiceTable(byte[] table) {
        super(table);
    }

    public boolean isAvailable(IsimService service) {
        return super.isAvailable(service.ordinal());
    }

    protected String getTag() {
        return "IsimServiceTable";
    }

    protected Object[] getValues() {
        return IsimService.values();
    }
}
