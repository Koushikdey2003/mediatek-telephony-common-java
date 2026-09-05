package com.mediatek.internal.telephony;

import android.content.Context;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class MtkEmbmsAdaptor {
    private static final int MSG_ID_EVENT_IND = 2;
    private static final int MSG_ID_EVENT_REQUEST = 0;
    private static final int MSG_ID_EVENT_RESPONSE = 1;
    private static final String TAG = "MtkEmbmsAdaptor";
    private static MtkEmbmsAdaptor sInstance = null;
    private Context mContext;
    private MtkEmbmsAdaptEventHandler mEventHandler;
    private SubscriptionManagerService mSubscriptionManagerService;

    /* JADX WARN: Type inference failed for: r0v4, types: [com.mediatek.internal.telephony.MtkEmbmsAdaptor$1] */
    private MtkEmbmsAdaptor(Context c, CommandsInterface[] ci) {
        this.mEventHandler = null;
        this.mContext = null;
        Rlog.i(TAG, "construtor 2 parameter is called - start");
        this.mContext = c;
        MtkEmbmsAdaptEventHandler mtkEmbmsAdaptEventHandler = new MtkEmbmsAdaptEventHandler();
        this.mEventHandler = mtkEmbmsAdaptEventHandler;
        mtkEmbmsAdaptEventHandler.setRil(c, ci);
        this.mSubscriptionManagerService = SubscriptionManagerService.getInstance();
        new Thread() { // from class: com.mediatek.internal.telephony.MtkEmbmsAdaptor.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                ServerTask server = MtkEmbmsAdaptor.this.new ServerTask();
                server.listenConnection(MtkEmbmsAdaptor.this.mEventHandler);
            }
        }.start();
        int numPhones = TelephonyManager.getDefault().getPhoneCount();
        for (int i = 0; i < numPhones; i++) {
            MtkRIL mci = (MtkRIL) ci[i];
            mci.setAtInfoNotification(this.mEventHandler, 2, Integer.valueOf(i));
        }
        Rlog.i(TAG, "construtor is called - end");
    }

    public static MtkEmbmsAdaptor getDefault(Context context, CommandsInterface[] ci) {
        Rlog.d(TAG, "getDefault()");
        if (sInstance == null) {
            sInstance = new MtkEmbmsAdaptor(context, ci);
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String messageToString(Message msg) {
        switch (msg.what) {
            case 0:
                return "MSG_ID_EVENT_REQUEST";
            case 1:
                return "MSG_ID_EVENT_RESPONSE";
            case 2:
                return "MSG_ID_EVENT_IND";
            default:
                return "UNKNOWN";
        }
    }

    public class ServerTask {
        public static final String HOST_NAME = "/dev/socket/embmsd";

        public ServerTask() {
        }

        public void listenConnection(MtkEmbmsAdaptEventHandler eventHandler) {
            Rlog.i(MtkEmbmsAdaptor.TAG, "listenConnection() - start");
            LocalServerSocket serverSocket = null;
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            try {
                try {
                    try {
                        try {
                            serverSocket = new LocalServerSocket(HOST_NAME);
                            while (true) {
                                LocalSocket socket = serverSocket.accept();
                                int clientUid = socket.getPeerCredentials().getUid();
                                String clientPackageName = MtkEmbmsAdaptor.this.mContext.getPackageManager().getNameForUid(clientUid);
                                Rlog.i(MtkEmbmsAdaptor.TAG, "client's uid = " + clientUid + " ,packagename = " + clientPackageName);
                                SigningInfo signingInfo = MtkEmbmsAdaptor.this.mContext.getPackageManager().getPackageInfo(clientPackageName, 134217728).signingInfo;
                                Signature[] sigs = signingInfo.getApkContentsSigners();
                                if (MtkEmbmsUtils.getInstance().isCertifiedMiddleware(sigs)) {
                                    Rlog.d(MtkEmbmsAdaptor.TAG, "There is a client is accepted: " + socket.toString());
                                    threadExecutor.execute(MtkEmbmsAdaptor.this.new ConnectionHandler(socket, eventHandler));
                                } else {
                                    Rlog.d(MtkEmbmsAdaptor.TAG, "The client is not certified " + socket.toString());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Rlog.i(MtkEmbmsAdaptor.TAG, "listenConnection() - end");
                        }
                    } catch (IOException e2) {
                        Rlog.e(MtkEmbmsAdaptor.TAG, "listenConnection catch IOException");
                        e2.printStackTrace();
                        Rlog.d(MtkEmbmsAdaptor.TAG, "listenConnection finally!!");
                        if (threadExecutor != null) {
                            threadExecutor.shutdown();
                        }
                        if (serverSocket != null) {
                            serverSocket.close();
                        }
                        Rlog.i(MtkEmbmsAdaptor.TAG, "listenConnection() - end");
                    }
                } catch (Exception e3) {
                    Rlog.e(MtkEmbmsAdaptor.TAG, "listenConnection catch Exception");
                    e3.printStackTrace();
                    Rlog.d(MtkEmbmsAdaptor.TAG, "listenConnection finally!!");
                    if (threadExecutor != null) {
                        threadExecutor.shutdown();
                    }
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                    Rlog.i(MtkEmbmsAdaptor.TAG, "listenConnection() - end");
                }
            } catch (Throwable th) {
                Rlog.d(MtkEmbmsAdaptor.TAG, "listenConnection finally!!");
                if (threadExecutor != null) {
                    threadExecutor.shutdown();
                }
                if (0 != 0) {
                    try {
                        serverSocket.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    public class ConnectionHandler implements Runnable {
        private MtkEmbmsAdaptEventHandler mEventHandler;
        private LocalSocket mSocket;

        public ConnectionHandler(LocalSocket clientSocket, MtkEmbmsAdaptEventHandler eventHandler) {
            this.mSocket = clientSocket;
            this.mEventHandler = eventHandler;
        }

        @Override // java.lang.Runnable
        public void run() {
            Rlog.i(MtkEmbmsAdaptor.TAG, "New connection: " + this.mSocket.toString());
            try {
                MtkEmbmsAdaptIoThread ioThread = MtkEmbmsAdaptor.this.new MtkEmbmsAdaptIoThread(ServerTask.HOST_NAME, this.mSocket.getInputStream(), this.mSocket.getOutputStream(), this.mEventHandler);
                this.mEventHandler.setDataStream(ioThread);
                ioThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class MtkEmbmsAdaptIoThread extends Thread {
        private static final int MAX_DATA_LENGTH = 4096;
        private MtkEmbmsAdaptEventHandler mEventHandler;
        private InputStream mInput;
        private String mName;
        private OutputStream mOutput;
        private byte[] readBuffer;
        private boolean mIsContinue = true;
        private final Object mOutputLock = new Object();

        public MtkEmbmsAdaptIoThread(String name, InputStream inputStream, OutputStream outputStream, MtkEmbmsAdaptEventHandler eventHandler) {
            this.mName = "";
            this.mInput = null;
            this.mOutput = null;
            this.mEventHandler = null;
            this.readBuffer = null;
            this.mName = name;
            this.mInput = inputStream;
            this.mOutput = outputStream;
            this.mEventHandler = eventHandler;
            Rlog.i(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptIoThread constructor is called.");
            this.readBuffer = new byte[4096];
        }

        public void terminate() {
            Rlog.i(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptIoThread terminate.");
            this.mIsContinue = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Rlog.i(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptIoThread running.");
            while (this.mIsContinue) {
                try {
                    try {
                        int count = this.mInput.read(this.readBuffer, 0, 4096);
                        if (count < 0) {
                            Rlog.e(MtkEmbmsAdaptor.TAG, "readEvent(), fail to read and throw exception");
                            return;
                        } else if (count > 0) {
                            try {
                                handleInput(new String(this.readBuffer, 0, count));
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        Rlog.e(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptIoThread Exception.");
                        e.printStackTrace();
                    }
                } catch (IOException e2) {
                    Rlog.e(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptIoThread IOException.");
                    e2.printStackTrace();
                    Rlog.e(MtkEmbmsAdaptor.TAG, "Socket disconnected.");
                    terminate();
                }
            }
        }

        protected void handleInput(String input) {
            Rlog.d(MtkEmbmsAdaptor.TAG, "process input: RCV <-(" + input + "),length:" + input.length());
            MtkEmbmsAdaptEventHandler mtkEmbmsAdaptEventHandler = this.mEventHandler;
            mtkEmbmsAdaptEventHandler.sendMessage(mtkEmbmsAdaptEventHandler.obtainMessage(0, input.trim()));
        }

        public void sendCommand(String rawCmd) {
            Rlog.d(MtkEmbmsAdaptor.TAG, "SND -> (" + rawCmd + ")");
            synchronized (this.mOutputLock) {
                OutputStream outputStream = this.mOutput;
                if (outputStream == null) {
                    Rlog.e(MtkEmbmsAdaptor.TAG, "missing SIM output stream");
                } else {
                    try {
                        outputStream.write(rawCmd.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class MtkEmbmsAdaptEventHandler extends Handler {
        private MtkEmbmsAdaptIoThread mAdaptorIoThread = null;
        private CommandsInterface[] mCis;
        private Context mContext;

        public MtkEmbmsAdaptEventHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Rlog.d(MtkEmbmsAdaptor.TAG, "handleMessage: " + MtkEmbmsAdaptor.this.messageToString(msg) + " = " + msg);
            switch (msg.what) {
                case 0:
                    String data = (String) msg.obj;
                    Rlog.i(MtkEmbmsAdaptor.TAG, "MSG_ID_EVENT_REQUEST data: " + data);
                    int subId = MtkEmbmsAdaptor.this.mSubscriptionManagerService.getDefaultDataSubId();
                    int slotId = -1;
                    if (subId == -1) {
                        Rlog.e(MtkEmbmsAdaptor.TAG, "getDefaultDataSubId fail: " + subId);
                    } else {
                        MtkEmbmsAdaptor.this.mSubscriptionManagerService.getSlotIndex(subId);
                        slotId = 0;
                    }
                    if (!SubscriptionManager.isValidSlotIndex(slotId)) {
                        Rlog.e(MtkEmbmsAdaptor.TAG, "inValidSlotIndex:" + slotId);
                        sendFailureCmd();
                    } else {
                        Message onCompleted = obtainMessage(1, Integer.valueOf(slotId));
                        MtkRIL ci = this.mCis[slotId];
                        ci.sendEmbmsAtCommand(data, onCompleted);
                    }
                    break;
                case 1:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    ((Integer) ar.userObj).intValue();
                    String data2 = (String) ar.result;
                    Rlog.i(MtkEmbmsAdaptor.TAG, "MSG_ID_EVENT_RESPONSE data: " + data2);
                    if ((ar.exception instanceof CommandException) && ar.exception.getCommandError() == CommandException.Error.RADIO_NOT_AVAILABLE) {
                        Rlog.e(MtkEmbmsAdaptor.TAG, "MSG_ID_EVENT_RESPONSE exception: " + ar.exception.getCommandError());
                        sendFailureCmd();
                    } else if (data2 != null) {
                        sendCommand(data2);
                    } else {
                        sendFailureCmd();
                    }
                    break;
                case 2:
                    AsyncResult ar2 = (AsyncResult) msg.obj;
                    ((Integer) ar2.userObj).intValue();
                    String data3 = (String) ar2.result;
                    Rlog.i(MtkEmbmsAdaptor.TAG, "MSG_ID_EVENT_IND data: " + data3);
                    sendCommand(data3);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDataStream(MtkEmbmsAdaptIoThread adpatorIo) {
            this.mAdaptorIoThread = adpatorIo;
            Rlog.d(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptEventHandler setDataStream done.");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRil(Context context, CommandsInterface[] ci) {
            this.mContext = context;
            this.mCis = ci;
            Rlog.d(MtkEmbmsAdaptor.TAG, "MtkEmbmsAdaptEventHandler setRil done.");
        }

        public void sendCommand(String rawCmd) {
            MtkEmbmsAdaptIoThread mtkEmbmsAdaptIoThread = this.mAdaptorIoThread;
            if (mtkEmbmsAdaptIoThread != null) {
                mtkEmbmsAdaptIoThread.sendCommand(rawCmd);
            } else {
                Rlog.e(MtkEmbmsAdaptor.TAG, "sendCommand fail!! mAdaptorIoThread is null!");
            }
        }

        public void sendFailureCmd() {
            String cmdStr = String.format("ERROR\n", new Object[0]);
            sendCommand(cmdStr);
        }
    }
}
