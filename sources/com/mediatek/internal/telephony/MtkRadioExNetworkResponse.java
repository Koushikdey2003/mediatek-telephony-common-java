package com.mediatek.internal.telephony;

import android.hardware.radio.RadioResponseInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RILUtils;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import vendor.mediatek.hardware.mtkradioex.network.BandModeInfo;
import vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse;
import vendor.mediatek.hardware.mtkradioex.network.Lte1xRttCellInfo;
import vendor.mediatek.hardware.mtkradioex.network.LteData;
import vendor.mediatek.hardware.mtkradioex.network.OperatorInfoWithAct;
import vendor.mediatek.hardware.mtkradioex.network.SignalStrengthWithWcdmaEcio;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExNetworkResponse extends IMtkRadioExNetworkResponse.Stub {
    MtkRIL mMtkRil;

    public MtkRadioExNetworkResponse(MtkRIL ril) {
        this.mMtkRil = ril;
    }

    public void acknowledgeRequest(int serial) {
        this.mMtkRil.processRequestAck(serial);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void abortFemtocellListResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void cancelAvailableNetworksResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void cfgA2offsetResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void cfgB1offsetResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void clearLteAvailableFileResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void deactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setCarrierAggregationModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void enableCAPlusBandWidthFilterResponse(RadioResponseInfo info) {
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void enableSCGfailureResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void get4x4MimoEnabledResponse(RadioResponseInfo responseInfo, int enabled_bitmask) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{enabled_bitmask});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getAllBandModeResponse(RadioResponseInfo responseInfo, BandModeInfo data) {
        this.mMtkRil.riljLog("getAllBandModeResponse, gsm =  " + data.gsm + ", umts = " + data.umts + ", lte=" + data.lte + ", sa = " + data.sa + ", nsa = " + data.nsa);
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, data);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, data);
            return;
        }
        this.mMtkRil.riljLog("getAllBandModeResponse, rr is null");
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getApcInfoResponse(RadioResponseInfo responseInfo, int[] cellInfo) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, cellInfo);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, cellInfo);
        }
    }

    private int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length <= 0) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    private static String convertOpertatorInfoToString(int status) {
        if (status == 0) {
            return "unknown";
        }
        if (status == 1) {
            return "available";
        }
        if (status == 2) {
            return "current";
        }
        if (status == 3) {
            return "forbidden";
        }
        return "";
    }

    private void responseOperatorInfosWithAct(RadioResponseInfo responseInfo, OperatorInfoWithAct[] networkInfos) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        if (rr != null) {
            ArrayList<OperatorInfo> ret = null;
            if (responseInfo.error == 0) {
                ret = new ArrayList<>();
                for (OperatorInfoWithAct info : networkInfos) {
                    int nLac = -1;
                    if (info.lac.length() > 0) {
                        nLac = Integer.parseInt(info.lac, 16);
                    }
                    android.hardware.radio.network.OperatorInfo operatorInfo = info.base;
                    MtkRIL mtkRIL = this.mMtkRil;
                    operatorInfo.alphaLong = mtkRIL.lookupOperatorNameForPlmnList(getSubId(mtkRIL.mInstanceId.intValue()), info.base.operatorNumeric, true, nLac);
                    android.hardware.radio.network.OperatorInfo operatorInfo2 = info.base;
                    MtkRIL mtkRIL2 = this.mMtkRil;
                    operatorInfo2.alphaShort = mtkRIL2.lookupOperatorNameForPlmnList(getSubId(mtkRIL2.mInstanceId.intValue()), info.base.operatorNumeric, false, nLac);
                    if (!info.base.alphaLong.toUpperCase().endsWith(info.act)) {
                        info.base.alphaLong = info.base.alphaLong.concat(" " + info.act);
                    }
                    if (!info.base.alphaShort.toUpperCase().endsWith(info.act)) {
                        info.base.alphaShort = info.base.alphaShort.concat(" " + info.act);
                    }
                    if (!this.mMtkRil.hidePLMN(info.base.operatorNumeric)) {
                        ret.add(new OperatorInfo(info.base.alphaLong, info.base.alphaShort, info.base.operatorNumeric, convertOpertatorInfoToString(info.base.status)));
                    } else {
                        this.mMtkRil.riljLog("remove this one " + info.base.operatorNumeric);
                    }
                }
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getAvailableNetworksWithActResponse(RadioResponseInfo responseInfo, OperatorInfoWithAct[] networkInfos) {
        responseOperatorInfosWithAct(responseInfo, networkInfos);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getBandModeResponse(RadioResponseInfo responseInfo, int[] data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(data));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getBandPriorityListResponse(RadioResponseInfo responseInfo, int[] bandPriList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(bandPriList));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getCALinkCapabilityListResponse(RadioResponseInfo responseInfo, String[] linkCapabilityList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(linkCapabilityList));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getCALinkEnableStatusResponse(RadioResponseInfo radioResponseInfo, boolean z) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, radioResponseInfo, new int[]{z ? 1 : 0});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getCaBandModeResponse(RadioResponseInfo responseInfo, int[] data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(data));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getCampedFemtoCellInfoResponse(RadioResponseInfo responseInfo, String[] data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(data));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getCurrentPOLListResponse(RadioResponseInfo responseInfo, String[] polList) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        ArrayList<NetworkInfoWithAcT> NetworkInfos = null;
        if (rr != null) {
            if (responseInfo.error == 0) {
                if (polList.length % 4 != 0) {
                    this.mMtkRil.riljLog("RIL_REQUEST_GET_POL_LIST: invalid response. Got " + polList.length + " strings, expected multible of 4");
                } else {
                    ArrayList<NetworkInfoWithAcT> NetworkInfos2 = new ArrayList<>(polList.length / 4);
                    for (int i = 0; i < polList.length; i += 4) {
                        String strOperName = null;
                        String strOperNumeric = null;
                        int nIndex = polList[i] != null ? Integer.parseInt(polList[i]) : 0;
                        if (polList[i + 1] != null) {
                            int format = Integer.parseInt(polList[i + 1]);
                            switch (format) {
                                case 0:
                                case 1:
                                    strOperName = polList[i + 2];
                                    break;
                                case 2:
                                    if (polList[i + 2] != null) {
                                        strOperNumeric = polList[i + 2];
                                        MtkRIL mtkRIL = this.mMtkRil;
                                        strOperName = mtkRIL.lookupOperatorName(getSubId(mtkRIL.mInstanceId.intValue()), strOperNumeric, true, -1);
                                    }
                                    break;
                            }
                        }
                        int format2 = i + 3;
                        int nAct = polList[format2] != null ? Integer.parseInt(polList[i + 3]) : 0;
                        if (strOperNumeric != null && !strOperNumeric.equals("?????")) {
                            NetworkInfos2.add(new NetworkInfoWithAcT(strOperName, strOperNumeric, nAct, nIndex));
                        }
                    }
                    this.mMtkRil.getMtkRadioResponse();
                    MtkRadioResponse.sendMessageResponse(rr.mResult, NetworkInfos2);
                    NetworkInfos = NetworkInfos2;
                }
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, NetworkInfos);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getDeactivateNrScgCommunicationResponse(RadioResponseInfo responseInfo, int deactivate, int allowSCGAdd) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{deactivate, allowSCGAdd});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getDisable2GResponse(RadioResponseInfo responseInfo, int mode) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{mode});
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void responseFemtoCellInfos(RadioResponseInfo responseInfo, String[] info) {
        Phone phone;
        boolean con;
        String actStr;
        int rat;
        boolean con2;
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        ArrayList<FemtoCellInfo> femtoInfos = null;
        String fPlmn = null;
        String fAct = null;
        String fCsgId = null;
        Phone phone2 = PhoneFactory.getPhone(this.mMtkRil.mInstanceId.intValue());
        if (phone2 != null) {
            ServiceStateTracker sst = phone2.getServiceStateTracker();
            ServiceState serviceState = sst.mSS;
            fPlmn = ((MtkServiceStateTracker) sst).getFemtoPlmn();
            fAct = ((MtkServiceStateTracker) sst).getFemtoAct();
            fCsgId = ((MtkServiceStateTracker) sst).getFemtoCsgId();
        }
        if (rr != null && responseInfo.error == 0) {
            if (info.length % 7 != 0) {
                throw new RuntimeException("responseFemtoCellInfos: invalid response. Got " + info.length + " strings, expected multible of 7");
            }
            ArrayList<FemtoCellInfo> femtoInfos2 = new ArrayList<>(info.length / 7);
            int i = 0;
            while (i < info.length) {
                if (info[i + 1] == null || !info[i + 1].startsWith("uCs2")) {
                    phone = phone2;
                } else {
                    this.mMtkRil.riljLog("responseFemtoCellInfos handling UCS2 format name");
                    try {
                        phone = phone2;
                        try {
                            info[i + 0] = new String(IccUtils.hexStringToBytes(info[i + 1].substring(4)), "UTF-16");
                        } catch (UnsupportedEncodingException e) {
                            this.mMtkRil.riljLog("responseFemtoCellInfos UnsupportedEncodingException");
                        }
                    } catch (UnsupportedEncodingException e2) {
                        phone = phone2;
                    }
                }
                if (info[i + 1] == null) {
                    con = false;
                } else if (!info[i + 1].equals("") && !info[i + 1].equals(info[i + 0])) {
                    con = false;
                } else {
                    this.mMtkRil.riljLog("lookup RIL responseFemtoCellInfos() for plmn id= " + info[i + 0]);
                    MtkRIL mtkRIL = this.mMtkRil;
                    con = false;
                    info[i + 1] = mtkRIL.lookupOperatorName(getSubId(mtkRIL.mInstanceId.intValue()), info[i + 0], true, -1);
                }
                int sig = Integer.valueOf(info[i + 6]).intValue();
                if (info[i + 2] != null && info[i + 2].equals(MtkGsmCdmaPhone.ACT_TYPE_LTE)) {
                    actStr = MtkGsmCdmaPhone.LTE_INDICATOR;
                    rat = 14;
                } else if (info[i + 2] != null && info[i + 2].equals(MtkGsmCdmaPhone.ACT_TYPE_UTRAN)) {
                    actStr = MtkGsmCdmaPhone.UTRAN_INDICATOR;
                    rat = 3;
                } else {
                    actStr = MtkGsmCdmaPhone.GSM_INDICATOR;
                    rat = 1;
                }
                info[i + 1] = info[i + 1].concat(" " + actStr);
                String hnbName = new String(IccUtils.hexStringToBytes(info[i + 5]));
                if (info[i + 0] != null && info[i + 0].equals(fPlmn) && info[i + 2] != null && info[i + 2].equals(fAct) && info[i + 3] != null && info[i + 3].equals(fCsgId)) {
                    con2 = true;
                } else {
                    con2 = con;
                }
                MtkRIL mtkRIL2 = this.mMtkRil;
                StringBuilder sbAppend = new StringBuilder().append("FemtoCellInfo(").append(info[i + 3]).append(",");
                String fPlmn2 = fPlmn;
                String fPlmn3 = info[i + 4];
                mtkRIL2.riljLog(sbAppend.append(fPlmn3).append(",").append(info[i + 5]).append(",").append(info[i + 0]).append(",").append(info[i + 1]).append(",").append(rat).append(") hnbName=").append(hnbName).append(",sig=").append(sig).append(",con=").append(con2).toString());
                femtoInfos2.add(new FemtoCellInfo(Integer.parseInt(info[i + 3]), Integer.parseInt(info[i + 4]), hnbName, info[i + 0], info[i + 1], rat, con2, sig));
                i += 7;
                phone2 = phone;
                fPlmn = fPlmn2;
            }
            this.mMtkRil.getMtkRadioResponse();
            MtkRadioResponse.sendMessageResponse(rr.mResult, femtoInfos2);
            femtoInfos = femtoInfos2;
        }
        if (rr != null) {
            this.mMtkRil.processResponseDone(rr, responseInfo, femtoInfos);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getFemtocellListResponse(RadioResponseInfo responseInfo, String[] femtoList) {
        responseFemtoCellInfos(responseInfo, femtoList);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getIWlanRegistrationStateResponse(RadioResponseInfo responseInfo, int state) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{state});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getLte1xRttCellListResponse(RadioResponseInfo responseInfo, Lte1xRttCellInfo[] list) {
        this.mMtkRil.riljLog("getLte1xRttCellListResponse, list.size= " + list.length);
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, list);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, list);
            return;
        }
        this.mMtkRil.riljLog("sendRsuRequestResponse, rr is null");
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getLteBsrTimerResponse(RadioResponseInfo responseInfo, int timer) {
        this.mMtkRil.riljLog("getLteBsrTimerResponse, timer = " + timer);
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{timer});
    }

    private void responseLteData(RadioResponseInfo responseInfo, LteData data) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        if (rr != null) {
            Object ret = null;
            if (responseInfo.error == 0) {
                int regState = data.state;
                this.mMtkRil.riljLog("responseLteData: from AIDL: " + regState);
                ret = data;
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, ret);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, ret);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getLteDataResponse(RadioResponseInfo responseInfo, LteData data) {
        responseLteData(responseInfo, data);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getLteRRCStateResponse(RadioResponseInfo responseInfo, int state) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{state});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getLteReleaseVersionResponse(RadioResponseInfo responseInfo, int mode) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{mode});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getLteScanDurationResponse(RadioResponseInfo responseInfo, int duration) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{duration});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getPOLCapabilityResponse(RadioResponseInfo responseInfo, int[] polCapability) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(polCapability));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getPlmnNameFromSE13TableResponse(RadioResponseInfo responseInfo, String name) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStrings(4, this.mMtkRil, responseInfo, new String[]{name});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getQamEnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, radioResponseInfo, new int[]{z ? 1 : 0, z2 ? 1 : 0});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getRoamingEnableResponse(RadioResponseInfo responseInfo, int[] data) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseIntArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(data));
    }

    private void responseGetSignalStrengthWithWcdmaEcio(RadioResponseInfo responseInfo, SignalStrengthWithWcdmaEcio signalStrength) {
        com.android.internal.telephony.RILRequest rr = this.mMtkRil.processResponse(4, responseInfo);
        if (rr != null) {
            if (responseInfo.error == 0) {
                this.mMtkRil.getMtkRadioResponse();
                MtkRadioResponse.sendMessageResponse(rr.mResult, signalStrength);
            }
            this.mMtkRil.processResponseDone(rr, responseInfo, signalStrength);
        }
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getSignalStrengthWithWcdmaEcioResponse(RadioResponseInfo responseInfo, SignalStrengthWithWcdmaEcio signalStrength) {
        responseGetSignalStrengthWithWcdmaEcio(responseInfo, signalStrength);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getSuggestedPlmnListResponse(RadioResponseInfo responseInfo, String[] plmnList) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStringArrayList(4, this.mMtkRil, responseInfo, RILUtils.primitiveArrayToArrayList(plmnList));
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getTOEInfoResponse(RadioResponseInfo responseInfo, String longName, String shortName, String numeric) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseStrings(4, this.mMtkRil, responseInfo, new String[]{longName, shortName, numeric});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void getTm9EnabledResponse(RadioResponseInfo radioResponseInfo, boolean z, boolean z2) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, radioResponseInfo, new int[]{z ? 1 : 0, z2 ? 1 : 0});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void queryFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo, int mode) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseInts(4, this.mMtkRil, responseInfo, new int[]{mode});
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void selectFemtocellResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void set4x4MimoEnabledResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setApcModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setBandPriorityListResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setBgsrchDeltaSleepTimerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setCALinkEnableStatusResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setDisable2GResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setFemtoCellSystemSelectionModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setLteBandEnableStatusResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setLteBsrTimerResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setLteReleaseVersionResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setLteScanDurationResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setNROptionResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setNetworkSelectionModeManualWithActResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setNrBandModeResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setPOLEntryResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setQamEnabledResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setSearchRatResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setSearchStoredFreqInfoResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setServiceStateToModemResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setTm9EnabledResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public void setRoamingEnableResponse(RadioResponseInfo responseInfo) {
        this.mMtkRil.getMtkRadioResponse();
        MtkRadioResponse.responseVoid(4, this.mMtkRil, responseInfo);
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public String getInterfaceHash() {
        return "583aefd4764eb70d99325928242c9ced29a9c0ee";
    }

    @Override // vendor.mediatek.hardware.mtkradioex.network.IMtkRadioExNetworkResponse
    public int getInterfaceVersion() {
        return 1;
    }
}
