/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;

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
 * ===============================================================================<br>
 */
/**
 * ﾒｯｾｰｼﾞ処理関連ﾕｰﾃｨﾘﾃｨｸﾗｽ
 */
public class MessageUtil {

    /**
     * ｺﾝｽﾄﾗｸﾀ
     */
    private MessageUtil() {
    }

    /**
     * ﾒｯｾｰｼﾞ取得処理
     *
     * @param id ﾒｯｾｰｼﾞID
     * @param params ﾊﾟﾗﾒｰﾀ
     * @return ﾒｯｾｰｼﾞ
     */
    public static String getMessage(String id, Object... params) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle messages = context.getApplication().getResourceBundle(context, "myMsg");

        String message = messages.getString(id);
        MessageFormat mf = new MessageFormat(message);

        return mf.format(params);
    }

    /**
     * メッセージ情報取得処理
     * @param messageId メッセージID
     * @param changeBackColor 背景色変更判定
     * @param changePage 項目リストのページ変更判定
     * @param errItemList エラー項目リスト
     * @param errorMessageParams エラーメッセージパラメータ
     * @return エラーメッセージ情報
     */
    public static ErrorMessageInfo getErrorMessageInfo(String messageId, boolean changeBackColor, boolean changePage, List<FXHDD01> errItemList, Object... errorMessageParams) {

        // エラーメッセージ情報
        ErrorMessageInfo errorMessageInfo = new ErrorMessageInfo();

        errorMessageInfo.setErrorMessageId(messageId); // エラーメッセージID
        errorMessageInfo.setErrorMessage(getMessage(messageId, errorMessageParams)); // エラーメッセージ
        errorMessageInfo.setIsChangeBackColor(changeBackColor); // 背景色を変更するかどうか

        // エラー対象の項目の情報を戻り値にセットする。
        List<ErrorMessageInfo.ErrorItemInfo> errorItemInfoList = new ArrayList<>();
        if (errItemList != null && !errItemList.isEmpty()) {
            // 項目一覧のページの変更をする場合
            if (changePage) {
                // エラー項目の先頭のインデックスを設定する。
                errorMessageInfo.setPageChangeItemIndex(errItemList.get(0).getItemIndex()); //項目Index
            }

            for (FXHDD01 fxhdd01 : errItemList) {
                ErrorMessageInfo.ErrorItemInfo errorItemInfo = errorMessageInfo.new ErrorItemInfo();
                errorItemInfo.setItemId(fxhdd01.getItemId()); //項目ID
                errorItemInfoList.add(errorItemInfo);
            }

            errorMessageInfo.setErrorItemInfoList(errorItemInfoList);
        }

        return errorMessageInfo;
    }
}
