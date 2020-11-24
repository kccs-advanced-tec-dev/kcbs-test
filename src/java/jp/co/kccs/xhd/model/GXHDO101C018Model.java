/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

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
 * GXHDO101C018Model(電気特性・熱処理_サブ画面)のモデルクラスです。
 * 
 * @author 863 zhangjy
 */
public class GXHDO101C018Model implements Cloneable {
    
    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C018Model clone() throws CloneNotSupportedException {
        
        GXHDO101C018Model cloneModel = (GXHDO101C018Model) super.clone();
        List<GoukiData> newList = new ArrayList();
        
        for (GoukiData data : this.getGouki1DataList()) {
            GoukiData newData = new GoukiData();
            newData.setItemName(data.getItemName());
            newData.setItemValue(data.getItemValue());
            newData.setCheckBoxValue(data.getCheckBoxValue());
            newData.setTabIndex(data.getTabIndex());
            newData.setTextBackColor(data.getTextBackColor());
            newList.add(newData);
        }
        
        cloneModel.setGouki1DataList(newList);
        return cloneModel;
    }
    
    /**
     * 号機①データリスト
     */
    private List<GoukiData> gouki1DataList;
    
    /**
     * 号機②データリスト
     */
    private List<GoukiData> gouki2DataList;
    
    /**
     * 号機③データリスト
     */
    private List<GoukiData> gouki3DataList;
    
    /**
     * 号機④データリスト
     */
    private List<GoukiData> gouki4DataList;

    /**
     * 号機①データリスト
     * 
     * @return the gouki1DataList
     */
    public List<GoukiData> getGouki1DataList() {
        return gouki1DataList;
    }

    /**
     * 号機①データリスト
     * 
     * @param gouki1DataList the gouki1DataList to set
     */
    public void setGouki1DataList(List<GoukiData> gouki1DataList) {
        this.gouki1DataList = gouki1DataList;
    }

    /**
     * 号機②データリスト
     * 
     * @return the gouki2DataList
     */
    public List<GoukiData> getGouki2DataList() {
        return gouki2DataList;
    }

    /**
     * 号機②データリスト
     * 
     * @param gouki2DataList the gouki2DataList to set
     */
    public void setGouki2DataList(List<GoukiData> gouki2DataList) {
        this.gouki2DataList = gouki2DataList;
    }

    /**
     * 号機③データリスト
     * 
     * @return the gouki3DataList
     */
    public List<GoukiData> getGouki3DataList() {
        return gouki3DataList;
    }

    /**
     * 号機③データリスト
     * 
     * @param gouki3DataList the gouki3DataList to set
     */
    public void setGouki3DataList(List<GoukiData> gouki3DataList) {
        this.gouki3DataList = gouki3DataList;
    }

    /**
     * 号機④データリスト
     * 
     * @return the gouki4DataList
     */
    public List<GoukiData> getGouki4DataList() {
        return gouki4DataList;
    }

    /**
     * 号機④データリスト
     * 
     * @param gouki4DataList the gouki4DataList to set
     */
    public void setGouki4DataList(List<GoukiData> gouki4DataList) {
        this.gouki4DataList = gouki4DataList;
    }
    
    /**
     * コンストラクタ
     */
    public GXHDO101C018Model() {

    }
    
    /**
     * 号機データ
     */
    public class GoukiData {

        /**
         * 種類
         */
        private String itemName;
        
        /**
         * 値
         */
        private String itemValue;
        
        /**
         * 値
         */
        private String checkBoxValue;

        /**
         * テキスト(BackGround)
         */
        private String textBackColor;

        /**
         * タブインデックス(TabIndex)
         */
        private String tabIndex;

        /**
         * 項目名
         * 
         * @return the itemName
         */
        public String getItemName() {
            return itemName;
        }

        /**
         * 項目名
         * 
         * @param itemName the itemName to set
         */
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        /**
         * 項目入力値
         * 
         * @return the itemValue
         */
        public String getItemValue() {
            return itemValue;
        }

        /**
         * 項目入力値
         * 
         * @param itemValue the itemValue to set
         */
        public void setItemValue(String itemValue) {
            this.itemValue = itemValue;
        }

        /**
         * チェックボックス項目入力値
         * 
         * @return the checkBoxValue
         */
        public String getCheckBoxValue() {
            return checkBoxValue;
        }

        /**
         * チェックボックス項目入力値
         * 
         * @param checkBoxValue the checkBoxValue to set
         */
        public void setCheckBoxValue(String checkBoxValue) {
            this.checkBoxValue = checkBoxValue;
        }
        
        /**
         * テキスト(BackGround)
         *
         * @return the textBackColor
         */
        public String getTextBackColor() {
            return textBackColor;
        }

        /**
         * テキスト(BackGround)
         *
         * @param textBackColor the textBackColor to set
         */
        public void setTextBackColor(String textBackColor) {
            this.textBackColor = textBackColor;
        }

        /**
         * タブインデックス(TabIndex)
         *
         * @return the tabIndex
         */
        public String getTabIndex() {
            return tabIndex;
        }

        /**
         * タブインデックス(TabIndex)
         *
         * @param tabIndex the tabIndex to set
         */
        public void setTabIndex(String tabIndex) {
            this.tabIndex = tabIndex;
        }
    }
}
