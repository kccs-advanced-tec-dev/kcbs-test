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
import jp.co.kccs.xhd.db.model.SubSrYuudentaiSyugenryou;
import jp.co.kccs.xhd.model.GXHDO102C011Model;
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
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C011Logic(誘電体ｽﾗﾘｰ作製・主原料秤量入力)ロジッククラス
 *
 * @author KCSS wxf
 * @since 2021/11/09
 */
public class GXHDO102C011Logic implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力画面のモデルデータを作成する
     *
     * @param subSrYuudentaiSyugenryouList 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データリスト
     * @return モデルデータ
     */
    public static GXHDO102C011Model createGXHDO102C011Model(List<SubSrYuudentaiSyugenryou> subSrYuudentaiSyugenryouList) {
        GXHDO102C011Model gxhdo102C011Model = new GXHDO102C011Model();
        gxhdo102C011Model.setShowsubgamendata(gxhdo102C011Model.new SubGamenData());
        gxhdo102C011Model.setSubgamen1(gxhdo102C011Model.new SubGamenData());
        gxhdo102C011Model.setSubgamen2(gxhdo102C011Model.new SubGamenData());
        gxhdo102C011Model.setSubgamen3(gxhdo102C011Model.new SubGamenData());
        gxhdo102C011Model.setSubgamen4(gxhdo102C011Model.new SubGamenData());
        gxhdo102C011Model.setSubgamen5(gxhdo102C011Model.new SubGamenData());
        gxhdo102C011Model.setSubgamen6(gxhdo102C011Model.new SubGamenData());

        // 主原料1_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C011Model.getSubgamen1(), subSrYuudentaiSyugenryouList.get(0));
        // 主原料2_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C011Model.getSubgamen2(), subSrYuudentaiSyugenryouList.get(1));
        // 主原料3_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C011Model.getSubgamen3(), subSrYuudentaiSyugenryouList.get(2));
        // 主原料4_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C011Model.getSubgamen4(), subSrYuudentaiSyugenryouList.get(3));
        // 主原料5_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C011Model.getSubgamen5(), subSrYuudentaiSyugenryouList.get(4));
        // 主原料6_材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C011Model.getSubgamen6(), subSrYuudentaiSyugenryouList.get(5));
        return gxhdo102C011Model;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面の初期データ設定
     *
     * @param subgamenData 表示される誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面
     * @param subSrYuudentaiSyugenryouData ｻﾌﾞ画面初期データ
     */
    private static void setSubGamenInitData(GXHDO102C011Model.SubGamenData subgamendata, SubSrYuudentaiSyugenryou subSrYuudentaiSyugenryouData) {
        subgamendata.setSubDataZairyokubun(subSrYuudentaiSyugenryouData.getZairyokubun());
        // サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> subgamenDataHeaderList = getSubHeaderInfo(subSrYuudentaiSyugenryouData);
        subgamendata.setSubDataTyogouryoukikaku(subgamenDataHeaderList.get(0));
        subgamendata.setSubDataTyogouzanryou(subgamenDataHeaderList.get(1));
        // サブ画面の部材①タブデータリスト設定
        subgamendata.setSubDataBuzaitab1(getSubTab1Info(subgamendata, subSrYuudentaiSyugenryouData));
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面の調合規格と調合残量のデータ取得
     *
     * @param subSrYuudentaiSyugenryouData 誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面データ
     * @return 調合規格と調合残量のデータリスト
     */
    private static ArrayList<FXHDD01> getSubHeaderInfo(SubSrYuudentaiSyugenryou subSrYuudentaiSyugenryouData) {
        ArrayList<FXHDD01> subDataHeaderList = new ArrayList<>();
        int index = 0;
        // サブ画面の調合規格
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouryoukikaku"), true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyogouryoukikaku()), index));
        // サブ画面の調合残量
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouzanryou"), true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyogouzanryou()), index));
        return subDataHeaderList;
    }

    /**
     * サブ画面の部材①タブデータリスト取得
     *
     * @param subgamenData 表示される誘電体ｽﾗﾘｰ作製・主原料秤量入力_ｻﾌﾞ画面
     * @param subSrYuudentaiSyugenryouData ｻﾌﾞ画面初期データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTab1Info(GXHDO102C011Model.SubGamenData subgamendata, SubSrYuudentaiSyugenryou subSrYuudentaiSyugenryouData) {
        int zairyokubun = subgamendata.getSubDataZairyokubun(); // 材料区分
        int index = 1;
        // サブ画面の部材①タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab1 = new ArrayList<>();
        // サブ画面の部材①タブの材料品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getZairyohinmei()), index));
        // サブ画面の部材①タブの部材在庫No
        subDataBuzaitab1.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno"), true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getBuzailotno()), "9", "", index));
        // サブ画面の部材①タブの部材在庫品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei"), true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getBuzaihinmei()), index));
        index += 1;
        // サブ画面の部材①タブの調合量1_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "1", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou1_1()), "5", "", false, "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量1_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou1_2()), "5", "", false, "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量2_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "2", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou2_1()), "5", "", getItemDisable(zairyokubun, 2), "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量2_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou2_2()), "5", "", getItemDisable(zairyokubun, 2), "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量3_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "3", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou3_1()), "5", "", getItemDisable(zairyokubun, 3), "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量3_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou3_2()), "5", "", getItemDisable(zairyokubun, 3), "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量4_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "4", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou4_1()), "5", "", getItemDisable(zairyokubun, 4), "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量4_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou4_2()), "5", "", getItemDisable(zairyokubun, 4), "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量5_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "5", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou5_1()), "5", "", getItemDisable(zairyokubun, 5), "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量5_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou5_2()), "5", "", getItemDisable(zairyokubun, 5), "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量6_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "6", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou6_1()), "5", "", getItemDisable(zairyokubun, 6), "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量6_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou6_2()), "5", "", getItemDisable(zairyokubun, 6), "g", index, true));
        index += 1;
        // サブ画面の部材①タブの調合量7_1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou") + "7", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou7_1()), "5", "", getItemDisable(zairyokubun, 7), "g", index, false));
        index += 1;
        // サブ画面の部材①タブの調合量7_2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrYuudentaiSyugenryouData.getTyougouryou7_2()), "5", "", getItemDisable(zairyokubun, 7), "g", index, true));
        return subDataBuzaitab1;
    }

    /**
     * 調合量項目の活性フラグを取得
     *
     * @param zairyokubun 材料区分
     * @param tyougouryouNo 調合量No
     * @return 調合量項目の活性フラグ(true:非活性 false:活性)
     */
    public static boolean getItemDisable(int zairyokubun, int tyougouryouNo) {
        boolean disableVal = false;
        switch (zairyokubun) {
            case 2:
            case 3:
                if (tyougouryouNo != 1 && tyougouryouNo != 2 && tyougouryouNo != 3) {
                    disableVal = true;
                }
                break;
            case 4:
            case 5:
                if (tyougouryouNo != 1 && tyougouryouNo != 2) {
                    disableVal = true;
                }
                break;
            case 6:
                if (tyougouryouNo != 1) {
                    disableVal = true;
                }
                break;
            default:
                break;
        }
        return disableVal;
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力画面のラベル項目の属性を設定
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力画面の入力文字列項目の属性を設定
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力画面の入力数値項目の属性を設定
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
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面の調合残量の計算
     *
     * @param gxhdo102c011model モデルデータ
     */
    public static void calcTyogouzanryou(GXHDO102C011Model gxhdo102c011model) {
        GXHDO102C011Model.SubGamenData showsubgamendata = gxhdo102c011model.getShowsubgamendata();
        // ｻﾌﾞ画面の調合残量の計算
        setTyogouzanryouValue(showsubgamendata);
    }

    /**
     * サブ画面の調合残量の計算
     *
     * @param subgamendata サブ画面のデータ
     */
    private static void setTyogouzanryouValue(GXHDO102C011Model.SubGamenData subgamendata) {
        // サブ画面の調合規格
        String subDataTyogouryoukikaku = StringUtil.nullToBlank(subgamendata.getSubDataTyogouryoukikaku().getValue());
        List<String> tyougouryouList = new ArrayList<>();
        // サブ画面の調合量1_1～調合量6_2のリストを取得
        getTyogouzanryouDataList(tyougouryouList, subgamendata.getSubDataBuzaitab1());
        // サブ画面の調合残量の計算
        String tyogouzanryou = getTyogouzanryouKeisann(subDataTyogouryoukikaku, tyougouryouList);
        if (tyogouzanryou == null) {
            tyogouzanryou = "-";
        }
        subgamendata.getSubDataTyogouzanryou().setValue(tyogouzanryou);
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面の調合量X_1～調合量X_6のリストを取得
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
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(9).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(10).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(11).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(12).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(13).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(14).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(15).getValue()));
        tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(16).getValue()));
    }

    /**
     * 誘電体ｽﾗﾘｰ作製・主原料秤量入力_サブ画面の調合残量の計算
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
    public static BigDecimal sumTyogouzanryou(List<String> tyougouryouList) {
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
     * @param gxhdo102c011model 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用ﾓﾃﾞﾙ
     */
    public static void clearBackColor(GXHDO102C011Model gxhdo102c011model) {
        GXHDO102C011Model.SubGamenData showsubgamendata = gxhdo102c011model.getShowsubgamendata();
        // ｻﾌﾞ画面の背景色のクリア処理
        clearSubGamenBackColor(showsubgamendata);
    }

    /**
     * 背景色のクリア処理
     *
     * @param subgamendata サブ画面のデータ
     */
    public static void clearSubGamenBackColor(GXHDO102C011Model.SubGamenData subgamendata) {
        // サブ画面の部材①タブ
        subgamendata.getSubDataBuzaitab1().forEach((tab1ItemData) -> {
            tab1ItemData.setBackColorInput("");
        });
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gxhdo102c011model 誘電体ｽﾗﾘｰ作製・主原料秤量入力サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO102C011Model gxhdo102c011model, List<FXHDD01> itemList) {
        GXHDO102C011Model.SubGamenData showsubgamendata = gxhdo102c011model.getShowsubgamendata();
        Integer subDataZairyokubun = showsubgamendata.getSubDataZairyokubun();
        GXHDO102C011Model.SubGamenData subgamendata = getC011subgamendata(gxhdo102c011model, subDataZairyokubun);

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
    public static void setReturnDataFromSubGamen(List<FXHDD01> itemList, GXHDO102C011Model.SubGamenData subgamendata) {

        // ｻﾌﾞ画面.部材在庫Noは主画面の部材在庫NoX_1に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdBuzailotno()), subgamendata.getSubDataBuzaitab1().get(1).getValue());
        int zairyokubun = subgamendata.getSubDataZairyokubun(); // 材料区分
        // ｻﾌﾞ画面.調合量1_1～調合量1_2の合計値は主画面の調合量1に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou1()), sumTyogouzanryou(getReturnTyogouzanryouDataList(1, subgamendata.getSubDataBuzaitab1())));
        if(zairyokubun != 6){
            // ｻﾌﾞ画面.調合量2_1～調合量2_2の合計値は主画面の調合量2に設定
            setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou2()), sumTyogouzanryou(getReturnTyogouzanryouDataList(2, subgamendata.getSubDataBuzaitab1())));
            if(zairyokubun != 4 && zairyokubun != 5){
                // ｻﾌﾞ画面.調合量3_1～調合量3_2の合計値は主画面の調合量2に設定
                setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou3()), sumTyogouzanryou(getReturnTyogouzanryouDataList(3, subgamendata.getSubDataBuzaitab1())));
                if(zairyokubun != 2 && zairyokubun != 3){
                    // ｻﾌﾞ画面.調合量4_1～調合量4_2の合計値は主画面の調合量2に設定
                    setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou4()), sumTyogouzanryou(getReturnTyogouzanryouDataList(4, subgamendata.getSubDataBuzaitab1())));
                    // ｻﾌﾞ画面.調合量5_1～調合量5_2の合計値は主画面の調合量2に設定
                    setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou5()), sumTyogouzanryou(getReturnTyogouzanryouDataList(5, subgamendata.getSubDataBuzaitab1())));
                    // ｻﾌﾞ画面.調合量6_1～調合量6_2の合計値は主画面の調合量2に設定
                    setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou6()), sumTyogouzanryou(getReturnTyogouzanryouDataList(6, subgamendata.getSubDataBuzaitab1())));
                    // ｻﾌﾞ画面.調合量7_1～調合量7_2の合計値は主画面の調合量2に設定
                    setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou7()), sumTyogouzanryou(getReturnTyogouzanryouDataList(7, subgamendata.getSubDataBuzaitab1())));
                }
            }
        }
    }

    /**
     * サブ画面から調合量X_1～調合量X_2のリストを取得
     *
     * @param tyougouryouList 調合量データリスト
     * @param subDataBuzaitab サブ画面タブデータリスト
     * @return 調合量データリスト
     */
    private static List<String> getReturnTyogouzanryouDataList(int tyougouryouNo, List<FXHDD01> subDataBuzaitab) {
        List<String> tyougouryouList = new ArrayList<>();
        switch (tyougouryouNo) {
            case 1:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(3).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(4).getValue()));
                break;
            case 2:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(5).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(6).getValue()));
                break;
            case 3:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(7).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(8).getValue()));
                break;
            case 4:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(9).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(10).getValue()));
                break;
            case 5:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(11).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(12).getValue()));
                break;
            case 6:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(13).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(14).getValue()));
                break;
            case 7:
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(15).getValue()));
                tyougouryouList.add(StringUtil.nullToBlank(subDataBuzaitab.get(16).getValue()));
                break;
            default:
                break;
        }
        return tyougouryouList;
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
     * @param gxhdo102c011model モデルデータ
     * @param zairyokubun 材料区分
     * @return サブ画面データ情報
     */
    public static GXHDO102C011Model.SubGamenData getC011subgamendata(GXHDO102C011Model gxhdo102c011model, int zairyokubun) {
        GXHDO102C011Model.SubGamenData subgamendata = null;
        switch (zairyokubun) {
            case 1:
                subgamendata = gxhdo102c011model.getSubgamen1();
                break;
            case 2:
                subgamendata = gxhdo102c011model.getSubgamen2();
                break;
            case 3:
                subgamendata = gxhdo102c011model.getSubgamen3();
                break;
            case 4:
                subgamendata = gxhdo102c011model.getSubgamen4();
                break;
            case 5:
                subgamendata = gxhdo102c011model.getSubgamen5();
                break;
            case 6:
                subgamendata = gxhdo102c011model.getSubgamen6();
                break;
            default:
                break;
        }
        return subgamendata;
    }

    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの表示用データに設定
     *
     * @param gxhdo102c011modelview モデルデータ(表示制御用)
     * @param gxhdo102c011model モデルデータ
     */
    public static void setShowsubgamendataFromView(GXHDO102C011Model gxhdo102c011modelview, GXHDO102C011Model gxhdo102c011model) {
        GXHDO102C011Model.SubGamenData showsubgamendata = gxhdo102c011modelview.getShowsubgamendata();
        gxhdo102c011model.setShowsubgamendata(showsubgamendata);
    }

    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの更新用データに設定
     *
     * @param gxhdo102c011modelview モデルデータ(表示制御用)
     * @param gxhdo102c011model モデルデータ
     */
    public static void setSubgamenDataFromView(GXHDO102C011Model gxhdo102c011modelview, GXHDO102C011Model gxhdo102c011model) {
        GXHDO102C011Model.SubGamenData showsubgamendata = gxhdo102c011modelview.getShowsubgamendata();
        Integer zairyokubun = showsubgamendata.getSubDataZairyokubun();
        switch (zairyokubun) {
            case 1:
                gxhdo102c011model.setSubgamen1(showsubgamendata);
                break;
            case 2:
                gxhdo102c011model.setSubgamen2(showsubgamendata);
                break;
            case 3:
                gxhdo102c011model.setSubgamen3(showsubgamendata);
                break;
            case 4:
                gxhdo102c011model.setSubgamen4(showsubgamendata);
                break;
            case 5:
                gxhdo102c011model.setSubgamen5(showsubgamendata);
                break;
            case 6:
                gxhdo102c011model.setSubgamen6(showsubgamendata);
                break;
            default:
                break;
        }
    }
}
