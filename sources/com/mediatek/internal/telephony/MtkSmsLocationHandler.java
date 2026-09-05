package com.mediatek.internal.telephony;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LastLocationRequest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PackageTagsList;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsLocationHandler extends Handler {
    private static final int BASE = 3000;
    private static final int BROADCAST_FLAG_ENABLE = 1;
    private static final String COUNTRY_CODE_HK = "HK";
    private static final int DEFAULT_CONFIDENCE_LEVEL = 68;
    private static final int EVENT_GET_LAST_KNOWN_LOCATION = 3001;
    private static final int EVENT_HANDLE_LAST_KNOWN_LOCATION_RESPONSE = 3003;
    private static final int EVENT_HANDLE_LOCATION_RESPONSE = 3002;
    private static final int EVENT_SET_LOCATION_INFO = 3004;
    public static final String MTK_KEY_SMS_SUPPORTS_T911_LOCATION = "mtk_carrier_sms_supports_t911_location_bool";
    public static final String MTK_KEY_SMS_TEST_EMERGENCY_NUMBER = "mtk_carrier_sms_test_emergency_number_string";
    public static final String MTK_KEY_WFC_GET_CONFIDENCE_LEVEL = "mtk_carrier_wfc_get_confidence_level";
    private static final int NA_CONFIDENCE_LEVEL = 90;
    private static final String TAG = "MtkSmsLocationHandler";
    private CarrierConfigManager mConfigManager;
    private Context mContext;
    private final ThreadPoolExecutor mExecutor;
    private Geocoder mGeoCoder;
    private ArrayList<LocationInfo> mLocationInfoQueue;
    private LocationListenerImp mLocationListener;
    private LocationManager mLocationManager;
    private boolean mLocationRequestRegistered;
    private ArrayList<LocationInfo> mLocationTasks;
    private boolean mLocationTimeout;
    private Object mLocationTimeoutLock;
    private AtomicInteger mNumber;
    private String mPlmnCountryCode;
    private int mSimCount;
    private SmsRIL[] mSmsRil;
    private static final boolean ENGLOAD = "eng".equals(Build.TYPE);
    private static MtkSmsLocationHandler sInstance = null;
    private static final float[] STANDARD_NORMAL_DISTRIBUTION_TABLE = {0.5f, 0.5398f, 0.5793f, 0.6179f, 0.6554f, 0.6915f, 0.7257f, 0.758f, 0.7881f, 0.8159f, 0.8413f, 0.8643f, 0.8849f, 0.9032f, 0.9192f, 0.9332f, 0.9452f, 0.9554f, 0.9641f, 0.9713f, 0.9772f, 0.9821f, 0.9861f, 0.9893f, 0.9918f, 0.9938f, 0.9953f, 0.9965f, 0.9974f, 0.9981f, 0.9987f, 1.0f};
    private static int REQUEST_GEOLOCATION_TIMEOUT = 55000;
    private static int LOCATION_UPDATE_TIME = 1000;

    public static MtkSmsLocationHandler getInstance() {
        MtkSmsLocationHandler mtkSmsLocationHandler;
        synchronized (MtkSmsLocationHandler.class) {
            mtkSmsLocationHandler = sInstance;
        }
        return mtkSmsLocationHandler;
    }

    public static MtkSmsLocationHandler init(Context context, Looper looper) {
        MtkSmsLocationHandler mtkSmsLocationHandler;
        synchronized (MtkSmsLocationHandler.class) {
            if (sInstance == null) {
                sInstance = new MtkSmsLocationHandler(context, looper);
            }
            mtkSmsLocationHandler = sInstance;
        }
        return mtkSmsLocationHandler;
    }

    public MtkSmsLocationHandler(Context context, Looper looper) {
        super(looper);
        this.mLocationListener = new LocationListenerImp();
        this.mLocationRequestRegistered = false;
        this.mLocationInfoQueue = new ArrayList<>();
        this.mPlmnCountryCode = "";
        this.mLocationTimeout = false;
        this.mLocationTimeoutLock = new Object();
        this.mLocationTasks = new ArrayList<>();
        this.mNumber = new AtomicInteger();
        this.mExecutor = new ThreadPoolExecutor(1, 2, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue(10), new ThreadFactory() { // from class: com.mediatek.internal.telephony.MtkSmsLocationHandler.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                String threadName = "Geo coder - " + MtkSmsLocationHandler.this.mNumber.getAndIncrement();
                Rlog.i(MtkSmsLocationHandler.TAG, "create thread name:" + threadName);
                return new Thread(r, threadName);
            }
        });
        this.mContext = context;
        this.mGeoCoder = new Geocoder(this.mContext, Locale.US);
        this.mLocationManager = (LocationManager) this.mContext.getSystemService("location");
        this.mSimCount = TelephonyManager.getDefault().getSimCount();
        Rlog.d(TAG, "MtkSmsLocationHandler mSimCount = " + this.mSimCount);
        this.mSmsRil = new SmsRIL[this.mSimCount];
        for (int i = 0; i < this.mSimCount; i++) {
            this.mSmsRil[i] = new SmsRIL(context, i);
        }
        this.mConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
    }

    public MtkSmsLocationHandler(Context context) {
        this.mLocationListener = new LocationListenerImp();
        this.mLocationRequestRegistered = false;
        this.mLocationInfoQueue = new ArrayList<>();
        this.mPlmnCountryCode = "";
        this.mLocationTimeout = false;
        this.mLocationTimeoutLock = new Object();
        this.mLocationTasks = new ArrayList<>();
        this.mNumber = new AtomicInteger();
        this.mExecutor = new ThreadPoolExecutor(1, 2, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue(10), new ThreadFactory() { // from class: com.mediatek.internal.telephony.MtkSmsLocationHandler.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                String threadName = "Geo coder - " + MtkSmsLocationHandler.this.mNumber.getAndIncrement();
                Rlog.i(MtkSmsLocationHandler.TAG, "create thread name:" + threadName);
                return new Thread(r, threadName);
            }
        });
        this.mContext = context;
    }

    private class LocationListenerImp implements LocationListener {
        private LocationListenerImp() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            MtkSmsLocationHandler.this.log("onLocationChanged: " + location);
            long newFlpTime = location.getTime();
            Rlog.d(MtkSmsLocationHandler.TAG, "onLocationChanged newFlpTime: " + newFlpTime);
            for (LocationInfo locationInfo : MtkSmsLocationHandler.this.mLocationInfoQueue) {
                MtkSmsLocationHandler.this.log("onLocationChanged locationInfo time: " + locationInfo.mTime);
                if (locationInfo.mTime == newFlpTime) {
                    MtkSmsLocationHandler.this.log("onLocationChanged isCache: true");
                }
            }
            MtkSmsLocationHandler.this.cancelLocationRequest();
            synchronized (MtkSmsLocationHandler.this.mLocationTimeoutLock) {
                MtkSmsLocationHandler.this.mLocationTimeout = false;
            }
            MtkSmsLocationHandler.this.removeMessages(MtkSmsLocationHandler.EVENT_GET_LAST_KNOWN_LOCATION);
            MtkSmsLocationHandler.this.obtainMessage(MtkSmsLocationHandler.EVENT_HANDLE_LOCATION_RESPONSE, 0, 0, location).sendToTarget();
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String provider) {
            MtkSmsLocationHandler.this.log("onProviderDisabled: " + provider);
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String provider) {
            MtkSmsLocationHandler.this.log("onProviderEnabled: " + provider);
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String provider, int status, Bundle extras) {
            MtkSmsLocationHandler.this.log("onStatusChanged: " + provider + ", status=" + status);
        }
    }

    public class LocationInfo {
        int mAccountId;
        float mAccuracy;
        int mBroadcastFlag;
        double mLatitude;
        double mLongitude;
        float mMajorAxisAccuracy;
        float mMinorAxisAccuracy;
        int mSimIdx;
        String mMethod = "";
        String mCity = "";
        String mState = "";
        String mZip = "";
        String mCountryCode = "";
        int mConfidence = 0;
        double mAltitude = 0.0d;
        float mVericalAxisAccuracy = 0.0f;
        long mTime = 0;

        public LocationInfo(int simIdx, int accountId, int broadcastFlag, double latitude, double longitude, float accuracy) {
            this.mSimIdx = simIdx;
            this.mAccountId = accountId;
            this.mBroadcastFlag = broadcastFlag;
            this.mLatitude = latitude;
            this.mLongitude = longitude;
            this.mAccuracy = accuracy;
            this.mMajorAxisAccuracy = accuracy;
            this.mMinorAxisAccuracy = accuracy;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[LocationInfo objId: ");
            sb.append(System.identityHashCode(this));
            sb.append(", phoneId: " + this.mSimIdx);
            sb.append(", transationId: " + this.mAccountId);
            sb.append(", accuracy: " + this.mAccuracy);
            sb.append(", confidence: " + this.mConfidence);
            sb.append(", vericalAxisAccuracy: " + this.mVericalAxisAccuracy);
            sb.append(", broadcastFlag: " + this.mBroadcastFlag);
            sb.append(", method: " + this.mMethod);
            sb.append(", city: " + MtkSmsLocationHandler.this.maskString(this.mCity));
            sb.append(", state: " + MtkSmsLocationHandler.this.maskString(this.mState));
            sb.append(", zip: " + MtkSmsLocationHandler.this.maskString(this.mZip));
            sb.append(", countryCode: " + MtkSmsLocationHandler.this.maskString(this.mCountryCode));
            sb.append(", time: " + this.mTime);
            return sb.toString();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        Rlog.d(TAG, "handleMessage: msg= " + messageToString(msg));
        switch (msg.what) {
            case EVENT_GET_LAST_KNOWN_LOCATION /* 3001 */:
                synchronized (this.mLocationTimeoutLock) {
                    this.mLocationTimeout = true;
                    break;
                }
                getLastKnownLocation();
                cancelLocationRequest();
                return;
            case EVENT_HANDLE_LOCATION_RESPONSE /* 3002 */:
            case EVENT_HANDLE_LAST_KNOWN_LOCATION_RESPONSE /* 3003 */:
                Location location = (Location) msg.obj;
                handleLocationUpdate(location);
                return;
            case EVENT_SET_LOCATION_INFO /* 3004 */:
                LocationInfo locationInfo = (LocationInfo) msg.obj;
                setLocationInfo(locationInfo);
                return;
            default:
                return;
        }
    }

    private String messageToString(Message msg) {
        switch (msg.what) {
            case EVENT_GET_LAST_KNOWN_LOCATION /* 3001 */:
                return "EVENT_GET_LAST_KNOWN_LOCATION";
            case EVENT_HANDLE_LOCATION_RESPONSE /* 3002 */:
                return "EVENT_HANDLE_LOCATION_RESPONSE";
            case EVENT_HANDLE_LAST_KNOWN_LOCATION_RESPONSE /* 3003 */:
                return "EVENT_HANDLE_LAST_KNOWN_LOCATION_RESPONSE";
            case EVENT_SET_LOCATION_INFO /* 3004 */:
                return "EVENT_SET_LOCATION_INFO";
            default:
                return "UNKNOWN";
        }
    }

    private void handleLocationUpdate(Location location) {
        if (location == null) {
            Rlog.d(TAG, "location get null, unexpected result");
            return;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double altitude = location.getAltitude();
        float accuracy = location.getAccuracy();
        float verticalAccuracy = location.getVerticalAccuracyMeters();
        long time = location.getTime();
        log("update all LocationInfo with  time: " + time + " accuracy: " + accuracy + " altitude: " + altitude + " verticalAccuracy: " + verticalAccuracy);
        ArrayList<LocationInfo> duplicatedInfo = new ArrayList<>();
        for (LocationInfo locationInfo : this.mLocationTasks) {
            locationInfo.mLatitude = latitude;
            locationInfo.mLongitude = longitude;
            locationInfo.mAltitude = altitude;
            locationInfo.mAccuracy = accuracy;
            locationInfo.mMajorAxisAccuracy = accuracy;
            locationInfo.mMinorAxisAccuracy = accuracy;
            locationInfo.mVericalAxisAccuracy = verticalAccuracy;
            locationInfo.mTime = time;
            duplicatedInfo.clear();
            for (LocationInfo gpsLocationInfo : this.mLocationInfoQueue) {
                double latitude2 = latitude;
                if (gpsLocationInfo.mAccountId == locationInfo.mAccountId) {
                    duplicatedInfo.add(gpsLocationInfo);
                }
                latitude = latitude2;
            }
            double latitude3 = latitude;
            Iterator<LocationInfo> it = duplicatedInfo.iterator();
            while (it.hasNext()) {
                this.mLocationInfoQueue.remove(it.next());
            }
            this.mLocationInfoQueue.add(locationInfo);
            latitude = latitude3;
        }
        pollLocationInfo();
        this.mLocationTasks.clear();
    }

    private void pollLocationInfo() {
        if (this.mLocationInfoQueue.isEmpty()) {
            Rlog.i(TAG, "No GeoLocation task");
            return;
        }
        final List<LocationInfo> LocationInfoQueueCopy = new ArrayList<>(this.mLocationInfoQueue);
        this.mLocationInfoQueue.clear();
        try {
            this.mExecutor.execute(new Runnable() { // from class: com.mediatek.internal.telephony.MtkSmsLocationHandler.2
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    if (LocationInfoQueueCopy != null) {
                        Rlog.i(MtkSmsLocationHandler.TAG, "pollLocationInfo: queue size=" + LocationInfoQueueCopy.size());
                        for (LocationInfo gpsLocationInfo : LocationInfoQueueCopy) {
                            LocationInfo res = MtkSmsLocationHandler.this.getGeoLocationFromLatLong(gpsLocationInfo);
                            if (res != null) {
                                MtkSmsLocationHandler.this.obtainMessage(MtkSmsLocationHandler.EVENT_SET_LOCATION_INFO, 0, 0, res).sendToTarget();
                            }
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            Rlog.w(TAG, "pollLocationInfo: " + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LocationInfo getGeoLocationFromLatLong(LocationInfo location) throws IOException {
        if (this.mGeoCoder == null) {
            log("getGeoLocationFromLatLong: empty geoCoder, return an empty location");
            return location;
        }
        if (!Geocoder.isPresent()) {
            log("getGeoLocationFromLatLong: this system has no GeoCoder implementation!!");
            return location;
        }
        double lat = location.mLatitude;
        double lng = location.mLongitude;
        List<Address> lstAddress = null;
        try {
            lstAddress = this.mGeoCoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            Rlog.e(TAG, "mGeoCoder.getFromLocation throw IOException:" + e);
        } catch (IllegalArgumentException e2) {
            Rlog.e(TAG, "mGeoCoder.getFromLocation throw IllegalArgumentException");
        }
        if (lstAddress == null || lstAddress.isEmpty()) {
            Rlog.e(TAG, "getGeoLocationFromLatLong: get empty address, time = " + location.mTime);
            return location;
        }
        location.mCity = lstAddress.get(0).getLocality();
        if (TextUtils.isEmpty(location.mCity)) {
            location.mCity = lstAddress.get(0).getSubAdminArea();
        }
        location.mState = lstAddress.get(0).getAdminArea();
        if (TextUtils.isEmpty(location.mState)) {
            location.mState = lstAddress.get(0).getCountryName();
        }
        location.mZip = lstAddress.get(0).getPostalCode();
        location.mCountryCode = lstAddress.get(0).getCountryCode();
        log("getGeoLocationFromLatLong: location=" + location);
        return location;
    }

    private boolean getLastKnownLocation() {
        Rlog.d(TAG, "getLastKnownLocation");
        if (this.mLocationManager == null) {
            Rlog.d(TAG, "getLastKnownLocation: empty locationManager, return");
            return false;
        }
        if (isCtaNotAllow()) {
            Rlog.e(TAG, "getLastKnownLocation: CTA not allow");
            return false;
        }
        if (this.mLocationManager.getProvider("fused") == null) {
            Rlog.d(TAG, "getLastKnownLocation: FUSED_PROVIDER doesn't exist or not ready");
            return false;
        }
        Location flpLocation = this.mLocationManager.getLastKnownLocation("fused", new LastLocationRequest.Builder().setLocationSettingsIgnored(true).build());
        if (flpLocation != null) {
            log("FLP location: " + flpLocation);
            if (System.currentTimeMillis() - flpLocation.getTime() < 1800000) {
                obtainMessage(EVENT_HANDLE_LAST_KNOWN_LOCATION_RESPONSE, 0, 0, flpLocation).sendToTarget();
                return true;
            }
        }
        if (this.mLocationManager.getProvider("gps") == null) {
            Rlog.d(TAG, "getLastKnownLocation: GPS_PROVIDER doesn't exist or not ready");
            return false;
        }
        Location gpsLocation = this.mLocationManager.getLastKnownLocation("gps", new LastLocationRequest.Builder().setLocationSettingsIgnored(true).build());
        if (gpsLocation != null) {
            log("GPS location: " + gpsLocation);
            if (System.currentTimeMillis() - gpsLocation.getTime() < 1800000) {
                obtainMessage(EVENT_HANDLE_LAST_KNOWN_LOCATION_RESPONSE, 0, 0, gpsLocation).sendToTarget();
                return true;
            }
        }
        Rlog.d(TAG, "getLastKnownLocation: no last known location");
        return false;
    }

    private boolean isCtaNotAllow() {
        boolean isCtaSet = SystemProperties.getInt("ro.vendor.mtk_cta_set", 0) == 1;
        boolean isCtaSecurity = SystemProperties.getInt("ro.vendor.mtk_mobile_management", 0) == 1;
        boolean isFlpEnabled = this.mLocationManager.isProviderEnabled("fused");
        Rlog.d(TAG, "isCtaNotAllow: isCtaSet:" + isCtaSet + ", isCtaSecurity:" + isCtaSecurity + ", isFlpEnabled:" + isFlpEnabled);
        if (!isCtaSet || !isCtaSecurity || isFlpEnabled) {
            return false;
        }
        Rlog.i(TAG, "isCtaNotAllow: true");
        return true;
    }

    public void requestLocation(int phoneId) {
        Rlog.d(TAG, "requestLocation");
        LocationInfo info = new LocationInfo(phoneId, 0, 1, 0.0d, 0.0d, 0.0f);
        info.mMethod = "FLP";
        this.mLocationTasks.add(info);
        if (!requestLocationFromFLP()) {
            Rlog.d(TAG, "requestLocationFromFLP failed");
            this.mLocationTasks.remove(info);
            info.mMethod = "GPS";
            this.mLocationTasks.add(info);
            if (!requestLocationFromGPS()) {
                Rlog.d(TAG, "requestLocationFromGPS failed");
                this.mLocationTasks.remove(info);
            }
        }
        if (!hasMessages(EVENT_GET_LAST_KNOWN_LOCATION)) {
            int timeout = REQUEST_GEOLOCATION_TIMEOUT;
            Rlog.d(TAG, "Add delayed message: EVENT_GET_LAST_KNOWN_LOCATION: " + timeout);
            sendMessageDelayed(obtainMessage(EVENT_GET_LAST_KNOWN_LOCATION, 0, 0, info), timeout);
        }
    }

    private boolean requestLocationFromFLP() {
        Rlog.d(TAG, "requestLocationFromFLP");
        LocationManager locationManager = this.mLocationManager;
        if (locationManager == null) {
            Rlog.e(TAG, "requestLocationFromFLP failed: empty locationManager");
            return false;
        }
        if (locationManager.getProvider("fused") == null) {
            Rlog.e(TAG, "requestLocationFromFLP failed: FUSED_PROVIDER not ready");
            return false;
        }
        if (isCtaNotAllow()) {
            Rlog.e(TAG, "requestLocationFromFLP failed: CTA not allow");
            return false;
        }
        isPackageInLocationSettingsWhitelist();
        if (!this.mLocationRequestRegistered) {
            LocationRequest request = LocationRequest.createFromDeprecatedProvider("fused", LOCATION_UPDATE_TIME, 0.0f, false);
            request.setHideFromAppOps(true);
            request.setQuality(100);
            request.setLocationSettingsIgnored(true);
            this.mLocationManager.requestLocationUpdates(request, this.mLocationListener, getLooper());
            this.mLocationRequestRegistered = true;
            Rlog.i(TAG, "requestLocationFromFLP: success");
        }
        return true;
    }

    private boolean requestLocationFromGPS() {
        Rlog.d(TAG, "requestLocationFromGPS");
        LocationManager locationManager = this.mLocationManager;
        if (locationManager == null) {
            Rlog.e(TAG, "requestLocationFromGPS failed: empty locationManager");
            return false;
        }
        if (locationManager.getProvider("gps") == null) {
            Rlog.e(TAG, "requestLocationFromGPS failed: GPS_PROVIDER not ready");
            return false;
        }
        if (isCtaNotAllow()) {
            Rlog.e(TAG, "requestLocationFromGPS failed: CTA not allow");
            return false;
        }
        isPackageInLocationSettingsWhitelist();
        if (!this.mLocationRequestRegistered) {
            LocationRequest request = LocationRequest.createFromDeprecatedProvider("gps", LOCATION_UPDATE_TIME, 0.0f, false);
            request.setHideFromAppOps(true);
            request.setQuality(100);
            request.setLocationSettingsIgnored(true);
            this.mLocationManager.requestLocationUpdates(request, this.mLocationListener, getLooper());
            this.mLocationRequestRegistered = true;
            Rlog.i(TAG, "requestLocationFromGPS: success");
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelLocationRequest() {
        LocationManager locationManager = this.mLocationManager;
        if (locationManager == null) {
            this.mLocationRequestRegistered = false;
            Rlog.e(TAG, "cancelLocationRequest: empty locationManager, return");
        } else {
            locationManager.removeUpdates(this.mLocationListener);
            this.mLocationRequestRegistered = false;
            Rlog.i(TAG, "cancelLocationRequest");
        }
    }

    private float getSigmaFromConf(float conf) {
        if (conf >= 100.0f) {
            return 6.0f;
        }
        if (conf <= 0.0f || conf == 67.0f || conf == 68.0f) {
            return 1.0f;
        }
        if (conf == 90.0f) {
            return 1.65f;
        }
        if (conf == 95.0f) {
            return 1.96f;
        }
        float distribution = (float) ((((double) conf) + 100.0d) / 200.0d);
        int index = 0;
        while (true) {
            float[] fArr = STANDARD_NORMAL_DISTRIBUTION_TABLE;
            if (index >= fArr.length) {
                return 0.0f;
            }
            float f = fArr[index];
            if (f == distribution) {
                return (float) (((double) index) * 0.1d);
            }
            if (index < 1 || f <= distribution) {
                index++;
            } else {
                float RetVal = (float) ((((double) (index - 1)) * 0.1d) + ((((double) (distribution - fArr[index - 1])) * 0.1d) / ((double) (f - fArr[index - 1]))));
                return RetVal;
            }
        }
    }

    private float adjustAccuracyForConfidence(float srcAccuracy, float srcConf, float destConf) {
        return (getSigmaFromConf(destConf) / getSigmaFromConf(srcConf)) * srcAccuracy;
    }

    private void setLocationInfo(LocationInfo info) {
        if (TextUtils.isEmpty(info.mState)) {
            info.mState = "Unknown";
        }
        if ((!TextUtils.isEmpty(this.mPlmnCountryCode) && TextUtils.length(info.mCountryCode) != 2) || COUNTRY_CODE_HK.equals(this.mPlmnCountryCode)) {
            info.mCountryCode = this.mPlmnCountryCode;
        } else if (TextUtils.isEmpty(this.mPlmnCountryCode) && TextUtils.length(info.mCountryCode) != 2) {
            info.mCountryCode = getSimCountryCode();
        } else if (TextUtils.length(info.mCountryCode) == 2) {
            this.mPlmnCountryCode = info.mCountryCode;
        }
        int destConf = info.mConfidence;
        if (destConf == 0) {
            destConf = getIntCarrierConfigEx(MTK_KEY_WFC_GET_CONFIDENCE_LEVEL, NA_CONFIDENCE_LEVEL, info.mSimIdx);
        }
        float destAccuracy = adjustAccuracyForConfidence(info.mAccuracy, 68.0f, destConf);
        log("setGeoLocation new accuracy:" + destAccuracy + ", new confidence:" + destConf);
        info.mAccuracy = destAccuracy;
        info.mMajorAxisAccuracy = info.mAccuracy;
        info.mMinorAxisAccuracy = info.mAccuracy;
        Rlog.i(TAG, "setLocationInfo info=" + info + ", mPlmnCountryCode:" + this.mPlmnCountryCode);
        getSmsRIL(info.mSimIdx).setLocationInfo(Integer.toString(info.mAccountId), Integer.toString(info.mBroadcastFlag), String.valueOf(info.mLatitude), String.valueOf(info.mLongitude), String.valueOf(info.mAccuracy), info.mMethod, info.mCity, info.mState, info.mZip, info.mCountryCode, null, Integer.toString(destConf), String.valueOf(info.mAltitude), String.valueOf(info.mMajorAxisAccuracy), String.valueOf(info.mMinorAxisAccuracy), String.valueOf(info.mVericalAxisAccuracy), null);
    }

    private SmsRIL getSmsRIL(int phoneId) {
        if (phoneId < 0 || phoneId >= TelephonyManager.getDefault().getPhoneCount()) {
            phoneId = 0;
        }
        return this.mSmsRil[phoneId];
    }

    protected void log(String s) {
        if (ENGLOAD) {
            Rlog.d(TAG, s);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String maskString(String s) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        int maskLength = s.length() / 2;
        if (maskLength < 1) {
            sb.append("*");
            return sb.toString();
        }
        for (int i = 0; i < maskLength; i++) {
            sb.append("*");
        }
        return sb.toString() + s.substring(maskLength);
    }

    private String getSimCountryCode() {
        String simCountryCode = TelephonyManager.getDefault().getSimCountryIso().toUpperCase(Locale.US);
        log("getSimCountryCode: " + simCountryCode);
        return simCountryCode;
    }

    private boolean isPackageInLocationSettingsWhitelist() {
        boolean result = false;
        String packageName = this.mContext.getPackageName();
        PackageTagsList whitelist = this.mLocationManager.getIgnoreSettingsAllowlist();
        if (whitelist.includes(packageName)) {
            result = true;
        }
        log("location setting whitelist:" + whitelist + ", result:" + result);
        return result;
    }

    public boolean isSupportT911Location(int phoneId) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        this.mConfigManager = carrierConfigManager;
        if (carrierConfigManager == null) {
            Rlog.e(TAG, "isSupportT911Location: Carrier Config service is NOT ready");
            return false;
        }
        int subId = SubscriptionManager.getSubscriptionId(phoneId);
        long token = Binder.clearCallingIdentity();
        try {
            PersistableBundle configs = SubscriptionManager.isValidSubscriptionId(subId) ? this.mConfigManager.getConfigForSubId(subId) : null;
            if (configs == null) {
                Rlog.e(TAG, "isSupportT911Location: SIM not ready, use default carrier config");
                configs = CarrierConfigManager.getDefaultConfig();
            }
            boolean supportT911Location = configs.getBoolean(MTK_KEY_SMS_SUPPORTS_T911_LOCATION);
            Rlog.i(TAG, "supportT911Location: " + supportT911Location);
            return supportT911Location;
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX WARN: Finally extract failed */
    public boolean isTestEmergencyNumber(int phoneId, String destAddress) {
        if (!isSupportT911Location(phoneId)) {
            return false;
        }
        boolean isTestEmergencyNumber = false;
        int subId = SubscriptionManager.getSubscriptionId(phoneId);
        long token = Binder.clearCallingIdentity();
        try {
            PersistableBundle configs = SubscriptionManager.isValidSubscriptionId(subId) ? this.mConfigManager.getConfigForSubId(subId) : null;
            Binder.restoreCallingIdentity(token);
            if (configs == null) {
                Rlog.e(TAG, "isTestEmergencyNumber: SIM not ready, use default carrier config");
                configs = CarrierConfigManager.getDefaultConfig();
            }
            String testEmergencyNumber = configs.getString(MTK_KEY_SMS_TEST_EMERGENCY_NUMBER);
            if (testEmergencyNumber != null && !testEmergencyNumber.equals("")) {
                isTestEmergencyNumber = destAddress.equals(testEmergencyNumber);
            }
            Rlog.i(TAG, "isTestEmergencyNumber: " + isTestEmergencyNumber);
            return isTestEmergencyNumber;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private int getIntCarrierConfigEx(String key, int def, int phoneId) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        this.mConfigManager = carrierConfigManager;
        if (carrierConfigManager == null) {
            Rlog.e(TAG, "getIntCarrierConfigEx: Carrier Config service is NOT ready");
            return def;
        }
        int ret = def;
        int simState = TelephonyManager.getSimStateForSlotIndex(phoneId);
        if (simState != 1) {
            int subId = SubscriptionManager.getSubscriptionId(phoneId);
            long token = Binder.clearCallingIdentity();
            try {
                PersistableBundle configs = SubscriptionManager.isValidSubscriptionId(subId) ? this.mConfigManager.getConfigForSubId(subId) : null;
                if (configs == null) {
                    Rlog.e(TAG, "getIntCarrierConfigEx: SIM not ready, use default carrier config");
                    configs = CarrierConfigManager.getDefaultConfig();
                }
                ret = configs.getInt(key, def);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
        log("getIntCarrierConfigEx phoneId: " + phoneId + " key: " + key + " ret: " + ret + " simState: " + simState);
        return ret;
    }
}
