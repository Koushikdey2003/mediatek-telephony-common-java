package com.mediatek.internal.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes.dex */
public class NrMap {
    protected static final int EVENT_REQUEST_LISTENER_UPDATE = 2;
    protected static final int EVENT_REQUEST_LOCATION_UPDATE = 1;
    protected static final int EVENT_RESPONSE_SET_NR = 101;
    protected static final int EVENT_SERVICE_STATE_CHANGE = 201;
    private static final String LOCATION_PERMISSION_NAME = "android.permission.ACCESS_FINE_LOCATION";
    private static final String NR_CITY_FILE = "system_ext/etc/nr-city.xml";
    private static final String NR_CITY_SQL_NAME = "mtk_nr_city_sql.db";
    private final String NR_MAP_SP_KEY;
    private final String NR_MAP_SW_KEY;
    private CitySQL city_sql;
    private SQLiteDatabase city_sql_data;
    private boolean listening;
    private final CommandsInterface mCi;
    private String mGnssProxyPackageName;
    private int mId;
    private LocationManager mLocationManager;
    private PackageManager mPackageManager;
    private final GsmCdmaPhone mPhone;
    private Handler mtkHandler;
    private HandlerThread mtkHandlerThread;
    private static String NR_AT = "AT+EGMC=1,\"5g_available\",";
    private static String NR_SET_UNAVAILABLE = "0";
    private static String NR_SET_AVAILABLE = RadioCapabilitySwitchUtil.IMSI_READY;
    private static String NR_SET_UNKNOWN = MtkGsmCdmaPhone.ACT_TYPE_UTRAN;
    private static LoadCityFromXml loader = null;
    private static Object mLock = new Object();
    private static ArrayList<Record> records = new ArrayList<>();
    private static Object lock = new Object();
    private String LOG_TAG = "NrMap";
    private boolean started = false;
    private int interval_location_update = 1800000;
    private int min_time_location_update = 0;
    private int min_distance_location_update = 0;
    private String is_nr_city = "";
    private String current_city = "n/a";
    private ServiceState mSS = null;
    private boolean camp_SA = false;
    private boolean manually_set = false;
    private int manually_config = -1;
    private boolean signal_update = false;
    private boolean mLastPermission = false;
    private LocationListener mLocationListener = new LocationListener() { // from class: com.mediatek.internal.telephony.NrMap.1
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) throws IOException {
            int interval;
            boolean listen;
            synchronized (NrMap.mLock) {
                interval = NrMap.this.interval_location_update;
                listen = NrMap.this.listening;
            }
            if (location == null) {
                Rlog.d(NrMap.this.LOG_TAG, "network location get null, unexpected result");
                NrMap.this.mtkHandler.sendMessageDelayed(NrMap.this.mtkHandler.obtainMessage(1), interval);
                return;
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (!Geocoder.isPresent()) {
                Rlog.d(NrMap.this.LOG_TAG, "getGeoLocationFromLatLong: this system has no GeoCoder implementation!!");
                return;
            }
            Address add = NrMap.this.getAddByLocation(latitude, longitude);
            if (add != null) {
                String iso = add.getCountryCode();
                if ((TextUtils.isEmpty(iso) || iso.equals("0")) && NrMap.this.mPhone.getServiceStateTracker() != null && NrMap.this.mPhone.getServiceStateTracker().getLocaleTracker() != null) {
                    iso = NrMap.this.mPhone.getServiceStateTracker().getLocaleTracker().getCurrentCountry();
                }
                synchronized (NrMap.mLock) {
                    NrMap.this.current_city = add.getLocality();
                }
                if (TextUtils.isEmpty(iso)) {
                    if (NrMap.this.mSS != null && NrMap.this.mSS.getVoiceRegState() != 3) {
                        NrMap.this.setNrCity(NrMap.NR_SET_UNKNOWN);
                    }
                } else if (!TextUtils.isEmpty(add.getLocality())) {
                    if (NrMap.this.camp_SA && !NrMap.this.city_sql.findByCityName(add.getLocality())) {
                        Rlog.d(NrMap.this.LOG_TAG, "find a new SA city " + add.getLocality());
                        NrMap.this.city_sql.insert("n/a", iso, add.getLocality(), true);
                    }
                    if (!NrMap.this.manually_set) {
                        if (!NrMap.this.city_sql.findByIsoName(iso)) {
                            NrMap.this.setNrCity(NrMap.NR_SET_UNKNOWN);
                        } else if (NrMap.this.city_sql.findByCityName(add.getLocality())) {
                            NrMap.this.setNrCity(NrMap.NR_SET_AVAILABLE);
                        } else {
                            NrMap.this.setNrCity(NrMap.NR_SET_UNAVAILABLE);
                        }
                    }
                } else {
                    Rlog.d(NrMap.this.LOG_TAG, "got a empty city");
                    NrMap.this.setNrCity(NrMap.NR_SET_UNKNOWN);
                }
            }
            if (listen && NrMap.this.signal_update) {
                NrMap.this.startPassiveLocationListener();
            }
            NrMap.this.signal_update = false;
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String provider) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String provider) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private class Record {
        private String city_name;
        private String iso;
        private String mccmnc;

        public Record(String mccmnc, String city_name, String iso) {
            this.mccmnc = mccmnc;
            this.city_name = city_name;
            this.iso = iso;
        }

        public String getMccMnc() {
            return this.mccmnc;
        }

        public String getCityName() {
            return this.city_name;
        }

        public String getIso() {
            return this.iso;
        }

        public String toString() {
            return new String("Record(" + this.mccmnc + "/" + this.city_name + "/" + this.iso + ")");
        }
    }

