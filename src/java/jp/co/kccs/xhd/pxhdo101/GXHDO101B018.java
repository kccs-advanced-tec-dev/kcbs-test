/*
 * Copyright 2019 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

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
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.FXHDD06;
import jp.co.kccs.xhd.db.model.SrSyosei;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/06/16<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B018(焼成・RHK焼成)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/06/15
 */
public class GXHDO101B018 implements IFormLogic {

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

            //日付入力をチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B018Const.BTN_STARTDATETIME_TOP,
                    GXHDO101B018Const.BTN_STARTDATETIME_BOTTOM,
                    GXHDO101B018Const.BTN_ENDDATETIME_TOP,
                    GXHDO101B018Const.BTN_ENDDATETIME_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B018Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B018Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B018Const.BTN_INSERT_TOP,
                    GXHDO101B018Const.BTN_INSERT_BOTTOM,
                    GXHDO101B018Const.BTN_DELETE_TOP,
                    GXHDO101B018Const.BTN_DELETE_BOTTOM,
                    GXHDO101B018Const.BTN_UPDATE_TOP,
                    GXHDO101B018Const.BTN_UPDATE_BOTTOM));

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

        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
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
            String kojyo = lotNo.substring(0, 3); //工場ｺｰﾄﾞ
            String lotNo8 = lotNo.substring(3, 11); //ﾛｯﾄNo(8桁)
            String edaban = lotNo.substring(11, 14); //枝番
            String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
            String formTitle = StringUtil.nullToBlank(session.getAttribute("formTitle"));
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            BigDecimal rev = BigDecimal.ZERO;
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_KARI_TOROKU, systemTime);
            }

            if (StringUtil.isEmpty(processData.getInitJotaiFlg()) || JOTAI_FLG_SAKUJO.equals(processData.getInitJotaiFlg())) {

                // 焼成_仮登録登録処理
                insertTmpSrSyosei(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList());

            } else {

                // 焼成_仮登録更新処理
                updateTmpSrSyosei(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList());
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
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

        FXHDD01 shinkuCheck = getItemRow(processData.getItemList(), GXHDO101B018Const.SYOSEI_SETTEI_PTN_CHECK); //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
        if (!"true".equals(shinkuCheck.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(shinkuCheck);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, shinkuCheck.getLabel1());
        }

        ErrorMessageInfo errorMessageInfo;
        //1回目再酸化関連チェック
        errorMessageInfo = checkSaisanka(processData);
        // エラーがある場合リターン
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 前工程関連チェック
        errorMessageInfo = checkMaekotei(processData);
        // エラーがある場合リターン
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 kaishiDay = getItemRow(processData.getItemList(), GXHDO101B018Const.KAISHI_DAY); //開始日
        FXHDD01 kaishiTime = getItemRow(processData.getItemList(), GXHDO101B018Const.KAISHI_TIME); //開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(kaishiDay.getValue(), kaishiTime.getValue());
        FXHDD01 shuryouDay = getItemRow(processData.getItemList(), GXHDO101B018Const.SHURYOU_DAY); //終了日
        FXHDD01 shuryouTime = getItemRow(processData.getItemList(), GXHDO101B018Const.SHURYOU_TIME); //終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(shuryouDay.getValue(), shuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(kaishiDay.getLabel1(), kaishiDate, shuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(kaishiDay, kaishiTime, shuryouDay, shuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        return null;
    }

    /**
     * 1回目再酸化関連ﾁｪｯｸ(リスト内の項目で入力がある状態で他の1件でも未入力がある場合エラー)
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkSaisanka(ProcessData processData) {
        List<String> checkItemList = new ArrayList<>();
        // チェック対象をリストに追加
        checkItemList.add(GXHDO101B018Const.FIRST_SAISANKA_GOUKI1); //1回目再酸化号機1
        checkItemList.add(GXHDO101B018Const.FIRST_SAISANKA_GOUKI2); //1回目再酸化号機2
        checkItemList.add(GXHDO101B018Const.FIRST_SAISANKA_SETTEI_PTN); //1回目再酸化設定ﾊﾟﾀｰﾝ(true以外は未入力扱い)
        checkItemList.add(GXHDO101B018Const.FIRST_SAISANKA_KEEP_ONDO);//1回目再酸化ｷｰﾌﾟ温度
        checkItemList.add(GXHDO101B018Const.FIRST_SAISANKA_CONVEYER_SPEED);//1回目再酸化ｺﾝﾍﾞｱ速度
        checkItemList.add(GXHDO101B018Const.FIRST_SAISANKA_ATO_GAIKAN);//1回目再酸化後外観

        // 入力項目有無
        boolean existInputData = false;
        // 未入力項目
        FXHDD01 nonInputitemRow = null;

        for (String checkItemId : checkItemList) {
            // 項目の取得
            FXHDD01 itemRow = getItemRow(processData.getItemList(), checkItemId);
            if (isNonInputItem(itemRow)) {
                // 値が入力されていない場合
                if (nonInputitemRow == null) {
                    // 未入力の項目、1件目を保持する
                    nonInputitemRow = itemRow;
                }
            } else {
                // 値が入力されている場合
                existInputData = true;
            }

            // 入力項目が存在するかつ未入力の項目も存在する場合
            if (existInputData && nonInputitemRow != null) {
                List<FXHDD01> errFxhdd01List = Arrays.asList(nonInputitemRow);
                return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, nonInputitemRow.getLabel1());
            }
        }
        return null;
    }

    /**
     * 前工程関連ﾁｪｯｸ、前工程が特定の工程以外の場合、必須チェックを行う。
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkMaekotei(ProcessData processData) {

        // 前工程の画面IDを取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String maekoteiFormId = StringUtil.nullToBlank(session.getAttribute("maekoteiFormId"));

        //前工程が焼成・2次脱脂(ﾍﾞﾙﾄ)、焼成・窒素脱脂の場合チェック不要
        if ("GXHDO101B016".equals(maekoteiFormId) || "GXHDO101B015".equals(maekoteiFormId)) {
            return null;
        }

        List<String> checkItemList = new ArrayList<>();
        checkItemList.add(GXHDO101B018Const.NIJIDASSHI_GOUKI); //2次脱脂号機
        checkItemList.add(GXHDO101B018Const.NIJIDASSHI_SETTEI_PATTERN); //2次脱脂設定ﾊﾟﾀｰﾝ
        checkItemList.add(GXHDO101B018Const.NIJIDASSHI_KEEPONDO);//2次脱脂ｷｰﾌﾟ温度
        checkItemList.add(GXHDO101B018Const.NIJIDASSHI_CONVEYER_SPEED);//2次脱脂ｺﾝﾍﾞｱ速度

        for (String checkItemId : checkItemList) {
            // 項目の取得
            FXHDD01 itemRow = getItemRow(processData.getItemList(), checkItemId);
            if (isNonInputItem(itemRow)) {
                // 未入力項目がある場合、リターン
                return MessageUtil.getErrorMessageInfo("XHD-000003", true, true, Arrays.asList(itemRow), itemRow.getLabel1());
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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            } else {
                rev = new BigDecimal(processData.getInitRev());
                // 最新のリビジョンを採番
                newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            SrSyosei tmpSrSyosei = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrSyosei> srSyoseiList = getSrSyoseiData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban, jissekino);
                if (!srSyoseiList.isEmpty()) {
                    tmpSrSyosei = srSyoseiList.get(0);
                }

                deleteTmpSrSyosei(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);
            }

            // 焼成_登録処理
            insertSrSyosei(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList(), tmpSrSyosei);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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
        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
        processData.setUserAuthParam(GXHDO101B018Const.USER_AUTH_UPDATE_PARAM);

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 焼成_更新処理
            updateSrSyosei(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, jissekino, systemTime, processData.getItemList());

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, conDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekino, "0", kikakuError.getKikakuchiInputErrorInfoList());
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

        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
        processData.setUserAuthParam(GXHDO101B018Const.USER_AUTH_DELETE_PARAM);

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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            // 品質DB登録実績データ取得
            //ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
            BigDecimal newRev = getNewRev(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId, jissekino);

            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekino, JOTAI_FLG_SAKUJO, systemTime);

            // 焼成_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban, jissekino);
            insertDeleteDataTmpSrSyosei(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, jissekino, systemTime);

            // 焼成_削除処理
            deleteSrSyosei(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban, jissekino);

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            // 完了メッセージとコールバックパラメータを設定
            processData.setCompMessage("削除しました。");
            processData.setCollBackParam("complete");

            return processData;

        } catch (NumberFormatException e) {
            ErrUtil.outputErrorLog("NumberFormatException発生", e, LOGGER);

            // コネクションロールバック処理
            DBUtil.rollbackConnection(conDoc, LOGGER);
            DBUtil.rollbackConnection(conQcdb, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));

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
                        GXHDO101B018Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B018Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B018Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B018Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B018Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B018Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B018Const.BTN_UPDATE_TOP,
                        GXHDO101B018Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B018Const.BTN_DELETE_TOP,
                        GXHDO101B018Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B018Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B018Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B018Const.BTN_INSERT_BOTTOM,
                        GXHDO101B018Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B018Const.BTN_STARTDATETIME_TOP,
                        GXHDO101B018Const.BTN_STARTDATETIME_BOTTOM,
                        GXHDO101B018Const.BTN_ENDDATETIME_TOP,
                        GXHDO101B018Const.BTN_ENDDATETIME_BOTTOM,
                        GXHDO101B018Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B018Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B018Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B018Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B018Const.BTN_INSERT_TOP,
                        GXHDO101B018Const.BTN_INSERT_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B018Const.BTN_DELETE_TOP,
                        GXHDO101B018Const.BTN_DELETE_BOTTOM,
                        GXHDO101B018Const.BTN_UPDATE_TOP,
                        GXHDO101B018Const.BTN_UPDATE_BOTTOM
                ));

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
            case GXHDO101B018Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B018Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B018Const.BTN_INSERT_TOP:
            case GXHDO101B018Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B018Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B018Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B018Const.BTN_UPDATE_TOP:
            case GXHDO101B018Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B018Const.BTN_DELETE_TOP:
            case GXHDO101B018Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 開始日時
            case GXHDO101B018Const.BTN_STARTDATETIME_TOP:
            case GXHDO101B018Const.BTN_STARTDATETIME_BOTTOM:
                method = "setStartDateTime";
                break;
            // 終了日時
            case GXHDO101B018Const.BTN_ENDDATETIME_TOP:
            case GXHDO101B018Const.BTN_ENDDATETIME_BOTTOM:
                method = "setEndDateTime";
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
     * @throws NumberFormatException 例外エラー
     */
    private ProcessData setInitDate(ProcessData processData) throws SQLException, NumberFormatException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
        int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

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
        
        // 指示温度データ取得
        List<FXHDD06> shijiOndoData = loadFxhdd06(queryRunnerDoc, lotNo);
        
        // 条件データ取得
        String sekkeiNo = StringUtil.nullToBlank(getMapData(sekkeiData, "SEKKEINO")); // 設計No
        
        // 条件から規格値(号機/温度)を取得
        Map jokenDataGokiOndo = loadDaJokenData(queryRunnerQcdb, sekkeiNo, "焼成", "設定", "ﾋﾟｰｸ温度");
        String kikakuchiGokiOndo = StringUtil.nullToBlank(getMapData(jokenDataGokiOndo, "KIKAKUCHI"));
        
        // 条件から規格値(水素濃度)を取得
        Map jokenDataH2Nodo = loadDaJokenData(queryRunnerQcdb, sekkeiNo, "焼成", "設定", "水素濃度");
        String kikakuchiH2Nodo = StringUtil.nullToBlank(getMapData(jokenDataH2Nodo, "KIKAKUCHI"));
        
        // 焼成ピーク温度指示取得
        Map ondoShijiInfo = getPeakShijiOndo(shijiOndoData, kikakuchiGokiOndo, kikakuchiH2Nodo);
        String ondoShiji = "";
        if (null != ondoShijiInfo.get("errorMessage")) {
            errorMessageList.add((String) ondoShijiInfo.get("errorMessage"));
        } else {
            ondoShiji = (String)ondoShijiInfo.get("peakShijiOndo");
        }

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, jissekino)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, lotKbnMasData, ownerMasData, shikakariData, lotNo, ondoShiji);

        processData.setInitMessageList(errorMessageList);
        return processData;
    }
    
    /**
     * 焼成ﾋﾟｰｸ温度指示情報の取得
     * 
     * @param shijiOndoData 指示温度情報
     * @param kikakuchiGokiOndo 規格値(号機/温度)
     * @param kikakuchiH2Nodo 規格値(水素濃度)
     * @return 焼成ﾋﾟｰｸ温度指示情報 / エラーメッセージ
     */
    private Map<String, String> getPeakShijiOndo(List<FXHDD06> shijiOndoData, String kikakuchiGokiOndo, String kikakuchiH2Nodo) {
        Map result = new HashMap<String, String>();
        result.put("peakShijiOndo", null);
        result.put("errorMessage", null);
        
        if (!CollectionUtils.isEmpty(shijiOndoData)) {
            // 1.指示温度情報取得
            Map<Integer, List<FXHDD06>> groupOndoMap = 
                    shijiOndoData.stream().collect(Collectors.groupingBy(n -> n.getShijiondogroup()));
            
            // 同一グループ内の「指示温度」、「水素濃度」が同一であるかチェック
            for (int group : groupOndoMap.keySet()) {
                List<FXHDD06> data = groupOndoMap.get(group);
                
                // 同一グループ内のデータを「指示温度」でグループ化し、2件以上のグループが存在する場合はエラー
                int chkOndoCount = data.stream().collect(Collectors.groupingBy(n -> n.getShijiondo())).size();
                if (1 < chkOndoCount) {
                    result.put("errorMessage", MessageUtil.getMessage("XHD-000079"));
                    return result;
                }
                // 同一グループ内のデータを「水素濃度」でグループ化し、2件以上のグループが存在する場合はエラー
                int chkH2Count = data.stream().collect(Collectors.groupingBy(n -> n.getSuisonoudo())).size();
                if (1 < chkH2Count) {
                    result.put("errorMessage", MessageUtil.getMessage("XHD-000080"));
                    return result;
                }
            }

            // グループごとに号機情報(文字列)を連結し、「焼成ﾋﾟｰｸ温度指示」に設定する文字列を編集する
            StringBuilder sb = new StringBuilder();
            for (int group : groupOndoMap.keySet()) {
                List<FXHDD06> data = groupOndoMap.get(group);
                String[] ondoGroupStr = getGroupData(data, group);
                if (0 < sb.length()) {
                    sb.append("、");
                }
                sb.append(String.format("[号機情報:%s/指示温度:%s/水素濃度:%s]", (Object[]) ondoGroupStr));
            }
            
            result.put("peakShijiOndo", sb.toString());
            return result;
            
        } else {
            // 2.条件情報取得
            // 規格値(号機/温度)が取得できない場合エラー
            if (StringUtils.isEmpty(kikakuchiGokiOndo)) {
                result.put("errorMessage", MessageUtil.getMessage("XHD-000071"));
                return result;
            }
            // 規格値(水素濃度)が取得できない場合エラー
            if (StringUtils.isEmpty(kikakuchiH2Nodo)) {
                result.put("errorMessage", MessageUtil.getMessage("XHD-000072"));
                return result;
            }
            
            // 規格値(号機/温度) + " " + 規格値(水素濃度) の文字列を返却する
            result.put("peakShijiOndo", kikakuchiGokiOndo + " " + kikakuchiH2Nodo);
            return result;
        }
    }
    
    /**
     * 指示温度データグループ別データ取得 ※グループ別にまとめて1レコードとしてデータを取得、号機情報については
     * 値がそれぞれ異なるため、結合して値を返す
     *
     * @param fxhdd06List 指示温度データリスト
     */
    private String[] getGroupData(List<FXHDD06> fxhdd06List, Integer getgroup) {
        String shijiOndo = "";
        String suisoNoudo = "";
        StringBuilder goukiJouhoGroup = new StringBuilder();
        int goukiJouho;
        int sabun;
        int prevGoukiJouho = 0;
        int renbanCount = 0;

        for (FXHDD06 fxhdd06 : fxhdd06List) {
            // 一致しないグループについてはコンティニュー
            if (!getgroup.equals(fxhdd06.getShijiondogroup())) {
                continue;
            }

            // 号機情報を数値化
            try {
                goukiJouho = Integer.parseInt(StringUtil.nullToBlank(fxhdd06.getGoukijyoho()));
            } catch (NumberFormatException e) {
                // ※エラーになることはありえないはずだが念の為、例外対応
                continue;
            }

            // 初回データセット
            if (0 == goukiJouhoGroup.length()) {
                //指示温度
                shijiOndo = StringUtil.nullToBlank(fxhdd06.getShijiondo());
                //水素濃度
                suisoNoudo = StringUtil.nullToBlank(fxhdd06.getSuisonoudo());
                // 号機情報
                goukiJouhoGroup.append(goukiJouho);
                prevGoukiJouho = goukiJouho;
                continue;
            }

            // 号機情報の前回値の差分を比較
            sabun = goukiJouho - prevGoukiJouho;

            // 差分が1の場合
            if (sabun == 1) {
                // 連番が続いている場合
                renbanCount++;
            } else if (1 < sabun) {
                if (1 < renbanCount) {
                    // 3つ以上の連番が途切れた場合
                    //前回までの連番分を追加
                    goukiJouhoGroup.append("～");
                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
                    renbanCount = 0;
                } else if (1 == renbanCount) {
                    // 1回の連番が途切れた場合
                    goukiJouhoGroup.append(",");
                    goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
                    renbanCount = 0;
                }

                //今回の号機情報を追加
                goukiJouhoGroup.append(",");
                goukiJouhoGroup.append(StringUtil.nullToBlank(goukiJouho));
            }
            //今回の号機情報を前回値にセット
            prevGoukiJouho = goukiJouho;
        }

        if (1 < renbanCount) {
            // 3つ以上の連番が途切れた場合
            //前回までの連番分を追加
            goukiJouhoGroup.append("～");
            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
        } else if (1 == renbanCount) {
            // 1回の連番が途切れた場合
            goukiJouhoGroup.append(",");
            goukiJouhoGroup.append(StringUtil.nullToBlank(prevGoukiJouho));
        }

        return new String[]{goukiJouhoGroup.toString(), shijiOndo, suisoNoudo};
    }

    /**
     * 入力項目以外のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     * @param ondoShiji 焼成ﾋﾟｰｸ温度指示
     */
    private void setViewItemData(ProcessData processData, Map lotKbnMasData, Map ownerMasData, Map shikakariData, String lotNo, String ondoShiji) {

        // ロットNo
        this.setItemData(processData, GXHDO101B018Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B018Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // 客先
        this.setItemData(processData, GXHDO101B018Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B018Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B018Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B018Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B018Const.OWNER, ownercode + ":" + owner);
        }

        // 指示
        this.setItemData(processData, GXHDO101B018Const.SIJI, "");
        
        // 焼成ﾋﾟｰｸ温度指示
        this.setItemData(processData, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SIJI, ondoShiji);
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

        List<SrSyosei> srSyoseiDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId, jissekino);
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
                
                // 前工程情報取得
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) externalContext.getSession(false);
                Map maekoteiInfo = (Map) session.getAttribute("maekoteiInfo");
                String maekoteiFormId = StringUtil.nullToBlank(session.getAttribute("maekoteiFormId"));
                
                // 受入ｾｯﾀ枚数(前工程情報がある場合は前工程情報の値をセットする。)
                FXHDD01 itemSettaMaisu = this.getItemRow(processData.getItemList(), GXHDO101B018Const.UKEIRE_SETTA_MAISU);
                if ("GXHDO101B013".equals(maekoteiFormId)) {
                    //前工程がｾｯﾀ詰めの場合、ｾｯﾀ枚数をセット
                    CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "sayasuu", false, true);
                } else if ("GXHDO101B014".equals(maekoteiFormId) ||
                           "GXHDO101B015".equals(maekoteiFormId) ||
                           "GXHDO101B016".equals(maekoteiFormId) ||
                           "GXHDO101B017".equals(maekoteiFormId) ||
                           "GXHDO101B018".equals(maekoteiFormId) ||
                           "GXHDO101B019".equals(maekoteiFormId)) {
                    //前工程がAir脱脂、窒素脱脂、2次脱脂(ﾍﾞﾙﾄ)、焼成、RHK焼成、再酸化の場合、回収ｾｯﾀ枚数をセット
                    if ("GXHDO101B014".equals(maekoteiFormId) ||
                        "GXHDO101B015".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaisyusettersuu", false, true);
                    } else if ("GXHDO101B016".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaishuusettasuu", false, true);
                    } else if ("GXHDO101B017".equals(maekoteiFormId) ||
                               "GXHDO101B018".equals(maekoteiFormId) ||
                               "GXHDO101B019".equals(maekoteiFormId)) {
                        CommonUtil.setMaekoteiInfo(itemSettaMaisu, maekoteiInfo, "kaishusettasuu", false, true);
                    }
                }

                return true;
            }

            // 焼成データ取得
            srSyoseiDataList = getSrSyoseiData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban, jissekino);
            if (srSyoseiDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSyoseiDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSyoseiDataList.get(0));

        return true;
    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSyoseiData 焼成データ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSyosei srSyoseiData) {
        //受入ｾｯﾀ枚数
        this.setItemData(processData, GXHDO101B018Const.UKEIRE_SETTA_MAISU, getSrSyoseiItemData(GXHDO101B018Const.UKEIRE_SETTA_MAISU, srSyoseiData));
        //開始日
        this.setItemData(processData, GXHDO101B018Const.KAISHI_DAY, getSrSyoseiItemData(GXHDO101B018Const.KAISHI_DAY, srSyoseiData));
        //開始時間
        this.setItemData(processData, GXHDO101B018Const.KAISHI_TIME, getSrSyoseiItemData(GXHDO101B018Const.KAISHI_TIME, srSyoseiData));
        //開始担当者
        this.setItemData(processData, GXHDO101B018Const.KAISHI_TANTOUSYA, getSrSyoseiItemData(GXHDO101B018Const.KAISHI_TANTOUSYA, srSyoseiData));
        //開始確認者
        this.setItemData(processData, GXHDO101B018Const.KAISHI_KAKUNINSYA, getSrSyoseiItemData(GXHDO101B018Const.KAISHI_KAKUNINSYA, srSyoseiData));
        //投入ｾｯﾀ枚数
        this.setItemData(processData, GXHDO101B018Const.TOUNYU_SETTA_MAISU, getSrSyoseiItemData(GXHDO101B018Const.TOUNYU_SETTA_MAISU, srSyoseiData));
        //2次脱脂号機
        this.setItemData(processData, GXHDO101B018Const.NIJIDASSHI_GOUKI, getSrSyoseiItemData(GXHDO101B018Const.NIJIDASSHI_GOUKI, srSyoseiData));
        //2次脱脂設定ﾊﾟﾀｰﾝ
        this.setItemData(processData, GXHDO101B018Const.NIJIDASSHI_SETTEI_PATTERN, getSrSyoseiItemData(GXHDO101B018Const.NIJIDASSHI_SETTEI_PATTERN, srSyoseiData));
        //2次脱脂ｷｰﾌﾟ温度
        this.setItemData(processData, GXHDO101B018Const.NIJIDASSHI_KEEPONDO, getSrSyoseiItemData(GXHDO101B018Const.NIJIDASSHI_KEEPONDO, srSyoseiData));
        //2次脱脂ｺﾝﾍﾞｱ速度
        this.setItemData(processData, GXHDO101B018Const.NIJIDASSHI_CONVEYER_SPEED, getSrSyoseiItemData(GXHDO101B018Const.NIJIDASSHI_CONVEYER_SPEED, srSyoseiData));
        //焼成号機
        this.setItemData(processData, GXHDO101B018Const.SYOSEI_GOUKI, getSrSyoseiItemData(GXHDO101B018Const.SYOSEI_GOUKI, srSyoseiData));
        //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
        this.setItemData(processData, GXHDO101B018Const.SYOSEI_SETTEI_PTN_CHECK, getSrSyoseiItemData(GXHDO101B018Const.SYOSEI_SETTEI_PTN_CHECK, srSyoseiData));
//        //焼成ﾋﾟｰｸ温度指示
//        this.setItemData(processData, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SIJI, getSrSyoseiItemData(GXHDO101B018Const.SYOSEI_PEAK_ONDO_SIJI, srSyoseiData));
        //焼成ﾋﾟｰｸ温度設定値
        this.setItemData(processData, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SETTEICHI, getSrSyoseiItemData(GXHDO101B018Const.SYOSEI_PEAK_ONDO_SETTEICHI, srSyoseiData));
        //焼成ﾛｰﾗｰ速度
        this.setItemData(processData, GXHDO101B018Const.SYOSEI_ROLLER_SPEED, getSrSyoseiItemData(GXHDO101B018Const.SYOSEI_ROLLER_SPEED, srSyoseiData));
        //焼成ﾊﾟｰｼﾞ
        this.setItemData(processData, GXHDO101B018Const.SYOSEI_PURGE, getSrSyoseiItemData(GXHDO101B018Const.SYOSEI_PURGE, srSyoseiData));
        //1回目再酸化号機1
        this.setItemData(processData, GXHDO101B018Const.FIRST_SAISANKA_GOUKI1, getSrSyoseiItemData(GXHDO101B018Const.FIRST_SAISANKA_GOUKI1, srSyoseiData));
        //1回目再酸化号機2
        this.setItemData(processData, GXHDO101B018Const.FIRST_SAISANKA_GOUKI2, getSrSyoseiItemData(GXHDO101B018Const.FIRST_SAISANKA_GOUKI2, srSyoseiData));
        //1回目再酸化設定ﾊﾟﾀｰﾝ
        this.setItemData(processData, GXHDO101B018Const.FIRST_SAISANKA_SETTEI_PTN, getSrSyoseiItemData(GXHDO101B018Const.FIRST_SAISANKA_SETTEI_PTN, srSyoseiData));
        //1回目再酸化ｷｰﾌﾟ温度
        this.setItemData(processData, GXHDO101B018Const.FIRST_SAISANKA_KEEP_ONDO, getSrSyoseiItemData(GXHDO101B018Const.FIRST_SAISANKA_KEEP_ONDO, srSyoseiData));
        //1回目再酸化ｺﾝﾍﾞｱ速度
        this.setItemData(processData, GXHDO101B018Const.FIRST_SAISANKA_CONVEYER_SPEED, getSrSyoseiItemData(GXHDO101B018Const.FIRST_SAISANKA_CONVEYER_SPEED, srSyoseiData));
        //1回目再酸化後外観
        this.setItemData(processData, GXHDO101B018Const.FIRST_SAISANKA_ATO_GAIKAN, getSrSyoseiItemData(GXHDO101B018Const.FIRST_SAISANKA_ATO_GAIKAN, srSyoseiData));
        //終了日
        this.setItemData(processData, GXHDO101B018Const.SHURYOU_DAY, getSrSyoseiItemData(GXHDO101B018Const.SHURYOU_DAY, srSyoseiData));
        //終了時間
        this.setItemData(processData, GXHDO101B018Const.SHURYOU_TIME, getSrSyoseiItemData(GXHDO101B018Const.SHURYOU_TIME, srSyoseiData));
        //終了担当者
        this.setItemData(processData, GXHDO101B018Const.SHURYOU_TANTOUSYA, getSrSyoseiItemData(GXHDO101B018Const.SHURYOU_TANTOUSYA, srSyoseiData));
        //回収ｾｯﾀ枚数
        this.setItemData(processData, GXHDO101B018Const.KAISHU_SETTA_MAISU, getSrSyoseiItemData(GXHDO101B018Const.KAISHU_SETTA_MAISU, srSyoseiData));
        //備考1
        this.setItemData(processData, GXHDO101B018Const.BIKOU1, getSrSyoseiItemData(GXHDO101B018Const.BIKOU1, srSyoseiData));
        //備考2
        this.setItemData(processData, GXHDO101B018Const.BIKOU2, getSrSyoseiItemData(GXHDO101B018Const.BIKOU2, srSyoseiData));
    }

    /**
     * 焼成の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @param jissekino 実績No
     * @return 焼成登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSyosei> getSrSyoseiData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSyosei(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        } else {
            return loadTmpSrSyosei(queryRunnerQcdb, kojyo, lotNo, edaban, jissekino, rev);
        }
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
        String sql = "SELECT SEKKEINO "
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
        String sql = "SELECT kcpno, oyalotedaban, suuryo, torikosuu, lotkubuncode, ownercode, tokuisaki"
                + " FROM sikakari WHERE kojyo = ? AND lotno = ? AND edaban = ? ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerWip.query(sql, new MapHandler(), params.toArray());
    }
    
    /**
     * [指示温度]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<FXHDD06> loadFxhdd06(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        String sql = "SELECT goukijyoho,shijiondo,suisonoudo,shijiondogroup,rev "
                + "FROM fxhdd06 "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND deleteflag = ? "
                + "ORDER BY shijiondogroup, goukijyoho";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);
        params.add(0);

        Map<String, String> mapping = new HashMap<>();
        mapping.put("goukijyoho", "goukijyoho"); //号機情報
        mapping.put("shijiondo", "shijiondo"); //指示温度
        mapping.put("suisonoudo", "suisonoudo"); //水素濃度
        mapping.put("shijiondogroup", "shijiondogroup"); //指示温度ｸﾞﾙｰﾌﾟ
        mapping.put("rev", "rev"); //REV

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<FXHDD06>> beanHandler = new BeanListHandler<>(FXHDD06.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, beanHandler, params.toArray());
    }
    
    /**
     * 条件データ検索
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param sekkeiNo 設計No(検索キー)
     * @param kouteiMei 工程名(検索キー)
     * @param komokuMei 項目名(検索キー)
     * @param kanriKomoku 管理項目名(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadDaJokenData(QueryRunner queryRunnerQcdb, String sekkeiNo, String kouteiMei, String komokuMei, String kanriKomoku) throws SQLException {
        // 条件データ取得
        String sql = "SELECT KIKAKUCHI FROM da_joken "
                + "WHERE SEKKEINO = ? AND KOUTEIMEI = ? AND KOUMOKUMEI = ? AND KANRIKOUMOKU = ? ";
        
        List<Object> params = Arrays.asList(sekkeiNo, kouteiMei, komokuMei, kanriKomoku);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * [品質DB登録実績]から、ﾘﾋﾞｼﾞｮﾝ,状態ﾌﾗｸﾞを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param formId 画面ID(検索キー)
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        // 品質DB登録実績データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);

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
     * @param jissekino 実績No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        // 設計データの取得
        String sql = "SELECT rev, jotai_flg "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? "
                + "FOR UPDATE NOWAIT ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);

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
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private BigDecimal getNewRev(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo,
            String edaban, String formId, int jissekino) throws SQLException {
        BigDecimal newRev = BigDecimal.ONE;
        // 設計データの取得
        String sql = "SELECT MAX(rev) AS rev "
                + "FROM fxhdd03 "
                + "WHERE kojyo = ? AND lotno = ? "
                + "AND edaban = ? AND gamen_id = ? "
                + "AND jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(formId);
        params.add(jissekino);
        Map map = queryRunnerDoc.query(conDoc, sql, new MapHandler(), params.toArray());
        if (map != null && !map.isEmpty()) {
            newRev = new BigDecimal(String.valueOf(map.get("rev")));
            newRev = newRev.add(BigDecimal.ONE);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return newRev;
    }

    /**
     * [焼成]から、ﾃﾞｰﾀを取得
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
    private List<SrSyosei> loadSrSyosei(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {

        String sql = "SELECT "
                + "kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,"
                + "bkaisinichiji,bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,torokunichiji,kosinnichiji,"
                + "SankaGoki,SankaOndo,SankaSyuryoNichiJi,tounyusettasuu,setteipattern,dansuu,gaikancheck,"
                + "kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,"
                + "Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,"
                + "saisankaCsokudo,saisankagogaikan,syoseisyurui,revision,'0' AS deleteflag "
                + "FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";
        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            sql += "AND revision = ? ";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("kcpno", "kcpno"); //KCPNo
        mapping.put("kosuu", "kosuu"); //個数
        mapping.put("genryohinsyumei", "genryohinsyumei"); //原料品種名
        mapping.put("genryogroup", "genryogroup"); //原料ｸﾞﾙｰﾌﾟ
        mapping.put("skaisinichiji", "skaisinichiji"); //焼成開始日時
        mapping.put("ssyuryonichiji", "ssyuryonichiji"); //焼成終了日時
        mapping.put("bprogramno", "bprogramno"); //ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
        mapping.put("syoseiondo", "syoseiondo"); //焼成温度
        mapping.put("goki", "goki"); //ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
        mapping.put("ssettermaisuu", "ssettermaisuu"); //開始日時
        mapping.put("nyurodaibanmaisuu", "nyurodaibanmaisuu"); //入炉台板枚数
        mapping.put("skaisitantosya", "skaisitantosya"); //焼成開始担当者
        mapping.put("ssyuryotantosya", "ssyuryotantosya"); //終了日時
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("biko3", "biko3"); //備考3
        mapping.put("biko4", "biko4"); //備考4
        mapping.put("biko5", "biko5"); //備考5
        mapping.put("bkaisinichiji", "bkaisinichiji"); //ﾊﾞﾗｼ開始日時
        mapping.put("bsyuryonichiji", "bsyuryonichiji"); //ﾊﾞﾗｼ終了日時
        mapping.put("bsettermaisuu", "bsettermaisuu"); //ﾊﾞﾗｼｾｯﾀｰ枚数
        mapping.put("potsuu", "potsuu"); //ﾎﾟｯﾄ数
        mapping.put("potno", "potno"); //ﾎﾟｯﾄNo
        mapping.put("btantosya", "btantosya"); //ﾊﾞﾗｼ担当者
        mapping.put("biko6", "biko6"); //備考6
        mapping.put("biko7", "biko7"); //備考7
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("SankaGoki", "sankagoki"); //再酸化号機
        mapping.put("SankaOndo", "sankaondo"); //再酸化温度
        mapping.put("SankaSyuryoNichiJi", "sankasyuryonichiji"); //再酸化終了日時
        mapping.put("tounyusettasuu", "tounyusettasuu"); //投入ｾｯﾀ枚数
        mapping.put("setteipattern", "setteipattern"); //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
        mapping.put("dansuu", "dansuu"); //段数
        mapping.put("gaikancheck", "gaikancheck"); //外観確認
        mapping.put("kaishusettasuu", "kaishusettasuu"); //回収ｾｯﾀ枚数
        mapping.put("StartKakuninsyacode", "startkakuninsyacode"); //開始確認者
        mapping.put("nijidasshigouki", "nijidasshigouki"); //2次脱脂号機
        mapping.put("NijidasshisetteiPT", "nijidasshisetteipt"); //2次脱脂設定ﾊﾟﾀｰﾝ
        mapping.put("Nijidasshikeepondo", "nijidasshikeepondo"); //2次脱脂ｷｰﾌﾟ温度
        mapping.put("Nijidasshispeed", "nijidasshispeed"); //2次脱脂ｺﾝﾍﾞｱ速度
        mapping.put("peakondo", "peakondo"); //焼成ﾋﾟｰｸ温度指示
        mapping.put("syoseispeed", "syoseispeed"); //焼成ﾛｰﾗｰ速度
        mapping.put("syoseipurge", "syoseipurge"); //焼成ﾊﾟｰｼﾞ
        mapping.put("saisankagouki1", "saisankagouki1"); //1回目再酸化号機1
        mapping.put("saisankagouki2", "saisankagouki2"); //1回目再酸化号機2
        mapping.put("saisankasetteiPT", "saisankasetteipt"); //1回目再酸化設定ﾊﾟﾀｰﾝ
        mapping.put("saisankakeepondo", "saisankakeepondo"); //1回目再酸化ｷｰﾌﾟ温度
        mapping.put("saisankaCsokudo", "saisankacsokudo"); //1回目再酸化ｺﾝﾍﾞｱ速度
        mapping.put("saisankagogaikan", "saisankagogaikan"); //1回目再酸化後外観
        mapping.put("syoseisyurui", "syoseisyurui"); //焼成種類
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSyosei>> beanHandler = new BeanListHandler<>(SrSyosei.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [焼成_仮登録]から、ﾃﾞｰﾀを取得
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
    private List<SrSyosei> loadTmpSrSyosei(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, int jissekino, String rev) throws SQLException {
        String sql = "SELECT "
                + "kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,"
                + "bkaisinichiji,bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,torokunichiji,kosinnichiji,"
                + "SankaGoki,SankaOndo,SankaSyuryoNichiJi,tounyusettasuu,setteipattern,dansuu,gaikancheck,"
                + "kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,"
                + "Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,"
                + "saisankaCsokudo,saisankagogaikan,syoseisyurui,revision,deleteflag "
                + "FROM tmp_sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? AND deleteflag = ? ";
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

        // revisionが入っている場合、条件に追加
        if (!StringUtil.isEmpty(rev)) {
            params.add(rev);
        }

        Map<String, String> mapping = new HashMap<>();
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("jissekino", "jissekino"); //実績No
        mapping.put("kcpno", "kcpno"); //KCPNo
        mapping.put("kosuu", "kosuu"); //個数
        mapping.put("genryohinsyumei", "genryohinsyumei"); //原料品種名
        mapping.put("genryogroup", "genryogroup"); //原料ｸﾞﾙｰﾌﾟ
        mapping.put("skaisinichiji", "skaisinichiji"); //焼成開始日時
        mapping.put("ssyuryonichiji", "ssyuryonichiji"); //焼成終了日時
        mapping.put("bprogramno", "bprogramno"); //ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
        mapping.put("syoseiondo", "syoseiondo"); //焼成温度
        mapping.put("goki", "goki"); //ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
        mapping.put("ssettermaisuu", "ssettermaisuu"); //開始日時
        mapping.put("nyurodaibanmaisuu", "nyurodaibanmaisuu"); //入炉台板枚数
        mapping.put("skaisitantosya", "skaisitantosya"); //焼成開始担当者
        mapping.put("ssyuryotantosya", "ssyuryotantosya"); //終了日時
        mapping.put("biko1", "biko1"); //備考1
        mapping.put("biko2", "biko2"); //備考2
        mapping.put("biko3", "biko3"); //備考3
        mapping.put("biko4", "biko4"); //備考4
        mapping.put("biko5", "biko5"); //備考5
        mapping.put("bkaisinichiji", "bkaisinichiji"); //ﾊﾞﾗｼ開始日時
        mapping.put("bsyuryonichiji", "bsyuryonichiji"); //ﾊﾞﾗｼ終了日時
        mapping.put("bsettermaisuu", "bsettermaisuu"); //ﾊﾞﾗｼｾｯﾀｰ枚数
        mapping.put("potsuu", "potsuu"); //ﾎﾟｯﾄ数
        mapping.put("potno", "potno"); //ﾎﾟｯﾄNo
        mapping.put("btantosya", "btantosya"); //ﾊﾞﾗｼ担当者
        mapping.put("biko6", "biko6"); //備考6
        mapping.put("biko7", "biko7"); //備考7
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("SankaGoki", "sankagoki"); //再酸化号機
        mapping.put("SankaOndo", "sankaondo"); //再酸化温度
        mapping.put("SankaSyuryoNichiJi", "sankasyuryonichiji"); //再酸化終了日時
        mapping.put("tounyusettasuu", "tounyusettasuu"); //投入ｾｯﾀ枚数
        mapping.put("setteipattern", "setteipattern"); //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
        mapping.put("dansuu", "dansuu"); //段数
        mapping.put("gaikancheck", "gaikancheck"); //外観確認
        mapping.put("kaishusettasuu", "kaishusettasuu"); //回収ｾｯﾀ枚数
        mapping.put("StartKakuninsyacode", "startkakuninsyacode"); //開始確認者
        mapping.put("nijidasshigouki", "nijidasshigouki"); //2次脱脂号機
        mapping.put("NijidasshisetteiPT", "nijidasshisetteipt"); //2次脱脂設定ﾊﾟﾀｰﾝ
        mapping.put("Nijidasshikeepondo", "nijidasshikeepondo"); //2次脱脂ｷｰﾌﾟ温度
        mapping.put("Nijidasshispeed", "nijidasshispeed"); //2次脱脂ｺﾝﾍﾞｱ速度
        mapping.put("peakondo", "peakondo"); //焼成ﾋﾟｰｸ温度指示
        mapping.put("syoseispeed", "syoseispeed"); //焼成ﾛｰﾗｰ速度
        mapping.put("syoseipurge", "syoseipurge"); //焼成ﾊﾟｰｼﾞ
        mapping.put("saisankagouki1", "saisankagouki1"); //1回目再酸化号機1
        mapping.put("saisankagouki2", "saisankagouki2"); //1回目再酸化号機2
        mapping.put("saisankasetteiPT", "saisankasetteipt"); //1回目再酸化設定ﾊﾟﾀｰﾝ
        mapping.put("saisankakeepondo", "saisankakeepondo"); //1回目再酸化ｷｰﾌﾟ温度
        mapping.put("saisankaCsokudo", "saisankacsokudo"); //1回目再酸化ｺﾝﾍﾞｱ速度
        mapping.put("saisankagogaikan", "saisankagogaikan"); //1回目再酸化後外観
        mapping.put("syoseisyurui", "syoseisyurui"); //焼成種類
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSyosei>> beanHandler = new BeanListHandler<>(SrSyosei.class, rowProcessor);

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
     * @param processData 処理データ
     * @return 処理データ
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
            int jissekino = Integer.parseInt(StringUtil.nullToBlank(session.getAttribute("jissekino")));

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerWip, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, formId, jissekino);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            if (!(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 焼成データ取得
            List<SrSyosei> srSyoseiDataList = getSrSyoseiData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban, jissekino);
            if (srSyoseiDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSyoseiDataList.get(0));

            // 次呼出しメソッドをクリア
            processData.setMethod("");

            return processData;
        } catch (NumberFormatException ex) {
            ErrUtil.outputErrorLog("NumberFormatException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
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
     * @param srSyoseiData 焼成
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrSyosei srSyoseiData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srSyoseiData != null) {
            // 元データが存在する場合元データより取得
            return getSrSyoseiItemData(itemId, srSyoseiData);
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
     * @param jissekino 実績No
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param systemTime システム日付
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateFxhdd03(QueryRunner queryRunnerDoc, Connection conDoc, String tantoshaCd, String formId, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino, String jotaiFlg, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE fxhdd03 SET "
                + "koshinsha = ?, koshin_date = ?,"
                + "rev = ?, jotai_flg = ? "
                + "WHERE gamen_id = ? AND kojyo = ? "
                + "  AND lotno = ? AND edaban = ? "
                + "  AND jissekino = ? ";

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
     * 焼成_仮登録(tmp_sr_syosei)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_syosei ("
                + "kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,"
                + "bkaisinichiji,bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,torokunichiji,kosinnichiji,"
                + "SankaGoki,SankaOndo,SankaSyuryoNichiJi,tounyusettasuu,setteipattern,dansuu,gaikancheck,"
                + "kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,"
                + "Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,"
                + "saisankaCsokudo,saisankagogaikan,syoseisyurui,revision,deleteflag "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrSyosei(true, newRev, deleteflag, kojyo, lotNo, edaban, jissekino, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());

    }

    /**
     * 焼成_仮登録(tmp_sr_syosei)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_syosei SET "
                + "kcpno = ?,skaisinichiji = ?,ssyuryonichiji = ?,syoseiondo = ?,goki = ?,ssettermaisuu = ?,skaisitantosya = ?,ssyuryotantosya = ?,"
                + "biko1 = ?,biko2 = ?,kosinnichiji = ?,tounyusettasuu = ?,setteipattern = ?,kaishusettasuu = ?,StartKakuninsyacode = ?,"
                + "nijidasshigouki = ?,NijidasshisetteiPT = ?,Nijidasshikeepondo = ?,Nijidasshispeed = ?,peakondo = ?,syoseispeed = ?,syoseipurge = ?,"
                + "saisankagouki1 = ?,saisankagouki2 = ?,saisankasetteiPT = ?,saisankakeepondo = ?,saisankaCsokudo = ?,saisankagogaikan = ?,syoseisyurui = ?,"
                + "revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ? ";

        // 更新前の値を取得
        List<SrSyosei> srSyoseiList = getSrSyoseiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSyosei srSyosei = null;
        if (!srSyoseiList.isEmpty()) {
            srSyosei = srSyoseiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSyosei(false, newRev, 0, "", "", "", jissekino, systemTime, itemList, srSyosei);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成_仮登録(tmp_sr_syosei)削除処理
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
    private void deleteTmpSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM tmp_sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND jissekino = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成_仮登録(tmp_sr_syosei)更新値パラメータ設定
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
     * @param srSyoseiData 焼成データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrSyosei(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrSyosei srSyoseiData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.KCPNO, srSyoseiData)));  //KCPNO
        if (isInsert) {
            params.add(null); //個数
            params.add(null); //原料品種名
            params.add(null); //原料ｸﾞﾙｰﾌﾟ
        }
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B018Const.KAISHI_DAY, srSyoseiData),
                getItemData(itemList, GXHDO101B018Const.KAISHI_TIME, srSyoseiData))); //焼成開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B018Const.SHURYOU_DAY, srSyoseiData),
                getItemData(itemList, GXHDO101B018Const.SHURYOU_TIME, srSyoseiData))); //焼成終了日時
        if (isInsert) {
            params.add(null); //ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SETTEICHI, srSyoseiData)));  //焼成温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.SYOSEI_GOUKI, srSyoseiData)));  //ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.UKEIRE_SETTA_MAISU, srSyoseiData)));  //受入ｾｯﾀ枚数
        if (isInsert) {
            params.add(null); //入炉台板枚数
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.KAISHI_TANTOUSYA, srSyoseiData)));  //焼成開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.SHURYOU_TANTOUSYA, srSyoseiData)));  //終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.BIKOU1, srSyoseiData)));  //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.BIKOU2, srSyoseiData)));  //備考2
        if (isInsert) {
            params.add(null); //備考3
            params.add(null); //備考4
            params.add(null); //備考5
            params.add(null); //ﾊﾞﾗｼ開始日時
            params.add(null); //ﾊﾞﾗｼ終了日時
            params.add(null); //ﾊﾞﾗｼｾｯﾀｰ枚数
            params.add(null); //ﾎﾟｯﾄ数
            params.add(null); //ﾎﾟｯﾄNo
            params.add(null); //ﾊﾞﾗｼ担当者
            params.add(null); //備考6
            params.add(null); //備考7
        }
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        if (isInsert) {
            params.add(null); //再酸化号機
            params.add(null); //再酸化温度
            params.add(null); //再酸化終了日時
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.TOUNYU_SETTA_MAISU, srSyoseiData)));  //投入ｾｯﾀ枚数
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B018Const.SYOSEI_SETTEI_PTN_CHECK, srSyoseiData), null));  //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
        if (isInsert) {
            params.add(null); //段数
            params.add(null); //外観確認
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.KAISHU_SETTA_MAISU, srSyoseiData)));  //回収ｾｯﾀ枚数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.KAISHI_KAKUNINSYA, srSyoseiData)));  //開始確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_GOUKI, srSyoseiData)));  //2次脱脂号機
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_SETTEI_PATTERN, srSyoseiData), null));  //2次脱脂設定ﾊﾟﾀｰﾝ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_KEEPONDO, srSyoseiData)));  //2次脱脂ｷｰﾌﾟ温度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_CONVEYER_SPEED, srSyoseiData)));  //2次脱脂ｺﾝﾍﾞｱ速度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SIJI, srSyoseiData)));  //焼成ﾋﾟｰｸ温度指示
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.SYOSEI_ROLLER_SPEED, srSyoseiData)));  //焼成ﾛｰﾗｰ速度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.SYOSEI_PURGE, srSyoseiData)));  //焼成ﾊﾟｰｼﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_GOUKI1, srSyoseiData)));  //1回目再酸化号機1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_GOUKI2, srSyoseiData)));  //1回目再酸化号機2
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_SETTEI_PTN, srSyoseiData), null));  //1回目再酸化設定ﾊﾟﾀｰﾝ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_KEEP_ONDO, srSyoseiData)));  //1回目再酸化ｷｰﾌﾟ温度
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_CONVEYER_SPEED, srSyoseiData)));  //1回目再酸化ｺﾝﾍﾞｱ速度
        params.add(getComboValue(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_ATO_GAIKAN, srSyoseiData), null));//1回目再酸化後外観
        params.add("RHK焼成"); //焼成種類
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        return params;
    }

    /**
     * 焼成(sr_syosei)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrSyosei 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList, SrSyosei tmpSrSyosei) throws SQLException {

        String sql = "INSERT INTO sr_syosei ("
                + "kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,"
                + "bkaisinichiji,bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,torokunichiji,kosinnichiji,"
                + "SankaGoki,SankaOndo,SankaSyuryoNichiJi,tounyusettasuu,setteipattern,dansuu,gaikancheck,"
                + "kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,"
                + "Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,"
                + "saisankaCsokudo,saisankagogaikan,syoseisyurui,revision "
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrSyosei(true, newRev, kojyo, lotNo, edaban, jissekino, systemTime, itemList, tmpSrSyosei);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成(sr_syosei)更新処理
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
     * @throws SQLException 例外エラー
     */
    private void updateSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_syosei SET "
                + "kcpno = ?,skaisinichiji = ?,ssyuryonichiji = ?,syoseiondo = ?,goki = ?,ssettermaisuu = ?,skaisitantosya = ?,ssyuryotantosya = ?,"
                + "biko1 = ?,biko2 = ?,kosinnichiji = ?,tounyusettasuu = ?,setteipattern = ?,kaishusettasuu = ?,StartKakuninsyacode = ?,"
                + "nijidasshigouki = ?,NijidasshisetteiPT = ?,Nijidasshikeepondo = ?,Nijidasshispeed = ?,peakondo = ?,syoseispeed = ?,syoseipurge = ?,"
                + "saisankagouki1 = ?,saisankagouki2 = ?,saisankasetteiPT = ?,saisankakeepondo = ?,saisankaCsokudo = ?,saisankagogaikan = ?,syoseisyurui = ?,"
                + "revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? AND jissekino = ? ";

        // 更新前の値を取得
        List<SrSyosei> srSyoseiList = getSrSyoseiData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban, jissekino);
        SrSyosei srSyosei = null;
        if (!srSyoseiList.isEmpty()) {
            srSyosei = srSyoseiList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrSyosei(false, newRev, "", "", "", jissekino, systemTime, itemList, srSyosei);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        params.add(jissekino);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 焼成(sr_syosei)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srSyoseiData 焼成データ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrSyosei(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, int jissekino,
            Timestamp systemTime, List<FXHDD01> itemList, SrSyosei srSyoseiData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(jissekino); //実績No
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.KCPNO, srSyoseiData)));  //KCPNO
        if (isInsert) {
            params.add(0); //個数
            params.add(""); //原料品種名
            params.add(""); //原料ｸﾞﾙｰﾌﾟ
        }
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B018Const.KAISHI_DAY, srSyoseiData),
                getItemData(itemList, GXHDO101B018Const.KAISHI_TIME, srSyoseiData))); //焼成開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B018Const.SHURYOU_DAY, srSyoseiData),
                getItemData(itemList, GXHDO101B018Const.SHURYOU_TIME, srSyoseiData))); //焼成終了日時
        if (isInsert) {
            params.add(0); //ﾊﾞｯﾁ炉ﾌﾟﾛｸﾞﾗﾑNo
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SETTEICHI, srSyoseiData)));  //焼成温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.SYOSEI_GOUKI, srSyoseiData)));  //ﾄﾝﾈﾙ炉・ﾊﾞｯﾁ炉号機
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.UKEIRE_SETTA_MAISU, srSyoseiData)));  //受入ｾｯﾀ枚数
        if (isInsert) {
            params.add(0); //入炉台板枚数
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.KAISHI_TANTOUSYA, srSyoseiData)));  //焼成開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.SHURYOU_TANTOUSYA, srSyoseiData)));  //終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.BIKOU1, srSyoseiData)));  //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.BIKOU2, srSyoseiData)));  //備考2
        if (isInsert) {
            params.add(""); //備考3
            params.add(""); //備考4
            params.add(""); //備考5
            params.add("0000-00-00 00:00:00"); //ﾊﾞﾗｼ開始日時
            params.add("0000-00-00 00:00:00"); //ﾊﾞﾗｼ終了日時
            params.add(0); //ﾊﾞﾗｼｾｯﾀｰ枚数
            params.add(0); //ﾎﾟｯﾄ数
            params.add(""); //ﾎﾟｯﾄNo
            params.add(""); //ﾊﾞﾗｼ担当者
            params.add(""); //備考6
            params.add(""); //備考7
        }
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時
        if (isInsert) {
            params.add(""); //再酸化号機
            params.add(0); //再酸化温度
            params.add("0000-00-00 00:00:00"); //再酸化終了日時
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.TOUNYU_SETTA_MAISU, srSyoseiData)));  //投入ｾｯﾀ枚数
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B018Const.SYOSEI_SETTEI_PTN_CHECK, srSyoseiData), 9));  //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
        if (isInsert) {
            params.add(0); //段数
            params.add(0); //外観確認
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.KAISHU_SETTA_MAISU, srSyoseiData)));  //回収ｾｯﾀ枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.KAISHI_KAKUNINSYA, srSyoseiData)));  //開始確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_GOUKI, srSyoseiData)));  //2次脱脂号機
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_SETTEI_PATTERN, srSyoseiData), 9));  //2次脱脂設定ﾊﾟﾀｰﾝ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_KEEPONDO, srSyoseiData)));  //2次脱脂ｷｰﾌﾟ温度
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.NIJIDASSHI_CONVEYER_SPEED, srSyoseiData)));  //2次脱脂ｺﾝﾍﾞｱ速度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.SYOSEI_PEAK_ONDO_SIJI, srSyoseiData)));  //焼成ﾋﾟｰｸ温度指示
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.SYOSEI_ROLLER_SPEED, srSyoseiData)));  //焼成ﾛｰﾗｰ速度
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.SYOSEI_PURGE, srSyoseiData)));  //焼成ﾊﾟｰｼﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_GOUKI1, srSyoseiData)));  //1回目再酸化号機1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_GOUKI2, srSyoseiData)));  //1回目再酸化号機2
        params.add(getCheckBoxDbValue(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_SETTEI_PTN, srSyoseiData), 9));  //1回目再酸化設定ﾊﾟﾀｰﾝ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_KEEP_ONDO, srSyoseiData)));  //1回目再酸化ｷｰﾌﾟ温度
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_CONVEYER_SPEED, srSyoseiData)));  //1回目再酸化ｺﾝﾍﾞｱ速度
        params.add(getComboValue(getItemData(itemList, GXHDO101B018Const.FIRST_SAISANKA_ATO_GAIKAN, srSyoseiData), 9));//1回目再酸化後外観
        params.add("RHK焼成"); //焼成種類
        params.add(newRev); //revision
        return params;
    }

    /**
     * 焼成(sr_syosei)削除処理
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
    private void deleteSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {

        String sql = "DELETE FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ?  AND jissekino = ? AND revision = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * [焼成_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外エラー
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, int jissekino) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(jissekino);

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
    public ProcessData setStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B018Const.KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B018Const.KAISHI_TIME);
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
    public ProcessData setEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B018Const.SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B018Const.SHURYOU_TIME);
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
     * @param srSyoseiData 焼成データ
     * @return DB値
     */
    private String getSrSyoseiItemData(String itemId, SrSyosei srSyoseiData) {
        switch (itemId) {
            //KCPNO
            case GXHDO101B018Const.KCPNO:
                return StringUtil.nullToBlank(srSyoseiData.getKcpno());
            //受入ｾｯﾀ枚数
            case GXHDO101B018Const.UKEIRE_SETTA_MAISU:
                return StringUtil.nullToBlank(srSyoseiData.getSsettermaisuu());
            //開始日
            case GXHDO101B018Const.KAISHI_DAY:
                return DateUtil.formattedTimestamp(srSyoseiData.getSkaisinichiji(), "yyMMdd");
            //開始時刻
            case GXHDO101B018Const.KAISHI_TIME:
                return DateUtil.formattedTimestamp(srSyoseiData.getSkaisinichiji(), "HHmm");
            //開始担当者
            case GXHDO101B018Const.KAISHI_TANTOUSYA:
                return StringUtil.nullToBlank(srSyoseiData.getSkaisitantosya());
            //開始確認者
            case GXHDO101B018Const.KAISHI_KAKUNINSYA:
                return StringUtil.nullToBlank(srSyoseiData.getStartkakuninsyacode());
            //投入ｾｯﾀ枚数
            case GXHDO101B018Const.TOUNYU_SETTA_MAISU:
                return StringUtil.nullToBlank(srSyoseiData.getTounyusettasuu());
            //2次脱脂号機							
            case GXHDO101B018Const.NIJIDASSHI_GOUKI:
                return StringUtil.nullToBlank(srSyoseiData.getNijidasshigouki());
            //2次脱脂設定ﾊﾟﾀｰﾝ							
            case GXHDO101B018Const.NIJIDASSHI_SETTEI_PATTERN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSyoseiData.getNijidasshisetteipt()));
            //2次脱脂ｷｰﾌﾟ温度							
            case GXHDO101B018Const.NIJIDASSHI_KEEPONDO:
                return StringUtil.nullToBlank(srSyoseiData.getNijidasshikeepondo());
            //2次脱脂ｺﾝﾍﾞｱ速度							
            case GXHDO101B018Const.NIJIDASSHI_CONVEYER_SPEED:
                return StringUtil.nullToBlank(srSyoseiData.getNijidasshispeed());
            //焼成号機
            case GXHDO101B018Const.SYOSEI_GOUKI:
                return StringUtil.nullToBlank(srSyoseiData.getGoki());
            //焼成設定ﾊﾟﾀｰﾝﾁｪｯｸ
            case GXHDO101B018Const.SYOSEI_SETTEI_PTN_CHECK:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSyoseiData.getSetteipattern()));
            //焼成ﾋﾟｰｸ温度
            case GXHDO101B018Const.SYOSEI_PEAK_ONDO_SIJI:
                return StringUtil.nullToBlank(srSyoseiData.getPeakondo());
            //焼成ﾋﾟｰｸ温度設定値
            case GXHDO101B018Const.SYOSEI_PEAK_ONDO_SETTEICHI:
                return StringUtil.nullToBlank(srSyoseiData.getSyoseiondo());
            //焼成ﾛｰﾗｰ速度							
            case GXHDO101B018Const.SYOSEI_ROLLER_SPEED:
                return StringUtil.nullToBlank(srSyoseiData.getSyoseispeed());
            //焼成ﾊﾟｰｼﾞ							
            case GXHDO101B018Const.SYOSEI_PURGE:
                return StringUtil.nullToBlank(srSyoseiData.getSyoseipurge());
            //1回目再酸化号機1
            case GXHDO101B018Const.FIRST_SAISANKA_GOUKI1:
                return StringUtil.nullToBlank(srSyoseiData.getSaisankagouki1());
            //1回目再酸化号機2
            case GXHDO101B018Const.FIRST_SAISANKA_GOUKI2:
                return StringUtil.nullToBlank(srSyoseiData.getSaisankagouki2());
            //1回目再酸化設定ﾊﾟﾀｰﾝ
            case GXHDO101B018Const.FIRST_SAISANKA_SETTEI_PTN:
                return getCheckBoxCheckValue(StringUtil.nullToBlank(srSyoseiData.getSaisankasetteipt()));
            //1回目再酸化ｷｰﾌﾟ温度
            case GXHDO101B018Const.FIRST_SAISANKA_KEEP_ONDO:
                return StringUtil.nullToBlank(srSyoseiData.getSaisankakeepondo());
            //1回目再酸化ｺﾝﾍﾞｱ速度
            case GXHDO101B018Const.FIRST_SAISANKA_CONVEYER_SPEED:
                return StringUtil.nullToBlank(srSyoseiData.getSaisankacsokudo());
            //1回目再酸化後外観
            case GXHDO101B018Const.FIRST_SAISANKA_ATO_GAIKAN:
                return getComboText(StringUtil.nullToBlank(srSyoseiData.getSaisankagogaikan()));
            //終了日
            case GXHDO101B018Const.SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srSyoseiData.getSsyuryonichiji(), "yyMMdd");
            //終了時刻
            case GXHDO101B018Const.SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srSyoseiData.getSsyuryonichiji(), "HHmm");
            //終了担当者
            case GXHDO101B018Const.SHURYOU_TANTOUSYA:
                return StringUtil.nullToBlank(srSyoseiData.getSsyuryotantosya());
            //回収ｾｯﾀ枚数
            case GXHDO101B018Const.KAISHU_SETTA_MAISU:
                return StringUtil.nullToBlank(srSyoseiData.getKaishusettasuu());
            //備考1
            case GXHDO101B018Const.BIKOU1:
                return StringUtil.nullToBlank(srSyoseiData.getBiko1());
            //備考2
            case GXHDO101B018Const.BIKOU2:
                return StringUtil.nullToBlank(srSyoseiData.getBiko2());

            default:
                return null;
        }
    }

    /**
     * 焼成_仮登録(tmp_sr_syosei)登録処理(削除時)
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jissekino 実績No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertDeleteDataTmpSrSyosei(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, int jissekino, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_syosei ("
                + "kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,"
                + "bkaisinichiji,bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,torokunichiji,kosinnichiji,"
                + "SankaGoki,SankaOndo,SankaSyuryoNichiJi,tounyusettasuu,setteipattern,dansuu,gaikancheck,"
                + "kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,"
                + "Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,"
                + "saisankaCsokudo,saisankagogaikan,syoseisyurui,revision,deleteflag "
                + ") SELECT "
                + "kojyo,lotno,edaban,jissekino,kcpno,kosuu,genryohinsyumei,genryogroup,skaisinichiji,ssyuryonichiji,bprogramno,"
                + "syoseiondo,goki,ssettermaisuu,nyurodaibanmaisuu,skaisitantosya,ssyuryotantosya,biko1,biko2,biko3,biko4,biko5,"
                + "bkaisinichiji,bsyuryonichiji,bsettermaisuu,potsuu,potno,btantosya,biko6,biko7,?,?,"
                + "SankaGoki,SankaOndo,SankaSyuryoNichiJi,tounyusettasuu,setteipattern,dansuu,gaikancheck,"
                + "kaishusettasuu,StartKakuninsyacode,nijidasshigouki,NijidasshisetteiPT,Nijidasshikeepondo,"
                + "Nijidasshispeed,peakondo,syoseispeed,syoseipurge,saisankagouki1,saisankagouki2,saisankasetteiPT,saisankakeepondo,"
                + "saisankaCsokudo,saisankagogaikan,syoseisyurui,?,? "
                + "FROM sr_syosei "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND jissekino = ? ";

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

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * コンボボックス(NG,OK)Value値取得
     *
     * @param comboText コンボボックステキスト
     * @return コンボボックスValue値
     */
    private Integer getComboValue(String comboText, Integer defaultValue) {
        switch (StringUtil.nullToBlank(comboText)) {
            case "NG":
                return 0;
            case "OK":
                return 1;
            default:
                return defaultValue;
        }
    }

    /**
     * コンボボックス(NG,OK)テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboText(String comboValue) {
        switch (comboValue) {
            case "0":
                return "NG";
            case "1":
                return "OK";
            default:
                return "";
        }
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
    
    /**
     * 項目の未入力判定
     * @param itemRow 項目データ
     * @return 未入力時にtrueを返却
     */
    private boolean isNonInputItem(FXHDD01 itemRow){
        if (itemRow.isRenderInputNumber()) {
            // 数値項目の場合、空またはZEROの場合未入力とする。
            if(StringUtil.isEmpty(itemRow.getValue()) || NumberUtil.isZero(itemRow.getValue())){
                return true;
            }
        } else if (itemRow.isRenderInputCheckBox()) {
            // チェックボックスの場合、"true"以外未入力とする。
            if(!"true".equals(itemRow.getValue())){
                return true;
            }
        } else {
            // その他、空の場合未入力とする。
            if(StringUtil.isEmpty(itemRow.getValue())){
                return true;
            }
        }
        return false;
    }

}
