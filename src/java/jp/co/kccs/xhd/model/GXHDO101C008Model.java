/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

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
 * GXHDO101C008Model(ﾊﾟﾀｰﾝ間距離サブ画面用)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/01/08
 */
public class GXHDO101C008Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C008Model clone() throws CloneNotSupportedException {
        GXHDO101C008Model cloneModel = (GXHDO101C008Model) super.clone();
        List<GXHDO101C008Model.PtnKanKyoriData> newList = new ArrayList();
        for (GXHDO101C008Model.PtnKanKyoriData data : this.ptnKanKyoriDataList) {
            GXHDO101C008Model.PtnKanKyoriData newData = new GXHDO101C008Model.PtnKanKyoriData();
            newData.setPtnKanKyori(data.getPtnKanKyori());
            newData.setValue(data.getValue());
            newData.setTextRendered(data.isTextRendered());
            newData.setTextMaxLength(data.getTextMaxLength());
            newData.setTextDecimalPlaces(data.getTextDecimalPlaces());
            newData.setTextMaxValue(data.getTextMaxValue());
            newData.setTextMinValue(data.getTextMinValue());
            newData.setTextBackColor(data.getTextBackColor());
            newData.setLabelRendered(data.isLabelRendered());
            newData.setTabIndex(data.getTabIndex());
            newList.add(newData);
        }

        cloneModel.setPtnKanKyoriDataList(newList);
        return cloneModel;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離データリスト
     */
    private List<PtnKanKyoriData> ptnKanKyoriDataList = new ArrayList<>();
    
    /**
     * コンストラクタ
     */
    public GXHDO101C008Model() {
        this.ptnKanKyoriDataList = new ArrayList<>();
    }

    /**
     * ﾊﾟﾀｰﾝ間距離データリスト
     *
     * @return the ptnKanKyoriDataList
     */
    public List<PtnKanKyoriData> getPtnKanKyoriDataList() {
        return ptnKanKyoriDataList;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離データリスト
     *
     * @param ptnKanKyori the ptnKanKyori to set
     */
    public void setPtnKanKyoriDataList(List<PtnKanKyoriData> ptnKanKyori) {
        this.ptnKanKyoriDataList = ptnKanKyori;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離データ
     */
    public class PtnKanKyoriData {

        /**
         * ﾊﾟﾀｰﾝ間距離
         */
        private String ptnKanKyori;

        /**
         * 値
         */
        private String value;

        /**
         * テキスト(Rendered)
         */
        private boolean textRendered;

        /**
         * テキスト(MaxLength)
         */
        private String textMaxLength;

        /**
         * テキスト(decimalPlaces)
         */
        private String textDecimalPlaces;

        /**
         * テキスト(maxValue)
         */
        private String textMaxValue;

        /**
         * テキスト(minValue)
         */
        private String textMinValue;

        /**
         * テキスト(BackGround)
         */
        private String textBackColor;

        /**
         * ラベル(Rendered)
         */
        private boolean labelRendered;
        
        /**
         * タブインデックス(TabIndex)
         */
        private String tabIndex;

        /**
         * ﾊﾟﾀｰﾝ間距離
         *
         * @return the ptnKanKyori
         */
        public String getPtnKanKyori() {
            return ptnKanKyori;
        }

        /**
         * ﾊﾟﾀｰﾝ間距離
         *
         * @param ptnKanKyori the ptnKanKyori to set
         */
        public void setPtnKanKyori(String ptnKanKyori) {
            this.ptnKanKyori = ptnKanKyori;
        }

        /**
         * 値
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * 値
         *
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * テキスト(Rendered)
         *
         * @return the textRendered
         */
        public boolean isTextRendered() {
            return textRendered;
        }

        /**
         * テキスト(Rendered)
         *
         * @param textRendered the textRendered to set
         */
        public void setTextRendered(boolean textRendered) {
            this.textRendered = textRendered;
        }

        /**
         * テキスト(MaxLength)
         *
         * @return the textMaxLength
         */
        public String getTextMaxLength() {
            return textMaxLength;
        }

        /**
         * テキスト(MaxLength)
         *
         * @param textMaxLength the textMaxLength to set
         */
        public void setTextMaxLength(String textMaxLength) {
            this.textMaxLength = textMaxLength;
        }
        
        /**
         * テキスト(decimalPlaces)
         * 
         * @return the textDecimalPlaces
         */
        public String getTextDecimalPlaces() {
            return textDecimalPlaces;
        }

        /**
         * テキスト(decimalPlaces)
         * 
         * @param textDecimalPlaces the textDecimalPlaces to set
         */
        public void setTextDecimalPlaces(String textDecimalPlaces) {
            this.textDecimalPlaces = textDecimalPlaces;
        }

        /**
         * テキスト(maxValue)
         * 
         * @return the textMaxValue
         */
        public String getTextMaxValue() {
            return textMaxValue;
        }

        /**
         * テキスト(maxValue)
         * 
         * @param textMaxValue the textMaxValue to set
         */
        public void setTextMaxValue(String textMaxValue) {
            this.textMaxValue = textMaxValue;
        }

        /**
         * テキスト(minValue)
         * 
         * @return the textMinValue
         */
        public String getTextMinValue() {
            return textMinValue;
        }

        /**
         * テキスト(minValue)
         * 
         * @param textMinValue the textMinValue to set
         */
        public void setTextMinValue(String textMinValue) {
            this.textMinValue = textMinValue;
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
         * ラベル(Rendered)
         *
         * @return the labelRendered
         */
        public boolean isLabelRendered() {
            return labelRendered;
        }

        /**
         * ラベル(Rendered)
         *
         * @param labelRendered the labelRendered to set
         */
        public void setLabelRendered(boolean labelRendered) {
            this.labelRendered = labelRendered;
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
