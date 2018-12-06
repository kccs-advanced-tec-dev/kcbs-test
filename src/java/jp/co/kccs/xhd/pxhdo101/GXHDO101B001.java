/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
 * 変更日	2018/11/29<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B001(SPS系印刷・SPSグラビア)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/29
 */
public class GXHDO101B001 implements IFormLogic {

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

        try {

            // 処理名を登録
            processData.setProcessName("initial");

            // データを設定する(デフォルト値)
            for (FXHDD01 fxhdd001 : processData.getItemList()) {
                this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
            }

            // 初期表示データ設定処理
            processData = setInitDate(processData);

            //******************************************************************************************
            // 膜厚(SPS)サブ画面初期表示データ設定
            processData.setGxhdo101c001Model(GXHDO101C001Logic.createGXHDO101C001Model(""));

            // PTN距離Xサブ画面初期表示データ設定
            processData.setGxhdo101c002Model(GXHDO101C002Logic.createGXHDO101C002Model(""));

            // PTN距離Yサブ画面初期表示データ設定
            processData.setGxhdo101c003Model(GXHDO101C003Logic.createGXHDO101C003Model(""));
            //******************************************************************************************

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, "initial");

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessage("実行時エラー");
            return processData;
        }

        // エラーが発生していない場合は後続処理なし
        if (StringUtil.isEmpty(processData.getErrorMessage())) {
            processData.setMethod("");
        }

        return processData;
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
     * 膜厚(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openMakuatsu(ProcessData processData) {
        processData.setProcessName("openMakuatsu");
        // コールバックパラメータにてサブ画面起動用の値を設定
        processData.setCollBackParam("gxhdo101c001");
        processData.setMethod("");
        return processData;
    }

    /**
     * PTN距離X(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriX(ProcessData processData) {
        processData.setProcessName("openPtnKyoriX");
        // コールバックパラメータにてサブ画面起動用の値を設定
        processData.setCollBackParam("gxhdo101c002");
        processData.setMethod("");
        return processData;
    }

    /**
     * PTN距離Y(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriY(ProcessData processData) {
        processData.setProcessName("openPtnKyoriY");
        // コールバックパラメータにてサブ画面起動用の値を設定
        processData.setCollBackParam("gxhdo101c003");
        processData.setMethod("");
        return processData;
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("tempResist");

        // 後続処理メソッド設定
        processData.setMethod("doTempResist");

        // 仮登録ﾁｪｯｸ処理
//        List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
//        ValidateUtil validateUtil = new ValidateUtil();
//        List<FXHDD01> itemList = processData.getItemList();
//
//        // ロットNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_LotNo", null, null));
//        // KCPNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_KCPNO", null, null));
//        // 個数
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C001, "syosei_kosuu", null, null));
//
//        String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, itemList);
//        if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
//            // チェックエラーが存在する場合処理終了
//            processData.setErrorMessage(requireCheckErrorMessage);
//            return processData;
//        }
        return processData;
    }

    /**
     * 仮登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTempResist(ProcessData processData) {
        // 後続処理メソッド設定
        processData.setMethod("");

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        try {
            // トランザクション開始
            processData.getDataSourceQcdb().getConnection().setAutoCommit(false);

            //TODO
            return processData;
        } catch (SQLException e) {
            try {
                processData.getDataSourceQcdb().getConnection().rollback();

            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }

            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;
        } finally {
            try {
                processData.getDataSourceQcdb().getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }
        }
    }

    /**
     * 登録処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataResist(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("resist");

        // 後続処理メソッド設定
        processData.setMethod("doResist");

        // 仮登録ﾁｪｯｸ処理
//        List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
//        ValidateUtil validateUtil = new ValidateUtil();
//        List<FXHDD01> itemList = processData.getItemList();
//
//        // ロットNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_LotNo", null, null));
//        // KCPNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_KCPNO", null, null));
//        // 個数
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C001, "syosei_kosuu", null, null));
//
//        String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, itemList);
//        if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
//            // チェックエラーが存在する場合処理終了
//            processData.setErrorMessage(requireCheckErrorMessage);
//            return processData;
//        }
        return processData;
    }

    /**
     * 登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doResist(ProcessData processData) {
        // 後続処理メソッド設定
        processData.setMethod("");

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        try {
            // トランザクション開始
            processData.getDataSourceQcdb().getConnection().setAutoCommit(false);

            //TODO
            return processData;
        } catch (SQLException e) {
            try {
                processData.getDataSourceQcdb().getConnection().rollback();

            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }

            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;
        } finally {
            try {
                processData.getDataSourceQcdb().getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }
        }
    }

    /**
     * 検索処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doSearch(ProcessData processData) {
//        try {
//            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
//
//            processData.setProcessName("searchData");
//
//            // 入力チェック
//            // 回数未入力チェック
//            FXHDD01 itemRowKaisu = this.getItemRow(processData.getItemList(), "syosei_kaisuu");
//            String kaisu = itemRowKaisu.getValue();
//            if (StringUtil.isEmpty(kaisu)) {
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000002", itemRowKaisu.getLabel1()));
//                return processData;
//            }
//
//            // ロットNo桁数チェック
//            FXHDD01 itemRowLot = this.getItemRow(processData.getItemList(), "syosei_LotNo");
//            String lotNo = itemRowLot.getValue();
//            if (LOTNO_BYTE != StringUtil.getByte(lotNo, CHARSET, LOGGER)) {
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000003", itemRowLot.getLabel1(), LOTNO_BYTE));
//                return processData;
//            }
//
//            // 焼成データ取得
//            SrSyosei syosei = this.loadSyoseiData(queryRunnerQcdb, lotNo, Integer.valueOf(kaisu));
//            if (null == syosei) {
//                processData.setMethod("");
//                processData.setInfoMessage("データは存在しません。");
//                return processData;
//            } else {
//                // データを転送する
//                this.setItemData(processData, "syosei_LotNo",
//                        StringUtil.nullToBlank(syosei.getKojyo()) + StringUtil.nullToBlank(syosei.getLotno()) + StringUtil.nullToBlank(syosei.getEdaban()));
//                this.setItemData(processData, "syosei_KCPNO", StringUtil.nullToBlank(syosei.getKcpno()));
//                this.setItemData(processData, "syosei_kosuu", StringUtil.nullToBlank(syosei.getKosuu()));
//                this.setItemData(processData, "syosei_genryo", StringUtil.nullToBlank(syosei.getGenryohinsyumei()));
//                this.setItemData(processData, "syosei_kaisibi", DateUtil.getDisplayDate(syosei.getSkaisinichiji(), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_kaisijikoku", DateUtil.getDisplayTime(syosei.getSkaisinichiji(), DateUtil.HHMM));
//                this.setItemData(processData, "syosei_shuryoubi", DateUtil.getDisplayDate(syosei.getSsyuryonichiji(), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_shuryoujikoku", DateUtil.getDisplayTime(syosei.getSsyuryonichiji(), DateUtil.HHMM));
//                this.setItemData(processData, "syosei_BProgramNo", StringUtil.nullToBlank(syosei.getBprogramno()));
//                this.setItemData(processData, "syosei_ondo", StringUtil.nullToBlank(syosei.getSyoseiondo()));
//                this.setItemData(processData, "syosei_tunnel_batchFurnaceGouki", StringUtil.nullToBlank(syosei.getGoki()));
//                this.setItemData(processData, "syosei_setterMaisuu", StringUtil.nullToBlank(syosei.getSsettermaisuu()));
//                this.setItemData(processData, "syosei_nyuroDaibanMaiSuu_seisuu", StringUtil.nullToBlank(syosei.getNyurodaibanmaisuu()));
//                this.setItemData(processData, "syosei_startTantousha", StringUtil.nullToBlank(syosei.getSkaisitantosya()));
//                this.setItemData(processData, "syosei_EndTantousha", StringUtil.nullToBlank(syosei.getSsyuryotantosya()));
//                this.setItemData(processData, "syosei_saisankaGouki", StringUtil.nullToBlank(syosei.getSankaGoki()));
//                this.setItemData(processData, "syosei_saisankaOndo", StringUtil.nullToBlank(syosei.getSankaOndo()));
//                this.setItemData(processData, "syosei_saisankaShuryoubi", DateUtil.getDisplayDate(syosei.getSankaSyuryoNichiJi(), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_saisankaShuryoujikoku", DateUtil.getDisplayTime(syosei.getSankaSyuryoNichiJi(), DateUtil.HHMM));
//                this.setItemData(processData, "syosei_bikou1", StringUtil.nullToBlank(syosei.getBiko1()));
//                this.setItemData(processData, "syosei_bikou2", StringUtil.nullToBlank(syosei.getBiko2()));
//                this.setItemData(processData, "syosei_bikou3", StringUtil.nullToBlank(syosei.getBiko3()));
//                this.setItemData(processData, "syosei_bikou4", StringUtil.nullToBlank(syosei.getBiko4()));
//                this.setItemData(processData, "syosei_kaisuu", StringUtil.nullToBlank(syosei.getJissekino()));
//
//                processData.setMethod("");
//                processData.setInfoMessage("データを表示しました。");
//
//                // ボタンの活性・非活性を設定
//                processData = this.setButtonEnable(processData, "search");
//
        return processData;
//            }
//
//        } catch (SQLException ex) {
//            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//            processData.setErrorMessage("実行時エラー");
//            return processData;
//        }
    }

    /**
     * 修正処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataCorrect(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("correct");

        // 後続処理メソッド設定
        processData.setMethod("doCorrect");

        // 仮登録ﾁｪｯｸ処理
//        List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
//        ValidateUtil validateUtil = new ValidateUtil();
//        List<FXHDD01> itemList = processData.getItemList();
//
//        // ロットNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_LotNo", null, null));
//        // KCPNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_KCPNO", null, null));
//        // 個数
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C001, "syosei_kosuu", null, null));
//
//        String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, itemList);
//        if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
//            // チェックエラーが存在する場合処理終了
//            processData.setErrorMessage(requireCheckErrorMessage);
//            return processData;
//        }
        return processData;
    }

    /**
     * 修正処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doCorrect(ProcessData processData) {
        // 後続処理メソッド設定
        processData.setMethod("");

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        try {
            // トランザクション開始
            processData.getDataSourceQcdb().getConnection().setAutoCommit(false);

            //TODO
            return processData;
        } catch (SQLException e) {
            try {
                processData.getDataSourceQcdb().getConnection().rollback();

            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }

            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;
        } finally {
            try {
                processData.getDataSourceQcdb().getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }
        }
    }

    /**
     * 修正処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataDelete(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("delete");

        // 後続処理メソッド設定
        processData.setMethod("doDelete");

        // 仮登録ﾁｪｯｸ処理
//        List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
//        ValidateUtil validateUtil = new ValidateUtil();
//        List<FXHDD01> itemList = processData.getItemList();
//
//        // ロットNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_LotNo", null, null));
//        // KCPNo
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C101, "syosei_KCPNO", null, null));
//        // 個数
//        requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.C001, "syosei_kosuu", null, null));
//
//        String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, itemList);
//        if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
//            // チェックエラーが存在する場合処理終了
//            processData.setErrorMessage(requireCheckErrorMessage);
//            return processData;
//        }
        return processData;
    }

    /**
     * 修正処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doDelete(ProcessData processData) {
        // 後続処理メソッド設定
        processData.setMethod("");

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        try {
            // トランザクション開始
            processData.getDataSourceQcdb().getConnection().setAutoCommit(false);

            //TODO
            return processData;
        } catch (SQLException e) {
            try {
                processData.getDataSourceQcdb().getConnection().rollback();

            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }

            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;
        } finally {
            try {
                processData.getDataSourceQcdb().getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
                processData = createRegistDataErrorMessage(processData);
                return processData;
            }
        }
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
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B001Const.BTN_DOWN_KARI_TOUROKU,
                        GXHDO101B001Const.BTN_DOWN_KENSAKU,
                        GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_SAKUJO,
                        GXHDO101B001Const.BTN_DOWN_SHUSEI,
                        GXHDO101B001Const.BTN_DOWN_TOUROKU,
                        GXHDO101B001Const.BTN_DOWN_WIP_TORIKOMI,
                        GXHDO101B001Const.BTN_UP_KARI_TOUROKU,
                        GXHDO101B001Const.BTN_UP_KENSAKU,
                        GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_SAKUJO,
                        GXHDO101B001Const.BTN_UP_SHUSEI,
                        GXHDO101B001Const.BTN_UP_TOUROKU,
                        GXHDO101B001Const.BTN_UP_WIP_TORIKOMI
                ));
                break;

            case "wipImport":
            case "resist":
            case "tempResist":
            case "correct":
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
        String method;
        switch (buttonId) {
            // WIP取込
            case GXHDO101B001Const.BTN_UP_WIP_TORIKOMI:
            case GXHDO101B001Const.BTN_DOWN_WIP_TORIKOMI:
                method = "wipImport";
                break;
            // 膜圧
            case GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN:
            case GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN:
                method = "openMakuatsu";
                break;
            // PTN距離X
            case GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN:
            case GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN:
                method = "openPtnKyoriX";
                break;
            // PTN距離Y
            case GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN:
            case GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN:
                method = "openPtnKyoriY";
                break;
            // 仮登録
            case GXHDO101B001Const.BTN_UP_KARI_TOUROKU:
            case GXHDO101B001Const.BTN_DOWN_KARI_TOUROKU:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B001Const.BTN_UP_TOUROKU:
            case GXHDO101B001Const.BTN_DOWN_TOUROKU:
                method = "checkDataResist";
                break;
            // 検索
            case GXHDO101B001Const.BTN_UP_KENSAKU:
            case GXHDO101B001Const.BTN_DOWN_KENSAKU:
                method = "doSearch";
                break;
            // 修正
            case GXHDO101B001Const.BTN_UP_SHUSEI:
            case GXHDO101B001Const.BTN_DOWN_SHUSEI:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B001Const.BTN_UP_SAKUJO:
            case GXHDO101B001Const.BTN_DOWN_SAKUJO:
                method = "checkDataDelete";
                break;
            default:
                method = "error";
                break;
        }

        return method;
    }

    /**
     * 初期表示データ設定
     *
     * @param processData
     * @return 処理制御データ
     * @throws SQLException
     */
    private ProcessData setInitDate(ProcessData processData) throws SQLException {

        //TODO
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
        if (sekkeiData == null || sekkeiData.isEmpty()) {
            processData.setErrorMessage(MessageUtil.getMessage("XHD-000014", ""));
            return processData;
        }

        // ロット区分マスタ情報の取得
        String lotKubunCode = StringUtil.nullToBlank(sekkeiData.get("KUBUN1")); // ﾛｯﾄ区分ｺｰﾄﾞ
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotKubunCode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            processData.setErrorMessage(MessageUtil.getMessage("XHD-000015", ""));
            return processData;
        }

        // オーナーマスタ情報の取得
        String ownerCode = StringUtil.nullToBlank(sekkeiData.get("OWNER")); // ｵｰﾅｰｺｰﾄﾞ
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownerCode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            processData.setErrorMessage(MessageUtil.getMessage("XHD-000016", ""));
            return processData;
        }

        // ロットNo
        this.setItemData(processData, GXHDO101B001Const.LOTNO, StringUtil.nullToBlank(lotNo));
        // KCPNO
        this.setItemData(processData, GXHDO101B001Const.KCPNO, "");
        // セット数
        this.setItemData(processData, GXHDO101B001Const.SET_SUU, StringUtil.nullToBlank(sekkeiData.get("SETSUU")));
        // 客先
        this.setItemData(processData, GXHDO101B001Const.KYAKUSAKI, StringUtil.nullToBlank(sekkeiData.get("TOKUISAKI")));
        // ロット区分
        this.setItemData(processData, GXHDO101B001Const.LOT_KUBUN, lotKubunCode + ":" + StringUtil.nullToBlank(lotKbnMasData.get("lotkubun")));
        // オーナー
        this.setItemData(processData, GXHDO101B001Const.OWNER, ownerCode + ":" + StringUtil.nullToBlank(ownerMasData.get("owner")));
        // 電極テープ
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_TAPE, StringUtil.nullToBlank(ownerMasData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(ownerMasData.get("ETAPE")));
        // 積層数
        this.setItemData(processData, GXHDO101B001Const.SEKISOU_SU, StringUtil.nullToBlank(ownerMasData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(ownerMasData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(ownerMasData.get("EMAISUU"))
                + "枚");
//        // ｽﾘｯﾌﾟﾛｯﾄNo
//        this.setItemData(processData, GXHDO101B001Const.SLIP_LOTNO, "");
//        // ﾛｰﾙNo1
//        this.setItemData(processData, GXHDO101B001Const.ROLL_NO1, "");
//        // ﾛｰﾙNo2
//        this.setItemData(processData, GXHDO101B001Const.ROLL_NO2, "");
//        // ﾛｰﾙNo3
//        this.setItemData(processData, GXHDO101B001Const.ROLL_NO3, "");
//        // 原料記号
//        this.setItemData(processData, GXHDO101B001Const.GENRYO_KIGOU, "");

        //TODO
        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B001Const.UE_COVER_TAPE1, StringUtil.nullToBlank(ownerMasData.get("TBUNRUI2"))
                + "-"
                + StringUtil.nullToBlank(ownerMasData.get("SYURUI2"))
                + "  "
                + StringUtil.nullToBlank(ownerMasData.get("ATUMI2"))
                + "μm×"
                + StringUtil.nullToBlank(ownerMasData.get("MAISUU2"))
                + "枚"
        );

        //TODO
        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B001Const.SHITA_COVER_TAPE1, StringUtil.nullToBlank(ownerMasData.get("TBUNRUI4"))
                + "-"
                + StringUtil.nullToBlank(ownerMasData.get("SYURUI4"))
                + "  "
                + StringUtil.nullToBlank(ownerMasData.get("ATUMI4"))
                + "μm×"
                + StringUtil.nullToBlank(ownerMasData.get("MAISUU4"))
                + "枚");

        //TODO
        // カバーテープ
        this.setItemData(processData, GXHDO101B001Const.COVER_TAPE, "");
        //TODO
        // 列 × 行
        this.setItemData(processData, GXHDO101B001Const.RETSU_GYOU, "");
        //TODO
        // ピッチ
        this.setItemData(processData, GXHDO101B001Const.PITCH, "");
