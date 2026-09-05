package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.internal.telecom.ITelecomService;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.mediatek.internal.telephony.devreg.DeviceRegisterController;
import com.mediatek.internal.telephony.phb.MtkUiccPhoneBookController;
import com.mediatek.internal.telephony.worldphone.WorldPhoneUtil;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MtkProxyController extends ProxyController {
    private static final int C6M_1RILD = 2;
    private static final int C6M_3RILD = 1;
    private static final int EVENT_ON_REQUEST = 7;
    private static final int EVENT_RADIO_AVAILABLE = 6;
    private static final int G5M_1RILD = 0;
    private static final String PROPERTY_CAPABILITY_SWITCH = "persist.vendor.radio.simswitch";
    private static final String PROPERTY_CAPABILITY_SWITCH_STATE = "persist.vendor.radio.simswitchstate";
    private static final int RC_CANNOT_SWITCH = 2;
    private static final int RC_DO_SWITCH = 0;
    private static final int RC_NO_NEED_SWITCH = 1;
    private static final int RC_RETRY_CAUSE_AIRPLANE_MODE = 5;
    private static final int RC_RETRY_CAUSE_CAPABILITY_SWITCHING = 2;
    private static final int RC_RETRY_CAUSE_IN_CALL = 3;
    private static final int RC_RETRY_CAUSE_NONE = 0;
    private static final int RC_RETRY_CAUSE_RADIO_UNAVAILABLE = 4;
    private static final int RC_RETRY_CAUSE_RESULT_ERROR = 6;
    private static final int RC_RETRY_CAUSE_WORLD_MODE_SWITCHING = 1;
    private BroadcastReceiver mCallStateReceiver;
    private CommandsInterface[] mCi;
    RadioAccessFamily[] mCurrRafs;
    private DeviceRegisterController mDeviceRegisterController;
    private boolean mHasRegisterCallStateReceiver;
    private boolean mHasRegisterWorldModeReceiver;
    private boolean mIsCapSwitching;
    private Handler mMtkHandler;
    private MtkPhoneSubInfoControllerEx mMtkPhoneSubInfoControllerEx;
    private MtkUiccPhoneBookController mMtkUiccPhoneBookController;
    private MtkUiccSmsController mMtkUiccSmsController;
    RadioAccessFamily[] mNextRafs;
    private int mRildMode;
    private int mSetRafRetryCause;
    private BroadcastReceiver mWorldModeReceiver;
    private int onExceptionCount;

    public MtkProxyController(Context context) {
        super(context);
        this.mIsCapSwitching = false;
        this.mHasRegisterWorldModeReceiver = false;
        this.mHasRegisterCallStateReceiver = false;
        this.mNextRafs = null;
        this.mCurrRafs = null;
        this.onExceptionCount = 0;
        this.mMtkHandler = new Handler() { // from class: com.mediatek.internal.telephony.MtkProxyController.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                MtkProxyController.this.mtkLogd("mtkHandleMessage msg.what=" + msg.what);
                switch (msg.what) {
                    case 6:
                        if (MtkProxyController.this.mRildMode != 2) {
                            MtkProxyController.this.onRetryWhenRadioAvailable(msg);
                            break;
                        }
                        break;
                    case 7:
                        MtkProxyController mtkProxyController = MtkProxyController.this;
                        mtkProxyController.onSetRadioCapabilityRequest(mtkProxyController.mCurrRafs);
                        break;
                }
            }
        };
        this.mWorldModeReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkProxyController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                MtkProxyController.this.mtkLogd("mWorldModeReceiver: action = " + action);
                if (!WorldPhoneUtil.isWorldModeSupport() && WorldPhoneUtil.isWorldPhoneSupport() && ModemSwitchHandler.ACTION_MODEM_SWITCH_DONE.equals(action) && MtkProxyController.this.mNextRafs != null && MtkProxyController.this.mSetRafRetryCause == 1) {
                    try {
                        MtkProxyController mtkProxyController = MtkProxyController.this;
                        if (!mtkProxyController.setRadioCapability(mtkProxyController.mNextRafs)) {
                            MtkProxyController.this.sendCapabilityFailBroadcast();
                        }
                    } catch (RuntimeException e) {
                        MtkProxyController.this.sendCapabilityFailBroadcast();
                    }
                }
            }
        };
        this.mCallStateReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.MtkProxyController.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                MtkProxyController.this.mtkLogd("mCallStateReceiver: action = " + action);
                if (TelephonyManager.getDefault().getCallState() == 0 && !MtkProxyController.this.isEccInProgress() && MtkProxyController.this.mNextRafs != null && MtkProxyController.this.mSetRafRetryCause == 3) {
                    MtkProxyController.this.unRegisterCallStateReceiver();
                    try {
                        MtkProxyController mtkProxyController = MtkProxyController.this;
                        if (!mtkProxyController.setRadioCapability(mtkProxyController.mNextRafs)) {
                            MtkProxyController.this.sendCapabilityFailBroadcast();
                        }
                    } catch (RuntimeException e) {
                        MtkProxyController.this.sendCapabilityFailBroadcast();
                    }
                }
            }
        };
        this.mCi = PhoneFactory.getCommandsInterfaces();
        String rilMode = SystemProperties.get("ro.vendor.mtk_ril_mode", "c6m_1rild");
        if (rilMode.equals("c6m_1rild")) {
            this.mRildMode = 2;
        } else if (rilMode.equals("c6m_3rild")) {
            this.mRildMode = 1;
        } else {
            this.mRildMode = 0;
        }
        mtkLogd("Constructor - Enter, rild mode = " + this.mRildMode);
        this.mMtkUiccPhoneBookController = new MtkUiccPhoneBookController();
        this.mMtkPhoneSubInfoControllerEx = new MtkPhoneSubInfoControllerEx(this.mContext, this.mPhones);
        this.mMtkUiccSmsController = new MtkUiccSmsController();
        mtkLogd("Constructor - Exit");
        this.mDeviceRegisterController = new DeviceRegisterController(this.mContext, this.mMtkUiccSmsController);
    }

    public DeviceRegisterController getDeviceRegisterController() {
        return this.mDeviceRegisterController;
    }

    public boolean setRadioCapability(RadioAccessFamily[] rafs) {
        if (SystemProperties.getBoolean("ro.vendor.mtk_disable_cap_switch", false)) {
            completeRadioCapabilityTransaction();
            mtkLogd("skip switching because mtk_disable_cap_switch is true");
        } else {
            Message tmsg = this.mMtkHandler.obtainMessage(7);
            this.mCurrRafs = rafs;
            this.mMtkHandler.sendMessage(tmsg);
        }
        return true;
    }

    public boolean onSetRadioCapabilityRequest(RadioAccessFamily[] rafs) {
        if (this.mRildMode == 2) {
            boolean ret = super.setRadioCapability(rafs);
            if (!ret) {
                this.mNextRafs = rafs;
            }
            return ret;
        }
        if (rafs.length != this.mPhones.length) {
            throw new RuntimeException("Length of input rafs must equal to total phone count");
        }
        int i = 0;
        while (true) {
            if (i >= rafs.length) {
                break;
            }
            if ((rafs[i].getRadioAccessFamily() & 1) <= 0) {
                i++;
            } else {
                SystemProperties.set(PROPERTY_CAPABILITY_SWITCH_STATE, String.valueOf(i));
                break;
            }
        }
        int result = checkRadioCapabilitySwitchConditions(rafs);
        if (result == 0) {
            return super.setRadioCapability(rafs);
        }
        return true;
    }

    protected boolean doSetRadioCapabilities(RadioAccessFamily[] rafs) {
        if (this.mRildMode == 2) {
            return super.doSetRadioCapabilities(rafs);
        }
        synchronized (this) {
            this.mIsCapSwitching = true;
        }
        this.onExceptionCount = 0;
        return super.doSetRadioCapabilities(rafs);
    }

    protected void onStartRadioCapabilityResponse(Message msg) {
        synchronized (this.mSetRadioAccessFamilyStatus) {
            AsyncResult ar = (AsyncResult) msg.obj;
            boolean z = true;
            if (ar.exception != null) {
                if (this.onExceptionCount == 0 && this.mRildMode != 2) {
                    CommandException.Error err = null;
                    this.onExceptionCount = 1;
                    if (ar.exception instanceof CommandException) {
                        err = ar.exception.getCommandError();
                    }
                    if (err == CommandException.Error.RADIO_NOT_AVAILABLE) {
                        this.mSetRafRetryCause = 4;
                        for (int i = 0; i < this.mPhones.length; i++) {
                            this.mCi[i].registerForAvailable(this.mMtkHandler, 6, (Object) null);
                        }
                        mtkLoge("onStartRadioCapabilityResponse: Retry later due to modem off");
                    }
                }
                mtkLogd("onStartRadioCapabilityResponse got exception=" + ar.exception);
                this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
                sendCapabilityFailBroadcast();
                resetSimSwitchState();
                return;
            }
            RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
            if (rc != null && rc.getSession() == this.mRadioCapabilitySessionId) {
                this.mRadioAccessFamilyStatusCounter--;
                int id = rc.getPhoneId();
                if (((AsyncResult) msg.obj).exception != null) {
                    mtkLogd("onStartRadioCapabilityResponse: Error response session=" + rc.getSession());
                    mtkLogd("onStartRadioCapabilityResponse: phoneId=" + id + " status=FAIL");
                    this.mSetRadioAccessFamilyStatus[id] = 5;
                    this.mTransactionFailed = true;
                } else {
                    mtkLogd("onStartRadioCapabilityResponse: phoneId=" + id + " status=STARTED");
                    this.mSetRadioAccessFamilyStatus[id] = 2;
                }
                if (this.mRadioAccessFamilyStatusCounter == 0) {
                    StringBuilder sbAppend = new StringBuilder().append("onStartRadioCapabilityResponse: success=");
                    if (this.mTransactionFailed) {
                        z = false;
                    }
                    mtkLogd(sbAppend.append(z).toString());
                    if (this.mTransactionFailed) {
                        issueFinish(this.mRadioCapabilitySessionId);
                    } else {
                        resetRadioAccessFamilyStatusCounter();
                        for (int i2 = 0; i2 < this.mPhones.length; i2++) {
                            sendRadioCapabilityRequest(i2, this.mRadioCapabilitySessionId, 2, this.mNewRadioAccessFamily[i2], this.mNewLogicalModemIds[i2], 0, 3);
                            mtkLogd("onStartRadioCapabilityResponse: phoneId=" + i2 + " status=APPLYING");
                            this.mSetRadioAccessFamilyStatus[i2] = 3;
                        }
                    }
                }
                return;
            }
            mtkLogd("onStartRadioCapabilityResponse: Ignore session=" + this.mRadioCapabilitySessionId + " rc=" + rc);
        }
    }

    protected void onApplyRadioCapabilityErrorHandler(Message msg) {
        if (this.mRildMode == 2) {
            this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
            sendCapabilityFailBroadcast();
            resetSimSwitchState();
            return;
        }
        RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
        AsyncResult ar = (AsyncResult) msg.obj;
        CommandException.Error err = null;
        if (rc == null && ar.exception != null && this.onExceptionCount == 0) {
            this.onExceptionCount = 1;
            if (ar.exception instanceof CommandException) {
                err = ar.exception.getCommandError();
            }
            if (err == CommandException.Error.RADIO_NOT_AVAILABLE) {
                this.mSetRafRetryCause = 4;
                for (int i = 0; i < this.mPhones.length; i++) {
                    this.mCi[i].registerForAvailable(this.mMtkHandler, 6, (Object) null);
                }
                mtkLoge("onApplyRadioCapabilityResponse: Retry due to RADIO_NOT_AVAILABLE");
            } else {
                mtkLoge("onApplyRadioCapabilityResponse: exception=" + ar.exception);
            }
            this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
            sendCapabilityFailBroadcast();
            resetSimSwitchState();
        }
    }

    protected void onApplyExceptionHandler(Message msg) {
        if (this.mRildMode == 2) {
            super.onApplyExceptionHandler(msg);
            return;
        }
        RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
        AsyncResult ar = (AsyncResult) msg.obj;
        int id = rc.getPhoneId();
        CommandException.Error err = null;
        if (ar.exception instanceof CommandException) {
            err = ar.exception.getCommandError();
        }
        if (err == CommandException.Error.RADIO_NOT_AVAILABLE) {
            this.mSetRafRetryCause = 4;
            this.mCi[id].registerForAvailable(this.mMtkHandler, 6, (Object) null);
            mtkLoge("onApplyRadioCapabilityResponse: Retry later due to modem off");
            return;
        }
        mtkLoge("onApplyRadioCapabilityResponse: exception=" + ar.exception);
    }

    protected void onNotificationRadioCapabilityChanged(Message msg) {
        RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
        if (rc == null) {
            logd("onNotificationRadioCapabilityChanged: rc == null");
            return;
        }
        logd("onNotificationRadioCapabilityChanged: rc=" + rc);
        int id = rc.getPhoneId();
        if (((AsyncResult) msg.obj).exception == null && (rc.getStatus() == 2 || rc.getSession() != this.mRadioCapabilitySessionId)) {
            logd("onNotificationRadioCapabilityChanged: update phone capability");
            this.mPhones[id].radioCapabilityUpdated(rc, true);
        }
        super.onNotificationRadioCapabilityChanged(msg);
    }

    protected void onFinishRadioCapabilityResponse(Message msg) {
        RadioCapability rc = (RadioCapability) ((AsyncResult) msg.obj).result;
        if ((rc == null || rc.getSession() != this.mRadioCapabilitySessionId) && ((AsyncResult) msg.obj).exception != null) {
            synchronized (this.mSetRadioAccessFamilyStatus) {
                mtkLogd("onFinishRadioCapabilityResponse C2K mRadioAccessFamilyStatusCounter=" + this.mRadioAccessFamilyStatusCounter);
                this.mRadioAccessFamilyStatusCounter--;
                if (this.mRadioAccessFamilyStatusCounter == 0) {
                    completeRadioCapabilityTransaction();
                }
                mtkLoge("onFinishRadioCapabilityResponse: exception=" + ((AsyncResult) msg.obj).exception);
            }
            return;
        }
        if (this.mRildMode == 2) {
            super.onFinishRadioCapabilityResponse(msg);
            return;
        }
        int phoneId = SystemProperties.getInt(PROPERTY_CAPABILITY_SWITCH_STATE, -1);
        if (phoneId >= 0 && phoneId < this.mPhones.length && this.mRadioAccessFamilyStatusCounter == 1) {
            int raf = this.mPhones[phoneId].getRadioAccessFamily();
            if ((raf & 1) == 0) {
                mtkLogd("onFinishRadioCapabilityResponse, main phone raf[" + phoneId + "]=" + raf);
                this.mSetRafRetryCause = 6;
            }
        }
        super.onFinishRadioCapabilityResponse(msg);
    }

    protected void onTimeoutRadioCapability(Message msg) {
        Message tmsg = this.mHandler.obtainMessage(5, this.mRadioCapabilitySessionId, 0);
        this.mHandler.sendMessageDelayed(tmsg, 45000L);
    }

    protected void issueFinish(int sessionId) {
        if (this.mRildMode == 2) {
            super.issueFinish(sessionId);
            return;
        }
        synchronized (this.mSetRadioAccessFamilyStatus) {
            resetRadioAccessFamilyStatusCounter();
            for (int i = 0; i < this.mPhones.length; i++) {
                mtkLogd("issueFinish: phoneId=" + i + " sessionId=" + sessionId + " mTransactionFailed=" + this.mTransactionFailed);
                sendRadioCapabilityRequest(i, sessionId, 4, this.mOldRadioAccessFamily[i], this.mCurrentLogicalModemIds[i], this.mTransactionFailed ? 2 : 1, 4);
                if (this.mTransactionFailed) {
                    mtkLogd("issueFinish: phoneId: " + i + " status: FAIL");
                    this.mSetRadioAccessFamilyStatus[i] = 5;
                }
            }
        }
    }

    protected void completeRadioCapabilityTransaction() {
        Intent intent;
        mtkLogd("onFinishRadioCapabilityResponse: success=" + (!this.mTransactionFailed));
        SystemProperties.set(PROPERTY_CAPABILITY_SWITCH_STATE, "-1");
        if (!this.mTransactionFailed) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
            for (int i = 0; i < this.mPhones.length; i++) {
                int raf = this.mPhones[i].getRadioAccessFamily();
                mtkLogd("radioAccessFamily[" + i + "]=" + raf);
                RadioAccessFamily phoneRC = new RadioAccessFamily(i, raf);
                arrayList.add(phoneRC);
            }
            intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
            intent.putParcelableArrayListExtra("rafs", arrayList);
            this.mRadioCapabilitySessionId = this.mUniqueIdGenerator.getAndIncrement();
            resetSimSwitchState();
        } else {
            intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
            this.mTransactionFailed = false;
            resetSimSwitchState();
        }
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PHONE_STATE");
        if (this.mNextRafs != null) {
            int i2 = this.mSetRafRetryCause;
            if (i2 == 2 || i2 == 6 || this.mRildMode == 2) {
                mtkLogd("has next request, trigger it, cause = " + this.mSetRafRetryCause);
                try {
                    if (!setRadioCapability(this.mNextRafs)) {
                        sendCapabilityFailBroadcast();
                    } else {
                        this.mSetRafRetryCause = 0;
                        this.mNextRafs = null;
                    }
                } catch (RuntimeException e) {
                    sendCapabilityFailBroadcast();
                }
            }
        }
    }

    private void resetSimSwitchState() {
        if (isCapabilitySwitching()) {
            this.mHandler.removeMessages(5);
        }
        if (this.mRildMode == 2) {
            clearTransaction();
            return;
        }
        synchronized (this) {
            this.mIsCapSwitching = false;
        }
        clearTransaction();
    }

    protected void sendRadioCapabilityRequest(int phoneId, int sessionId, int rcPhase, int radioFamily, String logicalModemId, int status, int eventId) {
        if (this.mRildMode == 2) {
            super.sendRadioCapabilityRequest(phoneId, sessionId, rcPhase, radioFamily, logicalModemId, status, eventId);
            return;
        }
        if (logicalModemId == null || logicalModemId.equals("")) {
            logicalModemId = "modem_sys3";
        }
        super.sendRadioCapabilityRequest(phoneId, sessionId, rcPhase, radioFamily, logicalModemId, status, eventId);
    }

    public int getMaxRafSupported() {
        int[] iArr = new int[this.mPhones.length];
        int maxRaf = 0;
        if (this.mRildMode == 2) {
            return super.getMaxRafSupported();
        }
        for (int len = 0; len < this.mPhones.length; len++) {
            if ((this.mPhones[len].getRadioAccessFamily() & 1) == 1) {
                maxRaf = this.mPhones[len].getRadioAccessFamily();
            }
        }
        mtkLogd("getMaxRafSupported: maxRafBit=0 maxRaf=" + maxRaf + " flag=" + (maxRaf & 1));
        if (maxRaf == 0) {
            return maxRaf | 1;
        }
        return maxRaf;
    }

    public int getMinRafSupported() {
        int[] iArr = new int[this.mPhones.length];
        int minRaf = 0;
        if (this.mRildMode == 2) {
            return super.getMinRafSupported();
        }
        for (int len = 0; len < this.mPhones.length; len++) {
            if ((this.mPhones[len].getRadioAccessFamily() & 1) == 0) {
                minRaf = this.mPhones[len].getRadioAccessFamily();
            }
        }
        mtkLogd("getMinRafSupported: minRafBit=0 minRaf=" + minRaf + " flag=" + (minRaf & 1));
        return minRaf;
    }

    protected void mtkLogd(String string) {
        Rlog.d("MtkProxyController", string);
    }

    protected void mtkLoge(String string) {
        Rlog.e("MtkProxyController", string);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        try {
            this.mPhoneSwitcher.dump(fd, pw, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isCapabilitySwitching() {
        boolean z;
        int i;
        if (this.mRildMode == 2) {
            synchronized (this.mSetRadioAccessFamilyStatus) {
                while (i < this.mPhones.length) {
                    i = (this.mSetRadioAccessFamilyStatus[i] == 2 || this.mSetRadioAccessFamilyStatus[i] == 3 || this.mSetRadioAccessFamilyStatus[i] == 4) ? 0 : i + 1;
                    mtkLogd("isCapabilitySwitching: Phone[" + i + "] status is " + this.mSetRadioAccessFamilyStatus[i]);
                    return true;
                }
                return false;
            }
        }
        synchronized (this) {
            z = this.mIsCapSwitching;
        }
        return z;
    }

    private int checkRadioCapabilitySwitchConditions(RadioAccessFamily[] rafs) {
        boolean bIsMajorPhone;
        synchronized (this) {
            this.mNextRafs = rafs;
            if (this.mIsCapSwitching) {
                mtkLogd("keep it and return,because capability swithing");
                this.mSetRafRetryCause = 2;
                return 1;
            }
            if (this.mSetRafRetryCause == 2) {
                mtkLogd("setCapability, mIsCapSwitching is not switching, can switch");
                this.mSetRafRetryCause = 0;
            }
            this.mIsCapSwitching = true;
            if (SystemProperties.getBoolean("ro.vendor.mtk_disable_cap_switch", false)) {
                this.mNextRafs = null;
                completeRadioCapabilityTransaction();
                mtkLogd("skip switching because mtk_disable_cap_switch is true");
                return 1;
            }
            if (SystemProperties.getInt("vendor.gsm.gcf.testmode", 0) == 2) {
                this.mNextRafs = null;
                completeRadioCapabilityTransaction();
                mtkLogd("skip switching because FTA mode");
                return 1;
            }
            if (SystemProperties.getInt("persist.vendor.radio.simswitch.emmode", 1) == 0) {
                this.mNextRafs = null;
                completeRadioCapabilityTransaction();
                mtkLogd("skip switching because EM disable mode");
                return 1;
            }
            if (WorldPhoneUtil.isWorldPhoneSupport()) {
                if (!WorldPhoneUtil.isWorldModeSupport()) {
                    if (ModemSwitchHandler.isModemTypeSwitching()) {
                        logd("world mode switching.");
                        if (!this.mHasRegisterWorldModeReceiver) {
                            registerWorldModeReceiverFor90Modem();
                        }
                        this.mSetRafRetryCause = 1;
                        synchronized (this) {
                            this.mIsCapSwitching = false;
                        }
                        return 2;
                    }
                } else if (this.mSetRafRetryCause == 1 && this.mHasRegisterWorldModeReceiver) {
                    unRegisterWorldModeReceiver();
                    this.mSetRafRetryCause = 0;
                }
            }
            if (TelephonyManager.getDefault().getCallState() != 0 || isEccInProgress()) {
                mtkLogd("setCapability in calling, fail to set RAT for phones");
                if (!this.mHasRegisterCallStateReceiver) {
                    registerCallStateReceiver();
                }
                this.mSetRafRetryCause = 3;
                synchronized (this) {
                    this.mIsCapSwitching = false;
                }
                return 2;
            }
            if (this.mSetRafRetryCause == 3 && this.mHasRegisterCallStateReceiver) {
                unRegisterCallStateReceiver();
                this.mSetRafRetryCause = 0;
            }
            for (int i = 0; i < this.mPhones.length; i++) {
                if (!this.mPhones[i].isRadioAvailable()) {
                    this.mSetRafRetryCause = 4;
                    this.mCi[i].registerForAvailable(this.mMtkHandler, 6, (Object) null);
                    mtkLogd("setCapability fail,Phone" + i + " is not available");
                    synchronized (this) {
                        this.mIsCapSwitching = false;
                    }
                    return 2;
                }
                if (this.mSetRafRetryCause == 4) {
                    this.mCi[i].unregisterForAvailable(this.mMtkHandler);
                    if (i == this.mPhones.length - 1) {
                        this.mSetRafRetryCause = 0;
                    }
                }
            }
            int switchStatus = Integer.valueOf(SystemProperties.get("persist.vendor.radio.simswitch", RadioCapabilitySwitchUtil.IMSI_READY)).intValue();
            int newMajorPhoneId = 0;
            boolean bIsSameRaf = true;
            boolean bIsboth3G = false;
            for (int i2 = 0; i2 < rafs.length; i2++) {
                if ((rafs[i2].getRadioAccessFamily() & 1) <= 0) {
                    bIsMajorPhone = false;
                } else {
                    bIsMajorPhone = true;
                }
                if (bIsMajorPhone) {
                    int newMajorPhoneId2 = rafs[i2].getPhoneId();
                    if (newMajorPhoneId2 == switchStatus - 1) {
                        this.mSetRafRetryCause = 0;
                        synchronized (this) {
                            RadioAccessFamily[] radioAccessFamilyArr = this.mNextRafs;
                            if (radioAccessFamilyArr == null || newMajorPhoneId2 != radioAccessFamilyArr[newMajorPhoneId2].getPhoneId()) {
                                mtkLogd("no change, skip setRadioCapability and trigger next");
                            } else {
                                this.mNextRafs = null;
                                mtkLogd("no change, skip setRadioCapability");
                            }
                        }
                        completeRadioCapabilityTransaction();
                        return 1;
                    }
                    if (bIsboth3G) {
                        mtkLogd("set more than one 3G phone, fail");
                        synchronized (this) {
                            this.mIsCapSwitching = false;
                        }
                        throw new RuntimeException("input parameter is incorrect");
                    }
                    bIsboth3G = true;
                    newMajorPhoneId = newMajorPhoneId2;
                }
                if (this.mPhones[i2].getRadioAccessFamily() != rafs[i2].getRadioAccessFamily()) {
                    bIsSameRaf = false;
                }
            }
            if (bIsSameRaf) {
                mtkLogd("setRadioCapability: Already in requested configuration, nothing to do.");
                synchronized (this) {
                    this.mIsCapSwitching = false;
                }
                return 1;
            }
            if (!bIsboth3G) {
                synchronized (this) {
                    this.mIsCapSwitching = false;
                }
                throw new RuntimeException("input parameter is incorrect - no 3g phone");
            }
            if (SystemProperties.getInt("ro.vendor.mtk_external_sim_support", 0) == 1) {
                for (int i3 = 0; i3 < this.mPhones.length; i3++) {
                    TelephonyManager.getDefault();
                    String isVsimEnabled = TelephonyManager.getTelephonyProperty(i3, "vendor.gsm.external.sim.enabled", "0");
                    TelephonyManager.getDefault();
                    String isVsimInserted = TelephonyManager.getTelephonyProperty(i3, "vendor.gsm.external.sim.inserted", "0");
                    int defaultPhoneId = SubscriptionManagerService.getInstance().getPhoneId(SubscriptionManagerService.getInstance().getDefaultDataSubId());
                    if (RadioCapabilitySwitchUtil.IMSI_READY.equals(isVsimEnabled) && (("0".equals(isVsimInserted) || "".equals(isVsimInserted)) && newMajorPhoneId != defaultPhoneId)) {
                        synchronized (this) {
                            this.mIsCapSwitching = false;
                        }
                        return 1;
                    }
                }
                int mainPhoneId = RadioCapabilitySwitchUtil.getMainCapabilityPhoneId();
                TelephonyManager.getDefault();
                String isVsimEnabledOnMain = TelephonyManager.getTelephonyProperty(mainPhoneId, "vendor.gsm.external.sim.enabled", "0");
                TelephonyManager.getDefault();
                String mainPhoneIdSimType = TelephonyManager.getTelephonyProperty(mainPhoneId, "vendor.gsm.external.sim.inserted", "0");
                int rsimPhoneId = ExternalSimManager.getPreferedRsimSlot();
                if ((isVsimEnabledOnMain.equals(RadioCapabilitySwitchUtil.IMSI_READY) && mainPhoneIdSimType.equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN)) || (rsimPhoneId != -1 && newMajorPhoneId != rsimPhoneId)) {
                    synchronized (this) {
                        this.mIsCapSwitching = false;
                    }
                    return 1;
                }
                if (SystemProperties.getInt("ro.vendor.mtk_non_dsda_rsim_support", 0) == 1 && rsimPhoneId != -1 && rsimPhoneId == newMajorPhoneId) {
                    return 0;
                }
            }
            if (RadioCapabilitySwitchUtil.isSkipCapabilitySwitch(newMajorPhoneId, this.mPhones.length, this.mContext)) {
                logd("check sim card type and skip setRadioCapability");
                this.mSetRafRetryCause = 0;
                this.mNextRafs = null;
                completeRadioCapabilityTransaction();
                return 1;
            }
            if (!WorldPhoneUtil.isWorldModeSupport() && WorldPhoneUtil.isWorldPhoneSupport()) {
                WorldPhoneUtil.getWorldPhone().notifyRadioCapabilityChange(newMajorPhoneId);
            }
            mtkLogd("checkRadioCapabilitySwitchConditions, do switch");
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRetryWhenRadioAvailable(Message msg) {
        mtkLogd("onRetryWhenRadioAvailable,mSetRafRetryCause:" + this.mSetRafRetryCause);
        for (int i = 0; i < this.mPhones.length; i++) {
            if (RadioManager.isModemPowerOff(i)) {
                mtkLogd("onRetryWhenRadioAvailable, Phone" + i + " modem off");
                return;
            }
        }
        RadioAccessFamily[] radioAccessFamilyArr = this.mNextRafs;
        if (radioAccessFamilyArr != null && this.mSetRafRetryCause == 4) {
            try {
                setRadioCapability(radioAccessFamilyArr);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCapabilityFailBroadcast() {
        if (this.mContext != null) {
            Intent intent = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private void registerWorldModeReceiverFor90Modem() {
        if (this.mContext == null) {
            logd("registerWorldModeReceiverFor90Modem, context is null => return");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ModemSwitchHandler.ACTION_MODEM_SWITCH_DONE);
        this.mContext.registerReceiver(this.mWorldModeReceiver, filter, 2);
        this.mHasRegisterWorldModeReceiver = true;
    }

    private void unRegisterWorldModeReceiver() {
        if (this.mContext == null) {
            mtkLogd("unRegisterWorldModeReceiver, context is null => return");
        } else {
            this.mContext.unregisterReceiver(this.mWorldModeReceiver);
            this.mHasRegisterWorldModeReceiver = false;
        }
    }

    private void registerCallStateReceiver() {
        if (this.mContext == null) {
            mtkLogd("registerCallStateReceiver, context is null => return");
            return;
        }
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
        this.mContext.registerReceiver(this.mCallStateReceiver, filter);
        this.mHasRegisterCallStateReceiver = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterCallStateReceiver() {
        if (this.mContext == null) {
            mtkLogd("unRegisterCallStateReceiver, context is null => return");
        } else {
            this.mContext.unregisterReceiver(this.mCallStateReceiver);
            this.mHasRegisterCallStateReceiver = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEccInProgress() {
        String value = SystemProperties.get("ril.cdma.inecmmode", "");
        boolean inEcm = value.contains("true");
        boolean isInEcc = false;
        ITelecomService tm = ITelecomService.Stub.asInterface(ServiceManager.getService("telecom"));
        if (tm != null) {
            try {
                isInEcc = tm.isInEmergencyCall();
            } catch (RemoteException e) {
                loge("Exception of isEccInProgress");
            }
        }
        logd("isEccInProgress, value:" + value + ", inEcm:" + inEcm + ", isInEcc:" + isInEcc);
        return inEcm || isInEcc;
    }
}
