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
import jp.co.kccs.xhd.db.model.SubSrYuudentaiYouzai;
import jp.co.kccs.xhd.model.GXHDO102C009Model;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/11/09<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2022/05/16<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	材料品名ﾘﾝｸ押下時、調合量規格チェックの追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C009Logic(誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力)ロジッククラス
 *
 * @author KCSS K.Jo
 * @since 2021/11/09
 */
public class GXHDO102C009Logic implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力画面のモデルデータを作成する
     *
     * @param subSrYuudentaiYouzaiList 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データリスト
     * @return モデルデータ
     */
    public static GXHDO102C009Model createGXHDO102C009Model(List<SubSrYuudentaiYouzai> subSrYuudentaiYouzaiList) {

        GXHDO102C009Model gxhdo102C009Model = new GXHDO102C009Model();
        gxhdo102C009Model.setShowsubgamendata(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen1(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen2(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen3(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen4(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen5(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen6(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen7(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen8(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen9(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen10(gxhdo102C009Model.new SubGamenData());
        gxhdo102C009Model.setSubgamen11(gxhdo102C009Model.new SubGamenData());

        // 分散材1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen1(), subSrYuudentaiYouzaiList.get(0));
        // 分散材2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen2(), subSrYuudentaiYouzaiList.get(1));
        // 溶剤1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen3(), subSrYuudentaiYouzaiList.get(2));
        // 溶剤2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen4(), subSrYuudentaiYouzaiList.get(3));
        // 溶剤3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen5(), subSrYuudentaiYouzaiList.get(4));
        // 溶剤4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen6(), subSrYuudentaiYouzaiList.get(5));
        // 溶剤5_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen7(), subSrYuudentaiYouzaiList.get(6));
        // 溶剤6_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen8(), subSrYuudentaiYouzaiList.get(7));
        // 溶剤7_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen9(), subSrYuudentaiYouzaiList.get(8));
        // 溶剤8_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen10(), subSrYuudentaiYouzaiList.get(9));
        // 溶剤9_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C009Model.getSubgamen11(), subSrYuudentaiYouzaiList.get(10));
        return gxhdo102C009Model;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面の初期データ設定
     *
     * @param gxhdo102C009Model モデルデータ
     * @param subgamenData 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面初期データ
     */
    private static void setSubGamenInitData(GXHDO102C009Model.SubGamenData subgamendata, SubSrYuudentaiYouzai subSrYuudentaiYouzaiData) {
        // サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> subgamenDataHeaderList = getSubHeaderInfo(subSrYuudentaiYouzaiData);
        subgamendata.setSubDataTyogouryoukikaku(subgamenDataHeaderList.get(0));
        subgamendata.setSubDataTyogouzanryou(subgamenDataHeaderList.get(1));
        // サブ画面の部材①タブデータリスト設定
        subgamendata.setSubDataBuzaitab1(getSubTab1Info(subSrYuudentaiYouzaiData));
        // サブ画面の部材②タブデータリスト設定
        subgamendata.setSubDataBuzaitab2(getSubTab2Info(subSrYuudentaiYouzaiData));
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面の調合規格と調合残量のデータ取得
     *
     * @param subSrYuudentaiYouzaiData 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ
     * @return 調合規格と調合残量のデータリスト
     */
    private static ArrayList<FXHDD01> getSubHeaderInfo(SubSrYuudentaiYouzai subSrYuudentaiYouzaiData) {
        ArrayList<FXHDD01> subDataHeaderList = new ArrayList<>();
        int index = 0;
        // サブ画面の調合規格
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouryoukikaku"), true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyogouryoukikaku()), StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getStandardpattern()), index));
        // サブ画面の調合残量
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouzanryou"), true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyogouzanryou()), StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getStandardpattern()), index));
        return subDataHeaderList;
    }

    /**
     * サブ画面の部材①タブデータリスト取得
     *
     * @param subSrYuudentaiYouzaiData 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab1Info(SubSrYuudentaiYouzai subSrYuudentaiYouzaiData) {
        int index = 1;
        // サブ画面の部材①タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab1 = new ArrayList<>();
        // サブ画面の部材①タブの材料品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getZairyohinmei()), StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getStandardpattern()), index));
        // サブ画面の部材①タブの部材在庫No1
        subDataBuzaitab1.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "1", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getBuzailotno1()), "9", "", index));
        // サブ画面の部材①タブの部材在庫品名1
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "1", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getBuzaihinmei1()), StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getStandardpattern()), index));
        index += 1;
        // サブ画面の部材①タブの風袋重量1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("fuutaijyuuryou") + "1", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getFuutaijyuuryou1()), "4", "", true, "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量1_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "1", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou1_1()), "5", "", true, "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量1_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou1_2()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量1_3
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou1_3()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量1_4
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou1_4()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量1_5
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou1_5()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量1_6
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou1_6()), "5", "", true, "g", index, true));
        return subDataBuzaitab1;
    }

    /**
     * サブ画面の部材②タブデータリスト取得
     *
     * @param subSrYuudentaiYouzaiData 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab2Info(SubSrYuudentaiYouzai subSrYuudentaiYouzaiData) {
        int index = 1;
        // サブ画面の部材②タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab2 = new ArrayList<>();
        // サブ画面の部材②タブの材料品名
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getZairyohinmei()), StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getStandardpattern()), index));
        // サブ画面の部材②タブの部材在庫No1
        subDataBuzaitab2.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "2", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getBuzailotno2()), "9", "", index));
        // サブ画面の部材②タブの部材在庫品名1
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "2", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getBuzaihinmei2()), StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getStandardpattern()), index));
        index += 1;
        // サブ画面の部材①タブの風袋重量2
        subDataBuzaitab2.add(setInputNumberInitInfo(true, MessageUtil.getMessage("fuutaijyuuryou") + "2", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getFuutaijyuuryou2()), "4", "", true, "g", index, false));
        index += 1;
        // サブ画面の部材②タブの調合量2_1
        subDataBuzaitab2.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "2", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou2_1()), "5", "", true, "g", index, false));
        index += 1;
        // サブ画面の部材②タブの調合量2_2
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou2_2()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材②タブの調合量2_3
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou2_3()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材②タブの調合量2_4
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou2_4()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材②タブの調合量2_5
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou2_5()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材②タブの調合量2_6
        subDataBuzaitab2.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiYouzaiData.getTyougouryou2_6()), "5", "", true, "g", index, true));
        return subDataBuzaitab2;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力画面のラベル項目の属性を設定
     *
     * @param render1 表示ﾗﾍﾞﾙ1render有無
     * @param label1 表示ﾗﾍﾞﾙ1
     * @param renderOutputLabel ラベル表示render有無
     * @param value 入力項目値
     * @param index 項目ｲﾝﾃﾞｯｸｽ
     * @return 項目データ
     */
    private static FXHDD01 setLabelInitInfo(boolean render1, String label1, boolean renderOutputLabel, String value, String standardPattern, int index) {
        FXHDD01 item = new FXHDD01();
        item.setRender1(render1);
        item.setLabel1(label1);
        item.setRenderOutputLabel(renderOutputLabel);
        item.setRenderInputText(false);
        item.setRenderInputNumber(false);
        item.setValue(value);
        item.setStandardPattern(standardPattern);
        item.setItemIndex(index);
        item.setBackColor1("#FFFFFF");
        return item;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力画面の入力文字列項目の属性を設定
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力画面の入力数値項目の属性を設定
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
     * @param customStyleInputFlag 入力項目カスタムスタイル設定フラグ
     * @return 項目データ
     */
    private static FXHDD01 setInputNumberInitInfo(boolean render1, String label1, boolean renderInputNumber, String value, String inputLength, String backColor, boolean render2, String label2, int index,
            boolean customStyleInputFlag) {
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
        if (customStyleInputFlag) {
            item.setCustomStyleInput("border-top-style: hidden;");
        }
        return item;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面の調合残量の計算
     *
     * @param gxhdo102c009model モデルデータ
     */
    public static void calcTyogouzanryou(GXHDO102C009Model gxhdo102c009model) {
        GXHDO102C009Model.SubGamenData showsubgamendata = gxhdo102c009model.getShowsubgamendata();
        // ｻﾌﾞ画面の調合残量の計算
        setTyogouzanryouValue(showsubgamendata);
    }

    /**
     * サブ画面の調合残量の計算
     *
     * @param subgamendata サブ画面のデータ
     */
    private static void setTyogouzanryouValue(GXHDO102C009Model.SubGamenData subgamendata) {
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
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面の調合量X_1～調合量X_6のリストを取得
     *
     * @param tyougouryouList 調合量データリスト
     * @param subDataBuzaitab サブ画面タブデータリスト
     */
    private static void getTyogouzanryouDataList(List<String> tyougouryouList, List<FXHDD01> subDataBuzaitab) {
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(4).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(5).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(6).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(7).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(8).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(9).getValue()));
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力_サブ画面の調合残量の計算
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
     * @param gxhdo102c009model 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面用ﾓﾃﾞﾙ
     */
    public static void clearBackColor(GXHDO102C009Model gxhdo102c009model) {
        GXHDO102C009Model.SubGamenData showsubgamendata = gxhdo102c009model.getShowsubgamendata();
        // ｻﾌﾞ画面の背景色のクリア処理
        clearSubGamenBackColor(showsubgamendata);
    }

    /**
     * 背景色のクリア処理
     *
     * @param subgamendata サブ画面のデータ
     */
    public static void clearSubGamenBackColor(GXHDO102C009Model.SubGamenData subgamendata) {
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
     * @param gxhdo102c009model 誘電体ｽﾗﾘｰ作製・溶剤・添加材ｽﾗﾘｰ秤量入力サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO102C009Model gxhdo102c009model, List<FXHDD01> itemList) {
        GXHDO102C009Model.SubGamenData showsubgamendata = gxhdo102c009model.getShowsubgamendata();
        Integer subDataZairyokubun = showsubgamendata.getSubDataZairyokubun();
        GXHDO102C009Model.SubGamenData subgamendata = getC009subgamendata(gxhdo102c009model, subDataZairyokubun);

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
    public static void setReturnDataFromSubGamen(List<FXHDD01> itemList, GXHDO102C009Model.SubGamenData subgamendata) {
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
     * @param gxhdo102c009model モデルデータ
     * @param zairyokubun 材料区分
     * @return サブ画面データ情報
     */
    public static GXHDO102C009Model.SubGamenData getC009subgamendata(GXHDO102C009Model gxhdo102c009model, int zairyokubun) {
        GXHDO102C009Model.SubGamenData subgamendata = null;
        switch (zairyokubun) {
            case 1:
                subgamendata = gxhdo102c009model.getSubgamen1();
                break;
            case 2:
                subgamendata = gxhdo102c009model.getSubgamen2();
                break;
            case 3:
                subgamendata = gxhdo102c009model.getSubgamen3();
                break;
            case 4:
                subgamendata = gxhdo102c009model.getSubgamen4();
                break;
            case 5:
                subgamendata = gxhdo102c009model.getSubgamen5();
                break;
            case 6:
                subgamendata = gxhdo102c009model.getSubgamen6();
                break;
            case 7:
                subgamendata = gxhdo102c009model.getSubgamen7();
                break;
            case 8:
                subgamendata = gxhdo102c009model.getSubgamen8();
                break;
            case 9:
                subgamendata = gxhdo102c009model.getSubgamen9();
                break;
            case 10:
                subgamendata = gxhdo102c009model.getSubgamen10();
                break;
            case 11:
                subgamendata = gxhdo102c009model.getSubgamen11();
                break;
            default:
                break;
        }
        return subgamendata;
    }

    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの表示用データに設定
     *
     * @param gxhdo102c009modelview モデルデータ(表示制御用)
     * @param gxhdo102c009model モデルデータ
     */
    public static void setShowsubgamendataFromView(GXHDO102C009Model gxhdo102c009modelview, GXHDO102C009Model gxhdo102c009model) {
        GXHDO102C009Model.SubGamenData showsubgamendata = gxhdo102c009modelview.getShowsubgamendata();
        gxhdo102c009model.setShowsubgamendata(showsubgamendata);
    }

    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの更新用データに設定
     *
     * @param gxhdo102c009modelview モデルデータ(表示制御用)
     * @param gxhdo102c009model モデルデータ
     */
    public static void setSubgamenDataFromView(GXHDO102C009Model gxhdo102c009modelview, GXHDO102C009Model gxhdo102c009model) {
        GXHDO102C009Model.SubGamenData showsubgamendata = gxhdo102c009modelview.getShowsubgamendata();
        Integer zairyokubun = showsubgamendata.getSubDataZairyokubun();
        switch (zairyokubun) {
            case 1:
                gxhdo102c009model.setSubgamen1(showsubgamendata);
                break;
            case 2:
                gxhdo102c009model.setSubgamen2(showsubgamendata);
                break;
            case 3:
                gxhdo102c009model.setSubgamen3(showsubgamendata);
                break;
            case 4:
                gxhdo102c009model.setSubgamen4(showsubgamendata);
                break;
            case 5:
                gxhdo102c009model.setSubgamen5(showsubgamendata);
                break;
            case 6:
                gxhdo102c009model.setSubgamen6(showsubgamendata);
                break;
            case 7:
                gxhdo102c009model.setSubgamen7(showsubgamendata);
                break;
            case 8:
                gxhdo102c009model.setSubgamen8(showsubgamendata);
                break;
            case 9:
                gxhdo102c009model.setSubgamen9(showsubgamendata);
                break;
            case 10:
                gxhdo102c009model.setSubgamen10(showsubgamendata);
                break;
            case 11:
                gxhdo102c009model.setSubgamen11(showsubgamendata);
                break;
            default:
                break;
        }
    }
}
