package com.mediatek.internal.telephony.cat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.HexDump;
import com.android.internal.telephony.cat.ImageDescriptor;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
class BipIconLoader extends Handler {
    private static final int CLUT_ENTRY_SIZE = 3;
    private static final int CLUT_LOCATION_OFFSET = 4;
    private static final int EVENT_READ_CLUT_DONE = 3;
    private static final int EVENT_READ_EF_IMG_RECOED_DONE = 1;
    private static final int EVENT_READ_ICON_DONE = 2;
    private static final int STATE_MULTI_ICONS = 2;
    private static final int STATE_SINGLE_ICON = 1;
    private static final String TAG = "Stk-BipIL";
    private Bitmap mCurrentIcon;
    private int mCurrentRecordIndex;
    private Message mEndMsg;
    private byte[] mIconData;
    private Bitmap[] mIcons;
    private HashMap<Integer, Bitmap> mIconsCache;
    private ImageDescriptor mId;
    private int mRecordNumber;
    private int[] mRecordNumbers;
    private IccFileHandler mSimFH;
    private int mSlotId;
    private int mState;
    private static BipIconLoader[] sLoader = null;
    private static HandlerThread[] sThread = null;
    private static int sSimCount = 0;

    private BipIconLoader(Looper looper, IccFileHandler fh, int slotId) {
        super(looper);
        this.mState = 1;
        this.mId = null;
        this.mCurrentIcon = null;
        this.mSimFH = null;
        this.mEndMsg = null;
        this.mIconData = null;
        this.mRecordNumbers = null;
        this.mCurrentRecordIndex = 0;
        this.mIcons = null;
        this.mIconsCache = null;
        this.mSimFH = fh;
        this.mSlotId = slotId;
        this.mIconsCache = new HashMap<>(50);
    }

    static BipIconLoader getInstance(Handler caller, IccFileHandler fh, int slotId) {
        BipIconLoader bipIconLoader;
        BipIconLoader[] bipIconLoaderArr = sLoader;
        if (bipIconLoaderArr != null && (bipIconLoader = bipIconLoaderArr[slotId]) != null) {
            return bipIconLoader;
        }
        if (sThread == null) {
            int simCount = TelephonyManager.getDefault().getSimCount();
            sSimCount = simCount;
            sThread = new HandlerThread[simCount];
            for (int i = 0; i < sSimCount; i++) {
                sThread[i] = null;
            }
        }
        if (sLoader == null) {
            int simCount2 = TelephonyManager.getDefault().getSimCount();
            sSimCount = simCount2;
            sLoader = new BipIconLoader[simCount2];
            for (int i2 = 0; i2 < sSimCount; i2++) {
                sLoader[i2] = null;
            }
        }
        if (fh != null) {
            HandlerThread[] handlerThreadArr = sThread;
            if (handlerThreadArr[slotId] == null) {
                handlerThreadArr[slotId] = new HandlerThread("BIP Icon Loader");
                sThread[slotId].start();
            }
            if (sLoader[slotId] != null || sThread[slotId].getLooper() == null) {
                return null;
            }
            sLoader[slotId] = new BipIconLoader(sThread[slotId].getLooper(), fh, slotId);
            return sLoader[slotId];
        }
        return null;
    }

    void loadIcons(int[] recordNumbers, Message msg) {
        if (recordNumbers == null || recordNumbers.length == 0 || msg == null) {
            return;
        }
        this.mEndMsg = msg;
        this.mIcons = new Bitmap[recordNumbers.length];
        this.mRecordNumbers = recordNumbers;
        this.mCurrentRecordIndex = 0;
        this.mState = 2;
        startLoadingIcon(recordNumbers[0]);
    }

    void loadIcon(int recordNumber, Message msg) {
        if (msg == null) {
            return;
        }
        this.mEndMsg = msg;
        this.mState = 1;
        startLoadingIcon(recordNumber);
    }

