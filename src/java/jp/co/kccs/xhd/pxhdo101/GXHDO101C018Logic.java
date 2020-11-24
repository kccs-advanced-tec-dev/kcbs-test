/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.util.ArrayList;
import java.util.List;
import jp.co.kccs.xhd.model.GXHDO101C018Model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/11/23<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C018(電気特性・熱処理_サブ画面)ロジッククラス
 */
public class GXHDO101C018Logic {

    /**
     * 通常カラーコード(テキスト)
     */
    public static final String DEFAULT_BACK_COLOR = "#E0FFFF";

    
    /**
     * 電気特性・熱処理_サブ画面のモデルデータを作成する
     * 
     * @param initGouki1DataList 号機①タブ初期値
     * @param initGouki2DataList 号機②タブ初期値
     * @param initGouki3DataList 号機③タブ初期値
     * @param initGouki4DataList 号機④タブ初期値
     * @return モデルデータ
     */
    public static GXHDO101C018Model createGXHDO101C018Model(List<String[]> initGouki1DataList, 
            List<String[]> initGouki2DataList, List<String[]> initGouki3DataList, List<String[]> initGouki4DataList) {
        
        GXHDO101C018Model gxhdo101C018Model = new GXHDO101C018Model();
        
        // 号機①データを作成
        List<GXHDO101C018Model.GoukiData> gouki1DataList = new ArrayList<>();
        for (int i = 0; i < initGouki1DataList.get(0).length; i++) {
            gouki1DataList.add(getInitGoukiData(gxhdo101C018Model, "", i, initGouki1DataList));
        }
        gxhdo101C018Model.setGouki1DataList(gouki1DataList);
        
        // 号機②データを作成
        List<GXHDO101C018Model.GoukiData> gouki2DataList = new ArrayList<>();
        for (int i = 0; i < initGouki2DataList.get(0).length; i++) {
            gouki2DataList.add(getInitGoukiData(gxhdo101C018Model, "", i, initGouki2DataList));
        }
        gxhdo101C018Model.setGouki2DataList(gouki2DataList);
        
        // 号機③データを作成
        List<GXHDO101C018Model.GoukiData> gouki3DataList = new ArrayList<>();
        for (int i = 0; i < initGouki3DataList.get(0).length; i++) {
            gouki3DataList.add(getInitGoukiData(gxhdo101C018Model, "", i, initGouki3DataList));
        }
        gxhdo101C018Model.setGouki3DataList(gouki3DataList);
        
        // 号機④データを作成
        List<GXHDO101C018Model.GoukiData> gouki4DataList = new ArrayList<>();
        for (int i = 0; i < initGouki4DataList.get(0).length; i++) {
            gouki4DataList.add(getInitGoukiData(gxhdo101C018Model, "", i, initGouki4DataList));
        }
        gxhdo101C018Model.setGouki4DataList(gouki4DataList);
        
        return gxhdo101C018Model;
    }
    
    /**
     * 電気特性・熱処理_サブ画面データ初期化データ取得
     * 
     * @param gxhdo101C018Model 画面モデル
     * @param textBackColor BackGround
     * @param index タブインデックス
     * @param initDataDataList 初期表示データリスト
     * @return 
     */
    private static GXHDO101C018Model.GoukiData getInitGoukiData(GXHDO101C018Model gxhdo101C018Model, 
            String textBackColor, int index, List<String[]> initDataDataList) {
        
        GXHDO101C018Model.GoukiData goukiData = gxhdo101C018Model.new GoukiData();
        String[] textValue = initDataDataList.get(0);
        String[] checkboxValue = initDataDataList.get(1);
        
        String tabIndex = String.valueOf(index + 1);
        
        goukiData.setItemName("ｻﾔNo" + tabIndex);
        goukiData.setItemValue(textValue[index]);
        goukiData.setCheckBoxValue(checkboxValue[index]);
        goukiData.setTabIndex(tabIndex);
        goukiData.setTextBackColor(textBackColor);
        
        return goukiData;
    }
}
