package com.mediatek.internal.telephony.gsm;

import android.R;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.MtkSuppServHelper;
import com.mediatek.internal.telephony.MtkSuppServManager;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import java.util.regex.Matcher;

/* JADX INFO: loaded from: classes.dex */
public final class MtkGsmMmiCode extends GsmMmiCode {
    static final String CNAPMmi = "Calling Name Presentation";
    static final int EVENT_GET_COLP_COMPLETE = 9;
    static final int EVENT_GET_COLR_COMPLETE = 8;
    static final String LOG_TAG = "MtkGsmMmiCode";
    static final String SC_CNAP = "300";
    static final String SC_COLP = "76";
    static final String SC_COLR = "77";
    private static final boolean SENLOG = TextUtils.equals(Build.TYPE, DataSubConstants.REASON_MOBILE_DATA_ENABLE_USER);
    MtkGsmCdmaPhone mPhone;

    public static MtkGsmMmiCode newFromDialString(String dialString, MtkGsmCdmaPhone phone, UiccCardApplication app) {
        return newFromDialString(dialString, phone, app, null);
    }

    public static MtkGsmMmiCode newFromDialString(String dialString, MtkGsmCdmaPhone phone, UiccCardApplication app, ResultReceiver wrappedCallback) {
        MtkGsmMmiCode ret = null;
        Rlog.d(LOG_TAG, "newFromDialString, dialstring = " + MtkSuppServHelper.encryptString(dialString));
        String dialPart = PhoneNumberUtils.extractNetworkPortionAlt(PhoneNumberUtils.stripSeparators(dialString));
        boolean isMmi = dialPart.startsWith("*") || dialPart.startsWith("#") || dialPart.endsWith("#");
        if (!isMmi && dialPart.length() > 2) {
            Rlog.d(LOG_TAG, "Not belong to MMI format.");
            return null;
        }
        if (phone.getServiceState().getVoiceRoaming() && phone.supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) {
            dialString = convertCdmaMmiCodesTo3gppMmiCodes(dialString);
        }
        Matcher m = sPatternSuppService.matcher(dialString);
        if (m.matches()) {
            ret = new MtkGsmMmiCode(phone, app);
            ret.mPoundString = makeEmptyNull(m.group(1));
            ret.mAction = makeEmptyNull(m.group(2));
            ret.mSc = makeEmptyNull(m.group(3));
            ret.mSia = makeEmptyNull(m.group(5));
            ret.mSib = makeEmptyNull(m.group(7));
            ret.mSic = makeEmptyNull(m.group(9));
            ret.mPwd = makeEmptyNull(m.group(11));
            ret.mDialingNumber = makeEmptyNull(m.group(12));
            if (ret.mDialingNumber != null && ret.mDialingNumber.endsWith("#") && dialString.endsWith("#")) {
                ret = new MtkGsmMmiCode(phone, app);
                ret.mPoundString = dialString;
            } else if (ret.isFacToDial()) {
                ret = null;
            }
        } else if (dialString.endsWith("#")) {
            ret = new MtkGsmMmiCode(phone, app);
            ret.mPoundString = dialString;
        } else if (isTwoDigitShortCode(phone.getContext(), phone.getSubId(), dialString)) {
            ret = null;
        } else if (isShortCode(dialString, phone)) {
            ret = new MtkGsmMmiCode(phone, app);
            ret.mDialingNumber = dialString;
        }
        if (ret != null) {
            ret.mCallbackReceiver = wrappedCallback;
        }
        return ret;
    }

