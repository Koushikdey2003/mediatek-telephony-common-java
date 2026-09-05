package com.mediatek.internal.telephony.uicc;

import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.CsimFileHandler;
import com.android.internal.telephony.uicc.UiccCardApplication;

/* JADX INFO: loaded from: classes.dex */
public final class MtkCsimFileHandler extends CsimFileHandler implements MtkIccConstants {
    static final String LOG_TAG = "MtkCsimFH";
    MtkIccFileHandler mMtkIccFh;

    public MtkCsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
        this.mMtkIccFh = null;
        this.mMtkIccFh = new MtkIccFileHandler(app, aid, ci);
    }

    protected String getEFPath(int efid) {
        logd("GetEFPath : " + efid);
        switch (efid) {
            case MtkIccConstants.EF_EST /* 28533 */:
                return "3F007FFF";
            default:
                return super.getEFPath(efid);
        }
    }

    protected String getCommonIccEFPath(int efid) {
        logd("getCommonIccEFPath : " + efid);
        switch (efid) {
            case 28645:
                return null;
            default:
                return super.getCommonIccEFPath(efid);
        }
    }

    public void loadEFLinearFixedAll(int fileid, Message onLoaded, boolean is7FFF) {
        this.mMtkIccFh.loadEFLinearFixedAllByPath(fileid, onLoaded, is7FFF);
    }

    public void loadEFLinearFixedAll(int fileid, int mode, Message onLoaded) {
        this.mMtkIccFh.loadEFLinearFixedAllByMode(fileid, mode, onLoaded);
    }

    public void loadEFTransparent(int fileid, String path, Message onLoaded) {
        this.mMtkIccFh.loadEFTransparent(fileid, path, onLoaded);
    }

    public void updateEFTransparent(int fileid, String path, byte[] data, Message onComplete) {
        this.mMtkIccFh.updateEFTransparent(fileid, path, data, onComplete);
    }

    public void readEFLinearFixed(int fileid, int recordNum, int recordSize, Message onLoaded) {
        this.mMtkIccFh.readEFLinearFixed(fileid, recordNum, recordSize, onLoaded);
    }

    public void selectEFFile(int fileid, Message onLoaded) {
        this.mMtkIccFh.selectEFFile(fileid, onLoaded);
    }

    protected void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    protected void loge(String msg) {
        Rlog.e(LOG_TAG, msg);
    }
}
