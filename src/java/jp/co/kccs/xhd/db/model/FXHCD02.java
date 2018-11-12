/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/04/24<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCCS D.Yanagida<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * FXHCD02(ボタンパラメータマスタ)のモデルクラスです。
 * 
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class FXHCD02 {
    /**
     * ボタンID
     */
    private String buttonId;
    /**
     * ボタンNo 
     */
    private int buttonNo;
    /**
     * ボタン表示場所
     */
    private String buttonLocation;
    /**
     * ボタン名
     */
    private String buttonName;
    /**
     * 文字サイズ
     */
    private int fontSize;
    /**
     * 文字色
     */
    private String fontColor;
    /**
     * 背景色
     */
    private String backColor;
    /**
     * render有無
     */
    private boolean render = true;
    /**
     * 有効・無効
     */
    private boolean enabled = true;

    /**
     * ボタンID
     * @return the buttonId
     */
    public String getButtonId() {
        return buttonId;
    }

    /**
     * ボタンID
     * @param buttonId the buttonId to set
     */
    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    /**
     * ボタンNo
     * @return the buttonNo
     */
    public int getButtonNo() {
        return buttonNo;
    }

    /**
     * ボタンNo
     * @param buttonNo the buttonNo to set
     */
    public void setButtonNo(int buttonNo) {
        this.buttonNo = buttonNo;
    }

    /**
     * ボタン表示場所
     * @return the buttonLocation
     */
    public String getButtonLocation() {
        return buttonLocation;
    }

    /**
     * ボタン表示場所
     * @param buttonLocation the buttonLocation to set
     */
    public void setButtonLocation(String buttonLocation) {
        this.buttonLocation = buttonLocation;
    }

    /**
     * ボタン名
     * @return the buttonName
     */
    public String getButtonName() {
        return buttonName;
    }

    /**
     * ボタン名
     * @param buttonName the buttonName to set
     */
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    /**
     * 文字サイズ
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * 文字サイズ
     * @param fontSize the fontSize to set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * 文字色
     * @return the fontColor
     */
    public String getFontColor() {
        return fontColor;
    }

    /**
     * 文字色
     * @param fontColor the fontColor to set
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * 背景色
     * @return the backColor
     */
    public String getBackColor() {
        return backColor;
    }

    /**
     * 背景色
     * @param backColor the backColor to set
     */
    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    /**
     * render有無
     * @return the render
     */
    public boolean getRender() {
        return render;
    }

    /**
     * render有無
     * @param render the render to set
     */
    public void setRender(boolean render) {
        this.render = render;
    }

    /**
     * 有効・無効
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 有効・無効
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
