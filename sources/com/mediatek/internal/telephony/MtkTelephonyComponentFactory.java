package com.mediatek.internal.telephony;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.telephony.data.DataProfile;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DefaultPhoneNotifier;
import com.android.internal.telephony.DeviceStateMonitor;
import com.android.internal.telephony.DisplayInfoController;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.ImsSmsDispatcher;
import com.android.internal.telephony.InboundSmsTracker;
import com.android.internal.telephony.MultiSimSettingController;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.SignalStrengthController;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.SmsUsageMonitor;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.WapPushOverSms;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cat.CommandParamsFactory;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.data.DataConfigManager;
import com.android.internal.telephony.data.DataEvaluation;
import com.android.internal.telephony.data.DataNetwork;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.data.DataProfileManager;
import com.android.internal.telephony.data.DataServiceManager;
import com.android.internal.telephony.data.PhoneSwitcher;
import com.android.internal.telephony.data.TelephonyNetworkFactory;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCallTracker;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import com.mediatek.internal.telephony.carrierexpress.CarrierExpressFwkHandler;
import com.mediatek.internal.telephony.cat.MtkCatLog;
import com.mediatek.internal.telephony.cat.MtkCatService;
import com.mediatek.internal.telephony.cat.MtkCommandParamsFactory;
import com.mediatek.internal.telephony.cat.MtkRilMessageDecoder;
import com.mediatek.internal.telephony.cdma.MtkCdmaInboundSmsHandler;
import com.mediatek.internal.telephony.cdma.MtkCdmaSMSDispatcher;
import com.mediatek.internal.telephony.cdma.MtkCdmaSubscriptionSourceManager;
import com.mediatek.internal.telephony.data.MtkDataConfigManager;
import com.mediatek.internal.telephony.data.MtkDataHelper;
import com.mediatek.internal.telephony.data.MtkDataNetwork;
import com.mediatek.internal.telephony.data.MtkDataNetworkController;
import com.mediatek.internal.telephony.data.MtkDataProfileManager;
import com.mediatek.internal.telephony.data.MtkPhoneSwitcher;
import com.mediatek.internal.telephony.data.MtkTelephonyNetworkFactory;
import com.mediatek.internal.telephony.datasub.SmartDataSwitchAssistant;
import com.mediatek.internal.telephony.gsm.MtkGsmInboundSmsHandler;
import com.mediatek.internal.telephony.gsm.MtkGsmSMSDispatcher;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import com.mediatek.internal.telephony.imsphone.MtkImsPhoneCallTracker;
import com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import com.mediatek.internal.telephony.uicc.MtkUiccProfile;
import com.mediatek.internal.telephony.uicc.MtkUiccSlot;
import com.mediatek.internal.telephony.worldphone.WorldPhoneUtil;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class MtkTelephonyComponentFactory extends TelephonyComponentFactory {
    private static final String LOG_TAG = "MtkTelephonyComponentFactory";
    private static MtkTelephonyComponentFactory sInstance;

    public static MtkTelephonyComponentFactory getInstance() {
        if (sInstance == null) {
            sInstance = new MtkTelephonyComponentFactory();
        }
        return sInstance;
    }

    /* JADX INFO: renamed from: makePhone, reason: merged with bridge method [inline-methods] */
    public GsmCdmaPhone m81makePhone(Context context, CommandsInterface ci, PhoneNotifier notifier, int phoneId, int precisePhoneType, TelephonyComponentFactory telephonyComponentFactory) {
        return new MtkGsmCdmaPhone(context, ci, notifier, phoneId, precisePhoneType, telephonyComponentFactory);
    }

    public RIL makeRil(Context context, int preferredNetworkType, int cdmaSubscription, Integer instanceId) {
        return new MtkRIL(context, preferredNetworkType, cdmaSubscription, instanceId);
    }

    public ServiceStateTracker makeServiceStateTracker(GsmCdmaPhone phone, CommandsInterface ci) {
        return new MtkServiceStateTracker(phone, ci);
    }

    public MultiSimSettingController initMultiSimSettingController(Context c) {
        return MtkMultiSimSettingController.init(c);
    }

    public GsmCdmaCallTracker makeGsmCdmaCallTracker(GsmCdmaPhone phone) {
        return new MtkGsmCdmaCallTracker(phone);
    }

    public SubscriptionManagerService makeSubscriptionManagerService(Context context, Looper looper) {
        return new MtkSubscriptionManagerService(context, looper);
    }

    public CdmaSubscriptionSourceManager makeCdmaSubscriptionSourceManager(Context context, CommandsInterface ci, Handler h, int what, Object obj) {
        return new MtkCdmaSubscriptionSourceManager(context, ci);
    }

    public DefaultPhoneNotifier makeDefaultPhoneNotifier(Context context) {
        Rlog.d(LOG_TAG, "makeDefaultPhoneNotifier mtk");
        return new MtkPhoneNotifier(context);
    }

    public UiccController makeUiccController(Context c) {
        Rlog.d(LOG_TAG, "makeUiccController mtk");
        return new MtkUiccController(c);
    }

    public UiccProfile makeUiccProfile(Context context, CommandsInterface ci, IccCardStatus ics, int phoneId, UiccCard uiccCard, Object lock) {
        return new MtkUiccProfile(context, ci, ics, phoneId, uiccCard, lock);
    }

    public UiccSlot makeUiccSlot(Context context, boolean isActive) {
        return new MtkUiccSlot(context, isActive);
    }

    public void initRadioManager(Context context, int numPhones, CommandsInterface[] sCommandsInterfaces) {
        RadioManager.init(context, numPhones, sCommandsInterfaces);
    }

    public CatService makeCatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir, Context context, IccFileHandler fh, UiccProfile uiccProfile, int slotId, Looper looper) {
        int phoneType;
        UiccCardApplication ca2;
        if (slotId == -1) {
            phoneType = 1;
        } else {
            int phoneType2 = TelephonyManager.getDefault().getCurrentPhoneTypeForSlot(slotId);
            MtkCatLog.d("MtkCatService", "makeCatService phoneType : " + phoneType2 + " slotId: " + slotId);
            phoneType = phoneType2;
        }
        if (uiccProfile == null) {
            ca2 = ca;
        } else if (phoneType != 2) {
            ca2 = uiccProfile.getApplicationIndex(0);
        } else {
            ca2 = uiccProfile.getApplication(2);
        }
        MtkCatLog.d("MtkCatService", "makeCatService  ca = " + ca2);
        if (ci == null || ca2 == null || ir == null || context == null || fh == null || uiccProfile == null) {
            MtkCatLog.e("MtkCatService", "makeCatService exception, will not create MtkCatservice!!!!");
            return null;
        }
        return new MtkCatService(ci, ca2, ir, context, fh, uiccProfile, slotId, looper);
    }

    public RilMessageDecoder makeRilMessageDecoder(Handler caller, IccFileHandler fh, Context context, int slotId) {
        return new MtkRilMessageDecoder(caller, fh, context, slotId);
    }

    public CommandParamsFactory makeCommandParamsFactory(RilMessageDecoder caller, IccFileHandler fh, Context context) {
        return new MtkCommandParamsFactory(caller, fh, context);
    }

    public DisplayInfoController makeDisplayInfoController(Phone phone) {
        return new MtkDisplayInfoController(phone);
    }

    public TelephonyNetworkFactory makeTelephonyNetworkFactories(Looper looper, Phone phone) {
        return new MtkTelephonyNetworkFactory(looper, phone);
    }

    public void makeSmartDataSwitchAssistant(Context context, Phone[] phones) {
        SmartDataSwitchAssistant.makeSmartDataSwitchAssistant(context, phones);
    }

    public void makeSuppServManager(Context context, Phone[] phones) {
        MtkSuppServManager ssManager = MtkSuppServManager.makeSuppServManager(context, phones);
        ssManager.init();
    }

    public PhoneSwitcher makePhoneSwitcher(int maxDataAttachModemCount, Context context, Looper looper) {
        return MtkPhoneSwitcher.mtkMake(maxDataAttachModemCount, context, looper);
    }

    public SmsStorageMonitor makeSmsStorageMonitor(Phone phone) {
        return new MtkSmsStorageMonitor(phone);
    }

    public SmsUsageMonitor makeSmsUsageMonitor(Context context) {
        return new MtkSmsUsageMonitor(context);
    }

    public IccSmsInterfaceManager makeIccSmsInterfaceManager(Phone phone) {
        return new MtkIccSmsInterfaceManager(phone);
    }

    public ImsSmsDispatcher makeImsSmsDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        return new MtkImsSmsDispatcher(phone, smsDispatchersController);
    }

    public GsmSMSDispatcher makeGsmSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController, GsmInboundSmsHandler gsmInboundSmsHandler) {
        return new MtkGsmSMSDispatcher(phone, smsDispatchersController, gsmInboundSmsHandler);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, byte[] pdu, long timestamp, int destPort, boolean is3gpp2, boolean is3gpp2WapPdu, String address, String displayAddr, String messageBody, boolean isClass0, int subId, int smsSource) {
        return new MtkInboundSmsTracker(context, pdu, timestamp, destPort, is3gpp2, is3gpp2WapPdu, address, displayAddr, messageBody, isClass0, subId, smsSource);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, byte[] pdu, long timestamp, int destPort, boolean is3gpp2, String address, String displayAddr, int referenceNumber, int sequenceNumber, int messageCount, boolean is3gpp2WapPdu, String messageBody, boolean isClass0, int subId, int smsSource) {
        return new MtkInboundSmsTracker(context, pdu, timestamp, destPort, is3gpp2, address, displayAddr, referenceNumber, sequenceNumber, messageCount, is3gpp2WapPdu, messageBody, isClass0, subId, smsSource);
    }

    public InboundSmsTracker makeInboundSmsTracker(Context context, Cursor cursor, boolean isCurrentFormat3gpp2) {
        return new MtkInboundSmsTracker(context, cursor, isCurrentFormat3gpp2);
    }

    public void makeSmsBroadcastUndelivered(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler) {
        MtkSmsBroadcastUndelivered.initialize(context, gsmInboundSmsHandler, cdmaInboundSmsHandler);
    }

    public WapPushOverSms makeWapPushOverSms(Context context) {
        return new MtkWapPushOverSms(context);
    }

    public GsmInboundSmsHandler makeGsmInboundSmsHandler(Context context, SmsStorageMonitor storageMonitor, Phone phone, Looper looper) {
        return MtkGsmInboundSmsHandler.makeInboundSmsHandler(context, storageMonitor, phone, looper);
    }

    public MtkSmsHeader makeSmsHeader() {
        return new MtkSmsHeader();
    }

    public SmsDispatchersController makeSmsDispatchersController(Phone phone, SmsStorageMonitor storageMonitor, SmsUsageMonitor usageMonitor) {
        return new MtkSmsDispatchersController(phone, phone.mSmsStorageMonitor, phone.mSmsUsageMonitor);
    }

    public ProxyController makeProxyController(Context context) {
        return new MtkProxyController(context);
    }

    public CdmaInboundSmsHandler makeCdmaInboundSmsHandler(Context context, SmsStorageMonitor storageMonitor, Phone phone, CdmaSMSDispatcher smsDispatcher, Looper looper) {
        return new MtkCdmaInboundSmsHandler(context, storageMonitor, phone, smsDispatcher, looper);
    }

    public CdmaSMSDispatcher makeCdmaSMSDispatcher(Phone phone, SmsDispatchersController smsDispatchersController) {
        return new MtkCdmaSMSDispatcher(phone, smsDispatchersController);
    }

    public void initEmbmsAdaptor(Context context, CommandsInterface[] sCommandsInterfaces) {
        MtkEmbmsAdaptor.getDefault(context, sCommandsInterfaces);
    }

    public void makeWorldPhoneManager() {
        WorldPhoneUtil.makeWorldPhoneManager();
    }

    public IccPhoneBookInterfaceManager makeIccPhoneBookInterfaceManager(Phone phone) {
        Rlog.d(LOG_TAG, "makeIccPhoneBookInterfaceManager mtk");
        return new MtkIccPhoneBookInterfaceManager(phone);
    }

    public ImsPhoneCallTracker makeImsPhoneCallTracker(ImsPhone imsPhone) {
        return new MtkImsPhoneCallTracker(imsPhone);
    }

    public ImsPhone makeImsPhone(Context context, PhoneNotifier phoneNotifier, Phone defaultPhone) {
        try {
            return new MtkImsPhone(context, phoneNotifier, defaultPhone);
        } catch (Exception e) {
            Rlog.e("TelephonyComponentFactoryEx", "makeImsPhoneExt", e);
            return null;
        }
    }

    public CallManager makeCallManager() {
        return new MtkCallManager();
    }

    public DeviceStateMonitor makeDeviceStateMonitor(Phone phone) {
        return new MtkDeviceStateMonitor(phone);
    }

    public DataNetworkController makeDataNetworkController(Phone phone, Looper looper) {
        return new MtkDataNetworkController(phone, looper);
    }

    public DataNetwork makeDataNetwork(Phone phone, Looper looper, SparseArray<DataServiceManager> dataServiceManagers, DataProfile dataProfile, DataNetworkController.NetworkRequestList networkRequestList, int transport, DataEvaluation.DataAllowedReason dataAllowedReason, DataNetwork.DataNetworkCallback callback) {
        return new MtkDataNetwork(phone, looper, dataServiceManagers, dataProfile, networkRequestList, transport, dataAllowedReason, callback);
    }

    public DataConfigManager makeDataConfigManager(Phone phone, Looper looper) {
        return new MtkDataConfigManager(phone, looper);
    }

    public DataProfileManager makeDataProfileManager(Phone phone, DataNetworkController dataNetworkController, DataServiceManager dataServiceManager, Looper looper, DataProfileManager.DataProfileManagerCallback callback) {
        return new MtkDataProfileManager(phone, dataNetworkController, dataServiceManager, looper, callback);
    }

    public void makeDataHelper(Context context, Phone[] phones) {
        MtkDataHelper.makeMtkDataHelper(context, phones);
    }

    public void initCarrierExpress() {
        Rlog.d(LOG_TAG, "Creating CarrierExpress");
        CarrierExpressFwkHandler.init();
    }

    public void initGwsdService(Context context) {
        if (SystemProperties.get("ro.vendor.mtk_protocol1_rat_config", "").equals("no")) {
            Rlog.d(LOG_TAG, "initGwsdService wifi only project rat = no, return directly!");
            return;
        }
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0") && SystemProperties.get("ro.vendor.mtk_gwsd_support", "0").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            String classPackage = "";
            try {
                if (SystemProperties.get("ro.vendor.mtk_gwsd_capability", "0").equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN)) {
                    classPackage = "/system_ext/framework/mediatek-gwsdv2.jar";
                }
                if (SystemProperties.get("ro.vendor.mtk_gwsd_capability", "0").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
                    classPackage = "/system_ext/framework/mediatek-gwsd.jar";
                }
                PathClassLoader classLoader = new PathClassLoader(classPackage, ClassLoader.getSystemClassLoader());
                Class<?> clazz = Class.forName("com.mediatek.gwsd.service.GwsdService", false, classLoader);
                Rlog.d(LOG_TAG, "class = " + clazz);
                Method method = clazz.getMethod("getInstance", Context.class);
                method.invoke(clazz, context);
            } catch (Exception e) {
                Rlog.e(LOG_TAG, Log.getStackTraceString(e));
            }
        }
    }

    public void initVoDataService(Context context) {
        try {
            PathClassLoader classLoader = new PathClassLoader("/system_ext/framework/mediatek-vodata.jar", ClassLoader.getSystemClassLoader());
            Class<?> clazz = Class.forName("com.mediatek.vodata.service.VoDataService", false, classLoader);
            Rlog.d(LOG_TAG, "class = " + clazz);
            Method method = clazz.getMethod("getInstance", Context.class);
            method.invoke(clazz, context);
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "not found VoData library");
        }
    }

    public SignalStrengthController makeSignalStrengthController(GsmCdmaPhone phone) {
        return new MtkSignalStrengthController(phone);
    }
}
