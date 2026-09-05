package com.mediatek.internal.telephony.cat;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemProperties;
import android.provider.Settings;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.BIPClientParams;
import com.android.internal.telephony.cat.CallSetupParams;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatResponseMessage;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cat.DisplayTextParams;
import com.android.internal.telephony.cat.Input;
import com.android.internal.telephony.cat.LaunchBrowserParams;
import com.android.internal.telephony.cat.ResponseData;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.RilMessage;
import com.android.internal.telephony.cat.TextMessage;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.mediatek.internal.telephony.ModemSwitchHandler;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.worldphone.WorldMode;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class MtkCatService extends CatService implements MtkAppInterface {
    static final String BIP_STATE_CHANGED = "mediatek.intent.action.BIP_STATE_CHANGED";
    private static final boolean DBG = true;
    private static final int DISABLE_DISPLAY_TEXT_DELAYED_TIME = 30000;
    private static final int IVSR_DELAYED_TIME = 60000;
    public static final int MSG_ID_CACHED_DISPLAY_TEXT_TIMEOUT = 46;
    private static final int MSG_ID_CALL_CTRL = 25;
    public static final int MSG_ID_CONN_RETRY_TIMEOUT = 47;
    private static final int MSG_ID_DISABLE_DISPLAY_TEXT_DELAYED = 15;
    static final int MSG_ID_EVENT_DOWNLOAD = 11;
    private static final int MSG_ID_IVSR_DELAYED = 14;
    private static final int MSG_ID_SETUP_MENU_RESET = 24;
    private BroadcastReceiver MtkCatServiceReceiver;
    private boolean isDisplayTextDisabled;
    private boolean isIvsrBootUp;
    private BipService mBipService;
    private MtkRIL mMtkCmdIf;
    private boolean mMtkStkAppInstalled;
    private int mPhoneType;
    private boolean mReadFromPreferenceDone;
    Handler mTimeoutHandler;
    private int simState;
    private static String[] sInstKey = {"sInstanceSim1", "sInstanceSim2", "sInstanceSim3", "sInstanceSim4"};
    protected static Object mLock = new Object();

    void cancelTimeOut(int msg) {
        MtkCatLog.d(this, "cancelTimeOut, sim_id: " + this.mSlotId + ", msg id: " + msg);
        this.mTimeoutHandler.removeMessages(msg);
    }

    void startTimeOut(int msg, long delay) {
        MtkCatLog.d(this, "startTimeOut, sim_id: " + this.mSlotId + ", msg id: " + msg);
        cancelTimeOut(msg);
        Handler handler = this.mTimeoutHandler;
        handler.sendMessageDelayed(handler.obtainMessage(msg), delay);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkCatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir, Context context, IccFileHandler fh, UiccProfile uiccProfile, int slotId, Looper looper) {
        super(ci, ca, ir, context, fh, uiccProfile, slotId, looper);
        this.mReadFromPreferenceDone = false;
        this.mMtkStkAppInstalled = false;
        this.mBipService = null;
        this.simState = 0;
        this.isIvsrBootUp = false;
        this.isDisplayTextDisabled = false;
        this.mPhoneType = 0;
        this.mTimeoutHandler = new Handler() { // from class: com.mediatek.internal.telephony.cat.MtkCatService.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 15:
                        MtkCatLog.d(this, "[Reset Disable Display Text flag because timeout");
                        MtkCatService.this.isDisplayTextDisabled = false;
                        break;
                    case MtkCatService.MSG_ID_CACHED_DISPLAY_TEXT_TIMEOUT /* 46 */:
                        MtkCatLog.d(this, "Cache DISPLAY_TEXT time out, sim_id: " + MtkCatService.this.mSlotId);
                        break;
                }
            }
        };
        this.MtkCatServiceReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.cat.MtkCatService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                MtkCatLog.d(this, "CatServiceReceiver action: " + action);
                if (action.equals("com.mediatek.intent.action.IVSR_NOTIFY")) {
                    if (MtkCatService.this.mSlotId == intent.getIntExtra("slot", 0) && intent.getStringExtra("action").equals("start")) {
                        MtkCatLog.d(this, "[IVSR set IVSR flag");
                        MtkCatService.this.isIvsrBootUp = MtkCatService.DBG;
                        MtkCatService.this.sendEmptyMessageDelayed(14, 60000L);
                        return;
                    }
                    return;
                }
                if (action.equals(ModemSwitchHandler.ACTION_MD_TYPE_CHANGE)) {
                    MtkCatLog.d(this, "[World phone flag: " + MtkCatService.this.mSlotId + ", isDisplayTextDisabled: " + (MtkCatService.this.isDisplayTextDisabled ? 1 : 0));
                    MtkCatService.this.startTimeOut(15, 30000L);
                    MtkCatService.this.isDisplayTextDisabled = MtkCatService.DBG;
                }
            }
        };
        MtkCatLog.d(this, "slotId " + slotId);
        this.mMtkCmdIf = (MtkRIL) ci;
        if (!SystemProperties.get("ro.vendor.mtk_ril_mode").equals("c6m_1rild")) {
            this.mBipService = BipService.getInstance(this.mContext, this, this.mSlotId, this.mCmdIf, fh);
        }
        IntentFilter intentFilter = new IntentFilter("com.mediatek.intent.action.IVSR_NOTIFY");
        intentFilter.addAction(ModemSwitchHandler.ACTION_MD_TYPE_CHANGE);
        this.mContext.registerReceiver(this.MtkCatServiceReceiver, intentFilter, 2);
        MtkCatLog.d(this, "CatService: is running");
        this.mMtkCmdIf.setOnStkSetupMenuReset(this, MSG_ID_SETUP_MENU_RESET, null);
        this.mMtkStkAppInstalled = isMtkStkAppInstalled();
        MtkCatLog.d(this, "MTK STK app installed = " + this.mMtkStkAppInstalled);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        synchronized (sInstanceLock) {
            MtkCatLog.d(this, "Disposing MtkCatService object : " + this.mSlotId);
            if (sInstance != null && sInstance[this.mSlotId] != null) {
                if (this.MtkCatServiceReceiver != null) {
                    this.mContext.unregisterReceiver(this.MtkCatServiceReceiver);
                    this.MtkCatServiceReceiver = null;
                }
                this.mMtkCmdIf.unSetOnStkSetupMenuReset(this);
                this.mCmdIf.unregisterForIccRefresh(this);
                BipService bipService = this.mBipService;
                if (bipService != null) {
                    bipService.dispose();
                }
                super.dispose();
                return;
            }
            MtkCatLog.d(this, "sInstance is null, maybe dispose already: " + this.mSlotId);
        }
    }

    protected void handleRilMsg(RilMessage rilMsg) {
        int typeOfCommand;
        int commandNumber;
        int commandQualifier;
        if (rilMsg == null) {
            return;
        }
        if (rilMsg.mId == 3) {
            CommandParams cmdParams = (CommandParams) rilMsg.mData;
            if (cmdParams != null) {
                if (rilMsg.mResCode == ResultCode.OK) {
                    handleCommand(cmdParams, false);
                    return;
                }
                MtkCatLog.d(this, "event notify error code: " + rilMsg.mResCode);
                if (rilMsg.mResCode == ResultCode.PRFRMD_ICON_NOT_DISPLAYED && (cmdParams.mCmdDet.typeOfCommand == 17 || cmdParams.mCmdDet.typeOfCommand == 18 || cmdParams.mCmdDet.typeOfCommand == 19 || cmdParams.mCmdDet.typeOfCommand == 20)) {
                    MtkCatLog.d(this, "notify user text message even though get icon fail");
                    handleCommand(cmdParams, false);
                }
                if (cmdParams.mCmdDet.typeOfCommand == 64) {
                    MtkCatLog.d(this, "Open Channel with ResultCode");
                    handleCommand(cmdParams, false);
                }
                if (rilMsg.mResCode == ResultCode.CMD_DATA_NOT_UNDERSTOOD) {
                    AppInterface.CommandType type = AppInterface.CommandType.fromInt(cmdParams.mCmdDet.typeOfCommand);
                    if (type == AppInterface.CommandType.SET_UP_CALL) {
                        this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(false, ResultCode.CMD_DATA_NOT_UNDERSTOOD.value(), null);
                        return;
                    } else {
                        sendTerminalResponse(cmdParams.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
                        return;
                    }
                }
                return;
            }
            return;
        }
        if (rilMsg.mId == 2) {
            this.mIsProactiveCmdResponsed = false;
            try {
                CommandParams cmdParams2 = (CommandParams) rilMsg.mData;
                if (cmdParams2 != null) {
                    if (rilMsg.mResCode == ResultCode.OK || rilMsg.mResCode == ResultCode.PRFRMD_ICON_NOT_DISPLAYED) {
                        handleCommand(cmdParams2, DBG);
                        return;
                    } else {
                        sendTerminalResponse(cmdParams2.mCmdDet, rilMsg.mResCode, false, 0, null);
                        return;
                    }
                }
                return;
            } catch (ClassCastException e) {
                MtkCatLog.d(this, "Fail to parse proactive command");
                if (this.mCurrntCmd != null) {
                    sendTerminalResponse(this.mCurrntCmd.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
                    return;
                }
                String cmdData = (String) rilMsg.mData;
                if (cmdData != null) {
                    if (Integer.parseInt(cmdData.split("")[2]) < 7) {
                        typeOfCommand = Integer.parseInt(cmdData.substring(10, 12), 16);
                        commandNumber = Integer.parseInt(cmdData.substring(8, 10));
                        commandQualifier = Integer.parseInt(cmdData.substring(12, 14));
                    } else {
                        typeOfCommand = Integer.parseInt(cmdData.substring(12, 14), 16);
                        commandNumber = Integer.parseInt(cmdData.substring(10, 12));
                        commandQualifier = Integer.parseInt(cmdData.substring(14, 16));
                    }
                    CommandDetails cmdDet = new CommandDetails();
                    cmdDet.compRequired = DBG;
                    cmdDet.commandNumber = commandNumber;
                    cmdDet.typeOfCommand = typeOfCommand;
                    cmdDet.commandQualifier = commandQualifier;
                    sendTerminalResponse(cmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
                    return;
                }
                return;
            }
        }
        super.handleRilMsg(rilMsg);
    }

    protected void handleCommand(CommandParams cmdParams, boolean isProactiveCmd) {
        int flightMode;
        int flightMode2;
        boolean noAlphaUsrCnf;
        MtkCatLog.d(this, cmdParams.getCommandType().name());
        if (isProactiveCmd && this.mUiccController != null) {
            UiccController.addLocalLog("ProactiveCommand mSlotId=" + this.mSlotId + " cmdParams=" + cmdParams);
        }
        MtkCatCmdMessage cmdMsg = new MtkCatCmdMessage(cmdParams);
        int i = AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[cmdParams.getCommandType().ordinal()];
        boolean z = DBG;
        switch (i) {
            case 1:
                if (removeMenu(cmdMsg.getMenu())) {
                    this.mMenuCmd = null;
                } else {
                    this.mMenuCmd = cmdMsg;
                }
                ResultCode resultCode = cmdParams.mLoadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK;
                sendTerminalResponse(cmdParams.mCmdDet, resultCode, false, 0, null);
                break;
            case 2:
                boolean isAlarmState = isAlarmBoot();
                try {
                    int flightMode3 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on");
                    flightMode = flightMode3;
                } catch (Settings.SettingNotFoundException e) {
                    MtkCatLog.d(this, "fail to get property from Settings");
                    flightMode = 0;
                }
                boolean isFlightMode = flightMode != 0;
                MtkCatLog.d(this, "isAlarmState = " + isAlarmState + ", isFlightMode = " + isFlightMode + ", flightMode = " + flightMode);
                if (isAlarmState && isFlightMode) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, null);
                    return;
                }
                if (this.isIvsrBootUp) {
                    MtkCatLog.d(this, "[IVSR send TR directly");
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.BACKWARD_MOVE_BY_USER, false, 0, null);
                    return;
                } else {
                    if (this.isDisplayTextDisabled) {
                        MtkCatLog.d(this, "[Sim Recovery send TR directly");
                        sendTerminalResponse(cmdParams.mCmdDet, ResultCode.BACKWARD_MOVE_BY_USER, false, 0, null);
                        return;
                    }
                    TextMessage textMessage = ((DisplayTextParams) cmdParams).mTextMsg;
                    if (textMessage.deviceIdentities != null && textMessage.deviceIdentities.destinationId == 255) {
                        MtkCatLog.d(this, "Command not understood send TR directly");
                        sendTerminalResponse(cmdParams.mCmdDet, ResultCode.CMD_TYPE_NOT_UNDERSTOOD, false, 0, null);
                        return;
                    }
                }
                break;
            case 3:
                ResultCode resultCode2 = cmdParams.mLoadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK;
                sendTerminalResponse(cmdParams.mCmdDet, resultCode2, false, 0, null);
                break;
            case 4:
                BipService bipService = this.mBipService;
                if (bipService != null) {
                    bipService.setSetupEventList(cmdMsg);
                }
                this.mIsProactiveCmdResponsed = DBG;
                break;
            case 5:
                if (cmdParams.mCmdDet.commandQualifier == 3) {
                    Calendar cal = Calendar.getInstance();
                    int temp = cal.get(1) - 2000;
                    int hibyte = temp % 10;
                    int lobyte = hibyte << 4;
                    int temp2 = cal.get(2) + 1;
                    int hibyte2 = temp2 / 10;
                    int lobyte2 = (temp2 % 10) << 4;
                    int temp3 = cal.get(5);
                    int hibyte3 = temp3 / 10;
                    int lobyte3 = (temp3 % 10) << 4;
                    int temp4 = cal.get(11);
                    int hibyte4 = temp4 / 10;
                    int lobyte4 = (temp4 % 10) << 4;
                    int temp5 = cal.get(12);
                    int hibyte5 = temp5 / 10;
                    int lobyte5 = (temp5 % 10) << 4;
                    int temp6 = cal.get(13);
                    int hibyte6 = temp6 / 10;
                    int lobyte6 = (temp6 % 10) << 4;
                    int temp7 = cal.get(15) / 900000;
                    int hibyte7 = temp7 / 10;
                    int hibyte8 = temp7 % 10;
                    int lobyte7 = hibyte8 << 4;
                    byte[] datetime = {(byte) (lobyte | (temp / 10)), (byte) (lobyte2 | hibyte2), (byte) (lobyte3 | hibyte3), (byte) (lobyte4 | hibyte4), (byte) (lobyte5 | hibyte5), (byte) (lobyte6 | hibyte6), (byte) (lobyte7 | hibyte7)};
                    ResponseData resp = new MtkProvideLocalInformationResponseData(datetime[0], datetime[1], datetime[2], datetime[3], datetime[4], datetime[5], datetime[6]);
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, resp);
                    return;
                }
                if (cmdParams.mCmdDet.commandQualifier == 4) {
                    Locale locale = Locale.getDefault();
                    byte[] lang = {(byte) locale.getLanguage().charAt(0), (byte) locale.getLanguage().charAt(1)};
                    ResponseData resp2 = new MtkProvideLocalInformationResponseData(lang);
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, resp2);
                    return;
                }
                if (cmdParams.mCmdDet.commandQualifier == 10) {
                    int batterystate = getBatteryState(this.mContext);
                    ResponseData resp3 = new MtkProvideLocalInformationResponseData(batterystate);
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, resp3);
                    return;
                }
                return;
            case 6:
                if (((LaunchBrowserParams) cmdParams).mConfirmMsg.text != null && ((LaunchBrowserParams) cmdParams).mConfirmMsg.text.equals("Default Message")) {
                    CharSequence message = this.mContext.getText(R.string.imProtocolYahoo);
                    ((LaunchBrowserParams) cmdParams).mConfirmMsg.text = message.toString();
                }
                break;
            case 7:
                boolean isAlarmState2 = isAlarmBoot();
                try {
                    int flightMode4 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on");
                    flightMode2 = flightMode4;
                } catch (Settings.SettingNotFoundException e2) {
                    MtkCatLog.d(this, "fail to get property from Settings");
                    flightMode2 = 0;
                }
                if (flightMode2 == 0) {
                    z = false;
                }
                boolean isFlightMode2 = z;
                MtkCatLog.d(this, "isAlarmState = " + isAlarmState2 + ", isFlightMode = " + isFlightMode2 + ", flightMode = " + flightMode2);
                if (isAlarmState2 && isFlightMode2) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.UICC_SESSION_TERM_BY_USER, false, 0, null);
                    return;
                }
                break;
            case 8:
            case 9:
                this.simState = MtkTelephonyManagerEx.getDefault().getSimCardState(this.mSlotId);
                MtkCatLog.d(this, "simState: " + this.simState);
                if (this.simState != 11) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS, false, 0, null);
                    return;
                }
                break;
            case 10:
            case 11:
                if ("Default Message".equals(((DisplayTextParams) cmdParams).mTextMsg.text)) {
                    ((DisplayTextParams) cmdParams).mTextMsg.text = null;
                }
                this.mIsProactiveCmdResponsed = DBG;
                break;
            case 12:
            case 13:
            case 14:
            case 15:
                this.mIsProactiveCmdResponsed = DBG;
                if (((DisplayTextParams) cmdParams).mTextMsg.text != null && ((DisplayTextParams) cmdParams).mTextMsg.text.equals("Default Message")) {
                    CharSequence message2 = this.mContext.getText(R.string.permlab_receiveWapPush);
                    ((DisplayTextParams) cmdParams).mTextMsg.text = message2.toString();
                }
                break;
            case 16:
                this.mIsProactiveCmdResponsed = DBG;
                break;
            case 17:
                if (((CallSetupParams) cmdParams).mConfirmMsg.text != null && ((CallSetupParams) cmdParams).mConfirmMsg.text.equals("Default Message")) {
                    CharSequence message3 = this.mContext.getText(R.string.RestrictedOnNormalTitle);
                    ((CallSetupParams) cmdParams).mConfirmMsg.text = message3.toString();
                }
                break;
            case 18:
            case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
            case 20:
            case WorldMode.MD_WORLD_MODE_LFCTG /* 21 */:
                BIPClientParams cmd = (BIPClientParams) cmdParams;
                try {
                    noAlphaUsrCnf = this.mContext.getResources().getBoolean(R.bool.config_mms_content_disposition_support);
                } catch (Resources.NotFoundException e3) {
                    noAlphaUsrCnf = false;
                }
                if (cmd.mTextMsg.text == null && (cmd.mHasAlphaId || noAlphaUsrCnf)) {
                    MtkCatLog.d(this, "cmd " + cmdParams.getCommandType() + " with null alpha id");
                    if (isProactiveCmd) {
                        sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, null);
                        return;
                    } else {
                        if (cmdParams.getCommandType() == AppInterface.CommandType.OPEN_CHANNEL) {
                            this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(DBG, ResultCode.OK.value(), null);
                            return;
                        }
                        return;
                    }
                }
                if (!this.mStkAppInstalled && !this.mMtkStkAppInstalled) {
                    MtkCatLog.d(this, "No STK application found.");
                    if (isProactiveCmd) {
                        sendTerminalResponse(cmdParams.mCmdDet, ResultCode.BEYOND_TERMINAL_CAPABILITY, false, 0, null);
                        return;
                    }
                }
                if (isProactiveCmd && (cmdParams.getCommandType() == AppInterface.CommandType.CLOSE_CHANNEL || cmdParams.getCommandType() == AppInterface.CommandType.RECEIVE_DATA || cmdParams.getCommandType() == AppInterface.CommandType.SEND_DATA)) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, null);
                }
                break;
                break;
            default:
                MtkCatLog.d(this, "HandleCommand Callback to CatService");
                super.handleCommand(cmdParams, isProactiveCmd);
                return;
        }
        this.mCurrntCmd = cmdMsg;
        mtkBroadcastCatCmdIntent(cmdMsg);
        broadcastCatCmdIntent(cmdMsg.convertToCatCmdMessage(cmdParams, cmdMsg));
    }

    private void mtkBroadcastCatCmdIntent(CatCmdMessage cmdMsg) {
        Intent intent = new Intent(MtkAppInterface.MTK_CAT_CMD_ACTION);
        intent.putExtra("STK CMD", (Parcelable) cmdMsg);
        intent.putExtra("SLOT_ID", this.mSlotId);
        intent.setComponent(AppInterface.getDefaultSTKApplication());
        MtkCatLog.d(this, "mtkBroadcastCatCmdIntent Sending CmdMsg: " + cmdMsg + " on slotid:" + this.mSlotId);
        this.mContext.sendBroadcast(intent, "android.permission.RECEIVE_STK_COMMANDS");
    }

    protected void sendMenuSelection(int menuId, boolean helpRequired) {
        MtkCatLog.d("CatService", "sendMenuSelection SET_UP_MENU");
        super.sendMenuSelection(menuId, helpRequired);
        cancelTimeOut(15);
        this.isDisplayTextDisabled = false;
    }

    public static CatService getInstance(CommandsInterface ci, Context context, UiccProfile uiccProfile) {
        MtkCatLog.d("CatService", "call getInstance 2");
        int sim_id = 0;
        if (uiccProfile != null) {
            sim_id = uiccProfile.getPhoneId();
            MtkCatLog.d("CatService", "get SIM id from UiccCard. sim id: " + sim_id);
        }
        return CatService.getInstance(ci, context, uiccProfile, sim_id);
    }

    public static MtkAppInterface getInstance() {
        MtkCatLog.d("CatService", "call getInstance 4");
        return (MtkCatService) getInstance(null, null, null, 0);
    }

    public static MtkAppInterface getInstance(int slotId) {
        MtkCatLog.d("CatService", "call getInstance 3");
        return (MtkCatService) getInstance(null, null, null, slotId);
    }

    public void handleMessage(Message msg) {
        MtkCatLog.d(this, "MtkCatservice handleMessage[" + msg.what + "]");
        switch (msg.what) {
            case 1:
            case 2:
            case 3:
            case 5:
                MtkCatLog.d(this, "ril message arrived, slotid:" + this.mSlotId);
                if (msg.obj != null) {
                    AsyncResult ar = (AsyncResult) msg.obj;
                    if (this.mMsgDecoder == null) {
                        MtkCatLog.e(this, "mMsgDecoder == null, return.");
                        return;
                    }
                    if (ar != null && ar.result != null) {
                        try {
                            String data = (String) ar.result;
                            if (data.contains("BIP")) {
                                Intent intent = new Intent(BIP_STATE_CHANGED);
                                intent.putExtra("BIP_CMD", data);
                                intent.putExtra("SLOT_ID", this.mSlotId);
                                intent.setPackage("com.mediatek.engineermode");
                                MtkCatLog.d(this, "Broadcast BIP Intent: Sending data: " + data + " on slotid:" + this.mSlotId);
                                this.mContext.sendBroadcast(intent);
                                return;
                            }
                            this.mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, data));
                            return;
                        } catch (ClassCastException e) {
                        }
                    }
                }
                break;
            case 11:
                handleEventDownload((MtkCatResponseMessage) msg.obj);
                return;
            case 14:
                MtkCatLog.d(this, "[IVSR cancel IVSR flag");
                this.isIvsrBootUp = false;
                return;
        }
        super.handleMessage(msg);
    }

    public synchronized void onCmdResponse(CatResponseMessage resMsg) {
        MtkCatResponseMessage resMtkMsg;
        MtkCatLog.d(this, "MtkCatService onCmdResponse");
        if (resMsg == null) {
            return;
        }
        if (MtkCatResponseMessage.class.isInstance(resMsg)) {
            Message msg = obtainMessage(6, resMsg);
            msg.sendToTarget();
        } else {
            if (this.mCurrntCmd != null) {
                resMtkMsg = new MtkCatResponseMessage(this.mCurrntCmd, resMsg);
            } else {
                resMtkMsg = new MtkCatResponseMessage(MtkCatCmdMessage.getCmdMsg(), resMsg);
            }
            Message msg2 = obtainMessage(6, resMtkMsg);
            msg2.sendToTarget();
        }
    }

    @Override // com.mediatek.internal.telephony.cat.MtkAppInterface
    public synchronized void onEventDownload(MtkCatResponseMessage resMsg) {
        if (resMsg == null) {
            return;
        }
        Message msg = obtainMessage(11, resMsg);
        msg.sendToTarget();
    }

    private void handleEventDownload(MtkCatResponseMessage resMsg) {
        eventDownload(resMsg.mEvent, resMsg.mSourceId, resMsg.mDestinationId, resMsg.mAdditionalInfo, resMsg.mOneShot);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected void handleCmdResponse(CatResponseMessage resMsg) {
        if (!validateResponse(resMsg)) {
        }
        CommandDetails cmdDet = resMsg.getCmdDetails();
        AppInterface.CommandType type = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
        switch (AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$ResultCode[resMsg.mResCode.ordinal()]) {
            case 1:
                if (type != AppInterface.CommandType.SET_UP_CALL || type == AppInterface.CommandType.OPEN_CHANNEL) {
                    this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(resMsg.mUsersConfirm, resMsg.mResCode.value(), null);
                    this.mCurrntCmd = null;
                }
                super.handleCmdResponse(resMsg);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                if (type != AppInterface.CommandType.SET_UP_CALL) {
                }
                this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(resMsg.mUsersConfirm, resMsg.mResCode.value(), null);
                this.mCurrntCmd = null;
                break;
            case 18:
                if (type == AppInterface.CommandType.SET_UP_CALL) {
                    this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(resMsg.mUsersConfirm, resMsg.mResCode.value(), null);
                    this.mCurrntCmd = null;
                } else {
                    if (type == AppInterface.CommandType.DISPLAY_TEXT) {
                        sendTerminalResponse(cmdDet, resMsg.mResCode, resMsg.mIncludeAdditionalInfo, resMsg.mAdditionalInfo, null);
                        this.mCurrntCmd = null;
                    }
                    super.handleCmdResponse(resMsg);
                }
                break;
            case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
                if (type == AppInterface.CommandType.LAUNCH_BROWSER) {
                    if (resMsg.mAdditionalInfo == 0) {
                        resMsg.setAdditionalInfo(4);
                    }
                    sendTerminalResponse(cmdDet, resMsg.mResCode, resMsg.mIncludeAdditionalInfo, resMsg.mAdditionalInfo, null);
                    this.mCurrntCmd = null;
                }
                super.handleCmdResponse(resMsg);
                break;
            default:
                super.handleCmdResponse(resMsg);
                break;
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.cat.MtkCatService$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$ResultCode;

        static {
            int[] iArr = new int[ResultCode.values().length];
            $SwitchMap$com$android$internal$telephony$cat$ResultCode = iArr;
            try {
                iArr[ResultCode.HELP_INFO_REQUIRED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.OK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_PARTIAL_COMPREHENSION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_MISSING_INFO.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_ADDITIONAL_EFS_READ.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_ICON_NOT_DISPLAYED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_MODIFIED_BY_NAA.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_LIMITED_SERVICE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_MODIFICATION.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_NAA_NOT_ACTIVE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_TONE_NOT_PLAYED.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.NO_RESPONSE_FROM_USER.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.UICC_SESSION_TERM_BY_USER.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.BACKWARD_MOVE_BY_USER.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.CMD_DATA_NOT_UNDERSTOOD.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.USER_NOT_ACCEPT.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.NETWORK_CRNTLY_UNABLE_TO_PROCESS.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.LAUNCH_BROWSER_ERROR.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            int[] iArr2 = new int[AppInterface.CommandType.values().length];
            $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType = iArr2;
            try {
                iArr2[AppInterface.CommandType.SET_UP_MENU.ordinal()] = 1;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.DISPLAY_TEXT.ordinal()] = 2;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_IDLE_MODE_TEXT.ordinal()] = 3;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_EVENT_LIST.ordinal()] = 4;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PROVIDE_LOCAL_INFORMATION.ordinal()] = 5;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.LAUNCH_BROWSER.ordinal()] = 6;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SELECT_ITEM.ordinal()] = 7;
            } catch (NoSuchFieldError e26) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INPUT.ordinal()] = 8;
            } catch (NoSuchFieldError e27) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INKEY.ordinal()] = 9;
            } catch (NoSuchFieldError e28) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.REFRESH.ordinal()] = 10;
            } catch (NoSuchFieldError e29) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RUN_AT.ordinal()] = 11;
            } catch (NoSuchFieldError e30) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DTMF.ordinal()] = 12;
            } catch (NoSuchFieldError e31) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SMS.ordinal()] = 13;
            } catch (NoSuchFieldError e32) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SS.ordinal()] = 14;
            } catch (NoSuchFieldError e33) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_USSD.ordinal()] = 15;
            } catch (NoSuchFieldError e34) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PLAY_TONE.ordinal()] = 16;
            } catch (NoSuchFieldError e35) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_CALL.ordinal()] = 17;
            } catch (NoSuchFieldError e36) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.OPEN_CHANNEL.ordinal()] = 18;
            } catch (NoSuchFieldError e37) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.CLOSE_CHANNEL.ordinal()] = 19;
            } catch (NoSuchFieldError e38) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RECEIVE_DATA.ordinal()] = 20;
            } catch (NoSuchFieldError e39) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DATA.ordinal()] = 21;
            } catch (NoSuchFieldError e40) {
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    protected void updateIccAvailability() {
        if (this.mUiccController == null) {
            MtkCatLog.d(this, "updateIccAvailability, mUiccController is null");
            return;
        }
        IccCardStatus.CardState newState = IccCardStatus.CardState.CARDSTATE_ABSENT;
        UiccCard newCard = this.mUiccController.getUiccCard(this.mSlotId);
        if (newCard != null) {
            newState = newCard.getCardState();
        }
        IccCardStatus.CardState oldState = this.mCardState;
        this.mCardState = newState;
        MtkCatLog.d(this, "Slot id: " + this.mSlotId + " New Card State = " + newState + " Old Card State = " + oldState);
        if (oldState == IccCardStatus.CardState.CARDSTATE_PRESENT && newState != IccCardStatus.CardState.CARDSTATE_PRESENT) {
            broadcastCardStateAndIccRefreshResp(newState, null);
            return;
        }
        if (oldState != IccCardStatus.CardState.CARDSTATE_PRESENT && newState == IccCardStatus.CardState.CARDSTATE_PRESENT) {
            if (this.mCmdIf.getRadioState() == 2) {
                MtkCatLog.w(this, "updateIccAvailability(): Radio unavailable");
                this.mCardState = oldState;
            } else {
                MtkCatLog.d(this, "SIM present. Reporting STK service running now...");
                this.mCmdIf.reportStkServiceIsRunning((Message) null);
            }
        }
    }

    private boolean isAlarmBoot() {
        String bootReason = SystemProperties.get("vendor.sys.boot.reason");
        if (bootReason == null || !bootReason.equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return false;
        }
        return DBG;
    }

    @Override // com.mediatek.internal.telephony.cat.MtkAppInterface
    public IccRecords getIccRecords() {
        IccRecords iccRecords;
        synchronized (sInstanceLock) {
            iccRecords = mIccRecords;
        }
        return iccRecords;
    }

    private static void saveCmdToPreference(Context context, String key, String cmd) {
        synchronized (mLock) {
            MtkCatLog.d("MtkCatService", "saveCmdToPreference, key: " + key + ", cmd: " + cmd);
            SharedPreferences preferences = context.getSharedPreferences("set_up_menu", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, cmd);
            editor.apply();
        }
    }

    private static String readCmdFromPreference(MtkCatService inst, Context context, String key) {
        String cmd = String.valueOf("");
        if (inst == null) {
            MtkCatLog.d("MtkCatService", "readCmdFromPreference with null instance");
            return null;
        }
        synchronized (mLock) {
            if (!inst.mReadFromPreferenceDone) {
                SharedPreferences preferences = context.getSharedPreferences("set_up_menu", 0);
                cmd = preferences.getString(key, "");
                inst.mReadFromPreferenceDone = DBG;
                MtkCatLog.d("MtkCatService", "readCmdFromPreference, key: " + key + ", cmd: " + cmd);
            } else {
                MtkCatLog.d("MtkCatService", "readCmdFromPreference, do not read again");
            }
        }
        if (cmd.length() == 0) {
            return null;
        }
        return cmd;
    }

    public static int getBatteryState(Context context) {
        int batteryState = 255;
        IntentFilter filter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batteryStatus = context.registerReceiver(null, filter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra("level", -1);
            int scale = batteryStatus.getIntExtra("scale", -1);
            int status = batteryStatus.getIntExtra("status", -1);
            boolean isCharging = (status == 2 || status == 5) ? DBG : false;
            float batteryPct = level / scale;
            MtkCatLog.d("MtkCatService", " batteryPct == " + batteryPct + "isCharging:" + isCharging);
            if (isCharging) {
                batteryState = 255;
            } else if (batteryPct <= 0.05d) {
                batteryState = 0;
            } else if (batteryPct > 0.05d && batteryPct <= 0.15d) {
                batteryState = 1;
            } else if (batteryPct > 0.15d && batteryPct <= 0.6d) {
                batteryState = 2;
            } else if (batteryPct > 0.6d && batteryPct < 1.0f) {
                batteryState = 3;
            } else if (batteryPct == 1.0f) {
                batteryState = 4;
            }
        }
        MtkCatLog.d("MtkCatService", "getBatteryState() batteryState = " + batteryState);
        return batteryState;
    }

    private boolean isMtkStkAppInstalled() {
        Intent intent = new Intent(MtkAppInterface.MTK_CAT_CMD_ACTION);
        PackageManager pm = this.mContext.getPackageManager();
        List<ResolveInfo> broadcastReceivers = pm.queryBroadcastReceivers(intent, 128);
        int numReceiver = broadcastReceivers == null ? 0 : broadcastReceivers.size();
        if (numReceiver > 0) {
            return DBG;
        }
        return false;
    }

    protected void getInKeyResponse(ByteArrayOutputStream buf, Input cmdInput) {
        int tag = ComprehensionTlvTag.DURATION.value();
        buf.write(tag);
        buf.write(2);
        buf.write(cmdInput.duration.timeUnit.value());
        buf.write(cmdInput.duration.timeInterval);
    }
}
