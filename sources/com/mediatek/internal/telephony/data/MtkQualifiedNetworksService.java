package com.mediatek.internal.telephony.data;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.data.QualifiedNetworksService;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.mediatek.internal.telephony.MtkRIL;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkQualifiedNetworksService extends QualifiedNetworksService {
    private static final String TAG = MtkQualifiedNetworksService.class.getSimpleName();
    private static final int UPDATE_QUALIFIED_NETWORKS = 1;

    public class MtkNetworkAvailabilityProvider extends QualifiedNetworksService.NetworkAvailabilityProvider {
        private static final boolean DBG = true;
        private static final int MD_NW_TYPE_CELLULAR = 1;
        private static final int MD_NW_TYPE_IWLAN = 2;
        private static final int MD_NW_TYPE_UNKNOWN = 0;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Looper mLooper;
        private final Phone mPhone;

        public MtkNetworkAvailabilityProvider(int slotIndex) {
            super(MtkQualifiedNetworksService.this, slotIndex);
            Phone phone = PhoneFactory.getPhone(getSlotIndex());
            this.mPhone = phone;
            HandlerThread handlerThread = new HandlerThread(MtkQualifiedNetworksService.class.getSimpleName());
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            this.mLooper = looper;
            Handler handler = new Handler(looper) { // from class: com.mediatek.internal.telephony.data.MtkQualifiedNetworksService.MtkNetworkAvailabilityProvider.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    AsyncResult ar = (AsyncResult) message.obj;
                    switch (message.what) {
                        case 1:
                            int[] availabilityUpdate = (int[]) ar.result;
                            int length = availabilityUpdate.length;
                            List<Integer> qualifiedNetworkTypes = new ArrayList<>();
                            int mode = availabilityUpdate[0];
                            int apnTypes = availabilityUpdate[1];
                            MtkQualifiedNetworksService.this.log("UPDATE_QUALIFIED_NETWORKS mode=" + mode + " apnTypes=" + apnTypes);
                            for (int i = 2; i < length; i++) {
                                qualifiedNetworkTypes.add(Integer.valueOf(MtkNetworkAvailabilityProvider.this.converNetworkType(availabilityUpdate[i])));
                                MtkQualifiedNetworksService.this.log("availabilityUpdate[" + i + "]=" + MtkNetworkAvailabilityProvider.this.converNetworkType(availabilityUpdate[i]));
                            }
                            MtkNetworkAvailabilityProvider.this.updateQualifiedNetworkTypes(apnTypes, qualifiedNetworkTypes);
                            break;
                        default:
                            MtkQualifiedNetworksService.this.loge("Unexpected event: " + message.what);
                            break;
                    }
                }
            };
            this.mHandler = handler;
            MtkQualifiedNetworksService.this.log("Register for qualified networks changed.");
            if (phone.mCi instanceof MtkRIL) {
                phone.mCi.registerForQualifiedNetworkTypesChanged(handler, 1, null);
            } else {
                MtkQualifiedNetworksService.this.loge("Constructor: mPhone.mCi is not instanceof MTKRIL!");
            }
        }

        public void close() {
            if (this.mPhone.mCi instanceof MtkRIL) {
                this.mPhone.mCi.unregisterForQualifiedNetworkTypesChanged(this.mHandler);
            } else {
                MtkQualifiedNetworksService.this.loge("close(): mPhone.mCi is not instanceof MTKRIL!");
            }
            this.mHandlerThread.quit();
        }

        public int converNetworkType(int mdReportType) {
            switch (mdReportType) {
                case 1:
                    return 3;
                case 2:
                    return 5;
                default:
                    return 0;
            }
        }
    }

    public QualifiedNetworksService.NetworkAvailabilityProvider onCreateNetworkAvailabilityProvider(int slotIndex) {
        log("MtkQNS create MtkNetworkAvailabilityProvider for slot " + slotIndex);
        if (!SubscriptionManager.isValidSlotIndex(slotIndex)) {
            loge("Tried to cellular data service with invalid slotId " + slotIndex);
            return null;
        }
        return new MtkNetworkAvailabilityProvider(slotIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String s) {
        Rlog.d(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String s) {
        Rlog.e(TAG, s);
    }
}
