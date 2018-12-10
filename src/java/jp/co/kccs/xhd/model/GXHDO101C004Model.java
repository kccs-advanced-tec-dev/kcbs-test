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
 * GXHDO101C004Model(膜厚(RSUS)サブ画面用)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/04
 */
    public class GXHDO101C004Model implements Cloneable{

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C004Model clone() throws CloneNotSupportedException {
        GXHDO101C004Model cloneModel = (GXHDO101C004Model) super.clone();
        List<GXHDO101C004Model.makuatsuData> newList = new ArrayList();
        for (GXHDO101C004Model.makuatsuData data : this.makuatsuDataList) {
            GXHDO101C004Model.makuatsuData newData = new GXHDO101C004Model.makuatsuData();
            newData.setMakuatsu(data.getMakuatsu());
            newData.setStartVal(data.getStartVal());
            newData.setStartTextRendered(data.isStartTextRendered());
            newData.setStartTextMaxLength(data.getStartTextMaxLength());
            newData.setStartTextBackColor(data.getStartTextBackColor());
            newData.setStartLabelRendered(data.isStartLabelRendered());
            newList.add(newData);
        }

        cloneModel.setMakuatsuDataList(newList);
        return cloneModel;
    }

    /**
     * 膜厚データリスト
     */
    private List<makuatsuData> makuatsuDataList = new ArrayList<>();

    /**
     * 帳票データリスト
     * @return the makuatsuDataList
     */
    public List<makuatsuData> getMakuatsuDataList() {
        return makuatsuDataList;
    }

    /**
     * 膜厚データリスト
     * @param makuatsuDataList the makuatsuDataList to set
     */
    public void setMakuatsuDataList(List<makuatsuData> makuatsuDataList) {
        this.makuatsuDataList = makuatsuDataList;
    }

    /**
     * 膜厚データ
     */
    public class makuatsuData {

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

    }

}
