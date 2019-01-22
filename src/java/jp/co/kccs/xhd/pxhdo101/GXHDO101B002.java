/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
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
import jp.co.kccs.xhd.db.model.SrSpsprintScr;
import jp.co.kccs.xhd.db.model.SubSrSpsprintScr;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.model.GXHDO101C002Model;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
import jp.co.kccs.xhd.pxhdo901.ErrorMessageInfo;
import jp.co.kccs.xhd.pxhdo901.GXHDO901A;
import jp.co.kccs.xhd.pxhdo901.IFormLogic;
import jp.co.kccs.xhd.pxhdo901.KikakuchiInputErrorInfo;
import jp.co.kccs.xhd.pxhdo901.ProcessData;
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
 * 変更日	2018/11/29<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B002(SPS系印刷・SPSスクリーン)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/29
 */
public class GXHDO101B002 implements IFormLogic {

    private static final Logger LOGGER = Logger.getLogger(GXHDO901A.class.getName());
    private static final String CHARSET = "MS932";
    private static final String JOTAI_FLG_KARI_TOROKU = "0";
    private static final String JOTAI_FLG_TOROKUZUMI = "1";
    private static final String JOTAI_FLG_SAKUJO = "9";

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
            if(processData.isFatalError()){
                if (!processData.getInitMessageList().isEmpty()) {
                    // 初期表示メッセージが設定されている場合、メッセージ表示のイベントを呼ぶ
                    processData.setMethod("openInitMessage");
                }
                return processData;
            }

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, processData.getInitJotaiFlg());

            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B002Const.BTN_UP_MAKUATSU_SUBGAMEN,
                    GXHDO101B002Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                    GXHDO101B002Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                    GXHDO101B002Const.BTN_UP_START_DATETIME,
                    GXHDO101B002Const.BTN_UP_END_DATETIME,
                    GXHDO101B002Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                    GXHDO101B002Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                    GXHDO101B002Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN,
                    GXHDO101B002Const.BTN_DOWN_START_DATETIME,
                    GXHDO101B002Const.BTN_DOWN_END_DATETIME
            ));

            // リビジョンチェック対象のボタンを設定する。
            processData.setCheckRevisionButtonId(Arrays.asList(
                    GXHDO101B002Const.BTN_UP_KARI_TOUROKU,
                    GXHDO101B002Const.BTN_UP_TOUROKU,
                    GXHDO101B002Const.BTN_UP_SAKUJO,
                    GXHDO101B002Const.BTN_UP_SHUSEI,
                    GXHDO101B002Const.BTN_DOWN_KARI_TOUROKU,
                    GXHDO101B002Const.BTN_DOWN_TOUROKU,
                    GXHDO101B002Const.BTN_DOWN_SAKUJO,
                    GXHDO101B002Const.BTN_DOWN_SHUSEI));

            //******************************************************************************************
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
     * 膜厚(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openMakuatsu(ProcessData processData) {

        try {

            processData.setProcessName("openMakuatsu");
            processData.setMethod("");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c001");

            // 膜厚(SPS)の現在の値をサブ画面の表示用の値に渡す
            GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
            beanGXHDO101C001.setGxhdO101c001ModelView(beanGXHDO101C001.getGxhdO101c001Model().clone());

        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;

        }

        return processData;
    }

    /**
     * PTN距離X(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriX(ProcessData processData) {
        try {
            processData.setProcessName("openPtnKyoriX");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c002");
            processData.setMethod("");

            // PTN距離Xの現在の値をサブ画面の表示用の値に設定
            GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
            beanGXHDO101C002.setGxhdO101c002ModelView(beanGXHDO101C002.getGxhdO101c002Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;

        }

    }

    /**
     * PTN距離Y(サブ画面Open)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData openPtnKyoriY(ProcessData processData) {
        try {

            processData.setProcessName("openPtnKyoriY");
            // コールバックパラメータにてサブ画面起動用の値を設定
            processData.setCollBackParam("gxhdo101c003");
            processData.setMethod("");

            // PTN距離Yの現在の値をサブ画面表示用に設定
            GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
            beanGXHDO101C003.setGxhdO101c003ModelView(beanGXHDO101C003.getGxhdO101c003Model().clone());

            return processData;
        } catch (CloneNotSupportedException ex) {

            ErrUtil.outputErrorLog("CloneNotSupportedException発生", ex, LOGGER);
            processData = createRegistDataErrorMessage(processData);
            return processData;

        }
    }

    /**
     * 仮登録処理(データチェック処理)
     *
     * @param processData 処理データ
     * @return 処理データ
     */
    public ProcessData checkDataTempResist(ProcessData processData) {
//        try {
        // 処理名を登録
        processData.setProcessName("tempResist");

//        try {
        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemTempResist(processData);
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
        processData.setMethod("doTempResist");

        return processData;

    }

    /**
     * 仮登録項目ﾁｪｯｸ
     *
     * @param processData
     * @return
     */
    private ErrorMessageInfo checkItemTempResist(ProcessData processData) {

        //ｽｷｰｼﾞ外観
        FXHDD01 itemBladeGikan = getItemRow(processData.getItemList(), GXHDO101B002Const.SQUEEGEE_GAIKAN);
        if ("NG".equals(itemBladeGikan.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBladeGikan);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemBladeGikan.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemShuryojiNijimiKasure.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShuryojiNijimiKasure.getLabel1());
        }

        return null;
    }

    /**
     * 仮登録処理(実処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData doTempResist(ProcessData processData) {

        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());
        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

        Connection conDoc = null;
        Connection conQcdb = null;

        try {
            // トランザクション開始
            //DocServer 
            conDoc = queryRunnerDoc.getDataSource().getConnection();
            conDoc.setAutoCommit(false);

            //Qcdb
            conQcdb = queryRunnerQcdb.getDataSource().getConnection();
            conQcdb.setAutoCommit(false);

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
                rollbackConnection(conDoc);
                rollbackConnection(conQcdb);

                return processData;
            }

            BigDecimal newRev = BigDecimal.ONE;
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);

                // 印刷SPSｽｸﾘｰﾝ_仮登録登録処理
                insertTmpSrSpsprintScr(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrSpsprintScr(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

            } else {
                // revisionを1加算する。
                BigDecimal rev = new BigDecimal(processData.getInitRev());
                newRev = rev.add(BigDecimal.ONE);
                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime);

                // 印刷SPSｽｸﾘｰﾝ_仮登録更新処理
                updateTmpSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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

            rollbackConnection(conDoc);
            rollbackConnection(conQcdb);

            processData = createRegistDataErrorMessage(processData);
        }

        return processData;

    }

    /**
     * 登録処理(データチェック処理)
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    public ProcessData checkDataResist(ProcessData processData) {
        // 処理名を登録
        processData.setProcessName("resist");

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // サブ画面の入力チェックを行う。
        List<String> errorListSubForm;
        // 膜厚(SPS)画面チェック
        errorListSubForm = checkSubFormMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openMakuatsu");
            return processData;
        }

        // PTN距離X画面チェック
        errorListSubForm = checkSubFormPtnKyoriX();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriX");
            return processData;
        }

        // PTN距離Y画面チェック
        errorListSubForm = checkSubFormPtnKyoriY();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriY");
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
        processData.setMethod("doResist");

        return processData;
    }

    /**
     * 登録項目ﾁｪｯｸ
     *
     * @param processData
     * @return
     */
    private ErrorMessageInfo checkItemResistCorrect(ProcessData processData) {

        //ブレード外観
        FXHDD01 itemBladeGikan = getItemRow(processData.getItemList(), GXHDO101B002Const.SQUEEGEE_GAIKAN);
        if ("NG".equals(itemBladeGikan.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBladeGikan);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemBladeGikan.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemShuryojiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShuryojiNijimiKasure.getLabel1());
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemInsatsuKaishiDay = getItemRow(processData.getItemList(), GXHDO101B002Const.INSATSU_KAISHI_DAY); //印刷開始日
        FXHDD01 itemInsatsuKaishiTime = getItemRow(processData.getItemList(), GXHDO101B002Const.INSATSU_KAISHI_TIME); // 印刷開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemInsatsuKaishiDay.getValue(), itemInsatsuKaishiTime.getValue());
        FXHDD01 itemInsatsuShuryouDay = getItemRow(processData.getItemList(), GXHDO101B002Const.INSATSU_SHUURYOU_DAY); //印刷終了日
        FXHDD01 itemInsatsuShuryouTime = getItemRow(processData.getItemList(), GXHDO101B002Const.INSATSU_SHUURYOU_TIME); //印刷終了時刻
        Date shuryoDate = DateUtil.convertStringToDate(itemInsatsuShuryouDay.getValue(), itemInsatsuShuryouTime.getValue());
        //R001チェック呼出し
        String msgCheckR001 = validateUtil.checkR001(itemInsatsuKaishiDay.getLabel1(), kaishiDate, itemInsatsuShuryouDay.getLabel1(), shuryoDate);
        if (!StringUtil.isEmpty(msgCheckR001)) {
            //エラー発生時
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure, itemInsatsuKaishiTime, itemInsatsuShuryouDay, itemInsatsuShuryouTime);
            return MessageUtil.getErrorMessageInfo("", msgCheckR001, true, true, errFxhdd01List);
        }

        return null;
    }

    /**
     * サブ画面(膜厚)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormMakuatsu() {
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
        return GXHDO101C001Logic.checkInput(beanGXHDO101C001.getGxhdO101c001Model());
    }

    /**
     * サブ画面(PTN距離X)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormPtnKyoriX() {
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        return GXHDO101C002Logic.checkInput(beanGXHDO101C002.getGxhdO101c002Model());
    }

    /**
     * サブ画面(PTN距離X)チェック処理
     *
     * @return エラーリスト
     */
    private List<String> checkSubFormPtnKyoriY() {
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        return GXHDO101C003Logic.checkInput(beanGXHDO101C003.getGxhdO101c003Model());
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
            conDoc = queryRunnerDoc.getDataSource().getConnection();
            conDoc.setAutoCommit(false);

            //Qcdb
            conQcdb = queryRunnerQcdb.getDataSource().getConnection();
            conQcdb.setAutoCommit(false);

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
                rollbackConnection(conDoc);
                rollbackConnection(conQcdb);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            BigDecimal newRev = BigDecimal.ONE;
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_TOROKUZUMI, systemTime);

            } else {
                // revisionを1加算する。
                newRev = rev.add(BigDecimal.ONE);
                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);
            }

            // 仮登録状態の場合、仮登録のデータを削除する。
            if (JOTAI_FLG_KARI_TOROKU.equals(processData.getInitJotaiFlg())) {
                deleteTmpSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 印刷SPSｽｸﾘｰﾝ_登録処理
            insertSrSpsprint(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面登録処理
            insertSubSrSpsprintScr(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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

            rollbackConnection(conDoc);
            rollbackConnection(conQcdb);

            processData = createRegistDataErrorMessage(processData);
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
        // 処理名を登録
        processData.setProcessName("correct");

        // 項目のチェック処理を行う。
        ErrorMessageInfo checkItemErrorInfo = checkItemResistCorrect(processData);
        if (checkItemErrorInfo != null) {
            processData.setErrorMessageInfoList(Arrays.asList(checkItemErrorInfo));
            return processData;
        }

        // サブ画面の入力チェックを行う。
        List<String> errorListSubForm;
        // 膜厚(SPS)画面チェック
        errorListSubForm = checkSubFormMakuatsu();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openMakuatsu");
            return processData;
        }

        // PTN距離X画面チェック
        errorListSubForm = checkSubFormPtnKyoriX();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriX");
            return processData;
        }

        // PTN距離Y画面チェック
        errorListSubForm = checkSubFormPtnKyoriY();
        if (!errorListSubForm.isEmpty()) {
            processData.setSubInitDispMsgList(errorListSubForm);
            processData.setMethod("openPtnKyoriY");
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
        processData.setUserAuthParam(GXHDO101B002Const.USER_AUTH_PARAM);

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
            conDoc = queryRunnerDoc.getDataSource().getConnection();
            conDoc.setAutoCommit(false);

            //Qcdb
            conQcdb = queryRunnerQcdb.getDataSource().getConnection();
            conQcdb.setAutoCommit(false);

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
                rollbackConnection(conDoc);
                rollbackConnection(conQcdb);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            BigDecimal newRev = rev.add(BigDecimal.ONE);
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 印刷SPSｽｸﾘｰﾝ_更新処理
            updateSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面更新処理
            updateSubSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (kikakuError.getKikakuchiInputErrorInfoList() != null && !kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
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

            rollbackConnection(conDoc);
            rollbackConnection(conQcdb);

            processData = createRegistDataErrorMessage(processData);
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
        // 処理名を登録
        processData.setProcessName("delete");

        // 警告メッセージの設定
        processData.setWarnMessage("削除します。よろしいですか？");

        // ユーザ認証用のパラメータをセットする。
        processData.setRquireAuth(true);
        processData.setUserAuthParam(GXHDO101B002Const.USER_AUTH_PARAM);

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
            conDoc = queryRunnerDoc.getDataSource().getConnection();
            conDoc.setAutoCommit(false);

            //Qcdb
            conQcdb = queryRunnerQcdb.getDataSource().getConnection();
            conQcdb.setAutoCommit(false);

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
                rollbackConnection(conDoc);
                rollbackConnection(conQcdb);

                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            BigDecimal newRev = rev.add(BigDecimal.ONE);
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_SAKUJO, systemTime);

            // 画面情報の読み直し
            processData = ReloadInputData(queryRunnerQcdb, processData, processData.getInitRev(), processData.getInitJotaiFlg(), kojyo, lotNo8, edaban);

            // 印刷SPSｽｸﾘｰﾝ_削除処理
            deleteSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面削除処理
            deleteSubSrSpsprintScr(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷SPSｽｸﾘｰﾝ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertTmpSrSpsprintScr(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録登録処理
            insertTmpSubSrSpsprintScr(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);

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

            rollbackConnection(conDoc);
            rollbackConnection(conQcdb);

            processData = createRegistDataErrorMessage(processData);
        }

        return processData;
    }

    private ProcessData ReloadInputData(QueryRunner queryRunnerQcdb, ProcessData processData, String rev, String jotaiFlg,
            String kojyo, String lotNo8, String edaban) throws SQLException {
        // 印刷SPSｽｸﾘｰﾝデータ取得
        List<SrSpsprintScr> srSpsprintScrDataList = getSrSpsprintScrData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);

        // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面データ取得
        List<SubSrSpsprintScr> subSrSpsprintScrDataList = getSubSrSpsprintScrData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpsprintScrDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC001(subSrSpsprintScrDataList.get(0), kojyo, lotNo8, edaban);

        // PTN距離X入力画面データ設定
        setInputItemDataSubFormC002(subSrSpsprintScrDataList.get(0));

        // PTN距離Y入力画面データ設定
        setInputItemDataSubFormC003(subSrSpsprintScrDataList.get(0));
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
                        GXHDO101B002Const.BTN_DOWN_EDABAN_COPY,
                        GXHDO101B002Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                        GXHDO101B002Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B002Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B002Const.BTN_DOWN_SAKUJO,
                        GXHDO101B002Const.BTN_DOWN_SHUSEI,
                        GXHDO101B002Const.BTN_DOWN_START_DATETIME,
                        GXHDO101B002Const.BTN_DOWN_END_DATETIME,
                        GXHDO101B002Const.BTN_UP_EDABAN_COPY,
                        GXHDO101B002Const.BTN_UP_MAKUATSU_SUBGAMEN,
                        GXHDO101B002Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B002Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B002Const.BTN_UP_SAKUJO,
                        GXHDO101B002Const.BTN_UP_SHUSEI,
                        GXHDO101B002Const.BTN_UP_START_DATETIME,
                        GXHDO101B002Const.BTN_UP_END_DATETIME
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B002Const.BTN_DOWN_KARI_TOUROKU,
                        GXHDO101B002Const.BTN_DOWN_TOUROKU,
                        GXHDO101B002Const.BTN_UP_KARI_TOUROKU,
                        GXHDO101B002Const.BTN_UP_TOUROKU));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B002Const.BTN_DOWN_KARI_TOUROKU,
                        GXHDO101B002Const.BTN_DOWN_EDABAN_COPY,
                        GXHDO101B002Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                        GXHDO101B002Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B002Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B002Const.BTN_DOWN_TOUROKU,
                        GXHDO101B002Const.BTN_DOWN_START_DATETIME,
                        GXHDO101B002Const.BTN_DOWN_END_DATETIME,
                        GXHDO101B002Const.BTN_UP_KARI_TOUROKU,
                        GXHDO101B002Const.BTN_UP_EDABAN_COPY,
                        GXHDO101B002Const.BTN_UP_MAKUATSU_SUBGAMEN,
                        GXHDO101B002Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B002Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B002Const.BTN_UP_TOUROKU,
                        GXHDO101B002Const.BTN_UP_START_DATETIME,
                        GXHDO101B002Const.BTN_UP_END_DATETIME
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B002Const.BTN_DOWN_SAKUJO,
                        GXHDO101B002Const.BTN_DOWN_SHUSEI,
                        GXHDO101B002Const.BTN_UP_SAKUJO,
                        GXHDO101B002Const.BTN_UP_SHUSEI));

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
            // 膜圧
            case GXHDO101B002Const.BTN_UP_MAKUATSU_SUBGAMEN:
            case GXHDO101B002Const.BTN_DOWN_MAKUATSU_SUBGAMEN:
                method = "openMakuatsu";
                break;
            // PTN距離X
            case GXHDO101B002Const.BTN_UP_PTN_KYORI_X_SUBGAMEN:
            case GXHDO101B002Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN:
                method = "openPtnKyoriX";
                break;
            // PTN距離Y
            case GXHDO101B002Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN:
            case GXHDO101B002Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN:
                method = "openPtnKyoriY";
                break;
            // 仮登録
            case GXHDO101B002Const.BTN_UP_KARI_TOUROKU:
            case GXHDO101B002Const.BTN_DOWN_KARI_TOUROKU:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B002Const.BTN_UP_TOUROKU:
            case GXHDO101B002Const.BTN_DOWN_TOUROKU:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B002Const.BTN_UP_EDABAN_COPY:
            case GXHDO101B002Const.BTN_DOWN_EDABAN_COPY:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B002Const.BTN_UP_SHUSEI:
            case GXHDO101B002Const.BTN_DOWN_SHUSEI:
                method = "checkDataCorrect";
                break;
            // 削除
            case GXHDO101B002Const.BTN_UP_SAKUJO:
            case GXHDO101B002Const.BTN_DOWN_SAKUJO:
                method = "checkDataDelete";
                break;

            // 開始日時
            case GXHDO101B002Const.BTN_UP_START_DATETIME:
            case GXHDO101B002Const.BTN_DOWN_START_DATETIME:
                method = "setKaishiDateTime";
                break;
            // 開始日時
            case GXHDO101B002Const.BTN_UP_END_DATETIME:
            case GXHDO101B002Const.BTN_DOWN_END_DATETIME:
                method = "setShuryouDateTime";
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
     * @throws SQLException
     */
    private ProcessData setInitDate(ProcessData processData) throws SQLException {

        QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());
        QueryRunner queryRunnerDoc = new QueryRunner(processData.getDataSourceDocServer());

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        String lotNo = (String) session.getAttribute("lotNo");
        String formId = StringUtil.nullToBlank(session.getAttribute("formId"));

        // エラーメッセージリスト
        List<String> errorMessageList = processData.getInitMessageList();

        // 設計情報の取得
        Map sekkeiData = this.loadSekkeiData(queryRunnerQcdb, lotNo);
        if (sekkeiData == null || sekkeiData.isEmpty()) {
            // TODO 処理中断エラー時はどうするか
            errorMessageList.clear();
            errorMessageList.add(MessageUtil.getMessage("XHD-000014", ""));
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
            return processData;
        }

        // 設計情報チェック(対象のデータが取得出来ていない場合エラー)
        errorMessageList.addAll(ValidateUtil.checkSekkeiUnsetItems(sekkeiData, getMapSekkeiAssociation()));

        // 製版ﾏｽﾀ情報取得
        String pattern = StringUtil.nullToBlank(sekkeiData.get("PATTERN")); //電極製版名 
        Map daPatternMasData = loadDaPatternMas(queryRunnerQcdb, pattern);
        if (daPatternMasData == null || daPatternMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000034", ""));
        }

        //仕掛情報の取得
        Map shikakariData = loadShikakariData(queryRunnerDoc, lotNo);
        if (shikakariData == null || shikakariData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000029", ""));
        }
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ

        // ロット区分マスタ情報の取得
        Map lotKbnMasData = loadLotKbnMas(queryRunnerDoc, lotkubuncode);
        if (lotKbnMasData == null || lotKbnMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000015", ""));
        }

        // オーナーマスタ情報の取得
        Map ownerMasData = loadOwnerMas(queryRunnerDoc, ownercode);
        if (ownerMasData == null || ownerMasData.isEmpty()) {
            errorMessageList.add(MessageUtil.getMessage("XHD-000016", ""));
        }

        // 画面に取得した情報をセットする。(入力項目以外)
        setViewItemData(processData, sekkeiData, lotKbnMasData, ownerMasData, daPatternMasData, shikakariData, lotNo);

        // 入力項目の情報を画面にセットする。
        if (!setInputItemData(processData, queryRunnerDoc, queryRunnerQcdb, lotNo, formId)) {
            // エラー発生時は処理を中断
            processData.setFatalError(true);
            processData.setInitMessageList(Arrays.asList("初期データの取得に失敗しました。"));
            return processData;
        }

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
     */
    private void setViewItemData(ProcessData processData, Map sekkeiData, Map lotKbnMasData, Map ownerMasData, Map daPatternMasData, Map shikakariData, String lotNo) {

        // ロットNo
        this.setItemData(processData, GXHDO101B002Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B002Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String suuryo = StringUtil.nullToBlank(getMapData(shikakariData, "suuryo"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(suuryo) || "0".equals(suuryo) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B002Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(suuryo);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B002Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B002Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B002Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B002Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B002Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "owner"));
            this.setItemData(processData, GXHDO101B002Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B002Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B002Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B002Const.UE_COVER_TAPE1, StringUtil.nullToBlank(sekkeiData.get("TBUNRUI2"))
                + "-"
                + StringUtil.nullToBlank(sekkeiData.get("SYURUI2"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI2"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU2"))
                + "枚"
        );

        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B002Const.SHITA_COVER_TAPE1, StringUtil.nullToBlank(sekkeiData.get("TBUNRUI4"))
                + "-"
                + StringUtil.nullToBlank(sekkeiData.get("SYURUI4"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI4"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU4"))
                + "枚");

        // 列 × 行
        String lRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "LRETU")); //列
        String wRetsu = StringUtil.nullToBlank(getMapData(daPatternMasData, "WRETU")); //行
        this.setItemData(processData, GXHDO101B002Const.RETSU_GYOU, lRetsu + "×" + wRetsu);

        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B002Const.PITCH, lSun + "×" + wSun);

        // 電極ペースト
        this.setItemData(processData, GXHDO101B002Const.DENKYOKU_PASTE, "");

        // 電極製版名
        this.setItemData(processData, GXHDO101B002Const.DENKYOKU_SEIHAN_MEI, StringUtil.nullToBlank(sekkeiData.get("PATTERN")));

        // 電極製版仕様
        this.setItemData(processData, GXHDO101B002Const.DENKYOKU_SEIHAN_SHIYOU, "");

        // 積層スライド量
        this.setItemData(processData, GXHDO101B002Const.SEKISOU_SLIDE_RYOU, "");

    }

    /**
     * 入力項目のデータを画面項目に設定
     *
     * @param processData 処理制御データ
     * @param sekkeiData 設計データ
     * @param lotKbnMasData ﾛｯﾄ区分ﾏｽﾀデータ
     * @param ownerMasData ｵｰﾅｰﾏｽﾀデータ
     * @param daPatternMasData 製版ﾏｽﾀデータ
     * @param shikakariData 仕掛データ
     * @param lotNo ﾛｯﾄNo
     */
    private boolean setInputItemData(ProcessData processData, QueryRunner queryRunnerDoc, QueryRunner queryRunnerQcdb, String lotNo, String formId) throws SQLException {

        List<SrSpsprintScr> srSpsprintScrDataList = new ArrayList<>();
        List<SubSrSpsprintScr> subSrSpsprintScrDataList = new ArrayList<>();
        String rev = "";
        String jotaiFlg = "";
        String kojyo = lotNo.substring(0, 3);
        String lotNo8 = lotNo.substring(3, 11);
        String edaban = lotNo.substring(11, 14);

        for (int i = 0; i < 5; i++) {
            // 品質DB実績登録Revision情報取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, edaban, formId);
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

                // サブ画面データ設定
                // 膜厚入力画面データ設定
                setInputItemDataSubFormC001(null, kojyo, lotNo8, edaban);

                // PTN距離X入力画面データ設定
                setInputItemDataSubFormC002(null);

                // PTN距離Y入力画面データ設定
                setInputItemDataSubFormC003(null);

                return true;
            }

            // 印刷SPSｽｸﾘｰﾝデータ取得
            srSpsprintScrDataList = getSrSpsprintScrData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srSpsprintScrDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面データ取得
            subSrSpsprintScrDataList = getSubSrSpsprintScrData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (subSrSpsprintScrDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSpsprintScrDataList.isEmpty() || subSrSpsprintScrDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpsprintScrDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC001(subSrSpsprintScrDataList.get(0), kojyo, lotNo8, edaban);

        // PTN距離X入力画面データ設定
        setInputItemDataSubFormC002(subSrSpsprintScrDataList.get(0));

        // PTN距離Y入力画面データ設定
        setInputItemDataSubFormC003(subSrSpsprintScrDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSpsprintScrData 印刷SPSｽｸﾘｰﾝ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSpsprintScr srSpsprintScrData) {

        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B002Const.SLIP_LOTNO, StringUtil.nullToBlank(srSpsprintScrData.getTapelotno()));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B002Const.ROLL_NO1, StringUtil.nullToBlank(srSpsprintScrData.getTaperollno1()));
        // ﾛｰﾙNo2
        this.setItemData(processData, GXHDO101B002Const.ROLL_NO2, StringUtil.nullToBlank(srSpsprintScrData.getTaperollno2()));
        // ﾛｰﾙNo3
        this.setItemData(processData, GXHDO101B002Const.ROLL_NO3, StringUtil.nullToBlank(srSpsprintScrData.getTaperollno3()));
        // 原料記号
        this.setItemData(processData, GXHDO101B002Const.GENRYO_KIGOU, StringUtil.nullToBlank(srSpsprintScrData.getGenryoukigou()));
        // ﾍﾟｰｽﾄﾛｯﾄNo1
        this.setItemData(processData, GXHDO101B002Const.PASTE_LOT_NO1, StringUtil.nullToBlank(srSpsprintScrData.getPastelotno()));
        // ﾍﾟｰｽﾄ粘度1
        this.setItemData(processData, GXHDO101B002Const.PASTE_NENDO1, StringUtil.nullToBlank(srSpsprintScrData.getPastenendo()));
        // ﾍﾟｰｽﾄ温度1
        this.setItemData(processData, GXHDO101B002Const.PASTE_ONDO1, StringUtil.nullToBlank(srSpsprintScrData.getPasteondo()));
        // ﾍﾟｰｽﾄ固形分1
        this.setItemData(processData, GXHDO101B002Const.PASTE_KOKEIBUN1, StringUtil.nullToBlank(srSpsprintScrData.getPkokeibun1()));
        // ﾍﾟｰｽﾄﾛｯﾄNo2
        this.setItemData(processData, GXHDO101B002Const.PASTE_LOT_NO2, StringUtil.nullToBlank(srSpsprintScrData.getPastelotno2()));
        // ﾍﾟｰｽﾄ粘度2
        this.setItemData(processData, GXHDO101B002Const.PASTE_NENDO2, StringUtil.nullToBlank(srSpsprintScrData.getPastenendo2()));
        // ﾍﾟｰｽﾄ温度2
        this.setItemData(processData, GXHDO101B002Const.PASTE_ONDO2, StringUtil.nullToBlank(srSpsprintScrData.getPasteondo2()));
        // ﾍﾟｰｽﾄ固形分2
        this.setItemData(processData, GXHDO101B002Const.PASTE_KOKEIBUN2, StringUtil.nullToBlank(srSpsprintScrData.getPkokeibun2()));
        // ＰＥＴフィルム種類
        this.setItemData(processData, GXHDO101B002Const.PET_FILM_SHURUI, StringUtil.nullToBlank(srSpsprintScrData.getPetfilmsyurui()));
        // 印刷号機
        this.setItemData(processData, GXHDO101B002Const.INSATSU_GOUKI, StringUtil.nullToBlank(srSpsprintScrData.getGouki()));
        // 乾燥温度表示値1
        this.setItemData(processData, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI1, StringUtil.nullToBlank(srSpsprintScrData.getKansouondo()));
        // 乾燥温度表示値2
        this.setItemData(processData, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI2, StringUtil.nullToBlank(srSpsprintScrData.getKansouondo2()));
        // 乾燥温度表示値3
        this.setItemData(processData, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI3, StringUtil.nullToBlank(srSpsprintScrData.getKansouondo3()));
        // 乾燥温度表示値4
        this.setItemData(processData, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI4, StringUtil.nullToBlank(srSpsprintScrData.getKansouondo4()));
        // 乾燥温度表示値5
        this.setItemData(processData, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI5, StringUtil.nullToBlank(srSpsprintScrData.getKansouondo5()));

        // テーブルクリアランス
        this.setItemData(processData, GXHDO101B002Const.TABLE_CLEARANCE, StringUtil.nullToBlank(srSpsprintScrData.getTableClearrance()));
        // スクレッパー速度
        this.setItemData(processData, GXHDO101B002Const.SCRAPER_SOKUDO, StringUtil.nullToBlank(srSpsprintScrData.getScraperspeed()));
        // スキージ速度
        this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_SOKUDO, StringUtil.nullToBlank(srSpsprintScrData.getSkeegespeed()));
        // スキージ角度
        this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_KAKUDO, StringUtil.nullToBlank(srSpsprintScrData.getSkeegeangle()));
        // 差圧
        this.setItemData(processData, GXHDO101B002Const.SAATSU, StringUtil.nullToBlank(srSpsprintScrData.getSaatu()));

        // 製版名
        this.setItemData(processData, GXHDO101B002Const.SEIHAN_OR_HANDOU_MEI, StringUtil.nullToBlank(srSpsprintScrData.getSeihanmei()));
        // 製版ロットNo
        this.setItemData(processData, GXHDO101B002Const.SEIHAN_OR_HANDOU_NO, StringUtil.nullToBlank(srSpsprintScrData.getSeihanno()));
        // 製版使用枚数
        this.setItemData(processData, GXHDO101B002Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, StringUtil.nullToBlank(srSpsprintScrData.getSeihanmaisuu()));
        // ｽｷｰｼﾞﾛｯﾄNo．
        this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_OR_ATSUDOU_NO, StringUtil.nullToBlank(srSpsprintScrData.getSkeegeno()));
        // ｽｷｰｼﾞ使用数(開始時)
        this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_SHIYOUSUU_KAISHIJI, StringUtil.nullToBlank(srSpsprintScrData.getSkeegemaisuu()));
        // スキージ外観
        switch (StringUtil.nullToBlank(srSpsprintScrData.getSkeegegaikan())) {
            case "0":
                this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_GAIKAN, "NG");
                break;
            case "1":
                this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_GAIKAN, "OK");
                break;
            default:
                this.setItemData(processData, GXHDO101B002Const.SQUEEGEE_GAIKAN, "");
                break;
        }

        // 印刷開始日
        this.setItemData(processData, GXHDO101B002Const.INSATSU_KAISHI_DAY, DateUtil.formattedTimestamp(srSpsprintScrData.getStartdatetime(), "yyMMdd"));
        // 印刷開始時間
        this.setItemData(processData, GXHDO101B002Const.INSATSU_KAISHI_TIME, DateUtil.formattedTimestamp(srSpsprintScrData.getStartdatetime(), "HHmm"));
        // 印刷ｽﾀｰﾄ膜厚AVE
        this.setItemData(processData, GXHDO101B002Const.INSATSU_START_MAKUATSU_AVE, StringUtil.nullToBlank(srSpsprintScrData.getMakuatuaveStart()));
        // 印刷ｽﾀｰﾄ膜厚MAX
        this.setItemData(processData, GXHDO101B002Const.INSATSU_START_MAKUATSU_MAX, StringUtil.nullToBlank(srSpsprintScrData.getMakuatumaxStart()));
        // 印刷ｽﾀｰﾄ膜厚MIN
        this.setItemData(processData, GXHDO101B002Const.INSATSU_START_MAKUATSU_MIN, StringUtil.nullToBlank(srSpsprintScrData.getMakuatuminStart()));
        // 印刷ｽﾀｰﾄ膜厚CV
        this.setItemData(processData, GXHDO101B002Const.INSATSU_START_MAKUATSU_CV, StringUtil.nullToBlank(srSpsprintScrData.getMakuatucvStart()));
        // PTN間距離印刷ｽﾀｰﾄ X Min
        this.setItemData(processData, GXHDO101B002Const.PTN_INSATSU_START_X_MIN, StringUtil.nullToBlank(srSpsprintScrData.getStartPtnDistX()));
        // PTN間距離印刷ｽﾀｰﾄ Y Min
        this.setItemData(processData, GXHDO101B002Const.PTN_INSATSU_START_Y_MIN, StringUtil.nullToBlank(srSpsprintScrData.getStartPtnDistY()));

        // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(srSpsprintScrData.getNijimikasureStart())) {
            case "0":
                this.setItemData(processData, GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK, "NG");
                break;
            case "1":
                this.setItemData(processData, GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK, "OK");
                break;
            default:
                this.setItemData(processData, GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK, "");
                break;
        }
        // 印刷スタート時担当者
        this.setItemData(processData, GXHDO101B002Const.INSATSU_STARTJI_TANTOUSHA, StringUtil.nullToBlank(srSpsprintScrData.getTantousya()));
        // 印刷終了日
        this.setItemData(processData, GXHDO101B002Const.INSATSU_SHUURYOU_DAY, DateUtil.formattedTimestamp(srSpsprintScrData.getEnddatetime(), "yyMMdd"));
        // 印刷終了時刻
        this.setItemData(processData, GXHDO101B002Const.INSATSU_SHUURYOU_TIME, DateUtil.formattedTimestamp(srSpsprintScrData.getEnddatetime(), "HHmm"));
        // 印刷ｴﾝﾄﾞ膜厚AVE
        this.setItemData(processData, GXHDO101B002Const.INSATSU_END_MAKUATSU_AVE, StringUtil.nullToBlank(srSpsprintScrData.getMakuatuaveEnd()));
        // 印刷ｴﾝﾄﾞ膜厚MAX
        this.setItemData(processData, GXHDO101B002Const.INSATSU_END_MAKUATSU_MAX, StringUtil.nullToBlank(srSpsprintScrData.getMakuatumaxEnd()));
        // 印刷ｴﾝﾄﾞ膜厚MIN
        this.setItemData(processData, GXHDO101B002Const.INSATSU_END_MAKUATSU_MIN, StringUtil.nullToBlank(srSpsprintScrData.getMakuatuminEnd()));
        // 印刷ｴﾝﾄﾞ膜厚CV
        this.setItemData(processData, GXHDO101B002Const.INSATSU_END_MAKUATSU_CV, StringUtil.nullToBlank(srSpsprintScrData.getMakuatucvEnd()));
        // PTN間距離印刷ｴﾝﾄﾞ X Min
        this.setItemData(processData, GXHDO101B002Const.PTN_INSATSU_END_X_MIN, StringUtil.nullToBlank(srSpsprintScrData.getEndPtnDistX()));
        // PTN間距離印刷ｴﾝﾄﾞ Y Min
        this.setItemData(processData, GXHDO101B002Const.PTN_INSATSU_END_Y_MIN, StringUtil.nullToBlank(srSpsprintScrData.getEndPtnDistY()));
        // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(srSpsprintScrData.getNijimikasureEnd())) {
            case "0":
                this.setItemData(processData, GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "NG");
                break;
            case "1":
                this.setItemData(processData, GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "OK");
                break;
            default:
                this.setItemData(processData, GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "");
                break;
        }

        // 印刷エンド時担当者
        this.setItemData(processData, GXHDO101B002Const.INSATSU_ENDJI_TANTOUSHA, StringUtil.nullToBlank(srSpsprintScrData.getTantoEnd()));
        // 印刷枚数
        this.setItemData(processData, GXHDO101B002Const.INSATSU_MAISUU, StringUtil.nullToBlank(srSpsprintScrData.getPrintmaisuu()));
        //備考1
        this.setItemData(processData, GXHDO101B002Const.BIKOU1, StringUtil.nullToBlank(srSpsprintScrData.getBikou1()));
        //備考2
        this.setItemData(processData, GXHDO101B002Const.BIKOU2, StringUtil.nullToBlank(srSpsprintScrData.getBikou2()));

    }

    /**
     * 膜厚入力画面データ設定処理
     *
     * @param subSrSpsprintScrData
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
    private void setInputItemDataSubFormC001(SubSrSpsprintScr subSrSpsprintScrData, String kojyo, String lotNo, String edaban) {
        // サブ画面の情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);

        //データの設定
        String[] makuatsuStart;
        String[] makuatsuEnd;
        beanGXHDO101C001.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C001.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C001.setEdaban(edaban); //枝番

        GXHDO101C001Model model;
        if (subSrSpsprintScrData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚スタート1～9
            makuatsuEnd = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚エンド1～9
            model = GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd);

        } else {
            // 登録データがあれば登録データをセットする。
            //膜厚スタート1～9
            makuatsuStart = new String[]{
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart1()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart2()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart3()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart4()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart5()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart6()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart7()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart8()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuStart9())};
            //膜厚エンド1～9
            makuatsuEnd = new String[]{
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd1()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd2()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd3()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd4()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd5()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd6()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd7()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd8()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getMakuatsuEnd9())
            };
            model = GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd);
        }

        // サブ画面から戻ったときに値を設定する項目を指定する。
        model.setReturnItemIdStartAve(GXHDO101B002Const.INSATSU_START_MAKUATSU_AVE);
        model.setReturnItemIdStartMax(GXHDO101B002Const.INSATSU_START_MAKUATSU_MAX);
        model.setReturnItemIdStartMin(GXHDO101B002Const.INSATSU_START_MAKUATSU_MIN);
        model.setReturnItemIdStartCv(GXHDO101B002Const.INSATSU_START_MAKUATSU_CV);
        model.setReturnItemIdEndAve(GXHDO101B002Const.INSATSU_END_MAKUATSU_AVE);
        model.setReturnItemIdEndMax(GXHDO101B002Const.INSATSU_END_MAKUATSU_MAX);
        model.setReturnItemIdEndMin(GXHDO101B002Const.INSATSU_END_MAKUATSU_MIN);
        model.setReturnItemIdEndCv(GXHDO101B002Const.INSATSU_END_MAKUATSU_CV);
        beanGXHDO101C001.setGxhdO101c001Model(model);
    }

    /**
     * PTN距離X入力画面データ設定処理
     *
     * @param subSrSpsprintScrData
     */
    private void setInputItemDataSubFormC002(SubSrSpsprintScr subSrSpsprintScrData) {

        // PTN距離Xサブ画面初期表示データ設定
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        //データの設定
        String[] startPtnDistX;
        String[] endPtnDistX;
        GXHDO101C002Model model;
        if (subSrSpsprintScrData == null) {
            startPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XStart
            endPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XEnd

            model = GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, endPtnDistX);

        } else {
            //PTN距離Xスタート1～5
            startPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistX1()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistX2()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistX3()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistX4()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistX5())};
            //PTN距離Xエンド1～5
            endPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistX1()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistX2()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistX3()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistX4()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistX5())};

            model = GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, endPtnDistX);

        }
        model.setReturnItemIdStartMin(GXHDO101B002Const.PTN_INSATSU_START_X_MIN);
        model.setReturnItemIdEndMin(GXHDO101B002Const.PTN_INSATSU_END_X_MIN);
        beanGXHDO101C002.setGxhdO101c002Model(model);
    }

    /**
     * PTN距離Y入力画面データ設定処理
     *
     * @param subSrSpsprintScrData
     */
    private void setInputItemDataSubFormC003(SubSrSpsprintScr subSrSpsprintScrData) {

        // PTN距離Yサブ画面初期表示データ設定
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        //データの設定
        String[] startPtnDistY;
        String[] endPtnDistY;
        GXHDO101C003Model model;
        if (subSrSpsprintScrData == null) {
            startPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YStart
            endPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YEnd
            model = GXHDO101C003Logic.createGXHDO101C003Model(startPtnDistY, endPtnDistY);

        } else {
            //PTN距離Yスタート1～5
            startPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistY1()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistY2()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistY3()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistY4()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getStartPtnDistY5())};
            //PTN距離Yエンド1～5
            endPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistY1()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistY2()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistY3()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistY4()),
                StringUtil.nullToBlank(subSrSpsprintScrData.getEndPtnDistY5())};
            model = GXHDO101C003Logic.createGXHDO101C003Model(startPtnDistY, endPtnDistY);

        }
        model.setReturnItemIdStartMin(GXHDO101B002Const.PTN_INSATSU_START_Y_MIN);
        model.setReturnItemIdEndMin(GXHDO101B002Const.PTN_INSATSU_END_Y_MIN);
        beanGXHDO101C003.setGxhdO101c003Model(model);
    }

    /**
     * 印刷SPSｽｸﾘｰﾝの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態フラグ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 印刷SPSｽｸﾘｰﾝ登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintScr> getSrSpsprintScrData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSpsprintScr(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSpsprintScr(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintScr> getSubSrSpsprintScrData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSpsprintScr(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSpsprintScr(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * (2)[設計]から、初期表示する情報を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadSekkeiData(QueryRunner queryRunnerQcdb, String lotNo) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        // 設計データの取得
        String sql = "SELECT SEKKEINO,"
                + "GENRYOU,ETAPE,EATUMI,SOUSUU,EMAISUU,TBUNRUI2,SYURUI2,ATUMI2,"
                + "MAISUU2,TBUNRUI4,SYURUI4,ATUMI4,MAISUU4,PATTERN "
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
     * @return 設計データ関連付けマップ
     */
    private Map getMapSekkeiAssociation() {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("GENRYOU", "電極テープ");
                put("ETAPE", "電極テープ");
                put("EATUMI", "積層数");
                put("SOUSUU", "積層数");
                put("EMAISUU", "積層数");
                put("TBUNRUI2", "上カバーテープ１");
                put("SYURUI2", "上カバーテープ１");
                put("ATUMI2", "上カバーテープ１");
                put("MAISUU2", "上カバーテープ１");
                put("TBUNRUI4", "下カバーテープ１");
                put("SYURUI4", "下カバーテープ１");
                put("ATUMI4", "下カバーテープ１");
                put("MAISUU4", "下カバーテープ１");
                put("PATTERN", "電極製版名");
            }
        };

        return map;
    }

    /**
     * (3)[ﾛｯﾄ区分ﾏｽﾀｰ]から、ﾛｯﾄ区分を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
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
     * (4)[ｵｰﾅｰｺｰﾄﾞﾏｽﾀｰ]から、ｵｰﾅｰ名を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadOwnerMas(QueryRunner queryRunnerDoc, String ownerCode) throws SQLException {

        // 設計データの取得
        String sql = "SELECT owner "
                + "FROM ownermas "
                + "WHERE ownercode = ?";

        List<Object> params = new ArrayList<>();
        params.add(ownerCode);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (5)仕掛データ検索
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadShikakariData(QueryRunner queryRunnerDoc, String lotNo) throws SQLException {
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
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (10)[品質DB登録実績]から、リビジョン,状態フラグを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfo(QueryRunner queryRunnerDoc, String kojyo, String lotNo, String edaban, String formId) throws SQLException {
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
     * (13)[品質DB登録実績]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param isLock true:ﾛｯｸする、false:ﾛｯｸしない
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private Map loadFxhdd03RevInfoWithLock(QueryRunner queryRunnerDoc, Connection conDoc, String kojyo, String lotNo, String edaban, String formId) throws SQLException {
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
     * (6)[印刷SPSｽｸﾘｰﾝ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintScr> loadSrSpsprintScr(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,genryoukigou,pastelotno,pastenendo,pasteondo,"
                + "seihanno,seihanmaisuu,startdatetime,enddatetime,skeegeno,skeegemaisuu,gouki,tantousya,kakuninsya,kansouondo,"
                + "prnprofile,kansoutime,saatu,skeegespeed,skeegeangle,mld,clearrance,bikou1,bikou2,makuatu1,makuatu2,makuatu3,"
                + "makuatu4,makuatu5,pastelotno2,pastelotno3,pastelotno4,pastelotno5,pastenendo2,pastenendo3,pastenendo4,"
                + "pastenendo5,pasteondo2,pasteondo3,pasteondo4,pasteondo5,bikou3,bikou4,bikou5,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,skeegemaisuu2,taperollno1,taperollno2,taperollno3,taperollno4,taperollno5,pastehinmei,seihanmei,"
                + "makuatuave_start,makuatumax_start,makuatumin_start,start_ptn_dist_x,start_ptn_dist_y,tanto_setting,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,end_ptn_dist_x,end_ptn_dist_y,tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,sijiondo4,"
                + "sijiondo5,pkokeibun1,pkokeibun2,petfilmsyurui,makuatucv_start,nijimikasure_start,makuatucv_end,nijimikasure_end,"
                + "printmaisuu,table_clearrance,scraperspeed,skeegegaikan,torokunichiji,kosinnichiji,revision,'0' AS deleteFlag "
                + "FROM sr_spsprint "
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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("tapesyurui", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("TapeSlipKigo", "tapeSlipKigo"); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("pastelotno", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo1
        mapping.put("pastenendo", "pastenendo"); //ﾍﾟｰｽﾄ粘度1
        mapping.put("pasteondo", "pasteondo"); //ﾍﾟｰｽﾄ温度1
        mapping.put("seihanno", "seihanno"); //製版No
        mapping.put("seihanmaisuu", "seihanmaisuu"); //製版枚数
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("enddatetime", "enddatetime"); //ﾌﾟﾘﾝﾄ終了日時
        mapping.put("skeegeno", "skeegeno"); //ｽｷｰｼﾞNo
        mapping.put("skeegemaisuu", "skeegemaisuu"); //ｽｷｰｼﾞ枚数
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("tantousya", "tantousya"); //担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); //確認者ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("prnprofile", "prnprofile"); //印刷ﾌﾟﾛﾌｧｲﾙ
        mapping.put("kansoutime", "kansoutime"); //乾燥時間
        mapping.put("saatu", "saatu"); //差圧
        mapping.put("skeegespeed", "skeegespeed"); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("skeegeangle", "skeegeangle"); //ｽｷｰｼﾞ角度
        mapping.put("mld", "mld"); //ﾒﾀﾙﾚｲﾀﾞｳﾝ値
        mapping.put("clearrance", "clearrance"); //ｸﾘｱﾗﾝｽ設定値
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("makuatu1", "makuatu1"); //膜厚1
        mapping.put("makuatu2", "makuatu2"); //膜厚2
        mapping.put("makuatu3", "makuatu3"); //膜厚3
        mapping.put("makuatu4", "makuatu4"); //膜厚4
        mapping.put("makuatu5", "makuatu5"); //膜厚5
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastelotno3", "pastelotno3"); //ﾍﾟｰｽﾄﾛｯﾄNo3
        mapping.put("pastelotno4", "pastelotno4"); //ﾍﾟｰｽﾄﾛｯﾄNo4
        mapping.put("pastelotno5", "pastelotno5"); //ﾍﾟｰｽﾄﾛｯﾄNo5
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pastenendo3", "pastenendo3"); //ﾍﾟｰｽﾄ粘度3
        mapping.put("pastenendo4", "pastenendo4"); //ﾍﾟｰｽﾄ粘度4
        mapping.put("pastenendo5", "pastenendo5"); //ﾍﾟｰｽﾄ粘度5
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pasteondo3", "pasteondo3"); //ﾍﾟｰｽﾄ温度3
        mapping.put("pasteondo4", "pasteondo4"); //ﾍﾟｰｽﾄ温度4
        mapping.put("pasteondo5", "pasteondo5"); //ﾍﾟｰｽﾄ温度5
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("skeegemaisuu2", "skeegemaisuu2"); //ｽｷｰｼﾞ枚数2
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("taperollno4", "taperollno4"); //ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("taperollno5", "taperollno5"); //ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("pastehinmei", "pastehinmei"); //ﾍﾟｰｽﾄ品名
        mapping.put("seihanmei", "seihanmei"); //製版名
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("tanto_setting", "tantoSetting"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("makuatuave_end", "makuatuaveEnd"); //終了時膜厚AVE
        mapping.put("makuatumax_end", "makuatumaxEnd"); //終了時膜厚MAX
        mapping.put("makuatumin_end", "makuatuminEnd"); //終了時膜厚MIN
        mapping.put("end_ptn_dist_x", "endPtnDistX"); //終了時PTN間距離X
        mapping.put("end_ptn_dist_y", "endPtnDistY"); //終了時PTN間距離Y
        mapping.put("tanto_end", "tantoEnd"); //終了時担当者ｺｰﾄﾞ
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("sijiondo", "sijiondo"); //指示乾燥温度1
        mapping.put("sijiondo2", "sijiondo2"); //指示乾燥温度2
        mapping.put("sijiondo3", "sijiondo3"); //指示乾燥温度3
        mapping.put("sijiondo4", "sijiondo4"); //指示乾燥温度4
        mapping.put("sijiondo5", "sijiondo5"); //指示乾燥温度5
        mapping.put("pkokeibun1", "pkokeibun1"); //ﾍﾟｰｽﾄ固形分1
        mapping.put("pkokeibun2", "pkokeibun2"); //ﾍﾟｰｽﾄ固形分2
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("makuatucv_start", "makuatucvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("makuatucv_end", "makuatucvEnd"); //印刷ｴﾝﾄﾞ膜厚CV
        mapping.put("nijimikasure_end", "nijimikasureEnd"); //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("printmaisuu", "printmaisuu"); //印刷枚数
        mapping.put("table_clearrance", "tableClearrance"); //ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        mapping.put("scraperspeed", "scraperspeed"); //ｽｸﾚｯﾊﾟｰ速度
        mapping.put("skeegegaikan", "skeegegaikan"); //ｽｷｰｼﾞ外観
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprintScr>> beanHandler = new BeanListHandler<>(SrSpsprintScr.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (7)[印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintScr> loadSubSrSpsprintScr(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,"
                + "makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "makuatsu_start6,makuatsu_start7,makuatsu_start8,"
                + "makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,"
                + "start_ptn_dist_x3,start_ptn_dist_x4,start_ptn_dist_x5,"
                + "start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,"
                + "start_ptn_dist_y4,start_ptn_dist_y5,makuatsu_end1,"
                + "makuatsu_end2,makuatsu_end3,makuatsu_end4,"
                + "makuatsu_end5,makuatsu_end6,makuatsu_end7,"
                + "makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,"
                + "end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,"
                + "end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,"
                + "torokunichiji,kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sub_sr_spsprint_scr "
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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("makuatsu_start1", "makuatsuStart1"); //膜厚ｽﾀｰﾄ1
        mapping.put("makuatsu_start2", "makuatsuStart2"); //膜厚ｽﾀｰﾄ2
        mapping.put("makuatsu_start3", "makuatsuStart3"); //膜厚ｽﾀｰﾄ3
        mapping.put("makuatsu_start4", "makuatsuStart4"); //膜厚ｽﾀｰﾄ4
        mapping.put("makuatsu_start5", "makuatsuStart5"); //膜厚ｽﾀｰﾄ5
        mapping.put("makuatsu_start6", "makuatsuStart6"); //膜厚ｽﾀｰﾄ6
        mapping.put("makuatsu_start7", "makuatsuStart7"); //膜厚ｽﾀｰﾄ7
        mapping.put("makuatsu_start8", "makuatsuStart8"); //膜厚ｽﾀｰﾄ8
        mapping.put("makuatsu_start9", "makuatsuStart9"); //膜厚ｽﾀｰﾄ9
        mapping.put("start_ptn_dist_x1", "startPtnDistX1"); //PTN距離X ｽﾀｰﾄ1
        mapping.put("start_ptn_dist_x2", "startPtnDistX2"); //PTN距離X ｽﾀｰﾄ2
        mapping.put("start_ptn_dist_x3", "startPtnDistX3"); //PTN距離X ｽﾀｰﾄ3
        mapping.put("start_ptn_dist_x4", "startPtnDistX4"); //PTN距離X ｽﾀｰﾄ4
        mapping.put("start_ptn_dist_x5", "startPtnDistX5"); //PTN距離X ｽﾀｰﾄ5
        mapping.put("start_ptn_dist_y1", "startPtnDistY1"); //PTN距離Y ｽﾀｰﾄ1
        mapping.put("start_ptn_dist_y2", "startPtnDistY2"); //PTN距離Y ｽﾀｰﾄ2
        mapping.put("start_ptn_dist_y3", "startPtnDistY3"); //PTN距離Y ｽﾀｰﾄ3
        mapping.put("start_ptn_dist_y4", "startPtnDistY4"); //PTN距離Y ｽﾀｰﾄ4
        mapping.put("start_ptn_dist_y5", "startPtnDistY5"); //PTN距離Y ｽﾀｰﾄ5
        mapping.put("makuatsu_end1", "makuatsuEnd1"); //膜厚ｴﾝﾄﾞ1
        mapping.put("makuatsu_end2", "makuatsuEnd2"); //膜厚ｴﾝﾄﾞ2
        mapping.put("makuatsu_end3", "makuatsuEnd3"); //膜厚ｴﾝﾄﾞ3
        mapping.put("makuatsu_end4", "makuatsuEnd4"); //膜厚ｴﾝﾄﾞ4
        mapping.put("makuatsu_end5", "makuatsuEnd5"); //膜厚ｴﾝﾄﾞ5
        mapping.put("makuatsu_end6", "makuatsuEnd6"); //膜厚ｴﾝﾄﾞ6
        mapping.put("makuatsu_end7", "makuatsuEnd7"); //膜厚ｴﾝﾄﾞ7
        mapping.put("makuatsu_end8", "makuatsuEnd8"); //膜厚ｴﾝﾄﾞ8
        mapping.put("makuatsu_end9", "makuatsuEnd9"); //膜厚ｴﾝﾄﾞ9
        mapping.put("end_ptn_dist_x1", "endPtnDistX1"); //PTN距離X ｴﾝﾄﾞ1
        mapping.put("end_ptn_dist_x2", "endPtnDistX2"); //PTN距離X ｴﾝﾄﾞ2
        mapping.put("end_ptn_dist_x3", "endPtnDistX3"); //PTN距離X ｴﾝﾄﾞ3
        mapping.put("end_ptn_dist_x4", "endPtnDistX4"); //PTN距離X ｴﾝﾄﾞ4
        mapping.put("end_ptn_dist_x5", "endPtnDistX5"); //PTN距離X ｴﾝﾄﾞ5
        mapping.put("end_ptn_dist_y1", "endPtnDistY1"); //PTN距離Y ｴﾝﾄﾞ1
        mapping.put("end_ptn_dist_y2", "endPtnDistY2"); //PTN距離Y ｴﾝﾄﾞ2
        mapping.put("end_ptn_dist_y3", "endPtnDistY3"); //PTN距離Y ｴﾝﾄﾞ3
        mapping.put("end_ptn_dist_y4", "endPtnDistY4"); //PTN距離Y ｴﾝﾄﾞ4
        mapping.put("end_ptn_dist_y5", "endPtnDistY5"); //PTN距離Y ｴﾝﾄﾞ5
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSpsprintScr>> beanHandler = new BeanListHandler<>(SubSrSpsprintScr.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (8)[印刷SPSｽｸﾘｰﾝ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintScr> loadTmpSrSpsprintScr(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,genryoukigou,pastelotno,pastenendo,pasteondo,"
                + "seihanno,seihanmaisuu,startdatetime,enddatetime,skeegeno,skeegemaisuu,gouki,tantousya,kakuninsya,kansouondo,"
                + "prnprofile,kansoutime,saatu,skeegespeed,skeegeangle,mld,clearrance,bikou1,bikou2,makuatu1,makuatu2,makuatu3,"
                + "makuatu4,makuatu5,pastelotno2,pastelotno3,pastelotno4,pastelotno5,pastenendo2,pastenendo3,pastenendo4,"
                + "pastenendo5,pasteondo2,pasteondo3,pasteondo4,pasteondo5,bikou3,bikou4,bikou5,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,skeegemaisuu2,taperollno1,taperollno2,taperollno3,taperollno4,taperollno5,pastehinmei,seihanmei,"
                + "makuatuave_start,makuatumax_start,makuatumin_start,start_ptn_dist_x,start_ptn_dist_y,tanto_setting,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,end_ptn_dist_x,end_ptn_dist_y,tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,sijiondo4,"
                + "sijiondo5,pkokeibun1,pkokeibun2,petfilmsyurui,makuatucv_start,nijimikasure_start,makuatucv_end,nijimikasure_end,"
                + "printmaisuu,table_clearrance,scraperspeed,skeegegaikan,torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sr_spsprint_scr "
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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("tapesyurui", "tapesyurui"); //ﾃｰﾌﾟ種類
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("TapeSlipKigo", "tapeSlipKigo"); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        mapping.put("genryoukigou", "genryoukigou"); //原料記号
        mapping.put("pastelotno", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo1
        mapping.put("pastenendo", "pastenendo"); //ﾍﾟｰｽﾄ粘度1
        mapping.put("pasteondo", "pasteondo"); //ﾍﾟｰｽﾄ温度1
        mapping.put("seihanno", "seihanno"); //製版No
        mapping.put("seihanmaisuu", "seihanmaisuu"); //製版枚数
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("enddatetime", "enddatetime"); //ﾌﾟﾘﾝﾄ終了日時
        mapping.put("skeegeno", "skeegeno"); //ｽｷｰｼﾞNo
        mapping.put("skeegemaisuu", "skeegemaisuu"); //ｽｷｰｼﾞ枚数
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("tantousya", "tantousya"); //担当者ｺｰﾄﾞ
        mapping.put("kakuninsya", "kakuninsya"); //確認者ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("prnprofile", "prnprofile"); //印刷ﾌﾟﾛﾌｧｲﾙ
        mapping.put("kansoutime", "kansoutime"); //乾燥時間
        mapping.put("saatu", "saatu"); //差圧
        mapping.put("skeegespeed", "skeegespeed"); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        mapping.put("skeegeangle", "skeegeangle"); //ｽｷｰｼﾞ角度
        mapping.put("mld", "mld"); //ﾒﾀﾙﾚｲﾀﾞｳﾝ値
        mapping.put("clearrance", "clearrance"); //ｸﾘｱﾗﾝｽ設定値
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("makuatu1", "makuatu1"); //膜厚1
        mapping.put("makuatu2", "makuatu2"); //膜厚2
        mapping.put("makuatu3", "makuatu3"); //膜厚3
        mapping.put("makuatu4", "makuatu4"); //膜厚4
        mapping.put("makuatu5", "makuatu5"); //膜厚5
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastelotno3", "pastelotno3"); //ﾍﾟｰｽﾄﾛｯﾄNo3
        mapping.put("pastelotno4", "pastelotno4"); //ﾍﾟｰｽﾄﾛｯﾄNo4
        mapping.put("pastelotno5", "pastelotno5"); //ﾍﾟｰｽﾄﾛｯﾄNo5
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pastenendo3", "pastenendo3"); //ﾍﾟｰｽﾄ粘度3
        mapping.put("pastenendo4", "pastenendo4"); //ﾍﾟｰｽﾄ粘度4
        mapping.put("pastenendo5", "pastenendo5"); //ﾍﾟｰｽﾄ粘度5
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pasteondo3", "pasteondo3"); //ﾍﾟｰｽﾄ温度3
        mapping.put("pasteondo4", "pasteondo4"); //ﾍﾟｰｽﾄ温度4
        mapping.put("pasteondo5", "pasteondo5"); //ﾍﾟｰｽﾄ温度5
        mapping.put("bikou3", "bikou3"); //備考3
        mapping.put("bikou4", "bikou4"); //備考4
        mapping.put("bikou5", "bikou5"); //備考5
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("skeegemaisuu2", "skeegemaisuu2"); //ｽｷｰｼﾞ枚数2
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("taperollno4", "taperollno4"); //ﾃｰﾌﾟﾛｰﾙNo4
        mapping.put("taperollno5", "taperollno5"); //ﾃｰﾌﾟﾛｰﾙNo5
        mapping.put("pastehinmei", "pastehinmei"); //ﾍﾟｰｽﾄ品名
        mapping.put("seihanmei", "seihanmei"); //製版名
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("tanto_setting", "tantoSetting"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("makuatuave_end", "makuatuaveEnd"); //終了時膜厚AVE
        mapping.put("makuatumax_end", "makuatumaxEnd"); //終了時膜厚MAX
        mapping.put("makuatumin_end", "makuatuminEnd"); //終了時膜厚MIN
        mapping.put("end_ptn_dist_x", "endPtnDistX"); //終了時PTN間距離X
        mapping.put("end_ptn_dist_y", "endPtnDistY"); //終了時PTN間距離Y
        mapping.put("tanto_end", "tantoEnd"); //終了時担当者ｺｰﾄﾞ
        mapping.put("kcpno", "kcpno"); //KCPNO
        mapping.put("sijiondo", "sijiondo"); //指示乾燥温度1
        mapping.put("sijiondo2", "sijiondo2"); //指示乾燥温度2
        mapping.put("sijiondo3", "sijiondo3"); //指示乾燥温度3
        mapping.put("sijiondo4", "sijiondo4"); //指示乾燥温度4
        mapping.put("sijiondo5", "sijiondo5"); //指示乾燥温度5
        mapping.put("pkokeibun1", "pkokeibun1"); //ﾍﾟｰｽﾄ固形分1
        mapping.put("pkokeibun2", "pkokeibun2"); //ﾍﾟｰｽﾄ固形分2
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("makuatucv_start", "makuatucvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("makuatucv_end", "makuatucvEnd"); //印刷ｴﾝﾄﾞ膜厚CV
        mapping.put("nijimikasure_end", "nijimikasureEnd"); //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("printmaisuu", "printmaisuu"); //印刷枚数
        mapping.put("table_clearrance", "tableClearrance"); //ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        mapping.put("scraperspeed", "scraperspeed"); //ｽｸﾚｯﾊﾟｰ速度
        mapping.put("skeegegaikan", "skeegegaikan"); //ｽｷｰｼﾞ外観
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprintScr>> beanHandler = new BeanListHandler<>(SrSpsprintScr.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (9)[印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintScr> loadTmpSubSrSpsprintScr(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        String sql = "SELECT kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,"
                + "makuatsu_start3,makuatsu_start4,makuatsu_start5,"
                + "makuatsu_start6,makuatsu_start7,makuatsu_start8,"
                + "makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,"
                + "start_ptn_dist_x3,start_ptn_dist_x4,start_ptn_dist_x5,"
                + "start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,"
                + "start_ptn_dist_y4,start_ptn_dist_y5,makuatsu_end1,"
                + "makuatsu_end2,makuatsu_end3,makuatsu_end4,"
                + "makuatsu_end5,makuatsu_end6,makuatsu_end7,"
                + "makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,"
                + "end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,"
                + "end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,"
                + "torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sub_sr_spsprint_scr "
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
        mapping.put("kojyo", "kojyo"); //工場ｺｰﾄﾞ
        mapping.put("lotno", "lotno"); //ﾛｯﾄNo
        mapping.put("edaban", "edaban"); //枝番
        mapping.put("makuatsu_start1", "makuatsuStart1"); //膜厚ｽﾀｰﾄ1
        mapping.put("makuatsu_start2", "makuatsuStart2"); //膜厚ｽﾀｰﾄ2
        mapping.put("makuatsu_start3", "makuatsuStart3"); //膜厚ｽﾀｰﾄ3
        mapping.put("makuatsu_start4", "makuatsuStart4"); //膜厚ｽﾀｰﾄ4
        mapping.put("makuatsu_start5", "makuatsuStart5"); //膜厚ｽﾀｰﾄ5
        mapping.put("makuatsu_start6", "makuatsuStart6"); //膜厚ｽﾀｰﾄ6
        mapping.put("makuatsu_start7", "makuatsuStart7"); //膜厚ｽﾀｰﾄ7
        mapping.put("makuatsu_start8", "makuatsuStart8"); //膜厚ｽﾀｰﾄ8
        mapping.put("makuatsu_start9", "makuatsuStart9"); //膜厚ｽﾀｰﾄ9
        mapping.put("start_ptn_dist_x1", "startPtnDistX1"); //PTN距離X ｽﾀｰﾄ1
        mapping.put("start_ptn_dist_x2", "startPtnDistX2"); //PTN距離X ｽﾀｰﾄ2
        mapping.put("start_ptn_dist_x3", "startPtnDistX3"); //PTN距離X ｽﾀｰﾄ3
        mapping.put("start_ptn_dist_x4", "startPtnDistX4"); //PTN距離X ｽﾀｰﾄ4
        mapping.put("start_ptn_dist_x5", "startPtnDistX5"); //PTN距離X ｽﾀｰﾄ5
        mapping.put("start_ptn_dist_y1", "startPtnDistY1"); //PTN距離Y ｽﾀｰﾄ1
        mapping.put("start_ptn_dist_y2", "startPtnDistY2"); //PTN距離Y ｽﾀｰﾄ2
        mapping.put("start_ptn_dist_y3", "startPtnDistY3"); //PTN距離Y ｽﾀｰﾄ3
        mapping.put("start_ptn_dist_y4", "startPtnDistY4"); //PTN距離Y ｽﾀｰﾄ4
        mapping.put("start_ptn_dist_y5", "startPtnDistY5"); //PTN距離Y ｽﾀｰﾄ5
        mapping.put("makuatsu_end1", "makuatsuEnd1"); //膜厚ｴﾝﾄﾞ1
        mapping.put("makuatsu_end2", "makuatsuEnd2"); //膜厚ｴﾝﾄﾞ2
        mapping.put("makuatsu_end3", "makuatsuEnd3"); //膜厚ｴﾝﾄﾞ3
        mapping.put("makuatsu_end4", "makuatsuEnd4"); //膜厚ｴﾝﾄﾞ4
        mapping.put("makuatsu_end5", "makuatsuEnd5"); //膜厚ｴﾝﾄﾞ5
        mapping.put("makuatsu_end6", "makuatsuEnd6"); //膜厚ｴﾝﾄﾞ6
        mapping.put("makuatsu_end7", "makuatsuEnd7"); //膜厚ｴﾝﾄﾞ7
        mapping.put("makuatsu_end8", "makuatsuEnd8"); //膜厚ｴﾝﾄﾞ8
        mapping.put("makuatsu_end9", "makuatsuEnd9"); //膜厚ｴﾝﾄﾞ9
        mapping.put("end_ptn_dist_x1", "endPtnDistX1"); //PTN距離X ｴﾝﾄﾞ1
        mapping.put("end_ptn_dist_x2", "endPtnDistX2"); //PTN距離X ｴﾝﾄﾞ2
        mapping.put("end_ptn_dist_x3", "endPtnDistX3"); //PTN距離X ｴﾝﾄﾞ3
        mapping.put("end_ptn_dist_x4", "endPtnDistX4"); //PTN距離X ｴﾝﾄﾞ4
        mapping.put("end_ptn_dist_x5", "endPtnDistX5"); //PTN距離X ｴﾝﾄﾞ5
        mapping.put("end_ptn_dist_y1", "endPtnDistY1"); //PTN距離Y ｴﾝﾄﾞ1
        mapping.put("end_ptn_dist_y2", "endPtnDistY2"); //PTN距離Y ｴﾝﾄﾞ2
        mapping.put("end_ptn_dist_y3", "endPtnDistY3"); //PTN距離Y ｴﾝﾄﾞ3
        mapping.put("end_ptn_dist_y4", "endPtnDistY4"); //PTN距離Y ｴﾝﾄﾞ4
        mapping.put("end_ptn_dist_y5", "endPtnDistY5"); //PTN距離Y ｴﾝﾄﾞ5
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SubSrSpsprintScr>> beanHandler = new BeanListHandler<>(SubSrSpsprintScr.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (12)[製版ﾏｽﾀ]から、ﾃﾞｰﾀを取得
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
            QueryRunner queryRunnerQcdb = new QueryRunner(processData.getDataSourceQcdb());

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            String formId = StringUtil.nullToBlank(session.getAttribute("formId"));
            String lotNo = (String) session.getAttribute("lotNo");
            String kojyo = lotNo.substring(0, 3);
            String lotNo8 = lotNo.substring(3, 11);
            String edaban = lotNo.substring(11, 14);

            //仕掛情報の取得
            Map shikakariData = loadShikakariData(queryRunnerDoc, lotNo);
            String oyalotEdaban = StringUtil.nullToBlank(getMapData(shikakariData, "oyalotedaban")); //親ﾛｯﾄ枝番

            // 品質DB登録実績データ取得
            Map fxhdd03RevInfo = loadFxhdd03RevInfo(queryRunnerDoc, kojyo, lotNo8, oyalotEdaban, formId);
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            String jotaiFlg = StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "jotai_flg"));

            // 印刷SPSｽｸﾘｰﾝデータ取得
            List<SrSpsprintScr> srSpsprintScrDataList = getSrSpsprintScrData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srSpsprintScrDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面データ取得
            List<SubSrSpsprintScr> subSrSpsprintScrDataList = getSubSrSpsprintScrData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (subSrSpsprintScrDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSpsprintScrDataList.get(0));

            // 膜厚入力画面データ設定 ※工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番は親ではなく自身の値を渡す。
            setInputItemDataSubFormC001(subSrSpsprintScrDataList.get(0), kojyo, lotNo8, edaban);

            // PTN距離X入力画面データ設定
            setInputItemDataSubFormC002(subSrSpsprintScrDataList.get(0));

            // PTN距離Y入力画面データ設定
            setInputItemDataSubFormC003(subSrSpsprintScrDataList.get(0));

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
        FXHDD01 fxhdd01
                = processData.getItemList().stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList()).get(0);
        fxhdd01.setValue(value);
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
     * @return 入力値
     */
    private String getItemData(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getValue();
        } else {
            return null;
        }
    }

    /**
     * 項目データ(項目名)取得
     *
     * @param listData フォームデータ
     * @param itemId 項目ID
     * @return 入力値
     */
    private String getItemName(List<FXHDD01> listData, String itemId) {
        List<FXHDD01> selectData
                = listData.stream().filter(n -> itemId.equals(n.getItemId())).collect(Collectors.toList());
        if (null != selectData && 0 < selectData.size()) {
            return selectData.get(0).getLabel1();
        } else {
            return null;
        }
    }

    /**
     * INSERT、UPDATE、DELTEに失敗した場合のエラーを生成します。
     *
     * @param processData 処理制御データ
     * @return 処理制御データ
     */
    private ProcessData createRegistDataErrorMessage(ProcessData processData) {
        if (null != processData.getProcessName()) {
            switch (processData.getProcessName()) {
                case "resist":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("登録に失敗しました。")));
                    break;
                case "tempResist":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("仮登録に失敗しました。")));
                    break;
                case "correct":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("修正に失敗しました。")));

                    break;
                case "delete":
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo("削除に失敗しました。")));

                    break;
                default:
                    break;
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
     * @throws SQLException 例外ｴﾗｰ
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
        // 焼成データの登録
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
     * 印刷SPSｽｸﾘｰﾝ_仮登録(tmp_sr_spsprint_scr)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目ﾘｽﾄ
     * @throws SQLException 例外ｴﾗｰ
     */
    private void insertTmpSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        // 焼成データの登録
        String sql = "INSERT INTO tmp_sr_spsprint_scr ("
                + "kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,genryoukigou,pastelotno,pastenendo,pasteondo,"
                + "seihanno,seihanmaisuu,startdatetime,enddatetime,skeegeno,skeegemaisuu,gouki,tantousya,kakuninsya,"
                + "kansouondo,prnprofile,kansoutime,saatu,skeegespeed,skeegeangle,mld,clearrance,bikou1,bikou2,makuatu1,"
                + "makuatu2,makuatu3,makuatu4,makuatu5,pastelotno2,pastelotno3,pastelotno4,pastelotno5,pastenendo2,"
                + "pastenendo3,pastenendo4,pastenendo5,pasteondo2,pasteondo3,pasteondo4,pasteondo5,bikou3,bikou4,bikou5,"
                + "kansouondo2,kansouondo3,kansouondo4,kansouondo5,skeegemaisuu2,taperollno1,taperollno2,taperollno3,"
                + "taperollno4,taperollno5,pastehinmei,seihanmei,makuatuave_start,makuatumax_start,makuatumin_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,tanto_setting,makuatuave_end,makuatumax_end,makuatumin_end,"
                + "end_ptn_dist_x,end_ptn_dist_y,tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,sijiondo4,sijiondo5,"
                + "pkokeibun1,pkokeibun2,petfilmsyurui,makuatucv_start,nijimikasure_start,makuatucv_end,nijimikasure_end,"
                + "printmaisuu,table_clearrance,scraperspeed,skeegegaikan,torokunichiji,kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSpsprintScr(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_仮登録(tmp_sr_spsprint_scr)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目ﾘｽﾄ
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateTmpSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        // 焼成データの登録
        String sql = "UPDATE tmp_sr_spsprint_scr SET "
                + "tapelotno=?,genryoukigou=?,pastelotno=?,pastenendo=?,pasteondo=?,seihanno=?,"
                + "seihanmaisuu=?,startdatetime=?,enddatetime=?,skeegeno=?,skeegemaisuu=?,gouki=?,kansouondo=?,saatu=?,"
                + "skeegespeed=?,skeegeangle=?,bikou1=?,bikou2=?,pastelotno2=?,pastenendo2=?,pasteondo2=?,kansouondo2=?,"
                + "kansouondo3=?,kansouondo4=?,kansouondo5=?,taperollno1=?,taperollno2=?,taperollno3=?,seihanmei=?,makuatuave_start=?,"
                + "makuatumax_start=?,makuatumin_start=?,start_ptn_dist_x=?,start_ptn_dist_y=?,tanto_setting=?,makuatuave_end=?,"
                + "makuatumax_end=?,makuatumin_end=?,end_ptn_dist_x=?,end_ptn_dist_y=?,tanto_end=?,kcpno=?,pkokeibun1=?,pkokeibun2=?,"
                + "petfilmsyurui=?,makuatucv_start=?,nijimikasure_start=?,makuatucv_end=?,nijimikasure_end=?,printmaisuu=?,"
                + "table_clearrance=?,scraperspeed=?,skeegegaikan=?,kosinnichiji=?,revision=?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSpsprintScr(false, newRev, 0, "", "", "", systemTime, itemList);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_仮登録(tmp_sr_spsprint_scr)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteTmpSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        // 焼成データの登録
        String sql = "DELETE FROM tmp_sr_spsprint_scr "
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
     * 印刷SPSｽｸﾘｰﾝ_仮登録(tmp_sr_spsprint_scr)更新値ﾊﾟﾗﾒｰﾀ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目ﾘｽﾄ
     * @return 更新ﾊﾟﾗﾒｰﾀ
     */
    private List<Object> setUpdateParameterTmpSrSpsprintScr(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo,
            String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(""); //ﾃｰﾌﾟ種類
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SLIP_LOTNO))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.GENRYO_KIGOU))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.PASTE_LOT_NO1))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_NENDO1))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_ONDO1))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SEIHAN_OR_HANDOU_NO))); //製版No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU))); //製版枚数
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B002Const.INSATSU_KAISHI_DAY),
                getItemData(itemList, GXHDO101B002Const.INSATSU_KAISHI_TIME))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B002Const.INSATSU_SHUURYOU_DAY),
                getItemData(itemList, GXHDO101B002Const.INSATSU_SHUURYOU_TIME))); //ﾌﾟﾘﾝﾄ終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_OR_ATSUDOU_NO))); //ｽｷｰｼﾞNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_SHIYOUSUU_KAISHIJI))); //ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.INSATSU_GOUKI))); //号機ｺｰﾄﾞ
        if (isInsert) {
            params.add(""); //担当者ｺｰﾄﾞ
            params.add(""); //確認者ｺｰﾄﾞ
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI1))); //乾燥温度
        if (isInsert) {
            params.add(""); //印刷ﾌﾟﾛﾌｧｲﾙ
            params.add(0); //乾燥時間
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.SAATSU))); //差圧
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_SOKUDO))); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_KAKUDO))); //ｽｷｰｼﾞ角度
        if (isInsert) {
            params.add(0); //ﾒﾀﾙﾚｲﾀﾞｳﾝ値
            params.add(0); //ｸﾘｱﾗﾝｽ設定値
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.BIKOU1))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.BIKOU2))); //備考2
        if (isInsert) {
            params.add(0); //膜厚1
            params.add(0); //膜厚2
            params.add(0); //膜厚3
            params.add(0); //膜厚4
            params.add(0); //膜厚5
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.PASTE_LOT_NO2))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        if (isInsert) {
            params.add(""); //ﾍﾟｰｽﾄﾛｯﾄNo3
            params.add(""); //ﾍﾟｰｽﾄﾛｯﾄNo4
            params.add(""); //ﾍﾟｰｽﾄﾛｯﾄNo5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_NENDO2))); //ﾍﾟｰｽﾄ粘度2
        if (isInsert) {
            params.add(0); //ﾍﾟｰｽﾄ粘度3
            params.add(0); //ﾍﾟｰｽﾄ粘度4
            params.add(0); //ﾍﾟｰｽﾄ粘度5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_ONDO2))); //ﾍﾟｰｽﾄ温度2
        if (isInsert) {
            params.add(0); //ﾍﾟｰｽﾄ温度3
            params.add(0); //ﾍﾟｰｽﾄ温度4
            params.add(0); //ﾍﾟｰｽﾄ温度5
            params.add(""); //備考3
            params.add(""); //備考4
            params.add(""); //備考5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI2))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI3))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI4))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI5))); //乾燥温度5
        if (isInsert) {
            params.add(0); //ｽｷｰｼﾞ枚数2
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.ROLL_NO1))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.ROLL_NO2))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.ROLL_NO3))); //ﾃｰﾌﾟﾛｰﾙNo3
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟﾛｰﾙNo4
            params.add(""); //ﾃｰﾌﾟﾛｰﾙNo5
            params.add(""); //ﾍﾟｰｽﾄ品名
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SEIHAN_OR_HANDOU_MEI))); //製版名
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_AVE))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_MAX))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_MIN))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_START_X_MIN))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_START_Y_MIN))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.INSATSU_STARTJI_TANTOUSHA))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_AVE))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_MAX))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_MIN))); //終了時膜厚MIN
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_END_X_MIN))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_END_Y_MIN))); //終了時PTN間距離Y
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.INSATSU_ENDJI_TANTOUSHA))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.KCPNO))); //KCPNO
        if (isInsert) {
            params.add(0); //指示乾燥温度1
            params.add(0); //指示乾燥温度2
            params.add(0); //指示乾燥温度3
            params.add(0); //指示乾燥温度4
            params.add(0); //指示乾燥温度5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_KOKEIBUN1))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_KOKEIBUN2))); //ﾍﾟｰｽﾄ固形分2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.PET_FILM_SHURUI))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_CV))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK))) {
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
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_CV))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.INSATSU_MAISUU))); //印刷枚数

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.TABLE_CLEARANCE))); //ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.SCRAPER_SOKUDO))); //ｽｸﾚｯﾊﾟｰ速度
        //ｽｷｰｼﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_GAIKAN))) {
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
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_scr)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外ｴﾗｰ
     */
    private void insertTmpSubSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_spsprint_scr ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSubSrSpsprintScr(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_scr)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateTmpSubSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_spsprint_scr SET "
                + "makuatsu_start1 = ?,makuatsu_start2 = ?,makuatsu_start3 = ?,makuatsu_start4 = ?,makuatsu_start5 = ?,"
                + "makuatsu_start6 = ?,makuatsu_start7 = ?,makuatsu_start8 = ?,makuatsu_start9 = ?,"
                + "start_ptn_dist_x1 = ?,start_ptn_dist_x2 = ?,start_ptn_dist_x3 = ?,start_ptn_dist_x4 = ?,start_ptn_dist_x5 = ?,"
                + "start_ptn_dist_y1 = ?,start_ptn_dist_y2 = ?,start_ptn_dist_y3 = ?,start_ptn_dist_y4 = ?,start_ptn_dist_y5 = ?,"
                + "makuatsu_end1 = ?,makuatsu_end2 = ?,makuatsu_end3 = ?,makuatsu_end4 = ?,makuatsu_end5 = ?,"
                + "makuatsu_end6 = ?,makuatsu_end7 = ?,makuatsu_end8 = ?,makuatsu_end9 = ?,"
                + "end_ptn_dist_x1 = ?,end_ptn_dist_x2 = ?,end_ptn_dist_x3 = ?,end_ptn_dist_x4 = ?,end_ptn_dist_x5 = ?,"
                + "end_ptn_dist_y1 = ?,end_ptn_dist_y2 = ?,end_ptn_dist_y3 = ?,end_ptn_dist_y4 = ?,end_ptn_dist_y5 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterTmpSubSrSpsprintScr(false, newRev, 0, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_scr)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteTmpSubSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_spsprint_scr "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

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
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_scr)更新値ﾊﾟﾗﾒｰﾀ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @return 更新ﾊﾟﾗﾒｰﾀ
     */
    private List<Object> setUpdateParameterTmpSubSrSpsprintScr(boolean isInsert, BigDecimal newRev,
            int deleteflag, String kojyo, String lotNo, String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = beanGXHDO101C001.getGxhdO101c001Model().getMakuatsuDataList();
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        List<GXHDO101C002Model.PtnKyoriXData> ptnKyoriXDataList = beanGXHDO101C002.getGxhdO101c002Model().getPtnKyoriXDataList();
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList = beanGXHDO101C003.getGxhdO101c003Model().getPtnKyoriYDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番

        }
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(0).getStartVal())); //膜厚ｽﾀｰﾄ1
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(1).getStartVal())); //膜厚ｽﾀｰﾄ2
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(2).getStartVal())); //膜厚ｽﾀｰﾄ3
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(3).getStartVal())); //膜厚ｽﾀｰﾄ4
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(4).getStartVal())); //膜厚ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(5).getStartVal())); //膜厚ｽﾀｰﾄ6
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(6).getStartVal())); //膜厚ｽﾀｰﾄ7
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(7).getStartVal())); //膜厚ｽﾀｰﾄ8
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(8).getStartVal())); //膜厚ｽﾀｰﾄ9
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(0).getStartVal())); //PTN距離X ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(1).getStartVal())); //PTN距離X ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(2).getStartVal())); //PTN距離X ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(3).getStartVal())); //PTN距離X ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(4).getStartVal())); //PTN距離X ｽﾀｰﾄ5
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(0).getStartVal())); //PTN距離Y ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(1).getStartVal())); //PTN距離Y ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(2).getStartVal())); //PTN距離Y ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(3).getStartVal())); //PTN距離Y ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(4).getStartVal())); //PTN距離Y ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(0).getEndVal())); //膜厚ｴﾝﾄﾞ1
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(1).getEndVal())); //膜厚ｴﾝﾄﾞ2
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(2).getEndVal())); //膜厚ｴﾝﾄﾞ3
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(3).getEndVal())); //膜厚ｴﾝﾄﾞ4
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(4).getEndVal())); //膜厚ｴﾝﾄﾞ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(5).getEndVal())); //膜厚ｴﾝﾄﾞ6
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(6).getEndVal())); //膜厚ｴﾝﾄﾞ7
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(7).getEndVal())); //膜厚ｴﾝﾄﾞ8
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(8).getEndVal())); //膜厚ｴﾝﾄﾞ9
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(0).getEndVal())); //PTN距離X ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(1).getEndVal())); //PTN距離X ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(2).getEndVal())); //PTN距離X ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(3).getEndVal())); //PTN距離X ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(4).getEndVal())); //PTN距離X ｴﾝﾄﾞ5
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(0).getEndVal())); //PTN距離Y ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(1).getEndVal())); //PTN距離Y ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(2).getEndVal())); //PTN距離Y ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(3).getEndVal())); //PTN距離Y ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(4).getEndVal())); //PTN距離Y ｴﾝﾄﾞ5
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
     * 印刷SPSｽｸﾘｰﾝ(sr_spsprint)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param newRev 新Revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目ﾘｽﾄ
     * @throws SQLException 例外ｴﾗｰ
     */
    private void insertSrSpsprint(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "INSERT INTO sr_spsprint ("
                + "kojyo,lotno,edaban,tapesyurui,tapelotno,TapeSlipKigo,genryoukigou,pastelotno,pastenendo,pasteondo,"
                + "seihanno,seihanmaisuu,startdatetime,enddatetime,skeegeno,skeegemaisuu,gouki,tantousya,kakuninsya,"
                + "kansouondo,prnprofile,kansoutime,saatu,skeegespeed,skeegeangle,mld,clearrance,bikou1,bikou2,makuatu1,"
                + "makuatu2,makuatu3,makuatu4,makuatu5,pastelotno2,pastelotno3,pastelotno4,pastelotno5,pastenendo2,"
                + "pastenendo3,pastenendo4,pastenendo5,pasteondo2,pasteondo3,pasteondo4,pasteondo5,bikou3,bikou4,bikou5,"
                + "kansouondo2,kansouondo3,kansouondo4,kansouondo5,skeegemaisuu2,taperollno1,taperollno2,taperollno3,"
                + "taperollno4,taperollno5,pastehinmei,seihanmei,makuatuave_start,makuatumax_start,makuatumin_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,tanto_setting,makuatuave_end,makuatumax_end,makuatumin_end,"
                + "end_ptn_dist_x,end_ptn_dist_y,tanto_end,kcpno,sijiondo,sijiondo2,sijiondo3,sijiondo4,sijiondo5,"
                + "pkokeibun1,pkokeibun2,petfilmsyurui,makuatucv_start,nijimikasure_start,makuatucv_end,nijimikasure_end,"
                + "printmaisuu,table_clearrance,scraperspeed,skeegegaikan,torokunichiji,kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSpsprintScr(true, newRev, kojyo, lotNo, edaban, systemTime, itemList);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ(sr_spsprint)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目ﾘｽﾄ
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_spsprint SET "
                + "tapelotno=?,genryoukigou=?,pastelotno=?,pastenendo=?,pasteondo=?,seihanno=?,"
                + "seihanmaisuu=?,startdatetime=?,enddatetime=?,skeegeno=?,skeegemaisuu=?,gouki=?,kansouondo=?,saatu=?,"
                + "skeegespeed=?,skeegeangle=?,bikou1=?,bikou2=?,pastelotno2=?,pastenendo2=?,pasteondo2=?,kansouondo2=?,"
                + "kansouondo3=?,kansouondo4=?,kansouondo5=?,taperollno1=?,taperollno2=?,taperollno3=?,seihanmei=?,makuatuave_start=?,"
                + "makuatumax_start=?,makuatumin_start=?,start_ptn_dist_x=?,start_ptn_dist_y=?,tanto_setting=?,makuatuave_end=?,"
                + "makuatumax_end=?,makuatumin_end=?,end_ptn_dist_x=?,end_ptn_dist_y=?,tanto_end=?,kcpno=?,pkokeibun1=?,pkokeibun2=?,"
                + "petfilmsyurui=?,makuatucv_start=?,nijimikasure_start=?,makuatucv_end=?,nijimikasure_end=?,printmaisuu=?,"
                + "table_clearrance=?,scraperspeed=?,skeegegaikan=?,kosinnichiji=?,revision=? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        //更新値設定
        List<Object> params = setUpdateParameterSrSpsprintScr(false, newRev, "", "", "", systemTime, itemList);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ(sr_spsprint)更新値ﾊﾟﾗﾒｰﾀ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @param itemList 項目ﾘｽﾄ
     * @return 更新ﾊﾟﾗﾒｰﾀ
     */
    private List<Object> setUpdateParameterSrSpsprintScr(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo,
            String edaban, Timestamp systemTime, List<FXHDD01> itemList) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
            params.add(""); //ﾃｰﾌﾟ種類
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SLIP_LOTNO))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟｽﾘｯﾌﾟ記号
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.GENRYO_KIGOU))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.PASTE_LOT_NO1))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_NENDO1))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_ONDO1))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SEIHAN_OR_HANDOU_NO))); //製版No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU))); //製版枚数
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B002Const.INSATSU_KAISHI_DAY),
                getItemData(itemList, GXHDO101B002Const.INSATSU_KAISHI_TIME))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B002Const.INSATSU_SHUURYOU_DAY),
                getItemData(itemList, GXHDO101B002Const.INSATSU_SHUURYOU_TIME))); //ﾌﾟﾘﾝﾄ終了日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_OR_ATSUDOU_NO))); //ｽｷｰｼﾞNo
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_SHIYOUSUU_KAISHIJI))); //ｽｷｰｼﾞ枚数
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.INSATSU_GOUKI))); //号機ｺｰﾄﾞ
        if (isInsert) {
            params.add(""); //担当者ｺｰﾄﾞ
            params.add(""); //確認者ｺｰﾄﾞ
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI1))); //乾燥温度
        if (isInsert) {
            params.add(""); //印刷ﾌﾟﾛﾌｧｲﾙ
            params.add(0); //乾燥時間
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.SAATSU))); //差圧
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_SOKUDO))); //ｽｷｰｼﾞｽﾋﾟｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_KAKUDO))); //ｽｷｰｼﾞ角度
        if (isInsert) {
            params.add(0); //ﾒﾀﾙﾚｲﾀﾞｳﾝ値
            params.add(0); //ｸﾘｱﾗﾝｽ設定値
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.BIKOU1))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.BIKOU2))); //備考2
        if (isInsert) {
            params.add(0); //膜厚1
            params.add(0); //膜厚2
            params.add(0); //膜厚3
            params.add(0); //膜厚4
            params.add(0); //膜厚5
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.PASTE_LOT_NO2))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        if (isInsert) {
            params.add(""); //ﾍﾟｰｽﾄﾛｯﾄNo3
            params.add(""); //ﾍﾟｰｽﾄﾛｯﾄNo4
            params.add(""); //ﾍﾟｰｽﾄﾛｯﾄNo5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_NENDO2))); //ﾍﾟｰｽﾄ粘度2
        if (isInsert) {
            params.add(0); //ﾍﾟｰｽﾄ粘度3
            params.add(0); //ﾍﾟｰｽﾄ粘度4
            params.add(0); //ﾍﾟｰｽﾄ粘度5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_ONDO2))); //ﾍﾟｰｽﾄ温度2
        if (isInsert) {
            params.add(0); //ﾍﾟｰｽﾄ温度3
            params.add(0); //ﾍﾟｰｽﾄ温度4
            params.add(0); //ﾍﾟｰｽﾄ温度5
            params.add(""); //備考3
            params.add(""); //備考4
            params.add(""); //備考5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI2))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI3))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI4))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.KANSOU_ONDO_HYOUJICHI5))); //乾燥温度5
        if (isInsert) {
            params.add(0); //ｽｷｰｼﾞ枚数2
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.ROLL_NO1))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.ROLL_NO2))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.ROLL_NO3))); //ﾃｰﾌﾟﾛｰﾙNo3
        if (isInsert) {
            params.add(""); //ﾃｰﾌﾟﾛｰﾙNo4
            params.add(""); //ﾃｰﾌﾟﾛｰﾙNo5
            params.add(""); //ﾍﾟｰｽﾄ品名
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.SEIHAN_OR_HANDOU_MEI))); //製版名
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_AVE))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_MAX))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_MIN))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_START_X_MIN))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_START_Y_MIN))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.INSATSU_STARTJI_TANTOUSHA))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_AVE))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_MAX))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_MIN))); //終了時膜厚MIN
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_END_X_MIN))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.PTN_INSATSU_END_Y_MIN))); //終了時PTN間距離Y
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.INSATSU_ENDJI_TANTOUSHA))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.KCPNO))); //KCPNO
        if (isInsert) {
            params.add(0); //指示乾燥温度1
            params.add(0); //指示乾燥温度2
            params.add(0); //指示乾燥温度3
            params.add(0); //指示乾燥温度4
            params.add(0); //指示乾燥温度5
        }
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_KOKEIBUN1))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.PASTE_KOKEIBUN2))); //ﾍﾟｰｽﾄ固形分2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B002Const.PET_FILM_SHURUI))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_START_MAKUATSU_CV))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B002Const.STARTJI_NIJIMI_KASURE_CHECK))) {
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
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.INSATSU_END_MAKUATSU_CV))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B002Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.INSATSU_MAISUU))); //印刷枚数

        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B002Const.TABLE_CLEARANCE))); //ﾃｰﾌﾞﾙｸﾘｱﾗﾝｽ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B002Const.SCRAPER_SOKUDO))); //ｽｸﾚｯﾊﾟｰ速度
        //ｽｷｰｼﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B002Const.SQUEEGEE_GAIKAN))) {
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
     * 印刷SPSｽｸﾘｰﾝ(sr_spsprint)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        // 焼成データの登録
        String sql = "DELETE FROM sr_spsprint "
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
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面(sub_sr_spsprint_scr)登録処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外ｴﾗｰ
     */
    private void insertSubSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_spsprint_scr ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSubSrSpsprintScr(true, newRev, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面(sub_sr_spsprint_scr)更新処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param newRev 新revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @throws SQLException 例外ｴﾗｰ
     */
    private void updateSubSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE sub_sr_spsprint_scr SET "
                + "makuatsu_start1 = ?,makuatsu_start2 = ?,makuatsu_start3 = ?,makuatsu_start4 = ?,makuatsu_start5 = ?,"
                + "makuatsu_start6 = ?,makuatsu_start7 = ?,makuatsu_start8 = ?,makuatsu_start9 = ?,"
                + "start_ptn_dist_x1 = ?,start_ptn_dist_x2 = ?,start_ptn_dist_x3 = ?,start_ptn_dist_x4 = ?,start_ptn_dist_x5 = ?,"
                + "start_ptn_dist_y1 = ?,start_ptn_dist_y2 = ?,start_ptn_dist_y3 = ?,start_ptn_dist_y4 = ?,start_ptn_dist_y5 = ?,"
                + "makuatsu_end1 = ?,makuatsu_end2 = ?,makuatsu_end3 = ?,makuatsu_end4 = ?,makuatsu_end5 = ?,"
                + "makuatsu_end6 = ?,makuatsu_end7 = ?,makuatsu_end8 = ?,makuatsu_end9 = ?,"
                + "end_ptn_dist_x1 = ?,end_ptn_dist_x2 = ?,end_ptn_dist_x3 = ?,end_ptn_dist_x4 = ?,end_ptn_dist_x5 = ?,"
                + "end_ptn_dist_y1 = ?,end_ptn_dist_y2 = ?,end_ptn_dist_y3 = ?,end_ptn_dist_y4 = ?,end_ptn_dist_y5 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        List<Object> params = setUpdateParameterSubSrSpsprintScr(false, newRev, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面登録(tmp_sub_sr_spsprint_scr)更新値ﾊﾟﾗﾒｰﾀ設定
     *
     * @param isInsert 登録判定(true:insert、false:update)
     * @param newRev 新revision
     * @param deleteflag 削除ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @param systemTime ｼｽﾃﾑ日付(品質DB登録実績に更新した値と同値)
     * @return 更新ﾊﾟﾗﾒｰﾀ
     */
    private List<Object> setUpdateParameterSubSrSpsprintScr(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, Timestamp systemTime) {
        List<Object> params = new ArrayList<>();

        // 子画面情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
        List<GXHDO101C001Model.MakuatsuData> makuatsuDataList = beanGXHDO101C001.getGxhdO101c001Model().getMakuatsuDataList();
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        List<GXHDO101C002Model.PtnKyoriXData> ptnKyoriXDataList = beanGXHDO101C002.getGxhdO101c002Model().getPtnKyoriXDataList();

        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        List<GXHDO101C003Model.PtnKyoriYData> ptnKyoriYDataList = beanGXHDO101C003.getGxhdO101c003Model().getPtnKyoriYDataList();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(0).getStartVal())); //膜厚ｽﾀｰﾄ1
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(1).getStartVal())); //膜厚ｽﾀｰﾄ2
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(2).getStartVal())); //膜厚ｽﾀｰﾄ3
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(3).getStartVal())); //膜厚ｽﾀｰﾄ4
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(4).getStartVal())); //膜厚ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(5).getStartVal())); //膜厚ｽﾀｰﾄ6
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(6).getStartVal())); //膜厚ｽﾀｰﾄ7
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(7).getStartVal())); //膜厚ｽﾀｰﾄ8
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(8).getStartVal())); //膜厚ｽﾀｰﾄ9
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(0).getStartVal())); //PTN距離X ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(1).getStartVal())); //PTN距離X ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(2).getStartVal())); //PTN距離X ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(3).getStartVal())); //PTN距離X ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(4).getStartVal())); //PTN距離X ｽﾀｰﾄ5
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(0).getStartVal())); //PTN距離Y ｽﾀｰﾄ1
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(1).getStartVal())); //PTN距離Y ｽﾀｰﾄ2
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(2).getStartVal())); //PTN距離Y ｽﾀｰﾄ3
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(3).getStartVal())); //PTN距離Y ｽﾀｰﾄ4
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(4).getStartVal())); //PTN距離Y ｽﾀｰﾄ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(0).getEndVal())); //膜厚ｴﾝﾄﾞ1
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(1).getEndVal())); //膜厚ｴﾝﾄﾞ2
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(2).getEndVal())); //膜厚ｴﾝﾄﾞ3
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(3).getEndVal())); //膜厚ｴﾝﾄﾞ4
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(4).getEndVal())); //膜厚ｴﾝﾄﾞ5
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(5).getEndVal())); //膜厚ｴﾝﾄﾞ6
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(6).getEndVal())); //膜厚ｴﾝﾄﾞ7
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(7).getEndVal())); //膜厚ｴﾝﾄﾞ8
        params.add(DBUtil.stringToBigDecimalObject(makuatsuDataList.get(8).getEndVal())); //膜厚ｴﾝﾄﾞ9
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(0).getEndVal())); //PTN距離X ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(1).getEndVal())); //PTN距離X ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(2).getEndVal())); //PTN距離X ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(3).getEndVal())); //PTN距離X ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObject(ptnKyoriXDataList.get(4).getEndVal())); //PTN距離X ｴﾝﾄﾞ5
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(0).getEndVal())); //PTN距離Y ｴﾝﾄﾞ1
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(1).getEndVal())); //PTN距離Y ｴﾝﾄﾞ2
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(2).getEndVal())); //PTN距離Y ｴﾝﾄﾞ3
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(3).getEndVal())); //PTN距離Y ｴﾝﾄﾞ4
        params.add(DBUtil.stringToIntObject(ptnKyoriYDataList.get(4).getEndVal())); //PTN距離Y ｴﾝﾄﾞ5
        if (isInsert) {
            params.add(systemTime); //登録日時
            //params.add(null); //更新日時
            params.add(systemTime); //更新日時
        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision

        return params;

    }

    /**
     * 印刷SPSｽｸﾘｰﾝ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_scr)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteSubSrSpsprintScr(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM sub_sr_spsprint_scr "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

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
     * [印刷SPSｽｸﾘｰﾝ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外ｴﾗｰ
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_spsprint_scr "
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
     * revisionﾁｪｯｸ
     *
     * @param processData
     * @param fxhdd03RevInfo
     * @return
     * @throws SQLException
     */
    private ErrorMessageInfo checkRevision(ProcessData processData, Map fxhdd03RevInfo) throws SQLException {

        if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
            // 新規の場合、データが存在する場合
            if (fxhdd03RevInfo != null && !fxhdd03RevInfo.isEmpty()) {
                return new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"));

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
     * @param processDate
     * @return
     */
    public ProcessData setKaishiDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B002Const.INSATSU_KAISHI_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B002Const.INSATSU_KAISHI_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }
        processDate.setMethod("");
        return processDate;
    }

    /**
     * 終了時間設定処理
     *
     * @param processDate
     * @return
     */
    public ProcessData setShuryouDateTime(ProcessData processDate) {
        FXHDD01 itemDay = getItemRow(processDate.getItemList(), GXHDO101B002Const.INSATSU_SHUURYOU_DAY);
        FXHDD01 itemTime = getItemRow(processDate.getItemList(), GXHDO101B002Const.INSATSU_SHUURYOU_TIME);
        if (StringUtil.isEmpty(itemDay.getValue()) && StringUtil.isEmpty(itemTime.getValue())) {
            setDateTimeItem(itemDay, itemTime, new Date());
        }

        processDate.setMethod("");
        return processDate;
    }

    /**
     * 日付(日、時間)の項目にフォーマットの日付(yyMMdd,HHmm)をセットする
     *
     * @param itemDay
     * @param itemTime
     * @param setDateTime
     */
    private void setDateTimeItem(FXHDD01 itemDay, FXHDD01 itemTime, Date setDateTime) {
        itemDay.setValue(new SimpleDateFormat("yyMMdd").format(setDateTime));
        itemTime.setValue(new SimpleDateFormat("HHmm").format(setDateTime));
    }

    /**
     * コネクションロールバック処理
     *
     * @param con
     */
    private void rollbackConnection(Connection con) {
        try {
            DbUtils.rollback(con);
        } catch (SQLException ex) {
            ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
        }
        DbUtils.closeQuietly(con);

    }

}
