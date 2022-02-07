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
import jp.co.kccs.xhd.db.model.SrSlipBinderhyouryouTounyuu;
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
 * 変更日	2021/12/08<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B031(ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入)
 *
 * @author KCSS K.Jo
 * @since 2021/12/08
 */
public class GXHDO102B031 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B031.class.getName());
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B031() {
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
            initGXHDO102B031A(processData);

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
            processData.setNoCheckButtonId(Arrays.asList(GXHDO102B031Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B031Const.BTN_TOUNYUUKAISI_TOP,
                    GXHDO102B031Const.BTN_BINDERKEIRYOU_TOP,
                    GXHDO102B031Const.BTN_TOUNYUUKAISI_TOP,
                    GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_TOP,
                    GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_TOP,
                    GXHDO102B031Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B031Const.BTN_TOUNYUUKAISI_BOTTOM,
                    GXHDO102B031Const.BTN_BINDERKEIRYOU_BOTTOM,
                    GXHDO102B031Const.BTN_TOUNYUUKAISI_BOTTOM,
                    GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_BOTTOM,
                    GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B031Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B031Const.BTN_INSERT_TOP,
                    GXHDO102B031Const.BTN_DELETE_TOP,
                    GXHDO102B031Const.BTN_UPDATE_TOP,
                    GXHDO102B031Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B031Const.BTN_INSERT_BOTTOM,
                    GXHDO102B031Const.BTN_DELETE_BOTTOM,
                    GXHDO102B031Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B031Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B031Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B031Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B031Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B031Const.BTN_INSERT_TOP:
            case GXHDO102B031Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B031Const.BTN_UPDATE_TOP:
            case GXHDO102B031Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B031Const.BTN_DELETE_TOP:
            case GXHDO102B031Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // ﾊﾞｲﾝﾀﾞｰ秤量日時
            case GXHDO102B031Const.BTN_BINDERKEIRYOU_TOP:
            case GXHDO102B031Const.BTN_BINDERKEIRYOU_BOTTOM:
                method = "setBinderkeiryouDateTime";
                break;
            // 投入開始日時
            case GXHDO102B031Const.BTN_TOUNYUUKAISI_TOP:
            case GXHDO102B031Const.BTN_TOUNYUUKAISI_BOTTOM:
                method = "setTounyuukaisiDateTime";
                break;
            // 投入終了日時
            case GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_TOP:
            case GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_BOTTOM:
                method = "setTounyuusyuuryouDateTime";
                break;
            // ﾊﾞｲﾝﾀﾞｰ添加量合計計算
            case GXHDO102B031Const.BTN_BINDERTENKARYOUGOUKEI_TOP:
            case GXHDO102B031Const.BTN_BINDERTENKARYOUGOUKEI_BOTTOM:
                method = "setBindertenkaryougoukei";
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

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrSlipBinderhyouryouTounyuu> srSlipBinderhyouryouTounyuuDataList = getSrSlipBinderhyouryouTounyuuData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srSlipBinderhyouryouTounyuuDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSlipBinderhyouryouTounyuuDataList.get(0));

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

                // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録処理
                insertTmpSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録更新処理
                updateTmpSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList);

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
     * 規格値の入力値チェックを行う。
     * 規格値のエラー対象は引数のリスト(kikakuchiInputErrorInfoList)に項目情報を詰めて返される。
     *
     * @param processData 処理データ
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     * @return チェックの正常終了時はNULL、異常時は内容に応じたエラーメッセージ情報を返す。
     */
    private ErrorMessageInfo checkInputKikakuchi(ProcessData processData, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // ﾊﾞｲﾝﾀﾞｰ添加量合計に規格値を設定する
        setKikakuItem(processData, GXHDO102B031Const.BINDERTENKARYOUKIKAKU, GXHDO102B031Const.BINDERTENKARYOUGOUKEI);
        List<String> kikakuItemList = Arrays.asList(GXHDO102B031Const.BINDERTENKARYOUKIKAKU);
        processData.getItemList().stream().filter(
                (fxhdd01) -> !(kikakuItemList.contains(fxhdd01.getItemId()) || StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);

        return errorMessageInfo;
    }

    /**
     * 規格値チェック項目に規格値を設定する
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param checkItem 規格値チェック項目
     */
    private void setKikakuItem(ProcessData processData, String kikakuItem, String checkItem) {

        FXHDD01 kikakuItemFxhdd01 = getItemRow(processData.getItemList(), kikakuItem);
        FXHDD01 checkItemFxhdd01 = getItemRow(processData.getItemList(), checkItem);
        if (kikakuItemFxhdd01 == null || checkItemFxhdd01 == null) {
            return;
        }
        // 項目の規格値を設置
        checkItemFxhdd01.setKikakuChi(kikakuItemFxhdd01.getKikakuChi());
        checkItemFxhdd01.setStandardPattern(kikakuItemFxhdd01.getStandardPattern());
    }

    /**
     * 登録・修正項目チェック
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {
        ValidateUtil validateUtil = new ValidateUtil();
        // 投入開始日時、投入終了日時前後チェック
        FXHDD01 itemKaisiDay = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAISI_DAY); //　投入開始日
        FXHDD01 itemKaisiTime = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAISI_TIME); // 投入開始時間
        FXHDD01 itemSyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUSYUURYOU_DAY); // 投入終了日
        FXHDD01 itemSyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUSYUURYOU_TIME); // 投入終了時間
        if (itemKaisiDay != null && itemKaisiTime != null && itemSyuuryouDay != null && itemSyuuryouTime != null) {
            Date kaisiDate = DateUtil.convertStringToDate(itemKaisiDay.getValue(), itemKaisiTime.getValue());
            Date syuuryouDate = DateUtil.convertStringToDate(itemSyuuryouDay.getValue(), itemSyuuryouTime.getValue());
            //R001チェック呼出し
            String msgCheckR001 = validateUtil.checkR001("投入開始日時", kaisiDate, "投入終了日時", syuuryouDate);
            if (!StringUtil.isEmpty(msgCheckR001)) {
                //エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemKaisiDay, itemKaisiTime, itemSyuuryouDay, itemSyuuryouTime);
                return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
            }
        }
        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ：ﾊﾞｲﾝﾀﾞｰ品名、ﾎｯﾊﾟｰ使用、投入確認①~⑥
        List<String> itemIdList = Arrays.asList(GXHDO102B031Const.BINDERHINMEI, GXHDO102B031Const.HOPPERSIYOU, GXHDO102B031Const.TOUNYUUKAKUNIN1, GXHDO102B031Const.TOUNYUUKAKUNIN2,
                GXHDO102B031Const.TOUNYUUKAKUNIN3, GXHDO102B031Const.TOUNYUUKAKUNIN4, GXHDO102B031Const.TOUNYUUKAKUNIN5, GXHDO102B031Const.TOUNYUUKAKUNIN6);
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
            SrSlipBinderhyouryouTounyuu tmpSrSlipBinderhyouryouTounyuu = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSlipBinderhyouryouTounyuu> srSlipBinderhyouryouTounyuuList = getSrSlipBinderhyouryouTounyuuData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srSlipBinderhyouryouTounyuuList.isEmpty()) {
                    tmpSrSlipBinderhyouryouTounyuu = srSlipBinderhyouryouTounyuuList.get(0);
                }

                deleteTmpSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_登録処理
            insertSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrSlipBinderhyouryouTounyuu);
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
        ErrorMessageInfo errorMessageInfo = checkInputKikakuchi(processData, kikakuchiInputErrorInfoList);

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
        processData.setUserAuthParam(GXHDO102B031Const.USER_AUTH_UPDATE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_更新処理
            updateSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
        processData.setUserAuthParam(GXHDO102B031Const.USER_AUTH_DELETE_PARAM);

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

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_削除処理
            deleteSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
     * ﾊﾞｲﾝﾀﾞｰ秤量日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBinderkeiryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKEIRYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKEIRYOU_TIME);
        if (itemDay != null && itemTime != null && StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 投入開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTounyuukaisiDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 投入終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTounyuusyuuryouDateTime(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUSYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * ﾊﾞｲﾝﾀﾞｰ添加量合計計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBindertenkaryougoukei(ProcessData processData) {

        FXHDD01 itemHyouryou1 = getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU1); // 秤量①
        FXHDD01 itemHyouryou2 = getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU2); // 秤量②
        FXHDD01 itemHyouryou3 = getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU3); // 秤量③
        FXHDD01 itemHyouryou4 = getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU4); // 秤量④
        FXHDD01 itemHyouryou5 = getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU5); // 秤量⑤
        FXHDD01 itemHyouryou6 = getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU6); // 秤量⑥
        ErrorMessageInfo checkItemErrorInfo = checkHyouryou(itemHyouryou1, itemHyouryou2, itemHyouryou3, itemHyouryou4, itemHyouryou5, itemHyouryou6);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }
        processData.setMethod("");
        // 「ﾊﾞｲﾝﾀﾞｰ添加量合計」計算処理
        calcBindertenkaryougoukei(processData, itemHyouryou1, itemHyouryou2, itemHyouryou3, itemHyouryou4, itemHyouryou5, itemHyouryou6);
        return processData;
    }

    /**
     * 【ﾊﾞｲﾝﾀﾞｰ添加量合計計算】ﾎﾞﾀﾝ押下時ﾁｪｯｸ処理
     *
     * @param itemHyouryou1 秤量①
     * @param itemHyouryou2 秤量②
     * @param itemHyouryou3 秤量③
     * @param itemHyouryou4 秤量④
     * @param itemHyouryou5 秤量⑤
     * @param itemHyouryou6 秤量⑥
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkHyouryou(FXHDD01 itemHyouryou1, FXHDD01 itemHyouryou2, FXHDD01 itemHyouryou3, FXHDD01 itemHyouryou4,
            FXHDD01 itemHyouryou5, FXHDD01 itemHyouryou6) {

        //「秤量①」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemHyouryou1.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryou1);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemHyouryou1.getLabel1());
        }
        //「秤量②」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemHyouryou2.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryou2);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemHyouryou2.getLabel1());
        }
        //「秤量③」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemHyouryou3.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryou3);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemHyouryou3.getLabel1());
        }
        //「秤量④」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemHyouryou4.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryou4);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemHyouryou4.getLabel1());
        }
        //「秤量⑤」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemHyouryou5.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryou5);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemHyouryou5.getLabel1());
        }
        //「秤量⑥」ﾁｪｯｸ
        if (StringUtil.isEmpty(itemHyouryou6.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHyouryou6);
            return MessageUtil.getErrorMessageInfo("XHD-000037", true, false, errFxhdd01List, itemHyouryou6.getLabel1());
        }
        return null;
    }

    /**
     * 「ﾊﾞｲﾝﾀﾞｰ添加量合計」計算処理
     *
     * @param processData 処理制御データ
     * @param itemHyouryou1 秤量①
     * @param itemHyouryou2 秤量②
     * @param itemHyouryou3 秤量③
     * @param itemHyouryou4 秤量④
     * @param itemHyouryou5 秤量⑤
     * @param itemHyouryou6 秤量⑥
     */
    private void calcBindertenkaryougoukei(ProcessData processData, FXHDD01 itemHyouryou1, FXHDD01 itemHyouryou2, FXHDD01 itemHyouryou3, FXHDD01 itemHyouryou4,
            FXHDD01 itemHyouryou5, FXHDD01 itemHyouryou6) {
        try {

            FXHDD01 bindertenkaryougoukei = getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERTENKARYOUGOUKEI); // ﾊﾞｲﾝﾀﾞｰ添加量合計
            BigDecimal itemHyouryou1Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemHyouryou1.getValue()); // 秤量①
            BigDecimal itemHyouryou2Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemHyouryou2.getValue()); // 秤量②
            BigDecimal itemHyouryou3Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemHyouryou3.getValue()); // 秤量③
            BigDecimal itemHyouryou4Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemHyouryou4.getValue()); // 秤量④
            BigDecimal itemHyouryou5Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemHyouryou5.getValue()); // 秤量⑤
            BigDecimal itemHyouryou6Va1 = (BigDecimal) DBUtil.stringToBigDecimalObject(itemHyouryou6.getValue()); // 秤量⑥
            // 「秤量①」 + 「秤量②」 + 「秤量③」 + 「秤量④」 + 「秤量⑤」 + 「秤量⑥」 を算出する。
            BigDecimal bindertenkaryougoukeiVal = itemHyouryou1Va1.add(itemHyouryou2Va1).add(itemHyouryou3Va1).add(itemHyouryou4Va1).add(itemHyouryou5Va1).add(itemHyouryou6Va1);
            bindertenkaryougoukei.setValue(bindertenkaryougoukeiVal.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
            ErrUtil.outputErrorLog("ﾊﾞｲﾝﾀﾞｰ添加量合計計算にエラー発生", ex, LOGGER);
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
                activeIdList.addAll(Arrays.asList(GXHDO102B031Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B031Const.BTN_BINDERKEIRYOU_TOP,
                        GXHDO102B031Const.BTN_TOUNYUUKAISI_TOP,
                        GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_TOP,
                        GXHDO102B031Const.BTN_BINDERTENKARYOUGOUKEI_TOP,
                        GXHDO102B031Const.BTN_UPDATE_TOP,
                        GXHDO102B031Const.BTN_DELETE_TOP,
                        GXHDO102B031Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B031Const.BTN_BINDERKEIRYOU_BOTTOM,
                        GXHDO102B031Const.BTN_TOUNYUUKAISI_BOTTOM,
                        GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_BOTTOM,
                        GXHDO102B031Const.BTN_BINDERTENKARYOUGOUKEI_BOTTOM,
                        GXHDO102B031Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B031Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B031Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B031Const.BTN_INSERT_TOP,
                        GXHDO102B031Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B031Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO102B031Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B031Const.BTN_BINDERKEIRYOU_TOP,
                        GXHDO102B031Const.BTN_TOUNYUUKAISI_TOP,
                        GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_TOP,
                        GXHDO102B031Const.BTN_BINDERTENKARYOUGOUKEI_TOP,
                        GXHDO102B031Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B031Const.BTN_INSERT_TOP,
                        GXHDO102B031Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B031Const.BTN_BINDERKEIRYOU_BOTTOM,
                        GXHDO102B031Const.BTN_TOUNYUUKAISI_BOTTOM,
                        GXHDO102B031Const.BTN_TOUNYUUSYUURYOU_BOTTOM,
                        GXHDO102B031Const.BTN_BINDERTENKARYOUGOUKEI_BOTTOM,
                        GXHDO102B031Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B031Const.BTN_INSERT_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B031Const.BTN_UPDATE_TOP,
                        GXHDO102B031Const.BTN_DELETE_TOP,
                        GXHDO102B031Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B031Const.BTN_DELETE_BOTTOM
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
        this.setItemData(processData, GXHDO102B031Const.WIPLOTNO, lotNo);
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B031Const.SLIPHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B031Const.SLIPLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名称
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));

        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B031Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B031Const.LOTKUBUN, lotkubuncode);
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

        List<SrSlipBinderhyouryouTounyuu> srSlipBinderhyouryouTounyuuList = new ArrayList<>();
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

            // ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ取得
            srSlipBinderhyouryouTounyuuList = getSrSlipBinderhyouryouTounyuuData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srSlipBinderhyouryouTounyuuList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSlipBinderhyouryouTounyuuList.isEmpty()) {
            return false;
        }
        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSlipBinderhyouryouTounyuuList.get(0));
        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSlipBinderhyouryouTounyuu ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu) {
        // ｽﾘｯﾌﾟ品名
        this.setItemData(processData, GXHDO102B031Const.SLIPHINMEI, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.SLIPHINMEI, srSlipBinderhyouryouTounyuu));

        // ｽﾘｯﾌﾟLotNo
        this.setItemData(processData, GXHDO102B031Const.SLIPLOTNO, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.SLIPLOTNO, srSlipBinderhyouryouTounyuu));

        // ﾛｯﾄ区分
        this.setItemData(processData, GXHDO102B031Const.LOTKUBUN, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.LOTKUBUN, srSlipBinderhyouryouTounyuu));

        // 混合ﾀﾝｸNo
        this.setItemData(processData, GXHDO102B031Const.KONGOUTANKNO, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.KONGOUTANKNO, srSlipBinderhyouryouTounyuu));

        // ﾊﾞｲﾝﾀﾞｰ秤量日
        this.setItemData(processData, GXHDO102B031Const.BINDERKEIRYOU_DAY, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.BINDERKEIRYOU_DAY, srSlipBinderhyouryouTounyuu));

        // ﾊﾞｲﾝﾀﾞｰ秤量時間
        this.setItemData(processData, GXHDO102B031Const.BINDERKEIRYOU_TIME, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.BINDERKEIRYOU_TIME, srSlipBinderhyouryouTounyuu));

        // ﾊﾞｲﾝﾀﾞｰ品名
        this.setItemData(processData, GXHDO102B031Const.BINDERHINMEI, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.BINDERHINMEI, srSlipBinderhyouryouTounyuu));

        // ﾎｯﾊﾟｰ使用
        this.setItemData(processData, GXHDO102B031Const.HOPPERSIYOU, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HOPPERSIYOU, srSlipBinderhyouryouTounyuu));

        // 投入開始日
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAISI_DAY, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAISI_DAY, srSlipBinderhyouryouTounyuu));

        // 投入開始時間
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAISI_TIME, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAISI_TIME, srSlipBinderhyouryouTounyuu));

        // 秤量①
        this.setItemData(processData, GXHDO102B031Const.HYOURYOU1, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HYOURYOU1, srSlipBinderhyouryouTounyuu));

        // 投入確認①
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAKUNIN1, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAKUNIN1, srSlipBinderhyouryouTounyuu));

        // 秤量②
        this.setItemData(processData, GXHDO102B031Const.HYOURYOU2, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HYOURYOU2, srSlipBinderhyouryouTounyuu));

        // 投入確認②
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAKUNIN2, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAKUNIN2, srSlipBinderhyouryouTounyuu));

        // 秤量③
        this.setItemData(processData, GXHDO102B031Const.HYOURYOU3, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HYOURYOU3, srSlipBinderhyouryouTounyuu));

        // 投入確認③
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAKUNIN3, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAKUNIN3, srSlipBinderhyouryouTounyuu));

        // 秤量④
        this.setItemData(processData, GXHDO102B031Const.HYOURYOU4, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HYOURYOU4, srSlipBinderhyouryouTounyuu));

        // 投入確認④
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAKUNIN4, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAKUNIN4, srSlipBinderhyouryouTounyuu));

        // 秤量⑤
        this.setItemData(processData, GXHDO102B031Const.HYOURYOU5, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HYOURYOU5, srSlipBinderhyouryouTounyuu));

        // 投入確認⑤
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAKUNIN5, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAKUNIN5, srSlipBinderhyouryouTounyuu));

        // 秤量⑥
        this.setItemData(processData, GXHDO102B031Const.HYOURYOU6, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.HYOURYOU6, srSlipBinderhyouryouTounyuu));

        // 投入確認⑥
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUKAKUNIN6, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUKAKUNIN6, srSlipBinderhyouryouTounyuu));

        // 投入終了日
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUSYUURYOU_DAY, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUSYUURYOU_DAY, srSlipBinderhyouryouTounyuu));

        // 投入終了時間
        this.setItemData(processData, GXHDO102B031Const.TOUNYUUSYUURYOU_TIME, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TOUNYUUSYUURYOU_TIME, srSlipBinderhyouryouTounyuu));

        // ﾊﾞｲﾝﾀﾞｰ添加量合計
        this.setItemData(processData, GXHDO102B031Const.BINDERTENKARYOUGOUKEI, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.BINDERTENKARYOUGOUKEI, srSlipBinderhyouryouTounyuu));

        // 担当者
        this.setItemData(processData, GXHDO102B031Const.TANTOUSYA, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.TANTOUSYA, srSlipBinderhyouryouTounyuu));

        // 備考1
        this.setItemData(processData, GXHDO102B031Const.BIKOU1, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.BIKOU1, srSlipBinderhyouryouTounyuu));

        // 備考2
        this.setItemData(processData, GXHDO102B031Const.BIKOU2, getSrSlipBinderhyouryouTounyuuItemData(GXHDO102B031Const.BIKOU2, srSlipBinderhyouryouTounyuu));

    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipBinderhyouryouTounyuu> getSrSlipBinderhyouryouTounyuuData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSlipBinderhyouryouTounyuu(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipBinderhyouryouTounyuu> loadSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,binderkongousetub,binderkongougoki,kongoutanksyurui,kongoutankno,"
                + "binderkeiryounichiji,binderhinmei,binderlotno,binderkokeibun,binderyuukoukigen,bindertenkaryoukikaku,hoppersiyou,tounyuukaisinichiji,"
                + "tounyuujikaitensuu,hyouryou1,tounyuukakunin1,hyouryou2,tounyuukakunin2,hyouryou3,tounyuukakunin3,hyouryou4,tounyuukakunin4,hyouryou5,"
                + "tounyuukakunin5,hyouryou6,tounyuukakunin6,tounyuusyuuryounichiji,bindertenkaryougoukei,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision"
                + " FROM sr_slip_binderhyouryou_tounyuu "
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
        mapping.put("kojyo", "kojyo");                                      // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                      // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                    // 枝番
        mapping.put("sliphinmei", "sliphinmei");                            // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                              // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                        // 原料記号
        mapping.put("binderkongousetub", "binderkongousetub");              // ﾊﾞｲﾝﾀﾞｰ混合設備
        mapping.put("binderkongougoki", "binderkongougoki");                // ﾊﾞｲﾝﾀﾞｰ混合号機
        mapping.put("kongoutanksyurui", "kongoutanksyurui");                // 混合ﾀﾝｸ種類
        mapping.put("kongoutankno", "kongoutankno");                        // 混合ﾀﾝｸNo
        mapping.put("binderkeiryounichiji", "binderkeiryounichiji");        // ﾊﾞｲﾝﾀﾞｰ秤量日時
        mapping.put("binderhinmei", "binderhinmei");                        // ﾊﾞｲﾝﾀﾞｰ品名
        mapping.put("binderlotno", "binderlotno");                          // ﾊﾞｲﾝﾀﾞｰLotNo
        mapping.put("binderkokeibun", "binderkokeibun");                    // ﾊﾞｲﾝﾀﾞｰ固形分
        mapping.put("binderyuukoukigen", "binderyuukoukigen");              // ﾊﾞｲﾝﾀﾞｰ有効期限
        mapping.put("bindertenkaryoukikaku", "bindertenkaryoukikaku");      // ﾊﾞｲﾝﾀﾞｰ添加量規格
        mapping.put("hoppersiyou", "hoppersiyou");                          // ﾎｯﾊﾟｰ使用
        mapping.put("tounyuukaisinichiji", "tounyuukaisinichiji");          // 投入開始日時
        mapping.put("tounyuujikaitensuu", "tounyuujikaitensuu");            // 投入時回転数
        mapping.put("hyouryou1", "hyouryou1");                              // 秤量①
        mapping.put("tounyuukakunin1", "tounyuukakunin1");                  // 投入確認①
        mapping.put("hyouryou2", "hyouryou2");                              // 秤量②
        mapping.put("tounyuukakunin2", "tounyuukakunin2");                  // 投入確認②
        mapping.put("hyouryou3", "hyouryou3");                              // 秤量③
        mapping.put("tounyuukakunin3", "tounyuukakunin3");                  // 投入確認③
        mapping.put("hyouryou4", "hyouryou4");                              // 秤量④
        mapping.put("tounyuukakunin4", "tounyuukakunin4");                  // 投入確認④
        mapping.put("hyouryou5", "hyouryou5");                              // 秤量⑤
        mapping.put("tounyuukakunin5", "tounyuukakunin5");                  // 投入確認⑤
        mapping.put("hyouryou6", "hyouryou6");                              // 秤量⑥
        mapping.put("tounyuukakunin6", "tounyuukakunin6");                  // 投入確認⑥
        mapping.put("tounyuusyuuryounichiji", "tounyuusyuuryounichiji");    // 投入終了日時
        mapping.put("bindertenkaryougoukei", "bindertenkaryougoukei");      // ﾊﾞｲﾝﾀﾞｰ添加量合計
        mapping.put("tantousya", "tantousya");                              // 担当者
        mapping.put("bikou1", "bikou1");                                    // 備考1
        mapping.put("bikou2", "bikou2");                                    // 備考2
        mapping.put("torokunichiji", "torokunichiji");                      // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                        // 更新日時
        mapping.put("revision", "revision");                                // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipBinderhyouryouTounyuu>> beanHandler = new BeanListHandler<>(SrSlipBinderhyouryouTounyuu.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSlipBinderhyouryouTounyuu> loadTmpSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,binderkongousetub,binderkongougoki,kongoutanksyurui,kongoutankno,"
                + "binderkeiryounichiji,binderhinmei,binderlotno,binderkokeibun,binderyuukoukigen,bindertenkaryoukikaku,hoppersiyou,tounyuukaisinichiji,"
                + "tounyuujikaitensuu,hyouryou1,tounyuukakunin1,hyouryou2,tounyuukakunin2,hyouryou3,tounyuukakunin3,hyouryou4,tounyuukakunin4,hyouryou5,"
                + "tounyuukakunin5,hyouryou6,tounyuukakunin6,tounyuusyuuryounichiji,bindertenkaryougoukei,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_slip_binderhyouryou_tounyuu "
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
        mapping.put("kojyo", "kojyo");                                      // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                      // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                    // 枝番
        mapping.put("sliphinmei", "sliphinmei");                            // ｽﾘｯﾌﾟ品名
        mapping.put("sliplotno", "sliplotno");                              // ｽﾘｯﾌﾟLotNo
        mapping.put("lotkubun", "lotkubun");                                // ﾛｯﾄ区分
        mapping.put("genryoukigou", "genryoukigou");                        // 原料記号
        mapping.put("binderkongousetub", "binderkongousetub");              // ﾊﾞｲﾝﾀﾞｰ混合設備
        mapping.put("binderkongougoki", "binderkongougoki");                // ﾊﾞｲﾝﾀﾞｰ混合号機
        mapping.put("kongoutanksyurui", "kongoutanksyurui");                // 混合ﾀﾝｸ種類
        mapping.put("kongoutankno", "kongoutankno");                        // 混合ﾀﾝｸNo
        mapping.put("binderkeiryounichiji", "binderkeiryounichiji");        // ﾊﾞｲﾝﾀﾞｰ秤量日時
        mapping.put("binderhinmei", "binderhinmei");                        // ﾊﾞｲﾝﾀﾞｰ品名
        mapping.put("binderlotno", "binderlotno");                          // ﾊﾞｲﾝﾀﾞｰLotNo
        mapping.put("binderkokeibun", "binderkokeibun");                    // ﾊﾞｲﾝﾀﾞｰ固形分
        mapping.put("binderyuukoukigen", "binderyuukoukigen");              // ﾊﾞｲﾝﾀﾞｰ有効期限
        mapping.put("bindertenkaryoukikaku", "bindertenkaryoukikaku");      // ﾊﾞｲﾝﾀﾞｰ添加量規格
        mapping.put("hoppersiyou", "hoppersiyou");                          // ﾎｯﾊﾟｰ使用
        mapping.put("tounyuukaisinichiji", "tounyuukaisinichiji");          // 投入開始日時
        mapping.put("tounyuujikaitensuu", "tounyuujikaitensuu");            // 投入時回転数
        mapping.put("hyouryou1", "hyouryou1");                              // 秤量①
        mapping.put("tounyuukakunin1", "tounyuukakunin1");                  // 投入確認①
        mapping.put("hyouryou2", "hyouryou2");                              // 秤量②
        mapping.put("tounyuukakunin2", "tounyuukakunin2");                  // 投入確認②
        mapping.put("hyouryou3", "hyouryou3");                              // 秤量③
        mapping.put("tounyuukakunin3", "tounyuukakunin3");                  // 投入確認③
        mapping.put("hyouryou4", "hyouryou4");                              // 秤量④
        mapping.put("tounyuukakunin4", "tounyuukakunin4");                  // 投入確認④
        mapping.put("hyouryou5", "hyouryou5");                              // 秤量⑤
        mapping.put("tounyuukakunin5", "tounyuukakunin5");                  // 投入確認⑤
        mapping.put("hyouryou6", "hyouryou6");                              // 秤量⑥
        mapping.put("tounyuukakunin6", "tounyuukakunin6");                  // 投入確認⑥
        mapping.put("tounyuusyuuryounichiji", "tounyuusyuuryounichiji");    // 投入終了日時
        mapping.put("bindertenkaryougoukei", "bindertenkaryougoukei");      // ﾊﾞｲﾝﾀﾞｰ添加量合計
        mapping.put("tantousya", "tantousya");                              // 担当者
        mapping.put("bikou1", "bikou1");                                    // 備考1
        mapping.put("bikou2", "bikou2");                                    // 備考2
        mapping.put("torokunichiji", "torokunichiji");                      // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                        // 更新日時
        mapping.put("revision", "revision");                                // revision
        mapping.put("deleteflag", "deleteflag");                            // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSlipBinderhyouryouTounyuu>> beanHandler = new BeanListHandler<>(SrSlipBinderhyouryouTounyuu.class, rowProcessor);

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
     * @param srSlipBinderhyouryouTounyuu ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSlipBinderhyouryouTounyuu != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipBinderhyouryouTounyuuItemData(itemId, srSlipBinderhyouryouTounyuu);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srSlipBinderhyouryouTounyuu != null) {
            // 元データが存在する場合元データより取得
            return getSrSlipBinderhyouryouTounyuuItemData(itemId, srSlipBinderhyouryouTounyuu);
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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録(tmp_sr_slip_binderhyouryou_tounyuu)登録処理
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
    private void insertTmpSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_binderhyouryou_tounyuu ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,binderkongousetub,binderkongougoki,kongoutanksyurui,kongoutankno,"
                + "binderkeiryounichiji,binderhinmei,binderlotno,binderkokeibun,binderyuukoukigen,bindertenkaryoukikaku,hoppersiyou,tounyuukaisinichiji,"
                + "tounyuujikaitensuu,hyouryou1,tounyuukakunin1,hyouryou2,tounyuukakunin2,hyouryou3,tounyuukakunin3,hyouryou4,tounyuukakunin4,hyouryou5,"
                + "tounyuukakunin5,hyouryou6,tounyuukakunin6,tounyuusyuuryounichiji,bindertenkaryougoukei,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSlipBinderhyouryouTounyuu(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録(tmp_sr_slip_binderhyouryou_tounyuu)更新処理
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
    private void updateTmpSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_slip_binderhyouryou_tounyuu SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,binderkongousetub = ?,binderkongougoki = ?,kongoutanksyurui = ?,kongoutankno = ?,"
                + "binderkeiryounichiji = ?,binderhinmei = ?,binderlotno = ?,binderkokeibun = ?,binderyuukoukigen = ?,bindertenkaryoukikaku = ?,hoppersiyou = ?,"
                + "tounyuukaisinichiji = ?,tounyuujikaitensuu = ?,hyouryou1 = ?,tounyuukakunin1 = ?,hyouryou2 = ?,tounyuukakunin2 = ?,hyouryou3 = ?,tounyuukakunin3 = ?,"
                + "hyouryou4 = ?,tounyuukakunin4 = ?,hyouryou5 = ?,tounyuukakunin5 = ?,hyouryou6 = ?,tounyuukakunin6 = ?,tounyuusyuuryounichiji = ?,bindertenkaryougoukei = ?,"
                + "tantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipBinderhyouryouTounyuu> srSlipBinderhyouryouTounyuuList = getSrSlipBinderhyouryouTounyuuData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu = null;
        if (!srSlipBinderhyouryouTounyuuList.isEmpty()) {
            srSlipBinderhyouryouTounyuu = srSlipBinderhyouryouTounyuuList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSlipBinderhyouryouTounyuu(false, newRev, 0, "", "", "", systemTime, processData, srSlipBinderhyouryouTounyuu);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録(tmp_sr_slip_binderhyouryou_tounyuu)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_slip_binderhyouryou_tounyuu "
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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録(tmp_sr_slip_binderhyouryou_tounyuu)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSlipBinderhyouryouTounyuu ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSlipBinderhyouryouTounyuu(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // ﾊﾞｲﾝﾀﾞｰ秤量日時
        String binderkeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B031Const.BINDERKEIRYOU_TIME, srSlipBinderhyouryouTounyuu));
        // 投入開始日時
        String tounyuukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAISI_TIME, srSlipBinderhyouryouTounyuu));
        // 投入終了日時
        String tounyuusyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B031Const.TOUNYUUSYUURYOU_TIME, srSlipBinderhyouryouTounyuu));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.SLIPHINMEI, srSlipBinderhyouryouTounyuu)));                     // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.SLIPLOTNO, srSlipBinderhyouryouTounyuu)));                      // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.LOTKUBUN, srSlipBinderhyouryouTounyuu)));                       // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.GENRYOUKIGOU, srSlipBinderhyouryouTounyuu)));              // 原料記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERKONGOUSETUB, srSlipBinderhyouryouTounyuu)));         // ﾊﾞｲﾝﾀﾞｰ混合設備
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERKONGOUGOKI, srSlipBinderhyouryouTounyuu)));          // ﾊﾞｲﾝﾀﾞｰ混合号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.KONGOUTANKSYURUI, srSlipBinderhyouryouTounyuu)));          // 混合ﾀﾝｸ種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.KONGOUTANKNO, srSlipBinderhyouryouTounyuu)));                      // 混合ﾀﾝｸNo
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.BINDERKEIRYOU_DAY, srSlipBinderhyouryouTounyuu),
                "".equals(binderkeiryouTime) ? "0000" : binderkeiryouTime));                                                                                       // ﾊﾞｲﾝﾀﾞｰ秤量日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.BINDERHINMEI, srSlipBinderhyouryouTounyuu), null));                                 // ﾊﾞｲﾝﾀﾞｰ品名
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERLOTNO, srSlipBinderhyouryouTounyuu)));                  // ﾊﾞｲﾝﾀﾞｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERKOKEIBUN, srSlipBinderhyouryouTounyuu)));            // ﾊﾞｲﾝﾀﾞｰ固形分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERYUUKOUKIGEN, srSlipBinderhyouryouTounyuu)));         // ﾊﾞｲﾝﾀﾞｰ有効期限
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERTENKARYOUKIKAKU, srSlipBinderhyouryouTounyuu)));     // ﾊﾞｲﾝﾀﾞｰ添加量規格
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.HOPPERSIYOU, srSlipBinderhyouryouTounyuu), null));                                  // ﾎｯﾊﾟｰ使用
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAISI_DAY, srSlipBinderhyouryouTounyuu),
                "".equals(tounyuukaisiTime) ? "0000" : tounyuukaisiTime));                                                                                         // 投入開始日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B031Const.TOUNYUUJIKAITENSUU, srSlipBinderhyouryouTounyuu)));           // 投入時回転数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.HYOURYOU1, srSlipBinderhyouryouTounyuu)));                         // 秤量①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN1, srSlipBinderhyouryouTounyuu), null));                              // 投入確認①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.HYOURYOU2, srSlipBinderhyouryouTounyuu)));                         // 秤量②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN2, srSlipBinderhyouryouTounyuu), null));                              // 投入確認②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.HYOURYOU3, srSlipBinderhyouryouTounyuu)));                         // 秤量③
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN3, srSlipBinderhyouryouTounyuu), null));                              // 投入確認③
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.HYOURYOU4, srSlipBinderhyouryouTounyuu)));                         // 秤量④
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN4, srSlipBinderhyouryouTounyuu), null));                              // 投入確認④
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.HYOURYOU5, srSlipBinderhyouryouTounyuu)));                         // 秤量⑤
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN5, srSlipBinderhyouryouTounyuu), null));                              // 投入確認⑤
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.HYOURYOU6, srSlipBinderhyouryouTounyuu)));                         // 秤量⑥
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN6, srSlipBinderhyouryouTounyuu), null));                              // 投入確認⑥
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.TOUNYUUSYUURYOU_DAY, srSlipBinderhyouryouTounyuu),
                "".equals(tounyuusyuuryouTime) ? "0000" : tounyuusyuuryouTime));                                                                                   // 投入終了日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.BINDERTENKARYOUGOUKEI, srSlipBinderhyouryouTounyuu)));             // ﾊﾞｲﾝﾀﾞｰ添加量合計
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.TANTOUSYA, srSlipBinderhyouryouTounyuu)));                      // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.BIKOU1, srSlipBinderhyouryouTounyuu)));                         // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B031Const.BIKOU2, srSlipBinderhyouryouTounyuu)));                         // 備考2

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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入(sr_slip_binderhyouryou_tounyuu)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrSlipBinderhyouryouTounyuu 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrSlipBinderhyouryouTounyuu tmpSrSlipBinderhyouryouTounyuu) throws SQLException {

        String sql = "INSERT INTO sr_slip_binderhyouryou_tounyuu ( "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,binderkongousetub,binderkongougoki,kongoutanksyurui,kongoutankno,"
                + "binderkeiryounichiji,binderhinmei,binderlotno,binderkokeibun,binderyuukoukigen,bindertenkaryoukikaku,hoppersiyou,tounyuukaisinichiji,"
                + "tounyuujikaitensuu,hyouryou1,tounyuukakunin1,hyouryou2,tounyuukakunin2,hyouryou3,tounyuukakunin3,hyouryou4,tounyuukakunin4,hyouryou5,"
                + "tounyuukakunin5,hyouryou6,tounyuukakunin6,tounyuusyuuryounichiji,bindertenkaryougoukei,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSlipBinderhyouryouTounyuu(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrSlipBinderhyouryouTounyuu);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入(sr_slip_binderhyouryou_tounyuu)更新処理
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
    private void updateSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {
        String sql = "UPDATE sr_slip_binderhyouryou_tounyuu SET "
                + " sliphinmei = ?,sliplotno = ?,lotkubun = ?,genryoukigou = ?,binderkongousetub = ?,binderkongougoki = ?,kongoutanksyurui = ?,kongoutankno = ?,"
                + "binderkeiryounichiji = ?,binderhinmei = ?,binderlotno = ?,binderkokeibun = ?,binderyuukoukigen = ?,bindertenkaryoukikaku = ?,hoppersiyou = ?,"
                + "tounyuukaisinichiji = ?,tounyuujikaitensuu = ?,hyouryou1 = ?,tounyuukakunin1 = ?,hyouryou2 = ?,tounyuukakunin2 = ?,hyouryou3 = ?,tounyuukakunin3 = ?,"
                + "hyouryou4 = ?,tounyuukakunin4 = ?,hyouryou5 = ?,tounyuukakunin5 = ?,hyouryou6 = ?,tounyuukakunin6 = ?,tounyuusyuuryounichiji = ?,bindertenkaryougoukei = ?,"
                + "tantousya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrSlipBinderhyouryouTounyuu> srSlipBinderhyouryouTounyuuList = getSrSlipBinderhyouryouTounyuuData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu = null;
        if (!srSlipBinderhyouryouTounyuuList.isEmpty()) {
            srSlipBinderhyouryouTounyuu = srSlipBinderhyouryouTounyuuList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSlipBinderhyouryouTounyuu(false, newRev, "", "", "", systemTime, processData, srSlipBinderhyouryouTounyuu);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入(sr_slip_binderhyouryou_tounyuu)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srSlipBinderhyouryouTounyuu ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSlipBinderhyouryouTounyuu(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // ﾊﾞｲﾝﾀﾞｰ秤量日時
        String binderkeiryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B031Const.BINDERKEIRYOU_TIME, srSlipBinderhyouryouTounyuu));
        // 投入開始日時
        String tounyuukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAISI_TIME, srSlipBinderhyouryouTounyuu));
        // 投入終了日時
        String tounyuusyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B031Const.TOUNYUUSYUURYOU_TIME, srSlipBinderhyouryouTounyuu));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B031Const.SLIPHINMEI, srSlipBinderhyouryouTounyuu)));                     // ｽﾘｯﾌﾟ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B031Const.SLIPLOTNO, srSlipBinderhyouryouTounyuu)));                      // ｽﾘｯﾌﾟLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B031Const.LOTKUBUN, srSlipBinderhyouryouTounyuu)));                       // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.GENRYOUKIGOU, srSlipBinderhyouryouTounyuu)));              // 原料記号
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERKONGOUSETUB, srSlipBinderhyouryouTounyuu)));         // ﾊﾞｲﾝﾀﾞｰ混合設備
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERKONGOUGOKI, srSlipBinderhyouryouTounyuu)));          // ﾊﾞｲﾝﾀﾞｰ混合号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.KONGOUTANKSYURUI, srSlipBinderhyouryouTounyuu)));          // 混合ﾀﾝｸ種類
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.KONGOUTANKNO, srSlipBinderhyouryouTounyuu)));                      // 混合ﾀﾝｸNo
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B031Const.BINDERKEIRYOU_DAY, srSlipBinderhyouryouTounyuu),
                "".equals(binderkeiryouTime) ? "0000" : binderkeiryouTime));                                                                            // ﾊﾞｲﾝﾀﾞｰ秤量日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.BINDERHINMEI, srSlipBinderhyouryouTounyuu), 9));                         // ﾊﾞｲﾝﾀﾞｰ品名
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERLOTNO, srSlipBinderhyouryouTounyuu)));                  // ﾊﾞｲﾝﾀﾞｰLotNo
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERKOKEIBUN, srSlipBinderhyouryouTounyuu)));            // ﾊﾞｲﾝﾀﾞｰ固形分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERYUUKOUKIGEN, srSlipBinderhyouryouTounyuu)));         // ﾊﾞｲﾝﾀﾞｰ有効期限
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B031Const.BINDERTENKARYOUKIKAKU, srSlipBinderhyouryouTounyuu)));     // ﾊﾞｲﾝﾀﾞｰ添加量規格
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.HOPPERSIYOU, srSlipBinderhyouryouTounyuu), 9));                          // ﾎｯﾊﾟｰ使用
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAISI_DAY, srSlipBinderhyouryouTounyuu),
                "".equals(tounyuukaisiTime) ? "0000" : tounyuukaisiTime));                                                                              // 投入開始日時
        params.add(DBUtil.stringToIntObject(getItemKikakuchi(pItemList, GXHDO102B031Const.TOUNYUUJIKAITENSUU, srSlipBinderhyouryouTounyuu)));           // 投入時回転数
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.HYOURYOU1, srSlipBinderhyouryouTounyuu)));                         // 秤量①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN1, srSlipBinderhyouryouTounyuu), 9));                      // 投入確認①
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.HYOURYOU2, srSlipBinderhyouryouTounyuu)));                         // 秤量②
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN2, srSlipBinderhyouryouTounyuu), 9));                      // 投入確認②
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.HYOURYOU3, srSlipBinderhyouryouTounyuu)));                         // 秤量③
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN3, srSlipBinderhyouryouTounyuu), 9));                      // 投入確認③
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.HYOURYOU4, srSlipBinderhyouryouTounyuu)));                         // 秤量④
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN4, srSlipBinderhyouryouTounyuu), 9));                      // 投入確認④
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.HYOURYOU5, srSlipBinderhyouryouTounyuu)));                         // 秤量⑤
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN5, srSlipBinderhyouryouTounyuu), 9));                      // 投入確認⑤
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.HYOURYOU6, srSlipBinderhyouryouTounyuu)));                         // 秤量⑥
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B031Const.TOUNYUUKAKUNIN6, srSlipBinderhyouryouTounyuu), 9));                      // 投入確認⑥
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B031Const.TOUNYUUSYUURYOU_DAY, srSlipBinderhyouryouTounyuu),
                "".equals(tounyuusyuuryouTime) ? "0000" : tounyuusyuuryouTime));                                                                        // 投入終了日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B031Const.BINDERTENKARYOUGOUKEI, srSlipBinderhyouryouTounyuu)));             // ﾊﾞｲﾝﾀﾞｰ添加量合計
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B031Const.TANTOUSYA, srSlipBinderhyouryouTounyuu)));                      // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B031Const.BIKOU1, srSlipBinderhyouryouTounyuu)));                         // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B031Const.BIKOU2, srSlipBinderhyouryouTounyuu)));                         // 備考2

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
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入(sr_slip_binderhyouryou_tounyuu)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_slip_binderhyouryou_tounyuu "
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
     * [ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_slip_binderhyouryou_tounyuu "
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
     * @param srSlipBinderhyouryouTounyuu ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入データ
     * @return DB値
     */
    private String getSrSlipBinderhyouryouTounyuuItemData(String itemId, SrSlipBinderhyouryouTounyuu srSlipBinderhyouryouTounyuu) {
        switch (itemId) {
            // ｽﾘｯﾌﾟ品名
            case GXHDO102B031Const.SLIPHINMEI:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getSliphinmei());

            // ｽﾘｯﾌﾟLotNo
            case GXHDO102B031Const.SLIPLOTNO:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getSliplotno());

            // ﾛｯﾄ区分
            case GXHDO102B031Const.LOTKUBUN:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getLotkubun());

            // 原料記号
            case GXHDO102B031Const.GENRYOUKIGOU:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getGenryoukigou());

            // ﾊﾞｲﾝﾀﾞｰ混合設備
            case GXHDO102B031Const.BINDERKONGOUSETUB:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBinderkongousetub());

            // ﾊﾞｲﾝﾀﾞｰ混合号機
            case GXHDO102B031Const.BINDERKONGOUGOKI:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBinderkongougoki());

            // 混合ﾀﾝｸ種類
            case GXHDO102B031Const.KONGOUTANKSYURUI:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getKongoutanksyurui());

            // 混合ﾀﾝｸNo
            case GXHDO102B031Const.KONGOUTANKNO:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getKongoutankno());

            // ﾊﾞｲﾝﾀﾞｰ秤量日
            case GXHDO102B031Const.BINDERKEIRYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipBinderhyouryouTounyuu.getBinderkeiryounichiji(), "yyMMdd");

            // ﾊﾞｲﾝﾀﾞｰ秤量時間
            case GXHDO102B031Const.BINDERKEIRYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipBinderhyouryouTounyuu.getBinderkeiryounichiji(), "HHmm");

            // ﾊﾞｲﾝﾀﾞｰ品名
            case GXHDO102B031Const.BINDERHINMEI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBinderhinmei()));

            // ﾊﾞｲﾝﾀﾞｰLotNo
            case GXHDO102B031Const.BINDERLOTNO:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBinderlotno());

            // ﾊﾞｲﾝﾀﾞｰ固形分
            case GXHDO102B031Const.BINDERKOKEIBUN:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBinderkokeibun());

            // ﾊﾞｲﾝﾀﾞｰ有効期限
            case GXHDO102B031Const.BINDERYUUKOUKIGEN:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBinderyuukoukigen());

            // ﾊﾞｲﾝﾀﾞｰ添加量規格
            case GXHDO102B031Const.BINDERTENKARYOUKIKAKU:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBindertenkaryoukikaku());

            // ﾎｯﾊﾟｰ使用
            case GXHDO102B031Const.HOPPERSIYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHoppersiyou()));

            // 投入開始日
            case GXHDO102B031Const.TOUNYUUKAISI_DAY:
                return DateUtil.formattedTimestamp(srSlipBinderhyouryouTounyuu.getTounyuukaisinichiji(), "yyMMdd");

            // 投入開始時間
            case GXHDO102B031Const.TOUNYUUKAISI_TIME:
                return DateUtil.formattedTimestamp(srSlipBinderhyouryouTounyuu.getTounyuukaisinichiji(), "HHmm");

            // 投入時回転数
            case GXHDO102B031Const.TOUNYUUJIKAITENSUU:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuujikaitensuu());

            // 秤量①
            case GXHDO102B031Const.HYOURYOU1:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHyouryou1());

            // 投入確認①
            case GXHDO102B031Const.TOUNYUUKAKUNIN1:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuukakunin1()));

            // 秤量②
            case GXHDO102B031Const.HYOURYOU2:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHyouryou2());

            // 投入確認②
            case GXHDO102B031Const.TOUNYUUKAKUNIN2:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuukakunin2()));

            // 秤量③
            case GXHDO102B031Const.HYOURYOU3:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHyouryou3());

            // 投入確認③
            case GXHDO102B031Const.TOUNYUUKAKUNIN3:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuukakunin3()));

            // 秤量④
            case GXHDO102B031Const.HYOURYOU4:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHyouryou4());

            // 投入確認④
            case GXHDO102B031Const.TOUNYUUKAKUNIN4:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuukakunin4()));

            // 秤量⑤
            case GXHDO102B031Const.HYOURYOU5:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHyouryou5());

            // 投入確認⑤
            case GXHDO102B031Const.TOUNYUUKAKUNIN5:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuukakunin5()));

            // 秤量⑥
            case GXHDO102B031Const.HYOURYOU6:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getHyouryou6());

            // 投入確認⑥
            case GXHDO102B031Const.TOUNYUUKAKUNIN6:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTounyuukakunin6()));

            // 投入終了日
            case GXHDO102B031Const.TOUNYUUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srSlipBinderhyouryouTounyuu.getTounyuusyuuryounichiji(), "yyMMdd");

            // 投入終了時間
            case GXHDO102B031Const.TOUNYUUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srSlipBinderhyouryouTounyuu.getTounyuusyuuryounichiji(), "HHmm");

            // ﾊﾞｲﾝﾀﾞｰ添加量合計
            case GXHDO102B031Const.BINDERTENKARYOUGOUKEI:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBindertenkaryougoukei());

            // 担当者
            case GXHDO102B031Const.TANTOUSYA:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getTantousya());

            // 備考1
            case GXHDO102B031Const.BIKOU1:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBikou1());

            // 備考2
            case GXHDO102B031Const.BIKOU2:
                return StringUtil.nullToBlank(srSlipBinderhyouryouTounyuu.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｽﾘｯﾌﾟ作製・ﾊﾞｲﾝﾀﾞｰ秤量・投入_仮登録(tmp_sr_slip_binderhyouryou_tounyuu)登録処理(削除時)
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
    private void insertDeleteDataTmpSrSlipBinderhyouryouTounyuu(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_slip_binderhyouryou_tounyuu ("
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,binderkongousetub,binderkongougoki,kongoutanksyurui,kongoutankno,"
                + "binderkeiryounichiji,binderhinmei,binderlotno,binderkokeibun,binderyuukoukigen,bindertenkaryoukikaku,hoppersiyou,tounyuukaisinichiji,"
                + "tounyuujikaitensuu,hyouryou1,tounyuukakunin1,hyouryou2,tounyuukakunin2,hyouryou3,tounyuukakunin3,hyouryou4,tounyuukakunin4,hyouryou5,"
                + "tounyuukakunin5,hyouryou6,tounyuukakunin6,tounyuusyuuryounichiji,bindertenkaryougoukei,tantousya,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,sliphinmei,sliplotno,lotkubun,genryoukigou,binderkongousetub,binderkongougoki,kongoutanksyurui,kongoutankno,"
                + "binderkeiryounichiji,binderhinmei,binderlotno,binderkokeibun,binderyuukoukigen,bindertenkaryoukikaku,hoppersiyou,tounyuukaisinichiji,"
                + "tounyuujikaitensuu,hyouryou1,tounyuukakunin1,hyouryou2,tounyuukakunin2,hyouryou3,tounyuukakunin3,hyouryou4,tounyuukakunin4,hyouryou5,"
                + "tounyuukakunin5,hyouryou6,tounyuukakunin6,tounyuusyuuryounichiji,bindertenkaryougoukei,tantousya,bikou1,bikou2,?,?,?,? "
                + " FROM sr_slip_binderhyouryou_tounyuu "
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
    private void initGXHDO102B031A(ProcessData processData) {
        GXHDO102B031A bean = (GXHDO102B031A) getFormBean("gXHDO102B031A");
        bean.setWiplotno(getItemRow(processData.getItemList(), GXHDO102B031Const.WIPLOTNO));
        bean.setSliphinmei(getItemRow(processData.getItemList(), GXHDO102B031Const.SLIPHINMEI));
        bean.setSliplotno(getItemRow(processData.getItemList(), GXHDO102B031Const.SLIPLOTNO));
        bean.setLotkubun(getItemRow(processData.getItemList(), GXHDO102B031Const.LOTKUBUN));
        bean.setGenryoukigou(getItemRow(processData.getItemList(), GXHDO102B031Const.GENRYOUKIGOU));
        bean.setBinderkongousetub(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKONGOUSETUB));
        bean.setBinderkongougoki(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKONGOUGOKI));
        bean.setKongoutanksyurui(getItemRow(processData.getItemList(), GXHDO102B031Const.KONGOUTANKSYURUI));
        bean.setKongoutankno(getItemRow(processData.getItemList(), GXHDO102B031Const.KONGOUTANKNO));
        bean.setBinderkeiryou_day(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKEIRYOU_DAY));
        bean.setBinderkeiryou_time(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKEIRYOU_TIME));
        bean.setBinderhinmei(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERHINMEI));
        bean.setBinderlotno(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERLOTNO));
        bean.setBinderkokeibun(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERKOKEIBUN));
        bean.setBinderyuukoukigen(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERYUUKOUKIGEN));
        bean.setBindertenkaryoukikaku(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERTENKARYOUKIKAKU));
        bean.setHoppersiyou(getItemRow(processData.getItemList(), GXHDO102B031Const.HOPPERSIYOU));
        bean.setTounyuukaisi_day(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAISI_DAY));
        bean.setTounyuukaisi_time(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAISI_TIME));
        bean.setTounyuujikaitensuu(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUJIKAITENSUU));
        bean.setHyouryou1(getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU1));
        bean.setTounyuukakunin1(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAKUNIN1));
        bean.setHyouryou2(getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU2));
        bean.setTounyuukakunin2(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAKUNIN2));
        bean.setHyouryou3(getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU3));
        bean.setTounyuukakunin3(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAKUNIN3));
        bean.setHyouryou4(getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU4));
        bean.setTounyuukakunin4(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAKUNIN4));
        bean.setHyouryou5(getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU5));
        bean.setTounyuukakunin5(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAKUNIN5));
        bean.setHyouryou6(getItemRow(processData.getItemList(), GXHDO102B031Const.HYOURYOU6));
        bean.setTounyuukakunin6(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUKAKUNIN6));
        bean.setTounyuusyuuryou_day(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUSYUURYOU_DAY));
        bean.setTounyuusyuuryou_time(getItemRow(processData.getItemList(), GXHDO102B031Const.TOUNYUUSYUURYOU_TIME));
        bean.setBindertenkaryougoukei(getItemRow(processData.getItemList(), GXHDO102B031Const.BINDERTENKARYOUGOUKEI));
        bean.setTantousya(getItemRow(processData.getItemList(), GXHDO102B031Const.TANTOUSYA));
        bean.setBikou1(getItemRow(processData.getItemList(), GXHDO102B031Const.BIKOU1));
        bean.setBikou2(getItemRow(processData.getItemList(), GXHDO102B031Const.BIKOU2));

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
        allItemIdMap.put(GXHDO102B031Const.WIPLOTNO, "WIPﾛｯﾄNo");
        allItemIdMap.put(GXHDO102B031Const.SLIPHINMEI, "ｽﾘｯﾌﾟ品名");
        allItemIdMap.put(GXHDO102B031Const.SLIPLOTNO, "ｽﾘｯﾌﾟLotNo");
        allItemIdMap.put(GXHDO102B031Const.LOTKUBUN, "ﾛｯﾄ区分");
        allItemIdMap.put(GXHDO102B031Const.GENRYOUKIGOU, "原料記号");
        allItemIdMap.put(GXHDO102B031Const.BINDERKONGOUSETUB, "ﾊﾞｲﾝﾀﾞｰ混合設備");
        allItemIdMap.put(GXHDO102B031Const.BINDERKONGOUGOKI, "ﾊﾞｲﾝﾀﾞｰ混合号機");
        allItemIdMap.put(GXHDO102B031Const.KONGOUTANKSYURUI, "混合ﾀﾝｸ種類");
        allItemIdMap.put(GXHDO102B031Const.KONGOUTANKNO, "混合ﾀﾝｸNo");
        allItemIdMap.put(GXHDO102B031Const.BINDERKEIRYOU_DAY, "ﾊﾞｲﾝﾀﾞｰ秤量日");
        allItemIdMap.put(GXHDO102B031Const.BINDERKEIRYOU_TIME, "ﾊﾞｲﾝﾀﾞｰ秤量時間");
        allItemIdMap.put(GXHDO102B031Const.BINDERHINMEI, "ﾊﾞｲﾝﾀﾞｰ品名");
        allItemIdMap.put(GXHDO102B031Const.BINDERLOTNO, "ﾊﾞｲﾝﾀﾞｰLotNo");
        allItemIdMap.put(GXHDO102B031Const.BINDERKOKEIBUN, "ﾊﾞｲﾝﾀﾞｰ固形分");
        allItemIdMap.put(GXHDO102B031Const.BINDERYUUKOUKIGEN, "ﾊﾞｲﾝﾀﾞｰ有効期限");
        allItemIdMap.put(GXHDO102B031Const.BINDERTENKARYOUKIKAKU, "ﾊﾞｲﾝﾀﾞｰ添加量規格");
        allItemIdMap.put(GXHDO102B031Const.HOPPERSIYOU, "ﾎｯﾊﾟｰ使用");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAISI_DAY, "投入開始日");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAISI_TIME, "投入開始時間");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUJIKAITENSUU, "投入時回転数");
        allItemIdMap.put(GXHDO102B031Const.HYOURYOU1, "秤量①");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAKUNIN1, "投入確認①");
        allItemIdMap.put(GXHDO102B031Const.HYOURYOU2, "秤量②");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAKUNIN2, "投入確認②");
        allItemIdMap.put(GXHDO102B031Const.HYOURYOU3, "秤量③");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAKUNIN3, "投入確認③");
        allItemIdMap.put(GXHDO102B031Const.HYOURYOU4, "秤量④");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAKUNIN4, "投入確認④");
        allItemIdMap.put(GXHDO102B031Const.HYOURYOU5, "秤量⑤");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAKUNIN5, "投入確認⑤");
        allItemIdMap.put(GXHDO102B031Const.HYOURYOU6, "秤量⑥");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUKAKUNIN6, "投入確認⑥");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUSYUURYOU_DAY, "投入終了日");
        allItemIdMap.put(GXHDO102B031Const.TOUNYUUSYUURYOU_TIME, "投入終了時間");
        allItemIdMap.put(GXHDO102B031Const.BINDERTENKARYOUGOUKEI, "ﾊﾞｲﾝﾀﾞｰ添加量合計");
        allItemIdMap.put(GXHDO102B031Const.TANTOUSYA, "担当者");
        allItemIdMap.put(GXHDO102B031Const.BIKOU1, "備考1");
        allItemIdMap.put(GXHDO102B031Const.BIKOU2, "備考2");

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
