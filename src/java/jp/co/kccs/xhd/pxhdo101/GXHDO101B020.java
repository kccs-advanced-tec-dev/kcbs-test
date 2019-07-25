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
import jp.co.kccs.xhd.db.model.SrBarrel1;
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
 * システム名  品質DB(コンデンサ)<br>
 * <br>
 * 変更日      2019/07/15<br>
 * 計画書No    K1803-DS001<br>
 * 変更者      KCSS K.Jo<br>
 * 変更理由    新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B020(研磨・ﾊﾞﾚﾙ)ロジック
 *
 * @author KCSS K.Jo
 * @since  2019/07/15
 */
public class GXHDO101B020 implements IFormLogic {

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
                    GXHDO101B020Const.BTN_START_DATETIME_TOP,
                    GXHDO101B020Const.BTN_END_DATETIME_TOP,
                    GXHDO101B020Const.BTN_START_DATETIME_BOTTOM,
                    GXHDO101B020Const.BTN_END_DATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B020Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B020Const.BTN_INSERT_TOP,
                    GXHDO101B020Const.BTN_DELETE_TOP,
                    GXHDO101B020Const.BTN_UPDATE_TOP,
                    GXHDO101B020Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B020Const.BTN_INSERT_BOTTOM,
                    GXHDO101B020Const.BTN_DELETE_BOTTOM,
                    GXHDO101B020Const.BTN_UPDATE_BOTTOM));

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
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ﾊﾞﾚﾙ_仮登録登録処理
                insertTmpSrBarrel1(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

            } else {

                // ﾊﾞﾚﾙ_仮登録更新処理
                updateTmpSrBarrel1(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B020Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B020Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B020Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B020Const.SHURYOU_TIME); //終了時刻
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
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrBarrel1 tmpSrBarrel1 = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrBarrel1> srBarrel1List = getSrBarrel1Data(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, paramJissekino);
                if (!srBarrel1List.isEmpty()) {
                    tmpSrBarrel1 = srBarrel1List.get(0);
                }
                
                deleteTmpSrBarrel1(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);
            }

            // ﾊﾞﾚﾙ_登録処理
            insertSrBarrel1(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrBarrel1);

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
        processData.setUserAuthParam(GXHDO101B020Const.USER_AUTH_UPDATE_PARAM);

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

            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);

            // ﾊﾞﾚﾙ_更新処理
            updateSrBarrel1(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B020Const.USER_AUTH_DELETE_PARAM);

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
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime);

            // ﾊﾞﾚﾙ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, paramJissekino);
            insertDeleteDataTmpSrBarrel1(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, paramJissekino, systemTime);

            // ﾊﾞﾚﾙ_削除処理
            deleteSrBarrel1(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, paramJissekino);

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
                        GXHDO101B020Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B020Const.BTN_DELETE_BOTTOM,
                        GXHDO101B020Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B020Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B020Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B020Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B020Const.BTN_DELETE_TOP,
                        GXHDO101B020Const.BTN_UPDATE_TOP,
                        GXHDO101B020Const.BTN_START_DATETIME_TOP,
                        GXHDO101B020Const.BTN_END_DATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B020Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B020Const.BTN_INSERT_BOTTOM,
                        GXHDO101B020Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B020Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B020Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B020Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO101B020Const.BTN_INSERT_BOTTOM,
                        GXHDO101B020Const.BTN_START_DATETIME_BOTTOM,
                        GXHDO101B020Const.BTN_END_DATETIME_BOTTOM,
                        GXHDO101B020Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B020Const.BTN_EDABAN_COPY_TOP,
                        GXHDO101B020Const.BTN_INSERT_TOP,
                        GXHDO101B020Const.BTN_START_DATETIME_TOP,
                        GXHDO101B020Const.BTN_END_DATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B020Const.BTN_DELETE_BOTTOM,
                        GXHDO101B020Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B020Const.BTN_DELETE_TOP,
                        GXHDO101B020Const.BTN_UPDATE_TOP));

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
            case GXHDO101B020Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B020Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B020Const.BTN_INSERT_TOP:
            case GXHDO101B020Const.BTN_INSERT_BOTTOM:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B020Const.BTN_EDABAN_COPY_TOP:
            case GXHDO101B020Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B020Const.BTN_UPDATE_TOP:
            case GXHDO101B020Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B020Const.BTN_DELETE_TOP:
            case GXHDO101B020Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B020Const.BTN_START_DATETIME_TOP:
            case GXHDO101B020Const.BTN_START_DATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B020Const.BTN_END_DATETIME_TOP:
            case GXHDO101B020Const.BTN_END_DATETIME_BOTTOM:
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
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

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
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {
        
        // ロットNo
        this.setItemData(processData, GXHDO101B020Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B020Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B020Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B020Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B020Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B020Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B020Const.OWNER, ownercode + ":" + owner);
        }

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

        List<SrBarrel1> srBarrel1DataList = new ArrayList<>();
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

            // ﾊﾞﾚﾙデータ取得
            srBarrel1DataList = getSrBarrel1Data(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srBarrel1DataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srBarrel1DataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srBarrel1DataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srBarrel1Data ﾊﾞﾚﾙデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrBarrel1 srBarrel1Data) {

        // 受入個数
        this.setItemData(processData, GXHDO101B020Const.UKEIREKOSUU, getSrBarrel1ItemData(GXHDO101B020Const.UKEIREKOSUU, srBarrel1Data));
        // 研磨方式
        this.setItemData(processData, GXHDO101B020Const.KENMA, getSrBarrel1ItemData(GXHDO101B020Const.KENMA, srBarrel1Data));
        // 研磨号機
        this.setItemData(processData, GXHDO101B020Const.BGOKI, getSrBarrel1ItemData(GXHDO101B020Const.BGOKI, srBarrel1Data));
        // ﾎﾟｯﾄ数
        this.setItemData(processData, GXHDO101B020Const.POTSUU, getSrBarrel1ItemData(GXHDO101B020Const.POTSUU, srBarrel1Data));
        // 研磨時間
        this.setItemData(processData, GXHDO101B020Const.BJIKAN, getSrBarrel1ItemData(GXHDO101B020Const.BJIKAN, srBarrel1Data));
        // 研磨機回転数
        this.setItemData(processData, GXHDO101B020Const.BJYOKENSYUSOKUDO, getSrBarrel1ItemData(GXHDO101B020Const.BJYOKENSYUSOKUDO, srBarrel1Data));
        // 研磨材量
        this.setItemData(processData, GXHDO101B020Const.KENMAZAIRYO, getSrBarrel1ItemData(GXHDO101B020Const.KENMAZAIRYO, srBarrel1Data));
        // 研磨材種類
        this.setItemData(processData, GXHDO101B020Const.KENMAZAISYURUI, getSrBarrel1ItemData(GXHDO101B020Const.KENMAZAISYURUI, srBarrel1Data));
        // 玉石種類
        this.setItemData(processData, GXHDO101B020Const.TAMAISHISYURUI, getSrBarrel1ItemData(GXHDO101B020Const.TAMAISHISYURUI, srBarrel1Data));
        // 玉石量
        this.setItemData(processData, GXHDO101B020Const.TAMAISHIRYOU, getSrBarrel1ItemData(GXHDO101B020Const.TAMAISHIRYOU, srBarrel1Data));
        // 外観確認
        this.setItemData(processData, GXHDO101B020Const.GAIKANCHECK, getSrBarrel1ItemData(GXHDO101B020Const.GAIKANCHECK, srBarrel1Data));
        // 開始日
        this.setItemData(processData, GXHDO101B020Const.KAISHI_DAY, getSrBarrel1ItemData(GXHDO101B020Const.KAISHI_DAY, srBarrel1Data));
        // 開始時刻
        this.setItemData(processData, GXHDO101B020Const.KAISHI_TIME, getSrBarrel1ItemData(GXHDO101B020Const.KAISHI_TIME, srBarrel1Data));
        // 開始担当者
        this.setItemData(processData, GXHDO101B020Const.KAISHI_TANTOUSYA, getSrBarrel1ItemData(GXHDO101B020Const.KAISHI_TANTOUSYA, srBarrel1Data));
        // 開始確認者
        this.setItemData(processData, GXHDO101B020Const.KAISHI_KAKUNINSYA, getSrBarrel1ItemData(GXHDO101B020Const.KAISHI_KAKUNINSYA, srBarrel1Data));
        // 終了日
        this.setItemData(processData, GXHDO101B020Const.SHURYOU_DAY, getSrBarrel1ItemData(GXHDO101B020Const.SHURYOU_DAY, srBarrel1Data));
        // 終了時刻
        this.setItemData(processData, GXHDO101B020Const.SHURYOU_TIME, getSrBarrel1ItemData(GXHDO101B020Const.SHURYOU_TIME, srBarrel1Data));
        // 終了担当者
        this.setItemData(processData, GXHDO101B020Const.SHURYOU_TANTOUSYA, getSrBarrel1ItemData(GXHDO101B020Const.SHURYOU_TANTOUSYA, srBarrel1Data));
        // 備考1
        this.setItemData(processData, GXHDO101B020Const.BIKO1, getSrBarrel1ItemData(GXHDO101B020Const.BIKO1, srBarrel1Data));
        // 備考2
        this.setItemData(processData, GXHDO101B020Const.BIKO2, getSrBarrel1ItemData(GXHDO101B020Const.BIKO2, srBarrel1Data));
 
    }

    /**
     * バレルの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return バレル登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrBarrel1> getSrBarrel1Data(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrBarrel1(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrBarrel1(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
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
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, lotkubuncode, ownercode "
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
     * [バレル]から、ﾃﾞｰﾀを取得
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
    private List<SrBarrel1> loadSrBarrel1(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT kojyo ,lotno ,edaban ,jissekino ,kcpno ,bkaisinichiji ,bsyuryonichiji ,bjyokensetteimode ,bjyokensyusokudo ,"
                + " bgoki ,bjikan ,potsuu ,chiphahenkakunin ,potkakunin ,btantosya ,ptantosya ,bpotno1 ,kankaisinichiji ,"
                + " kansyuryonichiji ,kantantosya ,mediasenbetu ,bpotno2 ,keinichiji ,ukeirekosuu ,tanijyuryo ,ryohinkosuu ,"
                + " furyosuu ,budomari ,keitantosya ,biko1 ,biko2 ,torokunichiji ,kosinNichiji ,kenma ,kenmazairyo ,"
                + " kenmazaisyurui ,tamaishisyurui ,tamaishiryou ,gaikancheck ,StartKakuninsyacode ,EndTantosyacode ,revision ,'0' AS deleteflag "
                + "FROM sr_barrel1 "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND JISSEKINO = ? ";
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
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("bkaisinichiji", "bkaisinichiji"); //ﾊﾞﾚﾙ開始日時
        mapping.put("bsyuryonichiji", "bsyuryonichiji"); //ﾊﾞﾚﾙ終了日時
        mapping.put("bjyokensetteimode", "bjyokensetteimode"); //ﾊﾞﾚﾙ条件設定ﾓｰﾄﾞ
        mapping.put("bjyokensyusokudo", "bjyokensyusokudo"); //ﾊﾞﾚﾙ条件周速度
        mapping.put("bgoki", "bgoki"); //ﾊﾞﾚﾙ号機
        mapping.put("bjikan", "bjikan"); //ﾊﾞﾚﾙ時間
        mapping.put("potsuu", "potsuu"); //ﾎﾟｯﾄ数
        mapping.put("chiphahenkakunin", "chiphahenkakunin"); //ﾁｯﾌﾟ破片確認
        mapping.put("potkakunin", "potkakunin"); //ﾎﾟｯﾄ内確認
        mapping.put("btantosya", "btantosya"); //ﾊﾞﾚﾙ担当者
        mapping.put("ptantosya", "ptantosya"); //ﾎﾟｯﾄ取り出し担当者
        mapping.put("bpotno1", "bpotno1"); //ﾊﾞﾚﾙﾎﾟｯﾄNO1
        mapping.put("kankaisinichiji", "kankaisinichiji"); //乾燥開始日時
        mapping.put("kansyuryonichiji", "kansyuryonichiji"); //乾燥終了日時
        mapping.put("kantantosya", "kantantosya"); //乾燥担当者
        mapping.put("mediasenbetu", "mediasenbetu"); //ﾒﾃﾞｨｱ選別
        mapping.put("bpotno2", "bpotno2"); //ﾊﾞﾚﾙﾎﾟｯﾄNO2
        mapping.put("keinichiji", "keinichiji"); //計数日時
        mapping.put("ukeirekosuu", "ukeirekosuu"); //受入個数
        mapping.put("tanijyuryo", "tanijyuryo"); //単位重量
        mapping.put("ryohinkosuu", "ryohinkosuu"); //良品個数
        mapping.put("furyosuu", "furyosuu"); //不良数
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("keitantosya", "keitantosya"); //計数担当者
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinNichiji", "kosinnichiji"); //更新日時
        mapping.put("kenma", "kenma"); //研磨方式
        mapping.put("kenmazairyo", "kenmazairyo"); //研磨材量
        mapping.put("kenmazaisyurui", "kenmazaisyurui"); //研磨材種類
        mapping.put("tamaishisyurui", "tamaishisyurui"); //玉石種類
        mapping.put("tamaishiryou", "tamaishiryou"); //玉石量
        mapping.put("gaikancheck", "gaikancheck"); //外観確認
        mapping.put("StartKakuninsyacode", "startkakuninsyacode"); //開始確認者
        mapping.put("EndTantosyacode", "endtantosyacode"); //終了担当者
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrBarrel1>> beanHandler = new BeanListHandler<>(SrBarrel1.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [バレル_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrBarrel1> loadTmpSrBarrel1(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        
        String sql = "SELECT kojyo ,lotno ,edaban ,jissekino ,kcpno ,bkaisinichiji ,bsyuryonichiji ,bjyokensetteimode ,bjyokensyusokudo ,"
                + " bgoki ,bjikan ,potsuu ,chiphahenkakunin ,potkakunin ,btantosya ,ptantosya ,bpotno1 ,kankaisinichiji ,"
                + " kansyuryonichiji ,kantantosya ,mediasenbetu ,bpotno2 ,keinichiji ,ukeirekosuu ,tanijyuryo ,ryohinkosuu ,"
                + " furyosuu ,budomari ,keitantosya ,biko1 ,biko2 ,torokunichiji ,kosinNichiji ,kenma ,kenmazairyo ,"
                + " kenmazaisyurui ,tamaishisyurui ,tamaishiryou ,gaikancheck ,StartKakuninsyacode ,EndTantosyacode ,revision ,deleteflag "
                + "FROM tmp_sr_barrel1 "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND jissekino = ? AND deleteflag = ? ";
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
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("bkaisinichiji", "bkaisinichiji"); //ﾊﾞﾚﾙ開始日時
        mapping.put("bsyuryonichiji", "bsyuryonichiji"); //ﾊﾞﾚﾙ終了日時
        mapping.put("bjyokensetteimode", "bjyokensetteimode"); //ﾊﾞﾚﾙ条件設定ﾓｰﾄﾞ
        mapping.put("bjyokensyusokudo", "bjyokensyusokudo"); //ﾊﾞﾚﾙ条件周速度
        mapping.put("bgoki", "bgoki"); //ﾊﾞﾚﾙ号機
        mapping.put("bjikan", "bjikan"); //ﾊﾞﾚﾙ時間
        mapping.put("potsuu", "potsuu"); //ﾎﾟｯﾄ数
        mapping.put("chiphahenkakunin", "chiphahenkakunin"); //ﾁｯﾌﾟ破片確認
        mapping.put("potkakunin", "potkakunin"); //ﾎﾟｯﾄ内確認
        mapping.put("btantosya", "btantosya"); //ﾊﾞﾚﾙ担当者
        mapping.put("ptantosya", "ptantosya"); //ﾎﾟｯﾄ取り出し担当者
        mapping.put("bpotno1", "bpotno1"); //ﾊﾞﾚﾙﾎﾟｯﾄNO1
        mapping.put("kankaisinichiji", "kankaisinichiji"); //乾燥開始日時
        mapping.put("kansyuryonichiji", "kansyuryonichiji"); //乾燥終了日時
        mapping.put("kantantosya", "kantantosya"); //乾燥担当者
        mapping.put("mediasenbetu", "mediasenbetu"); //ﾒﾃﾞｨｱ選別
        mapping.put("bpotno2", "bpotno2"); //ﾊﾞﾚﾙﾎﾟｯﾄNO2
        mapping.put("keinichiji", "keinichiji"); //計数日時
        mapping.put("ukeirekosuu", "ukeirekosuu"); //受入個数
        mapping.put("tanijyuryo", "tanijyuryo"); //単位重量
        mapping.put("ryohinkosuu", "ryohinkosuu"); //良品個数
        mapping.put("furyosuu", "furyosuu"); //不良数
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("keitantosya", "keitantosya"); //計数担当者
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinNichiji", "kosinnichiji"); //更新日時
        mapping.put("kenma", "kenma"); //研磨方式
        mapping.put("kenmazairyo", "kenmazairyo"); //研磨材量
        mapping.put("kenmazaisyurui", "kenmazaisyurui"); //研磨材種類
        mapping.put("tamaishisyurui", "tamaishisyurui"); //玉石種類
        mapping.put("tamaishiryou", "tamaishiryou"); //玉石量
        mapping.put("gaikancheck", "gaikancheck"); //外観確認
        mapping.put("StartKakuninsyacode", "startkakuninsyacode"); //開始確認者
        mapping.put("EndTantosyacode", "endtantosyacode"); //終了担当者
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrBarrel1>> beanHandler = new BeanListHandler<>(SrBarrel1.class, rowProcessor);

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

            // ﾊﾞﾚﾙデータ取得
            List<SrBarrel1> srBarrel1DataList = getSrBarrel1Data(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, paramJissekino);
            if (srBarrel1DataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srBarrel1DataList.get(0));

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
     * @param srBarrel1Data ﾊﾞﾚﾙ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrBarrel1 srBarrel1Data) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srBarrel1Data != null) {
            // 元データが存在する場合元データより取得
            return getSrBarrel1ItemData(itemId, srBarrel1Data);
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
     * ﾊﾞﾚﾙ_仮登録(tmp_sr_barrel1)登録処理
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
    private void insertTmpSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_barrel1 ("
                + "kojyo,lotno,edaban,jissekino,kcpno,bkaisinichiji,bsyuryonichiji,bjyokensetteimode,bjyokensyusokudo,bgoki,bjikan,potsuu"
                + ",chiphahenkakunin,potkakunin,btantosya,ptantosya,bpotno1,kankaisinichiji,kansyuryonichiji,kantantosya,mediasenbetu,bpotno2"
                + ",keinichiji,ukeirekosuu,tanijyuryo,ryohinkosuu,furyosuu,budomari,keitantosya,biko1,biko2,torokunichiji,kosinNichiji,kenma"
                + ",kenmazairyo,kenmazaisyurui,tamaishisyurui,tamaishiryou,gaikancheck,StartKakuninsyacode,EndTantosyacode,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrBarrel1(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞﾚﾙ_仮登録(tmp_sr_barrel1)更新処理
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
    private void updateTmpSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_barrel1 SET "
                + " kcpno = ?,bkaisinichiji = ?,bsyuryonichiji = ?,bjyokensetteimode = ?,bjyokensyusokudo = ?, "
                + " bgoki = ?,bjikan = ?,potsuu = ?,chiphahenkakunin = ?,potkakunin = ?,btantosya = ?,ptantosya = ?,bpotno1 = ?,kankaisinichiji = ?,"
                + " kansyuryonichiji = ?,kantantosya = ?,mediasenbetu = ?,bpotno2 = ?,keinichiji = ?,ukeirekosuu = ?,tanijyuryo = ?,ryohinkosuu = ?,"
                + " furyosuu = ?,budomari = ?,keitantosya = ?,biko1 = ?,biko2 = ?,kosinNichiji = ?,kenma = ?,kenmazairyo = ?,"
                + " kenmazaisyurui = ?,tamaishisyurui = ?,tamaishiryou = ?,gaikancheck = ?,StartKakuninsyacode = ?,EndTantosyacode = ?,"
                + "revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrBarrel1> srSrBarrel1List = getSrBarrel1Data(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrBarrel1 srBarrel1 = null;
        if (!srSrBarrel1List.isEmpty()) {
            srBarrel1 = srSrBarrel1List.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrBarrel1(false, newRev, 0, "", "", "", systemTime, itemList, srBarrel1, jissekino);

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
     * ﾊﾞﾚﾙ_仮登録(tmp_sr_barrel1)削除処理
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
    private void deleteTmpSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_barrel1 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ?";

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
     * ﾊﾞﾚﾙ_仮登録(tmp_sr_barrel1)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srBarrel1Data ﾊﾞﾚﾙデータ
     * @param jissekino 実績No
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrBarrel1(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrBarrel1 srBarrel1Data, int jissekino) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KCPNO, srBarrel1Data))); //KCPNO        

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KAISHI_DAY, srBarrel1Data),
            getItemData(itemList, GXHDO101B020Const.KAISHI_TIME, srBarrel1Data))); // ﾊﾞﾚﾙ開始日時

        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.SHURYOU_DAY, srBarrel1Data),
            getItemData(itemList, GXHDO101B020Const.SHURYOU_TIME, srBarrel1Data))); // ﾊﾞﾚﾙ終了日時
        
        params.add(null); // ﾊﾞﾚﾙ条件設定ﾓｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.BJYOKENSYUSOKUDO, srBarrel1Data))); //ﾊﾞﾚﾙ条件周速度      
        // ﾊﾞﾚﾙ号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.BGOKI, srBarrel1Data)));
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.BJIKAN, srBarrel1Data))); // ﾊﾞﾚﾙ時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.POTSUU, srBarrel1Data))); // ﾎﾟｯﾄ数
        params.add(null); // ﾁｯﾌﾟ破片確認
        params.add(null); // ﾎﾟｯﾄ内確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KAISHI_TANTOUSYA, srBarrel1Data))); // ﾊﾞﾚﾙ担当者
        params.add(null); // ﾎﾟｯﾄ取り出し担当者
        params.add(null); // ﾊﾞﾚﾙﾎﾟｯﾄNO1
        params.add(null); // 乾燥開始日時
        params.add(null); // 乾燥終了日時
        params.add(null); // 乾燥担当者
        params.add(null); // ﾒﾃﾞｨｱ選別
        params.add(null); // ﾊﾞﾚﾙﾎﾟｯﾄNO2
        params.add(null); // 計数日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.UKEIREKOSUU, srBarrel1Data))); // 受入個数
        params.add(null); // 単位重量
        params.add(null); // 良品個数
        params.add(null); // 不良数
        params.add(null); // 歩留まり
        params.add(null); // 計数担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.BIKO1, srBarrel1Data))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.BIKO2, srBarrel1Data))); // 備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KENMA, srBarrel1Data))); // 研磨方式
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KENMAZAIRYO, srBarrel1Data))); // 研磨材量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KENMAZAISYURUI, srBarrel1Data))); // 研磨材種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.TAMAISHISYURUI, srBarrel1Data))); // 玉石種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.TAMAISHIRYOU, srBarrel1Data))); // 玉石量

        //外観確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B020Const.GAIKANCHECK, srBarrel1Data))) {
            case "NG":
                params.add(0);
                break;
            case "OK":
                params.add(1);
                break;
            default:
                params.add(null);
                break;
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.KAISHI_KAKUNINSYA, srBarrel1Data))); // 開始確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B020Const.SHURYOU_TANTOUSYA, srBarrel1Data))); // 終了担当者

        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        
        return params;
    }

    /**
     * ﾊﾞﾚﾙ(sr_barrel1)登録処理
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
     * @param tmpSrBarrel1 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino,Timestamp systemTime, List<FXHDD01> itemList, SrBarrel1 tmpSrBarrel1) throws SQLException {

        String sql = "INSERT INTO sr_barrel1 ("
                + "kojyo ,lotno ,edaban ,jissekino ,kcpno ,bkaisinichiji ,bsyuryonichiji ,bjyokensetteimode ,bjyokensyusokudo ,"
                + " bgoki ,bjikan ,potsuu ,chiphahenkakunin ,potkakunin ,btantosya ,ptantosya ,bpotno1 ,kankaisinichiji ,"
                + " kansyuryonichiji ,kantantosya ,mediasenbetu ,bpotno2 ,keinichiji ,ukeirekosuu ,tanijyuryo ,ryohinkosuu ,"
                + " furyosuu ,budomari ,keitantosya ,biko1 ,biko2 ,torokunichiji ,kosinNichiji ,kenma ,kenmazairyo ,"
                + " kenmazaisyurui ,tamaishisyurui ,tamaishiryou ,gaikancheck ,StartKakuninsyacode ,EndTantosyacode ,revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrBarrel1(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrBarrel1);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞﾚﾙ(sr_barrel1)更新処理
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
    private void updateSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_barrel1 SET "
                + " kcpno = ?,bkaisinichiji = ?,bsyuryonichiji = ?,bjyokensetteimode = ?,bjyokensyusokudo = ?,"
                + " bgoki = ?,bjikan = ?,potsuu = ?,chiphahenkakunin = ?,potkakunin = ?,btantosya = ?,ptantosya = ?,bpotno1 = ?,kankaisinichiji = ?,"
                + " kansyuryonichiji = ?,kantantosya = ?,mediasenbetu = ?,bpotno2 = ?,keinichiji = ?,ukeirekosuu = ?,tanijyuryo = ?,ryohinkosuu = ?,"
                + " furyosuu = ?,budomari = ?,keitantosya = ?,biko1 = ?,biko2 = ?,kosinNichiji = ?,kenma = ?,kenmazairyo = ?,"
                + " kenmazaisyurui = ?,tamaishisyurui = ?,tamaishiryou = ?,gaikancheck = ?,StartKakuninsyacode = ?,EndTantosyacode = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrBarrel1> srBarrel1List = getSrBarrel1Data(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrBarrel1 srBarrel1 = null;
        if (!srBarrel1List.isEmpty()) {
            srBarrel1 = srBarrel1List.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrBarrel1(false, newRev, "", "", "", jissekino, systemTime, itemList, srBarrel1);

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
     * ﾊﾞﾚﾙ(sr_barrel1)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srBarrel1Data ﾊﾞﾚﾙデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrBarrel1(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrBarrel1 srBarrel1Data) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
            params.add(jissekino); // 実績No
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.KCPNO, srBarrel1Data))); // KCPNO
        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B020Const.KAISHI_DAY, srBarrel1Data),
                getItemData(itemList, GXHDO101B020Const.KAISHI_TIME, srBarrel1Data))); // ﾊﾞﾚﾙ開始日時
        
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B020Const.SHURYOU_DAY, srBarrel1Data),
                getItemData(itemList, GXHDO101B020Const.SHURYOU_TIME, srBarrel1Data))); // ﾊﾞﾚﾙ終了日時

        params.add(null); // ﾊﾞﾚﾙ条件設定ﾓｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B020Const.BJYOKENSYUSOKUDO, srBarrel1Data))); //ﾊﾞﾚﾙ条件周速度         
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.BGOKI, srBarrel1Data))); // ﾊﾞﾚﾙ号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.BJIKAN, srBarrel1Data))); // ﾊﾞﾚﾙ時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B020Const.POTSUU, srBarrel1Data))); // ﾎﾟｯﾄ数
        params.add(null); // ﾁｯﾌﾟ破片確認
        params.add(null); // ﾎﾟｯﾄ内確認
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.KAISHI_TANTOUSYA, srBarrel1Data))); // ﾊﾞﾚﾙ担当者
        params.add(null); // ﾎﾟｯﾄ取り出し担当者
        params.add(null); // ﾊﾞﾚﾙﾎﾟｯﾄNO1
        params.add(null); // 乾燥開始日時
        params.add(null); // 乾燥終了日時
        params.add(null); // 乾燥担当者
        params.add(null); // ﾒﾃﾞｨｱ選別
        params.add(null); // ﾊﾞﾚﾙﾎﾟｯﾄNO2
        params.add(null); // 計数日時
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B020Const.UKEIREKOSUU, srBarrel1Data))); // 受入個数
        params.add(null); // 単位重量
        params.add(null); // 良品個数
        params.add(null); // 不良数
        params.add(null); // 歩留まり
        params.add(null); // 計数担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.BIKO1, srBarrel1Data))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.BIKO2, srBarrel1Data))); // 備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時

        } else {
            params.add(systemTime); //更新日時
        }
        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.KENMA, srBarrel1Data))); // 研磨方式
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B020Const.KENMAZAIRYO, srBarrel1Data))); // 研磨材量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.KENMAZAISYURUI, srBarrel1Data))); // 研磨材種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.TAMAISHISYURUI, srBarrel1Data))); // 玉石種類
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B020Const.TAMAISHIRYOU, srBarrel1Data))); // 玉石量
        // 外観確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B020Const.GAIKANCHECK, srBarrel1Data))) {
            case "NG":
                params.add(0);
                break;
            case "OK":
                params.add(1);
                break;
            default:
                params.add(9);
                break;
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.KAISHI_KAKUNINSYA, srBarrel1Data))); // 開始確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B020Const.SHURYOU_TANTOUSYA, srBarrel1Data))); // 終了担当者

        params.add(newRev); //revision
        
        return params;
    }

    /**
     * ﾊﾞﾚﾙ(sr_barrel1)削除処理
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
    private void deleteSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_barrel1 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND revision = ?";

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
     * [ﾊﾞﾚﾙ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_barrel1 "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND JISSEKINO = ? ";
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
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B020Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B020Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B020Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B020Const.SHURYOU_TIME);
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
     * @param srBarrel1Data ﾊﾞﾚﾙデータ
     * @return DB値
     */
    private String getSrBarrel1ItemData(String itemId, SrBarrel1 srBarrel1Data) {
        switch (itemId) {
            // 受入個数
            case GXHDO101B020Const.UKEIREKOSUU:
                return StringUtil.nullToBlank(srBarrel1Data.getUkeirekosuu());
            // 研磨方式
            case GXHDO101B020Const.KENMA:
                return StringUtil.nullToBlank(srBarrel1Data.getKenma());
            // 研磨号機
            case GXHDO101B020Const.BGOKI:
                return StringUtil.nullToBlank(srBarrel1Data.getBgoki());
            // ﾎﾟｯﾄ数
            case GXHDO101B020Const.POTSUU:
                return StringUtil.nullToBlank(srBarrel1Data.getPotsuu());
            // 研磨時間
            case GXHDO101B020Const.BJIKAN:
                return StringUtil.nullToBlank(srBarrel1Data.getBjikan());
            // 研磨機回転数
            case GXHDO101B020Const.BJYOKENSYUSOKUDO:
                return StringUtil.nullToBlank(srBarrel1Data.getBjyokensyusokudo());
            // 研磨材量
            case GXHDO101B020Const.KENMAZAIRYO:
                return StringUtil.nullToBlank(srBarrel1Data.getKenmazairyo());
            // 研磨材種類
            case GXHDO101B020Const.KENMAZAISYURUI:
                return StringUtil.nullToBlank(srBarrel1Data.getKenmazaisyurui());
            // 玉石種類
            case GXHDO101B020Const.TAMAISHISYURUI:
                return StringUtil.nullToBlank(srBarrel1Data.getTamaishisyurui());
            // 玉石量
            case GXHDO101B020Const.TAMAISHIRYOU:
                return StringUtil.nullToBlank(srBarrel1Data.getTamaishiryou());
            // 外観確認
            case GXHDO101B020Const.GAIKANCHECK:
                switch (StringUtil.nullToBlank(srBarrel1Data.getGaikancheck())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 開始日
            case GXHDO101B020Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srBarrel1Data.getBkaisinichiji(), "yyMMdd");
            // 開始時刻
            case GXHDO101B020Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srBarrel1Data.getBkaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B020Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srBarrel1Data.getBtantosya());
            // 開始確認者
            case GXHDO101B020Const.KAISHI_KAKUNINSYA:
                return StringUtil.nullToBlank(srBarrel1Data.getStartkakuninsyacode());
            // 終了日
            case GXHDO101B020Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srBarrel1Data.getBsyuryonichiji(), "yyMMdd");
            // 終了時刻
            case GXHDO101B020Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srBarrel1Data.getBsyuryonichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B020Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srBarrel1Data.getEndtantosyacode());
            // 備考1
            case GXHDO101B020Const.BIKO1:
                return StringUtil.nullToBlank(srBarrel1Data.getBiko1());
            // 備考2
            case GXHDO101B020Const.BIKO2:
                return StringUtil.nullToBlank(srBarrel1Data.getBiko2());
            default:
                return null;            
        }
    }

    /**
     * ﾊﾞﾚﾙ_仮登録(tmp_sr_barrel1)登録処理(削除時)
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
    private void insertDeleteDataTmpSrBarrel1(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_barrel1 ("
                +  " kojyo ,lotno ,edaban ,jissekino ,kcpno ,bkaisinichiji ,bsyuryonichiji ,bjyokensetteimode ,bjyokensyusokudo , "
                +  " bgoki ,bjikan ,potsuu ,chiphahenkakunin ,potkakunin ,btantosya ,ptantosya ,bpotno1 ,kankaisinichiji ,"
                +  " kansyuryonichiji ,kantantosya ,mediasenbetu ,bpotno2 ,keinichiji ,ukeirekosuu ,tanijyuryo ,ryohinkosuu ,"
                +  " furyosuu ,budomari ,keitantosya ,biko1 ,biko2 ,torokunichiji ,kosinNichiji ,kenma ,kenmazairyo ,"
                +  " kenmazaisyurui ,tamaishisyurui ,tamaishiryou ,gaikancheck ,StartKakuninsyacode ,EndTantosyacode ,revision ,deleteflag"
                + ") SELECT "
                +  " kojyo ,lotno ,edaban ,jissekino ,kcpno ,bkaisinichiji ,bsyuryonichiji ,bjyokensetteimode ,bjyokensyusokudo , "
                +  " bgoki ,bjikan ,potsuu ,chiphahenkakunin ,potkakunin ,btantosya ,ptantosya ,bpotno1 ,kankaisinichiji ,"
                +  " kansyuryonichiji ,kantantosya ,mediasenbetu ,bpotno2 ,keinichiji ,ukeirekosuu ,tanijyuryo ,ryohinkosuu ,"
                +  " furyosuu ,budomari ,keitantosya ,biko1 ,biko2 ,? ,? ,kenma ,kenmazairyo ,"
                +  " kenmazaisyurui ,tamaishisyurui ,tamaishiryou ,gaikancheck ,StartKakuninsyacode ,EndTantosyacode ,? ,? "
                + " FROM sr_barrel1 "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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
}
