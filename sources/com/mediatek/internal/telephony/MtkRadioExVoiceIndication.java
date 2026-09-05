package com.mediatek.internal.telephony;

import android.hardware.radio.network.SuppSvcNotification;
import android.os.AsyncResult;
import com.mediatek.internal.telephony.gsm.MtkSuppCrssNotification;
import com.mediatek.internal.telephony.gsm.MtkSuppServiceNotification;
import vendor.mediatek.hardware.mtkradioex.voice.CfuStatusNotification;
import vendor.mediatek.hardware.mtkradioex.voice.CipherNotification;
import vendor.mediatek.hardware.mtkradioex.voice.CrssNotification;
import vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication;
import vendor.mediatek.hardware.mtkradioex.voice.IncomingCallNotification;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExVoiceIndication extends IMtkRadioExVoiceIndication.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExVoiceIndication(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void callAdditionalInfoInd(int type, int ciType, String[] info) {
        this.mMtkRil.processIndication(6, type);
        String[] notification = new String[info.length + 1];
        notification[0] = Integer.toString(ciType);
        for (int i = 0; i < info.length; i++) {
            notification[i + 1] = info[i];
        }
        this.mMtkRil.unsljLogRet(3126, null);
        if (this.mMtkRil.mCallAdditionalInfoRegistrants != null) {
            this.mMtkRil.mCallAdditionalInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void cdmaCallAccepted(int type) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void cfuStatusNotify(int type, CfuStatusNotification cfuStatus) {
        this.mMtkRil.processIndication(6, type);
        int[] notification = {cfuStatus.status, cfuStatus.lineId};
        this.mMtkRil.unsljLogRet(3070, notification);
        if (notification[1] == 1) {
            this.mMtkRil.mCfuReturnValue = notification;
        }
        if (this.mMtkRil.mCallForwardingInfoRegistrants.size() != 0 && notification[1] == 1) {
            this.mMtkRil.mCallForwardingInfoRegistrants.notifyRegistrants(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void cipherIndication(int type, CipherNotification cipherNotify) {
        this.mMtkRil.processIndication(6, type);
        String[] notification = {cipherNotify.simCipherStatus, cipherNotify.sessionStatus, cipherNotify.csStatus, cipherNotify.psStatus};
        this.mMtkRil.unsljLogRet(3024, notification);
        if (this.mMtkRil.mCipherIndicationRegistrants != null) {
            this.mMtkRil.mCipherIndicationRegistrants.notifyRegistrants(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void confSRVCC(int type, int[] callIds) {
        this.mMtkRil.processIndication(6, type);
        this.mMtkRil.unsljLog(3072);
        this.mMtkRil.mEconfSrvccRegistrants.notifyRegistrants(new AsyncResult((Object) null, callIds, (Throwable) null));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void crssIndication(int type, CrssNotification crssNotify) {
        this.mMtkRil.processIndication(6, type);
        MtkSuppCrssNotification notification = new MtkSuppCrssNotification();
        notification.code = crssNotify.code;
        notification.type = crssNotify.type;
        notification.alphaid = crssNotify.alphaid;
        notification.number = crssNotify.number;
        notification.cli_validity = crssNotify.cli_validity;
        this.mMtkRil.unsljLogRet(3025, null);
        if (this.mMtkRil.mCallRelatedSuppSvcRegistrant != null) {
            this.mMtkRil.mCallRelatedSuppSvcRegistrant.notifyRegistrant(new AsyncResult((Object) null, notification, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void eccNumIndication(int type, String eccListWithCard, String eccListMoCard) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void incomingCallIndication(int type, IncomingCallNotification inCallNotify) {
        this.mMtkRil.processIndication(6, type);
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

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public void suppSvcNotifyEx(int i, SuppSvcNotification suppSvcNotification) {
        this.mMtkRil.processIndication(6, i);
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

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public String getInterfaceHash() {
        return "6fb707faf11116647ab6b8daa3ee47c2662abaa2";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoiceIndication
    public int getInterfaceVersion() {
        return 1;
    }
}