    private class LoadCityFromXml extends AsyncTask<String, Integer, Integer> {
        private boolean done;

        private LoadCityFromXml() {
            this.done = false;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            Rlog.d(NrMap.this.LOG_TAG, "loadCityFromXml.onPreExecute: system_ext/etc/nr-city.xml");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(String... path) {
            String mccmnc = "n/a";
            try {
                XmlPullParserFactory xmlfactory = XmlPullParserFactory.newInstance();
                xmlfactory.setNamespaceAware(true);
                XmlPullParser xmlparser = xmlfactory.newPullParser();
                File city_file = new File(Environment.getRootDirectory(), NrMap.NR_CITY_FILE);
                if (city_file.exists()) {
                    FileReader xmlreader = new FileReader(city_file);
                    xmlparser.setInput(xmlreader);
                    XmlUtils.beginDocument(xmlparser, "records");
                    String iso = "n/a";
                    String city = "n/a";
                    for (int event = xmlparser.getEventType(); event != 1; event = xmlparser.next()) {
                        if (event != 0) {
                            if (event == 2) {
                                if (xmlparser.getName().equals("mccmnc")) {
                                    mccmnc = new String(xmlparser.nextText());
                                } else if (xmlparser.getName().equals("city_name")) {
                                    city = new String(xmlparser.nextText());
                                } else if (xmlparser.getName().equals("iso")) {
                                    iso = new String(xmlparser.nextText());
                                }
                            } else if (event == 3 && xmlparser.getName().equals("record")) {
                                NrMap.records.add(NrMap.this.new Record(mccmnc, city, iso));
                            }
                        }
                    }
                } else {
                    Rlog.e(NrMap.this.LOG_TAG, "LoadCityFromXml file doesn't exist system_ext/etc/nr-city.xml");
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < NrMap.records.size(); i++) {
                Rlog.d(NrMap.this.LOG_TAG, "[" + i + "]" + NrMap.records.get(i));
                if (!NrMap.this.city_sql.findByCityName(((Record) NrMap.records.get(i)).getCityName())) {
                    NrMap.this.city_sql.insert(((Record) NrMap.records.get(i)).getMccMnc(), ((Record) NrMap.records.get(i)).getIso(), ((Record) NrMap.records.get(i)).getCityName(), true);
                }
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer i) {
            Rlog.d(NrMap.this.LOG_TAG, "loadCityFromXml.done: system_ext/etc/nr-city.xml");
            this.done = true;
        }

        public boolean isReady() {
            return this.done;
        }
    }

    private class CitySQL {
        private final String TABLE_NAME = "table_nr_city";
        private final String COLUMN_ID = "_id";
        private final String COLUMN_MCCMNC = "mccmnc";
        private final String COLUMN_ISO = "iso";
        private final String COLUMN_COUNTRY_NAME = "country_name";
        private final String COLUMN_NR = "nr";

        public CitySQL(Context context) {
            synchronized (NrMap.lock) {
                NrMap.this.city_sql_data = context.openOrCreateDatabase(NrMap.NR_CITY_SQL_NAME, 0, null);
                NrMap.this.city_sql_data.execSQL("CREATE TABLE IF NOT EXISTS table_nr_city (_id INTEGER PRIMARY KEY AUTOINCREMENT, mccmnc VARCHAR(8), iso VARCHAR(8), country_name VARCHAR(32), nr INT)");
            }
        }

        public boolean insert(String str, String str2, String str3, boolean z) {
            ContentValues contentValues = new ContentValues(3);
            contentValues.put("mccmnc", str);
            contentValues.put("iso", str2.toLowerCase());
            contentValues.put("country_name", str3.toLowerCase());
            contentValues.put("nr", Integer.valueOf(z ? 1 : 0));
            synchronized (NrMap.lock) {
                return NrMap.this.city_sql_data.insert("table_nr_city", null, contentValues) != -1;
            }
        }

        public boolean findByIsoName(String iso) {
            synchronized (NrMap.lock) {
                boolean z = true;
                Cursor cursor = NrMap.this.city_sql_data.query(false, "table_nr_city", null, "iso=?", new String[]{iso.toLowerCase()}, null, null, null, null);
                if (cursor == null) {
                    Rlog.e(NrMap.this.LOG_TAG, "findByIsoName cursor null");
                    return false;
                }
                int count = cursor.getCount();
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    Rlog.d(NrMap.this.LOG_TAG, "findByIsoName Record[" + i + "] _id=" + cursor.getString(cursor.getColumnIndex("_id")) + ", mccmnc=" + cursor.getString(cursor.getColumnIndex("mccmnc")) + ", iso=" + cursor.getString(cursor.getColumnIndex("iso")) + ", country_name=" + cursor.getString(cursor.getColumnIndex("country_name")));
                }
                cursor.close();
                if (count <= 0) {
                    z = false;
                }
                return z;
            }
        }

        public boolean findByCityName(String country_name) {
            synchronized (NrMap.lock) {
                boolean z = true;
                Cursor cursor = NrMap.this.city_sql_data.query(false, "table_nr_city", null, "country_name=?", new String[]{country_name.toLowerCase()}, null, null, null, null);
                if (cursor == null) {
                    Rlog.e(NrMap.this.LOG_TAG, "findByCityName cursor null");
                    return false;
                }
                int count = cursor.getCount();
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    Rlog.d(NrMap.this.LOG_TAG, "findByCityName Record[" + i + "] _id=" + cursor.getString(cursor.getColumnIndex("_id")) + ", mccmnc=" + cursor.getString(cursor.getColumnIndex("mccmnc")) + ", iso=" + cursor.getString(cursor.getColumnIndex("iso")) + ", country_name=" + cursor.getString(cursor.getColumnIndex("country_name")));
                }
                cursor.close();
                if (count <= 0) {
                    z = false;
                }
                return z;
            }
        }
    }

