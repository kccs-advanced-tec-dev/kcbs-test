/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang3.math.NumberUtils;

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
 * 変更日	2018/11/26<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	共通チェック変更<br>
 * <br>
 * ===============================================================================<br>
 */

/**
 * 共通チェックユーティリティ
 * 
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class ValidateUtil {
    private static final Logger LOGGER = Logger.getLogger(ValidateUtil.class.getName());
    
    // 文字コード
    private static final String CHARSET = "MS932";
    
    // ロットチェック用ウェイト(1桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_1 = BigDecimal.valueOf(9);
    // ロットチェック用ウェイト(2桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_2 = BigDecimal.valueOf(5);
    // ロットチェック用ウェイト(3桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_3 = BigDecimal.valueOf(8);
    // ロットチェック用ウェイト(4桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_4 = BigDecimal.valueOf(7);
    // ロットチェック用ウェイト(5桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_5 = BigDecimal.valueOf(4);
    // ロットチェック用ウェイト(6桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_6 = BigDecimal.valueOf(3);
    // ロットチェック用ウェイト(7桁目)
    private static final BigDecimal LOT_CHECK_WEIGHT_7 = BigDecimal.valueOf(2);
    
    // チェック処理No列挙体
    public enum EnumCheckNo {
        C101,
        C102,
        C103,
        C104,
        C001,
        C002,
        C201,
        C301,
        C302,
        C501,
        C502,
        C601,
        S003,
        S004,
        E001,
    }
    
    /**
     * チェック処理内容
     */
    public class ValidateInfo {
        public EnumCheckNo checkNo;
        public String itemId;
        public BigDecimal maxValue;
        public BigDecimal minValue;
        
        /**
         * コンストラクタ
         * @param checkNo チェック処理No
         * @param itemId 項目ID
         * @param maxValue 最大値(S003, S004)
         * @param minValue 最小値(S003, S004)
         */
        public ValidateInfo(EnumCheckNo checkNo, String itemId, BigDecimal maxValue, BigDecimal minValue) {
            this.checkNo = checkNo;
            this.itemId = itemId;
            this.maxValue = maxValue;
            this.minValue = minValue;
        }
    }
    
    /**
     * チェック処理内容に応じたチェック処理を実施する。<BR>
     * チェックエラーが存在する場合、エラーメッセージを返却して処理を中断する。<BR>
     * @param validateInfoList チェック処理内容リスト
     * @param itemList 項目データリスト
     * @return エラー時はエラーメッセージを返却
     */
    public String executeValidation(List<ValidateInfo> validateInfoList, List<FXHDD01> itemList) {
        for (ValidateInfo validateInfo : validateInfoList) {
            String errorMessage = "";
            switch (validateInfo.checkNo) {
                case C101:
                    errorMessage = checkC101(itemList, validateInfo.itemId);
                    break;
                case C102:
                    errorMessage = checkC102(itemList, validateInfo.itemId);
                    break;
                case C103:
                    errorMessage = checkC103(itemList, validateInfo.itemId);
                    break;
                case C104:
                    errorMessage = checkC104(itemList, validateInfo.itemId);
                    break;
                case C001:
                    errorMessage = checkC001(itemList, validateInfo.itemId);
                    break;
                case C002:
                    errorMessage = checkC002(itemList, validateInfo.itemId);
                    break;
                case C201:
                    errorMessage = checkC201(itemList, validateInfo.itemId);
                    break;
                case C301:
                    errorMessage = checkC301(itemList, validateInfo.itemId);
                    break;
                case C302:
                    errorMessage = checkC302(itemList, validateInfo.itemId);
                    break;
                case C501:
                    errorMessage = checkC501(itemList, validateInfo.itemId);
                    break;
                case C502:
                    errorMessage = checkC502(itemList, validateInfo.itemId);
                    break;
                case C601:
                    errorMessage = checkC601(itemList, validateInfo.itemId);
                    break;
                case S003:
                    errorMessage = checkS003(itemList, validateInfo.itemId, validateInfo.maxValue, validateInfo.minValue);
                    break;
                case S004:
                    errorMessage = checkS004(itemList, validateInfo.itemId, validateInfo.maxValue, validateInfo.minValue);
                    break;
                case E001:
                    errorMessage = checkE001(itemList, validateInfo.itemId);
                    break;
                default:
                    break;
            }
            if (!StringUtil.isEmpty(errorMessage)) {
                return errorMessage;
            }
        }
        return null;
    }
    
    /**
     * 桁数チェック
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC101(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            int ketasu = -1;
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
                ketasu = Integer.parseInt(fXHDD01.getInputLength());
            }
            String value = fXHDD01.getValue();
            if (ketasu != StringUtil.getByte(value, CHARSET, LOGGER)) {
                return MessageUtil.getMessage("XHD-000004", fXHDD01.getLabel1(), ketasu);
            }
            if (ketasu != StringUtil.getLength(value)) {
               return MessageUtil.getMessage("XHD-000004", fXHDD01.getLabel1(), ketasu);
            }
        }
        return "";
    }

    /**
     * 指定桁数ﾁｪｯｸ(数値(小数あり))
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC102(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            int ketasuSeisu = -1;
            int ketasuShosu = -1;
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
                ketasuSeisu = Integer.parseInt(fXHDD01.getInputLength());
            }
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLengthDec())) {
                ketasuShosu = Integer.parseInt(fXHDD01.getInputLengthDec());
            }
            String value = fXHDD01.getValue();
            
            if(!NumberUtil.isNumeric(value) || !NumberUtil.isSameValidDigits(new BigDecimal(value), ketasuSeisu, ketasuShosu)){
                return MessageUtil.getMessage("XHD-000005", fXHDD01.getLabel1(), ketasuSeisu, ketasuShosu);
            }
            
        }
        return "";
    }
    
    /**
     * 桁数ﾁｪｯｸ(小数なし)
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC103(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            int ketasu = -1;
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
                ketasu = Integer.parseInt(fXHDD01.getInputLength());
            }
            String value = fXHDD01.getValue();
            
            if(!NumberUtil.isNumeric(value) || !NumberUtil.isIntegerNumeric(value) 
                    || !NumberUtil.isValidDigits(new BigDecimal(value), ketasu, 0)){
                return MessageUtil.getMessage("XHD-000006", fXHDD01.getLabel1(), ketasu);
            }
            
        }
        return "";
    }
    
    /**
     * 桁数ﾁｪｯｸ(小数あり)
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC104(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            int ketasuSeisu = -1;
            int ketasuShosu = -1;
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
                ketasuSeisu = Integer.parseInt(fXHDD01.getInputLength());
            }
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLengthDec())) {
                ketasuShosu = Integer.parseInt(fXHDD01.getInputLengthDec());
            }
            String value = fXHDD01.getValue();
            
            if(!NumberUtil.isNumeric(value) || !NumberUtil.isValidDigits(new BigDecimal(value), ketasuSeisu, ketasuShosu)){
                return MessageUtil.getMessage("XHD-000007", fXHDD01.getLabel1(), ketasuSeisu, ketasuShosu);
            }
            
        }
        return "";
    }
    
    /**
     * 必須チェック
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC001(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (StringUtil.isEmpty(value.trim())) {
                return MessageUtil.getMessage("XHD-000003", fXHDD01.getLabel1());
            }
        }
        return "";
    }

    /**
     * 必須チェック
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC002(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (StringUtil.isEmpty(value)) {
                return MessageUtil.getMessage("XHD-000003", fXHDD01.getLabel1());
            }
        }
        return "";
    }

    /**
     * 型ﾁｪｯｸ
     * @param itemList 入力項目名
     * @param itemId 入力項目ﾃﾞｰﾀ
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC201(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
            if (null != fXHDD01) {
                String value = fXHDD01.getValue();
                if(!NumberUtil.isNumeric(value)){
                    return MessageUtil.getMessage("XHD-000008", fXHDD01.getLabel1());
                }
            }
        return "";
    }

    /**
     * 正負ﾁｪｯｸ
     * @param itemList 入力項目名
     * @param itemId 入力項目ﾃﾞｰﾀ
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC301(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
            if (null != fXHDD01) {
                String value = fXHDD01.getValue();
                if(!NumberUtil.isNumeric(value)){
                    return MessageUtil.getMessage("XHD-000008", fXHDD01.getLabel1());
                }
                if (new BigDecimal(value).compareTo(BigDecimal.ZERO) == -1) {
                    return MessageUtil.getMessage("XHD-000009", fXHDD01.getLabel1());
                }
            }

        return "";
    }

    /**
     * 正負ﾁｪｯｸ
     * @param itemList 入力項目名
     * @param itemId 入力項目ﾃﾞｰﾀ
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC302(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
            if (null != fXHDD01) {
                String value = fXHDD01.getValue();
                if(!NumberUtil.isNumeric(value)){
                    return MessageUtil.getMessage("XHD-000008", fXHDD01.getLabel1());
                }
                if (new BigDecimal(value).compareTo(BigDecimal.ZERO) == 1) {
                    return MessageUtil.getMessage("XHD-000010", fXHDD01.getLabel1());
                }
            }

        return "";
    }
    
    /**
     * 数値チェック(範囲内)
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @param maxValue 上限値
     * @param minValue 下限値
     * @return エラー時はエラーメッセージを返却
     */
    public String checkS003(List<FXHDD01> itemList, String itemId, BigDecimal maxValue, BigDecimal minValue) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (!NumberUtil.isNumeric(value) ||
                !NumberUtil.isValidRange(new BigDecimal(value), maxValue, minValue)) {
                return MessageUtil.getMessage("XHC-000004", fXHDD01.getLabel1(), minValue.toPlainString(), maxValue.toPlainString());
            }
        }
        return "";
    }
    
    /**
     * 数値チェック(桁数)
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @param maxValue 上限値(メッセージ出力用)
     * @param minValue 下限値(メッセージ出力用)
     * @return エラー時はエラーメッセージを返却
     */
    public String checkS004(List<FXHDD01> itemList, String itemId, BigDecimal maxValue, BigDecimal minValue) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            
            int maxSeisu = 0;
            int maxSyosu = 0;
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
                maxSeisu = Integer.parseInt(fXHDD01.getInputLength());
            }
            if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLengthDec())) {
                maxSyosu = Integer.parseInt(fXHDD01.getInputLengthDec());
            }
            
            if (!NumberUtil.isNumeric(value) ||
                !NumberUtil.isValidDigits(new BigDecimal(value), maxSeisu, maxSyosu)) {
                return MessageUtil.getMessage("XHC-000004", fXHDD01.getLabel1(), minValue.toPlainString(), maxValue.toPlainString());
            }
        }
        return "";
    }
    
    /**
     * 日付チェック(YYMMDD)
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC501(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (!DateUtil.isValidYYMMDD(value)) {
                return MessageUtil.getMessage("XHD-000012", fXHDD01.getLabel1());
            }
        }
        return "";
    }
    
    /**
     * 時刻チェック(HHMM)
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC502(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (!DateUtil.isValidHHMM(value)) {
                return MessageUtil.getMessage("XHD-000013", fXHDD01.getLabel1());
            }
        }
        return "";
    }
    
    /**
     * 必須チェック2<BR>
     * ブランクでない、且つ入力値が"0"でないことを検証<BR>
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC601(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (StringUtil.isEmpty(value) || "0".equals(value)) {
                return MessageUtil.getMessage("XHD-000003", fXHDD01.getLabel1());
            }
        }
        return "";
    }
    
    /**
     * 日時前後チェック
     * @param itemList 項目データリスト
     * @param dateItemId1 日付項目ID
     * @param timeItemId1 時刻項目ID
     * @param dateItemId2 日付項目ID
     * @param timeItemId2 時刻項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkR001(List<FXHDD01> itemList, String dateItemId1, String timeItemId1, String dateItemId2, String timeItemId2) {
        // 項目データを取得
        FXHDD01 fXHDD01_date1 = getItemRow(itemList, dateItemId1);
        FXHDD01 fXHDD01_time1 = getItemRow(itemList, timeItemId1);
        FXHDD01 fXHDD01_date2 = getItemRow(itemList, dateItemId2);
        FXHDD01 fXHDD01_time2 = getItemRow(itemList, timeItemId2);
        if (null != fXHDD01_date1 && null != fXHDD01_time1 && null != fXHDD01_date2 && null != fXHDD01_time2) {
            String date1 = fXHDD01_date1.getValue();
            String time1 = fXHDD01_time1.getValue();
            String date2 = fXHDD01_date2.getValue();
            String time2 = fXHDD01_time2.getValue();
            
            if (!StringUtil.isEmpty(date1) && !StringUtil.isEmpty(time1) &&
                !StringUtil.isEmpty(date2) && !StringUtil.isEmpty(time2)) {
                Date d1 = DateUtil.convertStringToDate(date1, time1);
                Date d2 = DateUtil.convertStringToDate(date2, time2);
                if (null != d1 && null != d2 && d1.compareTo(d2) > 0) {
                    return MessageUtil.getMessage("XHC-000007", fXHDD01_date1.getLabel1(), fXHDD01_date2.getLabel1());
                }
            }
        }
        return "";
    }
    
    /**
     * ロットNoチェックデジットチェック
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public String checkE001(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (!checkLotNoDigit(value)) {
                return MessageUtil.getMessage("XHC-000010");
            }
        }
        return "";
    }
    
    /**
     * レコード存在チェック
     * @param queryRunner QueryRunnerオブジェクト
     * @param table テーブル名
     * @param items 検索条件項目
     * @param params パラメータ
     * @return レコードが存在する場合true
     */
    public boolean checkC401402(QueryRunner queryRunner, String table, List<String> items, List<Object> params) {
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM " + table + " ";
            if (null != items && !items.isEmpty()) {
                sql += "WHERE ";

                StringBuilder sb = new StringBuilder();
                for (String item : items) {
                    if (0 < sb.length()) {
                        sb.append("AND ");
                    }
                    sb.append(item);
                    sb.append(" = ? ");
                }

                sql += sb.toString();
            }

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map result = null;
            if (null == items || items.isEmpty()) {
                result = queryRunner.query(sql, new MapHandler());
            } else {
                result = queryRunner.query(sql, new MapHandler(), params.toArray());
            }
            
            if (null != result && !result.isEmpty()) {
                Object value = result.get("CNT");
                if (value instanceof Integer && (Integer)value == 0) {
                    return false;
                } else if (value instanceof Long && (Long)value == 0) {
                    return false;
                } else if (value instanceof BigDecimal && ((BigDecimal)value).equals(BigDecimal.ZERO)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
            
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return false;
        }
    }
        
    /**
     * 項目データ取得
     * @param itemList フォームデータ
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(List<FXHDD01> itemList, String itemId) {
        List<FXHDD01> selectData = 
                itemList.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * LotNo DigitCheck
     * @param lotNo ロットNo(13桁)
     * @return チェック結果(true:エラーなし、false:エラー有り)
     */
    private boolean checkLotNoDigit(String lotNo) {
        Map<String, BigDecimal> lotMap = new HashMap<>();
        // ロットNo4桁目から11桁を数値化してMapに設定、数値化できない場合はエラーとしてリターン
        String spLotNo;
        for (int i = 0; i < 8; i++) {
            spLotNo = StringUtil.mid(lotNo, i + 4, i + 4);
            if (StringUtil.isEmpty(spLotNo) || !NumberUtils.isDigits(spLotNo)) {
                return false;
            }

            lotMap.put("LOT" + (i + 4), new BigDecimal(spLotNo));
        }

        //ロットNoの各桁にWEIGHT(9587432)をかけての合計を取得
        BigDecimal sum1 = BigDecimal.ZERO;
        sum1 = sum1.add(lotMap.get("LOT4").multiply(LOT_CHECK_WEIGHT_1));
        sum1 = sum1.add(lotMap.get("LOT5").multiply(LOT_CHECK_WEIGHT_2));
        sum1 = sum1.add(lotMap.get("LOT6").multiply(LOT_CHECK_WEIGHT_3));
        sum1 = sum1.add(lotMap.get("LOT7").multiply(LOT_CHECK_WEIGHT_4));
        sum1 = sum1.add(lotMap.get("LOT8").multiply(LOT_CHECK_WEIGHT_5));
        sum1 = sum1.add(lotMap.get("LOT9").multiply(LOT_CHECK_WEIGHT_6));
        sum1 = sum1.add(lotMap.get("LOT10").multiply(LOT_CHECK_WEIGHT_7));

        // 10で割った余りを取得
        BigDecimal checkValue = sum1.remainder(BigDecimal.TEN);
        
        // ロットNoの11桁目の値がチェック値と一致しない場合、エラー
        return checkValue.compareTo(lotMap.get("LOT11")) == 0;
    }

}
