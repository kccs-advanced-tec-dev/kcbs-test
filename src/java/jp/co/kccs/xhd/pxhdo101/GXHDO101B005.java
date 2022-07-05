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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrRsussek;
import jp.co.kccs.xhd.db.model.SubSrRsussek;
import jp.co.kccs.xhd.model.GXHDO101C006Model;
import jp.co.kccs.xhd.model.GXHDO101C020Model;
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
 * 変更日	2019/03/08<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日       2019/9/18<br>
 * 計画書No     K1811-DS001<br>
 * 変更者       KCSS K.Jo<br>
 * 変更理由     項目追加・変更<br>
 * <br>
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * 変更日	2020/10/15<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	前工程WIPボタンロジックを追加<br>
 * <br>
 * 変更日	2022/06/17<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	項目追加<br>
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
                    GXHDO101B005Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B005Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_TOP,
                    GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_TOP,
                    GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_TOP,
                    GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_TOP,
                    GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM,
                    GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_BOTTOM,
                    GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_BOTTOM,
                    GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_BOTTOM,
                    GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_BOTTOM
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

                // 前工程WIP取込ｻﾌﾞ画面仮登録登録処理
                insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 積層・RSUS_仮登録更新処理
                updateTmpSrRsussek(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 積層・RSUS_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrRsussek(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

                // 前工程WIP取込_ｻﾌﾞ画面仮登録更新処理
                updateTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);
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
        
        // 下端子開始日時、下端子終了日時前後チェック
        FXHDD01 itemShitatanshiKaishiDay = getItemRow(processData.getItemList(), GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY); //下端子開始日
        FXHDD01 itemShitatanshiKaishiTime = getItemRow(processData.getItemList(), GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME); // 下端子開始時刻
        Date shitatanshiKaishiDate = DateUtil.convertStringToDate(itemShitatanshiKaishiDay.getValue(), itemShitatanshiKaishiTime.getValue());
        FXHDD01 itemShitatanshiShuryouDay = getItemRow(processData.getItemList(), GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY); //下端子終了日
        FXHDD01 itemShitatanshiShuryouTime = getItemRow(processData.getItemList(), GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME); //下端子終了時刻
        Date shitatanshiShuryoDate = DateUtil.convertStringToDate(itemShitatanshiShuryouDay.getValue(), itemShitatanshiShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR002 = validateUtil.checkR001(itemShitatanshiKaishiDay.getLabel1(), shitatanshiKaishiDate, itemShitatanshiShuryouDay.getLabel1(), shitatanshiShuryoDate);
        if (!StringUtil.isEmpty(msgCheckR002)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShitatanshiKaishiDay, itemShitatanshiKaishiTime, itemShitatanshiShuryouDay, itemShitatanshiShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR002, true, true, errFxhdd01List);
        }
        
        // 上端子開始日時、上端子終了日時前後チェック
        FXHDD01 itemUwetanshiKaishiDay = getItemRow(processData.getItemList(), GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY); //上端子開始日
        FXHDD01 itemUwetanshiKaishiTime = getItemRow(processData.getItemList(), GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME); // 上端子開始時刻
        Date uwetanshiKaishiDate = DateUtil.convertStringToDate(itemUwetanshiKaishiDay.getValue(), itemUwetanshiKaishiTime.getValue());
        FXHDD01 itemUwetanshiShuryouDay = getItemRow(processData.getItemList(), GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY); //上端子終了日
        FXHDD01 itemUwetanshiShuryouTime = getItemRow(processData.getItemList(), GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME); //上端子終了時刻
        Date uwetanshiShuryoDate = DateUtil.convertStringToDate(itemUwetanshiShuryouDay.getValue(), itemUwetanshiShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR003 = validateUtil.checkR001(itemUwetanshiKaishiDay.getLabel1(), uwetanshiKaishiDate, itemUwetanshiShuryouDay.getLabel1(), uwetanshiShuryoDate);
        if (!StringUtil.isEmpty(msgCheckR003)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemUwetanshiKaishiDay, itemUwetanshiKaishiTime, itemUwetanshiShuryouDay, itemUwetanshiShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR003, true, true, errFxhdd01List);
        }

        // 先行ﾛｯﾄNoチェック処理
        FXHDD01 itemSenkouLotNo = getItemRow(processData.getItemList(), GXHDO101B005Const.SENKOU_LOT_NO); //先行ﾛｯﾄNo
        FXHDD01 itemPrintRollNo = getItemRow(processData.getItemList(), GXHDO101B005Const.PRINT_ROLLNO); //印刷ﾛｰﾙNo.
        String senkouLotNo = StringUtil.nullToBlank(itemSenkouLotNo.getValue());
        if (!StringUtil.isEmpty(senkouLotNo)) {
            if (senkouLotNo.length() > 10) {
                String printRollNo = senkouLotNo.substring(7, 11);
                if (!printRollNo.equals(StringUtil.nullToBlank(itemPrintRollNo.getValue()))) {
                    // エラー発生時
                    List<FXHDD01> errFxhdd01List = Arrays.asList(itemSenkouLotNo);
                    return MessageUtil.getErrorMessageInfo("XHD-000197", true, true, errFxhdd01List, itemSenkouLotNo.getLabel1());
                }
            }
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
            SrRsussek tmpSrRsussek = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                
                // 更新前の値を取得
                List<SrRsussek> srSrRsussekList = getSrRsussekData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srSrRsussekList.isEmpty()) {
                    tmpSrRsussek = srSrRsussekList.get(0);
                }
                
                deleteTmpSrRsussek(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrRsussek(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban);
            }

            // 積層・RSUS_登録処理
            insertSrRsussek(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrRsussek);

            // 積層・RSUS_ｻﾌﾞ画面登録処理
            insertSubSrRsussek(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

            // 前工程WIP取込ｻﾌﾞ画面登録処理
            insertSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

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

            // 前工程WIP取込_ｻﾌﾞ画面更新処理
            updateSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

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

            // 前工程WIP取込_ｻﾌﾞ画面削除処理
            deleteSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

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
                        GXHDO101B005Const.BTN_DELETE_BOTTOM,
                        GXHDO101B005Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B005Const.BTN_DELETE_TOP,
                        GXHDO101B005Const.BTN_UPDATE_TOP,
                        GXHDO101B005Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_TOP,
                        GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_TOP
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
                        GXHDO101B005Const.BTN_INSERT_BOTTOM,
                        GXHDO101B005Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_BOTTOM,
                        GXHDO101B005Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B005Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B005Const.BTN_INSERT_TOP,
                        GXHDO101B005Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_TOP,
                        GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_TOP,
                        GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_TOP
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
            // 下端子開始日時
            case GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_TOP:
            case GXHDO101B005Const.BTN_SHITATANSHI_STARTDATETIME_BOTTOM:
                method = "setShitatanshiKaishiDateTime";
                break;
            // 下端子終了日時
            case GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_TOP:
            case GXHDO101B005Const.BTN_SHITATANSHI_ENDDATETIME_BOTTOM:
                method = "setShitatanshiShuryouDateTime";
                break;
            // 上端子開始日時
            case GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_TOP:
            case GXHDO101B005Const.BTN_UWATANSHI_STARTDATETIME_BOTTOM:
                method = "setUwatanshiKaishiDateTime";
                break;
            // 上端子終了日時
            case GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_TOP:
            case GXHDO101B005Const.BTN_UWATANSHI_ENDDATETIME_BOTTOM:
                method = "setUwatanshiShuryouDateTime";
                break;
            // 前工程WIP
            case GXHDO101B005Const.BTN_WIP_IMPORT_TOP:
            case GXHDO101B005Const.BTN_WIP_IMPORT_BOTTOM:
                method = "openWipImport";
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
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String sLotNo = (String) session.getAttribute("sLotNo");

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

        // 製造条件ﾏｽﾀを取得する
        String kochaku = loadJoken(sekkeiData.get("SEKKEINO").toString(), "設計仕様", "固着ｼｰﾄ", "固着ｼｰﾄ", queryRunnerQcdb);
        String petfilem = loadJoken(sekkeiData.get("SEKKEINO").toString(), "設計仕様", "PETﾌｨﾙﾑ種類", "PETﾌｨﾙﾑ種類", queryRunnerQcdb);

        //上ｶﾊﾞｰﾃｰﾌﾟ
        String ueCoverTape1DataValue = "";
        //下ｶﾊﾞｰﾃｰﾌﾟ
        String shitaCoverTape1DataValue = "";
        //印刷ﾛｰﾙNo
        String rollnoDataValue = "";

        List<String> checkListDataVal = new ArrayList<>();
        
        //2.上ｶﾊﾞｰﾃｰﾌﾟ対象項目の決定
        checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"CT",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    ueCoverTape1DataValue = checkListDataVal.get(i);
                }
            }
        }

        //3.下ｶﾊﾞｰﾃｰﾌﾟ対象項目の決定
        checkListDataVal.clear();
        checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"CB",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    shitaCoverTape1DataValue = checkListDataVal.get(i);
                }
            }
        }

        //4.印刷ﾛｰﾙNo対象項目の決定
        checkListDataVal.clear();
        checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"EA",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    rollnoDataValue = checkListDataVal.get(i);
                }
            }
        }

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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, kochaku, sekkeiData, petfilem)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo, 
                sLotNo,ueCoverTape1DataValue,shitaCoverTape1DataValue,rollnoDataValue);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }

    /**
     * 関連付けMapに定義されている項目が用途データで['CT','CB','EA']が存在しない場合エラーとしエラー情報を返す
     * ※関連付けMapには設計データに持っている項目IDが設定されていること
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param mapYoutoAssociation 用途関連付けMap
     * @param youtoType 用途データ型
     * @param mapSekkeiAssociation 設計データ関連付けMap
     * @return エラーメッセージリスト
     */
    public List<String> checkYoutoItems(ProcessData processData,Map<String, String> sekkeiData,
            Map<String, String> mapYoutoAssociation,String youtoType, Map<String, String> mapSekkeiAssociation) {

        List<String> retListData =  new ArrayList<>();
        boolean checkExistFlag = false;
        boolean checkCTCBExistFlag = false;
        String sekkeiDataKey = "";
        String checkSyuruiData = "";
        String checkAtumiData = "";
        String checkMaisuuData = "";
        String checkRollnoData = "";
        String checkTlotData = "";

        for (Map.Entry<String, String> entry : mapYoutoAssociation.entrySet()) {
            String checkData = "";
            if (sekkeiData.get(entry.getKey()) != null) {
                checkData = String.valueOf(sekkeiData.get(entry.getKey()));
            }

            if (youtoType.equals(checkData)) {
                checkExistFlag = true;
                sekkeiDataKey = entry.getKey();
                break;
            }
        }

        if(!checkExistFlag){
            retListData.add("ERROR");
            if(null != youtoType)switch (youtoType) {
                case "CT":
                    retListData.add(MessageUtil.getMessage("XHD-000100"));
                    break;
                case "CB":
                    retListData.add(MessageUtil.getMessage("XHD-000101"));
                    break;
                case "EA":
                    retListData.add(MessageUtil.getMessage("XHD-000104"));
                    break;
                default:
                    break;
            }
            return retListData;
        }

        String sekkeiDataRowNo = sekkeiDataKey.substring(5);
        String syuruiDataKey = "SYURUI" + sekkeiDataRowNo;
        String atumiDataKey = "ATUMI" + sekkeiDataRowNo;
        String maisuuDataKey = "MAISUU" + sekkeiDataRowNo;
        String rollnoDataKey = "ROLLNO" + sekkeiDataRowNo;
        String tlotDataKey = "TLOT" + sekkeiDataRowNo;

        checkSyuruiData = StringUtil.nullToBlank(sekkeiData.get(syuruiDataKey));
        checkAtumiData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(atumiDataKey)));
        checkMaisuuData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(maisuuDataKey)));
        checkRollnoData = StringUtil.nullToBlank(sekkeiData.get(rollnoDataKey));
        checkTlotData = StringUtil.nullToBlank(sekkeiData.get(tlotDataKey));

        if("CT".equals(youtoType) || "CB".equals(youtoType)){
            if ("".equals(checkSyuruiData) || "null".equals(checkSyuruiData)) {
                checkCTCBExistFlag = true;
                retListData.add("ERROR");
                for (Map.Entry<String, String> entry : mapSekkeiAssociation.entrySet()) {
                    if (syuruiDataKey.equals(entry.getKey())) {
                        retListData.add(MessageUtil.getMessage("XHD-000021", entry.getKey(), entry.getValue()));
                        break;
                    }
                }
            }

            if ("".equals(checkAtumiData) || "null".equals(checkAtumiData)) {
                if(!checkCTCBExistFlag){
                    retListData.add("ERROR");
                    checkCTCBExistFlag = true;
                }
                for (Map.Entry<String, String> entry : mapSekkeiAssociation.entrySet()) {
                    if (atumiDataKey.equals(entry.getKey())) {
                        retListData.add(MessageUtil.getMessage("XHD-000021", entry.getKey(), entry.getValue()));
                        break;
                    }
                }
            }

            if ("".equals(checkMaisuuData) || "null".equals(checkMaisuuData)) {
                if(!checkCTCBExistFlag){
                    retListData.add("ERROR");
                    checkCTCBExistFlag = true;
                }
                for (Map.Entry<String, String> entry : mapSekkeiAssociation.entrySet()) {
                    if (maisuuDataKey.equals(entry.getKey())) {
                        retListData.add(MessageUtil.getMessage("XHD-000021", entry.getKey(), entry.getValue()));
                        break;
                    }
                }
            }

            if(!checkCTCBExistFlag){
                retListData.add("OK");
                String retCTCBValue = checkSyuruiData + "  " + checkTlotData + "  " + checkAtumiData + "μm×" + checkMaisuuData + "枚";
                retListData.add(retCTCBValue);
            }
            return retListData;
        }else if("EA".equals(youtoType)){
            if ("".equals(checkRollnoData) || "null".equals(checkRollnoData)) {
                retListData.add("ERROR");
                for (Map.Entry<String, String> entry : mapSekkeiAssociation.entrySet()) {
                    if (rollnoDataKey.equals(entry.getKey())) {
                        retListData.add(MessageUtil.getMessage("XHD-000021", entry.getKey(), entry.getValue()));
                        break;
                    }
                }
            }else{
                retListData.add("OK");
                retListData.add(checkRollnoData);
                return retListData;
            }
        }

        return retListData;
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
     * @param ueCoverTape1 上カバーテープ１
     * @param shitaCoverTape1 下カバーテープ１
     * @param rollno 印刷ﾛｰﾙNo
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData,
            Map shikakariData, String lotNo, String sLotNo,String ueCoverTape1,String shitaCoverTape1,String rollno) {

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
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")) + "  " + StringUtil.nullToBlank(sekkeiData.get("ELOT")));

        // 積層数
        this.setItemData(processData, GXHDO101B005Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B005Const.UE_COVER_TAPE1, ueCoverTape1);

        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B005Const.SHITA_COVER_TAPE1, shitaCoverTape1);

        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
        this.setItemData(processData, GXHDO101B005Const.RETSU_GYOU, lRetsu + "×" + wRetsu);

        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B005Const.PITCH, lSun + "×" + wSun);

        // 電極ペースト
        this.setItemData(processData, GXHDO101B005Const.DENKYOKU_PASTE, getKikakuchiValue(processData.getItemList(), GXHDO101B005Const.DENKYOKU_PASTE));
        
        // 積層スライド量
        this.setItemData(processData, GXHDO101B005Const.SEKISOU_SLIDE_RYOU, StringUtil.nullToBlank(sekkeiData.get("ABSlide")));
        
        // 最上層スライド量
        this.setItemData(processData, GXHDO101B005Const.LAST_LAYER_SLIDE_RYO, StringUtil.nullToBlank(sekkeiData.get("LASTLAYERSLIDERYO")));
        
        // 先行ロットNo
        if ("".equals(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())){
            this.setItemData(processData, GXHDO101B005Const.SENKOU_LOT_NO, sLotNo);
        }

        // 印刷ﾛｰﾙNo
        this.setItemData(processData, GXHDO101B005Const.PRINT_ROLLNO, rollno);
        
        //製版名
        this.setItemData(processData, GXHDO101B005Const.PATERN, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));
        
        //取り個数
        this.setItemData(processData, GXHDO101B005Const.TORIKOSUU, StringUtil.nullToBlank(sekkeiData.get("TORIKOSUU")));
        
        //連続積層枚数
        this.setItemData(processData, GXHDO101B005Const.RENZOKUSEKISOUMAISUU, StringUtil.nullToBlank(sekkeiData.get("RENZOKUINSATUN")));
        
        // B層補正量
        this.setItemData(processData, GXHDO101B005Const.BSOUHOSEIRYOU, getKikakuchiValue(processData.getItemList(), GXHDO101B005Const.BSOUHOSEIRYOU));
        
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
            String lotNo, String formId, String kochaku, Map sekkeiData, String petfilem) throws SQLException {

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
                
                // 前工程WIP取込画面データ設定
                setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

                // 固着ｼｰﾄ
                this.setItemData(processData, GXHDO101B005Const.KOTYAKU_SHEET, kochaku);

                // PETﾌｨﾙﾑ種類
                this.setItemData(processData, GXHDO101B005Const.PET_FILM_SHURUI, petfilem);
                // ﾃｰﾌﾟ取得
                String eaTape = StringUtil.nullToBlank(sekkeiData.get("ETAPE"));
                if (eaTape != null && !eaTape.equals("")) {
                    String eaTapeValue[] = eaTape.split("-", 2);
                    if (eaTapeValue.length == 2) {
                        // ｽﾘｯﾌﾟﾛｯﾄ
                        this.setItemData(processData, GXHDO101B005Const.SLIP_LOTNO, StringUtil.nullToBlank(eaTapeValue[0]));
                        // ﾛｰﾙNo
                        this.setItemData(processData, GXHDO101B005Const.ROLL_NO1, StringUtil.nullToBlank(eaTapeValue[1]));                    
                    }
                }
                // 原料記号
                this.setItemData(processData, GXHDO101B005Const.GENRYO_KIGOU, StringUtil.nullToBlank(sekkeiData.get("ELOT")));

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
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

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
        // 積層号機
        this.setItemData(processData, GXHDO101B005Const.SEKISOU_GOKI, getSrRsussekItemData(GXHDO101B005Const.SEKISOU_GOKI, srSrRsussekData));
        // 下端子
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI, srSrRsussekData));
        // 下端子開始日
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY, srSrRsussekData));
        // 下端子開始時刻
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME, srSrRsussekData));
        // 下端子終了日
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY, srSrRsussekData));
        // 下端子終了時刻
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME, srSrRsussekData));
        // 下端子担当者
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_TANTOSYA, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_TANTOSYA, srSrRsussekData));
        // 下端子確認者
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_KAKUNINSYA, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_KAKUNINSYA, srSrRsussekData));
        // 下端子備考
        this.setItemData(processData, GXHDO101B005Const.SHITA_TANSHI_BIKO, getSrRsussekItemData(GXHDO101B005Const.SHITA_TANSHI_BIKO, srSrRsussekData));
        // 上端子
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI, srSrRsussekData));
        // 上端子開始日
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY, srSrRsussekData));
        // 上端子開始時刻
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME, srSrRsussekData));
        // 上端子終了日
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY, srSrRsussekData));
        // 上端子終了時刻
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME, srSrRsussekData));
        // 上端子担当者
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_TANTOSYA, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_TANTOSYA, srSrRsussekData));
        // 上端子確認者
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_KAKUNINSYA, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_KAKUNINSYA, srSrRsussekData));
        // 上端子備考
        this.setItemData(processData, GXHDO101B005Const.UWE_TANSHI_BIKO, getSrRsussekItemData(GXHDO101B005Const.UWE_TANSHI_BIKO, srSrRsussekData));
        // 処理セット数
        this.setItemData(processData, GXHDO101B005Const.SYORI_SET_SU, getSrRsussekItemData(GXHDO101B005Const.SYORI_SET_SU, srSrRsussekData));
        // 良品セット数
        this.setItemData(processData, GXHDO101B005Const.RYOUHIN_SET_SU, getSrRsussekItemData(GXHDO101B005Const.RYOUHIN_SET_SU, srSrRsussekData));
        // 外観確認1
        this.setItemData(processData, GXHDO101B005Const.GAIKAN_KAKUNIN1, getSrRsussekItemData(GXHDO101B005Const.GAIKAN_KAKUNIN1, srSrRsussekData));
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
        // 瞬時加熱時間
        this.setItemData(processData, GXHDO101B005Const.SHUNJI_KANETSU_TIME, getSrRsussekItemData(GXHDO101B005Const.SHUNJI_KANETSU_TIME, srSrRsussekData));
        // タクト
        this.setItemData(processData, GXHDO101B005Const.TAKT, getSrRsussekItemData(GXHDO101B005Const.TAKT, srSrRsussekData));
        // ﾍｯﾄﾞNo
        this.setItemData(processData, GXHDO101B005Const.HEADNO, getSrRsussekItemData(GXHDO101B005Const.HEADNO, srSrRsussekData));
        // SUS板枚数
        this.setItemData(processData, GXHDO101B005Const.SUSITAMAISU, getSrRsussekItemData(GXHDO101B005Const.SUSITAMAISU, srSrRsussekData));
        // 先行ロットNo
        this.setItemData(processData, GXHDO101B005Const.SENKOU_LOT_NO, getSrRsussekItemData(GXHDO101B005Const.SENKOU_LOT_NO, srSrRsussekData));
        // 備考1
        this.setItemData(processData, GXHDO101B005Const.BIKOU1, getSrRsussekItemData(GXHDO101B005Const.BIKOU1, srSrRsussekData));
        // 備考2
        this.setItemData(processData, GXHDO101B005Const.BIKOU2, getSrRsussekItemData(GXHDO101B005Const.BIKOU2, srSrRsussekData));
        // 電極製版ﾛｯﾄNo
        this.setItemData(processData, GXHDO101B005Const.ELOTNO, getSrRsussekItemData(GXHDO101B005Const.ELOTNO, srSrRsussekData));
        // 最上層担当者
        this.setItemData(processData, GXHDO101B005Const.LASTLAYERTANTOSYA, getSrRsussekItemData(GXHDO101B005Const.LASTLAYERTANTOSYA, srSrRsussekData));
        // 最上層備考
        this.setItemData(processData, GXHDO101B005Const.LASTLAYERBIKO, getSrRsussekItemData(GXHDO101B005Const.LASTLAYERBIKO, srSrRsussekData));
        // 製版名
        this.setItemData(processData, GXHDO101B005Const.PATERN, getSrRsussekItemData(GXHDO101B005Const.PATERN, srSrRsussekData));
        // 取り個数
        this.setItemData(processData, GXHDO101B005Const.TORIKOSUU, getSrRsussekItemData(GXHDO101B005Const.TORIKOSUU, srSrRsussekData));
        // 連続積層枚数
        this.setItemData(processData, GXHDO101B005Const.RENZOKUSEKISOUMAISUU, getSrRsussekItemData(GXHDO101B005Const.RENZOKUSEKISOUMAISUU, srSrRsussekData));
        // Y軸補正量
        this.setItemData(processData, GXHDO101B005Const.YJIKUHOSEIRYOU, getSrRsussekItemData(GXHDO101B005Const.YJIKUHOSEIRYOU, srSrRsussekData));
        // 電極ｽﾀｰﾄ確認者
        this.setItemData(processData, GXHDO101B005Const.KAKUNINSYA, getSrRsussekItemData(GXHDO101B005Const.KAKUNINSYA, srSrRsussekData));
        // 積層圧力
        this.setItemData(processData, GXHDO101B005Const.SEKISOATURYOKU, getSrRsussekItemData(GXHDO101B005Const.SEKISOATURYOKU, srSrRsussekData));
        // ﾃｰﾌﾟ使い切り
        this.setItemData(processData, GXHDO101B005Const.TAPETSUKAIKIRI, getSrRsussekItemData(GXHDO101B005Const.TAPETSUKAIKIRI, srSrRsussekData));
        // 次ﾛｯﾄへ
        this.setItemData(processData, GXHDO101B005Const.JILOTHE, getSrRsussekItemData(GXHDO101B005Const.JILOTHE, srSrRsussekData));
        // 備考3
        this.setItemData(processData, GXHDO101B005Const.BIKOU3, getSrRsussekItemData(GXHDO101B005Const.BIKOU3, srSrRsussekData));
        // 備考4
        this.setItemData(processData, GXHDO101B005Const.BIKOU4, getSrRsussekItemData(GXHDO101B005Const.BIKOU4, srSrRsussekData));
        // 備考5
        this.setItemData(processData, GXHDO101B005Const.BIKOU5, getSrRsussekItemData(GXHDO101B005Const.BIKOU5, srSrRsussekData));
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
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("SEKKEINO", "設計No");
                put("GENRYOU", "電極ﾃｰﾌﾟ");
                put("ETAPE", "電極ﾃｰﾌﾟ");
                put("EATUMI", "積層数");
                put("SOUSUU", "積層数");
                put("EMAISUU", "積層数");
                put("PATTERN", "電極製版名");
                put("TORIKOSUU", "取り個数");
                put("ABSlide", "積層ｽﾗｲﾄﾞ量");
                put("LASTLAYERSLIDERYO", "最上層ｽﾗｲﾄﾞ量");
                put("RENZOKUINSATUN", "連続積層枚数");
                
            }
        };

        return map;
    }
    
    /**
     * 設計データ関連付けマップ取得(用途関連)
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapSekkeiYotoAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("YOUTO1", "用途1");
                put("YOUTO2", "用途2");
                put("YOUTO3", "用途3");
                put("YOUTO4", "用途4");
                put("YOUTO5", "用途5");
                put("YOUTO6", "用途6");
                put("YOUTO7", "用途7");
                put("YOUTO8", "用途8");
                put("SYURUI1", "種類1");
                put("SYURUI2", "種類2");
                put("SYURUI3", "種類3");
                put("SYURUI4", "種類4");
                put("SYURUI5", "種類5");
                put("SYURUI6", "種類6");
                put("SYURUI7", "種類7");
                put("SYURUI8", "種類8");
                put("ATUMI1", "厚み1");
                put("ATUMI2", "厚み2");
                put("ATUMI3", "厚み3");
                put("ATUMI4", "厚み4");
                put("ATUMI5", "厚み5");
                put("ATUMI6", "厚み6");
                put("ATUMI7", "厚み7");
                put("ATUMI8", "厚み8");
                put("MAISUU1", "枚数1");
                put("MAISUU2", "枚数2");
                put("MAISUU3", "枚数3");
                put("MAISUU4", "枚数4");
                put("MAISUU5", "枚数5");
                put("MAISUU6", "枚数6");
                put("MAISUU7", "枚数7");
                put("MAISUU8", "枚数8");
                put("ROLLNO1", "ﾛｰﾙNo1");
                put("ROLLNO2", "ﾛｰﾙNo2");
                put("ROLLNO3", "ﾛｰﾙNo3");
                put("ROLLNO4", "ﾛｰﾙNo4");
                put("ROLLNO5", "ﾛｰﾙNo5");
                put("ROLLNO6", "ﾛｰﾙNo6");
                put("ROLLNO7", "ﾛｰﾙNo7");
                put("ROLLNO8", "ﾛｰﾙNo8");
            }
        };

        return map;
    }


    /**
     * 用途関連付けマップ取得
     *
     * @return 設計データ関連付けマップ
     */
    private Map getMapYoutoAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("YOUTO1", "用途1");
                put("YOUTO2", "用途2");
                put("YOUTO3", "用途3");
                put("YOUTO4", "用途4");
                put("YOUTO5", "用途5");
                put("YOUTO6", "用途6");
                put("YOUTO7", "用途7");
                put("YOUTO8", "用途8");
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
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,setsuu,tokuisaki,lotkubuncode,ownercode"
                + ",syurui3,atumi3,maisuu3,patern,torikosuu,etape,lretu,wretu,lsun,wsun,epaste,genryou,eatumi,sousuu"
                + ",emaisuu,sekisouslideryo,lastlayerslideryo,renzokusekisoumaisuu,bsouhoseiryou,yjikuhoseiryou"
                + ",syurui2,atumi2,maisuu2,ShitaTanshiKAISINICHIJI,ShitaTanshiSYURYONICHIJI,ShitaTanshiTANTOSYA,"
                + "ShitaTanshiKAKUNINSYA,ShitaTanshiBIKO,UwaTanshiKAISINICHIJI,UwaTanshiSYURYONICHIJI,UwaTanshiTANTOSYA,"
                + "UwaTanshiKAKUNINSYA,UwaTanshiBIKO,HeadNo,SUSitamaisu,lastlayerTANTOSYA,lastlayerBIKO,elotno,revision,'0' AS deleteflag, sekiatsu,"
                + "tapetsukaikiri,jilothe,bikou3,bikou4,bikou5 "
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
        mapping.put("setsuu", "setsuu"); //ｾｯﾄ数
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("syurui3", "syurui3"); //下ｶﾊﾞｰﾃｰﾌﾟ1種類
        mapping.put("atumi3", "atumi3"); //下ｶﾊﾞｰﾃｰﾌﾟ1厚み
        mapping.put("maisuu3", "maisuu3"); //下ｶﾊﾞｰﾃｰﾌﾟ1枚数
        mapping.put("patern", "patern"); //製版名
        mapping.put("torikosuu", "torikosuu"); //取り個数
        mapping.put("etape", "etape"); //電極ﾃｰﾌﾟ
        mapping.put("lretu", "lretu"); //列
        mapping.put("wretu", "wretu"); //行
        mapping.put("lsun", "lsun"); //LSUN
        mapping.put("wsun", "wsun"); //WSUN
        mapping.put("epaste", "epaste"); //電極ﾍﾟｰｽﾄ
        mapping.put("genryou", "genryou"); //原料
        mapping.put("eatumi", "eatumi"); //電極厚み
        mapping.put("sousuu", "sousuu"); //総数
        mapping.put("emaisuu", "emaisuu"); //電極枚数
        mapping.put("sekisouslideryo", "sekisouslideryo"); //積層ｽﾗｲﾄﾞ量
        mapping.put("lastlayerslideryo", "lastlayerslideryo"); //最上層ｽﾗｲﾄﾞ量
        mapping.put("renzokusekisoumaisuu", "renzokusekisoumaisuu"); //連続積層枚数
        mapping.put("bsouhoseiryou", "bsouhoseiryou"); //B層補正量
        mapping.put("yjikuhoseiryou", "yjikuhoseiryou"); //Y軸補正量
        mapping.put("syurui2", "syurui2"); //上ｶﾊﾞｰﾃｰﾌﾟ1種類
        mapping.put("atumi2", "atumi2"); //上ｶﾊﾞｰﾃｰﾌﾟ1厚み
        mapping.put("maisuu2", "maisuu2"); //上ｶﾊﾞｰﾃｰﾌﾟ1枚数
        mapping.put("ShitaTanshiKAISINICHIJI", "shitatanshikaisinichiji"); //下端子開始日時
        mapping.put("ShitaTanshiSYURYONICHIJI", "shitatanshisyuryonichiji"); //下端子終了日時
        mapping.put("ShitaTanshiTANTOSYA", "shitatanshitantosya"); //下端子担当者
        mapping.put("ShitaTanshiKAKUNINSYA", "shitatanshikakuninsya"); //下端子確認者
        mapping.put("ShitaTanshiBIKO", "shitatanshibiko"); //下端子備考
        mapping.put("UwaTanshiKAISINICHIJI", "uwatanshikaisinichiji"); //上端子開始日時
        mapping.put("UwaTanshiSYURYONICHIJI", "uwatanshisyuryonichiji"); //上端子終了日時
        mapping.put("UwaTanshiTANTOSYA", "uwatanshitantosya"); //上端子担当者
        mapping.put("UwaTanshiKAKUNINSYA", "uwatanshikakuninsya"); //上端子確認者
        mapping.put("UwaTanshiBIKO", "uwatanshibiko"); //上端子備考
        mapping.put("HeadNo", "headno"); //ﾍｯﾄﾞNo
        mapping.put("SUSitamaisu", "susitamaisu"); //SUS板枚数
        mapping.put("lastlayerTANTOSYA", "lastlayertantosya"); //最上層担当者
        mapping.put("lastlayerBIKO", "lastlayerbiko"); //最上層備考
        mapping.put("elotno", "elotno"); //電極製版ﾛｯﾄNo
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("sekiatsu", "sekiatsu"); //削除ﾌﾗｸﾞ
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); //ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); //次ﾛｯﾄへ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5

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
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,setsuu,tokuisaki,lotkubuncode,ownercode"
                + ",syurui3,atumi3,maisuu3,patern,torikosuu,etape,lretu,wretu,lsun,wsun,epaste,genryou,eatumi,sousuu"
                + ",emaisuu,sekisouslideryo,lastlayerslideryo,renzokusekisoumaisuu,bsouhoseiryou,yjikuhoseiryou"
                + ",syurui2,atumi2,maisuu2,ShitaTanshiKAISINICHIJI,ShitaTanshiSYURYONICHIJI,ShitaTanshiTANTOSYA,"
                + "ShitaTanshiKAKUNINSYA,ShitaTanshiBIKO,UwaTanshiKAISINICHIJI,UwaTanshiSYURYONICHIJI,UwaTanshiTANTOSYA,"
                + "UwaTanshiKAKUNINSYA,UwaTanshiBIKO,HeadNo,SUSitamaisu,lastlayerTANTOSYA,lastlayerBIKO,elotno,revision,deleteflag,sekiatsu,"
                + "tapetsukaikiri,jilothe,bikou3,bikou4,bikou5 "
                + "  FROM tmp_sr_rsussek "
                + " WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND deleteflag = ? ";
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
        mapping.put("setsuu", "setsuu"); //ｾｯﾄ数
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("syurui3", "syurui3"); //下ｶﾊﾞｰﾃｰﾌﾟ1種類
        mapping.put("atumi3", "atumi3"); //下ｶﾊﾞｰﾃｰﾌﾟ1厚み
        mapping.put("maisuu3", "maisuu3"); //下ｶﾊﾞｰﾃｰﾌﾟ1枚数
        mapping.put("patern", "patern"); //製版名
        mapping.put("torikosuu", "torikosuu"); //取り個数
        mapping.put("etape", "etape"); //電極ﾃｰﾌﾟ
        mapping.put("lretu", "lretu"); //列
        mapping.put("wretu", "wretu"); //行
        mapping.put("lsun", "lsun"); //LSUN
        mapping.put("wsun", "wsun"); //WSUN
        mapping.put("epaste", "epaste"); //電極ﾍﾟｰｽﾄ
        mapping.put("genryou", "genryou"); //原料
        mapping.put("eatumi", "eatumi"); //電極厚み
        mapping.put("sousuu", "sousuu"); //総数
        mapping.put("emaisuu", "emaisuu"); //電極枚数
        mapping.put("sekisouslideryo", "sekisouslideryo"); //積層ｽﾗｲﾄﾞ量
        mapping.put("lastlayerslideryo", "lastlayerslideryo"); //最上層ｽﾗｲﾄﾞ量
        mapping.put("renzokusekisoumaisuu", "renzokusekisoumaisuu"); //連続積層枚数
        mapping.put("bsouhoseiryou", "bsouhoseiryou"); //B層補正量
        mapping.put("yjikuhoseiryou", "yjikuhoseiryou"); //Y軸補正量
        mapping.put("syurui2", "syurui2"); //上ｶﾊﾞｰﾃｰﾌﾟ1種類
        mapping.put("atumi2", "atumi2"); //上ｶﾊﾞｰﾃｰﾌﾟ1厚み
        mapping.put("maisuu2", "maisuu2"); //上ｶﾊﾞｰﾃｰﾌﾟ1枚数
        mapping.put("ShitaTanshiKAISINICHIJI", "shitatanshikaisinichiji"); //下端子開始日時
        mapping.put("ShitaTanshiSYURYONICHIJI", "shitatanshisyuryonichiji"); //下端子終了日時
        mapping.put("ShitaTanshiTANTOSYA", "shitatanshitantosya"); //下端子担当者
        mapping.put("ShitaTanshiKAKUNINSYA", "shitatanshikakuninsya"); //下端子確認者
        mapping.put("ShitaTanshiBIKO", "shitatanshibiko"); //下端子備考
        mapping.put("UwaTanshiKAISINICHIJI", "uwatanshikaisinichiji"); //上端子開始日時
        mapping.put("UwaTanshiSYURYONICHIJI", "uwatanshisyuryonichiji"); //上端子終了日時
        mapping.put("UwaTanshiTANTOSYA", "uwatanshitantosya"); //上端子担当者
        mapping.put("UwaTanshiKAKUNINSYA", "uwatanshikakuninsya"); //上端子確認者
        mapping.put("UwaTanshiBIKO", "uwatanshibiko"); //上端子備考
        mapping.put("HeadNo", "headno"); //ﾍｯﾄﾞNo
        mapping.put("SUSitamaisu", "susitamaisu"); //SUS板枚数
        mapping.put("lastlayerTANTOSYA", "lastlayertantosya"); //最上層担当者
        mapping.put("lastlayerBIKO", "lastlayerbiko"); //最上層備考
        mapping.put("elotno", "elotno"); //電極製版ﾛｯﾄNo
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("sekiatsu", "sekiatsu"); //削除ﾌﾗｸﾞ
        mapping.put("tapetsukaikiri", "tapetsukaikiri"); //ﾃｰﾌﾟ使い切り
        mapping.put("jilothe", "jilothe"); //次ﾛｯﾄへ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5

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
            
            // 前工程WIP取込画面データ設定
            // ※下記メソッド内で親データの検索実行と値設定を実施
            setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, oyalotEdaban, jotaiFlg);

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
                + " KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN"
                + ",tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki"
                + ",Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi"
                + ",GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,SyoriSetsuu,RyouhinSetsuu,EndTantousyacode,TanshiTapeSyurui"
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,setsuu,tokuisaki,lotkubuncode,ownercode,syurui3,atumi3,maisuu3"
                + ",patern,torikosuu,etape,lretu,wretu,lsun,wsun,epaste,genryou,eatumi,sousuu,emaisuu,sekisouslideryo,lastlayerslideryo"
                + ",renzokusekisoumaisuu,bsouhoseiryou,yjikuhoseiryou,syurui2,atumi2,maisuu2,ShitaTanshiKAISINICHIJI,ShitaTanshiSYURYONICHIJI,"
                + "ShitaTanshiTANTOSYA,ShitaTanshiKAKUNINSYA,ShitaTanshiBIKO,UwaTanshiKAISINICHIJI,UwaTanshiSYURYONICHIJI,UwaTanshiTANTOSYA,"
                + "UwaTanshiKAKUNINSYA,UwaTanshiBIKO,HeadNo,SUSitamaisu,lastlayerTANTOSYA,lastlayerBIKO,elotno,revision,deleteflag,sekiatsu,"
                + "tapetsukaikiri,jilothe,bikou3,bikou4,bikou5 "
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

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
                + "tapelotno = ?,taperollno1 = ?,genryoukigou = ?,petfilmsyurui = ?,Kotyakugouki = ?,Kotyakusheet = ?,"
                + "ShitaTanshigouki = ?,UwaTanshigouki = ?,ShitaTanshi = ?,UwaTanshi = ?,GaikanKakunin1 = ?,"
                + "SyoriSetsuu = ?,RyouhinSetsuu = ?,EndTantousyacode = ?,"
                + "bikou2 = ?,setsuu = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,syurui3 = ?,atumi3 = ?,maisuu3 = ?,"
                + "patern = ?,torikosuu = ?,etape = ?,lretu = ?,wretu = ?,lsun = ?,wsun = ?,epaste = ?,genryou = ?,eatumi = ?,sousuu = ?,emaisuu = ?,sekisouslideryo = ?,"
                + "lastlayerslideryo = ?,renzokusekisoumaisuu = ?,yjikuhoseiryou = ?,syurui2 = ?,atumi2 = ?,maisuu2 = ?,ShitaTanshiKAISINICHIJI = ?,ShitaTanshiSYURYONICHIJI = ?,"
                + "ShitaTanshiTANTOSYA = ?,ShitaTanshiKAKUNINSYA = ?,ShitaTanshiBIKO = ?,UwaTanshiKAISINICHIJI = ?,UwaTanshiSYURYONICHIJI = ?,UwaTanshiTANTOSYA = ?,"
                + "UwaTanshiKAKUNINSYA = ?,UwaTanshiBIKO = ?,HeadNo = ?,SUSitamaisu = ?,lastlayerTANTOSYA = ?,lastlayerBIKO = ?,elotno = ?,revision = ?,deleteflag = ?,sekiatsu = ?,"
                + "tapetsukaikiri = ?,jilothe = ?,bikou3 = ?,bikou4 = ?,bikou5 = ? "
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
        
        //上ｶﾊﾞｰﾃｰﾌﾟ
        String ueCoverTape1 = getItemData(itemList, GXHDO101B005Const.UE_COVER_TAPE1, srRsussekData);
        String strUeCoverTape1Syurui2[] = null;
        String strUeCoverTape1Atumi2[] = null;
        String strSyurui2 = "";
        String strAtumi2 = "";
        String strMaisuu2 = "";

        if(!"".equals(ueCoverTape1) && ueCoverTape1 != null){
            strUeCoverTape1Syurui2 = ueCoverTape1.split("  ", -1);
            //上ｶﾊﾞｰﾃｰﾌﾟ1種類 syurui2
            strSyurui2 = strUeCoverTape1Syurui2[0];
            //上ｶﾊﾞｰﾃｰﾌﾟ1厚み atumi2 "μm×"
            strUeCoverTape1Atumi2 = strUeCoverTape1Syurui2[1].split("μm×", -1);
            strAtumi2 = strUeCoverTape1Atumi2[0];
            //上ｶﾊﾞｰﾃｰﾌﾟ1枚数 maisuu2 "枚"
            strMaisuu2 = strUeCoverTape1Atumi2[1].replaceAll("枚", "");
        }

        //下ｶﾊﾞｰﾃｰﾌﾟ
        String shitaCoverTape1 = getItemData(itemList, GXHDO101B005Const.SHITA_COVER_TAPE1, srRsussekData);
        String strShitaCoverTape1Syurui2[] = null;
        String strShitaCoverTape1Atumi2[] = null;
        String strSyurui3 = "";
        String strAtumi3 = "";
        String strMaisuu3 = "";

        if(!"".equals(shitaCoverTape1) && shitaCoverTape1 != null){
            strShitaCoverTape1Syurui2 = shitaCoverTape1.split("  ", -1);
            //下ｶﾊﾞｰﾃｰﾌﾟ1種類  syurui3
            strSyurui3 = strShitaCoverTape1Syurui2[0];
            //下ｶﾊﾞｰﾃｰﾌﾟ1厚み atumi3 "μm×"
            strShitaCoverTape1Atumi2 = strShitaCoverTape1Syurui2[1].split("μm×", -1);
            strAtumi3 = strShitaCoverTape1Atumi2[0];
            //下ｶﾊﾞｰﾃｰﾌﾟ1枚数 maisuu3 "枚"
            strMaisuu3 = strShitaCoverTape1Atumi2[1].replaceAll("枚", "");
        }
        
        //電極ﾃｰﾌﾟ
        String strDenkyokuTape = getItemData(itemList, GXHDO101B005Const.DENKYOKU_TAPE, srRsussekData);
        String strEtape = "";
        String strDenkyokuTapeSplit[] = null;
        if(!"".equals(strDenkyokuTape) && strDenkyokuTape != null){
            strDenkyokuTapeSplit = strDenkyokuTape.split("  ", -1);
            strEtape = strDenkyokuTapeSplit[0];
        }
        
        String retsuGyou = getItemData(itemList, GXHDO101B005Const.RETSU_GYOU, srRsussekData);
        String strRetsuGyouLretu[] = null;
        //列
        String strLretu = "";
        //行
        String strWretu = "";

        if(!"".equals(retsuGyou) && retsuGyou != null){
            strRetsuGyouLretu = retsuGyou.split("×", -1);
            //列
            strLretu = strRetsuGyouLretu[0];
            //行
            strWretu = strRetsuGyouLretu[1];
        }
        
        String pitch = getItemData(itemList, GXHDO101B005Const.PITCH, srRsussekData);
        String strPitch[] = null;
        //LSUN
        String strLsun = "";
        //WSUN
        String strWsun = "";

        if(!"".equals(pitch) && pitch != null){
            strPitch = pitch.split("×", -1);
            //LSUN
            strLsun = strPitch[0];
            //WSUN
            strWsun = strPitch[1];
        }
        
        String sekisousu = getItemData(itemList, GXHDO101B005Const.SEKISOU_SU, srRsussekData);
        String strSekisousuEatumi[] = null;
        String strSekisousuSousuu[] = null;
        String strEatumi = "";
        String strSousuu = "";
        String strEmaisuu = "";

        if(!"".equals(sekisousu) && sekisousu != null){
            strSekisousuEatumi = sekisousu.split("μm×", -1);
            //電極厚み
            strEatumi = strSekisousuEatumi[0];
            //総数
            strSekisousuSousuu = strSekisousuEatumi[1].split("層  ", -1);
            strSousuu = strSekisousuSousuu[0];
            //電極枚数
            strEmaisuu = strSekisousuSousuu[1].replaceAll("枚", "");
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
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KAKUNINSYA, srRsussekData))); //確認者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.PRINT_ROLLNO, srRsussekData))); //印刷ﾛｰﾙNo
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
        params.add(DBUtil.stringToStringObjectDefaultNull(skojyo)); //先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(slotNo)); //先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(sedaban)); //先行枝番
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SLIP_LOTNO, srRsussekData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.ROLL_NO1, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo1
        if (isInsert) {
            params.add(null); //ﾃｰﾌﾟﾛｰﾙNo2
            params.add(null); //ﾃｰﾌﾟﾛｰﾙNo3  
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GENRYO_KIGOU, srRsussekData))); //原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.PET_FILM_SHURUI, srRsussekData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI, srRsussekData))); //固着シート貼付り機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET, srRsussekData))); //固着シート
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_GOUKI, srRsussekData))); //下端子号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UE_TANSHI_GOUKI, srRsussekData))); //上端子号機  
        if (isInsert) {
            params.add(null); // 下端子ﾌﾞｸ抜き
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI, srRsussekData))); //下端子
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI, srRsussekData))); //上端子
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN1, srRsussekData))); //外観確認1
        if (isInsert) {
            params.add(null); //外観確認2
            params.add(null); //外観確認3
            params.add(null); //外観確認4
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SYORI_SET_SU, srRsussekData))); //処理セット数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.RYOUHIN_SET_SU, srRsussekData))); //良品セット数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHURYOU_TANTOUSHA, srRsussekData))); //終了担当者
        if (isInsert) {
            params.add(null);//端子ﾃｰﾌﾟ種類
            params.add(null);//隔離NG回数
            params.add(null);//剥離NG回数AVE
            params.add(null);//画像NG回数
            params.add(null);//画像NG回数AVE
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.BIKOU2, srRsussekData))); //備考2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SET_SUU, srRsussekData))); //ｾｯﾄ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.KYAKUSAKI, srRsussekData))); //客先
        String lotKbn = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.LOT_KUBUN, srRsussekData));
        String[] spLotKbn = lotKbn.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spLotKbn[0])); //ﾛｯﾄ区分
        String owner = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.OWNER, srRsussekData));
        String[] spOwner = owner.split(":", -1);
        params.add(DBUtil.stringToStringObjectDefaultNull(spOwner[0])); //ｵｰﾅｰ
        params.add(DBUtil.stringToStringObjectDefaultNull(strSyurui3)); //下ｶﾊﾞｰﾃｰﾌﾟ1種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(strAtumi3)); //下ｶﾊﾞｰﾃｰﾌﾟ1厚み
        params.add(DBUtil.stringToIntObjectDefaultNull(strMaisuu3)); //下ｶﾊﾞｰﾃｰﾌﾟ1枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.PATERN, srRsussekData))); //製版名
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.TORIKOSUU, srRsussekData))); //取り個数
        params.add(DBUtil.stringToStringObjectDefaultNull(strEtape));//電極ﾃｰﾌﾟ
        params.add(DBUtil.stringToIntObjectDefaultNull(StringUtil.nullToBlank(strLretu)));//列
        params.add(DBUtil.stringToIntObjectDefaultNull(StringUtil.nullToBlank(strWretu)));//行
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(StringUtil.nullToBlank(strLsun)));//LSUN
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(StringUtil.nullToBlank(strWsun)));//WSUN
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.DENKYOKU_PASTE, srRsussekData))); //電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(strEtape));//原料
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(strEatumi));//電極厚み
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(strSousuu));//総数
        params.add(DBUtil.stringToIntObjectDefaultNull(strEmaisuu));//電極枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SEKISOU_SLIDE_RYOU, srRsussekData))); //積層ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.LAST_LAYER_SLIDE_RYO, srRsussekData))); //最上層ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.RENZOKUSEKISOUMAISUU, srRsussekData))); //連続積層枚数
        if (isInsert) {
            params.add(null); //B層補正量
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.YJIKUHOSEIRYOU, srRsussekData))); //Y軸補正量
        params.add(DBUtil.stringToStringObjectDefaultNull(strSyurui2));//上ｶﾊﾞｰﾃｰﾌﾟ1種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(strAtumi2));//上ｶﾊﾞｰﾃｰﾌﾟ1厚み
        params.add(DBUtil.stringToIntObjectDefaultNull(strMaisuu2));//上ｶﾊﾞｰﾃｰﾌﾟ1枚数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY, srRsussekData),
            getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME, srRsussekData))); //下端子開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY, srRsussekData),
           getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME, srRsussekData))); //下端子終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_TANTOSYA, srRsussekData))); //下端子担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_KAKUNINSYA, srRsussekData))); //下端子確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_BIKO, srRsussekData))); //下端子備考
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY, srRsussekData),
            getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME, srRsussekData))); //上端子開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY, srRsussekData),
           getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME, srRsussekData))); //上端子終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_TANTOSYA, srRsussekData))); //上端子担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_KAKUNINSYA, srRsussekData))); //上端子確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_BIKO, srRsussekData))); //上端子備考
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.HEADNO, srRsussekData))); //ﾍｯﾄﾞNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SUSITAMAISU, srRsussekData))); //SUS板枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.LASTLAYERTANTOSYA, srRsussekData))); //最上層担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.LASTLAYERBIKO, srRsussekData))); //最上層備考
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.ELOTNO, srRsussekData))); //電極製版ﾛｯﾄNo
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SEKISOATURYOKU, srRsussekData)));//積層圧力
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B005Const.TAPETSUKAIKIRI, srRsussekData), null)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B005Const.JILOTHE, srRsussekData), null)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.BIKOU3, srRsussekData))); // 備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.BIKOU4, srRsussekData))); // 備考4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.BIKOU5, srRsussekData))); // 備考5

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
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(0).getBikouVal())); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(1).getBikouVal())); // 備考2
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(2).getBikouVal())); // 備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(3).getBikouVal())); // 備考4
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(4).getBikouVal())); // 備考5
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(5).getBikouVal())); // 備考6
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(6).getBikouVal())); // 備考7
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(7).getBikouVal())); // 備考8
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(8).getBikouVal())); // 備考9
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(9).getBikouVal())); // 備考10
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(10).getBikouVal())); // 備考11
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(11).getBikouVal())); // 備考12
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(12).getBikouVal())); // 備考13
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(13).getBikouVal())); // 備考14
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(14).getBikouVal())); // 備考15
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(15).getBikouVal())); // 備考16
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(16).getBikouVal())); // 備考17
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(17).getBikouVal())); // 備考18
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(18).getBikouVal())); // 備考19
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(19).getBikouVal())); // 備考20
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(20).getBikouVal())); // 備考21
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(21).getBikouVal())); // 備考22
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(22).getBikouVal())); // 備考23
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(23).getBikouVal())); // 備考24
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(24).getBikouVal())); // 備考25
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(25).getBikouVal())); // 備考26
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(26).getBikouVal())); // 備考27
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(27).getBikouVal())); // 備考28
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(28).getBikouVal())); // 備考29
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(29).getBikouVal())); // 備考30
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(30).getBikouVal())); // 備考31
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(31).getBikouVal())); // 備考32
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(32).getBikouVal())); // 備考33
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(33).getBikouVal())); // 備考34
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(34).getBikouVal())); // 備考35
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(35).getBikouVal())); // 備考36
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(36).getBikouVal())); // 備考37
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(37).getBikouVal())); // 備考38
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(38).getBikouVal())); // 備考39
        params.add(DBUtil.stringToStringObjectDefaultNull(hakuriInputDataList.get(39).getBikouVal())); // 備考40

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
                + " KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN"
                + ",tapelotno,taperollno1,taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki"
                + ",Kotyakusheet,ShitaTanshigouki,UwaTanshigouki,ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi"
                + ",GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4,SyoriSetsuu,RyouhinSetsuu,EndTantousyacode,TanshiTapeSyurui"
                + ",HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2,setsuu,tokuisaki,lotkubuncode,ownercode,syurui3,atumi3,maisuu3"
                + ",patern,torikosuu,etape,lretu,wretu,lsun,wsun,epaste,genryou,eatumi,sousuu,emaisuu,sekisouslideryo,lastlayerslideryo"
                + ",renzokusekisoumaisuu,bsouhoseiryou,yjikuhoseiryou,syurui2,atumi2,maisuu2,ShitaTanshiKAISINICHIJI,ShitaTanshiSYURYONICHIJI,"
                + "ShitaTanshiTANTOSYA,ShitaTanshiKAKUNINSYA,ShitaTanshiBIKO,UwaTanshiKAISINICHIJI,UwaTanshiSYURYONICHIJI,UwaTanshiTANTOSYA,"
                + "UwaTanshiKAKUNINSYA,UwaTanshiBIKO,HeadNo,SUSitamaisu,lastlayerTANTOSYA,lastlayerBIKO,elotno,revision,sekiatsu,"
                + "tapetsukaikiri,jilothe,bikou3,bikou4,bikou5"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
                + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        
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
                + "KOSINNICHIJI = ?,SKOJYO = ?,SLOTNO = ?,SEDABAN = ?,tapelotno = ?,taperollno1 = ?,genryoukigou = ?,"
                + "petfilmsyurui = ?,Kotyakugouki = ?,Kotyakusheet = ?,ShitaTanshigouki = ?,UwaTanshigouki = ?,ShitaTanshiBukunuki = ?,"
                + "ShitaTanshi = ?,UwaTanshi = ?,GaikanKakunin1 = ?,SyoriSetsuu = ?,"
                + "RyouhinSetsuu = ?,EndTantousyacode = ?,"
                + "bikou2 = ?,setsuu = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,syurui3 = ?,atumi3 = ?,maisuu3 = ?,patern = ?,torikosuu = ?,"
                + "etape = ?,lretu = ?,wretu = ?,lsun = ?,wsun = ?,epaste = ?,genryou = ?,eatumi = ?,sousuu = ?,emaisuu = ?,sekisouslideryo = ?,"
                + "lastlayerslideryo = ?,renzokusekisoumaisuu = ?,yjikuhoseiryou = ?,syurui2 = ?,atumi2 = ?,maisuu2 = ?,ShitaTanshiKAISINICHIJI = ?,"
                + "ShitaTanshiSYURYONICHIJI = ?,ShitaTanshiTANTOSYA = ?,ShitaTanshiKAKUNINSYA = ?,ShitaTanshiBIKO = ?,UwaTanshiKAISINICHIJI = ?,"
                + "UwaTanshiSYURYONICHIJI = ?,UwaTanshiTANTOSYA = ?,UwaTanshiKAKUNINSYA = ?,UwaTanshiBIKO = ?,HeadNo = ?,SUSitamaisu = ?,"
                + "lastlayerTANTOSYA = ?,lastlayerBIKO = ?,elotno = ?,revision = ?,sekiatsu = ?, "
                + "tapetsukaikiri = ?,jilothe = ?,bikou3 = ?,bikou4 = ?,bikou5 = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

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

        //上ｶﾊﾞｰﾃｰﾌﾟ
        String ueCoverTape1 = getItemData(itemList, GXHDO101B005Const.UE_COVER_TAPE1, srRsussekData);
        String strUeCoverTape1Syurui2[] = null;
        String strUeCoverTape1Atumi2[] = null;
        String strSyurui2 = "";
        String strAtumi2 = "";
        String strMaisuu2 = "";

        if(!"".equals(ueCoverTape1) && ueCoverTape1 != null){
            strUeCoverTape1Syurui2 = ueCoverTape1.split("  ", -1);
            //上ｶﾊﾞｰﾃｰﾌﾟ1種類 syurui2
            strSyurui2 = strUeCoverTape1Syurui2[0];
            //上ｶﾊﾞｰﾃｰﾌﾟ1厚み atumi2 "μm×"
            strUeCoverTape1Atumi2 = strUeCoverTape1Syurui2[1].split("μm×", -1);
            strAtumi2 = strUeCoverTape1Atumi2[0];
            //上ｶﾊﾞｰﾃｰﾌﾟ1枚数 maisuu2 "枚"
            strMaisuu2 = strUeCoverTape1Atumi2[1].replaceAll("枚", "");
        }

        //下ｶﾊﾞｰﾃｰﾌﾟ
        String shitaCoverTape1 = getItemData(itemList, GXHDO101B005Const.SHITA_COVER_TAPE1, srRsussekData);
        String strShitaCoverTape1Syurui2[] = null;
        String strShitaCoverTape1Atumi2[] = null;
        String strSyurui3 = "";
        String strAtumi3 = "";
        String strMaisuu3 = "";

        if(!"".equals(shitaCoverTape1) && shitaCoverTape1 != null){
            strShitaCoverTape1Syurui2 = shitaCoverTape1.split("  ", -1);
            //下ｶﾊﾞｰﾃｰﾌﾟ1種類  syurui3
            strSyurui3 = strShitaCoverTape1Syurui2[0];
            //下ｶﾊﾞｰﾃｰﾌﾟ1厚み atumi3 "μm×"
            strShitaCoverTape1Atumi2 = strShitaCoverTape1Syurui2[1].split("μm×", -1);
            strAtumi3 = strShitaCoverTape1Atumi2[0];
            //下ｶﾊﾞｰﾃｰﾌﾟ1枚数 maisuu3 "枚"
            strMaisuu3 = strShitaCoverTape1Atumi2[1].replaceAll("枚", "");
        }
        
        //電極ﾃｰﾌﾟ
        String strDenkyokuTape = getItemData(itemList, GXHDO101B005Const.DENKYOKU_TAPE, srRsussekData);
        String strEtape = "";
        String strDenkyokuTapeSplit[] = null;
        if(!"".equals(strDenkyokuTape) && strDenkyokuTape != null){
            strDenkyokuTapeSplit = strDenkyokuTape.split("  ", -1);
            strEtape = strDenkyokuTapeSplit[0];
        }
        
        String retsuGyou = getItemData(itemList, GXHDO101B005Const.RETSU_GYOU, srRsussekData);
        String strRetsuGyouLretu[] = null;
        //列
        String strLretu = "";
        //行
        String strWretu = "";

        if(!"".equals(retsuGyou) && retsuGyou != null){
            strRetsuGyouLretu = retsuGyou.split("×", -1);
            //列
            strLretu = strRetsuGyouLretu[0];
            //行
            strWretu = strRetsuGyouLretu[1];
        }
        
        String pitch = getItemData(itemList, GXHDO101B005Const.PITCH, srRsussekData);
        String strPitch[] = null;
        //LSUN
        String strLsun = "";
        //WSUN
        String strWsun = "";

        if(!"".equals(pitch) && pitch != null){
            strPitch = pitch.split("×", -1);
            //LSUN
            strLsun = strPitch[0];
            //WSUN
            strWsun = strPitch[1];
        }
        
        String sekisousu = getItemData(itemList, GXHDO101B005Const.SEKISOU_SU, srRsussekData);
        String strSekisousuEatumi[] = null;
        String strSekisousuSousuu[] = null;
        String strEatumi = "";
        String strSousuu = "";
        String strEmaisuu = "";

        if(!"".equals(sekisousu) && sekisousu != null){
            strSekisousuEatumi = sekisousu.split("μm×", -1);
            //電極厚み
            strEatumi = strSekisousuEatumi[0];
            //総数
            strSekisousuSousuu = strSekisousuEatumi[1].split("層  ", -1);
            strSousuu = strSekisousuSousuu[0];
            //電極枚数
            strEmaisuu = strSekisousuSousuu[1].replaceAll("枚", "");
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
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KAKUNINSYA, srRsussekData))); //確認者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.PRINT_ROLLNO, srRsussekData))); //印刷ﾛｰﾙNo
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
        params.add(DBUtil.stringToStringObject(skojyo)); //先行工場ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(slotNo)); //先行ﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(sedaban)); //先行枝番   
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SLIP_LOTNO, srRsussekData))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo 
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.ROLL_NO1, srRsussekData))); //ﾃｰﾌﾟﾛｰﾙNo1
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟﾛｰﾙNo2
            params.add(""); //ﾃｰﾌﾟﾛｰﾙNo3
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GENRYO_KIGOU, srRsussekData))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.PET_FILM_SHURUI, srRsussekData))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET_HARITSUKEKI, srRsussekData))); //固着シート貼付り機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KOTYAKU_SHEET, srRsussekData))); //固着シート
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_GOUKI, srRsussekData))); //下端子号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.UE_TANSHI_GOUKI, srRsussekData))); //上端子号機 
        params.add(0);// 下端子ﾌﾞｸ抜き
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI, srRsussekData))); //下端子
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI, srRsussekData))); //上端子
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.GAIKAN_KAKUNIN1, srRsussekData))); //外観確認1
        if (isInsert) {
            params.add(""); //外観確認2
            params.add(""); //外観確認3
            params.add(""); //外観確認4
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.SYORI_SET_SU, srRsussekData))); //処理セット数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.RYOUHIN_SET_SU, srRsussekData))); //良品セット数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SHURYOU_TANTOUSHA, srRsussekData))); //終了担当者
        if (isInsert) {
            params.add(0);// 端子テープ種類
            params.add(0); //剥離NG回数
            params.add(0); //剥離NG_AVE
            params.add(0); //画処NG回数
            params.add(0); //画処NG_AVE
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.BIKOU2, srRsussekData))); //備考2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.SET_SUU, srRsussekData))); //ｾｯﾄ数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.KYAKUSAKI, srRsussekData))); //客先
        String lotKbn = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.LOT_KUBUN, srRsussekData));
        String[] spLotKbn = lotKbn.split(":", -1);
        params.add(DBUtil.stringToStringObject(spLotKbn[0])); //ﾛｯﾄ区分
        String owner = StringUtil.nullToBlank(getItemData(itemList, GXHDO101B005Const.OWNER, srRsussekData));
        String[] spOwner = owner.split(":", -1);
        params.add(DBUtil.stringToStringObject(spOwner[0])); //ｵｰﾅｰ
        params.add(DBUtil.stringToStringObject(strSyurui3)); //下ｶﾊﾞｰﾃｰﾌﾟ1種類
        params.add(DBUtil.stringToBigDecimalObject(strAtumi3)); //下ｶﾊﾞｰﾃｰﾌﾟ1厚み
        params.add(DBUtil.stringToIntObject(strMaisuu3)); //下ｶﾊﾞｰﾃｰﾌﾟ1枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.PATERN, srRsussekData))); //製版名
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.TORIKOSUU, srRsussekData))); //取り個数
        params.add(DBUtil.stringToStringObject(strEtape));//電極ﾃｰﾌﾟ
        params.add(DBUtil.stringToIntObject(StringUtil.nullToBlank(strLretu)));//列
        params.add(DBUtil.stringToIntObject(StringUtil.nullToBlank(strWretu)));//行
        params.add(DBUtil.stringToBigDecimalObject(StringUtil.nullToBlank(strLsun)));//LSUN
        params.add(DBUtil.stringToBigDecimalObject(StringUtil.nullToBlank(strWsun)));//WSUN
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.DENKYOKU_PASTE, srRsussekData))); //電極ﾍﾟｰｽﾄ
        params.add(DBUtil.stringToStringObject(strEtape));//原料
        params.add(DBUtil.stringToBigDecimalObject(strEatumi));//電極厚み
        params.add(DBUtil.stringToBigDecimalObject(strSousuu));//総数
        params.add(DBUtil.stringToIntObject(strEmaisuu));//電極枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.SEKISOU_SLIDE_RYOU, srRsussekData))); //積層ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.LAST_LAYER_SLIDE_RYO, srRsussekData))); //最上層ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.RENZOKUSEKISOUMAISUU, srRsussekData))); //連続積層枚数
        if (isInsert) {
            params.add(0); //B層補正量
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B005Const.YJIKUHOSEIRYOU, srRsussekData))); //Y軸補正量
        params.add(DBUtil.stringToStringObject(strSyurui2));//上ｶﾊﾞｰﾃｰﾌﾟ1種類
        params.add(DBUtil.stringToBigDecimalObject(strAtumi2));//上ｶﾊﾞｰﾃｰﾌﾟ1厚み
        params.add(DBUtil.stringToIntObject(strMaisuu2));//上ｶﾊﾞｰﾃｰﾌﾟ1枚数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY, srRsussekData),
            getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME, srRsussekData))); //下端子開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY, srRsussekData),
           getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME, srRsussekData))); //下端子終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_TANTOSYA, srRsussekData))); //下端子担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_KAKUNINSYA, srRsussekData))); //下端子確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SHITA_TANSHI_BIKO, srRsussekData))); //下端子備考
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY, srRsussekData),
            getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME, srRsussekData))); //上端子開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY, srRsussekData),
           getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME, srRsussekData))); //上端子終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_TANTOSYA, srRsussekData))); //上端子担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_KAKUNINSYA, srRsussekData))); //上端子確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.UWE_TANSHI_BIKO, srRsussekData))); //上端子備考
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.HEADNO, srRsussekData))); //ﾍｯﾄﾞNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.SUSITAMAISU, srRsussekData))); //SUS板枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.LASTLAYERTANTOSYA, srRsussekData))); //最上層担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.LASTLAYERBIKO, srRsussekData))); //最上層備考
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B005Const.ELOTNO, srRsussekData))); //電極製版ﾛｯﾄNo
        params.add(newRev); //revision
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B005Const.SEKISOATURYOKU, srRsussekData)));//積層圧力
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B005Const.TAPETSUKAIKIRI, srRsussekData), 9)); // ﾃｰﾌﾟ使い切り
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B005Const.JILOTHE, srRsussekData), 9)); // 次ﾛｯﾄへ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.BIKOU3, srRsussekData))); // 備考3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.BIKOU4, srRsussekData))); // 備考4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B005Const.BIKOU5, srRsussekData))); // 備考5

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
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(0).getSetsuuVal())); // ｾｯﾄ数1
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(1).getSetsuuVal())); // ｾｯﾄ数2
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(2).getSetsuuVal())); // ｾｯﾄ数3
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(3).getSetsuuVal())); // ｾｯﾄ数4
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(4).getSetsuuVal())); // ｾｯﾄ数5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(5).getSetsuuVal())); // ｾｯﾄ数6
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(6).getSetsuuVal())); // ｾｯﾄ数7
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(7).getSetsuuVal())); // ｾｯﾄ数8
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(8).getSetsuuVal())); // ｾｯﾄ数9
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(9).getSetsuuVal())); // ｾｯﾄ数10
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(10).getSetsuuVal())); // ｾｯﾄ数11
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(11).getSetsuuVal())); // ｾｯﾄ数12
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(12).getSetsuuVal())); // ｾｯﾄ数13
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(13).getSetsuuVal())); // ｾｯﾄ数14
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(14).getSetsuuVal())); // ｾｯﾄ数15
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(15).getSetsuuVal())); // ｾｯﾄ数16
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(16).getSetsuuVal())); // ｾｯﾄ数17
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(17).getSetsuuVal())); // ｾｯﾄ数18
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(18).getSetsuuVal())); // ｾｯﾄ数19
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(19).getSetsuuVal())); // ｾｯﾄ数20
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(20).getSetsuuVal())); // ｾｯﾄ数21
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(21).getSetsuuVal())); // ｾｯﾄ数22
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(22).getSetsuuVal())); // ｾｯﾄ数23
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(23).getSetsuuVal())); // ｾｯﾄ数24
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(24).getSetsuuVal())); // ｾｯﾄ数25
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(25).getSetsuuVal())); // ｾｯﾄ数26
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(26).getSetsuuVal())); // ｾｯﾄ数27
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(27).getSetsuuVal())); // ｾｯﾄ数28
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(28).getSetsuuVal())); // ｾｯﾄ数29
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(29).getSetsuuVal())); // ｾｯﾄ数30
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(30).getSetsuuVal())); // ｾｯﾄ数31
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(31).getSetsuuVal())); // ｾｯﾄ数32
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(32).getSetsuuVal())); // ｾｯﾄ数33
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(33).getSetsuuVal())); // ｾｯﾄ数34
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(34).getSetsuuVal())); // ｾｯﾄ数35
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(35).getSetsuuVal())); // ｾｯﾄ数36
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(36).getSetsuuVal())); // ｾｯﾄ数37
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(37).getSetsuuVal())); // ｾｯﾄ数38
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(38).getSetsuuVal())); // ｾｯﾄ数39
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(39).getSetsuuVal())); // ｾｯﾄ数40
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
     * 下端子開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShitatanshiKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 下端子終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setShitatanshiShuryouDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 上端子開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setUwatanshiKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 上端子終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setUwatanshiShuryouDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME);
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
            // 積層号機
            case GXHDO101B005Const.SEKISOU_GOKI:
                return StringUtil.nullToBlank(srRsussekData.getGoki());
            // 下端子
            case GXHDO101B005Const.SHITA_TANSHI:
                return StringUtil.nullToBlank(srRsussekData.getShitatanshi());
            // 下端子開始日
            case GXHDO101B005Const.SHITA_TANSHI_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srRsussekData.getShitatanshikaisinichiji(), "yyMMdd");
            // 下端子開始時刻
            case GXHDO101B005Const.SHITA_TANSHI_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srRsussekData.getShitatanshikaisinichiji(), "HHmm");
            // 下端子開始日
            case GXHDO101B005Const.SHITA_TANSHI_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srRsussekData.getShitatanshisyuryonichiji(), "yyMMdd");
            // 下端子開始時刻
            case GXHDO101B005Const.SHITA_TANSHI_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srRsussekData.getShitatanshisyuryonichiji(), "HHmm");
            // 下端子担当者
            case GXHDO101B005Const.SHITA_TANSHI_TANTOSYA:
                return StringUtil.nullToBlank(srRsussekData.getShitatanshitantosya());
            // 下端子確認者
            case GXHDO101B005Const.SHITA_TANSHI_KAKUNINSYA:
                return StringUtil.nullToBlank(srRsussekData.getShitatanshikakuninsya());
            // 下端子備考
            case GXHDO101B005Const.SHITA_TANSHI_BIKO:
                return StringUtil.nullToBlank(srRsussekData.getShitatanshibiko());
            // 上端子
            case GXHDO101B005Const.UWE_TANSHI:
                return StringUtil.nullToBlank(srRsussekData.getUwatanshi());
            // 上端子開始日
            case GXHDO101B005Const.UWE_TANSHI_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srRsussekData.getUwatanshikaisinichiji(), "yyMMdd");
            // 上端子開始時刻
            case GXHDO101B005Const.UWE_TANSHI_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srRsussekData.getUwatanshikaisinichiji(), "HHmm");
            // 上端子開始日
            case GXHDO101B005Const.UWE_TANSHI_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srRsussekData.getUwatanshisyuryonichiji(), "yyMMdd");
            // 上端子開始時刻
            case GXHDO101B005Const.UWE_TANSHI_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srRsussekData.getUwatanshisyuryonichiji(), "HHmm");
            // 上端子担当者
            case GXHDO101B005Const.UWE_TANSHI_TANTOSYA:
                return StringUtil.nullToBlank(srRsussekData.getUwatanshitantosya());
            // 上端子確認者
            case GXHDO101B005Const.UWE_TANSHI_KAKUNINSYA:
                return StringUtil.nullToBlank(srRsussekData.getUwatanshikakuninsya());
            // 上端子備考
            case GXHDO101B005Const.UWE_TANSHI_BIKO:
                return StringUtil.nullToBlank(srRsussekData.getUwatanshibiko());
            // 処理セット数
            case GXHDO101B005Const.SYORI_SET_SU:
                return StringUtil.nullToBlank(srRsussekData.getSyorisetsuu());
            // 良品セット数
            case GXHDO101B005Const.RYOUHIN_SET_SU:
                return StringUtil.nullToBlank(srRsussekData.getRyouhinsetsuu());
            // 外観確認1
            case GXHDO101B005Const.GAIKAN_KAKUNIN1:
                return StringUtil.nullToBlank(srRsussekData.getGaikankakunin1());
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
            // 瞬時加熱時間
            case GXHDO101B005Const.SHUNJI_KANETSU_TIME:
                return StringUtil.nullToBlank(srRsussekData.getSkjikan());
            // タクト
            case GXHDO101B005Const.TAKT:
                return StringUtil.nullToBlank(srRsussekData.getTakuto());
            // ﾍｯﾄﾞNo
            case GXHDO101B005Const.HEADNO:
                return StringUtil.nullToBlank(srRsussekData.getHeadno());
            // SUS板枚数
            case GXHDO101B005Const.SUSITAMAISU:
                return StringUtil.nullToBlank(srRsussekData.getSusitamaisu());
            // 先行ロットNo
            case GXHDO101B005Const.SENKOU_LOT_NO:
                return StringUtil.nullToBlank(srRsussekData.getSkojyo()) + StringUtil.nullToBlank(srRsussekData.getSlotno()) + StringUtil.nullToBlank(srRsussekData.getSedaban());
            // 備考1
            case GXHDO101B005Const.BIKOU1:
                return StringUtil.nullToBlank(srRsussekData.getBiko1());
            // 備考2
            case GXHDO101B005Const.BIKOU2:
                return StringUtil.nullToBlank(srRsussekData.getBikou2());
            // 電極製版ﾛｯﾄNo
            case GXHDO101B005Const.ELOTNO:
                return StringUtil.nullToBlank(srRsussekData.getElotno());
            // 最上層担当者
            case GXHDO101B005Const.LASTLAYERTANTOSYA:
                return StringUtil.nullToBlank(srRsussekData.getLastlayertantosya());
            // 最上層備考
            case GXHDO101B005Const.LASTLAYERBIKO:
                return StringUtil.nullToBlank(srRsussekData.getLastlayerbiko());
            //KCPNo
            case GXHDO101B005Const.KCPNO:
                return StringUtil.nullToBlank(srRsussekData.getKcpno());
            // 製版名
            case GXHDO101B005Const.PATERN:
                return StringUtil.nullToBlank(srRsussekData.getPatern());
            // 取り個数
            case GXHDO101B005Const.TORIKOSUU:
                return StringUtil.nullToBlank(srRsussekData.getTorikosuu());
            // 連続積層枚数
            case GXHDO101B005Const.RENZOKUSEKISOUMAISUU:
                return StringUtil.nullToBlank(srRsussekData.getRenzokusekisoumaisuu());
            // B層補正量
            case GXHDO101B005Const.BSOUHOSEIRYOU:
                return StringUtil.nullToBlank(srRsussekData.getBsouhoseiryou());
            // Y軸補正量
            case GXHDO101B005Const.YJIKUHOSEIRYOU:
                return StringUtil.nullToBlank(srRsussekData.getYjikuhoseiryou());
            // 電極ｽﾀｰﾄ確認者
            case GXHDO101B005Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srRsussekData.getKakuninsya());
            // 電極ﾍﾟｰｽﾄ
            case GXHDO101B005Const.DENKYOKU_PASTE:
                return StringUtil.nullToBlank(srRsussekData.getEpaste());
            // 積層ｽﾗｲﾄﾞ量
            case GXHDO101B005Const.SEKISOU_SLIDE_RYOU:
                return StringUtil.nullToBlank(srRsussekData.getSekisouslideryo());
            // 最上層ｽﾗｲﾄﾞ量
            case GXHDO101B005Const.LAST_LAYER_SLIDE_RYO:
                return StringUtil.nullToBlank(srRsussekData.getLastlayerslideryo());
            // 印刷ﾛｰﾙNo
            case GXHDO101B005Const.PRINT_ROLLNO:
                return StringUtil.nullToBlank(srRsussekData.getInsaturollno());
            // ｾｯﾄ数
            case GXHDO101B005Const.SET_SUU:
                return StringUtil.nullToBlank(srRsussekData.getSetsuu());
            // 客先
            case GXHDO101B005Const.KYAKUSAKI:
                return StringUtil.nullToBlank(srRsussekData.getTokuisaki());
            // ﾛｯﾄ区分
            case GXHDO101B005Const.LOT_KUBUN:
                return StringUtil.nullToBlank(srRsussekData.getLotkubuncode());
            // ｵｰﾅｰ
            case GXHDO101B005Const.OWNER:
                return StringUtil.nullToBlank(srRsussekData.getOwnercode());
            case GXHDO101B005Const.SEKISOATURYOKU:
                return StringUtil.nullToBlank(srRsussekData.getSekiatsu());
            // ﾃｰﾌﾟ使い切り
            case GXHDO101B005Const.TAPETSUKAIKIRI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srRsussekData.getTapetsukaikiri()));
            // 次ﾛｯﾄへ
            case GXHDO101B005Const.JILOTHE:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srRsussekData.getJilothe()));
            // 備考3
            case GXHDO101B005Const.BIKOU3:
                return StringUtil.nullToBlank(srRsussekData.getBikou3());
            // 備考4
            case GXHDO101B005Const.BIKOU4:
                return StringUtil.nullToBlank(srRsussekData.getBikou4());
            // 備考5
            case GXHDO101B005Const.BIKOU5:
                return StringUtil.nullToBlank(srRsussekData.getBikou5());
                
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
                + " KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,TOROKUNICHIJI,KOSINNICHIJI,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4"
                + ",EndTantousyacode,TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2"
                + ",setsuu,tokuisaki,lotkubuncode,ownercode,syurui3,atumi3,maisuu3,patern,torikosuu,etape,lretu,wretu,lsun,wsun,epaste,genryou,eatumi,sousuu"
                + ",emaisuu,sekisouslideryo,lastlayerslideryo,renzokusekisoumaisuu,bsouhoseiryou,yjikuhoseiryou"
                + ",syurui2,atumi2,maisuu2,ShitaTanshiKAISINICHIJI,ShitaTanshiSYURYONICHIJI,ShitaTanshiTANTOSYA,ShitaTanshiKAKUNINSYA,ShitaTanshiBIKO,UwaTanshiKAISINICHIJI"
                + ",UwaTanshiSYURYONICHIJI,UwaTanshiTANTOSYA,UwaTanshiKAKUNINSYA,UwaTanshiBIKO,HeadNo,SUSitamaisu,lastlayerTANTOSYA,lastlayerBIKO,elotno,revision,deleteflag,sekiatsu,"
                + "tapetsukaikiri,jilothe,bikou3,bikou4,bikou5 "
                + ") SELECT "
                + " KOJYO,LOTNO,EDABAN,KCPNO,TNTAPESYURUI,TNTAPENO,TNTAPEGENRYO,KAISINICHIJI,SYURYONICHIJI,GOKI,JITUATURYOKU,SEKISOZURE2"
                + ",TANTOSYA,KAKUNINSYA,INSATUROLLNO,HAPPOSHEETNO,SKJIKAN,TAKUTO,BIKO1,?,?,SKOJYO,SLOTNO,SEDABAN,tapelotno,taperollno1"
                + ",taperollno2,taperollno3,genryoukigou,petfilmsyurui,Kotyakugouki,Kotyakusheet,ShitaTanshigouki,UwaTanshigouki"
                + ",ShitaTanshiBukunuki,ShitaTanshi,UwaTanshi,SyoriSetsuu,RyouhinSetsuu,GaikanKakunin1,GaikanKakunin2,GaikanKakunin3,GaikanKakunin4"
                + ",EndTantousyacode,TanshiTapeSyurui,HNGKaisuu,HNGKaisuuAve,GNGKaisuu,GNGKaisuuAve,bikou2"
                + ",setsuu,tokuisaki,lotkubuncode,ownercode,syurui3,atumi3,maisuu3,patern,torikosuu,etape,lretu,wretu,lsun,wsun,epaste,genryou,eatumi,sousuu"
                + ",emaisuu,sekisouslideryo,lastlayerslideryo,renzokusekisoumaisuu,bsouhoseiryou,yjikuhoseiryou"
                + ",syurui2,atumi2,maisuu2,ShitaTanshiKAISINICHIJI,ShitaTanshiSYURYONICHIJI,ShitaTanshiTANTOSYA,ShitaTanshiKAKUNINSYA,ShitaTanshiBIKO,UwaTanshiKAISINICHIJI"
                + ",UwaTanshiSYURYONICHIJI,UwaTanshiTANTOSYA,UwaTanshiKAKUNINSYA,UwaTanshiBIKO,HeadNo,SUSitamaisu,lastlayerTANTOSYA,lastlayerBIKO,elotno,?,?,sekiatsu,"
                + "tapetsukaikiri,jilothe,bikou3,bikou4,bikou5 "
                + " FROM sr_rsussek "
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
    
    /**
     * 規格値を括り【】を除いて取得
     * @param itemList 項目リスト
     * @param itemId 項目ID
     * @return 規格値
     */
    private String getKikakuchiValue(List<FXHDD01> itemList, String itemId) {

        FXHDD01 item = getItemRow(itemList, itemId); // 規格値を取得する項目を取得
        // 項目が取得出来ないまたは規格値が設定されていない場合
        if (item == null || StringUtil.isEmpty(item.getKikakuChi())) {
            return "";
        }

        String kikakuchi = item.getKikakuChi();
        if (kikakuchi.startsWith("【")) {
            // 本来ありえないが1文字しかない場合、空を返却
            if (StringUtil.length(kikakuchi) == 1) {
                return "";
            }
            // 先頭文字以降を切り出し
            kikakuchi = kikakuchi.substring(1);
        }

        if (kikakuchi.endsWith("】")) {
            // 本来ありえないが1文字しかない場合、空を返却
            if (StringUtil.length(kikakuchi) == 1) {
                return "";
            }
            // 最終文字文字以前を切り出し
            kikakuchi = kikakuchi.substring(0, StringUtil.length(kikakuchi) - 1);
        }

        return kikakuchi;
    }

    /**
     * 前工程WIP取込(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openWipImport(ProcessData processData) {

        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c020");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean("GXHDO101C020");
            beanGXHDO101C020.setGxhdO101c020ModelView(beanGXHDO101C020.getGxhdO101c020Model().clone());

        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;

        }

        return processData;
    }

    /**
     * 前工程WIP取込画面データ設定処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態フラグ
     */
    private void setInputItemDataSubFormC020(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String jotaiFlg) throws SQLException {
        // サブ画面の情報を取得
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean("GXHDO101C020");
        List<Map<String, Object>> initDataSubFormC020 = null;
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            initDataSubFormC020 = getInitDataSubFormC020(queryRunnerQcdb, kojyo, lotNo, edaban, jotaiFlg);
        }
        
        GXHDO101C020Model model = new GXHDO101C020Model();
        // 登録データが無い場合空の状態で初期値をセットする。
        // 登録データがあれば登録データをセットする。
        model = GXHDO101C020Logic.createGXHDO101C020Model(initDataSubFormC020, "GXHDO101B005");

        model.setReturnItemId_Uwatanshi_Conventionallot(GXHDO101B005Const.UWE_TANSHI);
        model.setReturnItemId_Shitatanshi_Conventionallot(GXHDO101B005Const.SHITA_TANSHI);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        beanGXHDO101C020.setGxhdO101c020Model(model);
    }

    /**
     * 前工程WIP取込画面初期表示データを取得する
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態フラグ
     * @return 前工程WIP取込画面初期表示データ
     * @throws SQLException 
     */
    private List<Map<String, Object>> getInitDataSubFormC020(QueryRunner queryRunnerQcdb, 
            String kojyo, String lotNo, String edaban, String jotaiFlg) throws SQLException {
        
        String tableName = " from sr_mwiplotlink ";
        String whereSQL = " where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and deleteflag = ?";
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg)) {
            tableName = " from tmp_sr_mwiplotlink ";
            whereSQL = " where kojyo = ? and lotno = ? and edaban = ? and gamenid = ?";
        }
        
        String sql = "select mkojyo, mlotno, medaban, mkubun, mkubunno" + tableName + whereSQL;
        
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B005");
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            params.add(0);
        }
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * 前工程WIP取込ｻﾌﾞ画面仮登録(tmp_sr_mwiplotlink)登録処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        String sql = "INSERT INTO tmp_sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        
        List<Object> params = new ArrayList<>();
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            if (StringUtil.isEmpty(genryouLotData.getValue())) {
                continue;
            }
            
            params.clear();
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            
            String motoLotno = genryouLotData.getValue();
            String motoKojyo = motoLotno.substring(0, 3);
            String motoLotNo9 = motoLotno.substring(3, 12);
            String motoEdaban = motoLotno.substring(12, 15);
            params.add(motoKojyo); //前工程工場ｺｰﾄﾞ
            params.add(motoLotNo9); //前工程ﾛｯﾄNo
            params.add(motoEdaban); //前工程枝番
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            params.add(mkubun); //前工程区分
            params.add(mkubunno); //前工程区分No
            params.add("GXHDO101B005"); //画面ID
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        }
    }

    /**
     * 前工程WIP取込ｻﾌﾞ画面登録(sr_mwiplotlink)登録処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        String sql = "INSERT INTO sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, deleteflag) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        
        List<Object> params = new ArrayList<>();
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            if (StringUtil.isEmpty(genryouLotData.getValue())) {
                continue;
            }
            
            params.clear();
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            
            String motoLotno = genryouLotData.getValue();
            String motoKojyo = motoLotno.substring(0, 3);
            String motoLotNo9 = motoLotno.substring(3, 12);
            String motoEdaban = motoLotno.substring(12, 15);
            params.add(motoKojyo); //前工程工場ｺｰﾄﾞ
            params.add(motoLotNo9); //前工程ﾛｯﾄNo
            params.add(motoEdaban); //前工程枝番
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            params.add(mkubun); //前工程区分
            params.add(mkubunno); //前工程区分No
            params.add("GXHDO101B005"); //画面ID
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            params.add(0); //削除フラグ
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        }
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面仮登録(tmp_sr_mwiplotlink)更新処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban);
        insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, systemTime);
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面(sr_mwiplotlink)更新処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            
            boolean isExist = isExistFromSrMwiplotlink(queryRunnerQcdb, kojyo, lotNo, edaban, mkubun, mkubunno);
            if (isExist) {
                // データが存在の場合、deleteflagを更新
                updateSrMwiplotlinkToDelete(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, systemTime);
            }
            insertSrMwiplotlinkByData(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, systemTime, genryouLotData);
        }
    }

    /**
     * sr_mwiplotlinkデータあり→なしの場合、deleteflagを更新
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param mkubun 前工程区分
     * @param mkubunno 前工程区分No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSrMwiplotlinkToDelete(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, String mkubun, String mkubunno, Timestamp systemTime) throws SQLException {
        
        String sql = "UPDATE sr_mwiplotlink "
                + "SET deleteflag = ?, koushinnichiji = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND gamenid = ? AND mkubun = ? AND mkubunno = ? AND deleteflag = '0'";
        
        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(1); //削除フラグ
        params.add(systemTime); //更新日

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add("GXHDO101B005");
        params.add(mkubun);
        params.add(mkubunno);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面(sr_mwiplotlink)更新処理
     * sr_mwiplotlinkデータが存在しない場合、INSERT
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param genryouLotData 前工程WIP取込ｻﾌﾞ画面のbean
     * @throws SQLException 例外エラー
     */
    private void insertSrMwiplotlinkByData(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime, 
            GXHDO101C020Model.GenryouLotData genryouLotData) throws SQLException {
        
        if (StringUtil.isEmpty(genryouLotData.getValue())) {
            return;
        }
        
        String sql = "INSERT INTO sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, deleteflag) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        
        List<Object> params = new ArrayList<>();

        params.clear();
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番

        String motoLotno = genryouLotData.getValue();
        String motoKojyo = motoLotno.substring(0, 3);
        String motoLotNo9 = motoLotno.substring(3, 12);
        String motoEdaban = motoLotno.substring(12, 15);
        params.add(motoKojyo); //前工程工場ｺｰﾄﾞ
        params.add(motoLotNo9); //前工程ﾛｯﾄNo
        params.add(motoEdaban); //前工程枝番

        String typeName = genryouLotData.getTypeName();
        String mkubun = getKubun(typeName);
        String mkubunno = getKubunNo(typeName);
        params.add(mkubun); //前工程区分
        params.add(mkubunno); //前工程区分No
        params.add("GXHDO101B005"); //画面ID
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(0); //削除フラグ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * sr_mwiplotlinkのデータが存在かの判断
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param mkubun 前工程区分
     * @param mkubunno 前工程区分No
     * @return true:存在　false:存在しない
     * @throws SQLException 例外エラー
     */
    private boolean isExistFromSrMwiplotlink(QueryRunner queryRunnerQcdb, 
            String kojyo, String lotNo, String edaban, String mkubun, String mkubunno) throws SQLException {
        
        String sql = "select mkojyo, mlotno, medaban, mkubun, mkubunno "
                + "from sr_mwiplotlink "
                + "where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and mkubun = ? and mkubunno = ? and deleteflag = ? ";
        
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B005");
        params.add(mkubun);
        params.add(mkubunno);
        params.add(0);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map<String, Object> query = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        return !(query == null || query.isEmpty());
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面仮登録(tmp_sr_mwiplotlink)削除処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー 
     */
    private void deleteTmpSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban) throws SQLException {
        
        String sql = "DELETE FROM tmp_sr_mwiplotlink "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND gamenid = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B005");
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面仮登録(sr_mwiplotlink)削除処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー 
     */
    private void deleteSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            
            if (isExistFromSrMwiplotlink(queryRunnerQcdb, kojyo, lotNo, edaban, mkubun, mkubunno)) {
                updateSrMwiplotlinkToDelete(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, systemTime);
            }
        }
    }

    /**
     * 前工程区分を取得する
     * 
     * @param typeName 前工程WIP取込サブ画面の種類名
     * @return 前工程区分
     */
    private String getKubun(String typeName) {
        String mkubun = "";
        switch (typeName) {
            case GXHDO101C020Model.TAPE_LOT_1:
            case GXHDO101C020Model.TAPE_LOT_2:
            case GXHDO101C020Model.TAPE_LOT_3:
                mkubun = "電極ﾃｰﾌﾟ";
                break;
            case GXHDO101C020Model.PASTE_LOT_1:
            case GXHDO101C020Model.PASTE_LOT_2:
                mkubun = "内部電極ﾍﾟｰｽﾄ";
                break;
            case GXHDO101C020Model.UWA_TANSHI:
                mkubun = "上端子ﾃｰﾌﾟ";
                break;
            case GXHDO101C020Model.SHITA_TANSHI:
                mkubun = "下端子ﾃｰﾌﾟ";
                break;
        }
        return mkubun;
    }

    /**
     * 前工程区分Noを取得する
     * 
     * @param typeName 前工程WIP取込サブ画面の種類名
     * @return 前工程区分No
     */
    private String getKubunNo(String typeName) {
        String mkubunno = "";
        switch (typeName) {
            case GXHDO101C020Model.TAPE_LOT_1:
            case GXHDO101C020Model.PASTE_LOT_1:
            case GXHDO101C020Model.UWA_TANSHI:
            case GXHDO101C020Model.SHITA_TANSHI:
                mkubunno = "1";
                break;
            case GXHDO101C020Model.TAPE_LOT_2:
            case GXHDO101C020Model.PASTE_LOT_2:
                mkubunno = "2";
                break;
            case GXHDO101C020Model.TAPE_LOT_3:
                mkubunno = "3";
                break;
        }
        return mkubunno;
    }
    /**
     * 製造条件マスタ
     * 
     * @param sekkeino
     * @param koteimei
     * @param koumokumei
     * @param kanrikoumoku
     * @param queryRunnerQcdb
     * @return 遷移先URL文字列
     * @throws java.sql.SQLException
     */
    public String loadJoken(String sekkeino, String koteimei, String koumokumei, String kanrikoumoku, QueryRunner queryRunnerQcdb) throws SQLException {
        
        // 検索用ロットNo
        
        // SQL生成
        String sql = "SELECT KIKAKUCHI AS kikakuchi"
                + " FROM DA_JOKEN "
                + "WHERE SEKKEINO = ? "
                + "AND KOUTEIMEI = ? "
                + "AND KOUMOKUMEI = ? "
                + "AND KANRIKOUMOKU = ?;";
        
        // パラメータの設定
        List<Object> params = new ArrayList<>();
        params.add(sekkeino);
        params.add(koteimei);
        params.add(koumokumei);
        params.add(kanrikoumoku);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sekkeiData = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

        if (sekkeiData == null || sekkeiData.isEmpty()) {
            return "";
        }
        String kikakuchi = StringUtil.nullToBlank(sekkeiData.get("kikakuchi"));
        
        return StringUtil.blankToNull(kikakuchi);
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
}
