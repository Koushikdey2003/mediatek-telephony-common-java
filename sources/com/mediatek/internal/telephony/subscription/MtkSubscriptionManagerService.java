package com.mediatek.internal.telephony.subscription;

import android.R;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.RadioAccessFamily;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.subscription.SubscriptionDatabaseManager;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.MtkDefaultSmsSimSettings;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.uicc.MtkSpnOverride;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class MtkSubscriptionManagerService extends SubscriptionManagerService {
    private static final boolean DBG = false;
    private static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 103;
    private static final int EVENT_RADIO_AVAILABLE = 101;
    private static final int EVENT_RADIO_UNAVAILABLE = 102;
    private static final int EVENT_SIM_MOUNT_CHANGED = 106;
    private static final String ICCID_STRING_FOR_NO_SIM = "N/A";
    private static final String LOG_TAG = "MTK_SMSVC";
    private static final String MTK_SIM_ON_OFF = "2";
    private static final String PROPERTY_SIM_CARD_ON_OFF = "ro.vendor.mtk_sim_card_onoff";
    private static final String PROPERTY_SML_MODE = "ro.vendor.sim_me_lock_mode";
    private static final int sReadIccIdRetryTime = 1000;
    private final AppOpsManager mAppOpsManager;
    private final Context mContext;
    private boolean mIsSmlLockMode;
    private boolean[] mIsUpdateAvailable;
    private final Object mLockUpdate;
    private Handler mMtkHandler;
    private BroadcastReceiver mMtkReceiver;
    private Runnable mReadCurrentIccIdRunnable;
    private int mReadIccIdCount;
    private String mSimOnOffMode;
    private boolean mSubscriptionDatabaseManagerInitialized;
    private final SubscriptionManager mSubscriptionManager;
    private final TelephonyManager mTelephonyManager;
    private final UiccController mUiccController;
    private int[] newSmlDt;
    private int[] newSmlInfo;
    private int[] oldSmlDt;
    private int[] oldSmlInfo;
    private static Intent sStickyIntent = null;
    private static final int SUPPORTED_MODEM_COUNT = TelephonyManager.getDefault().getSupportedModemCount();

    protected void onSubscriptionDatabaseManagerInitialized() {
        this.mSubscriptionDatabaseManagerInitialized = true;
        updateSubscriptionForRadioAvailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubscriptionForRadioAvailable() {
        if (this.mSubscriptionDatabaseManagerInitialized && checkIsAvailable()) {
            this.mReadIccIdCount = 0;
            if (!checkAllIccIdReady()) {
                this.mMtkHandler.postDelayed(this.mReadCurrentIccIdRunnable, 1000L);
            } else {
                updateSubscriptionInfoIfNeed();
            }
        }
    }

    public MtkSubscriptionManagerService(Context context, Looper looper) {
        super(context, looper);
        this.mLockUpdate = new Object();
        this.newSmlInfo = new int[]{4, 0, -1, -1, -1, -1};
        this.oldSmlInfo = new int[]{4, 0, -1, -1, -1, -1};
        this.newSmlDt = new int[]{4, 4, 4, 4};
        this.oldSmlDt = new int[]{4, 4, 4, 4};
        this.mIsSmlLockMode = SystemProperties.get(PROPERTY_SML_MODE, "").equals("3");
        this.mIsUpdateAvailable = new boolean[SUPPORTED_MODEM_COUNT];
        this.mReadIccIdCount = 0;
        this.mSimOnOffMode = SystemProperties.get(PROPERTY_SIM_CARD_ON_OFF);
        this.mSubscriptionDatabaseManagerInitialized = false;
        this.mMtkHandler = new Handler() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                Integer index = MtkSubscriptionManagerService.this.getCiIndex(msg);
                switch (msg.what) {
                    case 101:
                        MtkSubscriptionManagerService.this.log("handleMessage : <EVENT_RADIO_AVAILABLE> SIM" + (index.intValue() + 1));
                        MtkSubscriptionManagerService.this.mIsUpdateAvailable[index.intValue()] = true;
                        MtkSubscriptionManagerService.this.updateSubscriptionForRadioAvailable();
                        return;
                    case 102:
                        MtkSubscriptionManagerService.this.log("handleMessage : <EVENT_RADIO_UNAVAILABLE> SIM" + (index.intValue() + 1));
                        MtkSubscriptionManagerService.this.mIsUpdateAvailable[index.intValue()] = false;
                        return;
                    case 103:
                        MtkSubscriptionManagerService.this.onMultiSimConfigChanged();
                        return;
                    case 104:
                    case 105:
                    default:
                        super.handleMessage(msg);
                        return;
                    case MtkSubscriptionManagerService.EVENT_SIM_MOUNT_CHANGED /* 106 */:
                        synchronized (MtkSubscriptionManagerService.this.mLockUpdate) {
                            MtkSubscriptionManagerService mtkSubscriptionManagerService = MtkSubscriptionManagerService.this;
                            mtkSubscriptionManagerService.updateNewSmlInfo(mtkSubscriptionManagerService.newSmlDt[index.intValue()], MtkSubscriptionManagerService.this.newSmlInfo[1], index.intValue());
                            MtkSubscriptionManagerService.this.resetSimMountChangeState(index.intValue());
                            break;
                        }
                        return;
                }
            }
        };
        this.mReadCurrentIccIdRunnable = new Runnable() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService.2
            @Override // java.lang.Runnable
            public void run() {
                MtkSubscriptionManagerService.this.mReadIccIdCount++;
                if (MtkSubscriptionManagerService.this.mReadIccIdCount <= 10) {
                    if (!MtkSubscriptionManagerService.this.checkAllIccIdReady()) {
                        MtkSubscriptionManagerService.this.mMtkHandler.postDelayed(MtkSubscriptionManagerService.this.mReadCurrentIccIdRunnable, 1000L);
                    } else {
                        MtkSubscriptionManagerService.this.updateSubscriptionInfoIfNeed();
                    }
                }
            }
        };
        this.mMtkReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                MtkSubscriptionManagerService.this.log("onReceive, Action: " + action);
                if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                    int[] subIdList = MtkSubscriptionManagerService.this.mSubscriptionManager.getActiveSubscriptionIdList();
                    for (int subId : subIdList) {
                        MtkSubscriptionManagerService.this.updateSubName(subId);
                    }
                }
            }
        };
        log("MtkSubscriptionManagerService");
        this.mContext = context;
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mUiccController = UiccController.getInstance();
        this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
        if (DataSubConstants.OPERATOR_OP09.equals(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR))) {
            intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        }
        context.registerReceiver(this.mMtkReceiver, intentFilter);
        CommandsInterface[] ci = PhoneFactory.getCommandsInterfaces();
        for (int i = 0; i < ci.length; i++) {
            Integer index = new Integer(i);
            ci[i].registerForNotAvailable(this.mMtkHandler, 102, index);
            ci[i].registerForAvailable(this.mMtkHandler, 101, index);
        }
        PhoneConfigurationManager.registerForMultiSimConfigChange(this.mMtkHandler, 103, (Object) null);
    }

    public static MtkSubscriptionManagerService getInstance() {
        return (MtkSubscriptionManagerService) SubscriptionManagerService.getInstance();
    }

    protected SubscriptionDatabaseManager onCreateSubscriptionDatabaseManager(Context context, Looper looper, SubscriptionDatabaseManager.SubscriptionDatabaseManagerCallback callback) {
        log("create MtkSubscriptionDatabaseManager");
        return new MtkSubscriptionDatabaseManager(context, looper, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkAllIccIdReady() {
        log("checkAllIccIdReady +, retry_count = " + this.mReadIccIdCount);
        for (int i = 0; i < TelephonyManager.getDefault().getActiveModemCount(); i++) {
            String iccId = getIccId(i);
            if (iccId.length() == 3) {
                log("No SIM insert :" + i);
            }
            if (iccId.equals("")) {
                return false;
            }
            log("iccId[" + i + "] = " + SubscriptionInfo.getPrintableId(iccId));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubscriptionInfoIfNeed() {
        for (int i = 0; i < TelephonyManager.getDefault().getActiveModemCount(); i++) {
            String iccId = getIccId(i);
            if (!TextUtils.isEmpty(iccId) && !TextUtils.equals(iccId, "N/A")) {
                updateSubscription(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Integer getCiIndex(Message msg) {
        Integer index = new Integer(0);
        if (msg != null) {
            if (msg.obj != null && (msg.obj instanceof Integer)) {
                return (Integer) msg.obj;
            }
            if (msg.obj != null && (msg.obj instanceof AsyncResult)) {
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.userObj != null && (ar.userObj instanceof Integer)) {
                    return (Integer) ar.userObj;
                }
                return index;
            }
            return index;
        }
        return index;
    }

    private boolean checkIsAvailable() {
        boolean result = true;
        int i = 0;
        while (true) {
            if (i >= TelephonyManager.getDefault().getActiveModemCount()) {
                break;
            }
            if (this.mIsUpdateAvailable[i]) {
                i++;
            } else {
                log("mIsUpdateAvailable[" + i + "] = " + this.mIsUpdateAvailable[i]);
                result = false;
                break;
            }
        }
        log("checkIsAvailable result = " + result);
        return result;
    }

    protected void onMultiSimConfigChanged() {
        int activeModemCount = ((TelephonyManager) this.mContext.getSystemService("phone")).getActiveModemCount();
        loge("onMultiSimConfigChangedcase activeModemCount=" + activeModemCount + ", SUPPORTED_MODEM_COUNT=" + SUPPORTED_MODEM_COUNT);
        for (int phoneId = activeModemCount; phoneId < SUPPORTED_MODEM_COUNT; phoneId++) {
            this.mIsUpdateAvailable[phoneId] = false;
        }
    }

    protected String getIccId(int phoneId) {
        MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
        return ctrl == null ? "" : TextUtils.emptyIfNull(IccUtils.stripTrailingFs(ctrl.getIccid(phoneId)));
    }

    protected boolean onCheckIfSubscriptionLoaded(int phoneId) {
        return getSubId(phoneId) != -1 || TextUtils.equals(getIccId(phoneId), "N/A");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubName(int subId) {
        String nameToSet;
        SubscriptionInfo subInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(subId);
        if (subInfo != null && subInfo.getDisplayNameSource() != 2) {
            MtkSpnOverride spnOverride = MtkSpnOverride.getInstance();
            String carrierName = TelephonyManager.getDefault().getSimOperator(subId);
            int slotId = SubscriptionManager.getSlotIndex(subId);
            log("updateSubName, carrierName = " + carrierName + ", subId = " + subId);
            if (SubscriptionManager.isValidSlotIndex(slotId)) {
                if (spnOverride.containsCarrierEx(carrierName)) {
                    nameToSet = spnOverride.lookupOperatorName(subId, carrierName, true, this.mContext);
                    log("SPN found, name = " + nameToSet);
                } else {
                    nameToSet = "CARD " + Integer.toString(slotId + 1);
                    log("SPN not found, set name to " + nameToSet);
                }
                setDisplayNameUsingSrc(nameToSet, subId, 0);
            }
        }
    }

    protected void onHandleSimLoaded(int phoneId) {
        String nameToSet;
        int subId = getSubId(phoneId);
        if (subId == -1) {
            log("updateDisplayNameForMvno: No subscription for phone " + phoneId);
            return;
        }
        SubscriptionInfoInternal subInfo = getSubscriptionInfoInternal(subId);
        String simCarrierName = this.mTelephonyManager.getSimOperatorName(subId);
        if (subInfo != null && subInfo.getDisplayNameSource() != 2) {
            String simNumeric = this.mTelephonyManager.getSimOperatorNumeric(subId);
            String simMvnoName = MtkSpnOverride.getInstance().lookupOperatorNameForDisplayName(subId, simNumeric, true, this.mContext);
            log("[handleSimLoaded]- simNumeric: " + simNumeric + ", simMvnoName: " + simMvnoName + ", simCarrierName: " + simCarrierName);
            if (!TextUtils.isEmpty(simMvnoName)) {
                nameToSet = simMvnoName;
            } else if (!TextUtils.isEmpty(simCarrierName)) {
                nameToSet = simCarrierName;
            } else {
                Resources r = Resources.getSystem();
                nameToSet = r.getString(R.string.config_wimaxStateTrackerClassname, Integer.valueOf(phoneId + 1));
            }
            log("sim name = " + nameToSet + ", nameSource = " + subInfo.getDisplayNameSource());
            this.mSubscriptionDatabaseManager.setDisplayName(subId, nameToSet);
        }
    }

    public boolean areUiccAppsEnabledOnCard(int phoneId) {
        String iccId = getIccId(phoneId);
        if (TextUtils.isEmpty(iccId)) {
            loge("[areUiccAppsDisabledOnCard] iccid[" + phoneId + "] is empty.");
            return false;
        }
        log("[areUiccAppsDisabledOnCard] iccid[" + phoneId + "]: " + SubscriptionInfo.getPrintableId(iccId));
        if (iccId.equals("N/A")) {
            UiccPort port = UiccController.getInstance().getUiccPort(phoneId);
            iccId = port == null ? null : port.getIccId();
            if (iccId == null) {
                return false;
            }
        }
        SubscriptionInfoInternal subInfo = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(IccUtils.stripTrailingFs(iccId));
        loge("[areUiccAppsDisabledOnCard] subInfo[" + phoneId + "]: " + subInfo);
        return subInfo != null && subInfo.areUiccApplicationsEnabled();
    }

    protected void updateSubscription(int phoneId) {
        String oldIccId;
        String decIccId;
        int detectedType;
        String simMvnoName;
        String nameToSet;
        int simState = this.mSimState[phoneId];
        if (simState == 6) {
            IccCard iccCard = PhoneFactory.getPhone(phoneId).getIccCard();
            if (!iccCard.isEmptyProfile() && areUiccAppsEnabledOnCard(phoneId)) {
                log("updateSubscriptions: SIM_STATE_NOT_READY is not a final state. Will update subscription later.");
                return;
            }
        }
        SubscriptionInfoInternal oldSubInfo = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(getIccId(phoneId));
        super.updateSubscription(phoneId);
        int subId = getSubId(phoneId);
        if (oldSubInfo == null && subId != -1) {
            String simCarrierName = this.mTelephonyManager.getSimOperatorName(subId);
            String simNumeric = this.mTelephonyManager.getSimOperatorNumeric(subId);
            if (!"20404".equals(simNumeric) || !TextUtils.isEmpty(simCarrierName)) {
                simMvnoName = MtkSpnOverride.getInstance().lookupOperatorNameForDisplayName(subId, simNumeric, true, this.mContext);
            } else {
                simMvnoName = "";
            }
            if (!TextUtils.isEmpty(simMvnoName)) {
                nameToSet = simMvnoName;
            } else if (!TextUtils.isEmpty(simCarrierName)) {
                nameToSet = simCarrierName;
            } else {
                Resources r = Resources.getSystem();
                nameToSet = r.getString(R.string.config_wimaxStateTrackerClassname, Integer.valueOf(phoneId + 1));
            }
            this.mSubscriptionDatabaseManager.setDisplayName(subId, nameToSet);
        }
        List<SubscriptionInfo> subInfoList = getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
        if (areAllSubscriptionsLoaded()) {
            MtkDefaultSmsSimSettings.setSmsTalkDefaultSim(subInfoList, this.mContext);
            if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) == 1 && SystemProperties.getInt("ro.vendor.mtk_non_dsda_rsim_support", 0) == 1) {
                int rsimPhoneId = SystemProperties.getInt("vendor.gsm.prefered.rsim.slot", -1);
                int rsimSubId = getSubId(rsimPhoneId);
                if (rsimPhoneId >= 0 && rsimPhoneId < TelephonyManager.getDefault().getActiveModemCount() && SubscriptionManager.isValidSubscriptionId(rsimSubId)) {
                    setDefaultDataSubId(rsimSubId);
                }
            }
        }
        String iccId = getIccId(phoneId);
        if (oldSubInfo == null || oldSubInfo.getSimSlotIndex() != phoneId) {
            oldIccId = "N/A";
        } else {
            String oldIccId2 = oldSubInfo.getIccId();
            oldIccId = oldIccId2;
        }
        log("updateSubscriptions, phoneId:" + phoneId + " oldIccId:" + SubscriptionInfo.getPrintableId(oldIccId));
        if (iccId == null) {
            decIccId = null;
        } else {
            String decIccId2 = IccUtils.getDecimalSubstring(iccId);
            decIccId = decIccId2;
        }
        if (iccId != null && iccId.equals("N/A") && !oldIccId.equals("N/A")) {
            detectedType = 2;
        } else if (isNewSim(iccId, decIccId, oldIccId)) {
            detectedType = 1;
        } else {
            detectedType = 4;
        }
        int subCount = subInfoList != null ? subInfoList.size() : 0;
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        int detectedType2 = detectedType;
        putSubinfoRecordUpdatedExtra(intent, phoneId, detectedType, subCount, null);
        broadcastSimInfoContentChanged(intent);
        if (this.mIsSmlLockMode) {
            synchronized (this.mLockUpdate) {
                updateNewSmlInfo(detectedType2, subCount, phoneId);
                resetSimMountChangeState(phoneId);
            }
        }
    }

    protected boolean needSkipUpdateSubscription(int simState) {
        if (simState == 6 && "2".equals(this.mSimOnOffMode)) {
            log("SIM state is NOT_READY but MTK SIM on-off is enabled.");
            return false;
        }
        return super.needSkipUpdateSubscription(simState);
    }

    public void putSubinfoRecordUpdatedExtra(Intent intent, int phoneId, int detectedType, int subCount, String propKey) {
        log("putSubinfoRecordUpdatedExtra: phoneId = " + phoneId + " detectedType = " + detectedType + " subCount = " + subCount + " propKey = " + propKey);
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds != null && subIds.length > 0) {
            SubscriptionManager.putPhoneIdAndSubIdExtra(intent, phoneId, subIds[0]);
        } else {
            log("putSubinfoRecordUpdatedExtra: no valid subs");
            intent.putExtra("phone", phoneId);
            intent.putExtra("slot", phoneId);
        }
        intent.putExtra("simDetectStatus", detectedType);
        intent.putExtra("simCount", subCount);
        if (propKey == null) {
            intent.putExtra("simPropKey", "");
        } else {
            intent.putExtra("simPropKey", propKey);
        }
    }

    private void broadcastSimInfoContentChanged(Intent intentExt) {
        this.mContext.sendBroadcast(new Intent("com.mediatek.intent.action.ACTION_SUBINFO_CONTENT_CHANGE"));
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        if (intentExt == null) {
            List<SubscriptionInfo> subInfoList = getActiveSubscriptionInfoList(this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            int subCount = subInfoList == null ? 0 : subInfoList.size();
            putSubinfoRecordUpdatedExtra(intent, -1, 4, subCount, null);
        }
        synchronized (MtkSubscriptionManagerService.class) {
            Intent intent2 = intentExt == null ? intent : intentExt;
            sStickyIntent = intent2;
            intent2.getIntExtra("simDetectStatus", 0);
            sStickyIntent.getIntExtra("phone", -1);
            this.mContext.sendStickyBroadcast(sStickyIntent);
        }
    }

    private boolean isNewSim(String iccId, String decIccId, String oldIccId) {
        boolean newSim = true;
        if (iccId != null && oldIccId != null && (oldIccId.indexOf(iccId) == 0 || oldIccId.indexOf(iccId.toLowerCase()) == 0)) {
            newSim = false;
        } else if (decIccId != null && decIccId.equals(oldIccId)) {
            newSim = false;
        }
        log("isNewSim newSim = " + newSim);
        return newSim;
    }

    public String getSubscriptionProperty(int subId, String columnName, String callingPackage, String callingFeatureId) {
        if (!"vonr_ui_enabled".equals(columnName)) {
            return super.getSubscriptionProperty(subId, columnName, callingPackage, callingFeatureId);
        }
        this.mAppOpsManager.checkPackage(Binder.getCallingUid(), callingPackage);
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, subId, callingPackage, callingFeatureId, "getSubscriptionProperty")) {
            throw new SecurityException("Need READ_PHONE_STATE, READ_PRIVILEGED_PHONE_STATE, or carrier privilege");
        }
        long token = Binder.clearCallingIdentity();
        try {
            try {
                Object value = this.mSubscriptionDatabaseManager.getSubscriptionProperty(subId, columnName);
                if (value instanceof Integer) {
                    return String.valueOf(value);
                }
                if (value instanceof String) {
                    return (String) value;
                }
                throw new RuntimeException("Unexpected type " + value.getClass().getTypeName() + " was returned from SubscriptionDatabaseManager for column " + columnName);
            } catch (IllegalArgumentException e) {
                log("getSubscriptionProperty: Invalid subId " + subId + ", columnName=" + columnName);
                Binder.restoreCallingIdentity(token);
                return null;
            }
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    public void setSubscriptionProperty(int subId, String columnName, String value) {
        if ("vonr_ui_enabled".equals(columnName)) {
            enforcePermissions("setSubscriptionProperty", "android.permission.MODIFY_PHONE_STATE");
            long token = Binder.clearCallingIdentity();
            try {
                this.mSubscriptionDatabaseManager.setSubscriptionProperty(subId, columnName, value);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        } else {
            super.setSubscriptionProperty(subId, columnName, value);
        }
        Intent intent = new Intent("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        putSubinfoRecordUpdatedExtra(intent, getPhoneId(subId), 4, getActiveSubIdList(false).length, columnName);
        broadcastSimInfoContentChanged(intent);
    }

    private void enforcePermissions(String message, String... permissions) {
        for (String permission : permissions) {
            if (this.mContext.checkCallingOrSelfPermission(permission) == 0) {
                return;
            }
        }
        throw new SecurityException(message + ". Does not have any of the following permissions. " + Arrays.toString(permissions));
    }

    public void setDefaultDataSubId(int subId) {
        int raf;
        enforcePermissions("setDefaultDataSubId", "android.permission.MODIFY_PHONE_STATE");
        if (subId == Integer.MAX_VALUE) {
            throw new RuntimeException("setDefaultDataSubId called with DEFAULT_SUBSCRIPTION_ID");
        }
        long token = Binder.clearCallingIdentity();
        try {
            ProxyController proxyController = ProxyController.getInstance();
            int len = TelephonyManager.from(this.mContext).getActiveModemCount();
            log("[setDefaultDataSubId] num phones=" + len + ", subId=" + subId);
            if (proxyController != null && SubscriptionManager.isValidSubscriptionId(subId)) {
                RadioAccessFamily[] rafs = new RadioAccessFamily[len];
                boolean atLeastOneMatch = false;
                for (int phoneId = 0; phoneId < len; phoneId++) {
                    Phone phone = PhoneFactory.getPhone(phoneId);
                    int id = phone.getSubId();
                    if (id == subId) {
                        raf = proxyController.getMaxRafSupported();
                        atLeastOneMatch = true;
                    } else {
                        raf = proxyController.getMinRafSupported();
                    }
                    log("[setDefaultDataSubId] phoneId=" + phoneId + " subId=" + id + " RAF=" + raf);
                    rafs[phoneId] = new RadioAccessFamily(phoneId, raf);
                }
                if (atLeastOneMatch) {
                    proxyController.setRadioCapability(rafs);
                }
            }
            Binder.restoreCallingIdentity(token);
            super.setDefaultDataSubId(subId);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public SubscriptionInfo getSubInfoForIccId(String iccId) {
        SubscriptionInfoInternal subInfo = this.mSubscriptionDatabaseManager.getSubscriptionInfoInternalByIccId(IccUtils.stripTrailingFs(iccId));
        if (subInfo != null) {
            return subInfo.toSubscriptionInfo();
        }
        return null;
    }

    public List<SubscriptionInfo> getSubInfoUsingSlotIndexPrivileged(int slotIndex) {
        if (slotIndex == Integer.MAX_VALUE) {
            slotIndex = getSlotIndex(getDefaultSubId());
        }
        if (!SubscriptionManager.isValidSlotIndex(slotIndex)) {
            return null;
        }
        final int i = slotIndex;
        return (List) this.mSubscriptionDatabaseManager.getAllSubscriptions().stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return MtkSubscriptionManagerService.lambda$getSubInfoUsingSlotIndexPrivileged$0(i, (SubscriptionInfoInternal) obj);
            }
        }).map(new Function() { // from class: com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((SubscriptionInfoInternal) obj).toSubscriptionInfo();
            }
        }).collect(Collectors.toList());
    }

    static /* synthetic */ boolean lambda$getSubInfoUsingSlotIndexPrivileged$0(int i, SubscriptionInfoInternal subInfo) {
        return subInfo.getSimSlotIndex() == i;
    }

    public void triggerUpdateInternalSimMountState(int slotId) {
        log("triggerUpdateInternalSimMountState slotId " + slotId);
        Handler handler = this.mMtkHandler;
        handler.sendMessage(handler.obtainMessage(EVENT_SIM_MOUNT_CHANGED, Integer.valueOf(slotId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSimMountChangeState(int slotId) {
        boolean needReport;
        this.newSmlInfo[0] = this.newSmlDt[slotId];
        this.oldSmlInfo[0] = this.oldSmlDt[slotId];
        int i = 0;
        while (true) {
            if (i >= 6) {
                needReport = false;
                break;
            } else if (this.newSmlInfo[i] == this.oldSmlInfo[i]) {
                i++;
            } else {
                needReport = true;
                break;
            }
        }
        if (!needReport) {
            log("resetSimMountChangeState no  need report ");
            return;
        }
        int[] iArr = this.newSmlInfo;
        int newDetectedType = iArr[0];
        int newSimCount = iArr[1];
        int newValid1 = iArr[2];
        int newValid2 = iArr[3];
        int newValid3 = iArr[4];
        int newValid4 = iArr[5];
        Intent intent = new Intent("com.mediatek.phone.ACTION_SIM_SLOT_SIM_MOUNT_CHANGE");
        intent.putExtra("DETECTED_TYPE", newDetectedType);
        intent.putExtra("SML_SIM_COUNT", newSimCount);
        intent.putExtra("SML_SIM1_VALID", newValid1);
        intent.putExtra("SML_SIM2_VALID", newValid2);
        intent.putExtra("SML_SIM3_VALID", newValid3);
        intent.putExtra("SML_SIM4_VALID", newValid4);
        intent.putExtra("phone", slotId);
        intent.putExtra("slot", slotId);
        log("Broadcasting ACTION_SIM_SLOT_SIM_MOUNT_CHANGE,  [" + newDetectedType + ", " + newSimCount + ", " + newValid1 + ", " + newValid2 + ", " + newValid3 + ", " + newValid4 + ", " + this.newSmlDt[slotId] + ", " + slotId + "]");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        updateOldSmlInfo(newDetectedType, newSimCount, newValid1, newValid2, newValid3, newValid4, slotId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNewSmlInfo(int detectedType, int simCount, int slotId) {
        this.newSmlDt[slotId] = detectedType;
        int[] iArr = this.newSmlInfo;
        iArr[0] = detectedType;
        iArr[1] = simCount;
        iArr[2] = MtkTelephonyManagerEx.getDefault().checkValidCard(0);
        this.newSmlInfo[3] = MtkTelephonyManagerEx.getDefault().checkValidCard(1);
        this.newSmlInfo[4] = MtkTelephonyManagerEx.getDefault().checkValidCard(2);
        this.newSmlInfo[5] = MtkTelephonyManagerEx.getDefault().checkValidCard(3);
        log("[updateNewSmlInfo]- [" + this.newSmlInfo[0] + ", " + this.newSmlInfo[1] + ", " + this.newSmlInfo[2] + ", " + this.newSmlInfo[3] + ", " + this.newSmlInfo[4] + ", " + this.newSmlInfo[5] + ", " + this.newSmlDt[slotId] + ", " + slotId + "]");
    }

    private void updateOldSmlInfo(int detectedType, int simCount, int valid1, int valid2, int valid3, int valid4, int slotId) {
        this.oldSmlDt[slotId] = detectedType;
        int[] iArr = this.oldSmlInfo;
        iArr[0] = detectedType;
        iArr[1] = simCount;
        iArr[2] = valid1;
        iArr[3] = valid2;
        iArr[4] = valid3;
        iArr[5] = valid4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d(LOG_TAG, s);
    }

    private void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }
}
