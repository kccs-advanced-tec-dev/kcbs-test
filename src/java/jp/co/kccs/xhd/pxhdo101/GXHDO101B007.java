/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
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
import jp.co.kccs.xhd.db.model.SrPrepress;
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
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/05/21<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B007(ﾌﾟﾚｽ・仮ﾌﾟﾚｽ)ロジック
 *
 * @author KCSS K.Jo
 * @since  2019/05/21
 */
public class GXHDO101B007 implements IFormLogic {

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
                    GXHDO101B007Const.BTN_START_DATETIME_TOP,
                    GXHDO101B007Const.BTN_END_DATETIME_TOP,
                    GXHDO101B007Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B007Const.BTN_END_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B007Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B007Const.BTN_INSERT_TOP,
                    GXHDO101B007Const.BTN_DELETE_TOP,
                    GXHDO101B007Const.BTN_UPDATE_TOP,
                    GXHDO101B007Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B007Const.BTN_INSERT_BOTTOM,
                    GXHDO101B007Const.BTN_DELETE_BOTTOM,
                    GXHDO101B007Const.BTN_UPDATE_BOTTOM));

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
    public ProcessData checkDataTempRegist(ProcessData processData) {
        
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
        processData.setMethod("doTempRegist");

        return processData;

    }

    /**
     * 仮登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTempRegist(ProcessData processData) {
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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
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
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 仮ﾌﾟﾚｽ_仮登録登録処理
                insertTmpSrPrepress(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            } else {

                // 仮ﾌﾟﾚｽ_仮登録更新処理
                updateTmpSrPrepress(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
    public ProcessData checkDataRegist(ProcessData processData) {

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemRegistCorrect(processData);
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
        processData.setMethod("doRegist");

        return processData;
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B007Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B007Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B007Const.SHURYO_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B007Const.SHURYO_TIME); //終了時刻
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
    public ProcessData doRegist(ProcessData processData) {
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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
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
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrPrepress tmpSrPrepress = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrPrepress> srPrepressList = getSrPrepressData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srPrepressList.isEmpty()) {
                    tmpSrPrepress = srPrepressList.get(0);
                }
                
                deleteTmpSrPrepress(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 仮ﾌﾟﾚｽ_登録処理
            insertSrPrepress(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrPrepress);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        ErrorMessageInfo checkItemErrorInfo = checkItemRegistCorrect(processData);
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

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B007Const.USER_AUTH_UPDATE_PARAM);

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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 仮ﾌﾟﾚｽ_更新処理
            updateSrPrepress(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setUserAuthParam(GXHDO101B007Const.USER_AUTH_DELETE_PARAM);

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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime);

            // 仮ﾌﾟﾚｽ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrPrepress(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 仮ﾌﾟﾚｽ_削除処理
            deleteSrPrepress(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO101B007Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B007Const.BTN_DELETE_BOTTOM,
                        GXHDO101B007Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B007Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B007Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B007Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B007Const.BTN_DELETE_TOP,
                        GXHDO101B007Const.BTN_UPDATE_TOP,
                        GXHDO101B007Const.BTN_START_DATETIME_TOP,
                        GXHDO101B007Const.BTN_END_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B007Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B007Const.BTN_INSERT_BOTTOM,
                        GXHDO101B007Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B007Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B007Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B007Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B007Const.BTN_INSERT_BOTTOM,
                        GXHDO101B007Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B007Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B007Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B007Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B007Const.BTN_INSERT_TOP,
                        GXHDO101B007Const.BTN_START_DATETIME_TOP,
                        GXHDO101B007Const.BTN_END_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B007Const.BTN_DELETE_BOTTOM,
                        GXHDO101B007Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B007Const.BTN_DELETE_TOP,
                        GXHDO101B007Const.BTN_UPDATE_TOP));

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
            case GXHDO101B007Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B007Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B007Const.BTN_INSERT_TOP:
            case GXHDO101B007Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B007Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B007Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B007Const.BTN_UPDATE_TOP:
            case GXHDO101B007Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B007Const.BTN_DELETE_TOP:
            case GXHDO101B007Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B007Const.BTN_START_DATETIME_TOP:
            case GXHDO101B007Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B007Const.BTN_END_DATETIME_TOP:
            case GXHDO101B007Const.BTN_END_DATETIME_BOTTOM:
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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData,  Map shikakariData, String lotNo) {
        
        // 前工程情報の取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");
        
        // ロットNo
        this.setItemData(processData, GXHDO101B007Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B007Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B007Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B007Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B007Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B007Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B007Const.OWNER, ownercode + ":" + owner);
        }

        // セット数
        FXHDD01 itemRowSetsu = this.getItemRow(processData.getItemList(), GXHDO101B007Const.SET_SUU);
        // 前工程情報の設定処理
        CommonUtil.setMaekoteiInfo(itemRowSetsu, maekoteiInfo, "RyouhinSetsuu", true, true);
        
    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId) throws SQLException {

        List<SrPrepress> srPrepressDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId);
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
                
                // 前工程情報の取得
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) externalContext.getSession(false);
                Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");
                
                // 処理セット数(前工程情報がある場合は前工程情報の値をセットする。)
                FXHDD01 itemSyoriSetsu = this.getItemRow(processData.getItemList(), GXHDO101B007Const.SYORI_SETSUU);
                CommonUtil.setMaekoteiInfo(itemSyoriSetsu, maekoteiInfo, "RyouhinSetsuu", false, true);
                
                return true;
            }

            // 仮ﾌﾟﾚｽデータ取得
            srPrepressDataList = getSrPrepressData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srPrepressDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srPrepressDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srPrepressDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srPrepressData 仮ﾌﾟﾚｽデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrPrepress srPrepressData) {        
        
        // 仮ﾌﾟﾚｽ号機
        this.setItemData(processData, GXHDO101B007Const.PRE_PRESS_GOKI, getSrPrepressItemData(GXHDO101B007Const.PRE_PRESS_GOKI, srPrepressData));
        // 緩衝材
        this.setItemData(processData, GXHDO101B007Const.KASHOZAI, getSrPrepressItemData(GXHDO101B007Const.KASHOZAI, srPrepressData));
        // 真空保持
        this.setItemData(processData, GXHDO101B007Const.SINKU_HOJI, getSrPrepressItemData(GXHDO101B007Const.SINKU_HOJI, srPrepressData));
        //1次圧力
        this.setItemData(processData, GXHDO101B007Const.ICHI_JI_ATURYOKU, getSrPrepressItemData(GXHDO101B007Const.ICHI_JI_ATURYOKU, srPrepressData));
        // 1次時間
        this.setItemData(processData, GXHDO101B007Const.ICHI_JI_JIKAN, getSrPrepressItemData(GXHDO101B007Const.ICHI_JI_JIKAN, srPrepressData));
        // 2次圧力
        this.setItemData(processData, GXHDO101B007Const.NI_JI_ATURYOKU, getSrPrepressItemData(GXHDO101B007Const.NI_JI_ATURYOKU, srPrepressData));
        // 2次時間
        this.setItemData(processData, GXHDO101B007Const.NI_JI_JIKAN, getSrPrepressItemData(GXHDO101B007Const.NI_JI_JIKAN, srPrepressData));
        // 3次圧力
        this.setItemData(processData, GXHDO101B007Const.SAN_JI_ATURYOKU, getSrPrepressItemData(GXHDO101B007Const.SAN_JI_ATURYOKU, srPrepressData));
        // 3次時間
        this.setItemData(processData, GXHDO101B007Const.SAN_JI_JIKAN, getSrPrepressItemData(GXHDO101B007Const.SAN_JI_JIKAN, srPrepressData));
        // 真空度
        this.setItemData(processData, GXHDO101B007Const.SINKUDO, getSrPrepressItemData(GXHDO101B007Const.SINKUDO, srPrepressData));
        // 温度
        this.setItemData(processData, GXHDO101B007Const.ONDO, getSrPrepressItemData(GXHDO101B007Const.ONDO, srPrepressData));
        // 保管条件
        this.setItemData(processData, GXHDO101B007Const.HOKANJOUKEN, getSrPrepressItemData(GXHDO101B007Const.HOKANJOUKEN, srPrepressData));
        // 開始日
        this.setItemData(processData, GXHDO101B007Const.KAISHI_DAY, getSrPrepressItemData(GXHDO101B007Const.KAISHI_DAY, srPrepressData));
        // 開始時間
        this.setItemData(processData, GXHDO101B007Const.KAISHI_TIME, getSrPrepressItemData(GXHDO101B007Const.KAISHI_TIME, srPrepressData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B007Const.KAISHI_TANTOSYA, getSrPrepressItemData(GXHDO101B007Const.KAISHI_TANTOSYA, srPrepressData));
        // 開始確認者
        this.setItemData(processData, GXHDO101B007Const.KAISHI_KAKUNINSHA, getSrPrepressItemData(GXHDO101B007Const.KAISHI_KAKUNINSHA, srPrepressData));
        // 終了日
        this.setItemData(processData, GXHDO101B007Const.SHURYO_DAY, getSrPrepressItemData(GXHDO101B007Const.SHURYO_DAY, srPrepressData));
        // 終了時間
        this.setItemData(processData, GXHDO101B007Const.SHURYO_TIME, getSrPrepressItemData(GXHDO101B007Const.SHURYO_TIME, srPrepressData));
        // 終了担当者
        this.setItemData(processData, GXHDO101B007Const.SHURYO_TANTOSYA, getSrPrepressItemData(GXHDO101B007Const.SHURYO_TANTOSYA, srPrepressData));
        // 処理セット数
        this.setItemData(processData, GXHDO101B007Const.SYORI_SETSUU, getSrPrepressItemData(GXHDO101B007Const.SYORI_SETSUU, srPrepressData));
        // 良品セット数
        this.setItemData(processData, GXHDO101B007Const.RYOUHIN_SETSUU, getSrPrepressItemData(GXHDO101B007Const.RYOUHIN_SETSUU, srPrepressData));
        // 備考1
        this.setItemData(processData, GXHDO101B007Const.BIKOU1, getSrPrepressItemData(GXHDO101B007Const.BIKOU1, srPrepressData));
        // 備考2
        this.setItemData(processData, GXHDO101B007Const.BIKOU2, getSrPrepressItemData(GXHDO101B007Const.BIKOU2, srPrepressData));

    }

    /**
     * 仮ﾌﾟﾚｽの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 仮ﾌﾟﾚｽ登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrPrepress> getSrPrepressData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrPrepress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrPrepress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        String sql = "SELECT SEKKEINO "
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
        Map<String, String> map = new LinkedHashMap<>();
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
     * 仕掛データ検索
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 仕掛情報データの取得
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, torikosuu, lotkubuncode, ownercode "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ?";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
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
     * @param formId 画面ID(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
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
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
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
     * [仮ﾌﾟﾚｽ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrPrepress> loadSrPrepress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,tantosya"
                + ",kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,pressside"
                + ",jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,kaatujikan3"
                + ",shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,KCPNO,revision,'0' AS deleteflag "
                + "FROM sr_prepress "
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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("startdatetime", "startdatetime"); //開始日時
        mapping.put("enddatetime", "enddatetime"); //終了日時
        mapping.put("ondohyoji", "ondohyoji"); //温度表示
        mapping.put("aturyokusetteiti", "aturyokusetteiti"); //圧力設定値
        mapping.put("kaatujikan", "kaatujikan"); //加圧時間
        mapping.put("goki", "goki"); //号機ｺｰﾄﾞ
        mapping.put("tantosya", "tantosya"); //担当者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("sagyokubun", "sagyokubun"); //作業区分
        mapping.put("setsuu", "setsuu"); //ｾｯﾄ数
        mapping.put("tansimaisuu", "tansimaisuu"); //端子枚数
        mapping.put("aturyokuhyouji", "aturyokuhyouji"); //圧力表示値
        mapping.put("pressside", "pressside"); //ﾌﾟﾚｽｻｲﾄﾞ
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("koteicode", "koteicode"); //工程ｺｰﾄﾞ
        mapping.put("kansyouzai", "kansyouzai"); //緩衝材
        mapping.put("shinkuuhoji", "shinkuuhoji"); //真空保持
        mapping.put("aturyokusetteiti2", "aturyokusetteiti2"); //2次圧力
        mapping.put("kaatujikan2", "kaatujikan2"); //2次時間
        mapping.put("aturyokusetteiti3", "aturyokusetteiti3"); //3次圧力
        mapping.put("kaatujikan3", "kaatujikan3"); //3次時間
        mapping.put("shinkuudo", "shinkuudo"); //真空度
        mapping.put("Hokanjouken", "hokanjouken"); //保管条件
        mapping.put("EndTantousyacode", "endtantousyacode"); //終了担当者
        mapping.put("RyouhinSetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrPrepress>> beanHandler = new BeanListHandler<>(SrPrepress.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [仮ﾌﾟﾚｽ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrPrepress> loadTmpSrPrepress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        String sql = "SELECT kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,tantosya"
                + ",kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,pressside"
                + ",jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,kaatujikan3"
                + ",shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,KCPNO,revision,deleteflag "
                + "FROM tmp_sr_prepress "
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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("startdatetime", "startdatetime"); //開始日時
        mapping.put("enddatetime", "enddatetime"); //終了日時
        mapping.put("ondohyoji", "ondohyoji"); //温度表示
        mapping.put("aturyokusetteiti", "aturyokusetteiti"); //圧力設定値
        mapping.put("kaatujikan", "kaatujikan"); //加圧時間
        mapping.put("goki", "goki"); //号機ｺｰﾄﾞ
        mapping.put("tantosya", "tantosya"); //担当者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("sagyokubun", "sagyokubun"); //作業区分
        mapping.put("setsuu", "setsuu"); //ｾｯﾄ数
        mapping.put("tansimaisuu", "tansimaisuu"); //端子枚数
        mapping.put("aturyokuhyouji", "aturyokuhyouji"); //圧力表示値
        mapping.put("pressside", "pressside"); //ﾌﾟﾚｽｻｲﾄﾞ
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("koteicode", "koteicode"); //工程ｺｰﾄﾞ
        mapping.put("kansyouzai", "kansyouzai"); //緩衝材
        mapping.put("shinkuuhoji", "shinkuuhoji"); //真空保持
        mapping.put("aturyokusetteiti2", "aturyokusetteiti2"); //2次圧力
        mapping.put("kaatujikan2", "kaatujikan2"); //2次時間
        mapping.put("aturyokusetteiti3", "aturyokusetteiti3"); //3次圧力
        mapping.put("kaatujikan3", "kaatujikan3"); //3次時間
        mapping.put("shinkuudo", "shinkuudo"); //真空度
        mapping.put("Hokanjouken", "hokanjouken"); //保管条件
        mapping.put("EndTantousyacode", "endtantousyacode"); //終了担当者
        mapping.put("RyouhinSetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrPrepress>> beanHandler = new BeanListHandler<>(SrPrepress.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
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
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            
            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));
            
            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 仮ﾌﾟﾚｽデータ取得
            List<SrPrepress> srPrepressDataList = getSrPrepressData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srPrepressDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srPrepressDataList.get(0));

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
     * @param srPrepressData 仮ﾌﾟﾚｽ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrPrepress srPrepressData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srPrepressData != null) {
            // 元データが存在する場合元データより取得
            return getSrPrepressItemData(itemId, srPrepressData);
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
                + "torokusha,toroku_date,koshinsha,koshin_date,gamen_id,rev,kojyo,lotno,"
                + "edaban,jissekino,jotai_flg,tsuika_kotei_flg"
                + ") VALUES ("
                + "?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

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
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = 1  ";

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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 仮ﾌﾟﾚｽ_仮登録(tmp_sr_prepress)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_prepress ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,tantosya"
                + ",kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,pressside"
                + ",jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,kaatujikan3"
                + ",shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,KCPNO,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrPrepress(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 仮ﾌﾟﾚｽ_仮登録(tmp_sr_prepress)更新処理
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
    private void updateTmpSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_prepress SET "
                + "startdatetime = ?,enddatetime = ?,ondohyoji = ?,aturyokusetteiti = ?,kaatujikan = ?,goki = ?,tantosya = ?,kakuninsya = ?,"
                + "biko1 = ?,kosinnichiji = ?,sagyokubun = ?,setsuu = ?,tansimaisuu = ?,aturyokuhyouji = ?,pressside = ?,"
                + "jissekino = ?,koteicode = ?,kansyouzai = ?,shinkuuhoji = ?,aturyokusetteiti2 = ?,kaatujikan2 = ?,aturyokusetteiti3 = ?,"
                + "kaatujikan3 = ?,shinkuudo = ?,Hokanjouken = ?,EndTantousyacode = ?,RyouhinSetsuu = ?,biko2 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrPrepress> srSrPrepressList = getSrPrepressData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrPrepress srPrepress = null;
        if (!srSrPrepressList.isEmpty()) {
            srPrepress = srSrPrepressList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrPrepress(false, newRev, 0, "", "", "", systemTime, itemList, srPrepress);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 仮ﾌﾟﾚｽ_仮登録(tmp_sr_prepress)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_prepress "
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
     * 仮ﾌﾟﾚｽ_仮登録(tmp_sr_prepress)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srPrepressData 仮ﾌﾟﾚｽデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrPrepress(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrPrepress srPrepressData) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.KAISHI_DAY, srPrepressData),
            getItemData(itemList, GXHDO101B007Const.KAISHI_TIME, srPrepressData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SHURYO_DAY, srPrepressData),
            getItemData(itemList, GXHDO101B007Const.SHURYO_TIME, srPrepressData))); //終了日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.ONDO, srPrepressData))); //温度表示
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.ICHI_JI_ATURYOKU, srPrepressData))); //圧力設定値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.ICHI_JI_JIKAN, srPrepressData))); //加圧時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.PRE_PRESS_GOKI, srPrepressData))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.KAISHI_TANTOSYA, srPrepressData))); //担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.KAISHI_KAKUNINSHA, srPrepressData))); //確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.BIKOU1, srPrepressData))); //備考1
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(null); //作業区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SYORI_SETSUU, srPrepressData))); //ｾｯﾄ数
        params.add(null); //端子枚数
        params.add(null); //圧力表示値
        params.add(null); //ﾌﾟﾚｽｻｲﾄﾞ
        params.add(null); //実績No
        params.add(null); //工程ｺｰﾄﾞ
        //緩衝材
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B007Const.KASHOZAI, srPrepressData))) {
            case "なし":
                params.add(0);
                break;
            case "あり":
                params.add(1);
                break;
            default:
                params.add(null);
                break;
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SINKU_HOJI, srPrepressData))); //真空保持
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.NI_JI_ATURYOKU, srPrepressData))); //2次圧力
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.NI_JI_JIKAN, srPrepressData))); //2次時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SAN_JI_ATURYOKU, srPrepressData))); //3次圧力
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SAN_JI_JIKAN, srPrepressData))); //3次時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SINKUDO, srPrepressData))); //真空度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.HOKANJOUKEN, srPrepressData))); //保管条件
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.SHURYO_TANTOSYA, srPrepressData))); //終了担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.RYOUHIN_SETSUU, srPrepressData))); //良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.BIKOU2, srPrepressData))); //備考2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B007Const.KCPNO, srPrepressData)));  //KCPNO
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 仮ﾌﾟﾚｽ(sr_prepress)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrPrepress 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrPrepress tmpSrPrepress) throws SQLException {

        String sql = "INSERT INTO sr_prepress ("
                +  "kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,tantosya"
                + ",kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,pressside"
                + ",jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,kaatujikan3"
                + ",shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,KCPNO,revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        
        List<Object> params = setUpdateParameterSrPrepress(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrPrepress);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 仮ﾌﾟﾚｽ(sr_prepress)更新処理
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
    private void updateSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_prepress SET "
                + "startdatetime = ?,enddatetime = ?,ondohyoji = ?,aturyokusetteiti = ?,kaatujikan = ?,goki = ?,tantosya = ?,kakuninsya = ?,"
                + "biko1 = ?,kosinnichiji = ?,sagyokubun = ?,setsuu = ?,tansimaisuu = ?,aturyokuhyouji = ?,pressside = ?,"
                + "jissekino = ?,koteicode = ?,kansyouzai = ?,shinkuuhoji = ?,aturyokusetteiti2 = ?,kaatujikan2 = ?,aturyokusetteiti3 = ?,"
                + "kaatujikan3 = ?,shinkuudo = ?,Hokanjouken = ?,EndTantousyacode = ?,RyouhinSetsuu = ?,biko2 = ?,KCPNO = ?,revision = ? "                
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrPrepress> srPrepressList = getSrPrepressData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrPrepress srPrepress = null;
        if (!srPrepressList.isEmpty()) {
            srPrepress = srPrepressList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrPrepress(false, newRev, "", "", "", systemTime, itemList, srPrepress);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 仮ﾌﾟﾚｽ(sr_prepress)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srPrepressData 仮ﾌﾟﾚｽデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrPrepress(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrPrepress srPrepressData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B007Const.KAISHI_DAY, srPrepressData),
                getItemData(itemList, GXHDO101B007Const.KAISHI_TIME, srPrepressData))); // 開始日時
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B007Const.SHURYO_DAY, srPrepressData),
                getItemData(itemList, GXHDO101B007Const.SHURYO_TIME, srPrepressData))); // 終了日時
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.ONDO, srPrepressData))); //温度表示
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.ICHI_JI_ATURYOKU, srPrepressData))); //圧力設定値
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.ICHI_JI_JIKAN, srPrepressData))); //加圧時間
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.PRE_PRESS_GOKI, srPrepressData))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.KAISHI_TANTOSYA, srPrepressData))); //開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.KAISHI_KAKUNINSHA, srPrepressData))); //確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.BIKOU1, srPrepressData))); //備考1
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時

        } else {
            params.add(systemTime); //更新日時
        }
        params.add(""); // 作業区分
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.SYORI_SETSUU, srPrepressData))); //処理セット数
        params.add(0); // 端子枚数
        params.add(0); // 圧力表示値
        params.add(""); // ﾌﾟﾚｽｻｲﾄﾞ
        params.add(0); // 実績No
        params.add(""); // 工程ｺｰﾄﾞ
        // 緩衝材
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B007Const.KASHOZAI, srPrepressData))) {
            case "なし":
                params.add(0);
                break;
            case "あり":
                params.add(1);
                break;
            default:
                params.add(9);
                break;
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.SINKU_HOJI, srPrepressData))); //真空保持
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.NI_JI_ATURYOKU, srPrepressData))); //2次圧力
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.NI_JI_JIKAN, srPrepressData))); //2次時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.SAN_JI_ATURYOKU, srPrepressData))); //3次圧力
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.SAN_JI_JIKAN, srPrepressData))); //3次時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.SINKUDO, srPrepressData))); //真空度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.HOKANJOUKEN, srPrepressData))); //保管条件
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.SHURYO_TANTOSYA, srPrepressData))); //終了担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B007Const.RYOUHIN_SETSUU, srPrepressData))); //良品セット数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.BIKOU2, srPrepressData))); //備考2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B007Const.KCPNO, srPrepressData)));  //KCPNO
        params.add(newRev); //revision
        
        return params;
    }

    /**
     * 仮ﾌﾟﾚｽ(sr_prepress)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_prepress "
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
     * [仮ﾌﾟﾚｽ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_prepress "
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
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B007Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B007Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShuryouDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B007Const.SHURYO_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B007Const.SHURYO_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
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
     * @param srPrepressData 仮ﾌﾟﾚｽデータ
     * @return DB値
     */
    private String getSrPrepressItemData(String itemId, SrPrepress srPrepressData) {
        switch (itemId) {
            // 仮ﾌﾟﾚｽ号機
            case GXHDO101B007Const.PRE_PRESS_GOKI:
                return StringUtil.nullToBlank(srPrepressData.getGoki());
            // 緩衝材
            case GXHDO101B007Const.KASHOZAI:
                switch (StringUtil.nullToBlank(srPrepressData.getKansyouzai())) {
                    case "0":
                        return "なし";
                    case "1":
                        return "あり";
                    default:
                        return "";
                }
            // 真空保持
            case GXHDO101B007Const.SINKU_HOJI:
                return StringUtil.nullToBlank(srPrepressData.getShinkuuhoji());
            // 1次圧力
            case GXHDO101B007Const.ICHI_JI_ATURYOKU:
                return StringUtil.nullToBlank(srPrepressData.getAturyokusetteiti());
            // 1次時間
            case GXHDO101B007Const.ICHI_JI_JIKAN:
                return StringUtil.nullToBlank(srPrepressData.getKaatujikan());
            // 2次圧力
            case GXHDO101B007Const.NI_JI_ATURYOKU:
                return StringUtil.nullToBlank(srPrepressData.getAturyokusetteiti2());
            // 2次時間
            case GXHDO101B007Const.NI_JI_JIKAN:
                return StringUtil.nullToBlank(srPrepressData.getKaatujikan2());
            // 3次圧力
            case GXHDO101B007Const.SAN_JI_ATURYOKU:
                return StringUtil.nullToBlank(srPrepressData.getAturyokusetteiti3());
            // 3次時間
            case GXHDO101B007Const.SAN_JI_JIKAN:
                return StringUtil.nullToBlank(srPrepressData.getKaatujikan3());
            // 真空度
            case GXHDO101B007Const.SINKUDO:
                return StringUtil.nullToBlank(srPrepressData.getShinkuudo());
            // 温度
            case GXHDO101B007Const.ONDO:
                return StringUtil.nullToBlank(srPrepressData.getOndohyoji());
            // 保管条件
            case GXHDO101B007Const.HOKANJOUKEN:
                return StringUtil.nullToBlank(srPrepressData.getHokanjouken());
            // 開始日
            case GXHDO101B007Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srPrepressData.getStartdatetime(), "yyMMdd");
            // 開始時間
            case GXHDO101B007Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srPrepressData.getStartdatetime(), "HHmm");
            // 開始担当者
            case GXHDO101B007Const.KAISHI_TANTOSYA:
                return StringUtil.nullToBlank(srPrepressData.getTantosya());
            // 開始確認者
            case GXHDO101B007Const.KAISHI_KAKUNINSHA:
                return StringUtil.nullToBlank(srPrepressData.getKakuninsya());
            // 終了日
            case GXHDO101B007Const.SHURYO_DAY:
                return DateUtil.formattedTimestamp(srPrepressData.getEnddatetime(), "yyMMdd");
            // 終了時間
            case GXHDO101B007Const.SHURYO_TIME:
                return DateUtil.formattedTimestamp(srPrepressData.getEnddatetime(), "HHmm");
            // 終了担当者
            case GXHDO101B007Const.SHURYO_TANTOSYA:
                return StringUtil.nullToBlank(srPrepressData.getEndtantousyacode());            
            // 処理セット数
            case GXHDO101B007Const.SYORI_SETSUU:
                return StringUtil.nullToBlank(srPrepressData.getSetsuu());
            // 良品セット数
            case GXHDO101B007Const.RYOUHIN_SETSUU:
                return StringUtil.nullToBlank(srPrepressData.getRyouhinsetsuu());
            // 備考1
            case GXHDO101B007Const.BIKOU1:
                return StringUtil.nullToBlank(srPrepressData.getBiko1());
            // 備考2
            case GXHDO101B007Const.BIKOU2:
                return StringUtil.nullToBlank(srPrepressData.getBiko2());
            default:
                return null;

        }
    }

    /**
     * 仮ﾌﾟﾚｽ_仮登録(tmp_sr_prepress)登録処理(削除時)
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
    private void insertDeleteDataTmpSrPrepress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_prepress ("
                +  "kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,tantosya"
                + ",kakuninsya,biko1,torokunichiji,kosinnichiji,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,pressside"
                + ",jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,kaatujikan3"
                + ",shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,KCPNO,revision,deleteflag"
                + ") SELECT "
                +  "kojyo,lotno,edaban,startdatetime,enddatetime,ondohyoji,aturyokusetteiti,kaatujikan,goki,tantosya"
                + ",kakuninsya,biko1,?,?,sagyokubun,setsuu,tansimaisuu,aturyokuhyouji,pressside"
                + ",jissekino,koteicode,kansyouzai,shinkuuhoji,aturyokusetteiti2,kaatujikan2,aturyokusetteiti3,kaatujikan3"
                + ",shinkuudo,Hokanjouken,EndTantousyacode,RyouhinSetsuu,biko2,KCPNO,?,? "
                + "FROM sr_prepress "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

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
