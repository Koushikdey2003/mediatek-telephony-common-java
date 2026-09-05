package com.mediatek.internal.telephony.data;

import com.android.internal.telephony.data.DataUtils;
import java.util.function.Function;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class MtkDataNetworkController$$ExternalSyntheticLambda2 implements Function {
    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Integer.valueOf(DataUtils.networkCapabilityToApnType(((Integer) obj).intValue()));
    }
}
