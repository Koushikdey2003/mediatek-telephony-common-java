package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import com.android.internal.telephony.uicc.IccUtils;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication;
import vendor.mediatek.hardware.mtkradioex.sim.VsimOperationEvent;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExSimIndication extends IMtkRadioExSimIndication.Stub {
    private static final boolean ENG = "eng".equals(Build.TYPE);
    MtkRIL mMtkRil;

    public MtkRadioExSimIndication(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void bipProactiveCommand(int type, String cmd) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void iccidChanged(int type, String iccid) {
        this.mMtkRil.processIndicationMtk(5, type);
        this.mMtkRil.unsljLogRet(3135, SubscriptionInfo.getPrintableId(iccid));
        this.mMtkRil.cacheIccid(iccid);
        if (this.mMtkRil.mIccidRegistrants.size() != 0) {
            this.mMtkRil.mIccidRegistrants.notifyRegistrants(new AsyncResult((Object) null, iccid, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onCardDetectedInd(int type) {
        this.mMtkRil.processIndicationMtk(5, type);
        this.mMtkRil.unsljLog(3125);
        if (this.mMtkRil.mCardDetectedIndRegistrant.size() != 0) {
            this.mMtkRil.mCardDetectedIndRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
            return;
        }
        if (ENG) {
            this.mMtkRil.riljLog("Cache card detected event");
        }
        this.mMtkRil.mIsCardDetected = true;
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onImeiLock(int type) {
        this.mMtkRil.processIndicationMtk(5, type);
        if (ENG) {
            this.mMtkRil.unsljLog(3007);
        }
        if (this.mMtkRil.mImeiLockRegistrant != null) {
            this.mMtkRil.mImeiLockRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onImsiRefreshDone(int type) {
        this.mMtkRil.processIndicationMtk(5, type);
        if (ENG) {
            this.mMtkRil.unsljLog(3008);
        }
        if (this.mMtkRil.mImsiRefreshDoneRegistrant != null) {
            this.mMtkRil.mImsiRefreshDoneRegistrant.notifyRegistrants(new AsyncResult((Object) null, (Object) null, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onRsuEvent(int type, int eventId, String eventString) {
        this.mMtkRil.processIndication(5, type);
        this.mMtkRil.riljLog("[RSU-SIMLOCK] onRsuEvent eventId: " + eventId + ", eventString: " + eventString);
        if (ENG) {
            this.mMtkRil.unsljLog(3128);
        }
        int[] response = {eventId};
        if (this.mMtkRil.mRsuSimlockRegistrants != null) {
            this.mMtkRil.mRsuSimlockRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onRsuSimLockEvent(int indicationType, int eventId) {
        this.mMtkRil.processIndication(5, indicationType);
        this.mMtkRil.riljLog("[RSU-SIMLOCK] onRsuSimLockEvent eventId " + eventId);
        if (ENG) {
            this.mMtkRil.unsljLog(3128);
        }
        int[] response = {eventId};
        if (this.mMtkRil.mRsuSimlockRegistrants != null) {
            this.mMtkRil.mRsuSimlockRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onSimHotSwapInd(int type, int event, String info) {
        this.mMtkRil.processIndicationMtk(5, type);
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

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onSimPowerChangedInd(int type, int[] info) {
        this.mMtkRil.processIndicationMtk(5, type);
        if (ENG) {
            this.mMtkRil.unsljLog(3124);
        }
        this.mMtkRil.mSimPowerInfo = info;
        if (this.mMtkRil.mSimPowerChanged.size() != 0) {
            this.mMtkRil.mSimPowerChanged.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onStkMenuReset(int type) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onVirtualSimStatusChanged(int type, int simInserted) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void onVsimEventIndication(int type, VsimOperationEvent event) {
        this.mMtkRil.processIndicationMtk(5, type);
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

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void phbReadyNotification(int indicationType, int isPhbReady) {
        this.mMtkRil.processIndication(5, indicationType);
        int[] response = {isPhbReady};
        this.mMtkRil.unsljLogMore(3028, "phbReadyNotification: " + isPhbReady);
        if (this.mMtkRil.mPhbReadyRegistrants != null) {
            this.mMtkRil.mPhbReadyRegistrants.notifyRegistrants(new AsyncResult((Object) null, response, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public void smlSlotLockInfoChangedInd(int indicationType, int[] info) {
        this.mMtkRil.processIndication(5, indicationType);
        if (ENG) {
            this.mMtkRil.unsljLog(3115);
        }
        this.mMtkRil.mSmlSlotLockInfo = info;
        if (this.mMtkRil.mSmlSlotLockInfoChanged.size() != 0) {
            this.mMtkRil.mSmlSlotLockInfoChanged.notifyRegistrants(new AsyncResult((Object) null, info, (Throwable) null));
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public String getInterfaceHash() {
        return "a07d8b715b307b4e57aa776d209f5655cced7f96";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimIndication
    public int getInterfaceVersion() {
        return 1;
    }
}
