package com.mediatek.internal.telephony.phb;

import android.content.ContentValues;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.uicc.MtkRuimRecords;
import com.mediatek.internal.telephony.uicc.MtkSIMRecords;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class MtkIccPhoneBookInterfaceManager extends IccPhoneBookInterfaceManager {
    protected static final boolean DBG = !SystemProperties.get("ro.build.type").equals(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    static final String LOG_TAG = "MtkIccPhoneBookIM";
    private int mErrorCause;
    private IccRecords mIccRecords;
    private volatile MtkAdnRecordCache mMtkAdnCache;
    protected Handler mMtkBaseHandler;
    private List<MtkAdnRecord> mRecords;
    private int mSlotId;

    public MtkIccPhoneBookInterfaceManager(Phone phone) {
        super(phone);
        this.mSlotId = -1;
        this.mMtkBaseHandler = new Handler() { // from class: com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                IccPhoneBookInterfaceManager.Request request = (IccPhoneBookInterfaceManager.Request) ar.userObj;
                switch (msg.what) {
                    case 1:
                        MtkIccPhoneBookInterfaceManager.this.mBaseHandler.handleMessage(msg);
                        break;
                    case 2:
                        List<MtkAdnRecord> records = null;
                        if (ar.exception == null) {
                            records = (List) ar.result;
                        } else {
                            MtkIccPhoneBookInterfaceManager.this.mtkLoge("EVENT_LOAD_DONE: Cannot load ADN records; ex=" + ar.exception);
                        }
                        notifyPending(request, records);
                        break;
                    case 3:
                        MtkIccPhoneBookInterfaceManager.this.mtkLogd("EVENT_UPDATE_DONE");
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        boolean success = ar2.exception == null;
                        MtkIccPhoneBookInterfaceManager.this.mtkLogd("EVENT_UPDATE_DONE success:" + success);
                        if (!success) {
                            if (ar2.exception instanceof CommandException) {
                                MtkIccPhoneBookInterfaceManager mtkIccPhoneBookInterfaceManager = MtkIccPhoneBookInterfaceManager.this;
                                mtkIccPhoneBookInterfaceManager.mErrorCause = mtkIccPhoneBookInterfaceManager.getErrorCauseFromException(ar2.exception);
                            } else {
                                MtkIccPhoneBookInterfaceManager.this.mtkLoge("Error : Unknow exception instance");
                                MtkIccPhoneBookInterfaceManager.this.mErrorCause = -10;
                            }
                        } else {
                            MtkIccPhoneBookInterfaceManager.this.mErrorCause = 1;
                        }
                        MtkIccPhoneBookInterfaceManager.this.mtkLogi("update done result: " + MtkIccPhoneBookInterfaceManager.this.mErrorCause);
                        notifyPending(request, Boolean.valueOf(success));
                        break;
                }
            }

            private void notifyPending(IccPhoneBookInterfaceManager.Request request, Object result) {
                if (request != null) {
                    synchronized (request) {
                        request.mResult = result;
                        request.mStatus.set(true);
                        request.notifyAll();
                    }
                }
            }
        };
    }

    public void updateIccRecords(IccRecords iccRecords) {
        super.updateIccRecords(iccRecords);
        this.mIccRecords = iccRecords;
        if (iccRecords != null) {
            this.mMtkAdnCache = (MtkAdnRecordCache) iccRecords.getAdnCache();
            if (this.mMtkAdnCache != null) {
                this.mSlotId = this.mMtkAdnCache.getSlotId();
            } else {
                mtkLogi("[updateIccRecords] mMtkAdnCache == null");
                this.mSlotId = -1;
            }
            mtkLogi("[updateIccRecords] Set mMtkAdnCache value slotid:" + this.mSlotId);
            return;
        }
        this.mMtkAdnCache = null;
        mtkLogi("[updateIccRecords] Set mMtkAdnCache value to null");
        this.mSlotId = -1;
    }

    protected void mtkLogd(String msg) {
        Rlog.d(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    protected void mtkLoge(String msg) {
        Rlog.e(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    protected void mtkLogi(String msg) {
        Rlog.i(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    public boolean updateAdnRecordsInEfBySearch(int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) {
        int result = updateAdnRecordsInEfBySearchWithError(efid, oldTag, oldPhoneNumber, newTag, newPhoneNumber, pin2);
        return result == 1;
    }

    public synchronized int updateAdnRecordsInEfBySearchWithError(int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) {
        int index = -1;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateAdnRecordsInEfBySearchWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateAdnRecordsInEfBySearch: efid=0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
        }
        int efid2 = updateEfForIccType(efid);
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (updateRequest) {
            Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
            MtkAdnRecord oldAdn = new MtkAdnRecord(oldTag, oldPhoneNumber);
            if (newPhoneNumber == null) {
                newPhoneNumber = "";
            }
            if (newTag == null) {
                newTag = "";
            }
            MtkAdnRecord newAdn = new MtkAdnRecord(newTag, newPhoneNumber);
            if (this.mMtkAdnCache != null) {
                index = this.mMtkAdnCache.updateAdnBySearch(efid2, oldAdn, newAdn, pin2, response, null);
                waitForResult(updateRequest);
            } else {
                mtkLoge("Failure while trying to update by search due to uninitialised adncache");
            }
        }
        int i = this.mErrorCause;
        if (i != 1) {
            return i;
        }
        mtkLogi("updateAdnRecordsInEfBySearchWithError success index is " + index);
        return index;
    }

    public boolean updateAdnRecordsInEfBySearchForSubscriber(int efid, ContentValues values, String pin2) {
        if (values == null) {
            return false;
        }
        String newTag = values.getAsString("newTag");
        String newPhoneNumber = values.getAsString("newNumber");
        String oldTag = values.getAsString("tag");
        String oldPhoneNumber = values.getAsString("number");
        int result = updateAdnRecordsInEfBySearchWithError(efid, oldTag, oldPhoneNumber, newTag, newPhoneNumber, pin2);
        if (result != 1) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public synchronized int updateUsimPBRecordsInEfBySearchWithError(int efid, String oldTag, String oldPhoneNumber, String oldAnr, String oldGrpIds, String[] oldEmails, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails) {
        IccPhoneBookInterfaceManager.Request updateRequest;
        int index;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsInEfBySearchWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateUsimPBRecordsInEfBySearchWithError: efid=" + efid + "oldAnr" + oldAnr + " oldGrpIds " + oldGrpIds + "==> newAnr= " + newAnr + " newGrpIds = " + newGrpIds);
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest2 = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (updateRequest2) {
                try {
                    Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest2);
                    MtkAdnRecord oldAdn = new MtkAdnRecord(oldTag, oldPhoneNumber);
                    try {
                        MtkAdnRecord newAdn = new MtkAdnRecord(0, 0, newTag == null ? "" : newTag, newPhoneNumber == null ? "" : newPhoneNumber, newAnr, newEmails, newGrpIds);
                        index = this.mMtkAdnCache.updateAdnBySearch(efid, oldAdn, newAdn, null, response, null);
                        waitForResult(updateRequest2);
                    } catch (Throwable th) {
                        th = th;
                        updateRequest = updateRequest2;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    updateRequest = updateRequest2;
                }
            }
            int i = this.mErrorCause;
            if (i != 1) {
                return i;
            }
            mtkLogi("updateUsimPBRecordsInEfBySearchWithError success index is " + index);
            return index;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public synchronized int updateUsimPBRecordsBySearchWithError(int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn) {
        int index;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsBySearchWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateUsimPBRecordsBySearchWithError: efid=" + efid + " (" + oldAdn + ")==>(" + newAdn + ")");
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (updateRequest) {
            Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
            if (newAdn.getNumber() == null) {
                newAdn.setNumber("");
            }
            index = this.mMtkAdnCache.updateAdnBySearch(efid, oldAdn, newAdn, null, response, null);
            waitForResult(updateRequest);
        }
        int i = this.mErrorCause;
        if (i != 1) {
            return i;
        }
        mtkLogi("updateUsimPBRecordsBySearchWithError success index is " + index);
        return index;
    }

    public boolean updateAdnRecordsInEfByIndex(int efid, String newTag, String newPhoneNumber, int index, String pin2) {
        int result = updateAdnRecordsInEfByIndexWithError(efid, newTag, newPhoneNumber, index, pin2);
        return result == 1;
    }

    public boolean updateAdnRecordsInEfByIndex(int efid, ContentValues values, int index, String pin2) {
        String newTag = null;
        String newPhoneNumber = null;
        if (values != null) {
            newTag = values.getAsString("newTag");
            newPhoneNumber = values.getAsString("newNumber");
        }
        int result = updateAdnRecordsInEfByIndexWithError(efid, newTag, newPhoneNumber, index, pin2);
        return result == 1;
    }

    public synchronized int updateAdnRecordsInEfByIndexWithError(int efid, String newTag, String newPhoneNumber, int index, String pin2) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateAdnRecordsInEfByIndex mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateAdnRecordsInEfByIndex: efid=0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + " Index=" + index);
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (updateRequest) {
            Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
            if (newPhoneNumber == null) {
                newPhoneNumber = "";
            }
            if (newTag == null) {
                newTag = "";
            }
            MtkAdnRecord newAdn = new MtkAdnRecord(newTag, newPhoneNumber);
            if (this.mMtkAdnCache != null) {
                this.mMtkAdnCache.updateAdnByIndex(efid, newAdn, index, pin2, response);
                waitForResult(updateRequest);
            } else {
                mtkLoge("Failure while trying to update by index due to uninitialised adncache");
            }
        }
        return this.mErrorCause;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public synchronized int updateUsimPBRecordsInEfByIndexWithError(int efid, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails, int index) {
        Message response;
        String str = "android.permission.WRITE_CONTACTS";
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsInEfByIndexWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            str = " newGrpIds = ";
            mtkLogd("updateUsimPBRecordsInEfByIndexWithError: efid=" + efid + " Index=" + index + " ==>  newAnr= " + newAnr + " newGrpIds = " + newGrpIds);
        }
        checkThread();
        String request = new IccPhoneBookInterfaceManager.Request();
        try {
            synchronized (request) {
                try {
                    response = this.mMtkBaseHandler.obtainMessage(3, request);
                } catch (Throwable th) {
                    th = th;
                    str = request;
                }
                try {
                    MtkAdnRecord newAdn = new MtkAdnRecord(efid, index, newTag == null ? "" : newTag, newPhoneNumber == null ? "" : newPhoneNumber, newAnr, newEmails, newGrpIds);
                    this.mMtkAdnCache.updateAdnByIndex(efid, newAdn, index, null, response);
                    waitForResult(request);
                } catch (Throwable th2) {
                    th = th2;
                    str = request;
                    throw th;
                }
            }
            return this.mErrorCause;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public synchronized int updateUsimPBRecordsByIndexWithError(int efid, MtkAdnRecord record, int index) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("updateUsimPBRecordsByIndexWithError mMtkAdnCache is null");
            return 0;
        }
        if (DBG) {
            mtkLogd("updateUsimPBRecordsByIndexWithError: efid=" + efid + " Index=" + index + " ==> " + record);
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request updateRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (updateRequest) {
            Message response = this.mMtkBaseHandler.obtainMessage(3, updateRequest);
            this.mMtkAdnCache.updateAdnByIndex(efid, record, index, null, response);
            waitForResult(updateRequest);
        }
        return this.mErrorCause;
    }

    private String getAdnEFPath(int efid) {
        if (efid == 28474) {
            return "3F007F10";
        }
        return null;
    }

    public synchronized int[] getAdnRecordsSize(int efid) {
        mtkLogd("getAdnRecordsSize: efid=" + efid);
        checkThread();
        IccPhoneBookInterfaceManager.Request getSizeRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (getSizeRequest) {
            try {
                Message response = this.mMtkBaseHandler.obtainMessage(1, getSizeRequest);
                IccFileHandler fh = this.mPhone.getIccFileHandler();
                if (fh != null) {
                    try {
                        if (getAdnEFPath(efid) != null) {
                            fh.getEFLinearRecordSize(efid, getAdnEFPath(efid), response);
                        } else {
                            fh.getEFLinearRecordSize(efid, response);
                        }
                        waitForResult(getSizeRequest);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                if (efid == 28474 && getSizeRequest.mResult != null) {
                    int[] capabilityResult = getAdnRecordsCapacityExt();
                    int[] result = new int[3];
                    result[0] = ((int[]) getSizeRequest.mResult)[0];
                    if (capabilityResult != null) {
                        result[2] = capabilityResult[0];
                        result[1] = result[0] * result[2];
                    }
                    mtkLogd("getAdnRecordsSize ADN " + result[0] + "," + result[1] + "," + result[2]);
                    return result;
                }
                if (efid == 28489 && getSizeRequest.mResult != null) {
                    int[] result2 = new int[3];
                    result2[0] = ((int[]) getSizeRequest.mResult)[0];
                    result2[2] = ((int[]) getSizeRequest.mResult)[2];
                    if (result2[0] < 25) {
                        result2[0] = 26;
                    }
                    result2[1] = result2[0] * result2[2];
                    mtkLogd("getAdnRecordsSize SDN " + result2[0] + "," + result2[1] + "," + result2[2]);
                    return result2;
                }
                return getSizeRequest.mResult == null ? new int[3] : (int[]) getSizeRequest.mResult;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public synchronized List<MtkAdnRecord> getAdnRecordsInEf(int efid, Object object) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        int efid2 = updateEfForIccType(efid);
        if (DBG) {
            mtkLogd("getAdnRecordsInEF: efid=0x" + Integer.toHexString(efid2).toUpperCase(Locale.ROOT));
        }
        if (this.mMtkAdnCache == null) {
            mtkLoge("getAdnRecordsInEF mMtkAdnCache is null");
            return null;
        }
        checkThread();
        IccPhoneBookInterfaceManager.Request loadRequest = new IccPhoneBookInterfaceManager.Request();
        synchronized (loadRequest) {
            Message response = this.mMtkBaseHandler.obtainMessage(2, loadRequest);
            if (this.mMtkAdnCache != null) {
                this.mMtkAdnCache.requestLoadAllAdnLike(efid2, this.mMtkAdnCache.extensionEfForEf(efid2), response);
                waitForResult(loadRequest);
            } else {
                mtkLoge("Failure while trying to load from SIM due to uninitialised adncache");
            }
        }
        return (List) loadRequest.mResult;
    }

    protected void checkThread() {
        if (this.mMtkBaseHandler.getLooper().equals(Looper.myLooper())) {
            mtkLoge("query() called on the main UI thread!");
            throw new IllegalStateException("You cannot call query on this provder from the main UI thread.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getErrorCauseFromException(CommandException e) {
        if (e == null) {
            return 1;
        }
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$CommandException$Error[e.getCommandError().ordinal()]) {
            case 1:
                return -10;
            case 2:
                return -1;
            case 3:
            case 4:
                return -5;
            case 5:
                return -2;
            case 6:
                return -3;
            case 7:
                return -4;
            case 8:
                return -6;
            case 9:
                return -14;
            case 10:
            case 11:
                return -11;
            case 12:
                return -12;
            case 13:
                return -13;
            case 14:
                return -16;
            case 15:
                return -17;
            default:
                return 0;
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.phb.MtkIccPhoneBookInterfaceManager$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;

        static {
            int[] iArr = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr;
            try {
                iArr[CommandException.Error.GENERIC_FAILURE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_1.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_PUK2.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.PASSWORD_INCORRECT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_2.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_3.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_4.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_5.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_6.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_7.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_8.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_9.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_10.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OEM_ERROR_11.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    public void onPhbReady() {
        if (this.mMtkAdnCache != null) {
            this.mMtkAdnCache.requestLoadAllAdnLike(28474, this.mMtkAdnCache.extensionEfForEf(28474), null);
        }
    }

    public boolean isPhbReady() {
        IccRecords iccRecords;
        if (this.mMtkAdnCache == null || !SubscriptionManager.isValidSlotIndex(this.mSlotId) || (iccRecords = this.mIccRecords) == null) {
            return false;
        }
        if (iccRecords instanceof MtkSIMRecords) {
            boolean phbReady = ((MtkSIMRecords) iccRecords).isPhbReady();
            return phbReady;
        }
        if (!(iccRecords instanceof MtkRuimRecords)) {
            return false;
        }
        boolean phbReady2 = ((MtkRuimRecords) iccRecords).isPhbReady();
        return phbReady2;
    }

    public List<UsimGroup> getUsimGroups() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return null;
        }
        return adnCache.getUsimGroups();
    }

    public String getUsimGroupById(int nGasId) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return null;
        }
        return adnCache.getUsimGroupById(nGasId);
    }

    public boolean removeUsimGroupById(int nGasId) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.removeUsimGroupById(nGasId);
    }

    public int insertUsimGroup(String grpName) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.insertUsimGroup(grpName);
    }

    public int updateUsimGroup(int nGasId, String grpName) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.updateUsimGroup(nGasId, grpName);
    }

    public boolean addContactToGroup(int adnIndex, int grpIndex) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.addContactToGroup(adnIndex, grpIndex);
    }

    public boolean removeContactFromGroup(int adnIndex, int grpIndex) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.removeContactFromGroup(adnIndex, grpIndex);
    }

    public boolean updateContactToGroups(int adnIndex, int[] grpIdList) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.updateContactToGroups(adnIndex, grpIdList);
    }

    public boolean moveContactFromGroupsToGroups(int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.moveContactFromGroupsToGroups(adnIndex, fromGrpIdList, toGrpIdList);
    }

    public int hasExistGroup(String grpName) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.hasExistGroup(grpName);
    }

    public int getUsimGrpMaxNameLen() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.getUsimGrpMaxNameLen();
    }

    public int getUsimGrpMaxCount() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.getUsimGrpMaxCount();
    }

    public List<AlphaTag> getUsimAasList() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return null;
        }
        return adnCache.getUsimAasList();
    }

    public String getUsimAasById(int index) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return null;
        }
        return adnCache.getUsimAasById(index);
    }

    public boolean removeUsimAasById(int index, int pbrIndex) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.removeUsimAasById(index, pbrIndex);
    }

    public int insertUsimAas(String aasName) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.insertUsimAas(aasName);
    }

    public boolean updateUsimAas(int index, int pbrIndex, String aasName) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.updateUsimAas(index, pbrIndex, aasName);
    }

    public boolean updateAdnAas(int adnIndex, int aasIndex) {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.WRITE_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.WRITE_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.updateAdnAas(adnIndex, aasIndex);
    }

    public int getAnrCount() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return 0;
        }
        return adnCache.getAnrCount();
    }

    public int getEmailCount() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return 0;
        }
        return adnCache.getEmailCount();
    }

    public int getUsimAasMaxCount() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.getUsimAasMaxCount();
    }

    public int getUsimAasMaxNameLen() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.getUsimAasMaxNameLen();
    }

    public boolean hasSne() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.hasSne();
    }

    public int getSneRecordLen() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.getSneRecordLen();
    }

    public boolean isAdnAccessible() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return false;
        }
        return adnCache.isAdnAccessible();
    }

    public synchronized UsimPBMemInfo[] getPhonebookMemStorageExt() {
        MtkAdnRecordCache adnCache;
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        adnCache = this.mMtkAdnCache;
        return adnCache == null ? null : adnCache.getPhonebookMemStorageExt();
    }

    public int getUpbDone() {
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return -1;
        }
        return adnCache.getUpbDone();
    }

    public int[] getAdnRecordsCapacityExt() {
        if (this.mPhone.getContext().checkCallingOrSelfPermission("android.permission.READ_CONTACTS") != 0) {
            throw new SecurityException("Requires android.permission.READ_CONTACTS permission");
        }
        MtkAdnRecordCache adnCache = this.mMtkAdnCache;
        if (adnCache == null) {
            return null;
        }
        return adnCache.getAdnRecordsCapacityExt();
    }
}
