/*
 * Copyright 2018 Kyocera Communication Systems Co., Ltd All rights reserved.
 */
package jp.co.kccs.xhd.pxhdo101;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import jp.co.kccs.xhd.db.model.SrSpsprint;
import jp.co.kccs.xhd.db.model.SrSpsprintGra;
import jp.co.kccs.xhd.db.model.SubSrSpsprintGra;
import jp.co.kccs.xhd.model.GXHDO101C001Model;
import jp.co.kccs.xhd.model.GXHDO101C002Model;
import jp.co.kccs.xhd.model.GXHDO101C003Model;
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
 * 変更日	2018/11/29<br>
 * 計画書No	K1803-DS001<br>
 * 変更者	SYSNAVI K.Hisanaga<br>
 * 変更理由	新規作成<br>
 * <br>
 * ===============================================================================<br>
 */
/**
 * GXHDO101B001(SPS系印刷・SPSグラビア)ロジック
 *
 * @author SYSNAVI K.Hisanaga
 * @since 2018/11/29
 */
public class GXHDO101B001 implements IFormLogic {

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

            // ボタンの活性・非活性を設定
            processData = this.setButtonEnable(processData, processData.getInitJotaiFlg());

            //******************************************************************************************
//            // 膜厚(SPS)サブ画面初期表示データ設定
//            GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);
//            beanGXHDO101C001.setGxhdO101c001Model(GXHDO101C001Logic.createGXHDO101C001Model(""));
//
//            // PTN距離Xサブ画面初期表示データ設定
//            GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
//            beanGXHDO101C002.setGxhdO101c002Model(GXHDO101C002Logic.createGXHDO101C002Model(""));
//
//            // PTN距離Yサブ画面初期表示データ設定
//            GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
//            beanGXHDO101C003.setGxhdO101c003Model(GXHDO101C003Logic.createGXHDO101C003Model(""));
            //サブ画面呼出しをチェック処理なし(処理時にエラーの背景色を戻さない機能として登録)
            processData.setNoCheckButtonId(Arrays.asList(
                    GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN,
                    GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                    GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                    GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                    GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                    GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN));
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

