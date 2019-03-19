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
 * GXHDO101C009Model(合わせ(RZ)サブ画面用)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since 2019/01/08
 */
public class GXHDO101C009Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C009Model clone() throws CloneNotSupportedException {
        GXHDO101C009Model cloneModel = (GXHDO101C009Model) super.clone();
        List<GXHDO101C009Model.AwaseRzData> newList = new ArrayList();
        for (GXHDO101C009Model.AwaseRzData data : this.awaseRzDataList) {
            GXHDO101C009Model.AwaseRzData newData = new GXHDO101C009Model.AwaseRzData();
            newData.setAwaseRz(data.getAwaseRz());
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

        cloneModel.setAwaseRzDataList(newList);
        return cloneModel;
    }
    
    /**
     * 合わせ印刷サイドRZAVE設定項目ID
     */
    private String returnItemIdAve ="";


    /**
     * 合わせ(RZ)データリスト
     */
    private List<AwaseRzData> awaseRzDataList = new ArrayList<>();

    /**
     * コンストラクタ
     */
    public GXHDO101C009Model() {
        this.awaseRzDataList = new ArrayList<>();
    }

    /**
     * 合わせ印刷サイドRZAVE設定項目ID
     * @return the returnItemIdAve
     */
    public String getReturnItemIdAve() {
        return returnItemIdAve;
    }

    /**
     * 合わせ印刷サイドRZAVE設定項目ID
     * @param returnItemIdAve the returnItemIdAve to set
     */
    public void setReturnItemIdAve(String returnItemIdAve) {
        this.returnItemIdAve = returnItemIdAve;
    }

    /**
     * 合わせ(RZ)データリスト
     *
     * @return the awaseRzDataList
     */
    public List<AwaseRzData> getAwaseRzDataList() {
        return awaseRzDataList;
    }

    /**
     * 合わせ(RZ)データリスト
     *
     * @param awaseRzDataList the awaseRzDataList to set
     */
    public void setAwaseRzDataList(List<AwaseRzData> awaseRzDataList) {
        this.awaseRzDataList = awaseRzDataList;
    }

    /**
     * 合わせ(RZ)データ
     */
    public class AwaseRzData {

        /**
         * 合わせ(RZ)
         */
        private String awaseRz;

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
         * 合わせ(RZ)
         *
         * @return the awaseRz
         */
        public String getAwaseRz() {
            return awaseRz;
        }

        /**
         * 合わせ(RZ)
         *
         * @param awaseRz the awaseRz to set
         */
        public void setAwaseRz(String awaseRz) {
            this.awaseRz = awaseRz;
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