    public static MtkGsmMmiCode newFromUssdUserInput(String ussdMessge, MtkGsmCdmaPhone phone, UiccCardApplication app) {
        MtkGsmMmiCode ret = new MtkGsmMmiCode(phone, app);
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

    public MtkGsmMmiCode(MtkGsmCdmaPhone phone, UiccCardApplication app) {
        super(phone, app);
        this.mPhone = phone;
    }

    protected boolean isExtensionCode(String sc) {
        if (sc.equals(SC_CNAP) || this.mSc.equals(SC_COLP) || this.mSc.equals(SC_COLR) || this.mSc.equals("03")) {
            return true;
        }
        return false;
    }

    protected void processExtensionCode() {
        Rlog.d(LOG_TAG, "processExtensionCode");
        if (this.mSc.equals(SC_CNAP)) {
            if (isInterrogate()) {
                if (this.mPoundString != null) {
                    handleCNAP(this.mPoundString);
                    return;
                }
                return;
            }
            throw new RuntimeException("Invalid or Unsupported MMI Code");
        }
        if (this.mSc.equals(SC_COLP)) {
            handleCOLP();
        } else if (this.mSc.equals(SC_COLR)) {
            handleCOLR();
        } else if (this.mSc.equals("03")) {
            handleChangeBarringPassward();
        }
    }

    void handleCNAP(String cnapssMessage) {
        Rlog.d(LOG_TAG, "processCode: is CNAP");
        if (isInterrogate()) {
            this.mPhone.mMtkCi.sendCNAP(cnapssMessage, obtainMessage(5, this));
            return;
        }
        throw new RuntimeException("Invalid or Unsupported MMI Code");
    }

    void handleCOLP() {
        Rlog.d(LOG_TAG, "processCode: is COLP");
        if (isInterrogate()) {
            if (supportMdAutoSetupIms()) {
                this.mPhone.mMtkCi.getCOLP(obtainMessage(9, this));
                return;
            }
            return;
        }
        throw new RuntimeException("Invalid or Unsupported MMI Code");
    }

    void handleCOLR() {
        Rlog.d(LOG_TAG, "processCode: is COLR");
        if (isInterrogate()) {
            if (supportMdAutoSetupIms()) {
                this.mPhone.mMtkCi.getCOLR(obtainMessage(8, this));
                return;
            }
            return;
        }
        throw new RuntimeException("Invalid or Unsupported MMI Code");
    }

    void handleChangeBarringPassward() {
        String facility;
        ImsPhone imsPhone;
        Rlog.d(LOG_TAG, "processCode: is Change PWD");
        String oldPwd = this.mSib;
        String newPwd = this.mSic;
        if (isActivate() || isRegister()) {
            this.mAction = "**";
            if (this.mSia == null) {
                facility = "AB";
            } else {
                String facility2 = this.mSia;
                facility = scToBarringFacility(facility2);
            }
            if (oldPwd != null && newPwd != null && this.mPwd != null) {
                if (this.mPwd.length() != newPwd.length() || oldPwd.length() != 4 || this.mPwd.length() != 4) {
                    handlePasswordError(R.string.mediasize_na_arch_b);
                    return;
                }
                if (!(this.mPhone.getImsPhone() instanceof ImsPhone)) {
                    imsPhone = null;
                } else {
                    ImsPhone imsPhone2 = (ImsPhone) this.mPhone.getImsPhone();
                    imsPhone = imsPhone2;
                }
                if (imsPhone != null && imsPhone.isInCall()) {
                    Message msg = obtainMessage(1, this);
                    CommandException ce = new CommandException(CommandException.Error.GENERIC_FAILURE);
                    AsyncResult.forMessage(msg, (Object) null, ce);
                    msg.sendToTarget();
                    return;
                }
                this.mPhone.mMtkCi.changeBarringPassword(facility, oldPwd, newPwd, this.mPwd, obtainMessage(1, this));
                return;
            }
            handlePasswordError(R.string.mediasize_na_arch_b);
            return;
        }
        throw new RuntimeException("Invalid or Unsupported MMI Code");
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                AsyncResult ar = (AsyncResult) msg.obj;
                onSetComplete(msg, ar);
                break;
            case 2:
            case 3:
            case 4:
            case 7:
            default:
                super.handleMessage(msg);
                break;
            case 5:
                AsyncResult ar2 = (AsyncResult) msg.obj;
                onQueryComplete(ar2);
                break;
            case 6:
                AsyncResult ar3 = (AsyncResult) msg.obj;
                if (ar3.exception == null && msg.arg1 == 1) {
                    boolean cffEnabled = msg.arg2 == 1;
                    if (this.mIccRecords != null) {
                        this.mPhone.setVoiceCallForwardingFlag(1, cffEnabled, this.mDialingNumber);
                        this.mPhone.saveTimeSlot(null);
                    }
                }
                onSetComplete(msg, ar3);
                break;
            case 8:
                AsyncResult ar4 = (AsyncResult) msg.obj;
                onGetColrComplete(ar4);
                break;
            case 9:
                AsyncResult ar5 = (AsyncResult) msg.obj;
                onGetColpComplete(ar5);
                break;
        }
    }

