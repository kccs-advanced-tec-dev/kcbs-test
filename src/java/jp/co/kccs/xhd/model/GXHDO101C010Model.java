/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/01/09<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101C010Model(被り量（μｍ）サブ画面用)のモデルクラスです。
 *
 * @author KCSS K.Jo
 * @since 2019/01/09
 */
public class GXHDO101C010Model implements Cloneable {

    /**
     * クローン実装
     *
     * @return クローン
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public GXHDO101C010Model clone() throws CloneNotSupportedException {
        GXHDO101C010Model cloneModel = (GXHDO101C010Model) super.clone();
        cloneModel.setKaburiryouData1(createCloneModel(kaburiryouData1));
        cloneModel.setKaburiryouData2(createCloneModel(kaburiryouData2));
        cloneModel.setKaburiryouData3(createCloneModel(kaburiryouData3));
        cloneModel.setKaburiryouData4(createCloneModel(kaburiryouData4));
        cloneModel.setKaburiryouData5(createCloneModel(kaburiryouData5));
        cloneModel.setKaburiryouData6(createCloneModel(kaburiryouData6));
        cloneModel.setKaburiryouData7(createCloneModel(kaburiryouData7));
        cloneModel.setKaburiryouData8(createCloneModel(kaburiryouData8));
        cloneModel.setKaburiryouData9(createCloneModel(kaburiryouData9));
        cloneModel.setKaburiryouData10(createCloneModel(kaburiryouData10));
        cloneModel.setKaburiryouData11(createCloneModel(kaburiryouData11));
        cloneModel.setKaburiryouData12(createCloneModel(kaburiryouData12));
        cloneModel.setKaburiryouData13(createCloneModel(kaburiryouData13));
        cloneModel.setKaburiryouData14(createCloneModel(kaburiryouData14));
        cloneModel.setKaburiryouData15(createCloneModel(kaburiryouData15));
        cloneModel.setKaburiryouData16(createCloneModel(kaburiryouData16));
        cloneModel.setKaburiryouData17(createCloneModel(kaburiryouData17));
        cloneModel.setKaburiryouData18(createCloneModel(kaburiryouData18));
        cloneModel.setKaburiryouData19(createCloneModel(kaburiryouData19));
        cloneModel.setKaburiryouData20(createCloneModel(kaburiryouData20));

        return cloneModel;
    }

    /**
     * clone用のモデルを生成
     *
     * @param originalData 元データ
     * @return 被り量データモデル
     */
    private GXHDO101C010Model.KaburiryouData createCloneModel(GXHDO101C010Model.KaburiryouData originalData) {
        GXHDO101C010Model.KaburiryouData cloneData = new GXHDO101C010Model.KaburiryouData();
        cloneData.setValue(originalData.getValue());
        cloneData.setTextRendered(originalData.isTextRendered());
        cloneData.setTextMaxLength(originalData.getTextMaxLength());
        cloneData.setTextDecimalPlaces(originalData.getTextDecimalPlaces());
        cloneData.setTextMaxValue(originalData.getTextMaxValue());
        cloneData.setTextMinValue(originalData.getTextMinValue());
        cloneData.setTextBackColor(originalData.getTextBackColor());
        cloneData.setLabelRendered(originalData.isLabelRendered());
        cloneData.setTabIndex(originalData.getTabIndex());
        return cloneData;
    }

    /**
     * 被り量(μm)1
     *
     */
    private KaburiryouData kaburiryouData1;

    /**
     * 被り量(μm)2
     *
     */
    private KaburiryouData kaburiryouData2;

    /**
     * 被り量(μm)3
     *
     */
    private KaburiryouData kaburiryouData3;

    /**
     * 被り量(μm)4
     *
     */
    private KaburiryouData kaburiryouData4;

    /**
     * 被り量(μm)5
     *
     */
    private KaburiryouData kaburiryouData5;

    /**
     * 被り量(μm)6
     *
     */
    private KaburiryouData kaburiryouData6;

