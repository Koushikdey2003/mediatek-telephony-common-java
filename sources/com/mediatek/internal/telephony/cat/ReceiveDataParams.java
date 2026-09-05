package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.TextMessage;

/* JADX INFO: compiled from: BipCommandParams.java */
/* JADX INFO: loaded from: classes.dex */
class ReceiveDataParams extends CommandParams {
    int channelDataLength;
    int mReceiveDataCid;
    TextMessage textMsg;

    ReceiveDataParams(CommandDetails cmdDet, int length, int cid, TextMessage textMsg) {
        super(cmdDet);
        this.channelDataLength = 0;
        this.textMsg = new TextMessage();
        this.mReceiveDataCid = 0;
        this.channelDataLength = length;
        this.textMsg = textMsg;
        this.mReceiveDataCid = cid;
    }
}
