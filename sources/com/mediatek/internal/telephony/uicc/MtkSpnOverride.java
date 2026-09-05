package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.os.Environment;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Xml;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.util.XmlUtils;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.datasub.DataSubConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class MtkSpnOverride {
    private static HashMap<String, String> CarrierVirtualSpnMapByEfGid1 = null;
    private static HashMap<String, String> CarrierVirtualSpnMapByEfPnn = null;
    private static HashMap<String, String> CarrierVirtualSpnMapByEfSpn = null;
    static final String LOG_TAG = "SpnOverride";
    static final String LOG_TAG_EX = "MtkSpnOverride";
    protected static final String OEM_SPN_OVERRIDE_PATH = "telephony/spn-conf.xml";
    protected static final String PARTNER_SPN_OVERRIDE_PATH = "etc/spn-conf.xml";
    private static final String PARTNER_VIRTUAL_SPN_BY_EF_GID1_OVERRIDE_PATH = "etc/virtual-spn-conf-by-efgid1.xml";
    private static final String PARTNER_VIRTUAL_SPN_BY_EF_PNN_OVERRIDE_PATH = "etc/virtual-spn-conf-by-efpnn.xml";
    private static final String PARTNER_VIRTUAL_SPN_BY_EF_SPN_OVERRIDE_PATH = "etc/virtual-spn-conf-by-efspn.xml";
    private static final String PARTNER_VIRTUAL_SPN_BY_IMSI_OVERRIDE_PATH = "etc/virtual-spn-conf-by-imsi.xml";
    static final Object sInstSync = new Object();
    private static MtkSpnOverride sInstance;
    private ArrayList CarrierVirtualSpnMapByImsi;
    protected HashMap<String, String> mCarrierSpnMap = new HashMap<>();

    public class VirtualSpnByImsi {
        public String name;
        public String pattern;

        public VirtualSpnByImsi(String pattern, String name) {
            this.pattern = pattern;
            this.name = name;
        }
    }

    public static MtkSpnOverride getInstance() {
        MtkSpnOverride mtkSpnOverride;
        synchronized (sInstSync) {
            if (sInstance == null) {
                sInstance = new MtkSpnOverride();
            }
            mtkSpnOverride = sInstance;
        }
        return mtkSpnOverride;
    }

    MtkSpnOverride() {
        loadSpnOverrides();
        CarrierVirtualSpnMapByEfSpn = new HashMap<>();
        loadVirtualSpnOverridesByEfSpn();
        this.CarrierVirtualSpnMapByImsi = new ArrayList();
        loadVirtualSpnOverridesByImsi();
        CarrierVirtualSpnMapByEfPnn = new HashMap<>();
        loadVirtualSpnOverridesByEfPnn();
        CarrierVirtualSpnMapByEfGid1 = new HashMap<>();
        loadVirtualSpnOverridesByEfGid1();
    }

    protected void loadSpnOverrides() {
        File spnFile;
        StringBuilder sb;
        Rlog.d(LOG_TAG_EX, "loadSpnOverrides");
        if (DataSubConstants.OPERATOR_OP09.equals(SystemProperties.get(DataSubConstants.PROPERTY_OPERATOR_OPTR, ""))) {
            spnFile = new File(Environment.getVendorDirectory(), "etc/spn-conf-op09.xml");
            if (!spnFile.exists()) {
                Rlog.d(LOG_TAG_EX, "No spn-conf-op09.xml file");
                spnFile = new File(Environment.getSystemExtDirectory(), PARTNER_SPN_OVERRIDE_PATH);
            }
        } else {
            spnFile = new File(Environment.getSystemExtDirectory(), PARTNER_SPN_OVERRIDE_PATH);
        }
        File oemSpnFile = new File(Environment.getOemDirectory(), OEM_SPN_OVERRIDE_PATH);
        if (oemSpnFile.exists()) {
            long oemSpnTime = oemSpnFile.lastModified();
            long sysSpnTime = spnFile.lastModified();
            Rlog.d(LOG_TAG_EX, "SPN Timestamp: oemTime = " + oemSpnTime + " sysTime = " + sysSpnTime);
            if (oemSpnTime > sysSpnTime) {
                Rlog.d(LOG_TAG_EX, "SPN in OEM image is newer than System image");
                spnFile = oemSpnFile;
            }
        } else {
            Rlog.d(LOG_TAG_EX, "No SPN in OEM image = " + oemSpnFile.getPath() + " Load SPN from system image");
        }
        try {
            FileReader spnReader = new FileReader(spnFile);
            try {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(spnReader);
                    XmlUtils.beginDocument(parser, "spnOverrides");
                    while (true) {
                        XmlUtils.nextElement(parser);
                        String name = parser.getName();
                        if (!"spnOverride".equals(name)) {
                            try {
                                spnReader.close();
                                return;
                            } catch (IOException e) {
                                e = e;
                                sb = new StringBuilder();
                                Rlog.w(LOG_TAG_EX, sb.append("Exception in spn-conf parser ").append(e).toString());
                            }
                        }
                        String numeric = parser.getAttributeValue(null, "numeric");
                        String data = parser.getAttributeValue(null, "spn");
                        this.mCarrierSpnMap.put(numeric, data);
                    }
                } catch (Throwable th) {
                    try {
                        spnReader.close();
                    } catch (IOException e2) {
                        Rlog.w(LOG_TAG_EX, "Exception in spn-conf parser " + e2);
                    }
                    throw th;
                }
            } catch (IOException e3) {
                Rlog.w(LOG_TAG_EX, "Exception in spn-conf parser " + e3);
                try {
                    spnReader.close();
                } catch (IOException e4) {
                    e = e4;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in spn-conf parser ").append(e).toString());
                }
            } catch (XmlPullParserException e5) {
                Rlog.w(LOG_TAG_EX, "Exception in spn-conf parser " + e5);
                try {
                    spnReader.close();
                } catch (IOException e6) {
                    e = e6;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in spn-conf parser ").append(e).toString());
                }
            }
        } catch (FileNotFoundException e7) {
            Rlog.w(LOG_TAG_EX, "Can not open " + spnFile.getAbsolutePath());
        }
    }

    private static void loadVirtualSpnOverridesByEfSpn() {
        StringBuilder sb;
        Rlog.d(LOG_TAG_EX, "loadVirtualSpnOverridesByEfSpn");
        File spnFile = new File(Environment.getVendorDirectory(), PARTNER_VIRTUAL_SPN_BY_EF_SPN_OVERRIDE_PATH);
        try {
            FileReader spnReader = new FileReader(spnFile);
            try {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(spnReader);
                    XmlUtils.beginDocument(parser, "virtualSpnOverridesByEfSpn");
                    while (true) {
                        XmlUtils.nextElement(parser);
                        String name = parser.getName();
                        if (!"virtualSpnOverride".equals(name)) {
                            try {
                                spnReader.close();
                                return;
                            } catch (IOException e) {
                                e = e;
                                sb = new StringBuilder();
                                Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efspn parser ").append(e).toString());
                            }
                        }
                        String mccmncspn = parser.getAttributeValue(null, "mccmncspn");
                        String spn = parser.getAttributeValue(null, "name");
                        Rlog.w(LOG_TAG_EX, "test mccmncspn = " + mccmncspn + ", name = " + spn);
                        CarrierVirtualSpnMapByEfSpn.put(mccmncspn, spn);
                    }
                } catch (Throwable th) {
                    try {
                        spnReader.close();
                    } catch (IOException e2) {
                        Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efspn parser " + e2);
                    }
                    throw th;
                }
            } catch (IOException e3) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efspn parser " + e3);
                try {
                    spnReader.close();
                } catch (IOException e4) {
                    e = e4;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efspn parser ").append(e).toString());
                }
            } catch (XmlPullParserException e5) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efspn parser " + e5);
                try {
                    spnReader.close();
                } catch (IOException e6) {
                    e = e6;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efspn parser ").append(e).toString());
                }
            }
        } catch (FileNotFoundException e7) {
            Rlog.w(LOG_TAG_EX, "Can't open " + Environment.getVendorDirectory() + "/" + PARTNER_VIRTUAL_SPN_BY_EF_SPN_OVERRIDE_PATH);
        }
    }

    public String getSpnByEfSpn(String mccmnc, String spn) {
        if (mccmnc == null || spn == null || mccmnc.isEmpty() || spn.isEmpty()) {
            return null;
        }
        return CarrierVirtualSpnMapByEfSpn.get(mccmnc + spn);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r3v8, types: [java.io.FileReader, java.io.Reader] */
    private void loadVirtualSpnOverridesByImsi() {
        StringBuilder sb;
        Rlog.d(LOG_TAG_EX, "loadVirtualSpnOverridesByImsi");
        FileReader vendorDirectory = Environment.getVendorDirectory();
        File spnFile = new File((File) vendorDirectory, PARTNER_VIRTUAL_SPN_BY_IMSI_OVERRIDE_PATH);
        try {
            try {
                vendorDirectory = new FileReader(spnFile);
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(vendorDirectory);
                    XmlUtils.beginDocument(xmlPullParserNewPullParser, "virtualSpnOverridesByImsi");
                    while (true) {
                        XmlUtils.nextElement(xmlPullParserNewPullParser);
                        String name = xmlPullParserNewPullParser.getName();
                        if (!"virtualSpnOverride".equals(name)) {
                            try {
                                vendorDirectory.close();
                                return;
                            } catch (IOException e) {
                                e = e;
                                sb = new StringBuilder();
                                Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-imsi parser ").append(e).toString());
                            }
                        }
                        String imsipattern = xmlPullParserNewPullParser.getAttributeValue(null, "imsipattern");
                        String spn = xmlPullParserNewPullParser.getAttributeValue(null, "name");
                        Rlog.w(LOG_TAG_EX, "test imsipattern = " + imsipattern + ", name = " + spn);
                        this.CarrierVirtualSpnMapByImsi.add(new VirtualSpnByImsi(imsipattern, spn));
                    }
                } catch (IOException e2) {
                    Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-imsi parser " + e2);
                    try {
                        vendorDirectory.close();
                    } catch (IOException e3) {
                        e = e3;
                        sb = new StringBuilder();
                        Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-imsi parser ").append(e).toString());
                    }
                } catch (XmlPullParserException e4) {
                    Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-imsi parser " + e4);
                    try {
                        vendorDirectory.close();
                    } catch (IOException e5) {
                        e = e5;
                        sb = new StringBuilder();
                        Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-imsi parser ").append(e).toString());
                    }
                }
            } catch (FileNotFoundException e6) {
                Rlog.w(LOG_TAG_EX, "Can't open " + Environment.getVendorDirectory() + "/" + PARTNER_VIRTUAL_SPN_BY_IMSI_OVERRIDE_PATH);
            }
        } catch (Throwable th) {
            try {
                vendorDirectory.close();
            } catch (IOException e7) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-imsi parser " + e7);
            }
            throw th;
        }
    }

    public String getSpnByImsi(String mccmnc, String imsi) {
        if (mccmnc == null || imsi == null || mccmnc.isEmpty() || imsi.isEmpty()) {
            return null;
        }
        for (int i = 0; i < this.CarrierVirtualSpnMapByImsi.size(); i++) {
            VirtualSpnByImsi vsbi = (VirtualSpnByImsi) this.CarrierVirtualSpnMapByImsi.get(i);
            Rlog.d(LOG_TAG_EX, "getSpnByImsi(): mccmnc = " + mccmnc + ", imsi = " + (imsi.length() >= 6 ? imsi.substring(0, 6) : "xx") + ", pattern = " + vsbi.pattern);
            if (imsiMatches(vsbi.pattern, mccmnc + imsi)) {
                return vsbi.name;
            }
        }
        return null;
    }

    public String isOperatorMvnoForImsi(String mccmnc, String imsi) {
        if (mccmnc == null || imsi == null || mccmnc.isEmpty() || imsi.isEmpty()) {
            return null;
        }
        for (int i = 0; i < this.CarrierVirtualSpnMapByImsi.size(); i++) {
            VirtualSpnByImsi vsbi = (VirtualSpnByImsi) this.CarrierVirtualSpnMapByImsi.get(i);
            Rlog.w(LOG_TAG_EX, "isOperatorMvnoForImsi(): mccmnc = " + mccmnc + ", imsi = " + (imsi.length() >= 6 ? imsi.substring(0, 6) : "xx") + ", pattern = " + vsbi.pattern);
            if (imsiMatches(vsbi.pattern, mccmnc + imsi)) {
                return vsbi.pattern;
            }
        }
        return null;
    }

    private boolean imsiMatches(String imsiPatterns, String imsiSIM) {
        String strSubstring;
        int len = imsiPatterns.length();
        StringBuilder sbAppend = new StringBuilder().append("mvno match imsi = ");
        if (imsiSIM == null) {
            strSubstring = "";
        } else {
            strSubstring = imsiSIM.length() >= 6 ? imsiSIM.substring(0, 6) : "xx";
        }
        Rlog.d(LOG_TAG_EX, sbAppend.append(strSubstring).append("pattern = ").append(imsiPatterns).toString());
        if (len <= 0 || imsiSIM == null) {
            return false;
        }
        return imsiSIM.matches(imsiPatterns);
    }

    private static void loadVirtualSpnOverridesByEfPnn() {
        StringBuilder sb;
        Rlog.d(LOG_TAG_EX, "loadVirtualSpnOverridesByEfPnn");
        File spnFile = new File(Environment.getVendorDirectory(), PARTNER_VIRTUAL_SPN_BY_EF_PNN_OVERRIDE_PATH);
        try {
            FileReader spnReader = new FileReader(spnFile);
            try {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(spnReader);
                    XmlUtils.beginDocument(parser, "virtualSpnOverridesByEfPnn");
                    while (true) {
                        XmlUtils.nextElement(parser);
                        String name = parser.getName();
                        if (!"virtualSpnOverride".equals(name)) {
                            try {
                                spnReader.close();
                                return;
                            } catch (IOException e) {
                                e = e;
                                sb = new StringBuilder();
                                Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efpnn parser ").append(e).toString());
                            }
                        }
                        String mccmncpnn = parser.getAttributeValue(null, "mccmncpnn");
                        String spn = parser.getAttributeValue(null, "name");
                        Rlog.w(LOG_TAG_EX, "test mccmncpnn = " + mccmncpnn + ", name = " + spn);
                        CarrierVirtualSpnMapByEfPnn.put(mccmncpnn, spn);
                    }
                } catch (Throwable th) {
                    try {
                        spnReader.close();
                    } catch (IOException e2) {
                        Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efpnn parser " + e2);
                    }
                    throw th;
                }
            } catch (IOException e3) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efpnn parser " + e3);
                try {
                    spnReader.close();
                } catch (IOException e4) {
                    e = e4;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efpnn parser ").append(e).toString());
                }
            } catch (XmlPullParserException e5) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efpnn parser " + e5);
                try {
                    spnReader.close();
                } catch (IOException e6) {
                    e = e6;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efpnn parser ").append(e).toString());
                }
            }
        } catch (FileNotFoundException e7) {
            Rlog.w(LOG_TAG_EX, "Can't open " + Environment.getVendorDirectory() + "/" + PARTNER_VIRTUAL_SPN_BY_EF_PNN_OVERRIDE_PATH);
        }
    }

    public String getSpnByEfPnn(String mccmnc, String pnn) {
        if (mccmnc == null || pnn == null || mccmnc.isEmpty() || pnn.isEmpty()) {
            return null;
        }
        return CarrierVirtualSpnMapByEfPnn.get(mccmnc + pnn);
    }

    private static void loadVirtualSpnOverridesByEfGid1() {
        StringBuilder sb;
        Rlog.d(LOG_TAG_EX, "loadVirtualSpnOverridesByEfGid1");
        File spnFile = new File(Environment.getVendorDirectory(), PARTNER_VIRTUAL_SPN_BY_EF_GID1_OVERRIDE_PATH);
        try {
            FileReader spnReader = new FileReader(spnFile);
            try {
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(spnReader);
                    XmlUtils.beginDocument(parser, "virtualSpnOverridesByEfGid1");
                    while (true) {
                        XmlUtils.nextElement(parser);
                        String name = parser.getName();
                        if (!"virtualSpnOverride".equals(name)) {
                            try {
                                spnReader.close();
                                return;
                            } catch (IOException e) {
                                e = e;
                                sb = new StringBuilder();
                                Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efgid1 parser ").append(e).toString());
                            }
                        }
                        String mccmncgid1 = parser.getAttributeValue(null, "mccmncgid1");
                        String spn = parser.getAttributeValue(null, "name");
                        Rlog.w(LOG_TAG_EX, "test mccmncgid1 = " + mccmncgid1 + ", name = " + spn);
                        CarrierVirtualSpnMapByEfGid1.put(mccmncgid1, spn);
                    }
                } catch (Throwable th) {
                    try {
                        spnReader.close();
                    } catch (IOException e2) {
                        Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efgid1 parser " + e2);
                    }
                    throw th;
                }
            } catch (IOException e3) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efgid1 parser " + e3);
                try {
                    spnReader.close();
                } catch (IOException e4) {
                    e = e4;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efgid1 parser ").append(e).toString());
                }
            } catch (XmlPullParserException e5) {
                Rlog.w(LOG_TAG_EX, "Exception in virtual-spn-conf-by-efgid1 parser " + e5);
                try {
                    spnReader.close();
                } catch (IOException e6) {
                    e = e6;
                    sb = new StringBuilder();
                    Rlog.w(LOG_TAG_EX, sb.append("Exception in virtual-spn-conf-by-efgid1 parser ").append(e).toString());
                }
            }
        } catch (FileNotFoundException e7) {
            Rlog.w(LOG_TAG_EX, "Can't open " + Environment.getVendorDirectory() + "/" + PARTNER_VIRTUAL_SPN_BY_EF_GID1_OVERRIDE_PATH);
        }
    }

    public String getSpnByEfGid1(String mccmnc, String gid1) {
        if (mccmnc == null || gid1 == null || mccmnc.isEmpty() || gid1.isEmpty()) {
            return null;
        }
        return CarrierVirtualSpnMapByEfGid1.get(mccmnc + gid1);
    }

    public String getSpnByPattern(int subId, String numeric) {
        MtkGsmCdmaPhone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(subId));
        String mvnoOperName = getSpnByEfSpn(numeric, phone.getMvnoPattern("spn"));
        Rlog.d(LOG_TAG_EX, "the result of searching mvnoOperName by EF_SPN: " + mvnoOperName);
        if (mvnoOperName == null) {
            mvnoOperName = getSpnByImsi(numeric, phone.getSubscriberId());
            Rlog.d(LOG_TAG_EX, "the result of searching mvnoOperName by IMSI: " + mvnoOperName);
        }
        if (mvnoOperName == null) {
            mvnoOperName = getSpnByEfPnn(numeric, phone.getMvnoPattern("pnn"));
            Rlog.d(LOG_TAG_EX, "the result of searching mvnoOperName by EF_PNN: " + mvnoOperName);
        }
        if (mvnoOperName == null) {
            String mvnoOperName2 = getSpnByEfGid1(numeric, phone.getMvnoPattern("gid"));
            Rlog.d(LOG_TAG_EX, "the result of searching mvnoOperName by EF_GID1: " + mvnoOperName2);
            return mvnoOperName2;
        }
        return mvnoOperName;
    }

    private boolean isForceGetCtSpnFromRes(int subId, String numeric, Context context, String mvnoOperName) {
        boolean getFromResource = false;
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(subId));
        String ctName = context.getText(134545505).toString();
        String simCarrierName = TelephonyManager.from(context).getSimOperatorName(subId);
        Rlog.d(LOG_TAG_EX, "ctName:" + ctName + ", simCarrierName:" + simCarrierName + ", subId:" + subId);
        if (ctName != null && (ctName.equals(mvnoOperName) || ctName.equals(simCarrierName))) {
            Rlog.d(LOG_TAG_EX, "Get from resource.");
            getFromResource = true;
        }
        if (("20404".equals(numeric) || "45403".equals(numeric)) && phone.getPhoneType() == 2 && ctName != null && ctName.equals(simCarrierName)) {
            Rlog.d(LOG_TAG_EX, "Special handle for roaming case!");
            return true;
        }
        return getFromResource;
    }

    public String getSpnByNumeric(String numeric, boolean desireLongName, Context context) {
        return getSpnByNumeric(numeric, desireLongName, context, false, true);
    }

    private String getSpnByNumeric(String numeric, boolean desireLongName, Context context, boolean getCtSpn, boolean getDefaultSpn) {
        if (desireLongName) {
            if (numeric.equals("46000") || numeric.equals("46002") || numeric.equals("46004") || numeric.equals("46007") || numeric.equals("46008")) {
                String operName = context.getText(134545437).toString();
                return operName;
            }
            if (numeric.equals("46001") || numeric.equals("46009") || numeric.equals("45407") || numeric.equals("46030")) {
                String operName2 = context.getText(134545438).toString();
                return operName2;
            }
            if (numeric.equals("46003") || numeric.equals("46011") || getCtSpn) {
                String operName3 = context.getText(134545507).toString();
                return operName3;
            }
            if (numeric.equals("46015")) {
                String operName4 = context.getText(134545451).toString();
                return operName4;
            }
            if (numeric.equals("46601")) {
                String operName5 = context.getText(134545439).toString();
                return operName5;
            }
            if (!numeric.equals("46692")) {
                if (!numeric.equals("46697")) {
                    if (!numeric.equals("99998")) {
                        if (numeric.equals("99999")) {
                            String operName6 = context.getText(134545443).toString();
                            return operName6;
                        }
                        if (!getDefaultSpn || !containsCarrier(numeric)) {
                            Rlog.d(LOG_TAG_EX, "Can't find long operator name for " + numeric);
                        } else {
                            String operName7 = getSpn(numeric);
                            return operName7;
                        }
                    } else {
                        String operName8 = context.getText(134545442).toString();
                        return operName8;
                    }
                } else {
                    String operName9 = context.getText(134545441).toString();
                    return operName9;
                }
            } else {
                String operName10 = context.getText(134545440).toString();
                return operName10;
            }
        } else if (!desireLongName) {
            if (numeric.equals("46000") || numeric.equals("46002") || numeric.equals("46004") || numeric.equals("46007") || numeric.equals("46008")) {
                String operName11 = context.getText(134545444).toString();
                return operName11;
            }
            if (numeric.equals("46001") || numeric.equals("46009") || numeric.equals("45407") || numeric.equals("46030")) {
                String operName12 = context.getText(134545445).toString();
                return operName12;
            }
            if (numeric.equals("46003") || numeric.equals("46011") || getCtSpn) {
                String operName13 = context.getText(134545508).toString();
                return operName13;
            }
            if (numeric.equals("46015")) {
                String operName14 = context.getText(134545452).toString();
                return operName14;
            }
            if (numeric.equals("46601")) {
                String operName15 = context.getText(134545446).toString();
                return operName15;
            }
            if (!numeric.equals("46692")) {
                if (!numeric.equals("46697")) {
                    if (!numeric.equals("99997")) {
                        if (numeric.equals("99999")) {
                            String operName16 = context.getText(134545450).toString();
                            return operName16;
                        }
                        if (!getDefaultSpn || !containsCarrier(numeric)) {
                            Rlog.d(LOG_TAG_EX, "Can't find short operator name for " + numeric);
                        } else {
                            String operName17 = getSpn(numeric);
                            return operName17;
                        }
                    } else {
                        String operName18 = context.getText(134545449).toString();
                        return operName18;
                    }
                } else {
                    String operName19 = context.getText(134545448).toString();
                    return operName19;
                }
            } else {
                String operName20 = context.getText(134545447).toString();
                return operName20;
            }
        }
        return null;
    }

    public String lookupOperatorName(int subId, String numeric, boolean desireLongName, Context context, String defaultName) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(subId));
        if (phone == null) {
            Rlog.w(LOG_TAG_EX, "lookupOperatorName getPhone null");
            return defaultName;
        }
        String operName = getSpnByPattern(subId, numeric);
        boolean getCtSpn = isForceGetCtSpnFromRes(subId, numeric, context, operName);
        if (operName == null || getCtSpn) {
            operName = getSpnByNumeric(numeric, desireLongName, context, getCtSpn, true);
        }
        return operName == null ? defaultName : operName;
    }

    public String lookupOperatorName(int subId, String numeric, boolean desireLongName, Context context) {
        return lookupOperatorName(subId, numeric, desireLongName, context, numeric);
    }

    public String lookupOperatorNameForDisplayName(int subId, String numeric, boolean desireLongName, Context context) {
        return lookupOperatorName(subId, numeric, desireLongName, context, null);
    }

    public boolean containsCarrier(String carrier) {
        return this.mCarrierSpnMap.containsKey(carrier);
    }

    public String getSpn(String carrier) {
        return this.mCarrierSpnMap.get(carrier);
    }

    public boolean containsCarrierEx(String carrier) {
        return containsCarrier(carrier);
    }
}
