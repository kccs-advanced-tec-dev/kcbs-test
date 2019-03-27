/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/03/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C006Model(剥離内容入力サブ画面用)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/03/05
 */
public class GXHDO101C006Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C006Model clone() throws CloneNotSupportedException {
        GXHDO101C006Model cloneModel = (GXHDO101C006Model) super.clone();
        List<GXHDO101C006Model.HakuriInputData> newList = new ArrayList();
        
        for (GXHDO101C006Model.HakuriInputData data : this.hakuriInputDataList) {
            GXHDO101C006Model.HakuriInputData newData = new GXHDO101C006Model.HakuriInputData();
            newData.setHakuriInput(data.getHakuriInput());
            newData.setSetsuuVal(data.getSetsuuVal());
            newData.setSetsuuTextRendered(data.isSetsuuTextRendered());
            newData.setSetsuuTextMaxLength(data.getSetsuuTextMaxLength());
            newData.setSetsuuTextBackColor(data.getSetsuuTextBackColor());
            newData.setSetsuuLabelRendered(data.isSetsuuLabelRendered());
            newData.setSetsuuTabIndex(data.getSetsuuTabIndex());
            newData.setBikouVal(data.getBikouVal());
            newData.setBikouTextRendered(data.isBikouTextRendered());
            newData.setBikouTextMaxLength(data.getBikouTextMaxLength());
            newData.setBikouTextBackColor(data.getBikouTextBackColor());
            newData.setBikouLabelRendered(data.isBikouLabelRendered());
            newData.setBikouTabIndex(data.getBikouTabIndex());
            newList.add(newData);
        }

