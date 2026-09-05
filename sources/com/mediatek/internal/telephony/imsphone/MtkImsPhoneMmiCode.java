package com.mediatek.internal.telephony.imsphone;

import android.R;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.android.ims.ImsException;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneMmiCode;
import com.android.internal.util.ArrayUtils;
import com.mediatek.internal.telephony.MtkSuppServHelper;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import java.util.regex.Matcher;

/* JADX INFO: loaded from: classes.dex */
public final class MtkImsPhoneMmiCode extends ImsPhoneMmiCode {
    private static final int DIALOG_DISPLAY_TIME = 1000;
    static final String LOG_TAG = "MtkImsPhoneMmiCode";
    private static final String SC_CFNotRegister = "68";
    private static final boolean SENLOG = TextUtils.equals(Build.TYPE, DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    private long mProcessTime;

    public static MtkImsPhoneMmiCode newFromDialString(String dialString, ImsPhone phone) {
        return newFromDialString(dialString, phone, null);
    }

    public static MtkImsPhoneMmiCode newFromDialString(String dialString, ImsPhone phone, ResultReceiver wrappedCallback) {
        Rlog.d(LOG_TAG, "newFromDialString, dialstring = " + MtkSuppServHelper.encryptString(dialString));
        boolean isMmi = dialString.startsWith("*") || dialString.startsWith("#") || dialString.endsWith("#");
        if (!isMmi && dialString.length() > 2) {
            Rlog.d(LOG_TAG, "Not belong to MMI format.");
            return null;
        }
        if (phone.getDefaultPhone().getServiceState().getVoiceRoaming() && phone.getDefaultPhone().supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) {
            dialString = convertCdmaMmiCodesTo3gppMmiCodes(dialString);
        }
        Matcher m = sPatternSuppService.matcher(dialString);
        if (m.matches()) {
            MtkImsPhoneMmiCode ret = new MtkImsPhoneMmiCode(phone);
            ret.mPoundString = makeEmptyNull(m.group(1));
            ret.mAction = makeEmptyNull(m.group(2));
            ret.mSc = makeEmptyNull(m.group(3));
            ret.mSia = makeEmptyNull(m.group(5));
            ret.mSib = makeEmptyNull(m.group(7));
            ret.mSic = makeEmptyNull(m.group(9));
            ret.mPwd = makeEmptyNull(m.group(11));
            ret.mDialingNumber = makeEmptyNull(m.group(12));
            ret.mCallbackReceiver = wrappedCallback;
            if (ret.mDialingNumber != null && ret.mDialingNumber.endsWith("#") && dialString.endsWith("#")) {
                MtkImsPhoneMmiCode ret2 = new MtkImsPhoneMmiCode(phone);
                ret2.mPoundString = dialString;
                return ret2;
            }
            if (ret.isFacToDial()) {
                return null;
            }
            return ret;
        }
        if (dialString.endsWith("#")) {
            MtkImsPhoneMmiCode ret3 = new MtkImsPhoneMmiCode(phone);
            ret3.mPoundString = dialString;
            return ret3;
        }
        if (GsmMmiCode.isTwoDigitShortCode(phone.getContext(), phone.getSubId(), dialString) || !isShortCode(dialString, phone)) {
            return null;
        }
        MtkImsPhoneMmiCode ret4 = new MtkImsPhoneMmiCode(phone);
        ret4.mDialingNumber = dialString;
        return ret4;
    }

    public static MtkImsPhoneMmiCode newNetworkInitiatedUssd(String ussdMessage, boolean isUssdRequest, MtkImsPhone phone) {
        MtkImsPhoneMmiCode ret = new MtkImsPhoneMmiCode(phone);
        ret.mMessage = ussdMessage;
        ret.mIsUssdRequest = isUssdRequest;
        ret.mIsNetworkInitiatedUSSD = true;
        if (isUssdRequest) {
            ret.mIsPendingUSSD = true;
            ret.mState = MmiCode.State.PENDING;
        } else {
            ret.mState = MmiCode.State.COMPLETE;
        }
        return ret;
    }

    public static MtkImsPhoneMmiCode newFromUssdUserInput(String ussdMessge, MtkImsPhone phone) {
        MtkImsPhoneMmiCode ret = new MtkImsPhoneMmiCode(phone);
        ret.mMessage = ussdMessge;
        ret.mState = MmiCode.State.PENDING;
        ret.mIsPendingUSSD = true;
        return ret;
    }

    private static int siToServiceClass(String si) {
        if (si == null || si.length() == 0) {
            return 0;
        }
        int serviceCode = Integer.parseInt(si, 10);
        switch (serviceCode) {
            case 10:
                return 13;
            case 11:
                return 1;
            case 12:
                return 12;
            case 13:
                return 4;
            case 16:
                return 8;
            case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
                return 5;
            case 20:
                return 48;
            case WorldMode.MD_WORLD_MODE_LFCTG /* 21 */:
                return 160;
            case 22:
                return 80;
            case 24:
                return 528;
            case 25:
                return 32;
            case 26:
                return 17;
            case 99:
                return 64;
            default:
                throw new RuntimeException("unsupported MMI service code " + si);
        }
    }

    public MtkImsPhoneMmiCode(ImsPhone phone) {
        super(phone);
        this.mProcessTime = 0L;
    }

    public boolean isSupportedOverImsPhone() {
        if (isShortCode()) {
            return true;
        }
        if (this.mDialingNumber != null) {
            return false;
        }
        if (this.mSc != null && this.mSc.equals("300")) {
            return false;
        }
        if (!isServiceCodeCallForwarding(this.mSc) && !isServiceCodeCallBarring(this.mSc) && ((this.mSc == null || !this.mSc.equals("43")) && ((this.mSc == null || !this.mSc.equals("31")) && ((this.mSc == null || !this.mSc.equals("30")) && ((this.mSc == null || !this.mSc.equals("77")) && ((this.mSc == null || !this.mSc.equals("76")) && ((this.mSc == null || !this.mSc.equals("156")) && (this.mSc == null || !this.mSc.equals("157"))))))))) {
            return !isPinPukCommand() && (this.mSc == null || !(this.mSc.equals("03") || this.mSc.equals("30") || this.mSc.equals("31"))) && this.mPoundString != null;
        }
        if (supportMdAutoSetupIms()) {
            try {
                int serviceClass = siToServiceClass(this.mSib);
                if ((serviceClass & 1) == 0 && (serviceClass & 512) == 0 && serviceClass != 0) {
                    return false;
                }
                Rlog.d(LOG_TAG, "isSupportedOverImsPhone(), return true!");
                return true;
            } catch (RuntimeException exc) {
                Rlog.d(LOG_TAG, "Invalid service class " + exc);
            }
        }
        return false;
    }

    public void processCode() throws CallStateException {
        try {
            this.mProcessTime = System.currentTimeMillis();
            if (isShortCode()) {
                Rlog.d(LOG_TAG, "processCode: isShortCode");
                Rlog.d(LOG_TAG, "processCode: Sending short code '" + this.mDialingNumber + "' over CS pipe.");
                throw new CallStateException("cs_fallback");
            }
            if (isServiceCodeCallForwarding(this.mSc)) {
                handleCallForward();
            } else if (isServiceCodeCallBarring(this.mSc)) {
                handleCallBarring();
            } else if (this.mSc != null && this.mSc.equals("31")) {
                handleCLIR();
            } else if (this.mSc != null && this.mSc.equals("30")) {
                handleCLIP();
            } else if (this.mSc != null && this.mSc.equals("76")) {
                handleCOLP();
            } else if (this.mSc != null && this.mSc.equals("77")) {
                handleCOLR();
            } else if (this.mSc != null && this.mSc.equals("156")) {
                handleCallBarringSpecificMT();
            } else if (this.mSc != null && this.mSc.equals("157")) {
                handleCallBarringACR();
            } else if (this.mSc != null && this.mSc.equals("43")) {
                handleCW();
            } else if (this.mPoundString != null) {
                if (this.mPhone.getDefaultPhone().getServiceStateTracker().mSS.getState() == 0) {
                    Rlog.i(LOG_TAG, "processCode: Sending ussd string '" + Rlog.pii(LOG_TAG, this.mPoundString) + "' over CS pipe.");
                    throw new CallStateException("cs_fallback");
                }
                Rlog.i(LOG_TAG, "processCode: CS is out of service, sending ussd string '" + Rlog.pii(LOG_TAG, this.mPoundString) + "' over IMS pipe.");
                sendUssd(this.mPoundString);
            } else {
                throw new RuntimeException("Invalid or Unsupported MMI Code");
            }
        } catch (RuntimeException exc) {
            exc.printStackTrace();
            Rlog.d(LOG_TAG, "procesCode: mState = FAILED");
            this.mState = MmiCode.State.FAILED;
            this.mMessage = this.mContext.getText(R.string.lockscreen_pattern_correct);
            checkProcessTime();
            this.mPhone.onMMIDone(this);
        }
    }

    void checkProcessTime() {
        long now = System.currentTimeMillis();
        if (now - this.mProcessTime < 1000) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
        }
    }

