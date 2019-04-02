/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDM05;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
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
 * 変更日	2018/12/13<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	共通チェックの戻り値を変更・規格チェックの追加<br>
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

    // 規格チェックコード(正常)
    private static final String KIKAKU_CHECK_CD_NORMAL = "0";

    // 規格チェックコード(エラー)
    private static final String KIKAKU_CHECK_CD_ERR = "1";

    // 規格チェックコード(規格値の設定エラー)
    private static final String KIKAKU_CHECK_CD_SETTING_ERR = "-1";

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
        C401,
        C402,
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
         *
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
     *
     * @param itemRowCheckList チェック処理内容リスト
     * @param itemList 項目データリスト
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @return エラー時はエラーメッセージを返却
     */
    public ErrorMessageInfo executeValidation(List<FXHDM05> itemRowCheckList, List<FXHDD01> itemList, QueryRunner queryRunnerWip) {
        
        try {
            ErrorMessageInfo errorMessageInfo = null;
            for (FXHDD01 fxhdd01 : itemList) {
                if (fxhdd01 == null) {
                    continue;
                }

                //共通ﾁｪｯｸ
                List<FXHDM05> itemRowCheckListFilter
                        = itemRowCheckList.stream().filter(n -> fxhdd01.getItemId().equals(n.getItemId())).collect(Collectors.toList());

                for (FXHDM05 fxhdm05 : itemRowCheckListFilter) {
                    switch (fxhdm05.getCheckPattern()) {
                        case "C101":
                            errorMessageInfo = checkC101(fxhdd01);
                            break;
                        case "C102":
                            errorMessageInfo = checkC102(fxhdd01);
                            break;
                        case "C103":
                            errorMessageInfo = checkC103(fxhdd01);
                            break;
                        case "C104":
                            errorMessageInfo = checkC104(fxhdd01);
                            break;
                        case "C001":
                            errorMessageInfo = checkC001(fxhdd01);
                            break;
                        case "C002":
                            errorMessageInfo = checkC002(fxhdd01);
                            break;
                        case "C201":
                            errorMessageInfo = checkC201(fxhdd01);
                            break;
                        case "C301":
                            errorMessageInfo = checkC301(fxhdd01);
                            break;
                        case "C302":
                            errorMessageInfo = checkC302(fxhdd01);
                            break;
                        case "C401":
                            errorMessageInfo = checkC401(fxhdd01, queryRunnerWip);
                            break;
                        case "C402":
                            errorMessageInfo = checkC402(fxhdd01, queryRunnerWip);
                            break;
                        case "C501":
                            errorMessageInfo = checkC501(fxhdd01);
                            break;
                        case "C502":
                            errorMessageInfo = checkC502(fxhdd01);
                            break;
                        case "C601":
                            errorMessageInfo = checkC601(fxhdd01);
                            break;
                        default:
                            break;
                    }

                    if (errorMessageInfo != null) {
                        return errorMessageInfo;
                    }
                }
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            return new ErrorMessageInfo("SQLException発生");
        }
        return null;
    }

    /**
     * 桁数チェック
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC101(FXHDD01 fXHDD01) {
        // 項目データを取得
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(fXHDD01.getValue())) {
            return null;
        }

        int ketasu = -1;
        if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
            ketasu = Integer.parseInt(fXHDD01.getInputLength());
        }
        String value = fXHDD01.getValue();

        // エラー対象をリストに追加
        List<FXHDD01> errorItemList = Arrays.asList(fXHDD01);
        if (ketasu != StringUtil.getLength(value)) {
            return MessageUtil.getErrorMessageInfo("XHD-000004", true, true, errorItemList, fXHDD01.getLabel1(), ketasu);
        }
        return null;
    }
    
    /**
     * 桁数チェック
     *
     * @param value 入力値
     * @param itemName 項目名
     * @param length 桁数
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC101(String value, String itemName, int length) {
        // 項目データを取得
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        // エラー対象をリストに追加
        if (length != StringUtil.getLength(value)) {
            return MessageUtil.getMessage("XHD-000004", itemName, length);
        }
        return null;
    } 

    /**
     * 指定桁数ﾁｪｯｸ(数値(小数あり))
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC102(FXHDD01 fXHDD01) {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(fXHDD01.getValue())) {
            return null;
        }

        int ketasuSeisu = -1;
        int ketasuShosu = -1;
        if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
            ketasuSeisu = Integer.parseInt(fXHDD01.getInputLength());
        }
        if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLengthDec())) {
            ketasuShosu = Integer.parseInt(fXHDD01.getInputLengthDec());
        }
        String value = fXHDD01.getValue();

        if (!NumberUtil.isNumeric(value) || !NumberUtil.isSameValidDigits(new BigDecimal(value), ketasuSeisu, ketasuShosu)) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000005", true, true, errFxhdd01List, fXHDD01.getLabel1(), ketasuSeisu, ketasuShosu);
        }

        return null;
    }

    /**
     * 桁数ﾁｪｯｸ(小数なし)
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC103(FXHDD01 fXHDD01) {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(fXHDD01.getValue())) {
            return null;
        }

        int ketasu = -1;
        if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
            ketasu = Integer.parseInt(fXHDD01.getInputLength());
        }
        String value = StringUtil.nullToBlank(fXHDD01.getValue());

        if (ketasu < value.length()) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000006", true, true, errFxhdd01List, fXHDD01.getLabel1(), ketasu);
        }

        return null;
    }
    
    /**
     * 桁数ﾁｪｯｸ(小数なし)
     *
     * @param value 入力値
     * @param itemName 項目名
     * @param length 桁数
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC103(String value, String itemName, int length) {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        
        if (length < value.length()) {
            return MessageUtil.getMessage("XHD-000006", itemName, length);
        }

        return null;
    }

    /**
     * 桁数ﾁｪｯｸ(小数あり)
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC104(FXHDD01 fXHDD01) {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(fXHDD01.getValue())) {
            return null;
        }

        int ketasuSeisu = -1;
        int ketasuShosu = -1;
        if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLength())) {
            ketasuSeisu = Integer.parseInt(fXHDD01.getInputLength());
        }
        if (NumberUtil.isIntegerNumeric(fXHDD01.getInputLengthDec())) {
            ketasuShosu = Integer.parseInt(fXHDD01.getInputLengthDec());
        }
        String value = fXHDD01.getValue();

        if (!NumberUtil.isNumeric(value) || !NumberUtil.isValidDigits(new BigDecimal(value), ketasuSeisu, ketasuShosu)) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000007", true, true, errFxhdd01List, fXHDD01.getLabel1(), ketasuSeisu, ketasuShosu);
        }

        return null;
    }

    /**
     * 必須チェック
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC001(FXHDD01 fXHDD01) {
        // 項目データを取得

        String value = fXHDD01.getValue();
        if (StringUtil.isEmpty(StringUtil.trimAll(value))) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }
    
    /**
     * 必須チェック
     *
     * @param value 入力値
     * @param itemName 項目名
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC001(String value, String itemName) {
        // 項目データを取得
        if (StringUtil.isEmpty(value.trim())) {
            return MessageUtil.getMessage("XHD-000003", itemName);
        }

        return null;
    }

    /**
     * 必須チェック
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC002(FXHDD01 fXHDD01) {
        String value = fXHDD01.getValue();
        if (StringUtil.isEmpty(value)) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }

    /**
     * 数値型ﾁｪｯｸ
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC201(FXHDD01 fXHDD01) {
        String value = fXHDD01.getValue();

        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!NumberUtil.isNumeric(value)) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000008", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }
    
    /**
     * 数値型ﾁｪｯｸ
     *
     * @param value 入力値
     * @param itemName 項目名
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC201(String value, String itemName) {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!NumberUtil.isNumeric(value)) {
            return MessageUtil.getMessage("XHD-000008", itemName);
        }

        return null;
    }
    
    /**
     * 数値型ﾁｪｯｸ<br>
     * 文字列が数値のみ且つ小数点を含まないで構成されているか判定します。<br>
     * 先頭に"0"が含まれる場合もエラーとしません。<br>
     * ※日時検索条件の整合性チェックに使用<br>
     * 例)0001⇒true 1111⇒true 00.01⇒false<br>
     *
     * @param value 入力値
     * @param itemName 項目名
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC201ForDate(String value, String itemName) {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!NumberUtil.isNumericForDate(value)) {
            return MessageUtil.getMessage("XHD-000008", itemName);
        }

        return null;
    }

    /**
     * 正数ﾁｪｯｸ
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC301(FXHDD01 fXHDD01) {
        String value = fXHDD01.getValue();
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
        if (!NumberUtil.isNumeric(value)) {
            return MessageUtil.getErrorMessageInfo("XHD-000009", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }
        if (new BigDecimal(value).compareTo(BigDecimal.ZERO) == -1) {
            return MessageUtil.getErrorMessageInfo("XHD-000009", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }

    /**
     * 負数ﾁｪｯｸ
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC302(FXHDD01 fXHDD01) {
        // 項目データを取得

        String value = fXHDD01.getValue();
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
        if (!NumberUtil.isNumeric(value)) {
            return MessageUtil.getErrorMessageInfo("XHD-000010", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }
        if (0 <= new BigDecimal(value).compareTo(BigDecimal.ZERO)) {
            return MessageUtil.getErrorMessageInfo("XHD-000010", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }

    /**
     * 号機ﾏｽﾀﾁｪｯｸ
     *
     * @param fXHDD01 項目データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC401(FXHDD01 fXHDD01, QueryRunner queryRunnerDoc) throws SQLException {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(fXHDD01.getValue())) {
            return null;
        }

        List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);

        if (!existGokukimas(fXHDD01.getValue(), queryRunnerDoc)) {
            return MessageUtil.getErrorMessageInfo("XHD-000011", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }

    /**
     * 担当者ﾏｽﾀﾁｪｯｸ
     *
     * @param fXHDD01 項目データ
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC402(FXHDD01 fXHDD01, QueryRunner queryRunnerWip) throws SQLException {
        // 値が入っていない場合、チェック無し
        if (StringUtil.isEmpty(fXHDD01.getValue())) {
            return null;
        }

        List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);

        if (!existTantomas(fXHDD01.getValue(), queryRunnerWip)) {
            return MessageUtil.getErrorMessageInfo("XHD-000011", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }

    /**
     * 日付チェック(YYMMDD)
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC501(FXHDD01 fXHDD01) {
        String value = fXHDD01.getValue();
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!DateUtil.isValidYYMMDD(value)) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000012", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }
    
    /**
     * 日付チェック(YYMMDD)
     *
     * @param value 入力値
     * @param itemName 項目名
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC501(String value, String itemName) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!DateUtil.isValidYYMMDD(value)) {
            return MessageUtil.getMessage("XHD-000012", itemName);
        }

        return null;
    }

    /**
     * 時刻チェック(HHMM)
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC502(FXHDD01 fXHDD01) {
        // 項目データを取得
        String value = fXHDD01.getValue();
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!DateUtil.isValidHHMM(value)) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000013", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }

        return null;
    }
    
    /**
     * 時刻チェック(HHMM)
     *
     * @param value 入力値
     * @param itemName 項目名
     * @return エラー時はエラーメッセージを返却
     */
    public String checkC502(String value, String itemName) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }

        if (!DateUtil.isValidHHMM(value)) {
            return MessageUtil.getMessage("XHD-000013", itemName);
        }

        return null;
    }

    /**
     * 必須チェック2<BR>
     * ブランクでない、且つ入力値が"0"でないことを検証<BR>
     *
     * @param fXHDD01 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    private ErrorMessageInfo checkC601(FXHDD01 fXHDD01) {
        // 項目データを取得
        BigDecimal decValue = null;
        try {
            decValue = new BigDecimal(fXHDD01.getValue());
        } catch (Exception e) {
            // 処理なし
        }
        
        if(decValue == null){
            return null;
        }

        if (BigDecimal.ZERO.compareTo(decValue) == 0) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
            return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, fXHDD01.getLabel1());
        }
        return null;
    }

    /**
     * 日時前後チェック
     *
     * @param itemLabel1 項目ラベル
     * @param date1 入力項目データ
     * @param itemLabel2 項目ラベル
     * @param date2 入力項目データ
     * @return エラー時はエラーメッセージを返却(比較不可時は""を返す。)
     */
    public String checkR001(String itemLabel1, Date date1, String itemLabel2, Date date2) {

        // 比較データがNULLの場合、チェック不可として""を返す。
        if (date1 == null || date2 == null) {
            return "";
        }

        // 項目データを取得
        if (0 < date1.compareTo(date2)) {
            return MessageUtil.getMessage("XHD-000023", itemLabel1, itemLabel2);
        }

        return "";
    }

    /**
     * ロットNoチェックデジットチェック
     *
     * @param itemList 項目データリスト
     * @param itemId 項目ID
     * @return エラー時はエラーメッセージを返却
     */
    public ErrorMessageInfo checkE001(List<FXHDD01> itemList, String itemId) {
        // 項目データを取得
        FXHDD01 fXHDD01 = getItemRow(itemList, itemId);
        if (null != fXHDD01) {
            String value = fXHDD01.getValue();
            if (!checkLotNoDigit(value)) {
                List<FXHDD01> errFxhdd01List = Arrays.asList(fXHDD01);
                return MessageUtil.getErrorMessageInfo("XHD-000024", true, true, errFxhdd01List);
            }
        }
        return null;
    }

    /**
     * ロットNoチェックデジットチェック
     *
     * @param itemValue 項目データ
     * @return エラー時はエラーメッセージを返却
     */
    public String checkValueE001(String itemValue) {
        String value = itemValue;
        if (!checkLotNoDigit(value)) {
            return MessageUtil.getMessage("XHD-000024");
        }
        return "";
    }

    /**
     * 数値チェック(範囲内)
     *
     * @param itemLabel 入力項目名
     * @param minValue 下限値
     * @param maxValue 上限値
     * @param value 項目値
     * @return エラー時はエラーメッセージを返却
     */
    public String checkS001(String itemLabel, BigDecimal minValue, BigDecimal maxValue, BigDecimal value) {
        if (!NumberUtil.isValidRange(value, maxValue, minValue)) {
            return MessageUtil.getMessage("XHD-000022", itemLabel, minValue.toPlainString(), maxValue.toPlainString());
        }
        return "";
    }

    /**
     * ﾚｺｰﾄﾞ存在ﾁｪｯｸ
     *
     * @param table テーブル名
     * @param items カラム名
     * @param itemLabel 入力項目名
     * @param params 入力項目データ
     * @param queryRunner QueryRunnerオブジェクト
     * @return エラー時はエラーメッセージを返却
     * @throws java.sql.SQLException 例外エラー
     */
    public String checkT001(String table, List<String> items, String itemLabel, List<Object> params, QueryRunner queryRunner) throws SQLException {
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
        Map result;
        if (null == items || items.isEmpty()) {
            result = queryRunner.query(sql, new MapHandler());
        } else {
            result = queryRunner.query(sql, new MapHandler(), params.toArray());
        }

        if (null != result && !result.isEmpty()) {
            BigDecimal cnt = new BigDecimal(String.valueOf(result.get("CNT")));
            if (BigDecimal.ZERO.compareTo(cnt) == -1) {
                return "";
            }
        }

        return MessageUtil.getMessage("XHD-000011", itemLabel);

    }

    /**
     * 担当者ﾏｽﾀﾁｪｯｸ
     *
     * @param tantoshacode 担当者ｺｰﾄﾞ
     * @param itemLabel ラベル(入力項目名)
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     * @return エラー時はエラーメッセージを返却
     * @throws java.sql.SQLException 例外エラー
     */
    public String checkT002(String itemLabel, String tantoshacode, QueryRunner queryRunnerWip) throws SQLException {
        // 項目データを取得
        if (!existTantomas(tantoshacode, queryRunnerWip)) {
            return MessageUtil.getMessage("XHD-000011", itemLabel);
        }
        return "";
    }

    /**
     * 項目データ取得
     *
     * @param itemList 項目リスト
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(List<FXHDD01> itemList, String itemId) {
        List<FXHDD01> selectData
                = itemList.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

    /**
     * LotNo DigitCheck
     *
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

    /**
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param itemList 項目リスト
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    public static ErrorMessageInfo checkInputKikakuchi(List<FXHDD01> itemList, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {

        for (FXHDD01 fxhdd01 : itemList) {
            // 規格チェックの対象項目かどうか
            if (StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi())) {
                continue;
            }

            // 入力項目かつ入力値があるか
            if (!isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue())) {
                continue;
            }

            String resultCd = KIKAKU_CHECK_CD_NORMAL;
            switch (fxhdd01.getStandardPattern()) {
                case "1":
                    resultCd = checkKikakuST001(fxhdd01);
                    break;
                case "2":
                    resultCd = checkKikakuST002(fxhdd01);
                    break;
                case "3":
                    resultCd = checkKikakuST003(fxhdd01);
                    break;
                case "4":
                    resultCd = checkKikakuST004(fxhdd01);
                    break;
                case "5":
                    resultCd = checkKikakuST005(fxhdd01);
                    break;
                case "6":
                    resultCd = checkKikakuST006(fxhdd01);
                    break;
                case "7":
                    resultCd = checkKikakuST007(fxhdd01);
                    break;
                case "8":
                    resultCd = checkKikakuST008(fxhdd01);
                    break;
                case "9":
                    resultCd = checkKikakuST009(fxhdd01);
                    break;
                case "10":
                    resultCd = checkKikakuST010(fxhdd01);
                    break;
                default:
                    break;
            }

            // 規格値設定エラー
            if (KIKAKU_CHECK_CD_SETTING_ERR.equals(resultCd)) {
                return MessageUtil.getErrorMessageInfo("XHD-000028", true, true, Arrays.asList(fxhdd01), fxhdd01.getLabel1());
            }

            // 規格外エラー
            if (KIKAKU_CHECK_CD_ERR.equals(resultCd)) {
                // エラー項目の情報をリストに追加
                kikakuchiInputErrorInfoList.add(new KikakuchiInputErrorInfo(fxhdd01.getItemId(), fxhdd01.getLabel1(), fxhdd01.getKikakuChi(), fxhdd01.getItemIndex(), fxhdd01.getValue()));
            }

        }
        return null;
    }

    /**
     * 入力項目判定(入力項目かどうかを判定する)
     *
     * @param fxhdd01 項目データ
     * @return true:入力項目、false:入力項目以外
     */
    private static boolean isInputColumn(FXHDD01 fxhdd01) {
        if (fxhdd01.isRenderInputDate() || fxhdd01.isRenderInputNumber() || fxhdd01.isRenderInputRadio()
                || fxhdd01.isRenderInputSelect() || fxhdd01.isRenderInputText() || fxhdd01.isRenderInputTime()
                || fxhdd01.isRenderOutputLabel()) {
            return true;
        }
        return false;
    }

    /**
     * 規格チェック(1:〇±△)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST001(FXHDD01 fxhdd01) {

        // 項目データを取得
        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // 規格値を前後で分割
        String kikakuchi[] = strKikakuchi.split("±");
        if (kikakuchi.length != 2) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 規格値の数値部分を取得
        BigDecimal value1 = numberExtraction(kikakuchi[0]);
        BigDecimal value2 = numberExtraction(kikakuchi[1]);
        if (value1 == null || value2 == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 上限値取得
        BigDecimal maxValue = value1.add(value2);
        // 下限値取得
        BigDecimal minValue = value1.subtract(value2);

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 上限値、下限値の範囲外か判定
        if (inputValue.compareTo(maxValue) == 1 || inputValue.compareTo(minValue) == -1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 正常
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(2:〇～△)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST002(FXHDD01 fxhdd01) {

        // 項目データを取得
        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // 規格値を前後で分割
        String kikakuchi[] = strKikakuchi.split("～");
        if (kikakuchi.length != 2) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 規格値の数値部分を取得
        BigDecimal minValue = numberExtraction(kikakuchi[0]); // 下限値
        BigDecimal maxValue = numberExtraction(kikakuchi[1]); // 上限値
        if (minValue == null || maxValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }
        
        // 数値の大小が反転している場合、値を入れ替える
        if(maxValue.compareTo(minValue) < 0){
            BigDecimal tempValue = maxValue;
            maxValue = minValue;
            minValue = tempValue;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 上限値、下限値の範囲外か判定
        if (inputValue.compareTo(maxValue) == 1 || inputValue.compareTo(minValue) == -1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 正常
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(3:〇以上)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST003(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "以上"の文言が入っているか、入っていなければ不正
        if (!strKikakuchi.contains("以上")) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 下限値取得
        BigDecimal minValue = numberExtraction(strKikakuchi);
        if (minValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 下限値外か判定
        if (inputValue.compareTo(minValue) == -1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(4:〇以下)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST004(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "以上"の文言が入っているか、入っていなければ不正
        if (!strKikakuchi.contains("以下")) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 上限値取得
        BigDecimal maxValue = numberExtraction(strKikakuchi);
        if (maxValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 上限値外か判定
        if (inputValue.compareTo(maxValue) == 1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(5:≦〇)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST005(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "≦"の文言が入っていないまたは"≦"の後ろに文字が入っていない場合
        int startIndex = strKikakuchi.indexOf("≦");
        if (startIndex < 0 || strKikakuchi.length() <= (startIndex + 1)) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 上限値取得
        BigDecimal maxValue = numberExtraction(strKikakuchi.substring(startIndex + 1));
        if (maxValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 上限値外か判定
        if (inputValue.compareTo(maxValue) == 1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(6:≧〇)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST006(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "≧"の文言が入っていないまたは"≧"の後ろに文字が入っていない場合
        int startIndex = strKikakuchi.indexOf("≧");
        if (startIndex < 0 || strKikakuchi.length() <= (startIndex + 1)) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 下限値取得
        BigDecimal minValue = numberExtraction(strKikakuchi.substring(startIndex + 1));
        if (minValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 下限値外か判定
        if (inputValue.compareTo(minValue) == -1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(7:<〇)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST007(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "<"の文言が入っていないまたは"<"の後ろに文字が入っていない場合
        int startIndex = strKikakuchi.indexOf("<");
        if (startIndex < 0 || strKikakuchi.length() <= (startIndex + 1)) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 上限値取得
        BigDecimal maxValue = numberExtraction(strKikakuchi.substring(startIndex + 1));
        if (maxValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 上限値以上か判定
        if (0 <= inputValue.compareTo(maxValue)) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(8:>〇)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST008(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // ">"の文言が入っていないまたは">"の後ろに文字が入っていない場合
        int startIndex = strKikakuchi.indexOf(">");
        if (startIndex < 0 || strKikakuchi.length() <= (startIndex + 1)) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 下限値取得
        BigDecimal minValue = numberExtraction(strKikakuchi.substring(startIndex + 1));
        if (minValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 下限値以下か判定
        if (inputValue.compareTo(minValue) <= 0) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(9:MAX〇)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST009(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "MAX"の文言が入っていないまたは"MAX"の後ろに文字が入っていない場合
        int startIndex = strKikakuchi.indexOf("MAX");
        if (startIndex < 0 || strKikakuchi.length() <= (startIndex + 3)) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 上限値取得
        BigDecimal maxValue = numberExtraction(strKikakuchi.substring(startIndex + 3));
        if (maxValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 上限値外か判定
        if (inputValue.compareTo(maxValue) == 1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 規格チェック(10:MIN〇)
     *
     * @param fxhdd01 項目データ
     * @return エラーコード(0:正常、1:規格エラー、-1:規格値不正)
     */
    private static String checkKikakuST010(FXHDD01 fxhdd01) {

        String strKikakuchi = StringUtil.nullToBlank(fxhdd01.getKikakuChi()).replace("【", "");
        strKikakuchi = strKikakuchi.replace("】", "");

        // "MIN"の文言が入っていないまたは"MIN"の後ろに文字が入っていない場合
        int startIndex = strKikakuchi.indexOf("MIN");
        if (startIndex < 0 || strKikakuchi.length() <= (startIndex + 3)) {
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 下限値取得
        BigDecimal minValue = numberExtraction(strKikakuchi.substring(startIndex + 3));
        if (minValue == null) {
            // 規格値不正
            return KIKAKU_CHECK_CD_SETTING_ERR;
        }

        // 入力値取得
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(fxhdd01.getValue());
        } catch (NumberFormatException e) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }

        // 下限値外か判定
        if (inputValue.compareTo(minValue) == -1) {
            // 規格チェックエラー
            return KIKAKU_CHECK_CD_ERR;
        }
        return KIKAKU_CHECK_CD_NORMAL;
    }

    /**
     * 数値抽出処理先頭から数値のデータのみ取得
     *
     * @param value 文字列
     * @return 抽出した文字列
     */
    private static BigDecimal numberExtraction(String value) {
        BigDecimal result = null;
        try {
            // 一文字ずつ分割
            String[] spValue = value.split("");
            StringBuilder numberStr = new StringBuilder();
            for (String str : spValue) {
                if (",".equals(str)) {
                    continue;
                }

                if (!str.matches("0|1|2|3|4|5|6|7|8|9|\\+|-|\\.")) {
                    break;
                }

                numberStr.append(str);
            }

            result = new BigDecimal(numberStr.toString());
        } catch (NumberFormatException e) {
            //処理なし
        }
        return result;
    }

    /**
     * 関連付けMapに定義されている項目が設計データで空である場合エラーとしエラー情報を返す
     * ※関連付けMapには設計データに持っている項目IDが設定されていること
     *
     * @param sekkeiData 設計データ
     * @param mapSekkeiAssociation 設計データ関連付けMap
     * @return エラーメッセージリスト
     */
    public static List<String> checkSekkeiUnsetItems(Map<String, String> sekkeiData, Map<String, String> mapSekkeiAssociation) {

        List<String> errorMessageList = new ArrayList<>();
        for (Map.Entry<String, String> entry : mapSekkeiAssociation.entrySet()) {
            String checkData = "";
            if (sekkeiData.get(entry.getKey()) != null) {
                checkData = String.valueOf(sekkeiData.get(entry.getKey()));
            }

            if (StringUtil.isEmpty(checkData)) {
                errorMessageList.add(MessageUtil.getMessage("XHD-000021", entry.getKey(), entry.getValue()));
            }
        }

        return errorMessageList;
    }

    
    /**
     * 関連付けMapに定義されている項目が設計データで空である場合エラーとしエラー情報を返す
     * ※関連付けMapには設計データに持っている項目IDが設定されていること
     *
     * @param sekkeiData 設計データ
     * @param listSekkeiAssociation 設計データ関連付けList
     * @return エラーメッセージリスト
     */
    public static List<String> checkSekkeiUnsetItems(Map<String, String> sekkeiData, List<String[]> listSekkeiAssociation) {

        List<String> errorMessageList = new ArrayList<>();
        for (String[] sekkeiAssociation : listSekkeiAssociation) {
            String checkData = "";
            if (sekkeiData.get(sekkeiAssociation[0]) != null) {
                checkData = String.valueOf(sekkeiData.get(sekkeiAssociation[0]));
            }

            if (StringUtil.isEmpty(checkData)) {
                errorMessageList.add(MessageUtil.getMessage("XHD-000021", sekkeiAssociation[0], sekkeiAssociation[1]));
            }
        }

        return errorMessageList;
    }
    
    /**
     * 規格外登録履歴登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者コード
     * @param rev 履歴No
     * @param lotNo ロットNo
     * @param formId 画面ID
     * @param gamenTitle 画面タイトル
     * @param jissekino 実績No
     * @param deleteFlag 削除フラグ
     * @param kikakuchiErrorInfoList 規格値エラー情報リスト
     * @throws SQLException 例外エラー
     */
    public static void fxhdd04Insert(QueryRunner queryRunnerDoc, String tantoshaCd, BigDecimal rev, String lotNo, String formId, String gamenTitle, int jissekino, String deleteFlag, List<KikakuchiInputErrorInfo> kikakuchiErrorInfoList) throws SQLException {

        String sql = "INSERT INTO fxhdd04 ("
                + "torokusha, toroku_date, koshinsha, koshin_date, rev, gamen_id, gamen_title, kojyo, lotno, edaban,"
                + " jissekino, item_id, item_name, kikaku, input_value, delete_flag"
                + ") VALUES ("
                + "?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params;
        for (KikakuchiInputErrorInfo kikakuchiInputErrorInfo : kikakuchiErrorInfoList) {
            params = getFxhdd04UpdateParams(tantoshaCd, rev, lotNo, formId, gamenTitle, jissekino, deleteFlag, kikakuchiInputErrorInfo);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerDoc.update(sql, params.toArray());
        }
    }

    /**
     * 規格外登録履歴更新パラメータ取得処理
     *
     * @param tantoshaCd 担当者コード
     * @param rev 履歴No
     * @param lotNo ロットNo
     * @param formId 画面ID
     * @param gamenTitle 画面タイトル
     * @param jissekino 実績No
     * @param deleteFlag 削除フラグ
     * @param kikakuchiErrorInfo
     * @return 規格外登録履歴更新パラメータ
     */
    private static List<Object> getFxhdd04UpdateParams(String tantoshaCd, BigDecimal rev, String lotNo, String formId, String gamenTitle, int jissekino, String deleteFlag, KikakuchiInputErrorInfo kikakuchiErrorInfo) {
        List<Object> params = new ArrayList<>();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        params.add(tantoshaCd);
        params.add(timestamp);
        params.add(null);
        params.add(null);
        params.add(rev);
        params.add(formId);
        params.add(gamenTitle);
        params.add(lotNo.substring(0, 3));
        params.add(lotNo.substring(3, 11));
        params.add(lotNo.substring(11, 14));
        params.add(jissekino);
        params.add(kikakuchiErrorInfo.getItemId());
        params.add(kikakuchiErrorInfo.getItemLabel());
        params.add(kikakuchiErrorInfo.getItemKikakuchi());
        params.add(kikakuchiErrorInfo.getItemInputValue());
        params.add(deleteFlag);

        return params;

    }

    /**
     * 号機ﾏｽﾀ存在判定
     *
     * @param goukicode 号機ｺｰﾄﾞ
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     * @return true:存在する、false:存在しない
     */
    public boolean existGokukimas(String goukicode, QueryRunner queryRunnerWip) throws SQLException {
        String sql = "SELECT goukicode "
                + "FROM goukimas WHERE goukicode = ? ";

        List<Object> params = new ArrayList<>();
        params.add(goukicode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map gokukimas = queryRunnerWip.query(sql, new MapHandler(), params.toArray());
        if (null == gokukimas || gokukimas.isEmpty()) {
            return false;
        }
        return true;

    }

    /**
     * 担当者ﾏｽﾀ存在判定
     *
     * @param tantousyacode 担当者ｺｰﾄﾞ
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     * @return true:存在する、false:存在しない
     */
    public boolean existTantomas(String tantousyacode, QueryRunner queryRunnerWip) throws SQLException {
        String sql = "SELECT tantousyacode "
                + "FROM tantomas WHERE tantousyacode = ? and zaiseki = '1' ";

        List<Object> params = new ArrayList<>();
        params.add(tantousyacode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map tantomas = queryRunnerWip.query(sql, new MapHandler(), params.toArray());
        if (null == tantomas || tantomas.isEmpty()) {
            return false;
        }
        return true;

    }

}