        cloneModel.setHakuriInputDataList(newList);
        return cloneModel;
    }
    
    /**
     * 剥離内容入力データリスト
     */
    private List<HakuriInputData> hakuriInputDataList;

    /**
     * コンストラクタ
     */
    public GXHDO101C006Model() {
        this.hakuriInputDataList = new ArrayList<>();
    }

    /**
     * 剥離内容入力データリスト
     *
     * @return the hakuriInputDataList
     */
    public List<HakuriInputData> getHakuriInputDataList() {
        return hakuriInputDataList;
    }

    /**
     * 剥離内容入力データリスト
     *
     * @param hakuriInputDataList the ptnKyoriXDataList to set
     */
    public void setHakuriInputDataList(List<HakuriInputData> hakuriInputDataList) {
        this.hakuriInputDataList = hakuriInputDataList;
    }

    /**
     * 剥離内容入力データ
     */
    public class HakuriInputData {
        /**
         * 剥離内容入力
         */
        private String hakuriInput;

        /**
         * ｾｯﾄ数入力(値)
         */
        private String setsuuVal;

        /**
         * ｾｯﾄ数入力_テキスト(Rendered)
         */
        private boolean setsuuTextRendered;

        /**
         * ｾｯﾄ数入力_テキスト(MaxLength)
         */
        private String setsuuTextMaxLength;

        /**
         * ｾｯﾄ数入力_テキスト(BackGround)
         */
        private String setsuuTextBackColor;

        /**
         * ｾｯﾄ数入力_ラベル(Rendered)
         */
        private boolean setsuuLabelRendered;
        
        /**
         * ｾｯﾄ数入力_タブインデックス(TabIndex)
         */
        private String setsuuTabIndex;

        /**
         * 備考(値)
         */
        private String bikouVal;

        /**
         * 備考_テキスト(Rendered)
         */
        private boolean bikouTextRendered;

        /**
         * 備考_テキスト(MaxLength)
         */
        private String bikouTextMaxLength;

        /**
         * 備考_テキスト(BackGround)
         */
        private String bikouTextBackColor;

        /**
         * 備考_ラベル(Rendered)
         */
        private boolean bikouLabelRendered;
        
        /**
         * 備考_タブインデックス(TabIndex)
         */
        private String bikouTabIndex;
        
        /**
         * 剥離内容入力
         * @return the hakuriInput
         */
        public String getHakuriInput() {
            return hakuriInput;
        }

        /**
         * 剥離内容入力
         * @param hakuriInput the hakuriInput to set
         */
        public void setHakuriInput(String hakuriInput) {
            this.hakuriInput = hakuriInput;
        }

        /**
         * ｾｯﾄ数入力(値)
         * @return the setsuuVal
         */
        public String getSetsuuVal() {
            return setsuuVal;
        }

        /**
         * ｾｯﾄ数入力(値)
         * @param setsuuVal the setsuuVal to set
         */
        public void setSetsuuVal(String setsuuVal) {
            this.setsuuVal = setsuuVal;
        }

        /**
         * ｾｯﾄ数入力_テキスト(Rendered)
         * @return the setsuuTextRendered
         */
        public boolean isSetsuuTextRendered() {
            return setsuuTextRendered;
        }

        /**
         * ｾｯﾄ数入力_テキスト(Rendered)
         * @param setsuuTextRendered the setsuuTextRendered to set
         */
        public void setSetsuuTextRendered(boolean setsuuTextRendered) {
            this.setsuuTextRendered = setsuuTextRendered;
        }

        /**
         * ｾｯﾄ数入力_テキスト(MaxLength)
         * @return the setsuuTextMaxLength
         */
        public String getSetsuuTextMaxLength() {
            return setsuuTextMaxLength;
        }

        /**
         * ｾｯﾄ数入力_テキスト(MaxLength)
         * @param setsuuTextMaxLength the setsuuTextMaxLength to set
         */
        public void setSetsuuTextMaxLength(String setsuuTextMaxLength) {
            this.setsuuTextMaxLength = setsuuTextMaxLength;
        }

        /**
         * ｾｯﾄ数入力_テキスト(BackGround)
         * @return the setsuuTextBackColor
         */
        public String getSetsuuTextBackColor() {
            return setsuuTextBackColor;
        }

        /**
         * ｾｯﾄ数入力_テキスト(BackGround)
         * @param setsuuTextBackColor the setsuuTextBackColor to set
         */
        public void setSetsuuTextBackColor(String setsuuTextBackColor) {
            this.setsuuTextBackColor = setsuuTextBackColor;
        }

        /**
         * ｾｯﾄ数入力_ラベル(Rendered)
         * @return the setsuuLabelRendered
         */
        public boolean isSetsuuLabelRendered() {
            return setsuuLabelRendered;
        }

        /**
         * ｾｯﾄ数入力_ラベル(Rendered)
         * @param setsuuLabelRendered the setsuuLabelRendered to set
         */
        public void setSetsuuLabelRendered(boolean setsuuLabelRendered) {
            this.setsuuLabelRendered = setsuuLabelRendered;
        }

        /**
         * ｾｯﾄ数入力_タブインデックス(TabIndex)
         * @return the setsuuTabIndex
         */
        public String getSetsuuTabIndex() {
            return setsuuTabIndex;
        }

        /**
         * ｾｯﾄ数入力_タブインデックス(TabIndex)
         * @param setsuuTabIndex the setsuuTabIndex to set
         */
        public void setSetsuuTabIndex(String setsuuTabIndex) {
            this.setsuuTabIndex = setsuuTabIndex;
        }

        /**
         * 備考(値)
         * @return the bikouVal
         */
        public String getBikouVal() {
            return bikouVal;
        }

        /**
         * 備考(値)
         * @param bikouVal the bikouVal to set
         */
        public void setBikouVal(String bikouVal) {
            this.bikouVal = bikouVal;
        }

        /**
         * 備考_テキスト(Rendered)
         * @return the bikouTextRendered
         */
        public boolean isBikouTextRendered() {
            return bikouTextRendered;
        }

        /**
         * 備考_テキスト(Rendered)
         * @param bikouTextRendered the bikouTextRendered to set
         */
        public void setBikouTextRendered(boolean bikouTextRendered) {
            this.bikouTextRendered = bikouTextRendered;
        }

        /**
         * 備考_テキスト(MaxLength)
         * @return the bikouTextMaxLength
         */
        public String getBikouTextMaxLength() {
            return bikouTextMaxLength;
        }

        /**
         * 備考_テキスト(MaxLength)
         * @param bikouTextMaxLength the bikouTextMaxLength to set
         */
        public void setBikouTextMaxLength(String bikouTextMaxLength) {
            this.bikouTextMaxLength = bikouTextMaxLength;
        }

        /**
         * 備考_テキスト(BackGround)
         * @return the bikouTextBackColor
         */
        public String getBikouTextBackColor() {
            return bikouTextBackColor;
        }

        /**
         * 備考_テキスト(BackGround)
         * @param bikouTextBackColor the bikouTextBackColor to set
         */
        public void setBikouTextBackColor(String bikouTextBackColor) {
            this.bikouTextBackColor = bikouTextBackColor;
        }

        /**
         * 備考_ラベル(Rendered)
         * @return the bikouLabelRendered
         */
        public boolean isBikouLabelRendered() {
            return bikouLabelRendered;
        }

        /**
         * 備考_ラベル(Rendered)
         * @param bikouLabelRendered the bikouLabelRendered to set
         */
        public void setBikouLabelRendered(boolean bikouLabelRendered) {
            this.bikouLabelRendered = bikouLabelRendered;
        }

        /**
         * 備考_タブインデックス(TabIndex)
         * @return the bikouTabIndex
         */
        public String getBikouTabIndex() {
            return bikouTabIndex;
        }

        /**
         * 備考_タブインデックス(TabIndex)
         * @param bikouTabIndex the bikouTabIndex to set
         */
        public void setBikouTabIndex(String bikouTabIndex) {
            this.bikouTabIndex = bikouTabIndex;
        }
    }
}
