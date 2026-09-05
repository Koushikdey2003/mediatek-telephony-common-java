package com.mediatek.internal.telephony.data;

import android.os.Looper;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.util.ArrayUtils;
import com.android.telephony.Rlog;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataConfigManager extends DataConfigManager {
    private static final String LOG_TAG = "MtkDCM";
    private static final String SKIP_DATA_STALL_ALARM = "persist.vendor.skip.data.stall.alarm";
    private boolean mCcDomesticRoamingEnabled;
    private String[] mCcDomesticRoamingSpecifiedNw;
    private boolean mCcIntlRoamingEnabled;
    private boolean mHandleDialingDuringCall;
    private int mMtuForTestSim;
    private String[] mSkipDataStallApnList;
    private Set<Integer> mSkipRoamingOnIwlanApnTypes;
    private boolean mVolteIotFirewall;

    public MtkDataConfigManager(Phone phone, Looper looper) {
        super(phone, looper);
        this.mSkipDataStallApnList = null;
        this.mSkipRoamingOnIwlanApnTypes = new HashSet();
        this.mCcDomesticRoamingEnabled = false;
        this.mCcDomesticRoamingSpecifiedNw = null;
        this.mCcIntlRoamingEnabled = false;
        this.mVolteIotFirewall = false;
        this.mMtuForTestSim = 0;
        this.mHandleDialingDuringCall = true;
    }

    public void updateMtkExtendConfig(PersistableBundle config) {
        updateSkipDataStallApnList(config);
        updateRoamingConfigs(config);
        updateVolteIOTConfig(config);
        updateMtuForTestSim(config);
        updateHandleDialingDuringCall(config);
    }

    public boolean isPingTestBeforeAutoDataSwitchRequired() {
        if (this.mPhone.getHalVersion(1).less(RIL.RADIO_HAL_VERSION_2_1)) {
            return false;
        }
        return super.isPingTestBeforeAutoDataSwitchRequired();
    }

    private void updateSkipDataStallApnList(PersistableBundle config) {
        if (config != null) {
            this.mSkipDataStallApnList = config.getStringArray("mtk_skip_data_stall_apn_list_strings");
        }
    }

    private void updateRoamingConfigs(PersistableBundle config) {
        if (config == null) {
            return;
        }
        int[] apnTypes = config.getIntArray("mtk_skip_roaming_data_on_iwlan_apn_types_int_array");
        if (apnTypes != null) {
            for (int apn : apnTypes) {
                this.mSkipRoamingOnIwlanApnTypes.add(Integer.valueOf(apn));
            }
        }
        this.mCcDomesticRoamingEnabled = config.getBoolean("mtk_domestic_roaming_enabled_only_by_mobile_data_setting");
        this.mCcDomesticRoamingSpecifiedNw = config.getStringArray("mtk_domestic_roaming_enabled_only_by_mobile_data_setting_check_nw_plmn");
        this.mCcIntlRoamingEnabled = config.getBoolean("mtk_intl_roaming_enabled_only_by_roaming_data_setting");
    }

    private void updateMtuForTestSim(PersistableBundle config) {
        if (config != null) {
            this.mMtuForTestSim = config.getInt("mtk_test_sim_mtu_value_int");
        }
    }

    private void updateVolteIOTConfig(PersistableBundle config) {
        if (config != null) {
            this.mVolteIotFirewall = config.getBoolean("mtk_volte_iot_firewall_enable_bool");
            log("updateVolteIOTConfig: mVolteIotFirewall " + this.mVolteIotFirewall);
        }
    }

    private void updateHandleDialingDuringCall(PersistableBundle config) {
        if (config != null) {
            this.mHandleDialingDuringCall = config.getBoolean("mtk_handle_dialing_data_during_call_bool");
        }
    }

    public boolean shouldSkipDataStall(String apn) {
        boolean skipStall;
        boolean isTestSim = false;
        int phoneId = this.mPhone.getPhoneId();
        MtkDataHelper dcHelper = MtkDataHelper.getInstance();
        if (SubscriptionManager.isValidPhoneId(phoneId) && dcHelper != null && dcHelper.isTestIccCard(phoneId)) {
            isTestSim = true;
        }
        if (isTestSim) {
            if (SystemProperties.get(SKIP_DATA_STALL_ALARM).equals("0")) {
                skipStall = false;
            } else {
                skipStall = true;
            }
        } else if (SystemProperties.get(SKIP_DATA_STALL_ALARM).equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            skipStall = true;
        } else {
            skipStall = false;
        }
        if (skipStall) {
            log("skipStall, sim type: " + isTestSim);
            return true;
        }
        String[] strArr = this.mSkipDataStallApnList;
        if (strArr != null) {
            for (String item : strArr) {
                if (item != null && item.equals(apn)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Integer> getSkipRoamingOnIwlanApnTypes() {
        HashSet<Integer> types = new HashSet<>(this.mSkipRoamingOnIwlanApnTypes);
        return types;
    }

    public boolean getDomesticRoamingConfig() {
        String[] strArr;
        boolean z = this.mCcDomesticRoamingEnabled;
        if (!z || (strArr = this.mCcDomesticRoamingSpecifiedNw) == null) {
            return z;
        }
        return ArrayUtils.contains(strArr, TelephonyManager.getDefault().getNetworkOperatorForPhone(this.mPhone.getPhoneId()));
    }

    public boolean getInternatioalRoamingConfig() {
        return this.mCcIntlRoamingEnabled;
    }

    public boolean getVolteIotFirewallConfig() {
        return this.mVolteIotFirewall;
    }

    public int getMtuForTestSim() {
        return this.mMtuForTestSim;
    }

    public boolean getDataFdnSupported() {
        CarrierConfigManager configManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        if (configManager == null) {
            loge("config manager is null, return false.");
            return false;
        }
        PersistableBundle b = configManager.getConfigForSubId(this.mPhone.getSubId());
        if (b == null) {
            loge("PersistableBundle is null, return false.");
            return false;
        }
        boolean isDataFdnSupported = b.getBoolean("mtk_data_fdn_supported_bool");
        log("get data Fdn Supported: " + isDataFdnSupported);
        return isDataFdnSupported;
    }

    public boolean getHandleDialingDuringCall() {
        return this.mHandleDialingDuringCall;
    }

    private void log(String s) {
        Rlog.d(LOG_TAG, this.mLogTag + ": " + s);
    }

    private void loge(String s) {
        Rlog.e(LOG_TAG, this.mLogTag + ":" + s);
    }
}
