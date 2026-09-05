package com.mediatek.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.ims.ImsException;
import com.android.internal.telephony.Phone;
import com.mediatek.internal.telephony.imsphone.MtkImsPhone;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class MtkSuppServUtTest {
    private static final int ACTION_ACTIVATE = 1;
    private static final int ACTION_DEACTIVATE = 0;
    private static final int ACTION_INTERROGATE = 2;
    static final String ACTION_SUPPLEMENTARY_SERVICE_ROAMING_TEST = "android.intent.action.ACTION_SUPPLEMENTARY_SERVICE_ROAMING_TEST";
    static final String ACTION_SUPPLEMENTARY_SERVICE_UT_TEST = "android.intent.action.ACTION_SUPPLEMENTARY_SERVICE_UT_TEST";
    private static final String EXTRA_ACTION = "action";
    private static final String EXTRA_PHONE_ID = "phoneId";
    private static final String EXTRA_SERVICE_CODE = "serviceCode";
    private static final String EXTRA_SERVICE_INFO_A = "serviceInfoA";
    private static final String EXTRA_SERVICE_INFO_B = "serviceInfoB";
    private static final String EXTRA_SERVICE_INFO_C = "serviceInfoC";
    private static final String EXTRA_SERVICE_INFO_D = "serviceInfoD";
    static final String LOG_TAG = "MtkSuppServUtTest";
    private static final int NUM_PRESENTATION_ALLOWED = 0;
    private static final int NUM_PRESENTATION_RESTRICTED = 1;
    private static final String SC_BAIC = "35";
    private static final String SC_BAICr = "351";
    private static final String SC_CFB = "67";
    private static final String SC_CFNR = "62";
    private static final String SC_CFNRy = "61";
    private static final String SC_CFNotRegister = "68";
    private static final String SC_CFU = "21";
    private static final String SC_CFUR = "22";
    private static final String SC_CLIP = "30";
    private static final String SC_CLIR = "31";
    private static final String SC_COLP = "76";
    private static final String SC_COLR = "77";
    private static final String SC_WAIT = "43";
    static final String SUPPLEMENTARY_SERVICE_PERMISSION = "com.mediatek.permission.SUPPLEMENTARY_SERVICE_UT_TEST";
    private MtkImsPhone activeImsPhone;
    private MtkGsmCdmaPhone activePhone;
    private Context mContext;
    private int phoneId;
    private String serviceCode;
    private String serviceInfoA;
    private String serviceInfoB;
    private String serviceInfoC;
    private String serviceInfoD;
    private int ssAction;

    public MtkSuppServUtTest(Context context, Intent intent, Phone phone) {
        this.ssAction = intent.getIntExtra(EXTRA_ACTION, -1);
        this.serviceCode = intent.getStringExtra(EXTRA_SERVICE_CODE);
        this.serviceInfoA = intent.getStringExtra(EXTRA_SERVICE_INFO_A);
        this.serviceInfoB = intent.getStringExtra(EXTRA_SERVICE_INFO_B);
        this.serviceInfoC = intent.getStringExtra(EXTRA_SERVICE_INFO_C);
        this.serviceInfoD = intent.getStringExtra(EXTRA_SERVICE_INFO_D);
        this.phoneId = getValidPhoneId(intent.getIntExtra(EXTRA_PHONE_ID, -1));
        this.mContext = context;
        MtkGsmCdmaPhone mtkGsmCdmaPhone = (MtkGsmCdmaPhone) phone;
        this.activePhone = mtkGsmCdmaPhone;
        this.activeImsPhone = mtkGsmCdmaPhone.getImsPhone();
    }

    void run() {
        Log.d(LOG_TAG, "onReceive, ssAction = " + this.ssAction + ", serviceCode = " + this.serviceCode + ", serviceInfoA = " + this.serviceInfoA + ", serviceInfoB = " + this.serviceInfoB + ", serviceInfoC = " + this.serviceInfoC + ", serviceInfoD = " + this.serviceInfoD + ", phoneId = " + this.phoneId);
        try {
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
        if (!isServiceCodeCallForwarding(this.serviceCode)) {
            if (!isServiceCodeCallBarring(this.serviceCode)) {
                String str = this.serviceCode;
                if (str == null || !str.equals(SC_WAIT)) {
                    String str2 = this.serviceCode;
                    if (str2 != null && str2.equals(SC_CFUR)) {
                        if (!isActivate(this.ssAction) && !isDeactivate(this.ssAction)) {
                            Log.d(LOG_TAG, "onReceive: Not supported service code");
                            return;
                        }
                        Intent rIntent = new Intent(ACTION_SUPPLEMENTARY_SERVICE_ROAMING_TEST);
                        rIntent.putExtra(EXTRA_PHONE_ID, this.phoneId);
                        this.mContext.sendBroadcast(rIntent);
                        return;
                    }
                    String str3 = this.serviceCode;
                    int i = 1;
                    if (str3 == null || !str3.equals(SC_CLIR)) {
                        String str4 = this.serviceCode;
                        if (str4 == null || !str4.equals(SC_CLIP)) {
                            String str5 = this.serviceCode;
                            if (str5 == null || !str5.equals(SC_COLR)) {
                                String str6 = this.serviceCode;
                                if (str6 != null && str6.equals(SC_COLP)) {
                                    checkIMSStatus(SC_COLP);
                                    boolean colpMode = isActivate(this.ssAction);
                                    if (isInterrogate(this.ssAction)) {
                                        try {
                                            this.activeImsPhone.getCallTracker().getUtInterface().queryCOLP((Message) null);
                                        } catch (ImsException e2) {
                                            Log.d(LOG_TAG, "processCode: Could not get UT handle for queryCOLP.");
                                        }
                                    } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
                                        try {
                                            this.activeImsPhone.getCallTracker().getUtInterface().updateCOLP(colpMode, (Message) null);
                                        } catch (ImsException e3) {
                                            Log.d(LOG_TAG, "processCode: Could not get UT handle for updateCOLP.");
                                        }
                                    } else {
                                        Log.d(LOG_TAG, "onReceive: Not supported SS action");
                                    }
                                    return;
                                }
                                Log.d(LOG_TAG, "onReceive: Not supported service code");
                                return;
                            }
                            checkIMSStatus(SC_COLR);
                            if (!isActivate(this.ssAction)) {
                                i = 0;
                            }
                            int colrMode = i;
                            if (isInterrogate(this.ssAction)) {
                                try {
                                    this.activeImsPhone.getCallTracker().getUtInterface().queryCOLR((Message) null);
                                } catch (ImsException e4) {
                                    Log.d(LOG_TAG, "processCode: Could not get UT handle for queryCOLR.");
                                }
                            } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
                                try {
                                    this.activeImsPhone.getCallTracker().getUtInterface().updateCOLR(colrMode, (Message) null);
                                } catch (ImsException e5) {
                                    Log.d(LOG_TAG, "processCode: Could not get UT handle for updateCOLR.");
                                }
                            } else {
                                Log.d(LOG_TAG, "onReceive: Not supported SS action");
                            }
                            return;
                        }
                        checkIMSStatus(SC_CLIP);
                        boolean clipMode = isActivate(this.ssAction);
                        if (isInterrogate(this.ssAction)) {
                            try {
                                this.activeImsPhone.getCallTracker().getUtInterface().queryCLIP((Message) null);
                            } catch (ImsException e6) {
                                Log.d(LOG_TAG, "Could not get UT handle for queryCLIP.");
                            }
                        } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
                            try {
                                this.activeImsPhone.getCallTracker().getUtInterface().updateCLIP(clipMode, (Message) null);
                            } catch (ImsException e7) {
                                Log.d(LOG_TAG, "Could not get UT handle for updateCLIP.");
                            }
                        } else {
                            Log.d(LOG_TAG, "onReceive: Not supported SS action");
                        }
                        return;
                        e.printStackTrace();
                        return;
                    }
                    checkIMSStatus(SC_CLIR);
                    if (!isActivate(this.ssAction)) {
                        i = 2;
                    }
                    int clirMode = i;
                    if (isInterrogate(this.ssAction)) {
                        this.activePhone.getOutgoingCallerIdDisplay(null);
                    } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
                        this.activePhone.setOutgoingCallerIdDisplay(clirMode, null);
                    } else {
                        Log.d(LOG_TAG, "onReceive: Not supported SS action");
                    }
                    return;
                }
                boolean enable = isActivate(this.ssAction);
                siToServiceClass(this.serviceInfoA);
                Message msg = Message.obtain(new Handler());
                if (isInterrogate(this.ssAction)) {
                    this.activePhone.getCallWaiting(msg);
                } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
                    this.activePhone.setCallWaiting(enable, msg);
                } else {
                    Log.d(LOG_TAG, "onReceive: Not supported SS action");
                }
                return;
            }
            boolean lockState = isActivate(this.ssAction);
            String facility = scToBarringFacility(this.serviceCode);
            siToServiceClass(this.serviceInfoB);
            if (isInterrogate(this.ssAction)) {
                this.activePhone.getCallBarring(facility, "1234", null);
            } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
                this.activePhone.setCallBarring(facility, lockState, "1234", null);
            } else {
                Log.d(LOG_TAG, "onReceive: Not supported SS action");
            }
            return;
        }
        int cfAction = actionToCommandAction(this.ssAction);
        int reason = scToCallForwardReason(this.serviceCode);
        String dialingNumber = this.serviceInfoA;
        siToServiceClass(this.serviceInfoB);
        int time = siToTime(this.serviceInfoC);
        long[] timeSlot = convertToLongTime(this.serviceInfoD);
        Message msg2 = Message.obtain(new Handler());
        if (isInterrogate(this.ssAction)) {
            if (reason == 0) {
                this.activePhone.getCallForwardInTimeSlot(reason, msg2);
            } else {
                this.activePhone.getCallForwardingOption(reason, msg2);
            }
        } else if (isActivate(this.ssAction) || isDeactivate(this.ssAction)) {
            if (reason == 0 && timeSlot != null) {
                Log.d(LOG_TAG, "onReceive: setCallForwardInTimeSlot");
                this.activePhone.setCallForwardInTimeSlot(cfAction, reason, dialingNumber, time, timeSlot, msg2);
            } else {
                Log.d(LOG_TAG, "onReceive: setCallForwardingOption");
                this.activePhone.setCallForwardingOption(cfAction, reason, dialingNumber, time, msg2);
            }
        } else {
            Log.d(LOG_TAG, "onReceive: Not supported SS action");
        }
    }

    private static int getValidPhoneId(int phoneId) {
        if (phoneId >= 0 && phoneId < TelephonyManager.getDefault().getPhoneCount()) {
            return phoneId;
        }
        return 0;
    }

    private static boolean isServiceCodeCallForwarding(String sc) {
        return sc != null && (sc.equals(SC_CFU) || sc.equals(SC_CFB) || sc.equals(SC_CFNRy) || sc.equals(SC_CFNR) || sc.equals(SC_CFNotRegister));
    }

    private static boolean isServiceCodeCallBarring(String sc) {
        return sc != null && (sc.equals(SC_BAIC) || sc.equals(SC_BAICr));
    }

    private static boolean isActivate(int action) {
        return action == 1;
    }

    private static boolean isDeactivate(int action) {
        return action == 0;
    }

    private static boolean isInterrogate(int action) {
        return action == 2;
    }

    private void checkIMSStatus(String reason) {
        MtkImsPhone mtkImsPhone = this.activeImsPhone;
        if (mtkImsPhone == null || (mtkImsPhone.getServiceState().getState() != 0 && !mtkImsPhone.isUtEnabled())) {
            Log.d(LOG_TAG, "checkIMSStatus: IMS is not registered or not Ut enabled, code: " + reason);
        } else {
            Log.d(LOG_TAG, "checkIMSStatus: ready, code: " + reason);
        }
    }

    private static int actionToCommandAction(int action) {
        switch (action) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 2;
            default:
                throw new RuntimeException("invalid action command");
        }
    }

    private static int scToCallForwardReason(String sc) {
        if (sc == null) {
            throw new RuntimeException("invalid call forward sc");
        }
        if (sc.equals(SC_CFU)) {
            return 0;
        }
        if (sc.equals(SC_CFB)) {
            return 1;
        }
        if (sc.equals(SC_CFNR)) {
            return 3;
        }
        if (sc.equals(SC_CFNRy)) {
            return 2;
        }
        if (sc.equals(SC_CFNotRegister)) {
            return 6;
        }
        throw new RuntimeException("invalid call forward sc");
    }

    private static String scToBarringFacility(String sc) {
        if (sc == null) {
            throw new RuntimeException("invalid call barring sc");
        }
        if (sc.equals(SC_BAIC)) {
            return "AI";
        }
        if (sc.equals(SC_BAICr)) {
            return "IR";
        }
        throw new RuntimeException("invalid call barring sc");
    }

    private static int siToServiceClass(String si) {
        if (si == null || si.length() == 0) {
            return 0;
        }
        int serviceCode = Integer.parseInt(si, 10);
        switch (serviceCode) {
            case 1:
                return 1;
            case 2:
                return 512;
            default:
                throw new RuntimeException("unsupported service class " + si);
        }
    }

    private static int siToTime(String si) {
        if (si == null || si.length() == 0) {
            return 0;
        }
        return Integer.parseInt(si, 10);
    }

    private static long[] convertToLongTime(String timeSlotString) {
        long[] timeSlot = null;
        if (timeSlotString != null) {
            String[] timeArray = timeSlotString.split(",", 2);
            if (timeArray.length == 2) {
                timeSlot = new long[2];
                for (int i = 0; i < 2; i++) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    try {
                        Date date = dateFormat.parse(timeArray[i]);
                        timeSlot[i] = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return timeSlot;
    }
}