    void handleCallForward() {
        int cfAction;
        Rlog.d(LOG_TAG, "processCode: is CF");
        String dialingNumber = this.mSia;
        int reason = scToCallForwardReason(this.mSc);
        int serviceClass = siToServiceClass(this.mSib);
        int time = siToTime(this.mSic);
        int i = 1;
        if (isInterrogate()) {
            this.mPhone.getCallForwardingOption(reason, serviceClass, obtainMessage(1, this));
            return;
        }
        if (isActivate()) {
            if (isEmptyOrNull(dialingNumber)) {
                this.mIsCallFwdReg = false;
                cfAction = 1;
            } else {
                this.mIsCallFwdReg = true;
                cfAction = 3;
            }
        } else if (isDeactivate()) {
            cfAction = 0;
        } else if (isRegister()) {
            cfAction = 3;
        } else if (isErasure()) {
            cfAction = 4;
        } else {
            throw new RuntimeException("invalid action");
        }
        int isSettingUnconditional = ((reason == 0 || reason == 4) && ((serviceClass & 1) != 0 || serviceClass == 0)) ? 1 : 0;
        if (cfAction != 1 && cfAction != 3) {
            i = 0;
        }
        int isEnableDesired = i;
        Rlog.d(LOG_TAG, "handleCallForward: is CF setCallForward");
        this.mPhone.setCallForwardingOption(cfAction, reason, dialingNumber, serviceClass, time, obtainMessage(4, isSettingUnconditional, isEnableDesired, this));
    }

