/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * 数値操作用に使用する関数群です。
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class NumberUtil {

    /**
     * コンストラクタ
     */
    private NumberUtil() {
    }

    /**
     * 数値チェック
     *
     * @param value 判定値
     * @return 数値変換可能な場合true
     */
    public static boolean isNumeric(String value) {
        String regex = "^-?(0|[1-9]\\d*)(\\.\\d+|)$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }

    /**
     * 日付用数値チェック
     *
     * @param value 日付
     * @return 数値の場合true
     */
    public static boolean isDateNumber(String value) {
        String regex = "^([0-9]{2})$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }

    /**
     * 整数チェック
     *
     * @param value 判定値
     * @return 数値に小数が含まれている場合false
     */
    public static boolean isIntegerNumeric(String value) {
        try {
            Integer intValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * 数値の桁数チェック
     *
     * @param value 判定対象数値
     * @param maxSeisu 整数部の最大桁数
     * @param maxSyosu 小数部の最大桁数
     * @return 数値が範囲外(エラー)の場合 false
     */
    public static boolean isValidDigits(BigDecimal value, int maxSeisu, int maxSyosu) {
        BigDecimal bigSeisu = value.setScale(0, RoundingMode.DOWN);
        return maxSeisu >= bigSeisu.precision() && maxSyosu >= value.stripTrailingZeros().scale();
    }

    /**
     * 数値の桁数チェック
     *
     * @param value 判定対象数値
     * @param maxSeisu 整数部の最大桁数
     * @param maxSyosu 小数部の最大桁数
     * @return 桁数が一致しない場合 false
     */
    public static boolean isSameValidDigits(BigDecimal value, int maxSeisu, int maxSyosu) {
        BigDecimal bigSeisu = value.setScale(0, RoundingMode.DOWN);
        return maxSeisu == bigSeisu.precision() && maxSyosu == value.stripTrailingZeros().scale();
    }
    
    /**
     * 数値の範囲チェック
     *
     * @param value 判定対象数値
     * @param maxValue 最大値
     * @param minValue 最小値
     * @return 数値が範囲外(エラー)の場合 false
     */
    public static boolean isValidRange(BigDecimal value, BigDecimal maxValue, BigDecimal minValue) {
        if (null == value) {
            return false;
        }
        return value.compareTo(maxValue) <= 0 && value.compareTo(minValue) >= 0;
    }

    /**
     * オブジェクトが数値だった場合、整数型に変換します。<br>
     * それ以外の場合、nullを返却します。
     *
     * @param numObject 数値
     * @return 数値オブジェクト
     */
    public static String convertIntData(Object numObject) {
        if (null == numObject) {
            return null;
        }
        if (!isNumeric(numObject.toString())) {
            return null;
        }

        BigDecimal num = new BigDecimal(numObject.toString());

        return String.valueOf(num.intValue());
    }
}
