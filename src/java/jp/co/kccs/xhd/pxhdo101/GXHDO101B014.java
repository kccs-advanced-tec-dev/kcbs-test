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
import jp.co.kccs.xhd.db.model.SrYobikan;
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
import org.apache.commons.lang.math.NumberUtils;

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
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * 変更日	2020/10/19<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	仕様変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B014・GXHDO101B015(焼成・Air脱脂|窒素脱脂)ロジック定数
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/06
 */
public class GXHDO101B014 implements IFormLogic {

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

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);

            // 項目ID情報
            GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, processData.getInitJotaiFlg(), itemIdInfo);

            //日付入力をチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    itemIdInfo.getBtnStartdatetimeTop(),
                    itemIdInfo.getBtnStartdatetimeBottom(),
                    itemIdInfo.getBtnEnddatetimeTop(),
                    itemIdInfo.getBtnEnddatetimeBottom()
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    itemIdInfo.getBtnKariTourokuTop(),
                    itemIdInfo.getBtnKariTourokuBottom(),
                    itemIdInfo.getBtnInsertTop(),
                    itemIdInfo.getBtnInsertBottom(),
                    itemIdInfo.getBtnDeleteTop(),
                    itemIdInfo.getBtnDeleteBottom(),
                    itemIdInfo.getBtnUpdateTop(),
                    itemIdInfo.getBtnUpdateBottom()));

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
        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(formId);

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 1次脱脂(Air_窒素)_仮登録登録処理
                insertTmpSrYobikan(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, formId, jissekino, systemTime, processData.getItemList(), itemIdInfo);

            } else {

                // 1次脱脂(Air_窒素)_仮登録更新処理
                updateTmpSrYobikan(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, formId, jissekino, systemTime, processData.getItemList(), itemIdInfo);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 kaishiDay = getItemRow(processData.getItemList(), itemIdInfo.getKaishiDay()); //開始日
        FXHDD01 kaishiTime = getItemRow(processData.getItemList(), itemIdInfo.getKaishiTime()); //開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(kaishiDay.getValue(), kaishiTime.getValue());
        FXHDD01 shuryouDay = getItemRow(processData.getItemList(), itemIdInfo.getShuryouDay()); //終了日
        FXHDD01 shuryouTime = getItemRow(processData.getItemList(), itemIdInfo.getShuryouTime()); //終了時刻
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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(formId);

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrYobikan tmpSrYobikan = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrYobikan> srYobikanList = getSrYobikanData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, jissekino);
                if (!srYobikanList.isEmpty()) {
                    tmpSrYobikan = srYobikanList.get(0);
                }

                deleteTmpSrYobikan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);
            }

            // 1次脱脂(Air_窒素)_登録処理
            insertSrYobikan(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, formId, jissekino, systemTime, processData.getItemList(), tmpSrYobikan, itemIdInfo);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
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
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));
        processData.setRquireAuth(true);
        processData.setUserAuthParam(itemIdInfo.getUserAuthUpdateParam());

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));
            GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(formId);

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 1次脱脂(Air_窒素)_更新処理
            updateSrYobikan(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, formId, jissekino, systemTime, processData.getItemList(), itemIdInfo);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));
        processData.setRquireAuth(true);
        processData.setUserAuthParam(itemIdInfo.getUserAuthDeleteParam());

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_SAKUJO, systemTime);

            // 1次脱脂(Air_窒素)_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, jissekino);
            insertDeleteDataTmpSrYobikan(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 1次脱脂(Air_窒素)_削除処理
            deleteSrYobikan(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("削除しました。");
            processData.setCollBackParam("complete");

            return processData;
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
     * @param itemIdInfo 項目ID情報
     * @return 処理制御データ
     */
    private ProcessData setButtonEnable(ProcessData processData, String jotaiFlg, GXHDO101B014Const itemIdInfo) {

        List<String> activeIdList = new ArrayList<>();
        List<String> inactiveIdList = new ArrayList<>();
        switch (jotaiFlg) {
            case JOTAI_FLG_TOROKUZUMI:
                activeIdList.addAll(Arrays.asList(
                        itemIdInfo.getBtnStartdatetimeTop(),
                        itemIdInfo.getBtnStartdatetimeBottom(),
                        itemIdInfo.getBtnEnddatetimeTop(),
                        itemIdInfo.getBtnEnddatetimeBottom(),
                        itemIdInfo.getBtnCopyEdabanTop(),
                        itemIdInfo.getBtnCopyEdabanBottom(),
                        itemIdInfo.getBtnUpdateTop(),
                        itemIdInfo.getBtnUpdateBottom(),
                        itemIdInfo.getBtnDeleteTop(),
                        itemIdInfo.getBtnDeleteBottom()
                ));
                inactiveIdList.addAll(Arrays.asList(
                        itemIdInfo.getBtnKariTourokuTop(),
                        itemIdInfo.getBtnKariTourokuBottom(),
                        itemIdInfo.getBtnInsertBottom(),
                        itemIdInfo.getBtnInsertTop()));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        itemIdInfo.getBtnStartdatetimeTop(),
                        itemIdInfo.getBtnStartdatetimeBottom(),
                        itemIdInfo.getBtnEnddatetimeTop(),
                        itemIdInfo.getBtnEnddatetimeBottom(),
                        itemIdInfo.getBtnCopyEdabanTop(),
                        itemIdInfo.getBtnCopyEdabanBottom(),
                        itemIdInfo.getBtnKariTourokuTop(),
                        itemIdInfo.getBtnKariTourokuBottom(),
                        itemIdInfo.getBtnInsertTop(),
                        itemIdInfo.getBtnInsertBottom()
                ));

                inactiveIdList.addAll(Arrays.asList(
                        itemIdInfo.getBtnDeleteTop(),
                        itemIdInfo.getBtnDeleteBottom(),
                        itemIdInfo.getBtnUpdateTop(),
                        itemIdInfo.getBtnUpdateBottom()
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
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));

        if (itemIdInfo.getBtnKariTourokuTop().equals(buttonId) || itemIdInfo.getBtnKariTourokuBottom().equals(buttonId)) {
            // 仮登録
            return "checkDataTempRegist";
        } else if (itemIdInfo.getBtnInsertTop().equals(buttonId) || itemIdInfo.getBtnInsertBottom().equals(buttonId)) {
            // 登録
            return "checkDataRegist";
        } else if (itemIdInfo.getBtnCopyEdabanTop().equals(buttonId) || itemIdInfo.getBtnCopyEdabanBottom().equals(buttonId)) {
            // 枝番コピー
            return "confEdabanCopy";
        } else if (itemIdInfo.getBtnUpdateTop().equals(buttonId) || itemIdInfo.getBtnUpdateBottom().equals(buttonId)) {
            // 修正
            return "checkDataCorrect";
        } else if (itemIdInfo.getBtnDeleteTop().equals(buttonId) || itemIdInfo.getBtnDeleteBottom().equals(buttonId)) {
            // 削除
            return "checkDataDelete";
        } else if (itemIdInfo.getBtnStartdatetimeTop().equals(buttonId) || itemIdInfo.getBtnStartdatetimeBottom().equals(buttonId)) {
            // 開始日時
            return "setStartDateTime";
        } else if (itemIdInfo.getBtnEnddatetimeTop().equals(buttonId) || itemIdInfo.getBtnEnddatetimeBottom().equals(buttonId)) {
            // 終了日時
            return "setEndDateTime";
        }

        return "error";
    }

    /**
     * 初期表示データ設定
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    private ProcessData setInitDate(ProcessData processData) throws SQLException, NumberFormatException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, queryRunnerWip, lotNo);
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

        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(formId);

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, jissekino, itemIdInfo)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo, itemIdInfo);

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
     * @param itemIdInfo 項目ID情報
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo, GXHDO101B014Const itemIdInfo) {

        // ロットNo
        this.setItemData(processData, itemIdInfo.getLotno(), lotNo);
        // KCPNO
        this.setItemData(processData, itemIdInfo.getKcpno(), StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, itemIdInfo.getKyakusaki(), StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, itemIdInfo.getLotKubun(), "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, itemIdInfo.getLotKubun(), lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, itemIdInfo.getOwner(), "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, itemIdInfo.getOwner(), ownercode + ":" + owner);
        }

        // 指示
        this.setItemData(processData, itemIdInfo.getSiji(), "");

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
            String lotNo, String formId, int jissekino, GXHDO101B014Const itemIdInfo) throws SQLException {

        List<SrYobikan> srYobikanDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId, jissekino);
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

                // 前工程情報取得
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) externalContext.getSession(false);
                Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");
                String maekoteiFormId = StringUtil.nullToBlank(session.getAttribute("maekoteiFormId"));

                // 受入ｾｯﾀ枚数(前工程情報がある場合は前工程情報の値をセットする。)
                FXHDD01 itemSettaMaisu = this.getItemRow(processData.getItemList(), itemIdInfo.getUkeireSettaMaisu());
                if ("GXHDO101B013".equals(maekoteiFormId)) {
                    //前工程がｾｯﾀ詰めの場合、ｾｯﾀ枚数をセット
                    CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "sayasuu", false, true);
                } else if ("GXHDO101B014".equals(maekoteiFormId) ||
                           "GXHDO101B015".equals(maekoteiFormId) ||
                           "GXHDO101B016".equals(maekoteiFormId) ||
                           "GXHDO101B017".equals(maekoteiFormId) ||
                           "GXHDO101B018".equals(maekoteiFormId) ||
                           "GXHDO101B019".equals(maekoteiFormId)) {
                    //前工程がAir脱脂、窒素脱脂、2次脱脂(ﾍﾞﾙﾄ)、焼成、RHK焼成、再酸化の場合、回収ｾｯﾀ枚数をセット
                    if ("GXHDO101B014".equals(maekoteiFormId) ||
                        "GXHDO101B015".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaisyusettersuu", false, true);
                    } else if ("GXHDO101B016".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaishuusettasuu", false, true);
                    } else if ("GXHDO101B017".equals(maekoteiFormId) ||
                               "GXHDO101B018".equals(maekoteiFormId) ||
                               "GXHDO101B019".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaishusettasuu", false, true);
                    }
                }

                FXHDD01 itemMaxOndo = this.getItemRow(processData.getItemList(), itemIdInfo.getMaxOndo());
                setKikakuChiToValue(itemMaxOndo);
                FXHDD01 itemKeepTime = this.getItemRow(processData.getItemList(), itemIdInfo.getKeepTime());
                setKikakuChiToValue(itemKeepTime);
                FXHDD01 itemTotalTime = this.getItemRow(processData.getItemList(), itemIdInfo.getTotalTime());
                setKikakuChiToValue(itemTotalTime);
                return true;
            }

            // 1次脱脂(Air_窒素)データ取得
            srYobikanDataList = getSrYobikanData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srYobikanDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srYobikanDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srYobikanDataList.get(0), itemIdInfo);

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srYobikanData 1次脱脂(Air_窒素)データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrYobikan srYobikanData, GXHDO101B014Const itemIdInfo) {

        //受入ｾｯﾀ枚数
        this.setItemData(processData, itemIdInfo.getUkeireSettaMaisu(), getSrYobikanItemData(itemIdInfo.getUkeireSettaMaisu(), srYobikanData, itemIdInfo));
        //号機
        this.setItemData(processData, itemIdInfo.getGoki(), getSrYobikanItemData(itemIdInfo.getGoki(), srYobikanData, itemIdInfo));
        //ﾌﾟﾛｸﾞﾗﾑNo．
        this.setItemData(processData, itemIdInfo.getProgramNo(), getSrYobikanItemData(itemIdInfo.getProgramNo(), srYobikanData, itemIdInfo));
        //最高温度
        this.setItemData(processData, itemIdInfo.getMaxOndo(), getSrYobikanItemData(itemIdInfo.getMaxOndo(), srYobikanData, itemIdInfo));
        //ｷｰﾌﾟ時間
        this.setItemData(processData, itemIdInfo.getKeepTime(), getSrYobikanItemData(itemIdInfo.getKeepTime(), srYobikanData, itemIdInfo));
        //総時間
        this.setItemData(processData, itemIdInfo.getTotalTime(), getSrYobikanItemData(itemIdInfo.getTotalTime(), srYobikanData, itemIdInfo));
        //投入ｾｯﾀ枚数
        this.setItemData(processData, itemIdInfo.getTounyuSettaMaisu(), getSrYobikanItemData(itemIdInfo.getTounyuSettaMaisu(), srYobikanData, itemIdInfo));
        //開始日
        this.setItemData(processData, itemIdInfo.getKaishiDay(), getSrYobikanItemData(itemIdInfo.getKaishiDay(), srYobikanData, itemIdInfo));
        //開始時間
        this.setItemData(processData, itemIdInfo.getKaishiTime(), getSrYobikanItemData(itemIdInfo.getKaishiTime(), srYobikanData, itemIdInfo));
        //開始担当者
        this.setItemData(processData, itemIdInfo.getKaishiTantousya(), getSrYobikanItemData(itemIdInfo.getKaishiTantousya(), srYobikanData, itemIdInfo));
        //開始確認者
        this.setItemData(processData, itemIdInfo.getKaishiKakuninsya(), getSrYobikanItemData(itemIdInfo.getKaishiKakuninsya(), srYobikanData, itemIdInfo));
        //終了日
        this.setItemData(processData, itemIdInfo.getShuryouDay(), getSrYobikanItemData(itemIdInfo.getShuryouDay(), srYobikanData, itemIdInfo));
        //終了時間
        this.setItemData(processData, itemIdInfo.getShuryouTime(), getSrYobikanItemData(itemIdInfo.getShuryouTime(), srYobikanData, itemIdInfo));
        //終了担当者
        this.setItemData(processData, itemIdInfo.getShuryouTantousya(), getSrYobikanItemData(itemIdInfo.getShuryouTantousya(), srYobikanData, itemIdInfo));
        //回収ｾｯﾀ枚数
        this.setItemData(processData, itemIdInfo.getKaishuSettaMaisu(), getSrYobikanItemData(itemIdInfo.getKaishuSettaMaisu(), srYobikanData, itemIdInfo));
        //備考1
        this.setItemData(processData, itemIdInfo.getBikou1(), getSrYobikanItemData(itemIdInfo.getBikou1(), srYobikanData, itemIdInfo));
        //備考2
        this.setItemData(processData, itemIdInfo.getBikou2(), getSrYobikanItemData(itemIdInfo.getBikou2(), srYobikanData, itemIdInfo));

    }

    /**
     * 1次脱脂(Air_窒素)の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 1次脱脂(Air_窒素)登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrYobikan> getSrYobikanData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrYobikan(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
        } else {
            return loadTmpSrYobikan(queryRunnerQcdb, kojyo, lotNo, edaban, rev, jissekino);
        }
    }

    /**
     * [設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        return CommonUtil.getSekkeiInfoTogoLot(queryRunnerQcdb, queryRunnerWip, lotNo1, lotNo2, "001");
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
        String sql = "SELECT kcpno, oyalotedaban, lotkubuncode, ownercode, tokuisaki"
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
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        // 品質DB登録実績の取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);

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
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);

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
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);
        Map map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            newRev = new BigDecimal(String.valueOf(map.get("rev")));
            newRev = newRev.add(BigDecimal.ONE);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return newRev;
    }

    /**
     * [1次脱脂(Air_窒素)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYobikan> loadSrYobikan(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,yobikangoki,sayasuu,yobikanjikan,"
                + "yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,biko1,biko2,biko3,biko4,biko5,torokunichiji,"
                + "kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,"
                + "dasshisyurui,revision,'0' AS deleteflag "
                + "FROM sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";
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
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("kosuu", "kosuu"); //処理数
        mapping.put("kaisinichiji", "kaisinichiji"); //開始日時
        mapping.put("syuryoyoteinichiji", "syuryoyoteinichiji"); //終了予定日時
        mapping.put("syuryonichiji", "syuryonichiji"); //終了日時
        mapping.put("yobikangoki", "yobikangoki"); //予備乾燥号機ｺｰﾄﾞ
        mapping.put("sayasuu", "sayasuu"); //ｾｯﾀ枚数
        mapping.put("yobikanjikan", "yobikanjikan"); //予備乾燥時間
        mapping.put("yobikanondo", "yobikanondo"); //予備乾燥温度
        mapping.put("peakjikan", "peakjikan"); //ﾋﾟｰｸ時間
        mapping.put("kaisitantosya", "kaisitantosya"); //開始担当者ｺｰﾄﾞ
        mapping.put("syuryotantosya", "syuryotantosya"); //終了担当者ｺｰﾄﾞ
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("biko3", "biko3"); //備考3
        mapping.put("biko4", "biko4"); //備考4
        mapping.put("biko5", "biko5"); //備考5
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("NijiKaishiNichiji", "nijikaishinichiji"); //二次開始日時
        mapping.put("NijiGoki", "nijigoki"); //二次号機ｺｰﾄﾞ
        mapping.put("programno", "programno"); //プログラムＮｏ．
        mapping.put("tounyusettersuu", "tounyusettersuu"); //投入セッタ枚数
        mapping.put("StartKakuninsyacode", "startkakuninsyacode"); //開始確認者
        mapping.put("kaisyusettersuu", "kaisyusettersuu"); //回収セッタ枚数
        mapping.put("dasshisyurui", "dasshisyurui"); //脱脂種類
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYobikan>> beanHandler = new BeanListHandler<>(SrYobikan.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [1次脱脂(Air_窒素)_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrYobikan> loadTmpSrYobikan(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev, int jissekino) throws SQLException {
        String sql = "SELECT "
                + "kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,yobikangoki,sayasuu,yobikanjikan,"
                + "yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,biko1,biko2,biko3,biko4,biko5,torokunichiji,"
                + "kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,"
                + "dasshisyurui,revision,deleteflag "
                + "FROM tmp_sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? AND jissekino = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(0);
        params.add(jissekino);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("kosuu", "kosuu"); //処理数
        mapping.put("kaisinichiji", "kaisinichiji"); //開始日時
        mapping.put("syuryoyoteinichiji", "syuryoyoteinichiji"); //終了予定日時
        mapping.put("syuryonichiji", "syuryonichiji"); //終了日時
        mapping.put("yobikangoki", "yobikangoki"); //予備乾燥号機ｺｰﾄﾞ
        mapping.put("sayasuu", "sayasuu"); //ｾｯﾀ枚数
        mapping.put("yobikanjikan", "yobikanjikan"); //予備乾燥時間
        mapping.put("yobikanondo", "yobikanondo"); //予備乾燥温度
        mapping.put("peakjikan", "peakjikan"); //ﾋﾟｰｸ時間
        mapping.put("kaisitantosya", "kaisitantosya"); //開始担当者ｺｰﾄﾞ
        mapping.put("syuryotantosya", "syuryotantosya"); //終了担当者ｺｰﾄﾞ
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("biko3", "biko3"); //備考3
        mapping.put("biko4", "biko4"); //備考4
        mapping.put("biko5", "biko5"); //備考5
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("NijiKaishiNichiji", "nijikaishinichiji"); //二次開始日時
        mapping.put("NijiGoki", "nijigoki"); //二次号機ｺｰﾄﾞ
        mapping.put("programno", "programno"); //プログラムＮｏ．
        mapping.put("tounyusettersuu", "tounyusettersuu"); //投入セッタ枚数
        mapping.put("StartKakuninsyacode", "startkakuninsyacode"); //開始確認者
        mapping.put("kaisyusettersuu", "kaisyusettersuu"); //回収セッタ枚数
        mapping.put("dasshisyurui", "dasshisyurui"); //脱脂種類
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrYobikan>> beanHandler = new BeanListHandler<>(SrYobikan.class, rowProcessor);

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, formId, jissekino);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 1次脱脂(Air_窒素)データ取得
            List<SrYobikan> srYobikanDataList = getSrYobikanData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekino);
            if (srYobikanDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(formId);
            setInputItemDataMainForm(processData, srYobikanDataList.get(0), itemIdInfo);

            // 次呼出しメソッドをクリア
            processData.setMethod("");

            return processData;
        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
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
     * @param srYobikanData 1次脱脂(Air_窒素)
     * @param itemIdInfo 項目ID情報
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrYobikan srYobikanData, GXHDO101B014Const itemIdInfo) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srYobikanData != null) {
            // 元データが存在する場合元データより取得
            return getSrYobikanItemData(itemId, srYobikanData, itemIdInfo);
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
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = ? ";

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
     * 1次脱脂(Air_窒素)_仮登録(tmp_sr_yobikan)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param itemIdInfo 項目IDリスト
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String formId, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, GXHDO101B014Const itemIdInfo) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yobikan ("
                + "kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,yobikangoki,sayasuu,yobikanjikan,"
                + "yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,biko1,biko2,biko3,biko4,biko5,torokunichiji,"
                + "kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,"
                + "dasshisyurui,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrYobikan(true, newRev, deleteflag, kojyo, lotNo, edaban, formId, jissekino, systemTime, itemList, null, itemIdInfo);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());

    }

    /**
     * 1次脱脂(Air_窒素)_仮登録(tmp_sr_yobikan)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param itemIdInfo 項目IDリスト
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String formId, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, GXHDO101B014Const itemIdInfo) throws SQLException {

        String sql = "UPDATE tmp_sr_yobikan SET "
                + "kcpno = ?,kaisinichiji = ?,syuryonichiji = ?,yobikangoki = ?,sayasuu = ?,yobikanjikan = ?,yobikanondo = ?,"
                + "peakjikan = ?,kaisitantosya = ?,syuryotantosya = ?,biko1 = ?,biko2 = ?,kosinnichiji = ?,"
                + "programno = ?,tounyusettersuu = ?,StartKakuninsyacode = ?,kaisyusettersuu = ?,dasshisyurui = ?,"
                + "revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ?";

        // 更新前の値を取得
        List<SrYobikan> srYobikanList = getSrYobikanData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrYobikan srYobikan = null;
        if (!srYobikanList.isEmpty()) {
            srYobikan = srYobikanList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrYobikan(false, newRev, 0, "", "", "", formId, jissekino, systemTime, itemList, srYobikan, itemIdInfo);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 1次脱脂(Air_窒素)_仮登録(tmp_sr_yobikan)削除処理
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
    private void deleteTmpSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?  AND jissekino = ? ";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 1次脱脂(Air_窒素)_仮登録(tmp_sr_yobikan)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYobikanData 1次脱脂(Air_窒素)データ
     * @param itemIdInfo 項目ID情報
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrYobikan(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String formId, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrYobikan srYobikanData, GXHDO101B014Const itemIdInfo) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getKcpno(), srYobikanData, itemIdInfo)));  //KCPNo
        if (isInsert) {
            params.add(null); //処理数
        }
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, itemIdInfo.getKaishiDay(), srYobikanData, itemIdInfo),
                getItemData(itemList, itemIdInfo.getKaishiTime(), srYobikanData, itemIdInfo))); //開始日時

        if (isInsert) {
            params.add(null); //終了予定日時
        }
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, itemIdInfo.getShuryouDay(), srYobikanData, itemIdInfo),
                getItemData(itemList, itemIdInfo.getShuryouTime(), srYobikanData, itemIdInfo))); //終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getGoki(), srYobikanData, itemIdInfo)));  //予備乾燥号機ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, itemIdInfo.getUkeireSettaMaisu(), srYobikanData, itemIdInfo)));  //ｾｯﾀ枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, itemIdInfo.getTotalTime(), srYobikanData, itemIdInfo)));  //予備乾燥時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, itemIdInfo.getMaxOndo(), srYobikanData, itemIdInfo)));  //予備乾燥温度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, itemIdInfo.getKeepTime(), srYobikanData, itemIdInfo)));  //ﾋﾟｰｸ時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getKaishiTantousya(), srYobikanData, itemIdInfo)));  //開始担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getShuryouTantousya(), srYobikanData, itemIdInfo)));  //終了担当者ｺｰﾄﾞ
        if (isInsert) {
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getBikou1(), srYobikanData, itemIdInfo)));  //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getBikou2(), srYobikanData, itemIdInfo)));  //備考2
        if (isInsert) {
            params.add(null); //備考3
            params.add(null); //備考4
            params.add(null); //備考5
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        if (isInsert) {
            params.add(null); //二次開始日時
            params.add(null); //二次号機ｺｰﾄﾞ
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, itemIdInfo.getProgramNo(), srYobikanData, itemIdInfo)));  //ﾌﾟﾛｸﾞﾗﾑNo．
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, itemIdInfo.getTounyuSettaMaisu(), srYobikanData, itemIdInfo)));  //投入ｾｯﾀ枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, itemIdInfo.getKaishiKakuninsya(), srYobikanData, itemIdInfo)));  //開始確認者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, itemIdInfo.getKaishuSettaMaisu(), srYobikanData, itemIdInfo)));  //回収ｾｯﾀ枚数
        //脱脂種類
        if ("GXHDO101B014".equals(formId)) {
            params.add("Air脱脂");
        } else if ("GXHDO101B015".equals(formId)) {
            params.add("窒素脱脂");
        } else {
            params.add(null);
        }
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 1次脱脂(Air_窒素)(sr_yobikan)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrYobikan 仮登録データ
     * @param itemIdInfo 項目ID情報
     * @throws SQLException 例外エラー
     */
    private void insertSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String formId, int jissekino, Timestamp systemTime, List<FXHDD01> itemList,
            SrYobikan tmpSrYobikan, GXHDO101B014Const itemIdInfo) throws SQLException {

        String sql = "INSERT INTO sr_yobikan ("
                + "kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,yobikangoki,sayasuu,yobikanjikan,"
                + "yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,biko1,biko2,biko3,biko4,biko5,torokunichiji,"
                + "kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,"
                + "dasshisyurui,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrYobikan(true, newRev, kojyo, lotNo, edaban, formId, jissekino, systemTime, itemList, tmpSrYobikan, itemIdInfo);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 1次脱脂(Air_窒素)(sr_yobikan)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param itemIdInfo 項目IDリスト
     * @throws SQLException 例外エラー
     */
    private void updateSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String formId, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, GXHDO101B014Const itemIdInfo) throws SQLException {
        String sql = "UPDATE sr_yobikan SET "
                + "kcpno = ?,kaisinichiji = ?,syuryonichiji = ?,yobikangoki = ?,sayasuu = ?,yobikanjikan = ?,yobikanondo = ?,"
                + "peakjikan = ?,kaisitantosya = ?,syuryotantosya = ?,biko1 = ?,biko2 = ?,kosinnichiji = ?,"
                + "programno = ?,tounyusettersuu = ?,StartKakuninsyacode = ?,kaisyusettersuu = ?,dasshisyurui = ?,"
                + "revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ? ";

        // 更新前の値を取得
        List<SrYobikan> srYobikanList = getSrYobikanData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrYobikan srYobikan = null;
        if (!srYobikanList.isEmpty()) {
            srYobikan = srYobikanList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrYobikan(false, newRev, "", "", "", formId, jissekino, systemTime, itemList, srYobikan, itemIdInfo);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 1次脱脂(Air_窒素)(sr_yobikan)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param formId 画面ID
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srYobikanData 1次脱脂(Air_窒素)データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrYobikan(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String formId, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrYobikan srYobikanData, GXHDO101B014Const itemIdInfo) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getKcpno(), srYobikanData, itemIdInfo)));  //KCPNo
        if (isInsert) {
            params.add(0); //処理数
        }
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, itemIdInfo.getKaishiDay(), srYobikanData, itemIdInfo),
                getItemData(itemList, itemIdInfo.getKaishiTime(), srYobikanData, itemIdInfo))); //開始日時

        if (isInsert) {
            params.add("0000-00-00 00:00:00"); //終了予定日時
        }
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, itemIdInfo.getShuryouDay(), srYobikanData, itemIdInfo),
                getItemData(itemList, itemIdInfo.getShuryouTime(), srYobikanData, itemIdInfo))); //終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getGoki(), srYobikanData, itemIdInfo)));  //予備乾燥号機ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, itemIdInfo.getUkeireSettaMaisu(), srYobikanData, itemIdInfo)));  //ｾｯﾀ枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, itemIdInfo.getTotalTime(), srYobikanData, itemIdInfo)));  //予備乾燥時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, itemIdInfo.getMaxOndo(), srYobikanData, itemIdInfo)));  //予備乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, itemIdInfo.getKeepTime(), srYobikanData, itemIdInfo)));  //ﾋﾟｰｸ時間
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getKaishiTantousya(), srYobikanData, itemIdInfo)));  //開始担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getShuryouTantousya(), srYobikanData, itemIdInfo)));  //終了担当者ｺｰﾄﾞ
        if (isInsert) {
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getBikou1(), srYobikanData, itemIdInfo)));  //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getBikou2(), srYobikanData, itemIdInfo)));  //備考2
        if (isInsert) {
            params.add(""); //備考3
            params.add(""); //備考4
            params.add(""); //備考5
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        if (isInsert) {
            params.add("0000-00-00 00:00:00"); //二次開始日時
            params.add(""); //二次号機ｺｰﾄﾞ
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, itemIdInfo.getProgramNo(), srYobikanData, itemIdInfo)));  //ﾌﾟﾛｸﾞﾗﾑNo．
        params.add(DBUtil.stringToIntObject(getItemData(itemList, itemIdInfo.getTounyuSettaMaisu(), srYobikanData, itemIdInfo)));  //投入ｾｯﾀ枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, itemIdInfo.getKaishiKakuninsya(), srYobikanData, itemIdInfo)));  //開始確認者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, itemIdInfo.getKaishuSettaMaisu(), srYobikanData, itemIdInfo)));  //回収ｾｯﾀ枚数
        //脱脂種類
        if ("GXHDO101B014".equals(formId)) {
            params.add("Air脱脂");
        } else if ("GXHDO101B015".equals(formId)) {
            params.add("窒素脱脂");
        } else {
            params.add(null);
        }
        params.add(newRev); //revision

        return params;
    }

    /**
     * 1次脱脂(Air_窒素)(sr_yobikan)削除処理
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
    private void deleteSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ? ";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [1次脱脂(Air_窒素)_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";
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
     * 開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setStartDateTime(ProcessData processDate) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));

        FXHDD01 itemDay = getItemRow(processDate.getItemList(), itemIdInfo.getKaishiDay());
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), itemIdInfo.getKaishiTime());
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
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        GXHDO101B014Const itemIdInfo = new GXHDO101B014Const(StringUtil.nullToBlank(session.getAttribute("formId")));

        FXHDD01 itemDay = getItemRow(processDate.getItemList(), itemIdInfo.getShuryouDay());
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), itemIdInfo.getShuryouTime());
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
     * @param srYobikanData 1次脱脂(Air_窒素)データ
     * @return DB値
     */
    private String getSrYobikanItemData(String itemId, SrYobikan srYobikanData, GXHDO101B014Const itemIdInfo) {

        if (itemId.equals(itemIdInfo.getKcpno())) {
            //KCPNO
            return StringUtil.nullToBlank(srYobikanData.getKcpno());
        } else if (itemId.equals(itemIdInfo.getUkeireSettaMaisu())) {
            //ｾｯﾀ枚数
            return StringUtil.nullToBlank(srYobikanData.getSayasuu());
        } else if (itemId.equals(itemIdInfo.getGoki())) {
            //号機
            return StringUtil.nullToBlank(srYobikanData.getYobikangoki());
        } else if (itemId.equals(itemIdInfo.getProgramNo())) {
            //ﾌﾟﾛｸﾞﾗﾑNo．
            return StringUtil.nullToBlank(srYobikanData.getProgramno());
        } else if (itemId.equals(itemIdInfo.getMaxOndo())) {
            //最高温度
            return StringUtil.nullToBlank(srYobikanData.getYobikanondo());
        } else if (itemId.equals(itemIdInfo.getKeepTime())) {
            //ｷｰﾌﾟ時間
            return StringUtil.nullToBlank(srYobikanData.getPeakjikan());
        } else if (itemId.equals(itemIdInfo.getTotalTime())) {
            //総時間
            return StringUtil.nullToBlank(srYobikanData.getYobikanjikan());
        } else if (itemId.equals(itemIdInfo.getTounyuSettaMaisu())) {
            //投入ｾｯﾀ枚数
            return StringUtil.nullToBlank(srYobikanData.getTounyusettersuu());
        } else if (itemId.equals(itemIdInfo.getKaishiDay())) {
            //開始日
            return DateUtil.formattedTimestamp(srYobikanData.getKaisinichiji(), "yyMMdd");
        } else if (itemId.equals(itemIdInfo.getKaishiTime())) {
            //開始時間
            return DateUtil.formattedTimestamp(srYobikanData.getKaisinichiji(), "HHmm");
        } else if (itemId.equals(itemIdInfo.getKaishiTantousya())) {
            //開始担当者
            return StringUtil.nullToBlank(srYobikanData.getKaisitantosya());
        } else if (itemId.equals(itemIdInfo.getKaishiKakuninsya())) {
            //開始確認者
            return StringUtil.nullToBlank(srYobikanData.getStartkakuninsyacode());
        } else if (itemId.equals(itemIdInfo.getShuryouDay())) {
            //終了日
            return DateUtil.formattedTimestamp(srYobikanData.getSyuryonichiji(), "yyMMdd");
        } else if (itemId.equals(itemIdInfo.getShuryouTime())) {
            //終了時間
            return DateUtil.formattedTimestamp(srYobikanData.getSyuryonichiji(), "HHmm");
        } else if (itemId.equals(itemIdInfo.getShuryouTantousya())) {
            //終了担当者
            return StringUtil.nullToBlank(srYobikanData.getSyuryotantosya());
        } else if (itemId.equals(itemIdInfo.getKaishuSettaMaisu())) {
            //回収ｾｯﾀ枚数
            return StringUtil.nullToBlank(srYobikanData.getKaisyusettersuu());
        } else if (itemId.equals(itemIdInfo.getBikou1())) {
            //備考1
            return StringUtil.nullToBlank(srYobikanData.getBiko1());
        } else if (itemId.equals(itemIdInfo.getBikou2())) {
            //備考2
            return StringUtil.nullToBlank(srYobikanData.getBiko2());
        }

        return null;
    }

    /**
     * 1次脱脂(Air_窒素)_仮登録(tmp_sr_yobikan)登録処理(削除時)
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
    private void insertDeleteDataTmpSrYobikan(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_yobikan ("
                + "kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,yobikangoki,sayasuu,yobikanjikan,"
                + "yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,biko1,biko2,biko3,biko4,biko5,torokunichiji,"
                + "kosinnichiji,NijiKaishiNichiji,NijiGoki,programno,tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,"
                + "dasshisyurui,revision,deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,kcpno,kosuu,kaisinichiji,syuryoyoteinichiji,syuryonichiji,yobikangoki,sayasuu,yobikanjikan,"
                + "yobikanondo,peakjikan,kaisitantosya,syuryotantosya,jissekino,biko1,biko2,biko3,biko4,biko5,?,"
                + "?,NijiKaishiNichiji,NijiGoki,programno,tounyusettersuu,StartKakuninsyacode,kaisyusettersuu,"
                + "dasshisyurui,?,? "
                + "FROM sr_yobikan "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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
        params.add(jissekino); //実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 規格値を項目の初期値にセットする
     * 
     * @param item 項目データ
     */
    private void setKikakuChiToValue(FXHDD01 item) {
        if (item != null) {
            String kikakuChi = item.getKikakuChi();
            String label2 = item.getLabel2();
            if (!StringUtil.isEmpty(kikakuChi) && !StringUtil.isEmpty(label2)) {
                String value = kikakuChi.replace("【", "").replace("】", "").replace(label2, "");
                if (NumberUtils.isNumber(value)) {
                    item.setValue(value);
                }
            }
        }
    }
}