    /**
     * 被り量(μm)7
     *
     */
    private KaburiryouData kaburiryouData7;

    /**
     * 被り量(μm)8
     *
     */
    private KaburiryouData kaburiryouData8;

    /**
     * 被り量(μm)9
     *
     */
    private KaburiryouData kaburiryouData9;

    /**
     * 被り量(μm)10
     *
     */
    private KaburiryouData kaburiryouData10;

    /**
     * 被り量(μm)11
     *
     */
    private KaburiryouData kaburiryouData11;

    /**
     * 被り量(μm)12
     *
     */
    private KaburiryouData kaburiryouData12;

    /**
     * 被り量(μm)13
     *
     */
    private KaburiryouData kaburiryouData13;

    /**
     * 被り量(μm)14
     *
     */
    private KaburiryouData kaburiryouData14;

    /**
     * 被り量(μm)15
     *
     */
    private KaburiryouData kaburiryouData15;

    /**
     * 被り量(μm)16
     *
     */
    private KaburiryouData kaburiryouData16;

    /**
     * 被り量(μm)17
     *
     */
    private KaburiryouData kaburiryouData17;

    /**
     * 被り量(μm)18
     *
     */
    private KaburiryouData kaburiryouData18;

    /**
     * 被り量(μm)19
     *
     */
    private KaburiryouData kaburiryouData19;

    /**
     * 被り量(μm)20
     *
     */
    private KaburiryouData kaburiryouData20;

    /**
     * コンストラクタ
     */
    public GXHDO101C010Model() {

    }

    /**
     * 被り量(μm)1
     *
     * @return the kaburiryouData1
     */
    public KaburiryouData getKaburiryouData1() {
        return kaburiryouData1;
    }

    /**
     * 被り量(μm)1
     *
     * @param kaburiryouData1 the kaburiryouData1 to set
     */
    public void setKaburiryouData1(KaburiryouData kaburiryouData1) {
        this.kaburiryouData1 = kaburiryouData1;
    }

    /**
     * 被り量(μm)2
     *
     * @return the kaburiryouData2
     */
    public KaburiryouData getKaburiryouData2() {
        return kaburiryouData2;
    }

    /**
     * 被り量(μm)2
     *
     * @param kaburiryouData2 the kaburiryouData2 to set
     */
    public void setKaburiryouData2(KaburiryouData kaburiryouData2) {
        this.kaburiryouData2 = kaburiryouData2;
    }

    /**
     * 被り量(μm)2
     *
     * @return the kaburiryouData3
     */
    public KaburiryouData getKaburiryouData3() {
        return kaburiryouData3;
    }

    /**
     * 被り量(μm)3
     *
     * @param kaburiryouData3 the kaburiryouData3 to set
     */
    public void setKaburiryouData3(KaburiryouData kaburiryouData3) {
        this.kaburiryouData3 = kaburiryouData3;
    }

    /**
     * 被り量(μm)4
     *
     * @return the kaburiryouData4
     */
    public KaburiryouData getKaburiryouData4() {
        return kaburiryouData4;
    }

    /**
     * 被り量(μm)4
     *
     * @param kaburiryouData4 the kaburiryouData4 to set
     */
    public void setKaburiryouData4(KaburiryouData kaburiryouData4) {
        this.kaburiryouData4 = kaburiryouData4;
    }

    /**
     * 被り量(μm)5
     *
     * @return the kaburiryouData5
     */
    public KaburiryouData getKaburiryouData5() {
        return kaburiryouData5;
    }

    /**
     * 被り量(μm)5
     *
     * @param kaburiryouData5 the kaburiryouData5 to set
     */
    public void setKaburiryouData5(KaburiryouData kaburiryouData5) {
        this.kaburiryouData5 = kaburiryouData5;
    }

    /**
     * 被り量(μm)6
     *
     * @return the kaburiryouData6
     */
    public KaburiryouData getKaburiryouData6() {
        return kaburiryouData6;
    }

