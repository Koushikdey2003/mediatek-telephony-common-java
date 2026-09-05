package com.mediatek.internal.telephony;

import android.hardware.radio.network.CellInfo;
import android.hardware.radio.network.CellInfoGsm;
import android.hardware.radio.network.CellInfoLte;
import android.hardware.radio.network.CellInfoNr;
import android.hardware.radio.network.CellInfoWcdma;
import android.hardware.radio.network.NetworkScanResult;
import android.hardware.radio.network.OperatorInfo;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.NetworkIndication;

/* JADX INFO: loaded from: classes.dex */
public class MtkNetworkIndication extends NetworkIndication {
    private MtkRIL mMtkRil;

    public MtkNetworkIndication(MtkRIL ril) {
        super(ril);
        this.mMtkRil = ril;
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void networkScanResult(int indicationType, NetworkScanResult result) {
        for (CellInfo record : result.networkInfos) {
            String mccmnc = null;
            switch (record.ratSpecificInfo.getTag()) {
                case 0:
                    CellInfoGsm cellInfoGsm = record.ratSpecificInfo.getGsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.mcc + cellInfoGsm.cellIdentityGsm.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.lac;
                    OperatorInfo operatorInfo = cellInfoGsm.cellIdentityGsm.operatorNames;
                    MtkRIL mtkRIL = this.mMtkRil;
                    operatorInfo.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, nLac);
                    OperatorInfo operatorInfo2 = cellInfoGsm.cellIdentityGsm.operatorNames;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    operatorInfo2.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (0 != 0) {
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    CellInfoWcdma cellInfoWcdma = record.ratSpecificInfo.getWcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.mcc + cellInfoWcdma.cellIdentityWcdma.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.lac;
                    OperatorInfo operatorInfo3 = cellInfoWcdma.cellIdentityWcdma.operatorNames;
                    MtkRIL mtkRIL3 = this.mMtkRil;
                    operatorInfo3.alphaLong = mtkRIL3.lookupOperatorNameForPlmnList(getSubId(mtkRIL3.mInstanceId.intValue()), mccmnc, true, nLac2);
                    OperatorInfo operatorInfo4 = cellInfoWcdma.cellIdentityWcdma.operatorNames;
                    MtkRIL mtkRIL4 = this.mMtkRil;
                    operatorInfo4.alphaShort = mtkRIL4.lookupOperatorNameForPlmnList(getSubId(mtkRIL4.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (0 != 0) {
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 2:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + record.ratSpecificInfo.getTag());
                case 3:
                    CellInfoLte cellInfoLte = record.ratSpecificInfo.getLte();
                    mccmnc = cellInfoLte.cellIdentityLte.mcc + cellInfoLte.cellIdentityLte.mnc;
                    int nLac3 = cellInfoLte.cellIdentityLte.tac;
                    OperatorInfo operatorInfo5 = cellInfoLte.cellIdentityLte.operatorNames;
                    MtkRIL mtkRIL5 = this.mMtkRil;
                    operatorInfo5.alphaLong = mtkRIL5.lookupOperatorNameForPlmnList(getSubId(mtkRIL5.mInstanceId.intValue()), mccmnc, true, nLac3);
                    OperatorInfo operatorInfo6 = cellInfoLte.cellIdentityLte.operatorNames;
                    MtkRIL mtkRIL6 = this.mMtkRil;
                    operatorInfo6.alphaShort = mtkRIL6.lookupOperatorNameForPlmnList(getSubId(mtkRIL6.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (0 != 0) {
                        cellInfoLte.cellIdentityLte.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    CellInfoNr cellInfoNr = record.ratSpecificInfo.getNr();
                    mccmnc = cellInfoNr.cellIdentityNr.mcc + cellInfoNr.cellIdentityNr.mnc;
                    int nTac = cellInfoNr.cellIdentityNr.tac;
                    OperatorInfo operatorInfo7 = cellInfoNr.cellIdentityNr.operatorNames;
                    MtkRIL mtkRIL7 = this.mMtkRil;
                    operatorInfo7.alphaLong = mtkRIL7.lookupOperatorNameForPlmnList(getSubId(mtkRIL7.mInstanceId.intValue()), mccmnc, true, nTac);
                    OperatorInfo operatorInfo8 = cellInfoNr.cellIdentityNr.operatorNames;
                    MtkRIL mtkRIL8 = this.mMtkRil;
                    operatorInfo8.alphaShort = mtkRIL8.lookupOperatorNameForPlmnList(getSubId(mtkRIL8.mInstanceId.intValue()), mccmnc, false, nTac);
                    if (0 != 0) {
                        cellInfoNr.cellIdentityNr.operatorNames.alphaLong = cellInfoNr.cellIdentityNr.operatorNames.alphaLong.concat(" 5G");
                        cellInfoNr.cellIdentityNr.operatorNames.alphaShort = cellInfoNr.cellIdentityNr.operatorNames.alphaShort.concat(" 5G");
                    }
                    break;
                case 5:
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult(indicationType, result);
    }
}
