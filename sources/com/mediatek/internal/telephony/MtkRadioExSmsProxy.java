package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.telephony.Rlog;
import com.android.internal.telephony.HalVersion;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.ims.IMtkRadioExIms;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExSmsProxy extends MtkRadioExServiceProxy {
    private static final String TAG = "MtkRadioExSmsProxy";
    private volatile IMtkRadioExIms mImsProxyMtk = null;

    public void setAidl(HalVersion halVersion, IMtkRadioExIms ims) {
        this.mHalVersion = halVersion;
        this.mImsProxyMtk = ims;
        this.mIsAidl = true;
        try {
            int version = ims.getInterfaceVersion();
            HalVersion newHalVersion = SmsRIL.RADIO_HAL_VERSION_MTK_4_0;
            Rlog.d(TAG, "AIDL version=" + version + ", halVersion=" + newHalVersion);
            if (this.mHalVersion.less(newHalVersion)) {
                this.mHalVersion = newHalVersion;
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setAidl: " + e);
        }
    }

    public IMtkRadioExIms getAidl() {
        return this.mImsProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = SmsRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mImsProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mImsProxyMtk == null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mImsProxyMtk.responseAcknowledgementMtk();
        } else if (this.mHalVersion.greaterOrEqual(SmsRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        }
    }

    public void setLocationInfo(int serial, String accountId, String broadcastFlag, String latitude, String longitude, String accuracy, String method, String city, String state, String zip, String countryCode, String ueWlanMac, String confidence, String altitude, String majorAxisAccuracy, String minorAxisAccuracy, String vericalAxisAccuracy) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (!isAidl()) {
            ArrayList<String> list = new ArrayList<>();
            list.add(convertNullToEmptyString(accountId));
            list.add(convertNullToEmptyString(broadcastFlag));
            list.add(convertNullToEmptyString(latitude));
            list.add(convertNullToEmptyString(longitude));
            list.add(convertNullToEmptyString(accuracy));
            list.add(convertNullToEmptyString(method));
            list.add(convertNullToEmptyString(city));
            list.add(convertNullToEmptyString(state));
            list.add(convertNullToEmptyString(zip));
            list.add(convertNullToEmptyString(countryCode));
            list.add(convertNullToEmptyString(ueWlanMac));
            list.add(convertNullToEmptyString(confidence));
            list.add(convertNullToEmptyString(altitude));
            list.add(convertNullToEmptyString(majorAxisAccuracy));
            list.add(convertNullToEmptyString(minorAxisAccuracy));
            list.add(convertNullToEmptyString(vericalAxisAccuracy));
            if (this.mHalVersion.greaterOrEqual(SmsRIL.RADIO_HAL_VERSION_MTK_3_0)) {
                ((IMtkRadioEx) this.mRadioProxyMtk).setLocationInfo(serial, list);
                return;
            }
            return;
        }
        this.mImsProxyMtk.setLocationInfo(serial, new String[]{convertNullToEmptyString(accountId), convertNullToEmptyString(broadcastFlag), convertNullToEmptyString(latitude), convertNullToEmptyString(longitude), convertNullToEmptyString(accuracy), convertNullToEmptyString(method), convertNullToEmptyString(city), convertNullToEmptyString(state), convertNullToEmptyString(zip), convertNullToEmptyString(countryCode), convertNullToEmptyString(ueWlanMac), convertNullToEmptyString(confidence), convertNullToEmptyString(altitude), convertNullToEmptyString(majorAxisAccuracy), convertNullToEmptyString(minorAxisAccuracy), convertNullToEmptyString(vericalAxisAccuracy)}, 0);
    }

    protected String convertNullToEmptyString(String string) {
        return string != null ? string : "";
    }
}
