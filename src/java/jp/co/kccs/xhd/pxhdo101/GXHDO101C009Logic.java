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
import jp.co.kccs.xhd.model.GXHDO101C009Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
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
 * GXHDO101C009(合わせ(RZ))ロジッククラス
 */
public class GXHDO101C009Logic {

    public static GXHDO101C009Model createGXHDO101C009Model(String[] startValues) {
        GXHDO101C009Model model = new GXHDO101C009Model();
        List<GXHDO101C009Model.AwaseRzData> awaseRzDataList = new ArrayList<>();
        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < startValues.length; i++) {
            awaseRzDataList.add(getInitAwaseRzData(model, String.valueOf(i + 1), startValues[i], "TEXT", "1", "3", "9.999", "-9.999", "", String.valueOf(i + 1)));
        }
        model.setAwaseRzDataList(awaseRzDataList);
        return model;
    }

    /**
     * 合わせ(RZ)データ初期化データ取得
     *
     * @param GXHDO101C009Model ﾊﾟﾀｰﾝ間距離画面モデル
     * @param awaseRz 合わせ(RZ)
     * @param startVal スタート項目(値)
     * @param startInputType　スタート項目(入力タイプ)
     * @param startTextMaxLength　スタート項目(テキストMaxLength)
     * @param startTextDecimalPlaces　スタート項目(テキストDecimalPlaces)
     * @param startTextMaxValue　スタート項目(テキストMaxValue)
     * @param startTextMinValue　スタート項目(テキストMinValue)
     * @param startTextBackColor　スタート項目(BackGround)
     * @param startTabIndex　スタート項目(TabIndex)
     * @return 合わせ(RZ)データ
     */
    private static GXHDO101C009Model.AwaseRzData getInitAwaseRzData(GXHDO101C009Model GXHDO101C009Model, String awaseRz, 
            String startVal, String startInputType, String startTextMaxLength, String startTextDecimalPlaces, String startTextMaxValue, 
            String startTextMinValue, String startTextBackColor, String startTabIndex) {
        
        GXHDO101C009Model.AwaseRzData awaseRzListData = GXHDO101C009Model.new AwaseRzData();
        // 合わせ(RZ)
        awaseRzListData.setAwaseRz(awaseRz);
        // スタート
        awaseRzListData.setValue(startVal);
        if ("TEXT".equals(startInputType)) {
            awaseRzListData.setTextRendered(true);
            awaseRzListData.setLabelRendered(false);
        } else {
            awaseRzListData.setTextRendered(false);
            awaseRzListData.setLabelRendered(true);
        }
        awaseRzListData.setTextMaxLength(startTextMaxLength);
        awaseRzListData.setTextDecimalPlaces(startTextDecimalPlaces);
        awaseRzListData.setTextMaxValue(startTextMaxValue);
        awaseRzListData.setTextMinValue(startTextMinValue);
        awaseRzListData.setTextBackColor(startTextBackColor);
        awaseRzListData.setTabIndex(startTabIndex);

        return awaseRzListData;
    }
    
    /**
     * 入力チェック
     *
     * @param gXHDO101C009Model 合わせ(RZ)入力サブ画面用モデル
     * @return エラーリスト
     */
    public static List<String> checkInput(GXHDO101C009Model gXHDO101C009Model) {

        List<String> errorList = new ArrayList<>();

        // 6～9にデータが入力されているかどうか確認
        int index = 0;
        boolean hasInputAfter6 = false;
        for (GXHDO101C009Model.AwaseRzData denkyokuMakuatsuData : gXHDO101C009Model.getAwaseRzDataList()) {
            index++;
            if (6 <= index && !StringUtil.isEmpty(denkyokuMakuatsuData.getValue())) {
                hasInputAfter6 = true;
                break;
            }
        }

        int indexMain = 0;
        for (GXHDO101C009Model.AwaseRzData awaseRzData : gXHDO101C009Model.getAwaseRzDataList()) {
            indexMain++;
            if (StringUtil.isEmpty(awaseRzData.getValue())) {
                if (indexMain <= 5) {
                    //1～5は未入力はエラー
                    awaseRzData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "合わせ(RZ)" + awaseRzData.getAwaseRz()));
                    return errorList;

                } else if (hasInputAfter6) {
                    //6～9はいずれかが入力されている場合、未入力はエラー
                    awaseRzData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "合わせ(RZ)" + awaseRzData.getAwaseRz()));
                    return errorList;
                }
            }
        }

        return errorList;
    }
    
        /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C009Model 合わせ(RZ)サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C009Model gXHDO101C009Model, List<FXHDD01> itemList) {

        FXHDD01 itemAve = getItemRow(itemList, gXHDO101C009Model.getReturnItemIdAve());
        if (itemAve == null) {
            return;
        }

        List<String> dataList = new ArrayList<>();
        List<String> dataList5 = new ArrayList<>();
        for (GXHDO101C009Model.AwaseRzData makuatsuData : gXHDO101C009Model.getAwaseRzDataList()) {
            if (StringUtil.isEmpty(makuatsuData.getValue())) {
                break;
            }
            dataList.add(makuatsuData.getValue());
            if (dataList5.size() < 5) {
                dataList5.add(makuatsuData.getValue());
            }
        }

        if (dataList.size() == 9) {
            // 全て値が設定されていた場合のみ算出値をセットする
            setItemValue(itemAve, NumberUtil.getMax(dataList));
        } else if (dataList5.size() == 5) {
            // 1～5値が設定されていた場合のみ算出値をセットする
            setItemValue(itemAve, NumberUtil.getMax(dataList5));
        } else {
            setItemValue(itemAve, null);
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
            // 小数指定されている場合は小数部以下は四捨五入
            if (!StringUtil.isEmpty(itemData.getInputLengthDec())) {
                try {
                    value = value.setScale(Integer.parseInt(itemData.getInputLengthDec()), RoundingMode.HALF_UP);
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
