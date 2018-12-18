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
 * 変更日	2018/12/04<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C003Model(PTN距離Yサブ画面用)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
public class GXHDO101C003Model implements Cloneable{

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C003Model clone() throws CloneNotSupportedException {
        GXHDO101C003Model cloneModel = (GXHDO101C003Model) super.clone();
        List<GXHDO101C003Model.PtnKyoriYData> newList = new ArrayList();
        for (GXHDO101C003Model.PtnKyoriYData data : this.ptnKyoriYDataList) {
            GXHDO101C003Model.PtnKyoriYData newData = new GXHDO101C003Model.PtnKyoriYData();
            newData.setPtnKyoriY(data.ptnKyoriY);
            newData.setStartVal(data.getStartVal());
            newData.setStartTextRendered(data.isStartTextRendered());
            newData.setStartTextMaxLength(data.getStartTextMaxLength());
            newData.setStartTextBackColor(data.getStartTextBackColor());
            newData.setStartLabelRendered(data.isStartLabelRendered());
            newData.setEndVal(data.getEndVal());
            newData.setEndTextRendered(data.isEndTextRendered());
            newData.setEndTextMaxLength(data.getEndTextMaxLength());
            newData.setEndTextBackColor(data.getEndTextBackColor());
            newData.setEndLabelRendered(data.isEndLabelRendered());
            newList.add(newData);
        }

        cloneModel.setPtnKyoriYDataList(newList);
        return cloneModel;
    }
    /**
     * PTN距離Yデータリスト
     */
    private List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList;

        /**
     * コンストラクタ
     */
    public GXHDO101C003Model() {
        this.ptnKyoriYDataList = new ArrayList<>();
    }

    
    /**
     * PTN距離Yデータリスト
     *
     * @return the ptnKyoriYDataList
     */
    public List<GXHDO101C003Model.PtnKyoriYData> getPtnKyoriYDataList() {
        return ptnKyoriYDataList;
    }

    /**
     * PTN距離Yデータリスト
     *
     * @param ptnKyoriYDataList the ptnKyoriYDataList to set
     */
    public void setPtnKyoriYDataList(List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList) {
        this.ptnKyoriYDataList = ptnKyoriYDataList;
    }

    /**
     * PTN距離Yデータ
     */
    public class PtnKyoriYData {

        /**
         * PTN距離Y
         */
        private String ptnKyoriY;
        /**
         * スタート項目(値)
         */
        private String startVal;

        /**
         * スタート項目_テキスト(Rendered)
         */
        private boolean startTextRendered;

        /**
         * スタート項目_テキスト(MaxLength)
         */
        private String startTextMaxLength;

        /**
         * スタート項目_テキスト(BackGround)
         */
        private String startTextBackColor;

        /**
         * スタート項目_ラベル(Rendered)
         */
        private boolean startLabelRendered;

        /**
         * エンド項目(値)
         */
        private String endVal;

        /**
         * エンド項目_テキスト(Rendered)
         */
        private boolean endTextRendered;

        /**
         * エンド項目_テキスト(MaxLength)
         */
        private String endTextMaxLength;

        /**
         * エンド項目_テキスト(BackGround)
         */
        private String endTextBackColor;

        /**
         * エンド項目_ラベル(Rendered)
         */
        private boolean endLabelRendered;

        /**
         * PTN距離Y
         *
         * @return the ptnKyoriY
         */
        public String getPtnKyoriY() {
            return ptnKyoriY;
        }

        /**
         * PTN距離Y
         *
         * @param ptnKyoriY the ptnKyoriY to set
         */
        public void setPtnKyoriY(String ptnKyoriY) {
            this.ptnKyoriY = ptnKyoriY;
        }

        /**
         * スタート項目(値)
         *
         * @return the startVal
         */
        public String getStartVal() {
            return startVal;
        }

