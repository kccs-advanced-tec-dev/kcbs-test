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
 * GXHDO101C002Logic(PTN距離ｽﾀｰﾄ)ロジッククラス
 */
public class GXHDO101C002Logic {

    /**
     * PTN距離ｽﾀｰﾄ画面のモデルデータを作成する
     *
     * @param ptnKyoriXStart PTN距離Xスタートデータ
     * @param ptnKyoriYStart PTN距離Yスタートデータ
     * @return モデルデータ
     */
    public static GXHDO101C002Model createGXHDO101C002Model(String[] ptnKyoriXStart, String[] ptnKyoriYStart) {
        GXHDO101C002Model gxhdo101c002Model = new GXHDO101C002Model();
        List<GXHDO101C002Model.PtnKyoriStartData> ptnKyoriXDataList = new ArrayList<>();

        String startTabIndex;
        String endTabIndex;
        //PTN距離Xのデータをセットする
        for (int i = 0; i < ptnKyoriXStart.length; i++) {
            startTabIndex = String.valueOf(i + 1);
            endTabIndex = String.valueOf(i + 6);
            ptnKyoriXDataList.add(getInitPtnKyoriStartData(gxhdo101c002Model, String.valueOf(i + 1),
                    ptnKyoriXStart[i], "TEXT", "3", "", startTabIndex,
                    ptnKyoriYStart[i], "TEXT", "3", "", endTabIndex));
        }

        gxhdo101c002Model.setPtnKyoriStartDataList(ptnKyoriXDataList);

        return gxhdo101c002Model;
    }

    /**
     * PTN距離ｽﾀｰﾄデータ初期データ取得処理
     *
     * @param gxhdo101c002Model PTN距離ｽﾀｰﾄ画面モデル
     * @param ptnKyoriStart PTN距離ｽﾀｰﾄ
     * @param ptnKyoriXVal PTN距離X(値)
     * @param ptnKyoriXInputType　PTN距離X(入力タイプ)
     * @param ptnKyoriXTextMaxLength　PTN距離X(テキストMaxLength)
     * @param ptnKyoriXTextBackColor　PTN距離X(BackGround)
     * @param startTabIndex　スタート項目(TabIndex)
     * @param ptnKyoriYVal PTN距離Y項目(値)
     * @param ptnKyoriYInputType PTN距離Y(入力タイプ)
     * @param ptnKyoriYTextMaxLength　PTN距離Y(テキストMaxLength)
     * @param ptnKyoriYTextBackColor　PTN距離Y(BackGround)
     * @param endTabIndex　エンド項目(TabIndex)
     * @return PTN距離データ
     */
    private static GXHDO101C002Model.PtnKyoriStartData getInitPtnKyoriStartData(
            GXHDO101C002Model gxhdo101c002Model, String ptnKyoriStart,
            String ptnKyoriXVal, String ptnKyoriXInputType, String ptnKyoriXTextMaxLength, String ptnKyoriXTextBackColor, String startTabIndex,
            String ptnKyoriYVal, String ptnKyoriYInputType, String ptnKyoriYTextMaxLength, String ptnKyoriYTextBackColor, String endTabIndex) {
        GXHDO101C002Model.PtnKyoriStartData ptnKyoriStartData = gxhdo101c002Model.new PtnKyoriStartData();
        // PTN距離ｽﾀｰﾄ
        ptnKyoriStartData.setPtnKyoriStart(ptnKyoriStart);
        // PTN距離X
        ptnKyoriStartData.setPtnKyoriXVal(ptnKyoriXVal);
        if ("TEXT".equals(ptnKyoriXInputType)) {
            ptnKyoriStartData.setPtnKyoriXTextRendered(true);
            ptnKyoriStartData.setPtnKyoriXLabelRendered(false);

        } else {
            ptnKyoriStartData.setPtnKyoriXTextRendered(false);
            ptnKyoriStartData.setPtnKyoriXLabelRendered(true);
        }

        ptnKyoriStartData.setPtnKyoriXTextMaxLength(ptnKyoriXTextMaxLength);
        ptnKyoriStartData.setPtnKyoriXTextBackColor(ptnKyoriXTextBackColor);
        ptnKyoriStartData.setPtnKyoriXTabIndex(startTabIndex);

        // PTN距離Y
        ptnKyoriStartData.setPtnKyoriYVal(ptnKyoriYVal);
        if ("TEXT".equals(ptnKyoriYInputType)) {
            ptnKyoriStartData.setPtnKyoriYTextRendered(true);
            ptnKyoriStartData.setPtnKyoriYLabelRendered(false);
        } else {
            ptnKyoriStartData.setPtnKyoriYTextRendered(false);
            ptnKyoriStartData.setPtnKyoriYLabelRendered(true);
        }

        ptnKyoriStartData.setPtnKyoriYTextMaxLength(ptnKyoriYTextMaxLength);
        ptnKyoriStartData.setPtnKyoriYTextBackColor(ptnKyoriYTextBackColor);
        ptnKyoriStartData.setPtnKyoriYTabIndex(endTabIndex);

        return ptnKyoriStartData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C002Model PTN距離ｽﾀｰﾄサブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C002Model gXHDO101C002Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C002Model.PtnKyoriStartData> ptnKyoriStartDataList = gXHDO101C002Model.getPtnKyoriStartDataList();
        for (GXHDO101C002Model.PtnKyoriStartData ptnKyoriStart : ptnKyoriStartDataList) {
            if (StringUtil.isEmpty(ptnKyoriStart.getPtnKyoriXVal())) {
                ptnKyoriStart.setPtnKyoriXTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "PTN距離Xｽﾀｰﾄ" + ptnKyoriStart.getPtnKyoriStart()));
                return errorList;
            }

            if (StringUtil.isEmpty(ptnKyoriStart.getPtnKyoriYVal())) {
                ptnKyoriStart.setPtnKyoriYTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "PTN距離Yｽﾀｰﾄ" + ptnKyoriStart.getPtnKyoriStart()));
                return errorList;
            }
        }

        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C002Model PTN距離ｽﾀｰﾄサブ画面用モデル
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C002Model gXHDO101C002Model, List<FXHDD01> itemList) {

        List<String> startXDataList = new ArrayList<>();
        List<String> startYDataList = new ArrayList<>();
        for (GXHDO101C002Model.PtnKyoriStartData ptnKyoriStartData : gXHDO101C002Model.getPtnKyoriStartDataList()) {
            if (!StringUtil.isEmpty(ptnKyoriStartData.getPtnKyoriXVal())) {
                startXDataList.add(ptnKyoriStartData.getPtnKyoriXVal());
            }

            if (!StringUtil.isEmpty(ptnKyoriStartData.getPtnKyoriYVal())) {
                startYDataList.add(ptnKyoriStartData.getPtnKyoriYVal());
            }
        }

        FXHDD01 itemStartMin = getItemRow(itemList, gXHDO101C002Model.getReturnItemIdStartXMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C002Model.getPtnKyoriStartDataList().size() == startXDataList.size()) {
            setItemValue(itemStartMin, NumberUtil.getMin(startXDataList));
        } else {
            setItemValue(itemStartMin, null);
        }

        // 戻り先に指定した項目を取得
        FXHDD01 itemEndMin = getItemRow(itemList, gXHDO101C002Model.getReturnItemIdStartYMin());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C002Model.getPtnKyoriStartDataList().size() == startYDataList.size()) {
            setItemValue(itemEndMin, NumberUtil.getMin(startYDataList));
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
