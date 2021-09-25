/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.Seisan;
import jp.co.kccs.xhd.db.model.SrMaebarrel;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名  品質DB(コンデンサ)<br>
 * <br>
 * 変更日      2021/09/22<br>
 * 計画書No    MB2108-DK001<br>
 * 変更者      SRC T.Ushiyama<br>
 * 変更理由    新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B052(焼成・焼成前ﾊﾞﾚﾙ)ロジック
 *
 * @author SRC T.Ushiyama
 * @since  2021/09/22
 */
public class GXHDO101B052 implements IFormLogic {

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
                    GXHDO101B052Const.BTN_COPY_EDABAN_TOP,
                    GXHDO101B052Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B052Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_TOP,
                    GXHDO101B052Const.BTN_COPY_EDABAN_BOTTOM,
                    GXHDO101B052Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B052Const.BTN_ENDDATETIME_BOTTOM,
                    GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B052Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B052Const.BTN_INSERT_TOP,
                    GXHDO101B052Const.BTN_DELETE_TOP,
                    GXHDO101B052Const.BTN_UPDATE_TOP,
                    GXHDO101B052Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B052Const.BTN_INSERT_BOTTOM,
                    GXHDO101B052Const.BTN_DELETE_BOTTOM,
                    GXHDO101B052Const.BTN_UPDATE_BOTTOM));

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

                // 焼成前ﾊﾞﾚﾙ_仮登録登録処理
                insertTmpSrMaebarrel(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            } else {

                // 焼成前ﾊﾞﾚﾙ_仮登録更新処理
                updateTmpSrMaebarrel(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

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
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B052Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B052Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B052Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B052Const.SHURYOU_TIME); //終了時刻
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
            SrMaebarrel tmpSrMaebarrel = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrMaebarrel> srBarrel1List = getSrMaebarrelData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srBarrel1List.isEmpty()) {
                    tmpSrMaebarrel = srBarrel1List.get(0);
                }
                
                deleteTmpSrMaebarrel(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 焼成前ﾊﾞﾚﾙ_登録処理
            insertSrMaebarrel(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrMaebarrel);

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
        processData.setUserAuthParam(GXHDO101B052Const.USER_AUTH_UPDATE_PARAM);

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

            // ﾊﾞﾚﾙ_更新処理
            updateSrMaebarrel(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B052Const.USER_AUTH_DELETE_PARAM);

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

            // 焼成前ﾊﾞﾚﾙ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrMaebarrel(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 焼成前ﾊﾞﾚﾙ_削除処理
            deleteSrMaebarrel(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO101B052Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B052Const.BTN_DELETE_BOTTOM,
                        GXHDO101B052Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B052Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B052Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_BOTTOM,
                        GXHDO101B052Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B052Const.BTN_DELETE_TOP,
                        GXHDO101B052Const.BTN_UPDATE_TOP,
                        GXHDO101B052Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B052Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B052Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B052Const.BTN_INSERT_BOTTOM,
                        GXHDO101B052Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B052Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B052Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B052Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B052Const.BTN_INSERT_BOTTOM,
                        GXHDO101B052Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B052Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_BOTTOM,
                        GXHDO101B052Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B052Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B052Const.BTN_INSERT_TOP,
                        GXHDO101B052Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B052Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B052Const.BTN_DELETE_BOTTOM,
                        GXHDO101B052Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B052Const.BTN_DELETE_TOP,
                        GXHDO101B052Const.BTN_UPDATE_TOP));

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
            case GXHDO101B052Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B052Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B052Const.BTN_INSERT_TOP:
            case GXHDO101B052Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B052Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B052Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B052Const.BTN_UPDATE_TOP:
            case GXHDO101B052Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B052Const.BTN_DELETE_TOP:
            case GXHDO101B052Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B052Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B052Const.BTN_STARTDATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B052Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B052Const.BTN_ENDDATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // 終了日時計算
            case GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_TOP:
            case GXHDO101B052Const.BTN_ENDDATETIME_KEISAN_BOTTOM:
                method = "setShuryouDateTimeKeisan";
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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, queryRunnerWip, lotNo, formId, paramJissekino)) {
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
        this.setItemData(processData, GXHDO101B052Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B052Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B052Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B052Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B052Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B052Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B052Const.OWNER, ownercode + ":" + owner);
        }

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param queryRunnerWip QueryRunnerオブジェクト(Wip)
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @param jissekino 実績No
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            QueryRunner queryRunnerWip, String lotNo, String formId, int jissekino) throws SQLException {

        List<SrMaebarrel> srMaebarrelDataList = new ArrayList<>();
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
                
                // 受入個数初期値設定
                this.setItemData(processData, GXHDO101B052Const.UKEIREKOSUU, getSyorisu(queryRunnerDoc, queryRunnerWip, lotNo));
                
                return true;
            }

            // 焼成前ﾊﾞﾚﾙデータ取得
            srMaebarrelDataList = getSrMaebarrelData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srMaebarrelDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srMaebarrelDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srMaebarrelDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srMaebarrelData 焼成前ﾊﾞﾚﾙデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrMaebarrel srMaebarrelData) {

        // 受入個数
        this.setItemData(processData, GXHDO101B052Const.UKEIREKOSUU, getSrMaebarrelItemData(GXHDO101B052Const.UKEIREKOSUU, srMaebarrelData));
        // 研磨方式
        this.setItemData(processData, GXHDO101B052Const.KENMA, getSrMaebarrelItemData(GXHDO101B052Const.KENMA, srMaebarrelData));
        // 研磨号機
        this.setItemData(processData, GXHDO101B052Const.BGOKI, getSrMaebarrelItemData(GXHDO101B052Const.BGOKI, srMaebarrelData));
        // ﾎﾟｯﾄ数
        this.setItemData(processData, GXHDO101B052Const.POTSUU, getSrMaebarrelItemData(GXHDO101B052Const.POTSUU, srMaebarrelData));
        // ﾎﾟｯﾄﾁｬｰｼﾞ量
        this.setItemData(processData, GXHDO101B052Const.POT_CHARGERYOU, getSrMaebarrelItemData(GXHDO101B052Const.POT_CHARGERYOU, srMaebarrelData));
        // 片栗粉量
        this.setItemData(processData, GXHDO101B052Const.KATAKURI_FUNRYOU, getSrMaebarrelItemData(GXHDO101B052Const.KATAKURI_FUNRYOU, srMaebarrelData));
        // ﾒﾃﾞｨｱ種類
        this.setItemData(processData, GXHDO101B052Const.MEDIA_SYURUI, getSrMaebarrelItemData(GXHDO101B052Const.MEDIA_SYURUI, srMaebarrelData));
        // ﾒﾃﾞｨｱ量
        this.setItemData(processData, GXHDO101B052Const.MEDIA_RYOU, getSrMaebarrelItemData(GXHDO101B052Const.MEDIA_RYOU, srMaebarrelData));
        // 研磨時間①
        this.setItemData(processData, GXHDO101B052Const.BJIKAN, getSrMaebarrelItemData(GXHDO101B052Const.BJIKAN, srMaebarrelData));
        // 研磨機回転数①
        this.setItemData(processData, GXHDO101B052Const.BJYOKENSYUSOKUDO, getSrMaebarrelItemData(GXHDO101B052Const.BJYOKENSYUSOKUDO, srMaebarrelData));
        // 研磨時間②
        this.setItemData(processData, GXHDO101B052Const.BJIKAN2, getSrMaebarrelItemData(GXHDO101B052Const.BJIKAN2, srMaebarrelData));
        // 研磨機回転数②
        this.setItemData(processData, GXHDO101B052Const.BJYOKENSYUSOKUDO2, getSrMaebarrelItemData(GXHDO101B052Const.BJYOKENSYUSOKUDO2, srMaebarrelData));
        // 研磨時間③
        this.setItemData(processData, GXHDO101B052Const.BJIKAN3, getSrMaebarrelItemData(GXHDO101B052Const.BJIKAN3, srMaebarrelData));
        // 研磨機回転数③
        this.setItemData(processData, GXHDO101B052Const.BJYOKENSYUSOKUDO3, getSrMaebarrelItemData(GXHDO101B052Const.BJYOKENSYUSOKUDO3, srMaebarrelData));
        // 研磨時間④
        this.setItemData(processData, GXHDO101B052Const.BJIKAN4, getSrMaebarrelItemData(GXHDO101B052Const.BJIKAN4, srMaebarrelData));
        // 研磨機回転数④
        this.setItemData(processData, GXHDO101B052Const.BJYOKENSYUSOKUDO4, getSrMaebarrelItemData(GXHDO101B052Const.BJYOKENSYUSOKUDO4, srMaebarrelData));
        // 研磨時間⑤
        this.setItemData(processData, GXHDO101B052Const.BJIKAN5, getSrMaebarrelItemData(GXHDO101B052Const.BJIKAN5, srMaebarrelData));
        // 研磨機回転数⑤
        this.setItemData(processData, GXHDO101B052Const.BJYOKENSYUSOKUDO5, getSrMaebarrelItemData(GXHDO101B052Const.BJYOKENSYUSOKUDO5, srMaebarrelData));
        // 研磨時間⑥
        this.setItemData(processData, GXHDO101B052Const.BJIKAN6, getSrMaebarrelItemData(GXHDO101B052Const.BJIKAN6, srMaebarrelData));
        // 研磨機回転数⑥
        this.setItemData(processData, GXHDO101B052Const.BJYOKENSYUSOKUDO6, getSrMaebarrelItemData(GXHDO101B052Const.BJYOKENSYUSOKUDO6, srMaebarrelData));
        // 開始日
        this.setItemData(processData, GXHDO101B052Const.KAISHI_DAY, getSrMaebarrelItemData(GXHDO101B052Const.KAISHI_DAY, srMaebarrelData));
        // 開始時刻
        this.setItemData(processData, GXHDO101B052Const.KAISHI_TIME, getSrMaebarrelItemData(GXHDO101B052Const.KAISHI_TIME, srMaebarrelData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B052Const.KAISHI_TANTOUSYA, getSrMaebarrelItemData(GXHDO101B052Const.KAISHI_TANTOUSYA, srMaebarrelData));
        // 開始確認者
        this.setItemData(processData, GXHDO101B052Const.KAKUNINSYA, getSrMaebarrelItemData(GXHDO101B052Const.KAKUNINSYA, srMaebarrelData));
        // 終了日
        this.setItemData(processData, GXHDO101B052Const.SHURYOU_DAY, getSrMaebarrelItemData(GXHDO101B052Const.SHURYOU_DAY, srMaebarrelData));
        // 終了時刻
        this.setItemData(processData, GXHDO101B052Const.SHURYOU_TIME, getSrMaebarrelItemData(GXHDO101B052Const.SHURYOU_TIME, srMaebarrelData));
        // 終了担当者
        this.setItemData(processData, GXHDO101B052Const.SHURYOU_TANTOUSYA, getSrMaebarrelItemData(GXHDO101B052Const.SHURYOU_TANTOUSYA, srMaebarrelData));
        // 備考1
        this.setItemData(processData, GXHDO101B052Const.BIKO1, getSrMaebarrelItemData(GXHDO101B052Const.BIKO1, srMaebarrelData));
        // 備考2
        this.setItemData(processData, GXHDO101B052Const.BIKO2, getSrMaebarrelItemData(GXHDO101B052Const.BIKO2, srMaebarrelData));
        // 備考3
        this.setItemData(processData, GXHDO101B052Const.BIKO3, getSrMaebarrelItemData(GXHDO101B052Const.BIKO3, srMaebarrelData));
        // 備考4
        this.setItemData(processData, GXHDO101B052Const.BIKO4, getSrMaebarrelItemData(GXHDO101B052Const.BIKO4, srMaebarrelData));
 
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
    private List<SrMaebarrel> getSrMaebarrelData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrMaebarrel(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrMaebarrel(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [焼成前ﾊﾞﾚﾙ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrMaebarrel> loadSrMaebarrel(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,ukeirekosuu ,"
                + " kenma ,bgoki ,potsuu ,potcsuu ,katakuriko ,mediasyurui ,mediasenbetu ,"
                + " bjikan1 ,bkaiten1 ,bjikan2 ,bkaiten2 ,bjikan3 ,bkaiten3 ,bjikan4 ,bkaiten4 ,bjikan5 ,bkaiten5 ,bjikan6 ,bkaiten6 ,"
                + " kaisinichiji ,kaisitantosya ,kaisikakuninsya ,syuryonichiji ,syuryotantosya ,biko1 ,biko2 ,biko3 ,biko4 ,"
                + " torokunichiji ,kosinnichiji ,revision "
                + "FROM sr_maebarrel "
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
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ        
        mapping.put("ukeirekosuu", "ukeirekosuu"); //受入個数
        mapping.put("kenma", "kenma"); //研磨方法
        mapping.put("bgoki", "bgoki"); //研磨号機
        mapping.put("potsuu", "potsuu"); //ﾎﾟｯﾄ数
        mapping.put("potcsuu", "potcsuu"); //ﾎﾟｯﾄﾁｬｰｼﾞ量        
        mapping.put("katakuriko", "katakuriko"); //片栗粉量
        mapping.put("mediasyurui", "mediasyurui"); //ﾒﾃﾞｨｱ種類
        mapping.put("mediasenbetu", "mediasenbetu"); //ﾒﾃﾞｨｱ量
        mapping.put("bjikan1", "bjikan1"); //研磨時間①
        mapping.put("bkaiten1", "bkaiten1"); //研磨機回転数①
        mapping.put("bjikan2", "bjikan2"); //研磨時間②
        mapping.put("bkaiten2", "bkaiten2"); //研磨機回転数②
        mapping.put("bjikan3", "bjikan3"); //研磨時間③
        mapping.put("bkaiten3", "bkaiten3"); //研磨機回転数③
        mapping.put("bjikan4", "bjikan4"); //研磨時間④
        mapping.put("bkaiten4", "bkaiten4"); //研磨機回転数④
        mapping.put("bjikan5", "bjikan5"); //研磨時間⑤
        mapping.put("bkaiten5", "bkaiten5"); //研磨機回転数⑤
        mapping.put("bjikan6", "bjikan6"); //研磨時間⑥
        mapping.put("bkaiten6", "bkaiten6"); //研磨機回転数⑥
        mapping.put("kaisinichiji", "kaisinichiji"); //開始日時
        mapping.put("kaisitantosya", "kaisitantosya"); //担当者
        mapping.put("kaisikakuninsya", "kaisikakuninsya"); //開始確認者
        mapping.put("syuryonichiji", "syuryonichiji"); //終了日時
        mapping.put("syuryotantosya", "syuryotantosya"); //終了担当者
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("biko3", "biko3"); //備考3
        mapping.put("biko4", "biko4"); //備考4
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinNichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMaebarrel>> beanHandler = new BeanListHandler<>(SrMaebarrel.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [焼成前ﾊﾞﾚﾙ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrMaebarrel> loadTmpSrMaebarrel(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        

        String sql = "SELECT kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,ukeirekosuu ,"
                + " kenma ,bgoki ,potsuu ,potcsuu ,katakuriko ,mediasyurui ,mediasenbetu ,"
                + " bjikan1 ,bkaiten1 ,bjikan2 ,bkaiten2 ,bjikan3 ,bkaiten3 ,bjikan4 ,bkaiten4 ,bjikan5 ,bkaiten5 ,bjikan6 ,bkaiten6 ,"
                + " kaisinichiji ,kaisitantosya ,kaisikakuninsya ,syuryonichiji ,syuryotantosya ,biko1 ,biko2 ,biko3 ,biko4 ,"
                + " torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + "FROM tmp_sr_maebarrel "
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
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ        
        mapping.put("ukeirekosuu", "ukeirekosuu"); //受入個数
        mapping.put("kenma", "kenma"); //研磨方法
        mapping.put("bgoki", "bgoki"); //研磨号機
        mapping.put("potsuu", "potsuu"); //ﾎﾟｯﾄ数
        mapping.put("potcsuu", "potcsuu"); //ﾎﾟｯﾄﾁｬｰｼﾞ量        
        mapping.put("katakuriko", "katakuriko"); //片栗粉量
        mapping.put("mediasyurui", "mediasyurui"); //ﾒﾃﾞｨｱ種類
        mapping.put("mediasenbetu", "mediasenbetu"); //ﾒﾃﾞｨｱ量
        mapping.put("bjikan1", "bjikan1"); //研磨時間①
        mapping.put("bkaiten1", "bkaiten1"); //研磨機回転数①
        mapping.put("bjikan2", "bjikan2"); //研磨時間②
        mapping.put("bkaiten2", "bkaiten2"); //研磨機回転数②
        mapping.put("bjikan3", "bjikan3"); //研磨時間③
        mapping.put("bkaiten3", "bkaiten3"); //研磨機回転数③
        mapping.put("bjikan4", "bjikan4"); //研磨時間④
        mapping.put("bkaiten4", "bkaiten4"); //研磨機回転数④
        mapping.put("bjikan5", "bjikan5"); //研磨時間⑤
        mapping.put("bkaiten5", "bkaiten5"); //研磨機回転数⑤
        mapping.put("bjikan6", "bjikan6"); //研磨時間⑥
        mapping.put("bkaiten6", "bkaiten6"); //研磨機回転数⑥
        mapping.put("kaisinichiji", "kaisinichiji"); //開始日時
        mapping.put("kaisitantosya", "kaisitantosya"); //担当者
        mapping.put("kaisikakuninsya", "kaisikakuninsya"); //開始確認者
        mapping.put("syuryonichiji", "syuryonichiji"); //終了日時
        mapping.put("syuryotantosya", "syuryotantosya"); //終了担当者
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("biko3", "biko3"); //備考3
        mapping.put("biko4", "biko4"); //備考4
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinNichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrMaebarrel>> beanHandler = new BeanListHandler<>(SrMaebarrel.class, rowProcessor);

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

            // 焼成前ﾊﾞﾚﾙデータ取得
            List<SrMaebarrel> srBarrel1DataList = getSrMaebarrelData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
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
    private String getItemData(List<FXHDD01> listData, String itemId, SrMaebarrel srMaebarrelData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srMaebarrelData != null) {
            // 元データが存在する場合元データより取得
            return getSrMaebarrelItemData(itemId, srMaebarrelData);
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
     * @param systemTime システム日付
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
     * 焼成前ﾊﾞﾚﾙ_仮登録(tmp_sr_maebarrel)登録処理
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
    private void insertTmpSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_maebarrel ("
                + " kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,ukeirekosuu ,"
                + " kenma ,bgoki ,potsuu ,potcsuu ,katakuriko ,mediasyurui ,mediasenbetu ,"
                + " bjikan1 ,bkaiten1 ,bjikan2 ,bkaiten2 ,bjikan3 ,bkaiten3 ,bjikan4 ,bkaiten4 ,bjikan5 ,bkaiten5 ,bjikan6 ,bkaiten6 ,"
                + " kaisinichiji ,kaisitantosya ,kaisikakuninsya ,syuryonichiji ,syuryotantosya ,biko1 ,biko2 ,biko3 ,biko4 ,"
                + " torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrMaebarrel(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成前ﾊﾞﾚﾙ_仮登録(tmp_sr_maebarrel)更新処理
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
    private void updateTmpSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_maebarrel SET "
                + " kcpno = ? ,tokuisaki = ? ,lotkubuncode = ? ,ownercode = ? ,ukeirekosuu = ? ,"
                + " kenma = ? ,bgoki = ? ,potsuu = ? ,potcsuu = ? ,katakuriko = ? ,mediasyurui = ? ,mediasenbetu = ? ,"
                + " bjikan1 = ? ,bkaiten1 = ? ,bjikan2 = ? ,bkaiten2 = ? ,bjikan3 = ? ,bkaiten3 = ? ,"
                + " bjikan4 = ? ,bkaiten4 = ? ,bjikan5 = ? ,bkaiten5 = ? ,bjikan6 = ? ,bkaiten6 = ? ,"
                + " kaisinichiji = ? ,kaisitantosya = ? ,kaisikakuninsya = ? ,syuryonichiji = ? ,syuryotantosya = ? ,biko1 = ? ,biko2 = ? ,biko3 = ? ,biko4 = ? ,"
                + " kosinnichiji = ? ,revision = ? ,deleteflag = ?  "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMaebarrel> srSrMaebarrelList = getSrMaebarrelData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrMaebarrel srMaebarrel = null;
        if (!srSrMaebarrelList.isEmpty()) {
            srMaebarrel = srSrMaebarrelList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrMaebarrel(false, newRev, 0, "", "", "", systemTime, itemList, srMaebarrel);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成前ﾊﾞﾚﾙ_仮登録(tmp_sr_maebarrel)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_maebarrel "
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
     * 焼成前ﾊﾞﾚﾙ_仮登録(tmp_sr_maebarrel)更新値パラメータ設定
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
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrMaebarrel(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrMaebarrel srMaebarrelData) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KCPNO, srMaebarrelData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KYAKUSAKI, srMaebarrelData))); //客先
        //ﾛｯﾄ区分
        String lotKbn = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.LOT_KUBUN, srMaebarrelData));
        String[] spLotKbn = lotKbn.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spLotKbn[0]));
        //ｵｰﾅｰ
        String owner = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.OWNER, srMaebarrelData));
        String[] spOwner = owner.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spOwner[0]));
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.UKEIREKOSUU, srMaebarrelData))); //受入個数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KENMA, srMaebarrelData))); //研磨方法
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BGOKI, srMaebarrelData))); //研磨号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.POTSUU, srMaebarrelData))); //ﾎﾟｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.POT_CHARGERYOU, srMaebarrelData))); //ﾎﾟｯﾄﾁｬｰｼﾞ量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KATAKURI_FUNRYOU, srMaebarrelData))); //片栗粉量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.MEDIA_SYURUI, srMaebarrelData))); //ﾒﾃﾞｨｱ種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.MEDIA_RYOU, srMaebarrelData))); //ﾒﾃﾞｨｱ量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN, srMaebarrelData))); //研磨時間①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO, srMaebarrelData))); //研磨機回転数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN2, srMaebarrelData))); //研磨時間②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO2, srMaebarrelData))); //研磨機回転数②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN3, srMaebarrelData))); //研磨時間③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO3, srMaebarrelData))); //研磨機回転数③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN4, srMaebarrelData))); //研磨時間④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO4, srMaebarrelData))); //研磨機回転数④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN5, srMaebarrelData))); //研磨時間⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO5, srMaebarrelData))); //研磨機回転数⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN6, srMaebarrelData))); //研磨時間⑥
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO6, srMaebarrelData))); //研磨機回転数⑥
        //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAISHI_DAY, srMaebarrelData),
            getItemData(itemList, GXHDO101B050Const.KAISHI_TIME, srMaebarrelData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KAISHI_TANTOUSYA, srMaebarrelData))); //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KAKUNINSYA, srMaebarrelData))); //開始確認者
        //終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHURYOU_DAY, srMaebarrelData),
            getItemData(itemList, GXHDO101B050Const.SHURYOU_TIME, srMaebarrelData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.SHURYOU_TANTOUSYA, srMaebarrelData))); //終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO1, srMaebarrelData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO2, srMaebarrelData))); //備考2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO3, srMaebarrelData))); //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO4, srMaebarrelData))); //備考4

        if (isInsert) {
            //INSERT時のみマッピングする項目
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            //UPDATE時のみマッピングする項目
            params.add(systemTime); //更新日時
        }

        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 焼成前ﾊﾞﾚﾙ(sr_maebarrel)登録処理
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
    private void insertSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban,Timestamp systemTime, List<FXHDD01> itemList, SrMaebarrel tmpSrMaebarrel) throws SQLException {

        String sql = "INSERT INTO sr_maebarrel ("
                + "kojyo ,lotno ,edaban  ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,ukeirekosuu ,"
                + " kenma ,bgoki ,potsuu ,potcsuu ,katakuriko ,mediasyurui ,mediasenbetu ,"
                + " bjikan1 ,bkaiten1 ,bjikan2 ,bkaiten2 ,bjikan3 ,bkaiten3 ,bjikan4 ,bkaiten4 ,bjikan5 ,bkaiten5 ,bjikan6 ,bkaiten6 ,"
                + " kaisinichiji ,kaisitantosya ,kaisikakuninsya ,syuryonichiji ,syuryotantosya ,biko1 ,biko2 ,biko3 ,biko4 ,"
                + " torokunichiji ,kosinnichiji ,revision "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrMaebarrel(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrMaebarrel);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成前ﾊﾞﾚﾙ(sr_maebarrel)更新処理
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
    private void updateSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_maebarrel SET "
                + " kcpno = ? ,tokuisaki = ? ,lotkubuncode = ? ,ownercode = ? ,ukeirekosuu = ? ,"
                + " kenma = ? ,bgoki = ? ,potsuu = ? ,potcsuu = ? ,katakuriko = ? ,mediasyurui = ? ,mediasenbetu = ? ,"
                + " bjikan1 = ? ,bkaiten1 = ? ,bjikan2 = ? ,bkaiten2 = ? ,bjikan3 = ? ,bkaiten3 = ? ,"
                + " bjikan4 = ? ,bkaiten4 = ? ,bjikan5 = ? ,bkaiten5 = ? ,bjikan6 = ? ,bkaiten6 = ? ,"
                + " kaisinichiji = ? ,kaisitantosya = ? ,kaisikakuninsya = ? ,syuryonichiji = ? ,syuryotantosya = ? ,biko1 = ? ,biko2 = ? ,biko3 = ? ,biko4 = ? ,"
                + " kosinnichiji = ? ,revision = ?  "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrMaebarrel> srMaebarrelList = getSrMaebarrelData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrMaebarrel srMaebarrel = null;
        if (!srMaebarrelList.isEmpty()) {
            srMaebarrel = srMaebarrelList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrMaebarrel(false, newRev, "", "", "", systemTime, itemList, srMaebarrel);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成前ﾊﾞﾚﾙ(sr_maebarrel)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srBarrel1Data ﾊﾞﾚﾙデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrMaebarrel(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrMaebarrel srMaebarrelData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KCPNO, srMaebarrelData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KYAKUSAKI, srMaebarrelData))); //客先
        //ﾛｯﾄ区分
        String lotKbn = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.LOT_KUBUN, srMaebarrelData));
        String[] spLotKbn = lotKbn.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spLotKbn[0]));
        //ｵｰﾅｰ
        String owner = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.OWNER, srMaebarrelData));
        String[] spOwner = owner.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spOwner[0]));
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.UKEIREKOSUU, srMaebarrelData))); //受入個数        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KENMA, srMaebarrelData))); //研磨方式
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BGOKI, srMaebarrelData))); //研磨号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.POTSUU, srMaebarrelData))); //ﾎﾟｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.POT_CHARGERYOU, srMaebarrelData))); //ﾎﾟｯﾄﾁｬｰｼﾞ量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KATAKURI_FUNRYOU, srMaebarrelData))); //片栗粉量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.MEDIA_SYURUI, srMaebarrelData))); //ﾒﾃﾞｨｱ種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.MEDIA_RYOU, srMaebarrelData))); //ﾒﾃﾞｨｱ量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN, srMaebarrelData))); //研磨時間①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO, srMaebarrelData))); //研磨機回転数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN2, srMaebarrelData))); //研磨時間②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO2, srMaebarrelData))); //研磨機回転数②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN3, srMaebarrelData))); //研磨時間③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO3, srMaebarrelData))); //研磨機回転数③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN4, srMaebarrelData))); //研磨時間④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO4, srMaebarrelData))); //研磨機回転数④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN5, srMaebarrelData))); //研磨時間⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO5, srMaebarrelData))); //研磨機回転数⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJIKAN6, srMaebarrelData))); //研磨時間⑥
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BJYOKENSYUSOKUDO6, srMaebarrelData))); //研磨機回転数⑥
        //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAISHI_DAY, srMaebarrelData),
            getItemData(itemList, GXHDO101B050Const.KAISHI_TIME, srMaebarrelData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KAISHI_TANTOUSYA, srMaebarrelData))); //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.KAKUNINSYA, srMaebarrelData))); //開始確認者
        //終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHURYOU_DAY, srMaebarrelData),
            getItemData(itemList, GXHDO101B050Const.SHURYOU_TIME, srMaebarrelData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.SHURYOU_TANTOUSYA, srMaebarrelData))); //終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO1, srMaebarrelData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO2, srMaebarrelData))); //備考2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO3, srMaebarrelData))); //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B052Const.BIKO4, srMaebarrelData))); //備考4

        if (isInsert) {
            //INSERT時のみマッピングする項目
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            //UPDATE時のみマッピングする項目
            params.add(systemTime); //更新日時
        }

        params.add(newRev); //revision

        return params;
    }

    /**
     * 焼成前ﾊﾞﾚﾙ(sr_maebarrel)削除処理
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
    private void deleteSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_maebarrel "
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
     * [焼成前ﾊﾞﾚﾙ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_maebarrel "
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
     * 開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B052Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B052Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B052Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B052Const.SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }
    
    /**
     * 終了日時計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShuryouDateTimeKeisan(ProcessData processData) {
        
        processData.setMethod("");
        
        try {
            FXHDD01 itemEndDay = getItemRow(processData.getItemList(), GXHDO101B052Const.SHURYOU_DAY);
            FXHDD01 itemEndTime = getItemRow(processData.getItemList(), GXHDO101B052Const.SHURYOU_TIME);
            // ①入力ﾁｪｯｸ(0は入力とみなす)
            //  1.入力されている場合
            //   当処理(【終了日時計算】ﾎﾞﾀﾝ押下時)を終了する。
            //    【対象項目】
            //     ・終了日
            //     ・終了時刻
            //  2.入力されていない場合
            //   以降の処理を実行する。

            if (StringUtil.isEmpty(itemEndDay.getValue()) && StringUtil.isEmpty(itemEndTime.getValue())) {

                // ②入力値ﾁｪｯｸを行う
                //    【対象項目】
                //     ・研磨時間①～⑥
                //     ・開始日
                //     ・開始時刻
                FXHDD01 itemBjikan1 = getItemRow(processData.getItemList(), GXHDO101B052Const.BJIKAN);
                FXHDD01 itemBjikan2 = getItemRow(processData.getItemList(), GXHDO101B052Const.BJIKAN2);
                FXHDD01 itemBjikan3 = getItemRow(processData.getItemList(), GXHDO101B052Const.BJIKAN3);
                FXHDD01 itemBjikan4 = getItemRow(processData.getItemList(), GXHDO101B052Const.BJIKAN4);
                FXHDD01 itemBjikan5 = getItemRow(processData.getItemList(), GXHDO101B052Const.BJIKAN5);
                FXHDD01 itemBjikan6 = getItemRow(processData.getItemList(), GXHDO101B052Const.BJIKAN6);
                FXHDD01 itemStartDay = getItemRow(processData.getItemList(), GXHDO101B052Const.KAISHI_DAY);
                FXHDD01 itemStartTime = getItemRow(processData.getItemList(), GXHDO101B052Const.KAISHI_TIME);

                //  1.【開始日】、【開始時刻】が入力されていない場合
                //  2.【研磨時間①～⑥】いずれも入力されていない場合
                //   当処理(【終了日時計算】ﾎﾞﾀﾝ押下時)を終了する。
                if ((!StringUtil.isEmpty(itemBjikan1.getValue()) || !StringUtil.isEmpty(itemBjikan2.getValue()) || !StringUtil.isEmpty(itemBjikan3.getValue()) ||
                        !StringUtil.isEmpty(itemBjikan4.getValue()) || !StringUtil.isEmpty(itemBjikan5.getValue()) || !StringUtil.isEmpty(itemBjikan6.getValue())) &&
                        !StringUtil.isEmpty(itemStartDay.getValue()) && !StringUtil.isEmpty(itemStartTime.getValue())) {

                    //  3.【研磨時間】の入力値が数値変換できない場合。
                    //   当処理(【終了日時計算】ﾎﾞﾀﾝ押下時)を終了する。
                    BigDecimal decBjikan1 = new BigDecimal(0);
                    BigDecimal decBjikan2 = new BigDecimal(0);
                    BigDecimal decBjikan3 = new BigDecimal(0);
                    BigDecimal decBjikan4 = new BigDecimal(0);
                    BigDecimal decBjikan5 = new BigDecimal(0);
                    BigDecimal decBjikan6 = new BigDecimal(0);
                    //  3.【研磨時間】の入力値が0もしくは0以下 だった場合。
                    // 当処理(【終了日時計算】ﾎﾞﾀﾝ押下時)を終了する。
                    if (!StringUtil.isEmpty(itemBjikan1.getValue())){
                        BigDecimal bjikan1 = new BigDecimal(itemBjikan1.getValue());
                        if(0 <= BigDecimal.ZERO.compareTo(bjikan1)){
                            return processData;
                        }
                        decBjikan1 = bjikan1;
                    }
                    if (!StringUtil.isEmpty(itemBjikan2.getValue())){
                        BigDecimal bjikan2 = new BigDecimal(itemBjikan2.getValue());
                        if(0 <= BigDecimal.ZERO.compareTo(bjikan2)){
                            return processData;
                        }
                        decBjikan2 = bjikan2;
                    }
                    if (!StringUtil.isEmpty(itemBjikan3.getValue())){
                        BigDecimal bjikan3 = new BigDecimal(itemBjikan3.getValue());
                        if(0 <= BigDecimal.ZERO.compareTo(bjikan3)){
                            return processData;
                        }
                        decBjikan3 = bjikan3;
                    }
                    if (!StringUtil.isEmpty(itemBjikan4.getValue())){
                        BigDecimal bjikan4 = new BigDecimal(itemBjikan4.getValue());
                        if(0 <= BigDecimal.ZERO.compareTo(bjikan4)){
                            return processData;
                        }
                        decBjikan4 = bjikan4;
                    }
                    if (!StringUtil.isEmpty(itemBjikan5.getValue())){
                        BigDecimal bjikan5 = new BigDecimal(itemBjikan5.getValue());
                        if(0 <= BigDecimal.ZERO.compareTo(bjikan5)){
                            return processData;
                        }
                        decBjikan5 = bjikan5;
                    }
                    if (!StringUtil.isEmpty(itemBjikan6.getValue())){
                        BigDecimal bjikan6 = new BigDecimal(itemBjikan6.getValue());
                        if(0 <= BigDecimal.ZERO.compareTo(bjikan6)){
                            return processData;
                        }
                        decBjikan6 = bjikan6;
                    }

                    //  4.【開始日】の入力値が日付型に変換できない範囲の値である場合。
                    //   当処理(【終了日時計算】ﾎﾞﾀﾝ押下時)を終了する。
                    String startDate =  StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B052Const.KAISHI_DAY, null));

                    //  5.【開始時刻】の入力値が時刻型に変換できない範囲の値である場合。
                    //   当処理(【終了日時計算】ﾎﾞﾀﾝ押下時)を終了する。
                    String startTime =  StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B052Const.KAISHI_TIME, null));
                    
                    //  6.上記以外の場合
                    //   以降の処理を実行する。
                    
                    //   A.「開始日」と「開始時刻」を利用し日付型に変換し、「研磨時間」の値を【分】として加算する。
                    // ③計算処理を実施する。
                    Calendar cal = Calendar.getInstance();
                    Date dateStartDateTime = DateUtil.convertStringToDate(startDate, startTime);
                    cal.setTime(dateStartDateTime);
                    cal.add(Calendar.MINUTE, decBjikan1.intValue());
                    cal.add(Calendar.MINUTE, decBjikan2.intValue());
                    cal.add(Calendar.MINUTE, decBjikan3.intValue());
                    cal.add(Calendar.MINUTE, decBjikan4.intValue());
                    cal.add(Calendar.MINUTE, decBjikan5.intValue());
                    cal.add(Calendar.MINUTE, decBjikan6.intValue());
                    dateStartDateTime = cal.getTime();

                    itemEndDay.setValue(new SimpleDateFormat("yyMMdd").format(dateStartDateTime));
                    itemEndTime.setValue(new SimpleDateFormat("HHmm").format(dateStartDateTime));
                }
            }
        
        } catch (NumberFormatException e) {
            //数値型変換失敗時はそのままリターン
            //日付型変換失敗時はそのままリターン
        }
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
     * @param srMaebarrelData 焼成ﾊﾞﾚﾙデータ
     * @return DB値
     */
    private String getSrMaebarrelItemData(String itemId, SrMaebarrel srMaebarrelData) {
        switch (itemId) {
            // ﾛｯﾄNo.
            case GXHDO101B052Const.LOTNO:
                return StringUtil.nullToBlank(srMaebarrelData.getLotno());
            // KCPNO
            case GXHDO101B052Const.KCPNO:
                return StringUtil.nullToBlank(srMaebarrelData.getKcpno());
            // 客先
            case GXHDO101B052Const.KYAKUSAKI:
                return StringUtil.nullToBlank(srMaebarrelData.getTokuisaki());
            // ﾛｯﾄ区分
            case GXHDO101B052Const.LOT_KUBUN:
                return StringUtil.nullToBlank(srMaebarrelData.getLotkubuncode());
            // ｵｰﾅｰ
            case GXHDO101B052Const.OWNER:
                return StringUtil.nullToBlank(srMaebarrelData.getOwnercode());
            // 受入個数
            case GXHDO101B052Const.UKEIREKOSUU:
                return StringUtil.nullToBlank(srMaebarrelData.getUkeirekosuu());
            // 研磨方法
            case GXHDO101B052Const.KENMA:
                return StringUtil.nullToBlank(srMaebarrelData.getKenma());
            // 研磨号機
            case GXHDO101B052Const.BGOKI:
                return StringUtil.nullToBlank(srMaebarrelData.getBgoki());
            // ﾎﾟｯﾄ数
            case GXHDO101B052Const.POTSUU:
                return StringUtil.nullToBlank(srMaebarrelData.getPotsuu());
            // ﾎﾟｯﾄﾁｬｰｼﾞ量
            case GXHDO101B052Const.POT_CHARGERYOU:
                return StringUtil.nullToBlank(srMaebarrelData.getPotcsuu());
            // 片栗粉量
            case GXHDO101B052Const.KATAKURI_FUNRYOU:
                return StringUtil.nullToBlank(srMaebarrelData.getKatakuriko());
            // ﾒﾃﾞｨｱ種類
            case GXHDO101B052Const.MEDIA_SYURUI:
                return StringUtil.nullToBlank(srMaebarrelData.getMediasyurui());
            // ﾒﾃﾞｨｱ量
            case GXHDO101B052Const.MEDIA_RYOU:
                return StringUtil.nullToBlank(srMaebarrelData.getMediasenbetu());
            // 研磨時間①
            case GXHDO101B052Const.BJIKAN:
                return StringUtil.nullToBlank(srMaebarrelData.getBjikan1());
            // 研磨機回転数①
            case GXHDO101B052Const.BJYOKENSYUSOKUDO:
                return StringUtil.nullToBlank(srMaebarrelData.getBkaiten1());
            // 研磨時間②
            case GXHDO101B052Const.BJIKAN2:
                return StringUtil.nullToBlank(srMaebarrelData.getBjikan2());
            // 研磨機回転数②
            case GXHDO101B052Const.BJYOKENSYUSOKUDO2:
                return StringUtil.nullToBlank(srMaebarrelData.getBkaiten2());
            // 研磨時間③
            case GXHDO101B052Const.BJIKAN3:
                return StringUtil.nullToBlank(srMaebarrelData.getBjikan3());
            // 研磨機回転数③
            case GXHDO101B052Const.BJYOKENSYUSOKUDO3:
                return StringUtil.nullToBlank(srMaebarrelData.getBkaiten3());
            // 研磨時間④
            case GXHDO101B052Const.BJIKAN4:
                return StringUtil.nullToBlank(srMaebarrelData.getBjikan4());
            // 研磨機回転数④
            case GXHDO101B052Const.BJYOKENSYUSOKUDO4:
                return StringUtil.nullToBlank(srMaebarrelData.getBkaiten4());
            // 研磨時間⑤
            case GXHDO101B052Const.BJIKAN5:
                return StringUtil.nullToBlank(srMaebarrelData.getBjikan5());
            // 研磨機回転数⑤
            case GXHDO101B052Const.BJYOKENSYUSOKUDO5:
                return StringUtil.nullToBlank(srMaebarrelData.getBkaiten5());
            // 研磨時間⑥
            case GXHDO101B052Const.BJIKAN6:
                return StringUtil.nullToBlank(srMaebarrelData.getBjikan6());
            // 研磨機回転数⑥
            case GXHDO101B052Const.BJYOKENSYUSOKUDO6:
                return StringUtil.nullToBlank(srMaebarrelData.getBkaiten6());
            // 開始日
            case GXHDO101B052Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srMaebarrelData.getKaisinichiji(), "yyMMdd");
            // 開始時刻
            case GXHDO101B052Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srMaebarrelData.getKaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B052Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srMaebarrelData.getKaisitantosya());
            // 開始確認者
            case GXHDO101B052Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srMaebarrelData.getKaisikakuninsya());
            // 終了日
            case GXHDO101B052Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srMaebarrelData.getSyuryonichiji(), "yyMMdd");
            // 終了時刻
            case GXHDO101B052Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srMaebarrelData.getSyuryonichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B052Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srMaebarrelData.getSyuryotantosya());
            // 備考1
            case GXHDO101B052Const.BIKO1:
                return StringUtil.nullToBlank(srMaebarrelData.getBiko1());
            // 備考2
            case GXHDO101B052Const.BIKO2:
                return StringUtil.nullToBlank(srMaebarrelData.getBiko2());
            // 備考3
            case GXHDO101B052Const.BIKO3:
                return StringUtil.nullToBlank(srMaebarrelData.getBiko3());
            // 備考4
            case GXHDO101B052Const.BIKO4:
                return StringUtil.nullToBlank(srMaebarrelData.getBiko4());
            default:
                return null;            
        }
    }

    /**
     * 焼成前ﾊﾞﾚﾙ_仮登録(tmp_sr_maebarrel)登録処理(削除時)
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
    private void insertDeleteDataTmpSrMaebarrel(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_maebarrel ("
                + " kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,ukeirekosuu ,"
                + " kenma ,bgoki ,potsuu ,potcsuu ,katakuriko ,mediasyurui ,mediasenbetu ,"
                + " bjikan1 ,bkaiten1 ,bjikan2 ,bkaiten2 ,bjikan3 ,bkaiten3 ,bjikan4 ,bkaiten4 ,bjikan5 ,bkaiten5 ,bjikan6 ,bkaiten6 ,"
                + " kaisinichiji ,kaisitantosya ,kaisikakuninsya ,syuryonichiji ,syuryotantosya ,biko1 ,biko2 ,biko3 ,biko4 ,"
                + " torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + ") SELECT "
                + " kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,ukeirekosuu ,"
                + " kenma ,bgoki ,potsuu ,potcsuu ,katakuriko ,mediasyurui ,mediasenbetu ,"
                + " bjikan1 ,bkaiten1 ,bjikan2 ,bkaiten2 ,bjikan3 ,bkaiten3 ,bjikan4 ,bkaiten4 ,bjikan5 ,bkaiten5 ,bjikan6 ,bkaiten6 ,"
                + " kaisinichiji ,kaisitantosya ,kaisikakuninsya ,syuryonichiji ,syuryotantosya ,biko1 ,biko2 ,biko3 ,biko4 ,"
                + " ? ,? ,? ,? "
                + " FROM sr_maebarrel "
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

    /**
     * [生産実績]から、ﾃﾞｰﾀを取得
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
     private List<Seisan> loadSeisanData(QueryRunner queryRunnerWip, int jissekiNo) throws SQLException {
        // 生産実績データの取得
        String sql = "SELECT goukicode "
                + "FROM seisan "
                + "WHERE jissekino = ? ";

        Map mapping = new HashMap<>();
        mapping.put("goukicode", "goukicode");
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<Seisan>> beanHandler = new BeanListHandler<>(Seisan.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.add(jissekiNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, beanHandler, params.toArray());
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
        
        // 実績データの取得
        String sql = "SELECT syorisuu,jissekino "
                + "FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";
        
        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());
        
        sql += " ORDER BY syoribi DESC, syorijikoku DESC";
        
        Map mapping = new HashMap<>();
        mapping.put("syorisuu", "syorisuu");
        mapping.put("jissekino", "jissekino");
        
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
     * @param queryRunnerDoc オブジェクト
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
             String sql = "SELECT data "
                        + " FROM fxhbm03 "
                        + " WHERE user_name = 'common_user' AND key = 'xhd_焼成前真空脱脂_koteicode' ";
            return queryRunnerDoc.query(sql, new MapHandler());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
                
    }

    /**
     * 処理数取得処理
     * @param queryRunnerDoc queryRunner(Doc)オブジェクト
     * @param queryRunnerWip queryRunner(Wip)オブジェクト
     * @param lotNo ﾛｯﾄNo
     * @return 処理
     */
    private String getSyorisu(QueryRunner queryRunnerDoc, QueryRunner queryRunnerWip, String lotNo) throws SQLException{
        // 処理数の取得
        String syorisuu = null;

        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc);
        if (fxhbm03Data != null && !fxhbm03Data.isEmpty()) {
            String strfxhbm03List = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
            String fxhbm03DataArr[] = strfxhbm03List.split(",");

            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, fxhbm03DataArr);
            if (jissekiData != null && jissekiData.size() > 0) {
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数  
                if (0 < dbShorisu) {
                    syorisuu = String.valueOf(dbShorisu);
                }
            }
        }
        return syorisuu;
    }

}
