package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.TextMessage;

/* JADX INFO: compiled from: BipCommandParams.java */
/* JADX INFO: loaded from: classes.dex */
class GetChannelStatusParams extends CommandParams {
    TextMessage textMsg;

    GetChannelStatusParams(CommandDetails cmdDet, TextMessage textMsg) {
        super(cmdDet);
        this.textMsg = new TextMessage();
        this.textMsg = textMsg;
    }
}
