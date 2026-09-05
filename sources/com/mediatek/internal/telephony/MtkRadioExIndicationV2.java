package com.mediatek.internal.telephony;

import android.content.Intent;
import android.hardware.radio.V1_0.SuppSvcNotification;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Parcelable;
import android.os.SystemProperties;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.uicc.IccUtils;
import com.mediatek.internal.telephony.data.PlmnMvnoData;
import com.mediatek.internal.telephony.gsm.MtkSuppCrssNotification;
import com.mediatek.internal.telephony.gsm.MtkSuppServiceNotification;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V2_0.CfuStatusNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.CipherNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.CrssNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.IncomingCallNotification;
import vendor.mediatek.hardware.mtkradioex.V2_0.PcoDataAttachedInfo;
import vendor.mediatek.hardware.mtkradioex.V2_0.PlmnMvnoInfo;
import vendor.mediatek.hardware.mtkradioex.V2_0.SignalStrengthWithWcdmaEcio;
import vendor.mediatek.hardware.mtkradioex.V2_0.VsimOperationEvent;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExIndicationV2 extends MtkRadioExIndicationBaseV2 {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static final String TAG = "MtkRadioInd";
    private MtkRIL mMtkRil;

    MtkRadioExIndicationV2(RIL ril) {
        super(ril);
        this.mMtkRil = (MtkRIL) ril;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void cfuStatusNotify(int indicationType, CfuStatusNotification cfuStatus) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] notification = {cfuStatus.status, cfuStatus.lineId};
        this.mMtkRil.unsljLogRet(3070, notification);
        if (notification[1] == 1) {
            this.mMtkRil.mCfuReturnValue = notification;
        }
        if (this.mMtkRil.mCallForwardingInfoRegistrants.size() != 0 && notification[1] == 1) {
            this.mMtkRil.mCallForwardingInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void incomingCallIndication(int indicationType, IncomingCallNotification inCallNotify) {
        this.mMtkRil.processIndication(0, indicationType);
        String[] notification = new String[7];
        notification[0] = inCallNotify.callId;
        notification[1] = inCallNotify.number;
        notification[2] = inCallNotify.type;
        notification[3] = inCallNotify.callMode;
        notification[4] = inCallNotify.seqNo;
        notification[5] = inCallNotify.redirectNumber;
        this.mMtkRil.unsljLogRet(3015, null);
        if (this.mMtkRil.mIncomingCallIndicationRegistrant != null) {
            this.mMtkRil.mIncomingCallIndicationRegistrant.notifyRegistrant(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void callAdditionalInfoInd(int indicationType, int ciType, ArrayList<String> info) {
        this.mMtkRil.processIndication(0, indicationType);
        String[] notification = new String[info.size() + 1];
        notification[0] = Integer.toString(ciType);
        for (int i = 0; i < info.size(); i++) {
            notification[i + 1] = info.get(i);
        }
        this.mMtkRil.unsljLogRet(3126, null);
        if (this.mMtkRil.mCallAdditionalInfoRegistrants != null) {
            this.mMtkRil.mCallAdditionalInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void cipherIndication(int indicationType, CipherNotification cipherNotify) {
        this.mMtkRil.processIndication(0, indicationType);
        String[] notification = {cipherNotify.simCipherStatus, cipherNotify.sessionStatus, cipherNotify.csStatus, cipherNotify.psStatus};
        this.mMtkRil.unsljLogRet(3024, notification);
        if (this.mMtkRil.mCipherIndicationRegistrants != null) {
            this.mMtkRil.mCipherIndicationRegistrants.notifyRegistrants(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void suppSvcNotifyEx(int i, SuppSvcNotification suppSvcNotification) {
        this.mMtkRil.processIndication(0, i);
        MtkSuppServiceNotification mtkSuppServiceNotification = new MtkSuppServiceNotification();
        mtkSuppServiceNotification.notificationType = suppSvcNotification.isMT ? 1 : 0;
        mtkSuppServiceNotification.code = suppSvcNotification.code;
        mtkSuppServiceNotification.index = suppSvcNotification.index;
        mtkSuppServiceNotification.type = suppSvcNotification.type;
        mtkSuppServiceNotification.number = suppSvcNotification.number;
        this.mMtkRil.unsljLogRet(3026, null);
        if (this.mMtkRil.mSsnExRegistrant != null) {
            this.mMtkRil.mSsnExRegistrant.notifyRegistrant(new AsyncResult((Object) null, mtkSuppServiceNotification, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void crssIndication(int indicationType, CrssNotification crssNotification) {
        this.mMtkRil.processIndication(0, indicationType);
        MtkSuppCrssNotification notification = new MtkSuppCrssNotification();
        notification.code = crssNotification.code;
        notification.type = crssNotification.type;
        notification.alphaid = crssNotification.alphaid;
        notification.number = crssNotification.number;
        notification.cli_validity = crssNotification.cli_validity;
        this.mMtkRil.unsljLogRet(3025, null);
        if (this.mMtkRil.mCallRelatedSuppSvcRegistrant != null) {
            this.mMtkRil.mCallRelatedSuppSvcRegistrant.notifyRegistrant(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void eccNumIndication(int indicationType, String eccListWithCard, String eccListNoCard) {
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responseCsNetworkStateChangeInd(int indicationType, ArrayList<String> state) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.riljLog("[UNSL]< UNSOL_RESPONSE_CS_NETWORK_STATE_CHANGED");
        if (this.mMtkRil.mCsNetworkStateRegistrants.size() != 0) {
            this.mMtkRil.mCsNetworkStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, state.toArray(new String[state.size()]), (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responsePsNetworkStateChangeInd(int indicationType, ArrayList<Integer> state) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.riljLog("[UNSL]< UNSOL_RESPONSE_PS_NETWORK_STATE_CHANGED");
        int[] response = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            response[i] = state.get(i).intValue();
        }
        if (this.mMtkRil.mPsNetworkStateRegistrants.size() != 0) {
            this.mMtkRil.mPsNetworkStateRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responseNetworkEventInd(int indicationType, ArrayList<Integer> event) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[event.size()];
        for (int i = 0; i < event.size(); i++) {
            response[i] = event.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3018, response);
        if (this.mMtkRil.mNetworkEventRegistrants.size() != 0) {
            this.mMtkRil.mNetworkEventRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void networkRejectCauseInd(int indicationType, ArrayList<Integer> event) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[event.size()];
        for (int i = 0; i < event.size(); i++) {
            response[i] = event.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3109, response);
        if (this.mMtkRil.mNetworkRejectRegistrants.size() != 0) {
            this.mMtkRil.mNetworkRejectRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responseModulationInfoInd(int indicationType, ArrayList<Integer> data) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            response[i] = data.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3019, response);
        if (this.mMtkRil.mModulationRegistrants.size() != 0) {
            this.mMtkRil.mModulationRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responseInvalidSimInd(int indicationType, ArrayList<String> state) {
        this.mMtkRil.processIndication(0, indicationType);
        String[] ret = (String[]) state.toArray(new String[state.size()]);
        this.mMtkRil.unsljLogRet(3016, ret);
        if (this.mMtkRil.mInvalidSimInfoRegistrant.size() != 0) {
            this.mMtkRil.mInvalidSimInfoRegistrant.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responseFemtocellInfo(int indicationType, ArrayList<String> info) {
        this.mMtkRil.processIndication(0, indicationType);
        String[] response = (String[]) info.toArray(new String[info.size()]);
        this.mMtkRil.unsljLogRet(3029, response);
        if (this.mMtkRil.mFemtoCellInfoRegistrants.size() != 0) {
            this.mMtkRil.mFemtoCellInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void currentSignalStrengthWithWcdmaEcioInd(int indicationType, SignalStrengthWithWcdmaEcio signalStrength) {
        this.mMtkRil.processIndication(0, indicationType);
        SignalStrength ss = new SignalStrength();
        this.mMtkRil.unsljLogRet(3097, ss);
        this.mMtkRil.riljLog("currentSignalStrengthWithWcdmaEcioInd SignalStrength=" + ss);
        if (this.mMtkRil.mSignalStrengthWithWcdmaEcioRegistrants.size() != 0) {
            this.mMtkRil.mSignalStrengthWithWcdmaEcioRegistrants.notifyRegistrants(new AsyncResult((Object) null, ss, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void responseLteNetworkInfo(int indicationType, int info) {
        this.mMtkRil.riljLog("[UNSL]< RIL_UNSOL_LTE_NETWORK_INFO " + info);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onMccMncChanged(int indicationType, String mccmnc) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLogRet(3096, mccmnc);
        if (this.mMtkRil.mMccMncRegistrants.size() != 0) {
            this.mMtkRil.mMccMncRegistrants.notifyRegistrants(new AsyncResult((Object) null, mccmnc, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2
    public void onVirtualSimOn(int indicationType, int simInserted) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3005);
        }
        if (this.mMtkRil.mVirtualSimOn != null) {
            this.mMtkRil.mVirtualSimOn.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(simInserted), (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2
    public void onVirtualSimOff(int indicationType, int simInserted) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3006);
        }
        if (this.mMtkRil.mVirtualSimOff != null) {
            this.mMtkRil.mVirtualSimOn.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(simInserted), (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onVirtualSimStatusChanged(int indicationType, int simInserted) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3129);
        }
        if (simInserted == 0 && this.mMtkRil.mVirtualSimOff != null) {
            this.mMtkRil.mVirtualSimOff.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(simInserted), (Throwable) null));
        } else if (simInserted == 1 && this.mMtkRil.mVirtualSimOn != null) {
            this.mMtkRil.mVirtualSimOn.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(simInserted), (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onImeiLock(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3007);
        }
        if (this.mMtkRil.mImeiLockRegistrant != null) {
            this.mMtkRil.mImeiLockRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onImsiRefreshDone(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3008);
        }
        if (this.mMtkRil.mImsiRefreshDoneRegistrant != null) {
            this.mMtkRil.mImsiRefreshDoneRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onCardDetectedInd(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        boolean z = ENG;
        if (z) {
            this.mMtkRil.unsljLog(3125);
        }
        if (this.mMtkRil.mCardDetectedIndRegistrant.size() != 0) {
            this.mMtkRil.mCardDetectedIndRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            return;
        }
        if (z) {
            this.mMtkRil.riljLog("Cache card detected event");
        }
        this.mMtkRil.mIsCardDetected = true;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void meSmsStorageFullInd(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3011);
        }
        if (this.mMtkRil.mMeSmsFullRegistrant != null) {
            this.mMtkRil.mMeSmsFullRegistrant.notifyRegistrant();
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void smsReadyInd(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        boolean z = ENG;
        if (z) {
            this.mMtkRil.unsljLog(3012);
        }
        if (this.mMtkRil.mSmsReadyRegistrants.size() != 0) {
            this.mMtkRil.mSmsReadyRegistrants.notifyRegistrants();
            return;
        }
        if (z) {
            this.mMtkRil.riljLog("Cache sms ready event");
        }
        this.mMtkRil.mIsSmsReady = true;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void smsInfoExtInd(int indicationType, String info) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLogRet(3137, info);
        if (this.mMtkRil.mSmsInfoExtRegistrants.size() != 0) {
            this.mMtkRil.mSmsInfoExtRegistrants.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onPseudoCellInfoInd(int indicationType, ArrayList<Integer> info) {
        Parcelable pseudoCellInfo;
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3017);
        }
        int[] response = new int[info.size()];
        for (int i = 0; i < info.size(); i++) {
            response[i] = info.get(i).intValue();
        }
        String property = String.format("persist.vendor.radio.apc.mode%d", this.mMtkRil.mInstanceId);
        String propStr = SystemProperties.get(property, "0");
        int index = propStr.indexOf("=");
        if (index != -1) {
            String subStr = propStr.substring(index + 1);
            String[] settings = subStr.split(",");
            int mode = Integer.parseInt(settings[0]);
            int report = Integer.parseInt(settings[1]);
            boolean enable = report == 1;
            int interval = Integer.parseInt(settings[2]);
            pseudoCellInfo = new PseudoCellInfo(mode, enable, interval, response);
        } else {
            pseudoCellInfo = new PseudoCellInfo(0, false, 0, response);
        }
        if (this.mMtkRil.mPseudoCellInfoRegistrants != null) {
            this.mMtkRil.mPseudoCellInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, pseudoCellInfo, (Throwable) null));
        }
        Intent intent = new Intent("com.mediatek.phone.ACTION_APC_INFO_NOTIFY");
        intent.putExtra("phoneId", this.mMtkRil.mInstanceId);
        intent.putExtra("info", pseudoCellInfo);
        this.mMtkRil.mMtkContext.sendBroadcast(intent);
        this.mMtkRil.riljLog("Broadcast for APC info:cellInfo=" + pseudoCellInfo.toString());
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void eMBMSSessionStatusIndication(int indicationType, int status) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = {status};
        this.mMtkRil.unsljLogRet(3054, null);
        if (this.mMtkRil.mEmbmsSessionStatusNotificationRegistrant.size() > 0) {
            this.mMtkRil.riljLog("Notify mEmbmsSessionStatusNotificationRegistrant");
            this.mMtkRil.mEmbmsSessionStatusNotificationRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        } else {
            this.mMtkRil.riljLog("No mEmbmsSessionStatusNotificationRegistrant exist");
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onSimHotSwapInd(int indicationType, int event, String info) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.riljLog("onSimHotSwapInd event: " + event + " info: " + info);
        }
        switch (event) {
            case 0:
                if (this.mMtkRil.mSimPlugIn != null) {
                    this.mMtkRil.mSimPlugIn.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
                }
                break;
            case 1:
                if (this.mMtkRil.mSimPlugOut != null) {
                    this.mMtkRil.mSimPlugOut.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
                }
                break;
            case 2:
                if (this.mMtkRil.mSimRecovery != null) {
                    this.mMtkRil.mSimRecovery.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
                }
                break;
            case 3:
                if (this.mMtkRil.mSimMissing != null) {
                    this.mMtkRil.mSimMissing.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
                }
                break;
            default:
                this.mMtkRil.riljLog("onSimHotSwapInd Invalid event!");
                break;
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onSimPowerChangedInd(int indicationType, ArrayList<Integer> info) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3124);
        }
        int[] response = new int[info.size()];
        for (int i = 0; i < info.size(); i++) {
            response[i] = info.get(i).intValue();
        }
        this.mMtkRil.mSimPowerInfo = response;
        if (this.mMtkRil.mSimPowerChanged.size() != 0) {
            this.mMtkRil.mSimPowerChanged.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void smlSlotLockInfoChangedInd(int indicationType, ArrayList<Integer> info) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3115);
        }
        int[] response = new int[info.size()];
        for (int i = 0; i < info.size(); i++) {
            response[i] = info.get(i).intValue();
        }
        this.mMtkRil.mSmlSlotLockInfo = response;
        if (this.mMtkRil.mSmlSlotLockInfoChanged.size() != 0) {
            this.mMtkRil.mSmlSlotLockInfoChanged.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onRsuSimLockEvent(int indicationType, int eventId) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.riljLog("[RSU-SIMLOCK] onRsuSimLockEvent eventId " + eventId);
        if (ENG) {
            this.mMtkRil.unsljLog(3128);
        }
        int[] response = {eventId};
        if (this.mMtkRil.mRsuSimlockRegistrants != null) {
            this.mMtkRil.mRsuSimlockRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void eMBMSAtInfoIndication(int indicationType, String info) {
        this.mMtkRil.processIndication(0, indicationType);
        String response = new String(info);
        this.mMtkRil.unsljLogRet(3055, response);
        if (this.mMtkRil.mEmbmsAtInfoNotificationRegistrant.size() > 0) {
            this.mMtkRil.riljLog("Notify mEmbmsAtInfoNotificationRegistrant");
            this.mMtkRil.mEmbmsAtInfoNotificationRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        } else {
            this.mMtkRil.riljLog("No mEmbmsAtInfoNotificationRegistrant exist");
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void plmnChangedIndication(int indicationType, ArrayList<String> plmns) {
        this.mMtkRil.processIndication(0, indicationType);
        String[] response = new String[plmns.size()];
        for (int i = 0; i < plmns.size(); i++) {
            response[i] = plmns.get(i);
        }
        this.mMtkRil.unsljLogRet(3000, response);
        synchronized (this.mMtkRil.mWPMonitor) {
            if (this.mMtkRil.mPlmnChangeNotificationRegistrant.size() <= 0) {
                this.mMtkRil.mEcopsReturnValue = response;
            } else {
                this.mMtkRil.riljLog("ECOPS,notify mPlmnChangeNotificationRegistrant");
                this.mMtkRil.mPlmnChangeNotificationRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
            }
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void registrationSuspendedIndication(int indicationType, ArrayList<Integer> sessionIds) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[sessionIds.size()];
        for (int i = 0; i < sessionIds.size(); i++) {
            response[i] = sessionIds.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3001, response);
        synchronized (this.mMtkRil.mWPMonitor) {
            if (this.mMtkRil.mRegistrationSuspendedRegistrant == null) {
                this.mMtkRil.mEmsrReturnValue = response;
            } else {
                this.mMtkRil.riljLog("EMSR, notify mRegistrationSuspendedRegistrant");
                this.mMtkRil.mRegistrationSuspendedRegistrant.notifyRegistrant(new AsyncResult((Object) null, response, (Throwable) null));
            }
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void gmssRatChangedIndication(int indicationType, ArrayList<Integer> gmsss) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[gmsss.size()];
        for (int i = 0; i < gmsss.size(); i++) {
            response[i] = gmsss.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3003, response);
        int[] rat = response;
        if (this.mMtkRil.mGmssRatChangedRegistrant != null) {
            this.mMtkRil.mGmssRatChangedRegistrant.notifyRegistrants(new AsyncResult((Object) null, rat, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void worldModeChangedIndication(int indicationType, ArrayList<Integer> modes) {
        boolean retvalue;
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[modes.size()];
        for (int i = 0; i < modes.size(); i++) {
            response[i] = modes.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3022, response);
        int state = response[0];
        if (state == 2) {
            retvalue = WorldMode.resetSwitchingState(state);
            state = 1;
        } else if (state == 0) {
            retvalue = WorldMode.updateSwitchingState(true);
        } else {
            retvalue = WorldMode.updateSwitchingState(false);
        }
        if (!retvalue) {
            return;
        }
        Intent intent = new Intent(WorldMode.ACTION_WORLD_MODE_CHANGED);
        intent.putExtra(WorldMode.EXTRA_WORLD_MODE_CHANGE_STATE, Integer.valueOf(state));
        this.mMtkRil.mMtkContext.sendBroadcast(intent);
        this.mMtkRil.riljLog("Broadcast for WorldModeChanged: state=" + state);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void resetAttachApnInd(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3020);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void mdChangedApnInd(int indicationType, int apnClassType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3021);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void esnMeidChangeInd(int indicationType, String esnMeid) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3023);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void phbReadyNotification(int indicationType, int isPhbReady) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = {isPhbReady};
        this.mMtkRil.unsljLogMore(3028, "phbReadyNotification: " + isPhbReady);
        if (this.mMtkRil.mPhbReadyRegistrants != null) {
            this.mMtkRil.mPhbReadyRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void bipProactiveCommand(int indicationType, String cmd) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3057);
        }
        if (this.mMtkRil.mBipProCmdRegistrant != null) {
            this.mMtkRil.mBipProCmdRegistrant.notifyRegistrants(new AsyncResult((Object) null, cmd, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void triggerOtaSP(int indicationType) {
        String[] testTriggerOtasp = {"AT+CDV=*22899", "", "DESTRILD:C2K"};
        this.mMtkRil.invokeOemRilRequestStrings(testTriggerOtasp, null);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void pcoDataAfterAttached(int indicationType, PcoDataAttachedInfo pco) {
        this.mMtkRil.processIndication(0, indicationType);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onStkMenuReset(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3071);
        }
        if (this.mMtkRil.mStkSetupMenuResetRegistrant != null) {
            this.mMtkRil.mStkSetupMenuResetRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void networkInfoInd(int indicationType, ArrayList<String> networkinfo) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLogMore(3030, "networkInfo: " + networkinfo);
        String[] ret = (String[]) networkinfo.toArray(new String[networkinfo.size()]);
        if (this.mMtkRil.mNetworkInfoRegistrant.size() != 0) {
            this.mMtkRil.mNetworkInfoRegistrant.notifyRegistrants(new AsyncResult((Object) null, ret, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onMdDataRetryCountReset(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3059);
        }
        if (this.mMtkRil.mMdDataRetryCountResetRegistrants != null) {
            this.mMtkRil.mMdDataRetryCountResetRegistrants.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onRemoveRestrictEutran(int indicationType) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3060);
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void confSRVCC(int indicationType, ArrayList<Integer> callIds) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLog(3072);
        int[] response = new int[callIds.size()];
        for (int i = 0; i < callIds.size(); i++) {
            response[i] = callIds.get(i).intValue();
        }
        this.mMtkRil.mEconfSrvccRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onVsimEventIndication(int indicationType, VsimOperationEvent event) {
        this.mMtkRil.processIndication(0, indicationType);
        int length = event.dataLength > 0 ? (event.dataLength / 2) + 4 : 0;
        ExternalSimManager.VsimEvent indicationEvent = new ExternalSimManager.VsimEvent(event.transactionId, event.eventId, length, 1 << this.mMtkRil.mInstanceId.intValue());
        if (length > 0) {
            indicationEvent.putInt(event.dataLength / 2);
            indicationEvent.putBytes(IccUtils.hexStringToBytes(event.data));
        }
        if (ENG) {
            this.mMtkRil.unsljLogRet(3074, indicationEvent.toString());
        }
        if (this.mMtkRil.mVsimIndicationRegistrants != null) {
            this.mMtkRil.mVsimIndicationRegistrants.notifyRegistrants(new AsyncResult((Object) null, indicationEvent, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void oemHookRaw(int indicationType, ArrayList<Byte> data) {
        this.mMtkRil.processIndication(0, indicationType);
        byte[] response = RILUtils.arrayListToPrimitiveArray(data);
        this.mMtkRil.unsljLogvRet(1028, IccUtils.bytesToHexString(response));
        if (this.mMtkRil.mUnsolOemHookRegistrant != null) {
            this.mMtkRil.mUnsolOemHookRegistrant.notifyRegistrant(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onTxPowerIndication(int indicationType, ArrayList<Integer> txPower) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[txPower.size()];
        for (int i = 0; i < txPower.size(); i++) {
            response[i] = txPower.get(i).intValue();
        }
        if (this.mMtkRil.mTxPowerRegistrant != null) {
            this.mMtkRil.mTxPowerRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onTxPowerStatusIndication(int indicationType, ArrayList<Integer> txPower) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[txPower.size()];
        for (int i = 0; i < txPower.size(); i++) {
            response[i] = txPower.get(i).intValue();
        }
        if (this.mMtkRil.mTxPowerStatusRegistrant != null) {
            this.mMtkRil.mTxPowerStatusRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void dsbpStateChanged(int indicationType, int dsbpState) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3114);
        }
        this.mMtkRil.riljLog("dsbpStateChanged state: " + dsbpState);
        if (this.mMtkRil.mDsbpStateRegistrant != null) {
            this.mMtkRil.mDsbpStateRegistrant.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(dsbpState), (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onDsdaChangedInd(int indicationType, int mode) {
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3131);
        }
        this.mMtkRil.riljLog("onDsdaChangedInd: mode=" + mode);
        if (mode > 1) {
            SystemProperties.set("vendor.radio.dsda.state", mode + "");
        }
        if (this.mMtkRil.mDsdaStateRegistrant != null) {
            this.mMtkRil.mDsdaStateRegistrant.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(mode), (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void qualifiedNetworkTypesChangedInd(int indicationType, ArrayList<Integer> data) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            response[i] = data.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3130, response);
        if (this.mMtkRil.mQualifiedNetworkTypesRegistrant != null) {
            this.mMtkRil.mQualifiedNetworkTypesRegistrant.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onCellularQualityChangedInd(int indicationType, ArrayList<Integer> indStgs) {
        int CellularQualityType;
        this.mMtkRil.processIndication(0, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3132);
        }
        int[] data = new int[indStgs.size()];
        for (int i = 0; i < indStgs.size(); i++) {
            data[i] = indStgs.get(i).intValue();
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

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void mobileDataUsageInd(int indicationType, ArrayList<Integer> data) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            response[i] = data.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3133, response);
        if (this.mMtkRil.mMobileDataUsageRegistrants != null) {
            this.mMtkRil.mMobileDataUsageRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onNwLimitInd(int indicationType, ArrayList<Integer> state) {
        this.mMtkRil.processIndication(0, indicationType);
        int[] response = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            response[i] = state.get(i).intValue();
        }
        this.mMtkRil.unsljLogRet(3134, response);
        if (this.mMtkRil.mNwLimitRegistrants != null) {
            this.mMtkRil.mNwLimitRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void iccidChanged(int indicationType, String iccid) {
        this.mMtkRil.processIndication(0, indicationType);
        this.mMtkRil.unsljLogRet(3135, SubscriptionInfo.getPrintableId(iccid));
        this.mMtkRil.cacheIccid(iccid);
        if (this.mMtkRil.mIccidRegistrants.size() != 0) {
            this.mMtkRil.mIccidRegistrants.notifyRegistrants(new AsyncResult((Object) null, iccid, (Throwable) null));
        }
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onPlmnDataInd(int indicationType, PlmnMvnoInfo plmnMvnoInfo) {
        this.mMtkRil.processIndication(0, indicationType);
        PlmnMvnoData response = new PlmnMvnoData(plmnMvnoInfo.gsmPlmn, plmnMvnoInfo.cdmaPlmn, plmnMvnoInfo.gsmSpn, plmnMvnoInfo.cdmaSpn, plmnMvnoInfo.gsmImsi, plmnMvnoInfo.cdmaImsi, plmnMvnoInfo.gid1, plmnMvnoInfo.pnn, plmnMvnoInfo.impi);
        this.mMtkRil.unsljLogRet(3136, response);
        this.mMtkRil.notifyPlmnMvnoData(response);
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExIndicationBaseV2, vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioExIndication
    public void onRsuEvent(int type, int eventId, String eventString) {
        this.mMtkRil.processIndication(0, type);
        this.mMtkRil.riljLog("[RSU-SIMLOCK] onRsuEvent eventId: " + eventId + ", eventString: " + eventString);
        if (ENG) {
            this.mMtkRil.unsljLog(3128);
        }
        int[] response = {eventId};
        if (this.mMtkRil.mRsuSimlockRegistrants != null) {
            this.mMtkRil.mRsuSimlockRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }
}
