package com.mediatek.internal.telephony;

import android.content.Context;
import android.database.Cursor;
import android.util.Pair;
import com.android.internal.telephony.InboundSmsTracker;
import com.mediatek.internal.telephony.util.MtkSmsCommonUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkInboundSmsTracker extends InboundSmsTracker {
    public MtkInboundSmsTracker(Context context, byte[] pdu, long timestamp, int destPort, boolean is3gpp2, boolean is3gpp2WapPdu, String address, String displayAddress, String messageBody, boolean isClass0, int subId, int smsSource) {
        super(context, pdu, timestamp, destPort, is3gpp2, is3gpp2WapPdu, address, displayAddress, messageBody, isClass0, subId, smsSource);
    }

    public MtkInboundSmsTracker(Context context, byte[] pdu, long timestamp, int destPort, boolean is3gpp2, String address, String displayAddress, int referenceNumber, int sequenceNumber, int messageCount, boolean is3gpp2WapPdu, String messageBody, boolean isClass0, int subId, int smsSource) {
        super(context, pdu, timestamp, destPort, is3gpp2, address, displayAddress, referenceNumber, sequenceNumber, messageCount, is3gpp2WapPdu, messageBody, isClass0, subId, smsSource);
    }

    public MtkInboundSmsTracker(Context context, Cursor cursor, boolean isCurrentFormat3gpp2) {
        super(context, cursor, isCurrentFormat3gpp2);
        if (cursor.getInt(5) != 1) {
            setDeleteWhere(this.mDeleteWhere, this.mDeleteWhereArgs);
        }
    }

    public String getQueryForSegments() {
        return super.getQueryForSegments() + " AND sub_id=?" + (is3gpp2() ? MtkSmsCommonUtil.SQL_3GPP2_SMS : MtkSmsCommonUtil.SQL_3GPP_SMS);
    }

    public void setDeleteWhere(String deleteWhere, String[] deleteWhereArgs) {
        if (getMessageCount() == 1) {
            super.setDeleteWhere(deleteWhere, deleteWhereArgs);
        } else {
            Pair<String, String[]> pair = appendSubIdInQuery(null, deleteWhereArgs);
            super.setDeleteWhere(deleteWhere, (String[]) pair.second);
        }
    }

    public Pair<String, String[]> getExactMatchDupDetectQuery() {
        return appendSubIdInQuery(super.getExactMatchDupDetectQuery());
    }

    public Pair<String, String[]> getInexactMatchDupDetectQuery() {
        return appendSubIdInQuery(super.getInexactMatchDupDetectQuery());
    }

    private Pair<String, String[]> appendSubIdInQuery(Pair<String, String[]> base) {
        if (base == null) {
            return null;
        }
        return appendSubIdInQuery((String) base.first, (String[]) base.second);
    }

    private Pair<String, String[]> appendSubIdInQuery(String where, String[] whereArgs) {
        String newWhere;
        if (where == null) {
            newWhere = null;
        } else {
            newWhere = where + " AND sub_id=?" + (is3gpp2() ? MtkSmsCommonUtil.SQL_3GPP2_SMS : MtkSmsCommonUtil.SQL_3GPP_SMS);
        }
        List<String> baseWhereArgs = new ArrayList<>(Arrays.asList(whereArgs));
        baseWhereArgs.add(Integer.toString(this.mSubId));
        String[] newWhereArgs = new String[baseWhereArgs.size()];
        baseWhereArgs.toArray(newWhereArgs);
        return new Pair<>(newWhere, newWhereArgs);
    }
}
