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
 * GXHDO101C002Model(PTN距離ｽﾀｰﾄサブ画面用)のモデルクラスです。
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
        List<GXHDO101C002Model.PtnKyoriStartData> newList = new ArrayList();
        for (GXHDO101C002Model.PtnKyoriStartData data : this.ptnKyoriStartDataList) {
            GXHDO101C002Model.PtnKyoriStartData newData = new GXHDO101C002Model.PtnKyoriStartData();
            newData.setPtnKyoriStart(data.ptnKyoriStart);
            newData.setPtnKyoriXVal(data.getPtnKyoriXVal());
            newData.setPtnKyoriXTextRendered(data.isPtnKyoriXTextRendered());
            newData.setPtnKyoriXTextMaxLength(data.getPtnKyoriXTextMaxLength());
            newData.setPtnKyoriXTextBackColor(data.getPtnKyoriXTextBackColor());
            newData.setPtnKyoriXLabelRendered(data.isPtnKyoriXLabelRendered());
            newData.setPtnKyoriXTabIndex(data.getPtnKyoriXTabIndex());
            newData.setPtnKyoriYVal(data.getPtnKyoriYVal());
            newData.setPtnKyoriYTextRendered(data.isPtnKyoriYTextRendered());
            newData.setPtnKyoriYTextMaxLength(data.getPtnKyoriYTextMaxLength());
            newData.setPtnKyoriYTextBackColor(data.getPtnKyoriYTextBackColor());
            newData.setPtnKyoriYLabelRendered(data.isPtnKyoriYLabelRendered());
            newData.setPtnKyoriYTabIndex(data.getPtnKyoriYTabIndex());
            newList.add(newData);
        }

        cloneModel.setPtnKyoriStartDataList(newList);
        return cloneModel;
    }
    
    /**
     * PTN距離XｽﾀｰﾄMIN設定項目ID
     */
    private String returnItemIdStartXMin ="";
    
    /**
     * PTN距離YｽﾀｰﾄMIN設定項目ID
     */
    private String returnItemIdStartYMin ="";
    
    /**
     * PTN距離ｽﾀｰﾄデータリスト
     */
    private List<PtnKyoriStartData> ptnKyoriStartDataList;

    /**
     * コンストラクタ
     */
    public GXHDO101C002Model() {
        this.ptnKyoriStartDataList = new ArrayList<>();
    }

    /**
     * PTN距離XｽﾀｰﾄMIN設定項目ID
     * @return the returnItemIdStartXMin
     */
    public String getReturnItemIdStartXMin() {
        return returnItemIdStartXMin;
    }

    /**
     * PTN距離XｽﾀｰﾄMIN設定項目ID
     * @param returnItemIdStartXMin the returnItemIdStartXMin to set
     */
    public void setReturnItemIdStartXMin(String returnItemIdStartXMin) {
        this.returnItemIdStartXMin = returnItemIdStartXMin;
    }

    /**
     * PTN距離YｽﾀｰﾄMIN設定項目ID
     * @return the returnItemIdStartYMin
     */
    public String getReturnItemIdStartYMin() {
        return returnItemIdStartYMin;
    }

    /**
     * PTN距離YｽﾀｰﾄMIN設定項目ID
     * @param returnItemIdStartYMin the returnItemIdStartYMin to set
     */
    public void setReturnItemIdStartYMin(String returnItemIdStartYMin) {
        this.returnItemIdStartYMin = returnItemIdStartYMin;
    }

    /**
     * PTN距離ｽﾀｰﾄデータリスト
     *
     * @return the ptnKyoriStartDataList
     */
    public List<PtnKyoriStartData> getPtnKyoriStartDataList() {
        return ptnKyoriStartDataList;
    }

    /**
     * PTN距離ｽﾀｰﾄデータリスト
     *
     * @param ptnKyoriStartDataList the ptnKyoriStartDataList to set
     */
    public void setPtnKyoriStartDataList(List<PtnKyoriStartData> ptnKyoriStartDataList) {
        this.ptnKyoriStartDataList = ptnKyoriStartDataList;
    }

    /**
     * PTN距離ｽﾀｰﾄデータ
     */
    public class PtnKyoriStartData {

        /**
         * PTN距離ｽﾀｰﾄ
         */
        private String ptnKyoriStart;

        /**
         * PTN距離X(値)
         */
        private String ptnKyoriXVal;

        /**
         * PTN距離X_テキスト(Rendered)
         */
        private boolean ptnKyoriXTextRendered;

        /**
         * PTN距離X_テキスト(MaxLength)
         */
        private String ptnKyoriXTextMaxLength;

        /**
         * PTN距離X_テキスト(BackGround)
         */
        private String ptnKyoriXTextBackColor;

        /**
         * PTN距離X_ラベル(Rendered)
         */
        private boolean ptnKyoriXLabelRendered;

        /**
         * PTN距離X(タブインデックス)
         */
        private String ptnKyoriXTabIndex;

        /**
         * PTN距離Y(値)
         */
        private String ptnKyoriYVal;

        /**
         * PTN距離Y_テキスト(Rendered)
         */
        private boolean ptnKyoriYTextRendered;

        /**
         * PTN距離Y_テキスト(MaxLength)
         */
        private String ptnKyoriYTextMaxLength;

        /**
         * PTN距離Y_テキスト(BackGround)
         */
        private String ptnKyoriYTextBackColor;

        /**
         * PTN距離Y_ラベル(Rendered)
         */
        private boolean ptnKyoriYLabelRendered;

        /**
         * PTN距離Y(タブインデックス)
         */
        private String ptnKyoriYTabIndex;

        /**
         * PTN距離ｽﾀｰﾄ
         *
         * @return the ptnKyoriStart
         */
        public String getPtnKyoriStart() {
            return ptnKyoriStart;
        }

        /**
         * PTN距離ｽﾀｰﾄ
         *
         * @param ptnKyoriStart the ptnKyoriStart to set
         */
        public void setPtnKyoriStart(String ptnKyoriStart) {
            this.ptnKyoriStart = ptnKyoriStart;
        }

        /**
         * PTN距離X(値)
         *
         * @return the ptnKyoriXVal
         */
        public String getPtnKyoriXVal() {
            return ptnKyoriXVal;
        }

        /**
         * PTN距離X(値)
         *
         * @param ptnKyoriXVal the ptnKyoriXVal to set
         */
        public void setPtnKyoriXVal(String ptnKyoriXVal) {
            this.ptnKyoriXVal = ptnKyoriXVal;
        }

        /**
         * PTN距離X_テキスト(Rendered)
         *
         * @return the ptnKyoriXTextRendered
         */
        public boolean isPtnKyoriXTextRendered() {
            return ptnKyoriXTextRendered;
        }

        /**
         * PTN距離X_テキスト(Rendered)
         *
         * @param ptnKyoriXTextRendered the ptnKyoriXTextRendered to set
         */
        public void setPtnKyoriXTextRendered(boolean ptnKyoriXTextRendered) {
            this.ptnKyoriXTextRendered = ptnKyoriXTextRendered;
        }

        /**
         * PTN距離X_テキスト(MaxLength)
         *
         * @return the ptnKyoriXTextMaxLength
         */
        public String getPtnKyoriXTextMaxLength() {
            return ptnKyoriXTextMaxLength;
        }

        /**
         * PTN距離X_テキスト(MaxLength)
         *
         * @param ptnKyoriXTextMaxLength the ptnKyoriXTextMaxLength to set
         */
        public void setPtnKyoriXTextMaxLength(String ptnKyoriXTextMaxLength) {
            this.ptnKyoriXTextMaxLength = ptnKyoriXTextMaxLength;
        }

        /**
         * PTN距離X_テキスト(BackGround)
         *
         * @return the ptnKyoriXTextBackColor
         */
        public String getPtnKyoriXTextBackColor() {
            return ptnKyoriXTextBackColor;
        }

        /**
         * PTN距離X_テキスト(BackGround)
         *
         * @param ptnKyoriXTextBackColor the ptnKyoriXTextBackColor to set
         */
        public void setPtnKyoriXTextBackColor(String ptnKyoriXTextBackColor) {
            this.ptnKyoriXTextBackColor = ptnKyoriXTextBackColor;
        }

        /**
         * PTN距離X_ラベル(Rendered)
         *
         * @return the ptnKyoriXLabelRendered
         */
        public boolean isPtnKyoriXLabelRendered() {
            return ptnKyoriXLabelRendered;
        }

        /**
         * PTN距離X_ラベル(Rendered)
         *
         * @param ptnKyoriXLabelRendered the ptnKyoriXLabelRendered to set
         */
        public void setPtnKyoriXLabelRendered(boolean ptnKyoriXLabelRendered) {
            this.ptnKyoriXLabelRendered = ptnKyoriXLabelRendered;
        }

        /**
         * PTN距離X(タブインデックス)
         * 
         * @return the ptnKyoriXTabIndex
         */
        public String getPtnKyoriXTabIndex() {
            return ptnKyoriXTabIndex;
        }

        /**
         * PTN距離X(タブインデックス)
         * 
         * @param ptnKyoriXTabIndex the ptnKyoriXTabIndex to set
         */
        public void setPtnKyoriXTabIndex(String ptnKyoriXTabIndex) {
            this.ptnKyoriXTabIndex = ptnKyoriXTabIndex;
        }

        /**
         * PTN距離Y(値)
         *
         * @return the ptnKyoriYVal
         */
        public String getPtnKyoriYVal() {
            return ptnKyoriYVal;
        }

        /**
         * PTN距離Y(値)
         *
         * @param ptnKyoriYVal the ptnKyoriYVal to set
         */
        public void setPtnKyoriYVal(String ptnKyoriYVal) {
            this.ptnKyoriYVal = ptnKyoriYVal;
        }

        /**
         * PTN距離Y_テキスト(Rendered)
         *
         * @return the ptnKyoriYTextRendered
         */
        public boolean isPtnKyoriYTextRendered() {
            return ptnKyoriYTextRendered;
        }

        /**
         * PTN距離Y_テキスト(Rendered)
         *
         * @param ptnKyoriYTextRendered the ptnKyoriYTextRendered to set
         */
        public void setPtnKyoriYTextRendered(boolean ptnKyoriYTextRendered) {
            this.ptnKyoriYTextRendered = ptnKyoriYTextRendered;
        }

        /**
         * PTN距離Y_テキスト(MaxLength)
         *
         * @return the ptnKyoriYTextMaxLength
         */
        public String getPtnKyoriYTextMaxLength() {
            return ptnKyoriYTextMaxLength;
        }

        /**
         * PTN距離Y_テキスト(MaxLength)
         *
         * @param ptnKyoriYTextMaxLength the ptnKyoriYTextMaxLength to set
         */
        public void setPtnKyoriYTextMaxLength(String ptnKyoriYTextMaxLength) {
            this.ptnKyoriYTextMaxLength = ptnKyoriYTextMaxLength;
        }

        /**
         * PTN距離Y_テキスト(BackGround)
         *
         * @return the ptnKyoriYTextBackColor
         */
        public String getPtnKyoriYTextBackColor() {
            return ptnKyoriYTextBackColor;
        }

        /**
         * PTN距離Y_テキスト(BackGround)
         *
         * @param ptnKyoriYTextBackColor the ptnKyoriYTextBackColor to set
         */
        public void setPtnKyoriYTextBackColor(String ptnKyoriYTextBackColor) {
            this.ptnKyoriYTextBackColor = ptnKyoriYTextBackColor;
        }

        /**
         * PTN距離Y_ラベル(Rendered)
         *
         * @return the ptnKyoriYLabelRendered
         */
        public boolean isPtnKyoriYLabelRendered() {
            return ptnKyoriYLabelRendered;
        }

        /**
         * PTN距離Y_ラベル(Rendered)
         *
         * @param ptnKyoriYLabelRendered the ptnKyoriYLabelRendered to set
         */
        public void setPtnKyoriYLabelRendered(boolean ptnKyoriYLabelRendered) {
            this.ptnKyoriYLabelRendered = ptnKyoriYLabelRendered;
        }

        /**
         * PTN距離Y(タブインデックス)
         * 
         * @return the ptnKyoriYTabIndex
         */
        public String getPtnKyoriYTabIndex() {
            return ptnKyoriYTabIndex;
        }

        /**
         * PTN距離Y(タブインデックス)
         * 
         * @param ptnKyoriYTabIndex the ptnKyoriYTabIndex to set
         */
        public void setPtnKyoriYTabIndex(String ptnKyoriYTabIndex) {
            this.ptnKyoriYTabIndex = ptnKyoriYTabIndex;
        }
    }
}
