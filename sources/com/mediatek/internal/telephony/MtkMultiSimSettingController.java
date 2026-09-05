package com.mediatek.internal.telephony;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.MultiSimSettingController;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.ArrayUtils;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.datasub.DataSubSelector;
import com.mediatek.internal.telephony.datasub.DataSubSelectorUtil;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class MtkMultiSimSettingController extends MultiSimSettingController {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "MtkMultiSimSettingController";
    private static final int NO_SIM_STRING_LENGTH = 3;
    private int mActiveModemCount;
    private boolean mCarrierConfigsNotLoadedHappened;
    private DataSubSelector mDataSubSelector;
    private int mSimCloseMode;
    private boolean mSimMeLockSupport;

    public MtkMultiSimSettingController(Context context) {
        super(context);
        this.mDataSubSelector = null;
        this.mSimMeLockSupport = false;
        this.mSimCloseMode = 0;
        this.mActiveModemCount = 0;
        this.mCarrierConfigsNotLoadedHappened = false;
        this.mDataSubSelector = new DataSubSelector(context);
        initSimMeLock(context);
        initSimCloseMode(context);
    }

    public static MultiSimSettingController init(Context context) {
        MultiSimSettingController multiSimSettingController;
        synchronized (MultiSimSettingController.class) {
            if (sInstance == null) {
                sInstance = new MtkMultiSimSettingController(context);
            } else {
                Log.wtf(LOG_TAG, "init() called multiple times!  sInstance = " + sInstance);
            }
            multiSimSettingController = sInstance;
        }
        return multiSimSettingController;
    }

    public void handleMessage(Message msg) {
        if (msg.what == 9) {
            log("aosp radio unavailable to clear mSubInfoInitialized, but we needn't");
        } else {
            super.handleMessage(msg);
        }
    }

    protected void onUserDataEnabled(int subId, boolean enable, boolean setDefaultData) {
        log("[onUserDataEnabled] subId=" + subId + " enable=" + enable + " setDefaultData=" + setDefaultData);
        setUserDataEnabledForGroup(subId, enable);
    }

    public boolean isCarrierConfigLoadedForAllSub() {
        int[] activeSubIds = this.mSubscriptionManagerService.getActiveSubIdList(false);
        if (activeSubIds != null) {
            for (int activeSubId : activeSubIds) {
                boolean isLoaded = false;
                int[] iArr = this.mCarrierConfigLoadedSubIds;
                int length = iArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    int configLoadedSub = iArr[i];
                    if (configLoadedSub != activeSubId) {
                        i++;
                    } else {
                        isLoaded = DBG;
                        break;
                    }
                }
                if (!isLoaded) {
                    log("Carrier config subId " + activeSubId + " is not loaded.");
                    if (this.mSimCloseMode == 2 && isSimClosed(activeSubId)) {
                        log("mtk sim on/off feature used, ignore closed sim carrier loaded event.");
                    } else {
                        this.mCarrierConfigsNotLoadedHappened = DBG;
                        return false;
                    }
                }
            }
        }
        return DBG;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onMultiSimConfigChanged(int activeModems) {
        super.onMultiSimConfigChanged(activeModems);
        if (this.mSimCloseMode == 2) {
            MtkRIL[] commandsInterfaces = PhoneFactory.getCommandsInterfaces();
            for (int i = this.mActiveModemCount; i < activeModems; i++) {
                commandsInterfaces[i].registerForSimPower(this, 4, null);
            }
            int i2 = commandsInterfaces.length;
            this.mActiveModemCount = i2;
        }
    }

    protected void updateDefaults() {
        log("updateDefaults");
        if (isReadyToReevaluate()) {
            List<SubscriptionInfo> activeSubInfos = this.mSubscriptionManagerService.getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            int i = this.mSimCloseMode;
            if (i == 1 || i == 2) {
                filterOutClosedSim(activeSubInfos);
            }
            int activeSubCount = activeSubInfos == null ? 0 : activeSubInfos.size();
            if (ArrayUtils.isEmpty(activeSubInfos)) {
                this.mPrimarySubList.clear();
                log("updateDefaults: No active sub. Setting default to INVALID sub.");
                this.mSubscriptionManagerService.setDefaultDataSubId(-1);
                this.mSubscriptionManagerService.setDefaultVoiceSubId(-1);
                this.mSubscriptionManagerService.setDefaultSmsSubId(-1);
                return;
            }
            if (isAnySimNotReady(activeSubCount) || updateDefaultDataForSimMeLock(activeSubInfos.size()) || updateDefaultDataForSubSidyLock(activeSubInfos.size())) {
                return;
            }
            int change = updatePrimarySubListAndGetChangeType(activeSubInfos);
            log("updateDefaultValues: change: " + change);
            if (change == 0) {
                if (this.mCarrierConfigsNotLoadedHappened) {
                    log("no change but carrier loaded changed, force go on.");
                } else {
                    return;
                }
            }
            this.mCarrierConfigsNotLoadedHappened = false;
            if (this.mPrimarySubList.size() == 1) {
                int subId = ((Integer) this.mPrimarySubList.get(0)).intValue();
                log("updateDefaultValues: to only primary sub " + subId);
                this.mSubscriptionManagerService.setDefaultDataSubId(subId);
                this.mSubscriptionManagerService.setDefaultVoiceSubId(subId);
                this.mSubscriptionManagerService.setDefaultSmsSubId(subId);
                sendDefaultSubConfirmedNotification(subId);
                return;
            }
            log("updateDefaultValues: records: " + this.mPrimarySubList);
            log("updateDefaultValues: Update default data subscription");
            List list = this.mPrimarySubList;
            int defaultDataSubId = this.mSubscriptionManagerService.getDefaultDataSubId();
            final SubscriptionManagerService subscriptionManagerService = this.mSubscriptionManagerService;
            Objects.requireNonNull(subscriptionManagerService);
            boolean dataSelected = updateDefaultValue(list, defaultDataSubId, new MultiSimSettingController.UpdateDefaultAction() { // from class: com.mediatek.internal.telephony.MtkMultiSimSettingController$$ExternalSyntheticLambda0
                public final void update(int i2) {
                    subscriptionManagerService.setDefaultDataSubId(i2);
                }
            });
            log("updateDefaultValues: Update default voice subscription");
            List list2 = this.mPrimarySubList;
            int defaultVoiceSubId = this.mSubscriptionManagerService.getDefaultVoiceSubId();
            final SubscriptionManagerService subscriptionManagerService2 = this.mSubscriptionManagerService;
            Objects.requireNonNull(subscriptionManagerService2);
            boolean voiceSelected = updateDefaultValue(list2, defaultVoiceSubId, new MultiSimSettingController.UpdateDefaultAction() { // from class: com.mediatek.internal.telephony.MtkMultiSimSettingController$$ExternalSyntheticLambda1
                public final void update(int i2) {
                    subscriptionManagerService2.setDefaultVoiceSubId(i2);
                }
            });
            log("updateDefaultValues: Update default sms subscription");
            List list3 = this.mPrimarySubList;
            int defaultSmsSubId = this.mSubscriptionManagerService.getDefaultSmsSubId();
            final SubscriptionManagerService subscriptionManagerService3 = this.mSubscriptionManagerService;
            Objects.requireNonNull(subscriptionManagerService3);
            boolean smsSelected = updateDefaultValue(list3, defaultSmsSubId, new MultiSimSettingController.UpdateDefaultAction() { // from class: com.mediatek.internal.telephony.MtkMultiSimSettingController$$ExternalSyntheticLambda2
                public final void update(int i2) {
                    subscriptionManagerService3.setDefaultSmsSubId(i2);
                }
            }, this.mIsAskEverytimeSupportedForSms);
            boolean autoFallbackEnabled = this.mContext.getResources().getBoolean(R.bool.config_settingsHelpLinksEnabled);
            if (!autoFallbackEnabled) {
                sendSubChangeNotificationIfNeeded(change, dataSelected, voiceSelected, smsSelected);
            } else {
                updateUserPreferences(this.mPrimarySubList, dataSelected, voiceSelected, smsSelected);
            }
        }
    }

    protected MultiSimSettingController.SimCombinationWarningParams getSimCombinationWarningParams(int change) {
        MultiSimSettingController.SimCombinationWarningParams params = new MultiSimSettingController.SimCombinationWarningParams(this);
        return params;
    }

    protected void disableDataForNonDefaultNonOpportunisticSubscriptions() {
    }

    private void initSimMeLock(Context context) {
        if (MtkTelephonyManagerEx.getDefault().getSimLockPolicy() != 0) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.mediatek.phone.ACTION_SIM_SLOT_LOCK_POLICY_INFORMATION");
            context.registerReceiver(new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkMultiSimSettingController.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    String action = intent.getAction();
                    if (action == null) {
                        return;
                    }
                    MtkMultiSimSettingController.this.log("onReceive: action=" + action);
                    if (action.equals("com.mediatek.phone.ACTION_SIM_SLOT_LOCK_POLICY_INFORMATION")) {
                        MtkMultiSimSettingController.this.updateDefaults();
                    }
                }
            }, filter, 4);
            this.mSimMeLockSupport = DBG;
            log("initSimMeLock done.");
        }
    }

    private boolean updateDefaultDataForSimMeLock(int subCount) {
        if (!this.mSimMeLockSupport) {
            return false;
        }
        int lockCount = 0;
        int unlockCount = 0;
        int unlockSubId = -1;
        int phoneCount = ((TelephonyManager) this.mContext.getSystemService("phone")).getActiveModemCount();
        for (int i = 0; i < phoneCount; i++) {
            if (!DataSubSelectorUtil.isActiveSub(this.mContext, i)) {
                log("slot " + i + " has no active sub, ignore it.");
            } else if (this.mSimCloseMode == 2 && MtkTelephonyManagerEx.getDefault().getSimOnOffState(i) == 10) {
                log("slot " + i + " is closed sub(mtk sim on/off), ignore it.");
            } else if (this.mSimCloseMode == 1 && isRadioOffBySimManagement(SubscriptionManager.getSubscriptionId(i))) {
                log("slot " + i + " is closed sub(mtk radio on/off), ignore it.");
            } else {
                int status = MtkTelephonyManagerEx.getDefault().getShouldServiceCapability(i);
                switch (status) {
                    case 0:
                        unlockCount++;
                        unlockSubId = SubscriptionManager.getSubscriptionId(i);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                        lockCount++;
                        break;
                    case 4:
                    default:
                        log("sim me lock temp sim status:" + status + ", wait for next.");
                        return DBG;
                }
            }
        }
        if (unlockCount == 1 && subCount > unlockCount) {
            log("sim me lock set only unlock sim as default data between multi sims.");
            this.mSubscriptionManagerService.setDefaultDataSubId(unlockSubId);
            this.mInitialHandling = false;
            return DBG;
        }
        if (subCount == lockCount) {
            log("sim me lock block set lock sim as default data.");
            return DBG;
        }
        log("subCount:" + subCount + ", lock:" + lockCount + ", unlock:" + unlockCount);
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initSimCloseMode(Context context) {
        this.mSimCloseMode = MtkTelephonyManagerEx.getDefault().simSwitchMode();
        log("sim close mode is " + this.mSimCloseMode);
        int i = this.mSimCloseMode;
        Handler handler = null;
        if (i == 1) {
            context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("msim_mode_setting"), false, new ContentObserver(handler) { // from class: com.mediatek.internal.telephony.MtkMultiSimSettingController.2
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    MtkMultiSimSettingController.this.log("radio on/off status changed.");
                    MtkMultiSimSettingController.this.notifySubscriptionInfoChanged();
                }
            });
            return;
        }
        if (i == 2) {
            MtkRIL[] commandsInterfaces = PhoneFactory.getCommandsInterfaces();
            this.mActiveModemCount = commandsInterfaces.length;
            for (int i2 = 0; i2 < this.mActiveModemCount; i2++) {
                commandsInterfaces[i2].registerForSimPower(this, 4, null);
            }
        }
    }

    private void filterOutClosedSim(List<SubscriptionInfo> activeSubInfos) {
        if (activeSubInfos != null) {
            Iterator<SubscriptionInfo> iterator = activeSubInfos.iterator();
            while (iterator.hasNext()) {
                SubscriptionInfo subInfo = iterator.next();
                int subId = subInfo.getSubscriptionId();
                if (isSimClosed(subId)) {
                    iterator.remove();
                    log("sub " + subId + " is closed, remove from active.");
                }
            }
        }
    }

    private boolean isSimClosed(int subId) {
        int i = this.mSimCloseMode;
        if (i == 1) {
            return isRadioOffBySimManagement(subId);
        }
        if (i == 2) {
            int id = this.mSubscriptionManagerService.getPhoneId(subId);
            if (MtkTelephonyManagerEx.getDefault().getSimOnOffState(id) == 10) {
                return DBG;
            }
            return false;
        }
        return false;
    }

    private boolean isRadioOffBySimManagement(int subId) {
        try {
            IMtkTelephonyEx iTelEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
            if (iTelEx == null) {
                log("iTelEx is null!");
                return false;
            }
            boolean result = iTelEx.isRadioOffBySimManagement(subId);
            return result;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isAnySimNotReady(int activeSubCount) {
        int simCount = 0;
        int phoneCount = ((TelephonyManager) this.mContext.getSystemService("phone")).getActiveModemCount();
        List<SubscriptionInfo> availableSubInfos = this.mSubscriptionManagerService.getAvailableSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
        for (int i = 0; i < phoneCount; i++) {
            String iccid = "";
            if (ctrl != null) {
                iccid = ctrl.getIccid(i);
            }
            if (iccid.equals("")) {
                log("isAnySimNotReady sim" + i + " not ready, max:" + phoneCount);
                return DBG;
            }
            if (iccid.length() > 3) {
                boolean isEnabled = DBG;
                for (SubscriptionInfo subInfo : availableSubInfos) {
                    if (IccUtils.compareIgnoreTrailingFs(iccid, subInfo.getIccId()) && (!subInfo.areUiccApplicationsEnabled() || isSimClosed(subInfo.getSubscriptionId()))) {
                        log("isAnySimNotReady sim" + i + " disabled");
                        isEnabled = false;
                        break;
                    }
                }
                if (isEnabled) {
                    simCount++;
                }
            }
        }
        log("isAnySimNotReady simCount:" + simCount + " activeSubCount:" + activeSubCount);
        if (activeSubCount != simCount) {
            return DBG;
        }
        return false;
    }

    private boolean updateDefaultDataForSubSidyLock(int subCount) {
        if (RadioCapabilitySwitchUtil.isSubsidyLockForOmSupported() && subCount > 1) {
            return this.mDataSubSelector.getSimSwitchForDSSExt().checkCapSwitch(-1);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        Log.d(LOG_TAG, msg);
    }
}