    public NrMap(GsmCdmaPhone phone, CommandsInterface ci) {
        this.listening = false;
        this.mId = -1;
        this.mPhone = phone;
        this.mId = phone.getPhoneId();
        this.LOG_TAG += "-" + this.mId;
        String str = "nr_map_sp_key-" + this.mId;
        this.NR_MAP_SP_KEY = str;
        String str2 = "nr_map_sw_key-" + this.mId;
        this.NR_MAP_SW_KEY = str2;
        this.mCi = ci;
        HandlerThread handlerThread = new HandlerThread("MtkNrMapThread");
        this.mtkHandlerThread = handlerThread;
        handlerThread.start();
        this.mtkHandler = new MtkHandler(this.mtkHandlerThread.getLooper());
        Context context = phone.getContext();
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        this.mPackageManager = context.getPackageManager();
        this.mGnssProxyPackageName = loadProxyNameFromCarrierConfig(context);
        Rlog.d(this.LOG_TAG, "mGnssProxyPackageName from carrierConfig: " + this.mGnssProxyPackageName);
        enableAggressiveUpdate(false);
        this.city_sql = new CitySQL(context);
        if (loader == null) {
            LoadCityFromXml loadCityFromXml = new LoadCityFromXml();
            loader = loadCityFromXml;
            loadCityFromXml.execute("");
        }
        setNrCity(NR_SET_UNKNOWN);
        phone.registerForServiceStateChanged(this.mtkHandler, 201, (Object) null);
        String set = phone.getContext().getSharedPreferences(str, 0).getString(str2, "0");
        boolean permission = checkLocationProxyAppPermission();
        Rlog.d(this.LOG_TAG, "NrMap set=" + set + " permission=" + permission);
        if (set.equals(RadioCapabilitySwitchUtil.IMSI_READY) && permission) {
            this.listening = true;
            startSingleLocationUpdate();
        }
    }

