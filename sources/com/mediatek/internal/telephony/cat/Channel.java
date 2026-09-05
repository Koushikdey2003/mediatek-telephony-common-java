package com.mediatek.internal.telephony.cat;

import android.net.Network;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
abstract class Channel {
    protected static final int SOCKET_TIMEOUT = 3000;
    protected InetAddress mAddress;
    protected BipChannelManager mBipChannelManager;
    protected BipService mBipService;
    protected int mBufferSize;
    protected int mChannelId;
    protected ChannelStatus mChannelStatusData;
    private MtkCatService mHandler;
    protected int mLinkMode;
    protected int mPort;
    protected int mProtocolType;
    protected int mChannelStatus = 0;
    protected byte[] mRxBuffer = null;
    protected byte[] mTxBuffer = null;
    protected int mRxBufferCount = 0;
    protected int mRxBufferOffset = 0;
    protected int mTxBufferCount = 0;
    protected int mRxBufferCacheCount = 0;
    protected ReceiveDataResult mRecvDataRet = null;
    protected int needCopy = 0;
    protected boolean isChannelOpened = false;
    protected boolean isReceiveDataTRSent = false;
    protected Network mNetwork = null;
    private volatile boolean mStop = false;
    protected Object mLock = new Object();

    public abstract int closeChannel();

    public abstract int getTxAvailBufferSize();

    public abstract int openChannel(BipCmdMessage bipCmdMessage, Network network);

    public abstract int receiveData(int i, ReceiveDataResult receiveDataResult);

    public abstract ReceiveDataResult receiveData(int i);

    public abstract int sendData(byte[] bArr, int i);

    Channel(int cid, int linkMode, int protocolType, InetAddress address, int port, int bufferSize, MtkCatService handler, BipService bipManager) {
        this.mChannelId = -1;
        this.mLinkMode = 0;
        this.mProtocolType = 0;
        this.mAddress = null;
        this.mPort = 0;
        this.mHandler = null;
        this.mBipService = null;
        this.mBipChannelManager = null;
        this.mBufferSize = 0;
        this.mChannelStatusData = null;
        this.mChannelId = cid;
        this.mLinkMode = linkMode;
        this.mProtocolType = protocolType;
        this.mAddress = address;
        this.mPort = port;
        this.mBufferSize = bufferSize;
        this.mHandler = handler;
        this.mBipService = bipManager;
        this.mBipChannelManager = bipManager.getBipChannelManager();
        this.mChannelStatusData = new ChannelStatus(cid, 0, 0);
    }

    public void dataAvailable(int bufferSize) {
        if (this.mBipService.mCurrentSetupEventCmd == null) {
            MtkCatLog.e(this, "mCurrentSetupEventCmd is null");
            return;
        }
        if (!this.mBipService.hasPsEvent(9)) {
            MtkCatLog.d(this, "No need to send data available.");
            return;
        }
        MtkCatResponseMessage resMsg = new MtkCatResponseMessage(MtkCatCmdMessage.getCmdMsg(), 9);
        byte[] additionalInfo = new byte[7];
        additionalInfo[0] = -72;
        additionalInfo[1] = 2;
        additionalInfo[2] = (byte) (getChannelId() | this.mChannelStatusData.mChannelStatus);
        additionalInfo[3] = 0;
        additionalInfo[4] = -73;
        additionalInfo[5] = 1;
        if (bufferSize > 255) {
            additionalInfo[6] = -1;
        } else {
            additionalInfo[6] = (byte) bufferSize;
        }
        resMsg.setSourceId(130);
        resMsg.setDestinationId(129);
        resMsg.setEventDownload(9, additionalInfo);
        resMsg.setAdditionalInfo(additionalInfo);
        resMsg.setOneShot(false);
        MtkCatLog.d(this, "onEventDownload for dataAvailable");
        this.mHandler.onEventDownload(resMsg);
    }

