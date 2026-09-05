package com.mediatek.internal.telephony.cat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.cat.BerTlv;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.cat.RilMessage;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import com.mediatek.internal.telephony.worldphone.WorldMode;

/* JADX INFO: loaded from: classes.dex */
class BipRilMessageDecoder extends StateMachine {
    private static final int CMD_PARAMS_READY = 2;
    private static final int CMD_START = 1;
    private BipCommandParamsFactory mBipCmdParamsFactory;
    private Handler mCaller;
    private RilMessage mCurrentRilMessage;
    private int mSlotId;
    private StateCmdParamsReady mStateCmdParamsReady;
    private StateStart mStateStart;
    private static int mSimCount = 0;
    private static BipRilMessageDecoder[] mInstance = null;

    public static synchronized BipRilMessageDecoder getInstance(Handler caller, IccFileHandler fh, Context context, int slotId) {
        if (mInstance == null) {
            int simCount = TelephonyManager.getDefault().getSimCount();
            mSimCount = simCount;
            mInstance = new BipRilMessageDecoder[simCount];
            for (int i = 0; i < mSimCount; i++) {
                mInstance[i] = null;
            }
        }
        if (slotId != -1 && slotId < mSimCount) {
            BipRilMessageDecoder[] bipRilMessageDecoderArr = mInstance;
            if (bipRilMessageDecoderArr[slotId] == null) {
                bipRilMessageDecoderArr[slotId] = new BipRilMessageDecoder(caller, fh, context, slotId);
            }
            return mInstance[slotId];
        }
        MtkCatLog.d("BipRilMessageDecoder", "invaild slot id: " + slotId);
        return null;
    }

    public void sendStartDecodingMessageParams(RilMessage rilMsg) {
        Message msg = obtainMessage(1);
        msg.obj = rilMsg;
        sendMessage(msg);
    }

    public void sendMsgParamsDecoded(ResultCode resCode, CommandParams cmdParams) {
        Message msg = obtainMessage(2);
        msg.arg1 = resCode.value();
        msg.obj = cmdParams;
        sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCmdForExecution(RilMessage rilMsg) {
        Message msg = this.mCaller.obtainMessage(20, new RilMessage(rilMsg));
        msg.sendToTarget();
    }

    public int getSlotId() {
        return this.mSlotId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private BipRilMessageDecoder(Handler handler, IccFileHandler iccFileHandler, Context context, int i) {
        super("BipRilMessageDecoder");
        this.mBipCmdParamsFactory = null;
        this.mCurrentRilMessage = null;
        this.mCaller = null;
        this.mStateStart = new StateStart();
        this.mStateCmdParamsReady = new StateCmdParamsReady();
        addState(this.mStateStart);
        addState(this.mStateCmdParamsReady);
        setInitialState(this.mStateStart);
        this.mCaller = handler;
        this.mSlotId = i;
        MtkCatLog.d(this, "mCaller is " + this.mCaller.getClass().getName());
        this.mBipCmdParamsFactory = BipCommandParamsFactory.getInstance(this, iccFileHandler, context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private BipRilMessageDecoder() {
        super("BipRilMessageDecoder");
        this.mBipCmdParamsFactory = null;
        this.mCurrentRilMessage = null;
        this.mCaller = null;
        this.mStateStart = new StateStart();
        this.mStateCmdParamsReady = new StateCmdParamsReady();
    }

    private class StateStart extends State {
        private StateStart() {
        }

        public boolean processMessage(Message msg) {
            if (msg.what == 1) {
                if (BipRilMessageDecoder.this.decodeMessageParams((RilMessage) msg.obj)) {
                    BipRilMessageDecoder bipRilMessageDecoder = BipRilMessageDecoder.this;
                    bipRilMessageDecoder.transitionTo(bipRilMessageDecoder.mStateCmdParamsReady);
                }
            } else {
                MtkCatLog.d(this, "StateStart unexpected expecting START=1 got " + msg.what);
            }
            return true;
        }
    }

    private class StateCmdParamsReady extends State {
        private StateCmdParamsReady() {
        }

        public boolean processMessage(Message msg) {
            if (msg.what == 2) {
                BipRilMessageDecoder.this.mCurrentRilMessage.mResCode = ResultCode.fromInt(msg.arg1);
                BipRilMessageDecoder.this.mCurrentRilMessage.mData = msg.obj;
                BipRilMessageDecoder bipRilMessageDecoder = BipRilMessageDecoder.this;
                bipRilMessageDecoder.sendCmdForExecution(bipRilMessageDecoder.mCurrentRilMessage);
                BipRilMessageDecoder bipRilMessageDecoder2 = BipRilMessageDecoder.this;
                bipRilMessageDecoder2.transitionTo(bipRilMessageDecoder2.mStateStart);
                return true;
            }
            MtkCatLog.d(this, "StateCmdParamsReady expecting CMD_PARAMS_READY=2 got " + msg.what);
            BipRilMessageDecoder.this.deferMessage(msg);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean decodeMessageParams(RilMessage rilMsg) {
        this.mCurrentRilMessage = rilMsg;
        switch (rilMsg.mId) {
            case 18:
            case WorldMode.MD_WORLD_MODE_LTWCG /* 19 */:
                MtkCatLog.d(this, "decodeMessageParams raw: " + ((String) rilMsg.mData));
                try {
                    byte[] rawData = IccUtils.hexStringToBytes((String) rilMsg.mData);
                    try {
                        this.mBipCmdParamsFactory.make(BerTlv.decode(rawData));
                    } catch (ResultException e) {
                        MtkCatLog.d(this, "decodeMessageParams: caught ResultException e=" + e);
                        this.mCurrentRilMessage.mId = 1;
                        this.mCurrentRilMessage.mResCode = e.result();
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
        int i;
        this.mStateStart = null;
        this.mStateCmdParamsReady = null;
        this.mBipCmdParamsFactory.dispose();
        this.mBipCmdParamsFactory = null;
        this.mCurrentRilMessage = null;
        this.mCaller = null;
        BipRilMessageDecoder[] bipRilMessageDecoderArr = mInstance;
        if (bipRilMessageDecoderArr != null) {
            BipRilMessageDecoder bipRilMessageDecoder = bipRilMessageDecoderArr[this.mSlotId];
            if (bipRilMessageDecoder != null) {
                bipRilMessageDecoder.quit();
                mInstance[this.mSlotId] = null;
            }
            int i2 = 0;
            while (true) {
                i = mSimCount;
                if (i2 >= i || mInstance[i2] != null) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 == i) {
                mInstance = null;
            }
        }
    }
}
