package com.mediatek.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.InboundSmsTracker;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SmsBroadcastUndelivered;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.mediatek.internal.telephony.util.MtkSmsCommonUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsBroadcastUndelivered extends SmsBroadcastUndelivered {
    private static final String[] PDU_PENDING_MESSAGE_PROJECTION = {"pdu", "sequence", "destination_port", "date", "reference_number", "count", "address", "_id", "message_body", "display_originating_addr", "sub_id"};
    private static final String TAG = "MtkSmsBroadcastUndelivered";
    private static MtkSmsBroadcastUndelivered instance;

    public static void initialize(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler) {
        if (instance == null) {
            Rlog.d(TAG, " call initialize");
            instance = new MtkSmsBroadcastUndelivered(context, gsmInboundSmsHandler, cdmaInboundSmsHandler);
        }
        if (gsmInboundSmsHandler != null) {
            gsmInboundSmsHandler.sendMessage(6);
        }
        if (cdmaInboundSmsHandler != null) {
            cdmaInboundSmsHandler.sendMessage(6);
        }
    }

    private MtkSmsBroadcastUndelivered(Context context, GsmInboundSmsHandler gsmInboundSmsHandler, CdmaInboundSmsHandler cdmaInboundSmsHandler) {
        super(context);
    }

    public static void scanRawTable(Context context, long oldMessageTimestamp) throws Throwable {
        scanRawTable(context, false, oldMessageTimestamp);
        scanRawTable(context, true, oldMessageTimestamp);
    }

    private static void scanRawTable(Context context, boolean isCurrentFormat3gpp2, long oldMessageTimestamp) throws Throwable {
        String nonDeleteWhere;
        Cursor cursor;
        Phone phone;
        String nonDeleteWhere2;
        boolean z = isCurrentFormat3gpp2;
        Rlog.d(TAG, "scanning raw table for undelivered messages");
        long startTime = System.nanoTime();
        ContentResolver contentResolver = context.getContentResolver();
        HashMap<SmsReferenceKey, Integer> multiPartReceivedCount = new HashMap<>(4);
        HashSet<SmsReferenceKey> oldMultiPartMessages = new HashSet<>(4);
        Cursor cursor2 = null;
        try {
            try {
                nonDeleteWhere = "deleted = 0" + (z ? MtkSmsCommonUtil.SQL_3GPP2_SMS : MtkSmsCommonUtil.SQL_3GPP_SMS);
                cursor = contentResolver.query(InboundSmsHandler.sRawUri, PDU_PENDING_MESSAGE_PROJECTION, nonDeleteWhere, null, null);
            } catch (SQLException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (cursor == null) {
                Rlog.e(TAG, "error getting pending message cursor");
                if (cursor != null) {
                    cursor.close();
                }
                Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
                return;
            }
            while (cursor.moveToNext()) {
                try {
                    TelephonyComponentFactory telephonyComponentFactory = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName());
                    MtkInboundSmsTracker tracker = (MtkInboundSmsTracker) telephonyComponentFactory.makeInboundSmsTracker(context, cursor, z);
                    if (tracker.getMessageCount() == 1) {
                        broadcastSms(tracker);
                    } else {
                        SmsReferenceKey reference = new SmsReferenceKey(tracker);
                        Integer receivedCount = multiPartReceivedCount.get(reference);
                        if (receivedCount == null) {
                            multiPartReceivedCount.put(reference, 1);
                            if (tracker.getTimestamp() < oldMessageTimestamp) {
                                oldMultiPartMessages.add(reference);
                            }
                        } else {
                            int newCount = receivedCount.intValue() + 1;
                            if (newCount == tracker.getMessageCount()) {
                                Rlog.d(TAG, "found complete multi-part message");
                                broadcastSms(tracker);
                                oldMultiPartMessages.remove(reference);
                            } else {
                                multiPartReceivedCount.put(reference, Integer.valueOf(newCount));
                            }
                        }
                    }
                } catch (IllegalArgumentException e2) {
                    Rlog.e(TAG, "error loading SmsTracker: " + e2);
                }
            }
            Phone phone2 = PhoneFactory.getPhone(0);
            Iterator<SmsReferenceKey> it = oldMultiPartMessages.iterator();
            while (it.hasNext()) {
                SmsReferenceKey message = it.next();
                Iterator<SmsReferenceKey> it2 = it;
                String where = "address=? AND reference_number=? AND count=? AND deleted=0 AND sub_id=?" + (z ? MtkSmsCommonUtil.SQL_3GPP2_SMS : MtkSmsCommonUtil.SQL_3GPP_SMS);
                int rows = contentResolver.delete(InboundSmsHandler.sRawUriPermanentDelete, where, message.getDeleteWhereArgs());
                if (rows == 0) {
                    Rlog.e(TAG, "No rows were deleted from raw table!");
                } else {
                    Rlog.d(TAG, "Deleted " + rows + " rows from raw table for incomplete " + message.mMessageCount + " part message");
                }
                if (rows > 0) {
                    TelephonyMetrics metrics = TelephonyMetrics.getInstance();
                    nonDeleteWhere2 = nonDeleteWhere;
                    metrics.writeDroppedIncomingMultipartSms(0, message.mFormat, rows, message.mMessageCount);
                    if (phone2 != null) {
                        phone = phone2;
                        phone2.getSmsStats().onDroppedIncomingMultipartSms(message.mIs3gpp2, rows, message.mMessageCount);
                    } else {
                        phone = phone2;
                    }
                } else {
                    phone = phone2;
                    nonDeleteWhere2 = nonDeleteWhere;
                }
                z = isCurrentFormat3gpp2;
                it = it2;
                nonDeleteWhere = nonDeleteWhere2;
                phone2 = phone;
            }
            if (cursor != null) {
                cursor.close();
            }
            Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
        } catch (SQLException e3) {
            e = e3;
            cursor2 = cursor;
            Rlog.e(TAG, "error reading pending SMS messages", e);
            if (cursor2 != null) {
                cursor2.close();
            }
            Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
        } catch (Throwable th2) {
            th = th2;
            cursor2 = cursor;
            if (cursor2 != null) {
                cursor2.close();
            }
            Rlog.d(TAG, "finished scanning raw table in " + ((System.nanoTime() - startTime) / 1000000) + " ms");
            throw th;
        }
    }

    private static void broadcastSms(InboundSmsTracker tracker) {
        int subId = tracker.getSubId();
        int phoneId = SubscriptionManagerService.getInstance().getPhoneId(subId);
        if (!SubscriptionManager.isValidPhoneId(phoneId)) {
            Rlog.e(TAG, "broadcastSms: ignoring message; no phone found for subId " + subId);
            return;
        }
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone == null) {
            Rlog.e(TAG, "broadcastSms: ignoring message; no phone found for subId " + subId + " phoneId " + phoneId);
            return;
        }
        InboundSmsHandler handler = phone.getInboundSmsHandler(tracker.is3gpp2());
        if (handler != null) {
            handler.sendMessage(2, tracker);
        } else {
            Rlog.e(TAG, "null handler for " + tracker.getFormat() + " format, can't deliver.");
        }
    }

    private static class SmsReferenceKey {
        final String mAddress;
        final String mFormat;
        final boolean mIs3gpp2;
        final int mMessageCount;
        final String mQuery;
        final int mReferenceNumber;
        final long mSubId;

        SmsReferenceKey(MtkInboundSmsTracker tracker) {
            this.mAddress = tracker.getAddress();
            this.mReferenceNumber = tracker.getReferenceNumber();
            this.mMessageCount = tracker.getMessageCount();
            this.mQuery = tracker.getQueryForSegments();
            this.mIs3gpp2 = tracker.is3gpp2();
            this.mFormat = tracker.getFormat();
            this.mSubId = tracker.getSubId();
        }

        String[] getDeleteWhereArgs() {
            return new String[]{this.mAddress, Integer.toString(this.mReferenceNumber), Integer.toString(this.mMessageCount), Long.toString(this.mSubId)};
        }

        public int hashCode() {
            return (((((int) this.mSubId) * 63) + (this.mReferenceNumber * 31) + this.mMessageCount) * 31) + this.mAddress.hashCode();
        }

        public boolean equals(Object o) {
            if (!(o instanceof SmsReferenceKey)) {
                return false;
            }
            SmsReferenceKey other = (SmsReferenceKey) o;
            return other.mAddress.equals(this.mAddress) && other.mReferenceNumber == this.mReferenceNumber && other.mMessageCount == this.mMessageCount && other.mSubId == this.mSubId;
        }
    }
}
