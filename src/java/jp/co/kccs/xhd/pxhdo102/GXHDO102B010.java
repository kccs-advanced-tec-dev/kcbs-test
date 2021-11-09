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
import jp.co.kccs.xhd.db.model.FXHDD02;
import jp.co.kccs.xhd.db.model.SikakariJson;
import jp.co.kccs.xhd.db.model.SrTenkaPremixing;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901B;
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
 * GXHDO102B010(添加材ｽﾗﾘｰ作製・予備混合)
 *
 * @author KCSS K.Jo
 * @since 2021/10/22
 */
public class GXHDO102B010 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B010.class.getName());
    // 仮登録フラグ
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    // 登録済フラグ
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    // 削除フラグ
    private static final String JOTAI_FLG_SAKUJO = "9";
    // エラー背景色
    private static final String SQL_STATE_RECORD_LOCK_ERR = "55P03";

    /**
     * コンストラクタ
     */
    public GXHDO102B010() {
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
                    GXHDO102B010Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_TOP,
                    GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_TOP,
                    GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_TOP,
                    GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_TOP,
                    GXHDO102B010Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_BOTTOM,
                    GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_BOTTOM,
                    GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_BOTTOM,
                    GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B010Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B010Const.BTN_INSERT_TOP,
                    GXHDO102B010Const.BTN_DELETE_TOP,
                    GXHDO102B010Const.BTN_UPDATE_TOP,
                    GXHDO102B010Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B010Const.BTN_INSERT_BOTTOM,
                    GXHDO102B010Const.BTN_DELETE_BOTTOM,
                    GXHDO102B010Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B010Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B010Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B010Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B010Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B010Const.BTN_INSERT_TOP:
            case GXHDO102B010Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B010Const.BTN_UPDATE_TOP:
            case GXHDO102B010Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B010Const.BTN_DELETE_TOP:
            case GXHDO102B010Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 撹拌開始日時
            case GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_TOP:
            case GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_BOTTOM:
                method = "setKakuhankaisinichiji";
                break;
            // 撹拌終了日時
            case GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_TOP:
            case GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_BOTTOM:
                method = "setKakuhansyuryonichiji";
                break;
            // 途中撹拌開始日時
            case GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_TOP:
            case GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_BOTTOM:
                method = "setTotyukakuhankaisinichiji";
                break;
            // 途中撹拌終了日時
            case GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_TOP:
            case GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_BOTTOM:
                method = "setTotyukakuhansyuryonichiji";
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

            // (6)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
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

            // 添加材ｽﾗﾘｰ作製・予備混合の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrTenkaPremixing> srTenkaGlassDataList = getSrTenkaPremixingData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srTenkaGlassDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTenkaGlassDataList.get(0));

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
     * 撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhankaisinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 撹拌終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKakuhansyuryonichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANSYURYO_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANSYURYO_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 途中撹拌開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTotyukakuhankaisinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 途中撹拌終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTotyukakuhansyuryonichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
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
                // 原材料品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, jissekiNo);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 添加材ｽﾗﾘｰ作製・予備混合_仮登録登録処理
                insertTmpSrTenkaPremixing(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData, formId);
            } else {

                // 添加材ｽﾗﾘｰ作製・予備混合_仮登録更新処理
                updateTmpSrTenkaPremixing(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
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
        processData.setMethod("doResist");

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

        // 途中撹拌時間ﾁｪｯｸ
        List<String> totyukakuhankaisiList = Arrays.asList(GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY, GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME);
        List<String> totyukakuhansyuryoList = Arrays.asList(GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY, GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME);

        // 撹拌時間ﾁｪｯｸ
        List<String> kakuhankaisiList = Arrays.asList(GXHDO102B010Const.KAKUHANKAISI_DAY, GXHDO102B010Const.KAKUHANKAISI_TIME);
        List<String> kakuhansyuryo1List = Arrays.asList(GXHDO102B010Const.KAKUHANSYURYO_DAY, GXHDO102B010Const.KAKUHANSYURYO_TIME);

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // 途中撹拌時間の時間差数
        FXHDD01 totyukakuhanjikanDiffHours = getDiffMinutes(processData, GXHDO102B010Const.TOTYUKAKUHANJIKAN, totyukakuhankaisiList, totyukakuhansyuryoList);
        // 撹拌時間の時間差数
        FXHDD01 kakuhanjikandiffHours = getDiffMinutes(processData, GXHDO102B010Const.KAKUHANJIKAN, kakuhankaisiList, kakuhansyuryo1List);
        String totyukakuhanjikan = "途中撹拌時間";
        String kakuhanjikan = "撹拌時間";
        // 途中撹拌時間項目の項目名を設置
        if (totyukakuhanjikanDiffHours != null) {
            totyukakuhanjikanDiffHours.setLabel1(totyukakuhanjikan);
            itemList.add(totyukakuhanjikanDiffHours);
        }
        // 撹拌時間項目の項目名を設置
        if (kakuhanjikandiffHours != null) {
            kakuhanjikandiffHours.setLabel1(kakuhanjikan);
            itemList.add(kakuhanjikandiffHours);
        }
        processData.getItemList().stream().filter(
                (fxhdd01) -> !(StringUtil.isEmpty(fxhdd01.getStandardPattern()) || "【-】".equals(fxhdd01.getKikakuChi()))).filter(
                        (fxhdd01) -> !(!ValidateUtil.isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue()))).forEachOrdered(
                        (fxhdd01) -> {
                            // 規格チェックの対象項目である、かつ入力項目かつ入力値がある項目はリストに追加
                            itemList.add(fxhdd01);
                        });
        ErrorMessageInfo errorMessageInfo = ValidateUtil.checkInputKikakuchi(itemList, kikakuchiInputErrorInfoList);

        // エラー項目の背景色を設定
        setItemBackColor(processData, totyukakuhankaisiList, totyukakuhansyuryoList, totyukakuhanjikan, kikakuchiInputErrorInfoList);
        setItemBackColor(processData, kakuhankaisiList, kakuhansyuryo1List, kakuhanjikan, kikakuchiInputErrorInfoList);

        return errorMessageInfo;
    }

    /**
     * 取得時間差数は、設定した規格値と比較
     *
     * @param processData 処理データ
     * @param kikakuItem 規格値項目名
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     */
    private FXHDD01 getDiffMinutes(ProcessData processData, String kikakuItem, List<String> kakuhankaisiList, List<String> kakuhansyuuryouList) {

        FXHDD01 kikakuFxhdd01 = getItemRow(processData.getItemList(), kikakuItem);
        if (kikakuFxhdd01 == null) {
            return null;
        }
        int diffHours;
        Date kaishijikan = null;
        Date syuuryoujikan = null;
        FXHDD01 itemFxhdd01Clone = null;
        try {
            // 開始日
            FXHDD01 itemKakuhankaisiDay = getItemRow(processData.getItemList(), kakuhankaisiList.get(0));
            // 開始時間
            FXHDD01 itemKakuhankaisiTime = getItemRow(processData.getItemList(), kakuhankaisiList.get(1));
            // 終了日
            FXHDD01 itemKakuhansyuuryouDay = getItemRow(processData.getItemList(), kakuhansyuuryouList.get(0));
            // 終了時間
            FXHDD01 itemKakuhansyuuryouTime = getItemRow(processData.getItemList(), kakuhansyuuryouList.get(1));
            if (DateUtil.isValidYYMMDD(itemKakuhankaisiDay.getValue()) && DateUtil.isValidHHMM(itemKakuhankaisiTime.getValue())) {
                kaishijikan = DateUtil.convertStringToDate(itemKakuhankaisiDay.getValue(), itemKakuhankaisiTime.getValue());
            }
            if (DateUtil.isValidYYMMDD(itemKakuhansyuuryouDay.getValue()) && DateUtil.isValidHHMM(itemKakuhansyuuryouTime.getValue())) {
                syuuryoujikan = DateUtil.convertStringToDate(itemKakuhansyuuryouDay.getValue(), itemKakuhansyuuryouTime.getValue());
            }
            if (kaishijikan == null || syuuryoujikan == null) {
                return null;
            }
            // 時間を利用するため、該当項目をCloneする
            if (itemFxhdd01Clone == null) {
                itemFxhdd01Clone = kikakuFxhdd01.clone();
            }
            // 日付の差分分数の数取得処理
            diffHours = DateUtil.diffMinutes(kaishijikan, syuuryoujikan);
        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return null;
        }
        // 項目の規格値を設置
        itemFxhdd01Clone.setKikakuChi(kikakuFxhdd01.getKikakuChi());
        itemFxhdd01Clone.setStandardPattern(kikakuFxhdd01.getStandardPattern());
        itemFxhdd01Clone.setValue(String.valueOf(diffHours));
        return itemFxhdd01Clone;
    }

    /**
     * エラー項目の背景色を設定
     *
     * @param processData 処理データ
     * @param kakuhankaisiList 開始時間項目リスト
     * @param kakuhansyuuryouList 終了時間項目リスト
     * @param label 項目の項目名
     * @param kikakuchiInputErrorInfoList 規格値入力エラー情報リスト
     */
    private void setItemBackColor(ProcessData processData, List<String> kakuhankaisiList, List<String> kakuhansyuuryouList, String label, List<KikakuchiInputErrorInfo> kikakuchiInputErrorInfoList) {

        List<String> errorTyougouryouList = new ArrayList<>();
        // エラー項目の背景色を設定
        List<String> allTyougouryouList = new ArrayList<>();
        allTyougouryouList.addAll(kakuhankaisiList);
        allTyougouryouList.addAll(kakuhansyuuryouList);

        kikakuchiInputErrorInfoList.stream().forEachOrdered((errorInfo) -> {
            errorTyougouryouList.add(errorInfo.getItemLabel());
        });
        if (errorTyougouryouList.contains(label)) {
            FXHDD01 item1Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(0));
            FXHDD01 item2Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(1));
            FXHDD01 item3Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(2));
            FXHDD01 item4Fxhdd01 = getItemRow(processData.getItemList(), allTyougouryouList.get(3));
            item1Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            item2Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            item3Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
            item4Fxhdd01.setBackColorInput(ErrUtil.ERR_BACK_COLOR);
        }
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
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
                // 原材料品質DB登録実績登録処理
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrTenkaPremixing tmpSrTenkaPremixing = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTenkaPremixing> srTenkaPremixingList = getSrTenkaPremixingData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srTenkaPremixingList.isEmpty()) {
                    tmpSrTenkaPremixing = srTenkaPremixingList.get(0);
                }

                deleteTmpSrTenkaPremixing(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 添加材ｽﾗﾘｰ作製・予備混合_登録処理
            insertSrTenkaPremixing(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaPremixing);

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
        processData.setUserAuthParam(GXHDO102B010Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo9, edaban, jissekiNo, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);

            // 添加材ｽﾗﾘｰ作製・予備混合_更新処理
            updateSrTenkaPremixing(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

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
     * 登録・修正項目チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkItemRegistCorrect(ProcessData processData) {

        ValidateUtil validateUtil = new ValidateUtil();
        FXHDD01 itemTotyukakuhankaisiDay = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY);  // 途中撹拌開始日
        FXHDD01 itemTotyukakuhankaisiTime = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME); // 途中攪拌開始時間
        if (itemTotyukakuhankaisiDay != null && itemTotyukakuhankaisiTime != null) {
            // 画面.途中撹拌開始日 + 画面.途中撹拌開始時間
            Date totyukakuhankaisiDate = DateUtil.convertStringToDate(itemTotyukakuhankaisiDay.getValue(), itemTotyukakuhankaisiTime.getValue());
            FXHDD01 itemTotyukakuhansyuryoDay = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY);    // 途中撹拌終了日
            FXHDD01 itemTotyukakuhansyuryoTime = getItemRow(processData.getItemList(), GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME); // 途中撹拌終了時間
            // 画面.撹拌終了日 + 画面.撹拌終了時間
            Date totyukakuhansyuryoDate = DateUtil.convertStringToDate(itemTotyukakuhansyuryoDay.getValue(), itemTotyukakuhansyuryoTime.getValue());
            // R001チェック呼出し
            String msgTotyukakuhansyuryoCheckR001 = validateUtil.checkR001(itemTotyukakuhankaisiDay.getLabel1(), totyukakuhankaisiDate, itemTotyukakuhansyuryoDay.getLabel1(), totyukakuhansyuryoDate);
            if (!StringUtil.isEmpty(msgTotyukakuhansyuryoCheckR001)) {
                // エラー発生時
                List<FXHDD01> errFxhdd01List = Arrays.asList(itemTotyukakuhankaisiDay, itemTotyukakuhankaisiTime, itemTotyukakuhansyuryoDay, itemTotyukakuhansyuryoTime);
                return MessageUtil.getErrorMessageInfo("", msgTotyukakuhansyuryoCheckR001, true, true, errFxhdd01List);
            }
        }

        FXHDD01 itemKakuhankaisiDay = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANKAISI_DAY);  // 撹拌開始日
        FXHDD01 itemKakuhankaisiTime = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANKAISI_TIME); // 撹拌開始時間
        // 画面.撹拌開始日 + 画面.撹拌開始時間
        Date kakuhankaisiDate = DateUtil.convertStringToDate(itemKakuhankaisiDay.getValue(), itemKakuhankaisiTime.getValue());
        FXHDD01 itemKakuhansyuryoDay = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANSYURYO_DAY);    // 撹拌終了日
        FXHDD01 itemKakuhansyuryoTime = getItemRow(processData.getItemList(), GXHDO102B010Const.KAKUHANSYURYO_TIME); // 撹拌終了時間
        // 画面.撹拌終了日 + 画面.撹拌終了時間
        Date kakuhansyuryoDate = DateUtil.convertStringToDate(itemKakuhansyuryoDay.getValue(), itemKakuhansyuryoTime.getValue());
        // R001チェック呼出し
        String msgKakuhankaisiCheckR001 = validateUtil.checkR001(itemKakuhankaisiDay.getLabel1(), kakuhankaisiDate, itemKakuhansyuryoDay.getLabel1(), kakuhansyuryoDate);
        if (!StringUtil.isEmpty(msgKakuhankaisiCheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKakuhankaisiDay, itemKakuhankaisiTime, itemKakuhansyuryoDay, itemKakuhansyuryoTime);
            return MessageUtil.getErrorMessageInfo("", msgKakuhankaisiCheckR001, true, true, errFxhdd01List);
        }
        return null;
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
        processData.setUserAuthParam(GXHDO102B010Const.USER_AUTH_DELETE_PARAM);

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
            String lotNo9 = lotNo.substring(3, 12); //ﾛｯﾄNo(9桁)
            String edaban = lotNo.substring(12, 15); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // 原材料品質DB登録実績データ取得
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
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo9, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // 添加材ｽﾗﾘｰ作製・予備混合_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrTenkaPremixing(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・予備混合_削除処理
            deleteSrTenkaPremixing(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
                        GXHDO102B010Const.BTN_UPDATE_TOP,
                        GXHDO102B010Const.BTN_DELETE_TOP,
                        GXHDO102B010Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_TOP,
                        GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_TOP,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_TOP,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_TOP,
                        GXHDO102B010Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B010Const.BTN_DELETE_BOTTOM,
                        GXHDO102B010Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_BOTTOM,
                        GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_BOTTOM,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_BOTTOM,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B010Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B010Const.BTN_INSERT_TOP,
                        GXHDO102B010Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B010Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B010Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B010Const.BTN_INSERT_TOP,
                        GXHDO102B010Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_TOP,
                        GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_TOP,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_TOP,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_TOP,
                        GXHDO102B010Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B010Const.BTN_INSERT_BOTTOM,
                        GXHDO102B010Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B010Const.BTN_KAKUHANKAISINICHIJI_BOTTOM,
                        GXHDO102B010Const.BTN_KAKUHANSYURYONICHIJI_BOTTOM,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_BOTTOM,
                        GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B010Const.BTN_UPDATE_TOP,
                        GXHDO102B010Const.BTN_DELETE_TOP,
                        GXHDO102B010Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B010Const.BTN_DELETE_BOTTOM
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
        } else {
            // ﾛｯﾄ区分チェック
            String lotkubun = (String) shikakariData.get("lotkubun");
            if (StringUtil.isEmpty(lotkubun)) {
                errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }
        // 規格値により、画面項目を非表示にする。
        setItemNotShow(processData, queryRunnerDoc, queryRunnerQcdb, shikakariData, lotNo);
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
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map shikakariData, String lotNo) {
        // WIPﾛｯﾄNo
        this.setItemData(processData, GXHDO102B010Const.WIPLOTNO, lotNo);
        // 添加材ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B010Const.TENKAZAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 添加材ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B010Const.TENKAZAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        // ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B010Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B010Const.LOTKUBUN, lotkubuncode);
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

        List<SrTenkaPremixing> srTenkaPremixingList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);

        for (int i = 0; i < 5; i++) {
            // (3)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo9, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                // 【Ⅵ.画面項目制御・出力仕様.初期表示時①】を元に画面表示を行う。
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // 画面にデータを設定する(デフォルト値)
                processData.getItemList().forEach((fxhdd001) -> {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                });

                return true;
            }

            // 添加材ｽﾗﾘｰ作製・予備混合データ取得
            srTenkaPremixingList = getSrTenkaPremixingData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srTenkaPremixingList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTenkaPremixingList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTenkaPremixingList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTenkaPremixing 添加材ｽﾗﾘｰ作製・予備混合データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTenkaPremixing srTenkaPremixing) {

        // 投入①
        this.setItemData(processData, GXHDO102B010Const.TOUNYU1, getSrTenkaPremixingItemData(GXHDO102B010Const.TOUNYU1, srTenkaPremixing));

        // 投入②
        this.setItemData(processData, GXHDO102B010Const.TOUNYU2, getSrTenkaPremixingItemData(GXHDO102B010Const.TOUNYU2, srTenkaPremixing));

        // 途中撹拌開始日
        this.setItemData(processData, GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY, getSrTenkaPremixingItemData(GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY, srTenkaPremixing));

        // 途中攪拌開始時間
        this.setItemData(processData, GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME, getSrTenkaPremixingItemData(GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME, srTenkaPremixing));

        // 途中撹拌終了日
        this.setItemData(processData, GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY, getSrTenkaPremixingItemData(GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY, srTenkaPremixing));

        // 途中撹拌終了時間
        this.setItemData(processData, GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME, getSrTenkaPremixingItemData(GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME, srTenkaPremixing));

        // 投入③
        this.setItemData(processData, GXHDO102B010Const.TONYU3, getSrTenkaPremixingItemData(GXHDO102B010Const.TONYU3, srTenkaPremixing));

        // 投入④
        this.setItemData(processData, GXHDO102B010Const.TONYU4, getSrTenkaPremixingItemData(GXHDO102B010Const.TONYU4, srTenkaPremixing));

        // 投入⑤
        this.setItemData(processData, GXHDO102B010Const.TONYU5, getSrTenkaPremixingItemData(GXHDO102B010Const.TONYU5, srTenkaPremixing));

        // 撹拌開始日
        this.setItemData(processData, GXHDO102B010Const.KAKUHANKAISI_DAY, getSrTenkaPremixingItemData(GXHDO102B010Const.KAKUHANKAISI_DAY, srTenkaPremixing));

        // 撹拌開始時間
        this.setItemData(processData, GXHDO102B010Const.KAKUHANKAISI_TIME, getSrTenkaPremixingItemData(GXHDO102B010Const.KAKUHANKAISI_TIME, srTenkaPremixing));

        // 撹拌終了日
        this.setItemData(processData, GXHDO102B010Const.KAKUHANSYURYO_DAY, getSrTenkaPremixingItemData(GXHDO102B010Const.KAKUHANSYURYO_DAY, srTenkaPremixing));

        // 撹拌終了時間
        this.setItemData(processData, GXHDO102B010Const.KAKUHANSYURYO_TIME, getSrTenkaPremixingItemData(GXHDO102B010Const.KAKUHANSYURYO_TIME, srTenkaPremixing));

        // 回転体の接触確認
        this.setItemData(processData, GXHDO102B010Const.KAITENTAI, getSrTenkaPremixingItemData(GXHDO102B010Const.KAITENTAI, srTenkaPremixing));

        // 担当者
        this.setItemData(processData, GXHDO102B010Const.TANTOUSYA, getSrTenkaPremixingItemData(GXHDO102B010Const.TANTOUSYA, srTenkaPremixing));

        // 確認者
        this.setItemData(processData, GXHDO102B010Const.KAKUNINSYA, getSrTenkaPremixingItemData(GXHDO102B010Const.KAKUNINSYA, srTenkaPremixing));

        // 備考1
        this.setItemData(processData, GXHDO102B010Const.BIKOU1, getSrTenkaPremixingItemData(GXHDO102B010Const.BIKOU1, srTenkaPremixing));

        // 備考2
        this.setItemData(processData, GXHDO102B010Const.BIKOU2, getSrTenkaPremixingItemData(GXHDO102B010Const.BIKOU2, srTenkaPremixing));

    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・予備混合登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaPremixing> getSrTenkaPremixingData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTenkaPremixing(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrTenkaPremixing(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * [原材料品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
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
        // 原材料品質DB登録実績データの取得
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
     * [原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
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
        // 原材料品質DB登録実績データの取得
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
        // 原材料品質DB登録実績データの取得
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
     * [添加材ｽﾗﾘｰ作製・予備混合]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaPremixing> loadSrTenkaPremixing(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kakuhanki,tanku,youzai,senjojikan,"
                + "shujiku,ponpu,desubakaitensuu,tounyu1,tounyu2,totyukakuhanjikan,totyukakuhankaisinichiji,totyukakuhansyuryonichiji,"
                + "tonyu3,tonyu4,tonyu5,kakuhanjikan,kakuhankaisinichiji,kakuhansyuryonichiji,kaitentai,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_tenka_premixing "
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
        mapping.put("kojyo", "kojyo");                                            // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                            // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                          // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");              // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                      // ﾛｯﾄ区分
        mapping.put("kakuhanki", "kakuhanki");                                    // 撹拌機
        mapping.put("tanku", "tanku");                                            // ﾀﾝｸ
        mapping.put("youzai", "youzai");                                          // 溶剤
        mapping.put("senjojikan", "senjojikan");                                  // 洗浄時間
        mapping.put("shujiku", "shujiku");                                        // 主軸
        mapping.put("ponpu", "ponpu");                                            // ﾎﾟﾝﾌﾟ
        mapping.put("desubakaitensuu", "desubakaitensuu");                        // ﾃﾞｽﾊﾟ回転数
        mapping.put("tounyu1", "tounyu1");                                        // 投入①
        mapping.put("tounyu2", "tounyu2");                                        // 投入②
        mapping.put("totyukakuhanjikan", "totyukakuhanjikan");                    // 途中撹拌時間
        mapping.put("totyukakuhankaisinichiji", "totyukakuhankaisinichiji");      // 途中撹拌開始日時
        mapping.put("totyukakuhansyuryonichiji", "totyukakuhansyuryonichiji");    // 途中撹拌終了日時
        mapping.put("tonyu3", "tonyu3");                                          // 投入③
        mapping.put("tonyu4", "tonyu4");                                          // 投入④
        mapping.put("tonyu5", "tonyu5");                                          // 投入⑤
        mapping.put("kakuhanjikan", "kakuhanjikan");                              // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                // 撹拌開始日時
        mapping.put("kakuhansyuryonichiji", "kakuhansyuryonichiji");              // 撹拌終了日時
        mapping.put("kaitentai", "kaitentai");                                    // 回転体の接触確認
        mapping.put("tantousya", "tantousya");                                    // 担当者
        mapping.put("kakuninsya", "kakuninsya");                                  // 確認者
        mapping.put("bikou1", "bikou1");                                          // 備考1
        mapping.put("bikou2", "bikou2");                                          // 備考2
        mapping.put("torokunichiji", "torokunichiji");                            // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                              // 更新日時
        mapping.put("revision", "revision");                                      // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaPremixing>> beanHandler = new BeanListHandler<>(SrTenkaPremixing.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・予備混合_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaPremixing> loadTmpSrTenkaPremixing(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kakuhanki,tanku,youzai,senjojikan,"
                + "shujiku,ponpu,desubakaitensuu,tounyu1,tounyu2,totyukakuhanjikan,totyukakuhankaisinichiji,totyukakuhansyuryonichiji,"
                + "tonyu3,tonyu4,tonyu5,kakuhanjikan,kakuhankaisinichiji,kakuhansyuryonichiji,kaitentai,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_tenka_premixing "
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
        mapping.put("kojyo", "kojyo");                                            // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                            // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                          // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");              // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                      // ﾛｯﾄ区分
        mapping.put("kakuhanki", "kakuhanki");                                    // 撹拌機
        mapping.put("tanku", "tanku");                                            // ﾀﾝｸ
        mapping.put("youzai", "youzai");                                          // 溶剤
        mapping.put("senjojikan", "senjojikan");                                  // 洗浄時間
        mapping.put("shujiku", "shujiku");                                        // 主軸
        mapping.put("ponpu", "ponpu");                                            // ﾎﾟﾝﾌﾟ
        mapping.put("desubakaitensuu", "desubakaitensuu");                        // ﾃﾞｽﾊﾟ回転数
        mapping.put("tounyu1", "tounyu1");                                        // 投入①
        mapping.put("tounyu2", "tounyu2");                                        // 投入②
        mapping.put("totyukakuhanjikan", "totyukakuhanjikan");                    // 途中撹拌時間
        mapping.put("totyukakuhankaisinichiji", "totyukakuhankaisinichiji");      // 途中撹拌開始日時
        mapping.put("totyukakuhansyuryonichiji", "totyukakuhansyuryonichiji");    // 途中撹拌終了日時
        mapping.put("tonyu3", "tonyu3");                                          // 投入③
        mapping.put("tonyu4", "tonyu4");                                          // 投入④
        mapping.put("tonyu5", "tonyu5");                                          // 投入⑤
        mapping.put("kakuhanjikan", "kakuhanjikan");                              // 撹拌時間
        mapping.put("kakuhankaisinichiji", "kakuhankaisinichiji");                // 撹拌開始日時
        mapping.put("kakuhansyuryonichiji", "kakuhansyuryonichiji");              // 撹拌終了日時
        mapping.put("kaitentai", "kaitentai");                                    // 回転体の接触確認
        mapping.put("tantousya", "tantousya");                                    // 担当者
        mapping.put("kakuninsya", "kakuninsya");                                  // 確認者
        mapping.put("bikou1", "bikou1");                                          // 備考1
        mapping.put("bikou2", "bikou2");                                          // 備考2
        mapping.put("torokunichiji", "torokunichiji");                            // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                              // 更新日時
        mapping.put("revision", "revision");                                      // revision
        mapping.put("deleteflag", "deleteflag");                                  // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaPremixing>> beanHandler = new BeanListHandler<>(SrTenkaPremixing.class, rowProcessor);

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
     * @param srTenkaPremixing 添加材ｽﾗﾘｰ作製・予備混合データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTenkaPremixing srTenkaPremixing) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTenkaPremixing != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaPremixingItemData(itemId, srTenkaPremixing);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srTenkaPremixing 添加材ｽﾗﾘｰ作製・予備混合データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrTenkaPremixing srTenkaPremixing) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srTenkaPremixing != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaPremixingItemData(itemId, srTenkaPremixing);
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
     * 添加材ｽﾗﾘｰ作製・予備混合_仮登録(tmp_sr_tenka_premixing)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_premixing ("
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kakuhanki,tanku,youzai,senjojikan,shujiku,ponpu,"
                + "desubakaitensuu,tounyu1,tounyu2,totyukakuhanjikan,totyukakuhankaisinichiji,totyukakuhansyuryonichiji,tonyu3,tonyu4,"
                + "tonyu5,kakuhanjikan,kakuhankaisinichiji,kakuhansyuryonichiji,kaitentai,tantousya,kakuninsya,bikou1,bikou2,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrTenkaPremixing(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合_仮登録(tmp_sr_tenka_premixing)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_tenka_premixing SET "
                + " tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,kakuhanki = ?,tanku = ?,youzai = ?,senjojikan = ?,shujiku = ?,ponpu = ?,"
                + "desubakaitensuu = ?,tounyu1 = ?,tounyu2 = ?,totyukakuhanjikan = ?,totyukakuhankaisinichiji = ?,totyukakuhansyuryonichiji = ?,tonyu3 = ?,"
                + "tonyu4 = ?,tonyu5 = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuryonichiji = ?,kaitentai = ?,tantousya = ?,kakuninsya = ?,bikou1 = ?,"
                + "bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaPremixing> srTenkaPremixingList = getSrTenkaPremixingData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaPremixing srTenkaPremixing = null;
        if (!srTenkaPremixingList.isEmpty()) {
            srTenkaPremixing = srTenkaPremixingList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTenkaPremixing(false, newRev, 0, "", "", "", systemTime, processData, srTenkaPremixing);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合_仮登録(tmp_sr_tenka_premixing)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_tenka_premixing "
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
     * 添加材ｽﾗﾘｰ作製・予備混合_仮登録(tmp_sr_tenka_premixing)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaPremixing 添加材ｽﾗﾘｰ作製・予備混合データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTenkaPremixing(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaPremixing srTenkaPremixing) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 途中撹拌開始時間
        String totyukakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME, srTenkaPremixing));
        // 途中撹拌終了時間
        String totyukakuhansyuryoTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME, srTenkaPremixing));
        // 撹拌開始時間
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.KAKUHANKAISI_TIME, srTenkaPremixing));
        // 撹拌終了時間
        String kakuhansyuryoTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.KAKUHANSYURYO_TIME, srTenkaPremixing));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.TENKAZAISLURRYHINMEI, srTenkaPremixing))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.TENKAZAISLURRYLOTNO, srTenkaPremixing))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.LOTKUBUN, srTenkaPremixing))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.KAKUHANKI, srTenkaPremixing))); // 撹拌機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.TANKU, srTenkaPremixing))); // ﾀﾝｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.YOUZAI, srTenkaPremixing))); // 溶剤
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.SENJOJIKAN, srTenkaPremixing))); // 洗浄時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.SHUJIKU, srTenkaPremixing))); // 主軸
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.PONPU, srTenkaPremixing))); // ﾎﾟﾝﾌﾟ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.DESUBAKAITENSUU, srTenkaPremixing))); // ﾃﾞｽﾊﾟ回転数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TOUNYU1, srTenkaPremixing), null)); // 投入①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TOUNYU2, srTenkaPremixing), null)); // 投入②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.TOTYUKAKUHANJIKAN, srTenkaPremixing))); // 途中撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY, srTenkaPremixing),
                "".equals(totyukakuhankaisiTime) ? "0000" : totyukakuhankaisiTime)); // 途中撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY, srTenkaPremixing),
                "".equals(totyukakuhansyuryoTime) ? "0000" : totyukakuhansyuryoTime)); // 途中撹拌終了日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TONYU3, srTenkaPremixing), null)); // 投入③
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TONYU4, srTenkaPremixing), null)); // 投入④
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TONYU5, srTenkaPremixing), null)); // 投入⑤
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B010Const.KAKUHANJIKAN, srTenkaPremixing))); // 撹拌時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.KAKUHANKAISI_DAY, srTenkaPremixing),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime)); // 撹拌開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.KAKUHANSYURYO_DAY, srTenkaPremixing),
                "".equals(kakuhansyuryoTime) ? "0000" : kakuhansyuryoTime)); // 撹拌終了日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.KAITENTAI, srTenkaPremixing), null)); // 回転体の接触確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.TANTOUSYA, srTenkaPremixing))); // 担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.KAKUNINSYA, srTenkaPremixing))); // 確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.BIKOU1, srTenkaPremixing))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B010Const.BIKOU2, srTenkaPremixing))); // 備考2

        if (isInsert) {
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev);     //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ

        return params;
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合(sr_tenka_premixing)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaPremixing 登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaPremixing srTenkaPremixing) throws SQLException {

        String sql = "INSERT INTO sr_tenka_premixing ("
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kakuhanki,tanku,youzai,senjojikan,"
                + "shujiku,ponpu,desubakaitensuu,tounyu1,tounyu2,totyukakuhanjikan,totyukakuhankaisinichiji,totyukakuhansyuryonichiji,"
                + "tonyu3,tonyu4,tonyu5,kakuhanjikan,kakuhankaisinichiji,kakuhansyuryonichiji,kaitentai,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrTenkaPremixing(true, newRev, kojyo, lotNo, edaban, systemTime, processData, srTenkaPremixing);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合(sr_tenka_premixing)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_tenka_premixing SET "
                + " tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,kakuhanki = ?,tanku = ?,youzai = ?,senjojikan = ?,shujiku = ?,"
                + "ponpu = ?,desubakaitensuu = ?,tounyu1 = ?,tounyu2 = ?,totyukakuhanjikan = ?,totyukakuhankaisinichiji = ?,totyukakuhansyuryonichiji = ?,"
                + "tonyu3 = ?,tonyu4 = ?,tonyu5 = ?,kakuhanjikan = ?,kakuhankaisinichiji = ?,kakuhansyuryonichiji = ?,kaitentai = ?,tantousya = ?,"
                + "kakuninsya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaPremixing> srTenkaPremixingList = getSrTenkaPremixingData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaPremixing srTenkaPremixing = null;
        if (!srTenkaPremixingList.isEmpty()) {
            srTenkaPremixing = srTenkaPremixingList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTenkaPremixing(false, newRev, "", "", "", systemTime, processData, srTenkaPremixing);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合(sr_tenka_premixing)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaPremixing 添加材ｽﾗﾘｰ作製・予備混合データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTenkaPremixing(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaPremixing srTenkaPremixing) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 途中撹拌開始時間
        String totyukakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME, srTenkaPremixing));
        // 途中撹拌終了時間
        String totyukakuhansyuryoTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME, srTenkaPremixing));
        // 撹拌開始時間
        String kakuhankaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.KAKUHANKAISI_TIME, srTenkaPremixing));
        // 撹拌終了時間
        String kakuhansyuryoTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B010Const.KAKUHANSYURYO_TIME, srTenkaPremixing));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.TENKAZAISLURRYHINMEI, srTenkaPremixing))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.TENKAZAISLURRYLOTNO, srTenkaPremixing))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.LOTKUBUN, srTenkaPremixing))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.KAKUHANKI, srTenkaPremixing))); // 撹拌機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.TANKU, srTenkaPremixing))); // ﾀﾝｸ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.YOUZAI, srTenkaPremixing))); // 溶剤
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.SENJOJIKAN, srTenkaPremixing))); // 洗浄時間
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.SHUJIKU, srTenkaPremixing))); // 主軸
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.PONPU, srTenkaPremixing))); // ﾎﾟﾝﾌﾟ
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.DESUBAKAITENSUU, srTenkaPremixing))); // ﾃﾞｽﾊﾟ回転数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TOUNYU1, srTenkaPremixing), 9)); // 投入①
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TOUNYU2, srTenkaPremixing), 9)); // 投入②
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.TOTYUKAKUHANJIKAN, srTenkaPremixing))); // 途中撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY, srTenkaPremixing),
                "".equals(totyukakuhankaisiTime) ? "0000" : totyukakuhankaisiTime)); // 途中撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY, srTenkaPremixing),
                "".equals(totyukakuhansyuryoTime) ? "0000" : totyukakuhansyuryoTime)); // 途中撹拌終了日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TONYU3, srTenkaPremixing), 9)); // 投入③
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TONYU4, srTenkaPremixing), 9)); // 投入④
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.TONYU5, srTenkaPremixing), 9)); // 投入⑤
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B010Const.KAKUHANJIKAN, srTenkaPremixing))); // 撹拌時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B010Const.KAKUHANKAISI_DAY, srTenkaPremixing),
                "".equals(kakuhankaisiTime) ? "0000" : kakuhankaisiTime)); // 撹拌開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B010Const.KAKUHANSYURYO_DAY, srTenkaPremixing),
                "".equals(kakuhansyuryoTime) ? "0000" : kakuhansyuryoTime)); // 撹拌終了日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B010Const.KAITENTAI, srTenkaPremixing), 9)); // 回転体の接触確認
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.TANTOUSYA, srTenkaPremixing))); // 担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.KAKUNINSYA, srTenkaPremixing))); // 確認者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.BIKOU1, srTenkaPremixing))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B010Const.BIKOU2, srTenkaPremixing))); // 備考2

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
     * 添加材ｽﾗﾘｰ作製・予備混合(sr_tenka_premixing)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_tenka_premixing "
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
     * [添加材ｽﾗﾘｰ作製・予備混合_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_tenka_premixing "
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
     * @param fxhdd11RevInfo 原材料品質DB登録実績データ
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
            // 原材料品質DB登録実績データが取得出来ていない場合エラー
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
     * @param srTenkaPremixing 添加材ｽﾗﾘｰ作製・予備混合データ
     * @return DB値
     */
    private String getSrTenkaPremixingItemData(String itemId, SrTenkaPremixing srTenkaPremixing) {
        switch (itemId) {
            // 添加材ｽﾗﾘｰ品名
            case GXHDO102B010Const.TENKAZAISLURRYHINMEI:
                return StringUtil.nullToBlank(srTenkaPremixing.getTenkazaislurryhinmei());

            // 添加材ｽﾗﾘｰLotNo
            case GXHDO102B010Const.TENKAZAISLURRYLOTNO:
                return StringUtil.nullToBlank(srTenkaPremixing.getTenkazaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B010Const.LOTKUBUN:
                return StringUtil.nullToBlank(srTenkaPremixing.getLotkubun());

            // 撹拌機
            case GXHDO102B010Const.KAKUHANKI:
                return StringUtil.nullToBlank(srTenkaPremixing.getKakuhanki());

            // ﾀﾝｸ
            case GXHDO102B010Const.TANKU:
                return StringUtil.nullToBlank(srTenkaPremixing.getTanku());

            // 溶剤
            case GXHDO102B010Const.YOUZAI:
                return StringUtil.nullToBlank(srTenkaPremixing.getYouzai());

            // 洗浄時間
            case GXHDO102B010Const.SENJOJIKAN:
                return StringUtil.nullToBlank(srTenkaPremixing.getSenjojikan());

            // 主軸
            case GXHDO102B010Const.SHUJIKU:
                return StringUtil.nullToBlank(srTenkaPremixing.getShujiku());

            // ﾎﾟﾝﾌﾟ
            case GXHDO102B010Const.PONPU:
                return StringUtil.nullToBlank(srTenkaPremixing.getPonpu());

            // ﾃﾞｽﾊﾟ回転数
            case GXHDO102B010Const.DESUBAKAITENSUU:
                return StringUtil.nullToBlank(srTenkaPremixing.getDesubakaitensuu());

            // 投入①
            case GXHDO102B010Const.TOUNYU1:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaPremixing.getTounyu1()));

            // 投入②
            case GXHDO102B010Const.TOUNYU2:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaPremixing.getTounyu2()));

            // 途中撹拌時間
            case GXHDO102B010Const.TOTYUKAKUHANJIKAN:
                return StringUtil.nullToBlank(srTenkaPremixing.getTotyukakuhanjikan());

            // 途中撹拌開始日
            case GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getTotyukakuhankaisinichiji(), "yyMMdd");

            // 途中攪拌開始時間
            case GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getTotyukakuhankaisinichiji(), "HHmm");

            // 途中撹拌終了日
            case GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getTotyukakuhansyuryonichiji(), "yyMMdd");

            // 途中撹拌終了時間
            case GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getTotyukakuhansyuryonichiji(), "HHmm");

            // 投入③
            case GXHDO102B010Const.TONYU3:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaPremixing.getTonyu3()));

            // 投入④
            case GXHDO102B010Const.TONYU4:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaPremixing.getTonyu4()));

            // 投入⑤
            case GXHDO102B010Const.TONYU5:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaPremixing.getTonyu5()));

            // 撹拌時間
            case GXHDO102B010Const.KAKUHANJIKAN:
                return StringUtil.nullToBlank(srTenkaPremixing.getKakuhanjikan());

            // 撹拌開始日
            case GXHDO102B010Const.KAKUHANKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getKakuhankaisinichiji(), "yyMMdd");

            // 撹拌開始時間
            case GXHDO102B010Const.KAKUHANKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getKakuhankaisinichiji(), "HHmm");

            // 撹拌終了日
            case GXHDO102B010Const.KAKUHANSYURYO_DAY:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getKakuhansyuryonichiji(), "yyMMdd");

            // 撹拌終了時間
            case GXHDO102B010Const.KAKUHANSYURYO_TIME:
                return DateUtil.formattedTimestamp(srTenkaPremixing.getKakuhansyuryonichiji(), "HHmm");

            // 回転体の接触確認
            case GXHDO102B010Const.KAITENTAI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaPremixing.getKaitentai()));

            // 担当者
            case GXHDO102B010Const.TANTOUSYA:
                return StringUtil.nullToBlank(srTenkaPremixing.getTantousya());

            // 確認者
            case GXHDO102B010Const.KAKUNINSYA:
                return StringUtil.nullToBlank(srTenkaPremixing.getKakuninsya());

            // 備考1
            case GXHDO102B010Const.BIKOU1:
                return StringUtil.nullToBlank(srTenkaPremixing.getBikou1());

            // 備考2
            case GXHDO102B010Const.BIKOU2:
                return StringUtil.nullToBlank(srTenkaPremixing.getBikou2());

            default:
                return null;
        }
    }

    /**
     * 添加材ｽﾗﾘｰ作製・予備混合_仮登録(tmp_sr_tenka_premixing)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrTenkaPremixing(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_premixing ( "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kakuhanki,tanku,youzai,senjojikan,"
                + "shujiku,ponpu,desubakaitensuu,tounyu1,tounyu2,totyukakuhanjikan,totyukakuhankaisinichiji,totyukakuhansyuryonichiji,"
                + "tonyu3,tonyu4,tonyu5,kakuhanjikan,kakuhankaisinichiji,kakuhansyuryonichiji,kaitentai,tantousya,kakuninsya,"
                + "bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kakuhanki,tanku,youzai,senjojikan,"
                + "shujiku,ponpu,desubakaitensuu,tounyu1,tounyu2,totyukakuhanjikan,totyukakuhankaisinichiji,totyukakuhansyuryonichiji,"
                + "tonyu3,tonyu4,tonyu5,kakuhanjikan,kakuhankaisinichiji,kakuhansyuryonichiji,kaitentai,tantousya,kakuninsya,"
                + "bikou1,bikou2,?,?,?,? "
                + " FROM sr_tenka_premixing "
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
     * 規格値により、画面項目を非表示にする。
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param shikakariData 前工程WIPから仕掛情報
     * @param lotNo ﾛｯﾄNo
     * @throws SQLException 例外エラー
     */
    private void setItemNotShow(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, Map shikakariData, String lotNo) throws SQLException {

        String kojyo = lotNo.substring(0, 3);
        String lotNo9 = lotNo.substring(3, 12);
        String edaban = lotNo.substring(12, 15);
        String syurui = "添加材ｽﾗﾘｰ作製";
        // [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、ﾃﾞｰﾀを取得
        Map fxhbm03Data = loadFxhbm03Data(queryRunnerDoc, "添加剤ｽﾗﾘｰ_予備混合(ﾌﾟﾚﾐｷｼﾝｸﾞ)_重量_表示制御");
        // 画面非表示項目リスト:途中撹拌時間、途中撹拌開始日、途中攪拌開始時間、途中撹拌終了日、途中撹拌終了時間
        List<String> notShowItemList = Arrays.asList(GXHDO102B010Const.TOTYUKAKUHANJIKAN, GXHDO102B010Const.TOTYUKAKUHANKAISI_DAY, GXHDO102B010Const.TOTYUKAKUHANKAISI_TIME,
                GXHDO102B010Const.TOTYUKAKUHANSYURYO_DAY,
                GXHDO102B010Const.TOTYUKAKUHANSYURYO_TIME);

        // 画面非表示上部ﾎﾞﾀﾝリスト: 途中撹拌開始日時、途中撹拌終了日時
        List<String> notShowButtonTopList = Arrays.asList(GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_TOP, GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_TOP);
        // 画面非表示下部ﾎﾞﾀﾝリスト: 途中撹拌開始日時、途中撹拌終了日時
        List<String> notShowButtonButtonList = Arrays.asList(GXHDO102B010Const.BTN_TOTYUKAKUHANKAISINICHIJI_BOTTOM, GXHDO102B010Const.BTN_TOTYUKAKUHANSYURYONICHIJI_BOTTOM);
        if (fxhbm03Data == null) {
            // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
            removeItemFromItemList(processData, notShowItemList, notShowButtonTopList, notShowButtonButtonList);
            return;
        }

        // [前工程設計]から、ﾃﾞｰﾀを取得
        Map daMkSekKeiData = loadDaMkSekKeiData(queryRunnerQcdb, kojyo, lotNo9, edaban, syurui);
        if (daMkSekKeiData == null || daMkSekKeiData.isEmpty()) {
            // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
            removeItemFromItemList(processData, notShowItemList, notShowButtonTopList, notShowButtonButtonList);
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
                removeItemFromItemList(processData, notShowItemList, notShowButtonTopList, notShowButtonButtonList);
                return;
            }
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkhYoJunJokenData, "kikakuti"));
            if (!"1".equals(kikakuti)) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList, notShowButtonTopList, notShowButtonButtonList);
            }
        } else {
            // 前工程規格情報の規格値
            String kikakuti = StringUtil.nullToBlank(getMapData(daMkJokenData, "kikakuti"));
            if (!"1".equals(kikakuti)) {
                // 取得できなかった場合、以下の項目を非表示にして処理を終了する。
                removeItemFromItemList(processData, notShowItemList, notShowButtonTopList, notShowButtonButtonList);
            }
        }
    }

    /**
     * 項目の表示制御
     *
     * @param processData 処理制御データ
     * @param notShowItemList 画面非表示項目リスト
     * @param notShowButtonTopList 画面非表示上部ﾎﾞﾀﾝリスト
     * @param notShowButtonButtonList 画面非表示下部ﾎﾞﾀﾝリスト
     */
    private void removeItemFromItemList(ProcessData processData, List<String> notShowItemList, List<String> notShowButtonTopList, List<String> notShowButtonButtonList) {
        List<FXHDD01> itemList = processData.getItemList();
        // 以下の項目を画面非表示にする。
        notShowItemList.forEach((notShowItem) -> {
            itemList.remove(getItemRow(itemList, notShowItem));
        });

        GXHDO901B gxhdo901bBean = (GXHDO901B) getFormBean("gXHDO901B");
        List<FXHDD02> buttonListTop = gxhdo901bBean.getButtonListTop();
        List<FXHDD02> buttonListBottom = gxhdo901bBean.getButtonListBottom();
        notShowButtonTopList.forEach((notShowButton) -> {
            buttonListTop.remove(getButtonRow(buttonListTop, notShowButton));
        });
        notShowButtonButtonList.forEach((notShowButton) -> {
            buttonListBottom.remove(getButtonRow(buttonListBottom, notShowButton));
        });
    }

    /**
     * ボタンデータ取得
     *
     * @param listData フォームデータ
     * @param buttonId ボタンID
     * @return 項目データ
     */
    private FXHDD02 getButtonRow(List<FXHDD02> buttonList, String buttonId) {
        return buttonList.stream().filter(n -> buttonId.equals(n.getButtonId())).findFirst().orElse(null);
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
