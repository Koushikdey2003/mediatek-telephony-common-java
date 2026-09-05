package com.mediatek.internal.telephony.datasub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.mediatek.internal.telephony.OpTelephonyCustomizationFactoryBase;
import com.mediatek.internal.telephony.OpTelephonyCustomizationUtils;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DataSubSelector {
    private static final boolean DBG = true;
    private static final String LOG_TAG = "DSSelector";
    private static String mOperatorSpec;
    protected final BroadcastReceiver mBroadcastReceiver;
    private CapabilitySwitch mCapabilitySwitch;
    private Context mContext;
    private IDataSubSelectorOPExt mDataSubSelectorOPExt;
    private ContentObserver mPrefNetworkModeObserver;
    private Handler mProtocolHandler;
    private ISimSwitchForDSSExt mSimSwitchForDSSExt;
    private static final boolean USER_BUILD = TextUtils.equals(Build.TYPE, DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    private static String ACTION_BOOT_COMPLETE = "android.intent.action.BOOT_COMPLETED";
    private OpTelephonyCustomizationFactoryBase mTelephonyCustomizationFactory = null;
    private boolean mIsWaitIccid = false;
    private boolean mIsNeedPreCheck = DBG;
    private boolean mIsNeedWaitAirplaneModeOff = false;
    private boolean mIsNeedWaitAirplaneModeOffRoaming = false;
    private boolean mAirplaneModeOn = false;
    private Intent mIntent = null;
    private boolean mIsInRoaming = false;

    public IDataSubSelectorOPExt getDataSubSelectorOpExt() {
        return this.mDataSubSelectorOPExt;
    }

    public ISimSwitchForDSSExt getSimSwitchForDSSExt() {
        return this.mSimSwitchForDSSExt;
    }

    public DataSubSelector(Context context) {
        this.mDataSubSelectorOPExt = null;
        this.mSimSwitchForDSSExt = null;
        this.mContext = null;
        this.mCapabilitySwitch = null;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.datasub.DataSubSelector.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                DataSubSelector.this.log("onReceive: action=" + action);
                if (action.equals("android.telephony.action.SIM_APPLICATION_STATE_CHANGED")) {
                    DataSubSelector.this.handleSimStateChanged(intent);
                    return;
                }
                if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                    int nDefaultDataSubId = intent.getIntExtra("subscription", -1);
                    DataSubSelector.this.log("nDefaultDataSubId: " + nDefaultDataSubId);
                    DataSubSelector.this.handleDefaultDataChanged(intent);
                    return;
                }
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    DataSubSelector.this.mAirplaneModeOn = intent.getBooleanExtra("state", false);
                    DataSubSelector.this.log("ACTION_AIRPLANE_MODE_CHANGED, enabled = " + DataSubSelector.this.mAirplaneModeOn);
                    if (!DataSubSelector.this.mAirplaneModeOn) {
                        if (DataSubSelector.this.mIsNeedWaitAirplaneModeOff) {
                            DataSubSelector.this.mIsNeedWaitAirplaneModeOff = false;
                            DataSubSelector.this.handleAirPlaneModeOff(intent);
                        }
                        if (DataSubSelector.this.mIsNeedWaitAirplaneModeOffRoaming) {
                            DataSubSelector.this.mIsNeedWaitAirplaneModeOffRoaming = false;
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (action.equals("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED")) {
                    DataSubSelector.this.handleSubinfoRecordUpdated(intent);
                    return;
                }
                if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") && RadioCapabilitySwitchUtil.isSubsidyLockForOmSupported()) {
                    DataSubSelector.this.log("DataSubSelector receive CONNECTIVITY_ACTION");
                    DataSubSelector.this.handleConnectivityAction();
                } else if (action.equals(DataSubSelector.ACTION_BOOT_COMPLETE) && RadioCapabilitySwitchUtil.isSubsidyLockForOmSupported()) {
                    DataSubSelector.this.log("DataSubSelector receive ACTION_BOOT_COMPLETE");
                    DataSubSelector.this.handleBootCompleteAction();
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mPrefNetworkModeObserver = new ContentObserver(new Handler()) { // from class: com.mediatek.internal.telephony.datasub.DataSubSelector.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                DataSubSelector.this.log("mPrefNetworkModeObserver, changed");
                DataSubSelector.this.handlePrefNetworkModeChanged();
            }
        };
        log("DataSubSelector is created");
        mOperatorSpec = SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, "OM");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        filter.addAction("com.mediatek.intent.action.LOCATED_PLMN_CHANGED");
        filter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        filter.addAction("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(ACTION_BOOT_COMPLETE);
        context.registerReceiver(broadcastReceiver, filter, 4);
        this.mContext = context;
        initOpDataSubSelector(context);
        if (this.mDataSubSelectorOPExt == null) {
            this.mDataSubSelectorOPExt = new DataSubSelectorOpExt(context);
        }
        initSimSwitchForDSS(context);
        if (this.mSimSwitchForDSSExt == null) {
            this.mSimSwitchForDSSExt = new SimSwitchForDSSExt(context);
        }
        this.mCapabilitySwitch = CapabilitySwitch.getInstance(context, this);
        this.mSimSwitchForDSSExt.init(this);
        this.mDataSubSelectorOPExt.init(this, this.mSimSwitchForDSSExt);
        registerPrefNetworkModeObserver();
    }

    private void initOpDataSubSelector(Context context) {
        try {
            OpTelephonyCustomizationFactoryBase opFactory = OpTelephonyCustomizationUtils.getOpFactory(context);
            this.mTelephonyCustomizationFactory = opFactory;
            this.mDataSubSelectorOPExt = opFactory.makeDataSubSelectorOPExt(context);
        } catch (Exception e) {
            log("mDataSubSelectorOPExt init fail");
            e.printStackTrace();
        }
    }

    private void initSimSwitchForDSS(Context context) {
        try {
            OpTelephonyCustomizationFactoryBase opFactory = OpTelephonyCustomizationUtils.getOpFactory(context);
            this.mTelephonyCustomizationFactory = opFactory;
            this.mSimSwitchForDSSExt = opFactory.makeSimSwitchForDSSOPExt(context);
        } catch (Exception e) {
            log("mSimSwitchForDSSExt init fail");
            e.printStackTrace();
        }
    }

    private void registerPrefNetworkModeObserver() {
        for (int i = 0; i < getPhoneNum(); i++) {
            int subId = SubscriptionManagerService.getInstance().getSubId(i);
            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("preferred_network_mode" + subId), DBG, this.mPrefNetworkModeObserver);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePrefNetworkModeChanged() {
        int defDataPhoneId;
        int defDataSubId = SubscriptionManagerService.getInstance().getDefaultDataSubId();
        if (defDataSubId != -1 && (defDataPhoneId = SubscriptionManager.getPhoneId(defDataSubId)) >= 0) {
            this.mCapabilitySwitch.setCapability(defDataPhoneId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDefaultDataChanged(Intent intent) {
        this.mDataSubSelectorOPExt.handleDefaultDataChanged(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubinfoRecordUpdated(Intent intent) {
        this.mDataSubSelectorOPExt.handleSubinfoRecordUpdated(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimStateChanged(Intent intent) {
        this.mDataSubSelectorOPExt.handleSimStateChanged(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAirPlaneModeOff(Intent intent) {
        this.mDataSubSelectorOPExt.handleAirPlaneModeOff(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectivityAction() {
        this.mDataSubSelectorOPExt.handleConnectivityAction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBootCompleteAction() {
        this.mDataSubSelectorOPExt.handleBootCompleteAction();
    }

    public boolean getAirPlaneModeOn() {
        return this.mAirplaneModeOn;
    }

    public boolean getIsWaitIccid() {
        return this.mIsWaitIccid;
    }

    public void setIsWaitIccid(boolean isWaitIccid) {
        this.mIsWaitIccid = isWaitIccid;
    }

    public boolean getIsNeedPreCheck() {
        return this.mIsNeedPreCheck;
    }

    public void setIsNeedPreCheck(boolean isNeedPreCheck) {
        this.mIsNeedPreCheck = isNeedPreCheck;
    }

    public void setDataEnabled(int phoneId, boolean enable) {
        log("setDataEnabled: phoneId=" + phoneId + ", enable=" + enable);
        TelephonyManager telephony = TelephonyManager.getDefault();
        if (telephony != null) {
            if (phoneId == -1) {
                telephony.setDataEnabled(enable);
                return;
            }
            if (!enable) {
                int phoneSubId = PhoneFactory.getPhone(phoneId).getSubId();
                log("Set Sub" + phoneSubId + " to disable");
                telephony.setDataEnabled(phoneSubId, enable);
                return;
            }
            for (int i = 0; i < getPhoneNum(); i++) {
                int phoneSubId2 = PhoneFactory.getPhone(i).getSubId();
                if (i != phoneId) {
                    log("Set Sub" + phoneSubId2 + " to disable");
                    telephony.setDataEnabled(phoneSubId2, false);
                } else {
                    log("Set Sub" + phoneSubId2 + " to enable");
                    telephony.setDataEnabled(phoneSubId2, DBG);
                }
            }
        }
    }

    public void setDefaultData(int phoneId) {
        int sub = SubscriptionManagerService.getInstance().getSubId(phoneId);
        int currSub = SubscriptionManagerService.getInstance().getDefaultDataSubId();
        log("setDefaultDataSubId: " + sub + ", current default sub:" + currSub);
        if (sub != currSub && sub >= -1) {
            SubscriptionManagerService.getInstance().setDefaultDataSubId(sub);
        } else {
            log("setDefaultDataSubId: default data unchanged");
        }
    }

    public int getPhoneNum() {
        return ((TelephonyManager) this.mContext.getSystemService("phone")).getActiveModemCount();
    }

    public void updateNetworkMode(Context context, final int subId) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        SubscriptionManager subscriptionManager = SubscriptionManager.from(applicationContext);
        final List<SubscriptionInfo> subInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (subInfoList == null) {
            log("subInfoList null");
            return;
        }
        this.mProtocolHandler = new Handler();
        Runnable r = new Runnable() { // from class: com.mediatek.internal.telephony.datasub.DataSubSelector.3
            @Override // java.lang.Runnable
            public void run() {
                if (subInfoList.size() == 1) {
                    DataSubSelector.this.updateNetworkModeUtil(subId, 9);
                    return;
                }
                if (subInfoList.size() > 1) {
                    for (int index = 0; index < subInfoList.size(); index++) {
                        int tempSubId = ((SubscriptionInfo) subInfoList.get(index)).getSubscriptionId();
                        if (tempSubId == subId) {
                            DataSubSelector.this.updateNetworkModeUtil(tempSubId, 9);
                        } else {
                            DataSubSelector.this.updateNetworkModeUtil(tempSubId, 0);
                        }
                    }
                }
            }
        };
        this.mProtocolHandler.postDelayed(r, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNetworkModeUtil(int subId, int mode) {
        log("Updating network mode for subId " + subId + "mode " + mode);
        TelephonyManager telephony = new TelephonyManager(this.mContext, subId);
        telephony.setAllowedNetworkTypesForReason(0, RadioAccessFamily.getRafFromNetworkType(mode));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String txt) {
        Rlog.d(LOG_TAG, txt);
    }

    private static void loge(String txt) {
        Rlog.e(LOG_TAG, txt);
    }
}