    public void changeChannelStatus(byte status) {
        if (this.mBipService.mCurrentSetupEventCmd == null) {
            MtkCatLog.e(this, "mCurrentSetupEventCmd is null");
            return;
        }
        if (!this.mBipService.hasPsEvent(10)) {
            MtkCatLog.d(this, "No need to send channel status.");
            return;
        }
        MtkCatResponseMessage resMsg = new MtkCatResponseMessage(this.mBipService.mCurrentSetupEventCmd, 10);
        MtkCatLog.d("[BIP]", "[Channel]:changeChannelStatus:" + ((int) status));
        byte[] additionalInfo = {-72, 2, (byte) (getChannelId() | status), 0};
        resMsg.setSourceId(130);
        resMsg.setDestinationId(129);
        resMsg.setEventDownload(10, additionalInfo);
        resMsg.setAdditionalInfo(additionalInfo);
        resMsg.setOneShot(false);
        this.mHandler.onEventDownload(resMsg);
    }

    public int getChannelStatus() {
        return this.mChannelStatus;
    }

    public int getChannelId() {
        return this.mChannelId;
    }

    public void clearChannelBuffer(boolean resetBuffer) {
        if (true == resetBuffer) {
            Arrays.fill(this.mRxBuffer, (byte) 0);
            Arrays.fill(this.mTxBuffer, (byte) 0);
        } else {
            this.mRxBuffer = null;
            this.mTxBuffer = null;
        }
        this.mRxBufferCount = 0;
        this.mRxBufferOffset = 0;
        this.mTxBufferCount = 0;
    }

    protected int checkBufferSize() {
        int minBufferSize = 0;
        int maxBufferSize = 0;
        int defaultBufferSize = 0;
        int i = this.mProtocolType;
        if (i == 5 || i == 2 || i == 3 || i == 4 || i == 1) {
            minBufferSize = 255;
            maxBufferSize = 1400;
            defaultBufferSize = 1024;
        }
        MtkCatLog.d("[BIP]", "mBufferSize:" + this.mBufferSize + " minBufferSize:" + minBufferSize + " maxBufferSize:" + maxBufferSize);
        int i2 = this.mBufferSize;
        if (i2 >= minBufferSize && i2 <= maxBufferSize) {
            MtkCatLog.d("[BIP]", "buffer size is normal");
            return 0;
        }
        if (i2 > maxBufferSize) {
            MtkCatLog.d("[BIP]", "buffer size is too large, change it to maximum value");
            this.mBufferSize = maxBufferSize;
        } else {
            MtkCatLog.d("[BIP]", "buffer size is too small, change it to default value");
            this.mBufferSize = defaultBufferSize;
        }
        if (this.mBufferSize < 237) {
            MtkCatLog.d("[BIP]", "buffer size is smaller than 255, change it to MAX_APDU_SIZE");
            this.mBufferSize = BipUtils.MAX_APDU_SIZE;
        }
        return 3;
    }

    protected synchronized void requestStop() {
        this.mStop = true;
        MtkCatLog.d("[BIP]", "requestStop: " + this.mStop);
    }

    protected class UdpReceiverThread implements Runnable {
        DatagramSocket udpSocket;

        UdpReceiverThread(DatagramSocket s) {
            this.udpSocket = s;
        }

