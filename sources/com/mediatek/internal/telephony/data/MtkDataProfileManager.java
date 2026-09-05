package com.mediatek.internal.telephony.data;

import android.R;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Telephony;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.data.ApnSetting;
import android.telephony.data.DataProfile;
import android.telephony.data.TrafficDescriptor;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataProfileManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.uicc.MtkIccUtilsEx;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class MtkDataProfileManager extends DataProfileManager {
    private static final int BASE = 100;
    private static final boolean DBG = true;
    private static final int EVENT_CHECK_FDN_LIST = 105;
    private static final int EVENT_FDN_CHANGED = 103;
    private static final int EVENT_FDN_ENABLE_CHANGED = 106;
    private static final int EVENT_ICC_CHANGED = 101;
    private static final int EVENT_PLMN_DATA = 102;
    private static final int EVENT_RADIO_NOT_AVAILABLE = 107;
    private static final int EVENT_RECORDS_OVERRIDE = 104;
    private static final String FDN_CONTENT_URI = "content://icc/fdn";
    private static final String FDN_CONTENT_URI_WITH_SUB_ID = "content://icc/fdn/subId/";
    private static final String FDN_FOR_ALLOW_DATA = "*99#";
    private static final String LOG_TAG = "MtkDPM";
    private static final boolean VDBG = Build.IS_ENG;
    private static final boolean mEnableDataEnhance = MtkDataHelper.hasOperatorIaCapability();
    private String mCurrentOperatorNumeric;
    private boolean mFdnConstraint;
    private IccRecords mIccRecords;
    private final BroadcastReceiver mIntentReceiverEx;
    private boolean mIsFdnChecked;
    private boolean mIsFdnEnableStateChangedRegistered;
    private boolean mIsMatchFdnForAllowData;
    private boolean mIsPhbStateChangedIntentRegistered;
    private boolean mIsRecordsOverride;
    private int mMtuFromResource;
    private final DpmOnSubscriptionsChangedListener mOnSubscriptionsChangedListener;
    private BroadcastReceiver mPhbStateChangedIntentReceiver;
    private int mPhoneType;
    private PlmnMvnoData mPlmnMvnoData;
    private final SettingsObserver mSettingsObserver;
    private SubscriptionManager mSubscriptionManager;
    private Handler mWorkerHandler;

    private class DpmOnSubscriptionsChangedListener extends SubscriptionManager.OnSubscriptionsChangedListener {
        private final AtomicInteger mCurrentSubId;

        private DpmOnSubscriptionsChangedListener() {
            this.mCurrentSubId = new AtomicInteger(-1);
        }

        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            int subId = MtkDataProfileManager.this.mPhone.getSubId();
            MtkDataProfileManager.this.log("onSubscriptionsChanged: subId = " + subId + ", mPreviousSubId = " + this.mCurrentSubId);
            if (SubscriptionManager.isValidSubscriptionId(subId)) {
                if (this.mCurrentSubId.getAndSet(subId) != subId) {
                    MtkDataProfileManager.this.onSubIdReady();
                }
            } else if (SubscriptionManager.isValidSubscriptionId(this.mCurrentSubId.get())) {
                this.mCurrentSubId.set(-1);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkDataProfileManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, Looper looper, DataProfileManager.DataProfileManagerCallback callback) {
        super(phone, dataNetworkController, dataServiceManager, looper, callback);
        this.mPhoneType = 0;
        this.mIccRecords = null;
        this.mIsRecordsOverride = false;
        this.mCurrentOperatorNumeric = null;
        this.mPlmnMvnoData = null;
        this.mMtuFromResource = 0;
        this.mFdnConstraint = false;
        DpmOnSubscriptionsChangedListener dpmOnSubscriptionsChangedListener = new DpmOnSubscriptionsChangedListener();
        this.mOnSubscriptionsChangedListener = dpmOnSubscriptionsChangedListener;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                MtkDataProfileManager.this.log("mIntentReceiverEx onReceive: action=" + action);
                int phoneId = intent.getIntExtra("phone", -1);
                if (phoneId != MtkDataProfileManager.this.mPhone.getPhoneId()) {
                    return;
                }
                if (action.equals("android.intent.action.RADIO_TECHNOLOGY")) {
                    String operatorNumeric = MtkDataProfileManager.this.mtkGetOperatorNumeric();
                    MtkDataProfileManager.this.log("ACTION_RADIO_TECHNOLOGY_CHANGED: mCurrentOperatorNumeric = " + MtkDataProfileManager.this.mCurrentOperatorNumeric + ", curOperatorNumeric = " + operatorNumeric);
                    if (!TextUtils.isEmpty(MtkDataProfileManager.this.mCurrentOperatorNumeric) && !TextUtils.isEmpty(operatorNumeric) && !TextUtils.equals(MtkDataProfileManager.this.mCurrentOperatorNumeric, operatorNumeric)) {
                        MtkDataProfileManager.this.onSubIdReady();
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
                    String simState = intent.getStringExtra("ss");
                    if ("READY".equals(simState) || "LOADED".equals(simState)) {
                        MtkDataProfileManager.this.registerForFdnEnableChanged();
                        return;
                    } else {
                        MtkDataProfileManager.this.unregisterForFdnEnableChanged();
                        return;
                    }
                }
                if (action.equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    if (MtkDataProfileManager.this.mPhone.getPhoneId() != intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1) || intent.getBooleanExtra("android.telephony.extra.REBROADCAST_ON_UNLOCK", false)) {
                        return;
                    }
                    MtkDataProfileManager.this.registerForFdnEnableChanged();
                    MtkDataProfileManager.this.registerFdnContentObserver();
                    return;
                }
                MtkDataProfileManager.this.log("onReceive: Unknown action=" + action);
            }
        };
        this.mIntentReceiverEx = broadcastReceiver;
        this.mIsFdnChecked = false;
        this.mIsMatchFdnForAllowData = false;
        this.mIsPhbStateChangedIntentRegistered = false;
        this.mIsFdnEnableStateChangedRegistered = false;
        this.mPhbStateChangedIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                MtkDataProfileManager.this.log("onReceive: action=" + action);
                if (action.equals("mediatek.intent.action.PHB_STATE_CHANGED")) {
                    boolean bPhbReady = intent.getBooleanExtra("ready", false);
                    MtkDataProfileManager.this.log("bPhbReady: " + bPhbReady);
                    if (bPhbReady) {
                        MtkDataProfileManager.this.onFdnChanged();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.RADIO_TECHNOLOGY");
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        filter.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
        this.mPhone.getContext().registerReceiver(broadcastReceiver, filter, null, this.mPhone, 2);
        SubscriptionManager subscriptionManagerFrom = SubscriptionManager.from(this.mPhone.getContext());
        this.mSubscriptionManager = subscriptionManagerFrom;
        subscriptionManagerFrom.addOnSubscriptionsChangedListener(dpmOnSubscriptionsChangedListener);
        UiccController.getInstance().registerForIccChanged(this, 101, (Object) null);
        createWorkerHandler();
        this.mSettingsObserver = new SettingsObserver(this.mPhone.getContext(), this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void registerAllEvents() {
        super.registerAllEvents();
        this.mPhone.mCi.registerForPlmnData(this, 102, null);
        this.mPhone.mCi.registerForNotAvailable(this, EVENT_RADIO_NOT_AVAILABLE, (Object) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void handleMessage(Message msg) {
        if (VDBG) {
            log("handleMessage msg=" + msg);
        }
        switch (msg.what) {
            case 101:
                IccRecords newIccRecords = UiccController.getInstance().getIccRecords(this.mPhone.getPhoneId(), 1);
                IccRecords iccRecords = this.mIccRecords;
                if (iccRecords != newIccRecords) {
                    if (iccRecords != null) {
                        log("Removing stale icc objects.");
                        this.mIccRecords.unregisterForRecordsOverride(this);
                        this.mIccRecords = null;
                    }
                    if (newIccRecords != null) {
                        log("new Icc object");
                        this.mIsRecordsOverride = false;
                        newIccRecords.registerForRecordsOverride(this, EVENT_RECORDS_OVERRIDE, (Object) null);
                        this.mIccRecords = newIccRecords;
                        return;
                    }
                    return;
                }
                return;
            case 102:
                log("EVENT_PLMN_DATA");
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar != null && ar.result != null) {
                    PlmnMvnoData tempPlmnMvnoData = (PlmnMvnoData) ar.result;
                    boolean changed = isPlmnMvnoChanged(this.mPlmnMvnoData, tempPlmnMvnoData);
                    this.mPlmnMvnoData = tempPlmnMvnoData;
                    log("mPlmnMvnoData = " + this.mPlmnMvnoData + " changed = " + changed);
                    if (changed && SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
                        onSubIdReady();
                        return;
                    }
                    return;
                }
                return;
            case 103:
                break;
            case EVENT_RECORDS_OVERRIDE /* 104 */:
                log("EVENT_RECORDS_OVERRIDE");
                this.mIsRecordsOverride = DBG;
                return;
            case EVENT_CHECK_FDN_LIST /* 105 */:
            default:
                super.handleMessage(msg);
                return;
            case EVENT_FDN_ENABLE_CHANGED /* 106 */:
                log("EVENT_FDN_ENABLE_CHANGED");
                break;
            case EVENT_RADIO_NOT_AVAILABLE /* 107 */:
                log("EVENT_RADIO_NOT_AVAILABLE");
                reset();
                return;
        }
        onFdnChanged();
    }

    private void createDataProfiles(List<DataProfile> profiles, Cursor cursor) {
        ArrayList<ApnSetting> result;
        if (cursor == null) {
            loge("createDataProfiles: parameter cursor is null");
            return;
        }
        ArrayList<ApnSetting> mnoApns = new ArrayList<>();
        ArrayList<ApnSetting> mvnoApns = new ArrayList<>();
        UiccController.getInstance().getIccRecords(this.mPhone.getPhoneId(), 1);
        boolean isInternetSupportedByMno = false;
        boolean isInternetSupportedByMvno = false;
        if (cursor.moveToFirst()) {
            do {
                ApnSetting apn = ApnSetting.makeApnSetting(cursor);
                if (apn != null) {
                    if (apn.canHandleType(17)) {
                        apn = addVsimApnTypeToDefaultApnSetting(apn);
                    }
                    if (apn.hasMvnoParams()) {
                        if (isMvnoMatches(apn.getMvnoType(), apn.getMvnoMatchData())) {
                            mvnoApns.add(apn);
                            isInternetSupportedByMvno |= apn.canHandleType(17);
                        }
                    } else {
                        mnoApns.add(apn);
                        isInternetSupportedByMno |= apn.canHandleType(17);
                    }
                    if (this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
                        checkApnSetting(apn);
                    }
                }
            } while (cursor.moveToNext());
        }
        if (mvnoApns.isEmpty()) {
            result = mnoApns;
            if (!isInternetSupportedByMno && !mnoApns.isEmpty() && this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
                reportAnomaly("Carrier doesn't support internet.", "9af73e18-b523-4dc5-adab-363eb6613305");
            }
        } else {
            result = mvnoApns;
            if (!isInternetSupportedByMvno && this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
                reportAnomaly("Carrier(mvno) doesn't support internet.", "9af73e18-b523-4dc5-adab-363eb6613305");
            }
        }
        log("createApnList: X result=" + result);
        for (ApnSetting apn2 : result) {
            if (apn2 != null) {
                apn2.setPersistent(DBG);
                DataProfile dataProfile = new DataProfile.Builder().setApnSetting(apn2).setTrafficDescriptor(new TrafficDescriptor(apn2.getApnName(), null)).setPreferred(false).build();
                profiles.add(dataProfile);
                log("Added " + dataProfile);
            }
        }
    }

    private ApnSetting addVsimApnTypeToDefaultApnSetting(ApnSetting apn) {
        if (!ExternalSimManager.isNonDsdaRemoteSimSupport()) {
            return apn;
        }
        return new ApnSetting.Builder().setId(apn.getId()).setOperatorNumeric(apn.getOperatorNumeric()).setEntryName(apn.getEntryName()).setApnName(apn.getApnName()).setProxyAddress(apn.getProxyAddressAsString()).setProxyPort(apn.getProxyPort()).setMmsc(apn.getMmsc()).setMmsProxyAddress(apn.getMmsProxyAddressAsString()).setMmsProxyPort(apn.getMmsProxyPort()).setUser(apn.getUser()).setPassword(apn.getPassword()).setAuthType(apn.getAuthType()).setApnTypeBitmask(apn.getApnTypeBitmask() | 4096).setProtocol(apn.getProtocol()).setRoamingProtocol(apn.getRoamingProtocol()).setCarrierEnabled(apn.isEnabled()).setNetworkTypeBitmask(apn.getNetworkTypeBitmask()).setLingeringNetworkTypeBitmask(apn.getLingeringNetworkTypeBitmask()).setProfileId(apn.getProfileId()).setModemCognitive(apn.isPersistent()).setMaxConns(apn.getMaxConns()).setWaitTime(apn.getWaitTime()).setMaxConnsTime(apn.getMaxConnsTime()).setMtuV4(apn.getMtuV4()).setMtuV6(apn.getMtuV6()).setMvnoType(apn.getMvnoType()).setMvnoMatchData(apn.getMvnoMatchData()).setApnSetId(apn.getApnSetId()).setCarrierId(apn.getCarrierId()).setSkip464Xlat(apn.getSkip464Xlat()).setAlwaysOn(apn.isAlwaysOn()).buildWithoutCheck();
    }

    protected void updateDataProfiles(boolean forceUpdateIa) {
        DataProfile dataProfile;
        String operator = null;
        ArrayList arrayList = new ArrayList();
        if (!mEnableDataEnhance) {
            if (this.mDataConfigManager.isConfigCarrierSpecific()) {
                Cursor cursor = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.SIM_APN_URI, "filtered/subId/" + this.mPhone.getSubId()), null, null, null, "_id");
                if (cursor == null) {
                    loge("Cannot access APN database through telephony provider.");
                    return;
                }
                boolean isInternetSupported = false;
                while (cursor.moveToNext()) {
                    ApnSetting apn = ApnSetting.makeApnSetting(cursor);
                    if (apn != null) {
                        apn.setPersistent(DBG);
                        DataProfile dataProfile2 = new DataProfile.Builder().setApnSetting(apn).setTrafficDescriptor(new TrafficDescriptor(apn.getApnName(), null)).setPreferred(false).build();
                        arrayList.add(dataProfile2);
                        log("Added " + dataProfile2);
                        isInternetSupported |= apn.canHandleType(17);
                        if (this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
                            checkApnSetting(apn);
                        }
                    }
                }
                cursor.close();
                if (!isInternetSupported && !arrayList.isEmpty() && this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
                    reportAnomaly("Carrier doesn't support internet.", "9af73e18-b523-4dc5-adab-363eb6613305");
                }
            }
        } else {
            operator = mtkGetOperatorNumeric();
            if (!SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId()) || TextUtils.isEmpty(operator)) {
                log("updateDataProfiles: ignore, sim not ready or no operator numeric");
                return;
            }
            if (operator != null) {
                String selection = "numeric = '" + operator + "'";
                log("updateDataProfiles: selection=" + selection);
                Cursor cursor2 = this.mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "filtered"), null, selection, null, "_id");
                if (cursor2 != null) {
                    if (cursor2.getCount() > 0) {
                        createDataProfiles(arrayList, cursor2);
                    }
                    cursor2.close();
                } else {
                    log("updateDataProfiles: cursor is null");
                }
            }
        }
        DataProfile dataProfile3 = arrayList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(10);
            }
        }).findFirst().orElse(null);
        if (dataProfile3 == null) {
            arrayList.add(new DataProfile.Builder().setApnSetting(buildDefaultApnSetting("DEFAULT EIMS", "", 512)).setTrafficDescriptor(new TrafficDescriptor("", null)).build());
            log("Added default EIMS data profile.");
        }
        DataProfile dataProfile4 = arrayList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(4);
            }
        }).findFirst().orElse(null);
        if (dataProfile4 == null) {
            arrayList.add(new DataProfile.Builder().setApnSetting(buildDefaultApnSetting("DEFAULT IMS", "ims", 64)).setTrafficDescriptor(new TrafficDescriptor("ims", null)).build());
            log("Added default IMS data profile.");
        }
        DataProfile dataProfile5 = arrayList.stream().filter(new Predicate() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((DataProfile) obj).canSatisfy(29);
            }
        }).findFirst().orElse(null);
        if (dataProfile5 == null && (dataProfile = getEnterpriseDataProfile()) != null) {
            arrayList.add(dataProfile);
            log("Added enterprise profile " + dataProfile);
        }
        dedupeDataProfiles(arrayList);
        if (this.mDataConfigManager.isApnConfigAnomalyReportEnabled()) {
            checkDataProfiles(arrayList);
        }
        log("Found " + arrayList.size() + " data profiles. profiles = " + arrayList);
        boolean profilesChanged = false;
        if (this.mAllDataProfiles.size() != arrayList.size() || !this.mAllDataProfiles.containsAll(arrayList)) {
            log("Data profiles changed.");
            this.mAllDataProfiles.clear();
            this.mAllDataProfiles.addAll(arrayList);
            profilesChanged = DBG;
        }
        boolean profilesChanged2 = profilesChanged | updatePreferredDataProfile();
        int setId = getPreferredDataProfileSetId();
        if (setId != this.mPreferredDataProfileSetId) {
            logl("Changed preferred data profile set id to " + setId);
            this.mPreferredDataProfileSetId = setId;
            profilesChanged2 = DBG;
        }
        updateDataProfilesAtModem();
        updateInitialAttachDataProfileAtModem(forceUpdateIa);
        if (profilesChanged2) {
            this.mDataProfileManagerCallbacks.forEach(new Consumer() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    MtkDataProfileManager.lambda$updateDataProfiles$3((DataProfileManager.DataProfileManagerCallback) obj);
                }
            });
        }
        this.mCurrentOperatorNumeric = operator;
    }

    static /* synthetic */ void lambda$updateDataProfiles$3(final DataProfileManager.DataProfileManagerCallback callback) {
        Objects.requireNonNull(callback);
        callback.invokeFromExecutor(new Runnable() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                callback.onDataProfilesChanged();
            }
        });
    }

    protected ApnSetting buildDefaultApnSetting(String entry, String apn, int apnTypeBitmask) {
        return new ApnSetting.Builder().setEntryName(entry).setProtocol(2).setRoamingProtocol(2).setApnName(apn).setApnTypeBitmask(apnTypeBitmask).setCarrierEnabled(DBG).setApnSetId(-1).buildWithoutCheck();
    }

    private boolean isPlmnMvnoChanged(PlmnMvnoData oldValue, PlmnMvnoData newValue) {
        if (oldValue != null && newValue != null && TextUtils.equals(oldValue.getGsmNumeric(), newValue.getGsmNumeric()) && TextUtils.equals(oldValue.getCdmaNumeric(), newValue.getCdmaNumeric()) && TextUtils.equals(oldValue.getGsmSpn(), newValue.getGsmSpn()) && TextUtils.equals(oldValue.getCdmaSpn(), newValue.getCdmaSpn()) && TextUtils.equals(oldValue.getGsmImsi(), newValue.getGsmImsi()) && TextUtils.equals(oldValue.getCdmaImsi(), newValue.getCdmaImsi()) && TextUtils.equals(oldValue.getGid1(), newValue.getGid1()) && TextUtils.equals(oldValue.getImpi(), newValue.getImpi())) {
            return false;
        }
        return DBG;
    }

    private boolean updatePhoneType() {
        int tempPhoneType = this.mPhoneType;
        this.mPhoneType = 1;
        if (MtkDataHelper.isCdma3GCard(this.mPhone.getPhoneId())) {
            this.mPhoneType = 2;
        }
        if (MtkDataHelper.isCdma3GDualModeCard(this.mPhone.getPhoneId())) {
            this.mPhoneType = this.mPhone.getPhoneType();
        }
        if (tempPhoneType != this.mPhoneType) {
            return DBG;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String mtkGetOperatorNumeric() {
        String operatorNumeric;
        if (!mEnableDataEnhance) {
            return "";
        }
        String aospNumeric = this.mPhone.getOperatorNumeric();
        if (this.mDataConfigManager.isConfigCarrierSpecific() && !TextUtils.isEmpty(aospNumeric)) {
            log("mtkGetOperatorNumeric: carrier config is ready return " + aospNumeric);
            return aospNumeric;
        }
        if (this.mPhoneType == 2) {
            PlmnMvnoData plmnMvnoData = this.mPlmnMvnoData;
            operatorNumeric = plmnMvnoData != null ? plmnMvnoData.getCdmaNumeric() : "";
        } else {
            PlmnMvnoData plmnMvnoData2 = this.mPlmnMvnoData;
            operatorNumeric = plmnMvnoData2 != null ? plmnMvnoData2.getGsmNumeric() : "";
        }
        log("mtkGetOperatorNumeric: phone type = " + this.mPhoneType + " [1:GSM,2:CDMA], operator from phone = " + aospNumeric + ", operator from RIL = " + operatorNumeric);
        if (TextUtils.isEmpty(operatorNumeric) && !TextUtils.isEmpty(aospNumeric)) {
            log("mtkGetOperatorNumeric: maybe wrong phone type, update it");
            if (updatePhoneType()) {
                if (this.mPhoneType == 2) {
                    PlmnMvnoData plmnMvnoData3 = this.mPlmnMvnoData;
                    String operatorNumeric2 = plmnMvnoData3 != null ? plmnMvnoData3.getCdmaNumeric() : "";
                    return operatorNumeric2;
                }
                PlmnMvnoData plmnMvnoData4 = this.mPlmnMvnoData;
                String operatorNumeric3 = plmnMvnoData4 != null ? plmnMvnoData4.getGsmNumeric() : "";
                return operatorNumeric3;
            }
            return operatorNumeric;
        }
        return operatorNumeric;
    }

    public String getImsi() {
        PlmnMvnoData plmnMvnoData;
        if (this.mIsRecordsOverride || (plmnMvnoData = this.mPlmnMvnoData) == null) {
            return null;
        }
        if (this.mPhoneType == 1) {
            return plmnMvnoData.getGsmImsi();
        }
        return plmnMvnoData.getCdmaImsi();
    }

    public String getIccid() {
        MtkUiccController ctrl = (MtkUiccController) UiccController.getInstance();
        return ctrl != null ? ctrl.getIccid(this.mPhone.getPhoneId()) : "";
    }

    private boolean isMvnoMatches(int mvnoType, String mvnoMatchData) {
        String strHexSpn;
        String strImsi;
        String strSpn;
        log("mvnoMatchData=" + mvnoMatchData);
        strHexSpn = "";
        if (mvnoType == 0) {
            if (this.mPhoneType == 1) {
                PlmnMvnoData plmnMvnoData = this.mPlmnMvnoData;
                if (plmnMvnoData != null) {
                    strHexSpn = plmnMvnoData.getGsmSpn();
                }
            } else {
                PlmnMvnoData plmnMvnoData2 = this.mPlmnMvnoData;
                if (plmnMvnoData2 != null) {
                    strHexSpn = plmnMvnoData2.getCdmaSpn();
                }
            }
            if (strHexSpn.length() == 0) {
                return false;
            }
            if (this.mPhoneType == 1) {
                strSpn = MtkIccUtilsEx.parseSpnToString(1, IccUtils.hexStringToBytes(strHexSpn));
            } else {
                strSpn = MtkIccUtilsEx.parseSpnToString(2, IccUtils.hexStringToBytes(strHexSpn));
            }
            log("strSpn=" + strSpn);
            if (strSpn != null && strSpn.equalsIgnoreCase(mvnoMatchData)) {
                return DBG;
            }
        } else if (mvnoType != 1) {
            if (mvnoType == 2) {
                PlmnMvnoData plmnMvnoData3 = this.mPlmnMvnoData;
                strHexSpn = plmnMvnoData3 != null ? plmnMvnoData3.getGid1() : "";
                String gid1 = strHexSpn;
                log("gid1=" + gid1);
                int mvno_match_data_length = mvnoMatchData.length();
                if (gid1 != null && gid1.length() >= mvno_match_data_length && gid1.substring(0, mvno_match_data_length).equalsIgnoreCase(mvnoMatchData)) {
                    return DBG;
                }
            } else if (mvnoType == 3 && MtkDataHelper.iccidMatches(mvnoMatchData, getIccid())) {
                return DBG;
            }
        } else {
            if (this.mPhoneType == 1) {
                PlmnMvnoData plmnMvnoData4 = this.mPlmnMvnoData;
                strHexSpn = plmnMvnoData4 != null ? plmnMvnoData4.getGsmImsi() : "";
                strImsi = strHexSpn;
            } else {
                PlmnMvnoData plmnMvnoData5 = this.mPlmnMvnoData;
                strHexSpn = plmnMvnoData5 != null ? plmnMvnoData5.getCdmaImsi() : "";
                strImsi = strHexSpn;
            }
            if (strImsi != null && MtkDataHelper.imsiMatches(mvnoMatchData, strImsi)) {
                return DBG;
            }
        }
        return false;
    }

    private void getMtuFromResource() {
        String operator = mtkGetOperatorNumeric();
        int mcc = 0;
        int mnc = 0;
        if (operator != null && operator.length() > 3) {
            try {
                mcc = Integer.parseInt(operator.substring(0, 3));
                mnc = Integer.parseInt(operator.substring(3, operator.length()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                loge("operator numeric is invalid");
            }
        }
        new Configuration();
        Configuration configuration = this.mPhone.getContext().getResources().getConfiguration();
        configuration.mcc = mcc;
        configuration.mnc = mnc == 0 ? 65535 : mnc;
        Context resc = this.mPhone.getContext().createConfigurationContext(configuration);
        Resources resource = resc.getResources();
        if (resource != null) {
            this.mMtuFromResource = resource.getInteger(R.integer.config_letterboxActivityCornersRadius);
            log("getMtuFromResource: mcc = " + mcc + ", mnc = " + mnc + ", mtu = " + this.mMtuFromResource);
        }
    }

    public int getMtu() {
        return this.mMtuFromResource;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubIdReady() {
        updatePhoneType();
        log("onSubIdReady mPhoneType = " + this.mPhoneType + " [1:GSM,2:CDMA]");
        String operatorNumeric = mtkGetOperatorNumeric();
        if (TextUtils.isEmpty(operatorNumeric)) {
            log("onSubIdReady: empty operator numeric, return");
            return;
        }
        if (MtkDataHelper.isCdma3GDualModeCard(this.mPhone.getPhoneId())) {
            MtkDataNetworkController mtkDnc = (MtkDataNetworkController) this.mPhone.getDataNetworkController();
            if (mtkDnc == null) {
                loge("onSubIdReady: MtkDataNetworkController is null, return");
                return;
            }
            boolean isAnyConnected = mtkDnc.isAnyDataNetworkConnected();
            if (isAnyConnected && this.mAllDataProfiles.size() != 0) {
                DataProfile df = (DataProfile) this.mAllDataProfiles.get(0);
                String numeric = df.getApnSetting().getOperatorNumeric();
                if (numeric.length() > 0 && !numeric.equals(operatorNumeric)) {
                    log("CDMA 3G dual mode card numeric change, clean up.");
                    mtkDnc.tearDownAllDataNetworks(2);
                }
            }
        }
        this.mFdnConstraint = false;
        checkFdnState();
        getMtuFromResource();
        updateDataProfiles(DBG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void registerForFdnEnableChanged() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("registerForFdnEnableChanged(), fdn config disabled, skip register");
        } else {
            if (this.mIsFdnEnableStateChangedRegistered) {
                log("registerForFdnEnableChanged(), already registered, skip register");
                return;
            }
            log("registerForFdnEnableChanged(), register for FDN enable changed");
            this.mPhone.getIccCard().registerForFdnChanged(this, EVENT_FDN_ENABLE_CHANGED, (Object) null);
            this.mIsFdnEnableStateChangedRegistered = DBG;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void unregisterForFdnEnableChanged() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("unregisterForFdnEnableChanged(), fdn config disabled, skip unregister");
        } else {
            if (!this.mIsFdnEnableStateChangedRegistered) {
                log("unregisterForFdnEnableChanged(), already unregistered, skip unregister");
                return;
            }
            log("unregisterForFdnEnableChanged(), unregister for FDN enable changed");
            this.mPhone.getIccCard().unregisterForFdnChanged(this);
            this.mIsFdnEnableStateChangedRegistered = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerFdnContentObserver() {
        Uri fdnContentUri;
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("registerFdnContentObserver(), fdn config disabled, skip register");
            return;
        }
        log("registerFdnContentObserver(), register for FDN content observer");
        if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
            fdnContentUri = Uri.parse(FDN_CONTENT_URI_WITH_SUB_ID + this.mPhone.getSubId());
        } else {
            fdnContentUri = Uri.parse(FDN_CONTENT_URI);
        }
        this.mSettingsObserver.observe(fdnContentUri, 103);
    }

    private void checkFdnState() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            return;
        }
        IMtkTelephonyEx telephonyEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
        if (telephonyEx == null) {
            loge("checkFdnState(), get telephonyEx failed!!");
            return;
        }
        try {
            boolean bFdnEnabled = telephonyEx.isFdnEnabled(this.mPhone.getSubId());
            log("checkFdnState(), bFdnEnabled = " + bFdnEnabled);
            if (bFdnEnabled) {
                if (this.mIsFdnChecked) {
                    log("checkFdnState(), match FDN for allow data = " + this.mIsMatchFdnForAllowData);
                    return;
                }
                boolean bPhbReady = telephonyEx.isPhbReady(this.mPhone.getSubId());
                log("checkFdnState(), bPhbReady = " + bPhbReady);
                if (bPhbReady) {
                    this.mWorkerHandler.sendMessage(obtainMessage(EVENT_CHECK_FDN_LIST, Boolean.valueOf(bFdnEnabled)));
                } else if (!this.mIsPhbStateChangedIntentRegistered) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
                    this.mPhone.getContext().registerReceiver(this.mPhbStateChangedIntentReceiver, filter, 2);
                    this.mIsPhbStateChangedIntentRegistered = DBG;
                }
                return;
            }
            if (this.mIsPhbStateChangedIntentRegistered) {
                this.mIsPhbStateChangedIntentRegistered = false;
                this.mPhone.getContext().unregisterReceiver(this.mPhbStateChangedIntentReceiver);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFdnChanged() {
        if (!((MtkDataConfigManager) this.mDataConfigManager).getDataFdnSupported()) {
            log("onFdnChanged(), fdn config disabled, skip onFdnChanged");
            return;
        }
        log("onFdnChanged()");
        boolean bFdnEnabled = false;
        boolean bPhbReady = false;
        IMtkTelephonyEx telephonyEx = IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
        if (telephonyEx != null) {
            try {
                bFdnEnabled = telephonyEx.isFdnEnabled(this.mPhone.getSubId());
                bPhbReady = telephonyEx.isPhbReady(this.mPhone.getSubId());
                log("onFdnChanged(), bFdnEnabled = " + bFdnEnabled + ", bPhbReady = " + bPhbReady);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } else {
            loge("onFdnChanged(), get telephonyEx failed!!");
        }
        if (bPhbReady) {
            this.mWorkerHandler.sendMessage(obtainMessage(EVENT_CHECK_FDN_LIST, Boolean.valueOf(bFdnEnabled)));
        } else if (!this.mIsPhbStateChangedIntentRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
            this.mPhone.getContext().registerReceiver(this.mPhbStateChangedIntentReceiver, filter, 2);
            this.mIsPhbStateChangedIntentRegistered = DBG;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanOrSetupDataConnByCheckFdn(boolean bFdnEnabled) {
        Uri uriFdn;
        log("cleanOrSetupDataConnByCheckFdn(), bFdnEnabled:" + bFdnEnabled);
        if (bFdnEnabled) {
            if (SubscriptionManager.isValidSubscriptionId(this.mPhone.getSubId())) {
                uriFdn = Uri.parse(FDN_CONTENT_URI_WITH_SUB_ID + this.mPhone.getSubId());
            } else {
                uriFdn = Uri.parse(FDN_CONTENT_URI);
            }
            ContentResolver cr = this.mPhone.getContext().getContentResolver();
            Cursor cursor = cr.query(uriFdn, new String[]{"number"}, null, null, null);
            this.mIsMatchFdnForAllowData = false;
            if (cursor != null) {
                this.mIsFdnChecked = DBG;
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    while (true) {
                        String strFdnNumber = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                        log("strFdnNumber = " + strFdnNumber);
                        if (strFdnNumber.equals(FDN_FOR_ALLOW_DATA)) {
                            this.mIsMatchFdnForAllowData = DBG;
                            break;
                        } else if (!cursor.moveToNext()) {
                            break;
                        }
                    }
                }
                cursor.close();
            }
            this.mFdnConstraint = true ^ this.mIsMatchFdnForAllowData;
        } else {
            this.mFdnConstraint = false;
        }
        ((MtkDataNetworkController) this.mDataNetworkController).fdnStateChanged();
    }

    protected boolean isUseDataService() {
        return false;
    }

    protected void mtkOnDataServiceBound(int transport) {
        if (transport == 1) {
            log("mtkOnDataServiceBound(), update data profiles");
            updateDataProfiles(DBG);
        }
    }

    public boolean isApnLoaded() {
        if (this.mCurrentOperatorNumeric != null) {
            return DBG;
        }
        return false;
    }

    public boolean isFdnConstraint() {
        return this.mFdnConstraint;
    }

    public void reset() {
        this.mAllDataProfiles.clear();
        this.mPreferredDataProfile = null;
        this.mPreferredDataProfileSetId = 0;
        this.mPlmnMvnoData = null;
        this.mMtuFromResource = 0;
        this.mPhoneType = 0;
        this.mCurrentOperatorNumeric = null;
        this.mInitialAttachDataProfile = null;
    }

    private void createWorkerHandler() {
        if (this.mWorkerHandler == null) {
            Thread thread = new Thread() { // from class: com.mediatek.internal.telephony.data.MtkDataProfileManager.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    Looper.prepare();
                    MtkDataProfileManager.this.mWorkerHandler = new WorkerHandler();
                    Looper.loop();
                }
            };
            thread.start();
        }
    }

    private class WorkerHandler extends Handler {
        private WorkerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MtkDataProfileManager.EVENT_CHECK_FDN_LIST /* 105 */:
                    boolean bFdnEnabled = ((Boolean) msg.obj).booleanValue();
                    MtkDataProfileManager.this.cleanOrSetupDataConnByCheckFdn(bFdnEnabled);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d(LOG_TAG, this.mLogTag + ": " + s);
    }

    private void loge(String s) {
        Rlog.e(LOG_TAG, this.mLogTag + ": " + s);
    }
}
