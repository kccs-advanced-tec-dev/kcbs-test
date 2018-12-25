/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo901;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2018/12/12<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * エラーメッセージ情報
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/12/12
 */
public class ErrorMessageInfo {

    /**
     * エラーメッセージID
     */
    private String errorMessageId;

    /**
     * エラーメッセージ
     */
    private String errorMessage;

    /**
     * 背景色を変更するかどうかの判定
     */
    private Boolean isChangeBackColor;
    /**
     * ページ変更先の項目インデックス
     */
    private int pageChangeItemIndex;

    /**
     * エラーアイテム情報リスト
     */
    private List<ErrorItemInfo> errorItemInfoList;

    /**
     * コンストラクタ
     */
    public ErrorMessageInfo() {
        this.isChangeBackColor = false;
        this.pageChangeItemIndex = -1;
        this.errorItemInfoList = new ArrayList<>();
    }

    /**
     * コンストラクタ
     *
     * @param errorMessage エラーメッセージ
     */
    public ErrorMessageInfo(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isChangeBackColor = false;
        this.pageChangeItemIndex = -1;
        this.errorItemInfoList = new ArrayList<>();
    }

    /**
     * エラーメッセージID
     *
     * @return the errorMessageId
     */
    public String getErrorMessageId() {
        return errorMessageId;
    }

    /**
     * エラーメッセージID
     *
     * @param errorMessageId the errorMessageId to set
     */
    public void setErrorMessageId(String errorMessageId) {
        this.errorMessageId = errorMessageId;
    }

    /**
     * エラーメッセージ
     *
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * エラーメッセージ
     *
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 背景色を変更するかどうかの判定
     *
     * @return the isChangeBackColor
     */
    public Boolean getIsChangeBackColor() {
        return isChangeBackColor;
    }

    /**
     * 背景色を変更するかどうかの判定
     *
     * @param isChangeBackColor the isChangeBackColor to set
     */
    public void setIsChangeBackColor(Boolean isChangeBackColor) {
        this.isChangeBackColor = isChangeBackColor;
    }

    /**
     * 背景色を変更するかどうかの判定
     *
     * @return the pageChangeItemIndex
     */
    public int getPageChangeItemIndex() {
        return pageChangeItemIndex;
    }

    /**
     * @param pageChangeItemIndex the pageChangeItemIndex to set
     */
    public void setPageChangeItemIndex(int pageChangeItemIndex) {
        this.pageChangeItemIndex = pageChangeItemIndex;
    }

    /**
     * エラーアイテム情報リスト
     *
     * @return the errorItemInfoList
     */
    public List<ErrorItemInfo> getErrorItemInfoList() {
        return errorItemInfoList;
    }

    /**
     * エラーアイテム情報リスト
     *
     * @param errorItemInfoList the errorItemInfoList to set
     */
    public void setErrorItemInfoList(List<ErrorItemInfo> errorItemInfoList) {
        this.errorItemInfoList = errorItemInfoList;
    }

    /**
     * エラーアイテム情報
     */
    public class ErrorItemInfo {

        /**
         * 項目ID
         */
        private String itemId;

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
    }

}
