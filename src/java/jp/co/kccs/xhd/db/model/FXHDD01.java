/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/05/06<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * 変更日	2018/12/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	項目追加<br>
 * <br>
 * 変更日	2020/11/11<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	FXHDD01の入力項目型[9]の追加対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * FXHDD01(フォームパラメータマスタ)のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
public class FXHDD01 {

    /**
     * 画面ID
     */
    private String gamenId;
    /**
     * 項目ID
     */
    private String itemId;
    /**
     * 項目No
     */
    private int itemNo;
    /**
     * 入力項目型
     */
    private String inputItemType;
    /**
     * 表示ﾗﾍﾞﾙ1
     */
    private String label1;
    /**
     * 表示ﾗﾍﾞﾙ2
     */
    private String label2;
    /**
     * 入力項目設定値
     */
    private String inputList;
    /**
     * 入力項目初期値
     */
    private String inputDefault;
    /**
     * 入力項目桁数(整数部)
     */
    private String inputLength;
    /**
     * 入力項目桁数(小数部)
     */
    private String inputLengthDec;
    /**
     * 表示ﾗﾍﾞﾙ1文字ｻｲｽﾞ
     */
    private int fontSize1;
    /**
     * 表示ﾗﾍﾞﾙ1文字色
     */
    private String fontColor1;
    /**
     * 表示ﾗﾍﾞﾙ1背景色
     */
    private String backColor1;
    /**
     * 表示ﾗﾍﾞﾙ1render有無
     */
    private boolean render1;
    /**
     * 表示ﾗﾍﾞﾙ2文字ｻｲｽﾞ
     */
    private int fontSize2;
    /**
     * 表示ﾗﾍﾞﾙ2文字色
     */
    private String fontColor2;
    /**
     * 表示ﾗﾍﾞﾙ2背景色
     */
    private String backColor2;
    /**
     * 表示ﾗﾍﾞﾙ2render有無
     */
    private boolean render2;
    /**
     * 表示ﾗﾍﾞﾙリンクrender有無
     */
    private boolean renderLinkButton;
    /**
     * 入力項目文字ｻｲｽﾞ
     */
    private int fontSizeInput;
    /**
     * 入力項目文字色
     */
    private String fontColorInput;
    /**
     * 入力項目背景色
     */
    private String backColorInput;
    /**
     * 入力項目(文字列)render有無
     */
    private boolean renderInputText;
    /**
     * 入力項目(数値)render有無
     */
    private boolean renderInputNumber;
    /**
     * 入力項目(日付)render有無
     */
    private boolean renderInputDate;
    /**
     * 入力項目(ドロップダウンリスト)render有無
     */
    private boolean renderInputSelect;
    /**
     * 入力項目(ラジオボタン)render有無
     */
    private boolean renderInputRadio;
    /**
     * 入力項目(時刻)render有無
     */
    private boolean renderInputTime;
    /**
     * 入力項目(チェックボックス)render有無
     */
    private boolean renderInputCheckBox;
    /**
     * 入力項目カスタムスタイル
     */
    private String customStyleInput;
    /**
     * ラベル表示render有無
     */
    private boolean renderOutputLabel;
    /**
     * 入力項目(入力可能ドロップダウンリスト)render有無
     */
    private boolean renderInputSelectAvailable;
    /**
     * 表示ﾗﾍﾞﾙ3文字ｻｲｽﾞ
     */
    private int fontSize3;
    /**
     * 表示ﾗﾍﾞﾙ3文字色
     */
    private String fontColor3;
    /**
     * 表示ﾗﾍﾞﾙ3背景色
     */
    private String backColor3;
    /**
     * 条件ﾃｰﾌﾞﾙ工程名
     */
    private String jokenKoteiMei;
    /**
     * 条件ﾃｰﾌﾞﾙ項目名
     */
    private String jokenKomokuMei;
    /**
     * 条件ﾃｰﾌﾞﾙ管理項目
     */
    private String jokenKanriKomoku;
    /**
     * 規格情報ﾊﾟﾀｰﾝ
     */
    private String standardPattern;

    /**
     * ﾎﾞﾀﾝID
     */
    private String linkButtonId;
    
    /**
     * 規格値
     */
    private String kikakuChi;
    /**
     * 入力値
     */
    private String value;
    /**
     * 入力項目背景色(通常)
     */
    private String backColorInputDefault;
    /**
     * 項目ｲﾝﾃﾞｯｸｽ
     */
    private int itemIndex;

    public FXHDD01() {

    }

