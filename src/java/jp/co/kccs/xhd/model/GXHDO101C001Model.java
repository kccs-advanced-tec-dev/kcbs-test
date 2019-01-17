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
 * GXHDO101C001Model(膜厚(SPS)サブ画面用)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
public class GXHDO101C001Model implements Cloneable {


    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C001Model clone() throws CloneNotSupportedException {
        GXHDO101C001Model cloneModel = (GXHDO101C001Model) super.clone();
        List<MakuatsuData> newList = new ArrayList();
        for (MakuatsuData data : this.getMakuatsuDataList()) {
            MakuatsuData newData = new MakuatsuData();
            newData.setMakuatsu(data.getMakuatsu());
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

        cloneModel.setMakuatsuDataList(newList);
        return cloneModel;
    }
    
    
    /**
     * 膜厚ｽﾀｰﾄAVE設定項目ID
     */
    private String returnItemIdStartAve ="";
    
    /**
     * 膜厚ｽﾀｰﾄMAX設定項目ID
     */
    private String returnItemIdStartMax ="";
    
    /**
     * 膜厚ｽﾀｰﾄMIN設定項目ID
     */
    private String returnItemIdStartMin ="";
    
    /**
     * 膜厚ｽﾀｰﾄCV設定項目ID
     */
    private String returnItemIdStartCv ="";
    
    /**
     * 膜厚ｴﾝﾄﾞAVE設定項目ID
     */
    private String returnItemIdEndAve ="";
    
    /**
     * 膜厚ｴﾝﾄﾞMAX設定項目ID
     */
    private String returnItemIdEndMax ="";
    
    /**
     * 膜厚ｴﾝﾄﾞMIN設定項目ID
     */
    private String returnItemIdEndMin ="";
    
    /**
     * 膜厚ｴﾝﾄﾞCV設定項目ID
     */
    private String returnItemIdEndCv ="";
    
    
    /**
     * 膜厚データリスト
     */
    private List<MakuatsuData> makuatsuDataList;

    /**
     * コンストラクタ
     */
    public GXHDO101C001Model() {
        this.makuatsuDataList = new ArrayList<>();
    }

    /**
     * 膜厚ｽﾀｰﾄAVE設定項目ID
     * @return the returnItemIdStartAve
     */
    public String getReturnItemIdStartAve() {
        return returnItemIdStartAve;
    }

    /**
     * 膜厚ｽﾀｰﾄAVE設定項目ID
     * @param returnItemIdStartAve the returnItemIdStartAve to set
     */
    public void setReturnItemIdStartAve(String returnItemIdStartAve) {
        this.returnItemIdStartAve = returnItemIdStartAve;
    }

    /**
     * 膜厚ｽﾀｰﾄMAX設定項目ID
     * @return the returnItemIdStartMax
     */
    public String getReturnItemIdStartMax() {
        return returnItemIdStartMax;
    }

    /**
     * 膜厚ｽﾀｰﾄMAX設定項目ID
     * @param returnItemIdStartMax the returnItemIdStartMax to set
     */
    public void setReturnItemIdStartMax(String returnItemIdStartMax) {
        this.returnItemIdStartMax = returnItemIdStartMax;
    }

    /**
     * 膜厚ｽﾀｰﾄMIN設定項目ID
     * @return the returnItemIdStartMin
     */
    public String getReturnItemIdStartMin() {
        return returnItemIdStartMin;
    }

    /**
     * 膜厚ｽﾀｰﾄMIN設定項目ID
     * @param returnItemIdStartMin the returnItemIdStartMin to set
     */
    public void setReturnItemIdStartMin(String returnItemIdStartMin) {
        this.returnItemIdStartMin = returnItemIdStartMin;
    }

    /**
     * 膜厚ｽﾀｰﾄCV設定項目ID
     * @return the returnItemIdStartCv
     */
    public String getReturnItemIdStartCv() {
        return returnItemIdStartCv;
    }

    /**
     * 膜厚ｽﾀｰﾄCV設定項目ID
     * @param returnItemIdStartCv the returnItemIdStartCv to set
     */
    public void setReturnItemIdStartCv(String returnItemIdStartCv) {
        this.returnItemIdStartCv = returnItemIdStartCv;
    }

    /**
     * 膜厚ｴﾝﾄﾞAVE設定項目ID
     * @return the returnItemIdEndAve
     */
    public String getReturnItemIdEndAve() {
        return returnItemIdEndAve;
    }

    /**
     * 膜厚ｴﾝﾄﾞAVE設定項目ID
     * @param returnItemIdEndAve the returnItemIdEndAve to set
     */
    public void setReturnItemIdEndAve(String returnItemIdEndAve) {
        this.returnItemIdEndAve = returnItemIdEndAve;
    }

    /**
     * 膜厚ｴﾝﾄﾞMAX設定項目ID
     * @return the returnItemIdEndMax
     */
    public String getReturnItemIdEndMax() {
        return returnItemIdEndMax;
    }

    /**
     * 膜厚ｴﾝﾄﾞMAX設定項目ID
     * @param returnItemIdEndMax the returnItemIdEndMax to set
     */
    public void setReturnItemIdEndMax(String returnItemIdEndMax) {
        this.returnItemIdEndMax = returnItemIdEndMax;
    }

    /**
     * 膜厚ｴﾝﾄﾞMIN設定項目ID
     * @return the returnItemIdEndMin
     */
    public String getReturnItemIdEndMin() {
        return returnItemIdEndMin;
    }

    /**
     * 膜厚ｴﾝﾄﾞMIN設定項目ID
     * @param returnItemIdEndMin the returnItemIdEndMin to set
     */
    public void setReturnItemIdEndMin(String returnItemIdEndMin) {
        this.returnItemIdEndMin = returnItemIdEndMin;
    }

    /**
     * 膜厚ｴﾝﾄﾞCV設定項目ID
     * @return the returnItemIdEndCv
     */
    public String getReturnItemIdEndCv() {
        return returnItemIdEndCv;
    }

    /**
     * 膜厚ｴﾝﾄﾞCV設定項目ID
     * @param returnItemIdEndCv the returnItemIdEndCv to set
     */
    public void setReturnItemIdEndCv(String returnItemIdEndCv) {
        this.returnItemIdEndCv = returnItemIdEndCv;
    }

    /**
     * 膜厚データリスト
     *
     * @return the makuatsuDataList
     */
    public List<MakuatsuData> getMakuatsuDataList() {
        return makuatsuDataList;
    }

    /**
     * 膜厚データリスト
     *
     * @param makuatsuDataList the makuatsuDataList to set
     */
    public void setMakuatsuDataList(List<MakuatsuData> makuatsuDataList) {
        this.makuatsuDataList = makuatsuDataList;
    }

    /**
     * 膜厚データ
     */
    public class MakuatsuData {

        /**
         * 膜厚
         */
        private String makuatsu;

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
         * 膜厚
         *
         * @return the makuatsu
         */
        public String getMakuatsu() {
            return makuatsu;
        }

        /**
         * 膜厚
         *
         * @param makuatsu the makuatsu to set
         */
        public void setMakuatsu(String makuatsu) {
            this.makuatsu = makuatsu;
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
