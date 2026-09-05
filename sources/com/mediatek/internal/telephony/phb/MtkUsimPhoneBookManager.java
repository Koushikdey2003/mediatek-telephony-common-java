package com.mediatek.internal.telephony.phb;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.SimTlv;
import com.android.internal.telephony.gsm.UsimPhoneBookManager;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccException;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.mediatek.internal.telephony.MtkPhoneNumberUtils;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.uicc.EFResponseData;
import com.mediatek.telephony.internal.telephony.vsim.ExternalSimConstants;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class MtkUsimPhoneBookManager extends UsimPhoneBookManager {
    private static final boolean DBG;
    private static final int EVENT_AAS_LOAD_DONE = 5;
    private static final int EVENT_AAS_LOAD_DONE_OPTMZ = 28;
    private static final int EVENT_AAS_UPDATE_DONE = 10;
    private static final int EVENT_ANR_RECORD_LOAD_DONE = 16;
    private static final int EVENT_ANR_RECORD_LOAD_OPTMZ_DONE = 23;
    private static final int EVENT_ANR_UPDATE_DONE = 9;
    private static final int EVENT_EMAIL_RECORD_LOAD_DONE = 15;
    private static final int EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE = 22;
    private static final int EVENT_EMAIL_UPDATE_DONE = 8;
    private static final int EVENT_EXT1_LOAD_DONE = 1001;
    private static final int EVENT_GAS_LOAD_DONE = 6;
    private static final int EVENT_GAS_UPDATE_DONE = 13;
    private static final int EVENT_GET_RECORDS_SIZE_DONE = 1000;
    private static final int EVENT_GRP_RECORD_LOAD_DONE = 17;
    private static final int EVENT_GRP_UPDATE_DONE = 12;
    private static final int EVENT_IAP_RECORD_LOAD_DONE = 14;
    private static final int EVENT_IAP_UPDATE_DONE = 7;
    private static final int EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE = 26;
    private static final int EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE = 25;
    private static final int EVENT_QUERY_PHB_ADN_INFO = 21;
    private static final int EVENT_QUERY_SNE_AVAILABLE_OPTMZ_DONE = 27;
    private static final int EVENT_SELECT_EF_FILE_DONE = 20;
    private static final int EVENT_SNE_RECORD_LOAD_DONE = 18;
    private static final int EVENT_SNE_RECORD_LOAD_OPTMZ_DONE = 24;
    private static final int EVENT_SNE_UPDATE_DONE = 11;
    private static final int EVENT_UPB_CAPABILITY_QUERY_DONE = 19;
    private static final String LOG_TAG = "MtkUsimPhoneBookManager";
    private static final int PBR_NOT_NEED_NOTIFY = -1;
    private static final String PROP_FORCE_DEBUG_KEY = "persist.vendor.log.tel_dbg";
    private static final int UPB_EF_AAS = 3;
    private static final int UPB_EF_ANR = 0;
    private static final int UPB_EF_EMAIL = 1;
    private static final int UPB_EF_GAS = 4;
    private static final int UPB_EF_GRP = 5;
    private static final int UPB_EF_SNE = 2;
    private static final int USIM_DEFAULT_MAX_ADN_FILE_SIZE = 250;
    private static final int USIM_DEFAULT_MAX_EMAIL_FILE_SIZE = 100;
    public static final int USIM_ERROR_CAPACITY_FULL = -30;
    public static final int USIM_ERROR_GROUP_COUNT = -20;
    public static final int USIM_ERROR_NAME_LEN = -10;
    public static final int USIM_ERROR_OTHERS = -50;
    public static final int USIM_ERROR_STRING_TOOLONG = -40;
    private static final int USIM_MAX_AAS_ENTRIES_COUNT = 5;
    public static final int USIM_MAX_ANR_COUNT = 3;
    private static final int USIM_TYPE2_CONDITIONAL_LENGTH = 2;
    private ArrayList<String> mAasForAnr;
    private final Object mAasLock;
    private MtkAdnRecordCache mAdnCache;
    private int mAdnFileSize;
    private int[] mAdnRecordSize;
    private ArrayList<int[]> mAnrInfo;
    private int mAnrRecordSize;
    protected Handler mBaseHandler;
    private MtkRIL mCi;
    private UiccCardApplication mCurrentApp;
    protected EFResponseData mEfData;
    private int mEmailFileSize;
    private int[] mEmailInfo;
    private int[] mEmailRecTable;
    private int mEmailRecordSize;
    private ArrayList<ArrayList<byte[]>> mExt1FileList;
    private ArrayList<UsimGroup> mGasForGrp;
    private final Object mGasLock;
    private ArrayList<ArrayList<byte[]>> mIapFileList;
    private boolean mIsReset;
    private AtomicBoolean mNeedNotify;
    private int mPbrNeedNotify;
    private ArrayList<PbrRecord> mPbrRecords;
    private ArrayList<MtkAdnRecord> mPhoneBookRecords;
    private int mReadEFLinerRecordSizeNum;
    private AtomicInteger mReadingAnrNum;
    private AtomicInteger mReadingEmailNum;
    private AtomicInteger mReadingGrpNum;
    private AtomicInteger mReadingIapNum;
    private AtomicInteger mReadingSneNum;
    private SparseArray<int[]> mRecordSize;
    private boolean mRefreshAdnInfo;
    private boolean mRefreshAnrInfo;
    private boolean mRefreshEmailInfo;
    private int mResult;
    private int mSliceCount;
    private int mSlotId;
    private int[] mSneInfo;
    private final Object mUPBCapabilityLock;
    private int[] mUpbCap;
    private int mUpbDone;

    public static final class Request {
        public AtomicBoolean mStatus = new AtomicBoolean(false);
        public Object mResult = null;
    }

    static {
        boolean z = false;
        if (SystemProperties.getInt(PROP_FORCE_DEBUG_KEY, 0) == 1 && !SystemProperties.get("ro.build.type").equals(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER)) {
            z = true;
        }
        DBG = z;
    }

    private class File {
        public int mAnrIndex;
        private final int mEfid;
        private final int mIndex;
        private final int mParentTag;
        public int mPbrRecord;
        private final int mSfi;
        public int mTag;

        File(int parentTag, int efid, int sfi, int index) {
            this.mParentTag = parentTag;
            this.mEfid = efid;
            this.mSfi = sfi;
            this.mIndex = index;
        }

        public int getParentTag() {
            return this.mParentTag;
        }

        public int getEfid() {
            return this.mEfid;
        }

        public int getSfi() {
            return this.mSfi;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public String toString() {
            return "mParentTag:" + Integer.toHexString(this.mParentTag).toUpperCase(Locale.ROOT) + ",mEfid:" + Integer.toHexString(this.mEfid).toUpperCase(Locale.ROOT) + ",mSfi:" + Integer.toHexString(this.mSfi).toUpperCase(Locale.ROOT) + ",mIndex:" + this.mIndex + ",mPbrRecord:" + this.mPbrRecord + ",mAnrIndex" + this.mAnrIndex + ",mTag:" + Integer.toHexString(this.mTag).toUpperCase(Locale.ROOT);
        }
    }

    public MtkUsimPhoneBookManager(IccFileHandler fh, AdnRecordCache cache) {
        super(fh, cache);
        this.mSlotId = -1;
        this.mGasLock = new Object();
        this.mUPBCapabilityLock = new Object();
        this.mAasLock = new Object();
        this.mEmailRecordSize = -1;
        this.mEmailFileSize = 100;
        this.mAdnFileSize = USIM_DEFAULT_MAX_ADN_FILE_SIZE;
        this.mAnrRecordSize = -1;
        this.mSliceCount = 0;
        this.mUpbDone = -1;
        this.mIsReset = false;
        this.mPbrNeedNotify = -1;
        this.mReadEFLinerRecordSizeNum = 0;
        this.mIapFileList = null;
        this.mRefreshEmailInfo = false;
        this.mRefreshAnrInfo = false;
        this.mRefreshAdnInfo = false;
        this.mEmailRecTable = new int[400];
        this.mUpbCap = new int[8];
        this.mResult = -1;
        this.mReadingAnrNum = new AtomicInteger(0);
        this.mReadingEmailNum = new AtomicInteger(0);
        this.mReadingGrpNum = new AtomicInteger(0);
        this.mReadingSneNum = new AtomicInteger(0);
        this.mReadingIapNum = new AtomicInteger(0);
        this.mNeedNotify = new AtomicBoolean(false);
        this.mEfData = null;
        this.mBaseHandler = new Handler() { // from class: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                Request request = (Request) ar.userObj;
                switch (msg.what) {
                    case MtkUsimPhoneBookManager.EVENT_GET_RECORDS_SIZE_DONE /* 1000 */:
                        int efid = msg.arg1;
                        MtkUsimPhoneBookManager.this.logi("EVENT_GET_RECORDS_SIZE_DONE done, recNum:" + MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum + ", ef_id:" + efid);
                        if (ar.exception == null) {
                            int[] recordSize = (int[]) ar.result;
                            if (recordSize.length == 3) {
                                if (MtkUsimPhoneBookManager.this.mRecordSize == null) {
                                    MtkUsimPhoneBookManager.this.mRecordSize = new SparseArray();
                                }
                                MtkUsimPhoneBookManager.this.mRecordSize.put(efid, recordSize);
                            } else {
                                Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get wrong record size format" + ar.exception);
                            }
                        } else {
                            Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get EF record size failed" + ar.exception);
                        }
                        if (MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum > 0) {
                            notifyPending(request, MtkUsimPhoneBookManager.this.mRecordSize);
                        }
                        break;
                }
            }

            private void notifyPending(Request request, Object result) {
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

    public MtkUsimPhoneBookManager(IccFileHandler fh, AdnRecordCache cache, CommandsInterface ci, UiccCardApplication app) {
        super(fh, cache);
        this.mSlotId = -1;
        this.mGasLock = new Object();
        this.mUPBCapabilityLock = new Object();
        this.mAasLock = new Object();
        this.mEmailRecordSize = -1;
        this.mEmailFileSize = 100;
        this.mAdnFileSize = USIM_DEFAULT_MAX_ADN_FILE_SIZE;
        this.mAnrRecordSize = -1;
        this.mSliceCount = 0;
        this.mUpbDone = -1;
        this.mIsReset = false;
        this.mPbrNeedNotify = -1;
        this.mReadEFLinerRecordSizeNum = 0;
        this.mIapFileList = null;
        this.mRefreshEmailInfo = false;
        this.mRefreshAnrInfo = false;
        this.mRefreshAdnInfo = false;
        this.mEmailRecTable = new int[400];
        this.mUpbCap = new int[8];
        this.mResult = -1;
        this.mReadingAnrNum = new AtomicInteger(0);
        this.mReadingEmailNum = new AtomicInteger(0);
        this.mReadingGrpNum = new AtomicInteger(0);
        this.mReadingSneNum = new AtomicInteger(0);
        this.mReadingIapNum = new AtomicInteger(0);
        this.mNeedNotify = new AtomicBoolean(false);
        this.mEfData = null;
        this.mBaseHandler = new Handler() { // from class: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                AsyncResult ar = (AsyncResult) msg.obj;
                Request request = (Request) ar.userObj;
                switch (msg.what) {
                    case MtkUsimPhoneBookManager.EVENT_GET_RECORDS_SIZE_DONE /* 1000 */:
                        int efid = msg.arg1;
                        MtkUsimPhoneBookManager.this.logi("EVENT_GET_RECORDS_SIZE_DONE done, recNum:" + MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum + ", ef_id:" + efid);
                        if (ar.exception == null) {
                            int[] recordSize = (int[]) ar.result;
                            if (recordSize.length == 3) {
                                if (MtkUsimPhoneBookManager.this.mRecordSize == null) {
                                    MtkUsimPhoneBookManager.this.mRecordSize = new SparseArray();
                                }
                                MtkUsimPhoneBookManager.this.mRecordSize.put(efid, recordSize);
                            } else {
                                Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get wrong record size format" + ar.exception);
                            }
                        } else {
                            Rlog.e(MtkUsimPhoneBookManager.LOG_TAG, "get EF record size failed" + ar.exception);
                        }
                        if (MtkUsimPhoneBookManager.this.mReadEFLinerRecordSizeNum > 0) {
                            notifyPending(request, MtkUsimPhoneBookManager.this.mRecordSize);
                        }
                        break;
                }
            }

            private void notifyPending(Request request, Object result) {
                if (request != null) {
                    synchronized (request) {
                        request.mResult = result;
                        request.mStatus.set(true);
                        request.notifyAll();
                    }
                }
            }
        };
        this.mFh = fh;
        this.mPhoneBookRecords = new ArrayList<>();
        this.mGasForGrp = new ArrayList<>();
        this.mIapFileList = new ArrayList<>();
        this.mPbrRecords = null;
        this.mIsPbrPresent = true;
        this.mAdnCache = (MtkAdnRecordCache) cache;
        this.mCi = (MtkRIL) ci;
        this.mCurrentApp = app;
        this.mSlotId = app == null ? -1 : app.getPhoneId();
        this.mEmailsForAdnRec = new SparseArray();
        this.mSfiEfidTable = new SparseIntArray();
        for (int i = 0; i < 8; i++) {
            this.mUpbCap[i] = -1;
        }
        logi("constructor finished. ");
    }

    public void reset() {
        this.mIsReset = true;
        this.mPhoneBookRecords.clear();
        this.mIapFileRecord = null;
        this.mEmailFileRecord = null;
        this.mPbrRecords = null;
        this.mIsPbrPresent = true;
        this.mRefreshCache = false;
        this.mEmailsForAdnRec.clear();
        this.mSfiEfidTable.clear();
        this.mGasForGrp.clear();
        this.mIapFileList = null;
        this.mAasForAnr = null;
        this.mExt1FileList = null;
        this.mSliceCount = 0;
        this.mEmailRecTable = new int[400];
        this.mEmailInfo = null;
        this.mSneInfo = null;
        this.mAnrInfo = null;
        for (int i = 0; i < 8; i++) {
            this.mUpbCap[i] = -1;
        }
        this.mEmailRecordSize = -1;
        this.mAnrRecordSize = -1;
        this.mUpbDone = -1;
        this.mAdnRecordSize = null;
        this.mRefreshEmailInfo = false;
        this.mRefreshAnrInfo = false;
        this.mRefreshAdnInfo = false;
        synchronized (this.mLock) {
            this.mLock.notifyAll();
        }
        this.mPbrNeedNotify = -1;
        logi("reset finished, mPbrNeedNotify = " + this.mPbrNeedNotify);
    }

    public ArrayList<MtkAdnRecord> loadEfFilesFromUsim(Object object) {
        int[] size;
        long prevTime = System.currentTimeMillis();
        synchronized (this.mLock) {
            this.mIsReset = false;
            if (!this.mPhoneBookRecords.isEmpty()) {
                if (this.mRefreshCache) {
                    this.mRefreshCache = false;
                    refreshCache();
                }
                return this.mPhoneBookRecords;
            }
            if (!this.mIsPbrPresent.booleanValue()) {
                return null;
            }
            logi("loadEfFilesFromUsim, mPbrNeedNotify =" + this.mPbrNeedNotify);
            ArrayList<PbrRecord> arrayList = this.mPbrRecords;
            if (arrayList == null || arrayList.size() == 0) {
                this.mPbrNeedNotify++;
                readPbrFileAndWait();
            }
            ArrayList<PbrRecord> arrayList2 = this.mPbrRecords;
            if (arrayList2 == null || arrayList2.size() == 0) {
                if (checkIsPhbReady() && !this.mIsReset) {
                    if (true == readAdnFileAndWait(0)) {
                        this.mIsPbrPresent = false;
                        this.mEmailRecordSize = 0;
                        this.mAnrRecordSize = 0;
                        this.mUpbDone = 1;
                    }
                    logi("loadEfFilesFromUsim getRecordIfLoaded EF_ADN pbrP:" + this.mIsPbrPresent);
                    return this.mAdnCache.getRecordsIfLoaded(28474, null);
                }
                logi("loadEfFilesFromUsim phb not ready and Reset");
                return null;
            }
            logi("loadEfFilesFromUsim mPbrNeedNotify:" + this.mPbrNeedNotify);
            int adnEf = -1;
            int sneEf = -1;
            int numRecs = this.mPbrRecords.size();
            if (this.mPbrRecords.get(0).mFileIds.get(192) != null) {
                adnEf = ((File) this.mPbrRecords.get(0).mFileIds.get(192)).getEfid();
            }
            if (this.mPbrRecords.get(0).mFileIds.get(195) != null) {
                sneEf = ((File) this.mPbrRecords.get(0).mFileIds.get(195)).getEfid();
            }
            logi("loadEfFilesFromUsim adnEf:" + adnEf + ", sneEf:" + sneEf + ", numRecs:" + numRecs + ", mEmailRecordSize:" + this.mEmailRecordSize + ", mAnrRecordSize:" + this.mAnrRecordSize);
            if (this.mEmailRecordSize < 0) {
                readEmailRecordSize();
            }
            if (this.mAnrRecordSize < 0) {
                readAnrRecordSize();
            }
            ArrayList<PbrRecord> arrayList3 = this.mPbrRecords;
            if (arrayList3 == null || arrayList3.size() == 0) {
                logi("loadEfFilesFromUsim mPbrRecords is null");
                return null;
            }
            if (adnEf > 0 && (size = readEFLinearRecordSize(adnEf)) != null && size.length == 3) {
                this.mAdnFileSize = size[2];
            }
            if (sneEf > 0) {
                readEFLinearRecordSize(sneEf);
            }
            if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
                for (int i = 0; i < numRecs; i++) {
                    readAASFileAndWait(i);
                    readAdnFileAndWaitForUICC(i);
                }
            } else {
                readAasFileAndWaitOptmz();
                readAdnFileAndWait(0);
            }
            if (this.mPhoneBookRecords.isEmpty()) {
                logi("loadEfFilesFromUsim mPhoneBookRecords Empty");
                return this.mPhoneBookRecords;
            }
            if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
                for (int i2 = 0; i2 < numRecs; i2++) {
                    readSneFileAndWait(i2);
                    readAnrFileAndWait(i2);
                    readEmailFileAndWait(i2);
                }
            } else {
                logi("loadEfFilesFromUsim Speed up read begin");
                readSneFileAndWaitOptmz();
                readAnrFileAndWaitOptmz();
                readEmailFileAndWaitOptmz();
                logi("loadEfFilesFromUsim Speed up read end");
            }
            readGrpIdsAndWait();
            ArrayList<PbrRecord> arrayList4 = this.mPbrRecords;
            if (arrayList4 != null) {
                this.mUpbDone = 1;
            }
            if (arrayList4 != null) {
                long endTime = System.currentTimeMillis();
                logi("loadEfFilesFromUsim Time: " + (endTime - prevTime) + " AppType: " + this.mCurrentApp.getType());
            } else {
                logi("loadEfFilesFromUsim end");
            }
            return this.mPhoneBookRecords;
        }
    }

    private void readEmailFileAndWait(int recId) throws Throwable {
        SparseArray<File> files;
        File emailFile;
        logi("readEmailFileAndWait " + recId);
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList != null && arrayList.size() != 0 && (files = this.mPbrRecords.get(recId).mFileIds) != null && (emailFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP)) != null) {
            emailFile.getEfid();
            if (emailFile.getParentTag() == 168) {
                readType1Ef(emailFile, 0);
            } else if (emailFile.getParentTag() == 169) {
                readType2Ef(emailFile);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x014c A[Catch: all -> 0x01b9, TryCatch #2 {, blocks: (B:40:0x00dd, B:41:0x00e6, B:43:0x00f0, B:46:0x011c, B:45:0x00fa, B:49:0x0144, B:51:0x014c, B:52:0x0152, B:54:0x0154, B:55:0x017f, B:59:0x018d, B:58:0x0186, B:48:0x0121), top: B:71:0x00dd, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0154 A[Catch: all -> 0x01b9, TRY_LEAVE, TryCatch #2 {, blocks: (B:40:0x00dd, B:41:0x00e6, B:43:0x00f0, B:46:0x011c, B:45:0x00fa, B:49:0x0144, B:51:0x014c, B:52:0x0152, B:54:0x0154, B:55:0x017f, B:59:0x018d, B:58:0x0186, B:48:0x0121), top: B:71:0x00dd, inners: #0, #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readIapFileAndWait(int r18, int r19, boolean r20) {
        /*
            Method dump skipped, instruction units count: 462
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readIapFileAndWait(int, int, boolean):void");
    }

    private void readAASFileAndWait(int recId) {
        SparseArray<File> files;
        File aasFile;
        logi("readAASFileAndWait " + recId);
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0 || (files = this.mPbrRecords.get(recId).mFileIds) == null || (aasFile = files.get(199)) == null) {
            return;
        }
        int aasEfid = aasFile.getEfid();
        log("readAASFileAndWait-get AAS EFID " + aasEfid);
        if (this.mAasForAnr != null) {
            logi("AAS has been loaded for Pbr number " + recId);
        }
        if (this.mFh != null) {
            synchronized (this.mLock) {
                Message msg = obtainMessage(5);
                msg.arg1 = recId;
                this.mFh.loadEFLinearFixedAll(aasEfid, msg);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readAASFileAndWait");
                }
            }
            return;
        }
        Rlog.e(LOG_TAG, "readAASFileAndWait-IccFileHandler is null");
    }

    private void readSneFileAndWait(int recId) throws Throwable {
        SparseArray<File> files;
        File sneFile;
        logi("readSneFileAndWait " + recId);
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0 || (files = this.mPbrRecords.get(recId).mFileIds) == null || (sneFile = files.get(195)) == null) {
            return;
        }
        int sneEfid = sneFile.getEfid();
        log("readSneFileAndWait: EFSNE id is " + sneEfid);
        if (sneFile.getParentTag() == 169) {
            readType2Ef(sneFile);
        } else if (sneFile.getParentTag() == 168) {
            readType1Ef(sneFile, 0);
        }
    }

    private void readAnrFileAndWait(int recId) throws Throwable {
        logi("readAnrFileAndWait: recId is " + recId);
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        SparseArray<File> files = this.mPbrRecords.get(recId).mFileIds;
        if (files == null) {
            log("readAnrFileAndWait: No anr tag in pbr record " + recId);
            return;
        }
        for (int index = 0; index < this.mPbrRecords.get(recId).mAnrIndex; index++) {
            File anrFile = files.get((index * 256) + 196);
            if (anrFile != null) {
                if (anrFile.getParentTag() == 169) {
                    anrFile.mAnrIndex = index;
                    readType2Ef(anrFile);
                    return;
                } else {
                    if (anrFile.getParentTag() == 168) {
                        anrFile.mAnrIndex = index;
                        readType1Ef(anrFile, index);
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a3 A[Catch: all -> 0x010e, TryCatch #2 {, blocks: (B:16:0x0030, B:20:0x003a, B:21:0x0043, B:23:0x004d, B:26:0x0074, B:25:0x0057, B:29:0x009b, B:31:0x00a3, B:32:0x00a8, B:34:0x00aa, B:35:0x00d5, B:39:0x00e3, B:38:0x00dc, B:28:0x0078), top: B:52:0x0030, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00aa A[Catch: all -> 0x010e, TRY_LEAVE, TryCatch #2 {, blocks: (B:16:0x0030, B:20:0x003a, B:21:0x0043, B:23:0x004d, B:26:0x0074, B:25:0x0057, B:29:0x009b, B:31:0x00a3, B:32:0x00a8, B:34:0x00aa, B:35:0x00d5, B:39:0x00e3, B:38:0x00dc, B:28:0x0078), top: B:52:0x0030, inners: #0, #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readGrpIdsAndWait() {
        /*
            Method dump skipped, instruction units count: 274
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readGrpIdsAndWait():void");
    }

    private boolean readAdnFileAndWait(int recId) {
        logi("readAdnFileAndWait begin: recId is " + recId + ",mIsReset:" + this.mIsReset);
        int previousSize = this.mPhoneBookRecords.size();
        synchronized (this.mLock) {
            MtkAdnRecordCache mtkAdnRecordCache = this.mAdnCache;
            mtkAdnRecordCache.requestLoadAllAdnLike(28474, mtkAdnRecordCache.extensionEfForEf(28474), obtainMessage(2));
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in readAdnFileAndWait");
            }
        }
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList != null && arrayList.size() > recId) {
            this.mPbrRecords.get(recId).mMasterFileRecordNum = this.mPhoneBookRecords.size() - previousSize;
        }
        logi("readAdnFileAndWait end: recId is " + recId + ",mIsReset:" + this.mIsReset);
        if (!this.mIsReset) {
            return true;
        }
        return false;
    }

    private void createPbrFile(ArrayList<byte[]> records) {
        int sfi;
        if (records == null || records.size() == 0) {
            this.mPbrRecords = null;
            this.mIsPbrPresent = false;
            return;
        }
        this.mPbrRecords = new ArrayList<>();
        this.mSliceCount = 0;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i) != null && records.get(i).length > 0 && records.get(i)[0] != -1) {
                this.mPbrRecords.add(new PbrRecord(records.get(i)));
            }
        }
        for (PbrRecord record : this.mPbrRecords) {
            File file = (File) record.mFileIds.get(192);
            if (file != null && (sfi = file.getSfi()) != -1) {
                this.mSfiEfidTable.put(sfi, ((File) record.mFileIds.get(192)).getEfid());
            }
        }
    }

    private void readAasFileAndWaitOptmz() {
        SparseArray<File> files;
        File aasFile;
        logi("readAasFileAndWaitOptmz begin");
        ArrayList<String> arrayList = this.mAasForAnr;
        if (arrayList == null || arrayList.size() == 0) {
            int aasRecNum = 0;
            int[] iArr = this.mUpbCap;
            if (iArr[3] < 0) {
                ArrayList<PbrRecord> arrayList2 = this.mPbrRecords;
                if (arrayList2 == null || arrayList2.size() == 0 || (files = this.mPbrRecords.get(0).mFileIds) == null || (aasFile = files.get(199)) == null) {
                    return;
                }
                int[] size = readEFLinearRecordSize(aasFile.getEfid());
                if (size != null && size.length == 3) {
                    aasRecNum = size[2];
                }
            } else {
                aasRecNum = iArr[3];
            }
            if (aasRecNum > 5) {
                aasRecNum = 5;
            }
            synchronized (this.mLock) {
                this.mCi.readUPBAasList(1, aasRecNum, obtainMessage(EVENT_AAS_LOAD_DONE_OPTMZ));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readAasFileAndWaitOptmz");
                }
            }
        }
        logi("readAasFileAndWaitOptmz end");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x009d A[Catch: all -> 0x0108, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:29:0x009d, B:30:0x00a2, B:32:0x00a4, B:33:0x00cf, B:37:0x00dd, B:36:0x00d6, B:26:0x0072), top: B:48:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a4 A[Catch: all -> 0x0108, TRY_LEAVE, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:29:0x009d, B:30:0x00a2, B:32:0x00a4, B:33:0x00cf, B:37:0x00dd, B:36:0x00d6, B:26:0x0072), top: B:48:0x0035, inners: #0, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readEmailFileAndWaitOptmz() {
        /*
            Method dump skipped, instruction units count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readEmailFileAndWaitOptmz():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00a5 A[Catch: all -> 0x0110, TryCatch #0 {, blocks: (B:18:0x0038, B:19:0x0041, B:21:0x004b, B:24:0x0076, B:23:0x0055, B:27:0x009d, B:29:0x00a5, B:30:0x00aa, B:32:0x00ac, B:33:0x00d7, B:37:0x00e5, B:36:0x00de, B:26:0x007a), top: B:46:0x0038, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ac A[Catch: all -> 0x0110, TRY_LEAVE, TryCatch #0 {, blocks: (B:18:0x0038, B:19:0x0041, B:21:0x004b, B:24:0x0076, B:23:0x0055, B:27:0x009d, B:29:0x00a5, B:30:0x00aa, B:32:0x00ac, B:33:0x00d7, B:37:0x00e5, B:36:0x00de, B:26:0x007a), top: B:46:0x0038, inners: #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readAnrFileAndWaitOptmz() {
        /*
            Method dump skipped, instruction units count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readAnrFileAndWaitOptmz():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x009d A[Catch: all -> 0x0108, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:29:0x009d, B:30:0x00a2, B:32:0x00a4, B:33:0x00cf, B:37:0x00dd, B:36:0x00d6, B:26:0x0072), top: B:48:0x0035, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a4 A[Catch: all -> 0x0108, TRY_LEAVE, TryCatch #1 {, blocks: (B:18:0x0035, B:19:0x003e, B:21:0x0048, B:24:0x006e, B:23:0x0052, B:27:0x0095, B:29:0x009d, B:30:0x00a2, B:32:0x00a4, B:33:0x00cf, B:37:0x00dd, B:36:0x00d6, B:26:0x0072), top: B:48:0x0035, inners: #0, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readSneFileAndWaitOptmz() {
        /*
            Method dump skipped, instruction units count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readSneFileAndWaitOptmz():void");
    }

    private void updatePhoneAdnRecordWithEmailByIndexOptmz(int emailIndex, int adnIndex, String email) {
        log("updatePhoneAdnRecordWithEmailByIndex emailIndex = " + emailIndex + ",adnIndex = " + adnIndex);
        if (email != null && email != null) {
            try {
                if (!email.equals("")) {
                    MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                    rec.setEmails(new String[]{email});
                }
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "[JE]updatePhoneAdnRecordWithEmailByIndex " + e.getMessage());
            }
        }
    }

    private void updatePhoneAdnRecordWithAnrByIndexOptmz(int recId, int adnIndex, int anrIndex, PhbEntry anrData) {
        String anr;
        ArrayList<String> arrayList;
        log("updatePhoneAdnRecordWithAnrByIndexOptmz the " + adnIndex + " anr record:" + anrData);
        if (anrData != null && anrData.number != null && !anrData.number.equals("")) {
            if (anrData.ton == 145) {
                anr = MtkPhoneNumberUtils.prependPlusToNumber(anrData.number);
            } else {
                anr = anrData.number;
            }
            String anr2 = anr.replace('?', 'N').replace('p', ',').replace('w', ';');
            int anrAas = anrData.index;
            if (anr2 != null && !anr2.equals("")) {
                String aas = null;
                if (anrAas > 0 && anrAas != 255 && (arrayList = this.mAasForAnr) != null && anrAas <= arrayList.size()) {
                    aas = this.mAasForAnr.get(anrAas - 1);
                }
                log(" updatePhoneAdnRecordWithAnrByIndex " + adnIndex + " th anr is " + anr2 + " the anrIndex is " + anrIndex);
                try {
                    MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                    rec.setAnr(anr2, anrIndex);
                    if (aas != null && aas.length() > 0) {
                        rec.setAasIndex(anrAas);
                    }
                    this.mPhoneBookRecords.set(adnIndex, rec);
                } catch (IndexOutOfBoundsException e) {
                    Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithAnrByIndex: mPhoneBookRecords IndexOutOfBoundsException size: " + this.mPhoneBookRecords.size() + "index: " + adnIndex);
                }
            }
        }
    }

    private String[] buildAnrRecordOptmz(String number, int aas) {
        int ton = 129;
        if (number.indexOf(43) != -1) {
            if (number.indexOf(43) != number.lastIndexOf(43)) {
                Rlog.w(LOG_TAG, "There are multiple '+' in the number: " + number);
            }
            ton = 145;
            number = number.replace("+", "");
        }
        String[] res = {number.replace('N', '?').replace(',', 'p').replace(';', 'w'), Integer.toString(ton), Integer.toString(aas)};
        return res;
    }

    private void updatePhoneAdnRecordWithSneByIndexOptmz(int adnIndex, String sne) {
        if (sne == null) {
            return;
        }
        log("updatePhoneAdnRecordWithSneByIndex index " + adnIndex + " recData file is " + sne);
        if (sne != null && !sne.equals("")) {
            try {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                rec.setSne(sne);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithSneByIndex: mPhoneBookRecords IndexOutOfBoundsException size() is " + this.mPhoneBookRecords.size() + "index is " + adnIndex);
            }
        }
    }

    public void handleMessage(Message msg) {
        ArrayList<byte[]> aasFileRecords;
        String[] gasList;
        String[] aasList;
        ArrayList<byte[]> record;
        switch (msg.what) {
            case 1:
                logi("handleMessage: EVENT_PBR_LOAD_DONE:" + this.mPbrNeedNotify);
                if (this.mPbrNeedNotify != -1) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (ar.exception == null) {
                        createPbrFile((ArrayList) ar.result);
                    }
                    synchronized (this.mLock) {
                        this.mLock.notify();
                        break;
                    }
                    this.mPbrNeedNotify--;
                    return;
                }
                return;
            case 2:
                logi("Loading USIM ADN records done");
                AsyncResult ar2 = (AsyncResult) msg.obj;
                if (ar2.exception == null && this.mPhoneBookRecords != null) {
                    if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh) && this.mPhoneBookRecords.size() > 0 && ar2.result != null) {
                        ArrayList<MtkAdnRecord> adnList = changeAdnRecordNumber(this.mPhoneBookRecords.size(), (ArrayList) ar2.result);
                        this.mPhoneBookRecords.addAll(adnList);
                        CsimPhbUtil.initPhbStorage(adnList);
                    } else if (ar2.result != null) {
                        this.mPhoneBookRecords.addAll((ArrayList) ar2.result);
                        if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
                            CsimPhbUtil.initPhbStorage((ArrayList) ar2.result);
                        }
                        log("Loading USIM ADN records " + this.mPhoneBookRecords.size());
                    } else {
                        log("Loading USIM ADN records ar.result:" + ar2.result);
                    }
                } else {
                    Rlog.w(LOG_TAG, "Loading USIM ADN records fail.");
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 3:
                logi("Loading USIM IAP records done");
                AsyncResult ar3 = (AsyncResult) msg.obj;
                if (ar3.exception == null) {
                    this.mIapFileRecord = (ArrayList) ar3.result;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 4:
                logi("Loading USIM Email records done");
                AsyncResult ar4 = (AsyncResult) msg.obj;
                if (ar4.exception == null) {
                    this.mEmailFileRecord = (ArrayList) ar4.result;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 5:
                AsyncResult ar5 = (AsyncResult) msg.obj;
                int pbrIndexAAS = msg.arg1;
                logi("EVENT_AAS_LOAD_DONE done pbr " + pbrIndexAAS);
                if (ar5.exception == null && (aasFileRecords = (ArrayList) ar5.result) != null) {
                    int size = aasFileRecords.size();
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        byte[] aas = aasFileRecords.get(i);
                        if (aas == null) {
                            list.add(null);
                        } else {
                            String aasAlphaTag = IccUtils.adnStringFieldToString(aas, 0, aas.length);
                            list.add(aasAlphaTag);
                        }
                    }
                    this.mAasForAnr = list;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 6:
                logi("Load UPB GAS done");
                AsyncResult ar6 = (AsyncResult) msg.obj;
                if (ar6.exception == null && (gasList = (String[]) ar6.result) != null && gasList.length > 0) {
                    this.mGasForGrp = new ArrayList<>();
                    for (int i2 = 0; i2 < gasList.length; i2++) {
                        String gas = decodeGas(gasList[i2]);
                        UsimGroup uGasEntry = new UsimGroup(i2 + 1, gas);
                        this.mGasForGrp.add(uGasEntry);
                        log("Load UPB GAS done i is " + i2 + ", gas is " + gas);
                    }
                }
                synchronized (this.mGasLock) {
                    this.mGasLock.notify();
                    break;
                }
                return;
            case 7:
                logi("Updating USIM IAP records done");
                if (((AsyncResult) msg.obj).exception == null) {
                    log("Updating USIM IAP records successfully!");
                    return;
                }
                return;
            case 8:
                logi("Updating USIM Email records done");
                AsyncResult ar7 = (AsyncResult) msg.obj;
                if (ar7.exception == null) {
                    log("Updating USIM Email records successfully!");
                    this.mRefreshEmailInfo = true;
                } else {
                    Rlog.e(LOG_TAG, "EVENT_EMAIL_UPDATE_DONE exception", ar7.exception);
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 9:
                logi("Updating USIM ANR records done");
                AsyncResult ar8 = (AsyncResult) msg.obj;
                IccIoResult res = (IccIoResult) ar8.result;
                if (ar8.exception != null) {
                    Rlog.e(LOG_TAG, "EVENT_ANR_UPDATE_DONE exception", ar8.exception);
                } else if (res != null) {
                    IccException exception = res.getException();
                    if (exception == null) {
                        log("Updating USIM ANR records successfully!");
                        this.mRefreshAnrInfo = true;
                    }
                } else {
                    this.mRefreshAnrInfo = true;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 10:
                logi("EVENT_AAS_UPDATE_DONE done.");
                synchronized (this.mAasLock) {
                    this.mAasLock.notify();
                    break;
                }
                return;
            case 11:
                logi("update UPB SNE done");
                AsyncResult ar9 = (AsyncResult) msg.obj;
                if (ar9.exception != null) {
                    Rlog.e(LOG_TAG, "EVENT_SNE_UPDATE_DONE exception", ar9.exception);
                    CommandException e = ar9.exception;
                    if (e.getCommandError() == CommandException.Error.OEM_ERROR_2) {
                        this.mResult = -40;
                    } else if (e.getCommandError() == CommandException.Error.OEM_ERROR_3) {
                        this.mResult = -30;
                    } else {
                        this.mResult = -50;
                    }
                } else {
                    this.mResult = 0;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 12:
                logi("update UPB GRP done");
                if (((AsyncResult) msg.obj).exception == null) {
                    this.mResult = 0;
                } else {
                    this.mResult = -1;
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 13:
                logi("update UPB GAS done");
                AsyncResult ar10 = (AsyncResult) msg.obj;
                if (ar10.exception == null) {
                    this.mResult = 0;
                } else {
                    CommandException e2 = ar10.exception;
                    if (e2.getCommandError() == CommandException.Error.OEM_ERROR_2) {
                        this.mResult = -10;
                    } else if (e2.getCommandError() == CommandException.Error.OEM_ERROR_3) {
                        this.mResult = -20;
                    } else {
                        this.mResult = -1;
                    }
                }
                logi("update UPB GAS done mResult is " + this.mResult);
                synchronized (this.mGasLock) {
                    this.mGasLock.notify();
                    break;
                }
                return;
            case 14:
                AsyncResult ar11 = (AsyncResult) msg.obj;
                int[] userData = (int[]) ar11.userObj;
                IccIoResult re = (IccIoResult) ar11.result;
                boolean isNotify = this.mNeedNotify.get();
                if (re != null && this.mIapFileList != null) {
                    IccException iccException = re.getException();
                    if (iccException == null) {
                        log("Loading USIM Iap record done result is " + IccUtils.bytesToHexString(re.payload));
                        try {
                            ArrayList<byte[]> iapList = this.mIapFileList.get(userData[0]);
                            if (iapList.size() > 0) {
                                iapList.set(userData[1], re.payload);
                            } else {
                                Rlog.w(LOG_TAG, "Warning: IAP size is 0");
                            }
                        } catch (IndexOutOfBoundsException e3) {
                            Rlog.e(LOG_TAG, "Index out of bounds.");
                        }
                    }
                    break;
                }
                this.mReadingIapNum.decrementAndGet();
                log("haman, mReadingIapNum when load done after minus: " + this.mReadingIapNum.get() + ",mNeedNotify " + this.mNeedNotify.get() + ", Iap pbr:" + userData[0] + ", adn i:" + userData[1]);
                if (this.mReadingIapNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_IAP_RECORD_LOAD_DONE end mLock.notify:" + isNotify);
                    return;
                }
                return;
            case 15:
                AsyncResult ar12 = (AsyncResult) msg.obj;
                int[] userData2 = (int[]) ar12.userObj;
                IccIoResult em = (IccIoResult) ar12.result;
                log("Loading USIM email record done email index:" + userData2[0] + ", adn i:" + userData2[1]);
                if (em != null) {
                    IccException iccException2 = em.getException();
                    if (iccException2 == null) {
                        updatePhoneAdnRecordWithEmailByIndex(userData2[0], userData2[1], em.payload);
                    }
                }
                this.mReadingEmailNum.decrementAndGet();
                log("haman, mReadingEmailNum when load done after minus: " + this.mReadingEmailNum.get() + ", mNeedNotify:" + this.mNeedNotify.get());
                if (this.mReadingEmailNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_EMAIL_RECORD_LOAD_DONE end mLock.notify");
                    return;
                }
                return;
            case 16:
                AsyncResult ar13 = (AsyncResult) msg.obj;
                int[] userData3 = (int[]) ar13.userObj;
                IccIoResult result = (IccIoResult) ar13.result;
                if (result != null) {
                    IccException iccException3 = result.getException();
                    if (iccException3 == null) {
                        updatePhoneAdnRecordWithAnrByIndex(userData3[0], userData3[1], userData3[2], result.payload);
                    }
                }
                this.mReadingAnrNum.decrementAndGet();
                log("haman, mReadingAnrNum when load done after minus: " + this.mReadingAnrNum.get() + ", mNeedNotify:" + this.mNeedNotify.get());
                if (this.mReadingAnrNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_ANR_RECORD_LOAD_DONE end mLock.notify");
                    return;
                }
                return;
            case 17:
                AsyncResult ar14 = (AsyncResult) msg.obj;
                int[] userData4 = (int[]) ar14.userObj;
                boolean isNotify2 = this.mNeedNotify.get();
                if (ar14.result != null) {
                    int[] grpIds = (int[]) ar14.result;
                    if (grpIds.length > 0) {
                        updatePhoneAdnRecordWithGrpByIndex(userData4[0], userData4[1], grpIds);
                    }
                }
                this.mReadingGrpNum.decrementAndGet();
                log("haman, mReadingGrpNum when load done after minus: " + this.mReadingGrpNum.get() + ",mNeedNotify:" + isNotify2);
                if (this.mReadingGrpNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_GRP_RECORD_LOAD_DONE end mLock.notify:" + isNotify2);
                    return;
                }
                return;
            case 18:
                logi("Loading USIM SNE record done");
                AsyncResult ar15 = (AsyncResult) msg.obj;
                int[] userData5 = (int[]) ar15.userObj;
                IccIoResult r = (IccIoResult) ar15.result;
                if (r != null) {
                    IccException iccException4 = r.getException();
                    if (iccException4 == null) {
                        log("Loading USIM SNE record done result is " + IccUtils.bytesToHexString(r.payload));
                        updatePhoneAdnRecordWithSneByIndex(userData5[0], userData5[1], r.payload);
                    }
                }
                this.mReadingSneNum.decrementAndGet();
                log("haman, mReadingSneNum when load done after minus: " + this.mReadingSneNum.get() + ",mNeedNotify:" + this.mNeedNotify.get());
                if (this.mReadingSneNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_SNE_RECORD_LOAD_DONE end mLock.notify");
                    return;
                }
                return;
            case 19:
                logi("Query UPB capability done");
                AsyncResult ar16 = (AsyncResult) msg.obj;
                if (ar16.exception == null) {
                    this.mUpbCap = (int[]) ar16.result;
                }
                synchronized (this.mUPBCapabilityLock) {
                    this.mUPBCapabilityLock.notify();
                    break;
                }
                return;
            case 20:
                AsyncResult ar17 = (AsyncResult) msg.obj;
                if (ar17.exception == null) {
                    this.mEfData = (EFResponseData) ar17.result;
                } else {
                    Rlog.w(LOG_TAG, "Select EF file fail" + ar17.exception);
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 21:
                logi("EVENT_QUERY_PHB_ADN_INFO");
                AsyncResult ar18 = (AsyncResult) msg.obj;
                if (ar18.exception == null) {
                    int[] info = (int[]) ar18.result;
                    if (info != null && info.length == 4) {
                        this.mAdnRecordSize = new int[]{info[0], info[1], info[2], info[3]};
                        log("recordSize[0]=" + this.mAdnRecordSize[0] + ",recordSize[1]=" + this.mAdnRecordSize[1] + ",recordSize[2]=" + this.mAdnRecordSize[2] + ",recordSize[3]=" + this.mAdnRecordSize[3]);
                    } else {
                        this.mAdnRecordSize = new int[]{0, 0, 0, 0};
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE /* 22 */:
                AsyncResult ar19 = (AsyncResult) msg.obj;
                int[] userData6 = (int[]) ar19.userObj;
                String emailResult = (String) ar19.result;
                boolean isNotify3 = this.mNeedNotify.get();
                if (emailResult != null && ar19.exception == null) {
                    log("Loading USIM Email record done result is " + emailResult);
                    updatePhoneAdnRecordWithEmailByIndexOptmz(userData6[0], userData6[1], emailResult);
                }
                this.mReadingEmailNum.decrementAndGet();
                log("haman, mReadingEmailNum when load done after minus: " + this.mReadingEmailNum.get() + ", mNeedNotify:" + this.mNeedNotify.get() + ", email index:" + userData6[0] + ", adn i:" + userData6[1]);
                if (this.mReadingEmailNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_EMAIL_RECORD_LOAD_OPTMZ_DONE end mLock.notify:" + isNotify3);
                    return;
                }
                return;
            case EVENT_ANR_RECORD_LOAD_OPTMZ_DONE /* 23 */:
                AsyncResult ar20 = (AsyncResult) msg.obj;
                int[] userData7 = (int[]) ar20.userObj;
                PhbEntry[] anrResult = (PhbEntry[]) ar20.result;
                boolean isNotify4 = this.mNeedNotify.get();
                if (anrResult != null && ar20.exception == null) {
                    log("Loading USIM Anr record done result is " + anrResult[0]);
                    updatePhoneAdnRecordWithAnrByIndexOptmz(userData7[0], userData7[1], userData7[2], anrResult[0]);
                }
                this.mReadingAnrNum.decrementAndGet();
                log("haman, mReadingAnrNum when load done after minus: " + this.mReadingAnrNum.get() + ", mNeedNotify:" + this.mNeedNotify.get() + ", anr index:" + userData7[2] + ", adn i:" + userData7[1]);
                if (this.mReadingAnrNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_ANR_RECORD_LOAD_OPTMZ_DONE end mLock.notify:" + isNotify4);
                    return;
                }
                return;
            case EVENT_SNE_RECORD_LOAD_OPTMZ_DONE /* 24 */:
                AsyncResult ar21 = (AsyncResult) msg.obj;
                int[] userData8 = (int[]) ar21.userObj;
                String sneResult = (String) ar21.result;
                boolean isNotify5 = this.mNeedNotify.get();
                if (sneResult != null && ar21.exception == null) {
                    String sneResult2 = decodeGas(sneResult);
                    log("Loading USIM Sne record done result is " + sneResult2);
                    updatePhoneAdnRecordWithSneByIndexOptmz(userData8[1], sneResult2);
                }
                this.mReadingSneNum.decrementAndGet();
                log("haman, mReadingSneNum when load done after minus: " + this.mReadingSneNum.get() + ", mNeedNotify:" + this.mNeedNotify.get() + ", sne index:" + userData8[0] + ", adn i:" + userData8[1]);
                if (this.mReadingSneNum.get() == 0) {
                    if (this.mNeedNotify.get()) {
                        this.mNeedNotify.set(false);
                        synchronized (this.mLock) {
                            this.mLock.notify();
                            break;
                        }
                    }
                    logi("EVENT_SNE_RECORD_LOAD_OPTMZ_DONE end mLock.notify:" + isNotify5);
                    return;
                }
                return;
            case EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE /* 25 */:
                AsyncResult ar22 = (AsyncResult) msg.obj;
                if (ar22.exception == null) {
                    int[] iArr = (int[]) ar22.result;
                    this.mEmailInfo = iArr;
                    if (iArr == null) {
                        log("mEmailInfo Null!");
                    } else {
                        logi("mEmailInfo = " + this.mEmailInfo[0] + " " + this.mEmailInfo[1] + " " + this.mEmailInfo[2]);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE /* 26 */:
                AsyncResult ar23 = (AsyncResult) msg.obj;
                int[] tmpAnrInfo = (int[]) ar23.result;
                if (ar23.exception == null) {
                    if (tmpAnrInfo == null) {
                        log("tmpAnrInfo Null!");
                    } else {
                        logi("tmpAnrInfo = " + tmpAnrInfo[0] + " " + tmpAnrInfo[1] + " " + tmpAnrInfo[2]);
                        ArrayList<int[]> arrayList = this.mAnrInfo;
                        if (arrayList == null) {
                            this.mAnrInfo = new ArrayList<>();
                        } else if (arrayList.size() > 0) {
                            this.mAnrInfo.clear();
                        }
                        this.mAnrInfo.add(tmpAnrInfo);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_QUERY_SNE_AVAILABLE_OPTMZ_DONE /* 27 */:
                AsyncResult ar24 = (AsyncResult) msg.obj;
                if (ar24.exception == null) {
                    int[] iArr2 = (int[]) ar24.result;
                    this.mSneInfo = iArr2;
                    if (iArr2 == null) {
                        log("mSneInfo Null!");
                    } else {
                        logi("mSneInfo = " + this.mSneInfo[0] + " " + this.mSneInfo[1] + " " + this.mSneInfo[2]);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case EVENT_AAS_LOAD_DONE_OPTMZ /* 28 */:
                logi("Load UPB AAS done");
                AsyncResult ar25 = (AsyncResult) msg.obj;
                if (ar25.exception == null && (aasList = (String[]) ar25.result) != null && aasList.length > 0) {
                    this.mAasForAnr = new ArrayList<>();
                    for (int i3 = 0; i3 < aasList.length; i3++) {
                        String aas2 = decodeGas(aasList[i3]);
                        this.mAasForAnr.add(aas2);
                        log("Load UPB AAS done i is " + i3 + ", aas is " + aas2);
                    }
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            case 1001:
                AsyncResult ar26 = (AsyncResult) msg.obj;
                int pbrIndexExt1 = msg.arg1;
                logi("EVENT_EXT1_LOAD_DONE done pbr " + pbrIndexExt1);
                if (ar26.exception == null && (record = (ArrayList) ar26.result) != null) {
                    log("EVENT_EXT1_LOAD_DONE done size " + record.size());
                    if (this.mExt1FileList == null) {
                        this.mExt1FileList = new ArrayList<>();
                    }
                    this.mExt1FileList.add(record);
                }
                synchronized (this.mLock) {
                    this.mLock.notify();
                    break;
                }
                return;
            default:
                Rlog.e(LOG_TAG, "UnRecognized Message : " + msg.what);
                return;
        }
    }

    private class PbrRecord {
        private int mAnrIndex = 0;
        private SparseArray<File> mFileIds = new SparseArray<>();
        private int mMasterFileRecordNum;

        PbrRecord(byte[] record) {
            MtkUsimPhoneBookManager.this.logi("PBR rec: " + IccUtils.bytesToHexString(record));
            SimTlv recTlv = new SimTlv(record, 0, record.length);
            parseTag(recTlv);
        }

        void parseTag(SimTlv tlv) {
            do {
                int tag = tlv.getTag();
                switch (tag) {
                    case 168:
                    case 169:
                    case 170:
                        byte[] data = tlv.getData();
                        SimTlv tlvEfSfi = new SimTlv(data, 0, data.length);
                        parseEfAndSFI(tlvEfSfi, tag);
                        break;
                }
            } while (tlv.nextObject());
            MtkUsimPhoneBookManager.this.mSliceCount++;
        }

        void parseEfAndSFI(SimTlv tlv, int parentTag) {
            int sfi;
            int tag;
            int tagNumberWithinParentTag = 0;
            do {
                int tag2 = tlv.getTag();
                switch (tag2) {
                    case 192:
                    case 193:
                    case 194:
                    case 195:
                    case 196:
                    case 197:
                    case 198:
                    case 199:
                    case 200:
                    case ExternalSimConstants.EVENT_TYPE_SEND_RSIM_AUTH_IND /* 201 */:
                    case ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP /* 202 */:
                    case ExternalSimConstants.EVENT_TYPE_RSIM_AUTH_DONE /* 203 */:
                        byte[] data = tlv.getData();
                        if (data.length < 2 || data.length > 3) {
                            Rlog.w(MtkUsimPhoneBookManager.LOG_TAG, "Invalid TLV length: " + data.length);
                        } else {
                            if (data.length != 3) {
                                sfi = -1;
                            } else {
                                int sfi2 = data[2] & 255;
                                sfi = sfi2;
                            }
                            int efid = ((data[0] & 255) << 8) | (data[1] & 255);
                            if (tag2 != 196) {
                                tag = tag2;
                            } else {
                                int i = this.mAnrIndex;
                                this.mAnrIndex = i + 1;
                                tag = tag2 + (i * 256);
                            }
                            File object = MtkUsimPhoneBookManager.this.new File(parentTag, efid, sfi, tagNumberWithinParentTag);
                            object.mTag = tag;
                            object.mPbrRecord = MtkUsimPhoneBookManager.this.mSliceCount;
                            MtkUsimPhoneBookManager.this.logi("pbr " + object);
                            this.mFileIds.put(tag, object);
                        }
                        break;
                }
                tagNumberWithinParentTag++;
            } while (tlv.nextObject());
        }
    }

    private void queryUpbCapablityAndWait() {
        logi("queryUpbCapablityAndWait begin");
        synchronized (this.mUPBCapabilityLock) {
            for (int i = 0; i < 8; i++) {
                this.mUpbCap[i] = -1;
            }
            if (checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(19));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in queryUpbCapablityAndWait");
                }
            }
        }
        logi("queryUpbCapablityAndWait done:N_Anr :" + this.mUpbCap[0] + ",N_Email:" + this.mUpbCap[1] + ",N_Sne:" + this.mUpbCap[2] + ",N_Aas:" + this.mUpbCap[3] + ",L_Aas:" + this.mUpbCap[4] + ",N_Gas:" + this.mUpbCap[5] + ",L_Gas:" + this.mUpbCap[6] + ",N_Grp:" + this.mUpbCap[7]);
    }

    private void readGasListAndWait() {
        logi("readGasListAndWait begin");
        synchronized (this.mGasLock) {
            int i = this.mUpbCap[5];
            if (i <= 0) {
                log("readGasListAndWait no need to read. return");
                return;
            }
            this.mCi.readUPBGasList(1, i, obtainMessage(6));
            try {
                this.mGasLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in readGasListAndWait");
            }
            logi("readGasListAndWait end");
        }
    }

    private void updatePhoneAdnRecordWithAnrByIndex(int recId, int adnIndex, int anrIndex, byte[] anrRecData) {
        String anr;
        ArrayList<String> aasList;
        log("updatePhoneAdnRecordWithAnrByIndex the " + adnIndex + "th anr record is " + IccUtils.bytesToHexString(anrRecData));
        int anrRecLength = anrRecData[1];
        int anrAas = anrRecData[0];
        if (anrRecLength > 0 && anrRecLength <= 11 && (anr = MtkPhoneNumberUtils.calledPartyBCDToString(anrRecData, 2, anrRecData[1])) != null && !anr.equals("")) {
            String aas = null;
            if (anrAas > 0 && anrAas != 255 && this.mAasForAnr != null && (aasList = this.mAasForAnr) != null && anrAas <= aasList.size()) {
                aas = aasList.get(anrAas - 1);
            }
            logi(" updatePhoneAdnRecordWithAnrByIndex " + adnIndex + " th anr is " + anr + " the anrIndex is " + anrIndex);
            try {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                rec.setAnr(anr, anrIndex);
                if (aas != null && aas.length() > 0) {
                    rec.setAasIndex(anrAas);
                }
                this.mPhoneBookRecords.set(adnIndex, rec);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithAnrByIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + adnIndex);
            }
        }
    }

    public ArrayList<UsimGroup> getUsimGroups() {
        logi("getUsimGroups begin");
        synchronized (this.mGasLock) {
            if (!this.mGasForGrp.isEmpty()) {
                return this.mGasForGrp;
            }
            queryUpbCapablityAndWait();
            readGasListAndWait();
            logi("getUsimGroups end");
            return this.mGasForGrp;
        }
    }

    public String getUsimGroupById(int nGasId) {
        UsimGroup uGas;
        String grpName = null;
        logi("getUsimGroupById nGasId is " + nGasId);
        ArrayList<UsimGroup> arrayList = this.mGasForGrp;
        if (arrayList != null && nGasId <= arrayList.size() && (uGas = this.mGasForGrp.get(nGasId - 1)) != null) {
            grpName = uGas.getAlphaTag();
            log("getUsimGroupById index is " + uGas.getRecordIndex() + ", name is " + grpName);
        }
        logi("getUsimGroupById grpName is " + grpName);
        return grpName;
    }

    public synchronized boolean removeUsimGroupById(int nGasId) {
        boolean ret;
        ret = false;
        logi("removeUsimGroupById nGasId is " + nGasId);
        synchronized (this.mGasLock) {
            try {
                ArrayList<UsimGroup> arrayList = this.mGasForGrp;
                if (arrayList == null) {
                    Rlog.e(LOG_TAG, "removeUsimGroupById fail ");
                } else {
                    try {
                        if (nGasId > arrayList.size()) {
                            Rlog.e(LOG_TAG, "removeUsimGroupById fail ");
                        } else {
                            UsimGroup uGas = this.mGasForGrp.get(nGasId - 1);
                            if (uGas != null) {
                                log(" removeUsimGroupById index is " + uGas.getRecordIndex());
                            }
                            if (uGas != null && uGas.getAlphaTag() != null) {
                                this.mCi.deleteUPBEntry(4, 0, nGasId, obtainMessage(13));
                                try {
                                    this.mGasLock.wait();
                                } catch (InterruptedException e) {
                                    Rlog.e(LOG_TAG, "Interrupted Exception in removeUsimGroupById");
                                }
                                if (this.mResult == 0) {
                                    ret = true;
                                    uGas.setAlphaTag(null);
                                    this.mGasForGrp.set(nGasId - 1, uGas);
                                }
                            } else {
                                Rlog.w(LOG_TAG, "removeUsimGroupById fail: this gas doesn't exist ");
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                logi("removeUsimGroupById result is " + ret);
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return ret;
    }

    private String decodeGas(String srcGas) {
        log("[decodeGas] gas string is " + (srcGas == null ? "null" : srcGas));
        if (srcGas == null || TextUtils.isEmpty(srcGas) || srcGas.length() % 2 != 0) {
            return null;
        }
        try {
            byte[] ba = IccUtils.hexStringToBytes(srcGas);
            if (ba == null) {
                Rlog.w(LOG_TAG, "gas string is null");
                return null;
            }
            String retGas = new String(ba, 0, srcGas.length() / 2, "utf-16be");
            return retGas;
        } catch (UnsupportedEncodingException ex) {
            Rlog.e(LOG_TAG, "[decodeGas] implausible UnsupportedEncodingException", ex);
            return null;
        } catch (RuntimeException ex2) {
            Rlog.e(LOG_TAG, "[decodeGas] RuntimeException", ex2);
            return null;
        }
    }

    private String encodeToUcs2(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String hexInt = Integer.toHexString(input.charAt(i));
            for (int j = 0; j < 4 - hexInt.length(); j++) {
                output.append("0");
            }
            output.append(hexInt);
        }
        return output.toString();
    }

    public synchronized int insertUsimGroup(String grpName) {
        int index = -1;
        logi("insertUsimGroup grpName");
        synchronized (this.mGasLock) {
            try {
                ArrayList<UsimGroup> arrayList = this.mGasForGrp;
                if (arrayList == null || arrayList.size() == 0) {
                    Rlog.w(LOG_TAG, "insertUsimGroup fail ");
                } else {
                    UsimGroup gasEntry = null;
                    int i = 0;
                    while (true) {
                        if (i < this.mGasForGrp.size()) {
                            gasEntry = this.mGasForGrp.get(i);
                            if (gasEntry == null || gasEntry.getAlphaTag() != null) {
                                i++;
                            } else {
                                index = gasEntry.getRecordIndex();
                                log("insertUsimGroup index is " + index);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (index < 0) {
                        Rlog.w(LOG_TAG, "insertUsimGroup fail: gas file is full.");
                        return -20;
                    }
                    String temp = encodeToUcs2(grpName);
                    this.mCi.editUPBEntry(4, 0, index, temp, null, obtainMessage(13));
                    try {
                        this.mGasLock.wait();
                    } catch (InterruptedException e) {
                        Rlog.e(LOG_TAG, "Interrupted Exception in insertUsimGroup");
                    }
                    if (this.mResult < 0) {
                        Rlog.e(LOG_TAG, "result is negative. insertUsimGroup");
                        return this.mResult;
                    }
                    gasEntry.setAlphaTag(grpName);
                    this.mGasForGrp.set(i, gasEntry);
                }
                return index;
            } finally {
                th = th;
                while (true) {
                    try {
                    } catch (Throwable th) {
                        th = th;
                    }
                }
            }
        }
    }

    public synchronized int updateUsimGroup(int nGasId, String grpName) {
        int ret;
        logi("updateUsimGroup nGasId is " + nGasId);
        synchronized (this.mGasLock) {
            try {
                this.mResult = -1;
                ArrayList<UsimGroup> arrayList = this.mGasForGrp;
                if (arrayList == null || nGasId > arrayList.size()) {
                    Rlog.w(LOG_TAG, "updateUsimGroup fail ");
                } else {
                    if (grpName != null) {
                        String temp = encodeToUcs2(grpName);
                        this.mCi.editUPBEntry(4, 0, nGasId, temp, null, obtainMessage(13));
                        try {
                            this.mGasLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in updateUsimGroup");
                        }
                    }
                }
                int i = this.mResult;
                if (i == 0) {
                    ret = nGasId;
                    UsimGroup uGasEntry = this.mGasForGrp.get(nGasId - 1);
                    if (uGasEntry != null) {
                        log("updateUsimGroup index is " + uGasEntry.getRecordIndex());
                        uGasEntry.setAlphaTag(grpName);
                    } else {
                        Rlog.w(LOG_TAG, "updateUsimGroup the entry doesn't exist ");
                    }
                } else {
                    ret = i;
                }
            } finally {
                th = th;
                while (true) {
                    try {
                    } catch (Throwable th) {
                        th = th;
                    }
                }
            }
        }
        return ret;
    }

    public boolean addContactToGroup(int adnIndex, int grpIndex) {
        boolean ret = false;
        logi("addContactToGroup begin adnIndex is " + adnIndex + " to grp " + grpIndex);
        ArrayList<MtkAdnRecord> arrayList = this.mPhoneBookRecords;
        if (arrayList == null || adnIndex <= 0 || adnIndex > arrayList.size()) {
            Rlog.e(LOG_TAG, "addContactToGroup no records or invalid index.");
            return false;
        }
        synchronized (this.mLock) {
            try {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex - 1);
                if (rec != null) {
                    log(" addContactToGroup the adn index is " + rec.getRecId() + " old grpList is " + rec.getGrpIds());
                    String grpList = rec.getGrpIds();
                    boolean bExist = false;
                    int nOrder = -1;
                    int[] iArr = this.mUpbCap;
                    int grpCount = iArr[7];
                    int i = iArr[5];
                    if (grpCount > i) {
                        grpCount = i;
                    }
                    int grpMaxCount = grpCount;
                    int[] grpIdArray = new int[grpCount];
                    for (int i2 = 0; i2 < grpCount; i2++) {
                        grpIdArray[i2] = 0;
                    }
                    if (grpList != null) {
                        String[] grpIds = rec.getGrpIds().split(",");
                        int i3 = 0;
                        while (true) {
                            if (i3 >= grpMaxCount) {
                                break;
                            }
                            grpIdArray[i3] = Integer.parseInt(grpIds[i3]);
                            if (grpIndex == grpIdArray[i3]) {
                                bExist = true;
                                log(" addContactToGroup the adn is already in the group. i is " + i3);
                                break;
                            }
                            if (nOrder < 0 && (grpIdArray[i3] == 0 || grpIdArray[i3] == 255)) {
                                nOrder = i3;
                                log(" addContactToGroup found an unsed position in the group list. i is " + i3);
                            }
                            i3++;
                        }
                    } else {
                        nOrder = 0;
                    }
                    if (!bExist && nOrder >= 0) {
                        grpIdArray[nOrder] = grpIndex;
                        this.mCi.writeUPBGrpEntry(adnIndex, grpIdArray, obtainMessage(12));
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in addContactToGroup");
                        }
                        if (this.mResult == 0) {
                            ret = true;
                            updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                            logi(" addContactToGroup the adn index is " + rec.getRecId());
                            this.mResult = -1;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e2) {
                Rlog.e(LOG_TAG, "addContactToGroup: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                return false;
            }
        }
        logi("addContactToGroup end adnIndex is " + adnIndex + " to grp " + grpIndex);
        return ret;
    }

    public synchronized boolean removeContactFromGroup(int adnIndex, int grpIndex) {
        MtkAdnRecord rec;
        boolean ret = false;
        logi("removeContactFromGroup begin adnIndex is " + adnIndex + " to grp " + grpIndex);
        ArrayList<MtkAdnRecord> arrayList = this.mPhoneBookRecords;
        if (arrayList != null && adnIndex > 0 && adnIndex <= arrayList.size()) {
            synchronized (this.mLock) {
                try {
                    try {
                        try {
                            rec = this.mPhoneBookRecords.get(adnIndex - 1);
                        } catch (IndexOutOfBoundsException e) {
                            Rlog.e(LOG_TAG, "removeContactFromGroup: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                            return false;
                        }
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                if (rec != null) {
                    String grpList = rec.getGrpIds();
                    if (grpList == null) {
                        Rlog.e(LOG_TAG, " the adn is not in any group. ");
                        return false;
                    }
                    String[] grpIds = grpList.split(",");
                    boolean bExist = false;
                    int nOrder = -1;
                    int[] grpIdArray = new int[grpIds.length];
                    for (int i = 0; i < grpIds.length; i++) {
                        grpIdArray[i] = Integer.parseInt(grpIds[i]);
                        if (grpIndex == grpIdArray[i]) {
                            bExist = true;
                            nOrder = i;
                            log(" removeContactFromGroup the adn is in the group. i is " + i);
                        }
                    }
                    if (bExist && nOrder >= 0) {
                        grpIdArray[nOrder] = 0;
                        this.mCi.writeUPBGrpEntry(adnIndex, grpIdArray, obtainMessage(12));
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e2) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in removeContactFromGroup");
                        }
                        if (this.mResult == 0) {
                            ret = true;
                            updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                            this.mResult = -1;
                        }
                    } else {
                        Rlog.e(LOG_TAG, " removeContactFromGroup the adn is not in the group. ");
                    }
                    throw th;
                }
                logi("removeContactFromGroup end adnIndex is " + adnIndex + " to grp " + grpIndex);
                return ret;
            }
        }
        Rlog.e(LOG_TAG, "removeContactFromGroup no records or invalid index.");
        return false;
    }

    public boolean updateContactToGroups(int adnIndex, int[] grpIdList) {
        boolean ret = false;
        ArrayList<MtkAdnRecord> arrayList = this.mPhoneBookRecords;
        if (arrayList == null || adnIndex <= 0 || adnIndex > arrayList.size() || grpIdList == null) {
            Rlog.e(LOG_TAG, "updateContactToGroups no records or invalid index.");
            return false;
        }
        logi("updateContactToGroups begin grpIdList is " + adnIndex + " to grp list count " + grpIdList.length);
        synchronized (this.mLock) {
            MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex - 1);
            if (rec != null) {
                log(" updateContactToGroups the adn index is " + rec.getRecId() + " old grpList is " + rec.getGrpIds());
                int grpCount = this.mUpbCap[7];
                if (grpIdList.length > grpCount) {
                    Rlog.e(LOG_TAG, "updateContactToGroups length of grpIdList > grpCount.");
                    return false;
                }
                int[] grpIdArray = new int[grpCount];
                int i = 0;
                while (i < grpCount) {
                    grpIdArray[i] = i < grpIdList.length ? grpIdList[i] : 0;
                    log("updateContactToGroups i:" + i + ",grpIdArray[" + i + "]:" + grpIdArray[i]);
                    i++;
                }
                this.mCi.writeUPBGrpEntry(adnIndex, grpIdArray, obtainMessage(12));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in updateContactToGroups");
                }
                if (this.mResult == 0) {
                    ret = true;
                    updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                    logi(" updateContactToGroups the adn index is " + rec.getRecId());
                    this.mResult = -1;
                }
            }
            logi("updateContactToGroups end grpIdList is " + adnIndex + " to grp list count " + grpIdList.length);
            return ret;
        }
    }

    public boolean moveContactFromGroupsToGroups(int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) {
        boolean ret = false;
        ArrayList<MtkAdnRecord> arrayList = this.mPhoneBookRecords;
        if (arrayList == null || adnIndex <= 0 || adnIndex > arrayList.size()) {
            Rlog.e(LOG_TAG, "moveContactFromGroupsToGroups no records or invalid index.");
            return false;
        }
        synchronized (this.mLock) {
            MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex - 1);
            if (rec != null) {
                int[] iArr = this.mUpbCap;
                int grpMaxCount = iArr[7];
                int i = iArr[5];
                if (grpMaxCount > i) {
                    grpMaxCount = i;
                }
                String grpIds = rec.getGrpIds();
                logi(" moveContactFromGroupsToGroups the adn index is " + rec.getRecId() + " original grpIds is " + grpIds + ", fromGrpIdList: " + (fromGrpIdList == null ? "null" : fromGrpIdList) + ", toGrpIdList: " + (toGrpIdList == null ? "null" : toGrpIdList));
                int[] grpIdIntArray = new int[grpMaxCount];
                for (int i2 = 0; i2 < grpMaxCount; i2++) {
                    grpIdIntArray[i2] = 0;
                }
                if (grpIds != null) {
                    String[] grpIdStrArray = grpIds.split(",");
                    for (int i3 = 0; i3 < grpMaxCount; i3++) {
                        grpIdIntArray[i3] = Integer.parseInt(grpIdStrArray[i3]);
                    }
                }
                if (fromGrpIdList != null) {
                    for (int i4 : fromGrpIdList) {
                        for (int j = 0; j < grpMaxCount; j++) {
                            if (grpIdIntArray[j] == i4) {
                                grpIdIntArray[j] = 0;
                            }
                        }
                    }
                }
                if (toGrpIdList != null) {
                    for (int i5 = 0; i5 < toGrpIdList.length; i5++) {
                        boolean bEmpty = false;
                        boolean bExist = false;
                        int k = 0;
                        while (true) {
                            if (k >= grpMaxCount) {
                                break;
                            }
                            if (grpIdIntArray[k] != toGrpIdList[i5]) {
                                k++;
                            } else {
                                bExist = true;
                                break;
                            }
                        }
                        if (bExist) {
                            Rlog.w(LOG_TAG, "moveContactFromGroupsToGroups the adn isalready in the group.");
                        } else {
                            for (int j2 = 0; j2 < grpMaxCount; j2++) {
                                if (grpIdIntArray[j2] != 0 && grpIdIntArray[j2] != 255) {
                                }
                                bEmpty = true;
                                grpIdIntArray[j2] = toGrpIdList[i5];
                                break;
                            }
                            if (!bEmpty) {
                                Rlog.e(LOG_TAG, "moveContactFromGroupsToGroups no empty to add.");
                                return false;
                            }
                        }
                    }
                }
                this.mCi.writeUPBGrpEntry(adnIndex, grpIdIntArray, obtainMessage(12));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in moveContactFromGroupsToGroups");
                }
                if (this.mResult == 0) {
                    ret = true;
                    updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdIntArray);
                    logi("moveContactFromGroupsToGroups the adn index is " + rec.getRecId());
                    this.mResult = -1;
                }
            }
            return ret;
        }
    }

    public boolean removeContactGroup(int adnIndex) {
        boolean ret = false;
        logi("removeContactsGroup adnIndex is " + adnIndex);
        ArrayList<MtkAdnRecord> arrayList = this.mPhoneBookRecords;
        if (arrayList == null || arrayList.isEmpty()) {
            return false;
        }
        synchronized (this.mLock) {
            try {
                try {
                    MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex - 1);
                    if (rec == null) {
                        return false;
                    }
                    log("removeContactsGroup rec is " + rec);
                    String grpList = rec.getGrpIds();
                    if (grpList == null) {
                        return false;
                    }
                    String[] grpIds = grpList.split(",");
                    boolean hasGroup = false;
                    int i = 0;
                    while (true) {
                        if (i >= grpIds.length) {
                            break;
                        }
                        int value = Integer.parseInt(grpIds[i]);
                        if (value <= 0 || value >= 255) {
                            i++;
                        } else {
                            hasGroup = true;
                            break;
                        }
                    }
                    if (hasGroup) {
                        this.mCi.writeUPBGrpEntry(adnIndex, new int[0], obtainMessage(12));
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e) {
                            Rlog.e(LOG_TAG, "Interrupted Exception in removeContactGroup");
                        }
                        if (this.mResult == 0) {
                            ret = true;
                            int[] grpIdArray = new int[grpIds.length];
                            for (int i2 = 0; i2 < grpIds.length; i2++) {
                                grpIdArray[i2] = 0;
                            }
                            updatePhoneAdnRecordWithGrpByIndex(adnIndex - 1, adnIndex, grpIdArray);
                            logi(" removeContactGroup the adn index is " + rec.getRecId());
                            this.mResult = -1;
                        }
                    }
                    return ret;
                } catch (IndexOutOfBoundsException e2) {
                    Rlog.e(LOG_TAG, "removeContactGroup: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                    return false;
                }
            } finally {
            }
        }
    }

    public int hasExistGroup(String grpName) {
        int grpId = -1;
        logi("hasExistGroup grpName is " + grpName);
        if (grpName == null) {
            return -1;
        }
        ArrayList<UsimGroup> arrayList = this.mGasForGrp;
        if (arrayList != null && arrayList.size() > 0) {
            int i = 0;
            while (true) {
                if (i < this.mGasForGrp.size()) {
                    UsimGroup uGas = this.mGasForGrp.get(i);
                    if (uGas == null || !grpName.equals(uGas.getAlphaTag())) {
                        i++;
                    } else {
                        log("getUsimGroupById index is " + uGas.getRecordIndex() + ", name is " + grpName);
                        grpId = uGas.getRecordIndex();
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        logi("hasExistGroup grpId is " + grpId);
        return grpId;
    }

    public int getUsimGrpMaxNameLen() {
        int ret;
        logi("getUsimGrpMaxNameLen begin");
        synchronized (this.mUPBCapabilityLock) {
            if (checkIsPhbReady()) {
                if (this.mUpbCap[6] < 0) {
                    queryUpbCapablityAndWait();
                }
                ret = this.mUpbCap[6];
            } else {
                ret = -1;
            }
            logi("getUsimGrpMaxNameLen done: L_Gas is " + ret);
        }
        return ret;
    }

    public int getUsimGrpMaxCount() {
        int ret;
        logi("getUsimGrpMaxCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (checkIsPhbReady()) {
                if (this.mUpbCap[5] < 0) {
                    queryUpbCapablityAndWait();
                }
                ret = this.mUpbCap[5];
            } else {
                ret = -1;
            }
            logi("getUsimGrpMaxCount done: N_Gas is " + ret);
        }
        return ret;
    }

    private void log(String msg) {
        if (DBG) {
            Rlog.d(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(String msg) {
        Rlog.i(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    public boolean isAnrCapacityFree(String anr, int adnIndex, int anrIndex, MtkAdnRecord oldAdn) {
        String oldAnr = null;
        if (oldAdn != null) {
            oldAnr = oldAdn.getAdditionalNumber(anrIndex);
        }
        if (anr == null || anr.equals("") || anrIndex < 0 || getUsimEfType(196) == 168 || (oldAnr != null && !oldAnr.equals(""))) {
            return true;
        }
        if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
            int i = this.mAdnFileSize;
            int pbrRecNum = (adnIndex - 1) / i;
            int anrRecNum = (adnIndex - 1) % i;
            try {
                log("isAnrCapacityFree anr: " + anr);
                SparseArray<int[]> sparseArray = this.mRecordSize;
                if (sparseArray != null && sparseArray.size() != 0) {
                    File anrFile = (File) this.mPbrRecords.get(pbrRecNum).mFileIds.get((anrIndex * 256) + 196);
                    if (anrFile == null) {
                        return false;
                    }
                    int anrFileId = anrFile.getEfid();
                    int[] sizeInfo = this.mRecordSize.get(anrFileId);
                    int size = sizeInfo[2];
                    log("isAnrCapacityFree size: " + size);
                    if (size >= anrRecNum + 1) {
                        return true;
                    }
                    log("isAnrCapacityFree: anrRecNum out of size: " + anrRecNum);
                    return false;
                }
                log("isAnrCapacityFree: mAnrFileSize is empty");
                return false;
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "isAnrCapacityFree Index out of bounds.");
                return false;
            } catch (NullPointerException e2) {
                Rlog.e(LOG_TAG, "isAnrCapacityFree exception:" + e2.toString());
                return false;
            }
        }
        synchronized (this.mLock) {
            ArrayList<int[]> arrayList = this.mAnrInfo;
            if (arrayList == null || anrIndex >= arrayList.size()) {
                this.mCi.queryUPBAvailable(0, anrIndex + 1, obtainMessage(EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e3) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in isAnrCapacityFree");
                }
            }
        }
        ArrayList<int[]> arrayList2 = this.mAnrInfo;
        return (arrayList2 == null || arrayList2.get(anrIndex) == null || this.mAnrInfo.get(anrIndex)[1] <= 0) ? false : true;
    }

    public void updateAnrByAdnIndex(String anr, int adnIndex, int anrIndex, MtkAdnRecord oldAdn) throws Throwable {
        String oldAnr;
        Object obj;
        File anrFile;
        Message msg;
        int i = this.mAdnFileSize;
        int pbrRecNum = (adnIndex - 1) / i;
        int anrRecNum = (adnIndex - 1) % i;
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        SparseArray<File> fileIds = this.mPbrRecords.get(pbrRecNum).mFileIds;
        if (fileIds == null) {
            log("updateAnrByAdnIndex: No anr tag in pbr record 0");
            return;
        }
        ArrayList<MtkAdnRecord> arrayList2 = this.mPhoneBookRecords;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            Rlog.w(LOG_TAG, "updateAnrByAdnIndex: mPhoneBookRecords is empty");
            return;
        }
        File anrFile2 = fileIds.get((anrIndex * 256) + 196);
        if (anrFile2 == null) {
            log("updateAnrByAdnIndex no efFile anrIndex: " + anrIndex);
            return;
        }
        logi("updateAnrByAdnIndex begin effile " + anrFile2);
        if (oldAdn != null) {
            String oldAnr2 = oldAdn.getAdditionalNumber(anrIndex);
            oldAdn.getAasIndex();
            oldAnr = oldAnr2;
        } else {
            oldAnr = null;
        }
        if (CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
            try {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex - 1);
                int aas = rec.getAasIndex();
                Message msg2 = obtainMessage(9);
                Object obj2 = this.mLock;
                synchronized (obj2) {
                    try {
                    } catch (Throwable th) {
                        while (true) {
                            th = th;
                        }
                    }
                    if (anr != null) {
                        try {
                            if (anr.length() == 0) {
                                obj = obj2;
                                msg = msg2;
                                anrFile = anrFile2;
                            } else {
                                String[] param = buildAnrRecordOptmz(anr, aas);
                                obj = obj2;
                                anrFile = anrFile2;
                                try {
                                    this.mCi.editUPBEntry(0, anrIndex + 1, adnIndex, param[0], param[1], param[2], msg2);
                                    try {
                                        this.mLock.wait();
                                    } catch (InterruptedException e) {
                                        Rlog.e(LOG_TAG, "Interrupted Exception in updateAnrByAdnIndexOptmz");
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            obj = obj2;
                        }
                        throw th;
                    }
                    obj = obj2;
                    msg = msg2;
                    anrFile = anrFile2;
                    if (oldAnr != null) {
                        try {
                            if (oldAnr.length() != 0) {
                                try {
                                    this.mCi.deleteUPBEntry(0, anrIndex + 1, adnIndex, msg);
                                    this.mLock.wait();
                                } catch (Throwable th5) {
                                    th = th5;
                                }
                            }
                        } catch (Throwable th6) {
                            th = th6;
                        }
                        throw th;
                    }
                    return;
                }
            } catch (IndexOutOfBoundsException e2) {
                Rlog.e(LOG_TAG, "updateAnrByAdnIndexOptmz: mPhoneBookRecords IndexOutOfBoundsException size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                return;
            }
        }
        int efid = anrFile2.getEfid();
        log("updateAnrByAdnIndex recId: " + pbrRecNum + " EF_ANR id is " + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
        if (anrFile2.getParentTag() == 169) {
            updateType2Anr(anr, adnIndex, anrFile2);
            return;
        }
        try {
            MtkAdnRecord rec2 = this.mPhoneBookRecords.get(adnIndex - 1);
            int aas2 = rec2.getAasIndex();
            byte[] data = buildAnrRecord(anr, this.mAnrRecordSize, aas2);
            if (data != null) {
                this.mFh.updateEFLinearFixed(efid, anrRecNum + 1, data, (String) null, obtainMessage(9));
            }
            anrFile = anrFile2;
        } catch (IndexOutOfBoundsException e3) {
            Rlog.e(LOG_TAG, "updateAnrByAdnIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
            return;
        }
        logi("updateAnrByAdnIndex end effile " + anrFile);
    }

    private int getEmailRecNum(String[] emails, int pbrRecNum, int nIapRecNum, byte[] iapRec, int tagNum) {
        boolean hasEmail = false;
        int recNum = iapRec[tagNum] & 255;
        log("getEmailRecNum recNum:" + recNum);
        if (emails == null) {
            if (recNum < 255 && recNum > 0) {
                this.mEmailRecTable[recNum - 1] = 0;
            }
            return -1;
        }
        int i = 0;
        while (true) {
            if (i < emails.length) {
                if (emails[i] == null || emails[i].equals("")) {
                    i++;
                } else {
                    hasEmail = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!hasEmail) {
            if (recNum < 255 && recNum > 0) {
                this.mEmailRecTable[recNum - 1] = 0;
            }
            return -1;
        }
        int i2 = this.mEmailFileSize;
        if (recNum > i2 || recNum >= 255 || recNum <= 0) {
            int nOffset = i2 * pbrRecNum;
            int i3 = nOffset;
            while (true) {
                if (i3 >= this.mEmailFileSize + nOffset) {
                    break;
                }
                log("updateEmailsByAdnIndex: mEmailRecTable[" + i3 + "] is " + this.mEmailRecTable[i3]);
                int[] iArr = this.mEmailRecTable;
                if (iArr[i3] != 0) {
                    i3++;
                } else {
                    recNum = (i3 + 1) - nOffset;
                    iArr[i3] = nIapRecNum;
                    break;
                }
            }
        }
        int i4 = this.mEmailFileSize;
        if (recNum > i4) {
            recNum = 255;
        }
        if (recNum == -1) {
            return -2;
        }
        return recNum;
    }

    public boolean checkEmailCapacityFree(int adnIndex, String[] emails, MtkAdnRecord oldAdn) {
        int i;
        if (emails == null || getUsimEfType(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP) == 168 || (oldAdn != null && oldAdn.getEmails() != null)) {
            return true;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= emails.length) {
                i = 0;
                break;
            }
            if (emails[i2] == null || emails[i2].equals("")) {
                i2++;
            } else {
                i = 1;
                break;
            }
        }
        if (i == 0) {
            return true;
        }
        if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
            int pbrRecNum = (adnIndex - 1) / this.mAdnFileSize;
            int nOffset = this.mEmailFileSize * pbrRecNum;
            for (int i3 = nOffset; i3 < this.mEmailFileSize + nOffset; i3++) {
                if (this.mEmailRecTable[i3] == 0) {
                    return true;
                }
            }
            return false;
        }
        synchronized (this.mLock) {
            int[] iArr = this.mEmailInfo;
            if (iArr == null || iArr.length != 3) {
                this.mCi.queryUPBAvailable(1, 1, obtainMessage(EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in CheckEmailCapacityFree");
                }
                if (this.mUpbDone == -1) {
                    return true;
                }
                this.mEmailFileSize = countEmailFileSize();
            }
            int used = countEmailCapacity(adnIndex);
            logi("CheckEmailCapacityFree: mEmailFileSize: " + this.mEmailFileSize + " used: " + used + " adnIndex: " + adnIndex);
            return used < this.mEmailFileSize;
        }
    }

    private int countEmailFileSize() {
        int numAdnRecs = this.mPhoneBookRecords.size();
        int i = this.mAdnFileSize;
        int totalPbrRecNum = numAdnRecs / i;
        if (numAdnRecs % i > 0) {
            totalPbrRecNum++;
        }
        int[] iArr = this.mEmailInfo;
        if (iArr != null && iArr.length == 3 && totalPbrRecNum > 0) {
            return iArr[0] / totalPbrRecNum;
        }
        return 100;
    }

    private int countEmailCapacity(int adnIndex) {
        String[] emails;
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            return -1;
        }
        int i = this.mAdnFileSize;
        int pbrRecNum = (adnIndex - 1) / i;
        int nOffset = i * pbrRecNum;
        int numAdnRecs = this.mPhoneBookRecords.size();
        int nMax = this.mAdnFileSize + nOffset;
        int nMax2 = numAdnRecs < nMax ? numAdnRecs : nMax;
        SparseArray<File> files = this.mPbrRecords.get(pbrRecNum).mFileIds;
        File emailFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
        int used = 0;
        if (emailFile == null) {
            return -1;
        }
        MtkAdnRecord rec = null;
        for (int i2 = nOffset; i2 < nMax2; i2++) {
            try {
                rec = this.mPhoneBookRecords.get(i2);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "countEmailCapacity: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + i2);
            }
            if (rec != null && (emails = rec.getEmails()) != null && emails.length > 0 && emails[0].length() > 0) {
                used++;
            }
        }
        log("countEmailCapacity: email used: " + used);
        return used;
    }

    public boolean checkSneCapacityFree(int adnIndex, String sne, MtkAdnRecord oldAdn) {
        String oldSne = null;
        if (oldAdn != null) {
            oldSne = oldAdn.getSne();
        }
        if (sne == null || sne.equals("") || getUsimEfType(195) == 168 || ((oldSne != null && !oldSne.equals("")) || !CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh))) {
            return true;
        }
        synchronized (this.mLock) {
            if (this.mSneInfo == null) {
                this.mCi.queryUPBAvailable(2, 1, obtainMessage(EVENT_QUERY_SNE_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in checkSneCapacityFree");
                }
            }
        }
        int[] iArr = this.mSneInfo;
        if (iArr != null && iArr[1] > 0) {
            return true;
        }
        return false;
    }

    private int getUsimEfType(int efTag) {
        SparseArray<File> files;
        File efFile;
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0 || (files = this.mPbrRecords.get(0).mFileIds) == null || (efFile = files.get(efTag)) == null) {
            return 0;
        }
        Rlog.d(LOG_TAG, "[getUsimEfType] efTag: " + efTag + ", type: " + efFile.getParentTag());
        return efFile.getParentTag();
    }

    public boolean checkEmailLength(String[] emails) {
        ArrayList<PbrRecord> arrayList;
        SparseArray<File> files;
        File emailFile;
        if (emails == null || emails[0] == null || (arrayList = this.mPbrRecords) == null || arrayList.size() == 0 || (files = this.mPbrRecords.get(0).mFileIds) == null || (emailFile = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP)) == null) {
            return true;
        }
        boolean emailType2 = emailFile.getParentTag() == 169;
        int maxDataLength = this.mEmailRecordSize;
        if (maxDataLength != -1 && emailType2) {
            maxDataLength -= 2;
        }
        byte[] eMailData = GsmAlphabet.stringToGsm8BitPacked(emails[0]);
        logi("checkEmailLength eMailData.length=" + eMailData.length + ", maxDataLength=" + maxDataLength);
        return maxDataLength == -1 || eMailData.length <= maxDataLength;
    }

    /* JADX WARN: Not initialized variable reg: 18, insn: 0x018d: MOVE (r5 I:??[OBJECT, ARRAY]) = (r18 I:??[OBJECT, ARRAY] A[D('msg' android.os.Message)]), block:B:89:0x018d */
    /* JADX WARN: Not initialized variable reg: 19, insn: 0x018f: MOVE (r3 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r19 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('emailIndex' int)]), block:B:89:0x018d */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0192 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x018a A[Catch: all -> 0x018c, TRY_LEAVE, TryCatch #4 {all -> 0x018c, blocks: (B:86:0x018a, B:78:0x0161), top: B:115:0x010d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int updateEmailsByAdnIndex(java.lang.String[] r26, int r27, com.mediatek.internal.telephony.phb.MtkAdnRecord r28) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 439
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.updateEmailsByAdnIndex(java.lang.String[], int, com.mediatek.internal.telephony.phb.MtkAdnRecord):int");
    }

    private int updateType2Email(String[] emails, int adnIndex, File emailFile) {
        int i = this.mAdnFileSize;
        int pbrRecNum = (adnIndex - 1) / i;
        int adnRecNum = (adnIndex - 1) % i;
        int emailType2Index = emailFile.getIndex();
        try {
            ArrayList<byte[]> iapFile = this.mIapFileList.get(pbrRecNum);
            if (iapFile.size() > 0) {
                byte[] iapRec = iapFile.get(adnRecNum);
                int recNum = getEmailRecNum(emails, pbrRecNum, adnRecNum + 1, iapRec, emailType2Index);
                if (-2 == recNum) {
                    log("updateType2Email: Email recNum is " + recNum);
                    return -30;
                }
                log("updateType2Email: found Email recNum is " + recNum);
                iapRec[emailType2Index] = (byte) recNum;
                SparseArray<File> files = this.mPbrRecords.get(pbrRecNum).mFileIds;
                if (files.get(193) != null) {
                    int efid = files.get(193).getEfid();
                    this.mFh.updateEFLinearFixed(efid, adnRecNum + 1, iapRec, (String) null, obtainMessage(7));
                    if (recNum != 255 && recNum != -1) {
                        String eMailAd = null;
                        if (emails != null) {
                            try {
                                eMailAd = emails[0];
                            } catch (IndexOutOfBoundsException e) {
                                Rlog.e(LOG_TAG, "Error: updateType2Email no email address, continuing");
                            }
                            int i2 = this.mEmailRecordSize;
                            if (i2 > 0) {
                                byte[] eMailRecData = buildEmailRecord(eMailAd, adnIndex, i2, true);
                                if (eMailRecData == null) {
                                    return -40;
                                }
                                int efid2 = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP).getEfid();
                                this.mFh.updateEFLinearFixed(efid2, recNum, eMailRecData, (String) null, obtainMessage(8));
                            } else {
                                return -50;
                            }
                        }
                    }
                    return 0;
                }
                Rlog.e(LOG_TAG, "updateType2Email Error: No IAP file!");
                return -50;
            }
            Rlog.w(LOG_TAG, "Warning: IAP size is 0");
            return -50;
        } catch (IndexOutOfBoundsException e2) {
            Rlog.e(LOG_TAG, "Index out of bounds.");
            return -50;
        }
    }

    private byte[] buildAnrRecord(String anr, int recordSize, int aas) {
        log("buildAnrRecord anr:" + anr + ",recordSize:" + recordSize + ",aas:" + aas);
        if (recordSize <= 0) {
            readAnrRecordSize();
        }
        byte[] anrString = new byte[recordSize];
        for (int i = 0; i < recordSize; i++) {
            anrString[i] = -1;
        }
        String updatedAnr = MtkPhoneNumberUtils.convertPreDial(anr);
        if (TextUtils.isEmpty(updatedAnr)) {
            Rlog.w(LOG_TAG, "[buildAnrRecord] Empty dialing number");
            return anrString;
        }
        if (updatedAnr.length() > 20) {
            Rlog.w(LOG_TAG, "[buildAnrRecord] Max length of dialing number is 20");
            return null;
        }
        byte[] bcdNumber = MtkPhoneNumberUtils.numberToCalledPartyBCD(updatedAnr);
        if (bcdNumber != null) {
            anrString[0] = (byte) aas;
            System.arraycopy(bcdNumber, 0, anrString, 2, bcdNumber.length);
            anrString[1] = (byte) bcdNumber.length;
        }
        return anrString;
    }

    private byte[] buildEmailRecord(String strEmail, int adnIndex, int recordSize, boolean emailType2) {
        ArrayList<PbrRecord> arrayList;
        byte[] eMailRecData = new byte[recordSize];
        for (int i = 0; i < recordSize; i++) {
            eMailRecData[i] = -1;
        }
        if (strEmail != null && !strEmail.equals("")) {
            byte[] eMailData = GsmAlphabet.stringToGsm8BitPacked(strEmail);
            int maxDataLength = (this.mEmailRecordSize != -1 && emailType2) ? eMailRecData.length - 2 : eMailRecData.length;
            log("buildEmailRecord eMailData.length=" + eMailData.length + ", maxDataLength=" + maxDataLength);
            if (eMailData.length > maxDataLength) {
                return null;
            }
            System.arraycopy(eMailData, 0, eMailRecData, 0, eMailData.length);
            if (emailType2 && (arrayList = this.mPbrRecords) != null) {
                int i2 = this.mAdnFileSize;
                int pbrIndex = (adnIndex - 1) / i2;
                int adnRecId = (adnIndex % i2) & 255;
                SparseArray<File> files = arrayList.get(pbrIndex).mFileIds;
                File adnFile = files.get(192);
                eMailRecData[recordSize - 2] = (byte) adnFile.getSfi();
                eMailRecData[recordSize - 1] = (byte) adnRecId;
                log("buildEmailRecord x+1=" + adnFile.getSfi() + ", x+2=" + adnRecId);
            }
        }
        return eMailRecData;
    }

    public void updateUsimPhonebookRecordsList(int index, MtkAdnRecord newAdn) {
        logi("updateUsimPhonebookRecordsList update the " + index + "th record.");
        if (index < this.mPhoneBookRecords.size()) {
            MtkAdnRecord oldAdn = this.mPhoneBookRecords.get(index);
            if (oldAdn != null && oldAdn.getGrpIds() != null) {
                newAdn.setGrpIds(oldAdn.getGrpIds());
            }
            this.mPhoneBookRecords.set(index, newAdn);
            this.mRefreshAdnInfo = true;
        }
    }

    private void updatePhoneAdnRecordWithGrpByIndex(int recIndex, int adnIndex, int[] grpIds) {
        int grpSize;
        log("updatePhoneAdnRecordWithGrpByIndex the " + recIndex + "th grp ");
        if (recIndex <= this.mPhoneBookRecords.size() && (grpSize = grpIds.length) > 0) {
            try {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(recIndex);
                log("updatePhoneAdnRecordWithGrpByIndex the adnIndex is " + adnIndex + "; the original index is " + rec.getRecId());
                StringBuilder grpIdsSb = new StringBuilder();
                for (int i = 0; i < grpSize - 1; i++) {
                    grpIdsSb.append(grpIds[i]);
                    grpIdsSb.append(",");
                }
                int i2 = grpSize - 1;
                grpIdsSb.append(grpIds[i2]);
                rec.setGrpIds(grpIdsSb.toString());
                log("updatePhoneAdnRecordWithGrpByIndex grpIds is " + grpIdsSb.toString());
                this.mPhoneBookRecords.set(recIndex, rec);
                log("updatePhoneAdnRecordWithGrpByIndex the rec:" + rec);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithGrpByIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + recIndex);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:83:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01fc A[Catch: all -> 0x0241, TryCatch #2 {all -> 0x0241, blocks: (B:72:0x019f, B:71:0x0196, B:113:0x02b7, B:82:0x01f6, B:104:0x0243, B:105:0x0265, B:106:0x028b, B:110:0x0299, B:109:0x0292, B:84:0x01fc, B:86:0x0204, B:87:0x020a, B:89:0x020c, B:90:0x0213, B:92:0x021b, B:93:0x0221, B:95:0x0223, B:96:0x022a, B:98:0x0232, B:99:0x0238, B:101:0x023a, B:80:0x01c3, B:35:0x00d2), top: B:122:0x0196, inners: #4, #10 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0213 A[Catch: all -> 0x0241, TryCatch #2 {all -> 0x0241, blocks: (B:72:0x019f, B:71:0x0196, B:113:0x02b7, B:82:0x01f6, B:104:0x0243, B:105:0x0265, B:106:0x028b, B:110:0x0299, B:109:0x0292, B:84:0x01fc, B:86:0x0204, B:87:0x020a, B:89:0x020c, B:90:0x0213, B:92:0x021b, B:93:0x0221, B:95:0x0223, B:96:0x022a, B:98:0x0232, B:99:0x0238, B:101:0x023a, B:80:0x01c3, B:35:0x00d2), top: B:122:0x0196, inners: #4, #10 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x022a A[Catch: all -> 0x0241, TryCatch #2 {all -> 0x0241, blocks: (B:72:0x019f, B:71:0x0196, B:113:0x02b7, B:82:0x01f6, B:104:0x0243, B:105:0x0265, B:106:0x028b, B:110:0x0299, B:109:0x0292, B:84:0x01fc, B:86:0x0204, B:87:0x020a, B:89:0x020c, B:90:0x0213, B:92:0x021b, B:93:0x0221, B:95:0x0223, B:96:0x022a, B:98:0x0232, B:99:0x0238, B:101:0x023a, B:80:0x01c3, B:35:0x00d2), top: B:122:0x0196, inners: #4, #10 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readType1Ef(com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.File r24, int r25) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 742
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readType1Ef(com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager$File, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x02f0 A[Catch: all -> 0x039f, TryCatch #11 {all -> 0x039f, blocks: (B:142:0x03a0, B:111:0x02e8, B:112:0x02ea, B:132:0x0335, B:133:0x034d, B:134:0x0373, B:138:0x0381, B:137:0x037a, B:114:0x02f0, B:116:0x02f8, B:117:0x02fe, B:119:0x0300, B:120:0x0307, B:122:0x030f, B:123:0x0315, B:125:0x0317, B:126:0x031e, B:128:0x0326, B:129:0x032c, B:131:0x032e, B:109:0x02b9, B:56:0x014c), top: B:152:0x014c, inners: #1, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0307 A[Catch: all -> 0x039f, TryCatch #11 {all -> 0x039f, blocks: (B:142:0x03a0, B:111:0x02e8, B:112:0x02ea, B:132:0x0335, B:133:0x034d, B:134:0x0373, B:138:0x0381, B:137:0x037a, B:114:0x02f0, B:116:0x02f8, B:117:0x02fe, B:119:0x0300, B:120:0x0307, B:122:0x030f, B:123:0x0315, B:125:0x0317, B:126:0x031e, B:128:0x0326, B:129:0x032c, B:131:0x032e, B:109:0x02b9, B:56:0x014c), top: B:152:0x014c, inners: #1, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x031e A[Catch: all -> 0x039f, TryCatch #11 {all -> 0x039f, blocks: (B:142:0x03a0, B:111:0x02e8, B:112:0x02ea, B:132:0x0335, B:133:0x034d, B:134:0x0373, B:138:0x0381, B:137:0x037a, B:114:0x02f0, B:116:0x02f8, B:117:0x02fe, B:119:0x0300, B:120:0x0307, B:122:0x030f, B:123:0x0315, B:125:0x0317, B:126:0x031e, B:128:0x0326, B:129:0x032c, B:131:0x032e, B:109:0x02b9, B:56:0x014c), top: B:152:0x014c, inners: #1, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0187  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readType2Ef(com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.File r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1004
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager.readType2Ef(com.mediatek.internal.telephony.phb.MtkUsimPhoneBookManager$File):void");
    }

    private void updatePhoneAdnRecordWithEmailByIndex(int emailIndex, int adnIndex, byte[] emailRecData) {
        ArrayList<PbrRecord> arrayList;
        log("updatePhoneAdnRecordWithEmailByIndex emailIndex = " + emailIndex + ",adnIndex = " + adnIndex);
        if (emailRecData == null || (arrayList = this.mPbrRecords) == null || arrayList.size() == 0) {
            return;
        }
        boolean emailType2 = ((File) this.mPbrRecords.get(0).mFileIds.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP)).getParentTag() == 169;
        log("updatePhoneAdnRecordWithEmailByIndex: Type2: " + emailType2 + " emailData: " + IccUtils.bytesToHexString(emailRecData));
        int length = emailRecData.length;
        if (emailType2 && emailRecData.length >= 2) {
            length = emailRecData.length - 2;
        }
        log("updatePhoneAdnRecordWithEmailByIndex length = " + length);
        byte[] validEMailData = new byte[length];
        for (int i = 0; i < length; i++) {
            validEMailData[i] = -1;
        }
        System.arraycopy(emailRecData, 0, validEMailData, 0, length);
        try {
            String email = IccUtils.adnStringFieldToString(validEMailData, 0, length);
            if (email != null && !email.equals("")) {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                rec.setEmails(new String[]{email});
            }
            this.mEmailRecTable[emailIndex - 1] = adnIndex + 1;
        } catch (IndexOutOfBoundsException e) {
            Rlog.e(LOG_TAG, "[JE]updatePhoneAdnRecordWithEmailByIndex " + e.getMessage());
        }
    }

    private void updateType2Anr(String anr, int adnIndex, File file) {
        ArrayList<PbrRecord> arrayList;
        SparseArray<File> files;
        int i;
        ArrayList<byte[]> relatedList;
        int recNum;
        int pbrRecNum;
        ArrayList<byte[]> list;
        MtkAdnRecord rec;
        logi("updateType2Ef anr:" + anr + ",adnIndex:" + adnIndex + ",file:" + file);
        int i2 = this.mAdnFileSize;
        int pbrRecNum2 = (adnIndex - 1) / i2;
        int iapRecNum = (adnIndex - 1) % i2;
        log("updateType2Ef pbrRecNum:" + pbrRecNum2 + ",iapRecNum:" + iapRecNum);
        if (this.mIapFileList == null || file == null || (arrayList = this.mPbrRecords) == null || arrayList.size() == 0 || (files = this.mPbrRecords.get(file.mPbrRecord).mFileIds) == null) {
            return;
        }
        try {
            ArrayList<byte[]> list2 = this.mIapFileList.get(file.mPbrRecord);
            if (list2 == null) {
                return;
            }
            if (list2.size() == 0) {
                Rlog.e(LOG_TAG, "Warning: IAP size is 0");
                return;
            }
            byte[] iap = list2.get(iapRecNum);
            if (iap == null) {
                return;
            }
            int index = iap[file.getIndex()] & 255;
            log("updateType2Ef orignal index :" + index);
            if (anr == null || anr.length() == 0) {
                int index2 = index;
                if (index2 > 0) {
                    iap[file.getIndex()] = -1;
                    if (files.get(193) != null) {
                        this.mFh.updateEFLinearFixed(files.get(193).getEfid(), iapRecNum + 1, iap, (String) null, obtainMessage(7));
                        return;
                    } else {
                        Rlog.e(LOG_TAG, "updateType2Anr Error: No IAP file!");
                        return;
                    }
                }
                return;
            }
            int[] tmpSize = this.mRecordSize.get(file.getEfid());
            int size = tmpSize[2];
            log("updateType2Anr size :" + size);
            if (index > 0 && index <= size) {
                recNum = index;
            } else {
                int[] indexArray = new int[size + 1];
                for (int i3 = 1; i3 <= size; i3++) {
                    indexArray[i3] = 0;
                }
                int i4 = 0;
                while (i4 < list2.size()) {
                    byte[] value = list2.get(i4);
                    if (value == null) {
                        pbrRecNum = pbrRecNum2;
                        list = list2;
                    } else {
                        pbrRecNum = pbrRecNum2;
                        list = list2;
                        int tem = value[file.getIndex()] & 255;
                        if (tem > 0 && tem < 255 && tem <= size) {
                            indexArray[tem] = 1;
                        }
                    }
                    i4++;
                    list2 = list;
                    pbrRecNum2 = pbrRecNum;
                }
                int i5 = 0;
                File file2 = null;
                int i6 = 0;
                while (true) {
                    if (i6 >= this.mPbrRecords.size()) {
                        i = i5;
                        break;
                    }
                    if (i6 == file.mPbrRecord) {
                        i = i5;
                    } else {
                        i = i5;
                        file2 = (File) this.mPbrRecords.get(i6).mFileIds.get((adnIndex * 256) + 196);
                        if (file2 != null) {
                            if (file2.getEfid() == file.getEfid()) {
                                i = 1;
                            }
                        }
                    }
                    i6++;
                    i5 = i;
                }
                if (i != 0) {
                    try {
                        ArrayList<byte[]> relatedList2 = this.mIapFileList.get(file2.mPbrRecord);
                        if (relatedList2 != null && relatedList2.size() > 0) {
                            int i7 = 0;
                            while (i7 < relatedList2.size()) {
                                byte[] value2 = relatedList2.get(i7);
                                if (value2 == null) {
                                    relatedList = relatedList2;
                                } else {
                                    relatedList = relatedList2;
                                    int tem2 = value2[file2.getIndex()] & 255;
                                    if (tem2 > 0 && tem2 < 255 && tem2 <= size) {
                                        indexArray[tem2] = 1;
                                    }
                                }
                                i7++;
                                relatedList2 = relatedList;
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        Rlog.e(LOG_TAG, "Index out of bounds.");
                        return;
                    }
                }
                int i8 = 1;
                while (true) {
                    if (i8 > size) {
                        recNum = 0;
                        break;
                    } else if (indexArray[i8] != 0) {
                        i8++;
                    } else {
                        int recNum2 = i8;
                        recNum = recNum2;
                        break;
                    }
                }
            }
            log("updateType2Anr final index :" + recNum);
            if (recNum == 0) {
                return;
            }
            try {
                rec = this.mPhoneBookRecords.get(adnIndex - 1);
            } catch (IndexOutOfBoundsException e2) {
                Rlog.e(LOG_TAG, "updateType2Anr: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
                rec = null;
            }
            if (rec == null) {
                return;
            }
            int aas = rec.getAasIndex();
            byte[] data = buildAnrRecord(anr, this.mAnrRecordSize, aas);
            int fileId = file.getEfid();
            if (data != null) {
                this.mFh.updateEFLinearFixed(fileId, recNum, data, (String) null, obtainMessage(9));
                if (recNum != index) {
                    iap[file.getIndex()] = (byte) recNum;
                    if (files.get(193) != null) {
                        this.mFh.updateEFLinearFixed(files.get(193).getEfid(), iapRecNum + 1, iap, (String) null, obtainMessage(7));
                    } else {
                        Rlog.e(LOG_TAG, "updateType2Anr Error: No IAP file!");
                    }
                }
            }
        } catch (IndexOutOfBoundsException e3) {
            Rlog.e(LOG_TAG, "Index out of bounds.");
        }
    }

    private void readAnrRecordSize() {
        logi("readAnrRecordSize");
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            Rlog.w(LOG_TAG, "readAnrRecordSize: PBR null ");
            return;
        }
        SparseArray<File> fileIds = this.mPbrRecords.get(0).mFileIds;
        if (fileIds == null) {
            Rlog.w(LOG_TAG, "readAnrRecordSize: fileIds null ");
            return;
        }
        File anrFile = fileIds.get(196);
        if (fileIds.size() == 0 || anrFile == null) {
            this.mAnrRecordSize = 0;
            Rlog.w(LOG_TAG, "readAnrRecordSize: No anr tag in pbr file ");
            return;
        }
        int efid = anrFile.getEfid();
        int[] size = readEFLinearRecordSize(efid);
        if (size == null || size.length != 3) {
            Rlog.e(LOG_TAG, "readAnrRecordSize: read record size error.");
        } else {
            this.mAnrRecordSize = size[0];
            logi("readAnrRecordSize end size = " + this.mAnrRecordSize);
        }
    }

    private void readEmailRecordSize() {
        logi("readEmailRecordSize");
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            Rlog.w(LOG_TAG, "readEmailRecordSize: PBR null");
            return;
        }
        SparseArray<File> fileIds = this.mPbrRecords.get(0).mFileIds;
        if (fileIds == null) {
            Rlog.w(LOG_TAG, "readEmailRecordSize: fileId null");
            return;
        }
        File emailFile = fileIds.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
        if (fileIds.size() == 0 || emailFile == null) {
            this.mEmailRecordSize = 0;
            Rlog.w(LOG_TAG, "readEmailRecordSize: No email tag in pbr file ");
            return;
        }
        int efid = emailFile.getEfid();
        int[] size = readEFLinearRecordSize(efid);
        if (size == null || size.length != 3) {
            Rlog.e(LOG_TAG, "readEmailRecordSize: read record size error.");
            return;
        }
        this.mEmailFileSize = size[2];
        this.mEmailRecordSize = size[0];
        logi("readEmailRecordSize Size:" + this.mEmailFileSize + "," + this.mEmailRecordSize);
    }

    private boolean loadAasFiles() {
        synchronized (this.mLock) {
            ArrayList<String> arrayList = this.mAasForAnr;
            if (arrayList == null || arrayList.size() == 0) {
                if (!this.mIsPbrPresent.booleanValue()) {
                    Rlog.e(LOG_TAG, "No PBR files");
                    return false;
                }
                loadPBRFiles();
                ArrayList<PbrRecord> arrayList2 = this.mPbrRecords;
                if (arrayList2 != null && arrayList2.size() != 0) {
                    int numRecs = this.mPbrRecords.size();
                    if (this.mAasForAnr == null) {
                        this.mAasForAnr = new ArrayList<>();
                    }
                    this.mAasForAnr.clear();
                    logi("loadAasFiles read num:" + numRecs + ", " + this.mPbrNeedNotify);
                    if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
                        for (int i = 0; i < numRecs; i++) {
                            readAASFileAndWait(i);
                        }
                    } else {
                        readAasFileAndWaitOptmz();
                    }
                }
                return false;
            }
            return true;
        }
    }

    public ArrayList<AlphaTag> getUsimAasList() {
        ArrayList<String> allAas;
        logi("getUsimAasList start mPbrNeedNotify:" + this.mPbrNeedNotify);
        ArrayList<AlphaTag> results = new ArrayList<>();
        if (!loadAasFiles() || (allAas = this.mAasForAnr) == null) {
            return results;
        }
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < allAas.size(); j++) {
                String value = allAas.get(j);
                logi("aasIndex:" + (j + 1) + ",pbrIndex:" + i + ",value:" + value);
                AlphaTag tag = new AlphaTag(j + 1, value, i);
                results.add(tag);
            }
        }
        return results;
    }

    public String getUsimAasById(int index, int pbrIndex) {
        logi("getUsimAasById by id " + index + ",pbrIndex " + pbrIndex + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (!loadAasFiles()) {
            return null;
        }
        ArrayList<String> map = this.mAasForAnr;
        if (map != null) {
            logi("getUsimAasById NonNULL by id " + index + ",pbrIndex " + pbrIndex);
            return map.get(index - 1);
        }
        logi("getUsimAasById NULL by id " + index + ",pbrIndex " + pbrIndex);
        return null;
    }

    public boolean removeUsimAasById(int index, int pbrIndex) {
        logi("removeUsimAasById by id " + index + ",pbrIndex " + pbrIndex + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (!loadAasFiles()) {
            return false;
        }
        SparseArray<File> files = this.mPbrRecords.get(pbrIndex).mFileIds;
        if (files == null || files.get(199) == null) {
            Rlog.e(LOG_TAG, "removeUsimAasById-PBR have no AAS EF file");
            return false;
        }
        int efid = files.get(199).getEfid();
        log("removeUsimAasById result,efid:" + efid);
        if (this.mFh != null) {
            Message msg = obtainMessage(10);
            int len = getUsimAasMaxNameLen();
            byte[] aasString = new byte[len];
            for (int i = 0; i < len; i++) {
                aasString[i] = -1;
            }
            synchronized (this.mAasLock) {
                this.mCi.deleteUPBEntry(3, 1, index, msg);
                try {
                    this.mAasLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in removesimAasById");
                }
            }
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar == null || ar.exception == null) {
                ArrayList<String> list = this.mAasForAnr;
                if (list != null) {
                    log("remove aas done " + list.get(index - 1));
                    list.set(index - 1, null);
                } else {
                    log("remove aas mAasForAnr is null ");
                }
                return true;
            }
            Rlog.e(LOG_TAG, "removeUsimAasById exception " + ar.exception);
            return false;
        }
        Rlog.e(LOG_TAG, "removeUsimAasById-IccFileHandler is null");
        return false;
    }

    public int insertUsimAas(String aasName) {
        boolean found;
        int aasIndex;
        logi("insertUsimAas begin" + aasName + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (aasName == null || aasName.length() == 0) {
            return 0;
        }
        if (!loadAasFiles()) {
            return -1;
        }
        int limit = getUsimAasMaxNameLen();
        int len = aasName.length();
        if (len > limit) {
            return 0;
        }
        synchronized (this.mAasLock) {
            ArrayList<String> allAas = this.mAasForAnr;
            if (allAas == null) {
                Rlog.e(LOG_TAG, "insertUsimAas, mAasForAnr is null");
                return -2;
            }
            for (int j = 0; j < allAas.size(); j++) {
                String value = allAas.get(j);
                if (value != null && value.length() != 0) {
                }
                int aasIndex2 = j + 1;
                found = true;
                aasIndex = aasIndex2;
            }
            found = false;
            aasIndex = 0;
            log("insertUsimAas aasIndex:" + aasIndex + ",found:" + found);
            if (!found) {
                return -2;
            }
            String temp = encodeToUcs2(aasName);
            Message msg = obtainMessage(10);
            this.mCi.editUPBEntry(3, 0, aasIndex, temp, null, msg);
            try {
                this.mAasLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in insertUsimAas");
            }
            AsyncResult ar = (AsyncResult) msg.obj;
            logi("insertUsimAas UPB_EF_AAS: ar " + ar);
            if (ar != null && ar.exception != null) {
                Rlog.e(LOG_TAG, "insertUsimAas exception " + ar.exception);
                return -1;
            }
            ArrayList<String> list = this.mAasForAnr;
            if (list == null) {
                logi("insertUsimAas mAasForAnr is null");
            } else {
                list.set(aasIndex - 1, aasName);
                logi("insertUsimAas update mAasForAnr done");
            }
            return aasIndex;
        }
    }

    public boolean updateUsimAas(int index, int pbrIndex, String aasName) throws Throwable {
        Object obj;
        logi("updateUsimAas index " + index + ",pbrIndex " + pbrIndex + ",aasName " + aasName + ",mPbrNeedNotify " + this.mPbrNeedNotify);
        if (!loadAasFiles()) {
            return false;
        }
        ArrayList<String> map = this.mAasForAnr;
        if (map == null) {
            Rlog.e(LOG_TAG, "updateUsimAas, mAasForAnr is null, index " + index);
            return false;
        }
        if (index <= 0 || index > map.size()) {
            Rlog.e(LOG_TAG, "updateUsimAas not found aas index " + index);
            return false;
        }
        String aas = map.get(index - 1);
        log("updateUsimAas old aas " + aas);
        if (aasName == null || aasName.length() == 0) {
            return removeUsimAasById(index, pbrIndex);
        }
        int limit = getUsimAasMaxNameLen();
        int len = aasName.length();
        log("updateUsimAas aas limit " + limit);
        if (len > limit) {
            return false;
        }
        log("updateUsimAas offset 0");
        int aasIndex = index + 0;
        String temp = encodeToUcs2(aasName);
        Message msg = obtainMessage(10);
        Object obj2 = this.mAasLock;
        synchronized (obj2) {
            try {
                obj = obj2;
                try {
                    this.mCi.editUPBEntry(3, 0, aasIndex, temp, null, msg);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                obj = obj2;
            }
            try {
                this.mAasLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in updateUsimAas");
            } catch (Throwable th3) {
                th = th3;
                while (true) {
                    try {
                        throw th;
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
            }
            AsyncResult ar = (AsyncResult) msg.obj;
            if (ar == null || ar.exception == null) {
                ArrayList<String> list = this.mAasForAnr;
                if (list == null) {
                    logi("updateUsimAas mAasForAnr is null");
                    return true;
                }
                list.set(index - 1, aasName);
                logi("updateUsimAas update mAasForAnr done");
                return true;
            }
            Rlog.e(LOG_TAG, "updateUsimAas exception " + ar.exception);
            return false;
        }
    }

    public boolean updateAdnAas(int adnIndex, int aasIndex) throws Throwable {
        int i = this.mAdnFileSize;
        int i2 = (adnIndex - 1) / i;
        int i3 = (adnIndex - 1) % i;
        try {
            MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex - 1);
            rec.setAasIndex(aasIndex);
            for (int i4 = 0; i4 < 3; i4++) {
                String anr = rec.getAdditionalNumber(i4);
                updateAnrByAdnIndex(anr, adnIndex, i4, rec);
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            Rlog.e(LOG_TAG, "updateADNAAS: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + (adnIndex - 1));
            return false;
        }
    }

    public int getUsimAasMaxNameLen() {
        logi("getUsimAasMaxNameLen begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[4] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(19));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getUsimAasMaxNameLen");
                }
            }
        }
        logi("getUsimAasMaxNameLen done: L_AAS is " + this.mUpbCap[4]);
        return this.mUpbCap[4];
    }

    public int getUsimAasMaxCount() {
        logi("getUsimAasMaxCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[3] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(19));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getUsimAasMaxCount");
                }
            }
        }
        logi("getUsimAasMaxCount done: N_AAS is " + this.mUpbCap[3]);
        return this.mUpbCap[3];
    }

    public void loadPBRFiles() {
        if (!this.mIsPbrPresent.booleanValue()) {
            return;
        }
        synchronized (this.mLock) {
            ArrayList<PbrRecord> arrayList = this.mPbrRecords;
            if (arrayList == null || arrayList.size() == 0) {
                this.mPbrNeedNotify++;
                readPbrFileAndWait();
            }
        }
    }

    public int getAnrCount() {
        logi("getAnrCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[0] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(19));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAnrCount");
                }
            }
        }
        if (this.mAnrRecordSize <= 0) {
            logi("getAnrCount end mAnrRecordSize:" + this.mAnrRecordSize);
            return this.mAnrRecordSize;
        }
        logi("getAnrCount done: N_ANR is " + this.mUpbCap[0]);
        return this.mUpbCap[0] > 0 ? 1 : 0;
    }

    public int getEmailCount() {
        logi("getEmailCount begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[1] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(19));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getEmailCount");
                }
            }
        }
        if (this.mEmailRecordSize <= 0) {
            logi("getEmailCount end mEmailRecordSize:" + this.mEmailRecordSize);
            return this.mEmailRecordSize;
        }
        logi("getEmailCount done: N_EMAIL is " + this.mUpbCap[1]);
        return this.mUpbCap[1] > 0 ? 1 : 0;
    }

    public boolean hasSne() {
        log("hasSne begin");
        synchronized (this.mUPBCapabilityLock) {
            if (this.mUpbCap[2] < 0 && checkIsPhbReady()) {
                this.mCi.queryUPBCapability(obtainMessage(19));
                try {
                    this.mUPBCapabilityLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in hasSne");
                }
            }
        }
        log("hasSne done: N_Sne is " + this.mUpbCap[2]);
        return this.mUpbCap[2] > 0;
    }

    public int getSneRecordLen() {
        SparseArray<File> files;
        int[] size;
        if (!hasSne()) {
            return 0;
        }
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0 || this.mPbrRecords.get(0) == null || (files = this.mPbrRecords.get(0).mFileIds) == null) {
            return -1;
        }
        File sneFile = files.get(195);
        if (sneFile == null) {
            return 0;
        }
        int efid = sneFile.getEfid();
        boolean sneType2 = sneFile.getParentTag() == 169;
        logi("getSneRecordLen: EFSNE id is " + efid);
        SparseArray<int[]> sparseArray = this.mRecordSize;
        if (sparseArray != null && sparseArray.get(efid) != null) {
            int[] size2 = this.mRecordSize.get(efid);
            size = size2;
        } else {
            size = readEFLinearRecordSize(efid);
        }
        if (size == null) {
            return 0;
        }
        if (sneType2) {
            int resultSize = size[0] - 2;
            return resultSize;
        }
        int resultSize2 = size[0];
        return resultSize2;
    }

    public int getUpbDone() {
        return this.mUpbDone;
    }

    private void updatePhoneAdnRecordWithSneByIndex(int recNum, int adnIndex, byte[] recData) {
        if (recData == null) {
            return;
        }
        String sne = IccUtils.adnStringFieldToString(recData, 0, recData.length);
        log("updatePhoneAdnRecordWithSneByIndex index " + adnIndex + " recData file is " + sne);
        if (sne != null && !sne.equals("")) {
            try {
                MtkAdnRecord rec = this.mPhoneBookRecords.get(adnIndex);
                rec.setSne(sne);
            } catch (IndexOutOfBoundsException e) {
                Rlog.e(LOG_TAG, "updatePhoneAdnRecordWithSneByIndex: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + adnIndex);
            }
        }
    }

    public int updateSneByAdnIndex(String sne, int adnIndex, MtkAdnRecord oldAdn) throws Throwable {
        String oldSne;
        Object obj;
        int efIndex;
        logi("updateSneByAdnIndex begin, adnIndex " + adnIndex);
        int i = this.mAdnFileSize;
        int pbrRecNum = (adnIndex - 1) / i;
        int i2 = (adnIndex - 1) % i;
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            return -1;
        }
        Message msg = obtainMessage(11);
        SparseArray<File> files = this.mPbrRecords.get(pbrRecNum).mFileIds;
        if (files == null || files.get(195) == null) {
            log("updateSneByAdnIndex: No SNE tag in pbr file 0");
            return -1;
        }
        ArrayList<MtkAdnRecord> arrayList2 = this.mPhoneBookRecords;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            return -1;
        }
        if (oldAdn != null) {
            String oldSne2 = oldAdn.getSne();
            oldSne = oldSne2;
        } else {
            oldSne = null;
        }
        File sneFile = files.get(195);
        int efid = sneFile.getEfid();
        log("updateSneByAdnIndex: EF_SNE id is " + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
        log("updateSneByAdnIndex: efIndex is 1");
        Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
            } catch (Throwable th) {
                th = th;
            }
            if (sne != null) {
                try {
                    if (sne.length() == 0) {
                        obj = obj2;
                        efIndex = 1;
                    } else {
                        if (!sne.equals(oldSne)) {
                            String temp = encodeToUcs2(sne);
                            obj = obj2;
                            this.mCi.editUPBEntry(2, 1, adnIndex, temp, null, msg);
                            try {
                                this.mLock.wait();
                            } catch (InterruptedException e) {
                                Rlog.e(LOG_TAG, "Interrupted Exception in updateSneByAdnIndex");
                            }
                            logi("updateSneByAdnIndex end, adnIndex " + adnIndex);
                            return this.mResult;
                        }
                        try {
                        } catch (Throwable th2) {
                            th = th2;
                            obj = obj2;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    obj = obj2;
                }
                throw th;
            }
            obj = obj2;
            efIndex = 1;
            if (oldSne != null && oldSne.length() != 0) {
                this.mCi.deleteUPBEntry(2, efIndex, adnIndex, msg);
                this.mLock.wait();
                logi("updateSneByAdnIndex end, adnIndex " + adnIndex);
                return this.mResult;
            }
            return 0;
        }
        return 0;
    }

    public int[] getAdnRecordsCapacityExt() {
        ArrayList<int[]> arrayList;
        int[] iArr;
        int[] iArr2;
        int[] capacity = new int[6];
        if (this.mRefreshAdnInfo || this.mRefreshEmailInfo || this.mRefreshAnrInfo || (iArr2 = this.mAdnRecordSize) == null || iArr2.length != 4) {
            getAdnStorageInfo();
            this.mRefreshAdnInfo = false;
        }
        int[] iArr3 = this.mAdnRecordSize;
        if (iArr3 == null || iArr3.length != 4) {
            return null;
        }
        capacity[0] = iArr3[1];
        capacity[1] = iArr3[0];
        if (this.mRefreshEmailInfo || (iArr = this.mEmailInfo) == null || iArr.length != 3) {
            synchronized (this.mLock) {
                this.mCi.queryUPBAvailable(1, 1, obtainMessage(EVENT_QUERY_EMAIL_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAdnRecordsCapacityExt");
                }
            }
            this.mRefreshEmailInfo = false;
        }
        int[] iArr4 = this.mEmailInfo;
        if (iArr4 == null || iArr4.length != 3) {
            return null;
        }
        int i = iArr4[0];
        capacity[2] = i;
        capacity[3] = i - iArr4[1];
        if (this.mRefreshAnrInfo || (arrayList = this.mAnrInfo) == null || arrayList.get(0) == null || this.mAnrInfo.get(0).length != 3) {
            synchronized (this.mLock) {
                this.mCi.queryUPBAvailable(0, 1, obtainMessage(EVENT_QUERY_ANR_AVAILABLE_OPTMZ_DONE));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e2) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAdnRecordsCapacityExt");
                }
            }
            this.mRefreshAnrInfo = false;
        }
        ArrayList<int[]> arrayList2 = this.mAnrInfo;
        if (arrayList2 == null || arrayList2.get(0) == null || this.mAnrInfo.get(0).length != 3) {
            return null;
        }
        capacity[4] = this.mAnrInfo.get(0)[0];
        capacity[5] = this.mAnrInfo.get(0)[0] - this.mAnrInfo.get(0)[1];
        logi("getAdnRecordsCapacityExt: max adn=" + capacity[0] + ", used adn=" + capacity[1] + ", max email=" + capacity[2] + ", used email=" + capacity[3] + ", max anr=" + capacity[4] + ", used anr=" + capacity[5]);
        return capacity;
    }

    private int[] getAdnStorageInfo() {
        logi("getAdnStorageInfo");
        if (this.mCi != null) {
            synchronized (this.mLock) {
                this.mCi.queryPhbStorageInfo(0, obtainMessage(21));
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in getAdnStorageInfo");
                }
            }
            return this.mAdnRecordSize;
        }
        Rlog.w(LOG_TAG, "GetAdnStorageInfo: filehandle is null.");
        return null;
    }

    public UsimPBMemInfo[] getPhonebookMemStorageExt() throws Throwable {
        boolean is3G;
        char c;
        int[] size;
        ArrayList<byte[]> ext1;
        File emailFile;
        int[] size2;
        File anrFile;
        int[] size3;
        boolean is3G2;
        char c2 = 0;
        boolean is3G3 = this.mCurrentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM;
        logi("getPhonebookMemStorageExt isUsim " + is3G3);
        if (!is3G3) {
            return getPhonebookMemStorageExt2G();
        }
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            loadPBRFiles();
        }
        ArrayList<PbrRecord> arrayList2 = this.mPbrRecords;
        if (arrayList2 != null && arrayList2.size() != 0) {
            log("getPhonebookMemStorageExt slice " + this.mPbrRecords.size());
            UsimPBMemInfo[] response = new UsimPBMemInfo[this.mPbrRecords.size()];
            for (int i = 0; i < this.mPbrRecords.size(); i++) {
                response[i] = new UsimPBMemInfo();
            }
            if (this.mPhoneBookRecords.isEmpty()) {
                Rlog.w(LOG_TAG, "mPhoneBookRecords has not been loaded.");
                return response;
            }
            int pbrIndex = 0;
            while (pbrIndex < this.mPbrRecords.size()) {
                SparseArray<File> files = this.mPbrRecords.get(pbrIndex).mFileIds;
                int numAdnRecs = this.mPhoneBookRecords.size();
                int i2 = this.mAdnFileSize;
                int nOffset = pbrIndex * i2;
                int nMax = i2 + nOffset;
                int nMax2 = numAdnRecs < nMax ? numAdnRecs : nMax;
                File adnFile = files.get(192);
                if (adnFile != null) {
                    int[] size4 = readEFLinearRecordSize(adnFile.getEfid());
                    if (size4 != null) {
                        response[pbrIndex].setAdnLength(size4[c2]);
                        if (0 > 0) {
                            response[pbrIndex].setAdnTotal(size4[2] + 0);
                        } else {
                            response[pbrIndex].setAdnTotal(size4[2]);
                        }
                    }
                    response[pbrIndex].setAdnType(adnFile.getParentTag());
                    response[pbrIndex].setSliceIndex(pbrIndex + 1);
                    MtkAdnRecord rec = null;
                    int j = nOffset;
                    int used = 0;
                    while (j < nMax2) {
                        try {
                            rec = this.mPhoneBookRecords.get(j);
                            is3G2 = is3G3;
                        } catch (IndexOutOfBoundsException e) {
                            is3G2 = is3G3;
                            Rlog.e(LOG_TAG, "getPhonebookMemStorageExt: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + j);
                        }
                        if (rec != null && ((rec.getAlphaTag() != null && rec.getAlphaTag().length() > 0) || (rec.getNumber() != null && rec.getNumber().length() > 0))) {
                            log("Adn: " + rec.toString());
                            used++;
                            rec = null;
                        }
                        j++;
                        is3G3 = is3G2;
                    }
                    is3G = is3G3;
                    log("adn used " + used);
                    response[pbrIndex].setAdnUsed(used);
                } else {
                    is3G = is3G3;
                }
                File anrFile2 = files.get(196);
                if (anrFile2 != null) {
                    int[] size5 = readEFLinearRecordSize(anrFile2.getEfid());
                    if (size5 != null) {
                        response[pbrIndex].setAnrLength(size5[0]);
                        response[pbrIndex].setAnrTotal(size5[2]);
                    }
                    response[pbrIndex].setAnrType(anrFile2.getParentTag());
                    MtkAdnRecord rec2 = null;
                    int i3 = nOffset;
                    int used2 = 0;
                    while (i3 < nMax2) {
                        try {
                            MtkAdnRecord rec3 = this.mPhoneBookRecords.get(i3);
                            rec2 = rec3;
                            anrFile = anrFile2;
                            size3 = size5;
                        } catch (IndexOutOfBoundsException e2) {
                            anrFile = anrFile2;
                            size3 = size5;
                            Rlog.e(LOG_TAG, "getPhonebookMemStorageExt: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + i3);
                        }
                        if (rec2 == null) {
                            log("null anr rec ");
                        } else {
                            String anrStr = rec2.getAdditionalNumber();
                            if (anrStr != null && anrStr.length() > 0) {
                                log("anrStr: " + anrStr);
                                used2++;
                            }
                        }
                        i3++;
                        anrFile2 = anrFile;
                        size5 = size3;
                    }
                    log("anr used: " + used2);
                    response[pbrIndex].setAnrUsed(used2);
                }
                File emailFile2 = files.get(ExternalSimConstants.EVENT_TYPE_RECEIVE_RSIM_AUTH_RSP);
                if (emailFile2 != null) {
                    int[] size6 = readEFLinearRecordSize(emailFile2.getEfid());
                    if (size6 != null) {
                        response[pbrIndex].setEmailLength(size6[0]);
                        response[pbrIndex].setEmailTotal(size6[2]);
                    }
                    response[pbrIndex].setEmailType(emailFile2.getParentTag());
                    MtkAdnRecord rec4 = null;
                    int i4 = nOffset;
                    int used3 = 0;
                    while (i4 < nMax2) {
                        try {
                            MtkAdnRecord rec5 = this.mPhoneBookRecords.get(i4);
                            rec4 = rec5;
                            emailFile = emailFile2;
                            size2 = size6;
                        } catch (IndexOutOfBoundsException e3) {
                            emailFile = emailFile2;
                            size2 = size6;
                            Rlog.e(LOG_TAG, "getPhonebookMemStorageExt: mPhoneBookRecords IndexOutOfBoundsException mPhoneBookRecords.size() is " + this.mPhoneBookRecords.size() + "index is " + i4);
                        }
                        if (rec4 == null) {
                            log("null email rec ");
                        } else {
                            String[] emails = rec4.getEmails();
                            if (emails != null && emails.length > 0 && emails[0].length() > 0) {
                                log("email: " + emails[0]);
                                used3++;
                            }
                        }
                        i4++;
                        emailFile2 = emailFile;
                        size6 = size2;
                    }
                    log("email used: " + used3);
                    response[pbrIndex].setEmailUsed(used3);
                }
                File ext1File = files.get(194);
                if (ext1File != null) {
                    int[] size7 = readEFLinearRecordSize(ext1File.getEfid());
                    if (size7 != null) {
                        response[pbrIndex].setExt1Length(size7[0]);
                        response[pbrIndex].setExt1Total(size7[2]);
                    }
                    response[pbrIndex].setExt1Type(ext1File.getParentTag());
                    synchronized (this.mLock) {
                        try {
                            readExt1FileAndWait(pbrIndex);
                        } catch (Throwable th) {
                            th = th;
                            while (true) {
                                try {
                                    throw th;
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                            }
                        }
                    }
                    int used4 = 0;
                    ArrayList<ArrayList<byte[]>> arrayList3 = this.mExt1FileList;
                    if (arrayList3 == null || pbrIndex >= arrayList3.size() || (ext1 = this.mExt1FileList.get(pbrIndex)) == null) {
                        size = size7;
                    } else {
                        int len = ext1.size();
                        int i5 = 0;
                        while (i5 < len) {
                            byte[] arr = ext1.get(i5);
                            File ext1File2 = ext1File;
                            int[] size8 = size7;
                            log("ext1[" + i5 + "]=" + IccUtils.bytesToHexString(arr));
                            if (arr != null && arr.length > 0 && (arr[0] == 1 || arr[0] == 2)) {
                                used4++;
                            }
                            i5++;
                            ext1File = ext1File2;
                            size7 = size8;
                        }
                        size = size7;
                    }
                    response[pbrIndex].setExt1Used(used4);
                }
                File gasFile = files.get(200);
                if (gasFile != null) {
                    int[] size9 = readEFLinearRecordSize(gasFile.getEfid());
                    if (size9 != null) {
                        response[pbrIndex].setGasLength(size9[0]);
                        response[pbrIndex].setGasTotal(size9[2]);
                    }
                    response[pbrIndex].setGasType(gasFile.getParentTag());
                }
                File aasFile = files.get(199);
                if (aasFile != null) {
                    int[] size10 = readEFLinearRecordSize(aasFile.getEfid());
                    if (size10 != null) {
                        response[pbrIndex].setAasLength(size10[0]);
                        response[pbrIndex].setAasTotal(size10[2]);
                    }
                    response[pbrIndex].setAasType(aasFile.getParentTag());
                }
                File sneFile = files.get(195);
                if (sneFile != null) {
                    int[] size11 = readEFLinearRecordSize(sneFile.getEfid());
                    if (size11 != null) {
                        response[pbrIndex].setSneLength(size11[0]);
                        response[pbrIndex].setSneTotal(size11[0]);
                    }
                    response[pbrIndex].setSneType(sneFile.getParentTag());
                }
                File ccpFile = files.get(ExternalSimConstants.EVENT_TYPE_RSIM_AUTH_DONE);
                if (ccpFile != null) {
                    int[] size12 = readEFLinearRecordSize(ccpFile.getEfid());
                    if (size12 != null) {
                        c = 0;
                        response[pbrIndex].setCcpLength(size12[0]);
                        response[pbrIndex].setCcpTotal(size12[0]);
                    } else {
                        c = 0;
                    }
                    response[pbrIndex].setCcpType(ccpFile.getParentTag());
                } else {
                    c = 0;
                }
                pbrIndex++;
                c2 = c;
                is3G3 = is3G;
            }
            for (int i6 = 0; i6 < this.mPbrRecords.size(); i6++) {
                log("getPhonebookMemStorageExt[" + i6 + "]:" + response[i6]);
            }
            return response;
        }
        return null;
    }

    public UsimPBMemInfo[] getPhonebookMemStorageExt2G() {
        ArrayList<byte[]> ext1;
        UsimPBMemInfo[] response = {new UsimPBMemInfo()};
        int[] size = readEFLinearRecordSize(28474);
        if (size != null) {
            response[0].setAdnLength(size[0]);
            if (isAdnAccessible()) {
                response[0].setAdnTotal(size[2]);
            } else {
                response[0].setAdnTotal(0);
            }
        }
        response[0].setAdnType(168);
        response[0].setSliceIndex(1);
        int[] size2 = readEFLinearRecordSize(28490);
        if (size2 != null) {
            response[0].setExt1Length(size2[0]);
            response[0].setExt1Total(size2[2]);
        }
        response[0].setExt1Type(170);
        if (this.mFh != null) {
            synchronized (this.mLock) {
                Message msg = obtainMessage(1001);
                msg.arg1 = 0;
                this.mFh.loadEFLinearFixedAll(28490, msg);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readExt1FileAndWait");
                }
            }
            int used = 0;
            ArrayList<ArrayList<byte[]>> arrayList = this.mExt1FileList;
            if (arrayList != null && arrayList.size() > 0 && (ext1 = this.mExt1FileList.get(0)) != null) {
                int len = ext1.size();
                for (int i = 0; i < len; i++) {
                    byte[] arr = ext1.get(i);
                    log("ext1[" + i + "]=" + IccUtils.bytesToHexString(arr));
                    if (arr != null && arr.length > 0 && (arr[0] == 1 || arr[0] == 2)) {
                        used++;
                    }
                }
            }
            response[0].setExt1Used(used);
            logi("getPhonebookMemStorageExt2G:" + response[0]);
            return response;
        }
        Rlog.e(LOG_TAG, "readExt1FileAndWait-IccFileHandler is null");
        return response;
    }

    public int[] readEFLinearRecordSize(int fileId) {
        int[] recordSize;
        logi("readEFLinearRecordSize fileid:" + Integer.toHexString(fileId).toUpperCase(Locale.ROOT) + ",recordNum:" + this.mReadEFLinerRecordSizeNum);
        Request getSizeRequest = new Request();
        synchronized (getSizeRequest) {
            Message msg = this.mBaseHandler.obtainMessage(EVENT_GET_RECORDS_SIZE_DONE, getSizeRequest);
            msg.arg1 = fileId;
            if (this.mFh != null) {
                this.mReadEFLinerRecordSizeNum++;
                this.mFh.getEFLinearRecordSize(fileId, msg);
                waitForResult(getSizeRequest);
            } else {
                Rlog.e(LOG_TAG, "readEFLinearRecordSize-IccFileHandler is null");
            }
            if (this.mFh != null) {
                this.mReadEFLinerRecordSizeNum--;
            }
            SparseArray<int[]> sparseArray = this.mRecordSize;
            recordSize = sparseArray != null ? sparseArray.get(fileId) : null;
            if (recordSize != null) {
                logi("readEFLinearRecordSize fileid:" + Integer.toHexString(fileId).toUpperCase(Locale.ROOT) + ",len:" + recordSize[0] + ",total:" + recordSize[1] + ",count:" + recordSize[2] + ",recordNum:" + this.mReadEFLinerRecordSizeNum);
            } else {
                logi("readEFLinearRecordSize fileid:" + Integer.toHexString(fileId).toUpperCase(Locale.ROOT) + ",recordSize: null");
            }
        }
        return recordSize;
    }

    protected void waitForResult(Request request) {
        synchronized (request) {
            while (!request.mStatus.get()) {
                try {
                    request.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "interrupted while trying to update by search");
                }
            }
        }
    }

    private void readExt1FileAndWait(int recId) {
        logi("readExt1FileAndWait " + recId);
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0 || this.mPbrRecords.get(recId) == null) {
            return;
        }
        SparseArray<File> files = this.mPbrRecords.get(recId).mFileIds;
        if (files == null || files.get(194) == null) {
            Rlog.e(LOG_TAG, "readExt1FileAndWait-PBR have no Ext1 record");
            return;
        }
        int efid = files.get(194).getEfid();
        log("readExt1FileAndWait-get EXT1 EFID " + efid);
        ArrayList<ArrayList<byte[]>> arrayList2 = this.mExt1FileList;
        if (arrayList2 != null && recId < arrayList2.size()) {
            log("EXT1 has been loaded for Pbr number " + recId);
            return;
        }
        if (this.mFh != null) {
            synchronized (this.mLock) {
                Message msg = obtainMessage(1001);
                msg.arg1 = recId;
                this.mFh.loadEFLinearFixedAll(efid, msg);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in readExt1FileAndWait");
                }
            }
            return;
        }
        Rlog.e(LOG_TAG, "readExt1FileAndWait-IccFileHandler is null");
    }

    private boolean checkIsPhbReady() {
        String strCurSimState = "";
        if (!SubscriptionManager.isValidSlotIndex(this.mSlotId)) {
            log("[isPhbReady] InvalidSlotId slotId: " + this.mSlotId);
            return false;
        }
        String strPhbReady = TelephonyManager.getTelephonyProperty(this.mSlotId, "vendor.gsm.sim.ril.phbready", "false");
        if (SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            logi("[isPhbReady] isPhbReady: " + strPhbReady);
            return strPhbReady.equals("true");
        }
        String strAllSimState = SystemProperties.get("gsm.sim.state");
        if (strAllSimState != null && strAllSimState.length() > 0) {
            String[] values = strAllSimState.split(",");
            int i = this.mSlotId;
            if (i >= 0 && i < values.length && values[i] != null) {
                strCurSimState = values[i];
            }
        }
        boolean isSimLocked = strCurSimState.equals("NETWORK_LOCKED") || strCurSimState.equals("PIN_REQUIRED");
        logi("[isPhbReady] isPhbReady: " + strPhbReady + ",strSimState: " + strAllSimState);
        return strPhbReady.equals("true") && !isSimLocked;
    }

    public boolean isAdnAccessible() {
        if (this.mFh != null && this.mCurrentApp.getType() == IccCardApplicationStatus.AppType.APPTYPE_SIM) {
            synchronized (this.mLock) {
                Message response = obtainMessage(20);
                this.mFh.selectEFFile(28474, response);
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    Rlog.e(LOG_TAG, "Interrupted Exception in isAdnAccessible");
                }
            }
            EFResponseData eFResponseData = this.mEfData;
            if (eFResponseData != null) {
                int fs = eFResponseData.getFileStatus();
                return (fs & 5) > 0;
            }
        }
        return true;
    }

    public boolean isUsimPhbEfAndNeedReset(int fileId) {
        logi("isUsimPhbEfAndNeedReset, fileId: " + Integer.toHexString(fileId).toUpperCase(Locale.ROOT));
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0) {
            Rlog.e(LOG_TAG, "isUsimPhbEfAndNeedReset, No PBR files");
            return false;
        }
        int numRecs = this.mPbrRecords.size();
        for (int i = 0; i < numRecs; i++) {
            SparseArray<File> files = this.mPbrRecords.get(i).mFileIds;
            for (int j = 192; j <= 203; j++) {
                if (j == 197 || j == 201 || j == 203) {
                    logi("isUsimPhbEfAndNeedReset, not reset EF: " + j);
                } else if (files.get(j) != null && fileId == files.get(j).getEfid()) {
                    logi("isUsimPhbEfAndNeedReset, return true with EF: " + j);
                    return true;
                }
            }
        }
        log("isUsimPhbEfAndNeedReset, return false.");
        return false;
    }

    private void readAdnFileAndWaitForUICC(int recId) {
        SparseArray<File> files;
        logi("readAdnFileAndWaitForUICC begin" + recId);
        ArrayList<PbrRecord> arrayList = this.mPbrRecords;
        if (arrayList == null || arrayList.size() == 0 || (files = this.mPbrRecords.get(recId).mFileIds) == null || files.size() == 0) {
            return;
        }
        if (files.get(192) == null) {
            Rlog.e(LOG_TAG, "readAdnFileAndWaitForUICC: No ADN tag in pbr record " + recId);
            return;
        }
        int efid = files.get(192).getEfid();
        log("readAdnFileAndWaitForUICC: EFADN id is " + efid);
        log("UiccPhoneBookManager readAdnFileAndWaitForUICC: recId is " + recId + "");
        synchronized (this.mLock) {
            MtkAdnRecordCache mtkAdnRecordCache = this.mAdnCache;
            mtkAdnRecordCache.requestLoadAllAdnLike(efid, mtkAdnRecordCache.extensionEfForEf(28474), obtainMessage(2));
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                Rlog.e(LOG_TAG, "Interrupted Exception in readAdnFileAndWait");
            }
        }
        int previousSize = this.mPhoneBookRecords.size();
        ArrayList<PbrRecord> arrayList2 = this.mPbrRecords;
        if (arrayList2 != null && arrayList2.size() > recId) {
            this.mPbrRecords.get(recId).mMasterFileRecordNum = this.mPhoneBookRecords.size() - previousSize;
        }
        logi("readAdnFileAndWaitForUICC end" + recId);
    }

    private ArrayList<MtkAdnRecord> changeAdnRecordNumber(int baseNumber, ArrayList<MtkAdnRecord> adnList) {
        int size = adnList.size();
        for (int i = 0; i < size; i++) {
            MtkAdnRecord adnRecord = adnList.get(i);
            if (adnRecord != null) {
                adnRecord.setRecordIndex(adnRecord.getRecId() + baseNumber);
            }
        }
        return adnList;
    }
}