    private void startLoadingIcon(int recordNumber) {
        MtkCatLog.d(TAG, "call startLoadingIcon");
        this.mId = null;
        this.mIconData = null;
        this.mCurrentIcon = null;
        this.mRecordNumber = recordNumber;
        if (this.mIconsCache.containsKey(Integer.valueOf(recordNumber))) {
            MtkCatLog.d(TAG, "mIconsCache contains record " + recordNumber);
            this.mCurrentIcon = this.mIconsCache.get(Integer.valueOf(recordNumber));
            postIcon();
        } else {
            MtkCatLog.d(TAG, "to load icon from EFimg");
            readId();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case 1:
                    MtkCatLog.d(TAG, "load EFimg done");
                    if (msg.obj == null) {
                        MtkCatLog.e(TAG, "msg.obj is null.");
                        return;
                    }
                    MtkCatLog.d(TAG, "msg.obj is " + msg.obj.getClass().getName());
                    AsyncResult ar = (AsyncResult) msg.obj;
                    MtkCatLog.d(TAG, "EFimg raw data: " + HexDump.toHexString((byte[]) ar.result));
                    if (handleImageDescriptor((byte[]) ar.result)) {
                        readIconData();
                        return;
                    }
                    throw new Exception("Unable to parse image descriptor");
                case 2:
                    MtkCatLog.d(TAG, "load icon done");
                    byte[] rawData = (byte[]) ((AsyncResult) msg.obj).result;
                    MtkCatLog.d(TAG, "icon raw data: " + HexDump.toHexString(rawData));
                    MtkCatLog.d(TAG, "load icon CODING_SCHEME = " + this.mId.mCodingScheme);
                    if (this.mId.mCodingScheme == 17) {
                        this.mCurrentIcon = parseToBnW(rawData, rawData.length);
                        this.mIconsCache.put(Integer.valueOf(this.mRecordNumber), this.mCurrentIcon);
                        postIcon();
                        return;
                    } else if (this.mId.mCodingScheme == 33) {
                        this.mIconData = rawData;
                        readClut();
                        return;
                    } else {
                        MtkCatLog.d(TAG, "else  /postIcon ");
                        postIcon();
                        return;
                    }
                case 3:
                    MtkCatLog.d(TAG, "load clut done");
                    byte[] clut = (byte[]) ((AsyncResult) msg.obj).result;
                    byte[] bArr = this.mIconData;
                    this.mCurrentIcon = parseToRGB(bArr, bArr.length, false, clut);
                    this.mIconsCache.put(Integer.valueOf(this.mRecordNumber), this.mCurrentIcon);
                    postIcon();
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            MtkCatLog.d(this, "Icon load failed");
            e.printStackTrace();
            postIcon();
        }
    }

    private boolean handleImageDescriptor(byte[] rawData) {
        MtkCatLog.d(TAG, "call handleImageDescriptor");
        ImageDescriptor imageDescriptor = ImageDescriptor.parse(rawData, 1);
        this.mId = imageDescriptor;
        if (imageDescriptor == null) {
            MtkCatLog.d(TAG, "fail to parse image raw data");
            return false;
        }
        MtkCatLog.d(TAG, "success to parse image raw data");
        return true;
    }

    private void readClut() {
        int length = this.mIconData[3] * 3;
        Message msg = obtainMessage(3);
        IccFileHandler iccFileHandler = this.mSimFH;
        int i = this.mId.mImageId;
        byte[] bArr = this.mIconData;
        iccFileHandler.loadEFImgTransparent(i, bArr[4], bArr[5], length, msg);
    }

    private void readId() {
        MtkCatLog.d(TAG, "call readId");
        if (this.mRecordNumber < 0) {
            this.mCurrentIcon = null;
            postIcon();
        } else {
            Message msg = obtainMessage(1);
            this.mSimFH.loadEFImgLinearFixed(this.mRecordNumber, msg);
        }
    }

    private void readIconData() {
        MtkCatLog.d(TAG, "call readIconData");
        Message msg = obtainMessage(2);
        this.mSimFH.loadEFImgTransparent(this.mId.mImageId, 0, 0, this.mId.mLength, msg);
    }

    private void postIcon() {
        int i = this.mState;
        if (i == 1) {
            this.mEndMsg.obj = this.mCurrentIcon;
            this.mEndMsg.sendToTarget();
        } else if (i == 2) {
            Bitmap[] bitmapArr = this.mIcons;
            int i2 = this.mCurrentRecordIndex;
            int i3 = i2 + 1;
            this.mCurrentRecordIndex = i3;
            bitmapArr[i2] = this.mCurrentIcon;
            int[] iArr = this.mRecordNumbers;
            if (i3 < iArr.length) {
                startLoadingIcon(iArr[i3]);
            } else {
                this.mEndMsg.obj = bitmapArr;
                this.mEndMsg.sendToTarget();
            }
        }
    }

