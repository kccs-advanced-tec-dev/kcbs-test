/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import jp.co.kccs.xhd.db.model.SrTenkaFp;
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
 * 変更日	2021/10/22<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B012(添加材ｽﾗﾘｰ作製・FP排出)
 *
 * @author KCSS K.Jo
 * @since 2021/10/22
 */
public class GXHDO102B012 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B012.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B012() {
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
            initGXHDO102B012A(processData);

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
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO102B012Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B012Const.BTN_FPKAISINICHIJI_TOP,
                    GXHDO102B012Const.BTN_FPTEISINICHIJI_TOP,
                    GXHDO102B012Const.BTN_FPSAIKAINICHIJI_TOP,
                    GXHDO102B012Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B012Const.BTN_FPKAISINICHIJI_BOTTOM,
                    GXHDO102B012Const.BTN_FPTEISINICHIJI_BOTTOM,
                    GXHDO102B012Const.BTN_FPSAIKAINICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B012Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B012Const.BTN_INSERT_TOP,
                    GXHDO102B012Const.BTN_DELETE_TOP,
                    GXHDO102B012Const.BTN_UPDATE_TOP,
                    GXHDO102B012Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B012Const.BTN_INSERT_BOTTOM,
                    GXHDO102B012Const.BTN_DELETE_BOTTOM,
                    GXHDO102B012Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B012Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B012Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B012Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B012Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B012Const.BTN_INSERT_TOP:
            case GXHDO102B012Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B012Const.BTN_UPDATE_TOP:
            case GXHDO102B012Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B012Const.BTN_DELETE_TOP:
            case GXHDO102B012Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // F/P開始日時
            case GXHDO102B012Const.BTN_FPKAISINICHIJI_TOP:
            case GXHDO102B012Const.BTN_FPKAISINICHIJI_BOTTOM:
                method = "setFpkaisinichijiDateTime";
                break;
            // F/P停止日時
            case GXHDO102B012Const.BTN_FPTEISINICHIJI_TOP:
            case GXHDO102B012Const.BTN_FPTEISINICHIJI_BOTTOM:
                method = "setFpteisinichijiDateTime";
                break;
            // F/P再開日時
            case GXHDO102B012Const.BTN_FPSAIKAINICHIJI_TOP:
            case GXHDO102B012Const.BTN_FPSAIKAINICHIJI_BOTTOM:
                method = "setFpsaikainichijiDateTime";
                break;
            // F/P終了日時
            case GXHDO102B012Const.BTN_FPSYUURYOUNICHIJI_TOP:
            case GXHDO102B012Const.BTN_FPSYUURYOUNICHIJI_BOTTOM:
                method = "setFpsyuuryounichijiDateTime";
                break;
            // 添加材ｽﾗﾘｰ重量①計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU1_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU1_BOTTOM:
                method = "doTenkazaislurryjyuuryou1Keisan";
                break;
            // 添加材ｽﾗﾘｰ重量②計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU2_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU2_BOTTOM:
                method = "doTenkazaislurryjyuuryou2Keisan";
                break;
            // 添加材ｽﾗﾘｰ重量③計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU3_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU3_BOTTOM:
                method = "doTenkazaislurryjyuuryou3Keisan";
                break;
            // 添加材ｽﾗﾘｰ重量④計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU4_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU4_BOTTOM:
                method = "doTenkazaislurryjyuuryou4Keisan";
                break;
            // 添加材ｽﾗﾘｰ重量⑤計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU5_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU5_BOTTOM:
                method = "doTenkazaislurryjyuuryou5Keisan";
                break;
            // 添加材ｽﾗﾘｰ重量⑥計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU6_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU6_BOTTOM:
                method = "doTenkazaislurryjyuuryou6Keisan";
                break;
            // 歩留まり計算
            case GXHDO102B012Const.BTN_BUDOMARI_TOP:
            case GXHDO102B012Const.BTN_BUDOMARI_BOTTOM:
                method = "doBudomariKeisan";
                break;
            // 添加材ｽﾗﾘｰ有効期限計算
            case GXHDO102B012Const.BTN_TENKAZAISLURRYYUUKOUKIGEN_TOP:
            case GXHDO102B012Const.BTN_TENKAZAISLURRYYUUKOUKIGEN_BOTTOM:
                method = "doTenkazaislurryyuukoukigenKeisan";
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

            // 添加材ｽﾗﾘｰ作製・FP排出の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrTenkaFp> srTenkaYouzaiDataList = getSrTenkaFpData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srTenkaYouzaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTenkaYouzaiDataList.get(0));

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

