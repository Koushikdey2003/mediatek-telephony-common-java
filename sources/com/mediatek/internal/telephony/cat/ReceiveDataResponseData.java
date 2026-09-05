package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cat.ResponseData;
import java.io.ByteArrayOutputStream;

/* JADX INFO: compiled from: BipResponseData.java */
/* JADX INFO: loaded from: classes.dex */
class ReceiveDataResponseData extends ResponseData {
    byte[] mData;
    int mRemainingCount;

    ReceiveDataResponseData(byte[] data, int remaining) {
        this.mData = null;
        this.mRemainingCount = 0;
        this.mData = data;
        this.mRemainingCount = remaining;
    }

    public void format(ByteArrayOutputStream buf) {
        if (buf == null) {
            return;
        }
        int tag = ComprehensionTlvTag.CHANNEL_DATA.value() | 128;
        buf.write(tag);
        byte[] bArr = this.mData;
        if (bArr != null) {
            if (bArr.length >= 128) {
                buf.write(129);
            }
            buf.write(this.mData.length);
            byte[] bArr2 = this.mData;
            buf.write(bArr2, 0, bArr2.length);
        } else {
            buf.write(0);
        }
        int tag2 = ComprehensionTlvTag.CHANNEL_DATA_LENGTH.value() | 128;
        buf.write(tag2);
        buf.write(1);
        MtkCatLog.d("[BIP]", "ReceiveDataResponseData: length: " + this.mRemainingCount);
        int i = this.mRemainingCount;
        if (i >= 255) {
            buf.write(255);
        } else {
            buf.write(i);
        }
    }
}
