/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SubSrTenkaFunsaiHyoryo;
import jp.co.kccs.xhd.model.GXHDO102C006Model;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/15<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C006Logic(添加材ｽﾗﾘｰ作製・粉砕入力)ロジッククラス
 * 
 * @author KCSS wxf
 * @since 2021/10/15
 */
public class GXHDO102C006Logic implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力画面のモデルデータを作成する
     *
     * @param subSrTenkaFunsaiHyoryoList 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データリスト
     * @return モデルデータ
     */
    public static GXHDO102C006Model createGXHDO102C006Model(List<SubSrTenkaFunsaiHyoryo> subSrTenkaFunsaiHyoryoList) {

        GXHDO102C006Model gxhdo102C006Model = new GXHDO102C006Model();
        gxhdo102C006Model.setShowsubgamendata(gxhdo102C006Model.new SubGamenData());
        gxhdo102C006Model.setSubgamen1(gxhdo102C006Model.new SubGamenData());
        gxhdo102C006Model.setSubgamen2(gxhdo102C006Model.new SubGamenData());

        // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C006Model.getSubgamen1(), subSrTenkaFunsaiHyoryoList.get(0));
        // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C006Model.getSubgamen2(), subSrTenkaFunsaiHyoryoList.get(1));
        return gxhdo102C006Model;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面の初期データ設定
     *
     * @param gxhdo102C006Model モデルデータ
     * @param subgamenData 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面初期データ
     */
    private static void setSubGamenInitData(GXHDO102C006Model.SubGamenData subgamendata, SubSrTenkaFunsaiHyoryo subSrTenkaFunsaiHyoryo) {
        // サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> subgamenDataHeaderList = getSubHeaderInfo(subSrTenkaFunsaiHyoryo);
        subgamendata.setSubDataTyogouryoukikaku(subgamenDataHeaderList.get(0));
        subgamendata.setSubDataTyogouzanryou(subgamenDataHeaderList.get(1));
        // サブ画面の部材①タブデータリスト設定
        subgamendata.setSubDataBuzaitab1(getSubTab1Info(subSrTenkaFunsaiHyoryo));
        // サブ画面の部材②タブデータリスト設定
        subgamendata.setSubDataBuzaitab2(getSubTab2Info(subSrTenkaFunsaiHyoryo));
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面の調合規格と調合残量のデータ取得
     *
     * @param subSrTenkaFunsaiHyoryo 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ
     * @return 調合規格と調合残量のデータリスト
     */
    private static ArrayList<FXHDD01> getSubHeaderInfo(SubSrTenkaFunsaiHyoryo subSrTenkaFunsaiHyoryo) {
        ArrayList<FXHDD01> subDataHeaderList = new ArrayList<>();
        int index = 0;
        // 【材料品名】ﾘﾝｸ押下時、サブ画面の調合規格
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouryoukikaku"), true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyogouryoukikaku()), index));
        // 【材料品名】ﾘﾝｸ押下時、サブ画面の調合残量
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouzanryou"), true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyogouzanryou()), index));
        return subDataHeaderList;
    }

    /**
     * サブ画面の部材①タブデータリスト取得
     *
     * @param subSrTenkaFunsaiHyoryo 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab1Info(SubSrTenkaFunsaiHyoryo subSrTenkaFunsaiHyoryo) {
        int index = 1;
        // サブ画面の部材①タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab1 = new ArrayList<>();
        // サブ画面の部材①タブの材料品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getZairyohinmei()), index));
        // サブ画面の部材①タブの部材在庫No1
        subDataBuzaitab1.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "1", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getBuzailotno1()), "9", "", index));
        // サブ画面の部材①タブの部材在庫品名1
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "1", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getBuzaihinmei1()), index));
        index += 1;
        // サブ画面の部材①タブの調合量1_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou"), true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou1_1()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou1_2()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_3
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou1_3()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_4
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou1_4()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_5
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou1_5()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_6
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou1_6()), "5", "", true, "g", index));
        return subDataBuzaitab1;
    }

    /**
     * サブ画面の部材②タブデータリスト取得
     *
     * @param subSrTenkaFunsaiHyoryo 添加材ｽﾗﾘｰ作製・粉砕入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab2Info(SubSrTenkaFunsaiHyoryo subSrTenkaFunsaiHyoryo) {
        int index = 1;
        // サブ画面の部材②タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab2 = new ArrayList<>();
        // サブ画面の部材②タブの材料品名
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getZairyohinmei()), index));
        // サブ画面の部材②タブの部材在庫No1
        subDataBuzaitab2.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "2", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getBuzailotno2()), "9", "", index));
        // サブ画面の部材②タブの部材在庫品名1
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "2", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getBuzaihinmei2()), index));
        index += 1;
        // サブ画面の部材②タブの調合量2_1
        subDataBuzaitab2.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou"), true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou2_1()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_2
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou2_2()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_3
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou2_3()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_4
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou2_4()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_5
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou2_5()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_6
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrTenkaFunsaiHyoryo.getTyougouryou2_6()), "5", "", true, "g", index));
        return subDataBuzaitab2;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力画面のラベル項目の属性を設定
     *
     * @param render1 表示ﾗﾍﾞﾙ1render有無
     * @param label1 表示ﾗﾍﾞﾙ1
     * @param renderOutputLabel ラベル表示render有無
     * @param value 入力項目値
     * @param index 項目ｲﾝﾃﾞｯｸｽ
     * @return 項目データ
     */
    private static FXHDD01 setLabelInitInfo(boolean render1, String label1, boolean renderOutputLabel, String value, int index) {
        FXHDD01 item = new FXHDD01();
        item.setRender1(render1);
        item.setLabel1(label1);
        item.setRenderOutputLabel(renderOutputLabel);
        item.setRenderInputText(false);
        item.setRenderInputNumber(false);
        item.setValue(value);
        item.setItemIndex(index);
        item.setBackColor1("#FFFFFF");
        return item;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力画面の入力文字列項目の属性を設定
     *
     * @param label1 表示ﾗﾍﾞﾙ1
     * @param renderInputText 入力項目(文字列)render有無
     * @param value 入力項目値
     * @param inputLength 入力項目桁数(整数部)
     * @param backColor 入力項目背景色
     * @param index 項目ｲﾝﾃﾞｯｸｽ
     * @return 項目データ
     */
    private static FXHDD01 setInputTextInitInfo(String label1, boolean renderInputText, String value, String inputLength, String backColor, int index) {
        FXHDD01 item = new FXHDD01();
        item.setRender1(true);
        item.setLabel1(label1);
        item.setRenderOutputLabel(false);
        item.setRenderInputText(renderInputText);
        item.setRenderInputNumber(false);
        item.setValue(value);
        item.setInputLength(inputLength);
        item.setBackColorInput(backColor);
        item.setItemIndex(index);
        item.setBackColor1("#FFFFFF");
        return item;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力画面の入力数値項目の属性を設定
     *
     * @param render1 表示ﾗﾍﾞﾙ1render有無
     * @param label1 表示ﾗﾍﾞﾙ1
     * @param renderInputNumber 入力項目(数値)render有無
     * @param value 入力項目値
     * @param inputLength 入力項目桁数(整数部)
     * @param backColor 入力項目背景色
     * @param render2 表示ﾗﾍﾞﾙ2render有無
     * @param label2 表示ﾗﾍﾞﾙ2
     * @param index 項目ｲﾝﾃﾞｯｸｽ
     * @return 項目データ
     */
    private static FXHDD01 setInputNumberInitInfo(boolean render1, String label1, boolean renderInputNumber, String value, String inputLength, String backColor, boolean render2, String label2, int index) {
        FXHDD01 item = new FXHDD01();
        item.setRender1(render1);
        item.setLabel1(label1);
        item.setRenderOutputLabel(false);
        item.setRenderInputText(false);
        item.setRenderInputNumber(renderInputNumber);
        item.setValue(value);
        item.setInputLength(inputLength);
        item.setBackColorInput(backColor);
        item.setRender2(render2);
        item.setLabel2(label2);
        item.setItemIndex(index);
        item.setBackColor1("#FFFFFF");
        return item;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面の調合残量の計算
     *
     * @param gxhdo102c006model モデルデータ
     */
    public static void calcTyogouzanryou(GXHDO102C006Model gxhdo102c006model) {
        GXHDO102C006Model.SubGamenData showsubgamendata = gxhdo102c006model.getShowsubgamendata();
        // ｻﾌﾞ画面の調合残量の計算
        setTyogouzanryouValue(showsubgamendata);
    }

    /**
     * サブ画面の調合残量の計算
     *
     * @param subgamendata サブ画面のデータ
     */
    private static void setTyogouzanryouValue(GXHDO102C006Model.SubGamenData subgamendata) {
        // サブ画面の調合規格
        String subDataTyogouryoukikaku = StringUtil.nullToBlank(subgamendata.getSubDataTyogouryoukikaku().getValue());
        List<String> tyougouryouList = new ArrayList<>();
        // サブ画面の調合量1_1～調合量1_6のリストを取得
        getTyogouzanryouDataList(tyougouryouList, subgamendata.getSubDataBuzaitab1());
        // サブ画面の調合量2_1～調合量2_6のリストを取得
        getTyogouzanryouDataList(tyougouryouList, subgamendata.getSubDataBuzaitab2());
        // サブ画面の調合残量の計算
        String tyogouzanryou = getTyogouzanryouKeisann(subDataTyogouryoukikaku, tyougouryouList);
        if (tyogouzanryou == null) {
            tyogouzanryou = "-";
        }
        subgamendata.getSubDataTyogouzanryou().setValue(tyogouzanryou);
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面の調合量X_1～調合量X_6のリストを取得
     *
     * @param tyougouryouList 調合量データリスト
     * @param subDataBuzaitab サブ画面タブデータリスト
     */
    private static void getTyogouzanryouDataList(List<String> tyougouryouList, List<FXHDD01> subDataBuzaitab) {
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(3).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(4).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(5).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(6).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(7).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(8).getValue()));
    }

    /**
     * 添加材ｽﾗﾘｰ作製・粉砕入力_サブ画面の調合残量の計算
     *
     * @param subDataTyogouryoukikaku 調合規格
     * @param tyougouryouList 調合量データリスト
     * @return 調合残量の計算値
     */
    private static String getTyogouzanryouKeisann(String subDataTyogouryoukikaku, List<String> tyougouryouList) {
        subDataTyogouryoukikaku = subDataTyogouryoukikaku.replace("【", "").replace("】", "");
        if (!subDataTyogouryoukikaku.contains("±")) {
            return null;
        }
        String[] kikakuchi = subDataTyogouryoukikaku.split("±");
        BigDecimal valueKikaku = ValidateUtil.numberExtraction(kikakuchi[0]);
        if (valueKikaku == null) {
            return null;
        }
        BigDecimal sum = sumTyogouzanryou(tyougouryouList);
        if (sum == null) {
            sum = BigDecimal.ZERO;
        }
        return valueKikaku.subtract(sum).toPlainString();
    }

    /**
     * 調合量の合計値を計算
     *
     * @param tyougouryouList 調合量データリスト
     * @return 合計値
     */
    private static BigDecimal sumTyogouzanryou(List<String> tyougouryouList) {
        BigDecimal sum = null;
        BigDecimal value;
        for (String strValue : tyougouryouList) {
            try {
                value = new BigDecimal(strValue);
                // 合計
                if (sum == null) {
                    sum = value;
                } else {
                    sum = sum.add(value);
                }
            } catch (NumberFormatException e) {
                // 処理なし
            }
        }

        return sum;
    }

    /**
     * 背景色のクリア処理
     *
     * @param gxhdo102c006model 添加材ｽﾗﾘｰ作製・粉砕入力サブ画面用ﾓﾃﾞﾙ
     */
    public static void clearBackColor(GXHDO102C006Model gxhdo102c006model) {
        GXHDO102C006Model.SubGamenData showsubgamendata = gxhdo102c006model.getShowsubgamendata();
        // ｻﾌﾞ画面の背景色のクリア処理
        clearSubGamenBackColor(showsubgamendata);
    }

    /**
     * 背景色のクリア処理
     *
     * @param subgamendata サブ画面のデータ
     */
    public static void clearSubGamenBackColor(GXHDO102C006Model.SubGamenData subgamendata) {
        // サブ画面の部材①タブ
        subgamendata.getSubDataBuzaitab1().forEach((tab1ItemData) -> {
            tab1ItemData.setBackColorInput("");
        });
        // サブ画面の部材②タブ
        subgamendata.getSubDataBuzaitab2().forEach((tab2ItemData) -> {
            tab2ItemData.setBackColorInput("");
        });
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gxhdo102c006model 添加材ｽﾗﾘｰ作製・粉砕入力サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO102C006Model gxhdo102c006model, List<FXHDD01> itemList) {
        GXHDO102C006Model.SubGamenData showsubgamendata = gxhdo102c006model.getShowsubgamendata();
        Integer subDataZairyokubun = showsubgamendata.getSubDataZairyokubun();
        GXHDO102C006Model.SubGamenData subgamendata = getC006subgamendata(gxhdo102c006model, subDataZairyokubun);
        
        // サブ画面から戻り値をメイン画面の項目にセットするサブ画面から戻り値をメイン画面の項目にセットする
        setReturnDataFromSubGamen(itemList, subgamendata);
        setItemStyle(itemList);
    }

    /**
     * サブ画面から戻り値をメイン画面の項目にセットする
     *
     * @param itemList 項目リスト
     * @param subgamendata サブ画面のデータ
     */
    public static void setReturnDataFromSubGamen(List<FXHDD01> itemList, GXHDO102C006Model.SubGamenData subgamendata) {
        List<String> tyougouryouListTab1 = new ArrayList<>();
        List<String> tyougouryouListTab2 = new ArrayList<>();
        // サブ画面の調合量1_1～調合量1_6のリストを取得
        getTyogouzanryouDataList(tyougouryouListTab1, subgamendata.getSubDataBuzaitab1());
        // サブ画面の調合量2_1～調合量2_6のリストを取得
        getTyogouzanryouDataList(tyougouryouListTab2, subgamendata.getSubDataBuzaitab2());

        // ｻﾌﾞ画面.部材在庫No1は主画面の部材在庫NoX_1に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdBuzailotno1()), subgamendata.getSubDataBuzaitab1().get(1).getValue());
        // ｻﾌﾞ画面.調合量1の合計値は主画面の調合量X_1に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou1()), sumTyogouzanryou(tyougouryouListTab1));

        // ｻﾌﾞ画面.部材在庫No2は主画面の部材在庫NoX_2に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdBuzailotno2()), subgamendata.getSubDataBuzaitab2().get(1).getValue());
        // ｻﾌﾞ画面.調合量2の合計値は主画面の調合量X_2に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou2()), sumTyogouzanryou(tyougouryouListTab2));
    }

    /**
     * 主画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
     *
     * @param itemList 項目データ
     */
    public static void setItemStyle(List<FXHDD01> itemList) {
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        itemList.stream().map((item) -> {
            if (item.isRender1() || item.isRenderLinkButton()) {
                if ("".equals(StringUtil.nullToBlank(item.getBackColor3()))) {
                    item.setBackColor3("#EEEEEE");
                }
                if (0 == item.getFontSize3()) {
                    item.setFontSize3(16);
                }
            }
            return item;
        }).filter((item) -> (item.isRenderOutputLabel() && !item.isRenderInputText())).map((item) -> {
            if ("".equals(StringUtil.nullToBlank(item.getBackColorInput()))) {
                item.setBackColorInput("#EEEEEE");
                item.setBackColorInputDefault("#EEEEEE");
            }
            return item;
        }).filter((item) -> (0 == item.getFontSizeInput())).forEachOrdered((item) -> {
            item.setFontSizeInput(16);
        });
    }

    /**
     * 対象項目に値をセットする
     *
     * @param itemData 項目
     * @param value 値
     */
    private static void setItemValue(FXHDD01 itemData, String value) {
        if (itemData == null) {
            return;
        }
        // 値をセット
        itemData.setValue(StringUtil.nullToBlank(value));
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
        if (listData == null || "".equals(itemId)) {
            return null;
        }
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

    /**
     * サブ画面データ取得
     *
     * @param gxhdo102c006model モデルデータ
     * @param zairyokubun 材料区分
     * @return サブ画面データ情報
     */
    public static GXHDO102C006Model.SubGamenData getC006subgamendata(GXHDO102C006Model gxhdo102c006model, int zairyokubun) {
        GXHDO102C006Model.SubGamenData subgamendata = null;
        switch (zairyokubun) {
            case 1:
                subgamendata = gxhdo102c006model.getSubgamen1();
                break;
            case 2:
                subgamendata = gxhdo102c006model.getSubgamen2();
                break;
            default:
                break;
        }
        return subgamendata;
    }
    
    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの表示用データに設定
     *
     * @param gxhdo102c006modelview モデルデータ(表示制御用)
     * @param gxhdo102c006model モデルデータ
     */
    public static void setShowsubgamendataFromView(GXHDO102C006Model gxhdo102c006modelview, GXHDO102C006Model gxhdo102c006model) {
        GXHDO102C006Model.SubGamenData showsubgamendata = gxhdo102c006modelview.getShowsubgamendata();
        gxhdo102c006model.setShowsubgamendata(showsubgamendata);
    }
    
    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの更新用データに設定
     *
     * @param gxhdo102c006modelview モデルデータ(表示制御用)
     * @param gxhdo102c006model モデルデータ
     */
    public static void setSubgamenDataFromView(GXHDO102C006Model gxhdo102c006modelview, GXHDO102C006Model gxhdo102c006model) {
        GXHDO102C006Model.SubGamenData showsubgamendata = gxhdo102c006modelview.getShowsubgamendata();
        Integer zairyokubun = showsubgamendata.getSubDataZairyokubun();
        switch (zairyokubun) {
            case 1:
                gxhdo102c006model.setSubgamen1(showsubgamendata);
                break;
            case 2:
                gxhdo102c006model.setSubgamen2(showsubgamendata);
                break;
            default:
                break;
        }
    }
}