    protected void onSetComplete(Message msg, AsyncResult ar) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (ar.exception != null) {
            this.mState = MmiCode.State.FAILED;
            if (ar.exception instanceof CommandException) {
                CommandException.Error err = ar.exception.getCommandError();
                if (err == CommandException.Error.REQUEST_NOT_SUPPORTED && (this.mSc.equals("31") || this.mSc.equals("30"))) {
                    sb.append(this.mContext.getText(R.string.lockscreen_pattern_correct));
                    this.mMessage = sb;
                    this.mPhone.onMMIDone(this);
                    return;
                }
            }
        }
        super.onSetComplete(msg, ar);
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
                this.mPhone.setVoiceCallForwardingFlag(1, false, null);
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
                            if (infos[i].reason == 0 && (infos[i].serviceClass & serviceClassMask) == 1 && this.mIccRecords != null) {
                                this.mPhone.setVoiceCallForwardingFlag(1, infos[i].status == 1, null);
                            }
                        }
                    }
                }
            }
            sb.append((CharSequence) tb);
        }
        this.mState = MmiCode.State.COMPLETE;
        this.mMessage = sb;
        Rlog.d(LOG_TAG, "onQueryCfComplete: mmi=" + this);
        this.mPhone.onMMIDone(this);
    }

    protected void onQueryComplete(AsyncResult ar) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (ar.exception == null) {
            int[] ints = (int[]) ar.result;
            if (ints.length != 0 && this.mSc.equals(SC_CNAP)) {
                Rlog.d(LOG_TAG, "onQueryComplete_CNAP");
                sb.append(createQueryCnapResultMessage(ints[0], ints[1]));
                this.mMessage = sb;
                this.mState = MmiCode.State.COMPLETE;
                this.mPhone.onMMIDone(this);
                return;
            }
        }
        super.onQueryComplete(ar);
    }

    protected CharSequence createQueryCallWaitingResultMessage(int serviceClass) {
        StringBuilder sb = new StringBuilder(this.mContext.getText(R.string.permlab_sendSms));
        for (int classMask = 1; classMask <= 512; classMask <<= 1) {
            if ((classMask & serviceClass) != 0) {
                sb.append("\n");
                sb.append(serviceClassToCFString(classMask & serviceClass));
            }
        }
        return sb;
    }

    protected CharSequence createQueryCallBarringResultMessage(int serviceClass) {
        StringBuilder sb = new StringBuilder(this.mContext.getText(R.string.permlab_sendSms));
        for (int classMask = 1; classMask <= 512; classMask <<= 1) {
            if ((classMask & serviceClass) != 0) {
                sb.append("\n");
                sb.append(serviceClassToCFString(classMask & serviceClass));
            }
        }
        return sb;
    }

    private CharSequence createQueryCnapResultMessage(int status, int serviceProvisioned) {
        Rlog.d(LOG_TAG, "createQueryCnapResultMessage, status = " + status + ", provisioned = " + serviceProvisioned);
        StringBuilder sb = new StringBuilder();
        switch (serviceProvisioned) {
            case 0:
                sb.append(this.mContext.getText(R.string.permlab_setInputCalibration));
                break;
            case 1:
                if (status == 0) {
                    sb.append(this.mContext.getText(R.string.permlab_sdcardRead));
                } else if (status == 1) {
                    sb.append(this.mContext.getText(R.string.permlab_sdcardWrite));
                }
                break;
            default:
                sb.append(this.mContext.getText(R.string.permlab_setInputCalibration));
                break;
        }
        Rlog.d(LOG_TAG, "CNAP_sb = " + ((Object) sb));
        return sb;
    }

    private void onGetColpComplete(AsyncResult ar) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (ar.exception != null) {
            this.mState = MmiCode.State.FAILED;
            sb.append(getErrorMessage(ar));
        } else {
            int[] colpArgs = (int[]) ar.result;
            switch (colpArgs[1]) {
                case 0:
                    sb.append(this.mContext.getText(R.string.permlab_setInputCalibration));
                    this.mState = MmiCode.State.COMPLETE;
                    break;
                case 1:
                    sb.append(this.mContext.getText(134545420));
                    this.mState = MmiCode.State.COMPLETE;
                    break;
                case 2:
                    sb.append(this.mContext.getText(134545421));
                    this.mState = MmiCode.State.COMPLETE;
                    break;
            }
        }
        this.mMessage = sb;
        this.mPhone.onMMIDone(this);
    }

    private void onGetColrComplete(AsyncResult ar) {
        StringBuilder sb = new StringBuilder(getScString());
        sb.append("\n");
        if (ar.exception != null) {
            this.mState = MmiCode.State.FAILED;
            sb.append(getErrorMessage(ar));
        } else {
            int[] colrArgs = (int[]) ar.result;
            switch (colrArgs[0]) {
                case 0:
                    sb.append(this.mContext.getText(R.string.permlab_setInputCalibration));
                    this.mState = MmiCode.State.COMPLETE;
                    break;
                case 1:
                    sb.append(this.mContext.getText(134545420));
                    this.mState = MmiCode.State.COMPLETE;
                    break;
                case 2:
                    sb.append(this.mContext.getText(R.string.lockscreen_pattern_correct));
                    this.mState = MmiCode.State.FAILED;
                    break;
            }
        }
        this.mMessage = sb;
        this.mPhone.onMMIDone(this);
    }

    public static boolean isUtMmiCode(String dialString, MtkGsmCdmaPhone dialPhone, UiccCardApplication iccApp) {
        MtkGsmMmiCode mmi = newFromDialString(dialString, dialPhone, iccApp);
        if (mmi == null || mmi.isTemporaryModeCLIR() || mmi.isShortCode() || mmi.mDialingNumber != null || mmi.mSc == null || (!mmi.mSc.equals("30") && !mmi.mSc.equals("31") && !mmi.mSc.equals(SC_COLP) && !mmi.mSc.equals(SC_COLR) && !isServiceCodeCallForwarding(mmi.mSc) && !isServiceCodeCallBarring(mmi.mSc) && !mmi.mSc.equals("43"))) {
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

    protected CharSequence getScString() {
        if (this.mSc != null && this.mSc.equals(SC_CNAP)) {
            return CNAPMmi;
        }
        return super.getScString();
    }

    public CharSequence getErrorMessage(AsyncResult ar) {
        if (ar.exception instanceof CommandException) {
            CommandException.Error err = ar.exception.getCommandError();
            if (err == CommandException.Error.OEM_ERROR_25 || err == CommandException.Error.OEM_ERROR_6 || err == CommandException.Error.OEM_ERROR_24 || err == CommandException.Error.OEM_ERROR_23 || err == CommandException.Error.OEM_ERROR_22) {
                if (supportMdAutoSetupIms()) {
                    Rlog.i(LOG_TAG, "getErrorMessage, OEM_ERROR");
                    MtkSuppServHelper ssHelper = MtkSuppServManager.getSuppServHelper(this.mPhone.getPhoneId());
                    if (ssHelper != null) {
                        String errorMsg = ssHelper.getXCAPErrorMessageFromSysProp(err);
                        if (errorMsg != null && !errorMsg.isEmpty()) {
                            return errorMsg;
                        }
                        return this.mContext.getText(R.string.lockscreen_pattern_correct);
                    }
                } else {
                    return this.mContext.getText(R.string.lockscreen_pattern_correct);
                }
            } else {
                if (err == CommandException.Error.OEM_ERROR_5) {
                    Rlog.i(LOG_TAG, "getErrorMessage, OEM_ERROR_5 CALL_BARRED");
                    return this.mContext.getText(134545416);
                }
                if (err == CommandException.Error.FDN_CHECK_FAILURE) {
                    Rlog.i(LOG_TAG, "getErrorMessage, FDN_CHECK_FAILURE");
                    return this.mContext.getText(134545415);
                }
            }
        }
        return super.getErrorMessage(ar);
    }
}
