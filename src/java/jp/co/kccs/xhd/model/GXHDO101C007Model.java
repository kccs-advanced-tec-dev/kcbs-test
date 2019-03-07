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
 * GXHDO101C007Model(電極膜厚サブ画面用)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since  2019/01/08
 */
public class GXHDO101C007Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C007Model clone() throws CloneNotSupportedException {
        GXHDO101C007Model cloneModel = (GXHDO101C007Model) super.clone();
        List<GXHDO101C007Model.PrintWidthData> newList = new ArrayList();
        for (GXHDO101C007Model.PrintWidthData data : this.printWidthDataList) {
            GXHDO101C007Model.PrintWidthData newData = new GXHDO101C007Model.PrintWidthData();
            newData.setPrintWidth(data.getPrintWidth());
            newData.setStartVal(data.getStartVal());
            newData.setStartTextRendered(data.isStartTextRendered());
            newData.setStartTextMaxLength(data.getStartTextMaxLength());
            newData.setStartTextBackColor(data.getStartTextBackColor());
            newData.setStartLabelRendered(data.isStartLabelRendered());
            newList.add(newData);
        }

        cloneModel.setPrintWidthDataList(newList);
        return cloneModel;
    }

    /**
     * 電極膜厚データリスト
     */
    private List<PrintWidthData> printWidthDataList = new ArrayList<>();
    
    /**
     * コンストラクタ
     */
    public GXHDO101C007Model() {
        this.printWidthDataList = new ArrayList<>();
    }

    /**
     * 電極膜厚データリスト
     *
     * @return the printWidthDataList
     */
    public List<PrintWidthData> getPrintWidthDataList() {
        return printWidthDataList;
    }

    /**
     * 電極膜厚データリスト
     *
     * @param printWidthDataList the printWidthDataList to set
     */
    public void setPrintWidthDataList(List<PrintWidthData> printWidthDataList) {
        this.printWidthDataList = printWidthDataList;
    }

    /**
     * 電極膜厚データ
     */
    public class PrintWidthData {

        /**
         * 電極膜厚
         */
        private String printWidth;

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
         * 電極膜厚
         *
         * @return the printWidth
         */
        public String getPrintWidth() {
            return printWidth;
        }

        /**
         * 電極膜厚
         *
         * @param printWidth the printWidth to set
         */
        public void setPrintWidth(String printWidth) {
            this.printWidth = printWidth;
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
