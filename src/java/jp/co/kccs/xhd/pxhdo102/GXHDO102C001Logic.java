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
import jp.co.kccs.xhd.db.model.SubSrGlasshyoryo;
import jp.co.kccs.xhd.model.GXHDO102C001Model;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C001Logic(ｶﾞﾗｽ作製・秤量入力)ロジッククラス
 */
public class GXHDO102C001Logic implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    /**
     * ｶﾞﾗｽ作製・秤量入力画面のモデルデータを作成する
     *
     * @param subSrGlasshyoryoList ｶﾞﾗｽ作製・秤量入力_ｻﾌﾞ画面データリスト
     * @return モデルデータ
     */
    public static GXHDO102C001Model createGXHDO102C001Model(List<SubSrGlasshyoryo> subSrGlasshyoryoList) {

        GXHDO102C001Model gxhdo102C001Model = new GXHDO102C001Model();
        SubSrGlasshyoryo sub1SrGlasshyoryoData = subSrGlasshyoryoList.get(0);
        SubSrGlasshyoryo sub2SrGlasshyoryoData = subSrGlasshyoryoList.get(1);
        // ｶﾞﾗｽ作製・秤量入力_サブ画面の部材①タブデータ設定
        //【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> sub1DataHeaderList = getSubHeaderInfo(sub1SrGlasshyoryoData);
        gxhdo102C001Model.setSub1DataTyogouryoukikaku(sub1DataHeaderList.get(0));
        gxhdo102C001Model.setSub1DataTyogouzanryou(sub1DataHeaderList.get(1));
        //【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト設定
        gxhdo102C001Model.setSub1DataBuzaitab1(getSubTab1Info(sub1SrGlasshyoryoData));
        //【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト設定
        gxhdo102C001Model.setSub1DataBuzaitab2(getSubTab2Info(sub1SrGlasshyoryoData));

        //【材料品名2】ﾘﾝｸ押下時、サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> sub2DataHeaderList = getSubHeaderInfo(sub2SrGlasshyoryoData);
        gxhdo102C001Model.setSub2DataTyogouryoukikaku(sub2DataHeaderList.get(0));
        gxhdo102C001Model.setSub2DataTyogouzanryou(sub2DataHeaderList.get(1));
        //【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブデータリスト設定
        gxhdo102C001Model.setSub2DataBuzaitab1(getSubTab1Info(sub2SrGlasshyoryoData));
        //【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブデータリスト設定
        gxhdo102C001Model.setSub2DataBuzaitab2(getSubTab2Info(sub2SrGlasshyoryoData));

        return gxhdo102C001Model;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力_サブ画面の調合規格と調合残量のデータ取得
     *
     * @param subSrGlasshyoryoData ｶﾞﾗｽ作製・秤量入力_ｻﾌﾞ画面データ
     * @return 調合規格と調合残量のデータリスト
     */
    private static ArrayList<FXHDD01> getSubHeaderInfo(SubSrGlasshyoryo subSrGlasshyoryoData) {
        ArrayList<FXHDD01> subDataHeaderList = new ArrayList<>();
        int index = 0;
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouryoukikaku"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyogouryoukikaku()), index));
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouzanryou"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyogouzanryou()), index));
        return subDataHeaderList;
    }

    /**
     * サブ画面の部材①タブデータリスト取得
     *
     * @param subSrGlasshyoryoData ｶﾞﾗｽ作製・秤量入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab1Info(SubSrGlasshyoryo subSrGlasshyoryoData) {
        int index = 1;
        // サブ画面の部材①タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab1 = new ArrayList<>();
        // サブ画面の部材①タブの材料品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getZairyohinmei()), index));
        // サブ画面の部材①タブの部材在庫No1
        subDataBuzaitab1.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "1", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzailotno1()), "9", "", index));
        // サブ画面の部材①タブの部材在庫品名1
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "1", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzaihinmei1()), index));
        index += 1;
        // サブ画面の部材①タブの調合量1_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou1_1()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou1_2()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_3
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou1_3()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_4
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou1_4()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_5
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou1_5()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材①タブの調合量1_6
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou1_6()), "5", "", true, "g", index));
        return subDataBuzaitab1;
    }

    /**
     * サブ画面の部材②タブデータリスト取得
     *
     * @param gxhdo102C001Model モデルデータ
     * @param subSrGlasshyoryoData ｶﾞﾗｽ作製・秤量入力_ｻﾌﾞ画面データ
     */
    private static ArrayList<FXHDD01> getSubTab2Info(SubSrGlasshyoryo subSrGlasshyoryoData) {
        int index = 1;
        // サブ画面の部材②タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab2 = new ArrayList<>();
        // サブ画面の部材②タブの材料品名
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getZairyohinmei()), index));
        // サブ画面の部材②タブの部材在庫No1
        subDataBuzaitab2.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "2", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzailotno2()), "9", "", index));
        // サブ画面の部材②タブの部材在庫品名1
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "2", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzaihinmei2()), index));
        index += 1;
        // サブ画面の部材②タブの調合量2_1
        subDataBuzaitab2.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou2_1()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_2
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou2_2()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_3
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou2_3()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_4
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou2_4()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_5
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou2_5()), "5", "", true, "g", index));
        index += 1;
        // サブ画面の部材②タブの調合量2_6
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyougouryou2_6()), "5", "", true, "g", index));
        return subDataBuzaitab2;
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力画面のラベル項目の属性を設定
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
     * ｶﾞﾗｽ作製・秤量入力画面の入力文字列項目の属性を設定
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
     * ｶﾞﾗｽ作製・秤量入力画面の入力数値項目の属性を設定
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
     * ｶﾞﾗｽ作製・秤量入力_サブ画面の調合残量の計算
     *
     * @param gxhdo102c001model モデルデータ
     */
    public static void calcTyogouzanryou(GXHDO102C001Model gxhdo102c001model) {
        // 【材料品名1】ﾘﾝｸ押下時、
        if (gxhdo102c001model.isSub1DataRendered()) {
            // サブ画面の調合規格
            String sub1DataTyogouryoukikaku = StringUtil.nullToBlank(gxhdo102c001model.getSub1DataTyogouryoukikaku().getValue());
            List<String> tyougouryouList = new ArrayList<>();
            // サブ画面の調合量1_1～調合量1_6のリストを取得
            getTyogouzanryouDataList(tyougouryouList, gxhdo102c001model.getSub1DataBuzaitab1());
            // サブ画面の調合量2_1～調合量2_6のリストを取得
            getTyogouzanryouDataList(tyougouryouList, gxhdo102c001model.getSub1DataBuzaitab2());
            // サブ画面の調合残量の計算
            String tyogouzanryou = getTyogouzanryouKeisann(sub1DataTyogouryoukikaku, tyougouryouList);
            if (tyogouzanryou == null) {
                tyogouzanryou = "-";
            }
            gxhdo102c001model.getSub1DataTyogouzanryou().setValue(tyogouzanryou);
        } else if (gxhdo102c001model.isSub2DataRendered()) {
            // サブ画面の調合規格
            String sub2DataTyogouryoukikaku = StringUtil.nullToBlank(gxhdo102c001model.getSub2DataTyogouryoukikaku().getValue());
            List<String> tyougouryouList = new ArrayList<>();
            // サブ画面の調合量1_1～調合量1_6のリストを取得
            getTyogouzanryouDataList(tyougouryouList, gxhdo102c001model.getSub2DataBuzaitab1());
            // サブ画面の調合量2_1～調合量2_6のリストを取得
            getTyogouzanryouDataList(tyougouryouList, gxhdo102c001model.getSub2DataBuzaitab2());
            // サブ画面の調合残量の計算
            String tyogouzanryou = getTyogouzanryouKeisann(sub2DataTyogouryoukikaku, tyougouryouList);
            if (tyogouzanryou == null) {
                tyogouzanryou = "-";
            }
            gxhdo102c001model.getSub2DataTyogouzanryou().setValue(tyogouzanryou);
        }
    }

    /**
     * ｶﾞﾗｽ作製・秤量入力_サブ画面の調合量X_1～調合量X_6のリストを取得
     *
     * @param tyougouryouList 調合量データデータ
     * @param subDataBuzaitab サブ画面タブデータリスト
     * @return 調合量X_1～調合量X_6のデータリスト
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
     * ｶﾞﾗｽ作製・秤量入力_サブ画面の調合残量の計算
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
     * @param gxhdO102c001ModelView ｶﾞﾗｽ作製・秤量入力サブ画面用ﾓﾃﾞﾙ
     */
    public static void clearBackColor(GXHDO102C001Model gxhdO102c001ModelView) {
        if (gxhdO102c001ModelView.isSub1DataRendered()) {
            //【材料品名1】ﾘﾝｸ押下時、サブ画面の部材①タブ
            gxhdO102c001ModelView.getSub1DataBuzaitab1().forEach((tab1ItemData) -> {
                tab1ItemData.setBackColorInput("");
            });
            //【材料品名1】ﾘﾝｸ押下時、サブ画面の部材②タブ
            gxhdO102c001ModelView.getSub1DataBuzaitab2().forEach((tab2ItemData) -> {
                tab2ItemData.setBackColorInput("");
            });
        } else if (gxhdO102c001ModelView.isSub2DataRendered()) {
            //【材料品名2】ﾘﾝｸ押下時、サブ画面の部材①タブ
            gxhdO102c001ModelView.getSub2DataBuzaitab1().forEach((tab1ItemData) -> {
                tab1ItemData.setBackColorInput("");
            });
            //【材料品名2】ﾘﾝｸ押下時、サブ画面の部材②タブ
            gxhdO102c001ModelView.getSub2DataBuzaitab2().forEach((tab2ItemData) -> {
                tab2ItemData.setBackColorInput("");
            });
        }
    }
    
    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gxhdo102c001model ｶﾞﾗｽ作製・秤量入力サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO102C001Model gxhdo102c001model, List<FXHDD01> itemList) {
        List<String> tyougouryouListTab1 = new ArrayList<>();
        List<String> tyougouryouListTab2 = new ArrayList<>();
        // 【材料品名1】ﾘﾝｸ押下時、
        if (gxhdo102c001model.isSub1DataRendered()) {
            // サブ画面の調合量1_1～調合量1_6のリストを取得
            getTyogouzanryouDataList(tyougouryouListTab1, gxhdo102c001model.getSub1DataBuzaitab1());
            // サブ画面の調合量2_1～調合量2_6のリストを取得
            getTyogouzanryouDataList(tyougouryouListTab2, gxhdo102c001model.getSub1DataBuzaitab2());

            // ｻﾌﾞ画面.部材在庫No1は主画面の部材在庫No1_1に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdBuzailotno1_1()), gxhdo102c001model.getSub1DataBuzaitab1().get(1).getValue());
            // ｻﾌﾞ画面.調合量1の合計値は主画面の調合量1_1に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdTyougouryou1_1()), sumTyogouzanryou(tyougouryouListTab1));

            // ｻﾌﾞ画面.部材在庫No2は主画面の部材在庫No1_2に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdBuzailotno1_2()), gxhdo102c001model.getSub1DataBuzaitab2().get(1).getValue());
            // ｻﾌﾞ画面.調合量2の合計値は主画面の調合量1_2に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdTyougouryou1_2()), sumTyogouzanryou(tyougouryouListTab2));
            // 【材料品名2】ﾘﾝｸ押下時、
        } else if (gxhdo102c001model.isSub2DataRendered()) {
            // サブ画面の調合量1_1～調合量1_6のリストを取得
            getTyogouzanryouDataList(tyougouryouListTab1, gxhdo102c001model.getSub2DataBuzaitab1());
            // サブ画面の調合量2_1～調合量2_6のリストを取得
            getTyogouzanryouDataList(tyougouryouListTab2, gxhdo102c001model.getSub2DataBuzaitab2());

            // ｻﾌﾞ画面.部材在庫No1は主画面の部材在庫No1_1に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdBuzailotno2_1()), gxhdo102c001model.getSub2DataBuzaitab1().get(1).getValue());
            // ｻﾌﾞ画面.調合量1の合計値は主画面の調合量1_1に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdTyougouryou2_1()), sumTyogouzanryou(tyougouryouListTab1));

            // ｻﾌﾞ画面.部材在庫No2は主画面の部材在庫No1_2に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdBuzailotno2_2()), gxhdo102c001model.getSub2DataBuzaitab2().get(1).getValue());
            // ｻﾌﾞ画面.調合量2の合計値は主画面の調合量1_2に設定
            setItemValue(getItemRow(itemList, gxhdo102c001model.getReturnItemIdTyougouryou2_2()), sumTyogouzanryou(tyougouryouListTab2));
        }
        set102B001ItemStyle(itemList);
    }
    
    /**
     * 主画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
     *
     * @param itemList 項目データ
     */
    public static void set102B001ItemStyle(List<FXHDD01> itemList) {
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
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }
}
