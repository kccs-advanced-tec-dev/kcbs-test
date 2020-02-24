/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import org.apache.commons.lang3.time.DateUtils;

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
 * 変更日       2019/11/12<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       SYSNAVI K.Hisanaga<br>
 * 変更理由     日付文字列をIntegerに変換する処理を追加<br>
 * <br>
 * 変更日       2020/02/21<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       SYSNAVI K.Hisanaga<br>
 * 変更理由     "yyMMddHHmmss"形式での日付変換処理を追加<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * 共通日付変換ｸﾗｽ
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class DateUtil {
    private static final Logger LOGGER = Logger.getLogger(ValidateUtil.class.getName());
    
    // 文字コード
    private static final String CHARSET = "MS932";
    
    // 数値日付
    static final int D_19800101 = 722815;
    
    // フォーマット(yyMMdd)
    public static final String YYMMDD = "yyMMdd";

    // フォーマット(yy.MM.dd)
    public static final String YY_MM_DD = "yy.MM.dd";

    // フォーマット(yy.MM.dd)
    public static final String YYYY_MM_DD = "yyyy.MM.dd";
    
    // フォーマット(yyyy.MM.dd)
    public static final String YYYYMMDD_WITH_SLASH = "yyyy/MM/dd";
    
    // フォーマット(hhmmss)
    public static final String HHMMSS = "HHmmss";
    
    // フォーマット(hhmm)
    public static final String HHMM = "HHmm";
    
    // フォーマット(hh:mm:ss)
    public static final String HH_MM_SS = "HH:mm:ss";
    
    // フォーマット(hh:mm)
    public static final String HH_MM = "HH:mm";

    /**
     * ｲﾝｽﾀﾝｽ化させない。
     */
    private DateUtil() {
    }

    /**
     * 日付⇒数値(int)変換
     *
     * @param date Date型の日付
     * @return int型の日付
     */
    public static int convDateToInteger(Date date) {
        // ｶﾚﾝﾀﾞｰｸﾗｽのｲﾝｽﾀﾝｽを取得
        Calendar cal = Calendar.getInstance();
        // 1980年1月1日を設定
        cal.set(1980, 0, 1);
        // 日付の差分を取得
        Date dateComp = cal.getTime();
        int dayDiff = diffDays(dateComp, date);

        // 日付の差分を基準値に加算した値を返却する
        return D_19800101 + dayDiff;
    }

    /**
     * 数値(int)⇒日付変換
     *
     * @param date int型の日付
     * @return Date型の日付
     */
    public static Date convIntegerToDate(int date) {
        // ｶﾚﾝﾀﾞｰｸﾗｽのｲﾝｽﾀﾝｽを取得
        Calendar cal = Calendar.getInstance();
        // 1980年1月1日を設定
        cal.set(1980, 0, 1);
        // 加算
        cal.add(Calendar.DATE, date - D_19800101);

        return cal.getTime();
    }

    /**
     * 数値日付を表示用日付に変換
     *
     * @param date DBの日付数値
     * @param format 出力日付のﾌｫｰﾏｯﾄ
     * @return ﾌｫｰﾏｯﾄされた日付文字列
     */
    public static String getDisplayDate(int date, String format) {
        // 0は未設定扱いの為、空白を返す。
        if (date == 0) {
            
            switch (format) {
                case YY_MM_DD:
                    return "  .  .  ";
                case YYYY_MM_DD:
                    return "    .  .  ";
                default:
                    return "";
            }
        }

        DateFormat df = new SimpleDateFormat(format);
        int addDate = date - D_19800101;

        // ｶﾚﾝﾀﾞｰｸﾗｽのｲﾝｽﾀﾝｽを取得
        Calendar cal = Calendar.getInstance();
        // 1980年1月1日を設定
        cal.set(1980, 0, 1);
        // 加算
        cal.add(Calendar.DATE, addDate);

        return df.format(cal.getTime());
    }
    
    /**
     * 数値日付を表示用日付に変換
     *
     * @param date DBの日付数値
     * @param format 出力日付のﾌｫｰﾏｯﾄ
     * @return ﾌｫｰﾏｯﾄされた日付文字列
     */
    public static String getDisplayDate(Object date, String format) {
        // 0またはブランクは未設定扱いの為、空白を返す。
        if (null == date || StringUtil.isEmpty(date.toString()) ||  "0".equals(date.toString())) {
            switch (format) {
                case YY_MM_DD:
                    return "  .  .  ";
                case YYYY_MM_DD:
                    return "    .  .  ";
                default:
                    return "";
            }
        }
        
        int decDate = (int)date;

        DateFormat df = new SimpleDateFormat(format);
        int addDate = decDate - D_19800101;

        // ｶﾚﾝﾀﾞｰｸﾗｽのｲﾝｽﾀﾝｽを取得
        Calendar cal = Calendar.getInstance();
        // 1980年1月1日を設定
        cal.set(1980, 0, 1);
        // 加算
        cal.add(Calendar.DATE, addDate);

        return df.format(cal.getTime());
    }
    
    /**
     * 数値日付を表示用日付に変換
     *
     * @param date DBの日時
     * @param format 出力日付のﾌｫｰﾏｯﾄ
     * @return ﾌｫｰﾏｯﾄされた日付文字列
     */
    public static String getDisplayDate(Timestamp date, String format) {
        // 0またはブランクは未設定扱いの為、空白を返す。
        if (null == date || StringUtil.isEmpty(date.toString()) ||  "0".equals(date.toString())) {
            switch (format) {
                case YY_MM_DD:
                    return "  .  .  ";
                case YYYY_MM_DD:
                    return "    .  .  ";
                default:
                    return "";
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 日付の差分日数取得処理
     *
     * @param dateFrom 開始日付
     * @param dateTo 終了日付
     * @return 差分日数
     */
    public static int diffDays(Date dateFrom, Date dateTo) {
        // 日付の時分秒を切り捨てる
        Date dayFrom = DateUtils.truncate(dateFrom, Calendar.DAY_OF_MONTH);
        Date dayTo = DateUtils.truncate(dateTo, Calendar.DAY_OF_MONTH);

        // 日付をlong値に変換
        long dateTimeFrom = dayFrom.getTime();
        long dateTimeTo = dayTo.getTime();

        // 差分日数を取得
        return (int) ((dateTimeTo - dateTimeFrom) / (1000 * 60 * 60 * 24));
    }

    /**
     * 時間⇒数値(int)変換
     *
     * @param date Date型の日付
     * @return int型の時間
     */
    public static int convTimeToInteger(Date date) {

        Integer seconds;
        DateFormat dfHour = new SimpleDateFormat("H");
        DateFormat dfMinute = new SimpleDateFormat("m");
        DateFormat dfSeconds = new SimpleDateFormat("s");

        //Date型の日付から時間を求めて秒数に変換し、加算
        seconds = Integer.parseInt(dfHour.format(date)) * 3600;

        //Date型の日付から分を求めて秒数に変換し、加算
        seconds = seconds + Integer.parseInt(dfMinute.format(date)) * 60;

        //Date型の日付から秒を求めて、加算
        seconds = seconds + Integer.parseInt(dfSeconds.format(date));

        return seconds;
    }

    /**
     * 数値時間を表示用時間に変換
     *
     * @param time DBの時間数値
     * @param format 出力時間のﾌｫｰﾏｯﾄ
     * @return ﾌｫｰﾏｯﾄされた時間文字列
     */
    public static String getDisplayTime(int time, String format) {
        // 0は未設定扱いの為、空白を返す。
        if (time == 0) {
            return "";
        }

        String hour = String.format("%02d", time / 3600); //時間を取得
        String minute = String.format("%02d", (time % 3600) / 60); //分を取得
        String seconds = String.format("%02d", (time % 3600) % 60); //秒を取得
        
        switch (format) {
            case HHMMSS:
                return hour + minute + seconds;
            case HH_MM_SS:
                return hour + ":" + minute + ":" + seconds;
            case HHMM:
                return hour + minute;
            case HH_MM:
                return hour + ":" + minute;
                
            default:
                return "";
        }
    }
    
    /**
     * 数値時間を表示用時間に変換
     *
     * @param time DBの時間数値
     * @param format 出力時間のﾌｫｰﾏｯﾄ
     * @return ﾌｫｰﾏｯﾄされた時間文字列
     */
    public static String getDisplayTime(Object time, String format) {
        // 0は未設定扱いの為、空白を返す。
        if (null == time || StringUtil.isEmpty(time.toString()) ||  "0".equals(time.toString())) {
            return "";
        }
        
        int decTime = (int)time;

        String hour = String.format("%02d", decTime / 3600); //時間を取得
        String minute = String.format("%02d", (decTime % 3600) / 60); //分を取得
        String seconds = String.format("%02d", (decTime % 3600) % 60); //秒を取得
        
        switch (format) {
            case HHMMSS:
                return hour + minute + seconds;
            case HH_MM_SS:
                return hour + ":" + minute + ":" + seconds;
            case HHMM:
                return hour + minute;
            case HH_MM:
                return hour + ":" + minute;
                
            default:
                return "";
        }
    }
    
    /**
     * 数値時間を表示用時間に変換
     *
     * @param time DBの日時
     * @param format 出力時間のﾌｫｰﾏｯﾄ
     * @return ﾌｫｰﾏｯﾄされた時間文字列
     */
    public static String getDisplayTime(Timestamp time, String format) {
        // 0は未設定扱いの為、空白を返す。
        if (null == time || StringUtil.isEmpty(time.toString()) ||  "0".equals(time.toString())) {
            return "";
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }
    
    /**
     * 日付有効データチェック(YYMMDD)
     * @param date YYMMDD形式の日付
     * @return 有効な日付の場合 true
     */
    public static boolean isValidYYMMDD(String date) {
        boolean isValidData = true;
        if (6 != StringUtil.getByte(date, CHARSET, LOGGER)) {
            isValidData = false;
        } else {
            String yy = date.substring(0, 2);
            String mm = date.substring(2, 4);
            String dd = date.substring(4, 6);
            
            if (!NumberUtil.isDateNumber(yy) || !NumberUtil.isDateNumber(mm) || !NumberUtil.isDateNumber(dd)) {
                isValidData = false;
            } else {
                DateFormat format = new SimpleDateFormat("yyMMdd");
                try {
                    format.setLenient(false);
                    format.parse(yy + mm + dd);
                } catch (ParseException e) {
                    isValidData = false;
                }
            }
        }
        return isValidData;
    }
    
    /**
     * 時刻有効データチェック(HHMM)
     * @param time HHMM形式の時刻
     * @return 有効な時刻の場合 true
     */
    public static boolean isValidHHMM(String time) {
        boolean isValidData = true;
        if (4 != StringUtil.getByte(time, CHARSET, LOGGER)) {
            isValidData = false;
        } else {
            String hh = time.substring(0, 2);
            String mm = time.substring(2, 4);
            
            if (!NumberUtil.isIntegerNumeric(hh) || !NumberUtil.isIntegerNumeric(mm)) {
                isValidData = false;
            } else {
                int h = Integer.parseInt(hh);
                int m = Integer.parseInt(mm);
                
                if (0 > h || 23 < h || 0 > m || 59 < m) {
                    isValidData = false;
                }
            }
        }
        return isValidData;
    }
    
    /**
     * 日付文字列⇒Dateオブジェクト変換
     * @param yyMMdd 年月日
     * @param HHmm 時分
     * @return 変換後のデータ
     */
    public static Date convertStringToDate(String yyMMdd, String HHmm) {
        DateFormat format = new SimpleDateFormat("yyMMddHHmm");
        try {
            format.setLenient(false);
            Date result = format.parse(yyMMdd + HHmm);
            return result;
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 日付文字列⇒Dateオブジェクト変換
     * @param yyMMdd 年月日
     * @param HHmmss 時分秒
     * @return 変換後のデータ
     */
    public static Date convertStringToDateInSeconds(String yyMMdd, String HHmmss) {
        DateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        try {
            format.setLenient(false);
            Date result = format.parse(yyMMdd + HHmmss);
            return result;
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * タイムスタンプを指定のフォーマットで返す
     * @param timestamp タイムスタンプ
     * @param timeFormat フォーマット
     * @return フォーマット結果
     */
    public static String formattedTimestamp(Timestamp timestamp, String timeFormat) {
        if(timestamp == null){
            return "";
        }
        return new SimpleDateFormat(timeFormat).format(timestamp);
    }
    
    /**
     * 日付文字列⇒Integer変換
     * @param yyMMdd 年月日
     * @return 変換後のデータ
     */
    public static Integer convertDateStringToInteger(String yyMMdd) {
        DateFormat format = new SimpleDateFormat("yyMMdd");
        try {
            format.setLenient(false);
            return convDateToInteger(format.parse(yyMMdd));
        } catch (ParseException e) {
            return null;
        }
    }
    
}
