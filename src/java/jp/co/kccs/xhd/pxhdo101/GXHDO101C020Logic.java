/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.model.GXHDO101C020Model;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.primefaces.context.RequestContext;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/10/14<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C020(前工程WIP取込)ロジッククラス
 */
public class GXHDO101C020Logic {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    private static GXHDO101C020Model model;
    private static List<FXHDD01> item_List;

    /**
     * DataSource(qcdb)
     */
    @Resource(mappedName = "jdbc/qcdb")
    private transient DataSource dataSourceQcdb;

    /**
     * 前工程WIP取込画面のモデルデータを作成する
     * @param initData 前工程WIP取込画面初期値
     * @param gamenID 画面ID
     * @return モデルデータ
     */
    public static GXHDO101C020Model createGXHDO101C020Model(List<Map<String, Object>> initData, String gamenID) {
        GXHDO101C020Model gxhdo101C020Model = new GXHDO101C020Model();
        String[] typeName = setShowItem(gamenID);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = new ArrayList<>();
        String tabIndex;
        // 画面内のリストの一覧を作成する。
        for (int i = 0; i < typeName.length; i++) {
            tabIndex = String.valueOf(i + 1);
            genryouLotDataList.add(getInitGenryouLotData(gxhdo101C020Model, typeName[i], "TEXT", "6", "", tabIndex, initData));
        }

        gxhdo101C020Model.setGenryouLotDataList(genryouLotDataList);
        
        return gxhdo101C020Model;
    }
    
    /**
     * 前工程WIP取込データ初期化データ取得
     *
     * @param gxhdo101C020Model 画面モデル
     * @param value 値
     * @param inputType　入力タイプ
     * @param textMaxLength　テキストMaxLength
     * @param textBackColor　BackGround
     * @param tabIndex　タブインデックス
     * @return 前工程WIP取込データ
     */
    private static GXHDO101C020Model.GenryouLotData getInitGenryouLotData(
            GXHDO101C020Model gxhdo101C020Model, String typeName, String inputType, 
            String textMaxLength, String textBackColor, String tabIndex, List<Map<String, Object>> initData) {
        GXHDO101C020Model.GenryouLotData genryouLotData = gxhdo101C020Model.new GenryouLotData();
        String value = "";
        if (initData != null) {
            for (Map<String, Object> valueMap : initData) {
                String mkojyo = StringUtil.nullToBlank(valueMap.get("mkojyo"));
                String mlotno = StringUtil.nullToBlank(valueMap.get("mlotno"));
                String medaban = StringUtil.nullToBlank(valueMap.get("medaban"));
                String mkubun = StringUtil.nullToBlank(valueMap.get("mkubun"));
                String mkubunno = StringUtil.nullToBlank(valueMap.get("mkubunno"));
                
                if ((GXHDO101C020Model.TAPE_LOT_1.equals(typeName) && "電極ﾃｰﾌﾟ".equals(mkubun) && "1".equals(mkubunno))
                        || (GXHDO101C020Model.TAPE_LOT_2.equals(typeName) && "電極ﾃｰﾌﾟ".equals(mkubun) && "2".equals(mkubunno))
                        || (GXHDO101C020Model.TAPE_LOT_3.equals(typeName) && "電極ﾃｰﾌﾟ".equals(mkubun) && "3".equals(mkubunno))
                        || (GXHDO101C020Model.PASTE_LOT_1.equals(typeName) && "内部電極ﾍﾟｰｽﾄ".equals(mkubun) && "1".equals(mkubunno))
                        || (GXHDO101C020Model.PASTE_LOT_2.equals(typeName) && "内部電極ﾍﾟｰｽﾄ".equals(mkubun) && "2".equals(mkubunno))
                        || (GXHDO101C020Model.PASTE_LOT_3.equals(typeName) && "内部電極ﾍﾟｰｽﾄ".equals(mkubun) && "3".equals(mkubunno))
                        || (GXHDO101C020Model.PASTE_LOT_4.equals(typeName) && "内部電極ﾍﾟｰｽﾄ".equals(mkubun) && "4".equals(mkubunno))
                        || (GXHDO101C020Model.PASTE_LOT_5.equals(typeName) && "内部電極ﾍﾟｰｽﾄ".equals(mkubun) && "5".equals(mkubunno))
                        || (GXHDO101C020Model.YUDENTAI_PASTE_1.equals(typeName) && "誘電体ﾍﾟｰｽﾄ".equals(mkubun) && "1".equals(mkubunno))
                        || (GXHDO101C020Model.YUDENTAI_PASTE_2.equals(typeName) && "誘電体ﾍﾟｰｽﾄ".equals(mkubun) && "2".equals(mkubunno))
                        || (GXHDO101C020Model.UWA_TANSHI.equals(typeName) && "上端子ﾃｰﾌﾟ".equals(mkubun) && "1".equals(mkubunno))
                        || (GXHDO101C020Model.SHITA_TANSHI.equals(typeName) && "下端子ﾃｰﾌﾟ".equals(mkubun) && "1".equals(mkubunno))) {
                    value = mkojyo + mlotno + medaban;
                    break;
                }
            }
        }
        genryouLotData.setTypeName(typeName);

        genryouLotData.setValue(value);
        if ("TEXT".equals(inputType)) {
            genryouLotData.setTextRendered(true);
            genryouLotData.setLabelRendered(false);
        } else {
            genryouLotData.setTextRendered(false);
            genryouLotData.setLabelRendered(true);
        }
        genryouLotData.setTextMaxLength(textMaxLength);
        genryouLotData.setTextBackColor(textBackColor);
        genryouLotData.setTabIndex(tabIndex);

        return genryouLotData;
    }
    
    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     *
     * @param gXHDO101C020Model 前工程WIP取込サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     */
    public static void setReturnData(GXHDO101C020Model gXHDO101C020Model, List<FXHDD01> itemList) {
        
        model = gXHDO101C020Model;
        item_List = itemList;
        
        setReturnDataChild(gXHDO101C020Model, itemList, true, true, true, true);
    }
    
