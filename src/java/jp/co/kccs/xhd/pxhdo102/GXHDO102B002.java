/*
 * Copyright 2021 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo102;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
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
import jp.co.kccs.xhd.db.model.SrGlassscfunsai;
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
 * 変更日	2021/08/31<br>
 * 計画書No	MB2101-DK002<br>
 * 変更者	KCSS K.Jo<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO102B002(ｶﾞﾗｽ作製・SC粉砕)
 *
 * @author KCSS K.Jo
 * @since  2021/08/31
 */
public class GXHDO102B002 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO102B002.class.getName());
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
    public GXHDO102B002() {
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
                    GXHDO102B002Const.BTN_EDABAN_COPY_TOP,
                    GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_TOP,
                    GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_TOP,
                    GXHDO102B002Const.BTN_EDABAN_COPY_BOTTOM,
                    GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_BOTTOM,
                    GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO102B002Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO102B002Const.BTN_INSERT_TOP,
                    GXHDO102B002Const.BTN_DELETE_TOP,
                    GXHDO102B002Const.BTN_UPDATE_TOP,
                    GXHDO102B002Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO102B002Const.BTN_INSERT_BOTTOM,
                    GXHDO102B002Const.BTN_DELETE_BOTTOM,
                    GXHDO102B002Const.BTN_UPDATE_BOTTOM
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
            case GXHDO102B002Const.BTN_EDABAN_COPY_TOP:
            case GXHDO102B002Const.BTN_EDABAN_COPY_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 粉砕開始日時
            case GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_TOP:
            case GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_BOTTOM:
                method = "setFunsaikaisinichiji";
                break;
            // 粉砕終了日時
            case GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_TOP:
            case GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM:
                method = "setFunsaisyuuryounichiji";
                break;
            // 仮登録
            case GXHDO102B002Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO102B002Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO102B002Const.BTN_INSERT_TOP:
            case GXHDO102B002Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 修正
            case GXHDO102B002Const.BTN_UPDATE_TOP:
            case GXHDO102B002Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO102B002Const.BTN_DELETE_TOP:
            case GXHDO102B002Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
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
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            int paramJissekino = (Integer) session.getAttribute("jissekino");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);

            //仕掛情報の取得
            //Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            //1.前工程WIPから仕掛情報を取得する。 
            //  仮データ
            Map<String, String> shikakariData = new HashMap<>();
            shikakariData.put("oyalotedaban", "006");
            
            if (shikakariData == null || shikakariData.isEmpty() || !shikakariData.containsKey("oyalotedaban")) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // (6)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, paramJissekino, formId);
            if (fxhdd11RevInfo == null || fxhdd11RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // ｶﾞﾗｽ作製・SC粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
            List<SrGlassscfunsai> srGlassscfunsaiDataList = getSrGlassscfunsaiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srGlassscfunsaiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srGlassscfunsaiDataList.get(0));

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
     * 粉砕開始日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFunsaikaisinichiji(ProcessData processData) {
        // 粉砕開始日
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B002Const.FUNSAI_KAISHI_DAY);
        // 粉砕開始時間
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B002Const.FUNSAI_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());

            //「粉砕時間」
            FXHDD01 funsaijikan = getItemRow(processData.getItemList(), GXHDO102B002Const.FUNSAIJIKAN);
            String strFunsaijikan = funsaijikan.getKikakuChi();
            strFunsaijikan = strFunsaijikan.replace("【", "");
            strFunsaijikan = strFunsaijikan.replace("】", "");

            // 粉砕終了予定日    粉砕開始日+粉砕開始時間+粉砕時間(YYMMDD)で算出した日時の日付け部 
            // 粉砕終了予定時間  粉砕開始日+粉砕開始時間+粉砕時間(YYMMDD)で算出した日時の時間部 
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
                String strFunsaiKaishiDay = itemDay.getValue();
                String strFunsaiKaishiTime = itemTime.getValue();
                String strFunsaiDatetime = strFunsaiKaishiDay + strFunsaiKaishiTime;
                Date dtFunsaiDatetime = dateFormat.parse(strFunsaiDatetime);
                Calendar calFunsai = Calendar.getInstance();
                calFunsai.setTime(dtFunsaiDatetime);
                calFunsai.add(Calendar.HOUR, Integer.parseInt(strFunsaijikan));
                Date resultDT = calFunsai.getTime();
                String retFunsaiDateTime = dateFormat.format(resultDT);

                //「粉砕終了予定日」を設定する
                this.setItemData(processData, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_DAY, retFunsaiDateTime.substring(0, 6));
                //「粉砕終了予定時間」を設定する
                this.setItemData(processData, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_TIME, retFunsaiDateTime.substring(6, 10));
            } catch (ParseException ex) {
                ErrUtil.outputErrorLog("ParseException発生", ex, LOGGER);
            }
        }
        processData.setMethod("");
        return processData;
    }

    /**
     * 粉砕終了日時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setFunsaisyuuryounichiji(ProcessData processData) {
        FXHDD01 itemDay = getItemRow(processData.getItemList(), GXHDO102B002Const.FUNSAI_SYUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processData.getItemList(), GXHDO102B002Const.FUNSAI_SYUURYOU_TIME);
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
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime, jissekiNo);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // ｶﾞﾗｽ作製・SC粉砕_仮登録登録処理
                insertTmpSrGlassscfunsai(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekiNo, strSystime, processData, formId);
            } else {

                // ｶﾞﾗｽ作製・SC粉砕_仮登録更新処理
                updateTmpSrGlassscfunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, strSystime, processData);
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
        processData.setMethod("doResist");

        return processData;
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
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
                insertFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

                // 原材料品質DB登録実績更新処理
                updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrGlassscfunsai tmpSrGlassscfunsai = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrGlassscfunsai> srGlassscfunsaiList = getSrGlassscfunsaiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srGlassscfunsaiList.isEmpty()) {
                    tmpSrGlassscfunsai = srGlassscfunsaiList.get(0);
                }

                deleteTmpSrGlassscfunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // ｶﾞﾗｽ作製・SC粉砕_登録処理
            insertSrGlassscfunsai(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, strSystime, processData, tmpSrGlassscfunsai, formId);

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
        processData.setUserAuthParam(GXHDO102B002Const.USER_AUTH_UPDATE_PARAM);

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
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime, jissekiNo);

            // ｶﾞﾗｽ作製・SC粉砕_更新処理
            updateSrGlassscfunsai(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, strSystime, processData);

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
        processData.setUserAuthParam(GXHDO102B002Const.USER_AUTH_DELETE_PARAM);

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

            // 原材料品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd11RevInfo = loadFxhdd11RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, paramJissekino, formId);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            String strSystime = sdf.format(systemTime);
            // 原材料品質DB登録実績更新処理
            updateFxhdd11(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime, paramJissekino);

            // ｶﾞﾗｽ作製・SC粉砕_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrGlassscfunsai(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, strSystime);

            // ｶﾞﾗｽ作製・SC粉砕_削除処理
            deleteSrGlassscfunsai(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                        GXHDO102B002Const.BTN_UPDATE_TOP,
                        GXHDO102B002Const.BTN_DELETE_TOP,
                        GXHDO102B002Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_TOP,
                        GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_TOP,
                        GXHDO102B002Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B002Const.BTN_DELETE_BOTTOM,
                        GXHDO102B002Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_BOTTOM,
                        GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B002Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B002Const.BTN_INSERT_TOP,
                        GXHDO102B002Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B002Const.BTN_INSERT_BOTTOM));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO102B002Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO102B002Const.BTN_INSERT_TOP,
                        GXHDO102B002Const.BTN_EDABAN_COPY_TOP,
                        GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_TOP,
                        GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_TOP,
                        GXHDO102B002Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO102B002Const.BTN_INSERT_BOTTOM,
                        GXHDO102B002Const.BTN_EDABAN_COPY_BOTTOM,
                        GXHDO102B002Const.BTN_FUNSAIKAISINICHIJI_BOTTOM,
                        GXHDO102B002Const.BTN_FUNSAISYUURYOUNICHIJI_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO102B002Const.BTN_UPDATE_TOP,
                        GXHDO102B002Const.BTN_DELETE_TOP,
                        GXHDO102B002Const.BTN_UPDATE_BOTTOM,
                        GXHDO102B002Const.BTN_DELETE_BOTTOM
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
        
        // ②仕掛情報取得処理
        //Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
        //1.前工程WIPから仕掛情報を取得する。 
        //  仮データ
        Map<String, String> shikakariData = new HashMap<>();
        shikakariData.put("hinmei", "品名123");
        shikakariData.put("lotkubuncode", "2002");
        shikakariData.put("lotno", "82001240");
        shikakariData.put("oyalotedaban", "006");

        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029"));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); // ﾛｯﾄ区分ｺｰﾄﾞ
        String hinmei = StringUtil.nullToBlank(getMapData(shikakariData, "hinmei"));// 品名
        String oyalotedaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban"));// 親ﾛｯﾄ枝番
        String lotno = StringUtil.nullToBlank(getMapData(shikakariData, "lotno"));// LotNo
        Map hiddenMap = processData.getHiddenDataMap();
        hiddenMap.put("lotkubuncode", lotkubuncode);
        hiddenMap.put("hinmei", hinmei);
        hiddenMap.put("oyalotedaban", oyalotedaban);
        hiddenMap.put("lotno", lotno);

        // ﾛｯﾄ区分ﾏｽﾀ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerWip, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015"));
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, paramJissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, shikakariData, lotNo);
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
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map shikakariData, String lotNo) {
        // ロットNo
        this.setItemData(processData, GXHDO102B002Const.WIPLOTNO, lotNo);
        // ｶﾞﾗｽ品名
        this.setItemData(processData, GXHDO102B002Const.GLASSHINMEI, StringUtil.nullToBlank(getMapData(shikakariData, "hinmei")));
        // ｶﾞﾗｽLotNo
        this.setItemData(processData, GXHDO102B002Const.GLASSLOTNO, StringUtil.nullToBlank(getMapData(shikakariData, "lotno")));
        // ﾛｯﾄ区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode"));
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO102B002Const.LOTKUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO102B002Const.LOTKUBUN, lotkubuncode + ":" + lotKubun);
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

        List<SrGlassscfunsai> srGlassscfunsaiList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // (3)[原材料品質DB登録実績]から、ﾃﾞｰﾀを取得
            Map fxhdd11RevInfo = loadFxhdd11RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, jissekino, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd11RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                // 【Ⅵ.画面項目制御・出力仕様.初期表示時①】を元に画面表示を行う。
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // 画面にデータを設定する(デフォルト値)
                for (FXHDD01 fxhdd001 : processData.getItemList()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }

                return true;
            }

            // ｶﾞﾗｽ作製・SC粉砕データ取得
            srGlassscfunsaiList = getSrGlassscfunsaiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srGlassscfunsaiList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srGlassscfunsaiList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srGlassscfunsaiList.get(0));

        return true;

    }

    /**
     * データ設定処理
     *
     * @param processData 処理制御データ
     * @param srGlassscfunsai ｶﾞﾗｽ作製・SC粉砕データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrGlassscfunsai srGlassscfunsai) {
        
        // 累計稼働時間
        this.setItemData(processData, GXHDO102B002Const.KADOUJIKAN, getSrGlassscfunsaiItemData(GXHDO102B002Const.KADOUJIKAN, srGlassscfunsai));
        // ｼｰﾙ液量確認
        this.setItemData(processData, GXHDO102B002Const.SI_RUEKIKAKUNIN, getSrGlassscfunsaiItemData(GXHDO102B002Const.SI_RUEKIKAKUNIN, srGlassscfunsai));
        // 粉砕開始日
        this.setItemData(processData, GXHDO102B002Const.FUNSAI_KAISHI_DAY, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUNSAI_KAISHI_DAY, srGlassscfunsai));
        // 粉砕開始時間
        this.setItemData(processData, GXHDO102B002Const.FUNSAI_KAISHI_TIME, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUNSAI_KAISHI_TIME, srGlassscfunsai));
        // 粉砕開始担当者
        this.setItemData(processData, GXHDO102B002Const.KAISITANTOSYA, getSrGlassscfunsaiItemData(GXHDO102B002Const.KAISITANTOSYA, srGlassscfunsai));
        // 粉砕終了予定日
        this.setItemData(processData, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_DAY, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_DAY, srGlassscfunsai));
        // 粉砕終了予定時間
        this.setItemData(processData, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_TIME, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_TIME, srGlassscfunsai));
        // 負荷電流値
        this.setItemData(processData, GXHDO102B002Const.FUKADENRYUUTI_10MIN, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUKADENRYUUTI_10MIN, srGlassscfunsai));
        // ﾐﾙ出口温度
        this.setItemData(processData, GXHDO102B002Const.MIRUDEGUTIEKION_10MIN, getSrGlassscfunsaiItemData(GXHDO102B002Const.MIRUDEGUTIEKION_10MIN, srGlassscfunsai));
        // 内圧
        this.setItemData(processData, GXHDO102B002Const.NAIATU_10MIN, getSrGlassscfunsaiItemData(GXHDO102B002Const.NAIATU_10MIN, srGlassscfunsai));
        // ﾎﾟﾝﾌﾟ周波数
        this.setItemData(processData, GXHDO102B002Const.PONPUSYUUHASUU_10MIN, getSrGlassscfunsaiItemData(GXHDO102B002Const.PONPUSYUUHASUU_10MIN, srGlassscfunsai));
        // 粉砕終了日
        this.setItemData(processData, GXHDO102B002Const.FUNSAI_SYUURYOU_DAY, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUNSAI_SYUURYOU_DAY, srGlassscfunsai));
        // 粉砕終了時間
        this.setItemData(processData, GXHDO102B002Const.FUNSAI_SYUURYOU_TIME, getSrGlassscfunsaiItemData(GXHDO102B002Const.FUNSAI_SYUURYOU_TIME, srGlassscfunsai));
        // 粉砕終了担当者
        this.setItemData(processData, GXHDO102B002Const.SYURYOTANTOSYA, getSrGlassscfunsaiItemData(GXHDO102B002Const.SYURYOTANTOSYA, srGlassscfunsai));
        // 備考1
        this.setItemData(processData, GXHDO102B002Const.BIKOU1, getSrGlassscfunsaiItemData(GXHDO102B002Const.BIKOU1, srGlassscfunsai));
        // 備考2
        this.setItemData(processData, GXHDO102B002Const.BIKOU2, getSrGlassscfunsaiItemData(GXHDO102B002Const.BIKOU2, srGlassscfunsai));
    }

    /**
     * ｶﾞﾗｽ作製・SC粉砕の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return ｶﾞﾗｽ作製・SC粉砕登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassscfunsai> getSrGlassscfunsaiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrGlassscfunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrGlassscfunsai(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        return CommonUtil.getMkSekkeiInfo(queryRunnerQcdb, queryRunnerWip, lotNo1, lotNo2, "001");
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
     * 前工程WIPから仕掛情報を取得する。
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
        String sql = "SELECT kcpno, lotno, oyalotedaban, tokuisaki, lotkubuncode, ownercode, tanijuryo "
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map shikakariData = queryRunnerWip.query(sql, new MapHandler(), params.toArray());
        // TODO
        // 前工程WIPから取得した品名
        shikakariData.put("hinmei", "品名123");

        return shikakariData;
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
        String sql = "SELECT rev "
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
     * [ｶﾞﾗｽ作製・SC粉砕]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassscfunsai> loadSrGlassscfunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,glasshinmei,glasslotno,lotkubun,funsaiki,tamaishikei,tamaishijuryo,kaitensuu,"
                + " junkanshuhasuu,funsaijikan,kadoujikan,si_ruekikakunin,funsaikaisinichiji,kaisitantosya,funsaisyuuryouyoteinichiji, "
                + " fukadenryuuti_10min,mirudegutiekion_10min,naiatu_10min,ponpusyuuhasuu_10min,funsaisyuuryounichiji,syuryotantosya, "
                + " bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " FROM sr_glassscfunsai "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("glasshinmei", "glasshinmei"); // ｶﾞﾗｽ品名
        mapping.put("glasslotno", "glasslotno"); // ｶﾞﾗｽ品名LotNo
        mapping.put("lotkubun", "lotkubun"); // ﾛｯﾄ区分
        mapping.put("funsaiki", "funsaiki"); // 粉砕機
        mapping.put("tamaishikei", "tamaishikei"); // 玉石径
        mapping.put("tamaishijuryo", "tamaishijuryo"); // 玉石重量
        mapping.put("kaitensuu", "kaitensuu"); // 回転数
        mapping.put("junkanshuhasuu", "junkanshuhasuu"); // 循環周波数
        mapping.put("funsaijikan", "funsaijikan"); // 粉砕時間
        mapping.put("kadoujikan", "kadoujikan"); // 累計稼働時間
        mapping.put("si_ruekikakunin", "siruekikakunin"); // ｼｰﾙ液量確認
        mapping.put("funsaikaisinichiji", "funsaikaisinichiji"); // 粉砕開始日時
        mapping.put("kaisitantosya", "kaisitantosya"); // 粉砕開始担当者
        mapping.put("funsaisyuuryouyoteinichiji", "funsaisyuuryouyoteinichiji"); // 粉砕終了予定日時
        mapping.put("fukadenryuuti_10min", "fukadenryuuti10min"); // 負荷電流値
        mapping.put("mirudegutiekion_10min", "mirudegutiekion10min"); // ﾐﾙ出口液温
        mapping.put("naiatu_10min", "naiatu10min"); // 内圧
        mapping.put("ponpusyuuhasuu_10min", "ponpusyuuhasuu10min"); // ﾎﾟﾝﾌﾟ周波数
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji"); // 粉砕終了日時
        mapping.put("syuryotantosya", "syuryotantosya"); // 粉砕終了担当者
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGlassscfunsai>> beanHandler = new BeanListHandler<>(SrGlassscfunsai.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [ｶﾞﾗｽ作製・SC粉砕_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrGlassscfunsai> loadTmpSrGlassscfunsai(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = " SELECT "
                + " kojyo,lotno,edaban,glasshinmei,glasslotno,lotkubun,funsaiki,tamaishikei,tamaishijuryo,kaitensuu, "
                + " junkanshuhasuu,funsaijikan,kadoujikan,si_ruekikakunin,funsaikaisinichiji,kaisitantosya,funsaisyuuryouyoteinichiji, "
                + " fukadenryuuti_10min,mirudegutiekion_10min,naiatu_10min,ponpusyuuhasuu_10min,funsaisyuuryounichiji,syuryotantosya, "
                + " bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " FROM tmp_sr_glassscfunsai "
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
        mapping.put("kojyo", "kojyo"); // 工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); // ﾛｯﾄNo
        mapping.put("edaban", "edaban"); // 枝番
        mapping.put("glasshinmei", "glasshinmei"); // ｶﾞﾗｽ品名
        mapping.put("glasslotno", "glasslotno"); // ｶﾞﾗｽ品名LotNo
        mapping.put("lotkubun", "lotkubun"); // ﾛｯﾄ区分
        mapping.put("funsaiki", "funsaiki"); // 粉砕機
        mapping.put("tamaishikei", "tamaishikei"); // 玉石径
        mapping.put("tamaishijuryo", "tamaishijuryo"); // 玉石重量
        mapping.put("kaitensuu", "kaitensuu"); // 回転数
        mapping.put("junkanshuhasuu", "junkanshuhasuu"); // 循環周波数
        mapping.put("funsaijikan", "funsaijikan"); // 粉砕時間
        mapping.put("kadoujikan", "kadoujikan"); // 累計稼働時間
        mapping.put("si_ruekikakunin", "siruekikakunin"); // ｼｰﾙ液量確認
        mapping.put("funsaikaisinichiji", "funsaikaisinichiji"); // 粉砕開始日時
        mapping.put("kaisitantosya", "kaisitantosya"); // 粉砕開始担当者
        mapping.put("funsaisyuuryouyoteinichiji", "funsaisyuuryouyoteinichiji"); // 粉砕終了予定日時
        mapping.put("fukadenryuuti_10min", "fukadenryuuti10min"); // 負荷電流値
        mapping.put("mirudegutiekion_10min", "mirudegutiekion10min"); // ﾐﾙ出口液温
        mapping.put("naiatu_10min", "naiatu10min"); // 内圧
        mapping.put("ponpusyuuhasuu_10min", "ponpusyuuhasuu10min"); // ﾎﾟﾝﾌﾟ周波数
        mapping.put("funsaisyuuryounichiji", "funsaisyuuryounichiji"); // 粉砕終了日時
        mapping.put("syuryotantosya", "syuryotantosya"); // 粉砕終了担当者
        mapping.put("bikou1", "bikou1"); // 備考1
        mapping.put("bikou2", "bikou2"); // 備考2
        mapping.put("torokunichiji", "torokunichiji"); // 登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); // 更新日時
        mapping.put("revision", "revision"); // revision
        mapping.put("deleteflag", "deleteflag"); // 削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrGlassscfunsai>> beanHandler = new BeanListHandler<>(SrGlassscfunsai.class, rowProcessor);

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
     * @param srGlassscfunsai ｶﾞﾗｽ作製・SC粉砕データ
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrGlassscfunsai srGlassscfunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srGlassscfunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrGlassscfunsaiItemData(itemId, srGlassscfunsai);
        } else {
            return null;
        }
    }

    /**
     * 項目データ(入力値)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @param srGlassscfunsai ｶﾞﾗｽ作製・SC粉砕データ
     * @return 入力値
     */
    private String getItemKikakuchi(List<FXHDD01> listData, String itemId, SrGlassscfunsai srGlassscfunsai) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return StringUtil.nullToBlank(selectData.get(0).getKikakuChi()).replace("【", "").replace("】", "");
        } else if (srGlassscfunsai != null) {
            // 元データが存在する場合元データより取得
            return getSrGlassscfunsaiItemData(itemId, srGlassscfunsai);
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
     * ｶﾞﾗｽ作製・SC粉砕_仮登録(tmp_sr_glassscfunsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, String systemTime, ProcessData processData, String formId) throws SQLException {

        String sql = "INSERT INTO tmp_sr_glassscfunsai ("
                + " kojyo,lotno,edaban,glasshinmei,glasslotno,lotkubun,funsaiki,tamaishikei,tamaishijuryo,kaitensuu, "
                + " junkanshuhasuu,funsaijikan,kadoujikan,si_ruekikakunin,funsaikaisinichiji,kaisitantosya,funsaisyuuryouyoteinichiji, "
                + " fukadenryuuti_10min,mirudegutiekion_10min,naiatu_10min,ponpusyuuhasuu_10min,funsaisyuuryounichiji,syuryotantosya, "
                + " bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + " ) VALUES ( "
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = setUpdateParameterTmpSrGlassscfunsai(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, processData, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽ作製・SC粉砕_仮登録(tmp_sr_glassscfunsai)更新処理
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
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE tmp_sr_glassscfunsai SET "
                + " glasshinmei = ?,glasslotno = ?,lotkubun = ?,funsaiki = ?,tamaishikei = ?,tamaishijuryo = ?,kaitensuu = ?, "
                + " junkanshuhasuu = ?,funsaijikan = ?,kadoujikan = ?,si_ruekikakunin = ?,funsaikaisinichiji = ?,kaisitantosya = ?, "
                + " funsaisyuuryouyoteinichiji = ?,fukadenryuuti_10min = ?,mirudegutiekion_10min = ?,naiatu_10min = ?,ponpusyuuhasuu_10min = ?, "
                + " funsaisyuuryounichiji = ?,syuryotantosya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGlassscfunsai> srGlassscfunsaiList = getSrGlassscfunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrGlassscfunsai srGlassscfunsai = null;
        if (!srGlassscfunsaiList.isEmpty()) {
            srGlassscfunsai = srGlassscfunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrGlassscfunsai(false, newRev, 0, "", "", "", systemTime, processData, srGlassscfunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽ作製・SC粉砕_仮登録(tmp_sr_glassscfunsai)削除処理
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
    private void deleteTmpSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_glassscfunsai "
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
     * ｶﾞﾗｽ作製・SC粉砕_仮登録(tmp_sr_glassscfunsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srGlassscfunsai ｶﾞﾗｽ作製・SC粉砕データ
     * @param jissekino 実績No
     * @param processData 処理制御データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrGlassscfunsai(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, String systemTime, ProcessData processData, SrGlassscfunsai srGlassscfunsai) {

        List<FXHDD01> pItemList = processData.getItemList();
        List<Object> params = new ArrayList<>();
        // 粉砕開始時間
        String funsaikaishiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.FUNSAI_KAISHI_TIME, srGlassscfunsai));
        // 粉砕終了予定時間
        String funsaisyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_TIME, srGlassscfunsai));
        // 粉砕終了時間
        String funsaisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOU_TIME, srGlassscfunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.GLASSHINMEI, srGlassscfunsai))); // ｶﾞﾗｽ品名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.GLASSLOTNO, srGlassscfunsai))); // ｶﾞﾗｽLotNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.LOTKUBUN, srGlassscfunsai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B002Const.FUNSAIKI, srGlassscfunsai))); // 粉砕機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B002Const.TAMAISHIKEI, srGlassscfunsai))); // 玉石径
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B002Const.TAMAISHIJURYO, srGlassscfunsai))); // 玉石重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B002Const.KAITENSUU, srGlassscfunsai))); // 回転数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B002Const.JUNKANSHUHASUU, srGlassscfunsai))); // 循環周波数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemKikakuchi(pItemList, GXHDO102B002Const.FUNSAIJIKAN, srGlassscfunsai))); // 粉砕時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.KADOUJIKAN, srGlassscfunsai))); // 累計稼働時間
        // ｼｰﾙ液量確認
        switch (StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.SI_RUEKIKAKUNIN, srGlassscfunsai))) {
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
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.FUNSAI_KAISHI_DAY, srGlassscfunsai),
                "".equals(funsaikaishiTime) ? "0000" : funsaikaishiTime)); // 粉砕開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.KAISITANTOSYA, srGlassscfunsai))); // 粉砕開始担当者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_DAY, srGlassscfunsai),
                "".equals(funsaisyuuryouyoteiTime) ? "0000" : funsaisyuuryouyoteiTime)); // 粉砕終了予定日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.FUKADENRYUUTI_10MIN, srGlassscfunsai))); // 負荷電流値
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.MIRUDEGUTIEKION_10MIN, srGlassscfunsai))); // ﾐﾙ出口液温
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.NAIATU_10MIN, srGlassscfunsai))); // 内圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.PONPUSYUUHASUU_10MIN, srGlassscfunsai))); // ﾎﾟﾝﾌﾟ周波数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOU_DAY, srGlassscfunsai),
                "".equals(funsaisyuuryouTime) ? "0000" : funsaisyuuryouTime)); // 粉砕終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.SYURYOTANTOSYA, srGlassscfunsai))); // 粉砕終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.BIKOU1, srGlassscfunsai))); // 備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(pItemList, GXHDO102B002Const.BIKOU2, srGlassscfunsai))); // 備考2
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
     * ｶﾞﾗｽ作製・SC粉砕(sr_glassscfunsai)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param tmpSrGlassscfunsai 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData, SrGlassscfunsai tmpSrGlassscfunsai, String formId) throws SQLException {

        String sql = "INSERT INTO sr_glassscfunsai ("
                + " kojyo,lotno,edaban,glasshinmei,glasslotno,lotkubun,funsaiki,tamaishikei,tamaishijuryo,kaitensuu, "
                + " junkanshuhasuu,funsaijikan,kadoujikan,si_ruekikakunin,funsaikaisinichiji,kaisitantosya,funsaisyuuryouyoteinichiji, "
                + " fukadenryuuti_10min,mirudegutiekion_10min,naiatu_10min,ponpusyuuhasuu_10min,funsaisyuuryounichiji,syuryotantosya, "
                + " bikou1,bikou2,torokunichiji,kosinnichiji,revision "
                + " ) VALUES ( "
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = setUpdateParameterSrGlassscfunsai(true, newRev, kojyo, lotNo, edaban, systemTime, processData, tmpSrGlassscfunsai);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽ作製・SC粉砕(sr_glassscfunsai)更新処理
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
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param processData 処理制御データ
     * @throws SQLException 例外エラー
     */
    private void updateSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, String systemTime, ProcessData processData) throws SQLException {

        String sql = "UPDATE sr_glassscfunsai SET "
                + " glasshinmei = ?,glasslotno = ?,lotkubun = ?,funsaiki = ?,tamaishikei = ?,tamaishijuryo = ?,kaitensuu = ?, "
                + " junkanshuhasuu = ?,funsaijikan = ?,kadoujikan = ?,si_ruekikakunin = ?,funsaikaisinichiji = ?,kaisitantosya = ?, "
                + " funsaisyuuryouyoteinichiji = ?,fukadenryuuti_10min = ?,mirudegutiekion_10min = ?,naiatu_10min = ?,ponpusyuuhasuu_10min = ?, "
                + " funsaisyuuryounichiji = ?,syuryotantosya = ?,bikou1 = ?,bikou2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrGlassscfunsai> srGlassscfunsaiList = getSrGlassscfunsaiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrGlassscfunsai srGlassscfunsai = null;
        if (!srGlassscfunsaiList.isEmpty()) {
            srGlassscfunsai = srGlassscfunsaiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrGlassscfunsai(false, newRev, "", "", "", systemTime, processData, srGlassscfunsai);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * ｶﾞﾗｽ作製・SC粉砕(sr_glassscfunsai)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @param processData 処理制御データ
     * @param srGlassscfunsai ｶﾞﾗｽ作製・SC粉砕データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrGlassscfunsai(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            String systemTime, ProcessData processData, SrGlassscfunsai srGlassscfunsai) {

        List<FXHDD01> pItemList = processData.getItemList();

        List<Object> params = new ArrayList<>();
        // 粉砕開始時間
        String funsaikaishiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.FUNSAI_KAISHI_TIME, srGlassscfunsai));
        // 粉砕終了予定時間
        String funsaisyuuryouyoteiTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_TIME, srGlassscfunsai));
        // 粉砕終了時間
        String funsaisyuuryouTime = StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOU_TIME, srGlassscfunsai));

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.GLASSHINMEI, srGlassscfunsai))); // ｶﾞﾗｽ品名
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.GLASSLOTNO, srGlassscfunsai))); // ｶﾞﾗｽLotNo
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.LOTKUBUN, srGlassscfunsai))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B002Const.FUNSAIKI, srGlassscfunsai))); // 粉砕機
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B002Const.TAMAISHIKEI, srGlassscfunsai))); // 玉石径
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B002Const.TAMAISHIJURYO, srGlassscfunsai))); // 玉石重量
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B002Const.KAITENSUU, srGlassscfunsai))); // 回転数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B002Const.JUNKANSHUHASUU, srGlassscfunsai))); // 循環周波数
        params.add(DBUtil.stringToStringObject(getItemKikakuchi(pItemList, GXHDO102B002Const.FUNSAIJIKAN, srGlassscfunsai))); // 粉砕時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B002Const.KADOUJIKAN, srGlassscfunsai))); // 累計稼働時間
        // ｼｰﾙ液量確認
        switch (StringUtil.nullToBlank(getItemData(pItemList, GXHDO102B002Const.SI_RUEKIKAKUNIN, srGlassscfunsai))) {
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
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B002Const.FUNSAI_KAISHI_DAY, srGlassscfunsai),
                "".equals(funsaikaishiTime) ? "0000" : funsaikaishiTime)); // 粉砕開始日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.KAISITANTOSYA, srGlassscfunsai))); // 粉砕開始担当者
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_DAY, srGlassscfunsai),
                "".equals(funsaisyuuryouyoteiTime) ? "0000" : funsaisyuuryouyoteiTime)); // 粉砕終了予定日時
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B002Const.FUKADENRYUUTI_10MIN, srGlassscfunsai))); // 負荷電流値
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B002Const.MIRUDEGUTIEKION_10MIN, srGlassscfunsai))); // ﾐﾙ出口液温
        params.add(DBUtil.stringToBigDecimalObject(getItemData(pItemList, GXHDO102B002Const.NAIATU_10MIN, srGlassscfunsai))); // 内圧
        params.add(DBUtil.stringToIntObject(getItemData(pItemList, GXHDO102B002Const.PONPUSYUUHASUU_10MIN, srGlassscfunsai))); // ﾎﾟﾝﾌﾟ周波数
        params.add(DBUtil.stringToDateObject(getItemData(pItemList, GXHDO102B002Const.FUNSAI_SYUURYOU_DAY, srGlassscfunsai),
                "".equals(funsaisyuuryouTime) ? "0000" : funsaisyuuryouTime)); // 粉砕終了日時
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.SYURYOTANTOSYA, srGlassscfunsai))); // 粉砕終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.BIKOU1, srGlassscfunsai))); // 備考1
        params.add(DBUtil.stringToStringObject(getItemData(pItemList, GXHDO102B002Const.BIKOU2, srGlassscfunsai))); // 備考2        
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
     * ｶﾞﾗｽ作製・SC粉砕(sr_glassscfunsai)削除処理
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
    private void deleteSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = " DELETE FROM sr_glassscfunsai "
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
     * [ｶﾞﾗｽ作製・SC粉砕_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
        String sql = " SELECT MAX(deleteflag) AS deleteflag "
                + " FROM tmp_sr_glassscfunsai "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
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
     * @param srGlassscfunsai ｶﾞﾗｽ作製・SC粉砕データ
     * @return DB値
     */
    private String getSrGlassscfunsaiItemData(String itemId, SrGlassscfunsai srGlassscfunsai) {
        switch (itemId) {            
            // 粉砕機
            case GXHDO102B002Const.FUNSAIKI:
                return StringUtil.nullToBlank(srGlassscfunsai.getFunsaiki());
            // 玉石径
            case GXHDO102B002Const.TAMAISHIKEI:
                return StringUtil.nullToBlank(srGlassscfunsai.getTamaishikei());
            // 玉石重量
            case GXHDO102B002Const.TAMAISHIJURYO:
                return StringUtil.nullToBlank(srGlassscfunsai.getTamaishijuryo());
            // 回転数
            case GXHDO102B002Const.KAITENSUU:
                return StringUtil.nullToBlank(srGlassscfunsai.getKaitensuu());
            // 循環周波数
            case GXHDO102B002Const.JUNKANSHUHASUU:
                return StringUtil.nullToBlank(srGlassscfunsai.getJunkanshuhasuu());
            // 粉砕時間
            case GXHDO102B002Const.FUNSAIJIKAN:
                return StringUtil.nullToBlank(srGlassscfunsai.getFunsaijikan());
            // 累計稼働時間
            case GXHDO102B002Const.KADOUJIKAN:
                return StringUtil.nullToBlank(srGlassscfunsai.getKadoujikan());
            // ｼｰﾙ液量確認
            case GXHDO102B002Const.SI_RUEKIKAKUNIN:
                switch (StringUtil.nullToBlank(srGlassscfunsai.getSiruekikakunin())) {
                    case "0":
                        return "NG";
                    case "1":
                        return "OK";
                    default:
                        return "";
                }
            // 粉砕開始日
            case GXHDO102B002Const.FUNSAI_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srGlassscfunsai.getFunsaikaisinichiji(), "yyMMdd");
            // 粉砕開始時間
            case GXHDO102B002Const.FUNSAI_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srGlassscfunsai.getFunsaikaisinichiji(), "HHmm");
            // 粉砕開始担当者
            case GXHDO102B002Const.KAISITANTOSYA:
                return StringUtil.nullToBlank(srGlassscfunsai.getKaisitantosya());
            // 粉砕終了予定日
            case GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_DAY:
                return DateUtil.formattedTimestamp(srGlassscfunsai.getFunsaisyuuryouyoteinichiji(), "yyMMdd");
            // 粉砕終了予定時間
            case GXHDO102B002Const.FUNSAI_SYUURYOUYOTEI_TIME:
                return DateUtil.formattedTimestamp(srGlassscfunsai.getFunsaisyuuryouyoteinichiji(), "HHmm");
            // 負荷電流値
            case GXHDO102B002Const.FUKADENRYUUTI_10MIN:
                return StringUtil.nullToBlank(srGlassscfunsai.getFukadenryuuti10min());
            // ﾐﾙ出口温度
            case GXHDO102B002Const.MIRUDEGUTIEKION_10MIN:
                return StringUtil.nullToBlank(srGlassscfunsai.getMirudegutiekion10min());
            // 内圧
            case GXHDO102B002Const.NAIATU_10MIN:
                return StringUtil.nullToBlank(srGlassscfunsai.getNaiatu10min());
            // ﾎﾟﾝﾌﾟ周波数
            case GXHDO102B002Const.PONPUSYUUHASUU_10MIN:
                return StringUtil.nullToBlank(srGlassscfunsai.getPonpusyuuhasuu10min());
            // 粉砕終了日
            case GXHDO102B002Const.FUNSAI_SYUURYOU_DAY:
                return DateUtil.formattedTimestamp(srGlassscfunsai.getFunsaisyuuryounichiji(), "yyMMdd");
            // 粉砕終了時間
            case GXHDO102B002Const.FUNSAI_SYUURYOU_TIME:
                return DateUtil.formattedTimestamp(srGlassscfunsai.getFunsaisyuuryounichiji(), "HHmm");
            // 粉砕終了担当者
            case GXHDO102B002Const.SYURYOTANTOSYA:
                return StringUtil.nullToBlank(srGlassscfunsai.getSyuryotantosya());
            // 備考1
            case GXHDO102B002Const.BIKOU1:
                return StringUtil.nullToBlank(srGlassscfunsai.getBikou1());
            // 備考2
            case GXHDO102B002Const.BIKOU2:
                return StringUtil.nullToBlank(srGlassscfunsai.getBikou2());

            default:
                return null;
        }
    }

    /**
     * ｶﾞﾗｽ作製・SC粉砕_仮登録(tmp_sr_glassscfunsai)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(原材料品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrGlassscfunsai(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, String systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_glassscfunsai ( "
                + " kojyo,lotno,edaban,glasshinmei,glasslotno,lotkubun,funsaiki,tamaishikei,tamaishijuryo,kaitensuu, "
                + " junkanshuhasuu,funsaijikan,kadoujikan,si_ruekikakunin,funsaikaisinichiji,kaisitantosya,funsaisyuuryouyoteinichiji, "
                + " fukadenryuuti_10min,mirudegutiekion_10min,naiatu_10min,ponpusyuuhasuu_10min,funsaisyuuryounichiji,syuryotantosya, "
                + " bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag "
                + ") SELECT "
                + " kojyo,lotno,edaban,glasshinmei,glasslotno,lotkubun,funsaiki,tamaishikei,tamaishijuryo,kaitensuu, "
                + " junkanshuhasuu,funsaijikan,kadoujikan,si_ruekikakunin,funsaikaisinichiji,kaisitantosya,funsaisyuuryouyoteinichiji, "
                + " fukadenryuuti_10min,mirudegutiekion_10min,naiatu_10min,ponpusyuuhasuu_10min,funsaisyuuryounichiji,syuryotantosya, "
                + " bikou1,bikou2,?,?,?,? "
                + " FROM sr_glassscfunsai "
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
}
