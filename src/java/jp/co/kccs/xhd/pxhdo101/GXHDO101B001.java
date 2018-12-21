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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrSpsprint;
import jp.co.kccs.xhd.db.model.SrSyosei;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.util.SubFormUtil;

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
            GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
            beanGXHDO101C001.setGxhdO101c001Model(GXHDO101C001Logic.createGXHDO101C001Model(""));

            // PTN距離Xサブ画面初期表示データ設定
            GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
            beanGXHDO101C002.setGxhdO101c002Model(GXHDO101C002Logic.createGXHDO101C002Model(""));

            // PTN距離Yサブ画面初期表示データ設定
            GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
            beanGXHDO101C003.setGxhdO101c003Model(GXHDO101C003Logic.createGXHDO101C003Model(""));

            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN,
                    GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                    GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                    GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                    GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                    GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN));
            //******************************************************************************************

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, "initial");

            // エラーが発生していない場合
            if (processData.getErrorMessageInfoList().isEmpty()) {
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                } else {
                    // 後続処理なし
                    processData.setMethod("");
                }
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

            return processData;
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
//        try {
//            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
//            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
//
//            processData.setProcessName("wipImport");
//
//            // 入力チェック
//            FXHDD01 itemRow = this.getItemRow(processData.getItemList(), "syosei_LotNo");
//            String lotNo = itemRow.getValue();
//
//            // ロットNo桁数チェック
//            if (LOTNO_BYTE != StringUtil.getByte(lotNo, CHARSET, LOGGER)) {
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000003", itemRow.getLabel1(), LOTNO_BYTE));
//                return processData;
//            }
//
//            // 仕掛情報データの取得
//            Map sikakariData = this.loadShikakariData(queryRunnerWip, lotNo);
//            if (null == sikakariData || sikakariData.isEmpty()) {
//                // 仕掛情報データが取得出来なかった場合エラー終了
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
//                return processData;
//            }
//
//            // 実績情報の取得
//            Map jissekiData3 = this.loadJissekiData(queryRunnerWip, lotNo, "'DCK500', 'DCK600'");
//            if (null == jissekiData3 || jissekiData3.isEmpty()) {
//                // 実績情報データが取得出来なかった場合エラー終了
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
//                return processData;
//            }
//
//            // 生産情報の取得
//            Map seisanData4 = this.loadSeisanData(queryRunnerWip, jissekiData3.get("jissekino").toString());
//            if (null == seisanData4 || seisanData4.isEmpty()) {
//                // 生産情報データが取得出来なかった場合エラー終了
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
//                return processData;
//            }
//
//            // 設計情報の取得
//            Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
//
//            // 実績情報の取得
//            Map jissekiData6 = this.loadJissekiData(queryRunnerWip, lotNo, "'DDK100'");
//            if (null == jissekiData6 || jissekiData6.isEmpty()) {
//                // 実績情報データが取得出来なかった場合エラー終了
//                processData.setErrorMessage(MessageUtil.getMessage("XHC-000009"));
//                return processData;
//            }
//
//            // 実績情報の取得
//            Map jissekiData7 = this.loadJissekiData(queryRunnerWip, lotNo, "'DEK100', 'DEK200', 'DEK300'");
//
//            // 生産情報の取得
//            Map seisanData8 = null;
//            if (null != jissekiData7 && !jissekiData7.isEmpty()) {
//                seisanData8 = this.loadSeisanData(queryRunnerWip, jissekiData7.get("jissekino").toString());
//            }
//
//            // 取得データを画面に反映する
//            processData.setProcessName("wipImport");
//            processData.setMethod("");
//            // ボタンの活性・非活性を設定
//            processData = this.setButtonEnable(processData, "wipImport");
//
//            this.setItemData(processData, "syosei_KCPNO", StringUtil.nullToBlank(sikakariData.get("kcpno")));
//            this.setItemData(processData, "syosei_kosuu", StringUtil.nullToBlank(jissekiData3.get("syorisuu")));
//            this.setItemData(processData, "syosei_genryo", StringUtil.nullToBlank(sikakariData.get("genryo")));
//            this.setItemData(processData, "syosei_kaisibi", DateUtil.getDisplayDate(jissekiData3.get("syoribi"), DateUtil.YYMMDD));
//            this.setItemData(processData, "syosei_kaisijikoku", DateUtil.getDisplayTime(jissekiData3.get("syorijikoku"), DateUtil.HHMM));
//            this.setItemData(processData, "syosei_shuryoubi", DateUtil.getDisplayDate(jissekiData6.get("syoribi"), DateUtil.YYMMDD));
//            this.setItemData(processData, "syosei_shuryoujikoku", DateUtil.getDisplayTime(jissekiData6.get("syorijikoku"), DateUtil.HHMM));
//            if (null != sekkeiData && !sekkeiData.isEmpty()) {
//                this.setItemData(processData, "syosei_ondo", StringUtil.nullToBlank(NumberUtil.convertIntData(sekkeiData.get("SYOONDO"))));
//            } else {
//                this.setItemData(processData, "syosei_ondo", "");
//            }
//            this.setItemData(processData, "syosei_tunnel_batchFurnaceGouki", StringUtil.nullToBlank(seisanData4.get("goukicode")));
//            this.setItemData(processData, "syosei_startTantousha", StringUtil.nullToBlank(jissekiData3.get("tantousyacode")));
//            this.setItemData(processData, "syosei_EndTantousha", StringUtil.nullToBlank(jissekiData6.get("tantousyacode")));
//            if (null != seisanData8 && !seisanData8.isEmpty()) {
//                this.setItemData(processData, "syosei_saisankaGouki", StringUtil.nullToBlank(seisanData8.get("goukicode")));
//            } else {
//                this.setItemData(processData, "syosei_saisankaGouki", "");
//            }
//            if (null != jissekiData7 && !jissekiData7.isEmpty()) {
//                this.setItemData(processData, "syosei_saisankaShuryoubi", DateUtil.getDisplayDate(jissekiData7.get("syoribi"), DateUtil.YYMMDD));
//                this.setItemData(processData, "syosei_saisankaShuryoujikoku", DateUtil.getDisplayTime(jissekiData7.get("syorijikoku"), DateUtil.HHMM));
//            } else {
//                this.setItemData(processData, "syosei_saisankaShuryoubi", "");
//                this.setItemData(processData, "syosei_saisankaShuryoujikoku", "");
//            }
//
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
     * 膜厚(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openMakuatsu(ProcessData processData) {

        try {

            processData.setProcessName("openMakuatsu");
            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c001");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
            beanGXHDO101C001.setGxhdO101c001ModelView(beanGXHDO101C001.getGxhdO101c001Model().clone());
            
        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;

        }

        return processData;
    }

    /**
     * PTN距離X(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriX(ProcessData processData) {
        try {
            processData.setProcessName("openPtnKyoriX");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c002");
            processData.setMethod("");

            // PTN距離Xの現在の値をサブ画面の表示用の値に設定
            GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
            beanGXHDO101C002.setGxhdO101c002ModelView(beanGXHDO101C002.getGxhdO101c002Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;

        }

    }

    /**
     * PTN距離Y(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriY(ProcessData processData) {
        try {

            processData.setProcessName("openPtnKyoriY");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c003");
            processData.setMethod("");

            // PTN距離Yの現在の値をサブ画面表示用に設定
            GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
            beanGXHDO101C003.setGxhdO101c003ModelView(beanGXHDO101C003.getGxhdO101c003Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;

        }
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {
//        try {
            // 処理名を登録
            processData.setProcessName("tempResist");
            // 後続処理メソッド設定
            processData.setMethod("doTempResist");

            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String lotNo = (String) session.getAttribute("lotNo");

            // 規格チェック
            List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
            ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

            // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
            if (errorMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
                return processData;
            }
            
           // processData.setWarnMessage("TEST");

            // 規格値エラーがある場合は規格値エラーをセット
            if (!kikakuchiInputErrorInfoList.isEmpty()) {
                processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
            }

//            List<SrSpsprint> srSpsPrintData = this.loadSrSpsprintData(queryRunnerQcdb, lotNo, true);
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
            //processData.getDataSourceQcdb().getConnection().commit();
//        } catch (SQLException ex) {
//            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//            processData = createRegistDataErrorMessage(processData);
//        }
//
//        if (!processData.getErrorMessageInfoList().isEmpty()) {
//            try {
//                processData.getDataSourceQcdb().getConnection().rollback();
//            } catch (SQLException ex) {
//                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//                processData = createRegistDataErrorMessage(processData);
//            }
//            try {
//                processData.getDataSourceQcdb().getConnection().setAutoCommit(true);
//            } catch (SQLException ex) {
//                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//                processData = createRegistDataErrorMessage(processData);
//            }
//        }

//        finally {
//            try {
//                
//                processData.getDataSourceQcdb().getConnection().setAutoCommit(true);
//            } catch (SQLException ex) {
//                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
//                processData = createRegistDataErrorMessage(processData);
//                
//            }
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

        //TODO 登録ボタンでサブ画面呼び出しのエラーが発生した想定
        List<String> subInitDispMsg = Arrays.asList(MessageUtil.getMessage("XHC-000009"), MessageUtil.getMessage("XHC-000009"));
        processData.setSubInitDispMsgList(subInitDispMsg);
        processData.setMethod("openMakuatsu");

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
                method = "openInitMessage";
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

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");

        // エラーメッセージリスト
        List<String> errorMessageList = new ArrayList<>();

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
        if (sekkeiData == null || sekkeiData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000014", ""));
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 設計情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, getMapSekkeiAssociation()));

        // ロット区分マスタ情報の取得
        String lotKubunCode = StringUtil.nullToBlank(sekkeiData.get("KUBUN1")); // ﾛｯﾄ区分ｺｰﾄﾞ
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotKubunCode);
        String lotKubun = "";
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015", ""));
        } else {
            lotKubun = StringUtil.nullToBlank(lotKbnMasData.get("lotkubun"));
        }

        // オーナーマスタ情報の取得
        String ownerCode = StringUtil.nullToBlank(sekkeiData.get("OWNER")); // ｵｰﾅｰｺｰﾄﾞ
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownerCode);
        String owner = "";
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016", ""));
        } else {
            owner = StringUtil.nullToBlank(ownerMasData.get("owner"));
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
        this.setItemData(processData, GXHDO101B001Const.LOT_KUBUN, lotKubunCode + ":" + lotKubun);
        // オーナー
        this.setItemData(processData, GXHDO101B001Const.OWNER, ownerCode + ":" + owner);
        // 電極テープ
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));
        // 積層数
        this.setItemData(processData, GXHDO101B001Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
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
        this.setItemData(processData, GXHDO101B001Const.UE_COVER_TAPE1, StringUtil.nullToBlank(sekkeiData.get("TBUNRUI2"))
                + "-"
                + StringUtil.nullToBlank(sekkeiData.get("SYURUI2"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI2"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU2"))
                + "枚"
        );

        //TODO
        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B001Const.SHITA_COVER_TAPE1, StringUtil.nullToBlank(sekkeiData.get("TBUNRUI4"))
                + "-"
                + StringUtil.nullToBlank(sekkeiData.get("SYURUI4"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI4"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU4"))
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
        // 電極製版名
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_SEIHAN_MEI, "");
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

        //TODO
        //processData.setErrorMessageInfoList(errorMessageInfoList);
//        Map map03RevInfo = loadFxhdd03RevInfo(queryRunnerWip, lotNo);
//        
//        if (map03RevInfo != null && !map03RevInfo.isEmpty()) {
//            
//        }
        
        processData.setInitMessageList(errorMessageList);
        return processData;

    }
    
    /**
     * 初期表示データ設定
     *
     * @param processData
     * @return 処理制御データ
     * @throws SQLException
     */
    private ProcessData setInitInputDate(ProcessData processData) throws SQLException {
        //TODO
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        
//        Map map03RevInfo = loadFxhdd03RevInfo(queryRunnerWip, lotNo);
//        if (map03RevInfo != null && !map03RevInfo.isEmpty()) {
//            
//        }
        
        
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
        String sql = "SELECT HINMEI,SEKKEINO,PROCESS,SETSUU,TOKUISAKI,KUBUN1,OWNER,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,TBUNRUI2,SYURUI2,ATUMI2,"
                + "MAISUU2,TBUNRUI4,SYURUI4,ATUMI4,MAISUU4,PATTERN "
                + "FROM da_sekkei "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = '001'";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 設計データ関連付けマップ取得
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapSekkeiAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("HINMEI", "KCPNO");
                put("SETSUU", "セット数");
                put("TOKUISAKI", "客先");
                put("GENRYOU", "電極テープ");
                put("ETAPE", "電極テープ");
                put("EATUMI", "積層数");
                put("SOUSUU", "積層数");
                put("EMAISUU", "積層数");
                put("KUBUN1", "ロット区分");
                put("OWNER", "オーナー");
                put("TBUNRUI2", "上カバーテープ１");
                put("SYURUI2", "上カバーテープ１");
                put("ATUMI2", "上カバーテープ１");
                put("MAISUU2", "上カバーテープ１");
                put("TBUNRUI4", "下カバーテープ１");
                put("SYURUI4", "下カバーテープ１");
                put("ATUMI4", "下カバーテープ１");
                put("MAISUU4", "下カバーテープ１");
                put("PATTERN", "電極製版名");
            }
        };

        return map;
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
     * (5)[SPS印刷]から、情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private List<SrSpsprint> loadSrSpsprintData(QueryRunner queryRunnerQcdb, String lotNo, boolean isRecordLock) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);
        // 設計データの取得
        String sql = "kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,"
                + "genryoukigou,pastelotno,pastenendo,pasteondo,seihanno,"
                + "seihanmaisuu,startdatetime,enddatetime,skeegeno,"
                + "skeegemaisuu,gouki,tantousya,kakuninsya,kansouondo,"
                + "prnprofile,kansoutime,saatu,skeegespeed,skeegeangle,"
                + "mld,clearrance,bikou1,bikou2,makuatu1,makuatu2,makuatu3,"
                + "makuatu4,makuatu5,pastelotno2,pastelotno3,pastelotno4,"
                + "pastelotno5,pastenendo2,pastenendo3,pastenendo4,pastenendo5,"
                + "pasteondo2,pasteondo3,pasteondo4,pasteondo5,bikou3,bikou4,"
                + "bikou5,kansouondo2,kansouondo3,kansouondo4,kansouondo5,"
                + "skeegemaisuu2,taperollno1,taperollno2,taperollno3,taperollno4,"
                + "taperollno5,pastehinmei,seihanmei,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,start_ptn_dist_x,start_ptn_dist_y,tanto_setting,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,"
                + "sijiondo4,sijiondo5,TensionS,TensionE,TensionStemae,TensionEtemae,"
                + "TensionSoku,TensionEoku,AtuDoATu,BladeATu,AtuDoKeiLEnd,"
                + "AtuDoKeiLSide,AtuDoKeiCenter,AtuDoKeiRSide,AtuDoKeiREnd"
                + "FROM sr_spsprint "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";

        if (isRecordLock) {
            sql += " FOR UPDATE NOWAIT";
        }

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("tapesyurui", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("TapeSlipKigo", "tapeSlipKigo"); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("pastelotno", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo1
        mapping.put("pastenendo", "pastenendo"); //ﾍﾟｰｽﾄ粘度1
        mapping.put("pasteondo", "pasteondo"); //ﾍﾟｰｽﾄ温度1
        mapping.put("seihanno", "seihanno"); //製版No
        mapping.put("seihanmaisuu", "seihanmaisuu"); //製版枚数
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("enddatetime", "enddatetime"); //ﾌﾟﾘﾝﾄ終了日時
        mapping.put("skeegeno", "skeegeno"); //ｽｷｰｼﾞNo
        mapping.put("skeegemaisuu", "skeegemaisuu"); //ｽｷｰｼﾞ枚数
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("tantousya", "tantousya"); //担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); //確認者ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("prnprofile", "prnprofile"); //印刷ﾌﾟﾛﾌｧｲﾙ
        mapping.put("kansoutime", "kansoutime"); //乾燥時間
        mapping.put("saatu", "saatu"); //差圧
        mapping.put("skeegespeed", "skeegespeed"); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("skeegeangle", "skeegeangle"); //ｽｷｰｼﾞ角度
        mapping.put("mld", "mld"); //ﾒﾀﾙﾚｲﾀﾞｳﾝ値
        mapping.put("clearrance", "clearrance"); //ｸﾘｱﾗﾝｽ設定値
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("makuatu1", "makuatu1"); //膜厚1
        mapping.put("makuatu2", "makuatu2"); //膜厚2
        mapping.put("makuatu3", "makuatu3"); //膜厚3
        mapping.put("makuatu4", "makuatu4"); //膜厚4
        mapping.put("makuatu5", "makuatu5"); //膜厚5
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastelotno3", "pastelotno3"); //ﾍﾟｰｽﾄﾛｯﾄNo3
        mapping.put("pastelotno4", "pastelotno4"); //ﾍﾟｰｽﾄﾛｯﾄNo4
        mapping.put("pastelotno5", "pastelotno5"); //ﾍﾟｰｽﾄﾛｯﾄNo5
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pastenendo3", "pastenendo3"); //ﾍﾟｰｽﾄ粘度3
        mapping.put("pastenendo4", "pastenendo4"); //ﾍﾟｰｽﾄ粘度4
        mapping.put("pastenendo5", "pastenendo5"); //ﾍﾟｰｽﾄ粘度5
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pasteondo3", "pasteondo3"); //ﾍﾟｰｽﾄ温度3
        mapping.put("pasteondo4", "pasteondo4"); //ﾍﾟｰｽﾄ温度4
        mapping.put("pasteondo5", "pasteondo5"); //ﾍﾟｰｽﾄ温度5
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("skeegemaisuu2", "skeegemaisuu2"); //ｽｷｰｼﾞ枚数2
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("taperollno4", "taperollno4"); //ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("taperollno5", "taperollno5"); //ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("pastehinmei", "pastehinmei"); //ﾍﾟｰｽﾄ品名
        mapping.put("seihanmei", "seihanmei"); //製版名
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("tanto_setting", "tantoSetting"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("makuatuave_end", "makuatuaveEnd"); //終了時膜厚AVE
        mapping.put("makuatumax_end", "makuatumaxEnd"); //終了時膜厚MAX
        mapping.put("makuatumin_end", "makuatuminEnd"); //終了時膜厚MIN
        mapping.put("end_ptn_dist_x", "endPtnDistX"); //終了時PTN間距離X
        mapping.put("end_ptn_dist_y", "endPtnDistY"); //終了時PTN間距離Y
        mapping.put("tanto_end", "tantoEnd"); //終了時担当者ｺｰﾄﾞ
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("sijiondo", "sijiondo"); //指示乾燥温度1
        mapping.put("sijiondo2", "sijiondo2"); //指示乾燥温度2
        mapping.put("sijiondo3", "sijiondo3"); //指示乾燥温度3
        mapping.put("sijiondo4", "sijiondo4"); //指示乾燥温度4
        mapping.put("sijiondo5", "sijiondo5"); //指示乾燥温度5
        mapping.put("TensionS", "tensionS"); //ﾃﾝｼｮﾝ開始
        mapping.put("TensionE", "tensionE"); //ﾃﾝｼｮﾝ終了
        mapping.put("TensionStemae", "tensionStemae"); //ﾃﾝｼｮﾝ開始手前
        mapping.put("TensionEtemae", "tensionEtemae"); //ﾃﾝｼｮﾝ終了手前
        mapping.put("TensionSoku", "tensionSoku"); //ﾃﾝｼｮﾝ開始奥
        mapping.put("TensionEoku", "tensionEoku"); //ﾃﾝｼｮﾝ終了奥
        mapping.put("AtuDoATu", "atuDoATu"); //圧胴圧力
        mapping.put("BladeATu", "bladeATu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("AtuDoKeiLEnd", "atuDoKeiLEnd"); //圧胴径左端
        mapping.put("AtuDoKeiLSide", "atuDoKeiLSide"); //圧胴径左側
        mapping.put("AtuDoKeiCenter", "atuDoKeiCenter"); //圧胴径中央
        mapping.put("AtuDoKeiRSide", "atuDoKeiRSide"); //圧胴径右側
        mapping.put("AtuDoKeiREnd", "atuDoKeiREnd"); //圧胴径右端

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprint>> beanHandler = new BeanListHandler<>(SrSpsprint.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }
    
    
    /**
     * (10)[品質DB登録実績]から、リビジョン,状態フラグを取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerWip, String lotNo, String formId) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);
        params.add(formId);

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
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("登録に失敗しました。")));
                    break;
                case "tempResist":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("仮登録に失敗しました。")));
                    break;
                case "correct":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("修正に失敗しました。")));

                    break;
                case "delete":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("削除に失敗しました。")));

                    break;
                default:
                    break;
            }
        }
        return processData;
    }

//    /**
//     * サブ画面のBean情報を取得
//     *
//     * @param formId フォームID
//     * @return サブ画面情報
//     */
//    public Object getSubFormBean(String formId) {
//
//        Object returnBean = null;
//
//        switch (formId) {
//            // 膜厚(SPS)
//            case "GXHDO101C001":
//                returnBean = FacesContext.getCurrentInstance().
//                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
//                                getELContext(), null, "beanGXHDO101C001");
//                break;
//
//            // PTN距離X
//            case "GXHDO101C002":
//                returnBean = FacesContext.getCurrentInstance().
//                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
//                                getELContext(), null, "beanGXHDO101C002");
//                break;
//
//            // PTN距離Y
//            case "GXHDO101C003":
//                returnBean = FacesContext.getCurrentInstance().
//                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
//                                getELContext(), null, "beanGXHDO101C003");
//                break;
//            // 初期表示メッセージ
//            case "InitMessage":
//                returnBean = FacesContext.getCurrentInstance().
//                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
//                                getELContext(), null, "beanInitMessage");
//                break;
//            // 規格エラーダイアログ
//            case "KikakuError":
//                returnBean = FacesContext.getCurrentInstance().
//                        getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
//                                getELContext(), null, "beanKikakuError");
//                break;
//
//            default:
//                break;
//        }
//        return returnBean;
//    }

    /**
     * 初期表示メッセージ表示
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openInitMessage(ProcessData processData) {

        processData.setMethod("");

        // メッセージを画面に渡す
        InitMessage beanInitMessage = (InitMessage) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_INIT_MESSAGE);
        beanInitMessage.setInitMessageList(processData.getInitMessageList());

        // 実行スクリプトを設定
        processData.setExecuteScript("PF('W_dlg_initMessage').show();");
        return processData;
    }
    
    
    
    
    private String getMethodFromProcess(String processName){
        switch(processName){
            case "tempResist":
                return "doTempResist";
                default:
               break;
        }
        return "";
    }


    private Object getSrSpsprintItemData(SrSpsprint srSpsPritData, String itemId) {
        switch (itemId) {
            case GXHDO101B001Const.KCPNO: // KCPNO
                return srSpsPritData.getKcpno();
            case GXHDO101B001Const.LOTNO: // ロットＮｏ．
                return srSpsPritData.getLotno();
            case GXHDO101B001Const.SET_SUU: // セット数
                return null;//TODO
            case GXHDO101B001Const.KYAKUSAKI: // 客先
                return null;//TODO
            case GXHDO101B001Const.LOT_KUBUN: // ロット区分
                return null;//TODO
            case GXHDO101B001Const.OWNER: // オーナー
                return null;//TODO
            case GXHDO101B001Const.DENKYOKU_TAPE: // 電極テープ
                return null;//TODO
            case GXHDO101B001Const.SEKISOU_SU: // 積層数
                return null;//TODO
            case GXHDO101B001Const.SLIP_LOTNO: // ｽﾘｯﾌﾟﾛｯﾄNo
                return srSpsPritData.getTapelotno();//TODO
            case GXHDO101B001Const.ROLL_NO1: // ﾛｰﾙNo1
                return srSpsPritData.getTaperollno1();//TODO
            case GXHDO101B001Const.ROLL_NO2: // ﾛｰﾙNo2
                return srSpsPritData.getTaperollno2();//TODO
            case GXHDO101B001Const.ROLL_NO3: // ﾛｰﾙNo3
                return srSpsPritData.getTaperollno3(); //TODO
            case GXHDO101B001Const.GENRYO_KIGOU: // 原料記号
                return srSpsPritData.getGenryoukigou();
            case GXHDO101B001Const.UE_COVER_TAPE1: // 上カバーテープ１
                return null;//TODO
            case GXHDO101B001Const.SHITA_COVER_TAPE1: // 下カバーテープ１
                return null;//TODO
            case GXHDO101B001Const.COVER_TAPE: // カバーテープ
                return null;//TODO
            case GXHDO101B001Const.RETSU_GYOU: // 列×行
                return null;//TODO
            case GXHDO101B001Const.PITCH: // ピッチ
                return null;//TODO
            case GXHDO101B001Const.DENKYOKU_PASTE: // 電極ペースト
                return null;//TODO
            case GXHDO101B001Const.PASTE_HINMEI: // ﾍﾟｰｽﾄ品名
                return srSpsPritData.getPastehinmei();
            case GXHDO101B001Const.PASTE_LOT_NO1: // ﾍﾟｰｽﾄﾛｯﾄNo1
                return srSpsPritData.getPastelotno();
            case GXHDO101B001Const.PASTE_NENDO1: // ﾍﾟｰｽﾄ粘度1
                return srSpsPritData.getPastenendo();
            case GXHDO101B001Const.PASTE_ONDO1: // ﾍﾟｰｽﾄ温度1
                return srSpsPritData.getPasteondo();
            case GXHDO101B001Const.PASTE_KOKEIBUN1: // ﾍﾟｰｽﾄ固形分1
                return null;//TODO
            case GXHDO101B001Const.PASTE_LOT_NO2: // ﾍﾟｰｽﾄﾛｯﾄNo2
                return srSpsPritData.getPastelotno2();
            case GXHDO101B001Const.PASTE_NENDO2: // ﾍﾟｰｽﾄ粘度2
                return srSpsPritData.getPastenendo2();
            case GXHDO101B001Const.PASTE_ONDO2: // ﾍﾟｰｽﾄ温度2
                return srSpsPritData.getPasteondo2();
            case GXHDO101B001Const.PASTE_KOKEIBUN2: // ﾍﾟｰｽﾄ固形分2
                return null;//TODO
            case GXHDO101B001Const.DENKYOKU_SEIHAN_MEI: // 電極製版名
                return null;//TODO
            case GXHDO101B001Const.DENKYOKU_SEIHAN_SHIYOU: // 電極製版仕様
                return null;//TODO
            case GXHDO101B001Const.PET_FILM_SHURUI: // ＰＥＴフィルム種類
                return null;//TODO
            case GXHDO101B001Const.SEKISOU_SLIDE_RYOU: // 積層スライド量
                return null;//TODO
            case GXHDO101B001Const.INSATSU_GOUKI: // 印刷号機
                return srSpsPritData.getGouki();//TODO
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1: // 乾燥温度表示値1
                return srSpsPritData.getKansouondo();//TODO
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2: // 乾燥温度表示値2
                return srSpsPritData.getKansouondo2();//TODO
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3: // 乾燥温度表示値3
                return srSpsPritData.getKansouondo3();//TODO
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4: // 乾燥温度表示値4
                return srSpsPritData.getKansouondo4();//TODO
            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI5: // 乾燥温度表示値5
                return srSpsPritData.getKansouondo5();//TODO
            case GXHDO101B001Const.HANSOU_SOKUDO: // 搬送速度
                return null;//TODO
            case GXHDO101B001Const.KAISHI_TENSION_KEI: // 開始テンション計
                return srSpsPritData.getTensionS();
            case GXHDO101B001Const.KAISHI_TENSION_MAE: // 開始テンション前
                return srSpsPritData.getTensionStemae();
            case GXHDO101B001Const.KAISHI_TENSION_OKU: // 開始テンション奥
                return srSpsPritData.getTensionSoku();
            case GXHDO101B001Const.SHURYOU_TENSION_KEI: // 終了テンション計
                return srSpsPritData.getTensionE();
            case GXHDO101B001Const.SHURYOU_TENSION_MAE: // 終了テンション前
                return srSpsPritData.getTensionEtemae();
            case GXHDO101B001Const.SHURYOU_TENSION_OKU: // 終了テンション奥
                return srSpsPritData.getTensionSoku();
            case GXHDO101B001Const.ATSUDOU_ATSURYOKU: // 圧胴圧力
                return srSpsPritData.getAtuDoATu();
            case GXHDO101B001Const.BLADE_ATSURYOKU: // ブレード圧力
                return srSpsPritData.getBladeATu();
            case GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI: // 製版名 / 版胴名
                return srSpsPritData.getSeihanmei();
            case GXHDO101B001Const.SEIHAN_OR_HANDOU_NO: // 製版No / 版胴No
                return srSpsPritData.getSeihanno();
            case GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU: // 製版使用枚数/版胴使用枚数
                return srSpsPritData.getSeihanmaisuu();
            case GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO: // ｽｷｰｼﾞNo / 圧胴No．
                return srSpsPritData.getSkeegeno();
            case GXHDO101B001Const.ATSUDOU_KEI1: // 圧胴径1
                return srSpsPritData.getAtuDoKeiLSide();//TODO
            case GXHDO101B001Const.ATSUDOU_KEI2: // 圧胴径2
                return srSpsPritData.getAtuDoKeiLSide();//TODO
            case GXHDO101B001Const.ATSUDOU_KEI3: // 圧胴径3
                return srSpsPritData.getAtuDoKeiCenter();//TODO
            case GXHDO101B001Const.ATSUDOU_KEI4: // 圧胴径4
                return srSpsPritData.getAtuDoKeiRSide();//TODO
            case GXHDO101B001Const.ATSUDOU_KEI5: // 圧胴径5
                return srSpsPritData.getAtuDoKeiREnd();//TODO
            case GXHDO101B001Const.BLADE_NO: // ブレードNo.
                return null;// TODO
            case GXHDO101B001Const.BLADE_GAIKAN: // ブレード外観
                return null;// TODO
            case GXHDO101B001Const.INSATSU_KAISHI_DAY: // 印刷開始日
                return DateUtil.formattedTimestamp(srSpsPritData.getStartdatetime(), "yyMMdd");
            case GXHDO101B001Const.INSATSU_KAISHI_TIME: // 印刷開始時間
                return DateUtil.formattedTimestamp(srSpsPritData.getStartdatetime(), "HHss");
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE: // 印刷ｽﾀｰﾄ膜厚AVE
                return null;// TODO
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX: // 印刷ｽﾀｰﾄ膜厚MAX
                return null;// TODO
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN: // 印刷ｽﾀｰﾄ膜厚MIN
                return null;// TODO
            case GXHDO101B001Const.INSATSU_START_MAKUATSU_CV: // 印刷ｽﾀｰﾄ膜厚CV
                return null;// TODO
            case GXHDO101B001Const.PTN_INSATSU_START_X_MIN: // PTN間距離印刷ｽﾀｰﾄ X Min
                return srSpsPritData.getStartPtnDistX();//TODO
            case GXHDO101B001Const.PTN_INSATSU_START_Y_MIN: // PTN間距離印刷ｽﾀｰﾄ Y Min
                return srSpsPritData.getStartPtnDistY();//TODO
            case GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK: // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
                return null;//TODO
            case GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA: // 印刷スタート時担当者
                return null;//TODO
            case GXHDO101B001Const.INSATSU_SHUURYOU_DAY: // 印刷終了日
                return DateUtil.formattedTimestamp(srSpsPritData.getEnddatetime(), "yyMMdd");
            case GXHDO101B001Const.INSATSU_SHUURYOU_TIME: // 印刷終了時刻
                return DateUtil.formattedTimestamp(srSpsPritData.getEnddatetime(), "HHmm");
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE: // 印刷ｴﾝﾄﾞ膜厚AVE
                return srSpsPritData.getMakuatuaveEnd();
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX: // 印刷ｴﾝﾄﾞ膜厚MAX
                return srSpsPritData.getMakuatumaxEnd();
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN: // 印刷ｴﾝﾄﾞ膜厚MIN
                return srSpsPritData.getMakuatuminEnd();
            case GXHDO101B001Const.INSATSU_END_MAKUATSU_CV: // 印刷ｴﾝﾄﾞ膜厚CV
                return null;//TODO
            case GXHDO101B001Const.PTN_INSATSU_END_X_MIN: // PTN間距離印刷ｴﾝﾄﾞ X Min
                return srSpsPritData.getEndPtnDistX(); //TODO
            case GXHDO101B001Const.PTN_INSATSU_END_Y_MIN: // PTN間距離印刷ｴﾝﾄﾞ Y Min
                return srSpsPritData.getEndPtnDistY(); //TODO
            case GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK: // 終了時ニジミ・カスレ確認
                return null;//TODO
            case GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA: // 印刷エンド時担当者
                return srSpsPritData.getTantoEnd();// TODO
            case GXHDO101B001Const.INSATSU_MAISUU: // 印刷枚数
                return null; //TODO
            default:
                return null;
        }

    }

    /**
     * 同一項目の更新チェック
     */
    private void checkSameItemUpdate(ProcessData processData, SrSpsprint srSpsPritData) {
        for (FXHDD01 fxhdd01 : processData.getItemList()) {
            //入力項目かつ値が入っている場合、チェック
            if (!isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue())) {
                continue;
            }

            Object checkDbData = getSrSpsprintItemData(srSpsPritData, fxhdd01.getItemId());

            //checkDbData.
        }
    }

    private boolean isInputColumn(FXHDD01 fxhdd01) {
        if (fxhdd01.isRenderInputDate() || fxhdd01.isRenderInputNumber() || fxhdd01.isRenderInputRadio() || fxhdd01.isRenderInputSelect() || fxhdd01.isRenderInputText() || fxhdd01.isRenderInputTime()) {
            return true;
        }
        return false;
    }

    private boolean compItemValue(Object checkDbData, String itemValue) {
        try {

            // 値がNULLまたは空の場合は次の項目へ
            if (checkDbData == null || StringUtil.isEmpty(checkDbData.toString())) {
                return true;
            }

            if (checkDbData instanceof Integer) {
                return (Integer) checkDbData == Integer.parseInt(itemValue);
            } else if (checkDbData instanceof String) {
                return ((String) checkDbData).equals(itemValue);
            } else if (checkDbData instanceof BigDecimal) {
                return ((BigDecimal) checkDbData).compareTo(new BigDecimal(itemValue)) == 0;
            }

        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

}