    void handleCallBarring() {
        Rlog.d(LOG_TAG, "processCode: is CB");
        String password = this.mSia;
        String facility = scToBarringFacility(this.mSc);
        int serviceClass = siToServiceClass(this.mSib);
        if (isInterrogate()) {
            this.mPhone.getCallBarring(facility, obtainMessage(7, this), serviceClass);
        } else {
            if (isActivate() || isDeactivate()) {
                this.mPhone.setCallBarring(facility, isActivate(), password, obtainMessage(0, this), serviceClass);
                return;
            }
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        }
    }

    void handleCLIR() {
        Rlog.d(LOG_TAG, "processCode: is CLIR");
        if (isActivate() && !this.mPhone.getDefaultPhone().isClirActivationAndDeactivationPrevented()) {
            try {
                this.mPhone.setOutgoingCallerIdDisplay(1, obtainMessage(0, this));
            } catch (Exception e) {
                Rlog.d(LOG_TAG, "Could not get UT handle for updateCLIR.");
            }
        } else if (isDeactivate() && !this.mPhone.getDefaultPhone().isClirActivationAndDeactivationPrevented()) {
            try {
                this.mPhone.setOutgoingCallerIdDisplay(2, obtainMessage(0, this));
            } catch (Exception e2) {
                Rlog.d(LOG_TAG, "Could not get UT handle for updateCLIR.");
            }
        } else {
            if (isInterrogate()) {
                try {
                    this.mPhone.getOutgoingCallerIdDisplay(obtainMessage(6, this));
                    return;
                } catch (Exception e3) {
                    Rlog.d(LOG_TAG, "Could not get UT handle for queryCLIR.");
                    return;
                }
            }
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        }
    }