        @Override // java.lang.Runnable
        public void run() {
            byte[] localBuffer = new byte[1400];
            MtkCatLog.d("[BIP]", "[UDP]RecTr run");
            DatagramPacket recvPacket = new DatagramPacket(localBuffer, localBuffer.length);
            while (true) {
                try {
                    if (Channel.this.mStop) {
                        break;
                    }
                    MtkCatLog.d("[BIP]", "[UDP]RecTr: Wait data from network");
                    try {
                        Arrays.fill(localBuffer, (byte) 0);
                        this.udpSocket.receive(recvPacket);
                        int recvLen = recvPacket.getLength();
                        MtkCatLog.d("[BIP]", "[UDP]RecTr: recvLen:" + recvLen);
                        if (recvLen >= 0) {
                            synchronized (Channel.this.mLock) {
                                MtkCatLog.d("[BIP]", "[UDP]RecTr: mRxBufferCount:" + Channel.this.mRxBufferCount);
                                if (Channel.this.mRxBufferCount == 0) {
                                    if (recvLen > Channel.this.mBufferSize && Channel.this.mBufferSize < 1024) {
                                        Channel.this.mRxBuffer = new byte[1024];
                                    }
                                    System.arraycopy(localBuffer, 0, Channel.this.mRxBuffer, 0, recvLen);
                                    Channel.this.mRxBufferCount = recvLen;
                                    Channel.this.mRxBufferOffset = 0;
                                    Channel channel = Channel.this;
                                    channel.dataAvailable(channel.mRxBufferCount);
                                    try {
                                        Channel.this.mLock.wait();
                                    } catch (InterruptedException e) {
                                        MtkCatLog.e("[BIP]", "[UDP]RecTr: InterruptedException !!!");
                                        e.printStackTrace();
                                    }
                                } else if (Channel.this.mRxBufferCount > 0) {
                                    do {
                                        Channel channel2 = Channel.this;
                                        channel2.dataAvailable(channel2.mRxBufferCount);
                                        try {
                                            Channel.this.mLock.wait();
                                        } catch (InterruptedException e2) {
                                            MtkCatLog.e("[BIP]", "[UDP]RecTr: InterruptedException !!!");
                                            e2.printStackTrace();
                                        }
                                    } while (Channel.this.mRxBufferCount > 0);
                                    if (recvLen > 0) {
                                        System.arraycopy(localBuffer, 0, Channel.this.mRxBuffer, 0, recvLen);
                                        Channel.this.mRxBufferCount = recvLen;
                                        Channel.this.mRxBufferOffset = 0;
                                        Channel channel3 = Channel.this;
                                        channel3.dataAvailable(channel3.mRxBufferCount);
                                        try {
                                            Channel.this.mLock.wait();
                                        } catch (InterruptedException e3) {
                                            MtkCatLog.e("[BIP]", "[UDP]RecTr: InterruptedException !!!");
                                            e3.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } else {
                            MtkCatLog.e("[BIP]", "[UDP]RecTr: end of file or server is disconnected.");
                            break;
                        }
                    } catch (IOException e4) {
                        MtkCatLog.e("[BIP]", "[UDP]RecTr:read io exception.");
                        Arrays.fill(localBuffer, (byte) 0);
                        Channel.this.mChannelStatusData.mChannelStatus = 0;
                        Channel.this.clearChannelBuffer(false);
                    }
                } catch (Exception e5) {
                    MtkCatLog.d("[BIP]", "[UDP]RecTr:Error.");
                    e5.printStackTrace();
                    return;
                }
            }
            if (Channel.this.mStop) {
                MtkCatLog.d("[BIP]", "[UDP]RecTr: stop");
            }
        }
    }

    protected class TcpReceiverThread implements Runnable {
        DataInputStream di;

        TcpReceiverThread(DataInputStream s) {
            this.di = s;
        }

        @Override // java.lang.Runnable
        public void run() {
            byte[] localBuffer = new byte[1400];
            MtkCatLog.d("[BIP]", "[TCP]RecTr: run");
            while (true) {
                try {
                    if (Channel.this.mStop) {
                        break;
                    }
                    MtkCatLog.d("[BIP]", "[TCP]RecTr: Wait data from network");
                    try {
                        Arrays.fill(localBuffer, (byte) 0);
                        int recvLen = this.di.read(localBuffer);
                        MtkCatLog.d("[BIP]", "[TCP]RecTr: recvLen:" + recvLen);
                        if (recvLen >= 0) {
                            synchronized (Channel.this.mLock) {
                                MtkCatLog.d("[BIP]", "[TCP]RecTr: mRxBufferCount:" + Channel.this.mRxBufferCount);
                                if (Channel.this.mRxBufferCount == 0) {
                                    if (recvLen > Channel.this.mBufferSize && Channel.this.mBufferSize < 1024) {
                                        Channel.this.mRxBuffer = new byte[1024];
                                    }
                                    System.arraycopy(localBuffer, 0, Channel.this.mRxBuffer, 0, recvLen);
                                    Channel.this.mRxBufferCount = recvLen;
                                    Channel.this.mRxBufferOffset = 0;
                                    Channel channel = Channel.this;
                                    channel.dataAvailable(channel.mRxBufferCount);
                                    try {
                                        Channel.this.mLock.wait();
                                    } catch (InterruptedException e) {
                                        MtkCatLog.e("[BIP]", "[TCP]RecTr: InterruptedException !!!");
                                        e.printStackTrace();
                                    }
                                } else if (Channel.this.mRxBufferCount > 0) {
                                    do {
                                        Channel channel2 = Channel.this;
                                        channel2.dataAvailable(channel2.mRxBufferCount);
                                        try {
                                            Channel.this.mLock.wait();
                                        } catch (InterruptedException e2) {
                                            MtkCatLog.e("[BIP]", "[TCP]RecTr: InterruptedException !!!");
                                            e2.printStackTrace();
                                        }
                                    } while (Channel.this.mRxBufferCount > 0);
                                    if (recvLen > 0) {
                                        System.arraycopy(localBuffer, 0, Channel.this.mRxBuffer, 0, recvLen);
                                        Channel.this.mRxBufferCount = recvLen;
                                        Channel.this.mRxBufferOffset = 0;
                                        Channel channel3 = Channel.this;
                                        channel3.dataAvailable(channel3.mRxBufferCount);
                                        try {
                                            Channel.this.mLock.wait();
                                        } catch (InterruptedException e3) {
                                            MtkCatLog.e("[BIP]", "[TCP]RecTr: InterruptedException !!!");
                                            e3.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } else {
                            MtkCatLog.e("[BIP]", "[TCP]RecTr: end of file or server is disconnected.");
                            break;
                        }
                    } catch (IOException e4) {
                        MtkCatLog.e("[BIP]", "[TCP]RecTr:read io exception.");
                        Arrays.fill(localBuffer, (byte) 0);
                        Channel.this.clearChannelBuffer(false);
                    }
                } catch (Exception e5) {
                    MtkCatLog.d("[BIP]", "[TCP]RecTr:Error");
                    e5.printStackTrace();
                    return;
                }
            }
            if (Channel.this.mStop) {
                MtkCatLog.d("[BIP]", "[TCP]RecTr: stop");
            }
        }
    }

    protected class UICCServerThread implements Runnable {
        private static final int RETRY_ACCEPT_SLEEPTIME = 100;
        private static final int RETRY_COUNT = 4;
        TcpServerChannel mTcpServerChannel;
        int mReTryCount = 0;
        DataInputStream di = null;

        UICCServerThread(TcpServerChannel tcpServerChannel) {
            this.mTcpServerChannel = null;
            MtkCatLog.d("[BIP]", "OpenServerSocketThread Init");
            this.mTcpServerChannel = tcpServerChannel;
        }

        /* JADX WARN: Removed duplicated region for block: B:106:0x02f0 A[Catch: IOException -> 0x0305, TryCatch #0 {IOException -> 0x0305, blocks: (B:104:0x02ea, B:106:0x02f0, B:107:0x02f7, B:109:0x02fd), top: B:121:0x02ea }] */
        /* JADX WARN: Removed duplicated region for block: B:109:0x02fd A[Catch: IOException -> 0x0305, TRY_LEAVE, TryCatch #0 {IOException -> 0x0305, blocks: (B:104:0x02ea, B:106:0x02f0, B:107:0x02f7, B:109:0x02fd), top: B:121:0x02ea }] */
        /* JADX WARN: Removed duplicated region for block: B:115:0x0313 A[Catch: IOException -> 0x031b, TRY_LEAVE, TryCatch #8 {IOException -> 0x031b, blocks: (B:113:0x030d, B:115:0x0313), top: B:137:0x030d }] */
        /* JADX WARN: Removed duplicated region for block: B:161:0x02a4 A[EDGE_INSN: B:161:0x02a4->B:91:0x02a4 BREAK  A[LOOP:1: B:25:0x009b->B:63:0x021a], SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:63:0x021a A[LOOP:1: B:25:0x009b->B:63:0x021a, LOOP_END] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instruction units count: 831
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mediatek.internal.telephony.cat.Channel.UICCServerThread.run():void");
        }
    }
}
