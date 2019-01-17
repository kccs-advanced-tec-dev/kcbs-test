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
import jp.co.kccs.xhd.model.GXHDO101C002Model;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C003(PTN距離Y)ロジッククラス
 */
public class GXHDO101C003Logic {

    public static GXHDO101C003Model createGXHDO101C003Model(String[] ptnKyoriYStart, String[] ptnKyoriYEnd) {
        GXHDO101C003Model gxhdo101c003Model = new GXHDO101C003Model();
        List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList = new ArrayList<>();

        //PTN距離Yのデータをセットする
        for (int i = 0; i < ptnKyoriYStart.length; i++) {
            ptnKyoriYDataList.add(getInitPtnKyoriYData(gxhdo101c003Model, String.valueOf(i + 1), ptnKyoriYStart[i], "TEXT", "3", "", ptnKyoriYEnd[i], "TEXT", "3", ""));
        }
        gxhdo101c003Model.setPtnKyoriYDataList(ptnKyoriYDataList);

        return gxhdo101c003Model;
    }

    /**
     * PTN距離Yデータ初期データ取得処理
     *
     * @param gxhdo101c003Model PTN距離X画面モデル
     * @param ptnKyoriY PTN距離Y
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param endVal エンド項目(値)
     * @param endInputType エンド項目(入力タイプ)
     * @param endTextMaxLength　エンド項目(テキストMaxLength)
     * @param endTextBackColor　エンド項目(BackGround)
     * @return PTN距離データ
     */
    private static GXHDO101C003Model.PtnKyoriYData getInitPtnKyoriYData(
            GXHDO101C003Model gxhdo101c003Model, String ptnKyoriY,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C003Model.PtnKyoriYData ptnKyoriYData = gxhdo101c003Model.new PtnKyoriYData();
        // PTN距離Y
        ptnKyoriYData.setPtnKyoriY(ptnKyoriY);
        // スタート
        ptnKyoriYData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKyoriYData.setStartTextRendered(true);
            ptnKyoriYData.setStartLabelRendered(false);
        } else {
            ptnKyoriYData.setStartTextRendered(false);
            ptnKyoriYData.setStartLabelRendered(true);
        }
        ptnKyoriYData.setStartTextMaxLength(startTextMaxLength);
        ptnKyoriYData.setStartTextBackColor(startTextBackColor);

        // エンド
        ptnKyoriYData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            ptnKyoriYData.setEndTextRendered(true);
            ptnKyoriYData.setEndLabelRendered(false);

        } else {
            ptnKyoriYData.setEndTextRendered(false);
            ptnKyoriYData.setEndLabelRendered(true);
        }
        ptnKyoriYData.setEndTextMaxLength(endTextMaxLength);
        ptnKyoriYData.setEndTextBackColor(endTextBackColor);
        return ptnKyoriYData;
    }
    
    /**
     * 入力ﾁｪｯｸ
     * @param gXHDO101C003Model PTN距離Yサブ画面用ﾓﾃﾞﾙ
     * @return ｴﾗｰﾘｽﾄ
     */
    public static List<String> checkInput(GXHDO101C003Model gXHDO101C003Model) {
        
        List<String> errorList = new ArrayList<>();
        List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList = gXHDO101C003Model.getPtnKyoriYDataList();
        for (GXHDO101C003Model.PtnKyoriYData ptnKyoriY : ptnKyoriYDataList) {
            if (StringUtil.isEmpty(ptnKyoriY.getStartVal())) {
                ptnKyoriY.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add("スタートが入力されていません。");
                return errorList;
            }
            
            if (StringUtil.isEmpty(ptnKyoriY.getEndVal())) {
                ptnKyoriY.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add("エンドが入力されていません。");
                return errorList;
            }
        }
        
        return errorList;
    }
    
    
    
    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C003Model PTN距離Xサブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C003Model gXHDO101C003Model, List<FXHDD01> itemList) {

        List<String> startDataList = new ArrayList<>();
        List<String> endDataList = new ArrayList<>();
        for (GXHDO101C003Model.PtnKyoriYData ptnKyoriXdata : gXHDO101C003Model.getPtnKyoriYDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriXdata.getStartVal())) {
                startDataList.add(ptnKyoriXdata.getStartVal());
            }

            if (!StringUtil.isEmpty(ptnKyoriXdata.getEndVal())) {
                endDataList.add(ptnKyoriXdata.getEndVal());
            }
        }

        FXHDD01 itemStartMin = getItemRow(itemList, gXHDO101C003Model.getReturnItemIdStartMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C003Model.getPtnKyoriYDataList().size() == startDataList.size()) {
            setItemValue(itemStartMin, NumberUtil.getMin(startDataList));
        } else {
            setItemValue(itemStartMin, null);
        }

        // 戻り先に指定した項目を取得
        FXHDD01 itemEndMin = getItemRow(itemList, gXHDO101C003Model.getReturnItemIdEndMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C003Model.getPtnKyoriYDataList().size() == endDataList.size()) {
            setItemValue(itemEndMin, NumberUtil.getMin(endDataList));
        } else {
            setItemValue(itemEndMin, null);
        }
    }

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