    void handleCLIP() {
        Rlog.d(LOG_TAG, "processCode: is CLIP");
        if (isInterrogate()) {
            try {
                this.mPhone.queryCLIP(obtainMessage(7, this));
            } catch (Exception e) {
                Rlog.d(LOG_TAG, "Could not get UT handle for queryCLIP.");
            }
        } else {
            if (isActivate() || isDeactivate()) {
                try {
                    this.mPhone.getCallTracker().getUtInterface().updateCLIP(isActivate(), obtainMessage(0, this));
                    return;
                } catch (ImsException e2) {
                    Rlog.d(LOG_TAG, "Could not get UT handle for updateCLIP.");
                    return;
                }
            }
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        }
    }

    void handleCOLP() {
        Rlog.d(LOG_TAG, "processCode: is COLP");
        if (isInterrogate()) {
            try {
                this.mPhone.getCallTracker().getUtInterface().queryCOLP(obtainMessage(7, this));
            } catch (ImsException e) {
                Rlog.d(LOG_TAG, "Could not get UT handle for queryCOLP.");
            }
        } else {
            if (isActivate() || isDeactivate()) {
                try {
                    this.mPhone.getCallTracker().getUtInterface().updateCOLP(isActivate(), obtainMessage(0, this));
                    return;
                } catch (ImsException e2) {
                    Rlog.d(LOG_TAG, "Could not get UT handle for updateCOLP.");
                    return;
                }
            }
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        }
    }

    void handleCOLR() {
        Rlog.d(LOG_TAG, "processCode: is COLR");
        if (isActivate()) {
            try {
                this.mPhone.getCallTracker().getUtInterface().updateCOLR(1, obtainMessage(0, this));
            } catch (ImsException e) {
                Rlog.d(LOG_TAG, "Could not get UT handle for updateCOLR.");
            }
        } else if (isDeactivate()) {
            try {
                this.mPhone.getCallTracker().getUtInterface().updateCOLR(0, obtainMessage(0, this));
            } catch (ImsException e2) {
                Rlog.d(LOG_TAG, "Could not get UT handle for updateCOLR.");
            }
        } else {
            if (isInterrogate()) {
                try {
                    this.mPhone.getCallTracker().getUtInterface().queryCOLR(obtainMessage(7, this));
                    return;
                } catch (ImsException e3) {
                    Rlog.d(LOG_TAG, "Could not get UT handle for queryCOLR.");
                    return;
                }
            }
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        }
    }

    void handleCallBarringSpecificMT() {
        Rlog.d(LOG_TAG, "processCode: is CB (specifc MT)");
        try {
            if (isInterrogate()) {
                this.mPhone.getCallTracker().getUtInterface().queryCallBarring(10, obtainMessage(10, this));
            } else {
                processIcbMmiCodeForUpdate();
            }
        } catch (ImsException e) {
            Rlog.d(LOG_TAG, "Could not get UT handle for ICB.");
        }
    }

    void handleCallBarringACR() {
        int callAction;
        Rlog.d(LOG_TAG, "processCode: is CB (ACR/AR)");
        String password = this.mSia;
        int serviceClass = siToServiceClass(this.mSib);
        try {
            if (isInterrogate()) {
                this.mPhone.getCallTracker().getUtInterface().queryCallBarring(6, obtainMessage(10, this));
                return;
            }
            if (isActivate()) {
                callAction = 1;
            } else {
                isDeactivate();
                callAction = 0;
            }
            this.mPhone.getCallTracker().getUtInterface().updateCallBarring(6, callAction, obtainMessage(0, this), (String[]) null, serviceClass, password);
        } catch (ImsException e) {
            Rlog.d(LOG_TAG, "Could not get UT handle for ICBa.");
        }
    }

