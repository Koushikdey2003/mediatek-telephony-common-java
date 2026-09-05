package com.mediatek.internal.telephony.uicc;

/* JADX INFO: loaded from: classes.dex */
public class EFResponseData {
    private static final int RESPONSE_DATA_FILE_STATUS = 11;
    private int mFileStatus;

    public EFResponseData(byte[] data) {
        this.mFileStatus = data[11] & 255;
    }

    public int getFileStatus() {
        return this.mFileStatus;
    }
}
