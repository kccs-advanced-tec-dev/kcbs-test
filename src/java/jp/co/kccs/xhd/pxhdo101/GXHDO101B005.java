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
import java.util.LinkedHashMap;
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
import jp.co.kccs.xhd.db.model.SrRsussek;
import jp.co.kccs.xhd.db.model.SubSrRsussek;
import jp.co.kccs.xhd.model.GXHDO101C006Model;
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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/03/08<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B005(積層・RSUS)ロジック
 *
 * @author KCSS K.Jo
 * @since  2019/03/08
 */
public class GXHDO101B005 implements IFormLogic {

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

            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_TOP,
                    GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_TOP,
                    GXHDO101B005Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B005Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_BOTTOM,
                    GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_BOTTOM,
                    GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B005Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B005Const.BTN_INSERT_TOP,
                    GXHDO101B005Const.BTN_DELETE_TOP,
                    GXHDO101B005Const.BTN_UPDATE_TOP,
                    GXHDO101B005Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B005Const.BTN_INSERT_BOTTOM,
                    GXHDO101B005Const.BTN_DELETE_BOTTOM,
                    GXHDO101B005Const.BTN_UPDATE_BOTTOM));

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
     * 剥離内容入力画面(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openHakuriInput(ProcessData processData) {

        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c006");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
            beanGXHDO101C006.setGxhdO101c006ModelView(beanGXHDO101C006.getGxhdO101c006Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;

        }
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempRegist(ProcessData processData) {
        
        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemTempRegist(processData);
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
        processData.setMethod("doTempRegist");

        return processData;

    }

    /**
     * 仮登録項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemTempRegist(ProcessData processData) {
        
        //端子テープ種類
        FXHDD01 itemTtapeSyurui = getItemRow(processData.getItemList(), GXHDO101B005Const.TANSHI_TAPE_SHURUI);
        if ("NG".equals(itemTtapeSyurui.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTtapeSyurui);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemTtapeSyurui.getLabel1());
        }

        return null;
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

                // 積層・RSUS_仮登録登録処理
                insertTmpSrRsussek(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 積層・RSUS_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrRsussek(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 積層・RSUS_仮登録更新処理
                updateTmpSrRsussek(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 積層・RSUS_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrRsussek(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
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

        //下端子ﾌﾞｸ抜き
        FXHDD01 itemShitaTanshiBukunuki = getItemRow(processData.getItemList(), GXHDO101B005Const.SHITA_TANSHI_BUKUNUKI);
        if (!"実施".equals(itemShitaTanshiBukunuki.getValue()) && !"未実施".equals(itemShitaTanshiBukunuki.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShitaTanshiBukunuki);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShitaTanshiBukunuki.getLabel1());
        }

        //端子テープ種類
        FXHDD01 itemTtapeSyurui = getItemRow(processData.getItemList(), GXHDO101B005Const.TANSHI_TAPE_SHURUI);
        if ("NG".equals(itemTtapeSyurui.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTtapeSyurui);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemTtapeSyurui.getLabel1());
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaishiDay = getItemRow(processData.getItemList(), GXHDO101B005Const.KAISHI_DAY); //開始日
        FXHDD01 itemKaishiTime = getItemRow(processData.getItemList(), GXHDO101B005Const.KAISHI_TIME); // 開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemKaishiDay.getValue(), itemKaishiTime.getValue());
        FXHDD01 itemShuryouDay = getItemRow(processData.getItemList(), GXHDO101B005Const.SHURYOU_DAY); //終了日
        FXHDD01 itemShuryouTime = getItemRow(processData.getItemList(), GXHDO101B005Const.SHURYOU_TIME); //終了時刻
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
     * 剥離内容入力画面チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormHakuriInput() {
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
        return GXHDO101C006Logic.checkInput(beanGXHDO101C006.getGxhdO101c006Model());
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
            SrRsussek tmpSrRsussek = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrRsussek> srSrRsussekList = getSrRsussekData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srSrRsussekList.isEmpty()) {
                    tmpSrRsussek = srSrRsussekList.get(0);
                }
                
                deleteTmpSrRsussek(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrRsussek(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 積層・RSUS_登録処理
            insertSrRsussek(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrRsussek);

            // 積層・RSUS_ｻﾌﾞ画面登録処理
            insertSubSrRsussek(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

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
        processData.setUserAuthParam(GXHDO101B005Const.USER_AUTH_UPDATE_PARAM);

        // 後続処理メソッド設定
        processData.setMethod("doCorrect");

        return processData;
    }
        
    /**
     * 剥離/画処NG計算処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkHakuriGNGKeisan(ProcessData processData) {
        Boolean checkHngFlag = false;
        Boolean checkGngFlag = false;
        //・剥離NG回数
        FXHDD01 itemHngKaisuu = getItemRow(processData.getItemList(), GXHDO101B005Const.HAKURI_NG_KAISU);
        //・剥離NG_AVE
        FXHDD01 itemHngAVE = getItemRow(processData.getItemList(), GXHDO101B005Const.HAKURI_NG_AVE);
        //・画処NG回数
        FXHDD01 itemGngKkaisuu = getItemRow(processData.getItemList(), GXHDO101B005Const.GASHO_NG_KAISU);
        //・画処NG_AVE
        FXHDD01 itemGngKaisuuAVE = getItemRow(processData.getItemList(), GXHDO101B005Const.GASHO_NG_AVE);
        //1.以下の項目のいずれか、もしくは両方が入力されているかﾁｪｯｸを行う。
        //  ・剥離NG回数
        //  ・剥離NG_AVE
        // A.入力されている場合、上記項目は以降の処理で値を設定する対象項目とはしない。
        //  ※両方入力されていない場合、値を設定する対象項目とする。        
        if (("".equals(itemHngKaisuu.getValue()) && "".equals(itemHngAVE.getValue())) || 
            (itemHngKaisuu.getValue() == null && itemHngAVE.getValue() == null)) {
            checkHngFlag = true;
        }
        //2.以下の項目のいずれか、もしくは両方が入力されているかﾁｪｯｸを行う。
        //  ・画処NG回数
        //  ・画処NG_AVE
        // A.入力されている場合、上記項目は以降の処理で値を設定する対象項目とはしない。
        //  ※両方入力されていない場合、値を設定する対象項目とする。        
        if (("".equals(itemGngKkaisuu.getValue()) && "".equals(itemGngKaisuuAVE.getValue())) || 
            (itemGngKkaisuu.getValue() == null && itemGngKaisuuAVE.getValue() == null)) {
            checkGngFlag = true;
        }

        //3.上記1、2の両方で値を設定する対象項目としない結果となった場合、以降の処理は実行しない。
        if (!checkHngFlag && !checkGngFlag){
            // 後続処理メソッド設定
            processData.setMethod("");
            return processData;
        }
    
        QueryRunner queryRunnerSpskadoritu = new QueryRunner(processData.getDataSourceSpskadoritu());
        QueryRunner queryRunnerTtpkadoritu = new QueryRunner(processData.getDataSourceTtpkadoritu());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));

        // 変数SUM1
        int sumHensuu1 = 0;
        // 変数SUM2
        int sumHensuu2 = 0;
        // 画面.セット数
        BigDecimal setSuu = BigDecimal.ZERO;        
        BigDecimal valueAVE1= BigDecimal.ZERO;
        BigDecimal valueAVE2= BigDecimal.ZERO;
        Boolean gngSetFlag = false;
        FXHDD01 itemSetSuu = getItemRow(processData.getItemList(), GXHDO101B005Const.SET_SUU);
        setSuu = new BigDecimal(itemSetSuu.getValue());
        // 「画処NG_AVE」項目情報を取得
        FXHDD01 itemGNGKaisuuAve = getItemRow(processData.getItemList(),GXHDO101B005Const.GASHO_NG_AVE);
        // 「画処NG_AVE」小数桁を取得
        int gngKaisuuAveDecLength = Integer.parseInt(itemGNGKaisuuAve.getInputLengthDec());

        // 「剥離NG_AVE」項目情報を取得
        FXHDD01 itemHNGKaisuuAve = getItemRow(processData.getItemList(),GXHDO101B005Const.HAKURI_NG_AVE);
        // 「剥離NG_AVE」小数桁を取得
        int hngKaisuuAveDecLength = Integer.parseInt(itemHNGKaisuuAve.getInputLengthDec());
        
        try {
            // (19)[積層履歴ﾃﾞｰﾀ]から、ﾃﾞｰﾀを取得
            Map sekisouRirekiData = this.loadSekisouRirekiData(queryRunnerSpskadoritu, lotNo);
            if (sekisouRirekiData == null || sekisouRirekiData.isEmpty() || 
                    (sekisouRirekiData.get("HakuriErrSuu") == null && sekisouRirekiData.get("CcdErrSuu") == null) ) {
                // A.取得出来なかった場合 ｱ.Ⅲ.画面表示仕様(20)を発行する。
                // (20)[剥離NG履歴]から、ﾃﾞｰﾀを取得
                Map hakuringrirekiData = this.loadHakuringrirekiData(queryRunnerTtpkadoritu, lotNo);
                if (hakuringrirekiData == null || hakuringrirekiData.isEmpty() || 
                    (hakuringrirekiData.get("SekisouSuu") == null)) {
                    // α.取得出来なかった場合ｴﾗｰﾒｯｾｰｼﾞを表示し、以降の処理は実行しない。
                    // ・ｴﾗｰｺｰﾄﾞ:XHD-000036
                    // ・ｴﾗ-ﾒｯｾｰｼﾞ:剥離/画処NGﾃﾞｰﾀが取得出来ません。
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageUtil.getMessage("XHD-000036"), null);
                    facesContext.addMessage(null, message);
                    processData.setMethod("");
                    return processData;
                }else{
                    // β.取得出来た場合
                    // a.取得した積層数を合計する。この値を「変数SUM1」とする
                    // b. a ÷ 画面.セット数の計算を行う。この値を「変数AVE1」とする
                    // a.変数SUM1
                    BigDecimal sumSekisouSuu= BigDecimal.ZERO;

                    for (Object key : hakuringrirekiData.keySet()) {
                        sumSekisouSuu = new BigDecimal(String.valueOf(hakuringrirekiData.get(key)));
                    }

                    if (!setSuu.equals(BigDecimal.ZERO)){
                        valueAVE1 = sumSekisouSuu.divide(setSuu, hngKaisuuAveDecLength ,BigDecimal.ROUND_DOWN);
                    }
                    sumHensuu1 = sumSekisouSuu.intValue();
                }
            }else{
                // B.取得出来た場合
                // ｱ.取得した剥離ｴﾗｰ数を合計する。この値を「変数SUM1」とする。
                // ｲ. ｱ ÷ 画面.セット数の計算を行う。この値を「変数AVE1」とする。

                // 剥離ｴﾗｰ数
                BigDecimal sumHakuriErrSuu= BigDecimal.ZERO;
                // 画処ｴﾗｰ数
                BigDecimal sumCcdErrSuu= BigDecimal.ZERO;

                for (Object key : sekisouRirekiData.keySet()) {
                    if("HakuriErrSuu".equals(key)){
                        sumHakuriErrSuu = new BigDecimal(String.valueOf(sekisouRirekiData.get(key)));
                    }
                    if("CcdErrSuu".equals(key)){
                        sumCcdErrSuu = new BigDecimal(String.valueOf(sekisouRirekiData.get(key)));
                    }
                }
                if (!setSuu.equals(BigDecimal.ZERO)){
                    valueAVE1 = sumHakuriErrSuu.divide(setSuu, hngKaisuuAveDecLength ,BigDecimal.ROUND_DOWN);
                }
                sumHensuu1 = sumHakuriErrSuu.intValue();

                // ｳ.取得した画処ｴﾗｰ数を合計する。この値を「変数SUM2」とする。
                // ｴ. ｳ ÷ 画面.セット数の計算を行う。この値を「変数AVE2」とする。
                if (!setSuu.equals(BigDecimal.ZERO)){
                    valueAVE2 = sumCcdErrSuu.divide(setSuu, gngKaisuuAveDecLength ,BigDecimal.ROUND_DOWN);
                }
                sumHensuu2 = sumCcdErrSuu.intValue();
                gngSetFlag = true;
            }
            
            if (checkHngFlag){
                //・剥離NG回数
                setItemData(processData,GXHDO101B005Const.HAKURI_NG_KAISU, String.valueOf(sumHensuu1));
                //・剥離NG_AVE
                setItemData(processData,GXHDO101B005Const.HAKURI_NG_AVE, String.valueOf(valueAVE1));
            }
            
            if (checkGngFlag && gngSetFlag){
                //・画処NG回数
                setItemData(processData,GXHDO101B005Const.GASHO_NG_KAISU,String.valueOf(sumHensuu2));
                //・画処NG_AVE
                setItemData(processData,GXHDO101B005Const.GASHO_NG_AVE,String.valueOf(valueAVE2));
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        // 後続処理メソッド設定
        processData.setMethod("");
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

            // 積層・RSUS_更新処理
            updateSrRsussek(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 積層・RSUS_ｻﾌﾞ画面更新処理
            updateSubSrRsussek(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

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
        processData.setUserAuthParam(GXHDO101B005Const.USER_AUTH_DELETE_PARAM);

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

            // 積層・RSUS_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrRsussek(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 積層・RSUS_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrRsussek(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 積層・RSUS_削除処理
            deleteSrRsussek(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 積層・RSUS_ｻﾌﾞ画面削除処理
            deleteSubSrRsussek(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO101B005Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_BOTTOM,
                        GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_BOTTOM,
                        GXHDO101B005Const.BTN_DELETE_BOTTOM,
                        GXHDO101B005Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_TOP,
                        GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_TOP,
                        GXHDO101B005Const.BTN_DELETE_TOP,
                        GXHDO101B005Const.BTN_UPDATE_TOP,
                        GXHDO101B005Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_ENDDATETIME_TOP
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B005Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B005Const.BTN_INSERT_BOTTOM,
                        GXHDO101B005Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B005Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B005Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B005Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_BOTTOM,
                        GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_BOTTOM,
                        GXHDO101B005Const.BTN_INSERT_BOTTOM,
                        GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B005Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_TOP,
                        GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_TOP,
                        GXHDO101B005Const.BTN_INSERT_TOP,
                        GXHDO101B005Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_ENDDATETIME_TOP
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B005Const.BTN_DELETE_BOTTOM,
                        GXHDO101B005Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B005Const.BTN_DELETE_TOP,
                        GXHDO101B005Const.BTN_UPDATE_TOP));

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
            // 剥離内容入力
            case GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_TOP:
            case GXHDO101B005Const.BTN_HAKURI_NAIYOU_NYURYOKU_BOTTOM:
                method = "openHakuriInput";
                break;
            // 剥離/画処NG計算
            case GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_TOP:
            case GXHDO101B005Const.BTN_HAKURI_GASHO_NG_KEISAN_BOTTOM:
                method = "checkHakuriGNGKeisan";
                break;
            // 仮登録
            case GXHDO101B005Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B005Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B005Const.BTN_INSERT_TOP:
            case GXHDO101B005Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B005Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B005Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B005Const.BTN_UPDATE_TOP:
            case GXHDO101B005Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B005Const.BTN_DELETE_TOP:
            case GXHDO101B005Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B005Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM:
                method = "setKaishiDateTime";
                break;
            // 終了日時
            case GXHDO101B005Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM:
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
    private ProcessData setInitDate(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String sLotNo = (String) session.getAttribute("sLotNo");

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

        // 製版ﾏｽﾀ情報取得
        String pattern = StringUtil.nullToBlank(sekkeiData.get("PATTERN")); //電極製版名 
        Map daPatternMasData = loadDaPatternMas(queryRunnerQcdb, pattern);
        if (daPatternMasData == null || daPatternMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000034"));
        }

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
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo, sLotNo);

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
     * @param sLotNo 先行LotNo
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData, Map shikakariData, String lotNo, String sLotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B005Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B005Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B005Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B005Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B005Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B005Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B005Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B005Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B005Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B005Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B005Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B005Const.UE_COVER_TAPE1, 
                StringUtil.nullToBlank(sekkeiData.get("SYURUI2"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI2"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU2"))
                + "枚"
        );

        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B005Const.SHITA_COVER_TAPE1, 
                StringUtil.nullToBlank(sekkeiData.get("SYURUI3"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI3"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU3"))
                + "枚");

        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
        this.setItemData(processData, GXHDO101B005Const.RETSU_GYOU, lRetsu + "×" + wRetsu);

        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B005Const.PITCH, lSun + "×" + wSun);

        // 電極ペースト
        this.setItemData(processData, GXHDO101B005Const.DENKYOKU_PASTE, "");
        
        // 積層スライド量
        this.setItemData(processData, GXHDO101B005Const.SEKISOU_SLIDE_RYOU,  StringUtil.nullToBlank(sekkeiData.get("ABSLIDE")));

        // 最上層スライド量
        this.setItemData(processData, GXHDO101B005Const.LAST_LAYER_SLIDE_RYO, StringUtil.nullToBlank(sekkeiData.get("LASTLAYERSLIDERYO")));
        
        // 先行ロットNo
        if ("".equals(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())){
            this.setItemData(processData, GXHDO101B005Const.SENKOU_LOT_NO, sLotNo);
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
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId) throws SQLException {

        List<SrRsussek> srSrRsussekDataList = new ArrayList<>();
        List<SubSrRsussek> subSubSrRsussekDataList = new ArrayList<>();
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

                // サブ画面データ設定
                // 膜厚入力画面データ設定
                setInputItemDataSubFormC006(null);

                return true;
            }

            // 積層・RSUSデータ取得
            srSrRsussekDataList = getSrRsussekData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srSrRsussekDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 積層・RSUS_ｻﾌﾞ画面データ取得
            subSubSrRsussekDataList = getSubSrRsussekData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (subSubSrRsussekDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSrRsussekDataList.isEmpty() || subSubSrRsussekDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSrRsussekDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC006(subSubSrRsussekDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSrRsussekData 積層RSUSデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrRsussek srSrRsussekData) {
        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B005Const.SLIP_LOTNO, getSrRsussekItemData(GXHDO101B005Const.SLIP_LOTNO, srSrRsussekData));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B005Const.ROLL_NO1, getSrRsussekItemData(GXHDO101B005Const.ROLL_NO1, srSrRsussekData));
        // ﾛｰﾙNo2
        this.setItemData(processData, GXHDO101B005Const.ROLL_NO2, getSrRsussekItemData(GXHDO101B005Const.ROLL_NO2, srSrRsussekData));
        // ﾛｰﾙNo3
        this.setItemData(processData, GXHDO101B005Const.ROLL_NO3, getSrRsussekItemData(GXHDO101B005Const.ROLL_NO3, srSrRsussekData));
        // 原料記号
        this.setItemData(processData, GXHDO101B005Const.GENRYO_KIGOU, getSrRsussekItemData(GXHDO101B005Const.GENRYO_KIGOU, srSrRsussekData));
        // ＰＥＴフィルム種類
        this.setItemData(processData, GXHDO101B005Const.PET_FILM_SHURUI, getSrRsussekItemData(GXHDO101B005Const.PET_FILM_SHURUI, srSrRsussekData));
        // 固着シート貼付り機
        this.setItemData(processData, GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI, getSrRsussekItemData(GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI, srSrRsussekData));
        // 固着シート
        this.setItemData(processData, GXHDO101B005Const.KOTYAKU_SHEET, getSrRsussekItemData(GXHDO101B005Const.KOTYAKU_SHEET, srSrRsussekData));
        // 下端子号機
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_GOUKI, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_GOUKI, srSrRsussekData));
        // 上端子号機
        this.setItemData(processData, GXHDO101B005Const.UE_TANSHI_GOUKI, getSrRsussekItemData(GXHDO101B005Const.UE_TANSHI_GOUKI, srSrRsussekData));
        // 下端子ﾌﾞｸ抜き
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_BUKUNUKI, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_BUKUNUKI, srSrRsussekData));
        // 積層号機
        this.setItemData(processData, GXHDO101B005Const.SEKISOU_GOKI, getSrRsussekItemData(GXHDO101B005Const.SEKISOU_GOKI, srSrRsussekData));
        // 下端子
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI, srSrRsussekData));
        // 上端子
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI, srSrRsussekData));
        // 処理セット数
        this.setItemData(processData, GXHDO101B005Const.SYORI_SET_SU, getSrRsussekItemData(GXHDO101B005Const.SYORI_SET_SU, srSrRsussekData));
        // 良品セット数
        this.setItemData(processData, GXHDO101B005Const.RYOUHIN_SET_SU, getSrRsussekItemData(GXHDO101B005Const.RYOUHIN_SET_SU, srSrRsussekData));
        // 外観確認1
        this.setItemData(processData, GXHDO101B005Const.GAIKAN_KAKUNIN1, getSrRsussekItemData(GXHDO101B005Const.GAIKAN_KAKUNIN1, srSrRsussekData));
        // 外観確認2
        this.setItemData(processData, GXHDO101B005Const.GAIKAN_KAKUNIN2, getSrRsussekItemData(GXHDO101B005Const.GAIKAN_KAKUNIN2, srSrRsussekData));
        // 外観確認3
        this.setItemData(processData, GXHDO101B005Const.GAIKAN_KAKUNIN3, getSrRsussekItemData(GXHDO101B005Const.GAIKAN_KAKUNIN3, srSrRsussekData));
        // 外観確認4
        this.setItemData(processData, GXHDO101B005Const.GAIKAN_KAKUNIN4, getSrRsussekItemData(GXHDO101B005Const.GAIKAN_KAKUNIN4, srSrRsussekData));
        // 開始日
        this.setItemData(processData, GXHDO101B005Const.KAISHI_DAY, getSrRsussekItemData(GXHDO101B005Const.KAISHI_DAY, srSrRsussekData));
        // 開始時刻
        this.setItemData(processData, GXHDO101B005Const.KAISHI_TIME, getSrRsussekItemData(GXHDO101B005Const.KAISHI_TIME, srSrRsussekData));
        // 開始担当者
        this.setItemData(processData, GXHDO101B005Const.KAISHI_TANTOUSHA, getSrRsussekItemData(GXHDO101B005Const.KAISHI_TANTOUSHA, srSrRsussekData));
        // 終了日
        this.setItemData(processData, GXHDO101B005Const.SHURYOU_DAY, getSrRsussekItemData(GXHDO101B005Const.SHURYOU_DAY, srSrRsussekData));
        // 終了時刻
        this.setItemData(processData, GXHDO101B005Const.SHURYOU_TIME, getSrRsussekItemData(GXHDO101B005Const.SHURYOU_TIME, srSrRsussekData));
        // 終了担当者
        this.setItemData(processData, GXHDO101B005Const.SHURYOU_TANTOUSHA, getSrRsussekItemData(GXHDO101B005Const.SHURYOU_TANTOUSHA, srSrRsussekData));
        // 端子テープ種類
        this.setItemData(processData, GXHDO101B005Const.TANSHI_TAPE_SHURUI, getSrRsussekItemData(GXHDO101B005Const.TANSHI_TAPE_SHURUI, srSrRsussekData));
        // 瞬時加熱時間
        this.setItemData(processData, GXHDO101B005Const.SHUNJI_KANETSU_TIME, getSrRsussekItemData(GXHDO101B005Const.SHUNJI_KANETSU_TIME, srSrRsussekData));
        // タクト
        this.setItemData(processData, GXHDO101B005Const.TAKT, getSrRsussekItemData(GXHDO101B005Const.TAKT, srSrRsussekData));
        // 先行ロットNo
        this.setItemData(processData, GXHDO101B005Const.SENKOU_LOT_NO, getSrRsussekItemData(GXHDO101B005Const.SENKOU_LOT_NO, srSrRsussekData));
        // 剥離NG回数
        this.setItemData(processData, GXHDO101B005Const.HAKURI_NG_KAISU, getSrRsussekItemData(GXHDO101B005Const.HAKURI_NG_KAISU, srSrRsussekData));
        // 剥離NG_AVE
        this.setItemData(processData, GXHDO101B005Const.HAKURI_NG_AVE, getSrRsussekItemData(GXHDO101B005Const.HAKURI_NG_AVE, srSrRsussekData));
        // 画処NG回数
        this.setItemData(processData, GXHDO101B005Const.GASHO_NG_KAISU, getSrRsussekItemData(GXHDO101B005Const.GASHO_NG_KAISU, srSrRsussekData));
        // 画処NG_AVE
        this.setItemData(processData, GXHDO101B005Const.GASHO_NG_AVE, getSrRsussekItemData(GXHDO101B005Const.GASHO_NG_AVE, srSrRsussekData));
        // 備考1
        this.setItemData(processData, GXHDO101B005Const.BIKOU1, getSrRsussekItemData(GXHDO101B005Const.BIKOU1, srSrRsussekData));
        // 備考2
        this.setItemData(processData, GXHDO101B005Const.BIKOU2, getSrRsussekItemData(GXHDO101B005Const.BIKOU2, srSrRsussekData));

    }

    /**
     * 剥離内容入力画面データ設定処理
     *
     * @param subSrRsussekData 積層・RSUS_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
private void setInputItemDataSubFormC006(SubSrRsussek subSrRsussekData) {

        // サブ画面の情報を取得
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);

        //データの設定
        String[] setsuu;
        String[] bikou;

        GXHDO101C006Model model;
        if (subSrRsussekData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            setsuu = new String[]{"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""}; //ｾｯﾄ数入力1～40
            bikou = new String[]{"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""
                                 ,"", "", "", "", "", "", "", "", "", ""}; //備考1～40
            model = GXHDO101C006Logic.createGXHDO101C006Model(setsuu, bikou);

        } else {
            // 登録データがあれば登録データをセットする。
            //ｾｯﾄ数入力1～40
            setsuu = new String[]{
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu1()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu2()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu3()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu4()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu5()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu6()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu7()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu8()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu9()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu10()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu11()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu12()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu13()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu14()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu15()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu16()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu17()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu18()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu19()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu20()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu21()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu22()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu23()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu24()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu25()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu26()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu27()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu28()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu29()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu30()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu31()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu32()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu33()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu34()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu35()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu36()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu37()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu38()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu39()),
                StringUtil.nullToBlank(subSrRsussekData.getSetsuu40())};
            //備考1～40
            bikou = new String[]{
                StringUtil.nullToBlank(subSrRsussekData.getBikou1()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou2()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou3()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou4()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou5()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou6()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou7()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou8()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou9()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou10()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou11()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou12()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou13()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou14()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou15()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou16()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou17()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou18()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou19()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou20()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou21()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou22()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou23()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou24()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou25()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou26()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou27()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou28()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou29()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou30()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou31()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou32()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou33()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou34()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou35()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou36()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou37()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou38()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou39()),
                StringUtil.nullToBlank(subSrRsussekData.getBikou40())};
            model = GXHDO101C006Logic.createGXHDO101C006Model(setsuu, bikou);
        }

        beanGXHDO101C006.setGxhdO101c006Model(model);
    }

    /**
     * 積層・RSUSの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 積層・RSUS登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrRsussek> getSrRsussekData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrRsussek(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrRsussek(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 積層・RSUS_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRsussek> getSubSrRsussekData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrRsussek(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrRsussek(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [積層履歴ﾃﾞｰﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerSpskadoritu QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadSekisouRirekiData(QueryRunner queryRunnerSpskadoritu, String lotNo) throws SQLException {
        // 積層履歴ﾃﾞｰﾀの取得
        String sql = "SELECT sum(HakuriErrSuu) AS HakuriErrSuu,sum(CcdErrSuu) AS CcdErrSuu "
                + "FROM sekisourirekidata "
                + "WHERE LotNo = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerSpskadoritu.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [剥離NG履歴]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerTtpkadoritu QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadHakuringrirekiData(QueryRunner queryRunnerTtpkadoritu, String lotNo) throws SQLException {
        // 剥離NG履歴ﾃﾞｰﾀの取得
        String sql = "SELECT sum(SekisouSuu) AS SekisouSuu "
                + "FROM hakuringrireki "
                + "WHERE LotNo = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerTtpkadoritu.query(sql, new MapHandler(), params.toArray());
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
        String sql = "SELECT SEKKEINO,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,SYURUI2,ATUMI2,"
                + "MAISUU2,SYURUI3,ATUMI3,MAISUU3,PATTERN,LASTLAYERSLIDERYO,ABSLIDE "
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
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("GENRYOU", "電極テープ");
                put("ETAPE", "電極テープ");
                put("EATUMI", "積層数");
                put("SOUSUU", "積層数");
                put("EMAISUU", "積層数");
                put("SYURUI2", "上カバーテープ１");
                put("ATUMI2", "上カバーテープ１");
                put("MAISUU2", "上カバーテープ１");
                put("SYURUI3", "下カバーテープ１");
                put("ATUMI3", "下カバーテープ１");
                put("MAISUU3", "下カバーテープ１");
                put("PATTERN", "電極製版名");
                put("ABSLIDE", "積層スライド量");
                put("LASTLAYERSLIDERYO", "最上層スライド量");
            }
        };

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

        // 設計データの取得
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
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
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
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
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
     * [積層RSUS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrRsussek> loadSrRsussek(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI"
                + ",SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2,TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO"
                + ",SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki"
                + ",UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu"
                + ",GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,EndTantousyacode,TanshiTapeSyurui"
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision,'0' AS deleteflag "
                + "FROM sr_rsussek "
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
        mapping.put("KOJYO", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LOTNO", "lotno"); //ﾛｯﾄNo
        mapping.put("EDABAN", "edaban"); //枝番
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TNTAPESYURUI", "tntapesyurui"); //端子ﾃｰﾌﾟ種類
        mapping.put("TNTAPENO", "tntapeno"); //端子ﾃｰﾌﾟＮo
        mapping.put("TNTAPEGENRYO", "tntapegenryo"); //端子ﾃｰﾌﾟ原料
        mapping.put("KAISINICHIJI", "kaisinichiji"); //開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //終了日時
        mapping.put("GOKI", "goki"); //号機ｺｰﾄﾞ
        mapping.put("JITUATURYOKU", "jituaturyoku"); //実圧力
        mapping.put("SEKISOZURE2", "sekisozure2"); //積層ｽﾞﾚ値2
        mapping.put("TANTOSYA", "tantosya"); //担当者ｺｰﾄﾞ
        mapping.put("KAKUNINSYA", "kakuninsya"); //確認者ｺｰﾄﾞ
        mapping.put("INSATUROLLNO", "insaturollno"); //印刷ﾛｰﾙNo
        mapping.put("HAPPOSHEETNO", "happosheetno"); //発砲ｼｰﾄNo
        mapping.put("SKJIKAN", "skjikan"); //瞬時加熱時間
        mapping.put("TAKUTO", "takuto"); //ﾀｸﾄ
        mapping.put("BIKO1", "biko1"); //備考1
        mapping.put("TOROKUNICHIJI", "torokunichiji"); //登録日時
        mapping.put("KOSINNICHIJI", "kosinnichiji"); //更新日時
        mapping.put("SKOJYO", "skojyo"); //先行工場ｺｰﾄﾞ
        mapping.put("SLOTNO", "slotno"); //先行ﾛｯﾄNo
        mapping.put("SEDABAN", "sedaban"); //先行枝番
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("Kotyakugouki", "kotyakugouki"); //固着ｼｰﾄ貼付り機
        mapping.put("Kotyakusheet", "kotyakusheet"); //固着ｼｰﾄ
        mapping.put("ShitaTanshigouki", "shitatanshigouki"); //下端子号機
        mapping.put("UwaTanshigouki", "uwatanshigouki"); //上端子号機
        mapping.put("ShitaTanshiBukunuki", "shitatanshibukunuki"); //下端子ﾌﾞｸ抜き
        mapping.put("ShitaTanshi", "shitatanshi"); //下端子
        mapping.put("UwaTanshi", "uwatanshi"); //上端子
        mapping.put("SyoriSetsuu", "syorisetsuu"); //処理ｾｯﾄ数
        mapping.put("RyouhinSetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("GaikanKakunin1", "gaikankakunin1"); //外観確認1
        mapping.put("GaikanKakunin2", "gaikankakunin2"); //外観確認2
        mapping.put("GaikanKakunin3", "gaikankakunin3"); //外観確認3
        mapping.put("GaikanKakunin4", "gaikankakunin4"); //外観確認4
        mapping.put("EndTantousyacode", "endtantousyacode"); //終了担当者
        mapping.put("TanshiTapeSyurui", "tanshitapesyurui"); //端子ﾃｰﾌﾟ種類
        mapping.put("HNGKaisuu", "hngkaisuu"); //隔離NG回数
        mapping.put("HNGKaisuuAve", "hngkaisuuave"); //剥離NG回数AVE
        mapping.put("GNGKaisuu", "gngkaisuu"); //画像NG回数
        mapping.put("GNGKaisuuAve", "gngkaisuuave"); //画像NG回数AVE
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrRsussek>> beanHandler = new BeanListHandler<>(SrRsussek.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [積層・RSUS_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRsussek> loadSubSrRsussek(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6"
                + ",setsuu7,setsuu8,setsuu9,setsuu10,setsuu11,setsuu12,setsuu13,setsuu14"
                + ",setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22"
                + ",setsuu23,setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30"
                + ",setsuu31,setsuu32,setsuu33,setsuu34,setsuu35,setsuu36,setsuu37,setsuu38"
                + ",setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8"
                + ",bikou9,bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17"
                + ",bikou18,bikou19,bikou20,bikou21,bikou22,bikou23,bikou24,bikou25,bikou26"
                + ",bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35"
                + ",bikou36,bikou37,bikou38,bikou39,bikou40,torokunichiji,kosinnichiji"
                + ",revision,'0' AS deleteflag "
                + "FROM sub_sr_rsussek "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("setsuu1", "setsuu1"); // ｾｯﾄ数1
        mapping.put("setsuu2", "setsuu2"); // ｾｯﾄ数2
        mapping.put("setsuu3", "setsuu3"); // ｾｯﾄ数3
        mapping.put("setsuu4", "setsuu4"); // ｾｯﾄ数4
        mapping.put("setsuu5", "setsuu5"); // ｾｯﾄ数5
        mapping.put("setsuu6", "setsuu6"); // ｾｯﾄ数6
        mapping.put("setsuu7", "setsuu7"); // ｾｯﾄ数7
        mapping.put("setsuu8", "setsuu8"); // ｾｯﾄ数8
        mapping.put("setsuu9", "setsuu9"); // ｾｯﾄ数9
        mapping.put("setsuu10", "setsuu10"); // ｾｯﾄ数10
        mapping.put("setsuu11", "setsuu11"); // ｾｯﾄ数11
        mapping.put("setsuu12", "setsuu12"); // ｾｯﾄ数12
        mapping.put("setsuu13", "setsuu13"); // ｾｯﾄ数13
        mapping.put("setsuu14", "setsuu14"); // ｾｯﾄ数14
        mapping.put("setsuu15", "setsuu15"); // ｾｯﾄ数15
        mapping.put("setsuu16", "setsuu16"); // ｾｯﾄ数16
        mapping.put("setsuu17", "setsuu17"); // ｾｯﾄ数17
        mapping.put("setsuu18", "setsuu18"); // ｾｯﾄ数18
        mapping.put("setsuu19", "setsuu19"); // ｾｯﾄ数19
        mapping.put("setsuu20", "setsuu20"); // ｾｯﾄ数20
        mapping.put("setsuu21", "setsuu21"); // ｾｯﾄ数21
        mapping.put("setsuu22", "setsuu22"); // ｾｯﾄ数22
        mapping.put("setsuu23", "setsuu23"); // ｾｯﾄ数23
        mapping.put("setsuu24", "setsuu24"); // ｾｯﾄ数24
        mapping.put("setsuu25", "setsuu25"); // ｾｯﾄ数25
        mapping.put("setsuu26", "setsuu26"); // ｾｯﾄ数26
        mapping.put("setsuu27", "setsuu27"); // ｾｯﾄ数27
        mapping.put("setsuu28", "setsuu28"); // ｾｯﾄ数28
        mapping.put("setsuu29", "setsuu29"); // ｾｯﾄ数29
        mapping.put("setsuu30", "setsuu30"); // ｾｯﾄ数30
        mapping.put("setsuu31", "setsuu31"); // ｾｯﾄ数31
        mapping.put("setsuu32", "setsuu32"); // ｾｯﾄ数32
        mapping.put("setsuu33", "setsuu33"); // ｾｯﾄ数33
        mapping.put("setsuu34", "setsuu34"); // ｾｯﾄ数34
        mapping.put("setsuu35", "setsuu35"); // ｾｯﾄ数35
        mapping.put("setsuu36", "setsuu36"); // ｾｯﾄ数36
        mapping.put("setsuu37", "setsuu37"); // ｾｯﾄ数37
        mapping.put("setsuu38", "setsuu38"); // ｾｯﾄ数38
        mapping.put("setsuu39", "setsuu39"); // ｾｯﾄ数39
        mapping.put("setsuu40", "setsuu40"); // ｾｯﾄ数40
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("bikou6", "bikou6"); // 備考6
        mapping.put("bikou7", "bikou7"); // 備考7
        mapping.put("bikou8", "bikou8"); // 備考8
        mapping.put("bikou9", "bikou9"); // 備考9
        mapping.put("bikou10", "bikou10"); // 備考10
        mapping.put("bikou11", "bikou11"); // 備考11
        mapping.put("bikou12", "bikou12"); // 備考12
        mapping.put("bikou13", "bikou13"); // 備考13
        mapping.put("bikou14", "bikou14"); // 備考14
        mapping.put("bikou15", "bikou15"); // 備考15
        mapping.put("bikou16", "bikou16"); // 備考16
        mapping.put("bikou17", "bikou17"); // 備考17
        mapping.put("bikou18", "bikou18"); // 備考18
        mapping.put("bikou19", "bikou19"); // 備考19
        mapping.put("bikou20", "bikou20"); // 備考20
        mapping.put("bikou21", "bikou21"); // 備考21
        mapping.put("bikou22", "bikou22"); // 備考22
        mapping.put("bikou23", "bikou23"); // 備考23
        mapping.put("bikou24", "bikou24"); // 備考24
        mapping.put("bikou25", "bikou25"); // 備考25
        mapping.put("bikou26", "bikou26"); // 備考26
        mapping.put("bikou27", "bikou27"); // 備考27
        mapping.put("bikou28", "bikou28"); // 備考28
        mapping.put("bikou29", "bikou29"); // 備考29
        mapping.put("bikou30", "bikou30"); // 備考30
        mapping.put("bikou31", "bikou31"); // 備考31
        mapping.put("bikou32", "bikou32"); // 備考32
        mapping.put("bikou33", "bikou33"); // 備考33
        mapping.put("bikou34", "bikou34"); // 備考34
        mapping.put("bikou35", "bikou35"); // 備考35
        mapping.put("bikou36", "bikou36"); // 備考36
        mapping.put("bikou37", "bikou37"); // 備考37
        mapping.put("bikou38", "bikou38"); // 備考38
        mapping.put("bikou39", "bikou39"); // 備考39
        mapping.put("bikou40", "bikou40"); // 備考40
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrRsussek>> beanHandler = new BeanListHandler<>(SubSrRsussek.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [積層RSUS_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrRsussek> loadTmpSrRsussek(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        
        String sql = "SELECT KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI"
                + ",SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2,TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO"
                + ",SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki"
                + ",UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu"
                + ",GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,EndTantousyacode,TanshiTapeSyurui"
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision,deleteflag "
                + "FROM tmp_sr_rsussek "
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
        mapping.put("KOJYO", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LOTNO", "lotno"); //ﾛｯﾄNo
        mapping.put("EDABAN", "edaban"); //枝番
        mapping.put("KCPNO", "kcpno"); //KCPNO
        mapping.put("TNTAPESYURUI", "tntapesyurui"); //端子ﾃｰﾌﾟ種類
        mapping.put("TNTAPENO", "tntapeno"); //端子ﾃｰﾌﾟＮo
        mapping.put("TNTAPEGENRYO", "tntapegenryo"); //端子ﾃｰﾌﾟ原料
        mapping.put("KAISINICHIJI", "kaisinichiji"); //開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //終了日時
        mapping.put("GOKI", "goki"); //号機ｺｰﾄﾞ
        mapping.put("JITUATURYOKU", "jituaturyoku"); //実圧力
        mapping.put("SEKISOZURE2", "sekisozure2"); //積層ｽﾞﾚ値2
        mapping.put("TANTOSYA", "tantosya"); //担当者ｺｰﾄﾞ
        mapping.put("KAKUNINSYA", "kakuninsya"); //確認者ｺｰﾄﾞ
        mapping.put("INSATUROLLNO", "insaturollno"); //印刷ﾛｰﾙNo
        mapping.put("HAPPOSHEETNO", "happosheetno"); //発砲ｼｰﾄNo
        mapping.put("SKJIKAN", "skjikan"); //瞬時加熱時間
        mapping.put("TAKUTO", "takuto"); //ﾀｸﾄ
        mapping.put("BIKO1", "biko1"); //備考1
        mapping.put("TOROKUNICHIJI", "torokunichiji"); //登録日時
        mapping.put("KOSINNICHIJI", "kosinnichiji"); //更新日時
        mapping.put("SKOJYO", "skojyo"); //先行工場ｺｰﾄﾞ
        mapping.put("SLOTNO", "slotno"); //先行ﾛｯﾄNo
        mapping.put("SEDABAN", "sedaban"); //先行枝番
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("Kotyakugouki", "kotyakugouki"); //固着ｼｰﾄ貼付り機
        mapping.put("Kotyakusheet", "kotyakusheet"); //固着ｼｰﾄ
        mapping.put("ShitaTanshigouki", "shitatanshigouki"); //下端子号機
        mapping.put("UwaTanshigouki", "uwatanshigouki"); //上端子号機
        mapping.put("ShitaTanshiBukunuki", "shitatanshibukunuki"); //下端子ﾌﾞｸ抜き
        mapping.put("ShitaTanshi", "shitatanshi"); //下端子
        mapping.put("UwaTanshi", "uwatanshi"); //上端子
        mapping.put("SyoriSetsuu", "syorisetsuu"); //処理ｾｯﾄ数
        mapping.put("RyouhinSetsuu", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("GaikanKakunin1", "gaikankakunin1"); //外観確認1
        mapping.put("GaikanKakunin2", "gaikankakunin2"); //外観確認2
        mapping.put("GaikanKakunin3", "gaikankakunin3"); //外観確認3
        mapping.put("GaikanKakunin4", "gaikankakunin4"); //外観確認4
        mapping.put("EndTantousyacode", "endtantousyacode"); //終了担当者
        mapping.put("TanshiTapeSyurui", "tanshitapesyurui"); //端子ﾃｰﾌﾟ種類
        mapping.put("HNGKaisuu", "hngkaisuu"); //隔離NG回数
        mapping.put("HNGKaisuuAve", "hngkaisuuave"); //剥離NG回数AVE
        mapping.put("GNGKaisuu", "gngkaisuu"); //画像NG回数
        mapping.put("GNGKaisuuAve", "gngkaisuuave"); //画像NG回数AVE
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrRsussek>> beanHandler = new BeanListHandler<>(SrRsussek.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [積層・RSUS_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRsussek> loadTmpSubSrRsussek(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6"
                + ",setsuu7,setsuu8,setsuu9,setsuu10,setsuu11,setsuu12,setsuu13,setsuu14"
                + ",setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22"
                + ",setsuu23,setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30"
                + ",setsuu31,setsuu32,setsuu33,setsuu34,setsuu35,setsuu36,setsuu37,setsuu38"
                + ",setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8"
                + ",bikou9,bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17"
                + ",bikou18,bikou19,bikou20,bikou21,bikou22,bikou23,bikou24,bikou25,bikou26"
                + ",bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35"
                + ",bikou36,bikou37,bikou38,bikou39,bikou40,torokunichiji,kosinnichiji"
                + ",revision,deleteflag "
                + "FROM tmp_sub_sr_rsussek "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("setsuu1", "setsuu1"); // ｾｯﾄ数1
        mapping.put("setsuu2", "setsuu2"); // ｾｯﾄ数2
        mapping.put("setsuu3", "setsuu3"); // ｾｯﾄ数3
        mapping.put("setsuu4", "setsuu4"); // ｾｯﾄ数4
        mapping.put("setsuu5", "setsuu5"); // ｾｯﾄ数5
        mapping.put("setsuu6", "setsuu6"); // ｾｯﾄ数6
        mapping.put("setsuu7", "setsuu7"); // ｾｯﾄ数7
        mapping.put("setsuu8", "setsuu8"); // ｾｯﾄ数8
        mapping.put("setsuu9", "setsuu9"); // ｾｯﾄ数9
        mapping.put("setsuu10", "setsuu10"); // ｾｯﾄ数10
        mapping.put("setsuu11", "setsuu11"); // ｾｯﾄ数11
        mapping.put("setsuu12", "setsuu12"); // ｾｯﾄ数12
        mapping.put("setsuu13", "setsuu13"); // ｾｯﾄ数13
        mapping.put("setsuu14", "setsuu14"); // ｾｯﾄ数14
        mapping.put("setsuu15", "setsuu15"); // ｾｯﾄ数15
        mapping.put("setsuu16", "setsuu16"); // ｾｯﾄ数16
        mapping.put("setsuu17", "setsuu17"); // ｾｯﾄ数17
        mapping.put("setsuu18", "setsuu18"); // ｾｯﾄ数18
        mapping.put("setsuu19", "setsuu19"); // ｾｯﾄ数19
        mapping.put("setsuu20", "setsuu20"); // ｾｯﾄ数20
        mapping.put("setsuu21", "setsuu21"); // ｾｯﾄ数21
        mapping.put("setsuu22", "setsuu22"); // ｾｯﾄ数22
        mapping.put("setsuu23", "setsuu23"); // ｾｯﾄ数23
        mapping.put("setsuu24", "setsuu24"); // ｾｯﾄ数24
        mapping.put("setsuu25", "setsuu25"); // ｾｯﾄ数25
        mapping.put("setsuu26", "setsuu26"); // ｾｯﾄ数26
        mapping.put("setsuu27", "setsuu27"); // ｾｯﾄ数27
        mapping.put("setsuu28", "setsuu28"); // ｾｯﾄ数28
        mapping.put("setsuu29", "setsuu29"); // ｾｯﾄ数29
        mapping.put("setsuu30", "setsuu30"); // ｾｯﾄ数30
        mapping.put("setsuu31", "setsuu31"); // ｾｯﾄ数31
        mapping.put("setsuu32", "setsuu32"); // ｾｯﾄ数32
        mapping.put("setsuu33", "setsuu33"); // ｾｯﾄ数33
        mapping.put("setsuu34", "setsuu34"); // ｾｯﾄ数34
        mapping.put("setsuu35", "setsuu35"); // ｾｯﾄ数35
        mapping.put("setsuu36", "setsuu36"); // ｾｯﾄ数36
        mapping.put("setsuu37", "setsuu37"); // ｾｯﾄ数37
        mapping.put("setsuu38", "setsuu38"); // ｾｯﾄ数38
        mapping.put("setsuu39", "setsuu39"); // ｾｯﾄ数39
        mapping.put("setsuu40", "setsuu40"); // ｾｯﾄ数40
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("bikou3", "bikou3"); // 備考3
        mapping.put("bikou4", "bikou4"); // 備考4
        mapping.put("bikou5", "bikou5"); // 備考5
        mapping.put("bikou6", "bikou6"); // 備考6
        mapping.put("bikou7", "bikou7"); // 備考7
        mapping.put("bikou8", "bikou8"); // 備考8
        mapping.put("bikou9", "bikou9"); // 備考9
        mapping.put("bikou10", "bikou10"); // 備考10
        mapping.put("bikou11", "bikou11"); // 備考11
        mapping.put("bikou12", "bikou12"); // 備考12
        mapping.put("bikou13", "bikou13"); // 備考13
        mapping.put("bikou14", "bikou14"); // 備考14
        mapping.put("bikou15", "bikou15"); // 備考15
        mapping.put("bikou16", "bikou16"); // 備考16
        mapping.put("bikou17", "bikou17"); // 備考17
        mapping.put("bikou18", "bikou18"); // 備考18
        mapping.put("bikou19", "bikou19"); // 備考19
        mapping.put("bikou20", "bikou20"); // 備考20
        mapping.put("bikou21", "bikou21"); // 備考21
        mapping.put("bikou22", "bikou22"); // 備考22
        mapping.put("bikou23", "bikou23"); // 備考23
        mapping.put("bikou24", "bikou24"); // 備考24
        mapping.put("bikou25", "bikou25"); // 備考25
        mapping.put("bikou26", "bikou26"); // 備考26
        mapping.put("bikou27", "bikou27"); // 備考27
        mapping.put("bikou28", "bikou28"); // 備考28
        mapping.put("bikou29", "bikou29"); // 備考29
        mapping.put("bikou30", "bikou30"); // 備考30
        mapping.put("bikou31", "bikou31"); // 備考31
        mapping.put("bikou32", "bikou32"); // 備考32
        mapping.put("bikou33", "bikou33"); // 備考33
        mapping.put("bikou34", "bikou34"); // 備考34
        mapping.put("bikou35", "bikou35"); // 備考35
        mapping.put("bikou36", "bikou36"); // 備考36
        mapping.put("bikou37", "bikou37"); // 備考37
        mapping.put("bikou38", "bikou38"); // 備考38
        mapping.put("bikou39", "bikou39"); // 備考39
        mapping.put("bikou40", "bikou40"); // 備考40
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrRsussek>> beanHandler = new BeanListHandler<>(SubSrRsussek.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [製版ﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param pattern 電極製版名(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaPatternMas(QueryRunner queryRunnerQcdb, String pattern) throws SQLException {

        // 製版ﾏｽﾀデータの取得
        String sql = "SELECT LRETU, WRETU, LSUN, WSUN "
                + " FROM da_patternmas WHERE PATTERN = ? ";

        List<Object> params = new ArrayList<>();
        params.add(pattern);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
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
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            String edaban = lotNo.substring(11, 14);

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

            // 積層・RSUSデータ取得
            List<SrRsussek> srSrRsussekDataList = getSrRsussekData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srSrRsussekDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 積層・RSUS_ｻﾌﾞ画面データ取得
            List<SubSrRsussek> subSrRsussekDataList = getSubSrRsussekData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (subSrRsussekDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSrRsussekDataList.get(0));

            // 剥離内容入力画面データ設定
            setInputItemDataSubFormC006(subSrRsussekDataList.get(0));

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
     * @param srSrRsussekData 積層・RSUS
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrRsussek srSrRsussekData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSrRsussekData != null) {
            // 元データが存在する場合元データより取得
            return getSrRsussekItemData(itemId, srSrRsussekData);
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
     * 積層RSUS_仮登録(tmp_sr_rsussek)登録処理
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
    private void insertTmpSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_rsussek ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN"
                + ",tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki"
                + ",Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu"
                + ",GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,EndTantousyacode,TanshiTapeSyurui"
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrRsussek(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層RSUS_仮登録(tmp_sr_rsussek)更新処理
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
    private void updateTmpSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_rsussek SET "
                + "KCPNO = ?,TNTAPESYURUI = ?,TNTAPENO = ?,TNTAPEGENRYO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,GOKI = ?,JITUATURYOKU = ?,SEKISOZURE2 = ?,"
                + "TANTOSYA = ?,KAKUNINSYA = ?,INSATUROLLNO = ?,HAPPOSHEETNO = ?,SKJIKAN = ?,TAKUTO = ?,BIKO1 = ?,KOSINNICHIJI = ?,SKOJYO = ?,SLOTNO = ?,SEDABAN = ?,"
                + "tapelotno = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,genryoukigou = ?,petfilmsyurui = ?,Kotyakugouki = ?,Kotyakusheet = ?,"
                + "ShitaTanshigouki = ?,UwaTanshigouki = ?,ShitaTanshiBukunuki = ?,ShitaTanshi = ?,UwaTanshi = ?,SyoriSetsuu = ?,RyouhinSetsuu = ?,GaikanKakunin1 = ?,"
                + "GaikanKakunin2 = ?,GaikanKakunin3 = ?,GaikanKakunin4 = ?,EndTantousyacode = ?,TanshiTapeSyurui = ?,HNGKaisuu = ?,"
                + "HNGKaisuuAve = ?,GNGKaisuu = ?,GNGKaisuuAve = ?,bikou2 = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrRsussek> srSrRsussekList = getSrRsussekData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrRsussek srRsussek = null;
        if (!srSrRsussekList.isEmpty()) {
            srRsussek = srSrRsussekList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrRsussek(false, newRev, 0, "", "", "", systemTime, itemList, srRsussek);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層RSUS_仮登録(tmp_sr_rsussek)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_rsussek "
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
     * 積層RSUS_仮登録(tmp_sr_rsussek)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srRsussekData 積層SPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrRsussek(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrRsussek srRsussekData) {
        List<Object> params = new ArrayList<>();
        String senkouLotno = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.SENKOU_LOT_NO, srRsussekData));
        String skojyo = "";
        String slotNo = "";
        String sedaban = "";

        if (!"".equals(senkouLotno) && senkouLotno.length() > 0){
            if (senkouLotno.length() < 3){
                skojyo = senkouLotno.substring(0, senkouLotno.length());
            }
            if (senkouLotno.length() > 3 && senkouLotno.length() <= 11){
                skojyo = senkouLotno.substring(0, 3);
                slotNo = senkouLotno.substring(3, senkouLotno.length());
            }
            if (senkouLotno.length() > 11 && senkouLotno.length() <= 14){
                skojyo = senkouLotno.substring(0, 3);
                slotNo = senkouLotno.substring(3, 11);
                sedaban = senkouLotno.substring(11, senkouLotno.length());
            }
        }

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KCPNO, srRsussekData))); //KCPNO        
        params.add(null);// 端子テープ種類
        params.add(null); //端子ﾃｰﾌﾟＮo
        params.add(null); //端子ﾃｰﾌﾟ原料
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KAISHI_DAY, srRsussekData),
            getItemData(itemList, GXHDO101B005Const.KAISHI_TIME, srRsussekData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHURYOU_DAY, srRsussekData),
           getItemData(itemList, GXHDO101B005Const.SHURYOU_TIME, srRsussekData))); //終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SEKISOU_GOKI, srRsussekData))); //積層号機
        params.add(null); //実圧力
        params.add(null); //積層ｽﾞﾚ値2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KAISHI_TANTOUSHA, srRsussekData))); //開始担当者
        params.add(null); //確認者ｺｰﾄﾞ
        params.add(null); //印刷ﾛｰﾙNo
        params.add(null); //発砲ｼｰﾄNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHUNJI_KANETSU_TIME, srRsussekData))); //瞬時加熱時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.TAKT, srRsussekData))); //タクト
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.BIKOU1, srRsussekData))); //備考1        
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(skojyo); //先行工場ｺｰﾄﾞ
        params.add(slotNo); //先行ﾛｯﾄNo
        params.add(sedaban); //先行枝番
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SLIP_LOTNO, srRsussekData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.ROLL_NO1, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.ROLL_NO2, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.ROLL_NO3, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo3  
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GENRYO_KIGOU, srRsussekData))); //原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.PET_FILM_SHURUI, srRsussekData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI, srRsussekData))); //固着シート貼付り機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET, srRsussekData))); //固着シート
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_GOUKI, srRsussekData))); //下端子号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UE_TANSHI_GOUKI, srRsussekData))); //上端子号機  
        // 下端子ﾌﾞｸ抜き
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_BUKUNUKI, srRsussekData))) {
            case "未実施":
                params.add(0);
                break;
            case "実施":
                params.add(1);
                break;
            default:
                params.add(null);
                break;
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI, srRsussekData))); //下端子
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI, srRsussekData))); //上端子
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SYORI_SET_SU, srRsussekData))); //処理セット数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.RYOUHIN_SET_SU, srRsussekData))); //良品セット数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN1, srRsussekData))); //外観確認1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN2, srRsussekData))); //外観確認2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN3, srRsussekData))); //外観確認3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN4, srRsussekData))); //外観確認4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHURYOU_TANTOUSHA, srRsussekData))); //終了担当者
        // 端子テープ種類
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.TANSHI_TAPE_SHURUI, srRsussekData))) {
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.HAKURI_NG_KAISU, srRsussekData))); //剥離NG回数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.HAKURI_NG_AVE, srRsussekData))); //剥離NG_AVE        
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GASHO_NG_KAISU, srRsussekData))); //画処NG回数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GASHO_NG_AVE, srRsussekData))); //画処NG_AVE        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.BIKOU2, srRsussekData))); //備考2

        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsussek)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_rsussek ("
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10,setsuu11"
                + ",setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22,setsuu23"
                + ",setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32,setsuu33,setsuu34,setsuu35"
                + ",setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8,bikou9"
                + ",bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17,bikou18,bikou19,bikou20,bikou21,bikou22,bikou23"
                + ",bikou24,bikou25,bikou26,bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37"
                + ",bikou38,bikou39,bikou40,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";     

        List<Object> params = setUpdateParameterTmpSubSrRsussek(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsussek)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateTmpSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_rsussek SET "
                + "setsuu1 = ?,setsuu2 = ?,setsuu3 = ?,setsuu4 = ?,setsuu5 = ?,setsuu6 = ?,setsuu7 = ?"
                + ",setsuu8 = ?,setsuu9 = ?,setsuu10 = ?,setsuu11 = ?,setsuu12 = ?,setsuu13 = ?,setsuu14 = ?,setsuu15 = ?,setsuu16 = ?"
                + ",setsuu17 = ?,setsuu18 = ?,setsuu19 = ?,setsuu20 = ?,setsuu21 = ?,setsuu22 = ?,setsuu23 = ?,setsuu24 = ?,setsuu25 = ?"
                + ",setsuu26 = ?,setsuu27 = ?,setsuu28 = ?,setsuu29 = ?,setsuu30 = ?,setsuu31 = ?,setsuu32 = ?,setsuu33 = ?,setsuu34 = ?"
                + ",setsuu35 = ?,setsuu36 = ?,setsuu37 = ?,setsuu38 = ?,setsuu39 = ?,setsuu40 = ?,bikou1 = ?,bikou2 = ?,bikou3 = ?"
                + ",bikou4 = ?,bikou5 = ?,bikou6 = ?,bikou7 = ?,bikou8 = ?,bikou9 = ?,bikou10 = ?,bikou11 = ?,bikou12 = ?,bikou13 = ?"
                + ",bikou14 = ?,bikou15 = ?,bikou16 = ?,bikou17 = ?,bikou18 = ?,bikou19 = ?,bikou20 = ?,bikou21 = ?,bikou22 = ?,bikou23 = ?"
                + ",bikou24 = ?,bikou25 = ?,bikou26 = ?,bikou27 = ?,bikou28 = ?,bikou29 = ?,bikou30 = ?,bikou31 = ?,bikou32 = ?,bikou33 = ?"
                + ",bikou34 = ?,bikou35 = ?,bikou36 = ?,bikou37 = ?,bikou38 = ?,bikou39 = ?,bikou40 = ?,kosinnichiji = ? ,revision = ? "
                + ",deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterTmpSubSrRsussek(false, newRev, 0, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsussek)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_rsussek "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 検索条件
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsussek)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSubSrRsussek(boolean isInsert, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
        List<GXHDO101C006Model.HakuriInputData> hakuriInputDataList = beanGXHDO101C006.getGxhdO101c006Model().getHakuriInputDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(0).getSetsuuVal())); // ｾｯﾄ数1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(1).getSetsuuVal())); // ｾｯﾄ数2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(2).getSetsuuVal())); // ｾｯﾄ数3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(3).getSetsuuVal())); // ｾｯﾄ数4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(4).getSetsuuVal())); // ｾｯﾄ数5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(5).getSetsuuVal())); // ｾｯﾄ数6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(6).getSetsuuVal())); // ｾｯﾄ数7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(7).getSetsuuVal())); // ｾｯﾄ数8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(8).getSetsuuVal())); // ｾｯﾄ数9
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(9).getSetsuuVal())); // ｾｯﾄ数10
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(10).getSetsuuVal())); // ｾｯﾄ数11
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(11).getSetsuuVal())); // ｾｯﾄ数12
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(12).getSetsuuVal())); // ｾｯﾄ数13
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(13).getSetsuuVal())); // ｾｯﾄ数14
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(14).getSetsuuVal())); // ｾｯﾄ数15
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(15).getSetsuuVal())); // ｾｯﾄ数16
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(16).getSetsuuVal())); // ｾｯﾄ数17
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(17).getSetsuuVal())); // ｾｯﾄ数18
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(18).getSetsuuVal())); // ｾｯﾄ数19
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(19).getSetsuuVal())); // ｾｯﾄ数20
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(20).getSetsuuVal())); // ｾｯﾄ数21
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(21).getSetsuuVal())); // ｾｯﾄ数22
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(22).getSetsuuVal())); // ｾｯﾄ数23
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(23).getSetsuuVal())); // ｾｯﾄ数24
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(24).getSetsuuVal())); // ｾｯﾄ数25
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(25).getSetsuuVal())); // ｾｯﾄ数26
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(26).getSetsuuVal())); // ｾｯﾄ数27
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(27).getSetsuuVal())); // ｾｯﾄ数28
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(28).getSetsuuVal())); // ｾｯﾄ数29
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(29).getSetsuuVal())); // ｾｯﾄ数30
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(30).getSetsuuVal())); // ｾｯﾄ数31
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(31).getSetsuuVal())); // ｾｯﾄ数32
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(32).getSetsuuVal())); // ｾｯﾄ数33
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(33).getSetsuuVal())); // ｾｯﾄ数34
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(34).getSetsuuVal())); // ｾｯﾄ数35
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(35).getSetsuuVal())); // ｾｯﾄ数36
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(36).getSetsuuVal())); // ｾｯﾄ数37
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(37).getSetsuuVal())); // ｾｯﾄ数38
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(38).getSetsuuVal())); // ｾｯﾄ数39
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(hakuriInputDataList.get(39).getSetsuuVal())); // ｾｯﾄ数40
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(0).getBikouVal())); // 備考1
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(1).getBikouVal())); // 備考2
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(2).getBikouVal())); // 備考3
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(3).getBikouVal())); // 備考4
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(4).getBikouVal())); // 備考5
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(5).getBikouVal())); // 備考6
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(6).getBikouVal())); // 備考7
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(7).getBikouVal())); // 備考8
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(8).getBikouVal())); // 備考9
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(9).getBikouVal())); // 備考10
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(10).getBikouVal())); // 備考11
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(11).getBikouVal())); // 備考12
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(12).getBikouVal())); // 備考13
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(13).getBikouVal())); // 備考14
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(14).getBikouVal())); // 備考15
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(15).getBikouVal())); // 備考16
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(16).getBikouVal())); // 備考17
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(17).getBikouVal())); // 備考18
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(18).getBikouVal())); // 備考19
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(19).getBikouVal())); // 備考20
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(20).getBikouVal())); // 備考21
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(21).getBikouVal())); // 備考22
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(22).getBikouVal())); // 備考23
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(23).getBikouVal())); // 備考24
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(24).getBikouVal())); // 備考25
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(25).getBikouVal())); // 備考26
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(26).getBikouVal())); // 備考27
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(27).getBikouVal())); // 備考28
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(28).getBikouVal())); // 備考29
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(29).getBikouVal())); // 備考30
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(30).getBikouVal())); // 備考31
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(31).getBikouVal())); // 備考32
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(32).getBikouVal())); // 備考33
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(33).getBikouVal())); // 備考34
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(34).getBikouVal())); // 備考35
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(35).getBikouVal())); // 備考36
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(36).getBikouVal())); // 備考37
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(37).getBikouVal())); // 備考38
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(38).getBikouVal())); // 備考39
        params.add(DBUtil.stringToStringObject(hakuriInputDataList.get(39).getBikouVal())); // 備考40

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
     * 積層RSUS(sr_rsussek)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrRsussek 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrRsussek tmpSrRsussek) throws SQLException {

        String sql = "INSERT INTO sr_rsussek ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO"
                + ",SEDABAN,tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki"
                + ",UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3"
                + ",GaikanKakunin4,EndTantousyacode,TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        
        List<Object> params = setUpdateParameterSrRsussek(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrRsussek);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層RSUS(sr_rsussek)更新処理
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
    private void updateSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_rsussek SET "
                + "KCPNO = ?,TNTAPESYURUI = ?,TNTAPENO = ?,TNTAPEGENRYO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,GOKI = ?,JITUATURYOKU = ?,"
                + "SEKISOZURE2 = ?,TANTOSYA = ?,KAKUNINSYA = ?,INSATUROLLNO = ?,HAPPOSHEETNO = ?,SKJIKAN = ?,TAKUTO = ?,BIKO1 = ?,"
                + "KOSINNICHIJI = ?,SKOJYO = ?,SLOTNO = ?,SEDABAN = ?,tapelotno = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,genryoukigou = ?,"
                + "petfilmsyurui = ?,Kotyakugouki = ?,Kotyakusheet = ?,ShitaTanshigouki = ?,UwaTanshigouki = ?,ShitaTanshiBukunuki = ?,"
                + "ShitaTanshi = ?,UwaTanshi = ?,SyoriSetsuu = ?,RyouhinSetsuu = ?,GaikanKakunin1 = ?,GaikanKakunin2 = ?,GaikanKakunin3 = ?,"
                + "GaikanKakunin4 = ?,EndTantousyacode = ?,TanshiTapeSyurui = ?,HNGKaisuu = ?,HNGKaisuuAve = ?,GNGKaisuu = ?,GNGKaisuuAve = ?,"
                + "bikou2 = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 更新前の値を取得
        List<SrRsussek> srRsussekList = getSrRsussekData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrRsussek srRsussek = null;
        if (!srRsussekList.isEmpty()) {
            srRsussek = srRsussekList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrRsussek(false, newRev, "", "", "", systemTime, itemList, srRsussek);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層RSUS(sr_rsussek)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srRsussekData 積層SPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrRsussek(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrRsussek srRsussekData) {
        List<Object> params = new ArrayList<>();

        String senkouLotno = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.SENKOU_LOT_NO, srRsussekData));
        String skojyo = "";
        String slotNo = "";
        String sedaban = "";

        if (!"".equals(senkouLotno) && senkouLotno.length() > 0){
            if (senkouLotno.length() < 3){
                skojyo = senkouLotno.substring(0, senkouLotno.length());
            }
            if (senkouLotno.length() > 3 && senkouLotno.length() <= 11){
                skojyo = senkouLotno.substring(0, 3);
                slotNo = senkouLotno.substring(3, senkouLotno.length());
            }
            if (senkouLotno.length() > 11 && senkouLotno.length() <= 14){
                skojyo = senkouLotno.substring(0, 3);
                slotNo = senkouLotno.substring(3, 11);
                sedaban = senkouLotno.substring(11, senkouLotno.length());
            }
        }
        
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KCPNO, srRsussekData))); //KCPNO        
        params.add(""); // 端子テープ種類
        params.add(""); //端子ﾃｰﾌﾟＮo
        params.add(""); //端子ﾃｰﾌﾟ原料
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B005Const.KAISHI_DAY, srRsussekData),
                getItemData(itemList, GXHDO101B005Const.KAISHI_TIME, srRsussekData))); //開始日時
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B005Const.SHURYOU_DAY, srRsussekData),
                getItemData(itemList, GXHDO101B005Const.SHURYOU_TIME, srRsussekData))); //終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SEKISOU_GOKI, srRsussekData))); //積層号機
        params.add(""); //実圧力
        params.add(0); //積層ｽﾞﾚ値2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KAISHI_TANTOUSHA, srRsussekData))); //開始担当者
        params.add(""); //確認者ｺｰﾄﾞ
        params.add(""); //印刷ﾛｰﾙNo
        params.add(""); //発砲ｼｰﾄNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.SHUNJI_KANETSU_TIME, srRsussekData))); //瞬時加熱時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.TAKT, srRsussekData))); //タクト
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.BIKOU1, srRsussekData))); //備考1
        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時

        } else {
            params.add(systemTime); //更新日時
        }
        params.add(skojyo); //先行工場ｺｰﾄﾞ
        params.add(slotNo); //先行ﾛｯﾄNo
        params.add(sedaban); //先行枝番   
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SLIP_LOTNO, srRsussekData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo 
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.ROLL_NO1, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.ROLL_NO2, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.ROLL_NO3, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GENRYO_KIGOU, srRsussekData))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.PET_FILM_SHURUI, srRsussekData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI, srRsussekData))); //固着シート貼付り機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET, srRsussekData))); //固着シート
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_GOUKI, srRsussekData))); //下端子号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.UE_TANSHI_GOUKI, srRsussekData))); //上端子号機 
        // 下端子ﾌﾞｸ抜き
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_BUKUNUKI, srRsussekData))) {
            case "未実施":
                params.add(0);
                break;
            case "実施":
                params.add(1);
                break;
            default:
                params.add(9);
                break;
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI, srRsussekData))); //下端子
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI, srRsussekData))); //上端子
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.SYORI_SET_SU, srRsussekData))); //処理セット数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.RYOUHIN_SET_SU, srRsussekData))); //良品セット数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN1, srRsussekData))); //外観確認1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN2, srRsussekData))); //外観確認2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN3, srRsussekData))); //外観確認3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN4, srRsussekData))); //外観確認4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SHURYOU_TANTOUSHA, srRsussekData))); //終了担当者
        // 端子テープ種類
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.TANSHI_TAPE_SHURUI, srRsussekData))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.HAKURI_NG_KAISU, srRsussekData))); //剥離NG回数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.HAKURI_NG_AVE, srRsussekData))); //剥離NG_AVE        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.GASHO_NG_KAISU, srRsussekData))); //画処NG回数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.GASHO_NG_AVE, srRsussekData))); //画処NG_AVE        
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.BIKOU2, srRsussekData))); //備考2
        
        params.add(newRev); //revision

        return params;
    }

    /**
     * 積層・RSUS(sr_rsussek)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_rsussek "
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
     * 積層・RSUS_ｻﾌﾞ画面(sub_sr_rsussek)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_rsussek ("
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10"
                + ",setsuu11,setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21"
                + ",setsuu22,setsuu23,setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32"
                + ",setsuu33,setsuu34,setsuu35,setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4"
                + ",bikou5,bikou6,bikou7,bikou8,bikou9,bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17"
                + ",bikou18,bikou19,bikou20,bikou21,bikou22,bikou23,bikou24,bikou25,bikou26,bikou27,bikou28,bikou29"
                + ",bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37,bikou38,bikou39,bikou40,torokunichiji"
                + ",kosinnichiji,revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "                
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        List<Object> params = setUpdateParameterSubSrRsussek(true, newRev, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面(sub_sr_rsussek)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE sub_sr_rsussek SET "
                + "setsuu1 = ?,setsuu2 = ?,setsuu3 = ?,setsuu4 = ?,setsuu5 = ?,setsuu6 = ?,setsuu7 = ?,setsuu8 = ?,"
                + "setsuu9 = ?,setsuu10 = ?,setsuu11 = ?,setsuu12 = ?,setsuu13 = ?,setsuu14 = ?,setsuu15 = ?,setsuu16 = ?,"
                + "setsuu17 = ?,setsuu18 = ?,setsuu19 = ?,setsuu20 = ?,setsuu21 = ?,setsuu22 = ?,setsuu23 = ?,setsuu24 = ?,"
                + "setsuu25 = ?,setsuu26 = ?,setsuu27 = ?,setsuu28 = ?,setsuu29 = ?,setsuu30 = ?,setsuu31 = ?,setsuu32 = ?,"
                + "setsuu33 = ?,setsuu34 = ?,setsuu35 = ?,setsuu36 = ?,setsuu37 = ?,setsuu38 = ?,setsuu39 = ?,setsuu40 = ?,"
                + "bikou1 = ?,bikou2 = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,bikou6 = ?,bikou7 = ?,bikou8 = ?,bikou9 = ?,"
                + "bikou10 = ?,bikou11 = ?,bikou12 = ?,bikou13 = ?,bikou14 = ?,bikou15 = ?,bikou16 = ?,bikou17 = ?,"
                + "bikou18 = ?,bikou19 = ?,bikou20 = ?,bikou21 = ?,bikou22 = ?,bikou23 = ?,bikou24 = ?,bikou25 = ?,"
                + "bikou26 = ?,bikou27 = ?,bikou28 = ?,bikou29 = ?,bikou30 = ?,bikou31 = ?,bikou32 = ?,bikou33 = ?,"
                + "bikou34 = ?,bikou35 = ?,bikou36 = ?,bikou37 = ?,bikou38 = ?,bikou39 = ?,bikou40 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterSubSrRsussek(false, newRev, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 積層・RSUS_ｻﾌﾞ画面登録(tmp_sub_sr_rsussek)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSubSrRsussek(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C006 beanGXHDO101C006 = (GXHDO101C006) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C006);
        List<GXHDO101C006Model.HakuriInputData> makuatsuDataList = beanGXHDO101C006.getGxhdO101c006Model().getHakuriInputDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番

        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(0).getSetsuuVal())); // ｾｯﾄ数1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(1).getSetsuuVal())); // ｾｯﾄ数2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(2).getSetsuuVal())); // ｾｯﾄ数3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(3).getSetsuuVal())); // ｾｯﾄ数4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(4).getSetsuuVal())); // ｾｯﾄ数5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(5).getSetsuuVal())); // ｾｯﾄ数6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(6).getSetsuuVal())); // ｾｯﾄ数7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(7).getSetsuuVal())); // ｾｯﾄ数8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(8).getSetsuuVal())); // ｾｯﾄ数9
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(9).getSetsuuVal())); // ｾｯﾄ数10
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(10).getSetsuuVal())); // ｾｯﾄ数11
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(11).getSetsuuVal())); // ｾｯﾄ数12
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(12).getSetsuuVal())); // ｾｯﾄ数13
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(13).getSetsuuVal())); // ｾｯﾄ数14
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(14).getSetsuuVal())); // ｾｯﾄ数15
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(15).getSetsuuVal())); // ｾｯﾄ数16
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(16).getSetsuuVal())); // ｾｯﾄ数17
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(17).getSetsuuVal())); // ｾｯﾄ数18
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(18).getSetsuuVal())); // ｾｯﾄ数19
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(19).getSetsuuVal())); // ｾｯﾄ数20
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(20).getSetsuuVal())); // ｾｯﾄ数21
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(21).getSetsuuVal())); // ｾｯﾄ数22
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(22).getSetsuuVal())); // ｾｯﾄ数23
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(23).getSetsuuVal())); // ｾｯﾄ数24
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(24).getSetsuuVal())); // ｾｯﾄ数25
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(25).getSetsuuVal())); // ｾｯﾄ数26
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(26).getSetsuuVal())); // ｾｯﾄ数27
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(27).getSetsuuVal())); // ｾｯﾄ数28
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(28).getSetsuuVal())); // ｾｯﾄ数29
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(29).getSetsuuVal())); // ｾｯﾄ数30
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(30).getSetsuuVal())); // ｾｯﾄ数31
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(31).getSetsuuVal())); // ｾｯﾄ数32
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(32).getSetsuuVal())); // ｾｯﾄ数33
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(33).getSetsuuVal())); // ｾｯﾄ数34
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(34).getSetsuuVal())); // ｾｯﾄ数35
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(35).getSetsuuVal())); // ｾｯﾄ数36
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(36).getSetsuuVal())); // ｾｯﾄ数37
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(37).getSetsuuVal())); // ｾｯﾄ数38
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(38).getSetsuuVal())); // ｾｯﾄ数39
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(makuatsuDataList.get(39).getSetsuuVal())); // ｾｯﾄ数40
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(0).getBikouVal())); // 備考1
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(1).getBikouVal())); // 備考2
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(2).getBikouVal())); // 備考3
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(3).getBikouVal())); // 備考4
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(4).getBikouVal())); // 備考5
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(5).getBikouVal())); // 備考6
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(6).getBikouVal())); // 備考7
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(7).getBikouVal())); // 備考8
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(8).getBikouVal())); // 備考9
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(9).getBikouVal())); // 備考10
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(10).getBikouVal())); // 備考11
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(11).getBikouVal())); // 備考12
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(12).getBikouVal())); // 備考13
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(13).getBikouVal())); // 備考14
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(14).getBikouVal())); // 備考15
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(15).getBikouVal())); // 備考16
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(16).getBikouVal())); // 備考17
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(17).getBikouVal())); // 備考18
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(18).getBikouVal())); // 備考19
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(19).getBikouVal())); // 備考20
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(20).getBikouVal())); // 備考21
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(21).getBikouVal())); // 備考22
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(22).getBikouVal())); // 備考23
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(23).getBikouVal())); // 備考24
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(24).getBikouVal())); // 備考25
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(25).getBikouVal())); // 備考26
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(26).getBikouVal())); // 備考27
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(27).getBikouVal())); // 備考28
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(28).getBikouVal())); // 備考29
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(29).getBikouVal())); // 備考30
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(30).getBikouVal())); // 備考31
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(31).getBikouVal())); // 備考32
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(32).getBikouVal())); // 備考33
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(33).getBikouVal())); // 備考34
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(34).getBikouVal())); // 備考35
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(35).getBikouVal())); // 備考36
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(36).getBikouVal())); // 備考37
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(37).getBikouVal())); // 備考38
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(38).getBikouVal())); // 備考39
        params.add(DBUtil.stringToStringObject(makuatsuDataList.get(39).getBikouVal())); // 備考40

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
     * 積層・RSUS_ｻﾌﾞ画面(sub_sr_rsussek)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM sub_sr_rsussek "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 検索条件
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [積層・RSUS_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_rsussek "
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B005Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B005Const.KAISHI_TIME);
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
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B005Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B005Const.SHURYOU_TIME);
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
     * @param srRsussekData 積層・RSUSデータ
     * @return DB値
     */
    private String getSrRsussekItemData(String itemId, SrRsussek srRsussekData) {
        switch (itemId) {
            // ｽﾘｯﾌﾟﾛｯﾄNo
            case GXHDO101B005Const.SLIP_LOTNO:
                return StringUtil.nullToBlank(srRsussekData.getTapelotno());
            // ﾛｰﾙNo1
            case GXHDO101B005Const.ROLL_NO1:
                return StringUtil.nullToBlank(srRsussekData.getTaperollno1());
            // ﾛｰﾙNo2
            case GXHDO101B005Const.ROLL_NO2:
                return StringUtil.nullToBlank(srRsussekData.getTaperollno2());
            // ﾛｰﾙNo3
            case GXHDO101B005Const.ROLL_NO3:
                return StringUtil.nullToBlank(srRsussekData.getTaperollno3());
            // 原料記号
            case GXHDO101B005Const.GENRYO_KIGOU:
                return StringUtil.nullToBlank(srRsussekData.getGenryoukigou());
            // ＰＥＴフィルム種類
            case GXHDO101B005Const.PET_FILM_SHURUI:
                return StringUtil.nullToBlank(srRsussekData.getPetfilmsyurui());
            // 固着シート貼付り機
            case GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI:
                return StringUtil.nullToBlank(srRsussekData.getKotyakugouki());
            // 固着シート
            case GXHDO101B005Const.KOTYAKU_SHEET:
                return StringUtil.nullToBlank(srRsussekData.getKotyakusheet());
            // 下端子号機
            case GXHDO101B005Const.SHITA_TANSHI_GOUKI:
                return StringUtil.nullToBlank(srRsussekData.getShitatanshigouki());
            // 上端子号機
            case GXHDO101B005Const.UE_TANSHI_GOUKI:
                return StringUtil.nullToBlank(srRsussekData.getUwatanshigouki());
            // 下端子ﾌﾞｸ抜き
            case GXHDO101B005Const.SHITA_TANSHI_BUKUNUKI:
                switch (StringUtil.nullToBlank(srRsussekData.getShitatanshibukunuki())) {
                    case "0":
                        return "未実施";
                    case "1":
                        return "実施";
                    default:
                        return "";
                }
            // 積層号機
            case GXHDO101B005Const.SEKISOU_GOKI:
                return StringUtil.nullToBlank(srRsussekData.getGoki());
            // 下端子
            case GXHDO101B005Const.SHITA_TANSHI:
                return StringUtil.nullToBlank(srRsussekData.getShitatanshi());
            // 上端子
            case GXHDO101B005Const.UWE_TANSHI:
                return StringUtil.nullToBlank(srRsussekData.getUwatanshi());
            // 処理セット数
            case GXHDO101B005Const.SYORI_SET_SU:
                return StringUtil.nullToBlank(srRsussekData.getSyorisetsuu());
            // 良品セット数
            case GXHDO101B005Const.RYOUHIN_SET_SU:
                return StringUtil.nullToBlank(srRsussekData.getRyouhinsetsuu());
            // 外観確認1
            case GXHDO101B005Const.GAIKAN_KAKUNIN1:
                return StringUtil.nullToBlank(srRsussekData.getGaikankakunin1());
            // 外観確認2
            case GXHDO101B005Const.GAIKAN_KAKUNIN2:
                return StringUtil.nullToBlank(srRsussekData.getGaikankakunin2());
            // 外観確認3
            case GXHDO101B005Const.GAIKAN_KAKUNIN3:
                return StringUtil.nullToBlank(srRsussekData.getGaikankakunin3());
            // 外観確認4
            case GXHDO101B005Const.GAIKAN_KAKUNIN4:
                return StringUtil.nullToBlank(srRsussekData.getGaikankakunin4());
            // 開始日
            case GXHDO101B005Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srRsussekData.getKaisinichiji(), "yyMMdd");
            // 開始時刻
            case GXHDO101B005Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srRsussekData.getKaisinichiji(), "HHmm");
            // 開始担当者
            case GXHDO101B005Const.KAISHI_TANTOUSHA:
                return StringUtil.nullToBlank(srRsussekData.getTantosya());
            // 終了日
            case GXHDO101B005Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srRsussekData.getSyuryonichiji(), "yyMMdd");
            // 終了時刻
            case GXHDO101B005Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srRsussekData.getSyuryonichiji(), "HHmm");
            // 終了担当者
            case GXHDO101B005Const.SHURYOU_TANTOUSHA:
                return StringUtil.nullToBlank(srRsussekData.getEndtantousyacode());
            // 端子テープ種類
            case GXHDO101B005Const.TANSHI_TAPE_SHURUI:
                switch (StringUtil.nullToBlank(srRsussekData.getTanshitapesyurui())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 瞬時加熱時間
            case GXHDO101B005Const.SHUNJI_KANETSU_TIME:
                return StringUtil.nullToBlank(srRsussekData.getSkjikan());
            // タクト
            case GXHDO101B005Const.TAKT:
                return StringUtil.nullToBlank(srRsussekData.getTakuto());
            // 先行ロットNo
            case GXHDO101B005Const.SENKOU_LOT_NO:
                return StringUtil.nullToBlank(srRsussekData.getSkojyo() + srRsussekData.getSlotno() + srRsussekData.getSedaban());                
            // 剥離NG回数
            case GXHDO101B005Const.HAKURI_NG_KAISU:
                return StringUtil.nullToBlank(srRsussekData.getHngkaisuu());
            // 剥離NG_AVE
            case GXHDO101B005Const.HAKURI_NG_AVE:
                return StringUtil.nullToBlank(srRsussekData.getHngkaisuuave());
            // 画処NG回数
            case GXHDO101B005Const.GASHO_NG_KAISU:
                return StringUtil.nullToBlank(srRsussekData.getGngkaisuu());
            // 画処NG_AVE
            case GXHDO101B005Const.GASHO_NG_AVE:
                return StringUtil.nullToBlank(srRsussekData.getGngkaisuuave());
            // 備考1
            case GXHDO101B005Const.BIKOU1:
                return StringUtil.nullToBlank(srRsussekData.getBiko1());
            // 備考2
            case GXHDO101B005Const.BIKOU2:
                return StringUtil.nullToBlank(srRsussekData.getBikou2());
            //KCPNo
            case GXHDO101B005Const.KCPNO:
                return StringUtil.nullToBlank(srRsussekData.getKcpno());

            default:
                return null;

        }
    }

    /**
     * 積層・RSUS_仮登録(tmp_sr_rsussek)登録処理(削除時)
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
    private void insertDeleteDataTmpSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_rsussek ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4"
                + ",EndTantousyacode,TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,revision,deleteflag"
                + ") SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,?,?,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4"
                + ",EndTantousyacode,TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2"
                + ",?,? "
                + "FROM sr_rsussek "
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
     * 積層・RSUS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rsussek)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSubSrRsussek(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_rsussek ("
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10,setsuu11"
                + ",setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22,setsuu23"
                + ",setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32,setsuu33,setsuu34,setsuu35"
                + ",setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8,bikou9"
                + ",bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17,bikou18,bikou19,bikou20,bikou21,bikou22,bikou23"
                + ",bikou24,bikou25,bikou26,bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37"
                + ",bikou38,bikou39,bikou40,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "kojyo,lotno,edaban,setsuu1,setsuu2,setsuu3,setsuu4,setsuu5,setsuu6,setsuu7,setsuu8,setsuu9,setsuu10,setsuu11"
                + ",setsuu12,setsuu13,setsuu14,setsuu15,setsuu16,setsuu17,setsuu18,setsuu19,setsuu20,setsuu21,setsuu22,setsuu23"
                + ",setsuu24,setsuu25,setsuu26,setsuu27,setsuu28,setsuu29,setsuu30,setsuu31,setsuu32,setsuu33,setsuu34,setsuu35"
                + ",setsuu36,setsuu37,setsuu38,setsuu39,setsuu40,bikou1,bikou2,bikou3,bikou4,bikou5,bikou6,bikou7,bikou8,bikou9"
                + ",bikou10,bikou11,bikou12,bikou13,bikou14,bikou15,bikou16,bikou17,bikou18,bikou19,bikou20,bikou21,bikou22,bikou23"
                + ",bikou24,bikou25,bikou26,bikou27,bikou28,bikou29,bikou30,bikou31,bikou32,bikou33,bikou34,bikou35,bikou36,bikou37"
                + ",bikou38,bikou39,bikou40"
                + ",?,?,?,? "
                + "FROM sub_sr_rsussek "
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

}
