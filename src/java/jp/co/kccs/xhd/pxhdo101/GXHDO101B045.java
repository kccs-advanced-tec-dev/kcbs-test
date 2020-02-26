/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrHaps;
import jp.co.kccs.xhd.db.model.Jisseki;
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
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名  品質情報管理システム<br>
 * <br>
 * 変更日      2020/02/11<br>
 * 計画書No    K1811-DS001<br>
 * 変更者      KCSS K.Jo<br>
 * 変更理由    新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B045(印刷積層・HAPS)ロジック
 *
 * @author KCSS K.Jo
 * @since  2020/02/11
 */
public class GXHDO101B045 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";
    
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

            // 初期表示データ設定処理
            processData = setInitData(processData);
            // 中断エラー発生時
            if (processData.isFatalError()) {
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                }
                return processData;
            }

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, processData.getInitJotaiFlg());

            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B045Const.BTN_START_DATETIME_TOP,
                    GXHDO101B045Const.BTN_END_DATETIME_TOP,
                    GXHDO101B045Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B045Const.BTN_END_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B045Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B045Const.BTN_INSERT_TOP,
                    GXHDO101B045Const.BTN_DELETE_TOP,
                    GXHDO101B045Const.BTN_UPDATE_TOP,
                    GXHDO101B045Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B045Const.BTN_INSERT_BOTTOM,
                    GXHDO101B045Const.BTN_DELETE_BOTTOM,
                    GXHDO101B045Const.BTN_UPDATE_BOTTOM));

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
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {
        
        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

        // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        // 規格値エラーがある場合は規格値エラーをセット(警告表示)
        if (!kikakuchiInputErrorInfoList.isEmpty()) {
            processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
        }
        
        // 後続処理メソッド設定
        processData.setMethod("doTempResist");
        
        return processData;

    }

    /**
     * 仮登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTempResist(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
            
            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());
            
            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, paramJissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, paramJissekino);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 印刷積層・HAPS_仮登録登録処理
                insertTmpSrHaps(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            } else {

                // 印刷積層・HAPS_仮登録更新処理
                updateTmpSrHaps(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");
            //完了メッセージ設定
            processData.setInfoMessage("仮登録完了");

            // 背景色をクリア
            for (FXHDD01 fxhdd01 : processData.getItemList()) {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            }

            // 状態ﾌﾗｸﾞ、revisionを設定する。
            processData.setInitJotaiFlg(JOTAI_FLG_KARI_TOROKU);
            processData.setInitRev(newRev.toPlainString());

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
        }

        return processData;
    }

    /**
     * 登録処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataResist(ProcessData processData) {

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

        // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        // 規格値エラーがある場合は規格値エラーをセット(警告表示)
        if (!kikakuchiInputErrorInfoList.isEmpty()) {
            processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
        }
        
        // 後続処理メソッド設定
        processData.setMethod("doResist");

        return processData;
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData) {

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B045Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B045Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B045Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B045Const.SHURYOU_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemShuryouDay.getValue(), itemShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemKaishiDay.getLabel1(), kaishiDate, itemShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaishiDay, itemKaishiTime, itemShuryouDay, itemShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        return null;
    }
    
    /**
     * 登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doResist(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
            
            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());
            
            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = BigDecimal.ZERO;
            BigDecimal newRev = BigDecimal.ONE;

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, paramJissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrHaps tmpSrHaps = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrHaps> srHapsList = getSrHapsData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srHapsList.isEmpty()) {
                    tmpSrHaps = srHapsList.get(0);
                }
                
                deleteTmpSrHaps(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 印刷積層HAPS_登録処理
            insertSrHaps(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrHaps);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("登録しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);

            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }

        }

        return processData;

    }

    /**
     * 修正処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataCorrect(ProcessData processData) {

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // 規格チェック
        List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList = new ArrayList<>();
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(processData.getItemList(), kikakuchiInputErrorInfoList);

        // 規格チェック内で想定外のエラーが発生した場合、エラーを出して中断
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        // 規格値エラーがある場合は規格値エラーをセット(警告表示)
        if (!kikakuchiInputErrorInfoList.isEmpty()) {
            processData.setKikakuchiInputErrorInfoList(kikakuchiInputErrorInfoList);
        }

        // 後続処理メソッド設定
        processData.setMethod("doKakunin");

        return processData;
    }
    
    /**
     * 修正処理時の確認
     * 
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doKakunin(ProcessData processData) {
        
        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B045Const.USER_AUTH_UPDATE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doCorrect");
        
        return processData;
    }

    /**
     * 修正処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doCorrect(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
            
            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());
            
            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            // 最新のリビジョンを採番
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);

            // 印刷積層HAPS_更新処理
            updateSrHaps(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("修正しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
        }

        return processData;
    }

    /**
     * 削除処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataDelete(ProcessData processData) {

        // 警告メッセージの設定
        processData.setWarnMessage("削除します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B045Const.USER_AUTH_DELETE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doDelete");
        return processData;
    }

    /**
     * 削除処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doDelete(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());
            
            //Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());
            
            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd03RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            // 最新のリビジョンを採番
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // 印刷積層HAPS_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrHaps(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷積層HAPS_削除処理
            deleteSrHaps(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("削除しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            if (SQL_STATE_RECORD_LOCK_ERR.equals(e.getSQLState())) {
                // レコードロックエラー時
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
            } else {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            }
        }

        return processData;
    }

    /**
     * ボタン活性・非活性設定
     *
     * @param processData 処理制御データ
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @return 処理制御データ
     */
    private ProcessData setButtonEnable(ProcessData processData, String jotaiFlg) {

        List<String> activeIdList = new ArrayList<>();
        List<String> inactiveIdList = new ArrayList<>();
        switch (jotaiFlg) {
            case JOTAI_FLG_TOROKUZUMI:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B045Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B045Const.BTN_DELETE_BOTTOM,
                        GXHDO101B045Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B045Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B045Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B045Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B045Const.BTN_DELETE_TOP,
                        GXHDO101B045Const.BTN_UPDATE_TOP,
                        GXHDO101B045Const.BTN_START_DATETIME_TOP,
                        GXHDO101B045Const.BTN_END_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B045Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B045Const.BTN_INSERT_BOTTOM,
                        GXHDO101B045Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B045Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B045Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B045Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B045Const.BTN_INSERT_BOTTOM,
                        GXHDO101B045Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B045Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B045Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B045Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B045Const.BTN_INSERT_TOP,
                        GXHDO101B045Const.BTN_START_DATETIME_TOP,
                        GXHDO101B045Const.BTN_END_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B045Const.BTN_DELETE_BOTTOM,
                        GXHDO101B045Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B045Const.BTN_DELETE_TOP,
                        GXHDO101B045Const.BTN_UPDATE_TOP));

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
            // 仮登録
            case GXHDO101B045Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B045Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B045Const.BTN_INSERT_TOP:
            case GXHDO101B045Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B045Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B045Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B045Const.BTN_UPDATE_TOP:
            case GXHDO101B045Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B045Const.BTN_DELETE_TOP:
            case GXHDO101B045Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 印刷積層開始日時
            case GXHDO101B045Const.BTN_START_DATETIME_TOP:
            case GXHDO101B045Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 印刷積層終了日時
            case GXHDO101B045Const.BTN_END_DATETIME_TOP:
            case GXHDO101B045Const.BTN_END_DATETIME_BOTTOM:
                method = "setShuryouDateTime";
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
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    private ProcessData setInitData(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        int paramJissekino = (Integer) session.getAttribute("jissekino");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
        if (sekkeiData == null || sekkeiData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 設計情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, getMapSekkeiAssociation()));

        // 製版ﾏｽﾀ情報取得
        String pattern = StringUtil.nullToBlank(sekkeiData.get("PATTERN")); //電極製版名 
        Map daPatternMasData = loadDaPatternMas(queryRunnerQcdb, pattern);
        if (daPatternMasData == null || daPatternMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000034"));
        }
        
        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ

        // ﾛｯﾄ区分ﾏｽﾀ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
        }

        // ｵｰﾅｰﾏｽﾀ情報の取得
        Map ownerMasData = loadOwnerMas(queryRunnerWip, ownercode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016"));
        }
        
        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData, Map shikakariData, String lotNo) {
        
        // ロットNo
        this.setItemData(processData, GXHDO101B045Const.LOTNO, lotNo);
        
        // KCPNO
        this.setItemData(processData, GXHDO101B045Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        
        // ｾｯﾄ数
        if (!JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg()) && !JOTAI_FLG_TOROKUZUMI.equals(processData.getInitJotaiFlg())) {
            String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
            String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
            if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
                this.setItemData(processData, GXHDO101B045Const.SET_SUU, "0");
            } else {
                BigDecimal decHasseisu = new BigDecimal(suuryo);
                BigDecimal decTorikosuu = new BigDecimal(torikosuu);
                BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
                this.setItemData(processData, GXHDO101B045Const.SET_SUU, setsu.toPlainString());
            }
        }        
        
        // 客先
        this.setItemData(processData, GXHDO101B045Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B045Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B045Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B045Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B045Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        String genryou = StringUtil.nullToBlank(sekkeiData.get("GENRYOU"));
        if (StringUtil.isEmpty(genryou)) {
            this.setItemData(processData, GXHDO101B045Const.DENKYOKU_TAPE, "");
        } else {
            this.setItemData(processData, GXHDO101B045Const.DENKYOKU_TAPE, genryou
                    + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));
        }

        // 積層数
        if (!JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg()) && !JOTAI_FLG_TOROKUZUMI.equals(processData.getInitJotaiFlg())) {
            this.setItemData(processData, GXHDO101B045Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                    + "μm×"
                    + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                    + "層  "
                    + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                    + "枚");
        }

        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        if (StringUtil.isEmpty(lRetsu)) {
            this.setItemData(processData, GXHDO101B045Const.RETSU_GYOU, "");
        } else {
            String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
            this.setItemData(processData, GXHDO101B045Const.RETSU_GYOU, lRetsu + "×" + wRetsu);
        }
        
        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        if (StringUtil.isEmpty(lSun)) {
            this.setItemData(processData, GXHDO101B045Const.PITCH, "");
        } else {
            String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
            this.setItemData(processData, GXHDO101B045Const.PITCH, lSun + "×" + wSun);
        }

        // 電極製版名
        this.setItemData(processData, GXHDO101B045Const.ESEIHANMEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));
    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @param jissekino 実績No
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino) throws SQLException {

        List<SrHaps> srHapsDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // メイン画面にデータを設定する(デフォルト値)
                for (FXHDD01 fxhdd001 : processData.getItemList()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }
                return true;
            }

            // 印刷積層・HAPSデータ取得
            srHapsDataList = getSrHapsData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srHapsDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srHapsDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srHapsDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srHapsData 印刷積層HAPSデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrHaps srHapsData) {
        // ｾｯﾄ数
        this.setItemData(processData, GXHDO101B045Const.SET_SUU, getSrHapsItemData(GXHDO101B045Const.SET_SUU, srHapsData));
        // 積層数
        this.setItemData(processData, GXHDO101B045Const.SEKISOU_SU, getSrHapsItemData(GXHDO101B045Const.SEKISOU_SU, srHapsData));
        // 電極ﾍﾟｰｽﾄ	    
        this.setItemData(processData, GXHDO101B045Const.EPASTE, getSrHapsItemData(GXHDO101B045Const.EPASTE, srHapsData));
        // 電極ﾍﾟｰｽﾄﾛｯﾄNo 
        this.setItemData(processData, GXHDO101B045Const.PASTELOTNO, getSrHapsItemData(GXHDO101B045Const.PASTELOTNO, srHapsData));
        // 電極ﾍﾟｰｽﾄ粘度 
        this.setItemData(processData, GXHDO101B045Const.PASTENENDO, getSrHapsItemData(GXHDO101B045Const.PASTENENDO, srHapsData));
        // 積層ｽﾗｲﾄﾞ量	 
        this.setItemData(processData, GXHDO101B045Const.SEKISOUSLIDERYO, getSrHapsItemData(GXHDO101B045Const.SEKISOUSLIDERYO, srHapsData));
        // 仮ﾌﾟﾚｽ高圧	 
        this.setItemData(processData, GXHDO101B045Const.KARIPRESSKOU, getSrHapsItemData(GXHDO101B045Const.KARIPRESSKOU, srHapsData));
        // 仮ﾌﾟﾚｽ低圧	 
        this.setItemData(processData, GXHDO101B045Const.KARIPRESSTEI, getSrHapsItemData(GXHDO101B045Const.KARIPRESSTEI, srHapsData));
        // 仮ﾌﾟﾚｽ脱気時間 
        this.setItemData(processData, GXHDO101B045Const.KARIPRESSDAKKI, getSrHapsItemData(GXHDO101B045Const.KARIPRESSDAKKI, srHapsData));
        // 号機		 
        this.setItemData(processData, GXHDO101B045Const.GOKI, getSrHapsItemData(GXHDO101B045Const.GOKI, srHapsData));
        // 電極製版No	 
        this.setItemData(processData, GXHDO101B045Const.SEIHANNO, getSrHapsItemData(GXHDO101B045Const.SEIHANNO, srHapsData));
        // 電極製版枚数	 
        this.setItemData(processData, GXHDO101B045Const.SEIHANMAISUU, getSrHapsItemData(GXHDO101B045Const.SEIHANMAISUU, srHapsData));
        // 電極ｽｷｰｼﾞNo	 
        this.setItemData(processData, GXHDO101B045Const.SKEEGENO, getSrHapsItemData(GXHDO101B045Const.SKEEGENO, srHapsData));
        // 電極ｽｷｰｼﾞ枚数 
        this.setItemData(processData, GXHDO101B045Const.SKEEGEMAISUU, getSrHapsItemData(GXHDO101B045Const.SKEEGEMAISUU, srHapsData));

        // 電極ｸﾘｱﾗﾝｽ 
        this.setItemData(processData, GXHDO101B045Const.CLEARANCE, getSrHapsItemData(GXHDO101B045Const.CLEARANCE, srHapsData));
        // 電極差圧 
        this.setItemData(processData, GXHDO101B045Const.SAATU, getSrHapsItemData(GXHDO101B045Const.SAATU, srHapsData));
        // 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ 
        this.setItemData(processData, GXHDO101B045Const.SKEEGESPEED, getSrHapsItemData(GXHDO101B045Const.SKEEGESPEED, srHapsData));
        // ｽｷｰｼﾞ角度 
        this.setItemData(processData, GXHDO101B045Const.SKEEGEKAKUDO, getSrHapsItemData(GXHDO101B045Const.SKEEGEKAKUDO, srHapsData));
        // 加圧時間 
        this.setItemData(processData, GXHDO101B045Const.KAATUJIKAN, getSrHapsItemData(GXHDO101B045Const.KAATUJIKAN, srHapsData));
        // 剥離速度 
        this.setItemData(processData, GXHDO101B045Const.HAKURISPEED, getSrHapsItemData(GXHDO101B045Const.HAKURISPEED, srHapsData));
        // 上乾燥温度 
        this.setItemData(processData, GXHDO101B045Const.UWAKANSOONDO, getSrHapsItemData(GXHDO101B045Const.UWAKANSOONDO, srHapsData));
        // 下乾燥温度 
        this.setItemData(processData, GXHDO101B045Const.SHITAKANSOONDO, getSrHapsItemData(GXHDO101B045Const.SHITAKANSOONDO, srHapsData));
        // 乾燥速度 
        this.setItemData(processData, GXHDO101B045Const.KANSOSOKUDO, getSrHapsItemData(GXHDO101B045Const.KANSOSOKUDO, srHapsData));
        // 電極L/Dｽﾀｰﾄ時 
        this.setItemData(processData, GXHDO101B045Const.ELDSTART, getSrHapsItemData(GXHDO101B045Const.ELDSTART, srHapsData));
        // 印刷幅1 
        this.setItemData(processData, GXHDO101B045Const.INSATUHABA1, getSrHapsItemData(GXHDO101B045Const.INSATUHABA1, srHapsData));
        // 印刷幅2 
        this.setItemData(processData, GXHDO101B045Const.INSATUHABA2, getSrHapsItemData(GXHDO101B045Const.INSATUHABA2, srHapsData));
        // 印刷幅3 
        this.setItemData(processData, GXHDO101B045Const.INSATUHABA3, getSrHapsItemData(GXHDO101B045Const.INSATUHABA3, srHapsData));
        // 印刷幅4 
        this.setItemData(processData, GXHDO101B045Const.INSATUHABA4, getSrHapsItemData(GXHDO101B045Const.INSATUHABA4, srHapsData));
        // 印刷幅5 
        this.setItemData(processData, GXHDO101B045Const.INSATUHABA5, getSrHapsItemData(GXHDO101B045Const.INSATUHABA5, srHapsData));
        // 外観初め 
        this.setItemData(processData, GXHDO101B045Const.GAIKANF, getSrHapsItemData(GXHDO101B045Const.GAIKANF, srHapsData));
        // 外観中間 
        this.setItemData(processData, GXHDO101B045Const.GAIKANM, getSrHapsItemData(GXHDO101B045Const.GAIKANM, srHapsData));
        // 外観終わり 
        this.setItemData(processData, GXHDO101B045Const.GAIKANE, getSrHapsItemData(GXHDO101B045Const.GAIKANE, srHapsData));
        // 印刷積層開始日 
        this.setItemData(processData, GXHDO101B045Const.KAISHI_DAY, getSrHapsItemData(GXHDO101B045Const.KAISHI_DAY, srHapsData));
        // 印刷積層開始時間 
        this.setItemData(processData, GXHDO101B045Const.KAISHI_TIME, getSrHapsItemData(GXHDO101B045Const.KAISHI_TIME, srHapsData));
        // 印刷積層開始担当者 
        this.setItemData(processData, GXHDO101B045Const.TANTOSYA, getSrHapsItemData(GXHDO101B045Const.TANTOSYA, srHapsData));
        // 印刷積層開始確認者 
        this.setItemData(processData, GXHDO101B045Const.KAKUNINSYA, getSrHapsItemData(GXHDO101B045Const.KAKUNINSYA, srHapsData));
        // 印刷積層終了日 
        this.setItemData(processData, GXHDO101B045Const.SHURYOU_DAY, getSrHapsItemData(GXHDO101B045Const.SHURYOU_DAY, srHapsData));
        // 印刷積層終了時間 
        this.setItemData(processData, GXHDO101B045Const.SHURYOU_TIME, getSrHapsItemData(GXHDO101B045Const.SHURYOU_TIME, srHapsData));
        // 印刷積層終了担当者 
        this.setItemData(processData, GXHDO101B045Const.ENDTANTOU, getSrHapsItemData(GXHDO101B045Const.ENDTANTOU, srHapsData));
        // 処理ｾｯﾄ数 
        this.setItemData(processData, GXHDO101B045Const.SYORISETSUU, getSrHapsItemData(GXHDO101B045Const.SYORISETSUU, srHapsData));
        // 良品ｾｯﾄ数 
        this.setItemData(processData, GXHDO101B045Const.RYOUHINSETSUU, getSrHapsItemData(GXHDO101B045Const.RYOUHINSETSUU, srHapsData));
        // 積層後厚み 
        this.setItemData(processData, GXHDO101B045Const.SEKISOATUMI, getSrHapsItemData(GXHDO101B045Const.SEKISOATUMI, srHapsData));
        // 備考1
        this.setItemData(processData, GXHDO101B045Const.BIKOU1, getSrHapsItemData(GXHDO101B045Const.BIKOU1, srHapsData));
        // 備考2
        this.setItemData(processData, GXHDO101B045Const.BIKOU2, getSrHapsItemData(GXHDO101B045Const.BIKOU2, srHapsData));
    }

    /**
     * 印刷積層HAPSの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 印刷積層HAPS登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrHaps> getSrHapsData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrHaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrHaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }
    
    /**
     * [設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        String sql = "SELECT SEKKEINO, GENRYOU, ETAPE, EATUMI, SOUSUU, EMAISUU, PATTERN "
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
                put("SEKKEINO", "設計No");
                put("GENRYOU", "電極ﾃｰﾌﾟ");
                put("ETAPE", "電極ﾃｰﾌﾟ");
                put("EATUMI", "積層数");
                put("SOUSUU", "積層数");
                put("EMAISUU", "積層数");
                put("PATTERN", "電極製版名");
            }
        };

        return map;
    }

    /**
     * [ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotKubunCode ﾛｯﾄ区分ｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadLotKbnMas(QueryRunner queryRunnerDoc, String lotKubunCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT lotkubun "
                + "FROM lotkumas "
                + "WHERE lotkubuncode = ?";

        List<Object> params = new ArrayList<>();
        params.add(lotKubunCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ｵｰﾅｰｺｰﾄﾞﾏｽﾀｰ]から、ｵｰﾅｰ名を取得
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param ownerCode ｵｰﾅｰｺｰﾄﾞ(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadOwnerMas(QueryRunner queryRunnerWip, String ownerCode) throws SQLException {

        // オーナーデータの取得
        String sql = "SELECT \"owner\" AS ownername "
                + "FROM ownermas "
                + "WHERE ownercode = ?";

        List<Object> params = new ArrayList<>();
        params.add(ownerCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }
    
     /**
     * [実績]から、ﾃﾞｰﾀを取得
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
     private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {
         

        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);
        
        List<String> dataList= new ArrayList<>(Arrays.asList(data));
        
        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT syorisuu "
                + "FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";
        
        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());
        
        sql += " ORDER BY syoribi DESC, syorijikoku DESC";
        
        Map mapping = new HashMap<>();
        mapping.put("syorisuu", "syorisuu");
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<Jisseki>> beanHandler = new BeanListHandler<>(Jisseki.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);                
        params.addAll(dataList);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, beanHandler, params.toArray());
    }

    /**
     * 仕掛データ検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT kcpno, oyalotedaban, suuryo, torikosuu, lotkubuncode, ownercode, tokuisaki"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND gamen_id = ?";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [品質DB登録実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND gamen_id = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(formId);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
    }

    /**
     * 最大リビジョン+1のデータを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(formId);
        Map map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            newRev = new BigDecimal(String.valueOf(map.get("rev")));
            newRev = newRev.add(BigDecimal.ONE);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return newRev;
    }

    /**
     * [印刷積層HAPS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrHaps> loadSrHaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " KOJYO ,LOTNO ,EDABAN ,KCPNO ,TAPESYURUI ,TAPELOTNO ,GENRYOKIGO ,SETUBIONDO ,SETUBISITUDO ,PASTELOTNO ,"
                + " PASTENENDO ,PASTEONDO ,SEIHANNO ,SEIHANMAISUU ,SKEEGENO ,SKEEGEMAISUU ,CLEARANCE ,KANSOONDO ,KANSOSOKUDO ,"
                + " KARIPRESSKOU ,KARIPRESSTEI ,KARIPRESSDAKKI ,SAATU ,SKEEGESPEED ,SKEEGEKAKUDO ,MLD ,SEKISOATUMI ,INSATUHABA1 ,"
                + " INSATUHABA2 ,INSATUHABA3 ,INSATUHABA4 ,INSATUHABA5 ,GOKI ,TANTOSYA ,KAKUNINSYA ,KAISINICHIJI ,SYURYONICHIJI ,"
                + " GAIKANF ,GAIKANM ,GAIKANE ,TOROKUNICHIJI ,KOSINNICHIJI, setsuu, sekisouryo ,epaste ,eseihanmei ,sekisouslideryo ,kaatujikan ,"
                + " hakurispeed ,uwakansoondo ,shitakansoondo ,eldstart ,endtantou ,syorisetsuu ,ryouhinsetsuu ,bikou1 ,bikou2 , "
                + " revision ,'0' AS deleteflag "
                + "FROM sr_haps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
        
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("KOJYO", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LOTNO", "lotno"); //ﾛｯﾄNo
        mapping.put("EDABAN", "edaban"); //枝番
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TAPESYURUI", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("TAPELOTNO", "tapelotno"); //ﾃｰﾌﾟﾛｯﾄNo
        mapping.put("GENRYOKIGO", "genryokigo"); //原料記号
        mapping.put("SETUBIONDO", "setubiondo"); //設備温度
        mapping.put("SETUBISITUDO", "setubisitudo"); //設備湿度
        mapping.put("PASTELOTNO", "pastelotno"); //電極ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("PASTENENDO", "pastenendo"); //電極ﾍﾟｰｽﾄ粘度
        mapping.put("PASTEONDO", "pasteondo"); //ﾍﾟｰｽﾄ温度
        mapping.put("SEIHANNO", "seihanno"); //電極製版No
        mapping.put("SEIHANMAISUU", "seihanmaisuu"); //電極製版枚数
        mapping.put("SKEEGENO", "skeegeno"); //電極ｽｷｰｼﾞNo
        mapping.put("SKEEGEMAISUU", "skeegemaisuu"); //電極ｽｷｰｼﾞ枚数
        mapping.put("CLEARANCE", "clearance"); //電極ｸﾘｱﾗﾝｽ
        mapping.put("KANSOONDO", "kansoondo"); //乾燥温度
        mapping.put("KANSOSOKUDO", "kansosokudo"); //乾燥速度
        mapping.put("KARIPRESSKOU", "karipresskou"); //仮ﾌﾟﾚｽ高圧
        mapping.put("KARIPRESSTEI", "karipresstei"); //仮ﾌﾟﾚｽ低圧
        mapping.put("KARIPRESSDAKKI", "karipressdakki"); //仮ﾌﾟﾚｽ脱気時間
        mapping.put("SAATU", "saatu"); //電極差圧
        mapping.put("SKEEGESPEED", "skeegespeed"); //電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("SKEEGEKAKUDO", "skeegekakudo"); //ｽｷｰｼﾞ角度
        mapping.put("MLD", "mld"); //MLD
        mapping.put("SEKISOATUMI", "sekisoatumi"); //積層後厚み
        mapping.put("INSATUHABA1", "insatuhaba1"); //印刷幅1
        mapping.put("INSATUHABA2", "insatuhaba2"); //印刷幅2
        mapping.put("INSATUHABA3", "insatuhaba3"); //印刷幅3
        mapping.put("INSATUHABA4", "insatuhaba4"); //印刷幅4
        mapping.put("INSATUHABA5", "insatuhaba5"); //印刷幅5
        mapping.put("GOKI", "goki"); //号機
        mapping.put("TANTOSYA", "tantosya"); //印刷積層開始担当者
        mapping.put("KAKUNINSYA", "kakuninsya"); //印刷積層開始確認者
        mapping.put("KAISINICHIJI", "kaisinichiji"); //印刷積層開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //印刷積層終了日時
        mapping.put("GAIKANF", "gaikanf"); //外観初め
        mapping.put("GAIKANM", "gaikanm"); //外観中間
        mapping.put("GAIKANE", "gaikane"); //外観終わり
        mapping.put("TOROKUNICHIJI", "torokunichiji"); //登録日時
        mapping.put("KOSINNICHIJI", "kosinnichiji"); //更新日時
        mapping.put("setsuu", "setsuu"); //ｾｯﾄ数
        mapping.put("sekisouryo", "sekisouryo"); //積層量
        mapping.put("epaste", "epaste"); //電極ﾍﾟｰｽﾄ
        mapping.put("eseihanmei", "eseihanmei"); //電極製版名
        mapping.put("sekisouslideryo", "sekisouslideryo"); //積層ｽﾗｲﾄﾞ量
        mapping.put("kaatujikan", "kaatujikan"); //加圧時間
        mapping.put("hakurispeed", "hakurispeed"); //剥離速度
        mapping.put("uwakansoondo", "uwakansoondo"); //上乾燥温度
        mapping.put("shitakansoondo", "shitakansoondo"); //下乾燥温度
        mapping.put("eldstart", "eldstart"); //電極L/Dｽﾀｰﾄ時
        mapping.put("endtantou", "endtantou"); //印刷積層終了担当者
        mapping.put("syorisetsuu", "syorisetsuu"); //処理ｾｯﾄ数
        mapping.put("ryouhinsetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrHaps>> beanHandler = new BeanListHandler<>(SrHaps.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷積層HAPS_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrHaps> loadTmpSrHaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        String sql = "SELECT "
                + " KOJYO ,LOTNO ,EDABAN ,KCPNO ,TAPESYURUI ,TAPELOTNO ,GENRYOKIGO ,SETUBIONDO ,SETUBISITUDO ,PASTELOTNO ,"
                + " PASTENENDO ,PASTEONDO ,SEIHANNO ,SEIHANMAISUU ,SKEEGENO ,SKEEGEMAISUU ,CLEARANCE ,KANSOONDO ,KANSOSOKUDO ,"
                + " KARIPRESSKOU ,KARIPRESSTEI ,KARIPRESSDAKKI ,SAATU ,SKEEGESPEED ,SKEEGEKAKUDO ,MLD ,SEKISOATUMI ,INSATUHABA1 ,"
                + " INSATUHABA2 ,INSATUHABA3 ,INSATUHABA4 ,INSATUHABA5 ,GOKI ,TANTOSYA ,KAKUNINSYA ,KAISINICHIJI ,SYURYONICHIJI ,"
                + " GAIKANF ,GAIKANM ,GAIKANE ,TOROKUNICHIJI ,KOSINNICHIJI ,setsuu ,sekisouryo ,epaste ,eseihanmei ,sekisouslideryo ,kaatujikan ,"
                + " hakurispeed ,uwakansoondo ,shitakansoondo ,eldstart ,endtantou ,syorisetsuu ,ryouhinsetsuu ,bikou1 ,bikou2 , "
                + " revision , deleteflag "
                + "FROM tmp_sr_haps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND deleteflag = ? ";
        
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(0);
        
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("KOJYO", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LOTNO", "lotno"); //ﾛｯﾄNo
        mapping.put("EDABAN", "edaban"); //枝番
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TAPESYURUI", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("TAPELOTNO", "tapelotno"); //ﾃｰﾌﾟﾛｯﾄNo
        mapping.put("GENRYOKIGO", "genryokigo"); //原料記号
        mapping.put("SETUBIONDO", "setubiondo"); //設備温度
        mapping.put("SETUBISITUDO", "setubisitudo"); //設備湿度
        mapping.put("PASTELOTNO", "pastelotno"); //電極ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("PASTENENDO", "pastenendo"); //電極ﾍﾟｰｽﾄ粘度
        mapping.put("PASTEONDO", "pasteondo"); //ﾍﾟｰｽﾄ温度
        mapping.put("SEIHANNO", "seihanno"); //電極製版No
        mapping.put("SEIHANMAISUU", "seihanmaisuu"); //電極製版枚数
        mapping.put("SKEEGENO", "skeegeno"); //電極ｽｷｰｼﾞNo
        mapping.put("SKEEGEMAISUU", "skeegemaisuu"); //電極ｽｷｰｼﾞ枚数
        mapping.put("CLEARANCE", "clearance"); //電極ｸﾘｱﾗﾝｽ
        mapping.put("KANSOONDO", "kansoondo"); //乾燥温度
        mapping.put("KANSOSOKUDO", "kansosokudo"); //乾燥速度
        mapping.put("KARIPRESSKOU", "karipresskou"); //仮ﾌﾟﾚｽ高圧
        mapping.put("KARIPRESSTEI", "karipresstei"); //仮ﾌﾟﾚｽ低圧
        mapping.put("KARIPRESSDAKKI", "karipressdakki"); //仮ﾌﾟﾚｽ脱気時間
        mapping.put("SAATU", "saatu"); //電極差圧
        mapping.put("SKEEGESPEED", "skeegespeed"); //電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("SKEEGEKAKUDO", "skeegekakudo"); //ｽｷｰｼﾞ角度
        mapping.put("MLD", "mld"); //MLD
        mapping.put("SEKISOATUMI", "sekisoatumi"); //積層後厚み
        mapping.put("INSATUHABA1", "insatuhaba1"); //印刷幅1
        mapping.put("INSATUHABA2", "insatuhaba2"); //印刷幅2
        mapping.put("INSATUHABA3", "insatuhaba3"); //印刷幅3
        mapping.put("INSATUHABA4", "insatuhaba4"); //印刷幅4
        mapping.put("INSATUHABA5", "insatuhaba5"); //印刷幅5
        mapping.put("GOKI", "goki"); //号機
        mapping.put("TANTOSYA", "tantosya"); //印刷積層開始担当者
        mapping.put("KAKUNINSYA", "kakuninsya"); //印刷積層開始確認者
        mapping.put("KAISINICHIJI", "kaisinichiji"); //印刷積層開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //印刷積層終了日時
        mapping.put("GAIKANF", "gaikanf"); //外観初め
        mapping.put("GAIKANM", "gaikanm"); //外観中間
        mapping.put("GAIKANE", "gaikane"); //外観終わり
        mapping.put("TOROKUNICHIJI", "torokunichiji"); //登録日時
        mapping.put("KOSINNICHIJI", "kosinnichiji"); //更新日時
        mapping.put("setsuu", "setsuu"); //ｾｯﾄ数
        mapping.put("sekisouryo", "sekisouryo"); //積層量
        mapping.put("epaste", "epaste"); //電極ﾍﾟｰｽﾄ
        mapping.put("eseihanmei", "eseihanmei"); //電極製版名
        mapping.put("sekisouslideryo", "sekisouslideryo"); //積層ｽﾗｲﾄﾞ量
        mapping.put("kaatujikan", "kaatujikan"); //加圧時間
        mapping.put("hakurispeed", "hakurispeed"); //剥離速度
        mapping.put("uwakansoondo", "uwakansoondo"); //上乾燥温度
        mapping.put("shitakansoondo", "shitakansoondo"); //下乾燥温度
        mapping.put("eldstart", "eldstart"); //電極L/Dｽﾀｰﾄ時
        mapping.put("endtantou", "endtantou"); //印刷積層終了担当者
        mapping.put("syorisetsuu", "syorisetsuu"); //処理ｾｯﾄ数
        mapping.put("ryouhinsetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrHaps>> beanHandler = new BeanListHandler<>(SrHaps.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [製版ﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param pattern 電極製版名(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaPatternMas(QueryRunner queryRunnerQcdb, String pattern) throws SQLException {

        // 製版ﾏｽﾀデータの取得
        String sql = "SELECT LRETU, WRETU, LSUN, WSUN "
                + " FROM da_patternmas WHERE PATTERN = ? ";

        List<Object> params = new ArrayList<>();
        params.add(pattern);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * 枝番コピー確認メッセージ表示
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData confEdabanCopy(ProcessData processData) {
        processData.setWarnMessage("親ﾃﾞｰﾀを取得します。よろしいですか？");

        processData.setMethod("edabanCopy");
        return processData;
    }

    /**
     * 枝番コピー
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData edabanCopy(ProcessData processData) {
        try {

            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            
            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, paramJissekino, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));
            
            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷積層HAPSデータ取得
            List<SrHaps> srHapsDataList = getSrHapsData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srHapsDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srHapsDataList.get(0));

            // 次呼出しメソッドをクリア
            processData.setMethod("");

            return processData;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
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
        List<FXHDD01> selectData
                = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            selectData.get(0).setValue(value);
        }
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
     * @param srHapsData 印刷積層HAPSデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrHaps srHapsData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srHapsData != null) {
            // 元データが存在する場合元データより取得
            return getSrHapsItemData(itemId, srHapsData);
        } else {
            return null;
        }
    }

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
    
    /**
     * Mapから値を取得する(マップがNULLまたは空の場合はNULLを返却)
     *
     * @param map マップ
     * @param mapId ID
     * @return マップから取得した値
     */
    private Object getMapData(Map map, String mapId) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return map.get(mapId);
    }

    /**
     * 品質DB登録実績(fxhdd03)登録処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void insertFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd03 ("
                + "torokusha ,toroku_date ,koshinsha ,koshin_date ,gamen_id ,rev ,kojyo ,lotno ,"
                + "edaban ,jissekino ,jotai_flg ,tsuika_kotei_flg "
                + ") VALUES ("
                + "?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(tantoshaCd); //登録者
        params.add(systemTime); //登録日
        params.add(null); //更新者
        params.add(null); //更新日
        params.add(formId); //画面ID
        params.add(rev); //revision
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //実績No
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ
        params.add("0"); //追加工程ﾌﾗｸﾞ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 品質DB登録実績(fxhdd03)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param jissekino 実績No
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime, int jissekino) throws SQLException {
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = ?  ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日
        params.add(rev); //revision
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ

        // 検索条件
        params.add(formId); //画面ID
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 印刷積層HAPS_仮登録(tmp_sr_haps)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_haps ("
                + " KOJYO ,LOTNO ,EDABAN ,KCPNO ,TAPESYURUI ,TAPELOTNO ,GENRYOKIGO ,SETUBIONDO ,SETUBISITUDO ,PASTELOTNO ,"
                + " PASTENENDO ,PASTEONDO ,SEIHANNO ,SEIHANMAISUU ,SKEEGENO ,SKEEGEMAISUU ,CLEARANCE ,KANSOONDO ,KANSOSOKUDO ,"
                + " KARIPRESSKOU ,KARIPRESSTEI ,KARIPRESSDAKKI ,SAATU ,SKEEGESPEED ,SKEEGEKAKUDO ,MLD ,SEKISOATUMI ,INSATUHABA1 ,"
                + " INSATUHABA2 ,INSATUHABA3 ,INSATUHABA4 ,INSATUHABA5 ,GOKI ,TANTOSYA ,KAKUNINSYA ,KAISINICHIJI ,SYURYONICHIJI ,"
                + " GAIKANF ,GAIKANM ,GAIKANE ,TOROKUNICHIJI ,KOSINNICHIJI ,setsuu, sekisouryo, epaste ,eseihanmei ,sekisouslideryo ,kaatujikan ,"
                + " hakurispeed ,uwakansoondo ,shitakansoondo ,eldstart ,endtantou ,syorisetsuu ,ryouhinsetsuu ,bikou1 ,bikou2 ,"
                + " revision ,deleteflag "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrHaps(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層HAPS_仮登録(tmp_sr_haps)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_haps SET "
                + " KCPNO = ?,PASTELOTNO = ?,PASTENENDO = ?,SEIHANNO = ?,SEIHANMAISUU = ?,SKEEGENO = ?,SKEEGEMAISUU = ?,"
                + " CLEARANCE = ?,KANSOSOKUDO = ?,KARIPRESSKOU = ?,KARIPRESSTEI = ?,KARIPRESSDAKKI = ?,SAATU = ?,SKEEGESPEED = ?,"
                + " SKEEGEKAKUDO = ?,SEKISOATUMI = ?,INSATUHABA1 = ?,INSATUHABA2 = ?,INSATUHABA3 = ?,INSATUHABA4 = ?,"
                + " INSATUHABA5 = ?,GOKI = ?,TANTOSYA = ?,KAKUNINSYA = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,GAIKANF = ?,GAIKANM = ?,"
                + " GAIKANE = ?,KOSINNICHIJI = ?,setsuu = ? ,sekisouryo = ?,epaste = ?,eseihanmei = ?,sekisouslideryo = ?,kaatujikan = ?,hakurispeed = ?,"
                + " uwakansoondo = ?,shitakansoondo = ?,eldstart = ?,endtantou = ?,syorisetsuu = ?,ryouhinsetsuu = ?,bikou1 = ?,"
                + " bikou2 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrHaps> srHapsList = getSrHapsData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrHaps srHaps = null;
        if (!srHapsList.isEmpty()) {
            srHaps = srHapsList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrHaps(false, newRev, 0, "", "", "", systemTime, itemList, srHaps, jissekino, processData);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層HAPS_仮登録(tmp_sr_haps)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_haps "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層HAPS_仮登録(tmp_sr_haps)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srHapsData 印刷積層HAPSデータ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrHaps(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrHaps srHapsData, int jissekino, ProcessData processData) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KCPNO, srHapsData))); // KCPNO    
        
        if (isInsert) {
            params.add(null); // ﾃｰﾌﾟ種類
            params.add(null); // ﾃｰﾌﾟﾛｯﾄNo
            params.add(null); // 原料記号
            params.add(null); // 設備温度
            params.add(null); // 設備湿度
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.PASTELOTNO, srHapsData))); // 電極ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.PASTENENDO, srHapsData))); // 電極ﾍﾟｰｽﾄ粘度
        
        if (isInsert) {
            params.add(null); // ﾍﾟｰｽﾄ温度
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SEIHANNO, srHapsData))); // 電極製版No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SEIHANMAISUU, srHapsData))); // 電極製版枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SKEEGENO, srHapsData))); // 電極ｽｷｰｼﾞNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SKEEGEMAISUU, srHapsData))); // 電極ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.CLEARANCE, srHapsData))); // 電極ｸﾘｱﾗﾝｽ

        if (isInsert) {
            params.add(null); // 乾燥温度
        }
        
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KANSOSOKUDO, srHapsData))); // 乾燥速度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KARIPRESSKOU, srHapsData))); // 仮ﾌﾟﾚｽ高圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KARIPRESSTEI, srHapsData))); // 仮ﾌﾟﾚｽ低圧
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KARIPRESSDAKKI, srHapsData))); // 仮ﾌﾟﾚｽ脱気時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SAATU, srHapsData))); // 電極差圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SKEEGESPEED, srHapsData))); // 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SKEEGEKAKUDO, srHapsData))); // ｽｷｰｼﾞ角度

        if (isInsert) {
            params.add(null); // MLD
        }

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SEKISOATUMI, srHapsData))); // 積層後厚み
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.INSATUHABA1, srHapsData))); // 印刷幅1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.INSATUHABA2, srHapsData))); // 印刷幅2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.INSATUHABA3, srHapsData))); // 印刷幅3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.INSATUHABA4, srHapsData))); // 印刷幅4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.INSATUHABA5, srHapsData))); // 印刷幅5
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.GOKI, srHapsData))); // 号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.TANTOSYA, srHapsData))); // 印刷積層開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KAKUNINSYA, srHapsData))); // 印刷積層開始確認者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KAISHI_DAY, srHapsData),
            getItemData(itemList, GXHDO101B045Const.KAISHI_TIME, srHapsData))); //印刷積層開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SHURYOU_DAY, srHapsData),
            getItemData(itemList, GXHDO101B045Const.SHURYOU_TIME, srHapsData))); // 印刷積層終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.GAIKANF, srHapsData))); // 外観初め
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.GAIKANM, srHapsData))); // 外観中間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.GAIKANE, srHapsData))); // 外観終わり

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SET_SUU, srHapsData))); // ｾｯﾄ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SEKISOU_SU, srHapsData))); // 積層量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.EPASTE, srHapsData))); // 電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.ESEIHANMEI, srHapsData))); // 電極製版名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SEKISOUSLIDERYO, srHapsData))); // 積層ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.KAATUJIKAN, srHapsData))); // 加圧時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.HAKURISPEED, srHapsData))); // 剥離速度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.UWAKANSOONDO, srHapsData))); // 上乾燥温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SHITAKANSOONDO, srHapsData))); // 下乾燥温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.ELDSTART, srHapsData))); // 電極L/Dｽﾀｰﾄ時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.ENDTANTOU, srHapsData))); // 印刷積層終了担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.SYORISETSUU, srHapsData))); // 処理ｾｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.RYOUHINSETSUU, srHapsData))); // 良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.BIKOU1, srHapsData))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B045Const.BIKOU2, srHapsData))); // 備考2
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        
        return params;
    }

    /**
     * 印刷積層HAPS(sr_haps)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrHaps 印刷積層HAPS仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban,Timestamp systemTime, List<FXHDD01> itemList, SrHaps tmpSrHaps) throws SQLException {

        String sql = "INSERT INTO sr_haps ("
                + " KOJYO ,LOTNO ,EDABAN ,KCPNO ,TAPESYURUI ,TAPELOTNO ,GENRYOKIGO ,SETUBIONDO ,SETUBISITUDO ,PASTELOTNO ,"
                + " PASTENENDO ,PASTEONDO ,SEIHANNO ,SEIHANMAISUU ,SKEEGENO ,SKEEGEMAISUU ,CLEARANCE ,KANSOONDO ,KANSOSOKUDO ,"
                + " KARIPRESSKOU ,KARIPRESSTEI ,KARIPRESSDAKKI ,SAATU ,SKEEGESPEED ,SKEEGEKAKUDO ,MLD ,SEKISOATUMI ,INSATUHABA1 ,"
                + " INSATUHABA2 ,INSATUHABA3 ,INSATUHABA4 ,INSATUHABA5 ,GOKI ,TANTOSYA ,KAKUNINSYA ,KAISINICHIJI ,SYURYONICHIJI ,"
                + " GAIKANF ,GAIKANM ,GAIKANE ,TOROKUNICHIJI ,KOSINNICHIJI ,setsuu , sekisouryo ,epaste ,eseihanmei ,sekisouslideryo ,kaatujikan ,"
                + " hakurispeed ,uwakansoondo ,shitakansoondo ,eldstart ,endtantou ,syorisetsuu ,ryouhinsetsuu ,bikou1 ,bikou2 ,"
                + " revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrHaps(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrHaps);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層HAPS(sr_haps)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @throws SQLException 例外エラー
     */
    private void updateSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_haps SET "
                + " KCPNO = ?,PASTELOTNO = ?,PASTENENDO = ?,SEIHANNO = ?,SEIHANMAISUU = ?,SKEEGENO = ?,SKEEGEMAISUU = ?,"
                + " CLEARANCE = ?,KANSOSOKUDO = ?,KARIPRESSKOU = ?,KARIPRESSTEI = ?,KARIPRESSDAKKI = ?,SAATU = ?,SKEEGESPEED = ?,"
                + " SKEEGEKAKUDO = ?,SEKISOATUMI = ?,INSATUHABA1 = ?,INSATUHABA2 = ?,INSATUHABA3 = ?,INSATUHABA4 = ?,"
                + " INSATUHABA5 = ?,GOKI = ?,TANTOSYA = ?,KAKUNINSYA = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,GAIKANF = ?,GAIKANM = ?,"
                + " GAIKANE = ?,KOSINNICHIJI = ?,setsuu = ? ,sekisouryo = ?,epaste = ?,eseihanmei = ?,sekisouslideryo = ?,kaatujikan = ?,hakurispeed = ?,"
                + " uwakansoondo = ?,shitakansoondo = ?,eldstart = ?,endtantou = ?,syorisetsuu = ?,ryouhinsetsuu = ?,bikou1 = ?,"
                + " bikou2 = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND revision = ? ";

        // 更新前の値を取得
        List<SrHaps> srHapsList = getSrHapsData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrHaps srHaps = null;
        if (!srHapsList.isEmpty()) {
            srHaps = srHapsList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrHaps(false, newRev, "", "", "", systemTime, itemList, srHaps);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層HAPS(sr_haps)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srHapsData 印刷積層HAPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrHaps(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrHaps srHapsData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.KCPNO, srHapsData))); // KCPNO    
        
        if (isInsert) {
            params.add(null); // ﾃｰﾌﾟ種類
            params.add(null); // ﾃｰﾌﾟﾛｯﾄNo
            params.add(null); // 原料記号
            params.add(null); // 設備温度
            params.add(null); // 設備湿度
        }
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.PASTELOTNO, srHapsData))); // 電極ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.PASTENENDO, srHapsData))); // 電極ﾍﾟｰｽﾄ粘度
        
        if (isInsert) {
            params.add(null); // ﾍﾟｰｽﾄ温度
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.SEIHANNO, srHapsData))); // 電極製版No
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SEIHANMAISUU, srHapsData))); // 電極製版枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.SKEEGENO, srHapsData))); // 電極ｽｷｰｼﾞNo
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SKEEGEMAISUU, srHapsData))); // 電極ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.CLEARANCE, srHapsData))); // 電極ｸﾘｱﾗﾝｽ

        if (isInsert) {
            params.add(null); // 乾燥温度
        }
        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.KANSOSOKUDO, srHapsData))); // 乾燥速度
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.KARIPRESSKOU, srHapsData))); // 仮ﾌﾟﾚｽ高圧
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.KARIPRESSTEI, srHapsData))); // 仮ﾌﾟﾚｽ低圧
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.KARIPRESSDAKKI, srHapsData))); // 仮ﾌﾟﾚｽ脱気時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.SAATU, srHapsData))); // 電極差圧
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SKEEGESPEED, srHapsData))); // 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SKEEGEKAKUDO, srHapsData))); // ｽｷｰｼﾞ角度

        if (isInsert) {
            params.add(null); // MLD
        }

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SEKISOATUMI, srHapsData))); // 積層後厚み
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.INSATUHABA1, srHapsData))); // 印刷幅1
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.INSATUHABA2, srHapsData))); // 印刷幅2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.INSATUHABA3, srHapsData))); // 印刷幅3
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.INSATUHABA4, srHapsData))); // 印刷幅4
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.INSATUHABA5, srHapsData))); // 印刷幅5
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.GOKI, srHapsData))); // 号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.TANTOSYA, srHapsData))); // 印刷積層開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.KAKUNINSYA, srHapsData))); // 印刷積層開始確認者
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B045Const.KAISHI_DAY, srHapsData),
            getItemData(itemList, GXHDO101B045Const.KAISHI_TIME, srHapsData))); //印刷積層開始日時
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B045Const.SHURYOU_DAY, srHapsData),
            getItemData(itemList, GXHDO101B045Const.SHURYOU_TIME, srHapsData))); // 印刷積層終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.GAIKANF, srHapsData))); // 外観初め
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.GAIKANM, srHapsData))); // 外観中間
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.GAIKANE, srHapsData))); // 外観終わり

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SET_SUU, srHapsData))); // ｾｯﾄ数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.SEKISOU_SU, srHapsData))); // 積層量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.EPASTE, srHapsData))); // 電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.ESEIHANMEI, srHapsData))); // 電極製版名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.SEKISOUSLIDERYO, srHapsData))); // 積層ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.KAATUJIKAN, srHapsData))); // 加圧時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.HAKURISPEED, srHapsData))); // 剥離速度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.UWAKANSOONDO, srHapsData))); // 上乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.SHITAKANSOONDO, srHapsData))); // 下乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B045Const.ELDSTART, srHapsData))); // 電極L/Dｽﾀｰﾄ時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.ENDTANTOU, srHapsData))); // 印刷積層終了担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.SYORISETSUU, srHapsData))); // 処理ｾｯﾄ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B045Const.RYOUHINSETSUU, srHapsData))); // 良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.BIKOU1, srHapsData))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B045Const.BIKOU2, srHapsData))); // 備考2
        params.add(newRev); //revision
        
        return params;
    }

    /**
     * 印刷積層HAPS(sr_haps)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_haps "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [印刷積層HAPS_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_haps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        int newDeleteFlg = 0;
        if (!StringUtil.isEmpty(StringUtil.nullToBlank(resultMap.get("deleteflag")))) {
            newDeleteFlg = Integer.parseInt(StringUtil.nullToBlank(resultMap.get("deleteflag")));
        }
        newDeleteFlg++;

        return newDeleteFlg;
    }

    /**
     * リビジョンチェック
     *
     * @param processData 処理制御データ
     * @param fxhdd03RevInfo 品質DB登録実績データ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd03RevInfo) throws SQLException {

        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、データが存在する場合
            if (fxhdd03RevInfo != null && !fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
            }
        } else {
            // 品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }
        }

        return null;

    }

    /**
     * 開始時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B045Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B045Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 終了時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B045Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B045Const.SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processData.setMethod("");
        return processData;
    }
    
    /**
     * 日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param itemDay 項目日付(日)
     * @param itemTime 項目日付(時間)
     * @param setDateTime 設定日付
     */
    private void setDateTimeItem(FXHDD01 itemDay, FXHDD01 itemTime, Date setDateTime) {
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srHapsData 印刷積層HAPSデータ
     * @return DB値
     */
    private String getSrHapsItemData(String itemId, SrHaps srHapsData) {
        switch (itemId) {            
            // KCPNO
            case GXHDO101B045Const.KCPNO:
                return StringUtil.nullToBlank(srHapsData.getKcpno());
            // ｾｯﾄ数
            case GXHDO101B045Const.SET_SUU:
                return StringUtil.nullToBlank(srHapsData.getSetsuu());
            // 積層数
            case GXHDO101B045Const.SEKISOU_SU:
                return StringUtil.nullToBlank(srHapsData.getSekisouryo());
            // 電極ﾍﾟｰｽﾄ	
            case GXHDO101B045Const.EPASTE:
                return StringUtil.nullToBlank(srHapsData.getEpaste());
            // 電極ﾍﾟｰｽﾄﾛｯﾄNo
            case GXHDO101B045Const.PASTELOTNO:
                return StringUtil.nullToBlank(srHapsData.getPastelotno());
            // 電極ﾍﾟｰｽﾄ粘度	
            case GXHDO101B045Const.PASTENENDO:
                return StringUtil.nullToBlank(srHapsData.getPastenendo());
                
            // 電極製版名
            case GXHDO101B045Const.ESEIHANMEI:
                return StringUtil.nullToBlank(srHapsData.getEseihanmei());
                
            // 積層ｽﾗｲﾄﾞ量
            case GXHDO101B045Const.SEKISOUSLIDERYO:
                return StringUtil.nullToBlank(srHapsData.getSekisouslideryo());
            // 仮ﾌﾟﾚｽ高圧	
            case GXHDO101B045Const.KARIPRESSKOU:
                return StringUtil.nullToBlank(srHapsData.getKaripresskou());
            // 仮ﾌﾟﾚｽ低圧	
            case GXHDO101B045Const.KARIPRESSTEI:
                return StringUtil.nullToBlank(srHapsData.getKaripresstei());
            // 仮ﾌﾟﾚｽ脱気時間
            case GXHDO101B045Const.KARIPRESSDAKKI:
                return StringUtil.nullToBlank(srHapsData.getKaripressdakki());
            // 号機		
            case GXHDO101B045Const.GOKI:
                return StringUtil.nullToBlank(srHapsData.getGoki());
            // 電極製版No	
            case GXHDO101B045Const.SEIHANNO:
                return StringUtil.nullToBlank(srHapsData.getSeihanno());
            // 電極製版枚数	
            case GXHDO101B045Const.SEIHANMAISUU:
                return StringUtil.nullToBlank(srHapsData.getSeihanmaisuu());
            // 電極ｽｷｰｼﾞNo	
            case GXHDO101B045Const.SKEEGENO:
                return StringUtil.nullToBlank(srHapsData.getSkeegeno());
            // 電極ｽｷｰｼﾞ枚数
            case GXHDO101B045Const.SKEEGEMAISUU:
                return StringUtil.nullToBlank(srHapsData.getSkeegemaisuu());

            // 電極ｸﾘｱﾗﾝｽ 
            case GXHDO101B045Const.CLEARANCE:
                return StringUtil.nullToBlank(srHapsData.getClearance());
            // 電極差圧 
            case GXHDO101B045Const.SAATU:
                return StringUtil.nullToBlank(srHapsData.getSaatu());
            // 電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ 
            case GXHDO101B045Const.SKEEGESPEED:
                return StringUtil.nullToBlank(srHapsData.getSkeegespeed());
            // ｽｷｰｼﾞ角度 
            case GXHDO101B045Const.SKEEGEKAKUDO:
                return StringUtil.nullToBlank(srHapsData.getSkeegekakudo());
            // 加圧時間 
            case GXHDO101B045Const.KAATUJIKAN:
                return StringUtil.nullToBlank(srHapsData.getKaatujikan());
            // 剥離速度 
            case GXHDO101B045Const.HAKURISPEED:
                return StringUtil.nullToBlank(srHapsData.getHakurispeed());
            // 上乾燥温度 
            case GXHDO101B045Const.UWAKANSOONDO:
                return StringUtil.nullToBlank(srHapsData.getUwakansoondo());
            // 下乾燥温度 
            case GXHDO101B045Const.SHITAKANSOONDO:
                return StringUtil.nullToBlank(srHapsData.getShitakansoondo());
            // 乾燥速度 
            case GXHDO101B045Const.KANSOSOKUDO:
                return StringUtil.nullToBlank(srHapsData.getKansosokudo());
            // 電極L/Dｽﾀｰﾄ時 
            case GXHDO101B045Const.ELDSTART:
                return StringUtil.nullToBlank(srHapsData.getEldstart());
            // 印刷幅1 
            case GXHDO101B045Const.INSATUHABA1:
                return StringUtil.nullToBlank(srHapsData.getInsatuhaba1());
            // 印刷幅2 
            case GXHDO101B045Const.INSATUHABA2:
                return StringUtil.nullToBlank(srHapsData.getInsatuhaba2());
            // 印刷幅3 
            case GXHDO101B045Const.INSATUHABA3:
                return StringUtil.nullToBlank(srHapsData.getInsatuhaba3());
            // 印刷幅4 
            case GXHDO101B045Const.INSATUHABA4:
                return StringUtil.nullToBlank(srHapsData.getInsatuhaba4());
            // 印刷幅5 
            case GXHDO101B045Const.INSATUHABA5:
                return StringUtil.nullToBlank(srHapsData.getInsatuhaba5());
            // 外観初め 
            case GXHDO101B045Const.GAIKANF:
                return StringUtil.nullToBlank(srHapsData.getGaikanf());
            // 外観中間 
            case GXHDO101B045Const.GAIKANM:
                return StringUtil.nullToBlank(srHapsData.getGaikanm());
            // 外観終わり 
            case GXHDO101B045Const.GAIKANE:
                return StringUtil.nullToBlank(srHapsData.getGaikane());
            // 印刷積層開始日 
            case GXHDO101B045Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srHapsData.getKaisinichiji(), "yyMMdd");
            // 印刷積層開始時間 
            case GXHDO101B045Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srHapsData.getKaisinichiji(), "HHmm");
            // 印刷積層開始担当者 
            case GXHDO101B045Const.TANTOSYA:
                return StringUtil.nullToBlank(srHapsData.getTantosya());
            // 印刷積層開始確認者 
            case GXHDO101B045Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srHapsData.getKakuninsya());
            // 印刷積層終了日 
            case GXHDO101B045Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srHapsData.getSyuryonichiji(), "yyMMdd");
            // 印刷積層終了時間 
            case GXHDO101B045Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srHapsData.getSyuryonichiji(), "HHmm");
            // 印刷積層終了担当者 
            case GXHDO101B045Const.ENDTANTOU:
                return StringUtil.nullToBlank(srHapsData.getEndtantou());
            // 処理ｾｯﾄ数 
            case GXHDO101B045Const.SYORISETSUU:
                return StringUtil.nullToBlank(srHapsData.getSyorisetsuu());
            // 良品ｾｯﾄ数 
            case GXHDO101B045Const.RYOUHINSETSUU:
                return StringUtil.nullToBlank(srHapsData.getRyouhinsetsuu());
            // 積層後厚み 
            case GXHDO101B045Const.SEKISOATUMI:
                return StringUtil.nullToBlank(srHapsData.getSekisoatumi());
            // 備考1 
            case GXHDO101B045Const.BIKOU1:
                return StringUtil.nullToBlank(srHapsData.getBikou1());
            // 備考2 
            case GXHDO101B045Const.BIKOU2:
                return StringUtil.nullToBlank(srHapsData.getBikou2());
            default:
                return null;            
        }
    }

    /**
     * 印刷積層HAPS_仮登録(tmp_sr_haps)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrHaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_haps ("
                + " KOJYO ,LOTNO ,EDABAN ,KCPNO ,TAPESYURUI ,TAPELOTNO ,GENRYOKIGO ,SETUBIONDO ,SETUBISITUDO ,PASTELOTNO ,"
                + " PASTENENDO ,PASTEONDO ,SEIHANNO ,SEIHANMAISUU ,SKEEGENO ,SKEEGEMAISUU ,CLEARANCE ,KANSOONDO ,KANSOSOKUDO ,"
                + " KARIPRESSKOU ,KARIPRESSTEI ,KARIPRESSDAKKI ,SAATU ,SKEEGESPEED ,SKEEGEKAKUDO ,MLD ,SEKISOATUMI ,INSATUHABA1 ,"
                + " INSATUHABA2 ,INSATUHABA3 ,INSATUHABA4 ,INSATUHABA5 ,GOKI ,TANTOSYA ,KAKUNINSYA ,KAISINICHIJI ,SYURYONICHIJI ,"
                + " GAIKANF ,GAIKANM ,GAIKANE ,TOROKUNICHIJI ,KOSINNICHIJI,setsuu, sekisouryo ,epaste ,eseihanmei ,sekisouslideryo ,kaatujikan ,"
                + " hakurispeed ,uwakansoondo ,shitakansoondo ,eldstart ,endtantou ,syorisetsuu ,ryouhinsetsuu ,bikou1 ,bikou2 ,"
                + " revision ,deleteflag "
                + ") SELECT "
                + " KOJYO ,LOTNO ,EDABAN ,KCPNO ,TAPESYURUI ,TAPELOTNO ,GENRYOKIGO ,SETUBIONDO ,SETUBISITUDO ,PASTELOTNO ,"
                + " PASTENENDO ,PASTEONDO ,SEIHANNO ,SEIHANMAISUU ,SKEEGENO ,SKEEGEMAISUU ,CLEARANCE ,KANSOONDO ,KANSOSOKUDO ,"
                + " KARIPRESSKOU ,KARIPRESSTEI ,KARIPRESSDAKKI ,SAATU ,SKEEGESPEED ,SKEEGEKAKUDO ,MLD ,SEKISOATUMI ,INSATUHABA1 ,"
                + " INSATUHABA2 ,INSATUHABA3 ,INSATUHABA4 ,INSATUHABA5 ,GOKI ,TANTOSYA ,KAKUNINSYA ,KAISINICHIJI ,SYURYONICHIJI ,"
                + " GAIKANF ,GAIKANM ,GAIKANE ,? ,? ,setsuu, sekisouryo ,epaste ,eseihanmei ,sekisouslideryo ,kaatujikan ,"
                + " hakurispeed ,uwakansoondo ,shitakansoondo ,eldstart ,endtantou ,syorisetsuu ,ryouhinsetsuu ,bikou1 ,bikou2 ,"
                + " ? ,? "
                + " FROM sr_haps "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        // 更新値
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        // 検索値
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
}
