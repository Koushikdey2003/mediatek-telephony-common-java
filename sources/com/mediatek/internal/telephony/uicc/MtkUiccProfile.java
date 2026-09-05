package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Registrant;
import com.android.internal.telephony.RegistrantList;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccProfile extends UiccProfile {
    protected static final int EVENT_BASE_ID = 100;
    private static final int EVENT_GET_ATR_DONE = 102;
    private static final int EVENT_ICC_FDN_CHANGED = 104;
    private static final int EVENT_OPEN_CHANNEL_WITH_SW_DONE = 103;
    private static final int EVENT_SIM_IO_EX_DONE = 101;
    static final String[] UICCCARD_PROPERTY_RIL_UICC_TYPE = {"vendor.gsm.ril.uicctype", "vendor.gsm.ril.uicctype.2", "vendor.gsm.ril.uicctype.3", "vendor.gsm.ril.uicctype.4"};
    private RegistrantList mFdnChangedRegistrants;
    public final Handler mMtkHandler;

    public MtkUiccProfile(Context c, CommandsInterface ci, IccCardStatus ics, int phoneId, UiccCard uiccCard, Object lock) {
        super(c, ci, ics, phoneId, uiccCard, lock);
        this.mFdnChangedRegistrants = new RegistrantList();
        Handler handler = new Handler() { // from class: com.mediatek.internal.telephony.uicc.MtkUiccProfile.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (MtkUiccProfile.this.mDisposed) {
                    MtkUiccProfile.this.loge("handleMessage: Received " + msg.what + " after dispose(); ignoring the message");
                    return;
                }
                MtkUiccProfile.this.log("mHandlerEx Received message " + msg + "[" + msg.what + "]");
                switch (msg.what) {
                    case 101:
                    case 102:
                    case 103:
                        AsyncResult ar = (AsyncResult) msg.obj;
                        if (ar.exception != null) {
                            MtkUiccProfile.this.loge("Error in SIM access with exception" + ar.exception);
                        }
                        AsyncResult.forMessage((Message) ar.userObj, ar.result, ar.exception);
                        ((Message) ar.userObj).sendToTarget();
                        return;
                    case MtkUiccProfile.EVENT_ICC_FDN_CHANGED /* 104 */:
                        synchronized (MtkUiccProfile.this.mLock) {
                            MtkUiccProfile.this.mFdnChangedRegistrants.notifyRegistrants();
                            break;
                        }
                        return;
                    default:
                        MtkUiccProfile.this.mHandler.handleMessage(msg);
                        return;
                }
            }
        };
        this.mMtkHandler = handler;
        if (this.mUiccApplication != null && (this.mUiccApplication instanceof MtkUiccCardApplication)) {
            ((MtkUiccCardApplication) this.mUiccApplication).unregisterForFdnChanged(handler);
            ((MtkUiccCardApplication) this.mUiccApplication).registerForFdnChanged(handler, EVENT_ICC_FDN_CHANGED, null);
        }
        log("MtkUiccProfile Creating, registerForFdnChanged");
    }

    protected UiccCardApplication makeUiccApplication(UiccProfile uiccProfile, IccCardApplicationStatus as, Context c, CommandsInterface ci) {
        return new MtkUiccCardApplication(uiccProfile, as, c, ci);
    }

    protected void registerCurrAppEvents() {
        super.registerCurrAppEvents();
        if (this.mUiccApplication != null && (this.mUiccApplication instanceof MtkUiccCardApplication)) {
            if (this.mMtkHandler != null) {
                ((MtkUiccCardApplication) this.mUiccApplication).registerForFdnChanged(this.mMtkHandler, EVENT_ICC_FDN_CHANGED, null);
            } else {
                log("registerCurrAppEvents, mMtkHandler is null");
            }
        }
    }

    protected void unregisterCurrAppEvents() {
        super.unregisterCurrAppEvents();
        if (this.mUiccApplication != null && (this.mUiccApplication instanceof MtkUiccCardApplication)) {
            ((MtkUiccCardApplication) this.mUiccApplication).unregisterForFdnChanged(this.mMtkHandler);
        }
    }

    protected String getIccStateReason(IccCardConstants.State state) {
        log("getIccStateReason E");
        if (IccCardConstants.State.NETWORK_LOCKED == state && this.mUiccApplication != null) {
            switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[this.mUiccApplication.getPersoSubState().ordinal()]) {
                case 1:
                    return "NETWORK";
                case 2:
                    return "NETWORK_SUBSET";
                case 3:
                    return "CORPORATE";
                case 4:
                    return "SERVICE_PROVIDER";
                case 5:
                    return "SIM";
                case 6:
                    return "NETWORK_PUK";
                case 7:
                    return "NETWORK_SUBSET_PUK";
                case 8:
                    return "CORPORATE_PUK";
                case 9:
                    return "SERVICE_PROVIDER_PUK";
                case 10:
                    return "SIM_PUK";
                case 11:
                    return "NS_SP";
                case 12:
                    return "NS_SP_PUK";
                case 13:
                    return "SIM_C";
                case 14:
                    return "SIM_C_PUK";
                default:
                    return null;
            }
        }
        return super.getIccStateReason(state);
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.uicc.MtkUiccProfile$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState;

        static {
            int[] iArr = new int[IccCardApplicationStatus.PersoSubState.values().length];
            $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState = iArr;
            try {
                iArr[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_CORPORATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_PUK.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NETWORK_SUBSET_PUK.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_CORPORATE_PUK.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SERVICE_PROVIDER_PUK.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM_PUK.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NS_SP.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_NS_SP_PUK.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM_C.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[IccCardApplicationStatus.PersoSubState.PERSOSUBSTATE_SIM_SIM_C_PUK.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
        }
    }

    protected String getSubscriptionDisplayName(int subId, Context context) {
        String simNumeric = this.mTelephonyManager.getSimOperatorNumeric(subId);
        String simMvnoName = MtkSpnOverride.getInstance().lookupOperatorNameForDisplayName(subId, simNumeric, true, context);
        String simCarrierName = this.mTelephonyManager.getSimOperatorName(subId);
        log("getSubscriptionDisplayName- simNumeric: " + simNumeric + ", simMvnoName: " + simMvnoName + ", simCarrierName: " + simCarrierName);
        if (getState() == IccCardConstants.State.LOADED) {
            if (!TextUtils.isEmpty(simMvnoName) && !"carrier_a".equals(simCarrierName)) {
                return simMvnoName;
            }
            return simCarrierName;
        }
        return "";
    }

    protected boolean isUdpateCarrierName(String newCarrierName) {
        return !TextUtils.isEmpty(newCarrierName) && getState() == IccCardConstants.State.LOADED;
    }

    public void queryIccNetworkLock(int category, Message onComplete) {
        log("queryIccNetworkLock(): category =  " + category);
        boolean hasIccCard = hasIccCard();
        synchronized (this.mLock) {
            if (this.mUiccApplication != null) {
                ((MtkUiccCardApplication) this.mUiccApplication).queryIccNetworkLock(category, onComplete);
            } else if (onComplete != null) {
                CommandException commandExceptionFromRilErrno = CommandException.fromRilErrno(1);
                log("Fail to queryIccNetworkLock, hasIccCard = " + hasIccCard);
                AsyncResult.forMessage(onComplete).exception = commandExceptionFromRilErrno;
                onComplete.sendToTarget();
            }
        }
    }

    public void setIccNetworkLockEnabled(int category, int lockop, String password, String data_imsi, String gid1, String gid2, Message onComplete) {
        log("SetIccNetworkEnabled(): category = " + category + " lockop = " + lockop);
        boolean hasIccCard = hasIccCard();
        synchronized (this.mLock) {
            if (this.mUiccApplication != null) {
                ((MtkUiccCardApplication) this.mUiccApplication).setIccNetworkLockEnabled(category, lockop, password, data_imsi, gid1, gid2, onComplete);
            } else if (onComplete != null) {
                CommandException commandExceptionFromRilErrno = CommandException.fromRilErrno(1);
                log("Fail to setIccNetworkLockEnabled, hasIccCard = " + hasIccCard);
                AsyncResult.forMessage(onComplete).exception = commandExceptionFromRilErrno;
                onComplete.sendToTarget();
            }
        }
    }

    public void registerForFdnChanged(Handler h, int what, Object obj) {
        synchronized (this.mLock) {
            Registrant r = new Registrant(h, what, obj);
            this.mFdnChangedRegistrants.add(r);
            if (getIccFdnEnabled()) {
                r.notifyRegistrant();
            }
        }
    }

    public void unregisterForFdnChanged(Handler h) {
        synchronized (this.mLock) {
            this.mFdnChangedRegistrants.remove(h);
        }
    }

    public void repollIccStateForModemSmlChangeFeatrue(boolean needIntent) {
        log("repollIccStateForModemSmlChangeFeatrue, needIntent = " + needIntent);
        MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
        if (ctrl != null) {
            ctrl.repollIccStateForModemSmlChangeFeatrue(getPhoneId(), needIntent);
        }
    }

    protected Exception covertException(String operation) {
        log("Fail to " + operation + ", hasIccCard = " + hasIccCard());
        return CommandException.fromRilErrno(1);
    }

    public void iccGetAtr(Message onComplete) {
        this.mCi.getATR(this.mMtkHandler.obtainMessage(102, onComplete));
    }

    public String getIccCardType() {
        return SystemProperties.get(UICCCARD_PROPERTY_RIL_UICC_TYPE[getPhoneId()]);
    }

    protected void log(String msg) {
        Rlog.d("UiccProfile", msg + " (phoneId " + getPhoneId() + ")");
    }

    protected void loge(String msg) {
        Rlog.e("UiccProfile", msg + " (phoneId " + getPhoneId() + ")");
    }
}