        /**
         * スタート項目(値)
         *
         * @param startVal the startVal to set
         */
        public void setStartVal(String startVal) {
            this.startVal = startVal;
        }

        /**
         * スタート項目_テキスト(Rendered)
         *
         * @return the startTextRendered
         */
        public boolean isStartTextRendered() {
            return startTextRendered;
        }

        /**
         * スタート項目_テキスト(Rendered)
         *
         * @param startTextRendered the startTextRendered to set
         */
        public void setStartTextRendered(boolean startTextRendered) {
            this.startTextRendered = startTextRendered;
        }

        /**
         * スタート項目_テキスト(MaxLength)
         *
         * @return the startTextMaxLength
         */
        public String getStartTextMaxLength() {
            return startTextMaxLength;
        }

        /**
         * スタート項目_テキスト(MaxLength)
         *
         * @param startTextMaxLength the startTextMaxLength to set
         */
        public void setStartTextMaxLength(String startTextMaxLength) {
            this.startTextMaxLength = startTextMaxLength;
        }
        
        /**
         * スタート項目_テキスト(BackGround)
         *
         * @return the startTextBackColor
         */
        public String getStartTextBackColor() {
            return startTextBackColor;
        }

        /**
         * スタート項目_テキスト(BackGround)
         *
         * @param startTextBackColor the startTextBackColor to set
         */
        public void setStartTextBackColor(String startTextBackColor) {
            this.startTextBackColor = startTextBackColor;
        }

        
        /**
         * スタート項目_ラベル(Rendered)
         *
         * @return the startLabelRendered
         */
        public boolean isStartLabelRendered() {
            return startLabelRendered;
        }

        /**
         * スタート項目_ラベル(Rendered)
         *
         * @param startLabelRendered the startLabelRendered to set
         */
        public void setStartLabelRendered(boolean startLabelRendered) {
            this.startLabelRendered = startLabelRendered;
        }

        /**
         * エンド項目(値)
         *
         * @return the endVal
         */
        public String getEndVal() {
            return endVal;
        }

        /**
         * エンド項目(値)
         *
         * @param endVal the endVal to set
         */
        public void setEndVal(String endVal) {
            this.endVal = endVal;
        }

        /**
         * エンド項目_テキスト(Rendered)
         *
         * @return the endTextRendered
         */
        public boolean isEndTextRendered() {
            return endTextRendered;
        }

        /**
         * エンド項目_テキスト(Rendered)
         *
         * @param endTextRendered the endTextRendered to set
         */
        public void setEndTextRendered(boolean endTextRendered) {
            this.endTextRendered = endTextRendered;
        }

        /**
         * エンド項目_テキスト(MaxLength)
         *
         * @return the endTextMaxLength
         */
        public String getEndTextMaxLength() {
            return endTextMaxLength;
        }

        /**
         * エンド項目_テキスト(MaxLength)
         *
         * @param endTextMaxLength the endTextMaxLength to set
         */
        public void setEndTextMaxLength(String endTextMaxLength) {
            this.endTextMaxLength = endTextMaxLength;
        }

        /**
         * エンド項目_テキスト(BackGround)
         *
         * @return the endTextBackColor
         */
        public String getEndTextBackColor() {
            return endTextBackColor;
        }

        /**
         * エンド項目_テキスト(BackGround)
         *
         * @param endTextBackColor the endTextBackColor to set
         */
        public void setEndTextBackColor(String endTextBackColor) {
            this.endTextBackColor = endTextBackColor;
        }
        
        /**
         * エンド項目_ラベル(Rendered)
         *
         * @return the endLabelRendered
         */
        public boolean isEndLabelRendered() {
            return endLabelRendered;
        }

        /**
         * エンド項目_ラベル(Rendered)
         *
         * @param endLabelRendered the endLabelRendered to set
         */
        public void setEndLabelRendered(boolean endLabelRendered) {
            this.endLabelRendered = endLabelRendered;
        }
    }
}
