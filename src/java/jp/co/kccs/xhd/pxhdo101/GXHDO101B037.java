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
import jp.co.kccs.xhd.db.model.SrGdKeisuu;
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
import java.math.RoundingMode;
import jp.co.kccs.xhd.util.NumberUtil;

/**
 * ===============================================================================<br>
 * <br>
 * システム名 品質情報管理システム<br>
 * <br>
 * 変更日	2019/10/08<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	CTC H.Hagiuchi<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B037(外部電極・計数)ロジック
 *
 * @author CTC H.Hagiuchi
 * @since 2019/10/08
 */
public class GXHDO101B037 implements IFormLogic {

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

            // サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B037Const.BTN_KEISUUDATETIME_BOTTOM,
                    GXHDO101B037Const.BTN_KEISUUDATETIME_TOP
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B037Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B037Const.BTN_INSERT_TOP,
                    GXHDO101B037Const.BTN_DELETE_TOP,
                    GXHDO101B037Const.BTN_UPDATE_TOP,
                    GXHDO101B037Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B037Const.BTN_INSERT_BOTTOM,
                    GXHDO101B037Const.BTN_DELETE_BOTTOM,
                    GXHDO101B037Const.BTN_UPDATE_BOTTOM));

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
        
        // 処理数ﾁｪｯｸ
        String syorisuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.SYORISUU, null)); // 処理数
        // 0以下の場合
        if("".equals(syorisuu)) {
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000078"));
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
            // DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            // Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); // 工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); // ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); // 枝番
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
                // 計数_仮登録登録処理
                insertTmpSrGdKeisuu(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());
            } else {
                // 計数_仮登録更新処理
                updateTmpSrGdKeisuu(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());
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
            // 完了メッセージ設定
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

        // 処理数ﾁｪｯｸ
        String syorisuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.SYORISUU, null)); // 処理数
        // 0以下の場合
        if("".equals(syorisuu)) {
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000078"));
        }

        // 後続処理メソッド設定
        processData.setMethod("doResist");

        return processData;
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
            // DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            // Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); // 工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); // ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); // 枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            // ここでロックを掛ける
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
            SrGdKeisuu tmpSrGdKeisuu = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                // 更新前の値を取得
                List<SrGdKeisuu> srGdKeisuuList = getSrGdKeisuuData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srGdKeisuuList.isEmpty()) {
                    tmpSrGdKeisuu = srGdKeisuuList.get(0);
                }

                deleteTmpSrGdKeisuu(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // 計数_登録処理
            insertSrGdKeisuu(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrGdKeisuu);

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

        // 処理数ﾁｪｯｸ
        String syorisuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.SYORISUU, null)); // 処理数
        // 0以下の場合
        if("".equals(syorisuu)) {
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000078"));
        }

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B037Const.USER_AUTH_UPDATE_PARAM);

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
            // DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            // Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); // 工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); // ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); // 枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            // ここでロックを掛ける
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

            // 計数_更新処理
            updateSrGdKeisuu(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B037Const.USER_AUTH_DELETE_PARAM);
        
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
            // DocServer 
            conDoc = DBUtil.transactionStart(queryRunnerDoc.getDataSource().getConnection());

            // Qcdb
            conQcdb = DBUtil.transactionStart(queryRunnerQcdb.getDataSource().getConnection());

            // セッションから情報を取得
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); // 工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); // ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); // 枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
            // ここでロックを掛ける
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

            // 計数_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrGdKeisuu(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // 計数_削除処理
            deleteSrGdKeisuu(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B037Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B037Const.BTN_DELETE_BOTTOM,
                        GXHDO101B037Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B037Const.BTN_KEISUUDATETIME_BOTTOM,
                        GXHDO101B037Const.BTN_WIP_TORIKOMI_BOTTOM,
                        GXHDO101B037Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B037Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B037Const.BTN_DELETE_TOP,
                        GXHDO101B037Const.BTN_UPDATE_TOP,
                        GXHDO101B037Const.BTN_KEISUUDATETIME_TOP,
                        GXHDO101B037Const.BTN_WIP_TORIKOMI_TOP,
                        GXHDO101B037Const.BTN_BUDOMARI_KEISAN_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B037Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B037Const.BTN_INSERT_BOTTOM,
                        GXHDO101B037Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B037Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B037Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B037Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B037Const.BTN_INSERT_BOTTOM,
                        GXHDO101B037Const.BTN_KEISUUDATETIME_BOTTOM,
                        GXHDO101B037Const.BTN_WIP_TORIKOMI_BOTTOM,
                        GXHDO101B037Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B037Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B037Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B037Const.BTN_INSERT_TOP,
                        GXHDO101B037Const.BTN_KEISUUDATETIME_TOP,
                        GXHDO101B037Const.BTN_WIP_TORIKOMI_TOP,
                        GXHDO101B037Const.BTN_BUDOMARI_KEISAN_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B037Const.BTN_DELETE_BOTTOM,
                        GXHDO101B037Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B037Const.BTN_DELETE_TOP,
                        GXHDO101B037Const.BTN_UPDATE_TOP));

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
            case GXHDO101B037Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B037Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B037Const.BTN_INSERT_TOP:
            case GXHDO101B037Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B037Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B037Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B037Const.BTN_UPDATE_TOP:
            case GXHDO101B037Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B037Const.BTN_DELETE_TOP:
            case GXHDO101B037Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 計数日時
            case GXHDO101B037Const.BTN_KEISUUDATETIME_TOP:
            case GXHDO101B037Const.BTN_KEISUUDATETIME_BOTTOM:
                method = "setKeisuuDateTime";
                break;
            // 歩留まり計算
            case GXHDO101B037Const.BTN_BUDOMARI_KEISAN_TOP:
            case GXHDO101B037Const.BTN_BUDOMARI_KEISAN_BOTTOM:
                method = "checkDataCalculatBudomari";
                break;
            // WIP取込
            case GXHDO101B037Const.BTN_WIP_TORIKOMI_TOP:
            case GXHDO101B037Const.BTN_WIP_TORIKOMI_BOTTOM:
                method = "confWipTorikomi";
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

        // 仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); // ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode")); // ｵｰﾅｰｺｰﾄﾞ
        String tanijyuryo = StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo")); // 単位重量
        
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

        // 処理数の取得
        String syorisuu = null;

        // ﾃﾞｰﾀの取得
        String strfxhbm03List = "";

        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "xhd_gaibudenkyoku_dandori_koteicode");
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
            strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
            String fxhbm03DataArr[] = strfxhbm03List.split(",");

            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if (jissekiData != null && jissekiData.size() > 0) {
                int dbShorisu = jissekiData.get(0).getSyorisuu(); // 処理数               
                if (dbShorisu > 0) {
                    syorisuu = String.valueOf(dbShorisu);
                }
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, tanijyuryo)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, shikakariData, lotNo, syorisuu);

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
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     * @param syorisuu 処理数
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo, String syorisuu) {

        // ロットNo
        this.setItemData(processData, GXHDO101B037Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B037Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B037Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); // ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B037Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B037Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B037Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B037Const.OWNER, ownercode + ":" + owner);
        }
        
        // ﾛｯﾄﾌﾟﾚ
        this.setItemData(processData, GXHDO101B037Const.LOTPRE, StringUtil.nullToBlank(getMapData(shikakariData, "lotpre")));
        // 処理数
        this.setItemData(processData, GXHDO101B037Const.SYORISUU, syorisuu);

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
     * @param tanijyuryo 単位重量
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino, String tanijyuryo) throws SQLException {

        List<SrGdKeisuu> srGdKeisuuDataList = new ArrayList<>();
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
                
                // 単位重量の設定
                if (!StringUtil.isEmpty(tanijyuryo)) {
                    FXHDD01 itemTanijyuryo = getItemRow(processData.getItemList(), GXHDO101B037Const.TANIJURYO);
                    int scale = 0;
                    try {
                        scale = Integer.parseInt(itemTanijyuryo.getInputLengthDec());
                    } catch (NumberFormatException e) {
                        //処理なし  
                    }
                    BigDecimal decTanijuryo = new BigDecimal(tanijyuryo);

                    // 単位重量(現在の桁数に丸めて設定)
                    itemTanijyuryo.setValue(decTanijuryo.setScale(scale, RoundingMode.DOWN).toPlainString());
                }
                return true;
            }

            // 計数データ取得
            srGdKeisuuDataList = getSrGdKeisuuData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srGdKeisuuDataList.isEmpty()) {
                // 該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srGdKeisuuDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srGdKeisuuDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGdKeisuuData 計数データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrGdKeisuu srGdKeisuuData) {
        
        // 単位重量
        this.setItemData(processData, GXHDO101B037Const.TANIJURYO, getSrGdKeisuuItemData(GXHDO101B037Const.TANIJURYO, srGdKeisuuData));
        // 総重量
        this.setItemData(processData, GXHDO101B037Const.SOJURYO, getSrGdKeisuuItemData(GXHDO101B037Const.SOJURYO, srGdKeisuuData));
        // 送り良品数
        this.setItemData(processData, GXHDO101B037Const.OKURIRYOHINSUU, getSrGdKeisuuItemData(GXHDO101B037Const.OKURIRYOHINSUU, srGdKeisuuData));
        // 計数日
        this.setItemData(processData, GXHDO101B037Const.KEISUU_DAY, getSrGdKeisuuItemData(GXHDO101B037Const.KEISUU_DAY, srGdKeisuuData));
        // 計数時刻
        this.setItemData(processData, GXHDO101B037Const.KEISUU_TIME, getSrGdKeisuuItemData(GXHDO101B037Const.KEISUU_TIME, srGdKeisuuData));
        // 担当者
        this.setItemData(processData, GXHDO101B037Const.TANTOUSYA, getSrGdKeisuuItemData(GXHDO101B037Const.TANTOUSYA, srGdKeisuuData));
        // 歩留まり
        this.setItemData(processData, GXHDO101B037Const.BUDOMARI, getSrGdKeisuuItemData(GXHDO101B037Const.BUDOMARI, srGdKeisuuData));
        // 備考1
        this.setItemData(processData, GXHDO101B037Const.BIKO1, getSrGdKeisuuItemData(GXHDO101B037Const.BIKO1, srGdKeisuuData));
        // 備考2
        this.setItemData(processData, GXHDO101B037Const.BIKO2, getSrGdKeisuuItemData(GXHDO101B037Const.BIKO2, srGdKeisuuData));

    }

    /**
     * 計数の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 計数登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrGdKeisuu> getSrGdKeisuuData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrGdKeisuu(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrGdKeisuu(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * [実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param data ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {

        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        List<String> dataList = new ArrayList<>(Arrays.asList(data));

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT syorisuu, syoribi, syorijikoku, tantousyacode "
                + "FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";

        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());

        sql += " ORDER BY syoribi DESC, syorijikoku DESC";

        Map mapping = new HashMap<>();
        mapping.put("syorisuu", "syorisuu");
        mapping.put("syoribi", "syoribi");
        mapping.put("syorijikoku", "syorijikoku");
        mapping.put("tantousyacode", "tantousyacode");
                        
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
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param key キー(検索)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, String key) {
        try {
            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = 'common_user' AND key = '" + key +  "' ";
            List<Object> params = new ArrayList<>();
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunnerDoc.query(sql, new MapHandler());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
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
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, lotkubuncode, ownercode, lotpre, tanijuryo "
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
     * @param formId 画面ID(検索キー)
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
     * @param conDoc コネクション
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
     * @param conDoc コネクション
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
     * [外部電極・計数]から、ﾃﾞｰﾀを取得
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
    private List<SrGdKeisuu> loadSrGdKeisuu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo, lotno, edaban, kaisuu, kcpno, tokuisaki, lotkubuncode, ownercode, "
                + "lotpre, syorisuu, tanijyuryo, soujuryou, ryohinkosuu, keinichiji, keitantosya, budomari, "
                + "biko1, biko2, torokunichiji, kosinnichiji, revision , '0' AS deleteflag "
                + "FROM sr_gdkeisuu "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("kaisuu", "kaisuu"); // 回数
        mapping.put("kcpno", "kcpno"); // KCPNO
        mapping.put("tokuisaki", "tokuisaki"); // 客先
        mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
        mapping.put("lotpre", "lotpre"); // ﾛｯﾄﾌﾟﾚ
        mapping.put("syorisuu", "syorisuu"); // 処理数
        mapping.put("tanijyuryo", "tanijyuryo"); // 単位重量
        mapping.put("soujuryou", "soujuryou"); // 総重量
        mapping.put("ryohinkosuu", "ryohinkosuu"); // 送り良品数
        mapping.put("keinichiji", "keinichiji"); // 計数日時
        mapping.put("keitantosya", "keitantosya"); // 担当者
        mapping.put("budomari", "budomari"); // 歩留まり
        mapping.put("biko1", "biko1"); // 備考1
        mapping.put("biko2", "biko2"); // 備考2
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGdKeisuu>> beanHandler = new BeanListHandler<>(SrGdKeisuu.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [外部電極・計数_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrGdKeisuu> loadTmpSrGdKeisuu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo, lotno, edaban, kaisuu, kcpno, tokuisaki, lotkubuncode, ownercode, "
                + "lotpre, syorisuu, tanijyuryo, soujuryou, ryohinkosuu, keinichiji, keitantosya, budomari, "
                + "biko1, biko2, torokunichiji, kosinnichiji, revision , deleteflag "
                + "FROM tmp_sr_gdkeisuu "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("kaisuu", "kaisuu"); // 回数
        mapping.put("kcpno", "kcpno"); // KCPNO
        mapping.put("tokuisaki", "tokuisaki"); // 客先
        mapping.put("lotkubuncode", "lotkubuncode"); // ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); // ｵｰﾅｰ
        mapping.put("lotpre", "lotpre"); // ﾛｯﾄﾌﾟﾚ
        mapping.put("syorisuu", "syorisuu"); // 処理数
        mapping.put("tanijyuryo", "tanijyuryo"); // 単位重量
        mapping.put("soujuryou", "soujuryou"); // 総重量
        mapping.put("ryohinkosuu", "ryohinkosuu"); // 送り良品数
        mapping.put("keinichiji", "keinichiji"); // 計数日時
        mapping.put("keitantosya", "keitantosya"); // 担当者
        mapping.put("budomari", "budomari"); // 歩留まり
        mapping.put("biko1", "biko1"); // 備考1
        mapping.put("biko2", "biko2"); // 備考2
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGdKeisuu>> beanHandler = new BeanListHandler<>(SrGdKeisuu.class, rowProcessor);

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
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);

            // 仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); // 親ﾛｯﾄ枝番
            
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

            // 計数データ取得
            List<SrGdKeisuu> srGdKeisuuDataList = getSrGdKeisuuData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srGdKeisuuDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srGdKeisuuDataList.get(0));

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
     * @param srGdKeisuuData 計数データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrGdKeisuu srGdKeisuuData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srGdKeisuuData != null) {
            // 元データが存在する場合元データより取得
            return getSrGdKeisuuItemData(itemId, srGdKeisuuData);
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
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(tantoshaCd); // 登録者
        params.add(systemTime); // 登録日
        params.add(null); // 更新者
        params.add(null); // 更新日
        params.add(formId); // 画面ID
        params.add(rev); // revision
        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(jissekino); // 実績No
        params.add(jotaiFlg); // 状態ﾌﾗｸﾞ
        params.add("0"); // 追加工程ﾌﾗｸﾞ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 品質DB登録実績(fxhdd03)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param conDoc コネクション
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime ｼｽﾃﾑ日付
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
        params.add(tantoshaCd); // 更新者
        params.add(systemTime); // 更新日
        params.add(rev); // revision
        params.add(jotaiFlg); // 状態ﾌﾗｸﾞ

        // 検索条件
        params.add(formId); // 画面ID
        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(jissekino); // 実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 計数_仮登録(tmp_sr_gdkeisuu)登録処理
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
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_gdkeisuu ("
                + "kojyo ,lotno ,edaban ,kaisuu ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,lotpre ,syorisuu ,tanijyuryo ,soujuryou ,ryohinkosuu ,keinichiji ,"
                + " keitantosya ,budomari ,biko1 ,biko2 ,torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrGdKeisuu(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 計数_仮登録(tmp_sr_gdkeisuu)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_gdkeisuu SET "
                + " kcpno = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,lotpre = ?,syorisuu = ?,tanijyuryo = ?,soujuryou = ?,ryohinkosuu = ?, "
                + " keinichiji = ?,keitantosya = ?,budomari = ?,biko1 = ?,biko2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGdKeisuu> srSrGdKeisuuList = getSrGdKeisuuData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrGdKeisuu srGdKeisuu = null;
        if (!srSrGdKeisuuList.isEmpty()) {
            srGdKeisuu = srSrGdKeisuuList.get(0);
        }

        // 更新値設定
        List<Object> params = setUpdateParameterTmpSrGdKeisuu(false, newRev, 0, "", "", "", systemTime, itemList, srGdKeisuu, jissekino);

        // 検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 計数_仮登録(tmp_sr_gdkeisuu)削除処理
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
    private void deleteTmpSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_gdkeisuu "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        // 更新値設定
        List<Object> params = new ArrayList<>();

        // 検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 計数_仮登録(tmp_sr_gdkeisuu)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srGdKeisuuData 計数データ
     * @param jissekino 実績No
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrGdKeisuu(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrGdKeisuu srGdKeisuuData, int jissekino) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.KCPNO, srGdKeisuuData))); // KCPNO

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.KYAKUSAKI, srGdKeisuuData))); // 客先

        String[] codeLotkbn = getItemData(itemList, GXHDO101B037Const.LOT_KUBUN, srGdKeisuuData).split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(codeLotkbn[0])); // ﾛｯﾄ区分

        String[] codeOwner = getItemData(itemList, GXHDO101B037Const.OWNER, srGdKeisuuData).split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(codeOwner[0])); // ｵｰﾅｰ

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.LOTPRE, srGdKeisuuData))); // ﾛｯﾄﾌﾟﾚ

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.SYORISUU, srGdKeisuuData))); // 処理数

        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.TANIJURYO, srGdKeisuuData))); // 単位重量

        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.SOJURYO, srGdKeisuuData))); // 総重量

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.OKURIRYOHINSUU, srGdKeisuuData))); // 送り良品数

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.KEISUU_DAY, srGdKeisuuData),
                getItemData(itemList, GXHDO101B037Const.KEISUU_TIME, srGdKeisuuData))); // 計数日時

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.TANTOUSYA, srGdKeisuuData))); // 担当者

        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.BUDOMARI, srGdKeisuuData))); // 歩留まり

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.BIKO1, srGdKeisuuData))); // 備考1

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B037Const.BIKO2, srGdKeisuuData))); // 備考2

        if (isInsert) {
            params.add(systemTime); // 登録日時
            params.add(systemTime); // 更新日時
        } else {
            params.add(systemTime); // 更新日時
        }

        params.add(newRev); // revision
        params.add(deleteflag); // 削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 計数(sr_gdkeisuu)登録処理
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
     * @param tmpSrGdKeisuu 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrGdKeisuu tmpSrGdKeisuu) throws SQLException {

        String sql = "INSERT INTO sr_gdkeisuu ("
                + "kojyo ,lotno ,edaban ,kaisuu ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,lotpre ,syorisuu ,tanijyuryo ,soujuryou ,ryohinkosuu ,keinichiji ,"
                + " keitantosya ,budomari ,biko1 ,biko2 ,torokunichiji ,kosinnichiji ,revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrGdKeisuu(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrGdKeisuu);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 計数(sr_gdkeisuu)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_gdkeisuu SET "
                + " kcpno = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,lotpre = ?,syorisuu = ?,tanijyuryo = ?,soujuryou = ?,ryohinkosuu = ?, "
                + " keinichiji = ?,keitantosya = ?,budomari = ?,biko1 = ?,biko2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGdKeisuu> srGdKeisuuList = getSrGdKeisuuData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrGdKeisuu srGdKeisuu = null;
        if (!srGdKeisuuList.isEmpty()) {
            srGdKeisuu = srGdKeisuuList.get(0);
        }

        // 新値設定
        List<Object> params = setUpdateParameterSrGdKeisuu(false, newRev, "", "", "", jissekino, systemTime, itemList, srGdKeisuu);

        // 検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 計数(sr_gdkeisuu)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srGdKeisuuData 計数データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrGdKeisuu(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrGdKeisuu srGdKeisuuData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); // 工場ｺｰﾄﾞ
            params.add(lotNo); // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 回数
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B037Const.KCPNO, srGdKeisuuData))); // KCPNO

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B037Const.KYAKUSAKI, srGdKeisuuData))); // 客先

        String[] codeLotkbn = getItemData(itemList, GXHDO101B037Const.LOT_KUBUN, srGdKeisuuData).split(":", -1);
        params.add(DBUtil.stringToStringObject(codeLotkbn[0])); // ﾛｯﾄ区分

        String[] codeOwner = getItemData(itemList, GXHDO101B037Const.OWNER, srGdKeisuuData).split(":", -1);
        params.add(DBUtil.stringToStringObject(codeOwner[0])); // ｵｰﾅｰ

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B037Const.LOTPRE, srGdKeisuuData))); // ﾛｯﾄﾌﾟﾚ

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B037Const.SYORISUU, srGdKeisuuData))); // 処理数

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B037Const.TANIJURYO, srGdKeisuuData))); // 単位重量

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B037Const.SOJURYO, srGdKeisuuData))); // 総重量

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B037Const.OKURIRYOHINSUU, srGdKeisuuData))); // 送り良品数

        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B037Const.KEISUU_DAY, srGdKeisuuData),
                getItemData(itemList, GXHDO101B037Const.KEISUU_TIME, srGdKeisuuData))); // 計数日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B037Const.TANTOUSYA, srGdKeisuuData))); // 担当者

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B037Const.BUDOMARI, srGdKeisuuData))); // 歩留まり

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B037Const.BIKO1, srGdKeisuuData))); // 備考1

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B037Const.BIKO2, srGdKeisuuData))); // 備考2

        if (isInsert) {
            params.add(systemTime); // 登録日時
            params.add(systemTime); // 更新日時
        } else {
            params.add(systemTime); // 更新日時
        }

        params.add(newRev); // revision
        
        return params;
    }

    /**
     * 計数(sr_gdkeisuu)削除処理
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
    private void deleteSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_gdkeisuu "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ?";

        // 更新値設定
        List<Object> params = new ArrayList<>();

        // 検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [計数_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_gdkeisuu "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND KAISUU = ? ";
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
     * 計数日時設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKeisuuDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B037Const.KEISUU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B037Const.KEISUU_TIME);
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
     * @param srGdKeisuuData 計数データ
     * @return DB値
     */
    private String getSrGdKeisuuItemData(String itemId, SrGdKeisuu srGdKeisuuData) {
        switch (itemId) {
            // KCPNO
            case GXHDO101B037Const.KCPNO:
                return StringUtil.nullToBlank(srGdKeisuuData.getKcpno());
            // 得意先
            case GXHDO101B037Const.KYAKUSAKI:
                return StringUtil.nullToBlank(srGdKeisuuData.getTokuisaki());
            // ﾛｯﾄﾌﾟﾚ
            case GXHDO101B037Const.LOTPRE:
                return StringUtil.nullToBlank(srGdKeisuuData.getLotpre());
            // 単位重量
            case GXHDO101B037Const.TANIJURYO:
                return StringUtil.nullToBlank(srGdKeisuuData.getTanijyuryo());
            // 総重量
            case GXHDO101B037Const.SOJURYO:
                return StringUtil.nullToBlank(srGdKeisuuData.getSoujuryou());
            // 送り良品数
            case GXHDO101B037Const.OKURIRYOHINSUU:
                return StringUtil.nullToBlank(srGdKeisuuData.getRyohinkosuu());
            // 計数日
            case GXHDO101B037Const.KEISUU_DAY:
                return DateUtil.formattedTimestamp(srGdKeisuuData.getKeinichiji(), "yyMMdd");
            // 計数時刻
            case GXHDO101B037Const.KEISUU_TIME:
                return DateUtil.formattedTimestamp(srGdKeisuuData.getKeinichiji(), "HHmm");
            // 担当者
            case GXHDO101B037Const.TANTOUSYA:
                return StringUtil.nullToBlank(srGdKeisuuData.getKeitantosya());
            // 歩留まり
            case GXHDO101B037Const.BUDOMARI:
                return StringUtil.nullToBlank(srGdKeisuuData.getBudomari());
            // 備考1
            case GXHDO101B037Const.BIKO1:
                return StringUtil.nullToBlank(srGdKeisuuData.getBiko1());
            // 備考2
            case GXHDO101B037Const.BIKO2:
                return StringUtil.nullToBlank(srGdKeisuuData.getBiko2());
            default:
                return null;
        }
    }

    /**
     * 計数_仮登録(tmp_sr_gdkeisuu)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissystemTimesekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrGdKeisuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_gdkeisuu ("
                + "kojyo ,lotno ,edaban ,kaisuu ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,lotpre ,syorisuu ,tanijyuryo ,soujuryou ,ryohinkosuu ,keinichiji ,"
                + " keitantosya ,budomari ,biko1 ,biko2 ,torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + ") SELECT "
                + " kojyo ,lotno ,edaban ,kaisuu ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,lotpre ,syorisuu ,tanijyuryo ,soujuryou ,ryohinkosuu ,keinichiji ,"
                + " keitantosya ,budomari ,biko1 ,biko2 ,"
                + " ? ,? ,? ,?"
                + " FROM sr_gdkeisuu "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

        List<Object> params = new ArrayList<>();
        // 更新値
        params.add(systemTime); // 登録日時
        params.add(systemTime); // 更新日時
        params.add(newRev); // revision
        params.add(deleteflag); // 削除ﾌﾗｸﾞ

        // 検索値
        params.add(kojyo); // 工場ｺｰﾄﾞ
        params.add(lotNo); // ﾛｯﾄNo
        params.add(edaban); // 枝番
        params.add(jissekino); // 回数

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 歩留まり計算処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataCalculatBudomari(ProcessData processData) {

        // 背景色をクリア
        for (FXHDD01 fxhdd01 : processData.getItemList()) {
            fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
        }
        
        List<FXHDD01> errFxhdd01List = null;
        
        FXHDD01 itemRyohinsuu = getItemRow(processData.getItemList(), GXHDO101B037Const.OKURIRYOHINSUU);
        FXHDD01 itemSyorisuu = getItemRow(processData.getItemList(), GXHDO101B037Const.SYORISUU);
        
        String ryohinsuu = itemRyohinsuu.getValue(); // 送り良品数
        String syorisuu = itemSyorisuu.getValue(); // 処理数

        // 送り良品数チェック
        // 入力チェック
        if (StringUtil.isEmpty(ryohinsuu)) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            errFxhdd01List = Arrays.asList(itemRyohinsuu);
            processData.setErrorMessageInfoList(Arrays.asList(MessageUtil.getErrorMessageInfo("XHD-000117", true, true, errFxhdd01List, itemRyohinsuu.getLabel1())));
            return processData;
            // 数値チェック
        } else if (!NumberUtil.isNumeric(ryohinsuu)) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            errFxhdd01List = Arrays.asList(itemRyohinsuu);
            processData.setErrorMessageInfoList(Arrays.asList(MessageUtil.getErrorMessageInfo("XHD-000118", true, true, errFxhdd01List, itemRyohinsuu.getLabel1())));
            return processData;
        }

        // 処理数チェック
        // 入力チェック
        if (StringUtil.isEmpty(syorisuu)) {
            errFxhdd01List = Arrays.asList(itemSyorisuu);
            processData.setErrorMessageInfoList(Arrays.asList(MessageUtil.getErrorMessageInfo("XHD-000105", true, true, errFxhdd01List, itemSyorisuu.getLabel1())));
            return processData;
            // 数値チェック
        } else if (!NumberUtil.isNumeric(syorisuu)) {
            errFxhdd01List = Arrays.asList(itemSyorisuu);
            processData.setErrorMessageInfoList(Arrays.asList(MessageUtil.getErrorMessageInfo("XHD-000105", true, true, errFxhdd01List, itemSyorisuu.getLabel1())));
            return processData;
        } else {
            // 処理数を数値変換
            BigDecimal decSyorisuu = new BigDecimal(syorisuu);
            // 0の場合
            if (BigDecimal.ZERO.compareTo(decSyorisuu) == 0) {
                errFxhdd01List = Arrays.asList(itemSyorisuu);
                processData.setErrorMessageInfoList(Arrays.asList(MessageUtil.getErrorMessageInfo("XHD-000105", true, true, errFxhdd01List, itemSyorisuu.getLabel1())));
                return processData;
            }
        }

        // 後続処理メソッド設定
        processData.setMethod("doCalculatBudomari");

        return processData;
    }

    /**
     * 歩留まり計算処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doCalculatBudomari(ProcessData processData) {

        // 継続メソッドのクリア
        processData.setMethod("");

        try {
            FXHDD01 itemBudomari = getItemRow(processData.getItemList(), GXHDO101B037Const.BUDOMARI);
            String ryohinsuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.OKURIRYOHINSUU, null)); // 送り良品数
            String syorisuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.SYORISUU, null)); // 処理数

            // 送り良品数を数値変換
            BigDecimal decRyohinsu = new BigDecimal(ryohinsuu);

            // 処理数を数値変換
            BigDecimal decSyorisuu = new BigDecimal(syorisuu);

            // 歩留まり算出(小数以下四捨五入)
            // 送り良品数 / 処理数 ※100をかけた後に四捨五入を行うので小数部を5桁は残す
            BigDecimal budomari = decRyohinsu.divide(decSyorisuu, 5, RoundingMode.DOWN);
            // 除算結果に100を掛けて四捨五入
            budomari = budomari.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            // 結果を項目にセット
            itemBudomari.setValue(budomari.toPlainString());
        } catch (NumberFormatException e) {
            // 数値型変換失敗時はそのままリターン
        }
        return processData;
    }
    
    /**
     * WIP取込確認メッセージ表示
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData confWipTorikomi(ProcessData processData) {
        String keisuuday = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.KEISUU_DAY, null)); // 計数日
        String keisuutime = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.KEISUU_TIME, null)); // 計数時刻
        String tantousya = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.TANTOUSYA, null)); // 担当者
        String okuriryohinsuu = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.OKURIRYOHINSUU, null)); // 送り良品数
        String tanijuryo = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B037Const.TANIJURYO, null)); // 単位重量

        // 1項目以上が入力されている
        if (!StringUtil.isEmpty(keisuuday) || !StringUtil.isEmpty(keisuutime) || !StringUtil.isEmpty(tantousya) 
                || !StringUtil.isEmpty(okuriryohinsuu) || !StringUtil.isEmpty(tanijuryo)) {
            // 確認メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000116"));
        }

        // 後続処理メソッド設定
        processData.setMethod("doWipTorikomi");
        return processData;
    }
    
    /**
     * WIP取込処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     * @throws SQLException 例外エラー
     */
    public ProcessData doWipTorikomi(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");

        // 実績データ
        List<Jisseki> jissekiData = null;

        // 実績データを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "xhd_gaibudenkyoku_mekki_dandori_koteicode");
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
            String strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
            String fxhbm03DataArr[] = strfxhbm03List.split(",");

            // 実績情報の取得
            jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if (jissekiData != null && jissekiData.size() > 0) {
                int dbShorisu = jissekiData.get(0).getSyorisuu(); // 処理数               
                if (dbShorisu <= 0) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000119", dbShorisu))));
                    return processData;
                }
            }
        }

        // 仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000029"))));
            return processData;
        }

        // 実績情報の設定
        if (jissekiData != null && 0 < jissekiData.size()) {
            setItemData(processData, GXHDO101B037Const.OKURIRYOHINSUU, String.valueOf(jissekiData.get(0).getSyorisuu()));
            setItemData(processData, GXHDO101B037Const.KEISUU_DAY, DateUtil.getDisplayDate(jissekiData.get(0).getSyoribi(), "yyMMdd"));
            setItemData(processData, GXHDO101B037Const.KEISUU_TIME, DateUtil.getDisplayTime(jissekiData.get(0).getSyorijikoku(), "HHmm"));
            setItemData(processData, GXHDO101B037Const.TANTOUSYA, jissekiData.get(0).getTantousyacode());
        }

        // 仕掛情報の設定
        if (!shikakariData.isEmpty()) {
            FXHDD01 itemTanijyuryo = getItemRow(processData.getItemList(), GXHDO101B037Const.TANIJURYO);
            int scale = 0;
            try {
                scale = Integer.parseInt(itemTanijyuryo.getInputLengthDec());
            } catch (NumberFormatException e) {
                //処理なし  
            }
            // 単位重量
            BigDecimal tanijyuryo = new BigDecimal(StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo")));
            itemTanijyuryo.setValue(tanijyuryo.setScale(scale, RoundingMode.DOWN).toPlainString());
        }

        // 後続処理メソッド設定
        processData.setMethod("");

        return processData;
    }
    
}
