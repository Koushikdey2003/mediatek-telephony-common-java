package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExMessagingIndication extends IMtkRadioExMessagingIndication.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExMessagingIndication(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public void esnMeidChangeInd(int type, String esnMeid) {
        this.mMtkRil.processIndication(2, type);
        this.mMtkRil.unsljLog(3023);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public void meSmsStorageFullInd(int type) {
        this.mMtkRil.processIndication(2, type);
        this.mMtkRil.unsljLog(3011);
        if (this.mMtkRil.mMeSmsFullRegistrant != null) {
            this.mMtkRil.mMeSmsFullRegistrant.notifyRegistrant();
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public void smsInfoExtInd(int type, String info) {
        this.mMtkRil.processIndication(2, type);
        this.mMtkRil.unsljLogRet(3137, info);
        if (this.mMtkRil.mSmsInfoExtRegistrants.size() != 0) {
            this.mMtkRil.mSmsInfoExtRegistrants.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public void smsReadyInd(int type) {
        this.mMtkRil.processIndication(2, type);
        this.mMtkRil.unsljLog(3012);
        if (this.mMtkRil.mSmsReadyRegistrants.size() != 0) {
            this.mMtkRil.mSmsReadyRegistrants.notifyRegistrants();
        } else {
            this.mMtkRil.riljLog("Cache sms ready event");
            this.mMtkRil.mIsSmsReady = true;
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public void exitSCBMInd(int type) {
        this.mMtkRil.processIndication(2, type);
        this.mMtkRil.unsljLog(3146);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public void enterSCBMInd(int type) {
        this.mMtkRil.processIndication(2, type);
        this.mMtkRil.unsljLog(3145);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public String getInterfaceHash() {
        return "93a2d5af4d82e246ed35f8afe17083da08ba1147";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.messaging.IMtkRadioExMessagingIndication
    public int getInterfaceVersion() {
        return 1;
    }
}
