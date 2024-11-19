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
import jp.co.kccs.xhd.common.ErrorListMessage;
import jp.co.kccs.xhd.common.InitMessage;
import jp.co.kccs.xhd.common.KikakuError;
import jp.co.kccs.xhd.db.model.FXHDD01;
import jp.co.kccs.xhd.db.model.SrRhaps;
import jp.co.kccs.xhd.db.model.SubSrRhaps;
import jp.co.kccs.xhd.model.GXHDO101C007Model;
import jp.co.kccs.xhd.model.GXHDO101C008Model;
import jp.co.kccs.xhd.model.GXHDO101C009Model;
import jp.co.kccs.xhd.model.GXHDO101C010Model;
import jp.co.kccs.xhd.model.GXHDO101C020Model;
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
import jp.co.kccs.xhd.util.SubFormUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.MapListHandler;

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
 * 変更日	2020/09/21<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	KCSS D.Yanagida<br>
 * 変更理由	ロット混合対応<br>
 * <br>
 * 変更日	2020/10/15<br>
 * 計画書No	MB2008-DK001<br>
 * 変更者	863 zhangjy<br>
 * 変更理由	前工程WIPボタンロジックを追加<br>
 * <br>
 * 変更日       2022/03/10<br>
 * 計画書No     MB2202-D013<br>
 * 変更者       KCSS WXF<br>
 * 変更理由     仕様変更対応<br>
 * <br>
 * 変更日	2022/06/14<br>
 * 計画書No	MB2205-D010<br>
 * 変更者	KCSS wxf<br>
 * 変更理由	項目追加<br>
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
                    GXHDO101B006Const.BTN_DATACOOPERATION_TOP,
                    GXHDO101B006Const.BTN_INSERT_BOTTOM,
                    GXHDO101B006Const.BTN_DELETE_TOP,
                    GXHDO101B006Const.BTN_DELETE_BOTTOM,
                    GXHDO101B006Const.BTN_UPDATE_TOP,
                    GXHDO101B006Const.BTN_UPDATE_BOTTOM,
                    GXHDO101B006Const.BTN_DATACOOPERATION_BOTTOM));

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

                // 前工程WIP取込ｻﾌﾞ画面仮登録登録処理
                insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

            } else {

                // 印刷積層RHAPS_仮登録更新処理
                updateTmpSrRhaps(queryRunnerQcdb, conQcdb, rev, processData.getInitJotaiFlg(), newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷積層RHAPS_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrRhaps(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

                // 前工程WIP取込_ｻﾌﾞ画面仮登録更新処理
                updateTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);
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
                deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban);
            }

            // 印刷積層RHAPS_登録処理
            insertSrRhaps(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList(), tmpSrRhaps);

            // 印刷積層RHAPS_ｻﾌﾞ画面登録処理
            insertSubSrRhaps(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

            // 前工程WIP取込ｻﾌﾞ画面登録処理
            insertSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

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
     * 部材在庫の重量ﾃﾞｰﾀ連携
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doPMLA0212(ProcessData processData) {
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String tantoshaCd = StringUtil.nullToBlank(session.getAttribute("tantoshaCd"));
        // 部材在庫の重量ﾃﾞｰﾀ連携
        String responseResult = doPMLA0212Save(processData, tantoshaCd);
        if (!"ok".equals(responseResult)) {
            return processData;
        }
        // 後続処理メソッド設定
        processData.setMethod("");
        // 完了メッセージとコールバックパラメータを設定
        processData.setCompMessage("登録しました。");
        processData.setCollBackParam("complete");
        return processData;
    }

    /**
     * 部材在庫の重量ﾃﾞｰﾀ連携
     *
     * @param processData 処理制御データ
     * @param tantoshaCd 更新者
     * @return レスポンスデータ
     */
    private String doPMLA0212Save(ProcessData processData, String tantoshaCd) {
        ArrayList<String> errorItemList = new ArrayList<>();
        // 部材在庫No_電極に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO101B006Const.BUZAIZAIKONODENKYOKU, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, errorItemList);
        // 部材在庫No_誘電体に値が入っている場合、以下の内容を元にAPIを呼び出す
        doCallPmla0212Api(processData, tantoshaCd, GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, errorItemList);
        // 上記の処理でｴﾗｰが発生した場合、画面にエラーダイアログを出力する。
        if (!errorItemList.isEmpty()) {
            ErrorListMessage errorListMessageList = new ErrorListMessage();
            errorListMessageList.setResultMessageList(errorItemList);
            errorListMessageList.setTitleMessage(MessageUtil.getMessage("infoMsg"));
            processData.setErrorListMessage(errorListMessageList);
            return "error";
        }
        return "ok";
    }

    /**
     * 部材在庫管理を参照【PMLA0212_部材在庫ﾃﾞｰﾀ更新】
     *
     * @param processData 処理制御データ
     * @param tantoshaCd 更新者
     * @param buzaizaikonoStr 部材在庫No
     * @param siyoumaisuuStr 使用枚数
     * @param errorItemList エラー情報
     * @return レスポンスデータ
     */
    private void doCallPmla0212Api(ProcessData processData, String tantoshaCd, String buzaizaikonoStr, String siyoumaisuuStr, ArrayList<String> errorItemList) {

        // 部材在庫Noに値が入っている場合、以下の内容を元にAPIを呼び出す
        FXHDD01 itemFxhdd01Buzaizaikono = getItemRow(processData.getItemList(), buzaizaikonoStr);
        if (itemFxhdd01Buzaizaikono == null || StringUtil.isEmpty(itemFxhdd01Buzaizaikono.getValue())) {
            return;
        }
        // 部材在庫Noの値
        String buzaizaikonoValue = StringUtil.blankToNull(itemFxhdd01Buzaizaikono.getValue());
        // 使用枚数
        FXHDD01 itemFxhdd01Siyoumaisuu = getItemRow(processData.getItemList(), siyoumaisuuStr);
        String siyoumaisuuValue = null;
        if (itemFxhdd01Siyoumaisuu != null) {
            // 使用枚数の値
            siyoumaisuuValue = StringUtil.blankToNull(itemFxhdd01Siyoumaisuu.getValue());
        }
        // ﾛｯﾄNo
        FXHDD01 itemFxhdd01Lotno = getItemRow(processData.getItemList(), GXHDO101B006Const.LOTNO);
        String lotnoValue = null;
        if (itemFxhdd01Lotno != null) {
            // ﾛｯﾄNoの値
            lotnoValue = StringUtil.blankToNull(itemFxhdd01Lotno.getValue());
        }
        ArrayList<String> paramsList = new ArrayList<>();
        paramsList.add(buzaizaikonoValue);
        paramsList.add(tantoshaCd);
        paramsList.add("GXHDO101B006");
        paramsList.add(null);
        paramsList.add(null);
        paramsList.add(siyoumaisuuValue);
        paramsList.add(null);
        paramsList.add(lotnoValue);

        try {
            QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
            // 「/api/PMLA0212/doSave」APIを呼び出す
            String responseResult = CommonUtil.doRequestPmla0212Save(queryRunnerDoc, paramsList);
            if (!"ok".equals(responseResult)) {
                if (!errorItemList.isEmpty()) {
                    errorItemList.add("　");
                }
                errorItemList.add(MessageUtil.getMessage("buzailotnoErrorMsg", itemFxhdd01Buzaizaikono.getLabel1()));
                errorItemList.add("　" + buzaizaikonoValue);
            }
        } catch (Exception ex) {
            ErrUtil.outputErrorLog(itemFxhdd01Buzaizaikono.getLabel1() + "のﾃﾞｰﾀ連携処理エラー発生", ex, LOGGER);
            if (!errorItemList.isEmpty()) {
                errorItemList.add("　");
            }
            errorItemList.add(MessageUtil.getMessage("buzailotnoErrorMsg", itemFxhdd01Buzaizaikono.getLabel1()));
            errorItemList.add("　" + buzaizaikonoValue);
        }
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

            // 前工程WIP取込_ｻﾌﾞ画面更新処理
            updateSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

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

            // 前工程WIP取込_ｻﾌﾞ画面削除処理
            deleteSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo8, edaban, systemTime);

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
                        GXHDO101B006Const.BTN_DELETE_BOTTOM,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHODENKYOKU_TOP,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHODENKYOKU_BOTTOM,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHOYUUDENTAI_TOP,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHOYUUDENTAI_BOTTOM
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
                        GXHDO101B006Const.BTN_INSERT_BOTTOM,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHODENKYOKU_TOP,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHODENKYOKU_BOTTOM,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHOYUUDENTAI_TOP,
                        GXHDO101B006Const.BTN_BUZAIZAIKOJYOHOYUUDENTAI_BOTTOM
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
            // 前工程WIP
            case GXHDO101B006Const.BTN_WIP_IMPORT_TOP:
            case GXHDO101B006Const.BTN_WIP_IMPORT_BOTTOM:
                method = "openWipImport";
                break;
            // 部材在庫情報(電極)
            case GXHDO101B006Const.BTN_BUZAIZAIKOJYOHODENKYOKU_TOP:
            case GXHDO101B006Const.BTN_BUZAIZAIKOJYOHODENKYOKU_BOTTOM:
                method = "doBuzaizaikojyohoDenkyokuSyori";
                break;
            // 部材在庫情報(誘電体)
            case GXHDO101B006Const.BTN_BUZAIZAIKOJYOHOYUUDENTAI_TOP:
            case GXHDO101B006Const.BTN_BUZAIZAIKOJYOHOYUUDENTAI_BOTTOM:
                method = "doBuzaizaikojyohoYuudentaiSyori";
                break;
            // 設備ﾃﾞｰﾀ連携
            case GXHDO101B006Const.BTN_DATACOOPERATION_TOP:
            case GXHDO101B006Const.BTN_DATACOOPERATION_BOTTOM:
                method = "doDataCooperationSyori";
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
    private ProcessData setInitData(ProcessData processData) throws SQLException {

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
                ueCoverTape1, ueCoverTape2, shitaCoverTape1, shitaCoverTape2, queryRunnerQcdb);

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
            String ueCoverTape1, String ueCoverTape2, String shitaCoverTape1, String shitaCoverTape2, QueryRunner queryRunnerQcdb) throws SQLException {

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
//        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, StringUtil.nullToBlank(loadJoken(StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")),"設計仕様","電極","電極製版名",queryRunnerQcdb)));

        // 電極製版仕様
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_SHIYOU, "");

        // 電極誘電体ペースト
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE, "");

        // 誘電体製版名