        //ブレード外観
        FXHDD01 itemBladeGikan = getItemRow(processData.getItemList(), GXHDO101B001Const.BLADE_GAIKAN);
        if ("NG".equals(itemBladeGikan.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBladeGikan);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemBladeGikan.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK);
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
            //TODO ここでロックを掛ける
            Map fxhdd03RevInfo = loadFxhdd03RevInfoWithLock(queryRunnerDoc, conDoc, kojyo, lotNo8, edaban, formId);
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 新規の場合、データが存在する場合
                if (fxhdd03RevInfo != null && !fxhdd03RevInfo.isEmpty()) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                    return processData;
                }
            } else {
                // 品質DB登録実績データが取得出来ていない場合エラー
                if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                    return processData;
                }

                // revisionが更新されていた場合エラー
                if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                    return processData;
                }
            }

            BigDecimal newRev = BigDecimal.ONE;
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());

            if (StringUtil.isEmpty(processData.getInitRev())) {
                // 品質DB登録実績登録処理
                insertFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, jissekiNo, JOTAI_FLG_KARI_TOROKU, systemTime);

                // 印刷SPSｸﾞﾗﾋﾞｱ_仮登録登録処理
                insertTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録登録処理
                insertTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, 0, kojyo, lotNo8, edaban, systemTime);

            } else {
                // revisionを1加算する。
                BigDecimal rev = new BigDecimal(processData.getInitRev());
                newRev = rev.add(BigDecimal.ONE);
                // 品質DB登録実績更新処理
                updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_KARI_TOROKU, systemTime);

                // 印刷SPSｸﾞﾗﾋﾞｱ_仮登録更新処理
                updateTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

                // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録更新処理
                updateTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);
            }

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (!kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            try {

                DbUtils.rollback(conDoc);
                DbUtils.rollback(conQcdb);
                //processData.getDataSourceQcdb().getConnection().rollback();
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            }
            DbUtils.closeQuietly(conDoc);
            DbUtils.closeQuietly(conQcdb);

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
        FXHDD01 itemBladeGikan = getItemRow(processData.getItemList(), GXHDO101B001Const.BLADE_GAIKAN);
        if ("NG".equals(itemBladeGikan.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemBladeGikan);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemBladeGikan.getLabel1());
        }

        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemStartjiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemStartjiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemStartjiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemStartjiNijimiKasure.getLabel1());
        }

        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        FXHDD01 itemShuryojiNijimiKasure = getItemRow(processData.getItemList(), GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK);
        if ("NG".equals(itemShuryojiNijimiKasure.getValue())) {
            // ｴﾗｰ項目をﾘｽﾄに追加
            List<FXHDD01> errFxhdd01List = Arrays.asList(itemShuryojiNijimiKasure);
            return MessageUtil.getErrorMessageInfo("XHD-000032", true, true, errFxhdd01List, itemShuryojiNijimiKasure.getLabel1());
        }

        ValidateUtil validateUtil = new ValidateUtil();
        // 開始日時、終了日時前後チェック
        FXHDD01 itemInsatsuKaishiDay = getItemRow(processData.getItemList(), GXHDO101B001Const.INSATSU_KAISHI_DAY); //印刷開始日
        FXHDD01 itemInsatsuKaishiTime = getItemRow(processData.getItemList(), GXHDO101B001Const.INSATSU_KAISHI_TIME); // 印刷開始時刻
        Date kaishiDate = DateUtil.convertStringToDate(itemInsatsuKaishiDay.getValue(), itemInsatsuKaishiTime.getValue());
        FXHDD01 itemInsatsuShuryouDay = getItemRow(processData.getItemList(), GXHDO101B001Const.INSATSU_SHUURYOU_DAY); //印刷終了日
        FXHDD01 itemInsatsuShuryouTime = getItemRow(processData.getItemList(), GXHDO101B001Const.INSATSU_SHUURYOU_TIME); //印刷終了時刻
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
            if (StringUtil.isEmpty(processData.getInitJotaiFlg())) {
                // 新規の場合、データが存在する場合
                if (fxhdd03RevInfo != null && !fxhdd03RevInfo.isEmpty()) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                    return processData;
                }
            } else {
                // 品質DB登録実績データが取得出来ていない場合エラー
                if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                    return processData;
                }

                // revisionが更新されていた場合エラー
                if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                    processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                    return processData;
                }
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
                deleteTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
                deleteTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);
            }

            // 印刷SPSｸﾞﾗﾋﾞｱ_登録処理
            insertSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面登録処理
            insertSubSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, kojyo, lotNo8, edaban, systemTime);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (!kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            try {

                DbUtils.rollback(conDoc);
                DbUtils.rollback(conQcdb);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            }
            DbUtils.closeQuietly(conDoc);
            DbUtils.closeQuietly(conQcdb);

            processData = createRegistDataErrorMessage(processData);
        }

        return processData;

    }

    public ProcessData checkAuthCorrect(ProcessData processData) {
        processData.setProcessName("correct");
        processData.setRquireAuth(true);
        processData.setUserAuthParam("auth_list");//TODO何を定義すればよいか?
        processData.setMethod("checkDataCorrect");
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
            // 品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                return processData;
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                return processData;
            }

            BigDecimal rev = new BigDecimal(processData.getInitRev());
            BigDecimal newRev = rev.add(BigDecimal.ONE);
            int jissekiNo = 1;
            Timestamp systemTime = new Timestamp(System.currentTimeMillis());
            // 品質DB登録実績更新処理
            updateFxhdd03(queryRunnerDoc, conDoc, tantoshaCd, formId, newRev, kojyo, lotNo8, edaban, JOTAI_FLG_TOROKUZUMI, systemTime);

            // 印刷SPSｸﾞﾗﾋﾞｱ_更新処理
            updateSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime, processData.getItemList());

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面更新処理
            updateSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, newRev, kojyo, lotNo8, edaban, systemTime);

            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (!kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            try {

                DbUtils.rollback(conDoc);
                DbUtils.rollback(conQcdb);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            }
            DbUtils.closeQuietly(conDoc);
            DbUtils.closeQuietly(conQcdb);

            processData = createRegistDataErrorMessage(processData);
        }

        return processData;
    }

    
    public ProcessData checkAuthDelete(ProcessData processData) {
        processData.setProcessName("delete");
        processData.setRquireAuth(true);
        processData.setUserAuthParam("auth_list");//TODO何を定義すればよいか?
        processData.setMethod("checkDataDelete");
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
            // 品質DB登録実績データが取得出来ていない場合エラー
            if (fxhdd03RevInfo == null || fxhdd03RevInfo.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
                return processData;
            }

            // revisionが更新されていた場合エラー
            if (!processData.getInitRev().equals(StringUtil.nullToBlank(getMapData(fxhdd03RevInfo, "rev")))) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000025"))));
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
            
            // 印刷SPSｸﾞﾗﾋﾞｱ_削除処理
            deleteSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面削除処理
            deleteSubSrSpsprintGra(queryRunnerQcdb, conQcdb, rev, kojyo, lotNo8, edaban);

            // 印刷SPSｸﾞﾗﾋﾞｱ_仮登録登録処理
            int newDeleteflag = getNewDeleteflag(queryRunnerQcdb, kojyo, lotNo8, edaban);
            insertTmpSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime, processData.getItemList());
            
            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録登録処理
            insertTmpSubSrSpsprintGra(queryRunnerQcdb, conQcdb, newRev, newDeleteflag, kojyo, lotNo8, edaban, systemTime);
            
            // 規格情報でエラーが発生している場合、エラー内容を更新
            KikakuError kikakuError = (KikakuError) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_KIKAKU_ERROR);
            if (!kikakuError.getKikakuchiInputErrorInfoList().isEmpty()) {
                ValidateUtil.fxhdd04Insert(queryRunnerDoc, tantoshaCd, newRev, lotNo, formId, formTitle, jissekiNo, "0", kikakuError.getKikakuchiInputErrorInfoList());
            }

            DbUtils.commitAndCloseQuietly(conDoc);
            DbUtils.commitAndCloseQuietly(conQcdb);

            // 後続処理メソッド設定
            processData.setMethod("");

            return processData;
        } catch (SQLException e) {
            ErrUtil.outputErrorLog("SQLException発生", e, LOGGER);

            try {
                DbUtils.rollback(conDoc);
                DbUtils.rollback(conQcdb);
            } catch (SQLException ex) {
                ErrUtil.outputErrorLog("SQLException発生", ex, LOGGER);
            }
            DbUtils.closeQuietly(conDoc);
            DbUtils.closeQuietly(conQcdb);

            processData = createRegistDataErrorMessage(processData);
        }

        return processData;
    }
    
    private ProcessData ReloadInputData(QueryRunner queryRunnerQcdb, ProcessData processData, String rev, String jotaiFlg, 
            String kojyo, String lotNo8, String edaban) throws SQLException{
        // 印刷SPSｸﾞﾗﾋﾞｱデータ取得
        List<SrSpsprintGra> srSpsprintGraDataList = getSrSpsprintGraData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);

        // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ取得
        List<SubSrSpsprintGra> subSrSpsprintGraDataList = getSubSrSpsprintGraData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpsprintGraDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC001(subSrSpsprintGraDataList.get(0), kojyo, lotNo8, edaban);

        // PTN距離X入力画面データ設定
        setInputItemDataSubFormC002(subSrSpsprintGraDataList.get(0));

        // PTN距離Y入力画面データ設定
        setInputItemDataSubFormC003(subSrSpsprintGraDataList.get(0));
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
                        GXHDO101B001Const.BTN_DOWN_EDABAN_COPY,
                        GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_SAKUJO,
                        GXHDO101B001Const.BTN_DOWN_SHUSEI,
                        GXHDO101B001Const.BTN_DOWN_START_DATETIME,
                        GXHDO101B001Const.BTN_DOWN_END_DATETIME,
                        GXHDO101B001Const.BTN_UP_EDABAN_COPY,
                        GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_SAKUJO,
                        GXHDO101B001Const.BTN_UP_SHUSEI,
                        GXHDO101B001Const.BTN_UP_START_DATETIME,
                        GXHDO101B001Const.BTN_UP_END_DATETIME
                ));
                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B001Const.BTN_DOWN_KARI_TOUROKU,
                        GXHDO101B001Const.BTN_DOWN_TOUROKU,
                        GXHDO101B001Const.BTN_UP_KARI_TOUROKU,
                        GXHDO101B001Const.BTN_UP_TOUROKU));

                break;
            default:
                activeIdList.addAll(Arrays.asList(
                        GXHDO101B001Const.BTN_DOWN_KARI_TOUROKU,
                        GXHDO101B001Const.BTN_DOWN_EDABAN_COPY,
                        GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B001Const.BTN_DOWN_TOUROKU,
                        GXHDO101B001Const.BTN_DOWN_START_DATETIME,
                        GXHDO101B001Const.BTN_DOWN_END_DATETIME,
                        GXHDO101B001Const.BTN_UP_KARI_TOUROKU,
                        GXHDO101B001Const.BTN_UP_EDABAN_COPY,
                        GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN,
                        GXHDO101B001Const.BTN_UP_TOUROKU,
                        GXHDO101B001Const.BTN_UP_START_DATETIME,
                        GXHDO101B001Const.BTN_UP_END_DATETIME
                ));

                inactiveIdList.addAll(Arrays.asList(
                        GXHDO101B001Const.BTN_DOWN_SAKUJO,
                        GXHDO101B001Const.BTN_DOWN_SHUSEI,
                        GXHDO101B001Const.BTN_UP_SAKUJO,
                        GXHDO101B001Const.BTN_UP_SHUSEI));

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
            case GXHDO101B001Const.BTN_UP_MAKUATSU_SUBGAMEN:
            case GXHDO101B001Const.BTN_DOWN_MAKUATSU_SUBGAMEN:
                method = "openMakuatsu";
                break;
            // PTN距離X
            case GXHDO101B001Const.BTN_UP_PTN_KYORI_X_SUBGAMEN:
            case GXHDO101B001Const.BTN_DOWN_PTN_KYORI_X_SUBGAMEN:
                method = "openPtnKyoriX";
                break;
            // PTN距離Y
            case GXHDO101B001Const.BTN_UP_PTN_KYORI_Y_SUBGAMEN:
            case GXHDO101B001Const.BTN_DOWN_PTN_KYORI_Y_SUBGAMEN:
                method = "openPtnKyoriY";
                break;
            // 仮登録
            case GXHDO101B001Const.BTN_UP_KARI_TOUROKU:
            case GXHDO101B001Const.BTN_DOWN_KARI_TOUROKU:
                method = "checkDataTempResist";
                break;
            // 登録
            case GXHDO101B001Const.BTN_UP_TOUROKU:
            case GXHDO101B001Const.BTN_DOWN_TOUROKU:
                method = "checkDataResist";
                break;
            // 枝番コピー
            case GXHDO101B001Const.BTN_UP_EDABAN_COPY:
            case GXHDO101B001Const.BTN_DOWN_EDABAN_COPY:
                method = "confEdabanCopy";
                break;
            // 修正
            case GXHDO101B001Const.BTN_UP_SHUSEI:
            case GXHDO101B001Const.BTN_DOWN_SHUSEI:
                method = "checkAuthCorrect";
                break;
            // 削除
            case GXHDO101B001Const.BTN_UP_SAKUJO:
            case GXHDO101B001Const.BTN_DOWN_SAKUJO:
                method = "checkAuthDelete";
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
            // TODO 処理中断エラー時はどうするか
            // エラー発生時は処理を中断
            errorMessageList.clear();
            errorMessageList.add("致命的ｴﾗｰ※ID未設定");
            processData.setFatalError(true);
            processData.setInitMessageList(errorMessageList);
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
        this.setItemData(processData, GXHDO101B001Const.LOTNO, lotNo);
        // KCPNO
        this.setItemData(processData, GXHDO101B001Const.KCPNO, StringUtil.nullToBlank(getMapData(shikakariData, "kcpno")));
        // セット数
        String hasseisu = StringUtil.nullToBlank(getMapData(shikakariData, "hasseisuu"));
        String torikosuu = StringUtil.nullToBlank(getMapData(shikakariData, "torikosuu"));
        if (StringUtil.isEmpty(hasseisu) || "0".equals(hasseisu) || StringUtil.isEmpty(torikosuu) || "0".equals(torikosuu)) {
            this.setItemData(processData, GXHDO101B001Const.SET_SUU, "0");
        } else {
            BigDecimal decHasseisu = new BigDecimal(hasseisu);
            BigDecimal decTorikosuu = new BigDecimal(torikosuu);
            BigDecimal setsu = decHasseisu.divide(decTorikosuu, 0, RoundingMode.DOWN);
            this.setItemData(processData, GXHDO101B001Const.SET_SUU, setsu.toPlainString());
        }

        // 客先
        this.setItemData(processData, GXHDO101B001Const.KYAKUSAKI, StringUtil.nullToBlank(getMapData(shikakariData, "tokuisaki")));

        // ロット区分
        String lotkubuncode = StringUtil.nullToBlank(getMapData(shikakariData, "lotkubuncode")); //ﾛｯﾄ区分ｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B001Const.LOT_KUBUN, "");
        } else {
            String lotKubun = StringUtil.nullToBlank(getMapData(lotKbnMasData, "lotkubun"));
            this.setItemData(processData, GXHDO101B001Const.LOT_KUBUN, lotkubuncode + ":" + lotKubun);
        }

        // オーナー
        String ownercode = StringUtil.nullToBlank(getMapData(shikakariData, "ownercode"));// ｵｰﾅｰｺｰﾄﾞ
        if (StringUtil.isEmpty(lotkubuncode)) {
            this.setItemData(processData, GXHDO101B001Const.OWNER, "");
        } else {
            String owner = StringUtil.nullToBlank(getMapData(ownerMasData, "owner"));
            this.setItemData(processData, GXHDO101B001Const.OWNER, ownercode + ":" + owner);
        }

        // 電極テープ
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_TAPE, StringUtil.nullToBlank(sekkeiData.get("GENRYOU"))
                + "  " + StringUtil.nullToBlank(sekkeiData.get("ETAPE")));

        // 積層数
        this.setItemData(processData, GXHDO101B001Const.SEKISOU_SU, StringUtil.nullToBlank(sekkeiData.get("EATUMI"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("SOUSUU"))
                + "層  "
                + StringUtil.nullToBlank(sekkeiData.get("EMAISUU"))
                + "枚");

        // 上カバーテープ１
        this.setItemData(processData, GXHDO101B001Const.UE_COVER_TAPE1, StringUtil.nullToBlank(sekkeiData.get("TBUNRUI2"))
                + "-"
                + StringUtil.nullToBlank(sekkeiData.get("SYURUI2"))
                + "  "
                + StringUtil.nullToBlank(sekkeiData.get("ATUMI2"))
                + "μm×"
                + StringUtil.nullToBlank(sekkeiData.get("MAISUU2"))
                + "枚"
        );

        // 下カバーテープ１
        this.setItemData(processData, GXHDO101B001Const.SHITA_COVER_TAPE1, StringUtil.nullToBlank(sekkeiData.get("TBUNRUI4"))
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
        this.setItemData(processData, GXHDO101B001Const.RETSU_GYOU, lRetsu + "×" + wRetsu);

        // ピッチ
        String lSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "LSUN")); //LSUN
        String wSun = StringUtil.nullToBlank(getMapData(daPatternMasData, "WSUN")); //WSUN
        this.setItemData(processData, GXHDO101B001Const.PITCH, lSun + "×" + wSun);

        // 電極ペースト
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_PASTE, "");

        // 電極製版名
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_SEIHAN_MEI, "");

        // 電極製版仕様
        this.setItemData(processData, GXHDO101B001Const.DENKYOKU_SEIHAN_SHIYOU, "");

        // 積層スライド量
        this.setItemData(processData, GXHDO101B001Const.SEKISOU_SLIDE_RYOU, "");

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

        List<SrSpsprintGra> srSpsprintGraDataList = new ArrayList<>();
        List<SubSrSpsprintGra> subSrSpsprintGraDataList = new ArrayList<>();
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

            // 印刷SPSｸﾞﾗﾋﾞｱデータ取得
            srSpsprintGraDataList = getSrSpsprintGraData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (srSpsprintGraDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ取得
            subSrSpsprintGraDataList = getSubSrSpsprintGraData(queryRunnerQcdb, rev, jotaiFlg, kojyo, lotNo8, edaban);
            if (subSrSpsprintGraDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                continue;
            }

            // データが全て取得出来た場合、ループを抜ける。
            break;
        }

        // 制限回数内にデータが取得できなかった場合
        if (srSpsprintGraDataList.isEmpty() || subSrSpsprintGraDataList.isEmpty()) {
            return false;
        }

        processData.setInitRev(rev);
        processData.setInitJotaiFlg(jotaiFlg);

        // メイン画面データ設定
        setInputItemDataMainForm(processData, srSpsprintGraDataList.get(0));

        // 膜厚入力画面データ設定
        setInputItemDataSubFormC001(subSrSpsprintGraDataList.get(0), kojyo, lotNo8, edaban);

        // PTN距離X入力画面データ設定
        setInputItemDataSubFormC002(subSrSpsprintGraDataList.get(0));

        // PTN距離Y入力画面データ設定
        setInputItemDataSubFormC003(subSrSpsprintGraDataList.get(0));

        return true;

    }

    /**
     * メイン画面データ設定処理
     *
     * @param processData 処理制御データ
     * @param srSpsprintGraData 印刷SPSｸﾞﾗﾋﾞｱ
     */
    private void setInputItemDataMainForm(ProcessData processData, SrSpsprintGra srSpsprintGraData) {

        // ｽﾘｯﾌﾟﾛｯﾄNo
        this.setItemData(processData, GXHDO101B001Const.SLIP_LOTNO, StringUtil.nullToBlank(srSpsprintGraData.getTapelotno()));
        // ﾛｰﾙNo1
        this.setItemData(processData, GXHDO101B001Const.ROLL_NO1, StringUtil.nullToBlank(srSpsprintGraData.getTaperollno1()));
        // ﾛｰﾙNo2
        this.setItemData(processData, GXHDO101B001Const.ROLL_NO2, StringUtil.nullToBlank(srSpsprintGraData.getTaperollno2()));
        // ﾛｰﾙNo3
        this.setItemData(processData, GXHDO101B001Const.ROLL_NO3, StringUtil.nullToBlank(srSpsprintGraData.getTaperollno3()));
        // 原料記号
        this.setItemData(processData, GXHDO101B001Const.GENRYO_KIGOU, StringUtil.nullToBlank(srSpsprintGraData.getGenryoKigou()));
        // ﾍﾟｰｽﾄﾛｯﾄNo1
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO1, StringUtil.nullToBlank(srSpsprintGraData.getPastelotno()));
        // ﾍﾟｰｽﾄ粘度1
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO1, StringUtil.nullToBlank(srSpsprintGraData.getPastenendo()));
        // ﾍﾟｰｽﾄ温度1
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO1, StringUtil.nullToBlank(srSpsprintGraData.getPasteondo()));
        // ﾍﾟｰｽﾄ固形分1
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN1, StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun1()));
        // ﾍﾟｰｽﾄﾛｯﾄNo2
        this.setItemData(processData, GXHDO101B001Const.PASTE_LOT_NO2, StringUtil.nullToBlank(srSpsprintGraData.getPastelotno2()));
        // ﾍﾟｰｽﾄ粘度2
        this.setItemData(processData, GXHDO101B001Const.PASTE_NENDO2, StringUtil.nullToBlank(srSpsprintGraData.getPastenendo2()));
        // ﾍﾟｰｽﾄ温度2
        this.setItemData(processData, GXHDO101B001Const.PASTE_ONDO2, StringUtil.nullToBlank(srSpsprintGraData.getPasteondo2()));
        // ﾍﾟｰｽﾄ固形分2
        this.setItemData(processData, GXHDO101B001Const.PASTE_KOKEIBUN2, StringUtil.nullToBlank(srSpsprintGraData.getPkokeibun2()));
        // ＰＥＴフィルム種類
        this.setItemData(processData, GXHDO101B001Const.PET_FILM_SHURUI, StringUtil.nullToBlank(srSpsprintGraData.getPetfilmsyurui()));
        // 印刷号機
        this.setItemData(processData, GXHDO101B001Const.INSATSU_GOUKI, StringUtil.nullToBlank(srSpsprintGraData.getGouki()));
        // 乾燥温度表示値1
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1, StringUtil.nullToBlank(srSpsprintGraData.getKansouondo()));
        // 乾燥温度表示値2
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2, StringUtil.nullToBlank(srSpsprintGraData.getKansouondo2()));
        // 乾燥温度表示値3
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3, StringUtil.nullToBlank(srSpsprintGraData.getKansouondo3()));
        // 乾燥温度表示値4
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4, StringUtil.nullToBlank(srSpsprintGraData.getKansouondo4()));
        // 乾燥温度表示値5
        this.setItemData(processData, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI5, StringUtil.nullToBlank(srSpsprintGraData.getKansouondo5()));
        // 搬送速度
        this.setItemData(processData, GXHDO101B001Const.HANSOU_SOKUDO, StringUtil.nullToBlank(srSpsprintGraData.getHansouspeed()));
        // 開始テンション計
        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_KEI, StringUtil.nullToBlank(srSpsprintGraData.getTensionSSum()));
        // 開始テンション前
        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_MAE, StringUtil.nullToBlank(srSpsprintGraData.getTensionStemae()));
        // 開始テンション奥
        this.setItemData(processData, GXHDO101B001Const.KAISHI_TENSION_OKU, StringUtil.nullToBlank(srSpsprintGraData.getTensionSoku()));
        // 終了テンション計
        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_KEI, StringUtil.nullToBlank(srSpsprintGraData.getTensionESum()));
        // 終了テンション前
        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_MAE, StringUtil.nullToBlank(srSpsprintGraData.getTensionEtemae()));
        // 終了テンション奥
        this.setItemData(processData, GXHDO101B001Const.SHURYOU_TENSION_OKU, StringUtil.nullToBlank(srSpsprintGraData.getTensionEoku()));
        // 圧胴圧力
        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_ATSURYOKU, StringUtil.nullToBlank(srSpsprintGraData.getAtuDoATu()));
        // ブレード圧力
        this.setItemData(processData, GXHDO101B001Const.BLADE_ATSURYOKU, StringUtil.nullToBlank(srSpsprintGraData.getBladeATu()));
        // 製版名 / 版胴名
        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI, StringUtil.nullToBlank(srSpsprintGraData.getSeihanmei()));
        // 製版No / 版胴No
        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO, StringUtil.nullToBlank(srSpsprintGraData.getSeihanno()));
        // 製版使用枚数/版胴使用枚数
        this.setItemData(processData, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU, StringUtil.nullToBlank(srSpsprintGraData.getSeihanmaisuu()));
        // ｽｷｰｼﾞNo/圧胴No
        this.setItemData(processData, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO, StringUtil.nullToBlank(srSpsprintGraData.getAtsudono()));
        // 圧胴使用枚数
        this.setItemData(processData, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU, StringUtil.nullToBlank(srSpsprintGraData.getAtsudomaisuu()));
        // ブレードNo.
        this.setItemData(processData, GXHDO101B001Const.BLADE_NO, StringUtil.nullToBlank(srSpsprintGraData.getBladeno()));
        // ブレード外観
        switch (StringUtil.nullToBlank(srSpsprintGraData.getBladegaikan())) {
            case "0":
                this.setItemData(processData, GXHDO101B001Const.BLADE_GAIKAN, "NG");
                break;
            case "1":
                this.setItemData(processData, GXHDO101B001Const.BLADE_GAIKAN, "OK");
                break;
            default:
                // TODO
                this.setItemData(processData, GXHDO101B001Const.BLADE_GAIKAN, "");
                break;
        }

        // 印刷開始日
        this.setItemData(processData, GXHDO101B001Const.INSATSU_KAISHI_DAY, DateUtil.formattedTimestamp(srSpsprintGraData.getStartdatetime(), "yyMMdd"));
        // 印刷開始時間
        this.setItemData(processData, GXHDO101B001Const.INSATSU_KAISHI_TIME, DateUtil.formattedTimestamp(srSpsprintGraData.getStartdatetime(), "HHss"));
        // 印刷ｽﾀｰﾄ膜厚AVE
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE, StringUtil.nullToBlank(srSpsprintGraData.getMakuatuaveStart()));
        // 印刷ｽﾀｰﾄ膜厚MAX
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX, StringUtil.nullToBlank(srSpsprintGraData.getMakuatumaxStart()));
        // 印刷ｽﾀｰﾄ膜厚MIN
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN, StringUtil.nullToBlank(srSpsprintGraData.getMakuatuminStart()));
        // 印刷ｽﾀｰﾄ膜厚CV
        this.setItemData(processData, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV, StringUtil.nullToBlank(srSpsprintGraData.getMakuatucvStart()));
        // PTN間距離印刷ｽﾀｰﾄ X Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_START_X_MIN, StringUtil.nullToBlank(srSpsprintGraData.getStartPtnDistX()));
        // PTN間距離印刷ｽﾀｰﾄ Y Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN, StringUtil.nullToBlank(srSpsprintGraData.getStartPtnDistY()));
        //印刷ズレ①刷り始め
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE1_START, StringUtil.nullToBlank(srSpsprintGraData.getPrintzure1SurihajimeStart()));
        //印刷ズレ②中央　開始
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE2_START, StringUtil.nullToBlank(srSpsprintGraData.getPrintzure2CenterStart()));
        //印刷ズレ③刷り終わり　
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE3_START, StringUtil.nullToBlank(srSpsprintGraData.getPrintzure3SuriowariStart()));
        //A/Bズレ平均スタート
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE3_START, StringUtil.nullToBlank(srSpsprintGraData.getAbzureHeikinStart()));
        
        // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(srSpsprintGraData.getNijimikasureStart())) {
            case "0":
                this.setItemData(processData, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, "NG");
                break;
            case "1":
                this.setItemData(processData, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, "OK");
                break;
            default:
                this.setItemData(processData, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK, "");
                break;
        }
        // 印刷スタート時担当者
        this.setItemData(processData, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA, StringUtil.nullToBlank(srSpsprintGraData.getTantousya()));
        // 印刷終了日
        this.setItemData(processData, GXHDO101B001Const.INSATSU_SHUURYOU_DAY, DateUtil.formattedTimestamp(srSpsprintGraData.getEnddatetime(), "yyMMdd"));
        // 印刷終了時刻
        this.setItemData(processData, GXHDO101B001Const.INSATSU_SHUURYOU_TIME, DateUtil.formattedTimestamp(srSpsprintGraData.getEnddatetime(), "HHss"));
        // 印刷ｴﾝﾄﾞ膜厚AVE
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE, StringUtil.nullToBlank(srSpsprintGraData.getMakuatuaveEnd()));
        // 印刷ｴﾝﾄﾞ膜厚MAX
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX, StringUtil.nullToBlank(srSpsprintGraData.getMakuatumaxEnd()));
        // 印刷ｴﾝﾄﾞ膜厚MIN
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN, StringUtil.nullToBlank(srSpsprintGraData.getMakuatuminEnd()));
        // 印刷ｴﾝﾄﾞ膜厚CV
        this.setItemData(processData, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV, StringUtil.nullToBlank(srSpsprintGraData.getMakuatucvEnd()));
        // PTN間距離印刷ｴﾝﾄﾞ X Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_END_X_MIN, StringUtil.nullToBlank(srSpsprintGraData.getEndPtnDistX()));
        // PTN間距離印刷ｴﾝﾄﾞ Y Min
        this.setItemData(processData, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN, StringUtil.nullToBlank(srSpsprintGraData.getEndPtnDistY()));
        //印刷ズレ①刷り始め　終了							
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE1_START, StringUtil.nullToBlank(srSpsprintGraData.getPrintzure1SurihajimeEnd()));
        //印刷ズレ②中央　終了							
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE2_START, StringUtil.nullToBlank(srSpsprintGraData.getPrintzure2CenterEnd()));
        //印刷ズレ③刷り終わり　終了							
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE3_START, StringUtil.nullToBlank(srSpsprintGraData.getPrintzure3SuriowariEnd()));
        //A/Bズレ平均終了							
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ZURE3_START, StringUtil.nullToBlank(srSpsprintGraData.getAbzureHeikinEnd()));
        // 終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(srSpsprintGraData.getNijimikasureEnd())) {
            case "0":
                this.setItemData(processData, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "NG");
                break;
            case "1":
                this.setItemData(processData, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "OK");
                break;
            default:
                this.setItemData(processData, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK, "");
                break;
        }

        // 印刷エンド時担当者
        this.setItemData(processData, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA, StringUtil.nullToBlank(srSpsprintGraData.getTantoEnd()));
        // 印刷枚数
        this.setItemData(processData, GXHDO101B001Const.INSATSU_MAISUU, StringUtil.nullToBlank(srSpsprintGraData.getPrintmaisuu()));
        //備考1
        this.setItemData(processData, GXHDO101B001Const.BIKOU1, StringUtil.nullToBlank(srSpsprintGraData.getBikou1()));
        //備考2
        this.setItemData(processData, GXHDO101B001Const.BIKOU2, StringUtil.nullToBlank(srSpsprintGraData.getBikou2()));
        
    }

    /**
     * 膜厚入力画面データ設定処理
     *
     * @param subSrSpsprintGraData
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     */
    private void setInputItemDataSubFormC001(SubSrSpsprintGra subSrSpsprintGraData, String kojyo, String lotNo, String edaban) {
        // サブ画面の情報を取得
        GXHDO101C001 beanGXHDO101C001 = (GXHDO101C001) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C001);

        //データの設定
        String[] makuatsuStart;
        String[] makuatsuEnd;
        beanGXHDO101C001.setKojyo(kojyo); //工場ｺｰﾄﾞ
        beanGXHDO101C001.setLotno(lotNo); //ﾛｯﾄNo
        beanGXHDO101C001.setEdaban(edaban); //枝番

        if (subSrSpsprintGraData == null) {
            // 登録データが無い場合空の状態で初期値をセットする。
            makuatsuStart = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚スタート1～9
            makuatsuEnd = new String[]{"", "", "", "", "", "", "", "", ""}; //膜厚エンド1～9
            beanGXHDO101C001.setGxhdO101c001Model(GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd));

        } else {
            // 登録データがあれば登録データをセットする。
            //膜厚スタート1～9
            makuatsuStart = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart5()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart6()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart7()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart8()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuStart9())};
            //膜厚エンド1～9
            makuatsuEnd = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd5()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd6()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd7()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd8()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getMakuatsuEnd9())
            };
            beanGXHDO101C001.setGxhdO101c001Model(GXHDO101C001Logic.createGXHDO101C001Model(makuatsuStart, makuatsuEnd));

        }
    }

    /**
     * PTN距離X入力画面データ設定処理
     *
     * @param subSrSpsprintGraData
     */
    private void setInputItemDataSubFormC002(SubSrSpsprintGra subSrSpsprintGraData) {

        // PTN距離Xサブ画面初期表示データ設定
        GXHDO101C002 beanGXHDO101C002 = (GXHDO101C002) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C002);
        //データの設定
        String[] startPtnDistX;
        String[] endPtnDistX;
        if (subSrSpsprintGraData == null) {
            startPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XStart
            endPtnDistX = new String[]{"", "", "", "", ""}; //PTN距離XEnd
            beanGXHDO101C002.setGxhdO101c002Model(GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, endPtnDistX));

        } else {
            //PTN距離Xスタート1～5
            startPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistX5())};
            //PTN距離Xエンド1～5
            endPtnDistX = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistX5())};
            beanGXHDO101C002.setGxhdO101c002Model(GXHDO101C002Logic.createGXHDO101C002Model(startPtnDistX, endPtnDistX));

        }

    }

    /**
     * PTN距離Y入力画面データ設定処理
     *
     * @param subSrSpsprintGraData
     */
    private void setInputItemDataSubFormC003(SubSrSpsprintGra subSrSpsprintGraData) {

        // PTN距離Yサブ画面初期表示データ設定
        GXHDO101C003 beanGXHDO101C003 = (GXHDO101C003) SubFormUtil.getSubFormBean(SubFormUtil.FORM_ID_GXHDO101C003);
        //データの設定
        String[] startPtnDistY;
        String[] endPtnDistY;
        if (subSrSpsprintGraData == null) {
            startPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YStart
            endPtnDistY = new String[]{"", "", "", "", ""}; //PTN距離YEnd
            beanGXHDO101C003.setGxhdO101c003Model(GXHDO101C003Logic.createGXHDO101C003Model(startPtnDistY, endPtnDistY));

        } else {
            //PTN距離Yスタート1～5
            startPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getStartPtnDistY5())};
            //PTN距離Yエンド1～5
            endPtnDistY = new String[]{
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY1()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY2()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY3()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY4()),
                StringUtil.nullToBlank(subSrSpsprintGraData.getEndPtnDistY5())};
            beanGXHDO101C003.setGxhdO101c003Model(GXHDO101C003Logic.createGXHDO101C003Model(startPtnDistY, endPtnDistY));

        }

    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱの入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態フラグ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo.
     * @param edaban 枝番
     * @return 印刷SPSｸﾞﾗﾋﾞｱ登録データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintGra> getSrSpsprintGraData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {

        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSrSpsprintGra(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSrSpsprintGra(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        }
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面の入力項目の登録データ(仮登録時は仮登録データ)を取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param rev revision
     * @param jotaiFlg 状態ﾌﾗｸﾞ
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintGra> getSubSrSpsprintGraData(QueryRunner queryRunnerQcdb, String rev, String jotaiFlg, String kojyo, String lotNo, String edaban) throws SQLException {
        if (JOTAI_FLG_TOROKUZUMI.equals(jotaiFlg)) {
            return loadSubSrSpsprintGra(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
        } else {
            return loadTmpSubSrSpsprintGra(queryRunnerQcdb, kojyo, lotNo, edaban, rev);
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
        String sql = "SELECT HINMEI,SEKKEINO,PROCESS,SETSUU,TOKUISAKI,KUBUN1,OWNER,"
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
                put("HINMEI", "KCPNO");
                put("SETSUU", "セット数");
                put("TOKUISAKI", "客先");
                put("GENRYOU", "電極テープ");
                put("ETAPE", "電極テープ");
                put("EATUMI", "積層数");
                put("SOUSUU", "積層数");
                put("EMAISUU", "積層数");
                put("KUBUN1", "ロット区分");
                put("OWNER", "オーナー");
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
        String sql = "SELECT kcpno, oyalotedaban, hasseisuu, torikosuu, lotkubuncode, ownercode, tokuisaki"
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
     * @param isLock true:ﾛｯｸする、false:ﾛｯｸしない
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
     * (6)[印刷SPSｸﾞﾗﾋﾞｱ]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintGra> loadSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {

        // 設計データの取得
        String sql = "SELECT kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,"
                + "taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,"
                + "pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,"
                + "seihanmei,seihanno,seihanmaisuu,bladeno,bladegaikan,BladeATu,"
                + "atsudono,atsudomaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,"
                + "kansouondo3,kansouondo4,kansouondo5,hansouspeed,"
                + "startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,"
                + "TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,"
                + "end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,"
                + "TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,"
                + "abzure_heikin_start,printzure1_surihajime_end,"
                + "printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryo_kigou,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,'0' AS deleteflag "
                + "FROM sr_spsprint_gra "
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
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("pastelotno", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo1
        mapping.put("pastenendo", "pastenendo"); //ﾍﾟｰｽﾄ粘度1
        mapping.put("pasteondo", "pasteondo"); //ﾍﾟｰｽﾄ温度1
        mapping.put("pkokeibun1", "pkokeibun1"); //ﾍﾟｰｽﾄ固形分1
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pkokeibun2", "pkokeibun2"); //ﾍﾟｰｽﾄ固形分2
        mapping.put("seihanmei", "seihanmei"); //版胴名
        mapping.put("seihanno", "seihanno"); //版胴No
        mapping.put("seihanmaisuu", "seihanmaisuu"); //版胴使用枚数
        mapping.put("bladeno", "bladeno"); //ﾌﾞﾚｰﾄﾞNo.
        mapping.put("bladegaikan", "bladegaikan"); //ﾌﾞﾚｰﾄﾞ外観
        mapping.put("BladeATu", "bladeATu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("atsudono", "atsudono"); //圧胴No
        mapping.put("atsudomaisuu", "atsudomaisuu"); //圧胴使用枚数
        mapping.put("AtuDoATu", "atuDoATu"); //圧胴圧力
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("hansouspeed", "hansouspeed"); //搬送速度
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("tantousya", "tantousya"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("makuatucv_start", "makuatucvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("TensionS_sum", "tensionSSum"); //開始ﾃﾝｼｮﾝ計
        mapping.put("TensionStemae", "tensionStemae"); //ﾃﾝｼｮﾝ開始手前
        mapping.put("TensionSoku", "tensionSoku"); //ﾃﾝｼｮﾝ開始奥
        mapping.put("enddatetime", "enddatetime"); //ﾌﾟﾘﾝﾄ終了日時
        mapping.put("tanto_end", "tantoEnd"); //終了時担当者ｺｰﾄﾞ
        mapping.put("printmaisuu", "printmaisuu"); //印刷枚数
        mapping.put("makuatuave_end", "makuatuaveEnd"); //終了時膜厚AVE
        mapping.put("makuatumax_end", "makuatumaxEnd"); //終了時膜厚MAX
        mapping.put("makuatumin_end", "makuatuminEnd"); //終了時膜厚MIN
        mapping.put("makuatucv_end", "makuatucvEnd"); //印刷ｴﾝﾄﾞ膜厚CV
        mapping.put("nijimikasure_end", "nijimikasureEnd"); //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("end_ptn_dist_x", "endPtnDistX"); //終了時PTN間距離X
        mapping.put("end_ptn_dist_y", "endPtnDistY"); //終了時PTN間距離Y
        mapping.put("TensionE_sum", "tensionESum"); //終了ﾃﾝｼｮﾝ計
        mapping.put("TensionEtemae", "tensionEtemae"); //ﾃﾝｼｮﾝ終了手前
        mapping.put("TensionEoku", "tensionEoku"); //ﾃﾝｼｮﾝ終了奥
        mapping.put("printzure1_surihajime_start", "printzure1SurihajimeStart"); //印刷ズレ①刷り始め開始
        mapping.put("printzure2_center_start", "printzure2CenterStart"); //印刷ズレ②中央開始
        mapping.put("printzure3_suriowari_start", "printzure3SuriowariStart"); //印刷ズレ③刷り終わり開始
        mapping.put("abzure_heikin_start", "abzureHeikinStart"); //ABズレ平均スタート
        mapping.put("printzure1_surihajime_end", "printzure1SurihajimeEnd"); //印刷ズレ①刷り始め終了
        mapping.put("printzure2_center_end", "printzure2CenterEnd"); //印刷ズレ②中央終了
        mapping.put("printzure3_suriowari_end", "printzure3SuriowariEnd"); //印刷ズレ③刷り終わり終了
        mapping.put("abzure_heikin_end", "abzureHeikinEnd"); //ABズレ平均終了
        mapping.put("genryo_kigou", "genryoKigou"); //原料記号
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprintGra>> beanHandler = new BeanListHandler<>(SrSpsprintGra.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (7)[印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintGra> loadSubSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {

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
                + "FROM sub_sr_spsprint_gra "
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
        ResultSetHandler<List<SubSrSpsprintGra>> beanHandler = new BeanListHandler<>(SubSrSpsprintGra.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (8)[印刷SPSｸﾞﾗﾋﾞｱ_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SrSpsprintGra> loadTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {
        String sql = "SELECT kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,"
                + "taperollno2,taperollno3,pastelotno,pastenendo,pasteondo,"
                + "pkokeibun1,pastelotno2,pastenendo2,pasteondo2,pkokeibun2,"
                + "seihanmei,seihanno,seihanmaisuu,bladeno,bladegaikan,BladeATu,"
                + "atsudono,atsudomaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,"
                + "kansouondo3,kansouondo4,kansouondo5,hansouspeed,"
                + "startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,"
                + "start_ptn_dist_x,start_ptn_dist_y,TensionS_sum,TensionStemae,"
                + "TensionSoku,enddatetime,tanto_end,printmaisuu,makuatuave_end,"
                + "makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,"
                + "end_ptn_dist_x,end_ptn_dist_y,TensionE_sum,TensionEtemae,"
                + "TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,"
                + "abzure_heikin_start,printzure1_surihajime_end,"
                + "printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryo_kigou,bikou1,bikou2,"
                + "torokunichiji,kosinnichiji,revision,deleteflag "
                + "FROM tmp_sr_spsprint_gra "
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
        mapping.put("tapelotno", "tapelotno"); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        mapping.put("petfilmsyurui", "petfilmsyurui"); //PETﾌｨﾙﾑ種類
        mapping.put("taperollno1", "taperollno1"); //ﾃｰﾌﾟﾛｰﾙNo1
        mapping.put("taperollno2", "taperollno2"); //ﾃｰﾌﾟﾛｰﾙNo2
        mapping.put("taperollno3", "taperollno3"); //ﾃｰﾌﾟﾛｰﾙNo3
        mapping.put("pastelotno", "pastelotno"); //ﾍﾟｰｽﾄﾛｯﾄNo1
        mapping.put("pastenendo", "pastenendo"); //ﾍﾟｰｽﾄ粘度1
        mapping.put("pasteondo", "pasteondo"); //ﾍﾟｰｽﾄ温度1
        mapping.put("pkokeibun1", "pkokeibun1"); //ﾍﾟｰｽﾄ固形分1
        mapping.put("pastelotno2", "pastelotno2"); //ﾍﾟｰｽﾄﾛｯﾄNo2
        mapping.put("pastenendo2", "pastenendo2"); //ﾍﾟｰｽﾄ粘度2
        mapping.put("pasteondo2", "pasteondo2"); //ﾍﾟｰｽﾄ温度2
        mapping.put("pkokeibun2", "pkokeibun2"); //ﾍﾟｰｽﾄ固形分2
        mapping.put("seihanmei", "seihanmei"); //版胴名
        mapping.put("seihanno", "seihanno"); //版胴No
        mapping.put("seihanmaisuu", "seihanmaisuu"); //版胴使用枚数
        mapping.put("bladeno", "bladeno"); //ﾌﾞﾚｰﾄﾞNo.
        mapping.put("bladegaikan", "bladegaikan"); //ﾌﾞﾚｰﾄﾞ外観
        mapping.put("BladeATu", "bladeATu"); //ﾌﾞﾚｰﾄﾞ圧力
        mapping.put("atsudono", "atsudono"); //圧胴No
        mapping.put("atsudomaisuu", "atsudomaisuu"); //圧胴使用枚数
        mapping.put("AtuDoATu", "atuDoATu"); //圧胴圧力
        mapping.put("gouki", "gouki"); //号機ｺｰﾄﾞ
        mapping.put("kansouondo", "kansouondo"); //乾燥温度
        mapping.put("kansouondo2", "kansouondo2"); //乾燥温度2
        mapping.put("kansouondo3", "kansouondo3"); //乾燥温度3
        mapping.put("kansouondo4", "kansouondo4"); //乾燥温度4
        mapping.put("kansouondo5", "kansouondo5"); //乾燥温度5
        mapping.put("hansouspeed", "hansouspeed"); //搬送速度
        mapping.put("startdatetime", "startdatetime"); //ﾌﾟﾘﾝﾄ開始日時
        mapping.put("tantousya", "tantousya"); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        mapping.put("makuatuave_start", "makuatuaveStart"); //ｽﾀｰﾄ時膜厚AVE
        mapping.put("makuatumax_start", "makuatumaxStart"); //ｽﾀｰﾄ時膜厚MAX
        mapping.put("makuatumin_start", "makuatuminStart"); //ｽﾀｰﾄ時膜厚MIN
        mapping.put("makuatucv_start", "makuatucvStart"); //印刷ｽﾀｰﾄ膜厚CV
        mapping.put("nijimikasure_start", "nijimikasureStart"); //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("start_ptn_dist_x", "startPtnDistX"); //ｽﾀｰﾄ時PTN間距離X
        mapping.put("start_ptn_dist_y", "startPtnDistY"); //ｽﾀｰﾄ時PTN間距離Y
        mapping.put("TensionS_sum", "tensionSSum"); //開始ﾃﾝｼｮﾝ計
        mapping.put("TensionStemae", "tensionStemae"); //ﾃﾝｼｮﾝ開始手前
        mapping.put("TensionSoku", "tensionSoku"); //ﾃﾝｼｮﾝ開始奥
        mapping.put("enddatetime", "enddatetime"); //ﾌﾟﾘﾝﾄ終了日時
        mapping.put("tanto_end", "tantoEnd"); //終了時担当者ｺｰﾄﾞ
        mapping.put("printmaisuu", "printmaisuu"); //印刷枚数
        mapping.put("makuatuave_end", "makuatuaveEnd"); //終了時膜厚AVE
        mapping.put("makuatumax_end", "makuatumaxEnd"); //終了時膜厚MAX
        mapping.put("makuatumin_end", "makuatuminEnd"); //終了時膜厚MIN
        mapping.put("makuatucv_end", "makuatucvEnd"); //印刷ｴﾝﾄﾞ膜厚CV
        mapping.put("nijimikasure_end", "nijimikasureEnd"); //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        mapping.put("end_ptn_dist_x", "endPtnDistX"); //終了時PTN間距離X
        mapping.put("end_ptn_dist_y", "endPtnDistY"); //終了時PTN間距離Y
        mapping.put("TensionE_sum", "tensionESum"); //終了ﾃﾝｼｮﾝ計
        mapping.put("TensionEtemae", "tensionEtemae"); //ﾃﾝｼｮﾝ終了手前
        mapping.put("TensionEoku", "tensionEoku"); //ﾃﾝｼｮﾝ終了奥
        mapping.put("printzure1_surihajime_start", "printzure1SurihajimeStart"); //印刷ズレ①刷り始め開始
        mapping.put("printzure2_center_start", "printzure2CenterStart"); //印刷ズレ②中央開始
        mapping.put("printzure3_suriowari_start", "printzure3SuriowariStart"); //印刷ズレ③刷り終わり開始
        mapping.put("abzure_heikin_start", "abzureHeikinStart"); //ABズレ平均スタート
        mapping.put("printzure1_surihajime_end", "printzure1SurihajimeEnd"); //印刷ズレ①刷り始め終了
        mapping.put("printzure2_center_end", "printzure2CenterEnd"); //印刷ズレ②中央終了
        mapping.put("printzure3_suriowari_end", "printzure3SuriowariEnd"); //印刷ズレ③刷り終わり終了
        mapping.put("abzure_heikin_end", "abzureHeikinEnd"); //ABズレ平均終了
        mapping.put("genryo_kigou", "genryoKigou"); //原料記号
        mapping.put("bikou1", "bikou1"); //備考1
        mapping.put("bikou2", "bikou2"); //備考2
        mapping.put("torokunichiji", "torokunichiji"); //登録日時
        mapping.put("kosinnichiji", "kosinnichiji"); //更新日時
        mapping.put("revision", "revision"); //revision
        mapping.put("deleteflag", "deleteflag"); //削除ﾌﾗｸﾞ

        BeanProcessor beanProcessor = new BeanProcessor(mapping);
        RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
        ResultSetHandler<List<SrSpsprintGra>> beanHandler = new BeanListHandler<>(SrSpsprintGra.class, rowProcessor);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerQcdb.query(sql, beanHandler, params.toArray());
    }

    /**
     * (9)[印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面_仮登録]から、ﾃﾞｰﾀを取得
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ(検索キー)
     * @param lotNo ﾛｯﾄNo(検索キー)
     * @param edaban 枝番(検索キー)
     * @param rev revision(検索キー)
     * @return 取得データ
     * @throws SQLException 例外エラー
     */
    private List<SubSrSpsprintGra> loadTmpSubSrSpsprintGra(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban, String rev) throws SQLException {

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
                + "FROM tmp_sub_sr_spsprint_gra "
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
        ResultSetHandler<List<SubSrSpsprintGra>> beanHandler = new BeanListHandler<>(SubSrSpsprintGra.class, rowProcessor);

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

            // 印刷SPSｸﾞﾗﾋﾞｱデータ取得
            List<SrSpsprintGra> srSpsprintGraDataList = getSrSpsprintGraData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (srSpsprintGraDataList.isEmpty()) {
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面データ取得
            List<SubSrSpsprintGra> subSrSpsprintGraDataList = getSubSrSpsprintGraData(queryRunnerQcdb, "", jotaiFlg, kojyo, lotNo8, oyalotEdaban);
            if (subSrSpsprintGraDataList.isEmpty()) {
                //該当データが取得できなかった場合は処理を繰り返す。
                processData.setErrorMessageInfoList(Arrays.asList(new ErrorMessageInfo(MessageUtil.getMessage("XHD-000030"))));
                return processData;
            }

            // メイン画面データ設定
            setInputItemDataMainForm(processData, srSpsprintGraDataList.get(0));

            // 膜厚入力画面データ設定 ※工場ｺｰﾄﾞ、ﾛｯﾄNo、枝番は親ではなく自身の値を渡す。
            setInputItemDataSubFormC001(subSrSpsprintGraDataList.get(0), kojyo, lotNo8, edaban);

            // PTN距離X入力画面データ設定
            setInputItemDataSubFormC002(subSrSpsprintGraDataList.get(0));

            // PTN距離Y入力画面データ設定
            setInputItemDataSubFormC003(subSrSpsprintGraDataList.get(0));

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
     * (3)、(6)、(7)実績情報取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param lotNo ロットNo(検索キー)
     * @param inCondition IN句の条件
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadJissekiData(QueryRunner queryRunnerDoc, String lotNo, String inCondition) throws SQLException {
        String lotNo1 = lotNo.substring(0, 3);
        String lotNo2 = lotNo.substring(3, 11);
        String lotNo3 = lotNo.substring(11, 14);

        // 実績データの取得
        String sql = "SELECT tantousyacode, syorisuu, syoribi, syorijikoku, jissekino FROM jisseki "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND koteicode IN (" + inCondition + ") "
                + "ORDER BY jissekino DESC ";

        List<Object> params = new ArrayList<>();
        params.add(lotNo1);
        params.add(lotNo2);
        params.add(lotNo3);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
    }

    /**
     * (4)、(8)生産情報取得
     *
     * @param queryRunnerDoc QueryRunnerオブジェクト
     * @param jissekiNo 実績No
     * @return 取得データ
     * @throws SQLException
     */
    private Map loadSeisanData(QueryRunner queryRunnerDoc, String jissekiNo) throws SQLException {
        // 生産データの取得
        String sql = "SELECT goukicode FROM seisan WHERE jissekino = ? ";

        List<Object> params = new ArrayList<>();
        params.add(Integer.valueOf(jissekiNo));

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        return queryRunnerDoc.query(sql, new MapHandler(), params.toArray());
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

    private String getMethodFromProcess(String processName) {
        switch (processName) {
            case "tempResist":
                return "doTempResist";
            default:
                break;
        }
        return "";
    }

//    private Object getSrSpsprintItemData(SrSpsprint srSpsPritData, String itemId) {
//        switch (itemId) {
//            case GXHDO101B001Const.KCPNO: // KCPNO
//                return srSpsPritData.getKcpno();
//            case GXHDO101B001Const.LOTNO: // ロットＮｏ．
//                return srSpsPritData.getLotno();
//            case GXHDO101B001Const.SET_SUU: // セット数
//                return null;//TODO
//            case GXHDO101B001Const.KYAKUSAKI: // 客先
//                return null;//TODO
//            case GXHDO101B001Const.LOT_KUBUN: // ロット区分
//                return null;//TODO
//            case GXHDO101B001Const.OWNER: // オーナー
//                return null;//TODO
//            case GXHDO101B001Const.DENKYOKU_TAPE: // 電極テープ
//                return null;//TODO
//            case GXHDO101B001Const.SEKISOU_SU: // 積層数
//                return null;//TODO
//            case GXHDO101B001Const.SLIP_LOTNO: // ｽﾘｯﾌﾟﾛｯﾄNo
//                return srSpsPritData.getTapelotno();//TODO
//            case GXHDO101B001Const.ROLL_NO1: // ﾛｰﾙNo1
//                return srSpsPritData.getTaperollno1();//TODO
//            case GXHDO101B001Const.ROLL_NO2: // ﾛｰﾙNo2
//                return srSpsPritData.getTaperollno2();//TODO
//            case GXHDO101B001Const.ROLL_NO3: // ﾛｰﾙNo3
//                return srSpsPritData.getTaperollno3(); //TODO
//            case GXHDO101B001Const.GENRYO_KIGOU: // 原料記号
//                return srSpsPritData.getGenryoukigou();
//            case GXHDO101B001Const.UE_COVER_TAPE1: // 上カバーテープ１
//                return null;//TODO
//            case GXHDO101B001Const.SHITA_COVER_TAPE1: // 下カバーテープ１
//                return null;//TODO
////            case GXHDO101B001Const.COVER_TAPE: // カバーテープ
////                return null;//TODO
//            case GXHDO101B001Const.RETSU_GYOU: // 列×行
//                return null;//TODO
//            case GXHDO101B001Const.PITCH: // ピッチ
//                return null;//TODO
//            case GXHDO101B001Const.DENKYOKU_PASTE: // 電極ペースト
//                return null;//TODO
////            case GXHDO101B001Const.PASTE_HINMEI: // ﾍﾟｰｽﾄ品名
////                return srSpsPritData.getPastehinmei();
//            case GXHDO101B001Const.PASTE_LOT_NO1: // ﾍﾟｰｽﾄﾛｯﾄNo1
//                return srSpsPritData.getPastelotno();
//            case GXHDO101B001Const.PASTE_NENDO1: // ﾍﾟｰｽﾄ粘度1
//                return srSpsPritData.getPastenendo();
//            case GXHDO101B001Const.PASTE_ONDO1: // ﾍﾟｰｽﾄ温度1
//                return srSpsPritData.getPasteondo();
//            case GXHDO101B001Const.PASTE_KOKEIBUN1: // ﾍﾟｰｽﾄ固形分1
//                return null;//TODO
//            case GXHDO101B001Const.PASTE_LOT_NO2: // ﾍﾟｰｽﾄﾛｯﾄNo2
//                return srSpsPritData.getPastelotno2();
//            case GXHDO101B001Const.PASTE_NENDO2: // ﾍﾟｰｽﾄ粘度2
//                return srSpsPritData.getPastenendo2();
//            case GXHDO101B001Const.PASTE_ONDO2: // ﾍﾟｰｽﾄ温度2
//                return srSpsPritData.getPasteondo2();
//            case GXHDO101B001Const.PASTE_KOKEIBUN2: // ﾍﾟｰｽﾄ固形分2
//                return null;//TODO
//            case GXHDO101B001Const.DENKYOKU_SEIHAN_MEI: // 電極製版名
//                return null;//TODO
//            case GXHDO101B001Const.DENKYOKU_SEIHAN_SHIYOU: // 電極製版仕様
//                return null;//TODO
//            case GXHDO101B001Const.PET_FILM_SHURUI: // ＰＥＴフィルム種類
//                return null;//TODO
//            case GXHDO101B001Const.SEKISOU_SLIDE_RYOU: // 積層スライド量
//                return null;//TODO
//            case GXHDO101B001Const.INSATSU_GOUKI: // 印刷号機
//                return srSpsPritData.getGouki();//TODO
//            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1: // 乾燥温度表示値1
//                return srSpsPritData.getKansouondo();//TODO
//            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2: // 乾燥温度表示値2
//                return srSpsPritData.getKansouondo2();//TODO
//            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3: // 乾燥温度表示値3
//                return srSpsPritData.getKansouondo3();//TODO
//            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4: // 乾燥温度表示値4
//                return srSpsPritData.getKansouondo4();//TODO
//            case GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI5: // 乾燥温度表示値5
//                return srSpsPritData.getKansouondo5();//TODO
//            case GXHDO101B001Const.HANSOU_SOKUDO: // 搬送速度
//                return null;//TODO
//            case GXHDO101B001Const.KAISHI_TENSION_KEI: // 開始テンション計
//                return srSpsPritData.getTensionS();
//            case GXHDO101B001Const.KAISHI_TENSION_MAE: // 開始テンション前
//                return srSpsPritData.getTensionStemae();
//            case GXHDO101B001Const.KAISHI_TENSION_OKU: // 開始テンション奥
//                return srSpsPritData.getTensionSoku();
//            case GXHDO101B001Const.SHURYOU_TENSION_KEI: // 終了テンション計
//                return srSpsPritData.getTensionE();
//            case GXHDO101B001Const.SHURYOU_TENSION_MAE: // 終了テンション前
//                return srSpsPritData.getTensionEtemae();
//            case GXHDO101B001Const.SHURYOU_TENSION_OKU: // 終了テンション奥
//                return srSpsPritData.getTensionSoku();
//            case GXHDO101B001Const.ATSUDOU_ATSURYOKU: // 圧胴圧力
//                return srSpsPritData.getAtuDoATu();
//            case GXHDO101B001Const.BLADE_ATSURYOKU: // ブレード圧力
//                return srSpsPritData.getBladeATu();
//            case GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI: // 製版名 / 版胴名
//                return srSpsPritData.getSeihanmei();
//            case GXHDO101B001Const.SEIHAN_OR_HANDOU_NO: // 製版No / 版胴No
//                return srSpsPritData.getSeihanno();
//            case GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU: // 製版使用枚数/版胴使用枚数
//                return srSpsPritData.getSeihanmaisuu();
//            case GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO: // ｽｷｰｼﾞNo / 圧胴No．
//                return srSpsPritData.getSkeegeno();
////            case GXHDO101B001Const.ATSUDOU_KEI1: // 圧胴径1
////                return srSpsPritData.getAtuDoKeiLSide();//TODO
////            case GXHDO101B001Const.ATSUDOU_KEI2: // 圧胴径2
////                return srSpsPritData.getAtuDoKeiLSide();//TODO
////            case GXHDO101B001Const.ATSUDOU_KEI3: // 圧胴径3
////                return srSpsPritData.getAtuDoKeiCenter();//TODO
////            case GXHDO101B001Const.ATSUDOU_KEI4: // 圧胴径4
////                return srSpsPritData.getAtuDoKeiRSide();//TODO
////            case GXHDO101B001Const.ATSUDOU_KEI5: // 圧胴径5
////                return srSpsPritData.getAtuDoKeiREnd();//TODO
//            case GXHDO101B001Const.BLADE_NO: // ブレードNo.
//                return null;// TODO
//            case GXHDO101B001Const.BLADE_GAIKAN: // ブレード外観
//                return null;// TODO
//            case GXHDO101B001Const.INSATSU_KAISHI_DAY: // 印刷開始日
//                return DateUtil.formattedTimestamp(srSpsPritData.getStartdatetime(), "yyMMdd");
//            case GXHDO101B001Const.INSATSU_KAISHI_TIME: // 印刷開始時間
//                return DateUtil.formattedTimestamp(srSpsPritData.getStartdatetime(), "HHss");
//            case GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE: // 印刷ｽﾀｰﾄ膜厚AVE
//                return null;// TODO
//            case GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX: // 印刷ｽﾀｰﾄ膜厚MAX
//                return null;// TODO
//            case GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN: // 印刷ｽﾀｰﾄ膜厚MIN
//                return null;// TODO
//            case GXHDO101B001Const.INSATSU_START_MAKUATSU_CV: // 印刷ｽﾀｰﾄ膜厚CV
//                return null;// TODO
//            case GXHDO101B001Const.PTN_INSATSU_START_X_MIN: // PTN間距離印刷ｽﾀｰﾄ X Min
//                return srSpsPritData.getStartPtnDistX();//TODO
//            case GXHDO101B001Const.PTN_INSATSU_START_Y_MIN: // PTN間距離印刷ｽﾀｰﾄ Y Min
//                return srSpsPritData.getStartPtnDistY();//TODO
//            case GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK: // ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
//                return null;//TODO
//            case GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA: // 印刷スタート時担当者
//                return null;//TODO
//            case GXHDO101B001Const.INSATSU_SHUURYOU_DAY: // 印刷終了日
//                return DateUtil.formattedTimestamp(srSpsPritData.getEnddatetime(), "yyMMdd");
//            case GXHDO101B001Const.INSATSU_SHUURYOU_TIME: // 印刷終了時刻
//                return DateUtil.formattedTimestamp(srSpsPritData.getEnddatetime(), "HHmm");
//            case GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE: // 印刷ｴﾝﾄﾞ膜厚AVE
//                return srSpsPritData.getMakuatuaveEnd();
//            case GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX: // 印刷ｴﾝﾄﾞ膜厚MAX
//                return srSpsPritData.getMakuatumaxEnd();
//            case GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN: // 印刷ｴﾝﾄﾞ膜厚MIN
//                return srSpsPritData.getMakuatuminEnd();
//            case GXHDO101B001Const.INSATSU_END_MAKUATSU_CV: // 印刷ｴﾝﾄﾞ膜厚CV
//                return null;//TODO
//            case GXHDO101B001Const.PTN_INSATSU_END_X_MIN: // PTN間距離印刷ｴﾝﾄﾞ X Min
//                return srSpsPritData.getEndPtnDistX(); //TODO
//            case GXHDO101B001Const.PTN_INSATSU_END_Y_MIN: // PTN間距離印刷ｴﾝﾄﾞ Y Min
//                return srSpsPritData.getEndPtnDistY(); //TODO
//            case GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK: // 終了時ニジミ・カスレ確認
//                return null;//TODO
//            case GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA: // 印刷エンド時担当者
//                return srSpsPritData.getTantoEnd();// TODO
//            case GXHDO101B001Const.INSATSU_MAISUU: // 印刷枚数
//                return null; //TODO
//            default:
//                return null;
//        }
//
//    }

//    /**
//     * 同一項目の更新チェック
//     */
//    private void checkSameItemUpdate(ProcessData processData, SrSpsprint srSpsPritData) {
//        for (FXHDD01 fxhdd01 : processData.getItemList()) {
//            //入力項目かつ値が入っている場合、チェック
//            if (!isInputColumn(fxhdd01) || StringUtil.isEmpty(fxhdd01.getValue())) {
//                continue;
//            }
//
//            Object checkDbData = getSrSpsprintItemData(srSpsPritData, fxhdd01.getItemId());
//
//            //checkDbData.
//        }
//    }

    private boolean isInputColumn(FXHDD01 fxhdd01) {
        if (fxhdd01.isRenderInputDate() || fxhdd01.isRenderInputNumber() || fxhdd01.isRenderInputRadio() 
                || fxhdd01.isRenderInputSelect() || fxhdd01.isRenderInputText() || fxhdd01.isRenderInputTime()) {
            return true;
        }
        return false;
    }

    private boolean compItemValue(Object checkDbData, String itemValue) {
        try {

            // 値がNULLまたは空の場合は次の項目へ
            if (checkDbData == null || StringUtil.isEmpty(checkDbData.toString())) {
                return true;
            }

            if (checkDbData instanceof Integer) {
                return (Integer) checkDbData == Integer.parseInt(itemValue);
            } else if (checkDbData instanceof String) {
                return ((String) checkDbData).equals(itemValue);
            } else if (checkDbData instanceof BigDecimal) {
                return ((BigDecimal) checkDbData).compareTo(new BigDecimal(itemValue)) == 0;
            }

        } catch (NumberFormatException e) {
            return false;
        }
        return false;
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
                + "  AND lotno = ? AND edaban = ? ";

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
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)登録処理
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
    private void insertTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        // 焼成データの登録
        String sql = "INSERT INTO tmp_sr_spsprint_gra ("
                + "kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,seihanmei,seihanno,seihanmaisuu,bladeno,bladegaikan,BladeATu,atsudono,"
                + "atsudomaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,"
                + "TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryo_kigou,bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision,deleteflag"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSrSpsprintGra(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime, itemList);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)更新処理
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
    private void updateTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        // 焼成データの登録
        String sql = "UPDATE tmp_sr_spsprint_gra SET "
                + "tapelotno = ?,petfilmsyurui = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,"
                + "pastelotno = ?,pastenendo = ?,pasteondo = ?,pkokeibun1 = ?,pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,"
                + "pkokeibun2 = ?,seihanmei = ?,seihanno = ?,seihanmaisuu = ?,bladeno = ?,bladegaikan = ?,BladeATu = ?,atsudono = ?,"
                + "atsudomaisuu = ?,AtuDoATu = ?,gouki = ?,kansouondo = ?,kansouondo2 = ?,kansouondo3 = ?,kansouondo4 = ?,"
                + "kansouondo5 = ?,hansouspeed = ?,startdatetime = ?,tantousya = ?,makuatuave_start = ?,makuatumax_start = ?,"
                + "makuatumin_start = ?,makuatucv_start = ?,nijimikasure_start = ?,start_ptn_dist_x = ?,start_ptn_dist_y = ?,"
                + "TensionS_sum = ?,TensionStemae = ?,TensionSoku = ?,enddatetime = ?,tanto_end = ?,printmaisuu = ?,"
                + "makuatuave_end = ?,makuatumax_end = ?,makuatumin_end = ?,makuatucv_end = ?,nijimikasure_end = ?,end_ptn_dist_x = ?,"
                + "end_ptn_dist_y = ?,TensionE_sum = ?,TensionEtemae = ?,TensionEoku = ?,printzure1_surihajime_start = ?,"
                + "printzure2_center_start = ?,printzure3_suriowari_start = ?,abzure_heikin_start = ?,printzure1_surihajime_end = ?,"
                + "printzure2_center_end = ?,printzure3_suriowari_end = ?,abzure_heikin_end = ?,genryo_kigou = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ?,deleteflag = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ? ";

        //更新値設定
        List<Object> params = setUpdateParameterTmpSrSpsprintGra(false, newRev, 0, "", "", "", systemTime, itemList);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteTmpSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        // 焼成データの登録
        String sql = "DELETE FROM tmp_sr_spsprint_gra "
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_仮登録(tmp_sr_spsprint_gra)更新値ﾊﾟﾗﾒｰﾀ設定
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
    private List<Object> setUpdateParameterTmpSrSpsprintGra(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SLIP_LOTNO))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PET_FILM_SHURUI))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO1))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO2))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO3))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO1))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO1))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO1))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN1))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO2))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO2))); //ﾍﾟｰｽﾄ粘度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO2))); //ﾍﾟｰｽﾄ温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN2))); //ﾍﾟｰｽﾄ固形分2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI))); //版胴名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO))); //版胴No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU))); //版胴使用枚数 
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BLADE_NO))); //ﾌﾞﾚｰﾄﾞNo.
        //ﾌﾞﾚｰﾄﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.BLADE_GAIKAN))) {
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
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.BLADE_ATSURYOKU))); //ﾌﾞﾚｰﾄﾞ圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO))); //圧胴No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUDOU_SIYOU_MAISUU))); //圧胴使用枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUDOU_ATSURYOKU))); //圧胴圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_GOUKI))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1))); //乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI5))); //乾燥温度5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.HANSOU_SOKUDO))); //搬送速度
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_DAY),
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_TIME))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_X_MIN))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_KEI))); //開始ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_MAE))); //ﾃﾝｼｮﾝ開始手前
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_OKU))); //ﾃﾝｼｮﾝ開始奥
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_DAY),
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_TIME))); //ﾌﾟﾘﾝﾄ終了日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_MAISUU))); //印刷枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN))); //終了時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK))) {
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

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_X_MIN))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN))); //終了時PTN間距離Y
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_KEI))); //終了ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_MAE))); //ﾃﾝｼｮﾝ終了手前
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_OKU))); //ﾃﾝｼｮﾝ終了奥
        
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ZURE1_START))); //印刷ズレ①刷り始め開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ZURE2_START))); //印刷ズレ②中央開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ZURE3_START))); //印刷ズレ③刷り終わり開始
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.AB_ZURE_HEIKIN_START))); //ABズレ平均スタート
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ZURE1_START))); //印刷ズレ①刷り始め終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ZURE2_START))); //印刷ズレ②中央終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ZURE3_START))); //印刷ズレ③刷り終わり終了
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.AB_ZURE_HEIKIN_END))); //ABズレ平均終了
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.GENRYO_KIGOU))); //原料記号
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU1))); //備考1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BIKOU2))); //備考2

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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)登録処理
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
    private void insertTmpSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev, int deleteflag,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO tmp_sub_sr_spsprint_gra ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision,deleteflag"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterTmpSubSrSpsprintGra(true, newRev, deleteflag, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)更新処理
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
    private void updateTmpSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_spsprint_gra SET "
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

        List<Object> params = setUpdateParameterTmpSubSrSpsprintGra(false, newRev, 0, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteTmpSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM tmp_sub_sr_spsprint_gra "
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)更新値ﾊﾟﾗﾒｰﾀ設定
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
    private List<Object> setUpdateParameterTmpSubSrSpsprintGra(boolean isInsert, BigDecimal newRev, int deleteflag, String kojyo, String lotNo, String edaban, Timestamp systemTime) {
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
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)登録処理
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
    private void insertSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        // 焼成データの登録
        String sql = "INSERT INTO sr_spsprint_gra ("
                + "kojyo,lotno,edaban,tapelotno,petfilmsyurui,taperollno1,taperollno2,taperollno3,"
                + "pastelotno,pastenendo,pasteondo,pkokeibun1,pastelotno2,pastenendo2,pasteondo2,"
                + "pkokeibun2,seihanmei,seihanno,seihanmaisuu,bladeno,bladegaikan,BladeATu,atsudono,"
                + "atsudomaisuu,AtuDoATu,gouki,kansouondo,kansouondo2,kansouondo3,kansouondo4,"
                + "kansouondo5,hansouspeed,startdatetime,tantousya,makuatuave_start,makuatumax_start,"
                + "makuatumin_start,makuatucv_start,nijimikasure_start,start_ptn_dist_x,start_ptn_dist_y,"
                + "TensionS_sum,TensionStemae,TensionSoku,enddatetime,tanto_end,printmaisuu,"
                + "makuatuave_end,makuatumax_end,makuatumin_end,makuatucv_end,nijimikasure_end,end_ptn_dist_x,"
                + "end_ptn_dist_y,TensionE_sum,TensionEtemae,TensionEoku,printzure1_surihajime_start,"
                + "printzure2_center_start,printzure3_suriowari_start,abzure_heikin_start,"
                + "printzure1_surihajime_end,printzure2_center_end,printzure3_suriowari_end,"
                + "abzure_heikin_end,genryo_kigou,bikou1,bikou2,torokunichiji,kosinnichiji,"
                + "revision"
                + ") VALUES ("
                + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSrSpsprintGra(true, newRev, kojyo, lotNo, edaban, systemTime, itemList);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)更新処理
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
    private void updateSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) throws SQLException {
        String sql = "UPDATE sr_spsprint_gra SET "
                + "tapelotno = ?,petfilmsyurui = ?,taperollno1 = ?,taperollno2 = ?,taperollno3 = ?,"
                + "pastelotno = ?,pastenendo = ?,pasteondo = ?,pkokeibun1 = ?,pastelotno2 = ?,pastenendo2 = ?,pasteondo2 = ?,"
                + "pkokeibun2 = ?,seihanmei = ?,seihanno = ?,seihanmaisuu = ?,bladeno = ?,bladegaikan = ?,BladeATu = ?,atsudono = ?,"
                + "atsudomaisuu = ?,AtuDoATu = ?,gouki = ?,kansouondo = ?,kansouondo2 = ?,kansouondo3 = ?,kansouondo4 = ?,"
                + "kansouondo5 = ?,hansouspeed = ?,startdatetime = ?,tantousya = ?,makuatuave_start = ?,makuatumax_start = ?,"
                + "makuatumin_start = ?,makuatucv_start = ?,nijimikasure_start = ?,start_ptn_dist_x = ?,start_ptn_dist_y = ?,"
                + "TensionS_sum = ?,TensionStemae = ?,TensionSoku = ?,enddatetime = ?,tanto_end = ?,printmaisuu = ?,"
                + "makuatuave_end = ?,makuatumax_end = ?,makuatumin_end = ?,makuatucv_end = ?,nijimikasure_end = ?,end_ptn_dist_x = ?,"
                + "end_ptn_dist_y = ?,TensionE_sum = ?,TensionEtemae = ?,TensionEoku = ?,printzure1_surihajime_start = ?,"
                + "printzure2_center_start = ?,printzure3_suriowari_start = ?,abzure_heikin_start = ?,printzure1_surihajime_end = ?,"
                + "printzure2_center_end = ?,printzure3_suriowari_end = ?,abzure_heikin_end = ?,genryo_kigou = ?,bikou1 = ?,bikou2 = ?,"
                + "kosinnichiji = ?,revision = ? "
                + "WHERE kojyo = ? AND lotno = ? AND edaban = ? AND revision = ?";

        //更新値設定
        List<Object> params = setUpdateParameterSrSpsprintGra(false, newRev, "", "", "", systemTime, itemList);

        //検索条件設定
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)更新値ﾊﾟﾗﾒｰﾀ設定
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
    private List<Object> setUpdateParameterSrSpsprintGra(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, Timestamp systemTime, List<FXHDD01> itemList) {
        List<Object> params = new ArrayList<>();

        if (isInsert) {
            params.add(kojyo); //工場ｺｰﾄﾞ
            params.add(lotNo); //ﾛｯﾄNo
            params.add(edaban); //枝番
        }
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SLIP_LOTNO))); //ﾃｰﾌﾟｽﾘｯﾌﾟﾛｯﾄNo
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PET_FILM_SHURUI))); //PETﾌｨﾙﾑ種類
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO1))); //ﾃｰﾌﾟﾛｰﾙNo1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO2))); //ﾃｰﾌﾟﾛｰﾙNo2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.ROLL_NO3))); //ﾃｰﾌﾟﾛｰﾙNo3
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO1))); //ﾍﾟｰｽﾄﾛｯﾄNo1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO1))); //ﾍﾟｰｽﾄ粘度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO1))); //ﾍﾟｰｽﾄ温度1
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN1))); //ﾍﾟｰｽﾄ固形分1
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.PASTE_LOT_NO2))); //ﾍﾟｰｽﾄﾛｯﾄNo2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_NENDO2))); //ﾍﾟｰｽﾄ粘度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_ONDO2))); //ﾍﾟｰｽﾄ温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.PASTE_KOKEIBUN2))); //ﾍﾟｰｽﾄ固形分2
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_MEI))); //版胴名
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_NO))); //版胴No
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.SEIHAN_OR_HANDOU_SHIYOU_MAISUU))); //版胴使用枚数 
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.BLADE_NO))); //ﾌﾞﾚｰﾄﾞNo.
        //ﾌﾞﾚｰﾄﾞ外観
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.BLADE_GAIKAN))) {
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
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.BLADE_ATSURYOKU))); //ﾌﾞﾚｰﾄﾞ圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.SQUEEGEE_OR_ATSUDOU_NO))); //圧胴No
        params.add(0); //圧胴使用枚数 //TODO
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.ATSUDOU_ATSURYOKU))); //圧胴圧力
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_GOUKI))); //号機ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI1))); //乾燥温度
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI2))); //乾燥温度2
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI3))); //乾燥温度3
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI4))); //乾燥温度4
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.KANSOU_ONDO_HYOUJICHI5))); //乾燥温度5
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.HANSOU_SOKUDO))); //搬送速度
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_DAY),
                getItemData(itemList, GXHDO101B001Const.INSATSU_KAISHI_TIME))); //ﾌﾟﾘﾝﾄ開始日時
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_STARTJI_TANTOUSHA))); //ｾｯﾃｨﾝｸﾞ担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_AVE))); //ｽﾀｰﾄ時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MAX))); //ｽﾀｰﾄ時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_MIN))); //ｽﾀｰﾄ時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_START_MAKUATSU_CV))); //印刷ｽﾀｰﾄ膜厚CV
        //ｽﾀｰﾄ時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.STARTJI_NIJIMI_KASURE_CHECK))) {
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
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_X_MIN))); //ｽﾀｰﾄ時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_START_Y_MIN))); //ｽﾀｰﾄ時PTN間距離Y
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_KEI))); //開始ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_MAE))); //ﾃﾝｼｮﾝ開始手前
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.KAISHI_TENSION_OKU))); //ﾃﾝｼｮﾝ開始奥
        params.add(DBUtil.stringToDateObject(
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_DAY),
                getItemData(itemList, GXHDO101B001Const.INSATSU_SHUURYOU_TIME))); //ﾌﾟﾘﾝﾄ終了日時

        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.INSATSU_ENDJI_TANTOUSHA))); //終了時担当者ｺｰﾄﾞ
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.INSATSU_MAISUU))); //印刷枚数
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_AVE))); //終了時膜厚AVE
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MAX))); //終了時膜厚MAX
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_MIN))); //終了時膜厚MIN
        params.add(DBUtil.stringToBigDecimalObject(getItemData(itemList, GXHDO101B001Const.INSATSU_END_MAKUATSU_CV))); //印刷ｴﾝﾄﾞ膜厚CV
        //終了時ﾆｼﾞﾐ・ｶｽﾚ確認
        switch (StringUtil.nullToBlank(getItemData(itemList, GXHDO101B001Const.SHUURYOU_JI_NIJIMI_KASURE_CHECK))) {
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

        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_X_MIN))); //終了時PTN間距離X
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.PTN_INSATSU_END_Y_MIN))); //終了時PTN間距離Y
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_KEI))); //終了ﾃﾝｼｮﾝ計
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_MAE))); //ﾃﾝｼｮﾝ終了手前
        params.add(DBUtil.stringToIntObject(getItemData(itemList, GXHDO101B001Const.SHURYOU_TENSION_OKU))); //ﾃﾝｼｮﾝ終了奥
        params.add(0); //印刷ズレ①刷り始め開始 //TODO
        params.add(0); //印刷ズレ②中央開始 //TODO
        params.add(0); //印刷ズレ③刷り終わり開始 //TODO
        params.add(0); //ABズレ平均スタート //TODO
        params.add(0); //印刷ズレ①刷り始め終了 //TODO
        params.add(0); //印刷ズレ②中央終了 //TODO
        params.add(0); //印刷ズレ③刷り終わり終了 //TODO
        params.add(0); //ABズレ平均終了 //TODO
        params.add(DBUtil.stringToStringObject(getItemData(itemList, GXHDO101B001Const.GENRYO_KIGOU))); //原料記号
        params.add(""); //備考1 //TODO
        params.add(""); //備考2 //TODO

        if (isInsert) {
            params.add(systemTime); //登録日時
            //params.add(null); //更新日時 TODO
            params.add(systemTime); //更新日時

        } else {
            params.add(systemTime); //更新日時
        }
        params.add(newRev); //revision

        return params;
    }
    
    
    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ(sr_spsprint_gra)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        // 焼成データの登録
        String sql = "DELETE FROM sr_spsprint_gra "
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面(sub_sr_spsprint_gra)登録処理
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
    private void insertSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "INSERT INTO sub_sr_spsprint_gra ("
                + "kojyo,lotno,edaban,makuatsu_start1,makuatsu_start2,makuatsu_start3,makuatsu_start4,makuatsu_start5,makuatsu_start6,"
                + "makuatsu_start7,makuatsu_start8,makuatsu_start9,start_ptn_dist_x1,start_ptn_dist_x2,start_ptn_dist_x3,"
                + "start_ptn_dist_x4,start_ptn_dist_x5,start_ptn_dist_y1,start_ptn_dist_y2,start_ptn_dist_y3,start_ptn_dist_y4,"
                + "start_ptn_dist_y5,makuatsu_end1,makuatsu_end2,makuatsu_end3,makuatsu_end4,makuatsu_end5,makuatsu_end6,"
                + "makuatsu_end7,makuatsu_end8,makuatsu_end9,end_ptn_dist_x1,end_ptn_dist_x2,end_ptn_dist_x3,end_ptn_dist_x4,"
                + "end_ptn_dist_x5,end_ptn_dist_y1,end_ptn_dist_y2,end_ptn_dist_y3,end_ptn_dist_y4,end_ptn_dist_y5,torokunichiji,"
                + "kosinnichiji,revision"
                + ") VALUES ("
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        List<Object> params = setUpdateParameterSubSrSpsprintGra(true, newRev, kojyo, lotNo, edaban, systemTime);
        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面(sub_sr_spsprint_gra)更新処理
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
    private void updateSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev, BigDecimal newRev,
            String kojyo, String lotNo, String edaban, Timestamp systemTime) throws SQLException {
        String sql = "UPDATE tmp_sub_sr_spsprint_gra SET "
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

        List<Object> params = setUpdateParameterSubSrSpsprintGra(false, newRev, "", "", "", systemTime);

        // 検索条件
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);
        params.add(rev);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        queryRunnerQcdb.update(conQcdb, sql, params.toArray());
    }

    /**
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面登録(tmp_sub_sr_spsprint_gra)更新値ﾊﾟﾗﾒｰﾀ設定
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
    private List<Object> setUpdateParameterSubSrSpsprintGra(boolean isInsert, BigDecimal newRev, String kojyo, String lotNo, String edaban, Timestamp systemTime) {
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
     * 印刷SPSｸﾞﾗﾋﾞｱ_ｻﾌﾞ画面仮登録(tmp_sub_sr_spsprint_gra)削除処理
     *
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param conQcdb ｺﾈｸｼｮﾝ
     * @param rev revision
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @throws SQLException 例外ｴﾗｰ
     */
    private void deleteSubSrSpsprintGra(QueryRunner queryRunnerQcdb, Connection conQcdb, BigDecimal rev,
            String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "DELETE FROM sub_sr_spsprint_gra "
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
     * [印刷SPSｸﾞﾗﾋﾞｱ_仮登録]から最大値+1の削除ﾌﾗｸﾞを取得する
     * @param queryRunnerQcdb QueryRunnerオブジェクト
     * @param kojyo 工場ｺｰﾄﾞ
     * @param lotNo ﾛｯﾄNo
     * @param edaban 枝番
     * @return 削除ﾌﾗｸﾞ最大値 + 1
     * @throws SQLException 例外ｴﾗｰ 
     */
    private int getNewDeleteflag(QueryRunner queryRunnerQcdb, String kojyo, String lotNo, String edaban) throws SQLException {
        String sql = "SELECT MAX(deleteflag) AS deleteflag "
                + "FROM tmp_sr_spsprint_gra "
                + "WHERE KOJYO = ? AND LOTNO = ? AND EDABAN = ? ";
        List<Object> params = new ArrayList<>();
        params.add(kojyo);
        params.add(lotNo);
        params.add(edaban);

        DBUtil.outputSQLLog(sql, params.toArray(), LOGGER);
        Map resultMap = queryRunnerQcdb.query(sql, new MapHandler(), params.toArray());
        int newDeleteFlg = 0;
        if(!StringUtil.isEmpty(StringUtil.nullToBlank(resultMap.get("deleteflag")))){
           newDeleteFlg = Integer.parseInt(StringUtil.nullToBlank(resultMap.get("deleteflag")));
        }
        newDeleteFlg++;
        
        return newDeleteFlg;
    }

}