    public static Bitmap parseToBnW(byte[] data, int length) {
        int valueIndex = 0 + 1;
        int width = data[0] & 255;
        int valueIndex2 = valueIndex + 1;
        int height = data[valueIndex] & 255;
        int numOfPixels = width * height;
        int[] pixels = new int[numOfPixels];
        int pixelIndex = 0;
        int bitIndex = 7;
        byte currentByte = 0;
        while (pixelIndex < numOfPixels) {
            if (pixelIndex % 8 == 0) {
                int valueIndex3 = valueIndex2 + 1;
                byte currentByte2 = data[valueIndex2];
                bitIndex = 7;
                currentByte = currentByte2;
                valueIndex2 = valueIndex3;
            }
            pixels[pixelIndex] = bitToBnW((currentByte >> bitIndex) & 1);
            pixelIndex++;
            bitIndex--;
        }
        if (pixelIndex != numOfPixels) {
            MtkCatLog.d("BipIconLoader", "parseToBnW; size error");
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    private static int bitToBnW(int bit) {
        if (bit == 1) {
            return -1;
        }
        return -16777216;
    }

    public static Bitmap parseToRGB(byte[] data, int length, boolean transparency, byte[] clut) {
        int valueIndex = 0 + 1;
        int width = data[0] & 255;
        int valueIndex2 = valueIndex + 1;
        int height = data[valueIndex] & 255;
        int valueIndex3 = valueIndex2 + 1;
        int bitsPerImg = data[valueIndex2] & 255;
        int i = valueIndex3 + 1;
        int numOfClutEntries = data[valueIndex3] & 255;
        if (true == transparency) {
            clut[numOfClutEntries - 1] = 0;
        }
        int numOfPixels = width * height;
        int[] pixels = new int[numOfPixels];
        int pixelIndex = 0;
        int bitsStartOffset = 8 - bitsPerImg;
        int bitIndex = bitsStartOffset;
        int valueIndex4 = 6 + 1;
        byte currentByte = data[6];
        int mask = getMask(bitsPerImg);
        boolean bitsOverlaps = 8 % bitsPerImg == 0;
        while (pixelIndex < numOfPixels) {
            if (bitIndex < 0) {
                int valueIndex5 = valueIndex4 + 1;
                currentByte = data[valueIndex4];
                bitIndex = bitsOverlaps ? bitsStartOffset : bitIndex * (-1);
                valueIndex4 = valueIndex5;
            }
            int clutEntry = (currentByte >> bitIndex) & mask;
            int clutIndex = clutEntry * 3;
            int numOfClutEntries2 = numOfClutEntries;
            int numOfClutEntries3 = clut[clutIndex];
            byte currentByte2 = currentByte;
            byte currentByte3 = clut[clutIndex + 1];
            pixels[pixelIndex] = Color.rgb(numOfClutEntries3, (int) currentByte3, (int) clut[clutIndex + 2]);
            bitIndex -= bitsPerImg;
            pixelIndex++;
            numOfClutEntries = numOfClutEntries2;
            currentByte = currentByte2;
            bitsOverlaps = bitsOverlaps;
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    private static int getMask(int numOfBits) {
        switch (numOfBits) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 7;
            case 4:
                return 15;
            case 5:
                return 31;
            case 6:
                return 63;
            case 7:
                return 127;
            case 8:
                return 255;
            default:
                return 0;
        }
    }

    public void dispose() {
        int i;
        HandlerThread[] handlerThreadArr;
        HandlerThread handlerThread;
        this.mSimFH = null;
        HandlerThread[] handlerThreadArr2 = sThread;
        if (handlerThreadArr2 != null && (handlerThread = handlerThreadArr2[this.mSlotId]) != null) {
            handlerThread.quit();
            sThread[this.mSlotId] = null;
        }
        BipIconLoader[] bipIconLoaderArr = sLoader;
        if (bipIconLoaderArr != null) {
            int i2 = this.mSlotId;
            if (bipIconLoaderArr[i2] != null) {
                bipIconLoaderArr[i2] = null;
            }
        }
        int i3 = 0;
        while (true) {
            i = sSimCount;
            if (i3 >= i || !((handlerThreadArr = sThread) == null || handlerThreadArr[i3] == null)) {
                break;
            } else {
                i3++;
            }
        }
        if (i3 == i) {
            sThread = null;
            sLoader = null;
        }
        this.mIconsCache = null;
    }
}
