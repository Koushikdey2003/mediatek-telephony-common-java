package com.mediatek.internal.telephony.data;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class PlmnMvnoData implements Parcelable {
    public static final Parcelable.Creator<PlmnMvnoData> CREATOR = new Parcelable.Creator() { // from class: com.mediatek.internal.telephony.data.PlmnMvnoData.1
        @Override // android.os.Parcelable.Creator
        public PlmnMvnoData createFromParcel(Parcel in) {
            return new PlmnMvnoData(in);
        }

        @Override // android.os.Parcelable.Creator
        public PlmnMvnoData[] newArray(int size) {
            return new PlmnMvnoData[size];
        }
    };
    private final String m_cdmaImsi;
    private final String m_cdmaPlmn;
    private final String m_cdmaSpn;
    private final String m_gid1;
    private final String m_gsmImsi;
    private final String m_gsmPlmn;
    private final String m_gsmSpn;
    private final String m_impi;
    private final String m_pnn;

    public PlmnMvnoData(String gsmPlmn, String cdmaPlmn, String gsmSpn, String cdmaSpn, String gsmImsi, String cdmaImsi, String gid1, String pnn, String impi) {
        this.m_gsmPlmn = gsmPlmn;
        this.m_cdmaPlmn = cdmaPlmn;
        this.m_gsmSpn = gsmSpn;
        this.m_cdmaSpn = cdmaSpn;
        this.m_gsmImsi = gsmImsi;
        this.m_cdmaImsi = cdmaImsi;
        this.m_gid1 = gid1;
        this.m_pnn = pnn;
        this.m_impi = impi;
    }

    public PlmnMvnoData(Parcel in) {
        this.m_gsmPlmn = in.readString();
        this.m_cdmaPlmn = in.readString();
        this.m_gsmSpn = in.readString();
        this.m_cdmaSpn = in.readString();
        this.m_gsmImsi = in.readString();
        this.m_cdmaImsi = in.readString();
        this.m_gid1 = in.readString();
        this.m_pnn = in.readString();
        this.m_impi = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.m_gsmPlmn);
        out.writeString(this.m_cdmaPlmn);
        out.writeString(this.m_gsmSpn);
        out.writeString(this.m_cdmaSpn);
        out.writeString(this.m_gsmImsi);
        out.writeString(this.m_cdmaImsi);
        out.writeString(this.m_gid1);
        out.writeString(this.m_pnn);
        out.writeString(this.m_impi);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "PlmnMvnoData(xxx)";
    }

    public String getGsmNumeric() {
        return this.m_gsmPlmn;
    }

    public String getCdmaNumeric() {
        return this.m_cdmaPlmn;
    }

    public String getGsmSpn() {
        return this.m_gsmSpn;
    }

    public String getCdmaSpn() {
        return this.m_cdmaSpn;
    }

    public String getGsmImsi() {
        return this.m_gsmImsi;
    }

    public String getCdmaImsi() {
        return this.m_cdmaImsi;
    }

    public String getGid1() {
        return this.m_gid1;
    }

    public String getPnn() {
        return this.m_pnn;
    }

    public String getImpi() {
        return this.m_impi;
    }
}
