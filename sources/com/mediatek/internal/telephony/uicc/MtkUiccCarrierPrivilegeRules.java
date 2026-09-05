package com.mediatek.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Message;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.uicc.UiccCarrierPrivilegeRules;
import com.android.internal.telephony.uicc.UiccProfile;

/* JADX INFO: loaded from: classes.dex */
public class MtkUiccCarrierPrivilegeRules extends UiccCarrierPrivilegeRules {
    public MtkUiccCarrierPrivilegeRules(UiccProfile uiccProfile, Message loadedCallback) {
        super(uiccProfile, loadedCallback);
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                log("M: EVENT_OPEN_LOGICAL_CHANNEL_DONE");
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.exception != null || ar.result == null) {
                    if ((ar.exception instanceof CommandException) && ar.exception.getCommandError() == CommandException.Error.RADIO_NOT_AVAILABLE) {
                        updateState(2, "RADIO_NOT_AVAILABLE");
                    } else {
                        super.handleMessage(msg);
                    }
                } else {
                    super.handleMessage(msg);
                }
                break;
            default:
                log("Handled by AOSP handleMessage" + msg.what);
                super.handleMessage(msg);
                break;
        }
    }
}
