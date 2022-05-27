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
import jp.co.kccs.xhd.db.model.SubSrGlassslurryhyoryo;
import jp.co.kccs.xhd.model.GXHDO102C002Model;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/09/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
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
 * GXHDO102C002Logic(ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力)ロジッククラス
 */
public class GXHDO102C002Logic implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力画面のモデルデータを作成する
     *
     * @param subSrGlasshyoryoList ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データリスト
     * @return モデルデータ
     */
    public static GXHDO102C002Model createGXHDO102C002Model(List<SubSrGlassslurryhyoryo> subSrGlasshyoryoList) {

        GXHDO102C002Model gxhdo102C002Model = new GXHDO102C002Model();
        gxhdo102C002Model.setShowsubgamendata(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto1subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto1subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto1subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto1subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto2subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto2subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto2subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto2subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto3subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto3subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto3subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto3subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto4subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto4subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto4subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto4subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto5subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto5subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto5subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto5subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto6subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto6subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto6subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto6subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto7subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto7subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto7subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto7subgamen4(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto8subgamen1(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto8subgamen2(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto8subgamen3(gxhdo102C002Model.new C002SubGamenData());
        gxhdo102C002Model.setPotto8subgamen4(gxhdo102C002Model.new C002SubGamenData());

        // ﾎﾟｯﾄ1タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto1subgamen1(), subSrGlasshyoryoList.get(0));
        // ﾎﾟｯﾄ1タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto1subgamen2(), subSrGlasshyoryoList.get(1));
        // ﾎﾟｯﾄ1タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto1subgamen3(), subSrGlasshyoryoList.get(2));
        // ﾎﾟｯﾄ1タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto1subgamen4(), subSrGlasshyoryoList.get(3));
        // ﾎﾟｯﾄ2タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto2subgamen1(), subSrGlasshyoryoList.get(4));
        // ﾎﾟｯﾄ2タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto2subgamen2(), subSrGlasshyoryoList.get(5));
        // ﾎﾟｯﾄ2タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto2subgamen3(), subSrGlasshyoryoList.get(6));
        // ﾎﾟｯﾄ2タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto2subgamen4(), subSrGlasshyoryoList.get(7));
        // ﾎﾟｯﾄ3タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto3subgamen1(), subSrGlasshyoryoList.get(8));
        // ﾎﾟｯﾄ3タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto3subgamen2(), subSrGlasshyoryoList.get(9));
        // ﾎﾟｯﾄ3タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto3subgamen3(), subSrGlasshyoryoList.get(10));
        // ﾎﾟｯﾄ3タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto3subgamen4(), subSrGlasshyoryoList.get(11));
        // ﾎﾟｯﾄ4タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto4subgamen1(), subSrGlasshyoryoList.get(12));
        // ﾎﾟｯﾄ4タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto4subgamen2(), subSrGlasshyoryoList.get(13));
        // ﾎﾟｯﾄ4タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto4subgamen3(), subSrGlasshyoryoList.get(14));
        // ﾎﾟｯﾄ4タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto4subgamen4(), subSrGlasshyoryoList.get(15));
        // ﾎﾟｯﾄ5タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto5subgamen1(), subSrGlasshyoryoList.get(16));
        // ﾎﾟｯﾄ5タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto5subgamen2(), subSrGlasshyoryoList.get(17));
        // ﾎﾟｯﾄ5タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto5subgamen3(), subSrGlasshyoryoList.get(18));
        // ﾎﾟｯﾄ5タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto5subgamen4(), subSrGlasshyoryoList.get(19));
        // ﾎﾟｯﾄ6タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto6subgamen1(), subSrGlasshyoryoList.get(20));
        // ﾎﾟｯﾄ6タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto6subgamen2(), subSrGlasshyoryoList.get(21));
        // ﾎﾟｯﾄ6タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto6subgamen3(), subSrGlasshyoryoList.get(22));
        // ﾎﾟｯﾄ6タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto6subgamen4(), subSrGlasshyoryoList.get(23));
        // ﾎﾟｯﾄ7タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto7subgamen1(), subSrGlasshyoryoList.get(24));
        // ﾎﾟｯﾄ7タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto7subgamen2(), subSrGlasshyoryoList.get(25));
        // ﾎﾟｯﾄ7タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto7subgamen3(), subSrGlasshyoryoList.get(26));
        // ﾎﾟｯﾄ7タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto7subgamen4(), subSrGlasshyoryoList.get(27));
        // ﾎﾟｯﾄ8タブの【材料品名1】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto8subgamen1(), subSrGlasshyoryoList.get(28));
        // ﾎﾟｯﾄ8タブの【材料品名2】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto8subgamen2(), subSrGlasshyoryoList.get(29));
        // ﾎﾟｯﾄ8タブの【材料品名3】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto8subgamen3(), subSrGlasshyoryoList.get(30));
        // ﾎﾟｯﾄ8タブの【材料品名4】のサブ画面の初期データ設定
        setSubGamenInitData(gxhdo102C002Model.getPotto8subgamen4(), subSrGlasshyoryoList.get(31));
        return gxhdo102C002Model;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面の初期データ設定
     *
     * @param gxhdo102C002Model モデルデータ
     * @param pottosubgamenData ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面初期データ
     */
    private static void setSubGamenInitData(GXHDO102C002Model.C002SubGamenData c002subgamendata, SubSrGlassslurryhyoryo pottosubgamenData) {
        // サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> pottosubgamenDataHeaderList = getSubHeaderInfo(pottosubgamenData);
        c002subgamendata.setSubDataTyogouryoukikaku(pottosubgamenDataHeaderList.get(0));
        c002subgamendata.setSubDataTyogouzanryou(pottosubgamenDataHeaderList.get(1));
        // サブ画面の部材①タブデータリスト設定
        c002subgamendata.setSubDataBuzaitab1(getSubTab1Info(pottosubgamenData));
        // サブ画面の部材②タブデータリスト設定
        c002subgamendata.setSubDataBuzaitab2(getSubTab2Info(pottosubgamenData));
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面の調合規格と調合残量のデータ取得
     *
     * @param subSrGlasshyoryoData ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ
     * @return 調合規格と調合残量のデータリスト
     */
    private static ArrayList<FXHDD01> getSubHeaderInfo(SubSrGlassslurryhyoryo subSrGlasshyoryoData) {
        ArrayList<FXHDD01> subDataHeaderList = new ArrayList<>();
        int index = 0;
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouryoukikaku"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyogouryoukikaku()), StringUtil.nullToBlank(subSrGlasshyoryoData.getStandardpattern()), index));
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量 TODO
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouzanryou"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getTyogouzanryou()), StringUtil.nullToBlank(subSrGlasshyoryoData.getStandardpattern()), index));
        return subDataHeaderList;
    }

    /**
     * サブ画面の部材①タブデータリスト取得
     *
     * @param subSrGlasshyoryoData ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab1Info(SubSrGlassslurryhyoryo subSrGlasshyoryoData) {
        int index = 1;
        // サブ画面の部材①タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab1 = new ArrayList<>();
        // サブ画面の部材①タブの材料品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getZairyohinmei()), StringUtil.nullToBlank(subSrGlasshyoryoData.getStandardpattern()), index));
        // サブ画面の部材①タブの部材在庫No1
        subDataBuzaitab1.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "1", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzailotno1()), "9", "", index));
        // サブ画面の部材①タブの部材在庫品名1
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "1", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzaihinmei1()), StringUtil.nullToBlank(subSrGlasshyoryoData.getStandardpattern()), index));
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
     * @param subSrGlasshyoryoData ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab2Info(SubSrGlassslurryhyoryo subSrGlasshyoryoData) {
        int index = 1;
        // サブ画面の部材②タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab2 = new ArrayList<>();
        // サブ画面の部材②タブの材料品名
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrGlasshyoryoData.getZairyohinmei()), StringUtil.nullToBlank(subSrGlasshyoryoData.getStandardpattern()), index));
        // サブ画面の部材②タブの部材在庫No1
        subDataBuzaitab2.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno") + "2", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzailotno2()), "9", "", index));
        // サブ画面の部材②タブの部材在庫品名1
        subDataBuzaitab2.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei") + "2", true, StringUtil.nullToBlank(subSrGlasshyoryoData.getBuzaihinmei2()), StringUtil.nullToBlank(subSrGlasshyoryoData.getStandardpattern()), index));
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力画面のラベル項目の属性を設定
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力画面の入力文字列項目の属性を設定
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力画面の入力数値項目の属性を設定
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面の調合残量の計算
     *
     * @param gxhdo102c002model モデルデータ
     */
    public static void calcTyogouzanryou(GXHDO102C002Model gxhdo102c002model) {
        GXHDO102C002Model.C002SubGamenData showsubgamendata = gxhdo102c002model.getShowsubgamendata();
        // ｻﾌﾞ画面の調合残量の計算
        setTyogouzanryouValue(showsubgamendata);
    }

    /**
     * サブ画面の調合残量の計算
     *
     * @param c002subgamendata サブ画面のデータ
     */
    private static void setTyogouzanryouValue(GXHDO102C002Model.C002SubGamenData c002subgamendata) {
        // サブ画面の調合規格
        String subDataTyogouryoukikaku = StringUtil.nullToBlank(c002subgamendata.getSubDataTyogouryoukikaku().getValue());
        List<String> tyougouryouList = new ArrayList<>();
        // サブ画面の調合量1_1～調合量1_6のリストを取得
        getTyogouzanryouDataList(tyougouryouList, c002subgamendata.getSubDataBuzaitab1());
        // サブ画面の調合量2_1～調合量2_6のリストを取得
        getTyogouzanryouDataList(tyougouryouList, c002subgamendata.getSubDataBuzaitab2());
        // サブ画面の調合残量の計算
        String tyogouzanryou = getTyogouzanryouKeisann(subDataTyogouryoukikaku, tyougouryouList);
        if (tyogouzanryou == null) {
            tyogouzanryou = "-";
        }
        c002subgamendata.getSubDataTyogouzanryou().setValue(tyogouzanryou);
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面の調合量X_1～調合量X_6のリストを取得
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力_サブ画面の調合残量の計算
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
     * @param gxhdo102c002model ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面用ﾓﾃﾞﾙ
     */
    public static void clearBackColor(GXHDO102C002Model gxhdo102c002model) {
        GXHDO102C002Model.C002SubGamenData showsubgamendata = gxhdo102c002model.getShowsubgamendata();
        // ｻﾌﾞ画面の背景色のクリア処理
        clearSubGamenBackColor(showsubgamendata);
    }

    /**
     * 背景色のクリア処理
     *
     * @param c002subgamendata サブ画面のデータ
     */
    public static void clearSubGamenBackColor(GXHDO102C002Model.C002SubGamenData c002subgamendata) {
        // サブ画面の部材①タブ
        c002subgamendata.getSubDataBuzaitab1().forEach((tab1ItemData) -> {
            tab1ItemData.setBackColorInput("");
        });
        // サブ画面の部材②タブ
        c002subgamendata.getSubDataBuzaitab2().forEach((tab2ItemData) -> {
            tab2ItemData.setBackColorInput("");
        });
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gxhdo102c002model ｶﾞﾗｽｽﾗﾘｰ作製・秤量入力サブ画面用ﾓﾃﾞﾙ
     * @param itemListEx 項目リスト
     */
    public static void setReturnData(GXHDO102C002Model gxhdo102c002model, List<FXHDD01> itemListEx) {
        GXHDO102C002Model.C002SubGamenData showsubgamendata = gxhdo102c002model.getShowsubgamendata();
        Integer subDataPot = showsubgamendata.getSubDataPot();
        Integer subDataZairyokubun = showsubgamendata.getSubDataZairyokubun();
        GXHDO102C002Model.C002SubGamenData c002subgamendata = getC002subgamendata(gxhdo102c002model, subDataPot, subDataZairyokubun);
        
        // サブ画面から戻り値をメイン画面の項目にセットするサブ画面から戻り値をメイン画面の項目にセットする
        setReturnDataFromSubGamen(itemListEx, c002subgamendata);
        set102B005ItemStyle(itemListEx);
    }

    /**
     * サブ画面から戻り値をメイン画面の項目にセットする
     *
     * @param itemListEx 項目リスト
     * @param c002subgamendata サブ画面のデータ
     */
    public static void setReturnDataFromSubGamen(List<FXHDD01> itemListEx, GXHDO102C002Model.C002SubGamenData c002subgamendata) {
        List<String> tyougouryouListTab1 = new ArrayList<>();
        List<String> tyougouryouListTab2 = new ArrayList<>();
        // サブ画面の調合量1_1～調合量1_6のリストを取得
        getTyogouzanryouDataList(tyougouryouListTab1, c002subgamendata.getSubDataBuzaitab1());
        // サブ画面の調合量2_1～調合量2_6のリストを取得
        getTyogouzanryouDataList(tyougouryouListTab2, c002subgamendata.getSubDataBuzaitab2());

        // ｻﾌﾞ画面.部材在庫No1は主画面の部材在庫NoX_1に設定
        setItemValue(getItemRow(itemListEx, c002subgamendata.getReturnItemIdBuzailotno1()), c002subgamendata.getSubDataBuzaitab1().get(1).getValue());
        // ｻﾌﾞ画面.調合量1の合計値は主画面の調合量X_1に設定
        setItemValue(getItemRow(itemListEx, c002subgamendata.getReturnItemIdTyougouryou1()), sumTyogouzanryou(tyougouryouListTab1));

        // ｻﾌﾞ画面.部材在庫No2は主画面の部材在庫NoX_2に設定
        setItemValue(getItemRow(itemListEx, c002subgamendata.getReturnItemIdBuzailotno2()), c002subgamendata.getSubDataBuzaitab2().get(1).getValue());
        // ｻﾌﾞ画面.調合量2の合計値は主画面の調合量X_2に設定
        setItemValue(getItemRow(itemListEx, c002subgamendata.getReturnItemIdTyougouryou2()), sumTyogouzanryou(tyougouryouListTab2));
    }

    /**
     * 主画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
     *
     * @param itemList 項目データ
     */
    public static void set102B005ItemStyle(List<FXHDD01> itemList) {
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
        if (listData == null) {
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
     * @param gxhdo102c002model モデルデータ
     * @param pot ﾎﾟｯﾄ
     * @param zairyokubun 材料区分
     * @return サブ画面データ情報
     */
    public static GXHDO102C002Model.C002SubGamenData getC002subgamendata(GXHDO102C002Model gxhdo102c002model, int pot, int zairyokubun) {
        GXHDO102C002Model.C002SubGamenData c002subgamendata = null;
        switch (pot) {
            case 1:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto1subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto1subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto1subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto1subgamen4();
                        break;
                }
                break;
            case 2:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto2subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto2subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto2subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto2subgamen4();
                        break;
                }
                break;
            case 3:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto3subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto3subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto3subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto3subgamen4();
                        break;
                }
                break;
            case 4:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto4subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto4subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto4subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto4subgamen4();
                        break;
                }
                break;
            case 5:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto5subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto5subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto5subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto5subgamen4();
                        break;
                }
                break;
            case 6:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto6subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto6subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto6subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto6subgamen4();
                        break;
                }
                break;
            case 7:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto7subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto7subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto7subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto7subgamen4();
                        break;
                }
                break;
            case 8:
                switch (zairyokubun) {
                    case 1:
                        c002subgamendata = gxhdo102c002model.getPotto8subgamen1();
                        break;
                    case 2:
                        c002subgamendata = gxhdo102c002model.getPotto8subgamen2();
                        break;
                    case 3:
                        c002subgamendata = gxhdo102c002model.getPotto8subgamen3();
                        break;
                    case 4:
                        c002subgamendata = gxhdo102c002model.getPotto8subgamen4();
                        break;
                }
                break;
            default:
                break;
        }
        return c002subgamendata;
    }
    
    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの表示用データに設定
     *
     * @param gxhdo102c002modelview モデルデータ(表示制御用)
     * @param gxhdo102c002model モデルデータ
     */
    public static void cloneViewShowsubgamendataToShow(GXHDO102C002Model gxhdo102c002modelview, GXHDO102C002Model gxhdo102c002model) {
        GXHDO102C002Model.C002SubGamenData showsubgamendata = gxhdo102c002modelview.getShowsubgamendata();
        gxhdo102c002model.setShowsubgamendata(showsubgamendata);
    }
    
    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの更新用データに設定
     *
     * @param gxhdo102c002modelview モデルデータ(表示制御用)
     * @param gxhdo102c002model モデルデータ
     */
    public static void cloneViewShowsubgamendataToPotto(GXHDO102C002Model gxhdo102c002modelview, GXHDO102C002Model gxhdo102c002model) {
        GXHDO102C002Model.C002SubGamenData showsubgamendata = gxhdo102c002modelview.getShowsubgamendata();
        Integer pot = showsubgamendata.getSubDataPot();
        Integer zairyokubun = showsubgamendata.getSubDataZairyokubun();
        switch (pot) {
            case 1:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto1subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto1subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto1subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto1subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 2:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto2subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto2subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto2subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto2subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 3:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto3subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto3subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto3subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto3subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 4:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto4subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto4subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto4subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto4subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 5:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto5subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto5subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto5subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto5subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 6:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto6subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto6subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto6subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto6subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 7:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto7subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto7subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto7subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto7subgamen4(showsubgamendata);
                        break;
                }
                break;
            case 8:
                switch (zairyokubun) {
                    case 1:
                        gxhdo102c002model.setPotto8subgamen1(showsubgamendata);
                        break;
                    case 2:
                        gxhdo102c002model.setPotto8subgamen2(showsubgamendata);
                        break;
                    case 3:
                        gxhdo102c002model.setPotto8subgamen3(showsubgamendata);
                        break;
                    case 4:
                        gxhdo102c002model.setPotto8subgamen4(showsubgamendata);
                        break;
                }
                break;
            default:
                break;
        }
    }
}
