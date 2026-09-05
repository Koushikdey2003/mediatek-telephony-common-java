package com.mediatek.internal.telephony.phb;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class AlphaTag implements Parcelable {
    public static final Parcelable.Creator<AlphaTag> CREATOR = new Parcelable.Creator<AlphaTag>() { // from class: com.mediatek.internal.telephony.phb.AlphaTag.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AlphaTag createFromParcel(Parcel source) {
            int recordNumber = source.readInt();
            String alphaTag = source.readString();
            int pbrIndex = source.readInt();
            return new AlphaTag(recordNumber, alphaTag, pbrIndex);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AlphaTag[] newArray(int size) {
            return new AlphaTag[size];
        }
    };
    static final String LOG_TAG = "AlphaTag";
    String mAlphaTag;
    int mPbrIndex;
    int mRecordNumber;

    public AlphaTag(int recordNumber, String alphaTag, int pbr) {
        this.mAlphaTag = null;
        this.mRecordNumber = recordNumber;
        this.mAlphaTag = alphaTag;
        this.mPbrIndex = pbr;
    }

    public int getRecordIndex() {
        return this.mRecordNumber;
    }

    public String getAlphaTag() {
        return this.mAlphaTag;
    }

    public int getPbrIndex() {
        return this.mPbrIndex;
    }

    public void setRecordIndex(int nIndex) {
        this.mRecordNumber = nIndex;
    }

    public void setAlphaTag(String alphaString) {
        this.mAlphaTag = alphaString;
    }

    public void setPbrIndex(int pbr) {
        this.mPbrIndex = pbr;
    }

    public String toString() {
        return "AlphaTag: '" + this.mRecordNumber + "' '" + this.mAlphaTag + "' '" + this.mPbrIndex + "'";
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.mAlphaTag);
    }

    private static boolean stringCompareNullEqualsEmpty(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null) {
            s1 = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        return s1.equals(s2);
    }

    public boolean isEqual(AlphaTag uGas) {
        return stringCompareNullEqualsEmpty(this.mAlphaTag, uGas.mAlphaTag);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRecordNumber);
        dest.writeString(this.mAlphaTag);
        dest.writeInt(this.mPbrIndex);
    }
}
