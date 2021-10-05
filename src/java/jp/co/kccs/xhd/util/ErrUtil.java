/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;

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
 * 変更理由	エラー項目に背景色を付ける処理を追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * エラー関連のUtilクラスです。
 *
 * @author KCCS D.Yanagida
 * @since 2018/05/06
 */
public class ErrUtil {

    /**
     * エラー発生時背景色
     */
    public static final String ERR_BACK_COLOR = "#FFB6C1";
    
    /**
     * コンストラクタ--
     */
    private ErrUtil() {

    }

    /**
     * エラー時カラーの設定・解除
     *
     * @param id 項目ID
     */
    public static void setErrStyle(String id) {
        // コンテキストから項目を取得
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HtmlInputText input = (HtmlInputText) facesContext.getViewRoot().findComponent(id);

        // テキスト以外はリターン
        if (null == input) {
            return;
        }

        // エラースタイルをセット
        String nowStyle = StringUtil.nullToBlank(input.getStyle());
        input.setStyle(addErrStyle(nowStyle));

    }

    /**
     * エラー時カラーの設定・解除
     *
     * @param id 項目ID
     * @param defaultColor デフォルトカラー(エラー時は不要)
     */
    public static void releaseErrStyle(String id, String defaultColor) {
        // コンテキストから項目を取得
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HtmlInputText input = (HtmlInputText) facesContext.getViewRoot().findComponent(id);

        // テキスト以外はリターン
        if (null == input) {
            return;
        }

        // エラースタイルを削除
        String nowStyle = StringUtil.nullToBlank(input.getStyle());
        input.setStyle(removeErrStyle(nowStyle, defaultColor));
    }

    /**
     * エラー時のスタイル(background-color)を取得します。
     *
     * @param style スタイル(今時点のスタイル)
     * @return スタイル(エラーカラーセット)
     */
    private static String addErrStyle(String style) {

        String errStyle = style;
        // スタイル内から"background-color"のスタイルを削除
        errStyle = removeStyle(errStyle, "background-color");

        // スタイルを追加
        errStyle = errStyle + "background-color : #F6CEF5;";
        return errStyle;
    }

    /**
     * エラー時のスタイル(background-color)を削除します。 また、デフォルトのカラー指定があればスタイルを設定します。
     *
     * @param style スタイル(今時点のスタイル)
     * @return スタイル(エラーカラーセット)
     */
    private static String removeErrStyle(String style, String defaultBackColor) {

        String remErrStyle = style;
        // スタイル内から"background-color"のスタイルを削除
        remErrStyle = removeStyle(remErrStyle, "background-color");

        // デフォルトカラーの設定
        if (null != defaultBackColor && !"".equals(defaultBackColor)) {
            // スタイルを追加
            remErrStyle = remErrStyle + "background-color : " + defaultBackColor;
        }

        return remErrStyle;
    }

    /**
     * スタイルの削除処理 ※スタイルが";"で区切られていることが前提です。
     *
     * @param style style スタイル(今時点のスタイル)
     * @param removeStyle 削除したいスタイル
     * @return スタイル
     */
    private static String removeStyle(String style, String removeStyle) {

        String returnStyle = style;
        int startIndex = returnStyle.toLowerCase().indexOf(removeStyle.toLowerCase());
        if (-1 < startIndex) {

            // スタイルの区切り位置を取得
            int strCount = returnStyle.substring(startIndex).indexOf(';');
            // 区切り位置が見つからない場合、編集無しでリターン
            if (-1 == strCount) {
                return returnStyle;
            }

            // 削除したいスタイルの範囲を取得
            String removeStylel = returnStyle.substring(startIndex, startIndex + strCount + 1);

            // 削除したいスタイルの範囲を削除
            returnStyle = returnStyle.replaceAll(removeStylel, "");
        }
        return returnStyle;
    }

    /**
     * SQL実行時サーバログ出力処理
     *
     * @param errMessage エラーメッセージ
     * @param ex スタックトレース
     * @param loggerClass ログクラス
     */
    public static void outputErrorLog(String errMessage, Exception ex, Logger loggerClass) {
        loggerClass.log(Level.SEVERE, errMessage, ex);

        //SQLExceptionの場合のみ、エラーコードとSQLStateをﾛｸﾞ出力する
        if (ex instanceof SQLException) {
            SQLException sqlEx = (SQLException) ex;
            loggerClass.log(Level.SEVERE, "ErrorCode: {0}", sqlEx.getErrorCode());
            loggerClass.log(Level.SEVERE, "SQLState: {0}", sqlEx.getSQLState());
        }
    }

    /**
     * 項目の背景色を設定します。
     *
     * @param itemList 項目データ
     * @param errorMessageInfo エラーメッセージ情報
     */
    public static void setErrorItemBackColor(List<FXHDD01> itemList, ErrorMessageInfo errorMessageInfo) {
        
        List<FXHDD01> errorItemList;
        for (ErrorMessageInfo.ErrorItemInfo errorItemInfo : errorMessageInfo.getErrorItemInfoList()) {

            errorItemList
                    = itemList.stream().filter(n -> errorItemInfo.getItemId().equals(n.getItemId())).collect(Collectors.toList());

            for (FXHDD01 fxhdd01 : errorItemList) {
                fxhdd01.setBackColorInput(ERR_BACK_COLOR);
            }
        }
    }
    
    /**
     * 項目の背景色を設定します。
     *
     * @param itemList 項目データ
     * @param kikakuchiInputErrorInfoList 規格エラー情報
     * @return true:規格エラー情報に項目が存在、false:規格エラー情報に項目が存在しない
     */
    public static boolean setErrorItemBackColor(List<FXHDD01> itemList, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {
        boolean hasItemFlag = false;
        if (itemList == null) {
            return hasItemFlag;
        }
        List<FXHDD01> errorItemList;
        for (KikakuchiInputErrorInfo errorItemInfo : kikakuchiInputErrorInfoList) {

            errorItemList
                    = itemList.stream().filter(n -> errorItemInfo.getItemId().equals(n.getItemId())).collect(Collectors.toList());

            for (FXHDD01 fxhdd01 : errorItemList) {
                fxhdd01.setBackColorInput(ERR_BACK_COLOR);
                if(!hasItemFlag){
                    hasItemFlag = true;
                }
            }
        }
        return hasItemFlag;
    }
    
    
    
    
    
}
