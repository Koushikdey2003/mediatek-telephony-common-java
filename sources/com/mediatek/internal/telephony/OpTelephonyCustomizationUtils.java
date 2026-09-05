package com.mediatek.internal.telephony;

import android.content.Context;
import com.mediatek.common.util.OperatorCustomizationFactoryLoader;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OpTelephonyCustomizationUtils {
    static volatile OpTelephonyCustomizationFactoryBase sFactory;
    private static final List<OperatorCustomizationFactoryLoader.OperatorFactoryInfo> sOperatorFactoryInfoList;

    static {
        ArrayList arrayList = new ArrayList();
        sOperatorFactoryInfoList = arrayList;
        sFactory = null;
        arrayList.add(new OperatorCustomizationFactoryLoader.OperatorFactoryInfo("OP01Telephony.jar", "com.mediatek.op01.telephony.Op01TelephonyCustomizationFactory", (String) null, DataSubConstants.OPERATOR_OP01));
        arrayList.add(new OperatorCustomizationFactoryLoader.OperatorFactoryInfo("OP09CTelephony.jar", "com.mediatek.op09c.telephony.Op09CTelephonyCustomizationFactory", (String) null, DataSubConstants.OPERATOR_OP09, DataSubConstants.SEGC));
        arrayList.add(new OperatorCustomizationFactoryLoader.OperatorFactoryInfo("OP12Telephony.jar", "com.mediatek.op12.telephony.Op12TelephonyCustomizationFactory", (String) null, "OP12"));
    }

    public static OpTelephonyCustomizationFactoryBase getOpFactory(Context context) {
        OpTelephonyCustomizationFactoryBase opTelephonyCustomizationFactoryBase;
        synchronized (OpTelephonyCustomizationFactoryBase.class) {
            sFactory = (OpTelephonyCustomizationFactoryBase) OperatorCustomizationFactoryLoader.loadFactory(context, sOperatorFactoryInfoList);
            if (sFactory == null) {
                sFactory = new OpTelephonyCustomizationFactoryBase();
            }
            opTelephonyCustomizationFactoryBase = sFactory;
        }
        return opTelephonyCustomizationFactoryBase;
    }
}
