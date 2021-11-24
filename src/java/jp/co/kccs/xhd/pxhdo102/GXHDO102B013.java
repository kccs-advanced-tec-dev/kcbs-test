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
import jp.co.kccs.xhd.db.model.SrTenkaBet;
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

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2021/10/19<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B013(添加材ｽﾗﾘｰ作製・BET)
 *
 * @author KCSS K.Jo
 * @since 2021/10/19
 */
public class GXHDO102B013 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B013.class.getName());
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
    public GXHDO102B013() {
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
                    GXHDO102B013Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_TOP,
                    GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_TOP,
                    GXHDO102B013Const.BTN_DASSIKAISINICHIJI_TOP,
                    GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_TOP,
                    GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_TOP,
                    GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_TOP,
                    GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_TOP,
                    GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_TOP,
                    GXHDO102B013Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_BOTTOM,
                    GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_BOTTOM,
                    GXHDO102B013Const.BTN_DASSIKAISINICHIJI_BOTTOM,
                    GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_BOTTOM,
                    GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_BOTTOM,
                    GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_BOTTOM,
                    GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_BOTTOM,
                    GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B013Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B013Const.BTN_INSERT_TOP,
                    GXHDO102B013Const.BTN_DELETE_TOP,
                    GXHDO102B013Const.BTN_UPDATE_TOP,
                    GXHDO102B013Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B013Const.BTN_INSERT_BOTTOM,
                    GXHDO102B013Const.BTN_DELETE_BOTTOM,
                    GXHDO102B013Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B013Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B013Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 仮登録
            case GXHDO102B013Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B013Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B013Const.BTN_INSERT_TOP:
            case GXHDO102B013Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B013Const.BTN_UPDATE_TOP:
            case GXHDO102B013Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B013Const.BTN_DELETE_TOP:
            case GXHDO102B013Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 乾燥開始日時
            case GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_TOP:
            case GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_BOTTOM:
                method = "setKansoukaisinichiji";
                break;
            // 乾燥終了日時
            case GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_TOP:
            case GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_BOTTOM:
                method = "setKansousyuuryounichiji";
                break;
            // 脱脂開始日時
            case GXHDO102B013Const.BTN_DASSIKAISINICHIJI_TOP:
            case GXHDO102B013Const.BTN_DASSIKAISINICHIJI_BOTTOM:
                method = "setDassikaisinichiji";
                break;
            // 脱脂終了日時
            case GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_TOP:
            case GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_BOTTOM:
                method = "setDassisyuuryounichiji";
                break;
            // 前処理開始日時
            case GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_TOP:
            case GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_BOTTOM:
                method = "setMaesyorikaisinichiji";
                break;
            // 前処理終了日時
            case GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_TOP:
            case GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_BOTTOM:
                method = "setMaesyorisyuuryounichiji";
                break;
            // 比表面積測定開始日時
            case GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_TOP:
            case GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_BOTTOM:
                method = "setHihyoumensekisokuteikaisi";
                break;
            // 比表面積測定終了日時
            case GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_TOP:
            case GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_BOTTOM:
                method = "setHihyoumensekisokuteisyuuryou";
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

            // 添加材ｽﾗﾘｰ作製・BETの入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrTenkaBet> srTenkaBetDataList = getSrTenkaBetData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo9, oyalotEdaban);
            if (srTenkaBetDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTenkaBetDataList.get(0));

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
     * 乾燥開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansoukaisinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 乾燥終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setKansousyuuryounichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUSYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUSYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 脱脂開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setDassikaisinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSIKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSIKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 脱脂終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setDassisyuuryounichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSISYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSISYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 前処理開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setMaesyorikaisinichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORIKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORIKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 前処理終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setMaesyorisyuuryounichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORISYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORISYUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 比表面積測定開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHihyoumensekisokuteikaisi(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 比表面積測定終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setHihyoumensekisokuteisyuuryou(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME);
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

                // 添加材ｽﾗﾘｰ作製・BET_仮登録登録処理
                insertTmpSrTenkaBet(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo9, edaban, strSystime, processData);
            } else {

                // 添加材ｽﾗﾘｰ作製・BET_仮登録更新処理
                updateTmpSrTenkaBet(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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

        // 乾燥時間ﾁｪｯｸ
        List<String> kansoukaisiList = Arrays.asList(GXHDO102B013Const.KANSOUKAISI_DAY, GXHDO102B013Const.KANSOUKAISI_TIME);
        List<String> kansousyuuryouList = Arrays.asList(GXHDO102B013Const.KANSOUSYUURYOU_DAY, GXHDO102B013Const.KANSOUSYUURYOU_TIME);

        // 脱脂時間ﾁｪｯｸ
        List<String> dassikaisiList = Arrays.asList(GXHDO102B013Const.DASSIKAISI_DAY, GXHDO102B013Const.DASSIKAISI_TIME);
        List<String> dassisyuuryouList = Arrays.asList(GXHDO102B013Const.DASSISYUURYOU_DAY, GXHDO102B013Const.DASSISYUURYOU_TIME);

        // 前処理時間ﾁｪｯｸ
        List<String> maesyorikaisiList = Arrays.asList(GXHDO102B013Const.MAESYORIKAISI_DAY, GXHDO102B013Const.MAESYORIKAISI_TIME);
        List<String> maesyorisyuuryouList = Arrays.asList(GXHDO102B013Const.MAESYORISYUURYOU_DAY, GXHDO102B013Const.MAESYORISYUURYOU_TIME);

        // 比表面積測定規格ﾁｪｯｸ
        List<String> hihyoumensekisokuteikaisiList = Arrays.asList(GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME);
        List<String> hihyoumensekisokuteisyuuryouList = Arrays.asList(GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME);

        // 規格値の入力値チェック必要の項目リスト
        List<FXHDD01> itemList = new ArrayList<>();
        // 乾燥時間の時間差数
        FXHDD01 kansoukaisiDiffHours = getDiffMinutes(processData, GXHDO102B013Const.KANSOUJIKANKIKAKU, kansoukaisiList, kansousyuuryouList);
        // 脱脂時間の時間差数
        FXHDD01 dassikaisidiffHours = getDiffMinutes(processData, GXHDO102B013Const.DASSIJIKANKIKAKU, dassikaisiList, dassisyuuryouList);
        // 前処理時間の時間差数
        FXHDD01 maesyorikaisidiffHours = getDiffMinutes(processData, GXHDO102B013Const.MAESYORIJIKAN, maesyorikaisiList, maesyorisyuuryouList);
        // 比表面積測定規格の時間差数
        FXHDD01 hihyoumensekisokuteikaisidiffHours = getDiffMinutes(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKEKKA, hihyoumensekisokuteikaisiList, hihyoumensekisokuteisyuuryouList);

        String kansoukaisi = "乾燥時間";
        String dassikaisi = "脱脂時間";
        String maesyorikaisi = "前処理時間";
        String hihyoumensekisokuteikaisi = "比表面積測定規格";
        // 項目の項目名を設置
        if (kansoukaisiDiffHours != null) {
            kansoukaisiDiffHours.setLabel1(kansoukaisi);
            itemList.add(kansoukaisiDiffHours);
        }
        if (dassikaisidiffHours != null) {
            dassikaisidiffHours.setLabel1(dassikaisi);
            itemList.add(dassikaisidiffHours);
        }
        if (maesyorikaisidiffHours != null) {
            maesyorikaisidiffHours.setLabel1(maesyorikaisi);
            itemList.add(maesyorikaisidiffHours);
        }
        if (hihyoumensekisokuteikaisidiffHours != null) {
            hihyoumensekisokuteikaisidiffHours.setLabel1(hihyoumensekisokuteikaisi);
            itemList.add(hihyoumensekisokuteikaisidiffHours);
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
        if(errorMessageInfo == null){
            setItemBackColor(processData, kansoukaisiList, kansousyuuryouList, kansoukaisi, kikakuchiInputErrorInfoList);
            setItemBackColor(processData, dassikaisiList, dassisyuuryouList, dassikaisi, kikakuchiInputErrorInfoList);
            setItemBackColor(processData, maesyorikaisiList, maesyorisyuuryouList, maesyorikaisi, kikakuchiInputErrorInfoList);
            setItemBackColor(processData, hihyoumensekisokuteikaisiList, hihyoumensekisokuteisyuuryouList, hihyoumensekisokuteikaisi, kikakuchiInputErrorInfoList);
        }

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
            SrTenkaBet tmpSrTenkaBet = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTenkaBet> srTenkaBetList = getSrTenkaBetData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo9, edaban);
                if (!srTenkaBetList.isEmpty()) {
                    tmpSrTenkaBet = srTenkaBetList.get(0);
                }

                deleteTmpSrTenkaBet(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);
            }

            // 添加材ｽﾗﾘｰ作製・BET_登録処理
            insertSrTenkaBet(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo9, edaban, strSystime, processData, tmpSrTenkaBet);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        processData.setUserAuthParam(GXHDO102B013Const.USER_AUTH_UPDATE_PARAM);

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

            // 添加材ｽﾗﾘｰ作製・BET_更新処理
            updateSrTenkaBet(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo9, edaban, strSystime, processData);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert102B(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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

        FXHDD01 itemKansoukaisiDay = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUKAISI_DAY);  // 乾燥開始日
        FXHDD01 itemKansoukaisiTime = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUKAISI_TIME); // 乾燥開始時間
        // 画面.乾燥開始日 + 画面.乾燥開始時間
        Date kansoukaisiDate = DateUtil.convertStringToDate(itemKansoukaisiDay.getValue(), itemKansoukaisiTime.getValue());
        FXHDD01 itemKansousyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUSYUURYOU_DAY);    // 乾燥終了日
        FXHDD01 itemKansousyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B013Const.KANSOUSYUURYOU_TIME); // 乾燥終了時間
        // 画面.乾燥終了日 + 画面.乾燥終了時間
        Date kansousyuuryouDate = DateUtil.convertStringToDate(itemKansousyuuryouDay.getValue(), itemKansousyuuryouTime.getValue());
        // R001チェック呼出し
        String msgKansoukaisiCheckR001 = validateUtil.checkR001("乾燥開始日時", kansoukaisiDate, "乾燥終了日時", kansousyuuryouDate);
        if (!StringUtil.isEmpty(msgKansoukaisiCheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemKansoukaisiDay, itemKansoukaisiTime, itemKansousyuuryouDay, itemKansousyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgKansoukaisiCheckR001, true, true, errFxhdd01List);
        }

        FXHDD01 itemDassikaisiDay = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSIKAISI_DAY);   // 脱脂開始日
        FXHDD01 itemDassikaisiTime = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSIKAISI_TIME); // 脱脂開始時間
        // 画面.脱脂開始日 + 画面.脱脂開始時間
        Date dassikaisiDate = DateUtil.convertStringToDate(itemDassikaisiDay.getValue(), itemDassikaisiTime.getValue());
        FXHDD01 itemDassisyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSISYUURYOU_DAY);   // 脱脂終了日
        FXHDD01 itemDassisyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B013Const.DASSISYUURYOU_TIME); // 脱脂終了時間
        // 画面.脱脂終了日 + 画面.脱脂終了時間
        Date dassisyuuryouDate = DateUtil.convertStringToDate(itemDassisyuuryouDay.getValue(), itemDassisyuuryouTime.getValue());
        // R001チェック呼出し
        String msgDassikaisiDateCheckR001 = validateUtil.checkR001("脱脂開始日時", dassikaisiDate, "脱脂終了日時", dassisyuuryouDate);
        if (!StringUtil.isEmpty(msgDassikaisiDateCheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemDassikaisiDay, itemDassikaisiTime, itemDassisyuuryouDay, itemDassisyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgDassikaisiDateCheckR001, true, true, errFxhdd01List);
        }

        FXHDD01 itemMaesyorikaisiDay = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORIKAISI_DAY);   // 前処理開始日
        FXHDD01 itemMaesyorikaisiTime = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORIKAISI_TIME); // 前処理開始時間
        // 画面.前処理開始日 + 画面.前処理開始時間
        Date maesyorikaisiDate = DateUtil.convertStringToDate(itemMaesyorikaisiDay.getValue(), itemMaesyorikaisiTime.getValue());

        FXHDD01 itemMaesyorisyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORISYUURYOU_DAY);   // 前処理終了日
        FXHDD01 itemMaesyorisyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B013Const.MAESYORISYUURYOU_TIME); // 前処理終了時間
        // 画面.前処理終了日 + 画面.前処理終了時間
        Date maesyorisyuuryouDate = DateUtil.convertStringToDate(itemMaesyorisyuuryouDay.getValue(), itemMaesyorisyuuryouTime.getValue());
        // R001チェック呼出し
        String msgMaesyorisyuuryouCheckR001 = validateUtil.checkR001("前処理開始日時", maesyorikaisiDate, "前処理終了日時", maesyorisyuuryouDate);
        if (!StringUtil.isEmpty(msgMaesyorisyuuryouCheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemMaesyorikaisiDay, itemMaesyorikaisiTime, itemMaesyorisyuuryouDay, itemMaesyorisyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgMaesyorisyuuryouCheckR001, true, true, errFxhdd01List);

        }

        FXHDD01 itemHihyoumensekisokuteikaisiDay = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY);   // 比表面積測定開始日
        FXHDD01 itemHihyoumensekisokuteikaisiTime = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME); // 比表面積測定開始時間
        // 画面.比表面積測定開始日 + 画面.比表面積測定開始時間
        Date hihyoumensekisokuteikaisiDate = DateUtil.convertStringToDate(itemHihyoumensekisokuteikaisiDay.getValue(), itemHihyoumensekisokuteikaisiTime.getValue());

        FXHDD01 itemhiHyoumensekisokuteisyuuryouDay = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY);   // 比表面積測定終了日
        FXHDD01 itemHihyoumensekisokuteisyuuryouTime = getItemRow(processData.getItemList(), GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME); // 比表面積測定終了時間
        // 画面.比表面積測定終了日 + 画面.比表面積測定終了時間
        Date hihyoumensekisokuteisyuuryouDate = DateUtil.convertStringToDate(itemhiHyoumensekisokuteisyuuryouDay.getValue(), itemHihyoumensekisokuteisyuuryouTime.getValue());
        // R001チェック呼出し
        String msgHihyoumensekisokuteikaisiCheckR001 = validateUtil.checkR001("比表面積測定開始日時", hihyoumensekisokuteikaisiDate, "比表面積測定終了日時",
                hihyoumensekisokuteisyuuryouDate);
        if (!StringUtil.isEmpty(msgHihyoumensekisokuteikaisiCheckR001)) {
            // エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemHihyoumensekisokuteikaisiDay, itemHihyoumensekisokuteikaisiTime, itemhiHyoumensekisokuteisyuuryouDay, itemHihyoumensekisokuteisyuuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgHihyoumensekisokuteikaisiCheckR001, true, true, errFxhdd01List);
        }

        // ﾁｪｯｸﾎﾞｯｸｽがすべてﾁｪｯｸされているかﾁｪｯｸ
        List<String> itemIdList = Arrays.asList(GXHDO102B013Const.KANSOUZARANOSYURUI, GXHDO102B013Const.DASSIROGOUKI, GXHDO102B013Const.SOKUTEISAMPLE, GXHDO102B013Const.SAMPLEJYUURYOU);
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
        processData.setUserAuthParam(GXHDO102B013Const.USER_AUTH_DELETE_PARAM);

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

            // 添加材ｽﾗﾘｰ作製・BET_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo9, edaban);
            insertDeleteDataTmpSrTenkaBet(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo9, edaban, strSystime);

            // 添加材ｽﾗﾘｰ作製・BET_削除処理
            deleteSrTenkaBet(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo9, edaban);

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
                        GXHDO102B013Const.BTN_UPDATE_TOP,
                        GXHDO102B013Const.BTN_DELETE_TOP,
                        GXHDO102B013Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_TOP,
                        GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_TOP,
                        GXHDO102B013Const.BTN_DASSIKAISINICHIJI_TOP,
                        GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_TOP,
                        GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_TOP,
                        GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_TOP,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_TOP,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_TOP,
                        GXHDO102B013Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B013Const.BTN_DELETE_BOTTOM,
                        GXHDO102B013Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_DASSIKAISINICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_BOTTOM,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B013Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B013Const.BTN_INSERT_TOP,
                        GXHDO102B013Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B013Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B013Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B013Const.BTN_INSERT_TOP,
                        GXHDO102B013Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_TOP,
                        GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_TOP,
                        GXHDO102B013Const.BTN_DASSIKAISINICHIJI_TOP,
                        GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_TOP,
                        GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_TOP,
                        GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_TOP,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_TOP,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_TOP,
                        GXHDO102B013Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B013Const.BTN_INSERT_BOTTOM,
                        GXHDO102B013Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B013Const.BTN_KANSOUKAISINICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_KANSOUSYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_DASSIKAISINICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_DASSISYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_MAESYORIKAISINICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_MAESYORISYUURYOUNICHIJI_BOTTOM,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEIKAISI_BOTTOM,
                        GXHDO102B013Const.BTN_HIHYOUMENSEKISOKUTEISYUURYOU_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B013Const.BTN_UPDATE_TOP,
                        GXHDO102B013Const.BTN_DELETE_TOP,
                        GXHDO102B013Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B013Const.BTN_DELETE_BOTTOM
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
        this.setItemData(processData, GXHDO102B013Const.WIPLOTNO, lotNo);
        // 添加材ｽﾗﾘｰ品名
        this.setItemData(processData, GXHDO102B013Const.TENKAZAISLURRYHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // 添加材ｽﾗﾘｰLotNo
        this.setItemData(processData, GXHDO102B013Const.TENKAZAISLURRYLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        //ﾛｯﾄ区分名
        String lotkubun = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubun"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B013Const.LOTKUBUN, "");
        } else {
            if (!StringUtil.isEmpty(lotkubun)) {
                lotkubuncode = lotkubuncode + ":" + lotkubun;
            }
            this.setItemData(processData, GXHDO102B013Const.LOTKUBUN, lotkubuncode);
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

        List<SrTenkaBet> srTenkaBetList = new ArrayList<>();
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

            // 添加材ｽﾗﾘｰ作製・BETデータ取得
            srTenkaBetList = getSrTenkaBetData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo9, edaban);
            if (srTenkaBetList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srTenkaBetList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srTenkaBetList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTenkaBet 添加材ｽﾗﾘｰ作製・BETデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTenkaBet srTenkaBet) {

        // 乾燥皿の種類
        this.setItemData(processData, GXHDO102B013Const.KANSOUZARANOSYURUI, getSrTenkaBetItemData(GXHDO102B013Const.KANSOUZARANOSYURUI, srTenkaBet));

        // ｽﾗﾘｰ重量
        this.setItemData(processData, GXHDO102B013Const.SLURRYJYUURYOU, getSrTenkaBetItemData(GXHDO102B013Const.SLURRYJYUURYOU, srTenkaBet));

        // 乾燥開始日
        this.setItemData(processData, GXHDO102B013Const.KANSOUKAISI_DAY, getSrTenkaBetItemData(GXHDO102B013Const.KANSOUKAISI_DAY, srTenkaBet));

        // 乾燥開始時間
        this.setItemData(processData, GXHDO102B013Const.KANSOUKAISI_TIME, getSrTenkaBetItemData(GXHDO102B013Const.KANSOUKAISI_TIME, srTenkaBet));

        // 乾燥終了日
        this.setItemData(processData, GXHDO102B013Const.KANSOUSYUURYOU_DAY, getSrTenkaBetItemData(GXHDO102B013Const.KANSOUSYUURYOU_DAY, srTenkaBet));

        // 乾燥終了時間
        this.setItemData(processData, GXHDO102B013Const.KANSOUSYUURYOU_TIME, getSrTenkaBetItemData(GXHDO102B013Const.KANSOUSYUURYOU_TIME, srTenkaBet));

        // 脱脂炉号機
        this.setItemData(processData, GXHDO102B013Const.DASSIROGOUKI, getSrTenkaBetItemData(GXHDO102B013Const.DASSIROGOUKI, srTenkaBet));

        // 脱脂開始日
        this.setItemData(processData, GXHDO102B013Const.DASSIKAISI_DAY, getSrTenkaBetItemData(GXHDO102B013Const.DASSIKAISI_DAY, srTenkaBet));

        // 脱脂開始時間
        this.setItemData(processData, GXHDO102B013Const.DASSIKAISI_TIME, getSrTenkaBetItemData(GXHDO102B013Const.DASSIKAISI_TIME, srTenkaBet));

        // 脱脂終了日
        this.setItemData(processData, GXHDO102B013Const.DASSISYUURYOU_DAY, getSrTenkaBetItemData(GXHDO102B013Const.DASSISYUURYOU_DAY, srTenkaBet));

        // 脱脂終了時間
        this.setItemData(processData, GXHDO102B013Const.DASSISYUURYOU_TIME, getSrTenkaBetItemData(GXHDO102B013Const.DASSISYUURYOU_TIME, srTenkaBet));

        // 乾燥担当者
        this.setItemData(processData, GXHDO102B013Const.KANSOUTANTOUSYA, getSrTenkaBetItemData(GXHDO102B013Const.KANSOUTANTOUSYA, srTenkaBet));

        // 測定ｻﾝﾌﾟﾙ数
        this.setItemData(processData, GXHDO102B013Const.SOKUTEISAMPLE, getSrTenkaBetItemData(GXHDO102B013Const.SOKUTEISAMPLE, srTenkaBet));

        // ｻﾝﾌﾟﾙ重量
        this.setItemData(processData, GXHDO102B013Const.SAMPLEJYUURYOU, getSrTenkaBetItemData(GXHDO102B013Const.SAMPLEJYUURYOU, srTenkaBet));

        // 前処理開始日
        this.setItemData(processData, GXHDO102B013Const.MAESYORIKAISI_DAY, getSrTenkaBetItemData(GXHDO102B013Const.MAESYORIKAISI_DAY, srTenkaBet));

        // 前処理開始時間
        this.setItemData(processData, GXHDO102B013Const.MAESYORIKAISI_TIME, getSrTenkaBetItemData(GXHDO102B013Const.MAESYORIKAISI_TIME, srTenkaBet));

        // 前処理終了日
        this.setItemData(processData, GXHDO102B013Const.MAESYORISYUURYOU_DAY, getSrTenkaBetItemData(GXHDO102B013Const.MAESYORISYUURYOU_DAY, srTenkaBet));

        // 前処理終了時間
        this.setItemData(processData, GXHDO102B013Const.MAESYORISYUURYOU_TIME, getSrTenkaBetItemData(GXHDO102B013Const.MAESYORISYUURYOU_TIME, srTenkaBet));

        // 前処理担当者
        this.setItemData(processData, GXHDO102B013Const.MAESYORITANTOUSYA, getSrTenkaBetItemData(GXHDO102B013Const.MAESYORITANTOUSYA, srTenkaBet));

        // 比表面積測定開始日
        this.setItemData(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY, getSrTenkaBetItemData(GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY, srTenkaBet));

        // 比表面積測定開始時間
        this.setItemData(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME, getSrTenkaBetItemData(GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME, srTenkaBet));

        // 比表面積測定終了日
        this.setItemData(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY, getSrTenkaBetItemData(GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY, srTenkaBet));

        // 比表面積測定終了時間
        this.setItemData(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME, getSrTenkaBetItemData(GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME, srTenkaBet));

        // 比表面積測定結果
        this.setItemData(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKEKKA, getSrTenkaBetItemData(GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKEKKA, srTenkaBet));

        // 判定
        this.setItemData(processData, GXHDO102B013Const.HANTEI, getSrTenkaBetItemData(GXHDO102B013Const.HANTEI, srTenkaBet));

        // 比表面積測定担当者
        this.setItemData(processData, GXHDO102B013Const.HIHYOUMENSEKISOKUTEITANTOUSYA, getSrTenkaBetItemData(GXHDO102B013Const.HIHYOUMENSEKISOKUTEITANTOUSYA, srTenkaBet));

    }

    /**
     * 添加材ｽﾗﾘｰ作製・BETの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 添加材ｽﾗﾘｰ作製・BET登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaBet> getSrTenkaBetData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTenkaBet(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrTenkaBet(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
     * [添加材ｽﾗﾘｰ作製・BET]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaBet> loadSrTenkaBet(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kansouzaranosyurui,"
                + "slurryjyuuryou,kansouondo,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,"
                + "dassirogouki,dassiondo,dassijikankikaku,dassikaisinichiji,dassisyuuryounichiji,kansoutantousya,"
                + "sokuteisample,samplejyuuryou,maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,"
                + "maesyoritantousya,hihyoumensekisokuteikaisinichiji,hihyoumensekisokuteisyuuryounichiji,"
                + "hihyoumensekisokuteikekka,hantei,hihyoumensekisokuteitantousya,torokunichiji,kosinnichiji,revision"
                + " FROM sr_tenka_bet "
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
        mapping.put("kojyo", "kojyo");                                                               // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                               // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                             // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                                 // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                                   // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                         // ﾛｯﾄ区分
        mapping.put("kansouzaranosyurui", "kansouzaranosyurui");                                     // 乾燥皿の種類
        mapping.put("slurryjyuuryou", "slurryjyuuryou");                                             // ｽﾗﾘｰ重量
        mapping.put("kansouondo", "kansouondo");                                                     // 乾燥温度
        mapping.put("kansoujikankikaku", "kansoujikankikaku");                                       // 乾燥時間規格
        mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                                     // 乾燥開始日時
        mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                               // 乾燥終了日時
        mapping.put("dassirogouki", "dassirogouki");                                                 // 脱脂炉号機
        mapping.put("dassiondo", "dassiondo");                                                       // 脱脂温度
        mapping.put("dassijikankikaku", "dassijikankikaku");                                         // 脱脂時間規格
        mapping.put("dassikaisinichiji", "dassikaisinichiji");                                       // 脱脂開始日時
        mapping.put("dassisyuuryounichiji", "dassisyuuryounichiji");                                 // 脱脂終了日時
        mapping.put("kansoutantousya", "kansoutantousya");                                           // 乾燥担当者
        mapping.put("sokuteisample", "sokuteisample");                                               // 測定ｻﾝﾌﾟﾙ数
        mapping.put("samplejyuuryou", "samplejyuuryou");                                             // ｻﾝﾌﾟﾙ重量
        mapping.put("maesyoriondo", "maesyoriondo");                                                 // 前処理温度
        mapping.put("maesyorijikan", "maesyorijikan");                                               // 前処理時間
        mapping.put("maesyorikaisinichiji", "maesyorikaisinichiji");                                 // 前処理開始日時
        mapping.put("maesyorisyuuryounichiji", "maesyorisyuuryounichiji");                           // 前処理終了日時
        mapping.put("maesyoritantousya", "maesyoritantousya");                                       // 前処理担当者
        mapping.put("hihyoumensekisokuteikaisinichiji", "hihyoumensekisokuteikaisinichiji");         // 比表面積測定開始日時
        mapping.put("hihyoumensekisokuteisyuuryounichiji", "hihyoumensekisokuteisyuuryounichiji");   // 比表面積測定終了日時
        mapping.put("hihyoumensekisokuteikekka", "hihyoumensekisokuteikekka");                       // 比表面積測定結果
        mapping.put("hantei", "hantei");                                                             // 判定
        mapping.put("hihyoumensekisokuteitantousya", "hihyoumensekisokuteitantousya");               // 比表面積測定担当者
        mapping.put("torokunichiji", "torokunichiji");                                               // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                 // 更新日時
        mapping.put("revision", "revision");                                                         // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaBet>> beanHandler = new BeanListHandler<>(SrTenkaBet.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [添加材ｽﾗﾘｰ作製・BET_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrTenkaBet> loadTmpSrTenkaBet(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kansouzaranosyurui,"
                + "slurryjyuuryou,kansouondo,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,"
                + "dassirogouki,dassiondo,dassijikankikaku,dassikaisinichiji,dassisyuuryounichiji,kansoutantousya,"
                + "sokuteisample,samplejyuuryou,maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,"
                + "maesyoritantousya,hihyoumensekisokuteikaisinichiji,hihyoumensekisokuteisyuuryounichiji,"
                + "hihyoumensekisokuteikekka,hantei,hihyoumensekisokuteitantousya,torokunichiji,kosinnichiji,revision,sakujyoflg "
                + " FROM tmp_sr_tenka_bet "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND sakujyoflg = ? ";

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
        mapping.put("kojyo", "kojyo");                                                               // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno");                                                               // ﾛｯﾄNo
        mapping.put("edaban", "edaban");                                                             // 枝番
        mapping.put("tenkazaislurryhinmei", "tenkazaislurryhinmei");                                 // 添加材ｽﾗﾘｰ品名
        mapping.put("tenkazaislurrylotno", "tenkazaislurrylotno");                                   // 添加材ｽﾗﾘｰLotNo
        mapping.put("lotkubun", "lotkubun");                                                         // ﾛｯﾄ区分
        mapping.put("kansouzaranosyurui", "kansouzaranosyurui");                                     // 乾燥皿の種類
        mapping.put("slurryjyuuryou", "slurryjyuuryou");                                             // ｽﾗﾘｰ重量
        mapping.put("kansouondo", "kansouondo");                                                     // 乾燥温度
        mapping.put("kansoujikankikaku", "kansoujikankikaku");                                       // 乾燥時間規格
        mapping.put("kansoukaisinichiji", "kansoukaisinichiji");                                     // 乾燥開始日時
        mapping.put("kansousyuuryounichiji", "kansousyuuryounichiji");                               // 乾燥終了日時
        mapping.put("dassirogouki", "dassirogouki");                                                 // 脱脂炉号機
        mapping.put("dassiondo", "dassiondo");                                                       // 脱脂温度
        mapping.put("dassijikankikaku", "dassijikankikaku");                                         // 脱脂時間規格
        mapping.put("dassikaisinichiji", "dassikaisinichiji");                                       // 脱脂開始日時
        mapping.put("dassisyuuryounichiji", "dassisyuuryounichiji");                                 // 脱脂終了日時
        mapping.put("kansoutantousya", "kansoutantousya");                                           // 乾燥担当者
        mapping.put("sokuteisample", "sokuteisample");                                               // 測定ｻﾝﾌﾟﾙ数
        mapping.put("samplejyuuryou", "samplejyuuryou");                                             // ｻﾝﾌﾟﾙ重量
        mapping.put("maesyoriondo", "maesyoriondo");                                                 // 前処理温度
        mapping.put("maesyorijikan", "maesyorijikan");                                               // 前処理時間
        mapping.put("maesyorikaisinichiji", "maesyorikaisinichiji");                                 // 前処理開始日時
        mapping.put("maesyorisyuuryounichiji", "maesyorisyuuryounichiji");                           // 前処理終了日時
        mapping.put("maesyoritantousya", "maesyoritantousya");                                       // 前処理担当者
        mapping.put("hihyoumensekisokuteikaisinichiji", "hihyoumensekisokuteikaisinichiji");         // 比表面積測定開始日時
        mapping.put("hihyoumensekisokuteisyuuryounichiji", "hihyoumensekisokuteisyuuryounichiji");   // 比表面積測定終了日時
        mapping.put("hihyoumensekisokuteikekka", "hihyoumensekisokuteikekka");                       // 比表面積測定結果
        mapping.put("hantei", "hantei");                                                             // 判定
        mapping.put("hihyoumensekisokuteitantousya", "hihyoumensekisokuteitantousya");               // 比表面積測定担当者
        mapping.put("torokunichiji", "torokunichiji");                                               // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji");                                                 // 更新日時
        mapping.put("revision", "revision");                                                         // revision
        mapping.put("sakujyoflg", "sakujyoflg");                                                     // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTenkaBet>> beanHandler = new BeanListHandler<>(SrTenkaBet.class, rowProcessor);

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
     * @param srTenkaBet 添加材ｽﾗﾘｰ作製・BETデータ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTenkaBet srTenkaBet) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTenkaBet != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaBetItemData(itemId, srTenkaBet);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlasshyoryo ｶﾞﾗｽ作製・秤量データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrTenkaBet srTenkaBet) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srTenkaBet != null) {
            // 元データが存在する場合元データより取得
            return getSrTenkaBetItemData(itemId, srTenkaBet);
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
     * 添加材ｽﾗﾘｰ作製・BET_仮登録(tmp_sr_tenka_bet)登録処理
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
    private void insertTmpSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_bet ("
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kansouzaranosyurui,slurryjyuuryou,"
                + "kansouondo,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,dassirogouki,dassiondo,"
                + "dassijikankikaku,dassikaisinichiji,dassisyuuryounichiji,kansoutantousya,sokuteisample,samplejyuuryou,"
                + "maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,maesyoritantousya,"
                + "hihyoumensekisokuteikaisinichiji,hihyoumensekisokuteisyuuryounichiji,hihyoumensekisokuteikekka,hantei,"
                + "hihyoumensekisokuteitantousya,torokunichiji,kosinnichiji,revision,sakujyoflg"
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrTenkaBet(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・BET_仮登録(tmp_sr_tenka_bet)更新処理
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
    private void updateTmpSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_tenka_bet SET "
                + " tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,kansouzaranosyurui = ?,slurryjyuuryou = ?,kansouondo = ?,"
                + "kansoujikankikaku = ?,kansoukaisinichiji = ?,kansousyuuryounichiji = ?,dassirogouki = ?,dassiondo = ?,dassijikankikaku = ?,"
                + "dassikaisinichiji = ?,dassisyuuryounichiji = ?,kansoutantousya = ?,sokuteisample = ?,samplejyuuryou = ?,maesyoriondo = ?,"
                + "maesyorijikan = ?,maesyorikaisinichiji = ?,maesyorisyuuryounichiji = ?,maesyoritantousya = ?,hihyoumensekisokuteikaisinichiji = ?,"
                + "hihyoumensekisokuteisyuuryounichiji = ?,hihyoumensekisokuteikekka = ?,hantei = ?,hihyoumensekisokuteitantousya = ?,"
                + "kosinnichiji = ?,revision = ?,sakujyoflg = ? "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaBet> srTenkaBetList = getSrTenkaBetData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaBet srTenkaBet = null;
        if (!srTenkaBetList.isEmpty()) {
            srTenkaBet = srTenkaBetList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTenkaBet(false, newRev, 0, "", "", "", systemTime, processData, srTenkaBet);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・BET_仮登録(tmp_sr_tenka_bet)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_tenka_bet "
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
     * 添加材ｽﾗﾘｰ作製・BET_仮登録(tmp_sr_tenka_bet)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaBet 添加材ｽﾗﾘｰ作製・BETデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTenkaBet(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaBet srTenkaBet) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 乾燥開始時間
        String kansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.KANSOUKAISI_TIME, srTenkaBet));
        // 乾燥終了時間
        String kansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.KANSOUSYUURYOU_TIME, srTenkaBet));
        // 脱脂開始時間
        String dassikaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.DASSIKAISI_TIME, srTenkaBet));
        // 脱脂終了時間
        String dassisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.DASSISYUURYOU_TIME, srTenkaBet));
        // 前処理開始時間
        String maesyorikaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.MAESYORIKAISI_TIME, srTenkaBet));
        // 前処理終了時間
        String maesyorisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.MAESYORISYUURYOU_TIME, srTenkaBet));
        // 比表面積測定開始時間
        String hihyoumensekisokuteikaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME, srTenkaBet));
        // 比表面積測定終了時間
        String hihyoumensekisokuteisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME, srTenkaBet));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.TENKAZAISLURRYHINMEI, srTenkaBet))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.TENKAZAISLURRYLOTNO, srTenkaBet))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.LOTKUBUN, srTenkaBet))); // ﾛｯﾄ区分
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.KANSOUZARANOSYURUI, srTenkaBet), null)); // 乾燥皿の種類
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.SLURRYJYUURYOU, srTenkaBet))); // ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B013Const.KANSOUONDO, srTenkaBet))); // 乾燥温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B013Const.KANSOUJIKANKIKAKU, srTenkaBet))); // 乾燥時間規格
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.KANSOUKAISI_DAY, srTenkaBet),
                "".equals(kansoukaisiTime) ? "0000" : kansoukaisiTime)); // 乾燥開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.KANSOUSYUURYOU_DAY, srTenkaBet),
                "".equals(kansousyuuryouTime) ? "0000" : kansousyuuryouTime)); // 乾燥終了日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.DASSIROGOUKI, srTenkaBet), null)); // 脱脂炉号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B013Const.DASSIONDO, srTenkaBet))); // 脱脂温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B013Const.DASSIJIKANKIKAKU, srTenkaBet))); // 脱脂時間規格
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.DASSIKAISI_DAY, srTenkaBet),
                "".equals(dassikaisiTime) ? "0000" : dassikaisiTime)); // 脱脂開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.DASSISYUURYOU_DAY, srTenkaBet),
                "".equals(dassisyuuryouTime) ? "0000" : dassisyuuryouTime)); // 脱脂終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.KANSOUTANTOUSYA, srTenkaBet))); // 乾燥担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.SOKUTEISAMPLE, srTenkaBet), null)); // 測定ｻﾝﾌﾟﾙ数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.SAMPLEJYUURYOU, srTenkaBet), null)); // ｻﾝﾌﾟﾙ重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B013Const.MAESYORIONDO, srTenkaBet))); // 前処理温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B013Const.MAESYORIJIKAN, srTenkaBet))); // 前処理時間
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.MAESYORIKAISI_DAY, srTenkaBet),
                "".equals(maesyorikaisiTime) ? "0000" : maesyorikaisiTime)); // 前処理開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.MAESYORISYUURYOU_DAY, srTenkaBet),
                "".equals(maesyorisyuuryouTime) ? "0000" : maesyorisyuuryouTime)); // 前処理終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.MAESYORITANTOUSYA, srTenkaBet))); // 前処理担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY, srTenkaBet),
                "".equals(hihyoumensekisokuteikaisiTime) ? "0000" : hihyoumensekisokuteikaisiTime)); // 比表面積測定開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY, srTenkaBet),
                "".equals(hihyoumensekisokuteisyuuryouTime) ? "0000" : hihyoumensekisokuteisyuuryouTime)); // 比表面積測定終了日時
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKEKKA, srTenkaBet))); // 比表面積測定結果
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B013Const.HANTEI, srTenkaBet), null)); // 判定
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEITANTOUSYA, srTenkaBet))); // 比表面積測定担当者

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
     * 添加材ｽﾗﾘｰ作製・BET(sr_tenka_bet)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrTenkaBet 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrTenkaBet tmpSrTenkaBet) throws SQLException {

        String sql = "INSERT INTO sr_tenka_bet ("
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kansouzaranosyurui,"
                + "slurryjyuuryou,kansouondo,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,"
                + "dassirogouki,dassiondo,dassijikankikaku,dassikaisinichiji,dassisyuuryounichiji,kansoutantousya,"
                + "sokuteisample,samplejyuuryou,maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,"
                + "maesyoritantousya,hihyoumensekisokuteikaisinichiji,hihyoumensekisokuteisyuuryounichiji,"
                + "hihyoumensekisokuteikekka,hantei,hihyoumensekisokuteitantousya,torokunichiji,kosinnichiji,revision"
                + " ) VALUES ( "
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrTenkaBet(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrTenkaBet);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・BET(sr_tenka_bet)更新処理
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
    private void updateSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_tenka_bet SET "
                + " tenkazaislurryhinmei = ?,tenkazaislurrylotno = ?,lotkubun = ?,kansouzaranosyurui = ?,slurryjyuuryou = ?,kansouondo = ?,"
                + "kansoujikankikaku = ?,kansoukaisinichiji = ?,kansousyuuryounichiji = ?,dassirogouki = ?,dassiondo = ?,dassijikankikaku = ?,"
                + "dassikaisinichiji = ?,dassisyuuryounichiji = ?,kansoutantousya = ?,sokuteisample = ?,samplejyuuryou = ?,maesyoriondo = ?,"
                + "maesyorijikan = ?,maesyorikaisinichiji = ?,maesyorisyuuryounichiji = ?,maesyoritantousya = ?,hihyoumensekisokuteikaisinichiji = ?,"
                + "hihyoumensekisokuteisyuuryounichiji = ?,hihyoumensekisokuteikekka = ?,hantei = ?,hihyoumensekisokuteitantousya = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrTenkaBet> srTenkaBetList = getSrTenkaBetData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrTenkaBet srTenkaBet = null;
        if (!srTenkaBetList.isEmpty()) {
            srTenkaBet = srTenkaBetList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTenkaBet(false, newRev, "", "", "", systemTime, processData, srTenkaBet);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 添加材ｽﾗﾘｰ作製・BET(sr_tenka_bet)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srTenkaBet 添加材ｽﾗﾘｰ作製・BETデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTenkaBet(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrTenkaBet srTenkaBet) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 乾燥開始時間
        String kansoukaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.KANSOUKAISI_TIME, srTenkaBet));
        // 乾燥終了時間
        String kansousyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.KANSOUSYUURYOU_TIME, srTenkaBet));
        // 脱脂開始時間
        String dassikaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.DASSIKAISI_TIME, srTenkaBet));
        // 脱脂終了時間
        String dassisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.DASSISYUURYOU_TIME, srTenkaBet));
        // 前処理開始時間
        String maesyorikaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.MAESYORIKAISI_TIME, srTenkaBet));
        // 前処理終了時間
        String maesyorisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.MAESYORISYUURYOU_TIME, srTenkaBet));
        // 比表面積測定開始時間
        String hihyoumensekisokuteikaisiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME, srTenkaBet));
        // 比表面積測定終了時間
        String hihyoumensekisokuteisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME, srTenkaBet));
        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }

        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B013Const.TENKAZAISLURRYHINMEI, srTenkaBet))); // 添加材ｽﾗﾘｰ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B013Const.TENKAZAISLURRYLOTNO, srTenkaBet))); // 添加材ｽﾗﾘｰLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B013Const.LOTKUBUN, srTenkaBet))); // ﾛｯﾄ区分
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.KANSOUZARANOSYURUI, srTenkaBet), 9)); // 乾燥皿の種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B013Const.SLURRYJYUURYOU, srTenkaBet))); // ｽﾗﾘｰ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B013Const.KANSOUONDO, srTenkaBet))); // 乾燥温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B013Const.KANSOUJIKANKIKAKU, srTenkaBet))); // 乾燥時間規格
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.KANSOUKAISI_DAY, srTenkaBet),
                "".equals(kansoukaisiTime) ? "0000" : kansoukaisiTime)); // 乾燥開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.KANSOUSYUURYOU_DAY, srTenkaBet),
                "".equals(kansousyuuryouTime) ? "0000" : kansousyuuryouTime)); // 乾燥終了日時
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.DASSIROGOUKI, srTenkaBet), 9)); // 脱脂炉号機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B013Const.DASSIONDO, srTenkaBet))); // 脱脂温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B013Const.DASSIJIKANKIKAKU, srTenkaBet))); // 脱脂時間規格
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.DASSIKAISI_DAY, srTenkaBet),
                "".equals(dassikaisiTime) ? "0000" : dassikaisiTime)); // 脱脂開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.DASSISYUURYOU_DAY, srTenkaBet),
                "".equals(dassisyuuryouTime) ? "0000" : dassisyuuryouTime)); // 脱脂終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B013Const.KANSOUTANTOUSYA, srTenkaBet))); // 乾燥担当者
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.SOKUTEISAMPLE, srTenkaBet), 9)); // 測定ｻﾝﾌﾟﾙ数
        params.add(getCheckBoxDbValue(getItemData(pItemList, GXHDO102B013Const.SAMPLEJYUURYOU, srTenkaBet), 9)); // ｻﾝﾌﾟﾙ重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B013Const.MAESYORIONDO, srTenkaBet))); // 前処理温度
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B013Const.MAESYORIJIKAN, srTenkaBet))); // 前処理時間
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.MAESYORIKAISI_DAY, srTenkaBet),
                "".equals(maesyorikaisiTime) ? "0000" : maesyorikaisiTime)); // 前処理開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.MAESYORISYUURYOU_DAY, srTenkaBet),
                "".equals(maesyorisyuuryouTime) ? "0000" : maesyorisyuuryouTime)); // 前処理終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B013Const.MAESYORITANTOUSYA, srTenkaBet))); // 前処理担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY, srTenkaBet),
                "".equals(hihyoumensekisokuteikaisiTime) ? "0000" : hihyoumensekisokuteikaisiTime)); // 比表面積測定開始日時
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY, srTenkaBet),
                "".equals(hihyoumensekisokuteisyuuryouTime) ? "0000" : hihyoumensekisokuteisyuuryouTime)); // 比表面積測定終了日時
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKEKKA, srTenkaBet))); // 比表面積測定結果
        params.add(getComboBoxDbValue(getItemData(pItemList, GXHDO102B013Const.HANTEI, srTenkaBet), 9)); // 判定
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B013Const.HIHYOUMENSEKISOKUTEITANTOUSYA, srTenkaBet))); // 比表面積測定担当者

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
     * 添加材ｽﾗﾘｰ作製・BET(sr_tenka_bet)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_tenka_bet "
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
     * [添加材ｽﾗﾘｰ作製・BET_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT MAX(sakujyoflg) AS deleteflag "
                + "FROM tmp_sr_tenka_bet "
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
     * @param srTenkaBet 添加材ｽﾗﾘｰ作製・BETデータ
     * @return DB値
     */
    private String getSrTenkaBetItemData(String itemId, SrTenkaBet srTenkaBet) {
        switch (itemId) {
            // 添加材ｽﾗﾘｰ品名
            case GXHDO102B013Const.TENKAZAISLURRYHINMEI:
                return StringUtil.nullToBlank(srTenkaBet.getTenkazaislurryhinmei());

            // 添加材ｽﾗﾘｰLotNo
            case GXHDO102B013Const.TENKAZAISLURRYLOTNO:
                return StringUtil.nullToBlank(srTenkaBet.getTenkazaislurrylotno());

            // ﾛｯﾄ区分
            case GXHDO102B013Const.LOTKUBUN:
                return StringUtil.nullToBlank(srTenkaBet.getLotkubun());

            // 乾燥皿の種類
            case GXHDO102B013Const.KANSOUZARANOSYURUI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaBet.getKansouzaranosyurui()));

            // ｽﾗﾘｰ重量
            case GXHDO102B013Const.SLURRYJYUURYOU:
                return StringUtil.nullToBlank(srTenkaBet.getSlurryjyuuryou());

            // 乾燥開始日
            case GXHDO102B013Const.KANSOUKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getKansoukaisinichiji(), "yyMMdd");

            // 乾燥開始時間
            case GXHDO102B013Const.KANSOUKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getKansoukaisinichiji(), "HHmm");

            // 乾燥終了日
            case GXHDO102B013Const.KANSOUSYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getKansousyuuryounichiji(), "yyMMdd");

            // 乾燥終了時間
            case GXHDO102B013Const.KANSOUSYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getKansousyuuryounichiji(), "HHmm");

            // 脱脂炉号機
            case GXHDO102B013Const.DASSIROGOUKI:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaBet.getDassirogouki()));

            // 脱脂開始日
            case GXHDO102B013Const.DASSIKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getDassikaisinichiji(), "yyMMdd");

            // 脱脂開始時間
            case GXHDO102B013Const.DASSIKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getDassikaisinichiji(), "HHmm");

            // 脱脂終了日
            case GXHDO102B013Const.DASSISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getDassisyuuryounichiji(), "yyMMdd");

            // 脱脂終了時間
            case GXHDO102B013Const.DASSISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getDassisyuuryounichiji(), "HHmm");

            // 乾燥担当者
            case GXHDO102B013Const.KANSOUTANTOUSYA:
                return StringUtil.nullToBlank(srTenkaBet.getKansoutantousya());

            // 測定ｻﾝﾌﾟﾙ数
            case GXHDO102B013Const.SOKUTEISAMPLE:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaBet.getSokuteisample()));

            // ｻﾝﾌﾟﾙ重量
            case GXHDO102B013Const.SAMPLEJYUURYOU:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srTenkaBet.getSamplejyuuryou()));

            // 前処理開始日
            case GXHDO102B013Const.MAESYORIKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getMaesyorikaisinichiji(), "yyMMdd");

            // 前処理開始時間
            case GXHDO102B013Const.MAESYORIKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getMaesyorikaisinichiji(), "HHmm");

            // 前処理終了日
            case GXHDO102B013Const.MAESYORISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getMaesyorisyuuryounichiji(), "yyMMdd");

            // 前処理終了時間
            case GXHDO102B013Const.MAESYORISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getMaesyorisyuuryounichiji(), "HHmm");

            // 前処理担当者
            case GXHDO102B013Const.MAESYORITANTOUSYA:
                return StringUtil.nullToBlank(srTenkaBet.getMaesyoritantousya());

            // 比表面積測定開始日
            case GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getHihyoumensekisokuteikaisinichiji(), "yyMMdd");

            // 比表面積測定開始時間
            case GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKAISI_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getHihyoumensekisokuteikaisinichiji(), "HHmm");

            // 比表面積測定終了日
            case GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srTenkaBet.getHihyoumensekisokuteisyuuryounichiji(), "yyMMdd");

            // 比表面積測定終了時間
            case GXHDO102B013Const.HIHYOUMENSEKISOKUTEISYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srTenkaBet.getHihyoumensekisokuteisyuuryounichiji(), "HHmm");

            // 比表面積測定結果
            case GXHDO102B013Const.HIHYOUMENSEKISOKUTEIKEKKA:
                return StringUtil.nullToBlank(srTenkaBet.getHihyoumensekisokuteikekka());

            // 判定
            case GXHDO102B013Const.HANTEI:
                return getComboBoxCheckValue(StringUtil.nullToBlank(srTenkaBet.getHantei()));

            // 比表面積測定担当者
            case GXHDO102B013Const.HIHYOUMENSEKISOKUTEITANTOUSYA:
                return StringUtil.nullToBlank(srTenkaBet.getHihyoumensekisokuteitantousya());

            default:
                return null;
        }
    }

    /**
     * 添加材ｽﾗﾘｰ作製・BET_仮登録(tmp_sr_tenka_bet)登録処理(削除時)
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
    private void insertDeleteDataTmpSrTenkaBet(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_tenka_bet ( "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kansouzaranosyurui,"
                + "slurryjyuuryou,kansouondo,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,"
                + "dassirogouki,dassiondo,dassijikankikaku,dassikaisinichiji,dassisyuuryounichiji,kansoutantousya,"
                + "sokuteisample,samplejyuuryou,maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,"
                + "maesyoritantousya,hihyoumensekisokuteikaisinichiji,hihyoumensekisokuteisyuuryounichiji,"
                + "hihyoumensekisokuteikekka,hantei,hihyoumensekisokuteitantousya,torokunichiji,kosinnichiji,revision,sakujyoflg"
                + ") SELECT "
                + " kojyo,lotno,edaban,tenkazaislurryhinmei,tenkazaislurrylotno,lotkubun,kansouzaranosyurui,"
                + "slurryjyuuryou,kansouondo,kansoujikankikaku,kansoukaisinichiji,kansousyuuryounichiji,"
                + "dassirogouki,dassiondo,dassijikankikaku,dassikaisinichiji,dassisyuuryounichiji,kansoutantousya,"
                + "sokuteisample,samplejyuuryou,maesyoriondo,maesyorijikan,maesyorikaisinichiji,maesyorisyuuryounichiji,"
                + "maesyoritantousya,hihyoumensekisokuteikaisinichiji,hihyoumensekisokuteisyuuryounichiji,"
                + "hihyoumensekisokuteikekka,hantei,hihyoumensekisokuteitantousya,"
                + " ?,?,?,? "
                + " FROM sr_tenka_bet "
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
            return "追加粉砕";
        } else if ("1".equals(dbValue)) {
            return "OK";
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
        if ("追加粉砕".equals(StringUtil.nullToBlank(checkBoxValue))) {
            return 0;
        } else if ("OK".equals(StringUtil.nullToBlank(checkBoxValue))) {
            return 1;
        }
        return defaultValue;
    }
}