    /**
     * 被り量(μm)6
     *
     * @param kaburiryouData6 the kaburiryouData6 to set
     */
    public void setKaburiryouData6(KaburiryouData kaburiryouData6) {
        this.kaburiryouData6 = kaburiryouData6;
    }

    /**
     * 被り量(μm)7
     *
     * @return the kaburiryouData7
     */
    public KaburiryouData getKaburiryouData7() {
        return kaburiryouData7;
    }

    /**
     * 被り量(μm)7
     *
     * @param kaburiryouData7 the kaburiryouData7 to set
     */
    public void setKaburiryouData7(KaburiryouData kaburiryouData7) {
        this.kaburiryouData7 = kaburiryouData7;
    }

    /**
     * 被り量(μm)8
     *
     * @return the kaburiryouData8
     */
    public KaburiryouData getKaburiryouData8() {
        return kaburiryouData8;
    }

    /**
     * 被り量(μm)8
     *
     * @param kaburiryouData8 the kaburiryouData8 to set
     */
    public void setKaburiryouData8(KaburiryouData kaburiryouData8) {
        this.kaburiryouData8 = kaburiryouData8;
    }

    /**
     * 被り量(μm)9
     *
     * @return the kaburiryouData9
     */
    public KaburiryouData getKaburiryouData9() {
        return kaburiryouData9;
    }

    /**
     * 被り量(μm)9
     *
     * @param kaburiryouData9 the kaburiryouData9 to set
     */
    public void setKaburiryouData9(KaburiryouData kaburiryouData9) {
        this.kaburiryouData9 = kaburiryouData9;
    }

    /**
     * 被り量(μm)10
     *
     * @return the kaburiryouData10
     */
    public KaburiryouData getKaburiryouData10() {
        return kaburiryouData10;
    }

    /**
     * 被り量(μm)10
     *
     * @param kaburiryouData10 the kaburiryouData10 to set
     */
    public void setKaburiryouData10(KaburiryouData kaburiryouData10) {
        this.kaburiryouData10 = kaburiryouData10;
    }

    /**
     * 被り量(μm)11
     *
     * @return the kaburiryouData11
     */
    public KaburiryouData getKaburiryouData11() {
        return kaburiryouData11;
    }

    /**
     * 被り量(μm)11
     *
     * @param kaburiryouData11 the kaburiryouData11 to set
     */
    public void setKaburiryouData11(KaburiryouData kaburiryouData11) {
        this.kaburiryouData11 = kaburiryouData11;
    }

    /**
     * 被り量(μm)12
     *
     * @return the kaburiryouData12
     */
    public KaburiryouData getKaburiryouData12() {
        return kaburiryouData12;
    }

    /**
     * 被り量(μm)12
     *
     * @param kaburiryouData12 the kaburiryouData12 to set
     */
    public void setKaburiryouData12(KaburiryouData kaburiryouData12) {
        this.kaburiryouData12 = kaburiryouData12;
    }

    /**
     * 被り量(μm)13
     *
     * @return the kaburiryouData13
     */
    public KaburiryouData getKaburiryouData13() {
        return kaburiryouData13;
    }

    /**
     * 被り量(μm)13
     *
     * @param kaburiryouData13 the kaburiryouData13 to set
     */
    public void setKaburiryouData13(KaburiryouData kaburiryouData13) {
        this.kaburiryouData13 = kaburiryouData13;
    }

    /**
     * 被り量(μm)14
     *
     * @return the kaburiryouData14
     */
    public KaburiryouData getKaburiryouData14() {
        return kaburiryouData14;
    }

    /**
     * 被り量(μm)14
     *
     * @param kaburiryouData14 the kaburiryouData14 to set
     */
    public void setKaburiryouData14(KaburiryouData kaburiryouData14) {
        this.kaburiryouData14 = kaburiryouData14;
    }

    /**
     * 被り量(μm)15
     *
     * @return the kaburiryouData15
     */
    public KaburiryouData getKaburiryouData15() {
        return kaburiryouData15;
    }