//        // 電極ペースト
//        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_PASTE, "");
//        // ﾍﾟｰｽﾄ品名
//        this.setItemData(processData, GXHDO101B001Const.PASTE_HINMEI, "");
//        // ﾍﾟｰｽﾄﾛｯﾄNo1
//        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO1, "");
//        // ﾍﾟｰｽﾄ粘度1
//        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO1, "");
//        // ﾍﾟｰｽﾄ温度1
//        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO1, "");
//        // ﾍﾟｰｽﾄ固形分1
//        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN1, "");
//        // ﾍﾟｰｽﾄﾛｯﾄNo2
//        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO2, "");
//        // ﾍﾟｰｽﾄ粘度2
//        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO2, "");
//        // ﾍﾟｰｽﾄ温度2
//        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO2, "");
//        // ﾍﾟｰｽﾄ固形分2
//        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN2, "");
//        // 電極製版名
//        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_SEIHAN_MEI, "");
//        // 電極製版仕様
//        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_SEIHAN_SHIYOU, "");
//        // ＰＥＴフィルム種類
//        this.setItemData(processData, GXHDO101B001Const.PET_FILM_SHURUI, "");
//        // 積層スライド量
//        this.setItemData(processData, GXHDO101B001Const.SEKISOU_SLIDE_RYOU, "");
//        // 印刷号機
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_GOUKI, "");
//        // 乾燥温度表示値1
//        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, "");
//        // 乾燥温度表示値2
//        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2, "");
//        // 乾燥温度表示値3
//        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, "");
//        // 乾燥温度表示値4
//        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, "");
//        // 乾燥温度表示値5
//        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI5, "");
//        // 搬送速度
//        this.setItemData(processData, GXHDO101B001Const.HANSOU_SOKUDO, "");
//        // 開始テンション計
//        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_KEI, "");
//        // 開始テンション前
//        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_MAE, "");
//        // 開始テンション奥
//        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_OKU, "");
//        // 終了テンション計
//        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_KEI, "");
//        // 終了テンション前
//        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_MAE, "");
//        // 終了テンション奥
//        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_OKU, "");
//        // 圧胴圧力
//        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_ATSURYOKU, "");
//        // ブレード圧力
//        this.setItemData(processData, GXHDO101B001Const.BLADE_ATSURYOKU, "");
//        // 製版名 / 版胴名
//        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, "");
//        // 製版No / 版胴No
//        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, "");
//        // 製版使用枚数/版胴使用枚数
//        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, "");
//        // ｽｷｰｼﾞNo/圧胴No
//        this.setItemData(processData, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO, "");
//        // 圧胴経1
//        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_KEI1, "");
//        // 圧胴経2
//        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_KEI2, "");
//        // 圧胴経3
//        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_KEI3, "");
//        // 圧胴経4
//        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_KEI4, "");
//        // 圧胴経5
//        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_KEI5, "");
//        // ブレードNo.
//        this.setItemData(processData, GXHDO101B001Const.BLADE_NO, "");
//        // ブレード外観
//        this.setItemData(processData, GXHDO101B001Const.BLADE_GAIKAN, "");
//        // 印刷開始日
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_KAISHI_DAY, "");
//        // 印刷開始時間
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_KAISHI_TIME, "");
//        // 印刷ｽﾀｰﾄ膜厚AVE
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE, "");
//        // 印刷ｽﾀｰﾄ膜厚MAX
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX, "");
//        // 印刷ｽﾀｰﾄ膜厚MIN
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN, "");
//        // 印刷ｽﾀｰﾄ膜厚CV
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV, "");
//        // PTN間距離印刷ｽﾀｰﾄ X Min
//        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_START_X_MIN, "");
//        // PTN間距離印刷ｽﾀｰﾄ Y Min
//        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN, "");
//        // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
//        this.setItemData(processData, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, "");
//        // 印刷スタート時担当者
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA, "");
//        // 印刷終了日
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_SHUURYOU_DAY, "");
//        // 印刷終了時刻
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_SHUURYOU_TIME, "");
//        // 印刷ｴﾝﾄﾞ膜厚AVE
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE, "");
//        // 印刷ｴﾝﾄﾞ膜厚MAX
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX, "");
//        // 印刷ｴﾝﾄﾞ膜厚MIN
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN, "");
//        // 印刷ｴﾝﾄﾞ膜厚CV
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV, "");
//        // PTN間距離印刷ｴﾝﾄﾞ X Min
//        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_END_X_MIN, "");
//        // PTN間距離印刷ｴﾝﾄﾞ Y Min
//        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN, "");
//        // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
//        this.setItemData(processData, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "");
//        // 印刷エンド時担当者
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA, "");
//        // 印刷枚数
//        this.setItemData(processData, GXHDO101B001Const.INSATSU_MAISUU, "");

        return processData;

    }

    /**
     * (2)[設計]から、初期表示する情報を取得
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
        String sql = "SELECT SEKKEINO,PROCESS,SETSUU,TOKUISAKI,KUBUN1,OWNER,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,TBUNRUI2,SYURUI2,ATUMI2,"
                + "MAISUU2,TBUNRUI4,SYURUI4,ATUMI4,MAISUU4 "
                + "FROM da_sekkei "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (3)[ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadLotKbnMas(QueryRunner queryRunnerWip, String lotKubunCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT lotkubun "
                + "FROM lotkumas "
                + "WHERE lotkubuncode = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotKubunCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (4)[ｵｰﾅｰｺｰﾄﾞﾏｽﾀｰ]から、ｵｰﾅｰ名を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadOwnerMas(QueryRunner queryRunnerWip, String ownerCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT owner "
                + "FROM ownermas "
                + "WHERE ownercode = ?";

        List<Object> params = new ArrayList<>();
        params.add(ownerCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 検索
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData searchData(ProcessData processData) {
//        try {
//            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
//
//            processData.setProcessName("searchData");
//
//            // 入力チェック
//            // 回数未入力チェック
//            FXHDD01 itemRowKaisu = this.getItemRow(processData.getItemList(), "syosei_kaisuu");
//            String kaisu = itemRowKaisu.getValue();
//            if (StringUtil.isEmpty(kaisu)) {
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000002", itemRowKaisu.getLabel1()));
//                return processData;
//            }
//
//            // ロットNo桁数チェック
//            FXHDD01 itemRowLot = this.getItemRow(processData.getItemList(), "syosei_LotNo");
//            String lotNo = itemRowLot.getValue();
//            if (LOTNO_BYTE != StringUtil.getByte(lotNo, CHARSET, LOGGER)) {
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000003", itemRowLot.getLabel1(), LOTNO_BYTE));
//                return processData;
//            }
//
//            // 焼成データ取得
//            SrSyosei syosei = this.loadSyoseiData(queryRunnerQcdb, lotNo, Integer.valueOf(kaisu));
//            if (null == syosei) {
//                processData.setMethod("");
//                processData.setInfoMessage("データは存在しません。");
//                return processData;
//            } else {
//                // データを転送する
//                this.setItemData(processData, "syosei_LotNo",
//                        StringUtil.nullToBlank(syosei.getKojyo()) + StringUtil.nullToBlank(syosei.getLotno()) + StringUtil.nullToBlank(syosei.getEdaban()));
//                this.setItemData(processData, "syosei_KCPNO", StringUtil.nullToBlank(syosei.getKcpno()));
//                this.setItemData(processData, "syosei_kosuu", StringUtil.nullToBlank(syosei.getKosuu()));
//                this.setItemData(processData, "syosei_genryo", StringUtil.nullToBlank(syosei.getGenryohinsyumei()));
//                this.setItemData(processData, "syosei_kaisibi", DateUtil.getDisplayDate(syosei.getSkaisinichiji(), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_kaisijikoku", DateUtil.getDisplayTime(syosei.getSkaisinichiji(), DateUtil.HHMM));
//                this.setItemData(processData, "syosei_shuryoubi", DateUtil.getDisplayDate(syosei.getSsyuryonichiji(), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_shuryoujikoku", DateUtil.getDisplayTime(syosei.getSsyuryonichiji(), DateUtil.HHMM));
//                this.setItemData(processData, "syosei_BProgramNo", StringUtil.nullToBlank(syosei.getBprogramno()));
//                this.setItemData(processData, "syosei_ondo", StringUtil.nullToBlank(syosei.getSyoseiondo()));
//                this.setItemData(processData, "syosei_tunnel_batchFurnaceGouki", StringUtil.nullToBlank(syosei.getGoki()));
//                this.setItemData(processData, "syosei_setterMaisuu", StringUtil.nullToBlank(syosei.getSsettermaisuu()));
//                this.setItemData(processData, "syosei_nyuroDaibanMaiSuu_seisuu", StringUtil.nullToBlank(syosei.getNyurodaibanmaisuu()));
//                this.setItemData(processData, "syosei_startTantousha", StringUtil.nullToBlank(syosei.getSkaisitantosya()));
//                this.setItemData(processData, "syosei_EndTantousha", StringUtil.nullToBlank(syosei.getSsyuryotantosya()));
//                this.setItemData(processData, "syosei_saisankaGouki", StringUtil.nullToBlank(syosei.getSankaGoki()));
//                this.setItemData(processData, "syosei_saisankaOndo", StringUtil.nullToBlank(syosei.getSankaOndo()));
//                this.setItemData(processData, "syosei_saisankaShuryoubi", DateUtil.getDisplayDate(syosei.getSankaSyuryoNichiJi(), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_saisankaShuryoujikoku", DateUtil.getDisplayTime(syosei.getSankaSyuryoNichiJi(), DateUtil.HHMM));
//                this.setItemData(processData, "syosei_bikou1", StringUtil.nullToBlank(syosei.getBiko1()));
//                this.setItemData(processData, "syosei_bikou2", StringUtil.nullToBlank(syosei.getBiko2()));
//                this.setItemData(processData, "syosei_bikou3", StringUtil.nullToBlank(syosei.getBiko3()));
//                this.setItemData(processData, "syosei_bikou4", StringUtil.nullToBlank(syosei.getBiko4()));
//                this.setItemData(processData, "syosei_kaisuu", StringUtil.nullToBlank(syosei.getJissekino()));
//
//                processData.setMethod("");
//                processData.setInfoMessage("データを表示しました。");
//
//                // ボタンの活性・非活性を設定
//                processData = this.setButtonEnable(processData, "search");
//
        return processData;
//            }
//
//        } catch (SQLException ex) {
//            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//            processData.setErrorMessage("実行時エラー");
//            return processData;
//        }
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
//        try {
//            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
//            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
//            List<FXHDD01> itemList = processData.getItemList();
//            ValidateUtil validateUtil = new ValidateUtil();
//
//            if ("saveData".equals(processData.getProcessName())
//                    || "modifyData".equals(processData.getProcessName())) {
//
//                // 【登録】、【修正】共通必須ﾁｪｯｸ
//                List<ValidateUtil.ValidateInfo> requireCheckList = new ArrayList<>();
//                // ロットNo
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_LotNo", null, null));
//                // KCPNo
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_KCPNO", null, null));
//                // 個数
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kosuu", null, null));
//                // 原料品種名
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_genryo", null, null));
//                // 焼成開始日
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kaisibi", null, null));
//                // 焼成開始時刻
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kaisijikoku", null, null));
//                // 焼成終了日
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_shuryoubi", null, null));
//                // 焼成終了時刻
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_shuryoujikoku", null, null));
//                // 焼成温度
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S007, "syosei_ondo", null, null));
//                // トンネル炉・バッチ炉号機
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_tunnel_batchFurnaceGouki", null, null));
//                // 焼成セッター枚数
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_setterMaisuu", null, null));
//                // 焼成開始担当者
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_startTantousha", null, null));
//                // 焼成終了担当者
//                requireCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_EndTantousha", null, null));
//
//                String requireCheckErrorMessage = validateUtil.executeValidation(requireCheckList, itemList);
//                if (!StringUtil.isEmpty(requireCheckErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(requireCheckErrorMessage);
//                    return processData;
//                }
//
//                // 【登録】、【修正】共通入力値ﾁｪｯｸ
//                List<ValidateUtil.ValidateInfo> inputCheckList1 = new ArrayList<>();
//                // ロットNo
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.E001, "syosei_LotNo", null, null));
//                // 個数
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_kosuu", BigDecimal.valueOf(9999999), BigDecimal.ZERO));
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_kosuu", BigDecimal.valueOf(9999999), BigDecimal.ZERO));
//                // 焼成開始日
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S005, "syosei_kaisibi", null, null));
//                // 焼成開始時刻
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S006, "syosei_kaisijikoku", null, null));
//                // 焼成終了日
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S005, "syosei_shuryoubi", null, null));
//                // 焼成終了時刻
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S006, "syosei_shuryoujikoku", null, null));
//                // 焼成温度
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_ondo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
//                inputCheckList1.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_ondo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
//
//                String inputCheck1ErrorMessage = validateUtil.executeValidation(inputCheckList1, itemList);
//                if (!StringUtil.isEmpty(inputCheck1ErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(inputCheck1ErrorMessage);
//                    return processData;
//                }
//
//                // トンネル炉・バッチ炉号機(レコード存在チェック)
//                String goukiInput = this.getItemData(itemList, "syosei_tunnel_batchFurnaceGouki");
//                String goukiItemName = this.getItemName(itemList, "syosei_tunnel_batchFurnaceGouki");
//                boolean goukimasExist = validateUtil.checkT002T003(queryRunnerWip, "goukimas",
//                        new ArrayList<>(Arrays.asList("goukicode")), new ArrayList<>(Arrays.asList(goukiInput)));
//                if (!goukimasExist) {
//                    processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", goukiItemName));
//                    return processData;
//                }
//
//                List<ValidateUtil.ValidateInfo> inputCheckList2 = new ArrayList<>();
//
//                // 焼成セッター枚数
//                inputCheckList2.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_setterMaisuu", BigDecimal.valueOf(999), BigDecimal.ZERO));
//                inputCheckList2.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_setterMaisuu", BigDecimal.valueOf(999), BigDecimal.ZERO));
//
//                String inputCheck2ErrorMessage = validateUtil.executeValidation(inputCheckList2, itemList);
//                if (!StringUtil.isEmpty(inputCheck2ErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(inputCheck2ErrorMessage);
//                    return processData;
//                }
//
//                // 焼成開始担当者
//                String startTantouInput = this.getItemData(itemList, "syosei_startTantousha");
//                String startTantouItemName = this.getItemName(itemList, "syosei_startTantousha");
//                boolean startTantouExist = validateUtil.checkT002T003(queryRunnerWip, "tantomas",
//                        new ArrayList<>(Arrays.asList("tantousyacode", "zaiseki")),
//                        new ArrayList<>(Arrays.asList(startTantouInput, "1")));
//                if (!startTantouExist) {
//                    processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", startTantouItemName));
//                    return processData;
//                }
//                // 焼成終了担当者
//                String endTantouInput = this.getItemData(itemList, "syosei_EndTantousha");
//                String endTantouItemName = this.getItemName(itemList, "syosei_EndTantousha");
//                boolean endTantouExist = validateUtil.checkT002T003(queryRunnerWip, "tantomas",
//                        new ArrayList<>(Arrays.asList("tantousyacode", "zaiseki")),
//                        new ArrayList<>(Arrays.asList(endTantouInput, "1")));
//                if (!endTantouExist) {
//                    processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", endTantouItemName));
//                    return processData;
//                }
//
//                // 【その他】入力項目チェック
//                List<ValidateUtil.ValidateInfo> inputCheckList3 = new ArrayList<>();
//                // バッチ炉プログラムNo
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_BProgramNo"))) {
//                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_BProgramNo", BigDecimal.valueOf(99), BigDecimal.ZERO));
//                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_BProgramNo", BigDecimal.valueOf(99), BigDecimal.ZERO));
//                }
//                // 入炉台板枚数
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_nyuroDaibanMaiSuu_seisuu"))) {
//                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_nyuroDaibanMaiSuu_seisuu", BigDecimal.valueOf(99.99), BigDecimal.ZERO));
//                    inputCheckList3.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_nyuroDaibanMaiSuu_seisuu", BigDecimal.valueOf(99.99), BigDecimal.ZERO));
//                }
//
//                String inputCheck3ErrorMessage = validateUtil.executeValidation(inputCheckList3, itemList);
//                if (!StringUtil.isEmpty(inputCheck3ErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(inputCheck3ErrorMessage);
//                    return processData;
//                }
//
//                // 再酸化号機(レコード存在チェック)
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaGouki"))) {
//                    String sankaGoukiInput = this.getItemData(itemList, "syosei_saisankaGouki");
//                    String sankaGoukiItemName = this.getItemName(itemList, "syosei_saisankaGouki");
//                    boolean sankaGoukimasExist = validateUtil.checkT002T003(queryRunnerWip, "goukimas",
//                            new ArrayList<>(Arrays.asList("goukicode")), new ArrayList<>(Arrays.asList(sankaGoukiInput)));
//                    if (!sankaGoukimasExist) {
//                        processData.setErrorMessage(MessageUtil.getMessage("XHC-000008", sankaGoukiItemName));
//                        return processData;
//                    }
//                }
//
//                List<ValidateUtil.ValidateInfo> inputCheckList4 = new ArrayList<>();
//                // 再酸化温度
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaOndo"))) {
//                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_saisankaOndo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
//                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_saisankaOndo", BigDecimal.valueOf(9999), BigDecimal.ZERO));
//                }
//                // 再酸化終了日
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaShuryoubi"))) {
//                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S005, "syosei_saisankaShuryoubi", null, null));
//                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_saisankaShuryoujikoku", null, null));
//                }
//                // 再酸化終了時刻
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaShuryoujikoku"))) {
//                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S006, "syosei_saisankaShuryoujikoku", null, null));
//                    inputCheckList4.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_saisankaShuryoubi", null, null));
//                }
//
//                String inputCheck4ErrorMessage = validateUtil.executeValidation(inputCheckList4, itemList);
//                if (!StringUtil.isEmpty(inputCheck4ErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(inputCheck4ErrorMessage);
//                    return processData;
//                }
//
//                // 時刻前後チェック(再酸化終了日/再酸化終了時刻)
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_saisankaShuryoubi"))) {
//                    String errorMessageDate1
//                            = validateUtil.checkR001(itemList, "syosei_shuryoubi", "syosei_shuryoujikoku", "syosei_saisankaShuryoubi", "syosei_saisankaShuryoujikoku");
//                    if (!StringUtil.isEmpty(errorMessageDate1)) {
//                        processData.setErrorMessage(errorMessageDate1);
//                        return processData;
//                    }
//                }
//
//                List<ValidateUtil.ValidateInfo> inputCheckList5 = new ArrayList<>();
//                // 回数
//                if ("saveData".equals(processData.getProcessName())) {
//                    String lotNo = this.getItemData(itemList, "syosei_LotNo");
//                    int registJissekiNo = this.getSyoseiNextKaisu(queryRunnerQcdb, lotNo);
//                    this.setItemData(processData, "syosei_kaisuu", String.valueOf(registJissekiNo));
//                }
//                if (!StringUtil.isEmpty(this.getItemData(itemList, "syosei_kaisuu"))) {
//                    inputCheckList5.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S003, "syosei_kaisuu", BigDecimal.valueOf(9999), BigDecimal.ZERO));
//                    inputCheckList5.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S004, "syosei_kaisuu", BigDecimal.valueOf(9999), BigDecimal.ZERO));
//                }
//
//                String inputCheck5ErrorMessage = validateUtil.executeValidation(inputCheckList5, itemList);
//                if (!StringUtil.isEmpty(inputCheck5ErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(inputCheck5ErrorMessage);
//                    return processData;
//                }
//
//                // 時間前後チェック
//                String errorMessageDate2
//                        = validateUtil.checkR001(itemList, "syosei_kaisibi", "syosei_kaisijikoku", "syosei_shuryoubi", "syosei_shuryoujikoku");
//                if (!StringUtil.isEmpty(errorMessageDate2)) {
//                    processData.setErrorMessage(errorMessageDate2);
//                    return processData;
//                }
//            }
//
//            if ("deleteData".equals(processData.getProcessName())) {
//                // 【削除】
//                List<ValidateUtil.ValidateInfo> deleteCheckList = new ArrayList<>();
//                // ロットNo
//                deleteCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S001, "syosei_LotNo", null, null));
//                // 回数
//                deleteCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.S002, "syosei_kaisuu", null, null));
//                // ロットNo(チェックデジットチェック)
//                deleteCheckList.add(validateUtil.new ValidateInfo(ValidateUtil.EnumCheckNo.E001, "syosei_LotNo", null, null));
//
//                String checkErrorMessage = validateUtil.executeValidation(deleteCheckList, itemList);
//                if (!StringUtil.isEmpty(checkErrorMessage)) {
//                    // チェックエラーが存在する場合処理終了
//                    processData.setErrorMessage(checkErrorMessage);
//                    return processData;
//                }
//            }
//
//            // チェック処理でエラーが存在しない場合、確認メッセージ及び後続処理を登録して処理続行
//            if ("saveData".equals(processData.getProcessName())) {
//                if (1 < this.getSyoseiNextKaisu(queryRunnerQcdb, this.getItemData(itemList, "syosei_LotNo"))) {
//                    // 取得した実績Noが1以上の場合、警告メッセージを表示する
//                    processData.setWarnMessage("登録済みのデータがあります。<br/>登録してもよろしいですか？");
//                } else {
//                    processData.setWarnMessage("データベースに登録します。よろしいですか？");
//                }
//            } else if ("modifyData".equals(processData.getProcessName())) {
//                processData.setUserAuthParam("syosei_update_button");
//                processData.setRquireAuth(true);
//                processData.setWarnMessage("修正します。よろしいですか？");
//            } else if ("deleteData".equals(processData.getProcessName())) {
//                // 削除完了メッセージ表示
//                processData.setUserAuthParam("syosei_delete_button");
//                processData.setRquireAuth(true);
//                processData.setWarnMessage("データを削除します。よろしいですか？");
//            }
//            processData.setMethod("registData");
//            return processData;
//
//        } catch (SQLException ex) {
//            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//            processData.setErrorMessage("実行時エラー");
//            return processData;
//        }
        return processData;
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
            if (null != processData.getProcessName()) {
                switch (processData.getProcessName()) {
                    case "resist":
                    case "tempResist":
                        // 登録処理
                        effectiveCount = this.insert(queryRunnerQcdb, processData);
                        // 登録完了メッセージ表示
                        processData.setWarnMessage("登録しました。<br/><br/>画面をリセットします。よろしいですか？");
                        // ボタンの活性・非活性を設定
                        processData = this.setButtonEnable(processData, "insert");
                        break;
                    case "correct":
                        // 更新処理
                        effectiveCount = this.update(queryRunnerQcdb, processData);
                        // 登録完了メッセージ表示
                        processData.setWarnMessage("更新しました。<br/><br/>画面をリセットします。よろしいですか？");
                        // ボタンの活性・非活性を設定
                        processData = this.setButtonEnable(processData, "update");
                        break;
                    case "deleteData":
                        // 削除実行
                        effectiveCount = this.delete(queryRunnerQcdb, processData);
                        // 削除完了メッセージ表示
                        processData.setWarnMessage("削除しました。<br/><br/>画面をリセットします。よろしいですか？");
                        // ボタンの活性・非活性を設定
                        processData = this.setButtonEnable(processData, "delete");
                        break;
                    default:
                        break;
                }
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
        FXHDD01 fxhdd01
                = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList()).get(0);
        fxhdd01.setValue(value);
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
                case "resist":
                case "tempResist":
                    processData.setErrorMessage("登録に失敗しました。");
                    break;
                case "correct":
                    processData.setErrorMessage("修正に失敗しました。");
                    break;
                case "delete":
                    processData.setErrorMessage("削除に失敗しました。");
                    break;
                default:
                    break;
            }
        }
        return processData;
    }
}
