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
import jp.co.kccs.xhd.model.GXHDO101C004Model;
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
 * GXHDO101C004Logic(膜厚(RSUS))ロジッククラス
 */
public class GXHDO101C004Logic {

    /**
     * 膜厚(RSUS)画面のモデルデータを作成する
     *
     * @param makuatsuStart 膜厚スタートデータ
     * @return モデルデータ
     */
    public static GXHDO101C004Model createGXHDO101C004Model(String[] makuatsuStart) {
        List<GXHDO101C004Model.MakuatsuData> makuatsuDataList = new ArrayList<>();
        GXHDO101C004Model model = new GXHDO101C004Model();

        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < makuatsuStart.length; i++) {
            makuatsuDataList.add(getInitMakuatsuData(model, String.valueOf(i + 1), makuatsuStart[i], "TEXT", "6", ""));
        }

        model.setMakuatsuDataList(makuatsuDataList);
        return model;
    }

    /**
     * 膜厚(RSUS)データ初期化データ取得
     *
     * @param gxhdo101C004Model 膜厚(RSUS)画面モデル
     * @param makuatsu 膜厚
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @return 膜厚データ
     */
    private static GXHDO101C004Model.MakuatsuData getInitMakuatsuData(GXHDO101C004Model model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor) {
        GXHDO101C004Model.MakuatsuData makuatsuData = model.new MakuatsuData();
        // 膜厚
        makuatsuData.setMakuatsu(makuatsu);

        // スタート
        makuatsuData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            makuatsuData.setStartTextRendered(true);
            makuatsuData.setStartLabelRendered(false);
        } else {
            makuatsuData.setStartTextRendered(false);
            makuatsuData.setStartLabelRendered(true);
        }
        makuatsuData.setStartTextMaxLength(startTextMaxLength);
        makuatsuData.setStartTextBackColor(startTextBackColor);
        return makuatsuData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C004Model 膜厚(RSUS)サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C004Model gXHDO101C004Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C004Model.MakuatsuData> makuatsuDataList = gXHDO101C004Model.getMakuatsuDataList();
        for (GXHDO101C004Model.MakuatsuData makuatsuData : makuatsuDataList) {
            if (StringUtil.isEmpty(makuatsuData.getStartVal())) {
                makuatsuData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "スタート"));
                return errorList;
            }
        }
        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C004Model 膜厚(SPS)サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C004Model gXHDO101C004Model, List<FXHDD01> itemList) {

        List<String> startDataList = new ArrayList<>();
        for (GXHDO101C004Model.MakuatsuData makuatsuData : gXHDO101C004Model.getMakuatsuDataList()) {
            if (!StringUtil.isEmpty(makuatsuData.getStartVal())) {
                startDataList.add(makuatsuData.getStartVal());
            }
        }

        FXHDD01 itemStartAve = getItemRow(itemList, gXHDO101C004Model.getReturnItemIdStartAve());
        FXHDD01 itemStartMax = getItemRow(itemList, gXHDO101C004Model.getReturnItemIdStartMax());
        FXHDD01 itemStartMin = getItemRow(itemList, gXHDO101C004Model.getReturnItemIdStartMin());
        FXHDD01 itemStartCv = getItemRow(itemList, gXHDO101C004Model.getReturnItemIdStartCv());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C004Model.getMakuatsuDataList().size() == startDataList.size()) {
            BigDecimal[] calcDataStart = NumberUtil.getCalculatData(startDataList);
            setItemValue(itemStartAve, calcDataStart[3]);
            setItemValue(itemStartMax, calcDataStart[1]);
            setItemValue(itemStartMin, calcDataStart[2]);
            setItemValue(itemStartCv, calcDataStart[4]);
        } else {
            setItemValue(itemStartAve, null);
            setItemValue(itemStartMax, null);
            setItemValue(itemStartMin, null);
            setItemValue(itemStartCv, null);
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
