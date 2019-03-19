/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C008Model;
import jp.co.kccs.xhd.util.ErrUtil;
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
 * GXHDO101C008(ﾊﾟﾀｰﾝ間距離)ロジッククラス
 */
public class GXHDO101C008Logic {

    public static GXHDO101C008Model createGXHDO101C008Model(String[] startValues) {
        GXHDO101C008Model model = new GXHDO101C008Model();
        List<GXHDO101C008Model.PtnKanKyoriData> denkyokuMakuatsuDataList = new ArrayList<>();
        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < startValues.length; i++) {
            denkyokuMakuatsuDataList.add(getInitPtnKanKyoriData(model, String.valueOf(i + 1), startValues[i], "TEXT", "3", "0", "999", "-999", "", String.valueOf(i + 1)));
        }
        model.setPtnKanKyoriDataList(denkyokuMakuatsuDataList);
        return model;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離データ初期化データ取得
     *
     * @param GXHDO101C008Model ﾊﾟﾀｰﾝ間距離画面モデル
     * @param ptnKanKyori ﾊﾟﾀｰﾝ間距離
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextDecimalPlaces　スタート項目(テキストDecimalPlaces)
     * @param startTextMaxValue　スタート項目(テキストMaxValue)
     * @param startTextMinValue　スタート項目(テキストMinValue)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param startTabIndex　スタート項目(TabIndex)
     * @return ﾊﾟﾀｰﾝ間距離データ
     */
    private static GXHDO101C008Model.PtnKanKyoriData getInitPtnKanKyoriData(GXHDO101C008Model GXHDO101C008Model, String ptnKanKyori, 
            String startVal, String startInputType, String startTextMaxLength, String startTextDecimalPlaces, String startTextMaxValue, 
            String startTextMinValue, String startTextBackColor, String startTabIndex) {
        
        GXHDO101C008Model.PtnKanKyoriData ptnKanKyoriListData = GXHDO101C008Model.new PtnKanKyoriData();
        // ﾊﾟﾀｰﾝ間距離データ
        ptnKanKyoriListData.setPtnKanKyori(ptnKanKyori);
        // スタート
        ptnKanKyoriListData.setValue(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKanKyoriListData.setTextRendered(true);
            ptnKanKyoriListData.setLabelRendered(false);
        } else {
            ptnKanKyoriListData.setTextRendered(false);
            ptnKanKyoriListData.setLabelRendered(true);
        }
        ptnKanKyoriListData.setTextMaxLength(startTextMaxLength);
        ptnKanKyoriListData.setTextDecimalPlaces(startTextDecimalPlaces);
        ptnKanKyoriListData.setTextMaxValue(startTextMaxValue);
        ptnKanKyoriListData.setTextMinValue(startTextMinValue);
        ptnKanKyoriListData.setTextBackColor(startTextBackColor);
        ptnKanKyoriListData.setTabIndex(startTabIndex);

        return ptnKanKyoriListData;
    }
    
    /**
     * 入力チェック
     *
     * @param gXHDO101C008Model ﾊﾟﾀｰﾝ間距離入力サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C008Model gXHDO101C008Model) {

        List<String> errorList = new ArrayList<>();
        for (GXHDO101C008Model.PtnKanKyoriData ptnKanKyoriData : gXHDO101C008Model.getPtnKanKyoriDataList()) {
            if (StringUtil.isEmpty(ptnKanKyoriData.getValue())) {
                    ptnKanKyoriData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "ﾊﾟﾀｰﾝ間距離" + ptnKanKyoriData.getPtnKanKyori()));
                    return errorList;
            }
        }

        return errorList;
    }
}
