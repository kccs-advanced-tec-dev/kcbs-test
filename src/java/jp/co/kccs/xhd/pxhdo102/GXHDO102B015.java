/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

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
import jp.co.kccs.xhd.common.CompMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrBinderFp;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
import jp.co.kccs.xhd.util.CommonUtil;
import jp.co.kccs.xhd.util.DBUtil;
import jp.co.kccs.xhd.util.DateUtil;
import jp.co.kccs.xhd.util.ErrUtil;
import jp.co.kccs.xhd.util.MessageUtil;
import jp.co.kccs.xhd.util.StringUtil;
import jp.co.kccs.xhd.util.SubFormUtil;
import jp.co.kccs.xhd.util.ValidateUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/27<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B015(ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ)
 *
 * @author KCSS K.Jo
 * @since 2021/10/27
 */
public class GXHDO102B015 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B015.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B015() {
    }

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

            List<String> errorMassageList = checkExistFormItem(processData);
            if (0 < errorMassageList.size()) {
                processData.setFatalError(true);
                processData.setInitMessageList(errorMassageList);
                processData.setMethod("openInitMessage");
                return processData;
            }

            // 初期設定
            initGXHDO102B015A(processData);

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

            //処理時にエラーの背景色を戻さない機能として登録
            processData.setNoCheckButtonId(Arrays.asList(GXHDO102B015Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B015Const.BTN_FPKAISINICHIJI_TOP,
                    GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_TOP,
                    GXHDO102B015Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B015Const.BTN_FPKAISINICHIJI_BOTTOM,
                    GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B015Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B015Const.BTN_INSERT_TOP,
                    GXHDO102B015Const.BTN_DELETE_TOP,
                    GXHDO102B015Const.BTN_UPDATE_TOP,
                    GXHDO102B015Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B015Const.BTN_INSERT_BOTTOM,
                    GXHDO102B015Const.BTN_DELETE_BOTTOM,
                    GXHDO102B015Const.BTN_UPDATE_BOTTOM
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
     * ボタンID⇒メソッド名変換
     *
     * @param buttonId ボタンID
     * @return メソッド名
     */
    @Override
    public String convertButtonIdToMethod(String buttonId) {
        String method;
        switch (buttonId) {
            // 枝番コピー
            case GXHDO102B015Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B015Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B015Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B015Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B015Const.BTN_INSERT_TOP:
            case GXHDO102B015Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B015Const.BTN_UPDATE_TOP:
            case GXHDO102B015Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B015Const.BTN_DELETE_TOP:
            case GXHDO102B015Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // F/P開始日時
            case GXHDO102B015Const.BTN_FPKAISINICHIJI_TOP:
            case GXHDO102B015Const.BTN_FPKAISINICHIJI_BOTTOM:
                method = "setFpkaisinichijiDateTime";
                break;
            // F/P終了日時
            case GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_TOP:
            case GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_BOTTOM:
                method = "setFpsyuuryounichijiDateTime";
                break;
            default:
                method = "error";
                break;
        }

        return method;
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

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String tantoshaCd = (String) session.getAttribute("tantoshaCd");
            String kojyo = lotNo.substring(0, 3);
            String lotNo9 = lotNo.substring(3, 12);

            // 前工程WIPから仕掛情報を取得処理
            Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
            if (shikakariData == null || shikakariData.isEmpty() || !shikakariData.containsKey("oyalotedaban")) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, oyalotEdaban, paramJissekino, formId);
            if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽの入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrBinderFp> srBinderFpDataList = getSrBinderFpData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srBinderFpDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srBinderFpDataList.get(0));

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
            // リビジョンエラー時はリターン
            if (checkRevMessageInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkRevMessageInfo));
                // コネクションロールバック処理
                DBUtil.rollbackConnection(conDoc, LOGGER);
                DBUtil.rollbackConnection(conQcdb, LOGGER);
                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, paramJissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, paramJissekino);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録処理
                insertTmpSrBinderFp(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録更新処理
                updateTmpSrBinderFp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
            processData.getItemList().forEach((fxhdd01) -> {
                fxhdd01.setBackColorInput(fxhdd01.getBackColorInputDefault());
            });
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
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_DAY); // F/P開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_TIME); // F/P開始時間
        Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_TIME); // F/P終了時間
        Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001("F/P開始日時", kaisiDate, "F/P終了日時", syuuryouDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, paramJissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);

                // 品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrBinderFp tmpSrBinderFp = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrBinderFp> srBinderFpList = getSrBinderFpData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srBinderFpList.isEmpty()) {
                    tmpSrBinderFp = srBinderFpList.get(0);
                }

                deleteTmpSrBinderFp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_登録処理
            insertSrBinderFp(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrBinderFp);
            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            setCompMessage("登録しました。");
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

        // 後続処理メソッド設定
        processData.setMethod("doCorrectKakunin");

        return processData;

    }

    /**
     * 修正処理時の確認メッセージ表示
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doCorrectKakunin(ProcessData processData) {

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO102B015Const.USER_AUTH_UPDATE_PARAM);

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, paramJissekino);

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_更新処理
            updateSrBinderFp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, paramJissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }
            // 処理後はエラーリストをクリア
            kikakuError.setKikakuchiInputErrorInfoList(new ArrayList<>());
            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            setCompMessage("修正しました。");

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
        processData.setUserAuthParam(GXHDO102B015Const.USER_AUTH_DELETE_PARAM);

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd11RevInfo);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, paramJissekino, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrBinderFp(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_削除処理
            deleteSrBinderFp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            setCompMessage("削除しました。");

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
     * F/P開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpkaisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpsyuuryounichijiDateTime(ProcessData processData) {

        FXHDD01 itemFpkaisiDay = getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_DAY);// F/P開始日
        FXHDD01 itemFpkaisiTime = getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_TIME);// F/P開始時間
        FXHDD01 itemFpsyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_DAY);// F/P終了日
        FXHDD01 itemFpsyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_TIME);// F/P終了時間
        int diffHours;
        Date kaishijikan = null;
        Date syuuryoujikan = null;
        FXHDD01 fpjikan = getItemRow(processData.getItemList(), GXHDO102B015Const.FPJIKAN);
        if (StringUtil.isEmpty(itemFpsyuuryouDay.getValue()) && StringUtil.isEmpty(itemFpsyuuryouTime.getValue())) {
            setDateTimeItem(itemFpsyuuryouDay, itemFpsyuuryouTime, new Date());
        }
        if (DateUtil.isValidYYMMDD(itemFpkaisiDay.getValue()) && DateUtil.isValidHHMM(itemFpkaisiTime.getValue())) {
            kaishijikan = DateUtil.convertStringToDate(itemFpkaisiDay.getValue(), itemFpkaisiTime.getValue());
        }
        if (DateUtil.isValidYYMMDD(itemFpsyuuryouDay.getValue()) && DateUtil.isValidHHMM(itemFpsyuuryouTime.getValue())) {
            syuuryoujikan = DateUtil.convertStringToDate(itemFpsyuuryouDay.getValue(), itemFpsyuuryouTime.getValue());
        }
        // 日付の差分分数の数取得処理
        diffHours = DateUtil.diffMinutes(kaishijikan, syuuryoujikan);
        // 計算結果の設定
        fpjikan.setValue(BigDecimal.valueOf(diffHours).toPlainString());
        processData.setMethod("");
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
                activeIdList.addAll(Arrays.asList(GXHDO102B015Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B015Const.BTN_FPKAISINICHIJI_TOP,
                        GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_TOP,
                        GXHDO102B015Const.BTN_UPDATE_TOP,
                        GXHDO102B015Const.BTN_DELETE_TOP,
                        GXHDO102B015Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B015Const.BTN_FPKAISINICHIJI_BOTTOM,
                        GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B015Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B015Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B015Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B015Const.BTN_INSERT_TOP,
                        GXHDO102B015Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B015Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO102B015Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B015Const.BTN_FPKAISINICHIJI_TOP,
                        GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_TOP,
                        GXHDO102B015Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B015Const.BTN_INSERT_TOP,
                        GXHDO102B015Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B015Const.BTN_FPKAISINICHIJI_BOTTOM,
                        GXHDO102B015Const.BTN_FPSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B015Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B015Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B015Const.BTN_UPDATE_TOP,
                        GXHDO102B015Const.BTN_DELETE_TOP,
                        GXHDO102B015Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B015Const.BTN_DELETE_BOTTOM
                ));

                break;

        }
        processData.setActiveButtonId(activeIdList);
        processData.setInactiveButtonId(inactiveIdList);
        return processData;
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
        session.setAttribute("jissekino", 1);
        int paramJissekino = (Integer) session.getAttribute("jissekino");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        String tantoshaCd = (String) session.getAttribute("tantoshaCd");

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map mkSekkeiData = this.loadMkSekkeiData(queryRunnerQcdb, queryRunnerWip, lotNo);
        if (mkSekkeiData == null || mkSekkeiData.isEmpty()) {
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014"));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 前工程WIPから仕掛情報を取得処理
        Map shikakariData = loadShikakariDataFromWip(queryRunnerDoc, tantoshaCd, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        processData.getItemList().stream().map((item) -> {
            if ((item.isRender1() || item.isRenderLinkButton()) && !"".equals(StringUtil.nullToBlank(item.getKikakuChi()))) {
                if ("".equals(StringUtil.nullToBlank(item.getBackColor3()))) {
                    item.setBackColor3("#EEEEEE");
                }
                if (0 == item.getFontSize3()) {
                    item.setFontSize3(16);
                }
            }
            return item;
        }).filter((item) -> (item.isRenderOutputLabel() && !"".equals(StringUtil.nullToBlank(item.getValue())))).map((item) -> {
            if ("".equals(StringUtil.nullToBlank(item.getBackColorInput()))) {
                item.setBackColorInput("#EEEEEE");
            }
            return item;
        }).filter((item) -> (0 == item.getFontSizeInput())).forEachOrdered((item) -> {
            item.setFontSizeInput(16);
        });
        processData.setInitMessageList(errorMessageList);
        return processData;
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
    private Map loadMkSekkeiData(QueryRunner queryRunnerQcdb, QueryRunner queryRunnerWip, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 12);
        // 設計データの取得
        return CommonUtil.getMkSekkeiInfo(queryRunnerQcdb, queryRunnerWip, lotNo1, lotNo2, "001");
    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map shikakariData, String lotNo) {

        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B015Const.WIPLOTNO, lotNo);
        // ﾊﾞｲﾝﾀﾞｰ溶液品名
        this.setItemData(processData, GXHDO102B015Const.BINDERYOUEKIHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        this.setItemData(processData, GXHDO102B015Const.BINDERYOUEKILOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B015Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B015Const.LOTKUBUN, lotkubuncode);
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

        List<SrBinderFp> srBinderFpList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);

        for (int i = 0; i < 5; i++) {
            // [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });

                return true;
            }

            // ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ取得
            srBinderFpList = getSrBinderFpData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srBinderFpList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srBinderFpList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srBinderFpList.get(0));
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srBinderFp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrBinderFp srBinderFp) {

        // ﾌｨﾙﾀｰ交換日
        this.setItemData(processData, GXHDO102B015Const.FILTERKOUKAN_DAY, getSrBinderFpItemData(GXHDO102B015Const.FILTERKOUKAN_DAY, srBinderFp));

        // ﾌｨﾙﾀｰ交換時間
        this.setItemData(processData, GXHDO102B015Const.FILTERKOUKAN_TIME, getSrBinderFpItemData(GXHDO102B015Const.FILTERKOUKAN_TIME, srBinderFp));

        // LotNo
        this.setItemData(processData, GXHDO102B015Const.FPLOTNO, getSrBinderFpItemData(GXHDO102B015Const.FPLOTNO, srBinderFp));

        // 取り付け本数
        this.setItemData(processData, GXHDO102B015Const.TORITUKEHONSUU, getSrBinderFpItemData(GXHDO102B015Const.TORITUKEHONSUU, srBinderFp));

        // F/P準備_担当者
        this.setItemData(processData, GXHDO102B015Const.FPJYUNBI_TANTOUSYA, getSrBinderFpItemData(GXHDO102B015Const.FPJYUNBI_TANTOUSYA, srBinderFp));

        // F/P開始日
        this.setItemData(processData, GXHDO102B015Const.FPKAISI_DAY, getSrBinderFpItemData(GXHDO102B015Const.FPKAISI_DAY, srBinderFp));

        // F/P開始時間
        this.setItemData(processData, GXHDO102B015Const.FPKAISI_TIME, getSrBinderFpItemData(GXHDO102B015Const.FPKAISI_TIME, srBinderFp));

        // F/P開始_担当者
        this.setItemData(processData, GXHDO102B015Const.FPKAISI_TANTOUSYA, getSrBinderFpItemData(GXHDO102B015Const.FPKAISI_TANTOUSYA, srBinderFp));

        // F/P終了日
        this.setItemData(processData, GXHDO102B015Const.FPSYUURYOU_DAY, getSrBinderFpItemData(GXHDO102B015Const.FPSYUURYOU_DAY, srBinderFp));

        // F/P終了時間
        this.setItemData(processData, GXHDO102B015Const.FPSYUURYOU_TIME, getSrBinderFpItemData(GXHDO102B015Const.FPSYUURYOU_TIME, srBinderFp));

        // F/P終了_担当者
        this.setItemData(processData, GXHDO102B015Const.FPSYUURYOU_TANTOUSYA, getSrBinderFpItemData(GXHDO102B015Const.FPSYUURYOU_TANTOUSYA, srBinderFp));

        // F/P時間
        this.setItemData(processData, GXHDO102B015Const.FPJIKAN, getSrBinderFpItemData(GXHDO102B015Const.FPJIKAN, srBinderFp));

        // 備考1
        this.setItemData(processData, GXHDO102B015Const.BIKOU1, getSrBinderFpItemData(GXHDO102B015Const.BIKOU1, srBinderFp));

        // 備考2
        this.setItemData(processData, GXHDO102B015Const.BIKOU2, getSrBinderFpItemData(GXHDO102B015Const.BIKOU2, srBinderFp));

    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ
     * @throws SQLException 例外エラー
     */
    private List<SrBinderFp> getSrBinderFpData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrBinderFp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrBinderFp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 前工程WIPから仕掛情報を取得する。
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者コード
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariDataFromWip(QueryRunner queryRunnerDoc, String tantoshaCd, String lotNo) throws SQLException {
        List<SikakariJson> sikakariList = CommonUtil.getMwipResult(queryRunnerDoc, tantoshaCd, lotNo);
        SikakariJson sikakariObj;
        Map shikakariData = new HashMap();
        if (sikakariList != null) {
            sikakariObj = sikakariList.get(0);
            // 前工程WIPから取得した品名
            shikakariData.put("hinmei", sikakariObj.getHinmei());
            shikakariData.put("oyalotedaban", sikakariObj.getOyaLotEdaBan());
            shikakariData.put("lotkubuncode", sikakariObj.getLotKubunCode());
            shikakariData.put("lotkubun", sikakariObj.getLotkubun());
            shikakariData.put("lotno", sikakariObj.getConventionalLot());
        }

        return shikakariData;
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
    private Map loadFxhdd11RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 品質DB登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd11 "
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
    private Map loadFxhdd11RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String formId) throws SQLException {
        // 品質DB登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd11 "
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
        // 品質DB登録実績データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd11 "
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
     * [ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrBinderFp> loadSrBinderFp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,filterkoukannichiji,filterhinmei,"
                + "fplotno,toritukehonsuu,fpjyunbi_tantousya,fpkaisinichiji,fpkaisi_tantousya,fpsyuuryounichiji,"
                + "fpsyuuryou_tantousya,fpjikan,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_binder_fp "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

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
        mapping.put("kojyo", "kojyo");                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                               // 枝番
        mapping.put("binderyouekihinmei", "binderyouekihinmei");       // ﾊﾞｲﾝﾀﾞｰ溶液品名
        mapping.put("binderyouekilotno", "binderyouekilotno");         // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        mapping.put("lotkubun", "lotkubun");                           // ﾛｯﾄ区分
        mapping.put("filterkoukannichiji", "filterkoukannichiji");     // ﾌｨﾙﾀｰ交換日時
        mapping.put("filterhinmei", "filterhinmei");                   // ﾌｨﾙﾀｰ品名
        mapping.put("fplotno", "fplotno");                             // LotNo
        mapping.put("toritukehonsuu", "toritukehonsuu");               // 取り付け本数
        mapping.put("fpjyunbi_tantousya", "fpjyunbi_tantousya");       // F/P準備_担当者
        mapping.put("fpkaisinichiji", "fpkaisinichiji");               // F/P開始日時
        mapping.put("fpkaisi_tantousya", "fpkaisi_tantousya");         // F/P開始_担当者
        mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");         // F/P終了日時
        mapping.put("fpsyuuryou_tantousya", "fpsyuuryou_tantousya");   // F/P終了_担当者
        mapping.put("fpjikan", "fpjikan");                             // F/P時間
        mapping.put("bikou1", "bikou1");                               // 備考1
        mapping.put("bikou2", "bikou2");                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                   // 更新日時
        mapping.put("revision", "revision");                           // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrBinderFp>> beanHandler = new BeanListHandler<>(SrBinderFp.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrBinderFp> loadTmpSrBinderFp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,filterkoukannichiji,filterhinmei,"
                + "fplotno,toritukehonsuu,fpjyunbi_tantousya,fpkaisinichiji,fpkaisi_tantousya,fpsyuuryounichiji,"
                + "fpsyuuryou_tantousya,fpjikan,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_binder_fp "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? ";

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
        mapping.put("kojyo", "kojyo");                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                               // 枝番
        mapping.put("binderyouekihinmei", "binderyouekihinmei");       // ﾊﾞｲﾝﾀﾞｰ溶液品名
        mapping.put("binderyouekilotno", "binderyouekilotno");         // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        mapping.put("lotkubun", "lotkubun");                           // ﾛｯﾄ区分
        mapping.put("filterkoukannichiji", "filterkoukannichiji");     // ﾌｨﾙﾀｰ交換日時
        mapping.put("filterhinmei", "filterhinmei");                   // ﾌｨﾙﾀｰ品名
        mapping.put("fplotno", "fplotno");                             // LotNo
        mapping.put("toritukehonsuu", "toritukehonsuu");               // 取り付け本数
        mapping.put("fpjyunbi_tantousya", "fpjyunbi_tantousya");       // F/P準備_担当者
        mapping.put("fpkaisinichiji", "fpkaisinichiji");               // F/P開始日時
        mapping.put("fpkaisi_tantousya", "fpkaisi_tantousya");         // F/P開始_担当者
        mapping.put("fpsyuuryounichiji", "fpsyuuryounichiji");         // F/P終了日時
        mapping.put("fpsyuuryou_tantousya", "fpsyuuryou_tantousya");   // F/P終了_担当者
        mapping.put("fpjikan", "fpjikan");                             // F/P時間
        mapping.put("bikou1", "bikou1");                               // 備考1
        mapping.put("bikou2", "bikou2");                               // 備考2
        mapping.put("torokunichiji", "torokunichiji");                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                   // 更新日時
        mapping.put("revision", "revision");                           // revision
        mapping.put("deleteflag", "deleteflag");                       // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrBinderFp>> beanHandler = new BeanListHandler<>(SrBinderFp.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
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
        return listData.stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srBinderFp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrBinderFp srBinderFp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srBinderFp != null) {
            // 元データが存在する場合元データより取得
            return getSrBinderFpItemData(itemId, srBinderFp);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrBinderFp srBinderFp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srBinderFp != null) {
            // 元データが存在する場合元データより取得
            return getSrBinderFpItemData(itemId, srBinderFp);
        } else {
            return null;
        }
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
     * 原材料品質DB登録実績(fxhdd11)登録処理
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
    private void insertFxhdd11(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd11 ("
                + "torokusha ,toroku_date ,koshinsha ,koshin_date ,gamen_id ,rev ,kojyo ,lotno ,"
                + "edaban ,jissekino ,jotai_flg ,tsuika_kotei_flg "
                + ") VALUES ("
                + "? ,? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

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
     * 原材料品質DB登録実績(fxhdd11)更新処理
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
    private void updateFxhdd11(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, String jotaiFlg, Timestamp systemTime, int jissekino) throws SQLException {
        String sql = "UPDATE fxhdd11 SET "
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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_fp)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_binder_fp ( "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,filterkoukannichiji,filterhinmei,"
                + "fplotno,toritukehonsuu,fpjyunbi_tantousya,fpkaisinichiji,fpkaisi_tantousya,fpsyuuryounichiji,"
                + "fpsyuuryou_tantousya,fpjikan,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrBinderFp(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_fp)更新処理
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
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_binder_fp SET "
                + "binderyouekihinmei = ?,binderyouekilotno = ?,lotkubun = ?,filterkoukannichiji = ?,filterhinmei = ?,fplotno = ?,toritukehonsuu = ?,"
                + "fpjyunbi_tantousya = ?,fpkaisinichiji = ?,fpkaisi_tantousya = ?,fpsyuuryounichiji = ?,fpsyuuryou_tantousya = ?,fpjikan = ?,bikou1 = ?,"
                + "bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrBinderFp> srBinderFpList = getSrBinderFpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrBinderFp srBinderFp = null;
        if (!srBinderFpList.isEmpty()) {
            srBinderFp = srBinderFpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrBinderFp(false, newRev, 0, "", "", "", systemTime, processData, srBinderFp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_fp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_binder_fp "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_fp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srBinderFp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrBinderFp(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrBinderFp srBinderFp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // ﾌｨﾙﾀｰ交換日時
        String filterkoukanTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B015Const.FILTERKOUKAN_TIME, srBinderFp));
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B015Const.FPKAISI_TIME, srBinderFp));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B015Const.FPSYUURYOU_TIME, srBinderFp));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.BINDERYOUEKIHINMEI, srBinderFp))); // ﾊﾞｲﾝﾀﾞｰ溶液品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.BINDERYOUEKILOTNO, srBinderFp))); // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.LOTKUBUN, srBinderFp))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FILTERKOUKAN_DAY, srBinderFp),
                "".equals(filterkoukanTime) ? "0000" : filterkoukanTime)); // ﾌｨﾙﾀｰ交換日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B015Const.FILTERHINMEI, srBinderFp))); // ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPLOTNO, srBinderFp))); // LotNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.TORITUKEHONSUU, srBinderFp))); // 取り付け本数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPJYUNBI_TANTOUSYA, srBinderFp))); // F/P準備_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPKAISI_DAY, srBinderFp),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime)); // F/P開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPKAISI_TANTOUSYA, srBinderFp))); // F/P開始_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPSYUURYOU_DAY, srBinderFp),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime)); // F/P終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPSYUURYOU_TANTOUSYA, srBinderFp))); // F/P終了_担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.FPJIKAN, srBinderFp))); // F/P時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.BIKOU1, srBinderFp))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B015Const.BIKOU2, srBinderFp))); // 備考2

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev);         //revision
        params.add(deleteflag);     //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ(sr_binder_fp)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrBinderFp 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrBinderFp tmpSrBinderFp) throws SQLException {

        String sql = "INSERT INTO sr_binder_fp ( "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,filterkoukannichiji,filterhinmei,"
                + "fplotno,toritukehonsuu,fpjyunbi_tantousya,fpkaisinichiji,fpkaisi_tantousya,fpsyuuryounichiji,"
                + "fpsyuuryou_tantousya,fpjikan,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrBinderFp(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrBinderFp);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ(sr_binder_fp)更新処理
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
    private void updateSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_binder_fp SET "
                + "binderyouekihinmei = ?,binderyouekilotno = ?,lotkubun = ?,filterkoukannichiji = ?,filterhinmei = ?,fplotno = ?,toritukehonsuu = ?,"
                + "fpjyunbi_tantousya = ?,fpkaisinichiji = ?,fpkaisi_tantousya = ?,fpsyuuryounichiji = ?,fpsyuuryou_tantousya = ?,fpjikan = ?,bikou1 = ?,"
                + "bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrBinderFp> srBinderFpList = getSrBinderFpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrBinderFp srBinderFp = null;
        if (!srBinderFpList.isEmpty()) {
            srBinderFp = srBinderFpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrBinderFp(false, newRev, "", "", "", systemTime, processData, srBinderFp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ(sr_binder_fp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srBinderFp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrBinderFp(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrBinderFp srBinderFp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // ﾌｨﾙﾀｰ交換日時
        String filterkoukanTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B015Const.FILTERKOUKAN_TIME, srBinderFp));
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B015Const.FPKAISI_TIME, srBinderFp));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B015Const.FPSYUURYOU_TIME, srBinderFp));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.BINDERYOUEKIHINMEI, srBinderFp))); // ﾊﾞｲﾝﾀﾞｰ溶液品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.BINDERYOUEKILOTNO, srBinderFp))); // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.LOTKUBUN, srBinderFp))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B015Const.FILTERKOUKAN_DAY, srBinderFp),
                "".equals(filterkoukanTime) ? "0000" : filterkoukanTime)); // ﾌｨﾙﾀｰ交換日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B015Const.FILTERHINMEI, srBinderFp))); // ﾌｨﾙﾀｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.FPLOTNO, srBinderFp))); // LotNo
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B015Const.TORITUKEHONSUU, srBinderFp))); // 取り付け本数
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.FPJYUNBI_TANTOUSYA, srBinderFp))); // F/P準備_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B015Const.FPKAISI_DAY, srBinderFp),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime)); // F/P開始日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.FPKAISI_TANTOUSYA, srBinderFp))); // F/P開始_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B015Const.FPSYUURYOU_DAY, srBinderFp),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime)); // F/P終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.FPSYUURYOU_TANTOUSYA, srBinderFp))); // F/P終了_担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B015Const.FPJIKAN, srBinderFp))); // F/P時間
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.BIKOU1, srBinderFp))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B015Const.BIKOU2, srBinderFp))); // 備考2

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
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ(sr_binder_fp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_binder_fp "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

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
     * [ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_binder_fp "
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
     * @param fxhdd11RevInfo 品質DB登録実績データ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd11RevInfo) throws SQLException {

        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、データが存在する場合
            if (fxhdd11RevInfo != null && !fxhdd11RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
            }
        } else {
            // 品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev")))) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }
        }

        return null;

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
     * @param srBinderFp ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽデータ
     * @return DB値
     */
    private String getSrBinderFpItemData(String itemId, SrBinderFp srBinderFp) {
        switch (itemId) {
            // ﾊﾞｲﾝﾀﾞｰ溶液品名
            case GXHDO102B015Const.BINDERYOUEKIHINMEI:
                return StringUtil.nullToBlank(srBinderFp.getBinderyouekihinmei());

            // ﾊﾞｲﾝﾀﾞｰ溶液LotNo
            case GXHDO102B015Const.BINDERYOUEKILOTNO:
                return StringUtil.nullToBlank(srBinderFp.getBinderyouekilotno());

            // ﾛｯﾄ区分
            case GXHDO102B015Const.LOTKUBUN:
                return StringUtil.nullToBlank(srBinderFp.getLotkubun());

            // ﾌｨﾙﾀｰ交換日
            case GXHDO102B015Const.FILTERKOUKAN_DAY:
                return DateUtil.formattedTimestamp(srBinderFp.getFilterkoukannichiji(), "yyMMdd");

            // ﾌｨﾙﾀｰ交換時間
            case GXHDO102B015Const.FILTERKOUKAN_TIME:
                return DateUtil.formattedTimestamp(srBinderFp.getFilterkoukannichiji(), "HHmm");

            // ﾌｨﾙﾀｰ品名
            case GXHDO102B015Const.FILTERHINMEI:
                return StringUtil.nullToBlank(srBinderFp.getFilterhinmei());

            // LotNo
            case GXHDO102B015Const.FPLOTNO:
                return StringUtil.nullToBlank(srBinderFp.getFplotno());

            // 取り付け本数
            case GXHDO102B015Const.TORITUKEHONSUU:
                return StringUtil.nullToBlank(srBinderFp.getToritukehonsuu());

            // F/P準備_担当者
            case GXHDO102B015Const.FPJYUNBI_TANTOUSYA:
                return StringUtil.nullToBlank(srBinderFp.getFpjyunbi_tantousya());

            // F/P開始日
            case GXHDO102B015Const.FPKAISI_DAY:
                return DateUtil.formattedTimestamp(srBinderFp.getFpkaisinichiji(), "yyMMdd");

            // F/P開始時間
            case GXHDO102B015Const.FPKAISI_TIME:
                return DateUtil.formattedTimestamp(srBinderFp.getFpkaisinichiji(), "HHmm");

            // F/P開始_担当者
            case GXHDO102B015Const.FPKAISI_TANTOUSYA:
                return StringUtil.nullToBlank(srBinderFp.getFpkaisi_tantousya());

            // F/P終了日
            case GXHDO102B015Const.FPSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srBinderFp.getFpsyuuryounichiji(), "yyMMdd");

            // F/P終了時間
            case GXHDO102B015Const.FPSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srBinderFp.getFpsyuuryounichiji(), "HHmm");

            // F/P終了_担当者
            case GXHDO102B015Const.FPSYUURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srBinderFp.getFpsyuuryou_tantousya());

            // F/P時間
            case GXHDO102B015Const.FPJIKAN:
                return StringUtil.nullToBlank(srBinderFp.getFpjikan());

            // 備考1
            case GXHDO102B015Const.BIKOU1:
                return StringUtil.nullToBlank(srBinderFp.getBikou1());

            // 備考2
            case GXHDO102B015Const.BIKOU2:
                return StringUtil.nullToBlank(srBinderFp.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ溶液作製・ﾌｨﾙﾀｰﾊﾟｽ_仮登録(tmp_sr_binder_fp)登録処理(削除時)
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
    private void insertDeleteDataTmpSrBinderFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_binder_fp ("
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,filterkoukannichiji,filterhinmei,"
                + "fplotno,toritukehonsuu,fpjyunbi_tantousya,fpkaisinichiji,fpkaisi_tantousya,fpsyuuryounichiji,"
                + "fpsyuuryou_tantousya,fpjikan,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,binderyouekihinmei,binderyouekilotno,lotkubun,filterkoukannichiji,filterhinmei,"
                + "fplotno,toritukehonsuu,fpjyunbi_tantousya,fpkaisinichiji,fpkaisi_tantousya,fpsyuuryounichiji,"
                + "fpsyuuryou_tantousya,fpjikan,bikou1,bikou2,?,?,?,? "
                + " FROM sr_binder_fp "
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
     * 画面のBean情報を取得
     *
     * @param beanId フォームID
     * @return サブ画面情報
     */
    private Object getFormBean(String beanId) {
        return FacesContext.getCurrentInstance().
                getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().
                        getELContext(), null, beanId);
    }

    /**
     * 完了メッセージ設定処理
     *
     * @param message 完了メッセージ
     */
    private void setCompMessage(String message) {
        CompMessage compMessage = (CompMessage) getFormBean("beanCompMessage");
        compMessage.setCompMessage(message);
    }

    /**
     * 画面データ設定処理
     *
     * @param processData 処理制御データ
     */
    private void initGXHDO102B015A(ProcessData processData) {
        GXHDO102B015A bean = (GXHDO102B015A) getFormBean("gXHDO102B015A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B015Const.WIPLOTNO));
        bean.setBinderyouekihinmei(getItemRow(processData.getItemList(), GXHDO102B015Const.BINDERYOUEKIHINMEI));
        bean.setBinderyouekilotno(getItemRow(processData.getItemList(), GXHDO102B015Const.BINDERYOUEKILOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B015Const.LOTKUBUN));
        bean.setFilterkoukan_day(getItemRow(processData.getItemList(), GXHDO102B015Const.FILTERKOUKAN_DAY));
        bean.setFilterkoukan_time(getItemRow(processData.getItemList(), GXHDO102B015Const.FILTERKOUKAN_TIME));
        bean.setFilterhinmei(getItemRow(processData.getItemList(), GXHDO102B015Const.FILTERHINMEI));
        bean.setFplotno(getItemRow(processData.getItemList(), GXHDO102B015Const.FPLOTNO));
        bean.setToritukehonsuu(getItemRow(processData.getItemList(), GXHDO102B015Const.TORITUKEHONSUU));
        bean.setFpjyunbi_tantousya(getItemRow(processData.getItemList(), GXHDO102B015Const.FPJYUNBI_TANTOUSYA));
        bean.setFpkaisi_day(getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_DAY));
        bean.setFpkaisi_time(getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_TIME));
        bean.setFpkaisi_tantousya(getItemRow(processData.getItemList(), GXHDO102B015Const.FPKAISI_TANTOUSYA));
        bean.setFpsyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_DAY));
        bean.setFpsyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_TIME));
        bean.setFpsyuuryou_tantousya(getItemRow(processData.getItemList(), GXHDO102B015Const.FPSYUURYOU_TANTOUSYA));
        bean.setFpjikan(getItemRow(processData.getItemList(), GXHDO102B015Const.FPJIKAN));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B015Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B015Const.BIKOU2));

    }

    /**
     * 画面項目存在チェック(ﾌｫｰﾑﾊﾟﾗﾒｰﾀに対象の項目が存在していることを確認)
     *
     * @param processData 処理制御データ
     * @return エラー項目名リスト
     */
    private List<String> checkExistFormItem(ProcessData processData) {
        List<String> errorItemNameList = new ArrayList<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        // 項目IDリスト取得
        List<String> itemIdList = getItemIdList(processData, formId);
        Map<String, String> allItemIdMap = new HashMap<>();
        allItemIdMap.put(GXHDO102B015Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B015Const.BINDERYOUEKIHINMEI, "ﾊﾞｲﾝﾀﾞｰ溶液品名");
        allItemIdMap.put(GXHDO102B015Const.BINDERYOUEKILOTNO, "ﾊﾞｲﾝﾀﾞｰ溶液LotNo");
        allItemIdMap.put(GXHDO102B015Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B015Const.FILTERKOUKAN_DAY, "ﾌｨﾙﾀｰ交換日");
        allItemIdMap.put(GXHDO102B015Const.FILTERKOUKAN_TIME, "ﾌｨﾙﾀｰ交換時間");
        allItemIdMap.put(GXHDO102B015Const.FILTERHINMEI, "ﾌｨﾙﾀｰ品名");
        allItemIdMap.put(GXHDO102B015Const.FPLOTNO, "LotNo");
        allItemIdMap.put(GXHDO102B015Const.TORITUKEHONSUU, "取り付け本数");
        allItemIdMap.put(GXHDO102B015Const.FPJYUNBI_TANTOUSYA, "F/P準備_担当者");
        allItemIdMap.put(GXHDO102B015Const.FPKAISI_DAY, "F/P開始日");
        allItemIdMap.put(GXHDO102B015Const.FPKAISI_TIME, "F/P開始時間");
        allItemIdMap.put(GXHDO102B015Const.FPKAISI_TANTOUSYA, "F/P開始_担当者");
        allItemIdMap.put(GXHDO102B015Const.FPSYUURYOU_DAY, "F/P終了日");
        allItemIdMap.put(GXHDO102B015Const.FPSYUURYOU_TIME, "F/P終了時間");
        allItemIdMap.put(GXHDO102B015Const.FPSYUURYOU_TANTOUSYA, "F/P終了_担当者");
        allItemIdMap.put(GXHDO102B015Const.FPJIKAN, "F/P時間");
        allItemIdMap.put(GXHDO102B015Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B015Const.BIKOU2, "備考2");

        // 項目IDリストに存在しない画面項目を取得
        List<String> notExistItemidList = allItemIdMap.keySet().stream().filter(itemId -> !itemIdList.contains(itemId)).collect(Collectors.toList());
        notExistItemidList.forEach((notExistItemid) -> {
            errorItemNameList.add(allItemIdMap.get(notExistItemid));
        });

        List<String> errorMassageList = new ArrayList<>();
        if (0 < errorItemNameList.size()) {
            errorMassageList.add("以下の画面項目に対する情報が設定されていません。ｼｽﾃﾑに連絡してください。");
            errorMassageList.add("【対象項目】");
            errorMassageList.addAll(errorItemNameList);
        }

        return errorMassageList;
    }

    /**
     * 項目IDリスト取得
     *
     * @param processData 処理制御データ
     * @param formId 項目定義情報
     * @return 項目IDリスト
     */
    private List<String> getItemIdList(ProcessData processData, String formId) {
        try {
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            String sql = "SELECT item_id itemId "
                    + " FROM fxhdd01 "
                    + " WHERE gamen_id = ? "
                    + " ORDER BY item_no ";

            List<Object> params = new ArrayList<>();
            params.add(formId);

            List<Map<String, Object>> mapList = queryRunnerDoc.query(sql, new MapListHandler(), params.toArray());
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return mapList.stream().map(n -> n.get("itemId").toString()).collect(Collectors.toList());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;
    }
}
