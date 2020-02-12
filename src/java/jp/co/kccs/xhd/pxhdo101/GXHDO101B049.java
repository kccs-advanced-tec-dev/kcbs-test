/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.SrTapingCheck;
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
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/02/05<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B049(TPﾁｪｯｸ)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/10
 */
public class GXHDO101B049 implements IFormLogic {

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

            //処理時にエラーの背景色を戻さない機能として登録
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B049Const.BTN_TPNG_SHUTOKU_TOP,
                    GXHDO101B049Const.BTN_TPNG_SHUTOKU_BOTTOM,
                    GXHDO101B049Const.BTN_KENSA_START_TOP,
                    GXHDO101B049Const.BTN_KENSA_START_BOTTOM,
                    GXHDO101B049Const.BTN_KENSA_END_TOP,
                    GXHDO101B049Const.BTN_KENSA_END_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B049Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B049Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B049Const.BTN_INSERT_TOP,
                    GXHDO101B049Const.BTN_INSERT_BOTTOM,
                    GXHDO101B049Const.BTN_DELETE_TOP,
                    GXHDO101B049Const.BTN_DELETE_BOTTOM,
                    GXHDO101B049Const.BTN_UPDATE_TOP,
                    GXHDO101B049Const.BTN_UPDATE_BOTTOM));

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
            case GXHDO101B049Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B049Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B049Const.BTN_INSERT_TOP:
            case GXHDO101B049Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B049Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B049Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B049Const.BTN_UPDATE_TOP:
            case GXHDO101B049Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B049Const.BTN_DELETE_TOP:
            case GXHDO101B049Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 検査開始
            case GXHDO101B049Const.BTN_KENSA_START_TOP:
            case GXHDO101B049Const.BTN_KENSA_START_BOTTOM:
                method = "setKensaStartDateTime";
                break;
            // 検査終了
            case GXHDO101B049Const.BTN_KENSA_END_TOP:
            case GXHDO101B049Const.BTN_KENSA_END_BOTTOM:
                method = "setKensaEndDateTime";
                break;
            // TPNG取得
            case GXHDO101B049Const.BTN_TPNG_SHUTOKU_TOP:
            case GXHDO101B049Const.BTN_TPNG_SHUTOKU_BOTTOM:
                method = "doTpngShutoku";
                break;

            default:
                method = "error";
                break;
        }

        return method;
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // テーピングチェック_仮登録登録処理
                insertTmpSrTapingCheck(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekiNo, systemTime, processData.getItemList(), processData.getHiddenDataMap());

            } else {

                // テーピングチェック_仮登録更新処理
                updateTmpSrTapingCheck(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekiNo, systemTime, processData.getItemList(), processData.getHiddenDataMap());
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

        ErrorMessageInfo errorMessageInfo;

        // ﾎｯﾊﾟｰﾈｼﾞ確認
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B049Const.REEL_CHECK_KEKKA));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 kaishiDay = getItemRow(processData.getItemList(), GXHDO101B049Const.KENSA_KAISHI_DAY); //検査開始日
        FXHDD01 kaishiTime = getItemRow(processData.getItemList(), GXHDO101B049Const.KENSA_KAISHI_TIME); //検査開始時間
        Date kaishiDate = DateUtil.convertStringToDate(kaishiDay.getValue(), kaishiTime.getValue());
        FXHDD01 shuryouDay = getItemRow(processData.getItemList(), GXHDO101B049Const.KENSA_SHURYOU_DAY); //検査終了日
        FXHDD01 shuryouTime = getItemRow(processData.getItemList(), GXHDO101B049Const.KENSA_SHURYOU_TIME); //検査終了時間
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrTapingCheck tmpSrTapingCheck = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTapingCheck> srTapingCheckList = getSrTapingCheckData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, jissekiNo);
                if (!srTapingCheckList.isEmpty()) {
                    tmpSrTapingCheck = srTapingCheckList.get(0);
                }

                deleteTmpSrTapingCheck(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekiNo);
            }

            // テーピングチェック_登録処理
            insertSrTapingCheck(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, jissekiNo, systemTime, processData.getItemList(), tmpSrTapingCheck, processData.getHiddenDataMap());

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
        processData.setUserAuthParam(GXHDO101B049Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);

            // テーピングチェック_更新処理
            updateSrTapingCheck(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekiNo, systemTime, processData.getItemList(), processData.getHiddenDataMap());

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
        processData.setUserAuthParam(GXHDO101B049Const.USER_AUTH_DELETE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_SAKUJO, systemTime);

            // テーピングチェック_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, jissekiNo);
            insertDeleteDataTmpSrTapingCheck(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekiNo, systemTime);

            // テーピングチェック_削除処理
            deleteSrTapingCheck(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekiNo);

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
     * 検査開始日・検査開始時間設定処理(検査開始ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKensaStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B049Const.KENSA_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B049Const.KENSA_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 検査終了日・検査終了時間設定処理(検査終了ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKensaEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B049Const.KENSA_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B049Const.KENSA_SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     *TP取得
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTpngShutoku(ProcessData processData) {

        try {
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
            
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            String edaban = lotNo.substring(11, 14);
            
            List<ErrorMessageInfo> erroMessageInfoList = new ArrayList<>();
            
            //TPNG1より判定を取得
            List<Map<String,Object>> srTpng1Data = loadSrTpng1Data(queryRunnerQcdb, kojyo, lotNo8, edaban);
            String tpng1Hantei = "";
            if(srTpng1Data.isEmpty()){
               erroMessageInfoList.add(MessageUtil.getErrorMessageInfo("","TPNG1を取得できませんでした", false, false, new ArrayList<>())); 
            }else{
                tpng1Hantei =  StringUtil.nullToBlank(getMapData(srTpng1Data.get(0), "hantei"));
            }
           
            //TPNG2より判定を取得
            List<Map<String,Object>> srTpng2Data = loadSrTpng2Data(queryRunnerQcdb, kojyo, lotNo8, edaban);
            String tpng2Hantei = "";
            if(srTpng1Data.isEmpty()){
               erroMessageInfoList.add(MessageUtil.getErrorMessageInfo("","TPNG2を取得できませんでした", false, false, new ArrayList<>())); 
            }else{
                tpng2Hantei =  StringUtil.nullToBlank(getMapData(srTpng2Data.get(0), "hantei"));
            }
           
            //エラーがあればエラーメッセージをセット(通常ならばリターンを行うが、処理は継続な為、メッセージ表示処理を行う。)
            if (!erroMessageInfoList.isEmpty()) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                for (ErrorMessageInfo errorMessageInfo : erroMessageInfoList) {
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessageInfo.getErrorMessage(), null));
                }
            }

            setItemData(processData, GXHDO101B049Const.TPNG1, getHanteiOkNgText(tpng1Hantei));
            setItemData(processData, GXHDO101B049Const.TPNG2, getHanteiOkNgText(tpng2Hantei));

            processData.setMethod("");
            
            return processData;
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
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
                        GXHDO101B049Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B049Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B049Const.BTN_UPDATE_TOP,
                        GXHDO101B049Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B049Const.BTN_DELETE_TOP,
                        GXHDO101B049Const.BTN_DELETE_BOTTOM,
                        GXHDO101B049Const.BTN_KENSA_START_TOP,
                        GXHDO101B049Const.BTN_KENSA_START_BOTTOM,
                        GXHDO101B049Const.BTN_KENSA_END_TOP,
                        GXHDO101B049Const.BTN_KENSA_END_BOTTOM,
                        GXHDO101B049Const.BTN_TPNG_SHUTOKU_TOP,
                        GXHDO101B049Const.BTN_TPNG_SHUTOKU_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B049Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B049Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B049Const.BTN_INSERT_BOTTOM,
                        GXHDO101B049Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B049Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B049Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B049Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B049Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B049Const.BTN_INSERT_TOP,
                        GXHDO101B049Const.BTN_INSERT_BOTTOM,
                        GXHDO101B049Const.BTN_KENSA_START_TOP,
                        GXHDO101B049Const.BTN_KENSA_START_BOTTOM,
                        GXHDO101B049Const.BTN_KENSA_END_TOP,
                        GXHDO101B049Const.BTN_KENSA_END_BOTTOM,
                        GXHDO101B049Const.BTN_TPNG_SHUTOKU_TOP,
                        GXHDO101B049Const.BTN_TPNG_SHUTOKU_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B049Const.BTN_DELETE_TOP,
                        GXHDO101B049Const.BTN_DELETE_BOTTOM,
                        GXHDO101B049Const.BTN_UPDATE_TOP,
                        GXHDO101B049Const.BTN_UPDATE_BOTTOM
                ));

                break;

        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);
        return processData;
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
        int jissekiNo = (Integer) session.getAttribute("jissekino");

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
        Map hiddenMap = processData.getHiddenDataMap();
        hiddenMap.put("lotkubuncode", lotkubuncode);
        hiddenMap.put("ownercode", ownercode);

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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, jissekiNo, formId)) {
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

        // ロットNo
        this.setItemData(processData, GXHDO101B049Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B049Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B049Const.TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B049Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B049Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B049Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B049Const.OWNER, ownercode + ":" + owner);
        }

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param jissekino 実績No
     * @param formId 画面ID
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, int jissekino, String formId) throws SQLException {

        List<SrTapingCheck> srTapingCheckDataList = new ArrayList<>();
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

                // 前工程情報の取得
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) externalContext.getSession(false);
                Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");

                // 処理セット数(前工程情報がある場合は前工程情報の値をセットする。)
                //良品TPﾘｰﾙ巻数①
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU1), maekoteiInfo, "ryouhintopreelmaki1", true, true);
                //良品TPﾘｰﾙ本数①
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.RYOHIN_TP_REEL_HONSU1), maekoteiInfo, "ryouhintopreelhonsu1", true, true);
                //良品TPﾘｰﾙ巻数②
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU2), maekoteiInfo, "ryouhintopreelmaki2", true, true);
                //良品TPﾘｰﾙ本数②
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.RYOHIN_TP_REEL_HONSU2), maekoteiInfo, "ryouhintopreelhonsu2", true, true);
                //検査回数
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.KENSA_KAISUU), maekoteiInfo, "kaisuu", true, true);
                //ﾃｰﾋﾟﾝｸﾞ号機
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.TP_GOKI), maekoteiInfo, "gouki", true, false);
                //検査場所
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.KENSA_BASHO), maekoteiInfo, "kensabasyo", true, false);
                //ﾘｰﾙﾁｪｯｸ数
                CommonUtil.setMaekoteiInfo(getItemRow(processData.getItemList(), GXHDO101B049Const.REEL_CHECKSU), maekoteiInfo, "ryouhinreelsu2", true, true);
                return true;
            }

            // テーピングチェックデータ取得
            srTapingCheckDataList = getSrTapingCheckData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srTapingCheckDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTapingCheckDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTapingCheckDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTapingCheckData テーピングチェックデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTapingCheck srTapingCheckData) {

        //良品TPﾘｰﾙ巻数①
        this.setItemData(processData, GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU1, getSrTapingCheckItemData(GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU1, srTapingCheckData));
        //良品TPﾘｰﾙ本数①
        this.setItemData(processData, GXHDO101B049Const.RYOHIN_TP_REEL_HONSU1, getSrTapingCheckItemData(GXHDO101B049Const.RYOHIN_TP_REEL_HONSU1, srTapingCheckData));
        //良品TPﾘｰﾙ巻数②
        this.setItemData(processData, GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU2, getSrTapingCheckItemData(GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU2, srTapingCheckData));
        //良品TPﾘｰﾙ本数②
        this.setItemData(processData, GXHDO101B049Const.RYOHIN_TP_REEL_HONSU2, getSrTapingCheckItemData(GXHDO101B049Const.RYOHIN_TP_REEL_HONSU2, srTapingCheckData));
        //検査回数
        this.setItemData(processData, GXHDO101B049Const.KENSA_KAISUU, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_KAISUU, srTapingCheckData));
        //ﾃｰﾋﾟﾝｸﾞ号機
        this.setItemData(processData, GXHDO101B049Const.TP_GOKI, getSrTapingCheckItemData(GXHDO101B049Const.TP_GOKI, srTapingCheckData));
        //検査場所
        this.setItemData(processData, GXHDO101B049Const.KENSA_BASHO, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_BASHO, srTapingCheckData));
        //ﾘｰﾙﾁｪｯｸ数
        this.setItemData(processData, GXHDO101B049Const.REEL_CHECKSU, getSrTapingCheckItemData(GXHDO101B049Const.REEL_CHECKSU, srTapingCheckData));
        //検査開始日
        this.setItemData(processData, GXHDO101B049Const.KENSA_KAISHI_DAY, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_KAISHI_DAY, srTapingCheckData));
        //検査開始時間
        this.setItemData(processData, GXHDO101B049Const.KENSA_KAISHI_TIME, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_KAISHI_TIME, srTapingCheckData));
        //検査開始担当者
        this.setItemData(processData, GXHDO101B049Const.KENSA_KAISHI_TANTOUSYA, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_KAISHI_TANTOUSYA, srTapingCheckData));
        //物立ち
        this.setItemData(processData, GXHDO101B049Const.MONODACHI, getSrTapingCheckItemData(GXHDO101B049Const.MONODACHI, srTapingCheckData));
        //剥離
        this.setItemData(processData, GXHDO101B049Const.HAKURI, getSrTapingCheckItemData(GXHDO101B049Const.HAKURI, srTapingCheckData));
        //歯抜け
        this.setItemData(processData, GXHDO101B049Const.HANUKE, getSrTapingCheckItemData(GXHDO101B049Const.HANUKE, srTapingCheckData));
        //破れ
        this.setItemData(processData, GXHDO101B049Const.YABURE, getSrTapingCheckItemData(GXHDO101B049Const.YABURE, srTapingCheckData));
        //ｶｹNG
        this.setItemData(processData, GXHDO101B049Const.KAKE_NG, getSrTapingCheckItemData(GXHDO101B049Const.KAKE_NG, srTapingCheckData));
        //DIP不良
        this.setItemData(processData, GXHDO101B049Const.DIP_FURYO, getSrTapingCheckItemData(GXHDO101B049Const.DIP_FURYO, srTapingCheckData));
        //その他
        this.setItemData(processData, GXHDO101B049Const.SONOTA, getSrTapingCheckItemData(GXHDO101B049Const.SONOTA, srTapingCheckData));
        //ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
        this.setItemData(processData, GXHDO101B049Const.TOP_CARRIER_BOTTOM_TAPE_IJOU, getSrTapingCheckItemData(GXHDO101B049Const.TOP_CARRIER_BOTTOM_TAPE_IJOU, srTapingCheckData));
        //ﾘｰﾙﾁｪｯｸ結果
        this.setItemData(processData, GXHDO101B049Const.REEL_CHECK_KEKKA, getSrTapingCheckItemData(GXHDO101B049Const.REEL_CHECK_KEKKA, srTapingCheckData));
        //検査終了日
        this.setItemData(processData, GXHDO101B049Const.KENSA_SHURYOU_DAY, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_SHURYOU_DAY, srTapingCheckData));
        //検査終了時間
        this.setItemData(processData, GXHDO101B049Const.KENSA_SHURYOU_TIME, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_SHURYOU_TIME, srTapingCheckData));
        //検査終了担当者
        this.setItemData(processData, GXHDO101B049Const.KENSA_SHURYOU_TANTOUSYA, getSrTapingCheckItemData(GXHDO101B049Const.KENSA_SHURYOU_TANTOUSYA, srTapingCheckData));
        //TPNG1
        this.setItemData(processData, GXHDO101B049Const.TPNG1, getSrTapingCheckItemData(GXHDO101B049Const.TPNG1, srTapingCheckData));
        //TPNG2
        this.setItemData(processData, GXHDO101B049Const.TPNG2, getSrTapingCheckItemData(GXHDO101B049Const.TPNG2, srTapingCheckData));
        //電気特性再検査
        this.setItemData(processData, GXHDO101B049Const.DENKITOKUSEI_SAIKENSA, getSrTapingCheckItemData(GXHDO101B049Const.DENKITOKUSEI_SAIKENSA, srTapingCheckData));
        //外観再検査
        this.setItemData(processData, GXHDO101B049Const.GAIKAN_SAIKENSA, getSrTapingCheckItemData(GXHDO101B049Const.GAIKAN_SAIKENSA, srTapingCheckData));
        //備考1
        this.setItemData(processData, GXHDO101B049Const.BIKOU1, getSrTapingCheckItemData(GXHDO101B049Const.BIKOU1, srTapingCheckData));
        //備考2
        this.setItemData(processData, GXHDO101B049Const.BIKOU2, getSrTapingCheckItemData(GXHDO101B049Const.BIKOU2, srTapingCheckData));

    }

    /**
     * テーピングチェックの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return テーピングチェック登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrTapingCheck> getSrTapingCheckData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTapingCheck(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrTapingCheck(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
        String sql = "SELECT kcpno, oyalotedaban, lotkubuncode, ownercode, tokuisaki, tanijuryo"
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
     * @param formId 画面ID(検索キー)
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
                + "AND edaban = ? AND jissekino= ? AND gamen_id = ? ";

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
     * [テーピングチェック]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTapingCheck> loadSrTapingCheck(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,ownercode,ryouhintopreelmaki1,ryouhintopreelhonsu1,ryouhintopreelmaki2,ryouhintopreelhonsu2,"
                + "tapinggouki,kensabasyo,reelchecksu,kensakaisinichiji,kensakaisitantou,monotati,hakuri,hanuke,rabure,kakeng,dipfuryo,"
                + "sonota,tapeijyo,reelcheckkekka,kensasyuryonichiji,kensasyuryotantou,tapeng1,tapeng2,denkitokuseisaikensa,gaikansaikensa,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kaisuu", "kaisuu"); //検査回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("ownercode", "ownercode"); //オーナー
        mapping.put("ryouhintopreelmaki1", "ryouhintopreelmaki1"); //良品TPﾘｰﾙ巻数①
        mapping.put("ryouhintopreelhonsu1", "ryouhintopreelhonsu1"); //良品TPﾘｰﾙ本数①
        mapping.put("ryouhintopreelmaki2", "ryouhintopreelmaki2"); //良品TPﾘｰﾙ巻数②
        mapping.put("ryouhintopreelhonsu2", "ryouhintopreelhonsu2"); //良品TPﾘｰﾙ本数②
        mapping.put("tapinggouki", "tapinggouki"); //ﾃｰﾋﾟﾝｸﾞ号機
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("reelchecksu", "reelchecksu"); //ﾘｰﾙﾁｪｯｸ数
        mapping.put("kensakaisinichiji", "kensakaisinichiji"); //検査開始日時
        mapping.put("kensakaisitantou", "kensakaisitantou"); //検査開始担当者
        mapping.put("monotati", "monotati"); //物立ち
        mapping.put("hakuri", "hakuri"); //剥離
        mapping.put("hanuke", "hanuke"); //歯抜け
        mapping.put("rabure", "rabure"); //破れ
        mapping.put("kakeng", "kakeng"); //ｶｹNG
        mapping.put("dipfuryo", "dipfuryo"); //DIP不良
        mapping.put("sonota", "sonota"); //その他
        mapping.put("tapeijyo", "tapeijyo"); //ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
        mapping.put("reelcheckkekka", "reelcheckkekka"); //ﾘｰﾙﾁｪｯｸ結果
        mapping.put("kensasyuryonichiji", "kensasyuryonichiji"); //検査終了日時
        mapping.put("kensasyuryotantou", "kensasyuryotantou"); //検査終了担当者
        mapping.put("tapeng1", "tapeng1"); //TPNG１
        mapping.put("tapeng2", "tapeng2"); //TPNG2
        mapping.put("denkitokuseisaikensa", "denkitokuseisaikensa"); //電気特性再検査
        mapping.put("gaikansaikensa", "gaikansaikensa"); //外観再検査
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTapingCheck>> beanHandler = new BeanListHandler<>(SrTapingCheck.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [テーピングチェック_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTapingCheck> loadTmpSrTapingCheck(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        String sql = "SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,ownercode,ryouhintopreelmaki1,ryouhintopreelhonsu1,ryouhintopreelmaki2,ryouhintopreelhonsu2,"
                + "tapinggouki,kensabasyo,reelchecksu,kensakaisinichiji,kensakaisitantou,monotati,hakuri,hanuke,rabure,kakeng,dipfuryo,"
                + "sonota,tapeijyo,reelcheckkekka,kensasyuryonichiji,kensasyuryotantou,tapeng1,tapeng2,denkitokuseisaikensa,gaikansaikensa,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND deleteflag = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(0);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kaisuu", "kaisuu"); //検査回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("ownercode", "ownercode"); //オーナー
        mapping.put("ryouhintopreelmaki1", "ryouhintopreelmaki1"); //良品TPﾘｰﾙ巻数①
        mapping.put("ryouhintopreelhonsu1", "ryouhintopreelhonsu1"); //良品TPﾘｰﾙ本数①
        mapping.put("ryouhintopreelmaki2", "ryouhintopreelmaki2"); //良品TPﾘｰﾙ巻数②
        mapping.put("ryouhintopreelhonsu2", "ryouhintopreelhonsu2"); //良品TPﾘｰﾙ本数②
        mapping.put("tapinggouki", "tapinggouki"); //ﾃｰﾋﾟﾝｸﾞ号機
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("reelchecksu", "reelchecksu"); //ﾘｰﾙﾁｪｯｸ数
        mapping.put("kensakaisinichiji", "kensakaisinichiji"); //検査開始日時
        mapping.put("kensakaisitantou", "kensakaisitantou"); //検査開始担当者
        mapping.put("monotati", "monotati"); //物立ち
        mapping.put("hakuri", "hakuri"); //剥離
        mapping.put("hanuke", "hanuke"); //歯抜け
        mapping.put("rabure", "rabure"); //破れ
        mapping.put("kakeng", "kakeng"); //ｶｹNG
        mapping.put("dipfuryo", "dipfuryo"); //DIP不良
        mapping.put("sonota", "sonota"); //その他
        mapping.put("tapeijyo", "tapeijyo"); //ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
        mapping.put("reelcheckkekka", "reelcheckkekka"); //ﾘｰﾙﾁｪｯｸ結果
        mapping.put("kensasyuryonichiji", "kensasyuryonichiji"); //検査終了日時
        mapping.put("kensasyuryotantou", "kensasyuryotantou"); //検査終了担当者
        mapping.put("tapeng1", "tapeng1"); //TPNG１
        mapping.put("tapeng2", "tapeng2"); //TPNG2
        mapping.put("denkitokuseisaikensa", "denkitokuseisaikensa"); //電気特性再検査
        mapping.put("gaikansaikensa", "gaikansaikensa"); //外観再検査
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTapingCheck>> beanHandler = new BeanListHandler<>(SrTapingCheck.class, rowProcessor);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, jissekiNo, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // テーピングチェックデータ取得
            List<SrTapingCheck> srTapingCheckDataList = getSrTapingCheckData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekiNo);
            if (srTapingCheckDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTapingCheckDataList.get(0));

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
     * @param srTapingCheckData テーピングチェック
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTapingCheck srTapingCheckData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTapingCheckData != null) {
            // 元データが存在する場合元データより取得
            return getSrTapingCheckItemData(itemId, srTapingCheckData);
        } else {
            return null;
        }
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
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
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
     * テーピングチェック_仮登録(tmp_sr_taping_check)登録処理
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
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, Map hiddenDataMap) throws SQLException {

        String sql = "INSERT INTO tmp_sr_taping_check ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,ownercode,ryouhintopreelmaki1,ryouhintopreelhonsu1,ryouhintopreelmaki2,ryouhintopreelhonsu2,"
                + "tapinggouki,kensabasyo,reelchecksu,kensakaisinichiji,kensakaisitantou,monotati,hakuri,hanuke,rabure,kakeng,dipfuryo,"
                + "sonota,tapeijyo,reelcheckkekka,kensasyuryonichiji,kensasyuryotantou,tapeng1,tapeng2,denkitokuseisaikensa,gaikansaikensa,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrTapingCheck(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, itemList, null, hiddenDataMap);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());

    }

    /**
     * テーピングチェック_仮登録(tmp_sr_taping_check)更新処理
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
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, Map hiddenDataMap) throws SQLException {

        String sql = "UPDATE tmp_sr_taping_check SET "
                + "kcpno = ?,ownercode = ?,ryouhintopreelmaki1 = ?,ryouhintopreelhonsu1 = ?,ryouhintopreelmaki2 = ?,ryouhintopreelhonsu2 = ?,tapinggouki = ?,"
                + "kensabasyo = ?,reelchecksu = ?,kensakaisinichiji = ?,kensakaisitantou = ?,monotati = ?,hakuri = ?,hanuke = ?,rabure = ?,kakeng = ?,"
                + "dipfuryo = ?,sonota = ?,tapeijyo = ?,reelcheckkekka = ?,kensasyuryonichiji = ?,kensasyuryotantou = ?,tapeng1 = ?,tapeng2 = ?,"
                + "denkitokuseisaikensa = ?,gaikansaikensa = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTapingCheck> srTapingCheckList = getSrTapingCheckData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrTapingCheck srTapingCheck = null;
        if (!srTapingCheckList.isEmpty()) {
            srTapingCheck = srTapingCheckList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTapingCheck(false, newRev, 0, "", "", "", jissekino, systemTime, itemList, srTapingCheck, hiddenDataMap);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピングチェック_仮登録(tmp_sr_taping_check)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピングチェック_仮登録(tmp_sr_taping_check)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTapingCheckData テーピングチェックデータ
     * @param hiddenDataMap 保持データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTapingCheck(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrTapingCheck srTapingCheckData, Map hiddenDataMap) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KCPNO, srTapingCheckData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU1, srTapingCheckData))); //良品TPﾘｰﾙ巻数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_HONSU1, srTapingCheckData))); //良品TPﾘｰﾙ本数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU2, srTapingCheckData))); //良品TPﾘｰﾙ巻数②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_HONSU2, srTapingCheckData))); //良品TPﾘｰﾙ本数②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.TP_GOKI, srTapingCheckData))); //ﾃｰﾋﾟﾝｸﾞ号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KENSA_BASHO, srTapingCheckData))); //検査場所
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.REEL_CHECKSU, srTapingCheckData))); //ﾘｰﾙﾁｪｯｸ数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KENSA_KAISHI_DAY, srTapingCheckData),
                getItemData(itemList, GXHDO101B049Const.KENSA_KAISHI_TIME, srTapingCheckData))); //検査開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KENSA_KAISHI_TANTOUSYA, srTapingCheckData))); //検査開始担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.MONODACHI, srTapingCheckData))); //物立ち
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.HAKURI, srTapingCheckData))); //剥離
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.HANUKE, srTapingCheckData))); //歯抜け
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.YABURE, srTapingCheckData))); //破れ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KAKE_NG, srTapingCheckData))); //ｶｹNG
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.DIP_FURYO, srTapingCheckData))); //DIP不良
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.SONOTA, srTapingCheckData))); //その他
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.TOP_CARRIER_BOTTOM_TAPE_IJOU, srTapingCheckData))); //ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.REEL_CHECK_KEKKA, srTapingCheckData))); //ﾘｰﾙﾁｪｯｸ結果 //TODO
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KENSA_SHURYOU_DAY, srTapingCheckData),
                getItemData(itemList, GXHDO101B049Const.KENSA_SHURYOU_TIME, srTapingCheckData))); //検査終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.KENSA_SHURYOU_TANTOUSYA, srTapingCheckData))); //検査終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.TPNG1, srTapingCheckData)));//TPNG1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.TPNG2, srTapingCheckData)));//TPNG2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.DENKITOKUSEI_SAIKENSA, srTapingCheckData)));//電気特性再検査
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.GAIKAN_SAIKENSA, srTapingCheckData)));//外観再検査
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.BIKOU1, srTapingCheckData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B049Const.BIKOU2, srTapingCheckData))); //備考2

        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * テーピングチェック(sr_taping_check)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrTapingCheck 仮登録データ
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrTapingCheck tmpSrTapingCheck, Map hiddenDataMap) throws SQLException {

        String sql = "INSERT INTO sr_taping_check ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,ownercode,ryouhintopreelmaki1,ryouhintopreelhonsu1,ryouhintopreelmaki2,ryouhintopreelhonsu2,"
                + "tapinggouki,kensabasyo,reelchecksu,kensakaisinichiji,kensakaisitantou,monotati,hakuri,hanuke,rabure,kakeng,dipfuryo,"
                + "sonota,tapeijyo,reelcheckkekka,kensasyuryonichiji,kensasyuryotantou,tapeng1,tapeng2,denkitokuseisaikensa,gaikansaikensa,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrTapingCheck(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrTapingCheck, hiddenDataMap);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピングチェック(sr_taping_check)更新処理
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
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void updateSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, Map hiddenDataMap) throws SQLException {
        String sql = "UPDATE sr_taping_check SET "
                + "kcpno = ?,ownercode = ?,ryouhintopreelmaki1 = ?,ryouhintopreelhonsu1 = ?,ryouhintopreelmaki2 = ?,ryouhintopreelhonsu2 = ?,tapinggouki = ?,"
                + "kensabasyo = ?,reelchecksu = ?,kensakaisinichiji = ?,kensakaisitantou = ?,monotati = ?,hakuri = ?,hanuke = ?,rabure = ?,kakeng = ?,"
                + "dipfuryo = ?,sonota = ?,tapeijyo = ?,reelcheckkekka = ?,kensasyuryonichiji = ?,kensasyuryotantou = ?,tapeng1 = ?,tapeng2 = ?,"
                + "denkitokuseisaikensa = ?,gaikansaikensa = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTapingCheck> srTapingCheckList = getSrTapingCheckData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrTapingCheck srTapingCheck = null;
        if (!srTapingCheckList.isEmpty()) {
            srTapingCheck = srTapingCheckList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTapingCheck(false, newRev, "", "", "", jissekino, systemTime, itemList, srTapingCheck, hiddenDataMap);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピングチェック(sr_taping_check)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTapingCheckData テーピングチェックデータ
     * @param hiddenDataMap 保持データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTapingCheck(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrTapingCheck srTapingCheckData, Map hiddenDataMap) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //検査回数
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.KCPNO, srTapingCheckData))); //KCPNO
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(hiddenDataMap.get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU1, srTapingCheckData))); //良品TPﾘｰﾙ巻数①
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_HONSU1, srTapingCheckData))); //良品TPﾘｰﾙ本数①
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU2, srTapingCheckData))); //良品TPﾘｰﾙ巻数②
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.RYOHIN_TP_REEL_HONSU2, srTapingCheckData))); //良品TPﾘｰﾙ本数②
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.TP_GOKI, srTapingCheckData))); //ﾃｰﾋﾟﾝｸﾞ号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.KENSA_BASHO, srTapingCheckData))); //検査場所
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.REEL_CHECKSU, srTapingCheckData))); //ﾘｰﾙﾁｪｯｸ数
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B049Const.KENSA_KAISHI_DAY, srTapingCheckData),
                getItemData(itemList, GXHDO101B049Const.KENSA_KAISHI_TIME, srTapingCheckData))); //検査開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.KENSA_KAISHI_TANTOUSYA, srTapingCheckData))); //検査開始担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.MONODACHI, srTapingCheckData))); //物立ち
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.HAKURI, srTapingCheckData))); //剥離
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.HANUKE, srTapingCheckData))); //歯抜け
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.YABURE, srTapingCheckData))); //破れ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.KAKE_NG, srTapingCheckData))); //ｶｹNG
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.DIP_FURYO, srTapingCheckData))); //DIP不良
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.SONOTA, srTapingCheckData))); //その他
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B049Const.TOP_CARRIER_BOTTOM_TAPE_IJOU, srTapingCheckData))); //ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.REEL_CHECK_KEKKA, srTapingCheckData))); //ﾘｰﾙﾁｪｯｸ結果
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B049Const.KENSA_SHURYOU_DAY, srTapingCheckData),
                getItemData(itemList, GXHDO101B049Const.KENSA_SHURYOU_TIME, srTapingCheckData))); //検査終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.KENSA_SHURYOU_TANTOUSYA, srTapingCheckData))); //検査終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.TPNG1, srTapingCheckData)));//TPNG1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.TPNG2, srTapingCheckData)));//TPNG2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.DENKITOKUSEI_SAIKENSA, srTapingCheckData)));//電気特性再検査
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.GAIKAN_SAIKENSA, srTapingCheckData)));//外観再検査
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.BIKOU1, srTapingCheckData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B049Const.BIKOU2, srTapingCheckData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision

        return params;
    }

    /**
     * テーピングチェック(sr_taping_check)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [テーピングチェック_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

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
     * @param srTapingCheckData テーピングチェックデータ
     * @return DB値
     */
    private String getSrTapingCheckItemData(String itemId, SrTapingCheck srTapingCheckData) {
        switch (itemId) {
            //KCPNO
            case GXHDO101B049Const.KCPNO:
                return StringUtil.nullToBlank(srTapingCheckData.getKcpno());
            //ｵｰﾅｰ
            case GXHDO101B049Const.OWNER:
                return StringUtil.nullToBlank(srTapingCheckData.getOwnercode());
            //良品TPﾘｰﾙ巻数①
            case GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU1:
                return StringUtil.nullToBlank(srTapingCheckData.getRyouhintopreelmaki1());
            //良品TPﾘｰﾙ本数①
            case GXHDO101B049Const.RYOHIN_TP_REEL_HONSU1:
                return StringUtil.nullToBlank(srTapingCheckData.getRyouhintopreelhonsu1());
            //良品TPﾘｰﾙ巻数②
            case GXHDO101B049Const.RYOHIN_TP_REEL_MAKISU2:
                return StringUtil.nullToBlank(srTapingCheckData.getRyouhintopreelmaki2());
            //良品TPﾘｰﾙ本数②
            case GXHDO101B049Const.RYOHIN_TP_REEL_HONSU2:
                return StringUtil.nullToBlank(srTapingCheckData.getRyouhintopreelhonsu2());
            //検査回数
            case GXHDO101B049Const.KENSA_KAISUU:
                return StringUtil.nullToBlank(srTapingCheckData.getKaisuu());
            //ﾃｰﾋﾟﾝｸﾞ号機
            case GXHDO101B049Const.TP_GOKI:
                return StringUtil.nullToBlank(srTapingCheckData.getTapinggouki());
            //検査場所
            case GXHDO101B049Const.KENSA_BASHO:
                return StringUtil.nullToBlank(srTapingCheckData.getKensabasyo());
            //ﾘｰﾙﾁｪｯｸ数
            case GXHDO101B049Const.REEL_CHECKSU:
                return StringUtil.nullToBlank(srTapingCheckData.getReelchecksu());
            //検査開始日
            case GXHDO101B049Const.KENSA_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srTapingCheckData.getKensakaisinichiji(), "yyMMdd");
            //検査開始時間
            case GXHDO101B049Const.KENSA_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srTapingCheckData.getKensakaisinichiji(), "HHmm");
            //検査開始担当者
            case GXHDO101B049Const.KENSA_KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srTapingCheckData.getKensakaisitantou());
            //物立ち
            case GXHDO101B049Const.MONODACHI:
                return StringUtil.nullToBlank(srTapingCheckData.getMonotati());
            //剥離
            case GXHDO101B049Const.HAKURI:
                return StringUtil.nullToBlank(srTapingCheckData.getHakuri());
            //歯抜け
            case GXHDO101B049Const.HANUKE:
                return StringUtil.nullToBlank(srTapingCheckData.getHanuke());
            //破れ
            case GXHDO101B049Const.YABURE:
                return StringUtil.nullToBlank(srTapingCheckData.getRabure());//TODO
            //ｶｹNG
            case GXHDO101B049Const.KAKE_NG:
                return StringUtil.nullToBlank(srTapingCheckData.getKakeng());
            //DIP不良
            case GXHDO101B049Const.DIP_FURYO:
                return StringUtil.nullToBlank(srTapingCheckData.getDipfuryo());
            //その他
            case GXHDO101B049Const.SONOTA:
                return StringUtil.nullToBlank(srTapingCheckData.getSonota());
            //ﾄｯﾌﾟﾃｰﾌﾟ、ｷｬﾘｱﾃｰﾌﾟ、ﾎﾞﾄﾑﾃｰﾌﾟ異常
            case GXHDO101B049Const.TOP_CARRIER_BOTTOM_TAPE_IJOU:
                return StringUtil.nullToBlank(srTapingCheckData.getTapeijyo());
            //ﾘｰﾙﾁｪｯｸ結果
            case GXHDO101B049Const.REEL_CHECK_KEKKA:
                return StringUtil.nullToBlank(srTapingCheckData.getReelcheckkekka());
            //検査終了日
            case GXHDO101B049Const.KENSA_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srTapingCheckData.getKensasyuryonichiji(), "yyMMdd");
            //検査終了時間
            case GXHDO101B049Const.KENSA_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srTapingCheckData.getKensasyuryonichiji(), "HHmm");
            //検査終了担当者
            case GXHDO101B049Const.KENSA_SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srTapingCheckData.getKensasyuryotantou());
            //TPNG1
            case GXHDO101B049Const.TPNG1:
                return StringUtil.nullToBlank(srTapingCheckData.getTapeng1());
            //TPNG2
            case GXHDO101B049Const.TPNG2:
                return StringUtil.nullToBlank(srTapingCheckData.getTapeng2());
            //電気特性再検査
            case GXHDO101B049Const.DENKITOKUSEI_SAIKENSA:
                return StringUtil.nullToBlank(srTapingCheckData.getDenkitokuseisaikensa());
            //外観再検査
            case GXHDO101B049Const.GAIKAN_SAIKENSA:
                return StringUtil.nullToBlank(srTapingCheckData.getGaikansaikensa());
            //備考1
            case GXHDO101B049Const.BIKOU1:
                return StringUtil.nullToBlank(srTapingCheckData.getBikou1());
            //備考2
            case GXHDO101B049Const.BIKOU2:
                return StringUtil.nullToBlank(srTapingCheckData.getBikou2());

            default:
                return null;

        }
    }

    /**
     * テーピングチェック_仮登録(tmp_sr_taping_check)登録処理(削除時)
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
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrTapingCheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_taping_check ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,ownercode,ryouhintopreelmaki1,ryouhintopreelhonsu1,ryouhintopreelmaki2,ryouhintopreelhonsu2,"
                + "tapinggouki,kensabasyo,reelchecksu,kensakaisinichiji,kensakaisitantou,monotati,hakuri,hanuke,rabure,kakeng,dipfuryo,"
                + "sonota,tapeijyo,reelcheckkekka,kensasyuryonichiji,kensasyuryotantou,tapeng1,tapeng2,denkitokuseisaikensa,gaikansaikensa,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,ownercode,ryouhintopreelmaki1,ryouhintopreelhonsu1,ryouhintopreelmaki2,ryouhintopreelhonsu2,"
                + "tapinggouki,kensabasyo,reelchecksu,kensakaisinichiji,kensakaisitantou,monotati,hakuri,hanuke,rabure,kakeng,dipfuryo,"
                + "sonota,tapeijyo,reelcheckkekka,kensasyuryonichiji,kensasyuryotantou,tapeng1,tapeng2,denkitokuseisaikensa,gaikansaikensa,"
                + "bikou1,bikou2,?,?,?,? "
                + "FROM sr_taping_check "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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
        params.add(jissekino); //検査回数

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * コンボボックスNG選択チェック
     *
     * @param itemData 項目データデータ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkComboBoxSelectNG(FXHDD01 itemData) {
        if (itemData == null) {
            return null;
        }

        if ("NG".equals(itemData.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemData);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemData.getLabel1());
        }
        return null;
    }
//
//    /**
//     * コンボボックス(OK,NG)Value値取得
//     *
//     * @param comboText コンボボックステキスト
//     * @return コンボボックスValue値
//     */
//    private Integer getComboOkNgValue(String comboText, Integer defaultValue) {
//        switch (StringUtil.nullToBlank(comboText)) {
//            case "NG":
//                return 0;
//            case "OK":
//                return 1;
//            default:
//                return defaultValue;
//        }
//    }
//
//    /**
//     * コンボボックス(OK,NG)テキスト値取得
//     *
//     * @param comboValue コンボボックスValue値
//     * @return コンボボックステキスト値
//     */
//    private String getComboOkNgText(String comboValue) {
//        switch (comboValue) {
//            case "0":
//                return "NG";
//            case "1":
//                return "OK";
//            default:
//                return "";
//        }
//    }
//
//    /**
//     * コンボボックス(あり,なし)Value値取得
//     *
//     * @param comboText コンボボックステキスト
//     * @return コンボボックスValue値
//     */
//    private Integer getComboAriNashiValue(String comboText, Integer defaultValue) {
//        switch (StringUtil.nullToBlank(comboText)) {
//            case "なし":
//                return 0;
//            case "あり":
//                return 1;
//            default:
//                return defaultValue;
//        }
//    }
//
//    /**
//     * コンボボックス(あり,なし)テキスト値取得
//     *
//     * @param comboValue コンボボックスValue値
//     * @return コンボボックステキスト値
//     */
//    private String getComboAriNashiText(String comboValue) {
//        switch (comboValue) {
//            case "0":
//                return "なし";
//            case "1":
//                return "あり";
//            default:
//                return "";
//        }
//    }

    /**
     * [TPNG1]から情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List loadSrTpng1Data(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {

        // TPNG1データの取得
        String sql = "SELECT hantei "
                + "FROM sr_tpng1 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "ORDER BY jissekino desc";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }
    
    /**
     * [TPNG2]から情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List loadSrTpng2Data(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {

        // TPNG2データの取得
        String sql = "SELECT hantei "
                + "FROM sr_tpng2 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? "
                + "ORDER BY jissekino desc";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }
    
    /**
     * 判定項目(あり,なし)テキスト値取得
     *
     * @param hanteiValue 判定項目Value値
     * @return コンボボックステキスト値
     */
    private String getHanteiOkNgText(String hanteiValue) {
        switch (hanteiValue) {
            case "1":
                return "OK";
            case "2":
                return "NG";
            default:
                return "";
        }
    }
    
    
    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、データを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param userName ユーザー名
     * @param key Key
     * @return 取得データ
     */
    private String loadParamData(QueryRunner queryRunnerDoc, String userName, String key) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = ? AND key = ? ";

            List<Object> params = new ArrayList<>();
            params.add(userName);
            params.add(key);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map data = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
            if (data != null && !data.isEmpty()) {
                return StringUtil.nullToBlank(data.get("data"));
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;

    }
    
