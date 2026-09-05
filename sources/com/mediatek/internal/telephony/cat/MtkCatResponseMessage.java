package com.mediatek.internal.telephony.cat;

import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatResponseMessage;

/* JADX INFO: loaded from: classes.dex */
public class MtkCatResponseMessage extends CatResponseMessage {
    byte[] mAdditionalInfo;
    int mDestinationId;
    int mEvent;
    boolean mOneShot;
    int mSourceId;

    public MtkCatResponseMessage(CatCmdMessage cmdMsg) {
        super(cmdMsg);
        this.mEvent = 0;
        this.mSourceId = 0;
        this.mDestinationId = 0;
        this.mAdditionalInfo = null;
        this.mOneShot = false;
    }

    public MtkCatResponseMessage(CatCmdMessage cmdMsg, int event) {
        super(cmdMsg);
        this.mEvent = 0;
        this.mSourceId = 0;
        this.mDestinationId = 0;
        this.mAdditionalInfo = null;
        this.mOneShot = false;
        this.mEvent = event;
    }

    public MtkCatResponseMessage(CatCmdMessage cmdMsg, CatResponseMessage rspMsg) {
        super(cmdMsg);
        this.mEvent = 0;
        this.mSourceId = 0;
        this.mDestinationId = 0;
        this.mAdditionalInfo = null;
        this.mOneShot = false;
        this.mCmdDet = rspMsg.mCmdDet;
        this.mResCode = rspMsg.mResCode;
        this.mUsersMenuSelection = rspMsg.mUsersMenuSelection;
        this.mUsersInput = rspMsg.mUsersInput;
        this.mUsersYesNoSelection = rspMsg.mUsersYesNoSelection;
        this.mUsersConfirm = rspMsg.mUsersConfirm;
        this.mIncludeAdditionalInfo = rspMsg.mIncludeAdditionalInfo;
        this.mEventValue = rspMsg.mEventValue;
        this.mAddedInfo = rspMsg.mAddedInfo;
    }

    public void setSourceId(int sId) {
        this.mSourceId = sId;
    }

    public void setEventId(int event) {
        this.mEvent = event;
    }

    public void setDestinationId(int dId) {
        this.mDestinationId = dId;
    }

    public void setAdditionalInfo(byte[] additionalInfo) {
        if (additionalInfo != null) {
            this.mIncludeAdditionalInfo = true;
        }
        this.mAdditionalInfo = additionalInfo;
        if (additionalInfo != null && additionalInfo.length > 0) {
            ((CatResponseMessage) this).mAdditionalInfo = additionalInfo[0];
        }
    }

    public void setOneShot(boolean shot) {
        this.mOneShot = shot;
    }
}
