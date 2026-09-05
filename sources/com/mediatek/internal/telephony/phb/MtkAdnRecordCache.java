package com.mediatek.internal.telephony.phb;

import android.os.AsyncResult;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.util.SparseArray;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.AdnRecordCache;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.uicc.MtkUiccCardApplication;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class MtkAdnRecordCache extends AdnRecordCache {
    private static final int ADN_FILE_SIZE = 250;
    private static final boolean DBG;
    private static final String LOG_TAG = "MtkAdnRecordCache";
    public static final int MAX_PHB_NAME_LENGTH = 60;
    public static final int MAX_PHB_NUMBER_ANR_COUNT = 1;
    public static final int MAX_PHB_NUMBER_ANR_LENGTH = 20;
    public static final int MAX_PHB_NUMBER_LENGTH = 40;
    private static final String PROP_FORCE_DEBUG_KEY = "persist.vendor.log.tel_dbg";
    private CommandsInterface mCi;
    private UiccCardApplication mCurrentApp;
    private final Object mLock;
    protected SparseArray<ArrayList<MtkAdnRecord>> mMtkAdnLikeFiles;
    private boolean mNeedToWait;
    private int mSlotId;
    private boolean mSuccess;
    private MtkUsimPhoneBookManager mUsimPhoneBookManager;

    static {
        boolean z = false;
        if (SystemProperties.getInt(PROP_FORCE_DEBUG_KEY, 0) == 1 && !SystemProperties.get("ro.build.type").equals(DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER)) {
            z = true;
        }
        DBG = z;
    }

    public MtkAdnRecordCache(IccFileHandler fh, CommandsInterface ci, UiccCardApplication app) {
        super(fh);
        this.mSlotId = -1;
        this.mMtkAdnLikeFiles = new SparseArray<>();
        this.mLock = new Object();
        this.mSuccess = false;
        this.mNeedToWait = false;
        this.mCi = ci;
        this.mCurrentApp = app;
        this.mUsimPhoneBookManager = new MtkUsimPhoneBookManager(this.mFh, this, ci, app);
        if (app != null) {
            this.mSlotId = ((MtkUiccCardApplication) app).getPhoneId();
        }
    }

    public void reset() {
        logi("reset");
        Thread.dumpStack();
        this.mMtkAdnLikeFiles.clear();
        this.mUsimPhoneBookManager.reset();
        synchronized (this.mAdnLikeWaiters) {
            clearWaiters();
        }
        clearUserWriters();
        if (!CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
            CsimPhbUtil.clearAdnRecordSize();
        }
    }

    public int getSlotId() {
        return this.mSlotId;
    }

    private void clearUserWriters() {
        logi("clearUserWriters");
        synchronized (this.mLock) {
            logi("mNeedToWait " + this.mNeedToWait);
            if (this.mNeedToWait) {
                this.mNeedToWait = false;
                this.mLock.notifyAll();
            }
        }
        int size = this.mUserWriteResponse.size();
        for (int i = 0; i < size; i++) {
            sendErrorResponse((Message) this.mUserWriteResponse.valueAt(i), "AdnCace reset " + this.mUserWriteResponse.valueAt(i));
        }
        this.mUserWriteResponse.clear();
    }

    private void sendErrorResponse(Message response, String errString) {
        sendErrorResponse(response, errString, 2);
    }

    private void sendErrorResponse(Message response, String errString, int ril_errno) {
        CommandException e = CommandException.fromRilErrno(ril_errno);
        if (response != null) {
            logw(errString);
            AsyncResult.forMessage(response).exception = e;
            response.sendToTarget();
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(3:(3:210|106|219)|197|104) */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x030f, code lost:
    
        r0 = th;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void updateAdnByIndex(int r24, com.mediatek.internal.telephony.phb.MtkAdnRecord r25, int r26, java.lang.String r27, android.os.Message r28) {
        /*
            Method dump skipped, instruction units count: 813
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.phb.MtkAdnRecordCache.updateAdnByIndex(int, com.mediatek.internal.telephony.phb.MtkAdnRecord, int, java.lang.String, android.os.Message):void");
    }

    public synchronized int updateAdnBySearch(int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn, String pin2, Message response, Object object) {
        ArrayList<MtkAdnRecord> oldAdnList;
        int efid2;
        int index;
        int extensionEF;
        Object obj;
        logd("updateAdnBySearch efid 0x:" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + ", oldAdn [" + oldAdn + "], new Adn[" + newAdn + "]");
        int index2 = -1;
        String anr = null;
        int extensionEF2 = extensionEfForEf(efid);
        if (extensionEF2 < 0) {
            sendErrorResponse(response, "EF is not known ADN-like EF:0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
            return -1;
        }
        if (newAdn.mAlphaTag == null) {
            newAdn.mAlphaTag = "";
        }
        if (newAdn.mAlphaTag.length() > 60) {
            sendErrorResponse(response, "the input length of mAlphaTag is too long: " + newAdn.mAlphaTag, 502);
            return -1;
        }
        if (newAdn.mNumber == null) {
            newAdn.mNumber = "";
        }
        int numLength = newAdn.mNumber.length();
        if (newAdn.mNumber.indexOf(43) != -1) {
            numLength--;
        }
        if (numLength > 40) {
            sendErrorResponse(response, "the input length of phoneNumber is too long: " + newAdn.mNumber, 501);
            return -1;
        }
        for (int i = 0; i < 1; i++) {
            anr = newAdn.getAdditionalNumber(i);
            if (anr != null) {
                int numLength2 = anr.length();
                if (anr.indexOf(43) != -1) {
                    numLength2--;
                }
                if (numLength2 > 20) {
                    sendErrorResponse(response, "the input length of additional number is too long: " + anr, 505);
                    return -1;
                }
            }
        }
        if (!this.mUsimPhoneBookManager.checkEmailLength(newAdn.mEmails)) {
            sendErrorResponse(response, "the email string is too long", 509);
            return -1;
        }
        if (efid == 20272) {
            oldAdnList = this.mUsimPhoneBookManager.loadEfFilesFromUsim(null);
        } else {
            oldAdnList = getRecordsIfLoaded(efid, null);
        }
        if (oldAdnList == null) {
            sendErrorResponse(response, "Adn list not exist for EF:" + efid, 507);
            return -1;
        }
        int count = 1;
        Iterator<MtkAdnRecord> it = oldAdnList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (oldAdn.isEqual(it.next())) {
                index2 = count;
                break;
            }
            count++;
        }
        logi("updateAdnBySearch index " + index2);
        if (index2 == -1) {
            if (oldAdn.mAlphaTag.length() == 0 && oldAdn.mNumber.length() == 0) {
                sendErrorResponse(response, "Adn record don't exist for " + oldAdn, 503);
            } else {
                sendErrorResponse(response, "Adn record don't exist for " + oldAdn);
            }
            return index2;
        }
        MtkAdnRecord foundAdn = null;
        if (efid != 20272) {
            efid2 = efid;
            index = index2;
            extensionEF = extensionEF2;
        } else {
            foundAdn = oldAdnList.get(index2 - 1);
            int efid3 = foundAdn.mEfid;
            int extensionEF3 = foundAdn.mExtRecord;
            int index3 = foundAdn.mRecordNumber;
            newAdn.mEfid = efid3;
            newAdn.mExtRecord = extensionEF3;
            newAdn.mRecordNumber = index3;
            efid2 = efid3;
            index = index3;
            extensionEF = extensionEF3;
        }
        Message pendingResponse = (Message) this.mUserWriteResponse.get(efid2);
        if (pendingResponse != null) {
            sendErrorResponse(response, "Have pending update for EF:0x" + Integer.toHexString(efid2).toUpperCase(Locale.ROOT));
            return index;
        }
        if (efid2 == 0) {
            sendErrorResponse(response, "Abnormal efid: " + efid2);
            return index;
        }
        if (!this.mUsimPhoneBookManager.checkEmailCapacityFree(index, newAdn.mEmails, foundAdn)) {
            sendErrorResponse(response, "drop the email for the limitation of the SIM card", 508);
            return index;
        }
        for (int i2 = 0; i2 < 1; i2++) {
            String anr2 = newAdn.getAdditionalNumber(i2);
            if (!this.mUsimPhoneBookManager.isAnrCapacityFree(anr2, index, i2, foundAdn)) {
                sendErrorResponse(response, "drop the additional number for the write fail: " + anr2, 506);
                return index;
            }
        }
        if (!this.mUsimPhoneBookManager.checkSneCapacityFree(index, newAdn.mSne, foundAdn)) {
            sendErrorResponse(response, "drop the sne for the limitation of the SIM card", 510);
            return index;
        }
        this.mUserWriteResponse.put(efid2, response);
        Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                this.mSuccess = false;
                this.mNeedToWait = true;
                obj = obj2;
                int i3 = efid2;
                MtkAdnRecord foundAdn2 = foundAdn;
                try {
                    new MtkAdnRecordLoader(this.mFh).updateEF(newAdn, i3, extensionEF, index, pin2, obtainMessage(2, efid2, index, newAdn));
                    while (this.mNeedToWait) {
                        try {
                            try {
                                this.mLock.wait();
                            } catch (InterruptedException e) {
                                return index;
                            }
                        } catch (Throwable th) {
                            e = th;
                            throw e;
                        }
                    }
                    if (!this.mSuccess) {
                        loge("updateAdnBySearch mSuccess:" + this.mSuccess);
                        return index;
                    }
                    if (efid2 == 28474 || efid2 == 20272 || efid2 == 20282 || efid2 == 20283 || efid2 == 20284 || efid2 == 20285) {
                        int mResult = this.mUsimPhoneBookManager.updateSneByAdnIndex(newAdn.mSne, index, foundAdn2);
                        if (-30 == mResult) {
                            sendErrorResponse(response, "drop the SNE for the limitation of the SIM card", 510);
                        } else if (-40 == mResult) {
                            sendErrorResponse(response, "the sne string is too long", 511);
                        } else {
                            for (int i4 = 0; i4 < 1; i4++) {
                                String anr3 = newAdn.getAdditionalNumber(i4);
                                this.mUsimPhoneBookManager.updateAnrByAdnIndex(anr3, index, i4, foundAdn2);
                            }
                            int success = this.mUsimPhoneBookManager.updateEmailsByAdnIndex(newAdn.mEmails, index, foundAdn2);
                            if (-30 == success) {
                                sendErrorResponse(response, "drop the email for the limitation of the SIM card", 508);
                            } else if (-40 == success) {
                                sendErrorResponse(response, "the email string is too long", 509);
                            } else if (-50 == success) {
                                sendErrorResponse(response, "Unkown error occurs when update email", 2);
                            } else {
                                logd("updateAdnBySearch response:" + response);
                                AsyncResult.forMessage(response, (Object) null, (Throwable) null);
                                response.sendToTarget();
                            }
                        }
                    } else if (efid2 == 28475) {
                        logd("updateAdnBySearch FDN response:" + response);
                        AsyncResult.forMessage(response, (Object) null, (Throwable) null);
                        response.sendToTarget();
                    }
                    return index;
                } catch (Throwable th2) {
                    e = th2;
                    throw e;
                }
            } catch (Throwable th3) {
                e = th3;
                obj = obj2;
            }
        }
    }

    public void requestLoadAllAdnLike(int efid, int extensionEf, Message response) {
        ArrayList<MtkAdnRecord> result;
        logd("requestLoadAllAdnLike efid = 0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + ", extensionEf = " + extensionEf);
        if (efid == 20272) {
            result = this.mUsimPhoneBookManager.loadEfFilesFromUsim(null);
        } else {
            result = getRecordsIfLoaded(efid, null);
        }
        logi("requestLoadAllAdnLike efid =  0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + ", result = null ? " + (result == null));
        if (result != null) {
            if (response != null) {
                AsyncResult.forMessage(response).result = result;
                response.sendToTarget();
                return;
            }
            return;
        }
        if (result == null && efid == 20272) {
            sendErrorResponse(response, "Error occurs when query PBR", 2);
            return;
        }
        synchronized (this.mAdnLikeWaiters) {
            ArrayList<Message> waiters = (ArrayList) this.mAdnLikeWaiters.get(efid);
            if (waiters != null) {
                waiters.add(response);
                return;
            }
            ArrayList<Message> waiters2 = new ArrayList<>();
            waiters2.add(response);
            this.mAdnLikeWaiters.put(efid, waiters2);
            if (extensionEf < 0) {
                if (response != null) {
                    AsyncResult.forMessage(response).exception = new RuntimeException("EF is not known ADN-like EF:0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
                    response.sendToTarget();
                    return;
                }
                return;
            }
            new MtkAdnRecordLoader(this.mFh).loadAllFromEF(efid, extensionEf, obtainMessage(1, efid, 0));
        }
    }

    protected void notifyWaiters(ArrayList<Message> waiters, AsyncResult ar) {
        if (waiters == null) {
            return;
        }
        int s = waiters.size();
        for (int i = 0; i < s; i++) {
            Message waiter = waiters.get(i);
            if (waiter != null) {
                logi("NotifyWaiters: " + waiter);
                AsyncResult.forMessage(waiter, ar.result, ar.exception);
                waiter.sendToTarget();
            }
        }
    }

    public ArrayList<MtkAdnRecord> getRecordsIfLoaded(int efid, Object object) {
        return this.mMtkAdnLikeFiles.get(efid);
    }

    public ArrayList<AdnRecord> getRecordsIfLoaded(int efid) {
        ArrayList<MtkAdnRecord> mtkAdnList = this.mMtkAdnLikeFiles.get(efid);
        ArrayList<AdnRecord> result = new ArrayList<>();
        if (mtkAdnList == null) {
            loge("getRecordsIfLoaded, mtkAdnList is null, efid: 0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT));
            return null;
        }
        logd("getRecordsIfLoaded, convert mtkAdnList, efid: 0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + "record number:" + mtkAdnList.size());
        for (MtkAdnRecord rcd : mtkAdnList) {
            int index = mtkAdnList.indexOf(rcd);
            if (index == 0) {
                logd("getRecordsIfLoaded: index " + index + ", adn:" + rcd);
            }
            if (index >= 0) {
                result.add(index, rcd);
            }
        }
        return result;
    }

    public void handleMessage(Message msg) {
        ArrayList<Message> waiters;
        switch (msg.what) {
            case 1:
                AsyncResult ar = (AsyncResult) msg.obj;
                int efid = msg.arg1;
                synchronized (this.mAdnLikeWaiters) {
                    waiters = (ArrayList) this.mAdnLikeWaiters.get(efid);
                    this.mAdnLikeWaiters.delete(efid);
                    break;
                }
                if (ar.exception == null) {
                    logd("EVENT_LOAD_ALL_ADN_LIKE_DONE, put in ADN File, efid: 0x" + Integer.toHexString(efid).toUpperCase(Locale.ROOT) + ", mSlotId:" + this.mSlotId);
                    this.mMtkAdnLikeFiles.put(efid, (ArrayList) ar.result);
                } else {
                    Rlog.w(LOG_TAG, "EVENT_LOAD_ALL_ADN_LIKE_DONE exception(slot " + this.mSlotId + ")", ar.exception);
                }
                notifyWaiters(waiters, ar);
                return;
            case 2:
                logd("EVENT_UPDATE_ADN_DONE");
                synchronized (this.mLock) {
                    if (this.mNeedToWait) {
                        AsyncResult ar2 = (AsyncResult) msg.obj;
                        int efid2 = msg.arg1;
                        int index = msg.arg2;
                        MtkAdnRecord adn = (MtkAdnRecord) ar2.userObj;
                        if (ar2.exception == null && adn != null) {
                            adn.setRecordIndex(index);
                            if (adn.mEfid <= 0) {
                                adn.mEfid = efid2;
                            }
                            logd("mMtkAdnLikeFiles changed index:" + index + ",adn:" + adn + "efid: 0x" + Integer.toHexString(efid2).toUpperCase(Locale.ROOT));
                            SparseArray<ArrayList<MtkAdnRecord>> sparseArray = this.mMtkAdnLikeFiles;
                            if (sparseArray != null && sparseArray.get(efid2) != null) {
                                if (efid2 == 20283 && !CsimPhbUtil.hasModemPhbEnhanceCapability(this.mFh)) {
                                    index -= 250;
                                }
                                this.mMtkAdnLikeFiles.get(efid2).set(index - 1, adn);
                                logd(" index:" + index + "   efid: 0x" + Integer.toHexString(efid2).toUpperCase(Locale.ROOT));
                            }
                            if (this.mUsimPhoneBookManager != null && efid2 != 28475) {
                                if (efid2 == 20283) {
                                    index += ADN_FILE_SIZE;
                                    logd(" index2:" + index);
                                }
                                this.mUsimPhoneBookManager.updateUsimPhonebookRecordsList(index - 1, adn);
                            }
                        }
                        Message response = (Message) this.mUserWriteResponse.get(efid2);
                        this.mUserWriteResponse.delete(efid2);
                        logi("MtkAdnRecordCache: " + ar2.exception);
                        if (ar2.exception != null && response != null) {
                            AsyncResult.forMessage(response, (Object) null, ar2.exception);
                            response.sendToTarget();
                        }
                        this.mSuccess = ar2.exception == null;
                        this.mNeedToWait = false;
                        this.mLock.notifyAll();
                    }
                    break;
                }
                return;
            default:
                return;
        }
    }

    protected void logd(String msg) {
        if (DBG) {
            Rlog.d(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
        }
    }

    protected void loge(String msg) {
        Rlog.e(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    protected void logi(String msg) {
        Rlog.i(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    protected void logw(String msg) {
        Rlog.w(LOG_TAG, msg + "(slot " + this.mSlotId + ")");
    }

    public List<UsimGroup> getUsimGroups() {
        return this.mUsimPhoneBookManager.getUsimGroups();
    }

    public String getUsimGroupById(int nGasId) {
        return this.mUsimPhoneBookManager.getUsimGroupById(nGasId);
    }

    public boolean removeUsimGroupById(int nGasId) {
        return this.mUsimPhoneBookManager.removeUsimGroupById(nGasId);
    }

    public int insertUsimGroup(String grpName) {
        return this.mUsimPhoneBookManager.insertUsimGroup(grpName);
    }

    public int updateUsimGroup(int nGasId, String grpName) {
        return this.mUsimPhoneBookManager.updateUsimGroup(nGasId, grpName);
    }

    public boolean addContactToGroup(int adnIndex, int grpIndex) {
        return this.mUsimPhoneBookManager.addContactToGroup(adnIndex, grpIndex);
    }

    public boolean removeContactFromGroup(int adnIndex, int grpIndex) {
        return this.mUsimPhoneBookManager.removeContactFromGroup(adnIndex, grpIndex);
    }

    public boolean updateContactToGroups(int adnIndex, int[] grpIdList) {
        return this.mUsimPhoneBookManager.updateContactToGroups(adnIndex, grpIdList);
    }

    public boolean moveContactFromGroupsToGroups(int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) {
        return this.mUsimPhoneBookManager.moveContactFromGroupsToGroups(adnIndex, fromGrpIdList, toGrpIdList);
    }

    public int hasExistGroup(String grpName) {
        return this.mUsimPhoneBookManager.hasExistGroup(grpName);
    }

    public int getUsimGrpMaxNameLen() {
        return this.mUsimPhoneBookManager.getUsimGrpMaxNameLen();
    }

    public int getUsimGrpMaxCount() {
        return this.mUsimPhoneBookManager.getUsimGrpMaxCount();
    }

    private void dumpAdnLikeFile() {
        int size = this.mMtkAdnLikeFiles.size();
        logd("dumpAdnLikeFile size " + size);
        for (int i = 0; i < size; i++) {
            int key = this.mMtkAdnLikeFiles.keyAt(i);
            ArrayList<MtkAdnRecord> records = this.mMtkAdnLikeFiles.get(key);
            logd("dumpAdnLikeFile index " + i + " key " + key + "records size " + records.size());
            for (int j = 0; j < records.size(); j++) {
                MtkAdnRecord record = records.get(j);
                logd("mMtkAdnLikeFiles[" + j + "]=" + record);
            }
        }
    }

    public ArrayList<AlphaTag> getUsimAasList() {
        return this.mUsimPhoneBookManager.getUsimAasList();
    }

    public String getUsimAasById(int index) {
        return this.mUsimPhoneBookManager.getUsimAasById(index, 0);
    }

    public boolean removeUsimAasById(int index, int pbrIndex) {
        return this.mUsimPhoneBookManager.removeUsimAasById(index, pbrIndex);
    }

    public int insertUsimAas(String aasName) {
        return this.mUsimPhoneBookManager.insertUsimAas(aasName);
    }

    public boolean updateUsimAas(int index, int pbrIndex, String aasName) {
        return this.mUsimPhoneBookManager.updateUsimAas(index, pbrIndex, aasName);
    }

    public boolean updateAdnAas(int adnIndex, int aasIndex) {
        return this.mUsimPhoneBookManager.updateAdnAas(adnIndex, aasIndex);
    }

    public int getAnrCount() {
        return this.mUsimPhoneBookManager.getAnrCount();
    }

    public int getEmailCount() {
        return this.mUsimPhoneBookManager.getEmailCount();
    }

    public int getUsimAasMaxCount() {
        return this.mUsimPhoneBookManager.getUsimAasMaxCount();
    }

    public int getUsimAasMaxNameLen() {
        return this.mUsimPhoneBookManager.getUsimAasMaxNameLen();
    }

    public boolean hasSne() {
        return this.mUsimPhoneBookManager.hasSne();
    }

    public int getSneRecordLen() {
        return this.mUsimPhoneBookManager.getSneRecordLen();
    }

    public boolean isAdnAccessible() {
        return this.mUsimPhoneBookManager.isAdnAccessible();
    }

    public boolean isUsimPhbEfAndNeedReset(int fileId) {
        return this.mUsimPhoneBookManager.isUsimPhbEfAndNeedReset(fileId);
    }

    public UsimPBMemInfo[] getPhonebookMemStorageExt() {
        return this.mUsimPhoneBookManager.getPhonebookMemStorageExt();
    }

    public int getUpbDone() {
        return this.mUsimPhoneBookManager.getUpbDone();
    }

    public int[] getAdnRecordsCapacityExt() {
        return this.mUsimPhoneBookManager.getAdnRecordsCapacityExt();
    }
}