    void handleCW() {
        Rlog.d(LOG_TAG, "processCode: is CW");
        int serviceClass = siToServiceClass(this.mSia);
        if (supportMdAutoSetupIms()) {
            if (isActivate() || isDeactivate()) {
                this.mPhone.setCallWaiting(isActivate(), serviceClass, obtainMessage(0, this));
            } else {
                if (isInterrogate()) {
                    this.mPhone.getCallWaiting(obtainMessage(3, this));
                    return;
                }
                throw new RuntimeException("Invalid or Unsupported MMI Code");
            }
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                onSetComplete(msg, (AsyncResult) msg.obj);
                break;
            case 4:
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.exception == null && msg.arg1 == 1) {
                    boolean cffEnabled = msg.arg2 == 1;
                    Rlog.i(LOG_TAG, "EVENT_SET_CFF_COMPLETE: cffEnabled:" + cffEnabled + ", mDialingNumber=" + this.mDialingNumber + ", mIccRecords=" + this.mIccRecords);
                    if (this.mIccRecords != null) {
                        this.mPhone.mDefaultPhone.setVoiceCallForwardingFlag(1, cffEnabled, this.mDialingNumber);
                        ((MtkImsPhone) this.mPhone).saveTimeSlot(null);
                    }
                }
                onSetComplete(msg, ar);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public static boolean isUtMmiCode(String dialString, ImsPhone dialPhone) {
        MtkImsPhoneMmiCode mmi = newFromDialString(dialString, dialPhone);
        if (mmi == null || mmi.isTemporaryModeCLIR() || mmi.isShortCode() || mmi.mDialingNumber != null || mmi.mSc == null || (!mmi.mSc.equals("30") && !mmi.mSc.equals("31") && !mmi.mSc.equals("76") && !mmi.mSc.equals("77") && !isServiceCodeCallForwarding(mmi.mSc) && !isServiceCodeCallBarring(mmi.mSc) && !mmi.mSc.equals("43") && !mmi.mSc.equals("156") && !mmi.mSc.equals("157"))) {
            return false;
        }
        return true;
    }

    private boolean supportMdAutoSetupIms() {
        if (!SystemProperties.get("ro.vendor.md_auto_setup_ims").equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return false;
        }
        return true;
    }

    protected void onQueryCfComplete(AsyncResult ar) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (ar.exception != null) {
            super.onQueryCfComplete(ar);
            return;
        }
        CallForwardInfo[] infos = (CallForwardInfo[]) ar.result;
        if (infos == null || infos.length == 0) {
            sb.append(this.mContext.getText(R.string.permlab_sdcardRead));
            if (this.mIccRecords != null) {
                this.mPhone.setVoiceCallForwardingFlag(1, false, (String) null);
            }
        } else {
            SpannableStringBuilder tb = new SpannableStringBuilder();
            for (int serviceClassMask = 1; serviceClassMask <= 512; serviceClassMask <<= 1) {
                if (serviceClassMask != 256) {
                    int s = infos.length;
                    for (int i = 0; i < s; i++) {
                        if ((infos[i].serviceClass & serviceClassMask) != 0) {
                            tb.append(makeCFQueryResultMessage(infos[i], serviceClassMask));
                            tb.append((CharSequence) "\n");
                        }
                    }
                }
            }
            sb.append((CharSequence) tb);
        }
        this.mState = MmiCode.State.COMPLETE;
        this.mMessage = sb;
        Rlog.d(LOG_TAG, "onQueryCfComplete: mmi=" + this);
        checkProcessTime();
        this.mPhone.onMMIDone(this);
    }

    protected void onSuppSvcQueryComplete(AsyncResult ar) {
        if (isServiceCodeCallBarring(this.mSc) && ar.exception == null && !(ar.result instanceof Bundle)) {
            StringBuilder sb = new StringBuilder(getScString());
            sb.append("\n");
            Rlog.d(LOG_TAG, "onSuppSvcQueryComplete: Received Call Barring Response.");
            int[] cbInfos = (int[]) ar.result;
            if (cbInfos[0] == 0) {
                sb.append(this.mContext.getText(R.string.permlab_sdcardRead));
            } else {
                sb.append(createQueryCallBarringResultMessage(cbInfos[0]));
            }
            this.mState = MmiCode.State.COMPLETE;
            this.mMessage = sb;
            Rlog.d(LOG_TAG, "onSuppSvcQueryComplete mmi=" + this);
            checkProcessTime();
            this.mPhone.onMMIDone(this);
            return;
        }
        super.onSuppSvcQueryComplete(ar);
    }

