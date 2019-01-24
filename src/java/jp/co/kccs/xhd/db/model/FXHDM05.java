/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.db.model;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/11<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSYNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * FXHDM05(チェック処理マスタ)のモデルクラスです。
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/11
 */
public class FXHDM05 {

    /**
     * 画面ID
     */
    private String gamenId;
    /**
     * ﾎﾞﾀﾝID
     */
    private String buttonId;
    /**
     * 項目ID
     */
    private String itemId;
    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     */
    private String checkPattern;
    /**
     * ﾁｪｯｸ処理順
     */
    private int checkNo;

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
     * ﾎﾞﾀﾝID
     *
     * @return the buttonId
     */
    public String getButtonId() {
        return buttonId;
    }

    /**
     * ﾎﾞﾀﾝID
     *
     * @param buttonId the buttonId to set
     */
    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
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
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     *
     * @return the checkPattern
     */
    public String getCheckPattern() {
        return checkPattern;
    }

    /**
     * ﾁｪｯｸﾊﾟﾀｰﾝ
     *
     * @param checkPattern the checkPattern to set
     */
    public void setCheckPattern(String checkPattern) {
        this.checkPattern = checkPattern;
    }

    /**
     * ﾁｪｯｸ処理順
     *
     * @return the checkNo
     */
    public int getCheckNo() {
        return checkNo;
    }

    /**
     * ﾁｪｯｸ処理順
     *
     * @param checkNo the checkNo to set
     */
    public void setCheckNo(int checkNo) {
        this.checkNo = checkNo;
    }

}
