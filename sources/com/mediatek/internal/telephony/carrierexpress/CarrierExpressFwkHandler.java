package com.mediatek.internal.telephony.carrierexpress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;

/* JADX INFO: loaded from: classes.dex */
public class CarrierExpressFwkHandler extends Handler {
    private static final String ACTION_CXP_RESET_MODEM = "com.mediatek.common.carrierexpress.cxp_reset_modem";
    private static final String ACTION_CXP_SET_VENDOR_PROP = "com.mediatek.common.carrierexpress.cxp_set_vendor_prop";
    private static final String LOG_TAG = "CarrierExpress";
    private static CarrierExpressFwkHandler sInstance = null;
    private final BroadcastReceiver mCarrierExpressReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.carrierexpress.CarrierExpressFwkHandler.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Rlog.d(CarrierExpressFwkHandler.LOG_TAG, "BroadcastReceiver(), action= " + action);
            if (CarrierExpressFwkHandler.this.mPhone != null) {
                CarrierExpressFwkHandler carrierExpressFwkHandler = CarrierExpressFwkHandler.this;
                carrierExpressFwkHandler.mCi = carrierExpressFwkHandler.mPhone.mCi;
                if (CarrierExpressFwkHandler.this.mCi == null) {
                    Rlog.e(CarrierExpressFwkHandler.LOG_TAG, "MtkRIL is null");
                    return;
                }
                if (CarrierExpressFwkHandler.ACTION_CXP_RESET_MODEM.equals(action)) {
                    CarrierExpressFwkHandler.this.startResetModem();
                    return;
                }
                if (CarrierExpressFwkHandler.ACTION_CXP_SET_VENDOR_PROP.equals(action)) {
                    CarrierExpressFwkHandler.this.mCi.setVendorSetting(0, intent.getStringExtra("OPTR"), null);
                    CarrierExpressFwkHandler.this.mCi.setVendorSetting(1, intent.getStringExtra("SPEC"), null);
                    CarrierExpressFwkHandler.this.mCi.setVendorSetting(2, intent.getStringExtra("SEG"), null);
                    if (CarrierExpressFwkHandler.this.needCxpSetSbp()) {
                        CarrierExpressFwkHandler.this.mCi.setVendorSetting(3, intent.getStringExtra("SBP"), null);
                        CarrierExpressFwkHandler.this.mCi.setVendorSetting(4, intent.getStringExtra("SUBID"), null);
                        return;
                    }
                    return;
                }
                return;
            }
            Rlog.e(CarrierExpressFwkHandler.LOG_TAG, "phone is null, cannot reset modem");
        }
    };
    private MtkRIL mCi;
    private Context mContext;
    private Phone mPhone;

    public CarrierExpressFwkHandler() {
        this.mPhone = null;
        try {
            this.mPhone = PhoneFactory.getDefaultPhone();
        } catch (IllegalStateException e) {
            Rlog.e(LOG_TAG, "failed to get default phone from PhoneFactory: " + e.toString());
        }
        Phone phone = this.mPhone;
        if (phone == null) {
            Rlog.e(LOG_TAG, "default phone is null");
            return;
        }
        Context context = phone.getContext();
        this.mContext = context;
        if (context == null) {
            Rlog.e(LOG_TAG, "missing Context");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CXP_RESET_MODEM);
        filter.addAction(ACTION_CXP_SET_VENDOR_PROP);
        this.mContext.registerReceiver(this.mCarrierExpressReceiver, filter);
    }

    public static void init() {
        synchronized (CarrierExpressFwkHandler.class) {
            if (sInstance == null) {
                sInstance = new CarrierExpressFwkHandler();
            } else {
                Rlog.d(LOG_TAG, "init() called multiple times!  sInstance = " + sInstance);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startResetModem() {
        if (!needCxpSetSbp()) {
            Rlog.i(LOG_TAG, "No need reset modem for rild will set sbp");
            return;
        }
        MtkRIL mtkRIL = this.mCi;
        if (mtkRIL != null) {
            mtkRIL.restartRILD(null);
            Rlog.d(LOG_TAG, "Reset modem");
        } else {
            Rlog.e(LOG_TAG, "MtkRIL is null, cannot reset modem");
        }
    }

    void dispose() {
        this.mContext.unregisterReceiver(this.mCarrierExpressReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needCxpSetSbp() {
        String simSwitchMode = SystemProperties.get("persist.vendor.mtk_usp_switch_mode", RadioCapabilitySwitchUtil.IMSI_READY);
        String setSbpPlace = SystemProperties.get("ro.vendor.ril.set_sbp_place", "0");
        if ((simSwitchMode.equals(RadioCapabilitySwitchUtil.IMSI_READY) || simSwitchMode.equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN) || simSwitchMode.equals("4")) && !setSbpPlace.equals("0")) {
            return false;
        }
        return true;
    }
}
