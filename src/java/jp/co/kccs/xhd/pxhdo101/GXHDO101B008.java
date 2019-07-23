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
import jp.co.kccs.xhd.db.model.SrPress;
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
 * 変更日	2019/05/22<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B008(ﾌﾟﾚｽ・真空脱気)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/03/06
 */
public class GXHDO101B008 implements IFormLogic {

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
            processData = setInitDate(processData);
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

            //日付入力をチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B008Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B008Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B008Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B008Const.BTN_ENDDATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B008Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B008Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B008Const.BTN_INSERT_TOP,
                    GXHDO101B008Const.BTN_INSERT_BOTTOM,
                    GXHDO101B008Const.BTN_DELETE_TOP,
                    GXHDO101B008Const.BTN_DELETE_BOTTOM,
                    GXHDO101B008Const.BTN_UPDATE_TOP,
                    GXHDO101B008Const.BTN_UPDATE_BOTTOM));

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

                // 真空脱気_仮登録登録処理
                insertTmpSrPress(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            } else {

                // 真空脱気_仮登録更新処理
                updateTmpSrPress(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());
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

        FXHDD01 shinkuCheck = getItemRow(processData.getItemList(), GXHDO101B008Const.SINKU_CHECK); //真空チェック
        if (!"true".equals(shinkuCheck.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(shinkuCheck);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, shinkuCheck.getLabel1());
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 kaishiDay = getItemRow(processData.getItemList(), GXHDO101B008Const.KAISHI_DAY); //開始日
        FXHDD01 kaishiTime = getItemRow(processData.getItemList(), GXHDO101B008Const.KAISHI_TIME); //開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(kaishiDay.getValue(), kaishiTime.getValue());
        FXHDD01 shuryouDay = getItemRow(processData.getItemList(), GXHDO101B008Const.SHURYOU_DAY); //終了日
        FXHDD01 shuryouTime = getItemRow(processData.getItemList(), GXHDO101B008Const.SHURYOU_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(shuryouDay.getValue(), shuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(kaishiDay.getLabel1(), kaishiDate, shuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(kaishiDay, kaishiTime, shuryouDay, shuryouTime);
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
            SrPress tmpSrPress = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrPress> srPressList = getSrPressData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srPressList.isEmpty()) {
                    tmpSrPress = srPressList.get(0);
                }

                deleteTmpSrPress(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 真空脱気_登録処理
            insertSrPress(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrPress);

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
        processData.setUserAuthParam(GXHDO101B008Const.USER_AUTH_UPDATE_PARAM);

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

            // 真空脱気_更新処理
            updateSrPress(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B008Const.USER_AUTH_DELETE_PARAM);

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

            // 真空脱気_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrPress(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 真空脱気_削除処理
            deleteSrPress(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO101B008Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B008Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B008Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B008Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B008Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B008Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B008Const.BTN_UPDATE_TOP,
                        GXHDO101B008Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B008Const.BTN_DELETE_TOP,
                        GXHDO101B008Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B008Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B008Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B008Const.BTN_INSERT_BOTTOM,
                        GXHDO101B008Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B008Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B008Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B008Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B008Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B008Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B008Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B008Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B008Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B008Const.BTN_INSERT_TOP,
                        GXHDO101B008Const.BTN_INSERT_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B008Const.BTN_DELETE_TOP,
                        GXHDO101B008Const.BTN_DELETE_BOTTOM,
                        GXHDO101B008Const.BTN_UPDATE_TOP,
                        GXHDO101B008Const.BTN_UPDATE_BOTTOM
                ));

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
            case GXHDO101B008Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B008Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B008Const.BTN_INSERT_TOP:
            case GXHDO101B008Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B008Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B008Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B008Const.BTN_UPDATE_TOP:
            case GXHDO101B008Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B008Const.BTN_DELETE_TOP:
            case GXHDO101B008Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B008Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B008Const.BTN_STARTDATETIME_BOTTOM:
                method = "setStartDateTime";
                break;
            // 終了日時
            case GXHDO101B008Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B008Const.BTN_ENDDATETIME_BOTTOM:
                method = "setEndDateTime";
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
    private ProcessData setInitDate(ProcessData processData) throws SQLException {

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
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // 前工程情報の取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");
        
        // ロットNo
        this.setItemData(processData, GXHDO101B008Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B008Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B008Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B008Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B008Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B008Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B008Const.OWNER, ownercode + ":" + owner);
        }

        
        // セット数
        FXHDD01 itemRowSetsu = this.getItemRow(processData.getItemList(), GXHDO101B008Const.SETSU);
        // 前工程情報の設定処理
        CommonUtil.setMaekoteiInfo(itemRowSetsu, maekoteiInfo, "RyouhinSetsuu", true, true);

        // 指示
        this.setItemData(processData, GXHDO101B008Const.SIJI, "");

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

        List<SrPress> srPressDataList = new ArrayList<>();
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
                FXHDD01 itemSyoriSetsu = this.getItemRow(processData.getItemList(), GXHDO101B008Const.SHORI_SETSU);
                // 前工程情報の設定処理
                CommonUtil.setMaekoteiInfo(itemSyoriSetsu, maekoteiInfo, "RyouhinSetsuu", false, true);

                return true;
            }

            // 真空脱気データ取得
            srPressDataList = getSrPressData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srPressDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srPressDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srPressDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srPressData 真空脱気データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrPress srPressData) {

        //真空脱気号機
        this.setItemData(processData, GXHDO101B008Const.SINKU_DAKKI_GOKI, getSrPressItemData(GXHDO101B008Const.SINKU_DAKKI_GOKI, srPressData));
        //真空ﾁｪｯｸ
        this.setItemData(processData, GXHDO101B008Const.SINKU_CHECK, getSrPressItemData(GXHDO101B008Const.SINKU_CHECK, srPressData));
        //ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
        this.setItemData(processData, GXHDO101B008Const.CERAPEEL_BLAST, getSrPressItemData(GXHDO101B008Const.CERAPEEL_BLAST, srPressData));
        //緩衝材1
        this.setItemData(processData, GXHDO101B008Const.KASHOZAI1, getSrPressItemData(GXHDO101B008Const.KASHOZAI1, srPressData));
        //SUS板
        this.setItemData(processData, GXHDO101B008Const.SUS_BAN, getSrPressItemData(GXHDO101B008Const.SUS_BAN, srPressData));
        //緩衝材2
        this.setItemData(processData, GXHDO101B008Const.KASHOZAI2, getSrPressItemData(GXHDO101B008Const.KASHOZAI2, srPressData));
        //静水圧ﾌﾟﾚｽ号機
        this.setItemData(processData, GXHDO101B008Const.SEISUIATSU_PRESS_GOKI, getSrPressItemData(GXHDO101B008Const.SEISUIATSU_PRESS_GOKI, srPressData));
        //予熱時間1
        this.setItemData(processData, GXHDO101B008Const.YONETSU_TIME1, getSrPressItemData(GXHDO101B008Const.YONETSU_TIME1, srPressData));
        //予熱時間2
        this.setItemData(processData, GXHDO101B008Const.YONETSU_TIME2, getSrPressItemData(GXHDO101B008Const.YONETSU_TIME2, srPressData));
        //予熱時間3
        this.setItemData(processData, GXHDO101B008Const.YONETSU_TIME3, getSrPressItemData(GXHDO101B008Const.YONETSU_TIME3, srPressData));
        //温度
        this.setItemData(processData, GXHDO101B008Const.ONDO, getSrPressItemData(GXHDO101B008Const.ONDO, srPressData));
        //1次最高圧力
        this.setItemData(processData, GXHDO101B008Const.MAX_ATURYOKU_1_JI, getSrPressItemData(GXHDO101B008Const.MAX_ATURYOKU_1_JI, srPressData));
        //1次時間
        this.setItemData(processData, GXHDO101B008Const.JIKAN_1_JI, getSrPressItemData(GXHDO101B008Const.JIKAN_1_JI, srPressData));
        //2次最高圧力
        this.setItemData(processData, GXHDO101B008Const.MAX_ATURYOKU_2_JI, getSrPressItemData(GXHDO101B008Const.MAX_ATURYOKU_2_JI, srPressData));
        //2次時間
        this.setItemData(processData, GXHDO101B008Const.JIKAN_2_JI, getSrPressItemData(GXHDO101B008Const.JIKAN_2_JI, srPressData));
        //水ﾇﾚｾｯﾄ数
        this.setItemData(processData, GXHDO101B008Const.MIZUNURE_CHECK, getSrPressItemData(GXHDO101B008Const.MIZUNURE_CHECK, srPressData));
        //ﾌﾟﾚｽ後冷却時間
        this.setItemData(processData, GXHDO101B008Const.PRESS_GO_REIKYAKU_TIME, getSrPressItemData(GXHDO101B008Const.PRESS_GO_REIKYAKU_TIME, srPressData));
        //水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
        this.setItemData(processData, GXHDO101B008Const.MIZUKEI_TC_NI_PRESS_MAE_AGING, getSrPressItemData(GXHDO101B008Const.MIZUKEI_TC_NI_PRESS_MAE_AGING, srPressData));
        //開始日
        this.setItemData(processData, GXHDO101B008Const.KAISHI_DAY, getSrPressItemData(GXHDO101B008Const.KAISHI_DAY, srPressData));
        //開始時間
        this.setItemData(processData, GXHDO101B008Const.KAISHI_TIME, getSrPressItemData(GXHDO101B008Const.KAISHI_TIME, srPressData));
        //開始担当者
        this.setItemData(processData, GXHDO101B008Const.KAISHI_TANTOUSHA, getSrPressItemData(GXHDO101B008Const.KAISHI_TANTOUSHA, srPressData));
        //開始確認者
        this.setItemData(processData, GXHDO101B008Const.KAISHI_KAKUNINSHA, getSrPressItemData(GXHDO101B008Const.KAISHI_KAKUNINSHA, srPressData));
        //終了日
        this.setItemData(processData, GXHDO101B008Const.SHURYOU_DAY, getSrPressItemData(GXHDO101B008Const.SHURYOU_DAY, srPressData));
        //終了時間
        this.setItemData(processData, GXHDO101B008Const.SHURYOU_TIME, getSrPressItemData(GXHDO101B008Const.SHURYOU_TIME, srPressData));
        //終了担当者
        this.setItemData(processData, GXHDO101B008Const.SHURYOU_TANTOUSHA, getSrPressItemData(GXHDO101B008Const.SHURYOU_TANTOUSHA, srPressData));
        //処理ｾｯﾄ数
        this.setItemData(processData, GXHDO101B008Const.SHORI_SETSU, getSrPressItemData(GXHDO101B008Const.SHORI_SETSU, srPressData));
        //良品ｾｯﾄ数
        this.setItemData(processData, GXHDO101B008Const.RYOHIN_SETSU, getSrPressItemData(GXHDO101B008Const.RYOHIN_SETSU, srPressData));
        //備考1
        this.setItemData(processData, GXHDO101B008Const.BIKOU1, getSrPressItemData(GXHDO101B008Const.BIKOU1, srPressData));
        //備考2
        this.setItemData(processData, GXHDO101B008Const.BIKOU2, getSrPressItemData(GXHDO101B008Const.BIKOU2, srPressData));
        //ﾌﾟﾚｽ回数
        this.setItemData(processData, GXHDO101B008Const.PRESS_KAISU, getSrPressItemData(GXHDO101B008Const.PRESS_KAISU, srPressData));

    }

    /**
     * 真空脱気の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 真空脱気登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrPress> getSrPressData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrPress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrPress(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * @return 設計データ関連付けリスト
     */
    private List<String[]> getMapSekkeiAssociation() {

        // 対象無し(共通的なチェック処理の為、ロジックは残しておく)
        List<String[]> list = new ArrayList<>();
        return list;
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
     * [真空脱気]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrPress> loadSrPress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,bikou2,bikou3,"
                + "bikou4,bikou5,situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,cerapeel,"
                + "kansyouzai1,susborad,kansyouzai2,seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,aturyoku1max,"
                + "aturyoku2max,mizunureSetsuu,Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,"
                + "RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,KCPNO,revision,'0' AS deleteflag "
                + "FROM sr_press "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
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
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("startdatetime", "startdatetime"); //開始日時
        mapping.put("enddatetime", "enddatetime"); //終了日時
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("ondo", "ondo"); //温度
        mapping.put("tantousya", "tantousya"); //担当者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("situon", "situon"); //室温
        mapping.put("situdo", "situdo"); //湿度
        mapping.put("aturyoku", "aturyoku"); //圧力
        mapping.put("jikan1", "jikan1"); //時間1
        mapping.put("jikan2", "jikan2"); //時間2
        mapping.put("atumimin", "atumimin"); //厚みMIN
        mapping.put("atumimax", "atumimax"); //厚みMAX
        mapping.put("shinkuuhojicheck", "shinkuuhojicheck"); //真空ﾁｪｯｸ
        mapping.put("cerapeel", "cerapeel"); //ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
        mapping.put("kansyouzai1", "kansyouzai1"); //緩衝材1
        mapping.put("susborad", "susborad"); //SUS板
        mapping.put("kansyouzai2", "kansyouzai2"); //緩衝材2
        mapping.put("seisuiatupressgouki", "seisuiatupressgouki"); //静水圧ﾌﾟﾚｽ号機
        mapping.put("yonetujikan1", "yonetujikan1"); //予熱時間1
        mapping.put("yonetujikan2", "yonetujikan2"); //予熱時間2
        mapping.put("yonetujikan3", "yonetujikan3"); //予熱時間3
        mapping.put("aturyoku1max", "aturyoku1max"); //1次最高圧力
        mapping.put("aturyoku2max", "aturyoku2max"); //2次最高圧力
        mapping.put("mizunureSetsuu", "mizunureSetsuu"); //水ﾇﾚｾｯﾄ数
        mapping.put("Pressgoreikyakujikan", "pressgoreikyakujikan"); //ﾌﾟﾚｽ後冷却時間
        mapping.put("pressmaeaging", "pressmaeaging"); //水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
        mapping.put("EndTantousyacode", "endtantousyacode"); //終了担当者
        mapping.put("Setsuu", "setsuu"); //処理ｾｯﾄ数
        mapping.put("RyouhinSetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("Presskaisuu", "presskaisuu"); //ﾌﾟﾚｽ回数
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrPress>> beanHandler = new BeanListHandler<>(SrPress.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [真空脱気_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrPress> loadTmpSrPress(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT "
                + "kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,bikou2,bikou3,"
                + "bikou4,bikou5,situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,cerapeel,"
                + "kansyouzai1,susborad,kansyouzai2,seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,aturyoku1max,"
                + "aturyoku2max,mizunureSetsuu,Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,"
                + "RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,KCPNO,revision,deleteflag "
                + "FROM tmp_sr_press "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? ";
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
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("startdatetime", "startdatetime"); //開始日時
        mapping.put("enddatetime", "enddatetime"); //終了日時
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("ondo", "ondo"); //温度
        mapping.put("tantousya", "tantousya"); //担当者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("situon", "situon"); //室温
        mapping.put("situdo", "situdo"); //湿度
        mapping.put("aturyoku", "aturyoku"); //圧力
        mapping.put("jikan1", "jikan1"); //時間1
        mapping.put("jikan2", "jikan2"); //時間2
        mapping.put("atumimin", "atumimin"); //厚みMIN
        mapping.put("atumimax", "atumimax"); //厚みMAX
        mapping.put("shinkuuhojicheck", "shinkuuhojicheck"); //真空ﾁｪｯｸ
        mapping.put("cerapeel", "cerapeel"); //ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
        mapping.put("kansyouzai1", "kansyouzai1"); //緩衝材1
        mapping.put("susborad", "susborad"); //SUS板
        mapping.put("kansyouzai2", "kansyouzai2"); //緩衝材2
        mapping.put("seisuiatupressgouki", "seisuiatupressgouki"); //静水圧ﾌﾟﾚｽ号機
        mapping.put("yonetujikan1", "yonetujikan1"); //予熱時間1
        mapping.put("yonetujikan2", "yonetujikan2"); //予熱時間2
        mapping.put("yonetujikan3", "yonetujikan3"); //予熱時間3
        mapping.put("aturyoku1max", "aturyoku1max"); //1次最高圧力
        mapping.put("aturyoku2max", "aturyoku2max"); //2次最高圧力
        mapping.put("mizunureSetsuu", "mizunureSetsuu"); //水ﾇﾚｾｯﾄ数
        mapping.put("Pressgoreikyakujikan", "pressgoreikyakujikan"); //ﾌﾟﾚｽ後冷却時間
        mapping.put("pressmaeaging", "pressmaeaging"); //水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
        mapping.put("EndTantousyacode", "endtantousyacode"); //終了担当者
        mapping.put("Setsuu", "setsuu"); //処理ｾｯﾄ数
        mapping.put("RyouhinSetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("Presskaisuu", "presskaisuu"); //ﾌﾟﾚｽ回数
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrPress>> beanHandler = new BeanListHandler<>(SrPress.class, rowProcessor);

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
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

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

            // 真空脱気データ取得
            List<SrPress> srPressDataList = getSrPressData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srPressDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srPressDataList.get(0));

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
     * @param srPressData 真空脱気
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrPress srPressData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srPressData != null) {
            // 元データが存在する場合元データより取得
            return getSrPressItemData(itemId, srPressData);
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
     * 真空脱気_仮登録(tmp_sr_press)登録処理
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
    private void insertTmpSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_press ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,bikou2,bikou3,bikou4,bikou5,"
                + "situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,cerapeel,kansyouzai1,susborad,kansyouzai2,"
                + "seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,aturyoku1max,aturyoku2max,mizunureSetsuu,"
                + "Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,KCPNO,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrPress(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());

    }

    /**
     * 真空脱気_仮登録(tmp_sr_press)更新処理
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
    private void updateTmpSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_press SET "
                + "startdatetime = ?,enddatetime = ?,gouki = ?,ondo = ?,tantousya = ?,kakuninsya = ?,"
                + "bikou1 = ?,bikou2 = ?,jikan1 = ?,jikan2 = ?,"
                + "shinkuuhojicheck = ?,cerapeel = ?,kansyouzai1 = ?,susborad = ?,kansyouzai2 = ?,"
                + "seisuiatupressgouki = ?,yonetujikan1 = ?,yonetujikan2 = ?,yonetujikan3 = ?,aturyoku1max = ?,aturyoku2max = ?,"
                + "mizunureSetsuu = ?,Pressgoreikyakujikan = ?,pressmaeaging = ?,EndTantousyacode = ?,Setsuu = ?,RyouhinSetsuu = ?,Presskaisuu = ?,"
                + "kosinnichiji = ?,KCPNO = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrPress> srPressList = getSrPressData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrPress srPress = null;
        if (!srPressList.isEmpty()) {
            srPress = srPressList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrPress(false, newRev, 0, "", "", "", systemTime, itemList, srPress);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱気_仮登録(tmp_sr_press)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_press "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND revision = ?";

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
     * 真空脱気_仮登録(tmp_sr_press)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srPressData 真空脱気データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrPress(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrPress srPressData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B008Const.KAISHI_DAY, srPressData),
                getItemData(itemList, GXHDO101B008Const.KAISHI_TIME, srPressData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B008Const.SHURYOU_DAY, srPressData),
                getItemData(itemList, GXHDO101B008Const.SHURYOU_TIME, srPressData))); //終了日時

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.SINKU_DAKKI_GOKI, srPressData)));  //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.ONDO, srPressData)));  //温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.KAISHI_TANTOUSHA, srPressData)));  //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.KAISHI_KAKUNINSHA, srPressData)));  //開始確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.BIKOU1, srPressData)));  //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.BIKOU2, srPressData)));  //備考2

        if (isInsert) {
            params.add(null); //備考3
            params.add(null); //備考4
            params.add(null); //備考5
            params.add(null); //湿度
            params.add(null); //室温
            params.add(null); //圧力
        }

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.JIKAN_1_JI, srPressData)));  //時間1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.JIKAN_2_JI, srPressData)));  //時間2
        if (isInsert) {
            params.add(null); //厚みMIN
            params.add(null); //厚みMAX
        }
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B008Const.SINKU_CHECK, srPressData), null));  //真空ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(getItemData(itemList, GXHDO101B008Const.CERAPEEL_BLAST, srPressData))));  //ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B008Const.KASHOZAI1, srPressData), null));//緩衝材1
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B008Const.SUS_BAN, srPressData), null));//SUS板
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B008Const.KASHOZAI2, srPressData), null));//緩衝材2
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(getItemData(itemList, GXHDO101B008Const.SEISUIATSU_PRESS_GOKI, srPressData))));  //静水圧ﾌﾟﾚｽ号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.YONETSU_TIME1, srPressData)));  //予熱時間1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.YONETSU_TIME2, srPressData)));  //予熱時間2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.YONETSU_TIME3, srPressData)));  //予熱時間3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.MAX_ATURYOKU_1_JI, srPressData)));  //1次最高圧力
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.MAX_ATURYOKU_2_JI, srPressData)));  //2次最高圧力
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.MIZUNURE_CHECK, srPressData)));  //水ﾇﾚｾｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.PRESS_GO_REIKYAKU_TIME, srPressData)));  //ﾌﾟﾚｽ後冷却時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.MIZUKEI_TC_NI_PRESS_MAE_AGING, srPressData)));  //水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.SHURYOU_TANTOUSHA, srPressData)));  //終了担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.SHORI_SETSU, srPressData)));  //処理ｾｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.RYOHIN_SETSU, srPressData)));  //良品ｾｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.PRESS_KAISU, srPressData)));  //ﾌﾟﾚｽ回数
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B008Const.KCPNO, srPressData)));  //KCPNO
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 真空脱気(sr_press)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrPress 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrPress tmpSrPress) throws SQLException {

        String sql = "INSERT INTO sr_press ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,bikou2,bikou3,bikou4,bikou5,"
                + "situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,cerapeel,kansyouzai1,susborad,kansyouzai2,"
                + "seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,aturyoku1max,aturyoku2max,mizunureSetsuu,"
                + "Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,KCPNO,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrPress(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrPress);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱気(sr_press)更新処理
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
    private void updateSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_press SET "
                + "startdatetime = ?,enddatetime = ?,gouki = ?,ondo = ?,tantousya = ?,kakuninsya = ?,"
                + "bikou1 = ?,bikou2 = ?,jikan1 = ?,jikan2 = ?,"
                + "shinkuuhojicheck = ?,cerapeel = ?,kansyouzai1 = ?,susborad = ?,kansyouzai2 = ?,"
                + "seisuiatupressgouki = ?,yonetujikan1 = ?,yonetujikan2 = ?,yonetujikan3 = ?,aturyoku1max = ?,aturyoku2max = ?,"
                + "mizunureSetsuu = ?,Pressgoreikyakujikan = ?,pressmaeaging = ?,EndTantousyacode = ?,Setsuu = ?,RyouhinSetsuu = ?,Presskaisuu = ?,"
                + "kosinnichiji = ?,KCPNO = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrPress> srPressList = getSrPressData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrPress srPress = null;
        if (!srPressList.isEmpty()) {
            srPress = srPressList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrPress(false, newRev, "", "", "", systemTime, itemList, srPress);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱気(sr_press)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srPressData 真空脱気データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrPress(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrPress srPressData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B008Const.KAISHI_DAY, srPressData),
                getItemData(itemList, GXHDO101B008Const.KAISHI_TIME, srPressData))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B008Const.SHURYOU_DAY, srPressData),
                getItemData(itemList, GXHDO101B008Const.SHURYOU_TIME, srPressData))); //終了日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.SINKU_DAKKI_GOKI, srPressData)));  //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B008Const.ONDO, srPressData)));  //温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.KAISHI_TANTOUSHA, srPressData)));  //開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.KAISHI_KAKUNINSHA, srPressData)));  //開始確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.BIKOU1, srPressData)));  //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.BIKOU2, srPressData)));  //備考2

        if (isInsert) {
            params.add(""); //備考3
            params.add(""); //備考4
            params.add(""); //備考5
            params.add(0); //湿度
            params.add(0); //室温
            params.add(0); //圧力
        }

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.JIKAN_1_JI, srPressData)));  //時間1
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.JIKAN_2_JI, srPressData)));  //時間2
        if (isInsert) {
            params.add(0); //厚みMIN
            params.add(0); //厚みMAX
        }
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B008Const.SINKU_CHECK, srPressData), 9));  //真空ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(getItemData(itemList, GXHDO101B008Const.CERAPEEL_BLAST, srPressData))));  //ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B008Const.KASHOZAI1, srPressData), 9));//緩衝材1
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B008Const.SUS_BAN, srPressData), 9));//SUS板
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B008Const.KASHOZAI2, srPressData), 9));//緩衝材2
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(getItemData(itemList, GXHDO101B008Const.SEISUIATSU_PRESS_GOKI, srPressData))));  //静水圧ﾌﾟﾚｽ号機
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.YONETSU_TIME1, srPressData)));  //予熱時間1
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.YONETSU_TIME2, srPressData)));  //予熱時間2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.YONETSU_TIME3, srPressData)));  //予熱時間3
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.MAX_ATURYOKU_1_JI, srPressData)));  //1次最高圧力
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.MAX_ATURYOKU_2_JI, srPressData)));  //2次最高圧力
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.MIZUNURE_CHECK, srPressData)));  //水ﾇﾚｾｯﾄ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.PRESS_GO_REIKYAKU_TIME, srPressData)));  //ﾌﾟﾚｽ後冷却時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.MIZUKEI_TC_NI_PRESS_MAE_AGING, srPressData)));  //水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.SHURYOU_TANTOUSHA, srPressData)));  //終了担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.SHORI_SETSU, srPressData)));  //処理ｾｯﾄ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.RYOHIN_SETSU, srPressData)));  //良品ｾｯﾄ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B008Const.PRESS_KAISU, srPressData)));  //ﾌﾟﾚｽ回数
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B008Const.KCPNO, srPressData)));  //KCPNO
        params.add(newRev); //revision
        return params;
    }

    /**
     * 真空脱気(sr_press)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_press "
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
     * [真空脱気_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_press "
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
    public ProcessData setStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B008Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B008Const.KAISHI_TIME);
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
    public ProcessData setEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B008Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B008Const.SHURYOU_TIME);
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
        if (itemDay != null) {
            itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        }

        if (itemTime != null) {
            itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
        }
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srPressData 真空脱気データ
     * @return DB値
     */
    private String getSrPressItemData(String itemId, SrPress srPressData) {
        switch (itemId) {

            //開始日
            case GXHDO101B008Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srPressData.getStartdatetime(), "yyMMdd");
            //開始時間
            case GXHDO101B008Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srPressData.getStartdatetime(), "HHmm");
            //終了日
            case GXHDO101B008Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srPressData.getEnddatetime(), "yyMMdd");
            //終了時間
            case GXHDO101B008Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srPressData.getEnddatetime(), "HHmm");
            //真空脱気号機
            case GXHDO101B008Const.SINKU_DAKKI_GOKI:
                return StringUtil.nullToBlank(srPressData.getGouki());
            //温度
            case GXHDO101B008Const.ONDO:
                return StringUtil.nullToBlank(srPressData.getOndo());
            //開始担当者
            case GXHDO101B008Const.KAISHI_TANTOUSHA:
                return StringUtil.nullToBlank(srPressData.getTantousya());
            //開始確認者
            case GXHDO101B008Const.KAISHI_KAKUNINSHA:
                return StringUtil.nullToBlank(srPressData.getKakuninsya());
            //備考1
            case GXHDO101B008Const.BIKOU1:
                return StringUtil.nullToBlank(srPressData.getBikou1());
            //備考2
            case GXHDO101B008Const.BIKOU2:
                return StringUtil.nullToBlank(srPressData.getBikou2());
            //1次時間
            case GXHDO101B008Const.JIKAN_1_JI:
                return StringUtil.nullToBlank(srPressData.getJikan1());
            //2次時間
            case GXHDO101B008Const.JIKAN_2_JI:
                return StringUtil.nullToBlank(srPressData.getJikan2());
            //真空チェック
            case GXHDO101B008Const.SINKU_CHECK:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srPressData.getShinkuuhojicheck()));
            //ｾﾗﾋﾟｰﾙ/ﾌﾞﾗｽﾄ
            case GXHDO101B008Const.CERAPEEL_BLAST:
                return StringUtil.nullToBlank(srPressData.getCerapeel());
            //緩衝材1
            case GXHDO101B008Const.KASHOZAI1:
                return getComboAriNashiText(StringUtil.nullToBlank(srPressData.getKansyouzai1()));
            //SUS板
            case GXHDO101B008Const.SUS_BAN:
                return getComboAriNashiText(StringUtil.nullToBlank(srPressData.getSusborad()));
            //緩衝材2
            case GXHDO101B008Const.KASHOZAI2:
                return getComboAriNashiText(StringUtil.nullToBlank(srPressData.getKansyouzai2()));
            //静水圧ﾌﾟﾚｽ号機
            case GXHDO101B008Const.SEISUIATSU_PRESS_GOKI:
                return StringUtil.nullToBlank(srPressData.getSeisuiatupressgouki());
            //予熱時間1
            case GXHDO101B008Const.YONETSU_TIME1:
                return StringUtil.nullToBlank(srPressData.getYonetujikan1());
            //予熱時間2
            case GXHDO101B008Const.YONETSU_TIME2:
                return StringUtil.nullToBlank(srPressData.getYonetujikan2());
            //予熱時間3
            case GXHDO101B008Const.YONETSU_TIME3:
                return StringUtil.nullToBlank(srPressData.getYonetujikan3());
            //1次最高圧力
            case GXHDO101B008Const.MAX_ATURYOKU_1_JI:
                return StringUtil.nullToBlank(srPressData.getAturyoku1max());
            //2次最高圧力
            case GXHDO101B008Const.MAX_ATURYOKU_2_JI:
                return StringUtil.nullToBlank(srPressData.getAturyoku2max());
            //水ﾇﾚｾｯﾄ数
            case GXHDO101B008Const.MIZUNURE_CHECK:
                return StringUtil.nullToBlank(srPressData.getMizunureSetsuu());
            //ﾌﾟﾚｽ後冷却時間
            case GXHDO101B008Const.PRESS_GO_REIKYAKU_TIME:
                return StringUtil.nullToBlank(srPressData.getPressgoreikyakujikan());
            //水系TC Niﾌﾟﾚｽ前ｴｰｼﾞﾝｸﾞ
            case GXHDO101B008Const.MIZUKEI_TC_NI_PRESS_MAE_AGING:
                return StringUtil.nullToBlank(srPressData.getPressmaeaging());
            //終了担当者
            case GXHDO101B008Const.SHURYOU_TANTOUSHA:
                return StringUtil.nullToBlank(srPressData.getEndtantousyacode());
            //処理ｾｯﾄ数
            case GXHDO101B008Const.SHORI_SETSU:
                return StringUtil.nullToBlank(srPressData.getSetsuu());
            //良品ｾｯﾄ数
            case GXHDO101B008Const.RYOHIN_SETSU:
                return StringUtil.nullToBlank(srPressData.getRyouhinsetsuu());
            //ﾌﾟﾚｽ回数
            case GXHDO101B008Const.PRESS_KAISU:
                return StringUtil.nullToBlank(srPressData.getPresskaisuu());

            default:
                return null;

        }
    }

    /**
     * 真空脱気_仮登録(tmp_sr_press)登録処理(削除時)
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
    private void insertDeleteDataTmpSrPress(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_press ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,bikou2,bikou3,bikou4,bikou5,"
                + "situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,cerapeel,kansyouzai1,susborad,kansyouzai2,"
                + "seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,aturyoku1max,aturyoku2max,mizunureSetsuu,"
                + "Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,RyouhinSetsuu,Presskaisuu,torokunichiji,kosinnichiji,KCPNO,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,startdatetime,enddatetime,gouki,ondo,tantousya,kakuninsya,bikou1,bikou2,bikou3,bikou4,bikou5,"
                + "situon,situdo,aturyoku,jikan1,jikan2,atumimin,atumimax,shinkuuhojicheck,cerapeel,kansyouzai1,susborad,kansyouzai2,"
                + "seisuiatupressgouki,yonetujikan1,yonetujikan2,yonetujikan3,aturyoku1max,aturyoku2max,mizunureSetsuu,"
                + "Pressgoreikyakujikan,pressmaeaging,EndTantousyacode,Setsuu,RyouhinSetsuu,Presskaisuu,?,?,KCPNO,?,? "
                + "FROM sr_press "
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

    /**
     * コンボボックス(あり,なし)Value値取得
     *
     * @param comboText コンボボックステキスト
     * @return コンボボックスValue値
     */
    private Integer getComboAriNashiValue(String comboText, Integer defaultValue) {
        switch (StringUtil.nullToBlank(comboText)) {
            case "なし":
                return 0;
            case "あり":
                return 1;
            default:
                return defaultValue;
        }
    }

    /**
     * コンボボックス(あり,なし)テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboAriNashiText(String comboValue) {
        switch (comboValue) {
            case "0":
                return "なし";
            case "1":
                return "あり";
            default:
                return "";
        }
    }

    /**
     * チェックボックス値(チェックボックス内のValue値)取得
     *
     * @param dbValue コンボボックス(DB内)Value値
     * @return コンボボックステキスト値
     */
    private String getCheckBoxCheckValue(String dbValue) {
        if ("1".equals(dbValue)) {
            return "true";
        }
        return "false";
    }

    /**
     * チェックボックス値(DB内のValue値)取得
     *
     * @param checkBoxValue コンボボックスValue値
     * @param defaultValue チェックがついていない場合のデフォルト値
     * @return コンボボックステキスト値
     */
    private Integer getCheckBoxDbValue(String checkBoxValue, Integer defaultValue) {
        if ("true".equals(StringUtil.nullToBlank(checkBoxValue).toLowerCase())) {
            return 1;
        }
        return defaultValue;
    }
}
