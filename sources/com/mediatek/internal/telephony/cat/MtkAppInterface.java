package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.uicc.IccRecords;

/* JADX INFO: loaded from: classes.dex */
public interface MtkAppInterface extends AppInterface {
    public static final String MTK_CAT_CMD_ACTION = "com.mediatek.internal.stk.command";

    IccRecords getIccRecords();

    void onEventDownload(MtkCatResponseMessage mtkCatResponseMessage);
}
