package com.mediatek.internal.telephony.phb;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IMtkIccPhoneBook extends IInterface {
    public static final String DESCRIPTOR = "com.mediatek.internal.telephony.phb.IMtkIccPhoneBook";

    boolean addContactToGroup(int i, int i2, int i3) throws RemoteException;

    int[] getAdnRecordsCapacityExt() throws RemoteException;

    int[] getAdnRecordsCapacityForSubscriber(int i) throws RemoteException;

    List<MtkAdnRecord> getAdnRecordsInEf(int i) throws RemoteException;

    List<MtkAdnRecord> getAdnRecordsInEfForSubscriber(int i, int i2) throws RemoteException;

    int getAnrCount(int i) throws RemoteException;

    int getEmailCount(int i) throws RemoteException;

    UsimPBMemInfo[] getPhonebookMemStorageExt(int i) throws RemoteException;

    int getSneRecordLen(int i) throws RemoteException;

    int getUpbDone(int i) throws RemoteException;

    String getUsimAasById(int i, int i2) throws RemoteException;

    List<AlphaTag> getUsimAasList(int i) throws RemoteException;

    int getUsimAasMaxCount(int i) throws RemoteException;

    int getUsimAasMaxNameLen(int i) throws RemoteException;

    String getUsimGroupById(int i, int i2) throws RemoteException;

    List<UsimGroup> getUsimGroups(int i) throws RemoteException;

    int getUsimGrpMaxCount(int i) throws RemoteException;

    int getUsimGrpMaxNameLen(int i) throws RemoteException;

    int hasExistGroup(int i, String str) throws RemoteException;

    boolean hasSne(int i) throws RemoteException;

    int insertUsimAas(int i, String str) throws RemoteException;

    int insertUsimGroup(int i, String str) throws RemoteException;

    boolean isAdnAccessible(int i) throws RemoteException;

    boolean isPhbReady(int i) throws RemoteException;

    boolean moveContactFromGroupsToGroups(int i, int i2, int[] iArr, int[] iArr2) throws RemoteException;

    boolean removeContactFromGroup(int i, int i2, int i3) throws RemoteException;

    boolean removeUsimAasById(int i, int i2, int i3) throws RemoteException;

    boolean removeUsimGroupById(int i, int i2) throws RemoteException;

    int updateAdnRecordsInEfByIndexWithError(int i, int i2, String str, String str2, int i3, String str3) throws RemoteException;

    int updateAdnRecordsInEfBySearchWithError(int i, int i2, String str, String str2, String str3, String str4, String str5) throws RemoteException;

    boolean updateContactToGroups(int i, int i2, int[] iArr) throws RemoteException;

    boolean updateUsimAas(int i, int i2, int i3, String str) throws RemoteException;

    int updateUsimGroup(int i, int i2, String str) throws RemoteException;

    int updateUsimPBRecordsByIndexWithError(int i, int i2, MtkAdnRecord mtkAdnRecord, int i3) throws RemoteException;

    int updateUsimPBRecordsBySearchWithError(int i, int i2, MtkAdnRecord mtkAdnRecord, MtkAdnRecord mtkAdnRecord2) throws RemoteException;

    int updateUsimPBRecordsInEfByIndexWithError(int i, int i2, String str, String str2, String str3, String str4, String[] strArr, int i3) throws RemoteException;

    int updateUsimPBRecordsInEfBySearchWithError(int i, int i2, String str, String str2, String str3, String str4, String[] strArr, String str5, String str6, String str7, String str8, String[] strArr2) throws RemoteException;

    public static class Default implements IMtkIccPhoneBook {
        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<MtkAdnRecord> getAdnRecordsInEf(int efid) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<MtkAdnRecord> getAdnRecordsInEfForSubscriber(int subId, int efid) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateAdnRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String oldAnr, String oldGrpIds, String[] oldEmails, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateAdnRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, int index, String pin2) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails, int index) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsByIndexWithError(int subId, int efid, MtkAdnRecord record, int index) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimPBRecordsBySearchWithError(int subId, int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean isPhbReady(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<UsimGroup> getUsimGroups(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public String getUsimGroupById(int subId, int nGasId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean removeUsimGroupById(int subId, int nGasId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int insertUsimGroup(int subId, String grpName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int updateUsimGroup(int subId, int nGasId, String grpName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean addContactToGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean removeContactFromGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean updateContactToGroups(int subId, int adnIndex, int[] grpIdList) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean moveContactFromGroupsToGroups(int subId, int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int hasExistGroup(int subId, String grpName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimGrpMaxNameLen(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimGrpMaxCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public List<AlphaTag> getUsimAasList(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public String getUsimAasById(int subId, int index) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int insertUsimAas(int subId, String aasName) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getAnrCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getEmailCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimAasMaxCount(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUsimAasMaxNameLen(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean updateUsimAas(int subId, int index, int pbrIndex, String aasName) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean removeUsimAasById(int subId, int index, int pbrIndex) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean hasSne(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getSneRecordLen(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public boolean isAdnAccessible(int subId) throws RemoteException {
            return false;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public UsimPBMemInfo[] getPhonebookMemStorageExt(int subId) throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int getUpbDone(int subId) throws RemoteException {
            return 0;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int[] getAdnRecordsCapacityExt() throws RemoteException {
            return null;
        }

        @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
        public int[] getAdnRecordsCapacityForSubscriber(int subId) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMtkIccPhoneBook {
        static final int TRANSACTION_addContactToGroup = 15;
        static final int TRANSACTION_getAdnRecordsCapacityExt = 36;
        static final int TRANSACTION_getAdnRecordsCapacityForSubscriber = 37;
        static final int TRANSACTION_getAdnRecordsInEf = 1;
        static final int TRANSACTION_getAdnRecordsInEfForSubscriber = 2;
        static final int TRANSACTION_getAnrCount = 25;
        static final int TRANSACTION_getEmailCount = 26;
        static final int TRANSACTION_getPhonebookMemStorageExt = 34;
        static final int TRANSACTION_getSneRecordLen = 32;
        static final int TRANSACTION_getUpbDone = 35;
        static final int TRANSACTION_getUsimAasById = 23;
        static final int TRANSACTION_getUsimAasList = 22;
        static final int TRANSACTION_getUsimAasMaxCount = 27;
        static final int TRANSACTION_getUsimAasMaxNameLen = 28;
        static final int TRANSACTION_getUsimGroupById = 11;
        static final int TRANSACTION_getUsimGroups = 10;
        static final int TRANSACTION_getUsimGrpMaxCount = 21;
        static final int TRANSACTION_getUsimGrpMaxNameLen = 20;
        static final int TRANSACTION_hasExistGroup = 19;
        static final int TRANSACTION_hasSne = 31;
        static final int TRANSACTION_insertUsimAas = 24;
        static final int TRANSACTION_insertUsimGroup = 13;
        static final int TRANSACTION_isAdnAccessible = 33;
        static final int TRANSACTION_isPhbReady = 9;
        static final int TRANSACTION_moveContactFromGroupsToGroups = 18;
        static final int TRANSACTION_removeContactFromGroup = 16;
        static final int TRANSACTION_removeUsimAasById = 30;
        static final int TRANSACTION_removeUsimGroupById = 12;
        static final int TRANSACTION_updateAdnRecordsInEfByIndexWithError = 5;
        static final int TRANSACTION_updateAdnRecordsInEfBySearchWithError = 3;
        static final int TRANSACTION_updateContactToGroups = 17;
        static final int TRANSACTION_updateUsimAas = 29;
        static final int TRANSACTION_updateUsimGroup = 14;
        static final int TRANSACTION_updateUsimPBRecordsByIndexWithError = 7;
        static final int TRANSACTION_updateUsimPBRecordsBySearchWithError = 8;
        static final int TRANSACTION_updateUsimPBRecordsInEfByIndexWithError = 6;
        static final int TRANSACTION_updateUsimPBRecordsInEfBySearchWithError = 4;

        public Stub() {
            attachInterface(this, IMtkIccPhoneBook.DESCRIPTOR);
        }

        public static IMtkIccPhoneBook asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IMtkIccPhoneBook.DESCRIPTOR);
            if (iin != null && (iin instanceof IMtkIccPhoneBook)) {
                return (IMtkIccPhoneBook) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IMtkIccPhoneBook.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IMtkIccPhoneBook.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            List<MtkAdnRecord> _result = getAdnRecordsInEf(_arg0);
                            reply.writeNoException();
                            reply.writeTypedList(_result, 1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            List<MtkAdnRecord> _result2 = getAdnRecordsInEfForSubscriber(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeTypedList(_result2, 1);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg12 = data.readInt();
                            String _arg2 = data.readString();
                            String _arg3 = data.readString();
                            String _arg4 = data.readString();
                            String _arg5 = data.readString();
                            String _arg6 = data.readString();
                            data.enforceNoDataAvail();
                            int _result3 = updateAdnRecordsInEfBySearchWithError(_arg03, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg22 = data.readString();
                            String _arg32 = data.readString();
                            String _arg42 = data.readString();
                            String _arg52 = data.readString();
                            String[] _arg62 = data.createStringArray();
                            String _arg7 = data.readString();
                            String _arg8 = data.readString();
                            String _arg9 = data.readString();
                            String _arg10 = data.readString();
                            String[] _arg11 = data.createStringArray();
                            data.enforceNoDataAvail();
                            int _result4 = updateUsimPBRecordsInEfBySearchWithError(_arg04, _arg13, _arg22, _arg32, _arg42, _arg52, _arg62, _arg7, _arg8, _arg9, _arg10, _arg11);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            int _arg14 = data.readInt();
                            String _arg23 = data.readString();
                            String _arg33 = data.readString();
                            int _arg43 = data.readInt();
                            String _arg53 = data.readString();
                            data.enforceNoDataAvail();
                            int _result5 = updateAdnRecordsInEfByIndexWithError(_arg05, _arg14, _arg23, _arg33, _arg43, _arg53);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg15 = data.readInt();
                            String _arg24 = data.readString();
                            String _arg34 = data.readString();
                            String _arg44 = data.readString();
                            String _arg54 = data.readString();
                            String[] _arg63 = data.createStringArray();
                            int _arg72 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result6 = updateUsimPBRecordsInEfByIndexWithError(_arg06, _arg15, _arg24, _arg34, _arg44, _arg54, _arg63, _arg72);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg16 = data.readInt();
                            MtkAdnRecord _arg25 = (MtkAdnRecord) data.readTypedObject(MtkAdnRecord.CREATOR);
                            int _arg35 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result7 = updateUsimPBRecordsByIndexWithError(_arg07, _arg16, _arg25, _arg35);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 8:
                            int _arg08 = data.readInt();
                            int _arg17 = data.readInt();
                            MtkAdnRecord _arg26 = (MtkAdnRecord) data.readTypedObject(MtkAdnRecord.CREATOR);
                            MtkAdnRecord _arg36 = (MtkAdnRecord) data.readTypedObject(MtkAdnRecord.CREATOR);
                            data.enforceNoDataAvail();
                            int _result8 = updateUsimPBRecordsBySearchWithError(_arg08, _arg17, _arg26, _arg36);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 9:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = isPhbReady(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 10:
                            int _arg010 = data.readInt();
                            data.enforceNoDataAvail();
                            List<UsimGroup> _result10 = getUsimGroups(_arg010);
                            reply.writeNoException();
                            reply.writeTypedList(_result10, 1);
                            return true;
                        case 11:
                            int _arg011 = data.readInt();
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result11 = getUsimGroupById(_arg011, _arg18);
                            reply.writeNoException();
                            reply.writeString(_result11);
                            return true;
                        case 12:
                            int _arg012 = data.readInt();
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result12 = removeUsimGroupById(_arg012, _arg19);
                            reply.writeNoException();
                            reply.writeBoolean(_result12);
                            return true;
                        case 13:
                            int _arg013 = data.readInt();
                            String _arg110 = data.readString();
                            data.enforceNoDataAvail();
                            int _result13 = insertUsimGroup(_arg013, _arg110);
                            reply.writeNoException();
                            reply.writeInt(_result13);
                            return true;
                        case 14:
                            int _arg014 = data.readInt();
                            int _arg111 = data.readInt();
                            String _arg27 = data.readString();
                            data.enforceNoDataAvail();
                            int _result14 = updateUsimGroup(_arg014, _arg111, _arg27);
                            reply.writeNoException();
                            reply.writeInt(_result14);
                            return true;
                        case 15:
                            int _arg015 = data.readInt();
                            int _arg112 = data.readInt();
                            int _arg28 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result15 = addContactToGroup(_arg015, _arg112, _arg28);
                            reply.writeNoException();
                            reply.writeBoolean(_result15);
                            return true;
                        case 16:
                            int _arg016 = data.readInt();
                            int _arg113 = data.readInt();
                            int _arg29 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result16 = removeContactFromGroup(_arg016, _arg113, _arg29);
                            reply.writeNoException();
                            reply.writeBoolean(_result16);
                            return true;
                        case 17:
                            int _arg017 = data.readInt();
                            int _arg114 = data.readInt();
                            int[] _arg210 = data.createIntArray();
                            data.enforceNoDataAvail();
                            boolean _result17 = updateContactToGroups(_arg017, _arg114, _arg210);
                            reply.writeNoException();
                            reply.writeBoolean(_result17);
                            return true;
                        case 18:
                            int _arg018 = data.readInt();
                            int _arg115 = data.readInt();
                            int[] _arg211 = data.createIntArray();
                            int[] _arg37 = data.createIntArray();
                            data.enforceNoDataAvail();
                            boolean _result18 = moveContactFromGroupsToGroups(_arg018, _arg115, _arg211, _arg37);
                            reply.writeNoException();
                            reply.writeBoolean(_result18);
                            return true;
                        case 19:
                            int _arg019 = data.readInt();
                            String _arg116 = data.readString();
                            data.enforceNoDataAvail();
                            int _result19 = hasExistGroup(_arg019, _arg116);
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 20:
                            int _arg020 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result20 = getUsimGrpMaxNameLen(_arg020);
                            reply.writeNoException();
                            reply.writeInt(_result20);
                            return true;
                        case 21:
                            int _arg021 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result21 = getUsimGrpMaxCount(_arg021);
                            reply.writeNoException();
                            reply.writeInt(_result21);
                            return true;
                        case TRANSACTION_getUsimAasList /* 22 */:
                            int _arg022 = data.readInt();
                            data.enforceNoDataAvail();
                            List<AlphaTag> _result22 = getUsimAasList(_arg022);
                            reply.writeNoException();
                            reply.writeTypedList(_result22, 1);
                            return true;
                        case TRANSACTION_getUsimAasById /* 23 */:
                            int _arg023 = data.readInt();
                            int _arg117 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result23 = getUsimAasById(_arg023, _arg117);
                            reply.writeNoException();
                            reply.writeString(_result23);
                            return true;
                        case TRANSACTION_insertUsimAas /* 24 */:
                            int _arg024 = data.readInt();
                            String _arg118 = data.readString();
                            data.enforceNoDataAvail();
                            int _result24 = insertUsimAas(_arg024, _arg118);
                            reply.writeNoException();
                            reply.writeInt(_result24);
                            return true;
                        case TRANSACTION_getAnrCount /* 25 */:
                            int _arg025 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result25 = getAnrCount(_arg025);
                            reply.writeNoException();
                            reply.writeInt(_result25);
                            return true;
                        case TRANSACTION_getEmailCount /* 26 */:
                            int _arg026 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result26 = getEmailCount(_arg026);
                            reply.writeNoException();
                            reply.writeInt(_result26);
                            return true;
                        case TRANSACTION_getUsimAasMaxCount /* 27 */:
                            int _arg027 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result27 = getUsimAasMaxCount(_arg027);
                            reply.writeNoException();
                            reply.writeInt(_result27);
                            return true;
                        case TRANSACTION_getUsimAasMaxNameLen /* 28 */:
                            int _arg028 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result28 = getUsimAasMaxNameLen(_arg028);
                            reply.writeNoException();
                            reply.writeInt(_result28);
                            return true;
                        case TRANSACTION_updateUsimAas /* 29 */:
                            int _arg029 = data.readInt();
                            int _arg119 = data.readInt();
                            int _arg212 = data.readInt();
                            String _arg38 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result29 = updateUsimAas(_arg029, _arg119, _arg212, _arg38);
                            reply.writeNoException();
                            reply.writeBoolean(_result29);
                            return true;
                        case 30:
                            int _arg030 = data.readInt();
                            int _arg120 = data.readInt();
                            int _arg213 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result30 = removeUsimAasById(_arg030, _arg120, _arg213);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 31:
                            int _arg031 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result31 = hasSne(_arg031);
                            reply.writeNoException();
                            reply.writeBoolean(_result31);
                            return true;
                        case 32:
                            int _arg032 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result32 = getSneRecordLen(_arg032);
                            reply.writeNoException();
                            reply.writeInt(_result32);
                            return true;
                        case 33:
                            int _arg033 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result33 = isAdnAccessible(_arg033);
                            reply.writeNoException();
                            reply.writeBoolean(_result33);
                            return true;
                        case TRANSACTION_getPhonebookMemStorageExt /* 34 */:
                            int _arg034 = data.readInt();
                            data.enforceNoDataAvail();
                            UsimPBMemInfo[] _result34 = getPhonebookMemStorageExt(_arg034);
                            reply.writeNoException();
                            reply.writeTypedArray(_result34, 1);
                            return true;
                        case TRANSACTION_getUpbDone /* 35 */:
                            int _arg035 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result35 = getUpbDone(_arg035);
                            reply.writeNoException();
                            reply.writeInt(_result35);
                            return true;
                        case TRANSACTION_getAdnRecordsCapacityExt /* 36 */:
                            int[] _result36 = getAdnRecordsCapacityExt();
                            reply.writeNoException();
                            reply.writeIntArray(_result36);
                            return true;
                        case TRANSACTION_getAdnRecordsCapacityForSubscriber /* 37 */:
                            int _arg036 = data.readInt();
                            data.enforceNoDataAvail();
                            int[] _result37 = getAdnRecordsCapacityForSubscriber(_arg036);
                            reply.writeNoException();
                            reply.writeIntArray(_result37);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        private static class Proxy implements IMtkIccPhoneBook {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IMtkIccPhoneBook.DESCRIPTOR;
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<MtkAdnRecord> getAdnRecordsInEf(int efid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(efid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<MtkAdnRecord> _result = _reply.createTypedArrayList(MtkAdnRecord.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<MtkAdnRecord> getAdnRecordsInEfForSubscriber(int subId, int efid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<MtkAdnRecord> _result = _reply.createTypedArrayList(MtkAdnRecord.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateAdnRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String newTag, String newPhoneNumber, String pin2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeString(oldTag);
                    _data.writeString(oldPhoneNumber);
                    _data.writeString(newTag);
                    _data.writeString(newPhoneNumber);
                    _data.writeString(pin2);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsInEfBySearchWithError(int subId, int efid, String oldTag, String oldPhoneNumber, String oldAnr, String oldGrpIds, String[] oldEmails, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails) throws Throwable {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(oldTag);
                    try {
                        _data.writeString(oldPhoneNumber);
                        try {
                            _data.writeString(oldAnr);
                            try {
                                _data.writeString(oldGrpIds);
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringArray(oldEmails);
                        try {
                            _data.writeString(newTag);
                            try {
                                _data.writeString(newPhoneNumber);
                                try {
                                    _data.writeString(newAnr);
                                } catch (Throwable th5) {
                                    th = th5;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th6) {
                                th = th6;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(newGrpIds);
                            try {
                                _data.writeStringArray(newEmails);
                                try {
                                    this.mRemote.transact(4, _data, _reply, 0);
                                    _reply.readException();
                                    int _result = _reply.readInt();
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th8) {
                                    th = th8;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th9) {
                                th = th9;
                            }
                        } catch (Throwable th10) {
                            th = th10;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateAdnRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, int index, String pin2) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeString(newTag);
                    _data.writeString(newPhoneNumber);
                    _data.writeInt(index);
                    _data.writeString(pin2);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsInEfByIndexWithError(int subId, int efid, String newTag, String newPhoneNumber, String newAnr, String newGrpIds, String[] newEmails, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeString(newTag);
                    _data.writeString(newPhoneNumber);
                    _data.writeString(newAnr);
                    _data.writeString(newGrpIds);
                    _data.writeStringArray(newEmails);
                    _data.writeInt(index);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsByIndexWithError(int subId, int efid, MtkAdnRecord record, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeTypedObject(record, 0);
                    _data.writeInt(index);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimPBRecordsBySearchWithError(int subId, int efid, MtkAdnRecord oldAdn, MtkAdnRecord newAdn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(efid);
                    _data.writeTypedObject(oldAdn, 0);
                    _data.writeTypedObject(newAdn, 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean isPhbReady(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<UsimGroup> getUsimGroups(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<UsimGroup> _result = _reply.createTypedArrayList(UsimGroup.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public String getUsimGroupById(int subId, int nGasId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(nGasId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean removeUsimGroupById(int subId, int nGasId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(nGasId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int insertUsimGroup(int subId, String grpName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(grpName);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int updateUsimGroup(int subId, int nGasId, String grpName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(nGasId);
                    _data.writeString(grpName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean addContactToGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeInt(grpIndex);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean removeContactFromGroup(int subId, int adnIndex, int grpIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeInt(grpIndex);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean updateContactToGroups(int subId, int adnIndex, int[] grpIdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeIntArray(grpIdList);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean moveContactFromGroupsToGroups(int subId, int adnIndex, int[] fromGrpIdList, int[] toGrpIdList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(adnIndex);
                    _data.writeIntArray(fromGrpIdList);
                    _data.writeIntArray(toGrpIdList);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int hasExistGroup(int subId, String grpName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(grpName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimGrpMaxNameLen(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimGrpMaxCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public List<AlphaTag> getUsimAasList(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasList, _data, _reply, 0);
                    _reply.readException();
                    List<AlphaTag> _result = _reply.createTypedArrayList(AlphaTag.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public String getUsimAasById(int subId, int index) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(index);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasById, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int insertUsimAas(int subId, String aasName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(aasName);
                    this.mRemote.transact(Stub.TRANSACTION_insertUsimAas, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getAnrCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getAnrCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getEmailCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getEmailCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimAasMaxCount(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasMaxCount, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUsimAasMaxNameLen(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUsimAasMaxNameLen, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean updateUsimAas(int subId, int index, int pbrIndex, String aasName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(index);
                    _data.writeInt(pbrIndex);
                    _data.writeString(aasName);
                    this.mRemote.transact(Stub.TRANSACTION_updateUsimAas, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean removeUsimAasById(int subId, int index, int pbrIndex) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(index);
                    _data.writeInt(pbrIndex);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean hasSne(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getSneRecordLen(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public boolean isAdnAccessible(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public UsimPBMemInfo[] getPhonebookMemStorageExt(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getPhonebookMemStorageExt, _data, _reply, 0);
                    _reply.readException();
                    UsimPBMemInfo[] _result = (UsimPBMemInfo[]) _reply.createTypedArray(UsimPBMemInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int getUpbDone(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getUpbDone, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int[] getAdnRecordsCapacityExt() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getAdnRecordsCapacityExt, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.mediatek.internal.telephony.phb.IMtkIccPhoneBook
            public int[] getAdnRecordsCapacityForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IMtkIccPhoneBook.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(Stub.TRANSACTION_getAdnRecordsCapacityForSubscriber, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
