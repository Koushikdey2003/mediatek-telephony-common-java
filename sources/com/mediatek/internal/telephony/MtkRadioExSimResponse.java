package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.os.AsyncResult;
import com.mediatek.internal.telephony.phb.PBEntry;
import com.mediatek.internal.telephony.phb.PBMemStorage;
import com.mediatek.internal.telephony.phb.PhbEntry;
import com.mediatek.internal.telephony.rsu.RsuResponseData;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.rsu.RsuResponseInfo;
import vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse;
import vendor.mediatek.hardware.mtkradioex.sim.PhbEntryExt;
import vendor.mediatek.hardware.mtkradioex.sim.PhbEntryStructure;
import vendor.mediatek.hardware.mtkradioex.sim.PhbMemStorageResponse;
import vendor.mediatek.hardware.mtkradioex.sim.VsimEvent;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExSimResponse extends IMtkRadioExSimResponse.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExSimResponse(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void getIccidResponse(RadioResponseInfo info, String iccid) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseString(5, this.mMtkRil, info, iccid);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void activateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(5, this.mMtkRil, info, new int[]{simPowerOnOffResponse});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void deactivateUiccCardRsp(RadioResponseInfo info, int simPowerOnOffResponse) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(5, this.mMtkRil, info, new int[]{simPowerOnOffResponse});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void deleteUPBEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void editUPBEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void getATRResponse(RadioResponseInfo info, String response) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseString(5, this.mMtkRil, info, response);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void getCurrentUiccCardProvisioningStatusRsp(RadioResponseInfo info, int simPowerOnOffStatus) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(5, this.mMtkRil, info, new int[]{simPowerOnOffStatus});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void handleStkCallSetupRequestFromSimWithResCodeResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    private void responseGetPhbMemStorage(RadioResponseInfo responseInfo, PhbMemStorageResponse phbMemStorage) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(5, responseInfo);
        if (rr != null) {
            PBMemStorage ret = new PBMemStorage();
            if (responseInfo.error == 0) {
                ret.setStorage(phbMemStorage.storage);
                ret.setUsed(phbMemStorage.used);
                ret.setTotal(phbMemStorage.total);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void getPhoneBookMemStorageResponse(RadioResponseInfo responseInfo, PhbMemStorageResponse phbMemStorage) {
        responseGetPhbMemStorage(responseInfo, phbMemStorage);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void getPhoneBookStringsLengthResponse(RadioResponseInfo info, int[] stringLengthInfo) {
        ArrayList<Integer> stringLength = new ArrayList<>();
        for (int i : stringLengthInfo) {
            stringLength.add(Integer.valueOf(i));
        }
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(5, this.mMtkRil, info, stringLength);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void queryNetworkLockResponse(RadioResponseInfo info, int catagory, int state, int retry_cnt, int autolock_cnt, int num_set, int total_set, int key_state) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(5, this.mMtkRil, info, new int[]{catagory, state, retry_cnt, autolock_cnt, num_set, total_set, key_state});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void setNetworkLockResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void queryPhbStorageInfoResponse(RadioResponseInfo responseInfo, int[] storageInfo) {
        ArrayList<Integer> storInfo = new ArrayList<>();
        for (int i : storageInfo) {
            storInfo.add(Integer.valueOf(i));
        }
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(5, this.mMtkRil, responseInfo, storInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void queryUPBAvailableResponse(RadioResponseInfo responseInfo, int[] upbAvailable) {
        ArrayList<Integer> upb = new ArrayList<>();
        for (int i : upbAvailable) {
            upb.add(Integer.valueOf(i));
        }
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(5, this.mMtkRil, responseInfo, upb);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void queryUPBCapabilityResponse(RadioResponseInfo responseInfo, int[] upbCapability) {
        ArrayList<Integer> capability = new ArrayList<>();
        for (int i : upbCapability) {
            capability.add(Integer.valueOf(i));
        }
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(5, this.mMtkRil, responseInfo, capability);
    }

    private void responsePhbEntries(RadioResponseInfo responseInfo, PhbEntryStructure[] phbEntry) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(5, responseInfo);
        if (rr != null) {
            PhbEntry[] ret = null;
            if (responseInfo.error == 0) {
                ret = new PhbEntry[phbEntry.length];
                for (int i = 0; i < phbEntry.length; i++) {
                    ret[i] = new PhbEntry();
                    ret[i].type = phbEntry[i].type;
                    ret[i].index = phbEntry[i].index;
                    ret[i].number = phbEntry[i].number;
                    ret[i].ton = phbEntry[i].ton;
                    ret[i].alphaId = phbEntry[i].alphaId;
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readPhbEntryResponse(RadioResponseInfo responseInfo, PhbEntryStructure[] phbEntries) {
        responsePhbEntries(responseInfo, phbEntries);
    }

    private void responseReadPhbEntryExt(RadioResponseInfo responseInfo, PhbEntryExt[] phbEntryExts) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(5, responseInfo);
        if (rr != null) {
            PBEntry[] ret = null;
            if (responseInfo.error == 0) {
                ret = new PBEntry[phbEntryExts.length];
                for (int i = 0; i < phbEntryExts.length; i++) {
                    ret[i] = new PBEntry();
                    ret[i].setIndex1(phbEntryExts[i].type);
                    ret[i].setNumber(phbEntryExts[i].number);
                    ret[i].setType(phbEntryExts[i].type);
                    ret[i].setText(phbEntryExts[i].text);
                    ret[i].setHidden(phbEntryExts[i].hidden);
                    ret[i].setGroup(phbEntryExts[i].group);
                    ret[i].setAdnumber(phbEntryExts[i].adnumber);
                    ret[i].setAdtype(phbEntryExts[i].adtype);
                    ret[i].setSecondtext(phbEntryExts[i].secondtext);
                    ret[i].setEmail(phbEntryExts[i].email);
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readPhoneBookEntryExtResponse(RadioResponseInfo responseInfo, PhbEntryExt[] phbEntryExts) {
        responseReadPhbEntryExt(responseInfo, phbEntryExts);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readUPBAasListResponse(RadioResponseInfo responseInfo, String[] aasList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStrings(5, this.mMtkRil, responseInfo, aasList);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readUPBAnrEntryResponse(RadioResponseInfo responseInfo, PhbEntryStructure[] anrs) {
        responsePhbEntries(responseInfo, anrs);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readUPBEmailEntryResponse(RadioResponseInfo responseInfo, String email) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(5, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, email);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, "xxx@email.com");
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readUPBGasListResponse(RadioResponseInfo responseInfo, String[] gasList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStrings(5, this.mMtkRil, responseInfo, gasList);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readUPBGrpEntryResponse(RadioResponseInfo responseInfo, int[] grpEntries) {
        ArrayList<Integer> grp = new ArrayList<>();
        for (int i : grpEntries) {
            grp.add(Integer.valueOf(i));
        }
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(5, this.mMtkRil, responseInfo, grp);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void readUPBSneEntryResponse(RadioResponseInfo info, String sne) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseString(5, this.mMtkRil, info, sne);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void sendRsuRequestResponse(RadioResponseInfo info, RsuResponseInfo rsuInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(5, info);
        if (rr != null) {
            RsuResponseData rrs = new RsuResponseData();
            rrs.opId = rsuInfo.opId;
            rrs.requestId = rsuInfo.requestId;
            rrs.errCode = rsuInfo.errCode;
            rrs.time = rsuInfo.time;
            rrs.version = rsuInfo.version;
            rrs.status = rsuInfo.status;
            rrs.reserveInt1 = rsuInfo.reserveInt1;
            rrs.reserveInt2 = rsuInfo.reserveInt2;
            rrs.data = rsuInfo.data == null ? "" : rsuInfo.data;
            rrs.reserveString1 = rsuInfo.reserveString1 != null ? rsuInfo.reserveString1 : "";
            if (info.error == 0) {
                AsyncResult.forMessage(rr.mResult, rrs, (Throwable) null);
                rr.mResult.sendToTarget();
            }
            this.mMtkRil.processResponseDone(rr, info, rrs);
            return;
        }
        this.mMtkRil.riljLog("sendRsuRequestResponse, rr is null");
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void vsimNotificationResponse(RadioResponseInfo info, VsimEvent event) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(5, info);
        if (rr != null) {
            Object ret = null;
            if (info.error == 0) {
                ret = Integer.valueOf(event.transactionId);
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, info, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void vsimOperationResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void setPhoneBookMemStorageResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void setPhonebookReadyResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void setSimPowerResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void writePhbEntryResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void writePhoneBookEntryExtResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void writeUPBGrpEntryResponse(RadioResponseInfo info) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(5, this.mMtkRil, info);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void supplyDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(5, this.mMtkRil, info, new int[]{remainingRetries});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public void supplyDeviceNetworkDepersonalizationResponse(RadioResponseInfo info, int remainingRetries) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(5, this.mMtkRil, info, new int[]{remainingRetries});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public String getInterfaceHash() {
        return "a07d8b715b307b4e57aa776d209f5655cced7f96";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.sim.IMtkRadioExSimResponse
    public int getInterfaceVersion() {
        return 1;
    }
}