    /**
     * 画面ID
     *
     * @return the gamenId
     */
    public String getGamenId() {
        return gamenId;
    }

    /**
     * 画面ID
     *
     * @param gamenId the gamenId to set
     */
    public void setGamenId(String gamenId) {
        this.gamenId = gamenId;
    }

    /**
     * 項目ID
     *
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * 項目ID
     *
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * 項目No
     *
     * @return the itemNo
     */
    public int getItemNo() {
        return itemNo;
    }

    /**
     * 項目No
     *
     * @param itemNo the itemNo to set
     */
    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * 入力項目型
     *
     * @return the inputItemType
     */
    public String getInputItemType() {
        return inputItemType;
    }

    /**
     * 入力項目型
     *
     * @param inputItemType the inputItemType to set
     */
    public void setInputItemType(String inputItemType) {
        this.inputItemType = inputItemType;
    }

    /**
     * 表示ﾗﾍﾞﾙ1
     *
     * @return the label1
     */
    public String getLabel1() {
        return label1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1
     *
     * @param label1 the label1 to set
     */
    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    /**
     * 表示ﾗﾍﾞﾙ2
     *
     * @return the label2
     */
    public String getLabel2() {
        return label2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2
     *
     * @param label2 the label2 to set
     */
    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    /**
     * 入力項目設定値
     *
     * @return the inputList
     */
    public String getInputList() {
        return inputList;
    }

    /**
     * 入力項目設定値
     *
     * @param inputList the inputList to set
     */
    public void setInputList(String inputList) {
        this.inputList = inputList;
    }

    /**
     * 入力項目初期値
     *
     * @return the inputDefault
     */
    public String getInputDefault() {
        return inputDefault;
    }

    /**
     * 入力項目初期値
     *
     * @param inputDefault the inputDefault to set
     */
    public void setInputDefault(String inputDefault) {
        this.inputDefault = inputDefault;
    }

    /**
     * 入力項目桁数(整数部)
     *
     * @return the inputLength
     */
    public String getInputLength() {
        return inputLength;
    }

    /**
     * 入力項目桁数(整数部)
     *
     * @param inputLength the inputLength to set
     */
    public void setInputLength(String inputLength) {
        this.inputLength = inputLength;
    }

    /**
     * 入力項目桁数(小数部)
     *
     * @return the inputLengthDec
     */
    public String getInputLengthDec() {
        return inputLengthDec;
    }

    /**
     * 入力項目桁数(小数部)
     *
     * @param inputLengthDec the inputLengthDec to set
     */
    public void setInputLengthDec(String inputLengthDec) {
        this.inputLengthDec = inputLengthDec;
    }

    /**
     * 表示ﾗﾍﾞﾙ1文字ｻｲｽﾞ
     *
     * @return the fontSize1
     */
    public int getFontSize1() {
        return fontSize1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1文字ｻｲｽﾞ
     *
     * @param fontSize1 the fontSize1 to set
     */
    public void setFontSize1(int fontSize1) {
        this.fontSize1 = fontSize1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1文字色
     *
     * @return the fontColor1
     */
    public String getFontColor1() {
        return fontColor1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1文字色
     *
     * @param fontColor1 the fontColor1 to set
     */
    public void setFontColor1(String fontColor1) {
        this.fontColor1 = fontColor1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1背景色
     *
     * @return the backColor1
     */
    public String getBackColor1() {
        if (StringUtil.isEmpty(backColor1) && 0 == itemNo % 2) {
            return "#EEEEEE";
        }
        return backColor1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1背景色
     *
     * @param backColor1 the backColor1 to set
     */
    public void setBackColor1(String backColor1) {
        this.backColor1 = backColor1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1render有無
     *
     * @return the render1
     */
    public boolean isRender1() {
        return render1;
    }

    /**
     * 表示ﾗﾍﾞﾙ1render有無
     *
     * @param render1 the render1 to set
     */
    public void setRender1(boolean render1) {
        this.render1 = render1;
    }

    /**
     * 表示ﾗﾍﾞﾙ2文字ｻｲｽﾞ
     *
     * @return the fontSize2
     */
    public int getFontSize2() {
        return fontSize2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2文字ｻｲｽﾞ
     *
     * @param fontSize2 the fontSize2 to set
     */
    public void setFontSize2(int fontSize2) {
        this.fontSize2 = fontSize2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2文字色
     *
     * @return the fontColor2
     */
    public String getFontColor2() {
        return fontColor2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2文字色
     *
     * @param fontColor2 the fontColor2 to set
     */
    public void setFontColor2(String fontColor2) {
        this.fontColor2 = fontColor2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2背景色
     *
     * @return the backColor2
     */
    public String getBackColor2() {
        return backColor2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2背景色
     *
     * @param backColor2 the backColor2 to set
     */
    public void setBackColor2(String backColor2) {
        this.backColor2 = backColor2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2render有無
     *
     * @return the render2
     */
    public boolean isRender2() {
        return render2;
    }

    /**
     * 表示ﾗﾍﾞﾙ2render有無
     *
     * @param render2 the render2 to set
     */
    public void setRender2(boolean render2) {
        this.render2 = render2;
    }

    /**
     * 表示ﾗﾍﾞﾙリンクrender有無
     *
     * @return the renderLinkButton
     */
    public boolean isRenderLinkButton() {
        return renderLinkButton;
    }

    /**
     * 表示ﾗﾍﾞﾙリンクrender有無
     *
     * @param renderLinkButton the renderLinkButton to set
     */
    public void setRenderLinkButton(boolean renderLinkButton) {
        this.renderLinkButton = renderLinkButton;
    }
    
    /**
     * 入力項目文字ｻｲｽﾞ
     *
     * @return the fontSizeInput
     */
    public int getFontSizeInput() {
        return fontSizeInput;
    }

    /**
     * 入力項目文字ｻｲｽﾞ
     *
     * @param fontSizeInput the fontSizeInput to set
     */
    public void setFontSizeInput(int fontSizeInput) {
        this.fontSizeInput = fontSizeInput;
    }

    /**
     * 入力項目文字色
     *
     * @return the fontColorInput
     */
    public String getFontColorInput() {
        return fontColorInput;
    }

    /**
     * 入力項目文字色
     *
     * @param fontColorInput the fontColorInput to set
     */
    public void setFontColorInput(String fontColorInput) {
        this.fontColorInput = fontColorInput;
    }

    /**
     * 入力項目背景色
     *
     * @return the backColorInput
     */
    public String getBackColorInput() {
        return backColorInput;
    }

    /**
     * 入力項目背景色
     *
     * @param backColorInput the backColorInput to set
     */
    public void setBackColorInput(String backColorInput) {
        this.backColorInput = backColorInput;
    }

    /**
     * 入力項目(文字列)render有無
     *
     * @return the renderInputText
     */
    public boolean isRenderInputText() {
        return renderInputText;
    }

    /**
     * 入力項目(文字列)render有無
     *
     * @param renderInputText the renderInputText to set
     */
    public void setRenderInputText(boolean renderInputText) {
        this.renderInputText = renderInputText;
    }

    /**
     * 入力項目(数値)render有無
     *
     * @return the renderInputNumber
     */
    public boolean isRenderInputNumber() {
        return renderInputNumber;
    }

    /**
     * 入力項目(数値)render有無
     *
     * @param renderInputNumber the renderInputNumber to set
     */
    public void setRenderInputNumber(boolean renderInputNumber) {
        this.renderInputNumber = renderInputNumber;
    }

    /**
     * 入力項目(日付)render有無
     *
     * @return the renderInputDate
     */
    public boolean isRenderInputDate() {
        return renderInputDate;
    }

    /**
     * 入力項目(日付)render有無
     *
     * @param renderInputDate the renderInputDate to set
     */
    public void setRenderInputDate(boolean renderInputDate) {
        this.renderInputDate = renderInputDate;
    }

    /**
     * 入力項目(ドロップダウンリスト)render有無
     *
     * @return the renderInputSelect
     */
    public boolean isRenderInputSelect() {
        return renderInputSelect;
    }

    /**
     * 入力項目(ドロップダウンリスト)render有無
     *
     * @param renderInputSelect the renderInputSelect to set
     */
    public void setRenderInputSelect(boolean renderInputSelect) {
        this.renderInputSelect = renderInputSelect;
    }

    /**
     * 入力項目(ラジオボタン)render有無
     *
     * @return the renderInputRadio
     */
    public boolean isRenderInputRadio() {
        return renderInputRadio;
    }

    /**
     * 入力項目(ラジオボタン)render有無
     *
     * @param renderInputRadio the renderInputRadio to set
     */
    public void setRenderInputRadio(boolean renderInputRadio) {
        this.renderInputRadio = renderInputRadio;
    }

    /**
     * 入力項目(チェックボックス)render有無
     * @return the renderInputCheckBox
     */
    public boolean isRenderInputCheckBox() {
        return renderInputCheckBox;
    }

    /**
     * 入力項目(チェックボックス)render有無
     * @param renderInputCheckBox the renderInputCheckBox to set
     */
    public void setRenderInputCheckBox(boolean renderInputCheckBox) {
        this.renderInputCheckBox = renderInputCheckBox;
    }

    /**
     * 入力項目(時刻)render有無
     *
     * @return the renderInputTime
     */
    public boolean isRenderInputTime() {
        return renderInputTime;
    }

    /**
     * 入力項目(時刻)render有無
     *
     * @param renderInputTime the renderInputTime to set
     */
    public void setRenderInputTime(boolean renderInputTime) {
        this.renderInputTime = renderInputTime;
    }

    /**
     * 入力項目カスタムスタイル
     * @return the customStyleInput
     */
    public String getCustomStyleInput() {
        return customStyleInput;
    }

    /**
     * 入力項目カスタムスタイル
     * @param customStyleInput the customStyleInput to set
     */
    public void setCustomStyleInput(String customStyleInput) {
        this.customStyleInput = customStyleInput;
    }

    /**
     * ラベル表示render有無
     *
     * @return the isRenderOutputLabel
     */
    public boolean isRenderOutputLabel() {
        return renderOutputLabel;
    }

    /**
     * ラベル表示render有無
     *
     * @param renderOutputLabel the renderOutputLabel to set
     */
    public void setRenderOutputLabel(boolean renderOutputLabel) {
        this.renderOutputLabel = renderOutputLabel;
    }

    /**
     * 入力項目(入力可能ドロップダウンリスト)render有無
     * 
     * @return the isRenderInputSelectAvailable
     */
    public boolean isRenderInputSelectAvailable() {
        return renderInputSelectAvailable;
    }

    /**
     * 入力項目(入力可能ドロップダウンリスト)render有無
     * 
     * @param renderInputSelectAvailable the renderInputSelectAvailable to set
     */
    public void setRenderInputSelectAvailable(boolean renderInputSelectAvailable) {
        this.renderInputSelectAvailable = renderInputSelectAvailable;
    }

    /**
     * 表示ﾗﾍﾞﾙ3文字ｻｲｽﾞ
     *
     * @return the fontSize3
     */
    public int getFontSize3() {
        return fontSize3;
    }

    /**
     * 表示ﾗﾍﾞﾙ3文字ｻｲｽﾞ
     *
     * @param fontSize3 the fontSize3 to set
     */
    public void setFontSize3(int fontSize3) {
        this.fontSize3 = fontSize3;
    }

    /**
     * 表示ﾗﾍﾞﾙ3文字色
     *
     * @return the fontColor3
     */
    public String getFontColor3() {
        return fontColor3;
    }

    /**
     * 表示ﾗﾍﾞﾙ3文字色
     *
     * @param fontColor3 the fontColor3 to set
     */
    public void setFontColor3(String fontColor3) {
        this.fontColor3 = fontColor3;
    }

    /**
     * 表示ﾗﾍﾞﾙ3背景色
     *
     * @return the backColor3
     */
    public String getBackColor3() {
        return backColor3;
    }

    /**
     * 表示ﾗﾍﾞﾙ3背景色
     *
     * @param backColor3 the backColor3 to set
     */
    public void setBackColor3(String backColor3) {
        this.backColor3 = backColor3;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ工程名
     *
     * @return the jokenKoteiMei
     */
    public String getJokenKoteiMei() {
        return jokenKoteiMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ工程名
     *
     * @param jokenKoteiMei the jokenKoteiMei to set
     */
    public void setJokenKoteiMei(String jokenKoteiMei) {
        this.jokenKoteiMei = jokenKoteiMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ項目名
     *
     * @return the jokenKomokuMei
     */
    public String getJokenKomokuMei() {
        return jokenKomokuMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ項目名
     *
     * @param jokenKomokuMei the jokenKomokuMei to set
     */
    public void setJokenKomokuMei(String jokenKomokuMei) {
        this.jokenKomokuMei = jokenKomokuMei;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ管理項目
     *
     * @return the jokenKanriKomoku
     */
    public String getJokenKanriKomoku() {
        return jokenKanriKomoku;
    }

    /**
     * 条件ﾃｰﾌﾞﾙ管理項目
     *
     * @param jokenKanriKomoku the jokenKanriKomoku to set
     */
    public void setJokenKanriKomoku(String jokenKanriKomoku) {
        this.jokenKanriKomoku = jokenKanriKomoku;
    }

    /**
     * 規格情報ﾊﾟﾀｰﾝ
     *
     * @return the standardPattern
     */
    public String getStandardPattern() {
        return standardPattern;
    }

    /**
     * 規格情報ﾊﾟﾀｰﾝ
     *
     * @param standardPattern the standardPattern to set
     */
    public void setStandardPattern(String standardPattern) {
        this.standardPattern = standardPattern;
    }

    /**
     * ﾎﾞﾀﾝID
     *
     * @return the linkButtonId
     */
    public String getLinkButtonId() {
        return linkButtonId;
    }

    /**
     * ﾎﾞﾀﾝID
     *
     * @param linkButtonId the linkButtonId to set
     */
    public void setLinkButtonId(String linkButtonId) {
        this.linkButtonId = linkButtonId;
    }
    
    /**
     * 規格値
     *
     * @return the kikakuChi
     */
    public String getKikakuChi() {
        return kikakuChi;
    }

    /**
     * 規格値
     *
     * @param kikakuChi the kikakuChi to set
     */
    public void setKikakuChi(String kikakuChi) {
        this.kikakuChi = kikakuChi;
    }

    /**
     * 入力値
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * 入力値
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
        //this.checkByte();
    }

    /**
     * 入力項目背景色(通常)
     *
     * @return the backColorInputDefault
     */
    public String getBackColorInputDefault() {
        return backColorInputDefault;
    }

    /**
     * 入力項目背景色(通常)
     *
     * @param backColorInputDefault the backColorInputDefault to set
     */
    public void setBackColorInputDefault(String backColorInputDefault) {
        this.backColorInputDefault = backColorInputDefault;
    }

    /**
     * 項目ｲﾝﾃﾞｯｸｽ
     *
     * @return the itemIndex
     */
    public int getItemIndex() {
        return itemIndex;
    }

    /**
     * 項目ｲﾝﾃﾞｯｸｽ
     *
     * @param itemIndex the itemIndex to set
     */
    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    /**
     * (数値入力制御用)小数部桁数
     *
     * @return 小数部桁数
     */
    public String getNumberDecimalPlaces() {
        if (StringUtil.isEmpty(this.inputLengthDec) || !isNumeric(this.inputLengthDec)) {
            return "0";
        } else {
            return this.inputLengthDec;
        }
    }

    /**
     * (数値入力制御用)最大値
     *
     * @return 最大値
     */
    public String getNumberMaxValue() {
        int intPlaces = new BigDecimal(this.inputLength).intValue();
        int decPlaces = new BigDecimal(this.getNumberDecimalPlaces()).intValue();

        String result = StringUtils.repeat("9", intPlaces);
        if (0 < decPlaces) {
            result = result + "." + StringUtils.repeat("9", decPlaces);
        }

        return result;
    }

    /**
     * (数値入力制御用)最小値
     *
     * @return 最小値
     */
    public String getNumberMinValue() {
        return "-" + this.getNumberMaxValue();
    }

    /**
     * プルダウン/ラジオボタン候補値取得
     *
     * @return プルダウン/ラジオボタン候補値
     */
    public List<String> getSelectMenuItem() {
        if (StringUtil.isEmpty(this.inputList)) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(this.inputList.split(","));
        }
    }

    /**
     * 数値チェック
     *
     * @param value 判定値
     * @return 数値変換可能な場合true
     */
    private boolean isNumeric(String value) {
        try {
            BigDecimal decValue = new BigDecimal(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * 画面表示用桁数を返します。
     *
     * @return 画面表示用桁数
     */
    public String getDisplayMaxLength() {
        int maxSeisu = 0;
        int maxSyosu = 0;
        if (NumberUtil.isIntegerNumeric(this.getInputLength())) {
            maxSeisu = Integer.parseInt(this.getInputLength());
        }
        if (NumberUtil.isIntegerNumeric(this.getInputLengthDec())) {
            maxSyosu = Integer.parseInt(this.getInputLengthDec());
        }
        return String.valueOf(maxSyosu == 0 ? maxSeisu : maxSeisu + maxSyosu + 1);
    }

    /**
     * 文字列をバイトでカットします。
     */
    public void checkByte() {
        // 切り捨て処理
        this.value = StringUtil.left(this.value, Integer.parseInt(this.getDisplayMaxLength()));
        // 小文字を大文字に変換(StringUtil.leftでnullは返却されない)
        this.value = this.value.toUpperCase();
    }
}