    private CharSequence createQueryCallBarringResultMessage(int serviceClass) {
        StringBuilder sb = new StringBuilder(this.mContext.getText(R.string.permlab_sendSms));
        for (int classMask = 1; classMask <= 512; classMask <<= 1) {
            if ((classMask & serviceClass) != 0) {
                sb.append("\n");
                sb.append(serviceClassToCFString(classMask & serviceClass));
            }
        }
        return sb;
    }

    protected CharSequence serviceClassToCFString(int serviceClass) {
        Rlog.d(LOG_TAG, "serviceClassToCFString, serviceClass = " + serviceClass);
        switch (serviceClass) {
            case 256:
            case 512:
                return this.mContext.getText(134545476);
            default:
                return super.serviceClassToCFString(serviceClass);
        }
    }

    public CharSequence getMmiErrorMessage(AsyncResult ar) {
        if (ar.exception instanceof ImsException) {
            Rlog.d(LOG_TAG, "getMmiErrorMessage, ims error code = " + ar.exception.getCode());
            switch (ar.exception.getCode()) {
                case 241:
                    return this.mContext.getText(R.string.lockscreen_permanent_disabled_sim_instructions);
                case 822:
                    return this.mContext.getText(R.string.preposition_for_year);
                case 823:
                    return this.mContext.getText(R.string.printing_disabled_by);
                case 824:
                    return this.mContext.getText(R.string.print_service_installed_title);
                case 825:
                    return this.mContext.getText(R.string.print_service_installed_message);
                case 61449:
                    ImsException error = ar.exception;
                    String errorMsg = error.getMessage();
                    if (errorMsg != null && !errorMsg.isEmpty()) {
                        String errorMsg2 = removeLastErrorCode(errorMsg);
                        Rlog.d(LOG_TAG, "Ims errorMessage = " + errorMsg2);
                        return errorMsg2;
                    }
                    return this.mContext.getText(R.string.lockscreen_pattern_correct);
                default:
                    return this.mContext.getText(R.string.lockscreen_pattern_correct);
            }
        }
        if (ar.exception instanceof CommandException) {
            CommandException err = ar.exception;
            Rlog.d(LOG_TAG, "getMmiErrorMessage, error code = " + err.getCommandError());
            if (err.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
                return this.mContext.getText(R.string.lockscreen_permanent_disabled_sim_instructions);
            }
            if (err.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL) {
                return this.mContext.getText(R.string.preposition_for_year);
            }
            if (err.getCommandError() == CommandException.Error.SS_MODIFIED_TO_USSD) {
                return this.mContext.getText(R.string.printing_disabled_by);
            }
            if (err.getCommandError() == CommandException.Error.SS_MODIFIED_TO_SS) {
                return this.mContext.getText(R.string.print_service_installed_title);
            }
            if (err.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO) {
                return this.mContext.getText(R.string.print_service_installed_message);
            }
            if (err.getCommandError() == CommandException.Error.OEM_ERROR_25 || err.getCommandError() == CommandException.Error.OEM_ERROR_6 || err.getCommandError() == CommandException.Error.OEM_ERROR_24 || err.getCommandError() == CommandException.Error.OEM_ERROR_23 || err.getCommandError() == CommandException.Error.OEM_ERROR_22) {
                String errorMsg3 = err.getMessage();
                if (errorMsg3 != null && !errorMsg3.isEmpty()) {
                    String errorMsg4 = removeLastErrorCode(errorMsg3);
                    Rlog.d(LOG_TAG, "errorMessage = " + errorMsg4);
                    return errorMsg4;
                }
                return this.mContext.getText(R.string.lockscreen_pattern_correct);
            }
        }
        return this.mContext.getText(R.string.lockscreen_pattern_correct);
    }

