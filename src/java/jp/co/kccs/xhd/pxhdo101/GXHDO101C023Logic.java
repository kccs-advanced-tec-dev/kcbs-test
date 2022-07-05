/*
 * Copyright 2022 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO101C023Model;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質情報管理システム<br>
 * <br>
 * 変更日	2022/06/20<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C023(ｽﾞﾚ値サブ画面)ロジッククラス
 */
public class GXHDO101C023Logic {

    /**
     * ｽﾞﾚ値サブ画面のモデルデータを作成する
     *
     * @param zurechiValues ｽﾞﾚ値ｽﾞﾚ値データ
     * @return モデルデータ
     */
    public static GXHDO101C023Model createGXHDO101C023Model(String[] zurechiValues) {

        GXHDO101C023Model model = new GXHDO101C023Model();
        List<GXHDO101C023Model.ZurechiData> zurechiInputDataList = new ArrayList<>();
        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < zurechiValues.length; i++) {
            zurechiInputDataList.add(getInitZurechiData(model, String.valueOf(i + 1), zurechiValues[i], "9", "", String.valueOf(i + 1)));
        }

        model.setZurechiInputDataList(zurechiInputDataList);
        return model;

    }

    /**
     * ｽﾞﾚ値サブデータ初期化データ取得
     *
     * @param gxhdo101C023Model ｽﾞﾚ値サブ画面モデル
     * @param zurechiIndex 印刷幅
     * @param zurechiVal ｽﾞﾚ値項目(値)
     * @param zurechiTextMaxLength　ｽﾞﾚ値項目(テキストMaxLength)
     * @param zurechiTextBackColor　ｽﾞﾚ値項目(BackGround)
     * @param zurechiTabIndex　ｽﾞﾚ値項目(BackGround)
     * @return ｽﾞﾚ値データ
     */
    private static GXHDO101C023Model.ZurechiData getInitZurechiData(
            GXHDO101C023Model gxhdo101C023Model, String zurechiIndex, String zurechiVal,
            String zurechiTextMaxLength, String zurechiTextBackColor, String zurechiTabIndex) {
        GXHDO101C023Model.ZurechiData zurechiData = gxhdo101C023Model.new ZurechiData();
        // ｽﾞﾚ値
        zurechiData.setZurechiVal(zurechiVal);
        zurechiData.setZurechiTextRendered(true);
        zurechiData.setZurechiTextMaxLength(zurechiTextMaxLength);
        zurechiData.setZurechiTextBackColor(zurechiTextBackColor);
        zurechiData.setZurechiTabIndex(zurechiTabIndex);
        zurechiData.setZurechiIndex(zurechiIndex);
        switch (zurechiIndex) {
            case "1":
                zurechiData.setZurechiIndexName("①");
                break;
            case "2":
                zurechiData.setZurechiIndexName("②");
                break;
            case "3":
                zurechiData.setZurechiIndexName("③");
                break;
            case "4":
                zurechiData.setZurechiIndexName("④");
                break;
            case "5":
                zurechiData.setZurechiIndexName("⑤");
                break;
            case "6":
                zurechiData.setZurechiIndexName("⑥");
                break;
            case "7":
                zurechiData.setZurechiIndexName("⑦");
                break;
            case "8":
                zurechiData.setZurechiIndexName("⑧");
                break;
        }
        

        return zurechiData;
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C023Model ｽﾞﾚ値サブ画面用モデル
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C023Model gXHDO101C023Model, List<FXHDD01> itemList) {

        List<String> zurechiDataList = new ArrayList<>();
        for (GXHDO101C023Model.ZurechiData zurechiIndexData : gXHDO101C023Model.getZurechiInputDataList()) {
            if (!StringUtil.isEmpty(zurechiIndexData.getZurechiVal())) {
                zurechiDataList.add(zurechiIndexData.getZurechiVal());
            }
        }

        FXHDD01 itemZurechi = getItemRow(itemList, gXHDO101C023Model.getReturnItemIdZurechi());

        // 全て値が設定されていた場合のみｽﾞﾚ量ｽﾀｰﾄ1～8のなかで最大量をセットする
        if (gXHDO101C023Model.getZurechiInputDataList().size() == zurechiDataList.size()) {
            setItemValue(itemZurechi, NumberUtil.getMax(zurechiDataList));
        } else {
            setItemValue(itemZurechi, null);
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
