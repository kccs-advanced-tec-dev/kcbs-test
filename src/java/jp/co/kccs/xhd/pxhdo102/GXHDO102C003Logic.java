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
import jp.co.kccs.xhd.db.model.SubSrGlassslurryfunsai;
import jp.co.kccs.xhd.model.GXHDO102C003Model;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2022/01/10<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102C003Logic(ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力)ロジッククラス
 * 
 * @author KCSS wxf
 * @since 2022/01/10
 */
public class GXHDO102C003Logic implements Serializable {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力画面のモデルデータを作成する
     *
     * @param subSrGlassslurryfunsaiList ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_ｻﾌﾞ画面データリスト
     * @return モデルデータ
     */
    public static GXHDO102C003Model createGXHDO102C003Model(List<SubSrGlassslurryfunsai> subSrGlassslurryfunsaiList) {

        GXHDO102C003Model gxhdo102C003Model = new GXHDO102C003Model();
        gxhdo102C003Model.setShowsubgamendata(gxhdo102C003Model.new SubGamenData());
        gxhdo102C003Model.setSubgamen1(gxhdo102C003Model.new SubGamenData());

        // 材料品名のﾘﾝｸから遷移したｻﾌﾞ画面の初期データ設定
        setSubGamenInitData(gxhdo102C003Model.getSubgamen1(), subSrGlassslurryfunsaiList.get(0));
        return gxhdo102C003Model;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_サブ画面の初期データ設定
     *
     * @param gxhdo102C003Model モデルデータ
     * @param subgamenData ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_ｻﾌﾞ画面初期データ
     */
    private static void setSubGamenInitData(GXHDO102C003Model.SubGamenData subgamendata, SubSrGlassslurryfunsai subSrGlassslurryfunsai) {
        // サブ画面の調合規格と調合残量のデータ設定
        ArrayList<FXHDD01> subgamenDataHeaderList = getSubHeaderInfo(subSrGlassslurryfunsai);
        subgamendata.setSubDataTyogouryoukikaku(subgamenDataHeaderList.get(0));
        subgamendata.setSubDataTyogouzanryou(subgamenDataHeaderList.get(1));
        // サブ画面の部材タブデータリスト設定
        subgamendata.setSubDataBuzaitab(getSubTabInfo(subSrGlassslurryfunsai));
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_サブ画面の調合規格と調合残量のデータ取得
     *
     * @param subSrGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_ｻﾌﾞ画面データ
     * @return 調合規格と調合残量のデータリスト
     */
    private static ArrayList<FXHDD01> getSubHeaderInfo(SubSrGlassslurryfunsai subSrGlassslurryfunsai) {
        ArrayList<FXHDD01> subDataHeaderList = new ArrayList<>();
        int index = 0;
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合規格
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouryoukikaku"), true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyogouryoukikaku()), index));
        // 【材料品名1】ﾘﾝｸ押下時、サブ画面の調合残量
        subDataHeaderList.add(setLabelInitInfo(true, MessageUtil.getMessage("tyogouzanryou"), true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyogouzanryou()), index));
        return subDataHeaderList;
    }

    /**
     * サブ画面の部材タブデータリスト取得
     *
     * @param subSrGlassslurryfunsai ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_ｻﾌﾞ画面データ
     * @return サブ画面のタブデータリスト
     */
    private static ArrayList<FXHDD01> getSubTabInfo(SubSrGlassslurryfunsai subSrGlassslurryfunsai) {
        int index = 1;
        // サブ画面の部材タブデータリスト
        ArrayList<FXHDD01> subDataBuzaitab1 = new ArrayList<>();
        // サブ画面の部材タブの材料品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("zairyohinmei"), true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getZairyohinmei()), index));
        // サブ画面の部材タブの部材在庫No
        subDataBuzaitab1.add(setInputTextInitInfo(MessageUtil.getMessage("buzailotno"), true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getBuzailotno()), "9", "", index));
        // サブ画面の部材タブの部材在庫品名
        subDataBuzaitab1.add(setLabelInitInfo(true, MessageUtil.getMessage("buzaihinmei"), true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getBuzaihinmei()), index));
        index += 1;
        // サブ画面の部材タブの調合量1
        subDataBuzaitab1.add(setInputNumberInitInfo(true, MessageUtil.getMessage("tyogouryou"), true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyougouryou1()), "5", "", true, "g", index, false));
        index += 1;
        // サブ画面の部材タブの調合量2
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyougouryou2()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材タブの調合量3
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyougouryou3()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材タブの調合量4
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyougouryou4()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材タブの調合量5
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyougouryou5()), "5", "", true, "g", index, true));
        index += 1;
        // サブ画面の部材タブの調合量6
        subDataBuzaitab1.add(setInputNumberInitInfo(true, "", true, StringUtil.nullToBlank(subSrGlassslurryfunsai.getTyougouryou6()), "5", "", true, "g", index, true));
        return subDataBuzaitab1;
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力画面のラベル項目の属性を設定
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力画面の入力文字列項目の属性を設定
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力画面の入力数値項目の属性を設定
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_サブ画面の調合残量の計算
     *
     * @param gxhdo102c003model モデルデータ
     */
    public static void calcTyogouzanryou(GXHDO102C003Model gxhdo102c003model) {
        GXHDO102C003Model.SubGamenData showsubgamendata = gxhdo102c003model.getShowsubgamendata();
        // ｻﾌﾞ画面の調合残量の計算
        setTyogouzanryouValue(showsubgamendata);
    }

    /**
     * サブ画面の調合残量の計算
     *
     * @param subgamendata サブ画面のデータ
     */
    private static void setTyogouzanryouValue(GXHDO102C003Model.SubGamenData subgamendata) {
        // サブ画面の調合規格
        String subDataTyogouryoukikaku = StringUtil.nullToBlank(subgamendata.getSubDataTyogouryoukikaku().getValue());
        List<String> tyougouryouList = new ArrayList<>();
        // サブ画面の調合量1～調合量6のリストを取得
        getTyogouzanryouDataList(tyougouryouList, subgamendata.getSubDataBuzaitab());
        // サブ画面の調合残量の計算
        String tyogouzanryou = getTyogouzanryouKeisann(subDataTyogouryoukikaku, tyougouryouList);
        if (tyogouzanryou == null) {
            tyogouzanryou = "-";
        }
        subgamendata.getSubDataTyogouzanryou().setValue(tyogouzanryou);
    }

    /**
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_サブ画面の調合量X_1～調合量X_6のリストを取得
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
     * ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力_サブ画面の調合残量の計算
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
     * @param gxhdo102c003model ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力サブ画面用ﾓﾃﾞﾙ
     */
    public static void clearBackColor(GXHDO102C003Model gxhdo102c003model) {
        GXHDO102C003Model.SubGamenData showsubgamendata = gxhdo102c003model.getShowsubgamendata();
        // ｻﾌﾞ画面の背景色のクリア処理
        clearSubGamenBackColor(showsubgamendata);
    }

    /**
     * 背景色のクリア処理
     *
     * @param subgamendata サブ画面のデータ
     */
    public static void clearSubGamenBackColor(GXHDO102C003Model.SubGamenData subgamendata) {
        // サブ画面の部材タブ
        subgamendata.getSubDataBuzaitab().forEach((tab1ItemData) -> {
            tab1ItemData.setBackColorInput("");
        });
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gxhdo102c003model ｶﾞﾗｽｽﾗﾘｰ作製・ﾎﾟｯﾄ粉砕入力サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO102C003Model gxhdo102c003model, List<FXHDD01> itemList) {
        GXHDO102C003Model.SubGamenData subgamendata = gxhdo102c003model.getSubgamen1();
        
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
    public static void setReturnDataFromSubGamen(List<FXHDD01> itemList, GXHDO102C003Model.SubGamenData subgamendata) {
        List<String> tyougouryouListTab = new ArrayList<>();
        // サブ画面の調合量1～調合量6のリストを取得
        getTyogouzanryouDataList(tyougouryouListTab, subgamendata.getSubDataBuzaitab());

        // ｻﾌﾞ画面.部材在庫Noは主画面の部材在庫Noに設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdBuzailotno()), subgamendata.getSubDataBuzaitab().get(1).getValue());
        // ｻﾌﾞ画面.調合量の合計値は主画面の調合量に設定
        setItemValue(getItemRow(itemList, subgamendata.getReturnItemIdTyougouryou()), sumTyogouzanryou(tyougouryouListTab));
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
     * モデルデータ(表示制御用)からデータを取得してモデルデータの表示用データに設定
     *
     * @param gxhdo102c003modelview モデルデータ(表示制御用)
     * @param gxhdo102c003model モデルデータ
     */
    public static void setShowsubgamendataFromView(GXHDO102C003Model gxhdo102c003modelview, GXHDO102C003Model gxhdo102c003model) {
        GXHDO102C003Model.SubGamenData showsubgamendata = gxhdo102c003modelview.getShowsubgamendata();
        gxhdo102c003model.setShowsubgamendata(showsubgamendata);
    }
    
    /**
     * モデルデータ(表示制御用)からデータを取得してモデルデータの更新用データに設定
     *
     * @param gxhdo102c003modelview モデルデータ(表示制御用)
     * @param gxhdo102c003model モデルデータ
     */
    public static void setSubgamenDataFromView(GXHDO102C003Model gxhdo102c003modelview, GXHDO102C003Model gxhdo102c003model) {
        GXHDO102C003Model.SubGamenData showsubgamendata = gxhdo102c003modelview.getShowsubgamendata();
        gxhdo102c003model.setSubgamen1(showsubgamendata);
    }
}