    /**
     * 項目データ取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 項目データ
     */
    private static FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        if (StringUtil.isEmpty(itemId)) {
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
     * 対象項目に値をセットする
     *
     * @param itemData 項目
     * @param value 値
     */
    private static void setItemValue(FXHDD01 itemData, String value) {
        if (itemData == null) {
            return;
        }
        if (!StringUtil.isEmpty(value)) {
            // 値をセット
            itemData.setValue(value);
        } else {
            // 値をセット
            itemData.setValue("");
        }
    }
    
    /**
     * 電極ﾍﾟｰｽﾄﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public static void notCheckTapeSameHinmei() {
        setReturnDataChild(model, item_List, false, true, true, true);
    }
    
    /**
     * 電極ﾃｰﾌﾟﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public static void notCheckPasteSameHinmei() {
        setReturnDataChild(model, item_List, false, false, true, true);
    }

    /**
     * 上端子ﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public static void notCheckUwaTanshiSameHinmei() {
        setReturnDataChild(model, item_List, false, false, false, true);
    }

    /**
     * 下端子ﾃｰﾌﾟﾁｪｯｸ警告メッセージokボタン押下後の処理
     */
    public static void notCheckShitaTanshiSameHinmei() {
        setReturnDataChild(model, item_List, false, false, false, false);
    }

    /**
     * サブ画面からの戻り値をメイン画面の項目リストにセットする
     * 
     * @param gXHDO101C020Model 前工程WIP取込サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     * @param isCheckTape 電極ﾃｰﾌﾟﾁｪｯｸか
     * @param isCheckPaste 電極ﾍﾟｰｽﾄﾁｪｯｸか
     */
    private static void setReturnDataChild(GXHDO101C020Model gXHDO101C020Model, List<FXHDD01> itemList, boolean isCheckTape, boolean isCheckPaste, boolean isCheckUwaT, boolean isCheckShitaT) {
        
        if (isCheckTape && !checkTapeSameHinmei(gXHDO101C020Model, itemList)) {
            // エラーの場合はコールバック変数に"warning"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("param1", "warning");
            return;
        }
        if (isCheckPaste && !checkPasteSameHinmei(gXHDO101C020Model, itemList)) {
            // エラーの場合はコールバック変数に"warning"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("param2", "warning");
            return;
        }
        if (isCheckUwaT && !checkTanshiSameHinmei(gXHDO101C020Model, itemList, "T")) {
            // エラーの場合はコールバック変数に"warning"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("param3", "warning");
            return;
        }
        if (isCheckShitaT && !checkTanshiSameHinmei(gXHDO101C020Model, itemList, "B")) {
            // エラーの場合はコールバック変数に"warning"をセット
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("param4", "warning");
            return;
        }
        // ﾃｰﾌﾟﾛｯﾄ①の戻り項目
        FXHDD01 itemTapeLot1Hinmei = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_Hinmei());
        FXHDD01 itemTapeLot1Conventionallot = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_Conventionallot());
        FXHDD01 itemTapeLot1Lotkigo = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_Lotkigo());
        FXHDD01 itemTapeLot1Rollno = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_Rollno());
        FXHDD01 itemTapeLot1Length = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_Tapelength());
        FXHDD01 itemTapeLot1Tapeatu = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_thickness_um());
        
        // ﾃｰﾌﾟﾛｯﾄ②の戻り項目
        FXHDD01 itemTapeLot2Rollno = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot2_Rollno());
        FXHDD01 itemTapeLot2Length = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot2_Tapelength());        
        // ﾃｰﾌﾟﾛｯﾄ③の戻り項目
        FXHDD01 itemTapeLot3Rollno = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot3_Rollno());
        FXHDD01 itemTapeLot3Length = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot3_Tapelength());        
        
        // ﾍﾟｰｽﾄﾛｯﾄ①の戻り項目
        FXHDD01 itemPasteLot1Hinmei = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_PasteLot1_Hinmei());
        FXHDD01 itemPasteLot1Conventionallot = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_PasteLot1_Conventionallot());
        FXHDD01 itemPasteLot1kokeibunpct = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_PasteLot1_Kokeibunpct());
        
        // ﾍﾟｰｽﾄﾛｯﾄ②の戻り項目
        FXHDD01 itemPasteLot2Conventionallot = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_PasteLot2_Conventionallot());
        FXHDD01 itemPasteLot2Kokeibunpct = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_PasteLot2_Kokeibunpct());
        
        // 上端子の戻り項目
        FXHDD01 itemUwatanshiConventionallot = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Uwatanshi_Conventionallot());
        FXHDD01 itemUwatanshiRollno = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Uwatanshi_Rollno());
        
        // 下端子の戻り項目
        FXHDD01 itemShitatanshiConventionallot = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Shitatanshi_Conventionallot());
        FXHDD01 itemShitatanshiRollno = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Shitatanshi_Rollno());
        
        // petname
        FXHDD01 itemPetname = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Petname());

        // 下端子の戻り項目
        FXHDD01 itemYudentai1Conventionallot = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Yudentai1_Conventionallot());
        FXHDD01 itemYudentai1Hinmei = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_Yudentai1_Hinmei());

        
        Map<String, Map<String, String>> resultMaps = gXHDO101C020Model.getResultMap();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : gXHDO101C020Model.getGenryouLotDataList()) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            Map<String, String> resultMap = resultMaps.get(value);
            if (resultMap == null) {
                continue;
            }
            
            switch(typeName) {
                
                case GXHDO101C020Model.TAPE_LOT_1:
                    //setItemTapeLot1Hinmei(itemTapeLot1Hinmei, resultMap.get("hinmei"));
                    setItemValue(itemTapeLot1Conventionallot, resultMap.get("conventionallot"));
                    setItemValue(itemTapeLot1Lotkigo, resultMap.get("lotkigo"));
                    setItemValue(itemTapeLot1Rollno, resultMap.get("rollno"));
                    setItemValue(itemPetname, resultMap.get("petname"));
                    setItemValue(itemTapeLot1Length, resultMap.get("tapelength_m"));
                    setItemValue(itemTapeLot1Tapeatu, resultMap.get("thickness_um"));
                    break;
                case GXHDO101C020Model.TAPE_LOT_2:
                    setItemValue(itemTapeLot2Rollno, resultMap.get("rollno"));
                    setItemValue(itemTapeLot2Length, resultMap.get("tapelength_m"));
                    break;
                case GXHDO101C020Model.TAPE_LOT_3:
                    setItemValue(itemTapeLot3Rollno, resultMap.get("rollno"));
                    setItemValue(itemTapeLot3Length, resultMap.get("tapelength_m"));
                    break;
                case GXHDO101C020Model.PASTE_LOT_1:
                    setItemValue(itemPasteLot1Hinmei, resultMap.get("hinmei"));
                    setItemValue(itemPasteLot1Conventionallot, resultMap.get("conventionallot") + resultMap.get("lotkigo"));
                    setItemValue(itemPasteLot1kokeibunpct, resultMap.get("kokeibun_pct"));
                    break;
                case GXHDO101C020Model.PASTE_LOT_2:
                    setItemValue(itemPasteLot2Conventionallot, resultMap.get("conventionallot") + resultMap.get("lotkigo"));
                    setItemValue(itemPasteLot2Kokeibunpct, resultMap.get("kokeibun_pct"));
                    break;
                case GXHDO101C020Model.UWA_TANSHI:
                    setItemValue(itemUwatanshiConventionallot, resultMap.get("conventionallot"));
                    setItemValue(itemUwatanshiRollno, resultMap.get("rollno"));
                    break;
                case GXHDO101C020Model.SHITA_TANSHI:
                    setItemValue(itemShitatanshiConventionallot, resultMap.get("conventionallot"));
                    setItemValue(itemShitatanshiRollno, resultMap.get("rollno"));
                    break;
                case GXHDO101C020Model.YUDENTAI_PASTE_1:
                    setItemValue(itemYudentai1Conventionallot, resultMap.get("conventionallot"));
                    setItemValue(itemYudentai1Hinmei, resultMap.get("hinmei"));
                    break;
            }
        }
    }

    /**
     * 元画面の電極ﾃｰﾌﾟとﾃｰﾌﾟﾛｯﾄ①のhinmeiが一致かのチェック
     * 
     * @param gXHDO101C020Model 前工程WIP取込サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     * @return 
     */
    private static boolean checkTapeSameHinmei(GXHDO101C020Model gXHDO101C020Model, List<FXHDD01> itemList) {
        Map<String, Map<String, String>> resultMaps = gXHDO101C020Model.getResultMap();
        FXHDD01 itemRow = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_TapeLot1_Hinmei());
        if (itemRow == null) {
            return true;
        }
        String mValue = StringUtil.nullToBlank(itemRow.getValue());
        String[] splitVal = mValue.split("  ", 2);
        if (splitVal.length < 2) {
            return true;
        }
        String[] splitEaVal = null;
        String[] splitSekiVal = null;
        String[] splitSpVal = null;
        if ("GXHDO101B003".equals(itemRow.getGamenId())) {
            // 設計ﾃﾞｰﾀの電極ﾃｰﾌﾟを取得する
            FXHDD01 itemTape = getItemRow(itemList, "insatsu_rsus_denkyoku_tape");
            if (itemTape == null) {
                return true;
            }
            String eaTapeValue = itemTape.getValue();
            splitEaVal = eaTapeValue.split("  ", 3);
            if (splitEaVal.length < 3) {
                return true;
            }
            splitSpVal = splitEaVal[1].split("-", 2);
            if (splitSpVal.length < 2) {
                return true;
            }
            // 設計ﾃﾞｰﾀの積層数を取得する
            FXHDD01 itemSeki = getItemRow(itemList, "insatsu_rsus_sekisou_su");
            if (itemSeki == null) {
                return true;
            }
            String sekiValue = itemSeki.getValue();
            splitSekiVal = sekiValue.split("μm", 2);
            if (splitSekiVal.length < 2) {
                return true;
            }
        }
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gXHDO101C020Model.getGenryouLotDataList();
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            Map<String, String> resultMap = resultMaps.get(value);
            if (resultMap == null) {
                continue;
            }

            // 印刷RSUSの場合のﾁｪｯｸ
            if ("GXHDO101B003".equals(itemRow.getGamenId())) {
                if (GXHDO101C020Model.TAPE_LOT_1.equals(typeName)) {
                    if (!splitEaVal[0].equals(StringUtil.nullToBlank(resultMap.get("hinmei"))) ||
                            !splitEaVal[2].equals(StringUtil.nullToBlank(resultMap.get("lotkigo"))) ||
                            !splitSpVal[0].equals(StringUtil.nullToBlank(resultMap.get("conventionallot"))) ||
                            !splitSpVal[1].equals(StringUtil.nullToBlank(resultMap.get("rollno"))) ||
                            !splitSekiVal[0].equals(StringUtil.nullToBlank(resultMap.get("thickness_um")))) {
                        return false;                        
                    }
                }
            } else {
                // 元画面の電極ﾃｰﾌﾟの原料部分とﾃｰﾌﾟﾛｯﾄ①のhinmeiが一致していること
                if (GXHDO101C020Model.TAPE_LOT_1.equals(typeName) && !splitVal[1].equals(StringUtil.nullToBlank(resultMap.get("hinmei")))) {
                    return false;
                }
            }
            
        }
        return true;
    }
    
    /**
     * 元画面の電極ﾍﾟｰｽﾄの規格値とﾍﾟｰｽﾄﾛｯﾄ①のhinmeiが一致かのチェック
     * 
     * @param gXHDO101C020Model 前工程WIP取込サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     * @return 
     */
    private static boolean checkPasteSameHinmei(GXHDO101C020Model gXHDO101C020Model, List<FXHDD01> itemList) {
        Map<String, Map<String, String>> resultMaps = gXHDO101C020Model.getResultMap();
        FXHDD01 itemRow = getItemRow(itemList, gXHDO101C020Model.getReturnItemId_PasteLot1_Hinmei());
        if (itemRow == null) {
            return true;
        }
        String mKikakuChi = StringUtil.nullToBlank(itemRow.getKikakuChi());
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gXHDO101C020Model.getGenryouLotDataList();
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            Map<String, String> resultMap = resultMaps.get(value);
            if (resultMap == null) {
                continue;
            }
            mKikakuChi = mKikakuChi.replace("【", "").replace("】", "");
            // 元画面の電極ﾍﾟｰｽﾄの規格値とﾍﾟｰｽﾄﾛｯﾄ①のhinmeiが一致していること
            if (GXHDO101C020Model.PASTE_LOT_1.equals(typeName) && !mKikakuChi.equals(StringUtil.nullToBlank(resultMap.get("hinmei")))) {
                return false;
            }
        }
        return true;
    }

    /**
     * サブ画面の表示項目を取得する
     * 
     * @param gamenID 前画面ID
     * @return サブ画面の表示項目
     */
    private static String[] setShowItem(String gamenID) {
        String[] itemName = null;
        switch (gamenID) {
            case "GXHDO101B001":
                itemName = new String[8];
                itemName[0] = GXHDO101C020Model.TAPE_LOT_1;
                itemName[1] = GXHDO101C020Model.TAPE_LOT_2;
                itemName[2] = GXHDO101C020Model.TAPE_LOT_3;
                itemName[3] = GXHDO101C020Model.PASTE_LOT_1;
                itemName[4] = GXHDO101C020Model.PASTE_LOT_2;
                itemName[5] = GXHDO101C020Model.PASTE_LOT_3;
                itemName[6] = GXHDO101C020Model.PASTE_LOT_4;
                itemName[7] = GXHDO101C020Model.PASTE_LOT_5;
                break;
            case "GXHDO101B002":
                itemName = new String[8];
                itemName[0] = GXHDO101C020Model.TAPE_LOT_1;
                itemName[1] = GXHDO101C020Model.TAPE_LOT_2;
                itemName[2] = GXHDO101C020Model.TAPE_LOT_3;
                itemName[3] = GXHDO101C020Model.PASTE_LOT_1;
                itemName[4] = GXHDO101C020Model.PASTE_LOT_2;
                itemName[5] = GXHDO101C020Model.PASTE_LOT_3;
                itemName[6] = GXHDO101C020Model.PASTE_LOT_4;
                itemName[7] = GXHDO101C020Model.PASTE_LOT_5;
                break;
            case "GXHDO101B023":
                itemName = new String[8];
                itemName[0] = GXHDO101C020Model.TAPE_LOT_1;
                itemName[1] = GXHDO101C020Model.TAPE_LOT_2;
                itemName[2] = GXHDO101C020Model.TAPE_LOT_3;
                itemName[3] = GXHDO101C020Model.PASTE_LOT_1;
                itemName[4] = GXHDO101C020Model.PASTE_LOT_2;
                itemName[5] = GXHDO101C020Model.PASTE_LOT_3;
                itemName[6] = GXHDO101C020Model.PASTE_LOT_4;
                itemName[7] = GXHDO101C020Model.PASTE_LOT_5;
                break;
            case "GXHDO101B003":
                itemName = new String[3];
                itemName[0] = GXHDO101C020Model.TAPE_LOT_1;
                itemName[1] = GXHDO101C020Model.PASTE_LOT_1;
                itemName[2] = GXHDO101C020Model.PASTE_LOT_2;
                break;
            case "GXHDO101B004":
            case "GXHDO101B005":
                itemName = new String[2];
                itemName[0] = GXHDO101C020Model.UWA_TANSHI;
                itemName[1] = GXHDO101C020Model.SHITA_TANSHI;
                break;
            case "GXHDO101B006":
                itemName = new String[8];
                itemName[0] = GXHDO101C020Model.TAPE_LOT_1;
                itemName[1] = GXHDO101C020Model.TAPE_LOT_2;
                itemName[2] = GXHDO101C020Model.PASTE_LOT_1;
                itemName[3] = GXHDO101C020Model.PASTE_LOT_2;
                itemName[4] = GXHDO101C020Model.YUDENTAI_PASTE_1;
                itemName[5] = GXHDO101C020Model.YUDENTAI_PASTE_2;
                itemName[6] = GXHDO101C020Model.UWA_TANSHI;
                itemName[7] = GXHDO101C020Model.SHITA_TANSHI;
                break;
            case "GXHDO101B045":
                itemName = new String[2];
                itemName[0] = GXHDO101C020Model.TAPE_LOT_1;
                itemName[1] = GXHDO101C020Model.PASTE_LOT_1;
                break;
        }
        return itemName;
    }

    /**
     * 電極ﾃｰﾌﾟを戻る
     * 
     * @param itemTapeLot1Hinmei ﾃｰﾌﾟﾛｯﾄ①のHinmei戻り項目
     * @param hinmei 品名
     */
    private static void setItemTapeLot1Hinmei(FXHDD01 itemTapeLot1Hinmei, String hinmei) {
        if (itemTapeLot1Hinmei == null) {
            return;
        }
        String mValue = StringUtil.nullToBlank(itemTapeLot1Hinmei.getValue());
        String[] splitVal = mValue.split("  ", 2);
        hinmei = splitVal[0] + "  " + hinmei;
        itemTapeLot1Hinmei.setValue(hinmei);
    }
    
    /**
     * 元画面の電極ﾃｰﾌﾟとﾃｰﾌﾟﾛｯﾄ①のhinmeiが一致かのチェック
     * 
     * @param gXHDO101C020Model 前工程WIP取込サブ画面用ﾓﾃﾞﾙ
     * @param itemList 項目リスト
     * @return 
     */
    private static boolean checkTanshiSameHinmei(GXHDO101C020Model gXHDO101C020Model, List<FXHDD01> itemList, String tanshi) {
        Map<String, Map<String, String>> resultMaps = gXHDO101C020Model.getResultMap();
        // 設計ﾃﾞｰﾀの電極ﾃｰﾌﾟを取得する
        FXHDD01 itemShitaTape = getItemRow(itemList, "sr_rsussek_shita_cover_tape1");
        if (itemShitaTape == null) {
            return true;
        }
        String shitaValue = itemShitaTape.getValue();
        String[] splitShitaVal = shitaValue.split("  ", 3);
        if (splitShitaVal.length < 3) {
            return true;
        }
        String[] splitShitaAVal = splitShitaVal[2].split("μm", 2);
        if (splitShitaAVal.length < 2) {
            return true;
        }
        FXHDD01 itemUwaTape = getItemRow(itemList, "sr_rsussek_ue_cover_tape1");
        if (itemUwaTape == null) {
            return true;
        }
        String uwaValue = itemUwaTape.getValue();
        String[] splitUwaVal = uwaValue.split("  ", 3);
        if (splitUwaVal.length < 3) {
            return true;
        }
        String[] splitUwaAVal = splitUwaVal[2].split("μm", 2);
        if (splitUwaAVal.length < 2) {
            return true;
        }
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gXHDO101C020Model.getGenryouLotDataList();
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            Map<String, String> resultMap = resultMaps.get(value);
            if (resultMap == null) {
                continue;
            }

            // 印刷RSUSの場合のﾁｪｯｸ
            if ("GXHDO101B005".equals(itemShitaTape.getGamenId())) {
                if (GXHDO101C020Model.UWA_TANSHI.equals(typeName) && tanshi.equals("T")) {
                    if (!splitUwaVal[0].equals(StringUtil.nullToBlank(resultMap.get("hinmei"))) ||
                            !splitUwaAVal[0].equals(StringUtil.nullToBlank(resultMap.get("thickness_um")))) {
                        return false;                        
                    }
                } else if (GXHDO101C020Model.SHITA_TANSHI.equals(typeName) && tanshi.equals("B")) {
                    if (!splitShitaVal[0].equals(StringUtil.nullToBlank(resultMap.get("hinmei"))) ||
                            !splitShitaAVal[0].equals(StringUtil.nullToBlank(resultMap.get("thickness_um")))) {
                        return false;                        
                    }
                }
            }
            
        }
        return true;
    }
    
    /**
     * 印刷SPS/ｸﾞﾗﾋﾞｱ必須ﾁｪｯｸ
     * 
     * @param gXHDO101C020Model 各工程の必須ﾁｪｯｸ
     * @return List<String> ｴﾗｰﾒｯｾｰｼﾞ
     */
    public static List<String> checkLotSps(GXHDO101C020Model gXHDO101C020Model) {
        
        List<String> errorList = new ArrayList<>();
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gXHDO101C020Model.getGenryouLotDataList();
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            if (GXHDO101C020Model.TAPE_LOT_1.equals(typeName)) {
                if ("".equals(value)) {
                    genryouLotData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "前工程WIP" ));
                    return errorList;
                }
            } else if (GXHDO101C020Model.PASTE_LOT_1.equals(typeName)) {
                if ("".equals(value)) {
                    genryouLotData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "前工程WIP"));
                    return errorList;
                }
            }            
        }
        
        return errorList;
    }

    /**
     * 積層SPS必須ﾁｪｯｸ
     * 
     * @param gXHDO101C020Model 各工程の必須ﾁｪｯｸ
     * @return List<String> ｴﾗｰﾒｯｾｰｼﾞ
     */
    public static List<String> checkLotSekiSps(GXHDO101C020Model gXHDO101C020Model) {
        
        List<String> errorList = new ArrayList<>();
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = gXHDO101C020Model.getGenryouLotDataList();
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            String typeName = genryouLotData.getTypeName();
            String value = genryouLotData.getValue();
            if (GXHDO101C020Model.UWA_TANSHI.equals(typeName)) {
                if ("".equals(value)) {
                    genryouLotData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "前工程WIP" ));
                    return errorList;
                }
            } else if (GXHDO101C020Model.SHITA_TANSHI.equals(typeName)) {
                if ("".equals(value)) {
                    genryouLotData.setTextBackColor(ErrUtil.ERR_BACK_COLOR);
                    errorList.add(MessageUtil.getMessage("XHD-000037", "前工程WIP"));
                    return errorList;
                }
            }            
        }
        
        return errorList;
    }

}
