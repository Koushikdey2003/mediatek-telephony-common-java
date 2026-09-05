package com.mediatek.internal.telephony;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.ServiceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccPort;
import com.android.internal.telephony.uicc.UsimServiceTable;
import com.mediatek.internal.telephony.IMtkPhoneSubInfoEx;

/* JADX INFO: loaded from: classes.dex */
public class MtkPhoneSubInfoControllerEx extends IMtkPhoneSubInfoEx.Stub {
    private static final boolean DBG = true;
    private static final String TAG = "MtkPhoneSubInfoCtlEx";
    private static final boolean VDBG = false;
    private final AppOpsManager mAppOps;
    private final Context mContext;
    private final Phone[] mPhone;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkPhoneSubInfoControllerEx(Context context, Phone[] phone) {
        this.mPhone = phone;
        if (ServiceManager.getService("iphonesubinfoEx") == null) {
            ServiceManager.addService("iphonesubinfoEx", this);
        }
        this.mContext = context;
        this.mAppOps = (AppOpsManager) context.getSystemService("appops");
    }

    private void log(String s) {
        Rlog.d(TAG, s);
    }

    private void loge(String s) {
        Rlog.e(TAG, s);
    }

    private boolean checkReadPhoneState(String callingPackage, String message) {
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", message);
            return DBG;
        } catch (SecurityException e) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", message);
            if (this.mAppOps.noteOp(51, Binder.getCallingUid(), callingPackage) == 0) {
                return DBG;
            }
            return false;
        }
    }

    private int getDefaultSubscription() {
        return PhoneFactory.getDefaultSubscription();
    }

    private Phone getPhone(int subId) {
        int phoneId = SubscriptionManager.getPhoneId(subId);
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            phoneId = 0;
        }
        return this.mPhone[phoneId];
    }

    private IccRecords getIccRecords(int subId) {
        UiccPort uiccPort;
        UiccCardApplication uiccApp;
        Phone phone = getPhone(subId);
        if (phone != null) {
            UiccCard uiccCard = phone.getUiccCard();
            if (uiccCard == null) {
                uiccPort = null;
            } else {
                uiccPort = uiccCard.getUiccPortForPhone(phone.getPhoneId());
            }
            if (uiccPort == null) {
                uiccApp = null;
            } else {
                uiccApp = uiccPort.getApplication(1);
            }
            if (uiccApp == null) {
                return null;
            }
            IccRecords iccRecords = uiccApp.getIccRecords();
            return iccRecords;
        }
        loge("getIccRecords phone is null for Subscription:" + subId);
        return null;
    }

    public boolean getUsimService(int service, String callingPackage) {
        return getUsimServiceForSubscriber(getDefaultSubscription(), service, callingPackage);
    }

    public boolean getUsimServiceForSubscriber(int subId, int service, String callingPackage) {
        Phone phone = getPhone(subId);
        if (phone != null) {
            if (!checkReadPhoneState(callingPackage, "getUsimService")) {
                return false;
            }
            UsimServiceTable ust = phone.getUsimServiceTable();
            if (ust != null) {
                return ust.isAvailable(service);
            }
            log("getUsimService fail due to UST is null.");
            return false;
        }
        loge("getUsimService phone is null for Subscription:" + subId);
        return false;
    }

    public int getMncLength() {
        return getMncLengthForSubscriber(getDefaultSubscription());
    }

    public int getMncLengthForSubscriber(int subId) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
        IccRecords iccRecords = getIccRecords(subId);
        if (iccRecords != null) {
            return iccRecords.getMncLength();
        }
        loge("getMncLength iccRecords is null for Subscription:" + subId);
        return 0;
    }

    public String getIsimImpiForSubscriber(int subId) {
        Phone phone = getPhone(subId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
        if (phone != null) {
            IsimRecords isim = phone.getIsimRecords();
            if (isim == null) {
                return null;
            }
            return isim.getIsimImpi();
        }
        loge("getIsimImpi phone is null for Subscription:" + subId);
        return null;
    }

    public String getIsimDomainForSubscriber(int subId) {
        Phone phone = getPhone(subId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
        if (phone != null) {
            IsimRecords isim = phone.getIsimRecords();
            if (isim == null) {
                return null;
            }
            return isim.getIsimDomain();
        }
        loge("getIsimDomain phone is null for Subscription:" + subId);
        return null;
    }

    public String[] getIsimImpuForSubscriber(int subId) {
        Phone phone = getPhone(subId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
        if (phone != null) {
            IsimRecords isim = phone.getIsimRecords();
            if (isim == null) {
                return null;
            }
            return isim.getIsimImpu();
        }
        loge("getIsimImpu phone is null for Subscription:" + subId);
        return null;
    }

    public String getIsimIstForSubscriber(int subId) {
        Phone phone = getPhone(subId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
        if (phone != null) {
            IsimRecords isim = phone.getIsimRecords();
            if (isim == null) {
                return null;
            }
            return isim.getIsimIst();
        }
        loge("getIsimIst phone is null for Subscription:" + subId);
        return null;
    }

    public String[] getIsimPcscfForSubscriber(int subId) {
        Phone phone = getPhone(subId);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "Requires READ_PRIVILEGED_PHONE_STATE");
        if (phone != null) {
            IsimRecords isim = phone.getIsimRecords();
            if (isim == null) {
                return null;
            }
            return isim.getIsimPcscf();
        }
        loge("getIsimPcscf phone is null for Subscription:" + subId);
        return null;
    }

    public String getLine1PhoneNumberForSubscriber(int subId, String callingPackage, String callingFeatureId) {
        MtkGsmCdmaPhone phone = getPhone(subId);
        if (phone != null) {
            if (!TelephonyPermissions.checkCallingOrSelfReadPhoneNumber(this.mContext, subId, callingPackage, callingFeatureId, "getLine1PhoneNumber")) {
                loge("getLine1PhoneNumber permission check fail:" + subId);
                return null;
            }
            return phone.getLine1PhoneNumber();
        }
        loge("getLine1PhoneNumber phone is null for Subscription:" + subId);
        return null;
    }
}
