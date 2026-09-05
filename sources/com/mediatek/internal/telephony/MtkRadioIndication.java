package com.mediatek.internal.telephony;

import android.content.Intent;
import android.hardware.radio.V1_2.CellIdentityOperatorNames;
import android.hardware.radio.V1_2.CellInfo;
import android.hardware.radio.V1_2.CellInfoGsm;
import android.hardware.radio.V1_2.CellInfoLte;
import android.hardware.radio.V1_2.CellInfoWcdma;
import android.hardware.radio.V1_2.NetworkScanResult;
import android.hardware.radio.V1_5.CellInfoNr;
import android.os.UserHandle;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.RadioIndication;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioIndication extends RadioIndication {
    private static final String TAG = "MtkRadioInd";
    private MtkRIL mMtkRil;

    MtkRadioIndication(RIL ril) {
        super(ril);
        this.mMtkRil = (MtkRIL) ril;
    }

    public void rilConnected(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLog(1034);
        MtkRIL mtkRIL = this.mMtkRil;
        mtkRIL.setCdmaSubscriptionSource(mtkRIL.mCdmaSubscription, null);
        this.mMtkRil.notifyRegistrantsRilConnectionChanged(15);
    }

    public void radioStateChanged(int indicationType, int radioState) {
        int oldState = this.mMtkRil.getRadioState();
        super.radioStateChanged(indicationType, radioState);
        int newState = this.mMtkRil.getRadioState();
        if (newState != oldState) {
            Intent intent = new Intent("com.mediatek.intent.action.RADIO_STATE_CHANGED");
            int transferedState = RILUtils.convertHalRadioState(radioState);
            intent.putExtra("radioState", transferedState);
            intent.putExtra("subId", MtkSubscriptionManager.getSubIdUsingPhoneId(this.mMtkRil.mInstanceId.intValue()));
            this.mMtkRil.mMtkContext.sendBroadcastAsUser(intent, UserHandle.ALL);
            this.mMtkRil.riljLog("Broadcast for RadioStateChanged: state=" + transferedState);
        }
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public void networkScanResult_1_2(int indicationType, NetworkScanResult result) {
        Iterator<CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            CellInfo record = it.next();
            switch (record.cellInfoType) {
                case 1:
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) record.gsm.get(0);
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.mcc + cellInfoGsm.cellIdentityGsm.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames = cellInfoGsm.cellIdentityGsm.operatorNames;
                    MtkRIL mtkRIL = this.mMtkRil;
                    cellIdentityOperatorNames.alphaLong = mtkRIL.lookupOperatorName(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, nLac);
                    CellIdentityOperatorNames cellIdentityOperatorNames2 = cellInfoGsm.cellIdentityGsm.operatorNames;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    cellIdentityOperatorNames2.alphaShort = mtkRIL2.lookupOperatorName(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (0 != 0) {
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    CellInfoLte cellInfoLte = (CellInfoLte) record.lte.get(0);
                    mccmnc = cellInfoLte.cellIdentityLte.base.mcc + cellInfoLte.cellIdentityLte.base.mnc;
                    int nLac2 = cellInfoLte.cellIdentityLte.base.tac;
                    CellIdentityOperatorNames cellIdentityOperatorNames3 = cellInfoLte.cellIdentityLte.operatorNames;
                    MtkRIL mtkRIL3 = this.mMtkRil;
                    cellIdentityOperatorNames3.alphaLong = mtkRIL3.lookupOperatorName(getSubId(mtkRIL3.mInstanceId.intValue()), mccmnc, true, nLac2);
                    CellIdentityOperatorNames cellIdentityOperatorNames4 = cellInfoLte.cellIdentityLte.operatorNames;
                    MtkRIL mtkRIL4 = this.mMtkRil;
                    cellIdentityOperatorNames4.alphaShort = mtkRIL4.lookupOperatorName(getSubId(mtkRIL4.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (0 != 0) {
                        cellInfoLte.cellIdentityLte.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) record.wcdma.get(0);
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.mnc;
                    int nLac3 = cellInfoWcdma.cellIdentityWcdma.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames5 = cellInfoWcdma.cellIdentityWcdma.operatorNames;
                    MtkRIL mtkRIL5 = this.mMtkRil;
                    cellIdentityOperatorNames5.alphaLong = mtkRIL5.lookupOperatorName(getSubId(mtkRIL5.mInstanceId.intValue()), mccmnc, true, nLac3);
                    CellIdentityOperatorNames cellIdentityOperatorNames6 = cellInfoWcdma.cellIdentityWcdma.operatorNames;
                    MtkRIL mtkRIL6 = this.mMtkRil;
                    cellIdentityOperatorNames6.alphaShort = mtkRIL6.lookupOperatorName(getSubId(mtkRIL6.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (0 != 0) {
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + record.cellInfoType);
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_2(indicationType, result);
    }

    public void networkScanResult_1_4(int indicationType, android.hardware.radio.V1_4.NetworkScanResult result) {
        Iterator<android.hardware.radio.V1_4.CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            android.hardware.radio.V1_4.CellInfo record = it.next();
            switch (record.info.getDiscriminator()) {
                case 0:
                    CellInfoGsm cellInfoGsm = record.info.gsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.mcc + cellInfoGsm.cellIdentityGsm.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames = cellInfoGsm.cellIdentityGsm.operatorNames;
                    MtkRIL mtkRIL = this.mMtkRil;
                    cellIdentityOperatorNames.alphaLong = mtkRIL.lookupOperatorName(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, nLac);
                    CellIdentityOperatorNames cellIdentityOperatorNames2 = cellInfoGsm.cellIdentityGsm.operatorNames;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    cellIdentityOperatorNames2.alphaShort = mtkRIL2.lookupOperatorName(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (0 != 0) {
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    break;
                case 2:
                    CellInfoWcdma cellInfoWcdma = record.info.wcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames3 = cellInfoWcdma.cellIdentityWcdma.operatorNames;
                    MtkRIL mtkRIL3 = this.mMtkRil;
                    cellIdentityOperatorNames3.alphaLong = mtkRIL3.lookupOperatorName(getSubId(mtkRIL3.mInstanceId.intValue()), mccmnc, true, nLac2);
                    CellIdentityOperatorNames cellIdentityOperatorNames4 = cellInfoWcdma.cellIdentityWcdma.operatorNames;
                    MtkRIL mtkRIL4 = this.mMtkRil;
                    cellIdentityOperatorNames4.alphaShort = mtkRIL4.lookupOperatorName(getSubId(mtkRIL4.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (0 != 0) {
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 3:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + ((int) record.info.getDiscriminator()));
                case 4:
                    android.hardware.radio.V1_4.CellInfoLte cellInfoLte = record.info.lte();
                    mccmnc = cellInfoLte.base.cellIdentityLte.base.mcc + cellInfoLte.base.cellIdentityLte.base.mnc;
                    int nLac3 = cellInfoLte.base.cellIdentityLte.base.tac;
                    CellIdentityOperatorNames cellIdentityOperatorNames5 = cellInfoLte.base.cellIdentityLte.operatorNames;
                    MtkRIL mtkRIL5 = this.mMtkRil;
                    cellIdentityOperatorNames5.alphaLong = mtkRIL5.lookupOperatorName(getSubId(mtkRIL5.mInstanceId.intValue()), mccmnc, true, nLac3);
                    CellIdentityOperatorNames cellIdentityOperatorNames6 = cellInfoLte.base.cellIdentityLte.operatorNames;
                    MtkRIL mtkRIL6 = this.mMtkRil;
                    cellIdentityOperatorNames6.alphaShort = mtkRIL6.lookupOperatorName(getSubId(mtkRIL6.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (0 != 0) {
                        cellInfoLte.base.cellIdentityLte.operatorNames.alphaLong = cellInfoLte.base.cellIdentityLte.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.base.cellIdentityLte.operatorNames.alphaShort = cellInfoLte.base.cellIdentityLte.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_4(indicationType, result);
    }

    public void networkScanResult_1_5(int indicationType, android.hardware.radio.V1_5.NetworkScanResult result) {
        Iterator<android.hardware.radio.V1_5.CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            android.hardware.radio.V1_5.CellInfo record = it.next();
            switch (record.ratSpecificInfo.getDiscriminator()) {
                case 0:
                    android.hardware.radio.V1_5.CellInfoGsm cellInfoGsm = record.ratSpecificInfo.gsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.base.mcc + cellInfoGsm.cellIdentityGsm.base.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames = cellInfoGsm.cellIdentityGsm.base.operatorNames;
                    MtkRIL mtkRIL = this.mMtkRil;
                    cellIdentityOperatorNames.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, nLac);
                    CellIdentityOperatorNames cellIdentityOperatorNames2 = cellInfoGsm.cellIdentityGsm.base.operatorNames;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    cellIdentityOperatorNames2.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (0 != 0) {
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    android.hardware.radio.V1_5.CellInfoWcdma cellInfoWcdma = record.ratSpecificInfo.wcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.base.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.base.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames3 = cellInfoWcdma.cellIdentityWcdma.base.operatorNames;
                    MtkRIL mtkRIL3 = this.mMtkRil;
                    cellIdentityOperatorNames3.alphaLong = mtkRIL3.lookupOperatorNameForPlmnList(getSubId(mtkRIL3.mInstanceId.intValue()), mccmnc, true, nLac2);
                    CellIdentityOperatorNames cellIdentityOperatorNames4 = cellInfoWcdma.cellIdentityWcdma.base.operatorNames;
                    MtkRIL mtkRIL4 = this.mMtkRil;
                    cellIdentityOperatorNames4.alphaShort = mtkRIL4.lookupOperatorNameForPlmnList(getSubId(mtkRIL4.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (0 != 0) {
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 2:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + ((int) record.ratSpecificInfo.getDiscriminator()));
                case 3:
                    android.hardware.radio.V1_5.CellInfoLte cellInfoLte = record.ratSpecificInfo.lte();
                    mccmnc = cellInfoLte.cellIdentityLte.base.base.mcc + cellInfoLte.cellIdentityLte.base.base.mnc;
                    int nLac3 = cellInfoLte.cellIdentityLte.base.base.tac;
                    CellIdentityOperatorNames cellIdentityOperatorNames5 = cellInfoLte.cellIdentityLte.base.operatorNames;
                    MtkRIL mtkRIL5 = this.mMtkRil;
                    cellIdentityOperatorNames5.alphaLong = mtkRIL5.lookupOperatorNameForPlmnList(getSubId(mtkRIL5.mInstanceId.intValue()), mccmnc, true, nLac3);
                    CellIdentityOperatorNames cellIdentityOperatorNames6 = cellInfoLte.cellIdentityLte.base.operatorNames;
                    MtkRIL mtkRIL6 = this.mMtkRil;
                    cellIdentityOperatorNames6.alphaShort = mtkRIL6.lookupOperatorNameForPlmnList(getSubId(mtkRIL6.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (0 != 0) {
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    CellInfoNr cellInfoNr = record.ratSpecificInfo.nr();
                    mccmnc = cellInfoNr.cellIdentityNr.base.mcc + cellInfoNr.cellIdentityNr.base.mnc;
                    int nTac = cellInfoNr.cellIdentityNr.base.tac;
                    CellIdentityOperatorNames cellIdentityOperatorNames7 = cellInfoNr.cellIdentityNr.base.operatorNames;
                    MtkRIL mtkRIL7 = this.mMtkRil;
                    cellIdentityOperatorNames7.alphaLong = mtkRIL7.lookupOperatorNameForPlmnList(getSubId(mtkRIL7.mInstanceId.intValue()), mccmnc, true, nTac);
                    CellIdentityOperatorNames cellIdentityOperatorNames8 = cellInfoNr.cellIdentityNr.base.operatorNames;
                    MtkRIL mtkRIL8 = this.mMtkRil;
                    cellIdentityOperatorNames8.alphaShort = mtkRIL8.lookupOperatorNameForPlmnList(getSubId(mtkRIL8.mInstanceId.intValue()), mccmnc, false, nTac);
                    if (0 != 0) {
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong = cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong.concat(" 5G");
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort = cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort.concat(" 5G");
                    }
                    break;
                case 5:
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_5(indicationType, result);
    }

    public void networkScanResult_1_6(int indicationType, android.hardware.radio.V1_6.NetworkScanResult result) {
        Iterator<android.hardware.radio.V1_6.CellInfo> it = result.networkInfos.iterator();
        while (it.hasNext()) {
            String mccmnc = null;
            android.hardware.radio.V1_6.CellInfo record = it.next();
            switch (record.ratSpecificInfo.getDiscriminator()) {
                case 0:
                    android.hardware.radio.V1_5.CellInfoGsm cellInfoGsm = record.ratSpecificInfo.gsm();
                    mccmnc = cellInfoGsm.cellIdentityGsm.base.base.mcc + cellInfoGsm.cellIdentityGsm.base.base.mnc;
                    int nLac = cellInfoGsm.cellIdentityGsm.base.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames = cellInfoGsm.cellIdentityGsm.base.operatorNames;
                    MtkRIL mtkRIL = this.mMtkRil;
                    cellIdentityOperatorNames.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), mccmnc, true, nLac);
                    CellIdentityOperatorNames cellIdentityOperatorNames2 = cellInfoGsm.cellIdentityGsm.base.operatorNames;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    cellIdentityOperatorNames2.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), mccmnc, false, nLac);
                    if (0 != 0) {
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaLong.concat(" 2G");
                        cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort = cellInfoGsm.cellIdentityGsm.base.operatorNames.alphaShort.concat(" 2G");
                    }
                    break;
                case 1:
                    android.hardware.radio.V1_5.CellInfoWcdma cellInfoWcdma = record.ratSpecificInfo.wcdma();
                    mccmnc = cellInfoWcdma.cellIdentityWcdma.base.base.mcc + cellInfoWcdma.cellIdentityWcdma.base.base.mnc;
                    int nLac2 = cellInfoWcdma.cellIdentityWcdma.base.base.lac;
                    CellIdentityOperatorNames cellIdentityOperatorNames3 = cellInfoWcdma.cellIdentityWcdma.base.operatorNames;
                    MtkRIL mtkRIL3 = this.mMtkRil;
                    cellIdentityOperatorNames3.alphaLong = mtkRIL3.lookupOperatorNameForPlmnList(getSubId(mtkRIL3.mInstanceId.intValue()), mccmnc, true, nLac2);
                    CellIdentityOperatorNames cellIdentityOperatorNames4 = cellInfoWcdma.cellIdentityWcdma.base.operatorNames;
                    MtkRIL mtkRIL4 = this.mMtkRil;
                    cellIdentityOperatorNames4.alphaShort = mtkRIL4.lookupOperatorNameForPlmnList(getSubId(mtkRIL4.mInstanceId.intValue()), mccmnc, false, nLac2);
                    if (0 != 0) {
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaLong.concat(" 3G");
                        cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort = cellInfoWcdma.cellIdentityWcdma.base.operatorNames.alphaShort.concat(" 3G");
                    }
                    break;
                case 2:
                default:
                    throw new RuntimeException("unexpected cellinfotype: " + ((int) record.ratSpecificInfo.getDiscriminator()));
                case 3:
                    android.hardware.radio.V1_6.CellInfoLte cellInfoLte = record.ratSpecificInfo.lte();
                    mccmnc = cellInfoLte.cellIdentityLte.base.base.mcc + cellInfoLte.cellIdentityLte.base.base.mnc;
                    int nLac3 = cellInfoLte.cellIdentityLte.base.base.tac;
                    CellIdentityOperatorNames cellIdentityOperatorNames5 = cellInfoLte.cellIdentityLte.base.operatorNames;
                    MtkRIL mtkRIL5 = this.mMtkRil;
                    cellIdentityOperatorNames5.alphaLong = mtkRIL5.lookupOperatorNameForPlmnList(getSubId(mtkRIL5.mInstanceId.intValue()), mccmnc, true, nLac3);
                    CellIdentityOperatorNames cellIdentityOperatorNames6 = cellInfoLte.cellIdentityLte.base.operatorNames;
                    MtkRIL mtkRIL6 = this.mMtkRil;
                    cellIdentityOperatorNames6.alphaShort = mtkRIL6.lookupOperatorNameForPlmnList(getSubId(mtkRIL6.mInstanceId.intValue()), mccmnc, false, nLac3);
                    if (0 != 0) {
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong = cellInfoLte.cellIdentityLte.base.operatorNames.alphaLong.concat(" 4G");
                        cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort = cellInfoLte.cellIdentityLte.base.operatorNames.alphaShort.concat(" 4G");
                    }
                    break;
                case 4:
                    android.hardware.radio.V1_6.CellInfoNr cellInfoNr = record.ratSpecificInfo.nr();
                    mccmnc = cellInfoNr.cellIdentityNr.base.mcc + cellInfoNr.cellIdentityNr.base.mnc;
                    int nTac = cellInfoNr.cellIdentityNr.base.tac;
                    CellIdentityOperatorNames cellIdentityOperatorNames7 = cellInfoNr.cellIdentityNr.base.operatorNames;
                    MtkRIL mtkRIL7 = this.mMtkRil;
                    cellIdentityOperatorNames7.alphaLong = mtkRIL7.lookupOperatorNameForPlmnList(getSubId(mtkRIL7.mInstanceId.intValue()), mccmnc, true, nTac);
                    CellIdentityOperatorNames cellIdentityOperatorNames8 = cellInfoNr.cellIdentityNr.base.operatorNames;
                    MtkRIL mtkRIL8 = this.mMtkRil;
                    cellIdentityOperatorNames8.alphaShort = mtkRIL8.lookupOperatorNameForPlmnList(getSubId(mtkRIL8.mInstanceId.intValue()), mccmnc, false, nTac);
                    if (0 != 0) {
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong = cellInfoNr.cellIdentityNr.base.operatorNames.alphaLong.concat(" 5G");
                        cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort = cellInfoNr.cellIdentityNr.base.operatorNames.alphaShort.concat(" 5G");
                    }
                    break;
                case 5:
                    break;
            }
            if (this.mMtkRil.hidePLMN(mccmnc)) {
                it.remove();
                this.mMtkRil.mtkRiljLog("remove this one " + mccmnc);
            }
        }
        super.networkScanResult_1_6(indicationType, result);
    }
}
