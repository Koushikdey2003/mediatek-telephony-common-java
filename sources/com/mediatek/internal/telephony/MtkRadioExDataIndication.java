package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.SystemProperties;
import com.mediatek.internal.telephony.data.PlmnMvnoData;
import vendor.mediatek.hardware.mtkradioex.data.DedicateDataCall;
import vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication;
import vendor.mediatek.hardware.mtkradioex.data.PcoDataAttachedInfo;
import vendor.mediatek.hardware.mtkradioex.data.PlmnMvnoInfo;
import vendor.mediatek.hardware.mtkradioex.data.UrspRule;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExDataIndication extends IMtkRadioExDataIndication.Stub {
    private final MtkRIL mMtkRil;

    public MtkRadioExDataIndication(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void dedicatedBearerActivationInd(int type, DedicateDataCall ddcData) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void dedicatedBearerDeactivationInd(int type, int cid) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void dedicatedBearerModificationInd(int type, DedicateDataCall ddcData) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void mobileDataUsageInd(int type, int[] data) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.unsljLog(3133);
        if (this.mMtkRil.mMobileDataUsageRegistrants != null) {
            this.mMtkRil.mMobileDataUsageRegistrants.notifyRegistrants(new AsyncResult((Object) null, data, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void onDsdaChangedInd(int type, int mode) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.riljLog("onDsdaChangedInd: mode=" + mode);
        if (mode > 1) {
            SystemProperties.set("vendor.radio.dsda.state", mode + "");
        }
        if (this.mMtkRil.mDsdaStateRegistrant != null) {
            this.mMtkRil.mDsdaStateRegistrant.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(mode), (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void onMdDataRetryCountReset(int type) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.unsljLog(3059);
        if (this.mMtkRil.mMdDataRetryCountResetRegistrants != null) {
            this.mMtkRil.mMdDataRetryCountResetRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void onPlmnDataInd(int type, PlmnMvnoInfo plmnMvnoInfo) {
        this.mMtkRil.processIndication(1, type);
        PlmnMvnoData response = new PlmnMvnoData(plmnMvnoInfo.gsmPlmn, plmnMvnoInfo.cdmaPlmn, plmnMvnoInfo.gsmSpn, plmnMvnoInfo.cdmaSpn, plmnMvnoInfo.gsmImsi, plmnMvnoInfo.cdmaImsi, plmnMvnoInfo.gid1, plmnMvnoInfo.pnn, plmnMvnoInfo.impi);
        this.mMtkRil.unsljLog(3136);
        this.mMtkRil.notifyPlmnMvnoData(response);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void pcoDataAfterAttached(int type, PcoDataAttachedInfo pco) {
        this.mMtkRil.processIndication(1, type);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void networkRejectCauseInd(int type, int[] data) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.unsljLog(3109);
        if (this.mMtkRil.mNetworkRejectRegistrants.size() != 0) {
            this.mMtkRil.mNetworkRejectRegistrants.notifyRegistrants(new AsyncResult((Object) null, data, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void onNwLimitInd(int type, int[] state) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.unsljLog(3134);
        if (this.mMtkRil.mNwLimitRegistrants != null) {
            this.mMtkRil.mNwLimitRegistrants.notifyRegistrants(new AsyncResult((Object) null, state, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void qualifiedNetworkTypesChangedInd(int type, int[] data) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.unsljLog(3130);
        if (this.mMtkRil.mQualifiedNetworkTypesRegistrant != null) {
            this.mMtkRil.mQualifiedNetworkTypesRegistrant.notifyRegistrants(new AsyncResult((Object) null, data, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public void postUrspRule(int slotIndex, int type, String originalUrsp, UrspRule[] urspRules) {
        this.mMtkRil.processIndication(1, type);
        this.mMtkRil.riljLog("[postUrspRule] slotIndex:" + slotIndex + ", type: " + type + ", data: " + originalUrsp);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public String getInterfaceHash() {
        return "943612175caf84e3d94934844810892a1112b91e";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.data.IMtkRadioExDataIndication
    public int getInterfaceVersion() {
        return 2;
    }
}
