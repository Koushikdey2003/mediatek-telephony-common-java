package com.mediatek.internal.telephony;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.LteVopsSupportInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.NetworkService;
import android.telephony.NetworkServiceCallback;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.mediatek.wfo.IMwiService;
import com.mediatek.wfo.IWifiOffloadService;
import com.mediatek.wfo.MwisConstants;
import com.mediatek.wfo.WifiOffloadManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class IWlanNetworkService extends NetworkService {
    private static final String TAG = "IWlanNetworkService";
    private IBinder mBinder;
    private IMwiService mMwiService;
    private final ConcurrentHashMap<Integer, IWlanNetworkServiceProvider> mIWlanNetSrvProviderMap = new ConcurrentHashMap<>();
    private IWifiOffloadService sWifiOffloadService = null;
    private MwiServiceDeathRecipient mDeathRecipient = new MwiServiceDeathRecipient();
    private IWifiOffloadListenerProxy mProxy = null;

    public IWlanNetworkService() {
        bindAndRegisterWifiOffloadService();
        log("IWlanNetworkService init.");
    }

    private class IWlanNetworkServiceProvider extends NetworkService.NetworkServiceProvider {
        public static final int GET_IWLAN_REGISTRATION_STATE_DONE = 2;
        public static final int IWLAN_REGISTRATION_STATE_CHANGED = 1;
        private final ConcurrentHashMap<Message, NetworkServiceCallback> mCallbackMap;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Object mLock;
        private final Looper mLooper;
        private int mWfcState;

        IWlanNetworkServiceProvider(int slotId) {
            super(IWlanNetworkService.this, slotId);
            this.mCallbackMap = new ConcurrentHashMap<>();
            this.mWfcState = 0;
            this.mLock = new Object();
            IWlanNetworkService.this.log("IWlanNetworkServiceProvider construct.");
            HandlerThread handlerThread = new HandlerThread(IWlanNetworkServiceProvider.class.getSimpleName());
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            this.mLooper = looper;
            this.mHandler = new Handler(looper) { // from class: com.mediatek.internal.telephony.IWlanNetworkService.IWlanNetworkServiceProvider.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    NetworkServiceCallback callback = (NetworkServiceCallback) IWlanNetworkServiceProvider.this.mCallbackMap.remove(message);
                    switch (message.what) {
                        case 1:
                            int state = message.arg1;
                            IWlanNetworkService.this.log("IWLAN_REGISTRATION_STATE_CHANGED, slotid: " + IWlanNetworkServiceProvider.this.getSlotIndex() + ", state: " + state);
                            IWlanNetworkServiceProvider.this.mWfcState = state;
                            IWlanNetworkServiceProvider.this.notifyNetworkRegistrationInfoChanged();
                            break;
                        case 2:
                            IWlanNetworkServiceProvider iWlanNetworkServiceProvider = IWlanNetworkServiceProvider.this;
                            NetworkRegistrationInfo netState = iWlanNetworkServiceProvider.createRegistrationState(iWlanNetworkServiceProvider.mWfcState);
                            try {
                                IWlanNetworkService.this.log("Calling callback.onGetNetworkRegistrationStateComplete.resultCode = 0, netState = " + netState + ", slotid: " + IWlanNetworkServiceProvider.this.getSlotIndex());
                                callback.onRequestNetworkRegistrationInfoComplete(0, netState);
                            } catch (Exception e) {
                                IWlanNetworkService.this.loge("Exception: " + e);
                                return;
                            }
                            break;
                    }
                }
            };
        }

        public Handler getHandler() {
            return this.mHandler;
        }

        public int getWfcState() {
            return this.mWfcState;
        }

        public void requestNetworkRegistrationInfo(int domain, NetworkServiceCallback callback) {
            IWlanNetworkService.this.log("getNetworkRegistrationState for domain " + domain + ", slotid: " + getSlotIndex());
            if (domain == 2) {
                Message message = Message.obtain(this.mHandler, 2);
                this.mCallbackMap.put(message, callback);
                message.sendToTarget();
            } else {
                IWlanNetworkService.this.loge("getNetworkRegistrationState invalid domain " + domain + ", slotid: " + getSlotIndex());
                callback.onRequestNetworkRegistrationInfoComplete(2, (NetworkRegistrationInfo) null);
            }
        }

        public void close() {
            IWlanNetworkService.this.log("close.");
            this.mCallbackMap.clear();
            this.mHandlerThread.quit();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public NetworkRegistrationInfo createRegistrationState(int state) {
            int regState;
            int accessNetworkTechnology;
            IWlanNetworkService.this.log("createRegistrationState.");
            if (state == 1) {
                regState = 1;
                accessNetworkTechnology = 18;
            } else if (state == 2) {
                regState = 2;
                accessNetworkTechnology = 18;
            } else {
                regState = 0;
                accessNetworkTechnology = 0;
            }
            ArrayList<Integer> availableServices = IWlanNetworkService.this.getAvailableServices(regState, 2, false);
            LteVopsSupportInfo lteVopsSupportInfo = new LteVopsSupportInfo(1, 1);
            return new NetworkRegistrationInfo(2, 2, regState, accessNetworkTechnology, 0, false, availableServices, null, null, 0, false, false, false, lteVopsSupportInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<Integer> getAvailableServices(int regState, int domain, boolean emergencyOnly) {
        log("getAvailableServices.");
        if (emergencyOnly) {
            ArrayList<Integer> availableServices = new ArrayList<>(Arrays.asList(5));
            return availableServices;
        }
        if ((regState != 5 && regState != 1) || domain != 2) {
            return null;
        }
        ArrayList<Integer> availableServices2 = new ArrayList<>(Arrays.asList(2));
        return availableServices2;
    }

    public NetworkService.NetworkServiceProvider onCreateNetworkServiceProvider(int slotIndex) {
        log("IWlan network service created for slot " + slotIndex);
        if (!SubscriptionManager.isValidSlotIndex(slotIndex)) {
            loge("Tried to Iwlan network service with invalid slotId " + slotIndex);
            return null;
        }
        this.mIWlanNetSrvProviderMap.remove(Integer.valueOf(slotIndex));
        this.mIWlanNetSrvProviderMap.put(Integer.valueOf(slotIndex), new IWlanNetworkServiceProvider(slotIndex));
        return this.mIWlanNetSrvProviderMap.get(Integer.valueOf(slotIndex));
    }

    public int getWfcState(int slotIndex) {
        if (!this.mIWlanNetSrvProviderMap.containsKey(Integer.valueOf(slotIndex))) {
            log("getWfcState id " + slotIndex + " did not exist.");
            return 255;
        }
        int wfcState = this.mIWlanNetSrvProviderMap.get(Integer.valueOf(slotIndex)).getWfcState();
        log("getWfcState, slotid: " + slotIndex + ", state: " + wfcState);
        return wfcState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String s) {
        Rlog.i(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String s) {
        Rlog.e(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IWifiOffloadListenerProxy createWifiOffloadListenerProxy() {
        if (this.mProxy == null) {
            log("create WifiOffloadListenerProxy");
            this.mProxy = new IWifiOffloadListenerProxy();
        }
        return this.mProxy;
    }

    private class IWifiOffloadListenerProxy extends WifiOffloadManager.Listener {
        private IWifiOffloadListenerProxy() {
        }

        @Override // com.mediatek.wfo.WifiOffloadManager.Listener, com.mediatek.wfo.IWifiOffloadListener
        public void onWfcStateChanged(int simId, int state) {
            IWlanNetworkService.this.logi("onWfcStateChanged simIdx=" + simId + ", state=" + state);
            IWlanNetworkService.this.notifyWfcStateChanged(simId, state);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWfcStateChanged(int simId, int state) {
        if (!this.mIWlanNetSrvProviderMap.containsKey(Integer.valueOf(simId))) {
            log("IWlanNetworkServiceProvider id " + simId + " did not exist.");
            return;
        }
        log("notifyWfcStateChanged: " + state);
        Message msg = this.mIWlanNetSrvProviderMap.get(Integer.valueOf(simId)).getHandler().obtainMessage(1, state, 0);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initWfcState() {
        int count = this.mIWlanNetSrvProviderMap.size();
        for (int i = 0; i < count; i++) {
            try {
                if (!this.mIWlanNetSrvProviderMap.containsKey(Integer.valueOf(i))) {
                    log("initWfcState id " + i + " did not exist.");
                } else {
                    IMwiService iMwiService = this.mMwiService;
                    if (iMwiService != null) {
                        int wfcState = iMwiService.getWfcState(i);
                        logi("initWfcState simIdx=" + i + ", state=" + wfcState);
                        if (wfcState != 255 && wfcState != this.mIWlanNetSrvProviderMap.get(Integer.valueOf(i)).getWfcState()) {
                            notifyWfcStateChanged(i, wfcState);
                        }
                    } else {
                        log("initWfcState: No MwiService exist");
                    }
                }
            } catch (RemoteException e) {
                loge("initWfcState can't get MwiService:" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndBindWifiOffloadService() {
        try {
            if (this.mBinder == null) {
                this.mBinder = ServiceManager.getService(MwisConstants.MWI_SERVICE);
            }
            if (this.mBinder != null) {
                log("linkToDeath");
                this.mBinder.linkToDeath(this.mDeathRecipient, 0);
                IMwiService iMwiServiceAsInterface = IMwiService.Stub.asInterface(this.mBinder);
                this.mMwiService = iMwiServiceAsInterface;
                this.sWifiOffloadService = iMwiServiceAsInterface.getWfcHandlerInterface();
            } else {
                log("No MwiService exist");
            }
        } catch (RemoteException e) {
            loge("can't get MwiService:" + e);
        }
        log("checkAndBindWifiOffloadService: sWifiOffloadService = " + this.sWifiOffloadService);
    }

    private class MwiServiceDeathRecipient implements IBinder.DeathRecipient {
        private MwiServiceDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            IWlanNetworkService.this.loge("binderDied");
            try {
                if (IWlanNetworkService.this.mBinder != null) {
                    IWlanNetworkService.this.mBinder.unlinkToDeath(this, 0);
                }
            } catch (Exception e) {
                IWlanNetworkService.this.loge("binderDied: " + e);
            }
            IWlanNetworkService.this.sWifiOffloadService = null;
            IWlanNetworkService.this.mBinder = null;
            IWlanNetworkService.this.bindAndRegisterWifiOffloadService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindAndRegisterWifiOffloadService() {
        new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.IWlanNetworkService.1
            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    if (IWlanNetworkService.this.sWifiOffloadService != null) {
                        break;
                    }
                    IWlanNetworkService.this.checkAndBindWifiOffloadService();
                    if (IWlanNetworkService.this.sWifiOffloadService != null) {
                        try {
                            IWlanNetworkService.this.sWifiOffloadService.registerForHandoverEvent(IWlanNetworkService.this.createWifiOffloadListenerProxy());
                        } catch (RemoteException e) {
                            IWlanNetworkService.this.loge("can't register handover event");
                        }
                    } else if (SystemProperties.getInt("persist.vendor.mtk_wfc_support", 0) == 0) {
                        IWlanNetworkService.this.loge("can't get WifiOffloadService");
                        break;
                    }
                    if (IWlanNetworkService.this.sWifiOffloadService != null) {
                        break;
                    }
                    IWlanNetworkService.this.loge("can't get WifiOffloadService, retry after 1s.");
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e2) {
                    }
                }
                IWlanNetworkService.this.initWfcState();
            }
        }).start();
    }
}
