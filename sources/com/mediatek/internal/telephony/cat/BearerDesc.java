package com.mediatek.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public abstract class BearerDesc implements Parcelable {
    public int bearerType = 0;

    @Override // android.os.Parcelable
    public abstract void writeToParcel(Parcel parcel, int i);

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
