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
 * FXHDM02(表示設定マスタ)のモデルクラスです。
 * 
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class FXHDM02 {
    /**
     * 設定ID
     */
    private String settingId;
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
    private String bgColor;
    /**
     * 画面名
     */
    private String formName;
    
    /**
     * 設定ID
     * @return the settingId
     */
    public String getSettingId() {
        return settingId;
    }

    /**
     * 設定ID
     * @param settingId the settingId to set
     */
    public void setSettingId(String settingId) {
        this.settingId = settingId;
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
     * @return the bgColor
     */
    public String getBgColor() {
        return bgColor;
    }

    /**
     * 背景色
     * @param bgColor the bgColor to set
     */
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * 画面名
     * @return the formName
     */
    public String getFormName() {
        return formName;
    }

    /**
     * 画面名
     * @param formName the formName to set
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }
}
