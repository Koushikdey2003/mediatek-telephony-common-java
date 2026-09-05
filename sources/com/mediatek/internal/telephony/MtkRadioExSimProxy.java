package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.telephony.Rlog;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.RILUtils;
import com.mediatek.internal.telephony.phb.PBEntry;
import com.mediatek.internal.telephony.phb.PhbEntry;
import com.mediatek.internal.telephony.rsu.RsuRequestData;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.V3_0.IMtkRadioEx;
import vendor.mediatek.hardware.mtkradioex.V3_0.PhbEntryExt;
import vendor.mediatek.hardware.mtkradioex.V3_0.PhbEntryStructure;
import vendor.mediatek.hardware.mtkradioex.rsu.RsuRequestInfo;
import vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSim;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExSimProxy extends MtkRadioExServiceProxy {
    private static final String TAG = "MtkRadioExSimProxy";
    private volatile IMtkRadioExSim mSimProxyMtk = null;

    public HalVersion setAidl(HalVersion halVersion, IMtkRadioExSim sim) {
        this.mHalVersion = halVersion;
        this.mSimProxyMtk = sim;
        this.mIsAidl = true;
        try {
            int version = sim.getInterfaceVersion();
            HalVersion newHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_4_0;
            Rlog.d(TAG, "AIDL version=" + version + ", halVersion=" + newHalVersion);
            if (this.mHalVersion.less(newHalVersion)) {
                this.mHalVersion = newHalVersion;
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setAidl: " + e);
        }
        Rlog.d(TAG, "AIDL initialized mHalVersion=" + this.mHalVersion);
        return this.mHalVersion;
    }

    public IMtkRadioExSim getAidl() {
        return this.mSimProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mSimProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mSimProxyMtk == null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.responseAcknowledgementMtk();
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).responseAcknowledgementMtk();
        }
    }

    public void sendVsimNotification(int serial, int transactionId, int eventId, int simType) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.sendVsimNotification(serial, transactionId, eventId, simType, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).sendVsimNotification(serial, transactionId, eventId, simType);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendVsimNotification(serial, transactionId, eventId, simType);
        }
    }

    public void sendVsimOperation(int serial, int transactionId, int eventId, int message, int dataLength, byte[] data) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.sendVsimOperation(serial, transactionId, eventId, message, dataLength, data, 0);
            return;
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ArrayList<Byte> arrList = new ArrayList<>();
            for (byte b : data) {
                arrList.add(Byte.valueOf(b));
            }
            ((IMtkRadioEx) this.mRadioProxyMtk).sendVsimOperation(serial, transactionId, eventId, message, dataLength, arrList);
            return;
        }
        ArrayList<Byte> arrList2 = new ArrayList<>();
        for (byte b2 : data) {
            arrList2.add(Byte.valueOf(b2));
        }
        ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendVsimOperation(serial, transactionId, eventId, message, dataLength, arrList2);
    }

    public void handleStkCallSetupRequestFromSimWithResCode(int serial, int resultCode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.handleStkCallSetupRequestFromSimWithResCode(serial, resultCode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).handleStkCallSetupRequestFromSimWithResCode(serial, resultCode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).handleStkCallSetupRequestFromSimWithResCode(serial, resultCode);
        }
    }

    public void getATR(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.getATR(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getATR(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getATR(serial);
        }
    }

    public void getIccid(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.getIccid(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getIccid(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getIccid(serial);
        }
    }

    public void setSimPower(int serial, int mode) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.setSimPower(serial, mode, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setSimPower(serial, mode);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setSimPower(serial, mode);
        }
    }

    public void deleteUPBEntry(int serial, int entryType, int adnIndex, int entryIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.deleteUPBEntry(serial, entryType, adnIndex, entryIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).deleteUPBEntry(serial, entryType, adnIndex, entryIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).deleteUPBEntry(serial, entryType, adnIndex, entryIndex);
        }
    }

    public void editUPBEntry(int serial, int entryType, int adnIndex, int entryIndex, String strVal, String tonForNum, String aasAnrIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        ArrayList<String> arrList = new ArrayList<>();
        arrList.add(Integer.toString(entryType));
        arrList.add(Integer.toString(adnIndex));
        arrList.add(Integer.toString(entryIndex));
        arrList.add(strVal);
        if (entryType == 0) {
            arrList.add(tonForNum);
            arrList.add(aasAnrIndex);
        }
        if (isAidl()) {
            String[] newArr = (String[]) arrList.toArray(new String[arrList.size()]);
            this.mSimProxyMtk.editUPBEntry(serial, newArr, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).editUPBEntry(serial, arrList);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).editUPBEntry(serial, arrList);
        }
    }

    public void getPhoneBookMemStorage(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.getPhoneBookMemStorage(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getPhoneBookMemStorage(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getPhoneBookMemStorage(serial);
        }
    }

    public void getPhoneBookStringsLength(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.getPhoneBookStringsLength(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).getPhoneBookStringsLength(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).getPhoneBookStringsLength(serial);
        }
    }

    public void queryPhbStorageInfo(int serial, int type) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.queryPhbStorageInfo(serial, type, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).queryPhbStorageInfo(serial, type);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).queryPhbStorageInfo(serial, type);
        }
    }

    public void queryUPBAvailable(int serial, int eftype, int fileIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.queryUPBAvailable(serial, eftype, fileIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).queryUPBAvailable(serial, eftype, fileIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).queryUPBAvailable(serial, eftype, fileIndex);
        }
    }

    public void readPhbEntry(int serial, int type, int bIndex, int eIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readPhbEntry(serial, type, bIndex, eIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readPhbEntry(serial, type, bIndex, eIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readPhbEntry(serial, type, bIndex, eIndex);
        }
    }

    public void readPhoneBookEntryExt(int serial, int index1, int index2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readPhoneBookEntryExt(serial, index1, index2, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readPhoneBookEntryExt(serial, index1, index2);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readPhoneBookEntryExt(serial, index1, index2);
        }
    }

    public void readUPBAasList(int serial, int startIndex, int endIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readUPBAasList(serial, startIndex, endIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readUPBAasList(serial, startIndex, endIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readUPBAasList(serial, startIndex, endIndex);
        }
    }

    public void readUPBAnrEntry(int serial, int adnIndex, int fileIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readUPBAnrEntry(serial, adnIndex, fileIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readUPBAnrEntry(serial, adnIndex, fileIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readUPBAnrEntry(serial, adnIndex, fileIndex);
        }
    }

    public void readUPBEmailEntry(int serial, int adnIndex, int fileIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readUPBEmailEntry(serial, adnIndex, fileIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readUPBEmailEntry(serial, adnIndex, fileIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readUPBEmailEntry(serial, adnIndex, fileIndex);
        }
    }

    public void readUPBGasList(int serial, int startIndex, int endIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readUPBGasList(serial, startIndex, endIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readUPBGasList(serial, startIndex, endIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readUPBGasList(serial, startIndex, endIndex);
        }
    }

    public void readUPBGrpEntry(int serial, int adnIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readUPBGrpEntry(serial, adnIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readUPBGrpEntry(serial, adnIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readUPBGrpEntry(serial, adnIndex);
        }
    }

    public void readUPBSneEntry(int serial, int adnIndex, int fileIndex) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.readUPBSneEntry(serial, adnIndex, fileIndex, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).readUPBSneEntry(serial, adnIndex, fileIndex);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).readUPBSneEntry(serial, adnIndex, fileIndex);
        }
    }

    public void setPhoneBookMemStorage(int serial, String storage, String password) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.setPhoneBookMemStorage(serial, RILUtils.convertNullToEmptyString(storage), RILUtils.convertNullToEmptyString(password), 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setPhoneBookMemStorage(serial, RILUtils.convertNullToEmptyString(storage), RILUtils.convertNullToEmptyString(password));
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setPhoneBookMemStorage(serial, RILUtils.convertNullToEmptyString(storage), RILUtils.convertNullToEmptyString(password));
        }
    }

    public void setPhonebookReady(int serial, int ready) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.setPhonebookReady(serial, ready, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setPhonebookReady(serial, ready);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setPhonebookReady(serial, ready);
        }
    }

    public void queryUPBCapability(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.queryUPBCapability(serial, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).queryUPBCapability(serial);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).queryUPBCapability(serial);
        }
    }

    private PhbEntryStructure convertToHalPhbEntryStructure(PhbEntry pe) {
        PhbEntryStructure pes = new PhbEntryStructure();
        pes.type = pe.type;
        pes.index = pe.index;
        pes.number = RILUtils.convertNullToEmptyString(pe.number);
        pes.ton = pe.ton;
        pes.alphaId = RILUtils.convertNullToEmptyString(pe.alphaId);
        return pes;
    }

    private vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryStructure convertToHalPhbEntryStructureV2(PhbEntry pe) {
        vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryStructure pes = new vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryStructure();
        pes.type = pe.type;
        pes.index = pe.index;
        pes.number = RILUtils.convertNullToEmptyString(pe.number);
        pes.ton = pe.ton;
        pes.alphaId = RILUtils.convertNullToEmptyString(pe.alphaId);
        return pes;
    }

    private vendor.mediatek.hardware.mtkradioex.sim.PhbEntryStructure convertToAidlPhbEntryStructure(PhbEntry pe) {
        vendor.mediatek.hardware.mtkradioex.sim.PhbEntryStructure pes = new vendor.mediatek.hardware.mtkradioex.sim.PhbEntryStructure();
        pes.type = pe.type;
        pes.index = pe.index;
        pes.number = RILUtils.convertNullToEmptyString(pe.number);
        pes.ton = pe.ton;
        pes.alphaId = RILUtils.convertNullToEmptyString(pe.alphaId);
        return pes;
    }

    public void writePhbEntry(int serial, PhbEntry entry) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            vendor.mediatek.hardware.mtkradioex.sim.PhbEntryStructure pes = convertToAidlPhbEntryStructure(entry);
            this.mSimProxyMtk.writePhbEntry(serial, pes, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            PhbEntryStructure pes2 = convertToHalPhbEntryStructure(entry);
            ((IMtkRadioEx) this.mRadioProxyMtk).writePhbEntry(serial, pes2);
        } else {
            vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryStructure pesV2 = convertToHalPhbEntryStructureV2(entry);
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).writePhbEntry(serial, pesV2);
        }
    }

    private static PhbEntryExt convertToHalPhbEntryExt(PBEntry pbe) {
        PhbEntryExt pee = new PhbEntryExt();
        pee.index = pbe.getIndex1();
        pee.number = pbe.getNumber();
        pee.type = pbe.getType();
        pee.text = pbe.getText();
        pee.hidden = pbe.getHidden();
        pee.group = pbe.getGroup();
        pee.adnumber = pbe.getAdnumber();
        pee.adtype = pbe.getAdtype();
        pee.secondtext = pbe.getSecondtext();
        pee.email = pbe.getEmail();
        return pee;
    }

    private static vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryExt convertToHalPhbEntryExtV2(PBEntry pbe) {
        vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryExt pee = new vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryExt();
        pee.index = pbe.getIndex1();
        pee.number = pbe.getNumber();
        pee.type = pbe.getType();
        pee.text = pbe.getText();
        pee.hidden = pbe.getHidden();
        pee.group = pbe.getGroup();
        pee.adnumber = pbe.getAdnumber();
        pee.adtype = pbe.getAdtype();
        pee.secondtext = pbe.getSecondtext();
        pee.email = pbe.getEmail();
        return pee;
    }

    private static vendor.mediatek.hardware.mtkradioex.sim.PhbEntryExt convertToAidlPhbEntryExt(PBEntry pbe) {
        vendor.mediatek.hardware.mtkradioex.sim.PhbEntryExt pee = new vendor.mediatek.hardware.mtkradioex.sim.PhbEntryExt();
        pee.index = pbe.getIndex1();
        pee.number = pbe.getNumber();
        pee.type = pbe.getType();
        pee.text = pbe.getText();
        pee.hidden = pbe.getHidden();
        pee.group = pbe.getGroup();
        pee.adnumber = pbe.getAdnumber();
        pee.adtype = pbe.getAdtype();
        pee.secondtext = pbe.getSecondtext();
        pee.email = pbe.getEmail();
        return pee;
    }

    public void writePhoneBookEntryExt(int serial, PBEntry entry) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            vendor.mediatek.hardware.mtkradioex.sim.PhbEntryExt pee = convertToAidlPhbEntryExt(entry);
            this.mSimProxyMtk.writePhoneBookEntryExt(serial, pee, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            PhbEntryExt pee2 = convertToHalPhbEntryExt(entry);
            ((IMtkRadioEx) this.mRadioProxyMtk).writePhoneBookEntryExt(serial, pee2);
        } else {
            vendor.mediatek.hardware.mtkradioex.V2_0.PhbEntryExt peeV2 = convertToHalPhbEntryExtV2(entry);
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).writePhoneBookEntryExt(serial, peeV2);
        }
    }

    public void writeUPBGrpEntry(int serial, int adnIndex, int[] grpIds) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        int length = grpIds.length;
        ArrayList<Integer> intList = new ArrayList<>(grpIds.length);
        for (int i : grpIds) {
            intList.add(Integer.valueOf(i));
        }
        if (isAidl()) {
            this.mSimProxyMtk.writeUPBGrpEntry(serial, adnIndex, grpIds, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).writeUPBGrpEntry(serial, adnIndex, intList);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).writeUPBGrpEntry(serial, adnIndex, intList);
        }
    }

    public void queryNetworkLock(int serial, int category) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.queryNetworkLock(serial, category, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).queryNetworkLock(serial, category);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).queryNetworkLock(serial, category);
        }
    }

    public void setNetworkLock(int serial, int category, int lockop, String password, String data_imsi, String gid1, String gid2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        String password2 = password == null ? "" : password;
        String data_imsi2 = data_imsi == null ? "" : data_imsi;
        String gid12 = gid1 == null ? "" : gid1;
        String gid22 = gid2 == null ? "" : gid2;
        if (isAidl()) {
            this.mSimProxyMtk.setNetworkLock(serial, category, lockop, password2, data_imsi2, gid12, gid22, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).setNetworkLock(serial, category, lockop, password2, data_imsi2, gid12, gid22);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).setNetworkLock(serial, category, lockop, password2, data_imsi2, gid12, gid22);
        }
    }

    public void supplyDepersonalization(int serial, String netpin, int type) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.supplyDepersonalization(serial, RILUtils.convertNullToEmptyString(netpin), type, 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).supplyDepersonalization(serial, RILUtils.convertNullToEmptyString(netpin), type);
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).supplyDepersonalization(serial, RILUtils.convertNullToEmptyString(netpin), type);
        }
    }

    public void supplyDeviceNetworkDepersonalization(int serial, String pwd) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mSimProxyMtk.supplyDeviceNetworkDepersonalization(serial, RILUtils.convertNullToEmptyString(pwd), 0);
        } else if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            ((IMtkRadioEx) this.mRadioProxyMtk).supplyDeviceNetworkDepersonalization(serial, RILUtils.convertNullToEmptyString(pwd));
        } else {
            ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).supplyDeviceNetworkDepersonalization(serial, RILUtils.convertNullToEmptyString(pwd));
        }
    }

    public void sendRsuRequest(int serial, RsuRequestData rri) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            RsuRequestInfo aidlRri = new RsuRequestInfo();
            aidlRri.opId = rri.opId;
            aidlRri.requestId = rri.requestId;
            aidlRri.requestType = rri.requestType;
            aidlRri.reserveInt1 = rri.reserveInt1;
            aidlRri.reserveInt2 = rri.reserveInt2;
            aidlRri.data = rri.data == null ? "" : rri.data;
            aidlRri.reserveString1 = rri.reserveString1 != null ? rri.reserveString1 : "";
            this.mSimProxyMtk.sendRsuRequest(serial, aidlRri, 0);
            return;
        }
        if (this.mHalVersion.greaterOrEqual(MtkRIL.RADIO_HAL_VERSION_MTK_3_0)) {
            vendor.mediatek.hardware.mtkradioex.V3_0.RsuRequestInfo hidl3Rri = new vendor.mediatek.hardware.mtkradioex.V3_0.RsuRequestInfo();
            hidl3Rri.opId = rri.opId;
            hidl3Rri.requestId = rri.requestId;
            hidl3Rri.requestType = rri.requestType;
            hidl3Rri.reserveInt1 = rri.reserveInt1;
            hidl3Rri.reserveInt2 = rri.reserveInt2;
            hidl3Rri.data = rri.data == null ? "" : rri.data;
            hidl3Rri.reserveString1 = rri.reserveString1 != null ? rri.reserveString1 : "";
            ((IMtkRadioEx) this.mRadioProxyMtk).sendRsuRequest(serial, hidl3Rri);
            return;
        }
        vendor.mediatek.hardware.mtkradioex.V2_0.RsuRequestInfo hidl2Rri = new vendor.mediatek.hardware.mtkradioex.V2_0.RsuRequestInfo();
        hidl2Rri.opId = rri.opId;
        hidl2Rri.requestId = rri.requestId;
        hidl2Rri.requestType = rri.requestType;
        hidl2Rri.reserveInt1 = rri.reserveInt1;
        hidl2Rri.reserveInt2 = rri.reserveInt2;
        hidl2Rri.data = rri.data == null ? "" : rri.data;
        hidl2Rri.reserveString1 = rri.reserveString1 != null ? rri.reserveString1 : "";
        ((vendor.mediatek.hardware.mtkradioex.V2_0.IMtkRadioEx) this.mRadioProxyMtk).sendRsuRequest(serial, hidl2Rri);
    }
}
