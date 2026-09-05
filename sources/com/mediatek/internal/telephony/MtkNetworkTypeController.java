package com.mediatek.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityNr;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import com.android.internal.telephony.DisplayInfoController;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.util.ArrayUtils;

/* JADX INFO: loaded from: classes.dex */
public final class MtkNetworkTypeController extends NetworkTypeController {
    private static final int EVENT_5G_ICON_INFO_CHANGED = 100;
    private static final int EVENT_WLAN_REG_STATE_CHANGED = 101;
    public static final String TAG = "MtkNetworkTypeController";
    private DisplayInfoController mDisplayInfoController;
    private int mDisplayNrAdvanced;
    private Handler mHandler;
    private int mNrAdvancedPhysicalChannelConfigItemCount;

    public MtkNetworkTypeController(Phone phone, DisplayInfoController displayInfoController) {
        super(phone, displayInfoController);
        this.mDisplayNrAdvanced = 0;
        this.mNrAdvancedPhysicalChannelConfigItemCount = 0;
        this.mHandler = new MtkHandler();
        this.mDisplayInfoController = displayInfoController;
    }

    protected void registerForAllEvents() {
        super.registerForAllEvents();
        this.mPhone.mCi.registerFor5gUWInfo(this.mHandler, 100, null);
        this.mPhone.getServiceStateTracker().registerForDataRegStateOrRatChanged(2, this.mHandler, 101, (Object) null);
    }

    protected void unRegisterForAllEvents() {
        super.unRegisterForAllEvents();
        this.mPhone.getServiceStateTracker().unregisterForDataRegStateOrRatChanged(2, getHandler());
        this.mPhone.mCi.unregisterFor5gUWInfo(this.mHandler);
    }

    protected int getCurrentOverrideNetworkType() {
        int displayNetworkType = super.getCurrentOverrideNetworkType();
        Rlog.d(TAG, "[Override]getCurrentOverrideNetworkType: " + displayNetworkType);
        if (isDataInService() && this.mDisplayNrAdvanced == 1) {
            return 5;
        }
        return displayNetworkType;
    }

    protected boolean isNrAdvanced() {
        if (this.mNrAdvancedPhysicalChannelConfigItemCount > 0) {
            int physicalChannelItemCount = this.mPhysicalChannelConfigs == null ? 0 : this.mPhysicalChannelConfigs.size();
            int dataNetworkType = getDataNetworkType();
            if (physicalChannelItemCount != this.mNrAdvancedPhysicalChannelConfigItemCount || dataNetworkType != 20) {
                return false;
            }
        }
        return super.isNrAdvanced();
    }

    protected boolean isAdditionalNrAdvancedBand() {
        boolean result = false;
        if (this.mPhysicalChannelConfigs == null || this.mPhysicalChannelConfigs.isEmpty()) {
            NetworkRegistrationInfo nri = this.mServiceState.getNetworkRegistrationInfo(2, 1);
            if (nri == null || nri.getCellIdentity() == null) {
                return false;
            }
            CellIdentity cellIdentity = nri.getCellIdentity();
            if (cellIdentity.getType() != 6) {
                return false;
            }
            int[] bands = ((CellIdentityNr) cellIdentity).getBands();
            for (int band : bands) {
                if (ArrayUtils.contains(this.mAdditionalNrAdvancedBandsList, band)) {
                    log("use additional NR band[" + band + "] as MMWave");
                    result = true;
                }
            }
            return result;
        }
        boolean result2 = super.isAdditionalNrAdvancedBand();
        return result2;
    }

    private boolean isDataInService() {
        NetworkRegistrationInfo nri = this.mServiceState.getNetworkRegistrationInfo(2, 1);
        return nri != null && nri.isInService();
    }

    private class MtkHandler extends Handler {
        private MtkHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Rlog.d(MtkNetworkTypeController.TAG, "handleMessage: msg = " + msg.what);
            switch (msg.what) {
                case 100:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.exception != null || ar.result == null) {
                        MtkNetworkTypeController.this.loge("EVENT_5G_ICON_INFO_CHANGED exception");
                    } else {
                        int[] info = (int[]) ar.result;
                        if (info.length != 3 && info.length != 4) {
                            MtkNetworkTypeController.this.loge("EVENT_5G_ICON_INFO_CHANGED data size wrong: " + info.length);
                        } else {
                            int display = info[0];
                            Rlog.d(MtkNetworkTypeController.TAG, "EVENT_5G_ICON_INFO_CHANGED: new= " + display + ", oldDisplay = " + MtkNetworkTypeController.this.mDisplayNrAdvanced);
                            if (display != MtkNetworkTypeController.this.mDisplayNrAdvanced) {
                                MtkNetworkTypeController.this.mDisplayNrAdvanced = display;
                                MtkNetworkTypeController.this.updateOverrideNetworkType();
                            }
                        }
                    }
                    break;
                case 101:
                    MtkNetworkTypeController.this.mDisplayInfoController.updateTelephonyDisplayInfo();
                    break;
            }
        }
    }

    protected void onParseCarrierConfigs(PersistableBundle config) {
        this.mNrAdvancedPhysicalChannelConfigItemCount = config.getInt("mtk_nr_advanced_physical_channel_config_items_count_int", 0);
        Rlog.d(TAG, "mNrAdvancedPhysicalChannelConfigItemCount = " + this.mNrAdvancedPhysicalChannelConfigItemCount);
    }
}
