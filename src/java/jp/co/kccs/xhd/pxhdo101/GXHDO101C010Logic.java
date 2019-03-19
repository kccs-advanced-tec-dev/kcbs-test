/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C010Model;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C010(被り量（μｍ）)ロジッククラス
 */
public class GXHDO101C010Logic {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    public static GXHDO101C010Model createGXHDO101C010Model(String[] kaburiryou) {
        GXHDO101C010Model model = new GXHDO101C010Model();
        // 被り量（μｍ）1
        model.setKaburiryouData1(getInitKaburiryoData(model, kaburiryou[0], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "1"));
        // 被り量（μｍ）2
        model.setKaburiryouData2(getInitKaburiryoData(model, kaburiryou[1], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "2"));
        // 被り量（μｍ）3
        model.setKaburiryouData3(getInitKaburiryoData(model, kaburiryou[2], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "3"));
        // 被り量（μｍ）4
        model.setKaburiryouData4(getInitKaburiryoData(model, kaburiryou[3], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "4"));
        // 被り量（μｍ）5
        model.setKaburiryouData5(getInitKaburiryoData(model, kaburiryou[4], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "5"));
        // 被り量（μｍ）6
        model.setKaburiryouData6(getInitKaburiryoData(model, kaburiryou[5], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "6"));
        // 被り量（μｍ）7
        model.setKaburiryouData7(getInitKaburiryoData(model, kaburiryou[6], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "7"));
        // 被り量（μｍ）8
        model.setKaburiryouData8(getInitKaburiryoData(model, kaburiryou[7], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "8"));
        // 被り量（μｍ）9
        model.setKaburiryouData9(getInitKaburiryoData(model, kaburiryou[8], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "9"));
        // 被り量（μｍ）10
        model.setKaburiryouData10(getInitKaburiryoData(model, kaburiryou[9], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "10"));
        // 被り量（μｍ）11
        model.setKaburiryouData11(getInitKaburiryoData(model, kaburiryou[10], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "11"));
        // 被り量（μｍ）12
        model.setKaburiryouData12(getInitKaburiryoData(model, kaburiryou[11], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "12"));
        // 被り量（μｍ）13
        model.setKaburiryouData13(getInitKaburiryoData(model, kaburiryou[12], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "13"));
        // 被り量（μｍ）14
        model.setKaburiryouData14(getInitKaburiryoData(model, kaburiryou[13], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "14"));
        // 被り量（μｍ）15
        model.setKaburiryouData15(getInitKaburiryoData(model, kaburiryou[14], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "15"));
        // 被り量（μｍ）16
        model.setKaburiryouData16(getInitKaburiryoData(model, kaburiryou[15], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "16"));
        // 被り量（μｍ）17
        model.setKaburiryouData17(getInitKaburiryoData(model, kaburiryou[16], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "17"));
        // 被り量（μｍ）18
        model.setKaburiryouData18(getInitKaburiryoData(model, kaburiryou[17], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "18"));
        // 被り量（μｍ）19
        model.setKaburiryouData19(getInitKaburiryoData(model, kaburiryou[18], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "19"));
        // 被り量（μｍ）20
        model.setKaburiryouData20(getInitKaburiryoData(model, kaburiryou[19], "TEXT", "3", "0", "999", "-999", DEFAULT_BACK_COLOR, "20"));
        return model;
    }

    /**
     * 被り量（μｍ）データ初期化データ取得
     *
     * @param GXHDO101C010Model 被り量（μｍ）画面モデル
     * @param printWidth 被り量（μｍ）
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextDecimalPlaces　スタート項目(テキストDecimalPlaces)
     * @param startTextMaxValue　スタート項目(テキストMaxValue)
     * @param startTextMinValue　スタート項目(テキストMinValue)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param startTabIndex　スタート項目(TabIndex)
     * @return 被り量（μｍ）データ
     */
    private static GXHDO101C010Model.KaburiryouData getInitKaburiryoData(GXHDO101C010Model GXHDO101C010Model, 
            String startVal, String startInputType, String startTextMaxLength, String startTextDecimalPlaces, String startTextMaxValue, 
            String startTextMinValue, String startTextBackColor, String startTabIndex) {
        GXHDO101C010Model.KaburiryouData printWidthListData = GXHDO101C010Model.new KaburiryouData();
        // スタート
        printWidthListData.setValue(startVal);
        if ("TEXT".equals(startInputType)) {
            printWidthListData.setTextRendered(true);
            printWidthListData.setLabelRendered(false);
        } else {
            printWidthListData.setTextRendered(false);
            printWidthListData.setLabelRendered(true);
        }
        printWidthListData.setTextDecimalPlaces(startTextDecimalPlaces);
        printWidthListData.setTextMaxValue(startTextMaxValue);
        printWidthListData.setTextMinValue(startTextMinValue);
        printWidthListData.setTextMaxLength(startTextMaxLength);
        printWidthListData.setTextBackColor(startTextBackColor);
        printWidthListData.setTabIndex(startTabIndex);

        return printWidthListData;
    }
    
    /**
     * 入力チェック
     *
     * @param gXHDO101C010Model 被り量(µm)入力サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C010Model gXHDO101C010Model) {

        List<String> errorList = new ArrayList<>();

        // 1～5にデータが入力されているかどうか確認
        if(StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData1().getValue())){
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)1"));
            return errorList;
        }
        
        if(StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData2().getValue())){
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)2"));
            return errorList;
        }
        
        if(StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData3().getValue())){
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)3"));
            return errorList;
        }
        
        if(StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData4().getValue())){
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)4"));
            return errorList;
        }
        
        if(StringUtil.isEmpty(gXHDO101C010Model.getKaburiryouData5().getValue())){
            errorList.add(MessageUtil.getMessage("XHD-000037", "被り量(µm)5"));
            return errorList;
        }

        return errorList;
    }
}
