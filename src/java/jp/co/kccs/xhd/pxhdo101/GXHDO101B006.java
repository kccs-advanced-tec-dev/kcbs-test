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
import jp.co.kccs.xhd.db.model.SrRhaps;
import jp.co.kccs.xhd.db.model.SubSrRhaps;
import jp.co.kccs.xhd.model.GXHDO101C007Model;
import jp.co.kccs.xhd.model.GXHDO101C008Model;
import jp.co.kccs.xhd.model.GXHDO101C009Model;
import jp.co.kccs.xhd.model.GXHDO101C010Model;
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
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;

/**
 * ===============================================================================<br>
 * <br>
 * システム名	品質DB(コンデンサ)<br>
 * <br>
 * 変更日	2019/03/06<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * 変更日	2019/09/20<br>
 * 計画書No	K1811-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	項目追加・変更<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B006(印刷積層(RHAPS))ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2019/03/06
 */
public class GXHDO101B006 implements IFormLogic {

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

            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_TOP,
                    GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_BOTTOM,
                    GXHDO101B006Const.BTN_PTN_KAN_KYORI_TOP,
                    GXHDO101B006Const.BTN_PTN_KAN_KYORI_BOTTOM,
                    GXHDO101B006Const.BTN_AWASE_RZ_TOP,
                    GXHDO101B006Const.BTN_AWASE_RZ_BOTTOM,
                    GXHDO101B006Const.BTN_KABURIRYOU_TOP,
                    GXHDO101B006Const.BTN_KABURIRYOU_BOTTOM,
                    GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_TOP,
                    GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_BOTTOM,
                    GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_TOP,
                    GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_BOTTOM,
                    GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_TOP,
                    GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_BOTTOM
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B006Const.BTN_KARI_TOUROKU_TOP,
                    GXHDO101B006Const.BTN_KARI_TOUROKU_BOTTOM,
                    GXHDO101B006Const.BTN_INSERT_TOP,
                    GXHDO101B006Const.BTN_INSERT_BOTTOM,
                    GXHDO101B006Const.BTN_DELETE_TOP,
                    GXHDO101B006Const.BTN_DELETE_BOTTOM,
                    GXHDO101B006Const.BTN_UPDATE_TOP,
                    GXHDO101B006Const.BTN_UPDATE_BOTTOM));

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
     * 電極膜厚(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openDenkyokuMakuatsu(ProcessData processData) {

        try {
            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c007");

            // 電極膜厚の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C007 beanGXHDO101C007 = (GXHDO101C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C007);
            beanGXHDO101C007.setGxhdO101c007ModelView(beanGXHDO101C007.getGxhdO101c007Model().clone());

        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
        return processData;
    }

    /**
     * ﾊﾟﾀｰﾝ間距離(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPatternKanKyori(ProcessData processData) {
        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c008");

            // パターン間距離の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C008 beanGXHDO101C008 = (GXHDO101C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C008);
            beanGXHDO101C008.setGxhdO101c008ModelView(beanGXHDO101C008.getGxhdO101c008Model().clone());

        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }
        return processData;
    }

    /**
     * 合わせ(RZ)(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openAwaseRz(ProcessData processData) {
        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c009");
            // 合わせ(RZ)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C009 beanGXHDO101C009 = (GXHDO101C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C009);
            beanGXHDO101C009.setGxhdO101c009ModelView(beanGXHDO101C009.getGxhdO101c009Model().clone());

        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;
        }

        return processData;
    }

    /**
     * 被り量(µm)(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openKaburiryo(ProcessData processData) {
        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c010");

            // 被り量(µm)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C010 beanGXHDO101C010 = (GXHDO101C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C010);
            beanGXHDO101C010.setGxhdO101c010ModelView(beanGXHDO101C010.getGxhdO101c010Model().clone());

        } catch (CloneNotSupportedException ex) {
            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
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

        // 入力共通チェック
        ErrorMessageInfo errorMessageInfo = inputCommonCheck(processData);
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

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

                // 印刷積層RHAPS_仮登録登録処理
                insertTmpSrRhaps(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷積層RHAPS_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrRhaps(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 印刷積層RHAPS_仮登録更新処理
                updateTmpSrRhaps(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷積層RHAPS_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrRhaps(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
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

            // サブ画面背景色クリア
            ClearSubFormBackColor();

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
     * サブ画面背景色クリア
     */
    private void ClearSubFormBackColor() {

        // 電極膜厚入力画面背景色クリア
        GXHDO101C007 beanGXHDO101C007 = (GXHDO101C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C007);
        for (GXHDO101C007Model.DenkyokuMakuatsuData data : beanGXHDO101C007.getGxhdO101c007Model().getDenkyokuMakuatsuDataList()) {
            data.setTextBackColor("");
        }

        // ﾊﾟﾀｰﾝ間距離画面背景色クリア
        GXHDO101C008 beanGXHDO101C008 = (GXHDO101C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C008);
        for (GXHDO101C008Model.PtnKanKyoriData data : beanGXHDO101C008.getGxhdO101c008Model().getPtnKanKyoriDataList()) {
            data.setTextBackColor("");
        }

        // 合わせ(RZ)画面背景色クリア
        GXHDO101C009 beanGXHDO101C009 = (GXHDO101C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C009);
        for (GXHDO101C009Model.AwaseRzData data : beanGXHDO101C009.getGxhdO101c009Model().getAwaseRzDataList()) {
            data.setTextBackColor("");
        }

        // 被り量(μm)画面背景色クリア
        GXHDO101C010 beanGXHDO101C010 = (GXHDO101C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C010);
        GXHDO101C010Model gxhdO101c010Model = beanGXHDO101C010.getGxhdO101c010Model();
        gxhdO101c010Model.getKaburiryouData1().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData2().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData3().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData4().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData5().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData6().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData7().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData8().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData9().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData10().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData11().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData12().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData13().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData14().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData15().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData16().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData17().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData18().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData19().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
        gxhdO101c010Model.getKaburiryouData20().setTextBackColor(GXHDO101C010Logic.DEFAULT_BACK_COLOR);
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

        // サブ画面の入力チェックを行う。
        List<String> errorListSubForm;
        // 電極膜厚画面チェック
        errorListSubForm = checkSubFormDenkyokuMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openDenkyokuMakuatsu");
            return processData;
        }

        // ﾊﾟﾀｰﾝ間距離画面チェック
        errorListSubForm = checkSubFormPtnKanKyori();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPatternKanKyori");
            return processData;
        }

        // 合わせ(RZ)画面チェック
        errorListSubForm = checkSubFormAwaseRz();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openAwaseRz");
            return processData;
        }

        // 被り量(µm)画面チェック
        errorListSubForm = checkSubFormKaburiryou();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openKaburiryo");
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

        // 入力共通チェック
        ErrorMessageInfo errorMessageInfo = inputCommonCheck(processData);
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ターゲットあり・なし関連チェック(あり選択時チェック)
        errorMessageInfo = checkHasTarget(processData);
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }
        
        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 insatsuSekisouKaishiDay = getItemRow(processData.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY); //印刷積層開始日
        FXHDD01 insatsuSekisouKaishiTime = getItemRow(processData.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME); // 印刷積層開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(insatsuSekisouKaishiDay.getValue(), insatsuSekisouKaishiTime.getValue());
        FXHDD01 insatsuSekisouShuryouDay = getItemRow(processData.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY); //印刷積層終了日
        FXHDD01 itemInsatsuSekisouShuryouTime = getItemRow(processData.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME); //印刷積層終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(insatsuSekisouShuryouDay.getValue(), itemInsatsuSekisouShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(insatsuSekisouKaishiDay.getLabel1(), kaishiDate, insatsuSekisouShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(insatsuSekisouKaishiDay, insatsuSekisouKaishiTime, insatsuSekisouShuryouDay, itemInsatsuSekisouShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        return null;
    }

    /**
     * 入力共通チェック
     *
     * @param processData 処理データ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo inputCommonCheck(ProcessData processData) {

        ErrorMessageInfo errorMessageInfo;

        // 外観確認
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.GAIKAN_KAKUNIN1));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // テープ搬送目視確認
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.TAPE_HANSOU_MOKUSHI_KAKUNIN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 印刷サンプル外観確認
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.INSATSU_SAMPLE_GAIKAN_KAKUNIN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 印刷位置余白長さ
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.INSATSU_ICHI_YOHAKU_NAGASA));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 被り量測定
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.KABURIRYOU_SOKUTEI));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 積層中外観
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.SEKISOUTYUU_GAIKAN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 積層品外観
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.SEKISOUHIN_GAIKAN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // 積層ズレチェック
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.SEKISOU_ZURE_CHECK));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ターゲット印刷外観
        errorMessageInfo = checkComboBoxSelectNG(getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_GAIKAN));
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        // ターゲットあり・なし関連チェック
        errorMessageInfo = checkNoTarget(processData);
        if (errorMessageInfo != null) {
            return errorMessageInfo;
        }

        return null;
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
     * ターゲットありの場合のチェック処理
     *
     * @param itemData 項目データデータ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkHasTarget(ProcessData processData) {
        // ターゲットあり・なし取得
        FXHDD01 checkItemData = getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_UMU);

        // "あり"が選択されていない場合リターン
        if (!"あり".equals(StringUtil.nullToBlank(checkItemData.getValue()))) {
            return null;
        }

        List<FXHDD01> errorItemList = checkItemsEmpty(
                Arrays.asList(
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INK_SHURUI),//ﾀｰｹﾞｯﾄｲﾝｸ種類
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INK_LOT),//ﾀｰｹﾞｯﾄｲﾝｸﾛｯﾄ
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_CLEARANCE),// ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED),//印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SAATSU),//ﾀｰｹﾞｯﾄ印刷差圧
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_GAIKAN),//ﾀｰｹﾞｯﾄ印刷外観
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY),//ﾀｰｹﾞｯﾄ印刷終了日
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA)//ﾀｰｹﾞｯﾄ印刷担当者
                ), true);

        if (!errorItemList.isEmpty()) {
            return MessageUtil.getErrorMessageInfo("XHD-000103", true, true, errorItemList, checkItemData.getLabel1());
        }
        return null;
    }

    /**
     * ターゲットなしの場合のチェック処理
     *
     * @param itemData 項目データデータ
     * @return エラーメッセージ情報
     */
    private ErrorMessageInfo checkNoTarget(ProcessData processData) {
        // ターゲットあり・なし取得
        FXHDD01 checkItemData = getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_UMU);

        // "なし"が選択されていない場合リターン
        if (!"なし".equals(StringUtil.nullToBlank(checkItemData.getValue()))) {
            return null;
        }

        List<FXHDD01> errorItemList = checkItemsEmpty(
                Arrays.asList(
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INK_SHURUI),//ﾀｰｹﾞｯﾄｲﾝｸ種類
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INK_LOT),//ﾀｰｹﾞｯﾄｲﾝｸﾛｯﾄ
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_CLEARANCE),// ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED),//印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SAATSU),//ﾀｰｹﾞｯﾄ印刷差圧
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_GAIKAN),//ﾀｰｹﾞｯﾄ印刷外観
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY),//ﾀｰｹﾞｯﾄ印刷終了日
                        getItemRow(processData.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA)//ﾀｰｹﾞｯﾄ印刷担当者
                ), false);

        if (!errorItemList.isEmpty()) {
            return MessageUtil.getErrorMessageInfo("XHD-000102", true, true, errorItemList, checkItemData.getLabel1());
        }
        return null;
    }

    /**
     * 項目の空白チェック(空白があればエラーか、なければエラーかは引数にて判定)
     *
     * @param itemList 項目リスト
     * @param errorCondition エラー条件(true:空白があればエラー、false:空白以外があればエラー)
     * @return エラー項目リスト
     */
    private List<FXHDD01> checkItemsEmpty(List<FXHDD01> itemList, boolean errorCondition) {
        List<FXHDD01> errorItemList = new ArrayList<>();
        for (FXHDD01 fxhdd01 : itemList) {
            if (StringUtil.isEmpty(fxhdd01.getValue()) == errorCondition) {
                errorItemList.add(fxhdd01);
            }
        }
        return errorItemList;
    }


     /**
     * サブ画面(電極膜厚)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormDenkyokuMakuatsu() {
        GXHDO101C007 beanGXHDO101C007 = (GXHDO101C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C007);
        return GXHDO101C007Logic.checkInput(beanGXHDO101C007.getGxhdO101c007Model());
    }

    /**
     * サブ画面(ﾊﾟﾀｰﾝ間距離)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormPtnKanKyori() {
        GXHDO101C008 beanGXHDO101C008 = (GXHDO101C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C008);
        return GXHDO101C008Logic.checkInput(beanGXHDO101C008.getGxhdO101c008Model());
    }

    /**
     * サブ画面(合わせ(RZ))チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormAwaseRz() {
        GXHDO101C009 beanGXHDO101C009 = (GXHDO101C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C009);
        return GXHDO101C009Logic.checkInput(beanGXHDO101C009.getGxhdO101c009Model());
    }

    /**
     * サブ画面(被り量(µm))チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormKaburiryou() {
        GXHDO101C010 beanGXHDO101C010 = (GXHDO101C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C010);
        return GXHDO101C010Logic.checkInput(beanGXHDO101C010.getGxhdO101c010Model());
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
            SrRhaps tmpSrRhaps = null;
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {

                // 更新前の値を取得
                List<SrRhaps> srRhapsList = getSrRhapsData(queryRunnerQcdb, rev.toPlainString(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);
                if (!srRhapsList.isEmpty()) {
                    tmpSrRhaps = srRhapsList.get(0);
                }

                deleteTmpSrRhaps(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrRhaps(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 印刷積層RHAPS_登録処理
            insertSrRhaps(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrRhaps);

            // 印刷積層RHAPS_ｻﾌﾞ画面登録処理
            insertSubSrRhaps(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

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

        // サブ画面の入力チェックを行う。
        List<String> errorListSubForm;
        // 電極膜厚画面チェック
        errorListSubForm = checkSubFormDenkyokuMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openDenkyokuMakuatsu");
            return processData;
        }

        // ﾊﾟﾀｰﾝ間距離画面チェック
        errorListSubForm = checkSubFormPtnKanKyori();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPatternKanKyori");
            return processData;
        }

        // 合わせ(RZ)画面チェック
        errorListSubForm = checkSubFormAwaseRz();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openAwaseRz");
            return processData;
        }

        // 被り量(µm)画面チェック
        errorListSubForm = checkSubFormKaburiryou();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openKaburiryo");
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
        processData.setUserAuthParam(GXHDO101B006Const.USER_AUTH_UPDATE_PARAM);

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

            // 印刷積層RHAPS_更新処理
            updateSrRhaps(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷積層RHAPS_ｻﾌﾞ画面更新処理
            updateSubSrRhaps(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

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
        processData.setUserAuthParam(GXHDO101B006Const.USER_AUTH_DELETE_PARAM);

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

            // 印刷積層RHAPS_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertDeleteDataTmpSrRhaps(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷積層RHAPS_ｻﾌﾞ画面仮登録登録処理
            insertDeleteDataTmpSubSrRhaps(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

            // 印刷積層RHAPS_削除処理
            deleteSrRhaps(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷積層RHAPS_ｻﾌﾞ画面削除処理
            deleteSubSrRhaps(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

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
                activeIdList.addAll(Arrays.asList(GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_TOP,
                        GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_BOTTOM,
                        GXHDO101B006Const.BTN_PTN_KAN_KYORI_TOP,
                        GXHDO101B006Const.BTN_PTN_KAN_KYORI_BOTTOM,
                        GXHDO101B006Const.BTN_AWASE_RZ_TOP,
                        GXHDO101B006Const.BTN_AWASE_RZ_BOTTOM,
                        GXHDO101B006Const.BTN_KABURIRYOU_TOP,
                        GXHDO101B006Const.BTN_KABURIRYOU_BOTTOM,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_TOP,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_BOTTOM,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_TOP,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_BOTTOM,
                        GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_TOP,
                        GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_BOTTOM,
                        GXHDO101B006Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B006Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B006Const.BTN_UPDATE_TOP,
                        GXHDO101B006Const.BTN_UPDATE_BOTTOM,
                        GXHDO101B006Const.BTN_DELETE_TOP,
                        GXHDO101B006Const.BTN_DELETE_BOTTOM
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B006Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B006Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B006Const.BTN_INSERT_BOTTOM,
                        GXHDO101B006Const.BTN_INSERT_TOP));

                break;
            default:
                activeIdList.addAll(Arrays.asList(GXHDO101B006Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_TOP,
                        GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_BOTTOM,
                        GXHDO101B006Const.BTN_PTN_KAN_KYORI_TOP,
                        GXHDO101B006Const.BTN_PTN_KAN_KYORI_BOTTOM,
                        GXHDO101B006Const.BTN_AWASE_RZ_TOP,
                        GXHDO101B006Const.BTN_AWASE_RZ_BOTTOM,
                        GXHDO101B006Const.BTN_KABURIRYOU_TOP,
                        GXHDO101B006Const.BTN_KABURIRYOU_BOTTOM,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_TOP,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_BOTTOM,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_TOP,
                        GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_BOTTOM,
                        GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_TOP,
                        GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_BOTTOM,
                        GXHDO101B006Const.BTN_COPY_EDABAN_TOP,
                        GXHDO101B006Const.BTN_COPY_EDABAN_BOTTOM,
                        GXHDO101B006Const.BTN_KARI_TOUROKU_TOP,
                        GXHDO101B006Const.BTN_KARI_TOUROKU_BOTTOM,
                        GXHDO101B006Const.BTN_INSERT_TOP,
                        GXHDO101B006Const.BTN_INSERT_BOTTOM
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B006Const.BTN_DELETE_TOP,
                        GXHDO101B006Const.BTN_DELETE_BOTTOM,
                        GXHDO101B006Const.BTN_UPDATE_TOP,
                        GXHDO101B006Const.BTN_UPDATE_BOTTOM
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
            // 電極膜圧
            case GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_TOP:
            case GXHDO101B006Const.BTN_DENKYOKU_MAKUATSU_BOTTOM:
                method = "openDenkyokuMakuatsu";
                break;
            // ﾊﾟﾀｰﾝ間距離
            case GXHDO101B006Const.BTN_PTN_KAN_KYORI_TOP:
            case GXHDO101B006Const.BTN_PTN_KAN_KYORI_BOTTOM:
                method = "openPatternKanKyori";
                break;
            // 合わせ(RZ)
            case GXHDO101B006Const.BTN_AWASE_RZ_TOP:
            case GXHDO101B006Const.BTN_AWASE_RZ_BOTTOM:
                method = "openAwaseRz";
                break;
            // 被り量(µm)
            case GXHDO101B006Const.BTN_KABURIRYOU_TOP:
            case GXHDO101B006Const.BTN_KABURIRYOU_BOTTOM:
                method = "openKaburiryo";
                break;
            // 仮登録
            case GXHDO101B006Const.BTN_KARI_TOUROKU_TOP:
            case GXHDO101B006Const.BTN_KARI_TOUROKU_BOTTOM:
                method = "checkDataTempRegist";
                break;
            // 登録
            case GXHDO101B006Const.BTN_INSERT_TOP:
            case GXHDO101B006Const.BTN_INSERT_BOTTOM:
                method = "checkDataRegist";
                break;
            // 枝番コピー
            case GXHDO101B006Const.BTN_COPY_EDABAN_TOP:
            case GXHDO101B006Const.BTN_COPY_EDABAN_BOTTOM:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B006Const.BTN_UPDATE_TOP:
            case GXHDO101B006Const.BTN_UPDATE_BOTTOM:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B006Const.BTN_DELETE_TOP:
            case GXHDO101B006Const.BTN_DELETE_BOTTOM:
                method = "checkDataDelete";
                break;
            // 印刷積層開始日時
            case GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_TOP:
            case GXHDO101B006Const.BTN_INSATSU_SEKISOU_STARTDATETIME_BOTTOM:
                method = "setInsatsuSekisouStartDateTime";
                break;
            // 印刷積層終了日時
            case GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_TOP:
            case GXHDO101B006Const.BTN_INSATSU_SEKISOU_ENDDATETIME_BOTTOM:
                method = "setInsatsuSekisouEndDateTime";
                break;
            // ターゲット印刷終了日
            case GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_TOP:
            case GXHDO101B006Const.BTN_TARGET_INSATSU_ENDDAY_BOTTOM:
                method = "setTargetInsatsuEndDateTime";
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
    private ProcessData setInitDate(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));

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

        //上ｶﾊﾞｰﾃｰﾌﾟ1
        String ueCoverTape1 = "";
        //上ｶﾊﾞｰﾃｰﾌﾟ2
        String ueCoverTape2 = "";
        //下ｶﾊﾞｰﾃｰﾌﾟ1
        String shitaCoverTape1 = "";
        //下ｶﾊﾞｰﾃｰﾌﾟ2
        String shitaCoverTape2 = "";
        
        //上ｶﾊﾞｰﾃｰﾌﾟ1対象項目の決定
        List<String> checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"CT",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    ueCoverTape1 = checkListDataVal.get(i);
                }
            }
        }
        
        //上ｶﾊﾞｰﾃｰﾌﾟ2対象項目の決定
        checkListDataVal.clear();
        checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"ST",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    ueCoverTape2 = checkListDataVal.get(i);
                }
            }
        }

        //下ｶﾊﾞｰﾃｰﾌﾟ1対象項目の決定
        checkListDataVal.clear();
        checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"CB",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    shitaCoverTape1 = checkListDataVal.get(i);
                }
            }
        }

        //下ｶﾊﾞｰﾃｰﾌﾟ2対象項目の決定
        checkListDataVal.clear();
        checkListDataVal = checkYoutoItems(processData,sekkeiData, getMapYoutoAssociation(),"SB",getMapSekkeiYotoAssociation());
        for(int i=0; i<=checkListDataVal.size()-1; i++){
            if(i > 0){
                if("ERROR".equals(checkListDataVal.get(0))){
                    errorMessageList.add(checkListDataVal.get(i));
                }else if("OK".equals(checkListDataVal.get(0))){
                    shitaCoverTape2 = checkListDataVal.get(i);
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
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId, StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")))) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList(MessageUtil.getMessage("XHD-000038")));
            return processData;
        }
        
        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo,
                ueCoverTape1, ueCoverTape2, shitaCoverTape1, shitaCoverTape2);

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
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     * @param ueCoverTape1 上カバーテープ1
     * @param ueCoverTape2 上カバーテープ2
     * @param shitaCoverTape1 下カバーテープ1
     * @param shitaCoverTape2 下カバーテープ2
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData, Map shikakariData, String lotNo,
            String ueCoverTape1, String ueCoverTape2, String shitaCoverTape1, String shitaCoverTape2) {

        // ロットNo
        this.setItemData(processData, GXHDO101B006Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B006Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B006Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B006Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B006Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B006Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B006Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B006Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "ownername"));
            this.setItemData(processData, GXHDO101B006Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // カバーテープ仕様
        this.setItemData(processData, GXHDO101B006Const.COVER_TAPE_SHIYOU, "");

        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B006Const.UE_COVER_TAPE1, ueCoverTape1);

        // 上カバーテープ2
        this.setItemData(processData, GXHDO101B006Const.UE_COVER_TAPE2, ueCoverTape2);

        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B006Const.SHITA_COVER_TAPE1, shitaCoverTape1);

        // 下カバーテープ2 
        this.setItemData(processData, GXHDO101B006Const.SHITA_COVER_TAPE2, shitaCoverTape2);

        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
        this.setItemData(processData, GXHDO101B006Const.RETSU_GYOU, lRetsu + "×" + wRetsu);

        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B006Const.PITCH, lSun + "×" + wSun);

        // 電極ペースト
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_PASTE, "");

        // 電極製版名
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

        // 電極製版仕様
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_SHIYOU, "");

        // 電極誘電体ペースト
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE, "");

        // 誘電体製版名
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

        // 誘電体製版仕様
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_SIYOU, "");

        // 積層スライド量
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_SLIDE_RYOU, "");

        // 電極スライド量
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, "");

        // 誘電体スライド量
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU, "");

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param queryRunnerDoc QueryRunnerオブジェクト(DocServer)
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param lotNo ﾛｯﾄNo
     * @param formId 画面ID
     * @param sekkeino 設計No
     * @return 設定結果(失敗時false)
     * @throws SQLException 例外エラー
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb,
            String lotNo, String formId, String sekkeino) throws SQLException {

        List<SrRhaps> srRhapsDataList = new ArrayList<>();
        List<SubSrRhaps> subSrRhapsDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String maeGamenID = StringUtil.nullToBlank(session.getAttribute("maeGamenID"));
        String motoLotNo = (String) session.getAttribute("sanshouMotoLotNo");// 参照元ﾃﾞｰﾀﾛｯﾄNo

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId);
            rev = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev"));
            jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            // revisionが空のまたはjotaiFlgが"0"でも"1"でもない場合、新規としてデフォルト値を設定してリターンする。
            if (StringUtil.isEmpty(rev) || !(JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg))) {
                processData.setInitRev(rev);
                processData.setInitJotaiFlg(jotaiFlg);

                // ﾛｯﾄ参照画面より遷移してきた場合
                if ("GXHDO101C012".equals(maeGamenID)) {
                    // 参照元ﾛｯﾄのデータをセットする。
                    if (setSanshouMotoLotData(processData, queryRunnerQcdb, queryRunnerDoc, formId, motoLotNo, kojyo, lotNo8, edaban, sekkeino)) {
                        //ｾｯﾄ成功時はリターン
                        return true;
                    }
                }
                
                // メイン画面にデータを設定する(デフォルト値)
                for (FXHDD01 fxhdd001 : processData.getItemList()) {
                    this.setItemData(processData, fxhdd001.getItemId(), fxhdd001.getInputDefault());
                }

                // サブ画面データ設定
                // 電極膜厚画面データ設定
                setInputItemDataSubFormC007(null, kojyo, lotNo8, edaban);

                // ﾊﾟﾀｰﾝ間距離画面データ設定
                setInputItemDataSubFormC008(null, kojyo, lotNo8, edaban);

                // 合わせ(RZ)画面データ設定
                setInputItemDataSubFormC009(null, kojyo, lotNo8, edaban);

                // 被り量(µm)(データ設定
                setInputItemDataSubFormC010(null, kojyo, lotNo8, edaban, sekkeino, queryRunnerQcdb);

                return true;
            }

            // 印刷積層RHAPSデータ取得
            srRhapsDataList = getSrRhapsData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srRhapsDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 印刷積層RHAPS_ｻﾌﾞ画面データ取得
            subSrRhapsDataList = getSubSrRhapsData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (subSrRhapsDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srRhapsDataList.isEmpty() || subSrRhapsDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srRhapsDataList.get(0));

        // 電極膜厚画面データ設定
        setInputItemDataSubFormC007(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

        // ﾊﾟﾀｰﾝ間距離画面データ設定
        setInputItemDataSubFormC008(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

        // 合わせ(RZ)画面データ設定
        setInputItemDataSubFormC009(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

        // 被り量(µm)画面データ設定
        setInputItemDataSubFormC010(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban, sekkeino, queryRunnerQcdb);

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srRhapsData 印刷積層RHAPSデータ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrRhaps srRhapsData) {

        //電極テープ種類
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_SHURUI, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_SHURUI, srRhapsData));
        //電極テープロット
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_LOT, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_LOT, srRhapsData));
        //電極テープスリップ記号
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_SLIP_KIGOU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_SLIP_KIGOU, srRhapsData));
        //電極テープロールNo 1本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO1, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO1, srRhapsData));
        //電極テープロールNo 2本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, srRhapsData));
        //電極テープロールNo 3本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, srRhapsData));
        //電極テープロールNo 4本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, srRhapsData));
        //電極テープロールNo 5本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, srRhapsData));
        //電極テープ原料lot
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT, srRhapsData));
        //端子テープロットNo
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_LOT_NO, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_LOT_NO, srRhapsData));
        //端子テープスリップ記号
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU, srRhapsData));
        //端子テープロールNo 1本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1, srRhapsData));
        //端子テープロールNo 2本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2, srRhapsData));
        //端子テープロールNo 3本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3, srRhapsData));
        //端子テープロールNo 4本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4, srRhapsData));
        //端子テープロールNo 5本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5, srRhapsData));
        //電極ペーストロットNo
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO, srRhapsData));
        //電極ペースト粘度
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_PASTE_NENDO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_PASTE_NENDO, srRhapsData));
        //電極ペースト温度
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_PASTE_ONDO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_PASTE_ONDO, srRhapsData));
        //電極誘電体ペースト名
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI, srRhapsData));
        //誘電体ペーストロットNo
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO, srRhapsData));
        //誘電体ペースト粘度
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_PASTE_NENDO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_PASTE_NENDO, srRhapsData));
        //誘電体ペースト温度
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_PASTE_ONDO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_PASTE_ONDO, srRhapsData));
        //ＰＥＴフィルム種類
        this.setItemData(processData, GXHDO101B006Const.PET_FILM_SHURUI, getSrRhapsItemData(GXHDO101B006Const.PET_FILM_SHURUI, srRhapsData));
        //瞬時加熱時間
        this.setItemData(processData, GXHDO101B006Const.SHUNJI_KANETSU_TIME, getSrRhapsItemData(GXHDO101B006Const.SHUNJI_KANETSU_TIME, srRhapsData));
        //加圧力
        this.setItemData(processData, GXHDO101B006Const.KAATSU_ATSURYOKU, getSrRhapsItemData(GXHDO101B006Const.KAATSU_ATSURYOKU, srRhapsData));
        //外観確認
        this.setItemData(processData, GXHDO101B006Const.GAIKAN_KAKUNIN1, getSrRhapsItemData(GXHDO101B006Const.GAIKAN_KAKUNIN1, srRhapsData));
        //号機
        this.setItemData(processData, GXHDO101B006Const.GOUKI, getSrRhapsItemData(GXHDO101B006Const.GOUKI, srRhapsData));
        //積層プレス通電時間（瞬時加熱）
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, getSrRhapsItemData(GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, srRhapsData));
        //瞬時加熱ヘッドNo
        this.setItemData(processData, GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO, getSrRhapsItemData(GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO, srRhapsData));
        //SUS板使用回数
        this.setItemData(processData, GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU, getSrRhapsItemData(GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU, srRhapsData));
        //積層加圧力
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, getSrRhapsItemData(GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, srRhapsData));
        //積層上昇速切替位置
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI, getSrRhapsItemData(GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI, srRhapsData));
        //積層下降速切替位置
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, getSrRhapsItemData(GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, srRhapsData));
        //加圧時間
        this.setItemData(processData, GXHDO101B006Const.KAATSU_TIME, getSrRhapsItemData(GXHDO101B006Const.KAATSU_TIME, srRhapsData));
        //テープ搬送送りピッチ
        this.setItemData(processData, GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH, getSrRhapsItemData(GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH, srRhapsData));
        //テープ搬送目視確認
        this.setItemData(processData, GXHDO101B006Const.TAPE_HANSOU_MOKUSHI_KAKUNIN, getSrRhapsItemData(GXHDO101B006Const.TAPE_HANSOU_MOKUSHI_KAKUNIN, srRhapsData));
        //電極製版No
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_NO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SEIHAN_NO, srRhapsData));
        //電極製版枚数
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, srRhapsData));
        //電極スキージNo
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SQUEEGEE_NO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SQUEEGEE_NO, srRhapsData));
        //電極スキージ枚数
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SQUEEGEE_MAISU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SQUEEGEE_MAISU, srRhapsData));
        //電極クリアランス
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_CLEARANCE, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_CLEARANCE, srRhapsData));
        //電極差圧
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SAATSU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SAATSU, srRhapsData));
        //電極膜厚設定
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, srRhapsData));
        //電極スキージスピード
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED, srRhapsData));
        //電極スクレッパクリアランス
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, srRhapsData));
        //電極スキージ下降待ち時間
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, srRhapsData));
        //電極熱風風量
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU, srRhapsData));
        //電極乾燥温度
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, srRhapsData));
        //電極膜厚AVE
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_MAKUATSU_AVE, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_MAKUATSU_AVE, srRhapsData));
        //電極膜厚MAX
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_MAKUATSU_MAX, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_MAKUATSU_MAX, srRhapsData));
        //電極膜厚MIN
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_MAKUATSU_MIN, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_MAKUATSU_MIN, srRhapsData));
        //電極L/Dスタート時
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_L_D_STARTJI, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_L_D_STARTJI, srRhapsData));
        //にじみ量測定(ﾊﾟﾀｰﾝ間距離)
        this.setItemData(processData, GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_PTNKANKYORI, getSrRhapsItemData(GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_PTNKANKYORI, srRhapsData));
        //印刷サンプル外観確認
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SAMPLE_GAIKAN_KAKUNIN, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SAMPLE_GAIKAN_KAKUNIN, srRhapsData));
        //印刷位置余白長さ
        this.setItemData(processData, GXHDO101B006Const.INSATSU_ICHI_YOHAKU_NAGASA, getSrRhapsItemData(GXHDO101B006Const.INSATSU_ICHI_YOHAKU_NAGASA, srRhapsData));
        //誘電体製版No
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, srRhapsData));
        //誘電体製版枚数
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, srRhapsData));
        //誘電体スキージNo
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_NO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SQUEEGEE_NO, srRhapsData));
        //誘電体スキージ枚数
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_MAISU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SQUEEGEE_MAISU, srRhapsData));
        //誘電体テーブルクリアランス
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, srRhapsData));
        //誘電体差圧
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SAATSU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SAATSU, srRhapsData));
        //誘電体膜厚設定
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI, srRhapsData));
        //誘電体スキージスピード
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, srRhapsData));
        //誘電体スクレッパクリアランス
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE, srRhapsData));
        //誘電体スキージ下降待ち時間
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, srRhapsData));
        //誘電体シフト印刷
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, srRhapsData));
        //誘電体熱風風量
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU, srRhapsData));
        //誘電体乾燥温度
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO, srRhapsData));
        //誘電体L/Dスタート時
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_L_D_STARTJI, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_L_D_STARTJI, srRhapsData));
        //合わせ印刷サイドRZAVE
        this.setItemData(processData, GXHDO101B006Const.AWASE_INSATSU_SIDE_RZ_AVE, getSrRhapsItemData(GXHDO101B006Const.AWASE_INSATSU_SIDE_RZ_AVE, srRhapsData));
        //被り量測定
        this.setItemData(processData, GXHDO101B006Const.KABURIRYOU_SOKUTEI, getSrRhapsItemData(GXHDO101B006Const.KABURIRYOU_SOKUTEI, srRhapsData));
        //積層中外観
        this.setItemData(processData, GXHDO101B006Const.SEKISOUTYUU_GAIKAN, getSrRhapsItemData(GXHDO101B006Const.SEKISOUTYUU_GAIKAN, srRhapsData));
        //にじみ量測定(積層後)
        this.setItemData(processData, GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_SEKISOUGO, getSrRhapsItemData(GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_SEKISOUGO, srRhapsData));
        //積層品外観
        this.setItemData(processData, GXHDO101B006Const.SEKISOUHIN_GAIKAN, getSrRhapsItemData(GXHDO101B006Const.SEKISOUHIN_GAIKAN, srRhapsData));
        //積層ズレチェック
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_ZURE_CHECK, getSrRhapsItemData(GXHDO101B006Const.SEKISOU_ZURE_CHECK, srRhapsData));
        //上端子加圧力
        this.setItemData(processData, GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU, getSrRhapsItemData(GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU, srRhapsData));
        //上端子上昇速切替位置
        this.setItemData(processData, GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI, getSrRhapsItemData(GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI, srRhapsData));
        //下端子下降速切替位置
        this.setItemData(processData, GXHDO101B006Const.SHITA_TANSHI_KAKOU_SOKU_KIRIKAE_ICHI, getSrRhapsItemData(GXHDO101B006Const.SHITA_TANSHI_KAKOU_SOKU_KIRIKAE_ICHI, srRhapsData));
        //ターゲットインク種類
        this.setItemData(processData, GXHDO101B006Const.TARGET_INK_SHURUI, getSrRhapsItemData(GXHDO101B006Const.TARGET_INK_SHURUI, srRhapsData));
        //ターゲットインクLOT
        this.setItemData(processData, GXHDO101B006Const.TARGET_INK_LOT, getSrRhapsItemData(GXHDO101B006Const.TARGET_INK_LOT, srRhapsData));
        //ターゲット印刷クリアランス
        this.setItemData(processData, GXHDO101B006Const.TARGET_INSATSU_CLEARANCE, getSrRhapsItemData(GXHDO101B006Const.TARGET_INSATSU_CLEARANCE, srRhapsData));
        //ターゲット印刷スキージスピード
        this.setItemData(processData, GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED, getSrRhapsItemData(GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED, srRhapsData));
        //ターゲット印刷差圧
        this.setItemData(processData, GXHDO101B006Const.TARGET_INSATSU_SAATSU, getSrRhapsItemData(GXHDO101B006Const.TARGET_INSATSU_SAATSU, srRhapsData));
        //ターゲット印刷外観
        this.setItemData(processData, GXHDO101B006Const.TARGET_INSATSU_GAIKAN, getSrRhapsItemData(GXHDO101B006Const.TARGET_INSATSU_GAIKAN, srRhapsData));
        //印刷積層開始日
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY, srRhapsData));
        //印刷積層開始時間
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME, srRhapsData));
        //印刷積層開始担当者
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TANTOUSHA, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TANTOUSHA, srRhapsData));
        //印刷積層終了日
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY, srRhapsData));
        //印刷積層終了時間
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME, srRhapsData));
        //印刷積層終了担当者
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TANTOUSHA, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TANTOUSHA, srRhapsData));
        //ターゲット印刷終了日
        this.setItemData(processData, GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY, getSrRhapsItemData(GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY, srRhapsData));
        //ターゲット印刷担当者
        this.setItemData(processData, GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA, getSrRhapsItemData(GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA, srRhapsData));
        //処理セット数
        this.setItemData(processData, GXHDO101B006Const.SHORI_SET_SU, getSrRhapsItemData(GXHDO101B006Const.SHORI_SET_SU, srRhapsData));
        //良品セット数
        this.setItemData(processData, GXHDO101B006Const.RYOUHIN_SET_SU, getSrRhapsItemData(GXHDO101B006Const.RYOUHIN_SET_SU, srRhapsData));
        //ヘッド交換者
        this.setItemData(processData, GXHDO101B006Const.HEAD_KOUKANSHA, getSrRhapsItemData(GXHDO101B006Const.HEAD_KOUKANSHA, srRhapsData));
        //積層条件者
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_JOUKENSHA, getSrRhapsItemData(GXHDO101B006Const.SEKISOU_JOUKENSHA, srRhapsData));
        //Ｅ製版セット者
        this.setItemData(processData, GXHDO101B006Const.E_SEIHAN_SETSHA, getSrRhapsItemData(GXHDO101B006Const.E_SEIHAN_SETSHA, srRhapsData));
        //Ｃ製版セット者
        this.setItemData(processData, GXHDO101B006Const.C_SEIHAN_SETSHA, getSrRhapsItemData(GXHDO101B006Const.C_SEIHAN_SETSHA, srRhapsData));
        //段差測定者
        this.setItemData(processData, GXHDO101B006Const.DANSA_SOKUTEISHA, getSrRhapsItemData(GXHDO101B006Const.DANSA_SOKUTEISHA, srRhapsData));
        //備考1
        this.setItemData(processData, GXHDO101B006Const.BIKOU1, getSrRhapsItemData(GXHDO101B006Const.BIKOU1, srRhapsData));
        //備考2
        this.setItemData(processData, GXHDO101B006Const.BIKOU2, getSrRhapsItemData(GXHDO101B006Const.BIKOU2, srRhapsData));
        //印刷積層開始確認者
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, srRhapsData));
        //ﾀｰｹﾞｯﾄ有無
        this.setItemData(processData, GXHDO101B006Const.TARGET_UMU, getSrRhapsItemData(GXHDO101B006Const.TARGET_UMU, srRhapsData));

    }

    /**
     * 電極膜厚入力画面データ設定処理
     *
     * @param subSrRhapsData 印刷積層RHAPS_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
    private void setInputItemDataSubFormC007(SubSrRhaps subSrRhapsData, String kojyo, String lotNo, String edaban) {
        // サブ画面の情報を取得
        GXHDO101C007 beanGXHDO101C007 = (GXHDO101C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C007);

        //データの設定
        String[] makuatsuStart;
        beanGXHDO101C007.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C007.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C007.setEdaban(edaban); //枝番

        GXHDO101C007Model model;
        if (subSrRhapsData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", "", "", "", "", ""}; //電極膜厚1～9
            model = GXHDO101C007Logic.createGXHDO101C007Model(makuatsuStart);

        } else {
            // 登録データがあれば登録データをセットする。
            //電極膜厚1～9
            makuatsuStart = new String[]{
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu1()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu2()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu3()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu4()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu5()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu6()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu7()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu8()),
                StringUtil.nullToBlank(subSrRhapsData.getEmakuatsu9())};

            model = GXHDO101C007Logic.createGXHDO101C007Model(makuatsuStart);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdAve(GXHDO101B006Const.DENKYOKU_MAKUATSU_AVE);
        model.setReturnItemIdMax(GXHDO101B006Const.DENKYOKU_MAKUATSU_MAX);
        model.setReturnItemIdMin(GXHDO101B006Const.DENKYOKU_MAKUATSU_MIN);

        beanGXHDO101C007.setGxhdO101c007Model(model);
    }

    /**
     * ﾊﾟﾀｰﾝ間距離入力画面データ設定処理
     *
     * @param subSrRhapsData 印刷積層RHAPS_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
    private void setInputItemDataSubFormC008(SubSrRhaps subSrRhapsData, String kojyo, String lotNo, String edaban) {
        // サブ画面の情報を取得
        GXHDO101C008 beanGXHDO101C008 = (GXHDO101C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C008);

        //データの設定
        String[] makuatsuStart;
        beanGXHDO101C008.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C008.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C008.setEdaban(edaban); //枝番

        GXHDO101C008Model model;
        if (subSrRhapsData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", ""}; //ﾊﾟﾀｰﾝ間距離スタート1～5
            model = GXHDO101C008Logic.createGXHDO101C008Model(makuatsuStart);

        } else {
            // 登録データがあれば登録データをセットする。
            //ﾊﾟﾀｰﾝ間距離スタート1～5
            makuatsuStart = new String[]{
                StringUtil.nullToBlank(subSrRhapsData.getPtndist1()),
                StringUtil.nullToBlank(subSrRhapsData.getPtndist2()),
                StringUtil.nullToBlank(subSrRhapsData.getPtndist3()),
                StringUtil.nullToBlank(subSrRhapsData.getPtndist4()),
                StringUtil.nullToBlank(subSrRhapsData.getPtndist5())};

            model = GXHDO101C008Logic.createGXHDO101C008Model(makuatsuStart);
        }

        beanGXHDO101C008.setGxhdO101c008Model(model);
    }

    /**
     * 合わせ(RZ)入力画面データ設定処理
     *
     * @param subSrRhapsData 印刷積層RHAPS_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
    private void setInputItemDataSubFormC009(SubSrRhaps subSrRhapsData, String kojyo, String lotNo, String edaban) {
        // サブ画面の情報を取得
        GXHDO101C009 beanGXHDO101C009 = (GXHDO101C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C009);

        //データの設定
        String[] awaseRzStart;
        beanGXHDO101C009.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C009.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C009.setEdaban(edaban); //枝番

        GXHDO101C009Model model;
        if (subSrRhapsData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            awaseRzStart = new String[]{"", "", "", "", "", "", "", "", ""}; //合わせ(RZ)1～9
            model = GXHDO101C009Logic.createGXHDO101C009Model(awaseRzStart);

        } else {
            // 登録データがあれば登録データをセットする。
            //合わせ(RZ)スタート1～5
            awaseRzStart = new String[]{
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz1()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz2()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz3()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz4()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz5()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz6()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz7()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz8()),
                StringUtil.nullToBlank(subSrRhapsData.getAwaserz9())};

            model = GXHDO101C009Logic.createGXHDO101C009Model(awaseRzStart);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdAve(GXHDO101B006Const.AWASE_INSATSU_SIDE_RZ_AVE);

        beanGXHDO101C009.setGxhdO101c009Model(model);
    }

    /**
     * 被り量(µm)入力画面データ設定処理
     *
     * @param subSrRhapsData 印刷積層RHAPS_ｻﾌﾞ画面データ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param sekkeino 設計No
     */
    private void setInputItemDataSubFormC010(SubSrRhaps subSrRhapsData, String kojyo, String lotNo, String edaban, String sekkeino, QueryRunner queryRunnerQcdb) throws SQLException {
        // サブ画面の情報を取得
        GXHDO101C010 beanGXHDO101C010 = (GXHDO101C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C010);

        //データの設定
        String[] kaburiRyoStart;
        beanGXHDO101C010.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C010.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C010.setEdaban(edaban); //枝番
        if (!StringUtil.isEmpty(sekkeino)) {
            String[] kaburiryoXY = GXHDO101C010Logic.getKaburiryo(LOGGER, queryRunnerQcdb, sekkeino);
            beanGXHDO101C010.setKaburiryoX(kaburiryoXY[0]); //被り量X
            beanGXHDO101C010.setKaburiryoY(kaburiryoXY[1]); //被り量Y
        }

        GXHDO101C010Model model;
        if (subSrRhapsData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            kaburiRyoStart = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; //被り量(µm)1～20
            model = GXHDO101C010Logic.createGXHDO101C010Model(kaburiRyoStart);

        } else {
            // 登録データがあれば登録データをセットする。
            //被り量1～20
            kaburiRyoStart = new String[]{
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidariuex1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidariuex2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidariuey1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidariuey2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidarisitax1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidarisitax2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidarisitay1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidarisitay2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidaricenterx1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidaricenterx2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidaricentery1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburihidaricentery2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigiuex1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigiuex2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigiuey1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigiuey2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigisitax1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigisitax2()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigisitay1()),
                StringUtil.nullToBlank(subSrRhapsData.getKaburimigisitay2())};

            model = GXHDO101C010Logic.createGXHDO101C010Model(kaburiRyoStart);
        }

        beanGXHDO101C010.setGxhdO101c010Model(model);
    }

    /**
     * 印刷積層RHAPSの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 印刷積層RHAPS登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrRhaps> getSrRhapsData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrRhaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrRhaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 印刷積層RHAPS_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 印刷積層RHAPS_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRhaps> getSubSrRhapsData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg,
            String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrRhaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrRhaps(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        String sql = "SELECT SEKKEINO,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,"
                + "YOUTO1,YOUTO2,YOUTO3,YOUTO4,YOUTO5,YOUTO6,YOUTO7,YOUTO8,"
                + "SYURUI1,SYURUI2,SYURUI3,SYURUI4,SYURUI5,SYURUI6,SYURUI7,SYURUI8,"
                + "ATUMI1,ATUMI2,ATUMI3,ATUMI4,ATUMI5,ATUMI6,ATUMI7,ATUMI8,"
                + "MAISUU1,MAISUU2,MAISUU3,MAISUU4,MAISUU5,MAISUU6,MAISUU7,MAISUU8,"
                + "PATTERN "
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
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"GENRYOU", "電極テープ"});
        list.add(new String[]{"ETAPE", "電極テープ"});
        list.add(new String[]{"EATUMI", "積層数"});
        list.add(new String[]{"SOUSUU", "積層数"});
        list.add(new String[]{"EMAISUU", "積層数"});
        list.add(new String[]{"PATTERN", "電極製版名"});
        list.add(new String[]{"PATTERN", "誘電体製版名"});

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
     * [印刷積層RHAPS]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrRhaps> loadSrRhaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
                + "CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,AINSATUSRZ2,AINSATUSRZ3,"
                + "AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,"
                + "FSTMSX1,FSTMSX2,FSTMSY1,FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,"
                + "LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,"
                + "TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,"
                + "KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,EMAKUATSUMAX,EMAKUATSUMIN,"
                + "NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,"
                + "TGAIKAN,STARTTANTOU,ENDTANTOU,TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,"
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision,'0' AS deleteflag,startkakunin,TUMU "
                + "FROM sr_rhaps "
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
        mapping.put("KAISINICHIJI", "kaisinichiji"); //開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //終了日時
        mapping.put("TTAPESYURUI", "ttapesyurui"); //端子ﾃｰﾌﾟ種類
        mapping.put("TTAPELOTNO", "ttapelotno"); //端子ﾃｰﾌﾟﾛｯﾄNo
        mapping.put("TTapeSlipKigo", "tTapeSlipKigo"); //端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("TTapeRollNo1", "tTapeRollNo1"); //端子ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("TTapeRollNo2", "tTapeRollNo2"); //端子ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("TTapeRollNo3", "tTapeRollNo3"); //端子ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("TTapeRollNo4", "tTapeRollNo4"); //端子ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("TTapeRollNo5", "tTapeRollNo5"); //端子ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("TGENRYOKIGO", "tgenryokigo"); //端子原料記号
        mapping.put("STSIYO", "stsiyo"); //下端子仕様
        mapping.put("ESEKISOSIYO", "esekisosiyo"); //電極積層仕様
        mapping.put("ETAPESYURUI", "etapesyurui"); //電極ﾃｰﾌﾟ種類
        mapping.put("ETAPEGLOT", "etapeglot"); //電極ﾃｰﾌﾟ原料ﾛｯﾄ
        mapping.put("ETAPELOT", "etapelot"); //電極ﾃｰﾌﾟﾛｯﾄ
        mapping.put("ETapeSlipKigo", "eTapeSlipKigo"); //電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("ETapeRollNo1", "eTapeRollNo1"); //電極ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("ETapeRollNo2", "eTapeRollNo2"); //電極ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("ETapeRollNo3", "eTapeRollNo3"); //電極ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("ETapeRollNo4", "eTapeRollNo4"); //電極ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("ETapeRollNo5", "eTapeRollNo5"); //電極ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("SPTUDENJIKAN", "sptudenjikan"); //積層ﾌﾟﾚｽ通電時間
        mapping.put("SKAATURYOKU", "skaaturyoku"); //積層加圧力
        mapping.put("SKHEADNO", "skheadno"); //瞬時加熱ﾍｯﾄﾞNo
        mapping.put("SUSSKAISUU", "susskaisuu"); //SUS板使用回数
        mapping.put("ECPASTEMEI", "ecpastemei"); //電極誘電体ﾍﾟｰｽﾄ名
        mapping.put("EPASTELOTNO", "epastelotno"); //電極ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("EPASTENENDO", "epastenendo"); //電極ﾍﾟｰｽﾄ粘度
        mapping.put("EPASTEONDO", "epasteondo"); //電極ﾍﾟｰｽﾄ温度
        mapping.put("ESEIHANMEI", "eseihanmei"); //電極製版名
        mapping.put("ESEIHANNO", "eseihanno"); //電極製版No
        mapping.put("ESEIMAISUU", "eseimaisuu"); //電極製版枚数
        mapping.put("ECLEARANCE", "eclearance"); //電極ｸﾘｱﾗﾝｽ
        mapping.put("ESAATU", "esaatu"); //電極差圧
        mapping.put("ESKEEGENO", "eskeegeno"); //電極ｽｷｰｼﾞNo
        mapping.put("ESKMAISUU", "eskmaisuu"); //電極ｽｷｰｼﾞ枚数
        mapping.put("ESKSPEED", "eskspeed"); //電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("ESCCLEARANCE", "escclearance"); //電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        mapping.put("ESKKMJIKAN", "eskkmjikan"); //電極ｽｷｰｼﾞ下降待ち時間
        mapping.put("ELDSTART", "eldstart"); //電極L/Dｽﾀｰﾄ時
        mapping.put("ESEIMENSEKI", "eseimenseki"); //電極製版面積
        mapping.put("EMAKUATU", "emakuatu"); //電極膜厚
        mapping.put("ESLIDERYO", "eslideryo"); //電極ｽﾗｲﾄﾞ量
        mapping.put("EKANSOONDO", "ekansoondo"); //電極乾燥温度
        mapping.put("EKANSOJIKAN", "ekansojikan"); //電極乾燥時間
        mapping.put("CPASTELOTNO", "cpastelotno"); //誘電体ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("CPASTENENDO", "cpastenendo"); //誘電体ﾍﾟｰｽﾄ粘度
        mapping.put("CPASTEONDO", "cpasteondo"); //誘電体ﾍﾟｰｽﾄ温度
        mapping.put("CSEIHANMEI", "cseihanmei"); //誘電体製版名
        mapping.put("CSEIHANNO", "cseihanno"); //誘電体製版No
        mapping.put("CSEIMAISUU", "cseimaisuu"); //誘電体製版枚数
        mapping.put("CCLEARANCE", "cclearance"); //誘電体ｸﾘｱﾗﾝｽ
        mapping.put("CSAATU", "csaatu"); //誘電体差圧
        mapping.put("CSKEEGENO", "cskeegeno"); //誘電体ｽｷｰｼﾞNo
        mapping.put("CSKMAISUU", "cskmaisuu"); //誘電体ｽｷｰｼﾞ枚数
        mapping.put("CSCCLEARANCE", "cscclearance"); //誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        mapping.put("CSKKMJIKAN", "cskkmjikan"); //誘電体ｽｷｰｼﾞ下降待ち時間
        mapping.put("CSHIFTINSATU", "cshiftinsatu"); //誘電体ｼﾌﾄ印刷
        mapping.put("CLDSTART", "cldstart"); //誘電体L/Dｽﾀｰﾄ時
        mapping.put("CSEIMENSEKI", "cseimenseki"); //誘電体製版面積
        mapping.put("CSLIDERYO", "cslideryo"); //誘電体ｽﾗｲﾄﾞ量
        mapping.put("CKANSOONDO", "ckansoondo"); //誘電体乾燥温度
        mapping.put("CKANSOJIKAN", "ckansojikan"); //誘電体乾燥時間
        mapping.put("CMAKUATU", "cmakuatu"); //誘電体膜厚
        mapping.put("AINSATUSRZ1", "ainsatusrz1"); //合わせ印刷ｻｲﾄﾞRZ1
        mapping.put("AINSATUSRZ2", "ainsatusrz2"); //合わせ印刷ｻｲﾄﾞRZ2
        mapping.put("AINSATUSRZ3", "ainsatusrz3"); //合わせ印刷ｻｲﾄﾞRZ3
        mapping.put("AINSATUSRZ4", "ainsatusrz4"); //合わせ印刷ｻｲﾄﾞRZ4
        mapping.put("AINSATUSRZ5", "ainsatusrz5"); //合わせ印刷ｻｲﾄﾞRZ5
        mapping.put("AINSATUSRZAVE", "ainsatusrzave"); //合わせ印刷ｻｲﾄﾞRZAVE
        mapping.put("UTSIYO", "utsiyo"); //上端子仕様
        mapping.put("UTTUDENJIKAN", "uttudenjikan"); //上端子通電時間
        mapping.put("UTKAATURYOKU", "utkaaturyoku"); //上端子加圧力
        mapping.put("STAHOSEI", "stahosei"); //積層体厚み補正
        mapping.put("TICLEARANCE", "ticlearance"); //ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
        mapping.put("TISAATU", "tisaatu"); //ﾀｰｹﾞｯﾄ印刷差圧
        mapping.put("TISKSPEED", "tiskspeed"); //ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("FSTHUX1", "fsthux1"); //初層左上X1
        mapping.put("FSTHUX2", "fsthux2"); //初層左上X2
        mapping.put("FSTHUY1", "fsthuy1"); //初層左上Y1
        mapping.put("FSTHUY2", "fsthuy2"); //初層左上Y2
        mapping.put("FSTHSX1", "fsthsx1"); //初層左下X1
        mapping.put("FSTHSX2", "fsthsx2"); //初層左下X2
        mapping.put("FSTHSY1", "fsthsy1"); //初層左下Y1
        mapping.put("FSTHSY2", "fsthsy2"); //初層左下Y2
        mapping.put("FSTCX1", "fstcx1"); //初層中央X1
        mapping.put("FSTCX2", "fstcx2"); //初層中央X2
        mapping.put("FSTCY1", "fstcy1"); //初層中央Y1
        mapping.put("FSTCY2", "fstcy2"); //初層中央Y2
        mapping.put("FSTMUX1", "fstmux1"); //初層右上X1
        mapping.put("FSTMUX2", "fstmux2"); //初層右上X2
        mapping.put("FSTMUY1", "fstmuy1"); //初層右上Y1
        mapping.put("FSTMUY2", "fstmuy2"); //初層右上Y2
        mapping.put("FSTMSX1", "fstmsx1"); //初層右下X1
        mapping.put("FSTMSX2", "fstmsx2"); //初層右下X2
        mapping.put("FSTMSY1", "fstmsy1"); //初層右下Y1
        mapping.put("FSTMSY2", "fstmsy2"); //初層右下Y2
        mapping.put("LSTHUX1", "lsthux1"); //最終層左上X1
        mapping.put("LSTHUX2", "lsthux2"); //最終層左上X2
        mapping.put("LSTHUY1", "lsthuy1"); //最終層左上Y1
        mapping.put("LSTHUY2", "lsthuy2"); //最終層左上Y2
        mapping.put("LSTHSX1", "lsthsx1"); //最終層左下X1
        mapping.put("LSTHSX2", "lsthsx2"); //最終層左下X2
        mapping.put("LSTHSY1", "lsthsy1"); //最終層左下Y1
        mapping.put("LSTHSY2", "lsthsy2"); //最終層左下Y2
        mapping.put("LSTCX1", "lstcx1"); //最終層中央X1
        mapping.put("LSTCX2", "lstcx2"); //最終層中央X2
        mapping.put("LSTCY1", "lstcy1"); //最終層中央Y1
        mapping.put("LSTCY2", "lstcy2"); //最終層中央Y2
        mapping.put("LSTMUX1", "lstmux1"); //最終層右上X1
        mapping.put("LSTMUX2", "lstmux2"); //最終層右上X2
        mapping.put("LSTMUY1", "lstmuy1"); //最終層右上Y1
        mapping.put("LSTMUY2", "lstmuy2"); //最終層右上Y2
        mapping.put("LSTMSX1", "lstmsx1"); //最終層右下X1
        mapping.put("LSTMSX2", "lstmsx2"); //最終層右下X2
        mapping.put("LSTMSY1", "lstmsy1"); //最終層右下Y1
        mapping.put("LSTMSY2", "lstmsy2"); //最終層右下Y2
        mapping.put("BIKO1", "biko1"); //備考1
        mapping.put("BIKO2", "biko2"); //備考2
        mapping.put("TOROKUNICHIJI", "torokunichiji"); //登録日時
        mapping.put("KOSINNICHIJI", "kosinnichiji"); //更新日時
        mapping.put("GOKI", "goki"); //号機ｺｰﾄﾞ
        mapping.put("TTANSISUUU", "ttansisuuu"); //特別端子枚数上
        mapping.put("TTANSISUUS", "ttansisuus"); //特別端子枚数下
        mapping.put("SHUNKANKANETSUJIKAN", "shunkankanetsujikan"); //瞬時加熱時間
        mapping.put("PETFILMSYURUI", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("KAATURYOKU", "kaaturyoku"); //加圧力
        mapping.put("GAIKANKAKUNIN", "gaikankakunin"); //外観確認
        mapping.put("SEKIJSSKIRIKAEICHI", "sekijsskirikaeichi"); //積層上昇速切替位置
        mapping.put("SEKIKKSKIRIKAEICHI", "sekikkskirikaeichi"); //積層下降速切替位置
        mapping.put("KAATUJIKAN", "kaatujikan"); //加圧時間
        mapping.put("TAPEHANSOUPITCH", "tapehansoupitch"); //ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
        mapping.put("TAPEHANSOUKAKUNIN", "tapehansoukakunin"); //ﾃｰﾌﾟ搬送目視確認
        mapping.put("EMAKUATSUSETTEI", "emakuatsusettei"); //電極膜厚設定
        mapping.put("ENEPPUFURYOU", "eneppufuryou"); //電極熱風風量
        mapping.put("EMAKUATSUAVE", "emakuatsuave"); //電極膜厚AVE
        mapping.put("EMAKUATSUMAX", "emakuatsumax"); //電極膜厚MAX
        mapping.put("EMAKUATSUMIN", "emakuatsumin"); //電極膜厚MIN
        mapping.put("NIJIMISOKUTEIPTN", "nijimisokuteiptn"); //にじみ量測定(ﾊﾟﾀｰﾝ間距離)
        mapping.put("PRNSAMPLEGAIKAN", "prnsamplegaikan"); //印刷ｻﾝﾌﾟﾙ外観確認
        mapping.put("PRNICHIYOHAKUNAGASA", "prnichiyohakunagasa"); //印刷位置余白長さ
        mapping.put("CTABLECLEARANCE", "ctableclearance"); //誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        mapping.put("CMAKUATSUSETTEI", "cmakuatsusettei"); //誘電体膜厚設定
        mapping.put("CSKSPEED", "cskspeed"); //誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("CNEPPUFURYOU", "cneppufuryou"); //誘電体熱風風量
        mapping.put("KABURIRYOU", "kaburiryou"); //被り量測定
        mapping.put("SGAIKAN", "sgaikan"); //積層中外観
        mapping.put("NIJIMISOKUTEISEKISOUGO", "nijimisokuteisekisougo"); //にじみ量測定(積層後)
        mapping.put("SEKISOUHINGAIKAN", "sekisouhingaikan"); //積層品外観
        mapping.put("SEKISOUZURE", "sekisouzure"); //積層ｽﾞﾚﾁｪｯｸ
        mapping.put("UWAJSSKIRIKAEICHI", "uwajsskirikaeichi"); //上端子上昇速切替位置
        mapping.put("SHITAKKSKIRIKAEICHI", "shitakkskirikaeichi"); //下端子下降速切替位置
        mapping.put("TINKSYURYUI", "tinksyuryui"); //ﾀｰｹﾞｯﾄｲﾝｸ種類
        mapping.put("TINKLOT", "tinklot"); //ﾀｰｹﾞｯﾄｲﾝｸLOT
        mapping.put("TGAIKAN", "tgaikan"); //ﾀｰｹﾞｯﾄ印刷外観
        mapping.put("STARTTANTOU", "starttantou"); //印刷積層開始担当者
        mapping.put("ENDTANTOU", "endtantou"); //印刷積層終了担当者
        mapping.put("TENDDAY", "tendday"); //ﾀｰｹﾞｯﾄ印刷終了日
        mapping.put("TENDTANTOU", "tendtantou"); //ﾀｰｹﾞｯﾄ印刷担当者
        mapping.put("SYORISETSUU", "syorisetsuu"); //処理ｾｯﾄ数
        mapping.put("RYOUHINSETSUU", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("HEADKOUKANTANTOU", "headkoukantantou"); //ﾍｯﾄﾞ交換者
        mapping.put("SEKISOUJOUKENTANTOU", "sekisoujoukentantou"); //積層条件者
        mapping.put("ESEIHANSETTANTOU", "eseihansettantou"); //E製版ｾｯﾄ者
        mapping.put("CSEIHANSETTANTOU", "cseihansettantou"); //C製版ｾｯﾄ者
        mapping.put("DANSASOKUTEITANTOU", "dansasokuteitantou"); //段差測定者
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("startkakunin", "startkakunin"); //印刷積層開始確認者
        mapping.put("TUMU", "tumu"); //ﾀｰｹﾞｯﾄ有無

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrRhaps>> beanHandler = new BeanListHandler<>(SrRhaps.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷積層RHAPS_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRhaps> loadSubSrRhaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "KOJYO,LOTNO,EDABAN,EMAKUATSU1,EMAKUATSU2,EMAKUATSU3,EMAKUATSU4,EMAKUATSU5,EMAKUATSU6,"
                + "EMAKUATSU7,EMAKUATSU8,EMAKUATSU9,PTNDIST1,PTNDIST2,PTNDIST3,PTNDIST4,PTNDIST5,AWASERZ1,"
                + "AWASERZ2,AWASERZ3,AWASERZ4,AWASERZ5,AWASERZ6,AWASERZ7,AWASERZ8,AWASERZ9,KABURIHIDARIUEX1,"
                + "KABURIHIDARIUEX2,KABURIHIDARIUEY1,KABURIHIDARIUEY2,KABURIHIDARISITAX1,KABURIHIDARISITAX2,"
                + "KABURIHIDARISITAY1,KABURIHIDARISITAY2,KABURIHIDARICENTERX1,KABURIHIDARICENTERX2,"
                + "KABURIHIDARICENTERY1,KABURIHIDARICENTERY2,KABURIMIGIUEX1,KABURIMIGIUEX2,KABURIMIGIUEY1,"
                + "KABURIMIGIUEY2,KABURIMIGISITAX1,KABURIMIGISITAX2,KABURIMIGISITAY1,KABURIMIGISITAY2,"
                + "torokunichiji,kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sub_sr_rhaps "
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
        mapping.put("EMAKUATSU1", "emakuatsu1"); //電極膜厚1
        mapping.put("EMAKUATSU2", "emakuatsu2"); //電極膜厚2
        mapping.put("EMAKUATSU3", "emakuatsu3"); //電極膜厚3
        mapping.put("EMAKUATSU4", "emakuatsu4"); //電極膜厚4
        mapping.put("EMAKUATSU5", "emakuatsu5"); //電極膜厚5
        mapping.put("EMAKUATSU6", "emakuatsu6"); //電極膜厚6
        mapping.put("EMAKUATSU7", "emakuatsu7"); //電極膜厚7
        mapping.put("EMAKUATSU8", "emakuatsu8"); //電極膜厚8
        mapping.put("EMAKUATSU9", "emakuatsu9"); //電極膜厚9
        mapping.put("PTNDIST1", "ptndist1"); //ﾊﾟﾀｰﾝ間距離1
        mapping.put("PTNDIST2", "ptndist2"); //ﾊﾟﾀｰﾝ間距離2
        mapping.put("PTNDIST3", "ptndist3"); //ﾊﾟﾀｰﾝ間距離3
        mapping.put("PTNDIST4", "ptndist4"); //ﾊﾟﾀｰﾝ間距離4
        mapping.put("PTNDIST5", "ptndist5"); //ﾊﾟﾀｰﾝ間距離5
        mapping.put("AWASERZ1", "awaserz1"); //合わせ(RZ)1
        mapping.put("AWASERZ2", "awaserz2"); //合わせ(RZ)2
        mapping.put("AWASERZ3", "awaserz3"); //合わせ(RZ)3
        mapping.put("AWASERZ4", "awaserz4"); //合わせ(RZ)4
        mapping.put("AWASERZ5", "awaserz5"); //合わせ(RZ)5
        mapping.put("AWASERZ6", "awaserz6"); //合わせ(RZ)6
        mapping.put("AWASERZ7", "awaserz7"); //合わせ(RZ)7
        mapping.put("AWASERZ8", "awaserz8"); //合わせ(RZ)8
        mapping.put("AWASERZ9", "awaserz9"); //合わせ(RZ)9
        mapping.put("KABURIHIDARIUEX1", "kaburihidariuex1"); //被り量（μｍ）1
        mapping.put("KABURIHIDARIUEX2", "kaburihidariuex2"); //被り量（μｍ）2
        mapping.put("KABURIHIDARIUEY1", "kaburihidariuey1"); //被り量（μｍ）3
        mapping.put("KABURIHIDARIUEY2", "kaburihidariuey2"); //被り量（μｍ）4
        mapping.put("KABURIHIDARISITAX1", "kaburihidarisitax1"); //被り量（μｍ）5
        mapping.put("KABURIHIDARISITAX2", "kaburihidarisitax2"); //被り量（μｍ）6
        mapping.put("KABURIHIDARISITAY1", "kaburihidarisitay1"); //被り量（μｍ）7
        mapping.put("KABURIHIDARISITAY2", "kaburihidarisitay2"); //被り量（μｍ）8
        mapping.put("KABURIHIDARICENTERX1", "kaburihidaricenterx1"); //被り量（μｍ）9
        mapping.put("KABURIHIDARICENTERX2", "kaburihidaricenterx2"); //被り量（μｍ）10
        mapping.put("KABURIHIDARICENTERY1", "kaburihidaricentery1"); //被り量（μｍ）11
        mapping.put("KABURIHIDARICENTERY2", "kaburihidaricentery2"); //被り量（μｍ）12
        mapping.put("KABURIMIGIUEX1", "kaburimigiuex1"); //被り量（μｍ）13
        mapping.put("KABURIMIGIUEX2", "kaburimigiuex2"); //被り量（μｍ）14
        mapping.put("KABURIMIGIUEY1", "kaburimigiuey1"); //被り量（μｍ）15
        mapping.put("KABURIMIGIUEY2", "kaburimigiuey2"); //被り量（μｍ）16
        mapping.put("KABURIMIGISITAX1", "kaburimigisitax1"); //被り量（μｍ）17
        mapping.put("KABURIMIGISITAX2", "kaburimigisitax2"); //被り量（μｍ）18
        mapping.put("KABURIMIGISITAY1", "kaburimigisitay1"); //被り量（μｍ）19
        mapping.put("KABURIMIGISITAY2", "kaburimigisitay2"); //被り量（μｍ）20
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        
        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrRhaps>> beanHandler = new BeanListHandler<>(SubSrRhaps.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷積層RHAPS_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrRhaps> loadTmpSrRhaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {
        String sql = "SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
                + "CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,AINSATUSRZ2,AINSATUSRZ3,"
                + "AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,"
                + "FSTMSX1,FSTMSX2,FSTMSY1,FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,"
                + "LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,"
                + "TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,"
                + "KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,EMAKUATSUMAX,EMAKUATSUMIN,"
                + "NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,"
                + "TGAIKAN,STARTTANTOU,ENDTANTOU,TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,"
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision,deleteflag,startkakunin,TUMU "
                + "FROM tmp_sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND deleteflag = ? ";
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
        mapping.put("KAISINICHIJI", "kaisinichiji"); //開始日時
        mapping.put("SYURYONICHIJI", "syuryonichiji"); //終了日時
        mapping.put("TTAPESYURUI", "ttapesyurui"); //端子ﾃｰﾌﾟ種類
        mapping.put("TTAPELOTNO", "ttapelotno"); //端子ﾃｰﾌﾟﾛｯﾄNo
        mapping.put("TTapeSlipKigo", "tTapeSlipKigo"); //端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("TTapeRollNo1", "tTapeRollNo1"); //端子ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("TTapeRollNo2", "tTapeRollNo2"); //端子ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("TTapeRollNo3", "tTapeRollNo3"); //端子ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("TTapeRollNo4", "tTapeRollNo4"); //端子ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("TTapeRollNo5", "tTapeRollNo5"); //端子ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("TGENRYOKIGO", "tgenryokigo"); //端子原料記号
        mapping.put("STSIYO", "stsiyo"); //下端子仕様
        mapping.put("ESEKISOSIYO", "esekisosiyo"); //電極積層仕様
        mapping.put("ETAPESYURUI", "etapesyurui"); //電極ﾃｰﾌﾟ種類
        mapping.put("ETAPEGLOT", "etapeglot"); //電極ﾃｰﾌﾟ原料ﾛｯﾄ
        mapping.put("ETAPELOT", "etapelot"); //電極ﾃｰﾌﾟﾛｯﾄ
        mapping.put("ETapeSlipKigo", "eTapeSlipKigo"); //電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("ETapeRollNo1", "eTapeRollNo1"); //電極ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("ETapeRollNo2", "eTapeRollNo2"); //電極ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("ETapeRollNo3", "eTapeRollNo3"); //電極ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("ETapeRollNo4", "eTapeRollNo4"); //電極ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("ETapeRollNo5", "eTapeRollNo5"); //電極ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("SPTUDENJIKAN", "sptudenjikan"); //積層ﾌﾟﾚｽ通電時間
        mapping.put("SKAATURYOKU", "skaaturyoku"); //積層加圧力
        mapping.put("SKHEADNO", "skheadno"); //瞬時加熱ﾍｯﾄﾞNo
        mapping.put("SUSSKAISUU", "susskaisuu"); //SUS板使用回数
        mapping.put("ECPASTEMEI", "ecpastemei"); //電極誘電体ﾍﾟｰｽﾄ名
        mapping.put("EPASTELOTNO", "epastelotno"); //電極ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("EPASTENENDO", "epastenendo"); //電極ﾍﾟｰｽﾄ粘度
        mapping.put("EPASTEONDO", "epasteondo"); //電極ﾍﾟｰｽﾄ温度
        mapping.put("ESEIHANMEI", "eseihanmei"); //電極製版名
        mapping.put("ESEIHANNO", "eseihanno"); //電極製版No
        mapping.put("ESEIMAISUU", "eseimaisuu"); //電極製版枚数
        mapping.put("ECLEARANCE", "eclearance"); //電極ｸﾘｱﾗﾝｽ
        mapping.put("ESAATU", "esaatu"); //電極差圧
        mapping.put("ESKEEGENO", "eskeegeno"); //電極ｽｷｰｼﾞNo
        mapping.put("ESKMAISUU", "eskmaisuu"); //電極ｽｷｰｼﾞ枚数
        mapping.put("ESKSPEED", "eskspeed"); //電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("ESCCLEARANCE", "escclearance"); //電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        mapping.put("ESKKMJIKAN", "eskkmjikan"); //電極ｽｷｰｼﾞ下降待ち時間
        mapping.put("ELDSTART", "eldstart"); //電極L/Dｽﾀｰﾄ時
        mapping.put("ESEIMENSEKI", "eseimenseki"); //電極製版面積
        mapping.put("EMAKUATU", "emakuatu"); //電極膜厚
        mapping.put("ESLIDERYO", "eslideryo"); //電極ｽﾗｲﾄﾞ量
        mapping.put("EKANSOONDO", "ekansoondo"); //電極乾燥温度
        mapping.put("EKANSOJIKAN", "ekansojikan"); //電極乾燥時間
        mapping.put("CPASTELOTNO", "cpastelotno"); //誘電体ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("CPASTENENDO", "cpastenendo"); //誘電体ﾍﾟｰｽﾄ粘度
        mapping.put("CPASTEONDO", "cpasteondo"); //誘電体ﾍﾟｰｽﾄ温度
        mapping.put("CSEIHANMEI", "cseihanmei"); //誘電体製版名
        mapping.put("CSEIHANNO", "cseihanno"); //誘電体製版No
        mapping.put("CSEIMAISUU", "cseimaisuu"); //誘電体製版枚数
        mapping.put("CCLEARANCE", "cclearance"); //誘電体ｸﾘｱﾗﾝｽ
        mapping.put("CSAATU", "csaatu"); //誘電体差圧
        mapping.put("CSKEEGENO", "cskeegeno"); //誘電体ｽｷｰｼﾞNo
        mapping.put("CSKMAISUU", "cskmaisuu"); //誘電体ｽｷｰｼﾞ枚数
        mapping.put("CSCCLEARANCE", "cscclearance"); //誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        mapping.put("CSKKMJIKAN", "cskkmjikan"); //誘電体ｽｷｰｼﾞ下降待ち時間
        mapping.put("CSHIFTINSATU", "cshiftinsatu"); //誘電体ｼﾌﾄ印刷
        mapping.put("CLDSTART", "cldstart"); //誘電体L/Dｽﾀｰﾄ時
        mapping.put("CSEIMENSEKI", "cseimenseki"); //誘電体製版面積
        mapping.put("CSLIDERYO", "cslideryo"); //誘電体ｽﾗｲﾄﾞ量
        mapping.put("CKANSOONDO", "ckansoondo"); //誘電体乾燥温度
        mapping.put("CKANSOJIKAN", "ckansojikan"); //誘電体乾燥時間
        mapping.put("CMAKUATU", "cmakuatu"); //誘電体膜厚
        mapping.put("AINSATUSRZ1", "ainsatusrz1"); //合わせ印刷ｻｲﾄﾞRZ1
        mapping.put("AINSATUSRZ2", "ainsatusrz2"); //合わせ印刷ｻｲﾄﾞRZ2
        mapping.put("AINSATUSRZ3", "ainsatusrz3"); //合わせ印刷ｻｲﾄﾞRZ3
        mapping.put("AINSATUSRZ4", "ainsatusrz4"); //合わせ印刷ｻｲﾄﾞRZ4
        mapping.put("AINSATUSRZ5", "ainsatusrz5"); //合わせ印刷ｻｲﾄﾞRZ5
        mapping.put("AINSATUSRZAVE", "ainsatusrzave"); //合わせ印刷ｻｲﾄﾞRZAVE
        mapping.put("UTSIYO", "utsiyo"); //上端子仕様
        mapping.put("UTTUDENJIKAN", "uttudenjikan"); //上端子通電時間
        mapping.put("UTKAATURYOKU", "utkaaturyoku"); //上端子加圧力
        mapping.put("STAHOSEI", "stahosei"); //積層体厚み補正
        mapping.put("TICLEARANCE", "ticlearance"); //ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
        mapping.put("TISAATU", "tisaatu"); //ﾀｰｹﾞｯﾄ印刷差圧
        mapping.put("TISKSPEED", "tiskspeed"); //ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("FSTHUX1", "fsthux1"); //初層左上X1
        mapping.put("FSTHUX2", "fsthux2"); //初層左上X2
        mapping.put("FSTHUY1", "fsthuy1"); //初層左上Y1
        mapping.put("FSTHUY2", "fsthuy2"); //初層左上Y2
        mapping.put("FSTHSX1", "fsthsx1"); //初層左下X1
        mapping.put("FSTHSX2", "fsthsx2"); //初層左下X2
        mapping.put("FSTHSY1", "fsthsy1"); //初層左下Y1
        mapping.put("FSTHSY2", "fsthsy2"); //初層左下Y2
        mapping.put("FSTCX1", "fstcx1"); //初層中央X1
        mapping.put("FSTCX2", "fstcx2"); //初層中央X2
        mapping.put("FSTCY1", "fstcy1"); //初層中央Y1
        mapping.put("FSTCY2", "fstcy2"); //初層中央Y2
        mapping.put("FSTMUX1", "fstmux1"); //初層右上X1
        mapping.put("FSTMUX2", "fstmux2"); //初層右上X2
        mapping.put("FSTMUY1", "fstmuy1"); //初層右上Y1
        mapping.put("FSTMUY2", "fstmuy2"); //初層右上Y2
        mapping.put("FSTMSX1", "fstmsx1"); //初層右下X1
        mapping.put("FSTMSX2", "fstmsx2"); //初層右下X2
        mapping.put("FSTMSY1", "fstmsy1"); //初層右下Y1
        mapping.put("FSTMSY2", "fstmsy2"); //初層右下Y2
        mapping.put("LSTHUX1", "lsthux1"); //最終層左上X1
        mapping.put("LSTHUX2", "lsthux2"); //最終層左上X2
        mapping.put("LSTHUY1", "lsthuy1"); //最終層左上Y1
        mapping.put("LSTHUY2", "lsthuy2"); //最終層左上Y2
        mapping.put("LSTHSX1", "lsthsx1"); //最終層左下X1
        mapping.put("LSTHSX2", "lsthsx2"); //最終層左下X2
        mapping.put("LSTHSY1", "lsthsy1"); //最終層左下Y1
        mapping.put("LSTHSY2", "lsthsy2"); //最終層左下Y2
        mapping.put("LSTCX1", "lstcx1"); //最終層中央X1
        mapping.put("LSTCX2", "lstcx2"); //最終層中央X2
        mapping.put("LSTCY1", "lstcy1"); //最終層中央Y1
        mapping.put("LSTCY2", "lstcy2"); //最終層中央Y2
        mapping.put("LSTMUX1", "lstmux1"); //最終層右上X1
        mapping.put("LSTMUX2", "lstmux2"); //最終層右上X2
        mapping.put("LSTMUY1", "lstmuy1"); //最終層右上Y1
        mapping.put("LSTMUY2", "lstmuy2"); //最終層右上Y2
        mapping.put("LSTMSX1", "lstmsx1"); //最終層右下X1
        mapping.put("LSTMSX2", "lstmsx2"); //最終層右下X2
        mapping.put("LSTMSY1", "lstmsy1"); //最終層右下Y1
        mapping.put("LSTMSY2", "lstmsy2"); //最終層右下Y2
        mapping.put("BIKO1", "biko1"); //備考1
        mapping.put("BIKO2", "biko2"); //備考2
        mapping.put("TOROKUNICHIJI", "torokunichiji"); //登録日時
        mapping.put("KOSINNICHIJI", "kosinnichiji"); //更新日時
        mapping.put("GOKI", "goki"); //号機ｺｰﾄﾞ
        mapping.put("TTANSISUUU", "ttansisuuu"); //特別端子枚数上
        mapping.put("TTANSISUUS", "ttansisuus"); //特別端子枚数下
        mapping.put("SHUNKANKANETSUJIKAN", "shunkankanetsujikan"); //瞬時加熱時間
        mapping.put("PETFILMSYURUI", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("KAATURYOKU", "kaaturyoku"); //加圧力
        mapping.put("GAIKANKAKUNIN", "gaikankakunin"); //外観確認
        mapping.put("SEKIJSSKIRIKAEICHI", "sekijsskirikaeichi"); //積層上昇速切替位置
        mapping.put("SEKIKKSKIRIKAEICHI", "sekikkskirikaeichi"); //積層下降速切替位置
        mapping.put("KAATUJIKAN", "kaatujikan"); //加圧時間
        mapping.put("TAPEHANSOUPITCH", "tapehansoupitch"); //ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
        mapping.put("TAPEHANSOUKAKUNIN", "tapehansoukakunin"); //ﾃｰﾌﾟ搬送目視確認
        mapping.put("EMAKUATSUSETTEI", "emakuatsusettei"); //電極膜厚設定
        mapping.put("ENEPPUFURYOU", "eneppufuryou"); //電極熱風風量
        mapping.put("EMAKUATSUAVE", "emakuatsuave"); //電極膜厚AVE
        mapping.put("EMAKUATSUMAX", "emakuatsumax"); //電極膜厚MAX
        mapping.put("EMAKUATSUMIN", "emakuatsumin"); //電極膜厚MIN
        mapping.put("NIJIMISOKUTEIPTN", "nijimisokuteiptn"); //にじみ量測定(ﾊﾟﾀｰﾝ間距離)
        mapping.put("PRNSAMPLEGAIKAN", "prnsamplegaikan"); //印刷ｻﾝﾌﾟﾙ外観確認
        mapping.put("PRNICHIYOHAKUNAGASA", "prnichiyohakunagasa"); //印刷位置余白長さ
        mapping.put("CTABLECLEARANCE", "ctableclearance"); //誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        mapping.put("CMAKUATSUSETTEI", "cmakuatsusettei"); //誘電体膜厚設定
        mapping.put("CSKSPEED", "cskspeed"); //誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("CNEPPUFURYOU", "cneppufuryou"); //誘電体熱風風量
        mapping.put("KABURIRYOU", "kaburiryou"); //被り量測定
        mapping.put("SGAIKAN", "sgaikan"); //積層中外観
        mapping.put("NIJIMISOKUTEISEKISOUGO", "nijimisokuteisekisougo"); //にじみ量測定(積層後)
        mapping.put("SEKISOUHINGAIKAN", "sekisouhingaikan"); //積層品外観
        mapping.put("SEKISOUZURE", "sekisouzure"); //積層ｽﾞﾚﾁｪｯｸ
        mapping.put("UWAJSSKIRIKAEICHI", "uwajsskirikaeichi"); //上端子上昇速切替位置
        mapping.put("SHITAKKSKIRIKAEICHI", "shitakkskirikaeichi"); //下端子下降速切替位置
        mapping.put("TINKSYURYUI", "tinksyuryui"); //ﾀｰｹﾞｯﾄｲﾝｸ種類
        mapping.put("TINKLOT", "tinklot"); //ﾀｰｹﾞｯﾄｲﾝｸLOT
        mapping.put("TGAIKAN", "tgaikan"); //ﾀｰｹﾞｯﾄ印刷外観
        mapping.put("STARTTANTOU", "starttantou"); //印刷積層開始担当者
        mapping.put("ENDTANTOU", "endtantou"); //印刷積層終了担当者
        mapping.put("TENDDAY", "tendday"); //ﾀｰｹﾞｯﾄ印刷終了日
        mapping.put("TENDTANTOU", "tendtantou"); //ﾀｰｹﾞｯﾄ印刷担当者
        mapping.put("SYORISETSUU", "syorisetsuu"); //処理ｾｯﾄ数
        mapping.put("RYOUHINSETSUU", "ryouhinsetsuu"); //良品ｾｯﾄ数
        mapping.put("HEADKOUKANTANTOU", "headkoukantantou"); //ﾍｯﾄﾞ交換者
        mapping.put("SEKISOUJOUKENTANTOU", "sekisoujoukentantou"); //積層条件者
        mapping.put("ESEIHANSETTANTOU", "eseihansettantou"); //E製版ｾｯﾄ者
        mapping.put("CSEIHANSETTANTOU", "cseihansettantou"); //C製版ｾｯﾄ者
        mapping.put("DANSASOKUTEITANTOU", "dansasokuteitantou"); //段差測定者
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("startkakunin", "startkakunin"); //印刷積層開始確認者
        mapping.put("TUMU", "tumu"); //ﾀｰｹﾞｯﾄ有無

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrRhaps>> beanHandler = new BeanListHandler<>(SrRhaps.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * [印刷積層RHAPS_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrRhaps> loadTmpSubSrRhaps(QueryRunner queryRunnerQcdb, String kojyo, String lotNo,
            String edaban, String rev) throws SQLException {

        String sql = "SELECT "
                + "KOJYO,LOTNO,EDABAN,EMAKUATSU1,EMAKUATSU2,EMAKUATSU3,EMAKUATSU4,EMAKUATSU5,EMAKUATSU6,"
                + "EMAKUATSU7,EMAKUATSU8,EMAKUATSU9,PTNDIST1,PTNDIST2,PTNDIST3,PTNDIST4,PTNDIST5,AWASERZ1,"
                + "AWASERZ2,AWASERZ3,AWASERZ4,AWASERZ5,AWASERZ6,AWASERZ7,AWASERZ8,AWASERZ9,KABURIHIDARIUEX1,"
                + "KABURIHIDARIUEX2,KABURIHIDARIUEY1,KABURIHIDARIUEY2,KABURIHIDARISITAX1,KABURIHIDARISITAX2,"
                + "KABURIHIDARISITAY1,KABURIHIDARISITAY2,KABURIHIDARICENTERX1,KABURIHIDARICENTERX2,"
                + "KABURIHIDARICENTERY1,KABURIHIDARICENTERY2,KABURIMIGIUEX1,KABURIMIGIUEX2,KABURIMIGIUEY1,"
                + "KABURIMIGIUEY2,KABURIMIGISITAX1,KABURIMIGISITAX2,KABURIMIGISITAY1,KABURIMIGISITAY2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sub_sr_rhaps "
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
        mapping.put("KOJYO", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("LOTNO", "lotno"); //ﾛｯﾄNo
        mapping.put("EDABAN", "edaban"); //枝番
        mapping.put("EMAKUATSU1", "emakuatsu1"); //電極膜厚1
        mapping.put("EMAKUATSU2", "emakuatsu2"); //電極膜厚2
        mapping.put("EMAKUATSU3", "emakuatsu3"); //電極膜厚3
        mapping.put("EMAKUATSU4", "emakuatsu4"); //電極膜厚4
        mapping.put("EMAKUATSU5", "emakuatsu5"); //電極膜厚5
        mapping.put("EMAKUATSU6", "emakuatsu6"); //電極膜厚6
        mapping.put("EMAKUATSU7", "emakuatsu7"); //電極膜厚7
        mapping.put("EMAKUATSU8", "emakuatsu8"); //電極膜厚8
        mapping.put("EMAKUATSU9", "emakuatsu9"); //電極膜厚9
        mapping.put("PTNDIST1", "ptndist1"); //ﾊﾟﾀｰﾝ間距離1
        mapping.put("PTNDIST2", "ptndist2"); //ﾊﾟﾀｰﾝ間距離2
        mapping.put("PTNDIST3", "ptndist3"); //ﾊﾟﾀｰﾝ間距離3
        mapping.put("PTNDIST4", "ptndist4"); //ﾊﾟﾀｰﾝ間距離4
        mapping.put("PTNDIST5", "ptndist5"); //ﾊﾟﾀｰﾝ間距離5
        mapping.put("AWASERZ1", "awaserz1"); //合わせ(RZ)1
        mapping.put("AWASERZ2", "awaserz2"); //合わせ(RZ)2
        mapping.put("AWASERZ3", "awaserz3"); //合わせ(RZ)3
        mapping.put("AWASERZ4", "awaserz4"); //合わせ(RZ)4
        mapping.put("AWASERZ5", "awaserz5"); //合わせ(RZ)5
        mapping.put("AWASERZ6", "awaserz6"); //合わせ(RZ)6
        mapping.put("AWASERZ7", "awaserz7"); //合わせ(RZ)7
        mapping.put("AWASERZ8", "awaserz8"); //合わせ(RZ)8
        mapping.put("AWASERZ9", "awaserz9"); //合わせ(RZ)9
        mapping.put("KABURIHIDARIUEX1", "kaburihidariuex1"); //被り量（μｍ）1
        mapping.put("KABURIHIDARIUEX2", "kaburihidariuex2"); //被り量（μｍ）2
        mapping.put("KABURIHIDARIUEY1", "kaburihidariuey1"); //被り量（μｍ）3
        mapping.put("KABURIHIDARIUEY2", "kaburihidariuey2"); //被り量（μｍ）4
        mapping.put("KABURIHIDARISITAX1", "kaburihidarisitax1"); //被り量（μｍ）5
        mapping.put("KABURIHIDARISITAX2", "kaburihidarisitax2"); //被り量（μｍ）6
        mapping.put("KABURIHIDARISITAY1", "kaburihidarisitay1"); //被り量（μｍ）7
        mapping.put("KABURIHIDARISITAY2", "kaburihidarisitay2"); //被り量（μｍ）8
        mapping.put("KABURIHIDARICENTERX1", "kaburihidaricenterx1"); //被り量（μｍ）9
        mapping.put("KABURIHIDARICENTERX2", "kaburihidaricenterx2"); //被り量（μｍ）10
        mapping.put("KABURIHIDARICENTERY1", "kaburihidaricentery1"); //被り量（μｍ）11
        mapping.put("KABURIHIDARICENTERY2", "kaburihidaricentery2"); //被り量（μｍ）12
        mapping.put("KABURIMIGIUEX1", "kaburimigiuex1"); //被り量（μｍ）13
        mapping.put("KABURIMIGIUEX2", "kaburimigiuex2"); //被り量（μｍ）14
        mapping.put("KABURIMIGIUEY1", "kaburimigiuey1"); //被り量（μｍ）15
        mapping.put("KABURIMIGIUEY2", "kaburimigiuey2"); //被り量（μｍ）16
        mapping.put("KABURIMIGISITAX1", "kaburimigisitax1"); //被り量（μｍ）17
        mapping.put("KABURIMIGISITAX2", "kaburimigisitax2"); //被り量（μｍ）18
        mapping.put("KABURIMIGISITAY1", "kaburimigisitay1"); //被り量（μｍ）19
        mapping.put("KABURIMIGISITAY2", "kaburimigisitay2"); //被り量（μｍ）20
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrRhaps>> beanHandler = new BeanListHandler<>(SubSrRhaps.class, rowProcessor);

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
            QueryRunner queryRunnerWip = new QueryRunner(processData.getDataSourceWip());
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

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

            // 印刷積層RHAPSデータ取得
            List<SrRhaps> srRhapsDataList = getSrRhapsData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srRhapsDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷積層RHAPS_ｻﾌﾞ画面データ取得
            List<SubSrRhaps> subSrRhapsDataList = getSubSrRhapsData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (subSrRhapsDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srRhapsDataList.get(0));

            // 電極膜厚画面データ設定 ※工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番は親ではなく自身の値を渡す。
            setInputItemDataSubFormC007(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

            // ﾊﾟﾀｰﾝ間距離画面データ設定
            setInputItemDataSubFormC008(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

            // 合わせ(RZ)画面データ設定
            setInputItemDataSubFormC009(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

            // 被り量(µm)画面データ設定
            setInputItemDataSubFormC010(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban, null, null);

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
     * @param srRhapsData 印刷積層RHAPS
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId, SrRhaps srRhapsData) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else if (srRhapsData != null) {
            // 元データが存在する場合元データより取得
            return getSrRhapsItemData(itemId, srRhapsData);
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
     * 印刷積層RHAPS_仮登録(tmp_sr_rhaps)登録処理
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
    private void insertTmpSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "INSERT INTO tmp_sr_rhaps ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
                + "CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,AINSATUSRZ2,AINSATUSRZ3,"
                + "AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,"
                + "FSTMSX1,FSTMSX2,FSTMSY1,FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,"
                + "LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,"
                + "TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,"
                + "KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,EMAKUATSUMAX,EMAKUATSUMIN,"
                + "NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,"
                + "TGAIKAN,STARTTANTOU,ENDTANTOU,TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,"
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision,deleteflag,startkakunin,TUMU"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterTmpSrRhaps(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList, null);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS_仮登録(tmp_sr_rhaps)更新処理
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
    private void updateTmpSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {

        String sql = "UPDATE tmp_sr_rhaps SET "
                + "KCPNO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,TTAPELOTNO = ?,TTapeSlipKigo = ?,TTapeRollNo1 = ?,"
                + "TTapeRollNo2 = ?,TTapeRollNo3 = ?,TTapeRollNo4 = ?,TTapeRollNo5 = ?,ETAPESYURUI = ?,ETAPEGLOT = ?,"
                + "ETAPELOT = ?,ETapeSlipKigo = ?,ETapeRollNo1 = ?,ETapeRollNo2 = ?,ETapeRollNo3 = ?,ETapeRollNo4 = ?,ETapeRollNo5 = ?,SPTUDENJIKAN = ?,"
                + "SKAATURYOKU = ?,SKHEADNO = ?,SUSSKAISUU = ?,ECPASTEMEI = ?,EPASTELOTNO = ?,EPASTENENDO = ?,EPASTEONDO = ?,ESEIHANMEI = ?,ESEIHANNO = ?,"
                + "ESEIMAISUU = ?,ECLEARANCE = ?,ESAATU = ?,ESKEEGENO = ?,ESKMAISUU = ?,ESKSPEED = ?,ESCCLEARANCE = ?,ESKKMJIKAN = ?,ELDSTART = ?,"
                + "EKANSOONDO = ?,CPASTELOTNO = ?,CPASTENENDO = ?,CPASTEONDO = ?,CSEIHANMEI = ?,CSEIHANNO = ?,CSEIMAISUU = ?,CSAATU = ?,"
                + "CSKEEGENO = ?,CSKMAISUU = ?,CSCCLEARANCE = ?,CSKKMJIKAN = ?,CSHIFTINSATU = ?,CLDSTART = ?,CKANSOONDO = ?,AINSATUSRZAVE = ?,"
                + "UTKAATURYOKU = ?,TICLEARANCE = ?,TISAATU = ?,TISKSPEED = ?,BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,GOKI = ?,SHUNKANKANETSUJIKAN = ?,"
                + "PETFILMSYURUI = ?,KAATURYOKU = ?,GAIKANKAKUNIN = ?,SEKIJSSKIRIKAEICHI = ?,SEKIKKSKIRIKAEICHI = ?,KAATUJIKAN = ?,TAPEHANSOUPITCH = ?,"
                + "TAPEHANSOUKAKUNIN = ?,EMAKUATSUSETTEI = ?,ENEPPUFURYOU = ?,EMAKUATSUAVE = ?,EMAKUATSUMAX = ?,EMAKUATSUMIN = ?,NIJIMISOKUTEIPTN = ?,"
                + "PRNSAMPLEGAIKAN = ?,PRNICHIYOHAKUNAGASA = ?,CTABLECLEARANCE = ?,CMAKUATSUSETTEI = ?,CSKSPEED = ?,CNEPPUFURYOU = ?,KABURIRYOU = ?,SGAIKAN = ?,"
                + "NIJIMISOKUTEISEKISOUGO = ?,SEKISOUHINGAIKAN = ?,SEKISOUZURE = ?,UWAJSSKIRIKAEICHI = ?,SHITAKKSKIRIKAEICHI = ?,TINKSYURYUI = ?,TINKLOT = ?,"
                + "TGAIKAN = ?,STARTTANTOU = ?,ENDTANTOU = ?,TENDDAY = ?,TENDTANTOU = ?,SYORISETSUU = ?,RYOUHINSETSUU = ?,HEADKOUKANTANTOU = ?,SEKISOUJOUKENTANTOU = ?,"
                + "ESEIHANSETTANTOU = ?,CSEIHANSETTANTOU = ?,DANSASOKUTEITANTOU = ?,revision = ?,deleteflag = ?,startkakunin = ?,TUMU = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        // 更新前の値を取得
        List<SrRhaps> srRhapsList = getSrRhapsData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrRhaps srRhaps = null;
        if (!srRhapsList.isEmpty()) {
            srRhaps = srRhapsList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrRhaps(false, newRev, 0, "", "", "", systemTime, itemList, srRhaps);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS_仮登録(tmp_sr_rhaps)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM tmp_sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND revision = ?";

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
     * 印刷積層RHAPS_仮登録(tmp_sr_rhaps)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srRhapsData 印刷積層RHAPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterTmpSrRhaps(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrRhaps srRhapsData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.KCPNO, srRhapsData)));  //KCPNO
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY, srRhapsData),
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME, srRhapsData))); //開始日時
        params.add(DBUtil.stringToDateObjectDefaultNull(
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY, srRhapsData),
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME, srRhapsData))); //終了日時

        if (isInsert) {
            params.add(null); //端子ﾃｰﾌﾟ種類
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_LOT_NO, srRhapsData)));  //端子ﾃｰﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU, srRhapsData)));  //端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo5
        if (isInsert) {
            params.add(null); //端子原料記号
            params.add(null); //下端子仕様
            params.add(null); //電極積層仕様
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_SHURUI, srRhapsData)));  //電極ﾃｰﾌﾟ種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT, srRhapsData)));  //電極ﾃｰﾌﾟ原料ﾛｯﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_LOT, srRhapsData)));  //電極ﾃｰﾌﾟﾛｯﾄ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_SLIP_KIGOU, srRhapsData)));  //電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO1, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, srRhapsData)));  //積層ﾌﾟﾚｽ通電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, srRhapsData)));  //積層加圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO, srRhapsData)));  //瞬時加熱ﾍｯﾄﾞNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU, srRhapsData)));  //SUS板使用回数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI, srRhapsData)));  //電極誘電体ﾍﾟｰｽﾄ名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO, srRhapsData)));  //電極ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_NENDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_ONDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, srRhapsData)));  //電極製版名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_NO, srRhapsData)));  //電極製版No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, srRhapsData)));  //電極製版枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_CLEARANCE, srRhapsData)));  //電極ｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SAATSU, srRhapsData)));  //電極差圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_NO, srRhapsData)));  //電極ｽｷｰｼﾞNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_MAISU, srRhapsData)));  //電極ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED, srRhapsData)));  //電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, srRhapsData)));  //電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, srRhapsData)));  //電極ｽｷｰｼﾞ下降待ち時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_L_D_STARTJI, srRhapsData)));  //電極L/Dｽﾀｰﾄ時
        if (isInsert) {
            params.add(null); //電極製版面積
            params.add(null); //電極膜厚
            params.add(null); //電極ｽﾗｲﾄﾞ量
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, srRhapsData)));  //電極乾燥温度
        if (isInsert) {
            params.add(null); //電極乾燥時間
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_NENDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_ONDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, srRhapsData)));  //誘電体製版名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, srRhapsData)));  //誘電体製版No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, srRhapsData)));  //誘電体製版枚数
        if (isInsert) {
            params.add(null); //誘電体ｸﾘｱﾗﾝｽ
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SAATSU, srRhapsData)));  //誘電体差圧
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_NO, srRhapsData)));  //誘電体ｽｷｰｼﾞNo
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_MAISU, srRhapsData)));  //誘電体ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE, srRhapsData)));  //誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, srRhapsData)));  //誘電体ｽｷｰｼﾞ下降待ち時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, srRhapsData)));  //誘電体ｼﾌﾄ印刷
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_L_D_STARTJI, srRhapsData)));  //誘電体L/Dｽﾀｰﾄ時
        if (isInsert) {
            params.add(null); //誘電体製版面積
            params.add(null);  //誘電体ｽﾗｲﾄﾞ量
        }
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO, srRhapsData)));  //誘電体乾燥温度
        if (isInsert) {
            params.add(null); //誘電体乾燥時間
            params.add(null); //誘電体膜厚
            params.add(null); //合わせ印刷ｻｲﾄﾞRZ1
            params.add(null); //合わせ印刷ｻｲﾄﾞRZ2
            params.add(null); //合わせ印刷ｻｲﾄﾞRZ3
            params.add(null); //合わせ印刷ｻｲﾄﾞRZ4
            params.add(null); //合わせ印刷ｻｲﾄﾞRZ5
        }

        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.AWASE_INSATSU_SIDE_RZ_AVE, srRhapsData)));  //合わせ印刷ｻｲﾄﾞRZAVE
        if (isInsert) {
            params.add(null); //上端子仕様
            params.add(null); //上端子通電時間
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU, srRhapsData)));  //上端子加圧力
        if (isInsert) {
            params.add(null); //積層体厚み補正
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_CLEARANCE, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SAATSU, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷差圧
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        if (isInsert) {
            params.add(null); //初層左上X1
            params.add(null); //初層左上X2
            params.add(null); //初層左上Y1
            params.add(null); //初層左上Y2
            params.add(null); //初層左下X1
            params.add(null); //初層左下X2
            params.add(null); //初層左下Y1
            params.add(null); //初層左下Y2
            params.add(null); //初層中央X1
            params.add(null); //初層中央X2
            params.add(null); //初層中央Y1
            params.add(null); //初層中央Y2
            params.add(null); //初層右上X1
            params.add(null); //初層右上X2
            params.add(null); //初層右上Y1
            params.add(null); //初層右上Y2
            params.add(null); //初層右下X1
            params.add(null); //初層右下X2
            params.add(null); //初層右下Y1
            params.add(null); //初層右下Y2
            params.add(null); //最終層左上X1
            params.add(null); //最終層左上X2
            params.add(null); //最終層左上Y1
            params.add(null); //最終層左上Y2
            params.add(null); //最終層左下X1
            params.add(null); //最終層左下X2
            params.add(null); //最終層左下Y1
            params.add(null); //最終層左下Y2
            params.add(null); //最終層中央X1
            params.add(null); //最終層中央X2
            params.add(null); //最終層中央Y1
            params.add(null); //最終層中央Y2
            params.add(null); //最終層右上X1
            params.add(null); //最終層右上X2
            params.add(null); //最終層右上Y1
            params.add(null); //最終層右上Y2
            params.add(null); //最終層右下X1
            params.add(null); //最終層右下X2
            params.add(null); //最終層右下Y1
            params.add(null); //最終層右下Y2
        }

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BIKOU1, srRhapsData)));  //備考1
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BIKOU2, srRhapsData)));  //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.GOUKI, srRhapsData)));  //号機ｺｰﾄﾞ
        if (isInsert) {
            params.add(null); //特別端子枚数上
            params.add(null); //特別端子枚数下
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SHUNJI_KANETSU_TIME, srRhapsData)));  //瞬時加熱時間
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.PET_FILM_SHURUI, srRhapsData)));  //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.KAATSU_ATSURYOKU, srRhapsData)));  //加圧力
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.GAIKAN_KAKUNIN1, srRhapsData), null));//外観確認
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //積層上昇速切替位置
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //積層下降速切替位置
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.KAATSU_TIME, srRhapsData)));  //加圧時間
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH, srRhapsData)));  //ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.TAPE_HANSOU_MOKUSHI_KAKUNIN, srRhapsData), null));  //ﾃｰﾌﾟ搬送目視確認
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, srRhapsData)));  //電極膜厚設定
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU, srRhapsData)));  //電極熱風風量
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_AVE, srRhapsData)));  //電極膜厚AVE
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_MAX, srRhapsData)));  //電極膜厚MAX
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_MIN, srRhapsData)));  //電極膜厚MIN
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_PTNKANKYORI, srRhapsData)));  //にじみ量測定(ﾊﾟﾀｰﾝ間距離)
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.INSATSU_SAMPLE_GAIKAN_KAKUNIN, srRhapsData), null));  //印刷ｻﾝﾌﾟﾙ外観確認
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.INSATSU_ICHI_YOHAKU_NAGASA, srRhapsData), null));  //印刷位置余白長さ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, srRhapsData)));  //誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI, srRhapsData)));  //誘電体膜厚設定
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, srRhapsData)));  //誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU, srRhapsData)));  //誘電体熱風風量
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.KABURIRYOU_SOKUTEI, srRhapsData), null));  //被り量測定
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.SEKISOUTYUU_GAIKAN, srRhapsData), null));  //積層中外観
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_SEKISOUGO, srRhapsData)));  //にじみ量測定(積層後)
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.SEKISOUHIN_GAIKAN, srRhapsData), null));  //積層品外観
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.SEKISOU_ZURE_CHECK, srRhapsData), null));  //積層ｽﾞﾚﾁｪｯｸ
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //上端子上昇速切替位置
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SHITA_TANSHI_KAKOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //下端子下降速切替位置
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INK_SHURUI, srRhapsData)));  //ﾀｰｹﾞｯﾄｲﾝｸ種類
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INK_LOT, srRhapsData)));  //ﾀｰｹﾞｯﾄｲﾝｸLOT
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_GAIKAN, srRhapsData), null));  //ﾀｰｹﾞｯﾄ印刷外観
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TANTOUSHA, srRhapsData)));  //印刷積層開始担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TANTOUSHA, srRhapsData)));  //印刷積層終了担当者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷終了日
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷担当者
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SHORI_SET_SU, srRhapsData)));  //処理ｾｯﾄ数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.RYOUHIN_SET_SU, srRhapsData)));  //良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.HEAD_KOUKANSHA, srRhapsData)));  //ﾍｯﾄﾞ交換者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_JOUKENSHA, srRhapsData)));  //積層条件者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.E_SEIHAN_SETSHA, srRhapsData)));  //E製版ｾｯﾄ者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.C_SEIHAN_SETSHA, srRhapsData)));  //C製版ｾｯﾄ者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DANSA_SOKUTEISHA, srRhapsData)));  //段差測定者
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, srRhapsData)));  //印刷積層開始確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_UMU, srRhapsData)));  //ﾀｰｹﾞｯﾄ有無

        return params;
    }

    /**
     * 印刷積層RHAPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rhaps)登録処理
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
    private void insertTmpSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_rhaps ("
                + "KOJYO,LOTNO,EDABAN,EMAKUATSU1,EMAKUATSU2,EMAKUATSU3,EMAKUATSU4,EMAKUATSU5,EMAKUATSU6,EMAKUATSU7,EMAKUATSU8,EMAKUATSU9,"
                + "PTNDIST1,PTNDIST2,PTNDIST3,PTNDIST4,PTNDIST5,AWASERZ1,AWASERZ2,AWASERZ3,AWASERZ4,AWASERZ5,AWASERZ6,AWASERZ7,AWASERZ8,AWASERZ9,"
                + "KABURIHIDARIUEX1,KABURIHIDARIUEX2,KABURIHIDARIUEY1,KABURIHIDARIUEY2,KABURIHIDARISITAX1,KABURIHIDARISITAX2,"
                + "KABURIHIDARISITAY1,KABURIHIDARISITAY2,KABURIHIDARICENTERX1,KABURIHIDARICENTERX2,KABURIHIDARICENTERY1,KABURIHIDARICENTERY2,"
                + "KABURIMIGIUEX1,KABURIMIGIUEX2,KABURIMIGIUEY1,KABURIMIGIUEY2,KABURIMIGISITAX1,KABURIMIGISITAX2,KABURIMIGISITAY1,KABURIMIGISITAY2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSubSrRhaps(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rhaps)更新処理
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
    private void updateTmpSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_rhaps SET "
                + "EMAKUATSU1 = ?,EMAKUATSU2 = ?,EMAKUATSU3 = ?,EMAKUATSU4 = ?,EMAKUATSU5 = ?,"
                + "EMAKUATSU6 = ?,EMAKUATSU7 = ?,EMAKUATSU8 = ?,EMAKUATSU9 = ?,"
                + "PTNDIST1 = ?,PTNDIST2 = ?,PTNDIST3 = ?,PTNDIST4 = ?,PTNDIST5 = ?,"
                + "AWASERZ1 = ?,AWASERZ2 = ?,AWASERZ3 = ?,AWASERZ4 = ?,AWASERZ5 = ?,"
                + "AWASERZ6 = ?,AWASERZ7 = ?,AWASERZ8 = ?,AWASERZ9 = ?,"
                + "KABURIHIDARIUEX1 = ?,KABURIHIDARIUEX2 = ?,KABURIHIDARIUEY1 = ?,KABURIHIDARIUEY2 = ?,"
                + "KABURIHIDARISITAX1 = ?,KABURIHIDARISITAX2 = ?,KABURIHIDARISITAY1 = ?,KABURIHIDARISITAY2 = ?,"
                + "KABURIHIDARICENTERX1 = ?,KABURIHIDARICENTERX2 = ?,KABURIHIDARICENTERY1 = ?,KABURIHIDARICENTERY2 = ?,"
                + "KABURIMIGIUEX1 = ?,KABURIMIGIUEX2 = ?,KABURIMIGIUEY1 = ?,KABURIMIGIUEY2 = ?,KABURIMIGISITAX1 = ?,KABURIMIGISITAX2 = ?,"
                + "KABURIMIGISITAY1 = ?,KABURIMIGISITAY2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterTmpSubSrRhaps(false, newRev, 0, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rhaps)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteTmpSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND revision = ?";

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
     * 印刷積層RHAPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rhaps)更新値パラメータ設定
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
    private List<Object> setUpdateParameterTmpSubSrRhaps(boolean isInsert, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C007 beanGXHDO101C007 = (GXHDO101C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C007);
        List<GXHDO101C007Model.DenkyokuMakuatsuData> denkyokuMakuatsuDataList = beanGXHDO101C007.getGxhdO101c007Model().getDenkyokuMakuatsuDataList();

        GXHDO101C008 beanGXHDO101C008 = (GXHDO101C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C008);
        List<GXHDO101C008Model.PtnKanKyoriData> ptnKanKyoriDataList = beanGXHDO101C008.getGxhdO101c008Model().getPtnKanKyoriDataList();

        GXHDO101C009 beanGXHDO101C009 = (GXHDO101C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C009);
        List<GXHDO101C009Model.AwaseRzData> awaseRzDataList = beanGXHDO101C009.getGxhdO101c009Model().getAwaseRzDataList();

        GXHDO101C010 beanGXHDO101C010 = (GXHDO101C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C010);
        GXHDO101C010Model gXHDO101C010Model = beanGXHDO101C010.getGxhdO101c010Model();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(0).getValue())); //電極膜厚1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(1).getValue())); //電極膜厚2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(2).getValue())); //電極膜厚3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(3).getValue())); //電極膜厚4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(4).getValue())); //電極膜厚5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(5).getValue())); //電極膜厚6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(6).getValue())); //電極膜厚7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(7).getValue())); //電極膜厚8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(denkyokuMakuatsuDataList.get(8).getValue())); //電極膜厚9
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKanKyoriDataList.get(0).getValue())); //ﾊﾟﾀｰﾝ間距離1
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKanKyoriDataList.get(1).getValue())); //ﾊﾟﾀｰﾝ間距離2
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKanKyoriDataList.get(2).getValue())); //ﾊﾟﾀｰﾝ間距離3
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKanKyoriDataList.get(3).getValue())); //ﾊﾟﾀｰﾝ間距離4
        params.add(DBUtil.stringToIntObjectDefaultNull(ptnKanKyoriDataList.get(4).getValue())); //ﾊﾟﾀｰﾝ間距離5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(0).getValue())); //合わせ(RZ)1
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(1).getValue())); //合わせ(RZ)2
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(2).getValue())); //合わせ(RZ)3
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(3).getValue())); //合わせ(RZ)4
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(4).getValue())); //合わせ(RZ)5
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(5).getValue())); //合わせ(RZ)6
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(6).getValue())); //合わせ(RZ)7
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(7).getValue())); //合わせ(RZ)8
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(awaseRzDataList.get(8).getValue())); //合わせ(RZ)9
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData1().getValue())); //被り量（μｍ）1
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData2().getValue())); //被り量（μｍ）2
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData3().getValue())); //被り量（μｍ）3
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData4().getValue())); //被り量（μｍ）4
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData5().getValue())); //被り量（μｍ）5
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData6().getValue())); //被り量（μｍ）6
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData7().getValue())); //被り量（μｍ）7
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData8().getValue())); //被り量（μｍ）8
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData9().getValue())); //被り量（μｍ）9
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData10().getValue())); //被り量（μｍ）10
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData11().getValue())); //被り量（μｍ）11
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData12().getValue())); //被り量（μｍ）12
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData13().getValue())); //被り量（μｍ）13
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData14().getValue())); //被り量（μｍ）14
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData15().getValue())); //被り量（μｍ）15
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData16().getValue())); //被り量（μｍ）16
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData17().getValue())); //被り量（μｍ）17
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData18().getValue())); //被り量（μｍ）18
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData19().getValue())); //被り量（μｍ）19
        params.add(DBUtil.stringToIntObjectDefaultNull(gXHDO101C010Model.getKaburiryouData20().getValue())); //被り量（μｍ）20

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
     * 印刷積層RHAPS(sr_rhaps)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param newRev 新Revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param tmpSrRhaps 仮登録データ
     * @throws SQLException 例外エラー
     */
    private void insertSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList, SrRhaps tmpSrRhaps) throws SQLException {

        String sql = "INSERT INTO sr_rhaps ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
                + "CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,AINSATUSRZ2,AINSATUSRZ3,"
                + "AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,"
                + "FSTMSX1,FSTMSX2,FSTMSY1,FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,"
                + "LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,"
                + "TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,"
                + "KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,EMAKUATSUMAX,EMAKUATSUMIN,"
                + "NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,"
                + "TGAIKAN,STARTTANTOU,ENDTANTOU,TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,"
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision,startkakunin,TUMU"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
                + ") ";

        List<Object> params = setUpdateParameterSrRhaps(true, newRev, kojyo, lotNo, edaban, systemTime, itemList, tmpSrRhaps);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS(sr_rhaps)更新処理
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
    private void updateSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, String jotaiFlg, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_rhaps SET "
                + "KCPNO = ?,KAISINICHIJI = ?,SYURYONICHIJI = ?,TTAPELOTNO = ?,TTapeSlipKigo = ?,TTapeRollNo1 = ?,"
                + "TTapeRollNo2 = ?,TTapeRollNo3 = ?,TTapeRollNo4 = ?,TTapeRollNo5 = ?,ETAPESYURUI = ?,ETAPEGLOT = ?,"
                + "ETAPELOT = ?,ETapeSlipKigo = ?,ETapeRollNo1 = ?,ETapeRollNo2 = ?,ETapeRollNo3 = ?,ETapeRollNo4 = ?,ETapeRollNo5 = ?,SPTUDENJIKAN = ?,"
                + "SKAATURYOKU = ?,SKHEADNO = ?,SUSSKAISUU = ?,ECPASTEMEI = ?,EPASTELOTNO = ?,EPASTENENDO = ?,EPASTEONDO = ?,ESEIHANMEI = ?,ESEIHANNO = ?,"
                + "ESEIMAISUU = ?,ECLEARANCE = ?,ESAATU = ?,ESKEEGENO = ?,ESKMAISUU = ?,ESKSPEED = ?,ESCCLEARANCE = ?,ESKKMJIKAN = ?,ELDSTART = ?,"
                + "EKANSOONDO = ?,CPASTELOTNO = ?,CPASTENENDO = ?,CPASTEONDO = ?,CSEIHANMEI = ?,CSEIHANNO = ?,CSEIMAISUU = ?,CSAATU = ?,"
                + "CSKEEGENO = ?,CSKMAISUU = ?,CSCCLEARANCE = ?,CSKKMJIKAN = ?,CSHIFTINSATU = ?,CLDSTART = ?,CKANSOONDO = ?,AINSATUSRZAVE = ?,"
                + "UTKAATURYOKU = ?,TICLEARANCE = ?,TISAATU = ?,TISKSPEED = ?,BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,GOKI = ?,SHUNKANKANETSUJIKAN = ?,"
                + "PETFILMSYURUI = ?,KAATURYOKU = ?,GAIKANKAKUNIN = ?,SEKIJSSKIRIKAEICHI = ?,SEKIKKSKIRIKAEICHI = ?,KAATUJIKAN = ?,TAPEHANSOUPITCH = ?,"
                + "TAPEHANSOUKAKUNIN = ?,EMAKUATSUSETTEI = ?,ENEPPUFURYOU = ?,EMAKUATSUAVE = ?,EMAKUATSUMAX = ?,EMAKUATSUMIN = ?,NIJIMISOKUTEIPTN = ?,"
                + "PRNSAMPLEGAIKAN = ?,PRNICHIYOHAKUNAGASA = ?,CTABLECLEARANCE = ?,CMAKUATSUSETTEI = ?,CSKSPEED = ?,CNEPPUFURYOU = ?,KABURIRYOU = ?,SGAIKAN = ?,"
                + "NIJIMISOKUTEISEKISOUGO = ?,SEKISOUHINGAIKAN = ?,SEKISOUZURE = ?,UWAJSSKIRIKAEICHI = ?,SHITAKKSKIRIKAEICHI = ?,TINKSYURYUI = ?,TINKLOT = ?,"
                + "TGAIKAN = ?,STARTTANTOU = ?,ENDTANTOU = ?,TENDDAY = ?,TENDTANTOU = ?,SYORISETSUU = ?,RYOUHINSETSUU = ?,HEADKOUKANTANTOU = ?,SEKISOUJOUKENTANTOU = ?,"
                + "ESEIHANSETTANTOU = ?,CSEIHANSETTANTOU = ?,DANSASOKUTEITANTOU = ?,revision = ?,startkakunin = ?,TUMU = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        // 更新前の値を取得
        List<SrRhaps> srRhapsList = getSrRhapsData(queryRunnerQcdb, rev.toPlainString(), jotaiFlg, kojyo, lotNo, edaban);
        SrRhaps srRhaps = null;
        if (!srRhapsList.isEmpty()) {
            srRhaps = srRhapsList.get(0);
        }

        //更新値設定
        List<Object> params = setUpdateParameterSrRhaps(false, newRev, "", "", "", systemTime, itemList, srRhaps);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS(sr_rhaps)更新値パラメータ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目リスト
     * @param srRhapsData 印刷積層RHAPSデータ
     * @return 更新パラメータ
     */
    private List<Object> setUpdateParameterSrRhaps(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban,
            Timestamp systemTime, List<FXHDD01> itemList, SrRhaps srRhapsData) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.KCPNO, srRhapsData)));  //KCPNO
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY, srRhapsData),
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME, srRhapsData))); //開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY, srRhapsData),
                getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME, srRhapsData))); //終了日時

        if (isInsert) {
            params.add(""); //端子ﾃｰﾌﾟ種類
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_LOT_NO, srRhapsData)));  //端子ﾃｰﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU, srRhapsData)));  //端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5, srRhapsData)));  //端子ﾃｰﾌﾟﾛｰﾙNo5
        if (isInsert) {
            params.add(""); //端子原料記号
            params.add(""); //下端子仕様
            params.add(0); //電極積層仕様
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_SHURUI, srRhapsData)));  //電極ﾃｰﾌﾟ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT, srRhapsData)));  //電極ﾃｰﾌﾟ原料ﾛｯﾄ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_LOT, srRhapsData)));  //電極ﾃｰﾌﾟﾛｯﾄ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_SLIP_KIGOU, srRhapsData)));  //電極ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO1, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo5
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, srRhapsData)));  //積層ﾌﾟﾚｽ通電時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, srRhapsData)));  //積層加圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO, srRhapsData)));  //瞬時加熱ﾍｯﾄﾞNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU, srRhapsData)));  //SUS板使用回数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI, srRhapsData)));  //電極誘電体ﾍﾟｰｽﾄ名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO, srRhapsData)));  //電極ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_NENDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_ONDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, srRhapsData)));  //電極製版名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_NO, srRhapsData)));  //電極製版No
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, srRhapsData)));  //電極製版枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_CLEARANCE, srRhapsData)));  //電極ｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SAATSU, srRhapsData)));  //電極差圧
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_NO, srRhapsData)));  //電極ｽｷｰｼﾞNo
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_MAISU, srRhapsData)));  //電極ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED, srRhapsData)));  //電極ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, srRhapsData)));  //電極ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, srRhapsData)));  //電極ｽｷｰｼﾞ下降待ち時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_L_D_STARTJI, srRhapsData)));  //電極L/Dｽﾀｰﾄ時
        if (isInsert) {
            params.add(0); //電極製版面積
            params.add(0); //電極膜厚
            params.add(0);  //電極ｽﾗｲﾄﾞ量
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, srRhapsData)));  //電極乾燥温度
        if (isInsert) {
            params.add(0); //電極乾燥時間
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_NENDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_ONDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, srRhapsData)));  //誘電体製版名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, srRhapsData)));  //誘電体製版No
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, srRhapsData)));  //誘電体製版枚数
        if (isInsert) {
            params.add(0); //誘電体ｸﾘｱﾗﾝｽ
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SAATSU, srRhapsData)));  //誘電体差圧
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_NO, srRhapsData)));  //誘電体ｽｷｰｼﾞNo
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_MAISU, srRhapsData)));  //誘電体ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE, srRhapsData)));  //誘電体ｽｸﾚｯﾊﾟｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, srRhapsData)));  //誘電体ｽｷｰｼﾞ下降待ち時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, srRhapsData)));  //誘電体ｼﾌﾄ印刷
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_L_D_STARTJI, srRhapsData)));  //誘電体L/Dｽﾀｰﾄ時
        if (isInsert) {
            params.add(0); //誘電体製版面積
            params.add(0);  //誘電体ｽﾗｲﾄﾞ量
        }
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO, srRhapsData)));  //誘電体乾燥温度
        if (isInsert) {
            params.add(0); //誘電体乾燥時間
            params.add(0); //誘電体膜厚
            params.add(0); //合わせ印刷ｻｲﾄﾞRZ1
            params.add(0); //合わせ印刷ｻｲﾄﾞRZ2
            params.add(0); //合わせ印刷ｻｲﾄﾞRZ3
            params.add(0); //合わせ印刷ｻｲﾄﾞRZ4
            params.add(0); //合わせ印刷ｻｲﾄﾞRZ5
        }

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.AWASE_INSATSU_SIDE_RZ_AVE, srRhapsData)));  //合わせ印刷ｻｲﾄﾞRZAVE
        if (isInsert) {
            params.add(0); //上端子仕様
            params.add(0); //上端子通電時間
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU, srRhapsData)));  //上端子加圧力
        if (isInsert) {
            params.add(0); //積層体厚み補正
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_CLEARANCE, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷ｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SAATSU, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷差圧
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        if (isInsert) {
            params.add(0); //初層左上X1
            params.add(0); //初層左上X2
            params.add(0); //初層左上Y1
            params.add(0); //初層左上Y2
            params.add(0); //初層左下X1
            params.add(0); //初層左下X2
            params.add(0); //初層左下Y1
            params.add(0); //初層左下Y2
            params.add(0); //初層中央X1
            params.add(0); //初層中央X2
            params.add(0); //初層中央Y1
            params.add(0); //初層中央Y2
            params.add(0); //初層右上X1
            params.add(0); //初層右上X2
            params.add(0); //初層右上Y1
            params.add(0); //初層右上Y2
            params.add(0); //初層右下X1
            params.add(0); //初層右下X2
            params.add(0); //初層右下Y1
            params.add(0); //初層右下Y2
            params.add(0); //最終層左上X1
            params.add(0); //最終層左上X2
            params.add(0); //最終層左上Y1
            params.add(0); //最終層左上Y2
            params.add(0); //最終層左下X1
            params.add(0); //最終層左下X2
            params.add(0); //最終層左下Y1
            params.add(0); //最終層左下Y2
            params.add(0); //最終層中央X1
            params.add(0); //最終層中央X2
            params.add(0); //最終層中央Y1
            params.add(0); //最終層中央Y2
            params.add(0); //最終層右上X1
            params.add(0); //最終層右上X2
            params.add(0); //最終層右上Y1
            params.add(0); //最終層右上Y2
            params.add(0); //最終層右下X1
            params.add(0); //最終層右下X2
            params.add(0); //最終層右下Y1
            params.add(0); //最終層右下Y2
        }

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BIKOU1, srRhapsData)));  //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BIKOU2, srRhapsData)));  //備考2
        if (isInsert) {
            params.add(systemTime); //登録日時
        }
        params.add(systemTime); //更新日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.GOUKI, srRhapsData)));  //号機ｺｰﾄﾞ
        if (isInsert) {
            params.add(0); //特別端子枚数上
            params.add(0); //特別端子枚数下
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SHUNJI_KANETSU_TIME, srRhapsData)));  //瞬時加熱時間
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.PET_FILM_SHURUI, srRhapsData)));  //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.KAATSU_ATSURYOKU, srRhapsData)));  //加圧力
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.GAIKAN_KAKUNIN1, srRhapsData), 9));//外観確認
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //積層上昇速切替位置
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //積層下降速切替位置
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.KAATSU_TIME, srRhapsData)));  //加圧時間
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH, srRhapsData)));  //ﾃｰﾌﾟ搬送送りﾋﾟｯﾁ
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.TAPE_HANSOU_MOKUSHI_KAKUNIN, srRhapsData), 9));  //ﾃｰﾌﾟ搬送目視確認
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, srRhapsData)));  //電極膜厚設定
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU, srRhapsData)));  //電極熱風風量
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_AVE, srRhapsData)));  //電極膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_MAX, srRhapsData)));  //電極膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_MAKUATSU_MIN, srRhapsData)));  //電極膜厚MIN
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_PTNKANKYORI, srRhapsData)));  //にじみ量測定(ﾊﾟﾀｰﾝ間距離)
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.INSATSU_SAMPLE_GAIKAN_KAKUNIN, srRhapsData), 9));  //印刷ｻﾝﾌﾟﾙ外観確認
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.INSATSU_ICHI_YOHAKU_NAGASA, srRhapsData), 9));  //印刷位置余白長さ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, srRhapsData)));  //誘電体ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI, srRhapsData)));  //誘電体膜厚設定
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, srRhapsData)));  //誘電体ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU, srRhapsData)));  //誘電体熱風風量
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.KABURIRYOU_SOKUTEI, srRhapsData), 9));  //被り量測定
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.SEKISOUTYUU_GAIKAN, srRhapsData), 9));  //積層中外観
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_SEKISOUGO, srRhapsData)));  //にじみ量測定(積層後)
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.SEKISOUHIN_GAIKAN, srRhapsData), 9));  //積層品外観
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.SEKISOU_ZURE_CHECK, srRhapsData), 9));  //積層ｽﾞﾚﾁｪｯｸ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //上端子上昇速切替位置
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SHITA_TANSHI_KAKOU_SOKU_KIRIKAE_ICHI, srRhapsData)));  //下端子下降速切替位置
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TARGET_INK_SHURUI, srRhapsData)));  //ﾀｰｹﾞｯﾄｲﾝｸ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TARGET_INK_LOT, srRhapsData)));  //ﾀｰｹﾞｯﾄｲﾝｸLOT
        params.add(getComboOkNgValue(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_GAIKAN, srRhapsData), 9));  //ﾀｰｹﾞｯﾄ印刷外観
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TANTOUSHA, srRhapsData)));  //印刷積層開始担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TANTOUSHA, srRhapsData)));  //印刷積層終了担当者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷終了日
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA, srRhapsData)));  //ﾀｰｹﾞｯﾄ印刷担当者
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SHORI_SET_SU, srRhapsData)));  //処理ｾｯﾄ数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.RYOUHIN_SET_SU, srRhapsData)));  //良品ｾｯﾄ数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.HEAD_KOUKANSHA, srRhapsData)));  //ﾍｯﾄﾞ交換者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_JOUKENSHA, srRhapsData)));  //積層条件者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.E_SEIHAN_SETSHA, srRhapsData)));  //E製版ｾｯﾄ者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.C_SEIHAN_SETSHA, srRhapsData)));  //C製版ｾｯﾄ者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DANSA_SOKUTEISHA, srRhapsData)));  //段差測定者
        params.add(newRev); //revision
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, srRhapsData)));  //印刷積層開始確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TARGET_UMU, srRhapsData)));  //ﾀｰｹﾞｯﾄ有無

        return params;
    }

    /**
     * 印刷積層RHAPS(sr_rhaps)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND revision = ?";

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
     * 印刷積層RHAPS_ｻﾌﾞ画面(sub_sr_rhaps)登録処理
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
    private void insertSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_rhaps ("
                + "KOJYO,LOTNO,EDABAN,EMAKUATSU1,EMAKUATSU2,EMAKUATSU3,EMAKUATSU4,EMAKUATSU5,EMAKUATSU6,EMAKUATSU7,EMAKUATSU8,EMAKUATSU9,"
                + "PTNDIST1,PTNDIST2,PTNDIST3,PTNDIST4,PTNDIST5,AWASERZ1,AWASERZ2,AWASERZ3,AWASERZ4,AWASERZ5,AWASERZ6,AWASERZ7,AWASERZ8,AWASERZ9,"
                + "KABURIHIDARIUEX1,KABURIHIDARIUEX2,KABURIHIDARIUEY1,KABURIHIDARIUEY2,KABURIHIDARISITAX1,KABURIHIDARISITAX2,"
                + "KABURIHIDARISITAY1,KABURIHIDARISITAY2,KABURIHIDARICENTERX1,KABURIHIDARICENTERX2,KABURIHIDARICENTERY1,KABURIHIDARICENTERY2,"
                + "KABURIMIGIUEX1,KABURIMIGIUEX2,KABURIMIGIUEY1,KABURIMIGIUEY2,KABURIMIGISITAX1,KABURIMIGISITAX2,KABURIMIGISITAY1,KABURIMIGISITAY2,"
                + "torokunichiji,kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSubSrRhaps(true, newRev, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS_ｻﾌﾞ画面(sub_sr_rhaps)更新処理
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
    private void updateSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "UPDATE sub_sr_rhaps SET "
                + "EMAKUATSU1 = ?,EMAKUATSU2 = ?,EMAKUATSU3 = ?,EMAKUATSU4 = ?,EMAKUATSU5 = ?,"
                + "EMAKUATSU6 = ?,EMAKUATSU7 = ?,EMAKUATSU8 = ?,EMAKUATSU9 = ?,"
                + "PTNDIST1 = ?,PTNDIST2 = ?,PTNDIST3 = ?,PTNDIST4 = ?,PTNDIST5 = ?,"
                + "AWASERZ1 = ?,AWASERZ2 = ?,AWASERZ3 = ?,AWASERZ4 = ?,AWASERZ5 = ?,"
                + "AWASERZ6 = ?,AWASERZ7 = ?,AWASERZ8 = ?,AWASERZ9 = ?,"
                + "KABURIHIDARIUEX1 = ?,KABURIHIDARIUEX2 = ?,KABURIHIDARIUEY1 = ?,KABURIHIDARIUEY2 = ?,"
                + "KABURIHIDARISITAX1 = ?,KABURIHIDARISITAX2 = ?,KABURIHIDARISITAY1 = ?,KABURIHIDARISITAY2 = ?,"
                + "KABURIHIDARICENTERX1 = ?,KABURIHIDARICENTERX2 = ?,KABURIHIDARICENTERY1 = ?,KABURIHIDARICENTERY2 = ?,"
                + "KABURIMIGIUEX1 = ?,KABURIMIGIUEX2 = ?,KABURIMIGIUEY1 = ?,KABURIMIGIUEY2 = ?,KABURIMIGISITAX1 = ?,KABURIMIGISITAX2 = ?,"
                + "KABURIMIGISITAY1 = ?,KABURIMIGISITAY2 = ?,kosinnichiji = ?,revision = ? "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND revision = ?";

        List<Object> params = setUpdateParameterSubSrRhaps(false, newRev, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷積層RHAPS_ｻﾌﾞ画面登録(tmp_sub_sr_rhaps)更新値パラメータ設定
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
    private List<Object> setUpdateParameterSubSrRhaps(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C007 beanGXHDO101C007 = (GXHDO101C007) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C007);
        List<GXHDO101C007Model.DenkyokuMakuatsuData> denkyokuMakuatsuDataList = beanGXHDO101C007.getGxhdO101c007Model().getDenkyokuMakuatsuDataList();

        GXHDO101C008 beanGXHDO101C008 = (GXHDO101C008) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C008);
        List<GXHDO101C008Model.PtnKanKyoriData> ptnKanKyoriDataList = beanGXHDO101C008.getGxhdO101c008Model().getPtnKanKyoriDataList();

        GXHDO101C009 beanGXHDO101C009 = (GXHDO101C009) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C009);
        List<GXHDO101C009Model.AwaseRzData> awaseRzDataList = beanGXHDO101C009.getGxhdO101c009Model().getAwaseRzDataList();

        GXHDO101C010 beanGXHDO101C010 = (GXHDO101C010) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C010);
        GXHDO101C010Model gXHDO101C010Model = beanGXHDO101C010.getGxhdO101c010Model();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(0).getValue())); //電極膜厚1
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(1).getValue())); //電極膜厚2
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(2).getValue())); //電極膜厚3
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(3).getValue())); //電極膜厚4
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(4).getValue())); //電極膜厚5
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(5).getValue())); //電極膜厚6
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(6).getValue())); //電極膜厚7
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(7).getValue())); //電極膜厚8
        params.add(DBUtil.stringToBigDecimalObject(denkyokuMakuatsuDataList.get(8).getValue())); //電極膜厚9
        params.add(DBUtil.stringToIntObject(ptnKanKyoriDataList.get(0).getValue())); //ﾊﾟﾀｰﾝ間距離1
        params.add(DBUtil.stringToIntObject(ptnKanKyoriDataList.get(1).getValue())); //ﾊﾟﾀｰﾝ間距離2
        params.add(DBUtil.stringToIntObject(ptnKanKyoriDataList.get(2).getValue())); //ﾊﾟﾀｰﾝ間距離3
        params.add(DBUtil.stringToIntObject(ptnKanKyoriDataList.get(3).getValue())); //ﾊﾟﾀｰﾝ間距離4
        params.add(DBUtil.stringToIntObject(ptnKanKyoriDataList.get(4).getValue())); //ﾊﾟﾀｰﾝ間距離5
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(0).getValue())); //合わせ(RZ)1
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(1).getValue())); //合わせ(RZ)2
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(2).getValue())); //合わせ(RZ)3
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(3).getValue())); //合わせ(RZ)4
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(4).getValue())); //合わせ(RZ)5
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(5).getValue())); //合わせ(RZ)6
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(6).getValue())); //合わせ(RZ)7
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(7).getValue())); //合わせ(RZ)8
        params.add(DBUtil.stringToBigDecimalObject(awaseRzDataList.get(8).getValue())); //合わせ(RZ)9
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData1().getValue())); //被り量（μｍ）1
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData2().getValue())); //被り量（μｍ）2
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData3().getValue())); //被り量（μｍ）3
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData4().getValue())); //被り量（μｍ）4
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData5().getValue())); //被り量（μｍ）5
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData6().getValue())); //被り量（μｍ）6
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData7().getValue())); //被り量（μｍ）7
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData8().getValue())); //被り量（μｍ）8
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData9().getValue())); //被り量（μｍ）9
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData10().getValue())); //被り量（μｍ）10
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData11().getValue())); //被り量（μｍ）11
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData12().getValue())); //被り量（μｍ）12
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData13().getValue())); //被り量（μｍ）13
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData14().getValue())); //被り量（μｍ）14
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData15().getValue())); //被り量（μｍ）15
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData16().getValue())); //被り量（μｍ）16
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData17().getValue())); //被り量（μｍ）17
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData18().getValue())); //被り量（μｍ）18
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData19().getValue())); //被り量（μｍ）19
        params.add(DBUtil.stringToIntObject(gXHDO101C010Model.getKaburiryouData20().getValue())); //被り量（μｍ）20

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
     * 印刷積層RHAPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rhaps)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー
     */
    private void deleteSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {

        String sql = "DELETE FROM sub_sr_rhaps "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? AND revision = ?";

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
     * [印刷積層RHAPS_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
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
                + "FROM tmp_sr_rhaps "
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
     * 印刷積層開始時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setInsatsuSekisouStartDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 印刷積層終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setInsatsuSekisouEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 印刷積層終了時間設定処理
     *
     * @param processDate 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData setTargetInsatsuEndDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY);
        if (StringUtil.isEmpty(itemDay.getValue())) {
            setDateTimeItem(itemDay, null, new Date());
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
     * @param srRhapsData 印刷積層RHAPSデータ
     * @return DB値
     */
    private String getSrRhapsItemData(String itemId, SrRhaps srRhapsData) {
        switch (itemId) {
            //KCPNo
            case GXHDO101B006Const.KCPNO:
                return StringUtil.nullToBlank(srRhapsData.getKcpno());
            //電極テープ種類
            case GXHDO101B006Const.DENKYOKU_TAPE_SHURUI:
                return StringUtil.nullToBlank(srRhapsData.getEtapesyurui());
            //電極テープロット
            case GXHDO101B006Const.DENKYOKU_TAPE_LOT:
                return StringUtil.nullToBlank(srRhapsData.getEtapelot());
            //電極テープスリップ記号
            case GXHDO101B006Const.DENKYOKU_TAPE_SLIP_KIGOU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeSlipKigo());
            //電極テープロールNo 1本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO1:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo1());
            //電極テープロールNo 2本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo2());
            //電極テープロールNo 3本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo3());
            //電極テープロールNo 4本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo4());
            //電極テープロールNo 5本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo5());
            //電極テープ原料lot
            case GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT:
                return StringUtil.nullToBlank(srRhapsData.getEtapeglot());
            //端子テープロットNo
            case GXHDO101B006Const.TANSHI_TAPE_LOT_NO:
                return StringUtil.nullToBlank(srRhapsData.getTtapelotno());
            //端子テープスリップ記号
            case GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU:
                return StringUtil.nullToBlank(srRhapsData.gettTapeSlipKigo());
            //端子テープロールNo 1本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo1());
            //端子テープロールNo 2本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo2());
            //端子テープロールNo 3本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo3());
            //端子テープロールNo 4本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo4());
            //端子テープロールNo 5本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo5());
            //電極ペーストロットNo
            case GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO:
                return StringUtil.nullToBlank(srRhapsData.getEpastelotno());
            //電極ペースト粘度
            case GXHDO101B006Const.DENKYOKU_PASTE_NENDO:
                return StringUtil.nullToBlank(srRhapsData.getEpastenendo());
            //電極ペースト温度
            case GXHDO101B006Const.DENKYOKU_PASTE_ONDO:
                return StringUtil.nullToBlank(srRhapsData.getEpasteondo());
            //電極製版名
            case GXHDO101B006Const.DENKYOKU_SEIHAN_MEI:
                return StringUtil.nullToBlank(srRhapsData.getEseihanmei());
            //電極誘電体ペースト名
            case GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI:
                return StringUtil.nullToBlank(srRhapsData.getEcpastemei());
            //誘電体ペーストロットNo
            case GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO:
                return StringUtil.nullToBlank(srRhapsData.getCpastelotno());
            //誘電体ペースト粘度
            case GXHDO101B006Const.YUUDENTAI_PASTE_NENDO:
                return StringUtil.nullToBlank(srRhapsData.getCpastenendo());
            //誘電体ペースト温度
            case GXHDO101B006Const.YUUDENTAI_PASTE_ONDO:
                return StringUtil.nullToBlank(srRhapsData.getCpasteondo());
            //誘電体製版名
            case GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI:
                return StringUtil.nullToBlank(srRhapsData.getCseihanmei());
            //ＰＥＴフィルム種類
            case GXHDO101B006Const.PET_FILM_SHURUI:
                return StringUtil.nullToBlank(srRhapsData.getPetfilmsyurui());
            //瞬時加熱時間
            case GXHDO101B006Const.SHUNJI_KANETSU_TIME:
                return StringUtil.nullToBlank(srRhapsData.getShunkankanetsujikan());
            //加圧力
            case GXHDO101B006Const.KAATSU_ATSURYOKU:
                return StringUtil.nullToBlank(srRhapsData.getKaaturyoku());
            //外観確認
            case GXHDO101B006Const.GAIKAN_KAKUNIN1:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getGaikankakunin()));
            //号機
            case GXHDO101B006Const.GOUKI:
                return StringUtil.nullToBlank(srRhapsData.getGoki());
            //積層プレス通電時間（瞬時加熱）
            case GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME:
                return StringUtil.nullToBlank(srRhapsData.getSptudenjikan());
            //瞬時加熱ヘッドNo
            case GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO:
                return StringUtil.nullToBlank(srRhapsData.getSkheadno());
            //SUS板使用回数
            case GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU:
                return StringUtil.nullToBlank(srRhapsData.getSusskaisuu());
            //積層加圧力
            case GXHDO101B006Const.SEKISOU_KAATSU_RYOKU:
                return StringUtil.nullToBlank(srRhapsData.getSkaaturyoku());
            //積層上昇速切替位置
            case GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI:
                return StringUtil.nullToBlank(srRhapsData.getSekijsskirikaeichi());
            //積層下降速切替位置
            case GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI:
                return StringUtil.nullToBlank(srRhapsData.getSekikkskirikaeichi());
            //加圧時間
            case GXHDO101B006Const.KAATSU_TIME:
                return StringUtil.nullToBlank(srRhapsData.getKaatujikan());
            //テープ搬送送りピッチ
            case GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH:
                return StringUtil.nullToBlank(srRhapsData.getTapehansoupitch());
            //テープ搬送目視確認
            case GXHDO101B006Const.TAPE_HANSOU_MOKUSHI_KAKUNIN:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getTapehansoukakunin()));
            //電極製版No
            case GXHDO101B006Const.DENKYOKU_SEIHAN_NO:
                return StringUtil.nullToBlank(srRhapsData.getEseihanno());
            //電極製版枚数
            case GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU:
                return StringUtil.nullToBlank(srRhapsData.getEseimaisuu());
            //電極スキージNo
            case GXHDO101B006Const.DENKYOKU_SQUEEGEE_NO:
                return StringUtil.nullToBlank(srRhapsData.getEskeegeno());
            //電極スキージ枚数
            case GXHDO101B006Const.DENKYOKU_SQUEEGEE_MAISU:
                return StringUtil.nullToBlank(srRhapsData.getEskmaisuu());
            //電極クリアランス
            case GXHDO101B006Const.DENKYOKU_CLEARANCE:
                return StringUtil.nullToBlank(srRhapsData.getEclearance());
            //電極差圧
            case GXHDO101B006Const.DENKYOKU_SAATSU:
                return StringUtil.nullToBlank(srRhapsData.getEsaatu());
            //電極膜厚設定
            case GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI:
                return StringUtil.nullToBlank(srRhapsData.getEmakuatsusettei());
            //電極スキージスピード
            case GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED:
                return StringUtil.nullToBlank(srRhapsData.getEskspeed());
            //電極スクレッパクリアランス
            case GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE:
                return StringUtil.nullToBlank(srRhapsData.getEscclearance());
            //電極スキージ下降待ち時間
            case GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME:
                return StringUtil.nullToBlank(srRhapsData.getEskkmjikan());
            //電極熱風風量
            case GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU:
                return StringUtil.nullToBlank(srRhapsData.getEneppufuryou());
            //電極乾燥温度
            case GXHDO101B006Const.DENKYOKU_KANSOU_ONDO:
                return StringUtil.nullToBlank(srRhapsData.getEkansoondo());
            //電極膜厚AVE
            case GXHDO101B006Const.DENKYOKU_MAKUATSU_AVE:
                return StringUtil.nullToBlank(srRhapsData.getEmakuatsuave());
            //電極膜厚MAX
            case GXHDO101B006Const.DENKYOKU_MAKUATSU_MAX:
                return StringUtil.nullToBlank(srRhapsData.getEmakuatsumax());
            //電極膜厚MIN
            case GXHDO101B006Const.DENKYOKU_MAKUATSU_MIN:
                return StringUtil.nullToBlank(srRhapsData.getEmakuatsumin());
            //電極L/Dスタート時
            case GXHDO101B006Const.DENKYOKU_L_D_STARTJI:
                return StringUtil.nullToBlank(srRhapsData.getEldstart());
            //にじみ量測定(ﾊﾟﾀｰﾝ間距離)
            case GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_PTNKANKYORI:
                return StringUtil.nullToBlank(srRhapsData.getNijimisokuteiptn());
            //印刷サンプル外観確認
            case GXHDO101B006Const.INSATSU_SAMPLE_GAIKAN_KAKUNIN:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getPrnsamplegaikan()));
            //印刷位置余白長さ
            case GXHDO101B006Const.INSATSU_ICHI_YOHAKU_NAGASA:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getPrnichiyohakunagasa()));
            //誘電体製版No
            case GXHDO101B006Const.YUUDENTAI_SEIHAN_NO:
                return StringUtil.nullToBlank(srRhapsData.getCseihanno());
            //誘電体製版枚数
            case GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU:
                return StringUtil.nullToBlank(srRhapsData.getCseimaisuu());
            //誘電体スキージNo
            case GXHDO101B006Const.YUUDENTAI_SQUEEGEE_NO:
                return StringUtil.nullToBlank(srRhapsData.getCskeegeno());
            //誘電体スキージ枚数
            case GXHDO101B006Const.YUUDENTAI_SQUEEGEE_MAISU:
                return StringUtil.nullToBlank(srRhapsData.getCskmaisuu());
            //誘電体テーブルクリアランス
            case GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE:
                return StringUtil.nullToBlank(srRhapsData.getCtableclearance());
            //誘電体差圧
            case GXHDO101B006Const.YUUDENTAI_SAATSU:
                return StringUtil.nullToBlank(srRhapsData.getCsaatu());
            //誘電体膜厚設定
            case GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI:
                return StringUtil.nullToBlank(srRhapsData.getCmakuatsusettei());
            //誘電体スキージスピード
            case GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED:
                return StringUtil.nullToBlank(srRhapsData.getCskspeed());
            //誘電体スクレッパクリアランス
            case GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE:
                return StringUtil.nullToBlank(srRhapsData.getCscclearance());
            //誘電体スキージ下降待ち時間
            case GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME:
                return StringUtil.nullToBlank(srRhapsData.getCskkmjikan());
            //誘電体シフト印刷
            case GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU:
                return StringUtil.nullToBlank(srRhapsData.getCshiftinsatu());
            //誘電体熱風風量
            case GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU:
                return StringUtil.nullToBlank(srRhapsData.getCneppufuryou());
            //誘電体乾燥温度
            case GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO:
                return StringUtil.nullToBlank(srRhapsData.getCkansoondo());
            //誘電体L/Dスタート時
            case GXHDO101B006Const.YUUDENTAI_L_D_STARTJI:
                return StringUtil.nullToBlank(srRhapsData.getCldstart());
            //合わせ印刷サイドRZAVE
            case GXHDO101B006Const.AWASE_INSATSU_SIDE_RZ_AVE:
                return StringUtil.nullToBlank(srRhapsData.getAinsatusrzave());
            //被り量測定
            case GXHDO101B006Const.KABURIRYOU_SOKUTEI:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getKaburiryou()));
            //積層中外観
            case GXHDO101B006Const.SEKISOUTYUU_GAIKAN:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getSgaikan()));
            //にじみ量測定(積層後)
            case GXHDO101B006Const.NIJIMIRYOU_SOKUTEI_SEKISOUGO:
                return StringUtil.nullToBlank(srRhapsData.getNijimisokuteisekisougo());
            //積層品外観
            case GXHDO101B006Const.SEKISOUHIN_GAIKAN:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getSekisouhingaikan()));
            //積層ズレチェック
            case GXHDO101B006Const.SEKISOU_ZURE_CHECK:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getSekisouzure()));
            //上端子加圧力
            case GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU:
                return StringUtil.nullToBlank(srRhapsData.getUtkaaturyoku());
            //上端子上昇速切替位置
            case GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI:
                return StringUtil.nullToBlank(srRhapsData.getUwajsskirikaeichi());
            //下端子下降速切替位置
            case GXHDO101B006Const.SHITA_TANSHI_KAKOU_SOKU_KIRIKAE_ICHI:
                return StringUtil.nullToBlank(srRhapsData.getShitakkskirikaeichi());
            //ターゲットインク種類
            case GXHDO101B006Const.TARGET_INK_SHURUI:
                return StringUtil.nullToBlank(srRhapsData.getTinksyuryui());
            //ターゲットインクLOT
            case GXHDO101B006Const.TARGET_INK_LOT:
                return StringUtil.nullToBlank(srRhapsData.getTinklot());
            //ターゲット印刷クリアランス
            case GXHDO101B006Const.TARGET_INSATSU_CLEARANCE:
                return StringUtil.nullToBlank(srRhapsData.getTiclearance());
            //ターゲット印刷スキージスピード
            case GXHDO101B006Const.TARGET_INSATSU_SQUEEGEE_SPEED:
                return StringUtil.nullToBlank(srRhapsData.getTiskspeed());
            //ターゲット印刷差圧
            case GXHDO101B006Const.TARGET_INSATSU_SAATSU:
                return StringUtil.nullToBlank(srRhapsData.getTisaatu());
            //ターゲット印刷外観
            case GXHDO101B006Const.TARGET_INSATSU_GAIKAN:
                return getComboOkNgText(StringUtil.nullToBlank(srRhapsData.getTgaikan()));
            //印刷積層開始日
            case GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY:
                return DateUtil.formattedTimestamp(srRhapsData.getKaisinichiji(), "yyMMdd");
            //印刷積層開始時間
            case GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME:
                return DateUtil.formattedTimestamp(srRhapsData.getKaisinichiji(), "HHmm");
            //印刷積層開始担当者
            case GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TANTOUSHA:
                return StringUtil.nullToBlank(srRhapsData.getStarttantou());
            //印刷積層終了日
            case GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY:
                return DateUtil.formattedTimestamp(srRhapsData.getSyuryonichiji(), "yyMMdd");
            //印刷積層終了時間
            case GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME:
                return DateUtil.formattedTimestamp(srRhapsData.getSyuryonichiji(), "HHmm");
            //印刷積層終了担当者
            case GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TANTOUSHA:
                return StringUtil.nullToBlank(srRhapsData.getEndtantou());
            //ターゲット印刷終了日
            case GXHDO101B006Const.TARGET_INSATSU_SHURYOU_DAY:
                return StringUtil.nullToBlank(srRhapsData.getTendday());
            //ターゲット印刷担当者
            case GXHDO101B006Const.TARGET_INSATSU_SHURYOU_TANTOUSHA:
                return StringUtil.nullToBlank(srRhapsData.getTendtantou());
            //処理セット数
            case GXHDO101B006Const.SHORI_SET_SU:
                return StringUtil.nullToBlank(srRhapsData.getSyorisetsuu());
            //良品セット数
            case GXHDO101B006Const.RYOUHIN_SET_SU:
                return StringUtil.nullToBlank(srRhapsData.getRyouhinsetsuu());
            //ヘッド交換者
            case GXHDO101B006Const.HEAD_KOUKANSHA:
                return StringUtil.nullToBlank(srRhapsData.getHeadkoukantantou());
            //積層条件者
            case GXHDO101B006Const.SEKISOU_JOUKENSHA:
                return StringUtil.nullToBlank(srRhapsData.getSekisoujoukentantou());
            //Ｅ製版セット者
            case GXHDO101B006Const.E_SEIHAN_SETSHA:
                return StringUtil.nullToBlank(srRhapsData.getEseihansettantou());
            //Ｃ製版セット者
            case GXHDO101B006Const.C_SEIHAN_SETSHA:
                return StringUtil.nullToBlank(srRhapsData.getCseihansettantou());
            //段差測定者
            case GXHDO101B006Const.DANSA_SOKUTEISHA:
                return StringUtil.nullToBlank(srRhapsData.getDansasokuteitantou());
            //備考1
            case GXHDO101B006Const.BIKOU1:
                return StringUtil.nullToBlank(srRhapsData.getBiko1());
            //備考2
            case GXHDO101B006Const.BIKOU2:
                return StringUtil.nullToBlank(srRhapsData.getBiko2());
            //印刷積層開始確認者
            case GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA:
                return StringUtil.nullToBlank(srRhapsData.getStartkakunin());
            //ﾀｰｹﾞｯﾄ有無
            case GXHDO101B006Const.TARGET_UMU:
                return StringUtil.nullToBlank(srRhapsData.getTumu());
            default:
                return null;

        }
    }

    /**
     * 印刷積層RHAPS_仮登録(tmp_sr_rhaps)登録処理(削除時)
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
    private void insertDeleteDataTmpSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {

        String sql = "INSERT INTO tmp_sr_rhaps ("
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
                + "CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,AINSATUSRZ2,AINSATUSRZ3,"
                + "AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,"
                + "FSTMSX1,FSTMSX2,FSTMSY1,FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,"
                + "LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,TOROKUNICHIJI,KOSINNICHIJI,GOKI,"
                + "TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,"
                + "KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,EMAKUATSUMAX,EMAKUATSUMIN,"
                + "NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,"
                + "TGAIKAN,STARTTANTOU,ENDTANTOU,TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,"
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,revision,deleteflag,startkakunin,TUMU"
                + ") SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo2,ETapeRollNo3,ETapeRollNo4,ETapeRollNo5,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,ESEIHANNO,ESEIMAISUU,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,CSEIHANNO,CSEIMAISUU,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
                + "CSHIFTINSATU,CLDSTART,CSEIMENSEKI,CSLIDERYO,CKANSOONDO,CKANSOJIKAN,CMAKUATU,AINSATUSRZ1,AINSATUSRZ2,AINSATUSRZ3,"
                + "AINSATUSRZ4,AINSATUSRZ5,AINSATUSRZAVE,UTSIYO,UTTUDENJIKAN,UTKAATURYOKU,STAHOSEI,TICLEARANCE,TISAATU,TISKSPEED,FSTHUX1,"
                + "FSTHUX2,FSTHUY1,FSTHUY2,FSTHSX1,FSTHSX2,FSTHSY1,FSTHSY2,FSTCX1,FSTCX2,FSTCY1,FSTCY2,FSTMUX1,FSTMUX2,FSTMUY1,FSTMUY2,"
                + "FSTMSX1,FSTMSX2,FSTMSY1,FSTMSY2,LSTHUX1,LSTHUX2,LSTHUY1,LSTHUY2,LSTHSX1,LSTHSX2,LSTHSY1,LSTHSY2,LSTCX1,LSTCX2,LSTCY1,"
                + "LSTCY2,LSTMUX1,LSTMUX2,LSTMUY1,LSTMUY2,LSTMSX1,LSTMSX2,LSTMSY1,LSTMSY2,BIKO1,BIKO2,?,?,GOKI,"
                + "TTANSISUUU,TTANSISUUS,SHUNKANKANETSUJIKAN,PETFILMSYURUI,KAATURYOKU,GAIKANKAKUNIN,SEKIJSSKIRIKAEICHI,SEKIKKSKIRIKAEICHI,"
                + "KAATUJIKAN,TAPEHANSOUPITCH,TAPEHANSOUKAKUNIN,EMAKUATSUSETTEI,ENEPPUFURYOU,EMAKUATSUAVE,EMAKUATSUMAX,EMAKUATSUMIN,"
                + "NIJIMISOKUTEIPTN,PRNSAMPLEGAIKAN,PRNICHIYOHAKUNAGASA,CTABLECLEARANCE,CMAKUATSUSETTEI,CSKSPEED,CNEPPUFURYOU,KABURIRYOU,"
                + "SGAIKAN,NIJIMISOKUTEISEKISOUGO,SEKISOUHINGAIKAN,SEKISOUZURE,UWAJSSKIRIKAEICHI,SHITAKKSKIRIKAEICHI,TINKSYURYUI,TINKLOT,"
                + "TGAIKAN,STARTTANTOU,ENDTANTOU,TENDDAY,TENDTANTOU,SYORISETSUU,RYOUHINSETSUU,HEADKOUKANTANTOU,SEKISOUJOUKENTANTOU,ESEIHANSETTANTOU,"
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,?,?,startkakunin,TUMU "
                + "FROM sr_rhaps "
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
     * 印刷積層RHAPS_ｻﾌﾞ画面仮登録(tmp_sub_sr_rhaps)登録処理(削除時)
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
    private void insertDeleteDataTmpSubSrRhaps(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_rhaps ("
                + "KOJYO,LOTNO,EDABAN,EMAKUATSU1,EMAKUATSU2,EMAKUATSU3,EMAKUATSU4,"
                + "EMAKUATSU5,EMAKUATSU6,EMAKUATSU7,EMAKUATSU8,EMAKUATSU9,"
                + "PTNDIST1,PTNDIST2,PTNDIST3,PTNDIST4,PTNDIST5,"
                + "AWASERZ1,AWASERZ2,AWASERZ3,AWASERZ4,AWASERZ5,"
                + "AWASERZ6,AWASERZ7,AWASERZ8,AWASERZ9,"
                + "KABURIHIDARIUEX1,KABURIHIDARIUEX2,KABURIHIDARIUEY1,KABURIHIDARIUEY2,"
                + "KABURIHIDARISITAX1,KABURIHIDARISITAX2,KABURIHIDARISITAY1,KABURIHIDARISITAY2,"
                + "KABURIHIDARICENTERX1,KABURIHIDARICENTERX2,KABURIHIDARICENTERY1,KABURIHIDARICENTERY2,"
                + "KABURIMIGIUEX1,KABURIMIGIUEX2,KABURIMIGIUEY1,KABURIMIGIUEY2,"
                + "KABURIMIGISITAX1,KABURIMIGISITAX2,KABURIMIGISITAY1,KABURIMIGISITAY2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag"
                + ") SELECT "
                + "KOJYO,LOTNO,EDABAN,EMAKUATSU1,EMAKUATSU2,EMAKUATSU3,EMAKUATSU4,"
                + "EMAKUATSU5,EMAKUATSU6,EMAKUATSU7,EMAKUATSU8,EMAKUATSU9,"
                + "PTNDIST1,PTNDIST2,PTNDIST3,PTNDIST4,PTNDIST5,"
                + "AWASERZ1,AWASERZ2,AWASERZ3,AWASERZ4,AWASERZ5,"
                + "AWASERZ6,AWASERZ7,AWASERZ8,AWASERZ9,"
                + "KABURIHIDARIUEX1,KABURIHIDARIUEX2,KABURIHIDARIUEY1,KABURIHIDARIUEY2,"
                + "KABURIHIDARISITAX1,KABURIHIDARISITAX2,KABURIHIDARISITAY1,KABURIHIDARISITAY2,"
                + "KABURIHIDARICENTERX1,KABURIHIDARICENTERX2,KABURIHIDARICENTERY1,KABURIHIDARICENTERY2,"
                + "KABURIMIGIUEX1,KABURIMIGIUEX2,KABURIMIGIUEY1,KABURIMIGIUEY2,"
                + "KABURIMIGISITAX1,KABURIMIGISITAX2,KABURIMIGISITAY1,KABURIMIGISITAY2,"
                + "?,?,?,? "
                + "FROM sub_sr_rhaps "
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
     * コンボボックス(OK,NG)Value値取得
     *
     * @param comboText コンボボックステキスト
     * @return コンボボックスValue値
     */
    private Integer getComboOkNgValue(String comboText, Integer defaultValue) {
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
     * コンボボックス(OK,NG)テキスト値取得
     *
     * @param comboValue コンボボックスValue値
     * @return コンボボックステキスト値
     */
    private String getComboOkNgText(String comboValue) {
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
     * 元データ設定処理
     *
     * @param processData 処理制御データ
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param formId 画面ID
     * @param motoLotno 参照元ﾛｯﾄNo(ﾌﾙ桁)
     * @param sakiKojyo 工場ｺｰﾄﾞ
     * @param sakilotNo8 ﾛｯﾄNo(8桁)
     * @param sakiEdaban 枝番
     * @param sakiEdaban 設計No
     * @return 元データ設定 true(成功) false(失敗)
     * @throws SQLException 例外
     */
    private boolean setSanshouMotoLotData(ProcessData processData, QueryRunner queryRunnerQcdb, QueryRunner queryRunnerDoc, String formId, String motoLotno,
            String sakiKojyo, String sakilotNo8, String sakiEdaban, String sekkeino) throws SQLException {

        // 元ﾛｯﾄを分解
        String motoKojyo = motoLotno.substring(0, 3);
        String motoLotNo8 = motoLotno.substring(3, 11);
        String motoEdaban = motoLotno.substring(11, 14);

        Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, motoKojyo, motoLotNo8, motoEdaban, formId);
        String rev = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev"));

        // 印刷積層RHAPSデータ取得
        List<SrRhaps> srSpRhapsDataList = getSrRhapsData(queryRunnerQcdb, rev, "1", motoKojyo, motoLotNo8, motoEdaban);
        if (srSpRhapsDataList.isEmpty()) {
            //該当データが取得できなかった処理失敗としてリターンする
            return false;
        }

        // 印刷積層RHAPS_ｻﾌﾞ画面データ取得
        List<SubSrRhaps> subSrRhapsDataList = getSubSrRhapsData(queryRunnerQcdb, rev, "1", motoKojyo, motoLotNo8, motoEdaban);
        if (subSrRhapsDataList.isEmpty()) {
            //該当データが取得できなかった処理失敗としてリターンする
            return false;
        }

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpRhapsDataList.get(0));

        // 電極膜厚画面データ設定
        setInputItemDataSubFormC007(subSrRhapsDataList.get(0), sakiKojyo, sakilotNo8, sakiEdaban);

        // ﾊﾟﾀｰﾝ間距離画面データ設定
        setInputItemDataSubFormC008(subSrRhapsDataList.get(0), sakiKojyo, sakilotNo8, sakiEdaban);

        // 合わせ(RZ)画面データ設定
        setInputItemDataSubFormC009(subSrRhapsDataList.get(0), sakiKojyo, sakilotNo8, sakiEdaban);

        // 被り量(µm)画面データ設定
        setInputItemDataSubFormC010(subSrRhapsDataList.get(0), sakiKojyo, sakilotNo8, sakiEdaban, sekkeino, queryRunnerQcdb);

        return true;
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
     * 関連付けMapに定義されている項目が用途データで['CT','CB']が存在しない場合エラーとしエラー情報を返す
     * ※関連付けMapには設計データに持っている項目IDが設定されていること
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param mapYoutoAssociation 用途関連付けMap
     * @param youtoType 用途データ型
     * @param mapSekkeiAssociation 設計データ関連付けMap
     * @return エラーメッセージリスト
     */
    public List<String> checkYoutoItems(ProcessData processData, Map<String, String> sekkeiData,
            Map<String, String> mapYoutoAssociation, String youtoType, Map<String, String> mapSekkeiAssociation) {

        List<String> retListData = new ArrayList<>();
        boolean checkExistFlag = false;
        boolean checkCTCBExistFlag = false;
        String sekkeiDataKey = "";

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

        if (!checkExistFlag) {
            retListData.add("ERROR");
            if (null != youtoType) {
                switch (youtoType) {
                    case "CT":
                        retListData.add(MessageUtil.getMessage("XHD-000100"));
                        break;
                    case "CB":
                        retListData.add(MessageUtil.getMessage("XHD-000101"));
                        break;
                    case "ST":
                        retListData.add(MessageUtil.getMessage("XHD-000186"));
                        break;
                    case "SB":
                        retListData.add(MessageUtil.getMessage("XHD-000187"));
                        break;
                    default:
                        break;
                }
            }
            return retListData;
        }

        String sekkeiDataRowNo = sekkeiDataKey.substring(5);
        String syuruiDataKey = "SYURUI" + sekkeiDataRowNo;
        String atumiDataKey = "ATUMI" + sekkeiDataRowNo;
        String maisuuDataKey = "MAISUU" + sekkeiDataRowNo;

        String checkSyuruiData = StringUtil.nullToBlank(sekkeiData.get(syuruiDataKey));
        String checkAtumiData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(atumiDataKey)));
        String checkMaisuuData = StringUtil.nullToBlank(String.valueOf(sekkeiData.get(maisuuDataKey)));

        if ("CT".equals(youtoType) || "CB".equals(youtoType) || "ST".equals(youtoType) || "SB".equals(youtoType)) {
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
                if (!checkCTCBExistFlag) {
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
                if (!checkCTCBExistFlag) {
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

            if (!checkCTCBExistFlag) {
                retListData.add("OK");
                String retCTCBValue = checkSyuruiData + "  " + checkAtumiData + "μm×" + checkMaisuuData + "枚";
                retListData.add(retCTCBValue);
            }
            return retListData;
        }

        return retListData;
    }


}