                // 添加材ｽﾗﾘｰ作製・FP排出_仮登録処理
                insertTmpSrTenkaFp(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // 添加材ｽﾗﾘｰ作製・FP排出_仮登録更新処理
                updateTmpSrTenkaFp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_DAY); // F/P開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_TIME); // F/P開始時間
        Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_TIME); // F/P終了時間
        Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001("F/P開始日時", kaisiDate, "F/P終了日時", syuuryouDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // 循環開始日時、循環終了日時前後チェック
        itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_DAY); // F/P停止日
        itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_TIME); // F/P停止時間
        kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
        itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_DAY); // F/P再開日
        itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_TIME); // F/P再開時間
        syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
        //R001チェック呼出し
        msgCheckR001 = validateUtil.checkR001("F/P停止日時", kaisiDate, "F/P再開日時", syuuryouDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ：保存用ｻﾝﾌﾟﾙ回収、排出容器の内袋、洗浄確認
        List<String> itemIdList = Arrays.asList(GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU, GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO, GXHDO102B012Const.SENJYOUKAKUNIN);

        for (String itemId : itemIdList) {
            FXHDD01 itemFxhdd01 = getItemRow(processData.getItemList(), itemId);
            if (!hasCheck(itemFxhdd01)) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFxhdd01);
                return MessageUtil.getErrorMessageInfo("XHD-000199", true, true, errFxhdd01List, itemFxhdd01.getLabel1());
            }
        }
        return null;
    }

    /**
     * ﾁｪｯｸﾎﾞｯｸｽがﾁｪｯｸされているかﾁｪｯｸ。
     *
     * @param itemFxhdd01 項目データ
     * @return チェック結果(true:エラーなし、false:エラー有り)
     */
    private boolean hasCheck(FXHDD01 itemFxhdd01) {
        if (itemFxhdd01 == null) {
            return true;
        }
        return "true".equals(StringUtil.nullToBlank(itemFxhdd01.getValue()).toLowerCase());
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
            SrTenkaFp tmpSrTenkaFp = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTenkaFp> srTenkaFpList = getSrTenkaFpData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srTenkaFpList.isEmpty()) {
                    tmpSrTenkaFp = srTenkaFpList.get(0);
                }

                deleteTmpSrTenkaFp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 添加材ｽﾗﾘｰ作製・FP排出_登録処理
            insertSrTenkaFp(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaFp);
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
        processData.setUserAuthParam(GXHDO102B012Const.USER_AUTH_UPDATE_PARAM);

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

            // 添加材ｽﾗﾘｰ作製・FP排出_更新処理
            updateSrTenkaFp(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        processData.setUserAuthParam(GXHDO102B012Const.USER_AUTH_DELETE_PARAM);

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

            // 添加材ｽﾗﾘｰ作製・FP排出_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban, paramJissekino);
            insertDeleteDataTmpSrTenkaFp(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・FP排出_削除処理
            deleteSrTenkaFp(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P停止日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpteisinichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * F/P再開日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFpsaikainichijiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_TIME);
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
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_TIME); // F/P終了時間
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            // 【F/P終了日時】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
            ErrorMessageInfo checkItemErrorInfo = checkFpsyuuryounichijiDateTime(processData);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            // F/P終了日時設定
            setDateTimeItem(itemDay, itemTime, new Date());
            // 「F/P時間」計算処理
            setFpjikanItem(processData);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【F/P終了日時】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkFpsyuuryounichijiDateTime(ProcessData processData) {
        FXHDD01 itemFpkaisiDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_DAY); // F/P開始日
        FXHDD01 itemFpkaisiTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_TIME); // F/P開始時間

        // 「F/P開始日」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFpkaisiDay.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiDay);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFpkaisiDay.getLabel1());
        }
        // 「F/P開始時間」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFpkaisiTime.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpkaisiTime);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFpkaisiTime.getLabel1());
        }

        FXHDD01 itemFpteisiDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_DAY); // F/P停止日
        FXHDD01 itemFpteisiTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_TIME); // F/P停止時間
        FXHDD01 itemFpsaikaiDay = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_DAY); // F/P再開日
        FXHDD01 itemFpsaikaiTime = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_TIME); // F/P再開時間
        List<FXHDD01> itemList = Arrays.asList(itemFpteisiDay, itemFpteisiTime, itemFpsaikaiDay, itemFpsaikaiTime);
        ArrayList<FXHDD01> errorItemList = new ArrayList<>();
        // 「F/P停止日、F/P停止時間、F/P再開日、F/P再開時間」がすべて入力されているかﾁｪｯｸ
        itemList.stream().filter((item) -> (StringUtil.isEmpty(item.getValue()))).forEachOrdered((item) -> {
            errorItemList.add(item);
        });
        if (!errorItemList.isEmpty()) {
            StringBuilder errorMessageParams = new StringBuilder();
            List<FXHDD01> errorFxhdd01List = new ArrayList<>();
            errorItemList.stream().map((item) -> {
                // エラー情報の追加
                if (!StringUtil.isEmpty(errorMessageParams.toString())) {
                    // 追加された項目が既に存在している場合、エラーメッセージに分割文字「,」を追加
                    errorMessageParams.append(",");
                }
                errorMessageParams.append(item.getLabel1());
                return item;
            }).forEachOrdered((item) -> {
                errorFxhdd01List.add(item);
            });
            if (!errorFxhdd01List.isEmpty()) {
                return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errorFxhdd01List, errorMessageParams.toString());
            }
        }
        return null;
    }

    /**
     * 「F/P時間」計算処理
     *
     * @param processData 処理制御データ
     */
    private void setFpjikanItem(ProcessData processData) {
        FXHDD01 itemFpjikan = getItemRow(processData.getItemList(), GXHDO102B012Const.FPJIKAN); // F/P時間
        // Dateオブジェクト変換
        Date fpsyuuryouDate = getDateTimeItem(processData, GXHDO102B012Const.FPSYUURYOU_DAY, GXHDO102B012Const.FPSYUURYOU_TIME); // F/P終了日時
        Date fpkaisiDate = getDateTimeItem(processData, GXHDO102B012Const.FPKAISI_DAY, GXHDO102B012Const.FPKAISI_TIME); // F/P開始日時
        Date fpteisiDate = getDateTimeItem(processData, GXHDO102B012Const.FPTEISI_DAY, GXHDO102B012Const.FPTEISI_TIME); // F/P停止日時
        Date fpsaikaiDate = getDateTimeItem(processData, GXHDO102B012Const.FPSAIKAI_DAY, GXHDO102B012Const.FPSAIKAI_TIME); // F/P再開日時
        if (fpsyuuryouDate == null || fpkaisiDate == null || fpteisiDate == null || fpsaikaiDate == null) {
            itemFpjikan.setValue("");
            return;
        }
        // (「F/P終了日+F/P終了時間」 - 「F/P開始日+F/P開始時間」) - (「F/P再開日+F/P再開時間」 - 「F/P停止日+F/P停止時間」)(　時間　分)
        BigDecimal diffMinutes = BigDecimal.valueOf(DateUtil.diffMinutes(fpkaisiDate, fpsyuuryouDate)).subtract(BigDecimal.valueOf(DateUtil.diffMinutes(fpteisiDate, fpsaikaiDate)));
        itemFpjikan.setValue(diffMinutes.toPlainString());
    }

    /**
     * 日付文字列⇒Dateオブジェクト変換
     *
     * @param itemDay 項目日付(日)
     * @param itemTime 項目日付(時間)
     * @return 変換後のデータ
     */
    private Date getDateTimeItem(ProcessData processData, String dayItemId, String timeItemId) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), dayItemId); // F/P終了日
        FXHDD01 itemTime = getItemRow(processData.getItemList(), timeItemId); // F/P終了時間
        // Dateオブジェクト変換
        Date dateVal = DateUtil.convertStringToDate(itemDay.getValue(), itemTime.getValue()); // F/P終了日時
        return dateVal;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量①計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryjyuuryou1Keisan(ProcessData processData) {
        FXHDD01 itemSoujyuurou = getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU1); // 総重量①
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU1); // 風袋重量①
        // 【添加材ｽﾗﾘｰ重量①計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuuryouKeisan(itemSoujyuurou, itemFuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量①」計算処理
        calcTenkazaislurryjyuuryou(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1, itemSoujyuurou, itemFuutaijyuuryou);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量②計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryjyuuryou2Keisan(ProcessData processData) {
        FXHDD01 itemSoujyuurou = getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU2); // 総重量②
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU2); // 風袋重量②
        // 【添加材ｽﾗﾘｰ重量②計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuuryouKeisan(itemSoujyuurou, itemFuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量②」計算処理
        calcTenkazaislurryjyuuryou(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2, itemSoujyuurou, itemFuutaijyuuryou);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量③計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryjyuuryou3Keisan(ProcessData processData) {
        FXHDD01 itemSoujyuurou = getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU3); // 総重量③
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU3); // 風袋重量③
        // 【添加材ｽﾗﾘｰ重量③計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuuryouKeisan(itemSoujyuurou, itemFuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量③」計算処理
        calcTenkazaislurryjyuuryou(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3, itemSoujyuurou, itemFuutaijyuuryou);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量④計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryjyuuryou4Keisan(ProcessData processData) {
        FXHDD01 itemSoujyuurou = getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU4); // 総重量④
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU4); // 風袋重量④
        // 【添加材ｽﾗﾘｰ重量④計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuuryouKeisan(itemSoujyuurou, itemFuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量④」計算処理
        calcTenkazaislurryjyuuryou(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4, itemSoujyuurou, itemFuutaijyuuryou);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量⑤計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryjyuuryou5Keisan(ProcessData processData) {
        FXHDD01 itemSoujyuurou = getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU5); // 総重量⑤
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU5); // 風袋重量⑤
        // 【添加材ｽﾗﾘｰ重量⑤計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuuryouKeisan(itemSoujyuurou, itemFuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量⑤」計算処理
        calcTenkazaislurryjyuuryou(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5, itemSoujyuurou, itemFuutaijyuuryou);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量⑥計算】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryjyuuryou6Keisan(ProcessData processData) {
        FXHDD01 itemSoujyuurou = getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU6); // 総重量⑥
        FXHDD01 itemFuutaijyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU6); // 風袋重量⑥
        // 【添加材ｽﾗﾘｰ重量⑥計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryjyuuryouKeisan(itemSoujyuurou, itemFuutaijyuuryou);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「添加材ｽﾗﾘｰ重量⑥」計算処理
        calcTenkazaislurryjyuuryou(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6, itemSoujyuurou, itemFuutaijyuuryou);
        return processData;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemSoujyuurou 総重量
     * @param itemFuutaijyuuryou 風袋重量
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkTenkazaislurryjyuuryouKeisan(FXHDD01 itemSoujyuurou, FXHDD01 itemFuutaijyuuryou) {
        // 「総重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSoujyuurou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuurou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSoujyuurou.getLabel1());
        }
        // 「風袋重量」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFuutaijyuuryou.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemFuutaijyuuryou.getLabel1());
        }
        // [総重量]<[風袋重量]場合エラー
        BigDecimal soujyuurou = new BigDecimal(itemSoujyuurou.getValue());
        BigDecimal fuutaijyuuryou = new BigDecimal(itemFuutaijyuuryou.getValue());
        if (soujyuurou.compareTo(fuutaijyuuryou) < 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSoujyuurou, itemFuutaijyuuryou);
            return MessageUtil.getErrorMessageInfo("XHD-000023", true, false, errFxhdd01List, itemSoujyuurou.getLabel1(), itemFuutaijyuuryou.getLabel1());
        }
        return null;
    }

    /**
     * 【添加材ｽﾗﾘｰ重量計算】ﾎﾞﾀﾝ押下時計算処理
     *
     * @param processData 処理制御データ
     * @param tenkazaislurryjyuuryouItemId 添加材ｽﾗﾘｰ重量項目ID
     * @param itemSoujyuurou 総重量
     * @param itemFuutaijyuuryou 風袋重量
     */
    private void calcTenkazaislurryjyuuryou(ProcessData processData, String tenkazaislurryjyuuryouItemId, FXHDD01 itemSoujyuurou, FXHDD01 itemFuutaijyuuryou) {
        try {
            FXHDD01 itemTenkazaislurryjyuuryou = getItemRow(processData.getItemList(), tenkazaislurryjyuuryouItemId); // 添加材ｽﾗﾘｰ重量
            FXHDD01 itemTenkazaislurryjyuuryougoukei = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI); // 添加材ｽﾗﾘｰ重量合計
            FXHDD01 itemTenkazaislurryjyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1); // 添加材ｽﾗﾘｰ重量①
            FXHDD01 itemTenkazaislurryjyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2); // 添加材ｽﾗﾘｰ重量②
            FXHDD01 itemTenkazaislurryjyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3); // 添加材ｽﾗﾘｰ重量③
            FXHDD01 itemTenkazaislurryjyuuryou4 = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4); // 添加材ｽﾗﾘｰ重量④
            FXHDD01 itemTenkazaislurryjyuuryou5 = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5); // 添加材ｽﾗﾘｰ重量⑤
            FXHDD01 itemTenkazaislurryjyuuryou6 = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6); // 添加材ｽﾗﾘｰ重量⑥

            BigDecimal itemSoujyuurouVal = new BigDecimal(itemSoujyuurou.getValue()); // 総重量
            BigDecimal itemFuutaijyuuryouVal = new BigDecimal(itemFuutaijyuuryou.getValue()); // 風袋重量
            // 「添加材ｽﾗﾘｰ重量」計算処理: 総重量-風袋重量
            itemTenkazaislurryjyuuryou.setValue(itemSoujyuurouVal.subtract(itemFuutaijyuuryouVal).toPlainString());

            BigDecimal itemTenkazaislurryjyuuryou1Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemTenkazaislurryjyuuryou1.getValue()); // 添加材ｽﾗﾘｰ重量①
            BigDecimal itemTenkazaislurryjyuuryou1Va2 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemTenkazaislurryjyuuryou2.getValue()); // 添加材ｽﾗﾘｰ重量②
            BigDecimal itemTenkazaislurryjyuuryou1Va3 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemTenkazaislurryjyuuryou3.getValue()); // 添加材ｽﾗﾘｰ重量③
            BigDecimal itemTenkazaislurryjyuuryou1Va4 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemTenkazaislurryjyuuryou4.getValue()); // 添加材ｽﾗﾘｰ重量④
            BigDecimal itemTenkazaislurryjyuuryou1Va5 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemTenkazaislurryjyuuryou5.getValue()); // 添加材ｽﾗﾘｰ重量⑤
            BigDecimal itemTenkazaislurryjyuuryou1Va6 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemTenkazaislurryjyuuryou6.getValue()); // 添加材ｽﾗﾘｰ重量⑥
            // 「添加材ｽﾗﾘｰ重量合計」計算処理:添加材ｽﾗﾘｰ重量① + 添加材ｽﾗﾘｰ重量② + 添加材ｽﾗﾘｰ重量③ + 添加材ｽﾗﾘｰ重量④ + 添加材ｽﾗﾘｰ重量⑤ + 添加材ｽﾗﾘｰ重量⑥
            if (itemTenkazaislurryjyuuryougoukei != null) {
                BigDecimal itemTenkazaislurryjyuuryougoukeiVal = itemTenkazaislurryjyuuryou1Va1.add(itemTenkazaislurryjyuuryou1Va2).add(itemTenkazaislurryjyuuryou1Va3).add(
                        itemTenkazaislurryjyuuryou1Va4).add(itemTenkazaislurryjyuuryou1Va5).add(itemTenkazaislurryjyuuryou1Va6);
                itemTenkazaislurryjyuuryougoukei.setValue(itemTenkazaislurryjyuuryougoukeiVal.toPlainString());
            }

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("添加材ｽﾗﾘｰ重量計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 歩留まり計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBudomariKeisan(ProcessData processData) {
        // 「添加材ｽﾗﾘｰ重量合計」
        FXHDD01 itemTenkazaislurryjyuuryougoukei = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI);
        // 投入量
        FXHDD01 itemTounyuuryou = getItemRow(processData.getItemList(), GXHDO102B012Const.TOUNYUURYOU);
        if (itemTenkazaislurryjyuuryougoukei != null && itemTounyuuryou != null) {
            // 歩留まり計算チェック処理
            ErrorMessageInfo checkItemErrorInfo = checkBudomariKeisan(itemTenkazaislurryjyuuryougoukei, itemTounyuuryou);
            if (checkItemErrorInfo != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
                return processData;
            }
            //歩留まり計算処理
            calcBudomari(processData, itemTenkazaislurryjyuuryougoukei, itemTounyuuryou);
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 【歩留まり計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemTenkazaislurryjyuuryougoukei 添加材ｽﾗﾘｰ重量合計
     * @param itemTounyuuryou 投入量
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkBudomariKeisan(FXHDD01 itemTenkazaislurryjyuuryougoukei, FXHDD01 itemTounyuuryou) {
        // 投入量の規格値
        BigDecimal itemTounyuuryouVal = ValidateUtil.getItemKikakuChiCheckVal(itemTounyuuryou);
        // 「添加材ｽﾗﾘｰ重量合計」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemTenkazaislurryjyuuryougoukei.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTenkazaislurryjyuuryougoukei);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, itemTenkazaislurryjyuuryougoukei.getLabel1());
        }
        // 「投入量の規格値」ﾁｪｯｸ
        if (itemTounyuuryouVal == null || BigDecimal.ZERO.compareTo(itemTounyuuryouVal) == 0) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemTounyuuryou);
            itemTounyuuryou.setBackColor3(ErrUtil.ERR_BACK_COLOR);
            return MessageUtil.getErrorMessageInfo("XHD-000181", true, true, errFxhdd01List, itemTounyuuryou.getLabel1());
        }
        return null;
    }

    /**
     * 歩留まり計算
     *
     * @param processData 処理制御データ
     * @param itemTenkazaislurryjyuuryougoukei 添加材ｽﾗﾘｰ重量合計
     * @param itemTounyuuryou 投入量
     */
    private void calcBudomari(ProcessData processData, FXHDD01 itemTenkazaislurryjyuuryougoukei, FXHDD01 itemTounyuuryou) {
        try {
            FXHDD01 itemBudomari = getItemRow(processData.getItemList(), GXHDO102B012Const.BUDOMARI); // 歩留まり
            BigDecimal itemGarasukaisyuujyuuryouVal = new BigDecimal(itemTenkazaislurryjyuuryougoukei.getValue()); // 添加材ｽﾗﾘｰ重量合計
            BigDecimal itemTounyuuryouVal = ValidateUtil.getItemKikakuChiCheckVal(itemTounyuuryou);// 投入量の規格値

            // 「添加材ｽﾗﾘｰ重量合計」 ÷ 「投入量の規格値」 * 100(小数点第三位を四捨五入) → 式を変換して先に100を乗算
            BigDecimal budomari = itemGarasukaisyuujyuuryouVal.multiply(BigDecimal.valueOf(100)).divide(itemTounyuuryouVal, 2, RoundingMode.HALF_UP);

            //計算結果を誤差率にセット
            itemBudomari.setValue(budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("歩留まり計算にエラー発生", ex, LOGGER);
        }
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限計算
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTenkazaislurryyuukoukigenKeisan(ProcessData processData) {
        FXHDD01 itemFpsyuuryou_day = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_DAY); // F/P終了日
        FXHDD01 itemFpsyuuryou_time = getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_TIME); // F/P終了時間
        // 添加材ｽﾗﾘｰ有効期限計算ﾁｪｯｸ処理
        ErrorMessageInfo checkItemErrorInfo = checkTenkazaislurryyuukoukigenKeisan(processData, itemFpsyuuryou_day, itemFpsyuuryou_time);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 添加材ｽﾗﾘｰ有効期限計算処理
        calcTenkazaislurryyuukoukigen(processData, itemFpsyuuryou_day, itemFpsyuuryou_time);
        return processData;
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限計算ﾁｪｯｸ処理
     *
     * @param processData 処理制御データ
     * @param itemFpsyuuryou_day F/P終了日
     * @param itemFpsyuuryou_time F/P終了時間
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkTenkazaislurryyuukoukigenKeisan(ProcessData processData, FXHDD01 itemFpsyuuryou_day, FXHDD01 itemFpsyuuryou_time) {
        // 「F/P終了日」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFpsyuuryou_day.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpsyuuryou_day);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, MessageUtil.getMessage("fpsyuuryounichiji"));
        }
        // 「F/P終了時間」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemFpsyuuryou_time.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemFpsyuuryou_time);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, true, errFxhdd01List, MessageUtil.getMessage("fpsyuuryounichiji"));
        }

        return null;
    }

    /**
     * 添加材ｽﾗﾘｰ有効期限計算処理
     *
     * @param processData 処理制御データ
     * @param itemFpsyuuryou_day F/P終了日
     * @param itemFpsyuuryou_time F/P終了時間
     */
    private void calcTenkazaislurryyuukoukigen(ProcessData processData, FXHDD01 itemFpsyuuryou_day, FXHDD01 itemFpsyuuryou_time) {
        try {
            FXHDD01 itemTenkazaislurryyuukoukigen = getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN); // 添加材ｽﾗﾘｰ有効期限
            // 「F/P終了日時」 + 「90」
            Date dateTime = DateUtil.addJikan(itemFpsyuuryou_day.getValue(), itemFpsyuuryou_time.getValue(), 90, Calendar.DATE);
            if (dateTime != null) {
                itemTenkazaislurryyuukoukigen.setValue(new SimpleDateFormat("yyMMdd").format(dateTime));
            }
        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("添加材ｽﾗﾘｰ有効期限計算にエラー発生", ex, LOGGER);
        }
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
                        GXHDO102B012Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B012Const.BTN_FPKAISINICHIJI_TOP,
                        GXHDO102B012Const.BTN_FPTEISINICHIJI_TOP,
                        GXHDO102B012Const.BTN_FPSAIKAINICHIJI_TOP,
                        GXHDO102B012Const.BTN_FPSYUURYOUNICHIJI_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU1_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU2_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU3_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU4_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU5_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU6_TOP,
                        GXHDO102B012Const.BTN_BUDOMARI_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYYUUKOUKIGEN_TOP,
                        GXHDO102B012Const.BTN_UPDATE_TOP,
                        GXHDO102B012Const.BTN_DELETE_TOP,
                        GXHDO102B012Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B012Const.BTN_FPKAISINICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_FPTEISINICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_FPSAIKAINICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_FPSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU1_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU2_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU3_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU4_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU5_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU6_BOTTOM,
                        GXHDO102B012Const.BTN_BUDOMARI_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYYUUKOUKIGEN_BOTTOM,
                        GXHDO102B012Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B012Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B012Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B012Const.BTN_INSERT_TOP,
                        GXHDO102B012Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B012Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B012Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B012Const.BTN_FPKAISINICHIJI_TOP,
                        GXHDO102B012Const.BTN_FPTEISINICHIJI_TOP,
                        GXHDO102B012Const.BTN_FPSAIKAINICHIJI_TOP,
                        GXHDO102B012Const.BTN_FPSYUURYOUNICHIJI_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU1_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU2_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU3_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU4_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU5_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU6_TOP,
                        GXHDO102B012Const.BTN_BUDOMARI_TOP,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYYUUKOUKIGEN_TOP,
                        GXHDO102B012Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B012Const.BTN_INSERT_TOP,
                        GXHDO102B012Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B012Const.BTN_FPKAISINICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_FPTEISINICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_FPSAIKAINICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_FPSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU1_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU2_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU3_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU4_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU5_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYJYUURYOU6_BOTTOM,
                        GXHDO102B012Const.BTN_BUDOMARI_BOTTOM,
                        GXHDO102B012Const.BTN_TENKAZAISLURRYYUUKOUKIGEN_BOTTOM,
                        GXHDO102B012Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B012Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B012Const.BTN_UPDATE_TOP,
                        GXHDO102B012Const.BTN_DELETE_TOP,
                        GXHDO102B012Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B012Const.BTN_DELETE_BOTTOM
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
        // 添加材ｽﾗﾘｰ重量合計と歩留り計算項目の表示制御
        setItemRendered(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, shikakariData, lotNo);
        // 画面のラベル項目の値の背景色を取得できない場合、デフォルト値を設置
        processData.getItemList().stream().map((item) -> {
            if (item.isRender1() || item.isRenderLinkButton()) {
                if ("".equals(StringUtil.nullToBlank(item.getBackColor3()))) {
                    item.setBackColor3("#EEEEEE");
                }
                if (0 == item.getFontSize3()) {
                    item.setFontSize3(16);
                }
            }
            return item;
        }).filter((item) -> (item.isRenderOutputLabel() && !item.isRenderInputText())).map((item) -> {
            if ("".equals(StringUtil.nullToBlank(item.getBackColorInput()))) {
                item.setBackColorInput("#EEEEEE");
                item.setBackColorInputDefault("#EEEEEE");
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
        this.setItemData(processData, GXHDO102B012Const.WIPLOTNO, lotNo);
        // 添加材ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 添加材ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B012Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B012Const.LOTKUBUN, lotkubuncode);
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

        List<SrTenkaFp> srTenkaFpList = new ArrayList<>();
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

            // 添加材ｽﾗﾘｰ作製・FP排出データ取得
            srTenkaFpList = getSrTenkaFpData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srTenkaFpList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTenkaFpList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTenkaFpList.get(0));
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTenkaFp 添加材ｽﾗﾘｰ作製・FP排出
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTenkaFp srTenkaFp) {

        // 最終パス回数
        this.setItemData(processData, GXHDO102B012Const.SAISYUUPASSKAISUU, getSrTenkaFpItemData(GXHDO102B012Const.SAISYUUPASSKAISUU, srTenkaFp));

        // 保存用ｻﾝﾌﾟﾙ回収
        this.setItemData(processData, GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU, getSrTenkaFpItemData(GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU, srTenkaFp));

        // 風袋重量①
        this.setItemData(processData, GXHDO102B012Const.FUUTAIJYUURYOU1, getSrTenkaFpItemData(GXHDO102B012Const.FUUTAIJYUURYOU1, srTenkaFp));

        // 風袋重量②
        this.setItemData(processData, GXHDO102B012Const.FUUTAIJYUURYOU2, getSrTenkaFpItemData(GXHDO102B012Const.FUUTAIJYUURYOU2, srTenkaFp));

        // 風袋重量③
        this.setItemData(processData, GXHDO102B012Const.FUUTAIJYUURYOU3, getSrTenkaFpItemData(GXHDO102B012Const.FUUTAIJYUURYOU3, srTenkaFp));

        // 風袋重量④
        this.setItemData(processData, GXHDO102B012Const.FUUTAIJYUURYOU4, getSrTenkaFpItemData(GXHDO102B012Const.FUUTAIJYUURYOU4, srTenkaFp));

        // 風袋重量⑤
        this.setItemData(processData, GXHDO102B012Const.FUUTAIJYUURYOU5, getSrTenkaFpItemData(GXHDO102B012Const.FUUTAIJYUURYOU5, srTenkaFp));

        // 風袋重量⑥
        this.setItemData(processData, GXHDO102B012Const.FUUTAIJYUURYOU6, getSrTenkaFpItemData(GXHDO102B012Const.FUUTAIJYUURYOU6, srTenkaFp));

        // F/P準備_担当者
        this.setItemData(processData, GXHDO102B012Const.FPJYUNBI_TANTOUSYA, getSrTenkaFpItemData(GXHDO102B012Const.FPJYUNBI_TANTOUSYA, srTenkaFp));

        // F/P準備_LotNo①
        this.setItemData(processData, GXHDO102B012Const.FPJYUNBI_LOTNO1, getSrTenkaFpItemData(GXHDO102B012Const.FPJYUNBI_LOTNO1, srTenkaFp));

        // F/P準備_取り付け本数①
        this.setItemData(processData, GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1, getSrTenkaFpItemData(GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1, srTenkaFp));

        // F/P準備_LotNo②
        this.setItemData(processData, GXHDO102B012Const.FPJYUNBI_LOTNO2, getSrTenkaFpItemData(GXHDO102B012Const.FPJYUNBI_LOTNO2, srTenkaFp));

        // F/P準備_取り付け本数②
        this.setItemData(processData, GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2, getSrTenkaFpItemData(GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2, srTenkaFp));

        // 排出容器の内袋
        this.setItemData(processData, GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO, getSrTenkaFpItemData(GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO, srTenkaFp));

        // F/PﾀﾝｸNo
        this.setItemData(processData, GXHDO102B012Const.FPTANKNO, getSrTenkaFpItemData(GXHDO102B012Const.FPTANKNO, srTenkaFp));

        // 洗浄確認
        this.setItemData(processData, GXHDO102B012Const.SENJYOUKAKUNIN, getSrTenkaFpItemData(GXHDO102B012Const.SENJYOUKAKUNIN, srTenkaFp));

        // F/P開始日
        this.setItemData(processData, GXHDO102B012Const.FPKAISI_DAY, getSrTenkaFpItemData(GXHDO102B012Const.FPKAISI_DAY, srTenkaFp));

        // F/P開始時間
        this.setItemData(processData, GXHDO102B012Const.FPKAISI_TIME, getSrTenkaFpItemData(GXHDO102B012Const.FPKAISI_TIME, srTenkaFp));

        // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        this.setItemData(processData, GXHDO102B012Const.ASSOUREGULATORNO, getSrTenkaFpItemData(GXHDO102B012Const.ASSOUREGULATORNO, srTenkaFp));

        // 圧送圧力
        this.setItemData(processData, GXHDO102B012Const.ASSOUATURYOKU, getSrTenkaFpItemData(GXHDO102B012Const.ASSOUATURYOKU, srTenkaFp));

        // F/P開始_担当者
        this.setItemData(processData, GXHDO102B012Const.FPKAISI_TANTOUSYA, getSrTenkaFpItemData(GXHDO102B012Const.FPKAISI_TANTOUSYA, srTenkaFp));

        // F/P停止日
        this.setItemData(processData, GXHDO102B012Const.FPTEISI_DAY, getSrTenkaFpItemData(GXHDO102B012Const.FPTEISI_DAY, srTenkaFp));

        // F/P停止時間
        this.setItemData(processData, GXHDO102B012Const.FPTEISI_TIME, getSrTenkaFpItemData(GXHDO102B012Const.FPTEISI_TIME, srTenkaFp));

        // F/P交換_LotNo①
        this.setItemData(processData, GXHDO102B012Const.FPKOUKAN_LOTNO1, getSrTenkaFpItemData(GXHDO102B012Const.FPKOUKAN_LOTNO1, srTenkaFp));

        // F/P交換_取り付け本数①
        this.setItemData(processData, GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1, getSrTenkaFpItemData(GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1, srTenkaFp));

        // F/P交換_LotNo②
        this.setItemData(processData, GXHDO102B012Const.FPKOUKAN_LOTNO2, getSrTenkaFpItemData(GXHDO102B012Const.FPKOUKAN_LOTNO2, srTenkaFp));

        // F/P交換_取り付け本数②
        this.setItemData(processData, GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2, getSrTenkaFpItemData(GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2, srTenkaFp));

        // F/P再開日
        this.setItemData(processData, GXHDO102B012Const.FPSAIKAI_DAY, getSrTenkaFpItemData(GXHDO102B012Const.FPSAIKAI_DAY, srTenkaFp));

        // F/P再開時間
        this.setItemData(processData, GXHDO102B012Const.FPSAIKAI_TIME, getSrTenkaFpItemData(GXHDO102B012Const.FPSAIKAI_TIME, srTenkaFp));

        // F/P交換_担当者
        this.setItemData(processData, GXHDO102B012Const.FPKOUKAN_TANTOUSYA, getSrTenkaFpItemData(GXHDO102B012Const.FPKOUKAN_TANTOUSYA, srTenkaFp));

        // F/P終了日
        this.setItemData(processData, GXHDO102B012Const.FPSYUURYOU_DAY, getSrTenkaFpItemData(GXHDO102B012Const.FPSYUURYOU_DAY, srTenkaFp));

        // F/P終了時間
        this.setItemData(processData, GXHDO102B012Const.FPSYUURYOU_TIME, getSrTenkaFpItemData(GXHDO102B012Const.FPSYUURYOU_TIME, srTenkaFp));

        // F/P時間
        this.setItemData(processData, GXHDO102B012Const.FPJIKAN, getSrTenkaFpItemData(GXHDO102B012Const.FPJIKAN, srTenkaFp));

        // F/P終了_担当者
        this.setItemData(processData, GXHDO102B012Const.FPSYUUROU_TANTOUSYA, getSrTenkaFpItemData(GXHDO102B012Const.FPSYUUROU_TANTOUSYA, srTenkaFp));

        // 総重量①
        this.setItemData(processData, GXHDO102B012Const.SOUJYUUROU1, getSrTenkaFpItemData(GXHDO102B012Const.SOUJYUUROU1, srTenkaFp));

        // 総重量②
        this.setItemData(processData, GXHDO102B012Const.SOUJYUUROU2, getSrTenkaFpItemData(GXHDO102B012Const.SOUJYUUROU2, srTenkaFp));

        // 総重量③
        this.setItemData(processData, GXHDO102B012Const.SOUJYUUROU3, getSrTenkaFpItemData(GXHDO102B012Const.SOUJYUUROU3, srTenkaFp));

        // 総重量④
        this.setItemData(processData, GXHDO102B012Const.SOUJYUUROU4, getSrTenkaFpItemData(GXHDO102B012Const.SOUJYUUROU4, srTenkaFp));

        // 総重量⑤
        this.setItemData(processData, GXHDO102B012Const.SOUJYUUROU5, getSrTenkaFpItemData(GXHDO102B012Const.SOUJYUUROU5, srTenkaFp));

        // 総重量⑥
        this.setItemData(processData, GXHDO102B012Const.SOUJYUUROU6, getSrTenkaFpItemData(GXHDO102B012Const.SOUJYUUROU6, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量①
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量②
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量③
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量④
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量⑤
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量⑥
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6, srTenkaFp));

        // 添加材ｽﾗﾘｰ重量合計
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI, srTenkaFp));

        // 歩留まり
        this.setItemData(processData, GXHDO102B012Const.BUDOMARI, getSrTenkaFpItemData(GXHDO102B012Const.BUDOMARI, srTenkaFp));

        // 添加材ｽﾗﾘｰ有効期限
        this.setItemData(processData, GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN, getSrTenkaFpItemData(GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN, srTenkaFp));

        // 粉砕判定
        this.setItemData(processData, GXHDO102B012Const.FUNSAIHANTEI, getSrTenkaFpItemData(GXHDO102B012Const.FUNSAIHANTEI, srTenkaFp));

        // 担当者
        this.setItemData(processData, GXHDO102B012Const.TANTOUSYA, getSrTenkaFpItemData(GXHDO102B012Const.TANTOUSYA, srTenkaFp));

    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・FP排出データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFp> getSrTenkaFpData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTenkaFp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrTenkaFp(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [添加材ｽﾗﾘｰ作製・FP排出]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFp> loadSrTenkaFp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,saisyuupasskaisuu,hozonyousamplekaisyuu,"
                + "fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,fuutaijyuuryou4,fuutaijyuuryou5,fuutaijyuuryou6,FPjyunbi_tantousya,"
                + "FPjyunbi_filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,haisyutuyoukinoutibukuro,filterkaisuu,FPtankno,senjyoukakunin,FPkaisinichiji,"
                + "assouregulatorNo,assouaturyoku,FPkaisi_tantousya,FPteisinichiji,FPkoukan_filterrenketu,FPkoukan_filterhinmei1,"
                + "FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,"
                + "FPsaikainichiji,FPkoukan_tantousya,FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuurou1,soujyuurou2,"
                + "soujyuurou3,soujyuurou4,soujyuurou5,soujyuurou6,tenkazaislurryjyuuryou1,tenkazaislurryjyuuryou2,tenkazaislurryjyuuryou3,"
                + "tenkazaislurryjyuuryou4,tenkazaislurryjyuuryou5,tenkazaislurryjyuuryou6,tenkazaislurryjyuuryougoukei,budomari,"
                + "tenkazaislurryyuukoukigen,funsaihantei,tantousya,torokunichiji,kosinnichiji,revision "
                + " FROM sr_tenka_fp "
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
        mapping.put("kojyo", "kojyo");                                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                               // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                   // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                     // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                           // ﾛｯﾄ区分
        mapping.put("saisyuupasskaisuu", "saisyuupasskaisuu");                         // 最終パス回数
        mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                 // 保存用ｻﾝﾌﾟﾙ回収
        mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");                             // 風袋重量①
        mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                             // 風袋重量②
        mapping.put("fuutaijyuuryou3", "fuutaijyuuryou3");                             // 風袋重量③
        mapping.put("fuutaijyuuryou4", "fuutaijyuuryou4");                             // 風袋重量④
        mapping.put("fuutaijyuuryou5", "fuutaijyuuryou5");                             // 風袋重量⑤
        mapping.put("fuutaijyuuryou6", "fuutaijyuuryou6");                             // 風袋重量⑥
        mapping.put("FPjyunbi_tantousya", "fpjyunbi_tantousya");                       // F/P準備_担当者
        mapping.put("FPjyunbi_filterrenketu", "fpjyunbi_filterrenketu");               // F/P準備_ﾌｨﾙﾀｰ連結
        mapping.put("FPjyunbi_filterhinmei1", "fpjyunbi_filterhinmei1");               // F/P準備_ﾌｨﾙﾀｰ品名①
        mapping.put("FPjyunbi_lotno1", "fpjyunbi_lotno1");                             // F/P準備_LotNo①
        mapping.put("FPjyunbi_toritukehonsuu1", "fpjyunbi_toritukehonsuu1");           // F/P準備_取り付け本数①
        mapping.put("FPjyunbi_filterhinmei2", "fpjyunbi_filterhinmei2");               // F/P準備_ﾌｨﾙﾀｰ品名①
        mapping.put("FPjyunbi_lotno2", "fpjyunbi_lotno2");                             // F/P準備_LotNo②
        mapping.put("FPjyunbi_toritukehonsuu2", "fpjyunbi_toritukehonsuu2");           // F/P準備_取り付け本数②
        mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");           // 排出容器の内袋
        mapping.put("filterkaisuu", "filterkaisuu");                                   // ﾌｨﾙﾀｰ使用回数
        mapping.put("FPtankno", "fptankno");                                           // F/PﾀﾝｸNo
        mapping.put("senjyoukakunin", "senjyoukakunin");                               // 洗浄確認
        mapping.put("FPkaisinichiji", "fpkaisinichiji");                               // F/P開始日時
        mapping.put("assouregulatorNo", "assouregulatorno");                           // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouaturyoku", "assouaturyoku");                                 // 圧送圧力
        mapping.put("FPkaisi_tantousya", "fpkaisi_tantousya");                         // F/P開始_担当者
        mapping.put("FPteisinichiji", "fpteisinichiji");                               // F/P停止日時
        mapping.put("FPkoukan_filterrenketu", "fpkoukan_filterrenketu");               // F/P交換_ﾌｨﾙﾀｰ連結
        mapping.put("FPkoukan_filterhinmei1", "fpkoukan_filterhinmei1");               // F/P交換_ﾌｨﾙﾀｰ品名①
        mapping.put("FPkoukan_lotno1", "fpkoukan_lotno1");                             // F/P交換_LotNo①
        mapping.put("FPkoukan_toritukehonsuu1", "fpkoukan_toritukehonsuu1");           // F/P交換_取り付け本数①
        mapping.put("FPkoukan_filterhinmei2", "fpkoukan_filterhinmei2");               // F/P交換_ﾌｨﾙﾀｰ品名①
        mapping.put("FPkoukan_lotno2", "fpkoukan_lotno2");                             // F/P交換_LotNo②
        mapping.put("FPkoukan_toritukehonsuu2", "fpkoukan_toritukehonsuu2");           // F/P交換_取り付け本数②
        mapping.put("FPsaikainichiji", "fpsaikainichiji");                             // F/P再開日時
        mapping.put("FPkoukan_tantousya", "fpkoukan_tantousya");                       // F/P交換_担当者
        mapping.put("FPsyuuryounichiji", "fpsyuuryounichiji");                         // F/P終了日時
        mapping.put("FPjikan", "fpjikan");                                             // F/P時間
        mapping.put("FPsyuurou_tantousya", "fpsyuurou_tantousya");                     // F/P終了_担当者
        mapping.put("soujyuurou1", "soujyuurou1");                                     // 総重量①
        mapping.put("soujyuurou2", "soujyuurou2");                                     // 総重量②
        mapping.put("soujyuurou3", "soujyuurou3");                                     // 総重量③
        mapping.put("soujyuurou4", "soujyuurou4");                                     // 総重量④
        mapping.put("soujyuurou5", "soujyuurou5");                                     // 総重量⑤
        mapping.put("soujyuurou6", "soujyuurou6");                                     // 総重量⑥
        mapping.put("tenkazaislurryjyuuryou1", "tenkazaislurryjyuuryou1");             // 添加材ｽﾗﾘｰ重量①
        mapping.put("tenkazaislurryjyuuryou2", "tenkazaislurryjyuuryou2");             // 添加材ｽﾗﾘｰ重量②
        mapping.put("tenkazaislurryjyuuryou3", "tenkazaislurryjyuuryou3");             // 添加材ｽﾗﾘｰ重量③
        mapping.put("tenkazaislurryjyuuryou4", "tenkazaislurryjyuuryou4");             // 添加材ｽﾗﾘｰ重量④
        mapping.put("tenkazaislurryjyuuryou5", "tenkazaislurryjyuuryou5");             // 添加材ｽﾗﾘｰ重量⑤
        mapping.put("tenkazaislurryjyuuryou6", "tenkazaislurryjyuuryou6");             // 添加材ｽﾗﾘｰ重量⑥
        mapping.put("tenkazaislurryjyuuryougoukei", "tenkazaislurryjyuuryougoukei");   // 添加材ｽﾗﾘｰ重量合計
        mapping.put("budomari", "budomari");                                           // 歩留まり
        mapping.put("tenkazaislurryyuukoukigen", "tenkazaislurryyuukoukigen");         // 添加材ｽﾗﾘｰ有効期限
        mapping.put("funsaihantei", "funsaihantei");                                   // 粉砕判定
        mapping.put("tantousya", "tantousya");                                         // 担当者
        mapping.put("torokunichiji", "torokunichiji");                                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                   // 更新日時
        mapping.put("revision", "revision");                                           // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaFp>> beanHandler = new BeanListHandler<>(SrTenkaFp.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・FP排出_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaFp> loadTmpSrTenkaFp(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,saisyuupasskaisuu,hozonyousamplekaisyuu,"
                + "fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,fuutaijyuuryou4,fuutaijyuuryou5,fuutaijyuuryou6,FPjyunbi_tantousya,"
                + "FPjyunbi_filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,haisyutuyoukinoutibukuro,filterkaisuu,FPtankno,senjyoukakunin,FPkaisinichiji,"
                + "assouregulatorNo,assouaturyoku,FPkaisi_tantousya,FPteisinichiji,FPkoukan_filterrenketu,FPkoukan_filterhinmei1,"
                + "FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,"
                + "FPsaikainichiji,FPkoukan_tantousya,FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuurou1,soujyuurou2,"
                + "soujyuurou3,soujyuurou4,soujyuurou5,soujyuurou6,tenkazaislurryjyuuryou1,tenkazaislurryjyuuryou2,tenkazaislurryjyuuryou3,"
                + "tenkazaislurryjyuuryou4,tenkazaislurryjyuuryou5,tenkazaislurryjyuuryou6,tenkazaislurryjyuuryougoukei,budomari,"
                + "tenkazaislurryyuukoukigen,funsaihantei,tantousya,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_tenka_fp "
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
        mapping.put("kojyo", "kojyo");                                                 // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                 // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                               // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                   // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                     // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                           // ﾛｯﾄ区分
        mapping.put("saisyuupasskaisuu", "saisyuupasskaisuu");                         // 最終パス回数
        mapping.put("hozonyousamplekaisyuu", "hozonyousamplekaisyuu");                 // 保存用ｻﾝﾌﾟﾙ回収
        mapping.put("fuutaijyuuryou1", "fuutaijyuuryou1");                             // 風袋重量①
        mapping.put("fuutaijyuuryou2", "fuutaijyuuryou2");                             // 風袋重量②
        mapping.put("fuutaijyuuryou3", "fuutaijyuuryou3");                             // 風袋重量③
        mapping.put("fuutaijyuuryou4", "fuutaijyuuryou4");                             // 風袋重量④
        mapping.put("fuutaijyuuryou5", "fuutaijyuuryou5");                             // 風袋重量⑤
        mapping.put("fuutaijyuuryou6", "fuutaijyuuryou6");                             // 風袋重量⑥
        mapping.put("FPjyunbi_tantousya", "fpjyunbi_tantousya");                       // F/P準備_担当者
        mapping.put("FPjyunbi_filterrenketu", "fpjyunbi_filterrenketu");               // F/P準備_ﾌｨﾙﾀｰ連結
        mapping.put("FPjyunbi_filterhinmei1", "fpjyunbi_filterhinmei1");               // F/P準備_ﾌｨﾙﾀｰ品名①
        mapping.put("FPjyunbi_lotno1", "fpjyunbi_lotno1");                             // F/P準備_LotNo①
        mapping.put("FPjyunbi_toritukehonsuu1", "fpjyunbi_toritukehonsuu1");           // F/P準備_取り付け本数①
        mapping.put("FPjyunbi_filterhinmei2", "fpjyunbi_filterhinmei2");               // F/P準備_ﾌｨﾙﾀｰ品名①
        mapping.put("FPjyunbi_lotno2", "fpjyunbi_lotno2");                             // F/P準備_LotNo②
        mapping.put("FPjyunbi_toritukehonsuu2", "fpjyunbi_toritukehonsuu2");           // F/P準備_取り付け本数②
        mapping.put("haisyutuyoukinoutibukuro", "haisyutuyoukinoutibukuro");           // 排出容器の内袋
        mapping.put("filterkaisuu", "filterkaisuu");                                   // ﾌｨﾙﾀｰ使用回数
        mapping.put("FPtankno", "fptankno");                                           // F/PﾀﾝｸNo
        mapping.put("senjyoukakunin", "senjyoukakunin");                               // 洗浄確認
        mapping.put("FPkaisinichiji", "fpkaisinichiji");                               // F/P開始日時
        mapping.put("assouregulatorNo", "assouregulatorno");                           // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        mapping.put("assouaturyoku", "assouaturyoku");                                 // 圧送圧力
        mapping.put("FPkaisi_tantousya", "fpkaisi_tantousya");                         // F/P開始_担当者
        mapping.put("FPteisinichiji", "fpteisinichiji");                               // F/P停止日時
        mapping.put("FPkoukan_filterrenketu", "fpkoukan_filterrenketu");               // F/P交換_ﾌｨﾙﾀｰ連結
        mapping.put("FPkoukan_filterhinmei1", "fpkoukan_filterhinmei1");               // F/P交換_ﾌｨﾙﾀｰ品名①
        mapping.put("FPkoukan_lotno1", "fpkoukan_lotno1");                             // F/P交換_LotNo①
        mapping.put("FPkoukan_toritukehonsuu1", "fpkoukan_toritukehonsuu1");           // F/P交換_取り付け本数①
        mapping.put("FPkoukan_filterhinmei2", "fpkoukan_filterhinmei2");               // F/P交換_ﾌｨﾙﾀｰ品名①
        mapping.put("FPkoukan_lotno2", "fpkoukan_lotno2");                             // F/P交換_LotNo②
        mapping.put("FPkoukan_toritukehonsuu2", "fpkoukan_toritukehonsuu2");           // F/P交換_取り付け本数②
        mapping.put("FPsaikainichiji", "fpsaikainichiji");                             // F/P再開日時
        mapping.put("FPkoukan_tantousya", "fpkoukan_tantousya");                       // F/P交換_担当者
        mapping.put("FPsyuuryounichiji", "fpsyuuryounichiji");                         // F/P終了日時
        mapping.put("FPjikan", "fpjikan");                                             // F/P時間
        mapping.put("FPsyuurou_tantousya", "fpsyuurou_tantousya");                     // F/P終了_担当者
        mapping.put("soujyuurou1", "soujyuurou1");                                     // 総重量①
        mapping.put("soujyuurou2", "soujyuurou2");                                     // 総重量②
        mapping.put("soujyuurou3", "soujyuurou3");                                     // 総重量③
        mapping.put("soujyuurou4", "soujyuurou4");                                     // 総重量④
        mapping.put("soujyuurou5", "soujyuurou5");                                     // 総重量⑤
        mapping.put("soujyuurou6", "soujyuurou6");                                     // 総重量⑥
        mapping.put("tenkazaislurryjyuuryou1", "tenkazaislurryjyuuryou1");             // 添加材ｽﾗﾘｰ重量①
        mapping.put("tenkazaislurryjyuuryou2", "tenkazaislurryjyuuryou2");             // 添加材ｽﾗﾘｰ重量②
        mapping.put("tenkazaislurryjyuuryou3", "tenkazaislurryjyuuryou3");             // 添加材ｽﾗﾘｰ重量③
        mapping.put("tenkazaislurryjyuuryou4", "tenkazaislurryjyuuryou4");             // 添加材ｽﾗﾘｰ重量④
        mapping.put("tenkazaislurryjyuuryou5", "tenkazaislurryjyuuryou5");             // 添加材ｽﾗﾘｰ重量⑤
        mapping.put("tenkazaislurryjyuuryou6", "tenkazaislurryjyuuryou6");             // 添加材ｽﾗﾘｰ重量⑥
        mapping.put("tenkazaislurryjyuuryougoukei", "tenkazaislurryjyuuryougoukei");   // 添加材ｽﾗﾘｰ重量合計
        mapping.put("budomari", "budomari");                                           // 歩留まり
        mapping.put("tenkazaislurryyuukoukigen", "tenkazaislurryyuukoukigen");         // 添加材ｽﾗﾘｰ有効期限
        mapping.put("funsaihantei", "funsaihantei");                                   // 粉砕判定
        mapping.put("tantousya", "tantousya");                                         // 担当者
        mapping.put("torokunichiji", "torokunichiji");                                 // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                   // 更新日時
        mapping.put("revision", "revision");                                           // revision
        mapping.put("deleteflag", "deleteflag");                                       // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaFp>> beanHandler = new BeanListHandler<>(SrTenkaFp.class, rowProcessor);

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
     * @param srTenkaFp 添加材ｽﾗﾘｰ作製・FP排出データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTenkaFp srTenkaFp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTenkaFp != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaFpItemData(itemId, srTenkaFp);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo 添加材ｽﾗﾘｰ作製・FP排出データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrTenkaFp srTenkaFp) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srTenkaFp != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaFpItemData(itemId, srTenkaFp);
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
     * 添加材ｽﾗﾘｰ作製・FP排出_仮登録(tmp_sr_tenka_fp)登録処理
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
    private void insertTmpSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_fp ( "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,saisyuupasskaisuu,hozonyousamplekaisyuu,"
                + "fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,fuutaijyuuryou4,fuutaijyuuryou5,fuutaijyuuryou6,FPjyunbi_tantousya,"
                + "FPjyunbi_filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,haisyutuyoukinoutibukuro,filterkaisuu,FPtankno,senjyoukakunin,FPkaisinichiji,"
                + "assouregulatorNo,assouaturyoku,FPkaisi_tantousya,FPteisinichiji,FPkoukan_filterrenketu,FPkoukan_filterhinmei1,"
                + "FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,"
                + "FPsaikainichiji,FPkoukan_tantousya,FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuurou1,soujyuurou2,"
                + "soujyuurou3,soujyuurou4,soujyuurou5,soujyuurou6,tenkazaislurryjyuuryou1,tenkazaislurryjyuuryou2,tenkazaislurryjyuuryou3,"
                + "tenkazaislurryjyuuryou4,tenkazaislurryjyuuryou5,tenkazaislurryjyuuryou6,tenkazaislurryjyuuryougoukei,budomari,"
                + "tenkazaislurryyuukoukigen,funsaihantei,tantousya,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrTenkaFp(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出_仮登録(tmp_sr_tenka_fp)更新処理
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
    private void updateTmpSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_tenka_fp SET "
                + "tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,saisyuupasskaisuu = ?,hozonyousamplekaisyuu = ?,fuutaijyuuryou1 = ?,"
                + "fuutaijyuuryou2 = ?,fuutaijyuuryou3 = ?,fuutaijyuuryou4 = ?,fuutaijyuuryou5 = ?,fuutaijyuuryou6 = ?,FPjyunbi_tantousya = ?,"
                + "FPjyunbi_filterrenketu = ?,FPjyunbi_filterhinmei1 = ?,FPjyunbi_lotno1 = ?,FPjyunbi_toritukehonsuu1 = ?,FPjyunbi_filterhinmei2 = ?,"
                + "FPjyunbi_lotno2 = ?,FPjyunbi_toritukehonsuu2 = ?,haisyutuyoukinoutibukuro = ?,filterkaisuu = ?,FPtankno = ?,senjyoukakunin = ?,"
                + "FPkaisinichiji = ?,assouregulatorNo = ?,assouaturyoku = ?,FPkaisi_tantousya = ?,FPteisinichiji = ?,FPkoukan_filterrenketu = ?,"
                + "FPkoukan_filterhinmei1 = ?,FPkoukan_lotno1 = ?,FPkoukan_toritukehonsuu1 = ?,FPkoukan_filterhinmei2 = ?,FPkoukan_lotno2 = ?,"
                + "FPkoukan_toritukehonsuu2 = ?,FPsaikainichiji = ?,FPkoukan_tantousya = ?,FPsyuuryounichiji = ?,FPjikan = ?,FPsyuurou_tantousya = ?,"
                + "soujyuurou1 = ?,soujyuurou2 = ?,soujyuurou3 = ?,soujyuurou4 = ?,soujyuurou5 = ?,soujyuurou6 = ?,tenkazaislurryjyuuryou1 = ?,"
                + "tenkazaislurryjyuuryou2 = ?,tenkazaislurryjyuuryou3 = ?,tenkazaislurryjyuuryou4 = ?,tenkazaislurryjyuuryou5 = ?,tenkazaislurryjyuuryou6 = ?,"
                + "tenkazaislurryjyuuryougoukei = ?,budomari = ?,tenkazaislurryyuukoukigen = ?,funsaihantei = ?,tantousya = ?,kosinnichiji = ?,"
                + "revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFp> srTenkaFpList = getSrTenkaFpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFp srTenkaFp = null;
        if (!srTenkaFpList.isEmpty()) {
            srTenkaFp = srTenkaFpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTenkaFp(false, newRev, 0, "", "", "", systemTime, processData, srTenkaFp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出_仮登録(tmp_sr_tenka_fp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_tenka_fp "
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
     * 添加材ｽﾗﾘｰ作製・FP排出_仮登録(tmp_sr_tenka_fp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTenkaFp 添加材ｽﾗﾘｰ作製・FP排出データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTenkaFp(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFp srTenkaFp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPKAISI_TIME, srTenkaFp));
        // F/P停止日時
        String fpteisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPTEISI_TIME, srTenkaFp));
        // F/P再開日時
        String fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPSAIKAI_TIME, srTenkaFp));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPSYUURYOU_TIME, srTenkaFp));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYHINMEI, srTenkaFp))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYLOTNO, srTenkaFp))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.LOTKUBUN, srTenkaFp))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SAISYUUPASSKAISUU, srTenkaFp))); // 最終パス回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU, srTenkaFp), null)); // 保存用ｻﾝﾌﾟﾙ回収
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU1, srTenkaFp))); // 風袋重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU2, srTenkaFp))); // 風袋重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU3, srTenkaFp))); // 風袋重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU4, srTenkaFp))); // 風袋重量④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU5, srTenkaFp))); // 風袋重量⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU6, srTenkaFp))); // 風袋重量⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_TANTOUSYA, srTenkaFp))); // F/P準備_担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FPJYUNBI_FILTERRENKETU, srTenkaFp))); // F/P準備_ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FPJYUNBI_FILTERHINMEI1, srTenkaFp))); // F/P準備_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_LOTNO1, srTenkaFp))); // F/P準備_LotNo①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1, srTenkaFp))); // F/P準備_取り付け本数①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FPJYUNBI_FILTERHINMEI2, srTenkaFp))); // F/P準備_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_LOTNO2, srTenkaFp))); // F/P準備_LotNo②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2, srTenkaFp))); // F/P準備_取り付け本数②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO, srTenkaFp), null)); // 排出容器の内袋
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FILTERKAISUU, srTenkaFp))); // ﾌｨﾙﾀｰ使用回数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPTANKNO, srTenkaFp))); // F/PﾀﾝｸNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B012Const.SENJYOUKAKUNIN, srTenkaFp), null)); // 洗浄確認
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKAISI_DAY, srTenkaFp),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime)); // F/P開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.ASSOUREGULATORNO, srTenkaFp))); // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.ASSOUATURYOKU, srTenkaFp))); // 圧送圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKAISI_TANTOUSYA, srTenkaFp))); // F/P開始_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPTEISI_DAY, srTenkaFp),
                "".equals(fpteisiTime) ? "0000" : fpteisiTime)); // F/P停止日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FPKOUKAN_FILTERRENKETU, srTenkaFp))); // F/P交換_ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FPKOUKAN_FILTERHINMEI1, srTenkaFp))); // F/P交換_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_LOTNO1, srTenkaFp))); // F/P交換_LotNo①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1, srTenkaFp))); // F/P交換_取り付け本数①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B012Const.FPKOUKAN_FILTERHINMEI2, srTenkaFp))); // F/P交換_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_LOTNO2, srTenkaFp))); // F/P交換_LotNo②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2, srTenkaFp))); // F/P交換_取り付け本数②
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPSAIKAI_DAY, srTenkaFp),
                "".equals(fpsaikaiTime) ? "0000" : fpsaikaiTime)); // F/P再開日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_TANTOUSYA, srTenkaFp))); // F/P交換_担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPSYUURYOU_DAY, srTenkaFp),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime)); // F/P終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPJIKAN, srTenkaFp))); // F/P時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.FPSYUUROU_TANTOUSYA, srTenkaFp))); // F/P終了_担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU1, srTenkaFp))); // 総重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU2, srTenkaFp))); // 総重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU3, srTenkaFp))); // 総重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU4, srTenkaFp))); // 総重量④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU5, srTenkaFp))); // 総重量⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU6, srTenkaFp))); // 総重量⑥
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1, srTenkaFp))); // 添加材ｽﾗﾘｰ重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2, srTenkaFp))); // 添加材ｽﾗﾘｰ重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3, srTenkaFp))); // 添加材ｽﾗﾘｰ重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4, srTenkaFp))); // 添加材ｽﾗﾘｰ重量④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5, srTenkaFp))); // 添加材ｽﾗﾘｰ重量⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6, srTenkaFp))); // 添加材ｽﾗﾘｰ重量⑥
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI, srTenkaFp))); // 添加材ｽﾗﾘｰ重量合計
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.BUDOMARI, srTenkaFp))); // 歩留まり
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN, srTenkaFp))); // 添加材ｽﾗﾘｰ有効期限
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B012Const.FUNSAIHANTEI, srTenkaFp), null)); // 粉砕判定
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B012Const.TANTOUSYA, srTenkaFp))); // 担当者

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
     * 添加材ｽﾗﾘｰ作製・FP排出(sr_tenka_fp)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrTenkaFp 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaFp tmpSrTenkaFp) throws SQLException {

        String sql = "INSERT INTO sr_tenka_fp ( "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,saisyuupasskaisuu,hozonyousamplekaisyuu,"
                + "fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,fuutaijyuuryou4,fuutaijyuuryou5,fuutaijyuuryou6,FPjyunbi_tantousya,"
                + "FPjyunbi_filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,haisyutuyoukinoutibukuro,filterkaisuu,FPtankno,senjyoukakunin,FPkaisinichiji,"
                + "assouregulatorNo,assouaturyoku,FPkaisi_tantousya,FPteisinichiji,FPkoukan_filterrenketu,FPkoukan_filterhinmei1,"
                + "FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,"
                + "FPsaikainichiji,FPkoukan_tantousya,FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuurou1,soujyuurou2,"
                + "soujyuurou3,soujyuurou4,soujyuurou5,soujyuurou6,tenkazaislurryjyuuryou1,tenkazaislurryjyuuryou2,tenkazaislurryjyuuryou3,"
                + "tenkazaislurryjyuuryou4,tenkazaislurryjyuuryou5,tenkazaislurryjyuuryou6,tenkazaislurryjyuuryougoukei,budomari,"
                + "tenkazaislurryyuukoukigen,funsaihantei,tantousya,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrTenkaFp(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrTenkaFp);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出(sr_tenka_fp)更新処理
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
    private void updateSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_tenka_fp SET "
                + "tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,saisyuupasskaisuu = ?,hozonyousamplekaisyuu = ?,fuutaijyuuryou1 = ?,"
                + "fuutaijyuuryou2 = ?,fuutaijyuuryou3 = ?,fuutaijyuuryou4 = ?,fuutaijyuuryou5 = ?,fuutaijyuuryou6 = ?,FPjyunbi_tantousya = ?,"
                + "FPjyunbi_filterrenketu = ?,FPjyunbi_filterhinmei1 = ?,FPjyunbi_lotno1 = ?,FPjyunbi_toritukehonsuu1 = ?,FPjyunbi_filterhinmei2 = ?,"
                + "FPjyunbi_lotno2 = ?,FPjyunbi_toritukehonsuu2 = ?,haisyutuyoukinoutibukuro = ?,filterkaisuu = ?,FPtankno = ?,senjyoukakunin = ?,"
                + "FPkaisinichiji = ?,assouregulatorNo = ?,assouaturyoku = ?,FPkaisi_tantousya = ?,FPteisinichiji = ?,FPkoukan_filterrenketu = ?,"
                + "FPkoukan_filterhinmei1 = ?,FPkoukan_lotno1 = ?,FPkoukan_toritukehonsuu1 = ?,FPkoukan_filterhinmei2 = ?,FPkoukan_lotno2 = ?,"
                + "FPkoukan_toritukehonsuu2 = ?,FPsaikainichiji = ?,FPkoukan_tantousya = ?,FPsyuuryounichiji = ?,FPjikan = ?,FPsyuurou_tantousya = ?,"
                + "soujyuurou1 = ?,soujyuurou2 = ?,soujyuurou3 = ?,soujyuurou4 = ?,soujyuurou5 = ?,soujyuurou6 = ?,tenkazaislurryjyuuryou1 = ?,"
                + "tenkazaislurryjyuuryou2 = ?,tenkazaislurryjyuuryou3 = ?,tenkazaislurryjyuuryou4 = ?,tenkazaislurryjyuuryou5 = ?,tenkazaislurryjyuuryou6 = ?,"
                + "tenkazaislurryjyuuryougoukei = ?,budomari = ?,tenkazaislurryyuukoukigen = ?,funsaihantei = ?,tantousya = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaFp> srTenkaFpList = getSrTenkaFpData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaFp srTenkaFp = null;
        if (!srTenkaFpList.isEmpty()) {
            srTenkaFp = srTenkaFpList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTenkaFp(false, newRev, "", "", "", systemTime, processData, srTenkaFp);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出(sr_tenka_fp)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaFp 添加材ｽﾗﾘｰ作製・FP排出データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTenkaFp(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaFp srTenkaFp) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // F/P開始日時
        String fpkaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPKAISI_TIME, srTenkaFp));
        // F/P停止日時
        String fpteisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPTEISI_TIME, srTenkaFp));
        // F/P再開日時
        String fpsaikaiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPSAIKAI_TIME, srTenkaFp));
        // F/P終了日時
        String fpsyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B012Const.FPSYUURYOU_TIME, srTenkaFp));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYHINMEI, srTenkaFp))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYLOTNO, srTenkaFp))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.LOTKUBUN, srTenkaFp))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SAISYUUPASSKAISUU, srTenkaFp))); // 最終パス回数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU, srTenkaFp), 9)); // 保存用ｻﾝﾌﾟﾙ回収
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU1, srTenkaFp))); // 風袋重量①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU2, srTenkaFp))); // 風袋重量②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU3, srTenkaFp))); // 風袋重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU4, srTenkaFp))); // 風袋重量④
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU5, srTenkaFp))); // 風袋重量⑤
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FUUTAIJYUURYOU6, srTenkaFp))); // 風袋重量⑥
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_TANTOUSYA, srTenkaFp))); // F/P準備_担当者
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FPJYUNBI_FILTERRENKETU, srTenkaFp))); // F/P準備_ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FPJYUNBI_FILTERHINMEI1, srTenkaFp))); // F/P準備_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_LOTNO1, srTenkaFp))); // F/P準備_LotNo①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1, srTenkaFp))); // F/P準備_取り付け本数①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FPJYUNBI_FILTERHINMEI2, srTenkaFp))); // F/P準備_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_LOTNO2, srTenkaFp))); // F/P準備_LotNo②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2, srTenkaFp))); // F/P準備_取り付け本数②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO, srTenkaFp), 9)); // 排出容器の内袋
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FILTERKAISUU, srTenkaFp))); // ﾌｨﾙﾀｰ使用回数
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FPTANKNO, srTenkaFp))); // F/PﾀﾝｸNo
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B012Const.SENJYOUKAKUNIN, srTenkaFp), 9)); // 洗浄確認
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B012Const.FPKAISI_DAY, srTenkaFp),
                "".equals(fpkaisiTime) ? "0000" : fpkaisiTime)); // F/P開始日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.ASSOUREGULATORNO, srTenkaFp))); // 圧送ﾚｷﾞｭﾚｰﾀｰNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B012Const.ASSOUATURYOKU, srTenkaFp))); // 圧送圧力
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPKAISI_TANTOUSYA, srTenkaFp))); // F/P開始_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B012Const.FPTEISI_DAY, srTenkaFp),
                "".equals(fpteisiTime) ? "0000" : fpteisiTime)); // F/P停止日時
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FPKOUKAN_FILTERRENKETU, srTenkaFp))); // F/P交換_ﾌｨﾙﾀｰ連結
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FPKOUKAN_FILTERHINMEI1, srTenkaFp))); // F/P交換_ﾌｨﾙﾀｰ品名①
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_LOTNO1, srTenkaFp))); // F/P交換_LotNo①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1, srTenkaFp))); // F/P交換_取り付け本数①
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B012Const.FPKOUKAN_FILTERHINMEI2, srTenkaFp))); // F/P交換_ﾌｨﾙﾀｰ品名②
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_LOTNO2, srTenkaFp))); // F/P交換_LotNo②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2, srTenkaFp))); // F/P交換_取り付け本数②
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B012Const.FPSAIKAI_DAY, srTenkaFp),
                "".equals(fpsaikaiTime) ? "0000" : fpsaikaiTime)); // F/P再開日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPKOUKAN_TANTOUSYA, srTenkaFp))); // F/P交換_担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B012Const.FPSYUURYOU_DAY, srTenkaFp),
                "".equals(fpsyuuryouTime) ? "0000" : fpsyuuryouTime)); // F/P終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPJIKAN, srTenkaFp))); // F/P時間
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.FPSYUUROU_TANTOUSYA, srTenkaFp))); // F/P終了_担当者
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU1, srTenkaFp))); // 総重量①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU2, srTenkaFp))); // 総重量②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU3, srTenkaFp))); // 総重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU4, srTenkaFp))); // 総重量④
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU5, srTenkaFp))); // 総重量⑤
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.SOUJYUUROU6, srTenkaFp))); // 総重量⑥
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1, srTenkaFp))); // 添加材ｽﾗﾘｰ重量①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2, srTenkaFp))); // 添加材ｽﾗﾘｰ重量②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3, srTenkaFp))); // 添加材ｽﾗﾘｰ重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4, srTenkaFp))); // 添加材ｽﾗﾘｰ重量④
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5, srTenkaFp))); // 添加材ｽﾗﾘｰ重量⑤
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6, srTenkaFp))); // 添加材ｽﾗﾘｰ重量⑥
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI, srTenkaFp))); // 添加材ｽﾗﾘｰ重量合計
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B012Const.BUDOMARI, srTenkaFp))); // 歩留まり
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN, srTenkaFp))); // 添加材ｽﾗﾘｰ有効期限
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B012Const.FUNSAIHANTEI, srTenkaFp), 9)); // 粉砕判定
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B012Const.TANTOUSYA, srTenkaFp))); // 担当者

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
     * 添加材ｽﾗﾘｰ作製・FP排出(sr_tenka_fp)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_tenka_fp "
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
     * [添加材ｽﾗﾘｰ作製・FP排出_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_tenka_fp "
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
     * @param srTenkaFp 添加材ｽﾗﾘｰ作製・FP排出データ
     * @return DB値
     */
    private String getSrTenkaFpItemData(String itemId, SrTenkaFp srTenkaFp) {
        switch (itemId) {
            // 添加材ｽﾗﾘｰ品名
            case GXHDO102B012Const.TENKAZAISLURRYHINMEI:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryhinmei());

            // 添加材ｽﾗﾘｰLotNo
            case GXHDO102B012Const.TENKAZAISLURRYLOTNO:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B012Const.LOTKUBUN:
                return StringUtil.nullToBlank(srTenkaFp.getLotkubun());

            // 最終パス回数
            case GXHDO102B012Const.SAISYUUPASSKAISUU:
                return StringUtil.nullToBlank(srTenkaFp.getSaisyuupasskaisuu());

            // 保存用ｻﾝﾌﾟﾙ回収
            case GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFp.getHozonyousamplekaisyuu()));

            // 風袋重量①
            case GXHDO102B012Const.FUUTAIJYUURYOU1:
                return StringUtil.nullToBlank(srTenkaFp.getFuutaijyuuryou1());

            // 風袋重量②
            case GXHDO102B012Const.FUUTAIJYUURYOU2:
                return StringUtil.nullToBlank(srTenkaFp.getFuutaijyuuryou2());

            // 風袋重量③
            case GXHDO102B012Const.FUUTAIJYUURYOU3:
                return StringUtil.nullToBlank(srTenkaFp.getFuutaijyuuryou3());

            // 風袋重量④
            case GXHDO102B012Const.FUUTAIJYUURYOU4:
                return StringUtil.nullToBlank(srTenkaFp.getFuutaijyuuryou4());

            // 風袋重量⑤
            case GXHDO102B012Const.FUUTAIJYUURYOU5:
                return StringUtil.nullToBlank(srTenkaFp.getFuutaijyuuryou5());

            // 風袋重量⑥
            case GXHDO102B012Const.FUUTAIJYUURYOU6:
                return StringUtil.nullToBlank(srTenkaFp.getFuutaijyuuryou6());

            // F/P準備_担当者
            case GXHDO102B012Const.FPJYUNBI_TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_tantousya());

            // F/P準備_ﾌｨﾙﾀｰ連結
            case GXHDO102B012Const.FPJYUNBI_FILTERRENKETU:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_filterrenketu());

            // F/P準備_ﾌｨﾙﾀｰ品名①
            case GXHDO102B012Const.FPJYUNBI_FILTERHINMEI1:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_filterhinmei1());

            // F/P準備_LotNo①
            case GXHDO102B012Const.FPJYUNBI_LOTNO1:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_lotno1());

            // F/P準備_取り付け本数①
            case GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_toritukehonsuu1());

            // F/P準備_ﾌｨﾙﾀｰ品名②
            case GXHDO102B012Const.FPJYUNBI_FILTERHINMEI2:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_filterhinmei2());

            // F/P準備_LotNo②
            case GXHDO102B012Const.FPJYUNBI_LOTNO2:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_lotno2());

            // F/P準備_取り付け本数②
            case GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2:
                return StringUtil.nullToBlank(srTenkaFp.getFpjyunbi_toritukehonsuu2());

            // 排出容器の内袋
            case GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFp.getHaisyutuyoukinoutibukuro()));

            // ﾌｨﾙﾀｰ使用回数
            case GXHDO102B012Const.FILTERKAISUU:
                return StringUtil.nullToBlank(srTenkaFp.getFilterkaisuu());

            // F/PﾀﾝｸNo
            case GXHDO102B012Const.FPTANKNO:
                return StringUtil.nullToBlank(srTenkaFp.getFptankno());

            // 洗浄確認
            case GXHDO102B012Const.SENJYOUKAKUNIN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaFp.getSenjyoukakunin()));

            // F/P開始日
            case GXHDO102B012Const.FPKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpkaisinichiji(), "yyMMdd");

            // F/P開始時間
            case GXHDO102B012Const.FPKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpkaisinichiji(), "HHmm");

            // 圧送ﾚｷﾞｭﾚｰﾀｰNo
            case GXHDO102B012Const.ASSOUREGULATORNO:
                return StringUtil.nullToBlank(srTenkaFp.getAssouregulatorno());

            // 圧送圧力
            case GXHDO102B012Const.ASSOUATURYOKU:
                return StringUtil.nullToBlank(srTenkaFp.getAssouaturyoku());

            // F/P開始_担当者
            case GXHDO102B012Const.FPKAISI_TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFp.getFpkaisi_tantousya());

            // F/P停止日
            case GXHDO102B012Const.FPTEISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpteisinichiji(), "yyMMdd");

            // F/P停止時間
            case GXHDO102B012Const.FPTEISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpteisinichiji(), "HHmm");

            // F/P交換_ﾌｨﾙﾀｰ連結
            case GXHDO102B012Const.FPKOUKAN_FILTERRENKETU:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_filterrenketu());

            // F/P交換_ﾌｨﾙﾀｰ品名①
            case GXHDO102B012Const.FPKOUKAN_FILTERHINMEI1:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_filterhinmei1());

            // F/P交換_LotNo①
            case GXHDO102B012Const.FPKOUKAN_LOTNO1:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_lotno1());

            // F/P交換_取り付け本数①
            case GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_toritukehonsuu1());

            // F/P交換_ﾌｨﾙﾀｰ品名②
            case GXHDO102B012Const.FPKOUKAN_FILTERHINMEI2:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_filterhinmei2());

            // F/P交換_LotNo②
            case GXHDO102B012Const.FPKOUKAN_LOTNO2:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_lotno2());

            // F/P交換_取り付け本数②
            case GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_toritukehonsuu2());

            // F/P再開日
            case GXHDO102B012Const.FPSAIKAI_DAY:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpsaikainichiji(), "yyMMdd");

            // F/P再開時間
            case GXHDO102B012Const.FPSAIKAI_TIME:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpsaikainichiji(), "HHmm");

            // F/P交換_担当者
            case GXHDO102B012Const.FPKOUKAN_TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFp.getFpkoukan_tantousya());

            // F/P終了日
            case GXHDO102B012Const.FPSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpsyuuryounichiji(), "yyMMdd");

            // F/P終了時間
            case GXHDO102B012Const.FPSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaFp.getFpsyuuryounichiji(), "HHmm");

            // F/P時間
            case GXHDO102B012Const.FPJIKAN:
                return StringUtil.nullToBlank(srTenkaFp.getFpjikan());

            // F/P終了_担当者
            case GXHDO102B012Const.FPSYUUROU_TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFp.getFpsyuurou_tantousya());

            // 総重量①
            case GXHDO102B012Const.SOUJYUUROU1:
                return StringUtil.nullToBlank(srTenkaFp.getSoujyuurou1());

            // 総重量②
            case GXHDO102B012Const.SOUJYUUROU2:
                return StringUtil.nullToBlank(srTenkaFp.getSoujyuurou2());

            // 総重量③
            case GXHDO102B012Const.SOUJYUUROU3:
                return StringUtil.nullToBlank(srTenkaFp.getSoujyuurou3());

            // 総重量④
            case GXHDO102B012Const.SOUJYUUROU4:
                return StringUtil.nullToBlank(srTenkaFp.getSoujyuurou4());

            // 総重量⑤
            case GXHDO102B012Const.SOUJYUUROU5:
                return StringUtil.nullToBlank(srTenkaFp.getSoujyuurou5());

            // 総重量⑥
            case GXHDO102B012Const.SOUJYUUROU6:
                return StringUtil.nullToBlank(srTenkaFp.getSoujyuurou6());

            // 添加材ｽﾗﾘｰ重量①
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryou1());

            // 添加材ｽﾗﾘｰ重量②
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryou2());

            // 添加材ｽﾗﾘｰ重量③
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryou3());

            // 添加材ｽﾗﾘｰ重量④
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryou4());

            // 添加材ｽﾗﾘｰ重量⑤
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryou5());

            // 添加材ｽﾗﾘｰ重量⑥
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryou6());

            // 添加材ｽﾗﾘｰ重量合計
            case GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryjyuuryougoukei());

            // 歩留まり
            case GXHDO102B012Const.BUDOMARI:
                return StringUtil.nullToBlank(srTenkaFp.getBudomari());

            // 添加材ｽﾗﾘｰ有効期限
            case GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN:
                return StringUtil.nullToBlank(srTenkaFp.getTenkazaislurryyuukoukigen());

            // 粉砕判定
            case GXHDO102B012Const.FUNSAIHANTEI:
                return getComboBoxCheckValue(StringUtil.nullToBlank(srTenkaFp.getFunsaihantei()));

            // 担当者
            case GXHDO102B012Const.TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaFp.getTantousya());

            default:
                return null;
        }
    }

    /**
     * 添加材ｽﾗﾘｰ作製・FP排出_仮登録(tmp_sr_tenka_fp)登録処理(削除時)
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
    private void insertDeleteDataTmpSrTenkaFp(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_fp ("
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,saisyuupasskaisuu,hozonyousamplekaisyuu,"
                + "fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,fuutaijyuuryou4,fuutaijyuuryou5,fuutaijyuuryou6,FPjyunbi_tantousya,"
                + "FPjyunbi_filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,haisyutuyoukinoutibukuro,filterkaisuu,FPtankno,senjyoukakunin,FPkaisinichiji,"
                + "assouregulatorNo,assouaturyoku,FPkaisi_tantousya,FPteisinichiji,FPkoukan_filterrenketu,FPkoukan_filterhinmei1,"
                + "FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,"
                + "FPsaikainichiji,FPkoukan_tantousya,FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuurou1,soujyuurou2,"
                + "soujyuurou3,soujyuurou4,soujyuurou5,soujyuurou6,tenkazaislurryjyuuryou1,tenkazaislurryjyuuryou2,tenkazaislurryjyuuryou3,"
                + "tenkazaislurryjyuuryou4,tenkazaislurryjyuuryou5,tenkazaislurryjyuuryou6,tenkazaislurryjyuuryougoukei,budomari,"
                + "tenkazaislurryyuukoukigen,funsaihantei,tantousya,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,saisyuupasskaisuu,hozonyousamplekaisyuu,"
                + "fuutaijyuuryou1,fuutaijyuuryou2,fuutaijyuuryou3,fuutaijyuuryou4,fuutaijyuuryou5,fuutaijyuuryou6,FPjyunbi_tantousya,"
                + "FPjyunbi_filterrenketu,FPjyunbi_filterhinmei1,FPjyunbi_lotno1,FPjyunbi_toritukehonsuu1,FPjyunbi_filterhinmei2,"
                + "FPjyunbi_lotno2,FPjyunbi_toritukehonsuu2,haisyutuyoukinoutibukuro,filterkaisuu,FPtankno,senjyoukakunin,FPkaisinichiji,"
                + "assouregulatorNo,assouaturyoku,FPkaisi_tantousya,FPteisinichiji,FPkoukan_filterrenketu,FPkoukan_filterhinmei1,"
                + "FPkoukan_lotno1,FPkoukan_toritukehonsuu1,FPkoukan_filterhinmei2,FPkoukan_lotno2,FPkoukan_toritukehonsuu2,"
                + "FPsaikainichiji,FPkoukan_tantousya,FPsyuuryounichiji,FPjikan,FPsyuurou_tantousya,soujyuurou1,soujyuurou2,"
                + "soujyuurou3,soujyuurou4,soujyuurou5,soujyuurou6,tenkazaislurryjyuuryou1,tenkazaislurryjyuuryou2,tenkazaislurryjyuuryou3,"
                + "tenkazaislurryjyuuryou4,tenkazaislurryjyuuryou5,tenkazaislurryjyuuryou6,tenkazaislurryjyuuryougoukei,budomari,"
                + "tenkazaislurryyuukoukigen,funsaihantei,tantousya,?,?,?,? "
                + " FROM sr_tenka_fp "
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
    private void initGXHDO102B012A(ProcessData processData) {
        GXHDO102B012A bean = (GXHDO102B012A) getFormBean("gXHDO102B012A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B012Const.WIPLOTNO));
        bean.setTenkazaislurryhinmei(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYHINMEI));
        bean.setTenkazaislurrylotno(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B012Const.LOTKUBUN));
        bean.setTounyuuryou(getItemRow(processData.getItemList(), GXHDO102B012Const.TOUNYUURYOU));
        bean.setSaisyuupasskaisuu(getItemRow(processData.getItemList(), GXHDO102B012Const.SAISYUUPASSKAISUU));
        bean.setHozonyousamplekaisyuu(getItemRow(processData.getItemList(), GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU));
        bean.setFuutaijyuuryou1(getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU1));
        bean.setFuutaijyuuryou2(getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU2));
        bean.setFuutaijyuuryou3(getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU3));
        bean.setFuutaijyuuryou4(getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU4));
        bean.setFuutaijyuuryou5(getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU5));
        bean.setFuutaijyuuryou6(getItemRow(processData.getItemList(), GXHDO102B012Const.FUUTAIJYUURYOU6));
        bean.setFpjyunbi_tantousya(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_TANTOUSYA));
        bean.setFpjyunbi_filterrenketu(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_FILTERRENKETU));
        bean.setFpjyunbi_filterhinmei1(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_FILTERHINMEI1));
        bean.setFpjyunbi_lotno1(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_LOTNO1));
        bean.setFpjyunbi_toritukehonsuu1(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1));
        bean.setFpjyunbi_filterhinmei2(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_FILTERHINMEI2));
        bean.setFpjyunbi_lotno2(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_LOTNO2));
        bean.setFpjyunbi_toritukehonsuu2(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2));
        bean.setHaisyutuyoukinoutibukuro(getItemRow(processData.getItemList(), GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO));
        bean.setFilterkaisuu(getItemRow(processData.getItemList(), GXHDO102B012Const.FILTERKAISUU));
        bean.setFptankno(getItemRow(processData.getItemList(), GXHDO102B012Const.FPTANKNO));
        bean.setSenjyoukakunin(getItemRow(processData.getItemList(), GXHDO102B012Const.SENJYOUKAKUNIN));
        bean.setFpkaisi_day(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_DAY));
        bean.setFpkaisi_time(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_TIME));
        bean.setAssouregulatorno(getItemRow(processData.getItemList(), GXHDO102B012Const.ASSOUREGULATORNO));
        bean.setAssouaturyoku(getItemRow(processData.getItemList(), GXHDO102B012Const.ASSOUATURYOKU));
        bean.setFpkaisi_tantousya(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKAISI_TANTOUSYA));
        bean.setFpteisi_day(getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_DAY));
        bean.setFpteisi_time(getItemRow(processData.getItemList(), GXHDO102B012Const.FPTEISI_TIME));
        bean.setFpkoukan_filterrenketu(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_FILTERRENKETU));
        bean.setFpkoukan_filterhinmei1(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_FILTERHINMEI1));
        bean.setFpkoukan_lotno1(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_LOTNO1));
        bean.setFpkoukan_toritukehonsuu1(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1));
        bean.setFpkoukan_filterhinmei2(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_FILTERHINMEI2));
        bean.setFpkoukan_lotno2(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_LOTNO2));
        bean.setFpkoukan_toritukehonsuu2(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2));
        bean.setFpsaikai_day(getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_DAY));
        bean.setFpsaikai_time(getItemRow(processData.getItemList(), GXHDO102B012Const.FPSAIKAI_TIME));
        bean.setFpkoukan_tantousya(getItemRow(processData.getItemList(), GXHDO102B012Const.FPKOUKAN_TANTOUSYA));
        bean.setFpsyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_DAY));
        bean.setFpsyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUURYOU_TIME));
        bean.setFpjikan(getItemRow(processData.getItemList(), GXHDO102B012Const.FPJIKAN));
        bean.setFpsyuurou_tantousya(getItemRow(processData.getItemList(), GXHDO102B012Const.FPSYUUROU_TANTOUSYA));
        bean.setSoujyuurou1(getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU1));
        bean.setSoujyuurou2(getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU2));
        bean.setSoujyuurou3(getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU3));
        bean.setSoujyuurou4(getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU4));
        bean.setSoujyuurou5(getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU5));
        bean.setSoujyuurou6(getItemRow(processData.getItemList(), GXHDO102B012Const.SOUJYUUROU6));
        bean.setTenkazaislurryjyuuryou1(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1));
        bean.setTenkazaislurryjyuuryou2(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2));
        bean.setTenkazaislurryjyuuryou3(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3));
        bean.setTenkazaislurryjyuuryou4(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4));
        bean.setTenkazaislurryjyuuryou5(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5));
        bean.setTenkazaislurryjyuuryou6(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6));
        bean.setTenkazaislurryjyuuryougoukei(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI));
        bean.setBudomari(getItemRow(processData.getItemList(), GXHDO102B012Const.BUDOMARI));
        bean.setTenkazaislurryyuukoukigen(getItemRow(processData.getItemList(), GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN));
        bean.setFunsaihantei(getItemRow(processData.getItemList(), GXHDO102B012Const.FUNSAIHANTEI));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B012Const.TANTOUSYA));

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
        allItemIdMap.put(GXHDO102B012Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYHINMEI, "添加材ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYLOTNO, "添加材ｽﾗﾘｰLotNo");
        allItemIdMap.put(GXHDO102B012Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B012Const.TOUNYUURYOU, "投入量");
        allItemIdMap.put(GXHDO102B012Const.SAISYUUPASSKAISUU, "最終パス回数");
        allItemIdMap.put(GXHDO102B012Const.HOZONYOUSAMPLEKAISYUU, "保存用ｻﾝﾌﾟﾙ回収");
        allItemIdMap.put(GXHDO102B012Const.FUUTAIJYUURYOU1, "風袋重量①");
        allItemIdMap.put(GXHDO102B012Const.FUUTAIJYUURYOU2, "風袋重量②");
        allItemIdMap.put(GXHDO102B012Const.FUUTAIJYUURYOU3, "風袋重量③");
        allItemIdMap.put(GXHDO102B012Const.FUUTAIJYUURYOU4, "風袋重量④");
        allItemIdMap.put(GXHDO102B012Const.FUUTAIJYUURYOU5, "風袋重量⑤");
        allItemIdMap.put(GXHDO102B012Const.FUUTAIJYUURYOU6, "風袋重量⑥");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_TANTOUSYA, "F/P準備_担当者");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_FILTERRENKETU, "F/P準備_ﾌｨﾙﾀｰ連結");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_FILTERHINMEI1, "F/P準備_ﾌｨﾙﾀｰ品名①");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_LOTNO1, "F/P準備_LotNo①");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU1, "F/P準備_取り付け本数①");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_FILTERHINMEI2, "F/P準備_ﾌｨﾙﾀｰ品名②");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_LOTNO2, "F/P準備_LotNo②");
        allItemIdMap.put(GXHDO102B012Const.FPJYUNBI_TORITUKEHONSUU2, "F/P準備_取り付け本数②");
        allItemIdMap.put(GXHDO102B012Const.HAISYUTUYOUKINOUTIBUKURO, "排出容器の内袋");
        allItemIdMap.put(GXHDO102B012Const.FILTERKAISUU, "ﾌｨﾙﾀｰ使用回数");
        allItemIdMap.put(GXHDO102B012Const.FPTANKNO, "F/PﾀﾝｸNo");
        allItemIdMap.put(GXHDO102B012Const.SENJYOUKAKUNIN, "洗浄確認");
        allItemIdMap.put(GXHDO102B012Const.FPKAISI_DAY, "F/P開始日");
        allItemIdMap.put(GXHDO102B012Const.FPKAISI_TIME, "F/P開始時間");
        allItemIdMap.put(GXHDO102B012Const.ASSOUREGULATORNO, "圧送ﾚｷﾞｭﾚｰﾀｰNo");
        allItemIdMap.put(GXHDO102B012Const.ASSOUATURYOKU, "圧送圧力");
        allItemIdMap.put(GXHDO102B012Const.FPKAISI_TANTOUSYA, "F/P開始_担当者");
        allItemIdMap.put(GXHDO102B012Const.FPTEISI_DAY, "F/P停止日");
        allItemIdMap.put(GXHDO102B012Const.FPTEISI_TIME, "F/P停止時間");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_FILTERRENKETU, "F/P交換_ﾌｨﾙﾀｰ連結");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_FILTERHINMEI1, "F/P交換_ﾌｨﾙﾀｰ品名①");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_LOTNO1, "F/P交換_LotNo①");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU1, "F/P交換_取り付け本数①");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_FILTERHINMEI2, "F/P交換_ﾌｨﾙﾀｰ品名②");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_LOTNO2, "F/P交換_LotNo②");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_TORITUKEHONSUU2, "F/P交換_取り付け本数②");
        allItemIdMap.put(GXHDO102B012Const.FPSAIKAI_DAY, "F/P再開日");
        allItemIdMap.put(GXHDO102B012Const.FPSAIKAI_TIME, "F/P再開時間");
        allItemIdMap.put(GXHDO102B012Const.FPKOUKAN_TANTOUSYA, "F/P交換_担当者");
        allItemIdMap.put(GXHDO102B012Const.FPSYUURYOU_DAY, "F/P終了日");
        allItemIdMap.put(GXHDO102B012Const.FPSYUURYOU_TIME, "F/P終了時間");
        allItemIdMap.put(GXHDO102B012Const.FPJIKAN, "F/P時間");
        allItemIdMap.put(GXHDO102B012Const.FPSYUUROU_TANTOUSYA, "F/P終了_担当者");
        allItemIdMap.put(GXHDO102B012Const.SOUJYUUROU1, "総重量①");
        allItemIdMap.put(GXHDO102B012Const.SOUJYUUROU2, "総重量②");
        allItemIdMap.put(GXHDO102B012Const.SOUJYUUROU3, "総重量③");
        allItemIdMap.put(GXHDO102B012Const.SOUJYUUROU4, "総重量④");
        allItemIdMap.put(GXHDO102B012Const.SOUJYUUROU5, "総重量⑤");
        allItemIdMap.put(GXHDO102B012Const.SOUJYUUROU6, "総重量⑥");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU1, "添加材ｽﾗﾘｰ重量①");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU2, "添加材ｽﾗﾘｰ重量②");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU3, "添加材ｽﾗﾘｰ重量③");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU4, "添加材ｽﾗﾘｰ重量④");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU5, "添加材ｽﾗﾘｰ重量⑤");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOU6, "添加材ｽﾗﾘｰ重量⑥");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI, "添加材ｽﾗﾘｰ重量合計");
        allItemIdMap.put(GXHDO102B012Const.BUDOMARI, "歩留まり");
        allItemIdMap.put(GXHDO102B012Const.TENKAZAISLURRYYUUKOUKIGEN, "添加材ｽﾗﾘｰ有効期限");
        allItemIdMap.put(GXHDO102B012Const.FUNSAIHANTEI, "粉砕判定");
        allItemIdMap.put(GXHDO102B012Const.TANTOUSYA, "担当者");

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

    /**
     * 添加材ｽﾗﾘｰ重量合計と歩留り計算項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     */
    private void removeItemFromItemList(ProcessData processData, List<String> notShowItemList) {
        List<FXHDD01> itemList = processData.getItemList();
        GXHDO102B012A bean = (GXHDO102B012A) getFormBean("gXHDO102B012A");
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        bean.setTenkazaislurryjyuuryougoukei(null);
        bean.setBudomari(null);
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @throws SQLException 例外エラー
     */
    private void setItemRendered(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "添加材ｽﾗﾘｰ作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "添加剤ｽﾗﾘｰ_FP排出_重量_表示制御");
        // 画面非表示項目リスト: 添加材ｽﾗﾘｰ重量合計、歩留まり計算
        List<String> notShowItemList = Arrays.asList(GXHDO102B012Const.TENKAZAISLURRYJYUURYOUGOUKEI, GXHDO102B012Const.BUDOMARI);
        if (fxhbm03Data == null) {
            // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
            removeItemFromItemList(processData, notShowItemList);
            return;
        }
        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
            removeItemFromItemList(processData, notShowItemList);
            return;
        }

        // 設計No
        String sekkeiNo = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "sekkeiNo"));
        // ﾊﾟﾀｰﾝ
        String pattern = StringUtil.nullToBlank(getMapData(daMkSekKeiData, "pattern"));
        // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
        String data = StringUtil.nullToBlank(getMapData(fxhbm03Data, "data"));
        String[] dataSplitList = data.split(",");
        // [前工程規格情報]から、ﾃﾞｰﾀを取得
        Map daMkJokenData = loadDaMkJokenData(queryRunnerQcdb, sekkeiNo, dataSplitList);
        if (daMkJokenData == null || daMkJokenData.isEmpty()) {
            // [前工程標準規格情報]から、ﾃﾞｰﾀを取得
            Map daMkhYoJunJokenData = loadDaMkhYoJunJokenData(queryRunnerQcdb, (String) shikakariData.get("hinmei"), pattern, syurui);
            if (daMkhYoJunJokenData == null || daMkhYoJunJokenData.isEmpty()) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList);
                return;
            }
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            if (!"1".equals(kikakuti)) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList);
            }
        } else {
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
            if (!"1".equals(kikakuti)) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList);
            }
        }
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param key キー
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhbm03Data(QueryRunner queryRunnerDoc, String key) throws SQLException {
        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT data "
                + " FROM fxhbm03 "
                + " WHERE user_name = 'common_user' AND key = ? ";
        List<Object> params = new ArrayList<>();
        params.add(key);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程設計]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @param syurui 種類
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkSekKeiData(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String syurui) throws SQLException {
        // 前工程設計データの取得
        String sql = "SELECT sekkeino, pattern FROM da_mksekkei"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND syurui = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(syurui);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param sekkeiNo 設計No
     * @param data ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo, String[] data) throws SQLException {
        if (data == null || data.length < 3) {
            return null;
        }
        // 前工程規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkjoken"
                + " WHERE sekkeino = ? AND kouteimei = ? AND koumokumei = ? AND kanrikoumokumei = ? ";
        List<Object> params = new ArrayList<>();
        params.add(sekkeiNo);
        params.add(data[0]);
        params.add(data[1]);
        params.add(data[2]);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [前工程標準規格情報]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb オブジェクト
     * @param hinmei 品名
     * @param pattern ﾊﾟﾀｰﾝ
     * @param syurui 種類
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaMkhYoJunJokenData(QueryRunner queryRunnerQcdb, String hinmei, String pattern, String syurui) throws SQLException {
        // 前工程標準規格情報データの取得
        String sql = "SELECT kikakuti FROM da_mkhyojunjoken"
                + " WHERE hinmei = ? AND pattern = ? AND syurui = ? ";
        List<Object> params = new ArrayList<>();
        params.add(hinmei);
        params.add(pattern);
        params.add(syurui);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
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
        return null;
    }

    /**
     * コンボボックス値(コンボボックス内のValue値)取得
     *
     * @param dbValue コンボボックス(DB内)Value値
     * @return コンボボックステキスト値
     */
    private String getComboBoxCheckValue(String dbValue) {
        if ("0".equals(dbValue)) {
            return "不合格";
        } else if ("1".equals(dbValue)) {
            return "合格";
        }
        return null;
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
     * コンボボックス値(DB内のValue値)取得
     *
     * @param comboBoxValue コンボボックスValue値
     * @param defaultValue 選択されていないときのデフォルト
     * @return コンボボックステキスト値
     */
    private Integer getComboBoxDbValue(String checkBoxValue, Integer defaultValue) {
        if ("不合格".equals(StringUtil.nullToBlank(checkBoxValue))) {
            return 0;
        } else if ("合格".equals(StringUtil.nullToBlank(checkBoxValue))) {
            return 1;
        }
        return defaultValue;
    }
}
