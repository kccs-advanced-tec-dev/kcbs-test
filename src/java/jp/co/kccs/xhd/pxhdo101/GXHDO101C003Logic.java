/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C003Logic(PTN距離ｴﾝﾄﾞ)ロジッククラス
 */
public class GXHDO101C003Logic {

    /**
     * PTN距離ｴﾝﾄﾞ画面のモデルデータを作成する
     *
     * @param ptnKyoriXEnd PTN距離Xｴﾝﾄﾞデータ
     * @param ptnKyoriYEnd PTN距離Yｴﾝﾄﾞデータ
     * @return モデルデータ
     */
    public static GXHDO101C003Model createGXHDO101C003Model(String[] ptnKyoriXEnd, String[] ptnKyoriYEnd) {
        GXHDO101C003Model gxhdo101c003Model = new GXHDO101C003Model();
        List<GXHDO101C003Model.PtnKyoriEndData> ptnKyoriYDataList = new ArrayList<>();

         String startTabIndex;
        String endTabIndex;
       //PTN距離ｴﾝﾄﾞのデータをセットする
        for (int i = 0; i < ptnKyoriXEnd.length; i++) {
            startTabIndex = String.valueOf(i + 1);
            endTabIndex = String.valueOf(i + 6);
            ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, String.valueOf(i + 1), 
                    ptnKyoriXEnd[i], "TEXT", "3", "", startTabIndex, 
                    ptnKyoriYEnd[i], "TEXT", "3", "", endTabIndex));
        }
        gxhdo101c003Model.setPtnKyoriEndDataList(ptnKyoriYDataList);

        return gxhdo101c003Model;
    }

    /**
     * PTN距離ｴﾝﾄﾞデータ初期データ取得処理
     *
     * @param gxhdo101c003Model PTN距離ｴﾝﾄﾞ画面モデル
     * @param ptnKyoriEnd PTN距離ｴﾝﾄﾞ
     * @param ptnKyoriXVal PTN距離X(値)
     * @param ptnKyoriXInputType　PTN距離X(入力タイプ)
     * @param ptnKyoriXTextMaxLength　PTN距離X(テキストMaxLength)
     * @param ptnKyoriXTextBackColor　PTN距離X(BackGround)
     * @param startTabIndex　スタート項目(タブインデックス)
     * @param ptnKyoriYVal PTN距離Y項目(値)
     * @param ptnKyoriYInputType PTN距離Y(入力タイプ)
     * @param ptnKyoriYTextMaxLength　PTN距離Y(テキストMaxLength)
     * @param ptnKyoriYTextBackColor　PTN距離Y(BackGround)
     * @param endTabIndex　エンド項目(タブインデックス)
     * @return PTN距離データ
     */
    private static GXHDO101C003Model.PtnKyoriEndData getInitPtnKyoriYData(
            GXHDO101C003Model gxhdo101c003Model, String ptnKyoriEnd,
            String ptnKyoriXVal, String ptnKyoriXInputType, String ptnKyoriXTextMaxLength, String ptnKyoriXTextBackColor,String startTabIndex,
            String ptnKyoriYVal, String ptnKyoriYInputType, String ptnKyoriYTextMaxLength, String ptnKyoriYTextBackColor,String endTabIndex) {
        GXHDO101C003Model.PtnKyoriEndData ptnKyoriEndData = gxhdo101c003Model.new PtnKyoriEndData();
        // PTN距離Y
        ptnKyoriEndData.setPtnKyoriEnd(ptnKyoriEnd);
        // スタート
        ptnKyoriEndData.setPtnKyoriXVal(ptnKyoriXVal);
        if ("TEXT".equals(ptnKyoriXInputType)) {
            ptnKyoriEndData.setPtnKyoriXTextRendered(true);
            ptnKyoriEndData.setPtnKyoriXLabelRendered(false);
        } else {
            ptnKyoriEndData.setPtnKyoriXTextRendered(false);
            ptnKyoriEndData.setPtnKyoriXLabelRendered(true);
        }

        ptnKyoriEndData.setPtnKyoriXTextMaxLength(ptnKyoriXTextMaxLength);
        ptnKyoriEndData.setPtnKyoriXTextBackColor(ptnKyoriXTextBackColor);
        ptnKyoriEndData.setPtnKyoriXTabIndex(startTabIndex);

        // エンド
        ptnKyoriEndData.setPtnKyoriYVal(ptnKyoriYVal);
        if ("TEXT".equals(ptnKyoriYInputType)) {
            ptnKyoriEndData.setPtnKyoriYTextRendered(true);
            ptnKyoriEndData.setPtnKyoriYLabelRendered(false);
        } else {
            ptnKyoriEndData.setPtnKyoriYTextRendered(false);
            ptnKyoriEndData.setPtnKyoriYLabelRendered(true);
        }

        ptnKyoriEndData.setPtnKyoriYTextMaxLength(ptnKyoriYTextMaxLength);
        ptnKyoriEndData.setPtnKyoriYTextBackColor(ptnKyoriYTextBackColor);
        ptnKyoriEndData.setPtnKyoriYTabIndex(endTabIndex);
        return ptnKyoriEndData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C003Model PTN距離ｴﾝﾄﾞサブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C003Model gXHDO101C003Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C003Model.PtnKyoriEndData> ptnKyoriYDataList = gXHDO101C003Model.getPtnKyoriEndDataList();
        for (GXHDO101C003Model.PtnKyoriEndData ptnKyoriY : ptnKyoriYDataList) {
            if (StringUtil.isEmpty(ptnKyoriY.getPtnKyoriXVal())) {
                ptnKyoriY.setPtnKyoriXTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "PTN距離X"));
                return errorList;
            }

            if (StringUtil.isEmpty(ptnKyoriY.getPtnKyoriYVal())) {
                ptnKyoriY.setPtnKyoriYTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "PTN距離Y"));
                return errorList;
            }
        }

        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C003Model PTN距離ｴﾝﾄﾞサブ画面用モデル
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C003Model gXHDO101C003Model, List<FXHDD01> itemList) {

        List<String> startDataList = new ArrayList<>();
        List<String> endDataList = new ArrayList<>();
        for (GXHDO101C003Model.PtnKyoriEndData ptnKyoriXdata : gXHDO101C003Model.getPtnKyoriEndDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriXdata.getPtnKyoriXVal())) {
                startDataList.add(ptnKyoriXdata.getPtnKyoriXVal());
            }

            if (!StringUtil.isEmpty(ptnKyoriXdata.getPtnKyoriYVal())) {
                endDataList.add(ptnKyoriXdata.getPtnKyoriYVal());
            }
        }

        FXHDD01 itemStartMin = getItemRow(itemList, gXHDO101C003Model.getReturnItemIdEndXMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C003Model.getPtnKyoriEndDataList().size() == startDataList.size()) {
            setItemValue(itemStartMin, NumberUtil.getMin(startDataList));
        } else {
            setItemValue(itemStartMin, null);
        }

        // 戻り先に指定した項目を取得
        FXHDD01 itemEndMin = getItemRow(itemList, gXHDO101C003Model.getReturnItemIdEndYMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C003Model.getPtnKyoriEndDataList().size() == endDataList.size()) {
            setItemValue(itemEndMin, NumberUtil.getMin(endDataList));
        } else {
            setItemValue(itemEndMin, null);
        }
    }

    /**
     * 対象項目に値をセットする
     *
     * @param itemData 項目
     * @param value 値
     */
    private static void setItemValue(FXHDD01 itemData, BigDecimal value) {
        if (itemData == null) {
            return;
        }
        if (value != null) {
            // 小数指定されている場合は小数部以下は切り捨て
            if (!StringUtil.isEmpty(itemData.getInputLengthDec())) {
                try {
                    value = value.setScale(Integer.parseInt(itemData.getInputLengthDec()), RoundingMode.DOWN);
                } catch (NumberFormatException e) {
                    // 処理なし
                }
            }

            // 値をセット
            itemData.setValue(value.toPlainString());
        } else {
            // 値をセット
            itemData.setValue("");
        }
    }

    /**
     * 項目データ取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 項目データ
     */
    private static FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }
}