    protected CharSequence getImsErrorMessage(AsyncResult ar) {
        ImsException error = ar.exception;
        CharSequence errorMessage = getMmiErrorMessage(ar);
        if (errorMessage != null) {
            return errorMessage;
        }
        if (error.getMessage() != null) {
            String errorMsg = removeLastErrorCode(error.getMessage());
            Rlog.d(LOG_TAG, "getImsErrorMessage, errorMsg = " + errorMsg);
            return errorMsg;
        }
        return getErrorMessage(ar);
    }

    protected CharSequence getErrorMessage(AsyncResult ar) {
        if (ar.exception instanceof CommandException) {
            CommandException err = ar.exception;
            String errorMsg = err.getMessage();
            if (err.getCommandError() == CommandException.Error.OEM_ERROR_1 && errorMsg != null && !errorMsg.isEmpty()) {
                String errorMsg2 = removeLastErrorCode(errorMsg);
                Rlog.d(LOG_TAG, "getErrorMessage, errorMsg = " + errorMsg2);
                return errorMsg2;
            }
        }
        return super.getErrorMessage(ar);
    }

    private String removeLastErrorCode(CharSequence str) {
        String reverse = new StringBuilder(str).reverse().toString();
        String result = new StringBuilder(reverse.replaceFirst("\\)[0-9]{5}\\(", "")).reverse().toString();
        return result;
    }

    protected static boolean isServiceCodeCallForwarding(String sc) {
        return sc != null && (sc.equals("21") || sc.equals("67") || sc.equals("61") || sc.equals("62") || sc.equals("002") || sc.equals("004") || sc.equals(SC_CFNotRegister));
    }

    protected static int scToCallForwardReason(String sc) {
        if (sc == null) {
            throw new RuntimeException("invalid call forward sc");
        }
        if (sc.equals("002")) {
            return 4;
        }
        if (sc.equals("21")) {
            return 0;
        }
        if (sc.equals("67")) {
            return 1;
        }
        if (sc.equals("62")) {
            return 3;
        }
        if (sc.equals("61")) {
            return 2;
        }
        if (sc.equals("004")) {
            return 5;
        }
        if (sc.equals(SC_CFNotRegister)) {
            return 6;
        }
        throw new RuntimeException("invalid call forward sc");
    }

    private void handleGeneralError() {
        this.mState = MmiCode.State.FAILED;
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        sb.append(this.mContext.getText(R.string.lockscreen_pattern_correct));
        this.mMessage = sb;
        checkProcessTime();
        this.mPhone.onMMIDone(this);
    }

    private boolean isFacToDial() {
        CarrierConfigManager configManager = (CarrierConfigManager) this.mPhone.getContext().getSystemService("carrier_config");
        PersistableBundle b = configManager.getConfigForSubId(this.mPhone.getSubId());
        if (b != null) {
            String[] dialFacList = b.getStringArray("feature_access_codes_string_array");
            if (!ArrayUtils.isEmpty(dialFacList)) {
                for (String fac : dialFacList) {
                    if (fac.equals(this.mSc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("MtkImsPhoneMmiCode {");
        sb.append("State=" + getState());
        if (this.mAction != null) {
            sb.append(" action=" + this.mAction);
        }
        if (this.mSc != null) {
            sb.append(" sc=" + this.mSc);
        }
        if (this.mSia != null) {
            sb.append(" sia=" + MtkSuppServHelper.encryptString(this.mSia));
        }
        if (this.mSib != null) {
            sb.append(" sib=" + MtkSuppServHelper.encryptString(this.mSib));
        }
        if (this.mSic != null) {
            sb.append(" sic=" + MtkSuppServHelper.encryptString(this.mSic));
        }
        if (this.mPoundString != null) {
            sb.append(" poundString=" + Rlog.pii(LOG_TAG, this.mPoundString));
        }
        if (this.mDialingNumber != null) {
            sb.append(" dialingNumber=" + Rlog.pii(LOG_TAG, this.mDialingNumber));
        }
        if (this.mPwd != null) {
            sb.append(" pwd=" + Rlog.pii(LOG_TAG, this.mPwd));
        }
        if (this.mCallbackReceiver != null) {
            sb.append(" hasReceiver");
        }
        sb.append("}");
        return sb.toString();
    }
}