    /**
     * 被り量(μm)15
     *
     * @param kaburiryouData15 the kaburiryouData15 to set
     */
    public void setKaburiryouData15(KaburiryouData kaburiryouData15) {
        this.kaburiryouData15 = kaburiryouData15;
    }

    /**
     * 被り量(μm)16
     *
     * @return the kaburiryouData16
     */
    public KaburiryouData getKaburiryouData16() {
        return kaburiryouData16;
    }

    /**
     * 被り量(μm)16
     *
     * @param kaburiryouData16 the kaburiryouData16 to set
     */
    public void setKaburiryouData16(KaburiryouData kaburiryouData16) {
        this.kaburiryouData16 = kaburiryouData16;
    }

    /**
     * 被り量(μm)17
     *
     * @return the kaburiryouData17
     */
    public KaburiryouData getKaburiryouData17() {
        return kaburiryouData17;
    }

    /**
     * 被り量(μm)17
     *
     * @param kaburiryouData17 the kaburiryouData17 to set
     */
    public void setKaburiryouData17(KaburiryouData kaburiryouData17) {
        this.kaburiryouData17 = kaburiryouData17;
    }

    /**
     * 被り量(μm)18
     *
     * @return the kaburiryouData18
     */
    public KaburiryouData getKaburiryouData18() {
        return kaburiryouData18;
    }

    /**
     * 被り量(μm)18
     *
     * @param kaburiryouData18 the kaburiryouData18 to set
     */
    public void setKaburiryouData18(KaburiryouData kaburiryouData18) {
        this.kaburiryouData18 = kaburiryouData18;
    }

    /**
     * 被り量(μm)19
     *
     * @return the kaburiryouData19
     */
    public KaburiryouData getKaburiryouData19() {
        return kaburiryouData19;
    }

    /**
     * 被り量(μm)19
     *
     * @param kaburiryouData19 the kaburiryouData19 to set
     */
    public void setKaburiryouData19(KaburiryouData kaburiryouData19) {
        this.kaburiryouData19 = kaburiryouData19;
    }

    /**
     * 被り量(μm)20
     *
     * @return the kaburiryouData20
     */
    public KaburiryouData getKaburiryouData20() {
        return kaburiryouData20;
    }

    /**
     * 被り量(μm)20
     *
     * @param kaburiryouData20 the kaburiryouData20 to set
     */
    public void setKaburiryouData20(KaburiryouData kaburiryouData20) {
        this.kaburiryouData20 = kaburiryouData20;
    }

    /**
     * 被り量（μｍ）データ
     */
    public class KaburiryouData {

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
         * スタート項目_テキスト(BackGround)
         */
        private String textBackColor;

        /**
         * スタート項目_ラベル(Rendered)
         */
        private boolean labelRendered;

        /**
         * スタート項目_タブインデックス(TabIndex)
         */
        private String tabIndex;

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
         * テキスト(DecimalPlaces)
         *
         * @return the textDecimalPlaces
         */
        public String getTextDecimalPlaces() {
            return textDecimalPlaces;
        }

        /**
         * テキスト(DecimalPlaces)
         *
         * @param textDecimalPlaces the textDecimalPlaces to set
         */
        public void setTextDecimalPlaces(String textDecimalPlaces) {
            this.textDecimalPlaces = textDecimalPlaces;
        }

        /**
         * テキスト(MaxValue)
         *
         * @return the textMaxValue
         */
        public String getTextMaxValue() {
            return textMaxValue;
        }

        /**
         * テキスト(MaxValue)
         *
         * @param textMaxValue the textMaxValue to set
         */
        public void setTextMaxValue(String textMaxValue) {
            this.textMaxValue = textMaxValue;
        }

        /**
         * テキスト(MinValue)
         *
         * @return the textMinValue
         */
        public String getTextMinValue() {
            return textMinValue;
        }

        /**
         * テキスト(MinValue)
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