//     /**
//     * [実績]から、ﾃﾞｰﾀを取得
//     * @param queryRunnerWip オブジェクト
//     * @param lotNo ﾛｯﾄNo(検索キー)
//     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
//     * @return 取得データ
//     * @throws SQLException 
//     */
//     private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {
//         
//
//        String lotNo1 = lotNo.substring(0, 3);
//        String lotNo2 = lotNo.substring(3, 11);
//        String lotNo3 = lotNo.substring(11, 14);
//        
//        List<String> dataList= new ArrayList<>(Arrays.asList(data));
//        
//        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
//        String sql = "SELECT syorisuu "
//                + "FROM jisseki "
//                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";
//        
//        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());
//        
//        sql += " ORDER BY syoribi DESC, syorijikoku DESC";
//        
//        Map mapping = new HashMap<>();
//        mapping.put("syorisuu", "syorisuu");
//        
//        BeanProcessor beanProcessor = new BeanProcessor(mapping);
//        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
//        ResultSetHandler<List<Jisseki>> beanHandler = new BeanListHandler<>(Jisseki.class, rowProcessor);
//
//        List<Object> params = new ArrayList<>();
//        params.add(lotNo1);
//        params.add(lotNo2);
//        params.add(lotNo3);                
//        params.addAll(dataList);
//
//        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
//        return queryRunnerWip.query(sql, beanHandler, params.toArray());
//    }
//     
     
   
}
