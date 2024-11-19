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
import jp.co.kccs.xhd.db.model.SrShinkuukansou;
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.SubSrShinkuukansou;
import jp.co.kccs.xhd.model.GXHDO101C018Model;
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
import jp.co.kccs.xhd.util.NumberUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名 品質情報管理システム<br>
 * <br>
 * 変更日 2020/02/10<br>
 * 計画書No K1811-DS001<br>
 * 変更者 KCCS K.Jo<br>
 * 変更理由 新規作成<br>
 * <br>
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * 変更日	2020/11/23<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	サヤnoサブ画面追加<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B043(電気特性・熱処理)ロジック
 *
 * @author KCSS K.Jo
 * @since 2020/02/10
 */
public class GXHDO101B043 implements IFormLogic {

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
                    GXHDO101B043Const.BTN_START_DATETIME_TOP,
                    GXHDO101B043Const.BTN_END_DATETIME_TOP,
                    GXHDO101B043Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B043Const.BTN_END_DATETIME_BOTTOM,
                    GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_TOP,
                    GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM,
                    GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_TOP,
                    GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B043Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B043Const.BTN_INSERT_TOP,
                    GXHDO101B043Const.BTN_DELETE_TOP,
                    GXHDO101B043Const.BTN_UPDATE_TOP,
                    GXHDO101B043Const.BTN_DATACOOPERATION_TOP,
                    GXHDO101B043Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B043Const.BTN_INSERT_BOTTOM,
                    GXHDO101B043Const.BTN_DELETE_BOTTOM,
                    GXHDO101B043Const.BTN_UPDATE_BOTTOM,
                    GXHDO101B043Const.BTN_DATACOOPERATION_BOTTOM
                    ));

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

                // 熱処理_仮登録登録処理
                insertTmpSrShinkuukansou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), processData);

                // 熱処理_ｻﾌﾞ画面_仮登録登録処理
                insertTmpSubSrShinkuukansou(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            } else {

                // 熱処理_仮登録更新処理
                updateTmpSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

                // 熱処理_ｻﾌﾞ画面_仮登録更新処理
                updateTmpSubSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime);

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
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B043Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B043Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B043Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B043Const.SHURYOU_TIME); //終了時刻
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
            SrShinkuukansou tmpSrShinkuukansou = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrShinkuukansou> srShinkuukansouList = getSrShinkuukansouData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srShinkuukansouList.isEmpty()) {
                    tmpSrShinkuukansou = srShinkuukansouList.get(0);
                }

                deleteTmpSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
                deleteTmpSubSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // 熱処理_登録処理
            insertSrShinkuukansou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrShinkuukansou, processData);
            // 熱処理_ｻﾌﾞ画面_登録処理
            insertSubSrShinkuukansou(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B043Const.USER_AUTH_UPDATE_PARAM);

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

            // 熱処理_更新処理
            updateSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), processData);

            // 熱処理_ｻﾌﾞ画面_更新処理
            updateSubSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime);

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
        processData.setUserAuthParam(GXHDO101B043Const.USER_AUTH_DELETE_PARAM);

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

            // 熱処理_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrShinkuukansou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);
            // 熱処理_ｻﾌﾞ画面_仮登録登録処理
            insertDeleteDataTmpSubSrShinkuukansou(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // 熱処理_削除処理
            deleteSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            // 熱処理_ｻﾌﾞ画面_削除処理
            deleteSubSrShinkuukansou(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B043Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B043Const.BTN_DELETE_BOTTOM,
                        GXHDO101B043Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B043Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B043Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM,
                        GXHDO101B043Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B043Const.BTN_DELETE_TOP,
                        GXHDO101B043Const.BTN_UPDATE_TOP,
                        GXHDO101B043Const.BTN_START_DATETIME_TOP,
                        GXHDO101B043Const.BTN_END_DATETIME_TOP,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_TOP,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_TOP,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B043Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B043Const.BTN_INSERT_BOTTOM,
                        GXHDO101B043Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B043Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B043Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B043Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B043Const.BTN_INSERT_BOTTOM,
                        GXHDO101B043Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B043Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM,
                        GXHDO101B043Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B043Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B043Const.BTN_INSERT_TOP,
                        GXHDO101B043Const.BTN_START_DATETIME_TOP,
                        GXHDO101B043Const.BTN_END_DATETIME_TOP,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_TOP,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_TOP,
                        GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B043Const.BTN_DELETE_BOTTOM,
                        GXHDO101B043Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B043Const.BTN_DELETE_TOP,
                        GXHDO101B043Const.BTN_UPDATE_TOP));

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
            case GXHDO101B043Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B043Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B043Const.BTN_INSERT_TOP:
            case GXHDO101B043Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B043Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B043Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B043Const.BTN_UPDATE_TOP:
            case GXHDO101B043Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B043Const.BTN_DELETE_TOP:
            case GXHDO101B043Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B043Const.BTN_START_DATETIME_TOP:
            case GXHDO101B043Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B043Const.BTN_END_DATETIME_TOP:
            case GXHDO101B043Const.BTN_END_DATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // 受入れ総重量計算
            case GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_TOP:
            case GXHDO101B043Const.BTN_UKEIRESOJURYO_KEISAN_BOTTOM:
                method = "calculatUkeiresojuryo";
                break;
            // サヤNo
            case GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_TOP:
            case GXHDO101B043Const.BTN_UKEIRESOJURYO_SAYANO_BOTTOM:
                method = "openSayaNo";
                break;
            // 設備ﾃﾞｰﾀ連携
            case GXHDO101B043Const.BTN_DATACOOPERATION_TOP:
            case GXHDO101B043Const.BTN_DATACOOPERATION_BOTTOM:
                method = "doDataCooperationSyori";
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
        String lotpre = StringUtil.nullToBlank(getMapData(shikakariData, "lotpre"));// ﾛｯﾄ頭部
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

        // 送り良品数の取得
        String suuryo = null;

        //ﾃﾞｰﾀの取得
        String strfxhbm03List = "";

        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc);
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
            strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
            String fxhbm03DataArr[] = strfxhbm03List.split(",");

            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if(jissekiData != null && jissekiData.size() > 0){
                int dbJisseki = jissekiData.get(0).getJissekino(); //処理数
                // 生産情報の取得
                Map seisanData = loadSeisanData(queryRunnerWip, dbJisseki);
                if (seisanData == null || seisanData.isEmpty()) {
                    suuryo = "0";
                } else {
                    suuryo = String.valueOf(seisanData.get("ryohinsuu"));
                }
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo, suuryo, lotpre, paramJissekino, tanijuryo, queryRunnerDoc, formId);

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
     * @param suuryo 送り良品数
     * @param lotpre ﾛｯﾄ頭部
     * @param jissekino 実績No
     * @param tanijuryo 単位重量
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData,
            String lotNo, String suuryo, String lotpre, int paramJissekino, String tanijuryo, QueryRunner queryRunnerDoc,
            String formId) throws SQLException {

        // ロットNo
        this.setItemData(processData, GXHDO101B043Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B043Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B043Const.TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B043Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B043Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B043Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B043Const.OWNER, ownercode + ":" + owner);
        }

        // ﾛｯﾄﾌﾟﾚ
        this.setItemData(processData, GXHDO101B043Const.LOTPRE, lotpre);

        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);
        Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
        String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));
        if (jotaiFlg.isEmpty() || JOTAI_FLG_SAKUJO.equals(jotaiFlg)) {
            //送り良品数
            this.setItemData(processData, GXHDO101B043Const.SUURYO, suuryo);

            //受入れ単位重量
            FXHDD01 itemUkeireTanijuryo = getItemRow(processData.getItemList(), GXHDO101B043Const.UKEIRETANNIJYURYO);
            itemUkeireTanijuryo.setValue(NumberUtil.getTruncatData(tanijuryo, itemUkeireTanijuryo.getInputLength(), itemUkeireTanijuryo.getInputLengthDec()));//受入単位重量
                
            //受入れ総重量
            // Ⅳ.画面項目ｲﾍﾞﾝﾄ詳細.(8)【受入れ総重量計算】ﾎﾞﾀﾝ押下時 の処理実行
            calculatUkeiresojuryo(processData);
        }

        //回数
        this.setItemData(processData, GXHDO101B043Const.KAISUU, StringUtil.nullToBlank(paramJissekino));

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

        List<SrShinkuukansou> srShinkuukansouDataList = new ArrayList<>();
        List<SubSrShinkuukansou> subSrShinkuukansouDataList = new ArrayList<>();
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
                
                // 熱処理_ｻﾌﾞ画面データ設定
                setInputItemDataSubFormC018(null);
                return true;
            }

            // 熱処理データ取得
            srShinkuukansouDataList = getSrShinkuukansouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srShinkuukansouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 熱処理_サブ画面データ取得
            subSrShinkuukansouDataList = getSubSrShinkuukansouData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (subSrShinkuukansouDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srShinkuukansouDataList.isEmpty() || subSrShinkuukansouDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srShinkuukansouDataList.get(0));
        // 熱処理_ｻﾌﾞ画面データ設定
        setInputItemDataSubFormC018(subSrShinkuukansouDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srShinkuukansouData 熱処理データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrShinkuukansou srShinkuukansouData) {
        //送り良品数
        this.setItemData(processData, GXHDO101B043Const.SUURYO, getSrShinkuukansouItemData(GXHDO101B043Const.SUURYO, srShinkuukansouData));
        //受入れ単位重量
        this.setItemData(processData, GXHDO101B043Const.UKEIRETANNIJYURYO, getSrShinkuukansouItemData(GXHDO101B043Const.UKEIRETANNIJYURYO, srShinkuukansouData));
        //受入れ総重量
        this.setItemData(processData, GXHDO101B043Const.UKEIRESOUJYURYOU, getSrShinkuukansouItemData(GXHDO101B043Const.UKEIRESOUJYURYOU, srShinkuukansouData));
        //回数
        this.setItemData(processData, GXHDO101B043Const.KAISUU, getSrShinkuukansouItemData(GXHDO101B043Const.KAISUU, srShinkuukansouData));
        //種類
        this.setItemData(processData, GXHDO101B043Const.SYURUI, getSrShinkuukansouItemData(GXHDO101B043Const.SYURUI, srShinkuukansouData));
        // 号機①
        this.setItemData(processData, GXHDO101B043Const.GOUKI, getSrShinkuukansouItemData(GXHDO101B043Const.GOUKI, srShinkuukansouData));
        // 号機②
        this.setItemData(processData, GXHDO101B043Const.GOUKI2, getSrShinkuukansouItemData(GXHDO101B043Const.GOUKI2, srShinkuukansouData));
        // 号機③
        this.setItemData(processData, GXHDO101B043Const.GOUKI3, getSrShinkuukansouItemData(GXHDO101B043Const.GOUKI3, srShinkuukansouData));
        // 号機④
        this.setItemData(processData, GXHDO101B043Const.GOUKI4, getSrShinkuukansouItemData(GXHDO101B043Const.GOUKI4, srShinkuukansouData));
        //設定温度(℃)
        this.setItemData(processData, GXHDO101B043Const.SETTEIONDO, getSrShinkuukansouItemData(GXHDO101B043Const.SETTEIONDO, srShinkuukansouData));
        //設定時間(分)
        this.setItemData(processData, GXHDO101B043Const.SETTEIJIKAN, getSrShinkuukansouItemData(GXHDO101B043Const.SETTEIJIKAN, srShinkuukansouData));
        //開始日
        this.setItemData(processData, GXHDO101B043Const.KAISHI_DAY, getSrShinkuukansouItemData(GXHDO101B043Const.KAISHI_DAY, srShinkuukansouData));
        //開始時間
        this.setItemData(processData, GXHDO101B043Const.KAISHI_TIME, getSrShinkuukansouItemData(GXHDO101B043Const.KAISHI_TIME, srShinkuukansouData));
        //開始担当者
        this.setItemData(processData, GXHDO101B043Const.SAGYOSYA, getSrShinkuukansouItemData(GXHDO101B043Const.SAGYOSYA, srShinkuukansouData));
        //開始確認者
        this.setItemData(processData, GXHDO101B043Const.STARTKAKUNIN, getSrShinkuukansouItemData(GXHDO101B043Const.STARTKAKUNIN, srShinkuukansouData));
        //終了日
        this.setItemData(processData, GXHDO101B043Const.SHURYOU_DAY, getSrShinkuukansouItemData(GXHDO101B043Const.SHURYOU_DAY, srShinkuukansouData));
        //終了時間
        this.setItemData(processData, GXHDO101B043Const.SHURYOU_TIME, getSrShinkuukansouItemData(GXHDO101B043Const.SHURYOU_TIME, srShinkuukansouData));
        //終了担当者
        this.setItemData(processData, GXHDO101B043Const.ENDTANTOU, getSrShinkuukansouItemData(GXHDO101B043Const.ENDTANTOU, srShinkuukansouData));
        //検査場所
        this.setItemData(processData, GXHDO101B043Const.KENSABASYO, getSrShinkuukansouItemData(GXHDO101B043Const.KENSABASYO, srShinkuukansouData));
        //備考1
        this.setItemData(processData, GXHDO101B043Const.BIKOU1, getSrShinkuukansouItemData(GXHDO101B043Const.BIKOU1, srShinkuukansouData));
        //備考2
        this.setItemData(processData, GXHDO101B043Const.BIKOU2, getSrShinkuukansouItemData(GXHDO101B043Const.BIKOU2, srShinkuukansouData));
    }

    /**
     * 熱処理の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 熱処理登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrShinkuukansou> getSrShinkuukansouData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrShinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrShinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
        String sql = "SELECT jissekino,syorisuu "
                + "FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";

        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());

        sql += " ORDER BY syoribi DESC, syorijikoku DESC";

        Map mapping = new HashMap<>();
        mapping.put("jissekino", "jissekino");
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
     * @param queryRunnerDoc オブジェクト
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = 'common_user' AND key = 'xhd_dennkitokusei_maekoteicode' ";
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
     * [熱処理]から、ﾃﾞｰﾀを取得
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
    private List<SrShinkuukansou> loadSrShinkuukansou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,lotpre,kcpno,syoribi,kaishijikan,syuuryoujikan,sagyosya,koutei,gouki,setteiondo,setteijikan,"
                + " kaisuu,suuryo,bikou1,bikou2,bikou3,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,syurui,"
                + " startkakunin,syuryonichiji,endtantou,kensabasyo,torokunichiji,kosinnichiji,revision,'0' AS deleteflag,gouki2,gouki3,gouki4 "
                + "FROM sr_shinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND kaisuu = ? ";

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
        mapping.put("lotpre", "lotpre"); //ﾛｯﾄﾌﾟﾚ
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("syoribi", "syoribi"); //開始日時
        mapping.put("kaishijikan", "kaishijikan"); //開始時間
        mapping.put("syuuryoujikan", "syuuryoujikan"); //終了時間
        mapping.put("sagyosya", "sagyosya"); //開始担当者
        mapping.put("koutei", "koutei"); //工程
        mapping.put("gouki", "gouki"); //号機
        mapping.put("gouki2", "gouki2"); //号機
        mapping.put("gouki3", "gouki3"); //号機
        mapping.put("gouki4", "gouki4"); //号機
        mapping.put("setteiondo", "setteiondo"); //設定温度
        mapping.put("setteijikan", "setteijikan"); //設定時間
        mapping.put("kaisuu", "kaisuu"); //回数
        mapping.put("suuryo", "suuryo"); //送り良品数
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("syurui", "syurui"); //種類
        mapping.put("startkakunin", "startkakunin"); //開始確認者
        mapping.put("syuryonichiji", "syuryonichiji"); //終了日時
        mapping.put("endtantou", "endtantou"); //終了担当者
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrShinkuukansou>> beanHandler = new BeanListHandler<>(SrShinkuukansou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [熱処理_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrShinkuukansou> loadTmpSrShinkuukansou(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,lotpre,kcpno,syoribi,kaishijikan,syuuryoujikan,sagyosya,koutei,gouki,setteiondo,setteijikan,"
                + " kaisuu,suuryo,bikou1,bikou2,bikou3,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,syurui,"
                + " startkakunin,syuryonichiji,endtantou,kensabasyo,torokunichiji,kosinnichiji,revision,deleteflag,gouki2,gouki3,gouki4 "
                + "FROM tmp_sr_shinkuukansou "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND kaisuu = ? AND deleteflag = ? ";

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
        mapping.put("lotpre", "lotpre"); //ﾛｯﾄﾌﾟﾚ
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("syoribi", "syoribi"); //開始日時
        mapping.put("kaishijikan", "kaishijikan"); //開始時間
        mapping.put("syuuryoujikan", "syuuryoujikan"); //終了時間
        mapping.put("sagyosya", "sagyosya"); //開始担当者
        mapping.put("koutei", "koutei"); //工程
        mapping.put("gouki", "gouki"); //号機
        mapping.put("gouki2", "gouki2"); //号機2
        mapping.put("gouki3", "gouki3"); //号機3
        mapping.put("gouki4", "gouki4"); //号機4
        mapping.put("setteiondo", "setteiondo"); //設定温度
        mapping.put("setteijikan", "setteijikan"); //設定時間
        mapping.put("kaisuu", "kaisuu"); //回数
        mapping.put("suuryo", "suuryo"); //送り良品数
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("syurui", "syurui"); //種類
        mapping.put("startkakunin", "startkakunin"); //開始確認者
        mapping.put("syuryonichiji", "syuryonichiji"); //終了日時
        mapping.put("endtantou", "endtantou"); //終了担当者
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrShinkuukansou>> beanHandler = new BeanListHandler<>(SrShinkuukansou.class, rowProcessor);

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

            // 熱処理データ取得
            List<SrShinkuukansou> srShinkuukansouDataList = getSrShinkuukansouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srShinkuukansouDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 熱処理_ｻﾌﾞ画面データ取得
            List<SubSrShinkuukansou> subSrShinkuukansouDataList = getSubSrShinkuukansouData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (subSrShinkuukansouDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srShinkuukansouDataList.get(0));
            // 熱処理_ｻﾌﾞ画面データ設定
            setInputItemDataSubFormC018(subSrShinkuukansouDataList.get(0));

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
     * @param srShinkuukansouData 熱処理データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrShinkuukansou srShinkuukansouData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srShinkuukansouData != null) {
            // 元データが存在する場合元データより取得
            return getSrShinkuukansouItemData(itemId, srShinkuukansouData);
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
     * 熱処理_仮登録(tmp_sr_shinkuukansou)登録処理
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
    private void insertTmpSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_shinkuukansou ("
                + " kojyo,lotno,edaban,lotpre,kcpno,syoribi,kaishijikan,syuuryoujikan,sagyosya,koutei,gouki,gouki2,gouki3,gouki4,setteiondo,setteijikan,"
                + " kaisuu,suuryo,bikou1,bikou2,bikou3,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,syurui,"
                + " startkakunin,syuryonichiji,endtantou,kensabasyo,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrShinkuukansou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 熱処理_仮登録(tmp_sr_shinkuukansou)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_shinkuukansou SET "
                + " lotpre = ?,kcpno = ?,syoribi = ?,sagyosya = ?,koutei = ?,gouki = ?,gouki2 = ?,gouki3 = ?,gouki4 = ?,setteiondo = ?,setteijikan = ?,kaisuu = ?,suuryo = ?, "
                + " bikou1 = ?,bikou2 = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,syurui = ?,"
                + " startkakunin = ?,syuryonichiji = ?,endtantou = ?,kensabasyo = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrShinkuukansou> srShinkuukansouList = getSrShinkuukansouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrShinkuukansou srShinkuukansou = null;
        if (!srShinkuukansouList.isEmpty()) {
            srShinkuukansou = srShinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrShinkuukansou(false, newRev, 0, "", "", "", systemTime, itemList, srShinkuukansou, processData);

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
     * 熱処理_仮登録(tmp_sr_shinkuukansou)削除処理
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
    private void deleteTmpSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_shinkuukansou "
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
     * 熱処理_仮登録(tmp_sr_shinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srShinkuukansouData 熱処理データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrShinkuukansou(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrShinkuukansou srShinkuukansouData, ProcessData processData) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.LOTPRE, srShinkuukansouData))); //ﾛｯﾄﾌﾟﾚ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.KCPNO, srShinkuukansouData))); //KCPNO  
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.KAISHI_DAY, srShinkuukansouData),
                getItemData(itemList, GXHDO101B043Const.KAISHI_TIME, srShinkuukansouData))); //開始日時
        if (isInsert) {
            params.add(null); //開始時間
            params.add(null); //終了時間
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.SAGYOSYA, srShinkuukansouData))); // 開始担当者        
        params.add(""); //工程

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.GOUKI, srShinkuukansouData))); // 号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.GOUKI2, srShinkuukansouData))); // 号機2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.GOUKI3, srShinkuukansouData))); // 号機3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.GOUKI4, srShinkuukansouData))); // 号機4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.SETTEIONDO, srShinkuukansouData))); // 設定温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.SETTEIJIKAN, srShinkuukansouData))); // 設定時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.KAISUU, srShinkuukansouData))); // 回数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.SUURYO, srShinkuukansouData))); // 送り良品数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.BIKOU1, srShinkuukansouData))); // 備考1        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.BIKOU2, srShinkuukansouData))); // 備考2
        if (isInsert) {
            params.add(null); //備考3
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.TOKUISAKI, srShinkuukansouData))); // 客先
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分        
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.UKEIRETANNIJYURYO, srShinkuukansouData))); // 受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.UKEIRESOUJYURYOU, srShinkuukansouData))); // 受入れ総重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.SYURUI, srShinkuukansouData))); // 種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.STARTKAKUNIN, srShinkuukansouData))); // 開始確認者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.SHURYOU_DAY, srShinkuukansouData),
                getItemData(itemList, GXHDO101B043Const.SHURYOU_TIME, srShinkuukansouData))); // 終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.ENDTANTOU, srShinkuukansouData))); // 終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.KENSABASYO, srShinkuukansouData))); // 検査場所
        
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
     * 熱処理(sr_shinkuukansou)登録処理
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
     * @param tmpSrShinkuukansou 仮登録データ
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrShinkuukansou tmpSrShinkuukansou, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO sr_shinkuukansou ("
                + " kojyo,lotno,edaban,lotpre,kcpno,syoribi,kaishijikan,syuuryoujikan,sagyosya,koutei,gouki,gouki2,gouki3,gouki4,setteiondo,setteijikan,"
                + " kaisuu,suuryo,bikou1,bikou2,bikou3,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,syurui,"
                + " startkakunin,syuryonichiji,endtantou,kensabasyo,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrShinkuukansou(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrShinkuukansou, processData);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 熱処理(sr_shinkuukansou)更新処理
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
    private void updateSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_shinkuukansou SET "
                + " lotpre = ?,kcpno = ?,syoribi = ?,sagyosya = ?,gouki = ?,gouki2 = ?,gouki3 = ?,gouki4 = ?,setteiondo = ?,setteijikan = ?,kaisuu = ?,suuryo = ?, "
                + " bikou1 = ?,bikou2 = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,syurui = ?,"
                + " startkakunin = ?,syuryonichiji = ?,endtantou = ?,kensabasyo = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrShinkuukansou> srShinkuukansouList = getSrShinkuukansouData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrShinkuukansou srShinkuukansou = null;
        if (!srShinkuukansouList.isEmpty()) {
            srShinkuukansou = srShinkuukansouList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrShinkuukansou(false, newRev, "", "", "", jissekino, systemTime, itemList, srShinkuukansou, processData);

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
     * 熱処理(sr_shinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srShinkuukansouData 熱処理データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrShinkuukansou(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrShinkuukansou srShinkuukansouData, ProcessData processData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.LOTPRE, srShinkuukansouData))); //ﾛｯﾄﾌﾟﾚ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.KCPNO, srShinkuukansouData))); //KCPNO  
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B043Const.KAISHI_DAY, srShinkuukansouData),
                getItemData(itemList, GXHDO101B043Const.KAISHI_TIME, srShinkuukansouData))); //開始日時
        if (isInsert) {
            params.add(""); //開始時間
            params.add(""); //終了時間
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.SAGYOSYA, srShinkuukansouData))); // 開始担当者  
        if (isInsert) {
            params.add(""); //工程
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.GOUKI, srShinkuukansouData))); // 号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.GOUKI2, srShinkuukansouData))); // 号機2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.GOUKI3, srShinkuukansouData))); // 号機3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.GOUKI4, srShinkuukansouData))); // 号機4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.SETTEIONDO, srShinkuukansouData))); // 設定温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.SETTEIJIKAN, srShinkuukansouData))); // 設定時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B043Const.KAISUU, srShinkuukansouData))); // 回数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B043Const.SUURYO, srShinkuukansouData))); // 送り良品数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.BIKOU1, srShinkuukansouData))); // 備考1        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.BIKOU2, srShinkuukansouData))); // 備考2
        if (isInsert) {
            params.add(""); //備考3
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.TOKUISAKI, srShinkuukansouData))); // 客先
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("lotkubuncode")))); // ﾛｯﾄ区分        
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(processData.getHiddenDataMap().get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B043Const.UKEIRETANNIJYURYO, srShinkuukansouData))); // 受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B043Const.UKEIRESOUJYURYOU, srShinkuukansouData))); // 受入れ総重量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.SYURUI, srShinkuukansouData))); // 種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.STARTKAKUNIN, srShinkuukansouData))); // 開始確認者
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B043Const.SHURYOU_DAY, srShinkuukansouData),
                getItemData(itemList, GXHDO101B043Const.SHURYOU_TIME, srShinkuukansouData))); // 終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.ENDTANTOU, srShinkuukansouData))); // 終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B043Const.KENSABASYO, srShinkuukansouData))); // 検査場所
        

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
     * 熱処理(sr_shinkuukansou)削除処理
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
    private void deleteSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_shinkuukansou "
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
     * [熱処理_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_shinkuukansou "
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
     * 開始時間設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B043Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B043Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO101B043Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO101B043Const.SHURYOU_TIME);
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
     * @param srShinkuukansouData 熱処理データ
     * @return DB値
     */
    private String getSrShinkuukansouItemData(String itemId, SrShinkuukansou srShinkuukansouData) {
        switch (itemId) {
            // KCPNO
            case GXHDO101B043Const.KCPNO:
                return StringUtil.nullToBlank(srShinkuukansouData.getKcpno());
            // 客先
            case GXHDO101B043Const.TOKUISAKI:
                return StringUtil.nullToBlank(srShinkuukansouData.getTokuisaki());
            // ﾛｯﾄﾌﾟﾚ
            case GXHDO101B043Const.LOTPRE:
                return StringUtil.nullToBlank(srShinkuukansouData.getLotpre());
            //送り良品数
            case GXHDO101B043Const.SUURYO:
                return StringUtil.nullToBlank(srShinkuukansouData.getSuuryo());
            //受入れ単位重量
            case GXHDO101B043Const.UKEIRETANNIJYURYO:
                return StringUtil.nullToBlank(srShinkuukansouData.getUkeiretannijyuryo());
            //受入れ総重量
            case GXHDO101B043Const.UKEIRESOUJYURYOU:
                return StringUtil.nullToBlank(srShinkuukansouData.getUkeiresoujyuryou());
            //回数
            case GXHDO101B043Const.KAISUU:
                return StringUtil.nullToBlank(srShinkuukansouData.getKaisuu());
            //種類
            case GXHDO101B043Const.SYURUI:
                return StringUtil.nullToBlank(srShinkuukansouData.getSyurui());
            //号機
            case GXHDO101B043Const.GOUKI:
                return StringUtil.nullToBlank(srShinkuukansouData.getGouki());
            //号機2
            case GXHDO101B043Const.GOUKI2:
                return StringUtil.nullToBlank(srShinkuukansouData.getGouki2());
            //号機3
            case GXHDO101B043Const.GOUKI3:
                return StringUtil.nullToBlank(srShinkuukansouData.getGouki3());
            //号機4
            case GXHDO101B043Const.GOUKI4:
                return StringUtil.nullToBlank(srShinkuukansouData.getGouki4());
            //設定温度(℃)
            case GXHDO101B043Const.SETTEIONDO:
                return StringUtil.nullToBlank(srShinkuukansouData.getSetteiondo());
            //設定時間(分)
            case GXHDO101B043Const.SETTEIJIKAN:
                return StringUtil.nullToBlank(srShinkuukansouData.getSetteijikan());
            //開始日
            case GXHDO101B043Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srShinkuukansouData.getSyoribi(), "yyMMdd");
            //開始時間
            case GXHDO101B043Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srShinkuukansouData.getSyoribi(), "HHmm");
            //開始担当者
            case GXHDO101B043Const.SAGYOSYA:
                return StringUtil.nullToBlank(srShinkuukansouData.getSagyosya());
            //開始確認者
            case GXHDO101B043Const.STARTKAKUNIN:
                return StringUtil.nullToBlank(srShinkuukansouData.getStartkakunin());
            //終了日
            case GXHDO101B043Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srShinkuukansouData.getSyuryonichiji(), "yyMMdd");
            //終了時間
            case GXHDO101B043Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srShinkuukansouData.getSyuryonichiji(), "HHmm");
            //終了担当者
            case GXHDO101B043Const.ENDTANTOU:
                return StringUtil.nullToBlank(srShinkuukansouData.getEndtantou());
            //検査場所
            case GXHDO101B043Const.KENSABASYO:
                return StringUtil.nullToBlank(srShinkuukansouData.getKensabasyo());
            // 備考1
            case GXHDO101B043Const.BIKOU1:
                return StringUtil.nullToBlank(srShinkuukansouData.getBikou1());
            // 備考2
            case GXHDO101B043Const.BIKOU2:
                return StringUtil.nullToBlank(srShinkuukansouData.getBikou2());
            default:
                return null;
        }
    }

    /**
     * 熱処理_仮登録(tmp_sr_shinkuukansou)登録処理(削除時)
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
    private void insertDeleteDataTmpSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_shinkuukansou ("
                + " kojyo,lotno,edaban,lotpre,kcpno,syoribi,kaishijikan,syuuryoujikan,sagyosya,koutei,gouki,gouki2,gouki3,gouki4,setteiondo,setteijikan,"
                + " kaisuu,suuryo,bikou1,bikou2,bikou3,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,syurui,"
                + " startkakunin,syuryonichiji,endtantou,kensabasyo,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + " kojyo,lotno,edaban,lotpre,kcpno,syoribi,kaishijikan,syuuryoujikan,sagyosya,koutei,gouki,gouki2,gouki3,gouki4,setteiondo,setteijikan,"
                + " kaisuu,suuryo,bikou1,bikou2,bikou3,tokuisaki,lotkubuncode,ownercode,ukeiretannijyuryo,ukeiresoujyuryou,syurui,"
                + " startkakunin,syuryonichiji,endtantou,kensabasyo,? ,? ,? ,?"
                + " FROM sr_shinkuukansou "
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
     * 受入れ総重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData calculatUkeiresojuryo(ProcessData processData) {
        // 継続メソッドのクリア
        processData.setMethod("");

        try {
            //1.「送り良品数」の取得ﾁｪｯｸを行う。  
            String suuryo = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B043Const.SUURYO, null)); //送り良品数
            // A.送り良品数が0以下の場合 
            // 送り良品数を数値変換
            BigDecimal decSuuryo = new BigDecimal(suuryo);
            if(0 <= BigDecimal.ZERO.compareTo(decSuuryo)){
                //  何もしない(受入れ総重量==null)
                return processData;
            }
            // B.A以外の場合 
            //  以降の処理を続行する。

            //2.「受入れ単位重量」の取得ﾁｪｯｸを行う。  
            String ukeiretannijyuryo = StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B043Const.UKEIRETANNIJYURYO, null)); //受入れ単位重量
            // A.受入れ単位重量が0以下の場合 
            // 受入れ単位重量を数値変換
            BigDecimal decUkeiretannijyuryo = new BigDecimal(ukeiretannijyuryo);
            if (0 <= BigDecimal.ZERO.compareTo(decUkeiretannijyuryo)) {
                //  何もしない(受入れ総重量==null)
                return processData;
            }
            // B.A以外の場合 
            //  以降の処理を続行する。

            //3.「受入れ総重量」の計算を行う。  
            FXHDD01 itemUkeiresoujyuryou = getItemRow(processData.getItemList(), GXHDO101B043Const.UKEIRESOUJYURYOU);
            // 1.　「送り良品数」　÷　100　×　「受入れ単位重量」 
            // 2.　1の計算結果の小数点第三位を四捨五入する。 
            BigDecimal calSuuryo = decSuuryo.divide(new BigDecimal("100"));
            BigDecimal decUkeiresoujyuryou = calSuuryo.multiply(decUkeiretannijyuryo).setScale(2, RoundingMode.HALF_UP);

            // 結果を項目にセット
            itemUkeiresoujyuryou.setValue(decUkeiresoujyuryou.toPlainString());

        } catch (NumberFormatException e) {
            //数値型変換失敗時はそのままリターン
        }
        return processData;

    }
    
    /**
     * 熱処理_サブ画面Open
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openSayaNo(ProcessData processData) {

        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c018");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C018 beanGXHDO101C018 = (GXHDO101C018) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C018);
            beanGXHDO101C018.setGxhdO101c018ModelView(beanGXHDO101C018.getGxhdO101c018Model().clone());

        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;

        }

        return processData;
    }
    
    /**
     * 熱処理_サブ画面データ設定処理
     * 
     * @param subSrShinkuukansou 熱処理_サブ画面のデータ
     */
    private void setInputItemDataSubFormC018(SubSrShinkuukansou subSrShinkuukansou) {
        // サブ画面の情報を取得
        GXHDO101C018 beanGXHDO101C018 = (GXHDO101C018) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C018);

        GXHDO101C018Model model;
        List<String[]> initGouki1DataList = new ArrayList<>();
        List<String[]> initGouki2DataList = new ArrayList<>();
        List<String[]> initGouki3DataList = new ArrayList<>();
        List<String[]> initGouki4DataList = new ArrayList<>();
        String[] gouki1ItemValues;
        String[] gouki1CheckBoxValues;
        String[] gouki2ItemValues;
        String[] gouki2CheckBoxValues;
        String[] gouki3ItemValues;
        String[] gouki3CheckBoxValues;
        String[] gouki4ItemValues;
        String[] gouki4CheckBoxValues;
        if (subSrShinkuukansou == null) {
            gouki1ItemValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            gouki1CheckBoxValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            initGouki1DataList.add(gouki1ItemValues);
            initGouki1DataList.add(gouki1CheckBoxValues);
            gouki2ItemValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            gouki2CheckBoxValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            initGouki2DataList.add(gouki2ItemValues);
            initGouki2DataList.add(gouki2CheckBoxValues);
            gouki3ItemValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            gouki3CheckBoxValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            initGouki3DataList.add(gouki3ItemValues);
            initGouki3DataList.add(gouki3CheckBoxValues);
            gouki4ItemValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            gouki4CheckBoxValues = new String[]{"", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", ""};
            initGouki4DataList.add(gouki4ItemValues);
            initGouki4DataList.add(gouki4CheckBoxValues);
        } else {
            // 号機①
            gouki1ItemValues = new String[]{
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya1()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya2()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya3()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya4()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya5()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya6()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya7()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya8()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya9()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya10()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya11()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya12()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya13()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya14()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya15()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya16()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya17()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya18()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya19()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki1saya20())
            };
            gouki1CheckBoxValues = new String[]{
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check1())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check2())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check3())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check4())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check5())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check6())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check7())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check8())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check9())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check10())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check11())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check12())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check13())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check14())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check15())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check16())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check17())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check18())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check19())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki1check20()))
            };
            initGouki1DataList.add(gouki1ItemValues);
            initGouki1DataList.add(gouki1CheckBoxValues);

            //号機②
            gouki2ItemValues = new String[]{
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya1()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya2()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya3()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya4()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya5()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya6()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya7()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya8()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya9()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya10()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya11()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya12()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya13()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya14()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya15()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya16()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya17()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya18()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya19()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki2saya20())
            };
            gouki2CheckBoxValues = new String[]{
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check1())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check2())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check3())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check4())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check5())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check6())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check7())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check8())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check9())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check10())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check11())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check12())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check13())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check14())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check15())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check16())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check17())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check18())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check19())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki2check20()))
            };
            initGouki2DataList.add(gouki2ItemValues);
            initGouki2DataList.add(gouki2CheckBoxValues);

            //号機③
            gouki3ItemValues = new String[]{
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya1()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya2()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya3()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya4()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya5()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya6()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya7()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya8()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya9()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya10()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya11()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya12()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya13()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya14()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya15()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya16()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya17()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya18()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya19()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki3saya20())
            };
            gouki3CheckBoxValues = new String[]{
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check1())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check2())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check3())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check4())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check5())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check6())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check7())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check8())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check9())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check10())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check11())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check12())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check13())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check14())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check15())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check16())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check17())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check18())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check19())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki3check20()))
            };
            initGouki3DataList.add(gouki3ItemValues);
            initGouki3DataList.add(gouki3CheckBoxValues);

            //号機④
            gouki4ItemValues = new String[]{
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya1()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya2()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya3()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya4()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya5()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya6()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya7()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya8()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya9()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya10()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya11()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya12()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya13()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya14()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya15()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya16()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya17()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya18()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya19()),
                StringUtil.nullToBlank(subSrShinkuukansou.getGouki4saya20())
            };
            gouki4CheckBoxValues = new String[]{
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check1())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check2())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check3())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check4())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check5())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check6())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check7())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check8())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check9())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check10())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check11())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check12())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check13())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check14())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check15())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check16())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check17())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check18())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check19())),
                getCheckBoxCheckValue(StringUtil.nullToBlank(subSrShinkuukansou.getGouki4check20()))
            };
            initGouki4DataList.add(gouki4ItemValues);
            initGouki4DataList.add(gouki4CheckBoxValues);
        }
        
        model = GXHDO101C018Logic.createGXHDO101C018Model(initGouki1DataList, initGouki2DataList, initGouki3DataList, initGouki4DataList);
        
        beanGXHDO101C018.setGxhdO101c018Model(model);
    }

    /**
     * 熱処理_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 熱処理_ｻﾌﾞ画面登録データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrShinkuukansou> getSubSrShinkuukansouData(QueryRunner queryRunnerQcdb, 
            String rev, String jotaiFlg, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrShinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSubSrShinkuukansou(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        }
    }

    /**
     * [熱処理_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
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
    private List<SubSrShinkuukansou> loadSubSrShinkuukansou(QueryRunner queryRunnerQcdb, 
            String kojyo, String lotNo, String edaban, int jissekino, String rev) throws SQLException {
        
        String sql = "SELECT kojyo, lotno, edaban, kaisuu, gouki1saya1, gouki1saya2, gouki1saya3, "
                + "gouki1saya4, gouki1saya5, gouki1saya6, gouki1saya7, gouki1saya8, gouki1saya9, "
                + "gouki1saya10, gouki1saya11, gouki1saya12, gouki1saya13, gouki1saya14, "                
                + "gouki1saya15, gouki1saya16, gouki1saya17, gouki1saya18, gouki1saya19, "                
                + "gouki1saya20, gouki1check1, gouki1check2, gouki1check3, gouki1check4, "
                + "gouki1check5, gouki1check6, gouki1check7, gouki1check8, gouki1check9, "
                + "gouki1check10, gouki1check11, gouki1check12, gouki1check13, gouki1check14, "
                + "gouki1check15, gouki1check16, gouki1check17, gouki1check18, gouki1check19, "
                + "gouki1check20, gouki2saya1, gouki2saya2, gouki2saya3, gouki2saya4, gouki2saya5, "
                + "gouki2saya6, gouki2saya7, gouki2saya8, gouki2saya9, gouki2saya10, gouki2saya11, "
                + "gouki2saya12, gouki2saya13, gouki2saya14, gouki2saya15, gouki2saya16, gouki2saya17, "
                + "gouki2saya18, gouki2saya19, gouki2saya20, gouki2check1, "
                + "gouki2check2, gouki2check3, gouki2check4, gouki2check5, gouki2check6, "
                + "gouki2check7, gouki2check8, gouki2check9, gouki2check10, gouki2check11, "
                + "gouki2check12, gouki2check13, gouki2check14, gouki2check15, gouki2check16, "
                + "gouki2check17, gouki2check18, gouki2check19, gouki2check20, gouki3saya1, "
                + "gouki3saya2, gouki3saya3, gouki3saya4, gouki3saya5, gouki3saya6, gouki3saya7, "
                + "gouki3saya8, gouki3saya9, gouki3saya10, gouki3saya11, gouki3saya12, "
                + "gouki3saya13, gouki3saya14, gouki3saya15, gouki3saya16, gouki3saya17, "
                + "gouki3saya18, gouki3saya19, gouki3saya20, gouki3check1, gouki3check2, "
                + "gouki3check3, gouki3check4, gouki3check5, gouki3check6, gouki3check7, "
                + "gouki3check8, gouki3check9, gouki3check10, gouki3check11, gouki3check12, "
                + "gouki3check13, gouki3check14, gouki3check15, gouki3check16, gouki3check17, "
                + "gouki3check18, gouki3check19, gouki3check20, gouki4saya1, gouki4saya2, "
                + "gouki4saya3, gouki4saya4, gouki4saya5, gouki4saya6, gouki4saya7, gouki4saya8, "
                + "gouki4saya9, gouki4saya10, gouki4saya11, gouki4saya12, gouki4saya13, "
                + "gouki4saya14, gouki4saya15, gouki4saya16, gouki4saya17, gouki4saya18, "
                + "gouki4saya19, gouki4saya20, gouki4check1, gouki4check2, gouki4check3, "
                + "gouki4check4, gouki4check5, gouki4check6, gouki4check7, gouki4check8, "
                + "gouki4check9, gouki4check10, gouki4check11, gouki4check12, "
                + "gouki4check13, gouki4check14, gouki4check15, gouki4check16, gouki4check17, "
                + "gouki4check18, gouki4check19, gouki4check20, torokunichiji, kosinnichiji, revision, '0' AS deleteflag "
                + "FROM sub_sr_shinkuukansou "
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
        mapping.put("gouki1saya1", "gouki1saya1"); // 号機①ｻﾔNo1
        mapping.put("gouki1saya2", "gouki1saya2"); // 号機①ｻﾔNo2
        mapping.put("gouki1saya3", "gouki1saya3"); // 号機①ｻﾔNo3
        mapping.put("gouki1saya4", "gouki1saya4"); // 号機①ｻﾔNo4
        mapping.put("gouki1saya5", "gouki1saya5"); // 号機①ｻﾔNo5
        mapping.put("gouki1saya6", "gouki1saya6"); // 号機①ｻﾔNo6
        mapping.put("gouki1saya7", "gouki1saya7"); // 号機①ｻﾔNo7
        mapping.put("gouki1saya8", "gouki1saya8"); // 号機①ｻﾔNo8
        mapping.put("gouki1saya9", "gouki1saya9"); // 号機①ｻﾔNo9
        mapping.put("gouki1saya10", "gouki1saya10"); // 号機①ｻﾔNo10
        mapping.put("gouki1saya11", "gouki1saya11"); // 号機①ｻﾔNo11
        mapping.put("gouki1saya12", "gouki1saya12"); // 号機①ｻﾔNo12
        mapping.put("gouki1saya13", "gouki1saya13"); // 号機①ｻﾔNo13
        mapping.put("gouki1saya14", "gouki1saya14"); // 号機①ｻﾔNo14
        mapping.put("gouki1saya15", "gouki1saya15"); // 号機①ｻﾔNo15
        mapping.put("gouki1saya16", "gouki1saya16"); // 号機①ｻﾔNo16
        mapping.put("gouki1saya17", "gouki1saya17"); // 号機①ｻﾔNo17
        mapping.put("gouki1saya18", "gouki1saya18"); // 号機①ｻﾔNo18
        mapping.put("gouki1saya19", "gouki1saya19"); // 号機①ｻﾔNo19
        mapping.put("gouki1saya20", "gouki1saya20"); // 号機①ｻﾔNo20
        mapping.put("gouki1check1", "gouki1check1"); // 号機①ﾁｪｯｸ1
        mapping.put("gouki1check2", "gouki1check2"); // 号機①ﾁｪｯｸ2
        mapping.put("gouki1check3", "gouki1check3"); // 号機①ﾁｪｯｸ3
        mapping.put("gouki1check4", "gouki1check4"); // 号機①ﾁｪｯｸ4
        mapping.put("gouki1check5", "gouki1check5"); // 号機①ﾁｪｯｸ5
        mapping.put("gouki1check6", "gouki1check6"); // 号機①ﾁｪｯｸ6
        mapping.put("gouki1check7", "gouki1check7"); // 号機①ﾁｪｯｸ7
        mapping.put("gouki1check8", "gouki1check8"); // 号機①ﾁｪｯｸ8
        mapping.put("gouki1check9", "gouki1check9"); // 号機①ﾁｪｯｸ9
        mapping.put("gouki1check10", "gouki1check10"); // 号機①ﾁｪｯｸ10
        mapping.put("gouki1check11", "gouki1check11"); // 号機①ﾁｪｯｸ11
        mapping.put("gouki1check12", "gouki1check12"); // 号機①ﾁｪｯｸ12
        mapping.put("gouki1check13", "gouki1check13"); // 号機①ﾁｪｯｸ13
        mapping.put("gouki1check14", "gouki1check14"); // 号機①ﾁｪｯｸ14
        mapping.put("gouki1check15", "gouki1check15"); // 号機①ﾁｪｯｸ15
        mapping.put("gouki1check16", "gouki1check16"); // 号機①ﾁｪｯｸ16
        mapping.put("gouki1check17", "gouki1check17"); // 号機①ﾁｪｯｸ17
        mapping.put("gouki1check18", "gouki1check18"); // 号機①ﾁｪｯｸ18
        mapping.put("gouki1check19", "gouki1check19"); // 号機①ﾁｪｯｸ19
        mapping.put("gouki1check20", "gouki1check20"); // 号機①ﾁｪｯｸ20
        mapping.put("gouki2saya1", "gouki2saya1"); // 号機②ｻﾔNo1
        mapping.put("gouki2saya2", "gouki2saya2"); // 号機②ｻﾔNo2
        mapping.put("gouki2saya3", "gouki2saya3"); // 号機②ｻﾔNo3
        mapping.put("gouki2saya4", "gouki2saya4"); // 号機②ｻﾔNo4
        mapping.put("gouki2saya5", "gouki2saya5"); // 号機②ｻﾔNo5
        mapping.put("gouki2saya6", "gouki2saya6"); // 号機②ｻﾔNo6
        mapping.put("gouki2saya7", "gouki2saya7"); // 号機②ｻﾔNo7
        mapping.put("gouki2saya8", "gouki2saya8"); // 号機②ｻﾔNo8
        mapping.put("gouki2saya9", "gouki2saya9"); // 号機②ｻﾔNo9
        mapping.put("gouki2saya10", "gouki2saya10"); // 号機②ｻﾔNo10
        mapping.put("gouki2saya11", "gouki2saya11"); // 号機②ｻﾔNo11
        mapping.put("gouki2saya12", "gouki2saya12"); // 号機②ｻﾔNo12
        mapping.put("gouki2saya13", "gouki2saya13"); // 号機②ｻﾔNo13
        mapping.put("gouki2saya14", "gouki2saya14"); // 号機②ｻﾔNo14
        mapping.put("gouki2saya15", "gouki2saya15"); // 号機②ｻﾔNo15
        mapping.put("gouki2saya16", "gouki2saya16"); // 号機②ｻﾔNo16
        mapping.put("gouki2saya17", "gouki2saya17"); // 号機②ｻﾔNo17
        mapping.put("gouki2saya18", "gouki2saya18"); // 号機②ｻﾔNo18
        mapping.put("gouki2saya19", "gouki2saya19"); // 号機②ｻﾔNo19
        mapping.put("gouki2saya20", "gouki2saya20"); // 号機②ｻﾔNo20
        mapping.put("gouki2check1", "gouki2check1"); // 号機②ﾁｪｯｸ1
        mapping.put("gouki2check2", "gouki2check2"); // 号機②ﾁｪｯｸ2
        mapping.put("gouki2check3", "gouki2check3"); // 号機②ﾁｪｯｸ3
        mapping.put("gouki2check4", "gouki2check4"); // 号機②ﾁｪｯｸ4
        mapping.put("gouki2check5", "gouki2check5"); // 号機②ﾁｪｯｸ5
        mapping.put("gouki2check6", "gouki2check6"); // 号機②ﾁｪｯｸ6
        mapping.put("gouki2check7", "gouki2check7"); // 号機②ﾁｪｯｸ7
        mapping.put("gouki2check8", "gouki2check8"); // 号機②ﾁｪｯｸ8
        mapping.put("gouki2check9", "gouki2check9"); // 号機②ﾁｪｯｸ9
        mapping.put("gouki2check10", "gouki2check10"); // 号機②ﾁｪｯｸ10
        mapping.put("gouki2check11", "gouki2check11"); // 号機②ﾁｪｯｸ11
        mapping.put("gouki2check12", "gouki2check12"); // 号機②ﾁｪｯｸ12
        mapping.put("gouki2check13", "gouki2check13"); // 号機②ﾁｪｯｸ13
        mapping.put("gouki2check14", "gouki2check14"); // 号機②ﾁｪｯｸ14
        mapping.put("gouki2check15", "gouki2check15"); // 号機②ﾁｪｯｸ15
        mapping.put("gouki2check16", "gouki2check16"); // 号機②ﾁｪｯｸ16
        mapping.put("gouki2check17", "gouki2check17"); // 号機②ﾁｪｯｸ17
        mapping.put("gouki2check18", "gouki2check18"); // 号機②ﾁｪｯｸ18
        mapping.put("gouki2check19", "gouki2check19"); // 号機②ﾁｪｯｸ19
        mapping.put("gouki2check20", "gouki2check20"); // 号機②ﾁｪｯｸ20
        mapping.put("gouki3saya1", "gouki3saya1"); // 号機③ｻﾔNo1
        mapping.put("gouki3saya2", "gouki3saya2"); // 号機③ｻﾔNo2
        mapping.put("gouki3saya3", "gouki3saya3"); // 号機③ｻﾔNo3
        mapping.put("gouki3saya4", "gouki3saya4"); // 号機③ｻﾔNo4
        mapping.put("gouki3saya5", "gouki3saya5"); // 号機③ｻﾔNo5
        mapping.put("gouki3saya6", "gouki3saya6"); // 号機③ｻﾔNo6
        mapping.put("gouki3saya7", "gouki3saya7"); // 号機③ｻﾔNo7
        mapping.put("gouki3saya8", "gouki3saya8"); // 号機③ｻﾔNo8
        mapping.put("gouki3saya9", "gouki3saya9"); // 号機③ｻﾔNo9
        mapping.put("gouki3saya10", "gouki3saya10"); // 号機③ｻﾔNo10
        mapping.put("gouki3saya11", "gouki3saya11"); // 号機③ｻﾔNo11
        mapping.put("gouki3saya12", "gouki3saya12"); // 号機③ｻﾔNo12
        mapping.put("gouki3saya13", "gouki3saya13"); // 号機③ｻﾔNo13
        mapping.put("gouki3saya14", "gouki3saya14"); // 号機③ｻﾔNo14
        mapping.put("gouki3saya15", "gouki3saya15"); // 号機③ｻﾔNo15
        mapping.put("gouki3saya16", "gouki3saya16"); // 号機③ｻﾔNo16
        mapping.put("gouki3saya17", "gouki3saya17"); // 号機③ｻﾔNo17
        mapping.put("gouki3saya18", "gouki3saya18"); // 号機③ｻﾔNo18
        mapping.put("gouki3saya19", "gouki3saya19"); // 号機③ｻﾔNo19
        mapping.put("gouki3saya20", "gouki3saya20"); // 号機③ｻﾔNo20
        mapping.put("gouki3check1", "gouki3check1"); // 号機③ﾁｪｯｸ1
        mapping.put("gouki3check2", "gouki3check2"); // 号機③ﾁｪｯｸ2
        mapping.put("gouki3check3", "gouki3check3"); // 号機③ﾁｪｯｸ3
        mapping.put("gouki3check4", "gouki3check4"); // 号機③ﾁｪｯｸ4
        mapping.put("gouki3check5", "gouki3check5"); // 号機③ﾁｪｯｸ5
        mapping.put("gouki3check6", "gouki3check6"); // 号機③ﾁｪｯｸ6
        mapping.put("gouki3check7", "gouki3check7"); // 号機③ﾁｪｯｸ7
        mapping.put("gouki3check8", "gouki3check8"); // 号機③ﾁｪｯｸ8
        mapping.put("gouki3check9", "gouki3check9"); // 号機③ﾁｪｯｸ9
        mapping.put("gouki3check10", "gouki3check10"); // 号機③ﾁｪｯｸ10
        mapping.put("gouki3check11", "gouki3check11"); // 号機③ﾁｪｯｸ11
        mapping.put("gouki3check12", "gouki3check12"); // 号機③ﾁｪｯｸ12
        mapping.put("gouki3check13", "gouki3check13"); // 号機③ﾁｪｯｸ13
        mapping.put("gouki3check14", "gouki3check14"); // 号機③ﾁｪｯｸ14
        mapping.put("gouki3check15", "gouki3check15"); // 号機③ﾁｪｯｸ15
        mapping.put("gouki3check16", "gouki3check16"); // 号機③ﾁｪｯｸ16
        mapping.put("gouki3check17", "gouki3check17"); // 号機③ﾁｪｯｸ17
        mapping.put("gouki3check18", "gouki3check18"); // 号機③ﾁｪｯｸ18
        mapping.put("gouki3check19", "gouki3check19"); // 号機③ﾁｪｯｸ19
        mapping.put("gouki3check20", "gouki3check20"); // 号機③ﾁｪｯｸ20
        mapping.put("gouki4saya1", "gouki4saya1"); // 号機④ｻﾔNo1
        mapping.put("gouki4saya2", "gouki4saya2"); // 号機④ｻﾔNo2
        mapping.put("gouki4saya3", "gouki4saya3"); // 号機④ｻﾔNo3
        mapping.put("gouki4saya4", "gouki4saya4"); // 号機④ｻﾔNo4
        mapping.put("gouki4saya5", "gouki4saya5"); // 号機④ｻﾔNo5
        mapping.put("gouki4saya6", "gouki4saya6"); // 号機④ｻﾔNo6
        mapping.put("gouki4saya7", "gouki4saya7"); // 号機④ｻﾔNo7
        mapping.put("gouki4saya8", "gouki4saya8"); // 号機④ｻﾔNo8
        mapping.put("gouki4saya9", "gouki4saya9"); // 号機④ｻﾔNo9
        mapping.put("gouki4saya10", "gouki4saya10"); // 号機④ｻﾔNo10
        mapping.put("gouki4saya11", "gouki4saya11"); // 号機④ｻﾔNo11
        mapping.put("gouki4saya12", "gouki4saya12"); // 号機④ｻﾔNo12
        mapping.put("gouki4saya13", "gouki4saya13"); // 号機④ｻﾔNo13
        mapping.put("gouki4saya14", "gouki4saya14"); // 号機④ｻﾔNo14
        mapping.put("gouki4saya15", "gouki4saya15"); // 号機④ｻﾔNo15
        mapping.put("gouki4saya16", "gouki4saya16"); // 号機④ｻﾔNo16
        mapping.put("gouki4saya17", "gouki4saya17"); // 号機④ｻﾔNo17
        mapping.put("gouki4saya18", "gouki4saya18"); // 号機④ｻﾔNo18
        mapping.put("gouki4saya19", "gouki4saya19"); // 号機④ｻﾔNo19
        mapping.put("gouki4saya20", "gouki4saya20"); // 号機④ｻﾔNo20
        mapping.put("gouki4check1", "gouki4check1"); // 号機④ﾁｪｯｸ1
        mapping.put("gouki4check2", "gouki4check2"); // 号機④ﾁｪｯｸ2
        mapping.put("gouki4check3", "gouki4check3"); // 号機④ﾁｪｯｸ3
        mapping.put("gouki4check4", "gouki4check4"); // 号機④ﾁｪｯｸ4
        mapping.put("gouki4check5", "gouki4check5"); // 号機④ﾁｪｯｸ5
        mapping.put("gouki4check6", "gouki4check6"); // 号機④ﾁｪｯｸ6
        mapping.put("gouki4check7", "gouki4check7"); // 号機④ﾁｪｯｸ7
        mapping.put("gouki4check8", "gouki4check8"); // 号機④ﾁｪｯｸ8
        mapping.put("gouki4check9", "gouki4check9"); // 号機④ﾁｪｯｸ9
        mapping.put("gouki4check10", "gouki4check10"); // 号機④ﾁｪｯｸ10
        mapping.put("gouki4check11", "gouki4check11"); // 号機④ﾁｪｯｸ11
        mapping.put("gouki4check12", "gouki4check12"); // 号機④ﾁｪｯｸ12
        mapping.put("gouki4check13", "gouki4check13"); // 号機④ﾁｪｯｸ13
        mapping.put("gouki4check14", "gouki4check14"); // 号機④ﾁｪｯｸ14
        mapping.put("gouki4check15", "gouki4check15"); // 号機④ﾁｪｯｸ15
        mapping.put("gouki4check16", "gouki4check16"); // 号機④ﾁｪｯｸ16
        mapping.put("gouki4check17", "gouki4check17"); // 号機④ﾁｪｯｸ17
        mapping.put("gouki4check18", "gouki4check18"); // 号機④ﾁｪｯｸ18
        mapping.put("gouki4check19", "gouki4check19"); // 号機④ﾁｪｯｸ19
        mapping.put("gouki4check20", "gouki4check20"); // 号機④ﾁｪｯｸ20
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除フラグ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrShinkuukansou>> beanHandler = new BeanListHandler<>(SubSrShinkuukansou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [熱処理_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SubSrShinkuukansou> loadTmpSubSrShinkuukansou(QueryRunner queryRunnerQcdb, 
            String kojyo, String lotNo, String edaban, int jissekino, String rev) throws SQLException {
        
        String sql = "SELECT kojyo, lotno, edaban, kaisuu, gouki1saya1, gouki1saya2, gouki1saya3, "
                + "gouki1saya4, gouki1saya5, gouki1saya6, gouki1saya7, gouki1saya8, gouki1saya9, "
                + "gouki1saya10, gouki1saya11, gouki1saya12, gouki1saya13, gouki1saya14, "                
                + "gouki1saya15, gouki1saya16, gouki1saya17, gouki1saya18, gouki1saya19, "                
                + "gouki1saya20, gouki1check1, gouki1check2, gouki1check3, gouki1check4, "
                + "gouki1check5, gouki1check6, gouki1check7, gouki1check8, gouki1check9, "
                + "gouki1check10, gouki1check11, gouki1check12, gouki1check13, gouki1check14, "
                + "gouki1check15, gouki1check16, gouki1check17, gouki1check18, gouki1check19, "
                + "gouki1check20, gouki2saya1, gouki2saya2, gouki2saya3, gouki2saya4, gouki2saya5, "
                + "gouki2saya6, gouki2saya7, gouki2saya8, gouki2saya9, gouki2saya10, gouki2saya11, "
                + "gouki2saya12, gouki2saya13, gouki2saya14, gouki2saya15, gouki2saya16, gouki2saya17, "
                + "gouki2saya18, gouki2saya19, gouki2saya20, gouki2check1, "
                + "gouki2check2, gouki2check3, gouki2check4, gouki2check5, gouki2check6, "
                + "gouki2check7, gouki2check8, gouki2check9, gouki2check10, gouki2check11, "
                + "gouki2check12, gouki2check13, gouki2check14, gouki2check15, gouki2check16, "
                + "gouki2check17, gouki2check18, gouki2check19, gouki2check20, gouki3saya1, "
                + "gouki3saya2, gouki3saya3, gouki3saya4, gouki3saya5, gouki3saya6, gouki3saya7, "
                + "gouki3saya8, gouki3saya9, gouki3saya10, gouki3saya11, gouki3saya12, "
                + "gouki3saya13, gouki3saya14, gouki3saya15, gouki3saya16, gouki3saya17, "
                + "gouki3saya18, gouki3saya19, gouki3saya20, gouki3check1, gouki3check2, "
                + "gouki3check3, gouki3check4, gouki3check5, gouki3check6, gouki3check7, "
                + "gouki3check8, gouki3check9, gouki3check10, gouki3check11, gouki3check12, "
                + "gouki3check13, gouki3check14, gouki3check15, gouki3check16, gouki3check17, "
                + "gouki3check18, gouki3check19, gouki3check20, gouki4saya1, gouki4saya2, "
                + "gouki4saya3, gouki4saya4, gouki4saya5, gouki4saya6, gouki4saya7, gouki4saya8, "
                + "gouki4saya9, gouki4saya10, gouki4saya11, gouki4saya12, gouki4saya13, "
                + "gouki4saya14, gouki4saya15, gouki4saya16, gouki4saya17, gouki4saya18, "
                + "gouki4saya19, gouki4saya20, gouki4check1, gouki4check2, gouki4check3, "
                + "gouki4check4, gouki4check5, gouki4check6, gouki4check7, gouki4check8, "
                + "gouki4check9, gouki4check10, gouki4check11, gouki4check12, "
                + "gouki4check13, gouki4check14, gouki4check15, gouki4check16, gouki4check17, "
                + "gouki4check18, gouki4check19, gouki4check20, torokunichiji, kosinnichiji, revision, deleteflag "
                + "FROM tmp_sub_sr_shinkuukansou "
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
        mapping.put("gouki1saya1", "gouki1saya1"); // 号機①ｻﾔNo1
        mapping.put("gouki1saya2", "gouki1saya2"); // 号機①ｻﾔNo2
        mapping.put("gouki1saya3", "gouki1saya3"); // 号機①ｻﾔNo3
        mapping.put("gouki1saya4", "gouki1saya4"); // 号機①ｻﾔNo4
        mapping.put("gouki1saya5", "gouki1saya5"); // 号機①ｻﾔNo5
        mapping.put("gouki1saya6", "gouki1saya6"); // 号機①ｻﾔNo6
        mapping.put("gouki1saya7", "gouki1saya7"); // 号機①ｻﾔNo7
        mapping.put("gouki1saya8", "gouki1saya8"); // 号機①ｻﾔNo8
        mapping.put("gouki1saya9", "gouki1saya9"); // 号機①ｻﾔNo9
        mapping.put("gouki1saya10", "gouki1saya10"); // 号機①ｻﾔNo10
        mapping.put("gouki1saya11", "gouki1saya11"); // 号機①ｻﾔNo11
        mapping.put("gouki1saya12", "gouki1saya12"); // 号機①ｻﾔNo12
        mapping.put("gouki1saya13", "gouki1saya13"); // 号機①ｻﾔNo13
        mapping.put("gouki1saya14", "gouki1saya14"); // 号機①ｻﾔNo14
        mapping.put("gouki1saya15", "gouki1saya15"); // 号機①ｻﾔNo15
        mapping.put("gouki1saya16", "gouki1saya16"); // 号機①ｻﾔNo16
        mapping.put("gouki1saya17", "gouki1saya17"); // 号機①ｻﾔNo17
        mapping.put("gouki1saya18", "gouki1saya18"); // 号機①ｻﾔNo18
        mapping.put("gouki1saya19", "gouki1saya19"); // 号機①ｻﾔNo19
        mapping.put("gouki1saya20", "gouki1saya20"); // 号機①ｻﾔNo20
        mapping.put("gouki1check1", "gouki1check1"); // 号機①ﾁｪｯｸ1
        mapping.put("gouki1check2", "gouki1check2"); // 号機①ﾁｪｯｸ2
        mapping.put("gouki1check3", "gouki1check3"); // 号機①ﾁｪｯｸ3
        mapping.put("gouki1check4", "gouki1check4"); // 号機①ﾁｪｯｸ4
        mapping.put("gouki1check5", "gouki1check5"); // 号機①ﾁｪｯｸ5
        mapping.put("gouki1check6", "gouki1check6"); // 号機①ﾁｪｯｸ6
        mapping.put("gouki1check7", "gouki1check7"); // 号機①ﾁｪｯｸ7
        mapping.put("gouki1check8", "gouki1check8"); // 号機①ﾁｪｯｸ8
        mapping.put("gouki1check9", "gouki1check9"); // 号機①ﾁｪｯｸ9
        mapping.put("gouki1check10", "gouki1check10"); // 号機①ﾁｪｯｸ10
        mapping.put("gouki1check11", "gouki1check11"); // 号機①ﾁｪｯｸ11
        mapping.put("gouki1check12", "gouki1check12"); // 号機①ﾁｪｯｸ12
        mapping.put("gouki1check13", "gouki1check13"); // 号機①ﾁｪｯｸ13
        mapping.put("gouki1check14", "gouki1check14"); // 号機①ﾁｪｯｸ14
        mapping.put("gouki1check15", "gouki1check15"); // 号機①ﾁｪｯｸ15
        mapping.put("gouki1check16", "gouki1check16"); // 号機①ﾁｪｯｸ16
        mapping.put("gouki1check17", "gouki1check17"); // 号機①ﾁｪｯｸ17
        mapping.put("gouki1check18", "gouki1check18"); // 号機①ﾁｪｯｸ18
        mapping.put("gouki1check19", "gouki1check19"); // 号機①ﾁｪｯｸ19
        mapping.put("gouki1check20", "gouki1check20"); // 号機①ﾁｪｯｸ20
        mapping.put("gouki2saya1", "gouki2saya1"); // 号機②ｻﾔNo1
        mapping.put("gouki2saya2", "gouki2saya2"); // 号機②ｻﾔNo2
        mapping.put("gouki2saya3", "gouki2saya3"); // 号機②ｻﾔNo3
        mapping.put("gouki2saya4", "gouki2saya4"); // 号機②ｻﾔNo4
        mapping.put("gouki2saya5", "gouki2saya5"); // 号機②ｻﾔNo5
        mapping.put("gouki2saya6", "gouki2saya6"); // 号機②ｻﾔNo6
        mapping.put("gouki2saya7", "gouki2saya7"); // 号機②ｻﾔNo7
        mapping.put("gouki2saya8", "gouki2saya8"); // 号機②ｻﾔNo8
        mapping.put("gouki2saya9", "gouki2saya9"); // 号機②ｻﾔNo9
        mapping.put("gouki2saya10", "gouki2saya10"); // 号機②ｻﾔNo10
        mapping.put("gouki2saya11", "gouki2saya11"); // 号機②ｻﾔNo11
        mapping.put("gouki2saya12", "gouki2saya12"); // 号機②ｻﾔNo12
        mapping.put("gouki2saya13", "gouki2saya13"); // 号機②ｻﾔNo13
        mapping.put("gouki2saya14", "gouki2saya14"); // 号機②ｻﾔNo14
        mapping.put("gouki2saya15", "gouki2saya15"); // 号機②ｻﾔNo15
        mapping.put("gouki2saya16", "gouki2saya16"); // 号機②ｻﾔNo16
        mapping.put("gouki2saya17", "gouki2saya17"); // 号機②ｻﾔNo17
        mapping.put("gouki2saya18", "gouki2saya18"); // 号機②ｻﾔNo18
        mapping.put("gouki2saya19", "gouki2saya19"); // 号機②ｻﾔNo19
        mapping.put("gouki2saya20", "gouki2saya20"); // 号機②ｻﾔNo20
        mapping.put("gouki2check1", "gouki2check1"); // 号機②ﾁｪｯｸ1
        mapping.put("gouki2check2", "gouki2check2"); // 号機②ﾁｪｯｸ2
        mapping.put("gouki2check3", "gouki2check3"); // 号機②ﾁｪｯｸ3
        mapping.put("gouki2check4", "gouki2check4"); // 号機②ﾁｪｯｸ4
        mapping.put("gouki2check5", "gouki2check5"); // 号機②ﾁｪｯｸ5
        mapping.put("gouki2check6", "gouki2check6"); // 号機②ﾁｪｯｸ6
        mapping.put("gouki2check7", "gouki2check7"); // 号機②ﾁｪｯｸ7
        mapping.put("gouki2check8", "gouki2check8"); // 号機②ﾁｪｯｸ8
        mapping.put("gouki2check9", "gouki2check9"); // 号機②ﾁｪｯｸ9
        mapping.put("gouki2check10", "gouki2check10"); // 号機②ﾁｪｯｸ10
        mapping.put("gouki2check11", "gouki2check11"); // 号機②ﾁｪｯｸ11
        mapping.put("gouki2check12", "gouki2check12"); // 号機②ﾁｪｯｸ12
        mapping.put("gouki2check13", "gouki2check13"); // 号機②ﾁｪｯｸ13
        mapping.put("gouki2check14", "gouki2check14"); // 号機②ﾁｪｯｸ14
        mapping.put("gouki2check15", "gouki2check15"); // 号機②ﾁｪｯｸ15
        mapping.put("gouki2check16", "gouki2check16"); // 号機②ﾁｪｯｸ16
        mapping.put("gouki2check17", "gouki2check17"); // 号機②ﾁｪｯｸ17
        mapping.put("gouki2check18", "gouki2check18"); // 号機②ﾁｪｯｸ18
        mapping.put("gouki2check19", "gouki2check19"); // 号機②ﾁｪｯｸ19
        mapping.put("gouki2check20", "gouki2check20"); // 号機②ﾁｪｯｸ20
        mapping.put("gouki3saya1", "gouki3saya1"); // 号機③ｻﾔNo1
        mapping.put("gouki3saya2", "gouki3saya2"); // 号機③ｻﾔNo2
        mapping.put("gouki3saya3", "gouki3saya3"); // 号機③ｻﾔNo3
        mapping.put("gouki3saya4", "gouki3saya4"); // 号機③ｻﾔNo4
        mapping.put("gouki3saya5", "gouki3saya5"); // 号機③ｻﾔNo5
        mapping.put("gouki3saya6", "gouki3saya6"); // 号機③ｻﾔNo6
        mapping.put("gouki3saya7", "gouki3saya7"); // 号機③ｻﾔNo7
        mapping.put("gouki3saya8", "gouki3saya8"); // 号機③ｻﾔNo8
        mapping.put("gouki3saya9", "gouki3saya9"); // 号機③ｻﾔNo9
        mapping.put("gouki3saya10", "gouki3saya10"); // 号機③ｻﾔNo10
        mapping.put("gouki3saya11", "gouki3saya11"); // 号機③ｻﾔNo11
        mapping.put("gouki3saya12", "gouki3saya12"); // 号機③ｻﾔNo12
        mapping.put("gouki3saya13", "gouki3saya13"); // 号機③ｻﾔNo13
        mapping.put("gouki3saya14", "gouki3saya14"); // 号機③ｻﾔNo14
        mapping.put("gouki3saya15", "gouki3saya15"); // 号機③ｻﾔNo15
        mapping.put("gouki3saya16", "gouki3saya16"); // 号機③ｻﾔNo16
        mapping.put("gouki3saya17", "gouki3saya17"); // 号機③ｻﾔNo17
        mapping.put("gouki3saya18", "gouki3saya18"); // 号機③ｻﾔNo18
        mapping.put("gouki3saya19", "gouki3saya19"); // 号機③ｻﾔNo19
        mapping.put("gouki3saya20", "gouki3saya20"); // 号機③ｻﾔNo20
        mapping.put("gouki3check1", "gouki3check1"); // 号機③ﾁｪｯｸ1
        mapping.put("gouki3check2", "gouki3check2"); // 号機③ﾁｪｯｸ2
        mapping.put("gouki3check3", "gouki3check3"); // 号機③ﾁｪｯｸ3
        mapping.put("gouki3check4", "gouki3check4"); // 号機③ﾁｪｯｸ4
        mapping.put("gouki3check5", "gouki3check5"); // 号機③ﾁｪｯｸ5
        mapping.put("gouki3check6", "gouki3check6"); // 号機③ﾁｪｯｸ6
        mapping.put("gouki3check7", "gouki3check7"); // 号機③ﾁｪｯｸ7
        mapping.put("gouki3check8", "gouki3check8"); // 号機③ﾁｪｯｸ8
        mapping.put("gouki3check9", "gouki3check9"); // 号機③ﾁｪｯｸ9
        mapping.put("gouki3check10", "gouki3check10"); // 号機③ﾁｪｯｸ10
        mapping.put("gouki3check11", "gouki3check11"); // 号機③ﾁｪｯｸ11
        mapping.put("gouki3check12", "gouki3check12"); // 号機③ﾁｪｯｸ12
        mapping.put("gouki3check13", "gouki3check13"); // 号機③ﾁｪｯｸ13
        mapping.put("gouki3check14", "gouki3check14"); // 号機③ﾁｪｯｸ14
        mapping.put("gouki3check15", "gouki3check15"); // 号機③ﾁｪｯｸ15
        mapping.put("gouki3check16", "gouki3check16"); // 号機③ﾁｪｯｸ16
        mapping.put("gouki3check17", "gouki3check17"); // 号機③ﾁｪｯｸ17
        mapping.put("gouki3check18", "gouki3check18"); // 号機③ﾁｪｯｸ18
        mapping.put("gouki3check19", "gouki3check19"); // 号機③ﾁｪｯｸ19
        mapping.put("gouki3check20", "gouki3check20"); // 号機③ﾁｪｯｸ20
        mapping.put("gouki4saya1", "gouki4saya1"); // 号機④ｻﾔNo1
        mapping.put("gouki4saya2", "gouki4saya2"); // 号機④ｻﾔNo2
        mapping.put("gouki4saya3", "gouki4saya3"); // 号機④ｻﾔNo3
        mapping.put("gouki4saya4", "gouki4saya4"); // 号機④ｻﾔNo4
        mapping.put("gouki4saya5", "gouki4saya5"); // 号機④ｻﾔNo5
        mapping.put("gouki4saya6", "gouki4saya6"); // 号機④ｻﾔNo6
        mapping.put("gouki4saya7", "gouki4saya7"); // 号機④ｻﾔNo7
        mapping.put("gouki4saya8", "gouki4saya8"); // 号機④ｻﾔNo8
        mapping.put("gouki4saya9", "gouki4saya9"); // 号機④ｻﾔNo9
        mapping.put("gouki4saya10", "gouki4saya10"); // 号機④ｻﾔNo10
        mapping.put("gouki4saya11", "gouki4saya11"); // 号機④ｻﾔNo11
        mapping.put("gouki4saya12", "gouki4saya12"); // 号機④ｻﾔNo12
        mapping.put("gouki4saya13", "gouki4saya13"); // 号機④ｻﾔNo13
        mapping.put("gouki4saya14", "gouki4saya14"); // 号機④ｻﾔNo14
        mapping.put("gouki4saya15", "gouki4saya15"); // 号機④ｻﾔNo15
        mapping.put("gouki4saya16", "gouki4saya16"); // 号機④ｻﾔNo16
        mapping.put("gouki4saya17", "gouki4saya17"); // 号機④ｻﾔNo17
        mapping.put("gouki4saya18", "gouki4saya18"); // 号機④ｻﾔNo18
        mapping.put("gouki4saya19", "gouki4saya19"); // 号機④ｻﾔNo19
        mapping.put("gouki4saya20", "gouki4saya20"); // 号機④ｻﾔNo20
        mapping.put("gouki4check1", "gouki4check1"); // 号機④ﾁｪｯｸ1
        mapping.put("gouki4check2", "gouki4check2"); // 号機④ﾁｪｯｸ2
        mapping.put("gouki4check3", "gouki4check3"); // 号機④ﾁｪｯｸ3
        mapping.put("gouki4check4", "gouki4check4"); // 号機④ﾁｪｯｸ4
        mapping.put("gouki4check5", "gouki4check5"); // 号機④ﾁｪｯｸ5
        mapping.put("gouki4check6", "gouki4check6"); // 号機④ﾁｪｯｸ6
        mapping.put("gouki4check7", "gouki4check7"); // 号機④ﾁｪｯｸ7
        mapping.put("gouki4check8", "gouki4check8"); // 号機④ﾁｪｯｸ8
        mapping.put("gouki4check9", "gouki4check9"); // 号機④ﾁｪｯｸ9
        mapping.put("gouki4check10", "gouki4check10"); // 号機④ﾁｪｯｸ10
        mapping.put("gouki4check11", "gouki4check11"); // 号機④ﾁｪｯｸ11
        mapping.put("gouki4check12", "gouki4check12"); // 号機④ﾁｪｯｸ12
        mapping.put("gouki4check13", "gouki4check13"); // 号機④ﾁｪｯｸ13
        mapping.put("gouki4check14", "gouki4check14"); // 号機④ﾁｪｯｸ14
        mapping.put("gouki4check15", "gouki4check15"); // 号機④ﾁｪｯｸ15
        mapping.put("gouki4check16", "gouki4check16"); // 号機④ﾁｪｯｸ16
        mapping.put("gouki4check17", "gouki4check17"); // 号機④ﾁｪｯｸ17
        mapping.put("gouki4check18", "gouki4check18"); // 号機④ﾁｪｯｸ18
        mapping.put("gouki4check19", "gouki4check19"); // 号機④ﾁｪｯｸ19
        mapping.put("gouki4check20", "gouki4check20"); // 号機④ﾁｪｯｸ20
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除フラグ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrShinkuukansou>> beanHandler = new BeanListHandler<>(SubSrShinkuukansou.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * 熱処理_サブ画面_仮登録(tmp_sub_sr_shinkuukansou)登録処理
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
    private void insertTmpSubSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, 
            Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        
        String sql = "INSERT INTO tmp_sub_sr_shinkuukansou ("
                + "kojyo, lotno, edaban, kaisuu, gouki1saya1, gouki1saya2, gouki1saya3, "
                + "gouki1saya4, gouki1saya5, gouki1saya6, gouki1saya7, gouki1saya8, gouki1saya9, "
                + "gouki1saya10, gouki1saya11, gouki1saya12, gouki1saya13, gouki1saya14, "                
                + "gouki1saya15, gouki1saya16, gouki1saya17, gouki1saya18, gouki1saya19, "                
                + "gouki1saya20, gouki1check1, gouki1check2, gouki1check3, gouki1check4, "
                + "gouki1check5, gouki1check6, gouki1check7, gouki1check8, gouki1check9, "
                + "gouki1check10, gouki1check11, gouki1check12, gouki1check13, gouki1check14, "
                + "gouki1check15, gouki1check16, gouki1check17, gouki1check18, gouki1check19, "
                + "gouki1check20, gouki2saya1, gouki2saya2, gouki2saya3, gouki2saya4, gouki2saya5, "
                + "gouki2saya6, gouki2saya7, gouki2saya8, gouki2saya9, gouki2saya10, gouki2saya11, "
                + "gouki2saya12, gouki2saya13, gouki2saya14, gouki2saya15, gouki2saya16, gouki2saya17, "
                + "gouki2saya18, gouki2saya19, gouki2saya20, gouki2check1, "
                + "gouki2check2, gouki2check3, gouki2check4, gouki2check5, gouki2check6, "
                + "gouki2check7, gouki2check8, gouki2check9, gouki2check10, gouki2check11, "
                + "gouki2check12, gouki2check13, gouki2check14, gouki2check15, gouki2check16, "
                + "gouki2check17, gouki2check18, gouki2check19, gouki2check20, gouki3saya1, "
                + "gouki3saya2, gouki3saya3, gouki3saya4, gouki3saya5, gouki3saya6, gouki3saya7, "
                + "gouki3saya8, gouki3saya9, gouki3saya10, gouki3saya11, gouki3saya12, "
                + "gouki3saya13, gouki3saya14, gouki3saya15, gouki3saya16, gouki3saya17, "
                + "gouki3saya18, gouki3saya19, gouki3saya20, gouki3check1, gouki3check2, "
                + "gouki3check3, gouki3check4, gouki3check5, gouki3check6, gouki3check7, "
                + "gouki3check8, gouki3check9, gouki3check10, gouki3check11, gouki3check12, "
                + "gouki3check13, gouki3check14, gouki3check15, gouki3check16, gouki3check17, "
                + "gouki3check18, gouki3check19, gouki3check20, gouki4saya1, gouki4saya2, "
                + "gouki4saya3, gouki4saya4, gouki4saya5, gouki4saya6, gouki4saya7, gouki4saya8, "
                + "gouki4saya9, gouki4saya10, gouki4saya11, gouki4saya12, gouki4saya13, "
                + "gouki4saya14, gouki4saya15, gouki4saya16, gouki4saya17, gouki4saya18, "
                + "gouki4saya19, gouki4saya20, gouki4check1, gouki4check2, gouki4check3, "
                + "gouki4check4, gouki4check5, gouki4check6, gouki4check7, gouki4check8, "
                + "gouki4check9, gouki4check10, gouki4check11, gouki4check12, "
                + "gouki4check13, gouki4check14, gouki4check15, gouki4check16, gouki4check17, gouki4check18, "
                + "gouki4check19, gouki4check20, torokunichiji, kosinnichiji, revision, deleteflag"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?)";

        List<Object> params = setUpdateParameterTmpSubSrSrShinkuukansou(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 熱処理_仮登録(tmp_sr_shinkuukansou)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, 
            String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        
        String sql = "UPDATE tmp_sub_sr_shinkuukansou SET "
                + "gouki1saya1 = ?, gouki1saya2 = ?, gouki1saya3 = ?, gouki1saya4 = ?, gouki1saya5 = ?, "
                + "gouki1saya6 = ?, gouki1saya7 = ?, gouki1saya8 = ?, gouki1saya9 = ?, gouki1saya10 = ?, "
                + "gouki1saya11 = ?, gouki1saya12 = ?, gouki1saya13 = ?, gouki1saya14 = ?, gouki1saya15 = ?, "
                + "gouki1saya16 = ?, gouki1saya17 = ?, gouki1saya18 = ?, gouki1saya19 = ?, gouki1saya20 = ?, "
                + "gouki1check1 = ?, gouki1check2 = ?, gouki1check3 = ?, gouki1check4 = ?, gouki1check5 = ?, "
                + "gouki1check6 = ?, gouki1check7 = ?, gouki1check8 = ?, gouki1check9 = ?, gouki1check10 = ?, "
                + "gouki1check11 = ?, gouki1check12 = ?, gouki1check13 = ?, gouki1check14 = ?, gouki1check15 = ?, "
                + "gouki1check16 = ?, gouki1check17 = ?, gouki1check18 = ?, gouki1check19 = ?, gouki1check20 = ?, "
                + "gouki2saya1 = ?, gouki2saya2 = ?, gouki2saya3 = ?, gouki2saya4 = ?, gouki2saya5 = ?, "
                + "gouki2saya6 = ?, gouki2saya7 = ?, gouki2saya8 = ?, gouki2saya9 = ?, gouki2saya10 = ?, "
                + "gouki2saya11 = ?, gouki2saya12 = ?, gouki2saya13 = ?, gouki2saya14 = ?, gouki2saya15 = ?, "
                + "gouki2saya16 = ?, gouki2saya17 = ?, gouki2saya18 = ?, gouki2saya19 = ?, gouki2saya20 = ?, "
                + "gouki2check1 = ?, gouki2check2 = ?, gouki2check3 = ?, gouki2check4 = ?, gouki2check5 = ?, "
                + "gouki2check6 = ?, gouki2check7 = ?, gouki2check8 = ?, gouki2check9 = ?, gouki2check10 = ?, "
                + "gouki2check11 = ?, gouki2check12 = ?, gouki2check13 = ?, gouki2check14 = ?, gouki2check15 = ?, "
                + "gouki2check16 = ?, gouki2check17 = ?, gouki2check18 = ?, gouki2check19 = ?, gouki2check20 = ?, "
                + "gouki3saya1 = ?, gouki3saya2 = ?, gouki3saya3 = ?, gouki3saya4 = ?, gouki3saya5 = ?, "
                + "gouki3saya6 = ?, gouki3saya7 = ?, gouki3saya8 = ?, gouki3saya9 = ?, gouki3saya10 = ?, "
                + "gouki3saya11 = ?, gouki3saya12 = ?, gouki3saya13 = ?, gouki3saya14 = ?, gouki3saya15 = ?, "
                + "gouki3saya16 = ?, gouki3saya17 = ?, gouki3saya18 = ?, gouki3saya19 = ?, gouki3saya20 = ?, "
                + "gouki3check1 = ?, gouki3check2 = ?, gouki3check3 = ?, gouki3check4 = ?, gouki3check5 = ?, "
                + "gouki3check6 = ?, gouki3check7 = ?, gouki3check8 = ?, gouki3check9 = ?, gouki3check10 = ?, "
                + "gouki3check11 = ?, gouki3check12 = ?, gouki3check13 = ?, gouki3check14 = ?, gouki3check15 = ?, "
                + "gouki3check16 = ?, gouki3check17 = ?, gouki3check18 = ?, gouki3check19 = ?, gouki3check20 = ?, "
                + "gouki4saya1 = ?, gouki4saya2 = ?, gouki4saya3 = ?, gouki4saya4 = ?, gouki4saya5 = ?, "
                + "gouki4saya6 = ?, gouki4saya7 = ?, gouki4saya8 = ?, gouki4saya9 = ?, gouki4saya10 = ?, "
                + "gouki4saya11 = ?, gouki4saya12 = ?, gouki4saya13 = ?, gouki4saya14 = ?, gouki4saya15 = ?, "
                + "gouki4saya16 = ?, gouki4saya17 = ?, gouki4saya18 = ?, gouki4saya19 = ?, gouki4saya20 = ?, "
                + "gouki4check1 = ?, gouki4check2 = ?, gouki4check3 = ?, gouki4check4 = ?, gouki4check5 = ?, "
                + "gouki4check6 = ?, gouki4check7 = ?, gouki4check8 = ?, gouki4check9 = ?, gouki4check10 = ?, "
                + "gouki4check11 = ?, gouki4check12 = ?, gouki4check13 = ?, gouki4check14 = ?, gouki4check15 = ?, "
                + "gouki4check16 = ?, gouki4check17 = ?, gouki4check18 = ?, gouki4check19 = ?, gouki4check20 = ?, "
                + "kosinnichiji = ?, revision = ?, deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";
        
        List<Object> params = setUpdateParameterTmpSubSrSrShinkuukansou(false, newRev, 0, kojyo, lotNo, edaban, systemTime, null);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 熱処理_サブ画面仮登録(tmp_sub_sr_shinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrSrShinkuukansou(boolean isInsert, BigDecimal newRev, 
            int deleteflag, String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) {
        
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C018 beanGXHDO101C018 = (GXHDO101C018) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C018);
        List<GXHDO101C018Model.GoukiData> gouki1DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki1DataList();
        List<GXHDO101C018Model.GoukiData> gouki2DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki2DataList();
        List<GXHDO101C018Model.GoukiData> gouki3DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki3DataList();
        List<GXHDO101C018Model.GoukiData> gouki4DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki4DataList();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B043Const.KAISUU, null))); // 回数
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(0).getItemValue())); //号機①ｻﾔNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(1).getItemValue())); //号機①ｻﾔNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(2).getItemValue())); //号機①ｻﾔNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(3).getItemValue())); //号機①ｻﾔNo4
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(4).getItemValue())); //号機①ｻﾔNo5
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(5).getItemValue())); //号機①ｻﾔNo6
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(6).getItemValue())); //号機①ｻﾔNo7
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(7).getItemValue())); //号機①ｻﾔNo8
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(8).getItemValue())); //号機①ｻﾔNo9
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(9).getItemValue())); //号機①ｻﾔNo10
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(10).getItemValue())); //号機①ｻﾔNo11
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(11).getItemValue())); //号機①ｻﾔNo12
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(12).getItemValue())); //号機①ｻﾔNo13
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(13).getItemValue())); //号機①ｻﾔNo14
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(14).getItemValue())); //号機①ｻﾔNo15
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(15).getItemValue())); //号機①ｻﾔNo16
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(16).getItemValue())); //号機①ｻﾔNo17
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(17).getItemValue())); //号機①ｻﾔNo18
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(18).getItemValue())); //号機①ｻﾔNo19
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki1DataList.get(19).getItemValue())); //号機①ｻﾔNo20
        params.add(getCheckBoxDbValue(gouki1DataList.get(0).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki1DataList.get(1).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki1DataList.get(2).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki1DataList.get(3).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki1DataList.get(4).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki1DataList.get(5).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki1DataList.get(6).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki1DataList.get(7).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki1DataList.get(8).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki1DataList.get(9).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki1DataList.get(10).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki1DataList.get(11).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki1DataList.get(12).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki1DataList.get(13).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki1DataList.get(14).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki1DataList.get(15).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki1DataList.get(16).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki1DataList.get(17).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki1DataList.get(18).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki1DataList.get(19).getCheckBoxValue(), null)); //号機①ﾁｪｯｸ20
        
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(0).getItemValue())); //号機②ｻﾔNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(1).getItemValue())); //号機②ｻﾔNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(2).getItemValue())); //号機②ｻﾔNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(3).getItemValue())); //号機②ｻﾔNo4
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(4).getItemValue())); //号機②ｻﾔNo5
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(5).getItemValue())); //号機②ｻﾔNo6
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(6).getItemValue())); //号機②ｻﾔNo7
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(7).getItemValue())); //号機②ｻﾔNo8
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(8).getItemValue())); //号機②ｻﾔNo9
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(9).getItemValue())); //号機②ｻﾔNo10
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(10).getItemValue())); //号機②ｻﾔNo11
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(11).getItemValue())); //号機②ｻﾔNo12
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(12).getItemValue())); //号機②ｻﾔNo13
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(13).getItemValue())); //号機②ｻﾔNo14
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(14).getItemValue())); //号機②ｻﾔNo15
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(15).getItemValue())); //号機②ｻﾔNo16
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(16).getItemValue())); //号機②ｻﾔNo17
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(17).getItemValue())); //号機②ｻﾔNo18
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(18).getItemValue())); //号機②ｻﾔNo19
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki2DataList.get(19).getItemValue())); //号機②ｻﾔNo20
        
        params.add(getCheckBoxDbValue(gouki2DataList.get(0).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki2DataList.get(1).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki2DataList.get(2).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki2DataList.get(3).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki2DataList.get(4).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki2DataList.get(5).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki2DataList.get(6).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki2DataList.get(7).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki2DataList.get(8).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki2DataList.get(9).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki2DataList.get(10).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki2DataList.get(11).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki2DataList.get(12).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki2DataList.get(13).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki2DataList.get(14).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki2DataList.get(15).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki2DataList.get(16).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki2DataList.get(17).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki2DataList.get(18).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki2DataList.get(19).getCheckBoxValue(), null)); //号機②ﾁｪｯｸ20

        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(0).getItemValue())); //号機③ｻﾔNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(1).getItemValue())); //号機③ｻﾔNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(2).getItemValue())); //号機③ｻﾔNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(3).getItemValue())); //号機③ｻﾔNo4
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(4).getItemValue())); //号機③ｻﾔNo5
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(5).getItemValue())); //号機③ｻﾔNo6
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(6).getItemValue())); //号機③ｻﾔNo7
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(7).getItemValue())); //号機③ｻﾔNo8
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(8).getItemValue())); //号機③ｻﾔNo9
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(9).getItemValue())); //号機③ｻﾔNo10
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(10).getItemValue())); //号機③ｻﾔNo11
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(11).getItemValue())); //号機③ｻﾔNo12
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(12).getItemValue())); //号機③ｻﾔNo13
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(13).getItemValue())); //号機③ｻﾔNo14
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(14).getItemValue())); //号機③ｻﾔNo15
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(15).getItemValue())); //号機③ｻﾔNo16
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(16).getItemValue())); //号機③ｻﾔNo17
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(17).getItemValue())); //号機③ｻﾔNo18
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(18).getItemValue())); //号機③ｻﾔNo19
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki3DataList.get(19).getItemValue())); //号機③ｻﾔNo20

        params.add(getCheckBoxDbValue(gouki3DataList.get(0).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki3DataList.get(1).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki3DataList.get(2).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki3DataList.get(3).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki3DataList.get(4).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki3DataList.get(5).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki3DataList.get(6).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki3DataList.get(7).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki3DataList.get(8).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki3DataList.get(9).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki3DataList.get(10).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki3DataList.get(11).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki3DataList.get(12).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki3DataList.get(13).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki3DataList.get(14).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki3DataList.get(15).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki3DataList.get(16).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki3DataList.get(17).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki3DataList.get(18).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki3DataList.get(19).getCheckBoxValue(), null)); //号機③ﾁｪｯｸ20

        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(0).getItemValue())); //号機④ｻﾔNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(1).getItemValue())); //号機④ｻﾔNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(2).getItemValue())); //号機④ｻﾔNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(3).getItemValue())); //号機④ｻﾔNo4
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(4).getItemValue())); //号機④ｻﾔNo5
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(5).getItemValue())); //号機④ｻﾔNo6
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(6).getItemValue())); //号機④ｻﾔNo7
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(7).getItemValue())); //号機④ｻﾔNo8
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(8).getItemValue())); //号機④ｻﾔNo9
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(9).getItemValue())); //号機④ｻﾔNo10
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(10).getItemValue())); //号機④ｻﾔNo11
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(11).getItemValue())); //号機④ｻﾔNo12
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(12).getItemValue())); //号機④ｻﾔNo13
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(13).getItemValue())); //号機④ｻﾔNo14
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(14).getItemValue())); //号機④ｻﾔNo15
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(15).getItemValue())); //号機④ｻﾔNo16
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(16).getItemValue())); //号機④ｻﾔNo17
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(17).getItemValue())); //号機④ｻﾔNo18
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(18).getItemValue())); //号機④ｻﾔNo19
        params.add(DBUtil.stringToStringObjectDefaultNull(gouki4DataList.get(19).getItemValue())); //号機④ｻﾔNo20
        params.add(getCheckBoxDbValue(gouki4DataList.get(0).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki4DataList.get(1).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki4DataList.get(2).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki4DataList.get(3).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki4DataList.get(4).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki4DataList.get(5).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki4DataList.get(6).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki4DataList.get(7).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki4DataList.get(8).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki4DataList.get(9).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki4DataList.get(10).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki4DataList.get(11).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki4DataList.get(12).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki4DataList.get(13).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki4DataList.get(14).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki4DataList.get(15).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki4DataList.get(16).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki4DataList.get(17).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki4DataList.get(18).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki4DataList.get(19).getCheckBoxValue(), null)); //号機④ﾁｪｯｸ20
        
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
     * 熱処理_サブ画面仮登録(tmp_sub_sr_shinkuukansou)削除処理
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
    private void deleteTmpSubSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        
        String sql = "DELETE FROM tmp_sub_sr_shinkuukansou "
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
     * 熱処理_サブ画面(sub_sr_shinkuukansou)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @throws SQLException 例外エラー
     */
    private void insertSubSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal newRev, String kojyo, String lotNo, String edaban, 
            Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        
        String sql = "INSERT INTO sub_sr_shinkuukansou ("
                + "kojyo, lotno, edaban, kaisuu, gouki1saya1, gouki1saya2, gouki1saya3, "
                + "gouki1saya4, gouki1saya5, gouki1saya6, gouki1saya7, gouki1saya8, gouki1saya9, "
                + "gouki1saya10, gouki1saya11, gouki1saya12, gouki1saya13, gouki1saya14, "                
                + "gouki1saya15, gouki1saya16, gouki1saya17, gouki1saya18, gouki1saya19, "                
                + "gouki1saya20, gouki1check1, gouki1check2, gouki1check3, gouki1check4, "
                + "gouki1check5, gouki1check6, gouki1check7, gouki1check8, gouki1check9, "
                + "gouki1check10, gouki1check11, gouki1check12, gouki1check13, gouki1check14, "
                + "gouki1check15, gouki1check16, gouki1check17, gouki1check18, gouki1check19, "
                + "gouki1check20, gouki2saya1, gouki2saya2, gouki2saya3, gouki2saya4, gouki2saya5, "
                + "gouki2saya6, gouki2saya7, gouki2saya8, gouki2saya9, gouki2saya10, gouki2saya11, "
                + "gouki2saya12, gouki2saya13, gouki2saya14, gouki2saya15, gouki2saya16, gouki2saya17, "
                + "gouki2saya18, gouki2saya19, gouki2saya20, gouki2check1, "
                + "gouki2check2, gouki2check3, gouki2check4, gouki2check5, gouki2check6, "
                + "gouki2check7, gouki2check8, gouki2check9, gouki2check10, gouki2check11, "
                + "gouki2check12, gouki2check13, gouki2check14, gouki2check15, gouki2check16, "
                + "gouki2check17, gouki2check18, gouki2check19, gouki2check20, gouki3saya1, "
                + "gouki3saya2, gouki3saya3, gouki3saya4, gouki3saya5, gouki3saya6, gouki3saya7, "
                + "gouki3saya8, gouki3saya9, gouki3saya10, gouki3saya11, gouki3saya12, "
                + "gouki3saya13, gouki3saya14, gouki3saya15, gouki3saya16, gouki3saya17, "
                + "gouki3saya18, gouki3saya19, gouki3saya20, gouki3check1, gouki3check2, "
                + "gouki3check3, gouki3check4, gouki3check5, gouki3check6, gouki3check7, "
                + "gouki3check8, gouki3check9, gouki3check10, gouki3check11, gouki3check12, "
                + "gouki3check13, gouki3check14, gouki3check15, gouki3check16, gouki3check17, "
                + "gouki3check18, gouki3check19, gouki3check20, gouki4saya1, gouki4saya2, "
                + "gouki4saya3, gouki4saya4, gouki4saya5, gouki4saya6, gouki4saya7, gouki4saya8, "
                + "gouki4saya9, gouki4saya10, gouki4saya11, gouki4saya12, gouki4saya13, "
                + "gouki4saya14, gouki4saya15, gouki4saya16, gouki4saya17, gouki4saya18, "
                + "gouki4saya19, gouki4saya20, gouki4check1, gouki4check2, gouki4check3, "
                + "gouki4check4, gouki4check5, gouki4check6, gouki4check7, gouki4check8, "
                + "gouki4check9, gouki4check10, gouki4check11, gouki4check12, "
                + "gouki4check13, gouki4check14, gouki4check15, gouki4check16, gouki4check17, "
                + "gouki4check18, gouki4check19, gouki4check20, torokunichiji, kosinnichiji, revision"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?)";

        List<Object> params = setUpdateParameterSubSrSrShinkuukansou(true, newRev, kojyo, lotNo, edaban, systemTime, itemList);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面(sub_sr_shinkuukansou)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param paramJissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSubSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, BigDecimal newRev, String kojyo, String lotNo, String edaban, 
            int paramJissekino, Timestamp systemTime) throws SQLException {
        
        String sql = "UPDATE sub_sr_shinkuukansou SET "
                + "gouki1saya1 = ?, gouki1saya2 = ?, gouki1saya3 = ?, gouki1saya4 = ?, gouki1saya5 = ?, "
                + "gouki1saya6 = ?, gouki1saya7 = ?, gouki1saya8 = ?, gouki1saya9 = ?, gouki1saya10 = ?, "
                + "gouki1saya11 = ?, gouki1saya12 = ?, gouki1saya13 = ?, gouki1saya14 = ?, gouki1saya15 = ?, "
                + "gouki1saya16 = ?, gouki1saya17 = ?, gouki1saya18 = ?, gouki1saya19 = ?, gouki1saya20 = ?, "
                + "gouki1check1 = ?, gouki1check2 = ?, gouki1check3 = ?, gouki1check4 = ?, gouki1check5 = ?, "
                + "gouki1check6 = ?, gouki1check7 = ?, gouki1check8 = ?, gouki1check9 = ?, gouki1check10 = ?, "
                + "gouki1check11 = ?, gouki1check12 = ?, gouki1check13 = ?, gouki1check14 = ?, gouki1check15 = ?, "
                + "gouki1check16 = ?, gouki1check17 = ?, gouki1check18 = ?, gouki1check19 = ?, gouki1check20 = ?, "
                + "gouki2saya1 = ?, gouki2saya2 = ?, gouki2saya3 = ?, gouki2saya4 = ?, gouki2saya5 = ?, "
                + "gouki2saya6 = ?, gouki2saya7 = ?, gouki2saya8 = ?, gouki2saya9 = ?, gouki2saya10 = ?, "
                + "gouki2saya11 = ?, gouki2saya12 = ?, gouki2saya13 = ?, gouki2saya14 = ?, gouki2saya15 = ?, "
                + "gouki2saya16 = ?, gouki2saya17 = ?, gouki2saya18 = ?, gouki2saya19 = ?, gouki2saya20 = ?, "
                + "gouki2check1 = ?, gouki2check2 = ?, gouki2check3 = ?, gouki2check4 = ?, gouki2check5 = ?, "
                + "gouki2check6 = ?, gouki2check7 = ?, gouki2check8 = ?, gouki2check9 = ?, gouki2check10 = ?, "
                + "gouki2check11 = ?, gouki2check12 = ?, gouki2check13 = ?, gouki2check14 = ?, gouki2check15 = ?, "
                + "gouki2check16 = ?, gouki2check17 = ?, gouki2check18 = ?, gouki2check19 = ?, gouki2check20 = ?, "
                + "gouki3saya1 = ?, gouki3saya2 = ?, gouki3saya3 = ?, gouki3saya4 = ?, gouki3saya5 = ?, "
                + "gouki3saya6 = ?, gouki3saya7 = ?, gouki3saya8 = ?, gouki3saya9 = ?, gouki3saya10 = ?, "
                + "gouki3saya11 = ?, gouki3saya12 = ?, gouki3saya13 = ?, gouki3saya14 = ?, gouki3saya15 = ?, "
                + "gouki3saya16 = ?, gouki3saya17 = ?, gouki3saya18 = ?, gouki3saya19 = ?, gouki3saya20 = ?, "
                + "gouki3check1 = ?, gouki3check2 = ?, gouki3check3 = ?, gouki3check4 = ?, gouki3check5 = ?, "
                + "gouki3check6 = ?, gouki3check7 = ?, gouki3check8 = ?, gouki3check9 = ?, gouki3check10 = ?, "
                + "gouki3check11 = ?, gouki3check12 = ?, gouki3check13 = ?, gouki3check14 = ?, gouki3check15 = ?, "
                + "gouki3check16 = ?, gouki3check17 = ?, gouki3check18 = ?, gouki3check19 = ?, gouki3check20 = ?, "
                + "gouki4saya1 = ?, gouki4saya2 = ?, gouki4saya3 = ?, gouki4saya4 = ?, gouki4saya5 = ?, "
                + "gouki4saya6 = ?, gouki4saya7 = ?, gouki4saya8 = ?, gouki4saya9 = ?, gouki4saya10 = ?, "
                + "gouki4saya11 = ?, gouki4saya12 = ?, gouki4saya13 = ?, gouki4saya14 = ?, gouki4saya15 = ?, "
                + "gouki4saya16 = ?, gouki4saya17 = ?, gouki4saya18 = ?, gouki4saya19 = ?, gouki4saya20 = ?, "
                + "gouki4check1 = ?, gouki4check2 = ?, gouki4check3 = ?, gouki4check4 = ?, gouki4check5 = ?, "
                + "gouki4check6 = ?, gouki4check7 = ?, gouki4check8 = ?, gouki4check9 = ?, gouki4check10 = ?, "
                + "gouki4check11 = ?, gouki4check12 = ?, gouki4check13 = ?, gouki4check14 = ?, gouki4check15 = ?, "
                + "gouki4check16 = ?, gouki4check17 = ?, gouki4check18 = ?, gouki4check19 = ?, gouki4check20 = ?, "
                + "kosinnichiji = ?, revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? ";
        
        List<Object> params = setUpdateParameterSubSrSrShinkuukansou(false, newRev, kojyo, lotNo, edaban, systemTime, null);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(paramJissekino);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }
    
    /**
     * 熱処理_サブ画面登録(tmp_sub_sr_shinkuukansou)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrSrShinkuukansou(boolean isInsert, BigDecimal newRev, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) {
        
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C018 beanGXHDO101C018 = (GXHDO101C018) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C018);
        List<GXHDO101C018Model.GoukiData> gouki1DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki1DataList();
        List<GXHDO101C018Model.GoukiData> gouki2DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki2DataList();
        List<GXHDO101C018Model.GoukiData> gouki3DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki3DataList();
        List<GXHDO101C018Model.GoukiData> gouki4DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki4DataList();
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B043Const.KAISUU, null))); // 回数
        }
        
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(0).getItemValue())); //号機①ｻﾔNo1
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(1).getItemValue())); //号機①ｻﾔNo2
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(2).getItemValue())); //号機①ｻﾔNo3
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(3).getItemValue())); //号機①ｻﾔNo4
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(4).getItemValue())); //号機①ｻﾔNo5
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(5).getItemValue())); //号機①ｻﾔNo6
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(6).getItemValue())); //号機①ｻﾔNo7
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(7).getItemValue())); //号機①ｻﾔNo8
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(8).getItemValue())); //号機①ｻﾔNo9
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(9).getItemValue())); //号機①ｻﾔNo10
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(10).getItemValue())); //号機①ｻﾔNo11
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(11).getItemValue())); //号機①ｻﾔNo12
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(12).getItemValue())); //号機①ｻﾔNo13
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(13).getItemValue())); //号機①ｻﾔNo14
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(14).getItemValue())); //号機①ｻﾔNo15
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(15).getItemValue())); //号機①ｻﾔNo16
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(16).getItemValue())); //号機①ｻﾔNo17
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(17).getItemValue())); //号機①ｻﾔNo18
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(18).getItemValue())); //号機①ｻﾔNo19
        params.add(DBUtil.stringToStringObject(gouki1DataList.get(19).getItemValue())); //号機①ｻﾔNo20
        params.add(getCheckBoxDbValue(gouki1DataList.get(0).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki1DataList.get(1).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki1DataList.get(2).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki1DataList.get(3).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki1DataList.get(4).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki1DataList.get(5).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki1DataList.get(6).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki1DataList.get(7).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki1DataList.get(8).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki1DataList.get(9).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki1DataList.get(10).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki1DataList.get(11).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki1DataList.get(12).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki1DataList.get(13).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki1DataList.get(14).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki1DataList.get(15).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki1DataList.get(16).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki1DataList.get(17).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki1DataList.get(18).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki1DataList.get(19).getCheckBoxValue(), 0)); //号機①ﾁｪｯｸ20
        
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(0).getItemValue())); //号機②ｻﾔNo1
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(1).getItemValue())); //号機②ｻﾔNo2
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(2).getItemValue())); //号機②ｻﾔNo3
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(3).getItemValue())); //号機②ｻﾔNo4
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(4).getItemValue())); //号機②ｻﾔNo5
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(5).getItemValue())); //号機②ｻﾔNo6
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(6).getItemValue())); //号機②ｻﾔNo7
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(7).getItemValue())); //号機②ｻﾔNo8
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(8).getItemValue())); //号機②ｻﾔNo9
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(9).getItemValue())); //号機②ｻﾔNo10
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(10).getItemValue())); //号機②ｻﾔNo11
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(11).getItemValue())); //号機②ｻﾔNo12
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(12).getItemValue())); //号機②ｻﾔNo13
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(13).getItemValue())); //号機②ｻﾔNo14
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(14).getItemValue())); //号機②ｻﾔNo15
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(15).getItemValue())); //号機②ｻﾔNo16
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(16).getItemValue())); //号機②ｻﾔNo17
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(17).getItemValue())); //号機②ｻﾔNo18
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(18).getItemValue())); //号機②ｻﾔNo19
        params.add(DBUtil.stringToStringObject(gouki2DataList.get(19).getItemValue())); //号機②ｻﾔNo20
        params.add(getCheckBoxDbValue(gouki2DataList.get(0).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki2DataList.get(1).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki2DataList.get(2).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki2DataList.get(3).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki2DataList.get(4).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki2DataList.get(5).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki2DataList.get(6).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki2DataList.get(7).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki2DataList.get(8).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki2DataList.get(9).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki2DataList.get(10).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki2DataList.get(11).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki2DataList.get(12).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki2DataList.get(13).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki2DataList.get(14).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki2DataList.get(15).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki2DataList.get(16).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki2DataList.get(17).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki2DataList.get(18).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki2DataList.get(19).getCheckBoxValue(), 0)); //号機②ﾁｪｯｸ20

        params.add(DBUtil.stringToStringObject(gouki3DataList.get(0).getItemValue())); //号機③ｻﾔNo1
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(1).getItemValue())); //号機③ｻﾔNo2
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(2).getItemValue())); //号機③ｻﾔNo3
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(3).getItemValue())); //号機③ｻﾔNo4
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(4).getItemValue())); //号機③ｻﾔNo5
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(5).getItemValue())); //号機③ｻﾔNo6
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(6).getItemValue())); //号機③ｻﾔNo7
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(7).getItemValue())); //号機③ｻﾔNo8
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(8).getItemValue())); //号機③ｻﾔNo9
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(9).getItemValue())); //号機③ｻﾔNo10
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(10).getItemValue())); //号機③ｻﾔNo11
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(11).getItemValue())); //号機③ｻﾔNo12
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(12).getItemValue())); //号機③ｻﾔNo13
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(13).getItemValue())); //号機③ｻﾔNo14
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(14).getItemValue())); //号機③ｻﾔNo15
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(15).getItemValue())); //号機③ｻﾔNo16
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(16).getItemValue())); //号機③ｻﾔNo17
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(17).getItemValue())); //号機③ｻﾔNo18
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(18).getItemValue())); //号機③ｻﾔNo19
        params.add(DBUtil.stringToStringObject(gouki3DataList.get(19).getItemValue())); //号機③ｻﾔNo20
        params.add(getCheckBoxDbValue(gouki3DataList.get(0).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki3DataList.get(1).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki3DataList.get(2).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki3DataList.get(3).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki3DataList.get(4).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki3DataList.get(5).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki3DataList.get(6).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki3DataList.get(7).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki3DataList.get(8).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki3DataList.get(9).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki3DataList.get(10).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki3DataList.get(11).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki3DataList.get(12).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki3DataList.get(13).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki3DataList.get(14).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki3DataList.get(15).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki3DataList.get(16).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki3DataList.get(17).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki3DataList.get(18).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki3DataList.get(19).getCheckBoxValue(), 0)); //号機③ﾁｪｯｸ20

        params.add(DBUtil.stringToStringObject(gouki4DataList.get(0).getItemValue())); //号機④ｻﾔNo1
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(1).getItemValue())); //号機④ｻﾔNo2
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(2).getItemValue())); //号機④ｻﾔNo3
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(3).getItemValue())); //号機④ｻﾔNo4
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(4).getItemValue())); //号機④ｻﾔNo5
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(5).getItemValue())); //号機④ｻﾔNo6
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(6).getItemValue())); //号機④ｻﾔNo7
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(7).getItemValue())); //号機④ｻﾔNo8
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(8).getItemValue())); //号機④ｻﾔNo9
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(9).getItemValue())); //号機④ｻﾔNo10
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(10).getItemValue())); //号機④ｻﾔNo11
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(11).getItemValue())); //号機④ｻﾔNo12
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(12).getItemValue())); //号機④ｻﾔNo13
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(13).getItemValue())); //号機④ｻﾔNo14
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(14).getItemValue())); //号機④ｻﾔNo15
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(15).getItemValue())); //号機④ｻﾔNo16
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(16).getItemValue())); //号機④ｻﾔNo17
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(17).getItemValue())); //号機④ｻﾔNo18
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(18).getItemValue())); //号機④ｻﾔNo19
        params.add(DBUtil.stringToStringObject(gouki4DataList.get(19).getItemValue())); //号機④ｻﾔNo20
        params.add(getCheckBoxDbValue(gouki4DataList.get(0).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ1
        params.add(getCheckBoxDbValue(gouki4DataList.get(1).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ2
        params.add(getCheckBoxDbValue(gouki4DataList.get(2).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ3
        params.add(getCheckBoxDbValue(gouki4DataList.get(3).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ4
        params.add(getCheckBoxDbValue(gouki4DataList.get(4).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ5
        params.add(getCheckBoxDbValue(gouki4DataList.get(5).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ6
        params.add(getCheckBoxDbValue(gouki4DataList.get(6).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ7
        params.add(getCheckBoxDbValue(gouki4DataList.get(7).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ8
        params.add(getCheckBoxDbValue(gouki4DataList.get(8).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ9
        params.add(getCheckBoxDbValue(gouki4DataList.get(9).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ10
        params.add(getCheckBoxDbValue(gouki4DataList.get(10).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ11
        params.add(getCheckBoxDbValue(gouki4DataList.get(11).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ12
        params.add(getCheckBoxDbValue(gouki4DataList.get(12).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ13
        params.add(getCheckBoxDbValue(gouki4DataList.get(13).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ14
        params.add(getCheckBoxDbValue(gouki4DataList.get(14).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ15
        params.add(getCheckBoxDbValue(gouki4DataList.get(15).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ16
        params.add(getCheckBoxDbValue(gouki4DataList.get(16).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ17
        params.add(getCheckBoxDbValue(gouki4DataList.get(17).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ18
        params.add(getCheckBoxDbValue(gouki4DataList.get(18).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ19
        params.add(getCheckBoxDbValue(gouki4DataList.get(19).getCheckBoxValue(), 0)); //号機④ﾁｪｯｸ20
        
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
     * 熱処理_サブ画面仮登録(tmp_sub_sr_shinkuukansou)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSubSrShinkuukansou(QueryRunner queryRunnerQcdb, 
            Connection conQcdb, BigDecimal newRev, int deleteflag, String kojyo, 
            String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {
        
        String sql = "INSERT INTO tmp_sub_sr_shinkuukansou( kojyo, lotno, edaban, kaisuu, "
                + "gouki1saya1, gouki1saya2, gouki1saya3, gouki1saya4, gouki1saya5, "
                + "gouki1saya6, gouki1saya7, gouki1saya8, gouki1saya9, gouki1saya10, "
                + "gouki1saya11, gouki1saya12, gouki1saya13, gouki1saya14, gouki1saya15, "
                + "gouki1saya16, gouki1saya17, gouki1saya18, gouki1saya19, gouki1saya20, "
                + "gouki1check1, gouki1check2, gouki1check3, gouki1check4, gouki1check5, "
                + "gouki1check6, gouki1check7, gouki1check8, gouki1check9, gouki1check10, "
                + "gouki1check11, gouki1check12, gouki1check13, gouki1check14, gouki1check15, "
                + "gouki1check16, gouki1check17, gouki1check18, gouki1check19, gouki1check20, "
                + "gouki2saya1, gouki2saya2, gouki2saya3, gouki2saya4, gouki2saya5, "
                + "gouki2saya6, gouki2saya7, gouki2saya8, gouki2saya9, gouki2saya10, "
                + "gouki2saya11, gouki2saya12, gouki2saya13, gouki2saya14, gouki2saya15, "
                + "gouki2saya16, gouki2saya17, gouki2saya18, gouki2saya19, gouki2saya20, "
                + "gouki2check1, gouki2check2, gouki2check3, gouki2check4, gouki2check5, "
                + "gouki2check6, gouki2check7, gouki2check8, gouki2check9, gouki2check10, "
                + "gouki2check11, gouki2check12, gouki2check13, gouki2check14, gouki2check15, "
                + "gouki2check16, gouki2check17, gouki2check18, gouki2check19, gouki2check20, "
                + "gouki3saya1, gouki3saya2, gouki3saya3, gouki3saya4, gouki3saya5, "
                + "gouki3saya6, gouki3saya7, gouki3saya8, gouki3saya9, gouki3saya10, "
                + "gouki3saya11, gouki3saya12, gouki3saya13, gouki3saya14, gouki3saya15, "
                + "gouki3saya16, gouki3saya17, gouki3saya18, gouki3saya19, gouki3saya20, "
                + "gouki3check1, gouki3check2, gouki3check3, gouki3check4, gouki3check5, "
                + "gouki3check6, gouki3check7, gouki3check8, gouki3check9, gouki3check10, "
                + "gouki3check11, gouki3check12, gouki3check13, gouki3check14, gouki3check15, "
                + "gouki3check16, gouki3check17, gouki3check18, gouki3check19, gouki3check20, "
                + "gouki4saya1, gouki4saya2, gouki4saya3, gouki4saya4, gouki4saya5, "
                + "gouki4saya6, gouki4saya7, gouki4saya8, gouki4saya9, gouki4saya10, "
                + "gouki4saya11, gouki4saya12, gouki4saya13, gouki4saya14, gouki4saya15, "
                + "gouki4saya16, gouki4saya17, gouki4saya18, gouki4saya19, gouki4saya20, "
                + "gouki4check1, gouki4check2, gouki4check3, gouki4check4, gouki4check5, "
                + "gouki4check6, gouki4check7, gouki4check8, gouki4check9, gouki4check10, "
                + "gouki4check11, gouki4check12, gouki4check13, gouki4check14, gouki4check15, "
                + "gouki4check16, gouki4check17, gouki4check18, gouki4check19, gouki4check20, "
                + "torokunichiji, kosinnichiji, revision, deleteflag"
                + ") SELECT kojyo, lotno, edaban, kaisuu, "
                + "gouki1saya1, gouki1saya2, gouki1saya3, gouki1saya4, gouki1saya5, "
                + "gouki1saya6, gouki1saya7, gouki1saya8, gouki1saya9, gouki1saya10, "
                + "gouki1saya11, gouki1saya12, gouki1saya13, gouki1saya14, gouki1saya15, "
                + "gouki1saya16, gouki1saya17, gouki1saya18, gouki1saya19, gouki1saya20, "
                + "gouki1check1, gouki1check2, gouki1check3, gouki1check4, gouki1check5, "
                + "gouki1check6, gouki1check7, gouki1check8, gouki1check9, gouki1check10, "
                + "gouki1check11, gouki1check12, gouki1check13, gouki1check14, gouki1check15, "
                + "gouki1check16, gouki1check17, gouki1check18, gouki1check19, gouki1check20, "
                + "gouki2saya1, gouki2saya2, gouki2saya3, gouki2saya4, gouki2saya5, "
                + "gouki2saya6, gouki2saya7, gouki2saya8, gouki2saya9, gouki2saya10, "
                + "gouki2saya11, gouki2saya12, gouki2saya13, gouki2saya14, gouki2saya15, "
                + "gouki2saya16, gouki2saya17, gouki2saya18, gouki2saya19, gouki2saya20, "
                + "gouki2check1, gouki2check2, gouki2check3, gouki2check4, gouki2check5, "
                + "gouki2check6, gouki2check7, gouki2check8, gouki2check9, gouki2check10, "
                + "gouki2check11, gouki2check12, gouki2check13, gouki2check14, gouki2check15, "
                + "gouki2check16, gouki2check17, gouki2check18, gouki2check19, gouki2check20, "
                + "gouki3saya1, gouki3saya2, gouki3saya3, gouki3saya4, gouki3saya5, "
                + "gouki3saya6, gouki3saya7, gouki3saya8, gouki3saya9, gouki3saya10, "
                + "gouki3saya11, gouki3saya12, gouki3saya13, gouki3saya14, gouki3saya15, "
                + "gouki3saya16, gouki3saya17, gouki3saya18, gouki3saya19, gouki3saya20, "
                + "gouki3check1, gouki3check2, gouki3check3, gouki3check4, gouki3check5, "
                + "gouki3check6, gouki3check7, gouki3check8, gouki3check9, gouki3check10, "
                + "gouki3check11, gouki3check12, gouki3check13, gouki3check14, gouki3check15, "
                + "gouki3check16, gouki3check17, gouki3check18, gouki3check19, gouki3check20, "
                + "gouki4saya1, gouki4saya2, gouki4saya3, gouki4saya4, gouki4saya5, "
                + "gouki4saya6, gouki4saya7, gouki4saya8, gouki4saya9, gouki4saya10, "
                + "gouki4saya11, gouki4saya12, gouki4saya13, gouki4saya14, gouki4saya15, "
                + "gouki4saya16, gouki4saya17, gouki4saya18, gouki4saya19, gouki4saya20, "
                + "gouki4check1, gouki4check2, gouki4check3, gouki4check4, gouki4check5, "
                + "gouki4check6, gouki4check7, gouki4check8, gouki4check9, gouki4check10, "
                + "gouki4check11, gouki4check12, gouki4check13, gouki4check14, gouki4check15, "
                + "gouki4check16, gouki4check17, gouki4check18, gouki4check19, gouki4check20, "
                + "?, ?, ?, ? FROM sub_sr_shinkuukansou "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ?";

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
     * 熱処理_サブ画面仮登録(sub_sr_shinkuukansou)削除処理
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
    private void deleteSubSrShinkuukansou(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            BigDecimal rev, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        
        String sql = "DELETE FROM sub_sr_shinkuukansou "
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
     * [生産]から、ﾃﾞｰﾀを取得
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
    private Map loadSeisanData(QueryRunner queryRunnerWip, int jissekino) throws SQLException {
                 
        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT ryohinsuu "
                + "FROM seisan "
                + "WHERE jissekino = ?";
                
        List<Object> params = new ArrayList<>();
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doDataCooperationSyori(ProcessData processData) {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        try {
            // (23)[tmp_sr_netsusyori_kanri]から、ﾃﾞｰﾀの取得
            List<Map<String, Object>> tmpSrGraprintKanriDataList = loadTmpNetsusyoriKanriData(queryRunnerQcdb, lotNo, null);
            if (tmpSrGraprintKanriDataList == null || tmpSrGraprintKanriDataList.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "設備ﾃﾞｰﾀ");
                if (checkItemError != null) {
                    processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                    return processData;
                }
            }
            HashMap<String, String> itemIdConvertMap = new HashMap<>();
            itemIdConvertMap.put(GXHDO101B043Const.SYURUI, "netsusyori_syurui"); // 種類
            itemIdConvertMap.put(GXHDO101B043Const.GOUKI, "netsusyori_gouki1"); // 号機
            itemIdConvertMap.put(GXHDO101B043Const.GOUKI2, "netsusyori_gouki2"); // 号機②
            itemIdConvertMap.put(GXHDO101B043Const.GOUKI3, "netsusyori_gouki3"); // 号機③
            itemIdConvertMap.put(GXHDO101B043Const.GOUKI4, "netsusyori_gouki4"); // 号機④
            itemIdConvertMap.put(GXHDO101B043Const.SETTEIONDO, "netsusyori_settei_ondo"); // 設定温度
            itemIdConvertMap.put(GXHDO101B043Const.SETTEIJIKAN, "netsusyori_settei_jikan"); // 設定時間
            itemIdConvertMap.put(GXHDO101B043Const.KAISHI_DAY, "netsusyori_kaisibi"); // 開始日
            itemIdConvertMap.put(GXHDO101B043Const.KAISHI_TIME, "netsusyori_kaisi_jikan"); // 開始時間
            itemIdConvertMap.put(GXHDO101B043Const.SAGYOSYA, "netsusyori_kaisi_tantousya"); // 開始担当者
            itemIdConvertMap.put(GXHDO101B043Const.STARTKAKUNIN, "netsusyori_kaisi_kakuninsya"); // 開始確認者
            itemIdConvertMap.put(GXHDO101B043Const.SHURYOU_DAY, "netsusyori_syuuryoubi"); // 終了日
            itemIdConvertMap.put(GXHDO101B043Const.SHURYOU_TIME, "netsusyori_syuuryou_jikan"); // 終了時間
            itemIdConvertMap.put(GXHDO101B043Const.ENDTANTOU, "netsusyori_syuuryou_tantousya"); // 終了担当者
            ErrorMessageInfo checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap);
            if (checkItemError != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                return processData;
            }
            doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap);
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
        }
        
        processData.setMethod("");
        return processData;
    }
    
    /**
     * 設備ﾃﾞｰﾀ連携チェック処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_netsusyori(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap) throws SQLException {
        ErrorMessageInfo checkItemError = null;
        // 検索条件:ﾃﾞｰﾀの種類==3 で、Ⅲ.画面表示仕様(18)を発行する。
        List<Map<String, Object>> tmpSrGraprintKanriDataList = loadTmpNetsusyoriKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui));
        if (tmpSrGraprintKanriDataList != null && !tmpSrGraprintKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(19)を発行する。
            Map<String, Object> tmpSrGraprintKanriData = tmpSrGraprintKanriDataList.get(0);
            List<Map<String, Object>> tmpSrGraprintDataList = loadTmpNetsusyoriData(queryRunnerQcdb, (Long) tmpSrGraprintKanriData.get("kanrino"));
            if (tmpSrGraprintDataList != null && !tmpSrGraprintDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                    // 終了時(ﾃﾞｰﾀ種類3)
                List<String> numberItemList = Arrays.asList(GXHDO101B043Const.SETTEIONDO, GXHDO101B043Const.SETTEIJIKAN);
                checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrGraprintDataList, itemIdConvertMap);
            }
        }
        return checkItemError;
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時、該当項目(数値表示)で取得時に、取得した値が文字列であった場合チェック処理
     *
     * @param processData 処理制御データ
     * @param numberItemList 数値項目リスト
     * @param tmpSrGraprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_netsusyori(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     */
    private ErrorMessageInfo checkDataCooperationItemData(ProcessData processData, List<String> numberItemList, List<Map<String, Object>> tmpSrGraprintDataList,
            HashMap<String, String> itemIdConvertMap) {
        for(String itemId : numberItemList){
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrGraprintItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrGraprintItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrGraprintData = tmpSrGraprintDataList.stream().filter(e -> tmpSrGraprintItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrGraprintData != null && !tmpSrGraprintData.isEmpty()) {
                    String ataiValue = StringUtil.nullToBlank(tmpSrGraprintData.get("atai"));
                    if(!StringUtil.isEmpty(ataiValue)){
                        try {
                           BigDecimal bigDecimalVal = new BigDecimal(ataiValue);
                        } catch (NumberFormatException e) {
                            // 該当項目(数値表示)で取得時に、取得した値が文字列であった場合
                            // ｴﾗｰ項目をﾘｽﾄに追加
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemData);
                            ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000087", true, true, errFxhdd01List);
                            return checkItemError;
                        }
                    }
                }
            } 
        }
        return null;
    }

    /**
     * 設備ﾃﾞｰﾀ連携処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_netsusyori(item_id)の対比表
     * @throws SQLException 例外エラー
     */
    private void doDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(18)を発行する。
        List<Map<String, Object>> tmpSrGraprintKanriDataList = loadTmpNetsusyoriKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui));
        if (tmpSrGraprintKanriDataList != null && !tmpSrGraprintKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(19)を発行する。
            Map<String, Object> tmpSrGraprintKanriData = tmpSrGraprintKanriDataList.get(0);
            List<Map<String, Object>> tmpSrGraprintDataList = loadTmpNetsusyoriData(queryRunnerQcdb, (Long) tmpSrGraprintKanriData.get("kanrino"));
            if (tmpSrGraprintDataList != null && !tmpSrGraprintDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                // 終了時(ﾃﾞｰﾀ種類3)
                List<String> setValueItemList = Arrays.asList(GXHDO101B043Const.SYURUI, GXHDO101B043Const.GOUKI, GXHDO101B043Const.GOUKI2,
                        GXHDO101B043Const.GOUKI3, GXHDO101B043Const.GOUKI4, GXHDO101B043Const.SETTEIONDO, GXHDO101B043Const.SETTEIJIKAN,
                        GXHDO101B043Const.KAISHI_DAY, GXHDO101B043Const.KAISHI_TIME, GXHDO101B043Const.SAGYOSYA, GXHDO101B043Const.STARTKAKUNIN,
                        GXHDO101B043Const.SHURYOU_DAY, GXHDO101B043Const.SHURYOU_TIME, GXHDO101B043Const.ENDTANTOU);
                setDataCooperationItemData(processData, setValueItemList, tmpSrGraprintDataList, itemIdConvertMap);
                setDataCooperationSubItemData(tmpSrGraprintDataList);
            }
        }
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする
     *
     * @param processData 処理制御データ
     * @param setValueItemList 項目リスト
     * @param tmpSrGraprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_netsusyori(item_id)の対比表
     */
    private void setDataCooperationItemData(ProcessData processData, List<String> setValueItemList, List<Map<String, Object>> tmpSrGraprintDataList,
            HashMap<String, String> itemIdConvertMap) {
        setValueItemList.forEach(itemId -> {
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrGraprintItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrGraprintItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrGraprintData = tmpSrGraprintDataList.stream().filter(e -> tmpSrGraprintItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrGraprintData != null && !tmpSrGraprintData.isEmpty()) {
                    itemData.setValue(StringUtil.nullToBlank(tmpSrGraprintData.get("atai")));
                }
            }
        });
    }
    
    /**
     * [tmp_sr_netsusyori_kanri]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpNetsusyoriKanriData(QueryRunner queryRunnerQcdb, String lotNo, String datasyurui) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotno = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        // [tmp_sr_netsusyori_kanri]から、ﾃﾞｰﾀの取得
        String sql = "SELECT kanrino, kojyo, lotno, edaban, datasyurui, jissekino, torokunichiji"
                + " FROM tmp_sr_netsusyori_kanri "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        if (!StringUtil.isEmpty(datasyurui)) {
            sql += " AND datasyurui = ? ";
        }
        sql += " order by jissekino desc";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotno);
        params.add(edaban);
        if (!StringUtil.isEmpty(datasyurui)) {
            params.add(datasyurui);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * [tmp_netsusyori]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kanrino 管理No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpNetsusyoriData(QueryRunner queryRunnerQcdb, Long kanrino) throws SQLException {
        // [tmp_netsusyori]から、ﾃﾞｰﾀの取得
        String sql = "SELECT kanrino, item_id, atai"
                + " FROM tmp_sr_netsusyori WHERE kanrino = ?";

        List<Object> params = new ArrayList<>();
        params.add(kanrino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時、ｻﾌﾞ画面の内容を上書きする。
     *
     * @param processData 処理制御データ
     * @param setValueItemList 項目リスト
     * @param tmpSrGraprintDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_netsusyori(item_id)の対比表
     */
    private void setDataCooperationSubItemData(List<Map<String, Object>> tmpSrGraprintDataList) {

        GXHDO101C018 beanGXHDO101C018 = (GXHDO101C018) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C018);
        List<GXHDO101C018Model.GoukiData> gouki1DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki1DataList();
        List<GXHDO101C018Model.GoukiData> gouki2DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki2DataList();
        List<GXHDO101C018Model.GoukiData> gouki3DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki3DataList();
        List<GXHDO101C018Model.GoukiData> gouki4DataList = beanGXHDO101C018.getGxhdO101c018Model().getGouki4DataList();        

        Map<String, Object> sayadata;
        // 号機1ｻﾔNo1
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya1".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(0).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo2
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya2".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(1).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo3
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya3".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(2).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機1ｻﾔNo4
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya4".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(3).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機1ｻﾔNo5
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya5".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(4).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo6
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya6".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(5).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo7
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya7".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(6).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔN8
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya8".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(7).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔN9
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya9".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(8).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔN10
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya10".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(9).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo11
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya11".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(10).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }        
        // 号機1ｻﾔNo12
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya12".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(11).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo13
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya13".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(12).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機1ｻﾔNo14
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya14".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(13).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機1ｻﾔNo15
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya15".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(14).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo16
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya16".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(15).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔNo17
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya17".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(16).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔN18
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya18".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(17).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔN19
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya19".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(18).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機1ｻﾔN20
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki1_saya20".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki1DataList.get(19).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo1
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya1".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(0).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo2
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya2".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(1).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo3
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya3".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(2).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機2ｻﾔNo4
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya4".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(3).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機2ｻﾔNo5
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya5".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(4).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo6
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya6".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(5).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo7
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya7".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(6).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔN8
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya8".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(7).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔN9
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya9".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(8).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔN10
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya10".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(9).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo11
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya11".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(10).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }        
        // 号機2ｻﾔNo12
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya12".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(11).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo13
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya13".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(12).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機2ｻﾔNo14
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya14".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(13).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機2ｻﾔNo15
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya15".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(14).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo16
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya16".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(15).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔNo17
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya17".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(16).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔN18
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya18".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(17).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔN19
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya19".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(18).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機2ｻﾔN20
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki2_saya20".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki2DataList.get(19).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo1
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya1".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(0).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo2
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya2".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(1).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo3
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya3".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(2).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機3ｻﾔNo4
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya4".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(3).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機3ｻﾔNo5
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya5".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(4).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo6
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya6".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(5).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo7
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya7".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(6).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔN8
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya8".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(7).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔN9
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya9".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(8).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔN10
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya10".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(9).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo11
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya11".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(10).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }        
        // 号機3ｻﾔNo12
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya12".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(11).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo13
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya13".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(12).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機3ｻﾔNo14
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya14".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(13).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機3ｻﾔNo15
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya15".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(14).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo16
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya16".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(15).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔNo17
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya17".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(16).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔN18
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya18".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(17).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔN19
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya19".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(18).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機3ｻﾔN20
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki3_saya20".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki3DataList.get(19).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo1
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya1".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(0).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo2
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya2".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(1).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo3
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya3".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(2).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機4ｻﾔNo4
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya4".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(3).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機4ｻﾔNo5
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya5".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(4).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo6
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya6".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(5).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo7
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya7".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(6).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔN8
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya8".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(7).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔN9
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya9".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(8).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔN10
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya10".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(9).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo11
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya11".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(10).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }        
        // 号機4ｻﾔNo12
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya12".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(11).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo13
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya13".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(12).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機4ｻﾔNo14
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya14".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(13).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }    
        // 号機4ｻﾔNo15
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya15".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(14).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo16
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya16".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(15).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔNo17
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya17".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(16).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔN18
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya18".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(17).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔN19
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya19".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(18).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
        // 号機4ｻﾔN20
        sayadata = tmpSrGraprintDataList.stream().filter(e -> "netsusyori_gouki4_saya20".equals(e.get("item_id"))).findFirst().orElse(null);
        if (sayadata != null) {
            gouki4DataList.get(19).setItemValue(StringUtil.nullToBlank(sayadata.get("atai")));
        }
    }
}
