/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrSyosei;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

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
 * GXHDO101A(焼成画面)ロジック
 *
 * @author KCCS D.Yanagida
 * @since 2018/04/24
 */
public class GXHDO101A implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());

    private static final String CHARSET = "MS932";
    private static final int LOTNO_BYTE = 14;

    /**
     * 初期化処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    @Override
    public ProcessData initial(ProcessData processData) {
        // 初期化処理を呼び出す
        processData = this.resetData(processData);
        // ボタンの活性・非活性を設定
        processData = this.setButtonEnable(processData, "initial");
        return processData;
    }

    /**
     * ボタン活性・非活性設定
     *
     * @param processData 処理制御データ
     * @param process 処理名
     * @return 処理制御データ
     */
    private ProcessData setButtonEnable(ProcessData processData, String process) {
        List<String> activeIdList = new ArrayList<>();
        List<String> inactiveIdList = new ArrayList<>();
        switch (process) {
            case "initial":
            case "reset":
                activeIdList.addAll(Arrays.asList("syosei_wipTorikomi_Top", "syosei_wipTorikomi_Bottom",
                        "syosei_touroku_Top", "syosei_touroku_Bottom", "syosei_kensaku_Top", "syosei_kensaku_Bottom",
                        "syosei_reset_Top", "syosei_reset_Bottom"));
                inactiveIdList.addAll(Arrays.asList("syosei_shuusei_Top", "syosei_shuusei_Bottom",
                        "syosei_sakujo_Top", "syosei_sakujo_Bottom"));
                break;

            case "wipImport":
            case "insert":
            case "update":
            case "delete":
                break;

            case "search":
                activeIdList.addAll(Arrays.asList("syosei_shuusei_Top", "syosei_shuusei_Bottom", "syosei_sakujo_Top", "syosei_sakujo_Bottom"));
                break;
        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);

        return processData;
    }

    /**
     * ボタンID⇒メソッド名変換
     *
     * @param buttonId ボタンID
     * @return メソッド名
     */
    @Override
    public String convertButtonIdToMethod(String buttonId) {
        String method = "";

        switch (buttonId) {
            case "syosei_wipTorikomi_Top":
            case "syosei_wipTorikomi_Bottom":
                method = "wipImport";
                break;
            case "syosei_touroku_Top":
            case "syosei_touroku_Bottom":
                method = "saveData";
                break;
            case "syosei_kensaku_Top":
            case "syosei_kensaku_Bottom":
                method = "searchData";
                break;
            case "syosei_shuusei_Top":
            case "syosei_shuusei_Bottom":
                method = "modifyData";
                break;
            case "syosei_sakujo_Top":
            case "syosei_sakujo_Bottom":
                method = "deleteData";
                break;
            case "syosei_reset_Top":
            case "syosei_reset_Bottom":
                method = "resetDataConfirm";
                break;
            default:
                method = "error";
                break;
        }

        return method;
    }

    /**
     * ﾘｾｯﾄ処理<BR>
     * 確認ﾀﾞｲｱﾛｸﾞ表示設定のみ。ﾘｾｯﾄ処理はresetDataExec()で実施<BR>
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData resetDataConfirm(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("resetDataConfirm");
        // 確認メッセージ出力設定
        processData.setWarnMessage("画面をリセットします。よろしいですか？");
        // 後続処理メソッド設定
        processData.setMethod("resetData");

        return processData;
    }

    /**
     * ﾘｾｯﾄ処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData resetData(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("resetData");
        // 後続処理メソッド設定
        processData.setMethod("");

        // データを設定する
        for (FXHDD01 fxhcd001 : processData.getItemList()) {
            this.setItemData(processData, fxhcd001.getItemId(), fxhcd001.getInputDefault());
        }

        // ボタンの活性・非活性を設定
        processData = this.setButtonEnable(processData, "reset");

        return processData;
    }

    /**
     * 検索
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData searchData(ProcessData processData) {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

            processData.setProcessName("searchData");

            // 入力チェック
            // 回数未入力チェック
            FXHDD01 itemRowKaisu = this.getItemRow(processData.getItemList(), "syosei_kaisuu");
            String kaisu = itemRowKaisu.getValue();
            if (StringUtil.isEmpty(kaisu)) {
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000002", itemRowKaisu.getLabel1()));
                return processData;
            }

            // ロットNo桁数チェック
            FXHDD01 itemRowLot = this.getItemRow(processData.getItemList(), "syosei_LotNo");
            String lotNo = itemRowLot.getValue();
            if (LOTNO_BYTE != StringUtil.getByte(lotNo, CHARSET, LOGGER)) {
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000003", itemRowLot.getLabel1(), LOTNO_BYTE));
                return processData;
            }

            // 焼成データ取得
            SrSyosei syosei = this.loadSyoseiData(queryRunnerQcdb, lotNo, Integer.valueOf(kaisu));
            if (null == syosei) {
                processData.setMethod("");
                processData.setInfoMessage("データは存在しません。");
                return processData;
            } else {
                // データを転送する
                this.setItemData(processData, "syosei_LotNo",
                        StringUtil.nullToBlank(syosei.getKojyo()) + StringUtil.nullToBlank(syosei.getLotno()) + StringUtil.nullToBlank(syosei.getEdaban()));
                this.setItemData(processData, "syosei_KCPNO", StringUtil.nullToBlank(syosei.getKcpno()));
                this.setItemData(processData, "syosei_kosuu", StringUtil.nullToBlank(syosei.getKosuu()));
                this.setItemData(processData, "syosei_genryo", StringUtil.nullToBlank(syosei.getGenryohinsyumei()));
                this.setItemData(processData, "syosei_kaisibi", DateUtil.getDisplayDate(syosei.getSkaisinichiji(), DateUtil.YYMMDD));
                this.setItemData(processData, "syosei_kaisijikoku", DateUtil.getDisplayTime(syosei.getSkaisinichiji(), DateUtil.HHMM));
                this.setItemData(processData, "syosei_shuryoubi", DateUtil.getDisplayDate(syosei.getSsyuryonichiji(), DateUtil.YYMMDD));
                this.setItemData(processData, "syosei_shuryoujikoku", DateUtil.getDisplayTime(syosei.getSsyuryonichiji(), DateUtil.HHMM));
                this.setItemData(processData, "syosei_BProgramNo", StringUtil.nullToBlank(syosei.getBprogramno()));
                this.setItemData(processData, "syosei_ondo", StringUtil.nullToBlank(syosei.getSyoseiondo()));
                this.setItemData(processData, "syosei_tunnel_batchFurnaceGouki", StringUtil.nullToBlank(syosei.getGoki()));
                this.setItemData(processData, "syosei_setterMaisuu", StringUtil.nullToBlank(syosei.getSsettermaisuu()));
                this.setItemData(processData, "syosei_nyuroDaibanMaiSuu_seisuu", StringUtil.nullToBlank(syosei.getNyurodaibanmaisuu()));
                this.setItemData(processData, "syosei_startTantousha", StringUtil.nullToBlank(syosei.getSkaisitantosya()));
                this.setItemData(processData, "syosei_EndTantousha", StringUtil.nullToBlank(syosei.getSsyuryotantosya()));
                this.setItemData(processData, "syosei_saisankaGouki", StringUtil.nullToBlank(syosei.getSankaGoki()));
                this.setItemData(processData, "syosei_saisankaOndo", StringUtil.nullToBlank(syosei.getSankaOndo()));
                this.setItemData(processData, "syosei_saisankaShuryoubi", DateUtil.getDisplayDate(syosei.getSankaSyuryoNichiJi(), DateUtil.YYMMDD));
                this.setItemData(processData, "syosei_saisankaShuryoujikoku", DateUtil.getDisplayTime(syosei.getSankaSyuryoNichiJi(), DateUtil.HHMM));
                this.setItemData(processData, "syosei_bikou1", StringUtil.nullToBlank(syosei.getBiko1()));
                this.setItemData(processData, "syosei_bikou2", StringUtil.nullToBlank(syosei.getBiko2()));
                this.setItemData(processData, "syosei_bikou3", StringUtil.nullToBlank(syosei.getBiko3()));
                this.setItemData(processData, "syosei_bikou4", StringUtil.nullToBlank(syosei.getBiko4()));
                this.setItemData(processData, "syosei_kaisuu", StringUtil.nullToBlank(syosei.getJissekino()));

                processData.setMethod("");
                processData.setInfoMessage("データを表示しました。");

                // ボタンの活性・非活性を設定
                processData = this.setButtonEnable(processData, "search");

                return processData;
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessage("実行時エラー");
            return processData;
        }
    }

    /**
     * WIP取込
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData wipImport(ProcessData processData) {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

            processData.setProcessName("wipImport");

            // 入力チェック
            FXHDD01 itemRow = this.getItemRow(processData.getItemList(), "syosei_LotNo");
            String lotNo = itemRow.getValue();

            // ロットNo桁数チェック
            if (LOTNO_BYTE != StringUtil.getByte(lotNo, CHARSET, LOGGER)) {
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000003", itemRow.getLabel1(), LOTNO_BYTE));
                return processData;
            }

            // 仕掛情報データの取得
            Map sikakariData = this.loadShikakariData(queryRunnerWip, lotNo);
            if (null == sikakariData || sikakariData.isEmpty()) {
                // 仕掛情報データが取得出来なかった場合エラー終了
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
                return processData;
            }

            // 実績情報の取得
            Map jissekiData3 = this.loadJissekiData(queryRunnerWip, lotNo, "'DCK500', 'DCK600'");
            if (null == jissekiData3 || jissekiData3.isEmpty()) {
                // 実績情報データが取得出来なかった場合エラー終了
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
                return processData;
            }

            // 生産情報の取得
            Map seisanData4 = this.loadSeisanData(queryRunnerWip, jissekiData3.get("jissekino").toString());
            if (null == seisanData4 || seisanData4.isEmpty()) {
                // 生産情報データが取得出来なかった場合エラー終了
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
                return processData;
            }

            // 設計情報の取得
            Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);

            // 実績情報の取得
            Map jissekiData6 = this.loadJissekiData(queryRunnerWip, lotNo, "'DDK100'");
            if (null == jissekiData6 || jissekiData6.isEmpty()) {
                // 実績情報データが取得出来なかった場合エラー終了
                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
                return processData;
            }

            // 実績情報の取得
            Map jissekiData7 = this.loadJissekiData(queryRunnerWip, lotNo, "'DEK100', 'DEK200', 'DEK300'");

            // 生産情報の取得
            Map seisanData8 = null;
            if (null != jissekiData7 && !jissekiData7.isEmpty()) {
                seisanData8 = this.loadSeisanData(queryRunnerWip, jissekiData7.get("jissekino").toString());
            }

            // 取得データを画面に反映する
            processData.setProcessName("wipImport");
            processData.setMethod("");
            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, "wipImport");

            this.setItemData(processData, "syosei_KCPNO", StringUtil.nullToBlank(sikakariData.get("kcpno")));
            this.setItemData(processData, "syosei_kosuu", StringUtil.nullToBlank(jissekiData3.get("syorisuu")));
            this.setItemData(processData, "syosei_genryo", StringUtil.nullToBlank(sikakariData.get("genryo")));
            this.setItemData(processData, "syosei_kaisibi", DateUtil.getDisplayDate(jissekiData3.get("syoribi"), DateUtil.YYMMDD));
            this.setItemData(processData, "syosei_kaisijikoku", DateUtil.getDisplayTime(jissekiData3.get("syorijikoku"), DateUtil.HHMM));
            this.setItemData(processData, "syosei_shuryoubi", DateUtil.getDisplayDate(jissekiData6.get("syoribi"), DateUtil.YYMMDD));
            this.setItemData(processData, "syosei_shuryoujikoku", DateUtil.getDisplayTime(jissekiData6.get("syorijikoku"), DateUtil.HHMM));
            if (null != sekkeiData && !sekkeiData.isEmpty()) {
                this.setItemData(processData, "syosei_ondo", StringUtil.nullToBlank(NumberUtil.convertIntData(sekkeiData.get("SYOONDO"))));
            } else {
                this.setItemData(processData, "syosei_ondo", "");
            }
            this.setItemData(processData, "syosei_tunnel_batchFurnaceGouki", StringUtil.nullToBlank(seisanData4.get("goukicode")));
            this.setItemData(processData, "syosei_startTantousha", StringUtil.nullToBlank(jissekiData3.get("tantousyacode")));
            this.setItemData(processData, "syosei_EndTantousha", StringUtil.nullToBlank(jissekiData6.get("tantousyacode")));
            if (null != seisanData8 && !seisanData8.isEmpty()) {
                this.setItemData(processData, "syosei_saisankaGouki", StringUtil.nullToBlank(seisanData8.get("goukicode")));
            } else {
                this.setItemData(processData, "syosei_saisankaGouki", "");
            }
            if (null != jissekiData7 && !jissekiData7.isEmpty()) {
                this.setItemData(processData, "syosei_saisankaShuryoubi", DateUtil.getDisplayDate(jissekiData7.get("syoribi"), DateUtil.YYMMDD));
                this.setItemData(processData, "syosei_saisankaShuryoujikoku", DateUtil.getDisplayTime(jissekiData7.get("syorijikoku"), DateUtil.HHMM));
            } else {
                this.setItemData(processData, "syosei_saisankaShuryoubi", "");
                this.setItemData(processData, "syosei_saisankaShuryoujikoku", "");
            }

            return processData;

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessage("実行時エラー");
            return processData;
        }
    }

    /**
     * 登録処理(起動メソッド)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData saveData(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("saveData");
        // 後続処理メソッド設定
        processData.setMethod("checkData");

        return processData;
    }

    /**
     * 修正処理(起動メソッド)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData modifyData(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("modifyData");
        // 後続処理メソッド設定
        processData.setMethod("checkData");

        return processData;
    }

    /**
     * 削除処理(起動メソッド)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData deleteData(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("deleteData");
        // 後続処理メソッド設定
        processData.setMethod("checkData");

        return processData;
    }

    /**
     * チェック処理
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkData(ProcessData processData) {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
            List<FXHDD01> itemList = processData.getItemList();
            ValidateUtil validateUtil = new ValidateUtil();

            if ("saveData".equals(processData.getProcessName())
                    || "modifyData".equals(processData.getProcessName())) {

                // 【登録】、【修正】共通必須ﾁｪｯｸ
                List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
                // ロットNo
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_LotNo", null, null));
                // KCPNo
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_KCPNO", null, null));
                // 個数
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kosuu", null, null));
                // 原料品種名
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_genryo", null, null));
                // 焼成開始日
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kaisibi", null, null));
                // 焼成開始時刻
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kaisijikoku", null, null));
                // 焼成終了日
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_shuryoubi", null, null));
                // 焼成終了時刻
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_shuryoujikoku", null, null));
                // 焼成温度
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S007, "syosei_ondo", null, null));
                // トンネル炉・バッチ炉号機
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_tunnel_batchFurnaceGouki", null, null));
                // 焼成セッター枚数
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_setterMaisuu", null, null));
                // 焼成開始担当者
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_startTantousha", null, null));
                // 焼成終了担当者
                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_EndTantousha", null, null));

                String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, itemList);
                if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(requireCheckErrorMessage);
                    return processData;
                }

                // 【登録】、【修正】共通入力値ﾁｪｯｸ
                List<ValidateUtil.ValidateInfo> inputCheckList1 = new ArrayList<>();
                // ロットNo
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.E001, "syosei_LotNo", null, null));
                // 個数
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_kosuu", BigDecimal.valueOf(9999999), BigDecimal.ZERO));
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_kosuu", BigDecimal.valueOf(9999999), BigDecimal.ZERO));
                // 焼成開始日
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S005, "syosei_kaisibi", null, null));
                // 焼成開始時刻
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S006, "syosei_kaisijikoku", null, null));
                // 焼成終了日
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S005, "syosei_shuryoubi", null, null));
                // 焼成終了時刻
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S006, "syosei_shuryoujikoku", null, null));
                // 焼成温度
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_ondo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_ondo", BigDecimal.valueOf(9999), BigDecimal.ZERO));

                String inputCheck1ErrorMessage = validateUtil.executeValidation(inputCheckList1, itemList);
                if (!StringUtil.isEmpty(inputCheck1ErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(inputCheck1ErrorMessage);
                    return processData;
                }

                // トンネル炉・バッチ炉号機(レコード存在チェック)
                String goukiInput = this.getItemData(itemList, "syosei_tunnel_batchFurnaceGouki");
                String goukiItemName = this.getItemName(itemList, "syosei_tunnel_batchFurnaceGouki");
                boolean goukimasExist = validateUtil.checkT002T003(queryRunnerWip, "goukimas",
                        new ArrayList<>(Arrays.asList("goukicode")), new ArrayList<>(Arrays.asList(goukiInput)));
                if (!goukimasExist) {
                    processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", goukiItemName));
                    return processData;
                }

                List<ValidateUtil.ValidateInfo> inputCheckList2 = new ArrayList<>();

                // 焼成セッター枚数
                inputCheckList2.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_setterMaisuu", BigDecimal.valueOf(999), BigDecimal.ZERO));
                inputCheckList2.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_setterMaisuu", BigDecimal.valueOf(999), BigDecimal.ZERO));

                String inputCheck2ErrorMessage = validateUtil.executeValidation(inputCheckList2, itemList);
                if (!StringUtil.isEmpty(inputCheck2ErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(inputCheck2ErrorMessage);
                    return processData;
                }

                // 焼成開始担当者
                String startTantouInput = this.getItemData(itemList, "syosei_startTantousha");
                String startTantouItemName = this.getItemName(itemList, "syosei_startTantousha");
                boolean startTantouExist = validateUtil.checkT002T003(queryRunnerWip, "tantomas",
                        new ArrayList<>(Arrays.asList("tantousyacode", "zaiseki")),
                        new ArrayList<>(Arrays.asList(startTantouInput, "1")));
                if (!startTantouExist) {
                    processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", startTantouItemName));
                    return processData;
                }
                // 焼成終了担当者
                String endTantouInput = this.getItemData(itemList, "syosei_EndTantousha");
                String endTantouItemName = this.getItemName(itemList, "syosei_EndTantousha");
                boolean endTantouExist = validateUtil.checkT002T003(queryRunnerWip, "tantomas",
                        new ArrayList<>(Arrays.asList("tantousyacode", "zaiseki")),
                        new ArrayList<>(Arrays.asList(endTantouInput, "1")));
                if (!endTantouExist) {
                    processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", endTantouItemName));
                    return processData;
                }

                // 【その他】入力項目チェック
                List<ValidateUtil.ValidateInfo> inputCheckList3 = new ArrayList<>();
                // バッチ炉プログラムNo
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_BProgramNo"))) {
                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_BProgramNo", BigDecimal.valueOf(99), BigDecimal.ZERO));
                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_BProgramNo", BigDecimal.valueOf(99), BigDecimal.ZERO));
                }
                // 入炉台板枚数
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_nyuroDaibanMaiSuu_seisuu"))) {
                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_nyuroDaibanMaiSuu_seisuu", BigDecimal.valueOf(99.99), BigDecimal.ZERO));
                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_nyuroDaibanMaiSuu_seisuu", BigDecimal.valueOf(99.99), BigDecimal.ZERO));
                }

                String inputCheck3ErrorMessage = validateUtil.executeValidation(inputCheckList3, itemList);
                if (!StringUtil.isEmpty(inputCheck3ErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(inputCheck3ErrorMessage);
                    return processData;
                }

                // 再酸化号機(レコード存在チェック)
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaGouki"))) {
                    String sankaGoukiInput = this.getItemData(itemList, "syosei_saisankaGouki");
                    String sankaGoukiItemName = this.getItemName(itemList, "syosei_saisankaGouki");
                    boolean sankaGoukimasExist = validateUtil.checkT002T003(queryRunnerWip, "goukimas",
                            new ArrayList<>(Arrays.asList("goukicode")), new ArrayList<>(Arrays.asList(sankaGoukiInput)));
                    if (!sankaGoukimasExist) {
                        processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", sankaGoukiItemName));
                        return processData;
                    }
                }

                List<ValidateUtil.ValidateInfo> inputCheckList4 = new ArrayList<>();
                // 再酸化温度
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaOndo"))) {
                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_saisankaOndo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_saisankaOndo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
                }
                // 再酸化終了日
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaShuryoubi"))) {
                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S005, "syosei_saisankaShuryoubi", null, null));
                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_saisankaShuryoujikoku", null, null));
                }
                // 再酸化終了時刻
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaShuryoujikoku"))) {
                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S006, "syosei_saisankaShuryoujikoku", null, null));
                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_saisankaShuryoubi", null, null));
                }

                String inputCheck4ErrorMessage = validateUtil.executeValidation(inputCheckList4, itemList);
                if (!StringUtil.isEmpty(inputCheck4ErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(inputCheck4ErrorMessage);
                    return processData;
                }

                // 時刻前後チェック(再酸化終了日/再酸化終了時刻)
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaShuryoubi"))) {
                    String errorMessageDate1
                            = validateUtil.checkR001(itemList, "syosei_shuryoubi", "syosei_shuryoujikoku", "syosei_saisankaShuryoubi", "syosei_saisankaShuryoujikoku");
                    if (!StringUtil.isEmpty(errorMessageDate1)) {
                        processData.setErrorMessage(errorMessageDate1);
                        return processData;
                    }
                }

                List<ValidateUtil.ValidateInfo> inputCheckList5 = new ArrayList<>();
                // 回数
                if ("saveData".equals(processData.getProcessName())) {
                    String lotNo = this.getItemData(itemList, "syosei_LotNo");
                    int registJissekiNo = this.getSyoseiNextKaisu(queryRunnerQcdb, lotNo);
                    this.setItemData(processData, "syosei_kaisuu", String.valueOf(registJissekiNo));
                }
                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_kaisuu"))) {
                    inputCheckList5.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_kaisuu", BigDecimal.valueOf(9999), BigDecimal.ZERO));
                    inputCheckList5.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_kaisuu", BigDecimal.valueOf(9999), BigDecimal.ZERO));
                }

                String inputCheck5ErrorMessage = validateUtil.executeValidation(inputCheckList5, itemList);
                if (!StringUtil.isEmpty(inputCheck5ErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(inputCheck5ErrorMessage);
                    return processData;
                }

                // 時間前後チェック
                String errorMessageDate2
                        = validateUtil.checkR001(itemList, "syosei_kaisibi", "syosei_kaisijikoku", "syosei_shuryoubi", "syosei_shuryoujikoku");
                if (!StringUtil.isEmpty(errorMessageDate2)) {
                    processData.setErrorMessage(errorMessageDate2);
                    return processData;
                }
            }

            if ("deleteData".equals(processData.getProcessName())) {
                // 【削除】
                List<ValidateUtil.ValidateInfo> deleteCheckList = new ArrayList<>();
                // ロットNo
                deleteCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_LotNo", null, null));
                // 回数
                deleteCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kaisuu", null, null));
                // ロットNo(チェックデジットチェック)
                deleteCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.E001, "syosei_LotNo", null, null));

                String checkErrorMessage = validateUtil.executeValidation(deleteCheckList, itemList);
                if (!StringUtil.isEmpty(checkErrorMessage)) {
                    // チェックエラーが存在する場合処理終了
                    processData.setErrorMessage(checkErrorMessage);
                    return processData;
                }
            }

            // チェック処理でエラーが存在しない場合、確認メッセージ及び後続処理を登録して処理続行
            if ("saveData".equals(processData.getProcessName())) {
                if (1 < this.getSyoseiNextKaisu(queryRunnerQcdb, this.getItemData(itemList, "syosei_LotNo"))) {
                    // 取得した実績Noが1以上の場合、警告メッセージを表示する
                    processData.setWarnMessage("登録済みのデータがあります。<br/>登録してもよろしいですか？");
                } else {
                    processData.setWarnMessage("データベースに登録します。よろしいですか？");
                }
            } else if ("modifyData".equals(processData.getProcessName())) {
                processData.setUserAuthParam("syosei_update_button");
                processData.setRquireAuth(true);
                processData.setWarnMessage("修正します。よろしいですか？");
            } else if ("deleteData".equals(processData.getProcessName())) {
                // 削除完了メッセージ表示
                processData.setUserAuthParam("syosei_delete_button");
                processData.setRquireAuth(true);
                processData.setWarnMessage("データを削除します。よろしいですか？");
            }
            processData.setMethod("registData");
            return processData;

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessage("実行時エラー");
            return processData;
        }
    }

    /**
     * データ登録処理
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData registData(ProcessData processData) {
        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            int effectiveCount = 0;
            if ("saveData".equals(processData.getProcessName())) {
                // 登録処理
                effectiveCount = this.insert(queryRunnerQcdb, processData);
                // 登録完了メッセージ表示
                processData.setWarnMessage("登録しました。<br/><br/>画面をリセットします。よろしいですか？");
                // ボタンの活性・非活性を設定
                processData = this.setButtonEnable(processData, "insert");
            } else if ("modifyData".equals(processData.getProcessName())) {
                // 更新処理
                effectiveCount = this.update(queryRunnerQcdb, processData);
                // 登録完了メッセージ表示
                processData.setWarnMessage("更新しました。<br/><br/>画面をリセットします。よろしいですか？");
                // ボタンの活性・非活性を設定
                processData = this.setButtonEnable(processData, "update");
            } else if ("deleteData".equals(processData.getProcessName())) {
                // 削除実行
                effectiveCount = this.delete(queryRunnerQcdb, processData);
                // 削除完了メッセージ表示
                processData.setWarnMessage("削除しました。<br/><br/>画面をリセットします。よろしいですか？");
                // ボタンの活性・非活性を設定
                processData = this.setButtonEnable(processData, "delete");
            }

            // 処理件数を比較します。
            if (0 != effectiveCount) {
                // 後続処理メソッド設定
                processData.setMethod("resetData");
            } else {
                // 更新件数0件の場合、例外と同様に処理します。
                processData = createRegistDataErrorMessage(processData);
            }
            return processData;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;
        }
    }

    /**
     * 焼成データ登録実行
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(QCDB)
     * @param processData 処理制御データ
     * @throws SQLException
     */
    private int insert(QueryRunner queryRunnerQcdb, ProcessData processData) throws SQLException {
        // 焼成データの登録
        String sql = "INSERT INTO sr_syosei ("
                + "kojyo, lotno, edaban, jissekino, kcpno, kosuu, genryohinsyumei, genryogroup, skaisinichiji, ssyuryonichiji, "
                + "bprogramno, syoseiondo, goki, ssettermaisuu, nyurodaibanmaisuu, skaisitantosya, ssyuryotantosya, "
                + "biko1, biko2, biko3, biko4, biko5, bkaisinichiji, bsyuryonichiji, bsettermaisuu, potsuu, potno, btantosya, "
                + "biko6, biko7, torokunichiji, kosinnichiji, SankaGoki, SankaOndo, SankaSyuryoNichiJi "
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = this.getInsertUpdateParams(queryRunnerQcdb, processData, false);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.update(sql, params.toArray());
    }

    /**
     * 焼成データ更新実行
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(QCDB)
     * @param processData 処理制御データ
     * @throws SQLException
     */
    private int update(QueryRunner queryRunnerQcdb, ProcessData processData) throws SQLException {
        // 焼成データの登録
        String sql = "UPDATE sr_syosei SET "
                + "kcpno = ?, kosuu = ?, genryohinsyumei = ?, genryogroup = ?, skaisinichiji = ?, ssyuryonichiji = ?, bprogramno = ?, "
                + "syoseiondo = ?, goki = ?, ssettermaisuu = ?, nyurodaibanmaisuu = ?, skaisitantosya = ?, ssyuryotantosya = ?, "
                + "biko1 = ?, biko2 = ?, biko3 = ?, biko4 = ?, biko5 = ?, bkaisinichiji = ?, bsyuryonichiji = ?, bsettermaisuu = ?, "
                + "potsuu = ?, potno = ?, btantosya = ?, biko6 = ?, biko7 = ?, kosinnichiji = ?, SankaGoki = ?, SankaOndo = ?, SankaSyuryoNichiJi = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ?";

        List<Object> params = this.getInsertUpdateParams(queryRunnerQcdb, processData, true);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.update(sql, params.toArray());
    }

    /**
     * 登録・更新処理パラメータ
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(QCDB)
     * @param listData 項目データ
     * @param isUpdate 更新用パラメータ生成の場合true
     * @return パラメータリスト
     * @throws SQLException
     */
    private List<Object> getInsertUpdateParams(QueryRunner queryRunnerQcdb, ProcessData processData, boolean isUpdate) throws SQLException {
        List<Object> params = new ArrayList<>();
        List<FXHDD01> listData = processData.getItemList();

        Date now = new Date();
        String lotNo = this.getItemData(listData, "syosei_LotNo");

        if (!isUpdate) {
            params.add(lotNo.substring(0, 3));
            params.add(lotNo.substring(3, 11));
            params.add(lotNo.substring(11, 14));
            params.add(this.getItemData(listData, "syosei_kaisuu"));
        }

        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_KCPNO")));
        params.add(DBUtil.stringToIntObject(this.getItemData(listData, "syosei_kosuu")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_genryo")));
        params.add("");
        params.add(DBUtil.stringToDateObject(this.getItemData(listData, "syosei_kaisibi"),
                this.getItemData(listData, "syosei_kaisijikoku")));
        params.add(DBUtil.stringToDateObject(this.getItemData(listData, "syosei_shuryoubi"),
                this.getItemData(listData, "syosei_shuryoujikoku")));
        params.add(DBUtil.stringToIntObject(this.getItemData(listData, "syosei_BProgramNo")));
        params.add(DBUtil.stringToIntObject(this.getItemData(listData, "syosei_ondo")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_tunnel_batchFurnaceGouki")));
        params.add(DBUtil.stringToIntObject(this.getItemData(listData, "syosei_setterMaisuu")));
        params.add(DBUtil.stringToBigDecimalObject(this.getItemData(listData, "syosei_nyuroDaibanMaiSuu_seisuu")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_startTantousha")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_EndTantousha")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_bikou1")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_bikou2")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_bikou3")));
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_bikou4")));
        params.add("");
        params.add("0000-00-00 00:00:00");
        params.add("0000-00-00 00:00:00");
        params.add(0);
        params.add(0);
        params.add("");
        params.add("");
        params.add("");
        params.add("");
        if (!isUpdate) {
            params.add(now);
        }
        params.add(now);
        params.add(DBUtil.stringToStringObject(this.getItemData(listData, "syosei_saisankaGouki")));
        params.add(DBUtil.stringToIntObject(this.getItemData(listData, "syosei_saisankaOndo")));
        params.add(DBUtil.stringToDateObject(this.getItemData(listData, "syosei_saisankaShuryoubi"),
                this.getItemData(listData, "syosei_saisankaShuryoujikoku")));

        if (isUpdate) {
            params.add(lotNo.substring(0, 3));
            params.add(lotNo.substring(3, 11));
            params.add(lotNo.substring(11, 14));
            params.add(DBUtil.stringToIntObject(this.getItemData(listData, "syosei_kaisuu")));
        }

        return params;
    }

    /**
     * 焼成データ削除実行
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param processData 処理制御データ
     * @throws SQLException
     */
    private int delete(QueryRunner queryRunnerQcdb, ProcessData processData) throws SQLException {
        String lotNo = this.getItemData(processData.getItemList(), "syosei_LotNo");
        String kaisu = this.getItemData(processData.getItemList(), "syosei_kaisuu");
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 焼成データの削除
        String sql = "DELETE FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);
        params.add(Integer.valueOf(kaisu));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.update(sql, params.toArray());
    }

    /**
     * (2)仕掛データ検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT kcpno, genryo "
                + "FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (3)、(6)、(7)実績情報取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @param inCondition IN句の条件
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String inCondition) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 実績データの取得
        String sql = "SELECT tantousyacode, syorisuu, syoribi, syorijikoku, jissekino FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND koteicode IN (" + inCondition + ") "
                + "ORDER BY jissekino DESC ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (4)、(8)生産情報取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param jissekiNo 実績No
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadSeisanData(QueryRunner queryRunnerWip, String jissekiNo) throws SQLException {
        // 生産データの取得
        String sql = "SELECT goukicode FROM seisan WHERE jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(Integer.valueOf(jissekiNo));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (5)設計情報取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);

        // 設計データの取得
        String sql = "SELECT SYOONDO FROM da_sekkei "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 焼成データ取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo
     * @param kaisu 回数
     * @return 焼成データ
     * @throws SQLException
     */
    private SrSyosei loadSyoseiData(QueryRunner queryRunnerQcdb, String lotNo, int kaisu) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 焼成データの取得
        String sql = "SELECT kojyo, lotno, edaban, jissekino, kcpno, kosuu, genryohinsyumei, genryogroup, "
                + "skaisinichiji, ssyuryonichiji, bprogramno, syoseiondo, goki, ssettermaisuu, "
                + "nyurodaibanmaisuu, skaisitantosya, ssyuryotantosya, biko1, biko2, biko3, biko4, biko5, "
                + "bkaisinichiji, bsyuryonichiji, bsettermaisuu, potsuu, potno, btantosya, biko6, biko7, "
                + "torokunichiji, kosinnichiji, SankaGoki, SankaOndo, SankaSyuryoNichiJi "
                + "FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);
        params.add(kaisu);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo");
        mapping.put("lotno", "lotno");
        mapping.put("edaban", "edaban");
        mapping.put("jissekino", "jissekino");
        mapping.put("kcpno", "kcpno");
        mapping.put("kosuu", "kosuu");
        mapping.put("genryohinsyumei", "genryohinsyumei");
        mapping.put("genryogroup", "genryogroup");
        mapping.put("skaisinichiji", "skaisinichiji");
        mapping.put("ssyuryonichiji", "ssyuryonichiji");
        mapping.put("bprogramno", "bprogramno");
        mapping.put("syoseiondo", "syoseiondo");
        mapping.put("goki", "goki");
        mapping.put("ssettermaisuu", "ssettermaisuu");
        mapping.put("nyurodaibanmaisuu", "nyurodaibanmaisuu");
        mapping.put("skaisitantosya", "skaisitantosya");
        mapping.put("ssyuryotantosya", "ssyuryotantosya");
        mapping.put("biko1", "biko1");
        mapping.put("biko2", "biko2");
        mapping.put("biko3", "biko3");
        mapping.put("biko4", "biko4");
        mapping.put("biko5", "biko5");
        mapping.put("bkaisinichiji", "bkaisinichiji");
        mapping.put("bsyuryonichiji", "bsyuryonichiji");
        mapping.put("bsettermaisuu", "bsettermaisuu");
        mapping.put("potsuu", "potsuu");
        mapping.put("potno", "potno");
        mapping.put("btantosya", "btantosya");
        mapping.put("biko6", "biko6");
        mapping.put("biko7", "biko7");
        mapping.put("torokunichiji", "torokunichiji");
        mapping.put("kosinnichiji", "kosinnichiji");
        mapping.put("SankaGoki", "sankaGoki");
        mapping.put("SankaOndo", "sankaOndo");
        mapping.put("SankaSyuryoNichiJi", "sankaSyuryoNichiJi");

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<SrSyosei> beanHandler = new BeanHandler(SrSyosei.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (9)、(10)焼成データ 回数採番
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private int getSyoseiNextKaisu(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 焼成データの取得
        String sql = "SELECT MAX(jissekino) AS jisseki_max FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map result = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

        int kaisu = 1;
        if (null != result && !result.isEmpty() && null != result.get("jisseki_max")) {
            kaisu = (int) result.get("jisseki_max") + 1;
        }

        return kaisu;
    }

    /**
     * 項目データ設定
     *
     * @param processData 処理制御データ
     * @param itemId 項目ID
     * @param value 設定値
     * @return 処理制御データ
     */
    private ProcessData setItemData(ProcessData processData, String itemId, String value) {
        FXHDD01 fxhcd01
                = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList()).get(0);
        fxhcd01.setValue(value);
        return processData;
    }

    /**
     * 項目データ取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 項目データ
     */
    private FXHDD01 getItemRow(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else {
            return null;
        }
    }

    /**
     * 項目データ(項目名)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 入力値
     */
    private String getItemName(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getLabel1();
        } else {
            return null;
        }
    }

    /**
     * INSERT、UPDATE、DELTEに失敗した場合のエラーを生成します。
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ProcessData createRegistDataErrorMessage(ProcessData processData) {
        if (null != processData.getProcessName()) {
            switch (processData.getProcessName()) {
                case "saveData":
                    processData.setErrorMessage("登録に失敗しました。");
                    break;
                case "modifyData":
                    processData.setErrorMessage("修正に失敗しました。");
                    break;
                case "deleteData":
                    processData.setErrorMessage("削除に失敗しました。");
                    break;
                default:
                    break;
            }
        }
        return processData;
    }
}
