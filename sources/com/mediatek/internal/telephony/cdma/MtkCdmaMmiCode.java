package com.mediatek.internal.telephony.cdma;

import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.cdma.CdmaMmiCode;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MtkCdmaMmiCode extends CdmaMmiCode {
    static final String LOG_TAG = "MtkCdmaMmiCode";
    private static final Map<OPID, List> mOpMap = new HashMap<OPID, List>() { // from class: com.mediatek.internal.telephony.cdma.MtkCdmaMmiCode.1
        {
            put(OPID.OP12, Arrays.asList("311480"));
            put(OPID.OP20, Arrays.asList("310120", "312530"));
        }
    };

    private enum OPID {
        OP12,
        OP20
    }

    protected MtkCdmaMmiCode(GsmCdmaPhone phone, UiccCardApplication app) {
        super(phone, app);
    }

    public static String getCallForwardingPrefixAndNumberWithMccMnc(int action, int reason, String number, String mccMnc) {
        String prefixWithNum = getCallForwardingPrefixAndNumber(action, reason, number);
        if (isOp(OPID.OP20, mccMnc)) {
            Rlog.d(LOG_TAG, "MtkCdmaMmiCode, Sprint code");
            switch (reason) {
                case 1:
                    if (action == 3) {
                        return "*74" + number;
                    }
                    if (action == 0) {
                        return "*740";
                    }
                    return prefixWithNum;
                case 2:
                    if (action == 3) {
                        return "*73" + number;
                    }
                    if (action == 0) {
                        return "*730";
                    }
                    return prefixWithNum;
                default:
                    return prefixWithNum;
            }
        }
        return prefixWithNum;
    }

    public static String getDisableAllCallForwardingOptionsPrefix(String mccMnc) {
        if (!isOp(OPID.OP12, mccMnc)) {
            return "*730";
        }
        Rlog.d(LOG_TAG, "MtkCdmaMmiCode, Vzw cancel all");
        return "*73";
    }

    private static boolean isOp(OPID id, String mccMnc) {
        if (!mOpMap.get(id).contains(mccMnc)) {
            return false;
        }
        return true;
    }
}
