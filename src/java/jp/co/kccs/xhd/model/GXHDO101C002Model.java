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
 * GXHDO101C002Model(PTN距離Xサブ画面用)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
public class GXHDO101C002Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C002Model clone() throws CloneNotSupportedException {
        GXHDO101C002Model cloneModel = (GXHDO101C002Model) super.clone();
        List<GXHDO101C002Model.PtnKyoriXData> newList = new ArrayList();
        for (GXHDO101C002Model.PtnKyoriXData data : this.ptnKyoriXDataList) {
            GXHDO101C002Model.PtnKyoriXData newData = new GXHDO101C002Model.PtnKyoriXData();
            newData.setPtnKyoriX(data.ptnKyoriX);
            newData.setStartVal(data.getStartVal());
            newData.setStartTextRendered(data.isStartTextRendered());
            newData.setStartTextMaxLength(data.getStartTextMaxLength());
            newData.setStartTextBackColor(data.getStartTextBackColor());
            newData.setStartLabelRendered(data.isStartLabelRendered());
            newData.setStartTabIndex(data.getStartTabIndex());
            newData.setEndVal(data.getEndVal());
            newData.setEndTextRendered(data.isEndTextRendered());
            newData.setEndTextMaxLength(data.getEndTextMaxLength());
            newData.setEndTextBackColor(data.getEndTextBackColor());
            newData.setEndLabelRendered(data.isEndLabelRendered());
            newData.setEndTabIndex(data.getEndTabIndex());
            newList.add(newData);
        }

        cloneModel.setPtnKyoriXDataList(newList);
        return cloneModel;
    }
    
    /**
     * PTN距離XｽﾀｰﾄMIN設定項目ID
     */
    private String returnItemIdStartMin ="";
    
    /**
     * PTN距離XｴﾝﾄﾞMIN設定項目ID
     */
    private String returnItemIdEndMin ="";
    
    /**
     * PTN距離Xデータリスト
     */
    private List<PtnKyoriXData> ptnKyoriXDataList;

    /**
     * コンストラクタ
     */
    public GXHDO101C002Model() {
        this.ptnKyoriXDataList = new ArrayList<>();
    }

    /**
     * PTN距離XｽﾀｰﾄMIN設定項目ID
     * @return the returnItemIdStartMin
     */
    public String getReturnItemIdStartMin() {
        return returnItemIdStartMin;
    }

    /**
     * PTN距離XｽﾀｰﾄMIN設定項目ID
     * @param returnItemIdStartMin the returnItemIdStartMin to set
     */
    public void setReturnItemIdStartMin(String returnItemIdStartMin) {
        this.returnItemIdStartMin = returnItemIdStartMin;
    }

    /**
     * PTN距離XｴﾝﾄﾞMIN設定項目ID
     * @return the returnItemIdEndMin
     */
    public String getReturnItemIdEndMin() {
        return returnItemIdEndMin;
    }

    /**
     * PTN距離XｴﾝﾄﾞMIN設定項目ID
     * @param returnItemIdEndMin the returnItemIdEndMin to set
     */
    public void setReturnItemIdEndMin(String returnItemIdEndMin) {
        this.returnItemIdEndMin = returnItemIdEndMin;
    }

    /**
     * PTN距離Xデータリスト
     *
     * @return the ptnKyoriXDataList
     */
    public List<PtnKyoriXData> getPtnKyoriXDataList() {
        return ptnKyoriXDataList;
    }

    /**
     * PTN距離Xデータリスト
     *
     * @param ptnKyoriXDataList the ptnKyoriXDataList to set
     */
    public void setPtnKyoriXDataList(List<PtnKyoriXData> ptnKyoriXDataList) {
        this.ptnKyoriXDataList = ptnKyoriXDataList;
    }

    /**
     * PTN距離Xデータ
     */
    public class PtnKyoriXData {

        /**
         * PTN距離X
         */
        private String ptnKyoriX;

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
         * スタート項目(タブインデックス)
         */
        private String startTabIndex;

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
         * エンド項目(タブインデックス)
         */
        private String endTabIndex;

        /**
         * PTN距離X
         *
         * @return the ptnKyoriX
         */
        public String getPtnKyoriX() {
            return ptnKyoriX;
        }

        /**
         * PTN距離X
         *
         * @param ptnKyoriX the ptnKyoriX to set
         */
        public void setPtnKyoriX(String ptnKyoriX) {
            this.ptnKyoriX = ptnKyoriX;
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
         * スタート項目(タブインデックス)
         * 
         * @return the startTabIndex
         */
        public String getStartTabIndex() {
            return startTabIndex;
        }

        /**
         * スタート項目(タブインデックス)
         * 
         * @param startTabIndex the startTabIndex to set
         */
        public void setStartTabIndex(String startTabIndex) {
            this.startTabIndex = startTabIndex;
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

        /**
         * エンド項目(タブインデックス)
         * 
         * @return the endTabIndex
         */
        public String getEndTabIndex() {
            return endTabIndex;
        }

        /**
         * エンド項目(タブインデックス)
         * 
         * @param endTabIndex the endTabIndex to set
         */
        public void setEndTabIndex(String endTabIndex) {
            this.endTabIndex = endTabIndex;
        }
    }
}