//        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, StringUtil.nullToBlank(loadJoken(StringUtil.nullToBlank(sekkeiData.get("SEKKEINO")),"設計仕様","誘電体","誘電体製版名",queryRunnerQcdb)));

        // 誘電体製版仕様
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_SIYOU, "");

        // 積層スライド量
        this.setItemData(processData, GXHDO101B006Const.SEKISOU_SLIDE_RYOU, "");

        // 電極スライド量
   //     this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, "");

        // 誘電体スライド量
    //    this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU, "");

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
                
                // 前工程WIP取込画面データ設定
                setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

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
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, edaban, jotaiFlg);

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
        //電極ﾃｰﾌﾟﾛｰﾙNo 1本目開始
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO1KAISHI, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO1KAISHI, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 1本目終了
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO1SYURYOU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO1SYURYOU, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 1本目印刷
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO1INSATSU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO1INSATSU, srRhapsData));
        //電極テープロールNo 2本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 2本目開始
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO2KAISHI, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO2KAISHI, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 2本目終了
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO2SYURYOU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO2SYURYOU, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 2本目印刷
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO2INSATSU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO2INSATSU, srRhapsData));
        //電極テープロールNo 3本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 3本目開始
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO3KAISHI, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO3KAISHI, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 3本目終了
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO3SYURYOU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO3SYURYOU, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 3本目印刷
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO3INSATSU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO3INSATSU, srRhapsData));
        //電極テープロールNo 4本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 4本目開始
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO4KAISHI, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO4KAISHI, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 4本目終了
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO4SYURYOU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO4SYURYOU, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 4本目印刷
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO4INSATSU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO4INSATSU, srRhapsData));
        //電極テープロールNo 5本目
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 5本目開始
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO5KAISHI, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO5KAISHI, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 5本目終了
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO5SYURYOU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO5SYURYOU, srRhapsData));
        //電極ﾃｰﾌﾟﾛｰﾙNo 5本目印刷
        this.setItemData(processData, GXHDO101B006Const.ETAPEROLLNO5INSATSU, getSrRhapsItemData(GXHDO101B006Const.ETAPEROLLNO5INSATSU, srRhapsData));
        //電極テープ原料lot
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT, srRhapsData));
        //上端子テープロットNo
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_LOT_NO, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_LOT_NO, srRhapsData));
        //上端子テープスリップ記号
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU, srRhapsData));
        //上端子テープロールNo 1本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1, srRhapsData));
        //上端子テープロールNo 2本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2, srRhapsData));
        //上端子テープロールNo 3本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3, srRhapsData));
        //上端子テープロールNo 4本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4, srRhapsData));
        //上端子テープロールNo 5本目
        this.setItemData(processData, GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5, getSrRhapsItemData(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5, srRhapsData));
        //下端子テープロットNo
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_LOT_NO, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_LOT_NO, srRhapsData));
        //下端子テープスリップ記号
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_SLIP_KIGOU, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_SLIP_KIGOU, srRhapsData));
        //下端子テープロールNo 1本目
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO1, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO1, srRhapsData));
        //下端子テープロールNo 2本目
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO2, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO2, srRhapsData));
        //下端子テープロールNo 3本目
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO3, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO3, srRhapsData));
        //下端子テープロールNo 4本目
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO4, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO4, srRhapsData));
        //下端子テープロールNo 5本目
        this.setItemData(processData, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO5, getSrRhapsItemData(GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO5, srRhapsData));
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
        //部材在庫No_電極
        this.setItemData(processData, GXHDO101B006Const.BUZAIZAIKONODENKYOKU, getSrRhapsItemData(GXHDO101B006Const.BUZAIZAIKONODENKYOKU, srRhapsData));
        //電極製版No
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_NO, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SEIHAN_NO, srRhapsData));
        //電極製版枚数
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, srRhapsData));
        //累計処理数_電極
        this.setItemData(processData, GXHDO101B006Const.RUIKEISYORISUUDENKYOKU, getSrRhapsItemData(GXHDO101B006Const.RUIKEISYORISUUDENKYOKU, srRhapsData));
        //最大処理数_電極
        this.setItemData(processData, GXHDO101B006Const.SAIDAISYORISUUDENKYOKU, getSrRhapsItemData(GXHDO101B006Const.SAIDAISYORISUUDENKYOKU, srRhapsData));
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
        //部材在庫No_誘電体
        this.setItemData(processData, GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI, getSrRhapsItemData(GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI, srRhapsData));
        //誘電体製版No
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, srRhapsData));
        //誘電体製版枚数
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, srRhapsData));
        //累計処理数_誘電体
        this.setItemData(processData, GXHDO101B006Const.RUIKEISYORISUUYUUDENTAI, getSrRhapsItemData(GXHDO101B006Const.RUIKEISYORISUUYUUDENTAI, srRhapsData));
        //最大処理数_誘電体
        this.setItemData(processData, GXHDO101B006Const.SAIDAISYORISUUYUUDENTAI, getSrRhapsItemData(GXHDO101B006Const.SAIDAISYORISUUYUUDENTAI, srRhapsData));
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
        //成形長さ
        this.setItemData(processData, GXHDO101B006Const.SEIKEINAGASA, getSrRhapsItemData(GXHDO101B006Const.SEIKEINAGASA, srRhapsData));
        //備考1
        this.setItemData(processData, GXHDO101B006Const.BIKOU1, getSrRhapsItemData(GXHDO101B006Const.BIKOU1, srRhapsData));
        //備考2
        this.setItemData(processData, GXHDO101B006Const.BIKOU2, getSrRhapsItemData(GXHDO101B006Const.BIKOU2, srRhapsData));
        //備考3
        this.setItemData(processData, GXHDO101B006Const.BIKOU3, getSrRhapsItemData(GXHDO101B006Const.BIKOU3, srRhapsData));
        //備考4
        this.setItemData(processData, GXHDO101B006Const.BIKOU4, getSrRhapsItemData(GXHDO101B006Const.BIKOU4, srRhapsData));
        //備考5
        this.setItemData(processData, GXHDO101B006Const.BIKOU5, getSrRhapsItemData(GXHDO101B006Const.BIKOU5, srRhapsData));
        //印刷積層開始確認者
        this.setItemData(processData, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, getSrRhapsItemData(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, srRhapsData));
        //ﾀｰｹﾞｯﾄ有無
        this.setItemData(processData, GXHDO101B006Const.TARGET_UMU, getSrRhapsItemData(GXHDO101B006Const.TARGET_UMU, srRhapsData));
        //テープNG送り長　1本目
        this.setItemData(processData, GXHDO101B006Const.TAPE_NG1, getSrRhapsItemData(GXHDO101B006Const.TAPE_NG1, srRhapsData));
        //テープNG送り長　2本目
        this.setItemData(processData, GXHDO101B006Const.TAPE_NG2, getSrRhapsItemData(GXHDO101B006Const.TAPE_NG2, srRhapsData));
        //テープNG送り長　3本目
        this.setItemData(processData, GXHDO101B006Const.TAPE_NG3, getSrRhapsItemData(GXHDO101B006Const.TAPE_NG3, srRhapsData));
        //テープNG送り長　4本目
        this.setItemData(processData, GXHDO101B006Const.TAPE_NG4, getSrRhapsItemData(GXHDO101B006Const.TAPE_NG4, srRhapsData));
        //テープNG送り長　5本目
        this.setItemData(processData, GXHDO101B006Const.TAPE_NG5, getSrRhapsItemData(GXHDO101B006Const.TAPE_NG5, srRhapsData));
        //成形長さ2
        this.setItemData(processData, GXHDO101B006Const.SEIKEINAGASA2, getSrRhapsItemData(GXHDO101B006Const.SEIKEINAGASA2, srRhapsData));
        //成形長さ3
        this.setItemData(processData, GXHDO101B006Const.SEIKEINAGASA3, getSrRhapsItemData(GXHDO101B006Const.SEIKEINAGASA3, srRhapsData));
        //成形長さ4
        this.setItemData(processData, GXHDO101B006Const.SEIKEINAGASA4, getSrRhapsItemData(GXHDO101B006Const.SEIKEINAGASA4, srRhapsData));
        //成形長さ5
        this.setItemData(processData, GXHDO101B006Const.SEIKEINAGASA5, getSrRhapsItemData(GXHDO101B006Const.SEIKEINAGASA5, srRhapsData));
        //上端子号機
        this.setItemData(processData, GXHDO101B006Const.UWAGOKI, getSrRhapsItemData(GXHDO101B006Const.UWAGOKI, srRhapsData));
        //下端子号機
        this.setItemData(processData, GXHDO101B006Const.SHITAGOKI, getSrRhapsItemData(GXHDO101B006Const.SHITAGOKI, srRhapsData));
        //電極スライド
        this.setItemData(processData, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, getSrRhapsItemData(GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, srRhapsData));
        //誘電体スライド
        this.setItemData(processData, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU, getSrRhapsItemData(GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU, srRhapsData));

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
                + "ETapeRollNo1,ETapeRollNo1kaishi,ETapeRollNo1syuryou,ETapeRollNo1insatsu,ETapeRollNo2,ETapeRollNo2kaishi,"
                + "ETapeRollNo2syuryou,ETapeRollNo2insatsu,ETapeRollNo3,ETapeRollNo3kaishi,ETapeRollNo3syuryou,ETapeRollNo3insatsu,"
                + "ETapeRollNo4,ETapeRollNo4kaishi,ETapeRollNo4syuryou,ETapeRollNo4insatsu,ETapeRollNo5,ETapeRollNo5kaishi,"
                + "ETapeRollNo5syuryou,ETapeRollNo5insatsu,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,buzaizaikonodenkyoku,ESEIHANNO,ESEIMAISUU,"
                + "saidaisyorisuudenkyoku,ruikeisyorisuudenkyoku,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,buzaizaikonoyuudentai,CSEIHANNO,CSEIMAISUU,saidaisyorisuuyuudentai,ruikeisyorisuuyuudentai,"
                + "CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
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
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,seikeinagasa,bikou3,bikou4,bikou5,revision,'0' AS deleteflag,startkakunin,TUMU,SITATTAPELOTNO,SITATTapeSlipKigo,"
                + "SITATTapeRollNo1,SITATTapeRollNo2,SITATTapeRollNo3,SITATTapeRollNo4,SITATTapeRollNo5,tapeng1,seikeinagasa2,tapeng2,seikeinagasa3,tapeng3,seikeinagasa4,tapeng4,seikeinagasa5,tapeng5,uwagouki,shitagouki "
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
        mapping.put("ETapeRollNo1kaishi", "eTapeRollNo1kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 1本目開始
        mapping.put("ETapeRollNo1syuryou", "eTapeRollNo1syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 1本目終了
        mapping.put("ETapeRollNo1insatsu", "eTapeRollNo1insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 1本目印刷
        mapping.put("ETapeRollNo2", "eTapeRollNo2"); //電極ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("ETapeRollNo2kaishi", "eTapeRollNo2kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 2本目開始
        mapping.put("ETapeRollNo2syuryou", "eTapeRollNo2syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 2本目終了
        mapping.put("ETapeRollNo2insatsu", "eTapeRollNo2insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 2本目印刷
        mapping.put("ETapeRollNo3", "eTapeRollNo3"); //電極ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("ETapeRollNo3kaishi", "eTapeRollNo3kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 3本目開始
        mapping.put("ETapeRollNo3syuryou", "eTapeRollNo3syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 3本目終了
        mapping.put("ETapeRollNo3insatsu", "eTapeRollNo3insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 3本目印刷
        mapping.put("ETapeRollNo4", "eTapeRollNo4"); //電極ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("ETapeRollNo4kaishi", "eTapeRollNo4kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 4本目開始
        mapping.put("ETapeRollNo4syuryou", "eTapeRollNo4syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 4本目終了
        mapping.put("ETapeRollNo4insatsu", "eTapeRollNo4insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 4本目印刷
        mapping.put("ETapeRollNo5", "eTapeRollNo5"); //電極ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("ETapeRollNo5kaishi", "eTapeRollNo5kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 5本目開始
        mapping.put("ETapeRollNo5syuryou", "eTapeRollNo5syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 5本目終了
        mapping.put("ETapeRollNo5insatsu", "eTapeRollNo5insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 5本目印刷
        mapping.put("SPTUDENJIKAN", "sptudenjikan"); //積層ﾌﾟﾚｽ通電時間
        mapping.put("SKAATURYOKU", "skaaturyoku"); //積層加圧力
        mapping.put("SKHEADNO", "skheadno"); //瞬時加熱ﾍｯﾄﾞNo
        mapping.put("SUSSKAISUU", "susskaisuu"); //SUS板使用回数
        mapping.put("ECPASTEMEI", "ecpastemei"); //電極誘電体ﾍﾟｰｽﾄ名
        mapping.put("EPASTELOTNO", "epastelotno"); //電極ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("EPASTENENDO", "epastenendo"); //電極ﾍﾟｰｽﾄ粘度
        mapping.put("EPASTEONDO", "epasteondo"); //電極ﾍﾟｰｽﾄ温度
        mapping.put("ESEIHANMEI", "eseihanmei"); //電極製版名
        mapping.put("buzaizaikonodenkyoku", "buzaizaikonodenkyoku"); //部材在庫No_電極
        mapping.put("ESEIHANNO", "eseihanno"); //電極製版No
        mapping.put("ESEIMAISUU", "eseimaisuu"); //電極製版枚数
        mapping.put("saidaisyorisuudenkyoku", "saidaisyorisuudenkyoku"); //最大処理数_電極
        mapping.put("ruikeisyorisuudenkyoku", "ruikeisyorisuudenkyoku"); //累計処理数_電極
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
        mapping.put("buzaizaikonoyuudentai", "buzaizaikonoyuudentai"); //部材在庫No_誘電体
        mapping.put("CSEIHANNO", "cseihanno"); //誘電体製版No
        mapping.put("CSEIMAISUU", "cseimaisuu"); //誘電体製版枚数
        mapping.put("saidaisyorisuuyuudentai", "saidaisyorisuuyuudentai"); //最大処理数_誘電体
        mapping.put("ruikeisyorisuuyuudentai", "ruikeisyorisuuyuudentai"); //累計処理数_誘電体
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
        mapping.put("seikeinagasa", "seikeinagasa"); //成形長さ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("startkakunin", "startkakunin"); //印刷積層開始確認者
        mapping.put("TUMU", "tumu"); //ﾀｰｹﾞｯﾄ有無
        mapping.put("SITATTAPELOTNO", "sitattapelotno"); //下端子ﾃｰﾌﾟﾛｯﾄNo
        mapping.put("SITATTapeSlipKigo", "sitattapeslipkigo"); //下端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("SITATTapeRollNo1", "sitattaperollno1"); //下端子ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("SITATTapeRollNo2", "sitattaperollno2"); //下端子ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("SITATTapeRollNo3", "sitattaperollno3"); //下端子ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("SITATTapeRollNo4", "sitattaperollno4"); //下端子ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("SITATTapeRollNo5", "sitattaperollno5"); //下端子ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("tapeng1", "tapeng1"); //テープNG送り長　1本目
        mapping.put("seikeinagasa2", "seikeinagasa2"); //成形長さ　2本目
        mapping.put("tapeng2", "tapeng2"); //テープNG送り長　2本目
        mapping.put("seikeinagasa3", "seikeinagasa3"); //成形長さ　3本目
        mapping.put("tapeng3", "tapeng3"); //テープNG送り長　3本目
        mapping.put("seikeinagasa4", "seikeinagasa4"); //成形長さ　4本目
        mapping.put("tapeng4", "tapeng4"); //テープNG送り長　4本目
        mapping.put("seikeinagasa5", "seikeinagasa5"); //成形長さ　5本目
        mapping.put("tapeng5", "tapeng5"); //テープNG送り長　5本目
        mapping.put("uwagouki", "uwagouki"); //上端子号機No
        mapping.put("shitagouki", "shitagouki"); //下端子号機No
        
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
                + "ETapeRollNo1,ETapeRollNo1kaishi,ETapeRollNo1syuryou,ETapeRollNo1insatsu,ETapeRollNo2,ETapeRollNo2kaishi,"
                + "ETapeRollNo2syuryou,ETapeRollNo2insatsu,ETapeRollNo3,ETapeRollNo3kaishi,ETapeRollNo3syuryou,ETapeRollNo3insatsu,"
                + "ETapeRollNo4,ETapeRollNo4kaishi,ETapeRollNo4syuryou,ETapeRollNo4insatsu,ETapeRollNo5,ETapeRollNo5kaishi,"
                + "ETapeRollNo5syuryou,ETapeRollNo5insatsu,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,buzaizaikonodenkyoku,ESEIHANNO,ESEIMAISUU,"
                + "saidaisyorisuudenkyoku,ruikeisyorisuudenkyoku,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,buzaizaikonoyuudentai,CSEIHANNO,CSEIMAISUU,saidaisyorisuuyuudentai,ruikeisyorisuuyuudentai,"
                + "CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
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
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,seikeinagasa,bikou3,bikou4,bikou5,revision,deleteflag,startkakunin,TUMU,SITATTAPELOTNO,SITATTapeSlipKigo,"
                + "SITATTapeRollNo1,SITATTapeRollNo2,SITATTapeRollNo3,SITATTapeRollNo4,SITATTapeRollNo5,tapeng1,seikeinagasa2,tapeng2,seikeinagasa3,tapeng3,seikeinagasa4,tapeng4,seikeinagasa5,tapeng5,uwagouki,shitagouki "
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
        mapping.put("ETapeRollNo1kaishi", "eTapeRollNo1kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 1本目開始
        mapping.put("ETapeRollNo1syuryou", "eTapeRollNo1syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 1本目終了
        mapping.put("ETapeRollNo1insatsu", "eTapeRollNo1insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 1本目印刷
        mapping.put("ETapeRollNo2", "eTapeRollNo2"); //電極ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("ETapeRollNo2kaishi", "eTapeRollNo2kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 2本目開始
        mapping.put("ETapeRollNo2syuryou", "eTapeRollNo2syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 2本目終了
        mapping.put("ETapeRollNo2insatsu", "eTapeRollNo2insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 2本目印刷
        mapping.put("ETapeRollNo3", "eTapeRollNo3"); //電極ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("ETapeRollNo3kaishi", "eTapeRollNo3kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 3本目開始
        mapping.put("ETapeRollNo3syuryou", "eTapeRollNo3syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 3本目終了
        mapping.put("ETapeRollNo3insatsu", "eTapeRollNo3insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 3本目印刷
        mapping.put("ETapeRollNo4", "eTapeRollNo4"); //電極ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("ETapeRollNo4kaishi", "eTapeRollNo4kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 4本目開始
        mapping.put("ETapeRollNo4syuryou", "eTapeRollNo4syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 4本目終了
        mapping.put("ETapeRollNo4insatsu", "eTapeRollNo4insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 4本目印刷
        mapping.put("ETapeRollNo5", "eTapeRollNo5"); //電極ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("ETapeRollNo5kaishi", "eTapeRollNo5kaishi"); //電極ﾃｰﾌﾟﾛｰﾙNo 5本目開始
        mapping.put("ETapeRollNo5syuryou", "eTapeRollNo5syuryou"); //電極ﾃｰﾌﾟﾛｰﾙNo 5本目終了
        mapping.put("ETapeRollNo5insatsu", "eTapeRollNo5insatsu"); //電極ﾃｰﾌﾟﾛｰﾙNo 5本目印刷
        mapping.put("SPTUDENJIKAN", "sptudenjikan"); //積層ﾌﾟﾚｽ通電時間
        mapping.put("SKAATURYOKU", "skaaturyoku"); //積層加圧力
        mapping.put("SKHEADNO", "skheadno"); //瞬時加熱ﾍｯﾄﾞNo
        mapping.put("SUSSKAISUU", "susskaisuu"); //SUS板使用回数
        mapping.put("ECPASTEMEI", "ecpastemei"); //電極誘電体ﾍﾟｰｽﾄ名
        mapping.put("EPASTELOTNO", "epastelotno"); //電極ﾍﾟｰｽﾄﾛｯﾄNo
        mapping.put("EPASTENENDO", "epastenendo"); //電極ﾍﾟｰｽﾄ粘度
        mapping.put("EPASTEONDO", "epasteondo"); //電極ﾍﾟｰｽﾄ温度
        mapping.put("ESEIHANMEI", "eseihanmei"); //電極製版名
        mapping.put("buzaizaikonodenkyoku", "buzaizaikonodenkyoku"); //部材在庫No_電極
        mapping.put("ESEIHANNO", "eseihanno"); //電極製版No
        mapping.put("ESEIMAISUU", "eseimaisuu"); //電極製版枚数
        mapping.put("saidaisyorisuudenkyoku", "saidaisyorisuudenkyoku"); //最大処理数_電極
        mapping.put("ruikeisyorisuudenkyoku", "ruikeisyorisuudenkyoku"); //累計処理数_電極
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
        mapping.put("buzaizaikonoyuudentai", "buzaizaikonoyuudentai"); //部材在庫No_誘電体
        mapping.put("CSEIHANNO", "cseihanno"); //誘電体製版No
        mapping.put("CSEIMAISUU", "cseimaisuu"); //誘電体製版枚数
        mapping.put("saidaisyorisuuyuudentai", "saidaisyorisuuyuudentai"); //最大処理数_誘電体
        mapping.put("ruikeisyorisuuyuudentai", "ruikeisyorisuuyuudentai"); //累計処理数_誘電体
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
        mapping.put("seikeinagasa", "seikeinagasa"); //成形長さ
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ
        mapping.put("startkakunin", "startkakunin"); //印刷積層開始確認者
        mapping.put("TUMU", "tumu"); //ﾀｰｹﾞｯﾄ有無
        mapping.put("SITATTAPELOTNO", "sitattapelotno"); //下端子ﾃｰﾌﾟﾛｯﾄNo
        mapping.put("SITATTapeSlipKigo", "sitattapeslipkigo"); //下端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("SITATTapeRollNo1", "sitattaperollno1"); //下端子ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("SITATTapeRollNo2", "sitattaperollno2"); //下端子ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("SITATTapeRollNo3", "sitattaperollno3"); //下端子ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("SITATTapeRollNo4", "sitattaperollno4"); //下端子ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("SITATTapeRollNo5", "sitattaperollno5"); //下端子ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("tapeng1", "tapeng1"); //テープNG送り長　1本目
        mapping.put("seikeinagasa2", "seikeinagasa2"); //成形長さ　2本目
        mapping.put("tapeng2", "tapeng2"); //テープNG送り長　2本目
        mapping.put("seikeinagasa3", "seikeinagasa3"); //成形長さ　3本目
        mapping.put("tapeng3", "tapeng3"); //テープNG送り長　3本目
        mapping.put("seikeinagasa4", "seikeinagasa4"); //成形長さ　4本目
        mapping.put("tapeng4", "tapeng4"); //テープNG送り長　4本目
        mapping.put("seikeinagasa5", "seikeinagasa5"); //成形長さ　5本目
        mapping.put("tapeng5", "tapeng5"); //テープNG送り長　5本目
        mapping.put("uwagouki", "uwagouki"); //上端子号機No
        mapping.put("shitagouki", "shitagouki"); //下端子号機No
        
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
            //   →子画面側処理で自身の枝番を保持しておく必要がある。データ自体は親データの枝番で検索済みのものを引き渡す。
            setInputItemDataSubFormC007(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

            // ﾊﾟﾀｰﾝ間距離画面データ設定
            setInputItemDataSubFormC008(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

            // 合わせ(RZ)画面データ設定
            setInputItemDataSubFormC009(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban);

            // 被り量(µm)画面データ設定
            setInputItemDataSubFormC010(subSrRhapsDataList.get(0), kojyo, lotNo8, edaban, null, null);
            
            // 前工程WIP取込画面データ設定
            // ※電極膜厚画面とは異なり、下記メソッド内で親データの検索を実行しているため親データの枝番、状態フラグを引き渡す。
            //   また前工程WIP取込画面自体で自身の枝番は参照不要
            setInputItemDataSubFormC020(queryRunnerQcdb, kojyo, lotNo8, oyalotEdaban, jotaiFlg);

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
                + "ETapeRollNo1,ETapeRollNo1kaishi,ETapeRollNo1syuryou,ETapeRollNo1insatsu,ETapeRollNo2,ETapeRollNo2kaishi,"
                + "ETapeRollNo2syuryou,ETapeRollNo2insatsu,ETapeRollNo3,ETapeRollNo3kaishi,ETapeRollNo3syuryou,ETapeRollNo3insatsu,"
                + "ETapeRollNo4,ETapeRollNo4kaishi,ETapeRollNo4syuryou,ETapeRollNo4insatsu,ETapeRollNo5,ETapeRollNo5kaishi,"
                + "ETapeRollNo5syuryou,ETapeRollNo5insatsu,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,buzaizaikonodenkyoku,ESEIHANNO,ESEIMAISUU,"
                + "saidaisyorisuudenkyoku,ruikeisyorisuudenkyoku,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,buzaizaikonoyuudentai,CSEIHANNO,CSEIMAISUU,saidaisyorisuuyuudentai,"
                + "ruikeisyorisuuyuudentai,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
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
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,seikeinagasa,bikou3,bikou4,bikou5,revision,deleteflag,startkakunin,TUMU,SITATTAPELOTNO,SITATTapeSlipKigo,"
                + "SITATTapeRollNo1,SITATTapeRollNo2,SITATTapeRollNo3,SITATTapeRollNo4,SITATTapeRollNo5,tapeng1,seikeinagasa2,tapeng2,seikeinagasa3,tapeng3,seikeinagasa4,tapeng4,seikeinagasa5,tapeng5,uwagouki,shitagouki"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
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
                + "ETAPELOT = ?,ETapeSlipKigo = ?,ETapeRollNo1 = ?,ETapeRollNo1kaishi = ?,ETapeRollNo1syuryou = ?,ETapeRollNo1insatsu = ?,ETapeRollNo2 = ?,ETapeRollNo2kaishi = ?,"
                + "ETapeRollNo2syuryou = ?,ETapeRollNo2insatsu = ?,ETapeRollNo3 = ?,ETapeRollNo3kaishi = ?,ETapeRollNo3syuryou = ?,ETapeRollNo3insatsu = ?,ETapeRollNo4 = ?,"
                + "ETapeRollNo4kaishi = ?,ETapeRollNo4syuryou = ?,ETapeRollNo4insatsu = ?,ETapeRollNo5 = ?,ETapeRollNo5kaishi = ?,ETapeRollNo5syuryou = ?,ETapeRollNo5insatsu = ?,SPTUDENJIKAN = ?,"
                + "SKAATURYOKU = ?,SKHEADNO = ?,SUSSKAISUU = ?,ECPASTEMEI = ?,EPASTELOTNO = ?,EPASTENENDO = ?,EPASTEONDO = ?,ESEIHANMEI = ?,buzaizaikonodenkyoku = ?,ESEIHANNO = ?,"
                + "ESEIMAISUU = ?,saidaisyorisuudenkyoku = ?,ruikeisyorisuudenkyoku = ?,ECLEARANCE = ?,ESAATU = ?,ESKEEGENO = ?,ESKMAISUU = ?,ESKSPEED = ?,ESCCLEARANCE = ?,ESKKMJIKAN = ?,ELDSTART = ?,"
                + "ESLIDERYO = ?,EKANSOONDO = ?,CPASTELOTNO = ?,CPASTENENDO = ?,CPASTEONDO = ?,CSEIHANMEI = ?,buzaizaikonoyuudentai = ?,CSEIHANNO = ?,CSEIMAISUU = ?,saidaisyorisuuyuudentai = ?,ruikeisyorisuuyuudentai = ?,CSAATU = ?,"
                + "CSKEEGENO = ?,CSKMAISUU = ?,CSCCLEARANCE = ?,CSKKMJIKAN = ?,CSHIFTINSATU = ?,CLDSTART = ?,CSLIDERYO = ?,CKANSOONDO = ?,AINSATUSRZAVE = ?,"
                + "UTKAATURYOKU = ?,TICLEARANCE = ?,TISAATU = ?,TISKSPEED = ?,BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,GOKI = ?,SHUNKANKANETSUJIKAN = ?,"
                + "PETFILMSYURUI = ?,KAATURYOKU = ?,GAIKANKAKUNIN = ?,SEKIJSSKIRIKAEICHI = ?,SEKIKKSKIRIKAEICHI = ?,KAATUJIKAN = ?,TAPEHANSOUPITCH = ?,"
                + "TAPEHANSOUKAKUNIN = ?,EMAKUATSUSETTEI = ?,ENEPPUFURYOU = ?,EMAKUATSUAVE = ?,EMAKUATSUMAX = ?,EMAKUATSUMIN = ?,NIJIMISOKUTEIPTN = ?,"
                + "PRNSAMPLEGAIKAN = ?,PRNICHIYOHAKUNAGASA = ?,CTABLECLEARANCE = ?,CMAKUATSUSETTEI = ?,CSKSPEED = ?,CNEPPUFURYOU = ?,KABURIRYOU = ?,SGAIKAN = ?,"
                + "NIJIMISOKUTEISEKISOUGO = ?,SEKISOUHINGAIKAN = ?,SEKISOUZURE = ?,UWAJSSKIRIKAEICHI = ?,SHITAKKSKIRIKAEICHI = ?,TINKSYURYUI = ?,TINKLOT = ?,"
                + "TGAIKAN = ?,STARTTANTOU = ?,ENDTANTOU = ?,TENDDAY = ?,TENDTANTOU = ?,SYORISETSUU = ?,RYOUHINSETSUU = ?,HEADKOUKANTANTOU = ?,SEKISOUJOUKENTANTOU = ?,"
                + "ESEIHANSETTANTOU = ?,CSEIHANSETTANTOU = ?,DANSASOKUTEITANTOU = ?,seikeinagasa = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,revision = ?,deleteflag = ?,startkakunin = ?,TUMU = ?,SITATTAPELOTNO = ?,SITATTapeSlipKigo = ?,"
                + "SITATTapeRollNo1 = ?,SITATTapeRollNo2 = ?,SITATTapeRollNo3 = ?,SITATTapeRollNo4 = ?,SITATTapeRollNo5 = ?,tapeng1 = ?,seikeinagasa2 = ?,tapeng2 = ?,seikeinagasa3 = ?,tapeng3 = ?,seikeinagasa4 = ?,tapeng4 = ?,seikeinagasa5 = ?,tapeng5 = ?,uwagouki = ?,shitagouki = ? "
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO1KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 開始
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO1SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 終了
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO1INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 印刷
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO2KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 開始
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO2SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 終了
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO2INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 印刷
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO3KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 開始
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO3SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 終了
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO3INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 印刷
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO4KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 開始
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO4SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 終了
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO4INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 印刷
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO5KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 開始
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO5SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 終了
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO5INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 印刷
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, srRhapsData)));  //積層ﾌﾟﾚｽ通電時間
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, srRhapsData)));  //積層加圧力
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO, srRhapsData)));  //瞬時加熱ﾍｯﾄﾞNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU, srRhapsData)));  //SUS板使用回数
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI, srRhapsData)));  //電極誘電体ﾍﾟｰｽﾄ名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO, srRhapsData)));  //電極ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_NENDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_ONDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, srRhapsData)));  //電極製版名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BUZAIZAIKONODENKYOKU, srRhapsData))); //部材在庫No_電極
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_NO, srRhapsData)));  //電極製版No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, srRhapsData)));  //電極製版枚数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SAIDAISYORISUUDENKYOKU, srRhapsData))); //最大処理数_電極
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.RUIKEISYORISUUDENKYOKU, srRhapsData))); //累計処理数_電極
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
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, srRhapsData))); //電極ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, srRhapsData)));  //電極乾燥温度
        if (isInsert) {
            params.add(null); //電極乾燥時間
        }
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_NENDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_ONDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, srRhapsData)));  //誘電体製版名
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI, srRhapsData))); //部材在庫No_誘電体
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, srRhapsData)));  //誘電体製版No
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, srRhapsData)));  //誘電体製版枚数
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SAIDAISYORISUUYUUDENTAI, srRhapsData))); //最大処理数_誘電体
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.RUIKEISYORISUUYUUDENTAI, srRhapsData))); //累計処理数_誘電体
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
        }
        params.add(DBUtil.stringToBigDecimalObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU, srRhapsData)));  //誘電体ｽﾗｲﾄﾞ量
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
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA, srRhapsData)));  //成形長さ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BIKOU3, srRhapsData)));  //備考3
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BIKOU4, srRhapsData)));  //備考4
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.BIKOU5, srRhapsData)));  //備考5
        params.add(newRev); //revision
        params.add(deleteflag); //削除ﾌﾗｸﾞ
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, srRhapsData)));  //印刷積層開始確認者
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TARGET_UMU, srRhapsData)));  //ﾀｰｹﾞｯﾄ有無

        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_LOT_NO, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_SLIP_KIGOU, srRhapsData)));  //下端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO1, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO2, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO3, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO4, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO5, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo5
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TAPE_NG1, srRhapsData)));  //テープNG送り長　1本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA2, srRhapsData)));  //成形長さ　2本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TAPE_NG2, srRhapsData)));  //テープNG送り長　2本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA3, srRhapsData)));  //成形長さ　3本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TAPE_NG3, srRhapsData)));  //テープNG送り長　3本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA4, srRhapsData)));  //成形長さ　4本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TAPE_NG4, srRhapsData)));  //テープNG送り長　4本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA5, srRhapsData)));  //成形長さ　5本目
        params.add(DBUtil.stringToIntObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.TAPE_NG5, srRhapsData)));  //テープNG送り長　5本目
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.UWAGOKI, srRhapsData)));  //上端子号機No
        params.add(DBUtil.stringToStringObjectDefaultNull(getItemData(itemList, GXHDO101B006Const.SHITAGOKI, srRhapsData)));  //下端子号機No
        
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
                + "ETapeRollNo1,ETapeRollNo1kaishi,ETapeRollNo1syuryou,ETapeRollNo1insatsu,ETapeRollNo2,ETapeRollNo2kaishi,"
                + "ETapeRollNo2syuryou,ETapeRollNo2insatsu,ETapeRollNo3,ETapeRollNo3kaishi,ETapeRollNo3syuryou,ETapeRollNo3insatsu,"
                + "ETapeRollNo4,ETapeRollNo4kaishi,ETapeRollNo4syuryou,ETapeRollNo4insatsu,ETapeRollNo5,ETapeRollNo5kaishi,"
                + "ETapeRollNo5syuryou,ETapeRollNo5insatsu,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,buzaizaikonodenkyoku,ESEIHANNO,ESEIMAISUU,saidaisyorisuudenkyoku,"
                + "ruikeisyorisuudenkyoku,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,buzaizaikonoyuudentai,CSEIHANNO,CSEIMAISUU,saidaisyorisuuyuudentai,ruikeisyorisuuyuudentai,"
                + "CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
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
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,seikeinagasa,bikou3,bikou4,bikou5,revision,startkakunin,TUMU,SITATTAPELOTNO,SITATTapeSlipKigo,"
                + "SITATTapeRollNo1,SITATTapeRollNo2,SITATTapeRollNo3,SITATTapeRollNo4,SITATTapeRollNo5,tapeng1,seikeinagasa2,tapeng2,seikeinagasa3,tapeng3,seikeinagasa4,tapeng4,seikeinagasa5,tapeng5,uwagouki,shitagouki"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
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
                + "ETAPELOT = ?,ETapeSlipKigo = ?,ETapeRollNo1 = ?,ETapeRollNo1kaishi = ?,ETapeRollNo1syuryou = ?,ETapeRollNo1insatsu = ?,ETapeRollNo2 = ?,ETapeRollNo2kaishi = ?,"
                + "ETapeRollNo2syuryou = ?,ETapeRollNo2insatsu = ?,ETapeRollNo3 = ?,ETapeRollNo3kaishi = ?,ETapeRollNo3syuryou = ?,ETapeRollNo3insatsu = ?,ETapeRollNo4 = ?,"
                + "ETapeRollNo4kaishi = ?,ETapeRollNo4syuryou = ?,ETapeRollNo4insatsu = ?,ETapeRollNo5 = ?,ETapeRollNo5kaishi = ?,ETapeRollNo5syuryou = ?,ETapeRollNo5insatsu = ?,SPTUDENJIKAN = ?,"
                + "SKAATURYOKU = ?,SKHEADNO = ?,SUSSKAISUU = ?,ECPASTEMEI = ?,EPASTELOTNO = ?,EPASTENENDO = ?,EPASTEONDO = ?,ESEIHANMEI = ?,buzaizaikonodenkyoku = ?,ESEIHANNO = ?,"
                + "ESEIMAISUU = ?,saidaisyorisuudenkyoku = ?,ruikeisyorisuudenkyoku = ?,ECLEARANCE = ?,ESAATU = ?,ESKEEGENO = ?,ESKMAISUU = ?,ESKSPEED = ?,ESCCLEARANCE = ?,ESKKMJIKAN = ?,ELDSTART = ?,"
                + "ESLIDERYO = ?,EKANSOONDO = ?,CPASTELOTNO = ?,CPASTENENDO = ?,CPASTEONDO = ?,CSEIHANMEI = ?,buzaizaikonoyuudentai = ?,CSEIHANNO = ?,CSEIMAISUU = ?,saidaisyorisuuyuudentai = ?,ruikeisyorisuuyuudentai = ?,CSAATU = ?,"
                + "CSKEEGENO = ?,CSKMAISUU = ?,CSCCLEARANCE = ?,CSKKMJIKAN = ?,CSHIFTINSATU = ?,CLDSTART = ?,CSLIDERYO = ?, CKANSOONDO = ?,AINSATUSRZAVE = ?,"
                + "UTKAATURYOKU = ?,TICLEARANCE = ?,TISAATU = ?,TISKSPEED = ?,BIKO1 = ?,BIKO2 = ?,KOSINNICHIJI = ?,GOKI = ?,SHUNKANKANETSUJIKAN = ?,"
                + "PETFILMSYURUI = ?,KAATURYOKU = ?,GAIKANKAKUNIN = ?,SEKIJSSKIRIKAEICHI = ?,SEKIKKSKIRIKAEICHI = ?,KAATUJIKAN = ?,TAPEHANSOUPITCH = ?,"
                + "TAPEHANSOUKAKUNIN = ?,EMAKUATSUSETTEI = ?,ENEPPUFURYOU = ?,EMAKUATSUAVE = ?,EMAKUATSUMAX = ?,EMAKUATSUMIN = ?,NIJIMISOKUTEIPTN = ?,"
                + "PRNSAMPLEGAIKAN = ?,PRNICHIYOHAKUNAGASA = ?,CTABLECLEARANCE = ?,CMAKUATSUSETTEI = ?,CSKSPEED = ?,CNEPPUFURYOU = ?,KABURIRYOU = ?,SGAIKAN = ?,"
                + "NIJIMISOKUTEISEKISOUGO = ?,SEKISOUHINGAIKAN = ?,SEKISOUZURE = ?,UWAJSSKIRIKAEICHI = ?,SHITAKKSKIRIKAEICHI = ?,TINKSYURYUI = ?,TINKLOT = ?,"
                + "TGAIKAN = ?,STARTTANTOU = ?,ENDTANTOU = ?,TENDDAY = ?,TENDTANTOU = ?,SYORISETSUU = ?,RYOUHINSETSUU = ?,HEADKOUKANTANTOU = ?,SEKISOUJOUKENTANTOU = ?,"
                + "ESEIHANSETTANTOU = ?,CSEIHANSETTANTOU = ?,DANSASOKUTEITANTOU = ?,seikeinagasa = ?,bikou3 = ?,bikou4 = ?,bikou5 = ?,revision = ?,startkakunin = ?,TUMU = ?,SITATTAPELOTNO = ?,SITATTapeSlipKigo = ?,"
                + "SITATTapeRollNo1 = ?,SITATTapeRollNo2 = ?,SITATTapeRollNo3 = ?,SITATTapeRollNo4 = ?,SITATTapeRollNo5 = ?,tapeng1 = ?,seikeinagasa2 = ?,tapeng2 = ?,seikeinagasa3 = ?,tapeng3 = ?,seikeinagasa4 = ?,tapeng4 = ?,seikeinagasa5 = ?,tapeng5 = ?,uwagouki = ?,shitagouki = ? "
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO1KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO1SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO1INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 印刷
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO2KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO2SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO2INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 印刷
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO3KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO3SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO3INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 印刷
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO4KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO4SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO4INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 印刷
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO5KAISHI, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO5SYURYOU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.ETAPEROLLNO5INSATSU, srRhapsData)));  //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 印刷
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, srRhapsData)));  //積層ﾌﾟﾚｽ通電時間
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, srRhapsData)));  //積層加圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.SHUNJI_KANETSU_HEAD_NO, srRhapsData)));  //瞬時加熱ﾍｯﾄﾞNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.SUS_BAN_SHIYOU_KAISU, srRhapsData)));  //SUS板使用回数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI, srRhapsData)));  //電極誘電体ﾍﾟｰｽﾄ名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO, srRhapsData)));  //電極ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_NENDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_PASTE_ONDO, srRhapsData)));  //電極ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MEI, srRhapsData)));  //電極製版名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BUZAIZAIKONODENKYOKU, srRhapsData))); //部材在庫No_電極
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_NO, srRhapsData)));  //電極製版No
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU, srRhapsData)));  //電極製版枚数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SAIDAISYORISUUDENKYOKU, srRhapsData))); //最大処理数_電極
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.RUIKEISYORISUUDENKYOKU, srRhapsData))); //累計処理数_電極
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
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, srRhapsData)));  //電極ｽﾗｲﾄﾞ量
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, srRhapsData)));  //電極乾燥温度
        if (isInsert) {
            params.add(0); //電極乾燥時間
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄﾛｯﾄNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_NENDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ粘度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_PASTE_ONDO, srRhapsData)));  //誘電体ﾍﾟｰｽﾄ温度
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MEI, srRhapsData)));  //誘電体製版名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI, srRhapsData))); //部材在庫No_誘電体
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_NO, srRhapsData)));  //誘電体製版No
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, srRhapsData)));  //誘電体製版枚数
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SAIDAISYORISUUYUUDENTAI, srRhapsData))); //最大処理数_誘電体
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.RUIKEISYORISUUYUUDENTAI, srRhapsData))); //累計処理数_誘電体
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
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU, srRhapsData)));  //誘電体ｽﾗｲﾄﾞ量
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA, srRhapsData)));  //成形長さ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BIKOU3, srRhapsData)));  //備考3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BIKOU4, srRhapsData)));  //備考4
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.BIKOU5, srRhapsData)));  //備考5
        params.add(newRev); //revision
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA, srRhapsData)));  //印刷積層開始確認者
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.TARGET_UMU, srRhapsData)));  //ﾀｰｹﾞｯﾄ有無

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_LOT_NO, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_SLIP_KIGOU, srRhapsData)));  //下端子ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO1, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO2, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO3, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO4, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo4
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO5, srRhapsData)));  //下端子ﾃｰﾌﾟﾛｰﾙNo5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TAPE_NG1, srRhapsData)));  //テープNG送り長　1本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA2, srRhapsData)));  //成形長さ　2本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TAPE_NG2, srRhapsData)));  //テープNG送り長　2本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA3, srRhapsData)));  //成形長さ　3本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TAPE_NG3, srRhapsData)));  //テープNG送り長　3本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA4, srRhapsData)));  //成形長さ　4本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TAPE_NG4, srRhapsData)));  //テープNG送り長　4本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.SEIKEINAGASA5, srRhapsData)));  //成形長さ　5本目
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B006Const.TAPE_NG5, srRhapsData)));  //テープNG送り長　5本目
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.UWAGOKI, srRhapsData)));  //上端子号機No
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B006Const.SHITAGOKI, srRhapsData)));  //下端子号機No

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
            //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 開始
            case GXHDO101B006Const.ETAPEROLLNO1KAISHI:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo1kaishi());
            //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 終了
            case GXHDO101B006Const.ETAPEROLLNO1SYURYOU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo1syuryou());
            //電極ﾃｰﾌﾟﾛｰﾙNo 1本目 印刷
            case GXHDO101B006Const.ETAPEROLLNO1INSATSU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo1insatsu());
            //電極テープロールNo 2本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO2:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo2());
            //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 開始
            case GXHDO101B006Const.ETAPEROLLNO2KAISHI:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo2kaishi());
            //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 終了
            case GXHDO101B006Const.ETAPEROLLNO2SYURYOU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo2syuryou());
            //電極ﾃｰﾌﾟﾛｰﾙNo 2本目 印刷
            case GXHDO101B006Const.ETAPEROLLNO2INSATSU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo2insatsu());
            //電極テープロールNo 3本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO3:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo3());
            //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 開始
            case GXHDO101B006Const.ETAPEROLLNO3KAISHI:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo3kaishi());
            //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 終了
            case GXHDO101B006Const.ETAPEROLLNO3SYURYOU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo3syuryou());
            //電極ﾃｰﾌﾟﾛｰﾙNo 3本目 印刷
            case GXHDO101B006Const.ETAPEROLLNO3INSATSU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo3insatsu());
            //電極テープロールNo 4本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO4:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo4());
            //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 開始
            case GXHDO101B006Const.ETAPEROLLNO4KAISHI:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo4kaishi());
            //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 終了
            case GXHDO101B006Const.ETAPEROLLNO4SYURYOU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo4syuryou());
            //電極ﾃｰﾌﾟﾛｰﾙNo 4本目 印刷
            case GXHDO101B006Const.ETAPEROLLNO4INSATSU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo4insatsu());
            //電極テープロールNo 5本目
            case GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO5:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo5());
            //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 開始
            case GXHDO101B006Const.ETAPEROLLNO5KAISHI:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo5kaishi());
            //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 終了
            case GXHDO101B006Const.ETAPEROLLNO5SYURYOU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo5syuryou());
            //電極ﾃｰﾌﾟﾛｰﾙNo 5本目 印刷
            case GXHDO101B006Const.ETAPEROLLNO5INSATSU:
                return StringUtil.nullToBlank(srRhapsData.geteTapeRollNo5insatsu());
            //電極テープ原料lot
            case GXHDO101B006Const.DENKYOKU_TAPE_GENRYO_LOT:
                return StringUtil.nullToBlank(srRhapsData.getEtapeglot());
            //上端子テープロットNo
            case GXHDO101B006Const.TANSHI_TAPE_LOT_NO:
                return StringUtil.nullToBlank(srRhapsData.getTtapelotno());
            //上端子テープスリップ記号
            case GXHDO101B006Const.TANSHI_TAPE_SLIP_KIGOU:
                return StringUtil.nullToBlank(srRhapsData.gettTapeSlipKigo());
            //上端子テープロールNo 1本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo1());
            //上端子テープロールNo 2本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO2:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo2());
            //上端子テープロールNo 3本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO3:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo3());
            //上端子テープロールNo 4本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO4:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo4());
            //上端子テープロールNo 5本目
            case GXHDO101B006Const.TANSHI_TAPE_ROLL_NO5:
                return StringUtil.nullToBlank(srRhapsData.gettTapeRollNo5());
            //下端子テープロットNo
            case GXHDO101B006Const.SITA_TANSHI_TAPE_LOT_NO:
                return StringUtil.nullToBlank(srRhapsData.getSitattapelotno());
            //下端子テープスリップ記号
            case GXHDO101B006Const.SITA_TANSHI_TAPE_SLIP_KIGOU:
                return StringUtil.nullToBlank(srRhapsData.getSitattapeslipkigo());
            //下端子テープロールNo 1本目
            case GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO1:
                return StringUtil.nullToBlank(srRhapsData.getSitattaperollno1());
            //下端子テープロールNo 2本目
            case GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO2:
                return StringUtil.nullToBlank(srRhapsData.getSitattaperollno2());
            //下端子テープロールNo 3本目
            case GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO3:
                return StringUtil.nullToBlank(srRhapsData.getSitattaperollno3());
            //下端子テープロールNo 4本目
            case GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO4:
                return StringUtil.nullToBlank(srRhapsData.getSitattaperollno4());
            //下端子テープロールNo 5本目
            case GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO5:
                return StringUtil.nullToBlank(srRhapsData.getSitattaperollno5());
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
            //部材在庫No_電極
            case GXHDO101B006Const.BUZAIZAIKONODENKYOKU:
                return StringUtil.nullToBlank(srRhapsData.getBuzaizaikonodenkyoku());  
            //電極製版No
            case GXHDO101B006Const.DENKYOKU_SEIHAN_NO:
                return StringUtil.nullToBlank(srRhapsData.getEseihanno());
            //電極製版枚数
            case GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU:
                return StringUtil.nullToBlank(srRhapsData.getEseimaisuu());
            //累計処理数_電極
            case GXHDO101B006Const.RUIKEISYORISUUDENKYOKU:
                return StringUtil.nullToBlank(srRhapsData.getRuikeisyorisuudenkyoku());
            //最大処理数_電極
            case GXHDO101B006Const.SAIDAISYORISUUDENKYOKU:
                return StringUtil.nullToBlank(srRhapsData.getSaidaisyorisuudenkyoku());
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
            //部材在庫No_誘電体
            case GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI:
                return StringUtil.nullToBlank(srRhapsData.getBuzaizaikonoyuudentai());  
            //誘電体製版No
            case GXHDO101B006Const.YUUDENTAI_SEIHAN_NO:
                return StringUtil.nullToBlank(srRhapsData.getCseihanno());
            //誘電体製版枚数
            case GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU:
                return StringUtil.nullToBlank(srRhapsData.getCseimaisuu());
            //累計処理数_電極
            case GXHDO101B006Const.RUIKEISYORISUUYUUDENTAI:
                return StringUtil.nullToBlank(srRhapsData.getRuikeisyorisuuyuudentai());
            //最大処理数_電極
            case GXHDO101B006Const.SAIDAISYORISUUYUUDENTAI:
                return StringUtil.nullToBlank(srRhapsData.getSaidaisyorisuuyuudentai());
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
            //成形長さ
            case GXHDO101B006Const.SEIKEINAGASA:
                return StringUtil.nullToBlank(srRhapsData.getSeikeinagasa());
            //備考1
            case GXHDO101B006Const.BIKOU1:
                return StringUtil.nullToBlank(srRhapsData.getBiko1());
            //備考2
            case GXHDO101B006Const.BIKOU2:
                return StringUtil.nullToBlank(srRhapsData.getBiko2());
            //備考3
            case GXHDO101B006Const.BIKOU3:
                return StringUtil.nullToBlank(srRhapsData.getBikou3());
            //備考4
            case GXHDO101B006Const.BIKOU4:
                return StringUtil.nullToBlank(srRhapsData.getBikou4());
            //備考5
            case GXHDO101B006Const.BIKOU5:
                return StringUtil.nullToBlank(srRhapsData.getBikou5());
            //印刷積層開始確認者
            case GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_KAKUNINSHA:
                return StringUtil.nullToBlank(srRhapsData.getStartkakunin());
            //ﾀｰｹﾞｯﾄ有無
            case GXHDO101B006Const.TARGET_UMU:
                return StringUtil.nullToBlank(srRhapsData.getTumu());
            //テープNG送り長　1本目
            case GXHDO101B006Const.TAPE_NG1:
                return StringUtil.nullToBlank(srRhapsData.getTapeng1());
            //成形長さ2
            case GXHDO101B006Const.SEIKEINAGASA2:
                return StringUtil.nullToBlank(srRhapsData.getSeikeinagasa2());
            //テープNG送り長　2本目
            case GXHDO101B006Const.TAPE_NG2:
                return StringUtil.nullToBlank(srRhapsData.getTapeng2());
            //成形長さ3
            case GXHDO101B006Const.SEIKEINAGASA3:
                return StringUtil.nullToBlank(srRhapsData.getSeikeinagasa3());
            //テープNG送り長　3本目
            case GXHDO101B006Const.TAPE_NG3:
                return StringUtil.nullToBlank(srRhapsData.getTapeng3());
            //成形長さ4
            case GXHDO101B006Const.SEIKEINAGASA4:
                return StringUtil.nullToBlank(srRhapsData.getSeikeinagasa4());
            //テープNG送り長　4本目
            case GXHDO101B006Const.TAPE_NG4:
                return StringUtil.nullToBlank(srRhapsData.getTapeng4());
            //成形長さ5
            case GXHDO101B006Const.SEIKEINAGASA5:
                return StringUtil.nullToBlank(srRhapsData.getSeikeinagasa5());
            //テープNG送り長　5本目
            case GXHDO101B006Const.TAPE_NG5:
                return StringUtil.nullToBlank(srRhapsData.getTapeng5());
            //上端子号機
            case GXHDO101B006Const.UWAGOKI:
                return StringUtil.nullToBlank(srRhapsData.getUwagouki());
            //下端子号機
            case GXHDO101B006Const.SHITAGOKI:
                return StringUtil.nullToBlank(srRhapsData.getShitagouki());
            //電極スライド
            case GXHDO101B006Const.DENKYOKU_SLIDE_RYOU:
                return StringUtil.nullToBlank(srRhapsData.getEslideryo());
            //誘電体スライド
            case GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU:
                return StringUtil.nullToBlank(srRhapsData.getCslideryo());
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
                + "ETapeRollNo1,ETapeRollNo1kaishi,ETapeRollNo1syuryou,ETapeRollNo1insatsu,ETapeRollNo2,ETapeRollNo2kaishi,"
                + "ETapeRollNo2syuryou,ETapeRollNo2insatsu,ETapeRollNo3,ETapeRollNo3kaishi,ETapeRollNo3syuryou,ETapeRollNo3insatsu,"
                + "ETapeRollNo4,ETapeRollNo4kaishi,ETapeRollNo4syuryou,ETapeRollNo4insatsu,ETapeRollNo5,ETapeRollNo5kaishi,"
                + "ETapeRollNo5syuryou,ETapeRollNo5insatsu,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,buzaizaikonodenkyoku,ESEIHANNO,ESEIMAISUU,"
                + "saidaisyorisuudenkyoku,ruikeisyorisuudenkyoku,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,buzaizaikonoyuudentai,CSEIHANNO,CSEIMAISUU,saidaisyorisuuyuudentai,"
                + "ruikeisyorisuuyuudentai,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
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
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,seikeinagasa,bikou3,bikou4,bikou5,revision,deleteflag,startkakunin,TUMU,SITATTAPELOTNO,SITATTapeSlipKigo,"
                + "SITATTapeRollNo1,SITATTapeRollNo2,SITATTapeRollNo3,SITATTapeRollNo4,SITATTapeRollNo5,tapeng1,seikeinagasa2,tapeng2,seikeinagasa3,tapeng3,seikeinagasa4,tapeng4,seikeinagasa5,tapeng5,uwagouki,shitagouki"
                + ") SELECT "
                + "KOJYO,LOTNO,EDABAN,KCPNO,KAISINICHIJI,SYURYONICHIJI,TTAPESYURUI,TTAPELOTNO,TTapeSlipKigo,TTapeRollNo1,TTapeRollNo2,"
                + "TTapeRollNo3,TTapeRollNo4,TTapeRollNo5,TGENRYOKIGO,STSIYO,ESEKISOSIYO,ETAPESYURUI,ETAPEGLOT,ETAPELOT,ETapeSlipKigo,"
                + "ETapeRollNo1,ETapeRollNo1kaishi,ETapeRollNo1syuryou,ETapeRollNo1insatsu,ETapeRollNo2,ETapeRollNo2kaishi,"
                + "ETapeRollNo2syuryou,ETapeRollNo2insatsu,ETapeRollNo3,ETapeRollNo3kaishi,ETapeRollNo3syuryou,ETapeRollNo3insatsu,"
                + "ETapeRollNo4,ETapeRollNo4kaishi,ETapeRollNo4syuryou,ETapeRollNo4insatsu,ETapeRollNo5,ETapeRollNo5kaishi,"
                + "ETapeRollNo5syuryou,ETapeRollNo5insatsu,SPTUDENJIKAN,SKAATURYOKU,SKHEADNO,SUSSKAISUU,"
                + "ECPASTEMEI,EPASTELOTNO,EPASTENENDO,EPASTEONDO,ESEIHANMEI,buzaizaikonodenkyoku,ESEIHANNO,ESEIMAISUU,"
                + "saidaisyorisuudenkyoku,ruikeisyorisuudenkyoku,ECLEARANCE,ESAATU,ESKEEGENO,ESKMAISUU,"
                + "ESKSPEED,ESCCLEARANCE,ESKKMJIKAN,ELDSTART,ESEIMENSEKI,EMAKUATU,ESLIDERYO,EKANSOONDO,EKANSOJIKAN,CPASTELOTNO,"
                + "CPASTENENDO,CPASTEONDO,CSEIHANMEI,buzaizaikonoyuudentai,CSEIHANNO,CSEIMAISUU,saidaisyorisuuyuudentai,"
                + "ruikeisyorisuuyuudentai,CCLEARANCE,CSAATU,CSKEEGENO,CSKMAISUU,CSCCLEARANCE,CSKKMJIKAN,"
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
                + "CSEIHANSETTANTOU,DANSASOKUTEITANTOU,seikeinagasa,bikou3,bikou4,bikou5,?,?,startkakunin,TUMU,SITATTAPELOTNO,SITATTapeSlipKigo,"
                + "SITATTapeRollNo1,SITATTapeRollNo2,SITATTapeRollNo3,SITATTapeRollNo4,SITATTapeRollNo5,tapeng1,seikeinagasa2,tapeng2,seikeinagasa3,tapeng3,seikeinagasa4,tapeng4,seikeinagasa5,tapeng5,uwagouki,shitagouki "
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
        
        // 前工程WIP取込画面データ設定
        setInputItemDataSubFormC020(queryRunnerQcdb, sakiKojyo, sakilotNo8, sakiEdaban, "");

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

    /**
     * 前工程WIP取込(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openWipImport(ProcessData processData) {

        try {

            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c020");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean("GXHDO101C020");
            beanGXHDO101C020.setGxhdO101c020ModelView(beanGXHDO101C020.getGxhdO101c020Model().clone());

        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
            return processData;

        }

        return processData;
    }

    /**
     * 前工程WIP取込画面データ設定処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態フラグ
     */
    private void setInputItemDataSubFormC020(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String jotaiFlg) throws SQLException {
        // サブ画面の情報を取得
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean("GXHDO101C020");
        List<Map<String, Object>> initDataSubFormC020 = null;
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg) || JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            initDataSubFormC020 = getInitDataSubFormC020(queryRunnerQcdb, kojyo, lotNo, edaban, jotaiFlg);
        }
        
        GXHDO101C020Model model = new GXHDO101C020Model();
        // 登録データが無い場合空の状態で初期値をセットする。
        // 登録データがあれば登録データをセットする。
        model = GXHDO101C020Logic.createGXHDO101C020Model(initDataSubFormC020, "GXHDO101B006");

        model.setReturnItemId_TapeLot1_Hinmei(GXHDO101B006Const.DENKYOKU_TAPE);
        model.setReturnItemId_PasteLot1_Hinmei(GXHDO101B006Const.DENKYOKU_PASTE);
        model.setReturnItemId_TapeLot1_Conventionallot(GXHDO101B006Const.DENKYOKU_TAPE_LOT);
        model.setReturnItemId_TapeLot1_Tapelength(GXHDO101B006Const.SEIKEINAGASA);
        model.setReturnItemId_TapeLot2_Tapelength(GXHDO101B006Const.SEIKEINAGASA2);
        model.setReturnItemId_TapeLot3_Tapelength(GXHDO101B006Const.SEIKEINAGASA3);
        model.setReturnItemId_PasteLot1_Conventionallot(GXHDO101B006Const.DENKYOKU_PASTE_LOT_NO);
        model.setReturnItemId_Uwatanshi_Conventionallot(GXHDO101B006Const.TANSHI_TAPE_LOT_NO);
        model.setReturnItemId_Shitatanshi_Conventionallot(GXHDO101B006Const.SITA_TANSHI_TAPE_LOT_NO);
        model.setReturnItemId_TapeLot1_Rollno(GXHDO101B006Const.DENKYOKU_TAPE_ROLL_NO1);
        model.setReturnItemId_Uwatanshi_Rollno(GXHDO101B006Const.TANSHI_TAPE_ROLL_NO1);
        model.setReturnItemId_Shitatanshi_Rollno(GXHDO101B006Const.SITA_TANSHI_TAPE_ROLL_NO1);
        model.setReturnItemId_Petname(GXHDO101B006Const.PET_FILM_SHURUI);
        model.setReturnItemId_Yudentai1_Conventionallot(GXHDO101B006Const.YUUDENTAI_PASTE_LOT_NO);
        model.setReturnItemId_Yudentai1_Hinmei(GXHDO101B006Const.DENKYOKU_YUUDENTAI_PASTE_MEI);
        // サブ画面から戻ったときに値を設定する項目を指定する。
        beanGXHDO101C020.setGxhdO101c020Model(model);
    }

    /**
     * 前工程WIP取込画面初期表示データを取得する
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト(Qcdb)
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param jotaiFlg 状態フラグ
     * @return 前工程WIP取込画面初期表示データ
     * @throws SQLException 
     */
    private List<Map<String, Object>> getInitDataSubFormC020(QueryRunner queryRunnerQcdb, 
            String kojyo, String lotNo, String edaban, String jotaiFlg) throws SQLException {
        
        String tableName = " from sr_mwiplotlink ";
        String whereSQL = " where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and deleteflag = ?";
        if (JOTAI_FLG_KARI_TOROKU.equals(jotaiFlg)) {
            tableName = " from tmp_sr_mwiplotlink ";
            whereSQL = " where kojyo = ? and lotno = ? and edaban = ? and gamenid = ?";
        }
        
        String sql = "select mkojyo, mlotno, medaban, mkubun, mkubunno" + tableName + whereSQL;
        
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B006");
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            params.add(0);
        }
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * 【部材在庫情報(電極)】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBuzaizaikojyohoDenkyokuSyori(ProcessData processData) {
        FXHDD01 itemBuzaizaikonodenkyoku = getItemRow(processData.getItemList(), GXHDO101B006Const.BUZAIZAIKONODENKYOKU); // 部材在庫No_電極
        if(itemBuzaizaikonodenkyoku == null){
            processData.setMethod("");
            return processData;
        }
        // ｴﾗｰ項目をﾘｽﾄに追加
        List<FXHDD01> errFxhdd01List = Arrays.asList(itemBuzaizaikonodenkyoku);
        // 「部材在庫No_電極」が入力されていない場合
        if (StringUtil.isEmpty(itemBuzaizaikonodenkyoku.getValue())) {
            processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBuzaizaikonodenkyoku.getLabel1()));
            return processData;
        } else {
            // 「部材在庫No_電極」が入力されている場合
            if (StringUtil.getLength(itemBuzaizaikonodenkyoku.getValue()) != 9) {
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000004", true, true, errFxhdd01List, itemBuzaizaikonodenkyoku.getLabel1(), "9"));
                return processData;
            }
        }
        QueryRunner queryRunnerMLA = new QueryRunner(processData.getDataSourceMLAServer());
        // 部材在庫ﾃﾞｰﾀ取得
        Map<String, Object> fmlad01Data = getFmlad01Data(queryRunnerMLA, itemBuzaizaikonodenkyoku.getValue());
        if (fmlad01Data == null || fmlad01Data.isEmpty()) {
            processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000219", true, true, errFxhdd01List));
            return processData;
        }
        String siyoMaisu = StringUtil.nullToBlank(fmlad01Data.get("siyo_maisu")); // 使用枚数
        String saidaiSiyoMaisu = StringUtil.nullToBlank(fmlad01Data.get("saidai_siyo_maisu")); // 最大使用枚数
        FXHDD01 itemRuikeisyorisuudenkyoku = getItemRow(processData.getItemList(), GXHDO101B006Const.RUIKEISYORISUUDENKYOKU); // 累計処理数_電極
        FXHDD01 itemSaidaisyorisuudenkyoku = getItemRow(processData.getItemList(), GXHDO101B006Const.SAIDAISYORISUUDENKYOKU); // 最大処理数_電極
        setItemIntValue(itemRuikeisyorisuudenkyoku, siyoMaisu);
        setItemIntValue(itemSaidaisyorisuudenkyoku, saidaiSiyoMaisu);
        processData.setMethod("");
        return processData;
    }

    /**
     * 小数点以下を切り捨てして整数で対象項目に値をセットする
     *
     * @param itemData 項目
     * @param value 値
     */
    private void setItemIntValue(FXHDD01 itemData, String value) {
        if (itemData == null) {
            return;
        }
        
        if (!StringUtil.isEmpty(value)) {
            BigDecimal bigDecimalVal = null;
            // 小数部以下は切り捨て
            try {
                bigDecimalVal = new BigDecimal(value);
                bigDecimalVal = bigDecimalVal.setScale(0, RoundingMode.DOWN);
                // 値をセット
                itemData.setValue(bigDecimalVal.toPlainString());
            } catch (NumberFormatException e) {
                // 処理なし
            }
        } else {
            // 値をセット
            itemData.setValue("");
        }
    }

    /**
     * 【部材在庫情報(誘電体)】ﾎﾞﾀﾝ押下時処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doBuzaizaikojyohoYuudentaiSyori(ProcessData processData) {
        FXHDD01 itemBuzaizaikonoyuudentai = getItemRow(processData.getItemList(), GXHDO101B006Const.BUZAIZAIKONOYUUDENTAI); // 部材在庫No_誘電体
        if(itemBuzaizaikonoyuudentai == null){
            processData.setMethod("");
            return processData;
        }
        // ｴﾗｰ項目をﾘｽﾄに追加
        List<FXHDD01> errFxhdd01List = Arrays.asList(itemBuzaizaikonoyuudentai);
        // 「部材在庫No_誘電体」が入力されていない場合
        if (StringUtil.isEmpty(itemBuzaizaikonoyuudentai.getValue())) {
            processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000003", true, true, errFxhdd01List, itemBuzaizaikonoyuudentai.getLabel1()));
            return processData;
        } else {
            // 「部材在庫No_誘電体」が入力されている場合
            if (StringUtil.getLength(itemBuzaizaikonoyuudentai.getValue()) != 9) {
                processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000004", true, true, errFxhdd01List, itemBuzaizaikonoyuudentai.getLabel1(), "9"));
                return processData;
            }
        }
        QueryRunner queryRunnerMLA = new QueryRunner(processData.getDataSourceMLAServer());
        // 部材在庫ﾃﾞｰﾀ取得
        Map<String, Object> fmlad01Data = getFmlad01Data(queryRunnerMLA, itemBuzaizaikonoyuudentai.getValue());
        if (fmlad01Data == null || fmlad01Data.isEmpty()) {
            processData.getErrorMessageInfoList().add(MessageUtil.getErrorMessageInfo("XHD-000219", true, true, errFxhdd01List));
            return processData;
        }
        String siyoMaisu = StringUtil.nullToBlank(fmlad01Data.get("siyo_maisu")); // 使用枚数
        String saidaiSiyoMaisu = StringUtil.nullToBlank(fmlad01Data.get("saidai_siyo_maisu")); // 最大使用枚数
        FXHDD01 itemRuikeisyorisuuyuudentai = getItemRow(processData.getItemList(), GXHDO101B006Const.RUIKEISYORISUUYUUDENTAI); // 累計処理数_誘電体
        FXHDD01 itemSaidaisyorisuuyuudentai = getItemRow(processData.getItemList(), GXHDO101B006Const.SAIDAISYORISUUYUUDENTAI); // 最大処理数_誘電体
        setItemIntValue(itemRuikeisyorisuuyuudentai, siyoMaisu);
        setItemIntValue(itemSaidaisyorisuuyuudentai, saidaiSiyoMaisu);
        processData.setMethod("");
        return processData;
    }
    
    /**
     * 部材在庫ﾃﾞｰﾀの取得
     *
     * @param queryRunnerMLA QueryRunnerオブジェクト
     * @param zaikono 在庫No
     * @return 部材在庫ﾃﾞｰﾀ情報取得
     */
    private Map<String, Object> getFmlad01Data(QueryRunner queryRunnerMLA, String zaikono) {
        try {
            String sql = "SELECT siyo_maisu, saidai_siyo_maisu"
                    + " FROM fmlad01 "
                    + " WHERE zaiko_no = ? ";

            List<Object> params = new ArrayList<>();
            params.add(zaikono);

            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            return queryRunnerMLA.query(sql, new MapHandler(), params.toArray());
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("実行エラー", ex, LOGGER);
        }
        return null;
    }

    /**
     * 前工程WIP取込ｻﾌﾞ画面仮登録(tmp_sr_mwiplotlink)登録処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertTmpSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        String sql = "INSERT INTO tmp_sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        
        List<Object> params = new ArrayList<>();
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            if (StringUtil.isEmpty(genryouLotData.getValue())) {
                continue;
            }
            
            params.clear();
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            
            String motoLotno = genryouLotData.getValue();
            String motoKojyo = motoLotno.substring(0, 3);
            String motoLotNo9 = motoLotno.substring(3, 12);
            String motoEdaban = motoLotno.substring(12, 15);
            params.add(motoKojyo); //前工程工場ｺｰﾄﾞ
            params.add(motoLotNo9); //前工程ﾛｯﾄNo
            params.add(motoEdaban); //前工程枝番
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            params.add(mkubun); //前工程区分
            params.add(mkubunno); //前工程区分No
            params.add("GXHDO101B006"); //画面ID
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        }
    }

    /**
     * 前工程WIP取込ｻﾌﾞ画面登録(sr_mwiplotlink)登録処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void insertSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        String sql = "INSERT INTO sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, deleteflag) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        
        List<Object> params = new ArrayList<>();
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            if (StringUtil.isEmpty(genryouLotData.getValue())) {
                continue;
            }
            
            params.clear();
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            
            String motoLotno = genryouLotData.getValue();
            String motoKojyo = motoLotno.substring(0, 3);
            String motoLotNo9 = motoLotno.substring(3, 12);
            String motoEdaban = motoLotno.substring(12, 15);
            params.add(motoKojyo); //前工程工場ｺｰﾄﾞ
            params.add(motoLotNo9); //前工程ﾛｯﾄNo
            params.add(motoEdaban); //前工程枝番
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            params.add(mkubun); //前工程区分
            params.add(mkubunno); //前工程区分No
            params.add("GXHDO101B006"); //画面ID
            params.add(systemTime); //登録日時
            params.add(systemTime); //更新日時
            params.add(0); //削除フラグ
            
            DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
            queryRunnerQcdb.update(conQcdb, sql, params.toArray());
        }
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面仮登録(tmp_sr_mwiplotlink)更新処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateTmpSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        deleteTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban);
        insertTmpSrMwiplotlink(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, systemTime);
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面(sr_mwiplotlink)更新処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            
            boolean isExist = isExistFromSrMwiplotlink(queryRunnerQcdb, kojyo, lotNo, edaban, mkubun, mkubunno);
            if (isExist) {
                // データが存在の場合、deleteflagを更新
                updateSrMwiplotlinkToDelete(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, systemTime);
            }
            insertSrMwiplotlinkByData(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, systemTime, genryouLotData);
        }
    }

    /**
     * sr_mwiplotlinkデータあり→なしの場合、deleteflagを更新
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param mkubun 前工程区分
     * @param mkubunno 前工程区分No
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー
     */
    private void updateSrMwiplotlinkToDelete(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, String mkubun, String mkubunno, Timestamp systemTime) throws SQLException {
        
        String sql = "UPDATE sr_mwiplotlink "
                + "SET deleteflag = ?, koushinnichiji = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND gamenid = ? AND mkubun = ? AND mkubunno = ? AND deleteflag = '0'";
        
        List<Object> params = new ArrayList<>();
        // 更新内容
        params.add(1); //削除フラグ
        params.add(systemTime); //更新日

        // 検索条件
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番
        params.add("GXHDO101B006");
        params.add(mkubun);
        params.add(mkubunno);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面(sr_mwiplotlink)更新処理
     * sr_mwiplotlinkデータが存在しない場合、INSERT
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @param genryouLotData 前工程WIP取込ｻﾌﾞ画面のbean
     * @throws SQLException 例外エラー
     */
    private void insertSrMwiplotlinkByData(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime, 
            GXHDO101C020Model.GenryouLotData genryouLotData) throws SQLException {
        
        if (StringUtil.isEmpty(genryouLotData.getValue())) {
            return;
        }
        
        String sql = "INSERT INTO sr_mwiplotlink("
                + "kojyo, lotno, edaban, mkojyo, mlotno, medaban, mkubun, mkubunno, "
                + "gamenid, tourokunichiji, koushinnichiji, deleteflag) VALUES "
                + "(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        
        List<Object> params = new ArrayList<>();

        params.clear();
        params.add(kojyo); //工場ｺｰﾄﾞ
        params.add(lotNo); //ﾛｯﾄNo
        params.add(edaban); //枝番

        String motoLotno = genryouLotData.getValue();
        String motoKojyo = motoLotno.substring(0, 3);
        String motoLotNo9 = motoLotno.substring(3, 12);
        String motoEdaban = motoLotno.substring(12, 15);
        params.add(motoKojyo); //前工程工場ｺｰﾄﾞ
        params.add(motoLotNo9); //前工程ﾛｯﾄNo
        params.add(motoEdaban); //前工程枝番

        String typeName = genryouLotData.getTypeName();
        String mkubun = getKubun(typeName);
        String mkubunno = getKubunNo(typeName);
        params.add(mkubun); //前工程区分
        params.add(mkubunno); //前工程区分No
        params.add("GXHDO101B006"); //画面ID
        params.add(systemTime); //登録日時
        params.add(systemTime); //更新日時
        params.add(0); //削除フラグ

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * sr_mwiplotlinkのデータが存在かの判断
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param mkubun 前工程区分
     * @param mkubunno 前工程区分No
     * @return true:存在　false:存在しない
     * @throws SQLException 例外エラー
     */
    private boolean isExistFromSrMwiplotlink(QueryRunner queryRunnerQcdb, 
            String kojyo, String lotNo, String edaban, String mkubun, String mkubunno) throws SQLException {
        
        String sql = "select mkojyo, mlotno, medaban, mkubun, mkubunno "
                + "from sr_mwiplotlink "
                + "where kojyo = ? and lotno = ? and edaban = ? and gamenid = ? and mkubun = ? and mkubunno = ? and deleteflag = ? ";
        
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B006");
        params.add(mkubun);
        params.add(mkubunno);
        params.add(0);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map<String, Object> query = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        return !(query == null || query.isEmpty());
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面仮登録(tmp_sr_mwiplotlink)削除処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外エラー 
     */
    private void deleteTmpSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban) throws SQLException {
        
        String sql = "DELETE FROM tmp_sr_mwiplotlink "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND gamenid = ?";

        //更新値設定
        List<Object> params = new ArrayList<>();

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add("GXHDO101B006");
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 前工程WIP取込_ｻﾌﾞ画面仮登録(sr_mwiplotlink)削除処理
     * 
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb コネクション
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime システム日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外エラー 
     */
    private void deleteSrMwiplotlink(QueryRunner queryRunnerQcdb, Connection conQcdb, 
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        
        GXHDO101C020 beanGXHDO101C020 = (GXHDO101C020) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C020);
        List<GXHDO101C020Model.GenryouLotData> genryouLotDataList = beanGXHDO101C020.getGxhdO101c020Model().getGenryouLotDataList();
        
        for (GXHDO101C020Model.GenryouLotData genryouLotData : genryouLotDataList) {
            
            String typeName = genryouLotData.getTypeName();
            String mkubun = getKubun(typeName);
            String mkubunno = getKubunNo(typeName);
            
            if (isExistFromSrMwiplotlink(queryRunnerQcdb, kojyo, lotNo, edaban, mkubun, mkubunno)) {
                updateSrMwiplotlinkToDelete(queryRunnerQcdb, conQcdb, kojyo, lotNo, edaban, mkubun, mkubunno, systemTime);
            }
        }
    }

    /**
     * 前工程区分を取得する
     * 
     * @param typeName 前工程WIP取込サブ画面の種類名
     * @return 前工程区分
     */
    private String getKubun(String typeName) {
        String mkubun = "";
        switch (typeName) {
            case GXHDO101C020Model.TAPE_LOT_1:
            case GXHDO101C020Model.TAPE_LOT_2:
            case GXHDO101C020Model.TAPE_LOT_3:
                mkubun = "電極ﾃｰﾌﾟ";
                break;
            case GXHDO101C020Model.PASTE_LOT_1:
            case GXHDO101C020Model.PASTE_LOT_2:
            case GXHDO101C020Model.PASTE_LOT_3:
                mkubun = "内部電極ﾍﾟｰｽﾄ";
                break;
            case GXHDO101C020Model.UWA_TANSHI:
                mkubun = "上端子ﾃｰﾌﾟ";
                break;
            case GXHDO101C020Model.SHITA_TANSHI:
                mkubun = "下端子ﾃｰﾌﾟ";
                break;
            case GXHDO101C020Model.YUDENTAI_PASTE_1:
            case GXHDO101C020Model.YUDENTAI_PASTE_2:
                mkubun = "誘電体ﾍﾟｰｽﾄ";
                break;
        }
        return mkubun;
    }

    /**
     * 前工程区分Noを取得する
     * 
     * @param typeName 前工程WIP取込サブ画面の種類名
     * @return 前工程区分No
     */
    private String getKubunNo(String typeName) {
        String mkubunno = "";
        switch (typeName) {
            case GXHDO101C020Model.TAPE_LOT_1:
            case GXHDO101C020Model.PASTE_LOT_1:
            case GXHDO101C020Model.UWA_TANSHI:
            case GXHDO101C020Model.SHITA_TANSHI:
            case GXHDO101C020Model.YUDENTAI_PASTE_1:
                mkubunno = "1";
                break;
            case GXHDO101C020Model.TAPE_LOT_2:
            case GXHDO101C020Model.PASTE_LOT_2:
            case GXHDO101C020Model.YUDENTAI_PASTE_2:
                mkubunno = "2";
                break;
            case GXHDO101C020Model.TAPE_LOT_3:
            case GXHDO101C020Model.PASTE_LOT_3:
                mkubunno = "3";
                break;
        }
        return mkubunno;
    }

    /**
     * 製造条件マスタ
     * 
     * @param sekkeino
     * @param koteimei
     * @param koumokumei
     * @param kanrikoumoku
     * @param queryRunnerQcdb
     * @return 遷移先URL文字列
     * @throws java.sql.SQLException
     */
    public String loadJoken(String sekkeino, String koteimei, String koumokumei, String kanrikoumoku, QueryRunner queryRunnerQcdb) throws SQLException {
        
        // 検索用ロットNo
        
        // SQL生成
        String sql = "SELECT KIKAKUCHI AS kikakuchi"
                + " FROM DA_JOKEN "
                + "WHERE SEKKEINO = ? "
                + "AND KOUTEIMEI = ? "
                + "AND KOUMOKUMEI = ? "
                + "AND KANRIKOUMOKU = ?;";
        
        // パラメータの設定
        List<Object> params = new ArrayList<>();
        params.add(sekkeino);
        params.add(koteimei);
        params.add(koumokumei);
        params.add(kanrikoumoku);
        
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map sekkeiData = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());

        if (sekkeiData == null || sekkeiData.isEmpty()) {
            return "";
        }
        String kikakuchi = StringUtil.nullToBlank(sekkeiData.get("kikakuchi"));
        
        return StringUtil.blankToNull(kikakuchi);
    }

     /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時設定処理
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doDataCooperationSyori(ProcessData processData) {
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        // セッションから情報を取得
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        try {
            // (23)[tmp_sr_graprint_kanri]から、ﾃﾞｰﾀの取得
            List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, null, null, null);
            if (tmpSrRhapsKanriDataList == null || tmpSrRhapsKanriDataList.isEmpty()) {
                // ｴﾗｰ項目をﾘｽﾄに追加
                ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000210", true, true, null, "設備ﾃﾞｰﾀ");
                if (checkItemError != null) {
                    processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                    return processData;
                }
            }
            HashMap<String, String> itemIdConvertMap = new HashMap<>();
            // 開始日時を取得
            ErrorMessageInfo checkKaishiItemError = checkKaishiDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, "0");
            if (checkKaishiItemError != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkKaishiItemError));
                return processData;
            }
            // 電極の情報を取得
            ErrorMessageInfo checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, "0");
            if (checkItemError != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                return processData;
            }
            doDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, "0");
            
            // 上端子の情報を取得
            checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, "1");
            if (checkItemError != null) {
                processData.setErrorMessageInfoList(Arrays.asList(checkItemError));
                return processData;
            }
            doDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, "1");
            // 電極のロール情報を取得
            doRollDataCooperation(processData, queryRunnerQcdb, lotNo, 4, itemIdConvertMap, "0");
            // 号機の情報を取得
            doGokiDataCooperation(processData, queryRunnerQcdb, lotNo, 4, itemIdConvertMap, "0");
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("実行時エラー")));
        }
        
        processData.setMethod("");
        return processData;
    }
    
    /**
     * 設備ﾃﾞｰﾀ連携チェック処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap, String mode) throws SQLException {
        ErrorMessageInfo checkItemError = null;
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), mode, null);
        if (tmpSrRhapsKanriDataList != null && !tmpSrRhapsKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrRhapsKanriData = tmpSrRhapsKanriDataList.get(0);
            List<Map<String, Object>> tmpSrRhapsDataList = loadTmpRhapsData(queryRunnerQcdb, (Long) tmpSrRhapsKanriData.get("kanrino"));
            if (tmpSrRhapsDataList != null && !tmpSrRhapsDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> numberItemList;
                if (datasyurui == 1) {
                    // 開始時(ﾃﾞｰﾀ種類1)
                    numberItemList = Arrays.asList(GXHDO101B006Const.SHORI_SET_SU);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrRhapsDataList, itemIdConvertMap);
                    if (checkItemError == null) {
                        checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 2, itemIdConvertMap, mode);
                        if (checkItemError != null) {
                            return checkItemError;
                        }
                    } else {
                        return checkItemError;
                    }
                } else if (datasyurui == 2) {
                    // 開始時(ﾃﾞｰﾀ種類2)
                    numberItemList = Arrays.asList(GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI
                    , GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, GXHDO101B006Const.KAATSU_TIME, GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH
                    , GXHDO101B006Const.DENKYOKU_CLEARANCE, GXHDO101B006Const.DENKYOKU_SAATSU, GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED
                    , GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU
                    , GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU
                    , GXHDO101B006Const.SHORI_SET_SU, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI
                    , GXHDO101B006Const.YUUDENTAI_SAATSU, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE
                    , GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU
                    , GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO, GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU, GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrRhapsDataList, itemIdConvertMap);
                    if ("1".equals(mode)) {
                        return checkItemError;
                    }
                    if (checkItemError == null) {
                        checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, mode);
                        if (checkItemError != null) {
                            return checkItemError;
                        }
                    } else {
                        return checkItemError;
                    }
                } else if (datasyurui == 3) {
                    // 終了時(ﾃﾞｰﾀ種類3)
                    numberItemList = Arrays.asList(GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME);
                    checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrRhapsDataList, itemIdConvertMap);
                }
            } else {
                if (datasyurui == 1) {
                    checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 2, itemIdConvertMap, mode);
                    if (checkItemError != null) {
                        return checkItemError;
                    }
                } else if (datasyurui == 2) {
                    checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, mode);
                    if (checkItemError != null) {
                        return checkItemError;
                    }
                }
            }
        } else {
            datasyurui++;
            if (datasyurui <= 3) {
                checkItemError = checkDataCooperation(processData, queryRunnerQcdb, lotNo, datasyurui, itemIdConvertMap, mode);
                if (checkItemError != null) {
                    return checkItemError;
                }
            }
        }
        return checkItemError;
    }
    
    /**
     * 設備ﾃﾞｰﾀ連携チェック処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     * @throws SQLException 例外エラー
     */
    private ErrorMessageInfo checkKaishiDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap, String mode) throws SQLException {
        ErrorMessageInfo checkItemError = null;
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), mode, "1");
        if (tmpSrRhapsKanriDataList != null && !tmpSrRhapsKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrRhapsKanriData = tmpSrRhapsKanriDataList.get(0);
            List<Map<String, Object>> tmpSrRhapsDataList = loadTmpRhapsData(queryRunnerQcdb, (Long) tmpSrRhapsKanriData.get("kanrino"));
            if (tmpSrRhapsDataList != null && !tmpSrRhapsDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> numberItemList;
                // 開始時(ﾃﾞｰﾀ種類1)
                numberItemList = Arrays.asList(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU);
                checkItemError = checkDataCooperationItemData(processData, numberItemList, tmpSrRhapsDataList, itemIdConvertMap);
                if (checkItemError == null) {
                    doKaishiDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, "0");
                }
            }
        }
        return checkItemError;
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時、該当項目(数値表示)で取得時に、取得した値が文字列であった場合チェック処理
     *
     * @param processData 処理制御データ
     * @param numberItemList 数値項目リスト
     * @param tmpSrRphasDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @return ｴﾗｰﾒｯｾｰｼﾞ情報
     */
    private ErrorMessageInfo checkDataCooperationItemData(ProcessData processData, List<String> numberItemList, List<Map<String, Object>> tmpSrRphasDataList,
            HashMap<String, String> itemIdConvertMap) {
        for(String itemId : numberItemList){
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrRphasItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrRphasItemId[0] = itemIdConvertMap.get(itemId);
                }
                Map<String, Object> tmpSrRhapsData = tmpSrRphasDataList.stream().filter(e -> tmpSrRphasItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrRhapsData != null && !tmpSrRhapsData.isEmpty()) {
                    String ataiValue = StringUtil.nullToBlank(tmpSrRhapsData.get("atai"));
                    if(!StringUtil.isEmpty(ataiValue)){
                        try {
                           BigDecimal bigDecimalVal = new BigDecimal(ataiValue);
                        } catch (NumberFormatException e) {
                            // 該当項目(数値表示)で取得時に、取得した値が文字列であった場合
                            // ｴﾗｰ項目をﾘｽﾄに追加
                            List<FXHDD01> errFxhdd01List = Arrays.asList(itemData);
                            ErrorMessageInfo checkItemError = MessageUtil.getErrorMessageInfo("XHD-000087", true, true, errFxhdd01List);
                            return checkItemError;
                        }
                    }
                }
            } 
        }
        return null;
    }

    /**
     * 設備ﾃﾞｰﾀ連携処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @throws SQLException 例外エラー
     */
    private void doDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap, String mode) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), mode, null);
        if (tmpSrRhapsKanriDataList != null && !tmpSrRhapsKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrRhapsKanriData = tmpSrRhapsKanriDataList.get(0);
            List<Map<String, Object>> tmpSrRhapsDataList = loadTmpRhapsData(queryRunnerQcdb, (Long) tmpSrRhapsKanriData.get("kanrino"));
            if (tmpSrRhapsDataList != null && !tmpSrRhapsDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> setValueItemList;
                if (datasyurui == 1) {
                    // 開始時(ﾃﾞｰﾀ種類1)
                    if ("0".equals(mode)) {
                        setValueItemList = Arrays.asList(GXHDO101B006Const.SHORI_SET_SU, GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI
                        , GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, GXHDO101B006Const.KAATSU_TIME, GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH
                        , GXHDO101B006Const.DENKYOKU_CLEARANCE, GXHDO101B006Const.DENKYOKU_SAATSU, GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED
                        , GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU
                        , GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU
                        , GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI
                        , GXHDO101B006Const.YUUDENTAI_SAATSU, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE
                        , GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU
                        , GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO);
                    } else {
                        setValueItemList = Arrays.asList(GXHDO101B006Const.UWAGOKI);
                    }
                    setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 2, itemIdConvertMap, mode);
                } else if (datasyurui == 2) {
                    // 開始時(ﾃﾞｰﾀ種類2)
                    if ("0".equals(mode)) {
                        setValueItemList = Arrays.asList(GXHDO101B006Const.SEKISOU_PRESS_TSUUDEN_TIME, GXHDO101B006Const.SEKISOU_KAATSU_RYOKU, GXHDO101B006Const.SEKISOU_JYOUSHOU_SOKU_KIRIKAE_ICHI
                        , GXHDO101B006Const.SEKISOU_KAKOU_SOKU_KIRIKAE_ICHI, GXHDO101B006Const.KAATSU_TIME, GXHDO101B006Const.TAPE_HANSOU_OKURI_PITCH
                        , GXHDO101B006Const.DENKYOKU_CLEARANCE, GXHDO101B006Const.DENKYOKU_SAATSU, GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI, GXHDO101B006Const.DENKYOKU_SQUEEGEE_SPEED
                        , GXHDO101B006Const.DENKYOKU_SCRAPER_CLEARANCE, GXHDO101B006Const.DENKYOKU_SQUEEGEE_KAKOU_MACHI_TIME, GXHDO101B006Const.DENKYOKU_NEPPUU_HUURYOU
                        , GXHDO101B006Const.DENKYOKU_KANSOU_ONDO, GXHDO101B006Const.DENKYOKU_SLIDE_RYOU, GXHDO101B006Const.YUUDENTAI_SLIDE_RYOU
                        , GXHDO101B006Const.YUUDENTAI_TABLE_CLEARANCE, GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI
                        , GXHDO101B006Const.YUUDENTAI_SAATSU, GXHDO101B006Const.YUUDENTAI_SQUEEGEE_SPEED, GXHDO101B006Const.YUUDENTAI_SCRAPER_CLEARANCE
                        , GXHDO101B006Const.YUUDENTAI_SQUEEGEE_KAKOU_MACHI_TIME, GXHDO101B006Const.YUUDENTAI_SHIFT_INSATSU, GXHDO101B006Const.YUUDENTAI_NEPPUU_HUURYOU
                        , GXHDO101B006Const.YUUDENTAI_KANSOU_ONDO);
                        setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
                        doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, mode);
                    } else {
                        setValueItemList = Arrays.asList(GXHDO101B006Const.UE_TANSHI_KAATSU_RYOKU, GXHDO101B006Const.UE_TANSHI_JYOUSHOU_SOKU_KIRIKAE_ICHI);
                        setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
                    }
                } else if (datasyurui == 3) {
                    // 終了時(ﾃﾞｰﾀ種類3)
                    setValueItemList = Arrays.asList(GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_DAY, GXHDO101B006Const.INSATSU_SEKISOU_SHURYOU_TIME);
                    setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
                }
            } else {
                if (datasyurui == 1) {
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 2, itemIdConvertMap, mode);
                } else if (datasyurui == 2) {
                    doDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, mode);
                }
            }
        } else {
            datasyurui++;
            if (datasyurui <= 3) {
                doDataCooperation(processData, queryRunnerQcdb, lotNo, datasyurui, itemIdConvertMap, mode);
            }
        }
    }

    /**
     * 設備ﾃﾞｰﾀ連携処理 号機
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @throws SQLException 例外エラー
     */
    private void doGokiDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap, String mode) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), mode, null);
        if (tmpSrRhapsKanriDataList != null && !tmpSrRhapsKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrRhapsKanriData = tmpSrRhapsKanriDataList.get(0);
            List<Map<String, Object>> tmpSrRhapsDataList = loadTmpRhapsData(queryRunnerQcdb, (Long) tmpSrRhapsKanriData.get("kanrino"));
            if (tmpSrRhapsDataList != null && !tmpSrRhapsDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> setValueItemList;
                setValueItemList = Arrays.asList(GXHDO101B006Const.GOUKI);
                setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
            }
        }

        if (datasyurui == 2) {
            doGokiDataCooperation(processData, queryRunnerQcdb, lotNo, 3, itemIdConvertMap, mode);
        } else if (datasyurui == 3) {
            doGokiDataCooperation(processData, queryRunnerQcdb, lotNo, 1, itemIdConvertMap, mode);
        } else if (datasyurui == 4) {
            doGokiDataCooperation(processData, queryRunnerQcdb, lotNo, 2, itemIdConvertMap, mode);
        }

    }
    
    /**
     * 設備ﾃﾞｰﾀ連携処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @throws SQLException 例外エラー
     */
    private void doKaishiDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap, String mode) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), mode, "1");
        if (tmpSrRhapsKanriDataList != null && !tmpSrRhapsKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            Map<String, Object> tmpSrRhapsKanriData = tmpSrRhapsKanriDataList.get(0);
            List<Map<String, Object>> tmpSrRhapsDataList = loadTmpRhapsData(queryRunnerQcdb, (Long) tmpSrRhapsKanriData.get("kanrino"));
            if (tmpSrRhapsDataList != null && !tmpSrRhapsDataList.isEmpty()) {
                // Ⅵ.画面項目制御・出力仕様.G3)入力項目部.【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする。
                List<String> setValueItemList;
                setValueItemList = Arrays.asList(GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_DAY, GXHDO101B006Const.INSATSU_SEKISOU_KAISHI_TIME, GXHDO101B006Const.YUUDENTAI_SEIHAN_MAISU, GXHDO101B006Const.DENKYOKU_SEIHAN_MAISU);
                setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
            }
        }
    }
    
    /**
     * 設備ﾃﾞｰﾀ連携処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     * @throws SQLException 例外エラー
     */
    private void doRollDataCooperation(ProcessData processData, QueryRunner queryRunnerQcdb, String lotNo, Integer datasyurui, 
            HashMap<String, String> itemIdConvertMap, String mode) throws SQLException {
        // 検索条件:ﾃﾞｰﾀの種類==datasyurui で、Ⅲ.画面表示仕様(21)を発行する。
        List<Map<String, Object>> tmpSrRhapsKanriDataList = loadTmpRhapsKanriData(queryRunnerQcdb, lotNo, String.valueOf(datasyurui), mode, "1");
        if (tmpSrRhapsKanriDataList != null && !tmpSrRhapsKanriDataList.isEmpty()) {
            // 取得したﾃﾞｰﾀで実績Noが高い管理Noで、Ⅲ.画面表示仕様(22)を発行する。
            int i = 1;
            for (Map<String, Object> data : tmpSrRhapsKanriDataList) {
                List<Map<String, Object>> tmpSrRhapsDataList = loadTmpRhapsData(queryRunnerQcdb, (Long) data.get("kanrino"));
                if (tmpSrRhapsDataList != null && !tmpSrRhapsDataList.isEmpty()) {
                     List<String> setValueItemList;
                     switch (i) {
                         case 1:
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO1KAISHI, "sr_rhaps_eTapeRollNo_koukan_syuukaisuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO1SYURYOU, "sr_rhaps_eTapeRollNo_koukan_daibansuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO1INSATSU, "sr_rhaps_eTapeRollNoinsatsu");
                             itemIdConvertMap.put(GXHDO101B006Const.TAPE_NG1, "sr_rhaps_eTapeRollNo_tape_ng_okurityou");
                             setValueItemList = Arrays.asList(GXHDO101B006Const.ETAPEROLLNO1KAISHI, GXHDO101B006Const.ETAPEROLLNO1SYURYOU, GXHDO101B006Const.ETAPEROLLNO1INSATSU, GXHDO101B006Const.TAPE_NG1);
                             break;
                         case 2:
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO2KAISHI, "sr_rhaps_eTapeRollNo_koukan_syuukaisuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO2SYURYOU, "sr_rhaps_eTapeRollNo_koukan_daibansuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO2INSATSU, "sr_rhaps_eTapeRollNoinsatsu");
                             itemIdConvertMap.put(GXHDO101B006Const.TAPE_NG2, "sr_rhaps_eTapeRollNo_tape_ng_okurityou");
                             setValueItemList = Arrays.asList(GXHDO101B006Const.ETAPEROLLNO2KAISHI, GXHDO101B006Const.ETAPEROLLNO2SYURYOU, GXHDO101B006Const.ETAPEROLLNO2INSATSU, GXHDO101B006Const.TAPE_NG2);
                             break;
                         case 3:
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO3KAISHI, "sr_rhaps_eTapeRollNo_koukan_syuukaisuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO3SYURYOU, "sr_rhaps_eTapeRollNo_koukan_daibansuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO3INSATSU, "sr_rhaps_eTapeRollNoinsatsu");
                             itemIdConvertMap.put(GXHDO101B006Const.TAPE_NG3, "sr_rhaps_eTapeRollNo_tape_ng_okurityou");
                             setValueItemList = Arrays.asList(GXHDO101B006Const.ETAPEROLLNO3KAISHI, GXHDO101B006Const.ETAPEROLLNO3SYURYOU, GXHDO101B006Const.ETAPEROLLNO3INSATSU, GXHDO101B006Const.TAPE_NG3);
                             break;
                         case 4:
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO4KAISHI, "sr_rhaps_eTapeRollNo_koukan_syuukaisuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO4SYURYOU, "sr_rhaps_eTapeRollNo_koukan_daibansuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO4INSATSU, "sr_rhaps_eTapeRollNoinsatsu");
                             itemIdConvertMap.put(GXHDO101B006Const.TAPE_NG4, "sr_rhaps_eTapeRollNo_tape_ng_okurityou");
                             setValueItemList = Arrays.asList(GXHDO101B006Const.ETAPEROLLNO4KAISHI, GXHDO101B006Const.ETAPEROLLNO4SYURYOU, GXHDO101B006Const.ETAPEROLLNO4INSATSU, GXHDO101B006Const.TAPE_NG4);
                             break;
                         default:
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO5KAISHI, "sr_rhaps_eTapeRollNo_koukan_syuukaisuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO5SYURYOU, "sr_rhaps_eTapeRollNo_koukan_daibansuu");
                             itemIdConvertMap.put(GXHDO101B006Const.ETAPEROLLNO5INSATSU, "sr_rhaps_eTapeRollNoinsatsu");
                             itemIdConvertMap.put(GXHDO101B006Const.TAPE_NG5, "sr_rhaps_eTapeRollNo_tape_ng_okurityou");
                             setValueItemList = Arrays.asList(GXHDO101B006Const.ETAPEROLLNO5KAISHI, GXHDO101B006Const.ETAPEROLLNO5SYURYOU, GXHDO101B006Const.ETAPEROLLNO5INSATSU, GXHDO101B006Const.TAPE_NG5);
                             break;
                     }
                     setDataCooperationItemData(processData, setValueItemList, tmpSrRhapsDataList, itemIdConvertMap);
                     i++;
                }
            }
        }
    }

    /**
     * 【設備ﾃﾞｰﾀ連携】ﾎﾞﾀﾝ押下時.開始時 の該当項目へ取得ﾃﾞｰﾀを上書きする
     *
     * @param processData 処理制御データ
     * @param setValueItemList 項目リスト
     * @param tmpSrRhapsDataList 取得ﾃﾞｰﾀ
     * @param itemIdConvertMap ﾌｫｰﾑﾊﾟﾗﾒｰﾀ(item_id)とtmp_sr_graprint(item_id)の対比表
     */
    private void setDataCooperationItemData(ProcessData processData, List<String> setValueItemList, List<Map<String, Object>> tmpSrRhapsDataList,
            HashMap<String, String> itemIdConvertMap) {
        setValueItemList.forEach(itemId -> {
            FXHDD01 itemData = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).findFirst().orElse(null);
            String[] tmpSrRhapsItemId = {itemId};
            if (itemData != null) {
                if (itemIdConvertMap.containsKey(itemId)) {
                    tmpSrRhapsItemId[0] = itemIdConvertMap.get(itemId);
                }
                if (GXHDO101B006Const.YUUDENTAI_MAKUATSU_SETTEI.equals(itemId)
                        || GXHDO101B006Const.DENKYOKU_MAKUATSU_SETTEI.equals(itemId)) {
                    itemData.setValue("0");
                }
                Map<String, Object> tmpSrRhapsData = tmpSrRhapsDataList.stream().filter(e -> tmpSrRhapsItemId[0].equals(e.get("item_id"))).findFirst().orElse(null);
                if (tmpSrRhapsData != null && !tmpSrRhapsData.isEmpty()) {
                    itemData.setValue(StringUtil.nullToBlank(tmpSrRhapsData.get("atai")));
                }
            }
        });
    }
    
    /**
     * (21)[tmp_sr_graprint_kanri]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param datasyurui データ種類(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpRhapsKanriData(QueryRunner queryRunnerQcdb, String lotNo, String datasyurui, String mode, String kaishiFlg) throws SQLException {
        String kojyo = lotNo.substring(0, 3);
        String lotno = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        // [tmp_sr_graprint_kanri]から、ﾃﾞｰﾀの取得
        String sql = "SELECT distinct t1.kanrino, kojyo, lotno, edaban, datasyurui, jissekino, torokunichiji"
                + " FROM tmp_sr_rhapssekiso_kanri t1 "
                + " INNER JOIN tmp_sr_rhapssekiso t2 ON t1.kanrino = t2.kanrino "
                + " WHERE kojyo = ? AND lotno = ? AND edaban = ? ";
        if (!StringUtil.isEmpty(datasyurui)) {
            sql += " AND datasyurui = ? ";
        }
        if (!StringUtil.isEmpty(mode)) {
            sql += " AND t2.item_id = 'mode' AND t2.atai = ? ";
        }
        if (!StringUtil.isEmpty(kaishiFlg)) {
            sql += " order by jissekino asc";
        } else {
            sql += " order by jissekino desc";
        }

        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotno);
        params.add(edaban);
        if (!StringUtil.isEmpty(datasyurui)) {
            params.add(datasyurui);
        }
        if (!StringUtil.isEmpty(mode)) {
            params.add(mode);
        }

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

    /**
     * (22)[tmp_sr_graprint]から、ﾃﾞｰﾀの取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kanrino 管理No(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<Map<String, Object>> loadTmpRhapsData(QueryRunner queryRunnerQcdb, Long kanrino) throws SQLException {
        // [tmp_sr_graprint]から、ﾃﾞｰﾀの取得
        String sql = "SELECT kanrino, item_id, atai"
                + " FROM tmp_sr_rhapssekiso WHERE kanrino = ?";

        List<Object> params = new ArrayList<>();
        params.add(kanrino);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, new MapListHandler(), params.toArray());
    }

}
