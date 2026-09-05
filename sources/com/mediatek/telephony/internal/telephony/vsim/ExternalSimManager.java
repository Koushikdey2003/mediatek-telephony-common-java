package com.mediatek.telephony.internal.telephony.vsim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RadioConfig;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.IMtkTelephonyEx;
import com.mediatek.internal.telephony.MtkProxyController;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.RadioManager;
import com.mediatek.internal.telephony.subscription.MtkSubscriptionManagerService;
import com.mediatek.internal.telephony.uicc.MtkUiccController;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class ExternalSimManager {
    private static final int AUTO_RETRY_DURATION = 2000;
    private static final int EVENT_MULTI_SIM_CONFIG_CHANGED = 2;
    private static final int EVENT_VSIM_INDICATION = 1;
    private static final int MAX_VSIM_UICC_CMD_LEN = 269;
    private static final byte NO_RESPONSE_STATUS_WORD_BYTE1 = 0;
    private static final byte NO_RESPONSE_STATUS_WORD_BYTE2 = 0;
    private static final int NO_RESPONSE_TIMEOUT_DURATION = 13000;
    private static final int PLATFORM_READY_CATEGORY_RADIO = 3;
    private static final int PLATFORM_READY_CATEGORY_SIM_SWITCH = 2;
    private static final int PLUG_IN_AUTO_RETRY_TIMEOUT = 40000;
    private static final String PREFERED_AKA_SIM_SLOT = "vendor.gsm.prefered.aka.sim.slot";
    private static final String PREFERED_RSIM_SLOT = "vendor.gsm.prefered.rsim.slot";
    private static final int RECOVERY_TO_REAL_SIM_TIMEOUT = 300000;
    private static final int SET_CAPABILITY_DONE = 2;
    private static final int SET_CAPABILITY_FAILED = 3;
    private static final int SET_CAPABILITY_NONE = 0;
    private static final int SET_CAPABILITY_ONGOING = 1;
    private static final int SIM_STATE_RETRY_DURATION = 20000;
    private static final int SOCKET_OPEN_RETRY_MILLIS = 4000;
    private static final String TAG = "ExternalSimMgr";
    private static final int TRY_RESET_MODEM_DURATION = 2000;
    private CommandsInterface[] mCi;
    private Context mContext;
    private VsimEvenHandler mEventHandler;
    private VsimIndEventHandler mIndHandler;
    private final Object mLock;
    private final Object mLockForEventReq;
    private final BroadcastReceiver mReceiver;
    private Timer mRecoveryTimer;
    private int mSetCapabilityDone;
    private int mUserMainPhoneId;
    private boolean mUserRadioOn;
    private static final boolean ENG = "eng".equals(Build.TYPE);
    private static boolean PLUG_IN_AUTO_RETRY = true;
    private static ExternalSimManager sInstance = null;
    static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};
    private static int sPreferedRsimSlot = -1;
    private static int sPreferedAkaSlot = -1;

    public ExternalSimManager() {
        this.mRecoveryTimer = null;
        this.mContext = null;
        this.mCi = null;
        this.mIndHandler = null;
        this.mEventHandler = null;
        this.mLockForEventReq = new Object();
        this.mLock = new Object();
        this.mSetCapabilityDone = 0;
        this.mUserMainPhoneId = -1;
        this.mUserRadioOn = false;
        this.mReceiver = new BroadcastReceiver() { // from class: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Rlog.d(ExternalSimManager.TAG, "[Receiver]+");
                String action = intent.getAction();
                Rlog.d(ExternalSimManager.TAG, "Action: " + action);
                if (action.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE") && ExternalSimManager.this.mSetCapabilityDone == 1) {
                    if (ExternalSimManager.this.mEventHandler.getVsimSlotId(2) == RadioCapabilitySwitchUtil.getMainCapabilityPhoneId()) {
                        synchronized (ExternalSimManager.this.mLock) {
                            ExternalSimManager.this.mSetCapabilityDone = 2;
                            ExternalSimManager.this.sendCapabilityDoneEvent();
                        }
                        Rlog.d(ExternalSimManager.TAG, "SET_CAPABILITY_DONE, notify all");
                    } else if (!ExternalSimManager.this.mEventHandler.switchModemCapability(ExternalSimManager.sPreferedRsimSlot)) {
                        synchronized (ExternalSimManager.this.mLock) {
                            ExternalSimManager.this.mSetCapabilityDone = 3;
                            ExternalSimManager.this.sendCapabilityDoneEvent();
                        }
                    }
                } else if (action.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED") && ExternalSimManager.this.mSetCapabilityDone == 1) {
                    synchronized (ExternalSimManager.this.mLock) {
                        ExternalSimManager.this.mSetCapabilityDone = 3;
                        ExternalSimManager.this.sendCapabilityDoneEvent();
                    }
                    Rlog.d(ExternalSimManager.TAG, "SET_CAPABILITY_FAILED, notify all");
                }
                Rlog.d(ExternalSimManager.TAG, "[Receiver]-");
            }
        };
        Rlog.d(TAG, "construtor 0 parameter is called - done");
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager$2] */
    private ExternalSimManager(Context context, CommandsInterface[] ci) {
        this.mRecoveryTimer = null;
        this.mContext = null;
        this.mCi = null;
        this.mIndHandler = null;
        this.mEventHandler = null;
        this.mLockForEventReq = new Object();
        this.mLock = new Object();
        this.mSetCapabilityDone = 0;
        this.mUserMainPhoneId = -1;
        this.mUserRadioOn = false;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Rlog.d(ExternalSimManager.TAG, "[Receiver]+");
                String action = intent.getAction();
                Rlog.d(ExternalSimManager.TAG, "Action: " + action);
                if (action.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE") && ExternalSimManager.this.mSetCapabilityDone == 1) {
                    if (ExternalSimManager.this.mEventHandler.getVsimSlotId(2) == RadioCapabilitySwitchUtil.getMainCapabilityPhoneId()) {
                        synchronized (ExternalSimManager.this.mLock) {
                            ExternalSimManager.this.mSetCapabilityDone = 2;
                            ExternalSimManager.this.sendCapabilityDoneEvent();
                        }
                        Rlog.d(ExternalSimManager.TAG, "SET_CAPABILITY_DONE, notify all");
                    } else if (!ExternalSimManager.this.mEventHandler.switchModemCapability(ExternalSimManager.sPreferedRsimSlot)) {
                        synchronized (ExternalSimManager.this.mLock) {
                            ExternalSimManager.this.mSetCapabilityDone = 3;
                            ExternalSimManager.this.sendCapabilityDoneEvent();
                        }
                    }
                } else if (action.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED") && ExternalSimManager.this.mSetCapabilityDone == 1) {
                    synchronized (ExternalSimManager.this.mLock) {
                        ExternalSimManager.this.mSetCapabilityDone = 3;
                        ExternalSimManager.this.sendCapabilityDoneEvent();
                    }
                    Rlog.d(ExternalSimManager.TAG, "SET_CAPABILITY_FAILED, notify all");
                }
                Rlog.d(ExternalSimManager.TAG, "[Receiver]-");
            }
        };
        this.mReceiver = broadcastReceiver;
        Rlog.d(TAG, "construtor 1 parameter is called - start");
        initVsimConfiguration();
        startRecoveryTimer();
        this.mContext = context;
        new Thread() { // from class: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Looper.prepare();
                ExternalSimManager.this.mEventHandler = ExternalSimManager.this.new VsimEvenHandler();
                ExternalSimManager.this.mIndHandler = ExternalSimManager.this.new VsimIndEventHandler();
                ExternalSimManager.this.mCi = PhoneFactory.getCommandsInterfaces();
                for (int i = 0; i < ExternalSimManager.this.mCi.length; i++) {
                    Integer index = new Integer(i);
                    ExternalSimManager.this.mCi[i].registerForVsimIndication(ExternalSimManager.this.mIndHandler, 1, index);
                }
                PhoneConfigurationManager.registerForMultiSimConfigChange(ExternalSimManager.this.mIndHandler, 2, (Object) null);
                Looper.loop();
            }
        }.start();
        new Thread() { // from class: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                while (true) {
                    if (ExternalSimManager.this.mEventHandler == null || ExternalSimManager.this.mIndHandler == null) {
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ExternalSimManager.this.sendExternalSimConnectedEvent(0);
                        ServerTask server = ExternalSimManager.this.new ServerTask();
                        server.listenConnection();
                        return;
                    }
                }
            }
        }.start();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
        intentFilter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
        context.registerReceiver(broadcastReceiver, intentFilter);
        Rlog.d(TAG, "construtor is called - end");
    }

    public static ExternalSimManager make(Context context, CommandsInterface[] ci) {
        if (sInstance == null) {
            sInstance = new ExternalSimManager(context, ci);
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String truncateString(String original) {
        if (original == null || original.length() < 6) {
            return original;
        }
        return original.substring(0, 2) + "***" + original.substring(original.length() - 4);
    }

    private static IMtkTelephonyEx getITelephonyEx() {
        return IMtkTelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCapabilityDoneEvent() {
        VsimEvent event = new VsimEvent(0, ExternalSimConstants.MSG_ID_CAPABILITY_SWITCH_DONE, -1);
        Message msg = new Message();
        msg.obj = event;
        this.mEventHandler.sendMessage(msg);
        Rlog.d(TAG, "sendCapabilityDoneEvent....");
    }

    public boolean initializeService(byte[] userData) {
        Rlog.d(TAG, "initializeService() - start");
        if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) == 0) {
            Rlog.d(TAG, "initializeService() - mtk_external_sim_support didn't support");
            return false;
        }
        SystemProperties.set("ctl.start", "osi");
        Rlog.d(TAG, "initializeService() - end");
        return true;
    }

    public boolean finalizeService(byte[] userData) {
        Rlog.d(TAG, "finalizeService() - start");
        if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) == 0) {
            Rlog.d(TAG, "initializeService() - mtk_external_sim_support didn't support");
            return false;
        }
        SystemProperties.set("ctl.stop", "osi");
        Rlog.d(TAG, "finalizeService() - end");
        return true;
    }

    public void initVsimConfiguration() {
        sPreferedRsimSlot = SystemProperties.getInt(PREFERED_RSIM_SLOT, -1);
        sPreferedAkaSlot = SystemProperties.getInt(PREFERED_AKA_SIM_SLOT, -1);
    }

    public static boolean isNonDsdaRemoteSimSupport() {
        return SystemProperties.getInt("ro.vendor.mtk_non_dsda_rsim_support", 0) == 1;
    }

    public static boolean isSupportVsimHotPlugOut() {
        for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
            TelephonyManager.getDefault();
            String capability = TelephonyManager.getTelephonyProperty(i, "vendor.gsm.modem.vsim.capability", "0");
            if (capability != null && capability.length() > 0 && !"0".equals(capability) && (Integer.parseInt(capability) & 2) > 0) {
                return true;
            }
        }
        return false;
    }

    public static int getPreferedRsimSlot() {
        return sPreferedRsimSlot;
    }

    public static boolean isAnyVsimEnabled() {
        int vsimOnly = SystemProperties.getInt("ro.vendor.mtk_external_sim_only_slots", 0);
        if (vsimOnly != 0) {
            return true;
        }
        for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
            TelephonyManager.getDefault();
            String enable = TelephonyManager.getTelephonyProperty(i, "vendor.gsm.external.sim.enabled", "0");
            TelephonyManager.getDefault();
            String inserted = TelephonyManager.getTelephonyProperty(i, "vendor.gsm.external.sim.inserted", "0");
            if (enable != null && enable.length() > 0 && !"0".equals(enable) && inserted != null && inserted.length() > 0 && !"0".equals(inserted)) {
                return true;
            }
        }
        return false;
    }

    public void startRecoveryTimer() {
        for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
            TelephonyManager.getDefault();
            String persist = TelephonyManager.getTelephonyProperty(i, "persist.vendor.radio.external.sim", "0");
            if (persist != null && persist.length() > 0 && String.valueOf(2).equals(persist)) {
                if (this.mRecoveryTimer == null) {
                    this.mRecoveryTimer = new Timer();
                    int timout = RECOVERY_TO_REAL_SIM_TIMEOUT;
                    TelephonyManager.getDefault();
                    String userTimeout = TelephonyManager.getTelephonyProperty(i, "persist.vendor.radio.vsim.timeout", String.valueOf(RECOVERY_TO_REAL_SIM_TIMEOUT));
                    try {
                        if (Integer.parseInt(userTimeout) > 0) {
                            timout = Integer.parseInt(userTimeout) * 1000;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    this.mRecoveryTimer.schedule(new RecoveryRealSimTask(), timout);
                    Rlog.i(TAG, "startRecoveryTimer: " + timout + " ms.");
                    return;
                }
                return;
            }
        }
        Rlog.i(TAG, "No need to startRecoveryTimer since didn't set persist VSIM.");
    }

    public void stopRecoveryTimer() {
        Timer timer = this.mRecoveryTimer;
        if (timer != null) {
            timer.cancel();
            this.mRecoveryTimer.purge();
            this.mRecoveryTimer = null;
            Rlog.i(TAG, "stopRecoveryTimer.");
        }
    }

    public class RecoveryRealSimTask extends TimerTask {
        public RecoveryRealSimTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
                TelephonyManager.getDefault();
                String enabled = TelephonyManager.getTelephonyProperty(i, "vendor.gsm.external.sim.enabled", "0");
                if (enabled != null && enabled.length() > 0 && !"0".equals(enabled)) {
                    Rlog.i(ExternalSimManager.TAG, "Auto recovery time out, disable VSIM...");
                    ExternalSimManager.this.sendDisableEvent(1 << i, 1);
                }
            }
            if (!ExternalSimManager.isSupportVsimHotPlugOut()) {
                int i2 = 0;
                while (i2 < TelephonyManager.getDefault().getSimCount()) {
                    TelephonyManager.getDefault();
                    String enabled2 = TelephonyManager.getTelephonyProperty(i2, "vendor.gsm.external.sim.enabled", "0");
                    if (enabled2 != null && enabled2.length() > 0 && !"0".equals(enabled2)) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        i2++;
                    }
                }
                ExternalSimManager.this.disableAllVsimWithResetModem();
            }
        }
    }

    public void disableAllVsimWithResetModem() {
        for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
            waitRildSetDisabledProperty(i);
        }
        VsimEvenHandler vsimEvenHandler = this.mEventHandler;
        if (vsimEvenHandler != null) {
            vsimEvenHandler.retryIfRadioUnavailable(null);
        }
        RadioManager.getInstance().setSilentRebootPropertyForAllModem(RadioCapabilitySwitchUtil.IMSI_READY);
        MtkUiccController uiccCtrl = (MtkUiccController) UiccController.getInstance();
        uiccCtrl.resetRadioForVsim();
        Rlog.i(TAG, "disableAllVsimWithResetModem...");
    }

    public void sendDisableEvent(int slotId, int simType) {
        VsimEvent disableEvent = new VsimEvent(0, 3, slotId);
        disableEvent.putInt(2);
        disableEvent.putInt(simType);
        Message msg = new Message();
        msg.obj = disableEvent;
        this.mEventHandler.sendMessage(msg);
        Rlog.i(TAG, "sendDisableEvent[" + slotId + "]....");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendExternalSimConnectedEvent(int connected) {
        VsimEvent connectedEvent = new VsimEvent(0, 3, 0);
        connectedEvent.putInt(ExternalSimConstants.EVENT_TYPE_EXTERNAL_SIM_CONNECTED);
        connectedEvent.putInt(connected);
        Message msg = new Message();
        msg.obj = connectedEvent;
        this.mEventHandler.sendMessage(msg);
        Rlog.i(TAG, "sendExternalSimConnectedEvent connected=" + connected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void waitRildSetDisabledProperty(int slotId) {
        TelephonyManager.getDefault();
        String enabled = TelephonyManager.getTelephonyProperty(slotId, "vendor.gsm.external.sim.enabled", "0");
        while (enabled != null && enabled.length() > 0 && !"0".equals(enabled)) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TelephonyManager.getDefault();
            enabled = TelephonyManager.getTelephonyProperty(slotId, "vendor.gsm.external.sim.enabled", "0");
        }
    }

    public class ServerTask {
        public static final String HOST_NAME = "vsim-adaptor";
        private VsimIoThread ioThread = null;

        public ServerTask() {
        }

        public void listenConnection() {
            Rlog.d(ExternalSimManager.TAG, "listenConnection() - start");
            LocalServerSocket serverSocket = null;
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            try {
                try {
                    try {
                        serverSocket = new LocalServerSocket(HOST_NAME);
                        while (true) {
                            LocalSocket socket = serverSocket.accept();
                            Rlog.i(ExternalSimManager.TAG, "There is a client is accpted: " + socket.toString());
                            ExternalSimManager.this.stopRecoveryTimer();
                            ExternalSimManager.this.sendExternalSimConnectedEvent(1);
                            ExternalSimManager externalSimManager = ExternalSimManager.this;
                            threadExecutor.execute(externalSimManager.new ConnectionHandler(socket, externalSimManager.mEventHandler));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Rlog.d(ExternalSimManager.TAG, "listenConnection() - end");
                    }
                } catch (IOException e2) {
                    Rlog.w(ExternalSimManager.TAG, "listenConnection catch IOException");
                    e2.printStackTrace();
                    Rlog.d(ExternalSimManager.TAG, "listenConnection finally!!");
                    if (threadExecutor != null) {
                        threadExecutor.shutdown();
                    }
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                    Rlog.d(ExternalSimManager.TAG, "listenConnection() - end");
                } catch (Exception e3) {
                    Rlog.w(ExternalSimManager.TAG, "listenConnection catch Exception");
                    e3.printStackTrace();
                    Rlog.d(ExternalSimManager.TAG, "listenConnection finally!!");
                    if (threadExecutor != null) {
                        threadExecutor.shutdown();
                    }
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                    Rlog.d(ExternalSimManager.TAG, "listenConnection() - end");
                }
            } catch (Throwable th) {
                Rlog.d(ExternalSimManager.TAG, "listenConnection finally!!");
                if (threadExecutor != null) {
                    threadExecutor.shutdown();
                }
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    public class ConnectionHandler implements Runnable {
        public static final String RILD_SERVER_NAME = "rild-vsim";
        private VsimEvenHandler mEventHandler;
        private LocalSocket mSocket;

        public ConnectionHandler(LocalSocket clientSocket, VsimEvenHandler eventHandler) {
            this.mSocket = clientSocket;
            this.mEventHandler = eventHandler;
        }

        @Override // java.lang.Runnable
        public void run() {
            Rlog.i(ExternalSimManager.TAG, "New connection: " + this.mSocket.toString());
            try {
                VsimIoThread ioThread = ExternalSimManager.this.new VsimIoThread(ServerTask.HOST_NAME, this.mSocket.getInputStream(), this.mSocket.getOutputStream(), this.mEventHandler);
                this.mEventHandler.setDataStream(ioThread, null);
                ioThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class VsimEvent {
        public static final int DEFAULT_MAX_DATA_LENGTH = 512;
        private byte[] mData;
        private int mDataLen;
        private int mEventMaxDataLen;
        private int mMessageId;
        private int mReadOffset;
        private int mSlotId;
        private int mTransactionId;

        public VsimEvent(int transactionId, int messageId) {
            this(transactionId, messageId, 0);
        }

        public VsimEvent(int transactionId, int messageId, int slotId) {
            this(transactionId, messageId, 512, slotId);
        }

        public VsimEvent(int transactionId, int messageId, int length, int slotId) {
            this.mEventMaxDataLen = 512;
            this.mTransactionId = transactionId;
            this.mMessageId = messageId;
            this.mSlotId = slotId;
            this.mEventMaxDataLen = length;
            this.mData = new byte[length];
            this.mDataLen = 0;
            this.mReadOffset = 0;
        }

        public void resetOffset() {
            synchronized (this) {
                this.mReadOffset = 0;
            }
        }

        public int putInt(int value) {
            synchronized (this) {
                if (this.mDataLen > this.mEventMaxDataLen - 4) {
                    return -1;
                }
                for (int i = 0; i < 4; i++) {
                    byte[] bArr = this.mData;
                    int i2 = this.mDataLen;
                    bArr[i2] = (byte) ((value >> (i * 8)) & 255);
                    this.mDataLen = i2 + 1;
                }
                return 0;
            }
        }

        public int putShort(int value) {
            synchronized (this) {
                if (this.mDataLen > this.mEventMaxDataLen - 2) {
                    return -1;
                }
                for (int i = 0; i < 2; i++) {
                    byte[] bArr = this.mData;
                    int i2 = this.mDataLen;
                    bArr[i2] = (byte) ((value >> (i * 8)) & 255);
                    this.mDataLen = i2 + 1;
                }
                return 0;
            }
        }

        public int putByte(int value) {
            synchronized (this) {
                int i = this.mDataLen;
                if (i > this.mEventMaxDataLen - 1) {
                    return -1;
                }
                this.mData[i] = (byte) (value & 255);
                this.mDataLen = i + 1;
                return 0;
            }
        }

        public int putString(String str, int len) {
            synchronized (this) {
                if (this.mDataLen > this.mEventMaxDataLen - len) {
                    return -1;
                }
                byte[] s = str.getBytes();
                if (len < str.length()) {
                    System.arraycopy(s, 0, this.mData, this.mDataLen, len);
                    this.mDataLen += len;
                } else {
                    int remain = len - str.length();
                    System.arraycopy(s, 0, this.mData, this.mDataLen, str.length());
                    this.mDataLen += str.length();
                    for (int i = 0; i < remain; i++) {
                        byte[] bArr = this.mData;
                        int i2 = this.mDataLen;
                        bArr[i2] = 0;
                        this.mDataLen = i2 + 1;
                    }
                }
                return 0;
            }
        }

        public int putBytes(byte[] value) {
            synchronized (this) {
                int len = value.length;
                if (len > this.mEventMaxDataLen) {
                    return -1;
                }
                System.arraycopy(value, 0, this.mData, this.mDataLen, len);
                this.mDataLen += len;
                return 0;
            }
        }

        public byte[] getData() {
            byte[] tempData;
            synchronized (this) {
                int i = this.mDataLen;
                tempData = new byte[i];
                System.arraycopy(this.mData, 0, tempData, 0, i);
            }
            return tempData;
        }

        public int getDataLen() {
            int i;
            synchronized (this) {
                i = this.mDataLen;
            }
            return i;
        }

        public byte[] getDataByReadOffest() {
            byte[] tempData;
            synchronized (this) {
                int i = this.mDataLen;
                int i2 = this.mReadOffset;
                tempData = new byte[i - i2];
                System.arraycopy(this.mData, i2, tempData, 0, i - i2);
            }
            return tempData;
        }

        public int getMessageId() {
            return this.mMessageId;
        }

        public int getSlotBitMask() {
            return this.mSlotId;
        }

        public int getFirstSlotId() {
            int simCount = TelephonyManager.getDefault().getSimCount();
            if (getSlotBitMask() > (1 << (simCount - 1))) {
                Rlog.w(ExternalSimManager.TAG, "getFirstSlotId, invalid slot id: " + getSlotBitMask());
                return 0;
            }
            for (int i = 0; i < simCount; i++) {
                if ((getSlotBitMask() & (1 << i)) != 0) {
                    return i;
                }
            }
            Rlog.w(ExternalSimManager.TAG, "getFirstSlotId, invalid slot id: " + getSlotBitMask());
            return 0;
        }

        public int getTransactionId() {
            return this.mTransactionId;
        }

        public int getInt() {
            int ret = 0;
            synchronized (this) {
                byte[] bArr = this.mData;
                if (bArr.length >= 4) {
                    int i = this.mReadOffset;
                    ret = ((bArr[i + 3] & 255) << 24) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 1] & 255) << 8) | (bArr[i] & 255);
                    this.mReadOffset = i + 4;
                }
            }
            return ret;
        }

        public int getShort() {
            int ret;
            synchronized (this) {
                byte[] bArr = this.mData;
                int i = this.mReadOffset;
                ret = ((bArr[i + 1] & 255) << 8) | (bArr[i] & 255);
                this.mReadOffset = i + 2;
            }
            return ret;
        }

        public int getByte() {
            int ret;
            synchronized (this) {
                byte[] bArr = this.mData;
                int i = this.mReadOffset;
                ret = bArr[i] & 255;
                this.mReadOffset = i + 1;
            }
            return ret;
        }

        public byte[] getBytes(int length) {
            synchronized (this) {
                if (length > this.mDataLen - this.mReadOffset) {
                    return null;
                }
                byte[] ret = new byte[length];
                for (int i = 0; i < length; i++) {
                    byte[] bArr = this.mData;
                    int i2 = this.mReadOffset;
                    ret[i] = bArr[i2];
                    this.mReadOffset = i2 + 1;
                }
                return ret;
            }
        }

        public String getString(int len) {
            byte[] buf = new byte[len];
            synchronized (this) {
                System.arraycopy(this.mData, this.mReadOffset, buf, 0, len);
                this.mReadOffset += len;
            }
            return new String(buf).trim();
        }

        public String toString() {
            return new String("dumpEvent: transaction_id: " + getTransactionId() + ", message_id:" + getMessageId() + ", slot_id:" + getSlotBitMask() + ", data_len:" + getDataLen() + ", event:" + ExternalSimManager.truncateString(IccUtils.bytesToHexString(getData())));
        }
    }

    class VsimIoThread extends Thread {
        private static final int MAX_DATA_LENGTH = 20480;
        private VsimEvenHandler mEventHandler;
        private DataInputStream mInput;
        private String mName;
        private DataOutputStream mOutput;
        private boolean mIsContinue = true;
        private String mServerName = "";
        private byte[] readBuffer = null;

        public VsimIoThread(String name, InputStream inputStream, OutputStream outputStream, VsimEvenHandler eventHandler) {
            this.mName = "";
            this.mInput = null;
            this.mOutput = null;
            this.mEventHandler = null;
            this.mName = name;
            this.mInput = new DataInputStream(inputStream);
            this.mOutput = new DataOutputStream(outputStream);
            this.mEventHandler = eventHandler;
            logd("VsimIoThread constructor is called.");
        }

        public void terminate() {
            logd("VsimIoThread terminate.");
            this.mIsContinue = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            logd("VsimIoThread running.");
            while (this.mIsContinue) {
                try {
                    VsimEvent event = readEvent();
                    if (event != null) {
                        Message msg = new Message();
                        msg.obj = event;
                        this.mEventHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    logw("VsimIoThread IOException.");
                    e.printStackTrace();
                    if (this.mServerName.equals("")) {
                        for (int i = 0; i < TelephonyManager.getDefault().getSimCount(); i++) {
                            TelephonyManager.getDefault();
                            String enabled = TelephonyManager.getTelephonyProperty(i, "vendor.gsm.external.sim.enabled", "0");
                            TelephonyManager.getDefault();
                            String insert = TelephonyManager.getTelephonyProperty(i, "vendor.gsm.external.sim.inserted", "0");
                            if (enabled != null && enabled.length() > 0 && !"0".equals(enabled)) {
                                if (insert == null || insert.length() <= 0) {
                                    insert = "0";
                                }
                                ExternalSimManager.this.sendDisableEvent(1 << i, Integer.valueOf(insert).intValue());
                                logi("Disable VSIM and reset modem since socket disconnected.");
                            }
                        }
                        ExternalSimManager.this.sendExternalSimConnectedEvent(0);
                        logw("Socket disconnected and vsim is disabled.");
                        terminate();
                    }
                } catch (Exception e2) {
                    logw("VsimIoThread Exception.");
                    e2.printStackTrace();
                }
            }
        }

        private void writeBytes(byte[] value, int len) throws IOException {
            this.mOutput.write(value, 0, len);
        }

        private void writeInt(int value) throws IOException {
            for (int i = 0; i < 4; i++) {
                this.mOutput.write((value >> (i * 8)) & 255);
            }
        }

        public int writeEvent(VsimEvent event) {
            return writeEvent(event, false);
        }

        public int writeEvent(VsimEvent event, boolean isBigEndian) {
            logd("writeEvent Enter, isBigEndian:" + isBigEndian);
            int ret = -1;
            try {
                synchronized (this) {
                    if (this.mOutput != null) {
                        dumpEvent(event);
                        writeInt(event.getTransactionId());
                        writeInt(event.getMessageId());
                        writeInt(event.getSlotBitMask());
                        writeInt(event.getDataLen());
                        writeBytes(event.getData(), event.getDataLen());
                        this.mOutput.flush();
                        ret = 0;
                    } else {
                        loge("mOut is null, socket is not setup");
                    }
                }
                return ret;
            } catch (Exception e) {
                loge("writeEvent Exception");
                e.printStackTrace();
                return -1;
            }
        }

        private int readInt() throws IOException {
            byte[] tempBuf = new byte[8];
            int readCount = this.mInput.read(tempBuf, 0, 4);
            if (readCount < 0) {
                loge("readInt(), fail to read and throw exception");
                throw new IOException("fail to read");
            }
            return ((tempBuf[1] & 255) << 8) | (tempBuf[3] << 24) | ((tempBuf[2] & 255) << 16) | (tempBuf[0] & 255);
        }

        private VsimEvent readEvent() throws IOException {
            logd("readEvent Enter");
            int transaction_id = readInt();
            int msg_id = readInt();
            int slot_id = readInt();
            int data_len = readInt();
            logd("readEvent transaction_id: " + transaction_id + ", msgId: " + msg_id + ", slot_id: " + slot_id + ", len: " + data_len);
            if (data_len > ExternalSimManager.MAX_VSIM_UICC_CMD_LEN) {
                loge("readEvent(), data_len large than 269");
                throw new IOException("unreasonable data length");
            }
            this.readBuffer = new byte[data_len];
            int offset = 0;
            int remaining = data_len;
            do {
                int countRead = this.mInput.read(this.readBuffer, offset, remaining);
                if (countRead < 0) {
                    loge("readEvent(), fail to read and throw exception");
                    throw new IOException("fail to read");
                }
                offset += countRead;
                remaining -= countRead;
            } while (remaining > 0);
            VsimEvent event = new VsimEvent(transaction_id, msg_id, data_len, slot_id);
            event.putBytes(this.readBuffer);
            dumpEvent(event);
            return event;
        }

        private void dumpEvent(VsimEvent event) {
            if (ExternalSimManager.ENG) {
                logd("dumpEvent: transaction_id: " + event.getTransactionId() + ", message_id:" + event.getMessageId() + ", slot_id:" + event.getSlotBitMask() + ", data_len:" + event.getDataLen() + ", event:" + ExternalSimManager.truncateString(IccUtils.bytesToHexString(event.getData())));
            } else {
                logd("dumpEvent: transaction_id: " + event.getTransactionId() + ", message_id:" + event.getMessageId() + ", slot_id:" + event.getSlotBitMask() + ", data_len:" + event.getDataLen());
            }
        }

        private void logd(String s) {
            Rlog.d(ExternalSimManager.TAG, "[" + this.mName + "] " + s);
        }

        private void logi(String s) {
            Rlog.i(ExternalSimManager.TAG, "[" + this.mName + "] " + s);
        }

        private void logw(String s) {
            Rlog.w(ExternalSimManager.TAG, "[" + this.mName + "] " + s);
        }

        private void loge(String s) {
            Rlog.e(ExternalSimManager.TAG, "[" + this.mName + "] " + s);
        }
    }

    public class VsimIndEventHandler extends Handler {
        public VsimIndEventHandler() {
        }

        protected Integer getCiIndex(Message msg) {
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

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            AsyncResult ar = (AsyncResult) msg.obj;
            switch (msg.what) {
                case 1:
                    if (ExternalSimManager.ENG) {
                        Rlog.d(ExternalSimManager.TAG, "Received EVENT_VSIM_INDICATION...");
                    }
                    Integer ciIndex = getCiIndex(msg);
                    if (ciIndex.intValue() < 0 || ciIndex.intValue() >= ExternalSimManager.this.mCi.length) {
                        Rlog.e(ExternalSimManager.TAG, "Invalid index : " + ciIndex + " received with event " + msg.what);
                    } else {
                        VsimEvent indicationEvent = (VsimEvent) ar.result;
                        dumpEvent(indicationEvent);
                        Message vsimMsg = new Message();
                        vsimMsg.obj = indicationEvent;
                        ExternalSimManager.this.mEventHandler.sendMessage(vsimMsg);
                    }
                    break;
                case 2:
                    if (ExternalSimManager.ENG) {
                        Rlog.d(ExternalSimManager.TAG, "Received EVENT_MULTI_SIM_CONFIG_CHANGED...");
                    }
                    for (int i = 0; i < ExternalSimManager.this.mCi.length; i++) {
                        ExternalSimManager.this.mCi[i].unregisterForVsimIndication(this);
                    }
                    ExternalSimManager.this.mCi = PhoneFactory.getCommandsInterfaces();
                    for (int i2 = 0; i2 < ExternalSimManager.this.mCi.length; i2++) {
                        Integer index = new Integer(i2);
                        ExternalSimManager.this.mCi[i2].registerForVsimIndication(this, 1, index);
                    }
                    break;
                default:
                    Rlog.e(ExternalSimManager.TAG, " Unknown Event " + msg.what);
                    break;
            }
        }

        private void dumpEvent(VsimEvent event) {
            if (ExternalSimManager.ENG) {
                Rlog.d(ExternalSimManager.TAG, "dumpEvent: transaction_id: " + event.getTransactionId() + ", message_id:" + event.getMessageId() + ", slot_id:" + event.getSlotBitMask() + ", data_len:" + event.getDataLen() + ", event:" + ExternalSimManager.truncateString(IccUtils.bytesToHexString(event.getData())));
            } else {
                Rlog.d(ExternalSimManager.TAG, "dumpEvent: transaction_id: " + event.getTransactionId() + ", message_id:" + event.getMessageId() + ", slot_id:" + event.getSlotBitMask() + ", data_len:" + event.getDataLen());
            }
        }
    }

    public class VsimEvenHandler extends Handler {
        private eventHandlerTread[] mEventHandlingThread;
        private boolean[] mIsMdWaitingResponse;
        private boolean[] mIsWaitingAuthRsp;
        private int[] mNoResponseTimeOut;
        private Timer[] mNoResponseTimer;
        private VsimEvent[] mWaitingEvent;
        private VsimIoThread mVsimAdaptorIo = null;
        private VsimIoThread mVsimRilIo = null;
        private boolean mHasNotifyEnableEvnetToModem = false;
        private boolean mIsSwitchRfSuccessful = false;
        private boolean mIsAkaOccupyRf = false;
        private long mLastDisableEventTime = 0;
        private Runnable mTryResetModemRunnable = new Runnable() { // from class: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.VsimEvenHandler.1
            @Override // java.lang.Runnable
            public void run() {
                MtkUiccController uiccCtrl = (MtkUiccController) UiccController.getInstance();
                if (uiccCtrl.isAllRadioAvailable()) {
                    RadioManager.getInstance().setSilentRebootPropertyForAllModem(RadioCapabilitySwitchUtil.IMSI_READY);
                    uiccCtrl.resetRadioForVsim();
                    Rlog.i(ExternalSimManager.TAG, "mTryResetModemRunnable reset modem done.");
                } else {
                    VsimEvenHandler vsimEvenHandler = VsimEvenHandler.this;
                    vsimEvenHandler.postDelayed(vsimEvenHandler.mTryResetModemRunnable, 2000L);
                }
            }
        };

        public VsimEvenHandler() {
            this.mIsMdWaitingResponse = null;
            this.mWaitingEvent = null;
            this.mNoResponseTimer = null;
            this.mIsWaitingAuthRsp = null;
            this.mNoResponseTimeOut = null;
            this.mEventHandlingThread = null;
            int simCount = TelephonyManager.getDefault().getSupportedModemCount();
            this.mIsMdWaitingResponse = new boolean[simCount];
            this.mNoResponseTimer = new Timer[simCount];
            this.mWaitingEvent = new VsimEvent[simCount];
            this.mIsWaitingAuthRsp = new boolean[simCount];
            this.mNoResponseTimeOut = new int[simCount];
            this.mEventHandlingThread = new eventHandlerTread[simCount];
            for (int i = 0; i < simCount; i++) {
                this.mIsMdWaitingResponse[i] = false;
                this.mNoResponseTimer[i] = null;
                this.mWaitingEvent[i] = null;
                this.mIsWaitingAuthRsp[i] = false;
                this.mNoResponseTimeOut[i] = ExternalSimManager.NO_RESPONSE_TIMEOUT_DURATION;
                this.mEventHandlingThread[i] = null;
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            VsimEvent event;
            if (msg.obj instanceof AsyncResult) {
                AsyncResult ar = (AsyncResult) msg.obj;
                event = (VsimEvent) ar.userObj;
            } else {
                event = (VsimEvent) msg.obj;
            }
            int slotId = event.getFirstSlotId();
            if (slotId >= 0 && slotId < TelephonyManager.getDefault().getSimCount()) {
                while (true) {
                    eventHandlerTread eventhandlertread = this.mEventHandlingThread[slotId];
                    if (eventhandlertread == null || !eventhandlertread.isWaiting()) {
                        break;
                    }
                    Rlog.d(ExternalSimManager.TAG, "handleMessage[" + slotId + "] thread running, delay 100 ms...");
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.mEventHandlingThread[slotId] = new eventHandlerTread(event);
                this.mEventHandlingThread[slotId].start();
                return;
            }
            new eventHandlerTread((VsimEvent) msg.obj).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDataStream(VsimIoThread vsimAdpatorIo, VsimIoThread vsimRilIo) {
            this.mVsimAdaptorIo = vsimAdpatorIo;
            this.mVsimRilIo = vsimRilIo;
            Rlog.d(ExternalSimManager.TAG, "VsimEvenHandler setDataStream done.");
        }

        private int getRspMessageId(int requestMsgId) {
            switch (requestMsgId) {
                case 1:
                    return ExternalSimConstants.MSG_ID_INITIALIZATION_RESPONSE;
                case 2:
                    return ExternalSimConstants.MSG_ID_GET_PLATFORM_CAPABILITY_RESPONSE;
                case 3:
                    return ExternalSimConstants.MSG_ID_EVENT_RESPONSE;
                case 7:
                    return ExternalSimConstants.MSG_ID_GET_SERVICE_STATE_RESPONSE;
                case 8:
                    return ExternalSimConstants.MSG_ID_FINALIZATION_RESPONSE;
                case ExternalSimConstants.MSG_ID_UICC_RESET_REQUEST /* 1004 */:
                    return 4;
                case ExternalSimConstants.MSG_ID_UICC_APDU_REQUEST /* 1005 */:
                case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_REQUEST_IND /* 2001 */:
                    return 5;
                case ExternalSimConstants.MSG_ID_UICC_POWER_DOWN_REQUEST /* 1006 */:
                    return 6;
                default:
                    Rlog.d(ExternalSimManager.TAG, "getRspMessageId: " + requestMsgId + "no support.");
                    return -1;
            }
        }

        public class eventHandlerTread extends Thread {
            boolean isWaiting = true;
            VsimEvent mEvent;

            public eventHandlerTread(VsimEvent event) {
                this.mEvent = null;
                this.mEvent = event;
            }

            public boolean isWaiting() {
                return this.isWaiting;
            }

            public void setWaiting(boolean waiting) {
                this.isWaiting = waiting;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Rlog.d(ExternalSimManager.TAG, "eventHandlerTread[ " + this.mEvent.getFirstSlotId() + "]: run...");
                VsimEvenHandler.this.dispatchCallback(this.mEvent);
                this.isWaiting = false;
            }
        }

        public class TimeOutTimerTask extends TimerTask {
            int mSlotId;

            public TimeOutTimerTask(int slotId) {
                this.mSlotId = 0;
                this.mSlotId = slotId;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                synchronized (ExternalSimManager.this.mLock) {
                    if (VsimEvenHandler.this.mWaitingEvent[this.mSlotId] != null) {
                        VsimEvenHandler vsimEvenHandler = VsimEvenHandler.this;
                        vsimEvenHandler.sendNoResponseError(vsimEvenHandler.mWaitingEvent[this.mSlotId]);
                    }
                    Rlog.i(ExternalSimManager.TAG, "TimeOutTimerTask[" + this.mSlotId + "] time out and send response to modem directly.");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendNoResponseError(VsimEvent event) {
            if (this.mIsWaitingAuthRsp[event.getFirstSlotId()]) {
                this.mIsWaitingAuthRsp[event.getFirstSlotId()] = false;
                sendRsimAuthProgressEvent(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
            }
            if (getMdWaitingFlag(event.getFirstSlotId())) {
                setMdWaitingFlag(false, event.getFirstSlotId());
                VsimEvent response = new VsimEvent(event.getTransactionId(), getRspMessageId(event.getMessageId()), event.getSlotBitMask());
                response.putInt(-1);
                response.putInt(2);
                response.putByte(0);
                response.putByte(0);
                ExternalSimManager.this.mCi[event.getFirstSlotId()].sendVsimOperation(response.getTransactionId(), response.getMessageId(), response.getInt(), response.getInt(), response.getDataByReadOffest(), null);
            }
        }

        private void sendVsimNotification(int slotId, int transactionId, int eventId, int simType, Message message) {
            boolean result = ExternalSimManager.this.mCi[slotId].sendVsimNotification(transactionId, eventId, simType, message);
            Rlog.d(ExternalSimManager.TAG, "sendVsimNotification result = " + result);
            if (message == null) {
                int timeOut = 0;
                while (!result && timeOut < ExternalSimManager.PLUG_IN_AUTO_RETRY_TIMEOUT) {
                    try {
                        Thread.sleep(2000L);
                        timeOut += 2000;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result = ExternalSimManager.this.mCi[slotId].sendVsimNotification(transactionId, eventId, simType, message);
                }
            }
            if (!result) {
                Rlog.e(ExternalSimManager.TAG, "sendVsimNotification fail until 40000");
            }
        }

        private int sendSetRsimMappingInfoSync(int slotId, int transactionId) throws InterruptedException {
            VsimEvent event = new VsimEvent(0, ExternalSimConstants.MSG_ID_EVENT_RESPONSE, 1 << slotId);
            event.putInt(6);
            event.putInt(2);
            Message msg = new Message();
            msg.obj = event;
            msg.setTarget(ExternalSimManager.this.mEventHandler);
            boolean result = ExternalSimManager.this.mCi[slotId].sendVsimNotification(transactionId, 6, 2, msg);
            if (result) {
                Rlog.d(ExternalSimManager.TAG, "sendSetRsimMappingInfoSync before mLock.wait");
                ExternalSimManager.this.mLock.wait();
                return 0;
            }
            Rlog.e(ExternalSimManager.TAG, "sendSetRsimMappingInfoSync fail.");
            return -2;
        }

        private void sendPlugOutEvent(VsimEvent event) {
            TelephonyManager.getDefault();
            String isInserted = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
            if ("0".equals(isInserted)) {
                Rlog.d(ExternalSimManager.TAG, "sendPlugOutEvent: " + isInserted);
                return;
            }
            VsimEvent plugOutEvent = new VsimEvent(event.getTransactionId(), 3, event.getSlotBitMask());
            plugOutEvent.putInt(3);
            plugOutEvent.putInt(1);
            setMdWaitingFlag(false, event.getFirstSlotId());
            sendVsimNotification(event.getFirstSlotId(), plugOutEvent.mTransactionId, 3, 1, null);
        }

        private void sendHotPlugEvent(VsimEvent event, boolean plugIn) {
            int eventId = 4;
            if (!plugIn) {
                eventId = 3;
            }
            sendVsimNotification(event.getFirstSlotId(), event.getTransactionId(), eventId, 1, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getVsimSlotId(int simType) {
            switch (simType) {
                case 2:
                    int rSim = SystemProperties.getInt(ExternalSimManager.PREFERED_RSIM_SLOT, -1);
                    return rSim == -1 ? ExternalSimManager.sPreferedRsimSlot : rSim;
                case 3:
                    int akaSim = SystemProperties.getInt(ExternalSimManager.PREFERED_AKA_SIM_SLOT, -1);
                    return akaSim == -1 ? ExternalSimManager.sPreferedAkaSlot : akaSim;
                default:
                    for (int rSim2 = 0; rSim2 < TelephonyManager.getDefault().getSimCount(); rSim2++) {
                        TelephonyManager.getDefault();
                        String enable = TelephonyManager.getTelephonyProperty(rSim2, "vendor.gsm.external.sim.enabled", "0");
                        if (enable != null && enable.length() > 0 && !"0".equals(enable)) {
                            TelephonyManager.getDefault();
                            String inserted = TelephonyManager.getTelephonyProperty(rSim2, "vendor.gsm.external.sim.inserted", "0");
                            if (inserted != null && inserted.length() > 0 && String.valueOf(simType).equals(inserted)) {
                                return rSim2;
                            }
                        }
                    }
                    return -1;
            }
        }

        private void sendRsimAuthProgressEvent(int eventId) {
            this.mIsSwitchRfSuccessful = false;
            int akaSim = getVsimSlotId(3);
            int rSim = getVsimSlotId(2);
            if (akaSim < 0 || akaSim > TelephonyManager.getDefault().getSimCount() || rSim < 0 || rSim > TelephonyManager.getDefault().getSimCount()) {
                Rlog.d(ExternalSimManager.TAG, "sendRsimAuthProgressEvent aka sim: " + akaSim + ", rsim: " + rSim);
                this.mIsSwitchRfSuccessful = true;
                return;
            }
            if (eventId == 201) {
                this.mIsAkaOccupyRf = true;
                SubscriptionManagerService ctrl = SubscriptionManagerService.getInstance();
                int defaultDataSlot = ctrl.getSlotIndex(ctrl.getDefaultDataSubId());
                if (defaultDataSlot == akaSim) {
                    int subId = ctrl.getSubId(rSim);
                    Rlog.i(ExternalSimManager.TAG, "sendRsimAuthProgressEvent aka sim is default data, set to " + subId);
                    ctrl.setDefaultDataSubId(subId);
                    RadioConfig.getInstance().setPreferredDataModem(rSim, (Message) null);
                }
            } else if (eventId == 202) {
                if (!this.mIsAkaOccupyRf) {
                    Rlog.d(ExternalSimManager.TAG, "sendRsimAuthProgressEvent, aka didn't occupy rf");
                    return;
                }
                this.mIsAkaOccupyRf = false;
            }
            Rlog.d(ExternalSimManager.TAG, "sendRsimAuthProgressEvent mIsWaitingAuthRsp[" + rSim + "]: " + this.mIsWaitingAuthRsp[rSim]);
            VsimEvent event = new VsimEvent(0, ExternalSimConstants.MSG_ID_EVENT_RESPONSE, 1 << akaSim);
            event.putInt(eventId);
            event.putInt(1);
            Message msg = new Message();
            msg.obj = event;
            msg.setTarget(ExternalSimManager.this.mEventHandler);
            sendVsimNotification(event.getFirstSlotId(), event.mTransactionId, eventId, 1, msg);
            Rlog.d(ExternalSimManager.TAG, "sendRsimAuthProgressEvent eventId: " + eventId);
            try {
                ExternalSimManager.this.mLock.wait();
            } catch (InterruptedException e) {
                Rlog.w(ExternalSimManager.TAG, "sendRsimAuthProgressEvent InterruptedException.");
            }
        }

        private void sendActiveAkaSimEvent(int slotId, boolean turnOn) {
            int eventId;
            Rlog.d(ExternalSimManager.TAG, "sendActiveAkaSimEvent[" + slotId + "]: " + turnOn);
            int rsimSlot = getVsimSlotId(2);
            if (rsimSlot >= 0 && this.mIsWaitingAuthRsp[rsimSlot] && !turnOn) {
                sendRsimAuthProgressEvent(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
            }
            VsimEvent akaEvent = new VsimEvent(0, 3, 1 << slotId);
            if (turnOn) {
                ExternalSimManager.sPreferedAkaSlot = akaEvent.getFirstSlotId();
                eventId = 6;
                akaEvent.putInt(6);
            } else {
                ExternalSimManager.sPreferedAkaSlot = -1;
                eventId = ExternalSimConstants.EVENT_TYPE_RSIM_AUTH_DONE;
                akaEvent.putInt(ExternalSimConstants.EVENT_TYPE_RSIM_AUTH_DONE);
            }
            akaEvent.putInt(3);
            sendVsimNotification(akaEvent.getFirstSlotId(), akaEvent.mTransactionId, eventId, 3, null);
            if (rsimSlot >= 0 && this.mIsWaitingAuthRsp[rsimSlot] && turnOn) {
                sendRsimAuthProgressEvent(ExternalSimConstants.EVENT_TYPE_SEND_RSIM_AUTH_IND);
            }
        }

        private void setMdWaitingFlag(boolean isWaiting, int slotId) {
            setMdWaitingFlag(isWaiting, null, slotId);
        }

        private void setMdWaitingFlag(boolean isWaiting, VsimEvent event, int slotId) {
            Rlog.d(ExternalSimManager.TAG, "setMdWaitingFlag[" + slotId + "]: " + isWaiting);
            this.mIsMdWaitingResponse[slotId] = isWaiting;
            if (isWaiting) {
                this.mWaitingEvent[slotId] = event;
                Timer[] timerArr = this.mNoResponseTimer;
                if (timerArr[slotId] == null) {
                    timerArr[slotId] = new Timer(true);
                }
                TelephonyManager.getDefault();
                String isVsimEnabled = TelephonyManager.getTelephonyProperty(event != null ? event.getFirstSlotId() : -1, "vendor.gsm.external.sim.enabled", "0");
                if ("".equals(isVsimEnabled) || "0".equals(isVsimEnabled)) {
                    this.mNoResponseTimer[slotId].schedule(new TimeOutTimerTask(slotId), 500L);
                    if (System.currentTimeMillis() > this.mLastDisableEventTime + 5000) {
                        postDelayed(this.mTryResetModemRunnable, 2000L);
                    }
                    Rlog.i(ExternalSimManager.TAG, "recevice modem event under vsim disabled state. lastDisableTime:" + this.mLastDisableEventTime);
                    return;
                }
                this.mNoResponseTimer[slotId].schedule(new TimeOutTimerTask(slotId), this.mNoResponseTimeOut[slotId]);
                return;
            }
            Timer timer = this.mNoResponseTimer[slotId];
            if (timer != null) {
                timer.cancel();
                this.mNoResponseTimer[slotId].purge();
                this.mNoResponseTimer[slotId] = null;
            }
            this.mWaitingEvent[slotId] = null;
        }

        private boolean getMdWaitingFlag(int slotId) {
            Rlog.d(ExternalSimManager.TAG, "getMdWaitingFlag[" + slotId + "]: " + this.mIsMdWaitingResponse[slotId]);
            return this.mIsMdWaitingResponse[slotId];
        }

        private boolean isPlatformReady(int category) {
            switch (category) {
                case 2:
                    MtkProxyController ctrl = (MtkProxyController) ProxyController.getInstance();
                    boolean isReady = !ctrl.isCapabilitySwitching();
                    return isReady;
                case 3:
                    MtkUiccController ctrl2 = (MtkUiccController) UiccController.getInstance();
                    boolean isReady2 = ctrl2.isAllRadioAvailable();
                    return isReady2;
                default:
                    Rlog.d(ExternalSimManager.TAG, "isPlatformReady invalid category: " + category);
                    return true;
            }
        }

        private int retryIfPlatformNotReady(VsimEvent event, int category) {
            boolean isReady = isPlatformReady(category);
            Rlog.d(ExternalSimManager.TAG, "retryIfPlatformNotReady category= " + category + ", isReady= " + isReady);
            if (!isReady) {
                int timeOut = 0;
                do {
                    try {
                        Thread.sleep(2000L);
                        timeOut += 2000;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isReady = isPlatformReady(category);
                    if (isReady) {
                        break;
                    }
                } while (timeOut < ExternalSimManager.PLUG_IN_AUTO_RETRY_TIMEOUT);
            }
            if (isReady) {
                return 0;
            }
            Rlog.w(ExternalSimManager.TAG, "retryIfPlatformNotReady return not ready");
            return -2;
        }

        private boolean isCapabilitySwitchDisabled() {
            return SystemProperties.getBoolean("ro.vendor.mtk_disable_cap_switch", false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean switchModemCapability(int rsimSlot) {
            int raf;
            MtkProxyController ctrl = (MtkProxyController) ProxyController.getInstance();
            if (isCapabilitySwitchDisabled() || (ctrl != null && ctrl.getMaxRafSupported() == ctrl.getMinRafSupported())) {
                Rlog.d(ExternalSimManager.TAG, "[switchModemCapability] no need switch for disabled or same raf:" + ctrl.getMinRafSupported());
                ExternalSimManager.this.mSetCapabilityDone = 2;
                return true;
            }
            if (ctrl != null) {
                try {
                    int len = TelephonyManager.getDefault().getPhoneCount();
                    RadioAccessFamily[] rafs = new RadioAccessFamily[len];
                    boolean atLeastOneMatch = false;
                    for (int phoneId = 0; phoneId < len; phoneId++) {
                        if (phoneId == rsimSlot) {
                            raf = ctrl.getMaxRafSupported();
                            atLeastOneMatch = true;
                        } else {
                            raf = ctrl.getMinRafSupported();
                        }
                        Rlog.d(ExternalSimManager.TAG, "[switchModemCapability] raf[" + phoneId + "]=" + raf);
                        rafs[phoneId] = new RadioAccessFamily(phoneId, raf);
                    }
                    if (atLeastOneMatch) {
                        ctrl.setRadioCapability(rafs);
                        return true;
                    }
                    Rlog.e(ExternalSimManager.TAG, "[switchModemCapability] rsim error:" + rsimSlot);
                    return false;
                } catch (RuntimeException e) {
                    Rlog.e(ExternalSimManager.TAG, "[switchModemCapability] setRadioCapability: Runtime Exception");
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int retryIfRadioUnavailable(VsimEvent event) {
            return retryIfPlatformNotReady(event, 3);
        }

        private int retryIfCapabilitySwitching(VsimEvent event) {
            return retryIfPlatformNotReady(event, 2);
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0073  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void changeRadioSetting(boolean r12) {
            /*
                r11 = this;
                android.telephony.TelephonyManager r0 = android.telephony.TelephonyManager.getDefault()
                int r0 = r0.getSimCount()
                boolean r1 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.isNonDsdaRemoteSimSupport()
                if (r1 == 0) goto Laa
                r1 = 2
                if (r0 <= r1) goto Laa
                java.lang.String r1 = "vendor.gsm.prefered.rsim.slot"
                r2 = -1
                int r1 = android.os.SystemProperties.getInt(r1, r2)
                java.lang.String r3 = "vendor.gsm.prefered.aka.sim.slot"
                int r3 = android.os.SystemProperties.getInt(r3, r2)
                r4 = 0
            L1f:
                if (r4 >= r0) goto Laa
                if (r2 == r1) goto La6
                if (r4 == r1) goto La6
                if (r2 == r3) goto La6
                if (r4 == r3) goto La6
                com.android.internal.telephony.subscription.SubscriptionManagerService r5 = com.android.internal.telephony.subscription.SubscriptionManagerService.getInstance()
                int r5 = r5.getSubId(r4)
                java.lang.String r6 = "phone"
                android.os.IBinder r6 = android.os.ServiceManager.getService(r6)
                com.android.internal.telephony.ITelephony r6 = com.android.internal.telephony.ITelephony.Stub.asInterface(r6)
                java.lang.String r7 = "ExternalSimMgr"
                if (r6 == 0) goto L9c
                r8 = 0
                r9 = 1
                if (r12 != 0) goto L73
                com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager r10 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.this     // Catch: android.os.RemoteException -> La2
                android.content.Context r10 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.m379$$Nest$fgetmContext(r10)     // Catch: android.os.RemoteException -> La2
                java.lang.String r10 = r10.getOpPackageName()     // Catch: android.os.RemoteException -> La2
                boolean r10 = r6.isRadioOnForSubscriber(r5, r10)     // Catch: android.os.RemoteException -> La2
                if (r10 == 0) goto L73
                com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager r10 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.this     // Catch: android.os.RemoteException -> La2
                com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.m389$$Nest$fputmUserRadioOn(r10, r9)     // Catch: android.os.RemoteException -> La2
                r6.setRadioForSubscriber(r5, r8)     // Catch: android.os.RemoteException -> La2
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: android.os.RemoteException -> La2
                r8.<init>()     // Catch: android.os.RemoteException -> La2
                java.lang.String r9 = "changeRadioSetting trun off radio subId:"
                java.lang.StringBuilder r8 = r8.append(r9)     // Catch: android.os.RemoteException -> La2
                java.lang.StringBuilder r8 = r8.append(r5)     // Catch: android.os.RemoteException -> La2
                java.lang.String r8 = r8.toString()     // Catch: android.os.RemoteException -> La2
                android.telephony.Rlog.i(r7, r8)     // Catch: android.os.RemoteException -> La2
                goto La1
            L73:
                if (r9 != r12) goto La1
                com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager r10 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.this     // Catch: android.os.RemoteException -> La2
                boolean r10 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.m384$$Nest$fgetmUserRadioOn(r10)     // Catch: android.os.RemoteException -> La2
                if (r10 != r9) goto La1
                com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager r10 = com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.this     // Catch: android.os.RemoteException -> La2
                com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.m389$$Nest$fputmUserRadioOn(r10, r8)     // Catch: android.os.RemoteException -> La2
                r6.setRadioForSubscriber(r5, r9)     // Catch: android.os.RemoteException -> La2
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: android.os.RemoteException -> La2
                r8.<init>()     // Catch: android.os.RemoteException -> La2
                java.lang.String r9 = "changeRadioSetting trun on radio subId:"
                java.lang.StringBuilder r8 = r8.append(r9)     // Catch: android.os.RemoteException -> La2
                java.lang.StringBuilder r8 = r8.append(r5)     // Catch: android.os.RemoteException -> La2
                java.lang.String r8 = r8.toString()     // Catch: android.os.RemoteException -> La2
                android.telephony.Rlog.i(r7, r8)     // Catch: android.os.RemoteException -> La2
                goto La1
            L9c:
                java.lang.String r8 = "telephony is null"
                android.telephony.Rlog.d(r7, r8)     // Catch: android.os.RemoteException -> La2
            La1:
                goto La6
            La2:
                r7 = move-exception
                r7.printStackTrace()
            La6:
                int r4 = r4 + 1
                goto L1f
            Laa:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.VsimEvenHandler.changeRadioSetting(boolean):void");
        }

        /* JADX WARN: Not initialized variable reg: 17, insn: 0x01e8: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('result' int)]), block:B:57:0x01e7 */
        /* JADX WARN: Removed duplicated region for block: B:179:0x0496  */
        /* JADX WARN: Removed duplicated region for block: B:180:0x049b  */
        /* JADX WARN: Removed duplicated region for block: B:183:0x04a9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void handleEventRequest(int r21, com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.VsimEvent r22) {
            /*
                Method dump skipped, instruction units count: 1272
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager.VsimEvenHandler.handleEventRequest(int, com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager$VsimEvent):void");
        }

        private void waitSimPlugOut(int slotId, int duration) {
            int state;
            MtkSubscriptionManagerService ctrl = MtkSubscriptionManagerService.getInstance();
            int timeOut = 0;
            do {
                try {
                    Thread.sleep(200L);
                    timeOut += 200;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state = TelephonyManager.getSimStateForSlotIndex(slotId);
                List<SubscriptionInfo> subInfos = ctrl.getSubInfoUsingSlotIndexPrivileged(slotId);
                if ((state == 1 || state == 6 || state == 0) && (subInfos == null || subInfos.isEmpty())) {
                    break;
                }
            } while (timeOut < duration);
            Rlog.i(ExternalSimManager.TAG, "waitSimPlugOut, state=" + state + ", timeOut=" + timeOut);
        }

        private void handleGetPlatformCapability(VsimEvent event) {
            event.getInt();
            int simType = event.getInt();
            VsimEvent response = new VsimEvent(event.getTransactionId(), ExternalSimConstants.MSG_ID_GET_PLATFORM_CAPABILITY_RESPONSE, event.getSlotBitMask());
            response.putInt(0);
            TelephonyManager.MultiSimVariants config = TelephonyManager.getDefault().getMultiSimConfiguration();
            if (config == TelephonyManager.MultiSimVariants.DSDS) {
                response.putInt(1);
            } else if (config == TelephonyManager.MultiSimVariants.DSDA) {
                response.putInt(2);
            } else if (config == TelephonyManager.MultiSimVariants.TSTS) {
                response.putInt(3);
            } else {
                response.putInt(0);
            }
            if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) > 0) {
                int mDefaultSupportVersion = 3;
                if (ExternalSimManager.isNonDsdaRemoteSimSupport()) {
                    mDefaultSupportVersion = 3 | 4;
                }
                response.putInt(mDefaultSupportVersion);
            } else {
                response.putInt(0);
            }
            int simCount = TelephonyManager.getDefault().getSimCount();
            Rlog.d(ExternalSimManager.TAG, "handleGetPlatformCapability simType: " + simType + ", simCount: " + simCount);
            if (simType == 1) {
                int rsimSlot = SystemProperties.getInt(ExternalSimManager.PREFERED_RSIM_SLOT, -1);
                if (rsimSlot == -1) {
                    response.putInt((1 << simCount) - 1);
                } else if (rsimSlot == 1 || rsimSlot == 4) {
                    response.putInt(2);
                } else if (rsimSlot == 2) {
                    response.putInt(1);
                }
            } else if (config == TelephonyManager.MultiSimVariants.DSDA) {
                int isCdmaCard = 0;
                int isHasCard = 0;
                for (int i = 0; i < simCount; i++) {
                    String cardType = SystemProperties.get(ExternalSimManager.PROPERTY_RIL_FULL_UICC_TYPE[i], "");
                    if (!cardType.equals("")) {
                        isHasCard |= 1 << i;
                    }
                    if (cardType.contains("CSIM") || cardType.contains("RUIM") || cardType.contains("UIM")) {
                        isCdmaCard |= 1 << i;
                    }
                }
                Rlog.d(ExternalSimManager.TAG, "handleGetPlatformCapability isCdmaCard: " + isCdmaCard + ", isHasCard: " + isHasCard);
                if (isHasCard == 0 || isCdmaCard == 0) {
                    response.putInt(0);
                } else {
                    response.putInt(((1 << simCount) - 1) ^ isCdmaCard);
                }
            } else if (ExternalSimManager.isNonDsdaRemoteSimSupport()) {
                if (config == TelephonyManager.MultiSimVariants.DSDS) {
                    response.putInt((1 << simCount) - 1);
                } else if (config == TelephonyManager.MultiSimVariants.TSTS) {
                    int vsimOnly = SystemProperties.getInt("ro.vendor.mtk_external_sim_only_slots", 0);
                    if (vsimOnly != 0) {
                        response.putInt(vsimOnly);
                    } else {
                        response.putInt((1 << simCount) - 1);
                    }
                }
            } else {
                response.putInt(0);
            }
            this.mVsimAdaptorIo.writeEvent(response);
        }

        private void handleServiceStateRequest(VsimEvent event) {
            int voiceRejectCause = -1;
            int dataRejectCause = -1;
            VsimEvent response = new VsimEvent(event.getTransactionId(), ExternalSimConstants.MSG_ID_GET_SERVICE_STATE_RESPONSE, event.getSlotBitMask());
            int subId = SubscriptionManagerService.getInstance().getSubId(event.getFirstSlotId());
            ServiceState ss = TelephonyManager.getDefault().getServiceStateForSubscriber(subId);
            if (ss != null) {
                Rlog.d(ExternalSimManager.TAG, "handleServiceStateRequest subId: " + subId + ", ss = " + ss.toString());
                NetworkRegistrationInfo csInfo = ss.getNetworkRegistrationInfo(1, 1);
                if (csInfo != null) {
                    voiceRejectCause = csInfo.getRejectCause();
                }
                NetworkRegistrationInfo psInfo = ss.getNetworkRegistrationInfo(2, 1);
                if (psInfo != null) {
                    dataRejectCause = psInfo.getRejectCause();
                }
            }
            response.putInt(0);
            response.putInt(voiceRejectCause);
            response.putInt(dataRejectCause);
            this.mVsimAdaptorIo.writeEvent(response);
        }

        private Object getLock(int msgId) {
            switch (msgId) {
                case 3:
                case ExternalSimConstants.MSG_ID_EVENT_RESPONSE /* 1003 */:
                case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_DONE_IND /* 1009 */:
                case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_ABORT_IND /* 1010 */:
                case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_REQUEST_IND /* 2001 */:
                case ExternalSimConstants.MSG_ID_CAPABILITY_SWITCH_DONE /* 2002 */:
                    break;
            }
            return ExternalSimManager.this.mLock;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchCallback(VsimEvent event) {
            synchronized (getLock(event.getMessageId())) {
                boolean z = false;
                if (this.mEventHandlingThread[event.getFirstSlotId()] != null) {
                    this.mEventHandlingThread[event.getFirstSlotId()].setWaiting(false);
                }
                int msgId = event.getMessageId();
                Rlog.d(ExternalSimManager.TAG, "VsimEvenHandler handleMessage[" + event.getFirstSlotId() + "]: msgId[" + msgId + "] start");
                switch (msgId) {
                    case 1:
                        break;
                    case 2:
                        handleGetPlatformCapability(event);
                        break;
                    case 3:
                        handleEventRequest(event.getInt(), event);
                        break;
                    case 4:
                        if (getMdWaitingFlag(event.getFirstSlotId())) {
                            setMdWaitingFlag(false, event.getFirstSlotId());
                            ExternalSimManager.this.mCi[event.getFirstSlotId()].sendVsimOperation(event.getTransactionId(), event.getMessageId(), event.getInt(), event.getInt(), event.getDataByReadOffest(), null);
                        }
                        break;
                    case 5:
                        if (this.mIsWaitingAuthRsp[event.getFirstSlotId()]) {
                            this.mIsWaitingAuthRsp[event.getFirstSlotId()] = false;
                            sendRsimAuthProgressEvent(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
                        }
                        if (getMdWaitingFlag(event.getFirstSlotId())) {
                            setMdWaitingFlag(false, event.getFirstSlotId());
                            TelephonyManager.getDefault();
                            String inserted = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
                            if (inserted != null && inserted.length() > 0 && !"0".equals(inserted)) {
                                ExternalSimManager.this.mCi[event.getFirstSlotId()].sendVsimOperation(event.getTransactionId(), event.getMessageId(), event.getInt(), event.getInt(), event.getDataByReadOffest(), null);
                            } else {
                                Rlog.d(ExternalSimManager.TAG, "ignore UICC_APDU_RESPONSE since vsim plug out.");
                            }
                        }
                        break;
                    case 6:
                        break;
                    case 7:
                        handleServiceStateRequest(event);
                        break;
                    case 8:
                        break;
                    case ExternalSimConstants.MSG_ID_EVENT_RESPONSE /* 1003 */:
                        int type = event.getInt();
                        if (type == 201 || type == 202) {
                            if (event.getInt() >= 0) {
                                z = true;
                            }
                            this.mIsSwitchRfSuccessful = z;
                            ExternalSimManager.this.mLock.notifyAll();
                        } else if (type == 6) {
                            ExternalSimManager.this.mLock.notifyAll();
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_RESET_REQUEST /* 1004 */:
                        setMdWaitingFlag(true, event, event.getFirstSlotId());
                        TelephonyManager.getDefault();
                        String inserted2 = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
                        if (this.mVsimAdaptorIo != null && inserted2 != null && inserted2.length() > 0 && !"0".equals(inserted2)) {
                            this.mVsimAdaptorIo.writeEvent(event);
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_APDU_REQUEST /* 1005 */:
                        setMdWaitingFlag(true, event, event.getFirstSlotId());
                        TelephonyManager.getDefault();
                        String inserted3 = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
                        if (this.mVsimAdaptorIo != null && inserted3 != null && inserted3.length() > 0 && !"0".equals(inserted3)) {
                            this.mVsimAdaptorIo.writeEvent(event);
                        } else {
                            Rlog.d(ExternalSimManager.TAG, "ignore UICC_APDU_REQUEST since vsim plug out.");
                            sendNoResponseError(event);
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_POWER_DOWN_REQUEST /* 1006 */:
                    case ExternalSimConstants.MSG_ID_UICC_TRAY_PLUG_OUT_WHEN_VSIM_ENABLED /* 1011 */:
                    case ExternalSimConstants.MSG_ID_UICC_TRAY_PLUG_IN_WHEN_VSIM_ENABLED /* 1012 */:
                        TelephonyManager.getDefault();
                        String inserted4 = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
                        if (this.mVsimAdaptorIo != null && inserted4 != null && inserted4.length() > 0 && !"0".equals(inserted4)) {
                            this.mVsimAdaptorIo.writeEvent(event);
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_DONE_IND /* 1009 */:
                        TelephonyManager.getDefault();
                        String inserted5 = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
                        if (inserted5 != null && inserted5.length() > 0 && !"0".equals(inserted5)) {
                            this.mVsimAdaptorIo.writeEvent(event);
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_ABORT_IND /* 1010 */:
                        VsimEvent abortEvent = new VsimEvent(0, ExternalSimConstants.MSG_ID_UICC_APDU_REQUEST, 1 << getVsimSlotId(2));
                        sendNoResponseError(abortEvent);
                        TelephonyManager.getDefault();
                        String inserted6 = TelephonyManager.getTelephonyProperty(1 << getVsimSlotId(2), "vendor.gsm.external.sim.inserted", "0");
                        if (inserted6 != null && inserted6.length() > 0 && !"0".equals(inserted6)) {
                            this.mVsimAdaptorIo.writeEvent(event);
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_AUTHENTICATION_REQUEST_IND /* 2001 */:
                        setMdWaitingFlag(true, event, event.getFirstSlotId());
                        this.mIsWaitingAuthRsp[event.getFirstSlotId()] = true;
                        sendRsimAuthProgressEvent(ExternalSimConstants.EVENT_TYPE_SEND_RSIM_AUTH_IND);
                        event.mMessageId = ExternalSimConstants.MSG_ID_UICC_APDU_REQUEST;
                        TelephonyManager.getDefault();
                        String inserted7 = TelephonyManager.getTelephonyProperty(event.getFirstSlotId(), "vendor.gsm.external.sim.inserted", "0");
                        if (this.mIsSwitchRfSuccessful) {
                            if (inserted7 != null && inserted7.length() > 0 && !"0".equals(inserted7)) {
                                this.mVsimAdaptorIo.writeEvent(event);
                            }
                        } else {
                            sendNoResponseError(event);
                        }
                        break;
                    case ExternalSimConstants.MSG_ID_CAPABILITY_SWITCH_DONE /* 2002 */:
                        ExternalSimManager.this.mLock.notifyAll();
                        break;
                    case ExternalSimConstants.MSG_ID_UICC_TEST_MODE_REQUEST /* 5001 */:
                        ExternalSimManager.this.mCi[event.getFirstSlotId()].sendVsimOperation(event.getTransactionId(), event.getMessageId(), event.getInt(), event.getInt(), event.getDataByReadOffest(), null);
                        break;
                    default:
                        Rlog.d(ExternalSimManager.TAG, "VsimEvenHandler handleMessage: default");
                        break;
                }
                Rlog.d(ExternalSimManager.TAG, "VsimEvenHandler handleMessage[" + event.getFirstSlotId() + "]: msgId[" + msgId + "] end");
            }
        }
    }
}
