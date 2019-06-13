/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import jp.co.kccs.xhd.db.model.SrCutcheck;
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
 * 変更日	2019/05/25<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B012(生品質検査)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/05/25
 */
public class GXHDO101B012 implements IFormLogic {

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

            //チェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_TOP,
                    GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_BOTTOM,
                    GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_TOP,
                    GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_BOTTOM,
                    GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_TOP,
                    GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_BOTTOM,
                    GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_TOP,
                    GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_BOTTOM,
                    GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_TOP,
                    GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_BOTTOM,
                    GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_TOP,
                    GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_BOTTOM,
                    GXHDO101B012Const.BTN_BUDOMARI_KEISAN_TOP,
                    GXHDO101B012Const.BTN_BUDOMARI_KEISAN_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B012Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B012Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B012Const.BTN_INSERT_TOP,
                    GXHDO101B012Const.BTN_INSERT_BOTTOM,
                    GXHDO101B012Const.BTN_DELETE_TOP,
                    GXHDO101B012Const.BTN_DELETE_BOTTOM,
                    GXHDO101B012Const.BTN_UPDATE_TOP,
                    GXHDO101B012Const.BTN_UPDATE_BOTTOM));

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

                // 生品質検査_仮登録登録処理
                insertTmpSrCutcheck(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            } else {

                // 生品質検査_仮登録更新処理
                updateTmpSrCutcheck(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());
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

        // ばらし方法が「"円筒発泡" or "ホットプレート発泡」の場合、条件が未入力はエラー
        FXHDD01 barashiHouhou = getItemRow(processData.getItemList(), GXHDO101B012Const.BARASHI_HOUHOU); //ばらし方法
        if ("円筒発泡".equals(barashiHouhou.getValue()) || "ホットプレート発泡".equals(barashiHouhou.getValue())) {
            FXHDD01 joken = getItemRow(processData.getItemList(), GXHDO101B012Const.JOKEN); //条件
            if (StringUtil.isEmpty(joken.getValue())) {
                return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, Arrays.asList(joken), joken.getLabel1());
            }
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 外観検査開始日時、外観検査終了日時前後チェック
        FXHDD01 gaikankensaKaishiDay = getItemRow(processData.getItemList(), GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY); //外観検査開始日
        FXHDD01 gaikankensaKaishiTime = getItemRow(processData.getItemList(), GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME); //外観検査開始時刻
        Date gaikankensaKaishiDate = DateUtil.convertStringToDate(gaikankensaKaishiDay.getValue(), gaikankensaKaishiTime.getValue());
        FXHDD01 gaikankensaShuryouDay = getItemRow(processData.getItemList(), GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY); //外観検査終了日
        FXHDD01 gaikankensaShuryouTime = getItemRow(processData.getItemList(), GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME); //外観検査終了時刻
        Date gaikankensaShuryoDate = DateUtil.convertStringToDate(gaikankensaShuryouDay.getValue(), gaikankensaShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(gaikankensaKaishiDay.getLabel1(), gaikankensaKaishiDate, gaikankensaShuryouDay.getLabel1(), gaikankensaShuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(gaikankensaKaishiDay, gaikankensaKaishiTime, gaikankensaShuryouDay, gaikankensaShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // ばらし開始日時、ばらし終了日時前後チェック
        FXHDD01 barashiKaishiDay = getItemRow(processData.getItemList(), GXHDO101B012Const.BARASHI_KAISHI_DAY); //開始日
        FXHDD01 barashiKaishiTime = getItemRow(processData.getItemList(), GXHDO101B012Const.BARASHI_KAISHI_TIME); //開始時刻
        Date barashiKaishiDate = DateUtil.convertStringToDate(barashiKaishiDay.getValue(), barashiKaishiTime.getValue());
        FXHDD01 barashiShuryouDay = getItemRow(processData.getItemList(), GXHDO101B012Const.BARASHI_SHURYOU_DAY); //終了日
        FXHDD01 barashiShuryouTime = getItemRow(processData.getItemList(), GXHDO101B012Const.BARASHI_SHURYOU_TIME); //終了時刻
        Date barashiShuryoDate = DateUtil.convertStringToDate(barashiShuryouDay.getValue(), barashiShuryouTime.getValue());
        //R001チェック呼出し
        msgCheckR001 = validateUtil.checkR001(barashiKaishiDay.getLabel1(), barashiKaishiDate, barashiShuryouDay.getLabel1(), barashiShuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(barashiKaishiDay, barashiKaishiTime, barashiShuryouDay, barashiShuryouTime);
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
            SrCutcheck tmpSrCutcheck = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrCutcheck> srCutcheckList = getSrCutcheckData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srCutcheckList.isEmpty()) {
                    tmpSrCutcheck = srCutcheckList.get(0);
                }

                deleteTmpSrCutcheck(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 生品質検査_登録処理
            insertSrCutcheck(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrCutcheck);

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
        processData.setUserAuthParam(GXHDO101B012Const.USER_AUTH_UPDATE_PARAM);

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

            // 生品質検査_更新処理
            updateSrCutcheck(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B012Const.USER_AUTH_DELETE_PARAM);

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

            // 生品質検査_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrCutcheck(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 生品質検査_削除処理
            deleteSrCutcheck(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_TOP,
                        GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_TOP,
                        GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_TOP,
                        GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_TOP,
                        GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_TOP,
                        GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_BOTTOM,
                        GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_TOP,
                        GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_BOTTOM,
                        GXHDO101B012Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B012Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B012Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B012Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B012Const.BTN_UPDATE_TOP,
                        GXHDO101B012Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B012Const.BTN_DELETE_TOP,
                        GXHDO101B012Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B012Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B012Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B012Const.BTN_INSERT_BOTTOM,
                        GXHDO101B012Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_TOP,
                        GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_TOP,
                        GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_TOP,
                        GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_TOP,
                        GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_BOTTOM,
                        GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_TOP,
                        GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_BOTTOM,
                        GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_TOP,
                        GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_BOTTOM,
                        GXHDO101B012Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B012Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B012Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B012Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B012Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B012Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B012Const.BTN_INSERT_TOP,
                        GXHDO101B012Const.BTN_INSERT_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B012Const.BTN_DELETE_TOP,
                        GXHDO101B012Const.BTN_DELETE_BOTTOM,
                        GXHDO101B012Const.BTN_UPDATE_TOP,
                        GXHDO101B012Const.BTN_UPDATE_BOTTOM
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
            case GXHDO101B012Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B012Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B012Const.BTN_INSERT_TOP:
            case GXHDO101B012Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B012Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B012Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B012Const.BTN_UPDATE_TOP:
            case GXHDO101B012Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B012Const.BTN_DELETE_TOP:
            case GXHDO101B012Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 外観検査開始日時
            case GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_TOP:
            case GXHDO101B012Const.BTN_GAIKANKENSA_STARTDATETIME_BOTTOM:
                method = "setGaikankensaStartDateTime";
                break;
            // 外観検査終了日時
            case GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_TOP:
            case GXHDO101B012Const.BTN_GAIKANKENSA_ENDDATETIME_BOTTOM:
                method = "setGaikankensaEndDateTime";
                break;
            // ばらし開始日時
            case GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_TOP:
            case GXHDO101B012Const.BTN_BARASHI_STARTDATETIME_BOTTOM:
                method = "setBarashiStartDateTime";
                break;
            // ばらし終了日時
            case GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_TOP:
            case GXHDO101B012Const.BTN_BARASHI_ENDDATETIME_BOTTOM:
                method = "setBarashiEndDateTime";
                break;
            // 処理個数計算
            case GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_TOP:
            case GXHDO101B012Const.BTN_SHORIKOSU_KEISAN_BOTTOM:
                method = "calculatShorikosu";
                break;
            // 良品個数計算
            case GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_TOP:
            case GXHDO101B012Const.BTN_RYOUHINKOSU_KEISAN_BOTTOM:
                method = "calculatRyohinkosu";
                break;
            // 歩留まり計算
            case GXHDO101B012Const.BTN_BUDOMARI_KEISAN_TOP:
            case GXHDO101B012Const.BTN_BUDOMARI_KEISAN_BOTTOM:
                method = "calculatBudomari";
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
        this.setItemData(processData, GXHDO101B012Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B012Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B012Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B012Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B012Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B012Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B012Const.OWNER, ownercode + ":" + owner);
        }
        
        // セット数(前工程情報をセットする。)
        FXHDD01 itemRowSetsu = this.getItemRow(processData.getItemList(), GXHDO101B012Const.SETSU);
        CommonUtil.setMaekoteiInfo(itemRowSetsu, maekoteiInfo, "RyouhinSetsuu", true, true);

        // 指示
        this.setItemData(processData, GXHDO101B012Const.SIJI, "");

        //取個数
        this.setItemData(processData, GXHDO101B012Const.TORIKOSU, StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu")));

        //生T寸
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN, "");

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

        List<SrCutcheck> srCutcheckDataList = new ArrayList<>();
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

                return true;
            }

            // 生品質検査データ取得
            srCutcheckDataList = getSrCutcheckData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srCutcheckDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srCutcheckDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srCutcheckDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srCutcheckData 生品質検査データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrCutcheck srCutcheckData) {
        //外観検査開始日
        this.setItemData(processData, GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY, getSrCutcheckItemData(GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY, srCutcheckData));
        //外観検査開始時間
        this.setItemData(processData, GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME, getSrCutcheckItemData(GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME, srCutcheckData));
        //外観検査開始担当者
        this.setItemData(processData, GXHDO101B012Const.GAIKANKENSA_KAISHI_TANTOUSHA, getSrCutcheckItemData(GXHDO101B012Const.GAIKANKENSA_KAISHI_TANTOUSHA, srCutcheckData));
        //外観検査終了日
        this.setItemData(processData, GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY, getSrCutcheckItemData(GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY, srCutcheckData));
        //外観検査終了時間
        this.setItemData(processData, GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME, getSrCutcheckItemData(GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME, srCutcheckData));
        //外観検査終了担当者
        this.setItemData(processData, GXHDO101B012Const.GAIKANKENSA_SHURYOU_TANTOUSHA, getSrCutcheckItemData(GXHDO101B012Const.GAIKANKENSA_SHURYOU_TANTOUSHA, srCutcheckData));
        //ばらし方法
        this.setItemData(processData, GXHDO101B012Const.BARASHI_HOUHOU, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_HOUHOU, srCutcheckData));
        //条件
        this.setItemData(processData, GXHDO101B012Const.JOKEN, getSrCutcheckItemData(GXHDO101B012Const.JOKEN, srCutcheckData));
        //ばらし開始日
        this.setItemData(processData, GXHDO101B012Const.BARASHI_KAISHI_DAY, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_KAISHI_DAY, srCutcheckData));
        //ばらし開始時間
        this.setItemData(processData, GXHDO101B012Const.BARASHI_KAISHI_TIME, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_KAISHI_TIME, srCutcheckData));
        //ばらし開始担当者
        this.setItemData(processData, GXHDO101B012Const.BARASHI_KAISHI_TANTOUSHA, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_KAISHI_TANTOUSHA, srCutcheckData));
        //ばらし終了日
        this.setItemData(processData, GXHDO101B012Const.BARASHI_SHURYOU_DAY, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_SHURYOU_DAY, srCutcheckData));
        //ばらし終了時間
        this.setItemData(processData, GXHDO101B012Const.BARASHI_SHURYOU_TIME, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_SHURYOU_TIME, srCutcheckData));
        //ばらし終了担当者
        this.setItemData(processData, GXHDO101B012Const.BARASHI_SHURYOU_TANTOUSHA, getSrCutcheckItemData(GXHDO101B012Const.BARASHI_SHURYOU_TANTOUSHA, srCutcheckData));
        //単位重量
        this.setItemData(processData, GXHDO101B012Const.TANIJURYO, getSrCutcheckItemData(GXHDO101B012Const.TANIJURYO, srCutcheckData));
        //総重量
        this.setItemData(processData, GXHDO101B012Const.SOJURYO, getSrCutcheckItemData(GXHDO101B012Const.SOJURYO, srCutcheckData));
        //粉まぶし
        this.setItemData(processData, GXHDO101B012Const.KONAMABUSHI, getSrCutcheckItemData(GXHDO101B012Const.KONAMABUSHI, srCutcheckData));
        //生T寸 1
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN1, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN1, srCutcheckData));
        //生T寸 2
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN2, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN2, srCutcheckData));
        //生T寸 3
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN3, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN3, srCutcheckData));
        //生T寸 4
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN4, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN4, srCutcheckData));
        //生T寸 5
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN5, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN5, srCutcheckData));
        //生T寸 6
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN6, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN6, srCutcheckData));
        //生T寸 7
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN7, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN7, srCutcheckData));
        //生T寸 8
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN8, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN8, srCutcheckData));
        //生T寸 9
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN9, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN9, srCutcheckData));
        //生T寸 10
        this.setItemData(processData, GXHDO101B012Const.NAMA_T_SUN10, getSrCutcheckItemData(GXHDO101B012Const.NAMA_T_SUN10, srCutcheckData));
        //処理個数
        this.setItemData(processData, GXHDO101B012Const.SHORIKOSU, getSrCutcheckItemData(GXHDO101B012Const.SHORIKOSU, srCutcheckData));
        //良品個数
        this.setItemData(processData, GXHDO101B012Const.RYOHINKOSU, getSrCutcheckItemData(GXHDO101B012Const.RYOHINKOSU, srCutcheckData));
        //歩留まり
        this.setItemData(processData, GXHDO101B012Const.BUDOMARI, getSrCutcheckItemData(GXHDO101B012Const.BUDOMARI, srCutcheckData));
        //備考1
        this.setItemData(processData, GXHDO101B012Const.BIKOU1, getSrCutcheckItemData(GXHDO101B012Const.BIKOU1, srCutcheckData));
        //備考2
        this.setItemData(processData, GXHDO101B012Const.BIKOU2, getSrCutcheckItemData(GXHDO101B012Const.BIKOU2, srCutcheckData));

    }

    /**
     * 生品質検査の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 生品質検査登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrCutcheck> getSrCutcheckData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrCutcheck(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrCutcheck(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        String sql = "SELECT kcpno, oyalotedaban, torikosuu, lotkubuncode, ownercode, tokuisaki"
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
     * [生品質検査]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrCutcheck> loadSrCutcheck(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,Atumi02,"
                + "Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,Soujyuryo,"
                + "Tanijyuryo,gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,barashiendnichiji,"
                + "barashiendtantousya,konamabushi,syorisetsuu,ryouhinsetsuu,budomari,torokunichiji,kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sr_cutcheck "
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
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("startdatetime", "startdatetime"); //開始日時
        mapping.put("enddatetime", "enddatetime"); //終了日時
        mapping.put("tantousya", "tantousya"); //担当者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("AtumiMin", "atumimin"); //厚みMIN
        mapping.put("AtumiMax", "atumimax"); //厚みMAX
        mapping.put("Atumi01", "atumi01"); //厚み01
        mapping.put("Atumi02", "atumi02"); //厚み02
        mapping.put("Atumi03", "atumi03"); //厚み03
        mapping.put("Atumi04", "atumi04"); //厚み04
        mapping.put("Atumi05", "atumi05"); //厚み05
        mapping.put("Atumi06", "atumi06"); //厚み06
        mapping.put("Atumi07", "atumi07"); //厚み07
        mapping.put("Atumi08", "atumi08"); //厚み08
        mapping.put("Atumi09", "atumi09"); //厚み09
        mapping.put("Atumi10", "atumi10"); //厚み10
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("Soujyuryo", "soujyuryo"); //総重量
        mapping.put("Tanijyuryo", "tanijyuryo"); //単位重量
        mapping.put("gaikankensatantousya", "gaikankensatantousya"); //外観検査終了担当者
        mapping.put("barasshi", "barasshi"); //ばらし方法
        mapping.put("joken", "joken"); //条件
        mapping.put("barashistartnichiji", "barashistartnichiji"); //ばらし開始日時
        mapping.put("batashistarttantousya", "batashistarttantousya"); //ばらし開始担当者
        mapping.put("barashiendnichiji", "barashiendnichiji"); //ばらし終了日時
        mapping.put("barashiendtantousya", "barashiendtantousya"); //ばらし終了担当者
        mapping.put("konamabushi", "konamabushi"); //粉まぶし
        mapping.put("syorisetsuu", "syorisetsuu"); //処理個数
        mapping.put("ryouhinsetsuu", "ryouhinsetsuu"); //良品個数
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrCutcheck>> beanHandler = new BeanListHandler<>(SrCutcheck.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [生品質検査_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrCutcheck> loadTmpSrCutcheck(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT "
                + "kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,Atumi02,"
                + "Atumi03,Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,Soujyuryo,"
                + "Tanijyuryo,gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,barashiendnichiji,"
                + "barashiendtantousya,konamabushi,syorisetsuu,ryouhinsetsuu,budomari,torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sr_cutcheck "
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
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("startdatetime", "startdatetime"); //開始日時
        mapping.put("enddatetime", "enddatetime"); //終了日時
        mapping.put("tantousya", "tantousya"); //担当者
        mapping.put("kakuninsya", "kakuninsya"); //確認者
        mapping.put("AtumiMin", "atumimin"); //厚みMIN
        mapping.put("AtumiMax", "atumimax"); //厚みMAX
        mapping.put("Atumi01", "atumi01"); //厚み01
        mapping.put("Atumi02", "atumi02"); //厚み02
        mapping.put("Atumi03", "atumi03"); //厚み03
        mapping.put("Atumi04", "atumi04"); //厚み04
        mapping.put("Atumi05", "atumi05"); //厚み05
        mapping.put("Atumi06", "atumi06"); //厚み06
        mapping.put("Atumi07", "atumi07"); //厚み07
        mapping.put("Atumi08", "atumi08"); //厚み08
        mapping.put("Atumi09", "atumi09"); //厚み09
        mapping.put("Atumi10", "atumi10"); //厚み10
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("Soujyuryo", "soujyuryo"); //総重量
        mapping.put("Tanijyuryo", "tanijyuryo"); //単位重量
        mapping.put("gaikankensatantousya", "gaikankensatantousya"); //外観検査終了担当者
        mapping.put("barasshi", "barasshi"); //ばらし方法
        mapping.put("joken", "joken"); //条件
        mapping.put("barashistartnichiji", "barashistartnichiji"); //ばらし開始日時
        mapping.put("batashistarttantousya", "batashistarttantousya"); //ばらし開始担当者
        mapping.put("barashiendnichiji", "barashiendnichiji"); //ばらし終了日時
        mapping.put("barashiendtantousya", "barashiendtantousya"); //ばらし終了担当者
        mapping.put("konamabushi", "konamabushi"); //粉まぶし
        mapping.put("syorisetsuu", "syorisetsuu"); //処理個数
        mapping.put("ryouhinsetsuu", "ryouhinsetsuu"); //良品個数
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrCutcheck>> beanHandler = new BeanListHandler<>(SrCutcheck.class, rowProcessor);

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

            // 生品質検査データ取得
            List<SrCutcheck> srCutcheckDataList = getSrCutcheckData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srCutcheckDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srCutcheckDataList.get(0));

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
     * @param srCutcheckData 生品質検査
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrCutcheck srCutcheckData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srCutcheckData != null) {
            // 元データが存在する場合元データより取得
            return getSrCutcheckItemData(itemId, srCutcheckData);
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
     * 生品質検査_仮登録(tmp_sr_cutcheck)登録処理
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
    private void insertTmpSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_cutcheck ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,Atumi02,Atumi03,"
                + "Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,Soujyuryo,Tanijyuryo,"
                + "gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,barashiendnichiji,barashiendtantousya,"
                + "konamabushi,syorisetsuu,ryouhinsetsuu,budomari,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrCutcheck(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());

    }

    /**
     * 生品質検査_仮登録(tmp_sr_cutcheck)更新処理
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
    private void updateTmpSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_cutcheck SET "
                + "startdatetime = ?,enddatetime = ?,tantousya = ?,Atumi01 = ?,Atumi02 = ?,Atumi03 = ?,Atumi04 = ?,Atumi05 = ?,Atumi06 = ?,Atumi07 = ?,"
                + "Atumi08 = ?,Atumi09 = ?,Atumi10 = ?,bikou1 = ?,bikou2 = ?,Soujyuryo = ?,Tanijyuryo = ?,gaikankensatantousya = ?,barasshi = ?,joken = ?,"
                + "barashistartnichiji = ?,batashistarttantousya = ?,barashiendnichiji = ?,barashiendtantousya = ?,konamabushi = ?,syorisetsuu = ?,"
                + "ryouhinsetsuu = ?,budomari = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrCutcheck> srCutcheckList = getSrCutcheckData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrCutcheck srCutcheck = null;
        if (!srCutcheckList.isEmpty()) {
            srCutcheck = srCutcheckList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrCutcheck(false, newRev, 0, "", "", "", systemTime, itemList, srCutcheck);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 生品質検査_仮登録(tmp_sr_cutcheck)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_cutcheck "
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
     * 生品質検査_仮登録(tmp_sr_cutcheck)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srCutcheckData 生品質検査データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrCutcheck(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrCutcheck srCutcheckData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME, srCutcheckData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME, srCutcheckData))); //終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_KAISHI_TANTOUSHA, srCutcheckData)));  //担当者

        if (isInsert) {
            params.add(null); //確認者
            params.add(null); //厚みMIN
            params.add(null); //厚みMAX
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN1, srCutcheckData)));  //厚み01
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN2, srCutcheckData)));  //厚み02
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN3, srCutcheckData)));  //厚み03
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN4, srCutcheckData)));  //厚み04
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN5, srCutcheckData)));  //厚み05
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN6, srCutcheckData)));  //厚み06
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN7, srCutcheckData)));  //厚み07
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN8, srCutcheckData)));  //厚み08
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN9, srCutcheckData)));  //厚み09
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN10, srCutcheckData)));  //厚み10
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.BIKOU1, srCutcheckData)));  //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.BIKOU2, srCutcheckData)));  //備考2
        if (isInsert) {
            params.add(null); //備考3
            params.add(null); //備考4
            params.add(null); //備考5
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.SOJURYO, srCutcheckData)));  //総重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.TANIJURYO, srCutcheckData)));  //単位重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_SHURYOU_TANTOUSHA, srCutcheckData)));  //外観検査終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.BARASHI_HOUHOU, srCutcheckData)));  //ばらし方法
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.JOKEN, srCutcheckData)));  //条件
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B012Const.BARASHI_KAISHI_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.BARASHI_KAISHI_TIME, srCutcheckData))); //ばらし開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.BARASHI_KAISHI_TANTOUSHA, srCutcheckData)));  //ばらし開始担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B012Const.BARASHI_SHURYOU_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.BARASHI_SHURYOU_TIME, srCutcheckData))); //ばらし終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.BARASHI_SHURYOU_TANTOUSHA, srCutcheckData)));  //ばらし終了担当者
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B012Const.KONAMABUSHI, srCutcheckData), null));  //粉まぶし
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.SHORIKOSU, srCutcheckData)));  //処理個数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.RYOHINKOSU, srCutcheckData)));  //良品個数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B012Const.BUDOMARI, srCutcheckData)));  //歩留まり
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 生品質検査(sr_cutcheck)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrCutcheck 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrCutcheck tmpSrCutcheck) throws SQLException {

        String sql = "INSERT INTO sr_cutcheck ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,Atumi02,Atumi03,"
                + "Atumi04,Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,Soujyuryo,Tanijyuryo,"
                + "gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,barashiendnichiji,barashiendtantousya,"
                + "konamabushi,syorisetsuu,ryouhinsetsuu,budomari,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrCutcheck(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrCutcheck);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 生品質検査(sr_cutcheck)更新処理
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
    private void updateSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_cutcheck SET "
                + "startdatetime = ?,enddatetime = ?,tantousya = ?,Atumi01 = ?,Atumi02 = ?,Atumi03 = ?,Atumi04 = ?,Atumi05 = ?,Atumi06 = ?,Atumi07 = ?,"
                + "Atumi08 = ?,Atumi09 = ?,Atumi10 = ?,bikou1 = ?,bikou2 = ?,Soujyuryo = ?,Tanijyuryo = ?,gaikankensatantousya = ?,barasshi = ?,joken = ?,"
                + "barashistartnichiji = ?,batashistarttantousya = ?,barashiendnichiji = ?,barashiendtantousya = ?,konamabushi = ?,syorisetsuu = ?,"
                + "ryouhinsetsuu = ?,budomari = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrCutcheck> srCutcheckList = getSrCutcheckData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrCutcheck srCutcheck = null;
        if (!srCutcheckList.isEmpty()) {
            srCutcheck = srCutcheckList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrCutcheck(false, newRev, "", "", "", systemTime, itemList, srCutcheck);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 生品質検査(sr_cutcheck)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srCutcheckData 生品質検査データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrCutcheck(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrCutcheck srCutcheckData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME, srCutcheckData))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME, srCutcheckData))); //終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_KAISHI_TANTOUSHA, srCutcheckData)));  //担当者

        if (isInsert) {
            params.add(""); //確認者
            params.add(0); //厚みMIN
            params.add(0); //厚みMAX
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN1, srCutcheckData)));  //厚み01
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN2, srCutcheckData)));  //厚み02
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN3, srCutcheckData)));  //厚み03
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN4, srCutcheckData)));  //厚み04
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN5, srCutcheckData)));  //厚み05
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN6, srCutcheckData)));  //厚み06
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN7, srCutcheckData)));  //厚み07
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN8, srCutcheckData)));  //厚み08
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN9, srCutcheckData)));  //厚み09
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.NAMA_T_SUN10, srCutcheckData)));  //厚み10
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.BIKOU1, srCutcheckData)));  //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.BIKOU2, srCutcheckData)));  //備考2
        if (isInsert) {
            params.add(""); //備考3
            params.add(""); //備考4
            params.add(""); //備考5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.SOJURYO, srCutcheckData)));  //総重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.TANIJURYO, srCutcheckData)));  //単位重量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.GAIKANKENSA_SHURYOU_TANTOUSHA, srCutcheckData)));  //外観検査終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.BARASHI_HOUHOU, srCutcheckData)));  //ばらし方法
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B012Const.JOKEN, srCutcheckData)));  //条件
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B012Const.BARASHI_KAISHI_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.BARASHI_KAISHI_TIME, srCutcheckData))); //ばらし開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.BARASHI_KAISHI_TANTOUSHA, srCutcheckData)));  //ばらし開始担当者
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B012Const.BARASHI_SHURYOU_DAY, srCutcheckData),
                getItemData(itemList, GXHDO101B012Const.BARASHI_SHURYOU_TIME, srCutcheckData))); //ばらし終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B012Const.BARASHI_SHURYOU_TANTOUSHA, srCutcheckData)));  //ばらし終了担当者
        params.add(getComboAriNashiValue(getItemData(itemList, GXHDO101B012Const.KONAMABUSHI, srCutcheckData), 9));  //粉まぶし
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B012Const.SHORIKOSU, srCutcheckData)));  //処理個数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B012Const.RYOHINKOSU, srCutcheckData)));  //良品個数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B012Const.BUDOMARI, srCutcheckData)));  //歩留まり
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision

        return params;
    }

    /**
     * 生品質検査(sr_cutcheck)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_cutcheck "
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
     * [生品質検査_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_cutcheck "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
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
     * 外観検査開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setGaikankensaStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 外観検査終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setGaikankensaEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * ばらし開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBarashiStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B012Const.BARASHI_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B012Const.BARASHI_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * ばらし終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBarashiEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B012Const.BARASHI_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B012Const.BARASHI_SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 処理個数計算処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData calculatShorikosu(ProcessData processDate) {

        // 継続メソッドのクリア
        processDate.setMethod("");

        try {
            FXHDD01 itemShorikosu = getItemRow(processDate.getItemList(), GXHDO101B012Const.SHORIKOSU);
            // 処理個数がすでに入力されている場合はリターン
            if (!StringUtil.isEmpty(itemShorikosu.getValue())) {
                return processDate;
            }

            String setsu = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B012Const.SETSU, null)); //セット数
            String torikosu = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B012Const.TORIKOSU, null)); //取個数

            if (StringUtil.isEmpty(setsu) || StringUtil.isEmpty(torikosu)) {
                return processDate;
            }

            // セット数を数値変換
            BigDecimal decSetsu = new BigDecimal(setsu);
            // 取個数を数値変換
            BigDecimal decTorikosu = new BigDecimal(torikosu);
            // 処理個数算出(小数以下四捨五入)
            BigDecimal decShorikosu = decSetsu.multiply(decTorikosu).setScale(0, RoundingMode.HALF_UP);

            // 算出結果を処理個数に設定
            itemShorikosu.setValue(decShorikosu.toPlainString());

        } catch (NumberFormatException e) {
            //数値型変換失敗時はそのままリターン
        }
        return processDate;

    }

    /**
     * 良品個数計算処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData calculatRyohinkosu(ProcessData processDate) {

        // 継続メソッドのクリア
        processDate.setMethod("");

        try {
            FXHDD01 itemRyohinkosu = getItemRow(processDate.getItemList(), GXHDO101B012Const.RYOHINKOSU);
            // 良品個数がすでに入力されている場合はリターン
            if (!StringUtil.isEmpty(itemRyohinkosu.getValue())) {
                return processDate;
            }

            String sojuryo = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B012Const.SOJURYO, null)); //総重量
            String tanijuryo = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B012Const.TANIJURYO, null)); //単位重量

            if (StringUtil.isEmpty(sojuryo) || StringUtil.isEmpty(tanijuryo)) {
                return processDate;
            }

            // 総重量を数値変換
            BigDecimal decSojuryo = new BigDecimal(sojuryo);
            // 単位重量を数値変換
            BigDecimal decTanijuryo = new BigDecimal(tanijuryo);
            if(BigDecimal.ZERO.compareTo(decTanijuryo) == 0){
                return processDate;
            }

            // 良品個数算出(小数以下四捨五入)
            // 総重量 / 単位重量 ※100をかけた後に四捨五入を行うので小数部を3桁は残す
            BigDecimal decRyohinkosu = decSojuryo.divide(decTanijuryo, 3, RoundingMode.DOWN);
            // 除算結果に100を掛けて四捨五入
            decRyohinkosu = decRyohinkosu.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
            // 結果を項目にセット
            itemRyohinkosu.setValue(decRyohinkosu.toPlainString());

        } catch (NumberFormatException e) {
            //数値型変換失敗時はそのままリターン
        }
        return processDate;
    }

    /**
     * 歩留まり計算処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData calculatBudomari(ProcessData processDate) {
        // 継続メソッドのクリア
        processDate.setMethod("");

        try {
            FXHDD01 itemBudomari = getItemRow(processDate.getItemList(), GXHDO101B012Const.BUDOMARI);
            String shorikosu = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B012Const.SHORIKOSU, null)); //処理個数
            String ryohinkosu = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B012Const.RYOHINKOSU, null)); //良品個数

            if (StringUtil.isEmpty(shorikosu) || StringUtil.isEmpty(ryohinkosu)) {
                return processDate;
            }

            // 処理個数を数値変換
            BigDecimal decShorikosu = new BigDecimal(shorikosu);
            if(BigDecimal.ZERO.compareTo(decShorikosu) == 0){
                return processDate;
            }

            // 良品個数を数値変換
            BigDecimal decRyohinsu = new BigDecimal(ryohinkosu);

            // 歩留まり算出(小数以下四捨五入)
            // 良品個数 / 処理個数 ※100をかけた後に四捨五入を行うので小数部を5桁は残す
            BigDecimal decRyohinkosu = decRyohinsu.divide(decShorikosu, 5, RoundingMode.DOWN);
            // 除算結果に100を掛けて四捨五入
            decRyohinkosu = decRyohinkosu.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            // 結果を項目にセット
            itemBudomari.setValue(decRyohinkosu.toPlainString());

        } catch (NumberFormatException e) {
            //数値型変換失敗時はそのままリターン
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
     * @param srCutcheckData 生品質検査データ
     * @return DB値
     */
    private String getSrCutcheckItemData(String itemId, SrCutcheck srCutcheckData) {
        switch (itemId) {
            //外観検査開始日
            case GXHDO101B012Const.GAIKANKENSA_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srCutcheckData.getStartdatetime(), "yyMMdd");
            //外観検査開始時間
            case GXHDO101B012Const.GAIKANKENSA_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srCutcheckData.getStartdatetime(), "HHmm");
            //外観検査開始担当者
            case GXHDO101B012Const.GAIKANKENSA_KAISHI_TANTOUSHA:
                return StringUtil.nullToBlank(srCutcheckData.getTantousya());
            //外観検査終了日
            case GXHDO101B012Const.GAIKANKENSA_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srCutcheckData.getEnddatetime(), "yyMMdd");
            //外観検査終了時間
            case GXHDO101B012Const.GAIKANKENSA_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srCutcheckData.getEnddatetime(), "HHmm");
            //外観検査終了担当者
            case GXHDO101B012Const.GAIKANKENSA_SHURYOU_TANTOUSHA:
                return StringUtil.nullToBlank(srCutcheckData.getGaikankensatantousya());
            //ばらし方法
            case GXHDO101B012Const.BARASHI_HOUHOU:
                return StringUtil.nullToBlank(srCutcheckData.getBarasshi());
            //条件
            case GXHDO101B012Const.JOKEN:
                return StringUtil.nullToBlank(srCutcheckData.getJoken());
            //ばらし開始日
            case GXHDO101B012Const.BARASHI_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srCutcheckData.getBarashistartnichiji(), "yyMMdd");
            //ばらし開始時間
            case GXHDO101B012Const.BARASHI_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srCutcheckData.getBarashistartnichiji(), "HHmm");
            //ばらし開始担当者
            case GXHDO101B012Const.BARASHI_KAISHI_TANTOUSHA:
                return StringUtil.nullToBlank(srCutcheckData.getBatashistarttantousya());
            //ばらし終了日
            case GXHDO101B012Const.BARASHI_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srCutcheckData.getBarashiendnichiji(), "yyMMdd");
            //ばらし終了時間
            case GXHDO101B012Const.BARASHI_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srCutcheckData.getBarashiendnichiji(), "HHmm");
            //ばらし終了担当者
            case GXHDO101B012Const.BARASHI_SHURYOU_TANTOUSHA:
                return StringUtil.nullToBlank(srCutcheckData.getBarashiendtantousya());
            //単位重量
            case GXHDO101B012Const.TANIJURYO:
                return StringUtil.nullToBlank(srCutcheckData.getTanijyuryo());
            //総重量
            case GXHDO101B012Const.SOJURYO:
                return StringUtil.nullToBlank(srCutcheckData.getSoujyuryo());
            //粉まぶし
            case GXHDO101B012Const.KONAMABUSHI:
                return getComboAriNashiText(StringUtil.nullToBlank(srCutcheckData.getKonamabushi()));
            //生T寸 1
            case GXHDO101B012Const.NAMA_T_SUN1:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi01());
            //生T寸 2
            case GXHDO101B012Const.NAMA_T_SUN2:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi02());
            //生T寸 3
            case GXHDO101B012Const.NAMA_T_SUN3:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi03());
            //生T寸 4
            case GXHDO101B012Const.NAMA_T_SUN4:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi04());
            //生T寸 5
            case GXHDO101B012Const.NAMA_T_SUN5:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi05());
            //生T寸 6
            case GXHDO101B012Const.NAMA_T_SUN6:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi06());
            //生T寸 7
            case GXHDO101B012Const.NAMA_T_SUN7:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi07());
            //生T寸 8
            case GXHDO101B012Const.NAMA_T_SUN8:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi08());
            //生T寸 9
            case GXHDO101B012Const.NAMA_T_SUN9:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi09());
            //生T寸 10
            case GXHDO101B012Const.NAMA_T_SUN10:
                return StringUtil.nullToBlank(srCutcheckData.getAtumi10());
            //処理個数
            case GXHDO101B012Const.SHORIKOSU:
                return StringUtil.nullToBlank(srCutcheckData.getSyorisetsuu());
            //良品個数
            case GXHDO101B012Const.RYOHINKOSU:
                return StringUtil.nullToBlank(srCutcheckData.getRyouhinsetsuu());
            //歩留まり
            case GXHDO101B012Const.BUDOMARI:
                return StringUtil.nullToBlank(srCutcheckData.getBudomari());
            //備考1
            case GXHDO101B012Const.BIKOU1:
                return StringUtil.nullToBlank(srCutcheckData.getBikou1());
            //備考2
            case GXHDO101B012Const.BIKOU2:
                return StringUtil.nullToBlank(srCutcheckData.getBikou2());

            default:
                return null;

        }
    }

    /**
     * 生品質検査_仮登録(tmp_sr_cutcheck)登録処理(削除時)
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
    private void insertDeleteDataTmpSrCutcheck(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_cutcheck ("
                + "kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,Atumi02,Atumi03,Atumi04,"
                + "Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,Soujyuryo,Tanijyuryo,"
                + "gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,barashiendnichiji,barashiendtantousya,"
                + "konamabushi,syorisetsuu,ryouhinsetsuu,budomari,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,startdatetime,enddatetime,tantousya,kakuninsya,AtumiMin,AtumiMax,Atumi01,Atumi02,Atumi03,Atumi04,"
                + "Atumi05,Atumi06,Atumi07,Atumi08,Atumi09,Atumi10,bikou1,bikou2,bikou3,bikou4,bikou5,Soujyuryo,Tanijyuryo,"
                + "gaikankensatantousya,barasshi,joken,barashistartnichiji,batashistarttantousya,barashiendnichiji,barashiendtantousya,"
                + "konamabushi,syorisetsuu,ryouhinsetsuu,budomari,?,?,?,? "
                + "FROM sr_cutcheck "
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

}
