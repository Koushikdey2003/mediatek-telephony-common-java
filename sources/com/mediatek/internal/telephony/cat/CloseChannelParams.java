package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.TextMessage;

/* JADX INFO: compiled from: BipCommandParams.java */
/* JADX INFO: loaded from: classes.dex */
class CloseChannelParams extends CommandParams {
    boolean mBackToTcpListen;
    int mCloseCid;
    TextMessage textMsg;

    CloseChannelParams(CommandDetails cmdDet, int cid, TextMessage textMsg, boolean backToTcpListen) {
        super(cmdDet);
        this.textMsg = new TextMessage();
        this.mCloseCid = 0;
        this.mBackToTcpListen = false;
        this.textMsg = textMsg;
        this.mCloseCid = cid;
        this.mBackToTcpListen = backToTcpListen;
    }
}