    private String nrSetToString(String setting) {
        return setting.equals("0") ? "NR_SET_UNAVAILABLE" : setting.equals(RadioCapabilitySwitchUtil.IMSI_READY) ? "NR_SET_AVAILABLE" : "NR_SET_UNKNOWN";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNrCity(String setting) {
        String isNrCity;
        synchronized (mLock) {
            isNrCity = new String(this.is_nr_city);
        }
        Message result = this.mtkHandler.obtainMessage(101);
        if (!isNrCity.equals(setting)) {
            Rlog.d(this.LOG_TAG, "notify MD about NR city " + setting);
            String at = NR_AT + setting;
            this.mCi.invokeOemRilRequestRaw(at.getBytes(), result);
        }
        synchronized (mLock) {
            this.is_nr_city = isNrCity;
        }
    }

    public void swtichNrMap(boolean sw) {
        boolean listen;
        synchronized (mLock) {
            listen = this.listening;
        }
        Rlog.d(this.LOG_TAG, "swtichNrMap=" + sw + " listening=" + listen);
        if (sw) {
            this.mPhone.getContext().getSharedPreferences(this.NR_MAP_SP_KEY, 0).edit().putString(this.NR_MAP_SW_KEY, RadioCapabilitySwitchUtil.IMSI_READY).commit();
            if (!listen) {
                synchronized (mLock) {
                    this.listening = true;
                }
                Handler handler = this.mtkHandler;
                handler.sendMessage(handler.obtainMessage(2));
                return;
            }
            return;
        }
        this.mPhone.getContext().getSharedPreferences(this.NR_MAP_SP_KEY, 0).edit().putString(this.NR_MAP_SW_KEY, "0").commit();
        if (listen) {
            synchronized (mLock) {
                this.listening = false;
            }
            Handler handler2 = this.mtkHandler;
            handler2.sendMessage(handler2.obtainMessage(2));
        }
    }

    public void enableAggressiveUpdate(boolean sw) {
        boolean start;
        int interval;
        synchronized (mLock) {
            start = this.started;
            interval = this.interval_location_update;
        }
        Rlog.d(this.LOG_TAG, "swtichNrMap=" + sw + " listening=" + start);
        if (sw) {
            if (!start) {
                synchronized (mLock) {
                    this.started = true;
                }
                Handler handler = this.mtkHandler;
                handler.sendMessageDelayed(handler.obtainMessage(1), interval);
                return;
            }
            return;
        }
        if (start) {
            synchronized (mLock) {
                this.started = false;
            }
        }
    }

    public void setAggressiveUpdateInterval(int interval) {
        Rlog.d(this.LOG_TAG, "setAggressiveUpdateInterval " + interval);
        synchronized (mLock) {
            this.interval_location_update = interval;
        }
    }

    private class MtkHandler extends Handler {
        public MtkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            boolean start;
            boolean listen;
            boolean listen2;
            switch (msg.what) {
                case 1:
                    synchronized (NrMap.mLock) {
                        start = NrMap.this.started;
                        break;
                    }
                    Rlog.d(NrMap.this.LOG_TAG, "EVENT_REQUEST_LOCATION_UPDATE started=" + start);
                    if (start) {
                        NrMap.this.mLocationManager.requestSingleUpdate("network", NrMap.this.mLocationListener, (Looper) null);
                        return;
                    }
                    return;
                case 2:
                    boolean permission = NrMap.this.checkLocationProxyAppPermission();
                    synchronized (NrMap.mLock) {
                        listen = NrMap.this.listening;
                        break;
                    }
                    Rlog.d(NrMap.this.LOG_TAG, "EVENT_REQUEST_LISTENER_UPDATE listening=" + listen + " permission=" + permission);
                    if (!permission) {
                        synchronized (NrMap.mLock) {
                            NrMap.this.listening = false;
                            break;
                        }
                        NrMap.this.mPhone.getContext().getSharedPreferences(NrMap.this.NR_MAP_SP_KEY, 0).edit().putString(NrMap.this.NR_MAP_SW_KEY, "0").commit();
                        return;
                    }
                    if (listen) {
                        NrMap.this.startSingleLocationUpdate();
                        return;
                    } else {
                        NrMap.this.mLocationManager.removeUpdates(NrMap.this.mLocationListener);
                        return;
                    }
                case 101:
                default:
                    return;
                case 201:
                    synchronized (NrMap.mLock) {
                        listen2 = NrMap.this.listening;
                        break;
                    }
                    NrMap.this.mSS = (ServiceState) ((AsyncResult) msg.obj).result;
                    Rlog.d(NrMap.this.LOG_TAG, "EVENT_SERVICE_STATE_CHANGE camp_SA=" + NrMap.this.camp_SA + " listening=" + listen2);
                    NetworkRegistrationInfo wwanPsRegState = NrMap.this.mSS.getNetworkRegistrationInfo(2, 1);
                    if (wwanPsRegState != null && wwanPsRegState.getAccessNetworkTechnology() == 20) {
                        NrMap.this.camp_SA = true;
                        if (listen2) {
                            NrMap.this.startSingleLocationUpdate();
                            return;
                        }
                        return;
                    }
                    NrMap.this.camp_SA = false;
                    return;
            }
        }
    }

