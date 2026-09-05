package com.mediatek.internal.telephony.cat;

import android.net.Network;
import com.mediatek.internal.telephony.cat.Channel;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/* JADX INFO: compiled from: Channel.java */
/* JADX INFO: loaded from: classes.dex */
class TcpChannel extends Channel {
    private static final int TCP_CONN_TIMEOUT = 15000;
    DataInputStream mInput;
    BufferedOutputStream mOutput;
    Socket mSocket;
    Thread rt;

    TcpChannel(int cid, int linkMode, int protocolType, InetAddress address, int port, int bufferSize, MtkCatService handler, BipService bipManager) {
        super(cid, linkMode, protocolType, address, port, bufferSize, handler, bipManager);
        this.mSocket = null;
        this.mInput = null;
        this.mOutput = null;
    }

    @Override // com.mediatek.internal.telephony.cat.Channel
    public int openChannel(BipCmdMessage cmdMsg, Network network) {
        this.mNetwork = network;
        if (this.mLinkMode == 0) {
            Thread t_openChannelThread = new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.cat.TcpChannel.1
                @Override // java.lang.Runnable
                public synchronized void run() {
                    MtkCatLog.d("[BIP]", "[TCP]running TCP channel thread");
                    try {
                        TcpChannel tcpChannel = TcpChannel.this;
                        tcpChannel.mSocket = tcpChannel.mNetwork.getSocketFactory().createSocket();
                        TcpChannel.this.mSocket.setSoLinger(false, 0);
                        InetSocketAddress socketAddress = new InetSocketAddress(TcpChannel.this.mAddress, TcpChannel.this.mPort);
                        try {
                            TcpChannel.this.mSocket.connect(socketAddress, TcpChannel.TCP_CONN_TIMEOUT);
                        } catch (SocketTimeoutException e3) {
                            MtkCatLog.d("[BIP]", "[TCP]Time out of connect " + e3 + ":" + TcpChannel.TCP_CONN_TIMEOUT + " sec");
                            TcpChannel.this.mChannelStatus = 7;
                            if (TcpChannel.this.mBipService.mIsOpenChannelOverWifi) {
                                TcpChannel.this.mBipService.mIsConnectTimeout = true;
                                TcpChannel.this.mBipService.mIsOpenChannelOverWifi = false;
                            }
                        }
                        if (TcpChannel.this.mSocket.isConnected()) {
                            TcpChannel.this.mChannelStatus = 4;
                            TcpChannel.this.mChannelStatusData.mChannelStatus = 128;
                        } else {
                            MtkCatLog.e("[BIP]", "[TCP]socket is not connected.");
                            TcpChannel.this.mChannelStatus = 7;
                            TcpChannel.this.mSocket.close();
                        }
                    } catch (IOException e) {
                        MtkCatLog.e("[BIP]", "[TCP]Fail to create socket");
                        e.printStackTrace();
                        TcpChannel.this.mChannelStatus = 7;
                        if (TcpChannel.this.mBipService.mIsOpenChannelOverWifi) {
                            TcpChannel.this.mBipService.mIsConnectTimeout = true;
                            TcpChannel.this.mBipService.mIsOpenChannelOverWifi = false;
                        }
                    } catch (NullPointerException e2) {
                        MtkCatLog.e("[BIP]", "[TCP]Null pointer tcp socket " + e2);
                        TcpChannel.this.mChannelStatus = 7;
                    }
                    TcpChannel.this.onOpenChannelCompleted();
                }
            });
            t_openChannelThread.start();
            return 10;
        }
        if (this.mLinkMode != 1) {
            return 0;
        }
        Thread t_openChannelThread2 = new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.cat.TcpChannel.2
            @Override // java.lang.Runnable
            public synchronized void run() {
                MtkCatLog.d("[BIP]", "[TCP]running TCP channel thread");
                try {
                    TcpChannel.this.mSocket = new Socket();
                    TcpChannel.this.mSocket.setSoLinger(false, 0);
                    TcpChannel.this.mSocket.setSoTimeout(TcpChannel.TCP_CONN_TIMEOUT);
                } catch (SocketException e) {
                    MtkCatLog.d("[BIP]", "[TCP]Fail to create tcp socket");
                    TcpChannel.this.mChannelStatus = 7;
                }
            }
        });
        t_openChannelThread2.start();
        this.mChannelStatus = 4;
        int ret = checkBufferSize();
        if (ret == 3) {
            MtkCatLog.d("[BIP]", "[TCP]openChannel: buffer size is modified");
            cmdMsg.mBufferSize = this.mBufferSize;
        }
        this.mRxBuffer = new byte[this.mBufferSize];
        this.mTxBuffer = new byte[this.mBufferSize];
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOpenChannelCompleted() {
        int ret;
        if (this.mChannelStatus == 4) {
            try {
                MtkCatLog.d("[BIP]", "[TCP]stream is open");
                this.mInput = new DataInputStream(this.mSocket.getInputStream());
                this.mOutput = new BufferedOutputStream(this.mSocket.getOutputStream());
                Thread thread = new Thread(new Channel.TcpReceiverThread(this.mInput));
                this.rt = thread;
                thread.start();
                ret = checkBufferSize();
                this.mRxBuffer = new byte[this.mBufferSize];
                this.mTxBuffer = new byte[this.mBufferSize];
            } catch (IOException e) {
                MtkCatLog.d("[BIP]", "[TCP]Fail to create data stream");
                e.printStackTrace();
                ret = 5;
            }
        } else {
            MtkCatLog.d("[BIP]", "[TCP]socket is not open");
            ret = 5;
        }
        this.mBipService.openChannelCompleted(ret, this);
    }

    @Override // com.mediatek.internal.telephony.cat.Channel
    public int closeChannel() {
        MtkCatLog.d("[BIP]", "[TCP]closeChannel.");
        if (this.rt != null) {
            requestStop();
            this.rt = null;
        }
        Thread closeChannelThread = new Thread(new Runnable() { // from class: com.mediatek.internal.telephony.cat.TcpChannel.3
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v0 */
            /* JADX WARN: Type inference failed for: r1v1 */
            /* JADX WARN: Type inference failed for: r1v2, types: [byte[], java.net.Socket] */
            /* JADX WARN: Type inference failed for: r1v4 */
            /* JADX WARN: Type inference failed for: r1v7 */
            /* JADX WARN: Type inference failed for: r1v8 */
            /* JADX WARN: Type inference failed for: r1v9 */
            @Override // java.lang.Runnable
            public synchronized void run() {
                ?? r1 = 0;
                r1 = 0;
                try {
                    try {
                        if (TcpChannel.this.mInput != null) {
                            try {
                                TcpChannel.this.mInput.close();
                            } catch (IOException e) {
                                MtkCatLog.e("[BIP]", "[TCP]closeChannel - IOE");
                                TcpChannel.this.mSocket = null;
                                TcpChannel.this.mRxBuffer = null;
                                TcpChannel.this.mTxBuffer = null;
                                TcpChannel tcpChannel = TcpChannel.this;
                                tcpChannel.mChannelStatus = 2;
                                r1 = tcpChannel;
                            } catch (Throwable th) {
                                th = th;
                                TcpChannel.this.mSocket = r1;
                                TcpChannel.this.mRxBuffer = r1;
                                TcpChannel.this.mTxBuffer = r1;
                                TcpChannel.this.mChannelStatus = 2;
                                throw th;
                            }
                        }
                        if (TcpChannel.this.mOutput != null) {
                            TcpChannel.this.mOutput.close();
                        }
                        if (TcpChannel.this.mSocket != null) {
                            TcpChannel.this.mSocket.close();
                        }
                        TcpChannel.this.mSocket = null;
                        TcpChannel.this.mRxBuffer = null;
                        TcpChannel.this.mTxBuffer = null;
                        TcpChannel tcpChannel2 = TcpChannel.this;
                        tcpChannel2.mChannelStatus = 2;
                        r1 = tcpChannel2;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (IOException e2) {
                }
            }
        });
        closeChannelThread.start();
        return 0;
    }

    @Override // com.mediatek.internal.telephony.cat.Channel
    public ReceiveDataResult receiveData(int requestCount) {
        ReceiveDataResult ret = new ReceiveDataResult();
        ret.buffer = new byte[requestCount];
        MtkCatLog.d("[BIP]", "[TCP]receiveData " + this.mRxBufferCount + "/" + requestCount + "/" + this.mRxBufferOffset);
        if (this.mRxBufferCount >= requestCount) {
            try {
                MtkCatLog.d("[BIP]", "[TCP]Start to copy data from buffer");
                System.arraycopy(this.mRxBuffer, this.mRxBufferOffset, ret.buffer, 0, requestCount);
                this.mRxBufferCount -= requestCount;
                this.mRxBufferOffset += requestCount;
                ret.remainingCount = this.mRxBufferCount;
            } catch (IndexOutOfBoundsException e) {
            }
        } else {
            int needCopy = requestCount;
            int canCopy = this.mRxBufferCount;
            int countCopied = 0;
            boolean canExitLoop = false;
            while (!canExitLoop) {
                if (needCopy > canCopy) {
                    try {
                        System.arraycopy(this.mRxBuffer, this.mRxBufferOffset, ret.buffer, countCopied, canCopy);
                        this.mRxBufferOffset += canCopy;
                        this.mRxBufferCount -= canCopy;
                        countCopied += canCopy;
                        needCopy -= canCopy;
                    } catch (IndexOutOfBoundsException e2) {
                    }
                } else {
                    try {
                        System.arraycopy(Integer.valueOf(this.mRxBufferCount), this.mRxBufferOffset, ret.buffer, countCopied, canCopy);
                        this.mRxBufferOffset += needCopy;
                        countCopied += needCopy;
                        needCopy = 0;
                    } catch (IndexOutOfBoundsException e3) {
                    }
                }
                if (needCopy == 0) {
                    canExitLoop = true;
                } else {
                    try {
                        int count = this.mInput.read(this.mRxBuffer, 0, this.mRxBuffer.length);
                        this.mRxBufferCount = count;
                        this.mRxBufferOffset = 0;
                    } catch (IOException e4) {
                        MtkCatLog.e("[BIP]", "[TCP]receiveData - IOE");
                    }
                }
            }
        }
        return ret;
    }

    @Override // com.mediatek.internal.telephony.cat.Channel
    public int sendData(byte[] data, int mode) {
        byte[] tmpBuffer;
        if (data == null) {
            MtkCatLog.e("[BIP]", "[TCP]sendData - data null:");
            return 5;
        }
        if (this.mTxBuffer == null) {
            MtkCatLog.e("[BIP]", "[TCP]sendData - mTxBuffer null:");
            return 5;
        }
        int txRemaining = this.mTxBuffer.length - this.mTxBufferCount;
        try {
            MtkCatLog.d("[BIP]", "[TCP]sendData: size of data:" + data.length + " mode:" + mode);
            MtkCatLog.d("[BIP]", "[TCP]sendData: size of buffer:" + this.mTxBuffer.length + " count:" + this.mTxBufferCount);
            if (this.mTxBufferCount == 0 && 1 == mode) {
                tmpBuffer = data;
                this.mTxBufferCount = data.length;
            } else {
                try {
                    if (txRemaining >= data.length) {
                        System.arraycopy(data, 0, this.mTxBuffer, this.mTxBufferCount, data.length);
                        this.mTxBufferCount += data.length;
                    } else {
                        MtkCatLog.d("[BIP]", "[TCP]sendData - tx buffer is not enough");
                    }
                    tmpBuffer = this.mTxBuffer;
                } catch (IndexOutOfBoundsException e) {
                    return 5;
                }
            }
            if (mode != 1 || this.mChannelStatus != 4) {
                return 0;
            }
            try {
                MtkCatLog.d("[BIP]", "[TCP]SEND_DATA_MODE_IMMEDIATE:" + this.mTxBuffer.length + " count:" + this.mTxBufferCount);
                this.mOutput.write(tmpBuffer, 0, this.mTxBufferCount);
                this.mOutput.flush();
                this.mTxBufferCount = 0;
                return 0;
            } catch (IOException e2) {
                MtkCatLog.e("[BIP]", "[TCP]sendData - Exception");
                e2.printStackTrace();
                return 5;
            }
        } catch (NullPointerException ne) {
            MtkCatLog.d("[BIP]", "[UDP]sendData NE");
            ne.printStackTrace();
            return 5;
        }
    }

    @Override // com.mediatek.internal.telephony.cat.Channel
    public int getTxAvailBufferSize() {
        if (this.mTxBuffer == null) {
            MtkCatLog.e("[BIP]", "[TCP]getTxAvailBufferSize - mTxBuffer null:");
            return 0;
        }
        int txRemaining = this.mTxBuffer.length - this.mTxBufferCount;
        MtkCatLog.d("[BIP]", "[TCP]available tx buffer size:" + txRemaining);
        return txRemaining;
    }

    @Override // com.mediatek.internal.telephony.cat.Channel
    public int receiveData(int requestSize, ReceiveDataResult rdr) {
        MtkCatLog.d("[BIP]", "[TCP]new receiveData method");
        if (rdr == null) {
            MtkCatLog.e("[BIP]", "[TCP]rdr is null");
            return 5;
        }
        MtkCatLog.d("[BIP]", "[TCP]receiveData mRxBufferCount:" + this.mRxBufferCount + " requestSize: " + requestSize + " mRxBufferOffset:" + this.mRxBufferOffset);
        rdr.buffer = new byte[requestSize];
        if (this.mRxBufferCount >= requestSize) {
            try {
                synchronized (this.mLock) {
                    if (this.mRxBuffer != null && rdr.buffer != null) {
                        System.arraycopy(this.mRxBuffer, this.mRxBufferOffset, rdr.buffer, 0, requestSize);
                        this.mRxBufferOffset += requestSize;
                        this.mRxBufferCount -= requestSize;
                        if (this.mRxBufferCount == 0) {
                            this.mRxBufferOffset = 0;
                        }
                        rdr.remainingCount = this.mRxBufferCount;
                        return 0;
                    }
                    MtkCatLog.d("[BIP]", "[TCP]mRxBuffer or rdr.buffer is null 1");
                    return 5;
                }
            } catch (IndexOutOfBoundsException e) {
                MtkCatLog.e("[BIP]", "[TCP]fail copy rx buffer out 1");
                return 5;
            }
        }
        MtkCatLog.e("[BIP]", "[TCP]rx buffer is insufficient !!!");
        try {
            synchronized (this.mLock) {
                if (this.mRxBuffer != null && rdr.buffer != null) {
                    System.arraycopy(this.mRxBuffer, this.mRxBufferOffset, rdr.buffer, 0, this.mRxBufferCount);
                    this.mRxBufferOffset = 0;
                    this.mRxBufferCount = 0;
                    this.mLock.notify();
                    rdr.remainingCount = 0;
                    return 9;
                }
                MtkCatLog.d("[BIP]", "[TCP]mRxBuffer or rdr.buffer is null 2");
                return 5;
            }
        } catch (IndexOutOfBoundsException e2) {
            MtkCatLog.e("[BIP]", "[TCP]fail copy rx buffer out 2");
            return 5;
        }
    }
}
