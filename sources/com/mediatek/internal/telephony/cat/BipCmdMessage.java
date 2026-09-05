package com.mediatek.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.SetEventListParams;
import com.android.internal.telephony.cat.TextMessage;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BipCmdMessage implements Parcelable {
    public static final Parcelable.Creator<BipCmdMessage> CREATOR = new Parcelable.Creator<BipCmdMessage>() { // from class: com.mediatek.internal.telephony.cat.BipCmdMessage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BipCmdMessage createFromParcel(Parcel in) {
            return new BipCmdMessage(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BipCmdMessage[] newArray(int size) {
            return new BipCmdMessage[size];
        }
    };
    public String mApn;
    public BearerDesc mBearerDesc;
    public int mBufferSize;
    public byte[] mChannelData;
    public int mChannelDataLength;
    public ChannelStatus mChannelStatusData;
    public List<ChannelStatus> mChannelStatusList;
    public boolean mCloseBackToTcpListen;
    public int mCloseCid;
    CommandDetails mCmdDet;
    public OtherAddress mDataDestinationAddress;
    public String mDestAddress;
    public DnsServerAddress mDnsServerAddress;
    public int mInfoType;
    public OtherAddress mLocalAddress;
    public String mLogin;
    public String mPwd;
    public int mReceiveDataCid;
    public int mRemainingDataLength;
    public int mSendDataCid;
    public int mSendMode;
    private SetupEventListSettings mSetupEventListSettings;
    private TextMessage mTextMsg;
    public TransportProtocol mTransportProtocol;

    public class SetupEventListSettings {
        public int[] eventList;

        public SetupEventListSettings() {
        }
    }

    BipCmdMessage(CommandParams cmdParams) {
        this.mBearerDesc = null;
        this.mBufferSize = 0;
        this.mLocalAddress = null;
        this.mDnsServerAddress = null;
        this.mTransportProtocol = null;
        this.mDataDestinationAddress = null;
        this.mApn = null;
        this.mLogin = null;
        this.mPwd = null;
        this.mChannelDataLength = 0;
        this.mRemainingDataLength = 0;
        this.mChannelData = null;
        this.mChannelStatusData = null;
        this.mCloseCid = 0;
        this.mSendDataCid = 0;
        this.mReceiveDataCid = 0;
        this.mCloseBackToTcpListen = false;
        this.mSendMode = 0;
        this.mChannelStatusList = null;
        this.mInfoType = 0;
        this.mDestAddress = null;
        this.mSetupEventListSettings = null;
        this.mCmdDet = cmdParams.mCmdDet;
        if (getCmdType() == null) {
            MtkCatLog.e("[BIP]", "cmd type is null!");
        }
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()]) {
            case 1:
                this.mTextMsg = ((GetChannelStatusParams) cmdParams).textMsg;
                break;
            case 2:
                this.mBearerDesc = ((OpenChannelParams) cmdParams).bearerDesc;
                this.mBufferSize = ((OpenChannelParams) cmdParams).bufferSize;
                this.mLocalAddress = ((OpenChannelParams) cmdParams).localAddress;
                this.mTransportProtocol = ((OpenChannelParams) cmdParams).transportProtocol;
                this.mDataDestinationAddress = ((OpenChannelParams) cmdParams).dataDestinationAddress;
                this.mTextMsg = ((OpenChannelParams) cmdParams).textMsg;
                BearerDesc bearerDesc = this.mBearerDesc;
                if (bearerDesc != null) {
                    if (bearerDesc.bearerType == 2 || this.mBearerDesc.bearerType == 3 || this.mBearerDesc.bearerType == 9 || this.mBearerDesc.bearerType == 11) {
                        this.mApn = ((OpenChannelParams) cmdParams).gprsParams.accessPointName;
                        this.mLogin = ((OpenChannelParams) cmdParams).gprsParams.userLogin;
                        this.mPwd = ((OpenChannelParams) cmdParams).gprsParams.userPwd;
                    }
                } else {
                    MtkCatLog.d("[BIP]", "Invalid BearerDesc object");
                }
                break;
            case 3:
                this.mTextMsg = ((CloseChannelParams) cmdParams).textMsg;
                this.mCloseCid = ((CloseChannelParams) cmdParams).mCloseCid;
                this.mCloseBackToTcpListen = ((CloseChannelParams) cmdParams).mBackToTcpListen;
                break;
            case 4:
                this.mTextMsg = ((ReceiveDataParams) cmdParams).textMsg;
                this.mChannelDataLength = ((ReceiveDataParams) cmdParams).channelDataLength;
                this.mReceiveDataCid = ((ReceiveDataParams) cmdParams).mReceiveDataCid;
                break;
            case 5:
                this.mTextMsg = ((SendDataParams) cmdParams).textMsg;
                this.mChannelData = ((SendDataParams) cmdParams).channelData;
                this.mSendDataCid = ((SendDataParams) cmdParams).mSendDataCid;
                this.mSendMode = ((SendDataParams) cmdParams).mSendMode;
                break;
            case 6:
                SetupEventListSettings setupEventListSettings = new SetupEventListSettings();
                this.mSetupEventListSettings = setupEventListSettings;
                setupEventListSettings.eventList = ((SetEventListParams) cmdParams).mEventInfo;
                break;
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.cat.BipCmdMessage$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType;

        static {
            int[] iArr = new int[AppInterface.CommandType.values().length];
            $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType = iArr;
            try {
                iArr[AppInterface.CommandType.GET_CHANNEL_STATUS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.OPEN_CHANNEL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.CLOSE_CHANNEL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RECEIVE_DATA.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DATA.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_EVENT_LIST.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public BipCmdMessage(Parcel in) {
        this.mBearerDesc = null;
        this.mBufferSize = 0;
        this.mLocalAddress = null;
        this.mDnsServerAddress = null;
        this.mTransportProtocol = null;
        this.mDataDestinationAddress = null;
        this.mApn = null;
        this.mLogin = null;
        this.mPwd = null;
        this.mChannelDataLength = 0;
        this.mRemainingDataLength = 0;
        this.mChannelData = null;
        this.mChannelStatusData = null;
        this.mCloseCid = 0;
        this.mSendDataCid = 0;
        this.mReceiveDataCid = 0;
        this.mCloseBackToTcpListen = false;
        this.mSendMode = 0;
        this.mChannelStatusList = null;
        this.mInfoType = 0;
        this.mDestAddress = null;
        this.mSetupEventListSettings = null;
        this.mCmdDet = in.readParcelable(null);
        this.mTextMsg = in.readParcelable(null);
        if (getCmdType() == null) {
            MtkCatLog.e("[BIP]", "cmd type is null");
        }
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()]) {
            case 2:
                this.mBearerDesc = (BearerDesc) in.readParcelable(null);
                break;
            case 6:
                this.mSetupEventListSettings = new SetupEventListSettings();
                int length = in.readInt();
                this.mSetupEventListSettings.eventList = new int[length];
                for (int i = 0; i < length; i++) {
                    this.mSetupEventListSettings.eventList[i] = in.readInt();
                }
                break;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mCmdDet, 0);
        dest.writeParcelable(this.mTextMsg, 0);
        if (getCmdType() == null) {
            MtkCatLog.e("[BIP]", "cmd type is null");
        }
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()]) {
            case 2:
                dest.writeParcelable(this.mBearerDesc, 0);
                break;
            case 6:
                dest.writeIntArray(this.mSetupEventListSettings.eventList);
                break;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getCmdQualifier() {
        return this.mCmdDet.commandQualifier;
    }

    public AppInterface.CommandType getCmdType() {
        return AppInterface.CommandType.fromInt(this.mCmdDet.typeOfCommand);
    }

    public BearerDesc getBearerDesc() {
        return this.mBearerDesc;
    }
}
