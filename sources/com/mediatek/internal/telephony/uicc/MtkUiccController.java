package com.mediatek.internal.telephony.uicc;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.RadioManager;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.rsu.RsuSml;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccController extends UiccController {
    protected static final String DECRYPT_STATE = "trigger_restart_framework";
    protected static final int EVENT_BASE_ID = 100;
    protected static final int EVENT_CARD_DETECTED_IND = 115;
    protected static final int EVENT_GET_ICC_STATUS_DONE_FOR_SIM_MISSING = 105;
    protected static final int EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY = 106;
    protected static final int EVENT_ICCID_CHANGED_IND = 116;
    protected static final int EVENT_INVALID_SIM_DETECTED = 109;
    protected static final int EVENT_REPOLL_SML_STATE = 110;
    protected static final int EVENT_RSU_SIM_LOCK_CHANGED = 117;
    protected static final int EVENT_SIM_MISSING = 103;
    protected static final int EVENT_SIM_PLUG_IN = 108;
    protected static final int EVENT_SIM_PLUG_OUT = 107;
    protected static final int EVENT_SIM_POWER_CHANGED = 114;
    protected static final int EVENT_SIM_RECOVERY = 104;
    protected static final int EVENT_SML_SLOT_LOCK_INFO_CHANGED = 112;
    protected static final int EVENT_SUPPLY_DEVICE_LOCK_DONE = 113;
    protected static final int EVENT_VIRTUAL_SIM_OFF = 102;
    protected static final int EVENT_VIRTUAL_SIM_ON = 101;
    private static final String LOG_TAG_EX = "MtkUiccCtrl";
    private static final String PROPERTY_SIM_CARD_ONOFF = "ro.vendor.mtk_sim_card_onoff";
    private static final String[] PROPERTY_SIM_ONOFF_STATE = {"vendor.ril.sim.onoff.state1", "vendor.ril.sim.onoff.state2", "vendor.ril.sim.onoff.state3", "vendor.ril.sim.onoff.state4"};
    private static final String PROPERTY_SIM_ONOFF_SUPPORT = "vendor.ril.sim.onoff.support";
    private static final int RSU_EVENT_LOCK_STATE_CHANGE = 2;
    private static final int SML_FEATURE_NEED_BROADCAST_INTENT = 1;
    private static final int SML_FEATURE_NO_NEED_BROADCAST_INTENT = 0;
    private int[] UICCCONTROLLER_STRING_NOTIFICATION_VIRTUAL_SIM_ON;
    private String[] mIccid;
    private BroadcastReceiver mMdStateReceiver;
    private IMtkRsuSml mMtkRsuSml;
    private int[] mSimPower;
    private int[] mSimPowerExecutingState;
    private int prevActiveModemCount;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkUiccController(Context c) {
        super(c);
        this.mMtkRsuSml = null;
        this.UICCCONTROLLER_STRING_NOTIFICATION_VIRTUAL_SIM_ON = new int[]{134545755, 134545756, 134545757, 134545758};
        this.prevActiveModemCount = 0;
        Rlog.d(LOG_TAG_EX, "Creating MtkUiccController");
        this.prevActiveModemCount = this.mCis.length;
        for (int i = 0; i < this.mCis.length; i++) {
            Integer index = new Integer(i);
            this.mCis[i].unregisterForAvailable(this);
            this.mCis[i].unregisterForOn(this);
            if (SystemProperties.get("ro.crypto.state").equals("unencrypted") || SystemProperties.get("ro.crypto.state").equals("unsupported") || SystemProperties.get("ro.crypto.type").equals("file") || DECRYPT_STATE.equals(SystemProperties.get("vold.decrypt"))) {
                this.mCis[i].registerForAvailable(this, 6, index);
            } else {
                this.mCis[i].registerForOn(this, 5, index);
            }
            this.mCis[i].registerForVirtualSimOn(this, 101, index);
            this.mCis[i].registerForVirtualSimOff(this, 102, index);
            this.mCis[i].registerForSimMissing(this, 103, index);
            this.mCis[i].registerForSimRecovery(this, EVENT_SIM_RECOVERY, index);
            this.mCis[i].registerForSimPlugOut(this, EVENT_SIM_PLUG_OUT, index);
            this.mCis[i].registerForSimPlugIn(this, EVENT_SIM_PLUG_IN, index);
            this.mCis[i].registerForSmlSlotLockInfoChanged(this, EVENT_SML_SLOT_LOCK_INFO_CHANGED, index);
            this.mCis[i].registerForRsuSimLockChanged(this, EVENT_RSU_SIM_LOCK_CHANGED, index);
            this.mCis[i].registerForSimPower(this, EVENT_SIM_POWER_CHANGED, index);
            this.mCis[i].registerForCardDetectedInd(this, EVENT_CARD_DETECTED_IND, index);
            this.mCis[i].registerForIccidChanged(this, EVENT_ICCID_CHANGED_IND, index);
        }
        if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) == 1) {
            ExternalSimManager.make(c, this.mCis);
        }
        this.mMdStateReceiver = new ModemStateChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RadioManager.ACTION_MODEM_POWER_NO_CHANGE);
        this.mContext.registerReceiver(this.mMdStateReceiver, filter);
        this.mSimPower = new int[this.mCis.length];
        this.mSimPowerExecutingState = new int[this.mCis.length];
        Arrays.fill(this.mSimPower, -1);
        Arrays.fill(this.mSimPowerExecutingState, -1);
        String[] strArr = new String[this.mCis.length];
        this.mIccid = strArr;
        Arrays.fill(strArr, "");
        try {
            this.mMtkRsuSml = new RsuSml(this.mContext, this.mCis);
            Rlog.d(LOG_TAG_EX, "[RSU-SIMLOCK] Create RsuSml, mCis=" + this.mCis);
        } catch (Exception e) {
            Rlog.e(LOG_TAG_EX, "[RSU-SIMLOCK] e = " + e);
        }
    }

    public IMtkRsuSml getRsuSml() {
        if (this.mMtkRsuSml == null) {
            Rlog.e(LOG_TAG_EX, "getRsuSml : [RSU-SIMLOCK] Sml not supported");
        }
        return this.mMtkRsuSml;
    }

    public void handleMessage(Message msg) {
        synchronized (mLock) {
            Integer index = getCiIndex(msg);
            if (index.intValue() >= 0 && index.intValue() < this.mCis.length) {
                if (msg.obj != null && (msg.obj instanceof AsyncResult)) {
                }
                switch (msg.what) {
                    case 1:
                        mtkLog("Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus,index: " + index);
                        if (ignoreGetSimStatus()) {
                            mtkLog("FlightMode ON, Modem OFF: ignore get sim status");
                        } else {
                            this.mCis[index.intValue()].getIccCardStatus(obtainMessage(3, index));
                        }
                        break;
                    case 5:
                    case 6:
                        if (ignoreGetSimStatus()) {
                            mtkLog("FlightMode ON, Modem OFF: ignore get sim status, index: " + index);
                        } else {
                            super.handleMessage(msg);
                        }
                        break;
                    case 10:
                        int activeModemCount = ((Integer) ((AsyncResult) msg.obj).result).intValue();
                        this.prevActiveModemCount = this.mCis.length;
                        for (int i = activeModemCount; i < this.prevActiveModemCount; i++) {
                            int slotIndex = getSlotIdFromPhoneId(i);
                            UiccSlot uiccSlot = getUiccSlot(slotIndex);
                            if (uiccSlot != null) {
                                uiccSlot.onRadioStateUnavailable(i);
                            }
                            this.mIccChangedRegistrants.notifyRegistrants(new AsyncResult((Object) null, Integer.valueOf(i), (Throwable) null));
                        }
                        super.handleMessage(msg);
                        onMultiSimConfigChangedEx(activeModemCount);
                        break;
                    case 101:
                        mtkLog("handleMessage (EVENT_VIRTUAL_SIM_ON)");
                        setNotificationVirtual(index.intValue(), 101);
                        SharedPreferences shOn = this.mContext.getSharedPreferences("AutoAnswer", 0);
                        SharedPreferences.Editor editorOn = shOn.edit();
                        editorOn.putBoolean("flag", true);
                        editorOn.commit();
                        break;
                    case 102:
                        mtkLog("handleMessage (EVENT_VIRTUAL_SIM_OFF)");
                        removeNotificationVirtual(index.intValue(), 101);
                        SharedPreferences shOff = this.mContext.getSharedPreferences("AutoAnswer", 0);
                        SharedPreferences.Editor editorOff = shOff.edit();
                        editorOff.putBoolean("flag", false);
                        editorOff.commit();
                        break;
                    case 103:
                        mtkLog("handleMessage (EVENT_SIM_MISSING)");
                        this.mCis[index.intValue()].getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE_FOR_SIM_MISSING, index));
                        break;
                    case EVENT_SIM_RECOVERY /* 104 */:
                        mtkLog("handleMessage (EVENT_SIM_RECOVERY)");
                        this.mCis[index.intValue()].getIccCardStatus(obtainMessage(EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY, index));
                        Intent intent = new Intent();
                        intent.setAction("com.mediatek.phone.ACTION_SIM_RECOVERY_DONE");
                        this.mContext.sendBroadcast(intent);
                        break;
                    case EVENT_GET_ICC_STATUS_DONE_FOR_SIM_MISSING /* 105 */:
                        mtkLog("Received EVENT_GET_ICC_STATUS_DONE_FOR_SIM_MISSING");
                        AsyncResult ar = (AsyncResult) msg.obj;
                        onGetIccCardStatusDone(ar, index);
                        break;
                    case EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY /* 106 */:
                        mtkLog("Received EVENT_GET_ICC_STATUS_DONE_FOR_SIM_RECOVERY");
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        onGetIccCardStatusDone(ar2, index);
                        break;
                    case 110:
                        mtkLog("Received EVENT_REPOLL_SML_STATE");
                        AsyncResult ar3 = (AsyncResult) msg.obj;
                        boolean needIntent = msg.arg1 == 1;
                        onGetIccCardStatusDone(ar3, index);
                        if (needIntent) {
                            UiccCardApplication app = getUiccCardApplication(index.intValue(), 1);
                            if (app == null) {
                                mtkLog("UiccCardApplication = null");
                            } else if (app.getState() == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO) {
                                Intent lockIntent = new Intent();
                                mtkLog("Broadcast ACTION_UNLOCK_SIM_LOCK");
                                lockIntent.setAction("com.mediatek.phone.ACTION_UNLOCK_SIM_LOCK");
                                lockIntent.putExtra("ss", "LOCKED");
                                lockIntent.putExtra(DataSubConstants.EXTRA_MOBILE_DATA_ENABLE_REASON, parsePersoType(app.getPersoSubState()));
                                SubscriptionManager.putPhoneIdAndSubIdExtra(lockIntent, index.intValue());
                                this.mContext.sendBroadcast(lockIntent);
                            }
                        }
                        break;
                    case EVENT_SML_SLOT_LOCK_INFO_CHANGED /* 112 */:
                        mtkLog("handleMessage (EVENT_SML_SLOT_LOCK_INFO_CHANGED)");
                        AsyncResult ar4 = (AsyncResult) msg.obj;
                        onSmlSlotLoclInfoChaned(ar4, index);
                        triggerUpdateInternalSimMountState(index.intValue());
                        break;
                    case EVENT_SUPPLY_DEVICE_LOCK_DONE /* 113 */:
                        mtkLog("handleMessage (EVENT_SUPPLY_DEVICE_LOCK_DONE)");
                        int attemptsRemaining = -1;
                        AsyncResult ar5 = (AsyncResult) msg.obj;
                        if (ar5.result != null) {
                            attemptsRemaining = parseUnlockDeviceResult(ar5);
                        }
                        Message response = (Message) ar5.userObj;
                        AsyncResult.forMessage(response).exception = ar5.exception;
                        response.arg1 = attemptsRemaining;
                        response.sendToTarget();
                        break;
                    case EVENT_SIM_POWER_CHANGED /* 114 */:
                        AsyncResult ar6 = (AsyncResult) msg.obj;
                        if (ar6.exception == null && ar6.result != null) {
                            if (index.intValue() >= this.mSimPower.length) {
                                Rlog.e(LOG_TAG_EX, "EVENT_SIM_POWER_CHANGED index error");
                                return;
                            }
                            int[] state = (int[]) ar6.result;
                            if (state.length == 1) {
                                this.mSimPower[index.intValue()] = state[0];
                                if (state[0] == 10 || state[0] == 11) {
                                    this.mSimPowerExecutingState[index.intValue()] = -1;
                                }
                            }
                            mtkLog("Received EVENT_SIM_POWER_CHANGED, index: " + index + " simPower: " + this.mSimPower[index.intValue()] + " mSimPowerExecutingState = " + this.mSimPowerExecutingState[index.intValue()]);
                            break;
                        }
                        Rlog.e(LOG_TAG_EX, "EVENT_SIM_POWER_CHANGED exception");
                        return;
                    case EVENT_CARD_DETECTED_IND /* 115 */:
                        Intent cardDetectedInd = new Intent("com.mediatek.phone.ACTION_CARD_DETECTED");
                        int slotId = index.intValue();
                        SubscriptionManager.putPhoneIdAndSubIdExtra(cardDetectedInd, slotId);
                        mtkLog("Broadcasting intent ACTION_CARD_DETECTED, mSlotId : " + slotId);
                        this.mContext.sendBroadcast(cardDetectedInd);
                        break;
                    case EVENT_ICCID_CHANGED_IND /* 116 */:
                        AsyncResult ar7 = (AsyncResult) msg.obj;
                        if (ar7.exception == null && ar7.result != null) {
                            if (index.intValue() >= this.mIccid.length) {
                                Rlog.e(LOG_TAG_EX, "EVENT_ICCID_CHANGED_IND index error");
                                return;
                            }
                            updateAbsentFromIccid((String) ar7.result, index.intValue());
                            this.mIccid[index.intValue()] = (String) ar7.result;
                            mtkLog("Received EVENT_ICCID_CHANGED_IND, index: " + index + ", iccid: " + this.mIccid);
                            break;
                        }
                        Rlog.e(LOG_TAG_EX, "EVENT_ICCID_CHANGED_IND exception");
                        return;
                    case EVENT_RSU_SIM_LOCK_CHANGED /* 117 */:
                        AsyncResult ar8 = (AsyncResult) msg.obj;
                        if (ar8.exception == null && ar8.result != null) {
                            int[] event = (int[]) ar8.result;
                            mtkLog("EVENT_RSU_SIM_LOCK_CHANGED eventId = " + event[0]);
                            int eventId = event[0] & 255;
                            if (eventId == 2) {
                                int lockState = (event[0] >> 8) & 255;
                                Intent rsuInd = new Intent("com.mediatek.intent.SIM_ME_LOCK_STATE_CHANGE");
                                rsuInd.putExtra("LOCK_STATE", lockState);
                                mtkLog("Broadcasting intent ACTION_SIM_ME_LOCK_STATE_CHANGED, index: " + index + ", lockState: " + lockState);
                                this.mContext.sendBroadcast(rsuInd);
                            }
                            break;
                        }
                        Rlog.e(LOG_TAG_EX, "EVENT_RSU_SIM_LOCK_CHANGED exception");
                        return;
                    default:
                        super.handleMessage(msg);
                        break;
                }
                return;
            }
            Rlog.e(LOG_TAG_EX, "Invalid index : " + index + " received with event " + msg.what);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onMultiSimConfigChangedEx(int newActiveModemCount) {
        int prevActualActiveModemCount = this.mIccid.length;
        this.mCis = PhoneFactory.getCommandsInterfaces();
        this.mSimPowerExecutingState = Arrays.copyOf(this.mSimPowerExecutingState, newActiveModemCount);
        this.mSimPower = Arrays.copyOf(this.mSimPower, newActiveModemCount);
        this.mIccid = (String[]) Arrays.copyOf(this.mIccid, newActiveModemCount);
        for (int i = prevActualActiveModemCount; i < newActiveModemCount; i++) {
            this.mPhoneIdToSlotId[i] = -1;
            this.mSimPowerExecutingState[i] = -1;
            this.mSimPower[i] = -1;
            this.mIccid[i] = "";
        }
        for (int i2 = this.prevActiveModemCount; i2 < newActiveModemCount; i2++) {
            Integer index = new Integer(i2);
            this.mSimPowerExecutingState[i2] = -1;
            this.mSimPower[i2] = -1;
            this.mIccid[i2] = "";
            this.mCis[i2].registerForVirtualSimOn(this, 101, index);
            this.mCis[i2].registerForVirtualSimOff(this, 102, index);
            this.mCis[i2].registerForSimMissing(this, 103, index);
            this.mCis[i2].registerForSimRecovery(this, EVENT_SIM_RECOVERY, index);
            this.mCis[i2].registerForSimPlugOut(this, EVENT_SIM_PLUG_OUT, index);
            this.mCis[i2].registerForSimPlugIn(this, EVENT_SIM_PLUG_IN, index);
            this.mCis[i2].registerForSmlSlotLockInfoChanged(this, EVENT_SML_SLOT_LOCK_INFO_CHANGED, index);
            this.mCis[i2].registerForSimPower(this, EVENT_SIM_POWER_CHANGED, index);
            this.mCis[i2].registerForCardDetectedInd(this, EVENT_CARD_DETECTED_IND, index);
            this.mCis[i2].registerForIccidChanged(this, EVENT_ICCID_CHANGED_IND, index);
        }
    }

    private void onSmlSlotLoclInfoChaned(AsyncResult ar, Integer index) {
        if (ar.exception != null || ar.result == null) {
            Rlog.e(LOG_TAG_EX, "onSmlSlotLoclInfoChaned exception");
            return;
        }
        int[] info = (int[]) ar.result;
        if (info.length != 4) {
            Rlog.e(LOG_TAG_EX, "onSmlSlotLoclInfoChaned exception");
            return;
        }
        mtkLog("onSmlSlotLoclInfoChaned, infomation:,lock policy:" + info[0] + ",lock state:" + info[1] + ",service capability:" + info[2] + ",sim valid:" + info[3]);
        Intent smlLockInfoChanged = new Intent("com.mediatek.phone.ACTION_SIM_SLOT_LOCK_POLICY_INFORMATION");
        int slotId = index.intValue();
        SubscriptionManager.putPhoneIdAndSubIdExtra(smlLockInfoChanged, slotId);
        smlLockInfoChanged.putExtra("slot", slotId);
        smlLockInfoChanged.putExtra("DEVICE_LOCK_POLICY", info[0]);
        smlLockInfoChanged.putExtra("DEVICE_LOCK_STATE", info[1]);
        smlLockInfoChanged.putExtra("SIM_SERVICE_CAPABILITY", info[2]);
        smlLockInfoChanged.putExtra("SIM_VALID", info[3]);
        mtkLog("Broadcasting intent ACTION_SIM_SLOT_LOCK_POLICY_INFORMATION for mSlotId : " + slotId);
        this.mContext.sendBroadcastAsUser(smlLockInfoChanged, UserHandle.ALL);
    }

    private void triggerUpdateInternalSimMountState(int phoneId) {
        mtkLog("triggerUpdateInternalSimMountState.");
        MtkSubscriptionManagerService.getInstance().triggerUpdateInternalSimMountState(phoneId);
    }

    public void supplyDeviceNetworkDepersonalization(String pwd, Message onComplete) {
        this.mCis[0].supplyDeviceNetworkDepersonalization(pwd, obtainMessage(EVENT_SUPPLY_DEVICE_LOCK_DONE, onComplete));
    }

    private int parseUnlockDeviceResult(AsyncResult ar) {
        int[] result = (int[]) ar.result;
        if (result == null) {
            return -1;
        }
        int length = result.length;
        int attemptsRemaining = -1;
        if (length > 0) {
            attemptsRemaining = result[0];
        }
        mtkLog("parseUnlockDeviceResult: attemptsRemaining=" + attemptsRemaining);
        return attemptsRemaining;
    }

    private void setNotificationVirtual(int slot, int notifyType) {
        String title;
        mtkLog("setNotificationVirtual(): notifyType = " + notifyType);
        Notification notification = new Notification();
        notification.when = System.currentTimeMillis();
        notification.flags = 16;
        notification.icon = R.drawable.stat_sys_warning;
        Intent intent = new Intent();
        notification.contentIntent = PendingIntent.getActivity(this.mContext, 0, intent, 134217728);
        if (TelephonyManager.getDefault().getSimCount() > 1) {
            title = Resources.getSystem().getText(this.UICCCONTROLLER_STRING_NOTIFICATION_VIRTUAL_SIM_ON[slot]).toString();
        } else {
            title = Resources.getSystem().getText(134545754).toString();
        }
        CharSequence detail = this.mContext.getText(134545754).toString();
        notification.tickerText = this.mContext.getText(134545754).toString();
        notification.setLatestEventInfo(this.mContext, title, detail, notification.contentIntent);
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        notificationManager.notify(notifyType + slot, notification);
    }

    private void removeNotificationVirtual(int slot, int notifyType) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        notificationManager.cancel(notifyType + slot);
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.uicc.MtkUiccController$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
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
        }
    }

    private String parsePersoType(IccCardApplicationStatus.PersoSubState state) {
        mtkLog("parsePersoType, state = " + state);
        switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$PersoSubState[state.ordinal()]) {
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
            default:
                return "UNKNOWN";
        }
    }

    public void repollIccStateForModemSmlChangeFeatrue(int slotId, boolean needIntent) {
        mtkLog("repollIccStateForModemSmlChangeFeatrue, needIntent = " + needIntent);
        int arg1 = !needIntent ? 0 : 1;
        this.mCis[slotId].getIccCardStatus(obtainMessage(110, arg1, 0, Integer.valueOf(slotId)));
    }

    public boolean ignoreGetSimStatus() {
        int airplaneMode = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0);
        mtkLog("ignoreGetSimStatus(): airplaneMode - " + airplaneMode);
        if (!RadioManager.isFlightModePowerOffModemEnabled() || airplaneMode != 1) {
            return false;
        }
        mtkLog("ignoreGetSimStatus(): return true");
        return true;
    }

    protected void mtkLog(String string) {
        Rlog.d(LOG_TAG_EX, string);
    }

    public boolean isAllRadioAvailable() {
        boolean isRadioReady = true;
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            if (2 == this.mCis[i].getRadioState()) {
                isRadioReady = false;
            }
        }
        mtkLog("isAllRadioAvailable = " + isRadioReady);
        return isRadioReady;
    }

    public void resetRadioForVsim() {
        mtkLog("resetRadioForVsim...resetRadio");
        int mainPhoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
        this.mCis[mainPhoneId].restartRILD(null);
    }

    public static MtkIccCardConstants.VsimType getVsimCardType(int slotId) {
        int rSim = SystemProperties.getInt("vendor.gsm.prefered.rsim.slot", -1);
        int akaSim = SystemProperties.getInt("vendor.gsm.prefered.aka.sim.slot", -1);
        boolean isVsim = false;
        TelephonyManager.getDefault();
        String inserted = TelephonyManager.getTelephonyProperty(slotId, "vendor.gsm.external.sim.inserted", "0");
        if (inserted != null && inserted.length() > 0 && !"0".equals(inserted)) {
            isVsim = true;
        }
        if (slotId == rSim && isVsim) {
            return MtkIccCardConstants.VsimType.REMOTE_SIM;
        }
        if (slotId == akaSim) {
            if (isVsim) {
                return MtkIccCardConstants.VsimType.SOFT_AKA_SIM;
            }
            return MtkIccCardConstants.VsimType.PHYSICAL_AKA_SIM;
        }
        if (rSim == -1 && akaSim == -1 && isVsim) {
            return MtkIccCardConstants.VsimType.LOCAL_SIM;
        }
        return MtkIccCardConstants.VsimType.PHYSICAL_SIM;
    }

    private class ModemStateChangedReceiver extends BroadcastReceiver {
        private ModemStateChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RadioManager.ACTION_MODEM_POWER_NO_CHANGE)) {
                for (int i = 0; i < MtkUiccController.this.mCis.length; i++) {
                    Integer index = new Integer(i);
                    Message msg = MtkUiccController.this.obtainMessage(1, index);
                    MtkUiccController.this.sendMessage(msg);
                    MtkUiccController.this.mtkLog("Trigger GET_SIM_STATUS due to modem state changed for slot " + i);
                }
            }
        }
    }

    public void setSimPower(int slotId, int state, Message onComplete) {
        this.mCis[slotId].setSimPower(state, onComplete);
    }

    public int getSimOnOffState(int slotId) {
        if (slotId < 0 || slotId >= this.mSimPower.length) {
            mtkLog("getSimOnOffState: invalid slotId " + slotId);
            return -1;
        }
        boolean onoffAPSupport = SystemProperties.get(PROPERTY_SIM_CARD_ONOFF).equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN);
        boolean onoffMDSupport = SystemProperties.get(PROPERTY_SIM_ONOFF_SUPPORT).equals(RadioCapabilitySwitchUtil.IMSI_READY);
        int onoffState = SystemProperties.getInt(PROPERTY_SIM_ONOFF_STATE[slotId], -1);
        mtkLog("getSimOnOffState slotId = " + slotId + " onoffAPSupport = " + onoffAPSupport + " onoffMDSupport = " + onoffMDSupport + " mSimPower = " + this.mSimPower[slotId] + " onoffState = " + onoffState);
        if (onoffAPSupport && onoffMDSupport) {
            int i = this.mSimPower[slotId];
            if (i == -1) {
                return onoffState;
            }
            return i;
        }
        return 11;
    }

    public void setSimOnOffExecutingState(int slotId, int state) {
        this.mSimPowerExecutingState[slotId] = state;
    }

    public int getSimOnOffExecutingState(int slotId) {
        if (slotId < 0 || slotId >= this.mSimPowerExecutingState.length) {
            mtkLog("getSimOnOffExecutingState: invalid slotId " + slotId);
            return -1;
        }
        mtkLog("getSimOnOffExecutingState slotId = " + slotId + " mSimPowerExecutingState = " + this.mSimPowerExecutingState[slotId]);
        return this.mSimPowerExecutingState[slotId];
    }

    public String getIccid(int slotId) {
        if (slotId < 0 || slotId >= this.mIccid.length) {
            mtkLog("getIccid: invalid slotId " + slotId);
            return "";
        }
        mtkLog("getIccid: slotId = " + slotId);
        return this.mIccid[slotId];
    }

    private void updateAbsentFromIccid(String iccid, int phoneId) {
        int slotIndex = getSlotIdFromPhoneId(phoneId);
        UiccSlot uiccSlot = getUiccSlot(slotIndex);
        if (uiccSlot == null || uiccSlot.getCardState() != IccCardStatus.CardState.CARDSTATE_ABSENT) {
            return;
        }
        String str = this.mIccid[phoneId];
        if ((str != null && (str.equals(DataSubConstants.NO_SIM_VALUE) || this.mIccid[phoneId].equals(""))) || !iccid.equals(DataSubConstants.NO_SIM_VALUE)) {
            return;
        }
        mtkLog("updateAbsentFromIccid");
        updateSimState(phoneId, IccCardConstants.State.ABSENT, null);
    }
}
