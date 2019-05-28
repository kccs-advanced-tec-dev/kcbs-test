/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

import java.io.Serializable;

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
 * 変更日	2018/11/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	ﾛｯﾄｶｰﾄﾞ電子化対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * FXHDM01(メニューマスタ)のモデルクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class FXHDM01 implements Serializable {

    /**
     * 画面ID
     */
    private String formId;
    /**
     * 画面タイトル
     */
    private String formTitle;
    /**
     * タイトル表示設定
     */
    private String titleSetting;
    /**
     * ﾒﾆｭｰNo
     */
    private int menuNo;
    /**
     * メニュー名
     */
    private String menuName;
    /**
     * URLパターン
     */
    private String linkChar;
    /**
     * メニューパラメータ
     */
    private String menuParam;
    /**
     * メニューコメント
     */
    private String menuComment;
    /**
     * 画面クラス名
     */
    private String formClassName;
    /**
     * 表示件数
     */
    private int hyojiKensu;
    /**
     * 画面ID(前工程)
     */
    private String maeKoteiFormId;

    /**
     * 画面ID
     *
     * @return the formId
     */
    public String getFormId() {
        return formId;
    }

    /**
     * 画面ID
     *
     * @param formId the formId to set
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    /**
     * 画面タイトル
     *
     * @return the formTitle
     */
    public String getFormTitle() {
        return formTitle;
    }

    /**
     * 画面タイトル
     *
     * @param formTitle the formTitle to set
     */
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    /**
     * タイトル表示設定
     *
     * @return the titleSetting
     */
    public String getTitleSetting() {
        return titleSetting;
    }

    /**
     * タイトル表示設定
     *
     * @param titleSetting the titleSetting to set
     */
    public void setTitleSetting(String titleSetting) {
        this.titleSetting = titleSetting;
    }

    /**
     * メニューNo
     *
     * @return the menuNo
     */
    public int getMenuNo() {
        return menuNo;
    }

    /**
     * メニューNo
     *
     * @param menuNo the menuNo to set
     */
    public void setMenuNo(int menuNo) {
        this.menuNo = menuNo;
    }

    /**
     * メニュー名
     *
     * @return the menuName
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * メニュー名
     *
     * @param menuName the menuName to set
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * URLパターン
     *
     * @return the linkChar
     */
    public String getLinkChar() {
        return linkChar;
    }

    /**
     * URLパターン
     *
     * @param linkChar the linkChar to set
     */
    public void setLinkChar(String linkChar) {
        this.linkChar = linkChar;
    }

    /**
     * メニューパラメータ
     *
     * @return the menuParam
     */
    public String getMenuParam() {
        return menuParam;
    }

    /**
     * メニューパラメータ
     *
     * @param menuParam the menuParam to set
     */
    public void setMenuParam(String menuParam) {
        this.menuParam = menuParam;
    }

    /**
     * メニューコメント
     *
     * @return the menuComment
     */
    public String getMenuComment() {
        return menuComment;
    }

    /**
     * メニューコメント
     *
     * @param menuComment the menuComment to set
     */
    public void setMenuComment(String menuComment) {
        this.menuComment = menuComment;
    }

    /**
     * 画面クラス名
     *
     * @return the formClassName
     */
    public String getFormClassName() {
        return formClassName;
    }

    /**
     * 画面クラス名
     *
     * @param formClassName the formClassName to set
     */
    public void setFormClassName(String formClassName) {
        this.formClassName = formClassName;
    }

    /**
     * 表示件数
     *
     * @return the hyojiKensu
     */
    public int getHyojiKensu() {
        return hyojiKensu;
    }

    /**
     * 表示件数
     *
     * @param hyojiKensu the hyojiKensu to set
     */
    public void setHyojiKensu(int hyojiKensu) {
        this.hyojiKensu = hyojiKensu;
    }

    /**
     * 画面ID(前工程)
     * 
     * @return the maeKoteiFormId
     */
    public String getMaeKoteiFormId() {
        return maeKoteiFormId;
    }

    /**
     * 画面ID(前工程)
     * @param maeKoteiFormId the maeKoteiFormId to set
     */
    public void setMaeKoteiFormId(String maeKoteiFormId) {
        this.maeKoteiFormId = maeKoteiFormId;
    }
}
