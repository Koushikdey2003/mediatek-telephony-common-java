package com.mediatek.internal.telephony.cat;

import android.content.Context;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.CallSetupParams;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParamsFactory;
import com.android.internal.telephony.cat.ComprehensionTlv;
import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cat.DisplayTextParams;
import com.android.internal.telephony.cat.IconId;
import com.android.internal.telephony.cat.Item;
import com.android.internal.telephony.cat.ItemsIconId;
import com.android.internal.telephony.cat.LaunchBrowserMode;
import com.android.internal.telephony.cat.LaunchBrowserParams;
import com.android.internal.telephony.cat.PresentationType;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.ResultException;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.cat.SelectItemParams;
import com.android.internal.telephony.cat.SetEventListParams;
import com.android.internal.telephony.cat.TextMessage;
import com.android.internal.telephony.cat.ValueParser;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkCommandParamsFactory extends CommandParamsFactory {
    public static final int BATTERY_STATE = 10;
    private Context mContext;
    int tlvIndex;

    public MtkCommandParamsFactory(RilMessageDecoder caller, IccFileHandler fh, Context context) {
        super(caller, fh, context);
        this.tlvIndex = -1;
        this.mContext = context;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected boolean processSelectItem(CommandDetails cmdDet, List<ComprehensionTlv> ctlvs) throws ResultException {
        MtkCatLog.d(this, "process SelectItem");
        MtkMenu menu = new MtkMenu();
        IconId titleIconId = null;
        ItemsIconId itemsIconId = null;
        Iterator<ComprehensionTlv> iter = ctlvs.iterator();
        AppInterface.CommandType cmdType = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
        ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.ALPHA_ID, ctlvs);
        if (ctlv != null) {
            try {
                menu.title = MtkValueParser.retrieveAlphaId(ctlv, this.mNoAlphaUsrCnf);
            } catch (ResultException e) {
                MtkCatLog.e(this, "retrieveAlphaId ResultException: " + e.result());
            }
            MtkCatLog.d(this, "add AlphaId: " + menu.title);
        } else if (cmdType == AppInterface.CommandType.SET_UP_MENU) {
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
        while (true) {
            ComprehensionTlv ctlv2 = searchForNextTag(ComprehensionTlvTag.ITEM, iter);
            if (ctlv2 == null) {
                break;
            }
            Item item = MtkValueParser.retrieveItem(ctlv2);
            MtkCatLog.d(this, "add menu item: " + (item == null ? "" : item.toString()));
            menu.items.add(item);
        }
        if (menu.items.size() == 0) {
            MtkCatLog.d(this, "no menu item");
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
        ComprehensionTlv ctlv3 = searchForTag(ComprehensionTlvTag.NEXT_ACTION_INDICATOR, ctlvs);
        if (ctlv3 != null) {
            try {
                menu.nextActionIndicator = MtkValueParser.retrieveNextActionIndicator(ctlv3);
            } catch (ResultException e2) {
                MtkCatLog.e(this, "retrieveNextActionIndicator ResultException: " + e2.result());
            }
            try {
                if (menu.nextActionIndicator.length != menu.items.size()) {
                    MtkCatLog.d(this, "nextActionIndicator.length != number of menu items");
                    menu.nextActionIndicator = null;
                }
            } catch (NullPointerException e3) {
                MtkCatLog.e(this, "nextActionIndicator is null.");
            }
        }
        ComprehensionTlv ctlv4 = searchForTag(ComprehensionTlvTag.ITEM_ID, ctlvs);
        if (ctlv4 != null) {
            try {
                menu.defaultItem = ValueParser.retrieveItemId(ctlv4) - 1;
            } catch (ResultException e4) {
                MtkCatLog.e(this, "retrieveItemId ResultException: " + e4.result());
            }
            MtkCatLog.d(this, "default item: " + menu.defaultItem);
        }
        ComprehensionTlv ctlv5 = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
        if (ctlv5 != null) {
            this.mIconLoadState = 1;
            try {
                titleIconId = ValueParser.retrieveIconId(ctlv5);
            } catch (ResultException e5) {
                MtkCatLog.e(this, "retrieveIconId ResultException: " + e5.result());
            }
            try {
                menu.titleIconSelfExplanatory = titleIconId.selfExplanatory;
            } catch (NullPointerException e6) {
                MtkCatLog.e(this, "titleIconId is null.");
            }
        }
        ComprehensionTlv ctlv6 = searchForTag(ComprehensionTlvTag.ITEM_ICON_ID_LIST, ctlvs);
        if (ctlv6 != null) {
            this.mIconLoadState = 2;
            try {
                itemsIconId = MtkValueParser.retrieveItemsIconId(ctlv6);
            } catch (ResultException e7) {
                MtkCatLog.e(this, "retrieveItemsIconId ResultException: " + e7.result());
            }
            try {
                menu.itemsIconSelfExplanatory = itemsIconId.selfExplanatory;
            } catch (NullPointerException e8) {
                MtkCatLog.e(this, "itemsIconId is null.");
            }
        }
        boolean presentTypeSpecified = (cmdDet.commandQualifier & 1) != 0;
        if (presentTypeSpecified) {
            if ((2 & cmdDet.commandQualifier) == 0) {
                menu.presentationType = PresentationType.DATA_VALUES;
            } else {
                menu.presentationType = PresentationType.NAVIGATION_OPTIONS;
            }
        }
        menu.softKeyPreferred = (cmdDet.commandQualifier & 4) != 0;
        menu.helpAvailable = (cmdDet.commandQualifier & 128) != 0;
        this.mCmdParams = new SelectItemParams(cmdDet, menu, titleIconId != null);
        switch (this.mIconLoadState) {
            case 0:
                return false;
            case 1:
                if (titleIconId == null || titleIconId.recordNumber <= 0) {
                    return false;
                }
                this.mloadIcon = true;
                this.mIconLoader.loadIcon(titleIconId.recordNumber, obtainMessage(1));
                return true;
            case 2:
                if (itemsIconId == null) {
                    return false;
                }
                int[] recordNumbers = itemsIconId.recordNumbers;
                if (titleIconId != null) {
                    recordNumbers = new int[itemsIconId.recordNumbers.length + 1];
                    recordNumbers[0] = titleIconId.recordNumber;
                    System.arraycopy(itemsIconId.recordNumbers, 0, recordNumbers, 1, itemsIconId.recordNumbers.length);
                }
                this.mloadIcon = true;
                this.mIconLoader.loadIcons(recordNumbers, obtainMessage(1));
                return true;
            default:
                return true;
        }
    }

    protected boolean processEventNotify(CommandDetails cmdDet, List<ComprehensionTlv> ctlvs) throws ResultException {
        MtkCatLog.d(this, "process EventNotify");
        TextMessage textMsg = new TextMessage();
        IconId iconId = null;
        ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.ALPHA_ID, ctlvs);
        if (ctlv != null) {
            textMsg.text = MtkValueParser.retrieveAlphaId(ctlv, this.mNoAlphaUsrCnf);
        } else {
            textMsg.text = null;
        }
        ComprehensionTlv ctlv2 = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
        if (ctlv2 != null) {
            iconId = ValueParser.retrieveIconId(ctlv2);
            textMsg.iconSelfExplanatory = iconId.selfExplanatory;
        }
        textMsg.responseNeeded = false;
        this.mCmdParams = new DisplayTextParams(cmdDet, textMsg);
        if (iconId == null) {
            return false;
        }
        this.mloadIcon = true;
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconId.recordNumber, obtainMessage(1));
        return true;
    }

    protected boolean processSetUpEventList(CommandDetails cmdDet, List<ComprehensionTlv> ctlvs) {
        MtkCatLog.d(this, "process SetUpEventList");
        ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.EVENT_LIST, ctlvs);
        if (ctlv != null) {
            try {
                byte[] rawValue = ctlv.getRawValue();
                int valueIndex = ctlv.getValueIndex();
                int valueLen = ctlv.getLength();
                int[] eventList = new int[valueLen];
                int index = 0;
                while (index < valueLen) {
                    eventList[index] = rawValue[valueIndex];
                    MtkCatLog.v(this, "CPF-processSetUpEventList: eventList[" + index + "] = " + eventList[index]);
                    index++;
                    valueIndex++;
                }
                this.mCmdParams = new SetEventListParams(cmdDet, eventList);
                return false;
            } catch (IndexOutOfBoundsException e) {
                MtkCatLog.e(this, " IndexOutofBoundException in processSetUpEventList");
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.internal.telephony.cat.ResultException */
    protected boolean processLaunchBrowser(CommandDetails cmdDet, List<ComprehensionTlv> ctlvs) throws ResultException {
        LaunchBrowserMode mode;
        MtkCatLog.d(this, "process LaunchBrowser");
        TextMessage confirmMsg = new TextMessage();
        IconId iconId = null;
        String url = null;
        ComprehensionTlv ctlv = searchForTag(ComprehensionTlvTag.URL, ctlvs);
        if (ctlv != null) {
            try {
                byte[] rawValue = ctlv.getRawValue();
                int valueIndex = ctlv.getValueIndex();
                int valueLen = ctlv.getLength();
                if (valueLen > 0) {
                    url = GsmAlphabet.gsm8BitUnpackedToString(rawValue, valueIndex, valueLen);
                } else {
                    url = null;
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
        }
        ComprehensionTlv ctlv2 = searchForTag(ComprehensionTlvTag.ALPHA_ID, ctlvs);
        if (ctlv2 != null) {
            confirmMsg.text = MtkValueParser.retrieveAlphaId(ctlv2, this.mNoAlphaUsrCnf);
        }
        ComprehensionTlv ctlv3 = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
        if (ctlv3 != null) {
            iconId = ValueParser.retrieveIconId(ctlv3);
            confirmMsg.iconSelfExplanatory = iconId.selfExplanatory;
        }
        switch (cmdDet.commandQualifier) {
            case 2:
                mode = LaunchBrowserMode.USE_EXISTING_BROWSER;
                break;
            case 3:
                mode = LaunchBrowserMode.LAUNCH_NEW_BROWSER;
                break;
            default:
                mode = LaunchBrowserMode.LAUNCH_IF_NOT_ALREADY_LAUNCHED;
                break;
        }
        this.mCmdParams = new LaunchBrowserParams(cmdDet, confirmMsg, url, mode);
        if (iconId != null) {
            this.mIconLoadState = 1;
            this.mIconLoader.loadIcon(iconId.recordNumber, obtainMessage(1));
            return true;
        }
        return false;
    }

    protected boolean processSetupCall(CommandDetails cmdDet, List<ComprehensionTlv> ctlvs) throws ResultException {
        MtkCatLog.d(this, "process SetupCall");
        ctlvs.iterator();
        TextMessage confirmMsg = new TextMessage();
        TextMessage callMsg = new TextMessage();
        IconId confirmIconId = null;
        IconId callIconId = null;
        int addrIndex = getAddrIndex(ctlvs);
        if (-1 == addrIndex) {
            MtkCatLog.d(this, "fail to get ADDRESS data object");
            return false;
        }
        int alpha1Index = getConfirmationAlphaIdIndex(ctlvs, addrIndex);
        int alpha2Index = getCallingAlphaIdIndex(ctlvs, addrIndex);
        ComprehensionTlv ctlv = getConfirmationAlphaId(ctlvs, addrIndex);
        if (ctlv != null) {
            confirmMsg.text = MtkValueParser.retrieveAlphaId(ctlv, this.mNoAlphaUsrCnf);
        }
        ComprehensionTlv ctlv2 = getConfirmationIconId(ctlvs, alpha1Index, alpha2Index);
        if (ctlv2 != null) {
            confirmIconId = ValueParser.retrieveIconId(ctlv2);
            confirmMsg.iconSelfExplanatory = confirmIconId.selfExplanatory;
        }
        ComprehensionTlv ctlv3 = getCallingAlphaId(ctlvs, addrIndex);
        if (ctlv3 != null) {
            callMsg.text = MtkValueParser.retrieveAlphaId(ctlv3, this.mNoAlphaUsrCnf);
        }
        ComprehensionTlv ctlv4 = getCallingIconId(ctlvs, alpha2Index);
        if (ctlv4 != null) {
            callIconId = ValueParser.retrieveIconId(ctlv4);
            callMsg.iconSelfExplanatory = callIconId.selfExplanatory;
        }
        this.mCmdParams = new CallSetupParams(cmdDet, confirmMsg, callMsg);
        if (confirmIconId == null && callIconId == null) {
            return false;
        }
        this.mIconLoadState = 2;
        int[] recordNumbers = new int[2];
        recordNumbers[0] = confirmIconId != null ? confirmIconId.recordNumber : -1;
        recordNumbers[1] = callIconId != null ? callIconId.recordNumber : -1;
        this.mIconLoader.loadIcons(recordNumbers, obtainMessage(1));
        return true;
    }

    private int getAddrIndex(List<ComprehensionTlv> list) {
        int addrIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ADDRESS.value() || temp.getTag() == ComprehensionTlvTag.URL.value()) {
                return addrIndex;
            }
            addrIndex++;
        }
        return -1;
    }

    private int getConfirmationAlphaIdIndex(List<ComprehensionTlv> list, int addrIndex) {
        int alphaIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ALPHA_ID.value() && alphaIndex < addrIndex) {
                return alphaIndex;
            }
            alphaIndex++;
        }
        return -1;
    }

    private int getCallingAlphaIdIndex(List<ComprehensionTlv> list, int addrIndex) {
        int alphaIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ALPHA_ID.value() && alphaIndex > addrIndex) {
                return alphaIndex;
            }
            alphaIndex++;
        }
        return -1;
    }

    private ComprehensionTlv getConfirmationAlphaId(List<ComprehensionTlv> list, int addrIndex) {
        int alphaIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ALPHA_ID.value() && alphaIndex < addrIndex) {
                return temp;
            }
            alphaIndex++;
        }
        return null;
    }

    private ComprehensionTlv getCallingAlphaId(List<ComprehensionTlv> list, int addrIndex) {
        int alphaIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ALPHA_ID.value() && alphaIndex > addrIndex) {
                return temp;
            }
            alphaIndex++;
        }
        return null;
    }

    private ComprehensionTlv getConfirmationIconId(List<ComprehensionTlv> list, int alpha1Index, int alpha2Index) {
        if (-1 == alpha1Index) {
            return null;
        }
        int iconIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ICON_ID.value() && (-1 == alpha2Index || iconIndex < alpha2Index)) {
                return temp;
            }
            iconIndex++;
        }
        return null;
    }

    private ComprehensionTlv getCallingIconId(List<ComprehensionTlv> list, int alpha2Index) {
        if (-1 == alpha2Index) {
            return null;
        }
        int iconIndex = 0;
        for (ComprehensionTlv temp : list) {
            if (temp.getTag() == ComprehensionTlvTag.ICON_ID.value() && iconIndex > alpha2Index) {
                return temp;
            }
            iconIndex++;
        }
        return null;
    }
}
