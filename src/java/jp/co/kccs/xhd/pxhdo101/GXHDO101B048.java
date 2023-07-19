/*
 * Copyright 2020 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import jp.co.kccs.xhd.db.model.Seisan;
import jp.co.kccs.xhd.db.model.SrTapingSagyo;
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
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2020/02/03<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * 変更日	2021/10/08<br>
 * 計画書No	MB2109-DK002<br>
 * 変更者	SRC T.Ushiyama<br>
 * 変更理由	出荷履歴表対応<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B048(TP作業)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2020/02/03
 */
public class GXHDO101B048 implements IFormLogic {

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

            //処理時にエラーの背景色を戻さない機能として登録
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B048Const.BTN_SAGYO_START_TOP,
                    GXHDO101B048Const.BTN_SAGYO_END_TOP,
                    GXHDO101B048Const.BTN_BARASHI_START_TOP,
                    GXHDO101B048Const.BTN_BARASHI_END_TOP,
                    GXHDO101B048Const.BTN_DATSUJI_START_TOP,
                    GXHDO101B048Const.BTN_TP_SAGYO_TYUI_TOP,
                    GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_TOP,
                    GXHDO101B048Const.BTN_REELKEISU_TYUI_TOP,
                    GXHDO101B048Const.BTN_SAGYO_START_BOTTOM,
                    GXHDO101B048Const.BTN_SAGYO_END_BOTTOM,
                    GXHDO101B048Const.BTN_BARASHI_START_BOTTOM,
                    GXHDO101B048Const.BTN_BARASHI_END_BOTTOM,
                    GXHDO101B048Const.BTN_DATSUJI_START_BOTTOM,
                    GXHDO101B048Const.BTN_TP_SAGYO_TYUI_BOTTOM,
                    GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_BOTTOM,
                    GXHDO101B048Const.BTN_REELKEISU_TYUI_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(""));

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
            // 仮登録
            case GXHDO101B048Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B048Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B048Const.BTN_INSERT_TOP:
            case GXHDO101B048Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B048Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B048Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B048Const.BTN_UPDATE_TOP:
            case GXHDO101B048Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B048Const.BTN_DELETE_TOP:
            case GXHDO101B048Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 作業開始
            case GXHDO101B048Const.BTN_SAGYO_START_TOP:
            case GXHDO101B048Const.BTN_SAGYO_START_BOTTOM:
                method = "setStartDateTime";
                break;
            // 作業終了
            case GXHDO101B048Const.BTN_SAGYO_END_TOP:
            case GXHDO101B048Const.BTN_SAGYO_END_BOTTOM:
                method = "setEndDateTime";
                break;
            // ﾊﾞﾗｼ開始
            case GXHDO101B048Const.BTN_BARASHI_START_TOP:
            case GXHDO101B048Const.BTN_BARASHI_START_BOTTOM:
                method = "setBarashiStartDateTime";
                break;
            // ﾊﾞﾗｼ終了
            case GXHDO101B048Const.BTN_BARASHI_END_TOP:
            case GXHDO101B048Const.BTN_BARASHI_END_BOTTOM:
                method = "setBarashiEndDateTime";
                break;
            // 脱磁開始
            case GXHDO101B048Const.BTN_DATSUJI_START_TOP:
            case GXHDO101B048Const.BTN_DATSUJI_START_BOTTOM:
                method = "setDatsujiStartDateTime";
                break;
            // TP作業注意
            case GXHDO101B048Const.BTN_TP_SAGYO_TYUI_TOP:
            case GXHDO101B048Const.BTN_TP_SAGYO_TYUI_BOTTOM:
                method = "openTpSagyoTyui";
                break;
            // ﾊﾞﾗｼ作業注意
            case GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_TOP:
            case GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_BOTTOM:
                method = "openBarashiSagyoTyui";
                break;
            // ﾘｰﾙ計数注意
            case GXHDO101B048Const.BTN_REELKEISU_TYUI_TOP:
            case GXHDO101B048Const.BTN_REELKEISU_TYUI_BOTTOM:
                method = "openReelKeisuTyui";
                break;
            // 確保数計算
            case GXHDO101B048Const.BTN_KAKUHOSU_KEISAN_TOP:
            case GXHDO101B048Const.BTN_KAKUHOSU_KEISAN_BOTTOM:
                method = "doKakuhosuKeisan";
                break;
            // 良品数計算
            case GXHDO101B048Const.BTN_RYOHINSU_KEISAN_TOP:
            case GXHDO101B048Const.BTN_RYOHINSU_KEISAN_BOTTOM:
                method = "doRyohinsuKeisan";
                break;
            // 歩留まり計算
            case GXHDO101B048Const.BTN_BUDOMARI_KEISAN_TOP:
            case GXHDO101B048Const.BTN_BUDOMARI_KEISAN_BOTTOM:
                method = "doBudomariKeisan";
                break;
            // 受入れ総重量計算
            case GXHDO101B048Const.BTN_UKEIRE_SOUJURYO_KEISAN_TOP:
            case GXHDO101B048Const.BTN_UKEIRE_SOUJURYO_KEISAN_BOTTOM:
                method = "confUkeireSojuryoKeisan";
                break;
            default:
                method = "error";
                break;
        }

        return method;
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
        
        for (int i = 0; i <= processData.getItemList().size() - 1; i++) {
            if (processData.getItemList().get(i).getLabel1().equals("号機")){
                // 号機の場合の規格値が設定されているかつ入力値がある場合
                if (!StringUtil.isEmpty(processData.getItemList().get(i).getKikakuChi()) && !StringUtil.isEmpty(processData.getItemList().get(i).getValue())){
                    // 項目データを取得
                    String strKikakuchi = StringUtil.nullToBlank(processData.getItemList().get(i).getKikakuChi()).replace("【", "");
                    strKikakuchi = strKikakuchi.replace("】", "");
                    if (!strKikakuchi.equals(processData.getItemList().get(i).getValue())) {
                        // 規格値と入力値が正しくない場合エラー
                        errorMessageInfo = MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(processData.getItemList().get(i)), processData.getItemList().get(i).getLabel1());
                        processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
                        return processData;
                    }
                }
            }
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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
            int bango = (Integer) session.getAttribute("bango");
            String goki = "";

            // ﾃｰﾋﾟﾝｸﾞ号機選択データ取得
            Map fxhdd12RevInfo = loadFxhdd12RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd12RevInfo);
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
                // 号機取得
                goki = getGoki(processData, queryRunnerQcdb, rev.toPlainString(), JOTAI_FLG_TOROKUZUMI, kojyo, lotNo8, edaban, jissekiNo, bango);
                // ﾃｰﾋﾟﾝｸﾞ号機選択登録処理
                insertFxhdd12(queryRunnerDoc, conDoc, tantoshaCd, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, goki, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getFxhdd12NewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
                // 号機取得
                goki = getGoki(processData, queryRunnerQcdb, rev.toPlainString(), JOTAI_FLG_TOROKUZUMI, kojyo, lotNo8, edaban, jissekiNo, bango);

                // ﾃｰﾋﾟﾝｸﾞ号機選択更新処理
                updateFxhdd12(queryRunnerDoc, conDoc, tantoshaCd, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, goki, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            }else{
                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // テーピング作業_仮登録登録処理
                insertTmpSrTapingSagyo(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekiNo, bango, systemTime, processData.getItemList(), processData.getHiddenDataMap());

            } else {

                // テーピング作業_仮登録更新処理
                updateTmpSrTapingSagyo(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekiNo, bango, systemTime, processData.getItemList(), processData.getHiddenDataMap());
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

        ErrorMessageInfo errorMessageInfo;

        // ﾎｯﾊﾟｰﾈｼﾞ確認
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.HOPPERNEJI_KAKUNIN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ｺﾃ清掃
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.KOTE_SEISOU));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 開始前に製品残無き事
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.KAISIMAESEHINZANNASI));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 自重落下試験
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.JIJU_RAKKA));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ﾊﾞﾗｼﾁｪｯｸ
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.BARASHI_CHECK));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ﾄｯﾌﾟﾃｰﾌﾟ確認
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.TOPTAPE_KAKUNIN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ﾒﾝﾃ後TP外観
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.MAINTGO_TP_GAIKAN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ﾘｰﾙ数ﾁｪｯｸ
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.REELSU_CHECK));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // TP後清掃：ﾎｯﾊﾟｰ部
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.TPATO_SEISOU_HOPPER_PART));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // TP後清掃：ﾌｨｰﾀﾞ部
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.TPATO_SEISOU_FEEDER_PART));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // TP後清掃：INDEX内
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.TPATO_SEISOU_IN_INDEX));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // TP後清掃：NGBOX内
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B048Const.TPATO_SEISOU_IN_NGBOX));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 kaishiDay = getItemRow(processData.getItemList(), GXHDO101B048Const.KAISHI_DAY); //開始日
        FXHDD01 kaishiTime = getItemRow(processData.getItemList(), GXHDO101B048Const.KAISHI_TIME); //開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(kaishiDay.getValue(), kaishiTime.getValue());
        FXHDD01 shuryouDay = getItemRow(processData.getItemList(), GXHDO101B048Const.SHURYOU_DAY); //終了日
        FXHDD01 shuryouTime = getItemRow(processData.getItemList(), GXHDO101B048Const.SHURYOU_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(shuryouDay.getValue(), shuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(kaishiDay.getLabel1(), kaishiDate, shuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(kaishiDay, kaishiTime, shuryouDay, shuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        // 開始日時、終了日時前後チェック
        FXHDD01 barashiKaishiDay = getItemRow(processData.getItemList(), GXHDO101B048Const.BARASHI_KAISHI_DAY); //開始日
        FXHDD01 barashiKaishiTime = getItemRow(processData.getItemList(), GXHDO101B048Const.BARASHI_KAISHI_TIME); //開始時刻
        Date barashiKaishiDate = DateUtil.convertStringToDate(barashiKaishiDay.getValue(), barashiKaishiTime.getValue());
        FXHDD01 barashiShuryouDay = getItemRow(processData.getItemList(), GXHDO101B048Const.BARASHI_SHURYOU_DAY); //終了日
        FXHDD01 barashiShuryouTime = getItemRow(processData.getItemList(), GXHDO101B048Const.BARASHI_SHURYOU_TIME); //終了時刻
        Date barashiShuryoDate = DateUtil.convertStringToDate(barashiShuryouDay.getValue(), barashiShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001Barashi = validateUtil.checkR001(barashiKaishiDay.getLabel1(), barashiKaishiDate, barashiShuryouDay.getLabel1(), barashiShuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001Barashi)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(barashiKaishiDay, barashiKaishiTime, barashiShuryouDay, barashiShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001Barashi, true, true, errFxhdd01List);
        }

        for (int i = 0; i <= processData.getItemList().size() - 1; i++) {
            if (processData.getItemList().get(i).getLabel1().equals("号機")){
                // 号機の場合の規格値が設定されているかつ入力値がある場合
                if (!StringUtil.isEmpty(processData.getItemList().get(i).getKikakuChi()) && !StringUtil.isEmpty(processData.getItemList().get(i).getValue())){
                    // 項目データを取得
                    String strKikakuchi = StringUtil.nullToBlank(processData.getItemList().get(i).getKikakuChi()).replace("【", "");
                    strKikakuchi = strKikakuchi.replace("】", "");
                    if (!strKikakuchi.equals(processData.getItemList().get(i).getValue())) {
                        // 規格値と入力値が正しくない場合エラー
                        return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(processData.getItemList().get(i)), processData.getItemList().get(i).getLabel1());
                    }
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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            int bango = (Integer) session.getAttribute("bango");
            String goki = "";
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // ﾃｰﾋﾟﾝｸﾞ号機選択データ取得
            //ここでロックを掛ける
            Map fxhdd12RevInfo = loadFxhdd12RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd12RevInfo);
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
                // 号機取得
                goki = getGoki(processData, queryRunnerQcdb, rev.toPlainString(), JOTAI_FLG_TOROKUZUMI, kojyo, lotNo8, edaban, jissekiNo, bango);
                // ﾃｰﾋﾟﾝｸﾞ号機選択登録処理
                insertFxhdd12(queryRunnerDoc, conDoc, tantoshaCd, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, goki, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getFxhdd12NewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
                // 号機取得
                goki = getGoki(processData, queryRunnerQcdb, rev.toPlainString(), JOTAI_FLG_TOROKUZUMI, kojyo, lotNo8, edaban, jissekiNo, bango);

                // ﾃｰﾋﾟﾝｸﾞ号機選択更新処理
                updateFxhdd12(queryRunnerDoc, conDoc, tantoshaCd, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, goki, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // ﾃｰﾋﾟﾝｸﾞ号機選択状態ﾁｪｯｸ処理
            if (jotaiCheckFxhdd12(queryRunnerDoc, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, bango)) {
                // 品質DB登録実績データ取得
                Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
                if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                    // 品質DB登録実績登録処理
                    insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
                }else{
                    // 品質DB登録実績更新処理
                    updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
                }
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrTapingSagyo tmpSrTapingSagyo = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrTapingSagyo> srTapingSagyoList = getSrTapingSagyoData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, jissekiNo, bango);
                if (!srTapingSagyoList.isEmpty()) {
                    tmpSrTapingSagyo = srTapingSagyoList.get(0);
                }

                deleteTmpSrTapingSagyo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekiNo, bango);
            }

            // テーピング作業_登録処理
            insertSrTapingSagyo(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, systemTime, processData.getItemList(), tmpSrTapingSagyo, processData.getHiddenDataMap());

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
        
        for (int i = 0; i <= processData.getItemList().size() - 1; i++) {
            if (processData.getItemList().get(i).getLabel1().equals("号機")){
                // 号機の場合の規格値が設定されているかつ入力値がある場合
                if (!StringUtil.isEmpty(processData.getItemList().get(i).getKikakuChi()) && !StringUtil.isEmpty(processData.getItemList().get(i).getValue())){
                    // 項目データを取得
                    String strKikakuchi = StringUtil.nullToBlank(processData.getItemList().get(i).getKikakuChi()).replace("【", "");
                    strKikakuchi = strKikakuchi.replace("】", "");
                    if (!strKikakuchi.equals(processData.getItemList().get(i).getValue())) {
                        // 規格値と入力値が正しくない場合エラー
                        ErrorMessageInfo errorMessage = MessageUtil.getErrorMessageInfo("XHD-000032", true, true, Arrays.asList(processData.getItemList().get(i)), processData.getItemList().get(i).getLabel1());
                        processData.setErrorMessageInfoList(Arrays.asList(errorMessage));
                        return processData;
                    }
                }
            }
        }

        // 警告メッセージの設定
        processData.setWarnMessage("修正します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B048Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            int bango = (Integer) session.getAttribute("bango");
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));

            // ﾃｰﾋﾟﾝｸﾞ号機選択データ取得
            //ここでロックを掛ける
            Map fxhdd12RevInfo = loadFxhdd12RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd12RevInfo);
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
            BigDecimal newRev = getFxhdd12NewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
            
            String goki = getGoki(processData, queryRunnerQcdb, rev.toPlainString(), JOTAI_FLG_TOROKUZUMI, kojyo, lotNo8, edaban, jissekiNo, bango);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // ﾃｰﾋﾟﾝｸﾞ号機選択更新処理
            updateFxhdd12(queryRunnerDoc, conDoc, tantoshaCd, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, goki, JOTAI_FLG_TOROKUZUMI, systemTime);

            // ﾃｰﾋﾟﾝｸﾞ号機選択状態ﾁｪｯｸ処理
            if (jotaiCheckFxhdd12(queryRunnerDoc, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, bango)) {
                // 品質DB登録実績データ取得
                Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
                if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                    // 品質DB登録実績登録処理
                    insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
                }else{
                    // 品質DB登録実績更新処理
                    updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);
                }
            }

            // テーピング作業_更新処理
            updateSrTapingSagyo(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekiNo, bango, systemTime, processData.getItemList(), processData.getHiddenDataMap());

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
        processData.setUserAuthParam(GXHDO101B048Const.USER_AUTH_DELETE_PARAM);

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
            int jissekiNo = (Integer) session.getAttribute("jissekino");
            int bango = (Integer) session.getAttribute("bango");
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));

            // ﾃｰﾋﾟﾝｸﾞ号機選択データ取得
            //ここでロックを掛ける
            Map fxhdd12RevInfo = loadFxhdd12RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
            ErrorMessageInfo checkRevMessageInfo = checkRevision(processData, fxhdd12RevInfo);
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
            BigDecimal newRev = getFxhdd12NewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, bango);
            // 更新前の値を取得
            List<SrTapingSagyo> srTapingSagyoList = getSrTapingSagyoData(queryRunnerQcdb, rev.toPlainString(), JOTAI_FLG_SAKUJO, kojyo, lotNo, edaban, jissekiNo, bango);
            SrTapingSagyo srTapingSagyo = null;
            if (!srTapingSagyoList.isEmpty()) {
                srTapingSagyo = srTapingSagyoList.get(0);
            }
            String goki = getItemData(processData.getItemList(), GXHDO101B048Const.GOKI, srTapingSagyo);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // ﾃｰﾋﾟﾝｸﾞ号機選択更新処理
            updateFxhdd12(queryRunnerDoc, conDoc, tantoshaCd, newRev, kojyo, lotNo8, edaban, jissekiNo, bango, goki, JOTAI_FLG_SAKUJO, systemTime);

            // ﾃｰﾋﾟﾝｸﾞ号機選択状態ﾁｪｯｸ処理
            if (jotaiCheckFxhdd12(queryRunnerDoc, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_SAKUJO, bango)) {
                // 品質DB登録実績データ取得
                Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, jissekiNo, formId);
                if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                    // 品質DB登録実績登録処理
                    insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_SAKUJO, systemTime);
                }else{
                    // 品質DB登録実績更新処理
                    updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, BigDecimal.ZERO, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_SAKUJO, systemTime);
                }
            }

            // テーピング作業_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, jissekiNo, bango);
            insertDeleteDataTmpSrTapingSagyo(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekiNo, bango, systemTime);

            // テーピング作業_削除処理
            deleteSrTapingSagyo(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekiNo, bango);

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
     * 開始日・開始時間設定処理(作業開始ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B048Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B048Const.KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 終了日・終了時間設定処理(作業終了ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B048Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B048Const.SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * ﾊﾞﾗｼ開始日・ﾊﾞﾗｼ開始時間設定処理(ﾊﾞﾗｼ開始ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBarashiStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B048Const.BARASHI_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B048Const.BARASHI_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * ﾊﾞﾗｼ終了日・ﾊﾞﾗｼ終了時間設定処理(ﾊﾞﾗｼ終了ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setBarashiEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B048Const.BARASHI_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B048Const.BARASHI_SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 脱磁開始日・脱磁開始時間設定処理(脱磁開始ボタン押下)
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setDatsujiStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B048Const.DATSUJI_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B048Const.DATSUJI_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * TP作業注意(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openTpSagyoTyui(ProcessData processData) {

        processData.setMethod("");
        // コールバックパラメータにてサブ画面起動用の値を設定
        processData.setCollBackParam("gxhdo101c013");
        return processData;
    }

    /**
     * ﾊﾞﾗｼ作業注意(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openBarashiSagyoTyui(ProcessData processData) {

        processData.setMethod("");
        // コールバックパラメータにてサブ画面起動用の値を設定
        processData.setCollBackParam("gxhdo101c014");
        return processData;
    }

    /**
     * ﾘｰﾙ計数注意(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openReelKeisuTyui(ProcessData processData) {

        processData.setMethod("");
        // コールバックパラメータにてサブ画面起動用の値を設定
        processData.setCollBackParam("gxhdo101c015");
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
                        GXHDO101B048Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B048Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B048Const.BTN_UPDATE_TOP,
                        GXHDO101B048Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B048Const.BTN_DELETE_TOP,
                        GXHDO101B048Const.BTN_DELETE_BOTTOM,
                        GXHDO101B048Const.BTN_SAGYO_START_TOP,
                        GXHDO101B048Const.BTN_SAGYO_START_BOTTOM,
                        GXHDO101B048Const.BTN_SAGYO_END_TOP,
                        GXHDO101B048Const.BTN_SAGYO_END_BOTTOM,
                        GXHDO101B048Const.BTN_BARASHI_START_TOP,
                        GXHDO101B048Const.BTN_BARASHI_START_BOTTOM,
                        GXHDO101B048Const.BTN_BARASHI_END_TOP,
                        GXHDO101B048Const.BTN_BARASHI_END_BOTTOM,
                        GXHDO101B048Const.BTN_DATSUJI_START_TOP,
                        GXHDO101B048Const.BTN_DATSUJI_START_BOTTOM,
                        GXHDO101B048Const.BTN_TP_SAGYO_TYUI_TOP,
                        GXHDO101B048Const.BTN_TP_SAGYO_TYUI_BOTTOM,
                        GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_TOP,
                        GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_BOTTOM,
                        GXHDO101B048Const.BTN_REELKEISU_TYUI_TOP,
                        GXHDO101B048Const.BTN_REELKEISU_TYUI_BOTTOM,
                        GXHDO101B048Const.BTN_KAKUHOSU_KEISAN_TOP,
                        GXHDO101B048Const.BTN_KAKUHOSU_KEISAN_BOTTOM,
                        GXHDO101B048Const.BTN_RYOHINSU_KEISAN_TOP,
                        GXHDO101B048Const.BTN_RYOHINSU_KEISAN_BOTTOM,
                        GXHDO101B048Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B048Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B048Const.BTN_UKEIRE_SOUJURYO_KEISAN_TOP,
                        GXHDO101B048Const.BTN_UKEIRE_SOUJURYO_KEISAN_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B048Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B048Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B048Const.BTN_INSERT_BOTTOM,
                        GXHDO101B048Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B048Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B048Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B048Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B048Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B048Const.BTN_INSERT_TOP,
                        GXHDO101B048Const.BTN_INSERT_BOTTOM,
                        GXHDO101B048Const.BTN_SAGYO_START_TOP,
                        GXHDO101B048Const.BTN_SAGYO_START_BOTTOM,
                        GXHDO101B048Const.BTN_SAGYO_END_TOP,
                        GXHDO101B048Const.BTN_SAGYO_END_BOTTOM,
                        GXHDO101B048Const.BTN_BARASHI_START_TOP,
                        GXHDO101B048Const.BTN_BARASHI_START_BOTTOM,
                        GXHDO101B048Const.BTN_BARASHI_END_TOP,
                        GXHDO101B048Const.BTN_BARASHI_END_BOTTOM,
                        GXHDO101B048Const.BTN_DATSUJI_START_TOP,
                        GXHDO101B048Const.BTN_DATSUJI_START_BOTTOM,
                        GXHDO101B048Const.BTN_TP_SAGYO_TYUI_TOP,
                        GXHDO101B048Const.BTN_TP_SAGYO_TYUI_BOTTOM,
                        GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_TOP,
                        GXHDO101B048Const.BTN_BARASHI_SAGYO_TYUI_BOTTOM,
                        GXHDO101B048Const.BTN_REELKEISU_TYUI_TOP,
                        GXHDO101B048Const.BTN_REELKEISU_TYUI_BOTTOM,
                        GXHDO101B048Const.BTN_KAKUHOSU_KEISAN_TOP,
                        GXHDO101B048Const.BTN_KAKUHOSU_KEISAN_BOTTOM,
                        GXHDO101B048Const.BTN_RYOHINSU_KEISAN_TOP,
                        GXHDO101B048Const.BTN_RYOHINSU_KEISAN_BOTTOM,
                        GXHDO101B048Const.BTN_BUDOMARI_KEISAN_TOP,
                        GXHDO101B048Const.BTN_BUDOMARI_KEISAN_BOTTOM,
                        GXHDO101B048Const.BTN_UKEIRE_SOUJURYO_KEISAN_TOP,
                        GXHDO101B048Const.BTN_UKEIRE_SOUJURYO_KEISAN_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B048Const.BTN_DELETE_TOP,
                        GXHDO101B048Const.BTN_DELETE_BOTTOM,
                        GXHDO101B048Const.BTN_UPDATE_TOP,
                        GXHDO101B048Const.BTN_UPDATE_BOTTOM
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
    private ProcessData setInitDate(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        int jissekino = (Integer) session.getAttribute("jissekino");
        int bango = (Integer) session.getAttribute("bango");

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
        String tanijuryo = StringUtil.nullToBlank(getMapData(shikakariData, "tanijuryo"));// 単位重量
        Map hiddenMap = processData.getHiddenDataMap();
        hiddenMap.put("lotkubuncode", lotkubuncode);
        hiddenMap.put("ownercode", ownercode);

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

        String okuriRyohinsu = "";

        String param = loadParamData(queryRunnerDoc, "common_user", "xhd_taping_sagyo_maekoteicode");
        if (!StringUtil.isEmpty(param)) {
            String spParam[] = param.split(",");

            // 実績情報の取得
            List<Jisseki> jissekiData = loadJissekiData(queryRunnerWip, lotNo, spParam);
            if (jissekiData != null && 0 < jissekiData.size()) {
                int dbShorisu = jissekiData.get(0).getSyorisuu(); //処理数  
                if (0 < dbShorisu) {
                    okuriRyohinsu = String.valueOf(dbShorisu);
                }
            }
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, queryRunnerWip, lotNo, jissekino, bango, tanijuryo, okuriRyohinsu, shikakariData)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo);

        processData.setInitMessageList(errorMessageList);
        return processData;

    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B048Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B048Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B048Const.TOKUISAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B048Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B048Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B048Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B048Const.OWNER, ownercode + ":" + owner);
        }

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param jissekino 実績No
     * @param bango 番号
     * @param tanijuryo 単位重量
     * @param okuriRyohinsu 送り良品数
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            QueryRunner queryRunnerWip, String lotNo, int jissekino, int bango, String tanijuryo, String okuriRyohinsu,
            Map shikakariData) throws SQLException {

        List<SrTapingSagyo> srTapingSagyoDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);
        // 規格値が設定できているかの判定用フラグ
        boolean isDataGetflg = false;
        String initMessage = "";
        
        for (int i = 0; i <= processData.getItemList().size() - 1; i++) {

            // 対象の表示label1が「号機」の場合に規格値を取得する処理を実行
            if (processData.getItemList().get(i).getLabel1().equals("号機")){
                
                // 号機の規格値が設定されていない場合のｴﾗｰﾒｯｾｰｼﾞを設定
                initMessage = MessageUtil.getMessage("XHD-000019", "【" + processData.getItemList().get(i).getLabel1() + "】");
                
                // ﾊﾟﾗﾒｰﾀﾏｽﾀからﾊﾟﾗﾒｰﾀﾃﾞｰﾀを取得
                String param = loadParamData(queryRunnerDoc, "common_user", "xhd_taping_sagyo_maekoteicode");
                
                // ﾊﾟﾗﾒｰﾀﾏｽﾀが空では無い場合
                if (!StringUtil.isEmpty(param)) {
                       
                    // 取得したﾊﾟﾗﾒｰﾀﾃﾞｰﾀを「,」で分割し工程コードとして配列に格納
                    String spParam[] = param.split(",");

                    // 仕掛情報の取得(TP号機用処理)
                    Map tpShikakariData = loadTpGokiShikakariData(queryRunnerWip, lotNo, shikakariData.get("oyalotedaban").toString());
                    if (tpShikakariData == null || tpShikakariData.isEmpty()) {
                        // 仕掛ﾃﾞｰﾀが取得できなかった場合に処理を抜ける
                        break;
                    }
                    
                    // 仕掛情報(TP号機用処理)のレコード分ループ
                    for (int j = 0; j < tpShikakariData.size(); j++) {
                        
                        // 実績ﾃﾞｰﾀ(TP号機処理)を取得
                        List<Jisseki> jissekiData = loadTpJissekiData(queryRunnerWip, lotNo, spParam);
                        
                        // 実績ﾃﾞｰﾀ(TP号機処理)が取得できた場合
                        if (jissekiData != null && 0 < jissekiData.size()) {
                            
                            // 生産実績ﾃﾞｰﾀ取得
                            List<Seisan> seisanData = loadSeisanData(queryRunnerWip, jissekiData.get(0).getJissekino());
                            
                            // 生産実績ﾃﾞｰﾀが取得出来た場合
                            if (seisanData != null && seisanData.size() > 0) {
                                
                                // 号機コードを号機の規格値としてｾｯﾄ
                                processData.getItemList().get(i).setKikakuChi("【" + seisanData.get(0).getGoukicode() + "】");
                                
                                // 号機の規格値が設定できた情報をｾｯﾄ
                                isDataGetflg = true;
                                break;
                            } else {
                                // 生産実績ﾃﾞｰﾀが取得出来なかった場合
                                break;
                            }
                        }
                    }
                    break;
                } else {
                    // ﾊﾟﾗﾒｰﾀﾃﾞｰﾀが取得できなかった場合に処理を抜ける
                    break;
                }
    
            }
        }
        
        // 共通処理側で設定されている初期表示時ｴﾗｰﾒｯｾｰｼﾞﾘｽﾄがあるか
        if( processData.getInitMessageList()!= null && 0 < processData.getInitMessageList().size()) {
            // ｴﾗｰﾒｯｾｰｼﾞﾘｽﾄがあった場合、エラーリスト数分ループ
            for (String message : processData.getInitMessageList()) {
                // ｴﾗｰﾒｯｾｰｼﾞが号機の規格値が設定されていない場合のｴﾗｰﾒｯｾｰｼﾞかつ
                // 号機の規格値が設定されていた場合
                if (message.equals(initMessage) && isDataGetflg){
                    // 号機の規格値が設定されていない場合のｴﾗｰﾒｯｾｰｼﾞを削除
                    processData.getInitMessageList().remove(processData.getInitMessageList().indexOf(initMessage));
                    break;
                }
            }
        }
 
        if (!isDataGetflg){
            for (int i = 0; i < 5; i++) {
            // ﾃｰﾋﾟﾝｸﾞ号機選択Revision情報取得
                Map fxhdd12RevInfo = loadFxhdd12RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, jissekino, bango);
                rev = StringUtil.nullToBlank(getMapData(fxhdd12RevInfo, "rev"));
                jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd12RevInfo, "jotai_flg"));

                // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
                if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                    processData.setInitRev(rev);
                    processData.setInitJotaiFlg(jotaiFlg);

                    // メイン画面にデータを設定する(デフォルト値)
                    for (FXHDD01 fxhdd001 : processData.getItemList()) {
                        this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                    }

                    setItemData(processData, GXHDO101B048Const.KENSA_KAISUU, String.valueOf(jissekino));

                    //受入総重量計算処理
                    FXHDD01 itemUkeireSojuryo = getItemRow(processData.getItemList(), GXHDO101B048Const.UKEIRE_SOUJURYO);
                    FXHDD01 itemOkuriRyohinsu = getItemRow(processData.getItemList(), GXHDO101B048Const.OKURI_RYOHINSU);
                    FXHDD01 itemUkeireTanijuryo = getItemRow(processData.getItemList(), GXHDO101B048Const.UKEIRE_TANNIJURYO);
                    itemOkuriRyohinsu.setValue(okuriRyohinsu);//送り良品数
                    itemUkeireTanijuryo.setValue(NumberUtil.getTruncatData(tanijuryo, itemUkeireTanijuryo.getInputLength(), itemUkeireTanijuryo.getInputLengthDec()));//受入単位重量
                    if (checkUkeireSojuryo(itemUkeireSojuryo, itemOkuriRyohinsu, itemUkeireTanijuryo)) {
                        // ﾁｪｯｸに問題なければ値をセット
                        calcUkeireSojuryo(itemUkeireSojuryo, itemOkuriRyohinsu, itemUkeireTanijuryo);
                    }

                    return true;
                }

                // テーピング作業データ取得
                srTapingSagyoDataList = getSrTapingSagyoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino, bango);
                if (srTapingSagyoDataList.isEmpty()) {
                    //該当データが取得できなかった場合は処理を繰り返す。
                    continue;
                }

                // データが全て取得出来た場合、ループを抜ける。
                break;
            }

            // 制限回数内にデータが取得できなかった場合
            if (srTapingSagyoDataList.isEmpty()) {
                return false;
            }

            processData.setInitRev(rev);
            processData.setInitJotaiFlg(jotaiFlg);

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTapingSagyoDataList.get(0));
        }
        
        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srTapingSagyoData テーピング作業データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrTapingSagyo srTapingSagyoData) {

        //送り良品数
        this.setItemData(processData, GXHDO101B048Const.OKURI_RYOHINSU, getSrTapingSagyoItemData(GXHDO101B048Const.OKURI_RYOHINSU, srTapingSagyoData));
        //受入れ単位重量
        this.setItemData(processData, GXHDO101B048Const.UKEIRE_TANNIJURYO, getSrTapingSagyoItemData(GXHDO101B048Const.UKEIRE_TANNIJURYO, srTapingSagyoData));
        //受入れ総重量
        this.setItemData(processData, GXHDO101B048Const.UKEIRE_SOUJURYO, getSrTapingSagyoItemData(GXHDO101B048Const.UKEIRE_SOUJURYO, srTapingSagyoData));
        //検査回数
        this.setItemData(processData, GXHDO101B048Const.KENSA_KAISUU, getSrTapingSagyoItemData(GXHDO101B048Const.KENSA_KAISUU, srTapingSagyoData));
        //検査場所
        this.setItemData(processData, GXHDO101B048Const.KENSA_BASHO, getSrTapingSagyoItemData(GXHDO101B048Const.KENSA_BASHO, srTapingSagyoData));
        //号機
        this.setItemData(processData, GXHDO101B048Const.GOKI, getSrTapingSagyoItemData(GXHDO101B048Const.GOKI, srTapingSagyoData));
        //TP仕様
        this.setItemData(processData, GXHDO101B048Const.TP_SHIYO, getSrTapingSagyoItemData(GXHDO101B048Const.TP_SHIYO, srTapingSagyoData));
        //確保数
        this.setItemData(processData, GXHDO101B048Const.KAKUHOSU, getSrTapingSagyoItemData(GXHDO101B048Const.KAKUHOSU, srTapingSagyoData));
        //確保ﾘｰﾙ巻数①
        this.setItemData(processData, GXHDO101B048Const.KAKUHO_REEL_MAKISU1, getSrTapingSagyoItemData(GXHDO101B048Const.KAKUHO_REEL_MAKISU1, srTapingSagyoData));
        //確保ﾘｰﾙ本数①
        this.setItemData(processData, GXHDO101B048Const.KAKUHO_REEL_HONSU1, getSrTapingSagyoItemData(GXHDO101B048Const.KAKUHO_REEL_HONSU1, srTapingSagyoData));
        //確保ﾘｰﾙ巻数②
        this.setItemData(processData, GXHDO101B048Const.KAKUHO_REEL_MAKISU2, getSrTapingSagyoItemData(GXHDO101B048Const.KAKUHO_REEL_MAKISU2, srTapingSagyoData));
        //確保ﾘｰﾙ本数②
        this.setItemData(processData, GXHDO101B048Const.KAKUHO_REEL_HONSU2, getSrTapingSagyoItemData(GXHDO101B048Const.KAKUHO_REEL_HONSU2, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.①
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO1, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO1, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.②
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO2, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO2, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.③
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO3, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO3, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.④
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO4, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO4, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO5, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO5, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO6, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO6, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO7, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO7, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO8, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO8, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO9, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO9, srTapingSagyoData));
        //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
        this.setItemData(processData, GXHDO101B048Const.CARRIERTAPE_LOTNO10, getSrTapingSagyoItemData(GXHDO101B048Const.CARRIERTAPE_LOTNO10, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO1, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO1, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO2, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO2, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO3, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO3, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO4, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO4, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO5, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO5, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO6, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO6, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO7, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO7, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO8, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO8, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO9, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO9, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_LOTNO10, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_LOTNO10, srTapingSagyoData));
        //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
        this.setItemData(processData, GXHDO101B048Const.BOTTOMTAPE_LOTNO1, getSrTapingSagyoItemData(GXHDO101B048Const.BOTTOMTAPE_LOTNO1, srTapingSagyoData));
        //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
        this.setItemData(processData, GXHDO101B048Const.BOTTOMTAPE_LOTNO2, getSrTapingSagyoItemData(GXHDO101B048Const.BOTTOMTAPE_LOTNO2, srTapingSagyoData));
        //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
        this.setItemData(processData, GXHDO101B048Const.BOTTOMTAPE_LOTNO3, getSrTapingSagyoItemData(GXHDO101B048Const.BOTTOMTAPE_LOTNO3, srTapingSagyoData));
        //容量範囲ｾｯﾄ値 Hi
        this.setItemData(processData, GXHDO101B048Const.YORYOHANNI_SETCHI_HI, getSrTapingSagyoItemData(GXHDO101B048Const.YORYOHANNI_SETCHI_HI, srTapingSagyoData));
        //容量範囲ｾｯﾄ値 Lo
        this.setItemData(processData, GXHDO101B048Const.YORYOHANNI_SETCHI_LO, getSrTapingSagyoItemData(GXHDO101B048Const.YORYOHANNI_SETCHI_LO, srTapingSagyoData));
        //容量範囲ｾｯﾄ値単位
        this.setItemData(processData, GXHDO101B048Const.YORYOHANNI_SETCHI_TANI, getSrTapingSagyoItemData(GXHDO101B048Const.YORYOHANNI_SETCHI_TANI, srTapingSagyoData));
        //ﾚﾝｼﾞ表示
        this.setItemData(processData, GXHDO101B048Const.RANGE_HYOJI, getSrTapingSagyoItemData(GXHDO101B048Const.RANGE_HYOJI, srTapingSagyoData));
        //ﾚﾝｼﾞ表示単位
        this.setItemData(processData, GXHDO101B048Const.RANGE_HYOJI_TANI, getSrTapingSagyoItemData(GXHDO101B048Const.RANGE_HYOJI_TANI, srTapingSagyoData));
        //容量値
        this.setItemData(processData, GXHDO101B048Const.YORYOCHI, getSrTapingSagyoItemData(GXHDO101B048Const.YORYOCHI, srTapingSagyoData));
        //容量値単位
        this.setItemData(processData, GXHDO101B048Const.YORYOCHI_TANI, getSrTapingSagyoItemData(GXHDO101B048Const.YORYOCHI_TANI, srTapingSagyoData));
        //DFｾｯﾄ値 Hi
        this.setItemData(processData, GXHDO101B048Const.DF_SETCHI_HI, getSrTapingSagyoItemData(GXHDO101B048Const.DF_SETCHI_HI, srTapingSagyoData));
        //DFｾｯﾄ値 Lo
        this.setItemData(processData, GXHDO101B048Const.DF_SETCHI_LO, getSrTapingSagyoItemData(GXHDO101B048Const.DF_SETCHI_LO, srTapingSagyoData));
        //DF値
        this.setItemData(processData, GXHDO101B048Const.DF_CHI, getSrTapingSagyoItemData(GXHDO101B048Const.DF_CHI, srTapingSagyoData));
        //画像設定条件
        this.setItemData(processData, GXHDO101B048Const.GAZO_SETTEIJOKEN, getSrTapingSagyoItemData(GXHDO101B048Const.GAZO_SETTEIJOKEN, srTapingSagyoData));
        //ﾎｯﾊﾟｰﾈｼﾞ確認
        this.setItemData(processData, GXHDO101B048Const.HOPPERNEJI_KAKUNIN, getSrTapingSagyoItemData(GXHDO101B048Const.HOPPERNEJI_KAKUNIN, srTapingSagyoData));
        //ｺﾃ清掃
        this.setItemData(processData, GXHDO101B048Const.KOTE_SEISOU, getSrTapingSagyoItemData(GXHDO101B048Const.KOTE_SEISOU, srTapingSagyoData));
        //開始前に製品残なき事
        this.setItemData(processData, GXHDO101B048Const.KAISIMAESEHINZANNASI, getSrTapingSagyoItemData(GXHDO101B048Const.KAISIMAESEHINZANNASI, srTapingSagyoData));
        //SET者
        this.setItemData(processData, GXHDO101B048Const.SETSYA, getSrTapingSagyoItemData(GXHDO101B048Const.SETSYA, srTapingSagyoData));
        //Wﾁｪｯｸ者
        this.setItemData(processData, GXHDO101B048Const.W_CHECKSYA, getSrTapingSagyoItemData(GXHDO101B048Const.W_CHECKSYA, srTapingSagyoData));
        //開始日
        this.setItemData(processData, GXHDO101B048Const.KAISHI_DAY, getSrTapingSagyoItemData(GXHDO101B048Const.KAISHI_DAY, srTapingSagyoData));
        //開始時間
        this.setItemData(processData, GXHDO101B048Const.KAISHI_TIME, getSrTapingSagyoItemData(GXHDO101B048Const.KAISHI_TIME, srTapingSagyoData));
        //試験担当者
        this.setItemData(processData, GXHDO101B048Const.SHIKEN_TANTOSYA, getSrTapingSagyoItemData(GXHDO101B048Const.SHIKEN_TANTOSYA, srTapingSagyoData));
        //自重落下試験
        this.setItemData(processData, GXHDO101B048Const.JIJU_RAKKA, getSrTapingSagyoItemData(GXHDO101B048Const.JIJU_RAKKA, srTapingSagyoData));
        //ﾊﾞﾗｼﾁｪｯｸ
        this.setItemData(processData, GXHDO101B048Const.BARASHI_CHECK, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_CHECK, srTapingSagyoData));
        //ﾄｯﾌﾟﾃｰﾌﾟ確認
        this.setItemData(processData, GXHDO101B048Const.TOPTAPE_KAKUNIN, getSrTapingSagyoItemData(GXHDO101B048Const.TOPTAPE_KAKUNIN, srTapingSagyoData));
        //挿入数
        this.setItemData(processData, GXHDO101B048Const.SONYUSU, getSrTapingSagyoItemData(GXHDO101B048Const.SONYUSU, srTapingSagyoData));
        //投入数
        this.setItemData(processData, GXHDO101B048Const.TOUNYUSU, getSrTapingSagyoItemData(GXHDO101B048Const.TOUNYUSU, srTapingSagyoData));
        //良品TPﾘｰﾙ巻数①
        this.setItemData(processData, GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1, srTapingSagyoData));
        //良品TPﾘｰﾙ本数①
        this.setItemData(processData, GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1, srTapingSagyoData));
        //良品TPﾘｰﾙ巻数②
        this.setItemData(processData, GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2, srTapingSagyoData));
        //良品TPﾘｰﾙ本数②
        this.setItemData(processData, GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2, srTapingSagyoData));
        //良品数
        this.setItemData(processData, GXHDO101B048Const.RYOHINSU, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHINSU, srTapingSagyoData));
        //容量NG(NG1)
        this.setItemData(processData, GXHDO101B048Const.YORYO_NG_NG1, getSrTapingSagyoItemData(GXHDO101B048Const.YORYO_NG_NG1, srTapingSagyoData));
        //画像NG(上画像数):NG2
        this.setItemData(processData, GXHDO101B048Const.GAZO_NG_UE_GAZOSU_NG2, getSrTapingSagyoItemData(GXHDO101B048Const.GAZO_NG_UE_GAZOSU_NG2, srTapingSagyoData));
        //画像NG(下画像数):NG2
        this.setItemData(processData, GXHDO101B048Const.GAZO_NG_SHITA_GAZOSU_NG2, getSrTapingSagyoItemData(GXHDO101B048Const.GAZO_NG_SHITA_GAZOSU_NG2, srTapingSagyoData));
        //歩留まり
        this.setItemData(processData, GXHDO101B048Const.BUDOMARI, getSrTapingSagyoItemData(GXHDO101B048Const.BUDOMARI, srTapingSagyoData));
        //終了日
        this.setItemData(processData, GXHDO101B048Const.SHURYOU_DAY, getSrTapingSagyoItemData(GXHDO101B048Const.SHURYOU_DAY, srTapingSagyoData));
        //終了時間
        this.setItemData(processData, GXHDO101B048Const.SHURYOU_TIME, getSrTapingSagyoItemData(GXHDO101B048Const.SHURYOU_TIME, srTapingSagyoData));
        //ﾒﾝﾃﾅﾝｽ回数
        this.setItemData(processData, GXHDO101B048Const.MAINTENANCE_KAISU, getSrTapingSagyoItemData(GXHDO101B048Const.MAINTENANCE_KAISU, srTapingSagyoData));
        //ﾒﾝﾃ後TP外観
        this.setItemData(processData, GXHDO101B048Const.MAINTGO_TP_GAIKAN, getSrTapingSagyoItemData(GXHDO101B048Const.MAINTGO_TP_GAIKAN, srTapingSagyoData));
        //ﾊﾞﾗｼ対象ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.BARASHI_TAISYO_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_TAISYO_REELSU, srTapingSagyoData));
        //空ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.KARA_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.KARA_REELSU, srTapingSagyoData));
        //良品ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.RYOHIN_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHIN_REELSU, srTapingSagyoData));
        //QA確認依頼ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU, srTapingSagyoData));
        //ﾘｰﾙ数ﾁｪｯｸ
        this.setItemData(processData, GXHDO101B048Const.REELSU_CHECK, getSrTapingSagyoItemData(GXHDO101B048Const.REELSU_CHECK, srTapingSagyoData));
        //TP後清掃：ﾎｯﾊﾟｰ部
        this.setItemData(processData, GXHDO101B048Const.TPATO_SEISOU_HOPPER_PART, getSrTapingSagyoItemData(GXHDO101B048Const.TPATO_SEISOU_HOPPER_PART, srTapingSagyoData));
        //TP後清掃：ﾌｨｰﾀﾞ部
        this.setItemData(processData, GXHDO101B048Const.TPATO_SEISOU_FEEDER_PART, getSrTapingSagyoItemData(GXHDO101B048Const.TPATO_SEISOU_FEEDER_PART, srTapingSagyoData));
        //TP後清掃：INDEX内
        this.setItemData(processData, GXHDO101B048Const.TPATO_SEISOU_IN_INDEX, getSrTapingSagyoItemData(GXHDO101B048Const.TPATO_SEISOU_IN_INDEX, srTapingSagyoData));
        //TP後清掃：NGBOX内
        this.setItemData(processData, GXHDO101B048Const.TPATO_SEISOU_IN_NGBOX, getSrTapingSagyoItemData(GXHDO101B048Const.TPATO_SEISOU_IN_NGBOX, srTapingSagyoData));
        //清掃担当者
        this.setItemData(processData, GXHDO101B048Const.SEISOU_TANTOUSYA, getSrTapingSagyoItemData(GXHDO101B048Const.SEISOU_TANTOUSYA, srTapingSagyoData));
        //清掃確認者
        this.setItemData(processData, GXHDO101B048Const.SEISOU_KAKUNINSYA, getSrTapingSagyoItemData(GXHDO101B048Const.SEISOU_KAKUNINSYA, srTapingSagyoData));
        //ﾊﾞﾗｼ依頼ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.BARASHI_IRAI_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_IRAI_REELSU, srTapingSagyoData));
        //ﾊﾞﾗｼ開始日
        this.setItemData(processData, GXHDO101B048Const.BARASHI_KAISHI_DAY, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_KAISHI_DAY, srTapingSagyoData));
        //ﾊﾞﾗｼ開始時間
        this.setItemData(processData, GXHDO101B048Const.BARASHI_KAISHI_TIME, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_KAISHI_TIME, srTapingSagyoData));
        //ﾊﾞﾗｼ終了日
        this.setItemData(processData, GXHDO101B048Const.BARASHI_SHURYOU_DAY, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_SHURYOU_DAY, srTapingSagyoData));
        //ﾊﾞﾗｼ終了時間
        this.setItemData(processData, GXHDO101B048Const.BARASHI_SHURYOU_TIME, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_SHURYOU_TIME, srTapingSagyoData));
        //ﾊﾞﾗｼ担当者
        this.setItemData(processData, GXHDO101B048Const.BARASHI_TANTOUSYA, getSrTapingSagyoItemData(GXHDO101B048Const.BARASHI_TANTOUSYA, srTapingSagyoData));
        //脱磁依頼ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.DATSUJI_IRAI_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.DATSUJI_IRAI_REELSU, srTapingSagyoData));
        //脱磁開始日
        this.setItemData(processData, GXHDO101B048Const.DATSUJI_KAISHI_DAY, getSrTapingSagyoItemData(GXHDO101B048Const.DATSUJI_KAISHI_DAY, srTapingSagyoData));
        //脱磁開始時間
        this.setItemData(processData, GXHDO101B048Const.DATSUJI_KAISHI_TIME, getSrTapingSagyoItemData(GXHDO101B048Const.DATSUJI_KAISHI_TIME, srTapingSagyoData));
        //脱磁担当者
        this.setItemData(processData, GXHDO101B048Const.DATSUJI_TANTOUSYA, getSrTapingSagyoItemData(GXHDO101B048Const.DATSUJI_TANTOUSYA, srTapingSagyoData));
        //確保ﾘｰﾙ数
        this.setItemData(processData, GXHDO101B048Const.KAKUHO_REELSU, getSrTapingSagyoItemData(GXHDO101B048Const.KAKUHO_REELSU, srTapingSagyoData));
        //空ﾘｰﾙ数2
        this.setItemData(processData, GXHDO101B048Const.KARA_REELSU2, getSrTapingSagyoItemData(GXHDO101B048Const.KARA_REELSU2, srTapingSagyoData));
        //良品ﾘｰﾙ数2
        this.setItemData(processData, GXHDO101B048Const.RYOHIN_REELSU2, getSrTapingSagyoItemData(GXHDO101B048Const.RYOHIN_REELSU2, srTapingSagyoData));
        //QA確認依頼ﾘｰﾙ数2
        this.setItemData(processData, GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU2, getSrTapingSagyoItemData(GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU2, srTapingSagyoData));
        //最終確認担当者
        this.setItemData(processData, GXHDO101B048Const.SAISYUKAKUNIN_TANTOUSYA, getSrTapingSagyoItemData(GXHDO101B048Const.SAISYUKAKUNIN_TANTOUSYA, srTapingSagyoData));
        //備考1
        this.setItemData(processData, GXHDO101B048Const.BIKOU1, getSrTapingSagyoItemData(GXHDO101B048Const.BIKOU1, srTapingSagyoData));
        //備考2
        this.setItemData(processData, GXHDO101B048Const.BIKOU2, getSrTapingSagyoItemData(GXHDO101B048Const.BIKOU2, srTapingSagyoData));

    }

    /**
     * テーピング作業の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return テーピング作業登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrTapingSagyo> getSrTapingSagyoData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino, int bango) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrTapingSagyo(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, bango, rev);
        } else {
            return loadTmpSrTapingSagyo(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, bango, rev);
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
     * @return 設計データ関連付けリスト
     */
    private List<String[]> getMapSekkeiAssociation() {

        // 対象無し(共通的なチェック処理の為、ロジックは残しておく)
        List<String[]> list = new ArrayList<>();
        return list;
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
        String sql = "SELECT kcpno, oyalotedaban, lotkubuncode, ownercode, tokuisaki, tanijuryo"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ﾃｰﾋﾟﾝｸﾞ号機選択]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param bango 番号(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd12RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, int bango) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd12 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND bango = ?";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(bango);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [ﾃｰﾋﾟﾝｸﾞ号機選択]の状態フラグが同一のﾁｪｯｸ
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param bango 番号(検索キー)
     * @return ﾁｪｯｸ結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private Boolean jotaiCheckFxhdd12(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, int jissekino, String jotai, int bango) throws SQLException {
        // 設計データの取得
        // 登録されている件数と更新内容が一致している件数を比較
        String sql = "SELECT CASE A.check WHEN B.count THEN true ELSE false END AS jotaicheck "
                + "FROM (SELECT COUNT(*) + 1 AS check FROM fxhdd12 WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? "
                + "AND jotai_flg = ? AND bango != ?) AS A, "
                + "(SELECT COUNT(*) AS count FROM fxhdd12 WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ?) AS B";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(jotai);
        params.add(bango);
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map checkMap = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
        return "true".equals(StringUtil.nullToBlank(getMapData(checkMap, "jotaicheck")));
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
        // 実績データの取得
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
     * [ﾃｰﾋﾟﾝｸﾞ号機選択]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param bango 番号(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd12RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, int bango) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd12 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND bango = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(bango);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機選択の最大リビジョン+1のデータを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param jissekino 実績No(検索キー)
     * @param bango 番号(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getFxhdd12NewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, int jissekino, int bango) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd12 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND jissekino = ? AND bango = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(bango);
        Map map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            newRev = new BigDecimal(String.valueOf(map.get("rev")));
            newRev = newRev.add(BigDecimal.ONE);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return newRev;
    }

    /**
     * [テーピング作業]から、ﾃﾞｰﾀを取得
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
    private List<SrTapingSagyo> loadSrTapingSagyo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, int bango, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,kensabasyo,gouki,tpsiyou,"
                + "kakuhosu,kakuhoreelmaki1,kakuhoreelhonsu1,kakuhoreelmaki2,kakuhoreelhonsu2,tapelotno1,tapelotno2,tapelotno3,tapelotno4,tapelotno5,tapelotno6,"
                + "tapelotno7,tapelotno8,tapelotno9,tapelotno10,toptapelotno1,toptapelotno2,toptapelotno3,toptapelotno4,toptapelotno5,toptapelotno6,toptapelotno7,"
                + "toptapelotno8,toptapelotno9,toptapelotno10,bottomtapelot1,bottomtapelot2,bottomtapelot3,yoryohannihi,yoryohannilo,yoryohannitanni,rangehyoji,"
                + "rangehyojitanni,youryou,youryoutanni,dfsethi,dfsetlo,dfatai,gazousetteijyoken,hoperneji,koteseisou,kaisimaesehinzannasi,setsya,kakuninsya,"
                + "kaisinichiji,sikentantou,jijyurakkasiken,barasicheck,toptapekakunin,sounyuusu,tounyuusu,ryouhintopreelmaki1,ryouhintopreelhonsu1,"
                + "ryouhintopreelmaki2,ryouhintopreelhonsu2,ryouhinsu,youryong1,gazoungue2,gazoungsita2,budomari,shuryonichiji,mentekaisu,mentegotpgaikan,"
                + "barasitaisyoreelsu,akireelsu,ryouhinreelsu,qakakuniniraireelsu,reelsucheck,tpatohopper,tpatofeeder,tpatoindex,tpatongbox,seisoutantou,"
                + "seisoukakunin,barasiiraireelsu,barasikaisinichiji,barasiksyuryonichiji,barasitantou,datujiiraireelsu,datujikaisinichiji,datujitantou,"
                + "kakuhoreelsu,akireelsu2,ryouhinreelsu2,qakakuniniraireelsu2,saisyukakuninn,bikou1,bikou2,torokunichiji,kosinnichiji,revision,'0' AS deleteflag,bango "
                + "FROM sr_taping_sagyo "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND bango = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(bango);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kaisuu", "kaisuu"); //検査回数
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); //送り良品数
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("gouki", "gouki"); //号機
        mapping.put("tpsiyou", "tpsiyou"); //TP仕様
        mapping.put("kakuhosu", "kakuhosu"); //確保数
        mapping.put("kakuhoreelmaki1", "kakuhoreelmaki1"); //確保ﾘｰﾙ巻数①
        mapping.put("kakuhoreelhonsu1", "kakuhoreelhonsu1"); //確保ﾘｰﾙ本数①
        mapping.put("kakuhoreelmaki2", "kakuhoreelmaki2"); //確保ﾘｰﾙ巻数②
        mapping.put("kakuhoreelhonsu2", "kakuhoreelhonsu2"); //確保ﾘｰﾙ本数②
        mapping.put("tapelotno1", "tapelotno1"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.①
        mapping.put("tapelotno2", "tapelotno2"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.②
        mapping.put("tapelotno3", "tapelotno3"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.③
        mapping.put("tapelotno4", "tapelotno4"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.④
        mapping.put("tapelotno5", "tapelotno5"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
        mapping.put("tapelotno6", "tapelotno6"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
        mapping.put("tapelotno7", "tapelotno7"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
        mapping.put("tapelotno8", "tapelotno8"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
        mapping.put("tapelotno9", "tapelotno9"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
        mapping.put("tapelotno10", "tapelotno10"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
        mapping.put("toptapelotno1", "toptapelotno1"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
        mapping.put("toptapelotno2", "toptapelotno2"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
        mapping.put("toptapelotno3", "toptapelotno3"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
        mapping.put("toptapelotno4", "toptapelotno4"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
        mapping.put("toptapelotno5", "toptapelotno5"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
        mapping.put("toptapelotno6", "toptapelotno6"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
        mapping.put("toptapelotno7", "toptapelotno7"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
        mapping.put("toptapelotno8", "toptapelotno8"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
        mapping.put("toptapelotno9", "toptapelotno9"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
        mapping.put("toptapelotno10", "toptapelotno10"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
        mapping.put("bottomtapelot1", "bottomtapelot1"); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
        mapping.put("bottomtapelot2", "bottomtapelot2"); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
        mapping.put("bottomtapelot3", "bottomtapelot3"); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
        mapping.put("yoryohannihi", "yoryohannihi"); //容量範囲ｾｯﾄ値 Hi
        mapping.put("yoryohannilo", "yoryohannilo"); //容量範囲ｾｯﾄ値 Lo
        mapping.put("yoryohannitanni", "yoryohannitanni"); //容量範囲ｾｯﾄ値単位
        mapping.put("rangehyoji", "rangehyoji"); //ﾚﾝｼﾞ表示
        mapping.put("rangehyojitanni", "rangehyojitanni"); //ﾚﾝｼﾞ表示単位
        mapping.put("youryou", "youryou"); //容量値
        mapping.put("youryoutanni", "youryoutanni"); //容量値単位
        mapping.put("dfsethi", "dfsethi"); //DFｾｯﾄ値 Hi
        mapping.put("dfsetlo", "dfsetlo"); //DFｾｯﾄ値 Lo
        mapping.put("dfatai", "dfatai"); //DF値
        mapping.put("gazousetteijyoken", "gazousetteijyoken"); //画像設定条件
        mapping.put("hoperneji", "hoperneji"); //ﾎｯﾊﾟｰﾈｼﾞ確認
        mapping.put("koteseisou", "koteseisou"); //ｺﾃ清掃
        mapping.put("kaisimaesehinzannasi", "kaisimaesehinzannasi"); //開始前に製品残なき事
        mapping.put("setsya", "setsya"); //SET者
        mapping.put("kakuninsya", "kakuninsya"); //Wﾁｪｯｸ者
        mapping.put("kaisinichiji", "kaisinichiji"); //開始日時
        mapping.put("sikentantou", "sikentantou"); //試験担当者
        mapping.put("jijyurakkasiken", "jijyurakkasiken"); //自重落下試験
        mapping.put("barasicheck", "barasicheck"); //ﾊﾞﾗｼﾁｪｯｸ
        mapping.put("toptapekakunin", "toptapekakunin"); //ﾄｯﾌﾟﾃｰﾌﾟ確認
        mapping.put("sounyuusu", "sounyuusu"); //挿入数
        mapping.put("tounyuusu", "tounyuusu"); //投入数
        mapping.put("ryouhintopreelmaki1", "ryouhintopreelmaki1"); //良品TPﾘｰﾙ巻数①
        mapping.put("ryouhintopreelhonsu1", "ryouhintopreelhonsu1"); //良品TPﾘｰﾙ本数①
        mapping.put("ryouhintopreelmaki2", "ryouhintopreelmaki2"); //良品TPﾘｰﾙ巻数②
        mapping.put("ryouhintopreelhonsu2", "ryouhintopreelhonsu2"); //良品TPﾘｰﾙ本数②
        mapping.put("ryouhinsu", "ryouhinsu"); //良品数
        mapping.put("youryong1", "youryong1"); //容量NG(NG1)
        mapping.put("gazoungue2", "gazoungue2"); //画像NG(上画像数):NG2
        mapping.put("gazoungsita2", "gazoungsita2"); //画像NG(下画像数):NG2
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("shuryonichiji", "shuryonichiji"); //終了日時
        mapping.put("mentekaisu", "mentekaisu"); //ﾒﾝﾃﾅﾝｽ回数
        mapping.put("mentegotpgaikan", "mentegotpgaikan"); //ﾒﾝﾃ後TP外観
        mapping.put("barasitaisyoreelsu", "barasitaisyoreelsu"); //ﾊﾞﾗｼ対象ﾘｰﾙ数
        mapping.put("akireelsu", "akireelsu"); //空ﾘｰﾙ数
        mapping.put("ryouhinreelsu", "ryouhinreelsu"); //良品ﾘｰﾙ数
        mapping.put("qakakuniniraireelsu", "qakakuniniraireelsu"); //QA確認依頼ﾘｰﾙ数
        mapping.put("reelsucheck", "reelsucheck"); //ﾘｰﾙ数ﾁｪｯｸ
        mapping.put("tpatohopper", "tpatohopper"); //TP後清掃：ﾎｯﾊﾟｰ部
        mapping.put("tpatofeeder", "tpatofeeder"); //TP後清掃：ﾌｨｰﾀﾞ部
        mapping.put("tpatoindex", "tpatoindex"); //TP後清掃：INDEX内
        mapping.put("tpatongbox", "tpatongbox"); //TP後清掃：NGBOX内
        mapping.put("seisoutantou", "seisoutantou"); //清掃担当者
        mapping.put("seisoukakunin", "seisoukakunin"); //清掃確認者
        mapping.put("barasiiraireelsu", "barasiiraireelsu"); //ﾊﾞﾗｼ依頼ﾘｰﾙ数
        mapping.put("barasikaisinichiji", "barasikaisinichiji"); //ﾊﾞﾗｼ開始日時
        mapping.put("barasiksyuryonichiji", "barasiksyuryonichiji"); //ﾊﾞﾗｼ終了日時
        mapping.put("barasitantou", "barasitantou"); //ﾊﾞﾗｼ担当者
        mapping.put("datujiiraireelsu", "datujiiraireelsu"); //脱磁依頼ﾘｰﾙ数
        mapping.put("datujikaisinichiji", "datujikaisinichiji"); //脱磁開始日時
        mapping.put("datujitantou", "datujitantou"); //脱磁担当者
        mapping.put("kakuhoreelsu", "kakuhoreelsu"); //確保ﾘｰﾙ数2
        mapping.put("akireelsu2", "akireelsu2"); //空ﾘｰﾙ数2
        mapping.put("ryouhinreelsu2", "ryouhinreelsu2"); //良品ﾘｰﾙ数2
        mapping.put("qakakuniniraireelsu2", "qakakuniniraireelsu2"); //QA確認依頼ﾘｰﾙ数2
        mapping.put("saisyukakuninn", "saisyukakuninn"); //最終確認担当者
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("bango", "bango"); //番号

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTapingSagyo>> beanHandler = new BeanListHandler<>(SrTapingSagyo.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [テーピング作業_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrTapingSagyo> loadTmpSrTapingSagyo(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, int bango, String rev) throws SQLException {
        String sql = "SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,kensabasyo,gouki,tpsiyou,"
                + "kakuhosu,kakuhoreelmaki1,kakuhoreelhonsu1,kakuhoreelmaki2,kakuhoreelhonsu2,tapelotno1,tapelotno2,tapelotno3,tapelotno4,tapelotno5,tapelotno6,"
                + "tapelotno7,tapelotno8,tapelotno9,tapelotno10,toptapelotno1,toptapelotno2,toptapelotno3,toptapelotno4,toptapelotno5,toptapelotno6,toptapelotno7,"
                + "toptapelotno8,toptapelotno9,toptapelotno10,bottomtapelot1,bottomtapelot2,bottomtapelot3,yoryohannihi,yoryohannilo,yoryohannitanni,rangehyoji,"
                + "rangehyojitanni,youryou,youryoutanni,dfsethi,dfsetlo,dfatai,gazousetteijyoken,hoperneji,koteseisou,kaisimaesehinzannasi,setsya,kakuninsya,"
                + "kaisinichiji,sikentantou,jijyurakkasiken,barasicheck,toptapekakunin,sounyuusu,tounyuusu,ryouhintopreelmaki1,ryouhintopreelhonsu1,"
                + "ryouhintopreelmaki2,ryouhintopreelhonsu2,ryouhinsu,youryong1,gazoungue2,gazoungsita2,budomari,shuryonichiji,mentekaisu,mentegotpgaikan,"
                + "barasitaisyoreelsu,akireelsu,ryouhinreelsu,qakakuniniraireelsu,reelsucheck,tpatohopper,tpatofeeder,tpatoindex,tpatongbox,seisoutantou,"
                + "seisoukakunin,barasiiraireelsu,barasikaisinichiji,barasiksyuryonichiji,barasitantou,datujiiraireelsu,datujikaisinichiji,datujitantou,"
                + "kakuhoreelsu,akireelsu2,ryouhinreelsu2,qakakuniniraireelsu2,saisyukakuninn,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag,bango "
                + "FROM tmp_sr_taping_sagyo "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND kaisuu = ? AND deleteflag = ? AND bango = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(0);
        params.add(bango);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("kaisuu", "kaisuu"); //検査回数
        mapping.put("tokuisaki", "tokuisaki"); //客先
        mapping.put("lotkubuncode", "lotkubuncode"); //ﾛｯﾄ区分
        mapping.put("ownercode", "ownercode"); //ｵｰﾅｰ
        mapping.put("okuriryouhinsuu", "okuriryouhinsuu"); //送り良品数
        mapping.put("ukeiretannijyuryo", "ukeiretannijyuryo"); //受入れ単位重量
        mapping.put("ukeiresoujyuryou", "ukeiresoujyuryou"); //受入れ総重量
        mapping.put("kensabasyo", "kensabasyo"); //検査場所
        mapping.put("gouki", "gouki"); //号機
        mapping.put("tpsiyou", "tpsiyou"); //TP仕様
        mapping.put("kakuhosu", "kakuhosu"); //確保数
        mapping.put("kakuhoreelmaki1", "kakuhoreelmaki1"); //確保ﾘｰﾙ巻数①
        mapping.put("kakuhoreelhonsu1", "kakuhoreelhonsu1"); //確保ﾘｰﾙ本数①
        mapping.put("kakuhoreelmaki2", "kakuhoreelmaki2"); //確保ﾘｰﾙ巻数②
        mapping.put("kakuhoreelhonsu2", "kakuhoreelhonsu2"); //確保ﾘｰﾙ本数②
        mapping.put("tapelotno1", "tapelotno1"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.①
        mapping.put("tapelotno2", "tapelotno2"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.②
        mapping.put("tapelotno3", "tapelotno3"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.③
        mapping.put("tapelotno4", "tapelotno4"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.④
        mapping.put("tapelotno5", "tapelotno5"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
        mapping.put("tapelotno6", "tapelotno6"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
        mapping.put("tapelotno7", "tapelotno7"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
        mapping.put("tapelotno8", "tapelotno8"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
        mapping.put("tapelotno9", "tapelotno9"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
        mapping.put("tapelotno10", "tapelotno10"); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
        mapping.put("toptapelotno1", "toptapelotno1"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
        mapping.put("toptapelotno2", "toptapelotno2"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
        mapping.put("toptapelotno3", "toptapelotno3"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
        mapping.put("toptapelotno4", "toptapelotno4"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
        mapping.put("toptapelotno5", "toptapelotno5"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
        mapping.put("toptapelotno6", "toptapelotno6"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
        mapping.put("toptapelotno7", "toptapelotno7"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
        mapping.put("toptapelotno8", "toptapelotno8"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
        mapping.put("toptapelotno9", "toptapelotno9"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
        mapping.put("toptapelotno10", "toptapelotno10"); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
        mapping.put("bottomtapelot1", "bottomtapelot1"); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
        mapping.put("bottomtapelot2", "bottomtapelot2"); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
        mapping.put("bottomtapelot3", "bottomtapelot3"); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
        mapping.put("yoryohannihi", "yoryohannihi"); //容量範囲ｾｯﾄ値 Hi
        mapping.put("yoryohannilo", "yoryohannilo"); //容量範囲ｾｯﾄ値 Lo
        mapping.put("yoryohannitanni", "yoryohannitanni"); //容量範囲ｾｯﾄ値単位
        mapping.put("rangehyoji", "rangehyoji"); //ﾚﾝｼﾞ表示
        mapping.put("rangehyojitanni", "rangehyojitanni"); //ﾚﾝｼﾞ表示単位
        mapping.put("youryou", "youryou"); //容量値
        mapping.put("youryoutanni", "youryoutanni"); //容量値単位
        mapping.put("dfsethi", "dfsethi"); //DFｾｯﾄ値 Hi
        mapping.put("dfsetlo", "dfsetlo"); //DFｾｯﾄ値 Lo
        mapping.put("dfatai", "dfatai"); //DF値
        mapping.put("gazousetteijyoken", "gazousetteijyoken"); //画像設定条件
        mapping.put("hoperneji", "hoperneji"); //ﾎｯﾊﾟｰﾈｼﾞ確認
        mapping.put("koteseisou", "koteseisou"); //ｺﾃ清掃
        mapping.put("kaisimaesehinzannasi", "kaisimaesehinzannasi"); //開始前に製品残なき事
        mapping.put("setsya", "setsya"); //SET者
        mapping.put("kakuninsya", "kakuninsya"); //Wﾁｪｯｸ者
        mapping.put("kaisinichiji", "kaisinichiji"); //開始日時
        mapping.put("sikentantou", "sikentantou"); //試験担当者
        mapping.put("jijyurakkasiken", "jijyurakkasiken"); //自重落下試験
        mapping.put("barasicheck", "barasicheck"); //ﾊﾞﾗｼﾁｪｯｸ
        mapping.put("toptapekakunin", "toptapekakunin"); //ﾄｯﾌﾟﾃｰﾌﾟ確認
        mapping.put("sounyuusu", "sounyuusu"); //挿入数
        mapping.put("tounyuusu", "tounyuusu"); //投入数
        mapping.put("ryouhintopreelmaki1", "ryouhintopreelmaki1"); //良品TPﾘｰﾙ巻数①
        mapping.put("ryouhintopreelhonsu1", "ryouhintopreelhonsu1"); //良品TPﾘｰﾙ本数①
        mapping.put("ryouhintopreelmaki2", "ryouhintopreelmaki2"); //良品TPﾘｰﾙ巻数②
        mapping.put("ryouhintopreelhonsu2", "ryouhintopreelhonsu2"); //良品TPﾘｰﾙ本数②
        mapping.put("ryouhinsu", "ryouhinsu"); //良品数
        mapping.put("youryong1", "youryong1"); //容量NG(NG1)
        mapping.put("gazoungue2", "gazoungue2"); //画像NG(上画像数):NG2
        mapping.put("gazoungsita2", "gazoungsita2"); //画像NG(下画像数):NG2
        mapping.put("budomari", "budomari"); //歩留まり
        mapping.put("shuryonichiji", "shuryonichiji"); //終了日時
        mapping.put("mentekaisu", "mentekaisu"); //ﾒﾝﾃﾅﾝｽ回数
        mapping.put("mentegotpgaikan", "mentegotpgaikan"); //ﾒﾝﾃ後TP外観
        mapping.put("barasitaisyoreelsu", "barasitaisyoreelsu"); //ﾊﾞﾗｼ対象ﾘｰﾙ数
        mapping.put("akireelsu", "akireelsu"); //空ﾘｰﾙ数
        mapping.put("ryouhinreelsu", "ryouhinreelsu"); //良品ﾘｰﾙ数
        mapping.put("qakakuniniraireelsu", "qakakuniniraireelsu"); //QA確認依頼ﾘｰﾙ数
        mapping.put("reelsucheck", "reelsucheck"); //ﾘｰﾙ数ﾁｪｯｸ
        mapping.put("tpatohopper", "tpatohopper"); //TP後清掃：ﾎｯﾊﾟｰ部
        mapping.put("tpatofeeder", "tpatofeeder"); //TP後清掃：ﾌｨｰﾀﾞ部
        mapping.put("tpatoindex", "tpatoindex"); //TP後清掃：INDEX内
        mapping.put("tpatongbox", "tpatongbox"); //TP後清掃：NGBOX内
        mapping.put("seisoutantou", "seisoutantou"); //清掃担当者
        mapping.put("seisoukakunin", "seisoukakunin"); //清掃確認者
        mapping.put("barasiiraireelsu", "barasiiraireelsu"); //ﾊﾞﾗｼ依頼ﾘｰﾙ数
        mapping.put("barasikaisinichiji", "barasikaisinichiji"); //ﾊﾞﾗｼ開始日時
        mapping.put("barasiksyuryonichiji", "barasiksyuryonichiji"); //ﾊﾞﾗｼ終了日時
        mapping.put("barasitantou", "barasitantou"); //ﾊﾞﾗｼ担当者
        mapping.put("datujiiraireelsu", "datujiiraireelsu"); //脱磁依頼ﾘｰﾙ数
        mapping.put("datujikaisinichiji", "datujikaisinichiji"); //脱磁開始日時
        mapping.put("datujitantou", "datujitantou"); //脱磁担当者
        mapping.put("kakuhoreelsu", "kakuhoreelsu"); //確保ﾘｰﾙ数2
        mapping.put("akireelsu2", "akireelsu2"); //空ﾘｰﾙ数2
        mapping.put("ryouhinreelsu2", "ryouhinreelsu2"); //良品ﾘｰﾙ数2
        mapping.put("qakakuniniraireelsu2", "qakakuniniraireelsu2"); //QA確認依頼ﾘｰﾙ数2
        mapping.put("saisyukakuninn", "saisyukakuninn"); //最終確認担当者
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("bango", "bango"); //番号

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrTapingSagyo>> beanHandler = new BeanListHandler<>(SrTapingSagyo.class, rowProcessor);

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
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData edabanCopy(ProcessData processData) {
        try {

            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            int jissekino = (Integer) session.getAttribute("jissekino");
            int bango = (Integer) session.getAttribute("bango");
            String goki = StringUtil.nullToBlank(session.getAttribute("goki"));

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd12RevInfo = loadFxhdd12RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, jissekino, bango);
            if (fxhdd12RevInfo == null || fxhdd12RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd12RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // テーピング作業データ取得
            List<SrTapingSagyo> srTapingSagyoDataList = getSrTapingSagyoData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekino, bango);
            if (srTapingSagyoDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srTapingSagyoDataList.get(0));

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
     * 確保数計算押下処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doKakuhosuKeisan(ProcessData processData) {

        // 確保数計算チェック処理
        ErrorMessageInfo errorMessageInfo = calcKakuhosuCheck(processData);
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        //確保数計算
        calcKakuhosu(processData);

        processData.setMethod("");
        return processData;
    }

    /**
     * 良品数計算押下処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doRyohinsuKeisan(ProcessData processData) {

        // 良品数計算チェック処理
        ErrorMessageInfo errorMessageInfo = calcRyohinsuCheck(processData);
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        //良品数計算
        calcRyohinsu(processData);

        processData.setMethod("");
        return processData;
    }

    /**
     * 歩留まり計算計算押下処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBudomariKeisan(ProcessData processData) {

        // 歩留まり計算チェック処理
        ErrorMessageInfo errorMessageInfo = calcBudomariCheck(processData);
        if (errorMessageInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(errorMessageInfo));
            return processData;
        }

        //歩留まり計算
        calcBudomari(processData);

        processData.setMethod("");
        return processData;
    }

    /**
     * 受入れ総重量計算(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData confUkeireSojuryoKeisan(ProcessData processData) {

        processData.setMethod("");
        // チェック処理で計算対象外の場合はそのままリターン
        if (!checkUkeireSojuryo(getItemRow(processData.getItemList(), GXHDO101B048Const.UKEIRE_SOUJURYO),
                getItemRow(processData.getItemList(), GXHDO101B048Const.OKURI_RYOHINSU),
                getItemRow(processData.getItemList(), GXHDO101B048Const.UKEIRE_TANNIJURYO))) {
            return processData;
        }

        // 受入総重量が入力されている場合は警告メッセージを表示
        if (!StringUtil.isEmpty(getItemData(processData.getItemList(), GXHDO101B048Const.UKEIRE_SOUJURYO, null))) {
            // 警告メッセージの設定
            processData.setWarnMessage(MessageUtil.getMessage("XHD-000180"));
        }

        // 後続処理メソッド設定
        processData.setMethod("doUkeireSojuryoKeisan");

        return processData;

    }

    /**
     * 受入れ総重量計算処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doUkeireSojuryoKeisan(ProcessData processData) {

        processData.setMethod("");

        //受入総重量計算処理
        calcUkeireSojuryo(getItemRow(processData.getItemList(), GXHDO101B048Const.UKEIRE_SOUJURYO),
                getItemRow(processData.getItemList(), GXHDO101B048Const.OKURI_RYOHINSU),
                getItemRow(processData.getItemList(), GXHDO101B048Const.UKEIRE_TANNIJURYO));

        return processData;
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
     * @param srTapingSagyoData テーピング作業
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrTapingSagyo srTapingSagyoData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srTapingSagyoData != null) {
            // 元データが存在する場合元データより取得
            return getSrTapingSagyoItemData(itemId, srTapingSagyoData);
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
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
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
     * テーピング作業_仮登録(tmp_sr_taping_sagyo)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, int bango, Timestamp systemTime, List<FXHDD01> itemList, Map hiddenDataMap) throws SQLException {

        String sql = "INSERT INTO tmp_sr_taping_sagyo ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,kensabasyo,gouki,tpsiyou,"
                + "kakuhosu,kakuhoreelmaki1,kakuhoreelhonsu1,kakuhoreelmaki2,kakuhoreelhonsu2,tapelotno1,tapelotno2,tapelotno3,tapelotno4,tapelotno5,tapelotno6,"
                + "tapelotno7,tapelotno8,tapelotno9,tapelotno10,toptapelotno1,toptapelotno2,toptapelotno3,toptapelotno4,toptapelotno5,toptapelotno6,toptapelotno7,"
                + "toptapelotno8,toptapelotno9,toptapelotno10,bottomtapelot1,bottomtapelot2,bottomtapelot3,yoryohannihi,yoryohannilo,yoryohannitanni,rangehyoji,"
                + "rangehyojitanni,youryou,youryoutanni,dfsethi,dfsetlo,dfatai,gazousetteijyoken,hoperneji,koteseisou,kaisimaesehinzannasi,setsya,kakuninsya,"
                + "kaisinichiji,sikentantou,jijyurakkasiken,barasicheck,toptapekakunin,sounyuusu,tounyuusu,ryouhintopreelmaki1,ryouhintopreelhonsu1,"
                + "ryouhintopreelmaki2,ryouhintopreelhonsu2,ryouhinsu,youryong1,gazoungue2,gazoungsita2,budomari,shuryonichiji,mentekaisu,mentegotpgaikan,"
                + "barasitaisyoreelsu,akireelsu,ryouhinreelsu,qakakuniniraireelsu,reelsucheck,tpatohopper,tpatofeeder,tpatoindex,tpatongbox,seisoutantou,"
                + "seisoukakunin,barasiiraireelsu,barasikaisinichiji,barasiksyuryonichiji,barasitantou,datujiiraireelsu,datujikaisinichiji,datujitantou,"
                + "kakuhoreelsu,akireelsu2,ryouhinreelsu2,qakakuniniraireelsu2,saisyukakuninn,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag,bango "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrTapingSagyo(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, bango, systemTime, itemList, null, hiddenDataMap);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());

    }

    /**
     * テーピング作業_仮登録(tmp_sr_taping_sagyo)更新処理
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
     * @param bango 番号
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, int bango, Timestamp systemTime, List<FXHDD01> itemList, Map hiddenDataMap) throws SQLException {

        String sql = "UPDATE tmp_sr_taping_sagyo SET "
                + "kcpno = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,okuriryouhinsuu = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,kensabasyo = ?,gouki = ?,"
                + "tpsiyou = ?,kakuhosu = ?,kakuhoreelmaki1 = ?,kakuhoreelhonsu1 = ?,kakuhoreelmaki2 = ?,kakuhoreelhonsu2 = ?,tapelotno1 = ?,tapelotno2 = ?,tapelotno3 = ?,"
                + "tapelotno4 = ?,tapelotno5 = ?,tapelotno6 = ?,tapelotno7 = ?,tapelotno8 = ?,tapelotno9 = ?,tapelotno10 = ?,toptapelotno1 = ?,toptapelotno2 = ?,toptapelotno3 = ?,"
                + "toptapelotno4 = ?,toptapelotno5 = ?,toptapelotno6 = ?,toptapelotno7 = ?,toptapelotno8 = ?,toptapelotno9 = ?,toptapelotno10 = ?,bottomtapelot1 = ?,"
                + "bottomtapelot2 = ?,bottomtapelot3 = ?,yoryohannihi = ?,yoryohannilo = ?,yoryohannitanni = ?,rangehyoji = ?,rangehyojitanni = ?,youryou = ?,youryoutanni = ?,"
                + "dfsethi = ?,dfsetlo = ?,dfatai = ?,gazousetteijyoken = ?,hoperneji = ?,koteseisou = ?,kaisimaesehinzannasi = ?,setsya = ?,kakuninsya = ?,kaisinichiji = ?,"
                + "sikentantou = ?,jijyurakkasiken = ?,barasicheck = ?,toptapekakunin = ?,sounyuusu = ?,tounyuusu = ?,ryouhintopreelmaki1 = ?,ryouhintopreelhonsu1 = ?,"
                + "ryouhintopreelmaki2 = ?,ryouhintopreelhonsu2 = ?,ryouhinsu = ?,youryong1 = ?,gazoungue2 = ?,gazoungsita2 = ?,budomari = ?,shuryonichiji = ?,mentekaisu = ?,"
                + "mentegotpgaikan = ?,barasitaisyoreelsu = ?,akireelsu = ?,ryouhinreelsu = ?,qakakuniniraireelsu = ?,reelsucheck = ?,tpatohopper = ?,tpatofeeder = ?,tpatoindex = ?,"
                + "tpatongbox = ?,seisoutantou = ?,seisoukakunin = ?,barasiiraireelsu = ?,barasikaisinichiji = ?,barasiksyuryonichiji = ?,barasitantou = ?,datujiiraireelsu = ?,"
                + "datujikaisinichiji = ?,datujitantou = ?,kakuhoreelsu = ?,akireelsu2 = ?,ryouhinreelsu2 = ?,qakakuniniraireelsu2 = ?,saisyukakuninn = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? AND bango = ? ";

        // 更新前の値を取得
        List<SrTapingSagyo> srTapingSagyoList = getSrTapingSagyoData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino, bango);
        SrTapingSagyo srTapingSagyo = null;
        if (!srTapingSagyoList.isEmpty()) {
            srTapingSagyo = srTapingSagyoList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrTapingSagyo(false, newRev, 0, "", "", "", jissekino, bango, systemTime, itemList, srTapingSagyo, hiddenDataMap);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        params.add(bango);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピング作業_仮登録(tmp_sr_taping_sagyo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param bango 番号
     * @param jissekino 実績No
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, int bango) throws SQLException {

        String sql = "DELETE FROM tmp_sr_taping_sagyo "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? AND bango = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        params.add(bango);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピング作業_仮登録(tmp_sr_taping_sagyo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTapingSagyoData テーピング作業データ
     * @param hiddenDataMap 保持データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrTapingSagyo(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, int bango, Timestamp systemTime, List<FXHDD01> itemList, SrTapingSagyo srTapingSagyoData, Map hiddenDataMap) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //検査回数
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KCPNO, srTapingSagyoData))); //KCPNO
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOKUISAKI, srTapingSagyoData))); //客先
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("lotkubuncode")))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObjectDefaultNull(StringUtil.nullToBlank(hiddenDataMap.get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.OKURI_RYOHINSU, srTapingSagyoData))); //送り良品数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.UKEIRE_TANNIJURYO, srTapingSagyoData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.UKEIRE_SOUJURYO, srTapingSagyoData))); //受入れ総重量
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KENSA_BASHO, srTapingSagyoData))); //検査場所
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.GOKI, srTapingSagyoData))); //号機
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TP_SHIYO, srTapingSagyoData))); //TP仕様
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAKUHOSU, srTapingSagyoData))); //確保数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_MAKISU1, srTapingSagyoData))); //確保ﾘｰﾙ巻数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_HONSU1, srTapingSagyoData))); //確保ﾘｰﾙ本数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_MAKISU2, srTapingSagyoData))); //確保ﾘｰﾙ巻数②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_HONSU2, srTapingSagyoData))); //確保ﾘｰﾙ本数②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO1, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO2, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO3, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO4, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.④
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO5, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO6, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO7, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO8, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO9, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO10, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO1, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO2, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO3, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO4, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO5, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO6, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO7, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO8, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO9, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO10, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BOTTOMTAPE_LOTNO1, srTapingSagyoData))); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BOTTOMTAPE_LOTNO2, srTapingSagyoData))); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BOTTOMTAPE_LOTNO3, srTapingSagyoData))); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.YORYOHANNI_SETCHI_HI, srTapingSagyoData))); //容量範囲ｾｯﾄ値 Hi
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.YORYOHANNI_SETCHI_LO, srTapingSagyoData))); //容量範囲ｾｯﾄ値 Lo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.YORYOHANNI_SETCHI_TANI, srTapingSagyoData))); //容量範囲ｾｯﾄ値単位
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RANGE_HYOJI, srTapingSagyoData))); //ﾚﾝｼﾞ表示
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RANGE_HYOJI_TANI, srTapingSagyoData))); //ﾚﾝｼﾞ表示単位
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.YORYOCHI, srTapingSagyoData))); //容量値
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.YORYOCHI_TANI, srTapingSagyoData))); //容量値単位
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.DF_SETCHI_HI, srTapingSagyoData))); //DFｾｯﾄ値 Hi
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.DF_SETCHI_LO, srTapingSagyoData))); //DFｾｯﾄ値 Lo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.DF_CHI, srTapingSagyoData))); //DF値
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.GAZO_SETTEIJOKEN, srTapingSagyoData))); //画像設定条件
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.HOPPERNEJI_KAKUNIN, srTapingSagyoData))); //ﾎｯﾊﾟｰﾈｼﾞ確認
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KOTE_SEISOU, srTapingSagyoData))); //ｺﾃ清掃
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAISIMAESEHINZANNASI, srTapingSagyoData))); //開始前に製品残なき事
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SETSYA, srTapingSagyoData))); //SET者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.W_CHECKSYA, srTapingSagyoData))); //Wﾁｪｯｸ者
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAISHI_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.KAISHI_TIME, srTapingSagyoData))); //開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SHIKEN_TANTOSYA, srTapingSagyoData))); //試験担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.JIJU_RAKKA, srTapingSagyoData))); //自重落下試験
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BARASHI_CHECK, srTapingSagyoData))); //ﾊﾞﾗｼﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOPTAPE_KAKUNIN, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟ確認
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SONYUSU, srTapingSagyoData))); //挿入数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TOUNYUSU, srTapingSagyoData))); //投入数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1, srTapingSagyoData))); //良品TPﾘｰﾙ巻数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1, srTapingSagyoData))); //良品TPﾘｰﾙ本数①
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2, srTapingSagyoData))); //良品TPﾘｰﾙ巻数②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2, srTapingSagyoData))); //良品TPﾘｰﾙ本数②
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHINSU, srTapingSagyoData))); //良品数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.YORYO_NG_NG1, srTapingSagyoData))); //容量NG(NG1)
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.GAZO_NG_UE_GAZOSU_NG2, srTapingSagyoData))); //画像NG(上画像数):NG2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.GAZO_NG_SHITA_GAZOSU_NG2, srTapingSagyoData))); //画像NG(下画像数):NG2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BUDOMARI, srTapingSagyoData))); //歩留まり
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SHURYOU_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.SHURYOU_TIME, srTapingSagyoData))); //終了日時
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.MAINTENANCE_KAISU, srTapingSagyoData))); //ﾒﾝﾃﾅﾝｽ回数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.MAINTGO_TP_GAIKAN, srTapingSagyoData))); //ﾒﾝﾃ後TP外観
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BARASHI_TAISYO_REELSU, srTapingSagyoData))); //ﾊﾞﾗｼ対象ﾘｰﾙ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KARA_REELSU, srTapingSagyoData))); //空ﾘｰﾙ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHIN_REELSU, srTapingSagyoData))); //良品ﾘｰﾙ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU, srTapingSagyoData))); //QA確認依頼ﾘｰﾙ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.REELSU_CHECK, srTapingSagyoData))); //ﾘｰﾙ数ﾁｪｯｸ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_HOPPER_PART, srTapingSagyoData))); //TP後清掃：ﾎｯﾊﾟｰ部
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_FEEDER_PART, srTapingSagyoData))); //TP後清掃：ﾌｨｰﾀﾞ部
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_IN_INDEX, srTapingSagyoData))); //TP後清掃：INDEX内
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_IN_NGBOX, srTapingSagyoData))); //TP後清掃：NGBOX内
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SEISOU_TANTOUSYA, srTapingSagyoData))); //清掃担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SEISOU_KAKUNINSYA, srTapingSagyoData))); //清掃確認者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BARASHI_IRAI_REELSU, srTapingSagyoData))); //ﾊﾞﾗｼ依頼ﾘｰﾙ数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BARASHI_KAISHI_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.BARASHI_KAISHI_TIME, srTapingSagyoData))); //ﾊﾞﾗｼ開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BARASHI_SHURYOU_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.BARASHI_SHURYOU_TIME, srTapingSagyoData))); //ﾊﾞﾗｼ終了日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BARASHI_TANTOUSYA, srTapingSagyoData))); //ﾊﾞﾗｼ担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.DATSUJI_IRAI_REELSU, srTapingSagyoData))); //脱磁依頼ﾘｰﾙ数
        params.add(DBUtil.stringToDateObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.DATSUJI_KAISHI_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.DATSUJI_KAISHI_TIME, srTapingSagyoData))); //脱磁開始日時
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.DATSUJI_TANTOUSYA, srTapingSagyoData))); //脱磁担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KAKUHO_REELSU, srTapingSagyoData))); //確保ﾘｰﾙ数2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.KARA_REELSU2, srTapingSagyoData))); //空ﾘｰﾙ数2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.RYOHIN_REELSU2, srTapingSagyoData))); //良品ﾘｰﾙ数2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU2, srTapingSagyoData))); //QA確認依頼ﾘｰﾙ数2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.SAISYUKAKUNIN_TANTOUSYA, srTapingSagyoData))); //最終確認担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BIKOU1, srTapingSagyoData))); //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B048Const.BIKOU2, srTapingSagyoData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        if (isInsert) {
            params.add(bango); //番号
        }
        return params;
    }

    /**
     * テーピング作業(sr_taping_sagyo)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrTapingSagyo 仮登録データ
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void insertSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, int bango, Timestamp systemTime, List<FXHDD01> itemList, SrTapingSagyo tmpSrTapingSagyo, Map hiddenDataMap) throws SQLException {

        String sql = "INSERT INTO sr_taping_sagyo ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,kensabasyo,gouki,tpsiyou,"
                + "kakuhosu,kakuhoreelmaki1,kakuhoreelhonsu1,kakuhoreelmaki2,kakuhoreelhonsu2,tapelotno1,tapelotno2,tapelotno3,tapelotno4,tapelotno5,tapelotno6,"
                + "tapelotno7,tapelotno8,tapelotno9,tapelotno10,toptapelotno1,toptapelotno2,toptapelotno3,toptapelotno4,toptapelotno5,toptapelotno6,toptapelotno7,"
                + "toptapelotno8,toptapelotno9,toptapelotno10,bottomtapelot1,bottomtapelot2,bottomtapelot3,yoryohannihi,yoryohannilo,yoryohannitanni,rangehyoji,"
                + "rangehyojitanni,youryou,youryoutanni,dfsethi,dfsetlo,dfatai,gazousetteijyoken,hoperneji,koteseisou,kaisimaesehinzannasi,setsya,kakuninsya,"
                + "kaisinichiji,sikentantou,jijyurakkasiken,barasicheck,toptapekakunin,sounyuusu,tounyuusu,ryouhintopreelmaki1,ryouhintopreelhonsu1,"
                + "ryouhintopreelmaki2,ryouhintopreelhonsu2,ryouhinsu,youryong1,gazoungue2,gazoungsita2,budomari,shuryonichiji,mentekaisu,mentegotpgaikan,"
                + "barasitaisyoreelsu,akireelsu,ryouhinreelsu,qakakuniniraireelsu,reelsucheck,tpatohopper,tpatofeeder,tpatoindex,tpatongbox,seisoutantou,"
                + "seisoukakunin,barasiiraireelsu,barasikaisinichiji,barasiksyuryonichiji,barasitantou,datujiiraireelsu,datujikaisinichiji,datujitantou,"
                + "kakuhoreelsu,akireelsu2,ryouhinreelsu2,qakakuniniraireelsu2,saisyukakuninn,bikou1,bikou2,torokunichiji,kosinnichiji,revision,bango "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrTapingSagyo(true, newRev, kojyo, lotNo, edaban, jissekino, bango, systemTime, itemList, tmpSrTapingSagyo, hiddenDataMap);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピング作業(sr_taping_sagyo)更新処理
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
     * @param hiddenDataMap 保持データ
     * @throws SQLException 例外エラー
     */
    private void updateSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino,int bango, Timestamp systemTime, List<FXHDD01> itemList, Map hiddenDataMap) throws SQLException {
        String sql = "UPDATE sr_taping_sagyo SET "
                + "kcpno = ?,tokuisaki = ?,lotkubuncode = ?,ownercode = ?,okuriryouhinsuu = ?,ukeiretannijyuryo = ?,ukeiresoujyuryou = ?,kensabasyo = ?,gouki = ?,"
                + "tpsiyou = ?,kakuhosu = ?,kakuhoreelmaki1 = ?,kakuhoreelhonsu1 = ?,kakuhoreelmaki2 = ?,kakuhoreelhonsu2 = ?,tapelotno1 = ?,tapelotno2 = ?,tapelotno3 = ?,"
                + "tapelotno4 = ?,tapelotno5 = ?,tapelotno6 = ?,tapelotno7 = ?,tapelotno8 = ?,tapelotno9 = ?,tapelotno10 = ?,toptapelotno1 = ?,toptapelotno2 = ?,toptapelotno3 = ?,"
                + "toptapelotno4 = ?,toptapelotno5 = ?,toptapelotno6 = ?,toptapelotno7 = ?,toptapelotno8 = ?,toptapelotno9 = ?,toptapelotno10 = ?,bottomtapelot1 = ?,"
                + "bottomtapelot2 = ?,bottomtapelot3 = ?,yoryohannihi = ?,yoryohannilo = ?,yoryohannitanni = ?,rangehyoji = ?,rangehyojitanni = ?,youryou = ?,youryoutanni = ?,"
                + "dfsethi = ?,dfsetlo = ?,dfatai = ?,gazousetteijyoken = ?,hoperneji = ?,koteseisou = ?,kaisimaesehinzannasi = ?,setsya = ?,kakuninsya = ?,kaisinichiji = ?,"
                + "sikentantou = ?,jijyurakkasiken = ?,barasicheck = ?,toptapekakunin = ?,sounyuusu = ?,tounyuusu = ?,ryouhintopreelmaki1 = ?,ryouhintopreelhonsu1 = ?,"
                + "ryouhintopreelmaki2 = ?,ryouhintopreelhonsu2 = ?,ryouhinsu = ?,youryong1 = ?,gazoungue2 = ?,gazoungsita2 = ?,budomari = ?,shuryonichiji = ?,mentekaisu = ?,"
                + "mentegotpgaikan = ?,barasitaisyoreelsu = ?,akireelsu = ?,ryouhinreelsu = ?,qakakuniniraireelsu = ?,reelsucheck = ?,tpatohopper = ?,tpatofeeder = ?,tpatoindex = ?,"
                + "tpatongbox = ?,seisoutantou = ?,seisoukakunin = ?,barasiiraireelsu = ?,barasikaisinichiji = ?,barasiksyuryonichiji = ?,barasitantou = ?,datujiiraireelsu = ?,"
                + "datujikaisinichiji = ?,datujitantou = ?,kakuhoreelsu = ?,akireelsu2 = ?,ryouhinreelsu2 = ?,qakakuniniraireelsu2 = ?,saisyukakuninn = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? AND bango = ?";

        // 更新前の値を取得
        List<SrTapingSagyo> srTapingSagyoList = getSrTapingSagyoData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino, bango);
        SrTapingSagyo srTapingSagyo = null;
        if (!srTapingSagyoList.isEmpty()) {
            srTapingSagyo = srTapingSagyoList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrTapingSagyo(false, newRev, "", "", "", jissekino, bango, systemTime, itemList, srTapingSagyo, hiddenDataMap);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        params.add(bango);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * テーピング作業(sr_taping_sagyo)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srTapingSagyoData テーピング作業データ
     * @param hiddenDataMap 保持データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrTapingSagyo(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            int jissekino,int bango, Timestamp systemTime, List<FXHDD01> itemList, SrTapingSagyo srTapingSagyoData, Map hiddenDataMap) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //検査回数
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.KCPNO, srTapingSagyoData))); //KCPNO
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOKUISAKI, srTapingSagyoData))); //客先
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(hiddenDataMap.get("lotkubuncode")))); // ﾛｯﾄ区分
        params.add(DBUtil.stringToStringObject(StringUtil.nullToBlank(hiddenDataMap.get("ownercode")))); //ｵｰﾅｰ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.OKURI_RYOHINSU, srTapingSagyoData))); //送り良品数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.UKEIRE_TANNIJURYO, srTapingSagyoData))); //受入れ単位重量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.UKEIRE_SOUJURYO, srTapingSagyoData))); //受入れ総重量
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.KENSA_BASHO, srTapingSagyoData))); //検査場所
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.GOKI, srTapingSagyoData))); //号機
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TP_SHIYO, srTapingSagyoData))); //TP仕様
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KAKUHOSU, srTapingSagyoData))); //確保数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_MAKISU1, srTapingSagyoData))); //確保ﾘｰﾙ巻数①
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_HONSU1, srTapingSagyoData))); //確保ﾘｰﾙ本数①
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_MAKISU2, srTapingSagyoData))); //確保ﾘｰﾙ巻数②
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KAKUHO_REEL_HONSU2, srTapingSagyoData))); //確保ﾘｰﾙ本数②
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO1, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.①
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO2, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.②
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO3, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.③
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO4, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.④
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO5, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO6, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO7, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO8, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO9, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.CARRIERTAPE_LOTNO10, srTapingSagyoData))); //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO1, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO2, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO3, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO4, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO5, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO6, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO7, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO8, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO9, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_LOTNO10, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BOTTOMTAPE_LOTNO1, srTapingSagyoData))); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BOTTOMTAPE_LOTNO2, srTapingSagyoData))); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BOTTOMTAPE_LOTNO3, srTapingSagyoData))); //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.YORYOHANNI_SETCHI_HI, srTapingSagyoData))); //容量範囲ｾｯﾄ値 Hi
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.YORYOHANNI_SETCHI_LO, srTapingSagyoData))); //容量範囲ｾｯﾄ値 Lo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.YORYOHANNI_SETCHI_TANI, srTapingSagyoData))); //容量範囲ｾｯﾄ値単位
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RANGE_HYOJI, srTapingSagyoData))); //ﾚﾝｼﾞ表示
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.RANGE_HYOJI_TANI, srTapingSagyoData))); //ﾚﾝｼﾞ表示単位
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.YORYOCHI, srTapingSagyoData))); //容量値
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.YORYOCHI_TANI, srTapingSagyoData))); //容量値単位
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.DF_SETCHI_HI, srTapingSagyoData))); //DFｾｯﾄ値 Hi
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.DF_SETCHI_LO, srTapingSagyoData))); //DFｾｯﾄ値 Lo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.DF_CHI, srTapingSagyoData))); //DF値
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.GAZO_SETTEIJOKEN, srTapingSagyoData))); //画像設定条件
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.HOPPERNEJI_KAKUNIN, srTapingSagyoData))); //ﾎｯﾊﾟｰﾈｼﾞ確認
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.KOTE_SEISOU, srTapingSagyoData))); //ｺﾃ清掃
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.KAISIMAESEHINZANNASI, srTapingSagyoData))); //開始前に製品残なき事
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.SETSYA, srTapingSagyoData))); //SET者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.W_CHECKSYA, srTapingSagyoData))); //Wﾁｪｯｸ者
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B048Const.KAISHI_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.KAISHI_TIME, srTapingSagyoData))); //開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.SHIKEN_TANTOSYA, srTapingSagyoData))); //試験担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.JIJU_RAKKA, srTapingSagyoData))); //自重落下試験
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BARASHI_CHECK, srTapingSagyoData))); //ﾊﾞﾗｼﾁｪｯｸ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TOPTAPE_KAKUNIN, srTapingSagyoData))); //ﾄｯﾌﾟﾃｰﾌﾟ確認
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.SONYUSU, srTapingSagyoData))); //挿入数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.TOUNYUSU, srTapingSagyoData))); //投入数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1, srTapingSagyoData))); //良品TPﾘｰﾙ巻数①
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1, srTapingSagyoData))); //良品TPﾘｰﾙ本数①
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2, srTapingSagyoData))); //良品TPﾘｰﾙ巻数②
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2, srTapingSagyoData))); //良品TPﾘｰﾙ本数②
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHINSU, srTapingSagyoData))); //良品数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.YORYO_NG_NG1, srTapingSagyoData))); //容量NG(NG1)
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.GAZO_NG_UE_GAZOSU_NG2, srTapingSagyoData))); //画像NG(上画像数):NG2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.GAZO_NG_SHITA_GAZOSU_NG2, srTapingSagyoData))); //画像NG(下画像数):NG2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B048Const.BUDOMARI, srTapingSagyoData))); //歩留まり
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B048Const.SHURYOU_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.SHURYOU_TIME, srTapingSagyoData))); //終了日時
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.MAINTENANCE_KAISU, srTapingSagyoData))); //ﾒﾝﾃﾅﾝｽ回数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.MAINTGO_TP_GAIKAN, srTapingSagyoData))); //ﾒﾝﾃ後TP外観
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.BARASHI_TAISYO_REELSU, srTapingSagyoData))); //ﾊﾞﾗｼ対象ﾘｰﾙ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KARA_REELSU, srTapingSagyoData))); //空ﾘｰﾙ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHIN_REELSU, srTapingSagyoData))); //良品ﾘｰﾙ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU, srTapingSagyoData))); //QA確認依頼ﾘｰﾙ数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.REELSU_CHECK, srTapingSagyoData))); //ﾘｰﾙ数ﾁｪｯｸ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_HOPPER_PART, srTapingSagyoData))); //TP後清掃：ﾎｯﾊﾟｰ部
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_FEEDER_PART, srTapingSagyoData))); //TP後清掃：ﾌｨｰﾀﾞ部
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_IN_INDEX, srTapingSagyoData))); //TP後清掃：INDEX内
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.TPATO_SEISOU_IN_NGBOX, srTapingSagyoData))); //TP後清掃：NGBOX内
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.SEISOU_TANTOUSYA, srTapingSagyoData))); //清掃担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.SEISOU_KAKUNINSYA, srTapingSagyoData))); //清掃確認者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.BARASHI_IRAI_REELSU, srTapingSagyoData))); //ﾊﾞﾗｼ依頼ﾘｰﾙ数
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B048Const.BARASHI_KAISHI_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.BARASHI_KAISHI_TIME, srTapingSagyoData))); //ﾊﾞﾗｼ開始日時
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B048Const.BARASHI_SHURYOU_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.BARASHI_SHURYOU_TIME, srTapingSagyoData))); //ﾊﾞﾗｼ終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BARASHI_TANTOUSYA, srTapingSagyoData))); //ﾊﾞﾗｼ担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.DATSUJI_IRAI_REELSU, srTapingSagyoData))); //脱磁依頼ﾘｰﾙ数
        params.add(DBUtil.stringToDateObject(getItemData(itemList, GXHDO101B048Const.DATSUJI_KAISHI_DAY, srTapingSagyoData),
                getItemData(itemList, GXHDO101B048Const.DATSUJI_KAISHI_TIME, srTapingSagyoData))); //脱磁開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.DATSUJI_TANTOUSYA, srTapingSagyoData))); //脱磁担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KAKUHO_REELSU, srTapingSagyoData))); //確保ﾘｰﾙ数2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.KARA_REELSU2, srTapingSagyoData))); //空ﾘｰﾙ数2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.RYOHIN_REELSU2, srTapingSagyoData))); //良品ﾘｰﾙ数2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU2, srTapingSagyoData))); //QA確認依頼ﾘｰﾙ数2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.SAISYUKAKUNIN_TANTOUSYA, srTapingSagyoData))); //最終確認担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BIKOU1, srTapingSagyoData))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B048Const.BIKOU2, srTapingSagyoData))); //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        params.add(newRev); //revision
        if (isInsert) {
            params.add(bango); //番号
        }
        return params;
    }

    /**
     * テーピング作業(sr_taping_sagyo)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @throws SQLException 例外エラー
     */
    private void deleteSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, int bango) throws SQLException {

        String sql = "DELETE FROM sr_taping_sagyo "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND revision = ? AND bango = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        params.add(bango);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [テーピング作業_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino, int bango) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_taping_sagyo "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND bango = ?";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(bango);

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
     * @param fxhdd12RevInfo ﾃｰﾋﾟﾝｸﾞ号機選択データ
     * @return エラーメッセージ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd12RevInfo) throws SQLException {

        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、データが存在する場合
            if (fxhdd12RevInfo != null && !fxhdd12RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000026"));
            }
        } else {
            // ﾃｰﾋﾟﾝｸﾞ号機選択データが取得出来ていない場合エラー
            if (fxhdd12RevInfo == null || fxhdd12RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd12RevInfo, "rev")))) {
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
        if (itemDay != null) {
            itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        }

        if (itemTime != null) {
            itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
        }
    }

    /**
     * 項目IDに該当するDBの値を取得する。
     *
     * @param itemId 項目ID
     * @param srTapingSagyoData テーピング作業データ
     * @return DB値
     */
    private String getSrTapingSagyoItemData(String itemId, SrTapingSagyo srTapingSagyoData) {
        switch (itemId) {
            //KCPNO
            case GXHDO101B048Const.KCPNO:
                return StringUtil.nullToBlank(srTapingSagyoData.getKcpno());
            //客先
            case GXHDO101B048Const.TOKUISAKI:
                return StringUtil.nullToBlank(srTapingSagyoData.getTokuisaki());
            //ﾛｯﾄ区分
            case GXHDO101B048Const.LOT_KUBUN:
                return StringUtil.nullToBlank(srTapingSagyoData.getLotkubuncode());
            //ｵｰﾅｰ
            case GXHDO101B048Const.OWNER:
                return StringUtil.nullToBlank(srTapingSagyoData.getOwnercode());
            //送り良品数
            case GXHDO101B048Const.OKURI_RYOHINSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getOkuriryouhinsuu());
            //受入れ単位重量
            case GXHDO101B048Const.UKEIRE_TANNIJURYO:
                return StringUtil.nullToBlank(srTapingSagyoData.getUkeiretannijyuryo());
            //受入れ総重量
            case GXHDO101B048Const.UKEIRE_SOUJURYO:
                return StringUtil.nullToBlank(srTapingSagyoData.getUkeiresoujyuryou());
            //検査回数
            case GXHDO101B048Const.KENSA_KAISUU:
                return StringUtil.nullToBlank(srTapingSagyoData.getKaisuu());
            //検査場所
            case GXHDO101B048Const.KENSA_BASHO:
                return StringUtil.nullToBlank(srTapingSagyoData.getKensabasyo());
            //号機
            case GXHDO101B048Const.GOKI:
                return StringUtil.nullToBlank(srTapingSagyoData.getGouki());
            //TP仕様
            case GXHDO101B048Const.TP_SHIYO:
                return StringUtil.nullToBlank(srTapingSagyoData.getTpsiyou());
            //確保数
            case GXHDO101B048Const.KAKUHOSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuhosu());
            //確保ﾘｰﾙ巻数①
            case GXHDO101B048Const.KAKUHO_REEL_MAKISU1:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuhoreelmaki1());
            //確保ﾘｰﾙ本数①
            case GXHDO101B048Const.KAKUHO_REEL_HONSU1:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuhoreelhonsu1());
            //確保ﾘｰﾙ巻数②
            case GXHDO101B048Const.KAKUHO_REEL_MAKISU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuhoreelmaki2());
            //確保ﾘｰﾙ本数②
            case GXHDO101B048Const.KAKUHO_REEL_HONSU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuhoreelhonsu2());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.①
            case GXHDO101B048Const.CARRIERTAPE_LOTNO1:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno1());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.②
            case GXHDO101B048Const.CARRIERTAPE_LOTNO2:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno2());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.③
            case GXHDO101B048Const.CARRIERTAPE_LOTNO3:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno3());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.④
            case GXHDO101B048Const.CARRIERTAPE_LOTNO4:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno4());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑤
            case GXHDO101B048Const.CARRIERTAPE_LOTNO5:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno5());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑥
            case GXHDO101B048Const.CARRIERTAPE_LOTNO6:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno6());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑦
            case GXHDO101B048Const.CARRIERTAPE_LOTNO7:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno7());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑧
            case GXHDO101B048Const.CARRIERTAPE_LOTNO8:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno8());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑨
            case GXHDO101B048Const.CARRIERTAPE_LOTNO9:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno9());
            //ｷｬﾘｱﾃｰﾌﾟLOT NO.⑩
            case GXHDO101B048Const.CARRIERTAPE_LOTNO10:
                return StringUtil.nullToBlank(srTapingSagyoData.getTapelotno10());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.①
            case GXHDO101B048Const.TOPTAPE_LOTNO1:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno1());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.②
            case GXHDO101B048Const.TOPTAPE_LOTNO2:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno2());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.③
            case GXHDO101B048Const.TOPTAPE_LOTNO3:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno3());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.④
            case GXHDO101B048Const.TOPTAPE_LOTNO4:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno4());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑤
            case GXHDO101B048Const.TOPTAPE_LOTNO5:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno5());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑥
            case GXHDO101B048Const.TOPTAPE_LOTNO6:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno6());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑦
            case GXHDO101B048Const.TOPTAPE_LOTNO7:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno7());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑧
            case GXHDO101B048Const.TOPTAPE_LOTNO8:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno8());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑨
            case GXHDO101B048Const.TOPTAPE_LOTNO9:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno9());
            //ﾄｯﾌﾟﾃｰﾌﾟLOT NO.⑩
            case GXHDO101B048Const.TOPTAPE_LOTNO10:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapelotno10());
            //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.①
            case GXHDO101B048Const.BOTTOMTAPE_LOTNO1:
                return StringUtil.nullToBlank(srTapingSagyoData.getBottomtapelot1());
            //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.②
            case GXHDO101B048Const.BOTTOMTAPE_LOTNO2:
                return StringUtil.nullToBlank(srTapingSagyoData.getBottomtapelot2());
            //ﾎﾞﾄﾑﾃｰﾌﾟLOT NO.③
            case GXHDO101B048Const.BOTTOMTAPE_LOTNO3:
                return StringUtil.nullToBlank(srTapingSagyoData.getBottomtapelot3());
            //容量範囲ｾｯﾄ値 Hi
            case GXHDO101B048Const.YORYOHANNI_SETCHI_HI:
                return StringUtil.nullToBlank(srTapingSagyoData.getYoryohannihi());
            //容量範囲ｾｯﾄ値 Lo
            case GXHDO101B048Const.YORYOHANNI_SETCHI_LO:
                return StringUtil.nullToBlank(srTapingSagyoData.getYoryohannilo());
            //容量範囲ｾｯﾄ値単位
            case GXHDO101B048Const.YORYOHANNI_SETCHI_TANI:
                return StringUtil.nullToBlank(srTapingSagyoData.getYoryohannitanni());
            //ﾚﾝｼﾞ表示
            case GXHDO101B048Const.RANGE_HYOJI:
                return StringUtil.nullToBlank(srTapingSagyoData.getRangehyoji());
            //ﾚﾝｼﾞ表示単位
            case GXHDO101B048Const.RANGE_HYOJI_TANI:
                return StringUtil.nullToBlank(srTapingSagyoData.getRangehyojitanni());
            //容量値
            case GXHDO101B048Const.YORYOCHI:
                return StringUtil.nullToBlank(srTapingSagyoData.getYouryou());
            //容量値単位
            case GXHDO101B048Const.YORYOCHI_TANI:
                return StringUtil.nullToBlank(srTapingSagyoData.getYouryoutanni());
            //DFｾｯﾄ値 Hi
            case GXHDO101B048Const.DF_SETCHI_HI:
                return StringUtil.nullToBlank(srTapingSagyoData.getDfsethi());
            //DFｾｯﾄ値 Lo
            case GXHDO101B048Const.DF_SETCHI_LO:
                return StringUtil.nullToBlank(srTapingSagyoData.getDfsetlo());
            //DF値
            case GXHDO101B048Const.DF_CHI:
                return StringUtil.nullToBlank(srTapingSagyoData.getDfatai());
            //画像設定条件
            case GXHDO101B048Const.GAZO_SETTEIJOKEN:
                return StringUtil.nullToBlank(srTapingSagyoData.getGazousetteijyoken());
            //ﾎｯﾊﾟｰﾈｼﾞ確認
            case GXHDO101B048Const.HOPPERNEJI_KAKUNIN:
                return StringUtil.nullToBlank(srTapingSagyoData.getHoperneji());
            //ｺﾃ清掃
            case GXHDO101B048Const.KOTE_SEISOU:
                return StringUtil.nullToBlank(srTapingSagyoData.getKoteseisou());
            //開始前に製品残なき事
            case GXHDO101B048Const.KAISIMAESEHINZANNASI:
                return StringUtil.nullToBlank(srTapingSagyoData.getKaisimaesehinzannasi());
            //SET者
            case GXHDO101B048Const.SETSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getSetsya());
            //Wﾁｪｯｸ者
            case GXHDO101B048Const.W_CHECKSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuninsya());
            //開始日
            case GXHDO101B048Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getKaisinichiji(), "yyMMdd");
            //開始時間
            case GXHDO101B048Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getKaisinichiji(), "HHmm");
            //試験担当者
            case GXHDO101B048Const.SHIKEN_TANTOSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getSikentantou());
            //自重落下試験
            case GXHDO101B048Const.JIJU_RAKKA:
                return StringUtil.nullToBlank(srTapingSagyoData.getJijyurakkasiken());
            //ﾊﾞﾗｼﾁｪｯｸ
            case GXHDO101B048Const.BARASHI_CHECK:
                return StringUtil.nullToBlank(srTapingSagyoData.getBarasicheck());
            //ﾄｯﾌﾟﾃｰﾌﾟ確認
            case GXHDO101B048Const.TOPTAPE_KAKUNIN:
                return StringUtil.nullToBlank(srTapingSagyoData.getToptapekakunin());
            //挿入数
            case GXHDO101B048Const.SONYUSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getSounyuusu());
            //投入数
            case GXHDO101B048Const.TOUNYUSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getTounyuusu());
            //良品TPﾘｰﾙ巻数①
            case GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhintopreelmaki1());
            //良品TPﾘｰﾙ本数①
            case GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhintopreelhonsu1());
            //良品TPﾘｰﾙ巻数②
            case GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhintopreelmaki2());
            //良品TPﾘｰﾙ本数②
            case GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhintopreelhonsu2());
            //良品数
            case GXHDO101B048Const.RYOHINSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhinsu());
            //容量NG(NG1)
            case GXHDO101B048Const.YORYO_NG_NG1:
                return StringUtil.nullToBlank(srTapingSagyoData.getYouryong1());
            //画像NG(上画像数):NG2
            case GXHDO101B048Const.GAZO_NG_UE_GAZOSU_NG2:
                return StringUtil.nullToBlank(srTapingSagyoData.getGazoungue2());
            //画像NG(下画像数):NG2
            case GXHDO101B048Const.GAZO_NG_SHITA_GAZOSU_NG2:
                return StringUtil.nullToBlank(srTapingSagyoData.getGazoungsita2());
            //歩留まり
            case GXHDO101B048Const.BUDOMARI:
                return StringUtil.nullToBlank(srTapingSagyoData.getBudomari());
            //終了日
            case GXHDO101B048Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getShuryonichiji(), "yyMMdd");
            //終了時間
            case GXHDO101B048Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getShuryonichiji(), "HHmm");
            //ﾒﾝﾃﾅﾝｽ回数
            case GXHDO101B048Const.MAINTENANCE_KAISU:
                return StringUtil.nullToBlank(srTapingSagyoData.getMentekaisu());
            //ﾒﾝﾃ後TP外観
            case GXHDO101B048Const.MAINTGO_TP_GAIKAN:
                return StringUtil.nullToBlank(srTapingSagyoData.getMentegotpgaikan());
            //ﾊﾞﾗｼ対象ﾘｰﾙ数
            case GXHDO101B048Const.BARASHI_TAISYO_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getBarasitaisyoreelsu());
            //空ﾘｰﾙ数
            case GXHDO101B048Const.KARA_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getAkireelsu());
            //良品ﾘｰﾙ数
            case GXHDO101B048Const.RYOHIN_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhinreelsu());
            //QA確認依頼ﾘｰﾙ数
            case GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getQakakuniniraireelsu());
            //ﾘｰﾙ数ﾁｪｯｸ
            case GXHDO101B048Const.REELSU_CHECK:
                return StringUtil.nullToBlank(srTapingSagyoData.getReelsucheck());
            //TP後清掃：ﾎｯﾊﾟｰ部
            case GXHDO101B048Const.TPATO_SEISOU_HOPPER_PART:
                return StringUtil.nullToBlank(srTapingSagyoData.getTpatohopper());
            //TP後清掃：ﾌｨｰﾀﾞ部
            case GXHDO101B048Const.TPATO_SEISOU_FEEDER_PART:
                return StringUtil.nullToBlank(srTapingSagyoData.getTpatofeeder());
            //TP後清掃：INDEX内
            case GXHDO101B048Const.TPATO_SEISOU_IN_INDEX:
                return StringUtil.nullToBlank(srTapingSagyoData.getTpatoindex());
            //TP後清掃：NGBOX内
            case GXHDO101B048Const.TPATO_SEISOU_IN_NGBOX:
                return StringUtil.nullToBlank(srTapingSagyoData.getTpatongbox());
            //清掃担当者
            case GXHDO101B048Const.SEISOU_TANTOUSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getSeisoutantou());
            //清掃確認者
            case GXHDO101B048Const.SEISOU_KAKUNINSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getSeisoukakunin());
            //ﾊﾞﾗｼ依頼ﾘｰﾙ数
            case GXHDO101B048Const.BARASHI_IRAI_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getBarasiiraireelsu());
            //ﾊﾞﾗｼ開始日
            case GXHDO101B048Const.BARASHI_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getBarasikaisinichiji(), "yyMMdd");
            //ﾊﾞﾗｼ開始時間
            case GXHDO101B048Const.BARASHI_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getBarasikaisinichiji(), "HHmm");
            //ﾊﾞﾗｼ終了日
            case GXHDO101B048Const.BARASHI_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getBarasiksyuryonichiji(), "yyMMdd");
            //ﾊﾞﾗｼ終了時間
            case GXHDO101B048Const.BARASHI_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getBarasiksyuryonichiji(), "HHmm");
            //ﾊﾞﾗｼ担当者
            case GXHDO101B048Const.BARASHI_TANTOUSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getBarasitantou());
            //脱磁依頼ﾘｰﾙ数
            case GXHDO101B048Const.DATSUJI_IRAI_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getDatujiiraireelsu());
            //脱磁開始日
            case GXHDO101B048Const.DATSUJI_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getDatujikaisinichiji(), "yyMMdd");
            //脱磁開始時間
            case GXHDO101B048Const.DATSUJI_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srTapingSagyoData.getDatujikaisinichiji(), "HHmm");
            //脱磁担当者
            case GXHDO101B048Const.DATSUJI_TANTOUSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getDatujitantou());
            //確保ﾘｰﾙ数
            case GXHDO101B048Const.KAKUHO_REELSU:
                return StringUtil.nullToBlank(srTapingSagyoData.getKakuhoreelsu());
            //空ﾘｰﾙ数2
            case GXHDO101B048Const.KARA_REELSU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getAkireelsu2());
            //良品ﾘｰﾙ数2
            case GXHDO101B048Const.RYOHIN_REELSU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getRyouhinreelsu2());
            //QA確認依頼ﾘｰﾙ数2
            case GXHDO101B048Const.QAKAKUNIN_IRAI_REELSU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getQakakuniniraireelsu2());
            //最終確認担当者
            case GXHDO101B048Const.SAISYUKAKUNIN_TANTOUSYA:
                return StringUtil.nullToBlank(srTapingSagyoData.getSaisyukakuninn());
            //備考1
            case GXHDO101B048Const.BIKOU1:
                return StringUtil.nullToBlank(srTapingSagyoData.getBikou1());
            //備考2
            case GXHDO101B048Const.BIKOU2:
                return StringUtil.nullToBlank(srTapingSagyoData.getBikou2());

            default:
                return null;

        }
    }

    /**
     * テーピング作業_仮登録(tmp_sr_taping_sagyo)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrTapingSagyo(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, int bango, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_taping_sagyo ("
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,kensabasyo,gouki,tpsiyou,"
                + "kakuhosu,kakuhoreelmaki1,kakuhoreelhonsu1,kakuhoreelmaki2,kakuhoreelhonsu2,tapelotno1,tapelotno2,tapelotno3,tapelotno4,tapelotno5,tapelotno6,"
                + "tapelotno7,tapelotno8,tapelotno9,tapelotno10,toptapelotno1,toptapelotno2,toptapelotno3,toptapelotno4,toptapelotno5,toptapelotno6,toptapelotno7,"
                + "toptapelotno8,toptapelotno9,toptapelotno10,bottomtapelot1,bottomtapelot2,bottomtapelot3,yoryohannihi,yoryohannilo,yoryohannitanni,rangehyoji,"
                + "rangehyojitanni,youryou,youryoutanni,dfsethi,dfsetlo,dfatai,gazousetteijyoken,hoperneji,koteseisou,kaisimaesehinzannasi,setsya,kakuninsya,"
                + "kaisinichiji,sikentantou,jijyurakkasiken,barasicheck,toptapekakunin,sounyuusu,tounyuusu,ryouhintopreelmaki1,ryouhintopreelhonsu1,"
                + "ryouhintopreelmaki2,ryouhintopreelhonsu2,ryouhinsu,youryong1,gazoungue2,gazoungsita2,budomari,shuryonichiji,mentekaisu,mentegotpgaikan,"
                + "barasitaisyoreelsu,akireelsu,ryouhinreelsu,qakakuniniraireelsu,reelsucheck,tpatohopper,tpatofeeder,tpatoindex,tpatongbox,seisoutantou,"
                + "seisoukakunin,barasiiraireelsu,barasikaisinichiji,barasiksyuryonichiji,barasitantou,datujiiraireelsu,datujikaisinichiji,datujitantou,"
                + "kakuhoreelsu,akireelsu2,ryouhinreelsu2,qakakuniniraireelsu2,saisyukakuninn,bikou1,bikou2,torokunichiji,kosinnichiji,revision,deleteflag,bango "
                + ") SELECT "
                + "kojyo,lotno,edaban,kaisuu,kcpno,tokuisaki,lotkubuncode,ownercode,okuriryouhinsuu,ukeiretannijyuryo,ukeiresoujyuryou,kensabasyo,gouki,tpsiyou,"
                + "kakuhosu,kakuhoreelmaki1,kakuhoreelhonsu1,kakuhoreelmaki2,kakuhoreelhonsu2,tapelotno1,tapelotno2,tapelotno3,tapelotno4,tapelotno5,tapelotno6,"
                + "tapelotno7,tapelotno8,tapelotno9,tapelotno10,toptapelotno1,toptapelotno2,toptapelotno3,toptapelotno4,toptapelotno5,toptapelotno6,toptapelotno7,"
                + "toptapelotno8,toptapelotno9,toptapelotno10,bottomtapelot1,bottomtapelot2,bottomtapelot3,yoryohannihi,yoryohannilo,yoryohannitanni,rangehyoji,"
                + "rangehyojitanni,youryou,youryoutanni,dfsethi,dfsetlo,dfatai,gazousetteijyoken,hoperneji,koteseisou,kaisimaesehinzannasi,setsya,kakuninsya,"
                + "kaisinichiji,sikentantou,jijyurakkasiken,barasicheck,toptapekakunin,sounyuusu,tounyuusu,ryouhintopreelmaki1,ryouhintopreelhonsu1,"
                + "ryouhintopreelmaki2,ryouhintopreelhonsu2,ryouhinsu,youryong1,gazoungue2,gazoungsita2,budomari,shuryonichiji,mentekaisu,mentegotpgaikan,"
                + "barasitaisyoreelsu,akireelsu,ryouhinreelsu,qakakuniniraireelsu,reelsucheck,tpatohopper,tpatofeeder,tpatoindex,tpatongbox,seisoutantou,"
                + "seisoukakunin,barasiiraireelsu,barasikaisinichiji,barasiksyuryonichiji,barasitantou,datujiiraireelsu,datujikaisinichiji,datujitantou,"
                + "kakuhoreelsu,akireelsu2,ryouhinreelsu2,qakakuniniraireelsu2,saisyukakuninn,bikou1,bikou2,?,?,?,?,bango "
                + "FROM sr_taping_sagyo "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND kaisuu = ? AND bango = ? ";

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
        params.add(jissekino); //実績No
        params.add(bango); //実績No

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * コンボボックスNG選択チェック
     *
     * @param itemData 項目データデータ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkComboBoxSelectNG(FXHDD01 itemData) {
        if (itemData == null) {
            return null;
        }

        if ("NG".equals(itemData.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemData);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemData.getLabel1());
        }
        return null;
    }

    /**
     * 確保数計算(チェック処理)
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo calcKakuhosuCheck(ProcessData processData) {

        FXHDD01 itemMakisu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_MAKISU1); //確保ﾘｰﾙ巻数①
        FXHDD01 itemHonsu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_HONSU1); //確保ﾘｰﾙ本数①
        FXHDD01 itemMakisu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_MAKISU2); //確保ﾘｰﾙ巻数②
        FXHDD01 itemHonsu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_HONSU2); //確保ﾘｰﾙ本数②

        ErrorMessageInfo errorMessageInfo;
        errorMessageInfo = checkNumberItem(itemMakisu1, true, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemHonsu1, true, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemMakisu2, false, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemHonsu2, false, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        return null;

    }

    /**
     * 確保数計算
     *
     * @param processData 処理制御データ
     */
    private void calcKakuhosu(ProcessData processData) {
        try {

            FXHDD01 itemMakisu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_MAKISU1); //確保ﾘｰﾙ巻数①
            FXHDD01 itemHonsu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_HONSU1); //確保ﾘｰﾙ本数①
            FXHDD01 itemMakisu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_MAKISU2); //確保ﾘｰﾙ巻数②
            FXHDD01 itemHonsu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.KAKUHO_REEL_HONSU2); //確保ﾘｰﾙ本数②

            // 計算用に変換
            BigDecimal makisu1 = new BigDecimal(itemMakisu1.getValue());
            BigDecimal honsu1 = new BigDecimal(itemHonsu1.getValue());
            BigDecimal makisu2 = new BigDecimal(StringUtil.emptyToZero(itemMakisu2.getValue()));
            BigDecimal honsu2 = new BigDecimal(StringUtil.emptyToZero(itemHonsu2.getValue()));

            //「確保ﾘｰﾙ巻数①」 × 「確保ﾘｰﾙ本数①」 を算出する。
            BigDecimal kakuhosu1 = makisu1.multiply(honsu1);
            //「確保ﾘｰﾙ巻数②」 × 「確保ﾘｰﾙ本数②」 を算出する。
            BigDecimal kakuhosu2 = makisu2.multiply(honsu2);
            // 計算結果を加算する。
            BigDecimal kakuhosu = kakuhosu1.add(kakuhosu2);

            // 確保数の項目に値をセット
            setItemData(processData, GXHDO101B048Const.KAKUHOSU, kakuhosu.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }

    }

    /**
     * 良品数計算(チェック処理)
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo calcRyohinsuCheck(ProcessData processData) {

        FXHDD01 itemMakisu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1); //良品TPﾘｰﾙ巻数①
        FXHDD01 itemHonsu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1); //良品TPﾘｰﾙ本数①
        FXHDD01 itemMakisu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2); //良品TPﾘｰﾙ巻数②
        FXHDD01 itemHonsu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2); //良品TPﾘｰﾙ本数②

        ErrorMessageInfo errorMessageInfo;
        errorMessageInfo = checkNumberItem(itemMakisu1, true, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemHonsu1, true, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemMakisu2, false, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemHonsu2, false, false, "XHD-000178", "XHD-000037");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        return null;

    }

    /**
     * 良品数計算
     *
     * @param processData 処理制御データ
     */
    private void calcRyohinsu(ProcessData processData) {
        try {

            FXHDD01 itemMakisu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU1); //良品TPﾘｰﾙ巻数①
            FXHDD01 itemHonsu1 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_HONSU1); //良品TPﾘｰﾙ本数①
            FXHDD01 itemMakisu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_MAKISU2); //良品TPﾘｰﾙ巻数②
            FXHDD01 itemHonsu2 = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHIN_TP_REEL_HONSU2); //良品TPﾘｰﾙ本数②

            // 計算用に変換
            BigDecimal makisu1 = new BigDecimal(itemMakisu1.getValue());
            BigDecimal honsu1 = new BigDecimal(itemHonsu1.getValue());
            BigDecimal makisu2 = new BigDecimal(StringUtil.emptyToZero(itemMakisu2.getValue()));
            BigDecimal honsu2 = new BigDecimal(StringUtil.emptyToZero(itemHonsu2.getValue()));

            //「良品TPﾘｰﾙ巻数①」 × 1000 ×「良品TPﾘｰﾙ本数①」 を算出する。
            BigDecimal ryohinsu1 = makisu1.multiply(BigDecimal.valueOf(1000)).multiply(honsu1);
            //「良品TPﾘｰﾙ巻数②」 × 1000 ×「良品TPﾘｰﾙ本数②」 を算出する。
            BigDecimal ryohinsu2 = makisu2.multiply(BigDecimal.valueOf(1000)).multiply(honsu2);
            // 計算結果を加算する。
            BigDecimal ryohinsu = ryohinsu1.add(ryohinsu2);

            // 良品数の項目に値をセット
            setItemData(processData, GXHDO101B048Const.RYOHINSU, ryohinsu.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }
    }

    /**
     * 歩留まり計算(チェック処理)
     *
     * @param processData 処理制御データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo calcBudomariCheck(ProcessData processData) {

        FXHDD01 itemRyohinsu = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHINSU); //良品数
        FXHDD01 itemTounyusu = getItemRow(processData.getItemList(), GXHDO101B048Const.TOUNYUSU); //投入数

        ErrorMessageInfo errorMessageInfo;
        errorMessageInfo = checkNumberItem(itemRyohinsu, true, false, "XHD-000159", "XHD-000158");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        errorMessageInfo = checkNumberItem(itemTounyusu, true, true, "XHD-000181", "XHD-000181");
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        return null;

    }

    /**
     * 歩留まり計算
     *
     * @param processData 処理制御データ
     */
    private void calcBudomari(ProcessData processData) {
        try {

            FXHDD01 itemRyohinsu = getItemRow(processData.getItemList(), GXHDO101B048Const.RYOHINSU); //良品数
            FXHDD01 itemTounyusu = getItemRow(processData.getItemList(), GXHDO101B048Const.TOUNYUSU); //投入数

            // 計算用に変換
            BigDecimal ryohinsu = new BigDecimal(itemRyohinsu.getValue());
            BigDecimal tounyusu = new BigDecimal(itemTounyusu.getValue());

            //「良品数」 ÷ 「投入数」× 100※小数第3位を四捨五入
            BigDecimal budomari = ryohinsu.multiply(BigDecimal.valueOf(100)).divide(tounyusu, 2, RoundingMode.HALF_UP);

            // 歩留まりの項目に値をセット
            setItemData(processData, GXHDO101B048Const.BUDOMARI, budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            // 数値変換できない場合はリターン
        }
    }

    /**
     * 数値項目チェック処理
     *
     * @param item 項目
     * @param checkInput 入力チェック(する：true、しない:false)
     * @param checkZero ZERO値チェック(する：true、しない:false)
     * @return エラメッセージ情報(エラーが無い場合はNULL)
     */
    private ErrorMessageInfo checkNumberItem(FXHDD01 item, boolean checkInput, boolean checkZero, String checkNumberErrorId, String checkInputErrorId) {
        String strValue = StringUtil.nullToBlank(item.getValue());
        BigDecimal decValue;

        if (StringUtil.isEmpty(strValue)) {
            if (checkInput) {
                // 未入力かつ入力チェック対象の場合はエラー情報をリターン
                return MessageUtil.getErrorMessageInfo(checkInputErrorId, true, true, Arrays.asList(item), item.getLabel1());
            }

            // 未入力OKなのでNULLをリターン
            return null;
        }

        try {
            decValue = new BigDecimal(strValue);
        } catch (NumberFormatException ex) {
            // 数値変換できない場合エラー
            return MessageUtil.getErrorMessageInfo(checkNumberErrorId, true, true, Arrays.asList(item), item.getLabel1());
        }

        if (checkZero && 0 == BigDecimal.ZERO.compareTo(decValue)) {
            return MessageUtil.getErrorMessageInfo(checkInputErrorId, true, true, Arrays.asList(item), item.getLabel1());
        }

        return null;
    }

    /**
     * 受入れ総重量の計算前ﾁｪｯｸ
     *
     * @param itemUkeireSojuryo 受入れ総重量
     * @param itemUkeireTanijuryo 合計未処理:ﾘﾃｽﾄ個数
     * @param itemOkuriRyohinsu 送り良品数
     */
    private boolean checkUkeireSojuryo(FXHDD01 itemUkeireSojuryo, FXHDD01 itemOkuriRyohinsu, FXHDD01 itemUkeireTanijuryo) {
        try {
            // 項目が存在しない場合、リターン
            if (itemUkeireSojuryo == null || itemUkeireTanijuryo == null || itemOkuriRyohinsu == null) {
                return false;
            }

            BigDecimal taniJuryo = new BigDecimal(itemUkeireTanijuryo.getValue());
            BigDecimal okuriRyohinsu = new BigDecimal(itemOkuriRyohinsu.getValue());

            // 受入れ単位重量、送り良品数の値のいずれかが0以下の場合、リターン
            if (0 <= BigDecimal.ZERO.compareTo(taniJuryo) || 0 <= BigDecimal.ZERO.compareTo(okuriRyohinsu)) {
                return false;
            }

        } catch (NullPointerException | NumberFormatException ex) {
            return false;
        }
        return true;

    }

    /**
     * 受入れ総重量を計算してセットする。 ※事前にcheckUkeireSojuryoを呼び出してﾁｪｯｸ処理を行うこと
     *
     * @param itemUkeireSojuryo 受入れ総重量
     * @param itemUkeireTanijuryo 合計未処理:ﾘﾃｽﾄ個数
     * @param itemOkuriRyohinsu 送り良品数
     */
    private void calcUkeireSojuryo(FXHDD01 itemUkeireSojuryo, FXHDD01 itemOkuriRyohinsu, FXHDD01 itemUkeireTanijuryo) {
        try {
            
            BigDecimal taniJuryo = new BigDecimal(itemUkeireTanijuryo.getValue());
            BigDecimal okuriRyohinsu = new BigDecimal(itemOkuriRyohinsu.getValue());

            //「送り良品数」　÷　100　×　「受入れ単位重量」 → 式を変換して先に「受入れ単位重量」を乗算
            BigDecimal budomari = okuriRyohinsu.multiply(taniJuryo).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            //計算結果を未検査率にセット
            itemUkeireSojuryo.setValue(budomari.toPlainString());

        } catch (NullPointerException | NumberFormatException ex) {
            //処理なし
        }
    }

    /**
     * [ﾊﾟﾗﾒｰﾀﾏｽﾀ]から、データを取得
     *
     * @param queryRunnerDoc オブジェクト
     * @param userName ユーザー名
     * @param key Key
     * @return 取得データ
     */
    private String loadParamData(QueryRunner queryRunnerDoc, String userName, String key) {
        try {

            // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
            String sql = "SELECT data "
                    + " FROM fxhbm03 "
                    + " WHERE user_name = ? AND key = ? ";

            List<Object> params = new ArrayList<>();
            params.add(userName);
            params.add(key);
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            Map data = queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
            if (data != null && !data.isEmpty()) {
                return StringUtil.nullToBlank(data.get("data"));
            }

        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        return null;

    }

    /**
     * [実績]から、ﾃﾞｰﾀを取得
     *
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

        List<String> dataList = new ArrayList<>(Arrays.asList(data));

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
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
     * ﾃｰﾋﾟﾝｸﾞ号機選択(fxhdd12)登録処理
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
     * @param bango 番号
     * @param goki 号機ｺｰﾄﾞ
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外エラー
     */
    private void insertFxhdd12(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, int bango, String goki, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO fxhdd12 ( "
                + "torokusha,toroku_date,koshinsha,koshin_date,rev,kojyo,lotno,"
                + "edaban,jissekino,bango,goki,jotai_flg "
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> params = new ArrayList<>();
        params.add(tantoshaCd); //登録者
        params.add(systemTime); //登録日
        params.add(null); //更新者
        params.add(null); //更新日
        params.add(rev); //revision
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //実績No
        params.add(bango); //番号
        params.add(goki); //号機ｺｰﾄﾞ
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * ﾃｰﾋﾟﾝｸﾞ号機選択(fxhdd12)更新処理
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param tantoshaCd 担当者ｺｰﾄﾞ
     * @param formId 画面ID
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @param goki 号機ｺｰﾄﾞ
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd12(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, int bango, String goki, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd12 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, goki = ?, jotai_flg = ? "
                + "WHERE kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = ?  AND bango = ? ";

        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(tantoshaCd); //更新者
        params.add(systemTime); //更新日
        params.add(rev); //rev
        params.add(goki); //号機ｺｰﾄﾞ
        params.add(jotaiFlg); //状態ﾌﾗｸﾞ

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add(jissekino); //実績No
        params.add(bango); //番号

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerDoc.update(conDoc, sql, params.toArray());
    }

    /**
     * 号機情報取得処理
     *
     * @param processData
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param bango 番号
     * @retrun 号機
     * @throws SQLException 例外ｴﾗｰ
     */
    private String getGoki(ProcessData processData, QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino, int bango) throws SQLException {
            
        // 更新前の値を取得
        List<SrTapingSagyo> srTapingSagyoList = getSrTapingSagyoData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo, edaban, jissekino, bango);
        SrTapingSagyo srTapingSagyo = null;
        if (!srTapingSagyoList.isEmpty()) {
            srTapingSagyo = srTapingSagyoList.get(0);
        }
        
        return StringUtil.nullToBlank(getItemData(processData.getItemList(), GXHDO101B048Const.GOKI, srTapingSagyo));
    }
    
    /**
     * 仕掛データ検索
     *
     * @param queryRunnerWip QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadTpGokiShikakariData(QueryRunner queryRunnerWip, String lotNo, String kouteiCode) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);

        // 仕掛情報データの取得
        String sql = "SELECT kojyo, lotno, edaban"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND oyalotedaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(kouteiCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private List<Jisseki> loadTpJissekiData(QueryRunner queryRunnerWip, String lotNo, String[] data) throws SQLException {

        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        List<String> dataList = new ArrayList<>(Arrays.asList(data));

        // ﾊﾟﾗﾒｰﾀﾏｽﾀデータの取得
        String sql = "SELECT jissekino "
                + "FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND ";

        sql += DBUtil.getInConditionPreparedStatement("koteicode", dataList.size());

        sql += " ORDER BY syoribi DESC, syorijikoku DESC";

        Map mapping = new HashMap<>();
        mapping.put("jissekino", "jissekino");

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
     * [生産実績]から、ﾃﾞｰﾀを取得
     * @param queryRunnerWip オブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param date ﾊﾟﾗﾒｰﾀﾃﾞｰﾀ(検索キー)
     * @return 取得データ
     * @throws SQLException 
     */
     private List<Seisan> loadSeisanData(QueryRunner queryRunnerWip, int jissekiNo) throws SQLException {
        // 生産実績データの取得
        String sql = "SELECT goukicode "
                + "FROM seisan "
                + "WHERE jissekino = ? ";

        Map mapping = new HashMap<>();
        mapping.put("goukicode", "goukicode");
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<Seisan>> beanHandler = new BeanListHandler<>(Seisan.class, rowProcessor);

        List<Object> params = new ArrayList<>();
        params.add(jissekiNo);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, beanHandler, params.toArray());
    }
}
