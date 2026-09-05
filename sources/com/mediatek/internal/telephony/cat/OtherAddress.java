package com.mediatek.internal.telephony.cat;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* JADX INFO: loaded from: classes.dex */
public class OtherAddress {
    public InetAddress address;
    public int addressType;
    public byte[] rawAddress;

    public OtherAddress(int type, byte[] rawData, int index) throws UnknownHostException {
        this.addressType = 0;
        this.rawAddress = null;
        this.address = null;
        try {
            this.addressType = type;
            if (33 == type) {
                byte[] bArr = new byte[4];
                this.rawAddress = bArr;
                System.arraycopy(rawData, index, bArr, 0, bArr.length);
                this.address = InetAddress.getByAddress(this.rawAddress);
            } else if (87 != type) {
                MtkCatLog.e("[BIP]", "OtherAddress: unknown type: " + type);
            } else {
                byte[] bArr2 = new byte[16];
                this.rawAddress = bArr2;
                System.arraycopy(rawData, index, bArr2, 0, bArr2.length);
                this.address = InetAddress.getByAddress(this.rawAddress);
            }
        } catch (IndexOutOfBoundsException e) {
            MtkCatLog.d("[BIP]", "OtherAddress: out of bounds");
            this.rawAddress = null;
            this.address = null;
        } catch (UnknownHostException e2) {
            MtkCatLog.e("[BIP]", "OtherAddress: UnknownHostException");
            this.rawAddress = null;
            this.address = null;
        }
    }
}