    private double getDoubleFromProperty(String s) {
        try {
            return Double.parseDouble(SystemProperties.get(s));
        } catch (Exception e) {
            return 0.0d;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Address getAddByLocation(double latitude, double longitude) throws IOException {
        List<Address> lstAddress = null;
        try {
            Geocoder geocoder = new Geocoder(this.mPhone.getContext(), Locale.US);
            lstAddress = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {
            Rlog.d(this.LOG_TAG, "geocoder.getFromLocation throw exception:" + e);
        }
        if (lstAddress == null || lstAddress.isEmpty()) {
            Rlog.d(this.LOG_TAG, "getGeoLocationFromLatLong: get empty address");
            return null;
        }
        Address address = lstAddress.get(0);
        return address;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSingleLocationUpdate() {
        Rlog.d(this.LOG_TAG, "startSingleLocationUpdate");
        this.signal_update = true;
        this.mLocationManager.requestSingleUpdate("network", this.mLocationListener, (Looper) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPassiveLocationListener() {
        Rlog.d(this.LOG_TAG, "startPassiveLocationListener");
        this.mLocationManager.requestLocationUpdates("passive", this.min_time_location_update, this.min_distance_location_update, this.mLocationListener);
    }

    public void manuallySetNrMap(int i) {
        this.manually_set = true;
        Rlog.d(this.LOG_TAG, "manuallySetNrMap " + i + " listening=" + this.listening);
        this.manually_config = i;
        switch (i) {
            case 0:
                setNrCity(NR_SET_UNAVAILABLE);
                break;
            case 1:
                setNrCity(NR_SET_AVAILABLE);
                break;
            case 2:
                setNrCity(NR_SET_UNKNOWN);
                break;
            default:
                this.manually_set = false;
                this.manually_config = -1;
                if (this.listening) {
                    startSingleLocationUpdate();
                }
                break;
        }
    }

    public boolean isNrMapEnabled() {
        return this.listening;
    }

    public String getNrMapStatus() {
        String isNrCity;
        String currentCity;
        synchronized (mLock) {
            isNrCity = new String(this.is_nr_city);
            currentCity = new String(this.current_city);
        }
        if (this.manually_set) {
            switch (this.manually_config) {
                case 0:
                    return "Manually_UNAVAILABLE";
                case 1:
                    return "Manually_AVAILABLE";
                case 2:
                    return "Manually_UNKNOWN";
                default:
                    return "Unknown manually setting";
            }
        }
        if (!this.mLastPermission) {
            return "NEED PERMISSION";
        }
        if (this.listening) {
            return "NR MAP is ON, city=" + currentCity + "(" + nrSetToString(isNrCity) + ")";
        }
        return "NR MAP is OFF";
    }

    private boolean isPackageInstalled(String packagename) {
        try {
            this.mPackageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean hasLocationPermission(String pkgName) {
        return this.mPackageManager.checkPermission(LOCATION_PERMISSION_NAME, pkgName) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkLocationProxyAppPermission() {
        if (isPackageInstalled(this.mGnssProxyPackageName)) {
            boolean proxyAppLocationGranted = hasLocationPermission(this.mGnssProxyPackageName);
            Rlog.d(this.LOG_TAG, "proxyApp = " + this.mGnssProxyPackageName + " proxyAppLocationGranted = " + proxyAppLocationGranted);
            this.mLastPermission = proxyAppLocationGranted;
            return proxyAppLocationGranted;
        }
        this.mLastPermission = false;
        return false;
    }

    private String loadProxyNameFromCarrierConfig(Context sContext) {
        CarrierConfigManager configManager = (CarrierConfigManager) sContext.getSystemService("carrier_config");
        if (configManager == null) {
            return "";
        }
        int ddSubId = SubscriptionManager.getDefaultDataSubscriptionId();
        PersistableBundle configs = SubscriptionManager.isValidSubscriptionId(ddSubId) ? configManager.getConfigForSubId(ddSubId) : null;
        if (configs == null) {
            Rlog.d(this.LOG_TAG, "SIM not ready, use default carrier config.");
            configs = CarrierConfigManager.getDefaultConfig();
        }
        String value = (String) configs.get("gps.nfw_proxy_apps");
        Rlog.d(this.LOG_TAG, "gps.nfw_proxy_apps: " + value);
        if (value == null) {
            return "";
        }
        String[] strings = value.trim().split(" ");
        return strings[0];
    }
}
