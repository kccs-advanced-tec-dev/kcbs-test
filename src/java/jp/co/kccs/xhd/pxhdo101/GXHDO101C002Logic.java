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
 * GXHDO101C002Logic(PTN距離X)ロジッククラス
 */
public class GXHDO101C002Logic {

    /**
     * PTN距離X画面のモデルデータを作成する
     *
     * @param ptnKyoriXStart PTN距離Xスタートデータ
     * @param ptnKyoriXEnd PTN距離Xエンドデータ
     * @return モデルデータ
     */
    public static GXHDO101C002Model createGXHDO101C002Model(String[] ptnKyoriXStart, String[] ptnKyoriXEnd) {
        GXHDO101C002Model gxhdo101c002Model = new GXHDO101C002Model();
        List<GXHDO101C002Model.PtnKyoriXData> ptnKyoriXDataList = new ArrayList<>();
        String startTabIndex;
        String endTabIndex;
        //PTN距離Xのデータをセットする
        for (int i = 0; i < ptnKyoriXStart.length; i++) {
            startTabIndex = String.valueOf(i + 1);
            endTabIndex = String.valueOf(i + 6);
            ptnKyoriXDataList.add(getInitPtnKyoriXData(gxhdo101c002Model, String.valueOf(i + 1), 
                    ptnKyoriXStart[i], "TEXT", "3", "", startTabIndex,
                    ptnKyoriXEnd[i], "TEXT", "3", "", endTabIndex));
        }

        gxhdo101c002Model.setPtnKyoriXDataList(ptnKyoriXDataList);
        return gxhdo101c002Model;
    }

    /**
     * PTN距離Xデータ初期データ取得処理
     *
     * @param gxhdo101c002Model PTN距離X画面モデル
     * @param ptnKyoriX PTN距離X
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param startTabIndex　スタート項目(TabIndex)
     * @param endVal エンド項目(値)
     * @param endInputType エンド項目(入力タイプ)
     * @param endTextMaxLength　エンド項目(テキストMaxLength)
     * @param endTextBackColor　エンド項目(BackGround)
     * @param endTabIndex　エンド項目(TabIndex)
     * @return PTN距離データ
     */
    private static GXHDO101C002Model.PtnKyoriXData getInitPtnKyoriXData(
            GXHDO101C002Model gxhdo101c002Model, String ptnKyoriX,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor, String startTabIndex,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor, String endTabIndex) {
        GXHDO101C002Model.PtnKyoriXData ptnKyoriXData = gxhdo101c002Model.new PtnKyoriXData();
        //PTN距離X
        ptnKyoriXData.setPtnKyoriX(ptnKyoriX);
        // スタート
        ptnKyoriXData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            ptnKyoriXData.setStartTextRendered(true);
            ptnKyoriXData.setStartLabelRendered(false);

        } else {
            ptnKyoriXData.setStartTextRendered(false);
            ptnKyoriXData.setStartLabelRendered(true);
        }
        ptnKyoriXData.setStartTextMaxLength(startTextMaxLength);
        ptnKyoriXData.setStartTextBackColor(startTextBackColor);
        ptnKyoriXData.setStartTabIndex(startTabIndex);

        // エンド
        ptnKyoriXData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            ptnKyoriXData.setEndTextRendered(true);
            ptnKyoriXData.setEndLabelRendered(false);
        } else {
            ptnKyoriXData.setEndTextRendered(false);
            ptnKyoriXData.setEndLabelRendered(true);
        }
        ptnKyoriXData.setEndTextMaxLength(endTextMaxLength);
        ptnKyoriXData.setEndTextBackColor(endTextBackColor);
        ptnKyoriXData.setEndTabIndex(endTabIndex);

        return ptnKyoriXData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C002Model PTN距離Xサブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C002Model gXHDO101C002Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C002Model.PtnKyoriXData> ptnKyoriXDataList = gXHDO101C002Model.getPtnKyoriXDataList();
        for (GXHDO101C002Model.PtnKyoriXData ptnKyoriX : ptnKyoriXDataList) {
            if (StringUtil.isEmpty(ptnKyoriX.getStartVal())) {
                ptnKyoriX.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "スタート"));
                return errorList;
            }

            if (StringUtil.isEmpty(ptnKyoriX.getEndVal())) {
                ptnKyoriX.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "エンド"));
                return errorList;
            }
        }

        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C002Model PTN距離Xサブ画面用モデル
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C002Model gXHDO101C002Model, List<FXHDD01> itemList) {

        List<String> startDataList = new ArrayList<>();
        List<String> endDataList = new ArrayList<>();
        for (GXHDO101C002Model.PtnKyoriXData ptnKyoriXdata : gXHDO101C002Model.getPtnKyoriXDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriXdata.getStartVal())) {
                startDataList.add(ptnKyoriXdata.getStartVal());
            }

            if (!StringUtil.isEmpty(ptnKyoriXdata.getEndVal())) {
                endDataList.add(ptnKyoriXdata.getEndVal());
            }
        }

        FXHDD01 itemStartMin = getItemRow(itemList, gXHDO101C002Model.getReturnItemIdStartMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C002Model.getPtnKyoriXDataList().size() == startDataList.size()) {
            setItemValue(itemStartMin, NumberUtil.getMin(startDataList));
        } else {
            setItemValue(itemStartMin, null);
        }

        // 戻り先に指定した項目を取得
        FXHDD01 itemEndMin = getItemRow(itemList, gXHDO101C002Model.getReturnItemIdEndMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C002Model.getPtnKyoriXDataList().size() == endDataList.size()) {
            setItemValue(itemEndMin, NumberUtil.getMin(endDataList));
        } else {
            setItemValue(itemEndMin, null);
        }
    }

    /**
     * 対象項目に値をセットする。
     *
     * @param itemData 項目データ
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
     * @param listData 項目データリスト
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
