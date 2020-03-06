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
import jp.co.kccs.xhd.db.model.SrGaikankensa;
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
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名 品質情報管理システム<br>
 * <br>
 * 変更日 2020/01/29<br>
 * 計画書No K1811-DS001<br>
 * 変更者 SYSNAVI K.Hisanaga<br>
 * 変更理由 新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B046(外観検査)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/01/29
 */
public class GXHDO101B046 implements IFormLogic {

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
                    GXHDO101B046Const.BTN_KENSA_STARTDATETIME_TOP,
                    GXHDO101B046Const.BTN_BUDOMARI_KEISAN_TOP,
                    GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_TOP,
                    GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_TOP,
                    GXHDO101B046Const.BTN_KENSA_ENDDATETIME_TOP,
                    GXHDO101B046Const.BTN_KENSA_STARTDATETIME_BOTTOM,
                    GXHDO101B046Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                    GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_BOTTOM,
                    GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_BOTTOM,
                    GXHDO101B046Const.BTN_KENSA_ENDDATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B046Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B046Const.BTN_INSERT_TOP,
                    GXHDO101B046Const.BTN_DELETE_TOP,
                    GXHDO101B046Const.BTN_UPDATE_TOP,
                    GXHDO101B046Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B046Const.BTN_INSERT_BOTTOM,
                    GXHDO101B046Const.BTN_DELETE_BOTTOM,
                    GXHDO101B046Const.BTN_UPDATE_BOTTOM));

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

                // 外観検査_仮登録登録処理
                insertTmpSrGaikankensa(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            } else {

                // 外観検査_仮登録更新処理
                updateTmpSrGaikankensa(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

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
        // 検査開始日時、検査終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B046Const.KENSA_KAISHI_DAY); //検査開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B046Const.KENSA_KAISHI_TIME); // 検査開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B046Const.KENSA_SHURYO_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B046Const.KENSA_SHURYO_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemShuryouDay.getValue(), itemShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemKaishiDay.getLabel1(), kaishiDate, itemShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, Arrays.asList(itemKaishiDay, itemKaishiTime, itemShuryouDay, itemShuryouTime));
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
            SrGaikankensa tmpSrGaikankensa = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrGaikankensa> srGaikankensaList = getSrGaikankensaData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srGaikankensaList.isEmpty()) {
                    tmpSrGaikankensa = srGaikankensaList.get(0);
                }

                deleteTmpSrGaikankensa(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // 外観検査_登録処理
            insertSrGaikankensa(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrGaikankensa, processData);

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

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B046Const.USER_AUTH_UPDATE_PARAM);

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

            // 外観検査_更新処理
            updateSrGaikankensa(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

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
        processData.setUserAuthParam(GXHDO101B046Const.USER_AUTH_DELETE_PARAM);

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

            // 外観検査_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrGaikankensa(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // 外観検査_削除処理
            deleteSrGaikankensa(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B046Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B046Const.BTN_UPDATE_TOP,
                        GXHDO101B046Const.BTN_DELETE_TOP,
                        GXHDO101B046Const.BTN_KENSA_STARTDATETIME_TOP,
                        GXHDO101B046Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_TOP,
                        GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_TOP,
                        GXHDO101B046Const.BTN_KENSA_ENDDATETIME_TOP,
                        GXHDO101B046Const.BTN_UKEIRESOJURYO_KEISAN_TOP,
                        GXHDO101B046Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B046Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B046Const.BTN_DELETE_BOTTOM,
                        GXHDO101B046Const.BTN_KENSA_STARTDATETIME_BOTTOM,
                        GXHDO101B046Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_BOTTOM,
                        GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_BOTTOM,
                        GXHDO101B046Const.BTN_KENSA_ENDDATETIME_BOTTOM,
                        GXHDO101B046Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B046Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B046Const.BTN_INSERT_BOTTOM,
                        GXHDO101B046Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B046Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B046Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B046Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B046Const.BTN_INSERT_TOP,
                        GXHDO101B046Const.BTN_KENSA_STARTDATETIME_TOP,
                        GXHDO101B046Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_TOP,
                        GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_TOP,
                        GXHDO101B046Const.BTN_KENSA_ENDDATETIME_TOP,
                        GXHDO101B046Const.BTN_UKEIRESOJURYO_KEISAN_TOP,
                        GXHDO101B046Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B046Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B046Const.BTN_INSERT_BOTTOM,
                        GXHDO101B046Const.BTN_KENSA_STARTDATETIME_BOTTOM,
                        GXHDO101B046Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_BOTTOM,
                        GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_BOTTOM,
                        GXHDO101B046Const.BTN_KENSA_ENDDATETIME_BOTTOM,
                        GXHDO101B046Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B046Const.BTN_DELETE_BOTTOM,
                        GXHDO101B046Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B046Const.BTN_DELETE_TOP,
                        GXHDO101B046Const.BTN_UPDATE_TOP));

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
            case GXHDO101B046Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B046Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B046Const.BTN_INSERT_TOP:
            case GXHDO101B046Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B046Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B046Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B046Const.BTN_UPDATE_TOP:
            case GXHDO101B046Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B046Const.BTN_DELETE_TOP:
            case GXHDO101B046Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 検査開始日時
            case GXHDO101B046Const.BTN_KENSA_STARTDATETIME_TOP:
            case GXHDO101B046Const.BTN_KENSA_STARTDATETIME_BOTTOM:
                method = "setKensaKaishiDateTime";
                break;
            // 歩留まり計算
            case GXHDO101B046Const.BTN_BUDOMARI_KEISAN_TOP:
            case GXHDO101B046Const.BTN_BUDOMARI_KEISAN_BOTTOM:
                method = "doBudomariKeisan";
                break;
            // 合計項目計算
            case GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_TOP:
            case GXHDO101B046Const.BTN_GOUKEIKOUMOKU_KEISAN_BOTTOM:
                method = "doGokeiKomokuKeisan";
                break;
            // 未検査率計算
            case GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_TOP:
            case GXHDO101B046Const.BTN_MIKENSARITSU_KEISAN_BOTTOM:
                method = "doMikensaritsuKeisan";
                break;
            // 受入れ総重量計算
            case GXHDO101B046Const.BTN_UKEIRESOJURYO_KEISAN_TOP:
            case GXHDO101B046Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM:
                method = "confUkeireSojuryoKeisan";
                break;
            // 検査終了日時
            case GXHDO101B046Const.BTN_KENSA_ENDDATETIME_TOP:
            case GXHDO101B046Const.BTN_KENSA_ENDDATETIME_BOTTOM:
                method = "setKensaShuryoDateTime";
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

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        String tanijuryo = StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo"));// 単位重量

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

        // 処理数の取得
        String syorisuu = null;
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "common_user", "xhd_gaikankensa_maekoteicode");
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
            String strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
            String fxhbm03DataArr[] = strfxhbm03List.split(",");

            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if (jissekiData != null && 0 < jissekiData.size()) {
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数  
                if (0 < dbShorisu) {
                    syorisuu = String.valueOf(dbShorisu);
                }
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, syorisuu, tanijuryo)) {
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
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B046Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B046Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B046Const.TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B046Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B046Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B046Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B046Const.OWNER, ownercode + ":" + owner);
        }

        // 入力画面選択から受け取った情報を表示する。
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        Map srJikiqcInfo = (Map) session.getAttribute("SrJikiqcInfo");
        if (srJikiqcInfo != null && !srJikiqcInfo.isEmpty()) {
            //後工程指示内容←磁器QC[後工程指示内容2]
            this.setItemData(processData, GXHDO101B046Const.ATOKOUTEI_SHIJI_NAIYO, StringUtil.nullToBlank(srJikiqcInfo.get("sijinaiyou2")));
        }

        // 外観検査種類 
        this.setItemData(processData, GXHDO101B046Const.GAIKANKENSA_SYURUI, (String) session.getAttribute("kensashuri46"));

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
     * @param syorisu 処理数
     * @param tanijuryo 単位重量
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, int jissekino, String syorisuu, String tanijuryo) throws SQLException {

        List<SrGaikankensa> srGaikankensaDataList = new ArrayList<>();
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

                //受入総重量計算処理
                FXHDD01 itemUkeireSojuryo = getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_SOUJURYO);
                FXHDD01 itemOkuriRyohinsu = getItemRow(processData.getItemList(), GXHDO101B046Const.OKURI_RYOHINSU);
                FXHDD01 itemUkeireTanijuryo = getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_TANNIJURYO);

                itemOkuriRyohinsu.setValue(syorisuu);//送り良品数
                itemUkeireTanijuryo.setValue(NumberUtil.getTruncatData(tanijuryo, itemUkeireTanijuryo.getInputLength(), itemUkeireTanijuryo.getInputLengthDec()));//受入単位重量
                
                if (checkUkeireSojuryo(itemUkeireSojuryo, itemOkuriRyohinsu, itemUkeireTanijuryo)) {
                    // ﾁｪｯｸに問題なければ値をセット
                    calcUkeireSojuryo(itemUkeireSojuryo, itemOkuriRyohinsu, itemUkeireTanijuryo);
                }

                // 検査回数
                this.setItemData(processData, GXHDO101B046Const.KENSA_KAISUU, StringUtil.nullToBlank(jissekino));

                return true;

            }

            // 外観検査データ取得
            srGaikankensaDataList = getSrGaikankensaData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srGaikankensaDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srGaikankensaDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srGaikankensaDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGaikankensaData 外観検査データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrGaikankensa srGaikankensaData) {

        //送り良品数
        this.setItemData(processData, GXHDO101B046Const.OKURI_RYOHINSU, getSrGaikankensaItemData(GXHDO101B046Const.OKURI_RYOHINSU, srGaikankensaData));
        //受入れ単位重量
        this.setItemData(processData, GXHDO101B046Const.UKEIRE_TANNIJURYO, getSrGaikankensaItemData(GXHDO101B046Const.UKEIRE_TANNIJURYO, srGaikankensaData));
        //受入れ総重量
        this.setItemData(processData, GXHDO101B046Const.UKEIRE_SOUJURYO, getSrGaikankensaItemData(GXHDO101B046Const.UKEIRE_SOUJURYO, srGaikankensaData));
        //検査回数
        this.setItemData(processData, GXHDO101B046Const.KENSA_KAISUU, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_KAISUU, srGaikankensaData));
        //検査場所
        this.setItemData(processData, GXHDO101B046Const.KENSA_BASHO, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_BASHO, srGaikankensaData));
        //検査種類
        this.setItemData(processData, GXHDO101B046Const.KENSA_SYURUI, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_SYURUI, srGaikankensaData));
        //検査号機
        this.setItemData(processData, GXHDO101B046Const.KENSA_GOKI, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_GOKI, srGaikankensaData));
        //検査ﾌｧｲﾙNo.
        this.setItemData(processData, GXHDO101B046Const.KENSA_FILENO, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_FILENO, srGaikankensaData));
        //検査面
        this.setItemData(processData, GXHDO101B046Const.KENSA_MEN, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_MEN, srGaikankensaData));
        //検査開始日
        this.setItemData(processData, GXHDO101B046Const.KENSA_KAISHI_DAY, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_KAISHI_DAY, srGaikankensaData));
        //検査開始時間
        this.setItemData(processData, GXHDO101B046Const.KENSA_KAISHI_TIME, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_KAISHI_TIME, srGaikankensaData));
        //検査開始担当者
        this.setItemData(processData, GXHDO101B046Const.KENSA_KAISHI_TANTOUSYA, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_KAISHI_TANTOUSYA, srGaikankensaData));
        //検査開始認定者
        this.setItemData(processData, GXHDO101B046Const.KENSA_KAISHI_NINTEISYA, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_KAISHI_NINTEISYA, srGaikankensaData));
        //10kﾁｪｯｸ 未検査
        this.setItemData(processData, GXHDO101B046Const.JUU_K_CHECK_MIKENSA, getSrGaikankensaItemData(GXHDO101B046Const.JUU_K_CHECK_MIKENSA, srGaikankensaData));
        //10kﾁｪｯｸ ｶﾒﾗ差
        this.setItemData(processData, GXHDO101B046Const.JUU_K_CHECK_CAMERASA, getSrGaikankensaItemData(GXHDO101B046Const.JUU_K_CHECK_CAMERASA, srGaikankensaData));
        //1回目処理個数
        this.setItemData(processData, GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU, srGaikankensaData));
        //1回目良品個数
        this.setItemData(processData, GXHDO101B046Const.ICHI_KAIME_RYOHIN_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.ICHI_KAIME_RYOHIN_KOSU, srGaikankensaData));
        //1回目NG1数
        this.setItemData(processData, GXHDO101B046Const.ICHI_KAIME_NG1_SU, getSrGaikankensaItemData(GXHDO101B046Const.ICHI_KAIME_NG1_SU, srGaikankensaData));
        //1回目NG2数
        this.setItemData(processData, GXHDO101B046Const.ICHI_KAIME_NG2_SU, getSrGaikankensaItemData(GXHDO101B046Const.ICHI_KAIME_NG2_SU, srGaikankensaData));
        //1回目歩留まり
        this.setItemData(processData, GXHDO101B046Const.ICHI_KAIME_BUDOMARI, getSrGaikankensaItemData(GXHDO101B046Const.ICHI_KAIME_BUDOMARI, srGaikankensaData));
        //1回目未処理・ﾘﾃｽﾄ個数
        this.setItemData(processData, GXHDO101B046Const.ICHI_KAIME_MISYORI_RETEST_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.ICHI_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData));
        //2回目処理個数
        this.setItemData(processData, GXHDO101B046Const.NI_KAIME_SYORI_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.NI_KAIME_SYORI_KOSU, srGaikankensaData));
        //2回目良品個数
        this.setItemData(processData, GXHDO101B046Const.NI_KAIME_RYOHIN_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.NI_KAIME_RYOHIN_KOSU, srGaikankensaData));
        //2回目ＮＧ１数
        this.setItemData(processData, GXHDO101B046Const.NI_KAIME_NG1_SU, getSrGaikankensaItemData(GXHDO101B046Const.NI_KAIME_NG1_SU, srGaikankensaData));
        //2回目ＮＧ２数
        this.setItemData(processData, GXHDO101B046Const.NI_KAIME_NG2_SU, getSrGaikankensaItemData(GXHDO101B046Const.NI_KAIME_NG2_SU, srGaikankensaData));
        //2回目歩留まり
        this.setItemData(processData, GXHDO101B046Const.NI_KAIME_BUDOMARI, getSrGaikankensaItemData(GXHDO101B046Const.NI_KAIME_BUDOMARI, srGaikankensaData));
        //2回目未処理・ﾘﾃｽﾄ個数
        this.setItemData(processData, GXHDO101B046Const.NI_KAIME_MISYORI_RETEST_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.NI_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData));
        //3回目処理個数
        this.setItemData(processData, GXHDO101B046Const.SAN_KAIME_SYORI_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.SAN_KAIME_SYORI_KOSU, srGaikankensaData));
        //3回目良品個数
        this.setItemData(processData, GXHDO101B046Const.SAN_KAIME_RYOHIN_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.SAN_KAIME_RYOHIN_KOSU, srGaikankensaData));
        //3回目ＮＧ１数
        this.setItemData(processData, GXHDO101B046Const.SAN_KAIME_NG1_SU, getSrGaikankensaItemData(GXHDO101B046Const.SAN_KAIME_NG1_SU, srGaikankensaData));
        //3回目ＮＧ２数
        this.setItemData(processData, GXHDO101B046Const.SAN_KAIME_NG2_SU, getSrGaikankensaItemData(GXHDO101B046Const.SAN_KAIME_NG2_SU, srGaikankensaData));
        //3回目歩留まり
        this.setItemData(processData, GXHDO101B046Const.SAN_KAIME_BUDOMARI, getSrGaikankensaItemData(GXHDO101B046Const.SAN_KAIME_BUDOMARI, srGaikankensaData));
        //3回目未処理・ﾘﾃｽﾄ個数
        this.setItemData(processData, GXHDO101B046Const.SAN_KAIME_MISYORI_RETEST_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.SAN_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData));
        //合計処理個数
        this.setItemData(processData, GXHDO101B046Const.GOKEI_SYORI_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.GOKEI_SYORI_KOSU, srGaikankensaData));
        //良品総重量
        this.setItemData(processData, GXHDO101B046Const.RYOHIN_SOUJURYO, getSrGaikankensaItemData(GXHDO101B046Const.RYOHIN_SOUJURYO, srGaikankensaData));
        //合計良品個数
        this.setItemData(processData, GXHDO101B046Const.GOKEI_RYOHIN_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.GOKEI_RYOHIN_KOSU, srGaikankensaData));
        //NG総重量
        this.setItemData(processData, GXHDO101B046Const.NG_SOUJURYO, getSrGaikankensaItemData(GXHDO101B046Const.NG_SOUJURYO, srGaikankensaData));
        //合計NG1数
        this.setItemData(processData, GXHDO101B046Const.GOKEI_NGSU, getSrGaikankensaItemData(GXHDO101B046Const.GOKEI_NGSU, srGaikankensaData));
        //合計歩留まり
        this.setItemData(processData, GXHDO101B046Const.GOKEI_BUDOMARI, getSrGaikankensaItemData(GXHDO101B046Const.GOKEI_BUDOMARI, srGaikankensaData));
        //合計未処理・ﾘﾃｽﾄ個数
        this.setItemData(processData, GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU, getSrGaikankensaItemData(GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU, srGaikankensaData));
        //未検査率
        this.setItemData(processData, GXHDO101B046Const.MIKENSARITSU, getSrGaikankensaItemData(GXHDO101B046Const.MIKENSARITSU, srGaikankensaData));
        //検査終了日
        this.setItemData(processData, GXHDO101B046Const.KENSA_SHURYO_DAY, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_SHURYO_DAY, srGaikankensaData));
        //検査終了時間
        this.setItemData(processData, GXHDO101B046Const.KENSA_SHURYO_TIME, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_SHURYO_TIME, srGaikankensaData));
        //検査終了担当者１
        this.setItemData(processData, GXHDO101B046Const.KENSA_SHURYO_TANTOUSYA, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_SHURYO_TANTOUSYA, srGaikankensaData));
        //検査終了担当者２
        this.setItemData(processData, GXHDO101B046Const.KENSA_SHURYO_NINTEISYA, getSrGaikankensaItemData(GXHDO101B046Const.KENSA_SHURYO_NINTEISYA, srGaikankensaData));
        //QA外観抜き取り検査
        this.setItemData(processData, GXHDO101B046Const.QAGAIKAN_NUKITORIKENSA, getSrGaikankensaItemData(GXHDO101B046Const.QAGAIKAN_NUKITORIKENSA, srGaikankensaData));
        //備考1
        this.setItemData(processData, GXHDO101B046Const.BIKOU1, getSrGaikankensaItemData(GXHDO101B046Const.BIKOU1, srGaikankensaData));
        //備考2
        this.setItemData(processData, GXHDO101B046Const.BIKOU2, getSrGaikankensaItemData(GXHDO101B046Const.BIKOU2, srGaikankensaData));

    }

    /**
     * 外観検査の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 外観検査登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrGaikankensa> getSrGaikankensaData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrGaikankensa(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrGaikankensa(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private List<Jisseki> loadJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {

        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        List<String> dataList = new ArrayList<>(Arrays.asList(data));

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
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param userName ユーザ名
     * @param key Key
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, String userName, String key) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = ? "
                    + "   AND key = ?";

            List<Object> params = new ArrayList<>();
            params.add(userName);
            params.add(key);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);

            return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
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
        String sql = "SELECT rev "
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
     * [外観検査]から、ﾃﾞｰﾀを取得
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
    private List<SrGaikankensa> loadSrGaikankensa(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,"
                + "ukeiresoujyuryou,gaikankensasyurui,kensabasyo,kensasyurui,kensagouki,kensafileno,kensamen,kensakaishinichiji,kensakaishitantousya,"
                + "kensakaishininteisya,10Kcheckmikensa,10Kcheckcamerasa,1kaimesyorikosuu,1kaimeryouhinkosuu,1kaimeNG1suu,1kaimeNG2suu,1kaimebudomari,"
                + "1kaimemisyori,2kaimesyorikosuu,2kaimeryouhinkosuu,2kaimeNG1suu,2kaimeNG2suu,2kaimebudomari,2kaimemisyori,3kaimesyorikosuu,3kaimeryouhinkosuu,"
                + "3kaimeNG1suu,3kaimeNG2suu,3kaimebudomari,3kaimemisyori,goukeisyorikosuu,ryouhinsoujyuuryou,goukeiryouhinkosuu,NGsoujyuuryou,goukeiNGsuu,"
                + "goukeibudomari,goukeimisyori,mikennsaritu,kensasyuuryounichiji,kensasyuuryoutantousya,kensasyuuryouninteisya,QAgaikannukitorikensa,bikou1,"
                + "bikou2,tourokunichiji,koushinnichiji,revision,'0' AS deleteflag "
                + "FROM sr_gaikankensa "
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
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou"); //後工程指示内容
        mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); //送り良品数
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("gaikankensasyurui", "gaikankensasyurui"); //外観検査種類
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("kensasyurui", "kensasyurui"); //検査種類
        mapping.put("kensagouki", "kensagouki"); //検査号機
        mapping.put("kensafileno", "kensafileno"); //検査ﾌｧｲﾙNo.
        mapping.put("kensamen", "kensamen"); //検査面
        mapping.put("kensakaishinichiji", "kensakaishinichiji"); //検査開始日時
        mapping.put("kensakaishitantousya", "kensakaishitantousya"); //検査開始担当者
        mapping.put("kensakaishininteisya", "kensakaishininteisya"); //検査開始認定者
        mapping.put("10Kcheckmikensa", "juuKcheckmikensa"); //10kﾁｪｯｸ 未検査
        mapping.put("10Kcheckcamerasa", "juuKcheckcamerasa"); //10kﾁｪｯｸ ｶﾒﾗ差
        mapping.put("1kaimesyorikosuu", "ichikaimesyorikosuu"); //1回目処理個数
        mapping.put("1kaimeryouhinkosuu", "ichikaimeryouhinkosuu"); //1回目良品個数
        mapping.put("1kaimeNG1suu", "ichikaimeNg1suu"); //1回目NG1数
        mapping.put("1kaimeNG2suu", "ichikaimeNg2suu"); //1回目NG2数
        mapping.put("1kaimebudomari", "ichikaimebudomari"); //1回目歩留まり
        mapping.put("1kaimemisyori", "ichikaimemisyori"); //1回目未処理・ﾘﾃｽﾄ個数
        mapping.put("2kaimesyorikosuu", "nikaimesyorikosuu"); //2回目処理個数
        mapping.put("2kaimeryouhinkosuu", "nikaimeryouhinkosuu"); //2回目良品個数
        mapping.put("2kaimeNG1suu", "nikaimeNg1suu"); //2回目NG1数
        mapping.put("2kaimeNG2suu", "nikaimeNg2suu"); //2回目NG2数
        mapping.put("2kaimebudomari", "nikaimebudomari"); //2回目歩留まり
        mapping.put("2kaimemisyori", "nikaimemisyori"); //2回目未処理・ﾘﾃｽﾄ個数
        mapping.put("3kaimesyorikosuu", "sankaimesyorikosuu"); //3回目処理個数
        mapping.put("3kaimeryouhinkosuu", "sankaimeryouhinkosuu"); //3回目良品個数
        mapping.put("3kaimeNG1suu", "sankaimeNg1suu"); //3回目NG1数
        mapping.put("3kaimeNG2suu", "sankaimeNg2suu"); //3回目NG2数
        mapping.put("3kaimebudomari", "sankaimebudomari"); //3回目歩留まり
        mapping.put("3kaimemisyori", "sankaimemisyori"); //3回目未処理・ﾘﾃｽﾄ個数
        mapping.put("goukeisyorikosuu", "goukeisyorikosuu"); //合計処理個数
        mapping.put("ryouhinsoujyuuryou", "ryouhinsoujyuuryou"); //良品総重量
        mapping.put("goukeiryouhinkosuu", "goukeiryouhinkosuu"); //合計良品個数
        mapping.put("NGsoujyuuryou", "ngsoujyuuryou"); //NG総重量
        mapping.put("goukeiNGsuu", "goukeingsuu"); //合計NG数
        mapping.put("goukeibudomari", "goukeibudomari"); //合計歩留まり
        mapping.put("goukeimisyori", "goukeimisyori"); //合計未処理・ﾘﾃｽﾄ個数
        mapping.put("mikennsaritu", "mikennsaritu"); //未検査率
        mapping.put("kensasyuuryounichiji", "kensasyuuryounichiji"); //検査終了日
        mapping.put("kensasyuuryoutantousya", "kensasyuuryoutantousya"); //検査終了担当者
        mapping.put("kensasyuuryouninteisya", "kensasyuuryouninteisya"); //検査終了認定者
        mapping.put("QAgaikannukitorikensa", "qagaikannukitorikensa"); //QA外観抜き取り検査
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("tourokunichiji", "tourokunichiji"); //登録日時
        mapping.put("koushinnichiji", "koushinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGaikankensa>> beanHandler = new BeanListHandler<>(SrGaikankensa.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [外観検査_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrGaikankensa> loadTmpSrGaikankensa(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,"
                + "ukeiresoujyuryou,gaikankensasyurui,kensabasyo,kensasyurui,kensagouki,kensafileno,kensamen,kensakaishinichiji,kensakaishitantousya,"
                + "kensakaishininteisya,10Kcheckmikensa,10Kcheckcamerasa,1kaimesyorikosuu,1kaimeryouhinkosuu,1kaimeNG1suu,1kaimeNG2suu,1kaimebudomari,"
                + "1kaimemisyori,2kaimesyorikosuu,2kaimeryouhinkosuu,2kaimeNG1suu,2kaimeNG2suu,2kaimebudomari,2kaimemisyori,3kaimesyorikosuu,3kaimeryouhinkosuu,"
                + "3kaimeNG1suu,3kaimeNG2suu,3kaimebudomari,3kaimemisyori,goukeisyorikosuu,ryouhinsoujyuuryou,goukeiryouhinkosuu,NGsoujyuuryou,goukeiNGsuu,"
                + "goukeibudomari,goukeimisyori,mikennsaritu,kensasyuuryounichiji,kensasyuuryoutantousya,kensasyuuryouninteisya,QAgaikannukitorikensa,bikou1,bikou2,"
                + "tourokunichiji,koushinnichiji,revision,deleteflag "
                + "FROM tmp_sr_gaikankensa "
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
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("atokouteisijinaiyou", "atokouteisijinaiyou"); //後工程指示内容
        mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); //送り良品数
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("gaikankensasyurui", "gaikankensasyurui"); //外観検査種類
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("kensasyurui", "kensasyurui"); //検査種類
        mapping.put("kensagouki", "kensagouki"); //検査号機
        mapping.put("kensafileno", "kensafileno"); //検査ﾌｧｲﾙNo.
        mapping.put("kensamen", "kensamen"); //検査面
        mapping.put("kensakaishinichiji", "kensakaishinichiji"); //検査開始日時
        mapping.put("kensakaishitantousya", "kensakaishitantousya"); //検査開始担当者
        mapping.put("kensakaishininteisya", "kensakaishininteisya"); //検査開始認定者
        mapping.put("10Kcheckmikensa", "juuKcheckmikensa"); //10kﾁｪｯｸ 未検査
        mapping.put("10Kcheckcamerasa", "juuKcheckcamerasa"); //10kﾁｪｯｸ ｶﾒﾗ差
        mapping.put("1kaimesyorikosuu", "ichikaimesyorikosuu"); //1回目処理個数
        mapping.put("1kaimeryouhinkosuu", "ichikaimeryouhinkosuu"); //1回目良品個数
        mapping.put("1kaimeNG1suu", "ichikaimeNg1suu"); //1回目NG1数
        mapping.put("1kaimeNG2suu", "ichikaimeNg2suu"); //1回目NG2数
        mapping.put("1kaimebudomari", "ichikaimebudomari"); //1回目歩留まり
        mapping.put("1kaimemisyori", "ichikaimemisyori"); //1回目未処理・ﾘﾃｽﾄ個数
        mapping.put("2kaimesyorikosuu", "nikaimesyorikosuu"); //2回目処理個数
        mapping.put("2kaimeryouhinkosuu", "nikaimeryouhinkosuu"); //2回目良品個数
        mapping.put("2kaimeNG1suu", "nikaimeNg1suu"); //2回目NG1数
        mapping.put("2kaimeNG2suu", "nikaimeNg2suu"); //2回目NG2数
        mapping.put("2kaimebudomari", "nikaimebudomari"); //2回目歩留まり
        mapping.put("2kaimemisyori", "nikaimemisyori"); //2回目未処理・ﾘﾃｽﾄ個数
        mapping.put("3kaimesyorikosuu", "sankaimesyorikosuu"); //3回目処理個数
        mapping.put("3kaimeryouhinkosuu", "sankaimeryouhinkosuu"); //3回目良品個数
        mapping.put("3kaimeNG1suu", "sankaimeNg1suu"); //3回目NG1数
        mapping.put("3kaimeNG2suu", "sankaimeNg2suu"); //3回目NG2数
        mapping.put("3kaimebudomari", "sankaimebudomari"); //3回目歩留まり
        mapping.put("3kaimemisyori", "sankaimemisyori"); //3回目未処理・ﾘﾃｽﾄ個数
        mapping.put("goukeisyorikosuu", "goukeisyorikosuu"); //合計処理個数
        mapping.put("ryouhinsoujyuuryou", "ryouhinsoujyuuryou"); //良品総重量
        mapping.put("goukeiryouhinkosuu", "goukeiryouhinkosuu"); //合計良品個数
        mapping.put("NGsoujyuuryou", "ngsoujyuuryou"); //NG総重量
        mapping.put("goukeiNGsuu", "goukeingsuu"); //合計NG数
        mapping.put("goukeibudomari", "goukeibudomari"); //合計歩留まり
        mapping.put("goukeimisyori", "goukeimisyori"); //合計未処理・ﾘﾃｽﾄ個数
        mapping.put("mikennsaritu", "mikennsaritu"); //未検査率
        mapping.put("kensasyuuryounichiji", "kensasyuuryounichiji"); //検査終了日
        mapping.put("kensasyuuryoutantousya", "kensasyuuryoutantousya"); //検査終了担当者
        mapping.put("kensasyuuryouninteisya", "kensasyuuryouninteisya"); //検査終了認定者
        mapping.put("QAgaikannukitorikensa", "qagaikannukitorikensa"); //QA外観抜き取り検査
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("tourokunichiji", "tourokunichiji"); //登録日時
        mapping.put("koushinnichiji", "koushinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGaikankensa>> beanHandler = new BeanListHandler<>(SrGaikankensa.class, rowProcessor);

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

            // 外観検査データ取得
            List<SrGaikankensa> srGaikankensaDataList = getSrGaikankensaData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srGaikankensaDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srGaikankensaDataList.get(0));

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
     * @param srGaikankensaData 外観検査データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrGaikankensa srGaikankensaData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srGaikankensaData != null) {
            // 元データが存在する場合元データより取得
            return getSrGaikankensaItemData(itemId, srGaikankensaData);
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
     * @param systemTime システム日付
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
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
     * 外観検査_仮登録(tmp_sr_gaikankensa)登録処理
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
    private void insertTmpSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_gaikankensa ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gaikankensasyurui,kensabasyo,kensasyurui,kensagouki,kensafileno,kensamen,kensakaishinichiji,kensakaishitantousya,kensakaishininteisya,"
                + "10Kcheckmikensa,10Kcheckcamerasa,1kaimesyorikosuu,1kaimeryouhinkosuu,1kaimeNG1suu,1kaimeNG2suu,1kaimebudomari,1kaimemisyori,2kaimesyorikosuu,"
                + "2kaimeryouhinkosuu,2kaimeNG1suu,2kaimeNG2suu,2kaimebudomari,2kaimemisyori,3kaimesyorikosuu,3kaimeryouhinkosuu,3kaimeNG1suu,3kaimeNG2suu,"
                + "3kaimebudomari,3kaimemisyori,goukeisyorikosuu,ryouhinsoujyuuryou,goukeiryouhinkosuu,NGsoujyuuryou,goukeiNGsuu,goukeibudomari,goukeimisyori,"
                + "mikennsaritu,kensasyuuryounichiji,kensasyuuryoutantousya,kensasyuuryouninteisya,QAgaikannukitorikensa,bikou1,bikou2,tourokunichiji,"
                + "koushinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrGaikankensa(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 外観検査_仮登録(tmp_sr_gaikankensa)更新処理
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
    private void updateTmpSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_gaikankensa SET "
                + "kcpno = ?,tokuisaki = ?,ownercode = ?,lotkubuncode = ?,atokouteisijinaiyou = ?,okuriryouhinsuu = ?,"
                + "ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,gaikankensasyurui = ?,kensabasyo = ?,kensasyurui = ?,kensagouki = ?,kensafileno = ?,kensamen = ?,"
                + "kensakaishinichiji = ?,kensakaishitantousya = ?,kensakaishininteisya = ?,10Kcheckmikensa = ?,10Kcheckcamerasa = ?,1kaimesyorikosuu = ?,"
                + "1kaimeryouhinkosuu = ?,1kaimeNG1suu = ?,1kaimeNG2suu = ?,1kaimebudomari = ?,1kaimemisyori = ?,2kaimesyorikosuu = ?,2kaimeryouhinkosuu = ?,"
                + "2kaimeNG1suu = ?,2kaimeNG2suu = ?,2kaimebudomari = ?,2kaimemisyori = ?,3kaimesyorikosuu = ?,3kaimeryouhinkosuu = ?,3kaimeNG1suu = ?,3kaimeNG2suu = ?,"
                + "3kaimebudomari = ?,3kaimemisyori = ?,goukeisyorikosuu = ?,ryouhinsoujyuuryou = ?,goukeiryouhinkosuu = ?,NGsoujyuuryou = ?,goukeiNGsuu = ?,"
                + "goukeibudomari = ?,goukeimisyori = ?,mikennsaritu = ?,kensasyuuryounichiji = ?,kensasyuuryoutantousya = ?,kensasyuuryouninteisya = ?,QAgaikannukitorikensa = ?,"
                + "bikou1 = ?,bikou2 = ?,koushinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGaikankensa> srSrGaikankensaList = getSrGaikankensaData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrGaikankensa srGaikankensa = null;
        if (!srSrGaikankensaList.isEmpty()) {
            srGaikankensa = srSrGaikankensaList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrGaikankensa(false, newRev, 0, "", "", "", systemTime, itemList, srGaikankensa, jissekino, processData);

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
     * 外観検査_仮登録(tmp_sr_gaikankensa)削除処理
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
    private void deleteTmpSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_gaikankensa "
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
     * 外観検査_仮登録(tmp_sr_gaikankensa)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srGaikankensaData 外観検査データ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrGaikankensa(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrGaikankensa srGaikankensaData, int jissekino, ProcessData processData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 回数
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KCPNO, srGaikankensaData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.TOKUISAKI, srGaikankensaData))); //客先
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ATOKOUTEI_SHIJI_NAIYO, srGaikankensaData))); //後工程指示内容
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.OKURI_RYOHINSU, srGaikankensaData))); //送り良品数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.UKEIRE_TANNIJURYO, srGaikankensaData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.UKEIRE_SOUJURYO, srGaikankensaData))); //受入れ総重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.GAIKANKENSA_SYURUI, srGaikankensaData))); //外観検査種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_BASHO, srGaikankensaData))); //検査場所
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_SYURUI, srGaikankensaData))); //検査種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_GOKI, srGaikankensaData))); //検査号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_FILENO, srGaikankensaData))); //検査ﾌｧｲﾙNo.
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_MEN, srGaikankensaData))); //検査面
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_DAY, srGaikankensaData),
                getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_TIME, srGaikankensaData))); //検査開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_TANTOUSYA, srGaikankensaData))); //検査開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_NINTEISYA, srGaikankensaData))); //検査開始認定者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.JUU_K_CHECK_MIKENSA, srGaikankensaData))); //10kﾁｪｯｸ 未検査
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.JUU_K_CHECK_CAMERASA, srGaikankensaData))); //10kﾁｪｯｸ ｶﾒﾗ差
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU, srGaikankensaData))); //1回目処理個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_RYOHIN_KOSU, srGaikankensaData))); //1回目良品個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_NG1_SU, srGaikankensaData))); //1回目NG1数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_NG2_SU, srGaikankensaData))); //1回目NG2数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_BUDOMARI, srGaikankensaData))); //1回目歩留まり
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData))); //1回目未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NI_KAIME_SYORI_KOSU, srGaikankensaData))); //2回目処理個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NI_KAIME_RYOHIN_KOSU, srGaikankensaData))); //2回目良品個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NI_KAIME_NG1_SU, srGaikankensaData))); //2回目NG1数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NI_KAIME_NG2_SU, srGaikankensaData))); //2回目NG2数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NI_KAIME_BUDOMARI, srGaikankensaData))); //2回目歩留まり
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NI_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData))); //2回目未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_SYORI_KOSU, srGaikankensaData))); //3回目処理個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_RYOHIN_KOSU, srGaikankensaData))); //3回目良品個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_NG1_SU, srGaikankensaData))); //3回目NG1数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_NG2_SU, srGaikankensaData))); //3回目NG2数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_BUDOMARI, srGaikankensaData))); //3回目歩留まり
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData))); //3回目未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.GOKEI_SYORI_KOSU, srGaikankensaData))); //合計処理個数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.RYOHIN_SOUJURYO, srGaikankensaData))); //良品総重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.GOKEI_RYOHIN_KOSU, srGaikankensaData))); //合計良品個数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.NG_SOUJURYO, srGaikankensaData))); //NG総重量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.GOKEI_NGSU, srGaikankensaData))); //合計NG数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.GOKEI_BUDOMARI, srGaikankensaData))); //合計歩留まり
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU, srGaikankensaData))); //合計未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.MIKENSARITSU, srGaikankensaData))); //未検査率
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_DAY, srGaikankensaData),
                getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_TIME, srGaikankensaData))); //検査終了日
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_TANTOUSYA, srGaikankensaData))); //検査終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_NINTEISYA, srGaikankensaData))); //検査終了認定者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.QAGAIKAN_NUKITORIKENSA, srGaikankensaData))); //QA外観抜き取り検査
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.BIKOU1, srGaikankensaData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B046Const.BIKOU2, srGaikankensaData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 外観検査(sr_gaikankensa)登録処理
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
     * @param tmpSrGaikankensa 仮登録データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrGaikankensa tmpSrGaikankensa, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO sr_gaikankensa ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gaikankensasyurui,kensabasyo,kensasyurui,kensagouki,kensafileno,kensamen,kensakaishinichiji,kensakaishitantousya,kensakaishininteisya,"
                + "10Kcheckmikensa,10Kcheckcamerasa,1kaimesyorikosuu,1kaimeryouhinkosuu,1kaimeNG1suu,1kaimeNG2suu,1kaimebudomari,1kaimemisyori,2kaimesyorikosuu,"
                + "2kaimeryouhinkosuu,2kaimeNG1suu,2kaimeNG2suu,2kaimebudomari,2kaimemisyori,3kaimesyorikosuu,3kaimeryouhinkosuu,3kaimeNG1suu,3kaimeNG2suu,"
                + "3kaimebudomari,3kaimemisyori,goukeisyorikosuu,ryouhinsoujyuuryou,goukeiryouhinkosuu,NGsoujyuuryou,goukeiNGsuu,goukeibudomari,goukeimisyori,"
                + "mikennsaritu,kensasyuuryounichiji,kensasyuuryoutantousya,kensasyuuryouninteisya,QAgaikannukitorikensa,bikou1,bikou2,tourokunichiji,koushinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrGaikankensa(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrGaikankensa, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 外観検査(sr_gaikankensa)更新処理
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
    private void updateSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_gaikankensa SET "
                + "kcpno = ?,tokuisaki = ?,ownercode = ?,lotkubuncode = ?,atokouteisijinaiyou = ?,okuriryouhinsuu = ?,"
                + "ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,gaikankensasyurui = ?,kensabasyo = ?,kensasyurui = ?,kensagouki = ?,kensafileno = ?,kensamen = ?,"
                + "kensakaishinichiji = ?,kensakaishitantousya = ?,kensakaishininteisya = ?,10Kcheckmikensa = ?,10Kcheckcamerasa = ?,1kaimesyorikosuu = ?,"
                + "1kaimeryouhinkosuu = ?,1kaimeNG1suu = ?,1kaimeNG2suu = ?,1kaimebudomari = ?,1kaimemisyori = ?,2kaimesyorikosuu = ?,2kaimeryouhinkosuu = ?,"
                + "2kaimeNG1suu = ?,2kaimeNG2suu = ?,2kaimebudomari = ?,2kaimemisyori = ?,3kaimesyorikosuu = ?,3kaimeryouhinkosuu = ?,3kaimeNG1suu = ?,3kaimeNG2suu = ?,"
                + "3kaimebudomari = ?,3kaimemisyori = ?,goukeisyorikosuu = ?,ryouhinsoujyuuryou = ?,goukeiryouhinkosuu = ?,NGsoujyuuryou = ?,goukeiNGsuu = ?,"
                + "goukeibudomari = ?,goukeimisyori = ?,mikennsaritu = ?,kensasyuuryounichiji = ?,kensasyuuryoutantousya = ?,kensasyuuryouninteisya = ?,QAgaikannukitorikensa = ?,"
                + "bikou1 = ?,bikou2 = ?,koushinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGaikankensa> srGaikankensaList = getSrGaikankensaData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrGaikankensa srGaikankensa = null;
        if (!srGaikankensaList.isEmpty()) {
            srGaikankensa = srGaikankensaList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrGaikankensa(false, newRev, "", "", "", jissekino, systemTime, itemList, srGaikankensa, processData);

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
     * 外観検査(sr_gaikankensa)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srGaikankensaData 外観検査データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrGaikankensa(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrGaikankensa srGaikankensaData, ProcessData processData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 回数
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KCPNO, srGaikankensaData))); //KCPNO
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.TOKUISAKI, srGaikankensaData))); //客先
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.ATOKOUTEI_SHIJI_NAIYO, srGaikankensaData))); //後工程指示内容
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.OKURI_RYOHINSU, srGaikankensaData))); //送り良品数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.UKEIRE_TANNIJURYO, srGaikankensaData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.UKEIRE_SOUJURYO, srGaikankensaData))); //受入れ総重量
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.GAIKANKENSA_SYURUI, srGaikankensaData))); //外観検査種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_BASHO, srGaikankensaData))); //検査場所
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_SYURUI, srGaikankensaData))); //検査種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_GOKI, srGaikankensaData))); //検査号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_FILENO, srGaikankensaData))); //検査ﾌｧｲﾙNo.
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_MEN, srGaikankensaData))); //検査面
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_DAY, srGaikankensaData),
                getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_TIME, srGaikankensaData))); //検査開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_TANTOUSYA, srGaikankensaData))); //検査開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_KAISHI_NINTEISYA, srGaikankensaData))); //検査開始認定者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.JUU_K_CHECK_MIKENSA, srGaikankensaData))); //10kﾁｪｯｸ 未検査
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.JUU_K_CHECK_CAMERASA, srGaikankensaData))); //10kﾁｪｯｸ ｶﾒﾗ差
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU, srGaikankensaData))); //1回目処理個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_RYOHIN_KOSU, srGaikankensaData))); //1回目良品個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_NG1_SU, srGaikankensaData))); //1回目NG1数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_NG2_SU, srGaikankensaData))); //1回目NG2数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_BUDOMARI, srGaikankensaData))); //1回目歩留まり
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.ICHI_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData))); //1回目未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.NI_KAIME_SYORI_KOSU, srGaikankensaData))); //2回目処理個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.NI_KAIME_RYOHIN_KOSU, srGaikankensaData))); //2回目良品個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.NI_KAIME_NG1_SU, srGaikankensaData))); //2回目NG1数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.NI_KAIME_NG2_SU, srGaikankensaData))); //2回目NG2数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.NI_KAIME_BUDOMARI, srGaikankensaData))); //2回目歩留まり
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.NI_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData))); //2回目未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_SYORI_KOSU, srGaikankensaData))); //3回目処理個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_RYOHIN_KOSU, srGaikankensaData))); //3回目良品個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_NG1_SU, srGaikankensaData))); //3回目NG1数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_NG2_SU, srGaikankensaData))); //3回目NG2数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_BUDOMARI, srGaikankensaData))); //3回目歩留まり
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.SAN_KAIME_MISYORI_RETEST_KOSU, srGaikankensaData))); //3回目未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.GOKEI_SYORI_KOSU, srGaikankensaData))); //合計処理個数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.RYOHIN_SOUJURYO, srGaikankensaData))); //良品総重量
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.GOKEI_RYOHIN_KOSU, srGaikankensaData))); //合計良品個数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.NG_SOUJURYO, srGaikankensaData))); //NG総重量
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.GOKEI_NGSU, srGaikankensaData))); //合計NG数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.GOKEI_BUDOMARI, srGaikankensaData))); //合計歩留まり
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU, srGaikankensaData))); //合計未処理・ﾘﾃｽﾄ個数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B046Const.MIKENSARITSU, srGaikankensaData))); //未検査率
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_DAY, srGaikankensaData),
                getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_TIME, srGaikankensaData))); //検査終了日
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_TANTOUSYA, srGaikankensaData))); //検査終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.KENSA_SHURYO_NINTEISYA, srGaikankensaData))); //検査終了認定者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.QAGAIKAN_NUKITORIKENSA, srGaikankensaData))); //QA外観抜き取り検査
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.BIKOU1, srGaikankensaData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B046Const.BIKOU2, srGaikankensaData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision

        return params;
    }

    /**
     * 外観検査(sr_gaikankensa)削除処理
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
    private void deleteSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_gaikankensa "
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
     * [外観検査_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_gaikankensa "
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
     * 検査開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKensaKaishiDateTime(ProcessData processDate) {
        processDate.setMethod("");
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B046Const.KENSA_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B046Const.KENSA_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        return processDate;
    }

    /**
     * 歩留まり計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBudomariKeisan(ProcessData processData) {
        processData.setMethod("");
        //歩留まり計算処理
        // 1回目データ
        calcBudomari(getItemRow(processData.getItemList(), GXHDO101B046Const.ICHI_KAIME_BUDOMARI),
                getItemRow(processData.getItemList(), GXHDO101B046Const.ICHI_KAIME_RYOHIN_KOSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU));
        // 2回目データ
        calcBudomari(getItemRow(processData.getItemList(), GXHDO101B046Const.NI_KAIME_BUDOMARI),
                getItemRow(processData.getItemList(), GXHDO101B046Const.NI_KAIME_RYOHIN_KOSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.NI_KAIME_SYORI_KOSU));
        // 3回目データ
        calcBudomari(getItemRow(processData.getItemList(), GXHDO101B046Const.SAN_KAIME_BUDOMARI),
                getItemRow(processData.getItemList(), GXHDO101B046Const.SAN_KAIME_RYOHIN_KOSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.SAN_KAIME_SYORI_KOSU));
        return processData;
    }

    /**
     * 合計項目計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doGokeiKomokuKeisan(ProcessData processData) {
        processData.setMethod("");
        // 合計処理個数を計算
        calcSumData(getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_SYORI_KOSU),
                Arrays.asList(
                        getItemRow(processData.getItemList(), GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU),
                        getItemRow(processData.getItemList(), GXHDO101B046Const.NI_KAIME_SYORI_KOSU),
                        getItemRow(processData.getItemList(), GXHDO101B046Const.SAN_KAIME_SYORI_KOSU)
                ));

        // 合計良品個数計算
        calcKosuBasedJuryo(getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_RYOHIN_KOSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.RYOHIN_SOUJURYO),
                getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_TANNIJURYO));

        // 合計NG数計算
        calcKosuBasedJuryo(getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_NGSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.NG_SOUJURYO),
                getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_TANNIJURYO));

        //合計歩留まり計算処理
        calcBudomari(getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_BUDOMARI),
                getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_RYOHIN_KOSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.OKURI_RYOHINSU));

        //合計未処理・ﾘﾃｽﾄ個数
        calcSumData(getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU),
                Arrays.asList(
                        getItemRow(processData.getItemList(), GXHDO101B046Const.ICHI_KAIME_MISYORI_RETEST_KOSU),
                        getItemRow(processData.getItemList(), GXHDO101B046Const.NI_KAIME_MISYORI_RETEST_KOSU),
                        getItemRow(processData.getItemList(), GXHDO101B046Const.SAN_KAIME_MISYORI_RETEST_KOSU)
                ));

        return processData;
    }

    /**
     * 未検査率計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doMikensaritsuKeisan(ProcessData processData) {
        processData.setMethod("");
        //未検査率計算処理
        calcMikensaritsu(getItemRow(processData.getItemList(), GXHDO101B046Const.MIKENSARITSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.OKURI_RYOHINSU));

        return processData;
    }

    /**
     * 受入れ総重量計算(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData confUkeireSojuryoKeisan(ProcessData processData) {

        processData.setMethod("");
        // チェック処理で計算対象外の場合はそのままリターン
        if (!checkUkeireSojuryo(getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_SOUJURYO),
                getItemRow(processData.getItemList(), GXHDO101B046Const.OKURI_RYOHINSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_TANNIJURYO))) {
            return processData;
        }

        // 受入総重量が入力されている場合は警告メッセージを表示
        if (!StringUtil.isEmpty(getItemData(processData.getItemList(), GXHDO101B046Const.UKEIRE_SOUJURYO, null))) {
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000180"));
        }

        // 後続処理メソッド設定
        processData.setMethod("doUkeireSojuryoKeisan");

        return processData;

    }

    /**
     * 受入れ総重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doUkeireSojuryoKeisan(ProcessData processData) {

        processData.setMethod("");

        //受入総重量計算処理
        calcUkeireSojuryo(getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_SOUJURYO),
                getItemRow(processData.getItemList(), GXHDO101B046Const.OKURI_RYOHINSU),
                getItemRow(processData.getItemList(), GXHDO101B046Const.UKEIRE_TANNIJURYO));

        return processData;
    }

    /**
     * 検査終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKensaShuryoDateTime(ProcessData processDate) {
        processDate.setMethod("");
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B046Const.KENSA_SHURYO_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B046Const.KENSA_SHURYO_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

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
     * @param srGaikankensaData 外観検査データ
     * @return DB値
     */
    private String getSrGaikankensaItemData(String itemId, SrGaikankensa srGaikankensaData) {
        switch (itemId) {
            // KCPNO
            case GXHDO101B046Const.KCPNO:
                return StringUtil.nullToBlank(srGaikankensaData.getKcpno());
            // 客先
            case GXHDO101B046Const.TOKUISAKI:
                return StringUtil.nullToBlank(srGaikankensaData.getTokuisaki());
            // ﾛｯﾄ区分
            case GXHDO101B046Const.LOT_KUBUN:
                return StringUtil.nullToBlank(srGaikankensaData.getLotkubuncode());
            // ｵｰﾅｰ
            case GXHDO101B046Const.OWNER:
                return StringUtil.nullToBlank(srGaikankensaData.getOwnercode());
            // 後工程指示内容
            case GXHDO101B046Const.ATOKOUTEI_SHIJI_NAIYO:
                return StringUtil.nullToBlank(srGaikankensaData.getAtokouteisijinaiyou());
            // 送り良品数
            case GXHDO101B046Const.OKURI_RYOHINSU:
                return StringUtil.nullToBlank(srGaikankensaData.getOkuriryouhinsuu());
            // 受入れ単位重量
            case GXHDO101B046Const.UKEIRE_TANNIJURYO:
                return StringUtil.nullToBlank(srGaikankensaData.getUkeiretannijyuryo());
            // 受入れ総重量
            case GXHDO101B046Const.UKEIRE_SOUJURYO:
                return StringUtil.nullToBlank(srGaikankensaData.getUkeiresoujyuryou());
            // 外観検査種類
            case GXHDO101B046Const.GAIKANKENSA_SYURUI:
                return StringUtil.nullToBlank(srGaikankensaData.getGaikankensasyurui());
            // 検査回数
            case GXHDO101B046Const.KENSA_KAISUU:
                return StringUtil.nullToBlank(srGaikankensaData.getKaisuu());
            // 検査場所
            case GXHDO101B046Const.KENSA_BASHO:
                return StringUtil.nullToBlank(srGaikankensaData.getKensabasyo());
            // 検査種類
            case GXHDO101B046Const.KENSA_SYURUI:
                return StringUtil.nullToBlank(srGaikankensaData.getKensasyurui());
            // 検査号機
            case GXHDO101B046Const.KENSA_GOKI:
                return StringUtil.nullToBlank(srGaikankensaData.getKensagouki());
            // 検査ファイルＮｏ．
            case GXHDO101B046Const.KENSA_FILENO:
                return StringUtil.nullToBlank(srGaikankensaData.getKensafileno());
            // 検査面
            case GXHDO101B046Const.KENSA_MEN:
                return StringUtil.nullToBlank(srGaikankensaData.getKensamen());
            // 検査開始日
            case GXHDO101B046Const.KENSA_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srGaikankensaData.getKensakaishinichiji(), "yyMMdd");
            // 検査開始時間
            case GXHDO101B046Const.KENSA_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srGaikankensaData.getKensakaishinichiji(), "HHmm");
            // 検査開始担当者
            case GXHDO101B046Const.KENSA_KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srGaikankensaData.getKensakaishitantousya());
            // 検査開始認定者
            case GXHDO101B046Const.KENSA_KAISHI_NINTEISYA:
                return StringUtil.nullToBlank(srGaikankensaData.getKensakaishininteisya());
            // 10kﾁｪｯｸ 未検査
            case GXHDO101B046Const.JUU_K_CHECK_MIKENSA:
                return StringUtil.nullToBlank(srGaikankensaData.getJuuKcheckmikensa());
            // 10kﾁｪｯｸ ｶﾒﾗ差
            case GXHDO101B046Const.JUU_K_CHECK_CAMERASA:
                return StringUtil.nullToBlank(srGaikankensaData.getJuuKcheckcamerasa());
            // 1回目処理個数
            case GXHDO101B046Const.ICHI_KAIME_SYORI_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getIchikaimesyorikosuu());
            // 1回目良品個数
            case GXHDO101B046Const.ICHI_KAIME_RYOHIN_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getIchikaimeryouhinkosuu());
            // 1回目ＮＧ１数
            case GXHDO101B046Const.ICHI_KAIME_NG1_SU:
                return StringUtil.nullToBlank(srGaikankensaData.getIchikaimeNg1suu());
            // 1回目ＮＧ２数
            case GXHDO101B046Const.ICHI_KAIME_NG2_SU:
                return StringUtil.nullToBlank(srGaikankensaData.getIchikaimeNg2suu());
            // 1回目歩留まり
            case GXHDO101B046Const.ICHI_KAIME_BUDOMARI:
                return StringUtil.nullToBlank(srGaikankensaData.getIchikaimebudomari());
            // 1回目未処理・ﾘﾃｽﾄ個数
            case GXHDO101B046Const.ICHI_KAIME_MISYORI_RETEST_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getIchikaimemisyori());
            // 2回目処理個数
            case GXHDO101B046Const.NI_KAIME_SYORI_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getNikaimesyorikosuu());
            // 2回目良品個数
            case GXHDO101B046Const.NI_KAIME_RYOHIN_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getNikaimeryouhinkosuu());
            // 2回目ＮＧ１数
            case GXHDO101B046Const.NI_KAIME_NG1_SU:
                return StringUtil.nullToBlank(srGaikankensaData.getNikaimeNg1suu());
            // 2回目ＮＧ２数
            case GXHDO101B046Const.NI_KAIME_NG2_SU:
                return StringUtil.nullToBlank(srGaikankensaData.getNikaimeNg2suu());
            // 2回目歩留まり
            case GXHDO101B046Const.NI_KAIME_BUDOMARI:
                return StringUtil.nullToBlank(srGaikankensaData.getNikaimebudomari());
            // 2回目未処理・ﾘﾃｽﾄ個数
            case GXHDO101B046Const.NI_KAIME_MISYORI_RETEST_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getNikaimemisyori());
            // 3回目処理個数
            case GXHDO101B046Const.SAN_KAIME_SYORI_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getSankaimesyorikosuu());
            // 3回目良品個数
            case GXHDO101B046Const.SAN_KAIME_RYOHIN_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getSankaimeryouhinkosuu());
            // 3回目ＮＧ１数
            case GXHDO101B046Const.SAN_KAIME_NG1_SU:
                return StringUtil.nullToBlank(srGaikankensaData.getSankaimeNg1suu());
            // 3回目ＮＧ２数
            case GXHDO101B046Const.SAN_KAIME_NG2_SU:
                return StringUtil.nullToBlank(srGaikankensaData.getSankaimeNg2suu());
            // 3回目歩留まり
            case GXHDO101B046Const.SAN_KAIME_BUDOMARI:
                return StringUtil.nullToBlank(srGaikankensaData.getSankaimebudomari());
            // 3回目未処理・ﾘﾃｽﾄ個数
            case GXHDO101B046Const.SAN_KAIME_MISYORI_RETEST_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getSankaimemisyori());
            // 合計処理個数
            case GXHDO101B046Const.GOKEI_SYORI_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getGoukeisyorikosuu());
            // 良品総重量
            case GXHDO101B046Const.RYOHIN_SOUJURYO:
                return StringUtil.nullToBlank(srGaikankensaData.getRyouhinsoujyuuryou());
            // 合計良品個数
            case GXHDO101B046Const.GOKEI_RYOHIN_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getGoukeiryouhinkosuu());
            // NG総重量
            case GXHDO101B046Const.NG_SOUJURYO:
                return StringUtil.nullToBlank(srGaikankensaData.getNgsoujyuuryou());
            // 合計ＮＧ数
            case GXHDO101B046Const.GOKEI_NGSU:
                return StringUtil.nullToBlank(srGaikankensaData.getGoukeingsuu());
            // 合計歩留まり
            case GXHDO101B046Const.GOKEI_BUDOMARI:
                return StringUtil.nullToBlank(srGaikankensaData.getGoukeibudomari());
            // 合計未処理・ﾘﾃｽﾄ個数
            case GXHDO101B046Const.GOKEI_MISYORI_RETEST_KOSU:
                return StringUtil.nullToBlank(srGaikankensaData.getGoukeimisyori());
            // 未検査率
            case GXHDO101B046Const.MIKENSARITSU:
                return StringUtil.nullToBlank(srGaikankensaData.getMikennsaritu());
            // 検査終了日
            case GXHDO101B046Const.KENSA_SHURYO_DAY:
                return DateUtil.formattedTimestamp(srGaikankensaData.getKensasyuuryounichiji(), "yyMMdd");
            // 検査終了時間
            case GXHDO101B046Const.KENSA_SHURYO_TIME:
                return DateUtil.formattedTimestamp(srGaikankensaData.getKensasyuuryounichiji(), "HHmm");
            // 検査終了担当者
            case GXHDO101B046Const.KENSA_SHURYO_TANTOUSYA:
                return StringUtil.nullToBlank(srGaikankensaData.getKensasyuuryoutantousya());
            // 検査終了認定者
            case GXHDO101B046Const.KENSA_SHURYO_NINTEISYA:
                return StringUtil.nullToBlank(srGaikankensaData.getKensasyuuryouninteisya());
            // QA外観抜き取り検査
            case GXHDO101B046Const.QAGAIKAN_NUKITORIKENSA:
                return StringUtil.nullToBlank(srGaikankensaData.getQagaikannukitorikensa());
            // 備考1
            case GXHDO101B046Const.BIKOU1:
                return StringUtil.nullToBlank(srGaikankensaData.getBikou1());
            // 備考2
            case GXHDO101B046Const.BIKOU2:
                return StringUtil.nullToBlank(srGaikankensaData.getBikou2());
            default:
                return null;

        }
    }

    /**
     * 外観検査_仮登録(tmp_sr_gaikankensa)登録処理(削除時)
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
    private void insertDeleteDataTmpSrGaikankensa(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_gaikankensa ("
                + " kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gaikankensasyurui,kensabasyo,kensasyurui,kensagouki,kensafileno,kensamen,kensakaishinichiji,kensakaishitantousya,kensakaishininteisya,"
                + "10Kcheckmikensa,10Kcheckcamerasa,1kaimesyorikosuu,1kaimeryouhinkosuu,1kaimeNG1suu,1kaimeNG2suu,1kaimebudomari,1kaimemisyori,2kaimesyorikosuu,"
                + "2kaimeryouhinkosuu,2kaimeNG1suu,2kaimeNG2suu,2kaimebudomari,2kaimemisyori,3kaimesyorikosuu,3kaimeryouhinkosuu,3kaimeNG1suu,3kaimeNG2suu,"
                + "3kaimebudomari,3kaimemisyori,goukeisyorikosuu,ryouhinsoujyuuryou,goukeiryouhinkosuu,NGsoujyuuryou,goukeiNGsuu,goukeibudomari,goukeimisyori,"
                + "mikennsaritu,kensasyuuryounichiji,kensasyuuryoutantousya,kensasyuuryouninteisya,QAgaikannukitorikensa,bikou1,bikou2,tourokunichiji,koushinnichiji,revision,deleteflag"
                + ") SELECT "
                + " kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,ownercode,lotkubuncode,atokouteisijinaiyou,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,"
                + "gaikankensasyurui,kensabasyo,kensasyurui,kensagouki,kensafileno,kensamen,kensakaishinichiji,kensakaishitantousya,kensakaishininteisya,"
                + "10Kcheckmikensa,10Kcheckcamerasa,1kaimesyorikosuu,1kaimeryouhinkosuu,1kaimeNG1suu,1kaimeNG2suu,1kaimebudomari,1kaimemisyori,2kaimesyorikosuu,"
                + "2kaimeryouhinkosuu,2kaimeNG1suu,2kaimeNG2suu,2kaimebudomari,2kaimemisyori,3kaimesyorikosuu,3kaimeryouhinkosuu,3kaimeNG1suu,3kaimeNG2suu,"
                + "3kaimebudomari,3kaimemisyori,goukeisyorikosuu,ryouhinsoujyuuryou,goukeiryouhinkosuu,NGsoujyuuryou,goukeiNGsuu,goukeibudomari,goukeimisyori,"
                + "mikennsaritu,kensasyuuryounichiji,kensasyuuryoutantousya,kensasyuuryouninteisya,QAgaikannukitorikensa,bikou1,bikou2,?,?,?,?"
                + " FROM sr_gaikankensa "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? ";

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
        params.add(jissekino); //回数

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 歩留まりを計算し値を項目に値をセットする。
     *
     * @param itemBudomari 歩留まり項目
     * @param itemRyohinkosu 良品個数項目
     * @param itemSyorikosu 処理個数項目
     */
    private void calcBudomari(FXHDD01 itemBudomari, FXHDD01 itemRyohinkosu, FXHDD01 itemSyorikosu) {
        try {
            // 項目が存在しない場合、リターン
            if (itemBudomari == null || itemRyohinkosu == null || itemSyorikosu == null) {
                return;
            }

            BigDecimal syorikosu = new BigDecimal(itemSyorikosu.getValue());
            BigDecimal ryohinKosu = new BigDecimal(itemRyohinkosu.getValue());

            // 処理個数、良品個数の値のいずれかが0以下の場合、リターン
            if (0 < BigDecimal.ZERO.compareTo(ryohinKosu) || 0 <= BigDecimal.ZERO.compareTo(syorikosu)) {
                return;
            }

            //良品個数 / 処理個数 * 100(小数点第三位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal budomari = ryohinKosu.multiply(BigDecimal.valueOf(100)).divide(syorikosu, 2, RoundingMode.HALF_UP);

            //計算結果を歩留まりにセット
            itemBudomari.setValue(budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }
    }

    /**
     * 合計計算(対象項目に値の合計値を設定する。)
     *
     * @param sumItem 合計項目
     * @param addItemList 追加対象
     */
    private void calcSumData(FXHDD01 sumItem, List<FXHDD01> addItemList) {

        // 項目が存在しない場合、リターン
        if (sumItem == null) {
            return;
        }

        BigDecimal sumData = BigDecimal.ZERO;
        int addCount = 0;
        for (FXHDD01 addItem : addItemList) {
            try {
                sumData = sumData.add(new BigDecimal(addItem.getValue()));
                addCount++;
            } catch (NullPointerException | NumberFormatException ex) {
                // 数値変換できない場合は処理なし
            }
        }

        if (0 < addCount) {
            //計算結果を合計項目にセット
            sumItem.setValue(sumData.toPlainString());
        }
    }

    /**
     * 重量を元に個数を算出しセットする。
     *
     * @param itemKosu 個数
     * @param itemSojuryo 総重量
     * @param itemTanijuryo 単位重量
     */
    private void calcKosuBasedJuryo(FXHDD01 itemKosu, FXHDD01 itemSojuryo, FXHDD01 itemTanijuryo) {
        try {
            // 項目が存在しない場合、リターン
            if (itemKosu == null || itemSojuryo == null || itemTanijuryo == null) {
                return;
            }

            BigDecimal sojuryo = new BigDecimal(itemSojuryo.getValue());//総重量
            BigDecimal taniJuryo = new BigDecimal(itemTanijuryo.getValue());//単位重量

            // 単位重量の値のが0の場合リターン
            if (0 == BigDecimal.ZERO.compareTo(taniJuryo)) {
                return;
            }

            //総重量 / 単位重量(小数1以下を四捨五入)
            BigDecimal gokeiRyohikosu = sojuryo.divide(taniJuryo, 0, RoundingMode.HALF_UP);

            //計算結果を誤差率にセット
            itemKosu.setValue(gokeiRyohikosu.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }
    }

    /**
     * 未検査率を計算してセットする。
     *
     * @param itemMiKensaritsu 未検査率
     * @param itemGokeiMisyoriReTestkosu 合計未処理:ﾘﾃｽﾄ個数
     * @param itemOkuriRyohinsu 送り良品数
     */
    private void calcMikensaritsu(FXHDD01 itemMiKensaritsu, FXHDD01 itemGokeiMisyoriReTestkosu, FXHDD01 itemOkuriRyohinsu) {
        try {
            // 項目が存在しない場合、リターン
            if (itemMiKensaritsu == null || itemGokeiMisyoriReTestkosu == null || itemOkuriRyohinsu == null) {
                return;
            }

            BigDecimal gokeiMisyori = new BigDecimal(itemGokeiMisyoriReTestkosu.getValue());
            BigDecimal okuriRyohinsu = new BigDecimal(itemOkuriRyohinsu.getValue());

            // 送り良品数の値のいずれかが0以下の場合、リターン
            if (0 == BigDecimal.ZERO.compareTo(okuriRyohinsu)) {
                return;
            }

            //合計未処理・ﾘﾃｽﾄ個数 / 送り良品数 * 100(小数点第三位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal mikensaritsu = gokeiMisyori.multiply(BigDecimal.valueOf(100)).divide(okuriRyohinsu, 2, RoundingMode.HALF_UP);

            //計算結果を未検査率にセット
            itemMiKensaritsu.setValue(mikensaritsu.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }
    }

    /**
     * 受入れ総重量の計算前ﾁｪｯｸ
     *
     * @param itemUkeireSojuryo 受入れ総重量
     * @param itemUkeireTanijuryo 合計未処理:ﾘﾃｽﾄ個数
     * @param itemOkuriRyohinsu 送り良品数
     */
    private boolean checkUkeireSojuryo(FXHDD01 itemUkeireSojuryo, FXHDD01 itemOkuriRyohinsu, FXHDD01 itemUkeireTanijuryo) {
        try {
            // 項目が存在しない場合、リターン
            if (itemUkeireSojuryo == null || itemUkeireTanijuryo == null || itemOkuriRyohinsu == null) {
                return false;
            }

            BigDecimal taniJuryo = new BigDecimal(itemUkeireTanijuryo.getValue());
            BigDecimal okuriRyohinsu = new BigDecimal(itemOkuriRyohinsu.getValue());

            // 受入れ単位重量、送り良品数の値のいずれかが0以下の場合、リターン
            if (0 <= BigDecimal.ZERO.compareTo(taniJuryo) || 0 <= BigDecimal.ZERO.compareTo(okuriRyohinsu)) {
                return false;
            }

        } catch (NullPointerException | NumberFormatException ex) {
            return false;
        }
        return true;

    }

    /**
     * 受入れ総重量を計算してセットする。 ※事前にcheckUkeireSojuryoを呼び出してﾁｪｯｸ処理を行うこと
     *
     * @param itemUkeireSojuryo 受入れ総重量
     * @param itemUkeireTanijuryo 合計未処理:ﾘﾃｽﾄ個数
     * @param itemOkuriRyohinsu 送り良品数
     */
    private void calcUkeireSojuryo(FXHDD01 itemUkeireSojuryo, FXHDD01 itemOkuriRyohinsu, FXHDD01 itemUkeireTanijuryo) {
        try {
            BigDecimal taniJuryo = new BigDecimal(itemUkeireTanijuryo.getValue());
            BigDecimal okuriRyohinsu = new BigDecimal(itemOkuriRyohinsu.getValue());

            //「送り良品数」　÷　100　×　「受入れ単位重量」 → 式を変換して先に「受入れ単位重量」を乗算
            BigDecimal budomari = okuriRyohinsu.multiply(taniJuryo).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            //計算結果を未検査率にセット
            itemUkeireSojuryo.setValue(budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            //処理なし
        }
    }

}
