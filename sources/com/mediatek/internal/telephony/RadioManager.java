package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.ratconfiguration.RatConfiguration;
import com.mediatek.internal.telephony.selfactivation.SaPersistDataHelper;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class RadioManager extends Handler {
    protected static final String ACTION_AIRPLANE_CHANGE_DONE = "com.mediatek.intent.action.AIRPLANE_CHANGE_DONE";
    public static final String ACTION_FORCE_SET_RADIO_POWER = "com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER";
    public static final String ACTION_MODEM_POWER_NO_CHANGE = "com.mediatek.intent.action.MODEM_POWER_CHANGE";
    private static final String ACTION_WIFI_OFFLOAD_SERVICE_ON = "mediatek.intent.action.WFC_POWER_ON_MODEM";
    private static final String ACTION_WIFI_ONLY_MODE_CHANGED = "android.intent.action.ACTION_WIFI_ONLY_MODE";
    protected static final boolean AIRPLANE_MODE_OFF = false;
    protected static final boolean AIRPLANE_MODE_ON = true;
    public static final int ERROR_AIRPLANE_MODE = 2;
    public static final int ERROR_ICCID_NOT_READY = 5;
    public static final int ERROR_MODEM_OFF = 4;
    public static final int ERROR_NO_PHONE_INSTANCE = 1;
    public static final int ERROR_PCO = 6;
    public static final int ERROR_PCO_ALREADY_OFF = 7;
    public static final int ERROR_SIM_SWITCH_EXECUTING = 8;
    public static final int ERROR_TURN_OFF_RADIO_DURING_ECC = 9;
    public static final int ERROR_WIFI_ONLY = 3;
    private static final int EVENT_DSBP_STATE_CHANGED_SLOT_1 = 10;
    private static final int EVENT_DSBP_STATE_CHANGED_SLOT_2 = 11;
    private static final int EVENT_DSBP_STATE_CHANGED_SLOT_3 = 12;
    private static final int EVENT_DSBP_STATE_CHANGED_SLOT_4 = 13;
    private static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 14;
    private static final int EVENT_RADIO_AVAILABLE_SLOT_1 = 1;
    private static final int EVENT_RADIO_AVAILABLE_SLOT_2 = 2;
    private static final int EVENT_RADIO_AVAILABLE_SLOT_3 = 3;
    private static final int EVENT_RADIO_AVAILABLE_SLOT_4 = 4;
    private static final int EVENT_REPORT_AIRPLANE_DONE = 8;
    private static final int EVENT_REPORT_SIM_MODE_DONE = 9;
    private static final int EVENT_SET_MODEM_POWER_OFF_DONE = 6;
    private static final int EVENT_SET_SILENT_REBOOT_DONE = 7;
    private static final int EVENT_VIRTUAL_SIM_ON = 5;
    protected static final String EXTRA_AIRPLANE_MODE = "airplaneMode";
    public static final String EXTRA_MODEM_POWER = "modemPower";
    private static final String EXTRA_WIFI_OFFLOAD_SERVICE_ON = "mediatek:POWER_ON_MODEM";
    private static final boolean ICC_READ_NOT_READY = false;
    private static final boolean ICC_READ_READY = true;
    protected static final int INITIAL_RETRY_INTERVAL_MSEC = 200;
    protected static final int INVALID_PHONE_ID = -1;
    private static final String IS_NOT_SILENT_REBOOT = "0";
    protected static final String IS_SILENT_REBOOT = "1";
    static final String LOG_TAG = "RadioManager";
    private static final int MAX_PHONE_COUNT = 4;
    protected static final boolean MODEM_POWER_OFF = false;
    protected static final boolean MODEM_POWER_ON = true;
    protected static final int MODE_PHONE1_ONLY = 1;
    private static final int MODE_PHONE2_ONLY = 2;
    private static final int MODE_PHONE3_ONLY = 4;
    private static final int MODE_PHONE4_ONLY = 8;
    protected static final int NO_SIM_INSERTED = 0;
    private static final String PREF_CATEGORY_RADIO_STATUS = "RADIO_STATUS";
    private static final String PROPERTY_AIRPLANE_MODE = "persist.vendor.radio.airplane.mode.on";
    protected static final String PROPERTY_SILENT_REBOOT_MD1 = "vendor.gsm.ril.eboot";
    private static final String PROPERTY_SIM_MODE = "persist.vendor.radio.sim.mode";
    protected static final boolean RADIO_POWER_OFF = false;
    protected static final boolean RADIO_POWER_ON = true;
    public static final int REASON_NONE = -1;
    public static final int REASON_PCO_OFF = 1;
    public static final int REASON_PCO_ON = 0;
    private static final String REGISTRANTS_WITH_NO_NAME = "NO_NAME";
    protected static final int SIM_INSERTED = 1;
    private static final int SIM_NOT_INITIALIZED = -1;
    protected static final String STRING_NO_SIM_INSERTED = "N/A";
    public static final int SUCCESS = 0;
    protected static final int TO_SET_MODEM_POWER = 2;
    protected static final int TO_SET_RADIO_POWER = 1;
    private static final int WIFI_ONLY_INIT = -1;
    private static final boolean WIFI_ONLY_MODE_OFF = false;
    private static final boolean WIFI_ONLY_MODE_ON = true;
    protected static SharedPreferences sIccidPreference;
    private static RadioManager sRadioManager;
    private boolean mAirDnMsgSent;
    protected boolean mAirplaneMode;
    protected int mBitmapForPhoneCount;
    private CommandsInterface[] mCi;
    private Context mContext;
    private Runnable[] mForceRefreshSimStateRunnable;
    private Runnable[] mForceSetRadioPowerRunnable;
    private ImsSwitchController mImsSwitchController;
    private int[] mInitializeWaitCounter;
    private boolean[] mIsDsbpChanging;
    private boolean mIsWifiOn;
    private boolean mIsWifiOnlyDevice;
    private ModemPowerMessage[] mModemPowerMessages;
    private boolean mNeedIgnoreMessageForChangeDone;
    private boolean mNeedIgnoreMessageForWait;
    private Runnable mNotifyMSimModeChangeRunnable;
    private Runnable[] mNotifySimModeChangeRunnable;
    protected int mPhoneCount;
    private PowerSM mPowerSM;
    private Runnable[] mRadioPowerRunnable;
    public int[] mReason;
    private int mSimModeSetting;
    private boolean mWifiOnlyMode;
    protected static ConcurrentHashMap<IRadioPower, String> mNotifyRadioPowerChange = new ConcurrentHashMap<>();
    protected static String[] PROPERTY_RADIO_OFF = {"vendor.ril.ipo.radiooff", "vendor.ril.ipo.radiooff.2"};
    private static final int[] EVENT_RADIO_AVAILABLE = {1, 2, 3, 4};
    private static final int[] EVENT_DSBP_STATE_CHANGED = {10, 11, 12, 13};
    private static final boolean mFlightModePowerOffModem = SystemProperties.get("ro.vendor.mtk_flight_mode_power_off_md").equals("1");
    private static final boolean isOP01 = DataSubConstants.OPERATOR_OP01.equalsIgnoreCase(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, ""));
    private static final boolean isOP09 = DataSubConstants.OPERATOR_OP09.equalsIgnoreCase(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, ""));
    private boolean mIsPendingRadioByDsbpChanging = false;
    private boolean mModemPower = true;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.RadioManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            RadioManager.log("BroadcastReceiver: " + intent.getAction());
            if (intent.getAction().equals("android.telephony.action.SIM_CARD_STATE_CHANGED")) {
                RadioManager.this.onReceiveSimStateChangedIntent(intent);
                return;
            }
            if (intent.getAction().equals(RadioManager.ACTION_FORCE_SET_RADIO_POWER)) {
                RadioManager.this.onReceiveForceSetRadioPowerIntent(intent);
                return;
            }
            if (intent.getAction().equals(RadioManager.ACTION_WIFI_ONLY_MODE_CHANGED)) {
                RadioManager.this.onReceiveWifiOnlyModeStateChangedIntent(intent);
                return;
            }
            if (intent.getAction().equals(RadioManager.ACTION_WIFI_OFFLOAD_SERVICE_ON)) {
                RadioManager.this.onReceiveWifiStateChangedIntent(intent);
                return;
            }
            if (intent.getAction().equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE") || intent.getAction().equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED")) {
                if (RadioManager.isFlightModePowerOffModemConfigEnabled()) {
                    RadioManager.this.mPowerSM.updateModemPowerState(RadioManager.this.mAirplaneMode, RadioManager.this.mBitmapForPhoneCount, 128);
                }
                if (RadioManager.this.mIsDsbpChanging[RadioManager.this.findMainCapabilityPhoneId()]) {
                    RadioManager.this.mIsPendingRadioByDsbpChanging = true;
                } else {
                    RadioManager.this.setRadioPowerAfterCapabilitySwitch();
                }
            }
        }
    };

    public static RadioManager init(Context context, int phoneCount, CommandsInterface[] ci) {
        RadioManager radioManager;
        synchronized (RadioManager.class) {
            if (sRadioManager == null) {
                sRadioManager = new RadioManager(context, phoneCount, ci);
            }
            radioManager = sRadioManager;
        }
        return radioManager;
    }

    public static RadioManager getInstance() {
        RadioManager radioManager;
        synchronized (RadioManager.class) {
            radioManager = sRadioManager;
        }
        return radioManager;
    }

    protected RadioManager(Context context, int phoneCount, CommandsInterface[] ci) {
        boolean z;
        boolean z2;
        boolean z3;
        int i = 0;
        this.mAirplaneMode = false;
        this.mWifiOnlyMode = false;
        this.mIsWifiOn = false;
        this.mImsSwitchController = null;
        int airplaneMode = Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0);
        int wifionlyMode = ImsManager.getWfcMode(context);
        this.mAirDnMsgSent = false;
        ImsManager imsMgr = ImsManager.getInstance(context, 0);
        if (imsMgr.isServiceReady() && ImsManager.isWfcEnabledByPlatform(context)) {
            log("initial actual wifi state when wifi calling is on");
            WifiManager wiFiManager = (WifiManager) context.getSystemService("wifi");
            if (wiFiManager != null) {
                if (!wiFiManager.isWifiEnabled()) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                this.mIsWifiOn = z3;
            }
        }
        log("Initialize RadioManager under airplane mode:" + airplaneMode + " wifi only mode:" + wifionlyMode + " wifi mode: " + this.mIsWifiOn + "phoneCount:" + phoneCount);
        this.mInitializeWaitCounter = new int[4];
        for (int i2 = 0; i2 < 4; i2++) {
            this.mInitializeWaitCounter[i2] = 0;
        }
        this.mRadioPowerRunnable = new RadioPowerRunnable[4];
        for (int i3 = 0; i3 < 4; i3++) {
            this.mRadioPowerRunnable[i3] = new RadioPowerRunnable(true, i3);
        }
        this.mNotifySimModeChangeRunnable = new SimModeChangeRunnable[4];
        for (int i4 = 0; i4 < 4; i4++) {
            this.mNotifySimModeChangeRunnable[i4] = new SimModeChangeRunnable(true, i4);
        }
        this.mNotifyMSimModeChangeRunnable = new MSimModeChangeRunnable(3);
        this.mForceSetRadioPowerRunnable = new ForceSetRadioPowerRunnable[4];
        for (int i5 = 0; i5 < 4; i5++) {
            this.mForceSetRadioPowerRunnable[i5] = new ForceSetRadioPowerRunnable(true, i5);
        }
        this.mForceRefreshSimStateRunnable = new ForceRefreshSimStateRunnable[4];
        this.mContext = context;
        if (airplaneMode == 0) {
            z = false;
        } else {
            z = true;
        }
        this.mAirplaneMode = z;
        if (wifionlyMode != 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mWifiOnlyMode = z2;
        this.mCi = ci;
        this.mPhoneCount = phoneCount;
        this.mBitmapForPhoneCount = convertPhoneCountIntoBitmap(phoneCount);
        sIccidPreference = this.mContext.getSharedPreferences("RADIO_STATUS", 0);
        this.mSimModeSetting = Settings.Global.getInt(context.getContentResolver(), "msim_mode_setting", this.mBitmapForPhoneCount);
        this.mImsSwitchController = new ImsSwitchController(this.mContext, this.mPhoneCount, this.mCi);
        int mainPhoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
        this.mCi[mainPhoneId].setVendorSetting(8, Integer.toString(airplaneMode), obtainMessage(8));
        log("Not BSP Package, register intent!!!");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        filter.addAction(ACTION_FORCE_SET_RADIO_POWER);
        filter.addAction(ACTION_WIFI_ONLY_MODE_CHANGED);
        filter.addAction(ACTION_WIFI_OFFLOAD_SERVICE_ON);
        filter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
        filter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
        this.mContext.registerReceiver(this.mIntentReceiver, filter, 2);
        registerListener();
        PhoneConfigurationManager.registerForMultiSimConfigChange(this, 14, (Object) null);
        if ("".equals(RatConfiguration.getActiveRatConfig())) {
            this.mIsWifiOnlyDevice = true;
        }
        PowerSM powerSM = new PowerSM("PowerSM");
        this.mPowerSM = powerSM;
        powerSM.start();
        this.mReason = new int[4];
        int i6 = 0;
        for (int i7 = 4; i6 < i7; i7 = 4) {
            if (i6 < phoneCount) {
                MtkSubscriptionManager.getSubIdUsingPhoneId(i6);
                int selfActivationState = SaPersistDataHelper.getIntData(this.mContext, i6, SaPersistDataHelper.DATA_KEY_SA_STATE, i);
                String pcoEnable = SystemProperties.get("persist.vendor.pco5.radio.ctrl", "0");
                if (2 == selfActivationState && !pcoEnable.equals("0")) {
                    this.mReason[i6] = 1;
                } else {
                    this.mReason[i6] = -1;
                }
            } else {
                this.mReason[i6] = -1;
            }
            i6++;
            i = 0;
        }
        this.mIsDsbpChanging = new boolean[4];
        for (int i8 = 0; i8 < 4; i8++) {
            this.mIsDsbpChanging[i8] = false;
        }
    }

    private int convertPhoneCountIntoBitmap(int phoneCount) {
        int ret = 0;
        for (int i = 0; i < phoneCount; i++) {
            ret += 1 << i;
        }
        log("Convert phoneCount " + phoneCount + " into bitmap " + ret);
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRadioPowerAfterCapabilitySwitch() {
        log("Update radio power after capability switch or dsbp changing");
        SubscriptionManager.from(this.mContext);
        int targetSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        SubscriptionManager.from(this.mContext);
        int targetPhoneId = SubscriptionManager.getPhoneId(targetSubId);
        if (!SubscriptionManager.isValidPhoneId(targetPhoneId)) {
            targetPhoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
        }
        setRadioPower(!this.mAirplaneMode, targetPhoneId);
        for (int i = 0; i < this.mPhoneCount; i++) {
            if (targetPhoneId != i) {
                setRadioPower(!this.mAirplaneMode, i);
            }
        }
    }

    protected void onReceiveWifiStateChangedIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_WIFI_OFFLOAD_SERVICE_ON)) {
            int extraWifiState = intent.getBooleanExtra(EXTRA_WIFI_OFFLOAD_SERVICE_ON, false) ? 3 : 1;
            log("Receiving ACTION_WIFI_OFFLOAD_SERVICE_ON, airplaneMode: " + this.mAirplaneMode + " isFlightModePowerOffModemConfigEnabled:" + isFlightModePowerOffModemConfigEnabled() + ", mIsWifiOn: " + this.mIsWifiOn);
            switch (extraWifiState) {
                case 1:
                    log("WIFI_STATE_CHANGED disabled");
                    this.mIsWifiOn = false;
                    if (this.mAirplaneMode && isFlightModePowerOffModemConfigEnabled()) {
                        log("WIFI_STATE_CHANGED disabled, set modem off");
                        setSilentRebootPropertyForAllModem("1");
                        this.mPowerSM.updateModemPowerState(false, this.mBitmapForPhoneCount, 4);
                        break;
                    }
                    break;
                case 2:
                default:
                    log("default: WIFI_STATE_CHANGED extra" + extraWifiState);
                    break;
                case 3:
                    log("WIFI_STATE_CHANGED enabled");
                    this.mIsWifiOn = true;
                    if (this.mAirplaneMode && isFlightModePowerOffModemConfigEnabled()) {
                        if (isModemPowerOff(0)) {
                        }
                        log("WIFI_STATE_CHANGED enabled, set modem on");
                        setSilentRebootPropertyForAllModem("1");
                        this.mPowerSM.updateModemPowerState(true, this.mBitmapForPhoneCount, 4);
                        break;
                    }
                    break;
            }
            return;
        }
        log("Wrong intent");
    }

    protected void onReceiveSimStateChangedIntent(Intent intent) {
        int simStatus = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
        int phoneId = intent.getIntExtra("phone", -1);
        if (!isValidPhoneId(phoneId)) {
            log("INTENT:Invalid phone id:" + phoneId + ", do nothing!");
            return;
        }
        log("INTENT:SIM_STATE_CHANGED: " + intent.getAction() + ", sim status: " + simStatus + ", phoneId: " + phoneId);
        if (11 == simStatus) {
            log("Phone[" + phoneId + "]: " + simStatusToString(1));
            String iccid = readIccIdUsingPhoneId(phoneId);
            if ("N/A".equals(iccid)) {
                log("Phone " + phoneId + ":SIM ready but ICCID not ready, do nothing");
                return;
            } else {
                if (!this.mAirplaneMode) {
                    log("Set Radio Power due to SIM_STATE_CHANGED, power: true, phoneId: " + phoneId);
                    setRadioPower(true, phoneId);
                    return;
                }
                return;
            }
        }
        if (1 == simStatus) {
            log("Phone[" + phoneId + "]: " + simStatusToString(0));
        }
    }

    public void onReceiveWifiOnlyModeStateChangedIntent(Intent intent) {
        boolean enabled = intent.getBooleanExtra("state", false);
        log("Received ACTION_WIFI_ONLY_MODE_CHANGED, enabled = " + enabled);
        if (enabled == this.mWifiOnlyMode) {
            log("enabled = " + enabled + ", mWifiOnlyMode = " + this.mWifiOnlyMode + " is not expected (the same)");
            return;
        }
        this.mWifiOnlyMode = enabled;
        if (!this.mAirplaneMode) {
            boolean radioPower = !enabled;
            for (int i = 0; i < this.mPhoneCount; i++) {
                setRadioPower(radioPower, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReceiveForceSetRadioPowerIntent(Intent intent) {
        int mode = intent.getIntExtra("mode", -1);
        log("force set radio power, mode: " + mode);
        if (mode == -1) {
            log("Invalid mode, MSIM_MODE intent has no extra value");
            return;
        }
        for (int phoneId = 0; phoneId < this.mPhoneCount; phoneId++) {
            boolean singlePhonePower = ((1 << phoneId) & mode) != 0;
            if (true == singlePhonePower) {
                forceSetRadioPower(true, phoneId);
            }
        }
    }

    protected boolean isValidPhoneId(int phoneId) {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
            return false;
        }
        return true;
    }

    protected String simStatusToString(int simStatus) {
        switch (simStatus) {
            case -1:
                return "SIM HAVE NOT INITIALIZED";
            case 0:
                return "NO SIM DETECTED";
            case 1:
                return "SIM DETECTED";
            default:
                return null;
        }
    }

    public void notifyAirplaneModeChange(boolean z) {
        int i;
        if (z == this.mAirplaneMode) {
            log("enabled = " + z + ", mAirplaneMode = " + this.mAirplaneMode + " is not expected (the same)");
            return;
        }
        int iFindMainCapabilityPhoneId = findMainCapabilityPhoneId();
        this.mAirplaneMode = z;
        log("Airplane mode changed: " + z + " mDesiredPower: " + this.mPowerSM.mDesiredModemPower + " mCurrentModemPower: " + this.mPowerSM.mCurrentModemPower);
        this.mCi[iFindMainCapabilityPhoneId].setVendorSetting(8, Integer.toString(z ? 1 : 0), obtainMessage(8));
        if (z) {
            this.mIsWifiOn = false;
        }
        if (isFlightModePowerOffModemConfigEnabled() && !isUnderCryptKeeper()) {
            if (this.mPowerSM.mDesiredModemPower && !this.mAirplaneMode) {
                log("Airplane mode changed: turn on all radio due to mode conflict");
                i = 1;
            } else if (!this.mAirplaneMode && this.mIsWifiOn) {
                log("airplane mode changed: airplane mode on and wifi-calling on. Then,leave airplane mode: turn on/off all radio");
                i = 1;
            } else {
                log("Airplane mode changed: turn on/off all modem");
                i = 2;
            }
        } else {
            log("Airplane mode changed: turn on/off all radio");
            i = 1;
        }
        if (i == 1) {
            boolean z2 = !z;
            SubscriptionManager.from(this.mContext);
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            SubscriptionManager.from(this.mContext);
            int phoneId = SubscriptionManager.getPhoneId(defaultDataSubscriptionId);
            if (!SubscriptionManager.isValidPhoneId(phoneId)) {
                phoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
            }
            setRadioPower(z2, phoneId);
            for (int i2 = 0; i2 < this.mPhoneCount; i2++) {
                if (phoneId != i2) {
                    setRadioPower(z2, i2);
                }
            }
            Intent intent = new Intent(ACTION_AIRPLANE_CHANGE_DONE);
            intent.putExtra(EXTRA_AIRPLANE_MODE, !z);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
            return;
        }
        if (i == 2) {
            boolean z3 = !z;
            setSilentRebootPropertyForAllModem("1");
            this.mPowerSM.updateModemPowerState(z3, this.mBitmapForPhoneCount, 2);
        }
    }

    public static boolean isUnderCryptKeeper() {
        if (SystemProperties.get("ro.crypto.type").equals("block") && SystemProperties.get("ro.crypto.state").equals("encrypted") && SystemProperties.get("vold.decrypt").equals("trigger_restart_min_framework")) {
            log("[Special Case] Under CryptKeeper, Not to turn on/off modem");
            return true;
        }
        log("[Special Case] Not Under CryptKeeper");
        return false;
    }

    public void setSilentRebootPropertyForAllModem(String isSilentReboot) {
        TelephonyManager.getDefault().getMultiSimConfiguration();
        int phoneId = findMainCapabilityPhoneId();
        int on = 0;
        if (isSilentReboot.equals("1")) {
            on = 1;
        }
        log("enable silent reboot");
        this.mCi[phoneId].setVendorSetting(10, Integer.toString(on), obtainMessage(7));
    }

    public void notifyRadioAvailable(int phoneId) {
        log("Phone " + phoneId + " notifies radio available airplane mode: " + this.mAirplaneMode + " cryptkeeper: " + isUnderCryptKeeper() + " mIsWifiOn:" + this.mIsWifiOn);
        if (isRadioAvaliable()) {
            this.mPowerSM.sendEvent(3);
        }
        if (RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() == phoneId) {
            cleanModemPowerMessage();
            if (this.mAirplaneMode && isFlightModePowerOffModemConfigEnabled() && !isUnderCryptKeeper() && !this.mIsWifiOn) {
                log("Power off modem because boot up under airplane mode");
                this.mPowerSM.updateModemPowerState(false, 1 << phoneId, 64);
            }
        }
        if (!this.mAirDnMsgSent && this.mAirplaneMode) {
            if (!isFlightModePowerOffModemConfigEnabled() || isUnderCryptKeeper()) {
                Intent intent = new Intent(ACTION_AIRPLANE_CHANGE_DONE);
                intent.putExtra(EXTRA_AIRPLANE_MODE, true);
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
                this.mAirDnMsgSent = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setModemPower(boolean power, int phoneBitMap) {
        log("Set Modem Power according to bitmap, Power:" + power + ", PhoneBitMap:" + phoneBitMap);
        if (PhoneFactory.getDefaultPhone().getServiceStateTracker().isDeviceShuttingDown()) {
            Rlog.d(LOG_TAG, "[RadioManager] skip the request because device is shutdown");
            return;
        }
        TelephonyManager.MultiSimVariants config = TelephonyManager.getDefault().getMultiSimConfiguration();
        Message[] responses = monitorModemPowerChangeDone(power, phoneBitMap, findMainCapabilityPhoneId());
        switch (AnonymousClass2.$SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[config.ordinal()]) {
            case 1:
            case 2:
            case 3:
                int phoneId = findMainCapabilityPhoneId();
                log("Set Modem Power, Power:" + power + ", phoneId:" + phoneId);
                this.mCi[phoneId].setModemPower(power, responses[phoneId]);
                break;
            default:
                int phoneId2 = PhoneFactory.getDefaultPhone().getPhoneId();
                log("Set Modem Power under SS mode:" + power + ", phoneId:" + phoneId2);
                this.mCi[phoneId2].setModemPower(power, responses[phoneId2]);
                break;
        }
        if (power) {
            if ((isOP01 || isOP09) && SystemProperties.get("vendor.ril.atci.flightmode").equals("1")) {
                log("Power on Modem, Set vendor.ril.atci.flightmode to 0");
                SystemProperties.set("vendor.ril.atci.flightmode", "0");
            }
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.RadioManager$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants;

        static {
            int[] iArr = new int[TelephonyManager.MultiSimVariants.values().length];
            $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants = iArr;
            try {
                iArr[TelephonyManager.MultiSimVariants.DSDS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[TelephonyManager.MultiSimVariants.DSDA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[TelephonyManager.MultiSimVariants.TSTS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    protected int findMainCapabilityPhoneId() {
        int switchStatus = Integer.valueOf(SystemProperties.get("persist.vendor.radio.simswitch", "1")).intValue();
        int result = switchStatus - 1;
        if (result < 0 || result >= this.mPhoneCount) {
            return 0;
        }
        return result;
    }

    protected class RadioPowerRunnable implements Runnable {
        int retryPhoneId;
        boolean retryPower;

        public RadioPowerRunnable(boolean power, int phoneId) {
            this.retryPower = power;
            this.retryPhoneId = phoneId;
        }

        @Override // java.lang.Runnable
        public void run() {
            RadioManager.this.setRadioPower(this.retryPower, this.retryPhoneId);
        }
    }

    public int setRadioPower(boolean power, int phoneId) {
        String printableIccid;
        log("setRadioPower, power=" + power + "  phoneId=" + phoneId);
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            return 1;
        }
        if ((isFlightModePowerOffModemEnabled() || power) && this.mAirplaneMode) {
            log("Set Radio Power on under airplane mode, ignore");
            return 2;
        }
        if ("".equals(RatConfiguration.getActiveRatConfig())) {
            this.mIsWifiOnlyDevice = true;
            log("wifi-only device, so return");
            return 3;
        }
        if (isModemPowerOff(phoneId)) {
            log("modem for phone " + phoneId + " off, do not set radio again");
            return 4;
        }
        String pcoEnable = SystemProperties.get("persist.vendor.pco5.radio.ctrl", "0");
        if (1 != this.mReason[phoneId] || !power || pcoEnable.equals("0")) {
            if (1 == this.mReason[phoneId] && phone.mCi.getRadioState() == 0) {
                log("PCO5 and already off");
                return 7;
            }
            boolean isInEcc = MtkTelephonyManagerEx.getDefault().isInEmergencyCall();
            if (!power && isInEcc) {
                log("Not allow to operate radio power during emergency call");
                return 2;
            }
            removeCallbacks(this.mRadioPowerRunnable[phoneId]);
            if (!isRadioAvaliable() || !isIccIdReady(phoneId)) {
                if (hasCallbacks(this.mForceSetRadioPowerRunnable[phoneId])) {
                    log("ForceSetRadioPowerRunnable exists queue, do not execute RadioPowerRunnablefor phone " + phoneId);
                    return 5;
                }
                if (phoneId < this.mPhoneCount) {
                    log("RILD initialize not completed, wait for 200ms");
                    this.mRadioPowerRunnable[phoneId] = new RadioPowerRunnable(power, phoneId);
                    postDelayed(this.mRadioPowerRunnable[phoneId], 200L);
                    return 5;
                }
                log("mPhoneCount = " + this.mPhoneCount + ", ignore this request");
                return 5;
            }
            boolean radioPower = power;
            if (MtkTelephonyManagerEx.getDefault().simSwitchMode() == 1) {
                log("Radio on/off feature is enabled, adjust radio power according to ICCID");
                String iccId = readIccIdUsingPhoneId(phoneId);
                if (!isRequiredRadioOff(iccId)) {
                    if (!this.mAirplaneMode && !phone.isShuttingDown() && 1 != this.mReason[phoneId]) {
                        radioPower = true;
                    }
                } else {
                    if (isInEcc) {
                        log("Adjust radio to off because once manually turned off during ECC, return");
                        return 9;
                    }
                    if ("N/A".equals(iccId)) {
                        printableIccid = "N/A";
                    } else {
                        printableIccid = binaryToHex(getHashCode(SubscriptionInfo.getPrintableId(iccId)));
                    }
                    log("Adjust radio to off because once manually turned off, hash(iccid): " + printableIccid + " , phone: " + phoneId);
                    radioPower = false;
                }
            }
            if (this.mWifiOnlyMode && !isInEcc) {
                log("setradiopower but wifi only, turn off");
                radioPower = false;
            }
            log("Trigger set Radio Power, power: " + radioPower + ", phoneId: " + phoneId);
            PhoneFactory.getPhone(phoneId).setRadioPower(radioPower, false, false, false);
            refreshSimSetting(radioPower, phoneId);
            return 0;
        }
        log("Not allow to turn on radio under PCO=5");
        return 6;
    }

    public int setRadioPower(boolean power, int phoneId, int reason) {
        this.mReason[phoneId] = reason;
        return setRadioPower(power, phoneId);
    }

    protected boolean isIccIdReady(int phoneId) {
        String iccId = readIccIdUsingPhoneId(phoneId);
        if (iccId == null || "".equals(iccId)) {
            return false;
        }
        return true;
    }

    protected String readIccIdUsingPhoneId(int phoneId) {
        String printableIccid;
        String ret = "N/A";
        MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
        if (ctrl != null) {
            ret = ctrl.getIccid(phoneId);
        }
        if ("N/A".equals(ret)) {
            printableIccid = "N/A";
        } else {
            String printableIccid2 = SubscriptionInfo.getPrintableId(ret);
            printableIccid = binaryToHex(getHashCode(printableIccid2));
        }
        log("Hash(ICCID) for phone " + phoneId + " is " + printableIccid);
        return ret;
    }

    protected void refreshSimSetting(boolean radioPower, int phoneId) {
        if (PhoneFactory.getDefaultPhone().getServiceStateTracker().isDeviceShuttingDown()) {
            Rlog.i(LOG_TAG, "[RadioManager] skip the refreshSimSetting because device is shutdown");
            return;
        }
        int oldMode = this.mSimModeSetting;
        if (radioPower) {
            this.mSimModeSetting = (1 << phoneId) | this.mSimModeSetting;
        } else {
            this.mSimModeSetting = (~(1 << phoneId)) & this.mSimModeSetting;
        }
        log("Refresh MSIM mode setting to " + this.mSimModeSetting + " from " + oldMode);
        this.mCi[findMainCapabilityPhoneId()].setVendorSetting(9, Integer.toString(this.mSimModeSetting), obtainMessage(9));
        Settings.Global.putInt(this.mContext.getContentResolver(), "msim_mode_setting", this.mSimModeSetting);
    }

    protected class ForceSetRadioPowerRunnable implements Runnable {
        int mRetryPhoneId;
        boolean mRetryPower;

        public ForceSetRadioPowerRunnable(boolean power, int phoneId) {
            this.mRetryPower = power;
            this.mRetryPhoneId = phoneId;
        }

        @Override // java.lang.Runnable
        public void run() {
            RadioManager.this.forceSetRadioPower(this.mRetryPower, this.mRetryPhoneId);
        }
    }

    protected class ForceRefreshSimStateRunnable implements Runnable {
        int mRetryPhoneId;
        boolean mRetryPower;

        public ForceRefreshSimStateRunnable(boolean power, int phoneId) {
            this.mRetryPower = power;
            this.mRetryPhoneId = phoneId;
        }

        @Override // java.lang.Runnable
        public void run() {
            RadioManager.this.forceRefreshSimState(this.mRetryPower, this.mRetryPhoneId);
        }
    }

    public void forceSetRadioPower(boolean power, int phoneId) {
        log("force set radio power for phone" + phoneId + " ,power: " + power);
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            return;
        }
        if (isFlightModePowerOffModemConfigEnabled() && this.mAirplaneMode) {
            log("Force Set Radio Power under airplane mode, ignore");
            return;
        }
        if (isModemPowerOff(phoneId) && this.mAirplaneMode) {
            log("Modem Power Off for phone " + phoneId + ", Power on modem first");
            this.mPowerSM.updateModemPowerState(true, 1 << phoneId, 16);
        }
        removeCallbacks(this.mForceSetRadioPowerRunnable[phoneId]);
        if (!isIccIdReady(phoneId) || (isFlightModePowerOffModemConfigEnabled() && !this.mAirplaneMode && power && isModemOff(phoneId))) {
            log("force set radio power, read iccid not ready, wait for200ms");
            this.mForceSetRadioPowerRunnable[phoneId] = new ForceSetRadioPowerRunnable(power, phoneId);
            postDelayed(this.mForceSetRadioPowerRunnable[phoneId], 200L);
        } else {
            refreshIccIdPreference(power, readIccIdUsingPhoneId(phoneId));
            phone.setRadioPower(power, false, false, false);
            refreshSimSetting(power, phoneId);
        }
    }

    public void forceRefreshSimState(boolean power, int phoneId) {
        log("force refresh sim state" + phoneId + " ,power: " + power);
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            return;
        }
        if (isFlightModePowerOffModemConfigEnabled() && this.mAirplaneMode) {
            log("force refresh sim state under airplane mode, ignore");
            return;
        }
        if (isModemPowerOff(phoneId) && this.mAirplaneMode) {
            log("Modem Power Off for phone " + phoneId + ", Power on modem first");
            this.mPowerSM.updateModemPowerState(true, 1 << phoneId, 16);
        }
        removeCallbacks(this.mForceRefreshSimStateRunnable[phoneId]);
        if (!isIccIdReady(phoneId) || (isFlightModePowerOffModemConfigEnabled() && !this.mAirplaneMode && power && isModemOff(phoneId))) {
            log("force refresh sim state, read iccid not ready, wait for200ms");
            this.mForceRefreshSimStateRunnable[phoneId] = new ForceRefreshSimStateRunnable(power, phoneId);
            postDelayed(this.mForceRefreshSimStateRunnable[phoneId], 200L);
        } else {
            refreshIccIdPreference(power, readIccIdUsingPhoneId(phoneId));
            refreshSimSetting(power, phoneId);
        }
    }

    private class SimModeChangeRunnable implements Runnable {
        int mPhoneId;
        boolean mPower;

        public SimModeChangeRunnable(boolean power, int phoneId) {
            this.mPower = power;
            this.mPhoneId = phoneId;
        }

        @Override // java.lang.Runnable
        public void run() {
            RadioManager.this.notifySimModeChange(this.mPower, this.mPhoneId);
        }
    }

    public void notifySimModeChange(boolean power, int phoneId) {
        log("SIM mode changed, power: " + power + ", phoneId" + phoneId);
        if (this.mAirplaneMode) {
            log("Airplane mode on or MSIM Mode option is closed, do nothing!");
            return;
        }
        removeCallbacks(this.mNotifySimModeChangeRunnable[phoneId]);
        if (!isIccIdReady(phoneId)) {
            log("sim mode read iccid not ready, wait for 200ms");
            this.mNotifySimModeChangeRunnable[phoneId] = new SimModeChangeRunnable(power, phoneId);
            postDelayed(this.mNotifySimModeChangeRunnable[phoneId], 200L);
        } else {
            if ("N/A".equals(readIccIdUsingPhoneId(phoneId))) {
                power = false;
                log("phoneId " + phoneId + " sim not insert, set  power  to false");
            }
            refreshIccIdPreference(power, readIccIdUsingPhoneId(phoneId));
            log("Set Radio Power due to SIM mode change, power: " + power + ", phoneId: " + phoneId);
            setRadioPower(power, phoneId);
        }
    }

    protected class MSimModeChangeRunnable implements Runnable {
        int mRetryMode;

        public MSimModeChangeRunnable(int mode) {
            this.mRetryMode = mode;
        }

        @Override // java.lang.Runnable
        public void run() {
            RadioManager.this.notifyMSimModeChange(this.mRetryMode);
        }
    }

    public void notifyMSimModeChange(int mode) {
        log("MSIM mode changed, mode: " + mode);
        if (mode == -1) {
            log("Invalid mode, MSIM_MODE intent has no extra value");
            return;
        }
        if (this.mAirplaneMode) {
            log("Airplane mode on or MSIM Mode option is closed, do nothing!");
            return;
        }
        boolean iccIdReady = true;
        int phoneId = 0;
        while (true) {
            if (phoneId >= this.mPhoneCount) {
                break;
            }
            if (isIccIdReady(phoneId)) {
                phoneId++;
            } else {
                iccIdReady = false;
                break;
            }
        }
        removeCallbacks(this.mNotifyMSimModeChangeRunnable);
        if (!iccIdReady) {
            MSimModeChangeRunnable mSimModeChangeRunnable = new MSimModeChangeRunnable(mode);
            this.mNotifyMSimModeChangeRunnable = mSimModeChangeRunnable;
            postDelayed(mSimModeChangeRunnable, 200L);
            return;
        }
        for (int phoneId2 = 0; phoneId2 < this.mPhoneCount; phoneId2++) {
            boolean singlePhonePower = ((1 << phoneId2) & mode) != 0;
            if ("N/A".equals(readIccIdUsingPhoneId(phoneId2))) {
                singlePhonePower = false;
                log("phoneId " + phoneId2 + " sim not insert, set  power  to false");
            }
            refreshIccIdPreference(singlePhonePower, readIccIdUsingPhoneId(phoneId2));
            log("Set Radio Power due to MSIM mode change, power: " + singlePhonePower + ", phoneId: " + phoneId2);
            setRadioPower(singlePhonePower, phoneId2);
        }
    }

    protected void refreshIccIdPreference(boolean power, String iccid) {
        if (MtkTelephonyManagerEx.getDefault().simSwitchMode() != 1) {
            log("SIM on/off feature is enabled, don't refresh iccid preference");
            return;
        }
        log("refresh iccid preference");
        SharedPreferences.Editor editor = sIccidPreference.edit();
        if (!power && !"N/A".equals(iccid)) {
            putIccIdToPreference(editor, iccid);
        } else {
            removeIccIdFromPreference(editor, iccid);
        }
        editor.commit();
    }

    private void putIccIdToPreference(SharedPreferences.Editor editor, String iccid) {
        String printableIccid;
        if (iccid != null) {
            if ("N/A".equals(iccid)) {
                printableIccid = "N/A";
            } else {
                printableIccid = binaryToHex(getHashCode(SubscriptionInfo.getPrintableId(iccid)));
            }
            log("Add radio off SIM: " + printableIccid);
            editor.putInt(getHashCode(iccid), 0);
        }
    }

    private void removeIccIdFromPreference(SharedPreferences.Editor editor, String iccid) {
        String printableIccid;
        if (iccid != null) {
            if ("N/A".equals(iccid)) {
                printableIccid = "N/A";
            } else {
                printableIccid = binaryToHex(getHashCode(SubscriptionInfo.getPrintableId(iccid)));
            }
            log("Remove radio off SIM: " + printableIccid);
            editor.remove(getHashCode(iccid));
        }
    }

    public static void sendRequestBeforeSetRadioPower(boolean power, int phoneId) {
        log("Send request before EFUN, power:" + power + " phoneId:" + phoneId);
        notifyRadioPowerChange(power, phoneId);
    }

    public static boolean isPowerOnFeatureAllClosed() {
        return false;
    }

    public static boolean isFlightModePowerOffModemConfigEnabled() {
        if (SystemProperties.get("vendor.ril.testmode").equals("1")) {
            return SystemProperties.get("vendor.ril.test.poweroffmd").equals("1");
        }
        if (isOP01 || isOP09) {
            String fromAtci = SystemProperties.get("vendor.ril.atci.flightmode");
            if (fromAtci.equals("1")) {
                return true;
            }
            boolean isTestSim = SystemProperties.get("vendor.gsm.sim.ril.testsim").equals("1") || SystemProperties.get("vendor.gsm.sim.ril.testsim.2").equals("1") || SystemProperties.get("vendor.gsm.sim.ril.testsim.3").equals("1") || SystemProperties.get("vendor.gsm.sim.ril.testsim.4").equals("1");
            if (isTestSim) {
                return true;
            }
        }
        return mFlightModePowerOffModem;
    }

    public static boolean isFlightModePowerOffModemEnabled() {
        if (getInstance() == null) {
            log("Instance not exists, return config only");
            return isFlightModePowerOffModemConfigEnabled();
        }
        if (isFlightModePowerOffModemConfigEnabled()) {
            return !getInstance().mIsWifiOn;
        }
        return false;
    }

    public static boolean isModemPowerOff(int phoneId) {
        return getInstance().isModemOff(phoneId);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        int phoneIdForMsg = getCiIndex(msg);
        log("handleMessage msg.what: " + eventIdtoString(msg.what));
        switch (msg.what) {
            case 1:
            case 2:
            case 3:
            case 4:
                notifyRadioAvailable(msg.what - 1);
                break;
            case 5:
                forceSetRadioPower(true, phoneIdForMsg);
                break;
            case 6:
                log("handle EVENT_SET_MODEM_POWER_OFF_DONE -> " + (this.mModemPower ? "ON" : "OFF"));
                if (!this.mModemPower) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    ModemPowerMessage powerMessage = (ModemPowerMessage) ar.userObj;
                    log("handleModemPowerMessage, message:" + powerMessage.toString());
                    if (ar.exception == null) {
                        log("handleModemPowerMessage, result: " + ar.result);
                    } else {
                        log("handleModemPowerMessage, Unhandle ar.exception:" + ar.exception);
                    }
                    powerMessage.isFinish = true;
                    if (isSetModemPowerFinish()) {
                        cleanModemPowerMessage();
                        unMonitorModemPowerChangeDone();
                        if (ar.exception == null) {
                            log("send EVENT_MODEM_POWER_OFF_DONE");
                            this.mPowerSM.sendEvent(5);
                        } else {
                            log("retry turn off MD()");
                            setModemPower(false, this.mPhoneCount);
                        }
                        break;
                    }
                } else {
                    log("EVENT_SET_MODEM_POWER_OFF_DONE: wrong state");
                    break;
                }
                break;
            case 7:
            case 8:
            case 9:
            default:
                super.handleMessage(msg);
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                notifyDsbpStateChanged(msg.what, (AsyncResult) msg.obj);
                break;
            case 14:
                int activeModems = ((Integer) ((AsyncResult) msg.obj).result).intValue();
                notifyMultiSimConfigChanged(activeModems);
                break;
        }
    }

    private void notifyMultiSimConfigChanged(int activeModems) {
        int phoneCount = TelephonyManager.getDefault().getPhoneCount();
        int oldPhoneCount = this.mPhoneCount;
        log("multiSimConfigChanged, activeModems:" + activeModems + ", phoneCount:" + phoneCount + ", oldPhoneCount:" + oldPhoneCount);
        unregisterListener();
        this.mCi = PhoneFactory.getCommandsInterfaces();
        this.mPhoneCount = phoneCount;
        this.mBitmapForPhoneCount = convertPhoneCountIntoBitmap(phoneCount);
        registerListener();
        for (int i = oldPhoneCount; i < this.mPhoneCount; i++) {
            log("MultiSimSwitch, Phone[" + i + "] is a new phone");
            String iccid = readIccIdUsingPhoneId(i);
            if ("N/A".equals(iccid)) {
                log("MultiSimSwitch, Phone " + i + ":SIM ICCID not ready, do nothing");
                return;
            }
            if (!this.mAirplaneMode) {
                log("Set Radio Power on due to MultiSimConfigChanged, phoneId: " + i);
                PhoneFactory.getPhone(i).setRadioPower(true, false, false, false);
            }
        }
    }

    private void notifyDsbpStateChanged(int what, AsyncResult ar) {
        if (ar.exception == null && ar.result != null) {
            int state = ((Integer) ar.result).intValue();
            int phoneId = 0;
            switch (what) {
                case 10:
                    phoneId = 0;
                    break;
                case 11:
                    phoneId = 1;
                    break;
                case 12:
                    phoneId = 2;
                    break;
                case 13:
                    phoneId = 3;
                    break;
            }
            log("notifyDsbpStateChanged state:" + state + "phoneId:" + phoneId);
            if (state == 1) {
                this.mIsDsbpChanging[phoneId] = true;
                return;
            }
            this.mIsDsbpChanging[phoneId] = false;
            if (findMainCapabilityPhoneId() == phoneId && this.mIsPendingRadioByDsbpChanging) {
                this.mIsPendingRadioByDsbpChanging = false;
                setRadioPowerAfterCapabilitySwitch();
            }
        }
    }

    private String eventIdtoString(int what) {
        switch (what) {
            case 1:
            case 2:
            case 3:
            case 4:
                return "EVENT_RADIO_AVAILABLE";
            case 5:
                return "EVENT_VIRTUAL_SIM_ON";
            case 6:
            default:
                return null;
            case 7:
                return "EVENT_SET_SILENT_REBOOT_DONE";
            case 8:
                return "EVENT_REPORT_AIRPLANE_DONE";
            case 9:
                return "EVENT_REPORT_SIM_MODE_DONE";
            case 10:
            case 11:
            case 12:
            case 13:
                return "EVENT_DSBP_STATE_CHANGED";
            case 14:
                return "EVENT_MULTI_SIM_CONFIG_CHANGED";
        }
    }

    private int getCiIndex(Message msg) {
        Integer index = new Integer(0);
        if (msg != null) {
            if (msg.obj != null && (msg.obj instanceof Integer)) {
                index = (Integer) msg.obj;
            } else if (msg.obj != null && (msg.obj instanceof AsyncResult)) {
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.userObj != null && (ar.userObj instanceof Integer)) {
                    index = (Integer) ar.userObj;
                }
            }
        }
        return index.intValue();
    }

    protected boolean isModemOff(int phoneId) {
        TelephonyManager.MultiSimVariants config = TelephonyManager.getDefault().getMultiSimConfiguration();
        switch (AnonymousClass2.$SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[config.ordinal()]) {
            case 1:
                boolean powerOff = SystemProperties.get("vendor.ril.ipo.radiooff").equals("1");
                return powerOff;
            case 2:
                switch (phoneId) {
                    case 0:
                        boolean powerOff2 = SystemProperties.get("vendor.ril.ipo.radiooff").equals("1");
                        return powerOff2;
                    case 1:
                        boolean powerOff3 = SystemProperties.get("vendor.ril.ipo.radiooff.2").equals("1");
                        return powerOff3;
                    default:
                        return true;
                }
            case 3:
                boolean powerOff4 = SystemProperties.get("vendor.ril.ipo.radiooff").equals("1");
                return powerOff4;
            default:
                boolean powerOff5 = SystemProperties.get("vendor.ril.ipo.radiooff").equals("1");
                return powerOff5;
        }
    }

    public static synchronized void registerForRadioPowerChange(String name, IRadioPower iRadioPower) {
        if (name == null) {
            name = REGISTRANTS_WITH_NO_NAME;
        }
        log(name + " registerForRadioPowerChange");
        mNotifyRadioPowerChange.put(iRadioPower, name);
    }

    public static synchronized void unregisterForRadioPowerChange(IRadioPower iRadioPower) {
        log(mNotifyRadioPowerChange.get(iRadioPower) + " unregisterForRadioPowerChange");
        mNotifyRadioPowerChange.remove(iRadioPower);
    }

    private static synchronized void notifyRadioPowerChange(boolean power, int phoneId) {
        for (Map.Entry<IRadioPower, String> e : mNotifyRadioPowerChange.entrySet()) {
            log("notifyRadioPowerChange: user:" + e.getValue());
            IRadioPower iRadioPower = e.getKey();
            iRadioPower.notifyRadioPowerChange(power, phoneId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String s) {
        Rlog.d(LOG_TAG, "[RadioManager] " + s);
    }

    public boolean isAllowAirplaneModeChange() {
        log("always allow airplane mode");
        return true;
    }

    protected final Message[] monitorModemPowerChangeDone(boolean power, int phoneBitMap, int mainCapabilityPhoneId) {
        this.mModemPower = power;
        log("monitorModemPowerChangeDone, Power:" + power + ", PhoneBitMap:" + phoneBitMap + ", mainCapabilityPhoneId:" + mainCapabilityPhoneId + ", mPhoneCount:" + this.mPhoneCount);
        this.mNeedIgnoreMessageForChangeDone = false;
        int i = this.mPhoneCount;
        Message[] msgs = new Message[i];
        if (!this.mModemPower) {
            ModemPowerMessage[] messages = createMessage(power, phoneBitMap, mainCapabilityPhoneId, i);
            this.mModemPowerMessages = messages;
            for (int i2 = 0; i2 < messages.length; i2++) {
                if (messages[i2] != null) {
                    msgs[i2] = obtainMessage(6, messages[i2]);
                }
            }
        }
        return msgs;
    }

    protected void unMonitorModemPowerChangeDone() {
        this.mNeedIgnoreMessageForChangeDone = true;
        Intent intent = new Intent(ACTION_AIRPLANE_CHANGE_DONE);
        intent.putExtra(EXTRA_AIRPLANE_MODE, true ^ this.mModemPower);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        for (int i = 0; i < this.mPhoneCount; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.mCi.unregisterForRadioStateChanged(this);
                log("unMonitorModemPowerChangeDone, phoneId = " + i);
            }
        }
    }

    protected boolean waitForReady(boolean state) {
        if (waitRadioAvaliable(state)) {
            log("waitForReady, wait radio avaliable");
            this.mPowerSM.updateModemPowerState(state, this.mBitmapForPhoneCount, 2);
            return true;
        }
        return false;
    }

    private boolean waitRadioAvaliable(boolean state) {
        boolean wait = (this.mIsWifiOnlyDevice || isRadioAvaliable()) ? false : true;
        log("waitRadioAvaliable, state=" + state + ", wait=" + wait);
        return wait;
    }

    private boolean isRadioAvaliable() {
        for (int i = 0; i < this.mPhoneCount; i++) {
            if (!isRadioAvaliable(i)) {
                log("isRadioAvaliable=false, phoneId = " + i);
                return false;
            }
        }
        return true;
    }

    private boolean isRadioAvaliable(int phoneId) {
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            return false;
        }
        log("phoneId = " + phoneId + ", RadioState=" + phone.mCi.getRadioState());
        return phone.mCi.getRadioState() != 2;
    }

    private final boolean isSetModemPowerFinish() {
        if (this.mModemPowerMessages != null) {
            int i = 0;
            while (true) {
                ModemPowerMessage[] modemPowerMessageArr = this.mModemPowerMessages;
                if (i < modemPowerMessageArr.length) {
                    if (modemPowerMessageArr[i] != null) {
                        log("isSetModemPowerFinish [" + i + "]: " + this.mModemPowerMessages[i]);
                        if (!this.mModemPowerMessages[i].isFinish) {
                            return false;
                        }
                    } else {
                        log("isSetModemPowerFinish [" + i + "]: MPMsg is null");
                    }
                    i++;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    private final void cleanModemPowerMessage() {
        log("cleanModemPowerMessage");
        if (this.mModemPowerMessages != null) {
            int i = 0;
            while (true) {
                ModemPowerMessage[] modemPowerMessageArr = this.mModemPowerMessages;
                if (i < modemPowerMessageArr.length) {
                    modemPowerMessageArr[i] = null;
                    i++;
                } else {
                    this.mModemPowerMessages = null;
                    return;
                }
            }
        }
    }

    private static final class ModemPowerMessage {
        public boolean isFinish = false;
        private final int mPhoneId;

        public ModemPowerMessage(int phoneId) {
            this.mPhoneId = phoneId;
        }

        public String toString() {
            return "MPMsg [mPhoneId=" + this.mPhoneId + ", isFinish=" + this.isFinish + "]";
        }
    }

    private static final ModemPowerMessage[] createMessage(boolean power, int phoneBitMap, int mainCapabilityPhoneId, int phoneCount) {
        TelephonyManager.MultiSimVariants config = TelephonyManager.getDefault().getMultiSimConfiguration();
        log("createMessage, config:" + config);
        ModemPowerMessage[] msgs = new ModemPowerMessage[phoneCount];
        switch (AnonymousClass2.$SwitchMap$android$telephony$TelephonyManager$MultiSimVariants[config.ordinal()]) {
            case 1:
            case 2:
            case 3:
                msgs[mainCapabilityPhoneId] = new ModemPowerMessage(mainCapabilityPhoneId);
                break;
            default:
                int phoneId = PhoneFactory.getDefaultPhone().getPhoneId();
                msgs[phoneId] = new ModemPowerMessage(phoneId);
                break;
        }
        for (int i = 0; i < phoneCount; i++) {
            if (msgs[i] != null) {
                log("createMessage, [" + i + "]: " + msgs[i].toString());
            }
        }
        return msgs;
    }

    private void registerListener() {
        for (int i = 0; i < this.mPhoneCount; i++) {
            this.mCi[i].registerForVirtualSimOn(this, 5, null);
            this.mCi[i].registerForAvailable(this, EVENT_RADIO_AVAILABLE[i], (Object) null);
            this.mCi[i].registerForDsbpStateChanged(this, EVENT_DSBP_STATE_CHANGED[i], null);
        }
    }

    private void unregisterListener() {
        int i = 0;
        while (true) {
            MtkRIL[] mtkRILArr = this.mCi;
            if (i < mtkRILArr.length) {
                mtkRILArr[i].unregisterForVirtualSimOn(this);
                this.mCi[i].unregisterForAvailable(this);
                this.mCi[i].unregisterForDsbpStateChanged(this);
                i++;
            } else {
                return;
            }
        }
    }

    private boolean isRequiredRadioOff(String iccid) {
        if (sIccidPreference.contains(getHashCode(iccid))) {
            return true;
        }
        return false;
    }

    public String getHashCode(String iccid) {
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-256");
            alga.update(iccid.getBytes());
            byte[] hashCode = alga.digest();
            String strIccid = new String(hashCode);
            return strIccid;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("isRequiredRadioOff SHA-256 must exist");
        }
    }

    private class PowerSM extends StateMachine {
        private int mCurrentModemCause;
        public boolean mCurrentModemPower;
        private int mDesiredModemCause;
        public boolean mDesiredModemPower;
        protected PowerIdleState mIdleState;
        protected int mPhoneBitMap;
        protected PowerTurnOffState mTurnOffState;
        protected PowerTurnOnState mTurnOnState;
        private PowerSM self;

        /* JADX WARN: Multi-variable type inference failed */
        PowerSM(String str) {
            super(str);
            this.self = null;
            this.mIdleState = new PowerIdleState();
            this.mTurnOnState = new PowerTurnOnState();
            this.mTurnOffState = new PowerTurnOffState();
            this.mCurrentModemPower = true;
            this.mDesiredModemPower = true;
            this.mCurrentModemCause = 0;
            this.mDesiredModemCause = 0;
            addState(this.mIdleState);
            addState(this.mTurnOnState);
            addState(this.mTurnOffState);
            setInitialState(this.mIdleState);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateModemPowerState(boolean power, int phoneBitMap, int cause) {
            if ((!power) & RadioManager.isUnderCryptKeeper()) {
                log("Skip MODEM_POWER_OFF due to CryptKeeper mode");
                return;
            }
            this.mPhoneBitMap = phoneBitMap;
            if (4 == cause) {
                if (RadioManager.this.mAirplaneMode && RadioManager.isFlightModePowerOffModemConfigEnabled()) {
                    this.mDesiredModemCause = 4 | this.mDesiredModemCause;
                    if (RadioManager.this.mIsWifiOn) {
                        this.mDesiredModemPower = true;
                    } else {
                        this.mDesiredModemPower = false;
                    }
                }
                sendEvent(power ? 1 : 2);
                return;
            }
            if (2 == cause) {
                this.mDesiredModemCause |= 2;
                this.mDesiredModemPower = power;
                sendEvent(power ? 1 : 2);
                return;
            }
            if (16 == cause) {
                this.mDesiredModemCause |= 16;
                this.mDesiredModemPower = power;
                sendEvent(1);
            } else if (8 == cause) {
                this.mDesiredModemCause |= 8;
                this.mDesiredModemPower = power;
                sendEvent(power ? 1 : 2);
            } else if (64 == cause) {
                this.mCurrentModemPower = true;
                this.mDesiredModemPower = false;
                sendEvent(2);
            } else if (128 == cause) {
                sendEvent(6);
            }
        }

        private void sendEvent(int event, int arg1) {
            Rlog.i(RadioManager.LOG_TAG, "sendEvent: " + PowerEvent.print(event));
            Message msg = Message.obtain(getHandler(), event);
            msg.arg1 = arg1;
            getHandler().sendMessage(msg);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendEvent(int event) {
            Rlog.i(RadioManager.LOG_TAG, "sendEvent: " + PowerEvent.print(event));
            Message msg = Message.obtain(getHandler(), event);
            getHandler().sendMessage(msg);
        }

        private class PowerIdleState extends State {
            private PowerIdleState() {
            }

            public void enter() {
                Rlog.i(RadioManager.LOG_TAG, "PowerIdleState: enter");
                PowerSM.this.log("mDesiredModemPower: " + PowerSM.this.mDesiredModemPower + " mCurrentModemPower: " + PowerSM.this.mCurrentModemPower);
                if (RadioManager.this.mPowerSM.mDesiredModemPower != RadioManager.this.mPowerSM.mCurrentModemPower) {
                    if (RadioManager.this.mPowerSM.mDesiredModemPower) {
                        RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mTurnOnState);
                    } else {
                        RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mTurnOffState);
                    }
                }
            }

            public void exit() {
                Rlog.i(RadioManager.LOG_TAG, "PowerIdleState: exit");
            }

            public boolean processMessage(Message msg) {
                Rlog.i(RadioManager.LOG_TAG, "processMessage: " + PowerEvent.print(msg.what));
                switch (msg.what) {
                    case 1:
                        if (RadioManager.this.mPowerSM.mDesiredModemPower != RadioManager.this.mPowerSM.mCurrentModemPower) {
                            RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mTurnOnState);
                        } else {
                            Rlog.i(RadioManager.LOG_TAG, "the same power state: " + PowerEvent.print(msg.what));
                            Intent intent = new Intent(RadioManager.ACTION_MODEM_POWER_NO_CHANGE);
                            intent.putExtra(RadioManager.EXTRA_MODEM_POWER, true);
                            RadioManager.this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
                        }
                        return true;
                    case 2:
                        if (RadioManager.this.mPowerSM.mDesiredModemPower != RadioManager.this.mPowerSM.mCurrentModemPower) {
                            RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mTurnOffState);
                        } else {
                            Rlog.i(RadioManager.LOG_TAG, "the same power state: " + PowerEvent.print(msg.what));
                        }
                        return true;
                    case 3:
                        RadioManager.this.mPowerSM.mCurrentModemPower = true;
                        if (RadioManager.this.mPowerSM.mDesiredModemPower != RadioManager.this.mPowerSM.mCurrentModemPower) {
                            if (RadioManager.this.mPowerSM.mDesiredModemPower) {
                                RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mTurnOnState);
                            } else {
                                RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mTurnOffState);
                            }
                        }
                        return true;
                    case 4:
                    default:
                        Rlog.i(RadioManager.LOG_TAG, "un-expected event, stay at idle");
                        return true;
                    case 5:
                        RadioManager.this.mPowerSM.mCurrentModemPower = false;
                        if (RadioManager.this.mPowerSM.mDesiredModemPower != RadioManager.this.mPowerSM.mCurrentModemPower) {
                            RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mDesiredModemPower ? RadioManager.this.mPowerSM.mTurnOnState : RadioManager.this.mPowerSM.mTurnOffState);
                        } else {
                            Rlog.i(RadioManager.LOG_TAG, "the same power state: " + PowerEvent.print(msg.what));
                        }
                        return true;
                }
            }
        }

        private class PowerTurnOnState extends State {
            private PowerTurnOnState() {
            }

            public void enter() {
                Rlog.i(RadioManager.LOG_TAG, "PowerTurnOnState: enter");
                if (!RadioManager.this.waitForReady(true) && !((MtkProxyController) MtkProxyController.getInstance()).isCapabilitySwitching()) {
                    RadioManager.this.mPowerSM.mCurrentModemPower = true;
                    RadioManager.this.mPowerSM.mCurrentModemCause = RadioManager.this.mPowerSM.mDesiredModemCause;
                    RadioManager.this.setModemPower(true, RadioManager.this.mPowerSM.mPhoneBitMap);
                }
            }

            public void exit() {
                Rlog.i(RadioManager.LOG_TAG, "PowerTurnOnState: exit");
            }

            public boolean processMessage(Message msg) {
                Rlog.i(RadioManager.LOG_TAG, "processMessage: " + PowerEvent.print(msg.what));
                switch (msg.what) {
                    case 3:
                        PowerSM.this.mCurrentModemPower = true;
                        RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mIdleState);
                        return true;
                    case 4:
                    case 6:
                        RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mIdleState);
                        return true;
                    case 5:
                    default:
                        Rlog.i(RadioManager.LOG_TAG, "un-expected event, stay at PowerTurnOnState");
                        return true;
                }
            }
        }

        private class PowerTurnOffState extends State {
            private PowerTurnOffState() {
            }

            public void enter() {
                Rlog.i(RadioManager.LOG_TAG, "PowerTurnOffState: enter");
                if (!RadioManager.this.waitForReady(false) && !((MtkProxyController) MtkProxyController.getInstance()).isCapabilitySwitching()) {
                    RadioManager.this.mPowerSM.mCurrentModemPower = false;
                    RadioManager.this.mPowerSM.mCurrentModemCause = RadioManager.this.mPowerSM.mDesiredModemCause;
                    RadioManager.this.setModemPower(false, RadioManager.this.mPowerSM.mPhoneBitMap);
                }
            }

            public void exit() {
                Rlog.i(RadioManager.LOG_TAG, "PowerTurnOffState: exit");
            }

            public boolean processMessage(Message msg) {
                Rlog.i(RadioManager.LOG_TAG, "processMessage: " + PowerEvent.print(msg.what));
                switch (msg.what) {
                    case 3:
                        PowerSM.this.mCurrentModemPower = true;
                        RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mIdleState);
                        return true;
                    case 4:
                    default:
                        Rlog.i(RadioManager.LOG_TAG, "un-expected event, stay at PowerTurnOffState");
                        return true;
                    case 5:
                    case 6:
                        RadioManager.this.mPowerSM.transitionTo(RadioManager.this.mPowerSM.mIdleState);
                        return true;
                }
            }
        }
    }

    static class PowerEvent {
        static final int EVENT_MODEM_POWER_OFF = 2;
        static final int EVENT_MODEM_POWER_OFF_DONE = 5;
        static final int EVENT_MODEM_POWER_ON = 1;
        static final int EVENT_MODEM_POWER_ON_DONE = 4;
        static final int EVENT_RADIO_AVAILABLE = 3;
        static final int EVENT_SIM_SWITCH_DONE = 6;
        static final int EVENT_START = 0;

        PowerEvent() {
        }

        public static String print(int eventCode) {
            switch (eventCode) {
                case 1:
                    return "EVENT_MODEM_POWER_ON";
                case 2:
                    return "EVENT_MODEM_POWER_OFF";
                case 3:
                    return "EVENT_RADIO_AVAILABLE";
                case 4:
                    return "EVENT_MODEM_POWER_ON_DONE";
                case 5:
                    return "EVENT_MODEM_POWER_OFF_DONE";
                case 6:
                    return "EVENT_SIM_SWITCH_DONE";
                default:
                    throw new IllegalArgumentException("Invalid eventCode: " + eventCode);
            }
        }
    }

    static class ModemPowerCasue {
        static final int CAUSE_AIRPLANE_MODE = 2;
        static final int CAUSE_ECC = 16;
        static final int CAUSE_FORCE = 32;
        static final int CAUSE_IPO = 8;
        static final int CAUSE_RADIO_AVAILABLE = 64;
        static final int CAUSE_SIM_SWITCH = 128;
        static final int CAUSE_START = 0;
        static final int CAUSE_WIFI_CALLING = 4;

        ModemPowerCasue() {
        }

        public static String print(int eventCode) {
            switch (eventCode) {
                case 2:
                    return "CAUSE_AIRPLANE_MODE";
                case 4:
                    return "CAUSE_WIFI_CALLING";
                case 8:
                    return "CAUSE_IPO";
                case 16:
                    return "CAUSE_ECC";
                case 32:
                    return "CAUSE_FORCE";
                case 64:
                    return "CAUSE_RADIO_AVAILABLE";
                default:
                    throw new IllegalArgumentException("Invalid eventCode: " + eventCode);
            }
        }
    }

    private String binaryToHex(String binaryStr) {
        return String.format("%040x", new BigInteger(1, binaryStr.getBytes()));
    }
}
