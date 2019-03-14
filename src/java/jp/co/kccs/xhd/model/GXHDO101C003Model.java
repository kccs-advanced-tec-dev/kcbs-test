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
 * GXHDO101C003Model(PTN距離ｴﾝﾄﾞサブ画面用)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
public class GXHDO101C003Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C003Model clone() throws CloneNotSupportedException {
        GXHDO101C003Model cloneModel = (GXHDO101C003Model) super.clone();
        List<GXHDO101C003Model.PtnKyoriEndData> newList = new ArrayList();
        for (GXHDO101C003Model.PtnKyoriEndData data : this.ptnKyoriEndDataList) {
            GXHDO101C003Model.PtnKyoriEndData newData = new GXHDO101C003Model.PtnKyoriEndData();
            newData.setPtnKyoriEnd(data.ptnKyoriEnd);
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

        cloneModel.setPtnKyoriEndDataList(newList);
        return cloneModel;
    }

    /**
     * PTN距離ｴﾝﾄﾞXMIN設定項目ID
     */
    private String returnItemIdEndXMin = "";

    /**
     * PTN距離ｴﾝﾄﾞYMIN設定項目ID
     */
    private String returnItemIdEndYMin = "";

    /**
     * PTN距離ｴﾝﾄﾞデータリスト
     */
    private List<GXHDO101C003Model.PtnKyoriEndData> ptnKyoriEndDataList;

    /**
     * コンストラクタ
     */
    public GXHDO101C003Model() {
        this.ptnKyoriEndDataList = new ArrayList<>();
    }

    /**
     * PTN距離ｴﾝﾄﾞXMIN設定項目ID
     *
     * @return the returnItemIdEndXMin
     */
    public String getReturnItemIdEndXMin() {
        return returnItemIdEndXMin;
    }

    /**
     * PTN距離ｴﾝﾄﾞXMIN設定項目ID
     *
     * @param returnItemIdEndXMin the returnItemIdEndXMin to set
     */
    public void setReturnItemIdEndXMin(String returnItemIdEndXMin) {
        this.returnItemIdEndXMin = returnItemIdEndXMin;
    }

    /**
     * PTN距離ｴﾝﾄﾞYMIN設定項目ID
     *
     * @return the returnItemIdEndYMin
     */
    public String getReturnItemIdEndYMin() {
        return returnItemIdEndYMin;
    }

    /**
     * PTN距離ｴﾝﾄﾞYMIN設定項目ID
     *
     * @param returnItemIdEndYMin the returnItemIdEndYMin to set
     */
    public void setReturnItemIdEndYMin(String returnItemIdEndYMin) {
        this.returnItemIdEndYMin = returnItemIdEndYMin;
    }

    /**
     * PTN距離ｴﾝﾄﾞデータリスト
     *
     * @return the ptnKyoriEndDataList
     */
    public List<GXHDO101C003Model.PtnKyoriEndData> getPtnKyoriEndDataList() {
        return ptnKyoriEndDataList;
    }

    /**
     * PTN距離ｴﾝﾄﾞデータリスト
     *
     * @param ptnKyoriEndDataList the ptnKyoriEndDataList to set
     */
    public void setPtnKyoriEndDataList(List<GXHDO101C003Model.PtnKyoriEndData> ptnKyoriEndDataList) {
        this.ptnKyoriEndDataList = ptnKyoriEndDataList;
    }

    /**
     * PTN距離ｴﾝﾄﾞデータ
     */
    public class PtnKyoriEndData {

        /**
         * PTN距離ｴﾝﾄﾞ
         */
        private String ptnKyoriEnd;
        
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
         * PTN距離Y
         *
         * @return the ptnKyoriEnd
         */
        public String getPtnKyoriEnd() {
            return ptnKyoriEnd;
        }

        /**
         * PTN距離Y
         *
         * @param ptnKyoriEnd the ptnKyoriEnd to set
         */
        public void setPtnKyoriEnd(String ptnKyoriEnd) {
            this.ptnKyoriEnd = ptnKyoriEnd;
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
