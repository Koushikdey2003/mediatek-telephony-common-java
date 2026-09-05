package com.mediatek.internal.telephony.phb;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.IccInternalInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.phb.IMtkIccPhoneBook;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class MtkIccProvider implements IccInternalInterface {
    private static final int ADDRESS_SUPPORT_AAS = 8;
    private static final int ADDRESS_SUPPORT_SNE = 9;
    protected static final int ADN = 1;
    protected static final int ADN_ALL = 9;
    protected static final int ADN_SUB = 2;
    protected static final int FDN = 3;
    protected static final int FDN_SUB = 4;
    protected static final int SDN = 5;
    protected static final int SDN_SUB = 6;
    protected static final String STR_ANR = "anr";
    protected static final String STR_PIN2 = "pin2";
    protected static final String STR_TAG = "tag";
    private static final String TAG = "MtkIccProvider";
    protected static final int UPB = 7;
    protected static final int UPB_SUB = 8;
    private static UriMatcher URL_MATCHER;
    private Context mContext;
    private Runnable mPhbReadyRunnable;
    private BroadcastReceiver mPhbStateChangedIntentReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.phb.MtkIccProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MtkIccProvider.this.log("onReceive: action=" + action);
            if (action.equals("mediatek.intent.action.PHB_STATE_CHANGED")) {
                boolean bPhbReady = intent.getBooleanExtra("ready", false);
                int subId = intent.getIntExtra("subscription", -1);
                MtkIccProvider.this.log("subId: " + subId + ", bPhbReady: " + bPhbReady);
                if (bPhbReady && subId > 0) {
                    MtkIccProvider mtkIccProvider = MtkIccProvider.this;
                    mtkIccProvider.mPhbReadyRunnable = mtkIccProvider.new PhbReadyRunnable(subId);
                    Thread loadFDN = new Thread(MtkIccProvider.this.mPhbReadyRunnable);
                    loadFDN.start();
                }
            }
        }
    };
    private static final boolean DBG = !SystemProperties.get("ro.build.type").equals(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    protected static final String STR_INDEX = "index";
    protected static final String STR_NUMBER = "number";
    protected static final String STR_EMAILS = "emails";
    private static final String[] ADDRESS_BOOK_COLUMN_NAMES = {STR_INDEX, "name", STR_NUMBER, STR_EMAILS, "additionalNumber", "groupIds", "_id", "aas", "sne"};

    public MtkIccProvider(UriMatcher URL_MATCHER2, Context context) {
        logi("MtkIccProvider URL_MATCHER " + URL_MATCHER2);
        URL_MATCHER2.addURI("icc", "pbr", 7);
        URL_MATCHER2.addURI("icc", "pbr/subId/#", 8);
        URL_MATCHER = URL_MATCHER2;
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("mediatek.intent.action.PHB_STATE_CHANGED");
        context.registerReceiver(this.mPhbStateChangedIntentReceiver, filter, 2);
    }

    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
        logi("query " + url);
        switch (URL_MATCHER.match(url)) {
            case 1:
                return loadFromEf(28474, SubscriptionManager.getDefaultSubscriptionId());
            case 2:
                return loadFromEf(28474, getRequestSubId(url));
            case 3:
                return loadFromEf(28475, SubscriptionManager.getDefaultSubscriptionId());
            case 4:
                return loadFromEf(28475, getRequestSubId(url));
            case 5:
                return loadFromEf(28489, SubscriptionManager.getDefaultSubscriptionId());
            case 6:
                return loadFromEf(28489, getRequestSubId(url));
            case 7:
                return loadFromEf(20272, SubscriptionManager.getDefaultSubscriptionId());
            case 8:
                return loadFromEf(20272, getRequestSubId(url));
            case 9:
                return loadAllSimContacts(28474);
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
    }

    private Cursor loadAllSimContacts(int efType) {
        SubscriptionManager subscriptionManager = SubscriptionManager.from(this.mContext);
        int[] subIdList = subscriptionManager.getActiveSubscriptionIdList();
        Cursor[] result = new Cursor[subIdList.length];
        int i = 0;
        int length = subIdList.length;
        int i2 = 0;
        while (i2 < length) {
            int subId = subIdList[i2];
            result[i] = loadFromEf(efType, subId);
            Rlog.i(TAG, "loadAllSimContacts: subId=" + subId);
            i2++;
            i++;
        }
        return new MergeCursor(result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MatrixCursor loadFromEf(int efType, int subId) throws RemoteException {
        if (DBG) {
            log("loadFromEf: efType=0x" + Integer.toHexString(efType).toUpperCase(Locale.ROOT) + ", subscription=" + subId);
        }
        List<MtkAdnRecord> adnRecords = null;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                adnRecords = iccIpb.getAdnRecordsInEfForSubscriber(subId, efType);
            }
        } catch (RemoteException ex) {
            if (DBG) {
                log(ex.toString());
            }
        } catch (SecurityException ex2) {
            if (DBG) {
                log(ex2.toString());
            }
        }
        if (adnRecords != null) {
            int N = adnRecords.size();
            MatrixCursor cursor = new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES, N);
            if (DBG) {
                log("adnRecords.size=" + N);
            }
            for (int i = 0; i < N; i++) {
                loadRecord(adnRecords.get(i), cursor, i);
            }
            logi("query success, size = " + N);
            return cursor;
        }
        Rlog.w(TAG, "Cannot load ADN records");
        return new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES);
    }

    public Uri insert(Uri url, ContentValues initialValues) throws RemoteException {
        String pin2;
        int efType;
        int subId;
        int i;
        int result;
        String number;
        String tag;
        String tag2;
        String number2;
        String tag3;
        logi("insert " + url);
        int match = URL_MATCHER.match(url);
        switch (match) {
            case 1:
                int subId2 = SubscriptionManager.getDefaultSubscriptionId();
                pin2 = null;
                efType = 28474;
                subId = subId2;
                break;
            case 2:
                int subId3 = getRequestSubId(url);
                pin2 = null;
                efType = 28474;
                subId = subId3;
                break;
            case 3:
                int subId4 = SubscriptionManager.getDefaultSubscriptionId();
                String pin22 = initialValues.getAsString(STR_PIN2);
                pin2 = pin22;
                efType = 28475;
                subId = subId4;
                break;
            case 4:
                int subId5 = getRequestSubId(url);
                String pin23 = initialValues.getAsString(STR_PIN2);
                pin2 = pin23;
                efType = 28475;
                subId = subId5;
                break;
            case 5:
            case 6:
            default:
                throw new UnsupportedOperationException("Cannot insert into URL: " + url);
            case 7:
                int subId6 = SubscriptionManager.getDefaultSubscriptionId();
                pin2 = null;
                efType = 20272;
                subId = subId6;
                break;
            case 8:
                int subId7 = getRequestSubId(url);
                pin2 = null;
                efType = 20272;
                subId = subId7;
                break;
        }
        String tag4 = initialValues.getAsString(STR_TAG);
        String number3 = initialValues.getAsString(STR_NUMBER);
        if (7 == match || 8 == match) {
            String strGas = initialValues.getAsString("gas");
            String strAnr = initialValues.getAsString(STR_ANR);
            String strEmail = initialValues.getAsString(STR_EMAILS);
            String[] strArr = ADDRESS_BOOK_COLUMN_NAMES;
            if (strArr.length < 8) {
                logi("addUsimRecordToEf ");
                i = 1;
                result = addUsimRecordToEf(efType, tag4, number3, strAnr, strEmail, strGas, subId);
            } else {
                Integer aasIndex = initialValues.getAsInteger("aas");
                if (number3 != null) {
                    number = number3;
                } else {
                    number = "";
                }
                if (tag4 != null) {
                    tag = tag4;
                } else {
                    tag = "";
                }
                MtkAdnRecord record = new MtkAdnRecord(efType, 0, tag, number);
                record.setAnr(strAnr);
                if (!initialValues.containsKey("anr2")) {
                    number3 = number;
                    tag2 = tag;
                } else {
                    String strAnr2 = initialValues.getAsString("anr2");
                    if (!DBG) {
                        number3 = number;
                        tag2 = tag;
                    } else {
                        number3 = number;
                        tag2 = tag;
                        log("insert anr2: " + getMaskString(strAnr2));
                    }
                    record.setAnr(strAnr2, 1);
                }
                if (initialValues.containsKey("anr3")) {
                    String strAnr3 = initialValues.getAsString("anr3");
                    if (DBG) {
                        log("insert anr3: " + getMaskString(strAnr3));
                    }
                    record.setAnr(strAnr3, 2);
                }
                record.setGrpIds(strGas);
                String[] emails = null;
                if (strEmail != null && !strEmail.equals("")) {
                    emails = new String[]{strEmail};
                }
                record.setEmails(emails);
                if (aasIndex != null) {
                    record.setAasIndex(aasIndex.intValue());
                }
                if (strArr.length >= 9) {
                    String sne = initialValues.getAsString("sne");
                    record.setSne(sne);
                }
                logi("updateUsimPBRecordsBySearchWithError ");
                result = updateUsimPBRecordsBySearchWithError(efType, new MtkAdnRecord("", "", ""), record, subId);
                tag4 = tag2;
                i = 1;
            }
            if (result > 0) {
                updatePhbStorageInfo(i, subId);
            }
        } else {
            if (number3 != null) {
                number2 = number3;
            } else {
                number2 = "";
            }
            if (tag4 != null) {
                tag3 = tag4;
            } else {
                tag3 = "";
            }
            logi("addIccRecordToEf:" + getMaskString(number2) + ",tag:" + getMaskString(tag3));
            result = addIccRecordToEf(efType, tag3, number2, null, pin2, subId);
        }
        StringBuilder buf = new StringBuilder("content://icc/");
        if (result <= 0) {
            buf.append("error/");
            buf.append(result);
        } else {
            switch (match) {
                case 1:
                    buf.append("adn/");
                    break;
                case 2:
                    buf.append("adn/subId/");
                    break;
                case 3:
                    buf.append("fdn/");
                    break;
                case 4:
                    buf.append("fdn/subId/");
                    break;
                case 5:
                case 6:
                default:
                    throw new UnsupportedOperationException("Cannot insert into URL: " + url);
                case 7:
                    buf.append("pbr/");
                    break;
                case 8:
                    buf.append("pbr/subId/");
                    break;
            }
            buf.append(result);
        }
        Uri resultUri = Uri.parse(buf.toString());
        logi(resultUri.toString());
        return resultUri;
    }

    private String normalizeValue(String inVal) {
        int len = inVal.length();
        if (len == 0) {
            if (DBG) {
                log("len of input String is 0");
            }
            return inVal;
        }
        if (inVal.charAt(0) != '\'' || inVal.charAt(len - 1) != '\'') {
            return inVal;
        }
        String retVal = inVal.substring(1, len - 1);
        return retVal;
    }

    public int delete(Uri url, String where, String[] whereArgs) throws RemoteException {
        int efType;
        int subId;
        String str;
        int result;
        int i;
        int result2;
        logi("delete " + url);
        int match = URL_MATCHER.match(url);
        switch (match) {
            case 1:
                int subId2 = SubscriptionManager.getDefaultSubscriptionId();
                efType = 28474;
                subId = subId2;
                break;
            case 2:
                int subId3 = getRequestSubId(url);
                efType = 28474;
                subId = subId3;
                break;
            case 3:
                int subId4 = SubscriptionManager.getDefaultSubscriptionId();
                efType = 28475;
                subId = subId4;
                break;
            case 4:
                int subId5 = getRequestSubId(url);
                efType = 28475;
                subId = subId5;
                break;
            case 5:
            case 6:
            default:
                throw new UnsupportedOperationException("Cannot insert into URL: " + url);
            case 7:
                int subId6 = SubscriptionManager.getDefaultSubscriptionId();
                efType = 20272;
                subId = subId6;
                break;
            case 8:
                int subId7 = getRequestSubId(url);
                efType = 20272;
                subId = subId7;
                break;
        }
        String[] tokens = where.split(" AND ");
        String tag = "";
        String number = "";
        String[] emails = null;
        String pin2 = null;
        int n = tokens.length;
        int nIndex = -1;
        while (true) {
            int n2 = n - 1;
            if (n2 < 0) {
                if (nIndex > 0) {
                    logi("delete index is " + nIndex);
                    if (7 == match || 8 == match) {
                        logi("deleteUsimRecordFromEfByIndex ");
                        result2 = deleteUsimRecordFromEfByIndex(efType, nIndex, subId);
                        if (result2 > 0) {
                            updatePhbStorageInfo(-1, subId);
                        }
                    } else {
                        logi("deleteIccRecordFromEfByIndex ");
                        result2 = deleteIccRecordFromEfByIndex(efType, nIndex, pin2, subId);
                    }
                    logi("delete result = " + result2);
                    return result2;
                }
                if (efType == 28475 && TextUtils.isEmpty(pin2)) {
                    return -5;
                }
                if (tag.length() == 0 && number.length() == 0) {
                    return 0;
                }
                if (7 != match && 8 != match) {
                    logi("deleteIccRecordFromEf ");
                    str = "delete result = ";
                    result = deleteIccRecordFromEf(efType, tag, number, emails, pin2, subId);
                    logi(str + result);
                    return result;
                }
                str = "delete result = ";
                if (ADDRESS_BOOK_COLUMN_NAMES.length >= 8) {
                    logi("updateUsimPBRecordsBySearchWithError ");
                    result = updateUsimPBRecordsBySearchWithError(efType, new MtkAdnRecord(tag, number, ""), new MtkAdnRecord("", "", ""), subId);
                    i = -1;
                } else {
                    logi("deleteUsimRecordFromEf ");
                    i = -1;
                    result = deleteUsimRecordFromEf(efType, tag, number, emails, subId);
                }
                if (result > 0) {
                    updatePhbStorageInfo(i, subId);
                }
                logi(str + result);
                return result;
            }
            String param = tokens[n2];
            int index = param.indexOf(61);
            if (index == -1) {
                Rlog.e(TAG, "resolve: bad whereClause parameter: " + param);
                n = n2;
            } else {
                String key = param.substring(0, index).trim();
                String val = param.substring(index + 1).trim();
                if (STR_INDEX.equals(key)) {
                    nIndex = Integer.parseInt(val);
                } else if (STR_TAG.equals(key)) {
                    tag = normalizeValue(val);
                } else if (STR_NUMBER.equals(key)) {
                    number = normalizeValue(val);
                } else if (STR_EMAILS.equals(key)) {
                    emails = null;
                } else if (STR_PIN2.equals(key)) {
                    pin2 = normalizeValue(val);
                }
                n = n2;
            }
        }
    }

    public int update(Uri url, ContentValues values, String where, String[] whereArgs) throws RemoteException {
        String pin2;
        int efType;
        int subId;
        int index;
        int result;
        String newNumber;
        String newTag;
        String newTag2;
        String[] emails;
        String[] emails2;
        String newNumber2;
        String newTag3;
        logi("update " + url);
        int match = URL_MATCHER.match(url);
        switch (match) {
            case 1:
                int subId2 = SubscriptionManager.getDefaultSubscriptionId();
                pin2 = null;
                efType = 28474;
                subId = subId2;
                break;
            case 2:
                int subId3 = getRequestSubId(url);
                pin2 = null;
                efType = 28474;
                subId = subId3;
                break;
            case 3:
                int subId4 = SubscriptionManager.getDefaultSubscriptionId();
                String pin22 = values.getAsString(STR_PIN2);
                pin2 = pin22;
                efType = 28475;
                subId = subId4;
                break;
            case 4:
                int subId5 = getRequestSubId(url);
                String pin23 = values.getAsString(STR_PIN2);
                pin2 = pin23;
                efType = 28475;
                subId = subId5;
                break;
            case 5:
            case 6:
            default:
                throw new IllegalArgumentException("Unknown URL " + match);
            case 7:
                int subId6 = SubscriptionManager.getDefaultSubscriptionId();
                pin2 = null;
                efType = 20272;
                subId = subId6;
                break;
            case 8:
                int subId7 = getRequestSubId(url);
                pin2 = null;
                efType = 20272;
                subId = subId7;
                break;
        }
        String tag = values.getAsString(STR_TAG);
        String number = values.getAsString(STR_NUMBER);
        String newTag4 = values.getAsString("newTag");
        String newNumber3 = values.getAsString("newNumber");
        Integer idInt = values.getAsInteger(STR_INDEX);
        if (idInt == null) {
            index = 0;
        } else {
            int index2 = idInt.intValue();
            index = index2;
        }
        logi("update: index=" + index);
        if (7 == match || 8 == match) {
            int index3 = index;
            String pin24 = number;
            String strAnr = values.getAsString("newAnr");
            String strEmail = values.getAsString("newEmails");
            Integer aasIndex = values.getAsInteger("aas");
            String sne = values.getAsString("sne");
            if (newNumber3 != null) {
                newNumber = newNumber3;
            } else {
                newNumber = "";
            }
            if (newTag4 != null) {
                newTag = newTag4;
            } else {
                newTag = "";
            }
            MtkAdnRecord record = new MtkAdnRecord(efType, 0, newTag, newNumber);
            record.setAnr(strAnr);
            if (!values.containsKey("newAnr2")) {
                newTag2 = newTag;
            } else {
                String strAnr2 = values.getAsString("newAnr2");
                if (DBG) {
                    newTag2 = newTag;
                    log("update newAnr2: " + strAnr2);
                } else {
                    newTag2 = newTag;
                }
                record.setAnr(strAnr2, 1);
            }
            if (values.containsKey("newAnr3")) {
                String strAnr3 = values.getAsString("newAnr3");
                if (DBG) {
                    log("update newAnr3: " + strAnr3);
                }
                record.setAnr(strAnr3, 2);
            }
            if (strEmail != null && !strEmail.equals("")) {
                String[] emails3 = {strEmail};
                emails = emails3;
            } else {
                emails = null;
            }
            record.setEmails(emails);
            if (aasIndex != null) {
                record.setAasIndex(aasIndex.intValue());
            }
            if (sne != null) {
                record.setSne(sne);
            }
            if (index3 <= 0) {
                emails2 = emails;
                newNumber2 = newNumber;
                newTag3 = newTag2;
                if (ADDRESS_BOOK_COLUMN_NAMES.length >= 8) {
                    logi("updateUsimPBRecordsBySearchWithError");
                    result = updateUsimPBRecordsBySearchWithError(efType, new MtkAdnRecord(tag, pin24, ""), record, subId);
                } else {
                    logi("updateUsimRecordInEf");
                    result = updateUsimRecordInEf(efType, tag, pin24, newTag3, newNumber2, strAnr, strEmail, subId);
                }
            } else if (ADDRESS_BOOK_COLUMN_NAMES.length >= 8) {
                logi("updateUsimPBRecordsByIndexWithError");
                result = updateUsimPBRecordsByIndexWithError(efType, record, index3, subId);
                emails2 = emails;
                newNumber2 = newNumber;
                newTag3 = newTag2;
            } else {
                logi("updateUsimRecordInEfByIndex");
                emails2 = emails;
                newTag3 = newTag2;
                newNumber2 = newNumber;
                result = updateUsimRecordInEfByIndex(efType, index3, newTag3, newNumber, strAnr, strEmail, subId);
            }
            logi("update result = " + result);
            return result;
        }
        if (index > 0) {
            logi("updateIccRecordInEfByIndex");
            int index4 = subId;
            result = updateIccRecordInEfByIndex(efType, index, newTag4, newNumber3, pin2, index4);
        } else {
            logi("updateIccRecordInEf");
            result = updateIccRecordInEf(efType, tag, number, newTag4, newNumber3, pin2, subId);
        }
        logi("update result = " + result);
        return result;
    }

    private int addIccRecordToEf(int efType, String name, String number, String[] emails, String pin2, int subId) throws RemoteException {
        if (DBG) {
            log("addIccRecordToEf: efType=0x" + Integer.toHexString(efType).toUpperCase(Locale.ROOT) + ", name=" + getMaskString(name) + ", number=" + getMaskString(number) + ", emails=" + (emails == null ? "null" : getMaskString(emails[0])) + ", subscription=" + subId);
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateAdnRecordsInEfBySearchWithError(subId, efType, "", "", name, number, pin2);
            }
        } catch (RemoteException ex) {
            if (DBG) {
                log(ex.toString());
            }
        } catch (SecurityException ex2) {
            if (DBG) {
                log(ex2.toString());
            }
        }
        if (DBG) {
            log("addIccRecordToEf: " + result);
        }
        return result;
    }

    private int addUsimRecordToEf(int efType, String name, String number, String strAnr, String strEmail, String strGas, int subId) throws RemoteException {
        String[] emails;
        int result;
        if (DBG) {
            log("addUSIMRecordToEf: efType=" + efType + ", name=" + getMaskString(name) + ", number=" + getMaskString(number) + ", anr =" + getMaskString(strAnr) + ", emails=" + getMaskString(strEmail) + ", subId=" + subId);
        }
        int result2 = 0;
        if (strEmail != null && !strEmail.equals("")) {
            String[] emails2 = {strEmail};
            emails = emails2;
        } else {
            emails = null;
        }
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result2 = iccIpb.updateUsimPBRecordsInEfBySearchWithError(subId, efType, "", "", "", null, null, name, number, strAnr, null, emails);
            }
            result = result2;
        } catch (RemoteException ex) {
            log(ex.toString());
            result = 0;
        } catch (SecurityException ex2) {
            log(ex2.toString());
            result = 0;
        }
        log("addUsimRecordToEf: " + result);
        return result;
    }

    private int updateIccRecordInEf(int efType, String oldName, String oldNumber, String newName, String newNumber, String pin2, int subId) throws RemoteException {
        if (DBG) {
            log("updateIccRecordInEf: efType=0x" + Integer.toHexString(efType).toUpperCase(Locale.ROOT) + ", oldname=" + getMaskString(oldName) + ", oldnumber=" + getMaskString(oldNumber) + ", newname=" + getMaskString(newName) + ", newnumber=" + getMaskString(newNumber) + ", subscription=" + subId);
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateAdnRecordsInEfBySearchWithError(subId, efType, oldName, oldNumber, newName, newNumber, pin2);
            }
        } catch (RemoteException ex) {
            if (DBG) {
                log(ex.toString());
            }
        } catch (SecurityException ex2) {
            if (DBG) {
                log(ex2.toString());
            }
        }
        if (DBG) {
            log("updateIccRecordInEf: " + result);
        }
        return result;
    }

    private int updateIccRecordInEfByIndex(int efType, int nIndex, String newName, String newNumber, String pin2, int subId) throws RemoteException {
        if (DBG) {
            log("updateIccRecordInEfByIndex: efType=" + efType + ", index=" + nIndex + ", newname=" + getMaskString(newName) + ", newnumber=" + getMaskString(newNumber));
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateAdnRecordsInEfByIndexWithError(subId, efType, newName, newNumber, nIndex, pin2);
            }
        } catch (RemoteException ex) {
            log(ex.toString());
        } catch (SecurityException ex2) {
            log(ex2.toString());
        }
        log("updateIccRecordInEfByIndex: " + result);
        return result;
    }

    private int updateUsimRecordInEf(int efType, String oldName, String oldNumber, String newName, String newNumber, String strAnr, String strEmail, int subId) throws RemoteException {
        String[] emails;
        int result;
        if (DBG) {
            log("updateUsimRecordInEf: efType=" + efType + ", oldname=" + getMaskString(oldName) + ", oldnumber=" + getMaskString(oldNumber) + ", newname=" + getMaskString(newName) + ", newnumber=" + getMaskString(newNumber) + ", anr =" + getMaskString(strAnr) + ", emails=" + getMaskString(strEmail));
        }
        int result2 = 0;
        if (strEmail == null) {
            emails = null;
        } else {
            String[] emails2 = {strEmail};
            emails = emails2;
        }
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result2 = iccIpb.updateUsimPBRecordsInEfBySearchWithError(subId, efType, oldName, oldNumber, "", null, null, newName, newNumber, strAnr, null, emails);
            }
            result = result2;
        } catch (RemoteException ex) {
            log(ex.toString());
            result = 0;
        } catch (SecurityException ex2) {
            log(ex2.toString());
            result = 0;
        }
        log("updateUsimRecordInEf: " + result);
        return result;
    }

    private int updateUsimRecordInEfByIndex(int efType, int nIndex, String newName, String newNumber, String strAnr, String strEmail, int subId) throws RemoteException {
        String[] emails;
        int result;
        if (DBG) {
            log("updateUsimRecordInEfByIndex: efType=" + efType + ", Index=" + nIndex + ", newname=" + getMaskString(newName) + ", newnumber=" + getMaskString(newNumber) + ", anr =" + getMaskString(strAnr) + ", emails=" + getMaskString(strEmail));
        }
        int result2 = 0;
        if (strEmail == null) {
            emails = null;
        } else {
            String[] emails2 = {strEmail};
            emails = emails2;
        }
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result2 = iccIpb.updateUsimPBRecordsInEfByIndexWithError(subId, efType, newName, newNumber, strAnr, null, emails, nIndex);
            }
            result = result2;
        } catch (RemoteException ex) {
            log(ex.toString());
            result = 0;
        } catch (SecurityException ex2) {
            log(ex2.toString());
            result = 0;
        }
        log("updateUsimRecordInEfByIndex: " + result);
        return result;
    }

    private int deleteIccRecordFromEf(int efType, String name, String number, String[] emails, String pin2, int subId) throws RemoteException {
        if (DBG) {
            log("deleteIccRecordFromEf: efType=0x" + Integer.toHexString(efType).toUpperCase(Locale.ROOT) + ", name=" + getMaskString(name) + ", number=" + getMaskString(number) + ", pin2=" + getMaskString(pin2) + ", subscription=" + subId);
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateAdnRecordsInEfBySearchWithError(subId, efType, name, number, "", "", pin2);
            }
        } catch (RemoteException ex) {
            if (DBG) {
                log(ex.toString());
            }
        } catch (SecurityException ex2) {
            if (DBG) {
                log(ex2.toString());
            }
        }
        if (DBG) {
            log("deleteIccRecordFromEf: " + result);
        }
        return result;
    }

    private int deleteIccRecordFromEfByIndex(int efType, int nIndex, String pin2, int subId) throws RemoteException {
        if (DBG) {
            log("deleteIccRecordFromEfByIndex: efType=" + efType + ", index=" + nIndex + ", pin2=" + getMaskString(pin2));
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateAdnRecordsInEfByIndexWithError(subId, efType, "", "", nIndex, pin2);
            }
        } catch (RemoteException ex) {
            log(ex.toString());
        } catch (SecurityException ex2) {
            log(ex2.toString());
        }
        log("deleteIccRecordFromEfByIndex: " + result);
        return result;
    }

    private int deleteUsimRecordFromEf(int efType, String name, String number, String[] emails, int subId) throws RemoteException {
        int result;
        if (DBG) {
            log("deleteUsimRecordFromEf: efType=" + efType + ", name=" + getMaskString(name) + ", number=" + getMaskString(number));
        }
        int result2 = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result2 = iccIpb.updateUsimPBRecordsInEfBySearchWithError(subId, efType, name, number, "", null, null, "", "", "", null, null);
            }
            result = result2;
        } catch (RemoteException ex) {
            log(ex.toString());
            result = 0;
        } catch (SecurityException ex2) {
            log(ex2.toString());
            result = 0;
        }
        log("deleteUsimRecordFromEf: " + result);
        return result;
    }

    private int deleteUsimRecordFromEfByIndex(int efType, int nIndex, int subId) throws RemoteException {
        if (DBG) {
            log("deleteUsimRecordFromEfByIndex: efType=" + efType + ", index=" + nIndex);
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateUsimPBRecordsInEfByIndexWithError(subId, efType, "", "", "", null, null, nIndex);
            }
        } catch (RemoteException ex) {
            log(ex.toString());
        } catch (SecurityException ex2) {
            log(ex2.toString());
        }
        log("deleteUsimRecordFromEfByIndex: " + result);
        return result;
    }

    private void loadRecord(MtkAdnRecord record, MatrixCursor cursor, int id) {
        int len = ADDRESS_BOOK_COLUMN_NAMES.length;
        if (!record.isEmpty()) {
            Object[] contact = new Object[len];
            String alphaTag = record.getAlphaTag();
            String number = record.getNumber();
            String[] emails = record.getEmails();
            String grpIds = record.getGrpIds();
            String index = Integer.toString(record.getRecId());
            if (len >= 8) {
                int aasIndex = record.getAasIndex();
                contact[7] = Integer.valueOf(aasIndex);
            }
            if (len >= 9) {
                String sne = record.getSne();
                contact[8] = sne;
            }
            if (DBG) {
                log("loadRecord: record:" + record);
            }
            contact[0] = index;
            contact[1] = alphaTag;
            contact[2] = number;
            if (emails != null) {
                StringBuilder emailString = new StringBuilder();
                for (String email : emails) {
                    emailString.append(email);
                    emailString.append(",");
                }
                contact[3] = emailString.toString();
            }
            contact[4] = record.getAdditionalNumber();
            contact[5] = grpIds;
            contact[6] = Integer.valueOf(id);
            cursor.addRow(contact);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        Rlog.d(TAG, msg);
    }

    private void logi(String msg) {
        Rlog.i(TAG, msg);
    }

    private IMtkIccPhoneBook getIccPhbService() {
        IMtkIccPhoneBook iccIpb = IMtkIccPhoneBook.Stub.asInterface(ServiceManager.getService("mtksimphonebook"));
        return iccIpb;
    }

    private int getRequestSubId(Uri url) {
        if (DBG) {
            log("getRequestSubId url: " + url);
        }
        try {
            return Integer.parseInt(url.getLastPathSegment());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unknown URL " + url);
        }
    }

    private int updateUsimPBRecordsBySearchWithError(int efType, MtkAdnRecord oldAdn, MtkAdnRecord newAdn, int subId) throws RemoteException {
        if (DBG) {
            log("updateUsimPBRecordsBySearchWithError subId:" + subId + ",oldAdn:" + oldAdn + ",newAdn:" + newAdn);
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateUsimPBRecordsBySearchWithError(subId, efType, oldAdn, newAdn);
            }
        } catch (RemoteException ex) {
            log(ex.toString());
        } catch (SecurityException ex2) {
            log(ex2.toString());
        }
        log("updateUsimPBRecordsBySearchWithError: " + result);
        return result;
    }

    private int updateUsimPBRecordsByIndexWithError(int efType, MtkAdnRecord newAdn, int index, int subId) throws RemoteException {
        if (DBG) {
            log("updateUsimPBRecordsByIndexWithError subId:" + subId + ",index:" + index + ",newAdn:" + newAdn);
        }
        int result = 0;
        try {
            IMtkIccPhoneBook iccIpb = getIccPhbService();
            if (iccIpb != null) {
                result = iccIpb.updateUsimPBRecordsByIndexWithError(subId, efType, newAdn, index);
            }
        } catch (RemoteException ex) {
            log(ex.toString());
        } catch (SecurityException ex2) {
            log(ex2.toString());
        }
        log("updateUsimPBRecordsByIndexWithError: " + result);
        return result;
    }

    private void updatePhbStorageInfo(int update, int subId) {
        boolean res = false;
        try {
            int phoneId = SubscriptionManager.getPhoneId(subId);
            Phone phone = PhoneFactory.getPhone(phoneId);
            if (phone != null) {
                IccFileHandler mFh = phone.getIccFileHandler();
                if (!CsimPhbUtil.hasModemPhbEnhanceCapability(mFh)) {
                    res = CsimPhbUtil.updatePhbStorageInfo(update);
                } else {
                    log("[updatePhbStorageInfo] is not a csim card");
                    res = false;
                }
            }
        } catch (SecurityException ex) {
            log(ex.toString());
        }
        log("[updatePhbStorageInfo] res = " + res);
    }

    private String getMaskString(String str) {
        if (str == null) {
            return "null";
        }
        if (str.length() > 2) {
            return str.substring(0, str.length() >> 1) + "xxxxx";
        }
        return "xx";
    }

    private class PhbReadyRunnable implements Runnable {
        int mSubId;

        public PhbReadyRunnable(int subId) {
            this.mSubId = subId;
        }

        @Override // java.lang.Runnable
        public void run() throws RemoteException {
            MtkIccProvider.this.loadFromEf(28475, this.mSubId);
        }
    }
}
