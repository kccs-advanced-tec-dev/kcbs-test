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
import jp.co.kccs.xhd.model.GXHDO101C005Model;
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
 * 変更者	SYSNAVI K.Hisanaaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C005(印刷幅)ロジッククラス
 */
public class GXHDO101C005Logic {

    /**
     * 印刷幅画面のモデルデータを作成する
     *
     * @param startValues 印刷幅スタートデータ
     * @return モデルデータ
     */
    public static GXHDO101C005Model createGXHDO101C005Model(String[] startValues) {

        GXHDO101C005Model model = new GXHDO101C005Model();
        List<GXHDO101C005Model.PrintWidthData> printWidthDataList = new ArrayList<>();
        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < startValues.length; i++) {
            printWidthDataList.add(getInitPrintWidhData(model, String.valueOf(i + 1), startValues[i], "TEXT", "4", "", String.valueOf(i + 1)));
        }

        model.setPrintWidthDataList(printWidthDataList);
        return model;

    }

    /**
     * 印刷幅データ初期化データ取得
     *
     * @param gxhdo101C005Model 印刷幅画面モデル
     * @param printWidth 印刷幅
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param startTabIndex　スタート項目(BackGround)
     * @return 印刷幅データ
     */
    private static GXHDO101C005Model.PrintWidthData getInitPrintWidhData(
            GXHDO101C005Model gxhdo101C005Model, String printWidth, String startVal,
            String startInputType, String startTextMaxLength, String startTextBackColor, 
            String startTabIndex) {
        GXHDO101C005Model.PrintWidthData printWidthListData = gxhdo101C005Model.new PrintWidthData();
        // 印刷幅
        printWidthListData.setPrintWidth(printWidth);
        // スタート
        printWidthListData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            printWidthListData.setStartTextRendered(true);
            printWidthListData.setStartLabelRendered(false);
        } else {
            printWidthListData.setStartTextRendered(false);
            printWidthListData.setStartLabelRendered(true);
        }
        printWidthListData.setStartTextMaxLength(startTextMaxLength);
        printWidthListData.setStartTextBackColor(startTextBackColor);
        printWidthListData.setStartTabIndex(startTabIndex);

        return printWidthListData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C005Model 印刷幅サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C005Model gXHDO101C005Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C005Model.PrintWidthData> printWidthDataList = gXHDO101C005Model.getPrintWidthDataList();
        for (GXHDO101C005Model.PrintWidthData printWidthData : printWidthDataList) {
            if (StringUtil.isEmpty(printWidthData.getStartVal())) {
                printWidthData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "スタート"));
                return errorList;
            }
        }
        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C005Model 印刷幅サブ画面用モデル
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C005Model gXHDO101C005Model, List<FXHDD01> itemList) {

        List<String> startDataList = new ArrayList<>();
        for (GXHDO101C005Model.PrintWidthData printWidthData : gXHDO101C005Model.getPrintWidthDataList()) {
            if (!StringUtil.isEmpty(printWidthData.getStartVal())) {
                startDataList.add(printWidthData.getStartVal());
            }
        }

        FXHDD01 itemInsatsuHaba = getItemRow(itemList, gXHDO101C005Model.getReturnItemIdInsatsuHaba());

        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C005Model.getPrintWidthDataList().size() == startDataList.size()) {
            setItemValue(itemInsatsuHaba, NumberUtil.getAve(startDataList));
        } else {
            setItemValue(itemInsatsuHaba, null);
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
