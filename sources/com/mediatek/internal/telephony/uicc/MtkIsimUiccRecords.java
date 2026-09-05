package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.os.AsyncResult;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.IsimUiccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.mediatek.internal.telephony.uicc.IsimServiceTable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class MtkIsimUiccRecords extends IsimUiccRecords {
    private static final int EVENT_GET_GBABP_DONE = 200;
    private static final int EVENT_GET_GBANL_DONE = 201;
    protected static final String LOG_TAG = "MtkIsimUiccRecords";
    ArrayList<byte[]> mEfGbanlList;
    private String mIsimGbabp;
    IsimServiceTable mIsimServiceTable;
    private int mSlotId;

    static /* synthetic */ int access$208(MtkIsimUiccRecords x0) {
        int i = x0.mRecordsToLoad;
        x0.mRecordsToLoad = i + 1;
        return i;
    }

    public MtkIsimUiccRecords(UiccCardApplication app, Context c, CommandsInterface ci) {
        super(app, c, ci);
        log("MtkIsimUiccRecords X ctor this=" + this);
        this.mSlotId = ((MtkUiccCardApplication) app).getPhoneId();
    }

    protected void fetchIsimRecords() {
        this.mRecordsRequested = true;
        this.mFh.loadEFTransparent(28418, obtainMessage(100, new IsimUiccRecords.EfIsimImpiLoaded(this)));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixedAll(28420, obtainMessage(100, new IsimUiccRecords.EfIsimImpuLoaded(this)));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(28419, obtainMessage(100, new IsimUiccRecords.EfIsimDomainLoaded(this)));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(MtkIccConstants.EF_IMSI, obtainMessage(100, new MtkEfIsimIstLoaded()));
        this.mRecordsToLoad++;
        this.mFh.loadEFTransparent(28483, obtainMessage(100, new IsimUiccRecords.EfIsimSmssLoaded(this)));
        this.mRecordsToLoad++;
        this.mFh.loadEFLinearFixed(28645, 1, obtainMessage(100, new IsimUiccRecords.EfIsimPsiSmscLoaded(this)));
        this.mRecordsToLoad++;
        log("fetchIsimRecords " + this.mRecordsToLoad + " requested: " + this.mRecordsRequested);
    }

    private class MtkEfIsimIstLoaded implements IccRecords.IccRecordLoaded {
        private MtkEfIsimIstLoaded() {
        }

        public String getEfName() {
            return "EF_ISIM_IST";
        }

        public void onRecordLoaded(AsyncResult ar) {
            byte[] data = (byte[]) ar.result;
            MtkIsimUiccRecords.this.mIsimIst = IccUtils.bytesToHexString(data);
            MtkIsimUiccRecords.this.mIsimServiceTable = new IsimServiceTable(data);
            MtkIsimUiccRecords.this.log("IST: " + MtkIsimUiccRecords.this.mIsimServiceTable);
            if (MtkIsimUiccRecords.this.mIsimServiceTable.isAvailable(IsimServiceTable.IsimService.PCSCF_ADDRESS) || MtkIsimUiccRecords.this.mIsimServiceTable.isAvailable(IsimServiceTable.IsimService.PCSCF_DISCOVERY)) {
                MtkIsimUiccRecords.this.mFh.loadEFLinearFixedAll(28425, MtkIsimUiccRecords.this.obtainMessage(100, new IsimUiccRecords.EfIsimPcscfLoaded(MtkIsimUiccRecords.this)));
                MtkIsimUiccRecords.access$208(MtkIsimUiccRecords.this);
            }
        }
    }

    protected void log(String s) {
        Rlog.d(LOG_TAG, "[ISIM] " + s + " (slot " + this.mSlotId + ")");
    }

    protected void loge(String s) {
        Rlog.e(LOG_TAG, "[ISIM] " + s + " (slot " + this.mSlotId + ")");
    }
}
