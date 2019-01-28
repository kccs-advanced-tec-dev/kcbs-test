/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
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
 * GXHDO101C001Logic(膜厚(SPS))ロジッククラス
 */
public class GXHDO101C001Logic implements Serializable {

    /**
     * 膜厚(SPS)画面のモデルデータを作成する
     * @param makuatsuStart 膜厚スタートデータ
     * @param makuatsuEnd 膜厚エンドデータ
     * @return モデルデータ
     */
    public static GXHDO101C001Model createGXHDO101C001Model(String[] makuatsuStart, String[] makuatsuEnd) {
        GXHDO101C001Model gxhdo101C001Model = new GXHDO101C001Model();
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = new ArrayList<>();

        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < makuatsuStart.length; i++) {
            makuatsuDataList.add(getInitMakuatsuData(gxhdo101C001Model, String.valueOf(i + 1), makuatsuStart[i], "TEXT", "6", "", makuatsuEnd[i], "TEXT", "6", ""));
        }

        gxhdo101C001Model.setMakuatsuDataList(makuatsuDataList);
        return gxhdo101C001Model;
    }

    /**
     * 膜厚データ初期化データ取得
     *
     * @param gxhdo101C001Model 膜厚(SPS)画面モデル
     * @param makuatsu 膜厚
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param endVal エンド項目(値)
     * @param endInputType エンド項目(入力タイプ)
     * @param endTextMaxLength　エンド項目(テキストMaxLength)
     * @param endTextBackColor　スタート項目(BackGround)
     * @return 膜厚データ
     */
    private static GXHDO101C001Model.MakuatsuData getInitMakuatsuData(
            GXHDO101C001Model gxhdo101C001Model, String makuatsu,
            String startVal, String startInputType, String startTextMaxLength, String startTextBackColor,
            String endVal, String endInputType, String endTextMaxLength, String endTextBackColor) {
        GXHDO101C001Model.MakuatsuData makuatsuListData = gxhdo101C001Model.new MakuatsuData();
        // 膜厚
        makuatsuListData.setMakuatsu(makuatsu);

        // スタート
        makuatsuListData.setStartVal(startVal);
        if ("TEXT".equals(startInputType)) {
            makuatsuListData.setStartTextRendered(true);
            makuatsuListData.setStartLabelRendered(false);
        } else {
            makuatsuListData.setStartTextRendered(false);
            makuatsuListData.setStartLabelRendered(true);
        }
        makuatsuListData.setStartTextMaxLength(startTextMaxLength);
        makuatsuListData.setStartTextBackColor(startTextBackColor);

        // エンド
        makuatsuListData.setEndVal(endVal);
        if ("TEXT".equals(endInputType)) {
            makuatsuListData.setEndTextRendered(true);
            makuatsuListData.setEndLabelRendered(false);
        } else {
            makuatsuListData.setEndTextRendered(false);
            makuatsuListData.setEndLabelRendered(true);
        }
        makuatsuListData.setEndTextMaxLength(endTextMaxLength);
        makuatsuListData.setEndTextBackColor(endTextBackColor);
        return makuatsuListData;
    }

    /**
     * 入力チェック
     *
     * @param gXHDO101C001Model 膜厚(SPS)サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C001Model gXHDO101C001Model) {

        List<String> errorList = new ArrayList<>();
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = gXHDO101C001Model.getMakuatsuDataList();
        for (GXHDO101C001Model.MakuatsuData makuatsuData : makuatsuDataList) {
            if (StringUtil.isEmpty(makuatsuData.getStartVal())) {
                makuatsuData.setStartTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "スタート"));
                return errorList;
            }

            if (StringUtil.isEmpty(makuatsuData.getEndVal())) {
                makuatsuData.setEndTextBackColor(ErrUtil.ERR_BACK_COLOR);
                errorList.add(MessageUtil.getMessage("XHD-000037", "エンド"));
                return errorList;
            }
        }

        return errorList;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C001Model 膜厚(SPS)サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C001Model gXHDO101C001Model, List<FXHDD01> itemList) {

        List<String> startDataList = new ArrayList<>();
        List<String> endDataList = new ArrayList<>();
        for (GXHDO101C001Model.MakuatsuData makuatsuData : gXHDO101C001Model.getMakuatsuDataList()) {
            if (!StringUtil.isEmpty(makuatsuData.getStartVal())) {
                startDataList.add(makuatsuData.getStartVal());
            }

            if (!StringUtil.isEmpty(makuatsuData.getEndVal())) {
                endDataList.add(makuatsuData.getEndVal());
            }
        }

        FXHDD01 itemStartAve = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdStartAve());
        FXHDD01 itemStartMax = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdStartMax());
        FXHDD01 itemStartMin = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdStartMin());
        FXHDD01 itemStartCv = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdStartCv());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C001Model.getMakuatsuDataList().size() == startDataList.size()) {
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

        // 戻り先に指定した項目を取得
        FXHDD01 itemEndAve = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdEndAve());
        FXHDD01 itemEndMax = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdEndMax());
        FXHDD01 itemEndMin = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdEndMin());
        FXHDD01 itemEndCv = getItemRow(itemList, gXHDO101C001Model.getReturnItemIdEndCv());
        // 全て値が設定されていた場合のみ算出値をセットする
        if (gXHDO101C001Model.getMakuatsuDataList().size() == endDataList.size()) {
            BigDecimal[] calcDataEnd = NumberUtil.getCalculatData(endDataList);
            setItemValue(itemEndAve, calcDataEnd[3]);
            setItemValue(itemEndMax, calcDataEnd[1]);
            setItemValue(itemEndMin, calcDataEnd[2]);
            setItemValue(itemEndCv, calcDataEnd[4]);
        } else {
            setItemValue(itemEndAve, null);
            setItemValue(itemEndMax, null);
            setItemValue(itemEndMin, null);
            setItemValue(itemEndCv, null);
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
