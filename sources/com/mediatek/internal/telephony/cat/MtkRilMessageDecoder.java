package com.mediatek.internal.telephony.cat;

import android.content.Context;
import android.os.Handler;
import com.android.internal.telephony.cat.BerTlv;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.cat.RilMessage;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;

/* JADX INFO: loaded from: classes.dex */
public class MtkRilMessageDecoder extends RilMessageDecoder {
    private int mSlotId;

    public MtkRilMessageDecoder(Handler caller, IccFileHandler fh, Context context, int slotId) {
        super(caller, fh, context);
        this.mSlotId = slotId;
        MtkCatLog.d(this, "mCaller is " + this.mCaller.getClass().getName());
    }

    public MtkRilMessageDecoder() {
    }

    public boolean decodeMessageParams(RilMessage rilMsg) {
        MtkCatLog.d(this, "decodeMessageParams");
        this.mCurrentRilMessage = rilMsg;
        switch (rilMsg.mId) {
            case 1:
            case 4:
                this.mCurrentRilMessage.mResCode = ResultCode.OK;
                sendCmdForExecution(this.mCurrentRilMessage);
                break;
            case 2:
            case 3:
            case 5:
                try {
                    byte[] rawData = IccUtils.hexStringToBytes((String) rilMsg.mData);
                    try {
                        if (this.mCmdParamsFactory != null) {
                            this.mCmdParamsFactory.make(BerTlv.decode(rawData));
                        }
                    } catch (ResultException e) {
                        MtkCatLog.d(this, "decodeMessageParams: caught ResultException e=" + e);
                        this.mCurrentRilMessage.mResCode = e.result();
                        sendCmdForExecution(this.mCurrentRilMessage);
                        this.mCurrentRilMessage.mId = 1;
                        sendCmdForExecution(this.mCurrentRilMessage);
                        return false;
                    }
                } catch (Exception e2) {
                    MtkCatLog.d(this, "decodeMessageParams dropping zombie messages");
                    return false;
                }
                break;
        }
        return false;
    }

    public void dispose() {
        quitNow();
        this.mStateStart = null;
        this.mStateCmdParamsReady = null;
        this.mCmdParamsFactory.dispose();
        this.mCmdParamsFactory = null;
        this.mCurrentRilMessage = null;
        this.mCaller = null;
        if (mInstance != null) {
            if (mInstance[this.mSlotId] != null) {
                mInstance[this.mSlotId] = null;
            }
            int i = 0;
            while (i < mSimCount && mInstance[i] == null) {
                i++;
            }
            if (i == mSimCount) {
                mInstance = null;
            }
        }
    }
}
