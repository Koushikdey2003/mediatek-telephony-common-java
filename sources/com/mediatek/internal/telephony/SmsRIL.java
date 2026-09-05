package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.WorkSource;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.util.SparseArray;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.HalVersion;
import java.util.concurrent.atomic.AtomicLong;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms;

/* JADX INFO: loaded from: classes.dex */
public class SmsRIL {
    private static final int BASE = 0;
    private static final int EVENT_RADIO_PROXY_DEAD = 1;
    private static final int IRADIO_GET_SERVICE_DELAY_MILLIS = 4000;
    private static final String LOG_TAG = "SmsRIL";
    private static final int RADIO_NOT_AVAILABLE = 1;
    static final boolean RILJ_LOGD = true;
    private static final int RIL_REQUEST_SET_GEO_LOCATION = 2120;
    private int mPhoneId;
    protected WorkSource mRILDefaultWorkSource;
    private static final String[] IMS_HIDL_SERVICE_NAME = {"imsSlot1", "imsSlot2", "imsSlot3", "imsSlot4"};
    private static final String[] IMS_AIDL_SERVICE_NAME = {"slot1", "slot2", "slot3", "slot4"};
    public static final HalVersion RADIO_HAL_VERSION_MTK_UNKNOWN = HalVersion.UNKNOWN;
    public static final HalVersion RADIO_HAL_VERSION_MTK_2_0 = new HalVersion(2, 0);
    public static final HalVersion RADIO_HAL_VERSION_MTK_3_0 = new HalVersion(3, 0);
    public static final HalVersion RADIO_HAL_VERSION_MTK_4_0 = new HalVersion(4, 0);
    protected HalVersion mRadioVersionMtk = RADIO_HAL_VERSION_MTK_UNKNOWN;
    volatile IMtkRadioEx mRadioProxyMtk = null;
    private SparseArray<RILRequest> mRequestList = new SparseArray<>();
    private Handler mHandler = new Handler() { // from class: com.mediatek.internal.telephony.SmsRIL.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (((Long) msg.obj).longValue() == SmsRIL.this.mRadioProxyCookie.get()) {
                        SmsRIL.this.resetProxyAndRequestList();
                    }
                    break;
                default:
                    Rlog.d(SmsRIL.LOG_TAG, "Unhandled message with number: " + msg.what);
                    break;
            }
        }
    };
    private final SmsRadioProxyDeathRecipient mSmsRadioProxyDeathRecipient = new SmsRadioProxyDeathRecipient();
    private final BinderServiceDeathRecipient mBinderServiceDeathRecipient = new BinderServiceDeathRecipient();
    private final AtomicLong mRadioProxyCookie = new AtomicLong(0);
    private MtkRadioExSmsProxy mRadioExSmsProxy = new MtkRadioExSmsProxy();

    public SmsRIL(Context context, int phoneId) {
        this.mPhoneId = phoneId;
        this.mRILDefaultWorkSource = new WorkSource(context.getApplicationInfo().uid, context.getPackageName());
        getMtkRadioExSmsProxy(null);
    }

    final class SmsRadioProxyDeathRecipient implements IHwBinder.DeathRecipient {
        SmsRadioProxyDeathRecipient() {
        }

        public void serviceDied(long cookie) {
            SmsRIL.this.mHandler.sendMessage(SmsRIL.this.mHandler.obtainMessage(1, Long.valueOf(cookie)));
        }
    }

    final class BinderServiceDeathRecipient implements IBinder.DeathRecipient {
        private IBinder mBinder;

        BinderServiceDeathRecipient() {
        }

        public void linkToDeath(IBinder service) throws RemoteException {
            if (service != null) {
                this.mBinder = service;
                service.linkToDeath(this, (int) SmsRIL.this.mRadioProxyCookie.incrementAndGet());
            }
        }

        public synchronized void unlinkToDeath() {
            IBinder iBinder = this.mBinder;
            if (iBinder != null) {
                iBinder.unlinkToDeath(this, 0);
                this.mBinder = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            SmsRIL.this.mHandler.sendMessage(SmsRIL.this.mHandler.obtainMessage(1, Long.valueOf(SmsRIL.this.mRadioProxyCookie.get())));
            unlinkToDeath();
        }
    }

    public synchronized MtkRadioExSmsProxy getMtkRadioExSmsProxy(Message result) {
        IBinder binder;
        if (!SubscriptionManager.isValidPhoneId(this.mPhoneId)) {
            return null;
        }
        if (!this.mRadioExSmsProxy.isEmpty()) {
            return this.mRadioExSmsProxy;
        }
        try {
            String serviceName = IMtkRadioExIms.DESCRIPTOR + "/" + IMS_AIDL_SERVICE_NAME[this.mPhoneId];
            boolean isDeclared = ServiceManager.isDeclared(serviceName);
            if (isDeclared && (binder = ServiceManager.checkService(serviceName)) != null) {
                HalVersion halVersion = RADIO_HAL_VERSION_MTK_4_0;
                this.mRadioVersionMtk = halVersion;
                this.mRadioExSmsProxy.setAidl(halVersion, IMtkRadioExIms.Stub.asInterface(binder));
            }
            if (!this.mRadioExSmsProxy.isEmpty()) {
                if (this.mRadioExSmsProxy.isAidl()) {
                    this.mBinderServiceDeathRecipient.linkToDeath(this.mRadioExSmsProxy.getAidl().asBinder());
                }
            } else {
                try {
                    this.mRadioProxyMtk = IMtkRadioEx.getService(IMS_HIDL_SERVICE_NAME[this.mPhoneId], false);
                } catch (RemoteException | RuntimeException e) {
                    this.mRadioProxyMtk = null;
                    Rlog.e(LOG_TAG, "getMtkRadioExSmsProxy getServiceV3_0: " + e);
                }
                if (this.mRadioProxyMtk != null) {
                    try {
                        this.mRadioProxyMtk = IMtkRadioEx.castFrom(this.mRadioProxyMtk);
                        HalVersion halVersion2 = RADIO_HAL_VERSION_MTK_3_0;
                        this.mRadioVersionMtk = halVersion2;
                        this.mRadioExSmsProxy.setHidl(halVersion2, this.mRadioProxyMtk);
                        this.mRadioProxyMtk.linkToDeath(this.mSmsRadioProxyDeathRecipient, this.mRadioProxyCookie.incrementAndGet());
                    } catch (RemoteException | RuntimeException e2) {
                        this.mRadioProxyMtk = null;
                    }
                }
            }
        } catch (RemoteException e3) {
            this.mRadioExSmsProxy.clear();
            Rlog.e(LOG_TAG, "getMtkRadioExSmsProxy : " + e3);
        }
        if (this.mRadioExSmsProxy.isEmpty()) {
            Rlog.e(LOG_TAG, "getMtkRadioExSmsProxy: is empty");
            if (result != null) {
                AsyncResult.forMessage(result, (Object) null, CommandException.fromRilErrno(1));
                result.sendToTarget();
            }
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(1, Long.valueOf(this.mRadioProxyCookie.get())), 4000L);
        }
        return this.mRadioExSmsProxy;
    }

    private void clearRequestList(int error) {
        synchronized (this.mRequestList) {
            int count = this.mRequestList.size();
            for (int i = 0; i < count; i++) {
                RILRequest rr = this.mRequestList.valueAt(i);
                rr.onError(error, null);
                rr.release();
            }
            this.mRequestList.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetProxyAndRequestList() {
        Rlog.d(LOG_TAG, "resetProxyAndRequestList");
        this.mRadioProxyMtk = null;
        this.mRadioExSmsProxy.clear();
        this.mRadioProxyCookie.incrementAndGet();
        RILRequest.resetSerial();
        clearRequestList(1);
        getMtkRadioExSmsProxy(null);
    }

    public void setLocationInfo(String accountId, String broadcastFlag, String latitude, String longitude, String accuracy, String method, String city, String state, String zip, String countryCode, String ueWlanMac, String confidence, String altitude, String majorAxisAccuracy, String minorAxisAccuracy, String vericalAxisAccuracy, Message result) {
        RILRequest rr;
        MtkRadioExSmsProxy smsProxy = getMtkRadioExSmsProxy(result);
        if (smsProxy != null && !smsProxy.isEmpty()) {
            RILRequest rr2 = obtainRequest(RIL_REQUEST_SET_GEO_LOCATION, result, this.mRILDefaultWorkSource);
            Rlog.d(LOG_TAG, rr2.serialString() + "> " + requestToString(rr2.mRequest) + " accountId:" + accountId + " broadcastFlag:" + broadcastFlag + " accuracy:" + accuracy + " method:" + method);
            try {
                rr = rr2;
                try {
                    smsProxy.setLocationInfo(rr2.mSerial, accountId, broadcastFlag, latitude, longitude, accuracy, method, city, state, zip, countryCode, ueWlanMac, confidence, altitude, majorAxisAccuracy, minorAxisAccuracy, vericalAxisAccuracy);
                } catch (RemoteException | RuntimeException e) {
                    resetProxyAndRequestList();
                }
            } catch (RemoteException | RuntimeException e2) {
                rr = rr2;
            }
            if (rr != null) {
                findAndRemoveRequestFromList(rr.mSerial);
                rr.release();
            }
        }
    }

    private RILRequest findAndRemoveRequestFromList(int serial) {
        RILRequest rr;
        synchronized (this.mRequestList) {
            rr = this.mRequestList.get(serial);
            if (rr != null) {
                this.mRequestList.remove(serial);
            }
        }
        return rr;
    }

    private RILRequest obtainRequest(int request, Message result, WorkSource workSource) {
        RILRequest rr = RILRequest.obtain(request, result, workSource);
        synchronized (this.mRequestList) {
            this.mRequestList.append(rr.mSerial, rr);
        }
        return rr;
    }

    private String requestToString(int request) {
        switch (request) {
            case RIL_REQUEST_SET_GEO_LOCATION /* 2120 */:
                return "RIL_REQUEST_SET_GEO_LOCATION";
            default:
                return "<unknown request>";
        }
    }
}
