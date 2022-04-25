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
import jp.co.kccs.xhd.db.model.SrSlipSlurrykokeibuntyouseiSiropori;
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
 * 変更日	2021/12/06<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B027(ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ))
 *
 * @author KCSS K.Jo
 * @since 2021/12/06
 */
public class GXHDO102B027 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B027.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B027() {
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
            initGXHDO102B027A(processData);

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO102B027Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_TOP,
                    GXHDO102B027Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B027Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B027Const.BTN_INSERT_TOP,
                    GXHDO102B027Const.BTN_DELETE_TOP,
                    GXHDO102B027Const.BTN_UPDATE_TOP,
                    GXHDO102B027Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B027Const.BTN_INSERT_BOTTOM,
                    GXHDO102B027Const.BTN_DELETE_BOTTOM,
                    GXHDO102B027Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B027Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B027Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B027Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B027Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B027Const.BTN_INSERT_TOP:
            case GXHDO102B027Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B027Const.BTN_UPDATE_TOP:
            case GXHDO102B027Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B027Const.BTN_DELETE_TOP:
            case GXHDO102B027Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // ﾊﾞｲﾝﾀﾞｰ混合日時
            case GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_TOP:
            case GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_BOTTOM:
                method = "setBinderkongouDateTime";
                break;
            // ｽﾗﾘｰ合計重量計算
            case GXHDO102B027Const.BTN_SLURRYGOUKEIJYUURYOU_TOP:
            case GXHDO102B027Const.BTN_SLURRYGOUKEIJYUURYOU_BOTTOM:
                method = "setSlurrygoukeijyuuryou";
                break;
            // 固形分調整量計算
            case GXHDO102B027Const.BTN_KOKEIBUNTYOUSEIRYOU_TOP:
            case GXHDO102B027Const.BTN_KOKEIBUNTYOUSEIRYOU_BOTTOM:
                method = "setKokeibuntyouseiryou";
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

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipSlurrykokeibuntyouseiSiropori> srSSSiroporiDataList = GXHDO102B027.this.getSrSlipSlurrykokeibuntyouseiSiroporiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srSSSiroporiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSSSiroporiDataList.get(0));

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

                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録処理
                insertTmpSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録更新処理
                updateTmpSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
        // ﾊﾞｲﾝﾀﾞｰ混合日時、粉砕終了日時前後チェック
        FXHDD01 itemBinderkongouDay = getItemRow(processData.getItemList(), GXHDO102B027Const.BINDERKONGOU_DAY); //　ﾊﾞｲﾝﾀﾞｰ混合日
        FXHDD01 itemBinderkongouTime = getItemRow(processData.getItemList(), GXHDO102B027Const.BINDERKONGOU_TIME); // ﾊﾞｲﾝﾀﾞｰ混合時間
        FXHDD01 itemFunsaisyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B027Const.FUNSAISYUURYOU_DAY); // 粉砕終了日
        FXHDD01 itemFunsaisyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B027Const.FUNSAISYUURYOU_TIME); // 粉砕終了時間
        if (itemBinderkongouDay != null && itemBinderkongouTime != null && itemFunsaisyuuryouDay != null && itemFunsaisyuuryouTime != null) {
            Date binderkongouDate = DateUtil.convertStringToDate(itemBinderkongouDay.getValue(), itemBinderkongouTime.getValue());
            Date funsaisyuuryouDate = DateUtil.convertStringToDate(itemFunsaisyuuryouDay.getValue(), itemFunsaisyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001("粉砕終了日時", funsaisyuuryouDate, "ﾊﾞｲﾝﾀﾞｰ混合日時", binderkongouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemFunsaisyuuryouDay, itemFunsaisyuuryouTime, itemBinderkongouDay, itemBinderkongouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
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
            SrSlipSlurrykokeibuntyouseiSiropori tmpSrSlipSlurrykokeibuntyouseiSiropori = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipSlurrykokeibuntyouseiSiropori> srSlipSlurrykokeibuntyouseiSiroporiList = GXHDO102B027.this.getSrSlipSlurrykokeibuntyouseiSiroporiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srSlipSlurrykokeibuntyouseiSiroporiList.isEmpty()) {
                    tmpSrSlipSlurrykokeibuntyouseiSiropori = srSlipSlurrykokeibuntyouseiSiroporiList.get(0);
                }

                deleteTmpSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_登録処理
            insertSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrSlipSlurrykokeibuntyouseiSiropori);
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
        processData.setUserAuthParam(GXHDO102B027Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_更新処理
            updateSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        processData.setUserAuthParam(GXHDO102B027Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_削除処理
            deleteSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * ﾊﾞｲﾝﾀﾞｰ混合日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBinderkongouDateTime(ProcessData processData) {
        Date syuuryounichijiVal = null;
        Date binderkongounichijiVal = null;  
        
        FXHDD01 binderkongouDay = getItemRow(processData.getItemList(), GXHDO102B027Const.BINDERKONGOU_DAY);
        FXHDD01 binderkongouTime = getItemRow(processData.getItemList(), GXHDO102B027Const.BINDERKONGOU_TIME);
        FXHDD01 slurrykeikanisuu = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYKEIKANISUU);
        if (binderkongouDay == null || binderkongouTime == null || slurrykeikanisuu == null) {
            processData.setMethod("");
            return processData;
        }
        if (StringUtil.isEmpty(binderkongouDay.getValue()) && StringUtil.isEmpty(binderkongouTime.getValue())) {
            // ﾊﾞｲﾝﾀﾞｰ混合日時
            setDateTimeItem(binderkongouDay, binderkongouTime, new Date());
        }

        binderkongounichijiVal =  DateUtil.convertStringToDate(binderkongouDay.getValue(), binderkongouTime.getValue());
        
        FXHDD01 funsaisyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B027Const.FUNSAISYUURYOU_DAY);
        FXHDD01 funsaisyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B027Const.FUNSAISYUURYOU_TIME);
        if (funsaisyuuryouDay == null || funsaisyuuryouTime == null) {
            processData.setMethod("");
            return processData;
        }
        if (!StringUtil.isEmpty(funsaisyuuryouDay.getValue()) && !StringUtil.isEmpty(funsaisyuuryouTime.getValue())) {
            // 粉砕終了日時        
            syuuryounichijiVal = DateUtil.convertStringToDate(funsaisyuuryouDay.getValue(), funsaisyuuryouTime.getValue());           

            if (binderkongounichijiVal != null && syuuryounichijiVal != null) {
                // 日付の差分日数取得処理
                int diffDays = DateUtil.diffDaysRoundingMode(syuuryounichijiVal,binderkongounichijiVal, RoundingMode.CEILING);
                // ｽﾗﾘｰ経過日数の設定
                slurrykeikanisuu.setValue(BigDecimal.valueOf(diffDays).toPlainString());
            }
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 画面表示仕様(14)を発行する。
     *
     * @param queryRunnerQcdb オブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo LotNo
     * @param edaban 枝番
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    public static Map getSyuuryounichiji(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        // データの取得
        String sql = "SELECT syuuryounichiji FROM sr_yuudentai_funsai"
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * ｽﾗﾘｰ合計重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setSlurrygoukeijyuuryou(ProcessData processData) {

        FXHDD01 itemSlurryjyuuryou1 = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU1); // ｽﾗﾘｰ重量①
        FXHDD01 itemSlurryjyuuryou2 = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU2); // ｽﾗﾘｰ重量②
        FXHDD01 itemSlurryjyuuryou3 = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU3); // ｽﾗﾘｰ重量③
        FXHDD01 itemSlurryjyuuryou4 = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU4); // ｽﾗﾘｰ重量④
        FXHDD01 itemSlurryjyuuryou5 = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU5); // ｽﾗﾘｰ重量⑤
        FXHDD01 itemSlurryjyuuryou6 = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU6); // ｽﾗﾘｰ重量⑥

        ErrorMessageInfo checkItemErrorInfo = checkSlurryjyuuryou(itemSlurryjyuuryou1, itemSlurryjyuuryou2, itemSlurryjyuuryou3, itemSlurryjyuuryou4, itemSlurryjyuuryou5, itemSlurryjyuuryou6);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「ｽﾗﾘｰ合計重量」計算処理
        calcSlurrygoukeijyuuryou(processData, itemSlurryjyuuryou1, itemSlurryjyuuryou2, itemSlurryjyuuryou3, itemSlurryjyuuryou4, itemSlurryjyuuryou5, itemSlurryjyuuryou6);

        return processData;
    }

    /**
     * 【ｽﾗﾘｰ合計重量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemSlurryjyuuryou1 ｽﾗﾘｰ重量①
     * @param itemSlurryjyuuryou2 ｽﾗﾘｰ重量②
     * @param itemSlurryjyuuryou3 ｽﾗﾘｰ重量③
     * @param itemSlurryjyuuryou4 ｽﾗﾘｰ重量④
     * @param itemSlurryjyuuryou5 ｽﾗﾘｰ重量⑤
     * @param itemSlurryjyuuryou6 ｽﾗﾘｰ重量⑥
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkSlurryjyuuryou(FXHDD01 itemSlurryjyuuryou1, FXHDD01 itemSlurryjyuuryou2, FXHDD01 itemSlurryjyuuryou3, FXHDD01 itemSlurryjyuuryou4,
            FXHDD01 itemSlurryjyuuryou5, FXHDD01 itemSlurryjyuuryou6) {

        //「ｽﾗﾘｰ重量①」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSlurryjyuuryou1.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlurryjyuuryou1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlurryjyuuryou1.getLabel1());
        }
        //「ｽﾗﾘｰ重量②」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSlurryjyuuryou2.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlurryjyuuryou2);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlurryjyuuryou2.getLabel1());
        }
        //「ｽﾗﾘｰ重量③」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSlurryjyuuryou3.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlurryjyuuryou3);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlurryjyuuryou3.getLabel1());
        }
        //「ｽﾗﾘｰ重量④」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSlurryjyuuryou4.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlurryjyuuryou4);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlurryjyuuryou4.getLabel1());
        }
        //「ｽﾗﾘｰ重量⑤」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSlurryjyuuryou5.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlurryjyuuryou5);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlurryjyuuryou5.getLabel1());
        }
        //「ｽﾗﾘｰ重量⑥」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemSlurryjyuuryou6.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemSlurryjyuuryou6);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemSlurryjyuuryou6.getLabel1());
        }
        return null;
    }

    /**
     * 「ｽﾗﾘｰ合計重量」計算処理
     *
     * @param processData 処理制御データ
     * @param itemSlurryjyuuryou1 ｽﾗﾘｰ重量①
     * @param itemSlurryjyuuryou2 ｽﾗﾘｰ重量②
     * @param itemSlurryjyuuryou3 ｽﾗﾘｰ重量③
     * @param itemSlurryjyuuryou4 ｽﾗﾘｰ重量④
     * @param itemSlurryjyuuryou5 ｽﾗﾘｰ重量⑤
     * @param itemSlurryjyuuryou6 ｽﾗﾘｰ重量⑥
     */
    private void calcSlurrygoukeijyuuryou(ProcessData processData, FXHDD01 itemSlurryjyuuryou1, FXHDD01 itemSlurryjyuuryou2, FXHDD01 itemSlurryjyuuryou3, FXHDD01 itemSlurryjyuuryou4,
            FXHDD01 itemSlurryjyuuryou5, FXHDD01 itemSlurryjyuuryou6) {
        try {

            FXHDD01 slurrygoukeijyuuryou = getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYGOUKEIJYUURYOU); // ｽﾗﾘｰ合計重量
            BigDecimal itemSlurryjyuuryou1Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSlurryjyuuryou1.getValue()); // ｽﾗﾘｰ重量①
            BigDecimal itemSlurryjyuuryou2Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSlurryjyuuryou2.getValue()); // ｽﾗﾘｰ重量②
            BigDecimal itemSlurryjyuuryou3Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSlurryjyuuryou3.getValue()); // ｽﾗﾘｰ重量③
            BigDecimal itemSlurryjyuuryou4Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSlurryjyuuryou4.getValue()); // ｽﾗﾘｰ重量④
            BigDecimal itemSlurryjyuuryou5Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSlurryjyuuryou5.getValue()); // ｽﾗﾘｰ重量⑤
            BigDecimal itemSlurryjyuuryou6Val = (BigDecimal) DBUtil.stringToBigDecimalObject(itemSlurryjyuuryou6.getValue()); // ｽﾗﾘｰ重量⑥
            // 「ｽﾗﾘｰ重量①」 + 「ｽﾗﾘｰ重量②」 + 「ｽﾗﾘｰ重量③」 + 「ｽﾗﾘｰ重量④」 + 「ｽﾗﾘｰ重量⑤」 + 「ｽﾗﾘｰ重量⑥」 を算出する。
            BigDecimal slurrygoukeijyuuryouVal = itemSlurryjyuuryou1Val.add(itemSlurryjyuuryou2Val).add(itemSlurryjyuuryou3Val).add(itemSlurryjyuuryou4Val).add(itemSlurryjyuuryou5Val).add(itemSlurryjyuuryou6Val);
            slurrygoukeijyuuryou.setValue(slurrygoukeijyuuryouVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("ｽﾗﾘｰ合計重量計算にエラー発生", ex, LOGGER);
        }

    }

    /**
     * 固形分調整量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKokeibuntyouseiryou(ProcessData processData) {

        FXHDD01 itemKokeibuntyouseiryou1 = getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU1); // 固形分調整量➀
        FXHDD01 itemKokeibuntyouseiryou2 = getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU2); // 固形分調整量②
        BigDecimal itemKokeibuntyouseiryou1Val = ValidateUtil.getItemKikakuChiCheckVal(itemKokeibuntyouseiryou1);// 固形分調整量➀の規格値
        BigDecimal itemKokeibuntyouseiryou2Val = ValidateUtil.getItemKikakuChiCheckVal(itemKokeibuntyouseiryou2);// 固形分調整量②の規格値

        ErrorMessageInfo checkItemErrorInfo = checkKokeibuntyouseiryou(itemKokeibuntyouseiryou1, itemKokeibuntyouseiryou2, itemKokeibuntyouseiryou1Val, itemKokeibuntyouseiryou2Val);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「固形分調整量」計算処理
        calcKokeibuntyouseiryou(processData, itemKokeibuntyouseiryou1Val, itemKokeibuntyouseiryou2Val);
        return processData;
    }

    /**
     * 【固形分調整量計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemKokeibuntyouseiryou1 固形分調整量➀
     * @param itemKokeibuntyouseiryou2 固形分調整量②
     * @param itemKokeibuntyouseiryou1Val 固形分調整量➀の規格値
     * @param itemKokeibuntyouseiryou2Val 固形分調整量②の規格値
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkKokeibuntyouseiryou(FXHDD01 itemKokeibuntyouseiryou1, FXHDD01 itemKokeibuntyouseiryou2, BigDecimal itemKokeibuntyouseiryou1Val, BigDecimal itemKokeibuntyouseiryou2Val) {

        //「固形分調整量➀」ﾁｪｯｸ
        if (itemKokeibuntyouseiryou1Val == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibuntyouseiryou1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemKokeibuntyouseiryou1.getLabel1());
        }
        //「固形分調整量②」ﾁｪｯｸ
        if (itemKokeibuntyouseiryou2Val == null) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKokeibuntyouseiryou2);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemKokeibuntyouseiryou2.getLabel1());
        }
        return null;
    }

    /**
     * 「固形分調整量」計算処理
     *
     * @param processData 処理制御データ
     * @param itemKokeibuntyouseiryou1Val 固形分調整量➀の規格値
     * @param itemKokeibuntyouseiryou2Val 固形分調整量②の規格値
     */
    private void calcKokeibuntyouseiryou(ProcessData processData, BigDecimal itemKokeibuntyouseiryou1Val, BigDecimal itemKokeibuntyouseiryou2Val) {
        try {

            FXHDD01 kokeibuntyouseiryou = getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU); // 固形分調整量
            FXHDD01 toluenetenkaryou = getItemRow(processData.getItemList(), GXHDO102B027Const.TOLUENETENKARYOU); // ﾄﾙｴﾝ添加量
            FXHDD01 solmixtenkaryou = getItemRow(processData.getItemList(), GXHDO102B027Const.SOLMIXTENKARYOU); // ｿﾙﾐｯｸｽ添加量
            // 「固形分調整量➀」 + 「固形分調整量②」 を算出する。
            BigDecimal kokeibuntyouseiryouVal = itemKokeibuntyouseiryou1Val.add(itemKokeibuntyouseiryou2Val);
            kokeibuntyouseiryou.setValue(kokeibuntyouseiryouVal.toPlainString());
            if (!StringUtil.isEmpty(kokeibuntyouseiryou.getValue())) {
                BigDecimal solmixtenkaryouVal = (kokeibuntyouseiryouVal.multiply(BigDecimal.valueOf(0.5))).setScale(0, RoundingMode.HALF_UP);
                toluenetenkaryou.setValue(solmixtenkaryouVal.toPlainString());
                solmixtenkaryou.setValue(solmixtenkaryouVal.toPlainString());
            }

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン 
            ErrUtil.outputErrorLog("固形分調整量計算にエラー発生", ex, LOGGER);
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
                activeIdList.addAll(Arrays.asList(GXHDO102B027Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_TOP,
                        GXHDO102B027Const.BTN_SLURRYGOUKEIJYUURYOU_TOP,
                        GXHDO102B027Const.BTN_KOKEIBUNTYOUSEIRYOU_TOP,
                        GXHDO102B027Const.BTN_UPDATE_TOP,
                        GXHDO102B027Const.BTN_DELETE_TOP,
                        GXHDO102B027Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_BOTTOM,
                        GXHDO102B027Const.BTN_SLURRYGOUKEIJYUURYOU_BOTTOM,
                        GXHDO102B027Const.BTN_KOKEIBUNTYOUSEIRYOU_BOTTOM,
                        GXHDO102B027Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B027Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B027Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B027Const.BTN_INSERT_TOP,
                        GXHDO102B027Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B027Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO102B027Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_TOP,
                        GXHDO102B027Const.BTN_SLURRYGOUKEIJYUURYOU_TOP,
                        GXHDO102B027Const.BTN_KOKEIBUNTYOUSEIRYOU_TOP,
                        GXHDO102B027Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B027Const.BTN_INSERT_TOP,
                        GXHDO102B027Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B027Const.BTN_BINDERKONGOUNICHIJI_BOTTOM,
                        GXHDO102B027Const.BTN_SLURRYGOUKEIJYUURYOU_BOTTOM,
                        GXHDO102B027Const.BTN_KOKEIBUNTYOUSEIRYOU_BOTTOM,
                        GXHDO102B027Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B027Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B027Const.BTN_UPDATE_TOP,
                        GXHDO102B027Const.BTN_DELETE_TOP,
                        GXHDO102B027Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B027Const.BTN_DELETE_BOTTOM
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
        
        String syuuryouDate = "";
        String syuuryouTime = "";
        String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
        String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo
        String edaban = lotNo.substring(12, 15); //枝番
        Map shuryonichijiData = getSyuuryounichiji(queryRunnerQcdb, kojyo, lotNo9, edaban);
        
        if (shuryonichijiData != null && !shuryonichijiData.isEmpty()) {
            if (!StringUtil.isEmpty(StringUtil.nullToBlank(getMapData(shuryonichijiData, "syuuryounichiji")))) {
                // 終了日時
                syuuryouDate = DateUtil.formattedTimestamp((Timestamp) getMapData(shuryonichijiData, "syuuryounichiji"), "yyMMdd");
                syuuryouTime = DateUtil.formattedTimestamp((Timestamp) getMapData(shuryonichijiData, "syuuryounichiji"), "HHmm");
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino, syuuryouDate, syuuryouTime)) {
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
        this.setItemData(processData, GXHDO102B027Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B027Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B027Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B027Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B027Const.LOTKUBUN, lotkubuncode);
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
            String lotNo, String formId, int jissekino, String syuuryouDate, String syuuryouTime) throws SQLException {

        List<SrSlipSlurrykokeibuntyouseiSiropori> srSlipSlurrykokeibuntyouseiSiroporiList = new ArrayList<>();
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
                
                // 粉砕終了日
                this.setItemData(processData, GXHDO102B027Const.FUNSAISYUURYOU_DAY, syuuryouDate);
                // 粉砕終了時間
                this.setItemData(processData, GXHDO102B027Const.FUNSAISYUURYOU_TIME, syuuryouTime);

                return true;
            }

            // ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ取得
            srSlipSlurrykokeibuntyouseiSiroporiList = GXHDO102B027.this.getSrSlipSlurrykokeibuntyouseiSiroporiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srSlipSlurrykokeibuntyouseiSiroporiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipSlurrykokeibuntyouseiSiroporiList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipSlurrykokeibuntyouseiSiroporiList.get(0));
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipSlurrykokeibuntyouseiSiropori ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori) {
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B027Const.SLIPHINMEI, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLIPHINMEI, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B027Const.SLIPLOTNO, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLIPLOTNO, srSlipSlurrykokeibuntyouseiSiropori));

        // ﾛｯﾄ区分
        this.setItemData(processData, GXHDO102B027Const.LOTKUBUN, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.LOTKUBUN, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ有効期限
        this.setItemData(processData, GXHDO102B027Const.SLURRYYUUKOUKIGEN, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYYUUKOUKIGEN, srSlipSlurrykokeibuntyouseiSiropori));

        // 粉砕終了日
        this.setItemData(processData, GXHDO102B027Const.FUNSAISYUURYOU_DAY, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.FUNSAISYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSiropori));

        // 粉砕終了時間
        this.setItemData(processData, GXHDO102B027Const.FUNSAISYUURYOU_TIME, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.FUNSAISYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSiropori));

        // ﾊﾞｲﾝﾀﾞｰ混合日
        this.setItemData(processData, GXHDO102B027Const.BINDERKONGOU_DAY, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.BINDERKONGOU_DAY, srSlipSlurrykokeibuntyouseiSiropori));

        // ﾊﾞｲﾝﾀﾞｰ混合時間
        this.setItemData(processData, GXHDO102B027Const.BINDERKONGOU_TIME, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.BINDERKONGOU_TIME, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ経過日数
        this.setItemData(processData, GXHDO102B027Const.SLURRYKEIKANISUU, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYKEIKANISUU, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ重量①
        this.setItemData(processData, GXHDO102B027Const.SLURRYJYUURYOU1, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYJYUURYOU1, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ重量②
        this.setItemData(processData, GXHDO102B027Const.SLURRYJYUURYOU2, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYJYUURYOU2, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ重量③
        this.setItemData(processData, GXHDO102B027Const.SLURRYJYUURYOU3, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYJYUURYOU3, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ重量④
        this.setItemData(processData, GXHDO102B027Const.SLURRYJYUURYOU4, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYJYUURYOU4, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ重量⑤
        this.setItemData(processData, GXHDO102B027Const.SLURRYJYUURYOU5, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYJYUURYOU5, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ重量⑥
        this.setItemData(processData, GXHDO102B027Const.SLURRYJYUURYOU6, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYJYUURYOU6, srSlipSlurrykokeibuntyouseiSiropori));

        // ｽﾗﾘｰ合計重量
        this.setItemData(processData, GXHDO102B027Const.SLURRYGOUKEIJYUURYOU, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SLURRYGOUKEIJYUURYOU, srSlipSlurrykokeibuntyouseiSiropori));

        // 固形分調整量
        this.setItemData(processData, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSiropori));

        // ﾄﾙｴﾝ添加量
        this.setItemData(processData, GXHDO102B027Const.TOLUENETENKARYOU, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.TOLUENETENKARYOU, srSlipSlurrykokeibuntyouseiSiropori));

        // ｿﾙﾐｯｸｽ添加量
        this.setItemData(processData, GXHDO102B027Const.SOLMIXTENKARYOU, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.SOLMIXTENKARYOU, srSlipSlurrykokeibuntyouseiSiropori));

        // 担当者
        this.setItemData(processData, GXHDO102B027Const.TANTOUSYA, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.TANTOUSYA, srSlipSlurrykokeibuntyouseiSiropori));

        // 備考1
        this.setItemData(processData, GXHDO102B027Const.BIKOU1, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.BIKOU1, srSlipSlurrykokeibuntyouseiSiropori));

        // 備考2
        this.setItemData(processData, GXHDO102B027Const.BIKOU2, getSrSlipSlurrykokeibuntyouseiSiroporiItemData(GXHDO102B027Const.BIKOU2, srSlipSlurrykokeibuntyouseiSiropori));

    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlurrykokeibuntyouseiSiropori> getSrSlipSlurrykokeibuntyouseiSiroporiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSlipSlurrykokeibuntyouseiSiropori(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlurrykokeibuntyouseiSiropori> loadSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,slurryhinmei,slurrylotno1,slurrylotno2,slurrylotno3,slurryyuukoukigen,kansoukokeibun,"
                + "dassikokeibun,funsaisyuuryounichiji,binderkongounichij,slurrykeikanisuu,slurryjyuuryou1,slurryjyuuryou2,slurryjyuuryou3,slurryjyuuryou4,"
                + "slurryjyuuryou5,slurryjyuuryou6,slurrygoukeijyuuryou,kokeibunhiritu,kokeibuntyouseiryou1,kokeibuntyouseiryou2,kokeibuntyouseiryou,toluenetenkaryou,"
                + "solmixtenkaryou,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_slip_slurrykokeibuntyousei_siropori "
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
        mapping.put("kojyo", "kojyo");                                  // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                  // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                // 枝番
        mapping.put("sliphinmei", "sliphinmei");                        // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                          // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                            // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                    // 原料記号
        mapping.put("slurryhinmei", "slurryhinmei");                    // ｽﾗﾘｰ品名
        mapping.put("slurrylotno1", "slurrylotno1");                    // ｽﾗﾘｰLotNo①
        mapping.put("slurrylotno2", "slurrylotno2");                    // ｽﾗﾘｰLotNo②
        mapping.put("slurrylotno3", "slurrylotno3");                    // ｽﾗﾘｰLotNo③
        mapping.put("slurryyuukoukigen", "slurryyuukoukigen");          // ｽﾗﾘｰ有効期限
        mapping.put("kansoukokeibun", "kansoukokeibun");                // 乾燥固形分
        mapping.put("dassikokeibun", "dassikokeibun");                  // 脱脂固形分
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");  // 粉砕終了日時
        mapping.put("binderkongounichij", "binderkongounichij");        // ﾊﾞｲﾝﾀﾞｰ混合日時
        mapping.put("slurrykeikanisuu", "slurrykeikanisuu");            // ｽﾗﾘｰ経過日数
        mapping.put("slurryjyuuryou1", "slurryjyuuryou1");              // ｽﾗﾘｰ重量①
        mapping.put("slurryjyuuryou2", "slurryjyuuryou2");              // ｽﾗﾘｰ重量②
        mapping.put("slurryjyuuryou3", "slurryjyuuryou3");              // ｽﾗﾘｰ重量③
        mapping.put("slurryjyuuryou4", "slurryjyuuryou4");              // ｽﾗﾘｰ重量④
        mapping.put("slurryjyuuryou5", "slurryjyuuryou5");              // ｽﾗﾘｰ重量⑤
        mapping.put("slurryjyuuryou6", "slurryjyuuryou6");              // ｽﾗﾘｰ重量⑥
        mapping.put("slurrygoukeijyuuryou", "slurrygoukeijyuuryou");    // ｽﾗﾘｰ合計重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                // 固形分比率
        mapping.put("kokeibuntyouseiryou1", "kokeibuntyouseiryou1");    // 固形分調整量➀
        mapping.put("kokeibuntyouseiryou2", "kokeibuntyouseiryou2");    // 固形分調整量➁
        mapping.put("kokeibuntyouseiryou", "kokeibuntyouseiryou");      // 固形分調整量
        mapping.put("toluenetenkaryou", "toluenetenkaryou");            // ﾄﾙｴﾝ添加量
        mapping.put("solmixtenkaryou", "solmixtenkaryou");              // ｿﾙﾐｯｸｽ添加量
        mapping.put("tantousya", "tantousya");                          // 担当者
        mapping.put("bikou1", "bikou1");                                // 備考1
        mapping.put("bikou2", "bikou2");                                // 備考2
        mapping.put("torokunichiji", "torokunichiji");                  // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                    // 更新日時
        mapping.put("revision", "revision");                            // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSlurrykokeibuntyouseiSiropori>> beanHandler = new BeanListHandler<>(SrSlipSlurrykokeibuntyouseiSiropori.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipSlurrykokeibuntyouseiSiropori> loadTmpSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,slurryhinmei,slurrylotno1,slurrylotno2,slurrylotno3,slurryyuukoukigen,kansoukokeibun,"
                + "dassikokeibun,funsaisyuuryounichiji,binderkongounichij,slurrykeikanisuu,slurryjyuuryou1,slurryjyuuryou2,slurryjyuuryou3,slurryjyuuryou4,"
                + "slurryjyuuryou5,slurryjyuuryou6,slurrygoukeijyuuryou,kokeibunhiritu,kokeibuntyouseiryou1,kokeibuntyouseiryou2,kokeibuntyouseiryou,toluenetenkaryou,"
                + "solmixtenkaryou,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_slurrykokeibuntyousei_siropori "
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
        mapping.put("kojyo", "kojyo");                                  // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                  // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                // 枝番
        mapping.put("sliphinmei", "sliphinmei");                        // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                          // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                            // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                    // 原料記号
        mapping.put("slurryhinmei", "slurryhinmei");                    // ｽﾗﾘｰ品名
        mapping.put("slurrylotno1", "slurrylotno1");                    // ｽﾗﾘｰLotNo①
        mapping.put("slurrylotno2", "slurrylotno2");                    // ｽﾗﾘｰLotNo②
        mapping.put("slurrylotno3", "slurrylotno3");                    // ｽﾗﾘｰLotNo③
        mapping.put("slurryyuukoukigen", "slurryyuukoukigen");          // ｽﾗﾘｰ有効期限
        mapping.put("kansoukokeibun", "kansoukokeibun");                // 乾燥固形分
        mapping.put("dassikokeibun", "dassikokeibun");                  // 脱脂固形分
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji");  // 粉砕終了日時
        mapping.put("binderkongounichij", "binderkongounichij");        // ﾊﾞｲﾝﾀﾞｰ混合日時
        mapping.put("slurrykeikanisuu", "slurrykeikanisuu");            // ｽﾗﾘｰ経過日数
        mapping.put("slurryjyuuryou1", "slurryjyuuryou1");              // ｽﾗﾘｰ重量①
        mapping.put("slurryjyuuryou2", "slurryjyuuryou2");              // ｽﾗﾘｰ重量②
        mapping.put("slurryjyuuryou3", "slurryjyuuryou3");              // ｽﾗﾘｰ重量③
        mapping.put("slurryjyuuryou4", "slurryjyuuryou4");              // ｽﾗﾘｰ重量④
        mapping.put("slurryjyuuryou5", "slurryjyuuryou5");              // ｽﾗﾘｰ重量⑤
        mapping.put("slurryjyuuryou6", "slurryjyuuryou6");              // ｽﾗﾘｰ重量⑥
        mapping.put("slurrygoukeijyuuryou", "slurrygoukeijyuuryou");    // ｽﾗﾘｰ合計重量
        mapping.put("kokeibunhiritu", "kokeibunhiritu");                // 固形分比率
        mapping.put("kokeibuntyouseiryou1", "kokeibuntyouseiryou1");    // 固形分調整量➀
        mapping.put("kokeibuntyouseiryou2", "kokeibuntyouseiryou2");    // 固形分調整量➁
        mapping.put("kokeibuntyouseiryou", "kokeibuntyouseiryou");      // 固形分調整量
        mapping.put("toluenetenkaryou", "toluenetenkaryou");            // ﾄﾙｴﾝ添加量
        mapping.put("solmixtenkaryou", "solmixtenkaryou");              // ｿﾙﾐｯｸｽ添加量
        mapping.put("tantousya", "tantousya");                          // 担当者
        mapping.put("bikou1", "bikou1");                                // 備考1
        mapping.put("bikou2", "bikou2");                                // 備考2
        mapping.put("torokunichiji", "torokunichiji");                  // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                    // 更新日時
        mapping.put("revision", "revision");                            // revision
        mapping.put("deleteflag", "deleteflag");                        // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipSlurrykokeibuntyouseiSiropori>> beanHandler = new BeanListHandler<>(SrSlipSlurrykokeibuntyouseiSiropori.class, rowProcessor);

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
     * @param srSlipSlurrykokeibuntyouseiSiropori ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipSlurrykokeibuntyouseiSiropori != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSlurrykokeibuntyouseiSiroporiItemData(itemId, srSlipSlurrykokeibuntyouseiSiropori);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipSlurrykokeibuntyouseiSiropori != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipSlurrykokeibuntyouseiSiroporiItemData(itemId, srSlipSlurrykokeibuntyouseiSiropori);
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_siropori)登録処理
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
    private void insertTmpSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_slurrykokeibuntyousei_siropori ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,slurryhinmei,slurrylotno1,slurrylotno2,slurrylotno3,slurryyuukoukigen,kansoukokeibun,"
                + "dassikokeibun,funsaisyuuryounichiji,binderkongounichij,slurrykeikanisuu,slurryjyuuryou1,slurryjyuuryou2,slurryjyuuryou3,slurryjyuuryou4,"
                + "slurryjyuuryou5,slurryjyuuryou6,slurrygoukeijyuuryou,kokeibunhiritu,kokeibuntyouseiryou1,kokeibuntyouseiryou2,kokeibuntyouseiryou,toluenetenkaryou,"
                + "solmixtenkaryou,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipSlurrykokeibuntyouseiSiropori(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_siropori)更新処理
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
    private void updateTmpSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_slurrykokeibuntyousei_siropori SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,slurryhinmei = ?,slurrylotno1 = ?,slurrylotno2 = ?,slurrylotno3 = ?,slurryyuukoukigen = ?,"
                + "kansoukokeibun = ?,dassikokeibun = ?,funsaisyuuryounichiji = ?,binderkongounichij = ?,slurrykeikanisuu = ?,slurryjyuuryou1 = ?,slurryjyuuryou2 = ?,slurryjyuuryou3 = ?,"
                + "slurryjyuuryou4 = ?,slurryjyuuryou5 = ?,slurryjyuuryou6 = ?,slurrygoukeijyuuryou = ?,kokeibunhiritu = ?,kokeibuntyouseiryou1 = ?,kokeibuntyouseiryou2 = ?,"
                + "kokeibuntyouseiryou = ?,toluenetenkaryou = ?,solmixtenkaryou = ?,tantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSlurrykokeibuntyouseiSiropori> srSlipSlurrykokeibuntyouseiSiroporiList = GXHDO102B027.this.getSrSlipSlurrykokeibuntyouseiSiroporiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori = null;
        if (!srSlipSlurrykokeibuntyouseiSiroporiList.isEmpty()) {
            srSlipSlurrykokeibuntyouseiSiropori = srSlipSlurrykokeibuntyouseiSiroporiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipSlurrykokeibuntyouseiSiropori(false, newRev, 0, "", "", "", systemTime, processData, srSlipSlurrykokeibuntyouseiSiropori);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_siropori)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_slurrykokeibuntyousei_siropori "
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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_siropori)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSlipSlurrykokeibuntyouseiSiropori ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipSlurrykokeibuntyouseiSiropori(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 粉砕終了日時
        String funsaisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B027Const.FUNSAISYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSiropori));
        // ﾊﾞｲﾝﾀﾞｰ混合日時
        String binderkongouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B027Const.BINDERKONGOU_TIME, srSlipSlurrykokeibuntyouseiSiropori));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLIPHINMEI, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLIPLOTNO, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.LOTKUBUN, srSlipSlurrykokeibuntyouseiSiropori))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.GENRYOUKIGOU, srSlipSlurrykokeibuntyouseiSiropori))); // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYHINMEI, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ品名
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYLOTNO1, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰLotNo①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYLOTNO2, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰLotNo②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYLOTNO3, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰLotNo③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYYUUKOUKIGEN, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ有効期限
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.KANSOUKOKEIBUN, srSlipSlurrykokeibuntyouseiSiropori))); // 乾燥固形分
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.DASSIKOKEIBUN, srSlipSlurrykokeibuntyouseiSiropori))); // 脱脂固形分
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.FUNSAISYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSiropori),
                "".equals(funsaisyuuryouTime) ? "0000" : funsaisyuuryouTime)); // 粉砕終了日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.BINDERKONGOU_DAY, srSlipSlurrykokeibuntyouseiSiropori),
                "".equals(binderkongouTime) ? "0000" : binderkongouTime)); // ﾊﾞｲﾝﾀﾞｰ混合日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYKEIKANISUU, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ経過日数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU1, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU2, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU3, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU4, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU5, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU6, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量⑥
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SLURRYGOUKEIJYUURYOU, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ合計重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.KOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分比率
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU1, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分調整量➀
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU2, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分調整量➁
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分調整量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.TOLUENETENKARYOU, srSlipSlurrykokeibuntyouseiSiropori))); // ﾄﾙｴﾝ添加量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.SOLMIXTENKARYOU, srSlipSlurrykokeibuntyouseiSiropori))); // ｿﾙﾐｯｸｽ添加量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.TANTOUSYA, srSlipSlurrykokeibuntyouseiSiropori))); // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.BIKOU1, srSlipSlurrykokeibuntyouseiSiropori))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B027Const.BIKOU2, srSlipSlurrykokeibuntyouseiSiropori))); // 備考2

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)(sr_slip_slurrykokeibuntyousei_siropori)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipSlurrykokeibuntyouseiSiropori 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipSlurrykokeibuntyouseiSiropori tmpSrSlipSlurrykokeibuntyouseiSiropori) throws SQLException {

        String sql = "INSERT INTO sr_slip_slurrykokeibuntyousei_siropori ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,slurryhinmei,slurrylotno1,slurrylotno2,slurrylotno3,slurryyuukoukigen,kansoukokeibun,"
                + "dassikokeibun,funsaisyuuryounichiji,binderkongounichij,slurrykeikanisuu,slurryjyuuryou1,slurryjyuuryou2,slurryjyuuryou3,slurryjyuuryou4,"
                + "slurryjyuuryou5,slurryjyuuryou6,slurrygoukeijyuuryou,kokeibunhiritu,kokeibuntyouseiryou1,kokeibuntyouseiryou2,kokeibuntyouseiryou,toluenetenkaryou,"
                + "solmixtenkaryou,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipSlurrykokeibuntyouseiSiropori(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrSlipSlurrykokeibuntyouseiSiropori);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)(sr_slip_slurrykokeibuntyousei_siropori)更新処理
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
    private void updateSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_slip_slurrykokeibuntyousei_siropori SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,slurryhinmei = ?,slurrylotno1 = ?,slurrylotno2 = ?,slurrylotno3 = ?,slurryyuukoukigen = ?,"
                + "kansoukokeibun = ?,dassikokeibun = ?,funsaisyuuryounichiji = ?,binderkongounichij = ?,slurrykeikanisuu = ?,slurryjyuuryou1 = ?,slurryjyuuryou2 = ?,slurryjyuuryou3 = ?,"
                + "slurryjyuuryou4 = ?,slurryjyuuryou5 = ?,slurryjyuuryou6 = ?,slurrygoukeijyuuryou = ?,kokeibunhiritu = ?,kokeibuntyouseiryou1 = ?,kokeibuntyouseiryou2 = ?,"
                + "kokeibuntyouseiryou = ?,toluenetenkaryou = ?,solmixtenkaryou = ?,tantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipSlurrykokeibuntyouseiSiropori> srSlipSlurrykokeibuntyouseiSiroporiList = GXHDO102B027.this.getSrSlipSlurrykokeibuntyouseiSiroporiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori = null;
        if (!srSlipSlurrykokeibuntyouseiSiroporiList.isEmpty()) {
            srSlipSlurrykokeibuntyouseiSiropori = srSlipSlurrykokeibuntyouseiSiroporiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipSlurrykokeibuntyouseiSiropori(false, newRev, "", "", "", systemTime, processData, srSlipSlurrykokeibuntyouseiSiropori);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)(sr_slip_slurrykokeibuntyousei_siropori)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipSlurrykokeibuntyouseiSiropori ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipSlurrykokeibuntyouseiSiropori(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 粉砕終了日時
        String funsaisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B027Const.FUNSAISYUURYOU_TIME, srSlipSlurrykokeibuntyouseiSiropori));
        // ﾊﾞｲﾝﾀﾞｰ混合日時
        String binderkongouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B027Const.BINDERKONGOU_TIME, srSlipSlurrykokeibuntyouseiSiropori));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.SLIPHINMEI, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.SLIPLOTNO, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.LOTKUBUN, srSlipSlurrykokeibuntyouseiSiropori))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B027Const.GENRYOUKIGOU, srSlipSlurrykokeibuntyouseiSiropori))); // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYHINMEI, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ品名
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYLOTNO1, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰLotNo①
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYLOTNO2, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰLotNo②
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B027Const.SLURRYLOTNO3, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰLotNo③
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.SLURRYYUUKOUKIGEN, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ有効期限
        params.add(DBUtil.stringToBigDecimalObject(getItemKikakuchi(pItemList, GXHDO102B027Const.KANSOUKOKEIBUN, srSlipSlurrykokeibuntyouseiSiropori))); // 乾燥固形分
        params.add(DBUtil.stringToBigDecimalObject(getItemKikakuchi(pItemList, GXHDO102B027Const.DASSIKOKEIBUN, srSlipSlurrykokeibuntyouseiSiropori))); // 脱脂固形分
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B027Const.FUNSAISYUURYOU_DAY, srSlipSlurrykokeibuntyouseiSiropori),
                "".equals(funsaisyuuryouTime) ? "0000" : funsaisyuuryouTime)); // 粉砕終了日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B027Const.BINDERKONGOU_DAY, srSlipSlurrykokeibuntyouseiSiropori),
                "".equals(binderkongouTime) ? "0000" : binderkongouTime)); // ﾊﾞｲﾝﾀﾞｰ混合日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYKEIKANISUU, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ経過日数
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU1, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU2, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU3, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU4, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量④
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU5, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量⑤
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYJYUURYOU6, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ重量⑥
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SLURRYGOUKEIJYUURYOU, srSlipSlurrykokeibuntyouseiSiropori))); // ｽﾗﾘｰ合計重量
        params.add(DBUtil.stringToBigDecimalObject(getItemKikakuchi(pItemList, GXHDO102B027Const.KOKEIBUNHIRITU, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分比率
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU1, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分調整量➀
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU2, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分調整量➁
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU, srSlipSlurrykokeibuntyouseiSiropori))); // 固形分調整量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.TOLUENETENKARYOU, srSlipSlurrykokeibuntyouseiSiropori))); // ﾄﾙｴﾝ添加量
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B027Const.SOLMIXTENKARYOU, srSlipSlurrykokeibuntyouseiSiropori))); // ｿﾙﾐｯｸｽ添加量
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.TANTOUSYA, srSlipSlurrykokeibuntyouseiSiropori))); // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.BIKOU1, srSlipSlurrykokeibuntyouseiSiropori))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B027Const.BIKOU2, srSlipSlurrykokeibuntyouseiSiropori))); // 備考2

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
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)(sr_slip_slurrykokeibuntyousei_siropori)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_slip_slurrykokeibuntyousei_siropori "
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
     * [ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_slurrykokeibuntyousei_siropori "
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
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000027"));
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
     * @param srSlipSlurrykokeibuntyouseiSiropori ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)データ
     * @return DB値
     */
    private String getSrSlipSlurrykokeibuntyouseiSiroporiItemData(String itemId, SrSlipSlurrykokeibuntyouseiSiropori srSlipSlurrykokeibuntyouseiSiropori) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B027Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B027Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B027Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getLotkubun());

            // 原料記号
            case GXHDO102B027Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getGenryoukigou());

            // ｽﾗﾘｰ品名
            case GXHDO102B027Const.SLURRYHINMEI:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryhinmei());

            // ｽﾗﾘｰLotNo①
            case GXHDO102B027Const.SLURRYLOTNO1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurrylotno1());

            // ｽﾗﾘｰLotNo②
            case GXHDO102B027Const.SLURRYLOTNO2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurrylotno2());

            // ｽﾗﾘｰLotNo③
            case GXHDO102B027Const.SLURRYLOTNO3:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurrylotno3());

            // ｽﾗﾘｰ有効期限
            case GXHDO102B027Const.SLURRYYUUKOUKIGEN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryyuukoukigen());

            // 乾燥固形分
            case GXHDO102B027Const.KANSOUKOKEIBUN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getKansoukokeibun());

            // 脱脂固形分
            case GXHDO102B027Const.DASSIKOKEIBUN:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getDassikokeibun());

            // 粉砕終了日
            case GXHDO102B027Const.FUNSAISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSiropori.getFunsaisyuuryounichiji(), "yyMMdd");

            // 粉砕終了時間
            case GXHDO102B027Const.FUNSAISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSiropori.getFunsaisyuuryounichiji(), "HHmm");

            // ﾊﾞｲﾝﾀﾞｰ混合日
            case GXHDO102B027Const.BINDERKONGOU_DAY:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSiropori.getBinderkongounichij(), "yyMMdd");

            // ﾊﾞｲﾝﾀﾞｰ混合時間
            case GXHDO102B027Const.BINDERKONGOU_TIME:
                return DateUtil.formattedTimestamp(srSlipSlurrykokeibuntyouseiSiropori.getBinderkongounichij(), "HHmm");

            // ｽﾗﾘｰ経過日数
            case GXHDO102B027Const.SLURRYKEIKANISUU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurrykeikanisuu());

            // ｽﾗﾘｰ重量①
            case GXHDO102B027Const.SLURRYJYUURYOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryjyuuryou1());

            // ｽﾗﾘｰ重量②
            case GXHDO102B027Const.SLURRYJYUURYOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryjyuuryou2());

            // ｽﾗﾘｰ重量③
            case GXHDO102B027Const.SLURRYJYUURYOU3:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryjyuuryou3());

            // ｽﾗﾘｰ重量④
            case GXHDO102B027Const.SLURRYJYUURYOU4:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryjyuuryou4());

            // ｽﾗﾘｰ重量⑤
            case GXHDO102B027Const.SLURRYJYUURYOU5:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryjyuuryou5());

            // ｽﾗﾘｰ重量⑥
            case GXHDO102B027Const.SLURRYJYUURYOU6:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurryjyuuryou6());

            // ｽﾗﾘｰ合計重量
            case GXHDO102B027Const.SLURRYGOUKEIJYUURYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSlurrygoukeijyuuryou());

            // 固形分比率
            case GXHDO102B027Const.KOKEIBUNHIRITU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getKokeibunhiritu());

            // 固形分調整量➀
            case GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getKokeibuntyouseiryou1());

            // 固形分調整量➁
            case GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getKokeibuntyouseiryou2());

            // 固形分調整量
            case GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getKokeibuntyouseiryou());

            // ﾄﾙｴﾝ添加量
            case GXHDO102B027Const.TOLUENETENKARYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getToluenetenkaryou());

            // ｿﾙﾐｯｸｽ添加量
            case GXHDO102B027Const.SOLMIXTENKARYOU:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getSolmixtenkaryou());

            // 担当者
            case GXHDO102B027Const.TANTOUSYA:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getTantousya());

            // 備考1
            case GXHDO102B027Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getBikou1());

            // 備考2
            case GXHDO102B027Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipSlurrykokeibuntyouseiSiropori.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・ｽﾗﾘｰ固形分調整(白ﾎﾟﾘ)_仮登録(tmp_sr_slip_slurrykokeibuntyousei_siropori)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSlipSlurrykokeibuntyouseiSiropori(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_slurrykokeibuntyousei_siropori ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,slurryhinmei,slurrylotno1,slurrylotno2,slurrylotno3,slurryyuukoukigen,kansoukokeibun,"
                + "dassikokeibun,funsaisyuuryounichiji,binderkongounichij,slurrykeikanisuu,slurryjyuuryou1,slurryjyuuryou2,slurryjyuuryou3,slurryjyuuryou4,"
                + "slurryjyuuryou5,slurryjyuuryou6,slurrygoukeijyuuryou,kokeibunhiritu,kokeibuntyouseiryou1,kokeibuntyouseiryou2,kokeibuntyouseiryou,toluenetenkaryou,"
                + "solmixtenkaryou,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,slurryhinmei,slurrylotno1,slurrylotno2,slurrylotno3,slurryyuukoukigen,kansoukokeibun,"
                + "dassikokeibun,funsaisyuuryounichiji,binderkongounichij,slurrykeikanisuu,slurryjyuuryou1,slurryjyuuryou2,slurryjyuuryou3,slurryjyuuryou4,"
                + "slurryjyuuryou5,slurryjyuuryou6,slurrygoukeijyuuryou,kokeibunhiritu,kokeibuntyouseiryou1,kokeibuntyouseiryou2,kokeibuntyouseiryou,toluenetenkaryou,"
                + "solmixtenkaryou,tantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_slurrykokeibuntyousei_siropori "
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
    private void initGXHDO102B027A(ProcessData processData) {
        GXHDO102B027A bean = (GXHDO102B027A) getFormBean("gXHDO102B027A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B027Const.WIPLOTNO));
        bean.setSliphinmei(getItemRow(processData.getItemList(), GXHDO102B027Const.SLIPHINMEI));
        bean.setSliplotno(getItemRow(processData.getItemList(), GXHDO102B027Const.SLIPLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B027Const.LOTKUBUN));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B027Const.GENRYOUKIGOU));
        bean.setSlurryhinmei(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYHINMEI));
        bean.setSlurrylotno1(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYLOTNO1));
        bean.setSlurrylotno2(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYLOTNO2));
        bean.setSlurrylotno3(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYLOTNO3));
        bean.setSlurryyuukoukigen(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYYUUKOUKIGEN));
        bean.setKansoukokeibun(getItemRow(processData.getItemList(), GXHDO102B027Const.KANSOUKOKEIBUN));
        bean.setDassikokeibun(getItemRow(processData.getItemList(), GXHDO102B027Const.DASSIKOKEIBUN));
        bean.setFunsaisyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B027Const.FUNSAISYUURYOU_DAY));
        bean.setFunsaisyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B027Const.FUNSAISYUURYOU_TIME));
        bean.setBinderkongou_day(getItemRow(processData.getItemList(), GXHDO102B027Const.BINDERKONGOU_DAY));
        bean.setBinderkongou_time(getItemRow(processData.getItemList(), GXHDO102B027Const.BINDERKONGOU_TIME));
        bean.setSlurrykeikanisuu(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYKEIKANISUU));
        bean.setSlurryjyuuryou1(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU1));
        bean.setSlurryjyuuryou2(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU2));
        bean.setSlurryjyuuryou3(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU3));
        bean.setSlurryjyuuryou4(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU4));
        bean.setSlurryjyuuryou5(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU5));
        bean.setSlurryjyuuryou6(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYJYUURYOU6));
        bean.setSlurrygoukeijyuuryou(getItemRow(processData.getItemList(), GXHDO102B027Const.SLURRYGOUKEIJYUURYOU));
        bean.setKokeibunhiritu(getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNHIRITU));
        bean.setKokeibuntyouseiryou1(getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU1));
        bean.setKokeibuntyouseiryou2(getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU2));
        bean.setKokeibuntyouseiryou(getItemRow(processData.getItemList(), GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU));
        bean.setToluenetenkaryou(getItemRow(processData.getItemList(), GXHDO102B027Const.TOLUENETENKARYOU));
        bean.setSolmixtenkaryou(getItemRow(processData.getItemList(), GXHDO102B027Const.SOLMIXTENKARYOU));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B027Const.TANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B027Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B027Const.BIKOU2));

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
        allItemIdMap.put(GXHDO102B027Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B027Const.SLIPHINMEI, "ｽﾘｯﾌﾟ品名");
        allItemIdMap.put(GXHDO102B027Const.SLIPLOTNO, "ｽﾘｯﾌﾟLotNo");
        allItemIdMap.put(GXHDO102B027Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B027Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B027Const.SLURRYHINMEI, "ｽﾗﾘｰ品名");
        allItemIdMap.put(GXHDO102B027Const.SLURRYLOTNO1, "ｽﾗﾘｰLotNo①");
        allItemIdMap.put(GXHDO102B027Const.SLURRYLOTNO2, "ｽﾗﾘｰLotNo②");
        allItemIdMap.put(GXHDO102B027Const.SLURRYLOTNO3, "ｽﾗﾘｰLotNo③");
        allItemIdMap.put(GXHDO102B027Const.SLURRYYUUKOUKIGEN, "ｽﾗﾘｰ有効期限");
        allItemIdMap.put(GXHDO102B027Const.KANSOUKOKEIBUN, "乾燥固形分");
        allItemIdMap.put(GXHDO102B027Const.DASSIKOKEIBUN, "脱脂固形分");
        allItemIdMap.put(GXHDO102B027Const.FUNSAISYUURYOU_DAY, "粉砕終了日");
        allItemIdMap.put(GXHDO102B027Const.FUNSAISYUURYOU_TIME, "粉砕終了時間");
        allItemIdMap.put(GXHDO102B027Const.BINDERKONGOU_DAY, "ﾊﾞｲﾝﾀﾞｰ混合日");
        allItemIdMap.put(GXHDO102B027Const.BINDERKONGOU_TIME, "ﾊﾞｲﾝﾀﾞｰ混合時間");
        allItemIdMap.put(GXHDO102B027Const.SLURRYKEIKANISUU, "ｽﾗﾘｰ経過日数");
        allItemIdMap.put(GXHDO102B027Const.SLURRYJYUURYOU1, "ｽﾗﾘｰ重量①");
        allItemIdMap.put(GXHDO102B027Const.SLURRYJYUURYOU2, "ｽﾗﾘｰ重量②");
        allItemIdMap.put(GXHDO102B027Const.SLURRYJYUURYOU3, "ｽﾗﾘｰ重量③");
        allItemIdMap.put(GXHDO102B027Const.SLURRYJYUURYOU4, "ｽﾗﾘｰ重量④");
        allItemIdMap.put(GXHDO102B027Const.SLURRYJYUURYOU5, "ｽﾗﾘｰ重量⑤");
        allItemIdMap.put(GXHDO102B027Const.SLURRYJYUURYOU6, "ｽﾗﾘｰ重量⑥");
        allItemIdMap.put(GXHDO102B027Const.SLURRYGOUKEIJYUURYOU, "ｽﾗﾘｰ合計重量");
        allItemIdMap.put(GXHDO102B027Const.KOKEIBUNHIRITU, "固形分比率");
        allItemIdMap.put(GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU1, "固形分調整量➀");
        allItemIdMap.put(GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU2, "固形分調整量➁");
        allItemIdMap.put(GXHDO102B027Const.KOKEIBUNTYOUSEIRYOU, "固形分調整量");
        allItemIdMap.put(GXHDO102B027Const.TOLUENETENKARYOU, "ﾄﾙｴﾝ添加量");
        allItemIdMap.put(GXHDO102B027Const.SOLMIXTENKARYOU, "ｿﾙﾐｯｸｽ添加量");
        allItemIdMap.put(GXHDO102B027Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B027Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B027Const.BIKOU2, "備考2");

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
