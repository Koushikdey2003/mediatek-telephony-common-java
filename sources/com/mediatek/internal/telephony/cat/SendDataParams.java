package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.TextMessage;

/* JADX INFO: compiled from: BipCommandParams.java */
/* JADX INFO: loaded from: classes.dex */
class SendDataParams extends CommandParams {
    byte[] channelData;
    int mSendDataCid;
    int mSendMode;
    TextMessage textMsg;

    SendDataParams(CommandDetails cmdDet, byte[] data, int cid, TextMessage textMsg, int sendMode) {
        super(cmdDet);
        this.channelData = null;
        this.textMsg = new TextMessage();
        this.mSendDataCid = 0;
        this.mSendMode = 0;
        this.channelData = data;
        this.textMsg = textMsg;
        this.mSendDataCid = cid;
        this.mSendMode = sendMode;
    }
}
