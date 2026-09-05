package com.mediatek.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.cat.Menu;

/* JADX INFO: loaded from: classes.dex */
public class MtkMenu extends Menu {
    public static final Parcelable.Creator<MtkMenu> CREATOR = new Parcelable.Creator<MtkMenu>() { // from class: com.mediatek.internal.telephony.cat.MtkMenu.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkMenu createFromParcel(Parcel in) {
            return new MtkMenu(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MtkMenu[] newArray(int size) {
            return new MtkMenu[size];
        }
    };
    public byte[] nextActionIndicator;

    public MtkMenu() {
        this.nextActionIndicator = null;
    }

    private MtkMenu(Parcel in) {
        super(in);
        int naiLen = in.readInt();
        if (naiLen <= 0) {
            this.nextActionIndicator = null;
            return;
        }
        byte[] bArr = new byte[naiLen];
        this.nextActionIndicator = bArr;
        in.readByteArray(bArr);
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        byte[] bArr = this.nextActionIndicator;
        dest.writeInt(bArr == null ? -1 : bArr.length);
        byte[] bArr2 = this.nextActionIndicator;
        if (bArr2 != null && bArr2.length > 0) {
            dest.writeByteArray(bArr2);
        }
    }
}
