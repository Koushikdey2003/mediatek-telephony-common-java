package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.Build;
import com.android.internal.telephony.uicc.IccUtils;
import vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExModemIndication extends IMtkRadioExModemIndication.Stub {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    MtkRIL mMtkRil;

    public MtkRadioExModemIndication(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void dsbpStateChanged(int indicationType, int dsbpState) {
        this.mMtkRil.processIndicationMtk(3, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3114);
        }
        this.mMtkRil.riljLog("dsbpStateChanged state: " + dsbpState);
        if (this.mMtkRil.mDsbpStateRegistrant != null) {
            this.mMtkRil.mDsbpStateRegistrant.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(dsbpState), (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void eMBMSAtInfoIndication(int indicationType, String info) {
        this.mMtkRil.processIndication(3, indicationType);
        String response = new String(info);
        this.mMtkRil.unsljLogRet(3055, response);
        if (this.mMtkRil.mEmbmsAtInfoNotificationRegistrant.size() > 0) {
            this.mMtkRil.riljLog("Notify mEmbmsAtInfoNotificationRegistrant");
            this.mMtkRil.mEmbmsAtInfoNotificationRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        } else {
            this.mMtkRil.riljLog("No mEmbmsAtInfoNotificationRegistrant exist");
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void eMBMSSessionStatusIndication(int indicationType, int status) {
        this.mMtkRil.processIndication(3, indicationType);
        int[] response = {status};
        this.mMtkRil.unsljLogRet(3054, null);
        if (this.mMtkRil.mEmbmsSessionStatusNotificationRegistrant.size() > 0) {
            this.mMtkRil.riljLog("Notify mEmbmsSessionStatusNotificationRegistrant");
            this.mMtkRil.mEmbmsSessionStatusNotificationRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        } else {
            this.mMtkRil.riljLog("No mEmbmsSessionStatusNotificationRegistrant exist");
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void oemHookRaw(int indicationType, byte[] data) {
        this.mMtkRil.processIndication(3, indicationType);
        this.mMtkRil.unsljLogvRet(1028, IccUtils.bytesToHexString(data));
        if (this.mMtkRil.mUnsolOemHookRegistrant != null) {
            this.mMtkRil.mUnsolOemHookRegistrant.notifyRegistrant(new AsyncResult((Object) null, data, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void onTxPowerIndication(int indicationType, int[] indPower) {
        this.mMtkRil.processIndication(3, indicationType);
        if (this.mMtkRil.mTxPowerRegistrant != null) {
            this.mMtkRil.mTxPowerRegistrant.notifyRegistrants(new AsyncResult((Object) null, indPower, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void onTxPowerStatusIndication(int indicationType, int[] indPower) {
        this.mMtkRil.processIndication(3, indicationType);
        if (this.mMtkRil.mTxPowerStatusRegistrant != null) {
            this.mMtkRil.mTxPowerStatusRegistrant.notifyRegistrants(new AsyncResult((Object) null, indPower, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void worldModeChangedIndication(int type, int[] modes) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public void onCellularQualityChangedInd(int type, int[] indStgs) {
        int CellularQualityType;
        this.mMtkRil.processIndication(3, type);
        this.mMtkRil.unsljLog(3132);
        int[] data = new int[indStgs.length];
        for (int i = 0; i < indStgs.length; i++) {
            data[i] = indStgs[i];
        }
        switch (data[0]) {
            case 0:
                CellularQualityType = 0;
                break;
            case 1:
                CellularQualityType = 5;
                break;
            case 2:
                CellularQualityType = 2;
                break;
            case 3:
                CellularQualityType = 6;
                break;
            case 4:
                CellularQualityType = 7;
                break;
            default:
                CellularQualityType = 0;
                break;
        }
        this.mMtkRil.riljLog("RIL_UNSOL_IWLAN_CELLULAR_QUALITY_CHANGED_IND type:" + CellularQualityType + " , value = " + data[1]);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public String getInterfaceHash() {
        return "87512b9a1978fdb596a8d9176854d3761382dc82";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.modem.IMtkRadioExModemIndication
    public int getInterfaceVersion() {
        return 2;
    }
}
