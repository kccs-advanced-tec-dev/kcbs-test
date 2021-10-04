/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import jp.co.kccs.xhd.db.model.Jisseki;
import jp.co.kccs.xhd.db.model.SrDassisayadume;
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
 * 変更日	2021/09/20<br>
 * 計画書No	MB2108-DK001<br>
 * 変更者	SRC T.Takenouchi<br>
 * 変更理由    新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B050(焼成・真空脱脂ｻﾔ詰め)ロジック
 *
 * @author SRC T.Takenouchi
 * @since  2021/09/20
 */
public class GXHDO101B050 implements IFormLogic {

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
                    GXHDO101B050Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B050Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_TOP,
                    GXHDO101B050Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B050Const.BTN_ENDDATETIME_BOTTOM,
                    GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B050Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B050Const.BTN_INSERT_TOP,
                    GXHDO101B050Const.BTN_DELETE_TOP,
                    GXHDO101B050Const.BTN_UPDATE_TOP,
                    GXHDO101B050Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B050Const.BTN_INSERT_BOTTOM,
                    GXHDO101B050Const.BTN_DELETE_BOTTOM,
                    GXHDO101B050Const.BTN_UPDATE_BOTTOM));

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

                // 真空脱脂ｻﾔ詰め_仮登録登録処理
                insertTmpSrDassisayadume(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

            } else {

                // 真空脱脂ｻﾔ詰め_仮登録更新処理
                updateTmpSrDassisayadume(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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

        // ｻﾔ種類
        FXHDD01 itemSayaShurui = getItemRow(processData.getItemList(), GXHDO101B050Const.SAYASHURUI);
        if ("未選択".equals(itemSayaShurui.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(itemSayaShurui), itemSayaShurui.getLabel1());
        }

        // 粉まぶし
        FXHDD01 itemKonaMabushi = getItemRow(processData.getItemList(), GXHDO101B050Const.KONAMABUSHI);
        if ("未選択".equals(itemKonaMabushi.getValue())) {
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(itemKonaMabushi), itemKonaMabushi.getLabel1());
        }
        
        ValidateUtil validateUtil = new ValidateUtil();
        
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B050Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B050Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B050Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B050Const.SHURYOU_TIME); //終了時刻
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
            SrDassisayadume tmpSrDassisayadume = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrDassisayadume> srDassisayadumeList = getSrDassisayadumeData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srDassisayadumeList.isEmpty()) {
                    tmpSrDassisayadume = srDassisayadumeList.get(0);
                }
                
                deleteTmpSrDassisayadume(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 真空脱脂ｻﾔ詰め_登録処理
            insertSrDassisayadume(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList(), tmpSrDassisayadume);

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
        processData.setUserAuthParam(GXHDO101B050Const.USER_AUTH_UPDATE_PARAM);

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

            // 真空脱脂ｻﾔ詰め_更新処理
            updateSrDassisayadume(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, paramJissekino, systemTime, processData.getItemList());

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
        processData.setUserAuthParam(GXHDO101B050Const.USER_AUTH_DELETE_PARAM);

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

            // 真空脱脂ｻﾔ詰め_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrDassisayadume(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 真空脱脂ｻﾔ詰め_削除処理
            deleteSrDassisayadume(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO101B050Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B050Const.BTN_DELETE_BOTTOM,
                        GXHDO101B050Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B050Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B050Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_BOTTOM,
                        GXHDO101B050Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B050Const.BTN_DELETE_TOP,
                        GXHDO101B050Const.BTN_UPDATE_TOP,
                        GXHDO101B050Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B050Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B050Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B050Const.BTN_INSERT_BOTTOM,
                        GXHDO101B050Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B050Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B050Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B050Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B050Const.BTN_INSERT_BOTTOM,
                        GXHDO101B050Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B050Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_BOTTOM,
                        GXHDO101B050Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B050Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B050Const.BTN_INSERT_TOP,
                        GXHDO101B050Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B050Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B050Const.BTN_DELETE_BOTTOM,
                        GXHDO101B050Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B050Const.BTN_DELETE_TOP,
                        GXHDO101B050Const.BTN_UPDATE_TOP));

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
            case GXHDO101B050Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B050Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B050Const.BTN_INSERT_TOP:
            case GXHDO101B050Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B050Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B050Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B050Const.BTN_UPDATE_TOP:
            case GXHDO101B050Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B050Const.BTN_DELETE_TOP:
            case GXHDO101B050Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B050Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B050Const.BTN_STARTDATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B050Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B050Const.BTN_ENDDATETIME_BOTTOM:
                method = "setShuryouDateTime";
                break;
            // ｻﾔ詰め量計算
            case GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_TOP:
            case GXHDO101B050Const.BTN_SAYADUMERYOU_KEISAN_BOTTOM:
                method = "calcSayaDume";
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
        this.setItemData(processData, GXHDO101B050Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B050Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B050Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B050Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B050Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B050Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B050Const.OWNER, ownercode + ":" + owner);
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

        List<SrDassisayadume> srDassisayadumeDataList = new ArrayList<>();
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

                // 処理数初期値設定
                String strShorisu = getSyorisu(queryRunnerDoc, queryRunnerWip, lotNo);
                if (!StringUtil.isEmpty(strShorisu)){
                    FXHDD01 itemShorisu = getItemRow(processData.getItemList(), GXHDO101B050Const.SHORISU);
                    strShorisu = NumberUtil.getTruncatData(strShorisu, itemShorisu.getInputLength(), itemShorisu.getInputLengthDec());
                }
                this.setItemData(processData, GXHDO101B050Const.SHORISU, strShorisu);
                
                // 単位重量初期値設定
                String strTanijuryo = getTanijuryo(queryRunnerDoc, queryRunnerWip, lotNo);
                if (!StringUtil.isEmpty(strTanijuryo)){
                    FXHDD01 itemTanijuryo = getItemRow(processData.getItemList(), GXHDO101B050Const.TANIJURYO);
                    strTanijuryo = NumberUtil.getTruncatData(strTanijuryo, itemTanijuryo.getInputLength(), itemTanijuryo.getInputLengthDec());
                }                
                this.setItemData(processData, GXHDO101B050Const.TANIJURYO, strTanijuryo);
                
                // 総重量の計算・設定
                String strSojuryo = getSojuryo(strShorisu, strTanijuryo);
                if (!StringUtil.isEmpty(strSojuryo)){                
                    FXHDD01 itemSojuryo = getItemRow(processData.getItemList(), GXHDO101B050Const.SOJURYO);
                    strSojuryo = NumberUtil.getTruncatData(strSojuryo, itemSojuryo.getInputLength(), itemSojuryo.getInputLengthDec());
                }
                this.setItemData(processData, GXHDO101B050Const.SOJURYO, strSojuryo);

                return true;
            }

            // 真空脱脂ｻﾔ詰めデータ取得
            srDassisayadumeDataList = getSrDassisayadumeData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srDassisayadumeDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srDassisayadumeDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srDassisayadumeDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srDassisayadumeData 真空脱脂ｻﾔ詰めデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrDassisayadume srDassisayadumeData) {
        // 処理数
        this.setItemData(processData, GXHDO101B050Const.SHORISU, getSrDassisayadumeItemData(GXHDO101B050Const.SHORISU, srDassisayadumeData));
        // 単位重量
        this.setItemData(processData, GXHDO101B050Const.TANIJURYO, getSrDassisayadumeItemData(GXHDO101B050Const.TANIJURYO, srDassisayadumeData));
        // 総重量
        this.setItemData(processData, GXHDO101B050Const.SOJURYO, getSrDassisayadumeItemData(GXHDO101B050Const.SOJURYO, srDassisayadumeData));
        // 号機
        this.setItemData(processData, GXHDO101B050Const.GOKI, getSrDassisayadumeItemData(GXHDO101B050Const.GOKI, srDassisayadumeData));
        // ｻﾔ詰め方法
        this.setItemData(processData, GXHDO101B050Const.SAYADUME_HOUHOU, getSrDassisayadumeItemData(GXHDO101B050Const.SAYADUME_HOUHOU, srDassisayadumeData));
        // ｻﾔ種類
        this.setItemData(processData, GXHDO101B050Const.SAYASHURUI, getSrDassisayadumeItemData(GXHDO101B050Const.SAYASHURUI, srDassisayadumeData));
        // 粉まぶし
        this.setItemData(processData, GXHDO101B050Const.KONAMABUSHI, getSrDassisayadumeItemData(GXHDO101B050Const.KONAMABUSHI, srDassisayadumeData));
        // ｻﾔ詰め量
        this.setItemData(processData, GXHDO101B050Const.SAYADUME_RYOU, getSrDassisayadumeItemData(GXHDO101B050Const.SAYADUME_RYOU, srDassisayadumeData));
        // ｻﾔ枚数
        this.setItemData(processData, GXHDO101B050Const.SAYAMAISU, getSrDassisayadumeItemData(GXHDO101B050Const.SAYAMAISU, srDassisayadumeData));
        // 開始日
        this.setItemData(processData, GXHDO101B050Const.KAISHI_DAY, getSrDassisayadumeItemData(GXHDO101B050Const.KAISHI_DAY, srDassisayadumeData));
        // 開始時間
        this.setItemData(processData, GXHDO101B050Const.KAISHI_TIME, getSrDassisayadumeItemData(GXHDO101B050Const.KAISHI_TIME, srDassisayadumeData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B050Const.KAISHI_TANTOUSYA, getSrDassisayadumeItemData(GXHDO101B050Const.KAISHI_TANTOUSYA, srDassisayadumeData));
        // 開始確認者
        this.setItemData(processData, GXHDO101B050Const.KAKUNINSYA, getSrDassisayadumeItemData(GXHDO101B050Const.KAKUNINSYA, srDassisayadumeData));
        // 終了日
        this.setItemData(processData, GXHDO101B050Const.SHURYOU_DAY, getSrDassisayadumeItemData(GXHDO101B050Const.SHURYOU_DAY, srDassisayadumeData));
        // 終了時間
        this.setItemData(processData, GXHDO101B050Const.SHURYOU_TIME, getSrDassisayadumeItemData(GXHDO101B050Const.SHURYOU_TIME, srDassisayadumeData));
        // 終了担当者
        this.setItemData(processData, GXHDO101B050Const.SHURYOU_TANTOUSYA, getSrDassisayadumeItemData(GXHDO101B050Const.SHURYOU_TANTOUSYA, srDassisayadumeData));
        // 備考1
        this.setItemData(processData, GXHDO101B050Const.BIKO1, getSrDassisayadumeItemData(GXHDO101B050Const.BIKO1, srDassisayadumeData));
        // 備考2
        this.setItemData(processData, GXHDO101B050Const.BIKO2, getSrDassisayadumeItemData(GXHDO101B050Const.BIKO2, srDassisayadumeData));
        // 備考3
        this.setItemData(processData, GXHDO101B050Const.BIKO3, getSrDassisayadumeItemData(GXHDO101B050Const.BIKO3, srDassisayadumeData));
        // 備考4
        this.setItemData(processData, GXHDO101B050Const.BIKO4, getSrDassisayadumeItemData(GXHDO101B050Const.BIKO4, srDassisayadumeData));

    }

    /**
     * 真空脱脂ｻﾔ詰めの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 真空脱脂ｻﾔ詰め登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrDassisayadume> getSrDassisayadumeData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrDassisayadume(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrDassisayadume(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        String sql = "SELECT kcpno, oyalotedaban, tokuisaki, lotkubuncode, ownercode, tanijuryo "
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
     * [真空脱脂ｻﾔ詰め]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrDassisayadume> loadSrDassisayadume(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,kosuu ,ukeiretannijyuryo ,ukeiresoujyuryou ,"
                + " sayadumegouki ,sayadumehouhou ,sayasyurui ,konamabushi ,sayatumeryou ,sayasuu ,kaishinichiji ,tantousya ,startkakuninsyacode ,syuuryounichiji ,"
                + " endtantousyacode ,bikou1 ,bikou2 ,bikou3 ,bikou4 ,torokunichiji ,kosinnichiji ,revision , '0' AS deleteflag "
                + "FROM sr_dassisayadume "
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
        mapping.put("kojyo","kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno","lotno"); //ﾛｯﾄNo
        mapping.put("edaban","edaban"); //枝番
        mapping.put("kcpno","kcpno"); //KCPNO
        mapping.put("tokuisaki","tokuisaki"); //客先
        mapping.put("lotkubuncode","lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode","ownercode"); //ｵｰﾅｰ
        mapping.put("kosuu","kosuu"); //処理数
        mapping.put("ukeiretannijyuryo","ukeiretannijyuryo"); //単位重量
        mapping.put("ukeiresoujyuryou","ukeiresoujyuryou"); //総重量
        mapping.put("sayadumegouki","sayadumegouki"); //号機
        mapping.put("sayadumehouhou","sayadumehouhou"); //ｻﾔ詰め方法
        mapping.put("sayasyurui","sayasyurui"); //ｻﾔ種類
        mapping.put("konamabushi","konamabushi"); //粉まぶし
        mapping.put("sayatumeryou","sayatumeryou"); //ｻﾔ詰め量
        mapping.put("sayasuu","sayasuu"); //ｻﾔ枚数
        mapping.put("kaishinichiji","kaishinichiji"); //開始日時
        mapping.put("tantousya","tantousya"); //開始担当者
        mapping.put("startkakuninsyacode","startkakuninsyacode"); //開始確認者
        mapping.put("syuuryounichiji","syuuryounichiji"); //終了日時
        mapping.put("endtantousyacode","endtantousyacode"); //終了担当者
        mapping.put("bikou1","bikou1"); //備考1
        mapping.put("bikou2","bikou2"); //備考2
        mapping.put("bikou3","bikou3"); //備考3
        mapping.put("bikou4","bikou4"); //備考4
        mapping.put("torokunichiji","torokunichiji"); //登録日時
        mapping.put("kosinnichiji","kosinnichiji"); //更新日時
        mapping.put("revision","revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrDassisayadume>> beanHandler = new BeanListHandler<>(SrDassisayadume.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [真空脱脂ｻﾔ詰め_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrDassisayadume> loadTmpSrDassisayadume(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        String sql = "SELECT kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,kosuu ,ukeiretannijyuryo ,ukeiresoujyuryou ,"
                + " sayadumegouki ,sayadumehouhou ,sayasyurui ,konamabushi ,sayatumeryou ,sayasuu ,kaishinichiji ,tantousya ,startkakuninsyacode ,syuuryounichiji ,"
                + " endtantousyacode ,bikou1 ,bikou2 ,bikou3 ,bikou4 ,torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + "FROM tmp_sr_dassisayadume "
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
        mapping.put("kojyo","kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno","lotno"); //ﾛｯﾄNo
        mapping.put("edaban","edaban"); //枝番
        mapping.put("kcpno","kcpno"); //KCPNO
        mapping.put("tokuisaki","tokuisaki"); //客先
        mapping.put("lotkubuncode","lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode","ownercode"); //ｵｰﾅｰ
        mapping.put("kosuu","kosuu"); //処理数
        mapping.put("ukeiretannijyuryo","ukeiretannijyuryo"); //単位重量
        mapping.put("ukeiresoujyuryou","ukeiresoujyuryou"); //総重量
        mapping.put("sayadumegouki","sayadumegouki"); //号機
        mapping.put("sayadumehouhou","sayadumehouhou"); //ｻﾔ詰め方法
        mapping.put("sayasyurui","sayasyurui"); //ｻﾔ種類
        mapping.put("konamabushi","konamabushi"); //粉まぶし
        mapping.put("sayatumeryou","sayatumeryou"); //ｻﾔ詰め量
        mapping.put("sayasuu","sayasuu"); //ｻﾔ枚数
        mapping.put("kaishinichiji","kaishinichiji"); //開始日時
        mapping.put("tantousya","tantousya"); //開始担当者
        mapping.put("startkakuninsyacode","startkakuninsyacode"); //開始確認者
        mapping.put("syuuryounichiji","syuuryounichiji"); //終了日時
        mapping.put("endtantousyacode","endtantousyacode"); //終了担当者
        mapping.put("bikou1","bikou1"); //備考1
        mapping.put("bikou2","bikou2"); //備考2
        mapping.put("bikou3","bikou3"); //備考3
        mapping.put("bikou4","bikou4"); //備考4
        mapping.put("torokunichiji","torokunichiji"); //登録日時
        mapping.put("kosinnichiji","kosinnichiji"); //更新日時
        mapping.put("revision","revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrDassisayadume>> beanHandler = new BeanListHandler<>(SrDassisayadume.class, rowProcessor);

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

            // 真空脱脂ｻﾔ詰めデータ取得
            List<SrDassisayadume> srDassisayadumeDataList = getSrDassisayadumeData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srDassisayadumeDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srDassisayadumeDataList.get(0));

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
     * @param srDassisayadumeData 真空脱脂ｻﾔ詰め
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrDassisayadume srDassisayadumeData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srDassisayadumeData != null) {
            // 元データが存在する場合元データより取得
            return getSrDassisayadumeItemData(itemId, srDassisayadumeData);
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
     * 真空脱脂ｻﾔ詰め_仮登録(tmp_sr_dassisayadume)登録処理
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
    private void insertTmpSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_dassisayadume ("
                + "kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,kosuu ,ukeiretannijyuryo ,ukeiresoujyuryou"
                + ",sayadumegouki ,sayadumehouhou ,sayasyurui ,konamabushi ,sayatumeryou ,sayasuu ,kaishinichiji ,tantousya ,startkakuninsyacode ,syuuryounichiji"
                + ",endtantousyacode ,bikou1 ,bikou2 ,bikou3 ,bikou4 ,torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrDassisayadume(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null, jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱脂ｻﾔ詰め_仮登録(tmp_sr_dassisayadume)更新処理
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
    private void updateTmpSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_dassisayadume SET "
                + " kcpno = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,kosuu = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,sayadumegouki = ?,sayadumehouhou = ?,sayasyurui = ?,"
                + " konamabushi = ?,sayatumeryou = ?,sayasuu = ?,kaishinichiji = ?,tantousya = ?,startkakuninsyacode = ?,syuuryounichiji = ?,endtantousyacode = ?,bikou1 = ?,bikou2 = ?,"
                + " bikou3 = ?,bikou4 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrDassisayadume> srSrDassisayadumeList = getSrDassisayadumeData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrDassisayadume srDassisayadume = null;
        if (!srSrDassisayadumeList.isEmpty()) {
            srDassisayadume = srSrDassisayadumeList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrDassisayadume(false, newRev, 0, "", "", "", systemTime, itemList, srDassisayadume, jissekino);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱脂ｻﾔ詰め_仮登録(tmp_sr_dassisayadume)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_dassisayadume "
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
     * 真空脱脂ｻﾔ詰め_仮登録(tmp_sr_dassisayadume)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srDassisayadumeData 真空脱脂ｻﾔ詰めデータ
     * @param jissekino 実績No
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrDassisayadume(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrDassisayadume srDassisayadumeData, int jissekino) {
        List<Object> params = new ArrayList<>();
        if (isInsert) {
            //INSERT時のみマッピングする項目
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        //INSERT・UPDATE共通でマッピングする項目
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KCPNO, srDassisayadumeData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KYAKUSAKI, srDassisayadumeData))); //客先

        //ﾛｯﾄ区分
        String lotKbn = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.LOT_KUBUN, srDassisayadumeData));
        String[] spLotKbn = lotKbn.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spLotKbn[0]));

        //ｵｰﾅｰ
        String owner = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.OWNER, srDassisayadumeData));
        String[] spOwner = owner.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spOwner[0]));

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHORISU, srDassisayadumeData))); //処理数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.TANIJURYO, srDassisayadumeData))); //単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SOJURYO, srDassisayadumeData))); //総重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.GOKI, srDassisayadumeData))); //号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SAYADUME_HOUHOU, srDassisayadumeData))); //ｻﾔ詰め方法

        //ｻﾔ種類
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.SAYASHURUI, srDassisayadumeData))) {
            case "未使用":
                params.add(0);
                break;
            case "使用":
                params.add(1);
                break;
            default:
                params.add(null);
                break;
        }

        //粉まぶし
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.KONAMABUSHI, srDassisayadumeData))) {
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

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SAYADUME_RYOU, srDassisayadumeData))); //ｻﾔ詰め量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SAYAMAISU, srDassisayadumeData))); //ｻﾔ枚数
        //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAISHI_DAY, srDassisayadumeData),
            getItemData(itemList, GXHDO101B050Const.KAISHI_TIME, srDassisayadumeData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAISHI_TANTOUSYA, srDassisayadumeData))); //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAKUNINSYA, srDassisayadumeData))); //開始確認者
        //終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHURYOU_DAY, srDassisayadumeData),
            getItemData(itemList, GXHDO101B050Const.SHURYOU_TIME, srDassisayadumeData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHURYOU_TANTOUSYA, srDassisayadumeData))); //終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO1, srDassisayadumeData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO2, srDassisayadumeData))); //備考2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO3, srDassisayadumeData))); //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO4, srDassisayadumeData))); //備考4

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
     * 真空脱脂ｻﾔ詰め(sr_dassisayadume)登録処理
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
     * @param tmpSrDassisayadume 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino,Timestamp systemTime, List<FXHDD01> itemList, SrDassisayadume tmpSrDassisayadume) throws SQLException {

        String sql = "INSERT INTO sr_dassisayadume ("
                + "kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,kosuu ,ukeiretannijyuryo ,ukeiresoujyuryou ,"
                + " sayadumegouki ,sayadumehouhou ,sayasyurui ,konamabushi ,sayatumeryou ,sayasuu ,kaishinichiji ,tantousya ,startkakuninsyacode ,syuuryounichiji ,"
                + " endtantousyacode ,bikou1 ,bikou2 ,bikou3 ,bikou4 ,torokunichiji ,kosinnichiji ,revision "
                + ") VALUES ("
                + " ? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?) ";

        List<Object> params = setUpdateParameterSrDassisayadume(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrDassisayadume);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱脂ｻﾔ詰め(sr_dassisayadume)更新処理
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
    private void updateSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_dassisayadume SET "
                + " kcpno = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,kosuu = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,sayadumegouki = ?,sayadumehouhou = ?,sayasyurui = ?,"
                + " konamabushi = ?,sayatumeryou = ?,sayasuu = ?,kaishinichiji = ?,tantousya = ?,startkakuninsyacode = ?,syuuryounichiji = ?,endtantousyacode = ?,bikou1 = ?,bikou2 = ?,"
                + " bikou3 = ?,bikou4 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrDassisayadume> srDassisayadumeList = getSrDassisayadumeData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrDassisayadume srDassisayadume = null;
        if (!srDassisayadumeList.isEmpty()) {
            srDassisayadume = srDassisayadumeList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrDassisayadume(false, newRev, "", "", "", jissekino, systemTime, itemList, srDassisayadume);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 真空脱脂ｻﾔ詰め(sr_dassisayadume)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srDassisayadumeData 真空脱脂ｻﾔ詰めデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrDassisayadume(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrDassisayadume srDassisayadumeData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            //INSERT時のみマッピングする項目
            params.add(kojyo);  // 工場ｺｰﾄﾞ
            params.add(lotNo);  // ﾛｯﾄNo
            params.add(edaban); // 枝番
        }
        //INSERT・UPDATE共通でマッピングする項目
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KCPNO, srDassisayadumeData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KYAKUSAKI, srDassisayadumeData))); //客先

        //ﾛｯﾄ区分
        String lotKbn = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.LOT_KUBUN, srDassisayadumeData));
        String[] spLotKbn = lotKbn.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spLotKbn[0]));

        //ｵｰﾅｰ
        String owner = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.OWNER, srDassisayadumeData));
        String[] spOwner = owner.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spOwner[0]));

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHORISU, srDassisayadumeData))); //処理数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.TANIJURYO, srDassisayadumeData))); //単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SOJURYO, srDassisayadumeData))); //総重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.GOKI, srDassisayadumeData))); //号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SAYADUME_HOUHOU, srDassisayadumeData))); //ｻﾔ詰め方法

        //ｻﾔ種類
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.SAYASHURUI, srDassisayadumeData))) {
            case "未使用":
                params.add(0);
                break;
            case "使用":
                params.add(1);
                break;
            default:
                params.add(null);
                break;
        }

        //粉まぶし
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B050Const.KONAMABUSHI, srDassisayadumeData))) {
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

        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SAYADUME_RYOU, srDassisayadumeData))); //ｻﾔ詰め量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SAYAMAISU, srDassisayadumeData))); //ｻﾔ枚数
        //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAISHI_DAY, srDassisayadumeData),
            getItemData(itemList, GXHDO101B050Const.KAISHI_TIME, srDassisayadumeData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAISHI_TANTOUSYA, srDassisayadumeData))); //開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.KAKUNINSYA, srDassisayadumeData))); //開始確認者
        //終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHURYOU_DAY, srDassisayadumeData),
            getItemData(itemList, GXHDO101B050Const.SHURYOU_TIME, srDassisayadumeData)));
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.SHURYOU_TANTOUSYA, srDassisayadumeData))); //終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO1, srDassisayadumeData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO2, srDassisayadumeData))); //備考2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO3, srDassisayadumeData))); //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B050Const.BIKO4, srDassisayadumeData))); //備考4

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
     * 真空脱脂ｻﾔ詰め(sr_dassisayadume)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_dassisayadume "
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
     * [真空脱脂ｻﾔ詰め_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_dassisayadume "
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B050Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B050Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B050Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B050Const.SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * ｻﾔ詰め計算
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData calcSayaDume(ProcessData processDate) {

         // 継続メソッドのクリア
        processDate.setMethod("");

        try {
            FXHDD01 itemSayadumeRyou = getItemRow(processDate.getItemList(), GXHDO101B050Const.SAYADUME_RYOU);
            // ｻﾔ詰め量が、既に入力されている場合はリターン
            if (!StringUtil.isEmpty(itemSayadumeRyou.getValue())) {
                return processDate;
            }

            String syorisu = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B050Const.SHORISU, null)); //処理数
            String sayaMaisu = StringUtil.nullToBlank(getItemData(processDate.getItemList(), GXHDO101B050Const.SAYAMAISU, null)); //ｻﾔ枚数
            // 処理数・ｻﾔ枚数のいづれかが、入力されていない場合はリターン
            if (StringUtil.isEmpty(syorisu) || StringUtil.isEmpty(sayaMaisu)) {
                return processDate;
            }

            // 処理数を数値変換
            BigDecimal decSyorisu = new BigDecimal(syorisu);
            // ｻﾔ枚数を数値変換
            BigDecimal decSayaMaisu = new BigDecimal(sayaMaisu);
            if(BigDecimal.ZERO.compareTo(decSayaMaisu) == 0){
                return processDate;
            }

            // ｻﾔ詰め量算出(小数以下四捨五入)
            BigDecimal decSayadumeRyou = decSyorisu.divide(decSayaMaisu, 0, RoundingMode.HALF_UP);
            // 結果を項目にセット
            itemSayadumeRyou.setValue(decSayadumeRyou.toPlainString());

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
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srDassisayadumeData 真空脱脂ｻﾔ詰めデータ
     * @return DB値
     */
    private String getSrDassisayadumeItemData(String itemId, SrDassisayadume srDassisayadumeData) {
        switch (itemId) {
            // 処理数
            case GXHDO101B050Const.SHORISU:
                return StringUtil.nullToBlank(srDassisayadumeData.getKosuu());
            // 単位重量
            case GXHDO101B050Const.TANIJURYO:
                return StringUtil.nullToBlank(srDassisayadumeData.getUkeiretannijyuryo());
            // 総重量
            case GXHDO101B050Const.SOJURYO:
                return StringUtil.nullToBlank(srDassisayadumeData.getUkeiresoujyuryou());
            // 号機
            case GXHDO101B050Const.GOKI:
                return StringUtil.nullToBlank(srDassisayadumeData.getSayadumegouki());
            // ｻﾔ詰め方法
            case GXHDO101B050Const.SAYADUME_HOUHOU:
                return StringUtil.nullToBlank(srDassisayadumeData.getSayadumehouhou());
            // ｻﾔ種類
            case GXHDO101B050Const.SAYASHURUI:
                switch (StringUtil.nullToBlank(srDassisayadumeData.getSayasyurui())) {
                    case "0":
                        return "未使用";
                    case "1":
                        return "使用";
                    default:
                        return "";
                }
            // 粉まぶし
            case GXHDO101B050Const.KONAMABUSHI:
                switch (StringUtil.nullToBlank(srDassisayadumeData.getKonamabushi())) {
                    case "0":
                        return "なし";
                    case "1":
                        return "あり";
                    default:
                        return "";
                }
            // ｻﾔ詰め量
            case GXHDO101B050Const.SAYADUME_RYOU:
                return StringUtil.nullToBlank(srDassisayadumeData.getSayatumeryou());
            // ｻﾔ枚数
            case GXHDO101B050Const.SAYAMAISU:
                return StringUtil.nullToBlank(srDassisayadumeData.getSayasuu());
            // 開始日
            case GXHDO101B050Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srDassisayadumeData.getKaishinichiji(), "yyMMdd");
            // 開始時間
            case GXHDO101B050Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srDassisayadumeData.getKaishinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B050Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srDassisayadumeData.getTantousya());
            // 開始確認者
            case GXHDO101B050Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srDassisayadumeData.getStartkakuninsyacode());
            // 終了日
            case GXHDO101B050Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srDassisayadumeData.getSyuuryounichiji(), "yyMMdd");
            // 終了時間
            case GXHDO101B050Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srDassisayadumeData.getSyuuryounichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B050Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srDassisayadumeData.getEndtantousyacode());
            // 備考1
            case GXHDO101B050Const.BIKO1:
                return StringUtil.nullToBlank(srDassisayadumeData.getBikou1());
            // 備考2
            case GXHDO101B050Const.BIKO2:
                return StringUtil.nullToBlank(srDassisayadumeData.getBikou2());
            // 備考3
            case GXHDO101B050Const.BIKO3:
                return StringUtil.nullToBlank(srDassisayadumeData.getBikou3());
            // 備考4
            case GXHDO101B050Const.BIKO4:
                return StringUtil.nullToBlank(srDassisayadumeData.getBikou4());
            default:
                return null;
        }
    }

    /**
     * 真空脱脂ｻﾔ詰め_仮登録(tmp_sr_dassisayadume)登録処理(削除時)
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
    private void insertDeleteDataTmpSrDassisayadume(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_dassisayadume ("
                +  " kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,kosuu ,ukeiretannijyuryo ,ukeiresoujyuryou ,"
                +  " sayadumegouki ,sayadumehouhou ,sayasyurui ,konamabushi ,sayatumeryou ,sayasuu ,kaishinichiji ,tantousya ,startkakuninsyacode ,syuuryounichiji ,"
                +  " endtantousyacode ,bikou1 ,bikou2 ,bikou3 ,bikou4 ,torokunichiji ,kosinnichiji ,revision ,deleteflag "
                + ") SELECT "
                +  " kojyo ,lotno ,edaban ,kcpno ,tokuisaki ,lotkubuncode ,ownercode ,kosuu ,ukeiretannijyuryo ,ukeiresoujyuryou ,"
                +  " sayadumegouki ,sayadumehouhou ,sayasyurui ,konamabushi ,sayatumeryou ,sayasuu ,kaishinichiji ,tantousya ,startkakuninsyacode ,syuuryounichiji ,"
                +  " endtantousyacode ,bikou1 ,bikou2 ,bikou3 ,bikou4 ,? ,? ,? ,? "
                + " FROM sr_dassisayadume "
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
     * @param queryRunnerDoc オブジェクト
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
             String sql = "SELECT data "
                        + " FROM fxhbm03 "
                        + " WHERE user_name = 'common_user' AND key = '焼成真空脱脂ｻﾔ詰め_前工程' ";
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

    /**
     * 単位重量取得処理
     * @param queryRunnerDoc queryRunner(Doc)オブジェクト
     * @param queryRunnerWip queryRunner(Wip)オブジェクト
     * @param lotNo ﾛｯﾄNo
     * @return 処理
     */
    private String getTanijuryo(QueryRunner queryRunnerDoc, QueryRunner queryRunnerWip, String lotNo) throws SQLException{
        // 単位重量の取得
        String tanijuryo = null;

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        if (shikakariData != null && !shikakariData.isEmpty()){
            tanijuryo = StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo")); //単位重量
        }
        return tanijuryo;
    }

    /**
     * 総重量計算・取得処理
     * @param queryRunnerDoc queryRunner(Doc)オブジェクト
     * @param queryRunnerWip queryRunner(Wip)オブジェクト
     * @param lotNo ﾛｯﾄNo
     * @return 処理
     */
    private String getSojuryo(String strShorisu, String strTanijuryo) throws SQLException{
        // 総重量の取得
        String sojuryo = null;

        try {
            // 処理数の数値変換(数値以外、0以下の場合、Nullを返して処理終了)
            if (StringUtil.isEmpty(strShorisu)){
                return sojuryo;
            }
            BigDecimal decSyorisu = new BigDecimal(strShorisu);
            if(BigDecimal.ZERO.compareTo(decSyorisu) >= 0){
                return sojuryo;
            }
            
            // 単位重量の数値変換(数値以外、0以下の場合、Nullを返して処理終了)
            if (StringUtil.isEmpty(strTanijuryo)){
                return sojuryo;
            }
            BigDecimal decTanijuryo = new BigDecimal(strTanijuryo);
            if(BigDecimal.ZERO.compareTo(decTanijuryo) >= 0){
                return sojuryo;
            }

            //「処理数」　÷　100　×　「単位重量」 → 式を変換して先に「単位重量」を乗算
            BigDecimal calcResult = decSyorisu.multiply(decTanijuryo).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            sojuryo = calcResult.toPlainString();
        } catch (NumberFormatException e) {
            //数値型変換失敗時はそのままリターン
        }
        return sojuryo;
    }

}
