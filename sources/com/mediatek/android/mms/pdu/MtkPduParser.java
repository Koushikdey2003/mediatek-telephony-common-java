package com.mediatek.android.mms.pdu;

import android.util.Log;
import com.google.android.mms.pdu.CharacterSets;
import com.google.android.mms.pdu.PduContentTypes;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPart;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MtkPduParser extends PduParser {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String LOG_TAG = "MtkPduParser";

    public MtkPduParser(byte[] pduDataStream, boolean parseContentDisposition) {
        super(pduDataStream, parseContentDisposition);
    }

    protected boolean parsePartHeaders(ByteArrayInputStream pduDataStream, PduPart part, int length) {
        int startPos = pduDataStream.available();
        int lastLen = 0;
        int lastLen2 = length;
        while (lastLen2 > 0) {
            int header = pduDataStream.read();
            lastLen2--;
            Log.v(LOG_TAG, "Part headers: " + header);
            if (header > 127) {
                switch (header) {
                    case 142:
                        byte[] contentLocation = parseWapString(pduDataStream, 0);
                        if (contentLocation != null) {
                            part.setContentLocation(contentLocation);
                        }
                        int tempPos = pduDataStream.available();
                        lastLen2 = length - (startPos - tempPos);
                        lastLen = tempPos;
                        break;
                    case MtkPduPart.P_TRANSFER_ENCODING /* 167 */:
                        byte[] transferEncoding = parseWapString(pduDataStream, 0);
                        if (transferEncoding != null) {
                            part.setContentTransferEncoding(transferEncoding);
                        }
                        int tempPos2 = pduDataStream.available();
                        lastLen2 = length - (startPos - tempPos2);
                        lastLen = tempPos2;
                        break;
                    case 174:
                    case 197:
                        if (this.mParseContentDisposition) {
                            int len = parseValueLength(pduDataStream);
                            pduDataStream.mark(1);
                            int thisStartPos = pduDataStream.available();
                            int value = pduDataStream.read();
                            if (value == 128) {
                                part.setContentDisposition(PduPart.DISPOSITION_FROM_DATA);
                            } else if (value == 129) {
                                part.setContentDisposition(PduPart.DISPOSITION_ATTACHMENT);
                            } else if (value == 130) {
                                part.setContentDisposition(PduPart.DISPOSITION_INLINE);
                            } else {
                                pduDataStream.reset();
                                part.setContentDisposition(parseWapString(pduDataStream, 0));
                            }
                            if (thisStartPos - pduDataStream.available() < len) {
                                if (pduDataStream.read() == 152) {
                                    part.setFilename(parseWapString(pduDataStream, 0));
                                }
                                int thisEndPos = pduDataStream.available();
                                if (thisStartPos - thisEndPos < len) {
                                    int last = len - (thisStartPos - thisEndPos);
                                    byte[] temp = new byte[last];
                                    int readLen = pduDataStream.read(temp, 0, last);
                                    if (readLen < last) {
                                        Log.e(LOG_TAG, "Error happened when skipping paramlast = " + last + " but readLen = " + readLen);
                                    }
                                }
                            }
                            lastLen = pduDataStream.available();
                            lastLen2 = length - (startPos - lastLen);
                        }
                        break;
                    case 192:
                        byte[] contentId = parseWapString(pduDataStream, 1);
                        if (contentId != null) {
                            part.setContentId(contentId);
                        }
                        lastLen = pduDataStream.available();
                        lastLen2 = length - (startPos - lastLen);
                        break;
                    default:
                        int tempPos3 = lastLen;
                        int tempPos4 = skipWapValue(pduDataStream, lastLen2);
                        if (-1 == tempPos4) {
                            Log.e(LOG_TAG, "Corrupt Part headers");
                            return false;
                        }
                        lastLen2 = 0;
                        lastLen = tempPos3;
                        break;
                        break;
                }
            } else {
                int tempPos5 = lastLen;
                if (header >= 32 && header <= 127) {
                    byte[] tempHeader = parseWapString(pduDataStream, 0);
                    byte[] tempValue = parseWapString(pduDataStream, 0);
                    if (true == "Content-Transfer-Encoding".equalsIgnoreCase(new String(tempHeader))) {
                        part.setContentTransferEncoding(tempValue);
                    }
                    int tempPos6 = pduDataStream.available();
                    int lastLen3 = length - (startPos - tempPos6);
                    lastLen2 = lastLen3;
                    lastLen = tempPos6;
                } else {
                    if (-1 == skipWapValue(pduDataStream, lastLen2)) {
                        Log.e(LOG_TAG, "Corrupt Part headers");
                        return false;
                    }
                    lastLen2 = 0;
                    lastLen = tempPos5;
                }
            }
        }
        if (lastLen2 == 0) {
            return true;
        }
        Log.e(LOG_TAG, "Corrupt Part headers");
        return false;
    }

    protected void parseContentTypeParams(ByteArrayInputStream pduDataStream, HashMap<Integer, Object> map, Integer length) {
        byte[] type;
        int startPos = pduDataStream.available();
        int lastLen = length.intValue();
        while (lastLen > 0) {
            int param = pduDataStream.read();
            lastLen--;
            switch (param) {
                case 129:
                    pduDataStream.mark(1);
                    int firstValue = extractByteValue(pduDataStream);
                    pduDataStream.reset();
                    if ((firstValue > 32 && firstValue < 127) || firstValue == 0) {
                        byte[] charsetStr = parseWapString(pduDataStream, 0);
                        try {
                            int charsetInt = CharacterSets.getMibEnumValue(new String(charsetStr));
                            if (map != null) {
                                map.put(129, Integer.valueOf(charsetInt));
                            }
                        } catch (UnsupportedEncodingException e) {
                            Log.e(LOG_TAG, Arrays.toString(charsetStr), e);
                            if (map != null) {
                                map.put(129, 0);
                            }
                        }
                    } else {
                        int charset = (int) parseIntegerValue(pduDataStream);
                        if (map != null) {
                            map.put(129, Integer.valueOf(charset));
                        }
                    }
                    int tempPos = pduDataStream.available();
                    int lastLen2 = length.intValue() - (startPos - tempPos);
                    lastLen = lastLen2;
                    break;
                case 130:
                case 132:
                case 135:
                case 136:
                case 139:
                case 142:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 154:
                default:
                    if (-1 == skipWapValue(pduDataStream, lastLen)) {
                        Log.e(LOG_TAG, "Corrupt Content-Type");
                    } else {
                        lastLen = 0;
                    }
                    break;
                case 131:
                case 137:
                    pduDataStream.mark(1);
                    int first = extractByteValue(pduDataStream);
                    pduDataStream.reset();
                    if (first > 127) {
                        int index = parseShortInteger(pduDataStream);
                        if (index < PduContentTypes.contentTypes.length && (type = PduContentTypes.contentTypes[index].getBytes()) != null && map != null) {
                            map.put(131, type);
                        }
                    } else {
                        byte[] type2 = parseWapString(pduDataStream, 0);
                        if (type2 != null && map != null) {
                            map.put(131, type2);
                        }
                    }
                    int tempPos2 = pduDataStream.available();
                    int lastLen3 = length.intValue() - (startPos - tempPos2);
                    lastLen = lastLen3;
                    break;
                case 133:
                case 151:
                    byte[] name = parseWapString(pduDataStream, 0);
                    if (name != null && map != null) {
                        map.put(151, name);
                    }
                    int tempPos3 = pduDataStream.available();
                    int lastLen4 = length.intValue() - (startPos - tempPos3);
                    lastLen = lastLen4;
                    break;
                case 134:
                case 152:
                    byte[] fileName = parseWapString(pduDataStream, 0);
                    if (fileName != null && map != null) {
                        map.put(152, fileName);
                    }
                    int tempPos4 = pduDataStream.available();
                    int lastLen5 = length.intValue() - (startPos - tempPos4);
                    lastLen = lastLen5;
                    break;
                case 138:
                case 153:
                    byte[] start = parseWapString(pduDataStream, 0);
                    if (start != null && map != null) {
                        map.put(153, start);
                    }
                    int tempPos5 = pduDataStream.available();
                    int lastLen6 = length.intValue() - (startPos - tempPos5);
                    lastLen = lastLen6;
                    break;
                case 140:
                case 155:
                    byte[] comment = parseWapString(pduDataStream, 0);
                    if (comment != null && map != null) {
                        map.put(155, comment);
                    }
                    int tempPos6 = pduDataStream.available();
                    int lastLen7 = length.intValue() - (startPos - tempPos6);
                    lastLen = lastLen7;
                    break;
                case 141:
                case 156:
                    byte[] domain = parseWapString(pduDataStream, 0);
                    if (domain != null && map != null) {
                        map.put(156, domain);
                    }
                    int tempPos7 = pduDataStream.available();
                    int lastLen8 = length.intValue() - (startPos - tempPos7);
                    lastLen = lastLen8;
                    break;
                case 143:
                case 157:
                    byte[] path = parseWapString(pduDataStream, 0);
                    if (path != null && map != null) {
                        map.put(157, path);
                    }
                    int tempPos8 = pduDataStream.available();
                    int lastLen9 = length.intValue() - (startPos - tempPos8);
                    lastLen = lastLen9;
                    break;
            }
        }
        if (lastLen != 0) {
            Log.e(LOG_TAG, "Corrupt Content-Type");
        }
    }
}
