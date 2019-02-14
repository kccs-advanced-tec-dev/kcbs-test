/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;

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
 * DB操作ユーティリティクラス
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class DBUtil {

    /**
     * SQLのIN条件文(PreparedStatement)を取得します<BR>
     *
     * @param column 項目名
     * @param count IN条件の件数
     * @return IN条件文
     */
    public static String getInConditionPreparedStatement(String column, int count) {
        if (StringUtil.isEmpty(column) || count < 1) {
            return "";
        }

        String placeHolder = "?";
        for (int i = 2; i <= count; i++) {
            placeHolder += ",?";
        }

        return MessageFormat.format(" {0} IN ({1}) ", column, placeHolder);
    }

    /**
     * SQL実行ログを出力します。
     *
     * @param execSQL 実行対象SQL
     * @param bindParam バインド文字列群
     * @param loggerClass ログクラス
     */
    public static void outputSQLLog(String execSQL, Object[] bindParam, Logger loggerClass) {
        //パラメータログメッセージ作成
        String bindParameterString = "";
        if (bindParam.length > 0) {
            bindParameterString = "BindParameter:{0}";
            for (int i = 0; i < bindParam.length; i++) {
                if (0 < i) {
                    bindParameterString = bindParameterString + "\t{" + (i) + "}";
                }

                bindParam[i] = getNumberTypePlainString(bindParam[i]);
            }
        }

        //ログ出力
        loggerClass.log(Level.INFO, "SQL:{0}", execSQL);
        if (!"".equals(bindParameterString)) {
            loggerClass.log(Level.INFO, bindParameterString, bindParam);
        }
    }

    /**
     * 数値型の場合、BigDecimalに変換しPlainStringを返します。
     *
     * @param obj オブジェクト
     * @return 変換値(数値型以外は元の値)
     */
    private static Object getNumberTypePlainString(Object obj) {
        Object returnObj = obj;
        if (obj instanceof Short || obj instanceof Integer || obj instanceof Long
                || obj instanceof Double || obj instanceof Float || obj instanceof BigDecimal) {
            try {
                BigDecimal decValue = new BigDecimal(obj.toString());
                returnObj = decValue.toPlainString();
            } catch (NumberFormatException e) {
                return returnObj;
            }
        }
        return returnObj;
    }

    /**
     * 数値の場合、Integerに変換する。その他の場合0を返却する。
     *
     * @param value 文字列
     * @return 数値変換後のオブジェクト
     */
    public static Object stringToIntObject(String value) {
        if (NumberUtil.isIntegerNumeric(value)) {
            return Integer.parseInt(value);
        } else {
            return 0;
        }
    }

    /**
     * 数値の場合、BigDecimalに変換する。その他の場合0を返却する。
     *
     * @param value 文字列
     * @return 数値変換後のオブジェクト
     */
    public static Object stringToBigDecimalObject(String value) {
        if (NumberUtil.isNumeric(value)) {
            return new BigDecimal(value);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 日付文字列⇒Dateオブジェクト変換<br>
     * DB登録用にDateがNullだった場合、"0000-00-00 00:00:00"を返します。
     *
     * @param yyMMdd 年月日
     * @param HHmm 時分
     * @return 変換後のデータ
     */
    public static Object stringToDateObject(String yyMMdd, String HHmm) {
        Date date = DateUtil.convertStringToDate(yyMMdd, HHmm);
        return null == date ? "0000-00-00 00:00:00" : date;
    }

    /**
     * DB登録用に文字列がNULL値の場合、空の値を返します。<br>
     * NULL以外の場合、そのままの値を返します。<br>
     *
     * @param str 文字列
     * @return 変換値
     */
    public static Object stringToStringObject(String str) {
        return StringUtil.nullToBlank(str);
    }
    
    /**
     * 数値の場合、Integerに変換する。その他の場合Nullを返却する。
     *
     * @param value 文字列
     * @return 数値変換後のオブジェクト
     */
    public static Object stringToIntObjectDefaultNull(String value) {
        if (NumberUtil.isIntegerNumeric(value)) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }

    /**
     * 数値の場合、BigDecimalに変換する。その他の場合Nullを返却する。
     *
     * @param value 文字列
     * @return 数値変換後のオブジェクト
     */
    public static Object stringToBigDecimalObjectDefaultNull(String value) {
        if (NumberUtil.isNumeric(value)) {
            return new BigDecimal(value);
        } else {
            return null;
        }
    }

    /**
     * 日付文字列⇒Dateオブジェクト変換<br>
     * DB登録用にDateがNullだった場合、Nullを返します。
     *
     * @param yyMMdd 年月日
     * @param HHmm 時分
     * @return 変換後のデータ
     */
    public static Object stringToDateObjectDefaultNull(String yyMMdd, String HHmm) {
        return DateUtil.convertStringToDate(yyMMdd, HHmm);
    }

    /**
     * DB登録用に文字列が空値の場合、Nullを返します。<br>
     * 空値以外の場合、そのままの値を返します。<br>
     *
     * @param str 文字列
     * @return 変換値
     */
    public static Object stringToStringObjectDefaultNull(String str) {
        if(StringUtil.isEmpty(str)){
            return null;
        }
        return str;
    }
    
    
    /**
     * トランザクション開始
     * @param con コネクション
     * @return コネクション
     * @throws SQLException 例外エラー
     */
    public static Connection transactionStart(Connection con) throws SQLException{
        con.setAutoCommit(false);
        return con;
    }
    
    /**
     * コネクションロールバック処理
     *
     * @param con コネクション
     * @param loggerClass ログクラス
     */
    public static void rollbackConnection(Connection con, Logger loggerClass) {
        try {
            DbUtils.rollback(con);
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, loggerClass);
        }
        DbUtils.closeQuietly(con);
    }
    
    /**
     * LIKE検索用にワイルドカード文字列("%","_")にエスケープ文字("\")を付与します
     * @param parameter 検索文字列
     * @return エスケープ処理後の文字列
     */
    public static String escapeString(String parameter) {
        // "\"はreplaceAll内で置換文字列内のリテラル文字をエスケープするのに使用されるため、
        // replaceAll("\\\\", "\\\\\\\\") としている。
        // "\"→"\\" として処理される。
        return parameter.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("\\\\", "\\\\\\\\");
    }
}
